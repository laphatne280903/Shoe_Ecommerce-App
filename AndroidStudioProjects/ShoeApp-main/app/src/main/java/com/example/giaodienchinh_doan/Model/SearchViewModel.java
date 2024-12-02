package com.example.giaodienchinh_doan.Model;

import java.io.Serializable;

public class SearchViewModel implements Serializable {
    private String name;
    private String img_url;
    private String description;
    private String rating;
    private Float price;

    public SearchViewModel(){}

    public SearchViewModel(String name, String img_url, String description, String rating, Float price) {
        this.name = name;
        this.img_url = img_url;
        this.description = description;
        this.rating = rating;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
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

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }
}
