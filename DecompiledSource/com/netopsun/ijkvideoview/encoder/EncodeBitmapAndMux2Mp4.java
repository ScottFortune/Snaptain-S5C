package com.netopsun.ijkvideoview.encoder;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLExt;
import android.opengl.EGLSurface;
import android.opengl.GLES20;
import android.os.Build;
import android.util.Log;
import android.view.Surface;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

public class EncodeBitmapAndMux2Mp4 {
  private static int FRAME_RATE = 20;
  
  private static final int IFRAME_INTERVAL = 3;
  
  private static final String MIME_TYPE = "video/avc";
  
  private static final String TAG = "EncodeBitmapAndMux2Mp4";
  
  private static final boolean VERBOSE = false;
  
  private Thread encodeThread;
  
  private String fileName;
  
  private int mBitRate = -1;
  
  private MediaCodec.BufferInfo mBufferInfo;
  
  private MediaCodec mEncoder;
  
  private int mHeight = -1;
  
  private CodecInputSurface mInputSurface;
  
  private MediaMuxer mMuxer;
  
  private boolean mMuxerStarted;
  
  private int mOESTextureID = -1;
  
  protected EGLContext mShareEGLContext = EGL14.EGL_NO_CONTEXT;
  
  private int mTargetTextureID = -1;
  
  private int mTrackIndex;
  
  private int mWidth = -1;
  
  public EncodeBitmapAndMux2Mp4() {
    try {
      this.mEncoder = MediaCodec.createEncoderByType("video/avc");
      return;
    } catch (IOException iOException) {
      throw new RuntimeException("mEncoder creation failed", iOException);
    } 
  }
  
  private static long computePresentationTimeNsec(long paramLong) {
    return paramLong * 1000000L / FRAME_RATE;
  }
  
