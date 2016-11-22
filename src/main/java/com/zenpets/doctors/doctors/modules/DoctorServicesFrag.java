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
import com.zenpets.doctors.utils.models.doctors.ServicesData;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DoctorServicesFrag extends Fragment {

    /** A DATABASE REFERENCE INSTANCE **/
    DatabaseReference refDoctors;

    /** THE INCOMING DOCTOR ID **/
    String DOCTOR_ID = null;

    /** THE FIREBASE RECYCLER ADAPTER INSTANCE **/
    FirebaseRecyclerAdapter adapter;

    /** CAST THE LAYOUT ELEMENTS **/
    @BindView(R.id.linlaHeaderProgress) LinearLayout linlaHeaderProgress;
    @BindView(R.id.listDocServices) RecyclerView listDocServices;
    @BindView(R.id.linlaEmpty) LinearLayout linlaEmpty;

    /** ADD A NEW SERVICE **/
    @OnClick(R.id.fabNewService) void fabNewService()  {
        showNewServiceDialog();
    }

    @OnClick(R.id.linlaEmpty) void newService()  {
        showNewServiceDialog();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        /** CAST THE LAYOUT TO A NEW VIEW INSTANCE **/
        View view = inflater.inflate(R.layout.doctor_services_frag_list, container, false);
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
        refDoctors = FirebaseDatabase.getInstance().getReference().child("Doctors").child(DOCTOR_ID).child("Services");

        /** SETUP THE FIREBASE RECYCLER ADAPTER **/
        adapter = new FirebaseRecyclerAdapter<ServicesData, ServicesVH>
                (ServicesData.class, R.layout.doctor_services_frag_item, ServicesVH.class, refDoctors) {
            @Override
            protected void populateViewHolder(ServicesVH viewHolder, ServicesData model, int position) {
                if (model != null)  {
                    viewHolder.txtDoctorService.setText(model.getServiceName());
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
        listDocServices.setAdapter(adapter);
    }

    private static class ServicesVH extends RecyclerView.ViewHolder {

        AppCompatTextView txtDoctorService;

        public ServicesVH(View itemView) {
            super(itemView);

            txtDoctorService = (AppCompatTextView) itemView.findViewById(R.id.txtDoctorService);
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
            listDocServices.setVisibility(View.VISIBLE);
            linlaEmpty.setVisibility(View.GONE);
        } else {
            /** HIDE THE RECYCLER VIEW AND SHOW THE EMPTY LAYOUT **/
            listDocServices.setVisibility(View.GONE);
            linlaEmpty.setVisibility(View.VISIBLE);
        }
    }

    /** CONFIGURE THE RECYCLER VIEW **/
    private void configRecycler() {
        listDocServices.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listDocServices.setLayoutManager(llm);
    }

    /** SHOW THE NEW SERVICE DIALOG **/
    private void showNewServiceDialog() {
        new MaterialDialog.Builder(getActivity())
                .title("New Service")
                .content("Add a new service offered by this Doctor. \n\nExample 1: Vaccination / Immunization\nExample 2: Pet Counselling\nExample 3: Pet Grooming")
                .inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS | InputType.TYPE_TEXT_FLAG_MULTI_LINE)
                .inputRange(5, 200)
                .theme(Theme.LIGHT)
                .typeface("HelveticaNeueLTW1G-MdCn.otf", "HelveticaNeueLTW1G-Cn.otf")
                .positiveText("ADD")
                .negativeText("Cancel")
                .input("Add a service....", null, false, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        /** ADD THE RECORD TO THE FIREBASE DATABASE **/
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Doctors").child(DOCTOR_ID).child("Services").push();
                        reference.child("serviceName").setValue(input.toString());

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