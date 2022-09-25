package androidx.constraintlayout.solver.widgets;

import androidx.constraintlayout.solver.Cache;
import androidx.constraintlayout.solver.LinearSystem;
import androidx.constraintlayout.solver.SolverVariable;
import java.util.ArrayList;

public class ConstraintWidget {
  protected static final int ANCHOR_BASELINE = 4;
  
  protected static final int ANCHOR_BOTTOM = 3;
  
  protected static final int ANCHOR_LEFT = 0;
  
  protected static final int ANCHOR_RIGHT = 1;
  
  protected static final int ANCHOR_TOP = 2;
  
  private static final boolean AUTOTAG_CENTER = false;
  
  public static final int CHAIN_PACKED = 2;
  
  public static final int CHAIN_SPREAD = 0;
  
  public static final int CHAIN_SPREAD_INSIDE = 1;
  
  public static float DEFAULT_BIAS = 0.5F;
  
  static final int DIMENSION_HORIZONTAL = 0;
  
  static final int DIMENSION_VERTICAL = 1;
  
  protected static final int DIRECT = 2;
  
  public static final int GONE = 8;
  
  public static final int HORIZONTAL = 0;
  
  public static final int INVISIBLE = 4;
  
  public static final int MATCH_CONSTRAINT_PERCENT = 2;
  
  public static final int MATCH_CONSTRAINT_RATIO = 3;
  
  public static final int MATCH_CONSTRAINT_RATIO_RESOLVED = 4;
  
  public static final int MATCH_CONSTRAINT_SPREAD = 0;
  
  public static final int MATCH_CONSTRAINT_WRAP = 1;
  
  protected static final int SOLVER = 1;
  
  public static final int UNKNOWN = -1;
  
  public static final int VERTICAL = 1;
  
  public static final int VISIBLE = 0;
  
  private static final int WRAP = -2;
  
  protected ArrayList<ConstraintAnchor> mAnchors = new ArrayList<ConstraintAnchor>();
  
  ConstraintAnchor mBaseline = new ConstraintAnchor(this, ConstraintAnchor.Type.BASELINE);
  
  int mBaselineDistance = 0;
  
  ConstraintWidgetGroup mBelongingGroup = null;
  
  ConstraintAnchor mBottom = new ConstraintAnchor(this, ConstraintAnchor.Type.BOTTOM);
  
  boolean mBottomHasCentered;
  
