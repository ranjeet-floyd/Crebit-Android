package in.crebit.app.WebView.crebit.servicespage.activities;

import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import in.crebit.app.WebView.R;
import in.crebit.app.WebView.crebit.servicespage.activities.Electriciti.MSEB;
import in.crebit.app.WebView.crebit.servicespage.activities.Electriciti.RelianceMum;
import in.crebit.app.WebView.crebit.servicespage.activities.Electriciti.TorrentPower;


public class Electricity extends ActionBarActivity {
    private Bundle savedInstanceState;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState=savedInstanceState;
        setContentView(R.layout.activity_electricity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        initViews();
    }

    private void initViews() {
        TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
        LocalActivityManager mLocalActivityManager = new LocalActivityManager(this, false);
        mLocalActivityManager.dispatchCreate(savedInstanceState);
        tabHost.setup(mLocalActivityManager);
        TabSpec mseb = tabHost.newTabSpec("MSEB");
        mseb.setIndicator("MSEB");
        mseb.setContent(new Intent(this, MSEB.class));

        TabSpec reliance = tabHost.newTabSpec("Reliance");
        reliance.setIndicator("Reliance");
        reliance.setContent(new Intent(this, RelianceMum.class));

        TabSpec torrentPower = tabHost.newTabSpec("Torrent Power");
        torrentPower.setIndicator("Torrent Power");
        torrentPower.setContent(new Intent(this, TorrentPower.class));

        /** Add the tabs  to the TabHost to display. */
        tabHost.addTab(mseb);
        tabHost.addTab(reliance);
        tabHost.addTab(torrentPower);
    }
}
