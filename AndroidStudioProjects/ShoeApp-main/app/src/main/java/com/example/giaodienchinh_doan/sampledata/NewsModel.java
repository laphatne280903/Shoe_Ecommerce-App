package com.example.giaodienchinh_doan.sampledata;

import java.io.Serializable;

public class NewsModel implements Serializable{
    String photoTitle;
    String imgSrc;
    String description;
    public NewsModel(){}
    public NewsModel(String photoTitle, String imgSrc, String description) {
        this.photoTitle = photoTitle;
        this.imgSrc = imgSrc;
        this.description = description;
    }

    public String getPhotoTitle() {
        return photoTitle;
    }

    public void setPhotoTitle(String photoTitle) {
        this.photoTitle = photoTitle;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
