package com.bitblue.crebit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bitblue.crebit.services.Services;


public class LoginActivity extends ActionBarActivity implements View.OnClickListener {

    EditText mNumber, passwd;
    Button login, forgotPass, signUp;
    String mobileNumber, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setIcon(R.drawable.crebit);
        initViews();
    }

    public void initViews() {
        mNumber = (EditText) findViewById(R.id.et_mobileNumber);
        passwd = (EditText) findViewById(R.id.et_password);
        login = (Button) findViewById(R.id.b_login);
        forgotPass = (Button) findViewById(R.id.b_forgot_pass);
        signUp = (Button) findViewById(R.id.b_signUp);
/*        mNumber.setOnTouchListener(this);
        passwd.setOnTouchListener(this);*/
        mNumber.setOnClickListener(this);
        passwd.setOnClickListener(this);
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
                    Intent openServies=new Intent(LoginActivity.this, Services.class);
                    startActivity(openServies);
                }
                else{


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

/*    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()) {
            case R.id.et_mobileNumber:
                if (passwd.getText().toString().equals(""))
                    passwd.setHint("  Password");
                mNumber.setHint("");
                break;
            case R.id.et_password:
                if (mNumber.getText().toString().equals(""))
                    mNumber.setHint("  Mobile Number");
                passwd.setHint("");
                break;

        }
        return true;
    }*/

}
