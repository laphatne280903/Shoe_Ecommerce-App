package com.example.giaodienchinh_doan.Model;

public class BrandsModel {
    String img_url, name , brand;
    public BrandsModel() {
    }

    public BrandsModel(String img_url, String name, String brand) {
        this.img_url = img_url;
        this.name = name;
        this.brand = brand;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }
}
