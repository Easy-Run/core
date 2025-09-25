package com.easyrun.database.application.repository;

import com.easyrun.database.BaseRepository;
import com.easyrun.database.application.model.ApplicationResource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ApplicationResourceRepository extends BaseRepository {

    public ApplicationResourceRepository() {
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

    // Retourne toutes les ressources
    public List<ApplicationResource> findAll() {
        List<ApplicationResource> resources = new ArrayList<>();
        String sql = "SELECT * FROM application_resource";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                ApplicationResource resource = mapResultSetToApplicationResource(rs);
                resources.add(resource);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resources;
    }

    // Retourne une ressource par son id
    public ApplicationResource findById(int id) {
        ApplicationResource resource = null;
        String sql = "SELECT * FROM application_resource WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    resource = mapResultSetToApplicationResource(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resource;
    }

    // Retourne toutes les ressources pour une application donnée (id_application)
    public List<ApplicationResource> findAllByIdApplication(int idApplication) {
        List<ApplicationResource> resources = new ArrayList<>();
        String sql = "SELECT * FROM application_resource WHERE id_application = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idApplication);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    ApplicationResource resource = mapResultSetToApplicationResource(rs);
                    resources.add(resource);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resources;
    }

    // Crée une nouvelle ressource dans la base de données et retourne le nombre de lignes affectées
    public CrudResult create(ApplicationResource resource) {
        String sql = "INSERT INTO application_resource (id_application, name, path, type, command) VALUES (?, ?, ?, ?, ?)";
        int affectedRows = 0;
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, resource.getIdApplication());
            pstmt.setString(2, resource.getName());
            pstmt.setString(3, resource.getPath());
            pstmt.setString(4, resource.getType());
            pstmt.setString(5, resource.getCommand());
            affectedRows = pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    resource.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new CrudResult(affectedRows);
    }

    // Met à jour une ressource existante et retourne le nombre de lignes affectées
    public CrudResult update(ApplicationResource resource) {
        String sql = "UPDATE application_resource SET id_application = ?, name = ?, path = ?, type = ?, command = ? WHERE id = ?";
        int affectedRows = 0;
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, resource.getIdApplication());
            pstmt.setString(2, resource.getName());
            pstmt.setString(3, resource.getPath());
            pstmt.setString(4, resource.getType());
            pstmt.setString(5, resource.getCommand());
            pstmt.setInt(6, resource.getId());

            affectedRows = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new CrudResult(affectedRows);
    }

    // Supprime une ressource à partir de son id et retourne le nombre de lignes affectées
    public CrudResult delete(int id) {
        String sql = "DELETE FROM application_resource WHERE id = ?";
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

    // Supprime toutes les ressources pour une application donnée et retourne le nombre de lignes affectées
    public CrudResult deleteAllByIdApplication(int idApplication) {
        String sql = "DELETE FROM application_resource WHERE id_application = ?";
        int affectedRows = 0;
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idApplication);
            affectedRows = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new CrudResult(affectedRows);
    }

    // Méthode utilitaire pour mapper un ResultSet sur un objet ApplicationResource
    private ApplicationResource mapResultSetToApplicationResource(ResultSet rs) throws SQLException {
        ApplicationResource resource = new ApplicationResource();
        resource.setId(rs.getInt("id"));
        resource.setIdApplication(rs.getInt("id_application"));
        resource.setName(rs.getString("name"));
        resource.setPath(rs.getString("path"));
        resource.setType(rs.getString("type"));
        resource.setCommand(rs.getString("command"));
        return resource;
    }
}
