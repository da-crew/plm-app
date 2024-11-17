package com.studentgroup.app.model.repositories;

import org.springframework.data.repository.*;
import org.springframework.stereotype.Repository;

import com.studentgroup.app.model.EmployeeUser;
import java.util.List;
import com.studentgroup.app.model.enums.Role;


@Repository
public interface UserRepository extends CrudRepository<EmployeeUser, Long> {
    EmployeeUser findByUsername(String username);
    List<EmployeeUser> findByRole(Role role);
}