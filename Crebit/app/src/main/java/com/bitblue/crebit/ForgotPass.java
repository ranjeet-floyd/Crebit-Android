package com.bitblue.crebit;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class ForgotPass extends ActionBarActivity implements View.OnClickListener{
    EditText etmobileNumber;
    Button bforgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setIcon(R.drawable.crebit);
        initViews();
    }

    public void initViews() {
        etmobileNumber = (EditText) findViewById(R.id.et_MobileNumber);
        bforgotPassword = (Button) findViewById(R.id.b_forgotpassSubmit);
        bforgotPassword.setOnClickListener(this);
/*
        etmobileNumber.setOnTouchListener(this);
*/

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.b_forgotpassSubmit:

                break;

        }
    }

/*    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()) {
            case R.id.et_MobileNumber:
                etmobileNumber.setHint("");
                break;
        }
        return true;
    }*/
}