  ConstraintAnchor mCenter = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER);
  
  ConstraintAnchor mCenterX = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER_X);
  
  ConstraintAnchor mCenterY = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER_Y);
  
  private float mCircleConstraintAngle = 0.0F;
  
  private Object mCompanionWidget;
  
  private int mContainerItemSkip;
  
  private String mDebugName;
  
  protected float mDimensionRatio = 0.0F;
  
  protected int mDimensionRatioSide = -1;
  
  int mDistToBottom;
  
  int mDistToLeft;
  
  int mDistToRight;
  
  int mDistToTop;
  
  private int mDrawHeight = 0;
  
  private int mDrawWidth = 0;
  
  private int mDrawX = 0;
  
  private int mDrawY = 0;
  
  boolean mGroupsToSolver;
  
  int mHeight = 0;
  
  float mHorizontalBiasPercent;
  
  boolean mHorizontalChainFixedPosition;
  
  int mHorizontalChainStyle;
  
  ConstraintWidget mHorizontalNextWidget;
  
  public int mHorizontalResolution = -1;
  
  boolean mHorizontalWrapVisited;
  
  boolean mIsHeightWrapContent;
  
  boolean mIsWidthWrapContent;
  
  ConstraintAnchor mLeft = new ConstraintAnchor(this, ConstraintAnchor.Type.LEFT);
  
  boolean mLeftHasCentered;
  
  protected ConstraintAnchor[] mListAnchors = new ConstraintAnchor[] { this.mLeft, this.mRight, this.mTop, this.mBottom, this.mBaseline, this.mCenter };
  
  protected DimensionBehaviour[] mListDimensionBehaviors = new DimensionBehaviour[] { DimensionBehaviour.FIXED, DimensionBehaviour.FIXED };
  
  protected ConstraintWidget[] mListNextMatchConstraintsWidget;
  
  int mMatchConstraintDefaultHeight = 0;
  
  int mMatchConstraintDefaultWidth = 0;
  
  int mMatchConstraintMaxHeight = 0;
  
  int mMatchConstraintMaxWidth = 0;
  
  int mMatchConstraintMinHeight = 0;
  
  int mMatchConstraintMinWidth = 0;
  
  float mMatchConstraintPercentHeight = 1.0F;
  
  float mMatchConstraintPercentWidth = 1.0F;
  
  private int[] mMaxDimension = new int[] { Integer.MAX_VALUE, Integer.MAX_VALUE };
  
  protected int mMinHeight;
  
  protected int mMinWidth;
  
  protected ConstraintWidget[] mNextChainWidget;
  
  protected int mOffsetX = 0;
  
  protected int mOffsetY = 0;
  
  boolean mOptimizerMeasurable;
  
  boolean mOptimizerMeasured;
  
  ConstraintWidget mParent = null;
  
  int mRelX = 0;
  
  int mRelY = 0;
  
  ResolutionDimension mResolutionHeight;
  
  ResolutionDimension mResolutionWidth;
  
  float mResolvedDimensionRatio = 1.0F;
  
  int mResolvedDimensionRatioSide = -1;
  
  int[] mResolvedMatchConstraintDefault = new int[2];
  
  ConstraintAnchor mRight = new ConstraintAnchor(this, ConstraintAnchor.Type.RIGHT);
  
  boolean mRightHasCentered;
  
  ConstraintAnchor mTop = new ConstraintAnchor(this, ConstraintAnchor.Type.TOP);
  
  boolean mTopHasCentered;
  
  private String mType;
  
  float mVerticalBiasPercent;
  
  boolean mVerticalChainFixedPosition;
  
  int mVerticalChainStyle;
  
  ConstraintWidget mVerticalNextWidget;
  
  public int mVerticalResolution = -1;
  
  boolean mVerticalWrapVisited;
  
  private int mVisibility;
  
  float[] mWeight;
  
  int mWidth = 0;
  
  private int mWrapHeight;
  
  private int mWrapWidth;
  
  protected int mX = 0;
  
  protected int mY = 0;
  
  public ConstraintWidget() {
    float f = DEFAULT_BIAS;
    this.mHorizontalBiasPercent = f;
    this.mVerticalBiasPercent = f;
    this.mContainerItemSkip = 0;
    this.mVisibility = 0;
    this.mDebugName = null;
    this.mType = null;
    this.mOptimizerMeasurable = false;
    this.mOptimizerMeasured = false;
    this.mGroupsToSolver = false;
    this.mHorizontalChainStyle = 0;
    this.mVerticalChainStyle = 0;
    this.mWeight = new float[] { -1.0F, -1.0F };
    this.mListNextMatchConstraintsWidget = new ConstraintWidget[] { null, null };
    this.mNextChainWidget = new ConstraintWidget[] { null, null };
    this.mHorizontalNextWidget = null;
    this.mVerticalNextWidget = null;
    addAnchors();
  }
  
  public ConstraintWidget(int paramInt1, int paramInt2) {
    this(0, 0, paramInt1, paramInt2);
  }
  
  public ConstraintWidget(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    float f = DEFAULT_BIAS;
    this.mHorizontalBiasPercent = f;
    this.mVerticalBiasPercent = f;
    this.mContainerItemSkip = 0;
    this.mVisibility = 0;
    this.mDebugName = null;
    this.mType = null;
    this.mOptimizerMeasurable = false;
    this.mOptimizerMeasured = false;
    this.mGroupsToSolver = false;
    this.mHorizontalChainStyle = 0;
    this.mVerticalChainStyle = 0;
    this.mWeight = new float[] { -1.0F, -1.0F };
    this.mListNextMatchConstraintsWidget = new ConstraintWidget[] { null, null };
    this.mNextChainWidget = new ConstraintWidget[] { null, null };
    this.mHorizontalNextWidget = null;
    this.mVerticalNextWidget = null;
    this.mX = paramInt1;
    this.mY = paramInt2;
    this.mWidth = paramInt3;
    this.mHeight = paramInt4;
    addAnchors();
    forceUpdateDrawPosition();
  }
  
  private void addAnchors() {
    this.mAnchors.add(this.mLeft);
    this.mAnchors.add(this.mTop);
    this.mAnchors.add(this.mRight);
    this.mAnchors.add(this.mBottom);
    this.mAnchors.add(this.mCenterX);
    this.mAnchors.add(this.mCenterY);
    this.mAnchors.add(this.mCenter);
    this.mAnchors.add(this.mBaseline);
  }
  
  private void applyConstraints(LinearSystem paramLinearSystem, boolean paramBoolean1, SolverVariable paramSolverVariable1, SolverVariable paramSolverVariable2, DimensionBehaviour paramDimensionBehaviour, boolean paramBoolean2, ConstraintAnchor paramConstraintAnchor1, ConstraintAnchor paramConstraintAnchor2, int paramInt1, int paramInt2, int paramInt3, int paramInt4, float paramFloat1, boolean paramBoolean3, boolean paramBoolean4, int paramInt5, int paramInt6, int paramInt7, float paramFloat2, boolean paramBoolean5) {
    // Byte code:
    //   0: aload_1
    //   1: aload #7
    //   3: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroidx/constraintlayout/solver/SolverVariable;
    //   6: astore #21
    //   8: aload_1
    //   9: aload #8
    //   11: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroidx/constraintlayout/solver/SolverVariable;
    //   14: astore #22
    //   16: aload_1
    //   17: aload #7
    //   19: invokevirtual getTarget : ()Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   22: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroidx/constraintlayout/solver/SolverVariable;
    //   25: astore #23
    //   27: aload_1
    //   28: aload #8
    //   30: invokevirtual getTarget : ()Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   33: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroidx/constraintlayout/solver/SolverVariable;
    //   36: astore #24
    //   38: aload_1
    //   39: getfield graphOptimizer : Z
    //   42: ifeq -> 128
    //   45: aload #7
    //   47: invokevirtual getResolutionNode : ()Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;
    //   50: getfield state : I
    //   53: iconst_1
    //   54: if_icmpne -> 128
    //   57: aload #8
    //   59: invokevirtual getResolutionNode : ()Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;
    //   62: getfield state : I
    //   65: iconst_1
    //   66: if_icmpne -> 128
    //   69: invokestatic getMetrics : ()Landroidx/constraintlayout/solver/Metrics;
    //   72: ifnull -> 89
    //   75: invokestatic getMetrics : ()Landroidx/constraintlayout/solver/Metrics;
    //   78: astore_3
    //   79: aload_3
    //   80: aload_3
    //   81: getfield resolvedWidgets : J
    //   84: lconst_1
    //   85: ladd
    //   86: putfield resolvedWidgets : J
    //   89: aload #7
    //   91: invokevirtual getResolutionNode : ()Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;
    //   94: aload_1
    //   95: invokevirtual addResolvedValue : (Landroidx/constraintlayout/solver/LinearSystem;)V
    //   98: aload #8
    //   100: invokevirtual getResolutionNode : ()Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;
    //   103: aload_1
    //   104: invokevirtual addResolvedValue : (Landroidx/constraintlayout/solver/LinearSystem;)V
    //   107: iload #15
    //   109: ifne -> 127
    //   112: iload_2
    //   113: ifeq -> 127
    //   116: aload_1
    //   117: aload #4
    //   119: aload #22
    //   121: iconst_0
    //   122: bipush #6
    //   124: invokevirtual addGreaterThan : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)V
    //   127: return
    //   128: invokestatic getMetrics : ()Landroidx/constraintlayout/solver/Metrics;
    //   131: ifnull -> 151
    //   134: invokestatic getMetrics : ()Landroidx/constraintlayout/solver/Metrics;
    //   137: astore #25
    //   139: aload #25
    //   141: aload #25
    //   143: getfield nonresolvedWidgets : J
    //   146: lconst_1
    //   147: ladd
    //   148: putfield nonresolvedWidgets : J
    //   151: aload #7
    //   153: invokevirtual isConnected : ()Z
    //   156: istore #26
    //   158: aload #8
    //   160: invokevirtual isConnected : ()Z
    //   163: istore #27
    //   165: aload_0
    //   166: getfield mCenter : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   169: invokevirtual isConnected : ()Z
    //   172: istore #28
    //   174: iload #26
    //   176: ifeq -> 185
    //   179: iconst_1
    //   180: istore #29
    //   182: goto -> 188
    //   185: iconst_0
    //   186: istore #29
    //   188: iload #29
    //   190: istore #30
    //   192: iload #27
    //   194: ifeq -> 203
    //   197: iload #29
    //   199: iconst_1
    //   200: iadd
    //   201: istore #30
    //   203: iload #30
    //   205: istore #29
    //   207: iload #28
    //   209: ifeq -> 218
    //   212: iload #30
    //   214: iconst_1
    //   215: iadd
    //   216: istore #29
    //   218: iload #14
    //   220: ifeq -> 229
    //   223: iconst_3
    //   224: istore #30
    //   226: goto -> 233
    //   229: iload #16
    //   231: istore #30
    //   233: getstatic androidx/constraintlayout/solver/widgets/ConstraintWidget$1.$SwitchMap$androidx$constraintlayout$solver$widgets$ConstraintWidget$DimensionBehaviour : [I
    //   236: aload #5
    //   238: invokevirtual ordinal : ()I
    //   241: iaload
    //   242: istore #16
    //   244: iload #16
    //   246: iconst_1
    //   247: if_icmpeq -> 268
    //   250: iload #16
    //   252: iconst_2
    //   253: if_icmpeq -> 268
    //   256: iload #16
    //   258: iconst_3
    //   259: if_icmpeq -> 268
    //   262: iload #16
    //   264: iconst_4
    //   265: if_icmpeq -> 274
    //   268: iconst_0
    //   269: istore #16
    //   271: goto -> 286
    //   274: iload #30
    //   276: iconst_4
    //   277: if_icmpne -> 283
    //   280: goto -> 268
    //   283: iconst_1
    //   284: istore #16
    //   286: aload_0
    //   287: getfield mVisibility : I
    //   290: bipush #8
    //   292: if_icmpne -> 304
    //   295: iconst_0
    //   296: istore #10
    //   298: iconst_0
    //   299: istore #16
    //   301: goto -> 304
    //   304: iload #20
    //   306: ifeq -> 364
    //   309: iload #26
    //   311: ifne -> 335
    //   314: iload #27
    //   316: ifne -> 335
    //   319: iload #28
    //   321: ifne -> 335
    //   324: aload_1
    //   325: aload #21
    //   327: iload #9
    //   329: invokevirtual addEquality : (Landroidx/constraintlayout/solver/SolverVariable;I)V
    //   332: goto -> 364
    //   335: iload #26
    //   337: ifeq -> 364
    //   340: iload #27
    //   342: ifne -> 364
    //   345: aload_1
    //   346: aload #21
    //   348: aload #23
    //   350: aload #7
    //   352: invokevirtual getMargin : ()I
    //   355: bipush #6
    //   357: invokevirtual addEquality : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)Landroidx/constraintlayout/solver/ArrayRow;
    //   360: pop
    //   361: goto -> 364
    //   364: iload #16
    //   366: ifne -> 448
    //   369: iload #6
    //   371: ifeq -> 424
    //   374: aload_1
    //   375: aload #22
    //   377: aload #21
    //   379: iconst_0
    //   380: iconst_3
    //   381: invokevirtual addEquality : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)Landroidx/constraintlayout/solver/ArrayRow;
    //   384: pop
    //   385: iload #11
    //   387: ifle -> 402
    //   390: aload_1
    //   391: aload #22
    //   393: aload #21
    //   395: iload #11
    //   397: bipush #6
    //   399: invokevirtual addGreaterThan : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)V
    //   402: iload #12
    //   404: ldc 2147483647
    //   406: if_icmpge -> 421
    //   409: aload_1
    //   410: aload #22
    //   412: aload #21
    //   414: iload #12
    //   416: bipush #6
    //   418: invokevirtual addLowerThan : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)V
    //   421: goto -> 437
    //   424: aload_1
    //   425: aload #22
    //   427: aload #21
    //   429: iload #10
    //   431: bipush #6
    //   433: invokevirtual addEquality : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)Landroidx/constraintlayout/solver/ArrayRow;
    //   436: pop
    //   437: iload #18
    //   439: istore #12
    //   441: iload #17
    //   443: istore #10
    //   445: goto -> 825
    //   448: iload #17
    //   450: bipush #-2
    //   452: if_icmpne -> 462
    //   455: iload #10
    //   457: istore #12
    //   459: goto -> 466
    //   462: iload #17
    //   464: istore #12
    //   466: iload #18
    //   468: istore #9
    //   470: iload #18
    //   472: bipush #-2
    //   474: if_icmpne -> 481
    //   477: iload #10
    //   479: istore #9
    //   481: iload #10
    //   483: istore #17
    //   485: iload #12
    //   487: ifle -> 511
    //   490: aload_1
    //   491: aload #22
    //   493: aload #21
    //   495: iload #12
    //   497: bipush #6
    //   499: invokevirtual addGreaterThan : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)V
    //   502: iload #10
    //   504: iload #12
    //   506: invokestatic max : (II)I
    //   509: istore #17
    //   511: iload #17
    //   513: istore #18
    //   515: iload #9
    //   517: ifle -> 541
    //   520: aload_1
    //   521: aload #22
    //   523: aload #21
    //   525: iload #9
    //   527: bipush #6
    //   529: invokevirtual addLowerThan : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)V
    //   532: iload #17
    //   534: iload #9
    //   536: invokestatic min : (II)I
    //   539: istore #18
    //   541: iload #30
    //   543: iconst_1
    //   544: if_icmpne -> 602
    //   547: iload_2
    //   548: ifeq -> 567
    //   551: aload_1
    //   552: aload #22
    //   554: aload #21
    //   556: iload #18
    //   558: bipush #6
    //   560: invokevirtual addEquality : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)Landroidx/constraintlayout/solver/ArrayRow;
    //   563: pop
    //   564: goto -> 735
    //   567: iload #15
    //   569: ifeq -> 587
    //   572: aload_1
    //   573: aload #22
    //   575: aload #21
    //   577: iload #18
    //   579: iconst_4
    //   580: invokevirtual addEquality : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)Landroidx/constraintlayout/solver/ArrayRow;
    //   583: pop
    //   584: goto -> 735
    //   587: aload_1
    //   588: aload #22
    //   590: aload #21
    //   592: iload #18
    //   594: iconst_1
    //   595: invokevirtual addEquality : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)Landroidx/constraintlayout/solver/ArrayRow;
    //   598: pop
    //   599: goto -> 735
    //   602: iload #30
    //   604: iconst_2
    //   605: if_icmpne -> 735
    //   608: aload #7
    //   610: invokevirtual getType : ()Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   613: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.TOP : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   616: if_acmpeq -> 672
    //   619: aload #7
    //   621: invokevirtual getType : ()Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   624: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.BOTTOM : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   627: if_acmpne -> 633
    //   630: goto -> 672
    //   633: aload_1
    //   634: aload_0
    //   635: getfield mParent : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   638: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.LEFT : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   641: invokevirtual getAnchor : (Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   644: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroidx/constraintlayout/solver/SolverVariable;
    //   647: astore #5
    //   649: aload_0
    //   650: getfield mParent : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   653: astore #25
    //   655: aload_1
    //   656: aload #25
    //   658: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.RIGHT : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   661: invokevirtual getAnchor : (Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   664: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroidx/constraintlayout/solver/SolverVariable;
    //   667: astore #25
    //   669: goto -> 708
    //   672: aload_1
    //   673: aload_0
    //   674: getfield mParent : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   677: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.TOP : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   680: invokevirtual getAnchor : (Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   683: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroidx/constraintlayout/solver/SolverVariable;
    //   686: astore #5
    //   688: aload_0
    //   689: getfield mParent : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   692: astore #25
    //   694: aload_1
    //   695: aload #25
    //   697: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.BOTTOM : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   700: invokevirtual getAnchor : (Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   703: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroidx/constraintlayout/solver/SolverVariable;
    //   706: astore #25
    //   708: aload_1
    //   709: aload_1
    //   710: invokevirtual createRow : ()Landroidx/constraintlayout/solver/ArrayRow;
    //   713: aload #22
    //   715: aload #21
    //   717: aload #25
    //   719: aload #5
    //   721: fload #19
    //   723: invokevirtual createRowDimensionRatio : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;F)Landroidx/constraintlayout/solver/ArrayRow;
    //   726: invokevirtual addConstraint : (Landroidx/constraintlayout/solver/ArrayRow;)V
    //   729: iconst_0
    //   730: istore #10
    //   732: goto -> 739
    //   735: iload #16
    //   737: istore #10
    //   739: iload #10
    //   741: ifeq -> 809
    //   744: iload #29
    //   746: iconst_2
    //   747: if_icmpeq -> 809
    //   750: iload #14
    //   752: ifne -> 809
    //   755: iload #12
    //   757: iload #18
    //   759: invokestatic max : (II)I
    //   762: istore #16
    //   764: iload #16
    //   766: istore #10
    //   768: iload #9
    //   770: ifle -> 782
    //   773: iload #9
    //   775: iload #16
    //   777: invokestatic min : (II)I
    //   780: istore #10
    //   782: aload_1
    //   783: aload #22
    //   785: aload #21
    //   787: iload #10
    //   789: bipush #6
    //   791: invokevirtual addEquality : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)Landroidx/constraintlayout/solver/ArrayRow;
    //   794: pop
    //   795: iload #12
    //   797: istore #10
    //   799: iconst_0
    //   800: istore #16
    //   802: iload #9
    //   804: istore #12
    //   806: goto -> 825
    //   809: iload #12
    //   811: istore #17
    //   813: iload #9
    //   815: istore #12
    //   817: iload #10
    //   819: istore #16
    //   821: iload #17
    //   823: istore #10
    //   825: aload #23
    //   827: astore #5
    //   829: iload #20
    //   831: ifeq -> 1444
    //   834: iload #15
    //   836: ifeq -> 842
    //   839: goto -> 1444
    //   842: iload #26
    //   844: ifne -> 874
    //   847: iload #27
    //   849: ifne -> 874
    //   852: iload #28
    //   854: ifne -> 874
    //   857: iload_2
    //   858: ifeq -> 1426
    //   861: aload_1
    //   862: aload #4
    //   864: aload #22
    //   866: iconst_0
    //   867: iconst_5
    //   868: invokevirtual addGreaterThan : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)V
    //   871: goto -> 1426
    //   874: iload #26
    //   876: ifeq -> 901
    //   879: iload #27
    //   881: ifne -> 901
    //   884: iload_2
    //   885: ifeq -> 1426
    //   888: aload_1
    //   889: aload #4
    //   891: aload #22
    //   893: iconst_0
    //   894: iconst_5
    //   895: invokevirtual addGreaterThan : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)V
    //   898: goto -> 1426
    //   901: iload #26
    //   903: ifne -> 944
    //   906: iload #27
    //   908: ifeq -> 944
    //   911: aload_1
    //   912: aload #22
    //   914: aload #24
    //   916: aload #8
    //   918: invokevirtual getMargin : ()I
    //   921: ineg
    //   922: bipush #6
    //   924: invokevirtual addEquality : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)Landroidx/constraintlayout/solver/ArrayRow;
    //   927: pop
    //   928: iload_2
    //   929: ifeq -> 1426
    //   932: aload_1
    //   933: aload #21
    //   935: aload_3
    //   936: iconst_0
    //   937: iconst_5
    //   938: invokevirtual addGreaterThan : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)V
    //   941: goto -> 1426
    //   944: iload #26
    //   946: ifeq -> 1426
    //   949: iload #27
    //   951: ifeq -> 1426
    //   954: iload #16
    //   956: ifeq -> 1178
    //   959: iload_2
    //   960: ifeq -> 979
    //   963: iload #11
    //   965: ifne -> 979
    //   968: aload_1
    //   969: aload #22
    //   971: aload #21
    //   973: iconst_0
    //   974: bipush #6
    //   976: invokevirtual addGreaterThan : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)V
    //   979: iload #30
    //   981: ifne -> 1074
    //   984: iload #12
    //   986: ifgt -> 1007
    //   989: iload #10
    //   991: ifle -> 997
    //   994: goto -> 1007
    //   997: bipush #6
    //   999: istore #11
    //   1001: iconst_0
    //   1002: istore #9
    //   1004: goto -> 1013
    //   1007: iconst_4
    //   1008: istore #11
    //   1010: iconst_1
    //   1011: istore #9
    //   1013: aload_1
    //   1014: aload #21
    //   1016: aload #5
    //   1018: aload #7
    //   1020: invokevirtual getMargin : ()I
    //   1023: iload #11
    //   1025: invokevirtual addEquality : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)Landroidx/constraintlayout/solver/ArrayRow;
    //   1028: pop
    //   1029: aload_1
    //   1030: aload #22
    //   1032: aload #24
    //   1034: aload #8
    //   1036: invokevirtual getMargin : ()I
    //   1039: ineg
    //   1040: iload #11
    //   1042: invokevirtual addEquality : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)Landroidx/constraintlayout/solver/ArrayRow;
    //   1045: pop
    //   1046: iload #12
    //   1048: ifgt -> 1065
    //   1051: iload #10
    //   1053: ifle -> 1059
    //   1056: goto -> 1065
    //   1059: iconst_0
    //   1060: istore #11
    //   1062: goto -> 1068
    //   1065: iconst_1
    //   1066: istore #11
    //   1068: iconst_5
    //   1069: istore #10
    //   1071: goto -> 1090
    //   1074: iload #30
    //   1076: iconst_1
    //   1077: if_icmpne -> 1093
    //   1080: iconst_1
    //   1081: istore #11
    //   1083: bipush #6
    //   1085: istore #10
    //   1087: iconst_1
    //   1088: istore #9
    //   1090: goto -> 1187
    //   1093: iload #30
    //   1095: iconst_3
    //   1096: if_icmpne -> 1172
    //   1099: iload #14
    //   1101: ifne -> 1124
    //   1104: aload_0
    //   1105: getfield mResolvedDimensionRatioSide : I
    //   1108: iconst_m1
    //   1109: if_icmpeq -> 1124
    //   1112: iload #12
    //   1114: ifgt -> 1124
    //   1117: bipush #6
    //   1119: istore #9
    //   1121: goto -> 1127
    //   1124: iconst_4
    //   1125: istore #9
    //   1127: aload_1
    //   1128: aload #21
    //   1130: aload #5
    //   1132: aload #7
    //   1134: invokevirtual getMargin : ()I
    //   1137: iload #9
    //   1139: invokevirtual addEquality : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)Landroidx/constraintlayout/solver/ArrayRow;
    //   1142: pop
    //   1143: aload_1
    //   1144: aload #22
    //   1146: aload #24
    //   1148: aload #8
    //   1150: invokevirtual getMargin : ()I
    //   1153: ineg
    //   1154: iload #9
    //   1156: invokevirtual addEquality : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)Landroidx/constraintlayout/solver/ArrayRow;
    //   1159: pop
    //   1160: iconst_1
    //   1161: istore #11
    //   1163: iconst_5
    //   1164: istore #10
    //   1166: iconst_1
    //   1167: istore #9
    //   1169: goto -> 1187
    //   1172: iconst_0
    //   1173: istore #11
    //   1175: goto -> 1181
    //   1178: iconst_1
    //   1179: istore #11
    //   1181: iconst_5
    //   1182: istore #10
    //   1184: iconst_0
    //   1185: istore #9
    //   1187: aload #5
    //   1189: astore #25
    //   1191: iload #11
    //   1193: ifeq -> 1302
    //   1196: aload #7
    //   1198: invokevirtual getMargin : ()I
    //   1201: istore #12
    //   1203: aload #8
    //   1205: invokevirtual getMargin : ()I
    //   1208: istore #11
    //   1210: iconst_1
    //   1211: istore #6
    //   1213: aload_1
    //   1214: aload #21
    //   1216: aload #25
    //   1218: iload #12
    //   1220: fload #13
    //   1222: aload #24
    //   1224: aload #22
    //   1226: iload #11
    //   1228: iload #10
    //   1230: invokevirtual addCentering : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;IFLandroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)V
    //   1233: aload #7
    //   1235: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1238: getfield mOwner : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   1241: instanceof androidx/constraintlayout/solver/widgets/Barrier
    //   1244: istore #14
    //   1246: aload #8
    //   1248: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1251: getfield mOwner : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   1254: instanceof androidx/constraintlayout/solver/widgets/Barrier
    //   1257: istore #15
    //   1259: iload #14
    //   1261: ifeq -> 1285
    //   1264: iload #15
    //   1266: ifne -> 1285
    //   1269: iload_2
    //   1270: istore #15
    //   1272: iconst_1
    //   1273: istore #6
    //   1275: iconst_5
    //   1276: istore #11
    //   1278: bipush #6
    //   1280: istore #10
    //   1282: goto -> 1322
    //   1285: iload #14
    //   1287: ifne -> 1302
    //   1290: iload #15
    //   1292: ifeq -> 1302
    //   1295: bipush #6
    //   1297: istore #11
    //   1299: goto -> 1308
    //   1302: iload_2
    //   1303: istore #6
    //   1305: iconst_5
    //   1306: istore #11
    //   1308: iload_2
    //   1309: istore #14
    //   1311: iconst_5
    //   1312: istore #10
    //   1314: iload #6
    //   1316: istore #15
    //   1318: iload #14
    //   1320: istore #6
    //   1322: aload #22
    //   1324: astore #5
    //   1326: iload #9
    //   1328: ifeq -> 1339
    //   1331: bipush #6
    //   1333: istore #11
    //   1335: bipush #6
    //   1337: istore #10
    //   1339: iload #16
    //   1341: ifne -> 1349
    //   1344: iload #15
    //   1346: ifne -> 1354
    //   1349: iload #9
    //   1351: ifeq -> 1369
    //   1354: aload_1
    //   1355: aload #21
    //   1357: aload #25
    //   1359: aload #7
    //   1361: invokevirtual getMargin : ()I
    //   1364: iload #11
    //   1366: invokevirtual addGreaterThan : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)V
    //   1369: iload #16
    //   1371: ifne -> 1379
    //   1374: iload #6
    //   1376: ifne -> 1384
    //   1379: iload #9
    //   1381: ifeq -> 1400
    //   1384: aload_1
    //   1385: aload #5
    //   1387: aload #24
    //   1389: aload #8
    //   1391: invokevirtual getMargin : ()I
    //   1394: ineg
    //   1395: iload #10
    //   1397: invokevirtual addLowerThan : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)V
    //   1400: iload_2
    //   1401: ifeq -> 1420
    //   1404: aload_1
    //   1405: aload #21
    //   1407: aload_3
    //   1408: iconst_0
    //   1409: bipush #6
    //   1411: invokevirtual addGreaterThan : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)V
    //   1414: aload #5
    //   1416: astore_3
    //   1417: goto -> 1429
    //   1420: aload #5
    //   1422: astore_3
    //   1423: goto -> 1429
    //   1426: aload #22
    //   1428: astore_3
    //   1429: iload_2
    //   1430: ifeq -> 1443
    //   1433: aload_1
    //   1434: aload #4
    //   1436: aload_3
    //   1437: iconst_0
    //   1438: bipush #6
    //   1440: invokevirtual addGreaterThan : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)V
    //   1443: return
    //   1444: iload #29
    //   1446: iconst_2
    //   1447: if_icmpge -> 1475
    //   1450: iload_2
    //   1451: ifeq -> 1475
    //   1454: aload_1
    //   1455: aload #21
    //   1457: aload_3
    //   1458: iconst_0
    //   1459: bipush #6
    //   1461: invokevirtual addGreaterThan : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)V
    //   1464: aload_1
    //   1465: aload #4
    //   1467: aload #22
    //   1469: iconst_0
    //   1470: bipush #6
    //   1472: invokevirtual addGreaterThan : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)V
    //   1475: return
  }
  
  private boolean isChainHead(int paramInt) {
    paramInt *= 2;
    ConstraintAnchor constraintAnchor = (this.mListAnchors[paramInt]).mTarget;
    null = true;
    if (constraintAnchor != null) {
      ConstraintAnchor constraintAnchor1 = (this.mListAnchors[paramInt]).mTarget.mTarget;
      ConstraintAnchor[] arrayOfConstraintAnchor = this.mListAnchors;
      if (constraintAnchor1 != arrayOfConstraintAnchor[paramInt])
        if ((arrayOfConstraintAnchor[++paramInt]).mTarget != null && (this.mListAnchors[paramInt]).mTarget.mTarget == this.mListAnchors[paramInt])
          return null;  
    } 
    return false;
  }
  
  public void addToSolver(LinearSystem paramLinearSystem) {
    // Byte code:
    //   0: aload_1
    //   1: aload_0
    //   2: getfield mLeft : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   5: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroidx/constraintlayout/solver/SolverVariable;
    //   8: astore_2
    //   9: aload_1
    //   10: aload_0
    //   11: getfield mRight : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   14: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroidx/constraintlayout/solver/SolverVariable;
    //   17: astore_3
    //   18: aload_1
    //   19: aload_0
    //   20: getfield mTop : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   23: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroidx/constraintlayout/solver/SolverVariable;
    //   26: astore #4
    //   28: aload_1
    //   29: aload_0
    //   30: getfield mBottom : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   33: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroidx/constraintlayout/solver/SolverVariable;
    //   36: astore #5
    //   38: aload_1
    //   39: aload_0
    //   40: getfield mBaseline : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   43: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroidx/constraintlayout/solver/SolverVariable;
    //   46: astore #6
    //   48: aload_0
    //   49: getfield mParent : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   52: astore #7
    //   54: aload #7
    //   56: ifnull -> 326
    //   59: aload #7
    //   61: ifnull -> 83
    //   64: aload #7
    //   66: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   69: iconst_0
    //   70: aaload
    //   71: getstatic androidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   74: if_acmpne -> 83
    //   77: iconst_1
    //   78: istore #8
    //   80: goto -> 86
    //   83: iconst_0
    //   84: istore #8
    //   86: aload_0
    //   87: getfield mParent : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   90: astore #7
    //   92: aload #7
    //   94: ifnull -> 116
    //   97: aload #7
    //   99: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   102: iconst_1
    //   103: aaload
    //   104: getstatic androidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   107: if_acmpne -> 116
    //   110: iconst_1
    //   111: istore #9
    //   113: goto -> 119
    //   116: iconst_0
    //   117: istore #9
    //   119: aload_0
    //   120: iconst_0
    //   121: invokespecial isChainHead : (I)Z
    //   124: ifeq -> 145
    //   127: aload_0
    //   128: getfield mParent : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   131: checkcast androidx/constraintlayout/solver/widgets/ConstraintWidgetContainer
    //   134: aload_0
    //   135: iconst_0
    //   136: invokevirtual addChain : (Landroidx/constraintlayout/solver/widgets/ConstraintWidget;I)V
    //   139: iconst_1
    //   140: istore #10
    //   142: goto -> 151
    //   145: aload_0
    //   146: invokevirtual isInHorizontalChain : ()Z
    //   149: istore #10
    //   151: aload_0
    //   152: iconst_1
    //   153: invokespecial isChainHead : (I)Z
    //   156: ifeq -> 177
    //   159: aload_0
    //   160: getfield mParent : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   163: checkcast androidx/constraintlayout/solver/widgets/ConstraintWidgetContainer
    //   166: aload_0
    //   167: iconst_1
    //   168: invokevirtual addChain : (Landroidx/constraintlayout/solver/widgets/ConstraintWidget;I)V
    //   171: iconst_1
    //   172: istore #11
    //   174: goto -> 183
    //   177: aload_0
    //   178: invokevirtual isInVerticalChain : ()Z
    //   181: istore #11
    //   183: iload #8
    //   185: ifeq -> 235
    //   188: aload_0
    //   189: getfield mVisibility : I
    //   192: bipush #8
    //   194: if_icmpeq -> 235
    //   197: aload_0
    //   198: getfield mLeft : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   201: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   204: ifnonnull -> 235
    //   207: aload_0
    //   208: getfield mRight : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   211: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   214: ifnonnull -> 235
    //   217: aload_1
    //   218: aload_1
    //   219: aload_0
    //   220: getfield mParent : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   223: getfield mRight : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   226: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroidx/constraintlayout/solver/SolverVariable;
    //   229: aload_3
    //   230: iconst_0
    //   231: iconst_1
    //   232: invokevirtual addGreaterThan : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)V
    //   235: iload #9
    //   237: ifeq -> 295
    //   240: aload_0
    //   241: getfield mVisibility : I
    //   244: bipush #8
    //   246: if_icmpeq -> 295
    //   249: aload_0
    //   250: getfield mTop : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   253: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   256: ifnonnull -> 295
    //   259: aload_0
    //   260: getfield mBottom : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   263: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   266: ifnonnull -> 295
    //   269: aload_0
    //   270: getfield mBaseline : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   273: ifnonnull -> 295
    //   276: aload_1
    //   277: aload_1
    //   278: aload_0
    //   279: getfield mParent : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   282: getfield mBottom : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   285: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroidx/constraintlayout/solver/SolverVariable;
    //   288: aload #5
    //   290: iconst_0
    //   291: iconst_1
    //   292: invokevirtual addGreaterThan : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)V
    //   295: iload #9
    //   297: istore #12
    //   299: iload #10
    //   301: istore #9
    //   303: iload #11
    //   305: istore #13
    //   307: iload #8
    //   309: istore #10
    //   311: iload #12
    //   313: istore #8
    //   315: iload #9
    //   317: istore #11
    //   319: iload #13
    //   321: istore #9
    //   323: goto -> 338
    //   326: iconst_0
    //   327: istore #10
    //   329: iconst_0
    //   330: istore #8
    //   332: iconst_0
    //   333: istore #11
    //   335: iconst_0
    //   336: istore #9
    //   338: aload_0
    //   339: getfield mWidth : I
    //   342: istore #14
    //   344: aload_0
    //   345: getfield mMinWidth : I
    //   348: istore #15
    //   350: iload #14
    //   352: istore #16
    //   354: iload #14
    //   356: iload #15
    //   358: if_icmpge -> 365
    //   361: iload #15
    //   363: istore #16
    //   365: aload_0
    //   366: getfield mHeight : I
    //   369: istore #17
    //   371: aload_0
    //   372: getfield mMinHeight : I
    //   375: istore #15
    //   377: iload #17
    //   379: istore #14
    //   381: iload #17
    //   383: iload #15
    //   385: if_icmpge -> 392
    //   388: iload #15
    //   390: istore #14
    //   392: aload_0
    //   393: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   396: iconst_0
    //   397: aaload
    //   398: getstatic androidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   401: if_acmpeq -> 410
    //   404: iconst_1
    //   405: istore #13
    //   407: goto -> 413
    //   410: iconst_0
    //   411: istore #13
    //   413: aload_0
    //   414: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   417: iconst_1
    //   418: aaload
    //   419: getstatic androidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   422: if_acmpeq -> 431
    //   425: iconst_1
    //   426: istore #12
    //   428: goto -> 434
    //   431: iconst_0
    //   432: istore #12
    //   434: aload_0
    //   435: aload_0
    //   436: getfield mDimensionRatioSide : I
    //   439: putfield mResolvedDimensionRatioSide : I
    //   442: aload_0
    //   443: getfield mDimensionRatio : F
    //   446: fstore #18
    //   448: aload_0
    //   449: fload #18
    //   451: putfield mResolvedDimensionRatio : F
    //   454: aload_0
    //   455: getfield mMatchConstraintDefaultWidth : I
    //   458: istore #19
    //   460: aload_0
    //   461: getfield mMatchConstraintDefaultHeight : I
    //   464: istore #20
    //   466: fload #18
    //   468: fconst_0
    //   469: fcmpl
    //   470: ifle -> 808
    //   473: aload_0
    //   474: getfield mVisibility : I
    //   477: bipush #8
    //   479: if_icmpeq -> 808
    //   482: iload #19
    //   484: istore #17
    //   486: aload_0
    //   487: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   490: iconst_0
    //   491: aaload
    //   492: getstatic androidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   495: if_acmpne -> 510
    //   498: iload #19
    //   500: istore #17
    //   502: iload #19
    //   504: ifne -> 510
    //   507: iconst_3
    //   508: istore #17
    //   510: iload #20
    //   512: istore #15
    //   514: aload_0
    //   515: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   518: iconst_1
    //   519: aaload
    //   520: getstatic androidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   523: if_acmpne -> 538
    //   526: iload #20
    //   528: istore #15
    //   530: iload #20
    //   532: ifne -> 538
    //   535: iconst_3
    //   536: istore #15
    //   538: aload_0
    //   539: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   542: iconst_0
    //   543: aaload
    //   544: getstatic androidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   547: if_acmpne -> 589
    //   550: aload_0
    //   551: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   554: iconst_1
    //   555: aaload
    //   556: getstatic androidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   559: if_acmpne -> 589
    //   562: iload #17
    //   564: iconst_3
    //   565: if_icmpne -> 589
    //   568: iload #15
    //   570: iconst_3
    //   571: if_icmpne -> 589
    //   574: aload_0
    //   575: iload #10
    //   577: iload #8
    //   579: iload #13
    //   581: iload #12
    //   583: invokevirtual setupDimensionRatio : (ZZZZ)V
    //   586: goto -> 774
    //   589: aload_0
    //   590: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   593: iconst_0
    //   594: aaload
    //   595: getstatic androidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   598: if_acmpne -> 666
    //   601: iload #17
    //   603: iconst_3
    //   604: if_icmpne -> 666
    //   607: aload_0
    //   608: iconst_0
    //   609: putfield mResolvedDimensionRatioSide : I
    //   612: aload_0
    //   613: getfield mResolvedDimensionRatio : F
    //   616: aload_0
    //   617: getfield mHeight : I
    //   620: i2f
    //   621: fmul
    //   622: f2i
    //   623: istore #20
    //   625: aload_0
    //   626: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   629: iconst_1
    //   630: aaload
    //   631: astore #7
    //   633: getstatic androidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   636: astore #21
    //   638: aload #7
    //   640: aload #21
    //   642: if_acmpeq -> 663
    //   645: iload #14
    //   647: istore #17
    //   649: iload #15
    //   651: istore #14
    //   653: iconst_4
    //   654: istore #16
    //   656: iload #20
    //   658: istore #15
    //   660: goto -> 824
    //   663: goto -> 778
    //   666: aload_0
    //   667: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   670: iconst_1
    //   671: aaload
    //   672: getstatic androidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   675: if_acmpne -> 774
    //   678: iload #15
    //   680: iconst_3
    //   681: if_icmpne -> 774
    //   684: aload_0
    //   685: iconst_1
    //   686: putfield mResolvedDimensionRatioSide : I
    //   689: aload_0
    //   690: getfield mDimensionRatioSide : I
    //   693: iconst_m1
    //   694: if_icmpne -> 707
    //   697: aload_0
    //   698: fconst_1
    //   699: aload_0
    //   700: getfield mResolvedDimensionRatio : F
    //   703: fdiv
    //   704: putfield mResolvedDimensionRatio : F
    //   707: aload_0
    //   708: getfield mResolvedDimensionRatio : F
    //   711: aload_0
    //   712: getfield mWidth : I
    //   715: i2f
    //   716: fmul
    //   717: f2i
    //   718: istore #20
    //   720: aload_0
    //   721: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   724: iconst_0
    //   725: aaload
    //   726: astore #7
    //   728: getstatic androidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   731: astore #21
    //   733: iload #17
    //   735: istore #22
    //   737: iload #16
    //   739: istore #19
    //   741: iload #19
    //   743: istore #14
    //   745: iload #20
    //   747: istore #16
    //   749: aload #7
    //   751: aload #21
    //   753: if_acmpeq -> 786
    //   756: iconst_4
    //   757: istore #14
    //   759: iload #22
    //   761: istore #16
    //   763: iload #19
    //   765: istore #15
    //   767: iload #20
    //   769: istore #17
    //   771: goto -> 824
    //   774: iload #16
    //   776: istore #20
    //   778: iload #14
    //   780: istore #16
    //   782: iload #20
    //   784: istore #14
    //   786: iconst_1
    //   787: istore #20
    //   789: iload #17
    //   791: istore #19
    //   793: iload #20
    //   795: istore #17
    //   797: iload #14
    //   799: istore #22
    //   801: iload #16
    //   803: istore #20
    //   805: goto -> 847
    //   808: iload #14
    //   810: istore #17
    //   812: iload #20
    //   814: istore #14
    //   816: iload #16
    //   818: istore #15
    //   820: iload #19
    //   822: istore #16
    //   824: iconst_0
    //   825: istore #19
    //   827: iload #17
    //   829: istore #20
    //   831: iload #15
    //   833: istore #22
    //   835: iload #19
    //   837: istore #17
    //   839: iload #14
    //   841: istore #15
    //   843: iload #16
    //   845: istore #19
    //   847: aload_0
    //   848: getfield mResolvedMatchConstraintDefault : [I
    //   851: astore #7
    //   853: aload #7
    //   855: iconst_0
    //   856: iload #19
    //   858: iastore
    //   859: aload #7
    //   861: iconst_1
    //   862: iload #15
    //   864: iastore
    //   865: iload #17
    //   867: ifeq -> 893
    //   870: aload_0
    //   871: getfield mResolvedDimensionRatioSide : I
    //   874: istore #16
    //   876: iload #16
    //   878: ifeq -> 887
    //   881: iload #16
    //   883: iconst_m1
    //   884: if_icmpne -> 893
    //   887: iconst_1
    //   888: istore #13
    //   890: goto -> 896
    //   893: iconst_0
    //   894: istore #13
    //   896: aload_0
    //   897: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   900: iconst_0
    //   901: aaload
    //   902: getstatic androidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   905: if_acmpne -> 921
    //   908: aload_0
    //   909: instanceof androidx/constraintlayout/solver/widgets/ConstraintWidgetContainer
    //   912: ifeq -> 921
    //   915: iconst_1
    //   916: istore #12
    //   918: goto -> 924
    //   921: iconst_0
    //   922: istore #12
    //   924: aload_0
    //   925: getfield mCenter : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   928: invokevirtual isConnected : ()Z
    //   931: iconst_1
    //   932: ixor
    //   933: istore #23
    //   935: aload_0
    //   936: getfield mHorizontalResolution : I
    //   939: iconst_2
    //   940: if_icmpeq -> 1069
    //   943: aload_0
    //   944: getfield mParent : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   947: astore #7
    //   949: aload #7
    //   951: ifnull -> 968
    //   954: aload_1
    //   955: aload #7
    //   957: getfield mRight : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   960: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroidx/constraintlayout/solver/SolverVariable;
    //   963: astore #7
    //   965: goto -> 971
    //   968: aconst_null
    //   969: astore #7
    //   971: aload_0
    //   972: getfield mParent : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   975: astore #21
    //   977: aload #21
    //   979: ifnull -> 996
    //   982: aload_1
    //   983: aload #21
    //   985: getfield mLeft : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   988: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroidx/constraintlayout/solver/SolverVariable;
    //   991: astore #21
    //   993: goto -> 999
    //   996: aconst_null
    //   997: astore #21
    //   999: aload_0
    //   1000: aload_1
    //   1001: iload #10
    //   1003: aload #21
    //   1005: aload #7
    //   1007: aload_0
    //   1008: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   1011: iconst_0
    //   1012: aaload
    //   1013: iload #12
    //   1015: aload_0
    //   1016: getfield mLeft : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1019: aload_0
    //   1020: getfield mRight : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1023: aload_0
    //   1024: getfield mX : I
    //   1027: iload #22
    //   1029: aload_0
    //   1030: getfield mMinWidth : I
    //   1033: aload_0
    //   1034: getfield mMaxDimension : [I
    //   1037: iconst_0
    //   1038: iaload
    //   1039: aload_0
    //   1040: getfield mHorizontalBiasPercent : F
    //   1043: iload #13
    //   1045: iload #11
    //   1047: iload #19
    //   1049: aload_0
    //   1050: getfield mMatchConstraintMinWidth : I
    //   1053: aload_0
    //   1054: getfield mMatchConstraintMaxWidth : I
    //   1057: aload_0
    //   1058: getfield mMatchConstraintPercentWidth : F
    //   1061: iload #23
    //   1063: invokespecial applyConstraints : (Landroidx/constraintlayout/solver/LinearSystem;ZLandroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;ZLandroidx/constraintlayout/solver/widgets/ConstraintAnchor;Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;IIIIFZZIIIFZ)V
    //   1066: goto -> 1069
    //   1069: aload #4
    //   1071: astore #7
    //   1073: aload_0
    //   1074: getfield mVerticalResolution : I
    //   1077: iconst_2
    //   1078: if_icmpne -> 1082
    //   1081: return
    //   1082: aload_0
    //   1083: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   1086: iconst_1
    //   1087: aaload
    //   1088: getstatic androidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   1091: if_acmpne -> 1107
    //   1094: aload_0
    //   1095: instanceof androidx/constraintlayout/solver/widgets/ConstraintWidgetContainer
    //   1098: ifeq -> 1107
    //   1101: iconst_1
    //   1102: istore #10
    //   1104: goto -> 1110
    //   1107: iconst_0
    //   1108: istore #10
    //   1110: iload #17
    //   1112: ifeq -> 1139
    //   1115: aload_0
    //   1116: getfield mResolvedDimensionRatioSide : I
    //   1119: istore #16
    //   1121: iload #16
    //   1123: iconst_1
    //   1124: if_icmpeq -> 1133
    //   1127: iload #16
    //   1129: iconst_m1
    //   1130: if_icmpne -> 1139
    //   1133: iconst_1
    //   1134: istore #11
    //   1136: goto -> 1142
    //   1139: iconst_0
    //   1140: istore #11
    //   1142: aload_0
    //   1143: getfield mBaselineDistance : I
    //   1146: ifle -> 1235
    //   1149: aload_0
    //   1150: getfield mBaseline : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1153: invokevirtual getResolutionNode : ()Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;
    //   1156: getfield state : I
    //   1159: iconst_1
    //   1160: if_icmpne -> 1177
    //   1163: aload_0
    //   1164: getfield mBaseline : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1167: invokevirtual getResolutionNode : ()Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;
    //   1170: aload_1
    //   1171: invokevirtual addResolvedValue : (Landroidx/constraintlayout/solver/LinearSystem;)V
    //   1174: goto -> 1235
    //   1177: aload_1
    //   1178: astore #21
    //   1180: aload #21
    //   1182: aload #6
    //   1184: aload #7
    //   1186: aload_0
    //   1187: invokevirtual getBaselineDistance : ()I
    //   1190: bipush #6
    //   1192: invokevirtual addEquality : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)Landroidx/constraintlayout/solver/ArrayRow;
    //   1195: pop
    //   1196: aload_0
    //   1197: getfield mBaseline : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1200: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1203: ifnull -> 1235
    //   1206: aload #21
    //   1208: aload #6
    //   1210: aload #21
    //   1212: aload_0
    //   1213: getfield mBaseline : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1216: getfield mTarget : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1219: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroidx/constraintlayout/solver/SolverVariable;
    //   1222: iconst_0
    //   1223: bipush #6
    //   1225: invokevirtual addEquality : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;II)Landroidx/constraintlayout/solver/ArrayRow;
    //   1228: pop
    //   1229: iconst_0
    //   1230: istore #13
    //   1232: goto -> 1239
    //   1235: iload #23
    //   1237: istore #13
    //   1239: aload_1
    //   1240: astore #4
    //   1242: aload #7
    //   1244: astore #21
    //   1246: aload_0
    //   1247: getfield mParent : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   1250: astore #6
    //   1252: aload #6
    //   1254: ifnull -> 1272
    //   1257: aload #4
    //   1259: aload #6
    //   1261: getfield mBottom : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1264: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroidx/constraintlayout/solver/SolverVariable;
    //   1267: astore #6
    //   1269: goto -> 1275
    //   1272: aconst_null
    //   1273: astore #6
    //   1275: aload_0
    //   1276: getfield mParent : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   1279: astore #7
    //   1281: aload #7
    //   1283: ifnull -> 1301
    //   1286: aload #4
    //   1288: aload #7
    //   1290: getfield mTop : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1293: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroidx/constraintlayout/solver/SolverVariable;
    //   1296: astore #7
    //   1298: goto -> 1304
    //   1301: aconst_null
    //   1302: astore #7
    //   1304: aload_0
    //   1305: aload_1
    //   1306: iload #8
    //   1308: aload #7
    //   1310: aload #6
    //   1312: aload_0
    //   1313: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   1316: iconst_1
    //   1317: aaload
    //   1318: iload #10
    //   1320: aload_0
    //   1321: getfield mTop : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1324: aload_0
    //   1325: getfield mBottom : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1328: aload_0
    //   1329: getfield mY : I
    //   1332: iload #20
    //   1334: aload_0
    //   1335: getfield mMinHeight : I
    //   1338: aload_0
    //   1339: getfield mMaxDimension : [I
    //   1342: iconst_1
    //   1343: iaload
    //   1344: aload_0
    //   1345: getfield mVerticalBiasPercent : F
    //   1348: iload #11
    //   1350: iload #9
    //   1352: iload #15
    //   1354: aload_0
    //   1355: getfield mMatchConstraintMinHeight : I
    //   1358: aload_0
    //   1359: getfield mMatchConstraintMaxHeight : I
    //   1362: aload_0
    //   1363: getfield mMatchConstraintPercentHeight : F
    //   1366: iload #13
    //   1368: invokespecial applyConstraints : (Landroidx/constraintlayout/solver/LinearSystem;ZLandroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;ZLandroidx/constraintlayout/solver/widgets/ConstraintAnchor;Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;IIIIFZZIIIFZ)V
    //   1371: iload #17
    //   1373: ifeq -> 1428
    //   1376: aload_0
    //   1377: astore #6
    //   1379: aload #6
    //   1381: getfield mResolvedDimensionRatioSide : I
    //   1384: iconst_1
    //   1385: if_icmpne -> 1408
    //   1388: aload_1
    //   1389: aload #5
    //   1391: aload #21
    //   1393: aload_3
    //   1394: aload_2
    //   1395: aload #6
    //   1397: getfield mResolvedDimensionRatio : F
    //   1400: bipush #6
    //   1402: invokevirtual addRatio : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;FI)V
    //   1405: goto -> 1428
    //   1408: aload_1
    //   1409: aload_3
    //   1410: aload_2
    //   1411: aload #5
    //   1413: aload #21
    //   1415: aload #6
    //   1417: getfield mResolvedDimensionRatio : F
    //   1420: bipush #6
    //   1422: invokevirtual addRatio : (Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;Landroidx/constraintlayout/solver/SolverVariable;FI)V
    //   1425: goto -> 1428
    //   1428: aload_0
    //   1429: astore #6
    //   1431: aload #6
    //   1433: getfield mCenter : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1436: invokevirtual isConnected : ()Z
    //   1439: ifeq -> 1481
    //   1442: aload_1
    //   1443: aload #6
    //   1445: aload #6
    //   1447: getfield mCenter : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1450: invokevirtual getTarget : ()Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1453: invokevirtual getOwner : ()Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   1456: aload #6
    //   1458: getfield mCircleConstraintAngle : F
    //   1461: ldc_w 90.0
    //   1464: fadd
    //   1465: f2d
    //   1466: invokestatic toRadians : (D)D
    //   1469: d2f
    //   1470: aload #6
    //   1472: getfield mCenter : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1475: invokevirtual getMargin : ()I
    //   1478: invokevirtual addCenterPoint : (Landroidx/constraintlayout/solver/widgets/ConstraintWidget;Landroidx/constraintlayout/solver/widgets/ConstraintWidget;FI)V
    //   1481: return
  }
  
  public boolean allowedInBarrier() {
    boolean bool;
    if (this.mVisibility != 8) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void analyze(int paramInt) {
    Optimizer.analyze(paramInt, this);
  }
  
  public void connect(ConstraintAnchor.Type paramType1, ConstraintWidget paramConstraintWidget, ConstraintAnchor.Type paramType2) {
    connect(paramType1, paramConstraintWidget, paramType2, 0, ConstraintAnchor.Strength.STRONG);
  }
  
  public void connect(ConstraintAnchor.Type paramType1, ConstraintWidget paramConstraintWidget, ConstraintAnchor.Type paramType2, int paramInt) {
    connect(paramType1, paramConstraintWidget, paramType2, paramInt, ConstraintAnchor.Strength.STRONG);
  }
  
  public void connect(ConstraintAnchor.Type paramType1, ConstraintWidget paramConstraintWidget, ConstraintAnchor.Type paramType2, int paramInt, ConstraintAnchor.Strength paramStrength) {
    connect(paramType1, paramConstraintWidget, paramType2, paramInt, paramStrength, 0);
  }
  
  public void connect(ConstraintAnchor.Type paramType1, ConstraintWidget paramConstraintWidget, ConstraintAnchor.Type paramType2, int paramInt1, ConstraintAnchor.Strength paramStrength, int paramInt2) {
    ConstraintAnchor constraintAnchor;
    ConstraintAnchor.Type type = ConstraintAnchor.Type.CENTER;
    boolean bool = false;
    if (paramType1 == type) {
      if (paramType2 == ConstraintAnchor.Type.CENTER) {
        ConstraintAnchor constraintAnchor1 = getAnchor(ConstraintAnchor.Type.LEFT);
        constraintAnchor = getAnchor(ConstraintAnchor.Type.RIGHT);
        ConstraintAnchor constraintAnchor2 = getAnchor(ConstraintAnchor.Type.TOP);
        ConstraintAnchor constraintAnchor3 = getAnchor(ConstraintAnchor.Type.BOTTOM);
        bool = true;
        if ((constraintAnchor1 != null && constraintAnchor1.isConnected()) || (constraintAnchor != null && constraintAnchor.isConnected())) {
          paramInt1 = 0;
        } else {
          connect(ConstraintAnchor.Type.LEFT, paramConstraintWidget, ConstraintAnchor.Type.LEFT, 0, paramStrength, paramInt2);
          connect(ConstraintAnchor.Type.RIGHT, paramConstraintWidget, ConstraintAnchor.Type.RIGHT, 0, paramStrength, paramInt2);
          paramInt1 = 1;
        } 
        if ((constraintAnchor2 != null && constraintAnchor2.isConnected()) || (constraintAnchor3 != null && constraintAnchor3.isConnected())) {
          bool = false;
        } else {
          connect(ConstraintAnchor.Type.TOP, paramConstraintWidget, ConstraintAnchor.Type.TOP, 0, paramStrength, paramInt2);
          connect(ConstraintAnchor.Type.BOTTOM, paramConstraintWidget, ConstraintAnchor.Type.BOTTOM, 0, paramStrength, paramInt2);
        } 
        if (paramInt1 != 0 && bool) {
          getAnchor(ConstraintAnchor.Type.CENTER).connect(paramConstraintWidget.getAnchor(ConstraintAnchor.Type.CENTER), 0, paramInt2);
        } else if (paramInt1 != 0) {
          getAnchor(ConstraintAnchor.Type.CENTER_X).connect(paramConstraintWidget.getAnchor(ConstraintAnchor.Type.CENTER_X), 0, paramInt2);
        } else if (bool) {
          getAnchor(ConstraintAnchor.Type.CENTER_Y).connect(paramConstraintWidget.getAnchor(ConstraintAnchor.Type.CENTER_Y), 0, paramInt2);
        } 
      } else {
        if (constraintAnchor == ConstraintAnchor.Type.LEFT || constraintAnchor == ConstraintAnchor.Type.RIGHT) {
          connect(ConstraintAnchor.Type.LEFT, paramConstraintWidget, (ConstraintAnchor.Type)constraintAnchor, 0, paramStrength, paramInt2);
          paramType1 = ConstraintAnchor.Type.RIGHT;
          try {
            connect(paramType1, paramConstraintWidget, (ConstraintAnchor.Type)constraintAnchor, 0, paramStrength, paramInt2);
            getAnchor(ConstraintAnchor.Type.CENTER).connect(paramConstraintWidget.getAnchor((ConstraintAnchor.Type)constraintAnchor), 0, paramInt2);
            return;
          } finally {}
        } 
        if (constraintAnchor == ConstraintAnchor.Type.TOP || constraintAnchor == ConstraintAnchor.Type.BOTTOM) {
          connect(ConstraintAnchor.Type.TOP, paramConstraintWidget, (ConstraintAnchor.Type)constraintAnchor, 0, paramStrength, paramInt2);
          connect(ConstraintAnchor.Type.BOTTOM, paramConstraintWidget, (ConstraintAnchor.Type)constraintAnchor, 0, paramStrength, paramInt2);
          getAnchor(ConstraintAnchor.Type.CENTER).connect(paramConstraintWidget.getAnchor((ConstraintAnchor.Type)constraintAnchor), 0, paramInt2);
        } 
      } 
    } else {
      ConstraintAnchor constraintAnchor1;
      ConstraintAnchor constraintAnchor2;
      if (paramType1 == ConstraintAnchor.Type.CENTER_X && (constraintAnchor == ConstraintAnchor.Type.LEFT || constraintAnchor == ConstraintAnchor.Type.RIGHT)) {
        constraintAnchor1 = getAnchor(ConstraintAnchor.Type.LEFT);
        constraintAnchor2 = paramConstraintWidget.getAnchor((ConstraintAnchor.Type)constraintAnchor);
        constraintAnchor = getAnchor(ConstraintAnchor.Type.RIGHT);
        constraintAnchor1.connect(constraintAnchor2, 0, paramInt2);
        constraintAnchor.connect(constraintAnchor2, 0, paramInt2);
        getAnchor(ConstraintAnchor.Type.CENTER_X).connect(constraintAnchor2, 0, paramInt2);
      } else if (constraintAnchor1 == ConstraintAnchor.Type.CENTER_Y && (constraintAnchor == ConstraintAnchor.Type.TOP || constraintAnchor == ConstraintAnchor.Type.BOTTOM)) {
        constraintAnchor1 = constraintAnchor2.getAnchor((ConstraintAnchor.Type)constraintAnchor);
        getAnchor(ConstraintAnchor.Type.TOP).connect(constraintAnchor1, 0, paramInt2);
        getAnchor(ConstraintAnchor.Type.BOTTOM).connect(constraintAnchor1, 0, paramInt2);
        getAnchor(ConstraintAnchor.Type.CENTER_Y).connect(constraintAnchor1, 0, paramInt2);
      } else if (constraintAnchor1 == ConstraintAnchor.Type.CENTER_X && constraintAnchor == ConstraintAnchor.Type.CENTER_X) {
        getAnchor(ConstraintAnchor.Type.LEFT).connect(constraintAnchor2.getAnchor(ConstraintAnchor.Type.LEFT), 0, paramInt2);
        getAnchor(ConstraintAnchor.Type.RIGHT).connect(constraintAnchor2.getAnchor(ConstraintAnchor.Type.RIGHT), 0, paramInt2);
        getAnchor(ConstraintAnchor.Type.CENTER_X).connect(constraintAnchor2.getAnchor((ConstraintAnchor.Type)constraintAnchor), 0, paramInt2);
      } else if (constraintAnchor1 == ConstraintAnchor.Type.CENTER_Y && constraintAnchor == ConstraintAnchor.Type.CENTER_Y) {
        getAnchor(ConstraintAnchor.Type.TOP).connect(constraintAnchor2.getAnchor(ConstraintAnchor.Type.TOP), 0, paramInt2);
        getAnchor(ConstraintAnchor.Type.BOTTOM).connect(constraintAnchor2.getAnchor(ConstraintAnchor.Type.BOTTOM), 0, paramInt2);
        getAnchor(ConstraintAnchor.Type.CENTER_Y).connect(constraintAnchor2.getAnchor((ConstraintAnchor.Type)constraintAnchor), 0, paramInt2);
      } else {
        ConstraintAnchor constraintAnchor3 = getAnchor((ConstraintAnchor.Type)constraintAnchor1);
        constraintAnchor2 = constraintAnchor2.getAnchor((ConstraintAnchor.Type)constraintAnchor);
        if (constraintAnchor3.isValidConnection(constraintAnchor2)) {
          if (constraintAnchor1 == ConstraintAnchor.Type.BASELINE) {
            constraintAnchor = getAnchor(ConstraintAnchor.Type.TOP);
            constraintAnchor1 = getAnchor(ConstraintAnchor.Type.BOTTOM);
            if (constraintAnchor != null)
              constraintAnchor.reset(); 
            paramInt1 = bool;
            if (constraintAnchor1 != null) {
              constraintAnchor1.reset();
              paramInt1 = bool;
            } 
          } else if (constraintAnchor1 == ConstraintAnchor.Type.TOP || constraintAnchor1 == ConstraintAnchor.Type.BOTTOM) {
            constraintAnchor = getAnchor(ConstraintAnchor.Type.BASELINE);
            if (constraintAnchor != null)
              constraintAnchor.reset(); 
            constraintAnchor = getAnchor(ConstraintAnchor.Type.CENTER);
            if (constraintAnchor.getTarget() != constraintAnchor2)
              constraintAnchor.reset(); 
            constraintAnchor1 = getAnchor((ConstraintAnchor.Type)constraintAnchor1).getOpposite();
            constraintAnchor = getAnchor(ConstraintAnchor.Type.CENTER_Y);
            if (constraintAnchor.isConnected()) {
              constraintAnchor1.reset();
              constraintAnchor.reset();
            } 
          } else if (constraintAnchor1 == ConstraintAnchor.Type.LEFT || constraintAnchor1 == ConstraintAnchor.Type.RIGHT) {
            constraintAnchor = getAnchor(ConstraintAnchor.Type.CENTER);
            if (constraintAnchor.getTarget() != constraintAnchor2)
              constraintAnchor.reset(); 
            constraintAnchor1 = getAnchor((ConstraintAnchor.Type)constraintAnchor1).getOpposite();
            constraintAnchor = getAnchor(ConstraintAnchor.Type.CENTER_X);
            if (constraintAnchor.isConnected()) {
              constraintAnchor1.reset();
              constraintAnchor.reset();
            } 
          } 
          constraintAnchor3.connect(constraintAnchor2, paramInt1, paramStrength, paramInt2);
          constraintAnchor2.getOwner().connectedTo(constraintAnchor3.getOwner());
        } 
      } 
    } 
  }
  
  public void connect(ConstraintAnchor paramConstraintAnchor1, ConstraintAnchor paramConstraintAnchor2, int paramInt) {
    connect(paramConstraintAnchor1, paramConstraintAnchor2, paramInt, ConstraintAnchor.Strength.STRONG, 0);
  }
  
  public void connect(ConstraintAnchor paramConstraintAnchor1, ConstraintAnchor paramConstraintAnchor2, int paramInt1, int paramInt2) {
    connect(paramConstraintAnchor1, paramConstraintAnchor2, paramInt1, ConstraintAnchor.Strength.STRONG, paramInt2);
  }
  
  public void connect(ConstraintAnchor paramConstraintAnchor1, ConstraintAnchor paramConstraintAnchor2, int paramInt1, ConstraintAnchor.Strength paramStrength, int paramInt2) {
    if (paramConstraintAnchor1.getOwner() == this)
      connect(paramConstraintAnchor1.getType(), paramConstraintAnchor2.getOwner(), paramConstraintAnchor2.getType(), paramInt1, paramStrength, paramInt2); 
  }
  
  public void connectCircularConstraint(ConstraintWidget paramConstraintWidget, float paramFloat, int paramInt) {
    immediateConnect(ConstraintAnchor.Type.CENTER, paramConstraintWidget, ConstraintAnchor.Type.CENTER, paramInt, 0);
    this.mCircleConstraintAngle = paramFloat;
  }
  
  public void connectedTo(ConstraintWidget paramConstraintWidget) {}
  
  public void createObjectVariables(LinearSystem paramLinearSystem) {
    paramLinearSystem.createObjectVariable(this.mLeft);
    paramLinearSystem.createObjectVariable(this.mTop);
    paramLinearSystem.createObjectVariable(this.mRight);
    paramLinearSystem.createObjectVariable(this.mBottom);
    if (this.mBaselineDistance > 0)
      paramLinearSystem.createObjectVariable(this.mBaseline); 
  }
  
  public void disconnectUnlockedWidget(ConstraintWidget paramConstraintWidget) {
    ArrayList<ConstraintAnchor> arrayList = getAnchors();
    int i = arrayList.size();
    for (byte b = 0; b < i; b++) {
      ConstraintAnchor constraintAnchor = arrayList.get(b);
      if (constraintAnchor.isConnected() && constraintAnchor.getTarget().getOwner() == paramConstraintWidget && constraintAnchor.getConnectionCreator() == 2)
        constraintAnchor.reset(); 
    } 
  }
  
  public void disconnectWidget(ConstraintWidget paramConstraintWidget) {
    ArrayList<ConstraintAnchor> arrayList = getAnchors();
    int i = arrayList.size();
    for (byte b = 0; b < i; b++) {
      ConstraintAnchor constraintAnchor = arrayList.get(b);
      if (constraintAnchor.isConnected() && constraintAnchor.getTarget().getOwner() == paramConstraintWidget)
        constraintAnchor.reset(); 
    } 
  }
  
  public void forceUpdateDrawPosition() {
    int i = this.mX;
    int j = this.mY;
    int k = this.mWidth;
    int m = this.mHeight;
    this.mDrawX = i;
    this.mDrawY = j;
    this.mDrawWidth = k + i - i;
    this.mDrawHeight = m + j - j;
  }
  
  public ConstraintAnchor getAnchor(ConstraintAnchor.Type paramType) {
    switch (paramType) {
      default:
        throw new AssertionError(paramType.name());
      case null:
        return null;
      case null:
        return this.mCenterY;
      case null:
        return this.mCenterX;
      case null:
        return this.mCenter;
      case null:
        return this.mBaseline;
      case MATCH_CONSTRAINT:
        return this.mBottom;
      case MATCH_PARENT:
        return this.mRight;
      case WRAP_CONTENT:
        return this.mTop;
      case FIXED:
        break;
    } 
    return this.mLeft;
  }
  
  public ArrayList<ConstraintAnchor> getAnchors() {
    return this.mAnchors;
  }
  
  public int getBaselineDistance() {
    return this.mBaselineDistance;
  }
  
  public float getBiasPercent(int paramInt) {
    return (paramInt == 0) ? this.mHorizontalBiasPercent : ((paramInt == 1) ? this.mVerticalBiasPercent : -1.0F);
  }
  
  public int getBottom() {
    return getY() + this.mHeight;
  }
  
  public Object getCompanionWidget() {
    return this.mCompanionWidget;
  }
  
  public int getContainerItemSkip() {
    return this.mContainerItemSkip;
  }
  
  public String getDebugName() {
    return this.mDebugName;
  }
  
  public DimensionBehaviour getDimensionBehaviour(int paramInt) {
    return (paramInt == 0) ? getHorizontalDimensionBehaviour() : ((paramInt == 1) ? getVerticalDimensionBehaviour() : null);
  }
  
  public float getDimensionRatio() {
    return this.mDimensionRatio;
  }
  
  public int getDimensionRatioSide() {
    return this.mDimensionRatioSide;
  }
  
  public int getDrawBottom() {
    return getDrawY() + this.mDrawHeight;
  }
  
  public int getDrawHeight() {
    return this.mDrawHeight;
  }
  
  public int getDrawRight() {
    return getDrawX() + this.mDrawWidth;
  }
  
  public int getDrawWidth() {
    return this.mDrawWidth;
  }
  
  public int getDrawX() {
    return this.mDrawX + this.mOffsetX;
  }
  
  public int getDrawY() {
    return this.mDrawY + this.mOffsetY;
  }
  
  public int getHeight() {
    return (this.mVisibility == 8) ? 0 : this.mHeight;
  }
  
  public float getHorizontalBiasPercent() {
    return this.mHorizontalBiasPercent;
  }
  
  public ConstraintWidget getHorizontalChainControlWidget() {
    ConstraintWidget constraintWidget;
    if (isInHorizontalChain()) {
      ConstraintWidget constraintWidget1 = this;
      ConstraintAnchor constraintAnchor = null;
      while (true) {
        constraintWidget = (ConstraintWidget)constraintAnchor;
        if (constraintAnchor == null) {
          constraintWidget = (ConstraintWidget)constraintAnchor;
          if (constraintWidget1 != null) {
            ConstraintWidget constraintWidget2;
            ConstraintAnchor constraintAnchor1;
            constraintWidget = (ConstraintWidget)constraintWidget1.getAnchor(ConstraintAnchor.Type.LEFT);
            if (constraintWidget == null) {
              constraintWidget = null;
            } else {
              constraintWidget = (ConstraintWidget)constraintWidget.getTarget();
            } 
            if (constraintWidget == null) {
              constraintWidget = null;
            } else {
              constraintWidget2 = constraintWidget.getOwner();
            } 
            if (constraintWidget2 == getParent()) {
              constraintWidget2 = constraintWidget1;
              break;
            } 
            if (constraintWidget2 == null) {
              constraintAnchor1 = null;
            } else {
              constraintAnchor1 = constraintWidget2.getAnchor(ConstraintAnchor.Type.RIGHT).getTarget();
            } 
            if (constraintAnchor1 != null && constraintAnchor1.getOwner() != constraintWidget1) {
              ConstraintWidget constraintWidget3 = constraintWidget1;
              continue;
            } 
            constraintWidget1 = constraintWidget2;
            continue;
          } 
        } 
        break;
      } 
    } else {
      constraintWidget = null;
    } 
    return constraintWidget;
  }
  
  public int getHorizontalChainStyle() {
    return this.mHorizontalChainStyle;
  }
  
  public DimensionBehaviour getHorizontalDimensionBehaviour() {
    return this.mListDimensionBehaviors[0];
  }
  
  public int getInternalDrawBottom() {
    return this.mDrawY + this.mDrawHeight;
  }
  
  public int getInternalDrawRight() {
    return this.mDrawX + this.mDrawWidth;
  }
  
  int getInternalDrawX() {
    return this.mDrawX;
  }
  
  int getInternalDrawY() {
    return this.mDrawY;
  }
  
  public int getLeft() {
    return getX();
  }
  
  public int getLength(int paramInt) {
    return (paramInt == 0) ? getWidth() : ((paramInt == 1) ? getHeight() : 0);
  }
  
  public int getMaxHeight() {
    return this.mMaxDimension[1];
  }
  
  public int getMaxWidth() {
    return this.mMaxDimension[0];
  }
  
  public int getMinHeight() {
    return this.mMinHeight;
  }
  
  public int getMinWidth() {
    return this.mMinWidth;
  }
  
  public int getOptimizerWrapHeight() {
    int i = this.mHeight;
    int j = i;
    if (this.mListDimensionBehaviors[1] == DimensionBehaviour.MATCH_CONSTRAINT) {
      if (this.mMatchConstraintDefaultHeight == 1) {
        i = Math.max(this.mMatchConstraintMinHeight, i);
      } else {
        i = this.mMatchConstraintMinHeight;
        if (i > 0) {
          this.mHeight = i;
        } else {
          i = 0;
        } 
      } 
      int k = this.mMatchConstraintMaxHeight;
      j = i;
      if (k > 0) {
        j = i;
        if (k < i)
          j = k; 
      } 
    } 
    return j;
  }
  
  public int getOptimizerWrapWidth() {
    int i = this.mWidth;
    int j = i;
    if (this.mListDimensionBehaviors[0] == DimensionBehaviour.MATCH_CONSTRAINT) {
      if (this.mMatchConstraintDefaultWidth == 1) {
        i = Math.max(this.mMatchConstraintMinWidth, i);
      } else {
        i = this.mMatchConstraintMinWidth;
        if (i > 0) {
          this.mWidth = i;
        } else {
          i = 0;
        } 
      } 
      int k = this.mMatchConstraintMaxWidth;
      j = i;
      if (k > 0) {
        j = i;
        if (k < i)
          j = k; 
      } 
    } 
    return j;
  }
  
  public ConstraintWidget getParent() {
    return this.mParent;
  }
  
  int getRelativePositioning(int paramInt) {
    return (paramInt == 0) ? this.mRelX : ((paramInt == 1) ? this.mRelY : 0);
  }
  
  public ResolutionDimension getResolutionHeight() {
    if (this.mResolutionHeight == null)
      this.mResolutionHeight = new ResolutionDimension(); 
    return this.mResolutionHeight;
  }
  
  public ResolutionDimension getResolutionWidth() {
    if (this.mResolutionWidth == null)
      this.mResolutionWidth = new ResolutionDimension(); 
    return this.mResolutionWidth;
  }
  
  public int getRight() {
    return getX() + this.mWidth;
  }
  
  public WidgetContainer getRootWidgetContainer() {
    ConstraintWidget constraintWidget;
    for (constraintWidget = this; constraintWidget.getParent() != null; constraintWidget = constraintWidget.getParent());
    return (constraintWidget instanceof WidgetContainer) ? (WidgetContainer)constraintWidget : null;
  }
  
  protected int getRootX() {
    return this.mX + this.mOffsetX;
  }
  
  protected int getRootY() {
    return this.mY + this.mOffsetY;
  }
  
  public int getTop() {
    return getY();
  }
  
  public String getType() {
    return this.mType;
  }
  
  public float getVerticalBiasPercent() {
    return this.mVerticalBiasPercent;
  }
  
  public ConstraintWidget getVerticalChainControlWidget() {
    ConstraintWidget constraintWidget;
    if (isInVerticalChain()) {
      ConstraintWidget constraintWidget1 = this;
      ConstraintAnchor constraintAnchor = null;
      while (true) {
        constraintWidget = (ConstraintWidget)constraintAnchor;
        if (constraintAnchor == null) {
          constraintWidget = (ConstraintWidget)constraintAnchor;
          if (constraintWidget1 != null) {
            ConstraintWidget constraintWidget2;
            ConstraintAnchor constraintAnchor1;
            constraintWidget = (ConstraintWidget)constraintWidget1.getAnchor(ConstraintAnchor.Type.TOP);
            if (constraintWidget == null) {
              constraintWidget = null;
            } else {
              constraintWidget = (ConstraintWidget)constraintWidget.getTarget();
            } 
            if (constraintWidget == null) {
              constraintWidget = null;
            } else {
              constraintWidget2 = constraintWidget.getOwner();
            } 
            if (constraintWidget2 == getParent()) {
              constraintWidget2 = constraintWidget1;
              break;
            } 
            if (constraintWidget2 == null) {
              constraintAnchor1 = null;
            } else {
              constraintAnchor1 = constraintWidget2.getAnchor(ConstraintAnchor.Type.BOTTOM).getTarget();
            } 
            if (constraintAnchor1 != null && constraintAnchor1.getOwner() != constraintWidget1) {
              ConstraintWidget constraintWidget3 = constraintWidget1;
              continue;
            } 
            constraintWidget1 = constraintWidget2;
            continue;
          } 
        } 
        break;
      } 
    } else {
      constraintWidget = null;
    } 
    return constraintWidget;
  }
  
  public int getVerticalChainStyle() {
    return this.mVerticalChainStyle;
  }
  
  public DimensionBehaviour getVerticalDimensionBehaviour() {
    return this.mListDimensionBehaviors[1];
  }
  
  public int getVisibility() {
    return this.mVisibility;
  }
  
  public int getWidth() {
    return (this.mVisibility == 8) ? 0 : this.mWidth;
  }
  
  public int getWrapHeight() {
    return this.mWrapHeight;
  }
  
  public int getWrapWidth() {
    return this.mWrapWidth;
  }
  
  public int getX() {
    return this.mX;
  }
  
  public int getY() {
    return this.mY;
  }
  
  public boolean hasAncestor(ConstraintWidget paramConstraintWidget) {
    ConstraintWidget constraintWidget1 = getParent();
    if (constraintWidget1 == paramConstraintWidget)
      return true; 
    ConstraintWidget constraintWidget2 = constraintWidget1;
    if (constraintWidget1 == paramConstraintWidget.getParent())
      return false; 
    while (constraintWidget2 != null) {
      if (constraintWidget2 == paramConstraintWidget)
        return true; 
      if (constraintWidget2 == paramConstraintWidget.getParent())
        return true; 
      constraintWidget2 = constraintWidget2.getParent();
    } 
    return false;
  }
  
  public boolean hasBaseline() {
    boolean bool;
    if (this.mBaselineDistance > 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void immediateConnect(ConstraintAnchor.Type paramType1, ConstraintWidget paramConstraintWidget, ConstraintAnchor.Type paramType2, int paramInt1, int paramInt2) {
    getAnchor(paramType1).connect(paramConstraintWidget.getAnchor(paramType2), paramInt1, paramInt2, ConstraintAnchor.Strength.STRONG, 0, true);
  }
  
  public boolean isFullyResolved() {
    return ((this.mLeft.getResolutionNode()).state == 1 && (this.mRight.getResolutionNode()).state == 1 && (this.mTop.getResolutionNode()).state == 1 && (this.mBottom.getResolutionNode()).state == 1);
  }
  
  public boolean isHeightWrapContent() {
    return this.mIsHeightWrapContent;
  }
  
  public boolean isInHorizontalChain() {
    return ((this.mLeft.mTarget != null && this.mLeft.mTarget.mTarget == this.mLeft) || (this.mRight.mTarget != null && this.mRight.mTarget.mTarget == this.mRight));
  }
  
  public boolean isInVerticalChain() {
    return ((this.mTop.mTarget != null && this.mTop.mTarget.mTarget == this.mTop) || (this.mBottom.mTarget != null && this.mBottom.mTarget.mTarget == this.mBottom));
  }
  
  public boolean isInsideConstraintLayout() {
    ConstraintWidget constraintWidget1 = getParent();
    ConstraintWidget constraintWidget2 = constraintWidget1;
    if (constraintWidget1 == null)
      return false; 
    while (constraintWidget2 != null) {
      if (constraintWidget2 instanceof ConstraintWidgetContainer)
        return true; 
      constraintWidget2 = constraintWidget2.getParent();
    } 
    return false;
  }
  
  public boolean isRoot() {
    boolean bool;
    if (this.mParent == null) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isRootContainer() {
    if (this instanceof ConstraintWidgetContainer) {
      ConstraintWidget constraintWidget = this.mParent;
      if (constraintWidget == null || !(constraintWidget instanceof ConstraintWidgetContainer))
        return true; 
    } 
    return false;
  }
  
  public boolean isSpreadHeight() {
    int i = this.mMatchConstraintDefaultHeight;
    boolean bool = true;
    if (i != 0 || this.mDimensionRatio != 0.0F || this.mMatchConstraintMinHeight != 0 || this.mMatchConstraintMaxHeight != 0 || this.mListDimensionBehaviors[1] != DimensionBehaviour.MATCH_CONSTRAINT)
      bool = false; 
    return bool;
  }
  
  public boolean isSpreadWidth() {
    int i = this.mMatchConstraintDefaultWidth;
    boolean bool1 = false;
    boolean bool2 = bool1;
    if (i == 0) {
      bool2 = bool1;
      if (this.mDimensionRatio == 0.0F) {
        bool2 = bool1;
        if (this.mMatchConstraintMinWidth == 0) {
          bool2 = bool1;
          if (this.mMatchConstraintMaxWidth == 0) {
            bool2 = bool1;
            if (this.mListDimensionBehaviors[0] == DimensionBehaviour.MATCH_CONSTRAINT)
              bool2 = true; 
          } 
        } 
      } 
    } 
    return bool2;
  }
  
  public boolean isWidthWrapContent() {
    return this.mIsWidthWrapContent;
  }
  
  public void reset() {
    this.mLeft.reset();
    this.mTop.reset();
    this.mRight.reset();
    this.mBottom.reset();
    this.mBaseline.reset();
    this.mCenterX.reset();
    this.mCenterY.reset();
    this.mCenter.reset();
    this.mParent = null;
    this.mCircleConstraintAngle = 0.0F;
    this.mWidth = 0;
    this.mHeight = 0;
    this.mDimensionRatio = 0.0F;
    this.mDimensionRatioSide = -1;
    this.mX = 0;
    this.mY = 0;
    this.mDrawX = 0;
    this.mDrawY = 0;
    this.mDrawWidth = 0;
    this.mDrawHeight = 0;
    this.mOffsetX = 0;
    this.mOffsetY = 0;
    this.mBaselineDistance = 0;
    this.mMinWidth = 0;
    this.mMinHeight = 0;
    this.mWrapWidth = 0;
    this.mWrapHeight = 0;
    float f = DEFAULT_BIAS;
    this.mHorizontalBiasPercent = f;
    this.mVerticalBiasPercent = f;
    this.mListDimensionBehaviors[0] = DimensionBehaviour.FIXED;
    this.mListDimensionBehaviors[1] = DimensionBehaviour.FIXED;
    this.mCompanionWidget = null;
    this.mContainerItemSkip = 0;
    this.mVisibility = 0;
    this.mType = null;
    this.mHorizontalWrapVisited = false;
    this.mVerticalWrapVisited = false;
    this.mHorizontalChainStyle = 0;
    this.mVerticalChainStyle = 0;
    this.mHorizontalChainFixedPosition = false;
    this.mVerticalChainFixedPosition = false;
    float[] arrayOfFloat = this.mWeight;
    arrayOfFloat[0] = -1.0F;
    arrayOfFloat[1] = -1.0F;
    this.mHorizontalResolution = -1;
    this.mVerticalResolution = -1;
    int[] arrayOfInt = this.mMaxDimension;
    arrayOfInt[0] = Integer.MAX_VALUE;
    arrayOfInt[1] = Integer.MAX_VALUE;
    this.mMatchConstraintDefaultWidth = 0;
    this.mMatchConstraintDefaultHeight = 0;
    this.mMatchConstraintPercentWidth = 1.0F;
    this.mMatchConstraintPercentHeight = 1.0F;
    this.mMatchConstraintMaxWidth = Integer.MAX_VALUE;
    this.mMatchConstraintMaxHeight = Integer.MAX_VALUE;
    this.mMatchConstraintMinWidth = 0;
    this.mMatchConstraintMinHeight = 0;
    this.mResolvedDimensionRatioSide = -1;
    this.mResolvedDimensionRatio = 1.0F;
    ResolutionDimension resolutionDimension = this.mResolutionWidth;
    if (resolutionDimension != null)
      resolutionDimension.reset(); 
    resolutionDimension = this.mResolutionHeight;
    if (resolutionDimension != null)
      resolutionDimension.reset(); 
    this.mBelongingGroup = null;
    this.mOptimizerMeasurable = false;
    this.mOptimizerMeasured = false;
    this.mGroupsToSolver = false;
  }
  
  public void resetAllConstraints() {
    resetAnchors();
    setVerticalBiasPercent(DEFAULT_BIAS);
    setHorizontalBiasPercent(DEFAULT_BIAS);
    if (this instanceof ConstraintWidgetContainer)
      return; 
    if (getHorizontalDimensionBehaviour() == DimensionBehaviour.MATCH_CONSTRAINT)
      if (getWidth() == getWrapWidth()) {
        setHorizontalDimensionBehaviour(DimensionBehaviour.WRAP_CONTENT);
      } else if (getWidth() > getMinWidth()) {
        setHorizontalDimensionBehaviour(DimensionBehaviour.FIXED);
      }  
    if (getVerticalDimensionBehaviour() == DimensionBehaviour.MATCH_CONSTRAINT)
      if (getHeight() == getWrapHeight()) {
        setVerticalDimensionBehaviour(DimensionBehaviour.WRAP_CONTENT);
      } else if (getHeight() > getMinHeight()) {
        setVerticalDimensionBehaviour(DimensionBehaviour.FIXED);
      }  
  }
  
  public void resetAnchor(ConstraintAnchor paramConstraintAnchor) {
    if (getParent() != null && getParent() instanceof ConstraintWidgetContainer && ((ConstraintWidgetContainer)getParent()).handlesInternalConstraints())
      return; 
    ConstraintAnchor constraintAnchor1 = getAnchor(ConstraintAnchor.Type.LEFT);
    ConstraintAnchor constraintAnchor2 = getAnchor(ConstraintAnchor.Type.RIGHT);
    ConstraintAnchor constraintAnchor3 = getAnchor(ConstraintAnchor.Type.TOP);
    ConstraintAnchor constraintAnchor4 = getAnchor(ConstraintAnchor.Type.BOTTOM);
    ConstraintAnchor constraintAnchor5 = getAnchor(ConstraintAnchor.Type.CENTER);
    ConstraintAnchor constraintAnchor6 = getAnchor(ConstraintAnchor.Type.CENTER_X);
    ConstraintAnchor constraintAnchor7 = getAnchor(ConstraintAnchor.Type.CENTER_Y);
    if (paramConstraintAnchor == constraintAnchor5) {
      if (constraintAnchor1.isConnected() && constraintAnchor2.isConnected() && constraintAnchor1.getTarget() == constraintAnchor2.getTarget()) {
        constraintAnchor1.reset();
        constraintAnchor2.reset();
      } 
      if (constraintAnchor3.isConnected() && constraintAnchor4.isConnected() && constraintAnchor3.getTarget() == constraintAnchor4.getTarget()) {
        constraintAnchor3.reset();
        constraintAnchor4.reset();
      } 
      this.mHorizontalBiasPercent = 0.5F;
      this.mVerticalBiasPercent = 0.5F;
    } else if (paramConstraintAnchor == constraintAnchor6) {
      if (constraintAnchor1.isConnected() && constraintAnchor2.isConnected() && constraintAnchor1.getTarget().getOwner() == constraintAnchor2.getTarget().getOwner()) {
        constraintAnchor1.reset();
        constraintAnchor2.reset();
      } 
      this.mHorizontalBiasPercent = 0.5F;
    } else if (paramConstraintAnchor == constraintAnchor7) {
      if (constraintAnchor3.isConnected() && constraintAnchor4.isConnected() && constraintAnchor3.getTarget().getOwner() == constraintAnchor4.getTarget().getOwner()) {
        constraintAnchor3.reset();
        constraintAnchor4.reset();
      } 
      this.mVerticalBiasPercent = 0.5F;
    } else if (paramConstraintAnchor == constraintAnchor1 || paramConstraintAnchor == constraintAnchor2) {
      if (constraintAnchor1.isConnected() && constraintAnchor1.getTarget() == constraintAnchor2.getTarget())
        constraintAnchor5.reset(); 
    } else if ((paramConstraintAnchor == constraintAnchor3 || paramConstraintAnchor == constraintAnchor4) && constraintAnchor3.isConnected() && constraintAnchor3.getTarget() == constraintAnchor4.getTarget()) {
      constraintAnchor5.reset();
    } 
    paramConstraintAnchor.reset();
  }
  
  public void resetAnchors() {
    ConstraintWidget constraintWidget = getParent();
    if (constraintWidget != null && constraintWidget instanceof ConstraintWidgetContainer && ((ConstraintWidgetContainer)getParent()).handlesInternalConstraints())
      return; 
    byte b = 0;
    int i = this.mAnchors.size();
    while (b < i) {
      ((ConstraintAnchor)this.mAnchors.get(b)).reset();
      b++;
    } 
  }
  
  public void resetAnchors(int paramInt) {
    ConstraintWidget constraintWidget = getParent();
    if (constraintWidget != null && constraintWidget instanceof ConstraintWidgetContainer && ((ConstraintWidgetContainer)getParent()).handlesInternalConstraints())
      return; 
    byte b = 0;
    int i = this.mAnchors.size();
    while (b < i) {
      ConstraintAnchor constraintAnchor = this.mAnchors.get(b);
      if (paramInt == constraintAnchor.getConnectionCreator()) {
        if (constraintAnchor.isVerticalAnchor()) {
          setVerticalBiasPercent(DEFAULT_BIAS);
        } else {
          setHorizontalBiasPercent(DEFAULT_BIAS);
        } 
        constraintAnchor.reset();
      } 
      b++;
    } 
  }
  
  public void resetResolutionNodes() {
    for (byte b = 0; b < 6; b++)
      this.mListAnchors[b].getResolutionNode().reset(); 
  }
  
  public void resetSolverVariables(Cache paramCache) {
    this.mLeft.resetSolverVariable(paramCache);
    this.mTop.resetSolverVariable(paramCache);
    this.mRight.resetSolverVariable(paramCache);
    this.mBottom.resetSolverVariable(paramCache);
    this.mBaseline.resetSolverVariable(paramCache);
    this.mCenter.resetSolverVariable(paramCache);
    this.mCenterX.resetSolverVariable(paramCache);
    this.mCenterY.resetSolverVariable(paramCache);
  }
  
  public void resolve() {}
  
  public void setBaselineDistance(int paramInt) {
    this.mBaselineDistance = paramInt;
  }
  
  public void setCompanionWidget(Object paramObject) {
    this.mCompanionWidget = paramObject;
  }
  
  public void setContainerItemSkip(int paramInt) {
    if (paramInt >= 0) {
      this.mContainerItemSkip = paramInt;
    } else {
      this.mContainerItemSkip = 0;
    } 
  }
  
  public void setDebugName(String paramString) {
    this.mDebugName = paramString;
  }
  
  public void setDebugSolverName(LinearSystem paramLinearSystem, String paramString) {
    this.mDebugName = paramString;
    SolverVariable solverVariable1 = paramLinearSystem.createObjectVariable(this.mLeft);
    SolverVariable solverVariable2 = paramLinearSystem.createObjectVariable(this.mTop);
    SolverVariable solverVariable3 = paramLinearSystem.createObjectVariable(this.mRight);
    SolverVariable solverVariable4 = paramLinearSystem.createObjectVariable(this.mBottom);
    StringBuilder stringBuilder3 = new StringBuilder();
    stringBuilder3.append(paramString);
    stringBuilder3.append(".left");
    solverVariable1.setName(stringBuilder3.toString());
    stringBuilder3 = new StringBuilder();
    stringBuilder3.append(paramString);
    stringBuilder3.append(".top");
    solverVariable2.setName(stringBuilder3.toString());
    StringBuilder stringBuilder1 = new StringBuilder();
    stringBuilder1.append(paramString);
    stringBuilder1.append(".right");
    solverVariable3.setName(stringBuilder1.toString());
    StringBuilder stringBuilder2 = new StringBuilder();
    stringBuilder2.append(paramString);
    stringBuilder2.append(".bottom");
    solverVariable4.setName(stringBuilder2.toString());
    if (this.mBaselineDistance > 0) {
      SolverVariable solverVariable = paramLinearSystem.createObjectVariable(this.mBaseline);
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(paramString);
      stringBuilder.append(".baseline");
      solverVariable.setName(stringBuilder.toString());
    } 
  }
  
  public void setDimension(int paramInt1, int paramInt2) {
    this.mWidth = paramInt1;
    int i = this.mWidth;
    paramInt1 = this.mMinWidth;
    if (i < paramInt1)
      this.mWidth = paramInt1; 
    this.mHeight = paramInt2;
    paramInt2 = this.mHeight;
    paramInt1 = this.mMinHeight;
    if (paramInt2 < paramInt1)
      this.mHeight = paramInt1; 
  }
  
  public void setDimensionRatio(float paramFloat, int paramInt) {
    this.mDimensionRatio = paramFloat;
    this.mDimensionRatioSide = paramInt;
  }
  
  public void setDimensionRatio(String paramString) {
    // Byte code:
    //   0: aload_1
    //   1: ifnull -> 263
    //   4: aload_1
    //   5: invokevirtual length : ()I
    //   8: ifne -> 14
    //   11: goto -> 263
    //   14: iconst_m1
    //   15: istore_2
    //   16: aload_1
    //   17: invokevirtual length : ()I
    //   20: istore_3
    //   21: aload_1
    //   22: bipush #44
    //   24: invokevirtual indexOf : (I)I
    //   27: istore #4
    //   29: iconst_0
    //   30: istore #5
    //   32: iload_2
    //   33: istore #6
    //   35: iload #5
    //   37: istore #7
    //   39: iload #4
    //   41: ifle -> 108
    //   44: iload_2
    //   45: istore #6
    //   47: iload #5
    //   49: istore #7
    //   51: iload #4
    //   53: iload_3
    //   54: iconst_1
    //   55: isub
    //   56: if_icmpge -> 108
    //   59: aload_1
    //   60: iconst_0
    //   61: iload #4
    //   63: invokevirtual substring : (II)Ljava/lang/String;
    //   66: astore #8
    //   68: aload #8
    //   70: ldc_w 'W'
    //   73: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   76: ifeq -> 85
    //   79: iconst_0
    //   80: istore #6
    //   82: goto -> 102
    //   85: iload_2
    //   86: istore #6
    //   88: aload #8
    //   90: ldc_w 'H'
    //   93: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   96: ifeq -> 102
    //   99: iconst_1
    //   100: istore #6
    //   102: iload #4
    //   104: iconst_1
    //   105: iadd
    //   106: istore #7
    //   108: aload_1
    //   109: bipush #58
    //   111: invokevirtual indexOf : (I)I
    //   114: istore_2
    //   115: iload_2
    //   116: iflt -> 217
    //   119: iload_2
    //   120: iload_3
    //   121: iconst_1
    //   122: isub
    //   123: if_icmpge -> 217
    //   126: aload_1
    //   127: iload #7
    //   129: iload_2
    //   130: invokevirtual substring : (II)Ljava/lang/String;
    //   133: astore #8
    //   135: aload_1
    //   136: iload_2
    //   137: iconst_1
    //   138: iadd
    //   139: invokevirtual substring : (I)Ljava/lang/String;
    //   142: astore_1
    //   143: aload #8
    //   145: invokevirtual length : ()I
    //   148: ifle -> 240
    //   151: aload_1
    //   152: invokevirtual length : ()I
    //   155: ifle -> 240
    //   158: aload #8
    //   160: invokestatic parseFloat : (Ljava/lang/String;)F
    //   163: fstore #9
    //   165: aload_1
    //   166: invokestatic parseFloat : (Ljava/lang/String;)F
    //   169: fstore #10
    //   171: fload #9
    //   173: fconst_0
    //   174: fcmpl
    //   175: ifle -> 240
    //   178: fload #10
    //   180: fconst_0
    //   181: fcmpl
    //   182: ifle -> 240
    //   185: iload #6
    //   187: iconst_1
    //   188: if_icmpne -> 204
    //   191: fload #10
    //   193: fload #9
    //   195: fdiv
    //   196: invokestatic abs : (F)F
    //   199: fstore #9
    //   201: goto -> 243
    //   204: fload #9
    //   206: fload #10
    //   208: fdiv
    //   209: invokestatic abs : (F)F
    //   212: fstore #9
    //   214: goto -> 243
    //   217: aload_1
    //   218: iload #7
    //   220: invokevirtual substring : (I)Ljava/lang/String;
    //   223: astore_1
    //   224: aload_1
    //   225: invokevirtual length : ()I
    //   228: ifle -> 240
    //   231: aload_1
    //   232: invokestatic parseFloat : (Ljava/lang/String;)F
    //   235: fstore #9
    //   237: goto -> 243
    //   240: fconst_0
    //   241: fstore #9
    //   243: fload #9
    //   245: fconst_0
    //   246: fcmpl
    //   247: ifle -> 262
    //   250: aload_0
    //   251: fload #9
    //   253: putfield mDimensionRatio : F
    //   256: aload_0
    //   257: iload #6
    //   259: putfield mDimensionRatioSide : I
    //   262: return
    //   263: aload_0
    //   264: fconst_0
    //   265: putfield mDimensionRatio : F
    //   268: return
    //   269: astore_1
    //   270: goto -> 240
    // Exception table:
    //   from	to	target	type
    //   158	171	269	java/lang/NumberFormatException
    //   191	201	269	java/lang/NumberFormatException
    //   204	214	269	java/lang/NumberFormatException
    //   231	237	269	java/lang/NumberFormatException
  }
  
  public void setDrawHeight(int paramInt) {
    this.mDrawHeight = paramInt;
  }
  
  public void setDrawOrigin(int paramInt1, int paramInt2) {
    this.mDrawX = paramInt1 - this.mOffsetX;
    this.mDrawY = paramInt2 - this.mOffsetY;
    this.mX = this.mDrawX;
    this.mY = this.mDrawY;
  }
  
  public void setDrawWidth(int paramInt) {
    this.mDrawWidth = paramInt;
  }
  
  public void setDrawX(int paramInt) {
    this.mDrawX = paramInt - this.mOffsetX;
    this.mX = this.mDrawX;
  }
  
  public void setDrawY(int paramInt) {
    this.mDrawY = paramInt - this.mOffsetY;
    this.mY = this.mDrawY;
  }
  
  public void setFrame(int paramInt1, int paramInt2, int paramInt3) {
    if (paramInt3 == 0) {
      setHorizontalDimension(paramInt1, paramInt2);
    } else if (paramInt3 == 1) {
      setVerticalDimension(paramInt1, paramInt2);
    } 
    this.mOptimizerMeasured = true;
  }
  
  public void setFrame(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    // Byte code:
    //   0: iload_3
    //   1: iload_1
    //   2: isub
    //   3: istore #5
    //   5: iload #4
    //   7: iload_2
    //   8: isub
    //   9: istore_3
    //   10: aload_0
    //   11: iload_1
    //   12: putfield mX : I
    //   15: aload_0
    //   16: iload_2
    //   17: putfield mY : I
    //   20: aload_0
    //   21: getfield mVisibility : I
    //   24: bipush #8
    //   26: if_icmpne -> 40
    //   29: aload_0
    //   30: iconst_0
    //   31: putfield mWidth : I
    //   34: aload_0
    //   35: iconst_0
    //   36: putfield mHeight : I
    //   39: return
    //   40: aload_0
    //   41: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   44: iconst_0
    //   45: aaload
    //   46: getstatic androidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour.FIXED : Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   49: if_acmpne -> 66
    //   52: aload_0
    //   53: getfield mWidth : I
    //   56: istore_1
    //   57: iload #5
    //   59: iload_1
    //   60: if_icmpge -> 66
    //   63: goto -> 69
    //   66: iload #5
    //   68: istore_1
    //   69: aload_0
    //   70: getfield mListDimensionBehaviors : [Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   73: iconst_1
    //   74: aaload
    //   75: getstatic androidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour.FIXED : Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   78: if_acmpne -> 94
    //   81: aload_0
    //   82: getfield mHeight : I
    //   85: istore_2
    //   86: iload_3
    //   87: iload_2
    //   88: if_icmpge -> 94
    //   91: goto -> 96
    //   94: iload_3
    //   95: istore_2
    //   96: aload_0
    //   97: iload_1
    //   98: putfield mWidth : I
    //   101: aload_0
    //   102: iload_2
    //   103: putfield mHeight : I
    //   106: aload_0
    //   107: getfield mHeight : I
    //   110: istore_1
    //   111: aload_0
    //   112: getfield mMinHeight : I
    //   115: istore_2
    //   116: iload_1
    //   117: iload_2
    //   118: if_icmpge -> 126
    //   121: aload_0
    //   122: iload_2
    //   123: putfield mHeight : I
    //   126: aload_0
    //   127: getfield mWidth : I
    //   130: istore_1
    //   131: aload_0
    //   132: getfield mMinWidth : I
    //   135: istore_2
    //   136: iload_1
    //   137: iload_2
    //   138: if_icmpge -> 146
    //   141: aload_0
    //   142: iload_2
    //   143: putfield mWidth : I
    //   146: aload_0
    //   147: iconst_1
    //   148: putfield mOptimizerMeasured : Z
    //   151: return
  }
  
  public void setGoneMargin(ConstraintAnchor.Type paramType, int paramInt) {
    int i = null.$SwitchMap$androidx$constraintlayout$solver$widgets$ConstraintAnchor$Type[paramType.ordinal()];
    if (i != 1) {
      if (i != 2) {
        if (i != 3) {
          if (i == 4)
            this.mBottom.mGoneMargin = paramInt; 
        } else {
          this.mRight.mGoneMargin = paramInt;
        } 
      } else {
        this.mTop.mGoneMargin = paramInt;
      } 
    } else {
      this.mLeft.mGoneMargin = paramInt;
    } 
  }
  
  public void setHeight(int paramInt) {
    this.mHeight = paramInt;
    int i = this.mHeight;
    paramInt = this.mMinHeight;
    if (i < paramInt)
      this.mHeight = paramInt; 
  }
  
  public void setHeightWrapContent(boolean paramBoolean) {
    this.mIsHeightWrapContent = paramBoolean;
  }
  
  public void setHorizontalBiasPercent(float paramFloat) {
    this.mHorizontalBiasPercent = paramFloat;
  }
  
  public void setHorizontalChainStyle(int paramInt) {
    this.mHorizontalChainStyle = paramInt;
  }
  
  public void setHorizontalDimension(int paramInt1, int paramInt2) {
    this.mX = paramInt1;
    this.mWidth = paramInt2 - paramInt1;
    paramInt1 = this.mWidth;
    paramInt2 = this.mMinWidth;
    if (paramInt1 < paramInt2)
      this.mWidth = paramInt2; 
  }
  
  public void setHorizontalDimensionBehaviour(DimensionBehaviour paramDimensionBehaviour) {
    this.mListDimensionBehaviors[0] = paramDimensionBehaviour;
    if (paramDimensionBehaviour == DimensionBehaviour.WRAP_CONTENT)
      setWidth(this.mWrapWidth); 
  }
  
  public void setHorizontalMatchStyle(int paramInt1, int paramInt2, int paramInt3, float paramFloat) {
    this.mMatchConstraintDefaultWidth = paramInt1;
    this.mMatchConstraintMinWidth = paramInt2;
    this.mMatchConstraintMaxWidth = paramInt3;
    this.mMatchConstraintPercentWidth = paramFloat;
    if (paramFloat < 1.0F && this.mMatchConstraintDefaultWidth == 0)
      this.mMatchConstraintDefaultWidth = 2; 
  }
  
  public void setHorizontalWeight(float paramFloat) {
    this.mWeight[0] = paramFloat;
  }
  
  public void setLength(int paramInt1, int paramInt2) {
    if (paramInt2 == 0) {
      setWidth(paramInt1);
    } else if (paramInt2 == 1) {
      setHeight(paramInt1);
    } 
  }
  
  public void setMaxHeight(int paramInt) {
    this.mMaxDimension[1] = paramInt;
  }
  
  public void setMaxWidth(int paramInt) {
    this.mMaxDimension[0] = paramInt;
  }
  
  public void setMinHeight(int paramInt) {
    if (paramInt < 0) {
      this.mMinHeight = 0;
    } else {
      this.mMinHeight = paramInt;
    } 
  }
  
  public void setMinWidth(int paramInt) {
    if (paramInt < 0) {
      this.mMinWidth = 0;
    } else {
      this.mMinWidth = paramInt;
    } 
  }
  
  public void setOffset(int paramInt1, int paramInt2) {
    this.mOffsetX = paramInt1;
    this.mOffsetY = paramInt2;
  }
  
  public void setOrigin(int paramInt1, int paramInt2) {
    this.mX = paramInt1;
    this.mY = paramInt2;
  }
  
  public void setParent(ConstraintWidget paramConstraintWidget) {
    this.mParent = paramConstraintWidget;
  }
  
  void setRelativePositioning(int paramInt1, int paramInt2) {
    if (paramInt2 == 0) {
      this.mRelX = paramInt1;
    } else if (paramInt2 == 1) {
      this.mRelY = paramInt1;
    } 
  }
  
  public void setType(String paramString) {
    this.mType = paramString;
  }
  
  public void setVerticalBiasPercent(float paramFloat) {
    this.mVerticalBiasPercent = paramFloat;
  }
  
  public void setVerticalChainStyle(int paramInt) {
    this.mVerticalChainStyle = paramInt;
  }
  
  public void setVerticalDimension(int paramInt1, int paramInt2) {
    this.mY = paramInt1;
    this.mHeight = paramInt2 - paramInt1;
    paramInt1 = this.mHeight;
    paramInt2 = this.mMinHeight;
    if (paramInt1 < paramInt2)
      this.mHeight = paramInt2; 
  }
  
  public void setVerticalDimensionBehaviour(DimensionBehaviour paramDimensionBehaviour) {
    this.mListDimensionBehaviors[1] = paramDimensionBehaviour;
    if (paramDimensionBehaviour == DimensionBehaviour.WRAP_CONTENT)
      setHeight(this.mWrapHeight); 
  }
  
  public void setVerticalMatchStyle(int paramInt1, int paramInt2, int paramInt3, float paramFloat) {
    this.mMatchConstraintDefaultHeight = paramInt1;
    this.mMatchConstraintMinHeight = paramInt2;
    this.mMatchConstraintMaxHeight = paramInt3;
    this.mMatchConstraintPercentHeight = paramFloat;
    if (paramFloat < 1.0F && this.mMatchConstraintDefaultHeight == 0)
      this.mMatchConstraintDefaultHeight = 2; 
  }
  
  public void setVerticalWeight(float paramFloat) {
    this.mWeight[1] = paramFloat;
  }
  
  public void setVisibility(int paramInt) {
    this.mVisibility = paramInt;
  }
  
  public void setWidth(int paramInt) {
    this.mWidth = paramInt;
    paramInt = this.mWidth;
    int i = this.mMinWidth;
    if (paramInt < i)
      this.mWidth = i; 
  }
  
  public void setWidthWrapContent(boolean paramBoolean) {
    this.mIsWidthWrapContent = paramBoolean;
  }
  
  public void setWrapHeight(int paramInt) {
    this.mWrapHeight = paramInt;
  }
  
  public void setWrapWidth(int paramInt) {
    this.mWrapWidth = paramInt;
  }
  
  public void setX(int paramInt) {
    this.mX = paramInt;
  }
  
  public void setY(int paramInt) {
    this.mY = paramInt;
  }
  
  public void setupDimensionRatio(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4) {
    if (this.mResolvedDimensionRatioSide == -1)
      if (paramBoolean3 && !paramBoolean4) {
        this.mResolvedDimensionRatioSide = 0;
      } else if (!paramBoolean3 && paramBoolean4) {
        this.mResolvedDimensionRatioSide = 1;
        if (this.mDimensionRatioSide == -1)
          this.mResolvedDimensionRatio = 1.0F / this.mResolvedDimensionRatio; 
      }  
    if (this.mResolvedDimensionRatioSide == 0 && (!this.mTop.isConnected() || !this.mBottom.isConnected())) {
      this.mResolvedDimensionRatioSide = 1;
    } else if (this.mResolvedDimensionRatioSide == 1 && (!this.mLeft.isConnected() || !this.mRight.isConnected())) {
      this.mResolvedDimensionRatioSide = 0;
    } 
    if (this.mResolvedDimensionRatioSide == -1 && (!this.mTop.isConnected() || !this.mBottom.isConnected() || !this.mLeft.isConnected() || !this.mRight.isConnected()))
      if (this.mTop.isConnected() && this.mBottom.isConnected()) {
        this.mResolvedDimensionRatioSide = 0;
      } else if (this.mLeft.isConnected() && this.mRight.isConnected()) {
        this.mResolvedDimensionRatio = 1.0F / this.mResolvedDimensionRatio;
        this.mResolvedDimensionRatioSide = 1;
      }  
    if (this.mResolvedDimensionRatioSide == -1)
      if (paramBoolean1 && !paramBoolean2) {
        this.mResolvedDimensionRatioSide = 0;
      } else if (!paramBoolean1 && paramBoolean2) {
        this.mResolvedDimensionRatio = 1.0F / this.mResolvedDimensionRatio;
        this.mResolvedDimensionRatioSide = 1;
      }  
    if (this.mResolvedDimensionRatioSide == -1)
      if (this.mMatchConstraintMinWidth > 0 && this.mMatchConstraintMinHeight == 0) {
        this.mResolvedDimensionRatioSide = 0;
      } else if (this.mMatchConstraintMinWidth == 0 && this.mMatchConstraintMinHeight > 0) {
        this.mResolvedDimensionRatio = 1.0F / this.mResolvedDimensionRatio;
        this.mResolvedDimensionRatioSide = 1;
      }  
    if (this.mResolvedDimensionRatioSide == -1 && paramBoolean1 && paramBoolean2) {
      this.mResolvedDimensionRatio = 1.0F / this.mResolvedDimensionRatio;
      this.mResolvedDimensionRatioSide = 1;
    } 
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    String str1 = this.mType;
    String str2 = "";
    if (str1 != null) {
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("type: ");
      stringBuilder1.append(this.mType);
      stringBuilder1.append(" ");
      String str = stringBuilder1.toString();
    } else {
      str1 = "";
    } 
    stringBuilder.append(str1);
    str1 = str2;
    if (this.mDebugName != null) {
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("id: ");
      stringBuilder1.append(this.mDebugName);
      stringBuilder1.append(" ");
      str1 = stringBuilder1.toString();
    } 
    stringBuilder.append(str1);
    stringBuilder.append("(");
    stringBuilder.append(this.mX);
    stringBuilder.append(", ");
    stringBuilder.append(this.mY);
    stringBuilder.append(") - (");
    stringBuilder.append(this.mWidth);
    stringBuilder.append(" x ");
    stringBuilder.append(this.mHeight);
    stringBuilder.append(") wrap: (");
    stringBuilder.append(this.mWrapWidth);
    stringBuilder.append(" x ");
    stringBuilder.append(this.mWrapHeight);
    stringBuilder.append(")");
    return stringBuilder.toString();
  }
  
  public void updateDrawPosition() {
    int i = this.mX;
    int j = this.mY;
    int k = this.mWidth;
    int m = this.mHeight;
    this.mDrawX = i;
    this.mDrawY = j;
    this.mDrawWidth = k + i - i;
    this.mDrawHeight = m + j - j;
  }
  
  public void updateFromSolver(LinearSystem paramLinearSystem) {
    // Byte code:
    //   0: aload_1
    //   1: aload_0
    //   2: getfield mLeft : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   5: invokevirtual getObjectVariableValue : (Ljava/lang/Object;)I
    //   8: istore_2
    //   9: aload_1
    //   10: aload_0
    //   11: getfield mTop : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   14: invokevirtual getObjectVariableValue : (Ljava/lang/Object;)I
    //   17: istore_3
    //   18: aload_1
    //   19: aload_0
    //   20: getfield mRight : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   23: invokevirtual getObjectVariableValue : (Ljava/lang/Object;)I
    //   26: istore #4
    //   28: aload_1
    //   29: aload_0
    //   30: getfield mBottom : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   33: invokevirtual getObjectVariableValue : (Ljava/lang/Object;)I
    //   36: istore #5
    //   38: iload #4
    //   40: iload_2
    //   41: isub
    //   42: iflt -> 112
    //   45: iload #5
    //   47: iload_3
    //   48: isub
    //   49: iflt -> 112
    //   52: iload_2
    //   53: ldc_w -2147483648
    //   56: if_icmpeq -> 112
    //   59: iload_2
    //   60: ldc 2147483647
    //   62: if_icmpeq -> 112
    //   65: iload_3
    //   66: ldc_w -2147483648
    //   69: if_icmpeq -> 112
    //   72: iload_3
    //   73: ldc 2147483647
    //   75: if_icmpeq -> 112
    //   78: iload #4
    //   80: ldc_w -2147483648
    //   83: if_icmpeq -> 112
    //   86: iload #4
    //   88: ldc 2147483647
    //   90: if_icmpeq -> 112
    //   93: iload #5
    //   95: ldc_w -2147483648
    //   98: if_icmpeq -> 112
    //   101: iload #5
    //   103: istore #6
    //   105: iload #5
    //   107: ldc 2147483647
    //   109: if_icmpne -> 122
    //   112: iconst_0
    //   113: istore #6
    //   115: iconst_0
    //   116: istore_2
    //   117: iconst_0
    //   118: istore_3
    //   119: iconst_0
    //   120: istore #4
    //   122: aload_0
    //   123: iload_2
    //   124: iload_3
    //   125: iload #4
    //   127: iload #6
    //   129: invokevirtual setFrame : (IIII)V
    //   132: return
  }
  
  public void updateResolutionNodes() {
    for (byte b = 0; b < 6; b++)
      this.mListAnchors[b].getResolutionNode().update(); 
  }
  
  public enum ContentAlignment {
    BEGIN, BOTTOM, END, LEFT, MIDDLE, RIGHT, TOP, VERTICAL_MIDDLE;
    
    static {
      BOTTOM = new ContentAlignment("BOTTOM", 5);
      LEFT = new ContentAlignment("LEFT", 6);
      RIGHT = new ContentAlignment("RIGHT", 7);
      $VALUES = new ContentAlignment[] { BEGIN, MIDDLE, END, TOP, VERTICAL_MIDDLE, BOTTOM, LEFT, RIGHT };
    }
  }
  
  public enum DimensionBehaviour {
    FIXED, MATCH_CONSTRAINT, MATCH_PARENT, WRAP_CONTENT;
    
    static {
      $VALUES = new DimensionBehaviour[] { FIXED, WRAP_CONTENT, MATCH_CONSTRAINT, MATCH_PARENT };
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/androidx/constraintlayout/solver/widgets/ConstraintWidget.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */