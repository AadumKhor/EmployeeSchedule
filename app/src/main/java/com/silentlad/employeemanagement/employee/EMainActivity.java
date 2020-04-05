package com.silentlad.employeemanagement.employee;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.silentlad.employeemanagement.R;
import com.silentlad.employeemanagement.data.ScheduleCard;
import com.silentlad.employeemanagement.data.access.EmployeeAccess;
import com.silentlad.employeemanagement.data.access.EmployeePositionAccess;
import com.silentlad.employeemanagement.data.contracts.EmployeePositionContract;

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
    private String fullName = "";
    private String posId = "";
    private String position = "";
    private String empId = "";

    private EmployeeAccess employeeAccess;
    private EmployeePositionAccess employeePositionAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e_main);

        // GET EMP ID FROM INTENT
        empId = getIntent().getStringExtra("empId");

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
        getFullNameAndPosId();

        // SET GREETING TEXT
        TextView greetings = findViewById(R.id.greeting_text);
        greetings.setText(String.format("Hi! %s. Position : %s", fullName, position));

        // CALENDER WORKING
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
        // CREATE SCHEDULE LIST AND RECYCLER VIEW
        createList();
        buildRecyclerView();

    }

    private void getFullNameAndPosId() {
        fullName = employeeAccess.getName(empId);
        Cursor posCursor = employeePositionAccess.getEmpDetails(empId);

        if(posCursor.getCount() != 0){
            while(posCursor.moveToNext()){
                posId = posCursor.getString(posCursor.getColumnIndex(EmployeePositionContract.EmployeePositionEntry.COLUMN_ID));
                position = posCursor.getString(posCursor.getColumnIndex(EmployeePositionContract.EmployeePositionEntry.COLUMN_POSITION));
            }
        }
    }

    private void buildRecyclerView() {
        recyclerView = findViewById(R.id.schedule_of_this_day);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
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
        Cursor cursor = employeeAccess.getData();

        if (cursor.getCount() != 0) {

            while (cursor.moveToNext()) {
                arrayList.add(new ScheduleCard(cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2)));
            }
        }
    }
}
