package com.bitblue.crebit.servicespage.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bitblue.IDs.gasBill;
import com.bitblue.apinames.API;
import com.bitblue.crebit.R;
import com.bitblue.jsonparse.JSONParser;
import com.bitblue.nullcheck.Check;
import com.bitblue.requestparam.GasBillParams;
import com.bitblue.response.GasBillResponse;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GasBill extends ActionBarActivity implements View.OnClickListener {
    private TextView tvoperator, tvnumber, tvamount;
    private EditText et_number, et_amount;
    private Button recharge, operatorType;
    private TextView transId, message, statcode, availablebal;

    private String UserId, Key, OperatorId, Number;
    private double Amount;
    private static final String SOURCE = "2";
    private String TransId, Message;
    private int StatusCode;
    private String AvailableBalance;

    private ArrayAdapter<String> adapter;
    private String[] items;
    private JSONParser jsonParser;
    private JSONObject jsonResponse;
    private GasBillResponse gasBillResponse;
    private GasBillParams gasBillParams;
    private List<NameValuePair> nameValuePairs;

    private SharedPreferences prefs;
    private final static String MY_PREFS = "mySharedPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas_bill);
        items = getResources().getStringArray(R.array.gasbill_operator);
        initViews();
    }

    private void initViews() {
        tvoperator = (TextView) findViewById(R.id.tv_gas_operator);
        tvnumber = (TextView) findViewById(R.id.et_gas_number);
        tvamount = (TextView) findViewById(R.id.tv_gas_amount);
        transId = (TextView) findViewById(R.id.tv_gas_TransId);
        message = (TextView) findViewById(R.id.tv_gas_Message);
        statcode = (TextView) findViewById(R.id.tv_gas_StatusCode);
        availablebal = (TextView) findViewById(R.id.tv_gas_AvailableBalance);

        et_number = (EditText) findViewById(R.id.et_gas_number);
        et_amount = (EditText) findViewById(R.id.et_gas_amount);

        recharge = (Button) findViewById(R.id.b_gas_recharge);
        operatorType = (Button) findViewById(R.id.b_gas_operator);
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
            case R.id.b_gas_operator:
                new AlertDialog.Builder(this)
                        .setTitle("Select Operator")
                        .setAdapter(adapter, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int position) {
                                OperatorId = gasBill.getGasBillOperatorId(position);
                                operatorType.setText(items[position]);
                                dialog.dismiss();
                            }
                        }).create().show();
                break;
            case R.id.b_gas_recharge:
                Number = et_number.getText().toString();
                try {
                    Amount = Double.parseDouble(et_amount.getText().toString());
                } catch (Exception e) {
                    Amount = 0;
                }
                if (operatorType.getText().equals("--Select--")) {
                    tvoperator.setTextColor(getResources().getColor(R.color.red));
                }
                if (Check.ifEmpty(Amount)) {
                    et_amount.setHintTextColor(getResources().getColor(R.color.red));
                }
                if (Check.ifNumberInCorrect(Number)) {
                    et_number.setText("");
                    et_number.setHint(" Enter correct number");
                    et_number.setHintTextColor(getResources().getColor(R.color.red));
                    break;
                }

                new retrievegasbilldata().execute();
                break;
        }
    }

    private class retrievegasbilldata extends AsyncTask<String, String, String> {
        ProgressDialog dialog = new ProgressDialog(GasBill.this);

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
            gasBillParams = new GasBillParams(UserId, Key, OperatorId, Number, Amount, SOURCE);
            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("UserId", UserId));
            nameValuePairs.add(new BasicNameValuePair("Key", Key));
            nameValuePairs.add(new BasicNameValuePair("OperatorId", OperatorId));
            nameValuePairs.add(new BasicNameValuePair("Number", Number));
            nameValuePairs.add(new BasicNameValuePair("Amount", String.valueOf(Amount)));
            nameValuePairs.add(new BasicNameValuePair("Source", SOURCE));
            jsonResponse = jsonParser.makeHttpPostRequestforJsonObject(API.DASHBOARD_SERVICE, nameValuePairs);
            try {
                gasBillResponse = new GasBillResponse(jsonResponse.getString("transId"),
                        jsonResponse.getString("message"),
                        jsonResponse.getInt("statusCode"),
                        jsonResponse.getString("availableBalance"));

                TransId = gasBillResponse.getTransId();
                Message = gasBillResponse.getMessage();
                StatusCode = gasBillResponse.getStatusCode();
                AvailableBalance = gasBillResponse.getAvailableBalance();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return String.valueOf(StatusCode);
        }

        @Override
        protected void onPostExecute(String StatusCode) {
            if (StatusCode.equals("0") || StatusCode.equals("-1")) {
                new AlertDialog.Builder(GasBill.this)
                        .setTitle("Error")
                        .setMessage("Request Not Completed.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create().show();
            } else if (StatusCode.equals("1")) {
                new AlertDialog.Builder(GasBill.this)
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
                new AlertDialog.Builder(GasBill.this)
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
