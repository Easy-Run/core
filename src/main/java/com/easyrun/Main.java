// Main.java
package com.easyrun;

import org.json.JSONArray;
import org.json.JSONObject;
import java.lang.reflect.Constructor;
import com.easyrun.core.AppContext; // <-- ajoute cet import

public class Main {
    public static void main(String[] args) {
        JSONObject errorMessage = new JSONObject();

        CallbackContext callbackContext = new CallbackContext() {
            @Override public void success(Object object)  { System.out.println(object.toString()); }
            @Override public void error(Object object)    { System.err.println(object.toString()); }
            @Override public void progress(JSONObject p)  { System.out.println(p.toString()); }
        };

        if (args.length < 2) {
            errorMessage.put("message", "Usage: <fully-qualified-class> <method> [parameters as JSON array string]");
            callbackContext.error(errorMessage);
            return;
        }

        try {
            String fullClassName = args[0];
            String methodName    = args[1];

            Object[] methodParams = new Object[0];
            JSONArray jsonParams  = null;

            if (args.length > 2 && args[2].trim().length() > 0) {
                String paramsString = args[2].trim();
                if ((paramsString.startsWith("\"") && paramsString.endsWith("\"")) ||
                    (paramsString.startsWith("'")  && paramsString.endsWith("'"))) {
                    paramsString = paramsString.substring(1, paramsString.length() - 1);
                }
                if (!paramsString.startsWith("[")) {
                    int index = paramsString.indexOf("[");
                    if (index != -1) paramsString = paramsString.substring(index);
                }

                jsonParams   = new JSONArray(paramsString);
                methodParams = jsonParams.toList().toArray();
            }

            // >>> APPLIQUER LE CONTEXTE ICI (une fois pour toutes)
            String appId = null, profile = null, baseDir = null;

            // 1) si le 1er élément du tableau est un objet contexte { appId, profile, baseDir }
            if (jsonParams != null && jsonParams.length() > 0 && jsonParams.get(0) instanceof JSONObject jo) {
                appId   = jo.optString("appId",   null);
                profile = jo.optString("profile", null);
                baseDir = jo.optString("baseDir", null);
            } else if (methodParams.length > 0 && methodParams[0] instanceof java.util.Map<?,?> map) {
                JSONObject jo = new JSONObject((java.util.Map<?,?>) map);
                appId   = jo.optString("appId",   null);
                profile = jo.optString("profile", null);
                baseDir = jo.optString("baseDir", null);
            }

            // 2) fallback sur variables d’environnement si rien n’a été passé
            if (appId == null)   appId   = System.getenv("EASYRUN_APPID");
            if (profile == null) profile = System.getenv("EASYRUN_PROFILE");
            if (baseDir == null) baseDir = System.getenv("EASYRUN_HOME");

            // 3) poser le contexte (no-op si tout est null)
            com.easyrun.core.AppContext.set(appId, profile, baseDir);

            // >>> ensuite seulement, instancier la classe cible
            Class<?> targetClass = Class.forName(fullClassName);
            Constructor<?> ctor  = targetClass.getConstructor(String.class, CallbackContext.class, Object[].class);
            ctor.newInstance(methodName, callbackContext, methodParams);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
