package jp.co.cyberagent.android.gpuimage;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLDebugHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import java.io.Writer;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL10;

public class GLTextureView extends TextureView implements TextureView.SurfaceTextureListener, View.OnLayoutChangeListener {
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
  
  private static final String TAG = GLTextureView.class.getSimpleName();
  
  private static final GLThreadManager glThreadManager = new GLThreadManager();
  
  private int debugFlags;
  
  private boolean detached;
  
  private EGLConfigChooser eglConfigChooser;
  
  private int eglContextClientVersion;
  
  private EGLContextFactory eglContextFactory;
  
  private EGLWindowSurfaceFactory eglWindowSurfaceFactory;
  
  private GLThread glThread;
  
  private GLWrapper glWrapper;
  
  private final WeakReference<GLTextureView> mThisWeakRef = new WeakReference<GLTextureView>(this);
  
  private boolean preserveEGLContextOnPause;
  
  private Renderer renderer;
  
  private List<TextureView.SurfaceTextureListener> surfaceTextureListeners = new ArrayList<TextureView.SurfaceTextureListener>();
  
  public GLTextureView(Context paramContext) {
    super(paramContext);
    init();
  }
  
  public GLTextureView(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    init();
  }
  
  private void checkRenderThreadState() {
    if (this.glThread == null)
      return; 
    throw new IllegalStateException("setRenderer has already been called for this instance.");
  }
  
  private void init() {
    setSurfaceTextureListener(this);
  }
  
  public void addSurfaceTextureListener(TextureView.SurfaceTextureListener paramSurfaceTextureListener) {
    this.surfaceTextureListeners.add(paramSurfaceTextureListener);
  }
  
  protected void finalize() throws Throwable {
    try {
      if (this.glThread != null)
        this.glThread.requestExitAndWait(); 
      return;
    } finally {
      super.finalize();
    } 
  }
  
  public int getDebugFlags() {
    return this.debugFlags;
  }
  
  public boolean getPreserveEGLContextOnPause() {
    return this.preserveEGLContextOnPause;
  }
  
