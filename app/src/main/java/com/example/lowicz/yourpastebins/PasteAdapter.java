package com.example.lowicz.yourpastebins;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by s391382 on 2015-05-09.
 */
public class PasteAdapter extends BaseAdapter {
    private final Context context;
    List<Paste> items;

    public PasteAdapter(Context context, List<Paste> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView;

        if (convertView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.one_paste, parent, false);
        } else {
            itemView = convertView;
        }

        bindDataToView(items.get(position), itemView);
        return itemView;
    }

    private void bindDataToView(Paste item, View view) {
        this.notifyDataSetChanged();
        TextView url = (TextView) view.findViewById(R.id.url);
        if (url != null) {
            url.setText(item.getUrl());
        }
        TextView paste_text = (TextView) view.findViewById(R.id.paste_text3);
        if (paste_text != null) {
            paste_text.setText(item.getPaste_text());
        }
    }

}
