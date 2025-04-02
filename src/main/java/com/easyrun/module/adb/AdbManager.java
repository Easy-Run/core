package com.easyrun.module.adb;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.easyrun.CallbackContext;
import com.easyrun.LogController;
import com.easyrun.database.application.model.Application;
import com.easyrun.database.application.repository.ApplicationRepository;
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
        String adbPath = PLATFORM_TOOLS_DIR + File.separator + os + File.separator + adbExecutable;
        return adbPath;
    }

    public AdbManager(String methodName, CallbackContext callbackContext, Object[] args) {
        switch (methodName) {
            case "getPdaList" -> getPdaList(callbackContext);
            case "launchScrcpy" -> launchScrcpy(callbackContext, args);
            case "launchApp" -> launchApp(callbackContext, args);
            case "uninstallApp" -> uninstallApp(callbackContext, args);
            case "clearApp" -> clearApp(callbackContext, args);
            case "runCordova" -> runCordova(callbackContext, args);
            default -> {
                JSONObject errorMessage = new JSONObject();
                errorMessage.put("message", "La méthode " + methodName + " n'existe pas");
                callbackContext.error(errorMessage);
            }
        }
    }

    public static void launchScrcpy(CallbackContext callbackContext, Object[] args) {
        try {
            if (args == null || args.length == 0) {
                JSONObject errorMessage = new JSONObject();
                errorMessage.put("message", "Le numéro de série est requis.");
                callbackContext.error(errorMessage);
                return;
            }

            String serialNumber = args[0].toString();

            // Récupère le dossier racine de l'application
            String npmDir = System.getProperty("user.dir");
            // Construit le chemin complet vers scrcpy.exe
            String executablePath = npmDir + File.separator + "lib" + File.separator + "scrcpy" + File.separator + "scrcpy.exe";
            
            // Prépare le ProcessBuilder avec les arguments "-s" et le numéro de série
            ProcessBuilder pb = new ProcessBuilder(executablePath, "-s", serialNumber);
            // Redirige la sortie d'erreur sur la sortie standard
            pb.redirectErrorStream(true);
            // Démarre le processus (détaché)
            Process process = pb.start();
            
            // Lit la sortie du processus dans un thread séparé
            StringBuilder output = new StringBuilder();
            Thread outputReader = new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        output.append(line).append("\n");
                        // System.out.println(line);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            outputReader.start();

            // Attendre la fin du processus
            int exitCode = process.waitFor();
            outputReader.join();

            if (exitCode != 0) {
                String errorMsg = "Erreur lors du lancement de scrcpy : " + output.toString();
                throw new RuntimeException(errorMsg);
            } else {
                // Sinon, on renvoie la sortie en guise de succès.
                callbackContext.success(output.toString());
            }    
            
        } catch (Exception e) {
            JSONObject error = new JSONObject();
            error.put("message", e.getMessage());
            callbackContext.error(error);
        }
    }

    public static void launchApp(CallbackContext callbackContext, Object[] args) {
        try {
            if (args == null || args.length == 0) {
                JSONObject errorMessage = new JSONObject();
                errorMessage.put("message", "Le nom du package est requis.");
                callbackContext.error(errorMessage);
                return;
            }
    
            String packageName = args[0].toString();

            String deviceSerial = args[1].toString();
    
            String adbPath = getAdbPath();
    
            // Prépare la commande pour lancer l'application via la MainActivity
            // Remarque : cela suppose que l'activité principale est nommée "MainActivity"
            String component = packageName + "/.MainActivity";
            ProcessBuilder pb = new ProcessBuilder(adbPath, "-s", deviceSerial, "shell", "am", "start", "-n", component);
            pb.redirectErrorStream(true);
            Process process = pb.start();
    
            StringBuilder output = new StringBuilder();
            Thread outputReader = new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        output.append(line).append("\n");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            outputReader.start();
            int exitCode = process.waitFor();
            outputReader.join();
    
            if (exitCode != 0) {
                throw new RuntimeException("Erreur lors du lancement de l'application : " + output.toString());
            } else {
                callbackContext.success(output.toString());
            }
        } catch (Exception e) {
            JSONObject error = new JSONObject();
            error.put("message", e.getMessage());
            callbackContext.error(error);
        }
    }
    
    public static void uninstallApp(CallbackContext callbackContext, Object[] args) {
        try {
            if (args == null || args.length == 0) {
                JSONObject errorMessage = new JSONObject();
                errorMessage.put("message", "Le nom du package est requis.");
                callbackContext.error(errorMessage);
                return;
            }
    
            String packageName = args[0].toString();

            String deviceSerial = args[1].toString();
    
            String adbPath = getAdbPath();
    
            // Prépare la commande pour désinstaller l'application
            ProcessBuilder pb = new ProcessBuilder(adbPath, "-s", deviceSerial, "uninstall", packageName);
            pb.redirectErrorStream(true);
            Process process = pb.start();
    
            StringBuilder output = new StringBuilder();
            Thread outputReader = new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        output.append(line).append("\n");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            outputReader.start();
            int exitCode = process.waitFor();
            outputReader.join();
    
            if (exitCode != 0) {
                throw new RuntimeException("Erreur lors de la désinstallation de l'application : " + output.toString());
            } else {
                callbackContext.success(output.toString());
            }
        } catch (Exception e) {
            JSONObject error = new JSONObject();
            error.put("message", e.getMessage());
            callbackContext.error(error);
        }
    }

    public static void clearApp(CallbackContext callbackContext, Object[] args) {
        try {
            // Vérifier que nous avons au moins 2 arguments (nom du package et numéro de série)
            if (args == null || args.length < 2) {
                JSONObject errorMessage = new JSONObject();
                errorMessage.put("message", "Le nom du package et le numéro de série sont requis.");
                callbackContext.error(errorMessage);
                return;
            }
            
            // Récupérer les arguments
            String packageName = args[0].toString();
            String deviceSerial = args[1].toString();
            
            // Obtenir le chemin vers adb.exe (la méthode getAdbPath() doit être définie ailleurs dans votre code)
            String adbPath = getAdbPath();
            
            // Préparer la commande adb pour vider les données de l'application (pm clear)
            ProcessBuilder pb = new ProcessBuilder(adbPath, "-s", deviceSerial, "shell", "pm", "clear", packageName);
            pb.redirectErrorStream(true); // Redirige la sortie d'erreur vers la sortie standard
            
            // Démarrer le processus
            Process process = pb.start();
            
            // Utiliser un StringBuilder pour construire la sortie du processus
            StringBuilder output = new StringBuilder();
            
            // Créer un thread pour lire la sortie du processus
            Thread outputReader = new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        output.append(line).append("\n");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            outputReader.start();
            
            // Attendre que le processus se termine
            int exitCode = process.waitFor();
            // Attendre que le thread de lecture ait fini
            outputReader.join();
            
            // Si le code de sortie n'est pas 0, c'est qu'il y a eu une erreur
            if (exitCode != 0) {
                throw new RuntimeException("Erreur lors du clear de l'application : " + output.toString());
            } else {
                // Sinon, renvoyer la sortie comme succès
                callbackContext.success(output.toString());
            }
        } catch (Exception e) {
            JSONObject error = new JSONObject();
            error.put("message", e.getMessage());
            callbackContext.error(error);
        }
    }

    public static void runCordova(CallbackContext callbackContext, Object[] args) {
        try {
            // Vérifier qu'on a au moins un argument (le numéro de série)
            if (args == null || args.length == 0) {
                JSONObject errorMessage = new JSONObject();
                errorMessage.put("message", "Le numéro de série est requis.");
                callbackContext.error(errorMessage);
                return;
            }
    
            // Récupérer le numéro de série
            String serialNumber = args[0].toString();

            List<String> command = new ArrayList<>();
            if (EnvironmentInitializer.getOS().contains("Win")) {
                command.add("cordova.cmd");
            } else {
                command.add("cordova");
            }
            command.add("run");
            command.add("android");
            command.add("--target=" + serialNumber);

            ProcessBuilder pb = new ProcessBuilder(command);

            Integer idApplication = (args.length > 1 && args[1] != null) ? (Integer) args[1] : null;

            if (idApplication != null) {
                ApplicationRepository repository = new ApplicationRepository();
                Application app = repository.findById(idApplication);
                
                if (app != null && app.getBuildPath() != null && !app.getBuildPath().isEmpty()) {
                    File buildDir = new File(app.getBuildPath());
                    pb.directory(buildDir);
                }
            }

            pb.redirectErrorStream(true);
    
            // Démarrer le processus
            Process process = pb.start();
    
            // Utiliser un StringBuilder pour construire la sortie du processus
            StringBuilder output = new StringBuilder();
    
            // Créer un thread pour lire la sortie en continu du processus
            Thread outputReader = new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        output.append(line).append("\n");
                        // Vous pouvez aussi afficher la sortie en continu avec System.out.println(line);

                        JSONObject progressObj = new JSONObject();
                        progressObj.put("progressMessage", line);
                        callbackContext.progress(progressObj);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            outputReader.start();
    
            // Attendre la fin du processus
            int exitCode = process.waitFor();
            // Attendre que le thread de lecture ait terminé
            outputReader.join();
    
            // Vérifier le code de sortie
            if (exitCode != 0) {
                throw new RuntimeException("Erreur lors de l'exécution de cordova run : " + output.toString());
            } else {
                callbackContext.success(output.toString());
            }
        } catch (Exception e) {
            JSONObject error = new JSONObject();
            error.put("message", e.getMessage());
            callbackContext.error(error);
        }
    }

    /**
     * Récupère la liste des PDA en exécutant "adb devices -l" et enrichit les informations
     * via des commandes adb complémentaires.
     */
    public static void getPdaList(CallbackContext callbackContext) {
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
