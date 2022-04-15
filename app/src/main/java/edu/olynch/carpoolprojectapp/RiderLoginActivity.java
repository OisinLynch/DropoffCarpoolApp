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

    //Create variables
    private EditText mEmail, mPassword;
    private Button mLogin, mRegistration;
    private TextView mReturnToSelect;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_login);

        //initialise Firebase variables
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();
        //mDatabaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://carpoolprojectappv2-default-rtdb.firebaseio.com");
        mFirestore = FirebaseFirestore.getInstance();

        //Method to change the screen to the customer map activity when a passengers authentication state changes
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

        //Initialise the variables
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mRegistration = findViewById(R.id.registration);
        mLogin = findViewById(R.id.login);
        mReturnToSelect = findViewById(R.id.returnToSelect);

        //Registration button to call the registration method for passengers
        mRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //registerUserPassengerRealtime();
                registerUserPassenger();
            }
        });

        //login button to login passenger accounts
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

        //button to return to account selection screen
        mReturnToSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RiderLoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    //Method to allow for Passengers to register and adding their registration details to
    //Firebase Firestore Database
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

    //Method used to attempt to get tht Firebase Realtime Database to work on Passenger Registration

    /*
    private void registerUserPassengerRealtime() {
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
                            //DocumentReference dbPassenger = mFirestore.collection("Passengers").document(user.getUid());
                            mDatabaseReference.child("Users")
                                    .child("Passengers")
                                    .setValue(user.getUid());

                            //Map<String,Object> userEmail = new HashMap<>();
                            //userEmail.put("Email",mEmail.getText().toString());
                            //dbPassenger.set(userEmail);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(RiderLoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
     */

    //Start the auth state listener to see if passenger is logged in on the application startup
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthListener);
    }

    //Stop the auth state listener to see if passenger is logged out on the application startup
    @Override
    protected  void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthListener);
    }
}

