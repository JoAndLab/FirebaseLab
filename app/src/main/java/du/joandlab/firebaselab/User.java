package du.joandlab.firebaselab;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Anders Mellberg on 2016-10-26.
 */

@IgnoreExtraProperties
public class User {

    public String username;
    public String email;
    public String name;
    public String image;
    public String address;
    public String phone;
    public String registerdate;
    public String updatedate;
    public Boolean online;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String name, String email, String registerdate) {
        this.name = name;
        this.email = email;
        this.registerdate = registerdate;
    }

    public String getUsername(){
        return username;
    }

    public String getEmail(){
        return email;
    }

    public String getName(){
        return name;
    }

    public String getAddress(){
        return address;
    }

    public String getPhone(){
        return phone;
    }

    public String getRegisterdate(){
        return registerdate;
    }

    public String getUpdatedate(){
        return updatedate;
    }
}