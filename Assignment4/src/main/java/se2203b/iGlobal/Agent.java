package se2203b.iGlobal;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Agent {
    private final StringProperty agentID;
    private final StringProperty firstName;
    private final StringProperty lastName;
    private final StringProperty email;
    private final StringProperty phone;
    private final StringProperty licenceNumber;
    private final StringProperty specialization;

    private UserAccount userAccount;

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }


    public Agent(String agentID, String firstName, String lastName, String email, String phone, String licenceNumber, String specialization) {
        this.agentID = new SimpleStringProperty(agentID);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.email = new SimpleStringProperty(email);
        this.phone = new SimpleStringProperty(phone);
        this.licenceNumber = new SimpleStringProperty(licenceNumber);
        this.specialization = new SimpleStringProperty(specialization);
    }


    // Getters
    public String getAgentID() { return agentID.get(); }
    public String getFirstName() { return firstName.get(); }
    public String getLastName() { return lastName.get(); }
    public String getEmail() { return email.get(); }
    public String getPhone() { return phone.get(); }
    public String getLicenceNumber() { return licenceNumber.get(); }
    public String getSpecialization() { return specialization.get(); }

    // Properties for JavaFX binding
    public StringProperty agentIDProperty() { return agentID; }
    public StringProperty firstNameProperty() { return firstName; }
    public StringProperty lastNameProperty() { return lastName; }
    public StringProperty emailProperty() { return email; }
    public StringProperty phoneProperty() { return phone; }
    public StringProperty licenceNumberProperty() { return licenceNumber; }
    public StringProperty specializationProperty() { return specialization; }
}
