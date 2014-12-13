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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bitblue.IDs.BU;
import com.bitblue.apinames.API;
import com.bitblue.crebit.R;
import com.bitblue.jsonparse.JSONParser;
import com.bitblue.nullcheck.Check;
import com.bitblue.requestparam.MsebParams;
import com.bitblue.requestparam.MsebPayBillparams;
import com.bitblue.response.MsebPayBllResponse;
import com.bitblue.response.MsebResponse;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MSEB extends Activity implements View.OnClickListener {
    private String UserId, Key, Bu, DueDate, CusMob;
    private int ServiceId = 40, BillAmount, ConsumptionUnits;

    private TextView tvBu, tvcustAccNo, tvErrorDueDate, tverrorDate;
    private Button bBu, bGetDetails, bpaybill;
    private EditText etcustAccNo, etcusMobNo;
    private LinearLayout llerrorDueDate, lleleccustno;

    private ArrayAdapter<String> adapter;
    private String[] items;
    private String CusAcc;
    private JSONParser jsonParser;
    private JSONObject jsonResponse;
    private MsebParams msebParams;
    private MsebResponse msebResponse;
    private MsebPayBillparams msebPayBillparams;
    private MsebPayBllResponse msebPayBllResponse;
    private List<NameValuePair> nameValuePairs;
    private int Status, AvailBal;

    private SharedPreferences prefs;
    private final static String MY_PREFS = "mySharedPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        items = getResources().getStringArray(R.array.bu);
        setContentView(R.layout.activity_mseb);
        initViews();
    }

    private void initViews() {
        tvBu = (TextView) findViewById(R.id.tv_elec_mseb_buoperator);
        tvcustAccNo = (TextView) findViewById(R.id.tv_elec_mseb_cust_acc_no);
        tvErrorDueDate = (TextView) findViewById(R.id.tv_elec_mseb_errorDueDate);
        tverrorDate = (TextView) findViewById(R.id.tv_elec_mseb_error_date);

        bBu = (Button) findViewById(R.id.b_elec_mseb_BU);
        bGetDetails = (Button) findViewById(R.id.b_elec_mseb_getDetails);
        bpaybill = (Button) findViewById(R.id.b_elec_mseb_paybill);
        etcustAccNo = (EditText) findViewById(R.id.et_elec_mseb_cust_acc_no);
        etcusMobNo = (EditText) findViewById(R.id.et_elec_mseb_cust_mobno);
        llerrorDueDate = (LinearLayout) findViewById(R.id.ll_error_result);
        lleleccustno = (LinearLayout) findViewById(R.id.ll_elec_mseb_cust_mobno);
        bBu.setOnClickListener(this);
        bGetDetails.setOnClickListener(this);
        bpaybill.setOnClickListener(this);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        prefs = getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
        UserId = prefs.getString("userId", "");
        Key = prefs.getString("userKey", "");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.b_elec_mseb_BU:
                new AlertDialog.Builder(MSEB.this)
                        .setTitle("Select BU")
                        .setAdapter(adapter, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int position) {
                                bBu.setText(items[position]);
                                Bu = BU.getBuCode(items, position);
                                dialog.dismiss();
                            }
                        }).create().show();
                break;
            case R.id.b_elec_mseb_getDetails:
                CusAcc = etcustAccNo.getText().toString();
                if (bBu.getText().equals("--Select--")) {
                    tvBu.setTextColor(getResources().getColor(R.color.red));
                    break;
                }
                if (Check.ifAccountNumberIncorrect(CusAcc)) {
                    etcustAccNo.setText("");
                    etcustAccNo.setHint(" Enter correct number");
                    etcustAccNo.setHintTextColor(getResources().getColor(R.color.red));
                    break;

                }
                new retrieveDataforGetDetails().execute();
                break;
            case R.id.b_elec_mseb_paybill:
                CusMob = etcusMobNo.getText().toString();
                if (Check.ifNumberInCorrect(CusMob)) {
                    etcusMobNo.setText("");
                    etcusMobNo.setHint(" Enter correct number");
                    etcusMobNo.setHintTextColor(getResources().getColor(R.color.red));
                    break;
                }
                new retrieveDataforPayBill().execute();
                break;
        }
    }

    private class retrieveDataforGetDetails extends AsyncTask<String, String, String> {
        ProgressDialog dialog = new ProgressDialog(MSEB.this);

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Please Wait...");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            jsonParser = new JSONParser();
            msebParams = new MsebParams(UserId, Key, CusAcc, Bu, ServiceId);
            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("userId", UserId));
            nameValuePairs.add(new BasicNameValuePair("key", Key));
            nameValuePairs.add(new BasicNameValuePair("serviceId", String.valueOf(ServiceId)));
            nameValuePairs.add(new BasicNameValuePair("consumerNo", CusAcc));
            nameValuePairs.add(new BasicNameValuePair("buCode", Bu));
            jsonResponse = jsonParser.makeHttpPostRequestforJsonObject(API.DHS_GET_MSEB_CUS_DETAILS, nameValuePairs);
            try {
                msebResponse = new MsebResponse(jsonResponse.getInt("billAmount"),
                        jsonResponse.getString("dueDate"),
                        jsonResponse.getInt("consumptionUnits"));
                BillAmount = msebResponse.getBillAmount();
                DueDate = msebResponse.getDueDate();
                ConsumptionUnits = msebResponse.getConsumptionUnits();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return DueDate;
        }

        @Override
        protected void onPostExecute(String dueDate) {
            dialog.dismiss();
            llerrorDueDate.setVisibility(View.VISIBLE);
            if (Check.ifTodayLessThanDue(dueDate)) {
                llerrorDueDate.setBackgroundResource(R.drawable.rounded_green_layout);
                bpaybill.setVisibility(View.VISIBLE);
                tvErrorDueDate.setText("Due date not exceeded");
                tverrorDate.setText("Pay the bill now");
                lleleccustno.setVisibility(View.VISIBLE);

            } else {
                llerrorDueDate.setBackgroundResource(R.drawable.rounded_red_layout);
                tvErrorDueDate.setText("Due Date Exceeded");
                tverrorDate.setText("Your Due Date was on: " + dueDate);
            }
        }
    }

    private class retrieveDataforPayBill extends AsyncTask<String, String, String> {
        ProgressDialog dialog = new ProgressDialog(MSEB.this);

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Please Wait...");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            jsonParser = new JSONParser();
            msebPayBillparams = new MsebPayBillparams(BillAmount, Bu, CusAcc, CusMob, DueDate, Key, ServiceId, UserId);
            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("userId", UserId));
            nameValuePairs.add(new BasicNameValuePair("key", Key));
            nameValuePairs.add(new BasicNameValuePair("serviceId", String.valueOf(ServiceId)));
            nameValuePairs.add(new BasicNameValuePair("consumerNo", CusAcc));
            nameValuePairs.add(new BasicNameValuePair("buCode", Bu));
            jsonResponse = jsonParser.makeHttpPostRequestforJsonObject(API.DASHBOARD_ELECTRICITY, nameValuePairs);
            try {
                msebPayBllResponse = new MsebPayBllResponse(jsonResponse.getInt("avaiBal"), jsonResponse.getInt("status"));
                Status = msebPayBllResponse.getStatus();
                AvailBal = msebPayBllResponse.getAvaiBal();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return String.valueOf(Status);
        }

        @Override
        protected void onPostExecute(String status) {
            dialog.dismiss();
            if (status.equals("0") || status.equals("-1")) {
                new AlertDialog.Builder(MSEB.this)
                        .setTitle("Error")
                        .setMessage("Request Not Completed.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create().show();
            } else if (status.equals("1")) {
                new AlertDialog.Builder(MSEB.this)
                        .setTitle("Success")
                        .setMessage("Request Completed.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create().show();
            } else if (status.equals("2")) {
                new AlertDialog.Builder(MSEB.this)
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
