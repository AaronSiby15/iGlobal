package se2203b.iGlobal;

import javafx.beans.property.*;
import java.util.Date;
/**
 * @author Abdelkader Ouda
 */
public class Administrator extends IGlobalUser {
    private ObjectProperty<Date> dateCreated;



    public Administrator(){
        setID(null);
        setFirstName(null);
        setLastName(null);
        setEmail(null);
        setPhone(null);
        setUserAccount(new UserAccount());
        this.dateCreated = new SimpleObjectProperty<>();
    }

    public Administrator(int id,
            String firstName,
                         String lastName,
                         String email,
                         String phone,
                         Date dateCreated,
                         UserAccount account) {
        setID(String.valueOf(id));
        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
        setPhone(phone);
        setUserAccount(account);
        this.dateCreated = new SimpleObjectProperty<>(dateCreated);
    }


    public void setDateCreated(Date _date) {
        this.dateCreated.set(_date);
    }
    public ObjectProperty<Date> dateCreatedProperty() {
        return this.dateCreated;
    }
    public Date getDateCreated() {
        return this.dateCreated.get();
    }

}
