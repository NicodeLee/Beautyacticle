/**
 * Copyright (c) 2015 CommonsWare, LLC
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.commonsware.cwac.cam2;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;
import com.nicodelee.beautyarticle.ui.camara.PhotoProcessActivity;
import java.io.File;
import java.util.LinkedList;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Fragment for displaying a camera preview, with hooks to allow
 * you (or the user) to take a picture.
 */
public class CameraFragment extends Fragment {
  private static final String ARG_OUTPUT = "output";
  private static final String ARG_UPDATE_MEDIA_STORE = "updateMediaStore";
  private static final String ARG_IS_VIDEO = "isVideo";
  private static final String ARG_VIDEO_QUALITY = "quality";
  private static final String ARG_SIZE_LIMIT = "sizeLimit";
  private static final String ARG_DURATION_LIMIT = "durationLimit";
  private CameraController ctlr;
  private ViewGroup previewStack;
  private View progress;
  private boolean isVideoRecording = false;
  private boolean mirrorPreview = false;

  public static CameraFragment newPictureInstance(Uri output, boolean updateMediaStore) {
    CameraFragment f = new CameraFragment();
    Bundle args = new Bundle();

    args.putParcelable(ARG_OUTPUT, output);
    args.putBoolean(ARG_UPDATE_MEDIA_STORE, updateMediaStore);
    args.putBoolean(ARG_IS_VIDEO, false);
    f.setArguments(args);

    return (f);
  }

  public static CameraFragment newVideoInstance(Uri output, boolean updateMediaStore, int quality,
      int sizeLimit, int durationLimit) {
    CameraFragment f = new CameraFragment();
    Bundle args = new Bundle();

    args.putParcelable(ARG_OUTPUT, output);
    args.putBoolean(ARG_UPDATE_MEDIA_STORE, updateMediaStore);
    args.putBoolean(ARG_IS_VIDEO, true);
    args.putInt(ARG_VIDEO_QUALITY, quality);
    args.putInt(ARG_SIZE_LIMIT, sizeLimit);
    args.putInt(ARG_DURATION_LIMIT, durationLimit);
    f.setArguments(args);

    return (f);
  }

