package com.zenpets.doctors.landing;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.zenpets.doctors.R;
import com.zenpets.doctors.creators.ClinicCreator;
import com.zenpets.doctors.landing.modules.CalendarFrag;
import com.zenpets.doctors.landing.modules.ConsultFrag;
import com.zenpets.doctors.landing.modules.DashboardFrag;
import com.zenpets.doctors.landing.modules.HelpActivity;
import com.zenpets.doctors.landing.modules.PatientsFrag;
import com.zenpets.doctors.landing.modules.ReportsFrag;
import com.zenpets.doctors.landing.modules.SettingsActivity;
import com.zenpets.doctors.landing.modules.TipsFrag;
import com.zenpets.doctors.utils.models.ClinicsData;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainLandingActivity extends AppCompatActivity {

    /** THE FIREBASE USER INSTANCE **/
    FirebaseUser user;

    /** THE LOGGED IN USER'S USER ID **/
    String USER_ID = null;

    /** DECLARE THE USER PROFILE HEADER **/
    private AppCompatImageView imgvwClinicLogo;
    private AppCompatTextView txtClinicName;

    /** PROFILE DETAILS **/
    private String CLINIC_NAME;
    private String CLINIC_LOGO;

    /** DECLARE THE LAYOUT ELEMENTS **/
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    /** A FRAGMENT INSTANCE **/
    private Fragment mContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_landing_activity);

        /** CONFIGURE THE TOOLBAR **/
        configToolbar();

        /** CONFIGURE THE NAVIGATION BAR **/
        configureNavBar();

        /** GET THE USER DETAILS **/
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            /** CHECK IF THE USER HAS VERIFIED **/
            if (!user.isEmailVerified())    {
                /** SHOW THE UNVERIFIED DIALOG **/
                showVerificationDialog(user);
            }

            /** GET THE USER ID **/
            USER_ID = user.getUid();

            /** GET THE CLINIC NAME **/
            CLINIC_NAME = user.getDisplayName();

            /** SET THE CLINIC NAME **/
            if (CLINIC_NAME != null)  {
                txtClinicName.setText(CLINIC_NAME);
            }
        }

        /** CHECK IF THE CLINIC PROFILE HAS BEEN COMPLETED **/
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null)   {
            USER_ID = user.getUid();
            DatabaseReference refDoctors = FirebaseDatabase.getInstance().getReference().child("Clinics");
            refDoctors.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.hasChildren()) {
                        /** SHOW THE ADD CLINIC INFORMATION DIALOG **/
                        showClinicDetailsDialog();
                    } else {
                        for (DataSnapshot child: dataSnapshot.getChildren()) {
                            ClinicsData data = child.getValue(ClinicsData.class);
                            CLINIC_LOGO = data.getClinicLogo();
                            Log.e("CONTACT LOGO", CLINIC_LOGO);

                            /** SET THE CLINIC LOGO **/
                            if (CLINIC_LOGO != null)   {
                                Picasso.with(MainLandingActivity.this)
                                        .load(CLINIC_LOGO)
                                        .centerInside()
                                        .fit()
                                        .into(imgvwClinicLogo);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }

        /** SHOW THE FIRST FRAGMENT (DASHBOARD) **/
        if (savedInstanceState == null) {
            Fragment mContent = new DashboardFrag();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame, mContent, "KEY_FRAG")
                    .commit();
        }
    }

    /** CONFIGURE THE NAVIGATION BAR **/
    private void configureNavBar() {

        /** INITIALIZE THE NAVIGATION VIEW **/
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        View view = navigationView.getHeaderView(0);
        imgvwClinicLogo = (AppCompatImageView) view.findViewById(R.id.imgvwClinicLogo);
        txtClinicName = (AppCompatTextView) view.findViewById(R.id.txtClinicName);

        /** CHANGE THE FRAGMENTS ON NAVIGATION ITEM SELECTION **/
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                /** CHECK IF AN ITEM IS CHECKED / NOT CHECKED **/
                if (menuItem.isChecked())   {
                    menuItem.setChecked(false);
                }  else {
                    menuItem.setChecked(true);
                }

                /** Closing drawer on item click **/
                drawerLayout.closeDrawers();

                /** CHECK SELECTED ITEM AND SHOW APPROPRIATE FRAGMENT **/
                switch (menuItem.getItemId()){
                    case R.id.dashHome:
                        mContent = new DashboardFrag();
                        switchFragment(mContent);
                        return true;
                    case R.id.dashCalendar:
                        mContent = new CalendarFrag();
                        switchFragment(mContent);
                        return true;
                    case R.id.dashPatients:
                        mContent = new PatientsFrag();
                        switchFragment(mContent);
                        return true;
                    case R.id.dashReports:
                        mContent = new ReportsFrag();
                        switchFragment(mContent);
                        return true;
                    case R.id.dashConsult:
                        mContent = new ConsultFrag();
                        switchFragment(mContent);
                        return true;
                    case R.id.dashTips:
                        mContent = new TipsFrag();
                        switchFragment(mContent);
                        return true;
                    case R.id.dashSettings:
                        Intent showSettings = new Intent(MainLandingActivity.this, SettingsActivity.class);
                        startActivity(showSettings);
                        return true;
                    case R.id.dashHelp:
                        Intent showHelp = new Intent(MainLandingActivity.this, HelpActivity.class);
                        startActivity(showHelp);
                        return true;
                    default:
                        Toast.makeText(getApplicationContext(), "Somethings Wrong", Toast.LENGTH_SHORT).show();
                        return true;
                }
            }
        });
    }

    /***** CONFIGURE THE TOOLBAR *****/
    private void configToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.myToolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        if (drawerLayout != null) {
            drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        }

        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
                syncState();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
                syncState();
            }
        };
        drawerLayout.addDrawerListener(mDrawerToggle);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())   {
            case android.R.id.home:
                if (drawerLayout.isDrawerOpen(navigationView))    {
                    drawerLayout.closeDrawer(navigationView);
                } else {
                    drawerLayout.openDrawer(navigationView);
                }
                return  true;

            default: return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /** METHOD TO CHANGE THE FRAGMENT **/
    private void switchFragment(Fragment fragment) {

        /** HIDE THE NAV DRAWER **/
        drawerLayout.closeDrawer(GravityCompat.START);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame, fragment)
                .commit();
    }

    /** SHOW THE VERIFICATION DIALOG **/
    private void showVerificationDialog(final FirebaseUser user) {
        String message = getResources().getString(R.string.unverified_message);
        new MaterialDialog.Builder(MainLandingActivity.this)
                .icon(ContextCompat.getDrawable(MainLandingActivity.this, R.drawable.ic_info_outline_black_24dp))
                .title("Account Not Verified")
                .cancelable(false)
                .content(message)
                .positiveText("Send Verification Email")
                .negativeText("Later")
                .theme(Theme.LIGHT)
                .typeface("HelveticaNeueLTW1G-MdCn.otf", "HelveticaNeueLTW1G-Cn.otf")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        user.sendEmailVerification();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    /** SHOW THE ADD CLINIC INFORMATION DIALOG **/
    private void showClinicDetailsDialog() {
        String message = getResources().getString(R.string.clinic_profile);
        /** USER DOESN'T EXIST PROMPT **/
        new MaterialDialog.Builder(MainLandingActivity.this)
                .icon(ContextCompat.getDrawable(MainLandingActivity.this, R.drawable.ic_info_outline_black_24dp))
                .title("Additional Details Needed")
                .cancelable(true)
                .content(message)
                .positiveText("Add Clinic Details")
                .negativeText("Later")
                .theme(Theme.LIGHT)
                .typeface("HelveticaNeueLTW1G-MdCn.otf", "HelveticaNeueLTW1G-Cn.otf")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Intent intent = new Intent(MainLandingActivity.this, ClinicCreator.class);
                        startActivityForResult(intent, 101);
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}