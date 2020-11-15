package org.libmanager.client.model;

import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;

public class Book extends Item {

    private SimpleStringProperty publisher;

    public Book() {
        publisher = new SimpleStringProperty(null);
    }

    public Book(String title, String author, String genre, LocalDate releaseDate, boolean issued, String publisher) {
        super(title, author, genre, releaseDate, issued);
        this.publisher = new SimpleStringProperty(publisher);
    }

    public SimpleStringProperty publisherProperty() {
        return publisher;
    }

    public String getPublisher() {
        return publisher.get();
    }

    public void setPublisher(String publisher) {
        this.publisher.set(publisher);
    }
}