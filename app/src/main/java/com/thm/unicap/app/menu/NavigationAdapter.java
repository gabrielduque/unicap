package com.thm.unicap.app.menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.devspark.robototextview.widget.RobotoTextView;
import com.thm.unicap.app.R;

import java.util.List;

public class NavigationAdapter extends ArrayAdapter<NavigationItem> {

    public NavigationAdapter(Context context, int resource, List<NavigationItem> objects) {
        super(context, resource, objects);
    }

    @Override
    public int getViewTypeCount() {
        return NavigationItem.Type.values().length;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getType().ordinal();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Keeps reference to avoid future findViewById()
        MenuItemViewHolder viewHolder;
        int type = getItemViewType(position);

        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if(type == NavigationItem.Type.EXTRA.ordinal()) {
                convertView = li.inflate(R.layout.navigation_item_small, parent, false);
            } else {
                convertView = li.inflate(R.layout.navigation_item_big, parent, false);
            }

            viewHolder = new MenuItemViewHolder();
            viewHolder.icon = (ImageView) convertView.findViewById(R.id.nav_icon);
            viewHolder.title = (RobotoTextView) convertView.findViewById(R.id.nav_title);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MenuItemViewHolder) convertView.getTag();
        }

        NavigationItem item = getItem(position);
        if (item != null) {
            viewHolder.icon.setImageResource(item.getImageResource());
            viewHolder.title.setText(item.getTitle());
        }
        return convertView;
    }

    static class MenuItemViewHolder {
        ImageView icon;
        RobotoTextView title;
    }

}
