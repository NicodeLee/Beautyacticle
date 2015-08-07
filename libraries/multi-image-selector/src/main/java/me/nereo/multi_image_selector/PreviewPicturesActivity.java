package me.nereo.multi_image_selector;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class PreviewPicturesActivity extends Activity {
    ViewPager pager;
    MyPagerAdapter adapter;
    private ArrayList<String> picList = new ArrayList<>();
    private TextView tvCancel, tvSend;

    public static void preViewSingle(Activity activity, String url, int requestCode) {
        Intent intent = new Intent(activity, PreviewPicturesActivity.class);
        ArrayList<String> pic = new ArrayList<>();
        pic.add(url);
        intent.putExtra("pics", pic);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_pictures);
        pager = (ViewPager) findViewById(R.id.pager);
        tvCancel = (TextView) findViewById(R.id.tv_cancel);
        tvSend = (TextView) findViewById(R.id.tv_send);

        picList = getIntent().getStringArrayListExtra("pics");
        if (picList.size() != 0) {
            adapter = new MyPagerAdapter();
            pager.setAdapter(adapter);
        }

        Log.i("PreView", picList.toString());

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("pics", picList);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return picList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view.equals(o);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = View.inflate(PreviewPicturesActivity.this, R.layout.item_image, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.iv_pic);

            Picasso.with(PreviewPicturesActivity.this).load(new File(picList.get(position)))
//                    .placeholder(R.drawable.default_error)
                    .centerInside()
                    .resize(800, 1500)
                            //.error(R.drawable.default_error)
                    .into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {
//                            attacher = new PhotoViewAttacher(bgPic);
                        }

                        @Override
                        public void onError() {
                        }
                    });
            container.addView(view, 0);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            System.gc();
        }
    }

}
