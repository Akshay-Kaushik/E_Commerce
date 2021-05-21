package com.example.onlinestoreapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firestore.v1.StructuredQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyOrders extends AppCompatActivity {
    private FirebaseAuth mAuth;
    AlertDialog.Builder builder;
    RecyclerView recyclerView;
    DatabaseReference myRef;
    FirebaseDatabase database;
    String title,key,ImageURL,order_number,category;
    List<OrderItems> orderItemsList;
    OrderAdapter adapter;
    ConstraintLayout constraintLayout;
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MyOrders.this, LandingPage.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slidein_left,R.anim.slideout_right);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);
        mAuth = FirebaseAuth.getInstance();
        TextView textView = findViewById(R.id.cart_number);
        textView.setText(String.valueOf(Variables.global_cart_count));
        constraintLayout=findViewById(R.id.no_products_constraint);
        recyclerView=findViewById(R.id.rv_orders);
        create_RecyclerView();
        if(Variables.global_order_count==0)
        {
            recyclerView.setVisibility(View.GONE);
            Button button5=findViewById(R.id.back_to_home);
            button5.setVisibility(View.GONE);
            constraintLayout.setVisibility(View.VISIBLE);
            Button button=findViewById(R.id.back_to_products);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MyOrders.this, ProductsPage.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slidein_left,R.anim.slideout_right);
                }
            });
        }
        else
        {
            recyclerView.setVisibility(View.VISIBLE);
            constraintLayout.setVisibility(View.GONE);
            Button button=findViewById(R.id.back_to_home);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MyOrders.this, LandingPage.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slidein_left,R.anim.slideout_right);
                }
            });
        }
        orderItemsList=new ArrayList<>();
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        View header = navigationView.getHeaderView(0);
        TextView textUsername = header.findViewById(R.id.user_full_name_nav_header);
        textUsername.setText(Variables.name);
        ImageButton ImBtn1=findViewById(R.id.cart_icon);
        ImBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MyOrders.this,Cart.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slidein_right,R.anim.slideout_left);
            }
        });
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
                        builder = new AlertDialog.Builder(MyOrders.this);
                        builder.setMessage("Are you sure you want to log out?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        mAuth.signOut();
                                        Intent intent = new Intent(MyOrders.this, MainActivity.class);
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
                        Intent intent = new Intent(MyOrders.this, LandingPage.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        return true;
                    case R.id.nav_myprofile:
                        Intent intent1 = new Intent(MyOrders.this, MyProfile.class);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent1);
                        return true;
                        //Intent to MyProfile Page
                    case R.id.nav_cart:
                        Intent intent2 = new Intent(MyOrders.this, Cart.class);
                        intent2.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent2);
                        return true;
                        //Intent to cart
                    case R.id.nav_orders:
                        Intent intent3 = new Intent(MyOrders.this, MyOrders.class);
                        intent3.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent3);
                        return true;
                        //Intent to My Orders Page
                    case R.id.nav_sell:
                        Intent intent4 = new Intent(MyOrders.this, Sell.class);
                        intent4.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent4);
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
    public void create_RecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //Fetch Data From Real time database
        Log.d("recycler", String.valueOf(recyclerView.getId()));
        fetch_data_realtime();
    }
    public void fetch_data_realtime() {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Orders");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Map<String,String>> value= (List<Map<String, String>>) snapshot.getValue();
                if(value!=null) {
                    Log.d("ORDER",value.toString());
                    for(int i=1;i<value.size();i++)
                    {
                        check_UID(String.valueOf(i),value.get(i));
                    }
                }

                }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public void check_UID(String ordernumber, Map<String,String> value_required) {
        Map<String, String> value = new HashMap<>();
        if (value_required.get("UID").equals(mAuth.getUid())) {
            Variables.global_order_count=1;
            list_all_orders(value_required, ordernumber);
        }
    }
    public void list_all_orders(Map<String,String> value, String key)
    {
        for(String prod_key:value.keySet())
        {
            if(!prod_key.equals("Address") && !prod_key.equals("UID"))
            {
                get_ordered_product(prod_key,key);
            }
        }
    }
    public void get_ordered_product(String prodkey,String ordernumber)
    {
        myRef.child(ordernumber).child(prodkey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String,String> value=(Map<String, String>) snapshot.getValue();
                title=value.get("Name");
                order_number=ordernumber;
                category=value.get("Category");
                ImageURL=value.get("ImageURL");
                key=prodkey;
                orderItemsList.add(
                        new OrderItems(
                        order_number,
                                key,
                                category,
                                title,
                                ImageURL
                        )
                );
                adapter=new OrderAdapter(MyOrders.this,orderItemsList);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}