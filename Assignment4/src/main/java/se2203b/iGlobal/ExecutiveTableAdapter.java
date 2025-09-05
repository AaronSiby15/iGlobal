package se2203b.iGlobal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExecutiveTableAdapter implements DataStore {
    private Connection connection;
    private final String DB_URL = "jdbc:derby:iGlobalDB;create=true";

    public ExecutiveTableAdapter(boolean reset) throws SQLException {
        connection = DriverManager.getConnection(DB_URL);
        Statement stmt = connection.createStatement();

        if (reset) {
            try {
                stmt.execute("DROP TABLE Executives");
            } catch (SQLException ex) {
                System.out.println("Executives table did not exist, so no need to drop.");
            }
        }

        try {
            String command = "CREATE TABLE Executives ("
                    + "firstName VARCHAR(60) NOT NULL, "
                    + "lastName VARCHAR(60) NOT NULL, "
                    + "email VARCHAR(60), "
                    + "phone VARCHAR(20), "
                    + "privatePhone VARCHAR(20), "
                    + "userAccount VARCHAR(30) REFERENCES UserAccount(userAccountName)"
                    + ")";
            stmt.execute(command);
            System.out.println("Executives table created successfully.");
        } catch (SQLException ex) {
            System.out.println("Executives table already exists.");
        }
    }

    @Override
    public void addNewRecord(Object data) throws SQLException {
        Executive executive = (Executive) data;
        connection = DriverManager.getConnection(DB_URL);
        String command = "INSERT INTO Executives (firstName, lastName, email, phone, privatePhone, userAccount) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(command)) {
            stmt.setString(1, executive.getFirstName());
            stmt.setString(2, executive.getLastName());
            stmt.setString(3, executive.getEmail());
            stmt.setString(4, executive.getPhone());
            stmt.setString(5, executive.getPrivatePhone());
            stmt.setString(6, executive.getUserAccount() != null ? executive.getUserAccount().getUserAccountName() : null);
            stmt.executeUpdate();
        }
    }

    @Override
    public void updateRecord(Object data) throws SQLException {
        Executive executive = (Executive) data;
        connection = DriverManager.getConnection(DB_URL);
        String command = "UPDATE Executives SET firstName=?, lastName=?, phone=?, privatePhone=?, userAccount=? WHERE email=?";
        try (PreparedStatement stmt = connection.prepareStatement(command)) {
            stmt.setString(1, executive.getFirstName());
            stmt.setString(2, executive.getLastName());
            stmt.setString(3, executive.getPhone());
            stmt.setString(4, executive.getPrivatePhone());
            stmt.setString(5, executive.getUserAccount() != null ? executive.getUserAccount().getUserAccountName() : null);
            stmt.setString(6, executive.getEmail());
            stmt.executeUpdate();
        }
    }

    @Override
    public Object findOneRecord(String key) throws SQLException {
        // Optional: Implement if needed
        return null;
    }

    @Override
    public Object findOneRecord(Object referencedObject) throws SQLException {
        UserAccount account = (UserAccount) referencedObject;
        Executive executive = null;

        connection = DriverManager.getConnection(DB_URL);
        String command = "SELECT * FROM Executives WHERE userAccount = ?";

        try (PreparedStatement stmt = connection.prepareStatement(command)) {
            stmt.setString(1, account.getUserAccountName());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                executive = new Executive(
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("privatePhone")
                );

                // Link back the UserAccount
                executive.setUserAccount(account);
            }
        }

        connection.close();
        return executive;
    }


    @Override
    public void deleteOneRecord(Object data) throws SQLException {
        Executive executive = (Executive) data;
        connection = DriverManager.getConnection(DB_URL);
        String command = "DELETE FROM Executives WHERE firstName=? AND lastName=? AND email=?";
        try (PreparedStatement stmt = connection.prepareStatement(command)) {
            stmt.setString(1, executive.getFirstName());
            stmt.setString(2, executive.getLastName());
            stmt.setString(3, executive.getEmail());
            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteRecords(Object referencedObject) throws SQLException {
        // Not used
    }

    @Override
    public List<String> getKeys() throws SQLException {
        return new ArrayList<>();
    }

    @Override
    public List<Object> getAllRecords() throws SQLException {
        List<Object> list = new ArrayList<>();
        connection = DriverManager.getConnection(DB_URL);
        String command = "SELECT * FROM Executives";
        try (
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(command)
        ) {
            while (rs.next()) {
                Executive executive = new Executive(
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("privatePhone")
                );

                String userAccountName = rs.getString("userAccount");
                if (userAccountName != null) {
                    UserAccountTableAdapter uaAdapter = new UserAccountTableAdapter(false);
                    UserAccount userAccount = (UserAccount) uaAdapter.findOneRecord(userAccountName);
                    executive.setUserAccount(userAccount);
                }

                list.add(executive);
            }
        }
        return list;
    }

    @Override
    public List<Object> getAllRecords(Object referencedObject) throws SQLException {
        return null;
    }
}
