package com.studentgroup.app.webservices;

import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.studentgroup.app.model.EmployeeUser;
import com.studentgroup.app.model.Role;
import com.studentgroup.app.model.UserRepository;

import jakarta.annotation.PostConstruct;

class AuthResult {
    public String token;

    public AuthResult(String token) {
        this.token = token;
    }
}

class CredsRequest {
    public String username;
    public String password;

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}


@RestController
public class UserController {

    @Autowired
    UserRepository userRepo;

    @PostConstruct
    public void initDatabase() throws NoSuchAlgorithmException {
        /**/
        EmployeeUser[] users = {
            new EmployeeUser("skyline87", "Xy7gK@8!", Role.DISPATCHER),
            new EmployeeUser("quickstorm23", "9vDl#U3m", Role.EXPORTER),
            new EmployeeUser("crimsonwolf11", "P@ssW0rd!2", Role.CHECKER),
            new EmployeeUser("neonbyte9", "K3n$3iOq", Role.ADMIN),
            new EmployeeUser("ironhawk45", "G3l!nT@57", Role.DISPATCHER),
            new EmployeeUser("blueglade19", "C5f#Q1z9", Role.CHECKER),
            new EmployeeUser("emberforge77", "!Rn4Dv5w", Role.EXPORTER),
            new EmployeeUser("solaris22", "R&9Jt$zX", Role.ADMIN),
            new EmployeeUser("cyberwave55", "Mf3#xD2*", Role.DISPATCHER),
            new EmployeeUser("moonblade3", "QzT@8n$7", Role.EXPORTER)
        };

        for (EmployeeUser user : users) {
            userRepo.save(user);
        }
        //*/
    }


    @GetMapping("/test")
    public ResponseEntity<AuthResult> testGet() {
        return new ResponseEntity<AuthResult>(new AuthResult("OK"), HttpStatus.OK);
    }
    
    @GetMapping("/test/users/{username}")
    public ResponseEntity<EmployeeUser> getMethodName(@PathVariable String username) {
        EmployeeUser emp = userRepo.findByUsername(username);
        if (emp == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<EmployeeUser>(emp, HttpStatus.OK);
    }

    
    @GetMapping("/test/users")
    public ResponseEntity<Iterable<EmployeeUser>> getUsers() {
        return new ResponseEntity<Iterable<EmployeeUser>>(userRepo.findAll(), HttpStatus.OK);
    }

    //@RequestMapping(value = "/auth", method=RequestMethod.GET, consumes = "application/json")
    @RequestMapping("/auth")
    public ResponseEntity<AuthResult> authUser(@RequestBody CredsRequest creds) throws Exception {
        String username = creds.username;//jsonNode.get("username").asText();
        String password = creds.password;//jsonNode.get("password").asText();

        if (username == null || password == null) {
            return new ResponseEntity<AuthResult>(new AuthResult("BAD REQUEST: HI"), HttpStatus.BAD_REQUEST);
        }

        EmployeeUser emp = userRepo.findByUsername(username);
        if (emp == null) {
            return new ResponseEntity<AuthResult>(new AuthResult("USER NOT FOUND"), HttpStatus.NOT_FOUND);
        }

        if (!emp.verify(password)) {
            return new ResponseEntity<AuthResult>(new AuthResult("password incorrect"), HttpStatus.UNAUTHORIZED);
        }

        return ResponseEntity.ok(new AuthResult(emp.getToken()));
    }

    @RequestMapping(value = "/users/register", method=RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<String> registerUser(@RequestBody JsonNode jsonNode) throws Exception {
        JsonNode usernamejson = jsonNode.get("username");
        JsonNode passwordjson = jsonNode.get("password");
        JsonNode roleStringjson = jsonNode.get("role");
        JsonNode tokenjson = jsonNode.get("token");

        if (usernamejson == null || passwordjson == null || roleStringjson == null || tokenjson == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        String username = usernamejson.asText();
        String password = passwordjson.asText();
        String roleString = roleStringjson.asText();
        String token = tokenjson.asText();

        EmployeeUser caller = userRepo.findByToken(token);
        if (caller == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        if (caller.getRole() != Role.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        
        Role role = Role.UNKNOWN;
        switch (roleString) {
            case "DISPATCHER": 
                role = Role.DISPATCHER;
                break;
            case "EXPORTER":
                role = Role.EXPORTER;
                    break;
            case "CHECKER":
                role = Role.CHECKER;
                break;
            case "ADMIN":
                role = Role.ADMIN;
                break;
        }

        if (role == Role.UNKNOWN) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (userRepo.findByUsername(username) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("");
        }

        EmployeeUser emp = new EmployeeUser(username, password, role);
        userRepo.save(emp);

        return new ResponseEntity<String>(emp.toString(), HttpStatus.CREATED);
    }

}
