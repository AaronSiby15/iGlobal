package se2203b.iGlobal;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.Random;
import java.util.ResourceBundle;
/**
 * @author Abdelkader Ouda
 */
public class ChangePasswordController implements Initializable {

    @FXML
    private Button cancelBtn;

    @FXML
    private TextField newPassword1;

    @FXML
    private TextField newPassword2;

    @FXML
    private TextField oldPassword;

    @FXML
    private Button saveBtn;

    @FXML
    private Label username;

    @FXML
    private Label errorMsg;

    private String loggedInUser, userAccountName;
    IGlobalController iGlobalController;

    private DataStore userAccountTable;

    public void setDataStore(DataStore accountAdapter) {
        userAccountTable = accountAdapter;
    }


    public void setIGlobalController(IGlobalController controller) {
        iGlobalController = controller;
        loggedInUser = controller.getUserFullname();
        userAccountName = controller.getUserAccountName();
        username.setText("Change password for " + loggedInUser);
    }

    public void changePassword() {
        errorMsg.setText("");
        try {
            UserAccount account = (UserAccount) userAccountTable.findOneRecord(userAccountName);
            String salt = account.getPasswordSalt();
            String encryptedPassword = iGlobalController.encrypt(oldPassword.getText(), salt);
            String retrievedEncryptedPassword = account.getEncryptedPassword();
            if (encryptedPassword.equals(retrievedEncryptedPassword)) {
                if (newPassword1.getText().equals(newPassword2.getText())) {
                    Random random = new Random();
                    String newSalt = Integer.toString(random.nextInt());
                    String encryptedNewPassword = iGlobalController.encrypt(newPassword1.getText(), newSalt);

                    account.setEncryptedPassword(encryptedNewPassword);
                    account.setPasswordSalt(newSalt);
                    try {
                        userAccountTable.updateRecord(account);

                        // Get current stage reference
                        Stage stage = (Stage) cancelBtn.getScene().getWindow();

                        stage.close();
                        iGlobalController.logout();
                    } catch (SQLException e) {
                        iGlobalController.displayAlert("Change password: " + e.getMessage());
                    }
                } else {
                    // wrong new password
                    errorMsg.setText("The new passwords do not match");
                }
            } else {
                // wrong password
                errorMsg.setText("Wrong old password");
            }
        } catch (SQLException ex) {
            iGlobalController.displayAlert("Find User Account: " + ex.getMessage());
        }
    }

    public void cancel() {
        // Get current stage reference
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        // Close stage
        stage.close();
    }

    public void clearErrorMsg() {
        errorMsg.setText("");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
