package com.studentgroup.app.model;

import org.springframework.data.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<EmployeeUser, Long> {
    //EmployeeUser findByToken(String token);
    EmployeeUser findByUsername(String username);
}