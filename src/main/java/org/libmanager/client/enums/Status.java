package org.libmanager.client.enums;

public enum Status {
    UNAVAILABLE(0), AVAILABLE(1), ANY(2);

    int status;

    Status(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}