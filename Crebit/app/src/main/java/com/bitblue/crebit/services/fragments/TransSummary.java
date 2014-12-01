package com.bitblue.crebit.services.fragments;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.bitblue.crebit.DatePickerFragment;
import com.bitblue.crebit.R;

import java.util.Calendar;

public class TransSummary extends Fragment implements View.OnClickListener {
    TextView from, to, fromDate, toDate;
    Button from_Date, to_Date;

    public TransSummary() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trans_summary, container,
                false);
        initViews(view);
        return view;
    }

    public void initViews(View view) {
        from_Date = (Button) view.findViewById(R.id.b_from);
        to_Date = (Button) view.findViewById(R.id.b_to);
        from = (TextView) view.findViewById(R.id.tv_ts_from);
        to = (TextView) view.findViewById(R.id.tv_ts_to);
        fromDate = (TextView) view.findViewById(R.id.tv_fromDate);
        toDate = (TextView) view.findViewById(R.id.tv_toDate);

        from_Date.setOnClickListener(this);
        to_Date.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        showDatePicker();
    }

    private void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        /**
         * Set Up Current Date Into dialog
         */
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        date.setCallBack(ondate);
        date.show(getFragmentManager(), "Date Picker");
    }

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
        }
    };

}

