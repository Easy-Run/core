package com.easyrun.environment;

import java.io.File;
import org.json.JSONObject;
import com.easyrun.CallbackContext;
import com.easyrun.core.AppContext;

public class EnvironmentInitializer {

    public EnvironmentInitializer(String methodName, CallbackContext callbackContext, Object[] args) {
        if ("initialize".equals(methodName)) {
            JSONObject ctx = null;
            if (args != null && args.length > 0 && args[0] instanceof java.util.Map<?, ?> map) {
                ctx = new JSONObject((java.util.Map<?, ?>) map);
            }
            initialize(callbackContext, ctx);
        }
    }

    private static final String HOME_DIR = System.getProperty("user.home");
    private static final String OS_NAME = System.getProperty("os.name").toLowerCase();

    // === OS helpers ===
    private static String getEasyRunRoot() {
        if (OS_NAME.contains("win")) {
            String localAppData = System.getenv("LOCALAPPDATA");
            return (localAppData != null ? localAppData : HOME_DIR) + File.separator + "easyrun";
        } else if (OS_NAME.contains("mac")) {
            return HOME_DIR + File.separator + "Library" + File.separator + "Application Support" + File.separator + "easyrun";
        } else {
            String xdgConfig = System.getenv("XDG_CONFIG_HOME");
            if (xdgConfig == null || xdgConfig.isEmpty()) {
                xdgConfig = HOME_DIR + File.separator + ".config";
            }
            return xdgConfig + File.separator + "easyrun";
        }
    }

    public static String getOS() {
        if (OS_NAME.contains("win")) return "Win";
        if (OS_NAME.contains("mac")) return "MAC";
        if (OS_NAME.contains("nux") || OS_NAME.contains("nix") || OS_NAME.contains("aix")) return "Linux";
        return "Unknown";
    }

    // === Chemins dynamiques, namespacés par AppContext ===
    public static String getEasyrunDir() {
        String root = getEasyRunRoot();
        String app   = AppContext.getAppId();   if (app == null || app.isEmpty()) app = "default";
        String prof  = AppContext.getProfile(); if (prof == null || prof.isEmpty()) prof = "default";
        return root + File.separator + app + File.separator + prof;
    }

    public static String getCoreDbDir() {
        return getEasyrunDir() + File.separator + "core_data";
    }

    public static String getLogsDir() {
        return getEasyrunDir() + File.separator + "logs";
    }

    public static String getPdaDir() {
        return getEasyrunDir() + File.separator + "pda";
    }

    public static void initialize(CallbackContext callbackContext, JSONObject ctx) {
        JSONObject result = new JSONObject();
        try {
            // Appliquer le contexte si fourni
            if (ctx != null) {
                AppContext.set(ctx.optString("appId", null),
                               ctx.optString("profile", null),
                               ctx.optString("baseDir", null));
            }

            // Créer les dossiers dynamiquement
            createDirectory(getEasyrunDir());
            createDirectory(getLogsDir());
            createDirectory(getPdaDir());
            createDirectory(getCoreDbDir());

            result.put("message", "Initialisation réussi");
            result.put("appId", AppContext.getAppId());
            result.put("profile", AppContext.getProfile());
            result.put("baseDir", getEasyrunDir());
            callbackContext.success(result);
        } catch (Exception e) {
            result.put("message", e.getMessage());
            callbackContext.error(result);
        }
    }

    private static void createDirectory(String path) {
        File dir = new File(path);
        if (!dir.exists()) dir.mkdirs();
    }
}
