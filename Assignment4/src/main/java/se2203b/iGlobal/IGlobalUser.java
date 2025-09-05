package se2203b.iGlobal;

import javafx.beans.property.*;
/**
 * @author Abdelkader Ouda
 */
public class IGlobalUser {
    private StringProperty id;
    private StringProperty firstName;
    private StringProperty lastName;
    private StringProperty email;
    private StringProperty phone;
    private ObjectProperty<UserAccount> userAccount
            = new SimpleObjectProperty(new UserAccount());

    //set and get methods
    // id property
    public void setID(String id) {
        this.id = new SimpleStringProperty(id);
    }
    public StringProperty idProperty() {
        return this.id;
    }
    public String getID() {
        return this.id.get();
    }

    // first name property
    public void setFirstName(String firstName) {
        this.firstName = new SimpleStringProperty(firstName);
    }
    public StringProperty firstNameProperty() {
        return this.firstName;
    }
    public String getFirstName() {
        return this.firstName.get();
    }

    // last name property
    public void setLastName(String lastName) {
        this.lastName = new SimpleStringProperty(lastName);
    }
    public StringProperty lastNameProperty() {
        return this.lastName;
    }
    public String getLastName() {
        return this.lastName.get();
    }

    // email property
    public void setEmail(String email) {
        this.email = new SimpleStringProperty(email);
    }
    public StringProperty emailProperty() {
        return this.email;
    }
    public String getEmail() {
        return this.email.get();
    }

    // phone property
    public void setPhone(String phone) {
        this.phone = new SimpleStringProperty(phone);
    }
    public StringProperty phoneProperty() {
        return this.phone;
    }
    public String getPhone() {
        return this.phone.get();
    }

    // userAccount Property
    public void setUserAccount(UserAccount userAccount) {
        this.userAccount.set(userAccount);
    }
    public ObjectProperty<UserAccount> userAccountProperty() {
        return this.userAccount;
    }
    public UserAccount getUserAccount() {
        return this.userAccount.get();
    }

}
