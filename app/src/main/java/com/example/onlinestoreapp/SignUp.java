package com.example.onlinestoreapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {
    String full_name,email,password,phone_number;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String MobilePattern = "[0-9]{10}";
    String TAG="ABC";
    ProgressBar progressBar;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    @Override
    public void finish()
    {
        super.finish();
        overridePendingTransition(R.anim.slidein_left,R.anim.slideout_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Button btn, signup;
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        signup = findViewById(R.id.sign_up_btn);
        btn = findViewById(R.id.back_to_login);
        progressBar=findViewById(R.id.progressBar);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUp.this, MainActivity.class);
                startActivity(intent);
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate())
                {
                    progressBar.setVisibility(View.VISIBLE);
                    firebase_register();
                }
            }
        });
    }

    public boolean validate() {
        EditText FNAME = findViewById(R.id.fname_text);
        full_name = FNAME.getText().toString();
        if (full_name.length() == 0) {
            FNAME.setError("Name cannot be empty");
            return false;
        }
        EditText mobile_no = findViewById(R.id.phone_field);
        phone_number = mobile_no.getText().toString();
        if (!phone_number.matches(MobilePattern)) {
            mobile_no.setError("Enter Correct Mobile Number");
            return false;
        }
        EditText Email = findViewById(R.id.email_field);
        email = Email.getText().toString();
        if (!email.trim().matches(emailPattern)) {
            Email.setError("Enter Correct Email");
            return false;
        }
        EditText PASS = findViewById(R.id.password_field_signup);
        password = PASS.getText().toString();
        EditText Confirm_Pass = findViewById(R.id.confirm_password_field_signup);
        if (password.isEmpty()) {
            PASS.setError("Password cannot be empty");
            return false;
        }
        if (password.length() < 6)
        {
            PASS.setError("Password should contain atleast 6characters");
        }
        if(Confirm_Pass.getText().toString().isEmpty())
        {
            Confirm_Pass.setError("Confirm your password");
            return false;
        }
        if(!password.equals(Confirm_Pass.getText().toString()))
        {
            Confirm_Pass.setError("Make sure you confirm your password correctly!");
            return false;
        }
        else {
            return true;
        }

    }
    public void firebase_register()
    {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.d(TAG, "createUserWithEmail:success");
                            Toast.makeText(SignUp.this,"Registered Successfully!", Toast.LENGTH_SHORT).show();
                            store_info();
                            user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(SignUp.this,"Verification Email Sent !", Toast.LENGTH_LONG).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(SignUp.this,e.toString(), Toast.LENGTH_SHORT).show();
                                }
                            });
                            progressBar.setVisibility(View.INVISIBLE);
                            Intent intent=new Intent(SignUp.this,MainActivity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUp.this, "Email already registered or some error occurred!",
                                    Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }

                        // ...
                    }
                });
    }
    public void store_info() {
        Map<String, Object> user = new HashMap<>();
        user.put("FName", full_name);
        user.put("Email", email);
        user.put("Phone", phone_number);
// Add a new document with a generated ID
        db.collection("users")
                .document(mAuth.getUid())
                .set(user);
    }

}

