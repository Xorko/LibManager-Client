package org.libmanager.client.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import org.libmanager.client.enums.Genre;

import java.time.LocalDate;

public abstract class Item {

    private SimpleIntegerProperty id;
    private SimpleStringProperty title;
    private SimpleStringProperty author;
    private SimpleObjectProperty<Genre> genre;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private SimpleObjectProperty<LocalDate> releaseDate;
    private SimpleBooleanProperty status;

    public Item() {
        id = new SimpleIntegerProperty(0);
        title = new SimpleStringProperty(null);
        author = new SimpleStringProperty(null);
        genre = new SimpleObjectProperty<>(null);
        releaseDate = new SimpleObjectProperty<>(null);
        status = new SimpleBooleanProperty(false);
    }

    public Item(int id, String title, String author, Genre genre, LocalDate releaseDate, boolean status) {
        this.id = new SimpleIntegerProperty(id);
        this.title = new SimpleStringProperty(title);
        this.author = new SimpleStringProperty(author);
        this.genre = new SimpleObjectProperty<>(genre);
        this.releaseDate = new SimpleObjectProperty<>(releaseDate);
        this.status = new SimpleBooleanProperty(status);
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
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

    public SimpleObjectProperty<Genre> genreProperty() {
        return genre;
    }

    public Genre getGenre() {
        return genre.get();
    }

    public void setGenre(Genre genre) {
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

    public SimpleBooleanProperty statusProperty() {
        return status;
    }

    public boolean getStatus() {
        return status.get();
    }

    public void setStatus(boolean status) {this.status.set(status);}
}
