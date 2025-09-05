package se2203b.iGlobal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProvinceTableAdapter {

    private final String DB_URL = "jdbc:derby:iGlobalDB;create=true";

    public ProvinceTableAdapter() throws SQLException {
        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement stmt = connection.createStatement()) {

            stmt.execute("CREATE TABLE Province (name VARCHAR(50) PRIMARY KEY)");
        } catch (SQLException e) {
            // Table probably already exists — ignore
        }
    }

    public void insertIfNotExists(String name) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DB_URL)) {
            PreparedStatement check = connection.prepareStatement("SELECT COUNT(*) FROM Province WHERE name = ?");
            check.setString(1, name);
            ResultSet rs = check.executeQuery();
            rs.next();

            if (rs.getInt(1) == 0) {
                PreparedStatement insert = connection.prepareStatement("INSERT INTO Province VALUES (?)");
                insert.setString(1, name);
                insert.executeUpdate();
            }
        }
    }

    public List<String> getAllProvinces() throws SQLException {
        List<String> provinces = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT name FROM Province")) {

            while (rs.next()) {
                provinces.add(rs.getString("name"));
            }
        }
        return provinces;
    }
}
