package com.easyrun.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flywaydb.core.Flyway;
import org.json.JSONObject;

import com.easyrun.CallbackContext;
import com.easyrun.LogController;
import com.easyrun.environment.EnvironmentInitializer;
import com.easyrun.core.AppContext; // <-- ajout

public class DatabaseInitializer {

    private static final LogController logger = new LogController();

    public DatabaseInitializer(String methodName, CallbackContext callbackContext, Object[] args) {
        logger.info("DatabaseInitializer");
        // 0) Appliquer le contexte si fourni en args[0] (sinon ne rien changer)
        applyContextIfPresent(args);

        // 1) Router
        switch (methodName) {
            case "initialize" -> initialize(callbackContext);
            case "executeRequest" -> executeRequest(callbackContext, args);
            default -> {
                JSONObject errorMessage = new JSONObject();
                errorMessage.put("message", "La méthode " + methodName + " n'existe pas");
                callbackContext.error(errorMessage);
            }
        }
    }

    /** Détecte un contexte en args[0] (Map/JSONObject) et l'applique à AppContext. */
    @SuppressWarnings("unchecked")
    private void applyContextIfPresent(Object[] args) {
        if (args == null || args.length == 0) return;

        Object a0 = args[0];

        // Cas courant: JSONArray côté TS -> ici une Map brute
        if (a0 instanceof java.util.Map<?, ?> raw) {
            JSONObject ctx = new JSONObject((Map<String, Object>) raw);
            String appId   = ctx.optString("appId",   null);
            String profile = ctx.optString("profile", null);
            String baseDir = ctx.optString("baseDir", null);

            if (appId != null || profile != null || baseDir != null) {
                AppContext.set(appId, profile, baseDir);
            }
        } else if (a0 instanceof JSONObject ctx) {
            String appId   = ctx.optString("appId",   null);
            String profile = ctx.optString("profile", null);
            String baseDir = ctx.optString("baseDir", null);

            if (appId != null || profile != null || baseDir != null) {
                AppContext.set(appId, profile, baseDir);
            }
        }
        // Sinon: pas de contexte → on laisse tel quel (rétro-compat)
    }

    /**
     * Initialise la base de données : création du fichier, mise en place du schéma et insertion de données initiales.
     */
    public static void initialize(CallbackContext callbackContext) {
        JSONObject result = new JSONObject();
        try {
            runMigrations();
            updateAppDirParameter();

            result.put("message", "Initialisation réussi");
            // (optionnel) debug: retourner le contexte/dir effectif
            result.put("appId", AppContext.getAppId());
            result.put("profile", AppContext.getProfile());
            result.put("baseDir", EnvironmentInitializer.getEasyrunDir());

            callbackContext.success(result);
        } catch (Exception e) {
            result.put("message", e.getMessage());
            callbackContext.error(result);
        }
    }

    private static void runMigrations() {
        String coreDbDir = EnvironmentInitializer.getCoreDbDir();
        new File(coreDbDir).mkdirs();
        String dbFilePath = coreDbDir + File.separator + "easyrun.db";
        String jdbcUrl = "jdbc:sqlite:" + dbFilePath;

        Flyway flyway = Flyway.configure()
            .dataSource(jdbcUrl, null, null)
            .locations("classpath:db/migration")
            .load();

        flyway.migrate();
    }

