package com.zenpets.doctors.creators;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.zenpets.doctors.R;

import butterknife.ButterKnife;

public class ClinicCreator extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clinic_creator_activity);
        ButterKnife.bind(this);
    }
}