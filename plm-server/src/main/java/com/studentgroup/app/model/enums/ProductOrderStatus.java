package com.studentgroup.app.model.enums;

public enum ProductOrderStatus {
    UNKNOWN("UNKNOWN"),
    CHECKING("CHECKING"),
    EXPORTING("EXPORTING"),
    FINISHED("FINISHED"),
    REPORTED("REPORTED");

    private final String name;

    private ProductOrderStatus(String s) {
        name = s;
    }

    public String toString() {
        return this.name;
    }

    public static Role fromString(String name) {
        Role res = null;
        try {
            res = Enum.valueOf(Role.class, name);
        } catch (IllegalArgumentException e) {
            return null;
        }
        return res;
    }
}
