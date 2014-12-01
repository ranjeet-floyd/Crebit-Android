package com.bitblue.crebit.services.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bitblue.crebit.R;

public class Service extends Fragment implements View.OnClickListener {
    ImageButton postPaid, prePaid, dataCard, dth, insurance, electricity, gasBill, broadBand, fundTransfer;
    TextView postpaid, prepaid, datacard, Dth, Insurance, Electricity, gasbill, broadband, fundtransfer;
    public Service(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service_list, container,
                false);
        initViews(view);
        return view;
    }

    public void initViews(View view) {
        postPaid = (ImageButton) view.findViewById(R.id.ib_postPaid);
        prePaid = (ImageButton) view.findViewById(R.id.ib_prePaid);
        dth = (ImageButton) view.findViewById(R.id.ib_dth);

        dataCard = (ImageButton) view.findViewById(R.id.ib_dataCard);
        insurance = (ImageButton) view.findViewById(R.id.ib_insurance);
        electricity = (ImageButton) view.findViewById(R.id.ib_electricity);

        gasBill = (ImageButton) view.findViewById(R.id.ib_gasBill);
        broadBand = (ImageButton) view.findViewById(R.id.ib_broadBand);
        fundTransfer = (ImageButton) view.findViewById(R.id.ib_FundTransfer);

        postpaid = (TextView) view.findViewById(R.id.tv_postPaid);
        prepaid = (TextView) view.findViewById(R.id.tv_prePaid);
        Dth = (TextView) view.findViewById(R.id.tv_dth);

        datacard = (TextView) view.findViewById(R.id.tv_dataCard);
        Insurance = (TextView) view.findViewById(R.id.tv_insurance);
        Electricity = (TextView) view.findViewById(R.id.tv_electricity);

        gasbill = (TextView) view.findViewById(R.id.tv_gasBill);
        broadband = (TextView) view.findViewById(R.id.tv_broadBand);
        fundtransfer = (TextView) view.findViewById(R.id.tv_fundTransfer);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_postPaid:
                break;
            case R.id.ib_prePaid:
                break;
            case R.id.ib_dth:
                break;
            case R.id.ib_dataCard:
                break;
            case R.id.ib_insurance:
                break;
            case R.id.ib_electricity:
                break;
            case R.id.ib_gasBill:
                break;
            case R.id.ib_broadBand:
                break;
            case R.id.ib_FundTransfer:
                break;
        }
    }
}
