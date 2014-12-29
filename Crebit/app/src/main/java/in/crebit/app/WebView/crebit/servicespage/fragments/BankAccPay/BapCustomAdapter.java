package in.crebit.app.WebView.crebit.servicespage.fragments.BankAccPay;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import in.crebit.app.WebView.R;

public class BapCustomAdapter extends BaseAdapter {
    private static ArrayList<BankAccPayResult> bapResultArrayList;
    private LayoutInflater mInflater;

    public BapCustomAdapter(Context context, ArrayList<BankAccPayResult> bapResultArrayList) {
        if (context != null && bapResultArrayList != null) {
            this.bapResultArrayList = bapResultArrayList;
            mInflater = LayoutInflater.from(context);
        }
    }

    @Override
    public int getCount() {
        return bapResultArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return bapResultArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.bap_list_row, null);
            holder = new ViewHolder();
            holder.count = (TextView) convertView.findViewById(R.id.tv_bap_list_count);
            holder.name = (TextView) convertView.findViewById(R.id.tv_bap_list_name);
            holder.account = (TextView) convertView.findViewById(R.id.tv_bap_list_account);
            holder.ifsc = (TextView) convertView.findViewById(R.id.tv_bap_list_ifsc);
            holder.mobile = (TextView) convertView.findViewById(R.id.tv_bap_list_mobile);
            holder.amount = (TextView) convertView.findViewById(R.id.tv_bap_list_amount);
            holder.status = (TextView) convertView.findViewById(R.id.tv_bap_list_status);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.count.setText(bapResultArrayList.get(position).getCount());
        holder.name.setText(bapResultArrayList.get(position).getName());
        holder.account.setText(bapResultArrayList.get(position).getAccount());
        holder.ifsc.setText(bapResultArrayList.get(position).getiFSC());
        holder.mobile.setText(bapResultArrayList.get(position).getMobile());
        holder.amount.setText(bapResultArrayList.get(position).getAmount());
        holder.status.setText(bapResultArrayList.get(position).getStatus());
        return convertView;
    }

    static class ViewHolder {
        private TextView count, name, account, ifsc, mobile, amount, status;
    }
}
