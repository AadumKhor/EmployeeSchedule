package com.silentlad.employeemanagement.employee;

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

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.silentlad.employeemanagement.R;
import com.silentlad.employeemanagement.data.ScheduleCard;
import com.silentlad.employeemanagement.data.access.DayAccess;
import com.silentlad.employeemanagement.data.access.EmployeeAccess;
import com.silentlad.employeemanagement.data.access.EmployeePositionAccess;
import com.silentlad.employeemanagement.data.access.ScheduleAccess;
import com.silentlad.employeemanagement.data.contracts.EmployeePositionContract;
import com.silentlad.employeemanagement.data.dbhelpers.ActualTimingHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class EMainActivity extends AppCompatActivity {

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
    private Calendar currentCalender = Calendar.getInstance(Locale.getDefault());

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e_main);

        // GET EMP ID FROM INTENT
        empId = getIntent().getStringExtra("empId");

        timingHelper = new ActualTimingHelper(this);
        adapter = new EmployeeAdapter(empId, arrayList, timingHelper);


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
        getFullNameAndPosId();
//        fillDaysMaps();

        // SET GREETING TEXT
        TextView greetings = findViewById(R.id.greeting_text);
        greetings.setText(String.format("Hi! %s. Position : %s", fullName, position));

        // CALENDER WORKING
        calendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                if (dateClicked.getTime() >= System.currentTimeMillis()) {
                    arrayList.clear();
                    adapter.notifyDataSetChanged();
                    date = dateFormat.format(dateClicked);
                    actionBar.setTitle("Employee Schedule - " + date);
                    SimpleDateFormat format = new SimpleDateFormat("EEEE", Locale.US);
                    String dayValue = "";
                    try {
                        dayValue = Objects.requireNonNull(format.format(dateClicked));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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
                } else {
                    arrayList.clear();
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                long firstDayLong = firstDayOfNewMonth.getTime();
                long now = System.currentTimeMillis();
                if (firstDayLong - now >= 86399999) {
                    arrayList.clear();
                    adapter.notifyDataSetChanged();
                    date = dateFormat.format(firstDayOfNewMonth);
                    actionBar.setTitle("Employee Schedule - " + date);
                    SimpleDateFormat format = new SimpleDateFormat("EEEE", Locale.US);
                    String dayValue = "";
                    try {
                        dayValue = Objects.requireNonNull(format.format(firstDayOfNewMonth));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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

    private void createList(String dayValue) {
        Cursor cursor = scheduleAccess.getDataForPosition(posId);

        if (cursor.getCount() != 0) {
            arrayList.add(new ScheduleCard(dayValue,
                    cursor.getString(2),
                    cursor.getString(3)));
        }
    }

}
