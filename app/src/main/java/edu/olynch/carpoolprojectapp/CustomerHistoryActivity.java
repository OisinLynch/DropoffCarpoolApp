package edu.olynch.carpoolprojectapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.olynch.carpoolprojectapp.customerHistoryRecyclerView.HistoryAdapter;
import edu.olynch.carpoolprojectapp.customerHistoryRecyclerView.HistoryObject;

public class CustomerHistoryActivity extends AppCompatActivity {

    private String customerOrDriver, userId;

    private RecyclerView mCustomerHistoryRecyclerView;
    private RecyclerView.Adapter mCustomerHistoryAdapter;
    private RecyclerView.LayoutManager mCustomerHistoryLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_history);

        mCustomerHistoryRecyclerView = (RecyclerView) findViewById(R.id.customerHistoryRecyclerView);
        mCustomerHistoryRecyclerView.setNestedScrollingEnabled(false);
        mCustomerHistoryRecyclerView.setHasFixedSize(true);
        mCustomerHistoryLayoutManager = new LinearLayoutManager(CustomerHistoryActivity.this);
        mCustomerHistoryRecyclerView.setLayoutManager(mCustomerHistoryLayoutManager);
        mCustomerHistoryAdapter = new HistoryAdapter(getDataSetCustomerHistory(), CustomerHistoryActivity.this);
        mCustomerHistoryRecyclerView.setAdapter(mCustomerHistoryAdapter);

        customerOrDriver = getIntent().getExtras().getString("customerOrDriver");
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        getUserHistoryId();

        //Temporary show mock history
        /*for (int i = 0; i < 100; i++) {
            HistoryObject obj = new HistoryObject(Integer.toString(i));
            resultHistory.add(obj);
        }

        mCustomerHistoryAdapter.notifyDataSetChanged(); */
    }

    private void getUserHistoryId() {
        DatabaseReference userHistoryDb = FirebaseDatabase.getInstance().getReference().child("Users").child(customerOrDriver).child(userId).child("history");
        userHistoryDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot history : snapshot.getChildren()) {
                        fetchLiftInformation(history.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void fetchLiftInformation(String liftKey) {
        DatabaseReference historyDb = FirebaseDatabase.getInstance().getReference().child("history").child(liftKey);
        historyDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String liftId = snapshot.getKey();
                    HistoryObject obj = new HistoryObject(liftId);
                    resultHistory.add(obj);
                    mCustomerHistoryAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private ArrayList resultHistory = new ArrayList<HistoryObject>();
    private ArrayList<HistoryObject> getDataSetCustomerHistory() {
        return resultHistory;
    }
}