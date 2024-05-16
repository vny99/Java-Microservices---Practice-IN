package com.order.purchase.builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.order.purchase.entity.Product;
import com.order.purchase.entity.Purchase;
import com.order.purchase.response.PurchaseResponse;

public class PurchaseResponseBuilder {
    private String id;
    private List<Product> products;
    private String userName;
    private int quantity;
    private LocalDate purchaseTime;
    private Double totalAmount;

    public static PurchaseResponseBuilder from (Purchase purchase, List<Product> products){
        return new PurchaseResponseBuilder().withPurchase(purchase).withProducts(products);
    }

    private PurchaseResponseBuilder withPurchase(Purchase purchase) {
        this.id = purchase.getId();
        this.userName = purchase.getUserName();
        this.quantity = purchase.getQuantity();
        this.purchaseTime = purchase.getPurchaseTime();
        this.totalAmount = purchase.getTotalAmount();
        return this;
    }

    public PurchaseResponseBuilder withProducts(List<Product> products){
        this.products = products;
        return this;
    }

    public PurchaseResponse build(){
        return new PurchaseResponse(id, products, userName, quantity, purchaseTime, totalAmount);
    }
}
