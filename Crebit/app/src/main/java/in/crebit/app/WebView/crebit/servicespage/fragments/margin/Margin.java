package in.crebit.app.WebView.crebit.servicespage.fragments.margin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.crebit.app.WebView.Applicaton.GlobalVariable;
import in.crebit.app.WebView.R;
import in.crebit.app.WebView.apinames.API;
import in.crebit.app.WebView.jsonparse.JSONParser;
import in.crebit.app.WebView.network.NetworkUtil;
import in.crebit.app.WebView.response.MarginResult;

public class Margin extends Fragment {
    private JSONParser jsonParser;
    private ListView listView;
    private Tracker tracker;
    private LinearLayout listviewlayout, progressbarlayout;

    public Margin() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_margin, container, false);
        listviewlayout = (LinearLayout) view.findViewById(R.id.ll_margin_listview);
        progressbarlayout = (LinearLayout) view.findViewById(R.id.ll_margin_progressbar);
        listView = (ListView) view.findViewById(R.id.operator_margin_list);
        tracker = ((GlobalVariable) getActivity().getApplication()).getTracker(GlobalVariable.TrackerName.APP_TRACKER);
        tracker.setScreenName("Margin Page");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
        new retrieveMargin().execute();
        return view;
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


    private class retrieveMargin extends AsyncTask<String, String, String> {
        ProgressDialog dialog = new ProgressDialog(getActivity());
        JSONArray jsonArray;
        JSONObject jsonObject;
        MarginResult marginResult;
        ArrayList<MarginResult> marginResultArrayList;

        @Override
        protected void onPreExecute() {
           /* dialog.setMessage("Please wait ...");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setCancelable(false);
            dialog.show();*/
            listviewlayout.setVisibility(View.GONE);
            progressbarlayout.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... strings) {
            jsonParser = new JSONParser();
            String Response = jsonParser.getResponse(API.DHS_OPERATOR_MARGIN);
            if (Response == null || Response.equals("error")) {
                return Response;
            } else {
                try {
                    jsonArray = new JSONArray(Response);
                    if (jsonArray == null) return null;

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return Response;
            }
        }

        @Override
        protected void onPostExecute(String status) {
            //dialog.dismiss();
            if (status == null) {
                showAlertDialog();
            } else if (status.equals("error")) {
                showErrorDialog();
            } else {
                listviewlayout.setVisibility(View.VISIBLE);
                progressbarlayout.setVisibility(View.GONE);
                marginResultArrayList = new ArrayList<MarginResult>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        jsonObject = jsonArray.getJSONObject(i);
                        marginResult = new MarginResult(String.valueOf(i + 1), jsonObject.getString("type"),
                                jsonObject.getString("name"), jsonObject.getString("margin"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    marginResultArrayList.add(marginResult);
                }
                if (listView != null)
                    listView.setAdapter(new OpMarCustomAdapter(getActivity(), marginResultArrayList));
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
                            new retrieveMargin().execute();
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
