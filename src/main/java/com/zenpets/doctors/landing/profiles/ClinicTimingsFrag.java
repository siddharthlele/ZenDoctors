package com.zenpets.doctors.landing.profiles;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.zenpets.doctors.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ClinicTimingsFrag extends Fragment {

    /** THE FIREBASE USER INSTANCE **/
    FirebaseUser user;

    /** THE LOGGED IN USER'S USER ID **/
    String USER_ID = null;

    /** THE FIREBASE DATABASE REFERENCE AND QUERY INSTANCE **/
    DatabaseReference reference;
    Query query;

    /** THE CLINIC ID **/
    String CLINIC_ID = null;

    /****** DATA TYPES FOR CLINIC TIMINGS *****/
    String CLINIC_247 = null;
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
    @BindView(R.id.scrollTimings) ScrollView scrollTimings;
    @BindView(R.id.txt247) AppCompatTextView txt247;
    @BindView(R.id.linlaDetailedTimings) LinearLayout linlaDetailedTimings;
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
    @BindView(R.id.linlaEmpty) LinearLayout linlaEmpty;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        /** CAST THE LAYOUT TO A NEW VIEW INSTANCE **/
        View view = inflater.inflate(R.layout.clinic_timings_frag, container, false);
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

        /** GET THE USER DETAILS **/
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            /** GET THE USER ID **/
            USER_ID = user.getUid();

            /** FETCH THE CLINIC TIMINGS **/
            fetchClinicTimings();
        } else {
            Toast.makeText(getActivity(), "Failed to get necessary information. Please try again", Toast.LENGTH_LONG).show();
        }
    }

    /** FETCH THE CLINIC TIMINGS **/
    private void fetchClinicTimings() {

        /** GET THE CLINIC TIMINGS **/
        reference = FirebaseDatabase.getInstance().getReference().child("Clinic Timings");
        query = reference.orderByChild("clinicOwner").equalTo(USER_ID);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                /** SHOW OR HIDE THE EMPTY LAYOUT **/
                emptyShowOrHide(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    /** SHOW OR HIDE THE EMPTY LAYOUT **/
    private void emptyShowOrHide(DataSnapshot dataSnapshot) {
        if (dataSnapshot.hasChildren()) {
            /** SHOW THE TIMINGS AND HIDE THE EMPTY LAYOUT **/
            scrollTimings.setVisibility(View.VISIBLE);
            linlaEmpty.setVisibility(View.GONE);
        } else {
            /** HIDE THE TIMINGS VIEW AND SHOW THE EMPTY LAYOUT **/
            scrollTimings.setVisibility(View.GONE);
            linlaEmpty.setVisibility(View.VISIBLE);
        }
    }
}