package se2203b.iGlobal;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;


import java.util.UUID;


public class AddNewPropertyRecordController {

    @FXML private ComboBox<String> propertyTypeCombo;
    @FXML private TextField streetField;
    @FXML private TextField postalCodeField;
    @FXML private ComboBox<String> provinceCombo;
    @FXML private ComboBox<String> cityCombo;
    @FXML private TextField lotSizeField;
    @FXML private TextField squareFootageField;
    @FXML private TextField bedroomsField;
    @FXML private TextField bathroomsField;
    @FXML private TextField yearBuiltField;
    @FXML private TextField amenitiesField;
    @FXML private TextField priceField;
    @FXML private TextField descriptionField;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;


    private PropertyTableAdapter propertyAdapter;
    private ManagePropertyRecordsController manageController;
    private CityTableAdapter cityAdapter;
    private ProvinceTableAdapter provinceAdapter;

    public void setPropertyTableAdapter(PropertyTableAdapter adapter) {
        this.propertyAdapter = adapter;
    }

    public void setManagePropertyRecordsController(ManagePropertyRecordsController controller) {
        this.manageController = controller;
    }
    private PropertyTypeTableAdapter propertyTypeAdapter;
    public void setPropertyTypeAdapter(PropertyTypeTableAdapter adapter) {
        this.propertyTypeAdapter = adapter;
    }

    public void setCityTableAdapter(CityTableAdapter adapter) {
        this.cityAdapter = adapter;
    }

    public void setProvinceTableAdapter(ProvinceTableAdapter adapter) {
        this.provinceAdapter = adapter;
    }

    @FXML
    private void initialize() {
        try {
            if (propertyTypeAdapter != null) {
                for (Object obj : propertyTypeAdapter.getAllRecords()) {
                    if (obj instanceof PropertyType) {
                        PropertyType pt = (PropertyType) obj;
                        propertyTypeCombo.getItems().add(pt.getTypeName());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        provinceCombo.setEditable(true);
        cityCombo.setEditable(true);

        try {
            if (provinceAdapter != null) {
                provinceCombo.getItems().addAll(provinceAdapter.getAllProvinces());
            }
            if (cityAdapter != null) {
                cityCombo.getItems().addAll(cityAdapter.getAllCities());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        provinceCombo.getEditor().setOnAction(e -> addProvinceIfMissing());
        provinceCombo.getEditor().focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) addProvinceIfMissing();
        });

        cityCombo.getEditor().setOnAction(e -> addCityIfMissing());
        cityCombo.getEditor().focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) addCityIfMissing();
        });
    }
    public void loadPropertyTypes() {
        try {
            if (propertyTypeAdapter != null) {
                propertyTypeCombo.getItems().clear();
                for (Object obj : propertyTypeAdapter.getAllRecords()) {
                    if (obj instanceof PropertyType) {
                        propertyTypeCombo.getItems().add(((PropertyType) obj).getTypeName());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void loadProvinceAndCityDropdowns() {
        try {
            if (provinceAdapter != null) {
                provinceCombo.getItems().setAll(provinceAdapter.getAllProvinces());
            }
            if (cityAdapter != null) {
                cityCombo.getItems().setAll(cityAdapter.getAllCities());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void addProvinceIfMissing() {
        String input = provinceCombo.getEditor().getText().trim();
        if (!input.isEmpty()) {
            try {
                if (provinceAdapter != null) {
                    provinceAdapter.insertIfNotExists(input);
                }


                if (!provinceCombo.getItems().contains(input)) {
                    provinceCombo.getItems().add(input);
                }

                provinceCombo.setValue(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }




    private void addCityIfMissing() {
        String input = cityCombo.getEditor().getText().trim();
        if (!input.isEmpty()) {
            try {
                if (cityAdapter != null) {
                    cityAdapter.insertIfNotExists(input);
                }


                if (!cityCombo.getItems().contains(input)) {
                    cityCombo.getItems().add(input);
                }

                cityCombo.setValue(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @FXML
    private void handleSave() {
        try {
            addProvinceIfMissing();
            addCityIfMissing();

            if (propertyTypeCombo.getValue() == null ||
                    streetField.getText().trim().isEmpty() ||
                    postalCodeField.getText().trim().isEmpty() ||
                    provinceCombo.getValue() == null ||
                    cityCombo.getValue() == null ||
                    lotSizeField.getText().trim().isEmpty() ||
                    squareFootageField.getText().trim().isEmpty() ||
                    bedroomsField.getText().trim().isEmpty() ||
                    bathroomsField.getText().trim().isEmpty() ||
                    yearBuiltField.getText().trim().isEmpty() ||
                    amenitiesField.getText().trim().isEmpty() ||
                    priceField.getText().trim().isEmpty() ||
                    descriptionField.getText().trim().isEmpty()) {
                showAlert("Missing Information", "Please make sure all fields are filled in.");
                return;
            }

            double lotSize = Double.parseDouble(lotSizeField.getText().trim());
            double squareFootage = Double.parseDouble(squareFootageField.getText().trim());
            int bedrooms = Integer.parseInt(bedroomsField.getText().trim());
            double bathrooms = Double.parseDouble(bathroomsField.getText().trim());
            int yearBuilt = Integer.parseInt(yearBuiltField.getText().trim());
            double price = Double.parseDouble(priceField.getText().trim());

            String id = UUID.randomUUID().toString();
            Property property = new Property(
                    id,
                    propertyTypeCombo.getValue(),
                    streetField.getText().trim(),
                    postalCodeField.getText().trim(),
                    provinceCombo.getValue(),
                    cityCombo.getValue(),
                    lotSize,
                    squareFootage,
                    bedrooms,
                    bathrooms,
                    yearBuilt,
                    amenitiesField.getText().trim(),
                    price,
                    descriptionField.getText().trim()
            );

            propertyAdapter.addNewRecord(property);
            manageController.loadProperties();

            Stage stage = (Stage) saveButton.getScene().getWindow();
            stage.close();

        } catch (NumberFormatException nfe) {
            showAlert("Invalid Input", "Please enter valid numeric values for Lot Size, Square Footage, Bedrooms, Bathrooms, Year Built, and Price.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "An unexpected error occurred. Please check all fields and try again.");
        }
    }

    @FXML
    private void handleCancel() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
