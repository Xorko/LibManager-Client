package org.libmanager.client.enums;

public enum BookGenre implements Genre {
    POETRY("Poésie"), NOVEL("Roman"), SCIENCEFICTION("Science-fiction"), FANTASY("Fantasy"),
    FANTASTIC("Fantastique"),  BIOGRAPHY("Biographie"), TALE("Conte"), ESSAY("Essai"),
    COMICSTRIP("Bande dessinée"), DRAMA("Drame"), DOCUMENTARY("Documentaire"), ADVENTURE("Aventure"),
    SPYING("Espionnage"), WESTERN("Western"), CRIMINAL("Criminel"), SUPERHERO("Super-héros");

    final String name;

    BookGenre(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public static BookGenre getEnum(String value) {
        for (BookGenre b : values())
            if (b.toString().equalsIgnoreCase(value)) return b;
        throw new IllegalArgumentException();
    }
}
