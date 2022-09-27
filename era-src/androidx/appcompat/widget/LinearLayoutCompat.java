package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import androidx.appcompat.R;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class LinearLayoutCompat extends ViewGroup {
  private static final String ACCESSIBILITY_CLASS_NAME = "androidx.appcompat.widget.LinearLayoutCompat";
  
  public static final int HORIZONTAL = 0;
  
  private static final int INDEX_BOTTOM = 2;
  
  private static final int INDEX_CENTER_VERTICAL = 0;
  
  private static final int INDEX_FILL = 3;
  
  private static final int INDEX_TOP = 1;
  
  public static final int SHOW_DIVIDER_BEGINNING = 1;
  
  public static final int SHOW_DIVIDER_END = 4;
  
  public static final int SHOW_DIVIDER_MIDDLE = 2;
  
  public static final int SHOW_DIVIDER_NONE = 0;
  
  public static final int VERTICAL = 1;
  
  private static final int VERTICAL_GRAVITY_COUNT = 4;
  
  private boolean mBaselineAligned = true;
  
  private int mBaselineAlignedChildIndex = -1;
  
  private int mBaselineChildTop = 0;
  
  private Drawable mDivider;
  
  private int mDividerHeight;
  
  private int mDividerPadding;
  
  private int mDividerWidth;
  
  private int mGravity = 8388659;
  
  private int[] mMaxAscent;
  
  private int[] mMaxDescent;
  
  private int mOrientation;
  
  private int mShowDividers;
  
  private int mTotalLength;
  
  private boolean mUseLargestChild;
  
  private float mWeightSum;
  
  public LinearLayoutCompat(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public LinearLayoutCompat(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public LinearLayoutCompat(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(paramContext, paramAttributeSet, R.styleable.LinearLayoutCompat, paramInt, 0);
    paramInt = tintTypedArray.getInt(R.styleable.LinearLayoutCompat_android_orientation, -1);
    if (paramInt >= 0)
      setOrientation(paramInt); 
    paramInt = tintTypedArray.getInt(R.styleable.LinearLayoutCompat_android_gravity, -1);
    if (paramInt >= 0)
      setGravity(paramInt); 
    boolean bool = tintTypedArray.getBoolean(R.styleable.LinearLayoutCompat_android_baselineAligned, true);
    if (!bool)
      setBaselineAligned(bool); 
    this.mWeightSum = tintTypedArray.getFloat(R.styleable.LinearLayoutCompat_android_weightSum, -1.0F);
    this.mBaselineAlignedChildIndex = tintTypedArray.getInt(R.styleable.LinearLayoutCompat_android_baselineAlignedChildIndex, -1);
    this.mUseLargestChild = tintTypedArray.getBoolean(R.styleable.LinearLayoutCompat_measureWithLargestChild, false);
    setDividerDrawable(tintTypedArray.getDrawable(R.styleable.LinearLayoutCompat_divider));
    this.mShowDividers = tintTypedArray.getInt(R.styleable.LinearLayoutCompat_showDividers, 0);
    this.mDividerPadding = tintTypedArray.getDimensionPixelSize(R.styleable.LinearLayoutCompat_dividerPadding, 0);
    tintTypedArray.recycle();
  }
  
  private void forceUniformHeight(int paramInt1, int paramInt2) {
    int i = View.MeasureSpec.makeMeasureSpec(getMeasuredHeight(), 1073741824);
    for (byte b = 0; b < paramInt1; b++) {
      View view = getVirtualChildAt(b);
      if (view.getVisibility() != 8) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (layoutParams.height == -1) {
          int j = layoutParams.width;
          layoutParams.width = view.getMeasuredWidth();
          measureChildWithMargins(view, paramInt2, 0, i, 0);
          layoutParams.width = j;
        } 
      } 
    } 
  }
  
  private void forceUniformWidth(int paramInt1, int paramInt2) {
    int i = View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 1073741824);
    for (byte b = 0; b < paramInt1; b++) {
      View view = getVirtualChildAt(b);
      if (view.getVisibility() != 8) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (layoutParams.width == -1) {
          int j = layoutParams.height;
          layoutParams.height = view.getMeasuredHeight();
          measureChildWithMargins(view, i, 0, paramInt2, 0);
          layoutParams.height = j;
        } 
      } 
    } 
  }
  
  private void setChildFrame(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    paramView.layout(paramInt1, paramInt2, paramInt3 + paramInt1, paramInt4 + paramInt2);
  }
  
  protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    return paramLayoutParams instanceof LayoutParams;
  }
  
  void drawDividersHorizontal(Canvas paramCanvas) {
    int i = getVirtualChildCount();
    boolean bool = ViewUtils.isLayoutRtl((View)this);
    int j;
    for (j = 0; j < i; j++) {
      View view = getVirtualChildAt(j);
      if (view != null && view.getVisibility() != 8 && hasDividerBeforeChildAt(j)) {
        int k;
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (bool) {
          k = view.getRight() + layoutParams.rightMargin;
        } else {
          k = view.getLeft() - layoutParams.leftMargin - this.mDividerWidth;
        } 
        drawVerticalDivider(paramCanvas, k);
      } 
    } 
    if (hasDividerBeforeChildAt(i)) {
      View view = getVirtualChildAt(i - 1);
      if (view == null) {
        if (bool) {
          j = getPaddingLeft();
        } else {
          int k = getWidth() - getPaddingRight();
          j = this.mDividerWidth;
          j = k - j;
        } 
      } else {
        int k;
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (bool) {
          k = view.getLeft() - layoutParams.leftMargin;
          j = this.mDividerWidth;
        } else {
          j = view.getRight() + layoutParams.rightMargin;
          drawVerticalDivider(paramCanvas, j);
        } 
        j = k - j;
      } 
    } else {
      return;
    } 
    drawVerticalDivider(paramCanvas, j);
  }
  
  void drawDividersVertical(Canvas paramCanvas) {
    int i = getVirtualChildCount();
    int j;
    for (j = 0; j < i; j++) {
      View view = getVirtualChildAt(j);
      if (view != null && view.getVisibility() != 8 && hasDividerBeforeChildAt(j)) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        drawHorizontalDivider(paramCanvas, view.getTop() - layoutParams.topMargin - this.mDividerHeight);
      } 
    } 
    if (hasDividerBeforeChildAt(i)) {
      View view = getVirtualChildAt(i - 1);
      if (view == null) {
        j = getHeight() - getPaddingBottom() - this.mDividerHeight;
      } else {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        j = view.getBottom() + layoutParams.bottomMargin;
      } 
      drawHorizontalDivider(paramCanvas, j);
    } 
  }
  
  void drawHorizontalDivider(Canvas paramCanvas, int paramInt) {
    this.mDivider.setBounds(getPaddingLeft() + this.mDividerPadding, paramInt, getWidth() - getPaddingRight() - this.mDividerPadding, this.mDividerHeight + paramInt);
    this.mDivider.draw(paramCanvas);
  }
  
  void drawVerticalDivider(Canvas paramCanvas, int paramInt) {
    this.mDivider.setBounds(paramInt, getPaddingTop() + this.mDividerPadding, this.mDividerWidth + paramInt, getHeight() - getPaddingBottom() - this.mDividerPadding);
    this.mDivider.draw(paramCanvas);
  }
  
  protected LayoutParams generateDefaultLayoutParams() {
    int i = this.mOrientation;
    return (i == 0) ? new LayoutParams(-2, -2) : ((i == 1) ? new LayoutParams(-1, -2) : null);
  }
  
  public LayoutParams generateLayoutParams(AttributeSet paramAttributeSet) {
    return new LayoutParams(getContext(), paramAttributeSet);
  }
  
  protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    return new LayoutParams(paramLayoutParams);
  }
  
  public int getBaseline() {
    if (this.mBaselineAlignedChildIndex < 0)
      return super.getBaseline(); 
    int i = getChildCount();
    int j = this.mBaselineAlignedChildIndex;
    if (i > j) {
      View view = getChildAt(j);
      int k = view.getBaseline();
      if (k == -1) {
        if (this.mBaselineAlignedChildIndex == 0)
          return -1; 
        throw new RuntimeException("mBaselineAlignedChildIndex of LinearLayout points to a View that doesn't know how to get its baseline.");
      } 
      j = this.mBaselineChildTop;
      i = j;
      if (this.mOrientation == 1) {
        int m = this.mGravity & 0x70;
        i = j;
        if (m != 48)
          if (m != 16) {
            if (m != 80) {
              i = j;
            } else {
              i = getBottom() - getTop() - getPaddingBottom() - this.mTotalLength;
            } 
          } else {
            i = j + (getBottom() - getTop() - getPaddingTop() - getPaddingBottom() - this.mTotalLength) / 2;
          }  
      } 
      return i + ((LayoutParams)view.getLayoutParams()).topMargin + k;
    } 
    throw new RuntimeException("mBaselineAlignedChildIndex of LinearLayout set to an index that is out of bounds.");
  }
  
  public int getBaselineAlignedChildIndex() {
    return this.mBaselineAlignedChildIndex;
  }
  
  int getChildrenSkipCount(View paramView, int paramInt) {
    return 0;
  }
  
  public Drawable getDividerDrawable() {
    return this.mDivider;
  }
  
  public int getDividerPadding() {
    return this.mDividerPadding;
  }
  
  public int getDividerWidth() {
    return this.mDividerWidth;
  }
  
  public int getGravity() {
    return this.mGravity;
  }
  
  int getLocationOffset(View paramView) {
    return 0;
  }
  
  int getNextLocationOffset(View paramView) {
    return 0;
  }
  
  public int getOrientation() {
    return this.mOrientation;
  }
  
  public int getShowDividers() {
    return this.mShowDividers;
  }
  
  View getVirtualChildAt(int paramInt) {
    return getChildAt(paramInt);
  }
  
  int getVirtualChildCount() {
    return getChildCount();
  }
  
  public float getWeightSum() {
    return this.mWeightSum;
  }
  
  protected boolean hasDividerBeforeChildAt(int paramInt) {
    boolean bool1 = false;
    boolean bool2 = false;
    boolean bool3 = false;
    if (paramInt == 0) {
      if ((this.mShowDividers & 0x1) != 0)
        bool3 = true; 
      return bool3;
    } 
    if (paramInt == getChildCount()) {
      bool3 = bool1;
      if ((this.mShowDividers & 0x4) != 0)
        bool3 = true; 
      return bool3;
    } 
    bool3 = bool2;
    if ((this.mShowDividers & 0x2) != 0) {
      paramInt--;
      while (true) {
        bool3 = bool2;
        if (paramInt >= 0) {
          if (getChildAt(paramInt).getVisibility() != 8) {
            bool3 = true;
            break;
          } 
          paramInt--;
          continue;
        } 
        break;
      } 
    } 
    return bool3;
  }
  
  public boolean isBaselineAligned() {
    return this.mBaselineAligned;
  }
  
  public boolean isMeasureWithLargestChildEnabled() {
    return this.mUseLargestChild;
  }
  
  void layoutHorizontal(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    byte b1;
    byte b2;
    boolean bool1 = ViewUtils.isLayoutRtl((View)this);
    int i = getPaddingTop();
    int j = paramInt4 - paramInt2;
    int k = getPaddingBottom();
    int m = getPaddingBottom();
    int n = getVirtualChildCount();
    paramInt4 = this.mGravity;
    paramInt2 = paramInt4 & 0x70;
    boolean bool2 = this.mBaselineAligned;
    int[] arrayOfInt1 = this.mMaxAscent;
    int[] arrayOfInt2 = this.mMaxDescent;
    paramInt4 = GravityCompat.getAbsoluteGravity(0x800007 & paramInt4, ViewCompat.getLayoutDirection((View)this));
    if (paramInt4 != 1) {
      if (paramInt4 != 5) {
        paramInt1 = getPaddingLeft();
      } else {
        paramInt1 = getPaddingLeft() + paramInt3 - paramInt1 - this.mTotalLength;
      } 
    } else {
      paramInt1 = getPaddingLeft() + (paramInt3 - paramInt1 - this.mTotalLength) / 2;
    } 
    if (bool1) {
      b1 = n - 1;
      b2 = -1;
    } else {
      b1 = 0;
      b2 = 1;
    } 
    paramInt4 = 0;
    paramInt3 = i;
    while (paramInt4 < n) {
      int i1 = b1 + b2 * paramInt4;
      View view = getVirtualChildAt(i1);
      if (view == null) {
        paramInt1 += measureNullChild(i1);
      } else if (view.getVisibility() != 8) {
        int i2 = view.getMeasuredWidth();
        int i3 = view.getMeasuredHeight();
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (bool2 && layoutParams.height != -1) {
          i4 = view.getBaseline();
        } else {
          i4 = -1;
        } 
        int i5 = layoutParams.gravity;
        int i6 = i5;
        if (i5 < 0)
          i6 = paramInt2; 
        i6 &= 0x70;
        if (i6 != 16) {
          if (i6 != 48) {
            if (i6 != 80) {
              i6 = paramInt3;
            } else {
              i5 = j - k - i3 - layoutParams.bottomMargin;
              i6 = i5;
              if (i4 != -1) {
                i6 = view.getMeasuredHeight();
                i6 = i5 - arrayOfInt2[2] - i6 - i4;
              } 
            } 
          } else {
            i5 = layoutParams.topMargin + paramInt3;
            i6 = i5;
            if (i4 != -1)
              i6 = i5 + arrayOfInt1[1] - i4; 
          } 
        } else {
          i6 = (j - i - m - i3) / 2 + paramInt3 + layoutParams.topMargin - layoutParams.bottomMargin;
        } 
        int i4 = paramInt1;
        if (hasDividerBeforeChildAt(i1))
          i4 = paramInt1 + this.mDividerWidth; 
        paramInt1 = layoutParams.leftMargin + i4;
        setChildFrame(view, paramInt1 + getLocationOffset(view), i6, i2, i3);
        i6 = layoutParams.rightMargin;
        i4 = getNextLocationOffset(view);
        paramInt4 += getChildrenSkipCount(view, i1);
        paramInt1 += i2 + i6 + i4;
      } 
      paramInt4++;
    } 
  }
  
  void layoutVertical(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    int i = getPaddingLeft();
    int j = paramInt3 - paramInt1;
    int k = getPaddingRight();
    int m = getPaddingRight();
    int n = getVirtualChildCount();
    int i1 = this.mGravity;
    paramInt1 = i1 & 0x70;
    if (paramInt1 != 16) {
      if (paramInt1 != 80) {
        paramInt1 = getPaddingTop();
      } else {
        paramInt1 = getPaddingTop() + paramInt4 - paramInt2 - this.mTotalLength;
      } 
    } else {
      paramInt1 = getPaddingTop() + (paramInt4 - paramInt2 - this.mTotalLength) / 2;
    } 
    paramInt2 = 0;
    while (paramInt2 < n) {
      View view = getVirtualChildAt(paramInt2);
      if (view == null) {
        paramInt3 = paramInt1 + measureNullChild(paramInt2);
        paramInt4 = paramInt2;
      } else {
        paramInt3 = paramInt1;
        paramInt4 = paramInt2;
        if (view.getVisibility() != 8) {
          int i2 = view.getMeasuredWidth();
          int i3 = view.getMeasuredHeight();
          LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
          paramInt4 = layoutParams.gravity;
          paramInt3 = paramInt4;
          if (paramInt4 < 0)
            paramInt3 = i1 & 0x800007; 
          paramInt3 = GravityCompat.getAbsoluteGravity(paramInt3, ViewCompat.getLayoutDirection((View)this)) & 0x7;
          if (paramInt3 != 1) {
            if (paramInt3 != 5) {
              paramInt3 = layoutParams.leftMargin + i;
            } else {
              paramInt4 = j - k - i2;
              paramInt3 = layoutParams.rightMargin;
              paramInt3 = paramInt4 - paramInt3;
            } 
          } else {
            paramInt4 = (j - i - m - i2) / 2 + i + layoutParams.leftMargin;
            paramInt3 = layoutParams.rightMargin;
            paramInt3 = paramInt4 - paramInt3;
          } 
          paramInt4 = paramInt1;
          if (hasDividerBeforeChildAt(paramInt2))
            paramInt4 = paramInt1 + this.mDividerHeight; 
          paramInt1 = paramInt4 + layoutParams.topMargin;
          setChildFrame(view, paramInt3, paramInt1 + getLocationOffset(view), i2, i3);
          i2 = layoutParams.bottomMargin;
          paramInt3 = getNextLocationOffset(view);
          paramInt4 = paramInt2 + getChildrenSkipCount(view, paramInt2);
          paramInt3 = paramInt1 + i3 + i2 + paramInt3;
        } 
      } 
      paramInt2 = paramInt4 + 1;
      paramInt1 = paramInt3;
    } 
  }
  
  void measureChildBeforeLayout(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
    measureChildWithMargins(paramView, paramInt2, paramInt3, paramInt4, paramInt5);
  }
  
  void measureHorizontal(int paramInt1, int paramInt2) {
    // Byte code:
    //   0: aload_0
    //   1: iconst_0
    //   2: putfield mTotalLength : I
    //   5: aload_0
    //   6: invokevirtual getVirtualChildCount : ()I
    //   9: istore_3
    //   10: iload_1
    //   11: invokestatic getMode : (I)I
    //   14: istore #4
    //   16: iload_2
    //   17: invokestatic getMode : (I)I
    //   20: istore #5
    //   22: aload_0
    //   23: getfield mMaxAscent : [I
    //   26: ifnull -> 36
    //   29: aload_0
    //   30: getfield mMaxDescent : [I
    //   33: ifnonnull -> 50
    //   36: aload_0
    //   37: iconst_4
    //   38: newarray int
    //   40: putfield mMaxAscent : [I
    //   43: aload_0
    //   44: iconst_4
    //   45: newarray int
    //   47: putfield mMaxDescent : [I
    //   50: aload_0
    //   51: getfield mMaxAscent : [I
    //   54: astore #6
    //   56: aload_0
    //   57: getfield mMaxDescent : [I
    //   60: astore #7
    //   62: aload #6
    //   64: iconst_3
    //   65: iconst_m1
    //   66: iastore
    //   67: aload #6
    //   69: iconst_2
    //   70: iconst_m1
    //   71: iastore
    //   72: aload #6
    //   74: iconst_1
    //   75: iconst_m1
    //   76: iastore
    //   77: aload #6
    //   79: iconst_0
    //   80: iconst_m1
    //   81: iastore
    //   82: aload #7
    //   84: iconst_3
    //   85: iconst_m1
    //   86: iastore
    //   87: aload #7
    //   89: iconst_2
    //   90: iconst_m1
    //   91: iastore
    //   92: aload #7
    //   94: iconst_1
    //   95: iconst_m1
    //   96: iastore
    //   97: aload #7
    //   99: iconst_0
    //   100: iconst_m1
    //   101: iastore
    //   102: aload_0
    //   103: getfield mBaselineAligned : Z
    //   106: istore #8
    //   108: aload_0
    //   109: getfield mUseLargestChild : Z
    //   112: istore #9
    //   114: iload #4
    //   116: ldc 1073741824
    //   118: if_icmpne -> 127
    //   121: iconst_1
    //   122: istore #10
    //   124: goto -> 130
    //   127: iconst_0
    //   128: istore #10
    //   130: fconst_0
    //   131: fstore #11
    //   133: iconst_0
    //   134: istore #12
    //   136: iconst_0
    //   137: istore #13
    //   139: iconst_0
    //   140: istore #14
    //   142: iconst_0
    //   143: istore #15
    //   145: iconst_0
    //   146: istore #16
    //   148: iconst_0
    //   149: istore #17
    //   151: iconst_0
    //   152: istore #18
    //   154: iconst_1
    //   155: istore #19
    //   157: iconst_0
    //   158: istore #20
    //   160: iload #12
    //   162: iload_3
    //   163: if_icmpge -> 849
    //   166: aload_0
    //   167: iload #12
    //   169: invokevirtual getVirtualChildAt : (I)Landroid/view/View;
    //   172: astore #21
    //   174: aload #21
    //   176: ifnonnull -> 197
    //   179: aload_0
    //   180: aload_0
    //   181: getfield mTotalLength : I
    //   184: aload_0
    //   185: iload #12
    //   187: invokevirtual measureNullChild : (I)I
    //   190: iadd
    //   191: putfield mTotalLength : I
    //   194: goto -> 843
    //   197: aload #21
    //   199: invokevirtual getVisibility : ()I
    //   202: bipush #8
    //   204: if_icmpne -> 223
    //   207: iload #12
    //   209: aload_0
    //   210: aload #21
    //   212: iload #12
    //   214: invokevirtual getChildrenSkipCount : (Landroid/view/View;I)I
    //   217: iadd
    //   218: istore #12
    //   220: goto -> 194
    //   223: aload_0
    //   224: iload #12
    //   226: invokevirtual hasDividerBeforeChildAt : (I)Z
    //   229: ifeq -> 245
    //   232: aload_0
    //   233: aload_0
    //   234: getfield mTotalLength : I
    //   237: aload_0
    //   238: getfield mDividerWidth : I
    //   241: iadd
    //   242: putfield mTotalLength : I
    //   245: aload #21
    //   247: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   250: checkcast androidx/appcompat/widget/LinearLayoutCompat$LayoutParams
    //   253: astore #22
    //   255: fload #11
    //   257: aload #22
    //   259: getfield weight : F
    //   262: fadd
    //   263: fstore #11
    //   265: iload #4
    //   267: ldc 1073741824
    //   269: if_icmpne -> 381
    //   272: aload #22
    //   274: getfield width : I
    //   277: ifne -> 381
    //   280: aload #22
    //   282: getfield weight : F
    //   285: fconst_0
    //   286: fcmpl
    //   287: ifle -> 381
    //   290: iload #10
    //   292: ifeq -> 318
    //   295: aload_0
    //   296: aload_0
    //   297: getfield mTotalLength : I
    //   300: aload #22
    //   302: getfield leftMargin : I
    //   305: aload #22
    //   307: getfield rightMargin : I
    //   310: iadd
    //   311: iadd
    //   312: putfield mTotalLength : I
    //   315: goto -> 347
    //   318: aload_0
    //   319: getfield mTotalLength : I
    //   322: istore #23
    //   324: aload_0
    //   325: iload #23
    //   327: aload #22
    //   329: getfield leftMargin : I
    //   332: iload #23
    //   334: iadd
    //   335: aload #22
    //   337: getfield rightMargin : I
    //   340: iadd
    //   341: invokestatic max : (II)I
    //   344: putfield mTotalLength : I
    //   347: iload #8
    //   349: ifeq -> 375
    //   352: iconst_0
    //   353: iconst_0
    //   354: invokestatic makeMeasureSpec : (II)I
    //   357: istore #23
    //   359: aload #21
    //   361: iload #23
    //   363: iload #23
    //   365: invokevirtual measure : (II)V
    //   368: iload #13
    //   370: istore #23
    //   372: goto -> 566
    //   375: iconst_1
    //   376: istore #17
    //   378: goto -> 570
    //   381: aload #22
    //   383: getfield width : I
    //   386: ifne -> 412
    //   389: aload #22
    //   391: getfield weight : F
    //   394: fconst_0
    //   395: fcmpl
    //   396: ifle -> 412
    //   399: aload #22
    //   401: bipush #-2
    //   403: putfield width : I
    //   406: iconst_0
    //   407: istore #23
    //   409: goto -> 417
    //   412: ldc_w -2147483648
    //   415: istore #23
    //   417: fload #11
    //   419: fconst_0
    //   420: fcmpl
    //   421: ifne -> 433
    //   424: aload_0
    //   425: getfield mTotalLength : I
    //   428: istore #24
    //   430: goto -> 436
    //   433: iconst_0
    //   434: istore #24
    //   436: aload_0
    //   437: aload #21
    //   439: iload #12
    //   441: iload_1
    //   442: iload #24
    //   444: iload_2
    //   445: iconst_0
    //   446: invokevirtual measureChildBeforeLayout : (Landroid/view/View;IIIII)V
    //   449: iload #23
    //   451: ldc_w -2147483648
    //   454: if_icmpeq -> 464
    //   457: aload #22
    //   459: iload #23
    //   461: putfield width : I
    //   464: aload #21
    //   466: invokevirtual getMeasuredWidth : ()I
    //   469: istore #24
    //   471: iload #10
    //   473: ifeq -> 509
    //   476: aload_0
    //   477: aload_0
    //   478: getfield mTotalLength : I
    //   481: aload #22
    //   483: getfield leftMargin : I
    //   486: iload #24
    //   488: iadd
    //   489: aload #22
    //   491: getfield rightMargin : I
    //   494: iadd
    //   495: aload_0
    //   496: aload #21
    //   498: invokevirtual getNextLocationOffset : (Landroid/view/View;)I
    //   501: iadd
    //   502: iadd
    //   503: putfield mTotalLength : I
    //   506: goto -> 548
    //   509: aload_0
    //   510: getfield mTotalLength : I
    //   513: istore #23
    //   515: aload_0
    //   516: iload #23
    //   518: iload #23
    //   520: iload #24
    //   522: iadd
    //   523: aload #22
    //   525: getfield leftMargin : I
    //   528: iadd
    //   529: aload #22
    //   531: getfield rightMargin : I
    //   534: iadd
    //   535: aload_0
    //   536: aload #21
    //   538: invokevirtual getNextLocationOffset : (Landroid/view/View;)I
    //   541: iadd
    //   542: invokestatic max : (II)I
    //   545: putfield mTotalLength : I
    //   548: iload #13
    //   550: istore #23
    //   552: iload #9
    //   554: ifeq -> 566
    //   557: iload #24
    //   559: iload #13
    //   561: invokestatic max : (II)I
    //   564: istore #23
    //   566: iload #23
    //   568: istore #13
    //   570: iload #12
    //   572: istore #25
    //   574: iload #5
    //   576: ldc 1073741824
    //   578: if_icmpeq -> 599
    //   581: aload #22
    //   583: getfield height : I
    //   586: iconst_m1
    //   587: if_icmpne -> 599
    //   590: iconst_1
    //   591: istore #12
    //   593: iconst_1
    //   594: istore #20
    //   596: goto -> 602
    //   599: iconst_0
    //   600: istore #12
    //   602: aload #22
    //   604: getfield topMargin : I
    //   607: aload #22
    //   609: getfield bottomMargin : I
    //   612: iadd
    //   613: istore #23
    //   615: aload #21
    //   617: invokevirtual getMeasuredHeight : ()I
    //   620: iload #23
    //   622: iadd
    //   623: istore #24
    //   625: iload #18
    //   627: aload #21
    //   629: invokevirtual getMeasuredState : ()I
    //   632: invokestatic combineMeasuredStates : (II)I
    //   635: istore #26
    //   637: iload #8
    //   639: ifeq -> 726
    //   642: aload #21
    //   644: invokevirtual getBaseline : ()I
    //   647: istore #27
    //   649: iload #27
    //   651: iconst_m1
    //   652: if_icmpeq -> 726
    //   655: aload #22
    //   657: getfield gravity : I
    //   660: ifge -> 672
    //   663: aload_0
    //   664: getfield mGravity : I
    //   667: istore #18
    //   669: goto -> 679
    //   672: aload #22
    //   674: getfield gravity : I
    //   677: istore #18
    //   679: iload #18
    //   681: bipush #112
    //   683: iand
    //   684: iconst_4
    //   685: ishr
    //   686: bipush #-2
    //   688: iand
    //   689: iconst_1
    //   690: ishr
    //   691: istore #18
    //   693: aload #6
    //   695: iload #18
    //   697: aload #6
    //   699: iload #18
    //   701: iaload
    //   702: iload #27
    //   704: invokestatic max : (II)I
    //   707: iastore
    //   708: aload #7
    //   710: iload #18
    //   712: aload #7
    //   714: iload #18
    //   716: iaload
    //   717: iload #24
    //   719: iload #27
    //   721: isub
    //   722: invokestatic max : (II)I
    //   725: iastore
    //   726: iload #14
    //   728: iload #24
    //   730: invokestatic max : (II)I
    //   733: istore #14
    //   735: iload #19
    //   737: ifeq -> 755
    //   740: aload #22
    //   742: getfield height : I
    //   745: iconst_m1
    //   746: if_icmpne -> 755
    //   749: iconst_1
    //   750: istore #19
    //   752: goto -> 758
    //   755: iconst_0
    //   756: istore #19
    //   758: aload #22
    //   760: getfield weight : F
    //   763: fconst_0
    //   764: fcmpl
    //   765: ifle -> 792
    //   768: iload #12
    //   770: ifeq -> 776
    //   773: goto -> 780
    //   776: iload #24
    //   778: istore #23
    //   780: iload #16
    //   782: iload #23
    //   784: invokestatic max : (II)I
    //   787: istore #12
    //   789: goto -> 814
    //   792: iload #12
    //   794: ifeq -> 801
    //   797: iload #23
    //   799: istore #24
    //   801: iload #15
    //   803: iload #24
    //   805: invokestatic max : (II)I
    //   808: istore #15
    //   810: iload #16
    //   812: istore #12
    //   814: aload_0
    //   815: aload #21
    //   817: iload #25
    //   819: invokevirtual getChildrenSkipCount : (Landroid/view/View;I)I
    //   822: istore #16
    //   824: iload #26
    //   826: istore #18
    //   828: iload #16
    //   830: iload #25
    //   832: iadd
    //   833: istore #23
    //   835: iload #12
    //   837: istore #16
    //   839: iload #23
    //   841: istore #12
    //   843: iinc #12, 1
    //   846: goto -> 160
    //   849: aload_0
    //   850: getfield mTotalLength : I
    //   853: ifle -> 877
    //   856: aload_0
    //   857: iload_3
    //   858: invokevirtual hasDividerBeforeChildAt : (I)Z
    //   861: ifeq -> 877
    //   864: aload_0
    //   865: aload_0
    //   866: getfield mTotalLength : I
    //   869: aload_0
    //   870: getfield mDividerWidth : I
    //   873: iadd
    //   874: putfield mTotalLength : I
    //   877: aload #6
    //   879: iconst_1
    //   880: iaload
    //   881: iconst_m1
    //   882: if_icmpne -> 915
    //   885: aload #6
    //   887: iconst_0
    //   888: iaload
    //   889: iconst_m1
    //   890: if_icmpne -> 915
    //   893: aload #6
    //   895: iconst_2
    //   896: iaload
    //   897: iconst_m1
    //   898: if_icmpne -> 915
    //   901: aload #6
    //   903: iconst_3
    //   904: iaload
    //   905: iconst_m1
    //   906: if_icmpeq -> 912
    //   909: goto -> 915
    //   912: goto -> 973
    //   915: iload #14
    //   917: aload #6
    //   919: iconst_3
    //   920: iaload
    //   921: aload #6
    //   923: iconst_0
    //   924: iaload
    //   925: aload #6
    //   927: iconst_1
    //   928: iaload
    //   929: aload #6
    //   931: iconst_2
    //   932: iaload
    //   933: invokestatic max : (II)I
    //   936: invokestatic max : (II)I
    //   939: invokestatic max : (II)I
    //   942: aload #7
    //   944: iconst_3
    //   945: iaload
    //   946: aload #7
    //   948: iconst_0
    //   949: iaload
    //   950: aload #7
    //   952: iconst_1
    //   953: iaload
    //   954: aload #7
    //   956: iconst_2
    //   957: iaload
    //   958: invokestatic max : (II)I
    //   961: invokestatic max : (II)I
    //   964: invokestatic max : (II)I
    //   967: iadd
    //   968: invokestatic max : (II)I
    //   971: istore #14
    //   973: iload #14
    //   975: istore #23
    //   977: iload #9
    //   979: ifeq -> 1167
    //   982: iload #4
    //   984: ldc_w -2147483648
    //   987: if_icmpeq -> 999
    //   990: iload #14
    //   992: istore #23
    //   994: iload #4
    //   996: ifne -> 1167
    //   999: aload_0
    //   1000: iconst_0
    //   1001: putfield mTotalLength : I
    //   1004: iconst_0
    //   1005: istore #12
    //   1007: iload #14
    //   1009: istore #23
    //   1011: iload #12
    //   1013: iload_3
    //   1014: if_icmpge -> 1167
    //   1017: aload_0
    //   1018: iload #12
    //   1020: invokevirtual getVirtualChildAt : (I)Landroid/view/View;
    //   1023: astore #21
    //   1025: aload #21
    //   1027: ifnonnull -> 1048
    //   1030: aload_0
    //   1031: aload_0
    //   1032: getfield mTotalLength : I
    //   1035: aload_0
    //   1036: iload #12
    //   1038: invokevirtual measureNullChild : (I)I
    //   1041: iadd
    //   1042: putfield mTotalLength : I
    //   1045: goto -> 1071
    //   1048: aload #21
    //   1050: invokevirtual getVisibility : ()I
    //   1053: bipush #8
    //   1055: if_icmpne -> 1074
    //   1058: iload #12
    //   1060: aload_0
    //   1061: aload #21
    //   1063: iload #12
    //   1065: invokevirtual getChildrenSkipCount : (Landroid/view/View;I)I
    //   1068: iadd
    //   1069: istore #12
    //   1071: goto -> 1161
    //   1074: aload #21
    //   1076: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   1079: checkcast androidx/appcompat/widget/LinearLayoutCompat$LayoutParams
    //   1082: astore #22
    //   1084: iload #10
    //   1086: ifeq -> 1122
    //   1089: aload_0
    //   1090: aload_0
    //   1091: getfield mTotalLength : I
    //   1094: aload #22
    //   1096: getfield leftMargin : I
    //   1099: iload #13
    //   1101: iadd
    //   1102: aload #22
    //   1104: getfield rightMargin : I
    //   1107: iadd
    //   1108: aload_0
    //   1109: aload #21
    //   1111: invokevirtual getNextLocationOffset : (Landroid/view/View;)I
    //   1114: iadd
    //   1115: iadd
    //   1116: putfield mTotalLength : I
    //   1119: goto -> 1071
    //   1122: aload_0
    //   1123: getfield mTotalLength : I
    //   1126: istore #23
    //   1128: aload_0
    //   1129: iload #23
    //   1131: iload #23
    //   1133: iload #13
    //   1135: iadd
    //   1136: aload #22
    //   1138: getfield leftMargin : I
    //   1141: iadd
    //   1142: aload #22
    //   1144: getfield rightMargin : I
    //   1147: iadd
    //   1148: aload_0
    //   1149: aload #21
    //   1151: invokevirtual getNextLocationOffset : (Landroid/view/View;)I
    //   1154: iadd
    //   1155: invokestatic max : (II)I
    //   1158: putfield mTotalLength : I
    //   1161: iinc #12, 1
    //   1164: goto -> 1007
    //   1167: aload_0
    //   1168: aload_0
    //   1169: getfield mTotalLength : I
    //   1172: aload_0
    //   1173: invokevirtual getPaddingLeft : ()I
    //   1176: aload_0
    //   1177: invokevirtual getPaddingRight : ()I
    //   1180: iadd
    //   1181: iadd
    //   1182: putfield mTotalLength : I
    //   1185: aload_0
    //   1186: getfield mTotalLength : I
    //   1189: aload_0
    //   1190: invokevirtual getSuggestedMinimumWidth : ()I
    //   1193: invokestatic max : (II)I
    //   1196: iload_1
    //   1197: iconst_0
    //   1198: invokestatic resolveSizeAndState : (III)I
    //   1201: istore #25
    //   1203: ldc_w 16777215
    //   1206: iload #25
    //   1208: iand
    //   1209: aload_0
    //   1210: getfield mTotalLength : I
    //   1213: isub
    //   1214: istore #24
    //   1216: iload #17
    //   1218: ifne -> 1350
    //   1221: iload #24
    //   1223: ifeq -> 1236
    //   1226: fload #11
    //   1228: fconst_0
    //   1229: fcmpl
    //   1230: ifle -> 1236
    //   1233: goto -> 1350
    //   1236: iload #15
    //   1238: iload #16
    //   1240: invokestatic max : (II)I
    //   1243: istore #14
    //   1245: iload #9
    //   1247: ifeq -> 1336
    //   1250: iload #4
    //   1252: ldc 1073741824
    //   1254: if_icmpeq -> 1336
    //   1257: iconst_0
    //   1258: istore #15
    //   1260: iload #15
    //   1262: iload_3
    //   1263: if_icmpge -> 1336
    //   1266: aload_0
    //   1267: iload #15
    //   1269: invokevirtual getVirtualChildAt : (I)Landroid/view/View;
    //   1272: astore #7
    //   1274: aload #7
    //   1276: ifnull -> 1330
    //   1279: aload #7
    //   1281: invokevirtual getVisibility : ()I
    //   1284: bipush #8
    //   1286: if_icmpne -> 1292
    //   1289: goto -> 1330
    //   1292: aload #7
    //   1294: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   1297: checkcast androidx/appcompat/widget/LinearLayoutCompat$LayoutParams
    //   1300: getfield weight : F
    //   1303: fconst_0
    //   1304: fcmpl
    //   1305: ifle -> 1330
    //   1308: aload #7
    //   1310: iload #13
    //   1312: ldc 1073741824
    //   1314: invokestatic makeMeasureSpec : (II)I
    //   1317: aload #7
    //   1319: invokevirtual getMeasuredHeight : ()I
    //   1322: ldc 1073741824
    //   1324: invokestatic makeMeasureSpec : (II)I
    //   1327: invokevirtual measure : (II)V
    //   1330: iinc #15, 1
    //   1333: goto -> 1260
    //   1336: iload_3
    //   1337: istore #12
    //   1339: iload #23
    //   1341: istore #15
    //   1343: iload #14
    //   1345: istore #13
    //   1347: goto -> 2085
    //   1350: aload_0
    //   1351: getfield mWeightSum : F
    //   1354: fstore #28
    //   1356: fload #28
    //   1358: fconst_0
    //   1359: fcmpl
    //   1360: ifle -> 1367
    //   1363: fload #28
    //   1365: fstore #11
    //   1367: aload #6
    //   1369: iconst_3
    //   1370: iconst_m1
    //   1371: iastore
    //   1372: aload #6
    //   1374: iconst_2
    //   1375: iconst_m1
    //   1376: iastore
    //   1377: aload #6
    //   1379: iconst_1
    //   1380: iconst_m1
    //   1381: iastore
    //   1382: aload #6
    //   1384: iconst_0
    //   1385: iconst_m1
    //   1386: iastore
    //   1387: aload #7
    //   1389: iconst_3
    //   1390: iconst_m1
    //   1391: iastore
    //   1392: aload #7
    //   1394: iconst_2
    //   1395: iconst_m1
    //   1396: iastore
    //   1397: aload #7
    //   1399: iconst_1
    //   1400: iconst_m1
    //   1401: iastore
    //   1402: aload #7
    //   1404: iconst_0
    //   1405: iconst_m1
    //   1406: iastore
    //   1407: aload_0
    //   1408: iconst_0
    //   1409: putfield mTotalLength : I
    //   1412: iconst_m1
    //   1413: istore #16
    //   1415: iconst_0
    //   1416: istore #17
    //   1418: iload #19
    //   1420: istore #12
    //   1422: iload_3
    //   1423: istore #13
    //   1425: iload #15
    //   1427: istore #14
    //   1429: iload #18
    //   1431: istore #19
    //   1433: iload #24
    //   1435: istore #15
    //   1437: iload #17
    //   1439: istore #18
    //   1441: iload #18
    //   1443: iload #13
    //   1445: if_icmpge -> 1951
    //   1448: aload_0
    //   1449: iload #18
    //   1451: invokevirtual getVirtualChildAt : (I)Landroid/view/View;
    //   1454: astore #21
    //   1456: aload #21
    //   1458: ifnull -> 1945
    //   1461: aload #21
    //   1463: invokevirtual getVisibility : ()I
    //   1466: bipush #8
    //   1468: if_icmpne -> 1474
    //   1471: goto -> 1945
    //   1474: aload #21
    //   1476: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   1479: checkcast androidx/appcompat/widget/LinearLayoutCompat$LayoutParams
    //   1482: astore #22
    //   1484: aload #22
    //   1486: getfield weight : F
    //   1489: fstore #28
    //   1491: fload #28
    //   1493: fconst_0
    //   1494: fcmpl
    //   1495: ifle -> 1658
    //   1498: iload #15
    //   1500: i2f
    //   1501: fload #28
    //   1503: fmul
    //   1504: fload #11
    //   1506: fdiv
    //   1507: f2i
    //   1508: istore #23
    //   1510: iload_2
    //   1511: aload_0
    //   1512: invokevirtual getPaddingTop : ()I
    //   1515: aload_0
    //   1516: invokevirtual getPaddingBottom : ()I
    //   1519: iadd
    //   1520: aload #22
    //   1522: getfield topMargin : I
    //   1525: iadd
    //   1526: aload #22
    //   1528: getfield bottomMargin : I
    //   1531: iadd
    //   1532: aload #22
    //   1534: getfield height : I
    //   1537: invokestatic getChildMeasureSpec : (III)I
    //   1540: istore #24
    //   1542: aload #22
    //   1544: getfield width : I
    //   1547: ifne -> 1592
    //   1550: iload #4
    //   1552: ldc 1073741824
    //   1554: if_icmpeq -> 1560
    //   1557: goto -> 1592
    //   1560: iload #23
    //   1562: ifle -> 1572
    //   1565: iload #23
    //   1567: istore #17
    //   1569: goto -> 1575
    //   1572: iconst_0
    //   1573: istore #17
    //   1575: aload #21
    //   1577: iload #17
    //   1579: ldc 1073741824
    //   1581: invokestatic makeMeasureSpec : (II)I
    //   1584: iload #24
    //   1586: invokevirtual measure : (II)V
    //   1589: goto -> 1625
    //   1592: aload #21
    //   1594: invokevirtual getMeasuredWidth : ()I
    //   1597: iload #23
    //   1599: iadd
    //   1600: istore_3
    //   1601: iload_3
    //   1602: istore #17
    //   1604: iload_3
    //   1605: ifge -> 1611
    //   1608: iconst_0
    //   1609: istore #17
    //   1611: aload #21
    //   1613: iload #17
    //   1615: ldc 1073741824
    //   1617: invokestatic makeMeasureSpec : (II)I
    //   1620: iload #24
    //   1622: invokevirtual measure : (II)V
    //   1625: iload #19
    //   1627: aload #21
    //   1629: invokevirtual getMeasuredState : ()I
    //   1632: ldc_w -16777216
    //   1635: iand
    //   1636: invokestatic combineMeasuredStates : (II)I
    //   1639: istore #19
    //   1641: fload #11
    //   1643: fload #28
    //   1645: fsub
    //   1646: fstore #11
    //   1648: iload #15
    //   1650: iload #23
    //   1652: isub
    //   1653: istore #15
    //   1655: goto -> 1658
    //   1658: iload #10
    //   1660: ifeq -> 1699
    //   1663: aload_0
    //   1664: aload_0
    //   1665: getfield mTotalLength : I
    //   1668: aload #21
    //   1670: invokevirtual getMeasuredWidth : ()I
    //   1673: aload #22
    //   1675: getfield leftMargin : I
    //   1678: iadd
    //   1679: aload #22
    //   1681: getfield rightMargin : I
    //   1684: iadd
    //   1685: aload_0
    //   1686: aload #21
    //   1688: invokevirtual getNextLocationOffset : (Landroid/view/View;)I
    //   1691: iadd
    //   1692: iadd
    //   1693: putfield mTotalLength : I
    //   1696: goto -> 1741
    //   1699: aload_0
    //   1700: getfield mTotalLength : I
    //   1703: istore #17
    //   1705: aload_0
    //   1706: iload #17
    //   1708: aload #21
    //   1710: invokevirtual getMeasuredWidth : ()I
    //   1713: iload #17
    //   1715: iadd
    //   1716: aload #22
    //   1718: getfield leftMargin : I
    //   1721: iadd
    //   1722: aload #22
    //   1724: getfield rightMargin : I
    //   1727: iadd
    //   1728: aload_0
    //   1729: aload #21
    //   1731: invokevirtual getNextLocationOffset : (Landroid/view/View;)I
    //   1734: iadd
    //   1735: invokestatic max : (II)I
    //   1738: putfield mTotalLength : I
    //   1741: iload #5
    //   1743: ldc 1073741824
    //   1745: if_icmpeq -> 1763
    //   1748: aload #22
    //   1750: getfield height : I
    //   1753: iconst_m1
    //   1754: if_icmpne -> 1763
    //   1757: iconst_1
    //   1758: istore #17
    //   1760: goto -> 1766
    //   1763: iconst_0
    //   1764: istore #17
    //   1766: aload #22
    //   1768: getfield topMargin : I
    //   1771: aload #22
    //   1773: getfield bottomMargin : I
    //   1776: iadd
    //   1777: istore #24
    //   1779: aload #21
    //   1781: invokevirtual getMeasuredHeight : ()I
    //   1784: iload #24
    //   1786: iadd
    //   1787: istore_3
    //   1788: iload #16
    //   1790: iload_3
    //   1791: invokestatic max : (II)I
    //   1794: istore #23
    //   1796: iload #17
    //   1798: ifeq -> 1808
    //   1801: iload #24
    //   1803: istore #16
    //   1805: goto -> 1811
    //   1808: iload_3
    //   1809: istore #16
    //   1811: iload #14
    //   1813: iload #16
    //   1815: invokestatic max : (II)I
    //   1818: istore #16
    //   1820: iload #12
    //   1822: ifeq -> 1840
    //   1825: aload #22
    //   1827: getfield height : I
    //   1830: iconst_m1
    //   1831: if_icmpne -> 1840
    //   1834: iconst_1
    //   1835: istore #12
    //   1837: goto -> 1843
    //   1840: iconst_0
    //   1841: istore #12
    //   1843: iload #8
    //   1845: ifeq -> 1934
    //   1848: aload #21
    //   1850: invokevirtual getBaseline : ()I
    //   1853: istore #17
    //   1855: iload #17
    //   1857: iconst_m1
    //   1858: if_icmpeq -> 1934
    //   1861: aload #22
    //   1863: getfield gravity : I
    //   1866: ifge -> 1878
    //   1869: aload_0
    //   1870: getfield mGravity : I
    //   1873: istore #14
    //   1875: goto -> 1885
    //   1878: aload #22
    //   1880: getfield gravity : I
    //   1883: istore #14
    //   1885: iload #14
    //   1887: bipush #112
    //   1889: iand
    //   1890: iconst_4
    //   1891: ishr
    //   1892: bipush #-2
    //   1894: iand
    //   1895: iconst_1
    //   1896: ishr
    //   1897: istore #14
    //   1899: aload #6
    //   1901: iload #14
    //   1903: aload #6
    //   1905: iload #14
    //   1907: iaload
    //   1908: iload #17
    //   1910: invokestatic max : (II)I
    //   1913: iastore
    //   1914: aload #7
    //   1916: iload #14
    //   1918: aload #7
    //   1920: iload #14
    //   1922: iaload
    //   1923: iload_3
    //   1924: iload #17
    //   1926: isub
    //   1927: invokestatic max : (II)I
    //   1930: iastore
    //   1931: goto -> 1934
    //   1934: iload #16
    //   1936: istore #14
    //   1938: iload #23
    //   1940: istore #16
    //   1942: goto -> 1945
    //   1945: iinc #18, 1
    //   1948: goto -> 1441
    //   1951: aload_0
    //   1952: aload_0
    //   1953: getfield mTotalLength : I
    //   1956: aload_0
    //   1957: invokevirtual getPaddingLeft : ()I
    //   1960: aload_0
    //   1961: invokevirtual getPaddingRight : ()I
    //   1964: iadd
    //   1965: iadd
    //   1966: putfield mTotalLength : I
    //   1969: aload #6
    //   1971: iconst_1
    //   1972: iaload
    //   1973: iconst_m1
    //   1974: if_icmpne -> 2011
    //   1977: aload #6
    //   1979: iconst_0
    //   1980: iaload
    //   1981: iconst_m1
    //   1982: if_icmpne -> 2011
    //   1985: aload #6
    //   1987: iconst_2
    //   1988: iaload
    //   1989: iconst_m1
    //   1990: if_icmpne -> 2011
    //   1993: aload #6
    //   1995: iconst_3
    //   1996: iaload
    //   1997: iconst_m1
    //   1998: if_icmpeq -> 2004
    //   2001: goto -> 2011
    //   2004: iload #16
    //   2006: istore #15
    //   2008: goto -> 2069
    //   2011: iload #16
    //   2013: aload #6
    //   2015: iconst_3
    //   2016: iaload
    //   2017: aload #6
    //   2019: iconst_0
    //   2020: iaload
    //   2021: aload #6
    //   2023: iconst_1
    //   2024: iaload
    //   2025: aload #6
    //   2027: iconst_2
    //   2028: iaload
    //   2029: invokestatic max : (II)I
    //   2032: invokestatic max : (II)I
    //   2035: invokestatic max : (II)I
    //   2038: aload #7
    //   2040: iconst_3
    //   2041: iaload
    //   2042: aload #7
    //   2044: iconst_0
    //   2045: iaload
    //   2046: aload #7
    //   2048: iconst_1
    //   2049: iaload
    //   2050: aload #7
    //   2052: iconst_2
    //   2053: iaload
    //   2054: invokestatic max : (II)I
    //   2057: invokestatic max : (II)I
    //   2060: invokestatic max : (II)I
    //   2063: iadd
    //   2064: invokestatic max : (II)I
    //   2067: istore #15
    //   2069: iload #19
    //   2071: istore #18
    //   2073: iload #12
    //   2075: istore #19
    //   2077: iload #13
    //   2079: istore #12
    //   2081: iload #14
    //   2083: istore #13
    //   2085: iload #19
    //   2087: ifne -> 2100
    //   2090: iload #5
    //   2092: ldc 1073741824
    //   2094: if_icmpeq -> 2100
    //   2097: goto -> 2104
    //   2100: iload #15
    //   2102: istore #13
    //   2104: aload_0
    //   2105: iload #25
    //   2107: iload #18
    //   2109: ldc_w -16777216
    //   2112: iand
    //   2113: ior
    //   2114: iload #13
    //   2116: aload_0
    //   2117: invokevirtual getPaddingTop : ()I
    //   2120: aload_0
    //   2121: invokevirtual getPaddingBottom : ()I
    //   2124: iadd
    //   2125: iadd
    //   2126: aload_0
    //   2127: invokevirtual getSuggestedMinimumHeight : ()I
    //   2130: invokestatic max : (II)I
    //   2133: iload_2
    //   2134: iload #18
    //   2136: bipush #16
    //   2138: ishl
    //   2139: invokestatic resolveSizeAndState : (III)I
    //   2142: invokevirtual setMeasuredDimension : (II)V
    //   2145: iload #20
    //   2147: ifeq -> 2157
    //   2150: aload_0
    //   2151: iload #12
    //   2153: iload_1
    //   2154: invokespecial forceUniformHeight : (II)V
    //   2157: return
  }
  
  int measureNullChild(int paramInt) {
    return 0;
  }
  
  void measureVertical(int paramInt1, int paramInt2) {
    this.mTotalLength = 0;
    int i = getVirtualChildCount();
    int j = View.MeasureSpec.getMode(paramInt1);
    int k = View.MeasureSpec.getMode(paramInt2);
    int m = this.mBaselineAlignedChildIndex;
    boolean bool = this.mUseLargestChild;
    float f = 0.0F;
    int n = 0;
    int i1 = 0;
    int i2 = 0;
    int i3 = 0;
    int i4 = 0;
    int i5 = 0;
    int i6 = 0;
    int i7 = 1;
    boolean bool1 = false;
    while (i5 < i) {
      View view = getVirtualChildAt(i5);
      if (view == null) {
        this.mTotalLength += measureNullChild(i5);
      } else if (view.getVisibility() == 8) {
        i5 += getChildrenSkipCount(view, i5);
      } else {
        if (hasDividerBeforeChildAt(i5))
          this.mTotalLength += this.mDividerHeight; 
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        f += layoutParams.weight;
        if (k == 1073741824 && layoutParams.height == 0 && layoutParams.weight > 0.0F) {
          i6 = this.mTotalLength;
          this.mTotalLength = Math.max(i6, layoutParams.topMargin + i6 + layoutParams.bottomMargin);
          i6 = 1;
        } else {
          if (layoutParams.height == 0 && layoutParams.weight > 0.0F) {
            layoutParams.height = -2;
            i11 = 0;
          } else {
            i11 = Integer.MIN_VALUE;
          } 
          if (f == 0.0F) {
            i12 = this.mTotalLength;
          } else {
            i12 = 0;
          } 
          measureChildBeforeLayout(view, i5, paramInt1, 0, paramInt2, i12);
          if (i11 != Integer.MIN_VALUE)
            layoutParams.height = i11; 
          int i12 = view.getMeasuredHeight();
          int i11 = this.mTotalLength;
          this.mTotalLength = Math.max(i11, i11 + i12 + layoutParams.topMargin + layoutParams.bottomMargin + getNextLocationOffset(view));
          if (bool)
            i2 = Math.max(i12, i2); 
        } 
        int i10 = i5;
        if (m >= 0 && m == i10 + 1)
          this.mBaselineChildTop = this.mTotalLength; 
        if (i10 >= m || layoutParams.weight <= 0.0F) {
          if (j != 1073741824 && layoutParams.width == -1) {
            i5 = 1;
            bool1 = true;
          } else {
            i5 = 0;
          } 
          int i12 = layoutParams.leftMargin + layoutParams.rightMargin;
          int i11 = view.getMeasuredWidth() + i12;
          i1 = Math.max(i1, i11);
          int i13 = View.combineMeasuredStates(n, view.getMeasuredState());
          if (i7 && layoutParams.width == -1) {
            n = 1;
          } else {
            n = 0;
          } 
          if (layoutParams.weight > 0.0F) {
            if (i5 != 0)
              i11 = i12; 
            i3 = Math.max(i3, i11);
            i7 = i4;
            i4 = i3;
          } else {
            if (i5 != 0)
              i11 = i12; 
            i7 = Math.max(i4, i11);
            i4 = i3;
          } 
          i11 = getChildrenSkipCount(view, i10);
          i5 = n;
          i3 = i4;
          i4 = i7;
          n = i13;
          i11 += i10;
          i7 = i5;
          i5 = i11;
        } else {
          throw new RuntimeException("A child of LinearLayout with index less than mBaselineAlignedChildIndex has weight > 0, which won't work.  Either remove the weight, or don't set mBaselineAlignedChildIndex.");
        } 
      } 
      i5++;
    } 
    if (this.mTotalLength > 0 && hasDividerBeforeChildAt(i))
      this.mTotalLength += this.mDividerHeight; 
    if (bool && (k == Integer.MIN_VALUE || k == 0)) {
      this.mTotalLength = 0;
      for (i5 = 0; i5 < i; i5++) {
        View view = getVirtualChildAt(i5);
        if (view == null) {
          this.mTotalLength += measureNullChild(i5);
        } else if (view.getVisibility() == 8) {
          i5 += getChildrenSkipCount(view, i5);
        } else {
          LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
          int i10 = this.mTotalLength;
          this.mTotalLength = Math.max(i10, i10 + i2 + layoutParams.topMargin + layoutParams.bottomMargin + getNextLocationOffset(view));
        } 
      } 
    } 
    this.mTotalLength += getPaddingTop() + getPaddingBottom();
    int i9 = View.resolveSizeAndState(Math.max(this.mTotalLength, getSuggestedMinimumHeight()), paramInt2, 0);
    int i8 = (0xFFFFFF & i9) - this.mTotalLength;
    if (i6 != 0 || (i8 != 0 && f > 0.0F)) {
      float f1 = this.mWeightSum;
      if (f1 > 0.0F)
        f = f1; 
      this.mTotalLength = 0;
      i5 = 0;
      i3 = i8;
      i2 = i1;
      while (i5 < i) {
        View view = getVirtualChildAt(i5);
        if (view.getVisibility() != 8) {
          LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
          f1 = layoutParams.weight;
          if (f1 > 0.0F) {
            i1 = (int)(i3 * f1 / f);
            i8 = getPaddingLeft();
            int i10 = getPaddingRight();
            i6 = i3 - i1;
            i3 = layoutParams.leftMargin;
            int i11 = layoutParams.rightMargin;
            m = layoutParams.width;
            f -= f1;
            i8 = getChildMeasureSpec(paramInt1, i8 + i10 + i3 + i11, m);
            if (layoutParams.height != 0 || k != 1073741824) {
              i1 = view.getMeasuredHeight() + i1;
              i3 = i1;
              if (i1 < 0)
                i3 = 0; 
              view.measure(i8, View.MeasureSpec.makeMeasureSpec(i3, 1073741824));
            } else {
              if (i1 > 0) {
                i3 = i1;
              } else {
                i3 = 0;
              } 
              view.measure(i8, View.MeasureSpec.makeMeasureSpec(i3, 1073741824));
            } 
            n = View.combineMeasuredStates(n, view.getMeasuredState() & 0xFFFFFF00);
            i3 = i6;
          } 
          i1 = layoutParams.leftMargin + layoutParams.rightMargin;
          i8 = view.getMeasuredWidth() + i1;
          i6 = Math.max(i2, i8);
          if (j != 1073741824 && layoutParams.width == -1) {
            i2 = 1;
          } else {
            i2 = 0;
          } 
          if (i2 != 0) {
            i2 = i1;
          } else {
            i2 = i8;
          } 
          i4 = Math.max(i4, i2);
          if (i7 != 0 && layoutParams.width == -1) {
            i7 = 1;
          } else {
            i7 = 0;
          } 
          i2 = this.mTotalLength;
          this.mTotalLength = Math.max(i2, view.getMeasuredHeight() + i2 + layoutParams.topMargin + layoutParams.bottomMargin + getNextLocationOffset(view));
          i2 = i6;
        } 
        i5++;
      } 
      this.mTotalLength += getPaddingTop() + getPaddingBottom();
      i3 = i4;
      i4 = n;
      n = i3;
    } else {
      i3 = Math.max(i4, i3);
      if (bool && k != 1073741824)
        for (i4 = 0; i4 < i; i4++) {
          View view = getVirtualChildAt(i4);
          if (view != null && view.getVisibility() != 8 && ((LayoutParams)view.getLayoutParams()).weight > 0.0F)
            view.measure(View.MeasureSpec.makeMeasureSpec(view.getMeasuredWidth(), 1073741824), View.MeasureSpec.makeMeasureSpec(i2, 1073741824)); 
        }  
      i4 = n;
      n = i3;
      i2 = i1;
    } 
    if (i7 != 0 || j == 1073741824)
      n = i2; 
    setMeasuredDimension(View.resolveSizeAndState(Math.max(n + getPaddingLeft() + getPaddingRight(), getSuggestedMinimumWidth()), paramInt1, i4), i9);
    if (bool1)
      forceUniformWidth(i, paramInt2); 
  }
  
  protected void onDraw(Canvas paramCanvas) {
    if (this.mDivider == null)
      return; 
    if (this.mOrientation == 1) {
      drawDividersVertical(paramCanvas);
    } else {
      drawDividersHorizontal(paramCanvas);
    } 
  }
  
  public void onInitializeAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent) {
    super.onInitializeAccessibilityEvent(paramAccessibilityEvent);
    paramAccessibilityEvent.setClassName("androidx.appcompat.widget.LinearLayoutCompat");
  }
  
  public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo paramAccessibilityNodeInfo) {
    super.onInitializeAccessibilityNodeInfo(paramAccessibilityNodeInfo);
    paramAccessibilityNodeInfo.setClassName("androidx.appcompat.widget.LinearLayoutCompat");
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    if (this.mOrientation == 1) {
      layoutVertical(paramInt1, paramInt2, paramInt3, paramInt4);
    } else {
      layoutHorizontal(paramInt1, paramInt2, paramInt3, paramInt4);
    } 
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    if (this.mOrientation == 1) {
      measureVertical(paramInt1, paramInt2);
    } else {
      measureHorizontal(paramInt1, paramInt2);
    } 
  }
  
  public void setBaselineAligned(boolean paramBoolean) {
    this.mBaselineAligned = paramBoolean;
  }
  
  public void setBaselineAlignedChildIndex(int paramInt) {
    if (paramInt >= 0 && paramInt < getChildCount()) {
      this.mBaselineAlignedChildIndex = paramInt;
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("base aligned child index out of range (0, ");
    stringBuilder.append(getChildCount());
    stringBuilder.append(")");
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public void setDividerDrawable(Drawable paramDrawable) {
    if (paramDrawable == this.mDivider)
      return; 
    this.mDivider = paramDrawable;
    boolean bool = false;
    if (paramDrawable != null) {
      this.mDividerWidth = paramDrawable.getIntrinsicWidth();
      this.mDividerHeight = paramDrawable.getIntrinsicHeight();
    } else {
      this.mDividerWidth = 0;
      this.mDividerHeight = 0;
    } 
    if (paramDrawable == null)
      bool = true; 
    setWillNotDraw(bool);
    requestLayout();
  }
  
  public void setDividerPadding(int paramInt) {
    this.mDividerPadding = paramInt;
  }
  
  public void setGravity(int paramInt) {
    if (this.mGravity != paramInt) {
      int i = paramInt;
      if ((0x800007 & paramInt) == 0)
        i = paramInt | 0x800003; 
      paramInt = i;
      if ((i & 0x70) == 0)
        paramInt = i | 0x30; 
      this.mGravity = paramInt;
      requestLayout();
    } 
  }
  
  public void setHorizontalGravity(int paramInt) {
    paramInt &= 0x800007;
    int i = this.mGravity;
    if ((0x800007 & i) != paramInt) {
      this.mGravity = paramInt | 0xFF7FFFF8 & i;
      requestLayout();
    } 
  }
  
  public void setMeasureWithLargestChildEnabled(boolean paramBoolean) {
    this.mUseLargestChild = paramBoolean;
  }
  
  public void setOrientation(int paramInt) {
    if (this.mOrientation != paramInt) {
      this.mOrientation = paramInt;
      requestLayout();
    } 
  }
  
  public void setShowDividers(int paramInt) {
    if (paramInt != this.mShowDividers)
      requestLayout(); 
    this.mShowDividers = paramInt;
  }
  
  public void setVerticalGravity(int paramInt) {
    int i = paramInt & 0x70;
    paramInt = this.mGravity;
    if ((paramInt & 0x70) != i) {
      this.mGravity = i | paramInt & 0xFFFFFF8F;
      requestLayout();
    } 
  }
  
  public void setWeightSum(float paramFloat) {
    this.mWeightSum = Math.max(0.0F, paramFloat);
  }
  
  public boolean shouldDelayChildPressedState() {
    return false;
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface DividerMode {}
  
  public static class LayoutParams extends ViewGroup.MarginLayoutParams {
    public int gravity = -1;
    
    public float weight;
    
    public LayoutParams(int param1Int1, int param1Int2) {
      super(param1Int1, param1Int2);
      this.weight = 0.0F;
    }
    
    public LayoutParams(int param1Int1, int param1Int2, float param1Float) {
      super(param1Int1, param1Int2);
      this.weight = param1Float;
    }
    
    public LayoutParams(Context param1Context, AttributeSet param1AttributeSet) {
      super(param1Context, param1AttributeSet);
      TypedArray typedArray = param1Context.obtainStyledAttributes(param1AttributeSet, R.styleable.LinearLayoutCompat_Layout);
      this.weight = typedArray.getFloat(R.styleable.LinearLayoutCompat_Layout_android_layout_weight, 0.0F);
      this.gravity = typedArray.getInt(R.styleable.LinearLayoutCompat_Layout_android_layout_gravity, -1);
      typedArray.recycle();
    }
    
    public LayoutParams(ViewGroup.LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
    }
    
    public LayoutParams(ViewGroup.MarginLayoutParams param1MarginLayoutParams) {
      super(param1MarginLayoutParams);
    }
    
    public LayoutParams(LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
      this.weight = param1LayoutParams.weight;
      this.gravity = param1LayoutParams.gravity;
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface OrientationMode {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/androidx/appcompat/widget/LinearLayoutCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */