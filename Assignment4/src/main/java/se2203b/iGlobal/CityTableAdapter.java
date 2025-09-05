package se2203b.iGlobal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CityTableAdapter {

    private final String DB_URL = "jdbc:derby:iGlobalDB;create=true";

    public CityTableAdapter() throws SQLException {
        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement stmt = connection.createStatement()) {

            stmt.execute("CREATE TABLE City (name VARCHAR(50) PRIMARY KEY)");
        } catch (SQLException e) {
            // Table probably already exists — ignore
        }
    }

    public void insertIfNotExists(String name) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DB_URL)) {
            PreparedStatement check = connection.prepareStatement("SELECT COUNT(*) FROM City WHERE name = ?");
            check.setString(1, name);
            ResultSet rs = check.executeQuery();
            rs.next();

            if (rs.getInt(1) == 0) {
                PreparedStatement insert = connection.prepareStatement("INSERT INTO City VALUES (?)");
                insert.setString(1, name);
                insert.executeUpdate();
            }
        }
    }

    public List<String> getAllCities() throws SQLException {
        List<String> cities = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT name FROM City")) {

            while (rs.next()) {
                cities.add(rs.getString("name"));
            }
        }
        return cities;
    }
}
