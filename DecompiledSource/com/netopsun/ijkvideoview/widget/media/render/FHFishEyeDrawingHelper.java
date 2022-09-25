package com.netopsun.ijkvideoview.widget.media.render;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import com.fh.lib.FHSDK;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class FHFishEyeDrawingHelper {
  private static final int DISPLAY_TYPE_RGB = 1;
  
  private static final float H_OFFSET_BASE = 2000.0F;
  
  private static final float STEP_BASE_FAST = 200.0F;
  
  private static final float STEP_BASE_SLOW = 100.0F;
  
  private static final float STEP_OFFSET = 3.0F;
  
  public static float angle = 90.0F;
  
  public static boolean bMixMode = false;
  
  public static boolean bSnapshot = false;
  
  private static boolean bUpdated = false;
  
  public static float circleR = 0.0F;
  
  public static float circleX = 0.0F;
  
  public static float circleY = 0.0F;
  
  public static int ctrlIndex = 0;
  
  public static int curIndex = 0;
  
  public static float depth = 0.0F;
  
  public static int displayMode = 0;
  
  public static int eyeMode = 0;
  
  public static long hBuffer = 0L;
  
  public static float hDegrees = 0.0F;
  
  public static float[] hEyeDegrees;
  
  public static float hOffset = 0.0F;
  
  public static long hWin = 0L;
  
  public static long[] hWinMixMode;
  
  private static FHFishEyeDrawingHelper instance;
  
  public static final boolean isDebugMode = false;
  
  public static boolean isDoubleClick;
  
  public static boolean isZoomIn;
  
  public static int mDrawHeight;
  
  public static int mDrawWidth;
  
  public static RGBRes[] mRgbRes = new RGBRes[24];
  
  public static int mScreenHeight;
  
  public static int mScreenWidth;
  
  public static int modeOffset;
  
  public static boolean resChanged;
  
  public static int rgbResIndex;
  
  public static float scrollStep;
  
  public static float vDegrees;
  
  private static float velocityX;
  
  private static float velocityY;
  
  private final float MAX_DEPTH = 0.0F;
  
  private final String TAG = FHFishEyeDrawingHelper.class.getSimpleName();
  
  private boolean bSurfaceChanged = false;
  
  private boolean bSurfaceCreate = false;
  
  private int drawCount = 0;
  
  private byte[] frameBuf = null;
  
  private boolean isBucketMode = true;
  
  private boolean isNormalVRMode = false;
  
  private float lastDepth = -1.0F;
  
  private float lastHDegrees = -1.0F;
  
  private float lastHOffset = -1.0F;
  
  private int lastShowMode = -1;
  
  private float lastVDegrees = -1.0F;
  
  private Context mContext;
  
  private Handler mHandler = null;
  
  private SnapshotThread mSnapshotThread;
  
  private int oldTextureID;
  
  Runnable requestRender = new Runnable() {
      public void run() {
        FHFishEyeDrawingHelper.this.updateParams();
        if (FHFishEyeDrawingHelper.this.mHandler != null && FHFishEyeDrawingHelper.this.requestRender != null)
          FHFishEyeDrawingHelper.this.mHandler.postDelayed(FHFishEyeDrawingHelper.this.requestRender, 25L); 
      }
    };
  
  private byte[] rgb;
  
  private int rotateAngle = 0;
  
  Runnable scaleView = new Runnable() {
      public void run() {
        Log.e(FHFishEyeDrawingHelper.this.TAG, "scaleView");
        float f1 = Math.abs((FHSDK.getMaxVDegress(FHFishEyeDrawingHelper.hWin) - FHSDK.getMinVDegress(FHFishEyeDrawingHelper.hWin)) / 2.0F);
        float f2 = Math.abs(FHSDK.getMaxZDepth(FHFishEyeDrawingHelper.hWin) / 2.0F);
        if (FHFishEyeDrawingHelper.isZoomIn) {
          FHFishEyeDrawingHelper.depth += f2;
        } else {
          FHFishEyeDrawingHelper.vDegrees += f1 * 4.0F;
          FHFishEyeDrawingHelper.depth -= f2 * 4.0F;
        } 
        if (FHFishEyeDrawingHelper.vDegrees < FHSDK.getMaxVDegress(FHFishEyeDrawingHelper.hWin)) {
          FHFishEyeDrawingHelper.vDegrees = FHSDK.getMaxVDegress(FHFishEyeDrawingHelper.hWin);
        } else if (FHFishEyeDrawingHelper.vDegrees > FHSDK.getMinVDegress(FHFishEyeDrawingHelper.hWin)) {
          FHFishEyeDrawingHelper.vDegrees = FHSDK.getMinVDegress(FHFishEyeDrawingHelper.hWin);
        } 
        if (FHFishEyeDrawingHelper.depth < FHSDK.getMaxZDepth(FHFishEyeDrawingHelper.hWin)) {
          FHFishEyeDrawingHelper.depth = FHSDK.getMaxZDepth(FHFishEyeDrawingHelper.hWin);
        } else if (FHFishEyeDrawingHelper.depth > 0.0F) {
          FHFishEyeDrawingHelper.depth = 0.0F;
        } 
        if ((FHFishEyeDrawingHelper.isZoomIn && FHFishEyeDrawingHelper.depth != 0.0F) || (!FHFishEyeDrawingHelper.isZoomIn && FHFishEyeDrawingHelper.depth != FHSDK.getMaxZDepth(FHFishEyeDrawingHelper.hWin)))
          FHFishEyeDrawingHelper.this.mHandler.postDelayed(FHFishEyeDrawingHelper.this.scaleView, 40L); 
      }
    };
  
  private int view_h;
  
  private int view_w;
  
  private int view_x;
  
  private int view_y;
  
  static {
    hEyeDegrees = new float[] { 0.0F, 90.0F, 180.0F, 270.0F };
    curIndex = 0;
    isDoubleClick = false;
    isZoomIn = false;
    hWin = 0L;
    hBuffer = 0L;
    hWinMixMode = new long[4];
    bUpdated = true;
    bMixMode = false;
    ctrlIndex = 0;
    resChanged = false;
  }
  
  public FHFishEyeDrawingHelper() {
    init();
  }
  
  public FHFishEyeDrawingHelper(Context paramContext, DisplayMetrics paramDisplayMetrics) {
    this.mContext = paramContext;
    mScreenWidth = paramDisplayMetrics.widthPixels;
    mScreenHeight = paramDisplayMetrics.heightPixels;
  }
  
  private void drawFrame(int paramInt1, int paramInt2, int paramInt3) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield oldTextureID : I
    //   6: ifle -> 47
    //   9: getstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.hBuffer : J
    //   12: bipush #32
    //   14: lshr
    //   15: lconst_0
    //   16: lcmp
    //   17: ifle -> 35
    //   20: getstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.hBuffer : J
    //   23: bipush #28
    //   25: aload_0
    //   26: getfield oldTextureID : I
    //   29: invokestatic setInt : (JII)V
    //   32: goto -> 47
    //   35: getstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.hBuffer : J
    //   38: bipush #20
    //   40: aload_0
    //   41: getfield oldTextureID : I
    //   44: invokestatic setInt : (JII)V
    //   47: getstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.displayMode : I
    //   50: getstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.hWin : J
    //   53: invokestatic getDisplayMode : (J)I
    //   56: if_icmpeq -> 92
    //   59: getstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.hWin : J
    //   62: invokestatic unbind : (J)Z
    //   65: pop
    //   66: getstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.hWin : J
    //   69: invokestatic destroyWindow : (J)Z
    //   72: pop
    //   73: getstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.displayMode : I
    //   76: invokestatic createWindow : (I)J
    //   79: putstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.hWin : J
    //   82: getstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.hWin : J
    //   85: getstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.hBuffer : J
    //   88: invokestatic bind : (JJ)Z
    //   91: pop
    //   92: getstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.hWin : J
    //   95: getstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.circleX : F
    //   98: getstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.circleY : F
    //   101: getstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.circleR : F
    //   104: invokestatic setStandardCircle : (JFFF)Z
    //   107: pop
    //   108: aload_0
    //   109: getfield rgb : [B
    //   112: ifnonnull -> 126
    //   115: aload_0
    //   116: iload_2
    //   117: iload_3
    //   118: imul
    //   119: iconst_3
    //   120: imul
    //   121: newarray byte
    //   123: putfield rgb : [B
    //   126: aload_0
    //   127: getfield rgb : [B
    //   130: ifnull -> 201
    //   133: getstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.bUpdated : Z
    //   136: ifeq -> 201
    //   139: getstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.hBuffer : J
    //   142: aload_0
    //   143: getfield rgb : [B
    //   146: iload_2
    //   147: iload_3
    //   148: invokestatic update : (J[BII)Z
    //   151: pop
    //   152: aload_0
    //   153: getfield oldTextureID : I
    //   156: ifgt -> 197
    //   159: getstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.hBuffer : J
    //   162: bipush #32
    //   164: lshr
    //   165: lconst_0
    //   166: lcmp
    //   167: ifle -> 185
    //   170: aload_0
    //   171: getstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.hBuffer : J
    //   174: bipush #28
    //   176: invokestatic readTextureID : (JI)I
    //   179: putfield oldTextureID : I
    //   182: goto -> 197
    //   185: aload_0
    //   186: getstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.hBuffer : J
    //   189: bipush #20
    //   191: invokestatic readTextureID : (JI)I
    //   194: putfield oldTextureID : I
    //   197: iconst_1
    //   198: putstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.bUpdated : Z
    //   201: aload_0
    //   202: getfield oldTextureID : I
    //   205: ifle -> 240
    //   208: getstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.hBuffer : J
    //   211: bipush #32
    //   213: lshr
    //   214: lconst_0
    //   215: lcmp
    //   216: ifle -> 231
    //   219: getstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.hBuffer : J
    //   222: bipush #28
    //   224: iload_1
    //   225: invokestatic setInt : (JII)V
    //   228: goto -> 240
    //   231: getstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.hBuffer : J
    //   234: bipush #20
    //   236: iload_1
    //   237: invokestatic setInt : (JII)V
    //   240: invokestatic clear : ()Z
    //   243: pop
    //   244: getstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.bMixMode : Z
    //   247: ifeq -> 459
    //   250: aload_0
    //   251: getfield view_x : I
    //   254: istore #4
    //   256: aload_0
    //   257: getfield view_x : I
    //   260: istore #5
    //   262: aload_0
    //   263: getfield view_w : I
    //   266: iconst_2
    //   267: idiv
    //   268: istore_2
    //   269: aload_0
    //   270: getfield view_x : I
    //   273: istore #6
    //   275: aload_0
    //   276: getfield view_x : I
    //   279: istore #7
    //   281: aload_0
    //   282: getfield view_w : I
    //   285: iconst_2
    //   286: idiv
    //   287: istore #8
    //   289: aload_0
    //   290: getfield view_y : I
    //   293: istore #9
    //   295: aload_0
    //   296: getfield view_y : I
    //   299: istore #10
    //   301: aload_0
    //   302: getfield view_y : I
    //   305: istore #11
    //   307: aload_0
    //   308: getfield view_h : I
    //   311: iconst_2
    //   312: idiv
    //   313: istore #12
    //   315: aload_0
    //   316: getfield view_y : I
    //   319: istore #13
    //   321: aload_0
    //   322: getfield view_h : I
    //   325: iconst_2
    //   326: idiv
    //   327: istore_3
    //   328: iconst_0
    //   329: istore_1
    //   330: iload_1
    //   331: iconst_4
    //   332: if_icmpge -> 1442
    //   335: getstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.hWinMixMode : [J
    //   338: iload_1
    //   339: laload
    //   340: getstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.circleX : F
    //   343: getstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.circleY : F
    //   346: getstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.circleR : F
    //   349: invokestatic setStandardCircle : (JFFF)Z
    //   352: pop
    //   353: iconst_4
    //   354: newarray int
    //   356: dup
    //   357: iconst_0
    //   358: iload #4
    //   360: iastore
    //   361: dup
    //   362: iconst_1
    //   363: iload #5
    //   365: iload_2
    //   366: iadd
    //   367: iastore
    //   368: dup
    //   369: iconst_2
    //   370: iload #6
    //   372: iastore
    //   373: dup
    //   374: iconst_3
    //   375: iload #7
    //   377: iload #8
    //   379: iadd
    //   380: iastore
    //   381: iload_1
    //   382: iaload
    //   383: iconst_4
    //   384: newarray int
    //   386: dup
    //   387: iconst_0
    //   388: iload #9
    //   390: iastore
    //   391: dup
    //   392: iconst_1
    //   393: iload #10
    //   395: iastore
    //   396: dup
    //   397: iconst_2
    //   398: iload #11
    //   400: iload #12
    //   402: iadd
    //   403: iastore
    //   404: dup
    //   405: iconst_3
    //   406: iload #13
    //   408: iload_3
    //   409: iadd
    //   410: iastore
    //   411: iload_1
    //   412: iaload
    //   413: aload_0
    //   414: getfield view_w : I
    //   417: iconst_2
    //   418: idiv
    //   419: aload_0
    //   420: getfield view_h : I
    //   423: iconst_2
    //   424: idiv
    //   425: invokestatic viewport : (IIII)Z
    //   428: pop
    //   429: iconst_1
    //   430: iload_1
    //   431: if_icmpne -> 444
    //   434: getstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.hWinMixMode : [J
    //   437: iload_1
    //   438: laload
    //   439: iconst_1
    //   440: invokestatic setImagingType : (JI)Z
    //   443: pop
    //   444: getstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.hWinMixMode : [J
    //   447: iload_1
    //   448: laload
    //   449: invokestatic draw : (J)Z
    //   452: pop
    //   453: iinc #1, 1
    //   456: goto -> 330
    //   459: getstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.displayMode : I
    //   462: ifeq -> 516
    //   465: bipush #6
    //   467: getstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.displayMode : I
    //   470: if_icmpne -> 476
    //   473: goto -> 516
    //   476: aload_0
    //   477: getfield view_x : I
    //   480: aload_0
    //   481: getfield view_y : I
    //   484: aload_0
    //   485: getfield view_w : I
    //   488: aload_0
    //   489: getfield view_h : I
    //   492: invokestatic viewport : (IIII)Z
    //   495: pop
    //   496: getstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.hWin : J
    //   499: getstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.hOffset : F
    //   502: invokestatic expandLookAt : (JF)Z
    //   505: pop
    //   506: getstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.hWin : J
    //   509: invokestatic draw : (J)Z
    //   512: pop
    //   513: goto -> 1442
    //   516: getstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.eyeMode : I
    //   519: ifne -> 908
    //   522: aload_0
    //   523: getfield view_x : I
    //   526: aload_0
    //   527: getfield view_y : I
    //   530: aload_0
    //   531: getfield view_w : I
    //   534: aload_0
    //   535: getfield view_h : I
    //   538: invokestatic viewport : (IIII)Z
    //   541: pop
    //   542: getstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.mScreenWidth : I
    //   545: getstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.mScreenHeight : I
    //   548: if_icmple -> 562
    //   551: getstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.hWin : J
    //   554: invokestatic getViewAngle : (J)F
    //   557: fstore #14
    //   559: goto -> 603
    //   562: ldc_w 114.59156
    //   565: getstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.mScreenHeight : I
    //   568: i2f
    //   569: getstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.mScreenWidth : I
    //   572: i2f
    //   573: fdiv
    //   574: getstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.hWin : J
    //   577: invokestatic getViewAngle : (J)F
    //   580: fconst_2
    //   581: fdiv
    //   582: ldc_w 0.017453292
    //   585: fmul
    //   586: f2d
    //   587: invokestatic tan : (D)D
    //   590: d2f
    //   591: fmul
    //   592: f2d
    //   593: invokestatic atan : (D)D
    //   596: d2f
    //   597: fmul
    //   598: fstore #14
    //   600: goto -> 559
    //   603: aload_0
    //   604: getfield isBucketMode : Z
    //   607: ifeq -> 655
    //   610: getstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.vDegrees : F
    //   613: ldc_w -180.0
    //   616: fcmpg
    //   617: ifge -> 626
    //   620: ldc_w -180.0
    //   623: putstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.vDegrees : F
    //   626: getstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.vDegrees : F
    //   629: fconst_0
    //   630: fcmpl
    //   631: ifle -> 638
    //   634: fconst_0
    //   635: putstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.vDegrees : F
    //   638: fload #14
    //   640: getstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.vDegrees : F
    //   643: invokestatic abs : (F)F
    //   646: ldc 3.0
    //   648: fdiv
    //   649: fadd
    //   650: fstore #15
    //   652: goto -> 709
    //   655: ldc 90.0
    //   657: fload #14
    //   659: fconst_2
    //   660: fdiv
    //   661: fsub
    //   662: fstore #16
    //   664: getstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.vDegrees : F
    //   667: fstore #17
    //   669: fload #16
    //   671: fneg
    //   672: fstore #15
    //   674: fload #17
    //   676: fload #15
    //   678: fcmpg
    //   679: ifge -> 687
    //   682: fload #15
    //   684: putstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.vDegrees : F
    //   687: fload #16
    //   689: fstore #15
    //   691: getstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.vDegrees : F
    //   694: fload #16
    //   696: fcmpl
    //   697: ifle -> 709
    //   700: fload #16
    //   702: putstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.vDegrees : F
    //   705: fload #16
    //   707: fstore #15
    //   709: aload_0
    //   710: getfield isBucketMode : Z
    //   713: ifeq -> 769
    //   716: getstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.hWin : J
    //   719: invokestatic getMaxZDepth : (J)F
    //   722: putstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.depth : F
    //   725: ldc -1.0
    //   727: getstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.depth : F
    //   730: fsub
    //   731: ldc 90.0
    //   733: fdiv
    //   734: fstore #16
    //   736: getstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.hWin : J
    //   739: invokestatic getMaxZDepth : (J)F
    //   742: getstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.vDegrees : F
    //   745: invokestatic abs : (F)F
    //   748: fload #16
    //   750: fmul
    //   751: fadd
    //   752: putstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.depth : F
    //   755: getstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.depth : F
    //   758: ldc -1.0
    //   760: fcmpl
    //   761: ifle -> 769
    //   764: ldc -1.0
    //   766: putstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.depth : F
    //   769: aload_0
    //   770: getfield isBucketMode : Z
    //   773: ifeq -> 804
    //   776: getstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.hWin : J
    //   779: getstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.vDegrees : F
    //   782: getstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.hDegrees : F
    //   785: fneg
    //   786: aload_0
    //   787: getfield rotateAngle : I
    //   790: i2f
    //   791: fadd
    //   792: getstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.depth : F
    //   795: fload #15
    //   797: invokestatic eyeLookAtEx : (JFFFF)Z
    //   800: pop
    //   801: goto -> 823
    //   804: getstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.hWin : J
    //   807: getstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.vDegrees : F
    //   810: getstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.hDegrees : F
    //   813: fneg
    //   814: getstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.depth : F
    //   817: fload #14
    //   819: invokestatic eyeLookAtEx : (JFFFF)Z
    //   822: pop
    //   823: aload_0
    //   824: getfield isNormalVRMode : Z
    //   827: ifeq -> 898
    //   830: iconst_0
    //   831: aload_0
    //   832: getfield view_h : I
    //   835: iconst_4
    //   836: idiv
    //   837: aload_0
    //   838: getfield view_w : I
    //   841: iconst_2
    //   842: idiv
    //   843: aload_0
    //   844: getfield view_h : I
    //   847: iconst_2
    //   848: idiv
    //   849: invokestatic viewport : (IIII)Z
    //   852: pop
    //   853: getstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.hWin : J
    //   856: invokestatic draw : (J)Z
    //   859: pop
    //   860: aload_0
    //   861: getfield view_w : I
    //   864: iconst_2
    //   865: idiv
    //   866: aload_0
    //   867: getfield view_h : I
    //   870: iconst_4
    //   871: idiv
    //   872: aload_0
    //   873: getfield view_w : I
    //   876: iconst_2
    //   877: idiv
    //   878: aload_0
    //   879: getfield view_h : I
    //   882: iconst_2
    //   883: idiv
    //   884: invokestatic viewport : (IIII)Z
    //   887: pop
    //   888: getstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.hWin : J
    //   891: invokestatic draw : (J)Z
    //   894: pop
    //   895: goto -> 1442
    //   898: getstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.hWin : J
    //   901: invokestatic draw : (J)Z
    //   904: pop
    //   905: goto -> 1442
    //   908: iconst_1
    //   909: getstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.eyeMode : I
    //   912: if_icmpne -> 1112
    //   915: aload_0
    //   916: getfield view_x : I
    //   919: istore #6
    //   921: aload_0
    //   922: getfield view_x : I
    //   925: istore #5
    //   927: aload_0
    //   928: getfield view_w : I
    //   931: iconst_2
    //   932: idiv
    //   933: istore_2
    //   934: aload_0
    //   935: getfield view_x : I
    //   938: istore #9
    //   940: aload_0
    //   941: getfield view_x : I
    //   944: istore #12
    //   946: aload_0
    //   947: getfield view_w : I
    //   950: iconst_2
    //   951: idiv
    //   952: istore #11
    //   954: aload_0
    //   955: getfield view_y : I
    //   958: istore #13
    //   960: aload_0
    //   961: getfield view_y : I
    //   964: istore #10
    //   966: aload_0
    //   967: getfield view_y : I
    //   970: istore #8
    //   972: aload_0
    //   973: getfield view_h : I
    //   976: iconst_2
    //   977: idiv
    //   978: istore_3
    //   979: aload_0
    //   980: getfield view_y : I
    //   983: istore #4
    //   985: aload_0
    //   986: getfield view_h : I
    //   989: iconst_2
    //   990: idiv
    //   991: istore #7
    //   993: iconst_0
    //   994: istore_1
    //   995: iload_1
    //   996: iconst_4
    //   997: if_icmpge -> 1442
    //   1000: iconst_4
    //   1001: newarray int
    //   1003: dup
    //   1004: iconst_0
    //   1005: iload #6
    //   1007: iastore
    //   1008: dup
    //   1009: iconst_1
    //   1010: iload #5
    //   1012: iload_2
    //   1013: iadd
    //   1014: iastore
    //   1015: dup
    //   1016: iconst_2
    //   1017: iload #9
    //   1019: iastore
    //   1020: dup
    //   1021: iconst_3
    //   1022: iload #12
    //   1024: iload #11
    //   1026: iadd
    //   1027: iastore
    //   1028: iload_1
    //   1029: iaload
    //   1030: iconst_4
    //   1031: newarray int
    //   1033: dup
    //   1034: iconst_0
    //   1035: iload #13
    //   1037: iastore
    //   1038: dup
    //   1039: iconst_1
    //   1040: iload #10
    //   1042: iastore
    //   1043: dup
    //   1044: iconst_2
    //   1045: iload #8
    //   1047: iload_3
    //   1048: iadd
    //   1049: iastore
    //   1050: dup
    //   1051: iconst_3
    //   1052: iload #4
    //   1054: iload #7
    //   1056: iadd
    //   1057: iastore
    //   1058: iload_1
    //   1059: iaload
    //   1060: aload_0
    //   1061: getfield view_w : I
    //   1064: iconst_2
    //   1065: idiv
    //   1066: aload_0
    //   1067: getfield view_h : I
    //   1070: iconst_2
    //   1071: idiv
    //   1072: invokestatic viewport : (IIII)Z
    //   1075: pop
    //   1076: getstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.hWin : J
    //   1079: getstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.hWin : J
    //   1082: invokestatic getMaxVDegress : (J)F
    //   1085: getstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.hDegrees : F
    //   1088: iload_1
    //   1089: bipush #90
    //   1091: imul
    //   1092: i2f
    //   1093: fadd
    //   1094: fconst_0
    //   1095: invokestatic eyeLookAt : (JFFF)Z
    //   1098: pop
    //   1099: getstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.hWin : J
    //   1102: invokestatic draw : (J)Z
    //   1105: pop
    //   1106: iinc #1, 1
    //   1109: goto -> 995
    //   1112: iconst_2
    //   1113: getstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.eyeMode : I
    //   1116: if_icmpne -> 1312
    //   1119: aload_0
    //   1120: getfield view_x : I
    //   1123: istore #4
    //   1125: aload_0
    //   1126: getfield view_x : I
    //   1129: istore #12
    //   1131: aload_0
    //   1132: getfield view_w : I
    //   1135: iconst_2
    //   1136: idiv
    //   1137: istore_2
    //   1138: aload_0
    //   1139: getfield view_x : I
    //   1142: istore #5
    //   1144: aload_0
    //   1145: getfield view_x : I
    //   1148: istore #7
    //   1150: aload_0
    //   1151: getfield view_w : I
    //   1154: iconst_2
    //   1155: idiv
    //   1156: istore #9
    //   1158: aload_0
    //   1159: getfield view_y : I
    //   1162: istore #6
    //   1164: aload_0
    //   1165: getfield view_y : I
    //   1168: istore #13
    //   1170: aload_0
    //   1171: getfield view_y : I
    //   1174: istore #8
    //   1176: aload_0
    //   1177: getfield view_h : I
    //   1180: iconst_2
    //   1181: idiv
    //   1182: istore #10
    //   1184: aload_0
    //   1185: getfield view_y : I
    //   1188: istore_3
    //   1189: aload_0
    //   1190: getfield view_h : I
    //   1193: iconst_2
    //   1194: idiv
    //   1195: istore #11
    //   1197: iconst_0
    //   1198: istore_1
    //   1199: iload_1
    //   1200: iconst_4
    //   1201: if_icmpge -> 1442
    //   1204: iconst_4
    //   1205: newarray int
    //   1207: dup
    //   1208: iconst_0
    //   1209: iload #4
    //   1211: iastore
    //   1212: dup
    //   1213: iconst_1
    //   1214: iload #12
    //   1216: iload_2
    //   1217: iadd
    //   1218: iastore
    //   1219: dup
    //   1220: iconst_2
    //   1221: iload #5
    //   1223: iastore
    //   1224: dup
    //   1225: iconst_3
    //   1226: iload #7
    //   1228: iload #9
    //   1230: iadd
    //   1231: iastore
    //   1232: iload_1
    //   1233: iaload
    //   1234: iconst_4
    //   1235: newarray int
    //   1237: dup
    //   1238: iconst_0
    //   1239: iload #6
    //   1241: iastore
    //   1242: dup
    //   1243: iconst_1
    //   1244: iload #13
    //   1246: iastore
    //   1247: dup
    //   1248: iconst_2
    //   1249: iload #8
    //   1251: iload #10
    //   1253: iadd
    //   1254: iastore
    //   1255: dup
    //   1256: iconst_3
    //   1257: iload_3
    //   1258: iload #11
    //   1260: iadd
    //   1261: iastore
    //   1262: iload_1
    //   1263: iaload
    //   1264: aload_0
    //   1265: getfield view_w : I
    //   1268: iconst_2
    //   1269: idiv
    //   1270: aload_0
    //   1271: getfield view_h : I
    //   1274: iconst_2
    //   1275: idiv
    //   1276: invokestatic viewport : (IIII)Z
    //   1279: pop
    //   1280: getstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.hWin : J
    //   1283: getstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.hWin : J
    //   1286: invokestatic getMaxVDegress : (J)F
    //   1289: getstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.hEyeDegrees : [F
    //   1292: iload_1
    //   1293: faload
    //   1294: fconst_0
    //   1295: invokestatic eyeLookAt : (JFFF)Z
    //   1298: pop
    //   1299: getstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.hWin : J
    //   1302: invokestatic draw : (J)Z
    //   1305: pop
    //   1306: iinc #1, 1
    //   1309: goto -> 1199
    //   1312: iconst_3
    //   1313: getstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.eyeMode : I
    //   1316: if_icmpne -> 1442
    //   1319: aload_0
    //   1320: getfield view_x : I
    //   1323: istore_2
    //   1324: aload_0
    //   1325: getfield view_x : I
    //   1328: istore #9
    //   1330: aload_0
    //   1331: getfield view_w : I
    //   1334: iconst_2
    //   1335: idiv
    //   1336: istore #11
    //   1338: aload_0
    //   1339: getfield view_y : I
    //   1342: istore #13
    //   1344: aload_0
    //   1345: getfield view_y : I
    //   1348: istore_3
    //   1349: iconst_0
    //   1350: istore_1
    //   1351: iload_1
    //   1352: iconst_2
    //   1353: if_icmpge -> 1442
    //   1356: iconst_2
    //   1357: newarray int
    //   1359: dup
    //   1360: iconst_0
    //   1361: iload_2
    //   1362: iastore
    //   1363: dup
    //   1364: iconst_1
    //   1365: iload #9
    //   1367: iload #11
    //   1369: iadd
    //   1370: iastore
    //   1371: iload_1
    //   1372: iaload
    //   1373: iconst_2
    //   1374: newarray int
    //   1376: dup
    //   1377: iconst_0
    //   1378: iload #13
    //   1380: iastore
    //   1381: dup
    //   1382: iconst_1
    //   1383: iload_3
    //   1384: iastore
    //   1385: iload_1
    //   1386: iaload
    //   1387: aload_0
    //   1388: getfield view_w : I
    //   1391: iconst_2
    //   1392: idiv
    //   1393: aload_0
    //   1394: getfield view_h : I
    //   1397: invokestatic viewport : (IIII)Z
    //   1400: pop
    //   1401: getstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.hWin : J
    //   1404: getstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.vDegrees : F
    //   1407: getstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.hDegrees : F
    //   1410: fconst_0
    //   1411: invokestatic eyeLookAt : (JFFF)Z
    //   1414: pop
    //   1415: aload_0
    //   1416: getstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.vDegrees : F
    //   1419: putfield lastVDegrees : F
    //   1422: aload_0
    //   1423: getstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.hDegrees : F
    //   1426: putfield lastHDegrees : F
    //   1429: getstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.hWin : J
    //   1432: invokestatic draw : (J)Z
    //   1435: pop
    //   1436: iinc #1, 1
    //   1439: goto -> 1351
    //   1442: getstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.bSnapshot : Z
    //   1445: ifeq -> 1483
    //   1448: iconst_0
    //   1449: putstatic com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.bSnapshot : Z
    //   1452: aload_0
    //   1453: aload_0
    //   1454: getfield view_x : I
    //   1457: aload_0
    //   1458: getfield view_y : I
    //   1461: aload_0
    //   1462: getfield view_w : I
    //   1465: aload_0
    //   1466: getfield view_h : I
    //   1469: iconst_1
    //   1470: invokestatic snapshot : (IIIIZ)[B
    //   1473: putfield frameBuf : [B
    //   1476: aload_0
    //   1477: getfield mSnapshotThread : Lcom/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper$SnapshotThread;
    //   1480: invokevirtual start : ()V
    //   1483: aload_0
    //   1484: monitorexit
    //   1485: return
    //   1486: astore #18
    //   1488: aload_0
    //   1489: monitorexit
    //   1490: goto -> 1496
    //   1493: aload #18
    //   1495: athrow
    //   1496: goto -> 1493
    // Exception table:
    //   from	to	target	type
    //   2	32	1486	finally
    //   35	47	1486	finally
    //   47	92	1486	finally
    //   92	126	1486	finally
    //   126	182	1486	finally
    //   185	197	1486	finally
    //   197	201	1486	finally
    //   201	228	1486	finally
    //   231	240	1486	finally
    //   240	328	1486	finally
    //   335	429	1486	finally
    //   434	444	1486	finally
    //   444	453	1486	finally
    //   459	473	1486	finally
    //   476	513	1486	finally
    //   516	559	1486	finally
    //   562	600	1486	finally
    //   603	626	1486	finally
    //   626	638	1486	finally
    //   638	652	1486	finally
    //   664	669	1486	finally
    //   682	687	1486	finally
    //   691	705	1486	finally
    //   709	769	1486	finally
    //   769	801	1486	finally
    //   804	823	1486	finally
    //   823	895	1486	finally
    //   898	905	1486	finally
    //   908	993	1486	finally
    //   1000	1106	1486	finally
    //   1112	1197	1486	finally
    //   1204	1306	1486	finally
    //   1312	1349	1486	finally
    //   1356	1436	1486	finally
    //   1442	1483	1486	finally
    //   1483	1485	1486	finally
    //   1488	1490	1486	finally
  }
  
  public static FHFishEyeDrawingHelper getInstance(Context paramContext) {
    if (instance == null)
      instance = new FHFishEyeDrawingHelper(); 
    return instance;
  }
  
  private void init() {
    this.mHandler = new Handler(Looper.getMainLooper());
    this.mSnapshotThread = new SnapshotThread();
    rgbResIndex = 0;
    displayMode = 0;
    bSnapshot = false;
    vDegrees = 0.0F;
    hDegrees = 0.0F;
    depth = 0.0F;
    angle = 90.0F;
    hOffset = 0.0F;
    velocityX = 0.0F;
    velocityY = 0.0F;
    scrollStep = 0.0F;
    eyeMode = 0;
    hEyeDegrees = new float[] { 0.0F, 90.0F, 180.0F, 270.0F };
    curIndex = 0;
    isDoubleClick = false;
    isZoomIn = false;
    long l = hWin;
    if (l != 0L) {
      FHSDK.unbind(l);
      FHSDK.destroyWindow(hWin);
      hWin = 0L;
    } 
    hBuffer = 0L;
    hWinMixMode = new long[4];
    bUpdated = true;
    bMixMode = false;
    ctrlIndex = 0;
    resChanged = false;
  }
  
  private void updateParams() {
    int i = displayMode;
    if (i != 0 && 6 != i)
      eyeMode = 0; 
    if (isDoubleClick && ((displayMode == 0 && eyeMode == 0) || 6 == displayMode)) {
      isDoubleClick = false;
      if (depth != FHSDK.getMaxZDepth(hWin)) {
        isZoomIn = false;
        depth = FHSDK.getMaxZDepth(hWin);
      } else {
        isZoomIn = true;
        depth = 0.0F;
      } 
    } 
    float f = velocityX;
    if (f > 0.0F) {
      float f1 = scrollStep;
      velocityX = f - f1;
      if (velocityX < 0.0F || f1 / 2000.0F < 0.001F)
        velocityX = 0.0F; 
      i = displayMode;
      if (i == 0 || 6 == i) {
        i = eyeMode;
        if (i == 0 || 1 == i) {
          hDegrees += scrollStep / 2000.0F * 50.0F;
        } else if (2 == i) {
          float[] arrayOfFloat = hEyeDegrees;
          i = curIndex;
          arrayOfFloat[i] = arrayOfFloat[i] - scrollStep / 2000.0F * 50.0F;
        } 
      } else {
        hOffset -= scrollStep / 2000.0F;
      } 
    } else if (f < 0.0F) {
      float f1 = scrollStep;
      velocityX = f + f1;
      if (velocityX > 0.0F || f1 / 2000.0F < 0.005F)
        velocityX = 0.0F; 
      i = displayMode;
      if (i == 0 || 6 == i) {
        i = eyeMode;
        if (i == 0 || 1 == i) {
          hDegrees -= scrollStep / 2000.0F * 50.0F;
        } else if (2 == i) {
          float[] arrayOfFloat = hEyeDegrees;
          i = curIndex;
          arrayOfFloat[i] = arrayOfFloat[i] + scrollStep / 2000.0F * 50.0F;
        } 
      } else {
        hOffset += scrollStep / 2000.0F;
      } 
    } 
    f = velocityY;
    if (f > 0.0F) {
      float f1 = scrollStep;
      velocityY = f - f1;
      if (velocityY < 0.0F || f1 / 2000.0F < 0.005F)
        velocityY = 0.0F; 
      f = vDegrees;
      f1 = scrollStep;
      vDegrees = f - f1 / 2000.0F * 50.0F;
      hOffset -= f1 / 2000.0F;
    } else if (f < 0.0F) {
      float f1 = scrollStep;
      velocityY = f + f1;
      if (velocityY > 0.0F || f1 / 2000.0F < 0.005F)
        velocityY = 0.0F; 
      vDegrees += scrollStep / 2000.0F * 50.0F;
    } 
    f = scrollStep;
    if (f > 0.0F)
      scrollStep = f - 3.0F; 
  }
  
  public boolean isBucketMode() {
    return this.isBucketMode;
  }
  
  public boolean isMaxZDepth() {
    boolean bool;
    if (depth == FHSDK.getMaxZDepth(hWin)) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isNormalVRMode() {
    return this.isNormalVRMode;
  }
  
  public void onDrawFrame(GL10 paramGL10, int paramInt1, int paramInt2, int paramInt3) {
    drawFrame(paramInt1, paramInt2, paramInt3);
  }
  
  public void onSurfaceChanged(GL10 paramGL10, int paramInt1, int paramInt2) {
    mScreenWidth = paramInt1;
    mScreenHeight = paramInt2;
    this.mHandler.post(this.requestRender);
    int i = mScreenWidth;
    mDrawWidth = i;
    paramInt2 = mScreenHeight;
    mDrawHeight = paramInt2;
    this.bSurfaceChanged = true;
    paramInt1 = mDrawWidth;
    this.view_x = (i - paramInt1) / 2;
    i = mDrawHeight;
    this.view_y = (paramInt2 - i) / 2;
    this.view_w = paramInt1;
    this.view_h = i;
    FHSDK.init(paramInt1, i);
    if (0L == hBuffer)
      hBuffer = FHSDK.createBuffer(1); 
    if (0L == hWin)
      hWin = FHSDK.createWindow(displayMode); 
    FHSDK.unbind(hWin);
    FHSDK.bind(hWin, hBuffer);
    for (paramInt1 = 0; paramInt1 < 4; paramInt1++) {
      long[] arrayOfLong = hWinMixMode;
      if (0L == arrayOfLong[paramInt1])
        if (1 == paramInt1) {
          arrayOfLong[paramInt1] = FHSDK.createWindow(6);
        } else if (2 == paramInt1) {
          arrayOfLong[paramInt1] = FHSDK.createWindow(5);
        } else {
          arrayOfLong[paramInt1] = FHSDK.createWindow(paramInt1);
        }  
      FHSDK.unbind(hWinMixMode[paramInt1]);
      FHSDK.bind(hWinMixMode[paramInt1], hBuffer);
    } 
    depth = FHSDK.getMaxZDepth(hWin);
  }
  
  public void onSurfaceCreated(GL10 paramGL10, EGLConfig paramEGLConfig) {
    this.bSurfaceCreate = true;
    displayMode = 0;
  }
  
  public void reset() {
    vDegrees = 0.0F;
    hDegrees = 0.0F;
    depth = FHSDK.getMaxZDepth(hWin);
  }
  
  public void rotate(int paramInt) {
    this.rotateAngle = paramInt;
  }
  
  public void setBucket(boolean paramBoolean) {
    if (paramBoolean) {
      this.isBucketMode = true;
      vDegrees = 0.0F;
      depth = FHSDK.getMaxZDepth(hWin);
    } else {
      this.isBucketMode = false;
      vDegrees = 0.0F;
      depth = 0.0F;
    } 
  }
  
  public void setDoubleClick(boolean paramBoolean) {
    isDoubleClick = paramBoolean;
  }
  
  public void setNormalVRMode(boolean paramBoolean) {
    this.isNormalVRMode = paramBoolean;
  }
  
  public void setvelocityX(float paramFloat) {
    velocityX = paramFloat;
    if (Math.abs(paramFloat) > 3000.0F) {
      scrollStep = 200.0F;
    } else {
      scrollStep = 100.0F;
    } 
  }
  
  public void setvelocityY(float paramFloat) {
    velocityY = paramFloat;
    if (Math.abs(paramFloat) > 3000.0F) {
      scrollStep = 200.0F;
    } else {
      scrollStep = 100.0F;
    } 
  }
  
  public void switchFishEyeMode(int paramInt1, int paramInt2, boolean paramBoolean1, boolean paramBoolean2) {
    displayMode = paramInt1;
    eyeMode = paramInt2;
    if (paramBoolean1 && !isBucketMode())
      setBucket(true); 
    if (paramBoolean2 && isBucketMode())
      setBucket(false); 
    if (!paramBoolean1 && !paramBoolean2)
      setBucket(false); 
  }
  
  public void uninit() {
    long l = hWin;
    if (l != 0L) {
      FHSDK.unbind(l);
      FHSDK.destroyWindow(hWin);
    } 
  }
  
  public class RGBRes {
    public int height;
    
    public byte[] rgb;
    
    public int width;
  }
  
  class SnapshotThread implements Runnable {
    private boolean isShoting = false;
    
    public boolean isShoting() {
      return this.isShoting;
    }
    
    public void run() {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: getfield this$0 : Lcom/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper;
      //   6: invokestatic access$300 : (Lcom/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper;)[B
      //   9: arraylength
      //   10: newarray byte
      //   12: astore_1
      //   13: iconst_0
      //   14: istore_2
      //   15: iload_2
      //   16: aload_0
      //   17: getfield this$0 : Lcom/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper;
      //   20: invokestatic access$400 : (Lcom/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper;)I
      //   23: if_icmpge -> 114
      //   26: aload_0
      //   27: getfield this$0 : Lcom/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper;
      //   30: invokestatic access$500 : (Lcom/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper;)I
      //   33: istore_3
      //   34: aload_0
      //   35: getfield this$0 : Lcom/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper;
      //   38: invokestatic access$400 : (Lcom/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper;)I
      //   41: istore #4
      //   43: aload_0
      //   44: getfield this$0 : Lcom/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper;
      //   47: invokestatic access$500 : (Lcom/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper;)I
      //   50: istore #5
      //   52: iconst_0
      //   53: istore #6
      //   55: iload #6
      //   57: aload_0
      //   58: getfield this$0 : Lcom/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper;
      //   61: invokestatic access$500 : (Lcom/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper;)I
      //   64: iconst_4
      //   65: imul
      //   66: if_icmpge -> 108
      //   69: aload_1
      //   70: iload #4
      //   72: iload_2
      //   73: isub
      //   74: iconst_1
      //   75: isub
      //   76: iload #5
      //   78: imul
      //   79: iconst_4
      //   80: imul
      //   81: iload #6
      //   83: iadd
      //   84: aload_0
      //   85: getfield this$0 : Lcom/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper;
      //   88: invokestatic access$300 : (Lcom/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper;)[B
      //   91: iload_3
      //   92: iload_2
      //   93: imul
      //   94: iconst_4
      //   95: imul
      //   96: iload #6
      //   98: iadd
      //   99: baload
      //   100: i2b
      //   101: bastore
      //   102: iinc #6, 1
      //   105: goto -> 55
      //   108: iinc #2, 1
      //   111: goto -> 15
      //   114: aload_1
      //   115: invokestatic wrap : ([B)Ljava/nio/ByteBuffer;
      //   118: astore #7
      //   120: aload #7
      //   122: getstatic java/nio/ByteOrder.LITTLE_ENDIAN : Ljava/nio/ByteOrder;
      //   125: invokevirtual order : (Ljava/nio/ByteOrder;)Ljava/nio/ByteBuffer;
      //   128: pop
      //   129: aload #7
      //   131: invokevirtual rewind : ()Ljava/nio/Buffer;
      //   134: pop
      //   135: invokestatic getExternalStorageDirectory : ()Ljava/io/File;
      //   138: astore_1
      //   139: new java/lang/StringBuilder
      //   142: astore #8
      //   144: aload #8
      //   146: invokespecial <init> : ()V
      //   149: aload #8
      //   151: aload_1
      //   152: invokevirtual getPath : ()Ljava/lang/String;
      //   155: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   158: pop
      //   159: aload #8
      //   161: ldc '/'
      //   163: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   166: pop
      //   167: aload #8
      //   169: invokestatic currentTimeMillis : ()J
      //   172: invokevirtual append : (J)Ljava/lang/StringBuilder;
      //   175: pop
      //   176: aload #8
      //   178: ldc '.jpg'
      //   180: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   183: pop
      //   184: aload #8
      //   186: invokevirtual toString : ()Ljava/lang/String;
      //   189: astore #9
      //   191: aconst_null
      //   192: astore #10
      //   194: aconst_null
      //   195: astore #11
      //   197: aconst_null
      //   198: astore_1
      //   199: aload_1
      //   200: astore #8
      //   202: aload #10
      //   204: astore #12
      //   206: new java/io/BufferedOutputStream
      //   209: astore #13
      //   211: aload_1
      //   212: astore #8
      //   214: aload #10
      //   216: astore #12
      //   218: new java/io/FileOutputStream
      //   221: astore #14
      //   223: aload_1
      //   224: astore #8
      //   226: aload #10
      //   228: astore #12
      //   230: aload #14
      //   232: aload #9
      //   234: invokespecial <init> : (Ljava/lang/String;)V
      //   237: aload_1
      //   238: astore #8
      //   240: aload #10
      //   242: astore #12
      //   244: aload #13
      //   246: aload #14
      //   248: invokespecial <init> : (Ljava/io/OutputStream;)V
      //   251: aload #13
      //   253: astore_1
      //   254: goto -> 282
      //   257: astore_1
      //   258: goto -> 399
      //   261: astore_1
      //   262: goto -> 371
      //   265: astore #13
      //   267: aload_1
      //   268: astore #8
      //   270: aload #10
      //   272: astore #12
      //   274: aload #13
      //   276: invokevirtual printStackTrace : ()V
      //   279: aload #11
      //   281: astore_1
      //   282: aload_1
      //   283: astore #8
      //   285: aload_1
      //   286: astore #12
      //   288: aload_0
      //   289: getfield this$0 : Lcom/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper;
      //   292: invokestatic access$500 : (Lcom/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper;)I
      //   295: aload_0
      //   296: getfield this$0 : Lcom/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper;
      //   299: invokestatic access$400 : (Lcom/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper;)I
      //   302: getstatic android/graphics/Bitmap$Config.ARGB_8888 : Landroid/graphics/Bitmap$Config;
      //   305: invokestatic createBitmap : (IILandroid/graphics/Bitmap$Config;)Landroid/graphics/Bitmap;
      //   308: astore #10
      //   310: aload_1
      //   311: astore #8
      //   313: aload_1
      //   314: astore #12
      //   316: aload #10
      //   318: aload #7
      //   320: invokevirtual copyPixelsFromBuffer : (Ljava/nio/Buffer;)V
      //   323: aload_1
      //   324: astore #8
      //   326: aload_1
      //   327: astore #12
      //   329: aload #10
      //   331: getstatic android/graphics/Bitmap$CompressFormat.JPEG : Landroid/graphics/Bitmap$CompressFormat;
      //   334: bipush #90
      //   336: aload_1
      //   337: invokevirtual compress : (Landroid/graphics/Bitmap$CompressFormat;ILjava/io/OutputStream;)Z
      //   340: pop
      //   341: aload_1
      //   342: astore #8
      //   344: aload_1
      //   345: astore #12
      //   347: aload #10
      //   349: invokevirtual recycle : ()V
      //   352: aload_1
      //   353: ifnull -> 396
      //   356: aload_1
      //   357: invokevirtual close : ()V
      //   360: goto -> 396
      //   363: astore_1
      //   364: aload_1
      //   365: invokevirtual printStackTrace : ()V
      //   368: goto -> 396
      //   371: aload #12
      //   373: astore #8
      //   375: aload_1
      //   376: invokevirtual printStackTrace : ()V
      //   379: aload #12
      //   381: ifnull -> 396
      //   384: aload #12
      //   386: invokevirtual close : ()V
      //   389: goto -> 396
      //   392: astore_1
      //   393: goto -> 364
      //   396: aload_0
      //   397: monitorexit
      //   398: return
      //   399: aload #8
      //   401: ifnull -> 419
      //   404: aload #8
      //   406: invokevirtual close : ()V
      //   409: goto -> 419
      //   412: astore #8
      //   414: aload #8
      //   416: invokevirtual printStackTrace : ()V
      //   419: aload_1
      //   420: athrow
      //   421: astore_1
      //   422: aload_0
      //   423: monitorexit
      //   424: goto -> 429
      //   427: aload_1
      //   428: athrow
      //   429: goto -> 427
      // Exception table:
      //   from	to	target	type
      //   2	13	421	finally
      //   15	52	421	finally
      //   55	102	421	finally
      //   114	191	421	finally
      //   206	211	265	java/io/FileNotFoundException
      //   206	211	261	java/lang/Exception
      //   206	211	257	finally
      //   218	223	265	java/io/FileNotFoundException
      //   218	223	261	java/lang/Exception
      //   218	223	257	finally
      //   230	237	265	java/io/FileNotFoundException
      //   230	237	261	java/lang/Exception
      //   230	237	257	finally
      //   244	251	265	java/io/FileNotFoundException
      //   244	251	261	java/lang/Exception
      //   244	251	257	finally
      //   274	279	261	java/lang/Exception
      //   274	279	257	finally
      //   288	310	261	java/lang/Exception
      //   288	310	257	finally
      //   316	323	261	java/lang/Exception
      //   316	323	257	finally
      //   329	341	261	java/lang/Exception
      //   329	341	257	finally
      //   347	352	261	java/lang/Exception
      //   347	352	257	finally
      //   356	360	363	java/io/IOException
      //   356	360	421	finally
      //   364	368	421	finally
      //   375	379	257	finally
      //   384	389	392	java/io/IOException
      //   384	389	421	finally
      //   396	398	421	finally
      //   404	409	412	java/io/IOException
      //   404	409	421	finally
      //   414	419	421	finally
      //   419	421	421	finally
      //   422	424	421	finally
    }
    
    public void start() {
      (new Thread(this)).start();
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/ijkvideoview/widget/media/render/FHFishEyeDrawingHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */