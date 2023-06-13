package com.example.fabset;

import android.graphics.Bitmap;

public class User {
    private String name;
    private String password;
    private String email;
    private String address;
    private String phone;
    private String image;

    public User(String name, String password,String email, String address, String phone) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }
    public User(String name, String password,String email, String address, String phone, String img) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.image = img;
    }
    public User(String name, String password,String img) {
        this.name = name;
        this.password=password;
        this.image = img;
    }
    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }
    public User(){

    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
