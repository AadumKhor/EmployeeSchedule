package com.silentlad.employeemanagement.employee;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toolbar;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.silentlad.employeemanagement.R;
import com.silentlad.employeemanagement.data.ScheduleCard;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EMainActivity extends AppCompatActivity {

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
    private Calendar currentCalender = Calendar.getInstance(Locale.getDefault());

    private RecyclerView recyclerView;
    private ArrayList<ScheduleCard> arrayList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private EmployeeAdapter adapter = new EmployeeAdapter(arrayList);
    private String date = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e_main);

        final ActionBar actionBar = getSupportActionBar();
        // Setting default toolbar title to empty
        date = dateFormat.format(Calendar.getInstance().getTime());

        actionBar.setTitle("Employee Schedule - " + date);

        final CompactCalendarView calendarView = findViewById(R.id.calender);
        calendarView.setUseThreeLetterAbbreviation(true);

        calendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                date = dateFormat.format(dateClicked);
                actionBar.setTitle("Employee Schedule - " + date);
                arrayList.clear();
                createList();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                date = dateFormat.format(firstDayOfNewMonth);
                actionBar.setTitle("Employee Schedule - " + date);
                arrayList.clear();
                createSecondList();
                adapter.notifyDataSetChanged();
            }
        });
        createList();
        buildRecyclerView();

    }

    private void buildRecyclerView() {
        recyclerView = findViewById(R.id.schedule_of_this_day);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
//        adapter = new EmployeeAdapter(arrayList);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void createSecondList() {
        arrayList.add(new ScheduleCard("Tuesday", "10am", "5pm"));
        arrayList.add(new ScheduleCard("Tuesday", "10am", "5pm"));
        arrayList.add(new ScheduleCard("Tuesday", "10am", "5pm"));
        arrayList.add(new ScheduleCard("Tuesday", "10am", "5pm"));
    }

    private void createList() {
        arrayList.add(new ScheduleCard("Monday", "10am", "5pm"));
        arrayList.add(new ScheduleCard("Monday", "10am", "5pm"));
        arrayList.add(new ScheduleCard("Monday", "10am", "5pm"));
        arrayList.add(new ScheduleCard("Monday", "10am", "5pm"));
    }
}