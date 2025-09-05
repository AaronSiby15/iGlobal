package se2203b.iGlobal;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.sql.SQLException;
import java.util.List;

public class ManagePropertyRecordsController {

    @FXML private TableView<Property> propertyTable;
    @FXML private TableColumn<Property, String> colPropertyType;
    @FXML private TableColumn<Property, Double> colLotSize;
    @FXML private TableColumn<Property, Double> colSquareFootage;
    @FXML private TableColumn<Property, Integer> colBedrooms;
    @FXML private TableColumn<Property, Double> colBathrooms;
    @FXML private TableColumn<Property, Integer> colYear;
    @FXML private TableColumn<Property, Double> colPrice;
    @FXML private TableColumn<Property, String> colProvince;
    @FXML private TableColumn<Property, String> colCity;
    @FXML private TableColumn<Property, String> colAddress;
    @FXML private TableColumn<Property, String> colPostalCode;

    @FXML private Button addPropertyButton, removeButton, exitButton;

    private PropertyTableAdapter propertyAdapter;
    private ObservableList<Property> propertyList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        propertyTable.setEditable(true);

        colPropertyType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colLotSize.setCellValueFactory(new PropertyValueFactory<>("lotSize"));
        colSquareFootage.setCellValueFactory(new PropertyValueFactory<>("squareFootage"));
        colBedrooms.setCellValueFactory(new PropertyValueFactory<>("bedrooms"));
        colBathrooms.setCellValueFactory(new PropertyValueFactory<>("bathrooms"));
        colYear.setCellValueFactory(new PropertyValueFactory<>("yearBuilt"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colProvince.setCellValueFactory(new PropertyValueFactory<>("province"));
        colCity.setCellValueFactory(new PropertyValueFactory<>("city"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("street"));
        colPostalCode.setCellValueFactory(new PropertyValueFactory<>("postalCode"));


        propertyTable.setItems(propertyList);

        colPropertyType.setCellFactory(TextFieldTableCell.forTableColumn());
        colPropertyType.setOnEditCommit(e -> commit(e, Property::setType));

        colLotSize.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        colLotSize.setOnEditCommit(e -> commit(e, Property::setLotSize));

        colSquareFootage.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        colSquareFootage.setOnEditCommit(e -> commit(e, Property::setSquareFootage));

        colBedrooms.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        colBedrooms.setOnEditCommit(e -> commit(e, Property::setBedrooms));

        colBathrooms.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        colBathrooms.setOnEditCommit(e -> commit(e, Property::setBathrooms));

        colYear.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        colYear.setOnEditCommit(e -> commit(e, Property::setYearBuilt));

        colPrice.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        colPrice.setOnEditCommit(e -> commit(e, Property::setPrice));

        colProvince.setCellFactory(TextFieldTableCell.forTableColumn());
        colProvince.setOnEditCommit(e -> commit(e, Property::setProvince));

        colCity.setCellFactory(TextFieldTableCell.forTableColumn());
        colCity.setOnEditCommit(e -> commit(e, Property::setCity));

        colAddress.setCellFactory(TextFieldTableCell.forTableColumn());
        colAddress.setOnEditCommit(e -> commit(e, Property::setStreet));

        colPostalCode.setCellFactory(TextFieldTableCell.forTableColumn());
        colPostalCode.setOnEditCommit(e -> commit(e, Property::setPostalCode));

        try {
            propertyAdapter = new PropertyTableAdapter(false);
            loadProperties();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private <T> void commit(TableColumn.CellEditEvent<Property, T> event, java.util.function.BiConsumer<Property, T> setter) {
        Property property = event.getRowValue();
        setter.accept(property, event.getNewValue());
        updatePropertyInDatabase(property);
    }

    private void updatePropertyInDatabase(Property property) {
        try {
            propertyAdapter.updateRecord(property);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadProperties() {
        try {
            List<Property> properties = propertyAdapter.getAllProperties();
            propertyList.setAll(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddNewProperty() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/se2203b/iGlobal/addNewPropertyRecord-view.fxml"));
            Parent root = loader.load();

            AddNewPropertyRecordController controller = loader.getController();
            controller.setPropertyTableAdapter(propertyAdapter);
            controller.setManagePropertyRecordsController(this);
            controller.setCityTableAdapter(new CityTableAdapter()); // ✅ NEW
            controller.setProvinceTableAdapter(new ProvinceTableAdapter()); // ✅ NEW
            controller.loadProvinceAndCityDropdowns(); // ✅ new line here

            controller.setPropertyTypeAdapter(new PropertyTypeTableAdapter(false));
            controller.loadPropertyTypes();


            Stage stage = new Stage();
            stage.getIcons().add(new Image("file:src/main/resources/se2203b/iGlobal/WesternLogo.png"));
            stage.setTitle("Add New Property Record");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            loadProperties();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRemoveProperty() {
        Property selected = propertyTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                propertyAdapter.deleteOneRecord(selected);
                propertyList.remove(selected);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleExit() {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }
}
