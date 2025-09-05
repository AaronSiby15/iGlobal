package se2203b.iGlobal;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class AddNewAgentController {


    @FXML
    private TextField txtFirstName, txtLastName, txtEmail, txtPhone, txtLicenceNumber, txtSpecialization;

    private ManageAgentProfilesController manageAgentProfilesController;

    public void setManageAgentProfilesController(ManageAgentProfilesController controller) {
        this.manageAgentProfilesController = controller;}

    private AgentTableAdapter agentTableAdapter;

    public void setAgentTableAdapter(AgentTableAdapter adapter) {
        this.agentTableAdapter = adapter;
    }

    @FXML
    public void handleSave() {
        String firstName = txtFirstName.getText();
        String lastName = txtLastName.getText();
        String email = txtEmail.getText();
        String phone = txtPhone.getText();
        String licenceNumber = txtLicenceNumber.getText();
        String specialization = txtSpecialization.getText();

        if (firstName.isEmpty() || lastName.isEmpty()) {
            System.out.println("First and Last name are required.");
            return;
        }

        String id = "A" + System.currentTimeMillis();
        Agent newAgent = new Agent(id, firstName, lastName, email, phone, licenceNumber, specialization);

        try {
            agentTableAdapter.addNewRecord(newAgent);

            if (manageAgentProfilesController != null) {
                manageAgentProfilesController.loadAgents();
            }

            System.out.println("Agent added successfully (no user account yet).");
        } catch (Exception e) {
            e.printStackTrace();
        }

        ((Stage) txtFirstName.getScene().getWindow()).close();
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




    @FXML
    public void handleCancel() {

        Stage stage = (Stage) txtFirstName.getScene().getWindow();
        stage.close();
    }
}
