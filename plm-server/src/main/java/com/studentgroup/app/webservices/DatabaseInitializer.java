package com.studentgroup.app.webservices;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.studentgroup.app.model.*;
import com.studentgroup.app.model.enums.ProductOrderStatus;
import com.studentgroup.app.model.enums.Role;
import com.studentgroup.app.model.repositories.*;
import jakarta.annotation.PostConstruct;

@Component
public class DatabaseInitializer {

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private ActionLogRepository actionLogRepo;
    @Autowired
    private CarRepository carRepo;
    @Autowired
    private ProductOrderRepository prodOrderRepo;
    @Autowired
    private TruckRepository truckRepo;
    @Autowired 
    private ReportRepository reportRepo;

    //please find a way to turn this off when we're deploying this thing on Google Cloud.
    @PostConstruct
    public void initDatabase() throws Exception {
        
        /**/
        for (ProductOrder prod : prodOrderRepo.findAll()) {
            prod.setChecker(null);
            prod.setDispatcher(null);
            prodOrderRepo.save(prod);
        }

        for (ActionLog log : actionLogRepo.findAll()) {
            log.setEmployee(null);
            log.setProductOrder(null);
            actionLogRepo.save(log);
        }

        for (Car car : carRepo.findAll()) {
            car.setTruck(null);
            car.setProductOrder(null);
            carRepo.save(car);
        }

        for (Report report : reportRepo.findAll()) {
            report.setCar(null);
            reportRepo.save(report);
        }

        actionLogRepo.deleteAll();
        prodOrderRepo.deleteAll();
        carRepo.deleteAll();
        truckRepo.deleteAll();
        userRepo.deleteAll();
        reportRepo.deleteAll();

        // mock data
        EmployeeUser[] users = new EmployeeUser[] {
                new EmployeeUser("aminA22", "Amina", "Ali", "securePass123!", Role.DISPATCHER),  
                new EmployeeUser("carlos_H", "Carlos", "Hernandez", "safePwd234#", Role.CHECKER),
                new EmployeeUser("linhNg", "Linh", "Nguyen", "linhPwd345$", Role.ADMIN),         
                new EmployeeUser("sofiaG", "Sofia", "Garcia", "garciaPass456@", Role.EXPORTER),
                /**
                new EmployeeUser("anwark12", "Anwar", "Khan", "unique567&", Role.UNKNOWN),
                new EmployeeUser("fatimaB", "Fatima", "Bakshi", "fatima678*", Role.DISPATCHER),
                new EmployeeUser("joH56", "Joseph", "Ho", "joe!pass789", Role.CHECKER),
                new EmployeeUser("miriamB_", "Miriam", "Blake", "mirPwd890#", Role.ADMIN),
                new EmployeeUser("kunle_A", "Kunle", "Adebayo", "adebPass012!", Role.EXPORTER),
                new EmployeeUser("maya_R13", "Maya", "Rai", "may@Pass1234", Role.UNKNOWN),
                new EmployeeUser("zhangL", "Lei", "Zhang", "zhanPass456$", Role.DISPATCHER),
                new EmployeeUser("amal23K", "Amal", "Kamal", "kamalSafe567#", Role.CHECKER),
                new EmployeeUser("dianeT", "Diane", "Trent", "dian@pass678", Role.ADMIN),
                new EmployeeUser("thaboM", "Thabo", "Mokoena", "thaboPwd789!", Role.EXPORTER),
                new EmployeeUser("keiko_S", "Keiko", "Suzuki", "suzPass890*", Role.UNKNOWN),
                new EmployeeUser("priya34", "Priya", "Nair", "pnairSafe012$", Role.DISPATCHER),
                new EmployeeUser("omar_L55", "Omar", "Liu", "omarPass345!", Role.CHECKER),
                new EmployeeUser("brianN", "Brian", "Nkwanzi", "nkwPass234#", Role.ADMIN),
                new EmployeeUser("esme_R", "Esme", "Rogers", "esmeUnique678*", Role.EXPORTER),
                new EmployeeUser("yara_F22", "Yara", "Farah", "farahSafe456$", Role.UNKNOWN),
                //*/
        };

        ActionLog[] actionLogs = new ActionLog[] {
                new ActionLog(ZonedDateTime.of(2019, 5, 17, 8, 30, 15, 7895427, ZoneId.systemDefault())),
                new ActionLog(ZonedDateTime.of(2019, 8, 12, 13, 45, 33, 4524524, ZoneId.systemDefault())),
                new ActionLog(ZonedDateTime.of(2019, 11, 25, 19, 20, 50, 7527788, ZoneId.systemDefault())),

                new ActionLog(ZonedDateTime.of(2020, 2, 3, 6, 5, 12, 75278645, ZoneId.systemDefault())),
                new ActionLog(ZonedDateTime.of(2020, 7, 22, 14, 45, 50, 987654321, ZoneId.systemDefault())),
                new ActionLog(ZonedDateTime.of(2020, 12, 15, 17, 30, 10, 127934, ZoneId.systemDefault())),
        };

        String[] actionTexts = {
            "Product packed and ready for shipment.",
            "Payment has been successfully processed.",
            "Product has been shipped to the customer.",
            "Customer confirmed delivery of the product.",
            "Order has been cancelled by the customer.",
            "Order marked as completed in system."
        };

        for (int i = 0; i < actionTexts.length; i++) {
            actionLogs[i].setActionText(actionTexts[i]);
        }

        Report[] reports = new Report[] {
            new Report("Minor scratches on the front bumper and left door.", "dam1.png"),
            new Report("Broken tail light and dent on rear bumper.", "dam2.png"),
            new Report("Windshield has a small crack near the passenger side.", "dam3.png"),
            new Report("Front left tire worn out; needs replacement.", "dam4.png"),
            new Report("Dashboard display malfunction; intermittent screen blackout.", "dam5.png"),
            new Report("A large, perfect circular hole has pierced through the car, as if something passed clean through it.", "dam6.png"),
            new Report("Unexplained damage on the car's roof; as if something heavy had been dragged across it.", "dam7.png")
        };

        Car[] cars = new Car[] {
                new Car("Toyota Corolla"),
                new Car("Honda Civic"),
                new Car("Ford Focus"),
                new Car("Chevrolet Malibu"),
                new Car("BMW 3 Series"),
                new Car("Mercedes-Benz A-Class"),
                new Car("Audi A4"),
                new Car("Nissan Altima"),
                new Car("Hyundai Elantra"),
                new Car("Volkswagen Golf"),
                new Car("Mazda CX-5"),
                new Car("Kia Optima"),
                new Car("Subaru Impreza"),
                new Car("Toyota Camry"),
                new Car("Honda Accord"),
                new Car("Tesla Model 3")
        };

        ProductOrder[] productOrders = new ProductOrder[] {
                new ProductOrder("BL2023045",
                        ZonedDateTime.of(2019, 4, 15, 10, 30, 0, 123456789, ZoneId.systemDefault()),
                        "Vessel Phoenix", "VY201", "Cosignee A Ltd.", "imgUrl1.jpg", ProductOrderStatus.CHECKING),
                new ProductOrder("BL2023089",
                        ZonedDateTime.of(2020, 6, 22, 14, 45, 30, 987654321, ZoneId.systemDefault()),
                        "Ocean Star", "VY305", "Cosignee B Ltd.", "imgUrl2.jpg", ProductOrderStatus.EXPORTING),
                new ProductOrder("BL2023112",
                        ZonedDateTime.of(2021, 11, 30, 19, 20, 15, 223344556, ZoneId.systemDefault()),
                        "Seafarer", "VY212", "Cosignee C Corp.", "imgUrl3.jpg", ProductOrderStatus.FINISHED),
                new ProductOrder("BL2024011",
                        ZonedDateTime.of(2022, 1, 15, 8, 10, 45, 112233445, ZoneId.systemDefault()),
                        "Blue Horizon", "VY512", "Cosignee D LLC", "imgUrl4.jpg", ProductOrderStatus.REPORTED),
                new ProductOrder("BL2024053",
                        ZonedDateTime.of(2023, 3, 25, 11, 50, 5, 334455667, ZoneId.systemDefault()),
                        "Mariner X", "VY304", "Cosignee E Ltd.", "imgUrl5.jpg", ProductOrderStatus.CHECKING),
                new ProductOrder("BL2024090",
                        ZonedDateTime.of(2024, 8, 18, 17, 35, 25, 556677889, ZoneId.systemDefault()),
                        "Navigator", "VY451", "Cosignee F Corp.", "imgUrl6.jpg", ProductOrderStatus.EXPORTING)
        };

        Truck[] trucks = new Truck[] {
                new Truck("TRK-1023"),
                new Truck("TRK-2045"),
                new Truck("TRK-3089"),
                new Truck("TRK-4120"),
                new Truck("TRK-5234"),
                new Truck("TRK-6348")
        };

        //pretend that this is the data we've had beforehand

        userRepo.save(users[0]);
        userRepo.save(users[1]);
        truckRepo.save(trucks[0]);
        truckRepo.save(trucks[1]);

        //situation #1: a dispatcher(user[0]) creates a new product order(productOrders[0]) and assign it to a checker(users[1])

        //trucks[0] is being loaded with cars[0] and cars[1]
        trucks[0].addCar(cars[0]);
        trucks[0].addCar(cars[1]);
        truckRepo.save(trucks[0]);//we need to save the truck entities first

        //trucks[1] is being loaded with cars[2] and car[3]
        trucks[1].addCar(cars[2]);
        trucks[1].addCar(cars[3]);
        truckRepo.save(trucks[1]);

        productOrders[0].addCar(cars[0]);
        productOrders[0].addCar(cars[1]);
        productOrders[0].addCar(cars[2]);
        productOrders[0].addCar(cars[3]);
        users[0].assignAsDispatcher(productOrders[0]);
        users[1].assignAsChecker(productOrders[0]);

        
        ActionLog actionLog = new ActionLog(String.format("Create product order with BL Number %s.", productOrders[0].getBLNumber()));
        productOrders[0].addActionLog(actionLog, users[0]);

        actionLog = new ActionLog(String.format("Assign product order with BL Number %s to %s for checking.", productOrders[0].getBLNumber(), users[1].getUsername()));
        productOrders[0].addActionLog(actionLog, users[0]);
        //save the entities
        prodOrderRepo.save(productOrders[0]);

        carRepo.save(cars[0]);
        carRepo.save(cars[1]);
        carRepo.save(cars[2]);
        carRepo.save(cars[3]);

        userRepo.save(users[0]);
        //end of situation #1


    }
}
