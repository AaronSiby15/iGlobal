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
import se2203b.iGlobal.Agent;
import se2203b.iGlobal.AgentTableAdapter;
import javafx.scene.control.cell.TextFieldTableCell;



import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ManageAgentProfilesController {

    @FXML
    private TableView<Agent> agentsTable;
    @FXML
    private TableColumn<Agent, String> colFirstName;
    @FXML
    private TableColumn<Agent, String> colLastName;
    @FXML
    private TableColumn<Agent, String> colEmail;
    @FXML
    private TableColumn<Agent, String> colPhone;
    @FXML
    private TableColumn<Agent, String> colLicence;
    @FXML
    private TableColumn<Agent, String> colSpecialization;
    @FXML
    private Button btnAddAgent, btnRemoveAgent, btnExit;

    private ObservableList<Agent> agentList = FXCollections.observableArrayList();
    private AgentTableAdapter agentTableAdapter;


    @FXML
    public void initialize() {
        try {
            agentTableAdapter = new AgentTableAdapter(false);
            loadAgents();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        agentsTable.setEditable(true);

        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colLicence.setCellValueFactory(new PropertyValueFactory<>("licenceNumber"));
        colSpecialization.setCellValueFactory(new PropertyValueFactory<>("specialization"));

        colFirstName.setCellFactory(TextFieldTableCell.forTableColumn());
        colFirstName.setOnEditCommit(event -> {
            Agent agent = event.getRowValue();
            agent.firstNameProperty().set(event.getNewValue());
            saveEditedAgent(agent);
        });

        colLastName.setCellFactory(TextFieldTableCell.forTableColumn());
        colLastName.setOnEditCommit(event -> {
            Agent agent = event.getRowValue();
            agent.lastNameProperty().set(event.getNewValue());
            saveEditedAgent(agent);
        });
        colEmail.setCellFactory(TextFieldTableCell.forTableColumn());
        colEmail.setOnEditCommit(event -> {
            Agent agent = event.getRowValue();
            agent.emailProperty().set(event.getNewValue());
            saveEditedAgent(agent);
        });
        colPhone.setCellFactory(TextFieldTableCell.forTableColumn());
        colPhone.setOnEditCommit(event -> {
            Agent agent = event.getRowValue();
            agent.phoneProperty().set(event.getNewValue());
            saveEditedAgent(agent);
        });
        colLicence.setCellFactory(TextFieldTableCell.forTableColumn());
        colLicence.setOnEditCommit(event -> {
            Agent agent = event.getRowValue();
            agent.licenceNumberProperty().set(event.getNewValue());
            saveEditedAgent(agent);
        });
        colSpecialization.setCellFactory(TextFieldTableCell.forTableColumn());
        colSpecialization.setOnEditCommit(event -> {
            Agent agent = event.getRowValue();
            agent.specializationProperty().set(event.getNewValue());
            saveEditedAgent(agent);
        });


    }

    public void loadAgents() {
        try {
            List<Object> agents = agentTableAdapter.getAllRecords();


            agentList.clear();
            for (Object obj : agents) {
                if (obj instanceof Agent) {
                    agentList.add((Agent) obj);
                }
            }


            agentsTable.setItems(agentList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }





    @FXML
    public void addAgent() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("addNewAgent-view.fxml"));

            Stage stage = new Stage();
            stage.getIcons().add(new Image("file:src/main/resources/se2203b/iGlobal/WesternLogo.png"));

            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Add New Agent");
            stage.initModality(Modality.APPLICATION_MODAL);



            AddNewAgentController controller = loader.getController();
            controller.setAgentTableAdapter(agentTableAdapter);
            controller.setManageAgentProfilesController(this);

            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @FXML
    public void removeAgent() {
        Agent selectedAgent = agentsTable.getSelectionModel().getSelectedItem();
        if (selectedAgent != null) {
            try {
                AgentTableAdapter agentTableAdapter = new AgentTableAdapter(false);
                agentTableAdapter.removeAgent(selectedAgent.getAgentID());
                agentList.remove(selectedAgent);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    private void saveEditedAgent(Agent agent) {
        try {
            agentTableAdapter.updateRecord(agent);
            System.out.println("Agent updated successfully.");
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
