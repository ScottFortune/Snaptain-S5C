package com.netopsun.ijkvideoview.widget.media.render;

import android.graphics.SurfaceTexture;
import android.opengl.EGL14;
import android.opengl.EGLContext;
import android.opengl.GLDebugHelper;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import java.io.Writer;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL10;

public class MultiFunctionRenderRuntimeEnvironment {
  public static final int DEBUG_CHECK_GL_ERROR = 1;
  
  public static final int DEBUG_LOG_GL_CALLS = 2;
  
  private static final boolean LOG_ATTACH_DETACH = false;
  
  private static final boolean LOG_EGL = false;
  
  private static final boolean LOG_PAUSE_RESUME = false;
  
  private static final boolean LOG_RENDERER = false;
  
  private static final boolean LOG_RENDERER_DRAW_FRAME = false;
  
  private static final boolean LOG_SURFACE = false;
  
  private static final boolean LOG_THREADS = false;
  
  public static final int RENDERMODE_CONTINUOUSLY = 1;
  
  public static final int RENDERMODE_WHEN_DIRTY = 0;
  
  private static final String TAG = "MultiFuncEnv";
  
  private static final GLThreadManager sGLThreadManager = new GLThreadManager();
  
  private int defaultRenderMode = 1;
  
  private Surface displaySurface;
  
  private int mDebugFlags;
  
  private boolean mDetached;
  
  private EGLConfigChooser mEGLConfigChooser;
  
  private int mEGLContextClientVersion;
  
  private EGLContextFactory mEGLContextFactory;
  
  private EGLWindowSurfaceFactory mEGLWindowSurfaceFactory;
  
  private GLThread mGLThread;
  
  private GLWrapper mGLWrapper;
  
  private boolean mPreserveEGLContextOnPause;
  
  private Renderer mRenderer;
  
  private final WeakReference<MultiFunctionRenderRuntimeEnvironment> mThisWeakRef = new WeakReference<MultiFunctionRenderRuntimeEnvironment>(this);
  
  private void checkRenderThreadState() {
    if (this.mGLThread == null)
      return; 
    throw new IllegalStateException("setRenderer has already been called for this instance.");
  }
  
  private Surface getHolder() {
    return this.displaySurface;
  }
  
  protected void finalize() throws Throwable {
    try {
      if (this.mGLThread != null)
        this.mGLThread.requestExitAndWait(); 
      return;
    } finally {
      super.finalize();
    } 
  }
  
  public int getDebugFlags() {
    return this.mDebugFlags;
  }
  
  public EGLContext getEGLContext_14() {
    GLThread gLThread = this.mGLThread;
    return (gLThread == null) ? null : ((gLThread.mEglHelper == null) ? null : this.mGLThread.mEglHelper.mEglContext_14);
  }
  
  public boolean getPreserveEGLContextOnPause() {
    return this.mPreserveEGLContextOnPause;
  }
  
  public int getRenderMode() {
    return this.mGLThread.getRenderMode();
  }
  
  public void onAttachedToWindow() {
    if (this.mDetached && this.mRenderer != null) {
      boolean bool;
      GLThread gLThread = this.mGLThread;
      if (gLThread != null) {
        bool = gLThread.getRenderMode();
      } else {
        bool = true;
      } 
      this.mGLThread = new GLThread(this.mThisWeakRef);
      if (bool != true)
        this.mGLThread.setRenderMode(bool); 
      this.mGLThread.start();
    } 
    this.mDetached = false;
  }
  
  public void onDetachedFromWindow() {
    GLThread gLThread = this.mGLThread;
    if (gLThread != null)
      gLThread.requestExitAndWait(); 
    this.mDetached = true;
  }
  
  public void onPause() {
    this.mGLThread.onPause();
  }
  
  public void onResume() {
    this.mGLThread.onResume();
  }
  
  public void queueEvent(Runnable paramRunnable) {
    this.mGLThread.queueEvent(paramRunnable);
  }
  
  public void requestRender() {
    this.mGLThread.requestRender();
  }
  
  public void setDebugFlags(int paramInt) {
    this.mDebugFlags = paramInt;
  }
  
  public void setEGLConfigChooser(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6) {
    setEGLConfigChooser(new ComponentSizeChooser(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6));
  }
  
  public void setEGLConfigChooser(EGLConfigChooser paramEGLConfigChooser) {
    checkRenderThreadState();
    this.mEGLConfigChooser = paramEGLConfigChooser;
  }
  
  public void setEGLConfigChooser(boolean paramBoolean) {
    setEGLConfigChooser(new SimpleEGLConfigChooser(paramBoolean));
  }
  
  public void setEGLContextClientVersion(int paramInt) {
    checkRenderThreadState();
    this.mEGLContextClientVersion = paramInt;
  }
  
  public void setEGLContextFactory(EGLContextFactory paramEGLContextFactory) {
    checkRenderThreadState();
    this.mEGLContextFactory = paramEGLContextFactory;
  }
  
  public void setEGLWindowSurfaceFactory(EGLWindowSurfaceFactory paramEGLWindowSurfaceFactory) {
    checkRenderThreadState();
    this.mEGLWindowSurfaceFactory = paramEGLWindowSurfaceFactory;
  }
  
  public void setGLWrapper(GLWrapper paramGLWrapper) {
    this.mGLWrapper = paramGLWrapper;
  }
  
  public void setOnInputSurfaceTextureCallback(final GLThread.OnInputSurfaceTextureAvailableCallback callback) {
    queueEvent(new Runnable() {
          public void run() {
            if (MultiFunctionRenderRuntimeEnvironment.this.mGLThread.mEglHelper != null && MultiFunctionRenderRuntimeEnvironment.this.mGLThread.inputSurfaceTexture != null)
              callback.onInputSurfaceTextureAvailable(MultiFunctionRenderRuntimeEnvironment.this.mGLThread.inputSurfaceTexture, MultiFunctionRenderRuntimeEnvironment.this.mGLThread.mWidth, MultiFunctionRenderRuntimeEnvironment.this.mGLThread.mHeight); 
            MultiFunctionRenderRuntimeEnvironment.this.mGLThread.setOnInputSurfaceTextureCallback(callback);
          }
        });
  }
  
  public void setPreserveEGLContextOnPause(boolean paramBoolean) {
    this.mPreserveEGLContextOnPause = paramBoolean;
  }
  
  public void setRenderMode(int paramInt) {
    this.defaultRenderMode = paramInt;
    GLThread gLThread = this.mGLThread;
    if (gLThread != null)
      gLThread.setRenderMode(paramInt); 
  }
  
  public void setRenderer(Renderer paramRenderer) {
    checkRenderThreadState();
    if (this.mEGLConfigChooser == null)
      this.mEGLConfigChooser = new SimpleEGLConfigChooser(true); 
    if (this.mEGLContextFactory == null)
      this.mEGLContextFactory = new DefaultContextFactory(); 
    if (this.mEGLWindowSurfaceFactory == null)
      this.mEGLWindowSurfaceFactory = new DefaultWindowSurfaceFactory(); 
    this.mRenderer = paramRenderer;
    this.mGLThread = new GLThread(this.mThisWeakRef);
    this.mGLThread.start();
  }
  
  public void surfaceChanged(Surface paramSurface, int paramInt1, int paramInt2) {
    this.displaySurface = paramSurface;
    this.mGLThread.onWindowResize(paramInt1, paramInt2);
  }
  
  public void surfaceCreated(Surface paramSurface) {
    this.displaySurface = paramSurface;
    this.mGLThread.surfaceCreated();
  }
  
  public void surfaceDestroyed(Surface paramSurface) {
    this.displaySurface = paramSurface;
    this.mGLThread.surfaceDestroyed();
  }
  
  public void surfaceRedrawNeeded(SurfaceHolder paramSurfaceHolder) {}
  
  public void surfaceRedrawNeededAsync(SurfaceHolder paramSurfaceHolder, Runnable paramRunnable) {
    GLThread gLThread = this.mGLThread;
    if (gLThread != null)
      gLThread.requestRenderAndNotify(paramRunnable); 
  }
  
  private abstract class BaseConfigChooser implements EGLConfigChooser {
    protected int[] mConfigSpec;
    
    public BaseConfigChooser(int[] param1ArrayOfint) {
      this.mConfigSpec = filterConfigSpec(param1ArrayOfint);
    }
    
    private int[] filterConfigSpec(int[] param1ArrayOfint) {
      if (MultiFunctionRenderRuntimeEnvironment.this.mEGLContextClientVersion != 2 && MultiFunctionRenderRuntimeEnvironment.this.mEGLContextClientVersion != 3)
        return param1ArrayOfint; 
      int i = param1ArrayOfint.length;
      int[] arrayOfInt = new int[i + 2];
      int j = i - 1;
      System.arraycopy(param1ArrayOfint, 0, arrayOfInt, 0, j);
      arrayOfInt[j] = 12352;
      if (MultiFunctionRenderRuntimeEnvironment.this.mEGLContextClientVersion == 2) {
        arrayOfInt[i] = 4;
      } else {
        arrayOfInt[i] = 64;
      } 
      arrayOfInt[i + 1] = 12344;
      return arrayOfInt;
    }
    
