package in.crebit.app.WebView.crebit.servicespage;

import android.app.AlertDialog;
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
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.json.JSONException;
import org.json.JSONObject;

import in.crebit.app.WebView.Applicaton.GlobalVariable;
import in.crebit.app.WebView.R;
import in.crebit.app.WebView.apinames.API;
import in.crebit.app.WebView.crebit.servicespage.fragments.BankAccPay.BankAccPayContent;
import in.crebit.app.WebView.crebit.servicespage.fragments.Service;
import in.crebit.app.WebView.crebit.servicespage.fragments.balanceSummary.BalSummary;
import in.crebit.app.WebView.crebit.servicespage.fragments.margin.Margin;
import in.crebit.app.WebView.crebit.servicespage.fragments.transactionSummary.TransSummary;
import in.crebit.app.WebView.crebit.servicespage.fragments.updates.Updates;
import in.crebit.app.WebView.crebit.servicespage.menuitem.ChangePassword;
import in.crebit.app.WebView.crebit.servicespage.navDrawer.NavigationDrawerFragment;
import in.crebit.app.WebView.jsonparse.JSONParser;
import in.crebit.app.WebView.network.NetworkUtil;

public class
        service extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence mTitle;
    private String username, availableBalance, userType, userID;
    private SharedPreferences prefs;
    private final static String MY_PREFS = "mySharedPrefs";
    private JSONObject jsonResponse;
    private JSONParser jsonParser;
    private Tracker tracker;
    private SpannableString s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tracker = ((GlobalVariable) getApplication()).getTracker(GlobalVariable.TrackerName.APP_TRACKER);
        tracker.setScreenName("Service Page");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
        setContentView(R.layout.activity_service);
        prefs = getSharedPreferences(MY_PREFS, MODE_PRIVATE);
        username = prefs.getString("userName", "");
        userType = prefs.getString("uType", "");
        userID = prefs.getString("userId", "");
        new retrieveBalance().execute();
        if (availableBalance == null || availableBalance.equals("null"))
            availableBalance = "0.0";
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        // invalidateOptionsMenu();
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
    public void onBackPressed() {
        super.onBackPressed();
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        getSupportActionBar().setTitle("Crebit Wallet");
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                tracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Drawer Item")
                        .setAction("Open Service Page")
                        .setLabel("Service Page")
                        .build());
                fragment = new Service();
                getSupportFragmentManager()
                        .popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment)
                        .commit();

                break;
            case 1:
                tracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Drawer Item")
                        .setAction("Open Transaction Summary Page")
                        .setLabel("Service Page")
                        .build());
                fragment = new TransSummary();
                getSupportFragmentManager()
                        .popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment).addToBackStack(null)
                        .commit();
                break;
            case 2:
                tracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Drawer Item")
                        .setAction("Open Balance Summary Page")
                        .setLabel("Service Page")
                        .build());
                fragment = new BalSummary();
                getSupportFragmentManager()
                        .popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment).addToBackStack(null)
                        .commit();
                break;

            case 3:
                tracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Drawer Item")
                        .setAction("Open Bank Account Pay Page")
                        .setLabel("Service Page")
                        .build());
                fragment = new BankAccPayContent();
                getSupportFragmentManager()
                        .popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment).addToBackStack(null)
                        .commit();
                break;

            case 4:
                tracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Drawer Item")
                        .setAction("Open Updates Page")
                        .setLabel("Service Page")
                        .build());
                fragment = new Updates();
                getSupportFragmentManager()
                        .popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment).addToBackStack(null)
                        .commit();
                break;
            case 5:
                tracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Drawer Item")
                        .setAction("Open Margin Page")
                        .setLabel("Service Page")
                        .build());
                if (userType.equals("1")) {
                    fragment = new Margin();
                    getSupportFragmentManager()
                            .popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, fragment).addToBackStack(null)
                            .commit();
                }
                break;
            default:
                break;

        }
    }

    public void restoreActionBar() {
        invalidateOptionsMenu();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        s = new SpannableString("Crebit Wallet");
        s.setSpan(new in.crebit.app.WebView.customfont.TypefaceSpan(this, "coperplategothicbold.ttf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        actionBar.setTitle(s);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        menu.add(0, 0, 0, username.toUpperCase());
        menu.add(0, 0, 0, "Balance: Rs. " + availableBalance);
        getMenuInflater().inflate(R.menu.service, menu);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        menu.add(0, 0, 0, username.toUpperCase());
        menu.add(0, 0, 0, "Balance: Rs. " + availableBalance);
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.service, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mi_chgPasswd:
                tracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Button")
                        .setAction("Clicked on Change Password on Services Menu")
                        .setLabel("Change Password Menu Item")
                        .build());
                Intent openchgPassActivity = new Intent(service.this, ChangePassword.class);
                startActivity(openchgPassActivity);
                break;
            case R.id.mi_logout:
                tracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Button")
                        .setAction("Clicked on Logout on Services Menu")
                        .setLabel("Logout Menu Item")
                        .build());
                prefs.edit().clear().apply();
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private class retrieveBalance extends AsyncTask<String, String, String> {
        String status;


        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... strings) {
            jsonParser = new JSONParser();
            String Response = jsonParser.getResponse(API.GET_BALANCE + userID);
            if (Response == null || Response.equals("error")) {
                return Response;
            } else {
                try {
                    jsonResponse = new JSONObject(Response);
                    String status = jsonResponse.getString("Status");
                    if (status.equals("1"))
                        availableBalance = jsonResponse.getString("AvailBal");
                    return availableBalance;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return Response;
            }
        }

        @Override
        protected void onPostExecute(String status) {
            if (status == null) {
                showAlertDialog();
            } else if (status.equals("error")) {
                showErrorDialog();
            } else {
                invalidateOptionsMenu();
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

}

