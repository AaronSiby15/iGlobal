package se2203b.iGlobal;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.*;


public class ManageUserAccountController implements Initializable {

    @FXML private ComboBox<String> userDropdown;
    @FXML private TextField emailField;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Button deleteButton;
    @FXML private Button cancelButton;
    @FXML private Button saveButton;
    @FXML private Label newLabel;


    private UserAccountTableAdapter userAccountTableAdapter;
    private Map<String, Object> labelToUserMap = new HashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            userAccountTableAdapter = new UserAccountTableAdapter(false);
            setupUserDropdown();
            setupListeners();
            emailField.setDisable(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setupUserDropdown() throws SQLException {
        List<String> labels = new ArrayList<>();
        labelToUserMap.clear();

        // Admins
        AdministratorTableAdapter adminAdapter = new AdministratorTableAdapter(false);
        for (Object obj : adminAdapter.getAllRecords()) {
            if (obj instanceof Administrator) {
                Administrator admin = (Administrator) obj;
                String label = admin.getFirstName() + " " + admin.getLastName() + " [Admin]";
                labels.add(label);
                labelToUserMap.put(label, admin);
            }
        }

        // Executives
        ExecutiveTableAdapter execAdapter = new ExecutiveTableAdapter(false);
        for (Object obj : execAdapter.getAllRecords()) {
            if (obj instanceof Executive) {
                Executive exec = (Executive) obj;
                String label = exec.getFirstName() + " " + exec.getLastName() + " [Executive]";
                labels.add(label);
                labelToUserMap.put(label, exec);
            }
        }

        // Agents
        AgentTableAdapter agentAdapter = new AgentTableAdapter(false);
        for (Object obj : agentAdapter.getAllRecords()) {
            if (obj instanceof Agent) {
                Agent agent = (Agent) obj;
                String label = agent.getFirstName() + " " + agent.getLastName() + " [Agent]";
                labels.add(label);
                labelToUserMap.put(label, agent);
            }
        }

        userDropdown.setItems(FXCollections.observableArrayList(labels));
    }

    private void setupListeners() {
        userDropdown.setOnAction(event -> {
            String selected = userDropdown.getValue();
            if (selected == null || selected.isEmpty()) return;

            Object obj = labelToUserMap.get(selected);

            emailField.clear();
            usernameField.clear();
            passwordField.clear();
            confirmPasswordField.clear();

            if (obj instanceof Administrator) {
                Administrator admin = (Administrator) obj;
                emailField.setText(admin.getEmail());

                if ("1".equals(admin.getID()) &&
                        "Default iGlobal".equals(admin.getFirstName()) &&
                        "Admin".equals(admin.getLastName())) {

                    usernameField.setText("admin");
                    passwordField.setText("********");
                    confirmPasswordField.setText("********");

                    usernameField.setDisable(true);
                    passwordField.setDisable(true);
                    confirmPasswordField.setDisable(true);
                    saveButton.setDisable(true);
                    deleteButton.setDisable(true);
                    newLabel.setVisible(false);


                } else if (admin.getUserAccount() != null &&
                        admin.getUserAccount().getUserAccountName() != null) {

                    usernameField.setText(admin.getUserAccount().getUserAccountName());
                    disableAccountInputs();
                    deleteButton.setDisable(false);
                    newLabel.setVisible(false);
                } else {
                    enableAccountInputs();
                    deleteButton.setDisable(true);
                    newLabel.setVisible(true);
                }

            } else if (obj instanceof Executive) {
                Executive exec = (Executive) obj;
                emailField.setText(exec.getEmail());

                if (exec.getUserAccount() != null) {
                    usernameField.setText(exec.getUserAccount().getUserAccountName());
                    disableAccountInputs();
                    deleteButton.setDisable(false);
                    newLabel.setVisible(true);
                } else {
                    enableAccountInputs();
                    deleteButton.setDisable(true);
                    newLabel.setVisible(true);
                }

            } else if (obj instanceof Agent) {
                Agent agent = (Agent) obj;
                emailField.setText(agent.getEmail());

                if (agent.getUserAccount() != null) {
                    usernameField.setText(agent.getUserAccount().getUserAccountName());
                    disableAccountInputs();
                    deleteButton.setDisable(false);
                    newLabel.setVisible(true);
                } else {
                    enableAccountInputs();
                    deleteButton.setDisable(true);
                    newLabel.setVisible(true);
                }
            }
        });
    }



    private void disableAccountInputs() {
        usernameField.setDisable(true);
        passwordField.setText("********");
        confirmPasswordField.setText("********");
        passwordField.setDisable(true);
        confirmPasswordField.setDisable(true);
        saveButton.setDisable(true);
    }

    private void enableAccountInputs() {
        usernameField.setDisable(false);
        passwordField.setDisable(false);
        confirmPasswordField.setDisable(false);
        saveButton.setDisable(false);
        passwordField.clear();
        confirmPasswordField.clear();
    }

    @FXML
    private void handleSave() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String selected = userDropdown.getValue();

        if (selected == null || selected.isEmpty()) {
            showAlert("Please select a user.");
            return;
        }

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Username and password cannot be empty.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert("Passwords do not match.");
            return;
        }

