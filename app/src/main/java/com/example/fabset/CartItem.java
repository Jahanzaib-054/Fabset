package com.example.fabset;

public class CartItem {

    private String name;
    private String price;
    private String quantity;
    private String size;

    CartItem(){

    }
    CartItem(String name, String price, String quantity, String size){
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.size = size;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
