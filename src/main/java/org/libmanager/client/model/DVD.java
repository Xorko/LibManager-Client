package org.libmanager.client.model;

import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;

public class DVD extends Item {

    private SimpleStringProperty duration;

    public DVD() {
        duration = new SimpleStringProperty(null);
    }

    public DVD(String title, String author, String genre, LocalDate releaseDate, boolean state, String duration, String isbn) {
        super(title, author, genre, releaseDate, state, isbn);
        this.duration = new SimpleStringProperty(duration);
    }

    public SimpleStringProperty durationProperty() {
        return duration;
    }

    public String getDuration() {return duration.get();}

    public void setDuration(String duration) {
        this.duration.set(duration);
    }

}