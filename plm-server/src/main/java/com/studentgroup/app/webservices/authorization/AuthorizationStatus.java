package com.studentgroup.app.webservices.authorization;

public enum AuthorizationStatus {
    INVALID_CREDENTIAL,
    USER_NOT_FOUND,
    INCORRECT_PASSWORD,
    NO_PERMISSION,
    SUCCESSFUL;
}
