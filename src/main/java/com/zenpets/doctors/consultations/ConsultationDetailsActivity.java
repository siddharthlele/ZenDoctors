package com.zenpets.doctors.consultations;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zenpets.doctors.R;
import com.zenpets.doctors.utils.TypefaceSpan;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ConsultationDetailsActivity extends AppCompatActivity {

    /** THE INCOMING CONSULT ID **/
    String CONSULT_ID = null;

    /** CAST THE LAYOUT ELEMENTS **/
    @BindView(R.id.txtQuestionTitle) AppCompatTextView txtQuestionTitle;
    @BindView(R.id.txtTimeStamp) AppCompatTextView txtTimeStamp;
    @BindView(R.id.txtQuestionDescription) AppCompatTextView txtQuestionDescription;

    /** ADD A NEW ANSWER **/
    @OnClick(R.id.btnAnswer) void addAnswer()   {
        Intent intent = new Intent(ConsultationDetailsActivity.this, ConsultationAnswerActivity.class);
        intent.putExtra("CONSULT_ID", CONSULT_ID);
        startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consultation_details_activity);
        ButterKnife.bind(this);

        /***** CONFIGURE THE TOOLBAR *****/
        configTB();

        /** GET THE INCOMING DATA **/
        fetchIncomingData();
    }

    /** FETCH THE CONSULTATION DETAILS **/
    private void fetchConsultDetails() {
        DatabaseReference refConsult = FirebaseDatabase.getInstance().getReference().child("Consultations").child(CONSULT_ID);
        refConsult.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {

                    /** GET THE QUESTION TITLE  **/
                    String strQuestionTitle = dataSnapshot.child("consultHeader").getValue(String.class);
                    txtQuestionTitle.setText(strQuestionTitle);

                    /** GET THE QUESTION DESCRIPTION  **/
                    String strQuestionDescription = dataSnapshot.child("consultDescription").getValue(String.class);
                    txtQuestionDescription.setText(strQuestionDescription);

                    /** GET THE QUESTION DESCRIPTION  **/
                    String strTimestamp = dataSnapshot.child("timeStamp").getValue(String.class);
                    long lngTimeStamp = Long.parseLong(strTimestamp) * 1000;
                    Calendar calendar = Calendar.getInstance(Locale.getDefault());
                    calendar.setTimeInMillis(lngTimeStamp);
                    Date date = calendar.getTime();

                    PrettyTime prettyTime = new PrettyTime();
                    String strDate = prettyTime.format(date);
                    Log.e("DATE", strDate);
                    txtTimeStamp.setText(strDate);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    /** GET THE INCOMING DATA **/
    private void fetchIncomingData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle.containsKey("CONSULT_ID")) {
            CONSULT_ID = bundle.getString("CONSULT_ID");
            if (!TextUtils.isEmpty(CONSULT_ID)) {
                /** FETCH THE CONSULTATION DETAILS **/
                fetchConsultDetails();
            } else {
                Toast.makeText(getApplicationContext(), "Failed to get necessary data", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Failed to get necessary data", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    /***** CONFIGURE THE TOOLBAR *****/
    @SuppressWarnings("ConstantConditions")
    private void configTB() {
        Toolbar myToolbar = (Toolbar) findViewById(R.id.myToolbar);
        setSupportActionBar(myToolbar);

        String strTitle = "Question";
//        String strTitle = getString(R.string.add_a_new_medicine_record);
        SpannableString s = new SpannableString(strTitle);
        s.setSpan(new TypefaceSpan(getApplicationContext()), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(s);
        getSupportActionBar().setSubtitle(null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}