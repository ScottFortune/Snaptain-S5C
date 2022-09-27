package jp.co.cyberagent.android.gpuimage;

import android.graphics.Bitmap;
import android.opengl.GLSurfaceView;
import android.util.Log;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL10;

public class PixelBuffer {
  private static final boolean LIST_CONFIGS = false;
  
  private static final String TAG = "PixelBuffer";
  
  private Bitmap bitmap;
  
  private EGL10 egl10;
  
  private EGLConfig eglConfig;
  
  private EGLConfig[] eglConfigs;
  
  private EGLContext eglContext;
  
  private EGLDisplay eglDisplay;
  
  private EGLSurface eglSurface;
  
  private GL10 gl10;
  
  private int height;
  
  private String mThreadOwner;
  
  private GLSurfaceView.Renderer renderer;
  
  private int width;
  
  public PixelBuffer(int paramInt1, int paramInt2) {
    this.width = paramInt1;
    this.height = paramInt2;
    int[] arrayOfInt = new int[2];
    paramInt2 = this.width;
    paramInt1 = this.height;
    this.egl10 = (EGL10)EGLContext.getEGL();
    this.eglDisplay = this.egl10.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
    this.egl10.eglInitialize(this.eglDisplay, arrayOfInt);
    this.eglConfig = chooseConfig();
    this.eglContext = this.egl10.eglCreateContext(this.eglDisplay, this.eglConfig, EGL10.EGL_NO_CONTEXT, new int[] { 12440, 2, 12344 });
    this.eglSurface = this.egl10.eglCreatePbufferSurface(this.eglDisplay, this.eglConfig, new int[] { 12375, paramInt2, 12374, paramInt1, 12344 });
    EGL10 eGL10 = this.egl10;
    EGLDisplay eGLDisplay = this.eglDisplay;
    EGLSurface eGLSurface = this.eglSurface;
    eGL10.eglMakeCurrent(eGLDisplay, eGLSurface, eGLSurface, this.eglContext);
    this.gl10 = (GL10)this.eglContext.getGL();
    this.mThreadOwner = Thread.currentThread().getName();
  }
  
  private EGLConfig chooseConfig() {
    int[] arrayOfInt1 = new int[15];
    arrayOfInt1[0] = 12325;
    arrayOfInt1[1] = 0;
    arrayOfInt1[2] = 12326;
    arrayOfInt1[3] = 0;
    arrayOfInt1[4] = 12324;
    arrayOfInt1[5] = 8;
    arrayOfInt1[6] = 12323;
    arrayOfInt1[7] = 8;
    arrayOfInt1[8] = 12322;
    arrayOfInt1[9] = 8;
    arrayOfInt1[10] = 12321;
    arrayOfInt1[11] = 8;
    arrayOfInt1[12] = 12352;
    arrayOfInt1[13] = 4;
    arrayOfInt1[14] = 12344;
    int[] arrayOfInt2 = new int[1];
    this.egl10.eglChooseConfig(this.eglDisplay, arrayOfInt1, null, 0, arrayOfInt2);
    int i = arrayOfInt2[0];
    this.eglConfigs = new EGLConfig[i];
    this.egl10.eglChooseConfig(this.eglDisplay, arrayOfInt1, this.eglConfigs, i, arrayOfInt2);
    return this.eglConfigs[0];
  }
  
  private void convertToBitmap() {
    this.bitmap = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.ARGB_8888);
    GPUImageNativeLibrary.adjustBitmap(this.bitmap);
  }
  
  private int getConfigAttrib(EGLConfig paramEGLConfig, int paramInt) {
    int[] arrayOfInt = new int[1];
    boolean bool = this.egl10.eglGetConfigAttrib(this.eglDisplay, paramEGLConfig, paramInt, arrayOfInt);
    paramInt = 0;
    if (bool)
      paramInt = arrayOfInt[0]; 
    return paramInt;
  }
  
  private void listConfig() {
    Log.i("PixelBuffer", "Config List {");
    for (EGLConfig eGLConfig : this.eglConfigs) {
      int i = getConfigAttrib(eGLConfig, 12325);
      int j = getConfigAttrib(eGLConfig, 12326);
      int k = getConfigAttrib(eGLConfig, 12324);
      int m = getConfigAttrib(eGLConfig, 12323);
      int n = getConfigAttrib(eGLConfig, 12322);
      int i1 = getConfigAttrib(eGLConfig, 12321);
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("    <d,s,r,g,b,a> = <");
      stringBuilder.append(i);
      stringBuilder.append(",");
      stringBuilder.append(j);
      stringBuilder.append(",");
      stringBuilder.append(k);
      stringBuilder.append(",");
      stringBuilder.append(m);
      stringBuilder.append(",");
      stringBuilder.append(n);
      stringBuilder.append(",");
      stringBuilder.append(i1);
      stringBuilder.append(">");
      Log.i("PixelBuffer", stringBuilder.toString());
    } 
    Log.i("PixelBuffer", "}");
  }
  
  public void destroy() {
    this.renderer.onDrawFrame(this.gl10);
    this.renderer.onDrawFrame(this.gl10);
    this.egl10.eglMakeCurrent(this.eglDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
    this.egl10.eglDestroySurface(this.eglDisplay, this.eglSurface);
    this.egl10.eglDestroyContext(this.eglDisplay, this.eglContext);
    this.egl10.eglTerminate(this.eglDisplay);
  }
  
  public Bitmap getBitmap() {
    if (this.renderer == null) {
      Log.e("PixelBuffer", "getBitmap: Renderer was not set.");
      return null;
    } 
    if (!Thread.currentThread().getName().equals(this.mThreadOwner)) {
      Log.e("PixelBuffer", "getBitmap: This thread does not own the OpenGL context.");
      return null;
    } 
    this.renderer.onDrawFrame(this.gl10);
    this.renderer.onDrawFrame(this.gl10);
    convertToBitmap();
    return this.bitmap;
  }
  
  public void setRenderer(GLSurfaceView.Renderer paramRenderer) {
    this.renderer = paramRenderer;
    if (!Thread.currentThread().getName().equals(this.mThreadOwner)) {
      Log.e("PixelBuffer", "setRenderer: This thread does not own the OpenGL context.");
      return;
    } 
    this.renderer.onSurfaceCreated(this.gl10, this.eglConfig);
    this.renderer.onSurfaceChanged(this.gl10, this.width, this.height);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/jp/co/cyberagent/android/gpuimage/PixelBuffer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */