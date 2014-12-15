package com.bitblue.crebit.loginpage;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bitblue.apinames.API;
import com.bitblue.crebit.R;
import com.bitblue.jsonparse.JSONParser;
import com.bitblue.nullcheck.Check;
import com.bitblue.requestparam.ForgotPassParam;
import com.bitblue.requestparam.SignUpParams;
import com.bitblue.response.ForgotPassResponse;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ForgotPass extends ActionBarActivity implements View.OnClickListener {
    private JSONParser jsonParser = new JSONParser();
    private EditText etmobileNumber;
    private Button bforgotPassword;
    private String mobileNumber, status;
    private NotificationManager NM;
    private List<NameValuePair> nameValuePairs;
    private ForgotPassParam forgotPassParam;
    private JSONObject jsonResponse;
    private ForgotPassResponse forgotPassResponse;
    private SignUpParams signUpParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);
        initViews();
    }

    public void initViews() {
        etmobileNumber = (EditText) findViewById(R.id.et_MobileNumber);
        bforgotPassword = (Button) findViewById(R.id.b_forgotpassSubmit);
        bforgotPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.b_forgotpassSubmit:
                mobileNumber = etmobileNumber.getText().toString();
                if (Check.ifNumberInCorrect(mobileNumber)) {
                    clearField(etmobileNumber);
                    etmobileNumber.setHint(" Enter Correct Number");
                    etmobileNumber.setHintTextColor(getResources().getColor(R.color.red));
                    break;
                }
                new retrievePass().execute();
                break;
        }
    }

    private class retrievePass extends AsyncTask<String, String, String> {
        ProgressDialog dialog = new ProgressDialog(ForgotPass.this);

        @Override
        protected void onPreExecute() {
            dialog.setTitle("Please Wait");
            dialog.setMessage("Sending Message...");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            forgotPassParam = new ForgotPassParam(mobileNumber);
            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("Mobile", mobileNumber));
            jsonResponse = jsonParser.makeHttpPostRequestforJsonObject(API.DHS_FORGOT_PASSWORD, nameValuePairs);
            try {
                forgotPassResponse = new ForgotPassResponse(jsonResponse.getString("status"));
                status = forgotPassResponse.getStatus();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String name) {
            dialog.dismiss();
            if (status.equals("2")) {
                notifyuser();
            }
        }
    }

    public void notifyuser() {
        new AlertDialog.Builder(ForgotPass.this)
                .setTitle("Password Recovery")
                .setMessage("  A message has been sent to: " + mobileNumber +
                        "\t\t\tCheck SMS for password. ")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create().show();
        String title = "MESSAGE SENT";
        String subject = "Message Sent";
        String body = "Check received SMS for Password";
        NM = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notify = new Notification(R.drawable.crebit, title, System.currentTimeMillis());
        PendingIntent pending = PendingIntent.getActivity(
                getApplicationContext(), 0, new Intent(), 0);
        notify.setLatestEventInfo(getApplicationContext(), subject, body, pending);
        NM.notify(0, notify);
    }
    private void clearField(EditText et){
        et.setText("");
    }
}
