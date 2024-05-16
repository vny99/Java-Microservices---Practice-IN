package com.order.purchase.entity;

public class Stock {

    private String id;
    
    private String productName;

    private int productStock;

    public Stock(String stockId, String productName, int productStock) {
        this.id = stockId;
        this.productName = productName;
        this.productStock = productStock;
    }

    public Stock() {
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductStock() {
        return productStock;
    }

    public void setProductStock(int productStock) {
        this.productStock = productStock;
    }

    @Override
    public String toString() {
        return "Stock [productName=" + productName + ", productStock=" + productStock + "]";
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
