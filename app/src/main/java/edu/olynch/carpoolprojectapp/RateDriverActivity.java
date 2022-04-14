package edu.olynch.carpoolprojectapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class RateDriverActivity extends AppCompatActivity {

    Button mSubmitRating;
    RatingBar mStarRating;
    float myRating = 0;
    String message = null;
    EditText mFeedback;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_driver);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        mSubmitRating = (Button) findViewById(R.id.btnSubmitRating);
        mStarRating = (RatingBar) findViewById(R.id.starRating);
        mFeedback = (EditText) findViewById(R.id.etRatingFeedback);

        mStarRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

                int rating = (int) v;
                //String message = null;

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
                uploadRating();

                Intent intent = new Intent(RateDriverActivity.this, CustomerMapActivity.class);
                startActivity(intent);
            }
        });
    }

    //Upload rating for feedback to Firestore Database
    private void uploadRating() {
        FirebaseUser user = mAuth.getCurrentUser();
        DocumentReference dbRatings = mFirestore.collection("Driver Ratings").document(user.getUid());

        Map<String,Object> userRating = new HashMap<>();
        userRating.put("Rating",myRating);
        userRating.put("Message",message);
        userRating.put("Feedback",mFeedback.getText().toString());
        //Merge allows the data to be added to the current collection and not overwrite pre existing data
        dbRatings.set(userRating, SetOptions.merge());
    }


}