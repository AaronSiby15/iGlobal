package se2203b.iGlobal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;



public class PropertyTableAdapter {
    private Connection connection;
    private final String DB_URL = "jdbc:derby:iGlobalDB;create=true";

    public PropertyTableAdapter(boolean b) throws SQLException {
        connection = DriverManager.getConnection(DB_URL);
        Statement stmt = connection.createStatement();

        try {
            String command = "CREATE TABLE Property (" +
                    "id VARCHAR(36), " +  // NEW LINE
                    "propertyType VARCHAR(30), " +
                    "streetAddress VARCHAR(100), " +
                    "postalCode VARCHAR(15), " +
                    "province VARCHAR(30), " +
                    "city VARCHAR(30), " +
                    "lotSize DOUBLE, " +
                    "squareFootage DOUBLE, " +
                    "numberOfBedrooms INT, " +
                    "numberOfBathrooms DOUBLE, " +
                    "yearBuilt INT, " +
                    "amenities VARCHAR(255), " +
                    "price DOUBLE, " +
                    "generalDescription VARCHAR(255)" +
                    ")";

            stmt.execute(command);
        } catch (SQLException e) {
            // Table might already exist
        }

        connection.close();
    }

    public void addNewRecord(Property property) throws SQLException {
        connection = DriverManager.getConnection(DB_URL);

        String command = "INSERT INTO Property VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(command);

        ps.setString(1, property.getId()); // <-- New line for 'id'
        ps.setString(2, property.getType());
        ps.setString(3, property.getStreet());
        ps.setString(4, property.getPostalCode());
        ps.setString(5, property.getProvince());
        ps.setString(6, property.getCity());
        ps.setDouble(7, property.getLotSize());
        ps.setDouble(8, property.getSquareFootage());
        ps.setInt(9, property.getBedrooms());
        ps.setDouble(10, property.getBathrooms());
        ps.setInt(11, property.getYearBuilt());
        ps.setString(12, property.getAmenities());
        ps.setDouble(13, property.getPrice());
        ps.setString(14, property.getDescription());

        ps.executeUpdate();
        connection.close();
    }


    public List<Property> getAllRecords() throws SQLException {
        List<Property> properties = new ArrayList<>();
        connection = DriverManager.getConnection(DB_URL);
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Property");

        while (rs.next()) {
            Property p = new Property(
                    rs.getString("id"), // ✅ Corrected line
                    rs.getString("propertyType"),
                    rs.getString("streetAddress"),
                    rs.getString("postalCode"),
                    rs.getString("province"),
                    rs.getString("city"),
                    rs.getDouble("lotSize"),
                    rs.getDouble("squareFootage"),
                    rs.getInt("numberOfBedrooms"),
                    rs.getInt("numberOfBathrooms"),
                    rs.getInt("yearBuilt"),
                    rs.getString("amenities"),
                    rs.getDouble("price"),
                    rs.getString("generalDescription")
            );

            properties.add(p);
        }

        connection.close();
        return properties;
    }


    public void updateRecord(Property property) throws SQLException {
        connection = DriverManager.getConnection(DB_URL);
        String sql = "UPDATE Property SET " +
                "propertyType=?, streetAddress=?, postalCode=?, province=?, city=?, " +
                "lotSize=?, squareFootage=?, numberOfBedrooms=?, numberOfBathrooms=?, " +
                "yearBuilt=?, amenities=?, price=?, generalDescription=? " +
                "WHERE id=?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, property.getType());
        ps.setString(2, property.getStreet());
        ps.setString(3, property.getPostalCode());
        ps.setString(4, property.getProvince());
        ps.setString(5, property.getCity());
        ps.setDouble(6, property.getLotSize());
        ps.setDouble(7, property.getSquareFootage());
        ps.setInt(8, property.getBedrooms());
        ps.setDouble(9, property.getBathrooms());
        ps.setInt(10, property.getYearBuilt());
        ps.setString(11, property.getAmenities());
        ps.setDouble(12, property.getPrice());
        ps.setString(13, property.getDescription());
        ps.setString(14, property.getId());

        ps.executeUpdate();
        connection.close();
    }


    public List<Property> getAllProperties() throws SQLException {
        return getAllRecords().stream()
                .map(obj -> (Property) obj)
                .collect(Collectors.toList());
    }

    public void deleteOneRecord(Property property) throws SQLException {
        connection = DriverManager.getConnection(DB_URL);
        String command = "DELETE FROM Property WHERE streetAddress = ? AND postalCode = ?";
        PreparedStatement ps = connection.prepareStatement(command);
        ps.setString(1, property.getStreet());
        ps.setString(2, property.getPostalCode());
        ps.executeUpdate();
        connection.close();
    }
}


