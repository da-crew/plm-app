package com.studentgroup.app.model.repositories;

import org.springframework.data.repository.*;
import org.springframework.stereotype.Repository;

import com.studentgroup.app.model.ActionLog;

@Repository
public interface ActionLogRepository extends CrudRepository<ActionLog, Long> {
    
}