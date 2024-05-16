package com.order.purchase.response;

import java.time.LocalDate;
import java.util.List;

import com.order.purchase.entity.Product;

public class PurchaseResponse {
    private String id;
    private List<Product> products;
    private String userName;
    private int quantity;
    private LocalDate purchaseTime;
    private Double totalAmount;
    public PurchaseResponse() {
    }
    public PurchaseResponse(String id, List<Product> products, String userName, int quantity,
    LocalDate purchaseTime, Double totalAmount) {
        this.id = id;
        this.products = products;
        this.userName = userName;
        this.quantity = quantity;
        this.purchaseTime = purchaseTime;
        this.totalAmount = totalAmount;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public List<Product> getProducts() {
        return products;
    }
    public void setProducts(List<Product> products) {
        this.products = products;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public LocalDate getPurchaseTime() {
        return purchaseTime;
    }
    public void setPurchaseTime(LocalDate purchaseTime) {
        this.purchaseTime = purchaseTime;
    }
    public Double getTotalAmount() {
        return totalAmount;
    }
    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }
    @Override
    public String toString() {
        return "PurchaseResponse [id=" + id + ", products=" + products + ", userName=" + userName + ", quantity="
                + quantity + ", purchaseTime=" + purchaseTime + ", totalAmount=" + totalAmount + "]";
    }
    
}
