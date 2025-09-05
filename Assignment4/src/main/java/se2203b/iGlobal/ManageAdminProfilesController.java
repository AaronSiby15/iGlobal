package se2203b.iGlobal;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ManageAdminProfilesController {

    @FXML private TableView<Administrator> adminsTable;
    @FXML private TableColumn<Administrator, String> colFirstName, colLastName, colEmail, colPhone;
    @FXML private TableColumn<Administrator, String> colDateCreated;
    @FXML private Button btnAddAdmin, btnRemoveAdmin, btnExit;

    private ObservableList<Administrator> adminList = FXCollections.observableArrayList();
    private AdministratorTableAdapter adminTableAdapter;

    @FXML
    public void initialize() {
        try {
            adminTableAdapter = new AdministratorTableAdapter(false);
            loadAdmins();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colDateCreated.setCellValueFactory(new PropertyValueFactory<>("dateCreated"));

        btnRemoveAdmin.setDisable(true);
        adminsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null && "1".equals(newSelection.getID())) {
                btnRemoveAdmin.setDisable(true);
            } else {
                btnRemoveAdmin.setDisable(false);
            }
        });
    }

    public void loadAdmins() {
        try {
            adminList.clear();
            List<Object> admins = adminTableAdapter.getAllRecords();
            for (Object obj : admins) {
                if (obj instanceof Administrator) {
                    adminList.add((Administrator) obj);
                }
            }
            adminsTable.setItems(adminList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void addAdmin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("addNewAdmin-view.fxml"));
            Stage stage = new Stage();
            stage.getIcons().add(new Image("file:src/main/resources/se2203b/iGlobal/WesternLogo.png"));
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Add New Admin");
            stage.initModality(Modality.APPLICATION_MODAL);

            AddNewAdminController controller = loader.getController();
            controller.setAdminTableAdapter(adminTableAdapter);
            controller.setManageAdminProfilesController(this);

            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void removeAdmin() {
        Administrator selected = adminsTable.getSelectionModel().getSelectedItem();
        if (selected != null && !"1".equals(selected.getID())) {
            try {
                adminTableAdapter.deleteOneRecord(selected);
                adminList.remove(selected);
                btnRemoveAdmin.setDisable(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void closeWindow() {
        Stage stage = (Stage) btnExit.getScene().getWindow();
        stage.close();
    }
}
