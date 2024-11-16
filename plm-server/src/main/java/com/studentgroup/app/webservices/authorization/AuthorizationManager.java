package com.studentgroup.app.webservices.authorization;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.studentgroup.app.model.EmployeeUser;
import com.studentgroup.app.model.enums.Role;
import com.studentgroup.app.model.repositories.UserRepository;
import com.studentgroup.app.webservices.UserCreds;

@Component
public class AuthorizationManager {
    @Autowired
    UserRepository userRepo;

    public AuthorizationResult authorizeFromJson(JsonNode json, Role... permittedRoles) throws Exception {
        List<Role> roles = Arrays.asList(permittedRoles);
        UserCreds callerCreds = UserCreds.fromJson(json);
        if (callerCreds == null) {
            return new AuthorizationResult(AuthorizationStatus.INVALID_CREDENTIAL, null);
        }
        EmployeeUser caller = userRepo.findByUsername(callerCreds.getUsername());
        if (caller == null) {
            return new AuthorizationResult(AuthorizationStatus.USER_NOT_FOUND, null);
        }

        if (!caller.verify(callerCreds.getPassword()))
            return new AuthorizationResult(AuthorizationStatus.INCORRECT_PASSWORD, null);
        if (roles.indexOf(caller.getRole()) == -1)
            return new AuthorizationResult(AuthorizationStatus.NO_PERMISSION, caller);
        
        return new AuthorizationResult(AuthorizationStatus.SUCCESSFUL, caller);
    }

    public AuthorizationResult authorizeFromUserCreds(UserCreds userCreds, Role... permittedRoles) throws Exception {
        List<Role> roles = Arrays.asList(permittedRoles);

        EmployeeUser caller = userRepo.findByUsername(userCreds.getUsername());
        if (caller == null) {
            return new AuthorizationResult(AuthorizationStatus.USER_NOT_FOUND, null);
        }

        if (!caller.verify(userCreds.getPassword()))
            return new AuthorizationResult(AuthorizationStatus.INCORRECT_PASSWORD, null);
        if (roles.indexOf(caller.getRole()) == -1)
            return new AuthorizationResult(AuthorizationStatus.NO_PERMISSION, caller);
    
        return new AuthorizationResult(AuthorizationStatus.SUCCESSFUL, caller);
    }
}
