package com.ninam.employeemanagement.manager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import com.ninam.employeemanagement.R;
import com.ninam.employeemanagement.data.access.EmployeeAccess;
import com.ninam.employeemanagement.data.contracts.NotificationContract;
import com.ninam.employeemanagement.data.dbhelpers.NotificationsHelper;

import java.util.ArrayList;

public class MNotificationsActivity extends AppCompatActivity {
    private ArrayList<NotificationClass> notifications = new ArrayList<>();
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private NotificationAdapter adapter;

    private NotificationsHelper notificationsHelper;
    private EmployeeAccess employeeAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m_notifications);

        adapter = new NotificationAdapter(notifications);
        notificationsHelper = new NotificationsHelper(this);
        employeeAccess = EmployeeAccess.getInstance(this);

        recyclerView = findViewById(R.id.notif_recycler_view);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        createList();

    }

    private void createList() {
        Cursor cursor = notificationsHelper.getData();

        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                String empId = cursor.getString(cursor.getColumnIndex(NotificationContract.NotificationEntry.COLUMN_EMP_ID));
                String notifId = cursor.getString(cursor.getColumnIndex(NotificationContract.NotificationEntry.COLUMN_ID));
                String timeStamp = cursor.getString(cursor.getColumnIndex(NotificationContract.NotificationEntry.COLUMN_TIMESTAMP));
                Log.println(Log.DEBUG, "timestamp", timeStamp);
                String fullName = employeeAccess.getName(empId);
                String date = timeStamp.split(" ")[0];

                notifications.add(new NotificationClass(notifId, empId, fullName, date, timeStamp));
                adapter.notifyDataSetChanged();
            }
        }
    }
}
