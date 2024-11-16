package com.studentgroup.app.webservices.authorization;

import com.studentgroup.app.model.EmployeeUser;

public class AuthorizationResult {
    private AuthorizationStatus status;
    private EmployeeUser user;

    public AuthorizationResult(AuthorizationStatus status, EmployeeUser user) {
        this.status = status;
        this.user = user;
    }
    
    public AuthorizationStatus getStatus() {
        return status;
    }
    public EmployeeUser getUser() {
        return user;
    }
}
