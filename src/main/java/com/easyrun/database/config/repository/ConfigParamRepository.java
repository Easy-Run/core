package com.easyrun.database.config.repository;

import com.easyrun.database.BaseRepository;
import com.easyrun.database.config.model.ConfigParam;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.easyrun.LogController;


public class ConfigParamRepository extends BaseRepository {

    private static final LogController logger = new LogController();

    public ConfigParamRepository() {
        super();
    }

    /**
     * Recherche un ConfigParam par son id.
     */
    public ConfigParam findById(int id) throws SQLException {
        String sql = "SELECT * FROM config_param WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToConfigParam(rs);
                }
            }
        }
        return null;
    }

    /**
     * Retourne la liste de tous les ConfigParam.
     */
    public List<ConfigParam> findAll() throws SQLException {
        List<ConfigParam> list = new ArrayList<>();
        String sql = "SELECT * FROM config_param";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRowToConfigParam(rs));
            }
        }
        return list;
    }

    /**
     * Insère un nouveau ConfigParam en base.
     * Si l'insertion réussit, l'id généré est mis à jour dans l'objet.
     */
    public boolean save(ConfigParam configParam) throws SQLException {
        String sql = "INSERT INTO config_param (code, value, description, is_visible, editable, type, idconfig_category) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, configParam.getCode());
            stmt.setString(2, configParam.getValue());
            stmt.setString(3, configParam.getDescription());
            stmt.setBoolean(4, configParam.getIsVisible());
            stmt.setBoolean(5, configParam.getEditable());
            stmt.setString(6, configParam.getType());
            stmt.setInt(7, configParam.getIdconfigCategory());
            int affected = stmt.executeUpdate();
            if (affected == 1) {
                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        configParam.setId(keys.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Met à jour un ConfigParam existant.
     */
    public boolean update(ConfigParam configParam) throws SQLException {
        String sql = "UPDATE config_param SET code = ?, value = ?, description = ?, is_visible = ?, editable = ?, type = ?, idconfig_category = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, configParam.getCode());
            stmt.setString(2, configParam.getValue());
            stmt.setString(3, configParam.getDescription());
            stmt.setBoolean(4, configParam.getIsVisible());
            stmt.setBoolean(5, configParam.getEditable());
            stmt.setString(6, configParam.getType());
            stmt.setInt(7, configParam.getIdconfigCategory());
            stmt.setInt(8, configParam.getId());
            int affected = stmt.executeUpdate();
            return affected == 1;
        }
    }

    /**
     * Mise à jour de la valeur d'un paramètre
     * 
     * @param configParam
     * @return
     * @throws SQLException
     */
    public int updateValue(ConfigParam configParam) throws SQLException {
        String sql = "UPDATE config_param SET value = ? where id = ?";
        try (
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
        ) {
            stmt.setString(1, configParam.getValue());
            stmt.setInt(2, configParam.getId());
            int affected = stmt.executeUpdate();
            return affected;
        }
    }

    /**
     * Supprime le ConfigParam ayant l'id spécifié.
     */
    /* public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM config_param WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int affected = stmt.executeUpdate();
            return affected == 1;
        }
    } */

    /**
     * Convertit une ligne du ResultSet en objet ConfigParam.
     */
    private ConfigParam mapRowToConfigParam(ResultSet rs) throws SQLException {
        ConfigParam cp = new ConfigParam();
        cp.setId(rs.getInt("id"));
        cp.setCode(rs.getString("code"));
        cp.setValue(rs.getString("value"));
        cp.setDescription(rs.getString("description"));
        cp.setIsVisible(rs.getBoolean("is_visible"));
        cp.setEditable(rs.getBoolean("editable"));
        cp.setType(rs.getString("type"));
        cp.setIdconfigCategory(rs.getInt("idconfig_category"));
        return cp;
    }
}
