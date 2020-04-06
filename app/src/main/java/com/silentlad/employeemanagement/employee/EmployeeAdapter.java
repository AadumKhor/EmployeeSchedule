package com.silentlad.employeemanagement.employee;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.silentlad.employeemanagement.R;
import com.silentlad.employeemanagement.data.Result;
import com.silentlad.employeemanagement.data.ScheduleCard;
import com.silentlad.employeemanagement.data.dbhelpers.ActualTimingHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder> {
    private ArrayList<ScheduleCard> mArrayList;

    public EmployeeAdapter(ArrayList<ScheduleCard> arrayList) {
        this.mArrayList = arrayList;
    }

    public static class EmployeeViewHolder extends RecyclerView.ViewHolder {
        private TextView dayValue;
        private TextView startTime;
        private TextView endTime;
        private Button clockButton;
        private Button timeOffButton;

        public EmployeeViewHolder(@NonNull View itemView) {
            super(itemView);
            dayValue = itemView.findViewById(R.id.schedule_day_value);
            startTime = itemView.findViewById(R.id.schedule_start_value);
            endTime = itemView.findViewById(R.id.schedule_end_value);
            clockButton = itemView.findViewById(R.id.schedule_clock);
            timeOffButton = itemView.findViewById(R.id.schedule_time_off);
        }
    }

    @NonNull
    @Override
    public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_card, parent, false);
        return new EmployeeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final EmployeeViewHolder holder, int position) {
        ScheduleCard currentItem = mArrayList.get(position);

        holder.dayValue.setText(currentItem.getDayOfWeek());
        holder.startTime.setText(currentItem.getStartTime());
        holder.endTime.setText(currentItem.getEndTime());
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }
}
