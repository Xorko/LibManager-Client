package org.libmanager.client.enums;

import org.libmanager.client.util.Converter;

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

    public String getName() {
        return Converter.getDvdGenreConverter().toString(this);
    }

    public static DVDGenre getEnum(String value) {
        for (DVDGenre d : values())
            if (d.toString().equalsIgnoreCase(value)) return d;
        throw new IllegalArgumentException();
    }
}
