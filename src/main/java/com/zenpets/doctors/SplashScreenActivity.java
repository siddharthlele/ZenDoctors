package com.zenpets.doctors;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.zenpets.doctors.credentials.LoginActivity;
import com.zenpets.doctors.landing.MainLandingActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SplashScreenActivity extends AppCompatActivity {

    /** CAST THE LAYOUT ELEMENT/S **/
    @BindView(R.id.txtAppName) AppCompatTextView txtAppName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_activity);
        ButterKnife.bind(this);
        FirebaseAnalytics.getInstance(this);

        /** CONFIGURE THE ANIMATIONS **/
        Animation animIn = new AlphaAnimation(0.0f, 1.0f);
        animIn.setDuration(2000);

        /** ANIMATE THE APP NAME **/
        txtAppName.startAnimation(animIn);
        animIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                /** DETERMINE IF THE USER IS LOGGED IN **/
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null)   {
                    Intent showLanding = new Intent(SplashScreenActivity.this, MainLandingActivity.class);
                    showLanding.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(showLanding);
                    finish();
                } else {
                    Intent showLogin = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    showLogin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(showLogin);
                    finish();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}