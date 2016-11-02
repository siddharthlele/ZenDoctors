package com.zenpets.doctors.doctors.modules;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.zenpets.doctors.R;
import com.zenpets.doctors.creators.doctors.EducationCreatorActivity;
import com.zenpets.doctors.utils.models.doctors.EducationData;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DoctorEducationFrag extends Fragment {

    /** A DATABASE REFERENCE AND QUERY INSTANCE **/
    DatabaseReference refDoctors;
    Query qryDoctors;

    /** THE INCOMING DOCTOR ID **/
    String DOCTOR_ID = null;

    /** THE FIREBASE RECYCLER ADAPTER INSTANCE **/
    FirebaseRecyclerAdapter adapter;

    /** CAST THE LAYOUT ELEMENTS **/
    @BindView(R.id.linlaHeaderProgress) LinearLayout linlaHeaderProgress;
    @BindView(R.id.listDocEducation) RecyclerView listDocEducation;
    @BindView(R.id.linlaEmpty) LinearLayout linlaEmpty;

    /** ADD A NEW EDUCATIONAL QUALIFICATION **/
    @OnClick(R.id.fabNewEducation) void newFabNewEducation()   {
        Intent intent = new Intent(getActivity(), EducationCreatorActivity.class);
        intent.putExtra("DOCTOR_ID", DOCTOR_ID);
        startActivity(intent);
    }

    @OnClick(R.id.linlaEmpty) void newEducation()   {
        Intent intent = new Intent(getActivity(), EducationCreatorActivity.class);
        intent.putExtra("DOCTOR_ID", DOCTOR_ID);
        startActivity(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        /** CAST THE LAYOUT TO A NEW VIEW INSTANCE **/
        View view = inflater.inflate(R.layout.doctor_education_frag_list, container, false);
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

        /** CONFIGURE THE RECYCLER VIEW **/
        configRecycler();
    }

    /** GET THE DOCTOR'S LIST OF EDUCATIONAL QUALIFICATIONS **/
    private void getDoctorDetails() {
        refDoctors = FirebaseDatabase.getInstance().getReference().child("Doctor Education");
        qryDoctors = refDoctors.orderByChild("doctorID").equalTo(DOCTOR_ID);

        /** SETUP THE FIREBASE RECYCLER ADAPTER **/
        adapter = new FirebaseRecyclerAdapter<EducationData, EducationVH>
                (EducationData.class, R.layout.doctor_education_frag_item, EducationVH.class, qryDoctors) {
            @Override
            protected void populateViewHolder(EducationVH viewHolder, EducationData model, int position) {
                if (model != null)  {
                    /** SET THE EDUCATIONAL QUALIFICATION NAME **/
                    String QUALIFICATION_NAME = model.getQualificationName();
                    String COLLEGE_NAME = model.getCollegeName();
                    String QUALIFICATION_YEAR = model.getQualificationYear();
                    String strFinalText = QUALIFICATION_NAME + " - " + COLLEGE_NAME + ", " + QUALIFICATION_YEAR;
                    viewHolder.txtDoctorEducation.setText(strFinalText);
                }
            }
        };

        qryDoctors.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                /** SHOW OR HIDE THE EMPTY LAYOUT **/
                emptyShowOrHide(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                /** SHOW OR HIDE THE EMPTY LAYOUT **/
                emptyShowOrHide(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                /** SHOW OR HIDE THE EMPTY LAYOUT **/
                emptyShowOrHide(dataSnapshot);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                /** SHOW OR HIDE THE EMPTY LAYOUT **/
                emptyShowOrHide(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        qryDoctors.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                /** SHOW OR HIDE THE EMPTY LAYOUT **/
                emptyShowOrHide(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        /** SET THE ADAPTER **/
        listDocEducation.setAdapter(adapter);
    }

    private static class EducationVH extends RecyclerView.ViewHolder {

        final AppCompatTextView txtDoctorEducation;

        public EducationVH(View itemView) {
            super(itemView);

            txtDoctorEducation = (AppCompatTextView) itemView.findViewById(R.id.txtDoctorEducation);
        }
    }

    /** GET THE INCOMING DATA **/
    private void getIncomingData() {
        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle.containsKey("DOCTOR_ID"))    {
            DOCTOR_ID = bundle.getString("DOCTOR_ID");
            if (!TextUtils.isEmpty(DOCTOR_ID))   {

                /** GET THE DOCTOR'S LIST OF EDUCATIONAL QUALIFICATIONS **/
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
            /** SHOW THE RECYCLER VIEW AND HIDE THE EMPTY LAYOUT **/
            listDocEducation.setVisibility(View.VISIBLE);
            linlaEmpty.setVisibility(View.GONE);
        } else {
            /** HIDE THE RECYCLER VIEW AND SHOW THE EMPTY LAYOUT **/
            listDocEducation.setVisibility(View.GONE);
            linlaEmpty.setVisibility(View.VISIBLE);
        }
    }

    /** CONFIGURE THE RECYCLER VIEW **/
    private void configRecycler() {
        listDocEducation.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listDocEducation.setLayoutManager(llm);
    }
}