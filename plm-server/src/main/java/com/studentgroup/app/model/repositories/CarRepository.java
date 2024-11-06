package com.studentgroup.app.model.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.studentgroup.app.model.Car;

@Repository
public interface CarRepository extends CrudRepository<Car, Long>{

    
}