    private static void updateAppDirParameter() {
        String appDirValue = EnvironmentInitializer.getEasyrunDir();
        String coreDbDir = EnvironmentInitializer.getCoreDbDir();
        String dbFilePath = coreDbDir + File.separator + "easyrun.db";
        String jdbcUrl = "jdbc:sqlite:" + dbFilePath;

        String updateSQL = "UPDATE config_param SET value = ? WHERE code = 'APP_DIR'";

        try (Connection conn = DriverManager.getConnection(jdbcUrl);
             PreparedStatement stmt = conn.prepareStatement(updateSQL)) {
            stmt.setString(1, appDirValue);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du paramètre APP_DIR : " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Exécute une requête SQL. Supporte deux formes d'arguments:
     *  - NOUVELLE: [ ctx, { sql, params } ]
     *  - ANCIENNE: [ sql, params ]
     */
    @SuppressWarnings("unchecked")
    public static void executeRequest(CallbackContext callbackContext, Object[] args) {
        String sql = null;
        Object[] paramsArr = null;

        int payloadIndex = 0;

        // Si args[0] ressemble à un contexte, on déplace le payload sur args[1]
        if (args != null && args.length > 0) {
            Object a0 = args[0];
            if (a0 instanceof java.util.Map<?, ?> raw0) {
                JSONObject maybeCtx = new JSONObject((Map<String, Object>) raw0);
                if (maybeCtx.has("appId") || maybeCtx.has("profile") || maybeCtx.has("baseDir")) {
                    payloadIndex = 1; // nouvelle signature
                }
            } else if (a0 instanceof JSONObject maybeCtx) {
                if (maybeCtx.has("appId") || maybeCtx.has("profile") || maybeCtx.has("baseDir")) {
                    payloadIndex = 1;
                }
            }
        }

        // Lire le payload
        Object payload = (args != null && args.length > payloadIndex) ? args[payloadIndex] : null;

        if (payload instanceof java.util.Map<?, ?> rawPayload) {
            // Nouvelle forme: payload = { sql, params }
            JSONObject obj = new JSONObject((Map<String, Object>) rawPayload);
            sql = obj.optString("sql", null);

            Object p = obj.opt("params");
            if (p instanceof java.util.List<?> list) {
                paramsArr = list.toArray();
            } else if (p instanceof Object[]) {
                paramsArr = (Object[]) p;
            } else if (p == null) {
                paramsArr = null;
            } else {
                // param unique non-list → le passer tel quel
                paramsArr = new Object[] { p };
            }
        } else if (payload instanceof String) {
            // Ancienne forme: args[payloadIndex] = sql, args[payloadIndex+1] = params
            sql = (String) payload;
            Object rawParams = (args != null && args.length > payloadIndex + 1) ? args[payloadIndex + 1] : null;

            if (rawParams instanceof java.util.List<?> list) {
                paramsArr = list.toArray();
            } else if (rawParams instanceof Object[]) {
                paramsArr = (Object[]) rawParams;
            } else if (rawParams == null) {
                paramsArr = null;
            } else {
                paramsArr = new Object[] { rawParams };
            }
        } else {
            JSONObject errorMessage = new JSONObject();
            errorMessage.put("message", "Arguments invalides pour executeRequest");
            callbackContext.error(errorMessage);
            return;
        }

        // Résolution DB
        String coreDbDir = EnvironmentInitializer.getCoreDbDir();
        String dbFilePath = coreDbDir + File.separator + "easyrun.db";
        String jdbcUrl = "jdbc:sqlite:" + dbFilePath;

        try (Connection conn = DriverManager.getConnection(jdbcUrl);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Bind paramètres
            if (paramsArr != null) {
                for (int i = 0; i < paramsArr.length; i++) {
                    stmt.setObject(i + 1, paramsArr[i]);
                }
            }

            // SELECT vs non-SELECT
            if (sql != null && sql.trim().toUpperCase().startsWith("SELECT")) {
                try (ResultSet rs = stmt.executeQuery()) {
                    List<Map<String, Object>> results = new ArrayList<>();
                    ResultSetMetaData meta = rs.getMetaData();
                    int columnCount = meta.getColumnCount();

                    while (rs.next()) {
                        Map<String, Object> row = new HashMap<>();
                        for (int i = 1; i <= columnCount; i++) {
                            row.put(meta.getColumnName(i), rs.getObject(i));
                        }
                        results.add(row);
                    }

                    JSONObject jsonResults = new JSONObject();
                    jsonResults.put("data", results);
                    callbackContext.success(jsonResults);
                }
            } else {
                int updateCount = stmt.executeUpdate();
                callbackContext.success("Lignes affectées : " + updateCount);
            }
        } catch (SQLException e) {
            JSONObject errorMessage = new JSONObject();
            errorMessage.put("message", "Erreur lors de l'exécution de la requête SQL : " + e.getMessage());
            callbackContext.error(errorMessage);
        }
    }
}
