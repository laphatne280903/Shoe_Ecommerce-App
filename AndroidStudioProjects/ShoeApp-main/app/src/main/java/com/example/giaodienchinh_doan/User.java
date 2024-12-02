package com.example.giaodienchinh_doan;

public class User {
    public String id;
    public String email;
    public String token;
    public String displayName;
    public String Img;
    public String phoneNumber;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String email, String displayName, String phoneNumber,String id,String token, String Img) {
        this.id = id;
        this.Img=Img;
        this.email = email;
        this.displayName = displayName;
        this.phoneNumber = phoneNumber;
        this.token= token;
    }

    public String getEmail() {
        return email;
    }
    public String getID() {
        return id;
    }
    public String getToken() {
        return token;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getI() {
        return Img;
    }
    public String setI() {
        return Img;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
