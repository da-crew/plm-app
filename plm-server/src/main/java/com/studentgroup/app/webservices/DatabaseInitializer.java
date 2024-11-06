package com.studentgroup.app.webservices;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.studentgroup.app.model.*;
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
    private ReportRepository reportRepo;
    @Autowired
    private TruckRepository truckRepo;

    @PostConstruct
    public void initDatabase() throws Exception {
        userRepo.deleteAll();
        actionLogRepo.deleteAll();
        carRepo.deleteAll();
        prodOrderRepo.deleteAll();
        reportRepo.deleteAll();
        truckRepo.deleteAll();

        EmployeeUser[] users = new EmployeeUser[] {
                new EmployeeUser("aminA22", "Amina", "Ali", "securePass123!", Role.DISPATCHER),
                new EmployeeUser("carlos_H", "Carlos", "Hernandez", "safePwd234#", Role.CHECKER),
                new EmployeeUser("linhNg", "Linh", "Nguyen", "linhPwd345$", Role.ADMIN),
                new EmployeeUser("sofiaG", "Sofia", "Garcia", "garciaPass456@", Role.EXPORTER),
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
        };

        ActionLog[] actionLogs = new ActionLog[] {
            new ActionLog(ZonedDateTime.of(2019, 5, 17, 8, 30, 15, 7895427, ZoneId.systemDefault())),
            new ActionLog(ZonedDateTime.of(2019, 8, 12, 13, 45, 33, 4524524, ZoneId.systemDefault())),
            new ActionLog(ZonedDateTime.of(2019, 11, 25, 19, 20, 50, 7527788, ZoneId.systemDefault())),
        
            new ActionLog(ZonedDateTime.of(2020, 2, 3, 6, 5, 12, 75278645, ZoneId.systemDefault())),
            new ActionLog(ZonedDateTime.of(2020, 7, 22, 14, 45, 50, 987654321, ZoneId.systemDefault())),
            new ActionLog(ZonedDateTime.of(2020, 12, 15, 17, 30, 10, 127934, ZoneId.systemDefault())),
        
            new ActionLog(ZonedDateTime.of(2021, 1, 9, 10, 5, 5, 92792, ZoneId.systemDefault())),
            new ActionLog(ZonedDateTime.of(2021, 3, 10, 10, 5, 5, 7952982, ZoneId.systemDefault())),
            new ActionLog(ZonedDateTime.of(2021, 6, 20, 15, 40, 55, 69420, ZoneId.systemDefault())),
        
            new ActionLog(ZonedDateTime.of(2022, 4, 18, 8, 25, 35, 1642964, ZoneId.systemDefault())),
            new ActionLog(ZonedDateTime.of(2022, 10, 30, 18, 20, 30, 9120642, ZoneId.systemDefault())),
            new ActionLog(ZonedDateTime.of(2022, 12, 7, 21, 45, 0, 4104514, ZoneId.systemDefault())),
        
            new ActionLog(ZonedDateTime.of(2023, 3, 14, 5, 10, 15, 3141592, ZoneId.systemDefault())),
            new ActionLog(ZonedDateTime.of(2023, 8, 5, 14, 30, 25, 05772156, ZoneId.systemDefault())),
            new ActionLog(ZonedDateTime.of(2023, 12, 5, 23, 35, 45, 2718281, ZoneId.systemDefault())),
        
            new ActionLog(ZonedDateTime.of(2024, 2, 14, 6, 15, 25, 778899001, ZoneId.systemDefault())),
            new ActionLog(ZonedDateTime.of(2024, 7, 10, 13, 40, 55, 998877665, ZoneId.systemDefault())),
            new ActionLog(ZonedDateTime.of(2024, 11, 2, 20, 50, 35, 223344556, ZoneId.systemDefault()))
        };

        Car[] cars = new Car[] {

        };

        for (ActionLog log: actionLogs) {
            actionLogRepo.save(log);
        }

        for (EmployeeUser user : users) {
            userRepo.save(user);
        }

    }

}
