package com.nicodelee.beautyarticle.ui.view.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.commonsware.cwac.cam2.AbstractCameraActivity;
import com.commonsware.cwac.cam2.CameraEngine;
import com.commonsware.cwac.cam2.CameraFragment;
import com.commonsware.cwac.cam2.ConfirmationFragment;
import com.commonsware.cwac.cam2.ImageContext;
import com.nicodelee.beautyarticle.ui.camara.PhotoProcessActivity;
import com.nicodelee.beautyarticle.utils.Logger;
import java.io.File;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by NocodeLee on 15/11/23.
 * Email：lirizhilirizhi@163.com
 */
public class CameraActivity extends AbstractCameraActivity
    implements ConfirmationFragment.Contract {

  public static final String EXTRA_CONFIRM = "cwac_cam2_confirm";

  public static final String EXTRA_DEBUG_SAVE_PREVIEW_FRAME = "cwac_cam2_save_preview";

  private static final String TAG_CONFIRM = ConfirmationFragment.class.getCanonicalName();
  private static final String[] PERMS = { Manifest.permission.CAMERA };
  private ConfirmationFragment confirmFrag;
  private boolean needsThumbnail = false;

  @Override protected String[] getNeededPermissions() {
    return (PERMS);
  }

  @Override protected void init() {
    super.init();

    confirmFrag = (ConfirmationFragment) getFragmentManager().findFragmentByTag(TAG_CONFIRM);

    Uri output = getOutputUri();

    needsThumbnail = (output == null);

    if (confirmFrag == null) {
      confirmFrag = ConfirmationFragment.newInstance();
      getFragmentManager().beginTransaction()
          .add(android.R.id.content, confirmFrag, TAG_CONFIRM)
          .commit();
    }

    if (!cameraFrag.isVisible() && !confirmFrag.isVisible()) {
      getFragmentManager().beginTransaction().hide(confirmFrag).show(cameraFrag).commit();
    }
  }

  @Subscribe(sticky = false, threadMode = ThreadMode.MAIN)
  @SuppressWarnings("unused") public void onEventMainThread(CameraEngine.PictureTakenEvent event) {
    if (event.exception == null) {
      if (getIntent().getBooleanExtra(EXTRA_CONFIRM, true)) {
        confirmFrag.setImage(event.getImageContext());

        getFragmentManager().beginTransaction().hide(cameraFrag).show(confirmFrag).commit();
      } else {
        completeRequest(event.getImageContext(), true);
      }
    } else {
      finish();
    }
  }

  @Override public void retakePicture() {
    getFragmentManager().beginTransaction().hide(confirmFrag).show(cameraFrag).commit();
  }

  @Override public void completeRequest(ImageContext imageContext, boolean isOK) {
    Logger.e(String.format("isOk=%b",isOK));
    //拍照回调数据
    if (!isOK) {
      setResult(RESULT_CANCELED);
      finish();
    } else {
      Logger.e(String.format("needsThumbnail=%b",needsThumbnail));
      if (needsThumbnail) {
        final Intent result = new Intent();

        result.putExtra("data", imageContext.buildResultThumbnail());

        findViewById(android.R.id.content).post(new Runnable() {
          @Override public void run() {
            setResult(RESULT_OK, result);
            removeFragments();
          }
        });
      } else {
        findViewById(android.R.id.content).post(new Runnable() {
          @Override public void run() {
            //setResult(RESULT_OK, new Intent().setData(getOutputUri()));
            Intent intent = new Intent(CameraActivity.this,PhotoProcessActivity.class);
            intent.putExtra("uri",getOutputUri()+"");
            //intent.setData(getOutputUri());
            Logger.e("uri="+getOutputUri());
            startActivity(intent);
            removeFragments();
          }
        });
      }
    }
  }

  @Override protected boolean needsOverlay() {
    return (true);
  }

  @Override protected boolean needsActionBar() {
    return (true);
  }

  @Override protected boolean isVideo() {
    return (false);
  }


  @Override protected void configEngine(CameraEngine engine) {
    if (getIntent().getBooleanExtra(EXTRA_DEBUG_SAVE_PREVIEW_FRAME, false)) {
      engine.setDebugSavePreviewFile(new File(getExternalCacheDir(), "cam2-preview.jpg"));
    }
  }

  @Override protected CameraFragment buildFragment() {
    return (CameraFragment.newPictureInstance(getOutputUri(),
        getIntent().getBooleanExtra(EXTRA_UPDATE_MEDIA_STORE, false)));
  }


  private void removeFragments() {
    getFragmentManager().beginTransaction().remove(confirmFrag).remove(cameraFrag).commit();
  }

  public static class IntentBuilder extends AbstractCameraActivity.IntentBuilder {

    public IntentBuilder(Context ctxt) {
      super(ctxt, CameraActivity.class);
    }

    public IntentBuilder skipConfirm() {
      result.putExtra(EXTRA_CONFIRM, false);

      return (this);
    }
  }
}