  private void drainEncoder(boolean paramBoolean) {
    if (paramBoolean)
      this.mEncoder.signalEndOfInputStream(); 
    ByteBuffer[] arrayOfByteBuffer = this.mEncoder.getOutputBuffers();
    while (true) {
      int i = this.mEncoder.dequeueOutputBuffer(this.mBufferInfo, 10000L);
      if (i == -1) {
        if (!paramBoolean)
          continue; 
        continue;
      } 
      if (i == -3) {
        arrayOfByteBuffer = this.mEncoder.getOutputBuffers();
        continue;
      } 
      if (i == -2) {
        if (!this.mMuxerStarted) {
          MediaFormat mediaFormat = this.mEncoder.getOutputFormat();
          StringBuilder stringBuilder1 = new StringBuilder();
          stringBuilder1.append("encoder output format changed: ");
          stringBuilder1.append(mediaFormat);
          Log.e("EncodeBitmapAndMux2Mp4", stringBuilder1.toString());
          this.mTrackIndex = this.mMuxer.addTrack(mediaFormat);
          this.mMuxer.start();
          this.mMuxerStarted = true;
          continue;
        } 
        throw new RuntimeException("format changed twice");
      } 
      if (i < 0) {
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("unexpected result from encoder.dequeueOutputBuffer: ");
        stringBuilder1.append(i);
        Log.w("EncodeBitmapAndMux2Mp4", stringBuilder1.toString());
        continue;
      } 
      ByteBuffer byteBuffer = arrayOfByteBuffer[i];
      if (byteBuffer != null) {
        if ((this.mBufferInfo.flags & 0x2) != 0)
          this.mBufferInfo.size = 0; 
        if (this.mBufferInfo.size != 0)
          if (this.mMuxerStarted) {
            byteBuffer.position(this.mBufferInfo.offset);
            byteBuffer.limit(this.mBufferInfo.offset + this.mBufferInfo.size);
            this.mMuxer.writeSampleData(this.mTrackIndex, byteBuffer, this.mBufferInfo);
          } else {
            throw new RuntimeException("muxer hasn't started");
          }  
        this.mEncoder.releaseOutputBuffer(i, false);
        if ((this.mBufferInfo.flags & 0x4) != 0) {
          if (!paramBoolean)
            Log.w("EncodeBitmapAndMux2Mp4", "reached end of stream unexpectedly"); 
          return;
        } 
        continue;
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("encoderOutputBuffer ");
      stringBuilder.append(i);
      stringBuilder.append(" was null");
      RuntimeException runtimeException = new RuntimeException(stringBuilder.toString());
      throw runtimeException;
    } 
  }
  
  private void generateSurfaceFrame(long paramLong) {
    GLES20.glClear(16640);
    GLBitmap.onDrawFrame(this.mTargetTextureID);
  }
  
  private void prepareEncoder() {
    this.mBufferInfo = new MediaCodec.BufferInfo();
    MediaFormat mediaFormat = MediaFormat.createVideoFormat("video/avc", this.mWidth, this.mHeight);
    mediaFormat.setInteger("color-format", 2130708361);
    mediaFormat.setInteger("bitrate", this.mBitRate);
    mediaFormat.setInteger("frame-rate", FRAME_RATE);
    mediaFormat.setInteger("i-frame-interval", 3);
    this.mEncoder.configure(mediaFormat, null, null, 1);
    this.mInputSurface = new CodecInputSurface(this.mEncoder.createInputSurface(), this.mShareEGLContext);
    this.mEncoder.start();
    String str = (new File(this.fileName)).toString();
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("output file is ");
    stringBuilder.append(str);
    Log.e("EncodeBitmapAndMux2Mp4", stringBuilder.toString());
    try {
      MediaMuxer mediaMuxer = new MediaMuxer();
      this(str, 0);
      this.mMuxer = mediaMuxer;
      this.mTrackIndex = -1;
      this.mMuxerStarted = false;
      return;
    } catch (IOException iOException) {
      throw new RuntimeException("MediaMuxer creation failed", iOException);
    } 
  }
  
  private void releaseEncoder() {
    this.encodeThread = null;
    CodecInputSurface codecInputSurface = this.mInputSurface;
    if (codecInputSurface != null) {
      codecInputSurface.release();
      this.mInputSurface = null;
    } 
    MediaCodec mediaCodec = this.mEncoder;
    if (mediaCodec != null) {
      mediaCodec.stop();
      if (Build.VERSION.SDK_INT >= 21) {
        this.mEncoder.reset();
      } else {
        this.mEncoder.release();
        try {
          this.mEncoder = MediaCodec.createEncoderByType("video/avc");
        } catch (IOException iOException) {
          throw new RuntimeException("mEncoder creation failed", iOException);
        } 
      } 
    } 
    MediaMuxer mediaMuxer = this.mMuxer;
    if (mediaMuxer != null) {
      try {
        mediaMuxer.stop();
      } catch (Exception exception) {
        exception.printStackTrace();
      } 
      this.mMuxer.release();
      this.mMuxer = null;
    } 
  }
  
  protected void finalize() throws Throwable {
    this.mEncoder.release();
    super.finalize();
  }
  
  public void setOPENGLContext(EGLContext paramEGLContext, int paramInt) {
    this.mShareEGLContext = paramEGLContext;
    this.mTargetTextureID = paramInt;
  }
  
  public void startEncode(final int mWidth, final int mHeight, int paramInt3, int paramInt4, String paramString, final EncodeStatusCallback encodeStatusCallback) {
    FRAME_RATE = paramInt4;
    if (this.encodeThread != null || this.mMuxer != null) {
      encodeStatusCallback.onEncodingProcessNotFinishErro();
      return;
    } 
    this.mWidth = mWidth;
    this.mHeight = mHeight;
    this.mBitRate = paramInt3;
    this.fileName = paramString;
    this.encodeThread = new Thread(new Runnable() {
          long frameIndex = 0L;
          
          public void run() {
            try {
              EncodeBitmapAndMux2Mp4.this.prepareEncoder();
              EncodeBitmapAndMux2Mp4.this.mInputSurface.makeCurrent();
              GLBitmap.init();
              GLES20.glViewport(0, 0, mWidth, mHeight);
              while (!Thread.currentThread().isInterrupted()) {
                long l1 = System.currentTimeMillis();
                EncodeBitmapAndMux2Mp4.this.drainEncoder(false);
                EncodeBitmapAndMux2Mp4.this.generateSurfaceFrame(this.frameIndex);
                EncodeBitmapAndMux2Mp4.this.mInputSurface.setPresentationTime(EncodeBitmapAndMux2Mp4.computePresentationTimeNsec(this.frameIndex));
                EncodeBitmapAndMux2Mp4.this.mInputSurface.swapBuffers();
                this.frameIndex += 1000L;
                long l2 = (1000 / EncodeBitmapAndMux2Mp4.FRAME_RATE);
                long l3 = System.currentTimeMillis();
                l2 -= l3 - l1;
                if (l2 > 0L) {
                  try {
                    Thread.sleep(l2);
                  } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                    Thread.currentThread().interrupt();
                  } 
                  continue;
                } 
                double d1 = this.frameIndex;
                double d2 = Math.abs(l2);
                int i = EncodeBitmapAndMux2Mp4.FRAME_RATE;
                double d3 = i;
                Double.isNaN(d3);
                d3 = 1000.0D / d3;
                Double.isNaN(d2);
                d2 /= d3;
                Double.isNaN(d1);
                l2 = (long)(d1 + d2 * 1000.0D);
                this.frameIndex = l2;
              } 
              EncodeBitmapAndMux2Mp4.this.drainEncoder(true);
            } catch (Exception exception) {
              exception.printStackTrace();
              encodeStatusCallback.onEncoderErro(exception.toString());
            } finally {
              Exception exception;
            } 
            EncodeBitmapAndMux2Mp4.this.releaseEncoder();
            encodeStatusCallback.onFinish();
          }
        });
    this.encodeThread.start();
  }
  
