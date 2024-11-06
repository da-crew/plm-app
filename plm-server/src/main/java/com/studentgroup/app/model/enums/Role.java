package com.studentgroup.app.model.enums;

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
        Role res = null;
        try {
            res = Enum.valueOf(Role.class, name);
        } catch (IllegalArgumentException e) {
            return null;
        }
        return res;
    }
}
