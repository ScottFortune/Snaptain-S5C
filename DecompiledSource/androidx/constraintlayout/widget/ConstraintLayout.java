package androidx.constraintlayout.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import androidx.constraintlayout.solver.Metrics;
import androidx.constraintlayout.solver.widgets.Analyzer;
import androidx.constraintlayout.solver.widgets.ConstraintAnchor;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer;
import androidx.constraintlayout.solver.widgets.Guideline;
import java.util.ArrayList;
import java.util.HashMap;

public class ConstraintLayout extends ViewGroup {
  static final boolean ALLOWS_EMBEDDED = false;
  
  private static final boolean CACHE_MEASURED_DIMENSION = false;
  
  private static final boolean DEBUG = false;
  
  public static final int DESIGN_INFO_ID = 0;
  
  private static final String TAG = "ConstraintLayout";
  
  private static final boolean USE_CONSTRAINTS_HELPER = true;
  
  public static final String VERSION = "ConstraintLayout-1.1.3";
  
  SparseArray<View> mChildrenByIds = new SparseArray();
  
  private ArrayList<ConstraintHelper> mConstraintHelpers = new ArrayList<ConstraintHelper>(4);
  
  private ConstraintSet mConstraintSet = null;
  
  private int mConstraintSetId = -1;
  
  private HashMap<String, Integer> mDesignIds = new HashMap<String, Integer>();
  
  private boolean mDirtyHierarchy = true;
  
  private int mLastMeasureHeight = -1;
  
  int mLastMeasureHeightMode = 0;
  
  int mLastMeasureHeightSize = -1;
  
  private int mLastMeasureWidth = -1;
  
  int mLastMeasureWidthMode = 0;
  
  int mLastMeasureWidthSize = -1;
  
  ConstraintWidgetContainer mLayoutWidget = new ConstraintWidgetContainer();
  
  private int mMaxHeight = Integer.MAX_VALUE;
  
  private int mMaxWidth = Integer.MAX_VALUE;
  
  private Metrics mMetrics;
  
  private int mMinHeight = 0;
  
  private int mMinWidth = 0;
  
  private int mOptimizationLevel = 7;
  
  private final ArrayList<ConstraintWidget> mVariableDimensionsWidgets = new ArrayList<ConstraintWidget>(100);
  
  public ConstraintLayout(Context paramContext) {
    super(paramContext);
    init((AttributeSet)null);
  }
  
  public ConstraintLayout(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    init(paramAttributeSet);
  }
  
