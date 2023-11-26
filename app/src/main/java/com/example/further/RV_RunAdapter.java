package com.example.further;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RV_RunAdapter extends RecyclerView.Adapter<RV_RunAdapter.MyViewHolder> {

    Context context;
    ArrayList<Run> runArrayList;

    public RV_RunAdapter(Context context, ArrayList<Run> runArrayList){
        this.context = context;
        this.runArrayList = runArrayList;
    }

    @NonNull
    @Override
    public RV_RunAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.rv_run_row, parent, false);

        return new RV_RunAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RV_RunAdapter.MyViewHolder holder, int position) {
        holder.tv_date.setText(runArrayList.get(position).dateToString());
        holder.tv_distance.setText(Double.toString(runArrayList.get(position).getDistance()) + " Miles" );
        holder.tv_pace.setText(Double.toString(runArrayList.get(position).getPace()));
    }

    @Override
    public int getItemCount() {
        return runArrayList != null ? runArrayList.size(): 0;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tv_distance, tv_pace, tv_date;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);

            tv_distance = itemView.findViewById(R.id.tv_distance);
            tv_pace = itemView.findViewById(R.id.tv_pace);
            tv_date = itemView.findViewById(R.id.tv_date);
        }
    }
}
