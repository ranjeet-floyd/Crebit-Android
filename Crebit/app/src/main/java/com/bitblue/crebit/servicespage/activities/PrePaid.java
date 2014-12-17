package com.bitblue.crebit.servicespage.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bitblue.IDs.prePaid;
import com.bitblue.apinames.API;
import com.bitblue.crebit.R;
import com.bitblue.jsonparse.JSONParser;
import com.bitblue.nullcheck.Check;
import com.bitblue.requestparam.PrePaidParams;
import com.bitblue.response.PrePaidResponse;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PrePaid extends ActionBarActivity implements View.OnClickListener {
    private TextView operator, number, amount;
    private EditText et_number, et_amount;
    private Button recharge, operatorType;
    private TextView transId, message, statcode, availablebal;

    private String UserId, Key, OperatorId, Number, Amount, Account = "";
    private static final String SOURCE = "2";

    private String[] items;
    private ArrayAdapter<String> adapter;
    private JSONParser jsonParser;
    private JSONObject jsonResponse;
    private PrePaidResponse prePaidResponse;
    private PrePaidParams prePaidParams;
    private List<NameValuePair> nameValuePairs;

    private String TransId, Message;
    private int StatusCode;
    private String AvailableBalance;

    private SharedPreferences prefs;
    private final static String MY_PREFS = "mySharedPrefs";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_paid);
        items = getResources().getStringArray(R.array.prepaid_operator);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        initViews();
    }

    private void initViews() {
        operator = (TextView) findViewById(R.id.tv_pre_operator);
        number = (TextView) findViewById(R.id.tv_pre_number);
        amount = (TextView) findViewById(R.id.tv_pre_amount);

        et_number = (EditText) findViewById(R.id.et_pre_number);
        et_number.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View view, boolean hasfocus) {
                if (hasfocus) {

                    view.setBackgroundResource(R.drawable.edittext_focus);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_lostfocus);
                }
            }
        });
        et_amount = (EditText) findViewById(R.id.et_pre_amount);
        et_amount.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View view, boolean hasfocus) {
                if (hasfocus) {

                    view.setBackgroundResource(R.drawable.edittext_focus);
                } else {
                    view.setBackgroundResource(R.drawable.edittext_lostfocus);
                }
            }
        });

        recharge = (Button) findViewById(R.id.b_pre_recharge);
        operatorType = (Button) findViewById(R.id.b_pre_operator);

        recharge.setOnClickListener(this);
        operatorType.setOnClickListener(this);
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, items);

        prefs = getSharedPreferences(MY_PREFS, MODE_PRIVATE);
        UserId = prefs.getString("userId", "");
        Key = prefs.getString("userKey", "");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.b_pre_operator:
                new AlertDialog.Builder(this)
                        .setTitle("Select Operator")
                        .setAdapter(adapter, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int position) {
                                OperatorId = prePaid.getPrePaidOperatorId(position);
                                operatorType.setText(items[position]);
                                dialog.dismiss();
                            }
                        }).create().show();
                break;
            case R.id.b_pre_recharge:
                Number = et_number.getText().toString();
                try {
                    Amount = et_amount.getText().toString();
                } catch (Exception e) {
                    Amount = "0";
                }
                if (operatorType.getText().equals("Select")) {
                    operator.setTextColor(getResources().getColor(R.color.red));
                    break;
                }
                if (Check.ifNull(Amount)) {
                    et_amount.setHintTextColor(getResources().getColor(R.color.red));
                    break;
                }
                if (Check.ifNumberInCorrect(Number)) {
                    et_number.setText("");
                    et_number.setHint(" Enter correct number");
                    et_number.setHintTextColor(getResources().getColor(R.color.red));
                    break;
                }
                new retrieveprepaiddata().execute();

                break;

        }
    }

    private class retrieveprepaiddata extends AsyncTask<String, String, String> {
        ProgressDialog dialog = new ProgressDialog(PrePaid.this);

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
            prePaidParams = new PrePaidParams(UserId, Key, OperatorId, Number, Amount, SOURCE);
            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("userId", UserId));
            nameValuePairs.add(new BasicNameValuePair("key", Key));
            nameValuePairs.add(new BasicNameValuePair("operatorId", OperatorId));
            nameValuePairs.add(new BasicNameValuePair("number", Number));
            nameValuePairs.add(new BasicNameValuePair("amount", Amount));
            nameValuePairs.add(new BasicNameValuePair("source", SOURCE));
            nameValuePairs.add(new BasicNameValuePair("account", ""));
            Log.e("Params: ", UserId + " " + Key + " " + OperatorId + " " + Number + " " + Amount + " " + Account + " " + SOURCE);
            jsonResponse = jsonParser.makeHttpPostRequestforJsonObject(API.DASHBOARD_SERVICE, nameValuePairs);
            try {
                prePaidResponse = new PrePaidResponse(jsonResponse.getString("transId"),
                        jsonResponse.getString("message"),
                        jsonResponse.getInt("statusCode"),
                        jsonResponse.getString("availableBalance"));

                TransId = prePaidResponse.getTransId();
                Message = prePaidResponse.getMessage();
                StatusCode = prePaidResponse.getStatusCode();
                AvailableBalance = prePaidResponse.getAvailableBalance();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return String.valueOf(StatusCode);
        }

        @Override
        protected void onPostExecute(String StatusCode) {
            dialog.dismiss();
            if (StatusCode.equals("0") || StatusCode.equals("-1")) {
                new AlertDialog.Builder(PrePaid.this)
                        .setTitle("Error")
                        .setMessage("Request Not Completed." +
                                "\n TransID: " + TransId +
                                "\nMessage: " + Message +
                                "\nStatus Code: " + StatusCode)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create().show();
            } else if (StatusCode.equals("1")) {
                new AlertDialog.Builder(PrePaid.this)
                        .setTitle("Success")
                        .setMessage("Request Completed.\n" +
                                "Transaction ID: " + TransId +
                                "\nMessage: " + Message +
                                "\nAvailable Balance: " + AvailableBalance)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create().show();
            } else if (StatusCode.equals("2")) {
                new AlertDialog.Builder(PrePaid.this)
                        .setTitle("Error")
                        .setMessage("Insufficient Balance")
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
