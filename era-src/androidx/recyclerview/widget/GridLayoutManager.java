package androidx.recyclerview.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import java.util.Arrays;

public class GridLayoutManager extends LinearLayoutManager {
  private static final boolean DEBUG = false;
  
  public static final int DEFAULT_SPAN_COUNT = -1;
  
  private static final String TAG = "GridLayoutManager";
  
  int[] mCachedBorders;
  
  final Rect mDecorInsets = new Rect();
  
  boolean mPendingSpanCountChange = false;
  
  final SparseIntArray mPreLayoutSpanIndexCache = new SparseIntArray();
  
  final SparseIntArray mPreLayoutSpanSizeCache = new SparseIntArray();
  
  View[] mSet;
  
  int mSpanCount = -1;
  
  SpanSizeLookup mSpanSizeLookup = new DefaultSpanSizeLookup();
  
  private boolean mUsingSpansToEstimateScrollBarDimensions;
  
  public GridLayoutManager(Context paramContext, int paramInt) {
    super(paramContext);
    setSpanCount(paramInt);
  }
  
  public GridLayoutManager(Context paramContext, int paramInt1, int paramInt2, boolean paramBoolean) {
    super(paramContext, paramInt2, paramBoolean);
    setSpanCount(paramInt1);
  }
  
  public GridLayoutManager(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2) {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    setSpanCount((getProperties(paramContext, paramAttributeSet, paramInt1, paramInt2)).spanCount);
  }
  
  private void assignSpans(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, int paramInt, boolean paramBoolean) {
    byte b;
    int i = 0;
    int j = -1;
    if (paramBoolean) {
      boolean bool = false;
      b = 1;
      j = paramInt;
      paramInt = bool;
    } else {
      paramInt--;
      b = -1;
    } 
    while (paramInt != j) {
      View view = this.mSet[paramInt];
      LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
      layoutParams.mSpanSize = getSpanSize(paramRecycler, paramState, getPosition(view));
      layoutParams.mSpanIndex = i;
      i += layoutParams.mSpanSize;
      paramInt += b;
    } 
  }
  
  private void cachePreLayoutSpanMapping() {
    int i = getChildCount();
    for (byte b = 0; b < i; b++) {
      LayoutParams layoutParams = (LayoutParams)getChildAt(b).getLayoutParams();
      int j = layoutParams.getViewLayoutPosition();
      this.mPreLayoutSpanSizeCache.put(j, layoutParams.getSpanSize());
      this.mPreLayoutSpanIndexCache.put(j, layoutParams.getSpanIndex());
    } 
  }
  
  private void calculateItemBorders(int paramInt) {
    this.mCachedBorders = calculateItemBorders(this.mCachedBorders, this.mSpanCount, paramInt);
  }
  
  static int[] calculateItemBorders(int[] paramArrayOfint, int paramInt1, int paramInt2) {
    // Byte code:
    //   0: iconst_1
    //   1: istore_3
    //   2: aload_0
    //   3: ifnull -> 27
    //   6: aload_0
    //   7: arraylength
    //   8: iload_1
    //   9: iconst_1
    //   10: iadd
    //   11: if_icmpne -> 27
    //   14: aload_0
    //   15: astore #4
    //   17: aload_0
    //   18: aload_0
    //   19: arraylength
    //   20: iconst_1
    //   21: isub
    //   22: iaload
    //   23: iload_2
    //   24: if_icmpeq -> 34
    //   27: iload_1
    //   28: iconst_1
    //   29: iadd
    //   30: newarray int
    //   32: astore #4
    //   34: iconst_0
    //   35: istore #5
    //   37: aload #4
    //   39: iconst_0
    //   40: iconst_0
    //   41: iastore
    //   42: iload_2
    //   43: iload_1
    //   44: idiv
    //   45: istore #6
    //   47: iload_2
    //   48: iload_1
    //   49: irem
    //   50: istore #7
    //   52: iconst_0
    //   53: istore #8
    //   55: iload #5
    //   57: istore_2
    //   58: iload_3
    //   59: iload_1
    //   60: if_icmpgt -> 116
    //   63: iload_2
    //   64: iload #7
    //   66: iadd
    //   67: istore_2
    //   68: iload_2
    //   69: ifle -> 93
    //   72: iload_1
    //   73: iload_2
    //   74: isub
    //   75: iload #7
    //   77: if_icmpge -> 93
    //   80: iload #6
    //   82: iconst_1
    //   83: iadd
    //   84: istore #5
    //   86: iload_2
    //   87: iload_1
    //   88: isub
    //   89: istore_2
    //   90: goto -> 97
    //   93: iload #6
    //   95: istore #5
    //   97: iload #8
    //   99: iload #5
    //   101: iadd
    //   102: istore #8
    //   104: aload #4
    //   106: iload_3
    //   107: iload #8
    //   109: iastore
    //   110: iinc #3, 1
    //   113: goto -> 58
    //   116: aload #4
    //   118: areturn
  }
  
  private void clearPreLayoutSpanMappingCache() {
    this.mPreLayoutSpanSizeCache.clear();
    this.mPreLayoutSpanIndexCache.clear();
  }
  
  private int computeScrollOffsetWithSpanInfo(RecyclerView.State paramState) {
    if (getChildCount() != 0 && paramState.getItemCount() != 0) {
      ensureLayoutState();
      boolean bool = isSmoothScrollbarEnabled();
      View view1 = findFirstVisibleChildClosestToStart(bool ^ true, true);
      View view2 = findFirstVisibleChildClosestToEnd(bool ^ true, true);
      if (view1 != null && view2 != null) {
        int i = this.mSpanSizeLookup.getCachedSpanGroupIndex(getPosition(view1), this.mSpanCount);
        int j = this.mSpanSizeLookup.getCachedSpanGroupIndex(getPosition(view2), this.mSpanCount);
        int k = Math.min(i, j);
        j = Math.max(i, j);
        i = this.mSpanSizeLookup.getCachedSpanGroupIndex(paramState.getItemCount() - 1, this.mSpanCount);
        if (this.mShouldReverseLayout) {
          k = Math.max(0, i + 1 - j - 1);
        } else {
          k = Math.max(0, k);
        } 
        if (!bool)
          return k; 
        i = Math.abs(this.mOrientationHelper.getDecoratedEnd(view2) - this.mOrientationHelper.getDecoratedStart(view1));
        j = this.mSpanSizeLookup.getCachedSpanGroupIndex(getPosition(view1), this.mSpanCount);
        int m = this.mSpanSizeLookup.getCachedSpanGroupIndex(getPosition(view2), this.mSpanCount);
        float f = i / (m - j + 1);
        return Math.round(k * f + (this.mOrientationHelper.getStartAfterPadding() - this.mOrientationHelper.getDecoratedStart(view1)));
      } 
    } 
    return 0;
  }
  
