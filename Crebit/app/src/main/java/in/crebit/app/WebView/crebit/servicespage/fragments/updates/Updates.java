package in.crebit.app.WebView.crebit.servicespage.fragments.updates;

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

public class Updates extends Fragment {
    private Tracker tracker;
    private JSONParser jsonParser;
    private ListView listView;
    private LinearLayout listviewlayout, progressbarlayout;

    public Updates() {
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
        View view = inflater.inflate(R.layout.fragment_updates, container, false);
        listviewlayout = (LinearLayout) view.findViewById(R.id.ll_updates_listview);
        progressbarlayout = (LinearLayout) view.findViewById(R.id.ll_updates_progressbar);
        listView = (ListView) view.findViewById(R.id.updates_list);

        tracker = ((GlobalVariable) getActivity().getApplication()).getTracker(GlobalVariable.TrackerName.APP_TRACKER);
        tracker.setScreenName("Updates Page");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
        new update().execute();
        return view;
    }

    private class update extends AsyncTask<String, String, String> {
        ProgressDialog dialog = new ProgressDialog(getActivity());
        String jsonResult;
        JSONArray jsonArray;
        JSONObject jsonObject;
        UpdateResult updateResult;
        ArrayList<UpdateResult> updateResultArrayList = new ArrayList<UpdateResult>();

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
        protected String doInBackground(String... params) {
            jsonParser = new JSONParser();
            jsonResult = jsonParser.getResponse(API.DHS_ADMIN_UPDATE);
            if (jsonResult == null) return null;
            try {
                jsonArray = new JSONArray(jsonResult);
                if (jsonArray == null) return null;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonResult;
        }


        @Override
        protected void onPostExecute(String s) {
            if (jsonResult == null || jsonArray == null) {
                showAlertDialog();
            }
            // dialog.dismiss();
            else {
                listviewlayout.setVisibility(View.VISIBLE);
                progressbarlayout.setVisibility(View.GONE);
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        jsonObject = jsonArray.getJSONObject(i);
                        updateResult = new UpdateResult(String.valueOf(i + 1), jsonObject.getString("text"),
                                jsonObject.getString("fDate"), jsonObject.getString("tDate"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    updateResultArrayList.add(updateResult);
                }
                if (listView != null)
                    listView.setAdapter(new UpdatesCustomAdapter(getActivity(), updateResultArrayList));
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
                            new update().execute();
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
