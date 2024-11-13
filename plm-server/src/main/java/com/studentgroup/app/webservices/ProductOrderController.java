package com.studentgroup.app.webservices;

import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studentgroup.app.model.*;
import com.studentgroup.app.model.repositories.UserRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
public class ProductOrderController {

    @Autowired
    UserRepository userRepo;

    @Autowired
    ObjectMapper mapper;
 
    @GetMapping("/product-orders/{username}/checking")
    public ResponseEntity<List<ProductOrder>> getChecking(@PathVariable String username) {
        EmployeeUser user = userRepo.findByUsername(username);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        List<ProductOrder> orders = user.getCheckingOrders();

        return ResponseEntity.ok().body(orders);
    }

    @GetMapping("/product-orders/{username}/dispatching")
    public ResponseEntity<List<ProductOrder>> getDispatching(@PathVariable String username) {
        EmployeeUser user = userRepo.findByUsername(username);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        List<ProductOrder> orders = user.getDispatchingOrders();

        return ResponseEntity.ok().body(orders);
    }

}