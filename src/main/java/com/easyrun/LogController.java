package com.easyrun;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class LogController {

    private static final ObjectMapper objectMapper = new ObjectMapper()
        .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
        .registerModule(new JavaTimeModule())
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        .enable(SerializationFeature.INDENT_OUTPUT);

    // Méthode utilitaire pour convertir un objet en chaîne JSON (si besoin pour le debug interne)
    private String convert(Object obj) {
        if (obj instanceof String) {
            return (String) obj;
        }
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            return "Erreur lors de la sérialisation de l'objet: " + obj;
        }
    }

    // Ces méthodes ne font rien pour l'instant, afin de supprimer toute sortie de log
    public void info(Object message, Object... additional) {
        // No-op : aucun log n'est écrit
    }

    public void debug(Object message, Object... additional) {
        if (additional == null || additional.length == 0) {
            System.out.println("[DEBUG] " + convert(message));
        } else {
            System.out.println("[DEBUG] " + convert(message) + " " + convert(additional[0]));
        }
    }

    public void error(Object message, Object... additional) {
        // No-op : aucun log n'est écrit
    }
}
