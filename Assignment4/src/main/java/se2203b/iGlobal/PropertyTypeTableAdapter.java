/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se2203b.iGlobal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Abdelkader Ouda
 */
public class PropertyTypeTableAdapter implements DataStore {

    private Connection connection;
    private String DB_URL = "jdbc:derby:iGlobalDB;create=true";

    public PropertyTypeTableAdapter(Boolean reset) throws SQLException {
        connection = DriverManager.getConnection(DB_URL);
        Statement stmt = connection.createStatement();
        if (reset) {
            try {
                // Remove tables if database tables have been created.
                // This will throw an exception if the tables do not exist
                stmt.execute("DROP TABLE PropertyType");
                // then do finally
            } catch (SQLException ex) {
                // No need to report an error.
                // The table simply did not exist.
                // do finally to create it
            }
        }

        try {
            // Create the table
            String command = "CREATE TABLE PropertyType ("
                    + "typeCode VARCHAR(9) NOT NULL PRIMARY KEY, "
                    + "typeName VARCHAR(50) "
                    + ")";
            stmt.execute(command);
        } catch (SQLException ex) {
            // No need to report an error.
            // The table exists and may have some data.
        }
        connection.close();
    }

    @Override
    public void addNewRecord(Object data) throws SQLException {
        PropertyType propertyType = (PropertyType) data;
        connection = DriverManager.getConnection(DB_URL);
        Statement stmt = connection.createStatement();
        try {
            String command = "INSERT INTO PropertyType (typeCode, typeName) "
                    + "VALUES ('"
                    + propertyType.getTypeCode() + "', '"
                    + propertyType.getTypeName() + "'"
                    +  " )";
            stmt.executeUpdate(command);
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateRecord(Object data) throws SQLException {
        PropertyType propertyType = (PropertyType) data;
        connection = DriverManager.getConnection(DB_URL);
        Statement stmt = connection.createStatement();
        // Get the user account name if exists
        String command = "UPDATE PropertyType "
                + "SET "
                + "typeCode = '" + propertyType.getTypeCode() + "', "
                + "typeName = '" + propertyType.getTypeName() + "' "
                + " WHERE typeCode = "
                + "'" + propertyType.getTypeCode() + "'";

        stmt.executeUpdate(command);
        connection.close();
    }

    @Override
    public void deleteOneRecord(Object data) throws SQLException {
        PropertyType propertyType = (PropertyType) data;
        connection = DriverManager.getConnection(DB_URL);
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("DELETE FROM PropertyType WHERE typeCode = '" + propertyType.getTypeCode() + "'");
        connection.close();
    }

    @Override
    public void deleteRecords(Object referencedObject) throws SQLException {

    }

    @Override
    public List<String> getKeys() throws SQLException {
        List<String> list = new ArrayList<>();
        ResultSet rs;
        connection = DriverManager.getConnection(DB_URL);

        // Create a Statement object
        Statement stmt = connection.createStatement();

        // Create a string with a SELECT statement
        String command = "SELECT typeCode FROM PropertyType";

        // Execute the statement and return the result
        rs = stmt.executeQuery(command);

        while (rs.next()) {
            list.add(rs.getString(1));
        }
        connection.close();
        return list;
    }

    @Override
    public Object findOneRecord(String key) throws SQLException {
        PropertyType propertyType = new PropertyType();
        ResultSet rs;
        connection = DriverManager.getConnection(DB_URL);

        // Create a Statement object
        Statement stmt = connection.createStatement();

        // Create a string with a SELECT statement
        String command = "SELECT * FROM PropertyType WHERE typeCode = '" + key + "'";

        // Execute the statement and return the result
        rs = stmt.executeQuery(command);
        while (rs.next()) {
            propertyType.setTypeCode(rs.getString("typeCode"));
            propertyType.setTypeName(rs.getString("typeName"));

        }
        connection.close();
        return propertyType;
    }

    @Override
    public Object findOneRecord(Object object) throws SQLException {
        PropertyType propertyType = new PropertyType();
        ResultSet rs;
        connection = DriverManager.getConnection(DB_URL);

        // Create a Statement object
        Statement stmt = connection.createStatement();

        // Create a string with a SELECT statement
        String command = "SELECT * FROM PropertyType WHERE typeCode = '" + ((PropertyType) object).getTypeCode() + "'";
        // Execute the statement and return the result
        rs = stmt.executeQuery(command);
        while (rs.next()) {
            propertyType.setTypeCode(rs.getString("typeCode"));
            propertyType.setTypeName(rs.getString("typeName"));

        }
        connection.close();
        return propertyType;
    }

    @Override
    public List<Object> getAllRecords() throws SQLException {
        List<Object> list = new ArrayList<>();
        ResultSet result;

        try {
            connection = DriverManager.getConnection(DB_URL);

            // Create a Statement object
            Statement stmt = connection.createStatement();

            // Create a string with a SELECT statement
            String command = "SELECT * FROM PropertyType";

            // Execute the statement and return the result
            result = stmt.executeQuery(command);

            while (result.next()) {
                PropertyType propertyType = new PropertyType();
                propertyType.setTypeCode(result.getString("typeCode"));
                propertyType.setTypeName(result.getString("typeName"));
                list.add(propertyType);
            }
            connection.close();
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Object> getAllRecords(Object referencedObject) throws SQLException {
        return null;
    }
}
