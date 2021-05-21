package com.example.onlinestoreapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.server.converter.StringToIntConverter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.errorprone.annotations.Var;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.inappmessaging.display.internal.layout.CardLayoutLandscape;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class LandingPage extends AppCompatActivity {
    Map<String, Object> user_info ;
    AlertDialog.Builder builder;
    private FirebaseAuth mAuth;
    String email_id,name;
    FirebaseFirestore db;
    FirebaseDatabase database;
    DatabaseReference myRef;
    Map<String, Object> value;
    @Override
    public void onBackPressed() {
        builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to log out?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mAuth.signOut();
                        Intent intent = new Intent(LandingPage.this, MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slidein_left,R.anim.slideout_right);
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
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        setContentView(R.layout.activity_landing_page);
        ImageButton ImBtn1=findViewById(R.id.cart_icon);
        ImBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LandingPage.this,Cart.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slidein_right,R.anim.slideout_left);
            }
        });
        ImBtn1=findViewById(R.id.sell_products);
        ImBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LandingPage.this,Sell.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slidein_right,R.anim.slideout_left);
            }
        });
        ImBtn1=findViewById(R.id.my_profile_btn);
        ImBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LandingPage.this,MyProfile.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slidein_right,R.anim.slideout_left);
            }
        });
        ImBtn1=findViewById(R.id.my_orders);
        ImBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LandingPage.this,MyOrders.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slidein_right,R.anim.slideout_left);
            }
        });

        Log.d("value",String.valueOf(Variables.global_cart_count));
        db=FirebaseFirestore.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        email_id=currentUser.getEmail();
        check_cart_number();
        read_data();
        Log.d("TEST",email_id);
        DrawerLayout drawerLayout=findViewById(R.id.drawer_layout);
        NavigationView navigationView=findViewById(R.id.navigation_view);
        View header = navigationView.getHeaderView(0);
        TextView textUsername = header.findViewById(R.id.user_full_name_nav_header);
        textUsername.setText(Variables.name);
        ImageButton ImBtn =findViewById(R.id.navicon);
        ImageButton cart=findViewById(R.id.cart_icon);
        ImageButton products_page=findViewById(R.id.view_products);
        products_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LandingPage.this,ProductsPage.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slidein_right,R.anim.slideout_left);
            }
        });
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LandingPage.this,Cart.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slidein_right,R.anim.slideout_left);

                //Go to Cart Screen
            }
        });
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
                switch(id)
                {
                    case R.id.nav_logout:
                        onBackPressed();
                        return true;
                    case R.id.nav_home:
                        return true;
                    case R.id.nav_myprofile:
                        Intent intent1 = new Intent(LandingPage.this, MyProfile.class);
                        startActivity(intent1);
                        overridePendingTransition(R.anim.slidein_right,R.anim.slideout_left);
                        return true;
                    //Intent to MyProfile Page
                    case R.id.nav_cart:
                        Intent intent2 = new Intent(LandingPage.this, Cart.class);
                        startActivity(intent2);
                        overridePendingTransition(R.anim.slidein_right,R.anim.slideout_left);
                        return true;
                    //Intent to cart
                    case R.id.nav_orders:
                        Intent intent3 = new Intent(LandingPage.this, MyOrders.class);
                        startActivity(intent3);
                        overridePendingTransition(R.anim.slidein_right,R.anim.slideout_left);
                        return true;
                    //Intent to My Orders Page
                    case R.id.nav_sell:
                        Intent intent4 = new Intent(LandingPage.this, Sell.class);
                        startActivity(intent4);
                        overridePendingTransition(R.anim.slidein_right,R.anim.slideout_left);
                        return true;
                    //Intent to Selling Page
                    case R.id.nav_settings:
                        return true;
                        //Intent to settings Page
                    case R.id.nav_share:
                        //Function to share the link of the app
                        Intent myIntent=new Intent(Intent.ACTION_SEND);
                        myIntent.setType("text/plain");
                        String shareBody="You can download the XFORCE E-Commerce App from: https://drive.google.com/drive/folders/1Iy8DxuqBeI0ns6WbDnac34NUXPVETu_8?usp=sharing ";
                        String shareSub="Hey!";
                        myIntent.putExtra(Intent.EXTRA_SUBJECT,shareSub);
                        myIntent.putExtra(Intent.EXTRA_TEXT,shareBody);
                        startActivity(Intent.createChooser(myIntent,"Share Via"));
                    case R.id.nav_rate:
                        return true;
                        //Function to rate the app
                    default:
                        return true;
                }
            }
        });
    }
//    public void read_data()
//    {
//        user_info=new HashMap<>();
//        db.collection("users")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                user_info=document.getData();
//                                if(user_info.get("Email").equals(email_id)) {
//                                    name=user_info.get("FName").toString();
//                                    Log.d("ABC", document.getId() + " => " + user_info.get("FName"));
//                                    Variables.name=name;
//                                }
//                            }
//                        } else {
//                            Log.w("ABCD", "Error getting documents.", task.getException());
//                        }
//                    }
//                });
//    }
    public void read_data()
    {
        user_info=new HashMap<>();
        DocumentReference documentReference=db.collection("users").document(mAuth.getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot=task.getResult();
                    if(documentSnapshot.exists())
                    {
                        name=documentSnapshot.get("FName").toString();
                        Variables.name=name;
                    }
                }
            }
        });
    }
    public void check_cart_number()
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Cart");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String,String> value;
                value=(Map<String,String>)snapshot.getValue();
                if(value!=null) {
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