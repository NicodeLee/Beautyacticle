package com.nicodelee.beautyarticle.viewhelper;

import android.content.Context;

import com.nicodelee.beautyarticle.R;
import com.nicodelee.beautyarticle.mode.SlidMod;

import java.util.ArrayList;

/**
 * Created by alee on 2015/2/27.
 */
public class SlidData {
    public static ArrayList<SlidMod> SetData(Context context){
        ArrayList<SlidMod> mods = new ArrayList<SlidMod>();
        for (int i=0;i<3;i++){
            SlidMod slidMod = new SlidMod();
            switch (i){
                case 0:
                    slidMod.setIcon(context.getResources().getDrawable(R.drawable.ico_hot));
                    slidMod.setName("首页");
                    break;
                case 1:
                    slidMod.setIcon(context.getResources().getDrawable(R.drawable.ico_favorite));
                    slidMod.setName("喜欢");
                    break;
                case 2:
                    slidMod.setIcon(context.getResources().getDrawable(R.drawable.ico_setting));
                    slidMod.setName("设置");
                    break;
            }

            mods.add(slidMod);
        }
        return mods;
    }
}