    public EGLConfig chooseConfig(EGL10 param1EGL10, EGLDisplay param1EGLDisplay) {
      int[] arrayOfInt = new int[1];
      if (param1EGL10.eglChooseConfig(param1EGLDisplay, this.mConfigSpec, null, 0, arrayOfInt)) {
        int i = arrayOfInt[0];
        if (i > 0) {
          EGLConfig[] arrayOfEGLConfig = new EGLConfig[i];
          if (param1EGL10.eglChooseConfig(param1EGLDisplay, this.mConfigSpec, arrayOfEGLConfig, i, arrayOfInt)) {
            EGLConfig eGLConfig = chooseConfig(param1EGL10, param1EGLDisplay, arrayOfEGLConfig);
            if (eGLConfig != null)
              return eGLConfig; 
            throw new IllegalArgumentException("No config chosen");
          } 
          throw new IllegalArgumentException("eglChooseConfig#2 failed");
        } 
        throw new IllegalArgumentException("No configs match configSpec");
      } 
      throw new IllegalArgumentException("eglChooseConfig failed");
    }
    
    abstract EGLConfig chooseConfig(EGL10 param1EGL10, EGLDisplay param1EGLDisplay, EGLConfig[] param1ArrayOfEGLConfig);
  }
  
  private class ComponentSizeChooser extends BaseConfigChooser {
    protected int mAlphaSize;
    
    protected int mBlueSize;
    
    protected int mDepthSize;
    
    protected int mGreenSize;
    
    protected int mRedSize;
    
    protected int mStencilSize;
    
    private int[] mValue = new int[1];
    
    public ComponentSizeChooser(int param1Int1, int param1Int2, int param1Int3, int param1Int4, int param1Int5, int param1Int6) {
      super(new int[] { 
            12324, param1Int1, 12323, param1Int2, 12322, param1Int3, 12321, param1Int4, 12325, param1Int5, 
            12326, param1Int6, 12344 });
      this.mRedSize = param1Int1;
      this.mGreenSize = param1Int2;
      this.mBlueSize = param1Int3;
      this.mAlphaSize = param1Int4;
      this.mDepthSize = param1Int5;
      this.mStencilSize = param1Int6;
    }
    
    private int findConfigAttrib(EGL10 param1EGL10, EGLDisplay param1EGLDisplay, EGLConfig param1EGLConfig, int param1Int1, int param1Int2) {
      return param1EGL10.eglGetConfigAttrib(param1EGLDisplay, param1EGLConfig, param1Int1, this.mValue) ? this.mValue[0] : param1Int2;
    }
    
    public EGLConfig chooseConfig(EGL10 param1EGL10, EGLDisplay param1EGLDisplay, EGLConfig[] param1ArrayOfEGLConfig) {
      int i = param1ArrayOfEGLConfig.length;
      for (byte b = 0; b < i; b++) {
        EGLConfig eGLConfig = param1ArrayOfEGLConfig[b];
        int j = findConfigAttrib(param1EGL10, param1EGLDisplay, eGLConfig, 12325, 0);
        int k = findConfigAttrib(param1EGL10, param1EGLDisplay, eGLConfig, 12326, 0);
        if (j >= this.mDepthSize && k >= this.mStencilSize) {
          int m = findConfigAttrib(param1EGL10, param1EGLDisplay, eGLConfig, 12324, 0);
          j = findConfigAttrib(param1EGL10, param1EGLDisplay, eGLConfig, 12323, 0);
          k = findConfigAttrib(param1EGL10, param1EGLDisplay, eGLConfig, 12322, 0);
          int n = findConfigAttrib(param1EGL10, param1EGLDisplay, eGLConfig, 12321, 0);
          if (m == this.mRedSize && j == this.mGreenSize && k == this.mBlueSize && n == this.mAlphaSize)
            return eGLConfig; 
        } 
      } 
      return null;
    }
  }
  
  private class DefaultContextFactory implements EGLContextFactory {
    private int EGL_CONTEXT_CLIENT_VERSION = 12440;
    
    private DefaultContextFactory() {}
    
    public EGLContext createContext(EGL10 param1EGL10, EGLDisplay param1EGLDisplay, EGLConfig param1EGLConfig) {
      int[] arrayOfInt = new int[3];
      arrayOfInt[0] = this.EGL_CONTEXT_CLIENT_VERSION;
      arrayOfInt[1] = MultiFunctionRenderRuntimeEnvironment.this.mEGLContextClientVersion;
      arrayOfInt[2] = 12344;
      EGLContext eGLContext = EGL10.EGL_NO_CONTEXT;
      if (MultiFunctionRenderRuntimeEnvironment.this.mEGLContextClientVersion == 0)
        arrayOfInt = null; 
      return param1EGL10.eglCreateContext(param1EGLDisplay, param1EGLConfig, eGLContext, arrayOfInt);
    }
    
