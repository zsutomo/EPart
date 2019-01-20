package com.astra.acan.epart;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends BaseAdapter {

    private Activity activity; //context
    private ArrayList<ModelItem> listItems; //data source of the list adapter

    public ListAdapter(Activity activity, ArrayList<ModelItem> items) {
        this.activity = activity;
        this.listItems = items;
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int position) {
        return listItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(activity).
                    inflate(R.layout.layout_row_view, parent, false);
        }

        ModelItem modelItem = (ModelItem) getItem(position);

        TextView tv_namaPartItem = (TextView) convertView.findViewById(R.id.textViewNamaPart);
        TextView tv_hargaPartItem = (TextView) convertView.findViewById(R.id.textViewHargaPart);
        TextView tv_nomorPartItem = (TextView) convertView.findViewById(R.id.textViewNomorPart);
        TextView tv_jumlahPartItem = (TextView) convertView.findViewById(R.id.textViewJumlahItem);
        TextView tv_st_Stok = (TextView) convertView.findViewById(R.id.textViewSt_stok);

        tv_nomorPartItem.setText("Nomor Part : " + modelItem.getNomorPart());
        tv_namaPartItem.setText("Nama Part : " + modelItem.getNamaPart());
        tv_hargaPartItem.setText("Harga Part : " + modelItem.getHargaPart());
        tv_jumlahPartItem.setText("Jumlah : " + modelItem.getJmlahItem());
        tv_st_Stok.setText("Status Stok : " + modelItem.getStatusStok());


        return convertView;
    }
}
