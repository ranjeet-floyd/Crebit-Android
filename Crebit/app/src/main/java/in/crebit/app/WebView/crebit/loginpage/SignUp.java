package in.crebit.app.WebView.crebit.loginpage;

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
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.crebit.app.WebView.R;
import in.crebit.app.WebView.apinames.API;
import in.crebit.app.WebView.jsonparse.JSONParser;
import in.crebit.app.WebView.network.NetworkUtil;
import in.crebit.app.WebView.nullcheck.Check;
import in.crebit.app.WebView.requestparam.SignUpParams;
import in.crebit.app.WebView.response.SignUpResponse;


public class SignUp extends ActionBarActivity implements View.OnClickListener {
    private JSONParser jsonParser = new JSONParser();
    private JSONArray jsonArray;
    private TextView tvaccType, tvname, tvmobNum, tvpasswd;
    private EditText etname, etmobNum, etpasswd;
    private Button bSignUpSubmit, baccType;
    private String name, mobilenumber, password, status;
    private int userType;
    private ArrayAdapter<String> adapter;
    private String[] items;
    private List<NameValuePair> nameValuePairs;
    private JSONObject JsonResponse;
    private SignUpParams signUpParams;
    private SignUpResponse signUpResponse;
    private SpannableString s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        s = new SpannableString("Sign Up");
        s.setSpan(new in.crebit.app.WebView.customfont.TypefaceSpan(this, "coperplategothiclight.ttf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        items = getResources().getStringArray(R.array.accountType);
        initViews();
    }

    public void initViews() {
        tvaccType = (TextView) findViewById(R.id.tv_accType);
        tvname = (TextView) findViewById(R.id.tv_name);
        tvmobNum = (TextView) findViewById(R.id.tv_mobNum);
        tvpasswd = (TextView) findViewById(R.id.tv_passwd);
        etname = (EditText) findViewById(R.id.et_name);
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

        etpasswd = (EditText) findViewById(R.id.et_passwd);
        etpasswd.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                tvpasswd.setVisibility(View.VISIBLE);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                tvpasswd.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvpasswd.setVisibility(View.VISIBLE);
            }

        });

        etmobNum = (EditText) findViewById(R.id.et_mobNum);
        etmobNum.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                tvmobNum.setVisibility(View.VISIBLE);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                tvmobNum.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvmobNum.setVisibility(View.VISIBLE);
            }

        });

        bSignUpSubmit = (Button) findViewById(R.id.b_signUpSubmit);
        baccType = (Button) findViewById(R.id.b_accType);
        bSignUpSubmit.setOnClickListener(this);
        baccType.setOnClickListener(this);
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, items);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.b_accType:
                new AlertDialog.Builder(this)
                        .setTitle("Select Account")
                        .setAdapter(adapter, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int position) {
                                userType = position + 1;
                                baccType.setText(items[position]);
                                dialog.dismiss();
                            }
                        }).create().show();
                break;
            case R.id.b_signUpSubmit:
                name = etname.getText().toString();
                mobilenumber = etmobNum.getText().toString();
                password = etpasswd.getText().toString();
                if (baccType.getText().equals("Select")) {
                    tvaccType.setTextColor(getResources().getColor(R.color.red));
                    break;
                }
                if (Check.ifNull(name)) {
                    clearField(etname);
                    etname.setHint(" Enter Name");
                    etname.setHintTextColor(getResources().getColor(R.color.red));
                    break;
                }
                if (Check.ifNumberInCorrect(mobilenumber)) {
                    clearField(etmobNum);
                    etmobNum.setHint(" Enter correct number");
                    etmobNum.setHintTextColor(getResources().getColor(R.color.red));
                    break;
                }
                if (Check.ifNull(password)) {
                    clearField(etpasswd);
                    etpasswd.setHint(" Enter Password");
                    etpasswd.setHintTextColor(getResources().getColor(R.color.red));
                    break;
                }
                new storeData().execute();
                break;
        }
    }

    private class storeData extends AsyncTask<String, String, String> {
        ProgressDialog dialog = new ProgressDialog(SignUp.this);

        @Override
        protected void onPreExecute() {
            dialog.setTitle("Please Wait");
            dialog.setMessage("Registering...");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            signUpParams = new SignUpParams(userType, name, password, mobilenumber);
            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("userType", String.valueOf(userType)));
            nameValuePairs.add(new BasicNameValuePair("name", name));
            nameValuePairs.add(new BasicNameValuePair("pass", password));
            nameValuePairs.add(new BasicNameValuePair("mobile", mobilenumber));
            jsonArray = jsonParser.makeHttpPostRequest(API.DHS_SIGNUP, nameValuePairs);
            if (jsonArray == null) {
                return null;
            } else {
                try {
                    JsonResponse = jsonArray.getJSONObject(0);
                    signUpResponse = new SignUpResponse(JsonResponse.getString("status"));
                    status = signUpResponse.getStatus();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return status;
            }
        }

        @Override
        protected void onPostExecute(String status) {
            dialog.dismiss();
            if (status == null) {
                showAlertDialog();
            } else if (status.equals("1")) {
                clearField(etname);
                clearField(etpasswd);
                clearField(etmobNum);
                new AlertDialog.Builder(SignUp.this)
                        .setTitle("Success").setIcon(getResources().getDrawable(R.drawable.successicon))
                        .setMessage("\t\t\tRegistration Successful\n" +
                                "\t\t\tProceed to Login Page ")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                Intent openLoginActivity = new Intent(SignUp.this, LoginActivity.class);
                                startActivity(openLoginActivity);
                            }
                        }).create().show();
            } else if (status.equals("2")) {
                new AlertDialog.Builder(SignUp.this)
                        .setTitle("Error").setIcon(getResources().getDrawable(R.drawable.erroricon))
                        .setMessage("Account Already exist with" +
                                " \n Mobile Number: " + mobilenumber)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                Intent openLoginActivity = new Intent(SignUp.this, LoginActivity.class);
                                startActivity(openLoginActivity);
                            }
                        }).create().show();
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
