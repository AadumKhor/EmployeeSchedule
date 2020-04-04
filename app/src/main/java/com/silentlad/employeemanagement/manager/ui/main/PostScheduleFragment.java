package com.silentlad.employeemanagement.manager.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.silentlad.employeemanagement.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class PostScheduleFragment extends Fragment {
    private EditText posId;
    private TextView empId;
    private TextView position;
    private EditText startTime;
    private EditText endTime;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_post_schedule, container, false);

        return root;
    }
}