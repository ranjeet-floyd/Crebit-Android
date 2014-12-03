package com.bitblue.crebit.services.menuitem;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bitblue.crebit.R;

public class ChangePassword extends ActionBarActivity implements View.OnClickListener {
    private TextView old_pass, new_pass;
    private EditText et_oldPass, et_newPass;
    private Button change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        initViews();
    }

    private void initViews() {
        old_pass = (TextView) findViewById(R.id.tv_cp_oldpass);
        new_pass = (TextView) findViewById(R.id.tv_cp_newpass);
        et_oldPass = (EditText) findViewById(R.id.et_cp_oldpass);
        et_newPass = (EditText) findViewById(R.id.et_cp_newpass);
        change = (Button) findViewById(R.id.b_cp_change);
        change.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.b_cp_change:
                break;

        }
    }
}
