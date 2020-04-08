package com.ninam.employeemanagement.employee;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.ninam.employeemanagement.R;
import com.ninam.employeemanagement.data.Result;
import com.ninam.employeemanagement.data.ScheduleCard;
import com.ninam.employeemanagement.data.access.DayAccess;
import com.ninam.employeemanagement.data.access.EmployeeAccess;
import com.ninam.employeemanagement.data.access.EmployeePositionAccess;
import com.ninam.employeemanagement.data.access.ScheduleAccess;
import com.ninam.employeemanagement.data.contracts.EmployeePositionContract;
import com.ninam.employeemanagement.data.dbhelpers.ActualTimingHelper;
import com.ninam.employeemanagement.data.dbhelpers.NotificationsHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class EMainActivity extends AppCompatActivity {
    NotificationsHelper notificationsHelper;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

    private ArrayList<ScheduleCard> arrayList = new ArrayList<>();
    private EmployeeAdapter adapter;
    private String date = "";
    private String fullName = "";
    private String posId = "";
    private String position = "";
    private String empId = "";
    private HashMap<String, String> daysMaps = new HashMap<>();

    private EmployeeAccess employeeAccess;
    private EmployeePositionAccess employeePositionAccess;
    private ScheduleAccess scheduleAccess;
    private DayAccess dayAccess;
    private ActualTimingHelper timingHelper;

    long firstDayLong = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e_main);

        // GET EMP ID FROM INTENT
        empId = getIntent().getStringExtra("empId");

        timingHelper = new ActualTimingHelper(this);
        adapter = new EmployeeAdapter(arrayList);


        // ACTION BAR ACTIVATION AND TEXT
        final ActionBar actionBar = getSupportActionBar();
        date = dateFormat.format(Calendar.getInstance().getTime());
        assert actionBar != null;
        actionBar.setTitle("Employee Schedule - " + date);

        // CALENDER INIT
        final CompactCalendarView calendarView = findViewById(R.id.calender);
        calendarView.setUseThreeLetterAbbreviation(true);

        // DB INIT
        employeeAccess = EmployeeAccess.getInstance(getApplicationContext());
        employeePositionAccess = EmployeePositionAccess.getInstance(getApplicationContext());
        scheduleAccess = ScheduleAccess.getInstance(getApplicationContext());
        dayAccess = DayAccess.getInstance(getApplicationContext());
        notificationsHelper = new NotificationsHelper(EMainActivity.this);
        getFullNameAndPosId();

        Toast.makeText(getApplicationContext(), "Welcome " + fullName, Toast.LENGTH_SHORT).show();
