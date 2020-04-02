package com.silentlad.employeemanagement.employee;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder> {


    public static class EmployeeViewHolder extends RecyclerView.ViewHolder {
        private TextView dayValue;
        private TextView startTime;
        private TextView endTime;
        private Button clockButton;
        private Button timeOffButton;

        public EmployeeViewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }

    @NonNull
    @Override
    public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
