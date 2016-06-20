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

import android.content.res.Configuration;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.os.Build;
import android.view.View;
import com.commonsware.cwac.cam2.plugin.FlashModePlugin;
import com.commonsware.cwac.cam2.plugin.FocusModePlugin;
import com.commonsware.cwac.cam2.plugin.OrientationPlugin;
import com.commonsware.cwac.cam2.plugin.SizeAndFormatPlugin;
import com.commonsware.cwac.cam2.util.Size;
import com.commonsware.cwac.cam2.util.Utils;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Controller for camera-related functions, designed to be used
 * by CameraFragment or the equivalent.
 */
public class CameraController implements CameraView.StateCallback {
  private CameraEngine engine;
  private CameraSession session;
  private List<CameraDescriptor> cameras=null;
  private int currentCamera=0;
  private final HashMap<CameraDescriptor, CameraView> previews=
      new HashMap<CameraDescriptor, CameraView>();
  private Queue<CameraView> availablePreviews=null;
  private boolean switchPending=false;
  private boolean isVideoRecording=false;
  private final AbstractCameraActivity.FocusMode focusMode;
  private final List<FlashMode> flashModes;
  private final boolean isVideo;

  public CameraController(AbstractCameraActivity.FocusMode focusMode,
                          List<FlashMode> flashModes,
                          boolean isVideo) {
    this.focusMode=focusMode==null ?
      AbstractCameraActivity.FocusMode.CONTINUOUS : focusMode;
    this.flashModes=flashModes;
    this.isVideo=isVideo;
  }

  /**
   * @return the engine being used by this fragment to access
   * the camera(s) on the device
   */
  public CameraEngine getEngine() {
    return(engine);
  }

  /**
   * Setter for the engine. Must be called before onCreateView()
   * is called, preferably shortly after constructing the
   * fragment.
   *
   * @param engine the engine to be used by this fragment to access
   * the camera(s) on the device
   */
  public void setEngine(CameraEngine engine, CameraSelectionCriteria criteria) {
    this.engine=engine;

    EventBus.getDefault().register(this);

    engine.loadCameraDescriptors(criteria);
  }

  public int getNumberOfCameras() {
    return(cameras==null ? 0 : cameras.size());
  }

  /**
   * Call this from onStart() of an activity or fragment, or from
   * an equivalent point in time. If the CameraView is ready,
   * the preview should begin; otherwise, the preview will
   * begin after the CameraView is ready.
   */
  public void start() {
    if (cameras!=null) {
      CameraDescriptor camera=cameras.get(currentCamera);
      CameraView cv=getPreview(camera);

      if (cv.isAvailable()) {
        open();
      }
    }
  }

  /**
   * Call this from onStop() of an activity or fragment, or
   * from an equivalent point in time, to indicate that you want
   * the camera preview to stop.
   */
  public void stop() {
    if (session!=null) {
      CameraSession temp=session;

      session=null;
      engine.close(temp);
      // session.destroy(); -- moved into engines
    }
  }

  /**
   * Call this from onDestroy() of an activity or fragment,
   * or from an equivalent point in time, to tear down the
   * entire controller. A fresh controller should
   * be created if you want to use the camera again in the future.
   */
  public void destroy() {
    EventBus.getDefault().post(new ControllerDestroyedEvent(this));
    EventBus.getDefault().unregister(this);
  }

  /**
   * Call to switch to the next camera in sequence. Most
   * devices have only two cameras, and so calling this will
   * switch the preview and pictures to the camera other than
   * the one presently being used.
   */
  public void switchCamera() {
    if (session!=null) {
      getPreview(session.getDescriptor()).setVisibility(View.INVISIBLE);
      switchPending=true;
      stop();
    }
  }

  /**
   * Supplies CameraView objects for each camera. After this,
   * we can open() the camera.
   *
   * @param cameraViews a list of CameraViews
   */
  public void setCameraViews(Queue<CameraView> cameraViews) {
    availablePreviews=cameraViews;
    previews.clear();

    for (CameraView cv : cameraViews) {
      cv.setStateCallback(this);
    }

    open(); // in case visible CameraView is already ready
  }

  /**
   * Public because Java interfaces are intrinsically public.
   * This method is not part of the class' API and should not
   * be used by third-party developers.
   *
   * @param cv the CameraView that is now ready
   */
  @Override
  public void onReady(CameraView cv) {
    if (cameras!=null) {
      open();
    }
  }

  /**
   * Public because Java interfaces are intrinsically public.
   * This method is not part of the class' API and should not
   * be used by third-party developers.
   *
   * @param cv the CameraView that is now destroyed
   */
  @Override
  public void onDestroyed(CameraView cv) {
    stop();
  }

  /**
   * Takes a picture, in accordance with the details supplied
   * in the PictureTransaction. Subscribe to the
   * PictureTakenEvent to get the results of the picture.
   *
   * @param xact a PictureTransaction describing what should be taken
   */
  public void takePicture(PictureTransaction xact) {
    if (session!=null) {
      engine.takePicture(session, xact);
    }
  }

