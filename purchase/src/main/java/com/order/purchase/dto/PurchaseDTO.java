package com.order.purchase.dto;

import java.time.LocalDate;
import java.util.List;

public class PurchaseDTO {
    private List<String> productIds;

    public PurchaseDTO() {
    }

    public PurchaseDTO(List<String> productIds) {
        this.productIds = productIds;
    }

    public List<String> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<String> productIds) {
        this.productIds = productIds;
    }

}
