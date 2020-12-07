package org.libmanager.client.enums;

public enum DVDGenre implements Genre {
    ANY,
    SCIENCEFICTION,
    FANTASY,
    FANTASTIC,
    DRAMA,
    DOCUMENTARY,
    ADVENTURE,
    SPYING,
    WESTERN,
    CRIMINAL,
    SUPERHERO;

    @Override
    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }

    public static DVDGenre getEnum(String value) {
        for (DVDGenre d : values())
            if (d.toString().equalsIgnoreCase(value)) return d;
        throw new IllegalArgumentException();
    }
}