  public ConstraintLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    init(paramAttributeSet);
  }
  
  private final ConstraintWidget getTargetWidget(int paramInt) {
    ConstraintWidget constraintWidget;
    if (paramInt == 0)
      return (ConstraintWidget)this.mLayoutWidget; 
    View view1 = (View)this.mChildrenByIds.get(paramInt);
    View view2 = view1;
    if (view1 == null) {
      view1 = findViewById(paramInt);
      view2 = view1;
      if (view1 != null) {
        view2 = view1;
        if (view1 != this) {
          view2 = view1;
          if (view1.getParent() == this) {
            onViewAdded(view1);
            view2 = view1;
          } 
        } 
      } 
    } 
    if (view2 == this)
      return (ConstraintWidget)this.mLayoutWidget; 
    if (view2 == null) {
      view2 = null;
    } else {
      constraintWidget = ((LayoutParams)view2.getLayoutParams()).widget;
    } 
    return constraintWidget;
  }
  
  private void init(AttributeSet paramAttributeSet) {
    this.mLayoutWidget.setCompanionWidget(this);
    this.mChildrenByIds.put(getId(), this);
    this.mConstraintSet = null;
    if (paramAttributeSet != null) {
      TypedArray typedArray = getContext().obtainStyledAttributes(paramAttributeSet, R.styleable.ConstraintLayout_Layout);
      int i = typedArray.getIndexCount();
      for (byte b = 0; b < i; b++) {
        int j = typedArray.getIndex(b);
        if (j == R.styleable.ConstraintLayout_Layout_android_minWidth) {
          this.mMinWidth = typedArray.getDimensionPixelOffset(j, this.mMinWidth);
        } else if (j == R.styleable.ConstraintLayout_Layout_android_minHeight) {
          this.mMinHeight = typedArray.getDimensionPixelOffset(j, this.mMinHeight);
        } else if (j == R.styleable.ConstraintLayout_Layout_android_maxWidth) {
          this.mMaxWidth = typedArray.getDimensionPixelOffset(j, this.mMaxWidth);
        } else if (j == R.styleable.ConstraintLayout_Layout_android_maxHeight) {
          this.mMaxHeight = typedArray.getDimensionPixelOffset(j, this.mMaxHeight);
        } else if (j == R.styleable.ConstraintLayout_Layout_layout_optimizationLevel) {
          this.mOptimizationLevel = typedArray.getInt(j, this.mOptimizationLevel);
        } else if (j == R.styleable.ConstraintLayout_Layout_constraintSet) {
          j = typedArray.getResourceId(j, 0);
          try {
            ConstraintSet constraintSet = new ConstraintSet();
            this();
            this.mConstraintSet = constraintSet;
            this.mConstraintSet.load(getContext(), j);
          } catch (android.content.res.Resources.NotFoundException notFoundException) {
            this.mConstraintSet = null;
          } 
          this.mConstraintSetId = j;
        } 
      } 
      typedArray.recycle();
    } 
    this.mLayoutWidget.setOptimizationLevel(this.mOptimizationLevel);
  }
  
  private void internalMeasureChildren(int paramInt1, int paramInt2) {
    int i = getPaddingTop() + getPaddingBottom();
    int j = getPaddingLeft() + getPaddingRight();
    int k = getChildCount();
    for (byte b = 0; b < k; b++) {
      View view = getChildAt(b);
      if (view.getVisibility() != 8) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        ConstraintWidget constraintWidget = layoutParams.widget;
        if (!layoutParams.isGuideline && !layoutParams.isHelper) {
          int i1;
          int i2;
          int i3;
          boolean bool;
          constraintWidget.setVisibility(view.getVisibility());
          int m = layoutParams.width;
          int n = layoutParams.height;
          if (layoutParams.horizontalDimensionFixed || layoutParams.verticalDimensionFixed || (!layoutParams.horizontalDimensionFixed && layoutParams.matchConstraintDefaultWidth == 1) || layoutParams.width == -1 || (!layoutParams.verticalDimensionFixed && (layoutParams.matchConstraintDefaultHeight == 1 || layoutParams.height == -1))) {
            i1 = 1;
          } else {
            i1 = 0;
          } 
          if (i1) {
            boolean bool1;
            if (m == 0) {
              i2 = getChildMeasureSpec(paramInt1, j, -2);
              i1 = 1;
            } else if (m == -1) {
              i2 = getChildMeasureSpec(paramInt1, j, -1);
              i1 = 0;
            } else {
              if (m == -2) {
                i1 = 1;
              } else {
                i1 = 0;
              } 
              i2 = getChildMeasureSpec(paramInt1, j, m);
            } 
            if (n == 0) {
              i3 = getChildMeasureSpec(paramInt2, i, -2);
              bool = true;
            } else if (n == -1) {
              i3 = getChildMeasureSpec(paramInt2, i, -1);
              bool = false;
            } else {
              if (n == -2) {
                bool = true;
              } else {
                bool = false;
              } 
              i3 = getChildMeasureSpec(paramInt2, i, n);
            } 
            view.measure(i2, i3);
            Metrics metrics = this.mMetrics;
            if (metrics != null)
              metrics.measures++; 
            if (m == -2) {
              bool1 = true;
            } else {
              bool1 = false;
            } 
            constraintWidget.setWidthWrapContent(bool1);
            if (n == -2) {
              bool1 = true;
            } else {
              bool1 = false;
            } 
            constraintWidget.setHeightWrapContent(bool1);
            i3 = view.getMeasuredWidth();
            i2 = view.getMeasuredHeight();
          } else {
            i1 = 0;
            bool = false;
            i2 = n;
            i3 = m;
          } 
          constraintWidget.setWidth(i3);
          constraintWidget.setHeight(i2);
          if (i1)
            constraintWidget.setWrapWidth(i3); 
          if (bool)
            constraintWidget.setWrapHeight(i2); 
          if (layoutParams.needsBaseline) {
            i1 = view.getBaseline();
            if (i1 != -1)
              constraintWidget.setBaselineDistance(i1); 
          } 
        } 
      } 
    } 
  }
  
  private void internalMeasureDimensions(int paramInt1, int paramInt2) {
    // Byte code:
    //   0: aload_0
    //   1: astore_3
    //   2: aload_0
    //   3: invokevirtual getPaddingTop : ()I
    //   6: aload_0
    //   7: invokevirtual getPaddingBottom : ()I
    //   10: iadd
    //   11: istore #4
    //   13: aload_0
    //   14: invokevirtual getPaddingLeft : ()I
    //   17: aload_0
    //   18: invokevirtual getPaddingRight : ()I
    //   21: iadd
    //   22: istore #5
    //   24: aload_0
    //   25: invokevirtual getChildCount : ()I
    //   28: istore #6
    //   30: iconst_0
    //   31: istore #7
    //   33: lconst_1
    //   34: lstore #8
    //   36: iload #7
    //   38: iload #6
    //   40: if_icmpge -> 407
    //   43: aload_3
    //   44: iload #7
    //   46: invokevirtual getChildAt : (I)Landroid/view/View;
    //   49: astore #10
    //   51: aload #10
    //   53: invokevirtual getVisibility : ()I
    //   56: bipush #8
    //   58: if_icmpne -> 64
    //   61: goto -> 401
    //   64: aload #10
    //   66: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   69: checkcast androidx/constraintlayout/widget/ConstraintLayout$LayoutParams
    //   72: astore #11
    //   74: aload #11
    //   76: getfield widget : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   79: astore #12
    //   81: aload #11
    //   83: getfield isGuideline : Z
    //   86: ifne -> 401
    //   89: aload #11
    //   91: getfield isHelper : Z
    //   94: ifeq -> 100
    //   97: goto -> 401
    //   100: aload #12
    //   102: aload #10
    //   104: invokevirtual getVisibility : ()I
    //   107: invokevirtual setVisibility : (I)V
    //   110: aload #11
    //   112: getfield width : I
    //   115: istore #13
    //   117: aload #11
    //   119: getfield height : I
    //   122: istore #14
    //   124: iload #13
    //   126: ifeq -> 382
    //   129: iload #14
    //   131: ifne -> 137
    //   134: goto -> 382
    //   137: iload #13
    //   139: bipush #-2
    //   141: if_icmpne -> 150
    //   144: iconst_1
    //   145: istore #15
    //   147: goto -> 153
    //   150: iconst_0
    //   151: istore #15
    //   153: iload_1
    //   154: iload #5
    //   156: iload #13
    //   158: invokestatic getChildMeasureSpec : (III)I
    //   161: istore #16
    //   163: iload #14
    //   165: bipush #-2
    //   167: if_icmpne -> 176
    //   170: iconst_1
    //   171: istore #17
    //   173: goto -> 179
    //   176: iconst_0
    //   177: istore #17
    //   179: aload #10
    //   181: iload #16
    //   183: iload_2
    //   184: iload #4
    //   186: iload #14
    //   188: invokestatic getChildMeasureSpec : (III)I
    //   191: invokevirtual measure : (II)V
    //   194: aload_3
    //   195: getfield mMetrics : Landroidx/constraintlayout/solver/Metrics;
    //   198: astore #18
    //   200: aload #18
    //   202: ifnull -> 217
    //   205: aload #18
    //   207: aload #18
    //   209: getfield measures : J
    //   212: lconst_1
    //   213: ladd
    //   214: putfield measures : J
    //   217: iload #13
    //   219: bipush #-2
    //   221: if_icmpne -> 230
    //   224: iconst_1
    //   225: istore #19
    //   227: goto -> 233
    //   230: iconst_0
    //   231: istore #19
    //   233: aload #12
    //   235: iload #19
    //   237: invokevirtual setWidthWrapContent : (Z)V
    //   240: iload #14
    //   242: bipush #-2
    //   244: if_icmpne -> 253
    //   247: iconst_1
    //   248: istore #19
    //   250: goto -> 256
    //   253: iconst_0
    //   254: istore #19
    //   256: aload #12
    //   258: iload #19
    //   260: invokevirtual setHeightWrapContent : (Z)V
    //   263: aload #10
    //   265: invokevirtual getMeasuredWidth : ()I
    //   268: istore #13
    //   270: aload #10
    //   272: invokevirtual getMeasuredHeight : ()I
    //   275: istore #14
    //   277: aload #12
    //   279: iload #13
    //   281: invokevirtual setWidth : (I)V
    //   284: aload #12
    //   286: iload #14
    //   288: invokevirtual setHeight : (I)V
    //   291: iload #15
    //   293: ifeq -> 303
    //   296: aload #12
    //   298: iload #13
    //   300: invokevirtual setWrapWidth : (I)V
    //   303: iload #17
    //   305: ifeq -> 315
    //   308: aload #12
    //   310: iload #14
    //   312: invokevirtual setWrapHeight : (I)V
    //   315: aload #11
    //   317: getfield needsBaseline : Z
    //   320: ifeq -> 343
    //   323: aload #10
    //   325: invokevirtual getBaseline : ()I
    //   328: istore #15
    //   330: iload #15
    //   332: iconst_m1
    //   333: if_icmpeq -> 343
    //   336: aload #12
    //   338: iload #15
    //   340: invokevirtual setBaselineDistance : (I)V
    //   343: aload #11
    //   345: getfield horizontalDimensionFixed : Z
    //   348: ifeq -> 401
    //   351: aload #11
    //   353: getfield verticalDimensionFixed : Z
    //   356: ifeq -> 401
    //   359: aload #12
    //   361: invokevirtual getResolutionWidth : ()Landroidx/constraintlayout/solver/widgets/ResolutionDimension;
    //   364: iload #13
    //   366: invokevirtual resolve : (I)V
    //   369: aload #12
    //   371: invokevirtual getResolutionHeight : ()Landroidx/constraintlayout/solver/widgets/ResolutionDimension;
    //   374: iload #14
    //   376: invokevirtual resolve : (I)V
    //   379: goto -> 401
    //   382: aload #12
    //   384: invokevirtual getResolutionWidth : ()Landroidx/constraintlayout/solver/widgets/ResolutionDimension;
    //   387: invokevirtual invalidate : ()V
    //   390: aload #12
    //   392: invokevirtual getResolutionHeight : ()Landroidx/constraintlayout/solver/widgets/ResolutionDimension;
    //   395: invokevirtual invalidate : ()V
    //   398: goto -> 401
    //   401: iinc #7, 1
    //   404: goto -> 33
    //   407: aload_3
    //   408: getfield mLayoutWidget : Landroidx/constraintlayout/solver/widgets/ConstraintWidgetContainer;
    //   411: invokevirtual solveGraph : ()V
    //   414: iconst_0
    //   415: istore #20
    //   417: iload #20
    //   419: iload #6
    //   421: if_icmpge -> 1320
    //   424: aload_3
    //   425: iload #20
    //   427: invokevirtual getChildAt : (I)Landroid/view/View;
    //   430: astore #18
    //   432: aload #18
    //   434: invokevirtual getVisibility : ()I
    //   437: bipush #8
    //   439: if_icmpne -> 445
    //   442: goto -> 1308
    //   445: aload #18
    //   447: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   450: checkcast androidx/constraintlayout/widget/ConstraintLayout$LayoutParams
    //   453: astore #12
    //   455: aload #12
    //   457: getfield widget : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   460: astore #10
    //   462: aload #12
    //   464: getfield isGuideline : Z
    //   467: ifne -> 1308
    //   470: aload #12
    //   472: getfield isHelper : Z
    //   475: ifeq -> 481
    //   478: goto -> 1308
    //   481: aload #10
    //   483: aload #18
    //   485: invokevirtual getVisibility : ()I
    //   488: invokevirtual setVisibility : (I)V
    //   491: aload #12
    //   493: getfield width : I
    //   496: istore #14
    //   498: aload #12
    //   500: getfield height : I
    //   503: istore #16
    //   505: iload #14
    //   507: ifeq -> 518
    //   510: iload #16
    //   512: ifeq -> 518
    //   515: goto -> 1308
    //   518: aload #10
    //   520: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.LEFT : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   523: invokevirtual getAnchor : (Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   526: invokevirtual getResolutionNode : ()Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;
    //   529: astore #21
    //   531: aload #10
    //   533: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.RIGHT : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   536: invokevirtual getAnchor : (Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   539: invokevirtual getResolutionNode : ()Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;
    //   542: astore #22
    //   544: aload #10
    //   546: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.LEFT : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   549: invokevirtual getAnchor : (Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   552: invokevirtual getTarget : ()Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   555: ifnull -> 578
    //   558: aload #10
    //   560: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.RIGHT : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   563: invokevirtual getAnchor : (Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   566: invokevirtual getTarget : ()Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   569: ifnull -> 578
    //   572: iconst_1
    //   573: istore #15
    //   575: goto -> 581
    //   578: iconst_0
    //   579: istore #15
    //   581: aload #10
    //   583: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.TOP : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   586: invokevirtual getAnchor : (Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   589: invokevirtual getResolutionNode : ()Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;
    //   592: astore #23
    //   594: aload #10
    //   596: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.BOTTOM : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   599: invokevirtual getAnchor : (Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   602: invokevirtual getResolutionNode : ()Landroidx/constraintlayout/solver/widgets/ResolutionAnchor;
    //   605: astore #11
    //   607: aload #10
    //   609: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.TOP : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   612: invokevirtual getAnchor : (Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   615: invokevirtual getTarget : ()Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   618: ifnull -> 641
    //   621: aload #10
    //   623: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.BOTTOM : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   626: invokevirtual getAnchor : (Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   629: invokevirtual getTarget : ()Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   632: ifnull -> 641
    //   635: iconst_1
    //   636: istore #24
    //   638: goto -> 644
    //   641: iconst_0
    //   642: istore #24
    //   644: iload #14
    //   646: ifne -> 673
    //   649: iload #16
    //   651: ifne -> 673
    //   654: iload #15
    //   656: ifeq -> 673
    //   659: iload #24
    //   661: ifeq -> 673
    //   664: lconst_1
    //   665: lstore #8
    //   667: aload_3
    //   668: astore #12
    //   670: goto -> 1311
    //   673: aload_3
    //   674: getfield mLayoutWidget : Landroidx/constraintlayout/solver/widgets/ConstraintWidgetContainer;
    //   677: invokevirtual getHorizontalDimensionBehaviour : ()Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   680: getstatic androidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   683: if_acmpeq -> 692
    //   686: iconst_1
    //   687: istore #13
    //   689: goto -> 695
    //   692: iconst_0
    //   693: istore #13
    //   695: aload_3
    //   696: getfield mLayoutWidget : Landroidx/constraintlayout/solver/widgets/ConstraintWidgetContainer;
    //   699: invokevirtual getVerticalDimensionBehaviour : ()Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   702: getstatic androidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   705: if_acmpeq -> 714
    //   708: iconst_1
    //   709: istore #7
    //   711: goto -> 717
    //   714: iconst_0
    //   715: istore #7
    //   717: iload #13
    //   719: ifne -> 730
    //   722: aload #10
    //   724: invokevirtual getResolutionWidth : ()Landroidx/constraintlayout/solver/widgets/ResolutionDimension;
    //   727: invokevirtual invalidate : ()V
    //   730: iload #7
    //   732: ifne -> 743
    //   735: aload #10
    //   737: invokevirtual getResolutionHeight : ()Landroidx/constraintlayout/solver/widgets/ResolutionDimension;
    //   740: invokevirtual invalidate : ()V
    //   743: iload #14
    //   745: ifne -> 842
    //   748: iload #13
    //   750: ifeq -> 819
    //   753: aload #10
    //   755: invokevirtual isSpreadWidth : ()Z
    //   758: ifeq -> 819
    //   761: iload #15
    //   763: ifeq -> 819
    //   766: aload #21
    //   768: invokevirtual isResolved : ()Z
    //   771: ifeq -> 819
    //   774: aload #22
    //   776: invokevirtual isResolved : ()Z
    //   779: ifeq -> 819
    //   782: aload #22
    //   784: invokevirtual getResolvedValue : ()F
    //   787: aload #21
    //   789: invokevirtual getResolvedValue : ()F
    //   792: fsub
    //   793: f2i
    //   794: istore #14
    //   796: aload #10
    //   798: invokevirtual getResolutionWidth : ()Landroidx/constraintlayout/solver/widgets/ResolutionDimension;
    //   801: iload #14
    //   803: invokevirtual resolve : (I)V
    //   806: iload_1
    //   807: iload #5
    //   809: iload #14
    //   811: invokestatic getChildMeasureSpec : (III)I
    //   814: istore #17
    //   816: goto -> 857
    //   819: iload_1
    //   820: iload #5
    //   822: bipush #-2
    //   824: invokestatic getChildMeasureSpec : (III)I
    //   827: istore #17
    //   829: iconst_1
    //   830: istore #15
    //   832: iconst_0
    //   833: istore #25
    //   835: iload #14
    //   837: istore #26
    //   839: goto -> 905
    //   842: iload #14
    //   844: iconst_m1
    //   845: if_icmpne -> 871
    //   848: iload_1
    //   849: iload #5
    //   851: iconst_m1
    //   852: invokestatic getChildMeasureSpec : (III)I
    //   855: istore #17
    //   857: iconst_0
    //   858: istore #15
    //   860: iload #13
    //   862: istore #25
    //   864: iload #14
    //   866: istore #26
    //   868: goto -> 905
    //   871: iload #14
    //   873: bipush #-2
    //   875: if_icmpne -> 884
    //   878: iconst_1
    //   879: istore #15
    //   881: goto -> 887
    //   884: iconst_0
    //   885: istore #15
    //   887: iload_1
    //   888: iload #5
    //   890: iload #14
    //   892: invokestatic getChildMeasureSpec : (III)I
    //   895: istore #17
    //   897: iload #14
    //   899: istore #26
    //   901: iload #13
    //   903: istore #25
    //   905: iload #16
    //   907: ifne -> 1000
    //   910: iload #7
    //   912: ifeq -> 981
    //   915: aload #10
    //   917: invokevirtual isSpreadHeight : ()Z
    //   920: ifeq -> 981
    //   923: iload #24
    //   925: ifeq -> 981
    //   928: aload #23
    //   930: invokevirtual isResolved : ()Z
    //   933: ifeq -> 981
    //   936: aload #11
    //   938: invokevirtual isResolved : ()Z
    //   941: ifeq -> 981
    //   944: aload #11
    //   946: invokevirtual getResolvedValue : ()F
    //   949: aload #23
    //   951: invokevirtual getResolvedValue : ()F
    //   954: fsub
    //   955: f2i
    //   956: istore #16
    //   958: aload #10
    //   960: invokevirtual getResolutionHeight : ()Landroidx/constraintlayout/solver/widgets/ResolutionDimension;
    //   963: iload #16
    //   965: invokevirtual resolve : (I)V
    //   968: iload_2
    //   969: iload #4
    //   971: iload #16
    //   973: invokestatic getChildMeasureSpec : (III)I
    //   976: istore #14
    //   978: goto -> 1015
    //   981: iload_2
    //   982: iload #4
    //   984: bipush #-2
    //   986: invokestatic getChildMeasureSpec : (III)I
    //   989: istore #14
    //   991: iconst_1
    //   992: istore #7
    //   994: iconst_0
    //   995: istore #13
    //   997: goto -> 1063
    //   1000: iload #16
    //   1002: iconst_m1
    //   1003: if_icmpne -> 1025
    //   1006: iload_2
    //   1007: iload #4
    //   1009: iconst_m1
    //   1010: invokestatic getChildMeasureSpec : (III)I
    //   1013: istore #14
    //   1015: iload #7
    //   1017: istore #13
    //   1019: iconst_0
    //   1020: istore #7
    //   1022: goto -> 1063
    //   1025: iload #16
    //   1027: bipush #-2
    //   1029: if_icmpne -> 1038
    //   1032: iconst_1
    //   1033: istore #14
    //   1035: goto -> 1041
    //   1038: iconst_0
    //   1039: istore #14
    //   1041: iload_2
    //   1042: iload #4
    //   1044: iload #16
    //   1046: invokestatic getChildMeasureSpec : (III)I
    //   1049: istore #24
    //   1051: iload #7
    //   1053: istore #13
    //   1055: iload #14
    //   1057: istore #7
    //   1059: iload #24
    //   1061: istore #14
    //   1063: aload #18
    //   1065: iload #17
    //   1067: iload #14
    //   1069: invokevirtual measure : (II)V
    //   1072: aload_0
    //   1073: astore_3
    //   1074: aload_3
    //   1075: getfield mMetrics : Landroidx/constraintlayout/solver/Metrics;
    //   1078: astore #11
    //   1080: aload #11
    //   1082: ifnull -> 1100
    //   1085: aload #11
    //   1087: aload #11
    //   1089: getfield measures : J
    //   1092: lconst_1
    //   1093: ladd
    //   1094: putfield measures : J
    //   1097: goto -> 1100
    //   1100: lconst_1
    //   1101: lstore #27
    //   1103: iload #26
    //   1105: bipush #-2
    //   1107: if_icmpne -> 1116
    //   1110: iconst_1
    //   1111: istore #19
    //   1113: goto -> 1119
    //   1116: iconst_0
    //   1117: istore #19
    //   1119: aload #10
    //   1121: iload #19
    //   1123: invokevirtual setWidthWrapContent : (Z)V
    //   1126: iload #16
    //   1128: bipush #-2
    //   1130: if_icmpne -> 1139
    //   1133: iconst_1
    //   1134: istore #19
    //   1136: goto -> 1142
    //   1139: iconst_0
    //   1140: istore #19
    //   1142: aload #10
    //   1144: iload #19
    //   1146: invokevirtual setHeightWrapContent : (Z)V
    //   1149: aload #18
    //   1151: invokevirtual getMeasuredWidth : ()I
    //   1154: istore #14
    //   1156: aload #18
    //   1158: invokevirtual getMeasuredHeight : ()I
    //   1161: istore #17
    //   1163: aload #10
    //   1165: iload #14
    //   1167: invokevirtual setWidth : (I)V
    //   1170: aload #10
    //   1172: iload #17
    //   1174: invokevirtual setHeight : (I)V
    //   1177: iload #15
    //   1179: ifeq -> 1189
    //   1182: aload #10
    //   1184: iload #14
    //   1186: invokevirtual setWrapWidth : (I)V
    //   1189: iload #7
    //   1191: ifeq -> 1201
    //   1194: aload #10
    //   1196: iload #17
    //   1198: invokevirtual setWrapHeight : (I)V
    //   1201: iload #25
    //   1203: ifeq -> 1219
    //   1206: aload #10
    //   1208: invokevirtual getResolutionWidth : ()Landroidx/constraintlayout/solver/widgets/ResolutionDimension;
    //   1211: iload #14
    //   1213: invokevirtual resolve : (I)V
    //   1216: goto -> 1227
    //   1219: aload #10
    //   1221: invokevirtual getResolutionWidth : ()Landroidx/constraintlayout/solver/widgets/ResolutionDimension;
    //   1224: invokevirtual remove : ()V
    //   1227: iload #13
    //   1229: ifeq -> 1245
    //   1232: aload #10
    //   1234: invokevirtual getResolutionHeight : ()Landroidx/constraintlayout/solver/widgets/ResolutionDimension;
    //   1237: iload #17
    //   1239: invokevirtual resolve : (I)V
    //   1242: goto -> 1253
    //   1245: aload #10
    //   1247: invokevirtual getResolutionHeight : ()Landroidx/constraintlayout/solver/widgets/ResolutionDimension;
    //   1250: invokevirtual remove : ()V
    //   1253: aload #12
    //   1255: getfield needsBaseline : Z
    //   1258: ifeq -> 1298
    //   1261: aload #18
    //   1263: invokevirtual getBaseline : ()I
    //   1266: istore #7
    //   1268: aload_3
    //   1269: astore #12
    //   1271: lload #27
    //   1273: lstore #8
    //   1275: iload #7
    //   1277: iconst_m1
    //   1278: if_icmpeq -> 1311
    //   1281: aload #10
    //   1283: iload #7
    //   1285: invokevirtual setBaselineDistance : (I)V
    //   1288: aload_3
    //   1289: astore #12
    //   1291: lload #27
    //   1293: lstore #8
    //   1295: goto -> 1311
    //   1298: aload_3
    //   1299: astore #12
    //   1301: lload #27
    //   1303: lstore #8
    //   1305: goto -> 1311
    //   1308: aload_3
    //   1309: astore #12
    //   1311: iinc #20, 1
    //   1314: aload #12
    //   1316: astore_3
    //   1317: goto -> 417
    //   1320: return
  }
  
  private void setChildrenConstraints() {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual isInEditMode : ()Z
    //   4: istore_1
    //   5: aload_0
    //   6: invokevirtual getChildCount : ()I
    //   9: istore_2
    //   10: iconst_0
    //   11: istore_3
    //   12: iload_1
    //   13: ifeq -> 112
    //   16: iconst_0
    //   17: istore #4
    //   19: iload #4
    //   21: iload_2
    //   22: if_icmpge -> 112
    //   25: aload_0
    //   26: iload #4
    //   28: invokevirtual getChildAt : (I)Landroid/view/View;
    //   31: astore #5
    //   33: aload_0
    //   34: invokevirtual getResources : ()Landroid/content/res/Resources;
    //   37: aload #5
    //   39: invokevirtual getId : ()I
    //   42: invokevirtual getResourceName : (I)Ljava/lang/String;
    //   45: astore #6
    //   47: aload_0
    //   48: iconst_0
    //   49: aload #6
    //   51: aload #5
    //   53: invokevirtual getId : ()I
    //   56: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   59: invokevirtual setDesignInformation : (ILjava/lang/Object;Ljava/lang/Object;)V
    //   62: aload #6
    //   64: bipush #47
    //   66: invokevirtual indexOf : (I)I
    //   69: istore #7
    //   71: aload #6
    //   73: astore #8
    //   75: iload #7
    //   77: iconst_m1
    //   78: if_icmpeq -> 92
    //   81: aload #6
    //   83: iload #7
    //   85: iconst_1
    //   86: iadd
    //   87: invokevirtual substring : (I)Ljava/lang/String;
    //   90: astore #8
    //   92: aload_0
    //   93: aload #5
    //   95: invokevirtual getId : ()I
    //   98: invokespecial getTargetWidget : (I)Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   101: aload #8
    //   103: invokevirtual setDebugName : (Ljava/lang/String;)V
    //   106: iinc #4, 1
    //   109: goto -> 19
    //   112: iconst_0
    //   113: istore #4
    //   115: iload #4
    //   117: iload_2
    //   118: if_icmpge -> 152
    //   121: aload_0
    //   122: aload_0
    //   123: iload #4
    //   125: invokevirtual getChildAt : (I)Landroid/view/View;
    //   128: invokevirtual getViewWidget : (Landroid/view/View;)Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   131: astore #8
    //   133: aload #8
    //   135: ifnonnull -> 141
    //   138: goto -> 146
    //   141: aload #8
    //   143: invokevirtual reset : ()V
    //   146: iinc #4, 1
    //   149: goto -> 115
    //   152: aload_0
    //   153: getfield mConstraintSetId : I
    //   156: iconst_m1
    //   157: if_icmpeq -> 215
    //   160: iconst_0
    //   161: istore #4
    //   163: iload #4
    //   165: iload_2
    //   166: if_icmpge -> 215
    //   169: aload_0
    //   170: iload #4
    //   172: invokevirtual getChildAt : (I)Landroid/view/View;
    //   175: astore #8
    //   177: aload #8
    //   179: invokevirtual getId : ()I
    //   182: aload_0
    //   183: getfield mConstraintSetId : I
    //   186: if_icmpne -> 209
    //   189: aload #8
    //   191: instanceof androidx/constraintlayout/widget/Constraints
    //   194: ifeq -> 209
    //   197: aload_0
    //   198: aload #8
    //   200: checkcast androidx/constraintlayout/widget/Constraints
    //   203: invokevirtual getConstraintSet : ()Landroidx/constraintlayout/widget/ConstraintSet;
    //   206: putfield mConstraintSet : Landroidx/constraintlayout/widget/ConstraintSet;
    //   209: iinc #4, 1
    //   212: goto -> 163
    //   215: aload_0
    //   216: getfield mConstraintSet : Landroidx/constraintlayout/widget/ConstraintSet;
    //   219: astore #8
    //   221: aload #8
    //   223: ifnull -> 232
    //   226: aload #8
    //   228: aload_0
    //   229: invokevirtual applyToInternal : (Landroidx/constraintlayout/widget/ConstraintLayout;)V
    //   232: aload_0
    //   233: getfield mLayoutWidget : Landroidx/constraintlayout/solver/widgets/ConstraintWidgetContainer;
    //   236: invokevirtual removeAllChildren : ()V
    //   239: aload_0
    //   240: getfield mConstraintHelpers : Ljava/util/ArrayList;
    //   243: invokevirtual size : ()I
    //   246: istore #7
    //   248: iload #7
    //   250: ifle -> 285
    //   253: iconst_0
    //   254: istore #4
    //   256: iload #4
    //   258: iload #7
    //   260: if_icmpge -> 285
    //   263: aload_0
    //   264: getfield mConstraintHelpers : Ljava/util/ArrayList;
    //   267: iload #4
    //   269: invokevirtual get : (I)Ljava/lang/Object;
    //   272: checkcast androidx/constraintlayout/widget/ConstraintHelper
    //   275: aload_0
    //   276: invokevirtual updatePreLayout : (Landroidx/constraintlayout/widget/ConstraintLayout;)V
    //   279: iinc #4, 1
    //   282: goto -> 256
    //   285: iconst_0
    //   286: istore #4
    //   288: iload #4
    //   290: iload_2
    //   291: if_icmpge -> 325
    //   294: aload_0
    //   295: iload #4
    //   297: invokevirtual getChildAt : (I)Landroid/view/View;
    //   300: astore #8
    //   302: aload #8
    //   304: instanceof androidx/constraintlayout/widget/Placeholder
    //   307: ifeq -> 319
    //   310: aload #8
    //   312: checkcast androidx/constraintlayout/widget/Placeholder
    //   315: aload_0
    //   316: invokevirtual updatePreLayout : (Landroidx/constraintlayout/widget/ConstraintLayout;)V
    //   319: iinc #4, 1
    //   322: goto -> 288
    //   325: iconst_0
    //   326: istore #7
    //   328: iload_3
    //   329: istore #4
    //   331: iload #7
    //   333: iload_2
    //   334: if_icmpge -> 2056
    //   337: aload_0
    //   338: iload #7
    //   340: invokevirtual getChildAt : (I)Landroid/view/View;
    //   343: astore #5
    //   345: aload_0
    //   346: aload #5
    //   348: invokevirtual getViewWidget : (Landroid/view/View;)Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   351: astore #6
    //   353: aload #6
    //   355: ifnonnull -> 365
    //   358: iload #4
    //   360: istore #9
    //   362: goto -> 2046
    //   365: aload #5
    //   367: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   370: checkcast androidx/constraintlayout/widget/ConstraintLayout$LayoutParams
    //   373: astore #8
    //   375: aload #8
    //   377: invokevirtual validate : ()V
    //   380: aload #8
    //   382: getfield helped : Z
    //   385: ifeq -> 398
    //   388: aload #8
    //   390: iload #4
    //   392: putfield helped : Z
    //   395: goto -> 468
    //   398: iload_1
    //   399: ifeq -> 468
    //   402: aload_0
    //   403: invokevirtual getResources : ()Landroid/content/res/Resources;
    //   406: aload #5
    //   408: invokevirtual getId : ()I
    //   411: invokevirtual getResourceName : (I)Ljava/lang/String;
    //   414: astore #10
    //   416: aload_0
    //   417: iload #4
    //   419: aload #10
    //   421: aload #5
    //   423: invokevirtual getId : ()I
    //   426: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   429: invokevirtual setDesignInformation : (ILjava/lang/Object;Ljava/lang/Object;)V
    //   432: aload #10
    //   434: aload #10
    //   436: ldc_w 'id/'
    //   439: invokevirtual indexOf : (Ljava/lang/String;)I
    //   442: iconst_3
    //   443: iadd
    //   444: invokevirtual substring : (I)Ljava/lang/String;
    //   447: astore #10
    //   449: aload_0
    //   450: aload #5
    //   452: invokevirtual getId : ()I
    //   455: invokespecial getTargetWidget : (I)Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   458: aload #10
    //   460: invokevirtual setDebugName : (Ljava/lang/String;)V
    //   463: goto -> 468
    //   466: astore #10
    //   468: aload #6
    //   470: aload #5
    //   472: invokevirtual getVisibility : ()I
    //   475: invokevirtual setVisibility : (I)V
    //   478: aload #8
    //   480: getfield isInPlaceholder : Z
    //   483: ifeq -> 493
    //   486: aload #6
    //   488: bipush #8
    //   490: invokevirtual setVisibility : (I)V
    //   493: aload #6
    //   495: aload #5
    //   497: invokevirtual setCompanionWidget : (Ljava/lang/Object;)V
    //   500: aload_0
    //   501: getfield mLayoutWidget : Landroidx/constraintlayout/solver/widgets/ConstraintWidgetContainer;
    //   504: aload #6
    //   506: invokevirtual add : (Landroidx/constraintlayout/solver/widgets/ConstraintWidget;)V
    //   509: aload #8
    //   511: getfield verticalDimensionFixed : Z
    //   514: ifeq -> 525
    //   517: aload #8
    //   519: getfield horizontalDimensionFixed : Z
    //   522: ifne -> 535
    //   525: aload_0
    //   526: getfield mVariableDimensionsWidgets : Ljava/util/ArrayList;
    //   529: aload #6
    //   531: invokevirtual add : (Ljava/lang/Object;)Z
    //   534: pop
    //   535: aload #8
    //   537: getfield isGuideline : Z
    //   540: ifeq -> 663
    //   543: aload #6
    //   545: checkcast androidx/constraintlayout/solver/widgets/Guideline
    //   548: astore #6
    //   550: aload #8
    //   552: getfield resolvedGuideBegin : I
    //   555: istore #9
    //   557: aload #8
    //   559: getfield resolvedGuideEnd : I
    //   562: istore_3
    //   563: aload #8
    //   565: getfield resolvedGuidePercent : F
    //   568: fstore #11
    //   570: getstatic android/os/Build$VERSION.SDK_INT : I
    //   573: bipush #17
    //   575: if_icmpge -> 598
    //   578: aload #8
    //   580: getfield guideBegin : I
    //   583: istore #9
    //   585: aload #8
    //   587: getfield guideEnd : I
    //   590: istore_3
    //   591: aload #8
    //   593: getfield guidePercent : F
    //   596: fstore #11
    //   598: fload #11
    //   600: ldc_w -1.0
    //   603: fcmpl
    //   604: ifeq -> 621
    //   607: aload #6
    //   609: fload #11
    //   611: invokevirtual setGuidePercent : (F)V
    //   614: iload #4
    //   616: istore #9
    //   618: goto -> 2046
    //   621: iload #9
    //   623: iconst_m1
    //   624: if_icmpeq -> 641
    //   627: aload #6
    //   629: iload #9
    //   631: invokevirtual setGuideBegin : (I)V
    //   634: iload #4
    //   636: istore #9
    //   638: goto -> 2046
    //   641: iload #4
    //   643: istore #9
    //   645: iload_3
    //   646: iconst_m1
    //   647: if_icmpeq -> 2046
    //   650: aload #6
    //   652: iload_3
    //   653: invokevirtual setGuideEnd : (I)V
    //   656: iload #4
    //   658: istore #9
    //   660: goto -> 2046
    //   663: aload #8
    //   665: getfield leftToLeft : I
    //   668: iconst_m1
    //   669: if_icmpne -> 829
    //   672: aload #8
    //   674: getfield leftToRight : I
    //   677: iconst_m1
    //   678: if_icmpne -> 829
    //   681: aload #8
    //   683: getfield rightToLeft : I
    //   686: iconst_m1
    //   687: if_icmpne -> 829
    //   690: aload #8
    //   692: getfield rightToRight : I
    //   695: iconst_m1
    //   696: if_icmpne -> 829
    //   699: aload #8
    //   701: getfield startToStart : I
    //   704: iconst_m1
    //   705: if_icmpne -> 829
    //   708: aload #8
    //   710: getfield startToEnd : I
    //   713: iconst_m1
    //   714: if_icmpne -> 829
    //   717: aload #8
    //   719: getfield endToStart : I
    //   722: iconst_m1
    //   723: if_icmpne -> 829
    //   726: aload #8
    //   728: getfield endToEnd : I
    //   731: iconst_m1
    //   732: if_icmpne -> 829
    //   735: aload #8
    //   737: getfield topToTop : I
    //   740: iconst_m1
    //   741: if_icmpne -> 829
    //   744: aload #8
    //   746: getfield topToBottom : I
    //   749: iconst_m1
    //   750: if_icmpne -> 829
    //   753: aload #8
    //   755: getfield bottomToTop : I
    //   758: iconst_m1
    //   759: if_icmpne -> 829
    //   762: aload #8
    //   764: getfield bottomToBottom : I
    //   767: iconst_m1
    //   768: if_icmpne -> 829
    //   771: aload #8
    //   773: getfield baselineToBaseline : I
    //   776: iconst_m1
    //   777: if_icmpne -> 829
    //   780: aload #8
    //   782: getfield editorAbsoluteX : I
    //   785: iconst_m1
    //   786: if_icmpne -> 829
    //   789: aload #8
    //   791: getfield editorAbsoluteY : I
    //   794: iconst_m1
    //   795: if_icmpne -> 829
    //   798: aload #8
    //   800: getfield circleConstraint : I
    //   803: iconst_m1
    //   804: if_icmpne -> 829
    //   807: aload #8
    //   809: getfield width : I
    //   812: iconst_m1
    //   813: if_icmpeq -> 829
    //   816: iload #4
    //   818: istore #9
    //   820: aload #8
    //   822: getfield height : I
    //   825: iconst_m1
    //   826: if_icmpne -> 2046
    //   829: aload #8
    //   831: getfield resolvedLeftToLeft : I
    //   834: istore #12
    //   836: aload #8
    //   838: getfield resolvedLeftToRight : I
    //   841: istore #13
    //   843: aload #8
    //   845: getfield resolvedRightToLeft : I
    //   848: istore #9
    //   850: aload #8
    //   852: getfield resolvedRightToRight : I
    //   855: istore #4
    //   857: aload #8
    //   859: getfield resolveGoneLeftMargin : I
    //   862: istore #14
    //   864: aload #8
    //   866: getfield resolveGoneRightMargin : I
    //   869: istore_3
    //   870: aload #8
    //   872: getfield resolvedHorizontalBias : F
    //   875: fstore #11
    //   877: getstatic android/os/Build$VERSION.SDK_INT : I
    //   880: bipush #17
    //   882: if_icmpge -> 1102
    //   885: aload #8
    //   887: getfield leftToLeft : I
    //   890: istore #13
    //   892: aload #8
    //   894: getfield leftToRight : I
    //   897: istore #9
    //   899: aload #8
    //   901: getfield rightToLeft : I
    //   904: istore #12
    //   906: aload #8
    //   908: getfield rightToRight : I
    //   911: istore #15
    //   913: aload #8
    //   915: getfield goneLeftMargin : I
    //   918: istore #14
    //   920: aload #8
    //   922: getfield goneRightMargin : I
    //   925: istore #16
    //   927: aload #8
    //   929: getfield horizontalBias : F
    //   932: fstore #11
    //   934: iload #13
    //   936: istore_3
    //   937: iload #9
    //   939: istore #4
    //   941: iload #13
    //   943: iconst_m1
    //   944: if_icmpne -> 1008
    //   947: iload #13
    //   949: istore_3
    //   950: iload #9
    //   952: istore #4
    //   954: iload #9
    //   956: iconst_m1
    //   957: if_icmpne -> 1008
    //   960: aload #8
    //   962: getfield startToStart : I
    //   965: iconst_m1
    //   966: if_icmpeq -> 982
    //   969: aload #8
    //   971: getfield startToStart : I
    //   974: istore_3
    //   975: iload #9
    //   977: istore #4
    //   979: goto -> 1008
    //   982: iload #13
    //   984: istore_3
    //   985: iload #9
    //   987: istore #4
    //   989: aload #8
    //   991: getfield startToEnd : I
    //   994: iconst_m1
    //   995: if_icmpeq -> 1008
    //   998: aload #8
    //   1000: getfield startToEnd : I
    //   1003: istore #4
    //   1005: iload #13
    //   1007: istore_3
    //   1008: iload_3
    //   1009: istore #9
    //   1011: iload #4
    //   1013: istore #13
    //   1015: iload #12
    //   1017: istore_3
    //   1018: iload #15
    //   1020: istore #4
    //   1022: iload #12
    //   1024: iconst_m1
    //   1025: if_icmpne -> 1089
    //   1028: iload #12
    //   1030: istore_3
    //   1031: iload #15
    //   1033: istore #4
    //   1035: iload #15
    //   1037: iconst_m1
    //   1038: if_icmpne -> 1089
    //   1041: aload #8
    //   1043: getfield endToStart : I
    //   1046: iconst_m1
    //   1047: if_icmpeq -> 1063
    //   1050: aload #8
    //   1052: getfield endToStart : I
    //   1055: istore_3
    //   1056: iload #15
    //   1058: istore #4
    //   1060: goto -> 1089
    //   1063: iload #12
    //   1065: istore_3
    //   1066: iload #15
    //   1068: istore #4
    //   1070: aload #8
    //   1072: getfield endToEnd : I
    //   1075: iconst_m1
    //   1076: if_icmpeq -> 1089
    //   1079: aload #8
    //   1081: getfield endToEnd : I
    //   1084: istore #4
    //   1086: iload #12
    //   1088: istore_3
    //   1089: iload #9
    //   1091: istore #12
    //   1093: iload_3
    //   1094: istore #9
    //   1096: iload #16
    //   1098: istore_3
    //   1099: goto -> 1102
    //   1102: aload #8
    //   1104: getfield circleConstraint : I
    //   1107: iconst_m1
    //   1108: if_icmpeq -> 1147
    //   1111: aload_0
    //   1112: aload #8
    //   1114: getfield circleConstraint : I
    //   1117: invokespecial getTargetWidget : (I)Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   1120: astore #5
    //   1122: aload #5
    //   1124: ifnull -> 1708
    //   1127: aload #6
    //   1129: aload #5
    //   1131: aload #8
    //   1133: getfield circleAngle : F
    //   1136: aload #8
    //   1138: getfield circleRadius : I
    //   1141: invokevirtual connectCircularConstraint : (Landroidx/constraintlayout/solver/widgets/ConstraintWidget;FI)V
    //   1144: goto -> 1708
    //   1147: iload #12
    //   1149: iconst_m1
    //   1150: if_icmpeq -> 1196
    //   1153: aload_0
    //   1154: iload #12
    //   1156: invokespecial getTargetWidget : (I)Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   1159: astore #5
    //   1161: aload #5
    //   1163: ifnull -> 1189
    //   1166: aload #6
    //   1168: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.LEFT : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   1171: aload #5
    //   1173: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.LEFT : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   1176: aload #8
    //   1178: getfield leftMargin : I
    //   1181: iload #14
    //   1183: invokevirtual immediateConnect : (Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;Landroidx/constraintlayout/solver/widgets/ConstraintWidget;Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;II)V
    //   1186: goto -> 1189
    //   1189: iload #4
    //   1191: istore #12
    //   1193: goto -> 1247
    //   1196: iload #4
    //   1198: istore #12
    //   1200: iload #13
    //   1202: iconst_m1
    //   1203: if_icmpeq -> 1247
    //   1206: aload_0
    //   1207: iload #13
    //   1209: invokespecial getTargetWidget : (I)Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   1212: astore #5
    //   1214: iload #4
    //   1216: istore #12
    //   1218: aload #5
    //   1220: ifnull -> 1247
    //   1223: aload #6
    //   1225: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.LEFT : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   1228: aload #5
    //   1230: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.RIGHT : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   1233: aload #8
    //   1235: getfield leftMargin : I
    //   1238: iload #14
    //   1240: invokevirtual immediateConnect : (Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;Landroidx/constraintlayout/solver/widgets/ConstraintWidget;Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;II)V
    //   1243: iload #4
    //   1245: istore #12
    //   1247: iload #9
    //   1249: iconst_m1
    //   1250: if_icmpeq -> 1288
    //   1253: aload_0
    //   1254: iload #9
    //   1256: invokespecial getTargetWidget : (I)Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   1259: astore #5
    //   1261: aload #5
    //   1263: ifnull -> 1326
    //   1266: aload #6
    //   1268: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.RIGHT : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   1271: aload #5
    //   1273: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.LEFT : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   1276: aload #8
    //   1278: getfield rightMargin : I
    //   1281: iload_3
    //   1282: invokevirtual immediateConnect : (Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;Landroidx/constraintlayout/solver/widgets/ConstraintWidget;Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;II)V
    //   1285: goto -> 1326
    //   1288: iload #12
    //   1290: iconst_m1
    //   1291: if_icmpeq -> 1326
    //   1294: aload_0
    //   1295: iload #12
    //   1297: invokespecial getTargetWidget : (I)Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   1300: astore #5
    //   1302: aload #5
    //   1304: ifnull -> 1326
    //   1307: aload #6
    //   1309: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.RIGHT : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   1312: aload #5
    //   1314: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.RIGHT : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   1317: aload #8
    //   1319: getfield rightMargin : I
    //   1322: iload_3
    //   1323: invokevirtual immediateConnect : (Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;Landroidx/constraintlayout/solver/widgets/ConstraintWidget;Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;II)V
    //   1326: aload #8
    //   1328: getfield topToTop : I
    //   1331: iconst_m1
    //   1332: if_icmpeq -> 1377
    //   1335: aload_0
    //   1336: aload #8
    //   1338: getfield topToTop : I
    //   1341: invokespecial getTargetWidget : (I)Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   1344: astore #5
    //   1346: aload #5
    //   1348: ifnull -> 1425
    //   1351: aload #6
    //   1353: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.TOP : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   1356: aload #5
    //   1358: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.TOP : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   1361: aload #8
    //   1363: getfield topMargin : I
    //   1366: aload #8
    //   1368: getfield goneTopMargin : I
    //   1371: invokevirtual immediateConnect : (Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;Landroidx/constraintlayout/solver/widgets/ConstraintWidget;Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;II)V
    //   1374: goto -> 1425
    //   1377: aload #8
    //   1379: getfield topToBottom : I
    //   1382: iconst_m1
    //   1383: if_icmpeq -> 1425
    //   1386: aload_0
    //   1387: aload #8
    //   1389: getfield topToBottom : I
    //   1392: invokespecial getTargetWidget : (I)Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   1395: astore #5
    //   1397: aload #5
    //   1399: ifnull -> 1425
    //   1402: aload #6
    //   1404: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.TOP : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   1407: aload #5
    //   1409: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.BOTTOM : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   1412: aload #8
    //   1414: getfield topMargin : I
    //   1417: aload #8
    //   1419: getfield goneTopMargin : I
    //   1422: invokevirtual immediateConnect : (Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;Landroidx/constraintlayout/solver/widgets/ConstraintWidget;Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;II)V
    //   1425: aload #8
    //   1427: getfield bottomToTop : I
    //   1430: iconst_m1
    //   1431: if_icmpeq -> 1476
    //   1434: aload_0
    //   1435: aload #8
    //   1437: getfield bottomToTop : I
    //   1440: invokespecial getTargetWidget : (I)Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   1443: astore #5
    //   1445: aload #5
    //   1447: ifnull -> 1524
    //   1450: aload #6
    //   1452: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.BOTTOM : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   1455: aload #5
    //   1457: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.TOP : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   1460: aload #8
    //   1462: getfield bottomMargin : I
    //   1465: aload #8
    //   1467: getfield goneBottomMargin : I
    //   1470: invokevirtual immediateConnect : (Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;Landroidx/constraintlayout/solver/widgets/ConstraintWidget;Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;II)V
    //   1473: goto -> 1524
    //   1476: aload #8
    //   1478: getfield bottomToBottom : I
    //   1481: iconst_m1
    //   1482: if_icmpeq -> 1524
    //   1485: aload_0
    //   1486: aload #8
    //   1488: getfield bottomToBottom : I
    //   1491: invokespecial getTargetWidget : (I)Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   1494: astore #5
    //   1496: aload #5
    //   1498: ifnull -> 1524
    //   1501: aload #6
    //   1503: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.BOTTOM : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   1506: aload #5
    //   1508: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.BOTTOM : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   1511: aload #8
    //   1513: getfield bottomMargin : I
    //   1516: aload #8
    //   1518: getfield goneBottomMargin : I
    //   1521: invokevirtual immediateConnect : (Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;Landroidx/constraintlayout/solver/widgets/ConstraintWidget;Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;II)V
    //   1524: aload #8
    //   1526: getfield baselineToBaseline : I
    //   1529: iconst_m1
    //   1530: if_icmpeq -> 1653
    //   1533: aload_0
    //   1534: getfield mChildrenByIds : Landroid/util/SparseArray;
    //   1537: aload #8
    //   1539: getfield baselineToBaseline : I
    //   1542: invokevirtual get : (I)Ljava/lang/Object;
    //   1545: checkcast android/view/View
    //   1548: astore #10
    //   1550: aload_0
    //   1551: aload #8
    //   1553: getfield baselineToBaseline : I
    //   1556: invokespecial getTargetWidget : (I)Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
    //   1559: astore #5
    //   1561: aload #5
    //   1563: ifnull -> 1653
    //   1566: aload #10
    //   1568: ifnull -> 1653
    //   1571: aload #10
    //   1573: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   1576: instanceof androidx/constraintlayout/widget/ConstraintLayout$LayoutParams
    //   1579: ifeq -> 1653
    //   1582: aload #10
    //   1584: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   1587: checkcast androidx/constraintlayout/widget/ConstraintLayout$LayoutParams
    //   1590: astore #10
    //   1592: aload #8
    //   1594: iconst_1
    //   1595: putfield needsBaseline : Z
    //   1598: aload #10
    //   1600: iconst_1
    //   1601: putfield needsBaseline : Z
    //   1604: aload #6
    //   1606: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.BASELINE : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   1609: invokevirtual getAnchor : (Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1612: aload #5
    //   1614: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.BASELINE : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   1617: invokevirtual getAnchor : (Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1620: iconst_0
    //   1621: iconst_m1
    //   1622: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Strength.STRONG : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Strength;
    //   1625: iconst_0
    //   1626: iconst_1
    //   1627: invokevirtual connect : (Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;IILandroidx/constraintlayout/solver/widgets/ConstraintAnchor$Strength;IZ)Z
    //   1630: pop
    //   1631: aload #6
    //   1633: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.TOP : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   1636: invokevirtual getAnchor : (Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1639: invokevirtual reset : ()V
    //   1642: aload #6
    //   1644: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.BOTTOM : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   1647: invokevirtual getAnchor : (Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1650: invokevirtual reset : ()V
    //   1653: fload #11
    //   1655: fconst_0
    //   1656: fcmpl
    //   1657: iflt -> 1676
    //   1660: fload #11
    //   1662: ldc_w 0.5
    //   1665: fcmpl
    //   1666: ifeq -> 1676
    //   1669: aload #6
    //   1671: fload #11
    //   1673: invokevirtual setHorizontalBiasPercent : (F)V
    //   1676: aload #8
    //   1678: getfield verticalBias : F
    //   1681: fconst_0
    //   1682: fcmpl
    //   1683: iflt -> 1708
    //   1686: aload #8
    //   1688: getfield verticalBias : F
    //   1691: ldc_w 0.5
    //   1694: fcmpl
    //   1695: ifeq -> 1708
    //   1698: aload #6
    //   1700: aload #8
    //   1702: getfield verticalBias : F
    //   1705: invokevirtual setVerticalBiasPercent : (F)V
    //   1708: iload_1
    //   1709: ifeq -> 1745
    //   1712: aload #8
    //   1714: getfield editorAbsoluteX : I
    //   1717: iconst_m1
    //   1718: if_icmpne -> 1730
    //   1721: aload #8
    //   1723: getfield editorAbsoluteY : I
    //   1726: iconst_m1
    //   1727: if_icmpeq -> 1745
    //   1730: aload #6
    //   1732: aload #8
    //   1734: getfield editorAbsoluteX : I
    //   1737: aload #8
    //   1739: getfield editorAbsoluteY : I
    //   1742: invokevirtual setOrigin : (II)V
    //   1745: aload #8
    //   1747: getfield horizontalDimensionFixed : Z
    //   1750: ifne -> 1822
    //   1753: aload #8
    //   1755: getfield width : I
    //   1758: iconst_m1
    //   1759: if_icmpne -> 1805
    //   1762: aload #6
    //   1764: getstatic androidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_PARENT : Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   1767: invokevirtual setHorizontalDimensionBehaviour : (Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;)V
    //   1770: aload #6
    //   1772: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.LEFT : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   1775: invokevirtual getAnchor : (Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1778: aload #8
    //   1780: getfield leftMargin : I
    //   1783: putfield mMargin : I
    //   1786: aload #6
    //   1788: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.RIGHT : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   1791: invokevirtual getAnchor : (Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1794: aload #8
    //   1796: getfield rightMargin : I
    //   1799: putfield mMargin : I
    //   1802: goto -> 1840
    //   1805: aload #6
    //   1807: getstatic androidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   1810: invokevirtual setHorizontalDimensionBehaviour : (Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;)V
    //   1813: aload #6
    //   1815: iconst_0
    //   1816: invokevirtual setWidth : (I)V
    //   1819: goto -> 1840
    //   1822: aload #6
    //   1824: getstatic androidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour.FIXED : Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   1827: invokevirtual setHorizontalDimensionBehaviour : (Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;)V
    //   1830: aload #6
    //   1832: aload #8
    //   1834: getfield width : I
    //   1837: invokevirtual setWidth : (I)V
    //   1840: aload #8
    //   1842: getfield verticalDimensionFixed : Z
    //   1845: ifne -> 1917
    //   1848: aload #8
    //   1850: getfield height : I
    //   1853: iconst_m1
    //   1854: if_icmpne -> 1900
    //   1857: aload #6
    //   1859: getstatic androidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_PARENT : Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   1862: invokevirtual setVerticalDimensionBehaviour : (Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;)V
    //   1865: aload #6
    //   1867: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.TOP : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   1870: invokevirtual getAnchor : (Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1873: aload #8
    //   1875: getfield topMargin : I
    //   1878: putfield mMargin : I
    //   1881: aload #6
    //   1883: getstatic androidx/constraintlayout/solver/widgets/ConstraintAnchor$Type.BOTTOM : Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;
    //   1886: invokevirtual getAnchor : (Landroidx/constraintlayout/solver/widgets/ConstraintAnchor$Type;)Landroidx/constraintlayout/solver/widgets/ConstraintAnchor;
    //   1889: aload #8
    //   1891: getfield bottomMargin : I
    //   1894: putfield mMargin : I
    //   1897: goto -> 1935
    //   1900: aload #6
    //   1902: getstatic androidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   1905: invokevirtual setVerticalDimensionBehaviour : (Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;)V
    //   1908: aload #6
    //   1910: iconst_0
    //   1911: invokevirtual setHeight : (I)V
    //   1914: goto -> 1935
    //   1917: aload #6
    //   1919: getstatic androidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour.FIXED : Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   1922: invokevirtual setVerticalDimensionBehaviour : (Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;)V
    //   1925: aload #6
    //   1927: aload #8
    //   1929: getfield height : I
    //   1932: invokevirtual setHeight : (I)V
    //   1935: iconst_0
    //   1936: istore #9
    //   1938: aload #8
    //   1940: getfield dimensionRatio : Ljava/lang/String;
    //   1943: ifnull -> 1956
    //   1946: aload #6
    //   1948: aload #8
    //   1950: getfield dimensionRatio : Ljava/lang/String;
    //   1953: invokevirtual setDimensionRatio : (Ljava/lang/String;)V
    //   1956: aload #6
    //   1958: aload #8
    //   1960: getfield horizontalWeight : F
    //   1963: invokevirtual setHorizontalWeight : (F)V
    //   1966: aload #6
    //   1968: aload #8
    //   1970: getfield verticalWeight : F
    //   1973: invokevirtual setVerticalWeight : (F)V
    //   1976: aload #6
    //   1978: aload #8
    //   1980: getfield horizontalChainStyle : I
    //   1983: invokevirtual setHorizontalChainStyle : (I)V
    //   1986: aload #6
    //   1988: aload #8
    //   1990: getfield verticalChainStyle : I
    //   1993: invokevirtual setVerticalChainStyle : (I)V
    //   1996: aload #6
    //   1998: aload #8
    //   2000: getfield matchConstraintDefaultWidth : I
    //   2003: aload #8
    //   2005: getfield matchConstraintMinWidth : I
    //   2008: aload #8
    //   2010: getfield matchConstraintMaxWidth : I
    //   2013: aload #8
    //   2015: getfield matchConstraintPercentWidth : F
    //   2018: invokevirtual setHorizontalMatchStyle : (IIIF)V
    //   2021: aload #6
    //   2023: aload #8
    //   2025: getfield matchConstraintDefaultHeight : I
    //   2028: aload #8
    //   2030: getfield matchConstraintMinHeight : I
    //   2033: aload #8
    //   2035: getfield matchConstraintMaxHeight : I
    //   2038: aload #8
    //   2040: getfield matchConstraintPercentHeight : F
    //   2043: invokevirtual setVerticalMatchStyle : (IIIF)V
    //   2046: iinc #7, 1
    //   2049: iload #9
    //   2051: istore #4
    //   2053: goto -> 331
    //   2056: return
    //   2057: astore #8
    //   2059: goto -> 106
    // Exception table:
    //   from	to	target	type
    //   33	71	2057	android/content/res/Resources$NotFoundException
    //   81	92	2057	android/content/res/Resources$NotFoundException
    //   92	106	2057	android/content/res/Resources$NotFoundException
    //   402	463	466	android/content/res/Resources$NotFoundException
  }
  
  private void setSelfDimensionBehaviour(int paramInt1, int paramInt2) {
    // Byte code:
    //   0: iload_1
    //   1: invokestatic getMode : (I)I
    //   4: istore_3
    //   5: iload_1
    //   6: invokestatic getSize : (I)I
    //   9: istore_1
    //   10: iload_2
    //   11: invokestatic getMode : (I)I
    //   14: istore #4
    //   16: iload_2
    //   17: invokestatic getSize : (I)I
    //   20: istore_2
    //   21: aload_0
    //   22: invokevirtual getPaddingTop : ()I
    //   25: istore #5
    //   27: aload_0
    //   28: invokevirtual getPaddingBottom : ()I
    //   31: istore #6
    //   33: aload_0
    //   34: invokevirtual getPaddingLeft : ()I
    //   37: istore #7
    //   39: aload_0
    //   40: invokevirtual getPaddingRight : ()I
    //   43: istore #8
    //   45: getstatic androidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour.FIXED : Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   48: astore #9
    //   50: getstatic androidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour.FIXED : Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   53: astore #10
    //   55: aload_0
    //   56: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   59: pop
    //   60: iload_3
    //   61: ldc_w -2147483648
    //   64: if_icmpeq -> 109
    //   67: iload_3
    //   68: ifeq -> 101
    //   71: iload_3
    //   72: ldc_w 1073741824
    //   75: if_icmpeq -> 83
    //   78: iconst_0
    //   79: istore_1
    //   80: goto -> 114
    //   83: aload_0
    //   84: getfield mMaxWidth : I
    //   87: iload_1
    //   88: invokestatic min : (II)I
    //   91: iload #7
    //   93: iload #8
    //   95: iadd
    //   96: isub
    //   97: istore_1
    //   98: goto -> 114
    //   101: getstatic androidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   104: astore #9
    //   106: goto -> 78
    //   109: getstatic androidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   112: astore #9
    //   114: iload #4
    //   116: ldc_w -2147483648
    //   119: if_icmpeq -> 166
    //   122: iload #4
    //   124: ifeq -> 158
    //   127: iload #4
    //   129: ldc_w 1073741824
    //   132: if_icmpeq -> 140
    //   135: iconst_0
    //   136: istore_2
    //   137: goto -> 171
    //   140: aload_0
    //   141: getfield mMaxHeight : I
    //   144: iload_2
    //   145: invokestatic min : (II)I
    //   148: iload #5
    //   150: iload #6
    //   152: iadd
    //   153: isub
    //   154: istore_2
    //   155: goto -> 171
    //   158: getstatic androidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   161: astore #10
    //   163: goto -> 135
    //   166: getstatic androidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   169: astore #10
    //   171: aload_0
    //   172: getfield mLayoutWidget : Landroidx/constraintlayout/solver/widgets/ConstraintWidgetContainer;
    //   175: iconst_0
    //   176: invokevirtual setMinWidth : (I)V
    //   179: aload_0
    //   180: getfield mLayoutWidget : Landroidx/constraintlayout/solver/widgets/ConstraintWidgetContainer;
    //   183: iconst_0
    //   184: invokevirtual setMinHeight : (I)V
    //   187: aload_0
    //   188: getfield mLayoutWidget : Landroidx/constraintlayout/solver/widgets/ConstraintWidgetContainer;
    //   191: aload #9
    //   193: invokevirtual setHorizontalDimensionBehaviour : (Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;)V
    //   196: aload_0
    //   197: getfield mLayoutWidget : Landroidx/constraintlayout/solver/widgets/ConstraintWidgetContainer;
    //   200: iload_1
    //   201: invokevirtual setWidth : (I)V
    //   204: aload_0
    //   205: getfield mLayoutWidget : Landroidx/constraintlayout/solver/widgets/ConstraintWidgetContainer;
    //   208: aload #10
    //   210: invokevirtual setVerticalDimensionBehaviour : (Landroidx/constraintlayout/solver/widgets/ConstraintWidget$DimensionBehaviour;)V
    //   213: aload_0
    //   214: getfield mLayoutWidget : Landroidx/constraintlayout/solver/widgets/ConstraintWidgetContainer;
    //   217: iload_2
    //   218: invokevirtual setHeight : (I)V
    //   221: aload_0
    //   222: getfield mLayoutWidget : Landroidx/constraintlayout/solver/widgets/ConstraintWidgetContainer;
    //   225: aload_0
    //   226: getfield mMinWidth : I
    //   229: aload_0
    //   230: invokevirtual getPaddingLeft : ()I
    //   233: isub
    //   234: aload_0
    //   235: invokevirtual getPaddingRight : ()I
    //   238: isub
    //   239: invokevirtual setMinWidth : (I)V
    //   242: aload_0
    //   243: getfield mLayoutWidget : Landroidx/constraintlayout/solver/widgets/ConstraintWidgetContainer;
    //   246: aload_0
    //   247: getfield mMinHeight : I
    //   250: aload_0
    //   251: invokevirtual getPaddingTop : ()I
    //   254: isub
    //   255: aload_0
    //   256: invokevirtual getPaddingBottom : ()I
    //   259: isub
    //   260: invokevirtual setMinHeight : (I)V
    //   263: return
  }
  
  private void updateHierarchy() {
    boolean bool2;
    int i = getChildCount();
    boolean bool1 = false;
    byte b = 0;
    while (true) {
      bool2 = bool1;
      if (b < i) {
        if (getChildAt(b).isLayoutRequested()) {
          bool2 = true;
          break;
        } 
        b++;
        continue;
      } 
      break;
    } 
    if (bool2) {
      this.mVariableDimensionsWidgets.clear();
      setChildrenConstraints();
    } 
  }
  
  private void updatePostMeasures() {
    int i = getChildCount();
    boolean bool = false;
    byte b;
    for (b = 0; b < i; b++) {
      View view = getChildAt(b);
      if (view instanceof Placeholder)
        ((Placeholder)view).updatePostMeasure(this); 
    } 
    i = this.mConstraintHelpers.size();
    if (i > 0)
      for (b = bool; b < i; b++)
        ((ConstraintHelper)this.mConstraintHelpers.get(b)).updatePostMeasure(this);  
  }
  
  public void addView(View paramView, int paramInt, ViewGroup.LayoutParams paramLayoutParams) {
    super.addView(paramView, paramInt, paramLayoutParams);
    if (Build.VERSION.SDK_INT < 14)
      onViewAdded(paramView); 
  }
  
  protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    return paramLayoutParams instanceof LayoutParams;
  }
  
  public void dispatchDraw(Canvas paramCanvas) {
    super.dispatchDraw(paramCanvas);
    if (isInEditMode()) {
      int i = getChildCount();
      float f1 = getWidth();
      float f2 = getHeight();
      for (byte b = 0; b < i; b++) {
        View view = getChildAt(b);
        if (view.getVisibility() != 8) {
          Object object = view.getTag();
          if (object != null && object instanceof String) {
            object = ((String)object).split(",");
            if (object.length == 4) {
              int j = Integer.parseInt((String)object[0]);
              int k = Integer.parseInt((String)object[1]);
              int m = Integer.parseInt((String)object[2]);
              int n = Integer.parseInt((String)object[3]);
              j = (int)(j / 1080.0F * f1);
              k = (int)(k / 1920.0F * f2);
              m = (int)(m / 1080.0F * f1);
              n = (int)(n / 1920.0F * f2);
              object = new Paint();
              object.setColor(-65536);
              float f3 = j;
              float f4 = k;
              float f5 = (j + m);
              paramCanvas.drawLine(f3, f4, f5, f4, (Paint)object);
              float f6 = (k + n);
              paramCanvas.drawLine(f5, f4, f5, f6, (Paint)object);
              paramCanvas.drawLine(f5, f6, f3, f6, (Paint)object);
              paramCanvas.drawLine(f3, f6, f3, f4, (Paint)object);
              object.setColor(-16711936);
              paramCanvas.drawLine(f3, f4, f5, f6, (Paint)object);
              paramCanvas.drawLine(f3, f6, f5, f4, (Paint)object);
            } 
          } 
        } 
      } 
    } 
  }
  
  public void fillMetrics(Metrics paramMetrics) {
    this.mMetrics = paramMetrics;
    this.mLayoutWidget.fillMetrics(paramMetrics);
  }
  
  protected LayoutParams generateDefaultLayoutParams() {
    return new LayoutParams(-2, -2);
  }
  
  protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    return (ViewGroup.LayoutParams)new LayoutParams(paramLayoutParams);
  }
  
  public LayoutParams generateLayoutParams(AttributeSet paramAttributeSet) {
    return new LayoutParams(getContext(), paramAttributeSet);
  }
  
  public Object getDesignInformation(int paramInt, Object paramObject) {
    if (paramInt == 0 && paramObject instanceof String) {
      paramObject = paramObject;
      HashMap<String, Integer> hashMap = this.mDesignIds;
      if (hashMap != null && hashMap.containsKey(paramObject))
        return this.mDesignIds.get(paramObject); 
    } 
    return null;
  }
  
  public int getMaxHeight() {
    return this.mMaxHeight;
  }
  
  public int getMaxWidth() {
    return this.mMaxWidth;
  }
  
  public int getMinHeight() {
    return this.mMinHeight;
  }
  
  public int getMinWidth() {
    return this.mMinWidth;
  }
  
  public int getOptimizationLevel() {
    return this.mLayoutWidget.getOptimizationLevel();
  }
  
  public View getViewById(int paramInt) {
    return (View)this.mChildrenByIds.get(paramInt);
  }
  
  public final ConstraintWidget getViewWidget(View paramView) {
    ConstraintWidget constraintWidget;
    if (paramView == this)
      return (ConstraintWidget)this.mLayoutWidget; 
    if (paramView == null) {
      paramView = null;
    } else {
      constraintWidget = ((LayoutParams)paramView.getLayoutParams()).widget;
    } 
    return constraintWidget;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    paramInt3 = getChildCount();
    paramBoolean = isInEditMode();
    paramInt2 = 0;
    for (paramInt1 = 0; paramInt1 < paramInt3; paramInt1++) {
      View view = getChildAt(paramInt1);
      LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
      ConstraintWidget constraintWidget = layoutParams.widget;
      if ((view.getVisibility() != 8 || layoutParams.isGuideline || layoutParams.isHelper || paramBoolean) && !layoutParams.isInPlaceholder) {
        int i = constraintWidget.getDrawX();
        int j = constraintWidget.getDrawY();
        paramInt4 = constraintWidget.getWidth() + i;
        int k = constraintWidget.getHeight() + j;
        view.layout(i, j, paramInt4, k);
        if (view instanceof Placeholder) {
          view = ((Placeholder)view).getContent();
          if (view != null) {
            view.setVisibility(0);
            view.layout(i, j, paramInt4, k);
          } 
        } 
      } 
    } 
    paramInt3 = this.mConstraintHelpers.size();
    if (paramInt3 > 0)
      for (paramInt1 = paramInt2; paramInt1 < paramInt3; paramInt1++)
        ((ConstraintHelper)this.mConstraintHelpers.get(paramInt1)).updatePostLayout(this);  
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    int i4;
    boolean bool;
    System.currentTimeMillis();
    int i = View.MeasureSpec.getMode(paramInt1);
    int j = View.MeasureSpec.getSize(paramInt1);
    int k = View.MeasureSpec.getMode(paramInt2);
    int m = View.MeasureSpec.getSize(paramInt2);
    int n = getPaddingLeft();
    int i1 = getPaddingTop();
    this.mLayoutWidget.setX(n);
    this.mLayoutWidget.setY(i1);
    this.mLayoutWidget.setMaxWidth(this.mMaxWidth);
    this.mLayoutWidget.setMaxHeight(this.mMaxHeight);
    if (Build.VERSION.SDK_INT >= 17) {
      boolean bool1;
      ConstraintWidgetContainer constraintWidgetContainer = this.mLayoutWidget;
      if (getLayoutDirection() == 1) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      constraintWidgetContainer.setRtl(bool1);
    } 
    setSelfDimensionBehaviour(paramInt1, paramInt2);
    int i2 = this.mLayoutWidget.getWidth();
    int i3 = this.mLayoutWidget.getHeight();
    if (this.mDirtyHierarchy) {
      this.mDirtyHierarchy = false;
      updateHierarchy();
      i4 = 1;
    } else {
      i4 = 0;
    } 
    if ((this.mOptimizationLevel & 0x8) == 8) {
      bool = true;
    } else {
      bool = false;
    } 
    if (bool) {
      this.mLayoutWidget.preOptimize();
      this.mLayoutWidget.optimizeForDimensions(i2, i3);
      internalMeasureDimensions(paramInt1, paramInt2);
    } else {
      internalMeasureChildren(paramInt1, paramInt2);
    } 
    updatePostMeasures();
    if (getChildCount() > 0 && i4)
      Analyzer.determineGroups(this.mLayoutWidget); 
    if (this.mLayoutWidget.mGroupsWrapOptimized) {
      if (this.mLayoutWidget.mHorizontalWrapOptimized && i == Integer.MIN_VALUE) {
        if (this.mLayoutWidget.mWrapFixedWidth < j) {
          ConstraintWidgetContainer constraintWidgetContainer = this.mLayoutWidget;
          constraintWidgetContainer.setWidth(constraintWidgetContainer.mWrapFixedWidth);
        } 
        this.mLayoutWidget.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
      } 
      if (this.mLayoutWidget.mVerticalWrapOptimized && k == Integer.MIN_VALUE) {
        if (this.mLayoutWidget.mWrapFixedHeight < m) {
          ConstraintWidgetContainer constraintWidgetContainer = this.mLayoutWidget;
          constraintWidgetContainer.setHeight(constraintWidgetContainer.mWrapFixedHeight);
        } 
        this.mLayoutWidget.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
      } 
    } 
    if ((this.mOptimizationLevel & 0x20) == 32) {
      int i7 = this.mLayoutWidget.getWidth();
      i4 = this.mLayoutWidget.getHeight();
      if (this.mLastMeasureWidth != i7 && i == 1073741824)
        Analyzer.setPosition(this.mLayoutWidget.mWidgetGroups, 0, i7); 
      if (this.mLastMeasureHeight != i4 && k == 1073741824)
        Analyzer.setPosition(this.mLayoutWidget.mWidgetGroups, 1, i4); 
      if (this.mLayoutWidget.mHorizontalWrapOptimized && this.mLayoutWidget.mWrapFixedWidth > j)
        Analyzer.setPosition(this.mLayoutWidget.mWidgetGroups, 0, j); 
      if (this.mLayoutWidget.mVerticalWrapOptimized && this.mLayoutWidget.mWrapFixedHeight > m)
        Analyzer.setPosition(this.mLayoutWidget.mWidgetGroups, 1, m); 
    } 
    if (getChildCount() > 0)
      solveLinearSystem("First pass"); 
    m = this.mVariableDimensionsWidgets.size();
    int i5 = i1 + getPaddingBottom();
    int i6 = n + getPaddingRight();
    if (m > 0) {
      boolean bool1;
      if (this.mLayoutWidget.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
        k = 1;
      } else {
        k = 0;
      } 
      if (this.mLayoutWidget.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      i1 = Math.max(this.mLayoutWidget.getWidth(), this.mMinWidth);
      n = Math.max(this.mLayoutWidget.getHeight(), this.mMinHeight);
      byte b = 0;
      j = 0;
      i4 = 0;
      while (b < m) {
        ConstraintWidget constraintWidget = this.mVariableDimensionsWidgets.get(b);
        View view = (View)constraintWidget.getCompanionWidget();
        if (view != null) {
          LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
          if (!layoutParams.isHelper && !layoutParams.isGuideline) {
            int i7 = view.getVisibility();
            i = j;
            if (i7 != 8 && (!bool || !constraintWidget.getResolutionWidth().isResolved() || !constraintWidget.getResolutionHeight().isResolved())) {
              if (layoutParams.width == -2 && layoutParams.horizontalDimensionFixed) {
                j = getChildMeasureSpec(paramInt1, i6, layoutParams.width);
              } else {
                j = View.MeasureSpec.makeMeasureSpec(constraintWidget.getWidth(), 1073741824);
              } 
              if (layoutParams.height == -2 && layoutParams.verticalDimensionFixed) {
                i7 = getChildMeasureSpec(paramInt2, i5, layoutParams.height);
              } else {
                i7 = View.MeasureSpec.makeMeasureSpec(constraintWidget.getHeight(), 1073741824);
              } 
              view.measure(j, i7);
              Metrics metrics = this.mMetrics;
              if (metrics != null)
                metrics.additionalMeasures++; 
              int i8 = view.getMeasuredWidth();
              i7 = view.getMeasuredHeight();
              j = i1;
              if (i8 != constraintWidget.getWidth()) {
                constraintWidget.setWidth(i8);
                if (bool)
                  constraintWidget.getResolutionWidth().resolve(i8); 
                j = i1;
                if (k != 0) {
                  j = i1;
                  if (constraintWidget.getRight() > i1)
                    j = Math.max(i1, constraintWidget.getRight() + constraintWidget.getAnchor(ConstraintAnchor.Type.RIGHT).getMargin()); 
                } 
                i = 1;
              } 
              i1 = n;
              if (i7 != constraintWidget.getHeight()) {
                constraintWidget.setHeight(i7);
                if (bool)
                  constraintWidget.getResolutionHeight().resolve(i7); 
                i1 = n;
                if (bool1) {
                  i1 = n;
                  if (constraintWidget.getBottom() > n)
                    i1 = Math.max(n, constraintWidget.getBottom() + constraintWidget.getAnchor(ConstraintAnchor.Type.BOTTOM).getMargin()); 
                } 
                i = 1;
              } 
              n = i;
              if (layoutParams.needsBaseline) {
                i7 = view.getBaseline();
                n = i;
                if (i7 != -1) {
                  n = i;
                  if (i7 != constraintWidget.getBaselineDistance()) {
                    constraintWidget.setBaselineDistance(i7);
                    n = 1;
                  } 
                } 
              } 
              if (Build.VERSION.SDK_INT >= 11) {
                i4 = combineMeasuredStates(i4, view.getMeasuredState());
                i = i1;
                i1 = j;
                j = n;
              } else {
                i = i1;
                i1 = j;
                j = n;
              } 
              continue;
            } 
          } 
        } 
        i = n;
        continue;
        b++;
        n = i;
      } 
      i = i4;
      if (j != 0) {
        this.mLayoutWidget.setWidth(i2);
        this.mLayoutWidget.setHeight(i3);
        if (bool)
          this.mLayoutWidget.solveGraph(); 
        solveLinearSystem("2nd pass");
        if (this.mLayoutWidget.getWidth() < i1) {
          this.mLayoutWidget.setWidth(i1);
          i4 = 1;
        } else {
          i4 = 0;
        } 
        if (this.mLayoutWidget.getHeight() < n) {
          this.mLayoutWidget.setHeight(n);
          i4 = 1;
        } 
        if (i4 != 0)
          solveLinearSystem("3rd pass"); 
      } 
      n = 0;
      while (true) {
        i4 = i;
        if (n < m) {
          ConstraintWidget constraintWidget = this.mVariableDimensionsWidgets.get(n);
          View view = (View)constraintWidget.getCompanionWidget();
          if (view != null && (view.getMeasuredWidth() != constraintWidget.getWidth() || view.getMeasuredHeight() != constraintWidget.getHeight()) && constraintWidget.getVisibility() != 8) {
            view.measure(View.MeasureSpec.makeMeasureSpec(constraintWidget.getWidth(), 1073741824), View.MeasureSpec.makeMeasureSpec(constraintWidget.getHeight(), 1073741824));
            Metrics metrics = this.mMetrics;
            if (metrics != null)
              metrics.additionalMeasures++; 
          } 
          n++;
          continue;
        } 
        break;
      } 
    } else {
      i4 = 0;
    } 
    n = this.mLayoutWidget.getWidth() + i6;
    i1 = this.mLayoutWidget.getHeight() + i5;
    if (Build.VERSION.SDK_INT >= 11) {
      paramInt1 = resolveSizeAndState(n, paramInt1, i4);
      i4 = resolveSizeAndState(i1, paramInt2, i4 << 16);
      paramInt2 = Math.min(this.mMaxWidth, paramInt1 & 0xFFFFFF);
      i4 = Math.min(this.mMaxHeight, i4 & 0xFFFFFF);
      paramInt1 = paramInt2;
      if (this.mLayoutWidget.isWidthMeasuredTooSmall())
        paramInt1 = paramInt2 | 0x1000000; 
      paramInt2 = i4;
      if (this.mLayoutWidget.isHeightMeasuredTooSmall())
        paramInt2 = i4 | 0x1000000; 
      setMeasuredDimension(paramInt1, paramInt2);
      this.mLastMeasureWidth = paramInt1;
      this.mLastMeasureHeight = paramInt2;
    } else {
      setMeasuredDimension(n, i1);
      this.mLastMeasureWidth = n;
      this.mLastMeasureHeight = i1;
    } 
  }
  
  public void onViewAdded(View paramView) {
    if (Build.VERSION.SDK_INT >= 14)
      super.onViewAdded(paramView); 
    ConstraintWidget constraintWidget = getViewWidget(paramView);
    if (paramView instanceof Guideline && !(constraintWidget instanceof Guideline)) {
      LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
      layoutParams.widget = (ConstraintWidget)new Guideline();
      layoutParams.isGuideline = true;
      ((Guideline)layoutParams.widget).setOrientation(layoutParams.orientation);
    } 
    if (paramView instanceof ConstraintHelper) {
      ConstraintHelper constraintHelper = (ConstraintHelper)paramView;
      constraintHelper.validateParams();
      ((LayoutParams)paramView.getLayoutParams()).isHelper = true;
      if (!this.mConstraintHelpers.contains(constraintHelper))
        this.mConstraintHelpers.add(constraintHelper); 
    } 
    this.mChildrenByIds.put(paramView.getId(), paramView);
    this.mDirtyHierarchy = true;
  }
  
  public void onViewRemoved(View paramView) {
    if (Build.VERSION.SDK_INT >= 14)
      super.onViewRemoved(paramView); 
    this.mChildrenByIds.remove(paramView.getId());
    ConstraintWidget constraintWidget = getViewWidget(paramView);
    this.mLayoutWidget.remove(constraintWidget);
    this.mConstraintHelpers.remove(paramView);
    this.mVariableDimensionsWidgets.remove(constraintWidget);
    this.mDirtyHierarchy = true;
  }
  
  public void removeView(View paramView) {
    super.removeView(paramView);
    if (Build.VERSION.SDK_INT < 14)
      onViewRemoved(paramView); 
  }
  
  public void requestLayout() {
    super.requestLayout();
    this.mDirtyHierarchy = true;
    this.mLastMeasureWidth = -1;
    this.mLastMeasureHeight = -1;
    this.mLastMeasureWidthSize = -1;
    this.mLastMeasureHeightSize = -1;
    this.mLastMeasureWidthMode = 0;
    this.mLastMeasureHeightMode = 0;
  }
  
  public void setConstraintSet(ConstraintSet paramConstraintSet) {
    this.mConstraintSet = paramConstraintSet;
  }
  
  public void setDesignInformation(int paramInt, Object paramObject1, Object paramObject2) {
    if (paramInt == 0 && paramObject1 instanceof String && paramObject2 instanceof Integer) {
      if (this.mDesignIds == null)
        this.mDesignIds = new HashMap<String, Integer>(); 
      String str = (String)paramObject1;
      paramInt = str.indexOf("/");
      paramObject1 = str;
      if (paramInt != -1)
        paramObject1 = str.substring(paramInt + 1); 
      paramInt = ((Integer)paramObject2).intValue();
      this.mDesignIds.put(paramObject1, Integer.valueOf(paramInt));
    } 
  }
  
  public void setId(int paramInt) {
    this.mChildrenByIds.remove(getId());
    super.setId(paramInt);
    this.mChildrenByIds.put(getId(), this);
  }
  
  public void setMaxHeight(int paramInt) {
    if (paramInt == this.mMaxHeight)
      return; 
    this.mMaxHeight = paramInt;
    requestLayout();
  }
  
  public void setMaxWidth(int paramInt) {
    if (paramInt == this.mMaxWidth)
      return; 
    this.mMaxWidth = paramInt;
    requestLayout();
  }
  
  public void setMinHeight(int paramInt) {
    if (paramInt == this.mMinHeight)
      return; 
    this.mMinHeight = paramInt;
    requestLayout();
  }
  
  public void setMinWidth(int paramInt) {
    if (paramInt == this.mMinWidth)
      return; 
    this.mMinWidth = paramInt;
    requestLayout();
  }
  
  public void setOptimizationLevel(int paramInt) {
    this.mLayoutWidget.setOptimizationLevel(paramInt);
  }
  
  public boolean shouldDelayChildPressedState() {
    return false;
  }
  
  protected void solveLinearSystem(String paramString) {
    this.mLayoutWidget.layout();
    Metrics metrics = this.mMetrics;
    if (metrics != null)
      metrics.resolutions++; 
  }
  
  public static class LayoutParams extends ViewGroup.MarginLayoutParams {
    public static final int BASELINE = 5;
    
    public static final int BOTTOM = 4;
    
    public static final int CHAIN_PACKED = 2;
    
    public static final int CHAIN_SPREAD = 0;
    
    public static final int CHAIN_SPREAD_INSIDE = 1;
    
    public static final int END = 7;
    
    public static final int HORIZONTAL = 0;
    
    public static final int LEFT = 1;
    
    public static final int MATCH_CONSTRAINT = 0;
    
    public static final int MATCH_CONSTRAINT_PERCENT = 2;
    
    public static final int MATCH_CONSTRAINT_SPREAD = 0;
    
    public static final int MATCH_CONSTRAINT_WRAP = 1;
    
    public static final int PARENT_ID = 0;
    
    public static final int RIGHT = 2;
    
    public static final int START = 6;
    
    public static final int TOP = 3;
    
    public static final int UNSET = -1;
    
    public static final int VERTICAL = 1;
    
    public int baselineToBaseline = -1;
    
    public int bottomToBottom = -1;
    
    public int bottomToTop = -1;
    
    public float circleAngle = 0.0F;
    
    public int circleConstraint = -1;
    
    public int circleRadius = 0;
    
    public boolean constrainedHeight = false;
    
    public boolean constrainedWidth = false;
    
    public String dimensionRatio = null;
    
    int dimensionRatioSide = 1;
    
    float dimensionRatioValue = 0.0F;
    
    public int editorAbsoluteX = -1;
    
    public int editorAbsoluteY = -1;
    
    public int endToEnd = -1;
    
    public int endToStart = -1;
    
    public int goneBottomMargin = -1;
    
    public int goneEndMargin = -1;
    
    public int goneLeftMargin = -1;
    
    public int goneRightMargin = -1;
    
    public int goneStartMargin = -1;
    
    public int goneTopMargin = -1;
    
    public int guideBegin = -1;
    
    public int guideEnd = -1;
    
    public float guidePercent = -1.0F;
    
    public boolean helped = false;
    
    public float horizontalBias = 0.5F;
    
    public int horizontalChainStyle = 0;
    
    boolean horizontalDimensionFixed = true;
    
    public float horizontalWeight = -1.0F;
    
    boolean isGuideline = false;
    
    boolean isHelper = false;
    
    boolean isInPlaceholder = false;
    
    public int leftToLeft = -1;
    
    public int leftToRight = -1;
    
    public int matchConstraintDefaultHeight = 0;
    
    public int matchConstraintDefaultWidth = 0;
    
    public int matchConstraintMaxHeight = 0;
    
    public int matchConstraintMaxWidth = 0;
    
    public int matchConstraintMinHeight = 0;
    
    public int matchConstraintMinWidth = 0;
    
    public float matchConstraintPercentHeight = 1.0F;
    
    public float matchConstraintPercentWidth = 1.0F;
    
    boolean needsBaseline = false;
    
    public int orientation = -1;
    
    int resolveGoneLeftMargin = -1;
    
    int resolveGoneRightMargin = -1;
    
    int resolvedGuideBegin;
    
    int resolvedGuideEnd;
    
    float resolvedGuidePercent;
    
    float resolvedHorizontalBias = 0.5F;
    
    int resolvedLeftToLeft = -1;
    
    int resolvedLeftToRight = -1;
    
    int resolvedRightToLeft = -1;
    
    int resolvedRightToRight = -1;
    
    public int rightToLeft = -1;
    
    public int rightToRight = -1;
    
    public int startToEnd = -1;
    
    public int startToStart = -1;
    
    public int topToBottom = -1;
    
    public int topToTop = -1;
    
    public float verticalBias = 0.5F;
    
    public int verticalChainStyle = 0;
    
    boolean verticalDimensionFixed = true;
    
    public float verticalWeight = -1.0F;
    
    ConstraintWidget widget = new ConstraintWidget();
    
    public LayoutParams(int param1Int1, int param1Int2) {
      super(param1Int1, param1Int2);
    }
    
    public LayoutParams(Context param1Context, AttributeSet param1AttributeSet) {
      // Byte code:
      //   0: aload_0
      //   1: aload_1
      //   2: aload_2
      //   3: invokespecial <init> : (Landroid/content/Context;Landroid/util/AttributeSet;)V
      //   6: aload_0
      //   7: iconst_m1
      //   8: putfield guideBegin : I
      //   11: aload_0
      //   12: iconst_m1
      //   13: putfield guideEnd : I
      //   16: aload_0
      //   17: ldc -1.0
      //   19: putfield guidePercent : F
      //   22: aload_0
      //   23: iconst_m1
      //   24: putfield leftToLeft : I
      //   27: aload_0
      //   28: iconst_m1
      //   29: putfield leftToRight : I
      //   32: aload_0
      //   33: iconst_m1
      //   34: putfield rightToLeft : I
      //   37: aload_0
      //   38: iconst_m1
      //   39: putfield rightToRight : I
      //   42: aload_0
      //   43: iconst_m1
      //   44: putfield topToTop : I
      //   47: aload_0
      //   48: iconst_m1
      //   49: putfield topToBottom : I
      //   52: aload_0
      //   53: iconst_m1
      //   54: putfield bottomToTop : I
      //   57: aload_0
      //   58: iconst_m1
      //   59: putfield bottomToBottom : I
      //   62: aload_0
      //   63: iconst_m1
      //   64: putfield baselineToBaseline : I
      //   67: aload_0
      //   68: iconst_m1
      //   69: putfield circleConstraint : I
      //   72: aload_0
      //   73: iconst_0
      //   74: putfield circleRadius : I
      //   77: aload_0
      //   78: fconst_0
      //   79: putfield circleAngle : F
      //   82: aload_0
      //   83: iconst_m1
      //   84: putfield startToEnd : I
      //   87: aload_0
      //   88: iconst_m1
      //   89: putfield startToStart : I
      //   92: aload_0
      //   93: iconst_m1
      //   94: putfield endToStart : I
      //   97: aload_0
      //   98: iconst_m1
      //   99: putfield endToEnd : I
      //   102: aload_0
      //   103: iconst_m1
      //   104: putfield goneLeftMargin : I
      //   107: aload_0
      //   108: iconst_m1
      //   109: putfield goneTopMargin : I
      //   112: aload_0
      //   113: iconst_m1
      //   114: putfield goneRightMargin : I
      //   117: aload_0
      //   118: iconst_m1
      //   119: putfield goneBottomMargin : I
      //   122: aload_0
      //   123: iconst_m1
      //   124: putfield goneStartMargin : I
      //   127: aload_0
      //   128: iconst_m1
      //   129: putfield goneEndMargin : I
      //   132: aload_0
      //   133: ldc 0.5
      //   135: putfield horizontalBias : F
      //   138: aload_0
      //   139: ldc 0.5
      //   141: putfield verticalBias : F
      //   144: aload_0
      //   145: aconst_null
      //   146: putfield dimensionRatio : Ljava/lang/String;
      //   149: aload_0
      //   150: fconst_0
      //   151: putfield dimensionRatioValue : F
      //   154: aload_0
      //   155: iconst_1
      //   156: putfield dimensionRatioSide : I
      //   159: aload_0
      //   160: ldc -1.0
      //   162: putfield horizontalWeight : F
      //   165: aload_0
      //   166: ldc -1.0
      //   168: putfield verticalWeight : F
      //   171: aload_0
      //   172: iconst_0
      //   173: putfield horizontalChainStyle : I
      //   176: aload_0
      //   177: iconst_0
      //   178: putfield verticalChainStyle : I
      //   181: aload_0
      //   182: iconst_0
      //   183: putfield matchConstraintDefaultWidth : I
      //   186: aload_0
      //   187: iconst_0
      //   188: putfield matchConstraintDefaultHeight : I
      //   191: aload_0
      //   192: iconst_0
      //   193: putfield matchConstraintMinWidth : I
      //   196: aload_0
      //   197: iconst_0
      //   198: putfield matchConstraintMinHeight : I
      //   201: aload_0
      //   202: iconst_0
      //   203: putfield matchConstraintMaxWidth : I
      //   206: aload_0
      //   207: iconst_0
      //   208: putfield matchConstraintMaxHeight : I
      //   211: aload_0
      //   212: fconst_1
      //   213: putfield matchConstraintPercentWidth : F
      //   216: aload_0
      //   217: fconst_1
      //   218: putfield matchConstraintPercentHeight : F
      //   221: aload_0
      //   222: iconst_m1
      //   223: putfield editorAbsoluteX : I
      //   226: aload_0
      //   227: iconst_m1
      //   228: putfield editorAbsoluteY : I
      //   231: aload_0
      //   232: iconst_m1
      //   233: putfield orientation : I
      //   236: aload_0
      //   237: iconst_0
      //   238: putfield constrainedWidth : Z
      //   241: aload_0
      //   242: iconst_0
      //   243: putfield constrainedHeight : Z
      //   246: aload_0
      //   247: iconst_1
      //   248: putfield horizontalDimensionFixed : Z
      //   251: aload_0
      //   252: iconst_1
      //   253: putfield verticalDimensionFixed : Z
      //   256: aload_0
      //   257: iconst_0
      //   258: putfield needsBaseline : Z
      //   261: aload_0
      //   262: iconst_0
      //   263: putfield isGuideline : Z
      //   266: aload_0
      //   267: iconst_0
      //   268: putfield isHelper : Z
      //   271: aload_0
      //   272: iconst_0
      //   273: putfield isInPlaceholder : Z
      //   276: aload_0
      //   277: iconst_m1
      //   278: putfield resolvedLeftToLeft : I
      //   281: aload_0
      //   282: iconst_m1
      //   283: putfield resolvedLeftToRight : I
      //   286: aload_0
      //   287: iconst_m1
      //   288: putfield resolvedRightToLeft : I
      //   291: aload_0
      //   292: iconst_m1
      //   293: putfield resolvedRightToRight : I
      //   296: aload_0
      //   297: iconst_m1
      //   298: putfield resolveGoneLeftMargin : I
      //   301: aload_0
      //   302: iconst_m1
      //   303: putfield resolveGoneRightMargin : I
      //   306: aload_0
      //   307: ldc 0.5
      //   309: putfield resolvedHorizontalBias : F
      //   312: aload_0
      //   313: new androidx/constraintlayout/solver/widgets/ConstraintWidget
      //   316: dup
      //   317: invokespecial <init> : ()V
      //   320: putfield widget : Landroidx/constraintlayout/solver/widgets/ConstraintWidget;
      //   323: aload_0
      //   324: iconst_0
      //   325: putfield helped : Z
      //   328: aload_1
      //   329: aload_2
      //   330: getstatic androidx/constraintlayout/widget/R$styleable.ConstraintLayout_Layout : [I
      //   333: invokevirtual obtainStyledAttributes : (Landroid/util/AttributeSet;[I)Landroid/content/res/TypedArray;
      //   336: astore_1
      //   337: aload_1
      //   338: invokevirtual getIndexCount : ()I
      //   341: istore_3
      //   342: iconst_0
      //   343: istore #4
      //   345: iload #4
      //   347: iload_3
      //   348: if_icmpge -> 2040
      //   351: aload_1
      //   352: iload #4
      //   354: invokevirtual getIndex : (I)I
      //   357: istore #5
      //   359: getstatic androidx/constraintlayout/widget/ConstraintLayout$LayoutParams$Table.map : Landroid/util/SparseIntArray;
      //   362: iload #5
      //   364: invokevirtual get : (I)I
      //   367: tableswitch default -> 584, 0 -> 2034, 1 -> 2020, 2 -> 1984, 3 -> 1967, 4 -> 1919, 5 -> 1902, 6 -> 1885, 7 -> 1868, 8 -> 1832, 9 -> 1796, 10 -> 1760, 11 -> 1724, 12 -> 1688, 13 -> 1652, 14 -> 1616, 15 -> 1580, 16 -> 1544, 17 -> 1508, 18 -> 1472, 19 -> 1436, 20 -> 1400, 21 -> 1383, 22 -> 1366, 23 -> 1349, 24 -> 1332, 25 -> 1315, 26 -> 1298, 27 -> 1281, 28 -> 1264, 29 -> 1247, 30 -> 1230, 31 -> 1198, 32 -> 1166, 33 -> 1124, 34 -> 1082, 35 -> 1061, 36 -> 1019, 37 -> 977, 38 -> 956, 39 -> 2034, 40 -> 2034, 41 -> 2034, 42 -> 2034, 43 -> 584, 44 -> 683, 45 -> 666, 46 -> 649, 47 -> 635, 48 -> 621, 49 -> 604, 50 -> 587
      //   584: goto -> 2034
      //   587: aload_0
      //   588: aload_1
      //   589: iload #5
      //   591: aload_0
      //   592: getfield editorAbsoluteY : I
      //   595: invokevirtual getDimensionPixelOffset : (II)I
      //   598: putfield editorAbsoluteY : I
      //   601: goto -> 2034
      //   604: aload_0
      //   605: aload_1
      //   606: iload #5
      //   608: aload_0
      //   609: getfield editorAbsoluteX : I
      //   612: invokevirtual getDimensionPixelOffset : (II)I
      //   615: putfield editorAbsoluteX : I
      //   618: goto -> 2034
      //   621: aload_0
      //   622: aload_1
      //   623: iload #5
      //   625: iconst_0
      //   626: invokevirtual getInt : (II)I
      //   629: putfield verticalChainStyle : I
      //   632: goto -> 2034
      //   635: aload_0
      //   636: aload_1
      //   637: iload #5
      //   639: iconst_0
      //   640: invokevirtual getInt : (II)I
      //   643: putfield horizontalChainStyle : I
      //   646: goto -> 2034
      //   649: aload_0
      //   650: aload_1
      //   651: iload #5
      //   653: aload_0
      //   654: getfield verticalWeight : F
      //   657: invokevirtual getFloat : (IF)F
      //   660: putfield verticalWeight : F
      //   663: goto -> 2034
      //   666: aload_0
      //   667: aload_1
      //   668: iload #5
      //   670: aload_0
      //   671: getfield horizontalWeight : F
      //   674: invokevirtual getFloat : (IF)F
      //   677: putfield horizontalWeight : F
      //   680: goto -> 2034
      //   683: aload_0
      //   684: aload_1
      //   685: iload #5
      //   687: invokevirtual getString : (I)Ljava/lang/String;
      //   690: putfield dimensionRatio : Ljava/lang/String;
      //   693: aload_0
      //   694: ldc_w NaN
      //   697: putfield dimensionRatioValue : F
      //   700: aload_0
      //   701: iconst_m1
      //   702: putfield dimensionRatioSide : I
      //   705: aload_0
      //   706: getfield dimensionRatio : Ljava/lang/String;
      //   709: astore_2
      //   710: aload_2
      //   711: ifnull -> 2034
      //   714: aload_2
      //   715: invokevirtual length : ()I
      //   718: istore #6
      //   720: aload_0
      //   721: getfield dimensionRatio : Ljava/lang/String;
      //   724: bipush #44
      //   726: invokevirtual indexOf : (I)I
      //   729: istore #5
      //   731: iload #5
      //   733: ifle -> 795
      //   736: iload #5
      //   738: iload #6
      //   740: iconst_1
      //   741: isub
      //   742: if_icmpge -> 795
      //   745: aload_0
      //   746: getfield dimensionRatio : Ljava/lang/String;
      //   749: iconst_0
      //   750: iload #5
      //   752: invokevirtual substring : (II)Ljava/lang/String;
      //   755: astore_2
      //   756: aload_2
      //   757: ldc_w 'W'
      //   760: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
      //   763: ifeq -> 774
      //   766: aload_0
      //   767: iconst_0
      //   768: putfield dimensionRatioSide : I
      //   771: goto -> 789
      //   774: aload_2
      //   775: ldc_w 'H'
      //   778: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
      //   781: ifeq -> 789
      //   784: aload_0
      //   785: iconst_1
      //   786: putfield dimensionRatioSide : I
      //   789: iinc #5, 1
      //   792: goto -> 798
      //   795: iconst_0
      //   796: istore #5
      //   798: aload_0
      //   799: getfield dimensionRatio : Ljava/lang/String;
      //   802: bipush #58
      //   804: invokevirtual indexOf : (I)I
      //   807: istore #7
      //   809: iload #7
      //   811: iflt -> 928
      //   814: iload #7
      //   816: iload #6
      //   818: iconst_1
      //   819: isub
      //   820: if_icmpge -> 928
      //   823: aload_0
      //   824: getfield dimensionRatio : Ljava/lang/String;
      //   827: iload #5
      //   829: iload #7
      //   831: invokevirtual substring : (II)Ljava/lang/String;
      //   834: astore_2
      //   835: aload_0
      //   836: getfield dimensionRatio : Ljava/lang/String;
      //   839: iload #7
      //   841: iconst_1
      //   842: iadd
      //   843: invokevirtual substring : (I)Ljava/lang/String;
      //   846: astore #8
      //   848: aload_2
      //   849: invokevirtual length : ()I
      //   852: ifle -> 2034
      //   855: aload #8
      //   857: invokevirtual length : ()I
      //   860: ifle -> 2034
      //   863: aload_2
      //   864: invokestatic parseFloat : (Ljava/lang/String;)F
      //   867: fstore #9
      //   869: aload #8
      //   871: invokestatic parseFloat : (Ljava/lang/String;)F
      //   874: fstore #10
      //   876: fload #9
      //   878: fconst_0
      //   879: fcmpl
      //   880: ifle -> 2034
      //   883: fload #10
      //   885: fconst_0
      //   886: fcmpl
      //   887: ifle -> 2034
      //   890: aload_0
      //   891: getfield dimensionRatioSide : I
      //   894: iconst_1
      //   895: if_icmpne -> 913
      //   898: aload_0
      //   899: fload #10
      //   901: fload #9
      //   903: fdiv
      //   904: invokestatic abs : (F)F
      //   907: putfield dimensionRatioValue : F
      //   910: goto -> 2034
      //   913: aload_0
      //   914: fload #9
      //   916: fload #10
      //   918: fdiv
      //   919: invokestatic abs : (F)F
      //   922: putfield dimensionRatioValue : F
      //   925: goto -> 2034
      //   928: aload_0
      //   929: getfield dimensionRatio : Ljava/lang/String;
      //   932: iload #5
      //   934: invokevirtual substring : (I)Ljava/lang/String;
      //   937: astore_2
      //   938: aload_2
      //   939: invokevirtual length : ()I
      //   942: ifle -> 2034
      //   945: aload_0
      //   946: aload_2
      //   947: invokestatic parseFloat : (Ljava/lang/String;)F
      //   950: putfield dimensionRatioValue : F
      //   953: goto -> 2034
      //   956: aload_0
      //   957: fconst_0
      //   958: aload_1
      //   959: iload #5
      //   961: aload_0
      //   962: getfield matchConstraintPercentHeight : F
      //   965: invokevirtual getFloat : (IF)F
      //   968: invokestatic max : (FF)F
      //   971: putfield matchConstraintPercentHeight : F
      //   974: goto -> 2034
      //   977: aload_0
      //   978: aload_1
      //   979: iload #5
      //   981: aload_0
      //   982: getfield matchConstraintMaxHeight : I
      //   985: invokevirtual getDimensionPixelSize : (II)I
      //   988: putfield matchConstraintMaxHeight : I
      //   991: goto -> 2034
      //   994: astore_2
      //   995: aload_1
      //   996: iload #5
      //   998: aload_0
      //   999: getfield matchConstraintMaxHeight : I
      //   1002: invokevirtual getInt : (II)I
      //   1005: bipush #-2
      //   1007: if_icmpne -> 2034
      //   1010: aload_0
      //   1011: bipush #-2
      //   1013: putfield matchConstraintMaxHeight : I
      //   1016: goto -> 2034
      //   1019: aload_0
      //   1020: aload_1
      //   1021: iload #5
      //   1023: aload_0
      //   1024: getfield matchConstraintMinHeight : I
      //   1027: invokevirtual getDimensionPixelSize : (II)I
      //   1030: putfield matchConstraintMinHeight : I
      //   1033: goto -> 2034
      //   1036: astore_2
      //   1037: aload_1
      //   1038: iload #5
      //   1040: aload_0
      //   1041: getfield matchConstraintMinHeight : I
      //   1044: invokevirtual getInt : (II)I
      //   1047: bipush #-2
      //   1049: if_icmpne -> 2034
      //   1052: aload_0
      //   1053: bipush #-2
      //   1055: putfield matchConstraintMinHeight : I
      //   1058: goto -> 2034
      //   1061: aload_0
      //   1062: fconst_0
      //   1063: aload_1
      //   1064: iload #5
      //   1066: aload_0
      //   1067: getfield matchConstraintPercentWidth : F
      //   1070: invokevirtual getFloat : (IF)F
      //   1073: invokestatic max : (FF)F
      //   1076: putfield matchConstraintPercentWidth : F
      //   1079: goto -> 2034
      //   1082: aload_0
      //   1083: aload_1
      //   1084: iload #5
      //   1086: aload_0
      //   1087: getfield matchConstraintMaxWidth : I
      //   1090: invokevirtual getDimensionPixelSize : (II)I
      //   1093: putfield matchConstraintMaxWidth : I
      //   1096: goto -> 2034
      //   1099: astore_2
      //   1100: aload_1
      //   1101: iload #5
      //   1103: aload_0
      //   1104: getfield matchConstraintMaxWidth : I
      //   1107: invokevirtual getInt : (II)I
      //   1110: bipush #-2
      //   1112: if_icmpne -> 2034
      //   1115: aload_0
      //   1116: bipush #-2
      //   1118: putfield matchConstraintMaxWidth : I
      //   1121: goto -> 2034
      //   1124: aload_0
      //   1125: aload_1
      //   1126: iload #5
      //   1128: aload_0
      //   1129: getfield matchConstraintMinWidth : I
      //   1132: invokevirtual getDimensionPixelSize : (II)I
      //   1135: putfield matchConstraintMinWidth : I
      //   1138: goto -> 2034
      //   1141: astore_2
      //   1142: aload_1
      //   1143: iload #5
      //   1145: aload_0
      //   1146: getfield matchConstraintMinWidth : I
      //   1149: invokevirtual getInt : (II)I
      //   1152: bipush #-2
      //   1154: if_icmpne -> 2034
      //   1157: aload_0
      //   1158: bipush #-2
      //   1160: putfield matchConstraintMinWidth : I
      //   1163: goto -> 2034
      //   1166: aload_0
      //   1167: aload_1
      //   1168: iload #5
      //   1170: iconst_0
      //   1171: invokevirtual getInt : (II)I
      //   1174: putfield matchConstraintDefaultHeight : I
      //   1177: aload_0
      //   1178: getfield matchConstraintDefaultHeight : I
      //   1181: iconst_1
      //   1182: if_icmpne -> 2034
      //   1185: ldc_w 'ConstraintLayout'
      //   1188: ldc_w 'layout_constraintHeight_default="wrap" is deprecated.\\nUse layout_height="WRAP_CONTENT" and layout_constrainedHeight="true" instead.'
      //   1191: invokestatic e : (Ljava/lang/String;Ljava/lang/String;)I
      //   1194: pop
      //   1195: goto -> 2034
      //   1198: aload_0
      //   1199: aload_1
      //   1200: iload #5
      //   1202: iconst_0
      //   1203: invokevirtual getInt : (II)I
      //   1206: putfield matchConstraintDefaultWidth : I
      //   1209: aload_0
      //   1210: getfield matchConstraintDefaultWidth : I
      //   1213: iconst_1
      //   1214: if_icmpne -> 2034
      //   1217: ldc_w 'ConstraintLayout'
      //   1220: ldc_w 'layout_constraintWidth_default="wrap" is deprecated.\\nUse layout_width="WRAP_CONTENT" and layout_constrainedWidth="true" instead.'
      //   1223: invokestatic e : (Ljava/lang/String;Ljava/lang/String;)I
      //   1226: pop
      //   1227: goto -> 2034
      //   1230: aload_0
      //   1231: aload_1
      //   1232: iload #5
      //   1234: aload_0
      //   1235: getfield verticalBias : F
      //   1238: invokevirtual getFloat : (IF)F
      //   1241: putfield verticalBias : F
      //   1244: goto -> 2034
      //   1247: aload_0
      //   1248: aload_1
      //   1249: iload #5
      //   1251: aload_0
      //   1252: getfield horizontalBias : F
      //   1255: invokevirtual getFloat : (IF)F
      //   1258: putfield horizontalBias : F
      //   1261: goto -> 2034
      //   1264: aload_0
      //   1265: aload_1
      //   1266: iload #5
      //   1268: aload_0
      //   1269: getfield constrainedHeight : Z
      //   1272: invokevirtual getBoolean : (IZ)Z
      //   1275: putfield constrainedHeight : Z
      //   1278: goto -> 2034
      //   1281: aload_0
      //   1282: aload_1
      //   1283: iload #5
      //   1285: aload_0
      //   1286: getfield constrainedWidth : Z
      //   1289: invokevirtual getBoolean : (IZ)Z
      //   1292: putfield constrainedWidth : Z
      //   1295: goto -> 2034
      //   1298: aload_0
      //   1299: aload_1
      //   1300: iload #5
      //   1302: aload_0
      //   1303: getfield goneEndMargin : I
      //   1306: invokevirtual getDimensionPixelSize : (II)I
      //   1309: putfield goneEndMargin : I
      //   1312: goto -> 2034
      //   1315: aload_0
      //   1316: aload_1
      //   1317: iload #5
      //   1319: aload_0
      //   1320: getfield goneStartMargin : I
      //   1323: invokevirtual getDimensionPixelSize : (II)I
      //   1326: putfield goneStartMargin : I
      //   1329: goto -> 2034
      //   1332: aload_0
      //   1333: aload_1
      //   1334: iload #5
      //   1336: aload_0
      //   1337: getfield goneBottomMargin : I
      //   1340: invokevirtual getDimensionPixelSize : (II)I
      //   1343: putfield goneBottomMargin : I
      //   1346: goto -> 2034
      //   1349: aload_0
      //   1350: aload_1
      //   1351: iload #5
      //   1353: aload_0
      //   1354: getfield goneRightMargin : I
      //   1357: invokevirtual getDimensionPixelSize : (II)I
      //   1360: putfield goneRightMargin : I
      //   1363: goto -> 2034
      //   1366: aload_0
      //   1367: aload_1
      //   1368: iload #5
      //   1370: aload_0
      //   1371: getfield goneTopMargin : I
      //   1374: invokevirtual getDimensionPixelSize : (II)I
      //   1377: putfield goneTopMargin : I
      //   1380: goto -> 2034
      //   1383: aload_0
      //   1384: aload_1
      //   1385: iload #5
      //   1387: aload_0
      //   1388: getfield goneLeftMargin : I
      //   1391: invokevirtual getDimensionPixelSize : (II)I
      //   1394: putfield goneLeftMargin : I
      //   1397: goto -> 2034
      //   1400: aload_0
      //   1401: aload_1
      //   1402: iload #5
      //   1404: aload_0
      //   1405: getfield endToEnd : I
      //   1408: invokevirtual getResourceId : (II)I
      //   1411: putfield endToEnd : I
      //   1414: aload_0
      //   1415: getfield endToEnd : I
      //   1418: iconst_m1
      //   1419: if_icmpne -> 2034
      //   1422: aload_0
      //   1423: aload_1
      //   1424: iload #5
      //   1426: iconst_m1
      //   1427: invokevirtual getInt : (II)I
      //   1430: putfield endToEnd : I
      //   1433: goto -> 2034
      //   1436: aload_0
      //   1437: aload_1
      //   1438: iload #5
      //   1440: aload_0
      //   1441: getfield endToStart : I
      //   1444: invokevirtual getResourceId : (II)I
      //   1447: putfield endToStart : I
      //   1450: aload_0
      //   1451: getfield endToStart : I
      //   1454: iconst_m1
      //   1455: if_icmpne -> 2034
      //   1458: aload_0
      //   1459: aload_1
      //   1460: iload #5
      //   1462: iconst_m1
      //   1463: invokevirtual getInt : (II)I
      //   1466: putfield endToStart : I
      //   1469: goto -> 2034
      //   1472: aload_0
      //   1473: aload_1
      //   1474: iload #5
      //   1476: aload_0
      //   1477: getfield startToStart : I
      //   1480: invokevirtual getResourceId : (II)I
      //   1483: putfield startToStart : I
      //   1486: aload_0
      //   1487: getfield startToStart : I
      //   1490: iconst_m1
      //   1491: if_icmpne -> 2034
      //   1494: aload_0
      //   1495: aload_1
      //   1496: iload #5
      //   1498: iconst_m1
      //   1499: invokevirtual getInt : (II)I
      //   1502: putfield startToStart : I
      //   1505: goto -> 2034
      //   1508: aload_0
      //   1509: aload_1
      //   1510: iload #5
      //   1512: aload_0
      //   1513: getfield startToEnd : I
      //   1516: invokevirtual getResourceId : (II)I
      //   1519: putfield startToEnd : I
      //   1522: aload_0
      //   1523: getfield startToEnd : I
      //   1526: iconst_m1
      //   1527: if_icmpne -> 2034
      //   1530: aload_0
      //   1531: aload_1
      //   1532: iload #5
      //   1534: iconst_m1
      //   1535: invokevirtual getInt : (II)I
      //   1538: putfield startToEnd : I
      //   1541: goto -> 2034
      //   1544: aload_0
      //   1545: aload_1
      //   1546: iload #5
      //   1548: aload_0
      //   1549: getfield baselineToBaseline : I
      //   1552: invokevirtual getResourceId : (II)I
      //   1555: putfield baselineToBaseline : I
      //   1558: aload_0
      //   1559: getfield baselineToBaseline : I
      //   1562: iconst_m1
      //   1563: if_icmpne -> 2034
      //   1566: aload_0
      //   1567: aload_1
      //   1568: iload #5
      //   1570: iconst_m1
      //   1571: invokevirtual getInt : (II)I
      //   1574: putfield baselineToBaseline : I
      //   1577: goto -> 2034
      //   1580: aload_0
      //   1581: aload_1
      //   1582: iload #5
      //   1584: aload_0
      //   1585: getfield bottomToBottom : I
      //   1588: invokevirtual getResourceId : (II)I
      //   1591: putfield bottomToBottom : I
      //   1594: aload_0
      //   1595: getfield bottomToBottom : I
      //   1598: iconst_m1
      //   1599: if_icmpne -> 2034
      //   1602: aload_0
      //   1603: aload_1
      //   1604: iload #5
      //   1606: iconst_m1
      //   1607: invokevirtual getInt : (II)I
      //   1610: putfield bottomToBottom : I
      //   1613: goto -> 2034
      //   1616: aload_0
      //   1617: aload_1
      //   1618: iload #5
      //   1620: aload_0
      //   1621: getfield bottomToTop : I
      //   1624: invokevirtual getResourceId : (II)I
      //   1627: putfield bottomToTop : I
      //   1630: aload_0
      //   1631: getfield bottomToTop : I
      //   1634: iconst_m1
      //   1635: if_icmpne -> 2034
      //   1638: aload_0
      //   1639: aload_1
      //   1640: iload #5
      //   1642: iconst_m1
      //   1643: invokevirtual getInt : (II)I
      //   1646: putfield bottomToTop : I
      //   1649: goto -> 2034
      //   1652: aload_0
      //   1653: aload_1
      //   1654: iload #5
      //   1656: aload_0
      //   1657: getfield topToBottom : I
      //   1660: invokevirtual getResourceId : (II)I
      //   1663: putfield topToBottom : I
      //   1666: aload_0
      //   1667: getfield topToBottom : I
      //   1670: iconst_m1
      //   1671: if_icmpne -> 2034
      //   1674: aload_0
      //   1675: aload_1
      //   1676: iload #5
      //   1678: iconst_m1
      //   1679: invokevirtual getInt : (II)I
      //   1682: putfield topToBottom : I
      //   1685: goto -> 2034
      //   1688: aload_0
      //   1689: aload_1
      //   1690: iload #5
      //   1692: aload_0
      //   1693: getfield topToTop : I
      //   1696: invokevirtual getResourceId : (II)I
      //   1699: putfield topToTop : I
      //   1702: aload_0
      //   1703: getfield topToTop : I
      //   1706: iconst_m1
      //   1707: if_icmpne -> 2034
      //   1710: aload_0
      //   1711: aload_1
      //   1712: iload #5
      //   1714: iconst_m1
      //   1715: invokevirtual getInt : (II)I
      //   1718: putfield topToTop : I
      //   1721: goto -> 2034
      //   1724: aload_0
      //   1725: aload_1
      //   1726: iload #5
      //   1728: aload_0
      //   1729: getfield rightToRight : I
      //   1732: invokevirtual getResourceId : (II)I
      //   1735: putfield rightToRight : I
      //   1738: aload_0
      //   1739: getfield rightToRight : I
      //   1742: iconst_m1
      //   1743: if_icmpne -> 2034
      //   1746: aload_0
      //   1747: aload_1
      //   1748: iload #5
      //   1750: iconst_m1
      //   1751: invokevirtual getInt : (II)I
      //   1754: putfield rightToRight : I
      //   1757: goto -> 2034
      //   1760: aload_0
      //   1761: aload_1
      //   1762: iload #5
      //   1764: aload_0
      //   1765: getfield rightToLeft : I
      //   1768: invokevirtual getResourceId : (II)I
      //   1771: putfield rightToLeft : I
      //   1774: aload_0
      //   1775: getfield rightToLeft : I
      //   1778: iconst_m1
      //   1779: if_icmpne -> 2034
      //   1782: aload_0
      //   1783: aload_1
      //   1784: iload #5
      //   1786: iconst_m1
      //   1787: invokevirtual getInt : (II)I
      //   1790: putfield rightToLeft : I
      //   1793: goto -> 2034
      //   1796: aload_0
      //   1797: aload_1
      //   1798: iload #5
      //   1800: aload_0
      //   1801: getfield leftToRight : I
      //   1804: invokevirtual getResourceId : (II)I
      //   1807: putfield leftToRight : I
      //   1810: aload_0
      //   1811: getfield leftToRight : I
      //   1814: iconst_m1
      //   1815: if_icmpne -> 2034
      //   1818: aload_0
      //   1819: aload_1
      //   1820: iload #5
      //   1822: iconst_m1
      //   1823: invokevirtual getInt : (II)I
      //   1826: putfield leftToRight : I
      //   1829: goto -> 2034
      //   1832: aload_0
      //   1833: aload_1
      //   1834: iload #5
      //   1836: aload_0
      //   1837: getfield leftToLeft : I
      //   1840: invokevirtual getResourceId : (II)I
      //   1843: putfield leftToLeft : I
      //   1846: aload_0
      //   1847: getfield leftToLeft : I
      //   1850: iconst_m1
      //   1851: if_icmpne -> 2034
      //   1854: aload_0
      //   1855: aload_1
      //   1856: iload #5
      //   1858: iconst_m1
      //   1859: invokevirtual getInt : (II)I
      //   1862: putfield leftToLeft : I
      //   1865: goto -> 2034
      //   1868: aload_0
      //   1869: aload_1
      //   1870: iload #5
      //   1872: aload_0
      //   1873: getfield guidePercent : F
      //   1876: invokevirtual getFloat : (IF)F
      //   1879: putfield guidePercent : F
      //   1882: goto -> 2034
      //   1885: aload_0
      //   1886: aload_1
      //   1887: iload #5
      //   1889: aload_0
      //   1890: getfield guideEnd : I
      //   1893: invokevirtual getDimensionPixelOffset : (II)I
      //   1896: putfield guideEnd : I
      //   1899: goto -> 2034
      //   1902: aload_0
      //   1903: aload_1
      //   1904: iload #5
      //   1906: aload_0
      //   1907: getfield guideBegin : I
      //   1910: invokevirtual getDimensionPixelOffset : (II)I
      //   1913: putfield guideBegin : I
      //   1916: goto -> 2034
      //   1919: aload_0
      //   1920: aload_1
      //   1921: iload #5
      //   1923: aload_0
      //   1924: getfield circleAngle : F
      //   1927: invokevirtual getFloat : (IF)F
      //   1930: ldc_w 360.0
      //   1933: frem
      //   1934: putfield circleAngle : F
      //   1937: aload_0
      //   1938: getfield circleAngle : F
      //   1941: fstore #9
      //   1943: fload #9
      //   1945: fconst_0
      //   1946: fcmpg
      //   1947: ifge -> 2034
      //   1950: aload_0
      //   1951: ldc_w 360.0
      //   1954: fload #9
      //   1956: fsub
      //   1957: ldc_w 360.0
      //   1960: frem
      //   1961: putfield circleAngle : F
      //   1964: goto -> 2034
      //   1967: aload_0
      //   1968: aload_1
      //   1969: iload #5
      //   1971: aload_0
      //   1972: getfield circleRadius : I
      //   1975: invokevirtual getDimensionPixelSize : (II)I
      //   1978: putfield circleRadius : I
      //   1981: goto -> 2034
      //   1984: aload_0
      //   1985: aload_1
      //   1986: iload #5
      //   1988: aload_0
      //   1989: getfield circleConstraint : I
      //   1992: invokevirtual getResourceId : (II)I
      //   1995: putfield circleConstraint : I
      //   1998: aload_0
      //   1999: getfield circleConstraint : I
      //   2002: iconst_m1
      //   2003: if_icmpne -> 2034
      //   2006: aload_0
      //   2007: aload_1
      //   2008: iload #5
      //   2010: iconst_m1
      //   2011: invokevirtual getInt : (II)I
      //   2014: putfield circleConstraint : I
      //   2017: goto -> 2034
      //   2020: aload_0
      //   2021: aload_1
      //   2022: iload #5
      //   2024: aload_0
      //   2025: getfield orientation : I
      //   2028: invokevirtual getInt : (II)I
      //   2031: putfield orientation : I
      //   2034: iinc #4, 1
      //   2037: goto -> 345
      //   2040: aload_1
      //   2041: invokevirtual recycle : ()V
      //   2044: aload_0
      //   2045: invokevirtual validate : ()V
      //   2048: return
      //   2049: astore_2
      //   2050: goto -> 2034
      // Exception table:
      //   from	to	target	type
      //   863	876	2049	java/lang/NumberFormatException
      //   890	910	2049	java/lang/NumberFormatException
      //   913	925	2049	java/lang/NumberFormatException
      //   945	953	2049	java/lang/NumberFormatException
      //   977	991	994	java/lang/Exception
      //   1019	1033	1036	java/lang/Exception
      //   1082	1096	1099	java/lang/Exception
      //   1124	1138	1141	java/lang/Exception
    }
    
    public LayoutParams(ViewGroup.LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
    }
    
    public LayoutParams(LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
      this.guideBegin = param1LayoutParams.guideBegin;
      this.guideEnd = param1LayoutParams.guideEnd;
      this.guidePercent = param1LayoutParams.guidePercent;
      this.leftToLeft = param1LayoutParams.leftToLeft;
      this.leftToRight = param1LayoutParams.leftToRight;
      this.rightToLeft = param1LayoutParams.rightToLeft;
      this.rightToRight = param1LayoutParams.rightToRight;
      this.topToTop = param1LayoutParams.topToTop;
      this.topToBottom = param1LayoutParams.topToBottom;
      this.bottomToTop = param1LayoutParams.bottomToTop;
      this.bottomToBottom = param1LayoutParams.bottomToBottom;
      this.baselineToBaseline = param1LayoutParams.baselineToBaseline;
      this.circleConstraint = param1LayoutParams.circleConstraint;
      this.circleRadius = param1LayoutParams.circleRadius;
      this.circleAngle = param1LayoutParams.circleAngle;
      this.startToEnd = param1LayoutParams.startToEnd;
      this.startToStart = param1LayoutParams.startToStart;
      this.endToStart = param1LayoutParams.endToStart;
      this.endToEnd = param1LayoutParams.endToEnd;
      this.goneLeftMargin = param1LayoutParams.goneLeftMargin;
      this.goneTopMargin = param1LayoutParams.goneTopMargin;
      this.goneRightMargin = param1LayoutParams.goneRightMargin;
      this.goneBottomMargin = param1LayoutParams.goneBottomMargin;
      this.goneStartMargin = param1LayoutParams.goneStartMargin;
      this.goneEndMargin = param1LayoutParams.goneEndMargin;
      this.horizontalBias = param1LayoutParams.horizontalBias;
      this.verticalBias = param1LayoutParams.verticalBias;
      this.dimensionRatio = param1LayoutParams.dimensionRatio;
      this.dimensionRatioValue = param1LayoutParams.dimensionRatioValue;
      this.dimensionRatioSide = param1LayoutParams.dimensionRatioSide;
      this.horizontalWeight = param1LayoutParams.horizontalWeight;
      this.verticalWeight = param1LayoutParams.verticalWeight;
      this.horizontalChainStyle = param1LayoutParams.horizontalChainStyle;
      this.verticalChainStyle = param1LayoutParams.verticalChainStyle;
      this.constrainedWidth = param1LayoutParams.constrainedWidth;
      this.constrainedHeight = param1LayoutParams.constrainedHeight;
      this.matchConstraintDefaultWidth = param1LayoutParams.matchConstraintDefaultWidth;
      this.matchConstraintDefaultHeight = param1LayoutParams.matchConstraintDefaultHeight;
      this.matchConstraintMinWidth = param1LayoutParams.matchConstraintMinWidth;
      this.matchConstraintMaxWidth = param1LayoutParams.matchConstraintMaxWidth;
      this.matchConstraintMinHeight = param1LayoutParams.matchConstraintMinHeight;
      this.matchConstraintMaxHeight = param1LayoutParams.matchConstraintMaxHeight;
      this.matchConstraintPercentWidth = param1LayoutParams.matchConstraintPercentWidth;
      this.matchConstraintPercentHeight = param1LayoutParams.matchConstraintPercentHeight;
      this.editorAbsoluteX = param1LayoutParams.editorAbsoluteX;
      this.editorAbsoluteY = param1LayoutParams.editorAbsoluteY;
      this.orientation = param1LayoutParams.orientation;
      this.horizontalDimensionFixed = param1LayoutParams.horizontalDimensionFixed;
      this.verticalDimensionFixed = param1LayoutParams.verticalDimensionFixed;
      this.needsBaseline = param1LayoutParams.needsBaseline;
      this.isGuideline = param1LayoutParams.isGuideline;
      this.resolvedLeftToLeft = param1LayoutParams.resolvedLeftToLeft;
      this.resolvedLeftToRight = param1LayoutParams.resolvedLeftToRight;
      this.resolvedRightToLeft = param1LayoutParams.resolvedRightToLeft;
      this.resolvedRightToRight = param1LayoutParams.resolvedRightToRight;
      this.resolveGoneLeftMargin = param1LayoutParams.resolveGoneLeftMargin;
      this.resolveGoneRightMargin = param1LayoutParams.resolveGoneRightMargin;
      this.resolvedHorizontalBias = param1LayoutParams.resolvedHorizontalBias;
      this.widget = param1LayoutParams.widget;
    }
    
    public void reset() {
      ConstraintWidget constraintWidget = this.widget;
      if (constraintWidget != null)
        constraintWidget.reset(); 
    }
    
    public void resolveLayoutDirection(int param1Int) {
      // Byte code:
      //   0: aload_0
      //   1: getfield leftMargin : I
      //   4: istore_2
      //   5: aload_0
      //   6: getfield rightMargin : I
      //   9: istore_3
      //   10: aload_0
      //   11: iload_1
      //   12: invokespecial resolveLayoutDirection : (I)V
      //   15: aload_0
      //   16: iconst_m1
      //   17: putfield resolvedRightToLeft : I
      //   20: aload_0
      //   21: iconst_m1
      //   22: putfield resolvedRightToRight : I
      //   25: aload_0
      //   26: iconst_m1
      //   27: putfield resolvedLeftToLeft : I
      //   30: aload_0
      //   31: iconst_m1
      //   32: putfield resolvedLeftToRight : I
      //   35: aload_0
      //   36: iconst_m1
      //   37: putfield resolveGoneLeftMargin : I
      //   40: aload_0
      //   41: iconst_m1
      //   42: putfield resolveGoneRightMargin : I
      //   45: aload_0
      //   46: aload_0
      //   47: getfield goneLeftMargin : I
      //   50: putfield resolveGoneLeftMargin : I
      //   53: aload_0
      //   54: aload_0
      //   55: getfield goneRightMargin : I
      //   58: putfield resolveGoneRightMargin : I
      //   61: aload_0
      //   62: aload_0
      //   63: getfield horizontalBias : F
      //   66: putfield resolvedHorizontalBias : F
      //   69: aload_0
      //   70: aload_0
      //   71: getfield guideBegin : I
      //   74: putfield resolvedGuideBegin : I
      //   77: aload_0
      //   78: aload_0
      //   79: getfield guideEnd : I
      //   82: putfield resolvedGuideEnd : I
      //   85: aload_0
      //   86: aload_0
      //   87: getfield guidePercent : F
      //   90: putfield resolvedGuidePercent : F
      //   93: aload_0
      //   94: invokevirtual getLayoutDirection : ()I
      //   97: istore_1
      //   98: iconst_0
      //   99: istore #4
      //   101: iconst_1
      //   102: iload_1
      //   103: if_icmpne -> 111
      //   106: iconst_1
      //   107: istore_1
      //   108: goto -> 113
      //   111: iconst_0
      //   112: istore_1
      //   113: iload_1
      //   114: ifeq -> 359
      //   117: aload_0
      //   118: getfield startToEnd : I
      //   121: istore_1
      //   122: iload_1
      //   123: iconst_m1
      //   124: if_icmpeq -> 137
      //   127: aload_0
      //   128: iload_1
      //   129: putfield resolvedRightToLeft : I
      //   132: iconst_1
      //   133: istore_1
      //   134: goto -> 161
      //   137: aload_0
      //   138: getfield startToStart : I
      //   141: istore #5
      //   143: iload #4
      //   145: istore_1
      //   146: iload #5
      //   148: iconst_m1
      //   149: if_icmpeq -> 161
      //   152: aload_0
      //   153: iload #5
      //   155: putfield resolvedRightToRight : I
      //   158: goto -> 132
      //   161: aload_0
      //   162: getfield endToStart : I
      //   165: istore #4
      //   167: iload #4
      //   169: iconst_m1
      //   170: if_icmpeq -> 181
      //   173: aload_0
      //   174: iload #4
      //   176: putfield resolvedLeftToRight : I
      //   179: iconst_1
      //   180: istore_1
      //   181: aload_0
      //   182: getfield endToEnd : I
      //   185: istore #4
      //   187: iload #4
      //   189: iconst_m1
      //   190: if_icmpeq -> 201
      //   193: aload_0
      //   194: iload #4
      //   196: putfield resolvedLeftToLeft : I
      //   199: iconst_1
      //   200: istore_1
      //   201: aload_0
      //   202: getfield goneStartMargin : I
      //   205: istore #4
      //   207: iload #4
      //   209: iconst_m1
      //   210: if_icmpeq -> 219
      //   213: aload_0
      //   214: iload #4
      //   216: putfield resolveGoneRightMargin : I
      //   219: aload_0
      //   220: getfield goneEndMargin : I
      //   223: istore #4
      //   225: iload #4
      //   227: iconst_m1
      //   228: if_icmpeq -> 237
      //   231: aload_0
      //   232: iload #4
      //   234: putfield resolveGoneLeftMargin : I
      //   237: iload_1
      //   238: ifeq -> 251
      //   241: aload_0
      //   242: fconst_1
      //   243: aload_0
      //   244: getfield horizontalBias : F
      //   247: fsub
      //   248: putfield resolvedHorizontalBias : F
      //   251: aload_0
      //   252: getfield isGuideline : Z
      //   255: ifeq -> 449
      //   258: aload_0
      //   259: getfield orientation : I
      //   262: iconst_1
      //   263: if_icmpne -> 449
      //   266: aload_0
      //   267: getfield guidePercent : F
      //   270: fstore #6
      //   272: fload #6
      //   274: ldc -1.0
      //   276: fcmpl
      //   277: ifeq -> 301
      //   280: aload_0
      //   281: fconst_1
      //   282: fload #6
      //   284: fsub
      //   285: putfield resolvedGuidePercent : F
      //   288: aload_0
      //   289: iconst_m1
      //   290: putfield resolvedGuideBegin : I
      //   293: aload_0
      //   294: iconst_m1
      //   295: putfield resolvedGuideEnd : I
      //   298: goto -> 449
      //   301: aload_0
      //   302: getfield guideBegin : I
      //   305: istore_1
      //   306: iload_1
      //   307: iconst_m1
      //   308: if_icmpeq -> 330
      //   311: aload_0
      //   312: iload_1
      //   313: putfield resolvedGuideEnd : I
      //   316: aload_0
      //   317: iconst_m1
      //   318: putfield resolvedGuideBegin : I
      //   321: aload_0
      //   322: ldc -1.0
      //   324: putfield resolvedGuidePercent : F
      //   327: goto -> 449
      //   330: aload_0
      //   331: getfield guideEnd : I
      //   334: istore_1
      //   335: iload_1
      //   336: iconst_m1
      //   337: if_icmpeq -> 449
      //   340: aload_0
      //   341: iload_1
      //   342: putfield resolvedGuideBegin : I
      //   345: aload_0
      //   346: iconst_m1
      //   347: putfield resolvedGuideEnd : I
      //   350: aload_0
      //   351: ldc -1.0
      //   353: putfield resolvedGuidePercent : F
      //   356: goto -> 449
      //   359: aload_0
      //   360: getfield startToEnd : I
      //   363: istore_1
      //   364: iload_1
      //   365: iconst_m1
      //   366: if_icmpeq -> 374
      //   369: aload_0
      //   370: iload_1
      //   371: putfield resolvedLeftToRight : I
      //   374: aload_0
      //   375: getfield startToStart : I
      //   378: istore_1
      //   379: iload_1
      //   380: iconst_m1
      //   381: if_icmpeq -> 389
      //   384: aload_0
      //   385: iload_1
      //   386: putfield resolvedLeftToLeft : I
      //   389: aload_0
      //   390: getfield endToStart : I
      //   393: istore_1
      //   394: iload_1
      //   395: iconst_m1
      //   396: if_icmpeq -> 404
      //   399: aload_0
      //   400: iload_1
      //   401: putfield resolvedRightToLeft : I
      //   404: aload_0
      //   405: getfield endToEnd : I
      //   408: istore_1
      //   409: iload_1
      //   410: iconst_m1
      //   411: if_icmpeq -> 419
      //   414: aload_0
      //   415: iload_1
      //   416: putfield resolvedRightToRight : I
      //   419: aload_0
      //   420: getfield goneStartMargin : I
      //   423: istore_1
      //   424: iload_1
      //   425: iconst_m1
      //   426: if_icmpeq -> 434
      //   429: aload_0
      //   430: iload_1
      //   431: putfield resolveGoneLeftMargin : I
      //   434: aload_0
      //   435: getfield goneEndMargin : I
      //   438: istore_1
      //   439: iload_1
      //   440: iconst_m1
      //   441: if_icmpeq -> 449
      //   444: aload_0
      //   445: iload_1
      //   446: putfield resolveGoneRightMargin : I
      //   449: aload_0
      //   450: getfield endToStart : I
      //   453: iconst_m1
      //   454: if_icmpne -> 611
      //   457: aload_0
      //   458: getfield endToEnd : I
      //   461: iconst_m1
      //   462: if_icmpne -> 611
      //   465: aload_0
      //   466: getfield startToStart : I
      //   469: iconst_m1
      //   470: if_icmpne -> 611
      //   473: aload_0
      //   474: getfield startToEnd : I
      //   477: iconst_m1
      //   478: if_icmpne -> 611
      //   481: aload_0
      //   482: getfield rightToLeft : I
      //   485: istore_1
      //   486: iload_1
      //   487: iconst_m1
      //   488: if_icmpeq -> 515
      //   491: aload_0
      //   492: iload_1
      //   493: putfield resolvedRightToLeft : I
      //   496: aload_0
      //   497: getfield rightMargin : I
      //   500: ifgt -> 546
      //   503: iload_3
      //   504: ifle -> 546
      //   507: aload_0
      //   508: iload_3
      //   509: putfield rightMargin : I
      //   512: goto -> 546
      //   515: aload_0
      //   516: getfield rightToRight : I
      //   519: istore_1
      //   520: iload_1
      //   521: iconst_m1
      //   522: if_icmpeq -> 546
      //   525: aload_0
      //   526: iload_1
      //   527: putfield resolvedRightToRight : I
      //   530: aload_0
      //   531: getfield rightMargin : I
      //   534: ifgt -> 546
      //   537: iload_3
      //   538: ifle -> 546
      //   541: aload_0
      //   542: iload_3
      //   543: putfield rightMargin : I
      //   546: aload_0
      //   547: getfield leftToLeft : I
      //   550: istore_1
      //   551: iload_1
      //   552: iconst_m1
      //   553: if_icmpeq -> 580
      //   556: aload_0
      //   557: iload_1
      //   558: putfield resolvedLeftToLeft : I
      //   561: aload_0
      //   562: getfield leftMargin : I
      //   565: ifgt -> 611
      //   568: iload_2
      //   569: ifle -> 611
      //   572: aload_0
      //   573: iload_2
      //   574: putfield leftMargin : I
      //   577: goto -> 611
      //   580: aload_0
      //   581: getfield leftToRight : I
      //   584: istore_1
      //   585: iload_1
      //   586: iconst_m1
      //   587: if_icmpeq -> 611
      //   590: aload_0
      //   591: iload_1
      //   592: putfield resolvedLeftToRight : I
      //   595: aload_0
      //   596: getfield leftMargin : I
      //   599: ifgt -> 611
      //   602: iload_2
      //   603: ifle -> 611
      //   606: aload_0
      //   607: iload_2
      //   608: putfield leftMargin : I
      //   611: return
    }
    
    public void validate() {
      this.isGuideline = false;
      this.horizontalDimensionFixed = true;
      this.verticalDimensionFixed = true;
      if (this.width == -2 && this.constrainedWidth) {
        this.horizontalDimensionFixed = false;
        this.matchConstraintDefaultWidth = 1;
      } 
      if (this.height == -2 && this.constrainedHeight) {
        this.verticalDimensionFixed = false;
        this.matchConstraintDefaultHeight = 1;
      } 
      if (this.width == 0 || this.width == -1) {
        this.horizontalDimensionFixed = false;
        if (this.width == 0 && this.matchConstraintDefaultWidth == 1) {
          this.width = -2;
          this.constrainedWidth = true;
        } 
      } 
      if (this.height == 0 || this.height == -1) {
        this.verticalDimensionFixed = false;
        if (this.height == 0 && this.matchConstraintDefaultHeight == 1) {
          this.height = -2;
          this.constrainedHeight = true;
        } 
      } 
      if (this.guidePercent != -1.0F || this.guideBegin != -1 || this.guideEnd != -1) {
        this.isGuideline = true;
        this.horizontalDimensionFixed = true;
        this.verticalDimensionFixed = true;
        if (!(this.widget instanceof Guideline))
          this.widget = (ConstraintWidget)new Guideline(); 
        ((Guideline)this.widget).setOrientation(this.orientation);
      } 
    }
    
    private static class Table {
      public static final int ANDROID_ORIENTATION = 1;
      
      public static final int LAYOUT_CONSTRAINED_HEIGHT = 28;
      
      public static final int LAYOUT_CONSTRAINED_WIDTH = 27;
      
      public static final int LAYOUT_CONSTRAINT_BASELINE_CREATOR = 43;
      
      public static final int LAYOUT_CONSTRAINT_BASELINE_TO_BASELINE_OF = 16;
      
      public static final int LAYOUT_CONSTRAINT_BOTTOM_CREATOR = 42;
      
      public static final int LAYOUT_CONSTRAINT_BOTTOM_TO_BOTTOM_OF = 15;
      
      public static final int LAYOUT_CONSTRAINT_BOTTOM_TO_TOP_OF = 14;
      
      public static final int LAYOUT_CONSTRAINT_CIRCLE = 2;
      
      public static final int LAYOUT_CONSTRAINT_CIRCLE_ANGLE = 4;
      
      public static final int LAYOUT_CONSTRAINT_CIRCLE_RADIUS = 3;
      
      public static final int LAYOUT_CONSTRAINT_DIMENSION_RATIO = 44;
      
      public static final int LAYOUT_CONSTRAINT_END_TO_END_OF = 20;
      
      public static final int LAYOUT_CONSTRAINT_END_TO_START_OF = 19;
      
      public static final int LAYOUT_CONSTRAINT_GUIDE_BEGIN = 5;
      
      public static final int LAYOUT_CONSTRAINT_GUIDE_END = 6;
      
      public static final int LAYOUT_CONSTRAINT_GUIDE_PERCENT = 7;
      
      public static final int LAYOUT_CONSTRAINT_HEIGHT_DEFAULT = 32;
      
      public static final int LAYOUT_CONSTRAINT_HEIGHT_MAX = 37;
      
      public static final int LAYOUT_CONSTRAINT_HEIGHT_MIN = 36;
      
      public static final int LAYOUT_CONSTRAINT_HEIGHT_PERCENT = 38;
      
      public static final int LAYOUT_CONSTRAINT_HORIZONTAL_BIAS = 29;
      
      public static final int LAYOUT_CONSTRAINT_HORIZONTAL_CHAINSTYLE = 47;
      
      public static final int LAYOUT_CONSTRAINT_HORIZONTAL_WEIGHT = 45;
      
      public static final int LAYOUT_CONSTRAINT_LEFT_CREATOR = 39;
      
      public static final int LAYOUT_CONSTRAINT_LEFT_TO_LEFT_OF = 8;
      
      public static final int LAYOUT_CONSTRAINT_LEFT_TO_RIGHT_OF = 9;
      
      public static final int LAYOUT_CONSTRAINT_RIGHT_CREATOR = 41;
      
      public static final int LAYOUT_CONSTRAINT_RIGHT_TO_LEFT_OF = 10;
      
      public static final int LAYOUT_CONSTRAINT_RIGHT_TO_RIGHT_OF = 11;
      
      public static final int LAYOUT_CONSTRAINT_START_TO_END_OF = 17;
      
      public static final int LAYOUT_CONSTRAINT_START_TO_START_OF = 18;
      
      public static final int LAYOUT_CONSTRAINT_TOP_CREATOR = 40;
      
      public static final int LAYOUT_CONSTRAINT_TOP_TO_BOTTOM_OF = 13;
      
      public static final int LAYOUT_CONSTRAINT_TOP_TO_TOP_OF = 12;
      
      public static final int LAYOUT_CONSTRAINT_VERTICAL_BIAS = 30;
      
      public static final int LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE = 48;
      
      public static final int LAYOUT_CONSTRAINT_VERTICAL_WEIGHT = 46;
      
      public static final int LAYOUT_CONSTRAINT_WIDTH_DEFAULT = 31;
      
      public static final int LAYOUT_CONSTRAINT_WIDTH_MAX = 34;
      
      public static final int LAYOUT_CONSTRAINT_WIDTH_MIN = 33;
      
      public static final int LAYOUT_CONSTRAINT_WIDTH_PERCENT = 35;
      
      public static final int LAYOUT_EDITOR_ABSOLUTEX = 49;
      
      public static final int LAYOUT_EDITOR_ABSOLUTEY = 50;
      
      public static final int LAYOUT_GONE_MARGIN_BOTTOM = 24;
      
      public static final int LAYOUT_GONE_MARGIN_END = 26;
      
      public static final int LAYOUT_GONE_MARGIN_LEFT = 21;
      
      public static final int LAYOUT_GONE_MARGIN_RIGHT = 23;
      
      public static final int LAYOUT_GONE_MARGIN_START = 25;
      
      public static final int LAYOUT_GONE_MARGIN_TOP = 22;
      
      public static final int UNUSED = 0;
      
      public static final SparseIntArray map = new SparseIntArray();
      
      static {
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintLeft_toLeftOf, 8);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintLeft_toRightOf, 9);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintRight_toLeftOf, 10);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintRight_toRightOf, 11);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintTop_toTopOf, 12);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintTop_toBottomOf, 13);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintBottom_toTopOf, 14);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintBottom_toBottomOf, 15);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintBaseline_toBaselineOf, 16);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintCircle, 2);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintCircleRadius, 3);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintCircleAngle, 4);
        map.append(R.styleable.ConstraintLayout_Layout_layout_editor_absoluteX, 49);
        map.append(R.styleable.ConstraintLayout_Layout_layout_editor_absoluteY, 50);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintGuide_begin, 5);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintGuide_end, 6);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintGuide_percent, 7);
        map.append(R.styleable.ConstraintLayout_Layout_android_orientation, 1);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintStart_toEndOf, 17);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintStart_toStartOf, 18);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintEnd_toStartOf, 19);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintEnd_toEndOf, 20);
        map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginLeft, 21);
        map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginTop, 22);
        map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginRight, 23);
        map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginBottom, 24);
        map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginStart, 25);
        map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginEnd, 26);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHorizontal_bias, 29);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintVertical_bias, 30);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintDimensionRatio, 44);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHorizontal_weight, 45);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintVertical_weight, 46);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHorizontal_chainStyle, 47);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintVertical_chainStyle, 48);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constrainedWidth, 27);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constrainedHeight, 28);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth_default, 31);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight_default, 32);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth_min, 33);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth_max, 34);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth_percent, 35);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight_min, 36);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight_max, 37);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight_percent, 38);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintLeft_creator, 39);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintTop_creator, 40);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintRight_creator, 41);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintBottom_creator, 42);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintBaseline_creator, 43);
      }
    }
  }
  
  private static class Table {
    public static final int ANDROID_ORIENTATION = 1;
    
    public static final int LAYOUT_CONSTRAINED_HEIGHT = 28;
    
    public static final int LAYOUT_CONSTRAINED_WIDTH = 27;
    
    public static final int LAYOUT_CONSTRAINT_BASELINE_CREATOR = 43;
    
    public static final int LAYOUT_CONSTRAINT_BASELINE_TO_BASELINE_OF = 16;
    
    public static final int LAYOUT_CONSTRAINT_BOTTOM_CREATOR = 42;
    
    public static final int LAYOUT_CONSTRAINT_BOTTOM_TO_BOTTOM_OF = 15;
    
    public static final int LAYOUT_CONSTRAINT_BOTTOM_TO_TOP_OF = 14;
    
    public static final int LAYOUT_CONSTRAINT_CIRCLE = 2;
    
    public static final int LAYOUT_CONSTRAINT_CIRCLE_ANGLE = 4;
    
    public static final int LAYOUT_CONSTRAINT_CIRCLE_RADIUS = 3;
    
    public static final int LAYOUT_CONSTRAINT_DIMENSION_RATIO = 44;
    
    public static final int LAYOUT_CONSTRAINT_END_TO_END_OF = 20;
    
    public static final int LAYOUT_CONSTRAINT_END_TO_START_OF = 19;
    
    public static final int LAYOUT_CONSTRAINT_GUIDE_BEGIN = 5;
    
    public static final int LAYOUT_CONSTRAINT_GUIDE_END = 6;
    
    public static final int LAYOUT_CONSTRAINT_GUIDE_PERCENT = 7;
    
    public static final int LAYOUT_CONSTRAINT_HEIGHT_DEFAULT = 32;
    
    public static final int LAYOUT_CONSTRAINT_HEIGHT_MAX = 37;
    
    public static final int LAYOUT_CONSTRAINT_HEIGHT_MIN = 36;
    
    public static final int LAYOUT_CONSTRAINT_HEIGHT_PERCENT = 38;
    
    public static final int LAYOUT_CONSTRAINT_HORIZONTAL_BIAS = 29;
    
    public static final int LAYOUT_CONSTRAINT_HORIZONTAL_CHAINSTYLE = 47;
    
    public static final int LAYOUT_CONSTRAINT_HORIZONTAL_WEIGHT = 45;
    
    public static final int LAYOUT_CONSTRAINT_LEFT_CREATOR = 39;
    
    public static final int LAYOUT_CONSTRAINT_LEFT_TO_LEFT_OF = 8;
    
    public static final int LAYOUT_CONSTRAINT_LEFT_TO_RIGHT_OF = 9;
    
    public static final int LAYOUT_CONSTRAINT_RIGHT_CREATOR = 41;
    
    public static final int LAYOUT_CONSTRAINT_RIGHT_TO_LEFT_OF = 10;
    
    public static final int LAYOUT_CONSTRAINT_RIGHT_TO_RIGHT_OF = 11;
    
    public static final int LAYOUT_CONSTRAINT_START_TO_END_OF = 17;
    
    public static final int LAYOUT_CONSTRAINT_START_TO_START_OF = 18;
    
    public static final int LAYOUT_CONSTRAINT_TOP_CREATOR = 40;
    
    public static final int LAYOUT_CONSTRAINT_TOP_TO_BOTTOM_OF = 13;
    
    public static final int LAYOUT_CONSTRAINT_TOP_TO_TOP_OF = 12;
    
    public static final int LAYOUT_CONSTRAINT_VERTICAL_BIAS = 30;
    
    public static final int LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE = 48;
    
    public static final int LAYOUT_CONSTRAINT_VERTICAL_WEIGHT = 46;
    
    public static final int LAYOUT_CONSTRAINT_WIDTH_DEFAULT = 31;
    
    public static final int LAYOUT_CONSTRAINT_WIDTH_MAX = 34;
    
    public static final int LAYOUT_CONSTRAINT_WIDTH_MIN = 33;
    
    public static final int LAYOUT_CONSTRAINT_WIDTH_PERCENT = 35;
    
    public static final int LAYOUT_EDITOR_ABSOLUTEX = 49;
    
    public static final int LAYOUT_EDITOR_ABSOLUTEY = 50;
    
    public static final int LAYOUT_GONE_MARGIN_BOTTOM = 24;
    
    public static final int LAYOUT_GONE_MARGIN_END = 26;
    
    public static final int LAYOUT_GONE_MARGIN_LEFT = 21;
    
    public static final int LAYOUT_GONE_MARGIN_RIGHT = 23;
    
    public static final int LAYOUT_GONE_MARGIN_START = 25;
    
    public static final int LAYOUT_GONE_MARGIN_TOP = 22;
    
    public static final int UNUSED = 0;
    
    public static final SparseIntArray map = new SparseIntArray();
    
    static {
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintLeft_toLeftOf, 8);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintLeft_toRightOf, 9);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintRight_toLeftOf, 10);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintRight_toRightOf, 11);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintTop_toTopOf, 12);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintTop_toBottomOf, 13);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintBottom_toTopOf, 14);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintBottom_toBottomOf, 15);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintBaseline_toBaselineOf, 16);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintCircle, 2);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintCircleRadius, 3);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintCircleAngle, 4);
      map.append(R.styleable.ConstraintLayout_Layout_layout_editor_absoluteX, 49);
      map.append(R.styleable.ConstraintLayout_Layout_layout_editor_absoluteY, 50);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintGuide_begin, 5);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintGuide_end, 6);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintGuide_percent, 7);
      map.append(R.styleable.ConstraintLayout_Layout_android_orientation, 1);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintStart_toEndOf, 17);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintStart_toStartOf, 18);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintEnd_toStartOf, 19);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintEnd_toEndOf, 20);
      map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginLeft, 21);
      map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginTop, 22);
      map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginRight, 23);
      map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginBottom, 24);
      map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginStart, 25);
      map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginEnd, 26);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHorizontal_bias, 29);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintVertical_bias, 30);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintDimensionRatio, 44);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHorizontal_weight, 45);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintVertical_weight, 46);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHorizontal_chainStyle, 47);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintVertical_chainStyle, 48);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constrainedWidth, 27);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constrainedHeight, 28);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth_default, 31);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight_default, 32);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth_min, 33);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth_max, 34);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth_percent, 35);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight_min, 36);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight_max, 37);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight_percent, 38);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintLeft_creator, 39);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintTop_creator, 40);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintRight_creator, 41);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintBottom_creator, 42);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintBaseline_creator, 43);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/androidx/constraintlayout/widget/ConstraintLayout.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */