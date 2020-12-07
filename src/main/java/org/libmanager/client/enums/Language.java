package org.libmanager.client.enums;

import java.util.Locale;

public enum Language {
    fr_FR(Locale.FRANCE),
    en_US(Locale.US);


    private final Locale locale;

    Language(Locale locale) {
        this.locale = locale;
    }

    public Locale getLocale() {
        return locale;
    }
}
