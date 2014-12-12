package com.bitblue.crebit.servicespage.fragments;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bitblue.apinames.API;
import com.bitblue.crebit.R;
import com.bitblue.jsonparse.JSONParser;
import com.bitblue.nullcheck.Check;
import com.bitblue.requestparam.BankAccParams;
import com.bitblue.response.BankAccResponse;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BankAccPay extends Fragment implements View.OnClickListener {
    private TextView tvname, tvaccno, tvifsc, tvmobile, tvamount, tvavailablebalance, tvRefID;
    private EditText etname, etaccno, etifsc, etmobile, etamount;
    private Button bsubmit;
    private String UserId, Key, Mobile, Name, Account, IFSC;
    private double Amount;
    private String Status, AvailableBalance, RefId;

    private ArrayAdapter<String> adapter;
    private String[] items;
    private JSONParser jsonParser;
    private JSONObject jsonResponse;
    private BankAccParams bankAccParams;
    private BankAccResponse bankAccResponse;
    private List<NameValuePair> nameValuePairs;

    private SharedPreferences prefs;
    private final static String MY_PREFS = "mySharedPrefs";

    public BankAccPay() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bank_acc_pay, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        tvname = (TextView) view.findViewById(R.id.tv_bap_name);
        tvaccno = (TextView) view.findViewById(R.id.tv_bap_accNum);
        tvmobile = (TextView) view.findViewById(R.id.tv_bap_mobNum);
        tvifsc = (TextView) view.findViewById(R.id.tv_bap_ifsc);
        tvamount = (TextView) view.findViewById(R.id.tv_bap_amount);
        tvavailablebalance = (TextView) view.findViewById(R.id.tv_bap_AvailableBalance);
        tvRefID = (TextView) view.findViewById(R.id.tv_bap_RefId);
        etname = (EditText) view.findViewById(R.id.et_bap_name);
        etaccno = (EditText) view.findViewById(R.id.et_bap_accNum);
        etifsc = (EditText) view.findViewById(R.id.et_bap_ifsc);
        etmobile = (EditText) view.findViewById(R.id.et_bap_mobNum);
        etamount = (EditText) view.findViewById(R.id.et_bap_amount);

        bsubmit = (Button) view.findViewById(R.id.b_bap_submit);
        bsubmit.setOnClickListener(this);

        prefs = getActivity().getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
        UserId = prefs.getString("userId", "");
        Key = prefs.getString("userKey", "");

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.b_bap_submit:
                Name = etname.getText().toString();
                Account = etaccno.getText().toString();
                IFSC = etifsc.getText().toString();
                Mobile = etmobile.getText().toString();
                Amount = Double.parseDouble(etamount.getText().toString());
                if (Check.ifNull(Name)) {
                    etname.setText("");
                    etname.setHint(" Enter Name");
                    etname.setHintTextColor(getResources().getColor(R.color.red));
                    break;
                }
                if (Check.ifNull(Account)) {
                    etname.setText("");
                    etname.setHint(" Enter Account Number");
                    etname.setHintTextColor(getResources().getColor(R.color.red));
                    break;
                }
                if (Check.ifNull(IFSC)) {
                    etname.setText("");
                    etname.setHint(" Enter IFSC code");
                    etname.setHintTextColor(getResources().getColor(R.color.red));
                    break;
                }
                if (Check.ifNull(Mobile)) {
                    etname.setText("");
                    etname.setHint(" Enter Mobile Number");
                    etname.setHintTextColor(getResources().getColor(R.color.red));
                    break;
                }
                if (Check.ifEmpty(Amount)) {
                    etname.setText("");
                    etname.setHint(" Enter Amount");
                    etname.setHintTextColor(getResources().getColor(R.color.red));
                    break;
                }

                new retrievedata().execute();
                break;

        }
    }

    private class retrievedata extends AsyncTask<String, String, String> {
        ProgressDialog dialog = new ProgressDialog(getActivity());

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
            bankAccParams = new BankAccParams(UserId, Key, Mobile, Name, Account, IFSC, Amount);
            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("UserId", UserId));
            nameValuePairs.add(new BasicNameValuePair("Key", Key));
            nameValuePairs.add(new BasicNameValuePair("Mobile", Mobile));
            nameValuePairs.add(new BasicNameValuePair("Name", Name));
            nameValuePairs.add(new BasicNameValuePair("IFSC", IFSC));
            nameValuePairs.add(new BasicNameValuePair("Account", Account));
            nameValuePairs.add(new BasicNameValuePair("Amount", String.valueOf(Amount)));
            jsonResponse = jsonParser.makeHttpPostRequestforJsonObject(API.DHS_BANK_ACC_PAY, nameValuePairs);
            try {
                bankAccResponse = new BankAccResponse(jsonResponse.getString("status"),
                        jsonResponse.getString("availableBalance"),
                        jsonResponse.getString("refId"));

                AvailableBalance = bankAccResponse.getAvailableBalance();
                Status = bankAccResponse.getStatus();
                RefId = bankAccResponse.getRefId();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return Status;
        }

        @Override
        protected void onPostExecute(String StatusCode) {
            if (StatusCode.equals("0") || StatusCode.equals("-1")) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Error")
                        .setMessage("Request Not Completed.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create().show();
            } else if (StatusCode.equals("1")) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Success")
                        .setMessage("Request Completed.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create().show();
                tvavailablebalance.setText("AvailableBalance: " + AvailableBalance);
                tvRefID.setText("RefId: " + RefId);
            } else if (StatusCode.equals("2")) {
                new AlertDialog.Builder(getActivity())
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
