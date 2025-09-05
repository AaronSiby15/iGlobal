package se2203b.iGlobal;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddNewExecutiveController {

    @FXML private TextField txtFirstName, txtLastName, txtEmail, txtPhone, txtPrivatePhone;

    private ExecutiveTableAdapter executiveTableAdapter;
    private ManageExecutiveProfilesController manageExecutiveProfilesController;

    public void setExecutiveTableAdapter(ExecutiveTableAdapter adapter) {
        this.executiveTableAdapter = adapter;
    }

    public void setManageExecutiveProfilesController(ManageExecutiveProfilesController controller) {
        this.manageExecutiveProfilesController = controller;
    }

    @FXML
    public void handleSave() {
        String firstName = txtFirstName.getText();
        String lastName = txtLastName.getText();
        String email = txtEmail.getText();
        String phone = txtPhone.getText();
        String privatePhone = txtPrivatePhone.getText();

        if (firstName.isEmpty() || lastName.isEmpty()) {
            System.out.println("First and Last name are required.");
            return;
        }

        Executive exec = new Executive(firstName, lastName, email, phone, privatePhone);

        try {
            executiveTableAdapter.addNewRecord(exec);

            System.out.println("Executive added successfully.");
            if (manageExecutiveProfilesController != null) {
                manageExecutiveProfilesController.loadExecutives();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Stage stage = (Stage) txtFirstName.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void handleCancel() {
        Stage stage = (Stage) txtFirstName.getScene().getWindow();
        stage.close();
    }
}
