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
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.os.Build;
import android.util.Log;
import android.view.OrientationEventListener;
import com.commonsware.cwac.cam2.AbstractCameraActivity;
import com.commonsware.cwac.cam2.CameraConfigurator;
import com.commonsware.cwac.cam2.CameraPlugin;
import com.commonsware.cwac.cam2.CameraSession;
import com.commonsware.cwac.cam2.ClassicCameraConfigurator;
import com.commonsware.cwac.cam2.FlashMode;
import com.commonsware.cwac.cam2.SimpleCameraTwoConfigurator;
import com.commonsware.cwac.cam2.SimpleClassicCameraConfigurator;
import java.util.List;

/**
 * Plugin for managing flash modes
 */
public class FlashModePlugin implements CameraPlugin {
  private final List<FlashMode> flashModes;

  public FlashModePlugin(List<FlashMode> flashModes) {
    this.flashModes=flashModes;
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
    // no-op
  }

  class Classic extends SimpleClassicCameraConfigurator {
    /**
     * {@inheritDoc}
     */
    @Override
    public Camera.Parameters configureStillCamera(
      Camera.CameraInfo info,
      Camera camera, Camera.Parameters params) {
      if (params!=null) {
        String desiredMode=null;
        boolean matched=false;

        for (FlashMode mode : flashModes) {
          if (mode==FlashMode.OFF) {
            desiredMode=Camera.Parameters.FLASH_MODE_OFF;
          }
          else if (mode==FlashMode.ALWAYS) {
            desiredMode=Camera.Parameters.FLASH_MODE_ON;
          }
          else if (mode==FlashMode.AUTO) {
            desiredMode=Camera.Parameters.FLASH_MODE_AUTO;
          }
          else if (mode==FlashMode.REDEYE) {
            desiredMode=Camera.Parameters.FLASH_MODE_RED_EYE;
          }

          if (params.getSupportedFlashModes().contains(desiredMode)) {
            params.setFlashMode(desiredMode);
            matched=true;
            break;
          }
        }

        if (!matched) {
          Log.w("CWAC-Cam2", "no support for requested flash mode");
        }
      }

      return(params);
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
      int desiredMode=getConvertedFlashMode(cc);

      if (desiredMode==-1) {
        Log.w("CWAC-Cam2", "no support for requested flash mode");
      }
      else {
        captureBuilder.set(CaptureRequest.CONTROL_AE_MODE,
          desiredMode);
      }
    }

    @Override
    public void addToPreviewRequest(CameraCharacteristics cc,
                                    CaptureRequest.Builder captureBuilder) {
      int desiredMode=getConvertedFlashMode(cc);

      if (desiredMode==-1) {
        Log.w("CWAC-Cam2", "no support for requested flash mode");
      }
      else {
        captureBuilder.set(CaptureRequest.CONTROL_AE_MODE,
          desiredMode);
      }
    }

    private int getConvertedFlashMode(CameraCharacteristics cc) {
      int[] availModes=cc.get(CameraCharacteristics.CONTROL_AE_AVAILABLE_MODES);

      for (FlashMode mode : flashModes) {
        int desiredMode=-1;

        if (mode==FlashMode.OFF) {
          desiredMode=CameraMetadata.CONTROL_AE_MODE_ON;
        }
        else if (mode==FlashMode.ALWAYS) {
          desiredMode=CameraMetadata.CONTROL_AE_MODE_ON_ALWAYS_FLASH;
        }
        else if (mode==FlashMode.AUTO) {
          desiredMode=CameraMetadata.CONTROL_AE_MODE_ON_AUTO_FLASH;
        }
        else if (mode==FlashMode.REDEYE) {
          desiredMode=CameraMetadata.CONTROL_AE_MODE_ON_AUTO_FLASH_REDEYE;
        }

        for (int availMode : availModes) {
          if (desiredMode==availMode) {
            return(desiredMode);
          }
        }
      }

      return(-1);
    }
  }
}
