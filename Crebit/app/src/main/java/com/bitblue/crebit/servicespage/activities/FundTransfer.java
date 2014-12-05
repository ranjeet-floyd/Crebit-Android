package com.bitblue.crebit.servicespage.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bitblue.crebit.R;

public class FundTransfer extends ActionBarActivity implements View.OnClickListener {
    private TextView number, amount;
    private EditText et_number, et_amount;
    private Button transfer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fund_transfer);
        initViews();
    }

    private void initViews() {
        number = (TextView) findViewById(R.id.tv_ft_number);
        amount = (TextView) findViewById(R.id.tv_ft_amount);

        et_number = (EditText) findViewById(R.id.et_ft_number);
        et_amount = (EditText) findViewById(R.id.et_ft_amount);

        transfer = (Button) findViewById(R.id.b_ft_recharge);
        transfer.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.b_dth_recharge:

                break;

        }
    }
}
