package com.example.giaodienchinh_doan.Model;

import java.io.Serializable;

public class MyCartModel implements Serializable {
    String currentDate;
    String productName;
    String productPrice;
    String quantity;
    String description;
    String rating;
    public Float totalprice;
    String img_url;
    String id;

    public MyCartModel() {}

    public MyCartModel(String currentDate, String productName, String productPrice, String quantity, String description, String rating, Float totalprice, String img_url, String id) {
        this.currentDate = currentDate;
        this.productName = productName;
        this.productPrice = productPrice;
        this.quantity = quantity;
        this.description = description;
        this.rating = rating;
        this.totalprice = totalprice;
        this.img_url = img_url;
        this.id=id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public Float getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(Float totalprice) {
        this.totalprice = totalprice;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
