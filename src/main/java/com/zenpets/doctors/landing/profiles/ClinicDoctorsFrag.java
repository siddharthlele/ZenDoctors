package com.zenpets.doctors.landing.profiles;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.zenpets.doctors.R;
import com.zenpets.doctors.creators.DoctorCreatorActivity;
import com.zenpets.doctors.doctors.DoctorsActivity;
import com.zenpets.doctors.utils.models.DoctorsData;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ClinicDoctorsFrag extends Fragment {

    /** THE FIREBASE USER INSTANCE **/
    FirebaseUser user;

    /** THE LOGGED IN USER'S USER ID **/
    String USER_ID = null;

    /** THE FIREBASE DATABASE REFERENCE AND QUERY INSTANCE **/
    DatabaseReference reference;
    Query query;

    /** THE FIREBASE RECYCLER ADAPTER INSTANCE **/
    FirebaseRecyclerAdapter adapter;

    /** CAST THE LAYOUT ELEMENTS **/
    @BindView(R.id.linlaHeaderProgress) LinearLayout linlaHeaderProgress;
    @BindView(R.id.listDoctors) RecyclerView listDoctors;
    @BindView(R.id.linlaEmpty) LinearLayout linlaEmpty;

    /** ADD A NEW PET **/
    @OnClick(R.id.linlaEmpty) void newDoctor() {
        Intent addNewPet = new Intent(getActivity(), DoctorCreatorActivity.class);
        startActivity(addNewPet);
    }

    @OnClick(R.id.fabNewDoctor) void fabNewDoctor()   {
        Intent addNewPet = new Intent(getActivity(), DoctorCreatorActivity.class);
        startActivity(addNewPet);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        /** CAST THE LAYOUT TO A NEW VIEW INSTANCE **/
        View view = inflater.inflate(R.layout.clinic_doctors_frag_list, container, false);
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

        /** CONFIGURE THE RECYCLER VIEW **/
        configRecycler();

        /** GET THE USER DETAILS **/
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            /** GET THE USER ID **/
            USER_ID = user.getUid();
//            Log.e("USER ID", USER_ID);

            /** GET THE CLINIC DETAILS **/
            reference = FirebaseDatabase.getInstance().getReference().child("Doctors");
            query = reference.orderByChild("clinicOwner").equalTo(USER_ID);
//            Log.e("REFERENCE", String.valueOf(reference));

            /** SETUP THE FIREBASE ADAPTER **/
            setupFirebaseAdapter();
        }
    }

    /** SETUP THE FIREBASE ADAPTER **/
    private void setupFirebaseAdapter() {

        adapter = new FirebaseRecyclerAdapter<DoctorsData, DoctorsVH>
                (DoctorsData.class, R.layout.clinic_doctors_frag_item, DoctorsVH.class, query) {
            @Override
            protected void populateViewHolder(DoctorsVH viewHolder, DoctorsData model, final int position) {
                if (model != null)  {

                    /** SET THE DOCTOR'S NAME **/
                    String strPrefix = model.getDoctorPrefix();
                    String strName = model.getDoctorName();
                    String strDocName = strPrefix + " " + strName;
                    viewHolder.txtDoctorName.setText(strDocName);

                    /** SET THE DOCTOR'S PROFILE PICTURE **/
                    Picasso.with(getActivity())
                            .load(model.getDoctorProfile())
                            .resize(1024, 768)
                            .centerInside()
                            .into(viewHolder.imgvwDoctorProfile);

                    /** SHOW THE DOCTOR'S PROFILE **/
                    viewHolder.cardDoctor.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getActivity(), DoctorsActivity.class);
                            intent.putExtra("DOCTOR_ID", adapter.getRef(position).getKey());
                            startActivity(intent);
                        }
                    });
                }
            }
        };

        query.addChildEventListener(new ChildEventListener() {
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

        query.addListenerForSingleValueEvent(new ValueEventListener() {
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
        listDoctors.setAdapter(adapter);
    }

    private static class DoctorsVH extends RecyclerView.ViewHolder {

        CardView cardDoctor;
        final AppCompatTextView txtDoctorName;
        final AppCompatImageView imgvwDoctorProfile;
        final AppCompatImageView imgvwDoctorOptions;

        public DoctorsVH(View itemView) {
            super(itemView);

            cardDoctor = (CardView) itemView.findViewById(R.id.cardDoctor);
            txtDoctorName = (AppCompatTextView) itemView.findViewById(R.id.txtDoctorName);
            imgvwDoctorProfile = (AppCompatImageView) itemView.findViewById(R.id.imgvwDoctorProfile);
            imgvwDoctorOptions = (AppCompatImageView) itemView.findViewById(R.id.imgvwDoctorOptions);
        }
    }

    /** SHOW OR HIDE THE EMPTY LAYOUT **/
    private void emptyShowOrHide(DataSnapshot dataSnapshot) {
        if (dataSnapshot.hasChildren()) {
            /** SHOW THE RECYCLER VIEW AND HIDE THE EMPTY LAYOUT **/
            listDoctors.setVisibility(View.VISIBLE);
            linlaEmpty.setVisibility(View.GONE);
        } else {
            /** HIDE THE RECYCLER VIEW AND SHOW THE EMPTY LAYOUT **/
            listDoctors.setVisibility(View.GONE);
            linlaEmpty.setVisibility(View.VISIBLE);
        }
    }

    /** CONFIGURE THE RECYCLER VIEW **/
    private void configRecycler() {
        listDoctors.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listDoctors.setLayoutManager(llm);
    }
}