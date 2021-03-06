package in.crebit.app.WebView.crebit.loginpage;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.microsoft.windowsazure.messaging.NotificationHub;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.notifications.NotificationsManager;

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
import in.crebit.app.WebView.crebit.servicespage.service;
import in.crebit.app.WebView.jsonparse.JSONParser;
import in.crebit.app.WebView.network.NetworkUtil;
import in.crebit.app.WebView.nullcheck.Check;
import in.crebit.app.WebView.requestparam.LoginParams;
import in.crebit.app.WebView.response.LoginResponse;

public class LoginActivity extends ActionBarActivity implements View.OnClickListener {
    private JSONParser jsonParser = new JSONParser();
    private JSONArray jsonArray = null;
    private EditText mNumber, passwd;
    private Button login, forgotPass, signUp;
    private String logoutmessage;
    private String mobile, pass;
    private String Version = "1.0";
    private GlobalVariable globalVariable;
    private String userName, availableBalance, userId, userKey, uType;
    private boolean isActive;
    private Tracker tracker;
    private LoginParams loginParams;
    private JSONObject JsonResponse;
    private LoginResponse loginResponse;
    private List<NameValuePair> nameValuePairs;
    private final static String MY_PREFS = "mySharedPrefs";

    //Required for Azure cloud
    private String SENDER_ID = "1074159853606";
    private GoogleCloudMessaging gcm;
    private NotificationHub hub;
    public static MobileServiceClient mClient;

    //GCM Variables
    /*  private Context context;
    private String regId;
    public static final String REG_ID = "regId";
    private static final String APP_VERSION = "appVersion";
    private static final String TAG = "Register Activity";*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

     /*//GCM SETUP
        context = getApplicationContext();
        regId = registerGCM();*/

        gcm = GoogleCloudMessaging.getInstance(this);

