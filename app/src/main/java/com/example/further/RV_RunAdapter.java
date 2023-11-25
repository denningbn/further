package com.example.further;

import android.content.Context;
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
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RV_RunAdapter.MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return runArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public MyViewHolder(@NonNull View itemView){
            super(itemView);

            TextView tv_distance, tv_pace, tv_date;

            tv_distance = itemView.findViewById(R.id.tv_distance);
            tv_pace = itemView.findViewById(R.id.tv_pace);
            tv_date = itemView.findViewById(R.id.tv_date);
        }
    }
}
