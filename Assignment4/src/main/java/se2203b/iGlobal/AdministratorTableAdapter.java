package se2203b.iGlobal;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class AdministratorTableAdapter implements DataStore {
    private Connection connection;
    private final String DB_URL = "jdbc:derby:iGlobalDB;create=true";

    public AdministratorTableAdapter(Boolean reset) throws SQLException {
        connection = DriverManager.getConnection(DB_URL);
        Statement stmt = connection.createStatement();
        if (reset) {
            try { stmt.execute("DROP TABLE Administrator"); } catch (SQLException ignored) {}
            try { stmt.execute("DROP TABLE UserAccount"); } catch (SQLException ignored) {}
        }

        try {
            stmt.execute("CREATE TABLE UserAccount (" +
                    "userAccountName VARCHAR(30) NOT NULL PRIMARY KEY," +
                    "encryptedPassword VARCHAR(100) NOT NULL," +
                    "passwordSalt VARCHAR(50) NOT NULL," +
                    "accountType VARCHAR(10) NOT NULL)");
        } catch (SQLException ignored) {}

        try {
            stmt.execute("CREATE TABLE Administrator (" +
                    "id VARCHAR(100) NOT NULL PRIMARY KEY, " +
                    "firstName VARCHAR(60) NOT NULL, " +
                    "lastName VARCHAR(60) NOT NULL, " +
                    "email VARCHAR(60), " +
                    "phone VARCHAR(60), " +
                    "dateCreated DATE, " +
                    "userAccount VARCHAR(30) REFERENCES UserAccount(userAccountName))");
        } catch (SQLException ignored) {}

        try {
            addAdmin();
        } catch (SQLException ignored) {}

        connection.close();
    }

    private void addAdmin() throws SQLException {
        Administrator admin = new Administrator();
        admin.setID("1");
        admin.setFirstName("Default iGlobal");
        admin.setLastName("Admin");
        admin.setEmail("admin@iGlobal.com");
        admin.setPhone("519 123 4567");

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        admin.setDateCreated(Date.valueOf(df.format(new java.util.Date())));

        addNewRecord(admin);

        Random random = new Random();
        String salt = Integer.toString(random.nextInt());
        String password = "admin";

        UserAccount account = new UserAccount("admin", encrypt(password, salt), salt, "admin");
        DataStore userAccountAdapter = new UserAccountTableAdapter(false);
        userAccountAdapter.addNewRecord(account);

        admin.setUserAccount(account);
        updateRecord(admin);
    }

    private String encrypt(String password, String salt) {
        try {
            MessageDigest crypto = MessageDigest.getInstance("SHA-256");
            byte[] passBytes = (password + salt).getBytes();
            byte[] passHash = crypto.digest(passBytes);
            StringBuilder sb = new StringBuilder();
            for (byte b : passHash) {
                sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addNewRecord(Object data) throws SQLException {
        Administrator administrator = (Administrator) data;
        connection = DriverManager.getConnection(DB_URL);
        Statement stmt = connection.createStatement();
        String command = "INSERT INTO Administrator (id, firstName, lastName, email, phone, dateCreated) VALUES ('"
                + administrator.getID() + "', '"
                + administrator.getFirstName() + "', '"
                + administrator.getLastName() + "', '"
                + administrator.getEmail() + "', '"
                + administrator.getPhone() + "', '"
                + administrator.getDateCreated() + "')";
        stmt.executeUpdate(command);
        connection.close();
    }

    @Override
    public void updateRecord(Object data) throws SQLException {
        Administrator administrator = (Administrator) data;
        connection = DriverManager.getConnection(DB_URL);
        Statement stmt = connection.createStatement();
        String accountName = (administrator.getUserAccount() != null &&
                administrator.getUserAccount().getUserAccountName() != null)
                ? "'" + administrator.getUserAccount().getUserAccountName() + "'"
                : "NULL";

        String command = "UPDATE Administrator SET "
                + "firstName = '" + administrator.getFirstName() + "', "
                + "lastName = '" + administrator.getLastName() + "', "
                + "email = '" + administrator.getEmail() + "', "
                + "phone = '" + administrator.getPhone() + "', "
                + "dateCreated = '" + administrator.getDateCreated() + "', "
                + "userAccount = " + accountName
                + " WHERE id = '" + administrator.getID() + "'";
        stmt.executeUpdate(command);
        connection.close();
    }

    @Override
    public Object findOneRecord(String key) throws SQLException {
        Administrator admin = new Administrator();
        connection = DriverManager.getConnection(DB_URL);
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Administrator WHERE id = '" + key + "'");

        if (rs.next()) {
            admin.setID(rs.getString("id"));
            admin.setFirstName(rs.getString("firstName"));
            admin.setLastName(rs.getString("lastName"));
            admin.setEmail(rs.getString("email"));
            admin.setPhone(rs.getString("phone"));
            admin.setDateCreated(rs.getDate("dateCreated"));

            String accountName = rs.getString("userAccount");
            if (accountName != null) {
                UserAccountTableAdapter uaAdapter = new UserAccountTableAdapter(false);
                UserAccount ua = (UserAccount) uaAdapter.findOneRecord(accountName);
                if (ua.getUserAccountName() != null) {
                    admin.setUserAccount(ua);
                }
            }
        }

        connection.close();
        return admin;
    }

    @Override
    public Object findOneRecord(Object userAccount) throws SQLException {
        Administrator administrator = new Administrator();
        ResultSet rs;
        connection = DriverManager.getConnection(DB_URL);

        Statement stmt = connection.createStatement();
        String command = "SELECT * FROM Administrator WHERE userAccount = '" + ((UserAccount) userAccount).getUserAccountName() + "'";

        rs = stmt.executeQuery(command);
        while (rs.next()) {
            administrator.setID(rs.getString("id"));
            administrator.setFirstName(rs.getString("firstName"));
            administrator.setLastName(rs.getString("lastName"));
            administrator.setEmail(rs.getString("email"));
            administrator.setPhone(rs.getString("phone"));
            administrator.setDateCreated(rs.getDate("dateCreated"));

            administrator.setUserAccount((UserAccount) userAccount);
        }
        connection.close();
        return administrator;
    }


    @Override
    public void deleteOneRecord(Object data) throws SQLException {
        Administrator administrator = (Administrator) data;
        connection = DriverManager.getConnection(DB_URL);
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("DELETE FROM Administrator WHERE id = '" + administrator.getID() + "'");
        connection.close();
    }

    @Override
    public void deleteRecords(Object referencedObject) {}

    @Override
    public List<String> getKeys() throws SQLException {
        List<String> list = new ArrayList<>();
        connection = DriverManager.getConnection(DB_URL);
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT id FROM Administrator");
        while (rs.next()) {
            list.add(rs.getString(1));
        }
        connection.close();
        return list;
    }

    @Override
    public List<Object> getAllRecords() throws SQLException {
        List<Object> list = new ArrayList<>();
        connection = DriverManager.getConnection(DB_URL);
        Statement stmt = connection.createStatement();
        ResultSet result = stmt.executeQuery("SELECT * FROM Administrator");

        while (result.next()) {
            Administrator administrator = new Administrator();
            administrator.setID(result.getString("id"));
            administrator.setFirstName(result.getString("firstName"));
            administrator.setLastName(result.getString("lastName"));
            administrator.setEmail(result.getString("email"));
            administrator.setPhone(result.getString("phone"));
            administrator.setDateCreated(result.getDate("dateCreated"));

            String accountName = result.getString("userAccount");
            if (accountName != null) {
                UserAccountTableAdapter uaAdapter = new UserAccountTableAdapter(false);
                UserAccount ua = (UserAccount) uaAdapter.findOneRecord(accountName);
                if (ua.getUserAccountName() != null) {
                    administrator.setUserAccount(ua);
                }
            }

            list.add(administrator);
        }

        connection.close();
        return list;
    }

    @Override
    public List<Object> getAllRecords(Object referencedObject) { return null; }
}

