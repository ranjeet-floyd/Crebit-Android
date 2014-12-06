package com.bitblue.crebit.loginpage;

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
import com.bitblue.crebit.R;
import com.bitblue.crebit.servicespage.service;
import com.bitblue.jsonparse.JSONParser;
import com.bitblue.nullcheck.Check;
import com.bitblue.requestparam.LoginParams;
import com.bitblue.response.LoginResponse;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class LoginActivity extends ActionBarActivity implements View.OnClickListener {
    private JSONParser jsonParser = new JSONParser();
    private JSONArray jsonArray = null;
    private TextView existinguser;
    private EditText mNumber, passwd;
    private Button login, forgotPass, signUp;
    private String logoutmessage;
    private String mobile, pass;
    private String Version = "1.0";
    private String userName, availableBalance;
    private LoginParams loginParams;
    private JSONObject JsonResponse;
    private LoginResponse loginResponse;
    private List<NameValuePair> nameValuePairs;

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
                if (Check.ifNumberInCorrect(mobile)) {
                    mNumber.setHint(" Mobile Number Required");
                    mNumber.setHintTextColor(getResources().getColor(R.color.red));
                }
                if (Check.ifNull(pass)) {
                    passwd.setHint(" Password Required");
                    passwd.setHintTextColor(getResources().getColor(R.color.red));
                    break;
                }
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
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            loginParams = new LoginParams(mobile, pass, Version);
            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("Mobile", mobile));
            nameValuePairs.add(new BasicNameValuePair("Pass", pass));
            nameValuePairs.add(new BasicNameValuePair("Version", Version));
            jsonArray = jsonParser.makeHttpPostRequest(API.DHS_LOGIN, nameValuePairs);
            try {
                JsonResponse = jsonArray.getJSONObject(0);
                loginResponse = new LoginResponse(JsonResponse.getBoolean("isSupported"),
                        JsonResponse.getBoolean("isActive"),
                        JsonResponse.getString("userId"),
                        JsonResponse.getString("availableBalance"),
                        JsonResponse.getBoolean("isUpdated"),
                        JsonResponse.getBoolean("isDataUpdated"),
                        JsonResponse.getString("name"),
                        JsonResponse.getString("userKey"));
                userName = loginResponse.getName();
                availableBalance = loginResponse.getAvailableBalance();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return userName;
        }

        @Override
        protected void onPostExecute(String name) {
            dialog.dismiss();
            userName = name;
            openServiceIntent(userName);
        }
    }

    public void openServiceIntent(String name) {
        Intent openService = new Intent(LoginActivity.this, service.class);
        openService.putExtra("username", name);
        openService.putExtra("availableBalance", availableBalance);
        startActivity(openService);
    }
}
