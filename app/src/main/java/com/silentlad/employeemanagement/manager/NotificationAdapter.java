package com.silentlad.employeemanagement.manager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.silentlad.employeemanagement.R;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {
    private ArrayList<NotificationClass> mArrayList;

    public NotificationAdapter(ArrayList<NotificationClass> arrayList){
        this.mArrayList = arrayList;
    }

    static class NotificationViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.main_text_view);
        }
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.time_off_card, parent, false);
        return new NotificationViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        NotificationClass currentItem = mArrayList.get(position);

        String fullName = currentItem.getFullName();
        String date = currentItem.getDate();

        String finalString = fullName + " has requested time-off on date: "+date;
        holder.textView.setText(finalString);
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }
}
