package com.silentlad.employeemanagement.manager.ui.main;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import androidx.lifecycle.ViewModelProviders;

import com.silentlad.employeemanagement.R;
import com.silentlad.employeemanagement.UtilityFunctions;
import com.silentlad.employeemanagement.data.Result;
import com.silentlad.employeemanagement.data.access.DayAccess;
import com.silentlad.employeemanagement.data.access.EmployeeAccess;
import com.silentlad.employeemanagement.data.access.EmployeePositionAccess;
import com.silentlad.employeemanagement.data.access.ScheduleAccess;

import java.io.IOException;
import java.util.HashMap;

import ca.antonious.materialdaypicker.MaterialDayPicker;

/**
 * A placeholder fragment containing a simple view.
 */
public class PostScheduleFragment extends Fragment {
    private EditText posId;
    private TextView empId;
    private TextView position;
    private EditText startTime;
    private EditText endTime;
    private TextView name;
    private MaterialDayPicker dayPicker;
    private Button postScheduleButton;

    private EmployeePositionAccess employeePositionAccess;
    private EmployeeAccess employeeAccess;
    private DayAccess dayAccess;
    private ScheduleAccess scheduleAccess;

    private HashMap<String, Boolean> daysMap = new HashMap<>();
    private UtilityFunctions utilityFunctions;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_post_schedule, container, false);

        // INIT VIEW
        posId = root.findViewById(R.id.post_posId__edit);
        empId = root.findViewById(R.id.post_emp_id_value);
        position = root.findViewById(R.id.post_position_value);
        startTime = root.findViewById(R.id.add_emp_start_time_edit);
        endTime = root.findViewById(R.id.add_emp_end_time_edit);
        name = root.findViewById(R.id.post_emp_name_value);
        dayPicker = root.findViewById(R.id.post_materialDayPicker);
        postScheduleButton = root.findViewById(R.id.post_button);
        utilityFunctions = new UtilityFunctions();

        // DB INIT
        employeePositionAccess = EmployeePositionAccess.getInstance(getContext());
        employeeAccess = EmployeeAccess.getInstance(getContext());
        dayAccess = DayAccess.getInstance(getContext());
        scheduleAccess = ScheduleAccess.getInstance(getContext());

        // INIT DAYS MAP
        daysMap.put("sunday", false);
        daysMap.put("monday", false);
        daysMap.put("tuesday", false);
        daysMap.put("wednesday", false);
        daysMap.put("thursday", false);
        daysMap.put("friday", false);
        daysMap.put("saturday", false);

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
                        Log.println(Log.DEBUG, "position id", empIdValue+positionValue);
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
        dayPicker.setDayPressedListener(new MaterialDayPicker.DayPressedListener() {
            @Override
            public void onDayPressed(@NonNull MaterialDayPicker.Weekday weekday, boolean b) {
                daysMap.put(weekday.toString().toLowerCase(), b);
            }
        });

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
                    dayPicker.clearSelection();
                } else {
                    Toast.makeText(getContext(), "Please enter valid data", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return root;
    }

    private Result postNewSchedule() {
        String posIdValue = posId.getText().toString();
        String empIdValue = empId.getText().toString();
        String startTimeValue = startTime.getText().toString();
        String endTimeValue = endTime.getText().toString();
        String sId= utilityFunctions.random(15);

        Result result1 = scheduleAccess.insertSchedule(sId, posIdValue, startTimeValue, endTimeValue);
        Result result2 = dayAccess.insertDayData(utilityFunctions.random(15), empIdValue,sId,
                daysMap.get("sunday"), daysMap.get("monday"), daysMap.get("tuesday"), daysMap.get("wednesday"),
                daysMap.get("thursday"), daysMap.get("friday"), daysMap.get("saturday")
        );

        if (result1 instanceof Result.Success && result2 instanceof Result.Success) {
            return new Result.Success<>("Posted new schedule");
        } else {
            return new Result.Error(new IOException("Could not post schedule"));
        }
    }
}