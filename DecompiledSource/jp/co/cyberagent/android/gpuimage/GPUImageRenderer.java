package jp.co.cyberagent.android.gpuimage;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.LinkedList;
import java.util.Queue;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.util.OpenGlUtils;
import jp.co.cyberagent.android.gpuimage.util.Rotation;
import jp.co.cyberagent.android.gpuimage.util.TextureRotationUtil;

public class GPUImageRenderer implements GLSurfaceView.Renderer, GLTextureView.Renderer, Camera.PreviewCallback {
  public static final float[] CUBE = new float[] { -1.0F, -1.0F, 1.0F, -1.0F, -1.0F, 1.0F, 1.0F, 1.0F };
  
  private static final int NO_IMAGE = -1;
  
  private int addedPadding;
  
  private float backgroundBlue = 0.0F;
  
  private float backgroundGreen = 0.0F;
  
  private float backgroundRed = 0.0F;
  
  private GPUImageFilter filter;
  
  private boolean flipHorizontal;
  
  private boolean flipVertical;
  
  private final FloatBuffer glCubeBuffer;
  
  private IntBuffer glRgbBuffer;
  
  private final FloatBuffer glTextureBuffer;
  
  private int glTextureId = -1;
  
  private int imageHeight;
  
  private int imageWidth;
  
  private int outputHeight;
  
  private int outputWidth;
  
  private Rotation rotation;
  
  private final Queue<Runnable> runOnDraw;
  
  private final Queue<Runnable> runOnDrawEnd;
  
  private GPUImage.ScaleType scaleType = GPUImage.ScaleType.CENTER_CROP;
  
  public final Object surfaceChangedWaiter = new Object();
  
  private SurfaceTexture surfaceTexture = null;
  
