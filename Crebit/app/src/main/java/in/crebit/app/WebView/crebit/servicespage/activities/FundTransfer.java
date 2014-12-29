package in.crebit.app.WebView.crebit.servicespage.activities;

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
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.List;

import in.crebit.app.WebView.Applicaton.GlobalVariable;
import in.crebit.app.WebView.R;
import in.crebit.app.WebView.apinames.API;
import in.crebit.app.WebView.jsonparse.JSONParser;
import in.crebit.app.WebView.network.NetworkUtil;
import in.crebit.app.WebView.nullcheck.Check;
import in.crebit.app.WebView.requestparam.FundTransferParams;
import in.crebit.app.WebView.response.FundTransferResponse;

public class FundTransfer extends ActionBarActivity implements View.OnClickListener {
    private TextView number, amount;
    private EditText et_number, et_amount;
    private Button transfer;
    private String UserId, Key, MobileTo, Amount, UserTypeA, UserTypeB;

    private String Status, AvailableBalance;
    private GlobalVariable globalVariable;
    private JSONParser jsonParser;
    private JSONArray jsonArray;
    private JSONObject jsonResponse;
    private FundTransferResponse fundTransferResponse;
    private FundTransferParams fundTransferParams;
    private List<NameValuePair> nameValuePairs;
    private Tracker tracker;
    private SpannableString s;

    private SharedPreferences prefs;
    private final static String MY_PREFS = "mySharedPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tracker = ((GlobalVariable) getApplication()).getTracker(GlobalVariable.TrackerName.APP_TRACKER);
        tracker.setScreenName("FundTransfer Page");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());

        setContentView(R.layout.activity_fund_transfer);
        s = new SpannableString("Fund Transfer");
        s.setSpan(new in.crebit.app.WebView.customfont.TypefaceSpan(this, "coperplategothiclight.ttf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        globalVariable = (GlobalVariable) getApplicationContext();
        initViews();
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

    private void initViews() {
        number = (TextView) findViewById(R.id.tv_ft_number);
        amount = (TextView) findViewById(R.id.tv_ft_amount);

        et_number = (EditText) findViewById(R.id.et_ft_number);
        et_number.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                number.setVisibility(View.VISIBLE);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                number.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                number.setVisibility(View.VISIBLE);
            }

        });

        et_amount = (EditText) findViewById(R.id.et_ft_amount);
        et_amount.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                amount.setVisibility(View.VISIBLE);

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                amount.setVisibility(View.GONE);

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                amount.setVisibility(View.VISIBLE);

            }

        });

        transfer = (Button) findViewById(R.id.b_ft_recharge);
        transfer.setOnClickListener(this);

        prefs = getSharedPreferences(MY_PREFS, MODE_PRIVATE);
        UserId = prefs.getString("userId", "");
        Key = prefs.getString("userKey", "");

        if (UserId.equals("1")) {
            UserTypeA = "Enterprise";
            UserTypeB = "Personal";
        } else if (UserId.equals("2")) {
            UserTypeA = "Personal";
            UserTypeB = "Enterprise";
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.b_ft_recharge:


                tracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Button")
                        .setAction("Clicked on Transfer Button on FundTransfer Page")
                        .setLabel("Transfer Button")
                        .build());

                MobileTo = et_number.getText().toString();
                Amount = et_amount.getText().toString();
                if (Check.ifNull(Amount)) {
                    et_amount.setText("");
                    et_amount.setHint(" Enter valid Amount");
                    et_amount.setHintTextColor(getResources().getColor(R.color.red));
                    break;
                } else if (Check.ifNumberInCorrect(MobileTo)) {
                    et_number.setText("");
                    et_number.setHint(" Enter correct number");
                    et_number.setHintTextColor(getResources().getColor(R.color.red));
                    break;
                }
                new retrievefundtransferdata().execute();
                break;

        }
    }

    private class retrievefundtransferdata extends AsyncTask<String, String, String> {
        ProgressDialog dialog = new ProgressDialog(FundTransfer.this);

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Please Wait...");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            jsonParser = new JSONParser();
            fundTransferParams = new FundTransferParams(UserId, Key, MobileTo, Amount);
            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("userId", UserId));
            nameValuePairs.add(new BasicNameValuePair("key", Key));
            nameValuePairs.add(new BasicNameValuePair("mobileTo", MobileTo));
            nameValuePairs.add(new BasicNameValuePair("amount", Amount));
            jsonArray = jsonParser.makeHttpPostRequest(API.DASHBOARD_TRANSFER, nameValuePairs);
            if (jsonArray == null) {
                return null;
            } else {
                try {
                    jsonResponse = jsonArray.getJSONObject(0);
                    fundTransferResponse = new FundTransferResponse(jsonResponse.getString("status"),
                            jsonResponse.getString("availableBalance"));
                    Status = fundTransferResponse.getStatus();
                    AvailableBalance = fundTransferResponse.getAvailableBalance();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return Status;
            }
        }

        @Override
        protected void onPostExecute(String Status) {
            dialog.dismiss();
            if (Status == null) {
                showAlertDialog();
            } else {
                switch (Integer.parseInt(Status)) {
                    case 1:
                    case 2:
                        clearField(et_number);
                        clearField(et_amount);
                        new AlertDialog.Builder(FundTransfer.this)
                                .setTitle("Success").setIcon(getResources().getDrawable(R.drawable.successicon))
                                .setMessage(" Transfer Completed. " +
                                        "\n\nAvailable Balance: " + AvailableBalance)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                }).create().show();
                        globalVariable.setAvailableBalance(AvailableBalance);
                        break;
                    case 3:
                        new AlertDialog.Builder(FundTransfer.this)
                                .setTitle("Error").setIcon(getResources().getDrawable(R.drawable.erroricon))
                                .setMessage("Not Enough Balance" +
                                        "\n\n Available Balance: " + AvailableBalance)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                }).create().show();

                        break;
                    case 4:
                        new AlertDialog.Builder(FundTransfer.this)
                                .setTitle("Error").setIcon(getResources().getDrawable(R.drawable.erroricon))
                                .setMessage("Mobile Number Incorrect")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                }).create().show();
                        break;
                    case 5:
                        new AlertDialog.Builder(FundTransfer.this)
                                .setTitle("Error").setIcon(getResources().getDrawable(R.drawable.erroricon))
                                .setMessage(" Cannot Transfer From " + UserTypeA.toUpperCase() + " to " + UserTypeB.toUpperCase())
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                }).create().show();
                        break;
                    case 6:
                        new AlertDialog.Builder(FundTransfer.this)
                                .setTitle("Error").setIcon(getResources().getDrawable(R.drawable.erroricon))
                                .setMessage("Cannot Transfer to the same account")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                }).create().show();
                        break;
                }
            }
        }
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("\tUnable to connect to Internet." +
                "\n \tCheck Your Network Connection.")
                .setCancelable(false)
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

    private boolean isNetworkAvailable() {

        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
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
    private void clearField(EditText et) {
        et.setText("");
    }

}
