package org.libmanager.client.model;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;

public class Reservation {

    private final SimpleLongProperty id;
    private final SimpleStringProperty username;
    private final SimpleStringProperty title;
    private final SimpleObjectProperty<LocalDate> reservationDate;
    private final SimpleStringProperty itemType;

    public Reservation() {
        this.id = new SimpleLongProperty(0);
        this.username = new SimpleStringProperty(null);
        this.title = new SimpleStringProperty(null);
        this.reservationDate = new SimpleObjectProperty<>(null);
        this.itemType = new SimpleStringProperty(null);
    }

    public Reservation(long id, String username, String title, String itemType, LocalDate reservationDate) {
        this.id = new SimpleLongProperty(id);
        this.username = new SimpleStringProperty(username);
        this.title = new SimpleStringProperty(title);
        this.reservationDate = new SimpleObjectProperty<>(reservationDate);
        this.itemType = new SimpleStringProperty(itemType);
    }

    public SimpleLongProperty idProperty() {
        return id;
    }

    public long getId() {
        return id.get();
    }

    public void setId(long id) {
        this.id.set(id);
    }

    public SimpleStringProperty usernameProperty() {
        return username;
    }

    public String getUsername() {
        return username.get();
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public SimpleStringProperty titleProperty() {
        return title;
    }

    public String getTitle() {
        return title.get();
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public SimpleStringProperty itemTypeProperty() {
        return itemType;
    }

    public String getItemType() {
        return itemType.get();
    }

    public void setItemType(String itemType) {
        this.itemType.set(itemType);
    }

    public SimpleObjectProperty<LocalDate> reservationDateProperty() {
        return reservationDate;
    }

    public LocalDate getReservationDate() {
        return reservationDate.get();
    }

    public void setReservationDate(LocalDate reservationDate) {
        this.reservationDate.set(reservationDate);
    }
}
