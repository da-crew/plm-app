package com.studentgroup.app.webservices;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studentgroup.app.Misc;
import com.studentgroup.app.model.*;
import com.studentgroup.app.model.enums.ProductOrderStatus;
import com.studentgroup.app.model.enums.Role;
import com.studentgroup.app.model.repositories.ActionLogRepository;
import com.studentgroup.app.model.repositories.CarRepository;
import com.studentgroup.app.model.repositories.ProductOrderRepository;
import com.studentgroup.app.model.repositories.ReportRepository;
import com.studentgroup.app.model.repositories.TruckRepository;
import com.studentgroup.app.model.repositories.UserRepository;
import com.studentgroup.app.service.FileStorageService;
import com.studentgroup.app.webservices.authorization.*;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;

@RestController
public class ProductOrderController {

    @Autowired
    UserRepository userRepo;

    @Autowired
    ProductOrderRepository prodOrderRepo;

    @Autowired
    CarRepository carRepo;

    @Autowired
    ReportRepository reportRepo;

    @Autowired
    AuthorizationManager authMan;

    @Autowired
    ActionLogRepository actionLogRepo;

    @Autowired
    FileStorageService storageService;

    @Autowired
    TruckRepository truckRepo;

    @Autowired
    ObjectMapper mapper;
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
     Request Body: {
     
     caller: {
     username: String,
     password: String
     }
     
     }
     Returns:
     BAD_REQUEST
     FORBIDDEN
     NOT_FOUND
     NOT_ACCEPTABLE
     OK
     
     */
    @PostMapping("/product-orders/{blNumber}/return")
    public ResponseEntity<String> returnProductOrder(@PathVariable String blNumber, @RequestBody JsonNode json) throws Exception {
        AuthorizationResult authRes = authMan.authorizeFromJson(json.get("caller"), Role.ADMIN, Role.EXPORTER, Role.CHECKER, Role.DISPATCHER);
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
        AuthorizationResult authRes = authMan.authorizeFromJson(json.get("caller"), Role.ADMIN, Role.EXPORTER, Role.CHECKER, Role.DISPATCHER);
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

        if ((caller.getRole() == Role.DISPATCHER || caller.getRole() == Role.ADMIN) && productOrder.getStatusName() == ProductOrderStatus.REPORTED) {
            productOrder.setStatusName(ProductOrderStatus.CHECKING);
            actionLog = new ActionLog(String.format(actionFormatMessage, ProductOrderStatus.CHECKING));
        } else if ((caller.getRole() == Role.CHECKER || caller.getRole() == Role.ADMIN) && productOrder.getStatusName() == ProductOrderStatus.CHECKING) {
            productOrder.setStatusName(ProductOrderStatus.EXPORTING);
            actionLog = new ActionLog(String.format(actionFormatMessage, ProductOrderStatus.EXPORTING));
        } else if ((caller.getRole() == Role.EXPORTER || caller.getRole() == Role.ADMIN) && productOrder.getStatusName() == ProductOrderStatus.EXPORTING) {
            productOrder.setStatusName(ProductOrderStatus.FINISHED);
            actionLog = new ActionLog(String.format(actionFormatMessage, ProductOrderStatus.FINISHED));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("you are not allowed to do this!");
        }


        productOrder.addActionLog(actionLog, caller);
        prodOrderRepo.save(productOrder);

        return ResponseEntity.status(HttpStatus.OK).body("Forwarded successfully");
    }

    

