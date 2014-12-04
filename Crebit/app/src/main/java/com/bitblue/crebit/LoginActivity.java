package com.bitblue.crebit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bitblue.apinames.API;
import com.bitblue.crebit.services.service;
import com.bitblue.jsonparse.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class LoginActivity extends ActionBarActivity implements View.OnClickListener {
    private JSONParser jsonParser = new JSONParser();
    private JSONObject jsonObject = null;
    private JSONArray jsonArray = null;
    private TextView existinguser;
    private EditText mNumber, passwd;
    private Button login, forgotPass, signUp;
    private String logoutmessage;
    private String mobile, pass;
    private final String VERSION = "1.0";
    private String name;

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
                mobile = mNumber.getText().toString();
                pass = passwd.getText().toString();
                new retrieveData().execute();
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

    private class retrieveData extends AsyncTask<String, String, String> {
        ProgressDialog dialog = new ProgressDialog(LoginActivity.this);

        @Override
        protected void onPreExecute() {
            dialog.setTitle("Please Wait");
            dialog.setMessage("Signing in...");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setCancelable(true);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("Mobile", mobile);
            params.put("Pass", pass);
            params.put("Version", VERSION);
            jsonArray = jsonParser.makeHttpRequest(API.DHS_LOGIN, params);
            try {
                name = jsonArray.getJSONObject(0).getString("name");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return name;
        }

        @Override
        protected void onPostExecute(String username) {
            dialog.dismiss();
            name = username;
            openServiceIntent(name);
        }
    }

    public void openServiceIntent(String name) {
        Intent openService = new Intent(LoginActivity.this, service.class);
        openService.putExtra("username", name);
        startActivity(openService);


    }
}
