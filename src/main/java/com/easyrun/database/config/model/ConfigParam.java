package com.easyrun.database.config.model;

public class ConfigParam {
    private int id;
    private String code;
    private String value;
    private String description;
    private Boolean isVisible;
    private Boolean editable;
    private String type;
    private int idconfigCategory;

    //* constructeur */
    public ConfigParam() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(Boolean isVisible) {
        this.isVisible = isVisible;
    }

    public Boolean getEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getIdconfigCategory() {
        return idconfigCategory;
    }

    public void setIdconfigCategory(int idconfigCategory) {
        this.idconfigCategory = idconfigCategory;
    }
}
