package org.libmanager.client.model;

import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;

public class DVD extends Item {

    private SimpleStringProperty duration;

    public DVD() {
        duration = new SimpleStringProperty(null);
    }

    public DVD(String title, String author, String genre, LocalDate releaseDate, boolean issued, String duration) {
        super(title, author, genre, releaseDate, issued);
        this.duration = new SimpleStringProperty(duration);
    }

    public SimpleStringProperty getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration.set(duration);
    }

}