  public int getRenderMode() {
    return this.glThread.getRenderMode();
  }
  
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    if (this.detached && this.renderer != null) {
      boolean bool;
      GLThread gLThread = this.glThread;
      if (gLThread != null) {
        bool = gLThread.getRenderMode();
      } else {
        bool = true;
      } 
      this.glThread = new GLThread(this.mThisWeakRef);
      if (bool != true)
        this.glThread.setRenderMode(bool); 
      this.glThread.start();
    } 
    this.detached = false;
  }
  
  protected void onDetachedFromWindow() {
    GLThread gLThread = this.glThread;
    if (gLThread != null)
      gLThread.requestExitAndWait(); 
    this.detached = true;
    super.onDetachedFromWindow();
  }
  
  public void onLayoutChange(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8) {
    surfaceChanged(getSurfaceTexture(), 0, paramInt3 - paramInt1, paramInt4 - paramInt2);
  }
  
  public void onPause() {
    this.glThread.onPause();
  }
  
  public void onResume() {
    this.glThread.onResume();
  }
  
  public void onSurfaceTextureAvailable(SurfaceTexture paramSurfaceTexture, int paramInt1, int paramInt2) {
    surfaceCreated(paramSurfaceTexture);
    surfaceChanged(paramSurfaceTexture, 0, paramInt1, paramInt2);
    Iterator<TextureView.SurfaceTextureListener> iterator = this.surfaceTextureListeners.iterator();
    while (iterator.hasNext())
      ((TextureView.SurfaceTextureListener)iterator.next()).onSurfaceTextureAvailable(paramSurfaceTexture, paramInt1, paramInt2); 
  }
  
  public boolean onSurfaceTextureDestroyed(SurfaceTexture paramSurfaceTexture) {
    surfaceDestroyed(paramSurfaceTexture);
    Iterator<TextureView.SurfaceTextureListener> iterator = this.surfaceTextureListeners.iterator();
    while (iterator.hasNext())
      ((TextureView.SurfaceTextureListener)iterator.next()).onSurfaceTextureDestroyed(paramSurfaceTexture); 
    return true;
  }
  
  public void onSurfaceTextureSizeChanged(SurfaceTexture paramSurfaceTexture, int paramInt1, int paramInt2) {
    surfaceChanged(paramSurfaceTexture, 0, paramInt1, paramInt2);
    Iterator<TextureView.SurfaceTextureListener> iterator = this.surfaceTextureListeners.iterator();
    while (iterator.hasNext())
      ((TextureView.SurfaceTextureListener)iterator.next()).onSurfaceTextureSizeChanged(paramSurfaceTexture, paramInt1, paramInt2); 
  }
  
  public void onSurfaceTextureUpdated(SurfaceTexture paramSurfaceTexture) {
    requestRender();
    Iterator<TextureView.SurfaceTextureListener> iterator = this.surfaceTextureListeners.iterator();
    while (iterator.hasNext())
      ((TextureView.SurfaceTextureListener)iterator.next()).onSurfaceTextureUpdated(paramSurfaceTexture); 
  }
  
  public void queueEvent(Runnable paramRunnable) {
    this.glThread.queueEvent(paramRunnable);
  }
  
  public void requestRender() {
    this.glThread.requestRender();
  }
  
  public void setDebugFlags(int paramInt) {
    this.debugFlags = paramInt;
  }
  
  public void setEGLConfigChooser(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6) {
    setEGLConfigChooser(new ComponentSizeChooser(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6));
  }
  
  public void setEGLConfigChooser(EGLConfigChooser paramEGLConfigChooser) {
    checkRenderThreadState();
    this.eglConfigChooser = paramEGLConfigChooser;
  }
  
  public void setEGLConfigChooser(boolean paramBoolean) {
    setEGLConfigChooser(new SimpleEGLConfigChooser(paramBoolean));
  }
  
  public void setEGLContextClientVersion(int paramInt) {
    checkRenderThreadState();
    this.eglContextClientVersion = paramInt;
  }
  
  public void setEGLContextFactory(EGLContextFactory paramEGLContextFactory) {
    checkRenderThreadState();
    this.eglContextFactory = paramEGLContextFactory;
  }
  
  public void setEGLWindowSurfaceFactory(EGLWindowSurfaceFactory paramEGLWindowSurfaceFactory) {
    checkRenderThreadState();
    this.eglWindowSurfaceFactory = paramEGLWindowSurfaceFactory;
  }
  
  public void setGLWrapper(GLWrapper paramGLWrapper) {
    this.glWrapper = paramGLWrapper;
  }
  
  public void setPreserveEGLContextOnPause(boolean paramBoolean) {
    this.preserveEGLContextOnPause = paramBoolean;
  }
  
  public void setRenderMode(int paramInt) {
    this.glThread.setRenderMode(paramInt);
  }
  
  public void setRenderer(Renderer paramRenderer) {
    checkRenderThreadState();
    if (this.eglConfigChooser == null)
      this.eglConfigChooser = new SimpleEGLConfigChooser(true); 
    if (this.eglContextFactory == null)
      this.eglContextFactory = new DefaultContextFactory(); 
    if (this.eglWindowSurfaceFactory == null)
      this.eglWindowSurfaceFactory = new DefaultWindowSurfaceFactory(); 
    this.renderer = paramRenderer;
    this.glThread = new GLThread(this.mThisWeakRef);
    this.glThread.start();
  }
  
  public void surfaceChanged(SurfaceTexture paramSurfaceTexture, int paramInt1, int paramInt2, int paramInt3) {
    this.glThread.onWindowResize(paramInt2, paramInt3);
  }
  
  public void surfaceCreated(SurfaceTexture paramSurfaceTexture) {
    this.glThread.surfaceCreated();
  }
  
  public void surfaceDestroyed(SurfaceTexture paramSurfaceTexture) {
    this.glThread.surfaceDestroyed();
  }
  
  private abstract class BaseConfigChooser implements EGLConfigChooser {
    protected int[] mConfigSpec;
    
    public BaseConfigChooser(int[] param1ArrayOfint) {
      this.mConfigSpec = filterConfigSpec(param1ArrayOfint);
    }
    
    private int[] filterConfigSpec(int[] param1ArrayOfint) {
      if (GLTextureView.this.eglContextClientVersion != 2)
        return param1ArrayOfint; 
      int i = param1ArrayOfint.length;
      int[] arrayOfInt = new int[i + 2];
      int j = i - 1;
      System.arraycopy(param1ArrayOfint, 0, arrayOfInt, 0, j);
      arrayOfInt[j] = 12352;
      arrayOfInt[i] = 4;
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
    protected int alphaSize;
    
    protected int blueSize;
    
    protected int depthSize;
    
    protected int greenSize;
    
    protected int redSize;
    
    protected int stencilSize;
    
    private int[] value = new int[1];
    
    public ComponentSizeChooser(int param1Int1, int param1Int2, int param1Int3, int param1Int4, int param1Int5, int param1Int6) {
      super(new int[] { 
            12324, param1Int1, 12323, param1Int2, 12322, param1Int3, 12321, param1Int4, 12325, param1Int5, 
            12326, param1Int6, 12344 });
      this.redSize = param1Int1;
      this.greenSize = param1Int2;
      this.blueSize = param1Int3;
      this.alphaSize = param1Int4;
      this.depthSize = param1Int5;
      this.stencilSize = param1Int6;
    }
    
    private int findConfigAttrib(EGL10 param1EGL10, EGLDisplay param1EGLDisplay, EGLConfig param1EGLConfig, int param1Int1, int param1Int2) {
      return param1EGL10.eglGetConfigAttrib(param1EGLDisplay, param1EGLConfig, param1Int1, this.value) ? this.value[0] : param1Int2;
    }
    
    public EGLConfig chooseConfig(EGL10 param1EGL10, EGLDisplay param1EGLDisplay, EGLConfig[] param1ArrayOfEGLConfig) {
      int i = param1ArrayOfEGLConfig.length;
      for (byte b = 0; b < i; b++) {
        EGLConfig eGLConfig = param1ArrayOfEGLConfig[b];
        int j = findConfigAttrib(param1EGL10, param1EGLDisplay, eGLConfig, 12325, 0);
        int k = findConfigAttrib(param1EGL10, param1EGLDisplay, eGLConfig, 12326, 0);
        if (j >= this.depthSize && k >= this.stencilSize) {
          k = findConfigAttrib(param1EGL10, param1EGLDisplay, eGLConfig, 12324, 0);
          int m = findConfigAttrib(param1EGL10, param1EGLDisplay, eGLConfig, 12323, 0);
          j = findConfigAttrib(param1EGL10, param1EGLDisplay, eGLConfig, 12322, 0);
          int n = findConfigAttrib(param1EGL10, param1EGLDisplay, eGLConfig, 12321, 0);
          if (k == this.redSize && m == this.greenSize && j == this.blueSize && n == this.alphaSize)
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
      arrayOfInt[1] = GLTextureView.this.eglContextClientVersion;
      arrayOfInt[2] = 12344;
      EGLContext eGLContext = EGL10.EGL_NO_CONTEXT;
      if (GLTextureView.this.eglContextClientVersion == 0)
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
        GLTextureView.EglHelper.throwEglException("eglDestroyContex", param1EGL10.eglGetError());
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
        Log.e(GLTextureView.TAG, "eglCreateWindowSurface", illegalArgumentException1);
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
    EGL10 egl;
    
    EGLConfig eglConfig;
    
    EGLContext eglContext;
    
    EGLDisplay eglDisplay;
    
    EGLSurface eglSurface;
    
    private WeakReference<GLTextureView> glTextureViewWeakRef;
    
    public EglHelper(WeakReference<GLTextureView> param1WeakReference) {
      this.glTextureViewWeakRef = param1WeakReference;
    }
    
    private void destroySurfaceImp() {
      EGLSurface eGLSurface = this.eglSurface;
      if (eGLSurface != null && eGLSurface != EGL10.EGL_NO_SURFACE) {
        this.egl.eglMakeCurrent(this.eglDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
        GLTextureView gLTextureView = this.glTextureViewWeakRef.get();
        if (gLTextureView != null)
          gLTextureView.eglWindowSurfaceFactory.destroySurface(this.egl, this.eglDisplay, this.eglSurface); 
        this.eglSurface = null;
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
      throwEglException(param1String, this.egl.eglGetError());
    }
    
    public static void throwEglException(String param1String, int param1Int) {
      throw new RuntimeException(formatEglError(param1String, param1Int));
    }
    
    GL createGL() {
      GL gL1 = this.eglContext.getGL();
      GLTextureView gLTextureView = this.glTextureViewWeakRef.get();
      GL gL2 = gL1;
      if (gLTextureView != null) {
        GL gL = gL1;
        if (gLTextureView.glWrapper != null)
          gL = gLTextureView.glWrapper.wrap(gL1); 
        gL2 = gL;
        if ((gLTextureView.debugFlags & 0x3) != 0) {
          GLTextureView.LogWriter logWriter;
          boolean bool = false;
          gL2 = null;
          if ((gLTextureView.debugFlags & 0x1) != 0)
            bool = true; 
          if ((gLTextureView.debugFlags & 0x2) != 0)
            logWriter = new GLTextureView.LogWriter(); 
          gL2 = GLDebugHelper.wrap(gL, bool, logWriter);
        } 
      } 
      return gL2;
    }
    
    public boolean createSurface() {
      if (this.egl != null) {
        if (this.eglDisplay != null) {
          if (this.eglConfig != null) {
            destroySurfaceImp();
            GLTextureView gLTextureView = this.glTextureViewWeakRef.get();
            if (gLTextureView != null) {
              this.eglSurface = gLTextureView.eglWindowSurfaceFactory.createWindowSurface(this.egl, this.eglDisplay, this.eglConfig, gLTextureView.getSurfaceTexture());
            } else {
              this.eglSurface = null;
            } 
            EGLSurface eGLSurface = this.eglSurface;
            if (eGLSurface == null || eGLSurface == EGL10.EGL_NO_SURFACE) {
              if (this.egl.eglGetError() == 12299)
                Log.e("EglHelper", "createWindowSurface returned EGL_BAD_NATIVE_WINDOW."); 
              return false;
            } 
            EGL10 eGL10 = this.egl;
            EGLDisplay eGLDisplay = this.eglDisplay;
            eGLSurface = this.eglSurface;
            if (!eGL10.eglMakeCurrent(eGLDisplay, eGLSurface, eGLSurface, this.eglContext)) {
              logEglErrorAsWarning("EGLHelper", "eglMakeCurrent", this.egl.eglGetError());
              return false;
            } 
            return true;
          } 
          throw new RuntimeException("eglConfig not initialized");
        } 
        throw new RuntimeException("eglDisplay not initialized");
      } 
      throw new RuntimeException("egl not initialized");
    }
    
    public void destroySurface() {
      destroySurfaceImp();
    }
    
    public void finish() {
      if (this.eglContext != null) {
        GLTextureView gLTextureView = this.glTextureViewWeakRef.get();
        if (gLTextureView != null)
          gLTextureView.eglContextFactory.destroyContext(this.egl, this.eglDisplay, this.eglContext); 
        this.eglContext = null;
      } 
      EGLDisplay eGLDisplay = this.eglDisplay;
      if (eGLDisplay != null) {
        this.egl.eglTerminate(eGLDisplay);
        this.eglDisplay = null;
      } 
    }
    
    public void start() {
      this.egl = (EGL10)EGLContext.getEGL();
      this.eglDisplay = this.egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
      if (this.eglDisplay != EGL10.EGL_NO_DISPLAY) {
        int[] arrayOfInt = new int[2];
        if (this.egl.eglInitialize(this.eglDisplay, arrayOfInt)) {
          GLTextureView gLTextureView = this.glTextureViewWeakRef.get();
          if (gLTextureView == null) {
            this.eglConfig = null;
            this.eglContext = null;
          } else {
            this.eglConfig = gLTextureView.eglConfigChooser.chooseConfig(this.egl, this.eglDisplay);
            this.eglContext = gLTextureView.eglContextFactory.createContext(this.egl, this.eglDisplay, this.eglConfig);
          } 
          EGLContext eGLContext = this.eglContext;
          if (eGLContext == null || eGLContext == EGL10.EGL_NO_CONTEXT) {
            this.eglContext = null;
            throwEglException("createContext");
          } 
          this.eglSurface = null;
          return;
        } 
        throw new RuntimeException("eglInitialize failed");
      } 
      throw new RuntimeException("eglGetDisplay failed");
    }
    
    public int swap() {
      return !this.egl.eglSwapBuffers(this.eglDisplay, this.eglSurface) ? this.egl.eglGetError() : 12288;
    }
  }
  
  static class GLThread extends Thread {
    private GLTextureView.EglHelper eglHelper;
    
    private ArrayList<Runnable> eventQueue = new ArrayList<Runnable>();
    
    private boolean exited;
    
    private WeakReference<GLTextureView> glTextureViewWeakRef;
    
    private boolean hasSurface;
    
    private boolean haveEglContext;
    
    private boolean haveEglSurface;
    
    private int height = 0;
    
    private boolean paused;
    
    private boolean renderComplete;
    
    private int renderMode = 1;
    
    private boolean requestPaused;
    
    private boolean requestRender = true;
    
    private boolean shouldExit;
    
    private boolean shouldReleaseEglContext;
    
    private boolean sizeChanged = true;
    
    private boolean surfaceIsBad;
    
    private boolean waitingForSurface;
    
    private int width = 0;
    
    GLThread(WeakReference<GLTextureView> param1WeakReference) {
      this.glTextureViewWeakRef = param1WeakReference;
    }
    
    private void guardedRun() throws InterruptedException {
      // Byte code:
      //   0: aload_0
      //   1: new jp/co/cyberagent/android/gpuimage/GLTextureView$EglHelper
      //   4: dup
      //   5: aload_0
      //   6: getfield glTextureViewWeakRef : Ljava/lang/ref/WeakReference;
      //   9: invokespecial <init> : (Ljava/lang/ref/WeakReference;)V
      //   12: putfield eglHelper : Ljp/co/cyberagent/android/gpuimage/GLTextureView$EglHelper;
      //   15: aload_0
      //   16: iconst_0
      //   17: putfield haveEglContext : Z
      //   20: aload_0
      //   21: iconst_0
      //   22: putfield haveEglSurface : Z
      //   25: iconst_0
      //   26: istore_1
      //   27: iconst_0
      //   28: istore_2
      //   29: iconst_0
      //   30: istore_3
      //   31: aconst_null
      //   32: astore #4
      //   34: iconst_0
      //   35: istore #5
      //   37: iconst_0
      //   38: istore #6
      //   40: iconst_0
      //   41: istore #7
      //   43: iconst_0
      //   44: istore #8
      //   46: iconst_0
      //   47: istore #9
      //   49: iconst_0
      //   50: istore #10
      //   52: iconst_0
      //   53: istore #11
      //   55: aconst_null
      //   56: astore #12
      //   58: invokestatic access$900 : ()Ljp/co/cyberagent/android/gpuimage/GLTextureView$GLThreadManager;
      //   61: astore #13
      //   63: aload #13
      //   65: monitorenter
      //   66: iload #11
      //   68: istore #14
      //   70: iload #7
      //   72: istore #11
      //   74: iload_3
      //   75: istore #7
      //   77: aload_0
      //   78: getfield shouldExit : Z
      //   81: ifeq -> 115
      //   84: aload #13
      //   86: monitorexit
      //   87: invokestatic access$900 : ()Ljp/co/cyberagent/android/gpuimage/GLTextureView$GLThreadManager;
      //   90: astore #4
      //   92: aload #4
      //   94: monitorenter
      //   95: aload_0
      //   96: invokespecial stopEglSurfaceLocked : ()V
      //   99: aload_0
      //   100: invokespecial stopEglContextLocked : ()V
      //   103: aload #4
      //   105: monitorexit
      //   106: return
      //   107: astore #12
      //   109: aload #4
      //   111: monitorexit
      //   112: aload #12
      //   114: athrow
      //   115: aload_0
      //   116: getfield eventQueue : Ljava/util/ArrayList;
      //   119: invokevirtual isEmpty : ()Z
      //   122: ifne -> 152
      //   125: aload_0
      //   126: getfield eventQueue : Ljava/util/ArrayList;
      //   129: iconst_0
      //   130: invokevirtual remove : (I)Ljava/lang/Object;
      //   133: checkcast java/lang/Runnable
      //   136: astore #12
      //   138: iload #7
      //   140: istore_3
      //   141: iload #11
      //   143: istore #7
      //   145: iload #14
      //   147: istore #11
      //   149: goto -> 642
      //   152: aload_0
      //   153: getfield paused : Z
      //   156: aload_0
      //   157: getfield requestPaused : Z
      //   160: if_icmpeq -> 186
      //   163: aload_0
      //   164: getfield requestPaused : Z
      //   167: istore #15
      //   169: aload_0
      //   170: aload_0
      //   171: getfield requestPaused : Z
      //   174: putfield paused : Z
      //   177: invokestatic access$900 : ()Ljp/co/cyberagent/android/gpuimage/GLTextureView$GLThreadManager;
      //   180: invokevirtual notifyAll : ()V
      //   183: goto -> 189
      //   186: iconst_0
      //   187: istore #15
      //   189: aload_0
      //   190: getfield shouldReleaseEglContext : Z
      //   193: ifeq -> 211
      //   196: aload_0
      //   197: invokespecial stopEglSurfaceLocked : ()V
      //   200: aload_0
      //   201: invokespecial stopEglContextLocked : ()V
      //   204: aload_0
      //   205: iconst_0
      //   206: putfield shouldReleaseEglContext : Z
      //   209: iconst_1
      //   210: istore_2
      //   211: iload_1
      //   212: istore #16
      //   214: iload_1
      //   215: ifeq -> 229
      //   218: aload_0
      //   219: invokespecial stopEglSurfaceLocked : ()V
      //   222: aload_0
      //   223: invokespecial stopEglContextLocked : ()V
      //   226: iconst_0
      //   227: istore #16
      //   229: iload #15
      //   231: ifeq -> 245
      //   234: aload_0
      //   235: getfield haveEglSurface : Z
      //   238: ifeq -> 245
      //   241: aload_0
      //   242: invokespecial stopEglSurfaceLocked : ()V
      //   245: iload #15
      //   247: ifeq -> 305
      //   250: aload_0
      //   251: getfield haveEglContext : Z
      //   254: ifeq -> 305
      //   257: aload_0
      //   258: getfield glTextureViewWeakRef : Ljava/lang/ref/WeakReference;
      //   261: invokevirtual get : ()Ljava/lang/Object;
      //   264: checkcast jp/co/cyberagent/android/gpuimage/GLTextureView
      //   267: astore #17
      //   269: aload #17
      //   271: ifnonnull -> 280
      //   274: iconst_0
      //   275: istore #18
      //   277: goto -> 287
      //   280: aload #17
      //   282: invokestatic access$1000 : (Ljp/co/cyberagent/android/gpuimage/GLTextureView;)Z
      //   285: istore #18
      //   287: iload #18
      //   289: ifeq -> 301
      //   292: invokestatic access$900 : ()Ljp/co/cyberagent/android/gpuimage/GLTextureView$GLThreadManager;
      //   295: invokevirtual shouldReleaseEGLContextWhenPausing : ()Z
      //   298: ifeq -> 305
      //   301: aload_0
      //   302: invokespecial stopEglContextLocked : ()V
      //   305: iload #15
      //   307: ifeq -> 326
      //   310: invokestatic access$900 : ()Ljp/co/cyberagent/android/gpuimage/GLTextureView$GLThreadManager;
      //   313: invokevirtual shouldTerminateEGLWhenPausing : ()Z
      //   316: ifeq -> 326
      //   319: aload_0
      //   320: getfield eglHelper : Ljp/co/cyberagent/android/gpuimage/GLTextureView$EglHelper;
      //   323: invokevirtual finish : ()V
      //   326: aload_0
      //   327: getfield hasSurface : Z
      //   330: ifne -> 367
      //   333: aload_0
      //   334: getfield waitingForSurface : Z
      //   337: ifne -> 367
      //   340: aload_0
      //   341: getfield haveEglSurface : Z
      //   344: ifeq -> 351
      //   347: aload_0
      //   348: invokespecial stopEglSurfaceLocked : ()V
      //   351: aload_0
      //   352: iconst_1
      //   353: putfield waitingForSurface : Z
      //   356: aload_0
      //   357: iconst_0
      //   358: putfield surfaceIsBad : Z
      //   361: invokestatic access$900 : ()Ljp/co/cyberagent/android/gpuimage/GLTextureView$GLThreadManager;
      //   364: invokevirtual notifyAll : ()V
      //   367: aload_0
      //   368: getfield hasSurface : Z
      //   371: ifeq -> 392
      //   374: aload_0
      //   375: getfield waitingForSurface : Z
      //   378: ifeq -> 392
      //   381: aload_0
      //   382: iconst_0
      //   383: putfield waitingForSurface : Z
      //   386: invokestatic access$900 : ()Ljp/co/cyberagent/android/gpuimage/GLTextureView$GLThreadManager;
      //   389: invokevirtual notifyAll : ()V
      //   392: iload #7
      //   394: istore_3
      //   395: iload #7
      //   397: ifeq -> 416
      //   400: aload_0
      //   401: iconst_1
      //   402: putfield renderComplete : Z
      //   405: invokestatic access$900 : ()Ljp/co/cyberagent/android/gpuimage/GLTextureView$GLThreadManager;
      //   408: invokevirtual notifyAll : ()V
      //   411: iconst_0
      //   412: istore_3
      //   413: iconst_0
      //   414: istore #14
      //   416: iload_2
      //   417: istore #7
      //   419: iload #11
      //   421: istore #19
      //   423: iload #8
      //   425: istore #20
      //   427: iload #9
      //   429: istore #21
      //   431: iload #10
      //   433: istore #22
      //   435: aload_0
      //   436: invokespecial readyToDraw : ()Z
      //   439: ifeq -> 1038
      //   442: iload_2
      //   443: istore_1
      //   444: iload #11
      //   446: istore #7
      //   448: aload_0
      //   449: getfield haveEglContext : Z
      //   452: ifne -> 526
      //   455: iload_2
      //   456: ifeq -> 468
      //   459: iconst_0
      //   460: istore_1
      //   461: iload #11
      //   463: istore #7
      //   465: goto -> 526
      //   468: invokestatic access$900 : ()Ljp/co/cyberagent/android/gpuimage/GLTextureView$GLThreadManager;
      //   471: aload_0
      //   472: invokevirtual tryAcquireEglContextLocked : (Ljp/co/cyberagent/android/gpuimage/GLTextureView$GLThread;)Z
      //   475: istore #15
      //   477: iload_2
      //   478: istore_1
      //   479: iload #11
      //   481: istore #7
      //   483: iload #15
      //   485: ifeq -> 526
      //   488: aload_0
      //   489: getfield eglHelper : Ljp/co/cyberagent/android/gpuimage/GLTextureView$EglHelper;
      //   492: invokevirtual start : ()V
      //   495: aload_0
      //   496: iconst_1
      //   497: putfield haveEglContext : Z
      //   500: invokestatic access$900 : ()Ljp/co/cyberagent/android/gpuimage/GLTextureView$GLThreadManager;
      //   503: invokevirtual notifyAll : ()V
      //   506: iconst_1
      //   507: istore #7
      //   509: iload_2
      //   510: istore_1
      //   511: goto -> 526
      //   514: astore #4
      //   516: invokestatic access$900 : ()Ljp/co/cyberagent/android/gpuimage/GLTextureView$GLThreadManager;
      //   519: aload_0
      //   520: invokevirtual releaseEglContextLocked : (Ljp/co/cyberagent/android/gpuimage/GLTextureView$GLThread;)V
      //   523: aload #4
      //   525: athrow
      //   526: aload_0
      //   527: getfield haveEglContext : Z
      //   530: ifeq -> 557
      //   533: aload_0
      //   534: getfield haveEglSurface : Z
      //   537: ifne -> 557
      //   540: aload_0
      //   541: iconst_1
      //   542: putfield haveEglSurface : Z
      //   545: iconst_1
      //   546: istore #10
      //   548: iconst_1
      //   549: istore #9
      //   551: iconst_1
      //   552: istore #8
      //   554: goto -> 569
      //   557: iload #8
      //   559: istore #11
      //   561: iload #10
      //   563: istore #8
      //   565: iload #11
      //   567: istore #10
      //   569: aload_0
      //   570: getfield haveEglSurface : Z
      //   573: ifeq -> 1019
      //   576: aload_0
      //   577: getfield sizeChanged : Z
      //   580: ifeq -> 612
      //   583: aload_0
      //   584: getfield width : I
      //   587: istore #5
      //   589: aload_0
      //   590: getfield height : I
      //   593: istore #6
      //   595: aload_0
      //   596: iconst_0
      //   597: putfield sizeChanged : Z
      //   600: iconst_1
      //   601: istore #10
      //   603: iconst_1
      //   604: istore #8
      //   606: iconst_1
      //   607: istore #11
      //   609: goto -> 616
      //   612: iload #14
      //   614: istore #11
      //   616: aload_0
      //   617: iconst_0
      //   618: putfield requestRender : Z
      //   621: invokestatic access$900 : ()Ljp/co/cyberagent/android/gpuimage/GLTextureView$GLThreadManager;
      //   624: invokevirtual notifyAll : ()V
      //   627: iload #10
      //   629: istore_2
      //   630: iload #8
      //   632: istore #10
      //   634: iload_2
      //   635: istore #8
      //   637: iload_1
      //   638: istore_2
      //   639: iload #16
      //   641: istore_1
      //   642: aload #13
      //   644: monitorexit
      //   645: aload #12
      //   647: ifnull -> 660
      //   650: aload #12
      //   652: invokeinterface run : ()V
      //   657: goto -> 55
      //   660: iload #8
      //   662: istore #16
      //   664: iload #8
      //   666: ifeq -> 737
      //   669: aload_0
      //   670: getfield eglHelper : Ljp/co/cyberagent/android/gpuimage/GLTextureView$EglHelper;
      //   673: invokevirtual createSurface : ()Z
      //   676: ifne -> 734
      //   679: invokestatic access$900 : ()Ljp/co/cyberagent/android/gpuimage/GLTextureView$GLThreadManager;
      //   682: astore #13
      //   684: aload #13
      //   686: monitorenter
      //   687: aload_0
      //   688: iconst_1
      //   689: putfield surfaceIsBad : Z
      //   692: invokestatic access$900 : ()Ljp/co/cyberagent/android/gpuimage/GLTextureView$GLThreadManager;
      //   695: invokevirtual notifyAll : ()V
      //   698: aload #13
      //   700: monitorexit
      //   701: iload #8
      //   703: istore #14
      //   705: aload #4
      //   707: astore #13
      //   709: iload_1
      //   710: istore #20
      //   712: iload #20
      //   714: istore_1
      //   715: aload #13
      //   717: astore #4
      //   719: iload #14
      //   721: istore #8
      //   723: goto -> 58
      //   726: astore #4
      //   728: aload #13
      //   730: monitorexit
      //   731: aload #4
      //   733: athrow
      //   734: iconst_0
      //   735: istore #16
      //   737: iload #9
      //   739: istore #8
      //   741: iload #9
      //   743: ifeq -> 769
      //   746: aload_0
      //   747: getfield eglHelper : Ljp/co/cyberagent/android/gpuimage/GLTextureView$EglHelper;
      //   750: invokevirtual createGL : ()Ljavax/microedition/khronos/opengles/GL;
      //   753: checkcast javax/microedition/khronos/opengles/GL10
      //   756: astore #4
      //   758: invokestatic access$900 : ()Ljp/co/cyberagent/android/gpuimage/GLTextureView$GLThreadManager;
      //   761: aload #4
      //   763: invokevirtual checkGLDriver : (Ljavax/microedition/khronos/opengles/GL10;)V
      //   766: iconst_0
      //   767: istore #8
      //   769: iload #7
      //   771: istore #22
      //   773: iload #7
      //   775: ifeq -> 817
      //   778: aload_0
      //   779: getfield glTextureViewWeakRef : Ljava/lang/ref/WeakReference;
      //   782: invokevirtual get : ()Ljava/lang/Object;
      //   785: checkcast jp/co/cyberagent/android/gpuimage/GLTextureView
      //   788: astore #13
      //   790: aload #13
      //   792: ifnull -> 814
      //   795: aload #13
      //   797: invokestatic access$1100 : (Ljp/co/cyberagent/android/gpuimage/GLTextureView;)Ljp/co/cyberagent/android/gpuimage/GLTextureView$Renderer;
      //   800: aload #4
      //   802: aload_0
      //   803: getfield eglHelper : Ljp/co/cyberagent/android/gpuimage/GLTextureView$EglHelper;
      //   806: getfield eglConfig : Ljavax/microedition/khronos/egl/EGLConfig;
      //   809: invokeinterface onSurfaceCreated : (Ljavax/microedition/khronos/opengles/GL10;Ljavax/microedition/khronos/egl/EGLConfig;)V
      //   814: iconst_0
      //   815: istore #22
      //   817: iload #10
      //   819: istore #21
      //   821: iload #10
      //   823: ifeq -> 862
      //   826: aload_0
      //   827: getfield glTextureViewWeakRef : Ljava/lang/ref/WeakReference;
      //   830: invokevirtual get : ()Ljava/lang/Object;
      //   833: checkcast jp/co/cyberagent/android/gpuimage/GLTextureView
      //   836: astore #13
      //   838: aload #13
      //   840: ifnull -> 859
      //   843: aload #13
      //   845: invokestatic access$1100 : (Ljp/co/cyberagent/android/gpuimage/GLTextureView;)Ljp/co/cyberagent/android/gpuimage/GLTextureView$Renderer;
      //   848: aload #4
      //   850: iload #5
      //   852: iload #6
      //   854: invokeinterface onSurfaceChanged : (Ljavax/microedition/khronos/opengles/GL10;II)V
      //   859: iconst_0
      //   860: istore #21
      //   862: aload_0
      //   863: getfield glTextureViewWeakRef : Ljava/lang/ref/WeakReference;
      //   866: invokevirtual get : ()Ljava/lang/Object;
      //   869: checkcast jp/co/cyberagent/android/gpuimage/GLTextureView
      //   872: astore #13
      //   874: aload #13
      //   876: ifnull -> 891
      //   879: aload #13
      //   881: invokestatic access$1100 : (Ljp/co/cyberagent/android/gpuimage/GLTextureView;)Ljp/co/cyberagent/android/gpuimage/GLTextureView$Renderer;
      //   884: aload #4
      //   886: invokeinterface onDrawFrame : (Ljavax/microedition/khronos/opengles/GL10;)V
      //   891: aload_0
      //   892: getfield eglHelper : Ljp/co/cyberagent/android/gpuimage/GLTextureView$EglHelper;
      //   895: invokevirtual swap : ()I
      //   898: istore #10
      //   900: iload #10
      //   902: sipush #12288
      //   905: if_icmpeq -> 963
      //   908: iload #10
      //   910: sipush #12302
      //   913: if_icmpeq -> 958
      //   916: ldc 'GLThread'
      //   918: ldc 'eglSwapBuffers'
      //   920: iload #10
      //   922: invokestatic logEglErrorAsWarning : (Ljava/lang/String;Ljava/lang/String;I)V
      //   925: invokestatic access$900 : ()Ljp/co/cyberagent/android/gpuimage/GLTextureView$GLThreadManager;
      //   928: astore #13
      //   930: aload #13
      //   932: monitorenter
      //   933: aload_0
      //   934: iconst_1
      //   935: putfield surfaceIsBad : Z
      //   938: invokestatic access$900 : ()Ljp/co/cyberagent/android/gpuimage/GLTextureView$GLThreadManager;
      //   941: invokevirtual notifyAll : ()V
      //   944: aload #13
      //   946: monitorexit
      //   947: goto -> 963
      //   950: astore #4
      //   952: aload #13
      //   954: monitorexit
      //   955: aload #4
      //   957: athrow
      //   958: iconst_1
      //   959: istore_1
      //   960: goto -> 963
      //   963: iload_1
      //   964: istore #20
      //   966: aload #4
      //   968: astore #13
      //   970: iload #22
      //   972: istore #7
      //   974: iload #16
      //   976: istore #14
      //   978: iload #8
      //   980: istore #9
      //   982: iload #21
      //   984: istore #10
      //   986: iload #11
      //   988: ifeq -> 712
      //   991: iconst_1
      //   992: istore_3
      //   993: iload_1
      //   994: istore #20
      //   996: aload #4
      //   998: astore #13
      //   1000: iload #22
      //   1002: istore #7
      //   1004: iload #16
      //   1006: istore #14
      //   1008: iload #8
      //   1010: istore #9
      //   1012: iload #21
      //   1014: istore #10
      //   1016: goto -> 712
      //   1019: iload #8
      //   1021: istore #22
      //   1023: iload #9
      //   1025: istore #21
      //   1027: iload #10
      //   1029: istore #20
      //   1031: iload #7
      //   1033: istore #19
      //   1035: iload_1
      //   1036: istore #7
      //   1038: invokestatic access$900 : ()Ljp/co/cyberagent/android/gpuimage/GLTextureView$GLThreadManager;
      //   1041: invokevirtual wait : ()V
      //   1044: iload #16
      //   1046: istore_1
      //   1047: iload #7
      //   1049: istore_2
      //   1050: iload_3
      //   1051: istore #7
      //   1053: iload #19
      //   1055: istore #11
      //   1057: iload #20
      //   1059: istore #8
      //   1061: iload #21
      //   1063: istore #9
      //   1065: iload #22
      //   1067: istore #10
      //   1069: goto -> 77
      //   1072: astore #4
      //   1074: aload #13
      //   1076: monitorexit
      //   1077: aload #4
      //   1079: athrow
      //   1080: astore #12
      //   1082: invokestatic access$900 : ()Ljp/co/cyberagent/android/gpuimage/GLTextureView$GLThreadManager;
      //   1085: astore #4
      //   1087: aload #4
      //   1089: monitorenter
      //   1090: aload_0
      //   1091: invokespecial stopEglSurfaceLocked : ()V
      //   1094: aload_0
      //   1095: invokespecial stopEglContextLocked : ()V
      //   1098: aload #4
      //   1100: monitorexit
      //   1101: aload #12
      //   1103: athrow
      //   1104: astore #12
      //   1106: aload #4
      //   1108: monitorexit
      //   1109: goto -> 1115
      //   1112: aload #12
      //   1114: athrow
      //   1115: goto -> 1112
      // Exception table:
      //   from	to	target	type
      //   58	66	1080	finally
      //   77	87	1072	finally
      //   95	106	107	finally
      //   109	112	107	finally
      //   115	138	1072	finally
      //   152	183	1072	finally
      //   189	209	1072	finally
      //   218	226	1072	finally
      //   234	245	1072	finally
      //   250	269	1072	finally
      //   280	287	1072	finally
      //   292	301	1072	finally
      //   301	305	1072	finally
      //   310	326	1072	finally
      //   326	351	1072	finally
      //   351	367	1072	finally
      //   367	392	1072	finally
      //   400	411	1072	finally
      //   435	442	1072	finally
      //   448	455	1072	finally
      //   468	477	1072	finally
      //   488	495	514	java/lang/RuntimeException
      //   488	495	1072	finally
      //   495	506	1072	finally
      //   516	526	1072	finally
      //   526	545	1072	finally
      //   569	600	1072	finally
      //   616	627	1072	finally
      //   642	645	1072	finally
      //   650	657	1080	finally
      //   669	687	1080	finally
      //   687	701	726	finally
      //   728	731	726	finally
      //   731	734	1080	finally
      //   746	766	1080	finally
      //   778	790	1080	finally
      //   795	814	1080	finally
      //   826	838	1080	finally
      //   843	859	1080	finally
      //   862	874	1080	finally
      //   879	891	1080	finally
      //   891	900	1080	finally
      //   916	933	1080	finally
      //   933	947	950	finally
      //   952	955	950	finally
      //   955	958	1080	finally
      //   1038	1044	1072	finally
      //   1074	1077	1072	finally
      //   1077	1080	1080	finally
      //   1090	1101	1104	finally
      //   1106	1109	1104	finally
    }
    
    private boolean readyToDraw() {
      null = this.paused;
      boolean bool = true;
      if (!null && this.hasSurface && !this.surfaceIsBad && this.width > 0 && this.height > 0) {
        null = bool;
        if (!this.requestRender) {
          if (this.renderMode == 1)
            return bool; 
        } else {
          return null;
        } 
      } 
      return false;
    }
    
    private void stopEglContextLocked() {
      if (this.haveEglContext) {
        this.eglHelper.finish();
        this.haveEglContext = false;
        GLTextureView.glThreadManager.releaseEglContextLocked(this);
      } 
    }
    
    private void stopEglSurfaceLocked() {
      if (this.haveEglSurface) {
        this.haveEglSurface = false;
        this.eglHelper.destroySurface();
      } 
    }
    
    public boolean ableToDraw() {
      boolean bool;
      if (this.haveEglContext && this.haveEglSurface && readyToDraw()) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public int getRenderMode() {
      synchronized (GLTextureView.glThreadManager) {
        return this.renderMode;
      } 
    }
    
    public void onPause() {
      synchronized (GLTextureView.glThreadManager) {
        this.requestPaused = true;
        GLTextureView.glThreadManager.notifyAll();
        while (!this.exited) {
          boolean bool = this.paused;
          if (!bool)
            try {
              GLTextureView.glThreadManager.wait();
            } catch (InterruptedException interruptedException) {
              Thread.currentThread().interrupt();
            }  
        } 
        return;
      } 
    }
    
    public void onResume() {
      synchronized (GLTextureView.glThreadManager) {
        this.requestPaused = false;
        this.requestRender = true;
        this.renderComplete = false;
        GLTextureView.glThreadManager.notifyAll();
        while (!this.exited && this.paused) {
          boolean bool = this.renderComplete;
          if (!bool)
            try {
              GLTextureView.glThreadManager.wait();
            } catch (InterruptedException interruptedException) {
              Thread.currentThread().interrupt();
            }  
        } 
        return;
      } 
    }
    
    public void onWindowResize(int param1Int1, int param1Int2) {
      synchronized (GLTextureView.glThreadManager) {
        this.width = param1Int1;
        this.height = param1Int2;
        this.sizeChanged = true;
        this.requestRender = true;
        this.renderComplete = false;
        GLTextureView.glThreadManager.notifyAll();
        while (!this.exited && !this.paused && !this.renderComplete) {
          boolean bool = ableToDraw();
          if (bool)
            try {
              GLTextureView.glThreadManager.wait();
            } catch (InterruptedException interruptedException) {
              Thread.currentThread().interrupt();
            }  
        } 
        return;
      } 
    }
    
    public void queueEvent(Runnable param1Runnable) {
      if (param1Runnable != null)
        synchronized (GLTextureView.glThreadManager) {
          this.eventQueue.add(param1Runnable);
          GLTextureView.glThreadManager.notifyAll();
          return;
        }  
      throw new IllegalArgumentException("r must not be null");
    }
    
    public void requestExitAndWait() {
      synchronized (GLTextureView.glThreadManager) {
        this.shouldExit = true;
        GLTextureView.glThreadManager.notifyAll();
        while (true) {
          boolean bool = this.exited;
          if (!bool) {
            try {
              GLTextureView.glThreadManager.wait();
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
      this.shouldReleaseEglContext = true;
      GLTextureView.glThreadManager.notifyAll();
    }
    
    public void requestRender() {
      synchronized (GLTextureView.glThreadManager) {
        this.requestRender = true;
        GLTextureView.glThreadManager.notifyAll();
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
        GLTextureView.glThreadManager.threadExiting(this);
      } 
      GLTextureView.glThreadManager.threadExiting(this);
    }
    
    public void setRenderMode(int param1Int) {
      if (param1Int >= 0 && param1Int <= 1)
        synchronized (GLTextureView.glThreadManager) {
          this.renderMode = param1Int;
          GLTextureView.glThreadManager.notifyAll();
          return;
        }  
      throw new IllegalArgumentException("renderMode");
    }
    
    public void surfaceCreated() {
      synchronized (GLTextureView.glThreadManager) {
        this.hasSurface = true;
        GLTextureView.glThreadManager.notifyAll();
        while (this.waitingForSurface) {
          boolean bool = this.exited;
          if (!bool)
            try {
              GLTextureView.glThreadManager.wait();
            } catch (InterruptedException interruptedException) {
              Thread.currentThread().interrupt();
            }  
        } 
        return;
      } 
    }
    
    public void surfaceDestroyed() {
      synchronized (GLTextureView.glThreadManager) {
        this.hasSurface = false;
        GLTextureView.glThreadManager.notifyAll();
        while (!this.waitingForSurface) {
          boolean bool = this.exited;
          if (!bool)
            try {
              GLTextureView.glThreadManager.wait();
            } catch (InterruptedException interruptedException) {
              Thread.currentThread().interrupt();
            }  
        } 
        return;
      } 
    }
  }
  
  private static class GLThreadManager {
    private static String TAG = "GLThreadManager";
    
    private static final int kGLES_20 = 131072;
    
    private static final String kMSM7K_RENDERER_PREFIX = "Q3Dimension MSM7500 ";
    
    private GLTextureView.GLThread eglOwner;
    
    private boolean glesDriverCheckComplete;
    
    private int glesVersion;
    
    private boolean glesVersionCheckComplete;
    
    private boolean limitedGLESContexts;
    
    private boolean multipleGLESContextsAllowed;
    
    private GLThreadManager() {}
    
    private void checkGLESVersion() {
      if (!this.glesVersionCheckComplete)
        this.glesVersionCheckComplete = true; 
    }
    
    public void checkGLDriver(GL10 param1GL10) {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: getfield glesDriverCheckComplete : Z
      //   6: ifne -> 88
      //   9: aload_0
      //   10: invokespecial checkGLESVersion : ()V
      //   13: aload_1
      //   14: sipush #7937
      //   17: invokeinterface glGetString : (I)Ljava/lang/String;
      //   22: astore_1
      //   23: aload_0
      //   24: getfield glesVersion : I
      //   27: istore_2
      //   28: iconst_0
      //   29: istore_3
      //   30: iload_2
      //   31: ldc 131072
      //   33: if_icmpge -> 64
      //   36: aload_1
      //   37: ldc 'Q3Dimension MSM7500 '
      //   39: invokevirtual startsWith : (Ljava/lang/String;)Z
      //   42: ifne -> 51
      //   45: iconst_1
      //   46: istore #4
      //   48: goto -> 54
      //   51: iconst_0
      //   52: istore #4
      //   54: aload_0
      //   55: iload #4
      //   57: putfield multipleGLESContextsAllowed : Z
      //   60: aload_0
      //   61: invokevirtual notifyAll : ()V
      //   64: iload_3
      //   65: istore #4
      //   67: aload_0
      //   68: getfield multipleGLESContextsAllowed : Z
      //   71: ifne -> 77
      //   74: iconst_1
      //   75: istore #4
      //   77: aload_0
      //   78: iload #4
      //   80: putfield limitedGLESContexts : Z
      //   83: aload_0
      //   84: iconst_1
      //   85: putfield glesDriverCheckComplete : Z
      //   88: aload_0
      //   89: monitorexit
      //   90: return
      //   91: astore_1
      //   92: aload_0
      //   93: monitorexit
      //   94: aload_1
      //   95: athrow
      // Exception table:
      //   from	to	target	type
      //   2	28	91	finally
      //   36	45	91	finally
      //   54	64	91	finally
      //   67	74	91	finally
      //   77	88	91	finally
    }
    
    public void releaseEglContextLocked(GLTextureView.GLThread param1GLThread) {
      if (this.eglOwner == param1GLThread)
        this.eglOwner = null; 
      notifyAll();
    }
    
    public boolean shouldReleaseEGLContextWhenPausing() {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: getfield limitedGLESContexts : Z
      //   6: istore_1
      //   7: aload_0
      //   8: monitorexit
      //   9: iload_1
      //   10: ireturn
      //   11: astore_2
      //   12: aload_0
      //   13: monitorexit
      //   14: aload_2
      //   15: athrow
      // Exception table:
      //   from	to	target	type
      //   2	7	11	finally
    }
    
    public boolean shouldTerminateEGLWhenPausing() {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: invokespecial checkGLESVersion : ()V
      //   6: aload_0
      //   7: getfield multipleGLESContextsAllowed : Z
      //   10: istore_1
      //   11: aload_0
      //   12: monitorexit
      //   13: iload_1
      //   14: iconst_1
      //   15: ixor
      //   16: ireturn
      //   17: astore_2
      //   18: aload_0
      //   19: monitorexit
      //   20: aload_2
      //   21: athrow
      // Exception table:
      //   from	to	target	type
      //   2	11	17	finally
    }
    
    public void threadExiting(GLTextureView.GLThread param1GLThread) {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_1
      //   3: iconst_1
      //   4: invokestatic access$1202 : (Ljp/co/cyberagent/android/gpuimage/GLTextureView$GLThread;Z)Z
      //   7: pop
      //   8: aload_0
      //   9: getfield eglOwner : Ljp/co/cyberagent/android/gpuimage/GLTextureView$GLThread;
      //   12: aload_1
      //   13: if_acmpne -> 21
      //   16: aload_0
      //   17: aconst_null
      //   18: putfield eglOwner : Ljp/co/cyberagent/android/gpuimage/GLTextureView$GLThread;
      //   21: aload_0
      //   22: invokevirtual notifyAll : ()V
      //   25: aload_0
      //   26: monitorexit
      //   27: return
      //   28: astore_1
      //   29: aload_0
      //   30: monitorexit
      //   31: aload_1
      //   32: athrow
      // Exception table:
      //   from	to	target	type
      //   2	21	28	finally
      //   21	25	28	finally
    }
    
    public boolean tryAcquireEglContextLocked(GLTextureView.GLThread param1GLThread) {
      GLTextureView.GLThread gLThread = this.eglOwner;
      if (gLThread == param1GLThread || gLThread == null) {
        this.eglOwner = param1GLThread;
        notifyAll();
        return true;
      } 
      checkGLESVersion();
      if (this.multipleGLESContextsAllowed)
        return true; 
      param1GLThread = this.eglOwner;
      if (param1GLThread != null)
        param1GLThread.requestReleaseEglContextLocked(); 
      return false;
    }
  }
  
  public static interface GLWrapper {
    GL wrap(GL param1GL);
  }
  
  static class LogWriter extends Writer {
    private StringBuilder builder = new StringBuilder();
    
    private void flushBuilder() {
      if (this.builder.length() > 0) {
        Log.v("GLTextureView", this.builder.toString());
        StringBuilder stringBuilder = this.builder;
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
          this.builder.append(c);
        } 
      } 
    }
  }
  
  public static interface Renderer {
    void onDrawFrame(GL10 param1GL10);
    
    void onSurfaceChanged(GL10 param1GL10, int param1Int1, int param1Int2);
    
    void onSurfaceCreated(GL10 param1GL10, EGLConfig param1EGLConfig);
  }
  
  private class SimpleEGLConfigChooser extends ComponentSizeChooser {
    public SimpleEGLConfigChooser(boolean param1Boolean) {
      super(8, 8, 8, 0, bool, 0);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/jp/co/cyberagent/android/gpuimage/GLTextureView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */