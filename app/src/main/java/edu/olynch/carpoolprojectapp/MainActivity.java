package edu.olynch.carpoolprojectapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    //Create variables
    private ImageButton mDriver, mRider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialse variables
        mDriver = findViewById(R.id.driverButton);
        mRider = findViewById(R.id.riderButton);

        //When user selects the driver button send them to the driver registration/login screen
        mDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DriverLoginActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        });

        //When user selects the passenger button send them to the driver registration/login screen
        mRider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RiderLoginActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        });
    }
}