  public void recordVideo(VideoTransaction xact) throws Exception {
    if (session!=null) {
      engine.recordVideo(session, xact);
      isVideoRecording=true;
    }
  }

  public void stopVideoRecording() throws Exception {
    if (session!=null && isVideoRecording) {
      try {
        engine.stopVideoRecording(session);
      }
      finally {
        isVideoRecording=false;
      }
    }
  }

  private CameraView getPreview(CameraDescriptor camera) {
    CameraView result=previews.get(camera);

    if (result==null) {
      result=availablePreviews.remove();
      previews.put(camera, result);
    }

    return(result);
  }

  private int getNextCameraIndex() {
    int next=currentCamera+1;

    if (next==cameras.size()) {
      next=0;
    }

    return(next);
  }

  private void open() {
    if (session==null) {
      Size previewSize=null;
      CameraDescriptor camera=cameras.get(currentCamera);
      CameraView cv=getPreview(camera);
      Size largest=Utils.getLargestPictureSize(camera);

      if (camera != null && cv.getWidth() > 0 && cv.getHeight() > 0) {
        previewSize=Utils.chooseOptimalSize(camera.getPreviewSizes(),
            cv.getWidth(), cv.getHeight(), largest);

        boolean shouldSwapPreviewDimensions=
          cv
            .getContext()
            .getResources()
            .getConfiguration().orientation==
            Configuration.ORIENTATION_PORTRAIT;
        Size virtualPreviewSize=previewSize;

        if (shouldSwapPreviewDimensions) {
          virtualPreviewSize=new Size(previewSize.getHeight(),
          previewSize.getWidth());
        }

        cv.setPreviewSize(virtualPreviewSize);
      }

      SurfaceTexture texture=cv.getSurfaceTexture();

      if (previewSize != null && texture != null) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
          texture.setDefaultBufferSize(previewSize.getWidth(),
              previewSize.getHeight());
        }

        session=engine
            .buildSession(cv.getContext(), camera)
            .addPlugin(new SizeAndFormatPlugin(previewSize,
              largest, ImageFormat.JPEG))
            .addPlugin(new OrientationPlugin(cv.getContext()))
            .addPlugin(new FocusModePlugin(cv.getContext(), focusMode, isVideo))
            .addPlugin(
              new FlashModePlugin(flashModes))
            .build();

        engine.open(session, texture);
      }
    }
  }

  @SuppressWarnings("unused")
  @Subscribe(sticky = false, threadMode = ThreadMode.MAIN)
  public void onEventMainThread(CameraEngine.CameraDescriptorsEvent event) {
    if (event.descriptors.size()>0) {
      cameras=event.descriptors;
      EventBus.getDefault().post(new ControllerReadyEvent(this, cameras.size()));
    }
    else {
      EventBus.getDefault().post(new NoSuchCameraEvent());
    }
  }

  @SuppressWarnings("unused")
  @Subscribe(sticky = false, threadMode = ThreadMode.MAIN)
  public void onEventMainThread(CameraEngine.ClosedEvent event) {
    if (switchPending) {
      switchPending=false;
      currentCamera=getNextCameraIndex();
      getPreview(cameras.get(currentCamera)).setVisibility(View.VISIBLE);
      open();
    }
  }

  @SuppressWarnings("unused")
  @Subscribe(sticky = false, threadMode = ThreadMode.MAIN)
  public void onEventMainThread(CameraEngine.OrientationChangedEvent event) {
    if (engine!=null) {
      engine.handleOrientationChange(session, event);
    }
  }

  /**
   * Raised if there are no available cameras on this
   * device. Consider using uses-feature elements in the
   * manifest, so your app only runs on devices that have
   * a camera, if you need a camera.
   */
  public static class NoSuchCameraEvent {

  }

  /**
   * Event raised when the controller has its cameras
   * and is ready for use. Clients should then turn
   * around and call setCameraViews() to complete the process
   * and start showing the first preview.
   */
  public static class ControllerReadyEvent {
    final private int cameraCount;
    final private CameraController ctlr;

    private ControllerReadyEvent(CameraController ctlr, int cameraCount) {
      this.cameraCount=cameraCount;
      this.ctlr=ctlr;
    }

    public int getNumberOfCameras() {
      return(cameraCount);
    }

    public boolean isEventForController(CameraController ctlr) {
      return(this.ctlr==ctlr);
    }
  }

  /**
   * Event raised when the controller has its cameras
   * and is ready for use. Clients should then turn
   * around and call setCameraViews() to complete the process
   * and start showing the first preview.
   */
  public static class ControllerDestroyedEvent {
    private final CameraController ctlr;

    ControllerDestroyedEvent(CameraController ctlr) {
      this.ctlr=ctlr;
    }

    public CameraController getDestroyedController() {
      return(ctlr);
    }
  }
}
