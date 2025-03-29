package com.easyrun;

import org.json.JSONArray;

import java.lang.reflect.Constructor;

import org.json.JSONObject;

public class Main {
    public static void main(String[] args) {
        JSONObject errorMessage = new JSONObject();

        // Création d'un CallbackContext qui renvoie le résultat à la console
        CallbackContext callbackContext = new CallbackContext() {
            @Override
            public void success(Object object) {
                System.out.println(object.toString());
            }
            @Override
            public void error(Object object) {
                System.err.println(object.toString());
            }
        };

        if (args.length < 2) {
            errorMessage.put("message", "Usage: <fully-qualified-class> <method> [parameters as JSON array string]");
            callbackContext.error(errorMessage);
            return;
        }
        try {
            // Le nom complet de la classe à appeler (ex: "com.easyrun.environment.EnvironmentInitializer")
            String fullClassName = args[0];
            // Le nom de la méthode à appeler (ex: "initialize")
            String methodName = args[1];

            // Préparation des paramètres (à partir du 3ème argument, ou tableau vide)
            Object[] methodParams = new Object[0];
            if (args.length > 2 && args[2].trim().length() > 0) {
                String paramsString = args[2].trim();
                // Affichage de debug pour connaître le contenu exact de paramsString
                // System.err.println("DEBUG paramsString avant nettoyage: " + paramsString);
                
                // Retirer d'éventuels guillemets extérieurs simples ou doubles
                if ((paramsString.startsWith("\"") && paramsString.endsWith("\"")) ||
                    (paramsString.startsWith("'") && paramsString.endsWith("'"))) {
                    paramsString = paramsString.substring(1, paramsString.length() - 1);
                }
                // Si, après nettoyage, la chaîne ne commence pas par '[',
                // on tente de localiser le premier '[' et on en prend la sous-chaîne
                if (!paramsString.startsWith("[")) {
                    int index = paramsString.indexOf("[");
                    if (index != -1) {
                        paramsString = paramsString.substring(index);
                    }
                }
                // System.err.println("DEBUG paramsString après nettoyage: " + paramsString);
                
                // Parse la chaîne en JSONArray
                JSONArray jsonParams = new JSONArray(paramsString);
                methodParams = jsonParams.toList().toArray();
            }
            
            // Chargement de la classe demandée et instanciation avec les arguments :
            // - le nom de la méthode à appeler (en String)
            // - le CallbackContext
            // - le tableau d'arguments (même vide)
            Class<?> targetClass = Class.forName(fullClassName);
            Constructor<?> ctor = targetClass.getConstructor(String.class, CallbackContext.class, Object[].class);
            ctor.newInstance(methodName, callbackContext, methodParams);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
