package com.netopsun.ijkvideoview.extra.filter_choose_popupwindows;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import java.security.MessageDigest;
import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;

public class GlideCircleWithBorder extends BitmapTransformation {
  private GPUImageFilter filter;
  
  private final GPUImage gpuImage;
  
  private int mBorderColor;
  
  private Paint mBorderPaint;
  
  private float mBorderWidth;
  
  public GlideCircleWithBorder(Context paramContext, int paramInt1, int paramInt2, GPUImageFilter paramGPUImageFilter) {
    this.mBorderWidth = (Resources.getSystem().getDisplayMetrics()).density * paramInt1;
    this.mBorderColor = paramInt2;
    this.mBorderPaint = new Paint();
    this.mBorderPaint.setDither(true);
    this.mBorderPaint.setAntiAlias(true);
    this.mBorderPaint.setColor(paramInt2);
    this.mBorderPaint.setStyle(Paint.Style.STROKE);
    this.mBorderPaint.setStrokeWidth(this.mBorderWidth);
    this.gpuImage = new GPUImage(paramContext);
    this.gpuImage.setFilter(paramGPUImageFilter);
    this.filter = paramGPUImageFilter;
  }
  
  public GlideCircleWithBorder(Context paramContext, GPUImageFilter paramGPUImageFilter) {
    this.gpuImage = new GPUImage(paramContext);
    this.gpuImage.setFilter(paramGPUImageFilter);
    this.filter = paramGPUImageFilter;
  }
  
  private Bitmap circleCrop(BitmapPool paramBitmapPool, Bitmap paramBitmap) {
    if (paramBitmap == null)
      return null; 
    paramBitmap = this.gpuImage.getBitmapWithFilterApplied(paramBitmap);
    int i = (int)(Math.min(paramBitmap.getWidth(), paramBitmap.getHeight()) - this.mBorderWidth / 2.0F);
    Bitmap bitmap2 = Bitmap.createBitmap(paramBitmap, (paramBitmap.getWidth() - i) / 2, (paramBitmap.getHeight() - i) / 2, i, i);
    paramBitmap = paramBitmapPool.get(i, i, Bitmap.Config.ARGB_8888);
    Bitmap bitmap1 = paramBitmap;
    if (paramBitmap == null)
      bitmap1 = Bitmap.createBitmap(i, i, Bitmap.Config.ARGB_8888); 
    Canvas canvas = new Canvas(bitmap1);
    Paint paint2 = new Paint();
    paint2.setShader((Shader)new BitmapShader(bitmap2, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
    paint2.setAntiAlias(true);
    float f = i / 2.0F;
    canvas.drawCircle(f, f, f, paint2);
    Paint paint1 = this.mBorderPaint;
    if (paint1 != null)
      canvas.drawCircle(f, f, f - this.mBorderWidth / 2.0F, paint1); 
    return bitmap1;
  }
  
  public boolean equals(Object paramObject) {
    boolean bool = paramObject instanceof GlideCircleWithBorder;
    boolean bool1 = false;
    boolean bool2 = bool1;
    if (bool) {
      bool2 = bool1;
      if (hashCode() == paramObject.hashCode())
        bool2 = true; 
    } 
    return bool2;
  }
  
  public int hashCode() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(this.filter.getClass().getName());
    stringBuilder.append(":");
    stringBuilder.append((int)this.mBorderWidth);
    stringBuilder.append(":");
    stringBuilder.append(this.mBorderColor);
    return stringBuilder.toString().hashCode();
  }
  
  protected Bitmap transform(BitmapPool paramBitmapPool, Bitmap paramBitmap, int paramInt1, int paramInt2) {
    return circleCrop(paramBitmapPool, paramBitmap);
  }
  
  public void updateDiskCacheKey(MessageDigest paramMessageDigest) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(this.filter.getClass().getName());
    stringBuilder.append(":");
    stringBuilder.append((int)this.mBorderWidth);
    stringBuilder.append(":");
    stringBuilder.append(this.mBorderColor);
    paramMessageDigest.update(stringBuilder.toString().getBytes());
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/ijkvideoview/extra/filter_choose_popupwindows/GlideCircleWithBorder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */