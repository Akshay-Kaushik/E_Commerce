package com.example.onlinestoreapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Products_Details<productsList> extends AppCompatActivity {
    private FirebaseAuth mAuth;
    AlertDialog.Builder builder;
    FirebaseDatabase database;
    DatabaseReference myRef, myRef_Data,myRef_Cart;
    Map<String, String> value;
    RecyclerView recyclerView;
    String category;
    String name, description, ImageURL, Price,long_description,quantity;
    ProductAdapter adapter;
    List<Products> productsList;
    EditText editText;
    TextView textView;
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Products_Details.this, ProductsPage.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slidein_left,R.anim.slideout_right);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_details);
        ImageButton ImBtn1=findViewById(R.id.cart_icon);
        ImBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Products_Details.this,Cart.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slidein_right,R.anim.slideout_left);
            }
        });
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        View header = navigationView.getHeaderView(0);
        TextView textUsername = header.findViewById(R.id.user_full_name_nav_header);
        textUsername.setText(Variables.name);
        Bundle bundle = getIntent().getExtras();
        textView=findViewById(R.id.cart_number);
        textView.setText(String.valueOf(Variables.global_cart_count));
        Log.d("value",String.valueOf(Variables.global_cart_count));
        category = bundle.getString("category");
        productsList=new ArrayList<>();
        recyclerView = findViewById(R.id.rv_category_specific);
        create_RecyclerView();
        Log.d("category", category);
        mAuth = FirebaseAuth.getInstance();
        ImageButton ImBtn = findViewById(R.id.navicon);
        ImBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                drawerLayout.closeDrawer(GravityCompat.START);
                switch (id) {
                    case R.id.nav_logout:
                        builder = new AlertDialog.Builder(Products_Details.this);
                        builder.setMessage("Are you sure you want to log out?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        mAuth.signOut();
                                        Intent intent = new Intent(Products_Details.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                        startActivity(intent);
                                        finishAffinity();
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //  Action for 'NO' Button
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        //Setting the title manually
                        alert.setTitle("Log out?");
                        alert.show();
                        return true;
                    case R.id.nav_home:
                        Intent intent = new Intent(Products_Details.this, LandingPage.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slidein_left,R.anim.slideout_right);
                        return true;
                    case R.id.nav_myprofile:
                        Intent intent1 = new Intent(Products_Details.this, MyProfile.class);
                        startActivity(intent1);
                        overridePendingTransition(R.anim.slidein_right,R.anim.slideout_left);
                        return true;
                    //Intent to MyProfile Page
                    case R.id.nav_cart:
                        Intent intent2 = new Intent(Products_Details.this, Cart.class);
                        startActivity(intent2);
                        overridePendingTransition(R.anim.slidein_right,R.anim.slideout_left);
                        return true;
                    //Intent to cart
                    case R.id.nav_orders:
                        Intent intent3 = new Intent(Products_Details.this, MyOrders.class);
                        startActivity(intent3);
                        overridePendingTransition(R.anim.slidein_right,R.anim.slideout_left);
                        return true;
                    //Intent to My Orders Page
                    case R.id.nav_sell:
                        Intent intent4 = new Intent(Products_Details.this, Sell.class);
                        startActivity(intent4);
                        overridePendingTransition(R.anim.slidein_right,R.anim.slideout_left);
                        return true;
                    //Intent to Selling Page
                    case R.id.nav_settings:
                        return true;
                        //Intent to settings Page
                    case R.id.nav_share:
                        Intent myIntent=new Intent(Intent.ACTION_SEND);
                        myIntent.setType("text/plain");
                        String shareBody="You can download the XFORCE E-Commerce App from: https://drive.google.com/drive/folders/1Iy8DxuqBeI0ns6WbDnac34NUXPVETu_8?usp=sharing ";
                        String shareSub="Hey!";
                        myIntent.putExtra(Intent.EXTRA_SUBJECT,shareSub);
                        myIntent.putExtra(Intent.EXTRA_TEXT,shareBody);
                        startActivity(Intent.createChooser(myIntent,"Share Via"));
                        return true;
                        //Function to share the link of the app
                    case R.id.nav_rate:
                        return true;
                        //Function to rate the app
                    default:
                        return true;
                }
            }
        });
        editText = findViewById(R.id.search_editText);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d("Char",charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d("Hello", editable.toString());
                filter(editable.toString());
            }
        });

    }

    public void create_RecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //Fetch Data From Real time database
        Log.d("recycler", String.valueOf(recyclerView.getId()));
        fetch_data_realtime();

    }
    //Check category

    public void fetch_data_realtime() {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Products").child(category).child("Items");
        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                value = (Map<String, String>) dataSnapshot.getValue();
                Log.d("TAG", "Value is: " + value.keySet());
                for (String str : value.keySet()) {
                    add_to_products_list(str);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });

    }

    public void add_to_products_list(String str) {
        myRef_Data = myRef.child(str);
        Log.d("Child",str);
        myRef_Data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                value = (Map<String, String>) snapshot.getValue();
                name = value.get("Name");
                description = value.get("Description");
                long_description=value.get("Description");
                Price = value.get("Price");
                ImageURL = value.get("ImageURL");
                quantity= value.get("Quantity");
                productsList.add(
                        new Products(
                                str,
                                category,
                                name,
                                description,
                                long_description,
                                Double.parseDouble(Price),
                                ImageURL,
                                quantity
                        )
                );
                adapter = new ProductAdapter(Products_Details.this, productsList);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        myRef_Data.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String value_test = snapshot.getValue(String.class);
                Log.d("Hello", value_test);
                Log.d("Hello1", previousChildName);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

   private void filter(String text) {
       ArrayList<Products> filteredList = new ArrayList<>();
           for (Products item : productsList) {
               if (item.getTitle().toLowerCase().contains(text.toLowerCase()) || item.getDescription().toLowerCase().contains(text.toLowerCase())) {
                   filteredList.add(item);
               }
           }
           adapter.filterList(filteredList);
   }
}
