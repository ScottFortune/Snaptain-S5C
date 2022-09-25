package jp.co.cyberagent.android.gpuimage;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.concurrent.Semaphore;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.util.Rotation;

public class GPUImageView extends FrameLayout {
  public static final int RENDERMODE_CONTINUOUSLY = 1;
  
  public static final int RENDERMODE_WHEN_DIRTY = 0;
  
  private GPUImageFilter filter;
  
  public Size forceSize = null;
  
  private GPUImage gpuImage;
  
  private boolean isShowLoading = true;
  
  private float ratio = 0.0F;
  
  private int surfaceType = 0;
  
  private View surfaceView;
  
  public GPUImageView(Context paramContext) {
    super(paramContext);
    init(paramContext, (AttributeSet)null);
  }
  
  public GPUImageView(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    init(paramContext, paramAttributeSet);
  }
  
  private void init(Context paramContext, AttributeSet paramAttributeSet) {
    if (paramAttributeSet != null) {
      TypedArray typedArray = paramContext.getTheme().obtainStyledAttributes(paramAttributeSet, R.styleable.GPUImageView, 0, 0);
      try {
        this.surfaceType = typedArray.getInt(R.styleable.GPUImageView_gpuimage_surface_type, this.surfaceType);
        this.isShowLoading = typedArray.getBoolean(R.styleable.GPUImageView_gpuimage_show_loading, this.isShowLoading);
      } finally {
        typedArray.recycle();
      } 
    } 
    this.gpuImage = new GPUImage(paramContext);
    if (this.surfaceType == 1) {
      this.surfaceView = (View)new GPUImageGLTextureView(paramContext, paramAttributeSet);
      this.gpuImage.setGLTextureView((GLTextureView)this.surfaceView);
    } else {
      this.surfaceView = (View)new GPUImageGLSurfaceView(paramContext, paramAttributeSet);
      this.gpuImage.setGLSurfaceView((GLSurfaceView)this.surfaceView);
    } 
    addView(this.surfaceView);
  }
  
