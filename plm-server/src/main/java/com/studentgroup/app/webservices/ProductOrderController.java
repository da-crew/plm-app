package com.studentgroup.app.webservices;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studentgroup.app.Misc;
import com.studentgroup.app.model.*;
import com.studentgroup.app.model.enums.ProductOrderStatus;
import com.studentgroup.app.model.enums.Role;
import com.studentgroup.app.model.repositories.ProductOrderRepository;
import com.studentgroup.app.model.repositories.UserRepository;
import com.studentgroup.app.service.FileStorageService;
import com.studentgroup.app.webservices.authorization.*;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

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

    @Autowired
    FileStorageService storageService;

    /*
     * Request Body: {
     * productOrder: [see method ProductOrderInfo.fromJsonNode]
     * 
     * caller: {
     * username: String,
     * password: String
     * }
     * 
     * checker: String(checker's username)
     * 
     * }
     * Returns:
     * BAD_REQUEST
     * FORBIDDEN
     * CREATED
     */
    @PostMapping("/product-orders/create")
    public ResponseEntity<String> createProductOrder(@RequestBody JsonNode json) throws Exception {

        // authorization
        AuthorizationResult authRes = authMan.authorizeFromJson(json.get("caller"), Role.ADMIN, Role.DISPATCHER);
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

        EmployeeUser caller = authRes.getUser();

        // validation
        EmployeeUser checker = userRepo.findByUsername(Misc.jsonToString(json, "checker"));
        if (checker == null) {
            return ResponseEntity.badRequest().body("couldn't find a checker!");
        }

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
        checker.assignAsChecker(newProductOrder);

        prodOrderRepo.save(newProductOrder);
        userRepo.save(checker);
        userRepo.save(caller);

        return ResponseEntity.ok().build();
    }


    
    @PostMapping("/product-orders/{blNumber}/set-image")
    public ResponseEntity<String> setProductOrderImage(@PathVariable String blNumber, @RequestParam("file") MultipartFile file) {
        ProductOrder productOrder = prodOrderRepo.findByBLNumber(blNumber);
        if (productOrder == null) {
            return ResponseEntity.notFound().build();
        }
        
        try {
            ImageFile imgFile = storageService.store(file);
            productOrder.setWharfReceiptImgUrl(imgFile.getId());
            prodOrderRepo.save(productOrder);
            return ResponseEntity.ok().body("successful");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    /*
     * Request Body: {
     * productOrder: [see method ProductOrderInfo.fromJsonNode]
     * 
     * caller: {
     * username: String,
     * password: String
     * }
     * 
     * checker: String(checker's username)
     * 
     * }
     * Returns:
     * BAD_REQUEST
     * FORBIDDEN
     * CREATED
     *
    @PostMapping("/product-orders/{blNumber}/assign-for-checking")
    public ResponseEntity<String> assignForChecking(@PathVariable String blNumber, @RequestBody JsonNode json) throws Exception {
        //authorization
        AuthorizationResult authRes = authMan.authorizeFromJson(json.get("caller"), Role.ADMIN, Role.DISPATCHER);
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

        //validation
        ProductOrder productOrder = prodOrderRepo.findByBLNumber(blNumber);
        if (productOrder == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("product order not found!");
        }

        EmployeeUser checker = userRepo.findByUsername(Misc.jsonToString(json, blNumber));
        if (checker == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("checker not found!");
        }

        if (productOrder.getChecker() != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("product order already have a checker!");
        }

        checker.assignAsChecker(productOrder);
        ActionLog actionLog = new ActionLog(String.format("Assign product order with BL Number %s to %s for checking.", productOrder.getBLNumber(), checker.getUsername()));
        productOrder.addActionLog(actionLog, authRes.getUser());
        prodOrderRepo.save(productOrder);
        userRepo.save(checker);
        userRepo.save(authRes.getUser());

        return ResponseEntity.ok().body(String.format("Successfully assigned ", productOrder.getBLNumber(), checker.getUsername()));
    }
    */

    /*

      [ROLE]             [STATUS]

    DISPATCHER --------- REPORTED
                          |   ^
                /forward  |   |    /return
                          v   |
    CHECKER    --------- CHECKING <======= Start Here
                          |   ^
                /forward  |   |    /return
                          v   |
    EXPORTER   --------- EXPORTING
                          |
                /forward  |
                          v
                         FINISHED
    
    /*
     * Request Body: {
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
     * NOT_FOUND
     * NOT_ACCEPTABLE
     * OK
     * 
     */
    @PostMapping("/product-orders/{blNumber}/return")
    public ResponseEntity<String> returnProductOrder(@PathVariable String blNumber, @RequestBody JsonNode json) throws Exception {
        AuthorizationResult authRes = authMan.authorizeFromJson(json.get("caller"), Role.ADMIN, Role.EXPORTER, Role.CHECKER);
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

        ProductOrder productOrder = prodOrderRepo.findByBLNumber(blNumber);
        if (productOrder == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("product order not found!");
        }

        EmployeeUser caller = authRes.getUser();

        String actionFormatMessage = "Change status from " + productOrder.getStatusName().toString() + " to %s. (return)";
        ActionLog actionLog;

        if ((caller.getRole() == Role.CHECKER || caller.getRole() == Role.ADMIN) && productOrder.getStatusName() == ProductOrderStatus.CHECKING) {
            productOrder.setStatusName(ProductOrderStatus.REPORTED);
            actionLog = new ActionLog(String.format(actionFormatMessage, ProductOrderStatus.REPORTED));
        } else if ((caller.getRole() == Role.EXPORTER || caller.getRole() == Role.ADMIN) && productOrder.getStatusName() == ProductOrderStatus.EXPORTING) {
            productOrder.setStatusName(ProductOrderStatus.CHECKING);
            actionLog = new ActionLog(String.format(actionFormatMessage, ProductOrderStatus.CHECKING));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("you are not allowed to do this!");
        } 

        productOrder.addActionLog(actionLog, caller);
        prodOrderRepo.save(productOrder);
        userRepo.save(caller);

        return ResponseEntity.status(HttpStatus.OK).body("Returned successfully");
    }


    /*
     * Request Body: {
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
     * NOT_FOUND
     * NOT_ACCEPTABLE
     * OK
     * 
     */
    @PostMapping("/product-orders/{blNumber}/forward")
    public ResponseEntity<String> forwardProductOrder(@PathVariable String blNumber, @RequestBody JsonNode json) throws Exception {
        AuthorizationResult authRes = authMan.authorizeFromJson(json.get("caller"), Role.ADMIN, Role.EXPORTER, Role.CHECKER);
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

        ProductOrder productOrder = prodOrderRepo.findByBLNumber(blNumber);
        if (productOrder == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("product order not found!");
        }

        EmployeeUser caller = authRes.getUser();

        String actionFormatMessage = "Change status from " + productOrder.getStatusName().toString() + " to %s. (forward)";
        ActionLog actionLog;

        if ((caller.getRole() == Role.DISPATCHER  || caller.getRole() == Role.ADMIN) && productOrder.getStatusName() == ProductOrderStatus.REPORTED) {
            productOrder.setStatusName(ProductOrderStatus.CHECKING);
            actionLog = new ActionLog(String.format(actionFormatMessage, ProductOrderStatus.CHECKING));
        } else if ((caller.getRole() == Role.CHECKER || caller.getRole() == Role.ADMIN) && productOrder.getStatusName() == ProductOrderStatus.CHECKING) {
            productOrder.setStatusName(ProductOrderStatus.EXPORTING);
            actionLog = new ActionLog(String.format(actionFormatMessage, ProductOrderStatus.CHECKING));
        } else if ((caller.getRole() == Role.EXPORTER || caller.getRole() == Role.ADMIN) && productOrder.getStatusName() == ProductOrderStatus.EXPORTING) {
            productOrder.setStatusName(ProductOrderStatus.FINISHED);
            actionLog = new ActionLog(String.format(actionFormatMessage, ProductOrderStatus.FINISHED));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("you are not allowed to do this!");
        }


        productOrder.addActionLog(actionLog, caller);
        prodOrderRepo.save(productOrder);
        userRepo.save(caller);

        return ResponseEntity.status(HttpStatus.OK).body("Forwarded successfully");
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
    public ResponseEntity<List<ProductOrder>> getAllFromUsername(@PathVariable String username) {
        EmployeeUser user = userRepo.findByUsername(username);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        List<ProductOrder> orders = user.getDispatchingOrders();
        orders.addAll(user.getCheckingOrders());

        return ResponseEntity.ok().body(orders);
    }

    @GetMapping("/product-orders/")
    public ResponseEntity<Iterable<ProductOrder>> getAll() {
        return ResponseEntity.ok().body(prodOrderRepo.findAll());
    }
    
}