    public void destroyContext(EGL10 param1EGL10, EGLDisplay param1EGLDisplay, EGLContext param1EGLContext) {
      if (!param1EGL10.eglDestroyContext(param1EGLDisplay, param1EGLContext)) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("display:");
        stringBuilder.append(param1EGLDisplay);
        stringBuilder.append(" context: ");
        stringBuilder.append(param1EGLContext);
        Log.e("DefaultContextFactory", stringBuilder.toString());
        MultiFunctionRenderRuntimeEnvironment.EglHelper.throwEglException("eglDestroyContex", param1EGL10.eglGetError());
      } 
    }
  }
  
  private static class DefaultWindowSurfaceFactory implements EGLWindowSurfaceFactory {
    private DefaultWindowSurfaceFactory() {}
    
    public EGLSurface createWindowSurface(EGL10 param1EGL10, EGLDisplay param1EGLDisplay, EGLConfig param1EGLConfig, Object param1Object) {
      IllegalArgumentException illegalArgumentException2 = null;
      try {
        EGLSurface eGLSurface = param1EGL10.eglCreateWindowSurface(param1EGLDisplay, param1EGLConfig, param1Object, null);
      } catch (IllegalArgumentException illegalArgumentException1) {
        Log.e("MultiFuncEnv", "eglCreateWindowSurface", illegalArgumentException1);
        illegalArgumentException1 = illegalArgumentException2;
      } 
      return (EGLSurface)illegalArgumentException1;
    }
    
    public void destroySurface(EGL10 param1EGL10, EGLDisplay param1EGLDisplay, EGLSurface param1EGLSurface) {
      param1EGL10.eglDestroySurface(param1EGLDisplay, param1EGLSurface);
    }
  }
  
  public static interface EGLConfigChooser {
    EGLConfig chooseConfig(EGL10 param1EGL10, EGLDisplay param1EGLDisplay);
  }
  
  public static interface EGLContextFactory {
    EGLContext createContext(EGL10 param1EGL10, EGLDisplay param1EGLDisplay, EGLConfig param1EGLConfig);
    
    void destroyContext(EGL10 param1EGL10, EGLDisplay param1EGLDisplay, EGLContext param1EGLContext);
  }
  
  public static interface EGLWindowSurfaceFactory {
    EGLSurface createWindowSurface(EGL10 param1EGL10, EGLDisplay param1EGLDisplay, EGLConfig param1EGLConfig, Object param1Object);
    
    void destroySurface(EGL10 param1EGL10, EGLDisplay param1EGLDisplay, EGLSurface param1EGLSurface);
  }
  
  private static class EglHelper {
    EGL10 mEgl;
    
    EGLConfig mEglConfig;
    
    EGLContext mEglContext;
    
    EGLContext mEglContext_14;
    
    EGLDisplay mEglDisplay;
    
    EGLSurface mEglSurface;
    
    private WeakReference<MultiFunctionRenderRuntimeEnvironment> mGLSurfaceViewWeakRef;
    
    public EglHelper(WeakReference<MultiFunctionRenderRuntimeEnvironment> param1WeakReference) {
      this.mGLSurfaceViewWeakRef = param1WeakReference;
    }
    
    private void destroySurfaceImp() {
      EGLSurface eGLSurface = this.mEglSurface;
      if (eGLSurface != null && eGLSurface != EGL10.EGL_NO_SURFACE) {
        this.mEgl.eglMakeCurrent(this.mEglDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
        MultiFunctionRenderRuntimeEnvironment multiFunctionRenderRuntimeEnvironment = this.mGLSurfaceViewWeakRef.get();
        if (multiFunctionRenderRuntimeEnvironment != null)
          multiFunctionRenderRuntimeEnvironment.mEGLWindowSurfaceFactory.destroySurface(this.mEgl, this.mEglDisplay, this.mEglSurface); 
        this.mEglSurface = null;
      } 
    }
    
    public static String formatEglError(String param1String, int param1Int) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(param1String);
      stringBuilder.append(" failed: ");
      stringBuilder.append(param1Int);
      return stringBuilder.toString();
    }
    
    public static void logEglErrorAsWarning(String param1String1, String param1String2, int param1Int) {
      Log.w(param1String1, formatEglError(param1String2, param1Int));
    }
    
    private void throwEglException(String param1String) {
      throwEglException(param1String, this.mEgl.eglGetError());
    }
    
    public static void throwEglException(String param1String, int param1Int) {
      throw new RuntimeException(formatEglError(param1String, param1Int));
    }
    
    GL createGL() {
      GL gL1 = this.mEglContext.getGL();
      MultiFunctionRenderRuntimeEnvironment multiFunctionRenderRuntimeEnvironment = this.mGLSurfaceViewWeakRef.get();
      GL gL2 = gL1;
      if (multiFunctionRenderRuntimeEnvironment != null) {
        GL gL = gL1;
        if (multiFunctionRenderRuntimeEnvironment.mGLWrapper != null)
          gL = multiFunctionRenderRuntimeEnvironment.mGLWrapper.wrap(gL1); 
        gL2 = gL;
        if ((multiFunctionRenderRuntimeEnvironment.mDebugFlags & 0x3) != 0) {
          MultiFunctionRenderRuntimeEnvironment.LogWriter logWriter;
          boolean bool = false;
          gL2 = null;
          if ((multiFunctionRenderRuntimeEnvironment.mDebugFlags & 0x1) != 0)
            bool = true; 
          if ((multiFunctionRenderRuntimeEnvironment.mDebugFlags & 0x2) != 0)
            logWriter = new MultiFunctionRenderRuntimeEnvironment.LogWriter(); 
          gL2 = GLDebugHelper.wrap(gL, bool, logWriter);
        } 
      } 
      return gL2;
    }
    
    public boolean createSurface() {
      if (this.mEgl != null) {
        if (this.mEglDisplay != null) {
          if (this.mEglConfig != null) {
            destroySurfaceImp();
            MultiFunctionRenderRuntimeEnvironment multiFunctionRenderRuntimeEnvironment = this.mGLSurfaceViewWeakRef.get();
            if (multiFunctionRenderRuntimeEnvironment != null) {
              this.mEglSurface = multiFunctionRenderRuntimeEnvironment.mEGLWindowSurfaceFactory.createWindowSurface(this.mEgl, this.mEglDisplay, this.mEglConfig, multiFunctionRenderRuntimeEnvironment.getHolder());
            } else {
              this.mEglSurface = null;
            } 
            EGLSurface eGLSurface1 = this.mEglSurface;
            if (eGLSurface1 == null || eGLSurface1 == EGL10.EGL_NO_SURFACE) {
              if (this.mEgl.eglGetError() == 12299)
                Log.e("EglHelper", "createWindowSurface returned EGL_BAD_NATIVE_WINDOW."); 
              return false;
            } 
            EGL10 eGL10 = this.mEgl;
            EGLDisplay eGLDisplay = this.mEglDisplay;
            EGLSurface eGLSurface2 = this.mEglSurface;
            if (!eGL10.eglMakeCurrent(eGLDisplay, eGLSurface2, eGLSurface2, this.mEglContext)) {
              logEglErrorAsWarning("EGLHelper", "eglMakeCurrent", this.mEgl.eglGetError());
              return false;
            } 
            this.mEglContext_14 = EGL14.eglGetCurrentContext();
            return true;
          } 
          throw new RuntimeException("mEglConfig not initialized");
        } 
        throw new RuntimeException("eglDisplay not initialized");
      } 
      throw new RuntimeException("egl not initialized");
    }
    
    public void destroySurface() {
      destroySurfaceImp();
    }
    
    public void finish() {
      if (this.mEglContext != null) {
        MultiFunctionRenderRuntimeEnvironment multiFunctionRenderRuntimeEnvironment = this.mGLSurfaceViewWeakRef.get();
        if (multiFunctionRenderRuntimeEnvironment != null)
          multiFunctionRenderRuntimeEnvironment.mEGLContextFactory.destroyContext(this.mEgl, this.mEglDisplay, this.mEglContext); 
        this.mEglContext = null;
        this.mEglContext_14 = null;
      } 
      EGLDisplay eGLDisplay = this.mEglDisplay;
      if (eGLDisplay != null) {
        this.mEgl.eglTerminate(eGLDisplay);
        this.mEglDisplay = null;
      } 
    }
    
    public EGLContext getEglContext() {
      return this.mEglContext;
    }
    
    public void start() {
      this.mEgl = (EGL10)EGLContext.getEGL();
      this.mEglDisplay = this.mEgl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
      if (this.mEglDisplay != EGL10.EGL_NO_DISPLAY) {
        int[] arrayOfInt = new int[2];
        if (this.mEgl.eglInitialize(this.mEglDisplay, arrayOfInt)) {
          MultiFunctionRenderRuntimeEnvironment multiFunctionRenderRuntimeEnvironment = this.mGLSurfaceViewWeakRef.get();
          if (multiFunctionRenderRuntimeEnvironment == null) {
            this.mEglConfig = null;
            this.mEglContext = null;
            this.mEglContext_14 = null;
          } else {
            this.mEglConfig = multiFunctionRenderRuntimeEnvironment.mEGLConfigChooser.chooseConfig(this.mEgl, this.mEglDisplay);
            this.mEglContext = multiFunctionRenderRuntimeEnvironment.mEGLContextFactory.createContext(this.mEgl, this.mEglDisplay, this.mEglConfig);
          } 
          EGLContext eGLContext = this.mEglContext;
          if (eGLContext == null || eGLContext == EGL10.EGL_NO_CONTEXT) {
            this.mEglContext = null;
            this.mEglContext_14 = null;
            throwEglException("createContext");
          } 
          this.mEglSurface = null;
          return;
        } 
        throw new RuntimeException("eglInitialize failed");
      } 
      throw new RuntimeException("eglGetDisplay failed");
    }
    
    public int swap() {
      return !this.mEgl.eglSwapBuffers(this.mEglDisplay, this.mEglSurface) ? this.mEgl.eglGetError() : 12288;
    }
  }
  
  public static class GLThread extends Thread {
    SurfaceTexture inputSurfaceTexture;
    
    int inputSurfaceTextureID;
    
    private MultiFunctionRenderRuntimeEnvironment.EglHelper mEglHelper;
    
    private ArrayList<Runnable> mEventQueue = new ArrayList<Runnable>();
    
    private boolean mExited;
    
    private Runnable mFinishDrawingRunnable = null;
    
    private boolean mFinishedCreatingEglSurface;
    
    private WeakReference<MultiFunctionRenderRuntimeEnvironment> mGLSurfaceViewWeakRef;
    
    private boolean mHasSurface;
    
    private boolean mHaveEglContext;
    
    private boolean mHaveEglSurface;
    
    private int mHeight = 0;
    
    private boolean mPaused;
    
    private boolean mRenderComplete;
    
    private int mRenderMode;
    
    private boolean mRequestPaused;
    
    private boolean mRequestRender = true;
    
    private boolean mShouldExit;
    
    private boolean mShouldReleaseEglContext;
    
    private boolean mSizeChanged = true;
    
    private boolean mSurfaceIsBad;
    
    private boolean mWaitingForSurface;
    
    private boolean mWantRenderNotification;
    
    private int mWidth = 0;
    
    private final float[] mtx = new float[16];
    
    OnInputSurfaceTextureAvailableCallback onInputSurfaceTextureCallback;
    
    GLThread(WeakReference<MultiFunctionRenderRuntimeEnvironment> param1WeakReference) {
      this.mRenderMode = (param1WeakReference.get()).defaultRenderMode;
      this.mWantRenderNotification = false;
      this.mGLSurfaceViewWeakRef = param1WeakReference;
    }
    
    private void guardedRun() throws InterruptedException {
      // Byte code:
      //   0: aload_0
      //   1: new com/netopsun/ijkvideoview/widget/media/render/MultiFunctionRenderRuntimeEnvironment$EglHelper
      //   4: dup
      //   5: aload_0
      //   6: getfield mGLSurfaceViewWeakRef : Ljava/lang/ref/WeakReference;
      //   9: invokespecial <init> : (Ljava/lang/ref/WeakReference;)V
      //   12: putfield mEglHelper : Lcom/netopsun/ijkvideoview/widget/media/render/MultiFunctionRenderRuntimeEnvironment$EglHelper;
      //   15: aload_0
      //   16: iconst_0
      //   17: putfield mHaveEglContext : Z
      //   20: aload_0
      //   21: iconst_0
      //   22: putfield mHaveEglSurface : Z
      //   25: aload_0
      //   26: iconst_0
      //   27: putfield mWantRenderNotification : Z
      //   30: iconst_0
      //   31: istore_1
      //   32: iconst_0
      //   33: istore_2
      //   34: aconst_null
      //   35: astore_3
      //   36: iconst_0
      //   37: istore #4
      //   39: iconst_0
      //   40: istore #5
      //   42: aconst_null
      //   43: astore #6
      //   45: iconst_0
      //   46: istore #7
      //   48: iconst_0
      //   49: istore #8
      //   51: aconst_null
      //   52: astore #9
      //   54: iconst_0
      //   55: istore #10
      //   57: iconst_0
      //   58: istore #11
      //   60: iconst_0
      //   61: istore #12
      //   63: iconst_0
      //   64: istore #13
      //   66: invokestatic access$1400 : ()Lcom/netopsun/ijkvideoview/widget/media/render/MultiFunctionRenderRuntimeEnvironment$GLThreadManager;
      //   69: astore #14
      //   71: aload #14
      //   73: monitorenter
      //   74: iload #4
      //   76: istore #15
      //   78: aload_0
      //   79: getfield mShouldExit : Z
      //   82: ifeq -> 114
      //   85: aload #14
      //   87: monitorexit
      //   88: invokestatic access$1400 : ()Lcom/netopsun/ijkvideoview/widget/media/render/MultiFunctionRenderRuntimeEnvironment$GLThreadManager;
      //   91: astore #6
      //   93: aload #6
      //   95: monitorenter
      //   96: aload_0
      //   97: invokespecial stopEglSurfaceLocked : ()V
      //   100: aload_0
      //   101: invokespecial stopEglContextLocked : ()V
      //   104: aload #6
      //   106: monitorexit
      //   107: return
      //   108: astore_3
      //   109: aload #6
      //   111: monitorexit
      //   112: aload_3
      //   113: athrow
      //   114: aload_0
      //   115: getfield mEventQueue : Ljava/util/ArrayList;
      //   118: invokevirtual isEmpty : ()Z
      //   121: ifne -> 156
      //   124: aload_0
      //   125: getfield mEventQueue : Ljava/util/ArrayList;
      //   128: iconst_0
      //   129: invokevirtual remove : (I)Ljava/lang/Object;
      //   132: checkcast java/lang/Runnable
      //   135: astore #9
      //   137: iload_1
      //   138: istore #4
      //   140: iload_2
      //   141: istore_1
      //   142: iload #15
      //   144: istore_2
      //   145: iload #7
      //   147: istore #16
      //   149: iload #8
      //   151: istore #17
      //   153: goto -> 654
      //   156: aload_0
      //   157: getfield mPaused : Z
      //   160: aload_0
      //   161: getfield mRequestPaused : Z
      //   164: if_icmpeq -> 190
      //   167: aload_0
      //   168: getfield mRequestPaused : Z
      //   171: istore #18
      //   173: aload_0
      //   174: aload_0
      //   175: getfield mRequestPaused : Z
      //   178: putfield mPaused : Z
      //   181: invokestatic access$1400 : ()Lcom/netopsun/ijkvideoview/widget/media/render/MultiFunctionRenderRuntimeEnvironment$GLThreadManager;
      //   184: invokevirtual notifyAll : ()V
      //   187: goto -> 193
      //   190: iconst_0
      //   191: istore #18
      //   193: aload_0
      //   194: getfield mShouldReleaseEglContext : Z
      //   197: ifeq -> 215
      //   200: aload_0
      //   201: invokespecial stopEglSurfaceLocked : ()V
      //   204: aload_0
      //   205: invokespecial stopEglContextLocked : ()V
      //   208: aload_0
      //   209: iconst_0
      //   210: putfield mShouldReleaseEglContext : Z
      //   213: iconst_1
      //   214: istore_2
      //   215: iload #15
      //   217: istore #19
      //   219: iload #15
      //   221: ifeq -> 235
      //   224: aload_0
      //   225: invokespecial stopEglSurfaceLocked : ()V
      //   228: aload_0
      //   229: invokespecial stopEglContextLocked : ()V
      //   232: iconst_0
      //   233: istore #19
      //   235: iload #18
      //   237: ifeq -> 251
      //   240: aload_0
      //   241: getfield mHaveEglSurface : Z
      //   244: ifeq -> 251
      //   247: aload_0
      //   248: invokespecial stopEglSurfaceLocked : ()V
      //   251: iload #18
      //   253: ifeq -> 306
      //   256: aload_0
      //   257: getfield mHaveEglContext : Z
      //   260: ifeq -> 306
      //   263: aload_0
      //   264: getfield mGLSurfaceViewWeakRef : Ljava/lang/ref/WeakReference;
      //   267: invokevirtual get : ()Ljava/lang/Object;
      //   270: checkcast com/netopsun/ijkvideoview/widget/media/render/MultiFunctionRenderRuntimeEnvironment
      //   273: astore #20
      //   275: aload #20
      //   277: ifnull -> 294
      //   280: aload #20
      //   282: invokestatic access$1500 : (Lcom/netopsun/ijkvideoview/widget/media/render/MultiFunctionRenderRuntimeEnvironment;)Z
      //   285: ifeq -> 294
      //   288: iconst_1
      //   289: istore #4
      //   291: goto -> 297
      //   294: iconst_0
      //   295: istore #4
      //   297: iload #4
      //   299: ifne -> 306
      //   302: aload_0
      //   303: invokespecial stopEglContextLocked : ()V
      //   306: aload_0
      //   307: getfield mHasSurface : Z
      //   310: ifne -> 347
      //   313: aload_0
      //   314: getfield mWaitingForSurface : Z
      //   317: ifne -> 347
      //   320: aload_0
      //   321: getfield mHaveEglSurface : Z
      //   324: ifeq -> 331
      //   327: aload_0
      //   328: invokespecial stopEglSurfaceLocked : ()V
      //   331: aload_0
      //   332: iconst_1
      //   333: putfield mWaitingForSurface : Z
      //   336: aload_0
      //   337: iconst_0
      //   338: putfield mSurfaceIsBad : Z
      //   341: invokestatic access$1400 : ()Lcom/netopsun/ijkvideoview/widget/media/render/MultiFunctionRenderRuntimeEnvironment$GLThreadManager;
      //   344: invokevirtual notifyAll : ()V
      //   347: aload_0
      //   348: getfield mHasSurface : Z
      //   351: ifeq -> 372
      //   354: aload_0
      //   355: getfield mWaitingForSurface : Z
      //   358: ifeq -> 372
      //   361: aload_0
      //   362: iconst_0
      //   363: putfield mWaitingForSurface : Z
      //   366: invokestatic access$1400 : ()Lcom/netopsun/ijkvideoview/widget/media/render/MultiFunctionRenderRuntimeEnvironment$GLThreadManager;
      //   369: invokevirtual notifyAll : ()V
      //   372: iload_1
      //   373: istore #4
      //   375: iload_1
      //   376: ifeq -> 398
      //   379: aload_0
      //   380: iconst_0
      //   381: putfield mWantRenderNotification : Z
      //   384: aload_0
      //   385: iconst_1
      //   386: putfield mRenderComplete : Z
      //   389: invokestatic access$1400 : ()Lcom/netopsun/ijkvideoview/widget/media/render/MultiFunctionRenderRuntimeEnvironment$GLThreadManager;
      //   392: invokevirtual notifyAll : ()V
      //   395: iconst_0
      //   396: istore #4
      //   398: aload_0
      //   399: getfield mFinishDrawingRunnable : Ljava/lang/Runnable;
      //   402: ifnull -> 418
      //   405: aload_0
      //   406: getfield mFinishDrawingRunnable : Ljava/lang/Runnable;
      //   409: astore_3
      //   410: aload_0
      //   411: aconst_null
      //   412: putfield mFinishDrawingRunnable : Ljava/lang/Runnable;
      //   415: goto -> 418
      //   418: aload_0
      //   419: invokespecial readyToDraw : ()Z
      //   422: ifeq -> 1343
      //   425: aload_0
      //   426: getfield mHaveEglContext : Z
      //   429: istore #18
      //   431: iload_2
      //   432: istore_1
      //   433: iload #10
      //   435: istore #15
      //   437: iload #18
      //   439: ifne -> 491
      //   442: iload_2
      //   443: ifeq -> 455
      //   446: iconst_0
      //   447: istore_1
      //   448: iload #10
      //   450: istore #15
      //   452: goto -> 491
      //   455: aload_0
      //   456: getfield mEglHelper : Lcom/netopsun/ijkvideoview/widget/media/render/MultiFunctionRenderRuntimeEnvironment$EglHelper;
      //   459: invokevirtual start : ()V
      //   462: aload_0
      //   463: iconst_1
      //   464: putfield mHaveEglContext : Z
      //   467: invokestatic access$1400 : ()Lcom/netopsun/ijkvideoview/widget/media/render/MultiFunctionRenderRuntimeEnvironment$GLThreadManager;
      //   470: invokevirtual notifyAll : ()V
      //   473: iconst_1
      //   474: istore #15
      //   476: iload_2
      //   477: istore_1
      //   478: goto -> 491
      //   481: astore_3
      //   482: invokestatic access$1400 : ()Lcom/netopsun/ijkvideoview/widget/media/render/MultiFunctionRenderRuntimeEnvironment$GLThreadManager;
      //   485: aload_0
      //   486: invokevirtual releaseEglContextLocked : (Lcom/netopsun/ijkvideoview/widget/media/render/MultiFunctionRenderRuntimeEnvironment$GLThread;)V
      //   489: aload_3
      //   490: athrow
      //   491: iload #11
      //   493: istore #10
      //   495: iload #12
      //   497: istore #21
      //   499: iload #13
      //   501: istore_2
      //   502: aload_0
      //   503: getfield mHaveEglContext : Z
      //   506: ifeq -> 540
      //   509: iload #11
      //   511: istore #10
      //   513: iload #12
      //   515: istore #21
      //   517: iload #13
      //   519: istore_2
      //   520: aload_0
      //   521: getfield mHaveEglSurface : Z
      //   524: ifne -> 540
      //   527: aload_0
      //   528: iconst_1
      //   529: putfield mHaveEglSurface : Z
      //   532: iconst_1
      //   533: istore #10
      //   535: iconst_1
      //   536: istore #21
      //   538: iconst_1
      //   539: istore_2
      //   540: aload_0
      //   541: getfield mHaveEglSurface : Z
      //   544: ifeq -> 1333
      //   547: aload_0
      //   548: getfield mSizeChanged : Z
      //   551: ifeq -> 585
      //   554: aload_0
      //   555: getfield mWidth : I
      //   558: istore #7
      //   560: aload_0
      //   561: getfield mHeight : I
      //   564: istore #8
      //   566: aload_0
      //   567: iconst_1
      //   568: putfield mWantRenderNotification : Z
      //   571: aload_0
      //   572: iconst_0
      //   573: putfield mSizeChanged : Z
      //   576: iconst_1
      //   577: istore #11
      //   579: iconst_1
      //   580: istore #13
      //   582: goto -> 592
      //   585: iload_2
      //   586: istore #13
      //   588: iload #10
      //   590: istore #11
      //   592: aload_0
      //   593: iconst_0
      //   594: putfield mRequestRender : Z
      //   597: invokestatic access$1400 : ()Lcom/netopsun/ijkvideoview/widget/media/render/MultiFunctionRenderRuntimeEnvironment$GLThreadManager;
      //   600: invokevirtual notifyAll : ()V
      //   603: aload_0
      //   604: getfield mWantRenderNotification : Z
      //   607: ifeq -> 635
      //   610: iconst_1
      //   611: istore #5
      //   613: iload #19
      //   615: istore_2
      //   616: iload #7
      //   618: istore #16
      //   620: iload #8
      //   622: istore #17
      //   624: iload #15
      //   626: istore #10
      //   628: iload #21
      //   630: istore #12
      //   632: goto -> 654
      //   635: iload #21
      //   637: istore #12
      //   639: iload #15
      //   641: istore #10
      //   643: iload #8
      //   645: istore #17
      //   647: iload #7
      //   649: istore #16
      //   651: iload #19
      //   653: istore_2
      //   654: aload #14
      //   656: monitorexit
      //   657: aload #9
      //   659: ifnull -> 699
      //   662: aload #9
      //   664: invokeinterface run : ()V
      //   669: aconst_null
      //   670: astore #9
      //   672: iload_2
      //   673: istore #21
      //   675: iload_1
      //   676: istore #8
      //   678: iload #4
      //   680: istore_1
      //   681: iload #8
      //   683: istore_2
      //   684: iload #21
      //   686: istore #4
      //   688: iload #16
      //   690: istore #7
      //   692: iload #17
      //   694: istore #8
      //   696: goto -> 66
      //   699: iload #11
      //   701: ifeq -> 1011
      //   704: aload_0
      //   705: getfield mEglHelper : Lcom/netopsun/ijkvideoview/widget/media/render/MultiFunctionRenderRuntimeEnvironment$EglHelper;
      //   708: invokevirtual createSurface : ()Z
      //   711: ifeq -> 932
      //   714: invokestatic access$1400 : ()Lcom/netopsun/ijkvideoview/widget/media/render/MultiFunctionRenderRuntimeEnvironment$GLThreadManager;
      //   717: astore #14
      //   719: aload #14
      //   721: monitorenter
      //   722: aload_0
      //   723: getfield inputSurfaceTexture : Landroid/graphics/SurfaceTexture;
      //   726: ifnonnull -> 854
      //   729: iconst_1
      //   730: newarray int
      //   732: astore #20
      //   734: aload #20
      //   736: iconst_0
      //   737: iconst_m1
      //   738: iastore
      //   739: iconst_1
      //   740: aload #20
      //   742: iconst_0
      //   743: invokestatic glGenTextures : (I[II)V
      //   746: ldc 36197
      //   748: aload #20
      //   750: iconst_0
      //   751: iaload
      //   752: invokestatic glBindTexture : (II)V
      //   755: ldc 36197
      //   757: sipush #10241
      //   760: ldc 9728.0
      //   762: invokestatic glTexParameterf : (IIF)V
      //   765: ldc 36197
      //   767: sipush #10240
      //   770: ldc 9729.0
      //   772: invokestatic glTexParameterf : (IIF)V
      //   775: ldc 36197
      //   777: sipush #10242
      //   780: ldc 33071.0
      //   782: invokestatic glTexParameterf : (IIF)V
      //   785: ldc 36197
      //   787: sipush #10243
      //   790: ldc 33071.0
      //   792: invokestatic glTexParameterf : (IIF)V
      //   795: aload_0
      //   796: aload #20
      //   798: iconst_0
      //   799: iaload
      //   800: putfield inputSurfaceTextureID : I
      //   803: new android/graphics/SurfaceTexture
      //   806: astore #22
      //   808: aload #22
      //   810: aload #20
      //   812: iconst_0
      //   813: iaload
      //   814: invokespecial <init> : (I)V
      //   817: aload_0
      //   818: aload #22
      //   820: putfield inputSurfaceTexture : Landroid/graphics/SurfaceTexture;
      //   823: aload_0
      //   824: getfield onInputSurfaceTextureCallback : Lcom/netopsun/ijkvideoview/widget/media/render/MultiFunctionRenderRuntimeEnvironment$GLThread$OnInputSurfaceTextureAvailableCallback;
      //   827: ifnull -> 882
      //   830: aload_0
      //   831: getfield onInputSurfaceTextureCallback : Lcom/netopsun/ijkvideoview/widget/media/render/MultiFunctionRenderRuntimeEnvironment$GLThread$OnInputSurfaceTextureAvailableCallback;
      //   834: aload_0
      //   835: getfield inputSurfaceTexture : Landroid/graphics/SurfaceTexture;
      //   838: aload_0
      //   839: getfield mWidth : I
      //   842: aload_0
      //   843: getfield mHeight : I
      //   846: invokeinterface onInputSurfaceTextureAvailable : (Landroid/graphics/SurfaceTexture;II)V
      //   851: goto -> 882
      //   854: aload_0
      //   855: getfield onInputSurfaceTextureCallback : Lcom/netopsun/ijkvideoview/widget/media/render/MultiFunctionRenderRuntimeEnvironment$GLThread$OnInputSurfaceTextureAvailableCallback;
      //   858: ifnull -> 882
      //   861: aload_0
      //   862: getfield onInputSurfaceTextureCallback : Lcom/netopsun/ijkvideoview/widget/media/render/MultiFunctionRenderRuntimeEnvironment$GLThread$OnInputSurfaceTextureAvailableCallback;
      //   865: aload_0
      //   866: getfield inputSurfaceTexture : Landroid/graphics/SurfaceTexture;
      //   869: aload_0
      //   870: getfield mWidth : I
      //   873: aload_0
      //   874: getfield mHeight : I
      //   877: invokeinterface onInputSurfaceTextureSizeChanged : (Landroid/graphics/SurfaceTexture;II)V
      //   882: aload_0
      //   883: getfield inputSurfaceTexture : Landroid/graphics/SurfaceTexture;
      //   886: astore #20
      //   888: new com/netopsun/ijkvideoview/widget/media/render/MultiFunctionRenderRuntimeEnvironment$GLThread$1
      //   891: astore #22
      //   893: aload #22
      //   895: aload_0
      //   896: invokespecial <init> : (Lcom/netopsun/ijkvideoview/widget/media/render/MultiFunctionRenderRuntimeEnvironment$GLThread;)V
      //   899: aload #20
      //   901: aload #22
      //   903: invokevirtual setOnFrameAvailableListener : (Landroid/graphics/SurfaceTexture$OnFrameAvailableListener;)V
      //   906: aload_0
      //   907: iconst_1
      //   908: putfield mFinishedCreatingEglSurface : Z
      //   911: invokestatic access$1400 : ()Lcom/netopsun/ijkvideoview/widget/media/render/MultiFunctionRenderRuntimeEnvironment$GLThreadManager;
      //   914: invokevirtual notifyAll : ()V
      //   917: aload #14
      //   919: monitorexit
      //   920: iconst_0
      //   921: istore #15
      //   923: goto -> 1015
      //   926: astore_3
      //   927: aload #14
      //   929: monitorexit
      //   930: aload_3
      //   931: athrow
      //   932: invokestatic access$1400 : ()Lcom/netopsun/ijkvideoview/widget/media/render/MultiFunctionRenderRuntimeEnvironment$GLThreadManager;
      //   935: astore #14
      //   937: aload #14
      //   939: monitorenter
      //   940: aload_0
      //   941: iconst_1
      //   942: putfield mFinishedCreatingEglSurface : Z
      //   945: aload_0
      //   946: iconst_1
      //   947: putfield mSurfaceIsBad : Z
      //   950: invokestatic access$1400 : ()Lcom/netopsun/ijkvideoview/widget/media/render/MultiFunctionRenderRuntimeEnvironment$GLThreadManager;
      //   953: invokevirtual notifyAll : ()V
      //   956: aload #14
      //   958: monitorexit
      //   959: iload #12
      //   961: istore #19
      //   963: iload #11
      //   965: istore #8
      //   967: aload #6
      //   969: astore #20
      //   971: iload_2
      //   972: istore #7
      //   974: iload #19
      //   976: istore_2
      //   977: iload #8
      //   979: istore #15
      //   981: aload #20
      //   983: astore #6
      //   985: iload_1
      //   986: istore #8
      //   988: iload #4
      //   990: istore_1
      //   991: iload #7
      //   993: istore #21
      //   995: iload #15
      //   997: istore #11
      //   999: iload_2
      //   1000: istore #12
      //   1002: goto -> 681
      //   1005: astore_3
      //   1006: aload #14
      //   1008: monitorexit
      //   1009: aload_3
      //   1010: athrow
      //   1011: iload #11
      //   1013: istore #15
      //   1015: iload #12
      //   1017: istore #11
      //   1019: iload #12
      //   1021: ifeq -> 1039
      //   1024: aload_0
      //   1025: getfield mEglHelper : Lcom/netopsun/ijkvideoview/widget/media/render/MultiFunctionRenderRuntimeEnvironment$EglHelper;
      //   1028: invokevirtual createGL : ()Ljavax/microedition/khronos/opengles/GL;
      //   1031: checkcast javax/microedition/khronos/opengles/GL10
      //   1034: astore #6
      //   1036: iconst_0
      //   1037: istore #11
      //   1039: iload #10
      //   1041: istore #21
      //   1043: iload #10
      //   1045: ifeq -> 1087
      //   1048: aload_0
      //   1049: getfield mGLSurfaceViewWeakRef : Ljava/lang/ref/WeakReference;
      //   1052: invokevirtual get : ()Ljava/lang/Object;
      //   1055: checkcast com/netopsun/ijkvideoview/widget/media/render/MultiFunctionRenderRuntimeEnvironment
      //   1058: astore #14
      //   1060: aload #14
      //   1062: ifnull -> 1084
      //   1065: aload #14
      //   1067: invokestatic access$1600 : (Lcom/netopsun/ijkvideoview/widget/media/render/MultiFunctionRenderRuntimeEnvironment;)Lcom/netopsun/ijkvideoview/widget/media/render/MultiFunctionRenderRuntimeEnvironment$Renderer;
      //   1070: aload #6
      //   1072: aload_0
      //   1073: getfield mEglHelper : Lcom/netopsun/ijkvideoview/widget/media/render/MultiFunctionRenderRuntimeEnvironment$EglHelper;
      //   1076: getfield mEglConfig : Ljavax/microedition/khronos/egl/EGLConfig;
      //   1079: invokeinterface onSurfaceCreated : (Ljavax/microedition/khronos/opengles/GL10;Ljavax/microedition/khronos/egl/EGLConfig;)V
      //   1084: iconst_0
      //   1085: istore #21
      //   1087: iload #13
      //   1089: istore #12
      //   1091: iload #13
      //   1093: ifeq -> 1132
      //   1096: aload_0
      //   1097: getfield mGLSurfaceViewWeakRef : Ljava/lang/ref/WeakReference;
      //   1100: invokevirtual get : ()Ljava/lang/Object;
      //   1103: checkcast com/netopsun/ijkvideoview/widget/media/render/MultiFunctionRenderRuntimeEnvironment
      //   1106: astore #14
      //   1108: aload #14
      //   1110: ifnull -> 1129
      //   1113: aload #14
      //   1115: invokestatic access$1600 : (Lcom/netopsun/ijkvideoview/widget/media/render/MultiFunctionRenderRuntimeEnvironment;)Lcom/netopsun/ijkvideoview/widget/media/render/MultiFunctionRenderRuntimeEnvironment$Renderer;
      //   1118: aload #6
      //   1120: iload #16
      //   1122: iload #17
      //   1124: invokeinterface onSurfaceChanged : (Ljavax/microedition/khronos/opengles/GL10;II)V
      //   1129: iconst_0
      //   1130: istore #12
      //   1132: aload_0
      //   1133: getfield mGLSurfaceViewWeakRef : Ljava/lang/ref/WeakReference;
      //   1136: invokevirtual get : ()Ljava/lang/Object;
      //   1139: checkcast com/netopsun/ijkvideoview/widget/media/render/MultiFunctionRenderRuntimeEnvironment
      //   1142: astore #20
      //   1144: aload_3
      //   1145: astore #14
      //   1147: aload #20
      //   1149: ifnull -> 1206
      //   1152: aload_0
      //   1153: getfield inputSurfaceTexture : Landroid/graphics/SurfaceTexture;
      //   1156: invokevirtual updateTexImage : ()V
      //   1159: aload_0
      //   1160: getfield inputSurfaceTexture : Landroid/graphics/SurfaceTexture;
      //   1163: aload_0
      //   1164: getfield mtx : [F
      //   1167: invokevirtual getTransformMatrix : ([F)V
      //   1170: aload #20
      //   1172: invokestatic access$1600 : (Lcom/netopsun/ijkvideoview/widget/media/render/MultiFunctionRenderRuntimeEnvironment;)Lcom/netopsun/ijkvideoview/widget/media/render/MultiFunctionRenderRuntimeEnvironment$Renderer;
      //   1175: aload #6
      //   1177: aload_0
      //   1178: getfield inputSurfaceTextureID : I
      //   1181: aload_0
      //   1182: getfield mtx : [F
      //   1185: invokeinterface onDrawFrame : (Ljavax/microedition/khronos/opengles/GL10;I[F)V
      //   1190: aload_3
      //   1191: astore #14
      //   1193: aload_3
      //   1194: ifnull -> 1206
      //   1197: aload_3
      //   1198: invokeinterface run : ()V
      //   1203: aconst_null
      //   1204: astore #14
      //   1206: aload_0
      //   1207: getfield mEglHelper : Lcom/netopsun/ijkvideoview/widget/media/render/MultiFunctionRenderRuntimeEnvironment$EglHelper;
      //   1210: invokevirtual swap : ()I
      //   1213: istore #13
      //   1215: iload #13
      //   1217: sipush #12288
      //   1220: if_icmpeq -> 1276
      //   1223: iload #13
      //   1225: sipush #12302
      //   1228: if_icmpeq -> 1271
      //   1231: ldc_w 'GLThread'
      //   1234: ldc_w 'eglSwapBuffers'
      //   1237: iload #13
      //   1239: invokestatic logEglErrorAsWarning : (Ljava/lang/String;Ljava/lang/String;I)V
      //   1242: invokestatic access$1400 : ()Lcom/netopsun/ijkvideoview/widget/media/render/MultiFunctionRenderRuntimeEnvironment$GLThreadManager;
      //   1245: astore_3
      //   1246: aload_3
      //   1247: monitorenter
      //   1248: aload_0
      //   1249: iconst_1
      //   1250: putfield mSurfaceIsBad : Z
      //   1253: invokestatic access$1400 : ()Lcom/netopsun/ijkvideoview/widget/media/render/MultiFunctionRenderRuntimeEnvironment$GLThreadManager;
      //   1256: invokevirtual notifyAll : ()V
      //   1259: aload_3
      //   1260: monitorexit
      //   1261: goto -> 1276
      //   1264: astore #6
      //   1266: aload_3
      //   1267: monitorexit
      //   1268: aload #6
      //   1270: athrow
      //   1271: iconst_1
      //   1272: istore_2
      //   1273: goto -> 1276
      //   1276: aload #14
      //   1278: astore_3
      //   1279: iload_2
      //   1280: istore #7
      //   1282: aload #6
      //   1284: astore #20
      //   1286: iload #21
      //   1288: istore #10
      //   1290: iload #15
      //   1292: istore #8
      //   1294: iload #11
      //   1296: istore #19
      //   1298: iload #12
      //   1300: istore #13
      //   1302: iload #5
      //   1304: ifeq -> 974
      //   1307: iconst_1
      //   1308: istore #4
      //   1310: iconst_0
      //   1311: istore #5
      //   1313: aload #14
      //   1315: astore_3
      //   1316: iload_2
      //   1317: istore #7
      //   1319: iload #21
      //   1321: istore #10
      //   1323: iload #11
      //   1325: istore_2
      //   1326: iload #12
      //   1328: istore #13
      //   1330: goto -> 985
      //   1333: iload #10
      //   1335: istore #17
      //   1337: iload_2
      //   1338: istore #16
      //   1340: goto -> 1386
      //   1343: iload_2
      //   1344: istore_1
      //   1345: iload #10
      //   1347: istore #15
      //   1349: iload #11
      //   1351: istore #17
      //   1353: iload #12
      //   1355: istore #21
      //   1357: iload #13
      //   1359: istore #16
      //   1361: aload_3
      //   1362: ifnull -> 1386
      //   1365: ldc_w 'MultiFuncEnv'
      //   1368: ldc_w 'Warning, !readyToDraw() but waiting for draw finished! Early reporting draw finished.'
      //   1371: invokestatic w : (Ljava/lang/String;Ljava/lang/String;)I
      //   1374: pop
      //   1375: aload_3
      //   1376: invokeinterface run : ()V
      //   1381: aconst_null
      //   1382: astore_3
      //   1383: goto -> 1404
      //   1386: iload #16
      //   1388: istore #13
      //   1390: iload #21
      //   1392: istore #12
      //   1394: iload #17
      //   1396: istore #11
      //   1398: iload #15
      //   1400: istore #10
      //   1402: iload_1
      //   1403: istore_2
      //   1404: invokestatic access$1400 : ()Lcom/netopsun/ijkvideoview/widget/media/render/MultiFunctionRenderRuntimeEnvironment$GLThreadManager;
      //   1407: invokevirtual wait : ()V
      //   1410: iload #4
      //   1412: istore_1
      //   1413: iload #19
      //   1415: istore #15
      //   1417: goto -> 78
      //   1420: astore_3
      //   1421: aload #14
      //   1423: monitorexit
      //   1424: aload_3
      //   1425: athrow
      //   1426: astore #6
      //   1428: invokestatic access$1400 : ()Lcom/netopsun/ijkvideoview/widget/media/render/MultiFunctionRenderRuntimeEnvironment$GLThreadManager;
      //   1431: astore_3
      //   1432: aload_3
      //   1433: monitorenter
      //   1434: aload_0
      //   1435: invokespecial stopEglSurfaceLocked : ()V
      //   1438: aload_0
      //   1439: invokespecial stopEglContextLocked : ()V
      //   1442: aload_3
      //   1443: monitorexit
      //   1444: aload #6
      //   1446: athrow
      //   1447: astore #6
      //   1449: aload_3
      //   1450: monitorexit
      //   1451: goto -> 1457
      //   1454: aload #6
      //   1456: athrow
      //   1457: goto -> 1454
      // Exception table:
      //   from	to	target	type
      //   66	74	1426	finally
      //   78	88	1420	finally
      //   96	107	108	finally
      //   109	112	108	finally
      //   114	137	1420	finally
      //   156	187	1420	finally
      //   193	213	1420	finally
      //   224	232	1420	finally
      //   240	251	1420	finally
      //   256	275	1420	finally
      //   280	288	1420	finally
      //   302	306	1420	finally
      //   306	331	1420	finally
      //   331	347	1420	finally
      //   347	372	1420	finally
      //   379	395	1420	finally
      //   398	415	1420	finally
      //   418	431	1420	finally
      //   455	462	481	java/lang/RuntimeException
      //   455	462	1420	finally
      //   462	473	1420	finally
      //   482	491	1420	finally
      //   502	509	1420	finally
      //   520	532	1420	finally
      //   540	576	1420	finally
      //   592	610	1420	finally
      //   654	657	1420	finally
      //   662	669	1426	finally
      //   704	722	1426	finally
      //   722	734	926	finally
      //   739	851	926	finally
      //   854	882	926	finally
      //   882	920	926	finally
      //   927	930	926	finally
      //   930	932	1426	finally
      //   932	940	1426	finally
      //   940	959	1005	finally
      //   1006	1009	1005	finally
      //   1009	1011	1426	finally
      //   1024	1036	1426	finally
      //   1048	1060	1426	finally
      //   1065	1084	1426	finally
      //   1096	1108	1426	finally
      //   1113	1129	1426	finally
      //   1132	1144	1426	finally
      //   1152	1190	1426	finally
      //   1197	1203	1426	finally
      //   1206	1215	1426	finally
      //   1231	1248	1426	finally
      //   1248	1261	1264	finally
      //   1266	1268	1264	finally
      //   1268	1271	1426	finally
      //   1365	1381	1420	finally
      //   1404	1410	1420	finally
      //   1421	1424	1420	finally
      //   1424	1426	1426	finally
      //   1434	1444	1447	finally
      //   1449	1451	1447	finally
    }
    
    private boolean readyToDraw() {
      null = this.mPaused;
      boolean bool = true;
      if (!null && this.mHasSurface && !this.mSurfaceIsBad && this.mWidth > 0 && this.mHeight > 0) {
        null = bool;
        if (!this.mRequestRender) {
          if (this.mRenderMode == 1)
            return bool; 
        } else {
          return null;
        } 
      } 
      return false;
    }
    
    private void stopEglContextLocked() {
      if (this.mHaveEglContext) {
        OnInputSurfaceTextureAvailableCallback onInputSurfaceTextureAvailableCallback = this.onInputSurfaceTextureCallback;
        if (onInputSurfaceTextureAvailableCallback != null)
          onInputSurfaceTextureAvailableCallback.onInputSurfaceTextureDestroyed(this.inputSurfaceTexture); 
        this.mEglHelper.finish();
        this.mHaveEglContext = false;
        MultiFunctionRenderRuntimeEnvironment.sGLThreadManager.releaseEglContextLocked(this);
      } 
    }
    
    private void stopEglSurfaceLocked() {
      if (this.mHaveEglSurface) {
        this.mHaveEglSurface = false;
        this.mEglHelper.destroySurface();
      } 
    }
    
    public boolean ableToDraw() {
      boolean bool;
      if (this.mHaveEglContext && this.mHaveEglSurface && readyToDraw()) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public int getRenderMode() {
      synchronized (MultiFunctionRenderRuntimeEnvironment.sGLThreadManager) {
        return this.mRenderMode;
      } 
    }
    
    public void onPause() {
      synchronized (MultiFunctionRenderRuntimeEnvironment.sGLThreadManager) {
        this.mRequestPaused = true;
        MultiFunctionRenderRuntimeEnvironment.sGLThreadManager.notifyAll();
        while (!this.mExited) {
          boolean bool = this.mPaused;
          if (!bool)
            try {
              MultiFunctionRenderRuntimeEnvironment.sGLThreadManager.wait();
            } catch (InterruptedException interruptedException) {
              Thread.currentThread().interrupt();
            }  
        } 
        return;
      } 
    }
    
    public void onResume() {
      synchronized (MultiFunctionRenderRuntimeEnvironment.sGLThreadManager) {
        this.mRequestPaused = false;
        this.mRequestRender = true;
        this.mRenderComplete = false;
        MultiFunctionRenderRuntimeEnvironment.sGLThreadManager.notifyAll();
        while (!this.mExited && this.mPaused) {
          boolean bool = this.mRenderComplete;
          if (!bool)
            try {
              MultiFunctionRenderRuntimeEnvironment.sGLThreadManager.wait();
            } catch (InterruptedException interruptedException) {
              Thread.currentThread().interrupt();
            }  
        } 
        return;
      } 
    }
    
    public void onWindowResize(int param1Int1, int param1Int2) {
      synchronized (MultiFunctionRenderRuntimeEnvironment.sGLThreadManager) {
        this.mWidth = param1Int1;
        this.mHeight = param1Int2;
        this.mSizeChanged = true;
        this.mRequestRender = true;
        this.mRenderComplete = false;
        if (Thread.currentThread() == this)
          return; 
        MultiFunctionRenderRuntimeEnvironment.sGLThreadManager.notifyAll();
        while (!this.mExited && !this.mPaused && !this.mRenderComplete) {
          boolean bool = ableToDraw();
          if (bool)
            try {
              MultiFunctionRenderRuntimeEnvironment.sGLThreadManager.wait();
            } catch (InterruptedException interruptedException) {
              Thread.currentThread().interrupt();
            }  
        } 
        return;
      } 
    }
    
    public void queueEvent(Runnable param1Runnable) {
      if (param1Runnable != null)
        synchronized (MultiFunctionRenderRuntimeEnvironment.sGLThreadManager) {
          this.mEventQueue.add(param1Runnable);
          MultiFunctionRenderRuntimeEnvironment.sGLThreadManager.notifyAll();
          return;
        }  
      throw new IllegalArgumentException("r must not be null");
    }
    
    public void requestExitAndWait() {
      synchronized (MultiFunctionRenderRuntimeEnvironment.sGLThreadManager) {
        this.mShouldExit = true;
        MultiFunctionRenderRuntimeEnvironment.sGLThreadManager.notifyAll();
        while (true) {
          boolean bool = this.mExited;
          if (!bool) {
            try {
              MultiFunctionRenderRuntimeEnvironment.sGLThreadManager.wait();
            } catch (InterruptedException interruptedException) {
              Thread.currentThread().interrupt();
            } 
            continue;
          } 
          return;
        } 
      } 
    }
    
    public void requestReleaseEglContextLocked() {
      this.mShouldReleaseEglContext = true;
      MultiFunctionRenderRuntimeEnvironment.sGLThreadManager.notifyAll();
    }
    
    public void requestRender() {
      synchronized (MultiFunctionRenderRuntimeEnvironment.sGLThreadManager) {
        this.mRequestRender = true;
        MultiFunctionRenderRuntimeEnvironment.sGLThreadManager.notifyAll();
        return;
      } 
    }
    
    public void requestRenderAndNotify(Runnable param1Runnable) {
      synchronized (MultiFunctionRenderRuntimeEnvironment.sGLThreadManager) {
        if (Thread.currentThread() == this)
          return; 
        this.mWantRenderNotification = true;
        this.mRequestRender = true;
        this.mRenderComplete = false;
        this.mFinishDrawingRunnable = param1Runnable;
        MultiFunctionRenderRuntimeEnvironment.sGLThreadManager.notifyAll();
        return;
      } 
    }
    
    public void run() {
      null = new StringBuilder();
      null.append("GLThread ");
      null.append(getId());
      setName(null.toString());
      try {
      
      } catch (InterruptedException interruptedException) {
      
      } finally {
        MultiFunctionRenderRuntimeEnvironment.sGLThreadManager.threadExiting(this);
      } 
      MultiFunctionRenderRuntimeEnvironment.sGLThreadManager.threadExiting(this);
    }
    
    public void setOnInputSurfaceTextureCallback(OnInputSurfaceTextureAvailableCallback param1OnInputSurfaceTextureAvailableCallback) {
      this.onInputSurfaceTextureCallback = param1OnInputSurfaceTextureAvailableCallback;
    }
    
    public void setRenderMode(int param1Int) {
      if (param1Int >= 0 && param1Int <= 1)
        synchronized (MultiFunctionRenderRuntimeEnvironment.sGLThreadManager) {
          this.mRenderMode = param1Int;
          MultiFunctionRenderRuntimeEnvironment.sGLThreadManager.notifyAll();
          return;
        }  
      throw new IllegalArgumentException("renderMode");
    }
    
    public void surfaceCreated() {
      synchronized (MultiFunctionRenderRuntimeEnvironment.sGLThreadManager) {
        this.mHasSurface = true;
        this.mFinishedCreatingEglSurface = false;
        MultiFunctionRenderRuntimeEnvironment.sGLThreadManager.notifyAll();
        while (this.mWaitingForSurface && !this.mFinishedCreatingEglSurface) {
          boolean bool = this.mExited;
          if (!bool)
            try {
              MultiFunctionRenderRuntimeEnvironment.sGLThreadManager.wait();
            } catch (InterruptedException interruptedException) {
              Thread.currentThread().interrupt();
            }  
        } 
        return;
      } 
    }
    
    public void surfaceDestroyed() {
      synchronized (MultiFunctionRenderRuntimeEnvironment.sGLThreadManager) {
        this.mHasSurface = false;
        MultiFunctionRenderRuntimeEnvironment.sGLThreadManager.notifyAll();
        while (!this.mWaitingForSurface) {
          boolean bool = this.mExited;
          if (!bool)
            try {
              MultiFunctionRenderRuntimeEnvironment.sGLThreadManager.wait();
            } catch (InterruptedException interruptedException) {
              Thread.currentThread().interrupt();
            }  
        } 
        return;
      } 
    }
    
    public static interface OnInputSurfaceTextureAvailableCallback {
      void onInputSurfaceTextureAvailable(SurfaceTexture param2SurfaceTexture, int param2Int1, int param2Int2);
      
      void onInputSurfaceTextureDestroyed(SurfaceTexture param2SurfaceTexture);
      
      void onInputSurfaceTextureSizeChanged(SurfaceTexture param2SurfaceTexture, int param2Int1, int param2Int2);
      
      void onInputSurfaceTextureUpdated(SurfaceTexture param2SurfaceTexture);
    }
  }
  
  class null implements SurfaceTexture.OnFrameAvailableListener {
    public void onFrameAvailable(SurfaceTexture param1SurfaceTexture) {
      if (this.this$0.onInputSurfaceTextureCallback != null)
        this.this$0.onInputSurfaceTextureCallback.onInputSurfaceTextureUpdated(this.this$0.inputSurfaceTexture); 
    }
  }
  
  public static interface OnInputSurfaceTextureAvailableCallback {
    void onInputSurfaceTextureAvailable(SurfaceTexture param1SurfaceTexture, int param1Int1, int param1Int2);
    
    void onInputSurfaceTextureDestroyed(SurfaceTexture param1SurfaceTexture);
    
    void onInputSurfaceTextureSizeChanged(SurfaceTexture param1SurfaceTexture, int param1Int1, int param1Int2);
    
    void onInputSurfaceTextureUpdated(SurfaceTexture param1SurfaceTexture);
  }
  
  private static class GLThreadManager {
    private static String TAG = "GLThreadManager";
    
    private GLThreadManager() {}
    
    public void releaseEglContextLocked(MultiFunctionRenderRuntimeEnvironment.GLThread param1GLThread) {
      notifyAll();
    }
    
    public void threadExiting(MultiFunctionRenderRuntimeEnvironment.GLThread param1GLThread) {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_1
      //   3: iconst_1
      //   4: invokestatic access$1702 : (Lcom/netopsun/ijkvideoview/widget/media/render/MultiFunctionRenderRuntimeEnvironment$GLThread;Z)Z
      //   7: pop
      //   8: aload_0
      //   9: invokevirtual notifyAll : ()V
      //   12: aload_0
      //   13: monitorexit
      //   14: return
      //   15: astore_1
      //   16: aload_0
      //   17: monitorexit
      //   18: aload_1
      //   19: athrow
      // Exception table:
      //   from	to	target	type
      //   2	12	15	finally
    }
  }
  
  public static interface GLWrapper {
    GL wrap(GL param1GL);
  }
  
  static class LogWriter extends Writer {
    private StringBuilder mBuilder = new StringBuilder();
    
    private void flushBuilder() {
      if (this.mBuilder.length() > 0) {
        Log.v("GLSurfaceView", this.mBuilder.toString());
        StringBuilder stringBuilder = this.mBuilder;
        stringBuilder.delete(0, stringBuilder.length());
      } 
    }
    
    public void close() {
      flushBuilder();
    }
    
    public void flush() {
      flushBuilder();
    }
    
    public void write(char[] param1ArrayOfchar, int param1Int1, int param1Int2) {
      for (byte b = 0; b < param1Int2; b++) {
        char c = param1ArrayOfchar[param1Int1 + b];
        if (c == '\n') {
          flushBuilder();
        } else {
          this.mBuilder.append(c);
        } 
      } 
    }
  }
  
  public static interface Renderer {
    void onDrawFrame(GL10 param1GL10, int param1Int, float[] param1ArrayOffloat);
    
    void onSurfaceChanged(GL10 param1GL10, int param1Int1, int param1Int2);
    
    void onSurfaceCreated(GL10 param1GL10, EGLConfig param1EGLConfig);
  }
  
  private class SimpleEGLConfigChooser extends ComponentSizeChooser {
    public SimpleEGLConfigChooser(boolean param1Boolean) {
      super(8, 8, 8, 0, bool, 0);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/ijkvideoview/widget/media/render/MultiFunctionRenderRuntimeEnvironment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */