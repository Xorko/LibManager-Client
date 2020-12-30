package org.libmanager.client.enums;

public enum AdminSettings implements Settings {
    SERVER;

    @Override
    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }

    @Override
    public final String getKey() {
        return getClass().getSimpleName() + '.' + name();
    }
}
