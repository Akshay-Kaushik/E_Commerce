package com.example.onlinestoreapp;

public class Products {
    private String key;
    private String category;
    private String title;
    private String description;
    private double price;
    private String long_description;
    private String imageURL;
    private String Quantity;


    public Products(String key,String category, String title,String description, String long_description, double price, String imageURL, String Quantity) {
        this.key=key;
        this.long_description=long_description;
        this.category=category;
        this.title = title;
        this.description = description;
        this.price = price;
        this.imageURL = imageURL;
        this.Quantity=Quantity;
    }

    public String getQuantity() { return Quantity; }

    public String getLong_description() { return long_description; }

    public String getKey() {return key;}

    public String getCategory() {return category; }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public String getImage() {
        return imageURL;
    }
}

