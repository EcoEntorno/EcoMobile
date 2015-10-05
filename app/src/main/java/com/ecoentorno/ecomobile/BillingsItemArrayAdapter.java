package com.ecoentorno.ecomobile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ecoentorno.ecomobile.model.Client;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cristhian on 04/10/2015.
 */
public class BillingsItemArrayAdapter extends ArrayAdapter<Client> {

    private List<Client> pendingsList = new ArrayList();

    static class ItemViewHolder {
        TextView tvName;
        TextView tvAddress;
        TextView tvTelephone;
    }

    public BillingsItemArrayAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    @Override
    public void add(Client object) {
        pendingsList.add(object);
        super.add(object);
    }

    @Override
    public int getCount() {
        return this.pendingsList.size();
    }

    @Override
    public Client getItem(int index) {
        return this.pendingsList.get(index);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ItemViewHolder viewHolder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.item_pending_client, parent, false);
            viewHolder = new ItemViewHolder();
            viewHolder.tvName = (TextView) row.findViewById(R.id.tvName);
            viewHolder.tvAddress = (TextView) row.findViewById(R.id.tvAddress);
            viewHolder.tvTelephone = (TextView) row.findViewById(R.id.tvTelephone);
            row.setTag(viewHolder);
        } else {
            viewHolder = (ItemViewHolder)row.getTag();
        }
        Client client = getItem(position);
        viewHolder.tvName.setText(client.getSocialName());
        viewHolder.tvAddress.setText(client.getAddress());
        viewHolder.tvTelephone.setText(client.getTelephone()+"");
        return row;
    }
}
