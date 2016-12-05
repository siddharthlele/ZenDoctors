package com.zenpets.doctors.consultations;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.zenpets.doctors.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ConsultationAnswerActivity extends AppCompatActivity {

    /** THE INCOMING CONSULT ID **/
    String CONSULT_ID = null;

    /** THE DATA TYPES TO STORE THE ANSWER DATA **/
    String DOCTOR_ID = "-KXGKnGOZWDi4TnuYbkx";
    String ANSWER_DESCRIPTION = null;
    String ANSWER_NEXT_STEPS = null;
    String TIME_STAMP = null;

    /** CAST THE LAYOUT ELEMENTS **/
    @BindView(R.id.edtAnswer) AppCompatEditText edtAnswer;
    @BindView(R.id.edtNextSteps) AppCompatEditText edtNextSteps;

    /** POST THE ANSWER **/
    @OnClick(R.id.btnPostAnswer) void postAnswer()  {
        /** GATHER THE DATA **/
        ANSWER_DESCRIPTION = edtAnswer.getText().toString().trim();
        ANSWER_NEXT_STEPS = edtNextSteps.getText().toString().trim();

        /** POST THE ANSWER **/
        DatabaseReference refAnswer = FirebaseDatabase.getInstance().getReference().child("Answers").push();
        refAnswer.child("doctorID").setValue(DOCTOR_ID);
        refAnswer.child("consultID").setValue(CONSULT_ID);
        refAnswer.child("consultAnswer").setValue(ANSWER_DESCRIPTION);
        refAnswer.child("consultNextSteps").setValue(ANSWER_NEXT_STEPS);
        refAnswer.child("timeStamp").setValue(TIME_STAMP);
        refAnswer.child("helpfulYes").setValue(0);
        refAnswer.child("helpfulNo").setValue(0);

        /** FINISH THE ACTIVITY **/
        Toast.makeText(getApplicationContext(), "Successfully added your Answer", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consultation_answer_activity);
        ButterKnife.bind(this);

        /** GET THE INCOMING DATA **/
        fetchIncomingData();

        /** GET THE TIME STAMP **/
        Long aLong = System.currentTimeMillis() / 1000;
        TIME_STAMP = String.valueOf(aLong);
    }

    /** GET THE INCOMING DATA **/
    private void fetchIncomingData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle.containsKey("CONSULT_ID")) {
            CONSULT_ID = bundle.getString("CONSULT_ID");
            if (TextUtils.isEmpty(CONSULT_ID)) {
                Toast.makeText(getApplicationContext(), "Failed to get necessary data", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Failed to get necessary data", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}