package com.easyrun.database;

import com.easyrun.environment.EnvironmentInitializer;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class BaseRepository {
    protected final String jdbcUrl;

    public BaseRepository() {
        String coreDbDir = EnvironmentInitializer.getCoreDbDir();
        String dbFilePath = coreDbDir + File.separator + "easyrun.db";
        this.jdbcUrl = "jdbc:sqlite:" + dbFilePath;
    }

    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(jdbcUrl);
    }
}
