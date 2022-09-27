package com.bumptech.glide.request;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Option;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy;

public class RequestOptions extends BaseRequestOptions<RequestOptions> {
  private static RequestOptions centerCropOptions;
  
  private static RequestOptions centerInsideOptions;
  
  private static RequestOptions circleCropOptions;
  
  private static RequestOptions fitCenterOptions;
  
  private static RequestOptions noAnimationOptions;
  
  private static RequestOptions noTransformOptions;
  
  private static RequestOptions skipMemoryCacheFalseOptions;
  
  private static RequestOptions skipMemoryCacheTrueOptions;
  
  public static RequestOptions bitmapTransform(Transformation<Bitmap> paramTransformation) {
    return (new RequestOptions()).transform(paramTransformation);
  }
  
  public static RequestOptions centerCropTransform() {
    if (centerCropOptions == null)
      centerCropOptions = (new RequestOptions()).centerCrop().autoClone(); 
    return centerCropOptions;
  }
  
  public static RequestOptions centerInsideTransform() {
    if (centerInsideOptions == null)
      centerInsideOptions = (new RequestOptions()).centerInside().autoClone(); 
    return centerInsideOptions;
  }
  
  public static RequestOptions circleCropTransform() {
    if (circleCropOptions == null)
      circleCropOptions = (new RequestOptions()).circleCrop().autoClone(); 
    return circleCropOptions;
  }
  
  public static RequestOptions decodeTypeOf(Class<?> paramClass) {
    return (new RequestOptions()).decode(paramClass);
  }
  
  public static RequestOptions diskCacheStrategyOf(DiskCacheStrategy paramDiskCacheStrategy) {
    return (new RequestOptions()).diskCacheStrategy(paramDiskCacheStrategy);
  }
  
  public static RequestOptions downsampleOf(DownsampleStrategy paramDownsampleStrategy) {
    return (new RequestOptions()).downsample(paramDownsampleStrategy);
  }
  
  public static RequestOptions encodeFormatOf(Bitmap.CompressFormat paramCompressFormat) {
    return (new RequestOptions()).encodeFormat(paramCompressFormat);
  }
  
  public static RequestOptions encodeQualityOf(int paramInt) {
    return (new RequestOptions()).encodeQuality(paramInt);
  }
  
  public static RequestOptions errorOf(int paramInt) {
    return (new RequestOptions()).error(paramInt);
  }
  
  public static RequestOptions errorOf(Drawable paramDrawable) {
    return (new RequestOptions()).error(paramDrawable);
  }
  
  public static RequestOptions fitCenterTransform() {
    if (fitCenterOptions == null)
      fitCenterOptions = (new RequestOptions()).fitCenter().autoClone(); 
    return fitCenterOptions;
  }
  
  public static RequestOptions formatOf(DecodeFormat paramDecodeFormat) {
    return (new RequestOptions()).format(paramDecodeFormat);
  }
  
  public static RequestOptions frameOf(long paramLong) {
    return (new RequestOptions()).frame(paramLong);
  }
  
  public static RequestOptions noAnimation() {
    if (noAnimationOptions == null)
      noAnimationOptions = (new RequestOptions()).dontAnimate().autoClone(); 
    return noAnimationOptions;
  }
  
  public static RequestOptions noTransformation() {
    if (noTransformOptions == null)
      noTransformOptions = (new RequestOptions()).dontTransform().autoClone(); 
    return noTransformOptions;
  }
  
  public static <T> RequestOptions option(Option<T> paramOption, T paramT) {
    return (new RequestOptions()).set(paramOption, paramT);
  }
  
  public static RequestOptions overrideOf(int paramInt) {
    return overrideOf(paramInt, paramInt);
  }
  
  public static RequestOptions overrideOf(int paramInt1, int paramInt2) {
    return (new RequestOptions()).override(paramInt1, paramInt2);
  }
  
  public static RequestOptions placeholderOf(int paramInt) {
    return (new RequestOptions()).placeholder(paramInt);
  }
  
  public static RequestOptions placeholderOf(Drawable paramDrawable) {
    return (new RequestOptions()).placeholder(paramDrawable);
  }
  
  public static RequestOptions priorityOf(Priority paramPriority) {
    return (new RequestOptions()).priority(paramPriority);
  }
  
  public static RequestOptions signatureOf(Key paramKey) {
    return (new RequestOptions()).signature(paramKey);
  }
  
  public static RequestOptions sizeMultiplierOf(float paramFloat) {
    return (new RequestOptions()).sizeMultiplier(paramFloat);
  }
  
  public static RequestOptions skipMemoryCacheOf(boolean paramBoolean) {
    if (paramBoolean) {
      if (skipMemoryCacheTrueOptions == null)
        skipMemoryCacheTrueOptions = (new RequestOptions()).skipMemoryCache(true).autoClone(); 
      return skipMemoryCacheTrueOptions;
    } 
    if (skipMemoryCacheFalseOptions == null)
      skipMemoryCacheFalseOptions = (new RequestOptions()).skipMemoryCache(false).autoClone(); 
    return skipMemoryCacheFalseOptions;
  }
  
  public static RequestOptions timeoutOf(int paramInt) {
    return (new RequestOptions()).timeout(paramInt);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/request/RequestOptions.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */