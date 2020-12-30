package org.libmanager.client.enums;

import org.libmanager.client.util.Converter;

public enum ItemType {
    ANY,
    BOOK,
    DVD;

    String getName() {
        return Converter.getTypeConverter().toString(this);
    }
}
