package com.zenpets.doctors.doctors.modules;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.zenpets.doctors.R;
import com.zenpets.doctors.utils.helpers.TimingsPickerActivity;
import com.zenpets.doctors.utils.models.doctors.TimingsData;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DoctorTimingsFrag extends Fragment {

    /** A DATABASE REFERENCE AND QUERY INSTANCE **/
    DatabaseReference refDoctors;
    Query qryDoctors;

    /** THE INCOMING DOCTOR ID **/
    String DOCTOR_ID = null;

    /** THE TIMING STRINGS **/
    String SUN_MOR_FROM = null;
    String SUN_MOR_TO = null;
    String SUN_AFT_FROM = null;
    String SUN_AFT_TO = null;
    String MON_MOR_FROM = null;
    String MON_MOR_TO = null;
    String MON_AFT_FROM = null;
    String MON_AFT_TO = null;
    String TUE_MOR_FROM = null;
    String TUE_MOR_TO = null;
    String TUE_AFT_FROM = null;
    String TUE_AFT_TO = null;
    String WED_MOR_FROM = null;
    String WED_MOR_TO = null;
    String WED_AFT_FROM = null;
    String WED_AFT_TO = null;
    String THU_MOR_FROM = null;
    String THU_MOR_TO = null;
    String THU_AFT_FROM = null;
    String THU_AFT_TO = null;
    String FRI_MOR_FROM = null;
    String FRI_MOR_TO = null;
    String FRI_AFT_FROM = null;
    String FRI_AFT_TO = null;
    String SAT_MOR_FROM = null;
    String SAT_MOR_TO = null;
    String SAT_AFT_FROM = null;
    String SAT_AFT_TO = null;

    /** CAST THE LAYOUT ELEMENTS **/
    @BindView(R.id.linlaEmpty) LinearLayout linlaEmpty;
    @BindView(R.id.scrollTimings) ScrollView scrollTimings;
    @BindView(R.id.txtSunMorning) AppCompatTextView txtSunMorning;
    @BindView(R.id.txtSunAfternoon) AppCompatTextView txtSunAfternoon;
    @BindView(R.id.txtMonMorning) AppCompatTextView txtMonMorning;
    @BindView(R.id.txtMonAfternoon) AppCompatTextView txtMonAfternoon;
    @BindView(R.id.txtTueMorning) AppCompatTextView txtTueMorning;
    @BindView(R.id.txtTueAfternoon) AppCompatTextView txtTueAfternoon;
    @BindView(R.id.txtWedMorning) AppCompatTextView txtWedMorning;
    @BindView(R.id.txtWedAfternoon) AppCompatTextView txtWedAfternoon;
    @BindView(R.id.txtThuMorning) AppCompatTextView txtThuMorning;
    @BindView(R.id.txtThuAfternoon) AppCompatTextView txtThuAfternoon;
    @BindView(R.id.txtFriMorning) AppCompatTextView txtFriMorning;
    @BindView(R.id.txtFriAfternoon) AppCompatTextView txtFriAfternoon;
    @BindView(R.id.txtSatMorning) AppCompatTextView txtSatMorning;
    @BindView(R.id.txtSatAfternoon) AppCompatTextView txtSatAfternoon;

    /** ADD DOCTOR TIMINGS **/
    @OnClick(R.id.linlaEmpty) void configureTimings()   {
        Intent intent = new Intent(getActivity(), TimingsPickerActivity.class);
        intent.putExtra("DOCTOR_ID", DOCTOR_ID);
        startActivity(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        /** CAST THE LAYOUT TO A NEW VIEW INSTANCE **/
        View view = inflater.inflate(R.layout.doctor_timings_frag, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        /** INDICATE THAT THE FRAGMENT SHOULD RETAIN IT'S STATE **/
        setRetainInstance(true);

        /** INDICATE THAT THE FRAGMENT HAS AN OPTIONS MENU **/
        setHasOptionsMenu(true);

        /** INVALIDATE THE EARLIER OPTIONS MENU SET IN OTHER FRAGMENTS / ACTIVITIES **/
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /** GET THE INCOMING DATA **/
        getIncomingData();
    }

    /** GET THE DOCTOR'S TIMINGS **/
    private void getDoctorDetails() {
        refDoctors = FirebaseDatabase.getInstance().getReference().child("Doctor Timings");
        qryDoctors = refDoctors.orderByChild("doctorID").equalTo(DOCTOR_ID);
        qryDoctors.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                /** SHOW OR HIDE THE EMPTY LAYOUT **/
                emptyShowOrHide(dataSnapshot);

                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        Log.e("DATA", String.valueOf(dataSnapshot));
                        TimingsData data = child.getValue(TimingsData.class);

                        /** GET THE SUNDAY TIMINGS **/
                        SUN_MOR_FROM = data.getSunMorFrom();
                        SUN_MOR_TO = data.getSunMorTo();
                        SUN_AFT_FROM = data.getSunAftFrom();
                        SUN_AFT_TO = data.getSunAftTo();
                        if (SUN_MOR_FROM != null && SUN_MOR_TO != null) {
                            txtSunMorning.setText(SUN_MOR_FROM + " - " + SUN_MOR_TO);
                        } else {
                            txtSunMorning.setText("Closed");
                        }
                        if (SUN_AFT_FROM != null && SUN_AFT_TO != null) {
                            txtSunAfternoon.setText(SUN_AFT_FROM + " - " + SUN_AFT_TO);
                        } else {
                            txtSunAfternoon.setText("Closed");
                        }

                        /** THE MONDAY TIMINGS **/
                        MON_MOR_FROM = data.getMonMorFrom();
                        MON_MOR_TO = data.getMonMorTo();
                        MON_AFT_FROM = data.getMonAftFrom();
                        MON_AFT_TO = data.getMonAftTo();
                        if (MON_MOR_FROM != null && MON_MOR_TO != null)  {
                            txtMonMorning.setText(MON_MOR_FROM + " - " + MON_MOR_TO);
                        } else {
                            txtMonMorning.setText("Closed");
                        }
                        if (MON_AFT_FROM != null && MON_AFT_TO != null) {
                            txtMonAfternoon.setText(MON_AFT_FROM + " - " + MON_AFT_TO);
                        } else {
                            txtMonAfternoon.setText("Closed");
                        }

                        /** THE TUESDAY TIMINGS **/
                        TUE_MOR_FROM = data.getTueMorFrom();
                        TUE_MOR_TO = data.getTueMorTo();
                        TUE_AFT_FROM = data.getTueAftFrom();
                        TUE_AFT_TO = data.getTueAftTo();
                        if (TUE_MOR_FROM != null && TUE_MOR_TO != null) {
                            txtTueMorning.setText(TUE_MOR_FROM + " - " + TUE_MOR_TO);
                        } else {
                            txtTueMorning.setText("Closed");
                        }
                        if (TUE_AFT_FROM != null && TUE_AFT_TO != null) {
                            txtTueAfternoon.setText(TUE_AFT_FROM + " - " + TUE_AFT_TO);
                        } else {
                            txtTueAfternoon.setText("Closed");
                        }

                        /** THE WEDNESDAY TIMINGS **/
                        WED_MOR_FROM = data.getWedMorFrom();
                        WED_MOR_TO = data.getWedMorTo();
                        WED_AFT_FROM = data.getWedAftFrom();
                        WED_AFT_TO = data.getWedAftTo();
                        if (WED_MOR_FROM != null && WED_MOR_TO != null) {
                            txtWedMorning.setText(WED_MOR_FROM + " - " + WED_MOR_TO);
                        } else {
                            txtWedMorning.setText("Closed");
                        }
                        if (WED_AFT_FROM != null && WED_AFT_TO != null) {
                            txtWedAfternoon.setText(WED_AFT_FROM + " - " + WED_AFT_TO);
                        } else {
                            txtWedAfternoon.setText("Closed");
                        }

                        /** THE THURSDAY TIMINGS **/
                        THU_MOR_FROM = data.getThuMorFrom();
                        THU_MOR_TO = data.getThuMorTo();
                        THU_AFT_FROM = data.getThuAftFrom();
                        THU_AFT_TO = data.getThuAftTo();
                        if (THU_MOR_FROM != null && THU_MOR_TO != null) {
                            txtThuMorning.setText(THU_MOR_FROM + " - " + THU_MOR_TO);
                        } else {
                            txtThuMorning.setText("Closed");
                        }
                        if (THU_AFT_FROM != null && THU_AFT_TO != null) {
                            txtThuAfternoon.setText(THU_AFT_FROM + " - " + THU_AFT_TO);
                        } else {
                            txtThuAfternoon.setText("Closed");
                        }

                        /** THE FRIDAY TIMINGS **/
                        FRI_MOR_FROM = data.getFriMorFrom();
                        FRI_MOR_TO = data.getFriMorTo();
                        FRI_AFT_FROM = data.getFriAftFrom();
                        FRI_AFT_TO = data.getFriAftTo();
                        if (FRI_MOR_FROM != null && FRI_MOR_TO != null) {
                            txtFriMorning.setText(FRI_MOR_FROM + " - " + FRI_MOR_TO);
                        } else {
                            txtFriMorning.setText("Closed");
                        }
                        if (FRI_AFT_FROM != null && FRI_AFT_TO != null) {
                            txtFriAfternoon.setText(FRI_AFT_FROM + " - " + FRI_AFT_TO);
                        } else {
                            txtFriAfternoon.setText("Closed");
                        }

                        /** THE SATURDAY TIMINGS **/
                        SAT_MOR_FROM = data.getSatMorFrom();
                        SAT_MOR_TO = data.getSatMorTo();
                        SAT_AFT_FROM = data.getSatAftFrom();
                        SAT_AFT_TO = data.getSatAftTo();
                        if (SAT_MOR_FROM != null && SAT_MOR_TO != null) {
                            txtSatMorning.setText(SAT_MOR_FROM + " - " + SAT_MOR_TO);
                        } else {
                            txtSatMorning.setText("Closed");
                        }
                        if (SAT_AFT_FROM != null && SAT_AFT_TO != null) {
                            txtSatAfternoon.setText(SAT_AFT_FROM + " - " + SAT_AFT_TO);
                        } else {
                            txtSatAfternoon.setText("Closed");
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    /** GET THE INCOMING DATA **/
    private void getIncomingData() {
        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle.containsKey("DOCTOR_ID"))    {
            DOCTOR_ID = bundle.getString("DOCTOR_ID");
            if (!TextUtils.isEmpty(DOCTOR_ID))   {

                /** GET THE DOCTOR'S TIMINGS **/
                getDoctorDetails();

            } else {
                Toast.makeText(getActivity(), "Failed to get required information", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getActivity(), "Failed to get required information", Toast.LENGTH_LONG).show();
        }
    }

    /** SHOW OR HIDE THE EMPTY LAYOUT **/
    private void emptyShowOrHide(DataSnapshot dataSnapshot) {
        if (dataSnapshot.hasChildren()) {
            /** SHOW THE SCROLL VIEW AND HIDE THE EMPTY LAYOUT **/
            scrollTimings.setVisibility(View.VISIBLE);
            linlaEmpty.setVisibility(View.GONE);
        } else {
            /** HIDE THE SCROLL VIEW AND SHOW THE EMPTY LAYOUT **/
            scrollTimings.setVisibility(View.GONE);
            linlaEmpty.setVisibility(View.VISIBLE);
        }
    }
}