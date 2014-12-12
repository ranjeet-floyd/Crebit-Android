package com.bitblue.crebit.servicespage.fragments.margin;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.bitblue.crebit.R;
import com.bitblue.crebit.servicespage.listAdapter.OpMar;
import com.bitblue.crebit.servicespage.listAdapter.OpMarCustomAdapter;

import java.util.ArrayList;

public class Margin extends Fragment {
    public Margin() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_margin, container, false);
        ArrayList<OpMar> opMarArrayList = OperatorMarginList.getOpMarList();
        ListView listView = (ListView) view.findViewById(R.id.operator_margin_list);
        listView.setAdapter(new OpMarCustomAdapter(getActivity(), opMarArrayList));
        return view;
    }


}
