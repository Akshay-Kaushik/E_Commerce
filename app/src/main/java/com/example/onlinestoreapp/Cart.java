package com.example.onlinestoreapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.lang.Math.round;

public class Cart extends AppCompatActivity {
    AlertDialog.Builder builder;
    private FirebaseAuth mAuth;
    List<CartItems> cartItemsList,cartItemsList_1;
    FirebaseDatabase database, database1;
    DatabaseReference myRef, myRef_Data,myRef_Data1;
    TextView gst_added, cost_added, total_cost_cart;
    String title, imageURL, quantity, Price;
    CartAdapter adapter;
    RecyclerView recyclerView;

    ConstraintLayout no_products_constraint, cart_available_cl;
    View button, button1,button2;
    double cost=0.0, gst=0.0, total=0.0,total_cost=0.0;

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Cart.this, ProductsPage.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slidein_left,R.anim.slideout_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        no_products_constraint = findViewById(R.id.no_products_constraint);
        cart_available_cl = findViewById(R.id.cart_avalable_cl);
        mAuth = FirebaseAuth.getInstance();
        if (Variables.global_cart_count == 0) {
            cart_available_cl.setVisibility(View.GONE);
            no_products_constraint.setVisibility(View.VISIBLE);
            button1 = findViewById(R.id.back_to_products);
            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Cart.this, ProductsPage.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slidein_left,R.anim.slideout_right);

                }
            });

        } else {
            no_products_constraint.setVisibility(View.GONE);
            cart_available_cl.setVisibility(View.VISIBLE);
            button = findViewById(R.id.place_order);
            cost_added = findViewById(R.id.cost_added);
            gst_added = findViewById(R.id.gst_added);
            total_cost_cart = findViewById(R.id.total_cost_cart);
            setCostValue();
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(Cart.this,ConfirmOrder.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slidein_right,R.anim.slideout_left);
                }
            });
            button2=findViewById(R.id.back_to_products2);
            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Cart.this, ProductsPage.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slidein_left,R.anim.slideout_right);
                }
            });

        }
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        View header = navigationView.getHeaderView(0);
        TextView textUsername = header.findViewById(R.id.user_full_name_nav_header);
        textUsername.setText(Variables.name);
        recyclerView = findViewById(R.id.rv_cart);
        cartItemsList = new ArrayList<>();
        cartItemsList_1=new ArrayList<>();
        check_cart_number();
        create_RecyclerView();
        fetch_data_realtime();
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
                        builder = new AlertDialog.Builder(Cart.this);
                        builder.setMessage("Are you sure you want to log out?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        mAuth.signOut();
                                        Intent intent = new Intent(Cart.this, MainActivity.class);
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
                        Intent intent = new Intent(Cart.this, LandingPage.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slidein_left,R.anim.slideout_right);
                        return true;
                    case R.id.nav_myprofile:
                        Intent intent1 = new Intent(Cart.this, MyProfile.class);
                        startActivity(intent1);
                        overridePendingTransition(R.anim.slidein_left,R.anim.slideout_right);
                        return true;
                    //Intent to MyProfile Page
                    case R.id.nav_cart:
                        return true;
                    //Intent to cart
                    case R.id.nav_orders:
                        Intent intent3 = new Intent(Cart.this, MyOrders.class);
                        startActivity(intent3);
                        overridePendingTransition(R.anim.slidein_right,R.anim.slideout_left);
                        return true;
                    //Intent to My Orders Page
                    case R.id.nav_sell:
                        Intent intent4 = new Intent(Cart.this, Sell.class);
                        startActivity(intent4);
                        overridePendingTransition(R.anim.slidein_right,R.anim.slideout_left);
                        return true;
                    //Intent to Selling Page
                    case R.id.nav_settings:
                        //Intent to settings Page
                        return true;
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

    public void create_RecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //Fetch Data From Real time database
        Log.d("recycler", String.valueOf(recyclerView.getId()));
    }

    public void fetch_data_realtime() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Cart");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String, String> value;
                value = (Map<String, String>) snapshot.getValue();
                if (value != null){

                    for (String str : value.keySet()) {
                        if (str.equals(mAuth.getUid())) {
                            myRef_Data = database.getReference("Cart").child(mAuth.getUid());
                            myRef_Data.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    Map<String, String> value;
                                    value = (Map<String, String>) snapshot.getValue();
                                    if (value != null) {
                                        for (String key : value.keySet()) {
                                            add_to_cart_list(key);
                                            cartItemsList = new ArrayList<>();
                                        }
                                    } else {
                                        Log.d("Zero", String.valueOf(Variables.global_cart_count));
                                        Intent intent = new Intent(Cart.this, Cart.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                        startActivity(intent);
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

    public void check_cart_number() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Cart");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String, String> value;
                value = (Map<String, String>) snapshot.getValue();
                if (value != null){
                    for (String str : value.keySet()) {
                        if (str.equals(mAuth.getUid())) {
                            myRef.child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    Map<String, String> value_test;
                                    value_test = (Map<String, String>) snapshot.getValue();
                                    if (value_test == null) {
                                        Variables.global_cart_count = 0;
                                        Intent intent = new Intent(Cart.this, Cart.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                        startActivity(intent);
                                    } else {
                                        Variables.global_cart_count = value_test.keySet().size();
                                        TextView textView = findViewById(R.id.cart_number);
                                        textView.setText(String.valueOf(Variables.global_cart_count));
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

    public void setCostValue() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef_Data1 = database.getReference("Cart").child(mAuth.getUid());
        myRef_Data1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String,String> value=(Map<String, String>) snapshot.getValue();
                if(value!=null) {
                    for (String str : value.keySet()) {
                        total_cost=0.0;
                        Log.d("str", str);
                        fetch_info(str);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public void fetch_info(String str)
    {
        myRef_Data1.child(str).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String,String> value=(Map<String, String>) snapshot.getValue();
                if(value!=null) {
                    double price = Double.parseDouble(value.get("Price"));
                    int quantity = Integer.parseInt(value.get("Quantity"));
                    cost =price * quantity;
                    total_cost = total_cost + cost;
                    Log.d("gst", String.valueOf(total));
                    cost_added.setText(String.format("₹%s", round(total_cost*100.0)/100.0));
                    gst=0.18*total_cost;
                    gst_added.setText(String.format("₹%s", round(gst*100.0)/100.0));
                    total_cost_cart.setText(String.format("₹%s", round((total_cost*1.18+50)*100.0)/100.0));
                }
                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void add_to_cart_list(String str) {
        myRef_Data.child(str).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String, String> value = (Map<String, String>) snapshot.getValue();
                if (value != null) {
                    title = value.get("Name");
                    imageURL = value.get("ImageURL");
                    quantity = value.get("Quantity");
                    Price = value.get("Price");
                    String category = value.get("Category");
                    cartItemsList.add(
                            new CartItems(
                                    title,
                                    quantity,
                                    imageURL,
                                    Price,
                                    str,
                                    category
                            )
                    );
                    Log.d("list",cartItemsList.get(0).toString());
                    adapter = new CartAdapter(Cart.this, cartItemsList);
                    recyclerView.setAdapter(adapter);

                } else {
                    Variables.global_cart_count = 0;
                    Log.d("inside", "TEST");
                    cartItemsList = new ArrayList<>();
                    Intent intent = new Intent(Cart.this, Cart.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}