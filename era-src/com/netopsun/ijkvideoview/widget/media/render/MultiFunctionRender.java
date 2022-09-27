package com.netopsun.ijkvideoview.widget.media.render;

import android.opengl.GLES20;
import com.netopsun.ijkvideoview.extra.filter_choose_popupwindows.filter.MyGPUImageFilterGroup;
import com.yang.firework.ParticleLayer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.util.Rotation;
import jp.co.cyberagent.android.gpuimage.util.TextureRotationUtil;

public class MultiFunctionRender implements MultiFunctionRenderRuntimeEnvironment.Renderer {
  public static final float[] CUBE = new float[] { -1.0F, -1.0F, 1.0F, -1.0F, -1.0F, 1.0F, 1.0F, 1.0F };
  
  int callbackIntervalFramesCount = 0;
  
  private int callbackTextureSize;
  
  private int captureTextureSize;
  
  private int[] fboForCapture;
  
  private int[] fboForFrameCallback;
  
  private volatile FHFishEyeDrawingHelper fhFishEyeDrawingHelper;
  
  private volatile Runnable fhFishEyeModeInitEvent;
  
  private GPUImageFilter filter = new GPUImageFilter();
  
  private int frameBuffersID = -1;
  
  private FloatBuffer glCubeBuffer;
  
  private FloatBuffer glTextureBufferFlip;
  
  private int inputOESTextureID;
  
  private float[] inputOESTextureMtx = new float[16];
  
  private volatile boolean isFHFishEyeMode = false;
  
  private volatile boolean isVRMode = false;
  
  private volatile boolean keepVideoRatio = false;
  
  private ParticleLayer mParticleSystem;
  
  private int mScreenHeight;
  
  private int mScreenWidth;
  
  private int mVideoHeight;
  
  private int mVideoWidth;
  
  public double magnification = 1.0D;
  
  private OESTextureDrawingHelper oesTextureDrawingHelper;
  
  private OnFrameBufferCallback onFrameCallback;
  
  private ByteBuffer onFrameCallbackBuf;
  
  private Lock onFrameCallbackLock = new ReentrantLock();
  
  private int processedTextureBufferSize;
  
  private volatile int processedTextureID = -1;
  
  private int rgbaTextureID = -1;
  
  private TextureDrawingHelper textureDrawingHelper;
  
  private int[] textureForCapture;
  
  private int[] textureForFrameCallback;
  
  public void checkIfFrameByteNeedCallback() {
    if (this.onFrameCallback == null)
      return; 
    this.callbackIntervalFramesCount--;
    if (this.callbackIntervalFramesCount > 0)
      return; 
    this.onFrameCallbackBuf.rewind();
    this.onFrameCallbackLock.lock();
    try {
      if (this.onFrameCallback != null) {
        drawOriginalTextureToByteBuffer(this.onFrameCallbackBuf, this.onFrameCallback.width, this.onFrameCallback.height);
        this.callbackIntervalFramesCount = this.onFrameCallback.onFrameBuffer(this.onFrameCallbackBuf);
      } 
      this.onFrameCallbackLock.unlock();
    } catch (Exception exception) {
      exception.printStackTrace();
      this.onFrameCallbackLock.unlock();
    } finally {
      Exception exception;
    } 
  }
  
  public void drawOriginalTextureToByteBuffer(ByteBuffer paramByteBuffer, int paramInt1, int paramInt2) {
    GLES20.glBindTexture(36197, this.inputOESTextureID);
    if (this.fboForFrameCallback == null) {
      this.fboForFrameCallback = new int[1];
      GLES20.glGenFramebuffers(1, this.fboForFrameCallback, 0);
    } 
    if (this.textureForFrameCallback == null) {
      this.textureForFrameCallback = new int[1];
      GLES20.glGenTextures(1, this.textureForFrameCallback, 0);
    } 
    int i = this.callbackTextureSize;
    int j = paramInt1 * paramInt2;
    if (i != j) {
      GLES20.glBindTexture(3553, this.textureForFrameCallback[0]);
      GLES20.glTexImage2D(3553, 0, 6408, paramInt1, paramInt2, 0, 6408, 5121, null);
      this.callbackTextureSize = j;
    } 
    GLES20.glBindFramebuffer(36160, this.fboForFrameCallback[0]);
    GLES20.glBindTexture(3553, this.textureForFrameCallback[0]);
    GLES20.glFramebufferTexture2D(36160, 36064, 3553, this.textureForFrameCallback[0], 0);
    this.oesTextureDrawingHelper.drawOESTexture(this.inputOESTextureMtx, this.inputOESTextureID, paramInt1, paramInt2);
    GLES20.glReadPixels(0, 0, paramInt1, paramInt2, 6408, 5121, paramByteBuffer);
    GLES20.glBindFramebuffer(36160, 0);
  }
  
