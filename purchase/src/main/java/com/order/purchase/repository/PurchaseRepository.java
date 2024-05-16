package com.order.purchase.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import com.order.purchase.entity.Purchase;

import reactor.core.publisher.Flux;

@Repository
public interface PurchaseRepository extends R2dbcRepository<Purchase, String>{

    Flux<Purchase> findByUserName(String userName);

    

    
}
