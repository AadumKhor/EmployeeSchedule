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

    private SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
    private Calendar currentCalender = Calendar.getInstance(Locale.getDefault());

    private RecyclerView recyclerView;
    private ArrayList<ScheduleCard> arrayList;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e_main);

        final ActionBar actionBar = getSupportActionBar();
        // Setting default toolbar title to empty
        actionBar.setTitle("Employee Schedule");

        final CompactCalendarView calendarView = findViewById(R.id.calender);
        calendarView.setUseThreeLetterAbbreviation(true);
        final TextView textView = findViewById(R.id.textView);

        calendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                textView.setText(dateClicked.toString());
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                textView.setText(firstDayOfNewMonth.toString());
            }
        });
    }
}
