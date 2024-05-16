package com.order.purchase.dao;

import java.util.TreeMap;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.order.purchase.entity.Purchase;
import com.order.purchase.repository.PurchaseRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PurchaseDAO {

    @Autowired
    private PurchaseRepository purchaseRepository;

    public Mono<Purchase> savePurchase(Purchase purchase) {
        return purchaseRepository.save(purchase);
    }

    public Flux<Purchase> findByUserName(String userName) {
        return purchaseRepository.findByUserName(userName);
    }

    public Mono<Void> deleteAll() {
       return purchaseRepository.deleteAll();
    }



}
