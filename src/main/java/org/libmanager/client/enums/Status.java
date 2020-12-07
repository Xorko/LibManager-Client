package org.libmanager.client.enums;

public enum Status {
    AVAILABLE(true), UNAVAILABLE(false);

    boolean available;

    Status(boolean available) {
        this.available = available;
    }

    public boolean isAvailable() {
        return available;
    }
}