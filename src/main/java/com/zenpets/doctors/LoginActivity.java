package com.zenpets.doctors;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.zenpets.doctors.landing.MainLandingActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LoginActivity extends AppCompatActivity {

    /** THE FIREBASE AUTH INSTANCE **/
    private FirebaseAuth auth;

    /** THE FIREBASE AUTH STATE LISTENER INSTANCE **/
    private FirebaseAuth.AuthStateListener stateListener;

    /** CAST THE LAYOUT ELEMENTS **/
    @BindView(R.id.edtEmailAddress) AppCompatEditText edtEmailAddress;
    @BindView(R.id.edtPassword) AppCompatEditText edtPassword;
    @BindView(R.id.txtCreateAccount) AppCompatTextView txtCreateAccount;
    @BindView(R.id.txtForgotPassword) AppCompatTextView txtForgotPassword;

    /** SIGN IN / LOGIN **/
    @OnClick(R.id.btnSignIn) void signIn()  {
        String strEmail = edtEmailAddress.getText().toString().trim();
        String strPassword = edtPassword.getText().toString().trim();
        if (!TextUtils.isEmpty(strEmail) && !TextUtils.isEmpty(strPassword))    {
            auth.signInWithEmailAndPassword(strEmail, strPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful())    {
//                        Log.e("EXCEPTION", String.valueOf(task.getException()));
                        String result = String.valueOf(task.getException());
                        if (result.contains("There is no user record corresponding to this identifier"))    {
                            /** SHOW UNREGISTERED USER DIALOG **/
                            showUnregisteredUser();
                        } else if (result.contains("The password is invalid or the user does not have a password"))   {
                            /** SHOW AUTHENTICATION FAILED DIALOG **/
                            showAuthFailed();
                        }
                    }
                }
            });
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    "Please check your Email Address and your Password",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /** CREATE A NEW ACCOUNT **/
    @OnClick(R.id.txtCreateAccount) void newAccount()   {
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivityForResult(intent, 101);
    }

    /** FORGOT ACCOUNT PASSWORD **/
    @OnClick(R.id.txtForgotPassword) void forgotPassword()   {
        Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
        startActivityForResult(intent, 102);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        ButterKnife.bind(this);

        /** INITIALIZE THE FIREBASE AUTH INSTANCE **/
        auth = FirebaseAuth.getInstance();

        /** START THE AUTH STATE LISTENER **/
        stateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Intent showLanding = new Intent(LoginActivity.this, MainLandingActivity.class);
                    showLanding.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(showLanding);
                    finish();
                }
            }
        };
    }

    /** SHOW UNREGISTERED USER DIALOG **/
    private void showUnregisteredUser() {
        String message = "This email doesn't exist in our records. If you haven't signed up, please use the \"Create Account\" button to do so now.";
        /** USER DOESN'T EXIST PROMPT **/
        new MaterialDialog.Builder(LoginActivity.this)
                .icon(ContextCompat.getDrawable(LoginActivity.this, R.drawable.ic_info_outline_black_24dp))
                .title("User Not Found")
                .cancelable(true)
                .content(message)
                .positiveText("Create Account")
                .negativeText("Cancel")
                .theme(Theme.LIGHT)
                .typeface("HelveticaNeueLTW1G-MdCn.otf", "HelveticaNeueLTW1G-Cn.otf")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Intent signUp = new Intent(LoginActivity.this, SignUpActivity.class);
                        startActivity(signUp);
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    /** SHOW AUTHENTICATION FAILED DIALOG **/
    private void showAuthFailed() {
        String message = "Invalid Password. If you forgotten your password, click the \"Forgot Password\" button to reset your Password. We will send you an email on your registered Email Address to reset it.";
        new MaterialDialog.Builder(LoginActivity.this)
                .icon(ContextCompat.getDrawable(LoginActivity.this, R.drawable.ic_info_outline_black_24dp))
                .title("Incorrect Password")
                .cancelable(true)
                .content(message)
                .positiveText("Forgot Password")
                .negativeText("Cancel")
                .theme(Theme.LIGHT)
                .typeface("HelveticaNeueLTW1G-MdCn.otf", "HelveticaNeueLTW1G-Cn.otf")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Intent forgotPassword = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                        startActivity(forgotPassword);
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
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(stateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (stateListener != null)  {
            auth.removeAuthStateListener(stateListener);
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}