package org.libmanager.client.model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;

public abstract class Item {

    private SimpleStringProperty title;
    private SimpleStringProperty author;
    private SimpleStringProperty genre;
    private SimpleObjectProperty<LocalDate> releaseDate;
    private SimpleBooleanProperty issued;

    public Item() {
        title = new SimpleStringProperty(null);
        author = new SimpleStringProperty(null);
        genre = new SimpleStringProperty(null);
        releaseDate = new SimpleObjectProperty<>(null);
        issued = new SimpleBooleanProperty(false);
    }

    public Item(String title, String author, String genre, LocalDate releaseDate, boolean issued) {
        this.title = new SimpleStringProperty(null);
        this.author = new SimpleStringProperty(null);
        this.genre = new SimpleStringProperty(null);
        this.releaseDate = new SimpleObjectProperty<>(null);
        this.issued = new SimpleBooleanProperty(false);
    }

    public SimpleStringProperty titleProperty() {
        return title;
    }

    public String getTitle() {
        return title.get();
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public SimpleStringProperty authorProperty() {
        return author;
    }

    public String getAuthor() {
        return author.get();
    }

    public void setAuthor(String author) {
        this.author.set(author);
    }

    public SimpleStringProperty genreProperty() {
        return genre;
    }

    public String getGenre() {
        return genre.get();
    }

    public void setGenre(String genre) {
        this.genre.set(genre);
    }

    public SimpleObjectProperty<LocalDate> releaseDateProperty() {
        return releaseDate;
    }

    public LocalDate getReleaseDate() {
        return releaseDate.get();
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate.set(releaseDate);
    }

    public SimpleBooleanProperty issuedProperty() {
        return issued;
    }

    public boolean isIssued() {
        return issued.get();
    }

    public void setIssued(boolean issued) {
        this.issued.set(issued);
    }
}
