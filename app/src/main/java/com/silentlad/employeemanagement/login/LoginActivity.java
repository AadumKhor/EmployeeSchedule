package com.silentlad.employeemanagement.login;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.silentlad.employeemanagement.R;
import com.silentlad.employeemanagement.data.access.EmployeePositionAccess;
import com.silentlad.employeemanagement.data.contracts.EmployeePositionContract;
import com.silentlad.employeemanagement.employee.EMainActivity;
import com.silentlad.employeemanagement.manager.MMainActivity;

public class LoginActivity extends AppCompatActivity {
    private EmployeePositionAccess employeePositionAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        employeePositionAccess = EmployeePositionAccess.getInstance(getApplicationContext());
        final String type = getIntent().getStringExtra("type"); // to get the type of login
        Button loginButton = findViewById(R.id.login_button);
        final EditText enterId = findViewById(R.id.login_enter_id);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idEntered = "";
                try {
                    idEntered = enterId.getText().toString().trim();
                } catch (NullPointerException e) {
                    idEntered = "";
                    e.printStackTrace();
                }
                assert type != null;
                if (type.equals("emp")) {
                    if (checkValidEmpLogin(idEntered)) {
                        Intent intent = new Intent(getApplicationContext(), EMainActivity.class);
                        intent.putExtra("empId", idEntered);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid login!", Toast.LENGTH_LONG).show();
                    }
                }
                if (type.equals("man")) {
                    if (checkValidManLogin(idEntered)) {
                        Intent intent = new Intent(getApplicationContext(), MMainActivity.class);
                        intent.putExtra("empId", idEntered);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });
    }

    private boolean checkValidEmpLogin(String empId) {
        boolean dataExists = false;
        Cursor cursor = employeePositionAccess.getEmpDetails(empId);

        if(cursor.getCount()!=0){
            while(cursor.moveToNext()){
                String posId = cursor.getString(cursor.getColumnIndex(EmployeePositionContract.EmployeePositionEntry.COLUMN_ID));
                String position = cursor.getString(cursor.getColumnIndex(EmployeePositionContract.EmployeePositionEntry.COLUMN_POSITION));

                if((posId != null)&& !position.equals("Manager")){
                    dataExists = true;
                    break;
                }
            }
        }
        return dataExists;
    }

    private boolean checkValidManLogin(String empId) {
        boolean dataExists = false;
        Cursor cursor = employeePositionAccess.getEmpDetails(empId);

        if(cursor.getCount()!=0){
            while(cursor.moveToNext()){
                String posId = cursor.getString(cursor.getColumnIndex(EmployeePositionContract.EmployeePositionEntry.COLUMN_ID));
                String position = cursor.getString(cursor.getColumnIndex(EmployeePositionContract.EmployeePositionEntry.COLUMN_POSITION));

                if((posId != null)&& position.equals("Manager")){
                    dataExists = true;
                    break;
                }
            }
        }
        return dataExists;
    }
}
