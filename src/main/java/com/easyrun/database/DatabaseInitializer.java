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

public class DatabaseInitializer {

    private static final LogController logger = new LogController();

    public DatabaseInitializer(String methodName, CallbackContext callbackContext, Object[] args) {
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

    /**
     * Initialise la base de données : création du fichier, mise en place du schéma et insertion de données initiales.
     */
    public static void initialize(CallbackContext callbackContext) {
        JSONObject result = new JSONObject();
        try {
            runMigrations();
            updateAppDirParameter();

            result.put("message", "Initialisation réussi");

            callbackContext.success(result);
        } catch (Exception e) {
            result.put("message", e.getMessage());
            callbackContext.error(result);
        }
    }

    private static void runMigrations() {
        // Récupère le chemin du dossier core_db et assure son existence
        String coreDbDir = EnvironmentInitializer.getCoreDbDir();
        new File(coreDbDir).mkdirs();
        String dbFilePath = coreDbDir + File.separator + "easyrun.db";
        String jdbcUrl = "jdbc:sqlite:" + dbFilePath;

        // Configure Flyway
        Flyway flyway = Flyway.configure()
            .dataSource(jdbcUrl, null, null)
            .locations("classpath:db/migration")
            .load();

        // Exécute les migrations
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
     * Exécute une requête SQL avec des paramètres positionnels et renvoie le résultat via le CallbackContext.
     * Supporte les requêtes SELECT, INSERT, UPDATE et DELETE.
     *
     * @param sql la requête SQL, avec des paramètres indiqués par "?".
     * @param params un tableau d'objets représentant les paramètres.
     * @param callbackContext le callback à invoquer avec le résultat ou l'erreur.
     */
    public static void executeRequest(CallbackContext callbackContext, Object[] args) {
        String sql = (String) args[0];
        Object[] params;

        if (args[1] instanceof java.util.List) {
            params = ((java.util.List<?>) args[1]).toArray();
        } else {
            params = (Object[]) args[1];
        }    

        String coreDbDir = EnvironmentInitializer.getCoreDbDir();
        String dbFilePath = coreDbDir + File.separator + "easyrun.db";
        String jdbcUrl = "jdbc:sqlite:" + dbFilePath;
        
        try (Connection conn = DriverManager.getConnection(jdbcUrl);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Associe les paramètres au PreparedStatement
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    stmt.setObject(i + 1, params[i]);
                }
            }
            
            // Si la requête est un SELECT, exécute executeQuery()
            if (sql.trim().toUpperCase().startsWith("SELECT")) {
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
