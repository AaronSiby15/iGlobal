package se2203b.iGlobal;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML private TextField user;
    @FXML private PasswordField password;
    @FXML private Button saveBtn;
    @FXML private Button cancelBtn;
    @FXML private Label errorMsg;

    IGlobalController iGlobalController;

    private DataStore userAccountTable;
    private DataStore agentTable, executiveTable, adminTable;

    public void setDataStore(DataStore accountAdapter) {
        userAccountTable = accountAdapter;
    }



    public void authorize() {
        errorMsg.setText("");
        try {
            // Find user account
            UserAccount account = (UserAccount) userAccountTable.findOneRecord(user.getText());
            if (account.getUserAccountName() == null) {
                errorMsg.setText("Incorrect username");
                return;
            }

            // Encrypt and compare password
            String salt = account.getPasswordSalt();
            String encryptedPassword = encrypt(password.getText(), salt);

            if (!encryptedPassword.equals(account.getEncryptedPassword())) {
                errorMsg.setText("Incorrect password");
                return;
            }

            // Account is valid
            String privilege = account.getAccountType();
            iGlobalController.setUserName(account.getUserAccountName());

            if (privilege.equals("admin")) {
                adminTable = new AdministratorTableAdapter(false);
                Administrator admin = (Administrator) adminTable.findOneRecord(account);
                if (account.getUserAccountName().equals("admin")) {
                    iGlobalController.setUserFullname("Admin");
                } else {
                    iGlobalController.setUserFullname(admin.getFirstName() + " " + admin.getLastName());
                }
                iGlobalController.enableAdminControls();

            } else if (privilege.equals("executive")) {
                executiveTable = new ExecutiveTableAdapter(false);
                Executive exec = (Executive) executiveTable.findOneRecord(account);
                iGlobalController.setUserFullname(exec.getFirstName() + " " + exec.getLastName());
                iGlobalController.enableExecutiveControls();

            } else if (privilege.equals("agent")) {
                agentTable = new AgentTableAdapter(false);
                Agent agent = (Agent) agentTable.findOneRecord(account);
                iGlobalController.setUserFullname(agent.getFirstName() + " " + agent.getLastName());
                iGlobalController.enableAgentControls();
            }

            // Close login window
            Stage stage = (Stage) cancelBtn.getScene().getWindow();
            stage.close();

        } catch (SQLException e) {
            iGlobalController.displayAlert("Login error: " + e.getMessage());
        }
    }

    public void authenticated(UserAccount userAccount, String privilege) {
        iGlobalController.setUserName(userAccount.getUserAccountName());

        try {
            if (privilege.equals("admin")) {
                adminTable = new AdministratorTableAdapter(false);
                Administrator admin = (Administrator) adminTable.findOneRecord(userAccount);

                if (userAccount.getUserAccountName().equals("admin")) {
                    iGlobalController.setUserFullname("Admin");
                } else {
                    iGlobalController.setUserFullname(admin.getFirstName() + " " + admin.getLastName());
                }

                iGlobalController.enableAdminControls();

            } else if (privilege.equals("executive")) {
                executiveTable = new ExecutiveTableAdapter(false);
                Executive exec = (Executive) executiveTable.findOneRecord(userAccount);

                iGlobalController.setUserFullname(exec.getFirstName() + " " + exec.getLastName());
                iGlobalController.enableExecutiveControls();

            } else if (privilege.equals("agent")) {
                agentTable = new AgentTableAdapter(false);
                Agent agent = (Agent) agentTable.findOneRecord(userAccount);

                iGlobalController.setUserFullname(agent.getFirstName() + " " + agent.getLastName());
                iGlobalController.enableAgentControls();
            }

            // Close login popup
            Stage stage = (Stage) cancelBtn.getScene().getWindow();
            stage.close();

        } catch (SQLException e) {
            iGlobalController.displayAlert("ERROR-Login: " + e.getMessage());
        }
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

    public void cancel() {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }

    public void clearErrorMsg() {
        errorMsg.setText("");
    }

    public void setIGlobalController(IGlobalController controller) {
        iGlobalController = controller;
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        errorMsg.setText("");
    }
}