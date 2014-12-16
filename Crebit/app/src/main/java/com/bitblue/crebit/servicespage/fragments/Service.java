package com.bitblue.crebit.servicespage.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bitblue.crebit.R;
import com.bitblue.crebit.servicespage.activities.BroadBand;
import com.bitblue.crebit.servicespage.activities.Datacard;
import com.bitblue.crebit.servicespage.activities.Dth;
import com.bitblue.crebit.servicespage.activities.Electricity;
import com.bitblue.crebit.servicespage.activities.FundTransfer;
import com.bitblue.crebit.servicespage.activities.GasBill;
import com.bitblue.crebit.servicespage.activities.Insurance;
import com.bitblue.crebit.servicespage.activities.PostPaid;
import com.bitblue.crebit.servicespage.activities.PrePaid;

public class Service extends Fragment implements View.OnClickListener {
    ImageView postPaid, prePaid, dataCard, dth, insurance, electricity, gasBill, broadBand, fundTransfer;
    TextView postpaid, prepaid, datacard, Dth, Insurance, Electricity, gasbill, broadband, fundtransfer;

    public Service() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service_list, container,
                false);
        initViews(view);
        return view;
    }

    public void initViews(View view) {
        postPaid = (ImageView) view.findViewById(R.id.ib_postPaid);
        prePaid = (ImageView) view.findViewById(R.id.ib_prePaid);
        dth = (ImageView) view.findViewById(R.id.ib_dth);

        dataCard = (ImageView) view.findViewById(R.id.ib_datacard);
        insurance = (ImageView) view.findViewById(R.id.ib_insurance);
        electricity = (ImageView) view.findViewById(R.id.ib_electricity);

        gasBill = (ImageView) view.findViewById(R.id.ib_gasBill);
        broadBand = (ImageView) view.findViewById(R.id.ib_broadband);
        fundTransfer = (ImageView) view.findViewById(R.id.ib_fundTransfer);

        postpaid = (TextView) view.findViewById(R.id.tv_postPaid);
        prepaid = (TextView) view.findViewById(R.id.tv_prePaid);
        Dth = (TextView) view.findViewById(R.id.tv_dth);

        datacard = (TextView) view.findViewById(R.id.tv_datacard);
        Insurance = (TextView) view.findViewById(R.id.tv_insurance);
        Electricity = (TextView) view.findViewById(R.id.tv_electricity);

        gasbill = (TextView) view.findViewById(R.id.tv_gasBill);
        broadband = (TextView) view.findViewById(R.id.tv_broadband);
        fundtransfer = (TextView) view.findViewById(R.id.tv_fundTransfer);

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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_postPaid:
                Intent openPostPaidActivity = new Intent(getActivity(), PostPaid.class);
                startActivity(openPostPaidActivity);
                break;
            case R.id.ib_prePaid:
                Intent openPrePaidActivity = new Intent(getActivity(), PrePaid.class);
                startActivity(openPrePaidActivity);
                break;
            case R.id.ib_dth:
                Intent openDthActivity = new Intent(getActivity(),Dth.class);
                startActivity(openDthActivity);
                break;
            case R.id.ib_datacard:
                Intent opendataCardActivity = new Intent(getActivity(), Datacard.class);
                startActivity(opendataCardActivity);
                break;
            case R.id.ib_insurance:
                Intent openinsuranceActivity = new Intent(getActivity(), Insurance.class);
                startActivity(openinsuranceActivity);
                break;
            case R.id.ib_electricity:
                Intent openElectricityActivity = new Intent(getActivity(), Electricity.class);
                startActivity(openElectricityActivity);
                break;
            case R.id.ib_gasBill:
                Intent opengasBillActivity = new Intent(getActivity(), GasBill.class);
                startActivity(opengasBillActivity);
                break;
            case R.id.ib_broadband:
                Intent openBroadBandActivity = new Intent(getActivity(), BroadBand.class);
                startActivity(openBroadBandActivity);
                break;
            case R.id.ib_fundTransfer:
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
