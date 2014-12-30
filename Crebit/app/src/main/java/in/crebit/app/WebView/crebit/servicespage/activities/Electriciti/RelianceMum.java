package in.crebit.app.WebView.crebit.servicespage.activities.Electriciti;

import android.app.Activity;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
import in.crebit.app.WebView.requestparam.RelianceParams;
import in.crebit.app.WebView.response.RelianceResponse;

public class RelianceMum extends Activity implements View.OnClickListener {
    private String UserId, Key, Account, Amount, Number, OperatorID = "41";
    private static final String Source = "2";
    TextView tvcustAccNo, tvcycCode, tvAmount;
    EditText etcustAccNo, etcycCode, etAmount;
    Button bpayBill;
    private JSONParser jsonParser;
    private JSONObject jsonResponse;
    private List<NameValuePair> nameValuePairs;
    private RelianceParams relianceParams;
    private RelianceResponse relianceResponse;
    private String TransId, Message, AvailableBalance;
    private int StatusCode;
    private SharedPreferences prefs;
    private final static String MY_PREFS = "mySharedPrefs";
    private GlobalVariable globalVariable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reliance_mum);
        initViews();
    }

    private void initViews() {
        tvcustAccNo = (TextView) findViewById(R.id.tv_elec_reliance_cust_acc_no);
        tvcycCode = (TextView) findViewById(R.id.tv_elec_reliance_cycode);
        tvAmount = (TextView) findViewById(R.id.tv_elec_reliance_amount);
        etcustAccNo = (EditText) findViewById(R.id.et_elec_reliance_cust_acc_no);
        etcustAccNo.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                tvcustAccNo.setVisibility(View.VISIBLE);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                tvcustAccNo.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvcustAccNo.setVisibility(View.VISIBLE);
            }

        });
        etcycCode = (EditText) findViewById(R.id.et_elec_reliance_cycode);
        etcycCode.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                tvcycCode.setVisibility(View.VISIBLE);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                tvcycCode.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvcycCode.setVisibility(View.VISIBLE);
            }

        });

        etAmount = (EditText) findViewById(R.id.et_elec_reliance_amount);
        etAmount.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                tvAmount.setVisibility(View.VISIBLE);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                tvAmount.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvAmount.setVisibility(View.VISIBLE);
            }

        });

        bpayBill = (Button) findViewById(R.id.b_elec_reliance_payBill);
        bpayBill.setOnClickListener(this);
        prefs = getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
        UserId = prefs.getString("userId", "");
        Key = prefs.getString("userKey", "");
        globalVariable = (GlobalVariable) getApplicationContext();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.b_elec_reliance_payBill:
                Number = etcustAccNo.getText().toString();
                Account = etcycCode.getText().toString();
                Amount = etAmount.getText().toString();
                if (Check.ifAccountNumberIncorrect(Number)) {
                    etcustAccNo.setText("");
                    etcustAccNo.setHint("Enter Correct Account Number");
                    etcustAccNo.setHintTextColor(getResources().getColor(R.color.red));
                    break;
                }
                if (Check.ifNull(Account)) {
                    etcustAccNo.setText("");
                    etcustAccNo.setHint("Enter Correct Cycle/Div. Code");
                    etcustAccNo.setHintTextColor(getResources().getColor(R.color.red));
                    break;
                }
                double Amont;
                try {
                    Amont = Double.parseDouble(Amount);
                } catch (Exception e) {
                    Amont = 0;
                }
                if (Check.ifEmpty(Amont)) {
                    etAmount.setText("");
                    etAmount.setHint("Enter Correct Amount");
                    etAmount.setHintTextColor(getResources().getColor(R.color.red));
                    break;
                }
                new retrieveData().execute();

                break;
        }
    }

    private class retrieveData extends AsyncTask<String, String, String> {
        ProgressDialog dialog = new ProgressDialog(RelianceMum.this);

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
            relianceParams = new RelianceParams(UserId, Key, OperatorID, Number, Amount, Source);
            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("UserId", UserId));
            nameValuePairs.add(new BasicNameValuePair("Key", Key));
            nameValuePairs.add(new BasicNameValuePair("OperatorId", OperatorID));
            nameValuePairs.add(new BasicNameValuePair("Number", Number));
            nameValuePairs.add(new BasicNameValuePair("Amount", String.valueOf(Amount)));
            nameValuePairs.add(new BasicNameValuePair("Source", Source));
            String Response = jsonParser.makeHttpPostRequestforJsonObject(API.DASHBOARD_SERVICE, nameValuePairs);
            if (Response == null ||Response.equals("error")) {
                return Response;
            } else {
                try {
                    jsonResponse = new JSONObject(Response);
                    relianceResponse = new RelianceResponse(jsonResponse.getString("transId"),
                            jsonResponse.getString("message"),
                            jsonResponse.getInt("statusCode"),
                            jsonResponse.getString("availableBalance"));

                    TransId = relianceResponse.getTransId();
                    Message = relianceResponse.getMessage();
                    StatusCode = relianceResponse.getStatusCode();
                    AvailableBalance = relianceResponse.getAvailableBalance();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return String.valueOf(StatusCode);
            }
        }

        @Override
        protected void onPostExecute(String StatusCode) {
            dialog.dismiss();
            if (StatusCode == null) {
                showAlertDialog();
            } else if (StatusCode.equals("error")) {
                showErrorDialog();
            } else if (StatusCode.equals("0") || StatusCode.equals("-1")) {
                new AlertDialog.Builder(RelianceMum.this)
                        .setTitle("Error").setIcon(getResources().getDrawable(R.drawable.erroricon))
                        .setMessage("Request Not Completed.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create().show();
            } else if (StatusCode.equals("1")) {
                new AlertDialog.Builder(RelianceMum.this)
                        .setTitle("Success").setIcon(getResources().getDrawable(R.drawable.successicon))
                        .setMessage("Request Completed." +
                                "\n\nAvailable Balance: " + AvailableBalance)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create().show();
                globalVariable.setAvailableBalance(AvailableBalance);
            } else if (StatusCode.equals("2")) {
                new AlertDialog.Builder(RelianceMum.this)
                        .setTitle("Error").setIcon(getResources().getDrawable(R.drawable.erroricon))
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
}
