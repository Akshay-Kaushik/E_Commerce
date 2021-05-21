package com.example.onlinestoreapp;

import java.util.ArrayList;
import java.util.List;

public class Variables {
    public static String name;
    public static int cart_count;
    //product_info=[product_id, product_name, quantity, amount/qty, image_view]
    //key="Product Title"
    //value=[prod_id,quantity, description, price, image]
    public static String product_info[];
    public static List<Categories> global_category_list=new ArrayList<>();
    public static List<Products> global_products_list=new ArrayList<>();
    public static int condition;
    public static int global_cart_count=0;
    public static double price[];
    public static int global_order_count=0;
}
