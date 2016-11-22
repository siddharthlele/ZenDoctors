package com.zenpets.doctors.landing.profiles;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.helpers.Constants;
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
import com.zenpets.doctors.creators.ClinicAlbumCreator;
import com.zenpets.doctors.utils.models.ClinicAlbumsData;
import com.zenpets.doctors.utils.models.ClinicsData;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ClinicImagesFrag extends Fragment {

    /** THE FIREBASE USER INSTANCE **/
    FirebaseUser user;

    /** THE FIREBASE DATABASE REFERENCE AND QUERY INSTANCE **/
    DatabaseReference refClinic;
    DatabaseReference refAlbums;
    Query qryClinic;

    /** THE LOGGED IN USER'S USER ID **/
    String USER_ID = null;

    /** THE CLINIC ID AND NAME **/
    String CLINIC_ID = null;
    String CLINIC_NAME = null;

    /** FLAG TO CHECK CLINIC IMAGES AVAILABLE **/
    boolean blnImagesFlag = true;

    /** THE FIREBASE RECYCLER ADAPTER INSTANCE **/
    private FirebaseRecyclerAdapter adapter;

    /** CAST THE LAYOUT ELEMENTS **/
    @BindView(R.id.linlaHeaderProgress) LinearLayout linlaHeaderProgress;
    @BindView(R.id.gridClinicImages) RecyclerView gridClinicImages;
    @BindView(R.id.linlaEmpty) LinearLayout linlaEmpty;

    /** SELECT CLINIC IMAGES **/
    @OnClick(R.id.linlaEmpty) void selectImages()    {
        Intent intent = new Intent(getActivity(), AlbumSelectActivity.class);
        intent.putExtra(Constants.INTENT_EXTRA_LIMIT, 8);
        startActivityForResult(intent, Constants.REQUEST_CODE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        /** CAST THE LAYOUT TO A NEW VIEW INSTANCE **/
        View view = inflater.inflate(R.layout.clinic_images_frag_list, container, false);
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

            /** GET THE CLINIC ID **/
            refClinic = FirebaseDatabase.getInstance().getReference().child("Clinics");
            qryClinic = refClinic.orderByChild("clinicOwner").equalTo(USER_ID);
            qryClinic.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChildren()) {
                        for (DataSnapshot child: dataSnapshot.getChildren()) {

                            ClinicsData data = child.getValue(ClinicsData.class);
                            CLINIC_ID = child.getKey();
                            CLINIC_NAME = data.getClinicName();

                            /** CHECK IF THE CLINIC HAS IMAGES ON RECORD **/
                            refAlbums = FirebaseDatabase.getInstance().getReference().child("Clinics").child(CLINIC_ID).child("Images");
                            refAlbums.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.hasChildren() && dataSnapshot.getChildrenCount() > 0)  {
                                        blnImagesFlag = true;
                                    } else {
                                        blnImagesFlag = false;
                                        getActivity().invalidateOptionsMenu();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });

                            /** SETUP THE FIREBASE ADAPTER **/
                            setupFirebaseAdapter();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else {
            Toast.makeText(getActivity(), "failed to get required information", Toast.LENGTH_SHORT).show();
        }
    }

    private static class ClinicAlbumsVH extends RecyclerView.ViewHolder {

        AppCompatImageView imgvwClinicAlbum;

        public ClinicAlbumsVH(View itemView) {
            super(itemView);

            imgvwClinicAlbum = (AppCompatImageView) itemView.findViewById(R.id.imgvwClinicAlbum);
        }
    }

    /** SETUP THE FIREBASE ADAPTER **/
    private void setupFirebaseAdapter() {

        adapter = new FirebaseRecyclerAdapter<ClinicAlbumsData, ClinicAlbumsVH>
                (ClinicAlbumsData.class, R.layout.clinic_images_frag_item, ClinicAlbumsVH.class, refAlbums) {

            @Override
            protected void populateViewHolder(ClinicAlbumsVH viewHolder, final ClinicAlbumsData model, final int position) {

                if (model != null)  {

                    /** SET THE CLINIC IMAGE**/
                    Picasso.with(getActivity())
                            .load(model.getClinicImage())
                            .resize(1024, 800)
                            .centerCrop()
                            .into(viewHolder.imgvwClinicAlbum);
                }
            }
        };

        refAlbums.addChildEventListener(new ChildEventListener() {
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

        refAlbums.addListenerForSingleValueEvent(new ValueEventListener() {
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
        gridClinicImages.setAdapter(adapter);
    }

    /** SHOW OR HIDE THE EMPTY LAYOUT **/
    private void emptyShowOrHide(DataSnapshot dataSnapshot) {
        if (dataSnapshot.hasChildren()) {
            /** SHOW THE RECYCLER VIEW AND HIDE THE EMPTY LAYOUT **/
            gridClinicImages.setVisibility(View.VISIBLE);
            linlaEmpty.setVisibility(View.GONE);
        } else {
            /** HIDE THE RECYCLER VIEW AND SHOW THE EMPTY LAYOUT **/
            gridClinicImages.setVisibility(View.GONE);
            linlaEmpty.setVisibility(View.VISIBLE);
        }
    }

    /** CONFIGURE THE RECYCLER VIEW **/
    private void configRecycler() {
        int intOrientation = getActivity().getResources().getConfiguration().orientation;
        gridClinicImages.setHasFixedSize(true);
        GridLayoutManager glm = null;
        boolean isTablet = getResources().getBoolean(R.bool.isTablet);
        if (isTablet)   {
            if (intOrientation == 1)	{
                glm = new GridLayoutManager(getActivity(), 2);
            } else if (intOrientation == 2) {
                glm = new GridLayoutManager(getActivity(), 4);
            }
        } else {
            if (intOrientation == 1)    {
                glm = new GridLayoutManager(getActivity(), 2);
            } else if (intOrientation == 2) {
                glm = new GridLayoutManager(getActivity(), 4);
            }
        }
        gridClinicImages.setLayoutManager(glm);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuAdd:
                Intent intent = new Intent(getActivity(), ClinicAlbumCreator.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.frag_clinic_images_upload, menu);

        if (blnImagesFlag)  {
            MenuItem menuAdd = menu.findItem(R.id.menuAdd);
            menuAdd.setVisible(false);
        } else {
            MenuItem menuAdd = menu.findItem(R.id.menuAdd);
            menuAdd.setVisible(true);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }
}
