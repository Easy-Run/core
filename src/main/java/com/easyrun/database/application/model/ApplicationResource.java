package com.easyrun.database.application.model;

public class ApplicationResource {
    private int id;
    private int idApplication;
    private String name;
    private String path;
    private String type;
    private String command;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getIdApplication() {
        return idApplication;
    }
    public void setIdApplication(int idApplication) {
        this.idApplication = idApplication;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public String getCommand() {
        return command;
    }
    public void setCommand(String command) {
        this.command = command;
    }
}
