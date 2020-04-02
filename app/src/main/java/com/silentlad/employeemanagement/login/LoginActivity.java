package com.silentlad.employeemanagement.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.silentlad.employeemanagement.R;
import com.silentlad.employeemanagement.employee.EMainActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        final String type = getIntent().getStringExtra("type"); // to get the type of login
        Button loginButton = findViewById(R.id.login_button);
        EditText enterId = findViewById(R.id.login_enter_id);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type.equals("emp")){
                    Intent intent = new Intent(getApplicationContext(), EMainActivity.class);
                    startActivity(intent);
                }
                if(type.equals("man")){
//                    Intent intent = new Intent(getApplicationContext());
//                    startActivity(intent);
                }
            }
        });
    }
}
