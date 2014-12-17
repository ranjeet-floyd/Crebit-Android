package com.bitblue.crebit.servicespage.menuitem;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bitblue.apinames.API;
import com.bitblue.crebit.R;
import com.bitblue.jsonparse.JSONParser;
import com.bitblue.nullcheck.Check;
import com.bitblue.requestparam.ChangePassParams;
import com.bitblue.response.ChangePassResponse;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChangePassword extends ActionBarActivity implements View.OnClickListener {
    private TextView old_pass, new_pass;
    private String userId, key, olpass, nwpass, status;
    private EditText et_oldPass, et_newPass;
    private Button change;

    private ChangePassParams changePassParams;
    private ChangePassResponse changePassResponse;

    private JSONParser jsonParser;
    private JSONObject jsonResponse;
    private List<NameValuePair> nameValuePairs;

    private SharedPreferences prefs;
    private final static String MY_PREFS = "mySharedPrefs";


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
        et_oldPass.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View view, boolean hasfocus) {
                if (hasfocus) {

                    view.setBackgroundResource(R.drawable.edittext_focus);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_lostfocus);
                }
            }
        });
        et_newPass = (EditText) findViewById(R.id.et_cp_newpass);
        et_newPass.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View view, boolean hasfocus) {
                if (hasfocus) {

                    view.setBackgroundResource(R.drawable.edittext_focus);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_lostfocus);
                }
            }
        });
        change = (Button) findViewById(R.id.b_cp_change);
        change.setOnClickListener(this);
        prefs = getSharedPreferences(MY_PREFS, MODE_PRIVATE);
        userId = prefs.getString("userId", "");
        key = prefs.getString("userKey", "");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.b_cp_change:
                olpass = et_oldPass.getText().toString();
                nwpass = et_newPass.getText().toString();
                if (Check.ifNull(olpass)) {
                    et_oldPass.setText("");
                    et_oldPass.setHint(" Enter old password");
                    et_oldPass.setHintTextColor(getResources().getColor(R.color.red));
                    break;
                }
                if (Check.ifNull(nwpass)) {
                    et_newPass.setText("");
                    et_newPass.setHint(" Enter new password");
                    et_newPass.setHintTextColor(getResources().getColor(R.color.red));
                    break;
                }
                new changePass().execute();
                break;
        }
    }

    private class changePass extends AsyncTask<String, String, String> {
        ProgressDialog dialog = new ProgressDialog(ChangePassword.this);

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Please Wait...");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            changePassParams = new ChangePassParams(userId, key, olpass, nwpass);
            jsonParser = new JSONParser();
            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("UserId", userId));
            nameValuePairs.add(new BasicNameValuePair("Key", key));
            nameValuePairs.add(new BasicNameValuePair("OPass", olpass));
            nameValuePairs.add(new BasicNameValuePair("NPass", nwpass));
            jsonResponse = jsonParser.makeHttpPostRequestforJsonObject(API.DHS_CHANGE_PASSWORD, nameValuePairs);
            try {
                changePassResponse = new ChangePassResponse(jsonResponse.getString("status"));
                status = changePassResponse.getStatus();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return status;
        }

        @Override
        protected void onPostExecute(String status) {
            dialog.dismiss();
            if (status.equals("1")) {
                new AlertDialog.Builder(ChangePassword.this)
                        .setTitle("Password Recovery")
                        .setMessage("  Password has been changed\n" +
                                "\t\t\tProceed to Login Page. ")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create().show();
            } else {
                new AlertDialog.Builder(ChangePassword.this)
                        .setTitle("Error")
                        .setMessage(" Check the old Password ")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create().show();

            }
        }
    }

}
