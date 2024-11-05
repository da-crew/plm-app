package com.studentgroup.app.webservices;

import com.fasterxml.jackson.databind.JsonNode;
import com.studentgroup.app.Misc;
import org.apache.catalina.User;

public class UserCreds {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    /*
    Json must look this:
    {
        username: String,
        password: String
    }
     */
    public static UserCreds fromJson(JsonNode json) {
        if (json == null)
            return null;

        UserCreds creds = new UserCreds();
        creds.username = Misc.jsonToString(json, "username");
        creds.password = Misc.jsonToString(json, "password");

        if (creds.username == null || creds.password == null)
            return null;

        return creds;
    }
}
