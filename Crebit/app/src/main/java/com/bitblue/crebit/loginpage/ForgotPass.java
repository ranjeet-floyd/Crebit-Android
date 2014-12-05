package com.bitblue.crebit.loginpage;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
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
import com.bitblue.requestparam.ForgotPassParam;
import com.bitblue.response.ForgotPassResponse;
import com.bitblue.sqlite.SQLiteHelper;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ForgotPass extends ActionBarActivity implements View.OnClickListener {
    private JSONParser jsonParser = new JSONParser();
    private JSONArray jsonArray = null;
    private EditText etmobileNumber;
    private Button bforgotPassword;
    private String mobileNumber, password;
    private NotificationManager NM;
    private List<NameValuePair> nameValuePairs;
    private ForgotPassParam forgotPassParam;
    private JSONObject JsonResponse;
    private ForgotPassResponse forgotPassResponse;
    private SQLiteHelper sqLiteHelper = new SQLiteHelper(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);
        initViews();
    }

    public void initViews() {
        etmobileNumber = (EditText) findViewById(R.id.et_MobileNumber);
        bforgotPassword = (Button) findViewById(R.id.b_forgotpassSubmit);
        mobileNumber = etmobileNumber.getText().toString();
        bforgotPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.b_forgotpassSubmit:
                if (mobileNumber.equals("")) {
                    etmobileNumber.setHint(" Mobile Number Required");
                    etmobileNumber.setHintTextColor(getResources().getColor(R.color.red));
                    break;
                }


                break;

        }
    }

    public void notifyuser(View vobj) {
        String title = "Password";
        String subject = "";
        String body = "";
        NM = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notify = new Notification(android.R.drawable.
                stat_notify_more, title, System.currentTimeMillis());
        PendingIntent pending = PendingIntent.getActivity(
                getApplicationContext(), 0, new Intent(), 0);
        notify.setLatestEventInfo(getApplicationContext(), subject, body, pending);
        NM.notify(0, notify);
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
            jsonArray = jsonParser.makeHttpPostRequest(API.DHS_FORGOT_PASSWORD, nameValuePairs);
            try {
                JsonResponse = jsonArray.getJSONObject(0);
                forgotPassResponse = new ForgotPassResponse(mobileNumber);
                password = "";
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return password;
        }

        @Override
        protected void onPostExecute(String name) {
            dialog.dismiss();
            View view = null;
            notifyuser(view);
        }
    }
}
