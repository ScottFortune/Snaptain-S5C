package com.netopsun.drone.photo_activity;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class PinchImageView extends ImageView {
  public static final float FLING_DAMPING_FACTOR = 0.9F;
  
  private static final float MAX_SCALE = 4.0F;
  
  public static final int PINCH_MODE_FREE = 0;
  
  public static final int PINCH_MODE_SCALE = 2;
  
  public static final int PINCH_MODE_SCROLL = 1;
  
  public static final int SCALE_ANIMATOR_DURATION = 200;
  
  private int mDispatchOuterMatrixChangedLock;
  
  private FlingAnimator mFlingAnimator;
  
  private GestureDetector mGestureDetector = new GestureDetector(getContext(), (GestureDetector.OnGestureListener)new GestureDetector.SimpleOnGestureListener() {
        public boolean onDoubleTap(MotionEvent param1MotionEvent) {
          if (PinchImageView.this.mPinchMode == 1 && (PinchImageView.this.mScaleAnimator == null || !PinchImageView.this.mScaleAnimator.isRunning()))
            PinchImageView.this.doubleTap(param1MotionEvent.getX(), param1MotionEvent.getY()); 
          return true;
        }
        
        public boolean onFling(MotionEvent param1MotionEvent1, MotionEvent param1MotionEvent2, float param1Float1, float param1Float2) {
          if (PinchImageView.this.mPinchMode == 0 && (PinchImageView.this.mScaleAnimator == null || !PinchImageView.this.mScaleAnimator.isRunning()))
            PinchImageView.this.fling(param1Float1, param1Float2); 
          return true;
        }
        
        public void onLongPress(MotionEvent param1MotionEvent) {
          if (PinchImageView.this.mOnLongClickListener != null)
            PinchImageView.this.mOnLongClickListener.onLongClick((View)PinchImageView.this); 
        }
        
        public boolean onSingleTapConfirmed(MotionEvent param1MotionEvent) {
          if (PinchImageView.this.mOnClickListener != null)
            PinchImageView.this.mOnClickListener.onClick((View)PinchImageView.this); 
          return true;
        }
      });
  
  private PointF mLastMovePoint = new PointF();
  
  private RectF mMask;
  
  private MaskAnimator mMaskAnimator;
  
  private View.OnClickListener mOnClickListener;
  
  private View.OnLongClickListener mOnLongClickListener;
  
  private Matrix mOuterMatrix = new Matrix();
  
  private List<OuterMatrixChangedListener> mOuterMatrixChangedListeners;
  
  private List<OuterMatrixChangedListener> mOuterMatrixChangedListenersCopy;
  
  private int mPinchMode = 0;
  
  private ScaleAnimator mScaleAnimator;
  
  private float mScaleBase = 0.0F;
  
  private PointF mScaleCenter = new PointF();
  
  public PinchImageView(Context paramContext) {
    super(paramContext);
    initView();
  }
  
  public PinchImageView(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    initView();
  }
  
  public PinchImageView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    initView();
  }
  
  private void cancelAllAnimator() {
    ScaleAnimator scaleAnimator = this.mScaleAnimator;
    if (scaleAnimator != null) {
      scaleAnimator.cancel();
      this.mScaleAnimator = null;
    } 
    FlingAnimator flingAnimator = this.mFlingAnimator;
    if (flingAnimator != null) {
      flingAnimator.cancel();
      this.mFlingAnimator = null;
    } 
  }
  
  private void dispatchOuterMatrixChanged() {
    List<OuterMatrixChangedListener> list = this.mOuterMatrixChangedListeners;
    if (list == null)
      return; 
    this.mDispatchOuterMatrixChangedLock++;
    Iterator<OuterMatrixChangedListener> iterator = list.iterator();
    while (iterator.hasNext())
      ((OuterMatrixChangedListener)iterator.next()).onOuterMatrixChanged(this); 
    this.mDispatchOuterMatrixChangedLock--;
    if (this.mDispatchOuterMatrixChangedLock == 0) {
      List<OuterMatrixChangedListener> list1 = this.mOuterMatrixChangedListenersCopy;
      if (list1 != null) {
        this.mOuterMatrixChangedListeners = list1;
        this.mOuterMatrixChangedListenersCopy = null;
      } 
    } 
  }
  
  private void doubleTap(float paramFloat1, float paramFloat2) {
    if (!isReady())
      return; 
    Matrix matrix1 = MathUtils.matrixTake();
    getInnerMatrix(matrix1);
    float f1 = MathUtils.getMatrixScale(matrix1)[0];
    float f2 = MathUtils.getMatrixScale(this.mOuterMatrix)[0];
    float f3 = getWidth();
    float f4 = getHeight();
    float f5 = getMaxScale();
    float f6 = calculateNextScale(f1, f2);
    float f7 = f6;
    if (f6 > f5)
      f7 = f5; 
    if (f7 < f1)
      f7 = f1; 
    Matrix matrix2 = MathUtils.matrixTake(this.mOuterMatrix);
    f7 /= f1 * f2;
    matrix2.postScale(f7, f7, paramFloat1, paramFloat2);
    f1 = f3 / 2.0F;
    f7 = f4 / 2.0F;
    matrix2.postTranslate(f1 - paramFloat1, f7 - paramFloat2);
    Matrix matrix3 = MathUtils.matrixTake(matrix1);
    matrix3.postConcat(matrix2);
    f5 = getDrawable().getIntrinsicWidth();
    paramFloat1 = getDrawable().getIntrinsicHeight();
    paramFloat2 = 0.0F;
    RectF rectF = MathUtils.rectFTake(0.0F, 0.0F, f5, paramFloat1);
    matrix3.mapRect(rectF);
    if (rectF.right - rectF.left < f3) {
      paramFloat1 = f1 - (rectF.right + rectF.left) / 2.0F;
    } else if (rectF.left > 0.0F) {
      paramFloat1 = -rectF.left;
    } else if (rectF.right < f3) {
      paramFloat1 = f3 - rectF.right;
    } else {
      paramFloat1 = 0.0F;
    } 
    if (rectF.bottom - rectF.top < f4) {
      paramFloat2 = f7 - (rectF.bottom + rectF.top) / 2.0F;
    } else if (rectF.top > 0.0F) {
      paramFloat2 = -rectF.top;
    } else if (rectF.bottom < f4) {
      paramFloat2 = f4 - rectF.bottom;
    } 
    matrix2.postTranslate(paramFloat1, paramFloat2);
    cancelAllAnimator();
    this.mScaleAnimator = new ScaleAnimator(this.mOuterMatrix, matrix2);
    this.mScaleAnimator.start();
    MathUtils.rectFGiven(rectF);
    MathUtils.matrixGiven(matrix3);
    MathUtils.matrixGiven(matrix2);
    MathUtils.matrixGiven(matrix1);
  }
  
  private void fling(float paramFloat1, float paramFloat2) {
    if (!isReady())
      return; 
    cancelAllAnimator();
    this.mFlingAnimator = new FlingAnimator(paramFloat1 / 60.0F, paramFloat2 / 60.0F);
    this.mFlingAnimator.start();
  }
  
  private void initView() {
    super.setScaleType(ImageView.ScaleType.MATRIX);
  }
  
  private boolean isReady() {
    boolean bool;
    if (getDrawable() != null && getDrawable().getIntrinsicWidth() > 0 && getDrawable().getIntrinsicHeight() > 0 && getWidth() > 0 && getHeight() > 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private void saveScaleContext(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
    this.mScaleBase = MathUtils.getMatrixScale(this.mOuterMatrix)[0] / MathUtils.getDistance(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
    float[] arrayOfFloat = MathUtils.inverseMatrixPoint(MathUtils.getCenterPoint(paramFloat1, paramFloat2, paramFloat3, paramFloat4), this.mOuterMatrix);
    this.mScaleCenter.set(arrayOfFloat[0], arrayOfFloat[1]);
  }
  
  private void scale(PointF paramPointF1, float paramFloat1, float paramFloat2, PointF paramPointF2) {
    if (!isReady())
      return; 
    paramFloat1 *= paramFloat2;
    Matrix matrix = MathUtils.matrixTake();
    matrix.postScale(paramFloat1, paramFloat1, paramPointF1.x, paramPointF1.y);
    matrix.postTranslate(paramPointF2.x - paramPointF1.x, paramPointF2.y - paramPointF1.y);
    this.mOuterMatrix.set(matrix);
    MathUtils.matrixGiven(matrix);
    dispatchOuterMatrixChanged();
    invalidate();
  }
  
  private void scaleEnd() {
    // Byte code:
    //   0: aload_0
    //   1: invokespecial isReady : ()Z
    //   4: ifne -> 8
    //   7: return
    //   8: invokestatic matrixTake : ()Landroid/graphics/Matrix;
    //   11: astore_1
    //   12: aload_0
    //   13: aload_1
    //   14: invokevirtual getCurrentImageMatrix : (Landroid/graphics/Matrix;)Landroid/graphics/Matrix;
    //   17: pop
    //   18: aload_1
    //   19: invokestatic getMatrixScale : (Landroid/graphics/Matrix;)[F
    //   22: astore_2
    //   23: iconst_0
    //   24: istore_3
    //   25: aload_2
    //   26: iconst_0
    //   27: faload
    //   28: fstore #4
    //   30: aload_0
    //   31: getfield mOuterMatrix : Landroid/graphics/Matrix;
    //   34: invokestatic getMatrixScale : (Landroid/graphics/Matrix;)[F
    //   37: iconst_0
    //   38: faload
    //   39: fstore #5
    //   41: aload_0
    //   42: invokevirtual getWidth : ()I
    //   45: i2f
    //   46: fstore #6
    //   48: aload_0
    //   49: invokevirtual getHeight : ()I
    //   52: i2f
    //   53: fstore #7
    //   55: aload_0
    //   56: invokevirtual getMaxScale : ()F
    //   59: fstore #8
    //   61: fload #4
    //   63: fload #8
    //   65: fcmpl
    //   66: ifle -> 79
    //   69: fload #8
    //   71: fload #4
    //   73: fdiv
    //   74: fstore #4
    //   76: goto -> 82
    //   79: fconst_1
    //   80: fstore #4
    //   82: fload #4
    //   84: fstore #8
    //   86: fload #5
    //   88: fload #4
    //   90: fmul
    //   91: fconst_1
    //   92: fcmpg
    //   93: ifge -> 102
    //   96: fconst_1
    //   97: fload #5
    //   99: fdiv
    //   100: fstore #8
    //   102: fload #8
    //   104: fconst_1
    //   105: fcmpl
    //   106: ifeq -> 111
    //   109: iconst_1
    //   110: istore_3
    //   111: aload_1
    //   112: invokestatic matrixTake : (Landroid/graphics/Matrix;)Landroid/graphics/Matrix;
    //   115: astore #9
    //   117: aload #9
    //   119: fload #8
    //   121: fload #8
    //   123: aload_0
    //   124: getfield mLastMovePoint : Landroid/graphics/PointF;
    //   127: getfield x : F
    //   130: aload_0
    //   131: getfield mLastMovePoint : Landroid/graphics/PointF;
    //   134: getfield y : F
    //   137: invokevirtual postScale : (FFFF)Z
    //   140: pop
    //   141: fconst_0
    //   142: fconst_0
    //   143: aload_0
    //   144: invokevirtual getDrawable : ()Landroid/graphics/drawable/Drawable;
    //   147: invokevirtual getIntrinsicWidth : ()I
    //   150: i2f
    //   151: aload_0
    //   152: invokevirtual getDrawable : ()Landroid/graphics/drawable/Drawable;
    //   155: invokevirtual getIntrinsicHeight : ()I
    //   158: i2f
    //   159: invokestatic rectFTake : (FFFF)Landroid/graphics/RectF;
    //   162: astore #10
    //   164: aload #9
    //   166: aload #10
    //   168: invokevirtual mapRect : (Landroid/graphics/RectF;)Z
    //   171: pop
    //   172: aload #10
    //   174: getfield right : F
    //   177: aload #10
    //   179: getfield left : F
    //   182: fsub
    //   183: fload #6
    //   185: fcmpg
    //   186: ifge -> 220
    //   189: fload #6
    //   191: fconst_2
    //   192: fdiv
    //   193: fstore #6
    //   195: aload #10
    //   197: getfield right : F
    //   200: aload #10
    //   202: getfield left : F
    //   205: fadd
    //   206: fconst_2
    //   207: fdiv
    //   208: fstore #4
    //   210: fload #6
    //   212: fload #4
    //   214: fsub
    //   215: fstore #4
    //   217: goto -> 265
    //   220: aload #10
    //   222: getfield left : F
    //   225: fconst_0
    //   226: fcmpl
    //   227: ifle -> 241
    //   230: aload #10
    //   232: getfield left : F
    //   235: fneg
    //   236: fstore #4
    //   238: goto -> 265
    //   241: aload #10
    //   243: getfield right : F
    //   246: fload #6
    //   248: fcmpg
    //   249: ifge -> 262
    //   252: aload #10
    //   254: getfield right : F
    //   257: fstore #4
    //   259: goto -> 210
    //   262: fconst_0
    //   263: fstore #4
    //   265: aload #10
    //   267: getfield bottom : F
    //   270: aload #10
    //   272: getfield top : F
    //   275: fsub
    //   276: fload #7
    //   278: fcmpg
    //   279: ifge -> 313
    //   282: fload #7
    //   284: fconst_2
    //   285: fdiv
    //   286: fstore #6
    //   288: aload #10
    //   290: getfield bottom : F
    //   293: aload #10
    //   295: getfield top : F
    //   298: fadd
    //   299: fconst_2
    //   300: fdiv
    //   301: fstore #7
    //   303: fload #6
    //   305: fload #7
    //   307: fsub
    //   308: fstore #6
    //   310: goto -> 366
    //   313: aload #10
    //   315: getfield top : F
    //   318: fconst_0
    //   319: fcmpl
    //   320: ifle -> 334
    //   323: aload #10
    //   325: getfield top : F
    //   328: fneg
    //   329: fstore #6
    //   331: goto -> 366
    //   334: aload #10
    //   336: getfield bottom : F
    //   339: fload #7
    //   341: fcmpg
    //   342: ifge -> 363
    //   345: aload #10
    //   347: getfield bottom : F
    //   350: fstore #5
    //   352: fload #7
    //   354: fstore #6
    //   356: fload #5
    //   358: fstore #7
    //   360: goto -> 303
    //   363: fconst_0
    //   364: fstore #6
    //   366: fload #4
    //   368: fconst_0
    //   369: fcmpl
    //   370: ifne -> 380
    //   373: fload #6
    //   375: fconst_0
    //   376: fcmpl
    //   377: ifeq -> 382
    //   380: iconst_1
    //   381: istore_3
    //   382: iload_3
    //   383: ifeq -> 458
    //   386: aload_0
    //   387: getfield mOuterMatrix : Landroid/graphics/Matrix;
    //   390: invokestatic matrixTake : (Landroid/graphics/Matrix;)Landroid/graphics/Matrix;
    //   393: astore_2
    //   394: aload_2
    //   395: fload #8
    //   397: fload #8
    //   399: aload_0
    //   400: getfield mLastMovePoint : Landroid/graphics/PointF;
    //   403: getfield x : F
    //   406: aload_0
    //   407: getfield mLastMovePoint : Landroid/graphics/PointF;
    //   410: getfield y : F
    //   413: invokevirtual postScale : (FFFF)Z
    //   416: pop
    //   417: aload_2
    //   418: fload #4
    //   420: fload #6
    //   422: invokevirtual postTranslate : (FF)Z
    //   425: pop
    //   426: aload_0
    //   427: invokespecial cancelAllAnimator : ()V
    //   430: aload_0
    //   431: new com/netopsun/drone/photo_activity/PinchImageView$ScaleAnimator
    //   434: dup
    //   435: aload_0
    //   436: aload_0
    //   437: getfield mOuterMatrix : Landroid/graphics/Matrix;
    //   440: aload_2
    //   441: invokespecial <init> : (Lcom/netopsun/drone/photo_activity/PinchImageView;Landroid/graphics/Matrix;Landroid/graphics/Matrix;)V
    //   444: putfield mScaleAnimator : Lcom/netopsun/drone/photo_activity/PinchImageView$ScaleAnimator;
    //   447: aload_0
    //   448: getfield mScaleAnimator : Lcom/netopsun/drone/photo_activity/PinchImageView$ScaleAnimator;
    //   451: invokevirtual start : ()V
    //   454: aload_2
    //   455: invokestatic matrixGiven : (Landroid/graphics/Matrix;)V
    //   458: aload #10
    //   460: invokestatic rectFGiven : (Landroid/graphics/RectF;)V
    //   463: aload #9
    //   465: invokestatic matrixGiven : (Landroid/graphics/Matrix;)V
    //   468: aload_1
    //   469: invokestatic matrixGiven : (Landroid/graphics/Matrix;)V
    //   472: return
  }
  
  private boolean scrollBy(float paramFloat1, float paramFloat2) {
    // Byte code:
    //   0: aload_0
    //   1: invokespecial isReady : ()Z
    //   4: istore_3
    //   5: iconst_0
    //   6: istore #4
    //   8: iload_3
    //   9: ifne -> 14
    //   12: iconst_0
    //   13: ireturn
    //   14: invokestatic rectFTake : ()Landroid/graphics/RectF;
    //   17: astore #5
    //   19: aload_0
    //   20: aload #5
    //   22: invokevirtual getImageBound : (Landroid/graphics/RectF;)Landroid/graphics/RectF;
    //   25: pop
    //   26: aload_0
    //   27: invokevirtual getWidth : ()I
    //   30: i2f
    //   31: fstore #6
    //   33: aload_0
    //   34: invokevirtual getHeight : ()I
    //   37: i2f
    //   38: fstore #7
    //   40: aload #5
    //   42: getfield right : F
    //   45: aload #5
    //   47: getfield left : F
    //   50: fsub
    //   51: fload #6
    //   53: fcmpg
    //   54: ifge -> 63
    //   57: fconst_0
    //   58: fstore #8
    //   60: goto -> 133
    //   63: aload #5
    //   65: getfield left : F
    //   68: fload_1
    //   69: fadd
    //   70: fconst_0
    //   71: fcmpl
    //   72: ifle -> 96
    //   75: aload #5
    //   77: getfield left : F
    //   80: fconst_0
    //   81: fcmpg
    //   82: ifge -> 57
    //   85: aload #5
    //   87: getfield left : F
    //   90: fneg
    //   91: fstore #8
    //   93: goto -> 133
    //   96: fload_1
    //   97: fstore #8
    //   99: aload #5
    //   101: getfield right : F
    //   104: fload_1
    //   105: fadd
    //   106: fload #6
    //   108: fcmpg
    //   109: ifge -> 133
    //   112: aload #5
    //   114: getfield right : F
    //   117: fload #6
    //   119: fcmpl
    //   120: ifle -> 57
    //   123: fload #6
    //   125: aload #5
    //   127: getfield right : F
    //   130: fsub
    //   131: fstore #8
    //   133: aload #5
    //   135: getfield bottom : F
    //   138: aload #5
    //   140: getfield top : F
    //   143: fsub
    //   144: fload #7
    //   146: fcmpg
    //   147: ifge -> 155
    //   150: fconst_0
    //   151: fstore_1
    //   152: goto -> 222
    //   155: aload #5
    //   157: getfield top : F
    //   160: fload_2
    //   161: fadd
    //   162: fconst_0
    //   163: fcmpl
    //   164: ifle -> 187
    //   167: aload #5
    //   169: getfield top : F
    //   172: fconst_0
    //   173: fcmpg
    //   174: ifge -> 150
    //   177: aload #5
    //   179: getfield top : F
    //   182: fneg
    //   183: fstore_1
    //   184: goto -> 222
    //   187: fload_2
    //   188: fstore_1
    //   189: aload #5
    //   191: getfield bottom : F
    //   194: fload_2
    //   195: fadd
    //   196: fload #7
    //   198: fcmpg
    //   199: ifge -> 222
    //   202: aload #5
    //   204: getfield bottom : F
    //   207: fload #7
    //   209: fcmpl
    //   210: ifle -> 150
    //   213: fload #7
    //   215: aload #5
    //   217: getfield bottom : F
    //   220: fsub
    //   221: fstore_1
    //   222: aload #5
    //   224: invokestatic rectFGiven : (Landroid/graphics/RectF;)V
    //   227: aload_0
    //   228: getfield mOuterMatrix : Landroid/graphics/Matrix;
    //   231: fload #8
    //   233: fload_1
    //   234: invokevirtual postTranslate : (FF)Z
    //   237: pop
    //   238: aload_0
    //   239: invokespecial dispatchOuterMatrixChanged : ()V
    //   242: aload_0
    //   243: invokevirtual invalidate : ()V
    //   246: fload #8
    //   248: fconst_0
    //   249: fcmpl
    //   250: ifne -> 259
    //   253: fload_1
    //   254: fconst_0
    //   255: fcmpl
    //   256: ifeq -> 262
    //   259: iconst_1
    //   260: istore #4
    //   262: iload #4
    //   264: ireturn
  }
  
  public void addOuterMatrixChangedListener(OuterMatrixChangedListener paramOuterMatrixChangedListener) {
    if (paramOuterMatrixChangedListener == null)
      return; 
    if (this.mDispatchOuterMatrixChangedLock == 0) {
      if (this.mOuterMatrixChangedListeners == null)
        this.mOuterMatrixChangedListeners = new ArrayList<OuterMatrixChangedListener>(); 
      this.mOuterMatrixChangedListeners.add(paramOuterMatrixChangedListener);
    } else {
      if (this.mOuterMatrixChangedListenersCopy == null) {
        List<OuterMatrixChangedListener> list = this.mOuterMatrixChangedListeners;
        if (list != null) {
          this.mOuterMatrixChangedListenersCopy = new ArrayList<OuterMatrixChangedListener>(list);
        } else {
          this.mOuterMatrixChangedListenersCopy = new ArrayList<OuterMatrixChangedListener>();
        } 
      } 
      this.mOuterMatrixChangedListenersCopy.add(paramOuterMatrixChangedListener);
    } 
  }
  
  protected float calculateNextScale(float paramFloat1, float paramFloat2) {
    return (paramFloat2 * paramFloat1 < 4.0F) ? 4.0F : paramFloat1;
  }
  
  public boolean canScrollHorizontally(int paramInt) {
    int i = this.mPinchMode;
    boolean bool1 = true;
    boolean bool2 = true;
    if (i == 2)
      return true; 
    RectF rectF = getImageBound((RectF)null);
    if (rectF == null)
      return false; 
    if (rectF.isEmpty())
      return false; 
    if (paramInt > 0) {
      if (rectF.right <= getWidth())
        bool2 = false; 
      return bool2;
    } 
    if (rectF.left < 0.0F) {
      bool2 = bool1;
    } else {
      bool2 = false;
    } 
    return bool2;
  }
  
  public boolean canScrollVertically(int paramInt) {
    int i = this.mPinchMode;
    boolean bool1 = true;
    boolean bool2 = true;
    if (i == 2)
      return true; 
    RectF rectF = getImageBound((RectF)null);
    if (rectF == null)
      return false; 
    if (rectF.isEmpty())
      return false; 
    if (paramInt > 0) {
      if (rectF.bottom <= getHeight())
        bool2 = false; 
      return bool2;
    } 
    if (rectF.top < 0.0F) {
      bool2 = bool1;
    } else {
      bool2 = false;
    } 
    return bool2;
  }
  
  public Matrix getCurrentImageMatrix(Matrix paramMatrix) {
    paramMatrix = getInnerMatrix(paramMatrix);
    paramMatrix.postConcat(this.mOuterMatrix);
    return paramMatrix;
  }
  
  public RectF getImageBound(RectF paramRectF) {
    if (paramRectF == null) {
      paramRectF = new RectF();
    } else {
      paramRectF.setEmpty();
    } 
    if (!isReady())
      return paramRectF; 
    Matrix matrix = MathUtils.matrixTake();
    getCurrentImageMatrix(matrix);
    paramRectF.set(0.0F, 0.0F, getDrawable().getIntrinsicWidth(), getDrawable().getIntrinsicHeight());
    matrix.mapRect(paramRectF);
    MathUtils.matrixGiven(matrix);
    return paramRectF;
  }
  
  public Matrix getInnerMatrix(Matrix paramMatrix) {
    if (paramMatrix == null) {
      paramMatrix = new Matrix();
    } else {
      paramMatrix.reset();
    } 
    if (isReady()) {
      RectF rectF1 = MathUtils.rectFTake(0.0F, 0.0F, getDrawable().getIntrinsicWidth(), getDrawable().getIntrinsicHeight());
      RectF rectF2 = MathUtils.rectFTake(0.0F, 0.0F, getWidth(), getHeight());
      paramMatrix.setRectToRect(rectF1, rectF2, Matrix.ScaleToFit.CENTER);
      MathUtils.rectFGiven(rectF2);
      MathUtils.rectFGiven(rectF1);
    } 
    return paramMatrix;
  }
  
  public RectF getMask() {
    RectF rectF = this.mMask;
    return (rectF != null) ? new RectF(rectF) : null;
  }
  
  protected float getMaxScale() {
    return 4.0F;
  }
  
  public Matrix getOuterMatrix(Matrix paramMatrix) {
    if (paramMatrix == null) {
      paramMatrix = new Matrix(this.mOuterMatrix);
    } else {
      paramMatrix.set(this.mOuterMatrix);
    } 
    return paramMatrix;
  }
  
  public int getPinchMode() {
    return this.mPinchMode;
  }
  
  protected void onDraw(Canvas paramCanvas) {
    if (isReady()) {
      Matrix matrix = MathUtils.matrixTake();
      setImageMatrix(getCurrentImageMatrix(matrix));
      MathUtils.matrixGiven(matrix);
    } 
    if (this.mMask != null) {
      paramCanvas.save();
      paramCanvas.clipRect(this.mMask);
      super.onDraw(paramCanvas);
      paramCanvas.restore();
    } else {
      super.onDraw(paramCanvas);
    } 
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    super.onTouchEvent(paramMotionEvent);
    int i = paramMotionEvent.getAction() & 0xFF;
    if (i == 1 || i == 3) {
      if (this.mPinchMode == 2)
        scaleEnd(); 
      this.mPinchMode = 0;
      this.mGestureDetector.onTouchEvent(paramMotionEvent);
      return true;
    } 
    if (i == 6) {
      if (this.mPinchMode == 2 && paramMotionEvent.getPointerCount() > 2)
        if (paramMotionEvent.getAction() >> 8 == 0) {
          saveScaleContext(paramMotionEvent.getX(1), paramMotionEvent.getY(1), paramMotionEvent.getX(2), paramMotionEvent.getY(2));
        } else if (paramMotionEvent.getAction() >> 8 == 1) {
          saveScaleContext(paramMotionEvent.getX(0), paramMotionEvent.getY(0), paramMotionEvent.getX(2), paramMotionEvent.getY(2));
        }  
    } else if (i == 0) {
      ScaleAnimator scaleAnimator = this.mScaleAnimator;
      if (scaleAnimator == null || !scaleAnimator.isRunning()) {
        cancelAllAnimator();
        this.mPinchMode = 1;
        this.mLastMovePoint.set(paramMotionEvent.getX(), paramMotionEvent.getY());
      } 
    } else if (i == 5) {
      cancelAllAnimator();
      this.mPinchMode = 2;
      saveScaleContext(paramMotionEvent.getX(0), paramMotionEvent.getY(0), paramMotionEvent.getX(1), paramMotionEvent.getY(1));
    } else if (i == 2) {
      ScaleAnimator scaleAnimator = this.mScaleAnimator;
      if (scaleAnimator == null || !scaleAnimator.isRunning()) {
        i = this.mPinchMode;
        if (i == 1) {
          scrollBy(paramMotionEvent.getX() - this.mLastMovePoint.x, paramMotionEvent.getY() - this.mLastMovePoint.y);
          this.mLastMovePoint.set(paramMotionEvent.getX(), paramMotionEvent.getY());
        } else if (i == 2 && paramMotionEvent.getPointerCount() > 1) {
          float f = MathUtils.getDistance(paramMotionEvent.getX(0), paramMotionEvent.getY(0), paramMotionEvent.getX(1), paramMotionEvent.getY(1));
          float[] arrayOfFloat = MathUtils.getCenterPoint(paramMotionEvent.getX(0), paramMotionEvent.getY(0), paramMotionEvent.getX(1), paramMotionEvent.getY(1));
          this.mLastMovePoint.set(arrayOfFloat[0], arrayOfFloat[1]);
          scale(this.mScaleCenter, this.mScaleBase, f, this.mLastMovePoint);
        } 
      } 
    } 
    this.mGestureDetector.onTouchEvent(paramMotionEvent);
    return true;
  }
  
  public void outerMatrixTo(Matrix paramMatrix, long paramLong) {
    if (paramMatrix == null)
      return; 
    this.mPinchMode = 0;
    cancelAllAnimator();
    if (paramLong <= 0L) {
      this.mOuterMatrix.set(paramMatrix);
      dispatchOuterMatrixChanged();
      invalidate();
    } else {
      this.mScaleAnimator = new ScaleAnimator(this.mOuterMatrix, paramMatrix, paramLong);
      this.mScaleAnimator.start();
    } 
  }
  
  public void removeOuterMatrixChangedListener(OuterMatrixChangedListener paramOuterMatrixChangedListener) {
    if (paramOuterMatrixChangedListener == null)
      return; 
    if (this.mDispatchOuterMatrixChangedLock == 0) {
      List<OuterMatrixChangedListener> list = this.mOuterMatrixChangedListeners;
      if (list != null)
        list.remove(paramOuterMatrixChangedListener); 
    } else {
      if (this.mOuterMatrixChangedListenersCopy == null) {
        List<OuterMatrixChangedListener> list1 = this.mOuterMatrixChangedListeners;
        if (list1 != null)
          this.mOuterMatrixChangedListenersCopy = new ArrayList<OuterMatrixChangedListener>(list1); 
      } 
      List<OuterMatrixChangedListener> list = this.mOuterMatrixChangedListenersCopy;
      if (list != null)
        list.remove(paramOuterMatrixChangedListener); 
    } 
  }
  
  public void reset() {
    this.mOuterMatrix.reset();
    dispatchOuterMatrixChanged();
    this.mMask = null;
    this.mPinchMode = 0;
    this.mLastMovePoint.set(0.0F, 0.0F);
    this.mScaleCenter.set(0.0F, 0.0F);
    this.mScaleBase = 0.0F;
    MaskAnimator maskAnimator = this.mMaskAnimator;
    if (maskAnimator != null) {
      maskAnimator.cancel();
      this.mMaskAnimator = null;
    } 
    cancelAllAnimator();
    invalidate();
  }
  
  public void setOnClickListener(View.OnClickListener paramOnClickListener) {
    this.mOnClickListener = paramOnClickListener;
  }
  
  public void setOnLongClickListener(View.OnLongClickListener paramOnLongClickListener) {
    this.mOnLongClickListener = paramOnLongClickListener;
  }
  
  public void setScaleType(ImageView.ScaleType paramScaleType) {}
  
  public void zoomMaskTo(RectF paramRectF, long paramLong) {
    if (paramRectF == null)
      return; 
    MaskAnimator maskAnimator = this.mMaskAnimator;
    if (maskAnimator != null) {
      maskAnimator.cancel();
      this.mMaskAnimator = null;
    } 
    if (paramLong > 0L) {
      RectF rectF = this.mMask;
      if (rectF == null) {
        if (this.mMask == null)
          this.mMask = new RectF(); 
        this.mMask.set(paramRectF);
        invalidate();
        return;
      } 
      this.mMaskAnimator = new MaskAnimator(rectF, paramRectF, paramLong);
      this.mMaskAnimator.start();
      return;
    } 
    if (this.mMask == null)
      this.mMask = new RectF(); 
    this.mMask.set(paramRectF);
    invalidate();
  }
  
  private class FlingAnimator extends ValueAnimator implements ValueAnimator.AnimatorUpdateListener {
    private float[] mVector;
    
    public FlingAnimator(float param1Float1, float param1Float2) {
      setFloatValues(new float[] { 0.0F, 1.0F });
      setDuration(1000000L);
      addUpdateListener(this);
      this.mVector = new float[] { param1Float1, param1Float2 };
    }
    
    public void onAnimationUpdate(ValueAnimator param1ValueAnimator) {
      PinchImageView pinchImageView = PinchImageView.this;
      float[] arrayOfFloat2 = this.mVector;
      boolean bool = pinchImageView.scrollBy(arrayOfFloat2[0], arrayOfFloat2[1]);
      float[] arrayOfFloat1 = this.mVector;
      arrayOfFloat1[0] = arrayOfFloat1[0] * 0.9F;
      arrayOfFloat1[1] = arrayOfFloat1[1] * 0.9F;
      if (!bool || PinchImageView.MathUtils.getDistance(0.0F, 0.0F, arrayOfFloat1[0], arrayOfFloat1[1]) < 1.0F)
        param1ValueAnimator.cancel(); 
    }
  }
  
  private class MaskAnimator extends ValueAnimator implements ValueAnimator.AnimatorUpdateListener {
    private float[] mEnd = new float[4];
    
    private float[] mResult = new float[4];
    
    private float[] mStart = new float[4];
    
    public MaskAnimator(RectF param1RectF1, RectF param1RectF2, long param1Long) {
      setFloatValues(new float[] { 0.0F, 1.0F });
      setDuration(param1Long);
      addUpdateListener(this);
      this.mStart[0] = param1RectF1.left;
      this.mStart[1] = param1RectF1.top;
      this.mStart[2] = param1RectF1.right;
      this.mStart[3] = param1RectF1.bottom;
      this.mEnd[0] = param1RectF2.left;
      this.mEnd[1] = param1RectF2.top;
      this.mEnd[2] = param1RectF2.right;
      this.mEnd[3] = param1RectF2.bottom;
    }
    
    public void onAnimationUpdate(ValueAnimator param1ValueAnimator) {
      float f = ((Float)param1ValueAnimator.getAnimatedValue()).floatValue();
      for (byte b = 0; b < 4; b++) {
        float[] arrayOfFloat1 = this.mResult;
        float[] arrayOfFloat2 = this.mStart;
        arrayOfFloat1[b] = arrayOfFloat2[b] + (this.mEnd[b] - arrayOfFloat2[b]) * f;
      } 
      if (PinchImageView.this.mMask == null)
        PinchImageView.access$002(PinchImageView.this, new RectF()); 
      RectF rectF = PinchImageView.this.mMask;
      float[] arrayOfFloat = this.mResult;
      rectF.set(arrayOfFloat[0], arrayOfFloat[1], arrayOfFloat[2], arrayOfFloat[3]);
      PinchImageView.this.invalidate();
    }
  }
  
  public static class MathUtils {
    private static PinchImageView.MatrixPool mMatrixPool = new PinchImageView.MatrixPool(16);
    
    private static PinchImageView.RectFPool mRectFPool = new PinchImageView.RectFPool(16);
    
    public static void calculateRectTranslateMatrix(RectF param1RectF1, RectF param1RectF2, Matrix param1Matrix) {
      if (param1RectF1 != null && param1RectF2 != null && param1Matrix != null && param1RectF1.width() != 0.0F && param1RectF1.height() != 0.0F) {
        param1Matrix.reset();
        param1Matrix.postTranslate(-param1RectF1.left, -param1RectF1.top);
        param1Matrix.postScale(param1RectF2.width() / param1RectF1.width(), param1RectF2.height() / param1RectF1.height());
        param1Matrix.postTranslate(param1RectF2.left, param1RectF2.top);
      } 
    }
    
    public static void calculateScaledRectInContainer(RectF param1RectF1, float param1Float1, float param1Float2, ImageView.ScaleType param1ScaleType, RectF param1RectF2) {
      if (param1RectF1 != null && param1RectF2 != null) {
        float f = 0.0F;
        if (param1Float1 != 0.0F && param1Float2 != 0.0F) {
          ImageView.ScaleType scaleType = param1ScaleType;
          if (param1ScaleType == null)
            scaleType = ImageView.ScaleType.FIT_CENTER; 
          param1RectF2.setEmpty();
          if (ImageView.ScaleType.FIT_XY.equals(scaleType)) {
            param1RectF2.set(param1RectF1);
          } else {
            RectF rectF;
            if (ImageView.ScaleType.CENTER.equals(scaleType)) {
              Matrix matrix = matrixTake();
              rectF = rectFTake(0.0F, 0.0F, param1Float1, param1Float2);
              matrix.setTranslate((param1RectF1.width() - param1Float1) * 0.5F, (param1RectF1.height() - param1Float2) * 0.5F);
              matrix.mapRect(param1RectF2, rectF);
              rectFGiven(rectF);
              matrixGiven(matrix);
              param1RectF2.left += param1RectF1.left;
              param1RectF2.right += param1RectF1.left;
              param1RectF2.top += param1RectF1.top;
              param1RectF2.bottom += param1RectF1.top;
            } else if (ImageView.ScaleType.CENTER_CROP.equals(rectF)) {
              float f1;
              Matrix matrix = matrixTake();
              rectF = rectFTake(0.0F, 0.0F, param1Float1, param1Float2);
              if (param1RectF1.height() * param1Float1 > param1RectF1.width() * param1Float2) {
                f1 = param1RectF1.height() / param1Float2;
                param1Float2 = (param1RectF1.width() - param1Float1 * f1) * 0.5F;
                param1Float1 = f;
              } else {
                f1 = param1RectF1.width() / param1Float1;
                param1Float1 = (param1RectF1.height() - param1Float2 * f1) * 0.5F;
                param1Float2 = 0.0F;
              } 
              matrix.setScale(f1, f1);
              matrix.postTranslate(param1Float2, param1Float1);
              matrix.mapRect(param1RectF2, rectF);
              rectFGiven(rectF);
              matrixGiven(matrix);
              param1RectF2.left += param1RectF1.left;
              param1RectF2.right += param1RectF1.left;
              param1RectF2.top += param1RectF1.top;
              param1RectF2.bottom += param1RectF1.top;
            } else {
              Matrix matrix;
              if (ImageView.ScaleType.CENTER_INSIDE.equals(rectF)) {
                float f1;
                matrix = matrixTake();
                RectF rectF1 = rectFTake(0.0F, 0.0F, param1Float1, param1Float2);
                if (param1Float1 <= param1RectF1.width() && param1Float2 <= param1RectF1.height()) {
                  f1 = 1.0F;
                } else {
                  f1 = Math.min(param1RectF1.width() / param1Float1, param1RectF1.height() / param1Float2);
                } 
                float f2 = param1RectF1.width();
                f = param1RectF1.height();
                matrix.setScale(f1, f1);
                matrix.postTranslate((f2 - param1Float1 * f1) * 0.5F, (f - param1Float2 * f1) * 0.5F);
                matrix.mapRect(param1RectF2, rectF1);
                rectFGiven(rectF1);
                matrixGiven(matrix);
                param1RectF2.left += param1RectF1.left;
                param1RectF2.right += param1RectF1.left;
                param1RectF2.top += param1RectF1.top;
                param1RectF2.bottom += param1RectF1.top;
              } else if (ImageView.ScaleType.FIT_CENTER.equals(matrix)) {
                matrix = matrixTake();
                RectF rectF2 = rectFTake(0.0F, 0.0F, param1Float1, param1Float2);
                RectF rectF1 = rectFTake(0.0F, 0.0F, param1Float1, param1Float2);
                RectF rectF3 = rectFTake(0.0F, 0.0F, param1RectF1.width(), param1RectF1.height());
                matrix.setRectToRect(rectF1, rectF3, Matrix.ScaleToFit.CENTER);
                matrix.mapRect(param1RectF2, rectF2);
                rectFGiven(rectF3);
                rectFGiven(rectF1);
                rectFGiven(rectF2);
                matrixGiven(matrix);
                param1RectF2.left += param1RectF1.left;
                param1RectF2.right += param1RectF1.left;
                param1RectF2.top += param1RectF1.top;
                param1RectF2.bottom += param1RectF1.top;
              } else {
                RectF rectF1;
                if (ImageView.ScaleType.FIT_START.equals(matrix)) {
                  Matrix matrix1 = matrixTake();
                  rectF1 = rectFTake(0.0F, 0.0F, param1Float1, param1Float2);
                  RectF rectF3 = rectFTake(0.0F, 0.0F, param1Float1, param1Float2);
                  RectF rectF2 = rectFTake(0.0F, 0.0F, param1RectF1.width(), param1RectF1.height());
                  matrix1.setRectToRect(rectF3, rectF2, Matrix.ScaleToFit.START);
                  matrix1.mapRect(param1RectF2, rectF1);
                  rectFGiven(rectF2);
                  rectFGiven(rectF3);
                  rectFGiven(rectF1);
                  matrixGiven(matrix1);
                  param1RectF2.left += param1RectF1.left;
                  param1RectF2.right += param1RectF1.left;
                  param1RectF2.top += param1RectF1.top;
                  param1RectF2.bottom += param1RectF1.top;
                } else if (ImageView.ScaleType.FIT_END.equals(rectF1)) {
                  Matrix matrix1 = matrixTake();
                  RectF rectF2 = rectFTake(0.0F, 0.0F, param1Float1, param1Float2);
                  RectF rectF3 = rectFTake(0.0F, 0.0F, param1Float1, param1Float2);
                  RectF rectF4 = rectFTake(0.0F, 0.0F, param1RectF1.width(), param1RectF1.height());
                  matrix1.setRectToRect(rectF3, rectF4, Matrix.ScaleToFit.END);
                  matrix1.mapRect(param1RectF2, rectF2);
                  rectFGiven(rectF4);
                  rectFGiven(rectF3);
                  rectFGiven(rectF2);
                  matrixGiven(matrix1);
                  param1RectF2.left += param1RectF1.left;
                  param1RectF2.right += param1RectF1.left;
                  param1RectF2.top += param1RectF1.top;
                  param1RectF2.bottom += param1RectF1.top;
                } else {
                  param1RectF2.set(param1RectF1);
                } 
              } 
            } 
          } 
        } 
      } 
    }
    
    public static float[] getCenterPoint(float param1Float1, float param1Float2, float param1Float3, float param1Float4) {
      return new float[] { (param1Float1 + param1Float3) / 2.0F, (param1Float2 + param1Float4) / 2.0F };
    }
    
    public static float getDistance(float param1Float1, float param1Float2, float param1Float3, float param1Float4) {
      param1Float1 -= param1Float3;
      param1Float2 -= param1Float4;
      return (float)Math.sqrt((param1Float1 * param1Float1 + param1Float2 * param1Float2));
    }
    
    public static float[] getMatrixScale(Matrix param1Matrix) {
      if (param1Matrix != null) {
        float[] arrayOfFloat = new float[9];
        param1Matrix.getValues(arrayOfFloat);
        return new float[] { arrayOfFloat[0], arrayOfFloat[4] };
      } 
      return new float[2];
    }
    
    public static float[] inverseMatrixPoint(float[] param1ArrayOffloat, Matrix param1Matrix) {
      if (param1ArrayOffloat != null && param1Matrix != null) {
        float[] arrayOfFloat = new float[2];
        Matrix matrix = matrixTake();
        param1Matrix.invert(matrix);
        matrix.mapPoints(arrayOfFloat, param1ArrayOffloat);
        matrixGiven(matrix);
        return arrayOfFloat;
      } 
      return new float[2];
    }
    
    public static void matrixGiven(Matrix param1Matrix) {
      mMatrixPool.given(param1Matrix);
    }
    
    public static Matrix matrixTake() {
      return mMatrixPool.take();
    }
    
    public static Matrix matrixTake(Matrix param1Matrix) {
      Matrix matrix = mMatrixPool.take();
      if (param1Matrix != null)
        matrix.set(param1Matrix); 
      return matrix;
    }
    
    public static void rectFGiven(RectF param1RectF) {
      mRectFPool.given(param1RectF);
    }
    
    public static RectF rectFTake() {
      return mRectFPool.take();
    }
    
    public static RectF rectFTake(float param1Float1, float param1Float2, float param1Float3, float param1Float4) {
      RectF rectF = mRectFPool.take();
      rectF.set(param1Float1, param1Float2, param1Float3, param1Float4);
      return rectF;
    }
    
    public static RectF rectFTake(RectF param1RectF) {
      RectF rectF = mRectFPool.take();
      if (param1RectF != null)
        rectF.set(param1RectF); 
      return rectF;
    }
  }
  
  private static class MatrixPool extends ObjectsPool<Matrix> {
    public MatrixPool(int param1Int) {
      super(param1Int);
    }
    
    protected Matrix newInstance() {
      return new Matrix();
    }
    
    protected Matrix resetInstance(Matrix param1Matrix) {
      param1Matrix.reset();
      return param1Matrix;
    }
  }
  
  private static abstract class ObjectsPool<T> {
    private Queue<T> mQueue;
    
    private int mSize;
    
    public ObjectsPool(int param1Int) {
      this.mSize = param1Int;
      this.mQueue = new LinkedList<T>();
    }
    
    public void given(T param1T) {
      if (param1T != null && this.mQueue.size() < this.mSize)
        this.mQueue.offer(param1T); 
    }
    
    protected abstract T newInstance();
    
    protected abstract T resetInstance(T param1T);
    
    public T take() {
      return (this.mQueue.size() == 0) ? newInstance() : resetInstance(this.mQueue.poll());
    }
  }
  
  public static interface OuterMatrixChangedListener {
    void onOuterMatrixChanged(PinchImageView param1PinchImageView);
  }
  
  private static class RectFPool extends ObjectsPool<RectF> {
    public RectFPool(int param1Int) {
      super(param1Int);
    }
    
    protected RectF newInstance() {
      return new RectF();
    }
    
    protected RectF resetInstance(RectF param1RectF) {
      param1RectF.setEmpty();
      return param1RectF;
    }
  }
  
  private class ScaleAnimator extends ValueAnimator implements ValueAnimator.AnimatorUpdateListener {
    private float[] mEnd = new float[9];
    
    private float[] mResult = new float[9];
    
    private float[] mStart = new float[9];
    
    public ScaleAnimator(Matrix param1Matrix1, Matrix param1Matrix2) {
      this(param1Matrix1, param1Matrix2, 200L);
    }
    
    public ScaleAnimator(Matrix param1Matrix1, Matrix param1Matrix2, long param1Long) {
      setFloatValues(new float[] { 0.0F, 1.0F });
      setDuration(param1Long);
      addUpdateListener(this);
      param1Matrix1.getValues(this.mStart);
      param1Matrix2.getValues(this.mEnd);
    }
    
    public void onAnimationUpdate(ValueAnimator param1ValueAnimator) {
      float f = ((Float)param1ValueAnimator.getAnimatedValue()).floatValue();
      for (byte b = 0; b < 9; b++) {
        float[] arrayOfFloat2 = this.mResult;
        float[] arrayOfFloat1 = this.mStart;
        arrayOfFloat2[b] = arrayOfFloat1[b] + (this.mEnd[b] - arrayOfFloat1[b]) * f;
      } 
      PinchImageView.this.mOuterMatrix.setValues(this.mResult);
      PinchImageView.this.dispatchOuterMatrixChanged();
      PinchImageView.this.invalidate();
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/drone/photo_activity/PinchImageView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */