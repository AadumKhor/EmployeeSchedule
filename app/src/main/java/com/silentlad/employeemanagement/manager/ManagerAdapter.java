package com.silentlad.employeemanagement.manager;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.silentlad.employeemanagement.R;
import com.silentlad.employeemanagement.data.ModifyScheduleCard;

import java.util.ArrayList;

public class ManagerAdapter extends RecyclerView.Adapter<ManagerAdapter.ManagerViewHolder> {
    private ArrayList<ModifyScheduleCard> mArrayList;
    private Context context;

    public ManagerAdapter(ArrayList<ModifyScheduleCard> arrayList, Context context) {
        this.mArrayList = arrayList;
        this.context = context;
    }

    public static class ManagerViewHolder extends RecyclerView.ViewHolder {
        private TextView daysOfTheWeek;
        private TextView startTime;
        private TextView endTime;
        private TextView position;
        private TextView name;
        private Button editButton;

        public ManagerViewHolder(@NonNull View itemView) {
            super(itemView);
            daysOfTheWeek = itemView.findViewById(R.id.schedule_day_value);
            startTime = itemView.findViewById(R.id.schedule_start_value);
            endTime = itemView.findViewById(R.id.schedule_end_value);
            position = itemView.findViewById(R.id.schedule_emp_pos_value);
            name = itemView.findViewById(R.id.schedule_emp_name_value);
            editButton = itemView.findViewById(R.id.edit_button);

//            editButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });
        }
    }

    @NonNull
    @Override
    public ManagerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.modify_schedule_card, parent, false);
        return new ManagerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ManagerViewHolder holder, int position) {
        final ModifyScheduleCard currentCard = mArrayList.get(position);

        holder.daysOfTheWeek.setText(currentCard.getDaysOfTheWeek());
        holder.name.setText(currentCard.getName());
        holder.startTime.setText(currentCard.getStartTime());
        holder.endTime.setText(currentCard.getEndTime());
        holder.position.setText(currentCard.getPosition());

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), ModifyScheduleActivity.class);
                intent.putExtra("startTime", currentCard.getStartTime());
                intent.putExtra("endTime", currentCard.getEndTime());
                intent.putExtra("daysOfWeek", currentCard.getDaysOfTheWeek());
                intent.putExtra("sId", currentCard.getSId());
                context.startActivity(intent);
            }
        });

        holder.itemView.setTag(currentCard.getSId());
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }
}
