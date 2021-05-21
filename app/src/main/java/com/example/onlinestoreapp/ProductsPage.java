package com.example.onlinestoreapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.util.EventLog;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.errorprone.annotations.Var;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ProductsPage extends AppCompatActivity {
    private StorageReference mStorageRef;
    private FirebaseAuth mAuth;
    AlertDialog.Builder builder;
    RecyclerView recyclerView;
    CategoriesAdapter adapter;
    List<Categories> categoriesList;
    FirebaseDatabase database;
    DatabaseReference myRef, myRef_URL;
    Map<String, String> value;
    String value_url;
    String imageURL;
    TextView textView;
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ProductsPage.this, LandingPage.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slidein_left,R.anim.slideout_right);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_page);
        mAuth = FirebaseAuth.getInstance();
        textView=findViewById(R.id.cart_number);
        textView.setText(String.valueOf(Variables.global_cart_count));
        Log.d("value",String.valueOf(Variables.global_cart_count));
        categoriesList = new ArrayList<>();
        recyclerView = findViewById(R.id.rv_categories);
        create_RecyclerView(recyclerView);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        //Navigation Bar
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        View header = navigationView.getHeaderView(0);
        TextView textUsername = header.findViewById(R.id.user_full_name_nav_header);
        textUsername.setText(Variables.name);
        ImageButton ImBtn = findViewById(R.id.navicon);
        ImBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        ImageButton ImBtn1=findViewById(R.id.cart_icon);
        ImBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ProductsPage.this,Cart.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slidein_right,R.anim.slideout_left);
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                drawerLayout.closeDrawer(GravityCompat.START);
                switch (id) {
                    case R.id.nav_logout:
                        builder = new AlertDialog.Builder(ProductsPage.this);
                        builder.setMessage("Are you sure you want to log out?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        mAuth.signOut();
                                        Intent intent = new Intent(ProductsPage.this, MainActivity.class);
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
                        Intent intent = new Intent(ProductsPage.this, LandingPage.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slidein_left,R.anim.slideout_right);
                        return true;
                    case R.id.nav_myprofile:
                        Intent intent1 = new Intent(ProductsPage.this, MyProfile.class);
                        startActivity(intent1);
                        overridePendingTransition(R.anim.slidein_right,R.anim.slideout_left);
                        return true;
                        //Intent to MyProfile Page
                    case R.id.nav_cart:
                        Intent intent2 = new Intent(ProductsPage.this, Cart.class);
                        startActivity(intent2);
                        overridePendingTransition(R.anim.slidein_right,R.anim.slideout_left);
                        return true;
                        //Intent to cart
                    case R.id.nav_orders:
                        Intent intent3 = new Intent(ProductsPage.this, MyOrders.class);
                        startActivity(intent3);
                        overridePendingTransition(R.anim.slidein_right,R.anim.slideout_left);
                        return true;
                        //Intent to My Orders Page
                    case R.id.nav_sell:
                        Intent intent4 = new Intent(ProductsPage.this, Sell.class);
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
        //End of Navigation Bar
    }

    //Function to create the Recycler view based on the Category
    public void create_RecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //Fetch Data From Real time database
        Log.d("recycler", String.valueOf(recyclerView.getId()));
        fetch_data_realtime(recyclerView);
    }
    //Check category

    public void fetch_data_realtime(RecyclerView recyclerView) {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Products");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                value = (Map<String, String>) dataSnapshot.getValue();
                Log.d("TAG", "Value is: " + value.keySet());
                for(String str:value.keySet())
                {
                    categoriesList=new ArrayList<>();
                    add_to_category_list(str,recyclerView);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });


    }

    public void add_to_category_list(String str, RecyclerView recyclerView)
    {
        myRef_URL=myRef.child(str).child("imageURL");
        myRef_URL.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                value_url = snapshot.getValue(String.class);
                imageURL=value_url;
                Log.d("TEST",value_url);
                categoriesList.add(
                        new Categories(
                                str,
                                imageURL
                        )
                );
                adapter=new CategoriesAdapter(ProductsPage.this, categoriesList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}