package com.easyrun.environment;

import java.io.File;

public class EnvironmentInitializer {

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

    private static final String EASYRUN_DIR = getEasyRunDir();
    private static final String LOGS_DIR = EASYRUN_DIR + File.separator + "logs";
    private static final String PDA_DATABASES_DIR = EASYRUN_DIR + File.separator + "pda_databases";
    private static final String CORE_DB_DIR = EASYRUN_DIR + File.separator + "core_data";

    public static void initialize() {
        createDirectory(EASYRUN_DIR);
        createDirectory(LOGS_DIR);
        createDirectory(PDA_DATABASES_DIR);
        createDirectory(CORE_DB_DIR);
    }

    private static void createDirectory(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            if (dir.mkdirs()) {
                System.out.println("Dossier créé : " + path);
            } else {
                System.err.println("Erreur lors de la création du dossier : " + path);
            }
        } else {
            System.out.println("Dossier déjà existant : " + path);
        }
    }

    public static void main(String[] args) {
        initialize();
    }
}