  public void drawProcessedTextureToByteBuffer(ByteBuffer paramByteBuffer, int paramInt1, int paramInt2) {
    GLES20.glBindTexture(3553, this.processedTextureID);
    if (this.fboForCapture == null) {
      this.fboForCapture = new int[1];
      GLES20.glGenFramebuffers(1, this.fboForCapture, 0);
    } 
    if (this.textureForCapture == null) {
      this.textureForCapture = new int[1];
      GLES20.glGenTextures(1, this.textureForCapture, 0);
    } 
    int i = this.captureTextureSize;
    int j = paramInt1 * paramInt2;
    if (i != j) {
      GLES20.glBindTexture(3553, this.textureForCapture[0]);
      GLES20.glTexImage2D(3553, 0, 6408, paramInt1, paramInt2, 0, 6408, 5121, null);
      this.captureTextureSize = j;
    } 
    GLES20.glBindFramebuffer(36160, this.fboForCapture[0]);
    GLES20.glBindTexture(3553, this.textureForCapture[0]);
    GLES20.glFramebufferTexture2D(36160, 36064, 3553, this.textureForCapture[0], 0);
    GLES20.glViewport(0, 0, paramInt1, paramInt2);
    this.textureDrawingHelper.draw(this.processedTextureID);
    GLES20.glReadPixels(0, 0, paramInt1, paramInt2, 6408, 5121, paramByteBuffer);
    GLES20.glBindFramebuffer(36160, 0);
  }
  
  public FHFishEyeDrawingHelper getFhFishEyeDrawingHelper() {
    return this.fhFishEyeDrawingHelper;
  }
  
  public int getProcessedTextureId() {
    return this.processedTextureID;
  }
  
  public boolean isFHFishEyeMode() {
    return this.isFHFishEyeMode;
  }
  
  public boolean isVRMode() {
    return this.isVRMode;
  }
  
