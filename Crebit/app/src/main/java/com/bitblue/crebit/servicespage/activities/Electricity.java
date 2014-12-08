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

import com.bitblue.apinames.API;
import com.bitblue.crebit.R;
import com.bitblue.jsonparse.JSONParser;
import com.bitblue.nullcheck.Check;
import com.bitblue.requestparam.ElectricityParams;
import com.bitblue.response.ElectricityResponse;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Electricity extends ActionBarActivity implements View.OnClickListener {
    private TextView tvservice, tvoperator, accNo;
    private Button serviceId, Bu;
    private EditText custAccNo;

    private String UserId, Key;
    private int ServiceId;
    private String CusAcc;
    private int BU;
    private String CyDiv;
    private double Amount;
    private String CusMob, DueDate;

    private String Status, AvaiBal;

    private String[] service;
    private String[] bu;
    private ArrayAdapter<String> serviceadapter;
    private ArrayAdapter<String> buadapter;
    private JSONParser jsonParser;
    private JSONObject jsonResponse;
    private ElectricityResponse electricityResponse;
    private ElectricityParams electricityParams;
    private List<NameValuePair> nameValuePairs;

    private SharedPreferences prefs;
    private final static String MY_PREFS = "mySharedPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electricity);
        service = getResources().getStringArray(R.array.service);
        bu = getResources().getStringArray(R.array.bu);
        initViews();
    }

    private void initViews() {
        tvservice = (TextView) findViewById(R.id.tv_elec_service);
        tvoperator = (TextView) findViewById(R.id.tv_elec_operator);
        accNo = (TextView) findViewById(R.id.tv_elec_cust_acc_no);

        serviceId = (Button) findViewById(R.id.b_elec_service);
        Bu = (Button) findViewById(R.id.b_elec_BU);

        custAccNo = (EditText) findViewById(R.id.et_elec_cust_acc_no);

        serviceId.setOnClickListener(this);
        Bu.setOnClickListener(this);
        serviceadapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, service);
        buadapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, bu);

        prefs = getSharedPreferences(MY_PREFS, MODE_PRIVATE);
        UserId = prefs.getString("userId", "");
        Key = prefs.getString("userKey", "");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.b_elec_service:
                new AlertDialog.Builder(this)
                        .setTitle("Select Service")
                        .setAdapter(serviceadapter, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int position) {
                                ServiceId = (position + 1);
                                serviceId.setText(service[position]);
                                dialog.dismiss();
                            }
                        }).create().show();
                break;
            case R.id.b_elec_BU:
                new AlertDialog.Builder(this)
                        .setTitle("Select Operator")
                        .setAdapter(buadapter, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int position) {
                                BU = (position + 1);
                                Bu.setText(bu[position]);
                                //code to extract BU number
                                dialog.dismiss();
                            }
                        }).create().show();
                break;
            case R.id.b_elec_getDetails:
                CusAcc = custAccNo.getText().toString();
                if (serviceId.getText().equals("--Select--")) {
                    tvservice.setTextColor(getResources().getColor(R.color.red));
                }
                if (Bu.getText().equals("--Select--")) {
                    tvoperator.setTextColor(getResources().getColor(R.color.red));
                }
                if (Check.ifNull(CusAcc)) {
                    custAccNo.setText("");
                    custAccNo.setHint(" Enter correct number");
                    custAccNo.setHintTextColor(getResources().getColor(R.color.red));
                    break;
                }

                new retrievedata().execute();
                break;

        }

    }

    private class retrievedata extends AsyncTask<String, String, String> {
        ProgressDialog dialog = new ProgressDialog(Electricity.this);

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
            electricityParams = new ElectricityParams(UserId, Key, ServiceId, CusAcc, BU, CyDiv, Amount, CusMob, DueDate);
            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("UserId", UserId));
            nameValuePairs.add(new BasicNameValuePair("Key", Key));
            nameValuePairs.add(new BasicNameValuePair("ServiceId", String.valueOf(ServiceId)));
            nameValuePairs.add(new BasicNameValuePair("CusAcc", CusAcc));
            nameValuePairs.add(new BasicNameValuePair("BU", String.valueOf(BU)));
            nameValuePairs.add(new BasicNameValuePair("CyDiv", CyDiv));
            nameValuePairs.add(new BasicNameValuePair("Amount", String.valueOf(Amount)));
            nameValuePairs.add(new BasicNameValuePair("CusMob", CusMob));
            nameValuePairs.add(new BasicNameValuePair("DueDate", DueDate));
            jsonResponse = jsonParser.makeHttpPostRequestforJsonObject(API.DASHBOARD_ELECTRICITY, nameValuePairs);
            try {
                electricityResponse = new ElectricityResponse(jsonResponse.getString("Status"),
                        jsonResponse.getString("AvaiBal"));

                Status = electricityResponse.getStatus();
                AvaiBal = electricityResponse.getAvaiBal();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return Status;
        }

        @Override
        protected void onPostExecute(String Status) {
            if (Status.equals("")) {
            }
        }
    }
}
