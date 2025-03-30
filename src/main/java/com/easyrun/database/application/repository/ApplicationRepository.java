package com.easyrun.database.application.repository;

import com.easyrun.database.BaseRepository;
import com.easyrun.database.application.model.Application;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ApplicationRepository extends BaseRepository {

    public ApplicationRepository() {
        super();
    }

    // Classe interne pour retourner le résultat d'une opération CRUD
    public static class CrudResult {
        private int affectedRows;

        public CrudResult(int affectedRows) {
            this.affectedRows = affectedRows;
        }

        public int getAffectedRows() {
            return affectedRows;
        }

        public void setAffectedRows(int affectedRows) {
            this.affectedRows = affectedRows;
        }
    }

    // Retourne toutes les applications
    public List<Application> findAll() {
        List<Application> applications = new ArrayList<>();
        String sql = "SELECT * FROM application";
        try (
            Connection conn = getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)
        ) {

            while (rs.next()) {
                Application app = mapResultSetToApplication(rs);
                applications.add(app);
            }
        } catch (SQLException e) {
            // Gestion des erreurs à adapter si besoin
            e.printStackTrace();
        }
        return applications;
    }

    // retourne l'app par défaut
    public Application findDefault() {
        Application app = null;
        String sql = "SELECT * FROM application WHERE is_default = 1";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
             
            if (rs.next()) {
                app = mapResultSetToApplication(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return app;
    }

    // Retourne une application à partir de son id
    public Application findById(int id) {
        Application app = null;
        String sql = "SELECT * FROM application WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    app = mapResultSetToApplication(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return app;
    }

    // Crée une nouvelle application dans la base de données et retourne le nombre de lignes affectées
    public CrudResult create(Application app) {
        String sql = "INSERT INTO application (name, package_name, is_active, is_default, build_path) VALUES (?, ?, ?, ?, ?)";
        int affectedRows = 0;
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, app.getName());
            pstmt.setString(2, app.getPackageName());
            pstmt.setBoolean(3, app.getIsActive());
            pstmt.setObject(4, app.getIsDefault());
            pstmt.setString(5, app.getBuildPath());
            affectedRows = pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    app.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new CrudResult(affectedRows);
    }

    // Met à jour une application existante et retourne le nombre de lignes affectées
    public CrudResult update(Application app) {
        String sql = "UPDATE application SET name = ?, package_name = ?, is_active = ?, is_default = ?, build_path = ? WHERE id = ?";
        int affectedRows = 0;
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, app.getName());
            pstmt.setString(2, app.getPackageName());
            pstmt.setBoolean(3, app.getIsActive());
            pstmt.setObject(4, app.getIsDefault());
            pstmt.setString(5, app.getBuildPath());
            pstmt.setInt(6, app.getId());

            affectedRows = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new CrudResult(affectedRows);
    }

    // Supprime une application à partir de son id et retourne le nombre de lignes affectées
    public CrudResult delete(int id) {
        String sql = "DELETE FROM application WHERE id = ?";
        int affectedRows = 0;
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            affectedRows = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new CrudResult(affectedRows);
    }

    // Méthode utilitaire pour mapper un ResultSet sur un objet Application
    private Application mapResultSetToApplication(ResultSet rs) throws SQLException {
        Application app = new Application();
        app.setId(rs.getInt("id"));
        app.setName(rs.getString("name"));
        app.setPackageName(rs.getString("package_name"));
        app.setIsActive(rs.getBoolean("is_active"));
        Boolean isDefault = (Boolean) rs.getObject("is_default");
        app.setIsDefault(isDefault);
        app.setBuildPath(rs.getString("build_path"));
        return app;
    }
}
