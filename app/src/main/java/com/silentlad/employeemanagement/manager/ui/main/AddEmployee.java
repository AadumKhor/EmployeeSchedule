package com.silentlad.employeemanagement.manager.ui.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.silentlad.employeemanagement.R;
import com.silentlad.employeemanagement.data.Result;
import com.silentlad.employeemanagement.data.access.EmployeeAccess;

public class AddEmployee extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_add_employee, container, false);
        final EditText firstName = root.findViewById(R.id.add_emp_f_edit);
        final EditText lastName = root.findViewById(R.id.add_emp_l_edit);
        Button button = root.findViewById(R.id.add_employee_button);

        final EmployeeAccess access = EmployeeAccess.getInstance(getContext());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fName = firstName.getText().toString();
                String lName = lastName.getText().toString();

                if(fName.equals("") || lName.equals("")){
                    Toast.makeText(getContext(), "Please enter valid data", Toast.LENGTH_SHORT).show();
                }else{
                    Result result = access.insertNewEmployee(fName, lName);

                    if(result instanceof Result.Success){
                        Toast.makeText(getContext(), "Employee Added.", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getContext(), "Some error occurred", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        return root;
    }
}
