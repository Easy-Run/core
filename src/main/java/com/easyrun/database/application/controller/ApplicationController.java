package com.easyrun.database.application.controller;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.easyrun.CallbackContext;
import com.easyrun.LogController;
import com.easyrun.database.application.repository.ApplicationRepository;
import com.easyrun.database.application.repository.ApplicationRepository.CrudResult;
import com.easyrun.database.application.model.Application;

public class ApplicationController {
    private static final LogController logger = new LogController();

    public ApplicationController(String methodName, CallbackContext callbackContext, Object[] args) {
        switch (methodName) {
            case "getApplicationList" -> getApplicationList(callbackContext);
            case "getDefaultApplication" -> getDefaultApplication(callbackContext);
            case "getApplicationById" -> getApplicationById(callbackContext, args);
            case "createApplication" -> createApplication(callbackContext, args);
            case "updateApplication" -> updateApplication(callbackContext, args);
            case "deleteApplication" -> deleteApplication(callbackContext, args);
            // case "updateConfigValue" -> updateConfigValue(callbackContext, args);
            default -> {
                JSONObject errorMessage = new JSONObject();
                errorMessage.put("message", "La méthode " + methodName + " n'existe pas");
                callbackContext.error(errorMessage);
            }
        }
    }

    public static void getApplicationList(CallbackContext callbackContext) {
        JSONObject errorMessage = new JSONObject();
        try {
            ApplicationRepository appRepo = new ApplicationRepository();
            
            List<Application> apps = appRepo.findAll();
            
            JSONObject result = new JSONObject();
            result.put("data", apps);
            callbackContext.success(result);
        } catch (Exception e) {
            errorMessage.put("message", e.getMessage());
            callbackContext.error(errorMessage);
        }
    }

    // Retourne l'application par défaut
    public static void getDefaultApplication(CallbackContext callbackContext) {
        JSONObject result = new JSONObject();
        try {
            ApplicationRepository appRepo = new ApplicationRepository();
            Application app = appRepo.findDefault();
            result.put("data", app);
            callbackContext.success(result);
        } catch (Exception e) {
            JSONObject errorMessage = new JSONObject();
            errorMessage.put("message", e.getMessage());
            callbackContext.error(errorMessage);
        }
    }

    // Retourne une application à partir de son id (attendu en premier argument dans args)
    public static void getApplicationById(CallbackContext callbackContext, Object[] args) {
        JSONObject result = new JSONObject();
        try {
            if (args == null || args.length == 0) {
                throw new IllegalArgumentException("L'id de l'application est requis.");
            }
            int id = Integer.parseInt(args[0].toString());
            ApplicationRepository appRepo = new ApplicationRepository();
            Application app = appRepo.findById(id);
            result.put("data", app);
            callbackContext.success(result);
        } catch (Exception e) {
            JSONObject errorMessage = new JSONObject();
            errorMessage.put("message", e.getMessage());
            callbackContext.error(errorMessage);
        }
    }

    // Crée une nouvelle application (les données attendues dans args[0] sous forme de JSONObject)
    public static void createApplication(CallbackContext callbackContext, Object[] args) {
        JSONObject result = new JSONObject();
        try {
            if (args == null || args.length == 0) {
                throw new IllegalArgumentException("Les données de l'application sont requises.");
            }
            JSONObject appData = new JSONObject(args[0].toString());
            Application app = new Application();
            // Si le name n'est pas fourni, il peut rester null
            if (appData.has("name")) {
                app.setName(appData.getString("name"));
            }
            // package_name est obligatoire
            app.setPackageName(appData.getString("package_name"));
            // Pour is_active et is_default, on s'attend à des booléens
            app.setIsActive(appData.optBoolean("is_active", true));
            if (appData.has("is_default")) {
                app.setIsDefault(appData.getBoolean("is_default"));
            } else {
                app.setIsDefault(false);
            }
            if (appData.has("build_path")) {
                app.setBuildPath(appData.getString("build_path"));
            }
            ApplicationRepository appRepo = new ApplicationRepository();
            CrudResult crudResult = appRepo.create(app);
            result.put("affectedRows", crudResult.getAffectedRows());
            // On renvoie également l'application créée avec son id généré
            result.put("data", app);
            callbackContext.success(result);
        } catch (Exception e) {
            JSONObject errorMessage = new JSONObject();
            errorMessage.put("message", e.getMessage());
            callbackContext.error(errorMessage);
        }
    }

    // Met à jour une application existante (les données attendues dans args[0] sous forme de JSONObject)
    public static void updateApplication(CallbackContext callbackContext, Object[] args) {
        JSONObject result = new JSONObject();
        try {
            if (args == null || args.length == 0) {
                throw new IllegalArgumentException("Les données de l'application à mettre à jour sont requises.");
            }
            JSONObject appData = new JSONObject(args[0].toString());
            Application app = new Application();
            if (!appData.has("id")) {
                throw new IllegalArgumentException("L'id de l'application est requis pour la mise à jour.");
            }
            app.setId(appData.getInt("id"));
            if (appData.has("name")) {
                app.setName(appData.getString("name"));
            }
            app.setPackageName(appData.getString("package_name"));
            app.setIsActive(appData.optBoolean("is_active", true));
            if (appData.has("is_default")) {
                app.setIsDefault(appData.getBoolean("is_default"));
            } else {
                app.setIsDefault(false);
            }
            if (appData.has("build_path")) {
                app.setBuildPath(appData.getString("build_path"));
            }
            ApplicationRepository appRepo = new ApplicationRepository();
            CrudResult crudResult = appRepo.update(app);
            result.put("affectedRows", crudResult.getAffectedRows());
            callbackContext.success(result);
        } catch (Exception e) {
            JSONObject errorMessage = new JSONObject();
            errorMessage.put("message", e.getMessage());
            callbackContext.error(errorMessage);
        }
    }

    // Supprime une application à partir de son id (attendu en premier argument dans args)
    public static void deleteApplication(CallbackContext callbackContext, Object[] args) {
        JSONObject result = new JSONObject();
        try {
            if (args == null || args.length == 0) {
                throw new IllegalArgumentException("L'id de l'application à supprimer est requis.");
            }
            int id = Integer.parseInt(args[0].toString());
            ApplicationRepository appRepo = new ApplicationRepository();
            CrudResult crudResult = appRepo.delete(id);
            result.put("affectedRows", crudResult.getAffectedRows());
            callbackContext.success(result);
        } catch (Exception e) {
            JSONObject errorMessage = new JSONObject();
            errorMessage.put("message", e.getMessage());
            callbackContext.error(errorMessage);
        }
    }
}
