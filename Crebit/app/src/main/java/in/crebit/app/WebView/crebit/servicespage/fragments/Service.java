package in.crebit.app.WebView.crebit.servicespage.fragments;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import in.crebit.app.WebView.Applicaton.GlobalVariable;
import in.crebit.app.WebView.R;
import in.crebit.app.WebView.crebit.servicespage.activities.BroadBand;
import in.crebit.app.WebView.crebit.servicespage.activities.Datacard;
import in.crebit.app.WebView.crebit.servicespage.activities.Dth;
import in.crebit.app.WebView.crebit.servicespage.activities.Electricity;
import in.crebit.app.WebView.crebit.servicespage.activities.FundTransfer;
import in.crebit.app.WebView.crebit.servicespage.activities.GasBill;
import in.crebit.app.WebView.crebit.servicespage.activities.Insurance;
import in.crebit.app.WebView.crebit.servicespage.activities.PostPaid;
import in.crebit.app.WebView.crebit.servicespage.activities.PrePaid;

public class Service extends Fragment implements View.OnClickListener {
    ImageButton postPaid, prePaid, dataCard, dth, insurance, electricity, gasBill, broadBand, fundTransfer;
    TextView service, postpaid, prepaid, datacard, Dth, Insurance, Electricity, gasbill, broadband, fundtransfer;
    private Tracker tracker;

    public Service() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service_list, container, false);
        setHasOptionsMenu(true);
        tracker = ((GlobalVariable) getActivity().getApplication()).getTracker(GlobalVariable.TrackerName.APP_TRACKER);
        tracker.setScreenName("Services  Page");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
        initViews(view);
        return view;
    }

    public void initViews(View view) {
        postPaid = (ImageButton) view.findViewById(R.id.ib_postPaid);
        service = (TextView) view.findViewById(R.id.Service);
        Typeface fontbold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/coperplategothicbold.ttf");
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/coperplategothiclight.ttf");
        service.setTypeface(font);
        prePaid = (ImageButton) view.findViewById(R.id.ib_prePaid);
        dth = (ImageButton) view.findViewById(R.id.ib_dth);

        dataCard = (ImageButton) view.findViewById(R.id.ib_datacard);
        insurance = (ImageButton) view.findViewById(R.id.ib_insurance);
        electricity = (ImageButton) view.findViewById(R.id.ib_electricity);

        gasBill = (ImageButton) view.findViewById(R.id.ib_gasBill);
        broadBand = (ImageButton) view.findViewById(R.id.ib_broadband);
        fundTransfer = (ImageButton) view.findViewById(R.id.ib_fundTransfer);

        postpaid = (TextView) view.findViewById(R.id.tv_postPaid);
        postpaid.setTypeface(fontbold);
        prepaid = (TextView) view.findViewById(R.id.tv_prePaid);
        prepaid.setTypeface(fontbold);
        Dth = (TextView) view.findViewById(R.id.tv_dth);
        Dth.setTypeface(fontbold);
        datacard = (TextView) view.findViewById(R.id.tv_datacard);
        datacard.setTypeface(fontbold);
        Insurance = (TextView) view.findViewById(R.id.tv_insurance);
        Insurance.setTypeface(fontbold);
        Electricity = (TextView) view.findViewById(R.id.tv_electricity);
        Electricity.setTypeface(fontbold);
        gasbill = (TextView) view.findViewById(R.id.tv_gasBill);
        gasbill.setTypeface(fontbold);
        broadband = (TextView) view.findViewById(R.id.tv_broadband);
        broadband.setTypeface(fontbold);
        fundtransfer = (TextView) view.findViewById(R.id.tv_fundTransfer);
        fundtransfer.setTypeface(fontbold);

        postPaid.setOnClickListener(this);
        prePaid.setOnClickListener(this);
        dth.setOnClickListener(this);
        dataCard.setOnClickListener(this);
        insurance.setOnClickListener(this);
        electricity.setOnClickListener(this);
        gasBill.setOnClickListener(this);
        broadBand.setOnClickListener(this);
        fundTransfer.setOnClickListener(this);
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_postPaid:

                tracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Services Page items")
                        .setAction("Clicked on postpaid item on services Page")
                        .setLabel("postpaid image")
                        .build());

                Intent openPostPaidActivity = new Intent(getActivity(), PostPaid.class);
                startActivity(openPostPaidActivity);
                break;
            case R.id.ib_prePaid:

                tracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Services Page items")
                        .setAction("Clicked on prepaid item on services Page")
                        .setLabel("prepaid image")
                        .build());

                Intent openPrePaidActivity = new Intent(getActivity(), PrePaid.class);
                startActivity(openPrePaidActivity);
                break;
            case R.id.ib_dth:

                tracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Services Page items")
                        .setAction("Clicked on dth item on services Page")
                        .setLabel("dth image")
                        .build());

                Intent openDthActivity = new Intent(getActivity(), Dth.class);
                startActivity(openDthActivity);
                break;
            case R.id.ib_datacard:


                tracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Services Page items")
                        .setAction("Clicked on datacard item on services Page")
                        .setLabel("datacard image")
                        .build());

                Intent opendataCardActivity = new Intent(getActivity(), Datacard.class);
                startActivity(opendataCardActivity);
                break;
            case R.id.ib_insurance:


                tracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Services Page items")
                        .setAction("Clicked on insurance item on services Page")
                        .setLabel("insurance image")
                        .build());

                Intent openinsuranceActivity = new Intent(getActivity(), Insurance.class);
                startActivity(openinsuranceActivity);
                break;
            case R.id.ib_electricity:


                tracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Services Page items")
                        .setAction("Clicked on electricity item on services Page")
                        .setLabel("electricity image")
                        .build());

                Intent openElectricityActivity = new Intent(getActivity(), Electricity.class);
                startActivity(openElectricityActivity);
                break;
            case R.id.ib_gasBill:


                tracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Services Page items")
                        .setAction("Clicked on gasbill item on services Page")
                        .setLabel("gasbill image")
                        .build());

                Intent opengasBillActivity = new Intent(getActivity(), GasBill.class);
                startActivity(opengasBillActivity);
                break;
            case R.id.ib_broadband:


                tracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Services Page items")
                        .setAction("Clicked on broadband item on services Page")
                        .setLabel("broadband image")
                        .build());

                Intent openBroadBandActivity = new Intent(getActivity(), BroadBand.class);
                startActivity(openBroadBandActivity);
                break;
            case R.id.ib_fundTransfer:


                tracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Services Page items")
                        .setAction("Clicked on fundtransfer item on services Page")
                        .setLabel("fundtransfer image")
                        .build());

                Intent openFundTransferActivity = new Intent(getActivity(), FundTransfer.class);
                startActivity(openFundTransferActivity);
                break;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }
}
