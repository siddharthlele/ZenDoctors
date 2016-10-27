package com.zenpets.doctors;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;

import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgotPasswordActivity extends AppCompatActivity {

    /** THE FIREBASE AUTH INSTANCE **/
    private FirebaseAuth auth;

    /** CAST THE LAYOUT ELEMENTS **/
    @BindView(R.id.edtEmailAddress)
    AppCompatEditText edtEmailAddress;

    /** VALIDATE EMAIL ADDRESS AND RESET PASSWORD **/
    @OnClick(R.id.btnResetPassword) void resetPassword()    {
        if (!TextUtils.isEmpty(edtEmailAddress.getText().toString().trim()))    {
            auth.sendPasswordResetEmail(edtEmailAddress.getText().toString().trim());
        } else {
            edtEmailAddress.setError("Enter your registered Email Address");
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password_activity);
        ButterKnife.bind(this);

        /** INITIALIZE THE FIREBASE AUTH INSTANCE **/
        auth = FirebaseAuth.getInstance();
    }
}