package edu.olynch.carpoolprojectapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HistoryModel_RecyclerViewAdapter extends RecyclerView.Adapter<HistoryModel_RecyclerViewAdapter.MyViewHolder> {

    Context context;
    ArrayList<HistoryModel> historyModels;

    public HistoryModel_RecyclerViewAdapter(Context context, ArrayList<HistoryModel> historyModels) {
        this.context = context;
        this.historyModels = historyModels;
    }

    @NonNull
    @Override
    public HistoryModel_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflate the layout and give the look to the rows
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_row, parent, false);

        return new HistoryModel_RecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryModel_RecyclerViewAdapter.MyViewHolder holder, int position) {
        //Assign values to each of the rows as they come back onto the screen
        //Based on the position of the RecyclerView

        holder.tvName.setText(historyModels.get(position).getName());
        holder.tvDestination.setText(historyModels.get(position).getDestination());
        holder.tvJourneyLength.setText(historyModels.get(position).getJourneyLength());
        holder.imageView.setImageResource(historyModels.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        //Wants to know the number of items that you want to display
        return historyModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        //Grabbing the views from the recycler_view_row layout file

        ImageView imageView;
        TextView tvName, tvDestination, tvJourneyLength;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.personImage);
            tvName = itemView.findViewById(R.id.name);
            tvDestination = itemView.findViewById(R.id.destination);
            tvJourneyLength = itemView.findViewById(R.id.journeyLength);
        }
    }
}
