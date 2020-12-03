package org.libmanager.client.enums;

public enum DVDGenre implements Genre {
    SCIENCEFICTION("Science-fiction"), FANTASY("Fantasy"), FANTASTIC("Fantastique"),
    DRAMA("Drame"), DOCUMENTARY("Documentaire"), ADVENTURE("Aventure"),
    SPYING("Espionnage"), WESTERN("Western"), CRIMINAL("Criminel"), SUPERHERO("Super-héros");

    String name;

    DVDGenre(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public static DVDGenre getEnum(String value) {
        for (DVDGenre d : values())
            if (d.toString().equalsIgnoreCase(value)) return d;
        throw new IllegalArgumentException();
    }
}
