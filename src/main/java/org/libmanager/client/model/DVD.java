package org.libmanager.client.model;

import javafx.beans.property.SimpleStringProperty;
import org.libmanager.client.enums.Genre;

import java.time.LocalDate;

public class DVD extends Item {

    private final SimpleStringProperty duration;

    public DVD() {
        duration = new SimpleStringProperty(null);
    }

    public DVD(int id, String title, String author, Genre genre, LocalDate releaseDate, int copies, int totalCopies, String duration) {
        super(id, title, author, genre, releaseDate, copies, totalCopies);
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