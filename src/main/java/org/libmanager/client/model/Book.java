package org.libmanager.client.model;

import javafx.beans.property.SimpleStringProperty;
import org.libmanager.client.enums.BookGenre;

import java.time.LocalDate;

public class Book extends Item {

    private final SimpleStringProperty publisher;
    private final SimpleStringProperty isbn;

    public Book() {
        super();
        publisher = new SimpleStringProperty(null);
        isbn = new SimpleStringProperty(null);
    }

    public Book(int id, String title, String author, String genre, LocalDate releaseDate, int availableCopies, int totalCopies, String publisher, String isbn) {
        super(id, title, author, BookGenre.getEnum(genre), releaseDate, availableCopies, totalCopies);
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
