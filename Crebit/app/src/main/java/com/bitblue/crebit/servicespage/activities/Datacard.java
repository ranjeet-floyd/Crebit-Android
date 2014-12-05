package com.bitblue.crebit.servicespage.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.bitblue.crebit.R;

public class Datacard extends ActionBarActivity implements View.OnClickListener {
    private TextView operator, number, amount;
    private EditText et_number, et_amount;
    private Spinner sp_operator;
    private Button recharge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datacard);
        initViews();
    }

    private void initViews() {
        operator = (TextView) findViewById(R.id.tv_dc_operator);
        number = (TextView) findViewById(R.id.tv_dc_number);
        amount = (TextView) findViewById(R.id.tv_dc_amount);

        et_number = (EditText) findViewById(R.id.et_dc_number);
        et_amount = (EditText) findViewById(R.id.et_dc_amount);

        sp_operator = (Spinner) findViewById(R.id.sp_dc_operator);
        recharge = (Button) findViewById(R.id.b_dc_recharge);
        recharge.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.b_dc_recharge:

                break;

        }
    }
}
