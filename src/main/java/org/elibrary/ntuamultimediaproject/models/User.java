package org.elibrary.ntuamultimediaproject.models;

import java.io.Serial;
import java.io.Serializable;

public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String username;    // That will be the ID of the user
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String adt;
    private Boolean isAdmin;

    public User(String username, String password, String email, String firstName, String lastName, String adt) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.adt = adt;
        this.isAdmin = false;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.email = "";
        this.firstName = "";
        this.lastName = "";
        this.adt = "";
        this.isAdmin = true;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAdt() {
        return adt;
    }

    public void setAdt(String adt) {
        this.adt = adt;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", adt='" + adt + '\'' +
                ", isAdmin=" + isAdmin +
                '}';
    }
}