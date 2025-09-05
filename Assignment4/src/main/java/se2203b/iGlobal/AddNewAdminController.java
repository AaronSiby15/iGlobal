package se2203b.iGlobal;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Date;

public class AddNewAdminController {

    @FXML private TextField txtFirstName, txtLastName, txtEmail, txtPhone;
    @FXML private DatePicker dateHired;

    private AdministratorTableAdapter adminTableAdapter;
    private ManageAdminProfilesController manageAdminProfilesController;

    public void setAdminTableAdapter(AdministratorTableAdapter adapter) {
        this.adminTableAdapter = adapter;
    }

    public void setManageAdminProfilesController(ManageAdminProfilesController controller) {
        this.manageAdminProfilesController = controller;
    }

    @FXML
    public void handleSave() {
        String firstName = txtFirstName.getText();
        String lastName = txtLastName.getText();
        String email = txtEmail.getText();
        String phone = txtPhone.getText();
        Date date = Date.valueOf(dateHired.getValue());

        if (firstName.isEmpty() || lastName.isEmpty()) {
            System.out.println("First and Last name are required.");
            return;
        }

        try {
            Administrator admin = new Administrator();
            admin.setID(String.valueOf(System.currentTimeMillis()));
            admin.setFirstName(firstName);
            admin.setLastName(lastName);
            admin.setEmail(email);
            admin.setPhone(phone);
            admin.setDateCreated(date);

            adminTableAdapter.addNewRecord(admin);
            System.out.println("Admin added successfully!");

            if (manageAdminProfilesController != null) {
                manageAdminProfilesController.loadAdmins();
            }

            Stage stage = (Stage) txtFirstName.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleCancel() {
        Stage stage = (Stage) txtFirstName.getScene().getWindow();
        stage.close();
    }
}
