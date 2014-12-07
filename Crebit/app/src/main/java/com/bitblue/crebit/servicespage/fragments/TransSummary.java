package com.bitblue.crebit.servicespage.fragments;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import com.bitblue.crebit.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TransSummary extends Fragment implements View.OnClickListener {
    Button from_Date, to_Date;
    private int cur = 0;

    private static final int FROM_DATE = 1;
    private static final int TO_DATE = 2;

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
        from_Date = (Button) view.findViewById(R.id.b_ts_from);
        to_Date = (Button) view.findViewById(R.id.b_ts_to);
        from_Date.setOnClickListener(this);
        to_Date.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.b_ts_from:
                cur = FROM_DATE;
                showDatePicker();
                from_Date.setText("");
                break;
            case R.id.b_ts_to:
                cur = TO_DATE;
                showDatePicker();
                to_Date.setText("");
                break;
        }

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
            Date from = null;
            Date to = null;
            if (cur == FROM_DATE)
                from_Date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
            if (cur == TO_DATE) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-mm-yyyy");
                try {
                    from = sdf.parse(from_Date.getText().toString());
                    to = sdf.parse(to_Date.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (from.before(to))
                    to_Date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                else {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Error")
                            .setMessage("To-date cannot be smaller than from-Date")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).create().show();
                }
            }
        }
    };

}

