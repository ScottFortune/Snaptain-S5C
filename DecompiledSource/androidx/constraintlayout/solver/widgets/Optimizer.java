package androidx.constraintlayout.solver.widgets;

import androidx.constraintlayout.solver.LinearSystem;

public class Optimizer {
  static final int FLAG_CHAIN_DANGLING = 1;
  
  static final int FLAG_RECOMPUTE_BOUNDS = 2;
  
  static final int FLAG_USE_OPTIMIZE = 0;
  
  public static final int OPTIMIZATION_BARRIER = 2;
  
  public static final int OPTIMIZATION_CHAIN = 4;
  
  public static final int OPTIMIZATION_DIMENSIONS = 8;
  
  public static final int OPTIMIZATION_DIRECT = 1;
  
  public static final int OPTIMIZATION_GROUPS = 32;
  
  public static final int OPTIMIZATION_NONE = 0;
  
  public static final int OPTIMIZATION_RATIO = 16;
  
  public static final int OPTIMIZATION_STANDARD = 7;
  
  static boolean[] flags = new boolean[3];
  
  static void analyze(int paramInt, ConstraintWidget paramConstraintWidget) {
    int i;
    paramConstraintWidget.updateResolutionNodes();
    ResolutionAnchor resolutionAnchor1 = paramConstraintWidget.mLeft.getResolutionNode();
    ResolutionAnchor resolutionAnchor2 = paramConstraintWidget.mTop.getResolutionNode();
    ResolutionAnchor resolutionAnchor3 = paramConstraintWidget.mRight.getResolutionNode();
    ResolutionAnchor resolutionAnchor4 = paramConstraintWidget.mBottom.getResolutionNode();
    if ((paramInt & 0x8) == 8) {
      paramInt = 1;
    } else {
      paramInt = 0;
    } 
    if (paramConstraintWidget.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && optimizableMatchConstraint(paramConstraintWidget, 0)) {
      i = 1;
    } else {
      i = 0;
    } 
    if (resolutionAnchor1.type != 4 && resolutionAnchor3.type != 4)
      if (paramConstraintWidget.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.FIXED || (i && paramConstraintWidget.getVisibility() == 8)) {
        if (paramConstraintWidget.mLeft.mTarget == null && paramConstraintWidget.mRight.mTarget == null) {
          resolutionAnchor1.setType(1);
          resolutionAnchor3.setType(1);
          if (paramInt != 0) {
            resolutionAnchor3.dependsOn(resolutionAnchor1, 1, paramConstraintWidget.getResolutionWidth());
          } else {
            resolutionAnchor3.dependsOn(resolutionAnchor1, paramConstraintWidget.getWidth());
          } 
        } else if (paramConstraintWidget.mLeft.mTarget != null && paramConstraintWidget.mRight.mTarget == null) {
          resolutionAnchor1.setType(1);
          resolutionAnchor3.setType(1);
          if (paramInt != 0) {
            resolutionAnchor3.dependsOn(resolutionAnchor1, 1, paramConstraintWidget.getResolutionWidth());
          } else {
            resolutionAnchor3.dependsOn(resolutionAnchor1, paramConstraintWidget.getWidth());
          } 
        } else if (paramConstraintWidget.mLeft.mTarget == null && paramConstraintWidget.mRight.mTarget != null) {
          resolutionAnchor1.setType(1);
          resolutionAnchor3.setType(1);
          resolutionAnchor1.dependsOn(resolutionAnchor3, -paramConstraintWidget.getWidth());
          if (paramInt != 0) {
            resolutionAnchor1.dependsOn(resolutionAnchor3, -1, paramConstraintWidget.getResolutionWidth());
          } else {
            resolutionAnchor1.dependsOn(resolutionAnchor3, -paramConstraintWidget.getWidth());
          } 
        } else if (paramConstraintWidget.mLeft.mTarget != null && paramConstraintWidget.mRight.mTarget != null) {
          resolutionAnchor1.setType(2);
          resolutionAnchor3.setType(2);
          if (paramInt != 0) {
            paramConstraintWidget.getResolutionWidth().addDependent(resolutionAnchor1);
            paramConstraintWidget.getResolutionWidth().addDependent(resolutionAnchor3);
            resolutionAnchor1.setOpposite(resolutionAnchor3, -1, paramConstraintWidget.getResolutionWidth());
            resolutionAnchor3.setOpposite(resolutionAnchor1, 1, paramConstraintWidget.getResolutionWidth());
          } else {
            resolutionAnchor1.setOpposite(resolutionAnchor3, -paramConstraintWidget.getWidth());
            resolutionAnchor3.setOpposite(resolutionAnchor1, paramConstraintWidget.getWidth());
          } 
        } 
      } else if (i) {
        i = paramConstraintWidget.getWidth();
        resolutionAnchor1.setType(1);
        resolutionAnchor3.setType(1);
        if (paramConstraintWidget.mLeft.mTarget == null && paramConstraintWidget.mRight.mTarget == null) {
          if (paramInt != 0) {
            resolutionAnchor3.dependsOn(resolutionAnchor1, 1, paramConstraintWidget.getResolutionWidth());
          } else {
            resolutionAnchor3.dependsOn(resolutionAnchor1, i);
          } 
        } else if (paramConstraintWidget.mLeft.mTarget != null && paramConstraintWidget.mRight.mTarget == null) {
          if (paramInt != 0) {
            resolutionAnchor3.dependsOn(resolutionAnchor1, 1, paramConstraintWidget.getResolutionWidth());
          } else {
            resolutionAnchor3.dependsOn(resolutionAnchor1, i);
          } 
        } else if (paramConstraintWidget.mLeft.mTarget == null && paramConstraintWidget.mRight.mTarget != null) {
          if (paramInt != 0) {
            resolutionAnchor1.dependsOn(resolutionAnchor3, -1, paramConstraintWidget.getResolutionWidth());
          } else {
            resolutionAnchor1.dependsOn(resolutionAnchor3, -i);
          } 
        } else if (paramConstraintWidget.mLeft.mTarget != null && paramConstraintWidget.mRight.mTarget != null) {
          if (paramInt != 0) {
            paramConstraintWidget.getResolutionWidth().addDependent(resolutionAnchor1);
            paramConstraintWidget.getResolutionWidth().addDependent(resolutionAnchor3);
          } 
          if (paramConstraintWidget.mDimensionRatio == 0.0F) {
            resolutionAnchor1.setType(3);
            resolutionAnchor3.setType(3);
            resolutionAnchor1.setOpposite(resolutionAnchor3, 0.0F);
            resolutionAnchor3.setOpposite(resolutionAnchor1, 0.0F);
          } else {
            resolutionAnchor1.setType(2);
            resolutionAnchor3.setType(2);
            resolutionAnchor1.setOpposite(resolutionAnchor3, -i);
            resolutionAnchor3.setOpposite(resolutionAnchor1, i);
            paramConstraintWidget.setWidth(i);
          } 
        } 
      }  
    if (paramConstraintWidget.mListDimensionBehaviors[1] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && optimizableMatchConstraint(paramConstraintWidget, 1)) {
      i = 1;
    } else {
      i = 0;
    } 
    if (resolutionAnchor2.type != 4 && resolutionAnchor4.type != 4) {
      if (paramConstraintWidget.mListDimensionBehaviors[1] == ConstraintWidget.DimensionBehaviour.FIXED || (i != 0 && paramConstraintWidget.getVisibility() == 8)) {
        if (paramConstraintWidget.mTop.mTarget == null && paramConstraintWidget.mBottom.mTarget == null) {
          resolutionAnchor2.setType(1);
          resolutionAnchor4.setType(1);
          if (paramInt != 0) {
            resolutionAnchor4.dependsOn(resolutionAnchor2, 1, paramConstraintWidget.getResolutionHeight());
          } else {
            resolutionAnchor4.dependsOn(resolutionAnchor2, paramConstraintWidget.getHeight());
          } 
          if (paramConstraintWidget.mBaseline.mTarget != null) {
            paramConstraintWidget.mBaseline.getResolutionNode().setType(1);
            resolutionAnchor2.dependsOn(1, paramConstraintWidget.mBaseline.getResolutionNode(), -paramConstraintWidget.mBaselineDistance);
          } 
        } else if (paramConstraintWidget.mTop.mTarget != null && paramConstraintWidget.mBottom.mTarget == null) {
          resolutionAnchor2.setType(1);
          resolutionAnchor4.setType(1);
          if (paramInt != 0) {
            resolutionAnchor4.dependsOn(resolutionAnchor2, 1, paramConstraintWidget.getResolutionHeight());
          } else {
            resolutionAnchor4.dependsOn(resolutionAnchor2, paramConstraintWidget.getHeight());
          } 
          if (paramConstraintWidget.mBaselineDistance > 0)
            paramConstraintWidget.mBaseline.getResolutionNode().dependsOn(1, resolutionAnchor2, paramConstraintWidget.mBaselineDistance); 
        } else if (paramConstraintWidget.mTop.mTarget == null && paramConstraintWidget.mBottom.mTarget != null) {
          resolutionAnchor2.setType(1);
          resolutionAnchor4.setType(1);
          if (paramInt != 0) {
            resolutionAnchor2.dependsOn(resolutionAnchor4, -1, paramConstraintWidget.getResolutionHeight());
          } else {
            resolutionAnchor2.dependsOn(resolutionAnchor4, -paramConstraintWidget.getHeight());
          } 
          if (paramConstraintWidget.mBaselineDistance > 0)
            paramConstraintWidget.mBaseline.getResolutionNode().dependsOn(1, resolutionAnchor2, paramConstraintWidget.mBaselineDistance); 
        } else if (paramConstraintWidget.mTop.mTarget != null && paramConstraintWidget.mBottom.mTarget != null) {
          resolutionAnchor2.setType(2);
          resolutionAnchor4.setType(2);
          if (paramInt != 0) {
            resolutionAnchor2.setOpposite(resolutionAnchor4, -1, paramConstraintWidget.getResolutionHeight());
            resolutionAnchor4.setOpposite(resolutionAnchor2, 1, paramConstraintWidget.getResolutionHeight());
            paramConstraintWidget.getResolutionHeight().addDependent(resolutionAnchor2);
            paramConstraintWidget.getResolutionWidth().addDependent(resolutionAnchor4);
          } else {
            resolutionAnchor2.setOpposite(resolutionAnchor4, -paramConstraintWidget.getHeight());
            resolutionAnchor4.setOpposite(resolutionAnchor2, paramConstraintWidget.getHeight());
          } 
          if (paramConstraintWidget.mBaselineDistance > 0)
            paramConstraintWidget.mBaseline.getResolutionNode().dependsOn(1, resolutionAnchor2, paramConstraintWidget.mBaselineDistance); 
        } 
        return;
      } 
      if (i != 0) {
        i = paramConstraintWidget.getHeight();
        resolutionAnchor2.setType(1);
        resolutionAnchor4.setType(1);
        if (paramConstraintWidget.mTop.mTarget == null && paramConstraintWidget.mBottom.mTarget == null) {
          if (paramInt != 0) {
            resolutionAnchor4.dependsOn(resolutionAnchor2, 1, paramConstraintWidget.getResolutionHeight());
          } else {
            resolutionAnchor4.dependsOn(resolutionAnchor2, i);
          } 
        } else if (paramConstraintWidget.mTop.mTarget != null && paramConstraintWidget.mBottom.mTarget == null) {
          if (paramInt != 0) {
            resolutionAnchor4.dependsOn(resolutionAnchor2, 1, paramConstraintWidget.getResolutionHeight());
          } else {
            resolutionAnchor4.dependsOn(resolutionAnchor2, i);
          } 
        } else if (paramConstraintWidget.mTop.mTarget == null && paramConstraintWidget.mBottom.mTarget != null) {
          if (paramInt != 0) {
            resolutionAnchor2.dependsOn(resolutionAnchor4, -1, paramConstraintWidget.getResolutionHeight());
          } else {
            resolutionAnchor2.dependsOn(resolutionAnchor4, -i);
          } 
        } else if (paramConstraintWidget.mTop.mTarget != null && paramConstraintWidget.mBottom.mTarget != null) {
          if (paramInt != 0) {
            paramConstraintWidget.getResolutionHeight().addDependent(resolutionAnchor2);
            paramConstraintWidget.getResolutionWidth().addDependent(resolutionAnchor4);
          } 
          if (paramConstraintWidget.mDimensionRatio == 0.0F) {
            resolutionAnchor2.setType(3);
            resolutionAnchor4.setType(3);
            resolutionAnchor2.setOpposite(resolutionAnchor4, 0.0F);
            resolutionAnchor4.setOpposite(resolutionAnchor2, 0.0F);
          } else {
            resolutionAnchor2.setType(2);
            resolutionAnchor4.setType(2);
            resolutionAnchor2.setOpposite(resolutionAnchor4, -i);
            resolutionAnchor4.setOpposite(resolutionAnchor2, i);
            paramConstraintWidget.setHeight(i);
            if (paramConstraintWidget.mBaselineDistance > 0)
              paramConstraintWidget.mBaseline.getResolutionNode().dependsOn(1, resolutionAnchor2, paramConstraintWidget.mBaselineDistance); 
          } 
        } 
      } 
    } 
  }
  
  static boolean applyChainOptimized(ConstraintWidgetContainer paramConstraintWidgetContainer, LinearSystem paramLinearSystem, int paramInt1, int paramInt2, ChainHead paramChainHead) {
    // Byte code:
    //   0: aload #4
    //   2: getfield mFirst : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   5: astore #5
    //   7: aload #4
    //   9: getfield mLast : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   12: astore #6
    //   14: aload #4
    //   16: getfield mFirstVisibleWidget : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   19: astore #7
    //   21: aload #4
    //   23: getfield mLastVisibleWidget : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   26: astore #8
    //   28: aload #4
    //   30: getfield mHead : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   33: astore #9
    //   35: aload #4
    //   37: getfield mTotalWeight : F
    //   40: fstore #10
    //   42: aload #4
    //   44: getfield mFirstMatchConstraintWidget : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   47: astore #11
    //   49: aload #4
    //   51: getfield mLastMatchConstraintWidget : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   54: astore #4
    //   56: aload_0
    //   57: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   60: iload_2
    //   61: aaload
    //   62: astore_0
    //   63: getstatic androidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   66: astore_0
    //   67: iload_2
    //   68: ifne -> 151
    //   71: aload #9
    //   73: getfield mHorizontalChainStyle : I
    //   76: ifne -> 85
    //   79: iconst_1
    //   80: istore #12
    //   82: goto -> 88
    //   85: iconst_0
    //   86: istore #12
    //   88: aload #9
    //   90: getfield mHorizontalChainStyle : I
    //   93: iconst_1
    //   94: if_icmpne -> 103
    //   97: iconst_1
    //   98: istore #13
    //   100: goto -> 106
    //   103: iconst_0
    //   104: istore #13
    //   106: iload #12
    //   108: istore #14
    //   110: iload #13
    //   112: istore #15
    //   114: aload #9
    //   116: getfield mHorizontalChainStyle : I
    //   119: iconst_2
    //   120: if_icmpne -> 141
    //   123: iload #13
    //   125: istore #15
    //   127: iload #12
    //   129: istore #14
    //   131: iconst_1
    //   132: istore #12
    //   134: iload #14
    //   136: istore #13
    //   138: goto -> 214
    //   141: iconst_0
    //   142: istore #12
    //   144: iload #14
    //   146: istore #13
    //   148: goto -> 214
    //   151: aload #9
    //   153: getfield mVerticalChainStyle : I
    //   156: ifne -> 165
    //   159: iconst_1
    //   160: istore #12
    //   162: goto -> 168
    //   165: iconst_0
    //   166: istore #12
    //   168: aload #9
    //   170: getfield mVerticalChainStyle : I
    //   173: iconst_1
    //   174: if_icmpne -> 183
    //   177: iconst_1
    //   178: istore #13
    //   180: goto -> 186
    //   183: iconst_0
    //   184: istore #13
    //   186: iload #12
    //   188: istore #14
    //   190: iload #13
    //   192: istore #15
    //   194: aload #9
    //   196: getfield mVerticalChainStyle : I
    //   199: iconst_2
    //   200: if_icmpne -> 141
    //   203: iload #12
    //   205: istore #14
    //   207: iload #13
    //   209: istore #15
    //   211: goto -> 131
    //   214: aload #5
    //   216: astore #4
    //   218: iconst_0
    //   219: istore #16
    //   221: iconst_0
    //   222: istore #17
    //   224: iconst_0
    //   225: istore #14
    //   227: fconst_0
    //   228: fstore #18
    //   230: fconst_0
    //   231: fstore #19
    //   233: iload #17
    //   235: ifne -> 606
    //   238: iload #14
    //   240: istore #20
    //   242: fload #18
    //   244: fstore #21
    //   246: fload #19
    //   248: fstore #22
    //   250: aload #4
    //   252: invokevirtual getVisibility : ()I
    //   255: bipush #8
    //   257: if_icmpeq -> 381
    //   260: iload #14
    //   262: iconst_1
    //   263: iadd
    //   264: istore #20
    //   266: iload_2
    //   267: ifne -> 280
    //   270: aload #4
    //   272: invokevirtual getWidth : ()I
    //   275: istore #14
    //   277: goto -> 287
    //   280: aload #4
    //   282: invokevirtual getHeight : ()I
    //   285: istore #14
    //   287: fload #18
    //   289: iload #14
    //   291: i2f
    //   292: fadd
    //   293: fstore #21
    //   295: fload #21
    //   297: fstore #22
    //   299: aload #4
    //   301: aload #7
    //   303: if_acmpeq -> 322
    //   306: fload #21
    //   308: aload #4
    //   310: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   313: iload_3
    //   314: aaload
    //   315: invokevirtual getMargin : ()I
    //   318: i2f
    //   319: fadd
    //   320: fstore #22
    //   322: fload #22
    //   324: fstore #21
    //   326: aload #4
    //   328: aload #8
    //   330: if_acmpeq -> 351
    //   333: fload #22
    //   335: aload #4
    //   337: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   340: iload_3
    //   341: iconst_1
    //   342: iadd
    //   343: aaload
    //   344: invokevirtual getMargin : ()I
    //   347: i2f
    //   348: fadd
    //   349: fstore #21
    //   351: fload #19
    //   353: aload #4
    //   355: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   358: iload_3
    //   359: aaload
    //   360: invokevirtual getMargin : ()I
    //   363: i2f
    //   364: fadd
    //   365: aload #4
    //   367: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   370: iload_3
    //   371: iconst_1
    //   372: iadd
    //   373: aaload
    //   374: invokevirtual getMargin : ()I
    //   377: i2f
    //   378: fadd
    //   379: fstore #22
    //   381: aload #4
    //   383: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   386: iload_3
    //   387: aaload
    //   388: astore_0
    //   389: iload #16
    //   391: istore #14
    //   393: aload #4
    //   395: invokevirtual getVisibility : ()I
    //   398: bipush #8
    //   400: if_icmpeq -> 499
    //   403: iload #16
    //   405: istore #14
    //   407: aload #4
    //   409: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   412: iload_2
    //   413: aaload
    //   414: getstatic androidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   417: if_acmpne -> 499
    //   420: iload #16
    //   422: iconst_1
    //   423: iadd
    //   424: istore #14
    //   426: iload_2
    //   427: ifne -> 458
    //   430: aload #4
    //   432: getfield mMatchConstraintDefaultWidth : I
    //   435: ifeq -> 440
    //   438: iconst_0
    //   439: ireturn
    //   440: aload #4
    //   442: getfield mMatchConstraintMinWidth : I
    //   445: ifne -> 456
    //   448: aload #4
    //   450: getfield mMatchConstraintMaxWidth : I
    //   453: ifeq -> 487
    //   456: iconst_0
    //   457: ireturn
    //   458: aload #4
    //   460: getfield mMatchConstraintDefaultHeight : I
    //   463: ifeq -> 468
    //   466: iconst_0
    //   467: ireturn
    //   468: aload #4
    //   470: getfield mMatchConstraintMinHeight : I
    //   473: ifne -> 497
    //   476: aload #4
    //   478: getfield mMatchConstraintMaxHeight : I
    //   481: ifeq -> 487
    //   484: goto -> 497
    //   487: aload #4
    //   489: getfield mDimensionRatio : F
    //   492: fconst_0
    //   493: fcmpl
    //   494: ifeq -> 499
    //   497: iconst_0
    //   498: ireturn
    //   499: aload #4
    //   501: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   504: iload_3
    //   505: iconst_1
    //   506: iadd
    //   507: aaload
    //   508: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   511: astore_0
    //   512: aload_0
    //   513: ifnull -> 556
    //   516: aload_0
    //   517: getfield mOwner : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   520: astore_0
    //   521: aload_0
    //   522: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   525: iload_3
    //   526: aaload
    //   527: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   530: ifnull -> 556
    //   533: aload_0
    //   534: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   537: iload_3
    //   538: aaload
    //   539: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   542: getfield mOwner : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   545: aload #4
    //   547: if_acmpeq -> 553
    //   550: goto -> 556
    //   553: goto -> 558
    //   556: aconst_null
    //   557: astore_0
    //   558: aload_0
    //   559: ifnull -> 584
    //   562: iload #14
    //   564: istore #16
    //   566: aload_0
    //   567: astore #4
    //   569: iload #20
    //   571: istore #14
    //   573: fload #21
    //   575: fstore #18
    //   577: fload #22
    //   579: fstore #19
    //   581: goto -> 233
    //   584: iconst_1
    //   585: istore #17
    //   587: iload #14
    //   589: istore #16
    //   591: iload #20
    //   593: istore #14
    //   595: fload #21
    //   597: fstore #18
    //   599: fload #22
    //   601: fstore #19
    //   603: goto -> 233
    //   606: aload #5
    //   608: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   611: iload_3
    //   612: aaload
    //   613: invokevirtual getResolutionNode : ()Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;
    //   616: astore #9
    //   618: aload #6
    //   620: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   623: astore_0
    //   624: iload_3
    //   625: iconst_1
    //   626: iadd
    //   627: istore #17
    //   629: aload_0
    //   630: iload #17
    //   632: aaload
    //   633: invokevirtual getResolutionNode : ()Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;
    //   636: astore_0
    //   637: aload #9
    //   639: getfield target : Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;
    //   642: ifnull -> 1865
    //   645: aload_0
    //   646: getfield target : Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;
    //   649: ifnonnull -> 655
    //   652: goto -> 1865
    //   655: aload #9
    //   657: getfield target : Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;
    //   660: getfield state : I
    //   663: iconst_1
    //   664: if_icmpne -> 1863
    //   667: aload_0
    //   668: getfield target : Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;
    //   671: getfield state : I
    //   674: iconst_1
    //   675: if_icmpeq -> 681
    //   678: goto -> 1863
    //   681: iload #16
    //   683: ifle -> 695
    //   686: iload #16
    //   688: iload #14
    //   690: if_icmpeq -> 695
    //   693: iconst_0
    //   694: ireturn
    //   695: iload #12
    //   697: ifne -> 719
    //   700: iload #13
    //   702: ifne -> 719
    //   705: iload #15
    //   707: ifeq -> 713
    //   710: goto -> 719
    //   713: fconst_0
    //   714: fstore #21
    //   716: goto -> 769
    //   719: aload #7
    //   721: ifnull -> 740
    //   724: aload #7
    //   726: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   729: iload_3
    //   730: aaload
    //   731: invokevirtual getMargin : ()I
    //   734: i2f
    //   735: fstore #22
    //   737: goto -> 743
    //   740: fconst_0
    //   741: fstore #22
    //   743: fload #22
    //   745: fstore #21
    //   747: aload #8
    //   749: ifnull -> 769
    //   752: fload #22
    //   754: aload #8
    //   756: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   759: iload #17
    //   761: aaload
    //   762: invokevirtual getMargin : ()I
    //   765: i2f
    //   766: fadd
    //   767: fstore #21
    //   769: aload #9
    //   771: getfield target : Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;
    //   774: getfield resolvedOffset : F
    //   777: fstore #23
    //   779: aload_0
    //   780: getfield target : Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;
    //   783: getfield resolvedOffset : F
    //   786: fstore #22
    //   788: fload #23
    //   790: fload #22
    //   792: fcmpg
    //   793: ifge -> 806
    //   796: fload #22
    //   798: fload #23
    //   800: fsub
    //   801: fstore #22
    //   803: goto -> 813
    //   806: fload #23
    //   808: fload #22
    //   810: fsub
    //   811: fstore #22
    //   813: fload #22
    //   815: fload #18
    //   817: fsub
    //   818: fstore #24
    //   820: iload #16
    //   822: ifle -> 1138
    //   825: iload #16
    //   827: iload #14
    //   829: if_icmpne -> 1138
    //   832: aload #4
    //   834: invokevirtual getParent : ()Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   837: ifnull -> 858
    //   840: aload #4
    //   842: invokevirtual getParent : ()Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   845: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   848: iload_2
    //   849: aaload
    //   850: getstatic androidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   853: if_acmpne -> 858
    //   856: iconst_0
    //   857: ireturn
    //   858: fload #24
    //   860: fload #18
    //   862: fadd
    //   863: fload #19
    //   865: fsub
    //   866: fstore #22
    //   868: aload #5
    //   870: astore_0
    //   871: aload_0
    //   872: ifnull -> 1136
    //   875: getstatic androidx/constraintlayout/solver/LinearSystem.sMetrics : Landroidx/constraintlayout/solver/Metrics;
    //   878: ifnull -> 932
    //   881: getstatic androidx/constraintlayout/solver/LinearSystem.sMetrics : Landroidx/constraintlayout/solver/Metrics;
    //   884: astore #4
    //   886: aload #4
    //   888: aload #4
    //   890: getfield nonresolvedWidgets : J
    //   893: lconst_1
    //   894: lsub
    //   895: putfield nonresolvedWidgets : J
    //   898: getstatic androidx/constraintlayout/solver/LinearSystem.sMetrics : Landroidx/constraintlayout/solver/Metrics;
    //   901: astore #4
    //   903: aload #4
    //   905: aload #4
    //   907: getfield resolvedWidgets : J
    //   910: lconst_1
    //   911: ladd
    //   912: putfield resolvedWidgets : J
    //   915: getstatic androidx/constraintlayout/solver/LinearSystem.sMetrics : Landroidx/constraintlayout/solver/Metrics;
    //   918: astore #4
    //   920: aload #4
    //   922: aload #4
    //   924: getfield chainConnectionResolved : J
    //   927: lconst_1
    //   928: ladd
    //   929: putfield chainConnectionResolved : J
    //   932: aload_0
    //   933: getfield mNextChainWidget : [Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   936: iload_2
    //   937: aaload
    //   938: astore #4
    //   940: aload #4
    //   942: ifnonnull -> 955
    //   945: fload #23
    //   947: fstore #21
    //   949: aload_0
    //   950: aload #6
    //   952: if_acmpne -> 1126
    //   955: fload #22
    //   957: iload #16
    //   959: i2f
    //   960: fdiv
    //   961: fstore #21
    //   963: fload #10
    //   965: fconst_0
    //   966: fcmpl
    //   967: ifle -> 1002
    //   970: aload_0
    //   971: getfield mWeight : [F
    //   974: iload_2
    //   975: faload
    //   976: ldc -1.0
    //   978: fcmpl
    //   979: ifne -> 988
    //   982: fconst_0
    //   983: fstore #21
    //   985: goto -> 1002
    //   988: aload_0
    //   989: getfield mWeight : [F
    //   992: iload_2
    //   993: faload
    //   994: fload #22
    //   996: fmul
    //   997: fload #10
    //   999: fdiv
    //   1000: fstore #21
    //   1002: aload_0
    //   1003: invokevirtual getVisibility : ()I
    //   1006: bipush #8
    //   1008: if_icmpne -> 1014
    //   1011: fconst_0
    //   1012: fstore #21
    //   1014: fload #23
    //   1016: aload_0
    //   1017: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1020: iload_3
    //   1021: aaload
    //   1022: invokevirtual getMargin : ()I
    //   1025: i2f
    //   1026: fadd
    //   1027: fstore #23
    //   1029: aload_0
    //   1030: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1033: iload_3
    //   1034: aaload
    //   1035: invokevirtual getResolutionNode : ()Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;
    //   1038: aload #9
    //   1040: getfield resolvedTarget : Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;
    //   1043: fload #23
    //   1045: invokevirtual resolve : (Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;F)V
    //   1048: aload_0
    //   1049: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1052: iload #17
    //   1054: aaload
    //   1055: invokevirtual getResolutionNode : ()Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;
    //   1058: astore #7
    //   1060: aload #9
    //   1062: getfield resolvedTarget : Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;
    //   1065: astore #5
    //   1067: fload #23
    //   1069: fload #21
    //   1071: fadd
    //   1072: fstore #21
    //   1074: aload #7
    //   1076: aload #5
    //   1078: fload #21
    //   1080: invokevirtual resolve : (Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;F)V
    //   1083: aload_0
    //   1084: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1087: iload_3
    //   1088: aaload
    //   1089: invokevirtual getResolutionNode : ()Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;
    //   1092: aload_1
    //   1093: invokevirtual addResolvedValue : (Landroidx/constraintlayout/solver/LinearSystem;)V
    //   1096: aload_0
    //   1097: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1100: iload #17
    //   1102: aaload
    //   1103: invokevirtual getResolutionNode : ()Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;
    //   1106: aload_1
    //   1107: invokevirtual addResolvedValue : (Landroidx/constraintlayout/solver/LinearSystem;)V
    //   1110: fload #21
    //   1112: aload_0
    //   1113: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1116: iload #17
    //   1118: aaload
    //   1119: invokevirtual getMargin : ()I
    //   1122: i2f
    //   1123: fadd
    //   1124: fstore #21
    //   1126: aload #4
    //   1128: astore_0
    //   1129: fload #21
    //   1131: fstore #23
    //   1133: goto -> 871
    //   1136: iconst_1
    //   1137: ireturn
    //   1138: fload #24
    //   1140: fconst_0
    //   1141: fcmpg
    //   1142: ifge -> 1154
    //   1145: iconst_1
    //   1146: istore #12
    //   1148: iconst_0
    //   1149: istore #13
    //   1151: iconst_0
    //   1152: istore #15
    //   1154: iload #12
    //   1156: ifeq -> 1408
    //   1159: aload #5
    //   1161: astore_0
    //   1162: fload #23
    //   1164: fload #24
    //   1166: fload #21
    //   1168: fsub
    //   1169: aload_0
    //   1170: iload_2
    //   1171: invokevirtual getBiasPercent : (I)F
    //   1174: fmul
    //   1175: fadd
    //   1176: fstore #21
    //   1178: aload_0
    //   1179: ifnull -> 1421
    //   1182: getstatic androidx/constraintlayout/solver/LinearSystem.sMetrics : Landroidx/constraintlayout/solver/Metrics;
    //   1185: ifnull -> 1239
    //   1188: getstatic androidx/constraintlayout/solver/LinearSystem.sMetrics : Landroidx/constraintlayout/solver/Metrics;
    //   1191: astore #4
    //   1193: aload #4
    //   1195: aload #4
    //   1197: getfield nonresolvedWidgets : J
    //   1200: lconst_1
    //   1201: lsub
    //   1202: putfield nonresolvedWidgets : J
    //   1205: getstatic androidx/constraintlayout/solver/LinearSystem.sMetrics : Landroidx/constraintlayout/solver/Metrics;
    //   1208: astore #4
    //   1210: aload #4
    //   1212: aload #4
    //   1214: getfield resolvedWidgets : J
    //   1217: lconst_1
    //   1218: ladd
    //   1219: putfield resolvedWidgets : J
    //   1222: getstatic androidx/constraintlayout/solver/LinearSystem.sMetrics : Landroidx/constraintlayout/solver/Metrics;
    //   1225: astore #4
    //   1227: aload #4
    //   1229: aload #4
    //   1231: getfield chainConnectionResolved : J
    //   1234: lconst_1
    //   1235: ladd
    //   1236: putfield chainConnectionResolved : J
    //   1239: aload_0
    //   1240: getfield mNextChainWidget : [Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   1243: iload_2
    //   1244: aaload
    //   1245: astore #4
    //   1247: aload #4
    //   1249: ifnonnull -> 1262
    //   1252: fload #21
    //   1254: fstore #22
    //   1256: aload_0
    //   1257: aload #6
    //   1259: if_acmpne -> 1398
    //   1262: iload_2
    //   1263: ifne -> 1275
    //   1266: aload_0
    //   1267: invokevirtual getWidth : ()I
    //   1270: istore #12
    //   1272: goto -> 1281
    //   1275: aload_0
    //   1276: invokevirtual getHeight : ()I
    //   1279: istore #12
    //   1281: iload #12
    //   1283: i2f
    //   1284: fstore #22
    //   1286: fload #21
    //   1288: aload_0
    //   1289: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1292: iload_3
    //   1293: aaload
    //   1294: invokevirtual getMargin : ()I
    //   1297: i2f
    //   1298: fadd
    //   1299: fstore #21
    //   1301: aload_0
    //   1302: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1305: iload_3
    //   1306: aaload
    //   1307: invokevirtual getResolutionNode : ()Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;
    //   1310: aload #9
    //   1312: getfield resolvedTarget : Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;
    //   1315: fload #21
    //   1317: invokevirtual resolve : (Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;F)V
    //   1320: aload_0
    //   1321: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1324: iload #17
    //   1326: aaload
    //   1327: invokevirtual getResolutionNode : ()Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;
    //   1330: astore #7
    //   1332: aload #9
    //   1334: getfield resolvedTarget : Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;
    //   1337: astore #5
    //   1339: fload #21
    //   1341: fload #22
    //   1343: fadd
    //   1344: fstore #21
    //   1346: aload #7
    //   1348: aload #5
    //   1350: fload #21
    //   1352: invokevirtual resolve : (Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;F)V
    //   1355: aload_0
    //   1356: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1359: iload_3
    //   1360: aaload
    //   1361: invokevirtual getResolutionNode : ()Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;
    //   1364: aload_1
    //   1365: invokevirtual addResolvedValue : (Landroidx/constraintlayout/solver/LinearSystem;)V
    //   1368: aload_0
    //   1369: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1372: iload #17
    //   1374: aaload
    //   1375: invokevirtual getResolutionNode : ()Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;
    //   1378: aload_1
    //   1379: invokevirtual addResolvedValue : (Landroidx/constraintlayout/solver/LinearSystem;)V
    //   1382: fload #21
    //   1384: aload_0
    //   1385: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1388: iload #17
    //   1390: aaload
    //   1391: invokevirtual getMargin : ()I
    //   1394: i2f
    //   1395: fadd
    //   1396: fstore #22
    //   1398: aload #4
    //   1400: astore_0
    //   1401: fload #22
    //   1403: fstore #21
    //   1405: goto -> 1178
    //   1408: iload #13
    //   1410: ifne -> 1424
    //   1413: iload #15
    //   1415: ifeq -> 1421
    //   1418: goto -> 1424
    //   1421: goto -> 1861
    //   1424: iload #13
    //   1426: ifeq -> 1439
    //   1429: fload #24
    //   1431: fload #21
    //   1433: fsub
    //   1434: fstore #22
    //   1436: goto -> 1451
    //   1439: fload #24
    //   1441: fstore #22
    //   1443: iload #15
    //   1445: ifeq -> 1451
    //   1448: goto -> 1429
    //   1451: fload #22
    //   1453: iload #14
    //   1455: iconst_1
    //   1456: iadd
    //   1457: i2f
    //   1458: fdiv
    //   1459: fstore #19
    //   1461: iload #15
    //   1463: ifeq -> 1492
    //   1466: iload #14
    //   1468: iconst_1
    //   1469: if_icmple -> 1482
    //   1472: iload #14
    //   1474: iconst_1
    //   1475: isub
    //   1476: i2f
    //   1477: fstore #21
    //   1479: goto -> 1485
    //   1482: fconst_2
    //   1483: fstore #21
    //   1485: fload #22
    //   1487: fload #21
    //   1489: fdiv
    //   1490: fstore #19
    //   1492: aload #5
    //   1494: invokevirtual getVisibility : ()I
    //   1497: bipush #8
    //   1499: if_icmpeq -> 1512
    //   1502: fload #23
    //   1504: fload #19
    //   1506: fadd
    //   1507: fstore #21
    //   1509: goto -> 1516
    //   1512: fload #23
    //   1514: fstore #21
    //   1516: fload #21
    //   1518: fstore #22
    //   1520: iload #15
    //   1522: ifeq -> 1551
    //   1525: fload #21
    //   1527: fstore #22
    //   1529: iload #14
    //   1531: iconst_1
    //   1532: if_icmple -> 1551
    //   1535: aload #7
    //   1537: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1540: iload_3
    //   1541: aaload
    //   1542: invokevirtual getMargin : ()I
    //   1545: i2f
    //   1546: fload #23
    //   1548: fadd
    //   1549: fstore #22
    //   1551: aload #5
    //   1553: astore_0
    //   1554: fload #22
    //   1556: fstore #21
    //   1558: iload #13
    //   1560: ifeq -> 1594
    //   1563: aload #5
    //   1565: astore_0
    //   1566: fload #22
    //   1568: fstore #21
    //   1570: aload #7
    //   1572: ifnull -> 1594
    //   1575: fload #22
    //   1577: aload #7
    //   1579: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1582: iload_3
    //   1583: aaload
    //   1584: invokevirtual getMargin : ()I
    //   1587: i2f
    //   1588: fadd
    //   1589: fstore #21
    //   1591: aload #5
    //   1593: astore_0
    //   1594: aload_0
    //   1595: ifnull -> 1421
    //   1598: getstatic androidx/constraintlayout/solver/LinearSystem.sMetrics : Landroidx/constraintlayout/solver/Metrics;
    //   1601: ifnull -> 1655
    //   1604: getstatic androidx/constraintlayout/solver/LinearSystem.sMetrics : Landroidx/constraintlayout/solver/Metrics;
    //   1607: astore #4
    //   1609: aload #4
    //   1611: aload #4
    //   1613: getfield nonresolvedWidgets : J
    //   1616: lconst_1
    //   1617: lsub
    //   1618: putfield nonresolvedWidgets : J
    //   1621: getstatic androidx/constraintlayout/solver/LinearSystem.sMetrics : Landroidx/constraintlayout/solver/Metrics;
    //   1624: astore #4
    //   1626: aload #4
    //   1628: aload #4
    //   1630: getfield resolvedWidgets : J
    //   1633: lconst_1
    //   1634: ladd
    //   1635: putfield resolvedWidgets : J
    //   1638: getstatic androidx/constraintlayout/solver/LinearSystem.sMetrics : Landroidx/constraintlayout/solver/Metrics;
    //   1641: astore #4
    //   1643: aload #4
    //   1645: aload #4
    //   1647: getfield chainConnectionResolved : J
    //   1650: lconst_1
    //   1651: ladd
    //   1652: putfield chainConnectionResolved : J
    //   1655: aload_0
    //   1656: getfield mNextChainWidget : [Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   1659: iload_2
    //   1660: aaload
    //   1661: astore #4
    //   1663: aload #4
    //   1665: ifnonnull -> 1688
    //   1668: fload #21
    //   1670: fstore #22
    //   1672: aload_0
    //   1673: aload #6
    //   1675: if_acmpne -> 1681
    //   1678: goto -> 1688
    //   1681: fload #22
    //   1683: fstore #21
    //   1685: goto -> 1855
    //   1688: iload_2
    //   1689: ifne -> 1701
    //   1692: aload_0
    //   1693: invokevirtual getWidth : ()I
    //   1696: istore #12
    //   1698: goto -> 1707
    //   1701: aload_0
    //   1702: invokevirtual getHeight : ()I
    //   1705: istore #12
    //   1707: iload #12
    //   1709: i2f
    //   1710: fstore #23
    //   1712: fload #21
    //   1714: fstore #22
    //   1716: aload_0
    //   1717: aload #7
    //   1719: if_acmpeq -> 1737
    //   1722: fload #21
    //   1724: aload_0
    //   1725: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1728: iload_3
    //   1729: aaload
    //   1730: invokevirtual getMargin : ()I
    //   1733: i2f
    //   1734: fadd
    //   1735: fstore #22
    //   1737: aload_0
    //   1738: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1741: iload_3
    //   1742: aaload
    //   1743: invokevirtual getResolutionNode : ()Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;
    //   1746: aload #9
    //   1748: getfield resolvedTarget : Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;
    //   1751: fload #22
    //   1753: invokevirtual resolve : (Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;F)V
    //   1756: aload_0
    //   1757: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1760: iload #17
    //   1762: aaload
    //   1763: invokevirtual getResolutionNode : ()Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;
    //   1766: aload #9
    //   1768: getfield resolvedTarget : Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;
    //   1771: fload #22
    //   1773: fload #23
    //   1775: fadd
    //   1776: invokevirtual resolve : (Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;F)V
    //   1779: aload_0
    //   1780: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1783: iload_3
    //   1784: aaload
    //   1785: invokevirtual getResolutionNode : ()Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;
    //   1788: aload_1
    //   1789: invokevirtual addResolvedValue : (Landroidx/constraintlayout/solver/LinearSystem;)V
    //   1792: aload_0
    //   1793: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1796: iload #17
    //   1798: aaload
    //   1799: invokevirtual getResolutionNode : ()Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;
    //   1802: aload_1
    //   1803: invokevirtual addResolvedValue : (Landroidx/constraintlayout/solver/LinearSystem;)V
    //   1806: fload #22
    //   1808: fload #23
    //   1810: aload_0
    //   1811: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1814: iload #17
    //   1816: aaload
    //   1817: invokevirtual getMargin : ()I
    //   1820: i2f
    //   1821: fadd
    //   1822: fadd
    //   1823: fstore #23
    //   1825: fload #23
    //   1827: fstore #22
    //   1829: aload #4
    //   1831: ifnull -> 1681
    //   1834: fload #23
    //   1836: fstore #21
    //   1838: aload #4
    //   1840: invokevirtual getVisibility : ()I
    //   1843: bipush #8
    //   1845: if_icmpeq -> 1855
    //   1848: fload #23
    //   1850: fload #19
    //   1852: fadd
    //   1853: fstore #21
    //   1855: aload #4
    //   1857: astore_0
    //   1858: goto -> 1594
    //   1861: iconst_1
    //   1862: ireturn
    //   1863: iconst_0
    //   1864: ireturn
    //   1865: iconst_0
    //   1866: ireturn
  }
  
  static void checkMatchParent(ConstraintWidgetContainer paramConstraintWidgetContainer, LinearSystem paramLinearSystem, ConstraintWidget paramConstraintWidget) {
    if (paramConstraintWidgetContainer.mListDimensionBehaviors[0] != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && paramConstraintWidget.mListDimensionBehaviors[0] == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
      int i = paramConstraintWidget.mLeft.mMargin;
      int j = paramConstraintWidgetContainer.getWidth() - paramConstraintWidget.mRight.mMargin;
      paramConstraintWidget.mLeft.mSolverVariable = paramLinearSystem.createObjectVariable(paramConstraintWidget.mLeft);
      paramConstraintWidget.mRight.mSolverVariable = paramLinearSystem.createObjectVariable(paramConstraintWidget.mRight);
      paramLinearSystem.addEquality(paramConstraintWidget.mLeft.mSolverVariable, i);
      paramLinearSystem.addEquality(paramConstraintWidget.mRight.mSolverVariable, j);
      paramConstraintWidget.mHorizontalResolution = 2;
      paramConstraintWidget.setHorizontalDimension(i, j);
    } 
    if (paramConstraintWidgetContainer.mListDimensionBehaviors[1] != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && paramConstraintWidget.mListDimensionBehaviors[1] == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
      int j = paramConstraintWidget.mTop.mMargin;
      int i = paramConstraintWidgetContainer.getHeight() - paramConstraintWidget.mBottom.mMargin;
      paramConstraintWidget.mTop.mSolverVariable = paramLinearSystem.createObjectVariable(paramConstraintWidget.mTop);
      paramConstraintWidget.mBottom.mSolverVariable = paramLinearSystem.createObjectVariable(paramConstraintWidget.mBottom);
      paramLinearSystem.addEquality(paramConstraintWidget.mTop.mSolverVariable, j);
      paramLinearSystem.addEquality(paramConstraintWidget.mBottom.mSolverVariable, i);
      if (paramConstraintWidget.mBaselineDistance > 0 || paramConstraintWidget.getVisibility() == 8) {
        paramConstraintWidget.mBaseline.mSolverVariable = paramLinearSystem.createObjectVariable(paramConstraintWidget.mBaseline);
        paramLinearSystem.addEquality(paramConstraintWidget.mBaseline.mSolverVariable, paramConstraintWidget.mBaselineDistance + j);
      } 
      paramConstraintWidget.mVerticalResolution = 2;
      paramConstraintWidget.setVerticalDimension(j, i);
    } 
  }
  
  private static boolean optimizableMatchConstraint(ConstraintWidget paramConstraintWidget, int paramInt) {
    ConstraintWidget.DimensionBehaviour[] arrayOfDimensionBehaviour;
    if (paramConstraintWidget.mListDimensionBehaviors[paramInt] != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT)
      return false; 
    float f = paramConstraintWidget.mDimensionRatio;
    boolean bool = true;
    if (f != 0.0F) {
      arrayOfDimensionBehaviour = paramConstraintWidget.mListDimensionBehaviors;
      if (paramInt == 0) {
        paramInt = bool;
      } else {
        paramInt = 0;
      } 
      if (arrayOfDimensionBehaviour[paramInt] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT);
      return false;
    } 
    if (paramInt == 0) {
      if (((ConstraintWidget)arrayOfDimensionBehaviour).mMatchConstraintDefaultWidth != 0)
        return false; 
      if (((ConstraintWidget)arrayOfDimensionBehaviour).mMatchConstraintMinWidth != 0 || ((ConstraintWidget)arrayOfDimensionBehaviour).mMatchConstraintMaxWidth != 0)
        return false; 
    } else {
      if (((ConstraintWidget)arrayOfDimensionBehaviour).mMatchConstraintDefaultHeight != 0)
        return false; 
      if (((ConstraintWidget)arrayOfDimensionBehaviour).mMatchConstraintMinHeight != 0 || ((ConstraintWidget)arrayOfDimensionBehaviour).mMatchConstraintMaxHeight != 0)
        return false; 
    } 
    return true;
  }
  
  static void setOptimizedWidget(ConstraintWidget paramConstraintWidget, int paramInt1, int paramInt2) {
    int i = paramInt1 * 2;
    int j = i + 1;
    (paramConstraintWidget.mListAnchors[i].getResolutionNode()).resolvedTarget = (paramConstraintWidget.getParent()).mLeft.getResolutionNode();
    (paramConstraintWidget.mListAnchors[i].getResolutionNode()).resolvedOffset = paramInt2;
    (paramConstraintWidget.mListAnchors[i].getResolutionNode()).state = 1;
    (paramConstraintWidget.mListAnchors[j].getResolutionNode()).resolvedTarget = paramConstraintWidget.mListAnchors[i].getResolutionNode();
    (paramConstraintWidget.mListAnchors[j].getResolutionNode()).resolvedOffset = paramConstraintWidget.getLength(paramInt1);
    (paramConstraintWidget.mListAnchors[j].getResolutionNode()).state = 1;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/androidx/constraintlayout/solver/widgets/Optimizer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */