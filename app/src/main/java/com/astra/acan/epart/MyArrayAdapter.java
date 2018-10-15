package com.astra.acan.epart;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

class MyArrayAdapter extends ArrayAdapter<ModelDataPart> {

    private final Context context;
    private final LayoutInflater mInflater;
    private final List<ModelDataPart> modelList;

    public MyArrayAdapter(Context context, List<ModelDataPart> objects) {
        super(context, 0, objects);
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        modelList = objects;
    }

    @Override
    public ModelDataPart getItem(int position) {
        return modelList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        if (convertView == null) {
            View view = mInflater.inflate(R.layout.layout_row_view, parent, false);
            vh = ViewHolder.create((RelativeLayout) view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        ModelDataPart item = getItem(position);

        vh.textViewNomorPart.setText(item.getNomorPart());
        vh.textViewNamaPart.setText(item.getNamaPart());
        vh.textViewHargaPart.setText(item.getHargaPart());


        return vh.rootView;
    }

    private static class ViewHolder {
        public final RelativeLayout rootView;

        public final TextView textViewNomorPart;
        public final TextView textViewNamaPart;
        public final TextView textViewHargaPart;

        private ViewHolder(RelativeLayout rootView, TextView textViewNomorPart, TextView textViewNamaPart, TextView textViewHargaPart) {
            this.rootView = rootView;
            this.textViewNomorPart = textViewNomorPart;
            this.textViewNamaPart = textViewNamaPart;
            this.textViewHargaPart = textViewHargaPart;
        }

        public static ViewHolder create(RelativeLayout rootView) {
            TextView textViewNomorPart = (TextView) rootView.findViewById(R.id.textViewNomorPart);
            TextView textViewNamaPart = (TextView) rootView.findViewById(R.id.textViewNamaPart);
            TextView textViewHargaPart = (TextView) rootView.findViewById(R.id.textViewHargaPart);

            return new ViewHolder(rootView, textViewNomorPart, textViewNamaPart, textViewHargaPart);
        }
    }
}
