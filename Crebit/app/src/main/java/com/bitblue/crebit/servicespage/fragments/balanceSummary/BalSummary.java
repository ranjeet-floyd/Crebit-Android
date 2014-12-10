package com.bitblue.crebit.servicespage.fragments.balanceSummary;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.bitblue.apinames.API;
import com.bitblue.crebit.R;
import com.bitblue.crebit.servicespage.fragments.DatePickerFragment;
import com.bitblue.jsonparse.JSONParser;
import com.bitblue.requestparam.BalSumParams;
import com.bitblue.response.BalSumResponse;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BalSummary extends Fragment implements View.OnClickListener {
    Button from_Date, to_Date, btype, bsearch;
    EditText ettype;
    private String UserId, Key;
    private Double TotalBalanceGiven;

    private String fromDate, toDate, value;
    private int typeId, cur = 0;
    private static final int FROM_DATE = 1;
    private static final int TO_DATE = 2;
    private String[] items;
    private ArrayAdapter<String> adapter;
    private JSONArray balanceUseArr;
    private JSONParser jsonParser;
    private JSONObject jsonResponse, balanceUseArrobject;
    private BalSumParams balSumParams;
    private BalSumResponse balSumResponse;
    private List<NameValuePair> nameValuePairs;
    private SharedPreferences prefs;
    private final static String MY_PREFS = "mySharedPrefs";

    public BalSummary() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bal_summary, container, false);
        items = getResources().getStringArray(R.array.bs_status);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        from_Date = (Button) view.findViewById(R.id.b_balsum_from);
        to_Date = (Button) view.findViewById(R.id.b_balsum_to);
        btype = (Button) view.findViewById(R.id.b_balsum_type);
        ettype = (EditText) view.findViewById(R.id.et_balsum_type);
        bsearch = (Button) view.findViewById(R.id.b_balsum_search);
        from_Date.setOnClickListener(this);
        to_Date.setOnClickListener(this);
        btype.setOnClickListener(this);
        bsearch.setOnClickListener(this);
        adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, items);

        prefs = this.getActivity().getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
        UserId = prefs.getString("userId", "");
        Key = prefs.getString("userKey", "");
    }

    private void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        /**
         * Set Up Current Date Into dialog
         */
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        date.setCallBack(ondate);
        date.show(getFragmentManager(), "Date Picker");
    }

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            Date from = new Date();
            Date to = new Date();
            if (cur == FROM_DATE) {
                from_Date.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                fromDate = from_Date.getText().toString();

            }
            if (cur == TO_DATE) {
                to_Date.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                toDate = to_Date.getText().toString();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
                try {
                    from = sdf.parse(from_Date.getText().toString());
                    to = sdf.parse(to_Date.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (to.before(from)) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Error")
                            .setMessage("Enter Proper Range")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).create().show();
                }
            }
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.b_balsum_from:
                cur = FROM_DATE;
                showDatePicker();
                from_Date.setText("");
                break;
            case R.id.b_balsum_to:
                cur = TO_DATE;
                showDatePicker();
                to_Date.setText("");
                break;
            case R.id.b_balsum_type:
                new AlertDialog.Builder(getActivity())
                        .setTitle("Select Operator")
                        .setAdapter(adapter, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int position) {
                                btype.setText(items[position]);
                                switch (position) {
                                    case 0:
                                        ettype.setHint(" Enter Sender Number");
                                        break;
                                    case 1:
                                        ettype.setHint(" Enter Transaction ID");
                                        break;
                                }
                                dialog.dismiss();
                            }
                        }).create().show();
                break;
            case R.id.b_balsum_search:
                Bundle dates = new Bundle();
                dates.putString("fromDate", fromDate);
                dates.putString("toDate", toDate);

                new retrieveData().execute();

                BalSumResultFragment balSumResultFragment = new BalSumResultFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                balSumResultFragment.setArguments(dates);
                ft.replace(R.id.balsumframe, balSumResultFragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.addToBackStack(null);
                ft.commit();
                break;
        }

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
            balSumParams = new BalSumParams(UserId, Key, fromDate, toDate);
            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("UserId", UserId));
            nameValuePairs.add(new BasicNameValuePair("Key", Key));
            nameValuePairs.add(new BasicNameValuePair("FromDate", fromDate));
            nameValuePairs.add(new BasicNameValuePair("ToDate", toDate));
            jsonResponse = jsonParser.makeHttpPostRequestforJsonObject(API.DASHBOARD_BALANCE_USE, nameValuePairs);
            try {
                balSumResponse = new BalSumResponse(jsonResponse.getDouble("totalBalanceGiven"), jsonResponse.getDouble("totalBalanceTaken"),
                        jsonResponse.getJSONArray("balanceUse"));
                TotalBalanceGiven = balSumResponse.getTotalBalanceGiven();
                balanceUseArr = balSumResponse.getBalUse();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            dialog.dismiss();
        }
    }
}
