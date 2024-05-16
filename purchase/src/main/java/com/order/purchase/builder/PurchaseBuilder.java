package com.order.purchase.builder;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.order.purchase.dto.PurchaseDTO;
import com.order.purchase.entity.Purchase;

public class PurchaseBuilder {
    private List<String> productIds;
    private String userName;
    private int quantity;
    private LocalDate purchaseTime;
    private Double totalAmount;

    public static PurchaseBuilder from (PurchaseDTO purchaseDTO, String userId, LocalDate purchaseTime, Double totalAmount, int quantity){
        return new PurchaseBuilder().withProductIds(purchaseDTO.getProductIds()).withUserId(userId).withQuantity(quantity).withPurchaseTime(purchaseTime).withTotalAmount(totalAmount);
    }

    public PurchaseBuilder withTotalAmount (Double totalAmount){
        this.totalAmount = totalAmount;
        return this;
    }
    public PurchaseBuilder withPurchaseTime(LocalDate purchaseTime){
        this.purchaseTime = purchaseTime;
        return this;
    }
    public PurchaseBuilder withQuantity(int quantity){
        this.quantity = quantity;
        return this;
    }
    public PurchaseBuilder withUserId(String userId){
        this.userName = userId;
        return this;
    }
    public PurchaseBuilder withProductIds(List<String> productIds){
        this.productIds = productIds;
        return this;
    }
    public Purchase build(){
        return new Purchase(UUID.randomUUID().toString(), productIds, userName, quantity, purchaseTime, totalAmount);
    }
}
