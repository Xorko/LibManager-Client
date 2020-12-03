package org.libmanager.client.model;

import javafx.beans.property.SimpleStringProperty;
import org.libmanager.client.enums.Genre;

import java.time.LocalDate;

public class Book extends Item {

    private SimpleStringProperty publisher;
    private SimpleStringProperty isbn;

    public Book() {
        super();
        publisher = new SimpleStringProperty(null);
        isbn = new SimpleStringProperty(null);
    }

    public Book(String title, String author, Genre genre, LocalDate releaseDate, boolean status, String publisher, String isbn) {
        super(title, author, genre, releaseDate, status);
        this.publisher = new SimpleStringProperty(publisher);
        this.isbn = new SimpleStringProperty(isbn);
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

    public String getIsbn() {return isbn.get();}

    public SimpleStringProperty isbnProperty() {return isbn;}

    public void setIsbn(String isbn) {this.isbn.set(isbn);}
}
