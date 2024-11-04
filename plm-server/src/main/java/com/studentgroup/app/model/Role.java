package com.studentgroup.app.model;

public enum Role {
    UNKNOWN("UNKNOWN"),
    DISPATCHER("DISPATCHER"),
    EXPORTER("EXPORTER"),
    CHECKER("CHECKER"),
    ADMIN("ADMIN");

    private final String name;

    private Role(String s) {
        name = s;
    }

    public String toString() {
        return this.name;
    }

    public static Role fromString(String name) {
        try {
            return Enum.valueOf(Role.class, name);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
