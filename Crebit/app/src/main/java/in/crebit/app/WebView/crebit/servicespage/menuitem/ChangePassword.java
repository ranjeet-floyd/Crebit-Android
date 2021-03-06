package in.crebit.app.WebView.crebit.servicespage.menuitem;

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
import android.support.v7.app.ActionBarActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.crebit.app.WebView.Applicaton.GlobalVariable;
import in.crebit.app.WebView.R;
import in.crebit.app.WebView.apinames.API;
import in.crebit.app.WebView.jsonparse.JSONParser;
import in.crebit.app.WebView.network.NetworkUtil;
import in.crebit.app.WebView.nullcheck.Check;
import in.crebit.app.WebView.requestparam.ChangePassParams;
import in.crebit.app.WebView.response.ChangePassResponse;

public class ChangePassword extends ActionBarActivity implements View.OnClickListener {
    private TextView old_pass, new_pass;
    private String userId, key, olpass, nwpass, status;
    private EditText et_oldPass, et_newPass;
    private Button change;

    private ChangePassParams changePassParams;
    private ChangePassResponse changePassResponse;

    private JSONParser jsonParser;
    private JSONObject jsonResponse;
    private List<NameValuePair> nameValuePairs;
    private Tracker tracker;

    private SharedPreferences prefs;
    private final static String MY_PREFS = "mySharedPrefs";
    private SpannableString s;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tracker = ((GlobalVariable) getApplication()).getTracker(GlobalVariable.TrackerName.APP_TRACKER);
        tracker.setScreenName("Change password Page");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
        setContentView(R.layout.activity_change_password);
        s = new SpannableString("Change Password");
        s.setSpan(new in.crebit.app.WebView.customfont.TypefaceSpan(this, "coperplategothiclight.ttf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);
        initViews();
    }

    private void initViews() {
        old_pass = (TextView) findViewById(R.id.tv_cp_oldpass);
        new_pass = (TextView) findViewById(R.id.tv_cp_newpass);
        et_oldPass = (EditText) findViewById(R.id.et_cp_oldpass);

        et_newPass = (EditText) findViewById(R.id.et_cp_newpass);

        change = (Button) findViewById(R.id.b_cp_change);
        change.setOnClickListener(this);
        prefs = getSharedPreferences(MY_PREFS, MODE_PRIVATE);
        userId = prefs.getString("userId", "");
        key = prefs.getString("userKey", "");
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Get an Analytics tracker to report app starts & uncaught exceptions etc.
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //Stop the analytics tracking
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.b_cp_change:
                tracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Button")
                        .setAction("Clicked on change password Button on ChangePassword Page")
                        .setLabel("Login Button")
                        .build());
                olpass = et_oldPass.getText().toString();
                nwpass = et_newPass.getText().toString();
                if (Check.ifNull(olpass)) {
                    et_oldPass.setText("");
                    et_oldPass.setHint(" Enter old password");
                    et_oldPass.setHintTextColor(getResources().getColor(R.color.red));
                    break;
                }
                if (Check.ifNull(nwpass)) {
                    et_newPass.setText("");
                    et_newPass.setHint(" Enter new password");
                    et_newPass.setHintTextColor(getResources().getColor(R.color.red));
                    break;
                }
                new changePass().execute();
                break;
        }
    }

    private class changePass extends AsyncTask<String, String, String> {
        ProgressDialog dialog = new ProgressDialog(ChangePassword.this);

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Please Wait...");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            changePassParams = new ChangePassParams(userId, key, olpass, nwpass);
            jsonParser = new JSONParser();
            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("UserId", userId));
            nameValuePairs.add(new BasicNameValuePair("Key", key));
            nameValuePairs.add(new BasicNameValuePair("OPass", olpass));
            nameValuePairs.add(new BasicNameValuePair("NPass", nwpass));
            String Response = jsonParser.makeHttpPostRequestforJsonObject(API.DHS_CHANGE_PASSWORD, nameValuePairs);
            if (Response == null || Response.equals("error"))
                return Response;
            else {
                try {
                    jsonResponse = new JSONObject(Response);
                    changePassResponse = new ChangePassResponse(jsonResponse.getString("status"));
                    status = changePassResponse.getStatus();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return status;
            }

        }

        @Override
        protected void onPostExecute(String status) {
            dialog.dismiss();
            if (status == null)
                showAlertDialog();
            else if (status.equals("error")) {
                showErrorDialog();
            } else if (status.equals("1")) {
                clearField(et_oldPass);
                clearField(et_newPass);
                new AlertDialog.Builder(ChangePassword.this)
                        .setTitle("Password Recovery")
                        .setMessage("  Password has been changed\n" +
                                "\t\t\tProceed to Login Page. ")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create().show();
            } else {
                new AlertDialog.Builder(ChangePassword.this)
                        .setTitle("Error")
                        .setMessage(" Check the old Password ")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create().show();

            }
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("\tUnable to connect to Internet." +
                "\n \tCheck Your Network Connection.")
                .setCancelable(false)
                .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton("Retry", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (isNetworkAvailable()) {
                            dialog.cancel();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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

    public static class NetworkChangeReceiver extends BroadcastReceiver {
        public NetworkChangeReceiver() {
        }

        @Override
        public void onReceive(final Context context, final Intent intent) {
            String status = NetworkUtil.getConnectivityStatusString(context);
        }
    }

    private void clearField(EditText et) {
        et.setText("");
    }

}
