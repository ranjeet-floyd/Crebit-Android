package com.bitblue.crebit;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


public class SignUp extends ActionBarActivity implements View.OnClickListener{
    TextView tvaccType, tvname, tvmobNum, tvpasswd;
    Spinner enterprise, personal;
    EditText etname, etmobNum, etpasswd;
    Button bSignUpSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setIcon(R.drawable.crebit);
        initViews();
    }

    public void initViews() {
        tvaccType = (TextView) findViewById(R.id.tv_accType);
        tvname = (TextView) findViewById(R.id.tv_name);
        tvmobNum = (TextView) findViewById(R.id.tv_mobNum);
        tvpasswd = (TextView) findViewById(R.id.tv_passwd);
        etname = (EditText) findViewById(R.id.et_name);
        etpasswd = (EditText) findViewById(R.id.et_passwd);
        etmobNum = (EditText) findViewById(R.id.et_mobNum);
        bSignUpSubmit = (Button) findViewById(R.id.b_signUpSubmit);
/*        etname.setOnTouchListener(this);
        etmobNum.setOnTouchListener(this);
        etpasswd.setOnTouchListener(this);*/
        bSignUpSubmit.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.sp_accType:
                break;
            case R.id.b_signUpSubmit:
                break;

        }
    }

   /* @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()) {
            case R.id.et_name:
                if (etpasswd.getText().toString().equals(""))
                    etpasswd.setHint("  Enter Password");
                if (etmobNum.getText().toString().equals(""))
                    etmobNum.setHint("  Enter Mobile Number");
                etname.setHint("");
                break;
            case R.id.et_passwd:
                if (etmobNum.getText().toString().equals(""))
                    etmobNum.setHint("  Enter Mobile Number");
                if (etname.getText().toString().equals(""))
                    etname.setHint("  Enter Name");
                etpasswd.setHint("");
                break;
            case R.id.et_mobNum:
                if (etpasswd.getText().toString().equals(""))
                    etpasswd.setHint("  Enter Password");
                if (etname.getText().toString().equals(""))
                    etname.setHint("  Enter Name");
                etmobNum.setHint("");
                break;

        }
        return true;
    }*/
}
