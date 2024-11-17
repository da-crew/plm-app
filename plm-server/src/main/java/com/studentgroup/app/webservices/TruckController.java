package com.studentgroup.app.webservices;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.studentgroup.app.model.Truck;
import com.studentgroup.app.model.enums.Role;
import com.studentgroup.app.model.repositories.TruckRepository;
import com.studentgroup.app.webservices.authorization.AuthorizationManager;
import com.studentgroup.app.webservices.authorization.AuthorizationResult;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class TruckController {
    @Autowired
    TruckRepository truckRepo;

    @Autowired
    AuthorizationManager authMan;

    @GetMapping("/trucks")
    public Iterable<Truck> getAllTrucks() {
        return truckRepo.findAll();
    }

    @GetMapping("/trucks/{truckNumber}")
    public ResponseEntity<Truck> getTruckById(@PathVariable Long truckNumber) {
        Optional<Truck> truck = truckRepo.findById(truckNumber);
        if (!truck.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok().body(truck.get());
    }
    
    /*
    Request Body: {
        truckNumber: String,

        caller: {
            username: String,
            password: String
        }
    }
     */
    @PostMapping("/trucks")
    public ResponseEntity<String> addNewTruck(@RequestBody JsonNode json) throws Exception {
        AuthorizationResult authRes = authMan.authorizeFromJson(json.get("caller"), Role.ADMIN);
        switch (authRes.getStatus()) {
            case INVALID_CREDENTIAL:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("invalid credential!");
            case USER_NOT_FOUND:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("caller not found!");
            case INCORRECT_PASSWORD:
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("incorrect password!");
            case NO_PERMISSION:
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("are not permitted to do this!");
            case SUCCESSFUL: break;
        }
        

        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("in construction...");
    }

}