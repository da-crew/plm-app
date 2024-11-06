package com.studentgroup.app.webservices;

import com.fasterxml.jackson.databind.JsonNode;
import com.studentgroup.app.model.EmployeeUser;
import com.studentgroup.app.model.enums.Role;
import com.studentgroup.app.Misc;

public class UserInfo {

    private String username;
    private String firstname;
    private String lastname;
    private Role role;

    public String getUsername() {
        return username;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public Role getRole() {
        return role;
    }

    public UserInfo() {}

    public UserInfo(
            String username,
            String firstname,
            String lastname,
            Role role
    ) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.role = role;
    }

    public static UserInfo fromJson(JsonNode json) {
        UserInfo user = new UserInfo();
        user.username = Misc.jsonToString(json, "username");
        user.firstname = Misc.jsonToString(json, "firstname");
        user.lastname = Misc.jsonToString(json, "lastname");
        user.role = Misc.jsonToString(json, "role") != null ? Role.fromString(Misc.jsonToString(json, "role")) : null;

        if (user.username == null || user.firstname == null || user.lastname == null || user.role == null)
            return null;

        return user;
    }

    public static UserInfo fromUser(EmployeeUser user) {
        UserInfo newRes =  new UserInfo();

        newRes.username = user.getUsername();
        newRes.firstname = user.getFirstname();
        newRes.lastname = user.getLastname();
        newRes.role = user.getRole();

        return newRes;
    }
}