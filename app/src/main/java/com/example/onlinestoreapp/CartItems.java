package com.example.onlinestoreapp;

import android.widget.TextView;

public class CartItems {
    private String title;
    private String quantity;
    private String ImageURL;
    private String Price;
    private String key;
    private String Category;
    public CartItems(String title, String quantity, String imageURL, String Price,String key,String Category) {
        this.title = title;
        this.quantity = quantity;
        this.ImageURL = imageURL;
        this.Price=Price;
        this.key=key;
        this.Category=Category;
    }

    public String getCategory() { return Category; }

    public String getKey() { return key; }

    public String getTitle() {
        return title;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public String getPrice() {
        return Price;
    }
}

