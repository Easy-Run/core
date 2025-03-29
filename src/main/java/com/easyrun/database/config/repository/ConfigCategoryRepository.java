package com.easyrun.database.config.repository;

import com.easyrun.database.BaseRepository;
import com.easyrun.database.config.model.ConfigCategory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConfigCategoryRepository extends BaseRepository {

    public ConfigCategoryRepository() {
        super();
    }

    /**
     * Recherche une catégorie par son id.
     */
    public ConfigCategory findById(int id) throws SQLException {
        String sql = "SELECT * FROM config_category WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToConfigCategory(rs);
                }
            }
        }
        return null;
    }

    /**
     * Retourne la liste de toutes les catégories.
     */
    public List<ConfigCategory> findAll() throws SQLException {
        List<ConfigCategory> list = new ArrayList<>();
        String sql = "SELECT * FROM config_category";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRowToConfigCategory(rs));
            }
        }
        return list;
    }

    /**
     * Insère une nouvelle catégorie en base.
     * Si l'insertion réussit, l'id généré est mis à jour dans l'objet.
     */
    public boolean save(ConfigCategory category) throws SQLException {
        String sql = "INSERT INTO config_category (code, name, description) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, category.getCode());
            stmt.setString(2, category.getName());
            stmt.setString(3, category.getDescription());
            int affected = stmt.executeUpdate();
            if (affected == 1) {
                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        category.setId(keys.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Met à jour une catégorie existante.
     */
    public boolean update(ConfigCategory category) throws SQLException {
        String sql = "UPDATE config_category SET code = ?, name = ?, description = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, category.getCode());
            stmt.setString(2, category.getName());
            stmt.setString(3, category.getDescription());
            stmt.setInt(4, category.getId());
            int affected = stmt.executeUpdate();
            return affected == 1;
        }
    }

    /**
     * Supprime une catégorie par son id.
     */
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM config_category WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int affected = stmt.executeUpdate();
            return affected == 1;
        }
    }

    /**
     * Convertit une ligne du ResultSet en objet ConfigCategory.
     */
    private ConfigCategory mapRowToConfigCategory(ResultSet rs) throws SQLException {
        ConfigCategory category = new ConfigCategory();
        category.setId(rs.getInt("id"));
        category.setCode(rs.getString("code"));
        category.setName(rs.getString("name"));
        category.setDescription(rs.getString("description"));
        return category;
    }
}
