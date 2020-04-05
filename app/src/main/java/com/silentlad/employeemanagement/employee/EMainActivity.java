package com.silentlad.employeemanagement.employee;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.silentlad.employeemanagement.R;
import com.silentlad.employeemanagement.data.ScheduleCard;
import com.silentlad.employeemanagement.data.access.DayAccess;
import com.silentlad.employeemanagement.data.access.EmployeeAccess;
import com.silentlad.employeemanagement.data.access.EmployeePositionAccess;
import com.silentlad.employeemanagement.data.access.ScheduleAccess;
import com.silentlad.employeemanagement.data.contracts.EmployeePositionContract;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class EMainActivity extends AppCompatActivity {

    private SimpleDateFormat appBarFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
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
    private HashMap<String, Boolean> daysMaps = new HashMap<>();

    private EmployeeAccess employeeAccess;
    private EmployeePositionAccess employeePositionAccess;
    private DayAccess dayAccess;
    private ScheduleAccess scheduleAccess;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e_main);

        // GET EMP ID FROM INTENT
        empId = getIntent().getStringExtra("empId");

        // ACTION BAR ACTIVATION AND TEXT
        final ActionBar actionBar = getSupportActionBar();
        date = appBarFormat.format(Calendar.getInstance().getTime());
        assert actionBar != null;
        actionBar.setTitle("Employee Schedule - " + date);

        // CALENDER INIT
        final CompactCalendarView calendarView = findViewById(R.id.calender);
        calendarView.setUseThreeLetterAbbreviation(true);

        // DB INIT
        employeeAccess = EmployeeAccess.getInstance(getApplicationContext());
        employeePositionAccess = EmployeePositionAccess.getInstance(getApplicationContext());
        dayAccess = DayAccess.getInstance(getApplicationContext());
        scheduleAccess = ScheduleAccess.getInstance(getApplicationContext());
        getFullNameAndPosId();
        fillDaysMaps();

        // SET GREETING TEXT
        TextView greetings = findViewById(R.id.greeting_text);
        greetings.setText(String.format("Hi! %s. Position : %s", fullName, position));

        // CALENDER WORKING
        calendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                date = appBarFormat.format(dateClicked);
                actionBar.setTitle("Employee Schedule - " + date);
                String day ="";

                try {
                    day  = Objects.requireNonNull(appBarFormat.parse(date)).toString().split(" ")[0];
                }catch (ParseException e){
                    e.printStackTrace();
                }
                Log.println(Log.DEBUG, "day", day);

                if(Boolean.valueOf(daysMaps.get(day))){
                    Log.println(Log.DEBUG, "day", "Inside if");
                    arrayList.clear();
                    createList(day);
                    adapter.notifyDataSetChanged();
                }else{
                    Log.println(Log.DEBUG, "day", "Inside else");
                    arrayList.clear();
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                date = appBarFormat.format(firstDayOfNewMonth);
                actionBar.setTitle("Employee Schedule - " + date);

                String day ="";

                try {
                    day  = Objects.requireNonNull(appBarFormat.parse(date)).toString().split(" ")[0];
                }catch (ParseException e){
                    e.printStackTrace();
                }
                Log.println(Log.DEBUG, "day", day);

                if(Boolean.valueOf(daysMaps.get(day))){
                    Log.println(Log.DEBUG, "day", "Inside if");
                    arrayList.clear();
                    createList(day);
                    adapter.notifyDataSetChanged();
                }else{
                    Log.println(Log.DEBUG, "day", "Inside else");
                    arrayList.clear();
                    adapter.notifyDataSetChanged();
                }
            }
        });
        // CREATE SCHEDULE LIST AND RECYCLER VIEW
//        createList();
        buildRecyclerView();

    }

    private void fillDaysMaps(){
        String[] days = dayAccess.getDays(empId);

        String sunday = days[0];
        String monday = days[1];
        String tuesday = days[2];
        String wednesday = days[3];
        String thursday = days[4];
        String friday = days[5];
        String saturday = days[6];

        daysMaps.put("Sun", Boolean.valueOf(sunday));
        daysMaps.put("Mon", Boolean.valueOf(monday));
        daysMaps.put("Tue", Boolean.valueOf(tuesday));
        daysMaps.put("Wed", Boolean.valueOf(wednesday));
        daysMaps.put("Thu", Boolean.valueOf(thursday));
        daysMaps.put("Fri", Boolean.valueOf(friday));
        daysMaps.put("Sat", Boolean.valueOf(saturday));

        Log.println(Log.DEBUG, "day", String.valueOf(daysMaps.get("Wed")));
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

    private void createList(String dayValue) {
        Cursor cursor = scheduleAccess.getDataForPosition(posId);

        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                arrayList.add(new ScheduleCard(dayValue,
                        cursor.getString(2),
                        cursor.getString(3)));
            }
        }
    }
}
