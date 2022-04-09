package edu.olynch.carpoolprojectapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

public class RateDriverActivity extends AppCompatActivity {

    Button mSubmitRating;
    RatingBar mStarRating;
    float myRating = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_driver);

        mSubmitRating = (Button) findViewById(R.id.btnSubmitRating);
        mStarRating = (RatingBar) findViewById(R.id.starRating);

        mStarRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

                int rating = (int) v;
                String message = null;

                myRating = ratingBar.getRating();

                switch (rating) {
                    case 1:
                        Toast.makeText(RateDriverActivity.this, "Poor Service", Toast.LENGTH_SHORT).show();
                        message = "Poor Service";
                        break;
                    case 2:
                        Toast.makeText(RateDriverActivity.this, "Adequate Service", Toast.LENGTH_SHORT).show();
                        message = "Adequate Service";
                        break;
                    case 3:
                        Toast.makeText(RateDriverActivity.this, "Average Service", Toast.LENGTH_SHORT).show();
                        message = "Average Service";
                        break;
                    case 4:
                        Toast.makeText(RateDriverActivity.this, "Good Service", Toast.LENGTH_SHORT).show();
                        message = "Good Service";
                        break;
                    case 5:
                        Toast.makeText(RateDriverActivity.this, "Great Service", Toast.LENGTH_SHORT).show();
                        message = "Great Service";
                        break;
                }

                Toast.makeText(RateDriverActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });

        mSubmitRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RateDriverActivity.this, CustomerMapActivity.class);
                startActivity(intent);
            }
        });
    }
}