  private int computeScrollRangeWithSpanInfo(RecyclerView.State paramState) {
    if (getChildCount() != 0 && paramState.getItemCount() != 0) {
      ensureLayoutState();
      View view1 = findFirstVisibleChildClosestToStart(isSmoothScrollbarEnabled() ^ true, true);
      View view2 = findFirstVisibleChildClosestToEnd(isSmoothScrollbarEnabled() ^ true, true);
      if (view1 != null && view2 != null) {
        if (!isSmoothScrollbarEnabled())
          return this.mSpanSizeLookup.getCachedSpanGroupIndex(paramState.getItemCount() - 1, this.mSpanCount) + 1; 
        int i = this.mOrientationHelper.getDecoratedEnd(view2);
        int j = this.mOrientationHelper.getDecoratedStart(view1);
        int k = this.mSpanSizeLookup.getCachedSpanGroupIndex(getPosition(view1), this.mSpanCount);
        int m = this.mSpanSizeLookup.getCachedSpanGroupIndex(getPosition(view2), this.mSpanCount);
        int n = this.mSpanSizeLookup.getCachedSpanGroupIndex(paramState.getItemCount() - 1, this.mSpanCount);
        return (int)((i - j) / (m - k + 1) * (n + 1));
      } 
    } 
    return 0;
  }
  
  private void ensureAnchorIsInCorrectSpan(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, LinearLayoutManager.AnchorInfo paramAnchorInfo, int paramInt) {
    if (paramInt == 1) {
      paramInt = 1;
    } else {
      paramInt = 0;
    } 
    int i = getSpanIndex(paramRecycler, paramState, paramAnchorInfo.mPosition);
    if (paramInt != 0) {
      while (i > 0 && paramAnchorInfo.mPosition > 0) {
        paramAnchorInfo.mPosition--;
        i = getSpanIndex(paramRecycler, paramState, paramAnchorInfo.mPosition);
      } 
    } else {
      int j = paramState.getItemCount();
      paramInt = paramAnchorInfo.mPosition;
      while (paramInt < j - 1) {
        int k = paramInt + 1;
        int m = getSpanIndex(paramRecycler, paramState, k);
        if (m > i) {
          paramInt = k;
          i = m;
        } 
      } 
      paramAnchorInfo.mPosition = paramInt;
    } 
  }
  
  private void ensureViewSet() {
    View[] arrayOfView = this.mSet;
    if (arrayOfView == null || arrayOfView.length != this.mSpanCount)
      this.mSet = new View[this.mSpanCount]; 
  }
  
