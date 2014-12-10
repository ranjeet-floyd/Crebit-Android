package com.bitblue.crebit.servicespage.fragments.balanceSummary;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.bitblue.crebit.R;
import com.bitblue.crebit.servicespage.listAdapter.BalSumResult;
import com.bitblue.jsonparse.JSONParser;
import com.bitblue.requestparam.BalSumParams;
import com.bitblue.response.BalSumResponse;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BalSumResultFragment extends Fragment {
    private TextView tvtotalbalgiven, tvbalgiven, tvbalSumName, tvBalSumAmount,
            tvBalSumContact, tvBalSumDate, tvBalSumType, tvBalSumTransId;
    private ListView resultList;

    private String Name, Amount, Contact, Date, Type, TransId;
    private Double TotalBalanceGiven;

    private static final String SOURCE = "2";
    private ArrayList<BalSumResult> balSumResultList;
    private String UserId, Key, fromDate, toDate;
    private SharedPreferences prefs;
    private final static String MY_PREFS = "mySharedPrefs";

    private JSONArray balanceUse;
    private JSONParser jsonParser;
    private JSONObject jsonResponse;
    private BalSumParams balSumParams;
    private BalSumResponse balSumResponse;
    private List<NameValuePair> nameValuePairs;

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
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        tvtotalbalgiven = (TextView) view.findViewById(R.id.tvtotalgiven);
        tvbalgiven = (TextView) view.findViewById(R.id.tvbalgiven);
        tvbalSumName = (TextView) view.findViewById(R.id.tv_balsum_name);
        tvBalSumAmount = (TextView) view.findViewById(R.id.tv_balsum_amount);
        tvBalSumContact = (TextView) view.findViewById(R.id.tv_balsum_contact);
        tvBalSumDate = (TextView) view.findViewById(R.id.tv_balsum_date);
        tvBalSumType = (TextView) view.findViewById(R.id.tv_balsum_type);
        tvBalSumTransId = (TextView) view.findViewById(R.id.tv_balsum_transId);


        //initialize the list

       /* resultList = (ListView) view.findViewById(R.id.lv_balsum_result);
        resultList.setAdapter(new BalSumCustomAdapter(getActivity(), balSumResultList));*/
    }


}