  public Bitmap capture() throws InterruptedException {
    final Semaphore waiter = new Semaphore(0);
    final Bitmap resultBitmap = Bitmap.createBitmap(this.surfaceView.getMeasuredWidth(), this.surfaceView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
    this.gpuImage.runOnGLThread(new Runnable() {
          public void run() {
            GPUImageNativeLibrary.adjustBitmap(resultBitmap);
            waiter.release();
          }
        });
    requestRender();
    semaphore.acquire();
    return bitmap;
  }
  
  public Bitmap capture(int paramInt1, int paramInt2) throws InterruptedException {
    if (Looper.myLooper() != Looper.getMainLooper()) {
      this.forceSize = new Size(paramInt1, paramInt2);
      final Semaphore waiter = new Semaphore(0);
      getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
              if (Build.VERSION.SDK_INT < 16) {
                GPUImageView.this.getViewTreeObserver().removeGlobalOnLayoutListener(this);
              } else {
                GPUImageView.this.getViewTreeObserver().removeOnGlobalLayoutListener(this);
              } 
              waiter.release();
            }
          });
      if (this.isShowLoading)
        post(new Runnable() {
              public void run() {
                GPUImageView gPUImageView = GPUImageView.this;
                gPUImageView.addView((View)new GPUImageView.LoadingView(gPUImageView.getContext()));
                GPUImageView.this.surfaceView.requestLayout();
              }
            }); 
      semaphore.acquire();
      this.gpuImage.runOnGLThread(new Runnable() {
            public void run() {
              waiter.release();
            }
          });
      requestRender();
      semaphore.acquire();
      Bitmap bitmap = capture();
      this.forceSize = null;
      post(new Runnable() {
            public void run() {
              GPUImageView.this.surfaceView.requestLayout();
            }
          });
      requestRender();
      if (this.isShowLoading)
        postDelayed(new Runnable() {
              public void run() {
                GPUImageView.this.removeViewAt(1);
              }
            },  300L); 
      return bitmap;
    } 
    throw new IllegalStateException("Do not call this method from the UI thread!");
  }
  
  public GPUImageFilter getFilter() {
    return this.filter;
  }
  
  public GPUImage getGPUImage() {
    return this.gpuImage;
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    if (this.ratio != 0.0F) {
      paramInt1 = View.MeasureSpec.getSize(paramInt1);
      int i = View.MeasureSpec.getSize(paramInt2);
      float f1 = paramInt1;
      float f2 = this.ratio;
      float f3 = f1 / f2;
      float f4 = i;
      if (f3 < f4) {
        i = Math.round(f1 / f2);
        paramInt2 = paramInt1;
        paramInt1 = i;
      } else {
        paramInt2 = Math.round(f4 * f2);
        paramInt1 = i;
      } 
      super.onMeasure(View.MeasureSpec.makeMeasureSpec(paramInt2, 1073741824), View.MeasureSpec.makeMeasureSpec(paramInt1, 1073741824));
    } else {
      super.onMeasure(paramInt1, paramInt2);
    } 
  }
  
  public void onPause() {
    View view = this.surfaceView;
    if (view instanceof GLSurfaceView) {
      ((GLSurfaceView)view).onPause();
    } else if (view instanceof GLTextureView) {
      ((GLTextureView)view).onPause();
    } 
  }
  
  public void onResume() {
    View view = this.surfaceView;
    if (view instanceof GLSurfaceView) {
      ((GLSurfaceView)view).onResume();
    } else if (view instanceof GLTextureView) {
      ((GLTextureView)view).onResume();
    } 
  }
  
  public void requestRender() {
    View view = this.surfaceView;
    if (view instanceof GLSurfaceView) {
      ((GLSurfaceView)view).requestRender();
    } else if (view instanceof GLTextureView) {
      ((GLTextureView)view).requestRender();
    } 
  }
  
  public void saveToPictures(String paramString1, String paramString2, int paramInt1, int paramInt2, OnPictureSavedListener paramOnPictureSavedListener) {
    (new SaveTask(paramString1, paramString2, paramInt1, paramInt2, paramOnPictureSavedListener)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Object[])new Void[0]);
  }
  
  public void saveToPictures(String paramString1, String paramString2, OnPictureSavedListener paramOnPictureSavedListener) {
    (new SaveTask(paramString1, paramString2, paramOnPictureSavedListener)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Object[])new Void[0]);
  }
  
  public void setBackgroundColor(float paramFloat1, float paramFloat2, float paramFloat3) {
    this.gpuImage.setBackgroundColor(paramFloat1, paramFloat2, paramFloat3);
  }
  
  public void setFilter(GPUImageFilter paramGPUImageFilter) {
    this.filter = paramGPUImageFilter;
    this.gpuImage.setFilter(paramGPUImageFilter);
    requestRender();
  }
  
  public void setImage(Bitmap paramBitmap) {
    this.gpuImage.setImage(paramBitmap);
  }
  
  public void setImage(Uri paramUri) {
    this.gpuImage.setImage(paramUri);
  }
  
  public void setImage(File paramFile) {
    this.gpuImage.setImage(paramFile);
  }
  
  public void setRatio(float paramFloat) {
    this.ratio = paramFloat;
    this.surfaceView.requestLayout();
    this.gpuImage.deleteImage();
  }
  
  public void setRenderMode(int paramInt) {
    View view = this.surfaceView;
    if (view instanceof GLSurfaceView) {
      ((GLSurfaceView)view).setRenderMode(paramInt);
    } else if (view instanceof GLTextureView) {
      ((GLTextureView)view).setRenderMode(paramInt);
    } 
  }
  
  public void setRotation(Rotation paramRotation) {
    this.gpuImage.setRotation(paramRotation);
    requestRender();
  }
  
  public void setScaleType(GPUImage.ScaleType paramScaleType) {
    this.gpuImage.setScaleType(paramScaleType);
  }
  
  @Deprecated
  public void setUpCamera(Camera paramCamera) {
    this.gpuImage.setUpCamera(paramCamera);
  }
  
  @Deprecated
  public void setUpCamera(Camera paramCamera, int paramInt, boolean paramBoolean1, boolean paramBoolean2) {
    this.gpuImage.setUpCamera(paramCamera, paramInt, paramBoolean1, paramBoolean2);
  }
  
  public void updatePreviewFrame(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    this.gpuImage.updatePreviewFrame(paramArrayOfbyte, paramInt1, paramInt2);
  }
  
  private class GPUImageGLSurfaceView extends GLSurfaceView {
    public GPUImageGLSurfaceView(Context param1Context) {
      super(param1Context);
    }
    
    public GPUImageGLSurfaceView(Context param1Context, AttributeSet param1AttributeSet) {
      super(param1Context, param1AttributeSet);
    }
    
    protected void onMeasure(int param1Int1, int param1Int2) {
      if (GPUImageView.this.forceSize != null) {
        super.onMeasure(View.MeasureSpec.makeMeasureSpec(GPUImageView.this.forceSize.width, 1073741824), View.MeasureSpec.makeMeasureSpec(GPUImageView.this.forceSize.height, 1073741824));
      } else {
        super.onMeasure(param1Int1, param1Int2);
      } 
    }
  }
  
  private class GPUImageGLTextureView extends GLTextureView {
    public GPUImageGLTextureView(Context param1Context) {
      super(param1Context);
    }
    
    public GPUImageGLTextureView(Context param1Context, AttributeSet param1AttributeSet) {
      super(param1Context, param1AttributeSet);
    }
    
    protected void onMeasure(int param1Int1, int param1Int2) {
      if (GPUImageView.this.forceSize != null) {
        super.onMeasure(View.MeasureSpec.makeMeasureSpec(GPUImageView.this.forceSize.width, 1073741824), View.MeasureSpec.makeMeasureSpec(GPUImageView.this.forceSize.height, 1073741824));
      } else {
        super.onMeasure(param1Int1, param1Int2);
      } 
    }
  }
  
  private class LoadingView extends FrameLayout {
    public LoadingView(Context param1Context) {
      super(param1Context);
      init();
    }
    
    public LoadingView(Context param1Context, AttributeSet param1AttributeSet) {
      super(param1Context, param1AttributeSet);
      init();
    }
    
    public LoadingView(Context param1Context, AttributeSet param1AttributeSet, int param1Int) {
      super(param1Context, param1AttributeSet, param1Int);
      init();
    }
    
    private void init() {
      ProgressBar progressBar = new ProgressBar(getContext());
      progressBar.setLayoutParams((ViewGroup.LayoutParams)new FrameLayout.LayoutParams(-2, -2, 17));
      addView((View)progressBar);
      setBackgroundColor(-16777216);
    }
  }
  
  public static interface OnPictureSavedListener {
    void onPictureSaved(Uri param1Uri);
  }
  
  private class SaveTask extends AsyncTask<Void, Void, Void> {
    private final String fileName;
    
    private final String folderName;
    
    private final Handler handler;
    
    private final int height;
    
    private final GPUImageView.OnPictureSavedListener listener;
    
    private final int width;
    
    public SaveTask(String param1String1, String param1String2, int param1Int1, int param1Int2, GPUImageView.OnPictureSavedListener param1OnPictureSavedListener) {
      this.folderName = param1String1;
      this.fileName = param1String2;
      this.width = param1Int1;
      this.height = param1Int2;
      this.listener = param1OnPictureSavedListener;
      this.handler = new Handler();
    }
    
    public SaveTask(String param1String1, String param1String2, GPUImageView.OnPictureSavedListener param1OnPictureSavedListener) {
      this(param1String1, param1String2, 0, 0, param1OnPictureSavedListener);
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
        Context context = GPUImageView.this.getContext();
        String str = file1.toString();
        MediaScannerConnection.OnScanCompletedListener onScanCompletedListener = new MediaScannerConnection.OnScanCompletedListener() {
            public void onScanCompleted(String param2String, final Uri uri) {
              if (GPUImageView.SaveTask.this.listener != null)
                GPUImageView.SaveTask.this.handler.post(new Runnable() {
                      public void run() {
                        GPUImageView.SaveTask.this.listener.onPictureSaved(uri);
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
      try {
        Bitmap bitmap;
        if (this.width != 0) {
          bitmap = GPUImageView.this.capture(this.width, this.height);
        } else {
          bitmap = GPUImageView.this.capture();
        } 
        saveImage(this.folderName, this.fileName, bitmap);
      } catch (InterruptedException interruptedException) {
        interruptedException.printStackTrace();
      } 
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
  
  public static class Size {
    int height;
    
    int width;
    
    public Size(int param1Int1, int param1Int2) {
      this.width = param1Int1;
      this.height = param1Int2;
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/jp/co/cyberagent/android/gpuimage/GPUImageView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */