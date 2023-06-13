package com.example.fabset;

import android.graphics.Bitmap;

public class Product {
    private String name;
    private String price;
    private String category;
    private String subcategory;
    private String description;
    private String tag;
    private String image;

    public Product( String title, String price, String cat, String subcat, String desc, String tg, String img) {
        this.name = title;
        this.price = price;
        this.category = cat;
        this.subcategory = subcat;
        this.description = desc;
        this.tag = tg;
        this.image = img;
    }

    public Product(String title, String img){
        this.name = title;
        this.image = img;
    }
    public Product(){
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
