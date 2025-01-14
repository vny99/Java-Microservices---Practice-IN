package com.order.purchase.entity;

public class ProductCategory {
    private int id;

    private String type;

    public ProductCategory() {
    }

    public ProductCategory(int id, String type) {
        this.id = id;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ProductCategory [id=" + id + ", type=" + type + "]";
    }

}
