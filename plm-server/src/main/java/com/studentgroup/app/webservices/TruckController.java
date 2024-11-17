package com.studentgroup.app.webservices;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.studentgroup.app.model.Truck;
import com.studentgroup.app.model.repositories.TruckRepository;
import com.studentgroup.app.webservices.authorization.AuthorizationManager;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    
    
}