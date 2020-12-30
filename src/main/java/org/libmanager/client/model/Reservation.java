package org.libmanager.client.model;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class Reservation {

    private SimpleLongProperty id;
    private SimpleStringProperty username;
    private SimpleStringProperty title;
    private SimpleStringProperty itemType;

    public Reservation() {
        this.id = new SimpleLongProperty(0);
        this.username = new SimpleStringProperty(null);
        this.title = new SimpleStringProperty(null);
        this.itemType = new SimpleStringProperty(null);
    }

    public Reservation(long id, String username, String title, String itemType) {
        this.id = new SimpleLongProperty(id);
        this.username = new SimpleStringProperty(username);
        this.title = new SimpleStringProperty(title);
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
}
