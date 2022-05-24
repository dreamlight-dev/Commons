package com.dreamlightbuilds.commons.database;

import com.zaxxer.hikari.HikariConfig;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;

public class SQLHelper {

    private static SQLSession session;

    public static boolean connect(HikariConfig config, boolean mysql) {
        session = new SQLSession(config, mysql);
        return session.connect();
    }

    public static void executeUpdate(String statement, Consumer<SQLException> onFailure) {
        session.executeUpdate(statement, onFailure);
    }

    public static void executeQuery(String statement, Consumer<ResultSet> callback) {
        session.executeQuery(statement, callback);
    }

    public static void executeQuery(String statement, Consumer<ResultSet> callback, Consumer<SQLException> onFailure) {
        session.executeQuery(statement, callback, onFailure);
    }

    public static void buildStatement(String query, Consumer<PreparedStatement> consumer, Consumer<SQLException> onFailure) {
        session.buildStatement(query, consumer, onFailure);
    }

    public static void close() {
        session.close();
    }

}
