package com.example.onlinestoreapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class Sell extends AppCompatActivity {
    AlertDialog.Builder builder;
    private FirebaseAuth mAuth;
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Sell.this, LandingPage.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slidein_left,R.anim.slideout_right);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);
        mAuth = FirebaseAuth.getInstance();
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        View header = navigationView.getHeaderView(0);
        TextView textUsername = header.findViewById(R.id.user_full_name_nav_header);
        textUsername.setText(Variables.name);
        ImageButton ImBtn1=findViewById(R.id.cart_icon);
        ImBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Sell.this,Cart.class);
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
                        builder = new AlertDialog.Builder(Sell.this);
                        builder.setMessage("Are you sure you want to log out?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        mAuth.signOut();
                                        Intent intent = new Intent(Sell.this, MainActivity.class);
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
                        Intent intent = new Intent(Sell.this, LandingPage.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        return true;
                    case R.id.nav_myprofile:
                        Intent intent1 = new Intent(Sell.this, MyProfile.class);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent1);
                        return true;
                        //Intent to MyProfile Page
                    case R.id.nav_cart:
                        Intent intent2 = new Intent(Sell.this, Cart.class);
                        intent2.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent2);
                        return true;
                        //Intent to cart
                    case R.id.nav_orders:
                        Intent intent3 = new Intent(Sell.this, MyOrders.class);
                        intent3.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent3);
                        return true;
                        //Intent to My Orders Page
                    case R.id.nav_sell:
                        Intent intent4 = new Intent(Sell.this, Sell.class);
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
}