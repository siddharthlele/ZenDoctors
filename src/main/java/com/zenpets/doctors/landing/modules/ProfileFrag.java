package com.zenpets.doctors.landing.modules;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zenpets.doctors.R;
import com.zenpets.doctors.landing.profiles.ClinicDetailsFrag;
import com.zenpets.doctors.landing.profiles.ClinicDoctorsFrag;
import com.zenpets.doctors.landing.profiles.ClinicImagesFrag;
import com.zenpets.doctors.landing.profiles.ClinicTimingsFrag;
import com.zenpets.doctors.utils.TypefaceSpan;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import butterknife.ButterKnife;

public class ProfileFrag extends Fragment {

    /** THE TAB LAYOUT **/
    TabLayout tabLayout;

    /** THE VIEW PAGER **/
    ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        /** CAST THE LAYOUT TO A NEW VIEW INSTANCE **/
        View view = inflater.inflate(R.layout.dash_profile_frag, container, false);
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

        /** CONFIGURE THE TOOLBAR **/
        configToolbar();

        /** INSTANTIATE THE VIEW PAGER INSTANCE **/
        viewPager = (ViewPager) getActivity().findViewById(R.id.viewPager);
        setupViewPager(viewPager);

        /** INSTANTIATE THE TAB LAYOUT **/
        tabLayout = (TabLayout) getActivity().findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        /** APPLY THE CUSTOM FONT TO THE TITLES **/
        changeTabsFont();
    }

    /** SETUP THE VIEW PAGER **/
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        adapter.addFragment(new ClinicDetailsFrag(), "Clinic");
        adapter.addFragment(new ClinicDoctorsFrag(), "Doctors");
        adapter.addFragment(new ClinicImagesFrag(), "Images");
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
                    ((TextView) tabViewChild).setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/Exo2-Medium.otf"));
                }
            }
        }
    }

    /** CONFIGURE THE TOOLBAR **/
    private void configToolbar() {
        String strTitle = getResources().getString(R.string.dash_profile);
        SpannableString s = new SpannableString(strTitle);
        s.setSpan(new TypefaceSpan(getActivity()), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(s);
    }
}