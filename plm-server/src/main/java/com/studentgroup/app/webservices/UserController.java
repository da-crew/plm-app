package com.studentgroup.app.webservices;


import com.studentgroup.app.Misc;

import org.apache.catalina.connector.Response;
import org.hibernate.query.NativeQuery.ReturnProperty;
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
import com.studentgroup.app.model.repositories.ProductOrderRepository;
import com.studentgroup.app.model.repositories.UserRepository;
import com.studentgroup.app.webservices.authorization.AuthorizationManager;
import com.studentgroup.app.webservices.authorization.AuthorizationResult;

import org.springframework.web.bind.annotation.RequestMethod;


@RestController
public class UserController {

    @Autowired
    UserRepository userRepo;

    @Autowired
    ObjectMapper mapper;

    @Autowired 
    ProductOrderRepository productOrderRepo;

    @Autowired
    DatabaseInitializer dbInitializer;

    @Autowired
    AuthorizationManager authMan;

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
     * Request Body:
     * {
     *      caller: {
     *          username: String,
     *          password: String,
     *      },
     *      userInfo: [see UserInfo.fromJson ]
     * }
     * Returns:
     *      BAD_REQUEST,
     *      NOT_FOUND,
     *      
     */

    @RequestMapping(path = "/users/{username}/update", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> getMethodName(@PathVariable String username, @RequestBody JsonNode json) throws Exception {
        AuthorizationResult authRes = authMan.authorizeFromJson(json.get("caller"), Role.ADMIN);
        switch (authRes.getStatus()) {
            case INVALID_CREDENTIAL:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("invalid credential!");
            case USER_NOT_FOUND:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("caller not found!");
            case INCORRECT_PASSWORD:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("incorrect password!");
            case NO_PERMISSION:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("are not permitted to do this!");
            case SUCCESSFUL: break;
        }

        //validation
        UserInfo updateInfo = UserInfo.fromJson(json.get("userInfo"));
        if (updateInfo == null) {
            return ResponseEntity.badRequest().body("invalid userInfo!");
        }

        EmployeeUser user = userRepo.findByUsername(username);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        //check if the new username already exists
        if (userRepo.findByUsername(updateInfo.getUsername()) != null && !updateInfo.getUsername().equals(user.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("username already exists!");
        }

        user.setUsername(updateInfo.getUsername());
        user.setFirstname(updateInfo.getFirstname());
        user.setLastname(updateInfo.getFirstname());
        user.setRole(updateInfo.getRole());


        return ResponseEntity.ok().body("Changed ");

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
    public ResponseEntity<String> registerUser(@RequestBody JsonNode json) throws Exception {

        UserInfo userInfo = UserInfo.fromJson(json.get("user"));
        String password = Misc.jsonToString(json, "password");
        UserCreds callerCreds = json.get("caller") != null ? UserCreds.fromJson(json.get("caller")) : null;

        if (userInfo == null || callerCreds == null || password == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("invalid data");

        EmployeeUser caller = userRepo.findByUsername(callerCreds.getUsername());
        if (caller == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("couldn't find a user");

        //Authorization
        if (!caller.verify(callerCreds.getPassword()))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("invalid credentials");
        if (caller.getRole() != Role.ADMIN)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("you are not allowed to do this!");


        if (userRepo.findByUsername(userInfo.getUsername()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }

        EmployeeUser emp = new EmployeeUser(userInfo, password);
        userRepo.save(emp);

        return ResponseEntity.status(HttpStatus.CREATED).body("Successfully registered");
    }

}