  public void onDrawFrame(GL10 paramGL10, int paramInt, float[] paramArrayOffloat) {
    this.inputOESTextureID = paramInt;
    this.inputOESTextureMtx = paramArrayOffloat;
    int i = this.mVideoWidth;
    int j = this.mVideoHeight;
    int k = this.mScreenWidth;
    int m = i;
    int n = j;
    if (i > k) {
      int i1 = this.mScreenHeight;
      m = i;
      n = j;
      if (j > i1) {
        m = k;
        n = i1;
      } 
    } 
    GLES20.glBindTexture(36197, paramInt);
    this.oesTextureDrawingHelper.drawOESTexture2RGBTexture(paramArrayOffloat, paramInt, this.rgbaTextureID, this.mParticleSystem, m, n, (float)this.magnification);
    GLES20.glBindFramebuffer(36160, this.frameBuffersID);
    prepareProcessedTextureBuffer(m, n);
    this.filter.ifNeedInit();
    GLES20.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
    GLES20.glClear(16384);
    GLES20.glViewport(0, 0, m, n);
    GPUImageFilter gPUImageFilter = this.filter;
    if (gPUImageFilter instanceof MyGPUImageFilterGroup)
      ((MyGPUImageFilterGroup)gPUImageFilter).setFboFrameBuffer(this.frameBuffersID); 
    this.filter.onDraw(this.rgbaTextureID, this.glCubeBuffer, this.glTextureBufferFlip);
    GLES20.glBindTexture(3553, 0);
    GLES20.glBindFramebuffer(36160, 0);
    if (this.isFHFishEyeMode) {
      if (this.fhFishEyeDrawingHelper == null) {
        this.fhFishEyeDrawingHelper = new FHFishEyeDrawingHelper();
        this.fhFishEyeDrawingHelper.onSurfaceCreated(paramGL10, null);
        this.fhFishEyeDrawingHelper.onSurfaceChanged(paramGL10, this.mScreenWidth, this.mScreenHeight);
      } 
      Runnable runnable = this.fhFishEyeModeInitEvent;
      this.fhFishEyeModeInitEvent = null;
      if (runnable != null)
        runnable.run(); 
      this.fhFishEyeDrawingHelper.onDrawFrame(paramGL10, this.processedTextureID, m, n);
    } else {
      GLES20.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
      GLES20.glClear(16384);
      if (this.isVRMode) {
        if (this.keepVideoRatio) {
          int i1 = this.mVideoWidth;
          float f1 = i1;
          m = this.mVideoHeight;
          f1 = f1 * 1.0F / m;
          n = this.mScreenWidth;
          float f2 = n / 2.0F;
          paramInt = this.mScreenHeight;
          if (f1 > f2 / paramInt) {
            m = (int)(n / 2.0F * m * 1.0F / i1);
            GLES20.glViewport(0, (paramInt - m) / 2, n / 2, m);
          } else {
            m = (int)(paramInt * i1 / 2.0F / m);
            GLES20.glViewport((n / 2 - m) / 2, 0, m, paramInt);
          } 
        } else {
          paramInt = this.mScreenHeight;
          GLES20.glViewport(0, paramInt / 4, this.mScreenWidth / 2, paramInt / 2);
        } 
        this.textureDrawingHelper.draw(this.processedTextureID);
        if (this.keepVideoRatio) {
          m = this.mVideoWidth;
          float f1 = m;
          int i1 = this.mVideoHeight;
          float f2 = f1 * 1.0F / i1;
          n = this.mScreenWidth;
          f1 = n / 2.0F;
          paramInt = this.mScreenHeight;
          if (f2 > f1 / paramInt) {
            m = (int)(n / 2.0F * i1 * 1.0F / m);
            GLES20.glViewport(n / 2, (paramInt - m) / 2, n / 2, m);
          } else {
            m = (int)(paramInt * m / 2.0F / i1);
            GLES20.glViewport(n / 2 + (n / 2 - m) / 2, 0, m, paramInt);
          } 
        } else {
          paramInt = this.mScreenWidth;
          m = paramInt / 2;
          n = this.mScreenHeight;
          GLES20.glViewport(m, n / 4, paramInt / 2, n / 2);
        } 
        this.textureDrawingHelper.draw(this.processedTextureID);
      } else {
        if (this.keepVideoRatio) {
          m = this.mVideoWidth;
          float f1 = m;
          int i1 = this.mVideoHeight;
          float f2 = f1 * 1.0F / i1;
          n = this.mScreenWidth;
          f1 = n;
          paramInt = this.mScreenHeight;
          if (f2 > f1 * 1.0F / paramInt) {
            m = (int)(n * i1 * 1.0F / m);
            GLES20.glViewport(0, (paramInt - m) / 2, n, m);
          } else {
            m = (int)(paramInt * m * 1.0F / i1);
            GLES20.glViewport((n - m) / 2, 0, m, paramInt);
          } 
        } else {
          GLES20.glViewport(0, 0, this.mScreenWidth, this.mScreenHeight);
        } 
        this.textureDrawingHelper.draw(this.processedTextureID);
      } 
    } 
    checkIfFrameByteNeedCallback();
  }
  
  public void onSurfaceChanged(GL10 paramGL10, int paramInt1, int paramInt2) {
    if (this.mVideoWidth == 0) {
      this.mVideoWidth = paramInt1;
      this.mVideoHeight = paramInt2;
    } 
    this.mScreenWidth = paramInt1;
    this.mScreenHeight = paramInt2;
    this.filter.onOutputSizeChanged(this.mScreenWidth, this.mScreenHeight);
    if (this.fhFishEyeDrawingHelper != null)
      this.fhFishEyeDrawingHelper.onSurfaceChanged(paramGL10, paramInt1, paramInt2); 
  }
  
