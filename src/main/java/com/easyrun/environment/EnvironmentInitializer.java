package com.easyrun.environment;

import java.io.File;

import org.json.JSONArray;
import org.json.JSONObject;

import com.easyrun.CallbackContext;

public class EnvironmentInitializer {

    public EnvironmentInitializer(String methodName, CallbackContext callbackContext, Object[] args) {
        if ("initialize".equals(methodName)) {
            initialize(callbackContext);
        }
    }

    //* le répertoire home de l'utilisateur */
    private static final String HOME_DIR = System.getProperty("user.home");
    //* le nom de l'OS */
    private static final String OS_NAME = System.getProperty("os.name").toLowerCase();

    private static String getEasyRunDir() {
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
        if (OS_NAME.contains("win")) {
            return "Win";
        } else if (OS_NAME.contains("mac")) {
            return "MAC";
        } else if (OS_NAME.contains("nux") || OS_NAME.contains("nix") || OS_NAME.contains("aix")) {
            return "Linux";
        } else {
            return "Unknown";
        }
    }

    private static final String EASYRUN_DIR = getEasyRunDir();
    private static final String LOGS_DIR = EASYRUN_DIR + File.separator + "logs";
    private static final String PDA_DATABASES_DIR = EASYRUN_DIR + File.separator + "pda_databases";
    private static final String CORE_DB_DIR = EASYRUN_DIR + File.separator + "core_data";

    public static void initialize(CallbackContext callbackContext) {
        JSONObject result = new JSONObject();
        try {
            createDirectory(EASYRUN_DIR);
            createDirectory(LOGS_DIR);
            createDirectory(PDA_DATABASES_DIR);
            createDirectory(CORE_DB_DIR);

            result.put("message", "Initialisation réussi");
            callbackContext.success(result);
        } catch (Exception e) {
            result.put("message", e.getMessage());
            callbackContext.error(result);
        }
    }

    private static void createDirectory(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            if (dir.mkdirs()) {
                // System.out.println("Dossier créé : " + path);
            } else {
                // System.err.println("Erreur lors de la création du dossier : " + path);
            }
        } else {
            // System.out.println("Dossier déjà existant : " + path);
        }
    }

    public static String getCoreDbDir() {
        return CORE_DB_DIR;
    }

    public static String getEasyrunDir() {
        return EASYRUN_DIR;
    }
}