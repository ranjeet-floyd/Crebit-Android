package com.bitblue.crebit.servicespage.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bitblue.apinames.API;
import com.bitblue.crebit.R;
import com.bitblue.jsonparse.JSONParser;
import com.bitblue.nullcheck.Check;
import com.bitblue.requestparam.InsuranceParams;
import com.bitblue.response.InsuranceResponse;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Insurance extends ActionBarActivity implements View.OnClickListener {
    private TextView operator, number, amount;
    private EditText et_number, et_amount;
    private Button recharge, operatorType;
    private String[] items;
    private ArrayAdapter<String> adapter;
    private JSONParser jsonParser;
    private JSONObject jsonResponse;
    private InsuranceResponse insuranceResponse;
    private InsuranceParams insuranceParams;
    private List<NameValuePair> nameValuePairs;
    private String TransId;
    private String Message;
    private int StatusCode;
    private String AvailableBalance;
    private String UserId;
    private String Key;
    private String TransactionType;
    private String OperatorId;
    private String Number;
    private double Amount;
    private String Source;
    private String InsuranceDob;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insurance);
        items = getResources().getStringArray(R.array.insurance_operator);
        initViews();
    }

    private void initViews() {
        operator = (TextView) findViewById(R.id.tv_ins_operator);
        number = (TextView) findViewById(R.id.tv_ins_number);
        amount = (TextView) findViewById(R.id.tv_ins_amount);

        et_number = (EditText) findViewById(R.id.et_ins_number);
        et_amount = (EditText) findViewById(R.id.et_ins_amount);

        recharge = (Button) findViewById(R.id.b_ins_recharge);
        operatorType = (Button) findViewById(R.id.b_ins_operator);
        recharge.setOnClickListener(this);
        operatorType.setOnClickListener(this);
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, items);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.b_ins_operator:
                new AlertDialog.Builder(this)
                        .setTitle("Select Operator")
                        .setAdapter(adapter, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int position) {
                                OperatorId = String.valueOf(position + 1);
                                operatorType.setText(items[position]);
                                dialog.dismiss();
                            }
                        }).create().show();
                break;
            case R.id.b_ins_recharge:
                Number = et_number.getText().toString();
                try {
                    Amount = Double.parseDouble(et_amount.getText().toString());
                } catch (Exception e) {
                    Amount = 0;
                }
                if (operatorType.getText().equals("--Select--")) {
                    operator.setTextColor(getResources().getColor(R.color.red));
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

                new retrievedata().execute();
                break;

        }
    }

    private class retrievedata extends AsyncTask<String, String, String> {
        ProgressDialog dialog = new ProgressDialog(Insurance.this);

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
            insuranceParams = new InsuranceParams(UserId, Key, TransactionType, OperatorId, Number, Amount, Source, InsuranceDob);
            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("UserId", UserId));
            nameValuePairs.add(new BasicNameValuePair("Key", Key));
            nameValuePairs.add(new BasicNameValuePair("TransactionType", TransactionType));
            nameValuePairs.add(new BasicNameValuePair("OperatorId", OperatorId));
            nameValuePairs.add(new BasicNameValuePair("Number", Number));
            nameValuePairs.add(new BasicNameValuePair("Amount", String.valueOf(Amount)));
            nameValuePairs.add(new BasicNameValuePair("InsuranceDob", InsuranceDob));

            jsonResponse = jsonParser.makeHttpPostRequestforJsonObject(API.DASHBOARD_SERVICE, nameValuePairs);
            try {
                insuranceResponse = new InsuranceResponse(jsonResponse.getString("TransId"),
                        jsonResponse.getString("Message"),
                        jsonResponse.getInt("StatusCode"),
                        jsonResponse.getString("AvailableBalance"));

                TransId = insuranceResponse.getTransId();
                Message = insuranceResponse.getMessage();
                StatusCode = insuranceResponse.getStatusCode();
                AvailableBalance = insuranceResponse.getAvailableBalance();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return String.valueOf(StatusCode);
        }

        @Override
        protected void onPostExecute(String StatusCode) {
            if (StatusCode.equals("")) {
            }
        }
    }

}
