package com.studentgroup.app.model.repositories;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.studentgroup.app.model.Truck;


@Repository
public interface TruckRepository extends CrudRepository<Truck, Long>{
    public Truck findByTruckNumber(String truckNumber);
}