  public void onSurfaceCreated(GL10 paramGL10, EGLConfig paramEGLConfig) {
    if (this.textureDrawingHelper == null)
      this.textureDrawingHelper = new TextureDrawingHelper(); 
    if (this.oesTextureDrawingHelper == null)
      this.oesTextureDrawingHelper = new OESTextureDrawingHelper(); 
    if (this.rgbaTextureID == -1) {
      int[] arrayOfInt = new int[1];
      GLES20.glGenTextures(1, arrayOfInt, 0);
      this.rgbaTextureID = arrayOfInt[0];
    } 
    if (this.processedTextureID == -1) {
      int[] arrayOfInt = new int[1];
      GLES20.glGenTextures(1, arrayOfInt, 0);
      this.processedTextureID = arrayOfInt[0];
    } 
    if (this.frameBuffersID == -1) {
      int[] arrayOfInt = new int[1];
      GLES20.glGenFramebuffers(1, arrayOfInt, 0);
      this.frameBuffersID = arrayOfInt[0];
    } 
    this.glCubeBuffer = ByteBuffer.allocateDirect(CUBE.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
    this.glCubeBuffer.put(CUBE).position(0);
    this.glTextureBufferFlip = ByteBuffer.allocateDirect(32).order(ByteOrder.nativeOrder()).asFloatBuffer();
    float[] arrayOfFloat = TextureRotationUtil.getRotation(Rotation.NORMAL, false, false);
    this.glTextureBufferFlip.put(arrayOfFloat).position(0);
  }
  
  protected void prepareProcessedTextureBuffer(int paramInt1, int paramInt2) {
    int i = this.processedTextureBufferSize;
    int j = paramInt1 * paramInt2 * 4;
    if (i != j) {
      GLES20.glBindTexture(3553, this.processedTextureID);
      GLES20.glTexImage2D(3553, 0, 6408, paramInt1, paramInt2, 0, 6408, 5121, null);
      GLES20.glTexParameterf(3553, 10240, 9729.0F);
      GLES20.glTexParameterf(3553, 10241, 9729.0F);
      GLES20.glTexParameterf(3553, 10242, 33071.0F);
      GLES20.glTexParameterf(3553, 10243, 33071.0F);
      GLES20.glFramebufferTexture2D(36160, 36064, 3553, this.processedTextureID, 0);
      this.processedTextureBufferSize = j;
    } 
  }
  
  public void setFHFishEyeMode(boolean paramBoolean, Runnable paramRunnable) {
    this.fhFishEyeModeInitEvent = paramRunnable;
    this.isFHFishEyeMode = paramBoolean;
  }
  
  public void setFilter(GPUImageFilter paramGPUImageFilter) {
    this.filter.destroy();
    this.filter = paramGPUImageFilter;
    int i = this.mVideoWidth;
    int j = this.mVideoHeight;
    int k = this.mScreenWidth;
    int m = j;
    int n = i;
    if (i > k) {
      int i1 = this.mScreenHeight;
      m = j;
      n = i;
      if (j > i1) {
        n = k;
        m = i1;
      } 
    } 
    this.filter.onOutputSizeChanged(n, m);
  }
  
  public void setKeepVideoRatio(boolean paramBoolean) {
    this.keepVideoRatio = paramBoolean;
  }
  
  public void setMagnification(double paramDouble) {
    this.magnification = paramDouble;
  }
  
  public void setOnFrameByteBufferCallback(OnFrameBufferCallback paramOnFrameBufferCallback, int paramInt1, int paramInt2) {
    ByteBuffer byteBuffer = this.onFrameCallbackBuf;
    if (byteBuffer == null || byteBuffer.capacity() < paramInt1 * paramInt2 * 4)
      this.onFrameCallbackBuf = ByteBuffer.allocate(paramInt1 * paramInt2 * 4); 
    this.onFrameCallbackLock.lock();
    this.onFrameCallback = paramOnFrameBufferCallback;
    paramOnFrameBufferCallback = this.onFrameCallback;
    if (paramOnFrameBufferCallback != null) {
      paramOnFrameBufferCallback.height = paramInt2;
      paramOnFrameBufferCallback.width = paramInt1;
    } 
    this.onFrameCallbackLock.unlock();
  }
  
  public void setParticleSystem(ParticleLayer paramParticleLayer) {
    this.mParticleSystem = paramParticleLayer;
  }
  
  public void setVRMode(boolean paramBoolean) {
    this.isVRMode = paramBoolean;
  }
  
  public void setVideoSize(int paramInt1, int paramInt2) {
    this.mVideoWidth = paramInt1;
    this.mVideoHeight = paramInt2;
  }
  
  public static abstract class OnFrameBufferCallback {
    public int height = 225;
    
    public int width = 300;
    
    public abstract int onFrameBuffer(ByteBuffer param1ByteBuffer);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/ijkvideoview/widget/media/render/MultiFunctionRender.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */