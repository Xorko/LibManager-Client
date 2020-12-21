package org.libmanager.client.model;

import javafx.beans.property.SimpleStringProperty;
import org.libmanager.client.enums.Genre;

import java.time.LocalDate;

public class DVD extends Item {

    private SimpleStringProperty duration;

    public DVD() {
        duration = new SimpleStringProperty(null);
    }

    public DVD(int id, String title, String author, Genre genre, LocalDate releaseDate, boolean status, String duration) {
        super(id, title, author, genre, releaseDate, status);
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