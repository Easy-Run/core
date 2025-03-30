package com.easyrun.database.config.controller;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.easyrun.CallbackContext;
import com.easyrun.LogController;
import com.easyrun.database.config.model.ConfigCategory;
import com.easyrun.database.config.model.ConfigParam;
import com.easyrun.database.config.repository.ConfigCategoryRepository;
import com.easyrun.database.config.repository.ConfigParamRepository;

public class ConfigParamController {

    private static final LogController logger = new LogController();

    public ConfigParamController(String methodName, CallbackContext callbackContext, Object[] args) {
        switch (methodName) {
            case "getConfigList" -> getConfigList(callbackContext);
            case "updateConfigValue" -> updateConfigValue(callbackContext, args);
            default -> {
                JSONObject errorMessage = new JSONObject();
                errorMessage.put("message", "La méthode " + methodName + " n'existe pas");
                callbackContext.error(errorMessage);
            }
        }
    }

    public static void updateConfigValue(CallbackContext callbackContext, Object[] args) {
        JSONObject errorMessage = new JSONObject();
        try {
            Map<?, ?> mapInput = (Map<?, ?>) args[0];
            JSONObject input = new JSONObject(mapInput);
            int id = input.getInt("id");
            String value = input.getString("value");

            ConfigParam configParam = new ConfigParam();
            configParam.setId(id);
            configParam.setValue(value);

            ConfigParamRepository paramRepo = new ConfigParamRepository();
            int affected = paramRepo.updateValue(configParam);

            JSONObject result = new JSONObject();
            result.put("rowsAffected", affected);
            callbackContext.success(result);
        } catch (SQLException e) {
            errorMessage.put("message", e.getMessage());
            callbackContext.error(errorMessage);
        }
    }

    /**
     * Récupère tous les ConfigParam et y ajoute, si possible, la catégorie associée
     * dans la propriété "category". Le résultat est encapsulé dans un JSONObject
     * avec une propriété "data" contenant le tableau de paramètres.
     *
     * @return JSONObject contenant la liste des ConfigParam avec leur catégorie
     * @throws SQLException en cas d'erreur d'accès à la base de données
     */
    public static void getConfigList(CallbackContext callbackContext) {
        JSONObject errorMessage = new JSONObject();
        try {
            ConfigParamRepository paramRepo = new ConfigParamRepository();
            ConfigCategoryRepository categoryRepo = new ConfigCategoryRepository();
            
            List<ConfigParam> params = paramRepo.findAll();
            JSONArray array = new JSONArray();
            
            for (ConfigParam cp : params) {
                JSONObject obj = new JSONObject();
                obj.put("id", cp.getId());
                obj.put("code", cp.getCode());
                obj.put("value", cp.getValue());
                obj.put("description", cp.getDescription());
                obj.put("is_visible", cp.getIsVisible());
                obj.put("editable", cp.getEditable());
                obj.put("type", cp.getType());
                obj.put("idconfig_category", cp.getIdconfigCategory());
                
                try {
                    ConfigCategory category = categoryRepo.findById(cp.getIdconfigCategory());
                    if (category != null) {
                        JSONObject catObj = new JSONObject();
                        catObj.put("id", category.getId());
                        catObj.put("code", category.getCode());
                        catObj.put("name", category.getName());
                        catObj.put("description", category.getDescription());
                        obj.put("category", catObj);
                    } else {
                        obj.put("category", JSONObject.NULL);
                    }
                } catch (Exception e) {
                    obj.put("category", JSONObject.NULL);
                }
                
                array.put(obj);
            }
            
            JSONObject result = new JSONObject();
            result.put("data", array);
            callbackContext.success(result);
        } catch (Exception e) {
            errorMessage.put("message", e.getMessage());
            callbackContext.error(errorMessage);
        }
    }
}
