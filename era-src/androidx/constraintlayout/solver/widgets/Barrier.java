package androidx.constraintlayout.solver.widgets;

import androidx.constraintlayout.solver.LinearSystem;
import java.util.ArrayList;

public class Barrier extends Helper {
  public static final int BOTTOM = 3;
  
  public static final int LEFT = 0;
  
  public static final int RIGHT = 1;
  
  public static final int TOP = 2;
  
  private boolean mAllowsGoneWidget = true;
  
  private int mBarrierType = 0;
  
  private ArrayList<ResolutionAnchor> mNodes = new ArrayList<ResolutionAnchor>(4);
  
  public void addToSolver(LinearSystem paramLinearSystem) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   4: iconst_0
    //   5: aload_0
    //   6: getfield mLeft : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   9: aastore
    //   10: aload_0
    //   11: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   14: iconst_2
    //   15: aload_0
    //   16: getfield mTop : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   19: aastore
    //   20: aload_0
    //   21: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   24: iconst_1
    //   25: aload_0
    //   26: getfield mRight : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   29: aastore
    //   30: aload_0
    //   31: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   34: iconst_3
    //   35: aload_0
    //   36: getfield mBottom : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   39: aastore
    //   40: iconst_0
    //   41: istore_2
    //   42: iload_2
    //   43: aload_0
    //   44: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   47: arraylength
    //   48: if_icmpge -> 76
    //   51: aload_0
    //   52: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   55: iload_2
    //   56: aaload
    //   57: aload_1
    //   58: aload_0
    //   59: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   62: iload_2
    //   63: aaload
    //   64: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroidx/constraintlayout/solver/SolverVariable;
    //   67: putfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   70: iinc #2, 1
    //   73: goto -> 42
    //   76: aload_0
    //   77: getfield mBarrierType : I
    //   80: istore_2
    //   81: iload_2
    //   82: iflt -> 619
    //   85: iload_2
    //   86: iconst_4
    //   87: if_icmpge -> 619
    //   90: aload_0
    //   91: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   94: aload_0
    //   95: getfield mBarrierType : I
    //   98: aaload
    //   99: astore_3
    //   100: iconst_0
    //   101: istore_2
    //   102: iload_2
    //   103: aload_0
    //   104: getfield mWidgetsCount : I
    //   107: if_icmpge -> 208
    //   110: aload_0
    //   111: getfield mWidgets : [Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   114: iload_2
    //   115: aaload
    //   116: astore #4
    //   118: aload_0
    //   119: getfield mAllowsGoneWidget : Z
    //   122: ifne -> 136
    //   125: aload #4
    //   127: invokevirtual allowedInBarrier : ()Z
    //   130: ifne -> 136
    //   133: goto -> 202
    //   136: aload_0
    //   137: getfield mBarrierType : I
    //   140: istore #5
    //   142: iload #5
    //   144: ifeq -> 153
    //   147: iload #5
    //   149: iconst_1
    //   150: if_icmpne -> 170
    //   153: aload #4
    //   155: invokevirtual getHorizontalDimensionBehaviour : ()Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   158: getstatic androidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   161: if_acmpne -> 170
    //   164: iconst_1
    //   165: istore #6
    //   167: goto -> 211
    //   170: aload_0
    //   171: getfield mBarrierType : I
    //   174: istore #5
    //   176: iload #5
    //   178: iconst_2
    //   179: if_icmpeq -> 188
    //   182: iload #5
    //   184: iconst_3
    //   185: if_icmpne -> 202
    //   188: aload #4
    //   190: invokevirtual getVerticalDimensionBehaviour : ()Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   193: getstatic androidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   196: if_acmpne -> 202
    //   199: goto -> 164
    //   202: iinc #2, 1
    //   205: goto -> 102
    //   208: iconst_0
    //   209: istore #6
    //   211: aload_0
    //   212: getfield mBarrierType : I
    //   215: istore_2
    //   216: iload_2
    //   217: ifeq -> 244
    //   220: iload_2
    //   221: iconst_1
    //   222: if_icmpne -> 228
    //   225: goto -> 244
    //   228: aload_0
    //   229: invokevirtual getParent : ()Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   232: invokevirtual getVerticalDimensionBehaviour : ()Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   235: getstatic androidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   238: if_acmpne -> 260
    //   241: goto -> 257
    //   244: aload_0
    //   245: invokevirtual getParent : ()Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   248: invokevirtual getHorizontalDimensionBehaviour : ()Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   251: getstatic androidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   254: if_acmpne -> 260
    //   257: iconst_0
    //   258: istore #6
    //   260: iconst_0
    //   261: istore_2
    //   262: iload_2
    //   263: aload_0
    //   264: getfield mWidgetsCount : I
    //   267: if_icmpge -> 382
    //   270: aload_0
    //   271: getfield mWidgets : [Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   274: iload_2
    //   275: aaload
    //   276: astore #7
    //   278: aload_0
    //   279: getfield mAllowsGoneWidget : Z
    //   282: ifne -> 296
    //   285: aload #7
    //   287: invokevirtual allowedInBarrier : ()Z
    //   290: ifne -> 296
    //   293: goto -> 376
    //   296: aload_1
    //   297: aload #7
    //   299: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   302: aload_0
    //   303: getfield mBarrierType : I
    //   306: aaload
    //   307: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroidx/constraintlayout/solver/SolverVariable;
    //   310: astore #4
    //   312: aload #7
    //   314: getfield mListAnchors : [Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   317: astore #7
    //   319: aload_0
    //   320: getfield mBarrierType : I
    //   323: istore #5
    //   325: aload #7
    //   327: iload #5
    //   329: aaload
    //   330: aload #4
    //   332: putfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   335: iload #5
    //   337: ifeq -> 364
    //   340: iload #5
    //   342: iconst_2
    //   343: if_icmpne -> 349
    //   346: goto -> 364
    //   349: aload_1
    //   350: aload_3
    //   351: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   354: aload #4
    //   356: iload #6
    //   358: invokevirtual addGreaterBarrier : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;Z)V
    //   361: goto -> 376
    //   364: aload_1
    //   365: aload_3
    //   366: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   369: aload #4
    //   371: iload #6
    //   373: invokevirtual addLowerBarrier : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;Z)V
    //   376: iinc #2, 1
    //   379: goto -> 262
    //   382: aload_0
    //   383: getfield mBarrierType : I
    //   386: istore_2
    //   387: iload_2
    //   388: ifne -> 445
    //   391: aload_1
    //   392: aload_0
    //   393: getfield mRight : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   396: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   399: aload_0
    //   400: getfield mLeft : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   403: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   406: iconst_0
    //   407: bipush #6
    //   409: invokevirtual addEquality : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)Landroidx/constraintlayout/solver/ArrayRow;
    //   412: pop
    //   413: iload #6
    //   415: ifne -> 619
    //   418: aload_1
    //   419: aload_0
    //   420: getfield mLeft : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   423: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   426: aload_0
    //   427: getfield mParent : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   430: getfield mRight : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   433: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   436: iconst_0
    //   437: iconst_5
    //   438: invokevirtual addEquality : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)Landroidx/constraintlayout/solver/ArrayRow;
    //   441: pop
    //   442: goto -> 619
    //   445: iload_2
    //   446: iconst_1
    //   447: if_icmpne -> 504
    //   450: aload_1
    //   451: aload_0
    //   452: getfield mLeft : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   455: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   458: aload_0
    //   459: getfield mRight : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   462: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   465: iconst_0
    //   466: bipush #6
    //   468: invokevirtual addEquality : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)Landroidx/constraintlayout/solver/ArrayRow;
    //   471: pop
    //   472: iload #6
    //   474: ifne -> 619
    //   477: aload_1
    //   478: aload_0
    //   479: getfield mLeft : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   482: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   485: aload_0
    //   486: getfield mParent : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   489: getfield mLeft : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   492: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   495: iconst_0
    //   496: iconst_5
    //   497: invokevirtual addEquality : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)Landroidx/constraintlayout/solver/ArrayRow;
    //   500: pop
    //   501: goto -> 619
    //   504: iload_2
    //   505: iconst_2
    //   506: if_icmpne -> 563
    //   509: aload_1
    //   510: aload_0
    //   511: getfield mBottom : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   514: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   517: aload_0
    //   518: getfield mTop : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   521: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   524: iconst_0
    //   525: bipush #6
    //   527: invokevirtual addEquality : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)Landroidx/constraintlayout/solver/ArrayRow;
    //   530: pop
    //   531: iload #6
    //   533: ifne -> 619
    //   536: aload_1
    //   537: aload_0
    //   538: getfield mTop : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   541: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   544: aload_0
    //   545: getfield mParent : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   548: getfield mBottom : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   551: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   554: iconst_0
    //   555: iconst_5
    //   556: invokevirtual addEquality : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)Landroidx/constraintlayout/solver/ArrayRow;
    //   559: pop
    //   560: goto -> 619
    //   563: iload_2
    //   564: iconst_3
    //   565: if_icmpne -> 619
    //   568: aload_1
    //   569: aload_0
    //   570: getfield mTop : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   573: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   576: aload_0
    //   577: getfield mBottom : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   580: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   583: iconst_0
    //   584: bipush #6
    //   586: invokevirtual addEquality : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)Landroidx/constraintlayout/solver/ArrayRow;
    //   589: pop
    //   590: iload #6
    //   592: ifne -> 619
    //   595: aload_1
    //   596: aload_0
    //   597: getfield mTop : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   600: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   603: aload_0
    //   604: getfield mParent : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   607: getfield mTop : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   610: getfield mSolverVariable : Landroidx/constraintlayout/solver/SolverVariable;
    //   613: iconst_0
    //   614: iconst_5
    //   615: invokevirtual addEquality : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)Landroidx/constraintlayout/solver/ArrayRow;
    //   618: pop
    //   619: return
  }
  
  public boolean allowedInBarrier() {
    return true;
  }
  
  public boolean allowsGoneWidget() {
    return this.mAllowsGoneWidget;
  }
  
  public void analyze(int paramInt) {
    ResolutionAnchor resolutionAnchor;
    if (this.mParent == null)
      return; 
    if (!((ConstraintWidgetContainer)this.mParent).optimizeFor(2))
      return; 
    paramInt = this.mBarrierType;
    if (paramInt != 0) {
      if (paramInt != 1) {
        if (paramInt != 2) {
          if (paramInt != 3)
            return; 
          resolutionAnchor = this.mBottom.getResolutionNode();
        } else {
          resolutionAnchor = this.mTop.getResolutionNode();
        } 
      } else {
        resolutionAnchor = this.mRight.getResolutionNode();
      } 
    } else {
      resolutionAnchor = this.mLeft.getResolutionNode();
    } 
    resolutionAnchor.setType(5);
    paramInt = this.mBarrierType;
    if (paramInt == 0 || paramInt == 1) {
      this.mTop.getResolutionNode().resolve((ResolutionAnchor)null, 0.0F);
      this.mBottom.getResolutionNode().resolve((ResolutionAnchor)null, 0.0F);
    } else {
      this.mLeft.getResolutionNode().resolve((ResolutionAnchor)null, 0.0F);
      this.mRight.getResolutionNode().resolve((ResolutionAnchor)null, 0.0F);
    } 
    this.mNodes.clear();
    for (paramInt = 0; paramInt < this.mWidgetsCount; paramInt++) {
      ConstraintWidget constraintWidget = this.mWidgets[paramInt];
      if (this.mAllowsGoneWidget || constraintWidget.allowedInBarrier()) {
        ResolutionAnchor resolutionAnchor1;
        int i = this.mBarrierType;
        if (i != 0) {
          if (i != 1) {
            if (i != 2) {
              if (i != 3) {
                constraintWidget = null;
              } else {
                resolutionAnchor1 = constraintWidget.mBottom.getResolutionNode();
              } 
            } else {
              resolutionAnchor1 = ((ConstraintWidget)resolutionAnchor1).mTop.getResolutionNode();
            } 
          } else {
            resolutionAnchor1 = ((ConstraintWidget)resolutionAnchor1).mRight.getResolutionNode();
          } 
        } else {
          resolutionAnchor1 = ((ConstraintWidget)resolutionAnchor1).mLeft.getResolutionNode();
        } 
        if (resolutionAnchor1 != null) {
          this.mNodes.add(resolutionAnchor1);
          resolutionAnchor1.addDependent(resolutionAnchor);
        } 
      } 
    } 
  }
  
  public void resetResolutionNodes() {
    super.resetResolutionNodes();
    this.mNodes.clear();
  }
  
  public void resolve() {
    int i = this.mBarrierType;
    float f1 = Float.MAX_VALUE;
    if (i != 0) {
      if (i != 1) {
        if (i != 2) {
          if (i != 3)
            return; 
          ResolutionAnchor resolutionAnchor = this.mBottom.getResolutionNode();
        } else {
          ResolutionAnchor resolutionAnchor = this.mTop.getResolutionNode();
          int k = this.mNodes.size();
          Object object1 = null;
          i = 0;
          float f = f1;
        } 
      } else {
        ResolutionAnchor resolutionAnchor = this.mRight.getResolutionNode();
      } 
      f1 = 0.0F;
    } else {
      ResolutionAnchor resolutionAnchor = this.mLeft.getResolutionNode();
    } 
    int j = this.mNodes.size();
    Object object = null;
    i = 0;
    float f2 = f1;
  }
  
  public void setAllowsGoneWidget(boolean paramBoolean) {
    this.mAllowsGoneWidget = paramBoolean;
  }
  
  public void setBarrierType(int paramInt) {
    this.mBarrierType = paramInt;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/androidx/constraintlayout/solver/widgets/Barrier.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */