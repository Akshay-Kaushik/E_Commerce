package com.example.onlinestoreapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.TextKeyListener;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MyProfile extends AppCompatActivity {
    private FirebaseAuth mAuth;
    AlertDialog.Builder builder;
    ImageView imageView, imageView1;
    EditText Name_editText, email_editText, mobile_editText;
    FirebaseFirestore db;
    ProgressBar progressBar;
    Button button;

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MyProfile.this, LandingPage.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slidein_left, R.anim.slideout_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        progressBar=findViewById(R.id.progressBar2);
        button=findViewById(R.id.backtohome);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        mAuth = FirebaseAuth.getInstance();
        Log.d("UID", mAuth.getUid());
        db = FirebaseFirestore.getInstance();
        TextView textView = findViewById(R.id.cart_number);
        textView.setText(String.valueOf(Variables.global_cart_count));
        imageView = findViewById(R.id.edit_name_pencil);
        imageView1 = findViewById(R.id.check);
        Name_editText = findViewById(R.id.full_name);
        Name_editText.setKeyListener(null);
        email_editText = findViewById(R.id.email_editText);
        email_editText.setKeyListener(null);
        mobile_editText = findViewById(R.id.mobile_number);
        mobile_editText.setKeyListener(null);
        assign_field_value();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView.setVisibility(View.GONE);
                imageView1.setVisibility(View.VISIBLE);
                Name_editText.setKeyListener(TextKeyListener.getInstance(false, TextKeyListener.Capitalize.WORDS));
                imageView1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String new_name = Name_editText.getText().toString();
                        Name_editText.setKeyListener(null);
                        imageView1.setVisibility(View.GONE);
                        imageView.setVisibility(View.VISIBLE);
                        update(new_name);
                    }
                });
            }
        });

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        View header = navigationView.getHeaderView(0);
        TextView textUsername = header.findViewById(R.id.user_full_name_nav_header);
        textUsername.setText(Variables.name);
        ImageButton ImBtn = findViewById(R.id.navicon);
        ImageButton ImBtn1 = findViewById(R.id.cart_icon);
        ImBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyProfile.this, Cart.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slidein_right, R.anim.slideout_left);
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
                switch (id) {
                    case R.id.nav_logout:
                        builder = new AlertDialog.Builder(MyProfile.this);
                        builder.setMessage("Are you sure you want to log out?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        mAuth.signOut();
                                        Intent intent = new Intent(MyProfile.this, MainActivity.class);
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
                        Intent intent = new Intent(MyProfile.this, LandingPage.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        return true;
                    case R.id.nav_myprofile:
                        Intent intent1 = new Intent(MyProfile.this, MyProfile.class);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent1);
                        return true;
                    //Intent to MyProfile Page
                    case R.id.nav_cart:
                        Intent intent2 = new Intent(MyProfile.this, Cart.class);
                        intent2.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent2);
                        return true;
                    //Intent to cart
                    case R.id.nav_orders:
                        Intent intent3 = new Intent(MyProfile.this, MyOrders.class);
                        intent3.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent3);
                        return true;
                    //Intent to My Orders Page
                    case R.id.nav_sell:
                        Intent intent4 = new Intent(MyProfile.this, Sell.class);
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

    public void assign_field_value() {
        DocumentReference documentReference = db.collection("users").document(mAuth.getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        Name_editText.setText(documentSnapshot.get("FName").toString());
                        email_editText.setText(documentSnapshot.get("Email").toString());
                        mobile_editText.setText(documentSnapshot.get("Phone").toString());
                        progressBar.setVisibility(View.GONE);
                    }
                    else
                    {
                        Toast.makeText(MyProfile.this,"Could not fetch data! ",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void update(String new_name) {
        db.collection("users").document(mAuth.getUid()).update("FName", new_name);
        Toast.makeText(MyProfile.this,"Data Updated Successfully!",Toast.LENGTH_SHORT).show();
    }
}