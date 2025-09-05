package se2203b.iGlobal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 * @author Abdelkader Ouda
 */
public class UserAccountTableAdapter implements DataStore {
    private Connection connection;
    private String DB_URL = "jdbc:derby:iGlobalDB;create=true";

    public UserAccountTableAdapter(Boolean reset) throws SQLException {
        connection = DriverManager.getConnection(DB_URL);
        Statement stmt = connection.createStatement();
        if (reset) {
            try {
                // Remove tables if database tables have been created.
                // This will throw an exception if the tables do not exist
                stmt.execute("DROP TABLE UserAccount");
            } catch (SQLException ex) {
                // No need to report an error.
                // The table simply did not exist.
            }
        }

        try {
            String command = "CREATE TABLE UserAccount ("
                    + "userAccountName VARCHAR(30) NOT NULL PRIMARY KEY,"
                    + "encryptedPassword VARCHAR(100) NOT NULL,"
                    + "passwordSalt VARCHAR(50) NOT NULL,"
                    + "accountType VARCHAR(10) NOT NULL"
                    + ")";
            // Create the table
            stmt.execute(command);
        } catch (SQLException ex) {
            // No need to report an error.
            // The table exists and may have some data.
            // We will use it instead of creating a new one.
        }

        connection.close();
    }

    // adds new record, reads from Administrator object
    @Override
    public void addNewRecord(Object data) throws SQLException {
        UserAccount userAccount = (UserAccount) data;
        connection = DriverManager.getConnection(DB_URL);

        Statement stmt = connection.createStatement();
        String command = "INSERT INTO UserAccount ( userAccountName,  encryptedPassword, passwordSalt, accountType) "
                + "VALUES ('"
                + userAccount.getUserAccountName() + "', '"
                + userAccount.getEncryptedPassword() + "', '"
                + userAccount.getPasswordSalt() + "', '"
                + userAccount.getAccountType() + "')";
        int rows = stmt.executeUpdate(command);
        connection.close();
    }

    // Modify one record based on the given object
    @Override
    public void updateRecord(Object data) throws SQLException {
        UserAccount userAccount = (UserAccount) data;
        connection = DriverManager.getConnection(DB_URL);

        Statement stmt = connection.createStatement();
        String command = "UPDATE UserAccount SET "
                + "userAccountName = '" + userAccount.getUserAccountName() + "', "
                + "encryptedPassword = '" + userAccount.getEncryptedPassword() + "', "
                + "passwordSalt = '" + userAccount.getPasswordSalt() + "', "
                + "accountType = '" + userAccount.getAccountType() + "' "
                + "WHERE userAccountName = '" + userAccount.getUserAccountName() + "'";
        stmt.executeUpdate(command);
        connection.close();
    }

    // get one record, that matches the given name value
    @Override
    public Object findOneRecord(String key) throws SQLException {
        UserAccount userAccount = new UserAccount();
        ResultSet rs;
        connection = DriverManager.getConnection(DB_URL);

        // Create a Statement object
        Statement stmt = connection.createStatement();
        // Create a string with a SELECT statement
        String command = "SELECT * FROM UserAccount WHERE userAccountName = '" + key + "' ";
        // Execute the statement and return the result
        rs = stmt.executeQuery(command);
        while (rs.next()) {
            // note that, this loop will run only once
            userAccount.setUserAccountName(rs.getString(1));
            userAccount.setEncryptedPassword(rs.getString(2));
            userAccount.setPasswordSalt(rs.getString(3));
            userAccount.setAccountType(rs.getString(4));
        }
        connection.close();
        return userAccount;
    }

    @Override
    public Object findOneRecord(Object referencedObject) throws SQLException {
        return null;
    }

    // Get a String list of usernames.
    @Override
    public List<String> getKeys() throws SQLException {
        List<String> list = new ArrayList<>();
        ResultSet rs;
        connection = DriverManager.getConnection(DB_URL);

        // Create a Statement object
        Statement stmt = connection.createStatement();

        // Create a string with a SELECT statement
        String command = "SELECT userAccountName FROM UserAccount WHERE accountType <> 'admin'";

        // Execute the statement and return the result
        rs = stmt.executeQuery(command);

        while (rs.next()) {
            list.add(rs.getString(1));
        }
        connection.close();
        return list;
    }

    @Override
    public void deleteOneRecord(Object data) throws SQLException {
        UserAccount userAccount = (UserAccount) data;
        connection = DriverManager.getConnection(DB_URL);
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("DELETE FROM UserAccount WHERE userAccountName = '"
                + userAccount.getUserAccountName() + "'");
        connection.close();
    }

    @Override
    public void deleteRecords(Object referencedObject) throws SQLException {

    }

    @Override
    public List<Object> getAllRecords() throws SQLException {
        List<Object> list = new ArrayList<>();
        ResultSet rs;
        connection = DriverManager.getConnection(DB_URL);

        Statement stmt = connection.createStatement();
        String command = "SELECT * FROM UserAccount";
        rs = stmt.executeQuery(command);

        while (rs.next()) {
            UserAccount user = new UserAccount();
            user.setUserAccountName(rs.getString("userAccountName"));
            user.setEncryptedPassword(rs.getString("encryptedPassword"));
            user.setPasswordSalt(rs.getString("passwordSalt"));
            user.setAccountType(rs.getString("accountType"));
            list.add(user);
        }

        connection.close();
        return list;
    }



    @Override
    public List<Object> getAllRecords(Object referencedObject) throws SQLException {
        return null;
    }

    public List<Object> getAllRecordsIncludingAdmins() throws SQLException {
        List<Object> list = new ArrayList<>();
        ResultSet rs;
        connection = DriverManager.getConnection(DB_URL);

        Statement stmt = connection.createStatement();
        String command = "SELECT * FROM UserAccount";  // no filtering

        rs = stmt.executeQuery(command);
        while (rs.next()) {
            UserAccount account = new UserAccount();
            account.setUserAccountName(rs.getString("userAccountName"));
            account.setEncryptedPassword(rs.getString("encryptedPassword"));
            account.setPasswordSalt(rs.getString("passwordSalt"));
            account.setAccountType(rs.getString("accountType"));
            list.add(account);
        }

        connection.close();
        return list;
    }


}