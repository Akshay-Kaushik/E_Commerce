package com.example.onlinestoreapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class Product_Info extends AppCompatActivity {
    RecyclerView recyclerView;
    ProductAdapter adapter;
    List<Products> productsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product__info);
        productsList=new ArrayList<>();
        recyclerView=findViewById(R.id.rv_categories);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,true));
        adapter=new ProductAdapter(this, productsList);
        recyclerView.setAdapter(adapter);
    }
}