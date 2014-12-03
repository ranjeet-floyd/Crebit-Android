package com.bitblue.crebit.services.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.bitblue.crebit.R;

public class Dth extends ActionBarActivity implements View.OnClickListener {
    private TextView operator, number, amount;
    private EditText et_number, et_amount;
    private Spinner sp_operator;
    private Button recharge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dth);
        initViews();
    }

    private void initViews() {
        operator = (TextView) findViewById(R.id.tv_dth_operator);
        number = (TextView) findViewById(R.id.tv_dth_number);
        amount = (TextView) findViewById(R.id.tv_dth_amount);

        et_number = (EditText) findViewById(R.id.et_dth_number);
        et_amount = (EditText) findViewById(R.id.et_dth_amount);

        sp_operator = (Spinner) findViewById(R.id.sp_dth_operator);
        recharge = (Button) findViewById(R.id.b_dth_recharge);
        recharge.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.b_dth_recharge:

                break;

        }
    }
}
