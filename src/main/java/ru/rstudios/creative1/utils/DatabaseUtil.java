package ru.rstudios.creative1.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import ru.rstudios.creative1.handlers.customevents.main.DatabaseUpdateEvent;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.rstudios.creative1.CreativePlugin.plugin;

public class DatabaseUtil {

    private final static String JDBC_URL = "jdbc:h2:" + new File(plugin.getDataFolder() + File.separator + "database.db").getAbsolutePath();
    private final static String DB_USER = "sa";
    private final static String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, DB_USER, PASSWORD);
    }

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
                cost BIGINT,
                environment VARCHAR(100),
                generation VARCHAR(100),
                gen_structures BOOLEAN
            );
        """;

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(createPlayerTable);
            stmt.execute(createPlotTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertValue (String tableName, String columnName, Object value) {
        String insertSQL = String.format("INSERT INTO %s (%s) VALUES (?)", tableName, columnName);

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {

            pstmt.setObject(1, value);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertValue (String tableName, List<String> columnNames, List<Object> values) {
        if (columnNames.size() != values.size()) {
            throw new IllegalArgumentException("Cannot execute H2 query: Amount of columns != amount of values");
        }

        StringBuilder columnBuilder = new StringBuilder();
        StringBuilder valuesBuilder = new StringBuilder();

        for (int i = 0; i < columnNames.size(); i++) {
            columnBuilder.append(columnNames.get(i));
            valuesBuilder.append("?");

            if (i < columnNames.size() - 1) {
                columnBuilder.append(", ");
                valuesBuilder.append(", ");
            }
        }

        String insertSQL = String.format("INSERT INTO %s (%s) VALUES (%s)", tableName, columnBuilder, valuesBuilder);

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {

            for (int i = 0; i < values.size(); i++) {
                pstmt.setObject(i + 1, values.get(i));
            }

            pstmt.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().severe(e.getLocalizedMessage());
        }
    }


    public static void updateValue (String tableName, String columnName, Object value, String whereColumn, Object whereValue) {
        String updateSQL = String.format("UPDATE %s SET %s = ? WHERE %s = ?", tableName, columnName, whereColumn);

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {

            Object oldValue = getValue(tableName, columnName, whereColumn, whereValue);

            pstmt.setObject(1, value);
            pstmt.setObject(2, whereValue);
            pstmt.executeUpdate();

            Bukkit.getServer().getPluginManager().callEvent(new DatabaseUpdateEvent(tableName, columnName, value, oldValue, whereColumn, whereValue));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateValue (String tableName, List<String> columnNames, List<Object> values, String whereColumn, Object whereValue) {
        if (columnNames.size() != values.size()) {
            throw new IllegalArgumentException("Cannot execute H2 query: Amount of columns != amount of values");
        }

        StringBuilder setClauseBuilder = new StringBuilder();

        for (int i = 0; i < columnNames.size(); i++) {
            setClauseBuilder.append(columnNames.get(i)).append(" = ?");
            if (i < columnNames.size() - 1) {
                setClauseBuilder.append(", ");
            }
        }

        String updateSQL = String.format("UPDATE %s SET %s WHERE %s = ?", tableName, setClauseBuilder, whereColumn);

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {

            for (int i = 0; i < values.size(); i++) {
                pstmt.setObject(i + 1, values.get(i));
            }

            pstmt.setObject(values.size() + 1, whereValue);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().severe(e.getLocalizedMessage());
        }
    }


    public static Object getValue (String tableName, String columnName, String whereColumn, Object whereValue) {
        String selectSQL = String.format("SELECT %s FROM %s WHERE %s = ?", columnName, tableName, whereColumn);

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {

            pstmt.setObject(1, whereValue);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getObject(columnName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object selectValue (String tableName, String columnName) {
        String selectSQL = String.format("SELECT %s FROM %s", columnName, tableName);

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            plugin.getLogger().severe(e.getLocalizedMessage());
        }

        return null;
    }

    public static List<Long> selectAllValues(String tableName, String columnName) {
        List<Long> values = new ArrayList<>();
        String selectSQL = String.format("SELECT %s FROM %s", columnName, tableName);

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(selectSQL);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                values.add(rs.getLong(1));
            }

        } catch (SQLException e) {
            plugin.getLogger().severe(e.getLocalizedMessage());
        }

        return values;
    }


    public static boolean isValueExist (String tableName, String columnName, String providedValue) {
        String query = String.format("SELECT COUNT(*) FROM %s WHERE %s = ?", tableName, columnName);

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, providedValue);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isValueEmpty (String tableName, String columnName, String providedValue) {
        String query = String.format("SELECT COUNT(*) FROM %s WHERE %s = ?", tableName, columnName);

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, providedValue);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return ((String) rs.getObject(1)).isEmpty();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public static ResultSet executeQuery (String SQLQuery) {
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQLQuery)) {

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static ResultSet executeQueryNoAutoClosed (String SQLQuery) {
        try {
            Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(SQLQuery);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static void executeUpdate (String SQLQuery) {
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQLQuery)) {

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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