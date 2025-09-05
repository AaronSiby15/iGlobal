package se2203b.iGlobal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class AgentTableAdapter implements DataStore {
    private Connection connection;
    private final String DB_URL = "jdbc:derby:iGlobalDB";

    public AgentTableAdapter(boolean reset) throws SQLException {
        connection = DriverManager.getConnection("jdbc:derby:iGlobalDB;create=true");
        Statement stmt = connection.createStatement();

        if (reset) {
            try {
                stmt.execute("DROP TABLE Agents");
            } catch (SQLException ex) {
                System.out.println("Table Agents did not exist, so no need to drop.");
            }
        }

        try {
            String command = "CREATE TABLE Agents ("
                    + "agentID VARCHAR(20) NOT NULL PRIMARY KEY, "
                    + "firstName VARCHAR(60) NOT NULL, "
                    + "lastName VARCHAR(60) NOT NULL, "
                    + "email VARCHAR(60), "
                    + "phone VARCHAR(20), "
                    + "licenceNumber VARCHAR(30), "
                    + "specialization VARCHAR(50) "
                    + ")";
            stmt.execute(command);
            System.out.println("Agents table created successfully.");
        } catch (SQLException ex) {
            System.out.println("Agents table already exists.");
        }


    }


    @Override
    public void addNewRecord(Object data) throws SQLException {
        Agent agent = (Agent) data;
        connection = DriverManager.getConnection(DB_URL);
        connection.setAutoCommit(false);

        String command = "INSERT INTO Agents (agentID, firstName, lastName, email, phone, licenceNumber, specialization, userAccount) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(command)) {
            stmt.setString(1, agent.getAgentID());
            stmt.setString(2, agent.getFirstName());
            stmt.setString(3, agent.getLastName());
            stmt.setString(4, agent.getEmail());
            stmt.setString(5, agent.getPhone());
            stmt.setString(6, agent.getLicenceNumber());
            stmt.setString(7, agent.getSpecialization());
            stmt.setString(8, agent.getUserAccount() != null ? agent.getUserAccount().getUserAccountName() : null);
            stmt.executeUpdate();
            connection.commit();
        }
    }



    @Override
    public void updateRecord(Object data) throws SQLException {
        Agent agent = (Agent) data;
        connection = DriverManager.getConnection(DB_URL);

        String command = "UPDATE Agents SET firstName=?, lastName=?, email=?, phone=?, licenceNumber=?, specialization=?, userAccount=? WHERE agentID=?";
        try (PreparedStatement stmt = connection.prepareStatement(command)) {
            stmt.setString(1, agent.getFirstName());
            stmt.setString(2, agent.getLastName());
            stmt.setString(3, agent.getEmail());
            stmt.setString(4, agent.getPhone());
            stmt.setString(5, agent.getLicenceNumber());
            stmt.setString(6, agent.getSpecialization());
            stmt.setString(7, agent.getUserAccount() != null ? agent.getUserAccount().getUserAccountName() : null);
            stmt.setString(8, agent.getAgentID());
            stmt.executeUpdate();
        }
    }


    @Override
    public Object findOneRecord(String key) throws SQLException {
        Agent agent = null;
        connection = DriverManager.getConnection(DB_URL);
        String command = "SELECT * FROM Agents WHERE agentID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(command)) {
            stmt.setString(1, key);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                agent = new Agent(
                        rs.getString("agentID"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("licenceNumber"),
                        rs.getString("specialization")
                );
            }
        }

        return agent;
    }

    @Override
    public Object findOneRecord(Object referencedObject) throws SQLException {
        UserAccount account = (UserAccount) referencedObject;
        Agent agent = null;

        connection = DriverManager.getConnection(DB_URL);
        String command = "SELECT * FROM Agents WHERE userAccount = ?";

        try (PreparedStatement stmt = connection.prepareStatement(command)) {
            stmt.setString(1, account.getUserAccountName());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                agent = new Agent(
                        rs.getString("agentID"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("licenceNumber"),
                        rs.getString("specialization")
                );

                agent.setUserAccount(account);
            }
        }

        connection.close();
        return agent;
    }


    @Override
    public void deleteOneRecord(Object data) throws SQLException {
        Agent agent = (Agent) data;
        connection = DriverManager.getConnection(DB_URL);
        String command = "DELETE FROM Agents WHERE agentID=?";
        try (PreparedStatement stmt = connection.prepareStatement(command)) {
            stmt.setString(1, agent.getAgentID());
            stmt.executeUpdate();
        }

    }

    @Override
    public List<String> getKeys() throws SQLException {
        List<String> keys = new ArrayList<>();
        connection = DriverManager.getConnection(DB_URL);
        String command = "SELECT agentID FROM Agents";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(command)) {
            while (rs.next()) {
                keys.add(rs.getString("agentID"));
            }
        }

        return keys;
    }

    @Override
    public List<Object> getAllRecords() throws SQLException {
        List<Object> list = new ArrayList<>();
        connection = DriverManager.getConnection(DB_URL);
        String command = "SELECT * FROM Agents";
        try (
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(command)
        ) {
            while (rs.next()) {
                Agent agent = new Agent(
                        rs.getString("agentID"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("licenceNumber"),
                        rs.getString("specialization")
                );

                String userAccountName = rs.getString("userAccount");
                if (userAccountName != null) {
                    UserAccountTableAdapter uaAdapter = new UserAccountTableAdapter(false);
                    UserAccount userAccount = (UserAccount) uaAdapter.findOneRecord(userAccountName);
                    agent.setUserAccount(userAccount);
                }

                list.add(agent);
            }
        }
        return list;
    }


    @Override
    public List<Object> getAllRecords(Object referencedObject) throws SQLException {
        return null;
    }


    public void removeAgent(String agentID) throws SQLException {
        connection = DriverManager.getConnection(DB_URL);
        String command = "DELETE FROM Agents WHERE agentID=?";
        try (PreparedStatement stmt = connection.prepareStatement(command)) {
            stmt.setString(1, agentID);
            stmt.executeUpdate();
        }

    }


    @Override
    public void deleteRecords(Object referencedObject) throws SQLException {
        // Not needed for this case
    }



}

