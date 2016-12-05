package com.zenpets.doctors.landing.modules;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.zenpets.doctors.R;
import com.zenpets.doctors.consultations.ConsultationDetailsActivity;
import com.zenpets.doctors.utils.TypefaceSpan;
import com.zenpets.doctors.utils.models.consultations.ConsultationsData;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ConsultFrag extends Fragment {

    /** THE FIREBASE ADAPTER **/
    FirebaseRecyclerAdapter adapter;

    /** CAST THE LAYOUT ELEMENTS **/
    @BindView(R.id.linlaHeaderProgress) LinearLayout linlaHeaderProgress;
    @BindView(R.id.listConsultations) RecyclerView listConsultations;
    @BindView(R.id.linlaEmpty) LinearLayout linlaEmpty;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        /** CAST THE LAYOUT TO A NEW VIEW INSTANCE **/
        View view = inflater.inflate(R.layout.dash_consult_frag, container, false);
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

        /***** CONFIGURE THE TOOLBAR *****/
        configTB();

        /** CONFIGURE THE RECYCLER VIEW **/
        configRecycler();

        /** FETCH THE LIST OF PUBLIC CONSULTATIONS **/
        fetchPublicConsultations();
    }

    /** FETCH THE LIST OF PUBLIC CONSULTATIONS **/
    private void fetchPublicConsultations() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Consultations");
        Query query = reference.orderByChild("consultStatus").equalTo("Public");

        /** SETUP THE REVIEWS FIREBASE RECYCLER ADAPTER **/
        adapter = new FirebaseRecyclerAdapter<ConsultationsData, ConsultationsVH>
                (ConsultationsData.class, R.layout.dash_consult_item, ConsultationsVH.class, query) {
            @Override
            protected void populateViewHolder(ConsultationsVH viewHolder, ConsultationsData model, final int position) {
                if (model != null)  {

                    /** GET THE CONSULTATION HEADER **/
                    String strConsultHeader = model.getConsultHeader();
                    if (!TextUtils.isEmpty(strConsultHeader))   {
                        viewHolder.txtConsultHeader.setText(strConsultHeader);
                    }

                    /** GET THE CONSULTATION DESCRIPTION **/
                    String strConsultDescription = model.getConsultDescription();
                    if (!TextUtils.isEmpty(strConsultDescription))   {
                        viewHolder.txtConsultDescription.setText(strConsultDescription);
                    }

                    /** GET THE CONSULTATION TIME STAMP **/
                    String strTimeStamp = model.getTimeStamp();
                    long lngTimeStamp = Long.parseLong(strTimeStamp) * 1000;
                    Calendar calendar = Calendar.getInstance(Locale.getDefault());
                    calendar.setTimeInMillis(lngTimeStamp);
                    Date date = calendar.getTime();

                    PrettyTime prettyTime = new PrettyTime();
                    String strDate = prettyTime.format(date);
                    Log.e("DATE", strDate);

                    if (!TextUtils.isEmpty(strDate))   {
                        viewHolder.txtTimeStamp.setText(strDate);
                    }

                    /** GET THE CONSULTATION VIEWS **/
                    int intConsultViews = model.getConsultViews();
                    viewHolder.txtViews.setText(String.valueOf(intConsultViews) + " Views");

                    /** SHOW THE CONSULTATION DETAILS **/
                    viewHolder.linlaConsultContainer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getActivity(), ConsultationDetailsActivity.class);
                            intent.putExtra("CONSULT_ID", adapter.getRef(position).getKey());
                            startActivity(intent);
                        }
                    });
                }
            }
        };

        query.addChildEventListener(consultationsChildEventListener);
        query.addListenerForSingleValueEvent(consultationsValueEventListener);

        /** SET THE ADAPTER **/
        listConsultations.setAdapter(adapter);
    }

    /** THE REVIEWS VIEW HOLDER **/
    private static class ConsultationsVH extends RecyclerView.ViewHolder {
        LinearLayout linlaConsultContainer;
        AppCompatTextView txtConsultHeader;
        AppCompatTextView txtConsultDescription;
        AppCompatTextView txtTimeStamp;
        AppCompatTextView txtViews;
        AppCompatTextView txtAnswers;

        public ConsultationsVH(View itemView) {
            super(itemView);
            linlaConsultContainer = (LinearLayout) itemView.findViewById(R.id.linlaConsultContainer);
            txtConsultHeader = (AppCompatTextView) itemView.findViewById(R.id.txtConsultHeader);
            txtConsultDescription = (AppCompatTextView) itemView.findViewById(R.id.txtConsultDescription);
            txtTimeStamp = (AppCompatTextView) itemView.findViewById(R.id.txtTimeStamp);
            txtViews = (AppCompatTextView) itemView.findViewById(R.id.txtViews);
            txtAnswers = (AppCompatTextView) itemView.findViewById(R.id.txtAnswers);
        }
    }

    /** THE CONSULTATIONS CHILD EVENT LISTENER **/
    private ChildEventListener consultationsChildEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            /** SHOW OR HIDE THE EMPTY LAYOUT **/
            emptyShowOrHideReviews(dataSnapshot);
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            /** SHOW OR HIDE THE EMPTY LAYOUT **/
            emptyShowOrHideReviews(dataSnapshot);
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            /** SHOW OR HIDE THE EMPTY LAYOUT **/
            emptyShowOrHideReviews(dataSnapshot);
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            /** SHOW OR HIDE THE EMPTY LAYOUT **/
            emptyShowOrHideReviews(dataSnapshot);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    };

    /** THE CONSULTATIONS VALUE EVENT LISTENER **/
    private ValueEventListener consultationsValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            /** SHOW OR HIDE THE EMPTY LAYOUT **/
            emptyShowOrHideReviews(dataSnapshot);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    };

    private void emptyShowOrHideReviews(DataSnapshot dataSnapshot)  {
        if (dataSnapshot.hasChildren()) {
            /* SHOW THE RECYCLER VIEW CONTAINER */
            listConsultations.setVisibility(View.VISIBLE);
            linlaEmpty.setVisibility(View.GONE);
        } else {
            /* SHOW THE NO REVIEWS CONTAINER */
            linlaEmpty.setVisibility(View.VISIBLE);
            listConsultations.setVisibility(View.GONE);
        }
    }

    /** CONFIGURE THE RECYCLER VIEW **/
    private void configRecycler() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        listConsultations.setLayoutManager(manager);
        listConsultations.setHasFixedSize(true);
    }

    /***** CONFIGURE THE ACTIONBAR *****/
    private void configTB() {
//        String strTitle = getResources().getString(R.string.dash_consult);
        String strTitle = "Consultations Feed";
        SpannableString s = new SpannableString(strTitle);
        s.setSpan(new TypefaceSpan(getActivity()), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(s);
    }
}