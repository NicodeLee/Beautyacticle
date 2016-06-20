package com.nicodelee.beautyarticle.ui.camara;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.nicodelee.beautyarticle.base.R;
import com.nicodelee.beautyarticle.utils.AndroidUtils;
import com.nicodelee.beautyarticle.utils.TimeUtils;
import com.nicodelee.utils.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import it.sephiroth.android.library.widget.AdapterView;
import it.sephiroth.android.library.widget.HListView;
import java.util.Date;
import java.util.List;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageView;
import org.greenrobot.eventbus.EventBus;

/**
 * Created by NocodeLee on 15/11/24.
 * Email：lirizhilirizhi@163.com
 * 照片编辑
 */
public class PhotoProcessActivity extends BaseAct implements View.OnClickListener {

  private GPUImageView gpuimage;
  private ImageView left;
  private ImageView right;
  private HListView bottomToolBar;
  private TextView title;

  //预览的小图
  private Bitmap smallImageBackgroud;
  protected static PhotoProcessActivity photoProcessActivity;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_image_process);
    initView();
  }

  @Override public void onClick(View view) {
    int i = view.getId();
    if (i == R.id.left) {
      finish();
    } else if (i == R.id.right) {
      savePicture();
    }
  }

  private void initView() {
    photoProcessActivity = this;
    findView();
    setData();
  }

  private void findView() {
    title =  findViewById(this,R.id.title);
    left = findViewById(this,R.id.left);
    right =  findViewById(this,R.id.right);
    gpuimage = findViewById(this,R.id.gpuimage);
    bottomToolBar = findViewById(this,R.id.list_tools);

    title.setText("图片美化");
    left.setImageResource(R.drawable.ic_arrow_back_white_24dp);
    right.setImageResource(R.drawable.ic_arrow_forward_white_24dp);
    left.setOnClickListener(this);
    right.setOnClickListener(this);
  }

  private void setData() {
    Intent intent = getIntent();
    String uri = intent.getStringExtra("uri");
    if (!uri.contains("file://")) {
      uri = "file://" + uri;
    }
    if (!StringUtils.isEmpty(uri)) {
      ImageLoader imageLoader = ImageLoader.getInstance();
      WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
      int width = wm.getDefaultDisplay().getWidth();
      int heiht = wm.getDefaultDisplay().getWidth();
      Bitmap bitmap = imageLoader.loadImageSync(uri, new ImageSize(width, width));
      gpuimage.setImage(bitmap);
      smallImageBackgroud = imageLoader.loadImageSync(uri + "", new ImageSize(80, 80));
      RelativeLayout.LayoutParams rparams = new RelativeLayout.LayoutParams(heiht, width);
      gpuimage.setLayoutParams(rparams);
      initFilterToolBar();
    }
  }

  //保存图片
  private void savePicture() {
    loadingDialog.setMessage("图片保存中..");
    String picName = TimeUtils.dtFormat(new Date(), "yyyyMMddHHmmss");
    gpuimage.saveToPictures(AndroidUtils.PIC_CACHE_PATH, picName + ".jpg",
        new GPUImageView.OnPictureSavedListener() {
          @Override public void onPictureSaved(Uri uri) {
            EventBus.getDefault().post(uri);
            loadingDialog.dismiss();
            finish();
          }
        });
  }

  //初始化滤镜
  private void initFilterToolBar() {
    final List<FilterEffect> filters = EffectService.getInst().getLocalFilters();
    final FilterAdapter adapter =
        new FilterAdapter(PhotoProcessActivity.this, filters, smallImageBackgroud);
    bottomToolBar.setAdapter(adapter);
    bottomToolBar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (adapter.getSelectFilter() != position) {
          adapter.setSelectFilter(position);
          GPUImageFilter filter = GPUImageFilterTools.createFilterForType(PhotoProcessActivity.this,
              filters.get(position).getType());
          gpuimage.setFilter(filter);
          GPUImageFilterTools.FilterAdjuster mFilterAdjuster =
              new GPUImageFilterTools.FilterAdjuster(filter);
          //可调节颜色的滤镜
          if (mFilterAdjuster.canAdjust()) {
            //mFilterAdjuster.adjust(100); 给可调节的滤镜选一个合适的值
          }
        }
      }
    });
  }
}
