package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Xfermode;
import android.os.Build;
import android.util.Log;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.util.Preconditions;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public final class TransformationUtils {
  static {
    CIRCLE_CROP_SHAPE_PAINT = new Paint(7);
    MODELS_REQUIRING_BITMAP_LOCK = new HashSet<String>(Arrays.asList(new String[] { 
            "XT1085", "XT1092", "XT1093", "XT1094", "XT1095", "XT1096", "XT1097", "XT1098", "XT1031", "XT1028", 
            "XT937C", "XT1032", "XT1008", "XT1033", "XT1035", "XT1034", "XT939G", "XT1039", "XT1040", "XT1042", 
            "XT1045", "XT1063", "XT1064", "XT1068", "XT1069", "XT1072", "XT1077", "XT1078", "XT1079" }));
    if (MODELS_REQUIRING_BITMAP_LOCK.contains(Build.MODEL)) {
      ReentrantLock reentrantLock = new ReentrantLock();
    } else {
      noLock = new NoLock();
    } 
    BITMAP_DRAWABLE_LOCK = noLock;
    CIRCLE_CROP_BITMAP_PAINT = new Paint(7);
    CIRCLE_CROP_BITMAP_PAINT.setXfermode((Xfermode)new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
  }
  
  private static void applyMatrix(Bitmap paramBitmap1, Bitmap paramBitmap2, Matrix paramMatrix) {
    BITMAP_DRAWABLE_LOCK.lock();
    try {
      Canvas canvas = new Canvas();
      this(paramBitmap2);
      canvas.drawBitmap(paramBitmap1, paramMatrix, DEFAULT_PAINT);
      clear(canvas);
      return;
    } finally {
      BITMAP_DRAWABLE_LOCK.unlock();
    } 
  }
  
  public static Bitmap centerCrop(BitmapPool paramBitmapPool, Bitmap paramBitmap, int paramInt1, int paramInt2) {
    float f2;
    float f3;
    if (paramBitmap.getWidth() == paramInt1 && paramBitmap.getHeight() == paramInt2)
      return paramBitmap; 
    Matrix matrix = new Matrix();
    int i = paramBitmap.getWidth();
    int j = paramBitmap.getHeight();
    float f1 = 0.0F;
    if (i * paramInt2 > j * paramInt1) {
      f2 = paramInt2 / paramBitmap.getHeight();
      f3 = (paramInt1 - paramBitmap.getWidth() * f2) * 0.5F;
    } else {
      f2 = paramInt1 / paramBitmap.getWidth();
      f1 = (paramInt2 - paramBitmap.getHeight() * f2) * 0.5F;
      f3 = 0.0F;
    } 
    matrix.setScale(f2, f2);
    matrix.postTranslate((int)(f3 + 0.5F), (int)(f1 + 0.5F));
    Bitmap bitmap = paramBitmapPool.get(paramInt1, paramInt2, getNonNullConfig(paramBitmap));
    setAlpha(paramBitmap, bitmap);
    applyMatrix(paramBitmap, bitmap, matrix);
    return bitmap;
  }
  
  public static Bitmap centerInside(BitmapPool paramBitmapPool, Bitmap paramBitmap, int paramInt1, int paramInt2) {
    if (paramBitmap.getWidth() <= paramInt1 && paramBitmap.getHeight() <= paramInt2) {
      if (Log.isLoggable("TransformationUtils", 2))
        Log.v("TransformationUtils", "requested target size larger or equal to input, returning input"); 
      return paramBitmap;
    } 
    if (Log.isLoggable("TransformationUtils", 2))
      Log.v("TransformationUtils", "requested target size too big for input, fit centering instead"); 
    return fitCenter(paramBitmapPool, paramBitmap, paramInt1, paramInt2);
  }
  
  public static Bitmap circleCrop(BitmapPool paramBitmapPool, Bitmap paramBitmap, int paramInt1, int paramInt2) {
    int i = Math.min(paramInt1, paramInt2);
    float f1 = i;
    float f2 = f1 / 2.0F;
    paramInt2 = paramBitmap.getWidth();
    paramInt1 = paramBitmap.getHeight();
    float f3 = paramInt2;
    float f4 = f1 / f3;
    float f5 = paramInt1;
    f4 = Math.max(f4, f1 / f5);
    f3 *= f4;
    f4 *= f5;
    f5 = (f1 - f3) / 2.0F;
    f1 = (f1 - f4) / 2.0F;
    RectF rectF = new RectF(f5, f1, f3 + f5, f4 + f1);
    Bitmap bitmap1 = getAlphaSafeBitmap(paramBitmapPool, paramBitmap);
    Bitmap bitmap2 = paramBitmapPool.get(i, i, getAlphaSafeConfig(paramBitmap));
    bitmap2.setHasAlpha(true);
    BITMAP_DRAWABLE_LOCK.lock();
    try {
      Canvas canvas = new Canvas();
      this(bitmap2);
      canvas.drawCircle(f2, f2, f2, CIRCLE_CROP_SHAPE_PAINT);
      canvas.drawBitmap(bitmap1, null, rectF, CIRCLE_CROP_BITMAP_PAINT);
      clear(canvas);
      BITMAP_DRAWABLE_LOCK.unlock();
      return bitmap2;
    } finally {
      BITMAP_DRAWABLE_LOCK.unlock();
    } 
  }
  
  private static void clear(Canvas paramCanvas) {
    paramCanvas.setBitmap(null);
  }
  
  public static Bitmap fitCenter(BitmapPool paramBitmapPool, Bitmap paramBitmap, int paramInt1, int paramInt2) {
    if (paramBitmap.getWidth() == paramInt1 && paramBitmap.getHeight() == paramInt2) {
      if (Log.isLoggable("TransformationUtils", 2))
        Log.v("TransformationUtils", "requested target size matches input, returning input"); 
      return paramBitmap;
    } 
    float f = Math.min(paramInt1 / paramBitmap.getWidth(), paramInt2 / paramBitmap.getHeight());
    int i = Math.round(paramBitmap.getWidth() * f);
    int j = Math.round(paramBitmap.getHeight() * f);
    if (paramBitmap.getWidth() == i && paramBitmap.getHeight() == j) {
      if (Log.isLoggable("TransformationUtils", 2))
        Log.v("TransformationUtils", "adjusted target size matches input, returning input"); 
      return paramBitmap;
    } 
    Bitmap bitmap = paramBitmapPool.get((int)(paramBitmap.getWidth() * f), (int)(paramBitmap.getHeight() * f), getNonNullConfig(paramBitmap));
    setAlpha(paramBitmap, bitmap);
    if (Log.isLoggable("TransformationUtils", 2)) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("request: ");
      stringBuilder.append(paramInt1);
      stringBuilder.append("x");
      stringBuilder.append(paramInt2);
      Log.v("TransformationUtils", stringBuilder.toString());
      stringBuilder = new StringBuilder();
      stringBuilder.append("toFit:   ");
      stringBuilder.append(paramBitmap.getWidth());
      stringBuilder.append("x");
      stringBuilder.append(paramBitmap.getHeight());
      Log.v("TransformationUtils", stringBuilder.toString());
      stringBuilder = new StringBuilder();
      stringBuilder.append("toReuse: ");
      stringBuilder.append(bitmap.getWidth());
      stringBuilder.append("x");
      stringBuilder.append(bitmap.getHeight());
      Log.v("TransformationUtils", stringBuilder.toString());
      stringBuilder = new StringBuilder();
      stringBuilder.append("minPct:   ");
      stringBuilder.append(f);
      Log.v("TransformationUtils", stringBuilder.toString());
    } 
    Matrix matrix = new Matrix();
    matrix.setScale(f, f);
    applyMatrix(paramBitmap, bitmap, matrix);
    return bitmap;
  }
  
  private static Bitmap getAlphaSafeBitmap(BitmapPool paramBitmapPool, Bitmap paramBitmap) {
    Bitmap.Config config = getAlphaSafeConfig(paramBitmap);
    if (config.equals(paramBitmap.getConfig()))
      return paramBitmap; 
    Bitmap bitmap = paramBitmapPool.get(paramBitmap.getWidth(), paramBitmap.getHeight(), config);
    (new Canvas(bitmap)).drawBitmap(paramBitmap, 0.0F, 0.0F, null);
    return bitmap;
  }
  
  private static Bitmap.Config getAlphaSafeConfig(Bitmap paramBitmap) {
    return (Build.VERSION.SDK_INT >= 26 && Bitmap.Config.RGBA_F16.equals(paramBitmap.getConfig())) ? Bitmap.Config.RGBA_F16 : Bitmap.Config.ARGB_8888;
  }
  
  public static Lock getBitmapDrawableLock() {
    return BITMAP_DRAWABLE_LOCK;
  }
  
  public static int getExifOrientationDegrees(int paramInt) {
    switch (paramInt) {
      default:
        return 0;
      case 7:
      case 8:
        return 270;
      case 5:
      case 6:
        return 90;
      case 3:
      case 4:
        break;
    } 
    return 180;
  }
  
  private static Bitmap.Config getNonNullConfig(Bitmap paramBitmap) {
    Bitmap.Config config;
    if (paramBitmap.getConfig() != null) {
      config = paramBitmap.getConfig();
    } else {
      config = Bitmap.Config.ARGB_8888;
    } 
    return config;
  }
  
  static void initializeMatrixForRotation(int paramInt, Matrix paramMatrix) {
    switch (paramInt) {
      default:
        return;
      case 8:
        paramMatrix.setRotate(-90.0F);
      case 7:
        paramMatrix.setRotate(-90.0F);
        paramMatrix.postScale(-1.0F, 1.0F);
      case 6:
        paramMatrix.setRotate(90.0F);
      case 5:
        paramMatrix.setRotate(90.0F);
        paramMatrix.postScale(-1.0F, 1.0F);
      case 4:
        paramMatrix.setRotate(180.0F);
        paramMatrix.postScale(-1.0F, 1.0F);
      case 3:
        paramMatrix.setRotate(180.0F);
      case 2:
        break;
    } 
    paramMatrix.setScale(-1.0F, 1.0F);
  }
  
  public static boolean isExifOrientationRequired(int paramInt) {
    switch (paramInt) {
      default:
        return false;
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
      case 7:
      case 8:
        break;
    } 
    return true;
  }
  
  public static Bitmap rotateImage(Bitmap paramBitmap, int paramInt) {
    Bitmap bitmap = paramBitmap;
    if (paramInt != 0)
      try {
        Matrix matrix = new Matrix();
        this();
        matrix.setRotate(paramInt);
        Bitmap bitmap1 = Bitmap.createBitmap(paramBitmap, 0, 0, paramBitmap.getWidth(), paramBitmap.getHeight(), matrix, true);
      } catch (Exception exception) {
        bitmap = paramBitmap;
        if (Log.isLoggable("TransformationUtils", 6)) {
          Log.e("TransformationUtils", "Exception when trying to orient image", exception);
          bitmap = paramBitmap;
        } 
      }  
    return bitmap;
  }
  
  public static Bitmap rotateImageExif(BitmapPool paramBitmapPool, Bitmap paramBitmap, int paramInt) {
    if (!isExifOrientationRequired(paramInt))
      return paramBitmap; 
    Matrix matrix = new Matrix();
    initializeMatrixForRotation(paramInt, matrix);
    RectF rectF = new RectF(0.0F, 0.0F, paramBitmap.getWidth(), paramBitmap.getHeight());
    matrix.mapRect(rectF);
    Bitmap bitmap = paramBitmapPool.get(Math.round(rectF.width()), Math.round(rectF.height()), getNonNullConfig(paramBitmap));
    matrix.postTranslate(-rectF.left, -rectF.top);
    bitmap.setHasAlpha(paramBitmap.hasAlpha());
    applyMatrix(paramBitmap, bitmap, matrix);
    return bitmap;
  }
  
  public static Bitmap roundedCorners(BitmapPool paramBitmapPool, Bitmap paramBitmap, final float topLeft, final float topRight, final float bottomRight, final float bottomLeft) {
    return roundedCorners(paramBitmapPool, paramBitmap, new DrawRoundedCornerFn() {
          public void drawRoundedCorners(Canvas param1Canvas, Paint param1Paint, RectF param1RectF) {
            Path path = new Path();
            float f1 = topLeft;
            float f2 = topRight;
            float f3 = bottomRight;
            float f4 = bottomLeft;
            Path.Direction direction = Path.Direction.CW;
            path.addRoundRect(param1RectF, new float[] { f1, f1, f2, f2, f3, f3, f4, f4 }, direction);
            param1Canvas.drawPath(path, param1Paint);
          }
        });
  }
  
  public static Bitmap roundedCorners(BitmapPool paramBitmapPool, Bitmap paramBitmap, final int roundingRadius) {
    boolean bool;
    if (roundingRadius > 0) {
      bool = true;
    } else {
      bool = false;
    } 
    Preconditions.checkArgument(bool, "roundingRadius must be greater than 0.");
    return roundedCorners(paramBitmapPool, paramBitmap, new DrawRoundedCornerFn() {
          public void drawRoundedCorners(Canvas param1Canvas, Paint param1Paint, RectF param1RectF) {
            int i = roundingRadius;
            param1Canvas.drawRoundRect(param1RectF, i, i, param1Paint);
          }
        });
  }
  
  @Deprecated
  public static Bitmap roundedCorners(BitmapPool paramBitmapPool, Bitmap paramBitmap, int paramInt1, int paramInt2, int paramInt3) {
    return roundedCorners(paramBitmapPool, paramBitmap, paramInt3);
  }
  
  private static Bitmap roundedCorners(BitmapPool paramBitmapPool, Bitmap paramBitmap, DrawRoundedCornerFn paramDrawRoundedCornerFn) {
    Bitmap.Config config = getAlphaSafeConfig(paramBitmap);
    Bitmap bitmap2 = getAlphaSafeBitmap(paramBitmapPool, paramBitmap);
    Bitmap bitmap1 = paramBitmapPool.get(bitmap2.getWidth(), bitmap2.getHeight(), config);
    bitmap1.setHasAlpha(true);
    BitmapShader bitmapShader = new BitmapShader(bitmap2, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
    Paint paint = new Paint();
    paint.setAntiAlias(true);
    paint.setShader((Shader)bitmapShader);
    RectF rectF = new RectF(0.0F, 0.0F, bitmap1.getWidth(), bitmap1.getHeight());
    BITMAP_DRAWABLE_LOCK.lock();
    try {
      Canvas canvas = new Canvas();
      this(bitmap1);
      canvas.drawColor(0, PorterDuff.Mode.CLEAR);
      paramDrawRoundedCornerFn.drawRoundedCorners(canvas, paint, rectF);
      clear(canvas);
      BITMAP_DRAWABLE_LOCK.unlock();
      return bitmap1;
    } finally {
      BITMAP_DRAWABLE_LOCK.unlock();
    } 
  }
  
  public static void setAlpha(Bitmap paramBitmap1, Bitmap paramBitmap2) {
    paramBitmap2.setHasAlpha(paramBitmap1.hasAlpha());
  }
  
  static {
    NoLock noLock;
  }
  
  private static final Lock BITMAP_DRAWABLE_LOCK;
  
  private static final Paint CIRCLE_CROP_BITMAP_PAINT;
  
  private static final int CIRCLE_CROP_PAINT_FLAGS = 7;
  
  private static final Paint CIRCLE_CROP_SHAPE_PAINT;
  
  private static final Paint DEFAULT_PAINT = new Paint(6);
  
  private static final Set<String> MODELS_REQUIRING_BITMAP_LOCK;
  
  public static final int PAINT_FLAGS = 6;
  
  private static final String TAG = "TransformationUtils";
  
  private static interface DrawRoundedCornerFn {
    void drawRoundedCorners(Canvas param1Canvas, Paint param1Paint, RectF param1RectF);
  }
  
  private static final class NoLock implements Lock {
    public void lock() {}
    
    public void lockInterruptibly() throws InterruptedException {}
    
    public Condition newCondition() {
      throw new UnsupportedOperationException("Should not be called");
    }
    
    public boolean tryLock() {
      return true;
    }
    
    public boolean tryLock(long param1Long, TimeUnit param1TimeUnit) throws InterruptedException {
      return true;
    }
    
    public void unlock() {}
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/resource/bitmap/TransformationUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */