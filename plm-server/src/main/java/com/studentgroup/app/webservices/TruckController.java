package com.studentgroup.app.webservices;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.studentgroup.app.Misc;
import com.studentgroup.app.model.Car;
import com.studentgroup.app.model.Truck;
import com.studentgroup.app.model.enums.Role;
import com.studentgroup.app.model.repositories.CarRepository;
import com.studentgroup.app.model.repositories.TruckRepository;
import com.studentgroup.app.webservices.authorization.AuthorizationManager;
import com.studentgroup.app.webservices.authorization.AuthorizationResult;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class TruckController {
    @Autowired
    TruckRepository truckRepo;

    @Autowired
    AuthorizationManager authMan;

    @Autowired
    CarRepository carRepo;

    @GetMapping("/trucks")
    public Iterable<Truck> getAllTrucks() {
        return truckRepo.findAll();
    }

    @GetMapping("/trucks/{truckNumber}")
    public ResponseEntity<Truck> getTruckById(@PathVariable String truckNumber) {
        Truck truck = truckRepo.findByTruckNumber(truckNumber);
        if (truck == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok().body(truck);
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
        AuthorizationResult authRes = authMan.authorizeFromJson(json.get("caller"), Role.ADMIN, Role.DISPATCHER, Role.CHECKER);
        switch (authRes.getStatus()) {
            case INVALID_CREDENTIAL:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("invalid credential!");
            case USER_NOT_FOUND:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("caller not found!");
            case INCORRECT_PASSWORD:
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("incorrect password!");
            case NO_PERMISSION:
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("you are not permitted to do this!");
            case SUCCESSFUL: break;
        }

        String truckNumber = Misc.jsonToString(json, "truckNumber");
        if (truckRepo.findByTruckNumber(truckNumber) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("truck already exists!");
        }

        Truck truck = new Truck(truckNumber);
        truckRepo.save(truck);

        return ResponseEntity.status(HttpStatus.CREATED).body("truck created");
    }

    /*
    Request Body: {
        caller: {
            username: String,
            password: String
        }
    }
     */
    @DeleteMapping("/trucks/{truckNumber}")
    public ResponseEntity<String> deleteTruck(@RequestBody JsonNode json, @PathVariable String truckNumber) throws Exception {
        AuthorizationResult authRes = authMan.authorizeFromJson(json.get("caller"), Role.ADMIN, Role.DISPATCHER, Role.CHECKER);
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

        Truck truck = truckRepo.findByTruckNumber(truckNumber);
        if (truck == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("truck not found!");
        }

        for (Car car : truck.getCars()) {
            car.setTruck(null);
            car.setProductOrder(null);
            carRepo.save(car);
        }

        truckRepo.delete(truck);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("deleted truck " + truck.getTruckNumber());
    }

}