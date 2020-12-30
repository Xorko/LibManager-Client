package org.libmanager.client.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import javafx.beans.property.*;
import org.libmanager.client.enums.Genre;

import java.time.LocalDate;

public abstract class Item {

    private final SimpleLongProperty id;
    private final SimpleStringProperty title;
    private final SimpleStringProperty author;
    private final SimpleObjectProperty<Genre> genre;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private final SimpleObjectProperty<LocalDate> releaseDate;
    private final SimpleBooleanProperty status;
    private final SimpleIntegerProperty availableCopies;
    private final SimpleIntegerProperty totalCopies;

    public Item() {
        id = new SimpleLongProperty(0L);
        title = new SimpleStringProperty(null);
        author = new SimpleStringProperty(null);
        genre = new SimpleObjectProperty<>(null);
        releaseDate = new SimpleObjectProperty<>(null);
        status = new SimpleBooleanProperty(false);
        availableCopies = new SimpleIntegerProperty(0);
        totalCopies = new SimpleIntegerProperty(0);

        availableCopies.addListener(((observable, oldValue, newValue) -> this.setStatus(newValue.intValue() > 0)));
    }

    public Item(long id, String title, String author, Genre genre, LocalDate releaseDate, int availableCopies, int totalCopies) {
        this.id = new SimpleLongProperty(id);
        this.title = new SimpleStringProperty(title);
        this.author = new SimpleStringProperty(author);
        this.genre = new SimpleObjectProperty<>(genre);
        this.releaseDate = new SimpleObjectProperty<>(releaseDate);
        this.status = new SimpleBooleanProperty(availableCopies > 0);
        this.availableCopies = new SimpleIntegerProperty(availableCopies);
        this.totalCopies = new SimpleIntegerProperty(totalCopies);

        this.availableCopies.addListener(((observable, oldValue, newValue) -> this.setStatus(newValue.intValue() > 0)));
    }

    public void incrementCopies() {
        if (getAvailableCopies() < getTotalCopies())
            availableCopies.set(availableCopies.get() + 1);
    }

    public void decrementCopies() {
        if (availableCopies.get() - 1 >= 0) {
            availableCopies.set(availableCopies.get() - 1);
        }
    }

    public SimpleLongProperty idProperty() {
        return id;
    }

    public long getId() {
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

    public void setStatus(boolean status) {
        this.status.set(status);
    }

    public SimpleIntegerProperty availableCopiesProperty() {
        return availableCopies;
    }

    public int getAvailableCopies() {
        return availableCopies.get();
    }

    public void setAvailableCopies(int copies) {
        if (copies > 0 && copies <= totalCopies.get())
            this.availableCopies.set(copies);
    }

    public SimpleIntegerProperty totalCopiesProperty() {
        return totalCopies;
    }

    public int getTotalCopies() {
        return totalCopies.get();
    }

    public void setTotalCopies(int totalCopies) {
        if (totalCopies > 0)
            this.totalCopies.set(totalCopies);
    }
}
