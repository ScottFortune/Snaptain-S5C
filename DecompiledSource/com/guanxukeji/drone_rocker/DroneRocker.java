package com.guanxukeji.drone_rocker;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class DroneRocker extends FrameLayout {
  public static final int SENSOR_TOUCH_EVENT = -10086;
  
  private boolean fingersAreDown = false;
  
  private float fromX = 0.0F;
  
  private float fromY = 0.0F;
  
  private boolean isAutoHide = false;
  
  private boolean isFixedHighMode = false;
  
  private RockerChangeListener listener;
  
  private ImageView mRockerBackground;
  
  private ImageView mRockerMidpoint;
  
  private float midpointDrawRangeOffset_a = 0.0F;
  
  private float midpointDrawRangeOffset_b = 0.0F;
  
  public DroneRocker(Context paramContext) {
    super(paramContext);
    init((AttributeSet)null);
  }
  
  public DroneRocker(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    init(paramAttributeSet);
  }
  
  public DroneRocker(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    init(paramAttributeSet);
  }
  
  public DroneRocker(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2) {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    init(paramAttributeSet);
  }
  
  private void init(AttributeSet paramAttributeSet) {
    inflate(getContext(), R.layout.rocker_panel, (ViewGroup)this);
    this.mRockerBackground = (ImageView)findViewById(R.id.rocker_background);
    this.mRockerMidpoint = (ImageView)findViewById(R.id.rocker_midpoint);
    if (paramAttributeSet != null) {
      TypedArray typedArray = getContext().obtainStyledAttributes(paramAttributeSet, R.styleable.DroneRocker);
      Drawable drawable = typedArray.getDrawable(R.styleable.DroneRocker_drone_rocker_background);
      if (drawable != null)
        this.mRockerBackground.setImageDrawable(drawable); 
      typedArray.recycle();
    } 
  }
  
  public float getMidpointDrawRangeOffset_a() {
    return this.midpointDrawRangeOffset_a;
  }
  
  public float getMidpointDrawRangeOffset_b() {
    return this.midpointDrawRangeOffset_b;
  }
  
  public boolean isFixedHighMode() {
    return this.isFixedHighMode;
  }
  
  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent) {
    super.onInterceptTouchEvent(paramMotionEvent);
    return true;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    setFixedHighMode(this.isFixedHighMode);
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    // Byte code:
    //   0: aload_1
    //   1: invokevirtual getSource : ()I
    //   4: sipush #-10086
    //   7: if_icmpne -> 19
    //   10: aload_0
    //   11: getfield fingersAreDown : Z
    //   14: ifeq -> 19
    //   17: iconst_0
    //   18: ireturn
    //   19: aload_1
    //   20: invokevirtual getX : ()F
    //   23: aload_0
    //   24: getfield mRockerMidpoint : Landroid/widget/ImageView;
    //   27: invokevirtual getX : ()F
    //   30: fsub
    //   31: aload_0
    //   32: getfield mRockerMidpoint : Landroid/widget/ImageView;
    //   35: invokevirtual getWidth : ()I
    //   38: iconst_2
    //   39: idiv
    //   40: i2f
    //   41: fsub
    //   42: f2i
    //   43: i2f
    //   44: fstore_2
    //   45: aload_1
    //   46: invokevirtual getY : ()F
    //   49: aload_0
    //   50: getfield mRockerMidpoint : Landroid/widget/ImageView;
    //   53: invokevirtual getY : ()F
    //   56: fsub
    //   57: aload_0
    //   58: getfield mRockerMidpoint : Landroid/widget/ImageView;
    //   61: invokevirtual getHeight : ()I
    //   64: iconst_2
    //   65: idiv
    //   66: i2f
    //   67: fsub
    //   68: f2i
    //   69: i2f
    //   70: fstore_3
    //   71: aload_0
    //   72: invokevirtual getWidth : ()I
    //   75: aload_0
    //   76: invokevirtual getPaddingLeft : ()I
    //   79: isub
    //   80: aload_0
    //   81: invokevirtual getPaddingRight : ()I
    //   84: isub
    //   85: aload_0
    //   86: getfield mRockerMidpoint : Landroid/widget/ImageView;
    //   89: invokevirtual getWidth : ()I
    //   92: isub
    //   93: iconst_2
    //   94: idiv
    //   95: i2f
    //   96: aload_0
    //   97: getfield midpointDrawRangeOffset_a : F
    //   100: fadd
    //   101: fstore #4
    //   103: aload_0
    //   104: invokevirtual getHeight : ()I
    //   107: aload_0
    //   108: invokevirtual getPaddingTop : ()I
    //   111: isub
    //   112: aload_0
    //   113: invokevirtual getPaddingBottom : ()I
    //   116: isub
    //   117: aload_0
    //   118: getfield mRockerMidpoint : Landroid/widget/ImageView;
    //   121: invokevirtual getHeight : ()I
    //   124: isub
    //   125: iconst_2
    //   126: idiv
    //   127: i2f
    //   128: aload_0
    //   129: getfield midpointDrawRangeOffset_b : F
    //   132: fadd
    //   133: fstore #5
    //   135: fload #4
    //   137: fload #4
    //   139: fmul
    //   140: fstore #6
    //   142: fload_2
    //   143: fload_2
    //   144: fmul
    //   145: fload #6
    //   147: fdiv
    //   148: fstore #7
    //   150: fload #5
    //   152: fload #5
    //   154: fmul
    //   155: fstore #8
    //   157: fload_3
    //   158: fload_3
    //   159: fmul
    //   160: fload #8
    //   162: fdiv
    //   163: fstore #9
    //   165: fconst_0
    //   166: fstore #10
    //   168: fload #7
    //   170: fload #9
    //   172: fadd
    //   173: fconst_1
    //   174: fcmpl
    //   175: ifle -> 397
    //   178: fload_2
    //   179: fconst_0
    //   180: fcmpl
    //   181: ifne -> 221
    //   184: fload_3
    //   185: fload #5
    //   187: fsub
    //   188: invokestatic abs : (F)F
    //   191: fload_3
    //   192: fload #5
    //   194: fadd
    //   195: invokestatic abs : (F)F
    //   198: fcmpg
    //   199: ifge -> 211
    //   202: fload #5
    //   204: fstore_3
    //   205: fload #10
    //   207: fstore_2
    //   208: goto -> 397
    //   211: fload #5
    //   213: fneg
    //   214: fstore_3
    //   215: fload #10
    //   217: fstore_2
    //   218: goto -> 397
    //   221: fload #6
    //   223: fload #5
    //   225: fmul
    //   226: fload #5
    //   228: fmul
    //   229: f2d
    //   230: dstore #11
    //   232: fload #8
    //   234: f2d
    //   235: dstore #13
    //   237: fload_3
    //   238: fload_2
    //   239: fdiv
    //   240: fstore #8
    //   242: fload #8
    //   244: f2d
    //   245: dstore #15
    //   247: dload #15
    //   249: ldc2_w 2.0
    //   252: invokestatic pow : (DD)D
    //   255: dstore #17
    //   257: fload #4
    //   259: f2d
    //   260: dstore #19
    //   262: dload #19
    //   264: invokestatic isNaN : (D)Z
    //   267: pop
    //   268: dload #19
    //   270: invokestatic isNaN : (D)Z
    //   273: pop
    //   274: dload #13
    //   276: invokestatic isNaN : (D)Z
    //   279: pop
    //   280: dload #11
    //   282: invokestatic isNaN : (D)Z
    //   285: pop
    //   286: dload #11
    //   288: dload #17
    //   290: dload #19
    //   292: dmul
    //   293: dload #19
    //   295: dmul
    //   296: dload #13
    //   298: dadd
    //   299: ddiv
    //   300: invokestatic sqrt : (D)D
    //   303: d2f
    //   304: fstore #10
    //   306: dload #15
    //   308: ldc2_w 2.0
    //   311: invokestatic pow : (DD)D
    //   314: dstore #15
    //   316: dload #19
    //   318: invokestatic isNaN : (D)Z
    //   321: pop
    //   322: dload #19
    //   324: invokestatic isNaN : (D)Z
    //   327: pop
    //   328: dload #13
    //   330: invokestatic isNaN : (D)Z
    //   333: pop
    //   334: dload #11
    //   336: invokestatic isNaN : (D)Z
    //   339: pop
    //   340: dload #11
    //   342: dload #13
    //   344: dload #15
    //   346: dload #19
    //   348: dmul
    //   349: dload #19
    //   351: dmul
    //   352: dadd
    //   353: ddiv
    //   354: invokestatic sqrt : (D)D
    //   357: dneg
    //   358: d2f
    //   359: fstore_3
    //   360: fload_2
    //   361: fload #10
    //   363: fsub
    //   364: invokestatic abs : (F)F
    //   367: fload_2
    //   368: fload_3
    //   369: fsub
    //   370: invokestatic abs : (F)F
    //   373: fcmpg
    //   374: ifge -> 383
    //   377: fload #10
    //   379: fstore_3
    //   380: goto -> 383
    //   383: fload #8
    //   385: fload_3
    //   386: fmul
    //   387: fstore #10
    //   389: fload_3
    //   390: fstore_2
    //   391: fload #10
    //   393: fstore_3
    //   394: goto -> 397
    //   397: aload_1
    //   398: invokevirtual getAction : ()I
    //   401: istore #21
    //   403: iload #21
    //   405: ifeq -> 565
    //   408: iload #21
    //   410: iconst_1
    //   411: if_icmpeq -> 475
    //   414: iload #21
    //   416: iconst_2
    //   417: if_icmpeq -> 429
    //   420: iload #21
    //   422: iconst_3
    //   423: if_icmpeq -> 475
    //   426: goto -> 621
    //   429: aload_0
    //   430: getfield mRockerMidpoint : Landroid/widget/ImageView;
    //   433: invokevirtual clearAnimation : ()V
    //   436: new android/view/animation/TranslateAnimation
    //   439: dup
    //   440: aload_0
    //   441: getfield fromX : F
    //   444: fload_2
    //   445: aload_0
    //   446: getfield fromY : F
    //   449: fload_3
    //   450: invokespecial <init> : (FFFF)V
    //   453: astore_1
    //   454: aload_1
    //   455: lconst_1
    //   456: invokevirtual setDuration : (J)V
    //   459: aload_1
    //   460: iconst_1
    //   461: invokevirtual setFillAfter : (Z)V
    //   464: aload_0
    //   465: getfield mRockerMidpoint : Landroid/widget/ImageView;
    //   468: aload_1
    //   469: invokevirtual startAnimation : (Landroid/view/animation/Animation;)V
    //   472: goto -> 426
    //   475: aload_1
    //   476: invokevirtual getSource : ()I
    //   479: sipush #-10086
    //   482: if_icmpeq -> 490
    //   485: aload_0
    //   486: iconst_0
    //   487: putfield fingersAreDown : Z
    //   490: aload_0
    //   491: getfield isFixedHighMode : Z
    //   494: ifne -> 499
    //   497: fconst_0
    //   498: fstore_3
    //   499: new android/view/animation/TranslateAnimation
    //   502: dup
    //   503: aload_0
    //   504: getfield fromX : F
    //   507: fconst_0
    //   508: aload_0
    //   509: getfield fromY : F
    //   512: fload_3
    //   513: invokespecial <init> : (FFFF)V
    //   516: astore_1
    //   517: aload_1
    //   518: ldc2_w 200
    //   521: invokevirtual setDuration : (J)V
    //   524: aload_1
    //   525: new android/view/animation/OvershootInterpolator
    //   528: dup
    //   529: invokespecial <init> : ()V
    //   532: invokevirtual setInterpolator : (Landroid/view/animation/Interpolator;)V
    //   535: aload_1
    //   536: iconst_1
    //   537: invokevirtual setFillAfter : (Z)V
    //   540: aload_0
    //   541: getfield mRockerMidpoint : Landroid/widget/ImageView;
    //   544: aload_1
    //   545: invokevirtual startAnimation : (Landroid/view/animation/Animation;)V
    //   548: aload_0
    //   549: getfield isAutoHide : Z
    //   552: ifeq -> 560
    //   555: aload_0
    //   556: fconst_0
    //   557: invokevirtual setAlpha : (F)V
    //   560: fconst_0
    //   561: fstore_2
    //   562: goto -> 621
    //   565: aload_1
    //   566: invokevirtual getSource : ()I
    //   569: sipush #-10086
    //   572: if_icmpeq -> 580
    //   575: aload_0
    //   576: iconst_1
    //   577: putfield fingersAreDown : Z
    //   580: new android/view/animation/TranslateAnimation
    //   583: dup
    //   584: aload_0
    //   585: getfield fromX : F
    //   588: fload_2
    //   589: aload_0
    //   590: getfield fromY : F
    //   593: fload_3
    //   594: invokespecial <init> : (FFFF)V
    //   597: astore_1
    //   598: aload_1
    //   599: lconst_0
    //   600: invokevirtual setDuration : (J)V
    //   603: aload_1
    //   604: iconst_1
    //   605: invokevirtual setFillAfter : (Z)V
    //   608: aload_0
    //   609: getfield mRockerMidpoint : Landroid/widget/ImageView;
    //   612: aload_1
    //   613: invokevirtual startAnimation : (Landroid/view/animation/Animation;)V
    //   616: aload_0
    //   617: fconst_1
    //   618: invokevirtual setAlpha : (F)V
    //   621: aload_0
    //   622: getfield listener : Lcom/guanxukeji/drone_rocker/DroneRocker$RockerChangeListener;
    //   625: ifnull -> 706
    //   628: fload_2
    //   629: fload #4
    //   631: fdiv
    //   632: ldc 100.0
    //   634: fmul
    //   635: fstore #10
    //   637: fload_3
    //   638: fneg
    //   639: fload #5
    //   641: fdiv
    //   642: ldc 100.0
    //   644: fmul
    //   645: fstore #4
    //   647: fload #10
    //   649: ldc 100.0
    //   651: fcmpl
    //   652: ifgt -> 667
    //   655: fload #10
    //   657: fstore #5
    //   659: fload #10
    //   661: ldc -100.0
    //   663: fcmpg
    //   664: ifge -> 670
    //   667: fconst_0
    //   668: fstore #5
    //   670: fload #4
    //   672: ldc 100.0
    //   674: fcmpl
    //   675: ifgt -> 690
    //   678: fload #4
    //   680: fstore #10
    //   682: fload #4
    //   684: ldc -100.0
    //   686: fcmpg
    //   687: ifge -> 693
    //   690: fconst_0
    //   691: fstore #10
    //   693: aload_0
    //   694: getfield listener : Lcom/guanxukeji/drone_rocker/DroneRocker$RockerChangeListener;
    //   697: fload #5
    //   699: fload #10
    //   701: invokeinterface onMidpointChange : (FF)V
    //   706: aload_0
    //   707: fload_2
    //   708: putfield fromX : F
    //   711: aload_0
    //   712: fload_3
    //   713: putfield fromY : F
    //   716: iconst_1
    //   717: ireturn
  }
  
  public void setAutoHide(boolean paramBoolean) {
    this.isAutoHide = paramBoolean;
    if (paramBoolean) {
      setAlpha(0.0F);
    } else {
      setAlpha(1.0F);
    } 
  }
  
  public void setFixedHighMode(boolean paramBoolean) {
    this.isFixedHighMode = paramBoolean;
    MotionEvent motionEvent = MotionEvent.obtain(System.currentTimeMillis(), System.currentTimeMillis(), 3, (getWidth() / 2), getHeight(), 0);
    if (!paramBoolean)
      motionEvent = MotionEvent.obtain(System.currentTimeMillis(), System.currentTimeMillis(), 3, (getWidth() / 2), (getHeight() / 2), 0); 
    motionEvent.setSource(-10086);
    onTouchEvent(motionEvent);
  }
  
  public void setListener(RockerChangeListener paramRockerChangeListener) {
    this.listener = paramRockerChangeListener;
  }
  
  public void setMidpoint(int paramInt) {
    this.mRockerBackground.setImageResource(paramInt);
  }
  
  public void setMidpoint(Drawable paramDrawable) {
    this.mRockerBackground.setImageDrawable(paramDrawable);
  }
  
  public void setMidpointDrawRangeOffset_a(float paramFloat) {
    this.midpointDrawRangeOffset_a = paramFloat;
  }
  
  public void setMidpointDrawRangeOffset_b(float paramFloat) {
    this.midpointDrawRangeOffset_b = paramFloat;
  }
  
  public void setRockerBackground(int paramInt) {
    this.mRockerBackground.setImageResource(paramInt);
  }
  
  public void setRockerBackground(Drawable paramDrawable) {
    this.mRockerBackground.setImageDrawable(paramDrawable);
  }
  
  protected static interface RockerChangeListener {
    void onMidpointChange(float param1Float1, float param1Float2);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/guanxukeji/drone_rocker/DroneRocker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */