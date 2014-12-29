package in.crebit.app.WebView.crebit.servicespage.fragments.balanceSummary;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import in.crebit.app.WebView.Applicaton.GlobalVariable;
import in.crebit.app.WebView.IDs.balsumtype;
import in.crebit.app.WebView.R;
import in.crebit.app.WebView.crebit.servicespage.fragments.DatePickerFragment;
import in.crebit.app.WebView.crebit.servicespage.service;
import in.crebit.app.WebView.jsonparse.JSONParser;
import in.crebit.app.WebView.requestparam.BalSumParams;
import in.crebit.app.WebView.response.BalSumResponse;

public class BalSummary extends Fragment implements View.OnClickListener {
    TextView balSum;
    Button from_Date, to_Date, btype, bsearch, todaybalsearch;
    EditText ettype;
    private String UserId, Key, TypeId, Value;
    private Double TotalBalanceGiven;
    Date from, to;
    private String fromDate, toDate;
    private int cur = 0;
    private static final int FROM_DATE = 1;
    private static final int TO_DATE = 2;
    private String[] items;

    private ArrayList<BalSumResult> balSumResultList;
    private ArrayAdapter<String> adapter;
    private JSONArray balanceUse;
    private JSONParser jsonParser;
    private JSONObject jsonResponse, balanceUseArrobject;
    private BalSumParams balSumParams;
    private BalSumResponse balSumResponse;
    private BalSumResult balSumResult;
    private ArrayList<NameValuePair> nameValuePairs;
    private JSONArray balanceUseArr;
    private service Service;
    private SharedPreferences prefs;
    private final static String MY_PREFS = "mySharedPrefs";
    private LinearLayout balsumFrame;
    private Tracker tracker;

    public BalSummary() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bal_summary, container, false);
        tracker = ((GlobalVariable) getActivity().getApplication()).getTracker(GlobalVariable.TrackerName.APP_TRACKER);
        tracker.setScreenName("Balance Summary Page");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
        balsumFrame = (LinearLayout) view.findViewById(R.id.ll_balsum_content);
        items = getResources().getStringArray(R.array.bs_status);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        balSum = (TextView) view.findViewById(R.id.balSum);
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/coperplategothiclight.ttf");
        balSum.setTypeface(font);
        from_Date = (Button) view.findViewById(R.id.b_balsum_from);
        to_Date = (Button) view.findViewById(R.id.b_balsum_to);
        btype = (Button) view.findViewById(R.id.b_balsum_type);
        ettype = (EditText) view.findViewById(R.id.et_balsum_type);
        bsearch = (Button) view.findViewById(R.id.b_balsum_search);
        todaybalsearch = (Button) view.findViewById(R.id.b_ts_get_today_tran_search);

        from_Date.setOnClickListener(this);
        to_Date.setOnClickListener(this);
        btype.setOnClickListener(this);
        bsearch.setOnClickListener(this);
        todaybalsearch.setOnClickListener(this);

        adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, items);

        prefs = this.getActivity().getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
        UserId = prefs.getString("userId", "");
        Key = prefs.getString("userKey", "");
        balsumFrame = (LinearLayout) view.findViewById(R.id.ll_balsum_content);
    }

    @Override
    public void onStart() {
        super.onStart();
        //Get an Analytics tracker to report app starts & uncaught exceptions etc.
        GoogleAnalytics.getInstance(getActivity()).reportActivityStart(getActivity());
    }

    @Override
    public void onStop() {
        super.onStop();
        //Stop the analytics tracking
        GoogleAnalytics.getInstance(getActivity()).reportActivityStop(getActivity());
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
                                TypeId = balsumtype.getTypeId(position);
                                if (btype.getText().toString().equals("Select"))
                                    TypeId = "0";
                                switch (position) {
                                    case 0:
                                        ettype.setHint("Enter Sender Number");
                                        break;
                                    case 1:
                                        ettype.setHint("Enter Transaction ID");
                                        break;
                                }
                                dialog.dismiss();
                            }
                        }).create().show();
                break;
            case R.id.b_balsum_search:
                if (from_Date.getText().toString().equals(to_Date.getText().toString())) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Error").setIcon(getResources().getDrawable(R.drawable.erroricon))
                            .setMessage("Enter proper range")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).create().show();
                    break;
                }
                if (from_Date.getText().toString().equals("from") || to_Date.getText().toString().equals("to")) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Error").setIcon(getResources().getDrawable(R.drawable.erroricon))
                            .setMessage("Enter From and To Date")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).create().show();
                    break;
                } else {
                    Value = ettype.getText().toString();
                    Bundle args = new Bundle();
                    args.putString("fromDate", fromDate);
                    args.putString("toDate", toDate);
                    args.putString("TypeId", TypeId);
                    args.putString("Value", Value);
                    startbalSumResultFragment(args);
                    balsumFrame.setVisibility(View.INVISIBLE);
                    clearField(ettype);
                }
                break;

            case R.id.b_ts_get_today_tran_search:
                Bundle args = new Bundle();
                args.putString("fromDate", "");
                args.putString("toDate", "");
                args.putString("TypeId", "0");
                args.putString("Value", null);
                startbalSumResultFragment(args);
                balsumFrame.setVisibility(View.INVISIBLE);
                clearField(ettype);
                break;
        }

    }

    private void startbalSumResultFragment(Bundle args) {
        BalSumResultFragment balSumResultFragment = new BalSumResultFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        balSumResultFragment.setArguments(args);
        ft.replace(R.id.balsumframe, balSumResultFragment);
        ft.commit();
    }

    private void clearField(EditText et) {
        et.setText("");
    }

}
