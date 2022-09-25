package androidx.appcompat.graphics.drawable;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import androidx.appcompat.R;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class DrawerArrowDrawable extends Drawable {
  public static final int ARROW_DIRECTION_END = 3;
  
  public static final int ARROW_DIRECTION_LEFT = 0;
  
  public static final int ARROW_DIRECTION_RIGHT = 1;
  
  public static final int ARROW_DIRECTION_START = 2;
  
  private static final float ARROW_HEAD_ANGLE = (float)Math.toRadians(45.0D);
  
  private float mArrowHeadLength;
  
  private float mArrowShaftLength;
  
  private float mBarGap;
  
  private float mBarLength;
  
  private int mDirection = 2;
  
  private float mMaxCutForBarSize;
  
  private final Paint mPaint = new Paint();
  
  private final Path mPath = new Path();
  
  private float mProgress;
  
  private final int mSize;
  
  private boolean mSpin;
  
  private boolean mVerticalMirror = false;
  
  public DrawerArrowDrawable(Context paramContext) {
    this.mPaint.setStyle(Paint.Style.STROKE);
    this.mPaint.setStrokeJoin(Paint.Join.MITER);
    this.mPaint.setStrokeCap(Paint.Cap.BUTT);
    this.mPaint.setAntiAlias(true);
    TypedArray typedArray = paramContext.getTheme().obtainStyledAttributes(null, R.styleable.DrawerArrowToggle, R.attr.drawerArrowStyle, R.style.Base_Widget_AppCompat_DrawerArrowToggle);
    setColor(typedArray.getColor(R.styleable.DrawerArrowToggle_color, 0));
    setBarThickness(typedArray.getDimension(R.styleable.DrawerArrowToggle_thickness, 0.0F));
    setSpinEnabled(typedArray.getBoolean(R.styleable.DrawerArrowToggle_spinBars, true));
    setGapSize(Math.round(typedArray.getDimension(R.styleable.DrawerArrowToggle_gapBetweenBars, 0.0F)));
    this.mSize = typedArray.getDimensionPixelSize(R.styleable.DrawerArrowToggle_drawableSize, 0);
    this.mBarLength = Math.round(typedArray.getDimension(R.styleable.DrawerArrowToggle_barLength, 0.0F));
    this.mArrowHeadLength = Math.round(typedArray.getDimension(R.styleable.DrawerArrowToggle_arrowHeadLength, 0.0F));
    this.mArrowShaftLength = typedArray.getDimension(R.styleable.DrawerArrowToggle_arrowShaftLength, 0.0F);
    typedArray.recycle();
  }
  
  private static float lerp(float paramFloat1, float paramFloat2, float paramFloat3) {
    return paramFloat1 + (paramFloat2 - paramFloat1) * paramFloat3;
  }
  
  public void draw(Canvas paramCanvas) {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual getBounds : ()Landroid/graphics/Rect;
    //   4: astore_2
    //   5: aload_0
    //   6: getfield mDirection : I
    //   9: istore_3
    //   10: iconst_0
    //   11: istore #4
    //   13: iload #4
    //   15: istore #5
    //   17: iload_3
    //   18: ifeq -> 60
    //   21: iload_3
    //   22: iconst_1
    //   23: if_icmpeq -> 57
    //   26: iload_3
    //   27: iconst_3
    //   28: if_icmpeq -> 46
    //   31: iload #4
    //   33: istore #5
    //   35: aload_0
    //   36: invokestatic getLayoutDirection : (Landroid/graphics/drawable/Drawable;)I
    //   39: iconst_1
    //   40: if_icmpne -> 60
    //   43: goto -> 57
    //   46: iload #4
    //   48: istore #5
    //   50: aload_0
    //   51: invokestatic getLayoutDirection : (Landroid/graphics/drawable/Drawable;)I
    //   54: ifne -> 60
    //   57: iconst_1
    //   58: istore #5
    //   60: aload_0
    //   61: getfield mArrowHeadLength : F
    //   64: fstore #6
    //   66: fload #6
    //   68: fload #6
    //   70: fmul
    //   71: fconst_2
    //   72: fmul
    //   73: f2d
    //   74: invokestatic sqrt : (D)D
    //   77: d2f
    //   78: fstore #6
    //   80: aload_0
    //   81: getfield mBarLength : F
    //   84: fload #6
    //   86: aload_0
    //   87: getfield mProgress : F
    //   90: invokestatic lerp : (FFF)F
    //   93: fstore #7
    //   95: aload_0
    //   96: getfield mBarLength : F
    //   99: aload_0
    //   100: getfield mArrowShaftLength : F
    //   103: aload_0
    //   104: getfield mProgress : F
    //   107: invokestatic lerp : (FFF)F
    //   110: fstore #8
    //   112: fconst_0
    //   113: aload_0
    //   114: getfield mMaxCutForBarSize : F
    //   117: aload_0
    //   118: getfield mProgress : F
    //   121: invokestatic lerp : (FFF)F
    //   124: invokestatic round : (F)I
    //   127: i2f
    //   128: fstore #9
    //   130: fconst_0
    //   131: getstatic androidx/appcompat/graphics/drawable/DrawerArrowDrawable.ARROW_HEAD_ANGLE : F
    //   134: aload_0
    //   135: getfield mProgress : F
    //   138: invokestatic lerp : (FFF)F
    //   141: fstore #10
    //   143: iload #5
    //   145: ifeq -> 154
    //   148: fconst_0
    //   149: fstore #6
    //   151: goto -> 158
    //   154: ldc -180.0
    //   156: fstore #6
    //   158: iload #5
    //   160: ifeq -> 170
    //   163: ldc 180.0
    //   165: fstore #11
    //   167: goto -> 173
    //   170: fconst_0
    //   171: fstore #11
    //   173: fload #6
    //   175: fload #11
    //   177: aload_0
    //   178: getfield mProgress : F
    //   181: invokestatic lerp : (FFF)F
    //   184: fstore #6
    //   186: fload #7
    //   188: f2d
    //   189: dstore #12
    //   191: fload #10
    //   193: f2d
    //   194: dstore #14
    //   196: dload #14
    //   198: invokestatic cos : (D)D
    //   201: dstore #16
    //   203: dload #12
    //   205: invokestatic isNaN : (D)Z
    //   208: pop
    //   209: dload #16
    //   211: dload #12
    //   213: dmul
    //   214: invokestatic round : (D)J
    //   217: l2f
    //   218: fstore #11
    //   220: dload #14
    //   222: invokestatic sin : (D)D
    //   225: dstore #16
    //   227: dload #12
    //   229: invokestatic isNaN : (D)Z
    //   232: pop
    //   233: dload #12
    //   235: dload #16
    //   237: dmul
    //   238: invokestatic round : (D)J
    //   241: l2f
    //   242: fstore #7
    //   244: aload_0
    //   245: getfield mPath : Landroid/graphics/Path;
    //   248: invokevirtual rewind : ()V
    //   251: aload_0
    //   252: getfield mBarGap : F
    //   255: aload_0
    //   256: getfield mPaint : Landroid/graphics/Paint;
    //   259: invokevirtual getStrokeWidth : ()F
    //   262: fadd
    //   263: aload_0
    //   264: getfield mMaxCutForBarSize : F
    //   267: fneg
    //   268: aload_0
    //   269: getfield mProgress : F
    //   272: invokestatic lerp : (FFF)F
    //   275: fstore #10
    //   277: fload #8
    //   279: fneg
    //   280: fconst_2
    //   281: fdiv
    //   282: fstore #18
    //   284: aload_0
    //   285: getfield mPath : Landroid/graphics/Path;
    //   288: fload #18
    //   290: fload #9
    //   292: fadd
    //   293: fconst_0
    //   294: invokevirtual moveTo : (FF)V
    //   297: aload_0
    //   298: getfield mPath : Landroid/graphics/Path;
    //   301: fload #8
    //   303: fload #9
    //   305: fconst_2
    //   306: fmul
    //   307: fsub
    //   308: fconst_0
    //   309: invokevirtual rLineTo : (FF)V
    //   312: aload_0
    //   313: getfield mPath : Landroid/graphics/Path;
    //   316: fload #18
    //   318: fload #10
    //   320: invokevirtual moveTo : (FF)V
    //   323: aload_0
    //   324: getfield mPath : Landroid/graphics/Path;
    //   327: fload #11
    //   329: fload #7
    //   331: invokevirtual rLineTo : (FF)V
    //   334: aload_0
    //   335: getfield mPath : Landroid/graphics/Path;
    //   338: fload #18
    //   340: fload #10
    //   342: fneg
    //   343: invokevirtual moveTo : (FF)V
    //   346: aload_0
    //   347: getfield mPath : Landroid/graphics/Path;
    //   350: fload #11
    //   352: fload #7
    //   354: fneg
    //   355: invokevirtual rLineTo : (FF)V
    //   358: aload_0
    //   359: getfield mPath : Landroid/graphics/Path;
    //   362: invokevirtual close : ()V
    //   365: aload_1
    //   366: invokevirtual save : ()I
    //   369: pop
    //   370: aload_0
    //   371: getfield mPaint : Landroid/graphics/Paint;
    //   374: invokevirtual getStrokeWidth : ()F
    //   377: fstore #11
    //   379: aload_2
    //   380: invokevirtual height : ()I
    //   383: i2f
    //   384: fstore #8
    //   386: aload_0
    //   387: getfield mBarGap : F
    //   390: fstore #9
    //   392: fload #8
    //   394: ldc_w 3.0
    //   397: fload #11
    //   399: fmul
    //   400: fsub
    //   401: fconst_2
    //   402: fload #9
    //   404: fmul
    //   405: fsub
    //   406: f2i
    //   407: iconst_4
    //   408: idiv
    //   409: iconst_2
    //   410: imul
    //   411: i2f
    //   412: fstore #8
    //   414: aload_1
    //   415: aload_2
    //   416: invokevirtual centerX : ()I
    //   419: i2f
    //   420: fload #8
    //   422: fload #11
    //   424: ldc_w 1.5
    //   427: fmul
    //   428: fload #9
    //   430: fadd
    //   431: fadd
    //   432: invokevirtual translate : (FF)V
    //   435: aload_0
    //   436: getfield mSpin : Z
    //   439: ifeq -> 474
    //   442: aload_0
    //   443: getfield mVerticalMirror : Z
    //   446: iload #5
    //   448: ixor
    //   449: ifeq -> 458
    //   452: iconst_m1
    //   453: istore #5
    //   455: goto -> 461
    //   458: iconst_1
    //   459: istore #5
    //   461: aload_1
    //   462: fload #6
    //   464: iload #5
    //   466: i2f
    //   467: fmul
    //   468: invokevirtual rotate : (F)V
    //   471: goto -> 485
    //   474: iload #5
    //   476: ifeq -> 485
    //   479: aload_1
    //   480: ldc 180.0
    //   482: invokevirtual rotate : (F)V
    //   485: aload_1
    //   486: aload_0
    //   487: getfield mPath : Landroid/graphics/Path;
    //   490: aload_0
    //   491: getfield mPaint : Landroid/graphics/Paint;
    //   494: invokevirtual drawPath : (Landroid/graphics/Path;Landroid/graphics/Paint;)V
    //   497: aload_1
    //   498: invokevirtual restore : ()V
    //   501: return
  }
  
  public float getArrowHeadLength() {
    return this.mArrowHeadLength;
  }
  
  public float getArrowShaftLength() {
    return this.mArrowShaftLength;
  }
  
  public float getBarLength() {
    return this.mBarLength;
  }
  
  public float getBarThickness() {
    return this.mPaint.getStrokeWidth();
  }
  
  public int getColor() {
    return this.mPaint.getColor();
  }
  
  public int getDirection() {
    return this.mDirection;
  }
  
  public float getGapSize() {
    return this.mBarGap;
  }
  
  public int getIntrinsicHeight() {
    return this.mSize;
  }
  
  public int getIntrinsicWidth() {
    return this.mSize;
  }
  
  public int getOpacity() {
    return -3;
  }
  
  public final Paint getPaint() {
    return this.mPaint;
  }
  
  public float getProgress() {
    return this.mProgress;
  }
  
  public boolean isSpinEnabled() {
    return this.mSpin;
  }
  
  public void setAlpha(int paramInt) {
    if (paramInt != this.mPaint.getAlpha()) {
      this.mPaint.setAlpha(paramInt);
      invalidateSelf();
    } 
  }
  
  public void setArrowHeadLength(float paramFloat) {
    if (this.mArrowHeadLength != paramFloat) {
      this.mArrowHeadLength = paramFloat;
      invalidateSelf();
    } 
  }
  
  public void setArrowShaftLength(float paramFloat) {
    if (this.mArrowShaftLength != paramFloat) {
      this.mArrowShaftLength = paramFloat;
      invalidateSelf();
    } 
  }
  
  public void setBarLength(float paramFloat) {
    if (this.mBarLength != paramFloat) {
      this.mBarLength = paramFloat;
      invalidateSelf();
    } 
  }
  
  public void setBarThickness(float paramFloat) {
    if (this.mPaint.getStrokeWidth() != paramFloat) {
      this.mPaint.setStrokeWidth(paramFloat);
      double d1 = (paramFloat / 2.0F);
      double d2 = Math.cos(ARROW_HEAD_ANGLE);
      Double.isNaN(d1);
      this.mMaxCutForBarSize = (float)(d1 * d2);
      invalidateSelf();
    } 
  }
  
  public void setColor(int paramInt) {
    if (paramInt != this.mPaint.getColor()) {
      this.mPaint.setColor(paramInt);
      invalidateSelf();
    } 
  }
  
  public void setColorFilter(ColorFilter paramColorFilter) {
    this.mPaint.setColorFilter(paramColorFilter);
    invalidateSelf();
  }
  
  public void setDirection(int paramInt) {
    if (paramInt != this.mDirection) {
      this.mDirection = paramInt;
      invalidateSelf();
    } 
  }
  
  public void setGapSize(float paramFloat) {
    if (paramFloat != this.mBarGap) {
      this.mBarGap = paramFloat;
      invalidateSelf();
    } 
  }
  
  public void setProgress(float paramFloat) {
    if (this.mProgress != paramFloat) {
      this.mProgress = paramFloat;
      invalidateSelf();
    } 
  }
  
  public void setSpinEnabled(boolean paramBoolean) {
    if (this.mSpin != paramBoolean) {
      this.mSpin = paramBoolean;
      invalidateSelf();
    } 
  }
  
  public void setVerticalMirror(boolean paramBoolean) {
    if (this.mVerticalMirror != paramBoolean) {
      this.mVerticalMirror = paramBoolean;
      invalidateSelf();
    } 
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface ArrowDirection {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/androidx/appcompat/graphics/drawable/DrawerArrowDrawable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */