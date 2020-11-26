package org.libmanager.client.enums;

public enum DVDGenre {
    SCIENCEFICTION("Science-fiction"), FANTASY("Fantasy"), FANTASTIC("Fantastique"),
    COMICSTRIP("Bande dessinée"), DRAMA("Drame"), DOCUMENTARY("Documentaire"), ADVENTURE("Aventure"),
    SPYING("Espionnage"), WESTERN("Western"), CRIMINAL("Criminel"), SUPERHERO("Super-héros");

    String name;

    DVDGenre(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