    /*
    NOT TESTED
    Request Body: {
        caller: {
            username: String,
            password: String
        }

        productOrder: [see ProductOrderInfo.fromJsonNode]

        checker: String(can be left null if you want to leave it unchanged)
        dispatcher: String(can be left null if you want to leave it unchanged)

    }
     */
    @PostMapping("/product-orders/{blNumber}/edit")
    public ResponseEntity<String> editProductOrder(@PathVariable String blNumber, @RequestBody JsonNode json) throws Exception {
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

        ProductOrder productOrder = prodOrderRepo.findByBLNumber(blNumber);
        if (productOrder == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("product order not found!");
        }

        ProductOrderInfo productOrderInfo = ProductOrderInfo.fromJsonNode(json.get("productOrder"));
        if (productOrderInfo == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("couldn't parse or find productOrder!");
        }

        EmployeeUser newDispatcher = null;
        EmployeeUser newChecker = null;
        String newCheckerUsername = Misc.jsonToString(json, "checker");
        String newDispatcherUsername = Misc.jsonToString(json, "dispatcher");

        if (newCheckerUsername != null) {
            newChecker = userRepo.findByUsername(newCheckerUsername);
            if (newChecker == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("couldn't find checker!"); 
        }

        if (newDispatcherUsername != null) {
            newDispatcher = userRepo.findByUsername(newDispatcherUsername);
            if (newDispatcher == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("couldn't find dispatcher!"); 
        }

        EmployeeUser caller = authRes.getUser();


        if (newChecker != null && !productOrder.getChecker().getUsername().equals(newCheckerUsername)) {
            if (newChecker.getRole() != Role.ADMIN && newChecker.getRole() != Role.CHECKER)
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("assigned checker must be a checker or an admin!");

            productOrder.removeChecker();
            newChecker.assignAsChecker(productOrder);
            ActionLog actionLog = new ActionLog("Reassign to " + newChecker.getUsername() + " as a checker");
            productOrder.addActionLog(actionLog, caller);
        }

        if (newDispatcher != null && !productOrder.getDispatcher().getUsername().equals(newDispatcherUsername)) {
            if (newDispatcher.getRole() != Role.ADMIN && newDispatcher.getRole() != Role.DISPATCHER)
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("assigned dispatcher must be a dispatcher or an admin!");
            productOrder.removeDispatcher();
            newDispatcher.assignAsDispatcher(productOrder);
            ActionLog actionLog = new ActionLog("Reassign to " + newDispatcher.getUsername() + " as a dispatcher");
            productOrder.addActionLog(actionLog, caller);
        }

        List<ActionLog> actionLogs = new ArrayList<>();

        if (productOrderInfo.getBLNumber() != productOrder.getBLNumber()) {
            if (prodOrderRepo.findByBLNumber(productOrderInfo.getBLNumber()) != null)
                return ResponseEntity.status(HttpStatus.CONFLICT).body("BL Number already exists!"); 
            actionLogs.add(new ActionLog(String.format("Change BL Number from %s to %s", productOrder.getBLNumber(), productOrderInfo.getBLNumber())));
            productOrder.setBLNumber(productOrderInfo.getBLNumber());
        }

        if (productOrderInfo.getCosigneeName() != productOrder.getCosigneeName()) {
            actionLogs.add(new ActionLog(String.format("Change Co-signee Name from %s to %s", productOrder.getCosigneeName(), productOrderInfo.getCosigneeName())));
            productOrder.setCosigneeName(productOrderInfo.getCosigneeName());
        }

        if (!productOrderInfo.getOrderDate().equals(productOrder.getOrderDate())) {
            actionLogs.add(new ActionLog(String.format("Change Order Date from %s to %s", productOrder.getOrderDate(), productOrderInfo.getOrderDate())));
            productOrder.setOrderDate(productOrderInfo.getOrderDate());
        }

        if (productOrderInfo.getVesselName() != productOrder.getVesselName()) {
            actionLogs.add(new ActionLog(String.format("Change Vessel Name from %s to %s", productOrder.getVesselName(), productOrderInfo.getVesselName())));
            productOrder.setVesselName(productOrderInfo.getVesselName());
        }

        if (productOrderInfo.getVoyNumber() != productOrder.getVoyNumber()) {
            actionLogs.add(new ActionLog(String.format("Change Voyage Number from %s to %s", productOrder.getVoyNumber(), productOrderInfo.getVoyNumber())));
            productOrder.setVoyNumber(productOrderInfo.getVoyNumber());
        }

        if (productOrderInfo.getMarkAndNumsText() != productOrder.getMarkAndNumsText()) {
            actionLogs.add(new ActionLog("Change Mark & Nums"));
            productOrder.setMarkAndNumsText(productOrderInfo.getMarkAndNumsText());
        }

        if (productOrderInfo.getPackagesText() != productOrder.getPackagesText()) {
            actionLogs.add(new ActionLog("Change Packages column"));
            productOrder.setPackagesText(productOrderInfo.getPackagesText());
        }

        if (productOrderInfo.getDescription() != productOrder.getDescription()) {
            actionLogs.add(new ActionLog("Change Description"));
            productOrder.setDescription(productOrderInfo.getDescription());
        }

        if (productOrderInfo.getRemarks() != productOrder.getRemarks()) {
            actionLogs.add(new ActionLog("Change Remarks"));
            productOrder.setRemarks(productOrderInfo.getRemarks());
        }

        for (ActionLog actionLog : actionLogs) {
            productOrder.addActionLog(actionLog, caller);
        }

        prodOrderRepo.save(productOrder);

        return ResponseEntity.ok().body("Updated");
    }

    /*
    NOT TESTED
    Request Body: {
        caller: {
            username: String,
            password: String
        }
    }
     */
    @DeleteMapping("product-orders/{blnumber}")
    public ResponseEntity<String> deleteProductOrder(@PathVariable String blNumber, @RequestBody JsonNode json) throws Exception {
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

        ProductOrder productOrder = prodOrderRepo.findByBLNumber(blNumber);
        if (productOrder == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("product order not found!");
        }

        EmployeeUser checker = productOrder.getChecker();
        EmployeeUser dispatcher = productOrder.getDispatcher();

        for (ActionLog actionLog : productOrder.getActionLogs()) {
            actionLog.setEmployee(null);
            actionLog.setProductOrder(null);
        }

        actionLogRepo.deleteAll(productOrder.getActionLogs());

        for (Car car : productOrder.getCars()) {
            car.setProductOrder(null);
        }

        if (checker != null) {
            productOrder.removeChecker();
        }
        if (dispatcher != null) {
            productOrder.removeDispatcher();
        }

        storageService.deleteFile(productOrder.getWharfReceiptImgUrl());
        prodOrderRepo.delete(productOrder);

        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("in construction...");
    }

    /*NOT TESTED
    this one accepts form-data instead of json,
    and hasn't been tested yet.
    */
    @PostMapping(path = "/product-orders/{blNumber}/cars/{carId}/damage-report", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> addDamageReport(
    @PathVariable String blNumber, 
    @PathVariable Long carId, 
    @RequestPart("report") String reportText,
    @RequestPart("caller") UserCreds callerCreds,
    @RequestPart("image") MultipartFile imageFile) throws Exception {
        AuthorizationResult authRes = authMan.authorizeFromUserCreds(callerCreds, Role.ADMIN, Role.CHECKER);
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


        //get product order and car
        ProductOrder productOrder = prodOrderRepo.findByBLNumber(blNumber);
        if (productOrder == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("product order not found!");
        }

        Car foundCar = null;
        for (Car car : productOrder.getCars()) {
            if (car.getId() == carId) {
                foundCar = car;
                break;
            }
        }
        if (foundCar == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("car not found!");
        }
        
        EmployeeUser caller = authRes.getUser();
        try {
            ImageFile savedFile = storageService.store(imageFile);

            Report report = new Report();
            report.setDamageReportText(reportText.isEmpty() ? "No detail." : reportText);
            report.setImgUrl(savedFile.getId());

            foundCar.addReport(report);

            //save report, car, user
            
            ActionLog actionLog = new ActionLog(String.format("Add report text to car ID %s.", carId));
            productOrder.addActionLog(actionLog, caller);
            prodOrderRepo.save(productOrder);

            return ResponseEntity.status(HttpStatus.CREATED).body(String.format("Add report text to car ID %s.", carId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file");
        }
    }

    //NOT TESTED
    @DeleteMapping("/product-orders/{blNumber}/cars/{carId}/damage-report/{reportId}")
    public ResponseEntity<String> deleteDamageReport(    
    @PathVariable String blNumber, 
    @PathVariable Long carId, 
    @PathVariable Long reportId,
    @RequestBody JsonNode json) throws Exception {
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

        ProductOrder productOrder = prodOrderRepo.findByBLNumber(blNumber);
        if (productOrder == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("product order not found!");
        }
        Car foundCar = null;
        for (Car car : productOrder.getCars()) {
            if (car.getId() == carId) {
                foundCar = car;
                break;
            }
        }
        if (foundCar == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("car not found!");
        }

        Report foundReport = null;
        for (Report report : foundCar.getReports()) {
            if (report.getId() == reportId) {
                foundReport = report;
                break;
            }
        }
        if (foundReport == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("report not found!");
        }

        foundReport.setCar(null);
        storageService.deleteFile(foundReport.getImgUrl());
        reportRepo.delete(foundReport);

        ActionLog actionLog = new ActionLog("Delete damage report ");
        productOrder.addActionLog(actionLog, authRes.getUser());
        prodOrderRepo.save(productOrder);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("removed report");
    }


    /*NOT TESTED
    Request Body: {
        caller: {
            username: String,
            password: String
        },

        truckNumber: String,
        carModel: String
    }
     */
    @PostMapping("/product-orders/{blNumber}/cars/add")
    public ResponseEntity<String> addLoadDetails(@PathVariable String blNumber, @RequestBody JsonNode json) throws Exception {
        AuthorizationResult authRes = authMan.authorizeFromJson(json.get("caller"), Role.ADMIN, Role.CHECKER);
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

        
        String modelName = Misc.jsonToString(json, "carModel");
        if (modelName.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("car model name cannot be empty!");
        }

        ProductOrder productOrder = prodOrderRepo.findByBLNumber(blNumber);
        if (productOrder == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("product order not found!");
        }


        Truck truck = truckRepo.findByTruckNumber(Misc.jsonToString(json, "truckNumber"));
        if (truck == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("truck not found!");
        }
        if (truck.getCars().size() >= 8) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("truck is full!");
        }
        
        Car newCar = new Car(modelName);
        newCar.setProductOrder(productOrder);
        newCar.setTruck(truck);
        carRepo.save(newCar);

        return ResponseEntity.status(HttpStatus.CREATED).body("successfully added car");
    }

    /*NOT TESTED
    Request Body: {
        caller: {
            username: String,
            password: String
        }
    }
     */
    @DeleteMapping("/product-orders/{blNumber}/cars/{carId}")
    public ResponseEntity<String> removeCar(@PathVariable String blNumber, @PathVariable Long carId, @RequestBody JsonNode json) throws Exception{
        AuthorizationResult authRes = authMan.authorizeFromJson(json.get("caller"), Role.ADMIN, Role.CHECKER);
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

        Car foundCar = null;
        for (Car car : productOrder.getCars()) {
            if (car.getId() == carId) {
                foundCar = car;
                break;
            }
        }
        if (foundCar == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("car not found!");
        }

        foundCar.setProductOrder(null);
        foundCar.setTruck(null);
        carRepo.delete(foundCar);

        ActionLog actionLog = new ActionLog("Remove car");
        productOrder.addActionLog(actionLog, authRes.getUser());
        prodOrderRepo.save(productOrder);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("deleted car");
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

    @GetMapping("/product-orders")
    public ResponseEntity<Iterable<ProductOrder>> getAll() {
        return ResponseEntity.ok().body(prodOrderRepo.findAll());
    }
    
}
//make it stop