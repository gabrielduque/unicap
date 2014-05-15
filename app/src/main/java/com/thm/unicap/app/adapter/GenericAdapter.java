package com.thm.unicap.app.adapter;

import android.content.Context;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class GenericAdapter<T> extends BaseAdapter {

    protected List<T> items;
    protected Context context;

    protected GenericAdapter(List<T> items, Context context) {
        this.items = items;
        this.context = context;
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

}
