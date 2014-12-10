package com.bitblue.crebit.servicespage.fragments.transactionSummary;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.bitblue.IDs.status;
import com.bitblue.IDs.type;
import com.bitblue.apinames.API;
import com.bitblue.crebit.R;
import com.bitblue.crebit.servicespage.fragments.DatePickerFragment;
import com.bitblue.jsonparse.JSONParser;
import com.bitblue.requestparam.TranSumParams;
import com.bitblue.response.TranSumResponse;

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

public class TransSummary extends Fragment implements View.OnClickListener {
    private TextView tvfromto, tvstatus, tvtype, tvCbalance, tvprofit, tvamount, tvsource, tvtransdate, tvstat, tvopname;
    private Button from_Date, to_Date, bstatus, btype, search, mobsearch;
    private EditText mobNum;

    private String fromDate, toDate, stat, typ, mobileNumber;
    private int cur;
    private static final int FROM_DATE = 1;
    private static final int TO_DATE = 2;
    private String UserId, Key, FromDate, ToDate;
    private int StatusId, TypeId;
    private String CBalance, profit, Amount, Source, TDate, Status, OperaterName;

    private String[] statlist;
    private ArrayAdapter<String> statusAdapter;
    private String[] typelist;
    private ArrayAdapter<String> typeAdapter;
    private List<NameValuePair> nameValuePairs;
    private JSONParser jsonParser;
    private JSONObject jsonResponse;
    private JSONArray jsonArray, TranSumResults;
    private TranSumParams tranSumParams;
    private TranSumResponse tranSumResponse;

    private SharedPreferences prefs;
    private final static String MY_PREFS = "mySharedPrefs";
    private double TotalAmount, TotalProfit;

    public TransSummary() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trans_summary, container,
                false);
        statlist = getResources().getStringArray(R.array.status);
        typelist = getResources().getStringArray(R.array.type);

        initViews(view);
        return view;
    }

    public void initViews(View view) {

        tvfromto = (TextView) view.findViewById(R.id.tv_ts_fromto);
        tvstatus = (TextView) view.findViewById(R.id.tv_ts_status);
        tvtype = (TextView) view.findViewById(R.id.tv_ts_type);
        from_Date = (Button) view.findViewById(R.id.b_ts_from);
        to_Date = (Button) view.findViewById(R.id.b_ts_to);
        bstatus = (Button) view.findViewById(R.id.b_ts_status);
        btype = (Button) view.findViewById(R.id.b_ts_type);
        search = (Button) view.findViewById(R.id.b_ts_search);
        mobsearch = (Button) view.findViewById(R.id.b_ts_srch_mobnum);
        mobNum = (EditText) view.findViewById(R.id.et_ts_mobnum);

        from_Date.setOnClickListener(this);
        to_Date.setOnClickListener(this);
        bstatus.setOnClickListener(this);
        btype.setOnClickListener(this);

        search.setOnClickListener(this);
        mobsearch.setOnClickListener(this);
        statusAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, statlist);
        typeAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, typelist);

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
            Date from = null;
            Date to = null;
            if (cur == FROM_DATE) {
                from_Date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                fromDate = from_Date.getText().toString();

            }
            if (cur == TO_DATE) {
                to_Date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                toDate = to_Date.getText().toString();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-mm-yyyy");
                try {
                    from = sdf.parse(from_Date.getText().toString());
                    to = sdf.parse(toDate);
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
            case R.id.b_ts_from:
                cur = FROM_DATE;
                showDatePicker();
                break;
            case R.id.b_ts_to:
                cur = TO_DATE;
                showDatePicker();
                break;

            case R.id.b_ts_status:
                new AlertDialog.Builder(getActivity())
                        .setTitle("Select Status")
                        .setAdapter(statusAdapter, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int position) {
                                bstatus.setText(statlist[position]);
                                StatusId = status.getStatusId(position);
                                stat = bstatus.getText().toString();
                                dialog.dismiss();
                            }
                        }).create().show();
                break;
            case R.id.b_ts_type:
                new AlertDialog.Builder(getActivity())
                        .setTitle("Select Type")
                        .setAdapter(typeAdapter, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int position) {
                                btype.setText(typelist[position]);
                                TypeId = type.getTypeId(position);
                                typ = btype.getText().toString();
                                dialog.dismiss();
                            }
                        }).create().show();
                break;
            case R.id.b_ts_search:
                stat = bstatus.getText().toString();
                typ = btype.getText().toString();
                FromDate = from_Date.getText().toString();
                ToDate = to_Date.getText().toString();

                new retrieveTransactionData().execute();
            case R.id.b_ts_srch_mobnum:
                mobileNumber = mobNum.getText().toString();
                break;
        }

    }

    private class retrieveTransactionData extends AsyncTask<String, String, String> {
        ProgressDialog dialog = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Please wait ...");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            tranSumParams = new TranSumParams(UserId, Key, FromDate, ToDate, StatusId, TypeId);
            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("UserId", UserId));
            nameValuePairs.add(new BasicNameValuePair("Key", Key));
            nameValuePairs.add(new BasicNameValuePair("FromDate", FromDate));
            nameValuePairs.add(new BasicNameValuePair("ToDate", ToDate));
            nameValuePairs.add(new BasicNameValuePair("StatusId", String.valueOf(StatusId)));
            nameValuePairs.add(new BasicNameValuePair("TypeId", String.valueOf(TypeId)));
            jsonArray = jsonParser.makeHttpPostRequest(API.DASHBOARD_TRANSACTION_DETAILS, nameValuePairs);
            try {
                jsonResponse = jsonArray.getJSONObject(0);
                tranSumResponse = new TranSumResponse(jsonResponse.getDouble("totalAmount"),
                        jsonResponse.getDouble("totalProfit"), jsonResponse.getJSONArray("dL_TransactionReturns"));

                TotalAmount = tranSumResponse.getTotalAmount();
                TotalProfit = tranSumResponse.getTotalProfit();
                TranSumResults = tranSumResponse.getTranSumResults();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return Status;
        }


        @Override
        protected void onPostExecute(String status) {
            dialog.dismiss();
            if (status.equals("0")) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Error")
                        .setMessage("Request Not Completed.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create().show();
            } else if (status.equals("1")) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Success")
                        .setMessage("Request Completed.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create().show();
                tvCbalance.setText("Available Cbalance: " + CBalance);
                tvprofit.setText("Profit: " + profit);
                tvamount.setText("Amount: " + Amount);
                tvsource.setText("Source: " + Source);
                tvtransdate.setText("TDate: " + TDate);
                tvstat.setText("Status: " + Status);
                tvopname.setText("Operator Name: " + OperaterName);

            } else if (status.equals("2")) {
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

