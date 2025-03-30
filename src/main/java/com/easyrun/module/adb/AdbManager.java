package com.easyrun.module.adb;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import org.json.JSONArray;
import org.json.JSONObject;

import com.easyrun.CallbackContext;
import com.easyrun.LogController;
import com.easyrun.environment.EnvironmentInitializer;

public class AdbManager {

    private static final LogController logger = new LogController();

    private static final String PLATFORM_TOOLS_DIR = "lib/platform-tools";

    /**
     * Retourne le chemin complet vers l'exécutable adb correspondant à l'OS courant.
     */
    public static String getAdbPath() {
        String os = EnvironmentInitializer.getOS(); // "Win", "MAC" ou "Linux"
        String adbExecutable;
        if (os.equals("Win")) {
            adbExecutable = "adb.exe";
        } else if (os.equals("MAC") || os.equals("Linux")) {
            adbExecutable = "adb";
        } else {
            throw new RuntimeException("OS non supporté : " + os);
        }
        // Construit le chemin complet : par exemple "lib/platform-tools/Win/adb.exe"
        String adbPath = PLATFORM_TOOLS_DIR + File.separator + os + File.separator + adbExecutable;
        return adbPath;
    }

    public AdbManager(String methodName, CallbackContext callbackContext, Object[] args) {
        switch (methodName) {
            case "getPdaList" -> getPdaList(callbackContext);
            // case "executeRequest" -> executeRequest(callbackContext, args);
            default -> {
                JSONObject errorMessage = new JSONObject();
                errorMessage.put("message", "La méthode " + methodName + " n'existe pas");
                callbackContext.error(errorMessage);
            }
        }
    }

    /**
     * Récupère la liste des PDA en exécutant "adb devices -l" et enrichit les informations
     * via des commandes adb complémentaires.
     */
    public static CallbackContext getPdaList(CallbackContext callbackContext) {
        JSONObject result = new JSONObject();
        try {
            String adbPath = getAdbPath();
            ProcessBuilder pb = new ProcessBuilder(adbPath, "devices", "-l");
            Process process = pb.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            JSONArray devicesArray = new JSONArray();
            boolean headerSkipped = false;

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!headerSkipped) { // ignore la première ligne ("List of devices attached")
                    headerSkipped = true;
                    continue;
                }
                if (line.isEmpty()) continue;
                String[] tokens = line.split("\\s+");
                if (tokens.length < 2) continue;

                String deviceSerial = tokens[0];
                // Récupère les infos complémentaires pour chaque PDA
                String model = getPdaModel(deviceSerial);
                String serialNumber = getPdaSerialNumber(deviceSerial);
                String androidVersion = getPdaAndroidVersion(deviceSerial);

                JSONObject device = new JSONObject();
                device.put("deviceSerial", deviceSerial);
                device.put("model", model);
                device.put("androidVersion", androidVersion);
                devicesArray.put(device);
            }
            result.put("data", devicesArray);
            callbackContext.success(result);
        } catch (Exception e) {
            JSONObject error = new JSONObject();
            error.put("message", e.getMessage());
            callbackContext.error(error);
        }
        return callbackContext;
    }

    /**
     * Récupère le modèle du PDA via "adb -s <serial> shell getprop ro.product.model"
     */
    private static String getPdaModel(String serial) {
        return executeAdbShellCommand(serial, "ro.product.model");
    }

    /**
     * Récupère le numéro de série du PDA via "adb -s <serial> shell getprop ro.serialno"
     */
    private static String getPdaSerialNumber(String serial) {
        return executeAdbShellCommand(serial, "ro.serialno");
    }

    /**
     * Récupère la version EM du PDA via "adb -s <serial> shell getprop ro.em.version"
     * (modifiez la propriété si nécessaire)
     */
    private static String getPdaEMVersion(String serial) {
        return executeAdbShellCommand(serial, "ro.em.version");
    }

    /**
     * Récupère la version Android du PDA via "adb -s <serial> shell getprop ro.build.version.release"
     */
    private static String getPdaAndroidVersion(String serial) {
        return executeAdbShellCommand(serial, "ro.build.version.release");
    }

    public static JSONObject getAdbVersionInfo() {
        JSONObject versionInfo = new JSONObject();
        try {
            // Récupère le chemin vers l'exécutable adb (méthode déjà implémentée dans votre AdbManager)
            String adbPath = getAdbPath();
            ProcessBuilder pb = new ProcessBuilder(adbPath, "--version");
            Process process = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            String adbVersion = "";
            String buildVersion = "";
            String installedPath = "";
            
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("Android Debug Bridge version")) {
                    // Exemple : "Android Debug Bridge version 1.0.41"
                    String[] parts = line.split("version");
                    if (parts.length > 1) {
                        adbVersion = parts[1].trim();
                    }
                } else if (line.startsWith("Version")) {
                    // Exemple : "Version 29.0.6-6198805"
                    String[] parts = line.split("Version");
                    if (parts.length > 1) {
                        buildVersion = parts[1].trim();
                    }
                } else if (line.startsWith("Installed as")) {
                    // Exemple : "Installed as C:\SDK\Android\CURRENT_ANDROID\platform-tools\adb.exe"
                    String[] parts = line.split("Installed as");
                    if (parts.length > 1) {
                        installedPath = parts[1].trim();
                    }
                }
            }
            
            versionInfo.put("adbVersion", adbVersion);
            versionInfo.put("buildVersion", buildVersion);
            versionInfo.put("installedPath", installedPath);
        } catch (Exception e) {
            versionInfo.put("error", e.getMessage());
        }
        return versionInfo;
    }    

    /**
     * Méthode utilitaire pour exécuter une commande adb shell qui récupère une propriété donnée.
     */
    private static String executeAdbShellCommand(String serial, String property) {
        String adbPath = getAdbPath();
        try {
            ProcessBuilder pb = new ProcessBuilder(adbPath, "-s", serial, "shell", "getprop", property);
            Process process = pb.start();
            return readProcessOutput(process).trim();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Lit la sortie d'un Process et retourne le résultat sous forme de String.
     */
    private static String readProcessOutput(Process process) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder output = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line);
        }
        return output.toString();
    }
}
