package com.example.onlinestoreapp;

public class Categories {
    private String category;
    private String imageURL;

    public Categories(String category, String imageURL) {
        this.category = category;
        this.imageURL= imageURL;
    }

    public String getCategory() {
        return category;
    }
    public String getImageURL(){
        return imageURL;
    }
}

