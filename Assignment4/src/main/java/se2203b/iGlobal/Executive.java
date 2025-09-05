package se2203b.iGlobal;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Executive {
    private final StringProperty firstName;
    private final StringProperty lastName;
    private final StringProperty email;
    private final StringProperty phone;
    private final StringProperty privatePhone;

    private UserAccount userAccount;

    public Executive() {
        this.firstName = new SimpleStringProperty();
        this.lastName = new SimpleStringProperty();
        this.email = new SimpleStringProperty();
        this.phone = new SimpleStringProperty();
        this.privatePhone = new SimpleStringProperty();
    }

    public Executive(String firstName, String lastName, String email, String phone, String privatePhone) {
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.email = new SimpleStringProperty(email);
        this.phone = new SimpleStringProperty(phone);
        this.privatePhone = new SimpleStringProperty(privatePhone);
    }

    // UserAccount reference
    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    // Getters
    public String getFirstName() { return firstName.get(); }
    public String getLastName() { return lastName.get(); }
    public String getEmail() { return email.get(); }
    public String getPhone() { return phone.get(); }
    public String getPrivatePhone() { return privatePhone.get(); }

    // JavaFX Properties (used in TableViews or bindings)
    public StringProperty firstNameProperty() { return firstName; }
    public StringProperty lastNameProperty() { return lastName; }
    public StringProperty emailProperty() { return email; }
    public StringProperty phoneProperty() { return phone; }
    public StringProperty privatePhoneProperty() { return privatePhone; }
}
