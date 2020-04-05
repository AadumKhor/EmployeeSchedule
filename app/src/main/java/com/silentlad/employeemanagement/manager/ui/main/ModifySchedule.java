package com.silentlad.employeemanagement.manager.ui.main;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.silentlad.employeemanagement.R;
import com.silentlad.employeemanagement.data.ModifyScheduleCard;
import com.silentlad.employeemanagement.data.access.DayAccess;
import com.silentlad.employeemanagement.data.access.EmployeeAccess;
import com.silentlad.employeemanagement.data.access.EmployeePositionAccess;
import com.silentlad.employeemanagement.data.access.ScheduleAccess;
import com.silentlad.employeemanagement.data.contracts.ScheduleContract;
import com.silentlad.employeemanagement.manager.ManagerAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ModifySchedule extends Fragment {
    private ArrayList<ModifyScheduleCard> scheduleList = new ArrayList<ModifyScheduleCard>();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ManagerAdapter adapter;
    private ScheduleAccess scheduleAccess;
    private EmployeePositionAccess employeePositionAccess;
    private EmployeeAccess employeeAccess;
    private DayAccess dayAccess;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_modify_schedule, container, false);
        recyclerView = root.findViewById(R.id.modify_recycler);
        adapter = new ManagerAdapter(scheduleList, getContext());

        scheduleAccess = ScheduleAccess.getInstance(getContext());
        employeePositionAccess = EmployeePositionAccess.getInstance(getContext());
        employeeAccess = EmployeeAccess.getInstance(getContext());
        dayAccess = DayAccess.getInstance(getContext());

        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        createList();
        return root;
    }

    private void createList() {
        Cursor cursor = scheduleAccess.getData();
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                // get all data from schedule db
                String sId = cursor.getString(cursor.getColumnIndex(ScheduleContract.ScheduleEntry.COLUMN_ID));
                String posId = cursor.getString(cursor.getColumnIndex(ScheduleContract.ScheduleEntry.COLUMN_POSITION_ID));
                String startTime = cursor.getString(cursor.getColumnIndex(ScheduleContract.ScheduleEntry.COLUMN_START_TIME));
                String endTime = cursor.getString(cursor.getColumnIndex(ScheduleContract.ScheduleEntry.COLUMN_END_TIME));

                // use posId to generate position and empId
                String empId = employeePositionAccess.getEmpId(posId);
                String position = employeePositionAccess.getPosition(posId);
                // use empId to get full Name from employee db
                String fullName = employeeAccess.getName(empId.trim());

                // use empId to get working days
                String[] daysOfWeekArray = dayAccess.getDays(empId);
                String daysOfWeekString = "";

                for (String s : daysOfWeekArray) {
                    daysOfWeekString = daysOfWeekString.concat(s + ", ");
                }

                scheduleList.add(new ModifyScheduleCard(sId, daysOfWeekString, startTime,
                        endTime, position, fullName));
            }
        }
        adapter.notifyDataSetChanged();
    }
}
