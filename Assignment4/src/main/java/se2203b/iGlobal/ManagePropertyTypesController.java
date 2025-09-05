package se2203b.iGlobal;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
/**
 * @author Abdelkader Ouda
 */
public class ManagePropertyTypesController implements Initializable {


    @FXML
    private Button addTypeBtn;
    @FXML
    private TableView propertyTypeTableView;

    @FXML
    private TableColumn<PropertyType, String> typeCodeCol;
    @FXML
    private TableColumn<PropertyType, String> typeNameCol;
    @FXML
    private TextField typeCode;
    @FXML
    private TextField typeName;

    private ObservableList<Object> propertyTypeViewData = FXCollections.observableArrayList();

    private DataStore propertyTypeDataStore;
    private IGlobalController iGlobalController;
    @FXML
    private Button removeTypeBtn;
    @FXML
    private Button exitBtn;

    public void setIGlobalController(IGlobalController controller) {
        iGlobalController = controller;
    }

    public void setDataStore(DataStore propertyType) {
        propertyTypeDataStore = propertyType;
        // retrieve saved data
        buildPropertyTypeData();
    }


    @FXML
    public void addTypeBtnClicked(ActionEvent actionEvent) {

        PropertyType propertyType = new PropertyType(typeCode.getText(), typeName.getText());
        List<Object> list = new ArrayList<>();
        list.add(propertyType);
        ObservableList<Object> observableArrayList = FXCollections.observableArrayList(list);
        propertyTypeViewData.addAll(observableArrayList);
        propertyTypeTableView.setItems(propertyTypeViewData);


        try {
            propertyTypeDataStore.addNewRecord(propertyType);
        } catch (SQLException e) {
            iGlobalController.displayAlert("ERROR: " + e.getMessage());
        }


        typeCode.setText("");
        typeName.setText("");

    }

    private void buildPropertyTypeData() {
        try {
            List<Object> list = propertyTypeDataStore.getAllRecords();
            ObservableList<Object> observableArrayList = FXCollections.observableArrayList(list);
            propertyTypeViewData.addAll(observableArrayList);
        } catch (SQLException ex) {
            iGlobalController.displayAlert("ERROR: " + ex.getMessage());
        }
    }

    @FXML
    public void onTypeCodeEdit(TableColumn.CellEditEvent<PropertyType, String> event) {


        try {
            PropertyType propertyType = event.getRowValue();
            String newValue = event.getNewValue();
            propertyType.setTypeCode(newValue);
            propertyTypeDataStore.updateRecord(propertyType);
            propertyType.setTypeCode(newValue);
        } catch (SQLException e) {
            iGlobalController.displayAlert("ERROR: " + e.getMessage());
        }

    }

    @FXML
    public void onTypeNameEdit(TableColumn.CellEditEvent<PropertyType, String> event) {
        try {
            PropertyType propertyType = event.getRowValue();
            String newValue = event.getNewValue();
            propertyType.setTypeName(newValue);
            propertyTypeDataStore.updateRecord(propertyType);
            propertyType.setTypeName(newValue);
        } catch (SQLException e) {
            iGlobalController.displayAlert("ERROR: " + e.getMessage());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        typeCodeCol.setCellValueFactory(cellData -> cellData.getValue().typeCodeProperty());
        typeCodeCol.setCellFactory(TextFieldTableCell.forTableColumn());

        typeNameCol.setCellValueFactory(cellData -> cellData.getValue().typeNameProperty());
        typeNameCol.setCellFactory(TextFieldTableCell.forTableColumn());

        propertyTypeTableView.setItems(propertyTypeViewData);
    }

    @FXML
    public void deletePropertyType(ActionEvent actionEvent) {
        PropertyType selectedItem = (PropertyType) propertyTypeTableView.getSelectionModel().getSelectedItem();
        try {
            propertyTypeDataStore.deleteOneRecord(selectedItem);
            propertyTypeTableView.getItems().remove(selectedItem);
        } catch (SQLException e) {
            iGlobalController.displayAlert("ERROR: " + e.getMessage());
        }
    }

    @FXML
    public void closeForm(ActionEvent actionEvent) {
        Stage stage = (Stage) exitBtn.getScene().getWindow();
        stage.close();
    }
}
