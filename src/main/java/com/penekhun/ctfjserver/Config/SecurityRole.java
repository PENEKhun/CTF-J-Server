package com.penekhun.ctfjserver.Config;


public enum SecurityRole {
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");

    private final String myRole;

    SecurityRole(final String myRole) {
        this.myRole = myRole;
    }

    public String toString() {
        return myRole;
    }
}
