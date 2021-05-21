package com.example.onlinestoreapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

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

public class ConfirmOrder extends AppCompatActivity {
    AlertDialog.Builder builder;
    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef_Cart,myRef_Order;
    EditText editText;
    int order_no;
    RadioGroup radioGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        mAuth = FirebaseAuth.getInstance();
        radioGroup=findViewById(R.id.radioGroup);
        Button button = findViewById(R.id.confirm_order);
        editText = findViewById(R.id.delivery_address);
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        View header = navigationView.getHeaderView(0);
        TextView textUsername = header.findViewById(R.id.user_full_name_nav_header);
        textUsername.setText(Variables.name);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                drawerLayout.closeDrawer(GravityCompat.START);
                switch (id) {
                    case R.id.nav_logout:
                        builder = new AlertDialog.Builder(ConfirmOrder.this);
                        builder.setMessage("Are you sure you want to log out?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        mAuth.signOut();
                                        Intent intent = new Intent(ConfirmOrder.this, MainActivity.class);
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
                        Intent intent = new Intent(ConfirmOrder.this, LandingPage.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slidein_left,R.anim.slideout_right);
                        return true;
                    case R.id.nav_myprofile:
                        Intent intent1 = new Intent(ConfirmOrder.this, MyProfile.class);
                        startActivity(intent1);
                        overridePendingTransition(R.anim.slidein_left,R.anim.slideout_right);
                        return true;
                    //Intent to MyProfile Page
                    case R.id.nav_cart:
                        return true;
                    //Intent to cart
                    case R.id.nav_orders:
                        Intent intent3 = new Intent(ConfirmOrder.this, MyOrders.class);
                        startActivity(intent3);
                        overridePendingTransition(R.anim.slidein_right,R.anim.slideout_left);
                        return true;
                    //Intent to My Orders Page
                    case R.id.nav_sell:
                        Intent intent4 = new Intent(ConfirmOrder.this, Sell.class);
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
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText.getText().toString().isEmpty()) {
                    editText.setError("Address cannot be empty!");
                }
                if(radioGroup.getCheckedRadioButtonId()==-1)
                {
                    Toast.makeText(ConfirmOrder.this,"Select a payment method!",Toast.LENGTH_SHORT).show();
                }
                else
                 {
                    builder = new AlertDialog.Builder(ConfirmOrder.this);
                    builder.setMessage("Are you sure you want to place the order?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                        get_order_number();
                                        Toast.makeText(ConfirmOrder.this,"Order Confirmed! ", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(ConfirmOrder.this, MyOrders.class);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.slidein_left, R.anim.slideout_right);
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
                    alert.setTitle("Confirm?");
                    alert.show();
                }
            }

        });
    }
    public void perform_confirmation()
    {
        database=FirebaseDatabase.getInstance();
        myRef_Cart=database.getReference("Cart").child(mAuth.getUid());
        myRef_Order=database.getReference("Orders");
        myRef_Cart.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String,String> value=(Map<String, String>)snapshot.getValue();
                value.put("Address",editText.getText().toString());
                value.put("UID",mAuth.getUid());
                Log.d("value",value.toString());
                String order_number=String.valueOf(order_no-1);
                myRef_Order.child(order_number).setValue(value);
                myRef_Cart.removeValue();
                Toast.makeText(ConfirmOrder.this,"Order Number is #"+String.valueOf(order_no-1), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void get_order_number()
    {
        database=FirebaseDatabase.getInstance();
        myRef_Order=database.getReference("Orders");
        myRef_Order.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<ArrayList> value=(ArrayList)snapshot.getValue();
                if(value==null)
                {
                    order_no=2;
                }
                else{
                    Log.d("Size",String.valueOf(value.size()));
                    order_no=value.size()+1;
                }
                perform_confirmation();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}