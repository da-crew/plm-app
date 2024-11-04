package com.studentgroup.app.webservices;

import com.studentgroup.app.model.EmployeeUser;

public class AuthResult {

    public String username;
    public String firstname;
    public String lastname;
    public String role;

    public AuthResult() {}

    public AuthResult(
            String username,
            String firstname,
            String lastname,
            String role
    ) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.role = role;
    }

    public static AuthResult fromUser(EmployeeUser user) {
        AuthResult newRes =  new AuthResult();

        newRes.username = user.getUsername();
        newRes.firstname = user.getFirstname();
        newRes.lastname = user.getLastname();
        newRes.role = user.getRole().toString();

        return newRes;
    }
}