  /**
   * Standard fragment entry point.
   * @param savedInstanceState State of a previous instance
   */
  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setRetainInstance(true);
  }

  /**
   * Standard lifecycle method, passed along to the CameraController.
   */
  @Override public void onStart() {
    super.onStart();

    EventBus.getDefault().register(this);

    if (ctlr != null) {
      ctlr.start();
    }
  }

  @Override public void onHiddenChanged(boolean isHidden) {
    super.onHiddenChanged(isHidden);

    if (!isHidden) {
      ActionBar ab = getActivity().getActionBar();

      if (ab != null) {
        ab.setBackgroundDrawable(getActivity().getResources()
            .getDrawable(R.drawable.cwac_cam2_action_bar_bg_transparent));
        ab.setTitle("");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
          ab.setDisplayHomeAsUpEnabled(false);
        } else {
          ab.setDisplayShowHomeEnabled(false);
          ab.setHomeButtonEnabled(false);
        }
      }
    }
  }

  /**
   * Standard lifecycle method, for when the fragment moves into
   * the stopped state. Passed along to the CameraController.
   */
  @Override public void onStop() {
    if (ctlr != null) {
      ctlr.stop();
    }

    EventBus.getDefault().unregister(this);

    super.onStop();
  }

  /**
   * Standard lifecycle method, for when the fragment is utterly,
   * ruthlessly destroyed. Passed along to the CameraController,
   * because why should the fragment have all the fun?
   */
  @Override public void onDestroy() {
    if (ctlr != null) {
      ctlr.destroy();
    }

    super.onDestroy();
  }

  /**
   * Standard callback method to create the UI managed by
   * this fragment.
   *
   * @param inflater Used to inflate layouts
   * @param container Parent of the fragment's UI (eventually)
   * @param savedInstanceState State of a previous instance
   * @return the UI being managed by this fragment
   */
  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.cwac_cam2_fragment, container, false);

    WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
    int width = wm.getDefaultDisplay().getWidth();// 屏幕宽度

    previewStack = (ViewGroup) v.findViewById(R.id.cwac_cam2_preview_stack);
    ViewGroup.LayoutParams layout = previewStack.getLayoutParams();
    layout.height = width;
    layout.width = width;
    previewStack.setLayoutParams(layout);

    progress = v.findViewById(R.id.cwac_cam2_progress);
    v.findViewById(R.id.btnTakePhoto).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        performCameraAction();//拍照
      }
    });
    v.findViewById(R.id.btn_switch).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        progress.setVisibility(View.VISIBLE);
        ctlr.switchCamera();//前置后置摄像头切换
      }
    });
    v.findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        getActivity().finish();
      }
    });
    v.findViewById(R.id.btn_chiose_pic).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        Toast.makeText(getActivity(), "从相册选图", Toast.LENGTH_SHORT).show();
      }
    });

    onHiddenChanged(false); // hack, since this does not get

    if (ctlr != null && ctlr.getNumberOfCameras() > 0) {
      prepController();
    }

    return (v);
  }

  /**
   * @return the CameraController this fragment delegates to
   */
  public CameraController getController() {
    return (ctlr);
  }

  /**
   * Establishes the controller that this fragment delegates to
   *
   * @param ctlr the controller that this fragment delegates to
   */
  public void setController(CameraController ctlr) {
    this.ctlr = ctlr;
  }

  /**
   * Indicates if we should mirror the preview or not. Defaults
   * to false.
   *
   * @param mirror true if we should horizontally mirror the
   * preview, false otherwise
   */
  public void setMirrorPreview(boolean mirror) {
    this.mirrorPreview = mirror;
  }

  @SuppressWarnings("unused")
  @Subscribe(sticky = false, threadMode = ThreadMode.MAIN)
  public void onEventMainThread(CameraController.ControllerReadyEvent event) {
    if (event.isEventForController(ctlr)) {
      prepController();
    }
  }

  @Subscribe(sticky = false, threadMode = ThreadMode.MAIN)
  @SuppressWarnings("unused") public void onEventMainThread(CameraEngine.OpenedEvent event) {
    if (event.exception == null) {
      progress.setVisibility(View.GONE);
    } else {
      getActivity().finish();
    }
  }

  @Subscribe(sticky = false, threadMode = ThreadMode.MAIN)
  @SuppressWarnings("unused") public void onEventMainThread(CameraEngine.VideoTakenEvent event) {
    if (event.exception == null) {
      if (getArguments().getBoolean(ARG_UPDATE_MEDIA_STORE, false)) {
        final Context app = getActivity().getApplicationContext();
        Uri output = getArguments().getParcelable(ARG_OUTPUT);
        final String path = output.getPath();

        new Thread() {
          @Override public void run() {
            SystemClock.sleep(2000);
            MediaScannerConnection.scanFile(app, new String[] { path },
                new String[] { "video/mp4" }, null);
          }
        }.start();
      }

      isVideoRecording = false;
    } else {
      getActivity().finish();
    }
  }

  protected void performCameraAction() {
    if (isVideo()) {
      recordVideo();
    } else {
      takePicture();
    }
  }

  private void takePicture() {
    Uri output = getArguments().getParcelable(ARG_OUTPUT);

    PictureTransaction.Builder b = new PictureTransaction.Builder();

    if (output != null) {
      b.toUri(getActivity(), output, getArguments().getBoolean(ARG_UPDATE_MEDIA_STORE, false));
    }

    //Toast.makeText(getActivity(), "拍照,uri="+output, Toast.LENGTH_SHORT).show();
    //Intent intent = new Intent(getActivity(),PhotoProcessActivity.class);
    //intent.putExtra("uri",output+"");
    //startActivity(intent);
    //getActivity().finish();//图片未生成

    ctlr.takePicture(b.build());
  }

  private void recordVideo() {
    if (isVideoRecording) {
      try {
        ctlr.stopVideoRecording();
      } catch (Exception e) {
        Log.e(getClass().getSimpleName(), "Exception stopping recording of video", e);
        // TODO: um, do something here
      }
    } else {
      try {
        VideoTransaction.Builder b = new VideoTransaction.Builder();
        Uri output = getArguments().getParcelable(ARG_OUTPUT);

        b.to(new File(output.getPath()))
            .quality(getArguments().getInt(ARG_VIDEO_QUALITY, 1))
            .sizeLimit(getArguments().getInt(ARG_SIZE_LIMIT, 0))
            .durationLimit(getArguments().getInt(ARG_DURATION_LIMIT, 0));

        ctlr.recordVideo(b.build());
        isVideoRecording = true;
      } catch (Exception e) {
        Log.e(getClass().getSimpleName(), "Exception recording video", e);
        // TODO: um, do something here
      }
    }
  }

  private boolean isVideo() {
    return (getArguments().getBoolean(ARG_IS_VIDEO, false));
  }

  private void prepController() {
    LinkedList<CameraView> cameraViews = new LinkedList<CameraView>();
    CameraView cv = (CameraView) previewStack.getChildAt(0);

    cv.setMirror(mirrorPreview);
    cameraViews.add(cv);

    for (int i = 1; i < ctlr.getNumberOfCameras(); i++) {
      cv = new CameraView(getActivity());
      cv.setVisibility(View.INVISIBLE);
      cv.setMirror(mirrorPreview);
      previewStack.addView(cv);
      cameraViews.add(cv);
    }

    ctlr.setCameraViews(cameraViews);
  }
}
