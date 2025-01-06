package ru.rstudios.creative.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import kireiko.dev.millennium.core.MillenniumScheduler;
import lombok.SneakyThrows;
import org.bukkit.Material;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.rstudios.creative.CreativePlugin.plugin;

public class DatabaseUtil {

    private final static String JDBC_URL = "jdbc:h2:" + new File(plugin.getDataFolder() + File.separator + "database.db").getAbsolutePath() + ";AUTO_RECONNECT=TRUE;AUTO_SERVER=TRUE";
    private final static String DB_USER = "sa";
    private final static String PASSWORD = "";

    private static Connection connection;

    @SneakyThrows
    public static Connection getConnection() {
        if (connection == null || connection.isClosed() || !connection.isValid(2)) connection = DriverManager.getConnection(JDBC_URL, DB_USER, PASSWORD);
        return connection;
    }

    @SneakyThrows
    public static void closeConnection() {
        if (connection != null && !connection.isClosed()) connection.close();
    }

    // Привет разрабам из будущего, сегодня 6 января 2025 года, 18:30 вечера по МСК. Главный разработчик ГромоверГетсл лоханулся и при копипасте новой версии бд с другого нашего проекта забыл изменить таблицы, пишу этот комментарий чтобы запушить фикс.
    @SneakyThrows
    public static void createTables() {
        String createPlayerTable = """
            CREATE TABLE IF NOT EXISTS players (
                id INT PRIMARY KEY AUTO_INCREMENT,
                player_name VARCHAR(100) NOT NULL,
                player_locale VARCHAR(100)
            );
        """;
        String createPlotTable = """
            CREATE TABLE IF NOT EXISTS plots (
                id INT PRIMARY KEY,
                plot_name VARCHAR(100) NOT NULL,
                custom_id VARCHAR(100),
                owner_name VARCHAR(100) NOT NULL,
                openedState BOOLEAN,
                icon VARCHAR(100),
                icon_name VARCHAR(100),
                icon_lore VARCHAR(500),
                cost BIGINT
            );
        """;

        Connection conn = getConnection();
        Statement pstmt = conn.createStatement();

        pstmt.execute(createPlayerTable);
        pstmt.execute(createPlotTable);
    }

    public static void insertValue (String tableName, String columnName, Object value) {
        MillenniumScheduler.run(new Runnable() {
            @Override
            @SneakyThrows
            public void run() {
                String insertSQL = "INSERT INTO " + tableName + "(" + columnName + ") VALUES (?)";

                Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(insertSQL);

                pstmt.setObject(1, tableName);
                pstmt.setObject(2, columnName);
                pstmt.setObject(3, value);

                pstmt.executeUpdate();
            }
        });
    }

    public static void insertValues (String tableName, List<String> columns, List<Object> values) {
        MillenniumScheduler.run(new Runnable() {
            @Override
            @SneakyThrows
            public void run() {
                if (columns.size() != values.size()) throw new IllegalArgumentException("Cannot execute H2 query: Amount of columns != amount of values!");

                StringBuilder columnsBuilder = new StringBuilder();
                StringBuilder valuesBuilder = new StringBuilder();

                for (int i = 0; i < columns.size(); i++) {
                    columnsBuilder.append(columns.get(i));
                    valuesBuilder.append("?");

                    if (i < columns.size() - 1) {
                        columnsBuilder.append(", ");
                        valuesBuilder.append(", ");
                    }
                }

                String query = "INSERT INTO " + tableName + "(" + columnsBuilder + ") VALUES (" + valuesBuilder + ")";
                Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query);

                for (int i = 0; i < values.size(); i++) {
                    pstmt.setObject(i+1, values.get(i));
                }

                pstmt.executeUpdate();
            }
        });
    }

    public static void updateValue (String tableName, String column, Object value, String whereColumn, Object whereValue) {
        MillenniumScheduler.run(new Runnable() {
            @Override
            @SneakyThrows
            public void run() {
                String query = "UPDATE " + tableName + " SET " + column + " = ? WHERE " + whereColumn + " = ?";
                Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query);

                pstmt.setObject(1, value);
                pstmt.setObject(2, whereValue);
                pstmt.executeUpdate();
            }
        });
    }

    @SneakyThrows
    public static Object getValue (String tableName, String columnName, String whereColumn, Object whereValue) {
        String query = "SELECT " + columnName + " FROM " + tableName + " WHERE " + whereColumn + " = ?";
        Connection conn = getConnection();
        PreparedStatement pstmt = conn.prepareStatement(query);

        pstmt.setObject(1, whereValue);
        ResultSet rs = pstmt.executeQuery();

        return rs.next() ? rs.getObject(columnName) : null;
    }

    @SneakyThrows
    public static Object selectValue (String tableName, String columnName) {
        String query = "SELECT " + columnName + " FROM " + tableName;
        Connection conn = getConnection();
        PreparedStatement pstmt = conn.prepareStatement(query);

        ResultSet rs = pstmt.executeQuery();

        return rs.next() ? rs.getObject(1) : null;
    }

    @SneakyThrows
    public static List<Long> selectAllValues (String tableName, String columnName) {
        List<Long> values = new LinkedList<>();
        String query = "SELECT " + columnName + " FROM " + tableName;
        Connection conn = getConnection();
        PreparedStatement pstmt = conn.prepareStatement(query);
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) values.add(rs.getLong(1));
        return values;
    }

    @SneakyThrows
    public static boolean isValueExist (String tableName, String columnName, String providedValue) {
        String query = "SELECT COUNT(*) FROM " + tableName + " WHERE " + columnName + " = ?";
        Connection conn = getConnection();
        PreparedStatement pstmt = conn.prepareStatement(query);

        pstmt.setString(1, providedValue);
        ResultSet rs = pstmt.executeQuery();

        return rs.next() && rs.getInt(1) > 0;
    }

    @SneakyThrows
    public static boolean isValueEmpty (String tableName, String columnName, String providedValue) {
        String query = "SELECT COUNT(*) FROM " + tableName + " WHERE " + columnName + " = ?";
        Connection conn = getConnection();
        PreparedStatement pstmt = conn.prepareStatement(query);

        pstmt.setString(1, providedValue);
        ResultSet rs = pstmt.executeQuery();

        return rs.next() && ((String) rs.getObject(1)).isEmpty();
    }

    @SneakyThrows
    public static ResultSet executeQuery (String SQLQuery) {
        Connection conn = getConnection();
        PreparedStatement pstmt = conn.prepareStatement(SQLQuery);

        ResultSet rs = pstmt.executeQuery();
        return rs.next() ? rs : null;
    }

    @SneakyThrows
    public static void executeUpdate (String SQLQuery) {
        Connection conn = getConnection();
        PreparedStatement pstmt = conn.prepareStatement(SQLQuery);
        pstmt.executeUpdate();
    }

    public static String stringsToJson (List<String> list) {
        return new Gson().toJson(list);
    }

    public static String materialsToJson (List<Material> list) {
        return new Gson().toJson(list.stream().map(Material::name).collect(Collectors.toList()));
    }

    public static List<String> jsonToStringList(String s) {
        return new Gson().fromJson(s, new TypeToken<List<String>>() {}.getType());
    }
}