        //Google Analytics Setup
        tracker = ((GlobalVariable) getApplication()).getTracker(GlobalVariable.TrackerName.APP_TRACKER);
        tracker.setScreenName("Login Page");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());


        //Azure Setup
        NotificationsManager.handleNotifications(this, SENDER_ID, MyHandler.class);
        String connectionString = "Endpoint=sb://crebit.servicebus.windows.net/;SharedAccessKeyName=DefaultFullSharedAccessSignature;SharedAccessKey=ErxHM1NfR4IfR0SJiWbARUb5w4ZfBVNE9eL5jPAIeWg=";
        hub = new NotificationHub("crebithub", connectionString, this);
        registerWithNotificationHubs();

        setContentView(R.layout.activity_login);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        LayoutInflater inflator = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.centre_actionbar_title, null);
        TextView tv = (TextView) v.findViewById(R.id.centre_title);
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/coperplategothicbold.ttf");
        tv.setTypeface(tf);
        getSupportActionBar().setCustomView(v);

        logoutmessage = getIntent().getStringExtra("logout");
        initViews();
        if (!isNetworkAvailable())
            showAlertDialog();
    }

    @SuppressWarnings("unchecked")
    private void registerWithNotificationHubs() {
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object... params) {
                try {
                    String regid = gcm.register(SENDER_ID);
                    hub.register(regid);
                    Log.e("Device Registered", regid);
                } catch (Exception e) {
                    return e;
                }
                return null;
            }
        }.execute(null, null, null);
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
    protected void onRestart() {
        super.onRestart();
        if (!isNetworkAvailable())
            showAlertDialog();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void initViews() {

        globalVariable = (GlobalVariable) getApplicationContext();
        mNumber = (EditText) findViewById(R.id.et_mobileNumber);

        passwd = (EditText) findViewById(R.id.et_password);

        login = (Button) findViewById(R.id.b_login);
        forgotPass = (Button) findViewById(R.id.b_forgot_pass);
        signUp = (Button) findViewById(R.id.b_signUp);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/coperplategothicbold.ttf");
        login.setOnClickListener(this);
        forgotPass.setOnClickListener(this);
        signUp.setOnClickListener(this);
        login.setTypeface(font);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.b_login:

                tracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Button")
                        .setAction("Clicked on Login Button on Login Page")
                        .setLabel("Login Button")
                        .build());

                mobile = mNumber.getText().toString();
                pass = passwd.getText().toString();
                if (Check.ifNumberInCorrect(mobile)) {
                    mNumber.setHint(" Mobile Number Required");
                    mNumber.setHintTextColor(getResources().getColor(R.color.red));
                    break;
                }
                if (Check.ifNull(pass)) {
                    passwd.setHint(" Password Required");
                    passwd.setHintTextColor(getResources().getColor(R.color.red));
                    break;
                }
                new retrieveData().execute();
                break;
            case R.id.b_forgot_pass:
                tracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Button")
                        .setAction("Clicked on Forgot Pass Button on login Page")
                        .setLabel("Forgot Password Button")
                        .build());
                Intent openForgotPass = new Intent(LoginActivity.this, ForgotPass.class);
                startActivity(openForgotPass);
                break;
            case R.id.b_signUp:
                tracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Button")
                        .setAction("Clicked on Signup Button on login Page")
                        .setLabel("SignUp Button")
                        .build());
                Intent opensignUp = new Intent(LoginActivity.this, SignUp.class);
                startActivity(opensignUp);
                break;
        }
    }

    private class retrieveData extends AsyncTask<String, String, String> {
        ProgressDialog dialog = new ProgressDialog(LoginActivity.this);

        @Override
        protected void onPreExecute() {
            dialog.setTitle("Please Wait");
            dialog.setMessage("Signing in...");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            loginParams = new LoginParams(mobile, pass, Version);
            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("Mobile", mobile));
            nameValuePairs.add(new BasicNameValuePair("Pass", pass));
            nameValuePairs.add(new BasicNameValuePair("Version", Version));
            String Response = jsonParser.makeHttpPostRequest(API.DHS_LOGIN, nameValuePairs);
            if (Response==null||Response.equals("error")) {
                return Response;
            } else {
                try {
                    jsonArray = new JSONArray(Response);
                    JsonResponse = jsonArray.getJSONObject(0);
                    loginResponse = new LoginResponse(JsonResponse.getBoolean("isSupported"),
                            JsonResponse.getBoolean("isActive"),
                            JsonResponse.getString("userId"),
                            JsonResponse.getString("availableBalance"),
                            JsonResponse.getBoolean("isUpdated"),
                            JsonResponse.getBoolean("isDataUpdated"),
                            JsonResponse.getString("name"),
                            JsonResponse.getString("userKey"),
                            JsonResponse.getString("uType"));
                    userName = loginResponse.getName();
                    availableBalance = loginResponse.getAvailableBalance();
                    userId = loginResponse.getUserID();
                    userKey = loginResponse.getUserKey();
                    isActive = loginResponse.isActive();
                    uType = loginResponse.getuType();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return userName;
            }
        }

        @Override
        protected void onPostExecute(String name) {
            dialog.dismiss();
            if (name == null) {
                showAlertDialog();
            } else if (name.equals("error")) {
                showErrorDialog();
            } else if (name.equals("null")) {
                new AlertDialog.Builder(LoginActivity.this)
                        .setTitle("Error").setIcon(getResources().getDrawable(R.drawable.erroricon))
                        .setMessage("Invalid Credentials")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create().show();
            } else {
                SharedPreferences.Editor prefs = getSharedPreferences(MY_PREFS, MODE_PRIVATE).edit();  //to pass data between activities
                prefs.putString("userId", userId);
                prefs.putString("userKey", userKey);
                prefs.putString("availableBalance", availableBalance);
                prefs.putString("userName", userName);
                prefs.putBoolean("isActive", isActive);
                prefs.putString("uType", uType);
                prefs.commit();
                globalVariable.setuType(uType);
                globalVariable.setUserID(userId);
                globalVariable.setUserKey(userKey);

                clearField(mNumber);
                clearField(passwd);
                Intent openService = new Intent(LoginActivity.this, service.class);
                startActivity(openService);
            }
        }
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