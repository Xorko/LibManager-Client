package org.libmanager.client.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;

public class User {

    private final SimpleStringProperty username;
    private final SimpleStringProperty firstName;
    private final SimpleStringProperty lastName;
    private final SimpleStringProperty address;
    private final SimpleStringProperty email;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private final SimpleObjectProperty<LocalDate> birthday;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private final SimpleObjectProperty<LocalDate> registrationDate;
    private final SimpleStringProperty token;
    private final SimpleBooleanProperty admin;

    public User(String username, String firstName, String lastName, String address, String email, LocalDate birthday, LocalDate registrationDate, String token, boolean admin) {
        this.username = new SimpleStringProperty(username);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.address = new SimpleStringProperty(address);
        this.birthday = new SimpleObjectProperty<>(birthday);
        this.registrationDate = new SimpleObjectProperty<>(registrationDate);
        this.email = new SimpleStringProperty(email);
        this.token = new SimpleStringProperty(token);
        this.admin = new SimpleBooleanProperty(admin);
    }

    public User(String username, String token, boolean admin, LocalDate birthday, LocalDate registrationDate) {
        this.username = new SimpleStringProperty(username);
        this.firstName = new SimpleStringProperty(null);
        this.lastName = new SimpleStringProperty(null);
        this.address = new SimpleStringProperty(null);
        this.birthday = new SimpleObjectProperty<>(birthday);
        this.registrationDate = new SimpleObjectProperty<>(registrationDate);
        this.email = new SimpleStringProperty(null);
        this.token = new SimpleStringProperty(token);
        this.admin = new SimpleBooleanProperty(admin);
    }

    public User() {
        this.username = new SimpleStringProperty(null);
        this.firstName = new SimpleStringProperty(null);
        this.lastName = new SimpleStringProperty(null);
        this.address = new SimpleStringProperty(null);
        this.birthday = new SimpleObjectProperty<>(null);
        this.registrationDate = new SimpleObjectProperty<>(null);
        this.email = new SimpleStringProperty(null);
        this.token = new SimpleStringProperty(null);
        this.admin = new SimpleBooleanProperty(false);
    }

    public SimpleStringProperty usernameProperty() {
        return username;
    }

    public String getUsername() {
        return username.get();
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public SimpleStringProperty firstNameProperty() {
        return firstName;
    }

    public String getFirstName() {
        return firstName.get();
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public SimpleStringProperty lastNameProperty() {
        return lastName;
    }

    public String getLastName() {
        return lastName.get();
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public SimpleStringProperty addressProperty() {
        return address;
    }

    public String getAddress() {
        return address.get();
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public SimpleStringProperty emailProperty() {
        return email;
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public SimpleObjectProperty<LocalDate> birthdayProperty() {
        return birthday;
    }

    public SimpleObjectProperty<LocalDate> registrationDateProperty() {
        return registrationDate;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate.get();
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate.set(registrationDate);
    }

    public LocalDate getBirthday() {
        return birthday.get();
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday.set(birthday);
    }

    public SimpleStringProperty tokenProperty() {
        return token;
    }

    public String getToken() {
        return token.get();
    }

    public void setToken(String token) {
        this.token.set(token);
    }

    public SimpleBooleanProperty adminProperty() {
        return admin;
    }

    public boolean isAdmin() {
        return admin.get();
    }

    public void setAdmin(boolean admin) {
        this.admin.set(admin);
    }
}
