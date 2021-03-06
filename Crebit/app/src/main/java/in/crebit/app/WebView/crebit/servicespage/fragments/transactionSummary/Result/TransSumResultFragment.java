package in.crebit.app.WebView.crebit.servicespage.fragments.transactionSummary.Result;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.crebit.app.WebView.Applicaton.GlobalVariable;
import in.crebit.app.WebView.R;
import in.crebit.app.WebView.apinames.API;
import in.crebit.app.WebView.crebit.servicespage.fragments.transactionSummary.TransSumResult;
import in.crebit.app.WebView.crebit.servicespage.fragments.transactionSummary.adapter.TransSumCustomAdapter;
import in.crebit.app.WebView.jsonparse.JSONParser;
import in.crebit.app.WebView.network.NetworkUtil;
import in.crebit.app.WebView.requestparam.TranSumParams;
import in.crebit.app.WebView.response.TranSumResponse;

public class TransSumResultFragment extends Fragment implements View.OnClickListener {
    private TextView tvprofit, tvamount, tvnodata;
    private JSONParser jsonParser;
    private JSONObject jsonResponse, tranResArrObject;
    private JSONArray tranResArr;
    private ListView resultList;
    private TranSumParams tranSumParams;
    private TranSumResponse tranSumResponse;
    private TransSumResult transSumResult;
    private ArrayList<NameValuePair> nameValuePairs;
    private ArrayList<TransSumResult> transSumResultList = new ArrayList<TransSumResult>();

    private Tracker tracker;
    private String UserId, Key, fromDate, toDate;
    private int StatusId, TypeId;

    private SharedPreferences prefs;
    private final static String MY_PREFS = "mySharedPrefs";
    private double TotalAmount, TotalProfit;
    private TransSumCustomAdapter tranSumCustomAdapter;
    private LinearLayout progressbarlayout;
    private ProgressBar progressBar;

    public TransSumResultFragment() {
    }

    @Override
    public void onClick(View view) {
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_trans_sum_result, container, false);
        prefs = getActivity().getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
        fromDate = getArguments().getString("fromDate");
        toDate = getArguments().getString("toDate");
        StatusId = getArguments().getInt("StatusId");
        TypeId = getArguments().getInt("TypeId");
        UserId = prefs.getString("userId", "");
        Key = prefs.getString("userKey", "");
        tranSumCustomAdapter = new TransSumCustomAdapter();
        tracker = ((GlobalVariable) getActivity().getApplication()).getTracker(GlobalVariable.TrackerName.APP_TRACKER);
        tracker.setScreenName("Transaction Summary Result Page");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
        initViews(view);
        new retrieveTransactionData().execute();
        return view;
    }

    private void initViews(View view) {
        tvprofit = (TextView) view.findViewById(R.id.tvprofit);
        tvamount = (TextView) view.findViewById(R.id.tvamount);
        tvnodata = (TextView) view.findViewById(R.id.tv_tranSum_list_nodata);
        resultList = (ListView) view.findViewById(R.id.lv_transum_result);
        progressbarlayout = (LinearLayout) view.findViewById(R.id.ll_transum_progressbar);
        progressBar = (ProgressBar) view.findViewById(R.id.transum_progressBar);
    }

    private class retrieveTransactionData extends AsyncTask<String, String, String> {
        ProgressDialog dialog = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
           /* dialog.setMessage("Please wait ...");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setCancelable(false);
            dialog.show();*/
            resultList.setVisibility(View.GONE);
            tvnodata.setVisibility(View.GONE);
            progressbarlayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            jsonParser = new JSONParser();
            tranSumParams = new TranSumParams(UserId, Key, fromDate, toDate, StatusId, TypeId);
            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("UserId", UserId));
            nameValuePairs.add(new BasicNameValuePair("Key", Key));
            nameValuePairs.add(new BasicNameValuePair("FromDate", fromDate));
            nameValuePairs.add(new BasicNameValuePair("ToDate", toDate));
            nameValuePairs.add(new BasicNameValuePair("StatusId", String.valueOf(StatusId)));
            nameValuePairs.add(new BasicNameValuePair("TypeId", String.valueOf(TypeId)));
            String Response = jsonParser.makeHttpPostRequestforJsonObject(API.DASHBOARD_TRANSACTION_DETAILS, nameValuePairs);
            if (Response == null || Response.equals("error")) {
                return Response;
            } else {
                try {
                    jsonResponse = new JSONObject(Response);
                    tranSumResponse = new TranSumResponse(jsonResponse.getDouble("totalAmount"),
                            jsonResponse.getDouble("totalProfit"), jsonResponse.getJSONArray("dL_TransactionReturns"));

                    TotalAmount = tranSumResponse.getTotalAmount();
                    TotalProfit = tranSumResponse.getTotalProfit();
                    tranResArr = tranSumResponse.getTranSumResults();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return String.valueOf(TotalAmount);
            }
        }

        @Override
        protected void onPostExecute(String status) {
            // dialog.dismiss();
            if (status == null) {
                showAlertDialog();
            } else if (status.equals("error")) {
                showErrorDialog();
            } else {
                tvamount.setText("Amount Rs: " + String.valueOf(TotalAmount));
                tvprofit.setText("Profit Rs: " + String.valueOf(TotalProfit));
                if (tranResArr.length() == 0) {
                    resultList.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    progressbarlayout.setVisibility(View.VISIBLE);
                    tvnodata.setVisibility(View.VISIBLE);
                } else {
                    tvnodata.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    progressbarlayout.setVisibility(View.GONE);
                    resultList.setVisibility(View.VISIBLE);

                    for (int i = 0; i < tranResArr.length(); i++) {
                        try {
                            tranResArrObject = (JSONObject) tranResArr.get(i);
                            transSumResult = new TransSumResult();
                            transSumResult.setCount(i + 1);
                            transSumResult.setId(tranResArrObject.getString("id"));
                            transSumResult.setcBalance(tranResArrObject.getString("cBalance"));
                            transSumResult.setAmount(tranResArrObject.getString("amount"));
                            transSumResult.setProfit(tranResArrObject.getString("profit"));
                            transSumResult.setSource(tranResArrObject.getString("source"));
                            transSumResult.settDate(tranResArrObject.getString("tDate"));
                            transSumResult.setStatus(tranResArrObject.getString("status"));
                            transSumResult.setOperaterName(tranResArrObject.getString("operaterName"));
                            transSumResult.setOperaterId(tranResArrObject.getInt("operaterId"));
                            transSumResult.setOpType(tranResArrObject.getInt("OpType"));
                            transSumResult.setCharge(tranResArrObject.getString("charge"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        transSumResultList.add(transSumResult);
                    }
                    if (resultList != null)
                        resultList.setAdapter(new TransSumCustomAdapter(getActivity(), transSumResultList));
                }
            }
        }
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("\tUnable to connect to Internet." +
                "\n \tCheck Your Network Connection.")
                .setCancelable(false)
                .setNegativeButton("Retry", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (isNetworkAvailable()) {
                            dialog.cancel();
                            new retrieveTransactionData().execute();
                        } else {
                            showAlertDialog();
                        }
                    }
                })
                .setNeutralButton("Turn on Data", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                        startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void showErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("\tThere was a problem with server " +
                "\n \tTry again after sometime")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private boolean isNetworkAvailable() {

        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static class NetworkChangeReceiver extends BroadcastReceiver {
        public NetworkChangeReceiver() {
        }

        @Override
        public void onReceive(final Context context, final Intent intent) {
            String status = NetworkUtil.getConnectivityStatusString(context);
        }
    }
}
