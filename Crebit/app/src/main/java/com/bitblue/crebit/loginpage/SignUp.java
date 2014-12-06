package com.bitblue.crebit.loginpage;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bitblue.apinames.API;
import com.bitblue.crebit.R;
import com.bitblue.jsonparse.JSONParser;
import com.bitblue.nullcheck.Check;
import com.bitblue.requestparam.SignUpParams;
import com.bitblue.response.SignUpResponse;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Resources resource = getResources();
        items = resource.getStringArray(R.array.accountType);
        initViews();
    }

    public void initViews() {
        tvaccType = (TextView) findViewById(R.id.tv_accType);
        tvname = (TextView) findViewById(R.id.tv_name);
        tvmobNum = (TextView) findViewById(R.id.tv_mobNum);
        tvpasswd = (TextView) findViewById(R.id.tv_passwd);
        etname = (EditText) findViewById(R.id.et_name);
        etpasswd = (EditText) findViewById(R.id.et_passwd);
        etmobNum = (EditText) findViewById(R.id.et_mobNum);
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
                if (baccType.getText().equals("--Select--")) {
                    tvaccType.setTextColor(getResources().getColor(R.color.red));
                }
                if (Check.ifNull(name)) {
                    etname.setHintTextColor(getResources().getColor(R.color.red));
                }
                if (Check.ifNull(password)) {
                    etpasswd.setHintTextColor(getResources().getColor(R.color.red));
                }
                if (Check.ifNumberInCorrect(mobilenumber)) {
                    etmobNum.setText("");
                    etmobNum.setHint(" Enter correct number");
                    etmobNum.setHintTextColor(getResources().getColor(R.color.red));
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
            nameValuePairs.add(new BasicNameValuePair("UserType", String.valueOf(userType)));
            nameValuePairs.add(new BasicNameValuePair("Name", name));
            nameValuePairs.add(new BasicNameValuePair("Pass", password));
            nameValuePairs.add(new BasicNameValuePair("Mobile", mobilenumber));
            jsonArray = jsonParser.makeHttpPostRequest(API.DHS_SIGNUP, nameValuePairs);
            try {
                JsonResponse = jsonArray.getJSONObject(0);
                signUpResponse = new SignUpResponse(JsonResponse.getString("status"));
                status = signUpResponse.getStatus();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return status;
        }

        @Override
        protected void onPostExecute(String status) {
            dialog.dismiss();
            checkstatus();
        }
    }

    private void checkstatus() {
        if (status.equals("2")) {
            new AlertDialog.Builder(SignUp.this)
                    .setTitle("Success")
                    .setMessage("\t\t\tRegistration Successful\n" +
                            "\t\t\tProceed to login page ")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).create().show();
        }
    }
}
