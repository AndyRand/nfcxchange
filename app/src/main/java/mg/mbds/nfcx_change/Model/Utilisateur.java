package mg.mbds.nfcx_change.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author christophelai
 */

public class Utilisateur extends ClassMapTable implements Serializable {

    /** Property id */
    @JsonProperty("_id")
    String id;

    /** Property name */
    @JsonProperty("name")
    String name;

    /** Property firstname */
    @JsonProperty("firstName")
    String firstname;

    /** Property birthdaydate */
    @JsonProperty("birthdayDate")
    Date birthdaydate;

    /** Property mail */
    @JsonProperty("mail")
    String mail;

    /** Property password */
    @JsonProperty("password")
    String password;

    @JsonProperty("user")
    private String userToken;

    @JsonProperty("avatar")
    private String avatar;

    @JsonProperty("token")
    private double token;
    /**
     * Constructor
     */
    public Utilisateur() {
    }

    /**
     * Gets the id
     */
    public String getId() {
        return this.id;
    }

    /**
     * Sets the id
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the name
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the firstname
     */
    public String getFirstname() {
        return this.firstname;
    }

    /**
     * Sets the firstname
     */
    public void setFirstname(String value) {
        this.firstname = value;
    }

    /**
     * Gets the birthdaydate
     */
    public Date getBirthdaydate() {
        return this.birthdaydate;
    }

    /**
     * Sets the birthdaydate
     */
    public void setBirthdaydate(Date value) {
        this.birthdaydate = value;
    }

    /**
     * Gets the mail
     */
    public String getMail() {
        return this.mail;
    }

    /**
     * Sets the mail
     */
    public void setMail(String value) {
        this.mail = value;
    }

    /**
     * Gets the password
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Sets the password
     */
    public void setPassword(String value) {
        this.password = value;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setUserImageUrl(String avatar) {
        this.setAvatar(avatar);
    }

    public double getToken() {
        return token;
    }

    public void setToken(double token) {
        this.token = token;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}

