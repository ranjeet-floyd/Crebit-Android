package in.crebit.app.WebView.crebit.servicespage.fragments.BankAccPay;


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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import in.crebit.app.WebView.requestparam.BankAccParams;
import in.crebit.app.WebView.response.BankAccResponse;

public class
        BankAccPay extends Fragment implements View.OnClickListener {
    private TextView bankAccPay, tvname, tvaccno, tvifsc, tvmobile, tvamount;
    private EditText etname, etaccno, etifsc, etmobile, etamount;
    private Button bsubmit;
    private String UserId, Key, Mobile, Name, Account, IFSC;
    private double Amount;
    private String Status, AvailableBalance, RefId;

    private String[] items;
    private JSONParser jsonParser;
    private JSONObject jsonResponse;
    private BankAccParams bankAccParams;
    private BankAccResponse bankAccResponse;
    private List<NameValuePair> nameValuePairs;

    private SharedPreferences prefs;
    private final static String MY_PREFS = "mySharedPrefs";
    private Tracker tracker;

    public BankAccPay() {
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
        View view = inflater.inflate(R.layout.fragment_bank_acc_pay, container, false);
        tracker = ((GlobalVariable) getActivity().getApplication()).getTracker(GlobalVariable.TrackerName.APP_TRACKER);
        tracker.setScreenName("Bank Account Pay Page");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
        initViews(view);
        return view;
    }

    private void initViews(View view) {

        tvname = (TextView) view.findViewById(R.id.tv_bap_name);
        tvaccno = (TextView) view.findViewById(R.id.tv_bap_accNum);
        tvmobile = (TextView) view.findViewById(R.id.tv_bap_mobNum);
        tvifsc = (TextView) view.findViewById(R.id.tv_bap_ifsc);
        tvamount = (TextView) view.findViewById(R.id.tv_bap_amount);

        etname = (EditText) view.findViewById(R.id.et_bap_name);
        etname.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                tvname.setVisibility(View.VISIBLE);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                tvname.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvname.setVisibility(View.VISIBLE);
            }

        });

        etaccno = (EditText) view.findViewById(R.id.et_bap_accNum);
        etaccno.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                tvaccno.setVisibility(View.VISIBLE);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                tvaccno.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvaccno.setVisibility(View.VISIBLE);
            }

        });

        etifsc = (EditText) view.findViewById(R.id.et_bap_ifsc);
        etifsc.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                tvifsc.setVisibility(View.VISIBLE);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                tvifsc.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvifsc.setVisibility(View.VISIBLE);
            }

        });

        etmobile = (EditText) view.findViewById(R.id.et_bap_mobNum);
        etmobile.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                tvmobile.setVisibility(View.VISIBLE);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                tvmobile.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvmobile.setVisibility(View.VISIBLE);
            }

        });

        etamount = (EditText) view.findViewById(R.id.et_bap_amount);
        etamount.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                tvamount.setVisibility(View.VISIBLE);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                tvamount.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvamount.setVisibility(View.VISIBLE);
            }

        });

        bsubmit = (Button) view.findViewById(R.id.b_bap_submit);
        bsubmit.setOnClickListener(this);

        prefs = getActivity().getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
        UserId = prefs.getString("userId", "");
        Key = prefs.getString("userKey", "");

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.b_bap_submit:
                tracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Button")
                        .setAction("Clicked on Submit Button on BankAccountPay Page")
                        .setLabel("To Button")
                        .build());
                Name = etname.getText().toString();
                Account = etaccno.getText().toString();
                IFSC = etifsc.getText().toString();
                Mobile = etmobile.getText().toString();
                try {
                    Amount = Double.parseDouble(etamount.getText().toString());
                } catch (Exception e) {
                    Amount = 0;
                }
                if (Check.ifNull(Name)) {
                    etname.setText("");
                    etname.setHint(" Enter Name");
                    etname.setHintTextColor(getResources().getColor(R.color.red));
                    break;
                }
                if (Check.ifNull(Account)) {
                    etaccno.setText("");
                    etaccno.setHint(" Enter Account Number");
                    etaccno.setHintTextColor(getResources().getColor(R.color.red));
                    break;
                }
                if (Check.ifIncorrectIFSC(IFSC)) {
                    etifsc.setText("");
                    etifsc.setHint(" Enter IFSC code");
                    etifsc.setHintTextColor(getResources().getColor(R.color.red));
                    break;
                }
                if (Check.ifNull(Mobile)) {
                    etmobile.setText("");
                    etmobile.setHint(" Enter Mobile Number");
                    etmobile.setHintTextColor(getResources().getColor(R.color.red));
                    break;
                }
                if (Check.ifEmpty(Amount)) {
                    etamount.setText("");
                    etamount.setHint(" Enter Amount");
                    etamount.setHintTextColor(getResources().getColor(R.color.red));
                    break;
                }
                new retrievedata().execute();
                break;

        }
    }

    private class retrievedata extends AsyncTask<String, String, String> {
        ProgressDialog dialog = new ProgressDialog(getActivity());

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
            bankAccParams = new BankAccParams(UserId, Key, Mobile, Name, Account, IFSC, Amount);
            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("UserId", UserId));
            nameValuePairs.add(new BasicNameValuePair("Key", Key));
            nameValuePairs.add(new BasicNameValuePair("Mobile", Mobile));
            nameValuePairs.add(new BasicNameValuePair("Name", Name));
            nameValuePairs.add(new BasicNameValuePair("IFSC", IFSC));
            nameValuePairs.add(new BasicNameValuePair("Account", Account));
            nameValuePairs.add(new BasicNameValuePair("Amount", String.valueOf(Amount)));
            String Response = jsonParser.makeHttpPostRequestforJsonObject(API.DHS_BANK_ACC_PAY, nameValuePairs);
            if (Response == null || Response.equals("error")) {
                return Response;
            } else {
                try {
                    jsonResponse = new JSONObject(Response);
                    bankAccResponse = new BankAccResponse(jsonResponse.getString("status"),
                            jsonResponse.getString("availableBalance"),
                            jsonResponse.getString("refId"));

                    AvailableBalance = bankAccResponse.getAvailableBalance();
                    Status = bankAccResponse.getStatus();
                    RefId = bankAccResponse.getRefId();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return Status;
            }
        }

        @Override
        protected void onPostExecute(String StatusCode) {
            if (StatusCode == null) {
                showAlertDialog();
            } else if (StatusCode.equals("error")) {
                showErrorDialog();
            } else if (StatusCode.equals("0") || StatusCode.equals("-1")) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Error").setIcon(getResources().getDrawable(R.drawable.erroricon))
                        .setMessage("Request Not Completed.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create().show();
            } else if (StatusCode.equals("1")) {
                clearField(etname);
                clearField(etaccno);
                clearField(etifsc);
                clearField(etmobile);
                clearField(etamount);
                new AlertDialog.Builder(getActivity())
                        .setTitle("Success").setIcon(getResources().getDrawable(R.drawable.successicon))
                        .setMessage("Request Completed." +
                                "\n\nAvailable Balance: " + AvailableBalance +
                                "\nStatus: " + Status +
                                "\nReference ID: " + RefId)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create().show();

            } else if (StatusCode.equals("2")) {
                new AlertDialog.Builder(getActivity())
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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

    private void clearField(EditText et) {
        et.setText("");
    }

}
