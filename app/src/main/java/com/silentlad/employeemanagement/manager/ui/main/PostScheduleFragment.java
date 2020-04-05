package com.silentlad.employeemanagement.manager.ui.main;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.silentlad.employeemanagement.R;
import com.silentlad.employeemanagement.data.Result;
import com.silentlad.employeemanagement.data.access.DayAccess;
import com.silentlad.employeemanagement.data.access.EmployeeAccess;
import com.silentlad.employeemanagement.data.access.EmployeePositionAccess;
import com.silentlad.employeemanagement.data.access.ScheduleAccess;

import java.util.HashMap;
import java.util.Random;

import ca.antonious.materialdaypicker.MaterialDayPicker;

public class PostScheduleFragment extends Fragment {
    private EditText posId;
    private TextView empId;
    private TextView name;
    private TextView position;
    private EditText startTime;
    private EditText endTime;
//    private MaterialDayPicker dayPicker;
//
//    private HashMap<String, Boolean> daysMap = new HashMap<>();

    private EmployeePositionAccess employeePositionAccess;
    private EmployeeAccess employeeAccess;
    private DayAccess dayAccess;
    private ScheduleAccess scheduleAccess;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_post_schedule, container, false);

        // INIT VIEW
        posId = root.findViewById(R.id.post_posId_edit);
        empId = root.findViewById(R.id.post_emp_id_value);
        position = root.findViewById(R.id.post_position_value);
        startTime = root.findViewById(R.id.add_emp_start_time_edit);
        endTime = root.findViewById(R.id.add_emp_end_time_edit);
        name = root.findViewById(R.id.post_emp_name_value);
        Button postScheduleButton = root.findViewById(R.id.post_schedule_button);

        // DB INIT
        employeePositionAccess = EmployeePositionAccess.getInstance(getContext());
        employeeAccess = EmployeeAccess.getInstance(getContext());
        dayAccess = DayAccess.getInstance(getContext());
        scheduleAccess = ScheduleAccess.getInstance(getContext());

        // INIT DAYS MAP
//        daysMap.put("sunday", false);
//        daysMap.put("monday", false);
//        daysMap.put("tuesday", false);
//        daysMap.put("wednesday", false);
//        daysMap.put("thursday", false);
//        daysMap.put("friday", false);
//        daysMap.put("saturday", false);

        // LISTENER ON POSITION ID TO GET RELEVANT DETAILS AND VERIFY
        posId.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
                        || (actionId == EditorInfo.IME_ACTION_NEXT)) {
                    Log.println(Log.DEBUG, "position id", "Inside if");
                    if (posId.getText() != null) {
                        Log.println(Log.DEBUG, "position id", "Not null");
                        String empIdValue = employeePositionAccess.getEmpId(posId.getText().toString().trim());
                        Log.println(Log.DEBUG, "position id", empIdValue);
                        String positionValue = employeePositionAccess.getPosition(posId.getText().toString().trim());
                        Log.println(Log.DEBUG, "position id", positionValue);
                        Log.println(Log.DEBUG, "position id", empIdValue + positionValue);
                        String fullName = employeeAccess.getName(empIdValue.trim());

                        empId.setText(empIdValue);
                        position.setText(positionValue.trim());
                        name.setText(fullName);
                    } else {
                        Toast.makeText(getContext(), "Please enter posId!", Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
            }
        });

        // DAY PRESSED LISTENER
//        dayPicker.setDayPressedListener(new MaterialDayPicker.DayPressedListener() {
//            @Override
//            public void onDayPressed(@NonNull MaterialDayPicker.Weekday weekday, boolean b) {
//                daysMap.put(weekday.toString().toLowerCase(), b);
//            }
//        });

        // BUTTON LISTENER
        postScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // ADD DAY PICKER CHECK LATER ACCORDING TO TRENDS
                if (!posId.getText().toString().equals("") && !startTime.getText().toString().equals("")
                        && !startTime.getText().toString().equals("")) {
                    Result result = postNewSchedule();
                    if (result instanceof Result.Success) {
                        Toast.makeText(getContext(), "Schedule posted!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Some error occurred. Try again", Toast.LENGTH_SHORT).show();
                    }
                    posId.getText().clear();
                    empId.setText(R.string.enter_position_id);
                    name.setText(R.string.enter_position_id);
                    position.setText(R.string.enter_position_id);
                    startTime.getText().clear();
                    endTime.getText().clear();
//                    dayPicker.clearSelection();
                } else {
                    Toast.makeText(getContext(), "Please enter valid data", Toast.LENGTH_SHORT).show();
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

    private Result postNewSchedule() {
        String sId = random(15);
        String empIdValue = empId.getText().toString().trim();
        String posIdValue = posId.getText().toString().trim();
        String sTime = startTime.getText().toString().trim();
        String eTime = endTime.getText().toString().trim();

        return scheduleAccess.insertSchedule(sId, posIdValue, sTime, eTime);
    }
}