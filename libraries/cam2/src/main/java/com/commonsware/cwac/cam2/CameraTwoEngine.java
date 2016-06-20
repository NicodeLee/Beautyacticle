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

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.media.MediaActionSound;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.Surface;
import com.commonsware.cwac.cam2.util.Size;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import org.greenrobot.eventbus.EventBus;

/**
 * Implementation of a CameraEngine that supports the
 * Android 5.0+ android.hardware.camera2 API.
 */
@SuppressWarnings("ResourceType")
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class CameraTwoEngine extends CameraEngine {
  private final Context ctxt;
  private CameraManager mgr;
  final private HandlerThread handlerThread=new HandlerThread(getClass().getSimpleName(),
      android.os.Process.THREAD_PRIORITY_BACKGROUND);
  final private Handler handler;
  final private Semaphore lock=new Semaphore(1);
  private CountDownLatch closeLatch=null;
  private MediaActionSound shutter=new MediaActionSound();
  private List<Descriptor> descriptors=null;

  /**
   * Standard constructor
   *
   * @param ctxt any Context will do
   */
  public CameraTwoEngine(Context ctxt) {
    this.ctxt=ctxt.getApplicationContext();
    mgr=(CameraManager)this.ctxt.
        getSystemService(Context.CAMERA_SERVICE);
    handlerThread.start();
    handler=new Handler(handlerThread.getLooper());
    shutter.load(MediaActionSound.SHUTTER_CLICK);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CameraSession.Builder buildSession(Context ctxt, CameraDescriptor descriptor) {
    return(new SessionBuilder(ctxt, descriptor));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void loadCameraDescriptors(final CameraSelectionCriteria criteria) {
    getThreadPool().execute(new Runnable() {
      @Override
      public void run() {
        if (descriptors==null) {
          List<Descriptor> result=new ArrayList<Descriptor>();

          try {
            for (String cameraId : mgr.getCameraIdList()) {
              CameraCharacteristics cc=
                mgr.getCameraCharacteristics(cameraId);
              Descriptor camera=new Descriptor(cameraId, cc);
              StreamConfigurationMap map=cc.get(
                CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
              android.util.Size[] rawSizes=
                map.getOutputSizes(SurfaceTexture.class);
              ArrayList<Size> sizes=new ArrayList<Size>();

              for (android.util.Size size : rawSizes) {
                sizes.add(
                  new Size(size.getWidth(), size.getHeight()));
              }

              camera.setPreviewSizes(sizes);
              camera.setPictureSizes(Arrays.asList(
                map.getOutputSizes(ImageFormat.JPEG)));
              camera.setFacingFront(
                cc.get(CameraCharacteristics.LENS_FACING)==
                  CameraCharacteristics.LENS_FACING_FRONT);
              result.add(camera);
            }

            descriptors=result;
          }
          catch (CameraAccessException e) {
            getBus().post(
              new CameraEngine.CameraDescriptorsEvent(e));

            if (isDebug()) {
              Log.e(getClass().getSimpleName(),
                "Exception accessing camera", e);
            }
          }
        }

        List<CameraDescriptor> result=
          new ArrayList<CameraDescriptor>();

        for (Descriptor camera : descriptors) {
          if (!criteria.getFacingExactMatch() ||
            camera.getScore(criteria)>0) {
            result.add(camera);
          }
        }

        Collections.sort(result,
          new Comparator<CameraDescriptor>() {
            @Override
            public int compare(CameraDescriptor descriptor,
                               CameraDescriptor t1) {
              Descriptor lhs=(Descriptor)descriptor;
              Descriptor rhs=(Descriptor)t1;

              // descending, so invert normal side-ness

              return (Integer.compare(rhs.getScore(criteria),
                lhs.getScore(criteria)));
            }
          });

        getBus().post(
          new CameraEngine.CameraDescriptorsEvent(result));
      }
    });
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void open(final CameraSession session,
                   final SurfaceTexture texture) {
    getThreadPool().execute(new Runnable() {
      @Override
      public void run() {
        Descriptor camera=(Descriptor)session.getDescriptor();

        try {
          if (!lock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
            throw new RuntimeException("Time out waiting to lock camera opening.");
          }

          mgr.openCamera(camera.getId(),
              new InitPreviewTransaction(session, new Surface(texture)),
              handler);
        }
        catch (Exception e) {
          getBus().post(new OpenedEvent(e));

          if (isDebug()) {
            Log.e(getClass().getSimpleName(), "Exception opening camera", e);
          }
        }
      }
    });
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void close(final CameraSession session) {
    final Session s=(Session)session;

    try {
      lock.acquire();

      if (s.captureSession != null) {
        closeLatch=new CountDownLatch(1);
        s.captureSession.close();
        closeLatch.await(2, TimeUnit.SECONDS);
        s.captureSession=null;
      }

      if (s.cameraDevice != null) {
        s.cameraDevice.close();
        s.cameraDevice=null;
      }

      if (s.reader != null) {
        s.reader.close();
      }

      s.setClosed(true);

      Descriptor camera=(Descriptor)session.getDescriptor();

      camera.setDevice(null);
      session.destroy();
      getBus().post(new ClosedEvent());
    }
    catch (Exception e) {
      getBus().post(new ClosedEvent(e));
    }
    finally {
      lock.release();
    }
  }

  /**
   * {@inheritDoc}
   */
  public void takePicture(CameraSession session,
                          PictureTransaction xact) {
    final Session s=(Session)session;

    s.reader.setOnImageAvailableListener(new TakePictureTransaction(session.getContext(), getBus(), xact),
        handler);

    getThreadPool().execute(new Runnable() {
      @Override
      public void run() {
        try {
          // This is how to tell the camera to lock focus.
          s.previewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER,
              CameraMetadata.CONTROL_AF_TRIGGER_START);
          s.captureSession.setRepeatingRequest(s.previewRequestBuilder.build(),
              new RequestCaptureTransaction(s),
              handler);
        }
        catch (Exception e) {
          getBus().post(new PictureTakenEvent(e));

          if (isDebug()) {
            Log.e(getClass().getSimpleName(), "Exception taking picture", e);
          }
        }
      }
    });
  }

  @Override
  public void handleOrientationChange(CameraSession session,
                                      OrientationChangedEvent event) {
    // TODO
  }

  @Override
  public void recordVideo(CameraSession session, VideoTransaction xact) {
    // TODO
  }

  @Override
  public void stopVideoRecording(CameraSession session) {
    // TODO
  }

  private class InitPreviewTransaction extends CameraDevice.StateCallback {
    private final Session s;
    private final Surface surface;

    InitPreviewTransaction(CameraSession session, Surface surface) {
      this.s=(Session)session;
      this.surface=surface;
    }

    @Override
    public void onOpened(CameraDevice cameraDevice) {
      lock.release();
      s.cameraDevice=cameraDevice;
      s.reader=s.buildImageReader();

      Descriptor camera=(Descriptor)s.getDescriptor();

      camera.setDevice(cameraDevice);

      try {
        cameraDevice.createCaptureSession(
            Arrays.asList(surface, s.reader.getSurface()),
            new StartPreviewTransaction(s, surface), handler);
      }
      catch (CameraAccessException e) {
        getBus().post(new OpenedEvent(e));
      }
    }

    @Override
    public void onDisconnected(CameraDevice cameraDevice) {
      lock.release();
      cameraDevice.close();
    }

    @Override
    public void onError(CameraDevice cameraDevice, int i) {
      lock.release();
      cameraDevice.close();
      EventBus.getDefault().post(new CameraTwoPreviewErrorEvent(i));
    }

    @Override
    public void onClosed(CameraDevice camera) {
      super.onClosed(camera);

      if (closeLatch != null) {
        closeLatch.countDown();
      }
    }
  }

  private class StartPreviewTransaction extends CameraCaptureSession.StateCallback {
    private final Surface surface;
    private final Session s;

    StartPreviewTransaction(CameraSession session, Surface surface) {
      this.s=(Session)session;
      this.surface=surface;
    }

    @Override
    public void onConfigured(CameraCaptureSession session) {
      try {
        if (!s.isClosed()) {
          s.captureSession=session;

          s.previewRequestBuilder=session.getDevice().createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
          s.previewRequestBuilder.addTarget(surface);
          s.previewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,
              CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
          s.previewRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE,
              CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
          // TODO: offer other flash support

          Descriptor camera=(Descriptor)s.getDescriptor();
          CameraCharacteristics cc=mgr.getCameraCharacteristics(camera.cameraId);

          s.addToPreviewRequest(cc, s.previewRequestBuilder);

          s.previewRequest=s.previewRequestBuilder.build();

          session.setRepeatingRequest(s.previewRequest, null, handler);

          getBus().post(new OpenedEvent());
        }
      }
      catch (CameraAccessException e) {
        getBus().post(new OpenedEvent(e));
      }
      catch (IllegalStateException e) {
        if (isDebug()) {
          Log.w(getClass().getSimpleName(), "Exception resetting focus", e);
        }
      }
    }

    @Override
    public void onConfigureFailed(CameraCaptureSession session) {
      EventBus.getDefault().post(new CameraTwoPreviewFailureEvent());
    }
  }

  private class RequestCaptureTransaction extends CameraCaptureSession.CaptureCallback {
    private final Session s;
    boolean isWaitingForFocus=true;
    boolean isWaitingForPrecapture=false;
    boolean haveWeStartedCapture=false;

    RequestCaptureTransaction(CameraSession session) {
      this.s=(Session)session;
    }

    @Override
    public void onCaptureProgressed(CameraCaptureSession session,
                                    CaptureRequest request, CaptureResult partialResult) {
      capture(partialResult);
    }

    @Override
    public void onCaptureFailed(CameraCaptureSession session, CaptureRequest request, CaptureFailure failure) {
      getBus().post(new PictureTakenEvent(new RuntimeException("generic camera2 capture failure")));
    }

    @Override
    public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
      capture(result);
    }

    private void capture(CaptureResult result) {
      if (isWaitingForFocus) {
        isWaitingForFocus=false;

        int autoFocusState=result.get(CaptureResult.CONTROL_AF_STATE);

        if (CaptureResult.CONTROL_AF_STATE_FOCUSED_LOCKED == autoFocusState ||
            CaptureResult.CONTROL_AF_STATE_NOT_FOCUSED_LOCKED == autoFocusState) {
          Integer state=result.get(CaptureResult.CONTROL_AE_STATE);

          if (state == null ||
              state == CaptureResult.CONTROL_AE_STATE_CONVERGED) {
            isWaitingForPrecapture=false;
            haveWeStartedCapture=true;
            capture(s);
          } else {
            isWaitingForPrecapture=true;
            precapture(s);
          }
        }
      }
      else if (isWaitingForPrecapture) {
        Integer state=result.get(CaptureResult.CONTROL_AE_STATE);

        if (state == null ||
            state == CaptureResult.CONTROL_AE_STATE_PRECAPTURE ||
            state == CaptureRequest.CONTROL_AE_STATE_FLASH_REQUIRED) {
          isWaitingForPrecapture=false;
        }
      }
      else if (!haveWeStartedCapture) {
        Integer state=result.get(CaptureResult.CONTROL_AE_STATE);

        if (state == null ||
            state != CaptureResult.CONTROL_AE_STATE_PRECAPTURE) {
          haveWeStartedCapture=true;
          capture(s);
        }
      }
    }

    private void precapture(Session s) {
      try {
        s.previewRequestBuilder.set(CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER,
            CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER_START);
        s.captureSession.capture(s.previewRequestBuilder.build(), this,
            handler);
      }
      catch (Exception e) {
        getBus().post(new PictureTakenEvent(e));

        if (isDebug()) {
          Log.e(getClass().getSimpleName(), "Exception running precapture", e);
        }
      }
    }

    private void capture(Session s) {
      try {
        CaptureRequest.Builder captureBuilder=
            s.cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);

        captureBuilder.addTarget(s.reader.getSurface());
        captureBuilder.set(CaptureRequest.CONTROL_AF_MODE,
            CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
        captureBuilder.set(CaptureRequest.CONTROL_AE_MODE,
            CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);

        Descriptor camera=(Descriptor)s.getDescriptor();
        CameraCharacteristics cc=mgr.getCameraCharacteristics(camera.cameraId);

        s.addToCaptureRequest(cc, camera.isFacingFront, captureBuilder);

        s.captureSession.stopRepeating();
        s.captureSession.capture(captureBuilder.build(),
            new CapturePictureTransaction(s), null);
      }
      catch (Exception e) {
        getBus().post(new PictureTakenEvent(e));

        if (isDebug()) {
          Log.e(getClass().getSimpleName(), "Exception running capture", e);
        }
      }
    }
  }

  private class CapturePictureTransaction
    extends CameraCaptureSession.CaptureCallback {
    private final Session s;

    CapturePictureTransaction(CameraSession session) {
      this.s=(Session)session;
    }

    @Override
    public void onCaptureStarted(CameraCaptureSession session,
                                 CaptureRequest request,
                                 long timestamp, long frameNumber) {
      super.onCaptureStarted(session, request, timestamp, frameNumber);

      shutter.play(MediaActionSound.SHUTTER_CLICK);
    }

    @Override
    public void onCaptureCompleted(CameraCaptureSession session,
                                   CaptureRequest request,
                                   TotalCaptureResult result) {
      // TODO: something useful with the picture
      unlockFocus();
    }

    @Override
    public void onCaptureFailed(CameraCaptureSession session,
                                CaptureRequest request,
                                CaptureFailure failure) {
      getBus()
        .post(new PictureTakenEvent(new RuntimeException("generic camera2 capture failure")));
    }

    private void unlockFocus() {
      try {
        if (!s.isClosed()) {
          s.previewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER,
              CameraMetadata.CONTROL_AF_TRIGGER_CANCEL);
          s.previewRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE,
              CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
          s.captureSession.capture(s.previewRequestBuilder.build(), null,
              handler);
          s.captureSession.setRepeatingRequest(s.previewRequest, null, handler);
        }
      }
      catch (CameraAccessException e) {
        getBus().post(new PictureTakenEvent(e));

        if (isDebug()) {
          Log.e(getClass().getSimpleName(), "Exception resetting focus", e);
        }
      }
      catch (IllegalStateException e) {
        if (isDebug()) {
          Log.w(getClass().getSimpleName(), "Exception resetting focus", e);
        }
      }
    }
  }

  private static class AreaComparator implements Comparator<Size> {
    @Override
    public int compare(Size lhs, Size rhs) {
      long lhArea=(long)lhs.getWidth() * lhs.getHeight();
      long rhArea=(long)rhs.getWidth() * rhs.getHeight();

      return (Long.signum(lhArea - rhArea));
    }
  }

  static class Descriptor implements CameraDescriptor {
    private final String cameraId;
    private CameraDevice device;
    private ArrayList<Size> pictureSizes;
    private ArrayList<Size> previewSizes;
    private boolean isFacingFront;
    private final Integer facing;

    private Descriptor(String cameraId, CameraCharacteristics cc) {
      this.cameraId=cameraId;
      facing=cc.get(CameraCharacteristics.LENS_FACING);
    }

    public String getId() {
      return (cameraId);
    }

    private void setDevice(CameraDevice device) {
      this.device=device;
    }

    private CameraDevice getDevice() {
      return (device);
    }

    @Override
    public boolean isPictureFormatSupported(int format) {
      return(ImageFormat.JPEG == format);
    }

    @Override
    public ArrayList<Size> getPreviewSizes() {
      return(previewSizes);
    }

    private void setPreviewSizes(ArrayList<Size> sizes) {
      previewSizes=sizes;
    }

    @Override
    public ArrayList<Size> getPictureSizes() {
      return(pictureSizes);
    }

    private void setPictureSizes(List<android.util.Size> sizes) {
      pictureSizes=new ArrayList<Size>(sizes.size());

      for (android.util.Size size : sizes) {
        pictureSizes.add(new Size(size.getWidth(), size.getHeight()));
      }
    }

    private void setFacingFront(boolean isFacingFront) {
      this.isFacingFront=isFacingFront;
    }

    private int getScore(CameraSelectionCriteria criteria) {
      int score=10;

      if (criteria != null) {
        if ((criteria.getFacing().isFront() &&
            facing!=CameraCharacteristics.LENS_FACING_FRONT) ||
            (!criteria.getFacing().isFront() &&
                facing!=CameraCharacteristics.LENS_FACING_BACK)) {
          score=0;
        }
      }

      return(score);
    }
  }

  private static class Session extends CameraSession {
    CameraDevice cameraDevice=null;
    CameraCaptureSession captureSession=null;
    CaptureRequest.Builder previewRequestBuilder=null;
    CaptureRequest previewRequest;
    ImageReader reader;
    boolean isClosed=false;

    private Session(Context ctxt, CameraDescriptor descriptor) {
      super(ctxt, descriptor);
    }

    ImageReader buildImageReader() {
      ImageReader result=null;

      for (CameraPlugin plugin : getPlugins()) {
        CameraTwoConfigurator configurator=plugin.buildConfigurator(CameraTwoConfigurator.class);

        if (configurator!=null) {
          result=configurator.buildImageReader();
        }

        if (result!=null) break;
      }

      return(result);
    }

    void addToCaptureRequest(CameraCharacteristics cc,
                             boolean isFacingFront,
                             CaptureRequest.Builder captureBuilder) {
      for (CameraPlugin plugin : getPlugins()) {
        CameraTwoConfigurator configurator=plugin.buildConfigurator(CameraTwoConfigurator.class);

        if (configurator!=null) {
          configurator.addToCaptureRequest(cc, isFacingFront, captureBuilder);
        }
      }
    }

    void addToPreviewRequest(CameraCharacteristics cc,
                             CaptureRequest.Builder captureBuilder) {
      for (CameraPlugin plugin : getPlugins()) {
        CameraTwoConfigurator configurator=plugin.buildConfigurator(CameraTwoConfigurator.class);

        if (configurator!=null) {
          configurator.addToPreviewRequest(cc, captureBuilder);
        }
      }
    }

    boolean isClosed() {
      return(isClosed);
    }

    void setClosed(boolean isClosed) {
      this.isClosed=isClosed;
    }
  }

  private static class SessionBuilder extends CameraSession.Builder {
    private SessionBuilder(Context ctxt, CameraDescriptor descriptor) {
      super(new Session(ctxt, descriptor));
    }
  }

  private static class TakePictureTransaction implements ImageReader.OnImageAvailableListener {
    private final EventBus bus;
    private final PictureTransaction xact;
    private final Context ctxt;

    TakePictureTransaction(Context ctxt, EventBus bus, PictureTransaction xact) {
      this.bus=bus;
      this.xact=xact;
      this.ctxt=ctxt.getApplicationContext();
    }

    @Override
    public void onImageAvailable(ImageReader imageReader) {
      Image image=imageReader.acquireNextImage();
      ByteBuffer buffer=image.getPlanes()[0].getBuffer();
      byte[] bytes=new byte[buffer.remaining()];

      buffer.get(bytes);
      image.close();

      bus.post(new PictureTakenEvent(xact,
        xact.process(new ImageContext(ctxt, bytes))));
    }
  }
}
