package com.bitblue.crebit.servicespage.fragments.transactionSummary.checkStatus;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bitblue.apinames.API;
import com.bitblue.crebit.R;
import com.bitblue.jsonparse.JSONParser;
import com.bitblue.nullcheck.Check;
import com.bitblue.requestparam.RefundOrTransParams;
import com.bitblue.response.RefundOrTransResponse;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CheckStatus extends Activity implements View.OnClickListener {
    private String Status, OperatorName, Comments, Key, TransId, TypeId, UserId;
    private String RespStatus, Message, CyberTranID, OperatorId;
    private int cur;
    private TextView tvStatus, tvTranid, tvMessage;
    private EditText etcomment;
    private Button bchkstat, bsubrefreq;
    private LinearLayout checkstatus, chkstatButton;
    private JSONParser jsonParser;
    private ArrayList<NameValuePair> nameValuePairs;
    private JSONObject jsonResponse;
    private RefundOrTransParams refParams;
    private RefundOrTransResponse refResponse;
    private SharedPreferences prefs;
    private final static String MY_PREFS = "mySharedPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_status);
        Status = getIntent().getStringExtra("Status");
        Log.e("Status", Status);
        OperatorName = getIntent().getStringExtra("OperatorName");
        Log.e("OperatorName", OperatorName);
        TransId = getIntent().getStringExtra("TransId");
        Log.e("TransId", TransId);
        initViews();
        if (!(Status.equals("Success"))) {  //This is to test the crebit api since check status is not working poroperly
            finish();
        }
        if (Check.ifCrebitApi(OperatorName)) {
            chkstatButton.setVisibility(View.GONE);
            checkstatus.setVisibility(View.GONE);
        } else {
            chkstatButton.setVisibility(View.VISIBLE);
            checkstatus.setVisibility(View.INVISIBLE);
        }
    }

    private void initViews() {

        tvStatus = (TextView) findViewById(R.id.tv_cs_Status);
        tvTranid = (TextView) findViewById(R.id.tv_cs_TransId);
        tvMessage = (TextView) findViewById(R.id.tv_cs_Message);
        etcomment = (EditText) findViewById(R.id.et_cs_comment);
        bchkstat = (Button) findViewById(R.id.b_cs_checkstatus);
        bsubrefreq = (Button) findViewById(R.id.b_subrefreq);
        prefs = getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
        UserId = prefs.getString("userId", "");
        Log.e("Uid", UserId);
        TypeId = prefs.getString("uType", "");
        Log.e("typeid", TypeId);
        Key = prefs.getString("userKey", "");
        Log.e("key", Key);
        bchkstat.setOnClickListener(this);
        bsubrefreq.setOnClickListener(this);
        checkstatus = (LinearLayout) findViewById(R.id.ll_checkstatus);
        chkstatButton = (LinearLayout) findViewById(R.id.ll_checkstatusButton);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.b_cs_checkstatus:
                cur = R.id.b_cs_checkstatus;
                Comments = etcomment.getText().toString();
                if (Check.ifNull(Comments))
                    Comments = "Comments";
                new checkrefundresult().execute();
                break;
            case R.id.b_subrefreq:
                cur = R.id.b_subrefreq;
                Comments = etcomment.getText().toString();
                if (Check.ifNull(Comments)) {
                    etcomment.setText("");
                    etcomment.setHint("  Enter request for refund");
                    etcomment.setHintTextColor(getResources().getColor(R.color.red));
                    break;
                }
                new checkrefundresult().execute();
                break;
        }
    }

    private class checkrefundresult extends AsyncTask<String, String, String> {
        ProgressDialog dialog = new ProgressDialog(CheckStatus.this);

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Please wait ...");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            jsonParser = new JSONParser();
            refParams = new RefundOrTransParams(Comments, Key, TransId, TypeId, UserId);
            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("UserId", UserId));
            nameValuePairs.add(new BasicNameValuePair("Key", Key));
            nameValuePairs.add(new BasicNameValuePair("TransId", TransId));
            nameValuePairs.add(new BasicNameValuePair("TypeId", TypeId));
            nameValuePairs.add(new BasicNameValuePair("Comments", Comments));
            jsonResponse = jsonParser.makeHttpPostRequestforJsonObject(API.DASHBOARD_REFUNDORTRANS_STATUS, nameValuePairs);
            try {
                refResponse = new RefundOrTransResponse(jsonResponse.getString("typeId"),
                        jsonResponse.getString("status"), jsonResponse.getString("message"),
                        jsonResponse.getString("cybertransId"), jsonResponse.getString("operatorId"));

                Log.e("Response=", "\nTypeid: " +
                        jsonResponse.getString("typeId") + "\nMessage: " +
                        jsonResponse.getString("message") + "\nCybertransid: " +
                        jsonResponse.getString("cybertransId") + "\nStatus: " +
                        jsonResponse.getString("status"));
                TypeId = refResponse.getTypeId();
                RespStatus = refResponse.getStatus();
                Message = refResponse.getMessage();
                CyberTranID = refResponse.getCybertransId();
                OperatorId = refResponse.getOperatorId();

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String status) {
            dialog.dismiss();

            tvStatus.setText("Status: " + RespStatus);
            tvTranid.setText("TransId: " + CyberTranID);
            tvMessage.setText("Message: " + Message);
            if ((cur == R.id.b_subrefreq) || Check.ifCrebitApi(OperatorName)) {
                chkstatButton.setVisibility(View.INVISIBLE);
                checkstatus.setVisibility(View.INVISIBLE);
            } else {
                chkstatButton.setVisibility(View.VISIBLE);
                checkstatus.setVisibility(View.VISIBLE);
            }
        }
    }

}
