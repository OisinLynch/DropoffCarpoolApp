package edu.olynch.carpoolprojectapp.customerHistoryRecyclerView;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.olynch.carpoolprojectapp.R;

public class HistoryViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView liftId;

    public HistoryViewHolders(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        liftId = (TextView) itemView.findViewById(R.id.liftId);
    }

    @Override
    public void onClick(View view) {

    }
}
