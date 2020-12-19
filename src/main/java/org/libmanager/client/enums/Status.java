package org.libmanager.client.enums;

import org.libmanager.client.util.Converter;

public enum Status {
    UNAVAILABLE(0), AVAILABLE(1), ANY(2);

    int status;

    Status(int status) {
        this.status = status;
    }

    public String getName() {
        return Converter.getStatusConverter().toString(this);
    }

    public int getStatus() {
        return status;
    }
}