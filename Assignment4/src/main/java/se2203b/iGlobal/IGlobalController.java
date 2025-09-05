package se2203b.iGlobal;

import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ResourceBundle;

import se2203b.iGlobal.ManagePropertyRecordsController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * @author Abdelkader Ouda
 */
public class IGlobalController implements Initializable {
    @FXML
    private Menu aboutMenu;
    @FXML
    private MenuItem aboutusMenuItem;
    @FXML
    private MenuItem changePasswordMenuItem;
    @FXML
    private MenuItem closeMenuItem;
    @FXML
    private Menu fileMenu;
    @FXML
    private Menu agentPortalMenu;
    @FXML
    private MenuItem loginMenuItem;
    @FXML
    private MenuItem logoutMenuItem;
    @FXML
    private MenuBar mainMenu;
    @FXML
    private Menu executivePortalMenu;
    @FXML
    private Menu adminPortalMenu;
    @FXML
    private Menu userMenuItem;

    private String userAccountName;
    @FXML
    private MenuItem statusCodes;
    @FXML
    private MenuItem propertyTypes;
    @FXML
    private MenuItem documentTypes;
    @FXML
    private Menu configSystemCodes;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ImageView face = new ImageView(new Image("file:src/main/resources/se2203b/iGlobal/UserIcon.png",20,20,true,true));
        userMenuItem.setGraphic(face);
        disableMenuItems();

