package se2203b.iGlobal;

import javafx.beans.property.*;
/**
 * @author Abdelkader Ouda
 */
public class UserAccount {
    private StringProperty userAccountName;
    private StringProperty encryptedPassword;
    private StringProperty passwordSalt;
    private StringProperty accountType;  // "admin", "executive, or "egent"

    public UserAccount() {
        this.userAccountName = new SimpleStringProperty();
        this.encryptedPassword = new SimpleStringProperty();
        this.passwordSalt = new SimpleStringProperty();
        this.accountType = new SimpleStringProperty();
    }

    public UserAccount(String userAccountName, String encryptedPassword, String passwordSalt, String accountType) {
        this.userAccountName = new SimpleStringProperty(userAccountName);
        this.encryptedPassword = new SimpleStringProperty(encryptedPassword);
        this.passwordSalt = new SimpleStringProperty(passwordSalt);
        this.accountType = new SimpleStringProperty(accountType);
    }

    //set and get methods
    // userName property
    public void setUserAccountName(String userAccountName) {
        this.userAccountName.set(userAccountName);
    }
    public StringProperty userAccountNameProperty() {
        return this.userAccountName;
    }
    public String getUserAccountName() {
        return this.userAccountName.get();
    }

    // encryptedPassword property
    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword.set(encryptedPassword);
    }
    public StringProperty encryptedPasswordProperty() {
        return this.encryptedPassword;
    }
    public String getEncryptedPassword() {
        return this.encryptedPassword.get();
    }

    // passwordSalt property
    public void setPasswordSalt(String passwordSalt) {
        this.passwordSalt.set(passwordSalt);
    }
    public StringProperty passwordSaltProperty() {
        return this.passwordSalt;
    }
    public String getPasswordSalt() {
        return this.passwordSalt.get();
    }

    // account type property
    public void setAccountType(String accountType) {
        this.accountType.set(accountType);
    }
    public StringProperty accountTypeProperty() {
        return this.accountType;
    }
    public String getAccountType() {
        return this.accountType.get();
    }

}
