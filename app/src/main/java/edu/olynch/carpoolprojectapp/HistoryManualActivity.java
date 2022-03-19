package edu.olynch.carpoolprojectapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.akexorcist.googledirection.model.Line;

import java.util.ArrayList;

public class HistoryManualActivity extends AppCompatActivity {

    ArrayList<HistoryModel> historyModels = new ArrayList<>();

    int [] personImages = {R.drawable.man1, R.drawable.man2, R.drawable.woman1, R.drawable.man3, R.drawable.woman2, R.drawable.woman3, R.drawable.man4};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_manual);

        RecyclerView recyclerView = findViewById(R.id.mRecyclerView);

        setupHistoryModels();

        HistoryModel_RecyclerViewAdapter adapter = new HistoryModel_RecyclerViewAdapter(this, historyModels);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupHistoryModels() {
        String[] names = getResources().getStringArray(R.array.name);
        String[] destination = getResources().getStringArray(R.array.destinationHistory);
        String[] journeyLength = getResources().getStringArray(R.array.journey_length);

        for (int i = 0; i < names.length; i++) {
            historyModels.add(new HistoryModel(names[i], destination[i], journeyLength[i], personImages[i]));
        }
    }
}