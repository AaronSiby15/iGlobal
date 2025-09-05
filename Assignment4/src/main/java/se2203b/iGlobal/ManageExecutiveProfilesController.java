package se2203b.iGlobal;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ManageExecutiveProfilesController {

    @FXML private TableView<Executive> executivesTable;
    @FXML private TableColumn<Executive, String> colFirstName, colLastName, colEmail, colPhone, colPrivatePhone;
    @FXML private Button btnAddExecutive, btnRemoveExecutive, btnExit;

    private ObservableList<Executive> executiveList = FXCollections.observableArrayList();
    private ExecutiveTableAdapter executiveTableAdapter;

    @FXML
    public void initialize() {
        try {
            executiveTableAdapter = new ExecutiveTableAdapter(false);
            loadExecutives();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        executivesTable.setEditable(true);

        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colPrivatePhone.setCellValueFactory(new PropertyValueFactory<>("privatePhone"));

        colFirstName.setCellFactory(TextFieldTableCell.forTableColumn());
        colFirstName.setOnEditCommit(e -> {
            e.getRowValue().firstNameProperty().set(e.getNewValue());
            saveEditedExecutive(e.getRowValue());
        });

        colLastName.setCellFactory(TextFieldTableCell.forTableColumn());
        colLastName.setOnEditCommit(e -> {
            e.getRowValue().lastNameProperty().set(e.getNewValue());
            saveEditedExecutive(e.getRowValue());
        });

        colEmail.setCellFactory(TextFieldTableCell.forTableColumn());
        colEmail.setOnEditCommit(e -> {
            e.getRowValue().emailProperty().set(e.getNewValue());
            saveEditedExecutive(e.getRowValue());
        });

        colPhone.setCellFactory(TextFieldTableCell.forTableColumn());
        colPhone.setOnEditCommit(e -> {
            e.getRowValue().phoneProperty().set(e.getNewValue());
            saveEditedExecutive(e.getRowValue());
        });

        colPrivatePhone.setCellFactory(TextFieldTableCell.forTableColumn());
        colPrivatePhone.setOnEditCommit(e -> {
            e.getRowValue().privatePhoneProperty().set(e.getNewValue());
            saveEditedExecutive(e.getRowValue());
        });
    }

    public void loadExecutives() {
        try {
            executiveList.clear();
            List<Object> records = executiveTableAdapter.getAllRecords();
            for (Object obj : records) {
                if (obj instanceof Executive) {
                    executiveList.add((Executive) obj);
                }
            }
            executivesTable.setItems(executiveList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void addExecutive() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("addNewExecutive-view.fxml"));
            Stage stage = new Stage();
            stage.getIcons().add(new Image("file:src/main/resources/se2203b/iGlobal/WesternLogo.png"));
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Add New Executive");
            stage.initModality(Modality.APPLICATION_MODAL);

            AddNewExecutiveController controller = loader.getController();
            controller.setExecutiveTableAdapter(executiveTableAdapter);
            controller.setManageExecutiveProfilesController(this);

            stage.showAndWait(); // Refresh handled in child
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void removeExecutive() {
        Executive selected = executivesTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                executiveTableAdapter.deleteOneRecord(selected);
                executiveList.remove(selected);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveEditedExecutive(Executive exec) {
        try {
            executiveTableAdapter.updateRecord(exec);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void closeWindow() {
        Stage stage = (Stage) btnExit.getScene().getWindow();
        stage.close();
    }
}
