package com.studentgroup.app.webservices;

import java.security.NoSuchAlgorithmException;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.studentgroup.app.model.EmployeeUser;
import com.studentgroup.app.model.Role;
import com.studentgroup.app.model.UserRepository;

import jakarta.annotation.PostConstruct;
import org.springframework.web.bind.annotation.RequestMethod;


@RestController
public class UserController {

    @Autowired
    UserRepository userRepo;

    @Autowired
    ObjectMapper mapper;

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

    @RequestMapping(path = "/test/json", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<JsonNode> testJSON(@RequestBody JsonNode json) {
        //ObjectNode node = mapper
        return ResponseEntity.ok().body(json);
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

    @GetMapping("/users/auth")
    public ResponseEntity<AuthResult> authUser(@RequestParam("username") String username,
                                                @RequestParam("password") String password) throws Exception {
        if (username == "" || password == "") {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        EmployeeUser emp = userRepo.findByUsername(username);
        if (emp == null) {
            return ResponseEntity.notFound().build();
        }

        if (!emp.verify(password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        return ResponseEntity.ok(AuthResult.fromUser(emp));
    }

    /*
    Request Body: {
        username: String,
        password: String,
        role: String,
        caller: {
            username: String,
            password: String
        }
    }
    Returns:
        BAD_REQUEST
        FORBIDDEN
        CONFLICT
        CREATED
    */
    @RequestMapping(path = "/users/register", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<AuthResult> registerUser(@RequestBody JsonNode json) throws Exception {

        String username = json.get("username") != null
                ? json.get("username").toString()
                : null;

        String password = json.get("password") != null
                ? json.get("password").toString()
                : null;

        Role role = json.get("role") != null
                ? Enum.valueOf(Role.class, json.get("role").toString())
                : null;

        JsonNode callerNode = json.get("caller");

        if (username == null || password == null || role == null || callerNode == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        String callerUsername = callerNode.get("username") != null
                ? callerNode.get("username").toString()
                : null;

        String callerPassword = callerNode.get("password") != null
                ? callerNode.get("password").toString()
                : null;

        if (callerUsername == null || callerPassword == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        EmployeeUser caller = userRepo.findByUsername(callerUsername);

        if (caller == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        //Authorization
        if (!caller.verify(callerPassword))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        if (caller.getRole() != Role.ADMIN)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);


        if (userRepo.findByUsername(username) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }

        EmployeeUser emp = new EmployeeUser(username, password, role);
        userRepo.save(emp);

        return ResponseEntity.status(HttpStatus.CREATED).body(AuthResult.fromUser(emp));
    }

    
//    @GetMapping("/users/role")
//    public ResponseEntity<ObjectNode> checkRole(@RequestParam("token") String token) {
//        if (token == null) {
//            return ResponseEntity.badRequest().body(null);
//        }
//
//        EmployeeUser user = userRepo.findByToken(token);
//        if (user == null) {
//            return ResponseEntity.notFound().build();
//        }
//        ObjectNode obj = mapper.createObjectNode();
//        obj.put("role", user.getRole().toString());
//
//        return ResponseEntity.ok().body(obj);
//    }
//
//    @GetMapping("/validate-token")
//    public ResponseEntity<ObjectNode> validateToken(@RequestParam("token") String token) {
//        if (token == null) {
//            return ResponseEntity.badRequest().body(null);
//        }
//
//        if (userRepo.findByToken(token) != null) {
//            return ResponseEntity.ok().body(null);
//        }
//        return ResponseEntity.notFound().build();
//    }
//
//    @GetMapping("/users/auth-token")
//    public ResponseEntity<AuthResult> authToken(@RequestParam("token") String token) {
//        if (token == null) {
//            return ResponseEntity.badRequest().body(null);
//        }
//
//        EmployeeUser user = userRepo.findByToken(token);
//
//        if (user != null) {
//            return ResponseEntity.ok().body(AuthResult.fromUser(user));
//        }
//        return ResponseEntity.notFound().build();
//    }
//

}
