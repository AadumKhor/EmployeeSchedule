package com.ninam.employeemanagement.manager;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ninam.employeemanagement.R;
import com.ninam.employeemanagement.data.Result;
import com.ninam.employeemanagement.data.access.DayAccess;
import com.ninam.employeemanagement.data.access.EmployeeAccess;
import com.ninam.employeemanagement.data.access.EmployeePositionAccess;
import com.ninam.employeemanagement.data.access.ScheduleAccess;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import ca.antonious.materialdaypicker.MaterialDayPicker;

public class ModifyScheduleActivity extends AppCompatActivity {
    private TextView nameValue;
    private TextView positionValue;
    private EditText startTimeValue;
    private EditText endTimeValue;
    private MaterialDayPicker dayPicker;
    private Button editSchedule;
    private Button deleteSchedule;

    private String name = "";
    private String sId = "";
    private String position = "";
    private String startTime = "";
    private String endTime = "";
    private String daysOfWeek = "";
    private HashMap<String, Integer> daysMap = new HashMap<>();

    private ScheduleAccess scheduleAccess;
    private DayAccess dayAccess;
    private EmployeeAccess employeeAccess;
    private EmployeePositionAccess employeePositionAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_schedule);

        // INIT UI ELEMENTS
        nameValue = findViewById(R.id.edit_full_name_value);
        positionValue = findViewById(R.id.edit_position_value);
        startTimeValue = findViewById(R.id.edit_start_time);
        endTimeValue = findViewById(R.id.edit_end_time);
        dayPicker = findViewById(R.id.edit_materialDayPicker);
        editSchedule = findViewById(R.id.edit_schedule_button);
        deleteSchedule = findViewById(R.id.edit_delete_button);

        // INIT DB
        employeeAccess = EmployeeAccess.getInstance(this);
        employeePositionAccess = EmployeePositionAccess.getInstance(this);
        dayAccess = DayAccess.getInstance(this);
        scheduleAccess = ScheduleAccess.getInstance(this);

        // GET DATA FROM INTENT
        name = getIntent().getStringExtra("name");
        sId = getIntent().getStringExtra("sId");
        position = getIntent().getStringExtra("position");
        startTime = getIntent().getStringExtra("startTime");
        endTime = getIntent().getStringExtra("endTime");

        // FILL IN MAP0)
        daysMap.put("sunday",0);
        daysMap.put("monday", 0);
        daysMap.put("tuesday", 0);
        daysMap.put("wednesday", 0);
        daysMap.put("thursday", 0);
        daysMap.put("friday", 0);
        daysMap.put("saturday", 0);
        String[] days = Objects.requireNonNull(getIntent().getStringExtra("daysOfWeek")).split(", ");

//        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);

        for (String day : days) {
            daysMap.put(day, 1);
        }

        // SET UI ELEMENTS
        nameValue.setText(name);
        positionValue.setText(position);
        startTimeValue.setText(startTime);
        endTimeValue.setText(endTime);

        // SET SELECTED DAYS BY USING AN ITERATOR OVER THE DAYS MAP
        ArrayList<MaterialDayPicker.Weekday> weekdays = new ArrayList<>();

        for (HashMap.Entry<String, Integer> currentItem : daysMap.entrySet()) {
            if (currentItem.getValue() == 1 && !currentItem.getKey().equals("")) {
                weekdays.add(MaterialDayPicker.Weekday.valueOf(currentItem.getKey().toUpperCase()));
            }
        }

        dayPicker.setSelectedDays(weekdays);

        dayPicker.setDayPressedListener(new MaterialDayPicker.DayPressedListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDayPressed(@NonNull MaterialDayPicker.Weekday weekday, boolean b) {
                daysMap.put(weekday.toString().toLowerCase(), b ? 1 : 0);

            }
        });

        editSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dayPicker.getSelectedDays().size() != 0
                        && !startTimeValue.getText().toString().equals("")
                        && !endTimeValue.getText().toString().equals("")) {
                    Result result = updateSchedule();
                    if (result instanceof Result.Success) {
                        Toast.makeText(getApplicationContext(), "Databases updated", Toast.LENGTH_SHORT).show();
                        startTimeValue.getText().clear();
                        endTimeValue.getText().clear();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Some error occurred", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter valid data", Toast.LENGTH_SHORT).show();
                }
            }
        });

        deleteSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Result result = deleteEntry();

                if (result instanceof Result.Success) {
                    Toast.makeText(getApplicationContext(), "Data deleted successfully.", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Data could not be deleted", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private Result deleteEntry() {
        Result result1 = scheduleAccess.deleteEntry(sId);
        Result result2 = dayAccess.deleteDaySchedule(sId);

        if (result1 instanceof Result.Success && result2 instanceof Result.Success) {
            return new Result.Success<>("Data deleted ");
        } else {
            return new Result.Error(new IOException("Data could not be deleted"));
        }
    }

    private Result updateSchedule() {
        Result result1 = scheduleAccess.updateSchedule(sId,
                startTimeValue.getText().toString(),
                endTimeValue.getText().toString());
        String posId = scheduleAccess.getPosId(sId);
        String empId = employeePositionAccess.getEmpId(posId);

        int monday = daysMap.get("monday");
        int tuesday = daysMap.get("tuesday");
        int wednesday = daysMap.get("wednesday");
        int thursday = daysMap.get("thursday");
        int friday = daysMap.get("friday");
        int saturday = daysMap.get("saturday");
        int sunday = daysMap.get("sunday");

        Result result2 = dayAccess.updateDayData(empId, sId, sunday, monday, tuesday, wednesday, thursday, friday, saturday);

        if (result1 instanceof Result.Success && result2 instanceof Result.Success) {
            Log.println(Log.DEBUG, "result", (String) ((Result.Success) result2).getData());
            return new Result.Success<>("Data updated");
        } else {
            return new Result.Error(new IOException("Error updating data."));
        }
    }
}
