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

import butterknife.ButterKnife;
import butterknife.InjectView;

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

        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_left_menu,parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvName.setText(mylist.get(position).getName());
        holder.ivIcon.setImageDrawable(mylist.get(position).getIcon());
        return convertView;
    }

    public final class ViewHolder {
        @InjectView(R.id.txt_drawer_menu_item) TextView tvName;
        @InjectView(R.id.img_left_item_flag) ImageView ivIcon;
        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }

    }

}
