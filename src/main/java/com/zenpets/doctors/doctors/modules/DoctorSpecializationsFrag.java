package com.zenpets.doctors.doctors.modules;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zenpets.doctors.R;
import com.zenpets.doctors.utils.models.doctors.SpecializationsData;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DoctorSpecializationsFrag extends Fragment {

    /** A DATABASE REFERENCE INSTANCE **/
    DatabaseReference refDoctors;

    /** THE INCOMING DOCTOR ID **/
    String DOCTOR_ID = null;

    /** THE FIREBASE RECYCLER ADAPTER INSTANCE **/
    FirebaseRecyclerAdapter adapter;

    /** CAST THE LAYOUT ELEMENTS **/
    @BindView(R.id.linlaHeaderProgress) LinearLayout linlaHeaderProgress;
    @BindView(R.id.listDocSpecialization) RecyclerView listDocSpecialization;
    @BindView(R.id.linlaEmpty) LinearLayout linlaEmpty;

    /** ADD A NEW SPECIALIZATION **/
    @OnClick(R.id.fabNewSpecialization) void fabNewSpecialization()  {
        showNewSpecializationDialog();
    }

    @OnClick(R.id.linlaEmpty) void newSpecialization()  {
        showNewSpecializationDialog();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        /** CAST THE LAYOUT TO A NEW VIEW INSTANCE **/
        View view = inflater.inflate(R.layout.doctor_specializations_frag_list, container, false);
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
        refDoctors = FirebaseDatabase.getInstance().getReference().child("Doctors").child(DOCTOR_ID).child("Specializations");

        /** SETUP THE FIREBASE RECYCLER ADAPTER **/
        adapter = new FirebaseRecyclerAdapter<SpecializationsData, SpecializationVH>
                (SpecializationsData.class, R.layout.doctor_specialization_frag_item, SpecializationVH.class, refDoctors) {
            @Override
            protected void populateViewHolder(SpecializationVH viewHolder, SpecializationsData model, int position) {
                if (model != null)  {
                    viewHolder.txtDoctorSpecialization.setText(model.getSpecializationName());
                }
            }
        };

        refDoctors.addChildEventListener(new ChildEventListener() {
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

        refDoctors.addListenerForSingleValueEvent(new ValueEventListener() {
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
        listDocSpecialization.setAdapter(adapter);
    }

    private static class SpecializationVH extends RecyclerView.ViewHolder {

        AppCompatTextView txtDoctorSpecialization;

        public SpecializationVH(View itemView) {
            super(itemView);

            txtDoctorSpecialization = (AppCompatTextView) itemView.findViewById(R.id.txtDoctorSpecialization);
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
            listDocSpecialization.setVisibility(View.VISIBLE);
            linlaEmpty.setVisibility(View.GONE);
        } else {
            /** HIDE THE RECYCLER VIEW AND SHOW THE EMPTY LAYOUT **/
            listDocSpecialization.setVisibility(View.GONE);
            linlaEmpty.setVisibility(View.VISIBLE);
        }
    }

    /** CONFIGURE THE RECYCLER VIEW **/
    private void configRecycler() {
        listDocSpecialization.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listDocSpecialization.setLayoutManager(llm);
    }

    /** SHOW THE NEW SPECIALIZATION DIALOG **/
    private void showNewSpecializationDialog() {
        new MaterialDialog.Builder(getActivity())
                .title("New Experience")
                .content("Add a new specialization for this Doctor. \n\nExample 1: Veterinary Physician\n\nExample 2: Veterinary Surgeon")
                .inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS | InputType.TYPE_TEXT_FLAG_MULTI_LINE)
                .inputRange(5, 200)
                .theme(Theme.LIGHT)
                .typeface("HelveticaNeueLTW1G-MdCn.otf", "HelveticaNeueLTW1G-Cn.otf")
                .positiveText("ADD")
                .negativeText("Cancel")
                .input("Add a specialization....", null, false, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        /** ADD THE RECORD TO THE FIREBASE DATABASE **/
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Doctors").child(DOCTOR_ID).child("Specializations").push();
                        reference.child("specializationName").setValue(input.toString());

                        Toast.makeText(getActivity(), "Added \"" + input.toString() + "\" to this Doctors' record", Toast.LENGTH_SHORT).show();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
}