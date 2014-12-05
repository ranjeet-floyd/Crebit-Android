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
    private JSONArray jsonArray = null;
    private TextView tvaccType, tvname, tvmobNum, tvpasswd;
    private EditText etname, etmobNum, etpasswd;
    private Button bSignUpSubmit, baccType;
    private String uType, name, mobilenumber, password;
    private int userType, status;
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
        etname.setOnClickListener(this);
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, items);

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.et_name:
                if(etname==null)
                    etname.setCursorVisible(true);
                break;
            case R.id.b_accType:
                new AlertDialog.Builder(this)
                        .setTitle("Select Account")
                        .setAdapter(adapter, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int position) {
                                uType = items[position];
                                baccType.setText(items[position]);
                                dialog.dismiss();
                            }
                        }).create().show();
                break;
            case R.id.b_signUpSubmit:
                name = etname.getText().toString();
                mobilenumber = etmobNum.getText().toString();
                password = etpasswd.getText().toString();
                if (Check.ifNull(name)) {
                    etpasswd.setHintTextColor(getResources().getColor(R.color.red));
                    break;
                }
                if (Check.ifNull(mobilenumber)) {
                    etmobNum.setHintTextColor(getResources().getColor(R.color.red));
                    break;
                }
                if (Check.ifNull(password)) {
                    etpasswd.setHintTextColor(getResources().getColor(R.color.red));
                    break;
                }
                if (Check.ifNumberInCorrect(mobilenumber)) {
                    etmobNum.setText("Enter correct number");
                    etmobNum.setTextColor(getResources().getColor(R.color.red));
                    break;
                }
        }
    }

    private class retrieveData extends AsyncTask<String, String, String> {
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
            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("UserType", uType));
            nameValuePairs.add(new BasicNameValuePair("Name", name));
            nameValuePairs.add(new BasicNameValuePair("Pass", password));
            nameValuePairs.add(new BasicNameValuePair("Mobile", mobilenumber));
            jsonArray = jsonParser.makeHttpPostRequest(API.DHS_SIGNUP, nameValuePairs);
            try {
                JsonResponse = jsonArray.getJSONObject(0);
                signUpResponse = new SignUpResponse(JsonResponse.getString("Status"));
                status = signUpResponse.getStatus();
                if (status == 1) {
                } else if (status == 2) {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String name) {
            dialog.dismiss();
        }
    }
}
