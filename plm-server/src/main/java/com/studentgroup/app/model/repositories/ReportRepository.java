package com.studentgroup.app.model.repositories;

import org.springframework.data.repository.*;
import org.springframework.stereotype.Repository;

import com.studentgroup.app.model.Report;

@Repository
public interface ReportRepository extends CrudRepository<Report, Long> {
    
}