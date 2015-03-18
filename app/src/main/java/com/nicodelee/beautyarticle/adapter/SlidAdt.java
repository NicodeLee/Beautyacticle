package com.nicodelee.beautyarticle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nicodelee.beautyarticle.R;
import com.nicodelee.beautyarticle.mode.SlidMod;

import java.util.ArrayList;

public class SlidAdt extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<SlidMod> mylist;

    public SlidAdt(Context context, ArrayList<SlidMod> list) {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mylist = list;

    }

    public int getCount() {
        return mylist.size();
    }

    public Object getItem(int arg0) {
        return arg0;
    }

    public long getItemId(int arg0) {
        return arg0;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {

            holder = new ViewHolder();

            convertView = mInflater.inflate(R.layout.item_left_menu, null);
            holder.tvName = (TextView) convertView
                    .findViewById(R.id.txt_drawer_menu_item);
            holder.ivIcon = (ImageView) convertView
                    .findViewById(R.id.img_left_item_flag);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvName.setText(mylist.get(position).getName());
        holder.ivIcon.setImageDrawable(mylist.get(position).getIcon());

        return convertView;
    }

    public final class ViewHolder {
        public TextView tvName;
        public ImageView ivIcon;

    }

}
