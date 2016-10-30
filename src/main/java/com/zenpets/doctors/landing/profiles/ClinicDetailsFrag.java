package com.zenpets.doctors.landing.profiles;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.zenpets.doctors.R;
import com.zenpets.doctors.utils.models.ClinicsData;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ClinicDetailsFrag extends Fragment {

    /** THE FIREBASE USER INSTANCE **/
    FirebaseUser user;

    /** THE FIREBASE DATABASE REFERENCE AND QUERY INSTANCE **/
    DatabaseReference reference;
    Query query;

    /** THE LOGGED IN USER'S USER ID **/
    String USER_ID = null;

    /** THE CLINIC ID **/
    String CLINIC_ID = null;

    /****** DATA TYPES FOR PROFILE DETAILS *****/
    String CLINIC_NAME = null;
    String CLINIC_LOGO = null;
    String CONTACT_PERSON = null;
    String PHONE_NUMBER = null;
    String POSTAL_ADDRESS = null;
    String CITY = null;
    String STATE = null;
    String PIN_CODE = null;
    String LANDMARK = null;
    String COUNTRY_NAME = null;
    String CURRENCY_SYMBOL = null;
    String CLINIC_CHARGES = null;
    Double CLINIC_LATITUDE;
    Double CLINIC_LONGITUDE;

    /** CAST THE LAYOUT ELEMENTS **/
    @BindView(R.id.linlaHeaderProgress) LinearLayout linlaHeaderProgress;
    @BindView(R.id.imgvwClinicCircular) CircleImageView imgvwClinicCircular;
    @BindView(R.id.txtClinicName) AppCompatTextView txtClinicName;
    @BindView(R.id.txtContactPerson) AppCompatTextView txtContactPerson;
    @BindView(R.id.txtPhoneNumber) AppCompatTextView txtPhoneNumber;
    @BindView(R.id.txtPostalAddress) AppCompatTextView txtPostalAddress;
    @BindView(R.id.txtCombinedAddress) AppCompatTextView txtCombinedAddress;
    @BindView(R.id.txtCharges) AppCompatTextView txtCharges;

    /** THE CLINIC MAP VIEW **/
    MapView clinicMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        /** CAST THE LAYOUT TO A NEW VIEW INSTANCE **/
        View view = inflater.inflate(R.layout.clinic_details_frag, container, false);
        ButterKnife.bind(this, view);
        clinicMap = (MapView) view.findViewById(R.id.clinicMap);
        clinicMap.onCreate(savedInstanceState);
        clinicMap.onResume();
        clinicMap.setClickable(false);
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

            /** SHOW THE PROGRESS **/
            linlaHeaderProgress.setVisibility(View.VISIBLE);

            /** GET THE CLINIC DETAILS **/
            getClinicDetails();
        }
    }

    /** GET THE CLINIC DETAILS **/
    private void getClinicDetails() {
        reference = FirebaseDatabase.getInstance().getReference().child("Clinics");
        query = reference.orderByChild("clinicOwner").equalTo(USER_ID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot child: dataSnapshot.getChildren()) {
                        CLINIC_ID = child.getKey();
                        ClinicsData data = child.getValue(ClinicsData.class);

                        /* GET THE CLINIC NAME */
                        CLINIC_NAME = data.getClinicName();
                        if (CLINIC_NAME != null)    {
                            txtClinicName.setText(CLINIC_NAME);
                        }

                        /* GET THE CLINIC LOGO */
                        CLINIC_LOGO = data.getClinicLogo();
                        if (CLINIC_LOGO != null)   {
                            Picasso.with(getActivity())
                                    .load(CLINIC_LOGO)
                                    .centerInside()
                                    .resize(1024, 768)
                                    .into(imgvwClinicCircular);
                        }

                        /* GET THE CONTACT PERSON */
                        CONTACT_PERSON = data.getClinicContactPerson();
                        if (CONTACT_PERSON != null) {
                            txtContactPerson.setText(CONTACT_PERSON);
                        }

                        /* GET THE PHONE NUMBER */
                        PHONE_NUMBER = data.getClinicPhone();
                        if (PHONE_NUMBER != null)   {
                            txtPhoneNumber.setText(PHONE_NUMBER);
                        }

                        /* GET THE POSTAL ADDRESS */
                        POSTAL_ADDRESS = data.getClinicAddress();
                        if (POSTAL_ADDRESS != null) {
                            txtPostalAddress.setText(POSTAL_ADDRESS);
                        }

                        /* GET THE CITY */
                        CITY = data.getClinicCity();

                        /* GET THE STATE */
                        STATE = data.getClinicState();

                        /* GET THE PIN CODE */
                        PIN_CODE = data.getClinicPinCode();

                        /* GET THE LANDMARK */
                        LANDMARK = data.getClinicLandmark();

                         /* GET THE COUNTRY */
                        COUNTRY_NAME = data.getClinicCountry();

                        /* GET THE CURRENCY */
                        CURRENCY_SYMBOL = data.getClinicCurrency();

                        /** COMBINE THE SECONDARY ADDRESS **/
                        String strCombined = CITY + ", " + STATE + ", " + COUNTRY_NAME + " - " + PIN_CODE + "\n" + LANDMARK;
                        txtCombinedAddress.setText(strCombined);

                        /* GET THE LATITUDE */
                        CLINIC_LATITUDE = data.getClinicLatitude();

                        /* GET THE LONGITUDE */
                        CLINIC_LONGITUDE = data.getClinicLongitude();

                        /* GET THE CLINIC CHARGES*/
                        CLINIC_CHARGES = data.getClinicCharges();
                        if (CLINIC_CHARGES != null) {
                            txtCharges.setText(CURRENCY_SYMBOL + " " + CLINIC_CHARGES);
                        }

                        /* GET THE CLINIC LOCATION */
                        if (CLINIC_LATITUDE != null && CLINIC_LONGITUDE != null)    {
                            final LatLng latLng = new LatLng(CLINIC_LATITUDE, CLINIC_LONGITUDE);
                            clinicMap.getMapAsync(new OnMapReadyCallback() {
                                @Override
                                public void onMapReady(GoogleMap googleMap) {
                                    googleMap.getUiSettings().setMapToolbarEnabled(false);
                                    googleMap.getUiSettings().setAllGesturesEnabled(false);
                                    MarkerOptions options = new MarkerOptions();
                                    options.position(latLng);
                                    options.title(CLINIC_NAME);
                                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                                    Marker mMarker = googleMap.addMarker(options);
                                    googleMap.addMarker(new MarkerOptions().position(latLng).title(CLINIC_NAME));

                                    /** MOVE THE MAP CAMERA **/
                                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mMarker.getPosition(), 18));
                                    googleMap.animateCamera(CameraUpdateFactory.zoomTo(18), 2000, null);
                                }
                            });
                        }
                    }

                    /** HIDE THE PROGRESS **/
                    linlaHeaderProgress.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        clinicMap.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        clinicMap.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        clinicMap.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        clinicMap.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        clinicMap.onLowMemory();
    }
}