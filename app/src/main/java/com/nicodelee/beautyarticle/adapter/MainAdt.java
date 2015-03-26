package com.nicodelee.beautyarticle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nicodelee.beautyarticle.R;
import com.nicodelee.beautyarticle.mode.ActicleMod;
import com.nicodelee.beautyarticle.mode.SlidMod;
import com.nicodelee.beautyarticle.utils.DevicesUtil;
import com.nicodelee.utils.ListUtils;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainAdt extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<ActicleMod> mylist;

    public MainAdt(Context context, ArrayList<ActicleMod> list) {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mylist = list;

    }

    public void setAdtList(ArrayList<ActicleMod> list) {
        if (list == null) {
            list = new ArrayList<ActicleMod>();
        } else {
            this.mylist = list;
        }
        notifyDataSetChanged();
    }

    public int getCount() {
        return ListUtils.getSize(mylist);
//        return 20;
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
            convertView = mInflater.inflate(R.layout.item_main,parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.ivIcon
                .getLayoutParams();
        params.width = DevicesUtil.screenWidth;
        params.height = DevicesUtil.screenWidth;
        holder.ivIcon.setLayoutParams(params);

        ActicleMod mod = mylist.get(position);

        holder.tvName.setText(mod.title);
        holder.tvDesc.setText(mod.descriptions);

        return convertView;
    }

    public final class ViewHolder {
        @InjectView(R.id.main_title) TextView tvName;
        @InjectView(R.id.main_desc) TextView tvDesc;
        @InjectView(R.id.main_ic) ImageView ivIcon;
        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }

    }

}
