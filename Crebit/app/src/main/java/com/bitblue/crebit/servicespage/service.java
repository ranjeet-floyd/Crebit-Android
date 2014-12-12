package com.bitblue.crebit.servicespage;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bitblue.crebit.R;
import com.bitblue.crebit.loginpage.LoginActivity;
import com.bitblue.crebit.servicespage.fragments.balanceSummary.BalSummary;
import com.bitblue.crebit.servicespage.fragments.BankAccPay;
import com.bitblue.crebit.servicespage.fragments.margin.Margin;
import com.bitblue.crebit.servicespage.fragments.Service;
import com.bitblue.crebit.servicespage.fragments.transactionSummary.TransSummary;
import com.bitblue.crebit.servicespage.fragments.Updates;
import com.bitblue.crebit.servicespage.menuitem.ChangePassword;
import com.bitblue.crebit.servicespage.navDrawer.NavigationDrawerFragment;

public class service extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence mTitle;
    private String username, availableBalance;
    private SharedPreferences prefs;
    private final static String MY_PREFS = "mySharedPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        prefs = getSharedPreferences(MY_PREFS, MODE_PRIVATE);
        username = prefs.getString("userName", "");
        availableBalance = prefs.getString("availableBalance", "null");
        if (availableBalance.equals("null")) {
            availableBalance = "0";
        }
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:

                fragment = new Service();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment)
                        .addToBackStack(null)
                        .commit();

                break;
            case 1:
                fragment = new TransSummary();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment)
                        .addToBackStack(null)
                        .commit();
                break;
            case 2:
                fragment = new BalSummary();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment)
                        .addToBackStack(null)
                        .commit();
                break;

            case 3:
                fragment = new BankAccPay();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment)
                        .addToBackStack(null)
                        .commit();
                break;

            case 4:
                fragment = new Updates();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment)
                        .addToBackStack(null)
                        .commit();
                break;
            case 5:
                fragment = new Margin();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment)
                        .addToBackStack(null)
                        .commit();
                break;

            default:
                break;

        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            this.finish();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.services);
                break;
            case 2:
                mTitle = getString(R.string.transactionSummary);
                break;
            case 3:
                mTitle = getString(R.string.balanceSummary);
                break;
            case 4:
                mTitle = getString(R.string.bankAccountPay);
                break;
            case 5:
                mTitle = getString(R.string.updates);
                break;
            case 6:
                mTitle = getString(R.string.margin);
                break;

        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 0, 0, "WELCOME-> " + username.toUpperCase());
        menu.add(0, 0, 0, "Balance: Rs. " + Double.parseDouble(availableBalance));
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
                Intent openchgPassActivity = new Intent(service.this, ChangePassword.class);
                startActivity(openchgPassActivity);
                break;
            case R.id.mi_logout:
                Intent openLoginActivity = new Intent(service.this, LoginActivity.class);
                openLoginActivity.putExtra("logout", "You have been logged out \nLogin to Continue");
                startActivity(openLoginActivity);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_services, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((service) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
