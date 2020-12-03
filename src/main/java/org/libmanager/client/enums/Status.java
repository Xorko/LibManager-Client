package org.libmanager.client.enums;

public enum Status {
    AVAILABLE(true, "Disponible"), BORROWED(false, "Emprunté");

    boolean available;
    String name;

    Status(boolean available, String name) {
        this.available = available;
        this.name = name;
    }

    public boolean isAvailable() {
        return available;
    }

    @Override
    public String toString() {
        return name;
    }
}