        try {

            // Create the administrator data if it is not already in the database
            new AdministratorTableAdapter(false);

        } catch (SQLException ex) {
            displayAlert(ex.getMessage());
        }
    }
    @FXML
    public void showAbout() throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("about-view.fxml"));
        Parent About = (Parent) fxmlLoader.load();
        Scene scene = new Scene(About);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.getIcons().add(new Image("file:src/main/resources/se2203b/iGlobal/WesternLogo.png"));
        stage.setTitle("About Us");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }
    @FXML
    public void exit() {
        Stage stage = (Stage) mainMenu.getScene().getWindow();
        stage.close();
    }
    @FXML
    public void login() throws Exception {
        // load the fxml file (the UI elements)
        FXMLLoader fxmlLoader = new FXMLLoader(IGlobalController.class.getResource("login-view.fxml"));

        // create the root node
        Parent Login = fxmlLoader.load();
        LoginController loginController = (LoginController) fxmlLoader.getController();
        loginController.setIGlobalController(this);
        loginController.setDataStore(new UserAccountTableAdapter(false));

        // create new stage
        Stage stage = new Stage();
        stage.setScene(new Scene(Login));
        // add icon to the About window
        stage.getIcons().add(new Image("file:src/main/resources/se2203b/iGlobal/WesternLogo.png"));
        stage.setTitle("Login to iGlobal");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }
    @FXML
    public void changePassword() throws Exception {
        // load the fxml file (the UI elements)
        FXMLLoader fxmlLoader = new FXMLLoader(IGlobalController.class.getResource("ChangePassword-view.fxml"));

        // create the root node
        Parent changePassword = fxmlLoader.load();
        ChangePasswordController changePasswordController = (ChangePasswordController) fxmlLoader.getController();
        changePasswordController.setIGlobalController(this);
        changePasswordController.setDataStore(new UserAccountTableAdapter(false));

        // create new stage
        Stage stage = new Stage();
        stage.setScene(new Scene(changePassword));
        // add icon to the About window
        stage.getIcons().add(new Image("file:src/main/resources/se2203b/iGlobal/WesternLogo.png"));
        stage.setTitle("Change Password");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }
    @FXML
    public void logout() {
        disableMenuItems();
    }
    @FXML
    public void managePropertyTypes() throws Exception {
        // load the fxml file (the UI elements)
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("managePropertyTypes-view.fxml"));
        // create the root node
        Parent propertyType = fxmlLoader.load();
        ManagePropertyTypesController managePropertyTypesController = (ManagePropertyTypesController) fxmlLoader.getController();
        managePropertyTypesController.setIGlobalController(this);
        managePropertyTypesController.setDataStore(
                new PropertyTypeTableAdapter(false));
        // create new stage
        Stage stage = new Stage();
        stage.setScene(new Scene(propertyType));
        // add icon to the About window
        stage.getIcons().add(new Image("file:src/main/resources/se2203b/iglobal/WesternLogo.png"));
        stage.setTitle("Manage Property Types");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }
    @FXML
    public void openManageAgents(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/se2203b/iGlobal/manageAgentProfiles-view.fxml"));
            Stage stage = new Stage();
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/se2203b/iGlobal/WesternLogo.png")));

            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Manage Agent Profiles");
            stage.initModality(Modality.APPLICATION_MODAL); // Opens as a modal window
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void openManageExecutives() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/se2203b/iGlobal/manageExecutiveProfiles-view.fxml"));
            Stage stage = new Stage();
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/se2203b/iGlobal/WesternLogo.png")));

            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Manage Executive Profiles");
            stage.initModality(Modality.APPLICATION_MODAL); // optional
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void openManageAdministrators(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/se2203b/iGlobal/manageAdminProfiles-view.fxml"));
            Stage stage = new Stage();
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/se2203b/iGlobal/WesternLogo.png")));

            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Manage Administrator Profiles");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void openManageUserAccounts() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("manageUserAccount-view.fxml"));
            Stage stage = new Stage();
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/se2203b/iGlobal/WesternLogo.png")));

            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Manage User Accounts");
            stage.initModality(Modality.APPLICATION_MODAL); // optional, opens as a modal dialog
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void openManagePropertyRecords() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("managePropertyRecords-view.fxml"));

            Stage stage = new Stage();
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/se2203b/iGlobal/WesternLogo.png")));

            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Manage Property Records");
            stage.initModality(Modality.APPLICATION_MODAL); // optional, opens as a modal dialog
            stage.show();

        } catch (Exception e) {
            e.printStackTrace(); // Important: this should print if loading fails!
        }
    }








    public void enableExecutiveControls() {
        // Off
        agentPortalMenu.setDisable(true);
        adminPortalMenu.setDisable(true);
        loginMenuItem.setDisable(true);

        // On
        executivePortalMenu.setDisable(false);
        fileMenu.setDisable(false);
        logoutMenuItem.setDisable(false);
        mainMenu.setDisable(false);
        closeMenuItem.setDisable(false);
        userMenuItem.setVisible(true);
    }

    public void enableAgentControls() {
        // Off
        adminPortalMenu.setDisable(true);
        executivePortalMenu.setDisable(true);
        loginMenuItem.setDisable(true);

        // On
        agentPortalMenu.setDisable(false);
        fileMenu.setDisable(false);
        logoutMenuItem.setDisable(false);
        mainMenu.setDisable(false);
        closeMenuItem.setDisable(false);
        userMenuItem.setVisible(true);
    }



    ////////////
    public void enableAdminControls() {
        // Off
        agentPortalMenu.setDisable(true);
        executivePortalMenu.setDisable(true);
        loginMenuItem.setDisable(true);

        // On
        fileMenu.setDisable(false);
        logoutMenuItem.setDisable(false);
        mainMenu.setDisable(false);
        closeMenuItem.setDisable(false);
        adminPortalMenu.setDisable(false);
        userMenuItem.setVisible(true);
    }
    public void disableMenuItems() {
        // Off
        agentPortalMenu.setDisable(true);
        executivePortalMenu.setDisable(true);
        adminPortalMenu.setDisable(true);
        logoutMenuItem.setDisable(true);
        userMenuItem.setVisible(false);

        // On
        fileMenu.setDisable(false);
        mainMenu.setDisable(false);
        closeMenuItem.setDisable(false);
        loginMenuItem.setDisable(false);
    }
    // set the logged-in username into the menu item
    public void setUserFullname(String name) {
        userMenuItem.setText(name);
    }
    // save user account name
    public void setUserName(String userAccountName) {
        this.userAccountName = userAccountName;
    }
    public String getUserFullname() {
        return userMenuItem.getText();
    }
    public String getUserAccountName() {
        return userAccountName;
    }
    public void displayAlert(String msg) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(IGlobalApplication.class.getResource("alert-View.fxml"));
            // create the root node
            Parent alertWindow = fxmlLoader.load();
            AlertController alertController = (AlertController) fxmlLoader.getController();

            Stage stage = new Stage();
            stage.setScene(new Scene(alertWindow));
            stage.getIcons().add(new Image("file:src/main/resources/se2203b/iGlobal/WesternLogo.png"));
            alertController.setAlertText(msg);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException ex1) {
            System.out.println("Error in Display Alert " + ex1);
        }
    }
    // Just hash the password with a random number (salt) to encrypt it.
    public String encrypt(String password, String salt) {
        MessageDigest crypto = null;
        try {
            String saltedPassword = password + salt;
            crypto = MessageDigest.getInstance("SHA-256");
            byte[] passBytes = saltedPassword.getBytes();
            byte[] passHash = crypto.digest(passBytes);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < passHash.length; i++) {
                sb.append(Integer.toString((passHash[i] & 0xff) + 0x100, 16).substring(1));
            }
            String generatedPassword = sb.toString();
            return generatedPassword;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
