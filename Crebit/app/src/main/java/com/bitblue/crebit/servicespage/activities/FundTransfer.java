package com.bitblue.crebit.servicespage.activities;

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
import com.bitblue.requestparam.FundTransferParams;
import com.bitblue.response.FundTransferResponse;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FundTransfer extends ActionBarActivity implements View.OnClickListener {
    private TextView number, amount, status, availableBalance;
    private EditText et_number, et_amount;
    private Button transfer;

    private String UserId, Key, MobileTo, Amount, UserTypeA, UserTypeB;

    private String Status, AvailableBalance;

    private JSONParser jsonParser;
    private JSONArray jsonArray;
    private JSONObject jsonResponse;
    private FundTransferResponse fundTransferResponse;
    private FundTransferParams fundTransferParams;
    private List<NameValuePair> nameValuePairs;

    private SharedPreferences prefs;
    private final static String MY_PREFS = "mySharedPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fund_transfer);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        initViews();
    }

    private void initViews() {
        number = (TextView) findViewById(R.id.tv_ft_number);
        amount = (TextView) findViewById(R.id.tv_ft_amount);

        et_number = (EditText) findViewById(R.id.et_ft_number);
        et_number.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View view, boolean hasfocus) {
                if (hasfocus) {

                    view.setBackgroundResource(R.drawable.edittext_focus);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_lostfocus);
                }
            }
        });
        et_amount = (EditText) findViewById(R.id.et_ft_amount);
        et_amount.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View view, boolean hasfocus) {
                if (hasfocus) {

                    view.setBackgroundResource(R.drawable.edittext_focus);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_lostfocus);
                }
            }
        });

        transfer = (Button) findViewById(R.id.b_ft_recharge);
        transfer.setOnClickListener(this);

        prefs = getSharedPreferences(MY_PREFS, MODE_PRIVATE);
        UserId = prefs.getString("userId", "");
        Key = prefs.getString("userKey", "");

        if (UserId.equals("1")) {
            UserTypeA = "Enterprise";
            UserTypeB = "Personal";
        } else if (UserId.equals("2")) {
            UserTypeA = "Personal";
            UserTypeB = "Enterprise";
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.b_ft_recharge:
                MobileTo = et_number.getText().toString();
                Amount = et_amount.getText().toString();
                if (Check.ifNull(Amount)) {
                    et_amount.setHintTextColor(getResources().getColor(R.color.red));
                    break;
                }
                if (Check.ifNumberInCorrect(MobileTo)) {
                    et_number.setText("");
                    et_number.setHint(" Enter correct number");
                    et_number.setHintTextColor(getResources().getColor(R.color.red));
                    break;
                }
                new retrievefundtransferdata().execute();
                break;

        }
    }

    private class retrievefundtransferdata extends AsyncTask<String, String, String> {
        ProgressDialog dialog = new ProgressDialog(FundTransfer.this);

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
            fundTransferParams = new FundTransferParams(UserId, Key, MobileTo, Amount);
            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("userId", UserId));
            nameValuePairs.add(new BasicNameValuePair("key", Key));
            nameValuePairs.add(new BasicNameValuePair("mobileTo", MobileTo));
            nameValuePairs.add(new BasicNameValuePair("amount", Amount));
            jsonArray = jsonParser.makeHttpPostRequest(API.DASHBOARD_TRANSFER, nameValuePairs);
            try {
                jsonResponse = jsonArray.getJSONObject(0);
                fundTransferResponse = new FundTransferResponse(jsonResponse.getString("status"),
                        jsonResponse.getString("availableBalance"));
                Status = fundTransferResponse.getStatus();
                AvailableBalance = fundTransferResponse.getAvailableBalance();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return Status;
        }

        @Override
        protected void onPostExecute(String Status) {
            dialog.dismiss();
            switch (Integer.parseInt(Status)) {
                case 1:
                case 2:
                    new AlertDialog.Builder(FundTransfer.this)
                            .setTitle("Success")
                            .setMessage(" Transfer Completed. " +
                                    "\n Available Balance" + AvailableBalance)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).create().show();
                    break;
                case 3:
                    new AlertDialog.Builder(FundTransfer.this)
                            .setTitle("Error")
                            .setMessage("Not Enough Balance" +
                                    "\n Available Balance" + AvailableBalance)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).create().show();
                    break;
                case 4:
                    new AlertDialog.Builder(FundTransfer.this)
                            .setTitle("Error")
                            .setMessage("Mobile Number Incorrect")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).create().show();
                    break;
                case 5:
                    new AlertDialog.Builder(FundTransfer.this)
                            .setTitle("Error")
                            .setMessage(" Cannot Transfer From " + UserTypeA + " to " + UserTypeB)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).create().show();
                    break;
                case 6:
                    new AlertDialog.Builder(FundTransfer.this)
                            .setTitle("Error")
                            .setMessage("Cannot Transfer to the same account")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).create().show();

                    break;
            }
        }
    }
}
