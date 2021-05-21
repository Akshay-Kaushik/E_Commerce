package com.example.onlinestoreapp;

public class OrderItems {
    private String order_no;
    private String key;
    private String category;
    private String name;
    private String ImageURl;

    public OrderItems(String order_no, String key, String category, String name, String imageURl) {
        this.order_no = order_no;
        this.key = key;
        this.category = category;
        this.name = name;
        ImageURl = imageURl;
    }

    public String getOrder_no() {
        return order_no;
    }

    public String getKey() {
        return key;
    }

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public String getImageURl() {
        return ImageURl;
    }
}

