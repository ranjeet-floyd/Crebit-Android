package in.crebit.app.WebView.crebit.servicespage.fragments.BankAccPay;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import in.crebit.app.WebView.R;

public class BankAccPayContent extends Fragment {
    private FragmentTabHost mTabHost;
    private TextView bankAccPay;

    public BankAccPayContent() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bank_acc_pay_content, container, false);
        mTabHost = (FragmentTabHost) view.findViewById(android.R.id.tabhost);
        mTabHost.setup(getActivity(), getChildFragmentManager(), android.R.id.tabcontent);

        mTabHost.addTab(mTabHost.newTabSpec("Send Pay Request").setIndicator("Send Pay Request"),
                BankAccPay.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("Transaction History").setIndicator("Transaction History"),
                Last5Trans.class, null);
        bankAccPay = (TextView) view.findViewById(R.id.tv_bankAccPay_content);
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/coperplategothiclight.ttf");
        bankAccPay.setTypeface(font);
        return view;
    }

}
