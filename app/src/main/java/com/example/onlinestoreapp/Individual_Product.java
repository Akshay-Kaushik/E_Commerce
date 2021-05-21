package com.example.onlinestoreapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Individual_Product extends AppCompatActivity {
    private FirebaseAuth mAuth;
    String user;
    AlertDialog.Builder builder;
    String key,category,name,long_description,imageURL,quantity;
    double Price;
    Bundle bundle;
    Button add_to_cart;
    NumberPicker numberPicker;
    int quantity_added=1;
    TextView textView;
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Individual_Product.this, Products_Details.class);
        intent.putExtra("category",category);
        startActivity(intent);
        overridePendingTransition(R.anim.slidein_left,R.anim.slideout_right);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_product);
        ImageButton ImBtn1=findViewById(R.id.cart_icon);
        ImBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Individual_Product.this,Cart.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slidein_right,R.anim.slideout_left);
            }
        });
        Log.d("value",String.valueOf(Variables.global_cart_count));
        bundle = getIntent().getExtras();
        fetch_variables();
        numberPicker=findViewById(R.id.number_picker);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(Integer.parseInt(quantity));
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                quantity_added=numberPicker.getValue();
            }
        });
        textView=findViewById(R.id.cart_number);
        textView.setText(String.valueOf(Variables.global_cart_count));
        textView=findViewById(R.id.title_field);
        textView.setText(name);
        textView=findViewById(R.id.price_field);
        textView.setText("₹"+ Price);
        textView=findViewById(R.id.long_description_field);
        textView.setText("Description: \n"+long_description);
        ImageView imageView=findViewById(R.id.product_image_individual);
        Glide.with(Individual_Product.this)
                .load(imageURL)
                .into(imageView);
        add_to_cart=findViewById(R.id.add_to_cart_btn);
        add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Quantity",String.valueOf(quantity_added));
                add_to_cart_function(quantity_added,0,key);
            }
        });
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        View header = navigationView.getHeaderView(0);
        TextView textUsername = header.findViewById(R.id.user_full_name_nav_header);
        textUsername.setText(Variables.name);
        mAuth = FirebaseAuth.getInstance();
        user=mAuth.getUid();

        Log.d("MAUTH",user);
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
                        builder = new AlertDialog.Builder(Individual_Product.this);
                        builder.setMessage("Are you sure you want to log out?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        mAuth.signOut();
                                        Intent intent = new Intent(Individual_Product.this, MainActivity.class);
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
                        Intent intent = new Intent(Individual_Product.this, LandingPage.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slidein_left,R.anim.slideout_right);
                        return true;
                    case R.id.nav_myprofile:
                        Intent intent1 = new Intent(Individual_Product.this, MyProfile.class);
                        startActivity(intent1);
                        overridePendingTransition(R.anim.slidein_right,R.anim.slideout_left);
                        return true;
                    //Intent to MyProfile Page
                    case R.id.nav_cart:
                        Intent intent2 = new Intent(Individual_Product.this, Cart.class);
                        startActivity(intent2);
                        overridePendingTransition(R.anim.slidein_right,R.anim.slideout_left);
                        return true;
                    //Intent to cart
                    case R.id.nav_orders:
                        Intent intent3 = new Intent(Individual_Product.this, MyOrders.class);
                        startActivity(intent3);
                        overridePendingTransition(R.anim.slidein_right,R.anim.slideout_left);
                        return true;
                    //Intent to My Orders Page
                    case R.id.nav_sell:
                        Intent intent4 = new Intent(Individual_Product.this, Sell.class);
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
    }
    public void fetch_variables()
    {
        category = bundle.getString("category");
        key=bundle.getString("key");
        name=bundle.getString("name");
        long_description=bundle.getString("long_description");
        Price=bundle.getDouble("Price");
        quantity=bundle.getString("Quantity");
        imageURL=bundle.getString("imageURL");
    }
    public void add_to_cart_function(int quantity_added,int key_condition,String key)
    {
        if(key_condition==0)
        {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("Cart").child(user).child(key);
            Map<String,String> object =new HashMap<String,String>();
            object.put("Category",category);
            object.put("Name",name);
            object.put("Quantity",String.valueOf(quantity_added));
            object.put("Price",String.valueOf(Price));
            object.put("ImageURL",imageURL);
            myRef.setValue(object);
            Toast.makeText(Individual_Product.this,"Product Added To Cart!", Toast.LENGTH_SHORT).show();
            check_cart_number();
        }
        else if(key_condition==1)
        {
            mAuth = FirebaseAuth.getInstance();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("Cart").child(mAuth.getUid()).child(key).child("Quantity");
            myRef.setValue(String.valueOf(quantity_added));

        }
        else if(key_condition==2)
        {
            mAuth = FirebaseAuth.getInstance();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("Cart").child(mAuth.getUid()).child(key);
            myRef.removeValue();
        }

    }
    public void check_cart_number()
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Cart");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String, String> value;
                value = (Map<String, String>) snapshot.getValue();
                if (value != null)
                {
                    for (String str : value.keySet()) {
                        if (str.equals(mAuth.getUid())) {
                            myRef.child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    Map<String, String> value_test;
                                    value_test = (Map<String, String>) snapshot.getValue();
                                    if (value_test != null) {
                                        Variables.global_cart_count = value_test.keySet().size();
                                        TextView textView = findViewById(R.id.cart_number);
                                        textView.setText(String.valueOf(Variables.global_cart_count));
                                    } else {
                                        TextView textView = findViewById(R.id.cart_number);
                                        textView.setText(String.valueOf(0));
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }
            }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}