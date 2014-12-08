package com.bitblue.crebit.servicespage.activities;

import android.app.ProgressDialog;
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
    private TextView number, amount;
    private EditText et_number, et_amount;
    private Button transfer;

    private String UserId, Key, MobileTo, Amount;

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
        initViews();
    }

    private void initViews() {
        number = (TextView) findViewById(R.id.tv_ft_number);
        amount = (TextView) findViewById(R.id.tv_ft_amount);

        et_number = (EditText) findViewById(R.id.et_ft_number);
        et_amount = (EditText) findViewById(R.id.et_ft_amount);

        transfer = (Button) findViewById(R.id.b_ft_recharge);
        transfer.setOnClickListener(this);

        prefs = getSharedPreferences(MY_PREFS, MODE_PRIVATE);
        UserId = prefs.getString("userId", "");
        Key = prefs.getString("userKey", "");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.b_ft_recharge:
                MobileTo = et_number.getText().toString();
                Amount = et_amount.getText().toString();
                if (Check.ifNull(Amount)) {
                    et_amount.setHintTextColor(getResources().getColor(R.color.red));
                }
                if (Check.ifNumberInCorrect(MobileTo)) {
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
            nameValuePairs.add(new BasicNameValuePair("UserId", UserId));
            nameValuePairs.add(new BasicNameValuePair("Key", Key));
            nameValuePairs.add(new BasicNameValuePair("MobileTo", MobileTo));
            nameValuePairs.add(new BasicNameValuePair("Amount", Amount));
            jsonArray = jsonParser.makeHttpPostRequest(API.DASHBOARD_TRANSFER, nameValuePairs);
            try {
                jsonResponse = jsonArray.getJSONObject(0);
                fundTransferResponse = new FundTransferResponse(jsonResponse.getString("Status"),
                        jsonResponse.getString("AvailableBalance"));

                Status = fundTransferResponse.getStatus();
                AvailableBalance = fundTransferResponse.getAvailableBalance();
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
