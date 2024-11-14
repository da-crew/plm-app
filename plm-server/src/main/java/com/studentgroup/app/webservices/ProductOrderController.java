package com.studentgroup.app.webservices;

import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studentgroup.app.model.*;
import com.studentgroup.app.model.enums.ProductOrderStatus;
import com.studentgroup.app.model.enums.Role;
import com.studentgroup.app.model.repositories.ProductOrderRepository;
import com.studentgroup.app.model.repositories.UserRepository;
import com.studentgroup.app.webservices.authorization.*;
import com.studentgroup.app.webservices.authorization.AuthorizationResult;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class ProductOrderController {

    @Autowired
    UserRepository userRepo;

    @Autowired
    ProductOrderRepository prodOrderRepo;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    AuthorizationManager authMan;

    /*
     * Request Body: {
     * productOrder: [see method ProductOrderInfo.fromJsonNode]
     * 
     * caller: {
     * username: String,
     * password: String
     * }
     * 
     * }
     * Returns:
     * BAD_REQUEST
     * FORBIDDEN
     * CREATED
     */

    @PostMapping("/product-orders/create")
    public ResponseEntity<String> postMethodName(@RequestBody JsonNode json) throws Exception {

        // authorization
        AuthorizationResult authRes = authMan.authorizeFromJson(json.get("caller"), Role.ADMIN, Role.DISPATCHER);
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

        EmployeeUser caller = authRes.getUser();

        // validation
        ProductOrderInfo prodInfo = ProductOrderInfo.fromJsonNode(json.get("productOrder"));

        if (prodInfo == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("invalid product order data!");
        }

        ProductOrder newProductOrder = new ProductOrder(
                prodInfo.getBLNumber(),
                prodInfo.getOrderDate(),
                prodInfo.getVesselName(),
                prodInfo.getVoyNumber(),
                prodInfo.getCosigneeName(),
                null,
                ProductOrderStatus.CHECKING);

        if (prodOrderRepo.findByBLNumber(newProductOrder.getBLNumber()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("product order already exists");
        }

        ActionLog actionLog = new ActionLog("Create product order with BL Number " + newProductOrder.getBLNumber());
        newProductOrder.addActionLog(actionLog, caller);
        caller.assignAsDispatcher(newProductOrder);

        prodOrderRepo.save(newProductOrder);
        userRepo.save(caller);

        return ResponseEntity.ok().build();
    }



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

    @GetMapping("/product-orders/{username}/")
    public ResponseEntity<List<ProductOrder>> getAll(@PathVariable String username) {
        EmployeeUser user = userRepo.findByUsername(username);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        List<ProductOrder> orders = user.getDispatchingOrders();
        orders.addAll(user.getCheckingOrders());

        return ResponseEntity.ok().body(orders);
    }
}