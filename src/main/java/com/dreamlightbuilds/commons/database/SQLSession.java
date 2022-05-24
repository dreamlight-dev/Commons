package com.dreamlightbuilds.commons.database;

import com.dreamlightbuilds.commons.Common;
import com.dreamlightbuilds.commons.Logger;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;

public class SQLSession {

    private HikariDataSource dataSource;
    private Connection globalConnection;

    private final HikariConfig config;
    private final boolean mysql;

    public SQLSession(final HikariConfig config, final boolean mysql) {
        this.config = config;
        this.mysql = mysql;
    }

    public boolean connect() {
        if (this.mysql) {
            try {
                Logger.info("Trying to connect to MySQL database...");
                this.dataSource = new HikariDataSource(this.config);
                Logger.info("Successfully established connection with MySQL database!");
                return true;
            } catch (Exception ignored) {}

        } else {
            try {
                Logger.info("Trying to connect to SQLite database...");
                final File file = new File(Common.getInstance().getDataFolder(), "database.db");
                if (!file.exists()) {
                    file.mkdirs();
                }
                this.dataSource = new HikariDataSource(this.config);
                Logger.info("Successfully established connection with SQLite database!");
                return true;
            } catch (Exception ignored) {}
        }
        return false;
    }

    public void executeUpdate(final String query) {
        executeUpdate(query, error -> {
            Logger.error("&cAn error occurred while running statement: " + query);
            error.printStackTrace();
        });
    }

    public void executeUpdate(final String statement, final Consumer<SQLException> onFailure) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(statement)) {

            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            onFailure.accept(ex);
        }
    }

    public void executeQuery(final String query, final Consumer<ResultSet> callback) {
        executeQuery(query, callback, ex -> {
            Logger.error("&cAn error occurred while running query: " + query);
            ex.printStackTrace();
        });
    }

    public void executeQuery(final String query, final Consumer<ResultSet> callback, final Consumer<SQLException> onFailure) {
        try (Connection connection = this.getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery()) {

            callback.accept(resultSet);
        } catch (SQLException ex) {
            onFailure.accept(ex);
        }
    }

    public void buildStatement(final String query, final Consumer<PreparedStatement> consumer) {
        buildStatement(query, consumer, ex -> {
            Logger.error("&cAn error occurred while building statement: " + query);
            ex.printStackTrace();
        });
    }

    public void buildStatement(final String query, final Consumer<PreparedStatement> consumer, final Consumer<SQLException> onFailure) {
        try (Connection connection = this.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            consumer.accept(preparedStatement);
        } catch (SQLException ex) {
            onFailure.accept(ex);
        }
    }

    public Connection getConnection() throws SQLException {
        if (this.mysql) {
            return this.dataSource.getConnection();
        } else {
            if (this.globalConnection == null || this.globalConnection.isClosed()) {
                this.globalConnection = this.dataSource.getConnection();
            }
            return this.globalConnection;
        }
    }

    public void close() {
        this.dataSource.close();
    }

}
