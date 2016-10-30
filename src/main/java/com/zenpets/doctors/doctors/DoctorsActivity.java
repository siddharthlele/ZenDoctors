package com.zenpets.doctors.doctors;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zenpets.doctors.R;
import com.zenpets.doctors.doctors.modules.DoctorDetailsFrag;
import com.zenpets.doctors.doctors.modules.DoctorExperienceFrag;
import com.zenpets.doctors.doctors.modules.DoctorQualificationsFrag;
import com.zenpets.doctors.doctors.modules.DoctorServicesFrag;
import com.zenpets.doctors.doctors.modules.DoctorSpecializationsFrag;
import com.zenpets.doctors.doctors.modules.DoctorTimingsFrag;
import com.zenpets.doctors.landing.modules.ProfileFrag;
import com.zenpets.doctors.landing.profiles.ClinicDetailsFrag;
import com.zenpets.doctors.landing.profiles.ClinicDoctorsFrag;
import com.zenpets.doctors.landing.profiles.ClinicImagesFrag;
import com.zenpets.doctors.landing.profiles.ClinicTimingsFrag;
import com.zenpets.doctors.utils.TypefaceSpan;

import java.util.ArrayList;
import java.util.List;

public class DoctorsActivity extends AppCompatActivity {

    /** THE TAB LAYOUT **/
    TabLayout tabLayout;

    /** THE VIEW PAGER **/
    ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctors_activity);

        /** CONFIGURE THE TOOLBAR **/
        configToolbar();

        /** INSTANTIATE THE VIEW PAGER INSTANCE **/
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        setupViewPager(viewPager);

        /** INSTANTIATE THE TAB LAYOUT **/
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        /** APPLY THE CUSTOM FONT TO THE TITLES **/
        changeTabsFont();

        Bundle bundle = getIntent().getExtras();
        String s = bundle.getString("DOCTOR_ID");
        Log.e("DOCTOR ID", s);
    }

    /** SETUP THE VIEW PAGER **/
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new DoctorDetailsFrag(), "Clinic");
        adapter.addFragment(new DoctorQualificationsFrag(), "Qualifications");
        adapter.addFragment(new DoctorSpecializationsFrag(), "Specializations");
        adapter.addFragment(new DoctorServicesFrag(), "Service");
        adapter.addFragment(new DoctorExperienceFrag(), "Experience");
        adapter.addFragment(new DoctorTimingsFrag(), "Timings");
        viewPager.setAdapter(adapter);
    }

    /** THE PAGER ADAPTER **/
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    /** APPLY THE CUSTOM FONT TO THE TITLES **/
    private void changeTabsFont() {
        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Exo2-Medium.otf"));
                }
            }
        }
    }

    /** CONFIGURE THE TOOLBAR **/
    @SuppressWarnings("ConstantConditions")
    private void configToolbar() {
        Toolbar myToolbar = (Toolbar) findViewById(R.id.myToolbar);
        setSupportActionBar(myToolbar);

        String strTitle = "Add Clinic Images";
//        String strTitle = getString(R.string.add_a_new_pet);
        SpannableString s = new SpannableString(strTitle);
        s.setSpan(new TypefaceSpan(getApplicationContext()), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(s);
        getSupportActionBar().setSubtitle(null);
    }
}