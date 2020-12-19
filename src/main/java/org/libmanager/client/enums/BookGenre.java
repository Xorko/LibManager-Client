package org.libmanager.client.enums;

public enum BookGenre implements Genre {
    ANY,
    POETRY,
    NOVEL,
    SCIENCEFICTION,
    FANTASY,
    FANTASTIC,
    BIOGRAPHY,
    TALE,
    ESSAY,
    COMICSTRIP,
    DRAMA,
    CRIMINAL,
    THEATER;

    @Override
    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }

    public static BookGenre getEnum(String value) {
        for (BookGenre b : values())
            if (b.toString().equalsIgnoreCase(value)) return b;
        throw new IllegalArgumentException();
    }
}
