package org.libmanager.client.enums;

import org.libmanager.client.util.Converter;

public enum Theme {

    LIGHT,
    DARK;

    public String getName() {
        return Converter.getThemeConverter().toString(this);
    };
}
