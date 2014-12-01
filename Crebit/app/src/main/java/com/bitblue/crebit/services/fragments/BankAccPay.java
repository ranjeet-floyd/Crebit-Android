package com.bitblue.crebit.services.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bitblue.crebit.R;

public class BankAccPay extends Fragment {


    public BankAccPay() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_bank_acc_pay, container, false);
        return view;
    }


}
