package in.crebit.app.WebView.crebit.servicespage.fragments.BankAccPay;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import in.crebit.app.WebView.jsonparse.JSONParser;
import in.crebit.app.WebView.network.NetworkUtil;
import in.crebit.app.WebView.requestparam.BAPparams;
import in.crebit.app.WebView.response.BAPResponse;

public class Last5Trans extends Fragment {
    private Tracker tracker;
    private TextView tvnodata;
    private ListView resultList;
    private GlobalVariable globalVariable;
    private String userId, password;
    private ArrayList<BankAccPayResult> bankAccPayResultList = new ArrayList<BankAccPayResult>();
    private ArrayList<NameValuePair> nameValuePairs;
    private BAPResponse bapResponse;
    private BAPparams bapParams;
    private BankAccPayResult bapResult;
    private JSONParser jsonParser;
    private JSONArray lasTranArr;
    private JSONObject lasTranArrobject;
    private LinearLayout progressbarlayout;
    private ProgressBar progressBar;

    public Last5Trans() {
        // Required empty public constructor
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_last5_trans, container, false);
        tracker = ((GlobalVariable) getActivity().getApplication()).getTracker(GlobalVariable.TrackerName.APP_TRACKER);
        tracker.setScreenName("Last 5 transaction Page");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
        globalVariable = (GlobalVariable) getActivity().getApplicationContext();
        userId = globalVariable.getUserID();
        password = globalVariable.getUserKey();
        initViews(view);
        new retrievelasTranData().execute();
        return view;
    }

    private void initViews(View view) {
        tvnodata = (TextView) view.findViewById(R.id.tv_bap_list_nodata);
        resultList = (ListView) view.findViewById(R.id.lv_bap_result);
        progressbarlayout = (LinearLayout) view.findViewById(R.id.ll_bap_progressbar);
        progressBar = (ProgressBar) view.findViewById(R.id.bap_progressBar);

    }

    private class retrievelasTranData extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            resultList.setVisibility(View.GONE);
            tvnodata.setVisibility(View.GONE);
            progressbarlayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            jsonParser = new JSONParser();
            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("userId", userId));
            nameValuePairs.add(new BasicNameValuePair("password", password));
            String Response = jsonParser.makeHttpPostRequest(API.DASHBOARD_GET_ACCOUNT, nameValuePairs);
            if (Response == null || Response.equals("error"))
                return Response;
            else {
                try {
                    lasTranArr = new JSONArray(Response);
                    if (lasTranArr == null)
                        return null;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return Response;
            }
        }

        @Override
        protected void onPostExecute(String status) {
            if (status == null) {
                showAlertDialog();
            } else if (status.equals("error")) {
                showErrorDialog();
            } else {
                if (lasTranArr.length() == 0) {
                    resultList.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    progressbarlayout.setVisibility(View.VISIBLE);
                    tvnodata.setVisibility(View.VISIBLE);
                } else {
                    tvnodata.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    progressbarlayout.setVisibility(View.GONE);
                    resultList.setVisibility(View.VISIBLE);
                    for (int i = 0; i < lasTranArr.length(); i++) {
                        try {
                            lasTranArrobject = (JSONObject) lasTranArr.get(i);
                            bapResult = new BankAccPayResult();
                            bapResult.setCount(String.valueOf(i + 1));
                            bapResult.setName(lasTranArrobject.getString("name"));
                            bapResult.setAccount(lasTranArrobject.getString("account"));
                            bapResult.setiFSC(lasTranArrobject.getString("iFSC"));
                            bapResult.setMobile(lasTranArrobject.getString("mobile"));
                            bapResult.setAmount(lasTranArrobject.getString("amount"));
                            bapResult.setStatus(getStatusResult(Integer.parseInt(lasTranArrobject.getString("status"))));
                            Log.e("Status", bapResult.getStatus());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //doubt  check this
                        bankAccPayResultList.clear();
                        bankAccPayResultList.add(bapResult);
                    }
                    if (resultList != null) {
                        resultList.setAdapter(new BapCustomAdapter(getActivity(), bankAccPayResultList));
                    }
                }
            }
        }

        private String getStatusResult(int status) {
            String result = "";
            switch (status) {
                case 0:
                    result = "Failed";
                    break;
                case 1:
                    result = "Success";
                    break;
                case 2:
                    result = "Pending";
                    break;
                case 3:
                    result = "In Progress";
                    break;
                case 4:
                    result = "Reject";
                    break;
                case 5:
                    result = "Received";
                    break;
                case 6:
                    result = "Other";
                    break;
                case 7:
                    result = "Not Known";
                    break;
                case 8:
                    result = "Awaiting";
                    break;
                case 9:
                    result = "Refunded";
                    break;

            }
            return result;

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
                            new retrievelasTranData().execute();
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
