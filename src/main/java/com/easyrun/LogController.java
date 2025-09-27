package com.easyrun;

import com.easyrun.environment.EnvironmentInitializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.io.BufferedWriter;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogController {

    private static final ObjectMapper objectMapper = new ObjectMapper()
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .enable(SerializationFeature.INDENT_OUTPUT);

    // ddMMyyyy.log (ex: 26092025.log)
    private static final DateTimeFormatter FILE_FMT = DateTimeFormatter.ofPattern("ddMMyyyy");
    // Horodatage dans chaque ligne
    private static final DateTimeFormatter TS_FMT   = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    // >>> chemin résolu dynamiquement selon le contexte courant
    private static Path getLogDir() {
        return Paths.get(EnvironmentInitializer.getEasyrunDir(), "logs");
    }

    private static Path currentLogFile() {
        String name = LocalDate.now().format(FILE_FMT) + ".log";
        return getLogDir().resolve(name);
    }

    private static String convert(Object obj) {
        if (obj == null) return "null";
        if (obj instanceof String) return (String) obj;
        try { return objectMapper.writeValueAsString(obj); }
        catch (Exception e) { return String.valueOf(obj); }
    }

    private static String merge(Object message, Object... additional) {
        StringBuilder sb = new StringBuilder(convert(message));
        if (additional != null) {
            for (Object a : additional) sb.append(' ').append(convert(a));
        }
        return sb.toString();
    }

    private static synchronized void write(String level, Object message, Object... additional) {
        String lvl = (level == null ? "INFO" : level);
        if (!(lvl.startsWith("[") && lvl.endsWith("]"))) {
            lvl = "[" + lvl + "]";
        }
        String lvlPadded = String.format("%-8s", lvl);
        String line = String.format("%s %s %s",
            LocalDateTime.now().format(TS_FMT), lvlPadded, merge(message, additional));

        try {
            Files.createDirectories(getLogDir());
            try (BufferedWriter w = Files.newBufferedWriter(
                    currentLogFile(), StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
                w.write(line);
                w.newLine();
            }
        } catch (IOException ioe) {
            // dernier recours: stderr (n’interfère pas avec ton JSON stdout)
            System.err.println("LOG WRITE FAILURE: " + ioe);
        }
    }

    // API publique
    public void info(Object message, Object... additional) { write("INFO",  message, additional); }
    public void debug(Object message, Object... additional){ write("DEBUG", message, additional); }
    public void error(Object message, Object... additional){ write("ERROR", message, additional); }
    public void error(Object message, Throwable t)         { write("ERROR", message + " -> " + convert(t)); }
}
