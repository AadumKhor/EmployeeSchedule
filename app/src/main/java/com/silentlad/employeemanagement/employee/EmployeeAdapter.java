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
    private ActualTimingHelper mTimingHelper;
    private String mEmpId;

    public EmployeeAdapter(String EmpId, ArrayList<ScheduleCard> arrayList, ActualTimingHelper mTimingHelper) {
        this.mArrayList = arrayList;
        this.mTimingHelper = mTimingHelper;
        this.mEmpId = EmpId;
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
//        holder.timeOffButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(v.getContext(), "Mail sent to manager.", Toast.LENGTH_SHORT).show();
//            }
//        });
//
        holder.clockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.clockButton.getText().equals(String.valueOf(R.string.clock_in))) {

                    Result result = mTimingHelper.insertData(mEmpId,
                            String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())));

                    if (result instanceof Result.Success) {
                        Toast.makeText(v.getContext(), "Clocked IN!", Toast.LENGTH_LONG).show();
                        holder.clockButton.setBackgroundResource(R.color.clockOut);
                        holder.clockButton.setText(R.string.clock_out);
                    }else{
                        Toast.makeText(v.getContext(), "Some error occurred.", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Result result = mTimingHelper.updateData(mEmpId,
                            String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())));

                    if (result instanceof Result.Success) {
                        Toast.makeText(holder.itemView.getContext(), "Clocked OUT!", Toast.LENGTH_LONG).show();
                        holder.clockButton.setBackgroundResource(R.color.clockedOut);
                        holder.clockButton.setText(R.string.clocked_out);
                    }else{
                        Toast.makeText(holder.itemView.getContext(), "Some error occurred.", Toast.LENGTH_LONG).show();
                    }
                }
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
