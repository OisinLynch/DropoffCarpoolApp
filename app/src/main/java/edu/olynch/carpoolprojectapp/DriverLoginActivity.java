package edu.olynch.carpoolprojectapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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

public class DriverLoginActivity extends AppCompatActivity {

    //Create variables
    private EditText mEmail, mPassword;
    private Button mLogin, mRegistration;
    private TextView mReturnToSelect;
    private ImageView mDropoffLogo;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_login);

        //Initialise Firebase variables
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Drivers");

        //Bring user to the driver map activity if their authentiction state changes
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(DriverLoginActivity.this, DriverMapActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };

        //initilise variables
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mRegistration = findViewById(R.id.registration);
        mLogin = findViewById(R.id.login);
        mReturnToSelect = findViewById(R.id.returnToSelect);
        mDropoffLogo = findViewById(R.id.dropoffLogo);

        //Login Button
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mEmail.getText().toString();
                final String password = mPassword.getText().toString();
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(DriverLoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful())
                        {
                            Toast.makeText(DriverLoginActivity.this, "Login Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        //Register button
        mRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Firebase Authentication
                registerUserDriver();
            }
        });

        //Return to account selection method
        mReturnToSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DriverLoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        mDropoffLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DriverLoginActivity.this, DriverMapActivity.class);
                startActivity(intent);
            }
        });
    }

    /*
    //Create user method
    private void createUser(){
        final String email = mEmail.getText().toString();
        final String password = mPassword.getText().toString();

        if (TextUtils.isEmpty(email)){
            mEmail.setError("Email cannot be empty");
            mEmail.requestFocus();
        }
        else if (TextUtils.isEmpty(password)){
            mPassword.setError("Password cannot be empty");
            mPassword.requestFocus();
        }
        else {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    String user_id = mAuth.getCurrentUser().getUid();
                    DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(user_id).child("name");
                    current_user_db.setValue(email);
                    Toast.makeText(DriverLoginActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(DriverLoginActivity.this, DriverMapActivity.class));
                 }
                else {
                    Toast.makeText(DriverLoginActivity.this, "Registration Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

     */

    //Register driver and enter their details into Firebase Firestore database
    private void registerUserDriver() {
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

        //Create the user using their email and password
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            //If Authentication is successful display this to the user
                            Toast.makeText(DriverLoginActivity.this,"Account Created",Toast.LENGTH_SHORT).show();
                            DocumentReference dbDriver = mFirestore.collection("Drivers").document(user.getUid());

                            Map<String,Object> userEmail = new HashMap<>();
                            userEmail.put("Email",mEmail.getText().toString());
                            dbDriver.set(userEmail);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(DriverLoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    //Check to see the users auth state at app startup
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthListener);
    }

    //Check to see the users auth state at app closing
    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthListener);
    }
}

