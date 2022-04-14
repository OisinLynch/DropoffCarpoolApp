package edu.olynch.carpoolprojectapp;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RiderLoginActivity extends AppCompatActivity {

    private EditText mEmail, mPassword;
    private Button mLogin, mRegistration;
    private TextView mReturnToSelect;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private DatabaseReference mDatabaseReference;
    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_login);

        mAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://carpoolprojectappv2-default-rtdb.firebaseio.com");
        mFirestore = FirebaseFirestore.getInstance();

        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user != null)
                {
                    Intent intent = new Intent(RiderLoginActivity.this, CustomerMapActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };

        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mRegistration = findViewById(R.id.registration);
        mLogin = findViewById(R.id.login);
        mReturnToSelect = findViewById(R.id.returnToSelect);

        /*
        mRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mEmail.getText().toString();
                final String password = mPassword.getText().toString();
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RiderLoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful())
                        {
                            Toast.makeText(RiderLoginActivity.this, "Registration Error", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            String userId = mAuth.getCurrentUser().getUid();
                            mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(userId);
                            mDatabaseReference.setValue(true);
                            Toast.makeText(RiderLoginActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

         */


        mRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUserPassenger();
            }
        });

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mEmail.getText().toString();
                final String password = mPassword.getText().toString();
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(RiderLoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful())
                        {
                            Toast.makeText(RiderLoginActivity.this, "Login Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        mReturnToSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RiderLoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    private void registerUserPassenger() {
        String email = mEmail.getText().toString().trim();
        String password = mPassword.getText().toString().trim();

        if (email.isEmpty()) {
            mEmail.setError("Email is required");
            mEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmail.setError("Please provide a valid email address");
            mEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            mPassword.setError("Password is required");
            mPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            mPassword.setError("Password must be at least 6 characters long");
            mPassword.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            //If Authentication is successful display this to the user
                            Toast.makeText(RiderLoginActivity.this,"Account Created",Toast.LENGTH_SHORT).show();
                            DocumentReference dbPassenger = mFirestore.collection("Passengers").document(user.getUid());

                            Map<String,Object> userEmail = new HashMap<>();
                            userEmail.put("Email",mEmail.getText().toString());
                            dbPassenger.set(userEmail);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(RiderLoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected  void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthListener);
    }
}

