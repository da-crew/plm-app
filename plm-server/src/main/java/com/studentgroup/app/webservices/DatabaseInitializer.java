package com.studentgroup.app.webservices;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.studentgroup.app.model.ActionLog;
import com.studentgroup.app.model.EmployeeUser;
import com.studentgroup.app.model.Role;
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
                new EmployeeUser("yara_F22", "Yara", "Farah", "farahSafe456$", Role.UNKNOWN)
        };

        ActionLog[] actionLogs = new ActionLog[] {
                new ActionLog(new Date(2024 - 1900, 9, 1, 8, 30)), // October 1, 2024, 08:30 AM
                new ActionLog(new Date(2024 - 1900, 9, 5, 14, 45)), // October 5, 2024, 02:45 PM
                new ActionLog(new Date(2024 - 1900, 9, 10, 10, 0)), // October 10, 2024, 10:00 AM
                new ActionLog(new Date(2024 - 1900, 9, 15, 18, 15)), // October 15, 2024, 06:15 PM
                new ActionLog(new Date(2024 - 1900, 9, 20, 23, 30)), // October 20, 2024, 11:30 PM
                new ActionLog(new Date(2024 - 1900, 9, 25, 6, 5)), // October 25, 2024, 06:05 AM
                new ActionLog(new Date(2024 - 1900, 10, 1, 9, 0)), // November 1, 2024, 09:00 AM
                new ActionLog(new Date(2024 - 1900, 10, 3, 12, 20)), // November 3, 2024, 12:20 PM
                new ActionLog(new Date(2024 - 1900, 10, 6, 16, 0)) // November 6, 2024, 04:00 PM
        };

        for (EmployeeUser user : users) {
            userRepo.save(user);
        }

    }

}
