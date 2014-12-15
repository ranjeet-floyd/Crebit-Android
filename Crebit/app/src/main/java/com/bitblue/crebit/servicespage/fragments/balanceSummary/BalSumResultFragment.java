package com.bitblue.crebit.servicespage.fragments.balanceSummary;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.bitblue.apinames.API;
import com.bitblue.crebit.R;
import com.bitblue.crebit.servicespage.service;
import com.bitblue.jsonparse.JSONParser;
import com.bitblue.requestparam.BalSumParams;
import com.bitblue.response.BalSumResponse;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BalSumResultFragment extends Fragment {
    private TextView tvtotalbalgiven, tvbalgiven, tvbaltaken;

    private ListView resultList;
    private Double TotalBalanceGiven, TotalBalanceTaken;

    private static final String SOURCE = "2";
    private ArrayList<BalSumResult> balSumResultList = new ArrayList<BalSumResult>();
    private String UserId, Key, fromDate, toDate, TypeId, Value;
    private SharedPreferences prefs;
    private final static String MY_PREFS = "mySharedPrefs";
    private service Service;
    private JSONParser jsonParser;
    private JSONObject jsonResponse, balanceUseArrobject;
    private BalSumParams balSumParams;
    private BalSumResponse balSumResponse;
    private BalSumResult balSumResult;
    private ArrayList<NameValuePair> nameValuePairs;
    private JSONArray balanceUseArr;

    public BalSumResultFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bal_sum_result, container, false);
        prefs = getActivity().getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
        fromDate = getArguments().getString("fromDate");
        toDate = getArguments().getString("toDate");
        UserId = prefs.getString("userId", "");
        Key = prefs.getString("userKey", "");
        TypeId = prefs.getString("TypeId", "");
        Value = prefs.getString("Value", "");
        initViews(view);
        resultList = (ListView) view.findViewById(R.id.lv_balsum_result);
        new retrieveData().execute();
        return view;
    }

    private void initViews(View view) {
        tvbalgiven = (TextView) view.findViewById(R.id.tvbalgiven);
        tvbaltaken = (TextView) view.findViewById(R.id.tvbaltaken);

    }

    private class retrieveData extends AsyncTask<String, String, String> {
        ProgressDialog dialog = new ProgressDialog(getActivity());

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
            balSumParams = new BalSumParams(UserId, Key, fromDate, toDate, TypeId, Value);
            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("UserId", UserId));
            nameValuePairs.add(new BasicNameValuePair("Key", Key));
            nameValuePairs.add(new BasicNameValuePair("FromDate", fromDate));
            nameValuePairs.add(new BasicNameValuePair("ToDate", toDate));
            nameValuePairs.add(new BasicNameValuePair("TypeId", TypeId));
            nameValuePairs.add(new BasicNameValuePair("Value", Value));

            jsonResponse = jsonParser.makeHttpPostRequestforJsonObject(API.DASHBOARD_BALANCE_USE, nameValuePairs);
            try {
                balSumResponse = new BalSumResponse(jsonResponse.getDouble("totalBalanceGiven"),
                        jsonResponse.getDouble("totalBalanceTaken"),
                        jsonResponse.getJSONArray("balanceUse"));
                TotalBalanceGiven = balSumResponse.getTotalBalanceGiven();
                TotalBalanceTaken = balSumResponse.getTotalBalanceTaken();
                balanceUseArr = balSumResponse.getBalUse();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            dialog.dismiss();
            tvbalgiven.setText(String.valueOf(TotalBalanceGiven));
            tvbaltaken.setText(String.valueOf(TotalBalanceTaken));
            for (int i = 0; i < balanceUseArr.length(); i++) {
                try {
                    balanceUseArrobject = (JSONObject) balanceUseArr.get(i);
                    balSumResult = new BalSumResult();
                    balSumResult.setCount(i+1);
                    balSumResult.setName(balanceUseArrobject.getString("name"));
                    balSumResult.setAmount(balanceUseArrobject.getString("amount"));
                    balSumResult.setContact(balanceUseArrobject.getString("contact"));
                    balSumResult.setDate(balanceUseArrobject.getString("date"));
                    balSumResult.setTransactionId(balanceUseArrobject.getString("transactionId"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                balSumResultList.add(balSumResult);
            }
            resultList.setAdapter(new BalSumCustomAdapter(getActivity(), balSumResultList));


        }
    }
}