  private int getSpanGroupIndex(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, int paramInt) {
    if (!paramState.isPreLayout())
      return this.mSpanSizeLookup.getCachedSpanGroupIndex(paramInt, this.mSpanCount); 
    int i = paramRecycler.convertPreLayoutPositionToPostLayout(paramInt);
    if (i == -1) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Cannot find span size for pre layout position. ");
      stringBuilder.append(paramInt);
      Log.w("GridLayoutManager", stringBuilder.toString());
      return 0;
    } 
    return this.mSpanSizeLookup.getCachedSpanGroupIndex(i, this.mSpanCount);
  }
  
  private int getSpanIndex(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, int paramInt) {
    if (!paramState.isPreLayout())
      return this.mSpanSizeLookup.getCachedSpanIndex(paramInt, this.mSpanCount); 
    int i = this.mPreLayoutSpanIndexCache.get(paramInt, -1);
    if (i != -1)
      return i; 
    i = paramRecycler.convertPreLayoutPositionToPostLayout(paramInt);
    if (i == -1) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Cannot find span size for pre layout position. It is not cached, not in the adapter. Pos:");
      stringBuilder.append(paramInt);
      Log.w("GridLayoutManager", stringBuilder.toString());
      return 0;
    } 
    return this.mSpanSizeLookup.getCachedSpanIndex(i, this.mSpanCount);
  }
  
  private int getSpanSize(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, int paramInt) {
    if (!paramState.isPreLayout())
      return this.mSpanSizeLookup.getSpanSize(paramInt); 
    int i = this.mPreLayoutSpanSizeCache.get(paramInt, -1);
    if (i != -1)
      return i; 
    i = paramRecycler.convertPreLayoutPositionToPostLayout(paramInt);
    if (i == -1) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Cannot find span size for pre layout position. It is not cached, not in the adapter. Pos:");
      stringBuilder.append(paramInt);
      Log.w("GridLayoutManager", stringBuilder.toString());
      return 1;
    } 
    return this.mSpanSizeLookup.getSpanSize(i);
  }
  
  private void guessMeasurement(float paramFloat, int paramInt) {
    calculateItemBorders(Math.max(Math.round(paramFloat * this.mSpanCount), paramInt));
  }
  
  private void measureChild(View paramView, int paramInt, boolean paramBoolean) {
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    Rect rect = layoutParams.mDecorInsets;
    int i = rect.top + rect.bottom + layoutParams.topMargin + layoutParams.bottomMargin;
    int j = rect.left + rect.right + layoutParams.leftMargin + layoutParams.rightMargin;
    int k = getSpaceForSpanRange(layoutParams.mSpanIndex, layoutParams.mSpanSize);
    if (this.mOrientation == 1) {
      j = getChildMeasureSpec(k, paramInt, j, layoutParams.width, false);
      paramInt = getChildMeasureSpec(this.mOrientationHelper.getTotalSpace(), getHeightMode(), i, layoutParams.height, true);
    } else {
      paramInt = getChildMeasureSpec(k, paramInt, i, layoutParams.height, false);
      j = getChildMeasureSpec(this.mOrientationHelper.getTotalSpace(), getWidthMode(), j, layoutParams.width, true);
    } 
    measureChildWithDecorationsAndMargin(paramView, j, paramInt, paramBoolean);
  }
  
  private void measureChildWithDecorationsAndMargin(View paramView, int paramInt1, int paramInt2, boolean paramBoolean) {
    RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)paramView.getLayoutParams();
    if (paramBoolean) {
      paramBoolean = shouldReMeasureChild(paramView, paramInt1, paramInt2, layoutParams);
    } else {
      paramBoolean = shouldMeasureChild(paramView, paramInt1, paramInt2, layoutParams);
    } 
    if (paramBoolean)
      paramView.measure(paramInt1, paramInt2); 
  }
  
  private void updateMeasurements() {
    int i;
    int j;
    if (getOrientation() == 1) {
      i = getWidth() - getPaddingRight();
      j = getPaddingLeft();
    } else {
      i = getHeight() - getPaddingBottom();
      j = getPaddingTop();
    } 
    calculateItemBorders(i - j);
  }
  
  public boolean checkLayoutParams(RecyclerView.LayoutParams paramLayoutParams) {
    return paramLayoutParams instanceof LayoutParams;
  }
  
  void collectPrefetchPositionsForLayoutState(RecyclerView.State paramState, LinearLayoutManager.LayoutState paramLayoutState, RecyclerView.LayoutManager.LayoutPrefetchRegistry paramLayoutPrefetchRegistry) {
    int i = this.mSpanCount;
    for (byte b = 0; b < this.mSpanCount && paramLayoutState.hasMore(paramState) && i > 0; b++) {
      int j = paramLayoutState.mCurrentPosition;
      paramLayoutPrefetchRegistry.addPosition(j, Math.max(0, paramLayoutState.mScrollingOffset));
      i -= this.mSpanSizeLookup.getSpanSize(j);
      paramLayoutState.mCurrentPosition += paramLayoutState.mItemDirection;
    } 
  }
  
  public int computeHorizontalScrollOffset(RecyclerView.State paramState) {
    return this.mUsingSpansToEstimateScrollBarDimensions ? computeScrollOffsetWithSpanInfo(paramState) : super.computeHorizontalScrollOffset(paramState);
  }
  
  public int computeHorizontalScrollRange(RecyclerView.State paramState) {
    return this.mUsingSpansToEstimateScrollBarDimensions ? computeScrollRangeWithSpanInfo(paramState) : super.computeHorizontalScrollRange(paramState);
  }
  
  public int computeVerticalScrollOffset(RecyclerView.State paramState) {
    return this.mUsingSpansToEstimateScrollBarDimensions ? computeScrollOffsetWithSpanInfo(paramState) : super.computeVerticalScrollOffset(paramState);
  }
  
  public int computeVerticalScrollRange(RecyclerView.State paramState) {
    return this.mUsingSpansToEstimateScrollBarDimensions ? computeScrollRangeWithSpanInfo(paramState) : super.computeVerticalScrollRange(paramState);
  }
  
  View findReferenceChild(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, int paramInt1, int paramInt2, int paramInt3) {
    byte b;
    ensureLayoutState();
    int i = this.mOrientationHelper.getStartAfterPadding();
    int j = this.mOrientationHelper.getEndAfterPadding();
    if (paramInt2 > paramInt1) {
      b = 1;
    } else {
      b = -1;
    } 
    View view1 = null;
    View view2;
    for (view2 = null; paramInt1 != paramInt2; view2 = view5) {
      View view3 = getChildAt(paramInt1);
      int k = getPosition(view3);
      View view4 = view1;
      View view5 = view2;
      if (k >= 0) {
        view4 = view1;
        view5 = view2;
        if (k < paramInt3)
          if (getSpanIndex(paramRecycler, paramState, k) != 0) {
            view4 = view1;
            view5 = view2;
          } else if (((RecyclerView.LayoutParams)view3.getLayoutParams()).isItemRemoved()) {
            view4 = view1;
            view5 = view2;
            if (view2 == null) {
              view5 = view3;
              view4 = view1;
            } 
          } else if (this.mOrientationHelper.getDecoratedStart(view3) >= j || this.mOrientationHelper.getDecoratedEnd(view3) < i) {
            view4 = view1;
            view5 = view2;
            if (view1 == null) {
              view4 = view3;
              view5 = view2;
            } 
          } else {
            return view3;
          }  
      } 
      paramInt1 += b;
      view1 = view4;
    } 
    if (view1 != null)
      view2 = view1; 
    return view2;
  }
  
  public RecyclerView.LayoutParams generateDefaultLayoutParams() {
    return (this.mOrientation == 0) ? new LayoutParams(-2, -1) : new LayoutParams(-1, -2);
  }
  
  public RecyclerView.LayoutParams generateLayoutParams(Context paramContext, AttributeSet paramAttributeSet) {
    return new LayoutParams(paramContext, paramAttributeSet);
  }
  
  public RecyclerView.LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    return (paramLayoutParams instanceof ViewGroup.MarginLayoutParams) ? new LayoutParams((ViewGroup.MarginLayoutParams)paramLayoutParams) : new LayoutParams(paramLayoutParams);
  }
  
  public int getColumnCountForAccessibility(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState) {
    return (this.mOrientation == 1) ? this.mSpanCount : ((paramState.getItemCount() < 1) ? 0 : (getSpanGroupIndex(paramRecycler, paramState, paramState.getItemCount() - 1) + 1));
  }
  
  public int getRowCountForAccessibility(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState) {
    return (this.mOrientation == 0) ? this.mSpanCount : ((paramState.getItemCount() < 1) ? 0 : (getSpanGroupIndex(paramRecycler, paramState, paramState.getItemCount() - 1) + 1));
  }
  
  int getSpaceForSpanRange(int paramInt1, int paramInt2) {
    if (this.mOrientation == 1 && isLayoutRTL()) {
      int[] arrayOfInt1 = this.mCachedBorders;
      int i = this.mSpanCount;
      return arrayOfInt1[i - paramInt1] - arrayOfInt1[i - paramInt1 - paramInt2];
    } 
    int[] arrayOfInt = this.mCachedBorders;
    return arrayOfInt[paramInt2 + paramInt1] - arrayOfInt[paramInt1];
  }
  
  public int getSpanCount() {
    return this.mSpanCount;
  }
  
  public SpanSizeLookup getSpanSizeLookup() {
    return this.mSpanSizeLookup;
  }
  
  public boolean isUsingSpansToEstimateScrollbarDimensions() {
    return this.mUsingSpansToEstimateScrollBarDimensions;
  }
  
  void layoutChunk(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, LinearLayoutManager.LayoutState paramLayoutState, LinearLayoutManager.LayoutChunkResult paramLayoutChunkResult) {
    StringBuilder stringBuilder;
    int j;
    int k;
    boolean bool;
    int i = this.mOrientationHelper.getModeInOther();
    if (i != 1073741824) {
      j = 1;
    } else {
      j = 0;
    } 
    if (getChildCount() > 0) {
      k = this.mCachedBorders[this.mSpanCount];
    } else {
      k = 0;
    } 
    if (j)
      updateMeasurements(); 
    if (paramLayoutState.mItemDirection == 1) {
      bool = true;
    } else {
      bool = false;
    } 
    int m = this.mSpanCount;
    if (!bool)
      m = getSpanIndex(paramRecycler, paramState, paramLayoutState.mCurrentPosition) + getSpanSize(paramRecycler, paramState, paramLayoutState.mCurrentPosition); 
    byte b = 0;
    while (b < this.mSpanCount && paramLayoutState.hasMore(paramState) && m > 0) {
      int i2 = paramLayoutState.mCurrentPosition;
      int i3 = getSpanSize(paramRecycler, paramState, i2);
      if (i3 <= this.mSpanCount) {
        m -= i3;
        if (m < 0)
          break; 
        View view = paramLayoutState.next(paramRecycler);
        if (view == null)
          break; 
        this.mSet[b] = view;
        b++;
        continue;
      } 
      stringBuilder = new StringBuilder();
      stringBuilder.append("Item at position ");
      stringBuilder.append(i2);
      stringBuilder.append(" requires ");
      stringBuilder.append(i3);
      stringBuilder.append(" spans but GridLayoutManager has only ");
      stringBuilder.append(this.mSpanCount);
      stringBuilder.append(" spans.");
      throw new IllegalArgumentException(stringBuilder.toString());
    } 
    if (b == 0) {
      paramLayoutChunkResult.mFinished = true;
      return;
    } 
    float f = 0.0F;
    assignSpans((RecyclerView.Recycler)stringBuilder, paramState, b, bool);
    int i1 = 0;
    m = 0;
    while (i1 < b) {
      View view = this.mSet[i1];
      if (paramLayoutState.mScrapList == null) {
        if (bool) {
          addView(view);
        } else {
          addView(view, 0);
        } 
      } else if (bool) {
        addDisappearingView(view);
      } else {
        addDisappearingView(view, 0);
      } 
      calculateItemDecorationsForChild(view, this.mDecorInsets);
      measureChild(view, i, false);
      int i3 = this.mOrientationHelper.getDecoratedMeasurement(view);
      int i2 = m;
      if (i3 > m)
        i2 = i3; 
      LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
      float f1 = this.mOrientationHelper.getDecoratedMeasurementInOther(view) * 1.0F / layoutParams.mSpanSize;
      float f2 = f;
      if (f1 > f)
        f2 = f1; 
      i1++;
      m = i2;
      f = f2;
    } 
    int n = m;
    if (j) {
      guessMeasurement(f, k);
      j = 0;
      m = 0;
      while (true) {
        n = m;
        if (j < b) {
          View view = this.mSet[j];
          measureChild(view, 1073741824, true);
          k = this.mOrientationHelper.getDecoratedMeasurement(view);
          n = m;
          if (k > m)
            n = k; 
          j++;
          m = n;
          continue;
        } 
        break;
      } 
    } 
    for (m = 0; m < b; m++) {
      View view = this.mSet[m];
      if (this.mOrientationHelper.getDecoratedMeasurement(view) != n) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        Rect rect = layoutParams.mDecorInsets;
        j = rect.top + rect.bottom + layoutParams.topMargin + layoutParams.bottomMargin;
        k = rect.left + rect.right + layoutParams.leftMargin + layoutParams.rightMargin;
        i1 = getSpaceForSpanRange(layoutParams.mSpanIndex, layoutParams.mSpanSize);
        if (this.mOrientation == 1) {
          k = getChildMeasureSpec(i1, 1073741824, k, layoutParams.width, false);
          j = View.MeasureSpec.makeMeasureSpec(n - j, 1073741824);
        } else {
          k = View.MeasureSpec.makeMeasureSpec(n - k, 1073741824);
          j = getChildMeasureSpec(i1, 1073741824, j, layoutParams.height, false);
        } 
        measureChildWithDecorationsAndMargin(view, k, j, true);
      } 
    } 
    i1 = 0;
    paramLayoutChunkResult.mConsumed = n;
    if (this.mOrientation == 1) {
      if (paramLayoutState.mLayoutDirection == -1) {
        j = paramLayoutState.mOffset;
        m = j - n;
        n = j;
      } else {
        m = paramLayoutState.mOffset;
        n = m + n;
      } 
      j = 0;
      k = 0;
    } else if (paramLayoutState.mLayoutDirection == -1) {
      k = paramLayoutState.mOffset;
      j = k - n;
      m = 0;
      n = 0;
    } else {
      j = paramLayoutState.mOffset;
      m = 0;
      boolean bool1 = false;
      k = j + n;
      n = bool1;
    } 
    while (i1 < b) {
      View view = this.mSet[i1];
      LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
      if (this.mOrientation == 1) {
        if (isLayoutRTL()) {
          j = getPaddingLeft() + this.mCachedBorders[this.mSpanCount - layoutParams.mSpanIndex];
          int i2 = this.mOrientationHelper.getDecoratedMeasurementInOther(view);
          k = j;
          j -= i2;
        } else {
          k = getPaddingLeft() + this.mCachedBorders[layoutParams.mSpanIndex];
          int i2 = this.mOrientationHelper.getDecoratedMeasurementInOther(view);
          j = k;
          k = i2 + k;
        } 
      } else {
        n = getPaddingTop() + this.mCachedBorders[layoutParams.mSpanIndex];
        int i2 = this.mOrientationHelper.getDecoratedMeasurementInOther(view);
        m = n;
        n = i2 + n;
      } 
      layoutDecoratedWithMargins(view, j, m, k, n);
      if (layoutParams.isItemRemoved() || layoutParams.isItemChanged())
        paramLayoutChunkResult.mIgnoreConsumed = true; 
      paramLayoutChunkResult.mFocusable |= view.hasFocusable();
      i1++;
    } 
    Arrays.fill((Object[])this.mSet, (Object)null);
  }
  
  void onAnchorReady(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, LinearLayoutManager.AnchorInfo paramAnchorInfo, int paramInt) {
    super.onAnchorReady(paramRecycler, paramState, paramAnchorInfo, paramInt);
    updateMeasurements();
    if (paramState.getItemCount() > 0 && !paramState.isPreLayout())
      ensureAnchorIsInCorrectSpan(paramRecycler, paramState, paramAnchorInfo, paramInt); 
    ensureViewSet();
  }
  
  public View onFocusSearchFailed(View paramView, int paramInt, RecyclerView.Recycler paramRecycler, RecyclerView.State paramState) {
    // Byte code:
    //   0: aload_0
    //   1: aload_1
    //   2: invokevirtual findContainingItemView : (Landroid/view/View;)Landroid/view/View;
    //   5: astore #5
    //   7: aconst_null
    //   8: astore #6
    //   10: aload #5
    //   12: ifnonnull -> 17
    //   15: aconst_null
    //   16: areturn
    //   17: aload #5
    //   19: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   22: checkcast androidx/recyclerview/widget/GridLayoutManager$LayoutParams
    //   25: astore #7
    //   27: aload #7
    //   29: getfield mSpanIndex : I
    //   32: istore #8
    //   34: aload #7
    //   36: getfield mSpanIndex : I
    //   39: aload #7
    //   41: getfield mSpanSize : I
    //   44: iadd
    //   45: istore #9
    //   47: aload_0
    //   48: aload_1
    //   49: iload_2
    //   50: aload_3
    //   51: aload #4
    //   53: invokespecial onFocusSearchFailed : (Landroid/view/View;ILandroidx/recyclerview/widget/RecyclerView$Recycler;Landroidx/recyclerview/widget/RecyclerView$State;)Landroid/view/View;
    //   56: ifnonnull -> 61
    //   59: aconst_null
    //   60: areturn
    //   61: aload_0
    //   62: iload_2
    //   63: invokevirtual convertFocusDirectionToLayoutDirection : (I)I
    //   66: iconst_1
    //   67: if_icmpne -> 76
    //   70: iconst_1
    //   71: istore #10
    //   73: goto -> 79
    //   76: iconst_0
    //   77: istore #10
    //   79: iload #10
    //   81: aload_0
    //   82: getfield mShouldReverseLayout : Z
    //   85: if_icmpeq -> 93
    //   88: iconst_1
    //   89: istore_2
    //   90: goto -> 95
    //   93: iconst_0
    //   94: istore_2
    //   95: iload_2
    //   96: ifeq -> 116
    //   99: aload_0
    //   100: invokevirtual getChildCount : ()I
    //   103: iconst_1
    //   104: isub
    //   105: istore #11
    //   107: iconst_m1
    //   108: istore #12
    //   110: iconst_m1
    //   111: istore #13
    //   113: goto -> 128
    //   116: aload_0
    //   117: invokevirtual getChildCount : ()I
    //   120: istore #12
    //   122: iconst_0
    //   123: istore #11
    //   125: iconst_1
    //   126: istore #13
    //   128: aload_0
    //   129: getfield mOrientation : I
    //   132: iconst_1
    //   133: if_icmpne -> 149
    //   136: aload_0
    //   137: invokevirtual isLayoutRTL : ()Z
    //   140: ifeq -> 149
    //   143: iconst_1
    //   144: istore #14
    //   146: goto -> 152
    //   149: iconst_0
    //   150: istore #14
    //   152: aload_0
    //   153: aload_3
    //   154: aload #4
    //   156: iload #11
    //   158: invokespecial getSpanGroupIndex : (Landroidx/recyclerview/widget/RecyclerView$Recycler;Landroidx/recyclerview/widget/RecyclerView$State;I)I
    //   161: istore #15
    //   163: aconst_null
    //   164: astore_1
    //   165: iconst_m1
    //   166: istore #16
    //   168: iconst_0
    //   169: istore #17
    //   171: iconst_0
    //   172: istore_2
    //   173: iconst_m1
    //   174: istore #18
    //   176: iload #12
    //   178: istore #19
    //   180: iload #16
    //   182: istore #12
    //   184: iload #11
    //   186: istore #16
    //   188: iload #16
    //   190: iload #19
    //   192: if_icmpeq -> 585
    //   195: aload_0
    //   196: aload_3
    //   197: aload #4
    //   199: iload #16
    //   201: invokespecial getSpanGroupIndex : (Landroidx/recyclerview/widget/RecyclerView$Recycler;Landroidx/recyclerview/widget/RecyclerView$State;I)I
    //   204: istore #11
    //   206: aload_0
    //   207: iload #16
    //   209: invokevirtual getChildAt : (I)Landroid/view/View;
    //   212: astore #7
    //   214: aload #7
    //   216: aload #5
    //   218: if_acmpne -> 224
    //   221: goto -> 585
    //   224: aload #7
    //   226: invokevirtual hasFocusable : ()Z
    //   229: ifeq -> 250
    //   232: iload #11
    //   234: iload #15
    //   236: if_icmpeq -> 250
    //   239: aload #6
    //   241: ifnull -> 247
    //   244: goto -> 585
    //   247: goto -> 575
    //   250: aload #7
    //   252: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   255: checkcast androidx/recyclerview/widget/GridLayoutManager$LayoutParams
    //   258: astore #20
    //   260: aload #20
    //   262: getfield mSpanIndex : I
    //   265: istore #21
    //   267: aload #20
    //   269: getfield mSpanIndex : I
    //   272: aload #20
    //   274: getfield mSpanSize : I
    //   277: iadd
    //   278: istore #22
    //   280: aload #7
    //   282: invokevirtual hasFocusable : ()Z
    //   285: ifeq -> 305
    //   288: iload #21
    //   290: iload #8
    //   292: if_icmpne -> 305
    //   295: iload #22
    //   297: iload #9
    //   299: if_icmpne -> 305
    //   302: aload #7
    //   304: areturn
    //   305: aload #7
    //   307: invokevirtual hasFocusable : ()Z
    //   310: ifeq -> 318
    //   313: aload #6
    //   315: ifnull -> 330
    //   318: aload #7
    //   320: invokevirtual hasFocusable : ()Z
    //   323: ifne -> 336
    //   326: aload_1
    //   327: ifnonnull -> 336
    //   330: iconst_1
    //   331: istore #11
    //   333: goto -> 488
    //   336: iload #21
    //   338: iload #8
    //   340: invokestatic max : (II)I
    //   343: istore #11
    //   345: iload #22
    //   347: iload #9
    //   349: invokestatic min : (II)I
    //   352: iload #11
    //   354: isub
    //   355: istore #23
    //   357: aload #7
    //   359: invokevirtual hasFocusable : ()Z
    //   362: ifeq -> 408
    //   365: iload #23
    //   367: iload #17
    //   369: if_icmple -> 375
    //   372: goto -> 330
    //   375: iload #23
    //   377: iload #17
    //   379: if_icmpne -> 485
    //   382: iload #21
    //   384: iload #12
    //   386: if_icmple -> 395
    //   389: iconst_1
    //   390: istore #11
    //   392: goto -> 398
    //   395: iconst_0
    //   396: istore #11
    //   398: iload #14
    //   400: iload #11
    //   402: if_icmpne -> 485
    //   405: goto -> 330
    //   408: aload #6
    //   410: ifnonnull -> 485
    //   413: iconst_1
    //   414: istore #24
    //   416: iconst_1
    //   417: istore #25
    //   419: aload_0
    //   420: aload #7
    //   422: iconst_0
    //   423: iconst_1
    //   424: invokevirtual isViewPartiallyVisible : (Landroid/view/View;ZZ)Z
    //   427: istore #10
    //   429: iload_2
    //   430: istore #11
    //   432: iload #10
    //   434: ifeq -> 485
    //   437: iload #23
    //   439: iload #11
    //   441: if_icmple -> 451
    //   444: iload #24
    //   446: istore #11
    //   448: goto -> 488
    //   451: iload #23
    //   453: iload #11
    //   455: if_icmpne -> 485
    //   458: iload #21
    //   460: iload #18
    //   462: if_icmple -> 472
    //   465: iload #25
    //   467: istore #11
    //   469: goto -> 475
    //   472: iconst_0
    //   473: istore #11
    //   475: iload #14
    //   477: iload #11
    //   479: if_icmpne -> 485
    //   482: goto -> 330
    //   485: iconst_0
    //   486: istore #11
    //   488: iload #11
    //   490: ifeq -> 575
    //   493: aload #7
    //   495: invokevirtual hasFocusable : ()Z
    //   498: ifeq -> 540
    //   501: aload #20
    //   503: getfield mSpanIndex : I
    //   506: istore #12
    //   508: iload #22
    //   510: iload #9
    //   512: invokestatic min : (II)I
    //   515: istore #11
    //   517: iload #21
    //   519: iload #8
    //   521: invokestatic max : (II)I
    //   524: istore #17
    //   526: iload #11
    //   528: iload #17
    //   530: isub
    //   531: istore #17
    //   533: aload #7
    //   535: astore #6
    //   537: goto -> 575
    //   540: aload #20
    //   542: getfield mSpanIndex : I
    //   545: istore #18
    //   547: iload #22
    //   549: iload #9
    //   551: invokestatic min : (II)I
    //   554: istore_2
    //   555: iload #21
    //   557: iload #8
    //   559: invokestatic max : (II)I
    //   562: istore #11
    //   564: aload #7
    //   566: astore_1
    //   567: iload_2
    //   568: iload #11
    //   570: isub
    //   571: istore_2
    //   572: goto -> 575
    //   575: iload #16
    //   577: iload #13
    //   579: iadd
    //   580: istore #16
    //   582: goto -> 188
    //   585: aload #6
    //   587: ifnull -> 596
    //   590: aload #6
    //   592: astore_1
    //   593: goto -> 596
    //   596: aload_1
    //   597: areturn
  }
  
  public void onInitializeAccessibilityNodeInfoForItem(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, View paramView, AccessibilityNodeInfoCompat paramAccessibilityNodeInfoCompat) {
    ViewGroup.LayoutParams layoutParams1 = paramView.getLayoutParams();
    if (!(layoutParams1 instanceof LayoutParams)) {
      onInitializeAccessibilityNodeInfoForItem(paramView, paramAccessibilityNodeInfoCompat);
      return;
    } 
    LayoutParams layoutParams = (LayoutParams)layoutParams1;
    int i = getSpanGroupIndex(paramRecycler, paramState, layoutParams.getViewLayoutPosition());
    if (this.mOrientation == 0) {
      paramAccessibilityNodeInfoCompat.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(layoutParams.getSpanIndex(), layoutParams.getSpanSize(), i, 1, false, false));
    } else {
      paramAccessibilityNodeInfoCompat.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(i, 1, layoutParams.getSpanIndex(), layoutParams.getSpanSize(), false, false));
    } 
  }
  
  public void onItemsAdded(RecyclerView paramRecyclerView, int paramInt1, int paramInt2) {
    this.mSpanSizeLookup.invalidateSpanIndexCache();
    this.mSpanSizeLookup.invalidateSpanGroupIndexCache();
  }
  
  public void onItemsChanged(RecyclerView paramRecyclerView) {
    this.mSpanSizeLookup.invalidateSpanIndexCache();
    this.mSpanSizeLookup.invalidateSpanGroupIndexCache();
  }
  
  public void onItemsMoved(RecyclerView paramRecyclerView, int paramInt1, int paramInt2, int paramInt3) {
    this.mSpanSizeLookup.invalidateSpanIndexCache();
    this.mSpanSizeLookup.invalidateSpanGroupIndexCache();
  }
  
  public void onItemsRemoved(RecyclerView paramRecyclerView, int paramInt1, int paramInt2) {
    this.mSpanSizeLookup.invalidateSpanIndexCache();
    this.mSpanSizeLookup.invalidateSpanGroupIndexCache();
  }
  
  public void onItemsUpdated(RecyclerView paramRecyclerView, int paramInt1, int paramInt2, Object paramObject) {
    this.mSpanSizeLookup.invalidateSpanIndexCache();
    this.mSpanSizeLookup.invalidateSpanGroupIndexCache();
  }
  
  public void onLayoutChildren(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState) {
    if (paramState.isPreLayout())
      cachePreLayoutSpanMapping(); 
    super.onLayoutChildren(paramRecycler, paramState);
    clearPreLayoutSpanMappingCache();
  }
  
  public void onLayoutCompleted(RecyclerView.State paramState) {
    super.onLayoutCompleted(paramState);
    this.mPendingSpanCountChange = false;
  }
  
  public int scrollHorizontallyBy(int paramInt, RecyclerView.Recycler paramRecycler, RecyclerView.State paramState) {
    updateMeasurements();
    ensureViewSet();
    return super.scrollHorizontallyBy(paramInt, paramRecycler, paramState);
  }
  
  public int scrollVerticallyBy(int paramInt, RecyclerView.Recycler paramRecycler, RecyclerView.State paramState) {
    updateMeasurements();
    ensureViewSet();
    return super.scrollVerticallyBy(paramInt, paramRecycler, paramState);
  }
  
  public void setMeasuredDimension(Rect paramRect, int paramInt1, int paramInt2) {
    int[] arrayOfInt;
    if (this.mCachedBorders == null)
      super.setMeasuredDimension(paramRect, paramInt1, paramInt2); 
    int i = getPaddingLeft() + getPaddingRight();
    int j = getPaddingTop() + getPaddingBottom();
    if (this.mOrientation == 1) {
      paramInt2 = chooseSize(paramInt2, paramRect.height() + j, getMinimumHeight());
      arrayOfInt = this.mCachedBorders;
      paramInt1 = chooseSize(paramInt1, arrayOfInt[arrayOfInt.length - 1] + i, getMinimumWidth());
    } else {
      paramInt1 = chooseSize(paramInt1, arrayOfInt.width() + i, getMinimumWidth());
      arrayOfInt = this.mCachedBorders;
      paramInt2 = chooseSize(paramInt2, arrayOfInt[arrayOfInt.length - 1] + j, getMinimumHeight());
    } 
    setMeasuredDimension(paramInt1, paramInt2);
  }
  
  public void setSpanCount(int paramInt) {
    if (paramInt == this.mSpanCount)
      return; 
    this.mPendingSpanCountChange = true;
    if (paramInt >= 1) {
      this.mSpanCount = paramInt;
      this.mSpanSizeLookup.invalidateSpanIndexCache();
      requestLayout();
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Span count should be at least 1. Provided ");
    stringBuilder.append(paramInt);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public void setSpanSizeLookup(SpanSizeLookup paramSpanSizeLookup) {
    this.mSpanSizeLookup = paramSpanSizeLookup;
  }
  
  public void setStackFromEnd(boolean paramBoolean) {
    if (!paramBoolean) {
      super.setStackFromEnd(false);
      return;
    } 
    throw new UnsupportedOperationException("GridLayoutManager does not support stack from end. Consider using reverse layout");
  }
  
  public void setUsingSpansToEstimateScrollbarDimensions(boolean paramBoolean) {
    this.mUsingSpansToEstimateScrollBarDimensions = paramBoolean;
  }
  
  public boolean supportsPredictiveItemAnimations() {
    boolean bool;
    if (this.mPendingSavedState == null && !this.mPendingSpanCountChange) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public static final class DefaultSpanSizeLookup extends SpanSizeLookup {
    public int getSpanIndex(int param1Int1, int param1Int2) {
      return param1Int1 % param1Int2;
    }
    
    public int getSpanSize(int param1Int) {
      return 1;
    }
  }
  
  public static class LayoutParams extends RecyclerView.LayoutParams {
    public static final int INVALID_SPAN_ID = -1;
    
    int mSpanIndex = -1;
    
    int mSpanSize = 0;
    
    public LayoutParams(int param1Int1, int param1Int2) {
      super(param1Int1, param1Int2);
    }
    
    public LayoutParams(Context param1Context, AttributeSet param1AttributeSet) {
      super(param1Context, param1AttributeSet);
    }
    
    public LayoutParams(ViewGroup.LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
    }
    
    public LayoutParams(ViewGroup.MarginLayoutParams param1MarginLayoutParams) {
      super(param1MarginLayoutParams);
    }
    
    public LayoutParams(RecyclerView.LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
    }
    
    public int getSpanIndex() {
      return this.mSpanIndex;
    }
    
    public int getSpanSize() {
      return this.mSpanSize;
    }
  }
  
  public static abstract class SpanSizeLookup {
    private boolean mCacheSpanGroupIndices = false;
    
    private boolean mCacheSpanIndices = false;
    
    final SparseIntArray mSpanGroupIndexCache = new SparseIntArray();
    
    final SparseIntArray mSpanIndexCache = new SparseIntArray();
    
    static int findFirstKeyLessThan(SparseIntArray param1SparseIntArray, int param1Int) {
      int i = param1SparseIntArray.size() - 1;
      int j = 0;
      while (j <= i) {
        int k = j + i >>> 1;
        if (param1SparseIntArray.keyAt(k) < param1Int) {
          j = k + 1;
          continue;
        } 
        i = k - 1;
      } 
      param1Int = j - 1;
      return (param1Int >= 0 && param1Int < param1SparseIntArray.size()) ? param1SparseIntArray.keyAt(param1Int) : -1;
    }
    
    int getCachedSpanGroupIndex(int param1Int1, int param1Int2) {
      if (!this.mCacheSpanGroupIndices)
        return getSpanGroupIndex(param1Int1, param1Int2); 
      int i = this.mSpanGroupIndexCache.get(param1Int1, -1);
      if (i != -1)
        return i; 
      param1Int2 = getSpanGroupIndex(param1Int1, param1Int2);
      this.mSpanGroupIndexCache.put(param1Int1, param1Int2);
      return param1Int2;
    }
    
    int getCachedSpanIndex(int param1Int1, int param1Int2) {
      if (!this.mCacheSpanIndices)
        return getSpanIndex(param1Int1, param1Int2); 
      int i = this.mSpanIndexCache.get(param1Int1, -1);
      if (i != -1)
        return i; 
      param1Int2 = getSpanIndex(param1Int1, param1Int2);
      this.mSpanIndexCache.put(param1Int1, param1Int2);
      return param1Int2;
    }
    
    public int getSpanGroupIndex(int param1Int1, int param1Int2) {
      // Byte code:
      //   0: aload_0
      //   1: getfield mCacheSpanGroupIndices : Z
      //   4: ifeq -> 86
      //   7: aload_0
      //   8: getfield mSpanGroupIndexCache : Landroid/util/SparseIntArray;
      //   11: iload_1
      //   12: invokestatic findFirstKeyLessThan : (Landroid/util/SparseIntArray;I)I
      //   15: istore_3
      //   16: iload_3
      //   17: iconst_m1
      //   18: if_icmpeq -> 86
      //   21: aload_0
      //   22: getfield mSpanGroupIndexCache : Landroid/util/SparseIntArray;
      //   25: iload_3
      //   26: invokevirtual get : (I)I
      //   29: istore #4
      //   31: iload_3
      //   32: iconst_1
      //   33: iadd
      //   34: istore #5
      //   36: aload_0
      //   37: iload_3
      //   38: iload_2
      //   39: invokevirtual getCachedSpanIndex : (II)I
      //   42: istore #6
      //   44: aload_0
      //   45: iload_3
      //   46: invokevirtual getSpanSize : (I)I
      //   49: iload #6
      //   51: iadd
      //   52: istore #7
      //   54: iload #7
      //   56: istore #6
      //   58: iload #4
      //   60: istore_3
      //   61: iload #5
      //   63: istore #8
      //   65: iload #7
      //   67: iload_2
      //   68: if_icmpne -> 94
      //   71: iload #4
      //   73: iconst_1
      //   74: iadd
      //   75: istore_3
      //   76: iconst_0
      //   77: istore #6
      //   79: iload #5
      //   81: istore #8
      //   83: goto -> 94
      //   86: iconst_0
      //   87: istore #6
      //   89: iconst_0
      //   90: istore_3
      //   91: iconst_0
      //   92: istore #8
      //   94: aload_0
      //   95: iload_1
      //   96: invokevirtual getSpanSize : (I)I
      //   99: istore #9
      //   101: iload #8
      //   103: istore #5
      //   105: iload #5
      //   107: iload_1
      //   108: if_icmpge -> 174
      //   111: aload_0
      //   112: iload #5
      //   114: invokevirtual getSpanSize : (I)I
      //   117: istore #4
      //   119: iload #6
      //   121: iload #4
      //   123: iadd
      //   124: istore #7
      //   126: iload #7
      //   128: iload_2
      //   129: if_icmpne -> 143
      //   132: iload_3
      //   133: iconst_1
      //   134: iadd
      //   135: istore #8
      //   137: iconst_0
      //   138: istore #6
      //   140: goto -> 165
      //   143: iload #7
      //   145: istore #6
      //   147: iload_3
      //   148: istore #8
      //   150: iload #7
      //   152: iload_2
      //   153: if_icmple -> 165
      //   156: iload_3
      //   157: iconst_1
      //   158: iadd
      //   159: istore #8
      //   161: iload #4
      //   163: istore #6
      //   165: iinc #5, 1
      //   168: iload #8
      //   170: istore_3
      //   171: goto -> 105
      //   174: iload_3
      //   175: istore_1
      //   176: iload #6
      //   178: iload #9
      //   180: iadd
      //   181: iload_2
      //   182: if_icmple -> 189
      //   185: iload_3
      //   186: iconst_1
      //   187: iadd
      //   188: istore_1
      //   189: iload_1
      //   190: ireturn
    }
    
    public int getSpanIndex(int param1Int1, int param1Int2) {
      // Byte code:
      //   0: aload_0
      //   1: iload_1
      //   2: invokevirtual getSpanSize : (I)I
      //   5: istore_3
      //   6: iload_3
      //   7: iload_2
      //   8: if_icmpne -> 13
      //   11: iconst_0
      //   12: ireturn
      //   13: aload_0
      //   14: getfield mCacheSpanIndices : Z
      //   17: ifeq -> 56
      //   20: aload_0
      //   21: getfield mSpanIndexCache : Landroid/util/SparseIntArray;
      //   24: iload_1
      //   25: invokestatic findFirstKeyLessThan : (Landroid/util/SparseIntArray;I)I
      //   28: istore #4
      //   30: iload #4
      //   32: iflt -> 56
      //   35: aload_0
      //   36: getfield mSpanIndexCache : Landroid/util/SparseIntArray;
      //   39: iload #4
      //   41: invokevirtual get : (I)I
      //   44: aload_0
      //   45: iload #4
      //   47: invokevirtual getSpanSize : (I)I
      //   50: iadd
      //   51: istore #5
      //   53: goto -> 121
      //   56: iconst_0
      //   57: istore #6
      //   59: iconst_0
      //   60: istore #5
      //   62: iload #6
      //   64: iload_1
      //   65: if_icmpge -> 130
      //   68: aload_0
      //   69: iload #6
      //   71: invokevirtual getSpanSize : (I)I
      //   74: istore #7
      //   76: iload #5
      //   78: iload #7
      //   80: iadd
      //   81: istore #8
      //   83: iload #8
      //   85: iload_2
      //   86: if_icmpne -> 99
      //   89: iconst_0
      //   90: istore #5
      //   92: iload #6
      //   94: istore #4
      //   96: goto -> 121
      //   99: iload #6
      //   101: istore #4
      //   103: iload #8
      //   105: istore #5
      //   107: iload #8
      //   109: iload_2
      //   110: if_icmple -> 121
      //   113: iload #7
      //   115: istore #5
      //   117: iload #6
      //   119: istore #4
      //   121: iload #4
      //   123: iconst_1
      //   124: iadd
      //   125: istore #6
      //   127: goto -> 62
      //   130: iload_3
      //   131: iload #5
      //   133: iadd
      //   134: iload_2
      //   135: if_icmpgt -> 141
      //   138: iload #5
      //   140: ireturn
      //   141: iconst_0
      //   142: ireturn
    }
    
    public abstract int getSpanSize(int param1Int);
    
    public void invalidateSpanGroupIndexCache() {
      this.mSpanGroupIndexCache.clear();
    }
    
    public void invalidateSpanIndexCache() {
      this.mSpanIndexCache.clear();
    }
    
    public boolean isSpanGroupIndexCacheEnabled() {
      return this.mCacheSpanGroupIndices;
    }
    
    public boolean isSpanIndexCacheEnabled() {
      return this.mCacheSpanIndices;
    }
    
    public void setSpanGroupIndexCacheEnabled(boolean param1Boolean) {
      if (!param1Boolean)
        this.mSpanGroupIndexCache.clear(); 
      this.mCacheSpanGroupIndices = param1Boolean;
    }
    
    public void setSpanIndexCacheEnabled(boolean param1Boolean) {
      if (!param1Boolean)
        this.mSpanGroupIndexCache.clear(); 
      this.mCacheSpanIndices = param1Boolean;
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/androidx/recyclerview/widget/GridLayoutManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */