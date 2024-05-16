package com.order.purchase.entity;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("purchase")
public class Purchase implements Persistable<String> {
    @Id
    @Column("id")
    private String id;
    @Column("productIds")
    private List<String> productIds;
    @Column("userName")
    private String userName;
    @Column("quantity")
    private int quantity;
    @Column("purchaseTime")
    private LocalDate purchaseTime;
    @Column("totalAmount")
    private Double totalAmount;
    @Transient
    private boolean isNew = true;

    public Purchase() {
    }

    public Purchase(String id, List<String> productIds, String userName, int quantity, LocalDate purchaseTime,
            Double totalAmount) {
        this.id = id;
        this.productIds = productIds;
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

    public List<String> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<String> productIds) {
        this.productIds = productIds;
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

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean isNew) {
        this.isNew = isNew;
    }

    @Override
    public String toString() {
        return "Purchase [id=" + id + ", productIds=" + productIds + ", userName=" + userName + ", quantity=" + quantity
                + ", purchaseTime=" + purchaseTime + ", totalAmount=" + totalAmount + ", isNew=" + isNew + "]";
    }

}
