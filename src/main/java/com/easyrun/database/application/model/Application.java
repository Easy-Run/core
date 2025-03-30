package com.easyrun.database.application.model;

public class Application {
    private int id;
    private String name;
    private String packageName;
    private boolean isActive;
    private boolean isDefault;
    private String buildPath;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPackageName() {
        return packageName;
    }
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
    public boolean getIsActive() {
        return isActive;
    }
    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
    public boolean getIsDefault() {
        return isDefault;
    }
    public void setIsDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }
    public String getBuildPath() {
        return buildPath;
    }
    public void setBuildPath(String buildPath) {
        this.buildPath = buildPath;
    }
}
