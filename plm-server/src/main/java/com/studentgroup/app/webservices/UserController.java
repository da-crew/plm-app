package com.studentgroup.app.webservices;

import java.security.NoSuchAlgorithmException;

import com.studentgroup.app.Misc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studentgroup.app.model.EmployeeUser;
import com.studentgroup.app.model.enums.Role;
import com.studentgroup.app.model.repositories.UserRepository;

import jakarta.annotation.PostConstruct;
import org.springframework.web.bind.annotation.RequestMethod;


@RestController
public class UserController {

    @Autowired
    UserRepository userRepo;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    DatabaseInitializer dbInitializer;

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
    public ResponseEntity<UserInfo> authUser(@RequestParam("username") String username,
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

        return ResponseEntity.ok(UserInfo.fromUser(emp));
    }

    /*
    Request Body: {
        user: {
            username: String,
            firstname: String,
            lastname: String,
            role: String
        }

        password: String,

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
    public ResponseEntity<UserInfo> registerUser(@RequestBody JsonNode json) throws Exception {

        UserInfo userInfo = UserInfo.fromJson(json.get("user"));
        String password = Misc.jsonToString(json, "password");
        UserCreds callerCreds = json.get("caller") != null ? UserCreds.fromJson(json.get("caller")) : null;

        if (userInfo == null || callerCreds == null || password == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        EmployeeUser caller = userRepo.findByUsername(callerCreds.getUsername());
        if (caller == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        //Authorization
        if (!caller.verify(callerCreds.getPassword()))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        if (caller.getRole() != Role.ADMIN)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);


        if (userRepo.findByUsername(userInfo.getUsername()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }

        EmployeeUser emp = new EmployeeUser(userInfo, password);
        userRepo.save(emp);

        return ResponseEntity.status(HttpStatus.CREATED).body(UserInfo.fromUser(emp));
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