  public void stopEncode() {
    Thread thread = this.encodeThread;
    if (thread != null) {
      thread.interrupt();
      this.encodeThread = null;
    } 
  }
  
  private static class CodecInputSurface {
    private static final int EGL_RECORDABLE_ANDROID = 12610;
    
    private EGLContext mEGLContext = EGL14.EGL_NO_CONTEXT;
    
    private EGLDisplay mEGLDisplay = EGL14.EGL_NO_DISPLAY;
    
    private EGLSurface mEGLSurface = EGL14.EGL_NO_SURFACE;
    
    private Surface mSurface;
    
    public CodecInputSurface(Surface param1Surface, EGLContext param1EGLContext) {
      if (param1Surface != null) {
        this.mSurface = param1Surface;
        eglSetup(param1EGLContext);
        return;
      } 
      throw new NullPointerException();
    }
    
    private void checkEglError(String param1String) {
      int i = EGL14.eglGetError();
      if (i == 12288)
        return; 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(param1String);
      stringBuilder.append(": EGL error: 0x");
      stringBuilder.append(Integer.toHexString(i));
      throw new RuntimeException(stringBuilder.toString());
    }
    
    private void eglSetup(EGLContext param1EGLContext) {
      this.mEGLDisplay = EGL14.eglGetDisplay(0);
      if (this.mEGLDisplay != EGL14.EGL_NO_DISPLAY) {
        int[] arrayOfInt = new int[2];
        if (EGL14.eglInitialize(this.mEGLDisplay, arrayOfInt, 0, arrayOfInt, 1)) {
          EGLConfig[] arrayOfEGLConfig = new EGLConfig[1];
          arrayOfInt = new int[1];
          EGLDisplay eGLDisplay = this.mEGLDisplay;
          int i = arrayOfEGLConfig.length;
          EGL14.eglChooseConfig(eGLDisplay, new int[] { 
                12324, 8, 12323, 8, 12322, 8, 12321, 8, 12352, 4, 
                12610, 1, 12344 }, 0, arrayOfEGLConfig, 0, i, arrayOfInt, 0);
          checkEglError("eglCreateContext RGB888+recordable ES2");
          this.mEGLContext = EGL14.eglCreateContext(this.mEGLDisplay, arrayOfEGLConfig[0], param1EGLContext, new int[] { 12440, 2, 12344 }, 0);
          checkEglError("eglCreateContext");
          this.mEGLSurface = EGL14.eglCreateWindowSurface(this.mEGLDisplay, arrayOfEGLConfig[0], this.mSurface, new int[] { 12344 }, 0);
          checkEglError("eglCreateWindowSurface");
          return;
        } 
        throw new RuntimeException("unable to initialize EGL14");
      } 
      throw new RuntimeException("unable to get EGL14 display");
    }
    
    public void makeCurrent() {
      EGLDisplay eGLDisplay = this.mEGLDisplay;
      EGLSurface eGLSurface = this.mEGLSurface;
      EGL14.eglMakeCurrent(eGLDisplay, eGLSurface, eGLSurface, this.mEGLContext);
      checkEglError("eglMakeCurrent");
    }
    
    public void release() {
      if (this.mEGLDisplay != EGL14.EGL_NO_DISPLAY) {
        EGL14.eglMakeCurrent(this.mEGLDisplay, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_CONTEXT);
        EGL14.eglDestroySurface(this.mEGLDisplay, this.mEGLSurface);
        EGL14.eglDestroyContext(this.mEGLDisplay, this.mEGLContext);
        EGL14.eglReleaseThread();
        EGL14.eglTerminate(this.mEGLDisplay);
      } 
      this.mSurface.release();
      this.mEGLDisplay = EGL14.EGL_NO_DISPLAY;
      this.mEGLContext = EGL14.EGL_NO_CONTEXT;
      this.mEGLSurface = EGL14.EGL_NO_SURFACE;
      this.mSurface = null;
    }
    
    public void setPresentationTime(long param1Long) {
      EGLExt.eglPresentationTimeANDROID(this.mEGLDisplay, this.mEGLSurface, param1Long);
      checkEglError("eglPresentationTimeANDROID");
    }
    
    public boolean swapBuffers() {
      boolean bool = EGL14.eglSwapBuffers(this.mEGLDisplay, this.mEGLSurface);
      checkEglError("eglSwapBuffers");
      return bool;
    }
  }
  
  public static interface EncodeStatusCallback {
    void onEncoderErro(String param1String);
    
    void onEncodingProcessNotFinishErro();
    
    void onFinish();
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/ijkvideoview/encoder/EncodeBitmapAndMux2Mp4.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */