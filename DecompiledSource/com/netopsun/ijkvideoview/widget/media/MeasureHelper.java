package com.netopsun.ijkvideoview.widget.media;

import android.content.Context;
import android.view.View;
import com.netopsun.ijkvideoview.R;
import java.lang.ref.WeakReference;

public final class MeasureHelper {
  private int mCurrentAspectRatio = 0;
  
  private int mMeasuredHeight;
  
  private int mMeasuredWidth;
  
  private int mVideoHeight;
  
  private int mVideoRotationDegree;
  
  private int mVideoSarDen;
  
  private int mVideoSarNum;
  
  private int mVideoWidth;
  
  private WeakReference<View> mWeakView;
  
  public MeasureHelper(View paramView) {
    this.mWeakView = new WeakReference<View>(paramView);
  }
  
  public static String getAspectRatioText(Context paramContext, int paramInt) {
    String str;
    if (paramInt != 0) {
      if (paramInt != 1) {
        if (paramInt != 2) {
          if (paramInt != 3) {
            if (paramInt != 4) {
              if (paramInt != 5) {
                str = paramContext.getString(R.string.N_A);
              } else {
                str = str.getString(R.string.VideoView_ar_4_3_fit_parent);
              } 
            } else {
              str = str.getString(R.string.VideoView_ar_16_9_fit_parent);
            } 
          } else {
            str = str.getString(R.string.VideoView_ar_match_parent);
          } 
        } else {
          str = str.getString(R.string.VideoView_ar_aspect_wrap_content);
        } 
      } else {
        str = str.getString(R.string.VideoView_ar_aspect_fill_parent);
      } 
    } else {
      str = str.getString(R.string.VideoView_ar_aspect_fit_parent);
    } 
    return str;
  }
  
  public void doMeasure(int paramInt1, int paramInt2) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mVideoRotationDegree : I
    //   4: istore_3
    //   5: iload_3
    //   6: bipush #90
    //   8: if_icmpeq -> 24
    //   11: iload_1
    //   12: istore #4
    //   14: iload_2
    //   15: istore #5
    //   17: iload_3
    //   18: sipush #270
    //   21: if_icmpne -> 30
    //   24: iload_1
    //   25: istore #5
    //   27: iload_2
    //   28: istore #4
    //   30: aload_0
    //   31: getfield mVideoWidth : I
    //   34: iload #4
    //   36: invokestatic getDefaultSize : (II)I
    //   39: istore_2
    //   40: aload_0
    //   41: getfield mVideoHeight : I
    //   44: iload #5
    //   46: invokestatic getDefaultSize : (II)I
    //   49: istore_1
    //   50: aload_0
    //   51: getfield mCurrentAspectRatio : I
    //   54: iconst_3
    //   55: if_icmpne -> 67
    //   58: iload #4
    //   60: istore_2
    //   61: iload #5
    //   63: istore_1
    //   64: goto -> 650
    //   67: aload_0
    //   68: getfield mVideoWidth : I
    //   71: ifle -> 650
    //   74: aload_0
    //   75: getfield mVideoHeight : I
    //   78: ifle -> 650
    //   81: iload #4
    //   83: invokestatic getMode : (I)I
    //   86: istore #6
    //   88: iload #4
    //   90: invokestatic getSize : (I)I
    //   93: istore_3
    //   94: iload #5
    //   96: invokestatic getMode : (I)I
    //   99: istore_2
    //   100: iload #5
    //   102: invokestatic getSize : (I)I
    //   105: istore #5
    //   107: iload #6
    //   109: ldc -2147483648
    //   111: if_icmpne -> 388
    //   114: iload_2
    //   115: ldc -2147483648
    //   117: if_icmpne -> 388
    //   120: iload_3
    //   121: i2f
    //   122: fstore #7
    //   124: iload #5
    //   126: i2f
    //   127: fstore #8
    //   129: fload #7
    //   131: fload #8
    //   133: fdiv
    //   134: fstore #9
    //   136: aload_0
    //   137: getfield mCurrentAspectRatio : I
    //   140: istore_1
    //   141: iload_1
    //   142: iconst_4
    //   143: if_icmpeq -> 238
    //   146: iload_1
    //   147: iconst_5
    //   148: if_icmpeq -> 203
    //   151: aload_0
    //   152: getfield mVideoWidth : I
    //   155: i2f
    //   156: aload_0
    //   157: getfield mVideoHeight : I
    //   160: i2f
    //   161: fdiv
    //   162: fstore #10
    //   164: aload_0
    //   165: getfield mVideoSarNum : I
    //   168: istore_1
    //   169: fload #10
    //   171: fstore #11
    //   173: iload_1
    //   174: ifle -> 270
    //   177: aload_0
    //   178: getfield mVideoSarDen : I
    //   181: istore_2
    //   182: fload #10
    //   184: fstore #11
    //   186: iload_2
    //   187: ifle -> 270
    //   190: fload #10
    //   192: iload_1
    //   193: i2f
    //   194: fmul
    //   195: iload_2
    //   196: i2f
    //   197: fdiv
    //   198: fstore #11
    //   200: goto -> 270
    //   203: aload_0
    //   204: getfield mVideoRotationDegree : I
    //   207: istore_1
    //   208: iload_1
    //   209: bipush #90
    //   211: if_icmpeq -> 231
    //   214: iload_1
    //   215: sipush #270
    //   218: if_icmpne -> 224
    //   221: goto -> 231
    //   224: ldc 1.3333334
    //   226: fstore #11
    //   228: goto -> 270
    //   231: ldc 0.75
    //   233: fstore #11
    //   235: goto -> 270
    //   238: aload_0
    //   239: getfield mVideoRotationDegree : I
    //   242: istore_1
    //   243: iload_1
    //   244: bipush #90
    //   246: if_icmpeq -> 266
    //   249: iload_1
    //   250: sipush #270
    //   253: if_icmpne -> 259
    //   256: goto -> 266
    //   259: ldc 1.7777778
    //   261: fstore #11
    //   263: goto -> 270
    //   266: ldc 0.5625
    //   268: fstore #11
    //   270: fload #11
    //   272: fload #9
    //   274: fcmpl
    //   275: ifle -> 283
    //   278: iconst_1
    //   279: istore_1
    //   280: goto -> 285
    //   283: iconst_0
    //   284: istore_1
    //   285: aload_0
    //   286: getfield mCurrentAspectRatio : I
    //   289: istore_2
    //   290: iload_2
    //   291: ifeq -> 359
    //   294: iload_2
    //   295: iconst_1
    //   296: if_icmpeq -> 352
    //   299: iload_2
    //   300: iconst_4
    //   301: if_icmpeq -> 359
    //   304: iload_2
    //   305: iconst_5
    //   306: if_icmpeq -> 359
    //   309: iload_1
    //   310: ifeq -> 332
    //   313: aload_0
    //   314: getfield mVideoWidth : I
    //   317: iload_3
    //   318: invokestatic min : (II)I
    //   321: istore_2
    //   322: iload_2
    //   323: i2f
    //   324: fload #11
    //   326: fdiv
    //   327: f2i
    //   328: istore_1
    //   329: goto -> 650
    //   332: aload_0
    //   333: getfield mVideoHeight : I
    //   336: iload #5
    //   338: invokestatic min : (II)I
    //   341: istore_1
    //   342: iload_1
    //   343: i2f
    //   344: fload #11
    //   346: fmul
    //   347: f2i
    //   348: istore_2
    //   349: goto -> 650
    //   352: iload_1
    //   353: ifeq -> 363
    //   356: goto -> 375
    //   359: iload_1
    //   360: ifeq -> 375
    //   363: fload #7
    //   365: fload #11
    //   367: fdiv
    //   368: f2i
    //   369: istore_1
    //   370: iload_3
    //   371: istore_2
    //   372: goto -> 650
    //   375: fload #8
    //   377: fload #11
    //   379: fmul
    //   380: f2i
    //   381: istore_2
    //   382: iload #5
    //   384: istore_1
    //   385: goto -> 650
    //   388: iload #6
    //   390: ldc 1073741824
    //   392: if_icmpne -> 470
    //   395: iload_2
    //   396: ldc 1073741824
    //   398: if_icmpne -> 470
    //   401: aload_0
    //   402: getfield mVideoWidth : I
    //   405: istore #6
    //   407: aload_0
    //   408: getfield mVideoHeight : I
    //   411: istore #4
    //   413: iload #6
    //   415: iload #5
    //   417: imul
    //   418: iload_3
    //   419: iload #4
    //   421: imul
    //   422: if_icmpge -> 440
    //   425: iload #6
    //   427: iload #5
    //   429: imul
    //   430: iload #4
    //   432: idiv
    //   433: istore_2
    //   434: iload #5
    //   436: istore_1
    //   437: goto -> 650
    //   440: iload_3
    //   441: istore_2
    //   442: iload #5
    //   444: istore_1
    //   445: iload #6
    //   447: iload #5
    //   449: imul
    //   450: iload_3
    //   451: iload #4
    //   453: imul
    //   454: if_icmple -> 650
    //   457: iload #4
    //   459: iload_3
    //   460: imul
    //   461: iload #6
    //   463: idiv
    //   464: istore_1
    //   465: iload_3
    //   466: istore_2
    //   467: goto -> 650
    //   470: iload #6
    //   472: ldc 1073741824
    //   474: if_icmpne -> 514
    //   477: aload_0
    //   478: getfield mVideoHeight : I
    //   481: iload_3
    //   482: imul
    //   483: aload_0
    //   484: getfield mVideoWidth : I
    //   487: idiv
    //   488: istore_1
    //   489: iload_2
    //   490: ldc -2147483648
    //   492: if_icmpne -> 509
    //   495: iload_1
    //   496: iload #5
    //   498: if_icmple -> 509
    //   501: iload_3
    //   502: istore_2
    //   503: iload #5
    //   505: istore_1
    //   506: goto -> 650
    //   509: iload_3
    //   510: istore_2
    //   511: goto -> 650
    //   514: iload_2
    //   515: ldc 1073741824
    //   517: if_icmpne -> 570
    //   520: aload_0
    //   521: getfield mVideoWidth : I
    //   524: iload #5
    //   526: imul
    //   527: aload_0
    //   528: getfield mVideoHeight : I
    //   531: idiv
    //   532: istore #4
    //   534: iload #4
    //   536: istore_2
    //   537: iload #5
    //   539: istore_1
    //   540: iload #6
    //   542: ldc -2147483648
    //   544: if_icmpne -> 567
    //   547: iload #4
    //   549: istore_2
    //   550: iload #5
    //   552: istore_1
    //   553: iload #4
    //   555: iload_3
    //   556: if_icmple -> 567
    //   559: iload_3
    //   560: istore_2
    //   561: iload #5
    //   563: istore_1
    //   564: goto -> 650
    //   567: goto -> 650
    //   570: aload_0
    //   571: getfield mVideoWidth : I
    //   574: istore #4
    //   576: aload_0
    //   577: getfield mVideoHeight : I
    //   580: istore_1
    //   581: iload_2
    //   582: ldc -2147483648
    //   584: if_icmpne -> 605
    //   587: iload_1
    //   588: iload #5
    //   590: if_icmple -> 605
    //   593: iload #4
    //   595: iload #5
    //   597: imul
    //   598: iload_1
    //   599: idiv
    //   600: istore #4
    //   602: goto -> 608
    //   605: iload_1
    //   606: istore #5
    //   608: iload #4
    //   610: istore_2
    //   611: iload #5
    //   613: istore_1
    //   614: iload #6
    //   616: ldc -2147483648
    //   618: if_icmpne -> 567
    //   621: iload #4
    //   623: istore_2
    //   624: iload #5
    //   626: istore_1
    //   627: iload #4
    //   629: iload_3
    //   630: if_icmple -> 567
    //   633: aload_0
    //   634: getfield mVideoHeight : I
    //   637: iload_3
    //   638: imul
    //   639: aload_0
    //   640: getfield mVideoWidth : I
    //   643: idiv
    //   644: istore_1
    //   645: iload_3
    //   646: istore_2
    //   647: goto -> 650
    //   650: aload_0
    //   651: iload_2
    //   652: putfield mMeasuredWidth : I
    //   655: aload_0
    //   656: iload_1
    //   657: putfield mMeasuredHeight : I
    //   660: return
  }
  
  public int getMeasuredHeight() {
    return this.mMeasuredHeight;
  }
  
  public int getMeasuredWidth() {
    return this.mMeasuredWidth;
  }
  
  public View getView() {
    WeakReference<View> weakReference = this.mWeakView;
    return (weakReference == null) ? null : weakReference.get();
  }
  
  public void setAspectRatio(int paramInt) {
    this.mCurrentAspectRatio = paramInt;
  }
  
  public void setVideoRotation(int paramInt) {
    this.mVideoRotationDegree = paramInt;
  }
  
  public void setVideoSampleAspectRatio(int paramInt1, int paramInt2) {
    this.mVideoSarNum = paramInt1;
    this.mVideoSarDen = paramInt2;
  }
  
  public void setVideoSize(int paramInt1, int paramInt2) {
    this.mVideoWidth = paramInt1;
    this.mVideoHeight = paramInt2;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/ijkvideoview/widget/media/MeasureHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */