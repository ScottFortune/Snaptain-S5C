package jp.co.cyberagent.android.gpuimage;

import android.app.ActivityManager;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.view.WindowManager;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.util.Rotation;

public class GPUImage {
  static final int SURFACE_TYPE_SURFACE_VIEW = 0;
  
  static final int SURFACE_TYPE_TEXTURE_VIEW = 1;
  
  private final Context context;
  
  private Bitmap currentBitmap;
  
  private GPUImageFilter filter;
  
  private GLSurfaceView glSurfaceView;
  
  private GLTextureView glTextureView;
  
  private final GPUImageRenderer renderer;
  
  private ScaleType scaleType = ScaleType.CENTER_CROP;
  
  private int surfaceType = 0;
  
  public GPUImage(Context paramContext) {
    if (supportsOpenGLES2(paramContext)) {
      this.context = paramContext;
      this.filter = new GPUImageFilter();
      this.renderer = new GPUImageRenderer(this.filter);
      return;
    } 
    throw new IllegalStateException("OpenGL ES 2.0 is not supported on this phone.");
  }
  
  public static void getBitmapForMultipleFilters(Bitmap paramBitmap, List<GPUImageFilter> paramList, ResponseListener<Bitmap> paramResponseListener) {
    if (paramList.isEmpty())
      return; 
    GPUImageRenderer gPUImageRenderer = new GPUImageRenderer(paramList.get(0));
    gPUImageRenderer.setImageBitmap(paramBitmap, false);
    PixelBuffer pixelBuffer = new PixelBuffer(paramBitmap.getWidth(), paramBitmap.getHeight());
    pixelBuffer.setRenderer(gPUImageRenderer);
    for (GPUImageFilter gPUImageFilter : paramList) {
      gPUImageRenderer.setFilter(gPUImageFilter);
      paramResponseListener.response(pixelBuffer.getBitmap());
      gPUImageFilter.destroy();
    } 
    gPUImageRenderer.deleteImage();
    pixelBuffer.destroy();
  }
  
  private int getOutputHeight() {
    GPUImageRenderer gPUImageRenderer = this.renderer;
    if (gPUImageRenderer != null && gPUImageRenderer.getFrameHeight() != 0)
      return this.renderer.getFrameHeight(); 
    Bitmap bitmap = this.currentBitmap;
    return (bitmap != null) ? bitmap.getHeight() : ((WindowManager)this.context.getSystemService("window")).getDefaultDisplay().getHeight();
  }
  
  private int getOutputWidth() {
    GPUImageRenderer gPUImageRenderer = this.renderer;
    if (gPUImageRenderer != null && gPUImageRenderer.getFrameWidth() != 0)
      return this.renderer.getFrameWidth(); 
    Bitmap bitmap = this.currentBitmap;
    return (bitmap != null) ? bitmap.getWidth() : ((WindowManager)this.context.getSystemService("window")).getDefaultDisplay().getWidth();
  }
  
  private String getPath(Uri paramUri) {
    String str;
    Cursor cursor = this.context.getContentResolver().query(paramUri, new String[] { "_data" }, null, null, null);
    paramUri = null;
    if (cursor == null)
      return null; 
    if (cursor.moveToFirst())
      str = cursor.getString(cursor.getColumnIndexOrThrow("_data")); 
    cursor.close();
    return str;
  }
  
  private boolean supportsOpenGLES2(Context paramContext) {
    boolean bool;
    if ((((ActivityManager)paramContext.getSystemService("activity")).getDeviceConfigurationInfo()).reqGlEsVersion >= 131072) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void deleteImage() {
    this.renderer.deleteImage();
    this.currentBitmap = null;
    requestRender();
  }
  
  public Bitmap getBitmapWithFilterApplied() {
    return getBitmapWithFilterApplied(this.currentBitmap);
  }
  
  public Bitmap getBitmapWithFilterApplied(Bitmap paramBitmap) {
    return getBitmapWithFilterApplied(paramBitmap, false);
  }
  
  public Bitmap getBitmapWithFilterApplied(Bitmap paramBitmap, boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: getfield glSurfaceView : Landroid/opengl/GLSurfaceView;
    //   4: ifnonnull -> 14
    //   7: aload_0
    //   8: getfield glTextureView : Ljp/co/cyberagent/android/gpuimage/GLTextureView;
    //   11: ifnull -> 66
    //   14: aload_0
    //   15: getfield renderer : Ljp/co/cyberagent/android/gpuimage/GPUImageRenderer;
    //   18: invokevirtual deleteImage : ()V
    //   21: aload_0
    //   22: getfield renderer : Ljp/co/cyberagent/android/gpuimage/GPUImageRenderer;
    //   25: new jp/co/cyberagent/android/gpuimage/GPUImage$1
    //   28: dup
    //   29: aload_0
    //   30: invokespecial <init> : (Ljp/co/cyberagent/android/gpuimage/GPUImage;)V
    //   33: invokevirtual runOnDraw : (Ljava/lang/Runnable;)V
    //   36: aload_0
    //   37: getfield filter : Ljp/co/cyberagent/android/gpuimage/filter/GPUImageFilter;
    //   40: astore_3
    //   41: aload_3
    //   42: monitorenter
    //   43: aload_0
    //   44: invokevirtual requestRender : ()V
    //   47: aload_0
    //   48: getfield filter : Ljp/co/cyberagent/android/gpuimage/filter/GPUImageFilter;
    //   51: invokevirtual wait : ()V
    //   54: goto -> 64
    //   57: astore #4
    //   59: aload #4
    //   61: invokevirtual printStackTrace : ()V
    //   64: aload_3
    //   65: monitorexit
    //   66: new jp/co/cyberagent/android/gpuimage/GPUImageRenderer
    //   69: dup
    //   70: aload_0
    //   71: getfield filter : Ljp/co/cyberagent/android/gpuimage/filter/GPUImageFilter;
    //   74: invokespecial <init> : (Ljp/co/cyberagent/android/gpuimage/filter/GPUImageFilter;)V
    //   77: astore_3
    //   78: aload_3
    //   79: getstatic jp/co/cyberagent/android/gpuimage/util/Rotation.NORMAL : Ljp/co/cyberagent/android/gpuimage/util/Rotation;
    //   82: aload_0
    //   83: getfield renderer : Ljp/co/cyberagent/android/gpuimage/GPUImageRenderer;
    //   86: invokevirtual isFlippedHorizontally : ()Z
    //   89: aload_0
    //   90: getfield renderer : Ljp/co/cyberagent/android/gpuimage/GPUImageRenderer;
    //   93: invokevirtual isFlippedVertically : ()Z
    //   96: invokevirtual setRotation : (Ljp/co/cyberagent/android/gpuimage/util/Rotation;ZZ)V
    //   99: aload_3
    //   100: aload_0
    //   101: getfield scaleType : Ljp/co/cyberagent/android/gpuimage/GPUImage$ScaleType;
    //   104: invokevirtual setScaleType : (Ljp/co/cyberagent/android/gpuimage/GPUImage$ScaleType;)V
    //   107: new jp/co/cyberagent/android/gpuimage/PixelBuffer
    //   110: dup
    //   111: aload_1
    //   112: invokevirtual getWidth : ()I
    //   115: aload_1
    //   116: invokevirtual getHeight : ()I
    //   119: invokespecial <init> : (II)V
    //   122: astore #4
    //   124: aload #4
    //   126: aload_3
    //   127: invokevirtual setRenderer : (Landroid/opengl/GLSurfaceView$Renderer;)V
    //   130: aload_3
    //   131: aload_1
    //   132: iload_2
    //   133: invokevirtual setImageBitmap : (Landroid/graphics/Bitmap;Z)V
    //   136: aload #4
    //   138: invokevirtual getBitmap : ()Landroid/graphics/Bitmap;
    //   141: astore_1
    //   142: aload_0
    //   143: getfield filter : Ljp/co/cyberagent/android/gpuimage/filter/GPUImageFilter;
    //   146: invokevirtual destroy : ()V
    //   149: aload_3
    //   150: invokevirtual deleteImage : ()V
    //   153: aload #4
    //   155: invokevirtual destroy : ()V
    //   158: aload_0
    //   159: getfield renderer : Ljp/co/cyberagent/android/gpuimage/GPUImageRenderer;
    //   162: aload_0
    //   163: getfield filter : Ljp/co/cyberagent/android/gpuimage/filter/GPUImageFilter;
    //   166: invokevirtual setFilter : (Ljp/co/cyberagent/android/gpuimage/filter/GPUImageFilter;)V
    //   169: aload_0
    //   170: getfield currentBitmap : Landroid/graphics/Bitmap;
    //   173: astore_3
    //   174: aload_3
    //   175: ifnull -> 187
    //   178: aload_0
    //   179: getfield renderer : Ljp/co/cyberagent/android/gpuimage/GPUImageRenderer;
    //   182: aload_3
    //   183: iconst_0
    //   184: invokevirtual setImageBitmap : (Landroid/graphics/Bitmap;Z)V
    //   187: aload_0
    //   188: invokevirtual requestRender : ()V
    //   191: aload_1
    //   192: areturn
    //   193: astore_1
    //   194: aload_3
    //   195: monitorexit
    //   196: aload_1
    //   197: athrow
    // Exception table:
    //   from	to	target	type
    //   43	47	193	finally
    //   47	54	57	java/lang/InterruptedException
    //   47	54	193	finally
    //   59	64	193	finally
    //   64	66	193	finally
    //   194	196	193	finally
  }
  
  public void requestRender() {
    int i = this.surfaceType;
    if (i == 0) {
      GLSurfaceView gLSurfaceView = this.glSurfaceView;
      if (gLSurfaceView != null)
        gLSurfaceView.requestRender(); 
    } else if (i == 1) {
      GLTextureView gLTextureView = this.glTextureView;
      if (gLTextureView != null)
        gLTextureView.requestRender(); 
    } 
  }
  
  void runOnGLThread(Runnable paramRunnable) {
    this.renderer.runOnDrawEnd(paramRunnable);
  }
  
  public void saveToPictures(Bitmap paramBitmap, String paramString1, String paramString2, OnPictureSavedListener paramOnPictureSavedListener) {
    (new SaveTask(paramBitmap, paramString1, paramString2, paramOnPictureSavedListener)).execute((Object[])new Void[0]);
  }
  
  public void saveToPictures(String paramString1, String paramString2, OnPictureSavedListener paramOnPictureSavedListener) {
    saveToPictures(this.currentBitmap, paramString1, paramString2, paramOnPictureSavedListener);
  }
  
  public void setBackgroundColor(float paramFloat1, float paramFloat2, float paramFloat3) {
    this.renderer.setBackgroundColor(paramFloat1, paramFloat2, paramFloat3);
  }
  
  public void setFilter(GPUImageFilter paramGPUImageFilter) {
    this.filter = paramGPUImageFilter;
    this.renderer.setFilter(this.filter);
    requestRender();
  }
  
  public void setGLSurfaceView(GLSurfaceView paramGLSurfaceView) {
    this.surfaceType = 0;
    this.glSurfaceView = paramGLSurfaceView;
    this.glSurfaceView.setEGLContextClientVersion(2);
    this.glSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
    this.glSurfaceView.getHolder().setFormat(1);
    this.glSurfaceView.setRenderer(this.renderer);
    this.glSurfaceView.setRenderMode(0);
    this.glSurfaceView.requestRender();
  }
  
  public void setGLTextureView(GLTextureView paramGLTextureView) {
    this.surfaceType = 1;
    this.glTextureView = paramGLTextureView;
    this.glTextureView.setEGLContextClientVersion(2);
    this.glTextureView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
    this.glTextureView.setOpaque(false);
    this.glTextureView.setRenderer(this.renderer);
    this.glTextureView.setRenderMode(0);
    this.glTextureView.requestRender();
  }
  
  public void setImage(Bitmap paramBitmap) {
    this.currentBitmap = paramBitmap;
    this.renderer.setImageBitmap(paramBitmap, false);
    requestRender();
  }
  
  public void setImage(Uri paramUri) {
    (new LoadImageUriTask(this, paramUri)).execute((Object[])new Void[0]);
  }
  
  public void setImage(File paramFile) {
    (new LoadImageFileTask(this, paramFile)).execute((Object[])new Void[0]);
  }
  
  public void setRotation(Rotation paramRotation) {
    this.renderer.setRotation(paramRotation);
  }
  
  public void setRotation(Rotation paramRotation, boolean paramBoolean1, boolean paramBoolean2) {
    this.renderer.setRotation(paramRotation, paramBoolean1, paramBoolean2);
  }
  
  public void setScaleType(ScaleType paramScaleType) {
    this.scaleType = paramScaleType;
    this.renderer.setScaleType(paramScaleType);
    this.renderer.deleteImage();
    this.currentBitmap = null;
    requestRender();
  }
  
  @Deprecated
  public void setUpCamera(Camera paramCamera) {
    setUpCamera(paramCamera, 0, false, false);
  }
  
  @Deprecated
  public void setUpCamera(Camera paramCamera, int paramInt, boolean paramBoolean1, boolean paramBoolean2) {
    int i = this.surfaceType;
    if (i == 0) {
      this.glSurfaceView.setRenderMode(1);
    } else if (i == 1) {
      this.glTextureView.setRenderMode(1);
    } 
    this.renderer.setUpSurfaceTexture(paramCamera);
    Rotation rotation = Rotation.NORMAL;
    if (paramInt != 90) {
      if (paramInt != 180) {
        if (paramInt == 270)
          rotation = Rotation.ROTATION_270; 
      } else {
        rotation = Rotation.ROTATION_180;
      } 
    } else {
      rotation = Rotation.ROTATION_90;
    } 
    this.renderer.setRotationCamera(rotation, paramBoolean1, paramBoolean2);
  }
  
  public void updatePreviewFrame(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    this.renderer.onPreviewFrame(paramArrayOfbyte, paramInt1, paramInt2);
  }
  
  private class LoadImageFileTask extends LoadImageTask {
    private final File imageFile;
    
    public LoadImageFileTask(GPUImage param1GPUImage1, File param1File) {
      super(param1GPUImage1);
      this.imageFile = param1File;
    }
    
    protected Bitmap decode(BitmapFactory.Options param1Options) {
      return BitmapFactory.decodeFile(this.imageFile.getAbsolutePath(), param1Options);
    }
    
    protected int getImageOrientation() throws IOException {
      int i = (new ExifInterface(this.imageFile.getAbsolutePath())).getAttributeInt("Orientation", 1);
      return (i != 1) ? ((i != 3) ? ((i != 6) ? ((i != 8) ? 0 : 270) : 90) : 180) : 0;
    }
  }
  
  private abstract class LoadImageTask extends AsyncTask<Void, Void, Bitmap> {
    private final GPUImage gpuImage;
    
    private int outputHeight;
    
    private int outputWidth;
    
    public LoadImageTask(GPUImage param1GPUImage1) {
      this.gpuImage = param1GPUImage1;
    }
    
    private boolean checkSize(boolean param1Boolean1, boolean param1Boolean2) {
      GPUImage.ScaleType scaleType1 = GPUImage.this.scaleType;
      GPUImage.ScaleType scaleType2 = GPUImage.ScaleType.CENTER_CROP;
      boolean bool1 = false;
      boolean bool2 = false;
      if (scaleType1 == scaleType2) {
        bool1 = bool2;
        if (param1Boolean1) {
          bool1 = bool2;
          if (param1Boolean2)
            bool1 = true; 
        } 
        return bool1;
      } 
      if (!param1Boolean1) {
        param1Boolean1 = bool1;
        return param1Boolean2 ? true : param1Boolean1;
      } 
      return true;
    }
    
    private int[] getScaleSize(int param1Int1, int param1Int2) {
      float f1 = param1Int1;
      float f2 = f1 / this.outputWidth;
      float f3 = param1Int2;
      float f4 = f3 / this.outputHeight;
      if ((GPUImage.this.scaleType == GPUImage.ScaleType.CENTER_CROP) ? (f2 > f4) : (f2 < f4)) {
        param1Int1 = 1;
      } else {
        param1Int1 = 0;
      } 
      if (param1Int1 != 0) {
        f2 = this.outputHeight;
        f4 = f2 / f3 * f1;
      } else {
        f4 = this.outputWidth;
        f2 = f4 / f1 * f3;
      } 
      return new int[] { Math.round(f4), Math.round(f2) };
    }
    
    private Bitmap loadResizedImage() {
      BitmapFactory.Options options = new BitmapFactory.Options();
      options.inJustDecodeBounds = true;
      decode(options);
      int i = 1;
      while (true) {
        boolean bool2;
        int j = options.outWidth / i;
        int k = this.outputWidth;
        boolean bool1 = false;
        if (j > k) {
          bool2 = true;
        } else {
          bool2 = false;
        } 
        if (options.outHeight / i > this.outputHeight)
          bool1 = true; 
        if (checkSize(bool2, bool1)) {
          i++;
          continue;
        } 
        k = i - 1;
        i = k;
        if (k < 1)
          i = 1; 
        options = new BitmapFactory.Options();
        options.inSampleSize = i;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inPurgeable = true;
        options.inTempStorage = new byte[32768];
        Bitmap bitmap = decode(options);
        return (bitmap == null) ? null : scaleBitmap(rotateImage(bitmap));
      } 
    }
    
    private Bitmap rotateImage(Bitmap param1Bitmap) {
      Bitmap bitmap;
      if (param1Bitmap == null)
        return null; 
      try {
        int i = getImageOrientation();
        bitmap = param1Bitmap;
        if (i != 0) {
          Matrix matrix = new Matrix();
          this();
          matrix.postRotate(i);
          Bitmap bitmap1 = Bitmap.createBitmap(param1Bitmap, 0, 0, param1Bitmap.getWidth(), param1Bitmap.getHeight(), matrix, true);
          try {
            param1Bitmap.recycle();
          } catch (IOException iOException2) {
            param1Bitmap = bitmap1;
            IOException iOException1 = iOException2;
            iOException1.printStackTrace();
            Bitmap bitmap2 = param1Bitmap;
          } 
        } 
      } catch (IOException iOException) {
        iOException.printStackTrace();
        bitmap = param1Bitmap;
      } 
      return bitmap;
    }
    
    private Bitmap scaleBitmap(Bitmap param1Bitmap) {
      int[] arrayOfInt = getScaleSize(param1Bitmap.getWidth(), param1Bitmap.getHeight());
      Bitmap bitmap1 = Bitmap.createScaledBitmap(param1Bitmap, arrayOfInt[0], arrayOfInt[1], true);
      Bitmap bitmap2 = param1Bitmap;
      if (bitmap1 != param1Bitmap) {
        param1Bitmap.recycle();
        System.gc();
        bitmap2 = bitmap1;
      } 
      param1Bitmap = bitmap2;
      if (GPUImage.this.scaleType == GPUImage.ScaleType.CENTER_CROP) {
        int i = arrayOfInt[0] - this.outputWidth;
        int j = arrayOfInt[1] - this.outputHeight;
        bitmap1 = Bitmap.createBitmap(bitmap2, i / 2, j / 2, arrayOfInt[0] - i, arrayOfInt[1] - j);
        param1Bitmap = bitmap2;
        if (bitmap1 != bitmap2) {
          bitmap2.recycle();
          param1Bitmap = bitmap1;
        } 
      } 
      return param1Bitmap;
    }
    
    protected abstract Bitmap decode(BitmapFactory.Options param1Options);
    
    protected Bitmap doInBackground(Void... param1VarArgs) {
      if (GPUImage.this.renderer != null && GPUImage.this.renderer.getFrameWidth() == 0)
        try {
          synchronized (GPUImage.this.renderer.surfaceChangedWaiter) {
            GPUImage.this.renderer.surfaceChangedWaiter.wait(3000L);
          } 
        } catch (InterruptedException interruptedException) {
          interruptedException.printStackTrace();
        }  
      this.outputWidth = GPUImage.this.getOutputWidth();
      this.outputHeight = GPUImage.this.getOutputHeight();
      return loadResizedImage();
    }
    
    protected abstract int getImageOrientation() throws IOException;
    
    protected void onPostExecute(Bitmap param1Bitmap) {
      super.onPostExecute(param1Bitmap);
      this.gpuImage.deleteImage();
      this.gpuImage.setImage(param1Bitmap);
    }
  }
  
  private class LoadImageUriTask extends LoadImageTask {
    private final Uri uri;
    
    public LoadImageUriTask(GPUImage param1GPUImage1, Uri param1Uri) {
      super(param1GPUImage1);
      this.uri = param1Uri;
    }
    
    protected Bitmap decode(BitmapFactory.Options param1Options) {
      try {
        InputStream inputStream;
        if (this.uri.getScheme().startsWith("http") || this.uri.getScheme().startsWith("https")) {
          URL uRL = new URL();
          this(this.uri.toString());
          inputStream = uRL.openStream();
          return BitmapFactory.decodeStream(inputStream, null, param1Options);
        } 
        if (this.uri.getPath().startsWith("/android_asset/")) {
          inputStream = GPUImage.this.context.getAssets().open(this.uri.getPath().substring(15));
        } else {
          inputStream = GPUImage.this.context.getContentResolver().openInputStream(this.uri);
        } 
        return BitmapFactory.decodeStream(inputStream, null, param1Options);
      } catch (Exception exception) {
        exception.printStackTrace();
        return null;
      } 
    }
    
    protected int getImageOrientation() throws IOException {
      Cursor cursor = GPUImage.this.context.getContentResolver().query(this.uri, new String[] { "orientation" }, null, null, null);
      byte b = 0;
      int i = b;
      if (cursor != null)
        if (cursor.getCount() != 1) {
          i = b;
        } else {
          cursor.moveToFirst();
          i = cursor.getInt(0);
          cursor.close();
        }  
      return i;
    }
  }
  
  public static interface OnPictureSavedListener {
    void onPictureSaved(Uri param1Uri);
  }
  
  public static interface ResponseListener<T> {
    void response(T param1T);
  }
  
  @Deprecated
  private class SaveTask extends AsyncTask<Void, Void, Void> {
    private final Bitmap bitmap;
    
    private final String fileName;
    
    private final String folderName;
    
    private final Handler handler;
    
    private final GPUImage.OnPictureSavedListener listener;
    
    public SaveTask(Bitmap param1Bitmap, String param1String1, String param1String2, GPUImage.OnPictureSavedListener param1OnPictureSavedListener) {
      this.bitmap = param1Bitmap;
      this.folderName = param1String1;
      this.fileName = param1String2;
      this.listener = param1OnPictureSavedListener;
      this.handler = new Handler();
    }
    
    private void saveImage(String param1String1, String param1String2, Bitmap param1Bitmap) {
      File file2 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(param1String1);
      stringBuilder.append("/");
      stringBuilder.append(param1String2);
      File file1 = new File(file2, stringBuilder.toString());
      try {
        file1.getParentFile().mkdirs();
        Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.JPEG;
        FileOutputStream fileOutputStream = new FileOutputStream();
        this(file1);
        param1Bitmap.compress(compressFormat, 80, fileOutputStream);
        Context context = GPUImage.this.context;
        String str = file1.toString();
        MediaScannerConnection.OnScanCompletedListener onScanCompletedListener = new MediaScannerConnection.OnScanCompletedListener() {
            public void onScanCompleted(String param2String, final Uri uri) {
              if (GPUImage.SaveTask.this.listener != null)
                GPUImage.SaveTask.this.handler.post(new Runnable() {
                      public void run() {
                        GPUImage.SaveTask.this.listener.onPictureSaved(uri);
                      }
                    }); 
            }
          };
        super(this);
        MediaScannerConnection.scanFile(context, new String[] { str }, null, onScanCompletedListener);
      } catch (FileNotFoundException fileNotFoundException) {
        fileNotFoundException.printStackTrace();
      } 
    }
    
    protected Void doInBackground(Void... param1VarArgs) {
      Bitmap bitmap = GPUImage.this.getBitmapWithFilterApplied(this.bitmap);
      saveImage(this.folderName, this.fileName, bitmap);
      return null;
    }
  }
  
  class null implements MediaScannerConnection.OnScanCompletedListener {
    public void onScanCompleted(String param1String, final Uri uri) {
      if (this.this$1.listener != null)
        this.this$1.handler.post(new Runnable() {
              public void run() {
                this.this$2.this$1.listener.onPictureSaved(uri);
              }
            }); 
    }
  }
  
  class null implements Runnable {
    public void run() {
      this.this$2.this$1.listener.onPictureSaved(uri);
    }
  }
  
  public enum ScaleType {
    CENTER_CROP, CENTER_INSIDE;
    
    static {
      $VALUES = new ScaleType[] { CENTER_INSIDE, CENTER_CROP };
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/jp/co/cyberagent/android/gpuimage/GPUImage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */