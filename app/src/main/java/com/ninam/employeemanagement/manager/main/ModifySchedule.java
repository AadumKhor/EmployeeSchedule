package com.ninam.employeemanagement.manager.main;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ninam.employeemanagement.R;
import com.ninam.employeemanagement.UtilityFunctions;
import com.ninam.employeemanagement.data.ModifyScheduleCard;
import com.ninam.employeemanagement.data.access.DayAccess;
import com.ninam.employeemanagement.data.access.EmployeeAccess;
import com.ninam.employeemanagement.data.access.EmployeePositionAccess;
import com.ninam.employeemanagement.data.access.ScheduleAccess;
import com.ninam.employeemanagement.data.contracts.ScheduleContract;
import com.ninam.employeemanagement.manager.ManagerAdapter;

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

    private UtilityFunctions utilityFunctions = new UtilityFunctions();

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
                int[] daysOfWeekArray = dayAccess.getDaysForSchedule(empId, sId);
                String daysOfWeekString = "";
                String daysOfWeekDisplay = "";

                for (String s : utilityFunctions.getDaysFromInt(daysOfWeekArray)) {
                    daysOfWeekString = daysOfWeekString.concat(s + ", ");
                    daysOfWeekDisplay = daysOfWeekDisplay.concat(s.substring(0, 3) + ", ");
                }

                scheduleList.add(new ModifyScheduleCard(sId, daysOfWeekDisplay, daysOfWeekString, startTime,
                        endTime, position, fullName));
                adapter.notifyDataSetChanged();
            }
        }

    }
}