  public GPUImageRenderer(GPUImageFilter paramGPUImageFilter) {
    this.filter = paramGPUImageFilter;
    this.runOnDraw = new LinkedList<Runnable>();
    this.runOnDrawEnd = new LinkedList<Runnable>();
    this.glCubeBuffer = ByteBuffer.allocateDirect(CUBE.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
    this.glCubeBuffer.put(CUBE).position(0);
    this.glTextureBuffer = ByteBuffer.allocateDirect(TextureRotationUtil.TEXTURE_NO_ROTATION.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
    setRotation(Rotation.NORMAL, false, false);
  }
  
  private float addDistance(float paramFloat1, float paramFloat2) {
    if (paramFloat1 != 0.0F)
      paramFloat2 = 1.0F - paramFloat2; 
    return paramFloat2;
  }
  
  private void adjustImageScaling() {
    float f1 = this.outputWidth;
    float f2 = this.outputHeight;
    if (this.rotation == Rotation.ROTATION_270 || this.rotation == Rotation.ROTATION_90) {
      f1 = this.outputHeight;
      f2 = this.outputWidth;
    } 
    float f3 = Math.max(f1 / this.imageWidth, f2 / this.imageHeight);
    int i = Math.round(this.imageWidth * f3);
    int j = Math.round(this.imageHeight * f3);
    f3 = i / f1;
    f1 = j / f2;
    float[] arrayOfFloat1 = CUBE;
    float[] arrayOfFloat2 = TextureRotationUtil.getRotation(this.rotation, this.flipHorizontal, this.flipVertical);
    if (this.scaleType == GPUImage.ScaleType.CENTER_CROP) {
      f2 = (1.0F - 1.0F / f3) / 2.0F;
      f1 = (1.0F - 1.0F / f1) / 2.0F;
      float[] arrayOfFloat = new float[8];
      arrayOfFloat[0] = addDistance(arrayOfFloat2[0], f2);
      arrayOfFloat[1] = addDistance(arrayOfFloat2[1], f1);
      arrayOfFloat[2] = addDistance(arrayOfFloat2[2], f2);
      arrayOfFloat[3] = addDistance(arrayOfFloat2[3], f1);
      arrayOfFloat[4] = addDistance(arrayOfFloat2[4], f2);
      arrayOfFloat[5] = addDistance(arrayOfFloat2[5], f1);
      arrayOfFloat[6] = addDistance(arrayOfFloat2[6], f2);
      arrayOfFloat[7] = addDistance(arrayOfFloat2[7], f1);
      arrayOfFloat2 = arrayOfFloat;
    } else {
      arrayOfFloat1 = new float[8];
      float[] arrayOfFloat = CUBE;
      arrayOfFloat1[0] = arrayOfFloat[0] / f1;
      arrayOfFloat1[1] = arrayOfFloat[1] / f3;
      arrayOfFloat1[2] = arrayOfFloat[2] / f1;
      arrayOfFloat1[3] = arrayOfFloat[3] / f3;
      arrayOfFloat1[4] = arrayOfFloat[4] / f1;
      arrayOfFloat1[5] = arrayOfFloat[5] / f3;
      arrayOfFloat1[6] = arrayOfFloat[6] / f1;
      arrayOfFloat1[7] = arrayOfFloat[7] / f3;
    } 
    this.glCubeBuffer.clear();
    this.glCubeBuffer.put(arrayOfFloat1).position(0);
    this.glTextureBuffer.clear();
    this.glTextureBuffer.put(arrayOfFloat2).position(0);
  }
  
  private void runAll(Queue<Runnable> paramQueue) {
    // Byte code:
    //   0: aload_1
    //   1: monitorenter
    //   2: aload_1
    //   3: invokeinterface isEmpty : ()Z
    //   8: ifne -> 28
    //   11: aload_1
    //   12: invokeinterface poll : ()Ljava/lang/Object;
    //   17: checkcast java/lang/Runnable
    //   20: invokeinterface run : ()V
    //   25: goto -> 2
    //   28: aload_1
    //   29: monitorexit
    //   30: return
    //   31: astore_2
    //   32: aload_1
    //   33: monitorexit
    //   34: goto -> 39
    //   37: aload_2
    //   38: athrow
    //   39: goto -> 37
    // Exception table:
    //   from	to	target	type
    //   2	25	31	finally
    //   28	30	31	finally
    //   32	34	31	finally
  }
  
  public void deleteImage() {
    runOnDraw(new Runnable() {
          public void run() {
            GLES20.glDeleteTextures(1, new int[] { GPUImageRenderer.access$100(this.this$0) }, 0);
            GPUImageRenderer.access$102(GPUImageRenderer.this, -1);
          }
        });
  }
  
  protected int getFrameHeight() {
    return this.outputHeight;
  }
  
  protected int getFrameWidth() {
    return this.outputWidth;
  }
  
  public Rotation getRotation() {
    return this.rotation;
  }
  
  public boolean isFlippedHorizontally() {
    return this.flipHorizontal;
  }
  
  public boolean isFlippedVertically() {
    return this.flipVertical;
  }
  
  public void onDrawFrame(GL10 paramGL10) {
    GLES20.glClear(16640);
    runAll(this.runOnDraw);
    this.filter.onDraw(this.glTextureId, this.glCubeBuffer, this.glTextureBuffer);
    runAll(this.runOnDrawEnd);
    SurfaceTexture surfaceTexture = this.surfaceTexture;
    if (surfaceTexture != null)
      surfaceTexture.updateTexImage(); 
  }
  
  public void onPreviewFrame(final byte[] data, final int width, final int height) {
    if (this.glRgbBuffer == null)
      this.glRgbBuffer = IntBuffer.allocate(width * height); 
    if (this.runOnDraw.isEmpty())
      runOnDraw(new Runnable() {
            public void run() {
              GPUImageNativeLibrary.YUVtoRBGA(data, width, height, GPUImageRenderer.this.glRgbBuffer.array());
              GPUImageRenderer gPUImageRenderer = GPUImageRenderer.this;
              GPUImageRenderer.access$102(gPUImageRenderer, OpenGlUtils.loadTexture(gPUImageRenderer.glRgbBuffer, width, height, GPUImageRenderer.this.glTextureId));
              int i = GPUImageRenderer.this.imageWidth;
              int j = width;
              if (i != j) {
                GPUImageRenderer.access$202(GPUImageRenderer.this, j);
                GPUImageRenderer.access$302(GPUImageRenderer.this, height);
                GPUImageRenderer.this.adjustImageScaling();
              } 
            }
          }); 
  }
  
  public void onPreviewFrame(byte[] paramArrayOfbyte, Camera paramCamera) {
    Camera.Size size = paramCamera.getParameters().getPreviewSize();
    onPreviewFrame(paramArrayOfbyte, size.width, size.height);
  }
  
  public void onSurfaceChanged(GL10 paramGL10, int paramInt1, int paramInt2) {
    this.outputWidth = paramInt1;
    this.outputHeight = paramInt2;
    GLES20.glViewport(0, 0, paramInt1, paramInt2);
    GLES20.glUseProgram(this.filter.getProgram());
    this.filter.onOutputSizeChanged(paramInt1, paramInt2);
    adjustImageScaling();
    synchronized (this.surfaceChangedWaiter) {
      this.surfaceChangedWaiter.notifyAll();
      return;
    } 
  }
  
  public void onSurfaceCreated(GL10 paramGL10, EGLConfig paramEGLConfig) {
    GLES20.glClearColor(this.backgroundRed, this.backgroundGreen, this.backgroundBlue, 1.0F);
    GLES20.glDisable(2929);
    this.filter.ifNeedInit();
  }
  
  protected void runOnDraw(Runnable paramRunnable) {
    synchronized (this.runOnDraw) {
      this.runOnDraw.add(paramRunnable);
      return;
    } 
  }
  
  protected void runOnDrawEnd(Runnable paramRunnable) {
    synchronized (this.runOnDrawEnd) {
      this.runOnDrawEnd.add(paramRunnable);
      return;
    } 
  }
  
  public void setBackgroundColor(float paramFloat1, float paramFloat2, float paramFloat3) {
    this.backgroundRed = paramFloat1;
    this.backgroundGreen = paramFloat2;
    this.backgroundBlue = paramFloat3;
  }
  
  public void setFilter(final GPUImageFilter filter) {
    runOnDraw(new Runnable() {
          public void run() {
            GPUImageFilter gPUImageFilter = GPUImageRenderer.this.filter;
            GPUImageRenderer.access$602(GPUImageRenderer.this, filter);
            if (gPUImageFilter != null)
              gPUImageFilter.destroy(); 
            GPUImageRenderer.this.filter.ifNeedInit();
            GLES20.glUseProgram(GPUImageRenderer.this.filter.getProgram());
            GPUImageRenderer.this.filter.onOutputSizeChanged(GPUImageRenderer.this.outputWidth, GPUImageRenderer.this.outputHeight);
          }
        });
  }
  
  public void setImageBitmap(Bitmap paramBitmap) {
    setImageBitmap(paramBitmap, true);
  }
  
  public void setImageBitmap(final Bitmap bitmap, final boolean recycle) {
    if (bitmap == null)
      return; 
    runOnDraw(new Runnable() {
          public void run() {
            Bitmap bitmap1;
            Bitmap bitmap2;
            if (bitmap.getWidth() % 2 == 1) {
              bitmap1 = Bitmap.createBitmap(bitmap.getWidth() + 1, bitmap.getHeight(), Bitmap.Config.ARGB_8888);
              Canvas canvas = new Canvas(bitmap1);
              canvas.drawARGB(0, 0, 0, 0);
              canvas.drawBitmap(bitmap, 0.0F, 0.0F, null);
              GPUImageRenderer.access$902(GPUImageRenderer.this, 1);
            } else {
              GPUImageRenderer.access$902(GPUImageRenderer.this, 0);
              bitmap1 = null;
            } 
            GPUImageRenderer gPUImageRenderer = GPUImageRenderer.this;
            if (bitmap1 != null) {
              bitmap2 = bitmap1;
            } else {
              bitmap2 = bitmap;
            } 
            GPUImageRenderer.access$102(gPUImageRenderer, OpenGlUtils.loadTexture(bitmap2, GPUImageRenderer.this.glTextureId, recycle));
            if (bitmap1 != null)
              bitmap1.recycle(); 
            GPUImageRenderer.access$202(GPUImageRenderer.this, bitmap.getWidth());
            GPUImageRenderer.access$302(GPUImageRenderer.this, bitmap.getHeight());
            GPUImageRenderer.this.adjustImageScaling();
          }
        });
  }
  
  public void setRotation(Rotation paramRotation) {
    this.rotation = paramRotation;
    adjustImageScaling();
  }
  
  public void setRotation(Rotation paramRotation, boolean paramBoolean1, boolean paramBoolean2) {
    this.flipHorizontal = paramBoolean1;
    this.flipVertical = paramBoolean2;
    setRotation(paramRotation);
  }
  
  public void setRotationCamera(Rotation paramRotation, boolean paramBoolean1, boolean paramBoolean2) {
    setRotation(paramRotation, paramBoolean2, paramBoolean1);
  }
  
  public void setScaleType(GPUImage.ScaleType paramScaleType) {
    this.scaleType = paramScaleType;
  }
  
  public void setUpSurfaceTexture(final Camera camera) {
    runOnDraw(new Runnable() {
          public void run() {
            int[] arrayOfInt = new int[1];
            GLES20.glGenTextures(1, arrayOfInt, 0);
            GPUImageRenderer.access$502(GPUImageRenderer.this, new SurfaceTexture(arrayOfInt[0]));
            try {
              camera.setPreviewTexture(GPUImageRenderer.this.surfaceTexture);
              camera.setPreviewCallback(GPUImageRenderer.this);
              camera.startPreview();
            } catch (IOException iOException) {
              iOException.printStackTrace();
            } 
          }
        });
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/jp/co/cyberagent/android/gpuimage/GPUImageRenderer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */