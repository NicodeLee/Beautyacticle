/***
 Copyright (c) 2015 CommonsWare, LLC

 Licensed under the Apache License, Version 2.0 (the "License"); you may
 not use this file except in compliance with the License. You may obtain
 a copy of the License at http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
*/

package com.commonsware.cwac.cam2;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Stock activity for taking pictures. Supports the same
 * protocol, in terms of extras and return data, as does
 * ACTION_IMAGE_CAPTURE.
 */
public class CameraActivity extends AbstractCameraActivity
    implements ConfirmationFragment.Contract {

  /**
   * Extra name for indicating whether a confirmation screen
   * should appear after taking the picture, or whether taking
   * the picture should immediately return said picture. Defaults
   * to true, meaning that the user should confirm the picture.
   */
  public static final String EXTRA_CONFIRM="cwac_cam2_confirm";

  /**
   * Extra name for whether a preview frame should be saved
   * to getExternalCacheDir() at the point when a picture
   * is taken. This is for debugging purposes, to compare
   * the preview frame with both the taken picture and what
   * you see on the activity's preview. It is very unlikely
   * that you will want this enabled in a production app.
   * Defaults to false.
   */
  public static final String EXTRA_DEBUG_SAVE_PREVIEW_FRAME=
    "cwac_cam2_save_preview";

  private static final String TAG_CONFIRM=ConfirmationFragment.class.getCanonicalName();
  private static final String[] PERMS={Manifest.permission.CAMERA};
  private ConfirmationFragment confirmFrag;
  private boolean needsThumbnail=false;

  @Override
  protected String[] getNeededPermissions() {
    return(PERMS);
  }

  @Override
  protected void init() {
    super.init();

    confirmFrag=(ConfirmationFragment)getFragmentManager().findFragmentByTag(TAG_CONFIRM);

    Uri output=getOutputUri();

    needsThumbnail=(output==null);

    if (confirmFrag==null) {
      confirmFrag=ConfirmationFragment.newInstance();
      getFragmentManager()
          .beginTransaction()
          .add(android.R.id.content, confirmFrag, TAG_CONFIRM)
          .commit();
    }

    if (!cameraFrag.isVisible() && !confirmFrag.isVisible()) {
      getFragmentManager()
          .beginTransaction()
          .hide(confirmFrag)
          .show(cameraFrag)
          .commit();
    }
  }

  @SuppressWarnings("unused")
  @Subscribe(sticky = false, threadMode = ThreadMode.MAIN)
  public void onEventMainThread(CameraEngine.PictureTakenEvent event) {
    if (event.exception==null) {
      if (getIntent().getBooleanExtra(EXTRA_CONFIRM, true)) {
        confirmFrag.setImage(event.getImageContext());

        getFragmentManager()
          .beginTransaction()
          .hide(cameraFrag)
          .show(confirmFrag)
          .commit();
      }
      else {
        completeRequest(event.getImageContext(), true);
      }
    }
    else {
      finish();
    }
  }

  @Override
  public void retakePicture() {
    getFragmentManager()
        .beginTransaction()
        .hide(confirmFrag)
        .show(cameraFrag)
        .commit();
  }

  @Override
  public void completeRequest(ImageContext imageContext, boolean isOK) {
    if (!isOK) {
      setResult(RESULT_CANCELED);
      finish();
    }
    else {
      if (needsThumbnail) {
        final Intent result=new Intent();

        result.putExtra("data", imageContext.buildResultThumbnail());

        findViewById(android.R.id.content).post(new Runnable() {
          @Override
          public void run() {
            setResult(RESULT_OK, result);
            removeFragments();
          }
        });
      }
      else {
        findViewById(android.R.id.content).post(new Runnable() {
          @Override
          public void run() {
            setResult(RESULT_OK, new Intent().setData(getOutputUri()));
            removeFragments();
          }
        });
      }
    }
  }

  @Override
  protected boolean needsOverlay() {
    return(true);
  }

  @Override
  protected boolean needsActionBar() {
    return(true);
  }

  @Override
  protected boolean isVideo() {
    return(false);
  }

  @Override
  protected void configEngine(CameraEngine engine) {
    if (getIntent()
      .getBooleanExtra(EXTRA_DEBUG_SAVE_PREVIEW_FRAME, false)) {
      engine
        .setDebugSavePreviewFile(new File(getExternalCacheDir(),
          "cam2-preview.jpg"));
    }
  }

  @Override
  protected CameraFragment buildFragment() {
    return(CameraFragment.newPictureInstance(getOutputUri(),
        getIntent().getBooleanExtra(EXTRA_UPDATE_MEDIA_STORE, false)));
  }

  private void removeFragments() {
    getFragmentManager()
        .beginTransaction()
        .remove(confirmFrag)
        .remove(cameraFrag)
        .commit();
  }

  /**
   * Class to build an Intent used to start the CameraActivity.
   * Call setComponent() on the Intent if you are using your
   * own subclass of CameraActivity.
   */
  public static class IntentBuilder
    extends AbstractCameraActivity.IntentBuilder<IntentBuilder> {
    /**
     * Standard constructor. May throw a runtime exception
     * if the environment is not set up properly (see
     * validateEnvironment() on Utils).
     *
     * @param ctxt any Context will do
     */
    public IntentBuilder(Context ctxt) {
      super(ctxt, CameraActivity.class);
    }

    /**
     * Call to skip the confirmation screen, so once the user
     * takes the picture, you get control back right away.
     *
     * @return the builder, for further configuration
     */
    public IntentBuilder skipConfirm() {
      result.putExtra(EXTRA_CONFIRM, false);

      return(this);
    }

    public IntentBuilder debugSavePreviewFrame() {
      result.putExtra(EXTRA_DEBUG_SAVE_PREVIEW_FRAME, true);

      return(this);
    }
  }
}
