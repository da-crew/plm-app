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
}
