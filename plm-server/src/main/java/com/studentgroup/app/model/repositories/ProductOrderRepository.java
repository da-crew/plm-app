package com.studentgroup.app.model.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.studentgroup.app.model.ProductOrder;

@Repository
public interface ProductOrderRepository extends CrudRepository<ProductOrder, Long>{

}

    
