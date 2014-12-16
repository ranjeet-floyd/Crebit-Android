package com.bitblue.crebit.servicespage.activities.Electriciti;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bitblue.apinames.API;
import com.bitblue.crebit.R;
import com.bitblue.jsonparse.JSONParser;
import com.bitblue.nullcheck.Check;
import com.bitblue.requestparam.RelianceParams;
import com.bitblue.response.RelianceResponse;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RelianceMum extends Activity implements View.OnClickListener {
    private String UserId, Key, Account, Amount, Number, OperatorID = "41";
    private static final String Source = "2";
    TextView tvcustAccNo, tvcycCode, tvAmount;
    EditText etcustAccNo, etcycCode, etAmount;
    Button bpayBill;
    private JSONParser jsonParser;
    private JSONObject jsonResponse;
    private List<NameValuePair> nameValuePairs;
    private RelianceParams relianceParams;
    private RelianceResponse relianceResponse;
    private String TransId, Message, AvailableBalance;
    private int StatusCode;
    private SharedPreferences prefs;
    private final static String MY_PREFS = "mySharedPrefs";
    private TextView transId, message, statcode, availablebal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reliance_mum);
        initViews();
    }

    private void initViews() {
        tvcustAccNo = (TextView) findViewById(R.id.tv_elec_reliance_cust_acc_no);
        tvcycCode = (TextView) findViewById(R.id.tv_elec_reliance_cycode);
        tvAmount = (TextView) findViewById(R.id.tv_elec_reliance_amount);
        etcustAccNo = (EditText) findViewById(R.id.et_elec_reliance_cust_acc_no);
        etcycCode = (EditText) findViewById(R.id.et_elec_reliance_cycode);
        etAmount = (EditText) findViewById(R.id.et_elec_reliance_amount);

        bpayBill = (Button) findViewById(R.id.b_elec_reliance_payBill);
        bpayBill.setOnClickListener(this);
        prefs = getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
        UserId = prefs.getString("userId", "");
        Key = prefs.getString("userKey", "");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.b_elec_reliance_payBill:
                Number = etcustAccNo.getText().toString();
                Account = etcycCode.getText().toString();
                Amount = etAmount.getText().toString();
                if (Check.ifAccountNumberIncorrect(Number)) {

                }
                if (Check.ifNull(Account)) {

                }
                if (Check.ifEmpty(Double.parseDouble(Amount))) {
                }
                new retrieveData().execute();

                break;
        }
    }

    private class retrieveData extends AsyncTask<String, String, String> {
        ProgressDialog dialog = new ProgressDialog(RelianceMum.this);

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Please Wait...");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            jsonParser = new JSONParser();
            relianceParams = new RelianceParams(UserId, Key, OperatorID, Number, Amount, Source);
            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("UserId", UserId));
            nameValuePairs.add(new BasicNameValuePair("Key", Key));
            nameValuePairs.add(new BasicNameValuePair("OperatorId", OperatorID));
            nameValuePairs.add(new BasicNameValuePair("Number", Number));
            nameValuePairs.add(new BasicNameValuePair("Amount", String.valueOf(Amount)));
            nameValuePairs.add(new BasicNameValuePair("Source", Source));
            jsonResponse = jsonParser.makeHttpPostRequestforJsonObject(API.DASHBOARD_SERVICE, nameValuePairs);
            try {
                relianceResponse = new RelianceResponse(jsonResponse.getString("transId"),
                        jsonResponse.getString("message"),
                        jsonResponse.getInt("statusCode"),
                        jsonResponse.getString("availableBalance"));

                TransId = relianceResponse.getTransId();
                Message = relianceResponse.getMessage();
                StatusCode = relianceResponse.getStatusCode();
                AvailableBalance = relianceResponse.getAvailableBalance();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return String.valueOf(StatusCode);
        }

        @Override
        protected void onPostExecute(String StatusCode) {
            dialog.dismiss();
            if (StatusCode.equals("0") || StatusCode.equals("-1")) {
                new AlertDialog.Builder(RelianceMum.this)
                        .setTitle("Error")
                        .setMessage("Request Not Completed.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create().show();
            } else if (StatusCode.equals("1")) {
                new AlertDialog.Builder(RelianceMum.this)
                        .setTitle("Success")
                        .setMessage("Request Completed.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create().show();
                transId.setText("TransId: " + TransId);
                message.setText("Message: " + Message);
                statcode.setText("StatusCode: " + StatusCode);
                availablebal.setText("AvailableBalance: " + AvailableBalance);
            } else if (StatusCode.equals("2")) {
                new AlertDialog.Builder(RelianceMum.this)
                        .setTitle("Error")
                        .setMessage("Insufficient Balance")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create().show();
                dialog.dismiss();
            }
        }
    }
}
