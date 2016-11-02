package com.zenpets.doctors.doctors.modules;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.zenpets.doctors.R;
import com.zenpets.doctors.utils.models.DoctorsData;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorDetailsFrag extends Fragment {

    /** THE INCOMING DOCTOR ID **/
    String DOCTOR_ID = null;

    /** DATA TYPES TO HOLD THE DOCTOR'S DATA **/
    String DOCTOR_PREFIX;
    String DOCTOR_NAME;
    String DOCTOR_GENDER;
    String DOCTOR_EXPERIENCE;
    String DOCTOR_CHARGES;
    String DOCTOR_SUMMARY;
    String DOCTOR_PROFILE_PICTURE;

    /** CAST THE LAYOUT ELEMENTS **/
    @BindView(R.id.imgvwDoctorProfile) CircleImageView imgvwDoctorProfile;
    @BindView(R.id.txtDoctorName) AppCompatTextView txtDoctorName;
    @BindView(R.id.txtDoctorGender) AppCompatTextView txtDoctorGender;
    @BindView(R.id.txtDoctorExperience) AppCompatTextView txtDoctorExperience;
    @BindView(R.id.txtDoctorCharges) AppCompatTextView txtDoctorCharges;
    @BindView(R.id.txtDoctorSummary) AppCompatTextView txtDoctorSummary;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        /** CAST THE LAYOUT TO A NEW VIEW INSTANCE **/
        View view = inflater.inflate(R.layout.doctor_details_frag, container, false);
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

    /** GET THE DOCTOR DETAILS **/
    private void getDoctorDetails() {
        DatabaseReference refDoctor = FirebaseDatabase.getInstance().getReference().child("Doctors").child(DOCTOR_ID);
        refDoctor.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    DoctorsData data = dataSnapshot.getValue(DoctorsData.class);

                    /* GET THE DOCTOR'S PREFIX AND THE NAME */
                    DOCTOR_PREFIX = data.getDoctorPrefix();
                    DOCTOR_NAME = data.getDoctorName();
                    txtDoctorName.setText(DOCTOR_PREFIX + " " + DOCTOR_NAME);

                    /* GET THE DOCTOR'S GENDER*/
                    DOCTOR_GENDER = data.getDoctorGender();
                    txtDoctorGender.setText(DOCTOR_GENDER);

                    /* GET THE DOCTOR'S EXPERIENCE */
                    DOCTOR_EXPERIENCE = data.getDoctorExperience();
                    txtDoctorExperience.setText(DOCTOR_EXPERIENCE + " Years");

                    /* GET THE DOCTOR'S CHARGES */
                    DOCTOR_CHARGES = data.getDoctorCharges();
                    txtDoctorCharges.setText(DOCTOR_CHARGES);

                    /* GET THE DOCTOR'S SUMMARY */
                    DOCTOR_SUMMARY = data.getDoctorSummary();
                    txtDoctorSummary.setText(DOCTOR_SUMMARY);

                    /* GET THE DOCTOR'S PROFILE PICTURE */
                    DOCTOR_PROFILE_PICTURE = data.getDoctorProfile();
                    Picasso.with(getActivity())
                            .load(DOCTOR_PROFILE_PICTURE)
                            .centerInside()
                            .resize(1024, 768)
                            .into(imgvwDoctorProfile);
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

                /* GET THE DOCTOR DETAILS */
                getDoctorDetails();

            } else {
                Toast.makeText(getActivity(), "Failed to get required information", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getActivity(), "Failed to get required information", Toast.LENGTH_LONG).show();
        }
    }
}