package com.bitblue.crebit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bitblue.crebit.services.service;


public class LoginActivity extends ActionBarActivity implements View.OnClickListener {
    TextView existinguser;
    EditText mNumber, passwd;
    Button login, forgotPass, signUp;
    String mobileNumber, password, logoutmessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        logoutmessage = getIntent().getStringExtra("logout");
        initViews();
    }

    public void initViews() {
        existinguser = (TextView) findViewById(R.id.existingUser);
        existinguser.setText(logoutmessage);
        existinguser.setTextColor(getResources().getColor(R.color.red));
        mNumber = (EditText) findViewById(R.id.et_mobileNumber);
        passwd = (EditText) findViewById(R.id.et_password);
        login = (Button) findViewById(R.id.b_login);
        forgotPass = (Button) findViewById(R.id.b_forgot_pass);
        signUp = (Button) findViewById(R.id.b_signUp);
        login.setOnClickListener(this);
        forgotPass.setOnClickListener(this);
        signUp.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.b_login:
                if (mNumber.getText() != null && passwd.getText().toString() != null) {
                    mobileNumber = mNumber.getText().toString();
                    password = passwd.getText().toString();
                    Intent openServices = new Intent(LoginActivity.this, service.class);
                    startActivity(openServices);
                } else {


                }
                break;

            case R.id.b_forgot_pass:
                Intent openForgotPass = new Intent(LoginActivity.this, ForgotPass.class);
                startActivity(openForgotPass);

                break;
            case R.id.b_signUp:
                Intent opensignUp = new Intent(LoginActivity.this, SignUp.class);
                startActivity(opensignUp);
                break;

        }
    }
}
