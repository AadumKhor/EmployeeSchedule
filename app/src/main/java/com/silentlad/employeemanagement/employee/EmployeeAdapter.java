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
import com.silentlad.employeemanagement.data.ScheduleCard;

import java.util.ArrayList;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder> {
    private ArrayList<ScheduleCard> mArrayList;

    public EmployeeAdapter(ArrayList<ScheduleCard> arrayList){
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

            clockButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clockButton.setBackgroundResource(R.color.clockOut);
                }
            });
        }
    }

    @NonNull
    @Override
    public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_card, parent, false);
        return new EmployeeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeViewHolder holder, int position) {
        holder.timeOffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Mail sent to manager.", Toast.LENGTH_SHORT).show();
            }
        });
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
