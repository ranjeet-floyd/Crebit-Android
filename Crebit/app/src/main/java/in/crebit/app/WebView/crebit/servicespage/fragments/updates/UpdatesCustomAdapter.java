package in.crebit.app.WebView.crebit.servicespage.fragments.updates;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import in.crebit.app.WebView.R;

public class UpdatesCustomAdapter extends BaseAdapter {
    private static ArrayList<UpdateResult> updateResultArrayList;
    private LayoutInflater mInflater;

    public UpdatesCustomAdapter(Context context, ArrayList<UpdateResult> updateResultArrayList1) {
        if (updateResultArrayList1 != null && context != null) {
            mInflater = LayoutInflater.from(context);
            updateResultArrayList = updateResultArrayList1;
        }

    }

    @Override
    public int getCount() {
        return updateResultArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return updateResultArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.update_list_row, null);
            holder = new ViewHolder();
            holder.count = (TextView) convertView.findViewById(R.id.tv_update_list_count);
            holder.text = (TextView) convertView.findViewById(R.id.tv_update_list_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.count.setText(updateResultArrayList.get(position).getCount());
        holder.text.setText(updateResultArrayList.get(position).getText());
        return convertView;
    }

    static class ViewHolder {
        TextView count, text;
    }
}
