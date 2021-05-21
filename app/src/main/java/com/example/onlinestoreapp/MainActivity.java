package com.example.onlinestoreapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    String Email,Password;
    String name;
    Intent intent;
    ProgressBar progressBar2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null && currentUser.isEmailVerified())
        {
            Intent intent=new Intent(MainActivity.this,LandingPage.class);
            startActivity(intent);
        }
        setContentView(R.layout.activity_main);
        Button btn,btn1;
        EditText email,password;
        progressBar2=findViewById(R.id.progressBar);
        email=findViewById(R.id.email_id);
        password=findViewById(R.id.password_field);
        btn=findViewById(R.id.signup_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,SignUp.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slidein_right,R.anim.slideout_left);
            }
        });
        btn1=findViewById(R.id.login_btn);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Email=email.getText().toString();
                if(email.getText().toString().isEmpty())
                {
                    email.setError("Email should not be empty");
                }
                Password=password.getText().toString();
                if(password.getText().toString().isEmpty())
                {
                    password.setError("Password cannot be empty");
                }
                else
                {
                    progressBar2.setVisibility(View.VISIBLE);
                    login_activity();
                }

            }
        });


    }
    public void login_activity()
    {
        mAuth.signInWithEmailAndPassword(Email, Password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressBar2.setVisibility(View.INVISIBLE);
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("ABC", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if(user.isEmailVerified()) {
                                intent = new Intent(MainActivity.this, LandingPage.class);
                                startActivity(intent);
                            }
                                else {
                                progressBar2.setVisibility(View.INVISIBLE);
                                Toast.makeText(MainActivity.this, "Email is not verified! ", Toast.LENGTH_SHORT).show();
                                user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(MainActivity.this,"Verification Email Sent !", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(MainActivity.this,e.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("ABC", "signInWithEmail:failure", task.getException());
                            progressBar2.setVisibility(View.INVISIBLE);
                            Toast.makeText(MainActivity.this, "Incorrect Password or Email ID is not registered!",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }
}