//        fillDaysMaps();

        // SET GREETING TEXT
        TextView greetings = findViewById(R.id.greeting_text);
        greetings.setText(String.format("Hi! %s. Position : %s", fullName, position));

        createInitialList();

        // CALENDER WORKING
        calendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                adapter.notifyDataSetChanged();
                date = dateFormat.format(dateClicked);
                actionBar.setTitle("Employee Schedule - " + date);

                firstDayLong = dateClicked.getTime();
                long now = System.currentTimeMillis();

                if (firstDayLong - now >= 86400000 || firstDayLong - now >= -86399999) {
                    arrayList.clear();
                    SimpleDateFormat format = new SimpleDateFormat("EEEE", Locale.US);
                    String dayValue = "";

                    dayValue = Objects.requireNonNull(format.format(dateClicked));

                    daysMaps = dayAccess.getScheduleIdAndDayValue(empId, dayValue.toLowerCase());

                    if (daysMaps.size() > 0) {
                        for (HashMap.Entry<String, String> currentItem : daysMaps.entrySet()) {
                            String startTime = scheduleAccess.getScheduleWithScheduleId(currentItem.getKey())[0];
                            String endTime = scheduleAccess.getScheduleWithScheduleId(currentItem.getKey())[1];

                            arrayList.add(new ScheduleCard(dayValue, startTime, endTime));
                            adapter.notifyDataSetChanged();
                        }
                    } else {
                        arrayList.clear();
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    arrayList.clear();
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                adapter.notifyDataSetChanged();
                date = dateFormat.format(firstDayOfNewMonth);
                actionBar.setTitle("Employee Schedule - " + date);

                firstDayLong = firstDayOfNewMonth.getTime();
                long now = System.currentTimeMillis();
                if (firstDayLong - now >= 86399999) {
                    arrayList.clear();
                    SimpleDateFormat format = new SimpleDateFormat("EEEE", Locale.US);
                    String dayValue = "";
                    try {
                        dayValue = Objects.requireNonNull(format.format(firstDayOfNewMonth));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    daysMaps = dayAccess.getScheduleIdAndDayValue(empId, dayValue.toLowerCase());

                    if (daysMaps.size() > 0) {
                        for (HashMap.Entry<String, String> currentItem : daysMaps.entrySet()) {
                            String startTime = scheduleAccess.getScheduleWithScheduleId(currentItem.getKey())[0];
                            String endTime = scheduleAccess.getScheduleWithScheduleId(currentItem.getKey())[1];

                            arrayList.add(new ScheduleCard(dayValue, startTime, endTime));
                            adapter.notifyDataSetChanged();
                        }
                    } else {
                        arrayList.clear();
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    arrayList.clear();
                    adapter.notifyDataSetChanged();
                }
            }
        });
        // CREATE SCHEDULE LIST AND RECYCLER VIEW
        buildRecyclerView(adapter);

    }

    private void getFullNameAndPosId() {
        fullName = employeeAccess.getName(empId.trim());
        Cursor posCursor = employeePositionAccess.getEmpDetails(empId.trim());

        if (posCursor.getCount() != 0) {
            while (posCursor.moveToNext()) {
                posId = posCursor.getString(posCursor.getColumnIndex(EmployeePositionContract.EmployeePositionEntry.COLUMN_ID));
                position = posCursor.getString(posCursor.getColumnIndex(EmployeePositionContract.EmployeePositionEntry.COLUMN_POSITION));
            }
        }
    }

    private void buildRecyclerView(EmployeeAdapter mAdapter) {
        RecyclerView recyclerView = findViewById(R.id.schedule_of_this_day);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mAdapter);
    }

    private void createInitialList() {
        SimpleDateFormat format = new SimpleDateFormat("EEEE", Locale.US);
        String dayValue = "";

        dayValue = Objects.requireNonNull(format.format(Calendar.getInstance().getTime()));
        Log.println(Log.DEBUG, "getScheduleAndDay", dayValue.toLowerCase());
        daysMaps = dayAccess.getScheduleIdAndDayValue(empId, dayValue.toLowerCase());

        if (daysMaps.size() > 0) {
            for (HashMap.Entry<String, String> currentItem : daysMaps.entrySet()) {
                String startTime = scheduleAccess.getScheduleWithScheduleId(currentItem.getKey())[0];
                String endTime = scheduleAccess.getScheduleWithScheduleId(currentItem.getKey())[1];

                arrayList.add(new ScheduleCard(dayValue, startTime, endTime));
                adapter.notifyDataSetChanged();
            }
        } else {
            arrayList.clear();
            adapter.notifyDataSetChanged();
        }

    }

    public void notifyManager(View v) {
        Button timeOffButton = v.findViewById(R.id.schedule_time_off);

        timeOffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean alreadyExists = notificationsHelper.checkIfExists(empId, sdf.format(new Date().getTime())) != null;
                if (!alreadyExists) {
                    Result result = notificationsHelper.insertData(empId);

                    if (result instanceof Result.Success) {
                        Toast.makeText(getApplicationContext(), "Notified Manager!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Some error occurred!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }

    public void enterOrUpdateTime(View v) {
        Button clock = v.findViewById(R.id.schedule_clock);
        if (firstDayLong > System.currentTimeMillis()) {
            clock.setBackgroundResource(R.color.clockedOut);
            Toast.makeText(getApplicationContext(), "Can't clock future time!", Toast.LENGTH_LONG).show();
        } else {
            if (clock.getText().toString().equals("Clock In")) {
                Result result = timingHelper.insertData(empId,
                        String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())));

                if (result instanceof Result.Success) {
                    Toast.makeText(getApplicationContext(), "Clocked IN!", Toast.LENGTH_LONG).show();
                    clock.setBackgroundResource(R.color.clockOut);
                    clock.setText(R.string.clock_out);
                } else {
                    Toast.makeText(v.getContext(), "Some error occurred.", Toast.LENGTH_LONG).show();
                }
            } else {
                Result result = timingHelper.updateData(empId,
                        String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())));

                if (result instanceof Result.Success) {
                    Toast.makeText(getApplicationContext(), "Clocked OUT!", Toast.LENGTH_LONG).show();
                    clock.setBackgroundResource(R.color.clockedOut);
                    clock.setText(R.string.clocked_out);
                } else {
                    Toast.makeText(getApplicationContext(), "Some error occurred.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

}
