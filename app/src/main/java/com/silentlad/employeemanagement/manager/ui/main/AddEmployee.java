package com.silentlad.employeemanagement.manager.ui.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.silentlad.employeemanagement.R;
import com.silentlad.employeemanagement.data.Result;
import com.silentlad.employeemanagement.data.access.DayAccess;
import com.silentlad.employeemanagement.data.access.EmployeeAccess;
import com.silentlad.employeemanagement.data.access.EmployeePositionAccess;
import com.silentlad.employeemanagement.data.access.ScheduleAccess;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

import ca.antonious.materialdaypicker.MaterialDayPicker;

public class AddEmployee extends Fragment {
    private HashMap<String, Boolean> daysMap = new HashMap<String, Boolean>();
    private EditText firstName;
    private EditText lastName;
    private EditText position;
    private EditText startTime;
    private EditText endTime;

    private SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss", Locale.US);

    private EmployeeAccess employeeAccess;
    private EmployeePositionAccess employeePositionAccess;
    private DayAccess dayAccess;
    private ScheduleAccess scheduleAccess;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_add_employee, container, false);

        employeeAccess = EmployeeAccess.getInstance(getContext());
        employeePositionAccess = EmployeePositionAccess.getInstance(getContext());
        dayAccess = DayAccess.getInstance(getContext());
        scheduleAccess = ScheduleAccess.getInstance(getContext());

        daysMap.put("sunday",false);
        daysMap.put("monday",false);
        daysMap.put("tuesday",false);
        daysMap.put("wednesday",false);
        daysMap.put("thursday",false);
        daysMap.put("friday",false);
        daysMap.put("saturday",false);

        firstName = root.findViewById(R.id.add_emp_f_edit);
        lastName = root.findViewById(R.id.add_emp_l_edit);
        position = root.findViewById(R.id.post_posId__edit);
        startTime = root.findViewById(R.id.add_emp_start_time_edit);
        endTime = root.findViewById(R.id.add_emp_end_time_edit);

        Button button = root.findViewById(R.id.add_employee_button);
        MaterialDayPicker dayPicker = root.findViewById(R.id.materialDayPicker);
        dayPicker.setLocale(Locale.US);

        dayPicker.setDayPressedListener(new MaterialDayPicker.DayPressedListener() {
            @Override
            public void onDayPressed(@NonNull MaterialDayPicker.Weekday weekday, boolean b) {
                daysMap.put(weekday.toString().toLowerCase(), b);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    addDataToDb();
                } catch (ParseException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Some error occurred", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return root;
    }


    private String random(int maxLength) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        char tempChar;
        for (int i = 0; i < maxLength; i++) {
            tempChar = (char) (random.nextInt(25) + 97);
            sb.append(tempChar);
        }
        return sb.toString();
    }

    private void addDataToDb() throws ParseException {
        final String fName = firstName.getText().toString();
        final String lName = lastName.getText().toString();
        final String pos = position.getText().toString();
        final String sTime = Objects.requireNonNull(timeFormat.parse(startTime.getText().toString())).toString().split(" ")[3];
        final String eTime = Objects.requireNonNull(timeFormat.parse(endTime.getText().toString())).toString().split(" ")[3];

        boolean isDataValid = !fName.equals("") && !lName.equals("") && !pos.equals("")
                && !sTime.equals("") && !eTime.equals("");

        if (isDataValid) {
            String empId = random(6);
            String posId = random(8);
            Result result1 = employeeAccess.insertNewEmployee(empId, fName, lName);
            Result result2 = employeePositionAccess.insertData(posId, empId, pos);
            Result result3 = dayAccess.insertDayData(random(15), empId,
                    daysMap.get("sunday"), daysMap.get("monday"), daysMap.get("tuesday"),
                    daysMap.get("wednesday"), daysMap.get("thursday")
                    , daysMap.get("friday"), daysMap.get("saturday"));
            Result result4 = scheduleAccess.insertSchedule(random(15), posId, sTime, eTime);

            if (result1 instanceof Result.Success && result2 instanceof Result.Success
                    && result3 instanceof Result.Success && result4 instanceof Result.Success) {
                Toast.makeText(getContext(), "All databases updated", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getContext(), "Some databases could not be updated.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "Please enter valid data.", Toast.LENGTH_SHORT).show();
        }
    }
}
