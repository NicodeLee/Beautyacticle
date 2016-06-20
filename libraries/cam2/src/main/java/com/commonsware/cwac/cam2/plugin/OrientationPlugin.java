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

package com.commonsware.cwac.cam2.plugin;

import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CaptureRequest;
import android.media.MediaRecorder;
import android.os.Build;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.WindowManager;
import com.commonsware.cwac.cam2.CameraConfigurator;
import com.commonsware.cwac.cam2.CameraEngine;
import com.commonsware.cwac.cam2.CameraPlugin;
import com.commonsware.cwac.cam2.CameraSession;
import com.commonsware.cwac.cam2.ClassicCameraConfigurator;
import com.commonsware.cwac.cam2.SimpleCameraTwoConfigurator;
import com.commonsware.cwac.cam2.SimpleClassicCameraConfigurator;
import com.commonsware.cwac.cam2.VideoTransaction;
import org.greenrobot.eventbus.EventBus;

/**
 * Plugin for managing orientation effects on the previews
 * and pictures.
 */
public class OrientationPlugin implements CameraPlugin {
  private final Context ctxt;
  private OrientationEventListener orientationEventListener;
  private int lastOrientation=OrientationEventListener.ORIENTATION_UNKNOWN;

  public OrientationPlugin(Context ctxt) {
    this.ctxt=ctxt.getApplicationContext();

    orientationEventListener=new OrientationEventListener(ctxt) {
      @Override
      public void onOrientationChanged(int orientation) {
        if (lastOrientation!=orientation) {
          EventBus
            .getDefault()
            .post(new CameraEngine.OrientationChangedEvent());
        }

        lastOrientation=orientation;
      }
    };

    if (orientationEventListener.canDetectOrientation()) {
      orientationEventListener.enable();
    }
    else {
      orientationEventListener.disable();
      orientationEventListener=null;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <T extends CameraConfigurator> T buildConfigurator(Class<T> type) {
    if (type == ClassicCameraConfigurator.class) {
      return (type.cast(new Classic()));
    }

    return(type.cast(new Two()));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void validate(CameraSession session) {
    // no validation required
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void destroy() {
    if (orientationEventListener!=null) {
      orientationEventListener.disable();
      orientationEventListener=null;
    }
  }

  class Classic extends SimpleClassicCameraConfigurator {
    /**
     * {@inheritDoc}
     */
    @Override
    public Camera.Parameters configureStillCamera(
      Camera.CameraInfo info,
      Camera camera, Camera.Parameters params) {
      int displayOrientation=getDisplayOrientation(info, true);

      if ("samsung".equals(Build.MANUFACTURER) &&
        "sf2wifixx".equals(Build.PRODUCT)) {
        camera.setDisplayOrientation(0);
      }
      else if (displayOrientation==180) {
        camera.setDisplayOrientation(270);
      }
      else {
        camera.setDisplayOrientation(90);
      }

      if (params!=null) {
        int outputOrientation;

        if (info.facing==Camera.CameraInfo.CAMERA_FACING_FRONT) {
          outputOrientation=(360-displayOrientation)%360;
        }
        else {
          outputOrientation=displayOrientation;
        }

        params.setRotation(outputOrientation);
      }

      return(params);
    }

    @Override
    public void configureRecorder(int cameraId,
                                  VideoTransaction xact,
                                  MediaRecorder recorder) {
      Camera.CameraInfo info=new Camera.CameraInfo();
      Camera.getCameraInfo(cameraId, info);
      int displayOrientation=getDisplayOrientation(info, false);

      recorder.setOrientationHint(displayOrientation);
    }

    private int getDisplayOrientation(Camera.CameraInfo info,
                                      boolean isStillCapture) {
      WindowManager mgr=(WindowManager)ctxt.getSystemService(Context.WINDOW_SERVICE);
      int rotation=mgr.getDefaultDisplay().getRotation();
      int degrees=0;

      switch (rotation) {
        case Surface.ROTATION_0:
          degrees=0;
          break;
        case Surface.ROTATION_90:
          degrees=90;
          break;
        case Surface.ROTATION_180:
          degrees=180;
          break;
        case Surface.ROTATION_270:
          degrees=270;
          break;
      }

      int displayOrientation;

      if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
        displayOrientation=(info.orientation + degrees) % 360;
        displayOrientation=(360 - displayOrientation) % 360;

        if (!isStillCapture && displayOrientation==90) {
          displayOrientation=270;
        }
      }
      else {
        displayOrientation=(info.orientation - degrees + 360) % 360;
      }

      return(displayOrientation);
    }
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  class Two extends SimpleCameraTwoConfigurator {
    /**
     * {@inheritDoc}
     */
    @Override
    public void addToCaptureRequest(CameraCharacteristics cc,
                                    boolean facingFront,
                                    CaptureRequest.Builder captureBuilder) {
      // based on https://developer.android.com/reference/android/hardware/camera2/CaptureRequest.html#JPEG_ORIENTATION

      int pictureOrientation=0;

      if (lastOrientation!=android.view.OrientationEventListener.ORIENTATION_UNKNOWN) {
        int sensorOrientation=cc.get(CameraCharacteristics.SENSOR_ORIENTATION);
        int deviceOrientation=(lastOrientation + 45) / 90 * 90;

        if (facingFront) {
          deviceOrientation = -deviceOrientation;
        }

        pictureOrientation=(sensorOrientation + deviceOrientation + 360) % 360;
      }

      captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, pictureOrientation);
    }
  }
}