        try {
            String salt = Integer.toString(new Random().nextInt());
            String encryptedPassword = encrypt(password, salt);

            String accountType;
            if (selected.endsWith("[Admin]")) {
                accountType = "admin";
            } else if (selected.endsWith("[Executive]")) {
                accountType = "executive";
            } else {
                accountType = "agent";
            }

            UserAccount newAccount = new UserAccount(username, encryptedPassword, salt, accountType);
            userAccountTableAdapter.addNewRecord(newAccount);

            if (accountType.equals("admin")) {
                AdministratorTableAdapter adapter = new AdministratorTableAdapter(false);
                for (Object obj : adapter.getAllRecords()) {
                    Administrator admin = (Administrator) obj;
                    String label = admin.getFirstName() + " " + admin.getLastName() + " [Admin]";
                    if (label.equals(selected)) {
                        admin.setUserAccount(newAccount);
                        adapter.updateRecord(admin);
                        break;
                    }
                }
            } else if (accountType.equals("executive")) {
                ExecutiveTableAdapter adapter = new ExecutiveTableAdapter(false);
                for (Object obj : adapter.getAllRecords()) {
                    Executive exec = (Executive) obj;
                    String label = exec.getFirstName() + " " + exec.getLastName() + " [Executive]";
                    if (label.equals(selected)) {
                        exec.setUserAccount(newAccount);
                        adapter.updateRecord(exec);
                        break;
                    }
                }
            } else {
                AgentTableAdapter adapter = new AgentTableAdapter(false);
                for (Object obj : adapter.getAllRecords()) {
                    Agent agent = (Agent) obj;
                    String label = agent.getFirstName() + " " + agent.getLastName() + " [Agent]";
                    if (label.equals(selected)) {
                        agent.setUserAccount(newAccount);
                        adapter.updateRecord(agent);
                        break;
                    }
                }
            }

            setupUserDropdown();
            clearForm();


        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @FXML
    private void handleDelete() {
        String selected = userDropdown.getValue();
        if (selected == null || selected.isEmpty()) {
            showAlert("Please select a user.");
            return;
        }

        try {
            String role = selected.substring(selected.indexOf("[") + 1, selected.indexOf("]"));
            String fullName = selected.substring(0, selected.indexOf("[")).trim();
            String username = usernameField.getText();

            if (username == null || username.isEmpty()) {
                showAlert("No username to delete.");
                return;
            }

            if (role.equalsIgnoreCase("Admin")) {
                AdministratorTableAdapter adapter = new AdministratorTableAdapter(false);
                for (Object obj : adapter.getAllRecords()) {
                    Administrator admin = (Administrator) obj;
                    String label = admin.getFirstName() + " " + admin.getLastName();
                    if (label.equals(fullName)) {
                        admin.setUserAccount(null);
                        adapter.updateRecord(admin);
                        break;
                    }
                }
            } else if (role.equalsIgnoreCase("Executive")) {
                ExecutiveTableAdapter adapter = new ExecutiveTableAdapter(false);
                for (Object obj : adapter.getAllRecords()) {
                    Executive exec = (Executive) obj;
                    String label = exec.getFirstName() + " " + exec.getLastName();
                    if (label.equals(fullName)) {
                        exec.setUserAccount(null);
                        adapter.updateRecord(exec);
                        break;
                    }
                }
            } else {
                AgentTableAdapter adapter = new AgentTableAdapter(false);
                for (Object obj : adapter.getAllRecords()) {
                    Agent agent = (Agent) obj;
                    String label = agent.getFirstName() + " " + agent.getLastName();
                    if (label.equals(fullName)) {
                        agent.setUserAccount(null);
                        adapter.updateRecord(agent);
                        break;
                    }
                }
            }

            UserAccount account = (UserAccount) userAccountTableAdapter.findOneRecord(username);
            if (account != null) {
                userAccountTableAdapter.deleteOneRecord(account);
            }

            ;
            clearForm();
            setupUserDropdown();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Failed to delete user.");
        }
    }

    @FXML
    private void handleCancel() {
        clearForm();
    }

    private void clearForm() {
        userDropdown.getSelectionModel().clearSelection();
        emailField.clear();
        usernameField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
        enableAccountInputs();
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private String encrypt(String password, String salt) {
        try {
            MessageDigest crypto = MessageDigest.getInstance("SHA-256");
            byte[] passBytes = (password + salt).getBytes();
            byte[] passHash = crypto.digest(passBytes);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < passHash.length; i++) {
                sb.append(Integer.toString((passHash[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
