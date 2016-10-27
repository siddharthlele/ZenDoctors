package com.zenpets.doctors;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.zenpets.doctors.legal.PrivacyPolicyActivity;
import com.zenpets.doctors.legal.SellerAgreementActivity;
import com.zenpets.doctors.utils.TypefaceSpan;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SignUpActivity extends AppCompatActivity {

    /** THE FIREBASE AUTH INSTANCE **/
    private FirebaseAuth mAuth;

    /** A FIREBASE USER INSTANCE **/
    FirebaseUser user;

    /** CAST THE LAYOUT ELEMENTS **/
    @BindView(R.id.edtEmailAddress) AppCompatEditText edtEmailAddress;
    @BindView(R.id.edtPassword) AppCompatEditText edtPassword;
    @BindView(R.id.edtConfirmPassword) AppCompatEditText edtConfirmPassword;
    @BindView(R.id.txtTermsOfService) AppCompatTextView txtTermsOfService;

    /****** DATA TYPES FOR ACCOUNT DETAILS *****/
    String EMAIL_ADDRESS = null;
    String PASSWORD = null;
    String CONFIRM_PASSWORD = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_activity);
        ButterKnife.bind(this);

        /** INSTANTIATE THE FIREBASE AUTH INSTANCE **/
        mAuth = FirebaseAuth.getInstance();

        /** SET THE TERMS OF SERVICE TEXT **/
        setTermsAndConditions(txtTermsOfService);

        /***** CONFIGURE THE ACTIONBAR *****/
        configAB();
    }

    /***** CONFIGURE THE ACTIONBAR *****/
    @SuppressWarnings("ConstantConditions")
    private void configAB() {

        Toolbar myToolbar = (Toolbar) findViewById(R.id.myToolbar);
        setSupportActionBar(myToolbar);

        String strTitle = "Sign Up";
//        String strTitle = getString(R.string.add_a_new_pet);
        SpannableString s = new SpannableString(strTitle);
        s.setSpan(new TypefaceSpan(getApplicationContext()), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(s);
        getSupportActionBar().setSubtitle(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(SignUpActivity.this);
        inflater.inflate(R.menu.generic_activity_save_cancel, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.save:
                /***** CHECK FOR ALL ACCOUNT DETAILS  *****/
                checkAccountDetails();
                break;
            case R.id.cancel:
                finish();
                break;
            default:
                break;
        }
        return false;
    }

    /***** CHECK FOR ALL ACCOUNT DETAILS  *****/
    private void checkAccountDetails() {

        /** HIDE THE KEYBOARD **/
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edtEmailAddress.getWindowToken(), 0);

        /** COLLECT THE NECESSARY DATA **/
        EMAIL_ADDRESS = edtEmailAddress.getText().toString().trim();
        PASSWORD = edtPassword.getText().toString().trim();
        CONFIRM_PASSWORD = edtConfirmPassword.getText().toString().trim();
        boolean blnValidEmail = isValidEmail(EMAIL_ADDRESS);

        /** VALIDATE THE DATA **/
        if (TextUtils.isEmpty(EMAIL_ADDRESS))    {
            edtEmailAddress.setError("Provide your Email Address");
        } else if (!blnValidEmail)  {
            edtEmailAddress.setError("Provide a valid Email Address");
        } else if (TextUtils.isEmpty(PASSWORD)) {
            edtPassword.setError("Choose a Password");
        } else if (PASSWORD.length() < 8)   {
            edtPassword.setError("Password has to be at least 8 characters");
        } else if (TextUtils.isEmpty(CONFIRM_PASSWORD)) {
            edtConfirmPassword.setError("Confirm your Password");
        } else if (CONFIRM_PASSWORD.length() < 8)   {
            edtConfirmPassword.setError("password has to be at least 8 characters");
        } else if (!CONFIRM_PASSWORD.equals(PASSWORD)) {
            edtConfirmPassword.setError("Passwords don't match.");
        } else {
            /** CREATE THE NEW ACCOUNT **/
            createAccount();
        }
    }

    /** CREATE THE NEW ACCOUNT **/
    private void createAccount() {

        /** SHOW THE PROGRESS DIALOG WHILE UPLOADING THE IMAGE **/
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait while we create your new account....");
        dialog.setIndeterminate(false);
        dialog.setCancelable(false);
        dialog.show();

        mAuth.createUserWithEmailAndPassword(EMAIL_ADDRESS, PASSWORD).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())   {
                    /* GET THE NEW USERS INSTANCE */
                    user = mAuth.getCurrentUser();

                    if (user != null)   {
                        String message = getResources().getString(R.string.unverified_message);
                        new MaterialDialog.Builder(SignUpActivity.this)
                                .icon(ContextCompat.getDrawable(SignUpActivity.this, R.drawable.ic_info_outline_black_24dp))
                                .title("Validate Email Address")
                                .cancelable(false)
                                .content(message)
                                .positiveText("Send Verification Email")
                                .theme(Theme.LIGHT)
                                .typeface("HelveticaNeueLTW1G-MdCn.otf", "HelveticaNeueLTW1G-Cn.otf")
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                        /** SEND THE VERIFICATION MAILER **/
                                        user.sendEmailVerification();

                                        /** LOGOUT THE USER **/
                                        mAuth.signOut();

                                        /** DISMISS THE DIALOG **/
                                        dialog.dismiss();

                                        /** FINISH THE ACTIVITY **/
                                        Toast.makeText(getApplicationContext(), "Your account was created successfully", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent();
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        finish();
                                    }
                                }).show();
                    }
                } else {
                    Toast.makeText(
                            getApplicationContext(),
                            "There was a problem creating your new account. Please try again by clicking the Save button.",
                            Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignUpActivity.this, "EXCEPTION: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /** SET THE TERMS AND CONDITIONS **/
    private void setTermsAndConditions(AppCompatTextView view) {
        SpannableStringBuilder spanTxt = new SpannableStringBuilder(getResources().getString(R.string.terms_part_1));
        spanTxt.append(getResources().getString(R.string.terms_part_2));
        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent showSellerAgreement = new Intent(getApplicationContext(), SellerAgreementActivity.class);
                startActivity(showSellerAgreement);
            }
        }, spanTxt.length() - getResources().getString(R.string.terms_part_2).length(), spanTxt.length(), 0);
        spanTxt.append(getResources().getString(R.string.terms_part_3));
        spanTxt.setSpan(new ForegroundColorSpan(Color.WHITE), 48, spanTxt.length(), 0);
        spanTxt.append(getResources().getString(R.string.terms_part_4));
        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent showPrivacyPolicy = new Intent(getApplicationContext(), PrivacyPolicyActivity.class);
                startActivity(showPrivacyPolicy);
            }
        }, spanTxt.length() - " Privacy Policy".length(), spanTxt.length(), 0);
        view.setMovementMethod(LinkMovementMethod.getInstance());
        view.setText(spanTxt, TextView.BufferType.SPANNABLE);
    }

    /** VALIDATE EMAIL SYNTAX / FORMAT **/
    public static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}