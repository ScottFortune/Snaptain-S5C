package com.shizhefei.view.coolrefreshview;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import androidx.core.view.MotionEventCompat;
import androidx.core.view.NestedScrollingChild;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.NestedScrollingParent;
import androidx.core.view.NestedScrollingParentHelper;
import androidx.core.view.ViewCompat;
import androidx.core.widget.ScrollerCompat;
import com.shizhefei.view.coolrefreshview.header.DefaultHeader;

public class CoolRefreshView extends ViewGroup implements NestedScrollingParent, NestedScrollingChild {
  private static boolean DEBUG = false;
  
  private static IPullHeaderFactory HEADER_FACTORY = new IPullHeaderFactory() {
      public boolean isPinContent() {
        return false;
      }
      
      public PullHeader made(Context param1Context) {
        return (PullHeader)new DefaultHeader();
      }
    };
  
  private static final String LOG_TAG = "CoolRefreshView";
  
  public static final byte PULL_STATUS_COMPLETE = 5;
  
  public static final byte PULL_STATUS_INIT = 1;
  
  public static final byte PULL_STATUS_REFRESHING = 4;
  
  public static final byte PULL_STATUS_RESET = 3;
  
  public static final byte PULL_STATUS_TOUCH_MOVE = 2;
  
  private int mActivePointerId;
  
  private View mContentView;
  
  private View mHeaderView;
  
  private float mInitialDownY;
  
  private float mInitialMotionY;
  
  private boolean mIsBeingDragged;
  
  private boolean mIsPinContent = true;
  
  private float mLastMotionY;
  
  private boolean mNestedScrollInProgress;
  
  private NestedScrollingChildHelper mNestedScrollingChildHelper;
  
  private NestedScrollingParentHelper mNestedScrollingParentHelper;
  
  private final int[] mParentOffsetInWindow = new int[2];
  
  private final int[] mParentScrollConsumed = new int[2];
  
  private ProxyPullHeader mPullHandler;
  
  private boolean mRefreshing = false;
  
  private byte mStatus = (byte)1;
  
  private int mTopOffset = -1;
  
  private int mTouchSlop;
  
  private ScrollerHelper scrollerHelper;
  
  static {
    DEBUG = false;
  }
  
  public CoolRefreshView(Context paramContext) {
    super(paramContext);
    init();
  }
  
  public CoolRefreshView(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    init();
  }
  
  public CoolRefreshView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    init();
  }
  
  private void addHeadView() {
    this.mHeaderView = this.mPullHandler.createHeaderView(this);
    ViewGroup.LayoutParams layoutParams1 = this.mHeaderView.getLayoutParams();
    ViewGroup.LayoutParams layoutParams2 = layoutParams1;
    if (layoutParams1 == null)
      layoutParams2 = new ViewGroup.LayoutParams(-1, -2); 
    addView(this.mHeaderView, layoutParams2);
  }
  
  private boolean canChildScrollUp() {
    return this.mPullHandler.getConfig().contentCanScrollUp(this, getContentView());
  }
  
  private static int clamp(int paramInt1, int paramInt2, int paramInt3) {
    return (paramInt2 >= paramInt3 || paramInt1 < 0) ? 0 : ((paramInt2 + paramInt1 > paramInt3) ? (paramInt3 - paramInt2) : paramInt1);
  }
  
  private void finishSpinner() {
    int i = this.mPullHandler.getConfig().offsetToRefresh(this, this.mHeaderView);
    if (-this.scrollerHelper.getOffsetY() > i) {
      this.mStatus = (byte)4;
      setRefreshing(true);
    } else {
      reset(true);
    } 
  }
  
  private float getMotionEventY(MotionEvent paramMotionEvent, int paramInt) {
    paramInt = MotionEventCompat.findPointerIndex(paramMotionEvent, paramInt);
    return (paramInt < 0) ? -1.0F : MotionEventCompat.getY(paramMotionEvent, paramInt);
  }
  
  private void init() {
    this.mPullHandler = new ProxyPullHeader(HEADER_FACTORY.made(getContext()));
    this.mIsPinContent = HEADER_FACTORY.isPinContent();
    if (this.mIsPinContent) {
      this.scrollerHelper = new PinContentScroller();
    } else {
      this.scrollerHelper = new AllScroller();
    } 
    setWillNotDraw(false);
    addHeadView();
    this.mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    this.mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);
    this.mNestedScrollingChildHelper = new NestedScrollingChildHelper((View)this);
    setNestedScrollingEnabled(true);
  }
  
  private void onSecondaryPointerUp(MotionEvent paramMotionEvent) {
    int i = MotionEventCompat.getActionIndex(paramMotionEvent);
    if (MotionEventCompat.getPointerId(paramMotionEvent, i) == this.mActivePointerId) {
      if (i == 0) {
        i = 1;
      } else {
        i = 0;
      } 
      this.mActivePointerId = MotionEventCompat.getPointerId(paramMotionEvent, i);
    } 
  }
  
  private void postSetRefreshing(boolean paramBoolean) {
    if (paramBoolean) {
      if (!this.mRefreshing) {
        this.mStatus = (byte)4;
        this.mRefreshing = paramBoolean;
        this.mPullHandler.onRefreshing(this);
        int i = -this.mPullHandler.getConfig().offsetToKeepHeaderWhileLoading(this, this.mHeaderView);
        int j = this.scrollerHelper.getOffsetY();
        ScrollerHelper scrollerHelper = this.scrollerHelper;
        scrollerHelper.startScroll(scrollerHelper.getOffsetX(), this.scrollerHelper.getOffsetY(), 0, i - j);
      } 
    } else if (this.mRefreshing) {
      this.mStatus = (byte)5;
      this.mRefreshing = paramBoolean;
      this.mPullHandler.onPullRefreshComplete(this);
      int i = -this.scrollerHelper.getOffsetY();
      ScrollerHelper scrollerHelper = this.scrollerHelper;
      scrollerHelper.startScroll(scrollerHelper.getOffsetX(), this.scrollerHelper.getOffsetY(), 0, i);
    } 
  }
  
  private void reset(boolean paramBoolean) {
    this.mRefreshing = false;
    this.mStatus = (byte)3;
    this.mPullHandler.onReset(this, paramBoolean);
    int i = -this.scrollerHelper.getOffsetY();
    ScrollerHelper scrollerHelper = this.scrollerHelper;
    scrollerHelper.startScroll(scrollerHelper.getOffsetX(), this.scrollerHelper.getOffsetY(), 0, i);
  }
  
  public static void setPullHeaderFactory(IPullHeaderFactory paramIPullHeaderFactory) {
    HEADER_FACTORY = paramIPullHeaderFactory;
  }
  
  private void startDragging(float paramFloat) {
    float f = this.mInitialDownY;
    int i = this.mTouchSlop;
    if (paramFloat - f > i && !this.mIsBeingDragged) {
      this.mInitialMotionY = f + i;
      this.mIsBeingDragged = true;
    } 
  }
  
  private void touchMove(int paramInt) {
    if (!this.scrollerHelper.isFinished())
      this.scrollerHelper.abortAnimation(); 
    int i = this.scrollerHelper.getOffsetX();
    int j = this.scrollerHelper.getOffsetY();
    int k = this.mPullHandler.getConfig().totalDistance(this, this.mHeaderView);
    int m = -j;
    if (-paramInt + m < k) {
      if (this.mStatus != 2) {
        this.mStatus = (byte)2;
        this.mPullHandler.onPullBegin(this);
      } 
      paramInt = this.mPullHandler.getConfig().dispatchTouchMove(this, paramInt, m, k);
      this.scrollerHelper.overScrollByCompat(0, paramInt, i, j, 0, 0, 0, k);
    } 
  }
  
  public void addOnPullListener(OnPullListener paramOnPullListener) {
    this.mPullHandler.addListener(paramOnPullListener);
  }
  
  public void addView(View paramView) {
    if (getChildCount() <= 1) {
      super.addView(paramView);
      return;
    } 
    throw new IllegalStateException("CoolRefreshView can host only one direct child");
  }
  
  public void addView(View paramView, int paramInt) {
    if (getChildCount() <= 1) {
      super.addView(paramView, paramInt);
      return;
    } 
    throw new IllegalStateException("CoolRefreshView can host only one direct child");
  }
  
  public void addView(View paramView, int paramInt, ViewGroup.LayoutParams paramLayoutParams) {
    if (getChildCount() <= 1) {
      super.addView(paramView, paramInt, paramLayoutParams);
      return;
    } 
    throw new IllegalStateException("CoolRefreshView can host only one direct child");
  }
  
  public void addView(View paramView, ViewGroup.LayoutParams paramLayoutParams) {
    if (getChildCount() <= 1) {
      super.addView(paramView, paramLayoutParams);
      return;
    } 
    throw new IllegalStateException("CoolRefreshView can host only one direct child");
  }
  
  protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    return paramLayoutParams instanceof ViewGroup.MarginLayoutParams;
  }
  
  public void computeScroll() {
    super.computeScroll();
    this.scrollerHelper.computeScroll();
  }
  
  public boolean dispatchNestedFling(float paramFloat1, float paramFloat2, boolean paramBoolean) {
    return this.mNestedScrollingChildHelper.dispatchNestedFling(paramFloat1, paramFloat2, paramBoolean);
  }
  
  public boolean dispatchNestedPreFling(float paramFloat1, float paramFloat2) {
    return this.mNestedScrollingChildHelper.dispatchNestedPreFling(paramFloat1, paramFloat2);
  }
  
  public boolean dispatchNestedPreScroll(int paramInt1, int paramInt2, int[] paramArrayOfint1, int[] paramArrayOfint2) {
    return this.mNestedScrollingChildHelper.dispatchNestedPreScroll(paramInt1, paramInt2, paramArrayOfint1, paramArrayOfint2);
  }
  
  public boolean dispatchNestedScroll(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfint) {
    return this.mNestedScrollingChildHelper.dispatchNestedScroll(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfint);
  }
  
  protected ViewGroup.MarginLayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    return new ViewGroup.MarginLayoutParams(paramLayoutParams);
  }
  
  public View getContentView() {
    if (this.mContentView == null) {
      int i = getChildCount();
      for (byte b = 0; b < i; b++) {
        View view = getChildAt(b);
        if (view != this.mHeaderView) {
          this.mContentView = view;
          break;
        } 
      } 
    } 
    return this.mContentView;
  }
  
  public int getNestedScrollAxes() {
    return this.mNestedScrollingParentHelper.getNestedScrollAxes();
  }
  
  public boolean hasNestedScrollingParent() {
    return this.mNestedScrollingChildHelper.hasNestedScrollingParent();
  }
  
  public boolean isNestedScrollingEnabled() {
    return this.mNestedScrollingChildHelper.isNestedScrollingEnabled();
  }
  
  public boolean isRefreshing() {
    return this.mRefreshing;
  }
  
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    reset(false);
  }
  
  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent) {
    int i = MotionEventCompat.getActionMasked(paramMotionEvent);
    if (!isEnabled() || canChildScrollUp() || this.mRefreshing || this.mNestedScrollInProgress)
      return false; 
    if (i != 0) {
      if (i != 1)
        if (i != 2) {
          if (i != 3) {
            if (i == 6)
              onSecondaryPointerUp(paramMotionEvent); 
            return this.mIsBeingDragged;
          } 
        } else {
          i = this.mActivePointerId;
          if (i == -1) {
            Log.e("CoolRefreshView", "Got ACTION_MOVE event but don't have an active pointer id.");
            return false;
          } 
          i = paramMotionEvent.findPointerIndex(i);
          if (i < 0)
            return false; 
          startDragging(paramMotionEvent.getY(i));
          return this.mIsBeingDragged;
        }  
      this.mIsBeingDragged = false;
      this.mActivePointerId = -1;
    } else {
      this.mActivePointerId = paramMotionEvent.getPointerId(0);
      this.mIsBeingDragged = false;
      i = paramMotionEvent.findPointerIndex(this.mActivePointerId);
      if (i < 0)
        return false; 
      this.mInitialDownY = paramMotionEvent.getY(i);
      this.mLastMotionY = getMotionEventY(paramMotionEvent, i);
    } 
    return this.mIsBeingDragged;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    View view = getContentView();
    paramInt2 = this.scrollerHelper.getAlreadyOffset();
    paramInt3 = this.mPullHandler.getConfig().headerViewLayoutOffset(this, this.mHeaderView);
    ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams)this.mHeaderView.getLayoutParams();
    paramInt1 = getPaddingLeft() + marginLayoutParams.leftMargin;
    paramInt3 = marginLayoutParams.topMargin - paramInt3 - paramInt2;
    paramInt4 = this.mHeaderView.getMeasuredHeight();
    paramInt2 = this.mHeaderView.getMeasuredWidth();
    this.mHeaderView.layout(paramInt1, paramInt3, paramInt2 + paramInt1, paramInt3 + paramInt4);
    this.mTopOffset = -marginLayoutParams.topMargin - paramInt4;
    marginLayoutParams = (ViewGroup.MarginLayoutParams)view.getLayoutParams();
    paramInt3 = getPaddingLeft() + marginLayoutParams.leftMargin;
    paramInt1 = getPaddingTop() + marginLayoutParams.topMargin;
    paramInt2 = view.getMeasuredHeight();
    view.layout(paramInt3, paramInt1, view.getMeasuredWidth() + paramInt3, paramInt2 + paramInt1);
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    int i = getChildCount();
    int j = 0;
    int k = 0;
    int m = 0;
    int n;
    for (n = 0; j < i; n = i4) {
      View view = getChildAt(j);
      int i2 = k;
      int i3 = m;
      int i4 = n;
      if (view.getVisibility() != 8) {
        measureChildWithMargins(view, paramInt1, 0, paramInt2, 0);
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams)view.getLayoutParams();
        i3 = Math.max(m, view.getMeasuredWidth() + marginLayoutParams.leftMargin + marginLayoutParams.rightMargin);
        i2 = k + view.getMeasuredHeight() + marginLayoutParams.topMargin + marginLayoutParams.bottomMargin;
        i4 = ViewCompat.combineMeasuredStates(n, ViewCompat.getMeasuredState(view));
      } 
      j++;
      k = i2;
      m = i3;
    } 
    int i1 = Math.max(k, getSuggestedMinimumHeight());
    j = Math.max(m, getSuggestedMinimumWidth());
    if (DEBUG) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("onMeasure: ");
      stringBuilder.append(View.MeasureSpec.getSize(paramInt2));
      stringBuilder.append(" maxHeight:");
      stringBuilder.append(i1);
      stringBuilder.append(" mHeaderView：");
      stringBuilder.append(this.mHeaderView.getMeasuredHeight());
      Log.d("wsx", stringBuilder.toString());
    } 
    setMeasuredDimension(ViewCompat.resolveSizeAndState(j, paramInt1, n), ViewCompat.resolveSizeAndState(i1, paramInt2, n << 16));
  }
  
  public boolean onNestedFling(View paramView, float paramFloat1, float paramFloat2, boolean paramBoolean) {
    return dispatchNestedFling(paramFloat1, paramFloat2, paramBoolean);
  }
  
  public boolean onNestedPreFling(View paramView, float paramFloat1, float paramFloat2) {
    return dispatchNestedPreFling(paramFloat1, paramFloat2);
  }
  
  public void onNestedPreScroll(View paramView, int paramInt1, int paramInt2, int[] paramArrayOfint) {
    int i = this.scrollerHelper.getOffsetY();
    if (paramInt2 > 0 && i < 0) {
      i = Math.abs(i);
      if (paramInt2 > i) {
        paramArrayOfint[1] = paramInt2 - i;
      } else {
        paramArrayOfint[1] = paramInt2;
      } 
      touchMove(paramArrayOfint[1]);
    } 
    int[] arrayOfInt = this.mParentScrollConsumed;
    if (dispatchNestedPreScroll(paramInt1 - paramArrayOfint[0], paramInt2 - paramArrayOfint[1], arrayOfInt, (int[])null)) {
      paramArrayOfint[0] = paramArrayOfint[0] + arrayOfInt[0];
      paramArrayOfint[1] = paramArrayOfint[1] + arrayOfInt[1];
    } 
  }
  
  public void onNestedScroll(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    dispatchNestedScroll(paramInt1, paramInt2, paramInt3, paramInt4, this.mParentOffsetInWindow);
    paramInt1 = paramInt4 + this.mParentOffsetInWindow[1];
    if (paramInt1 < 0 && !canChildScrollUp())
      touchMove(paramInt1); 
  }
  
  public void onNestedScrollAccepted(View paramView1, View paramView2, int paramInt) {
    this.mNestedScrollingParentHelper.onNestedScrollAccepted(paramView1, paramView2, paramInt);
    startNestedScroll(paramInt & 0x2);
    this.mNestedScrollInProgress = true;
  }
  
  protected void onOverScrolled(int paramInt1, int paramInt2, boolean paramBoolean1, boolean paramBoolean2) {
    super.scrollTo(paramInt1, paramInt2);
  }
  
  public boolean onStartNestedScroll(View paramView1, View paramView2, int paramInt) {
    boolean bool;
    if (isEnabled() && !this.mRefreshing && (paramInt & 0x2) != 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void onStopNestedScroll(View paramView) {
    this.mNestedScrollingParentHelper.onStopNestedScroll(paramView);
    this.mNestedScrollInProgress = false;
    finishSpinner();
    stopNestedScroll();
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    int i = MotionEventCompat.getActionMasked(paramMotionEvent);
    if (!isEnabled() || canChildScrollUp() || this.mRefreshing || this.mNestedScrollInProgress)
      return false; 
    if (i != 0) {
      if (i != 1) {
        if (i != 2) {
          if (i != 3) {
            if (i != 5) {
              if (i == 6)
                onSecondaryPointerUp(paramMotionEvent); 
            } else {
              i = MotionEventCompat.getActionIndex(paramMotionEvent);
              if (i < 0) {
                Log.e("CoolRefreshView", "Got ACTION_POINTER_DOWN event but have an invalid action index.");
                return false;
              } 
              this.mActivePointerId = paramMotionEvent.getPointerId(i);
            } 
          } else {
            return false;
          } 
        } else {
          i = paramMotionEvent.findPointerIndex(this.mActivePointerId);
          if (i < 0) {
            Log.e("CoolRefreshView", "Got ACTION_MOVE event but have an invalid active pointer id.");
            return false;
          } 
          float f = paramMotionEvent.getY(i);
          startDragging(f);
          if (this.mIsBeingDragged) {
            touchMove((int)(this.mLastMotionY - f));
            this.mLastMotionY = f;
          } 
        } 
      } else {
        if (paramMotionEvent.findPointerIndex(this.mActivePointerId) < 0) {
          Log.e("CoolRefreshView", "Got ACTION_UP event but don't have an active pointer id.");
          return false;
        } 
        if (this.mIsBeingDragged) {
          this.mIsBeingDragged = false;
          finishSpinner();
        } 
        this.mActivePointerId = -1;
        return false;
      } 
    } else {
      this.mActivePointerId = paramMotionEvent.getPointerId(0);
      this.mIsBeingDragged = false;
      this.mLastMotionY = getMotionEventY(paramMotionEvent, this.mActivePointerId);
    } 
    return true;
  }
  
  public void removeOnPullListener(OnPullListener paramOnPullListener) {
    this.mPullHandler.removeListener(paramOnPullListener);
  }
  
  public void requestDisallowInterceptTouchEvent(boolean paramBoolean) {
    View view = getContentView();
    if ((Build.VERSION.SDK_INT >= 21 || !(view instanceof android.widget.AbsListView)) && (view == null || ViewCompat.isNestedScrollingEnabled(view)))
      super.requestDisallowInterceptTouchEvent(paramBoolean); 
  }
  
  public void scrollTo(int paramInt1, int paramInt2) {
    if (getChildCount() > 0) {
      View view = getChildAt(0);
      paramInt1 = clamp(paramInt1, getWidth() - getPaddingRight() - getPaddingLeft(), view.getWidth());
      paramInt2 = clamp(paramInt2, getHeight() - getPaddingBottom() - getPaddingTop(), view.getHeight());
      if (paramInt1 != getScrollX() || paramInt2 != getScrollY())
        super.scrollTo(paramInt1, paramInt2); 
    } 
  }
  
  public void setEnabled(boolean paramBoolean) {
    super.setEnabled(paramBoolean);
    if (!paramBoolean)
      reset(false); 
  }
  
  public void setNestedScrollingEnabled(boolean paramBoolean) {
    this.mNestedScrollingChildHelper.setNestedScrollingEnabled(paramBoolean);
  }
  
  public void setPullHeader(PullHeader paramPullHeader) {
    setPullHeader(paramPullHeader, false);
  }
  
  public void setPullHeader(PullHeader paramPullHeader, boolean paramBoolean) {
    if (!this.scrollerHelper.isFinished())
      this.scrollerHelper.abortAnimation(); 
    if (this.mIsPinContent != paramBoolean) {
      if (paramBoolean) {
        this.scrollerHelper = new PinContentScroller();
      } else {
        this.scrollerHelper = new AllScroller();
      } 
      this.mIsPinContent = paramBoolean;
    } 
    this.mPullHandler.setPullHandler(paramPullHeader);
    removeView(this.mHeaderView);
    addHeadView();
  }
  
  public void setRefreshing(final boolean refreshing) {
    if (getWidth() == 0) {
      post(new Runnable() {
            public void run() {
              CoolRefreshView.this.postSetRefreshing(refreshing);
            }
          });
    } else {
      postSetRefreshing(refreshing);
    } 
  }
  
  public boolean startNestedScroll(int paramInt) {
    return this.mNestedScrollingChildHelper.startNestedScroll(paramInt);
  }
  
  public void stopNestedScroll() {
    this.mNestedScrollingChildHelper.stopNestedScroll();
  }
  
  private class AllScroller extends ScrollerHelper {
    private AllScroller() {}
    
    public void abortAnimation() {
      this.mScroller.abortAnimation();
    }
    
    public void computeScroll() {
      if (this.mScroller.computeScrollOffset()) {
        int i = this.mScroller.getCurrY();
        int j = this.mScroller.getCurrY();
        int k = getOffsetX();
        int m = getOffsetY();
        overScrollByCompat(i - k, j - m, k, m, 0, 0, 0, CoolRefreshView.this.mHeaderView.getMeasuredHeight());
      } 
    }
    
    public int getOffsetX() {
      return CoolRefreshView.this.getScrollX();
    }
    
    public int getOffsetY() {
      return CoolRefreshView.this.getScrollY();
    }
    
    void overScrollByCompat(int param1Int1, int param1Int2, int param1Int3, int param1Int4, int param1Int5, int param1Int6, int param1Int7, int param1Int8) {
      // Byte code:
      //   0: iload_3
      //   1: iload_1
      //   2: iadd
      //   3: istore_1
      //   4: iload #4
      //   6: iload_2
      //   7: iadd
      //   8: istore_3
      //   9: iload #7
      //   11: ineg
      //   12: istore_2
      //   13: iload #7
      //   15: iload #5
      //   17: iadd
      //   18: istore #7
      //   20: iload #8
      //   22: ineg
      //   23: istore #4
      //   25: iload #6
      //   27: iload #8
      //   29: iadd
      //   30: istore #5
      //   32: iconst_1
      //   33: istore #9
      //   35: iload_1
      //   36: iload #7
      //   38: if_icmple -> 52
      //   41: iload #7
      //   43: istore_1
      //   44: iconst_1
      //   45: istore #10
      //   47: iload_1
      //   48: istore_2
      //   49: goto -> 67
      //   52: iload_1
      //   53: iload_2
      //   54: if_icmpge -> 62
      //   57: iload_2
      //   58: istore_1
      //   59: goto -> 44
      //   62: iload_1
      //   63: istore_2
      //   64: iconst_0
      //   65: istore #10
      //   67: iload_3
      //   68: iload #5
      //   70: if_icmple -> 79
      //   73: iload #5
      //   75: istore_1
      //   76: goto -> 96
      //   79: iload_3
      //   80: iload #4
      //   82: if_icmpge -> 91
      //   85: iload #4
      //   87: istore_1
      //   88: goto -> 96
      //   91: iconst_0
      //   92: istore #9
      //   94: iload_3
      //   95: istore_1
      //   96: iload_1
      //   97: istore_3
      //   98: iload_1
      //   99: ifle -> 104
      //   102: iconst_0
      //   103: istore_3
      //   104: iload_3
      //   105: aload_0
      //   106: invokevirtual getOffsetY : ()I
      //   109: isub
      //   110: istore_1
      //   111: iload_3
      //   112: ineg
      //   113: istore #4
      //   115: iload_1
      //   116: ifeq -> 162
      //   119: aload_0
      //   120: getfield this$0 : Lcom/shizhefei/view/coolrefreshview/CoolRefreshView;
      //   123: iload_2
      //   124: iload_3
      //   125: iload #10
      //   127: iload #9
      //   129: invokevirtual onOverScrolled : (IIZZ)V
      //   132: aload_0
      //   133: getfield this$0 : Lcom/shizhefei/view/coolrefreshview/CoolRefreshView;
      //   136: invokestatic access$500 : (Lcom/shizhefei/view/coolrefreshview/CoolRefreshView;)Lcom/shizhefei/view/coolrefreshview/ProxyPullHeader;
      //   139: astore #11
      //   141: aload_0
      //   142: getfield this$0 : Lcom/shizhefei/view/coolrefreshview/CoolRefreshView;
      //   145: astore #12
      //   147: aload #11
      //   149: aload #12
      //   151: aload #12
      //   153: invokestatic access$400 : (Lcom/shizhefei/view/coolrefreshview/CoolRefreshView;)B
      //   156: iload_1
      //   157: iload #4
      //   159: invokevirtual onPositionChange : (Lcom/shizhefei/view/coolrefreshview/CoolRefreshView;III)V
      //   162: aload_0
      //   163: getfield mScroller : Landroidx/core/widget/ScrollerCompat;
      //   166: invokevirtual isFinished : ()Z
      //   169: ifne -> 196
      //   172: aload_0
      //   173: getfield mScroller : Landroidx/core/widget/ScrollerCompat;
      //   176: invokevirtual getFinalY : ()I
      //   179: aload_0
      //   180: getfield mScroller : Landroidx/core/widget/ScrollerCompat;
      //   183: invokevirtual getCurrY : ()I
      //   186: if_icmpeq -> 196
      //   189: aload_0
      //   190: getfield this$0 : Lcom/shizhefei/view/coolrefreshview/CoolRefreshView;
      //   193: invokestatic postInvalidateOnAnimation : (Landroid/view/View;)V
      //   196: invokestatic access$600 : ()Z
      //   199: ifeq -> 342
      //   202: new java/lang/StringBuilder
      //   205: dup
      //   206: invokespecial <init> : ()V
      //   209: astore #11
      //   211: aload #11
      //   213: ldc ' bottom:'
      //   215: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   218: pop
      //   219: aload #11
      //   221: aload_0
      //   222: getfield this$0 : Lcom/shizhefei/view/coolrefreshview/CoolRefreshView;
      //   225: invokestatic access$300 : (Lcom/shizhefei/view/coolrefreshview/CoolRefreshView;)Landroid/view/View;
      //   228: invokevirtual getBottom : ()I
      //   231: invokevirtual append : (I)Ljava/lang/StringBuilder;
      //   234: pop
      //   235: aload #11
      //   237: ldc ' mHeaderView.getTop:'
      //   239: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   242: pop
      //   243: aload #11
      //   245: aload_0
      //   246: getfield this$0 : Lcom/shizhefei/view/coolrefreshview/CoolRefreshView;
      //   249: invokestatic access$300 : (Lcom/shizhefei/view/coolrefreshview/CoolRefreshView;)Landroid/view/View;
      //   252: invokevirtual getTop : ()I
      //   255: invokevirtual append : (I)Ljava/lang/StringBuilder;
      //   258: pop
      //   259: aload #11
      //   261: ldc ' scrollY:'
      //   263: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   266: pop
      //   267: aload #11
      //   269: aload_0
      //   270: getfield this$0 : Lcom/shizhefei/view/coolrefreshview/CoolRefreshView;
      //   273: invokevirtual getScrollY : ()I
      //   276: invokevirtual append : (I)Ljava/lang/StringBuilder;
      //   279: pop
      //   280: aload #11
      //   282: ldc ' newScrollY:'
      //   284: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   287: pop
      //   288: aload #11
      //   290: iload_3
      //   291: invokevirtual append : (I)Ljava/lang/StringBuilder;
      //   294: pop
      //   295: aload #11
      //   297: ldc ' deltaY:'
      //   299: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   302: pop
      //   303: aload #11
      //   305: iload_1
      //   306: invokevirtual append : (I)Ljava/lang/StringBuilder;
      //   309: pop
      //   310: aload #11
      //   312: ldc ' finalY:'
      //   314: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   317: pop
      //   318: aload #11
      //   320: aload_0
      //   321: getfield mScroller : Landroidx/core/widget/ScrollerCompat;
      //   324: invokevirtual getFinalY : ()I
      //   327: invokevirtual append : (I)Ljava/lang/StringBuilder;
      //   330: pop
      //   331: ldc 'zzzz'
      //   333: aload #11
      //   335: invokevirtual toString : ()Ljava/lang/String;
      //   338: invokestatic d : (Ljava/lang/String;Ljava/lang/String;)I
      //   341: pop
      //   342: return
    }
    
    public void startScroll(int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
      this.mScroller.startScroll(param1Int1, param1Int2, param1Int3, param1Int4, 800);
      ViewCompat.postInvalidateOnAnimation((View)CoolRefreshView.this);
    }
  }
  
  private class PinContentScroller extends ScrollerHelper {
    private Runnable runnable = new Runnable() {
        public void run() {
          if (CoolRefreshView.PinContentScroller.this.mScroller.computeScrollOffset()) {
            int i = CoolRefreshView.PinContentScroller.this.mScroller.getCurrY();
            int j = CoolRefreshView.PinContentScroller.this.mScroller.getCurrY();
            int k = CoolRefreshView.PinContentScroller.this.getOffsetX();
            int m = CoolRefreshView.PinContentScroller.this.getOffsetY();
            CoolRefreshView.PinContentScroller pinContentScroller = CoolRefreshView.PinContentScroller.this;
            pinContentScroller.overScrollByCompat(i - k, j - m, k, m, 0, 0, 0, CoolRefreshView.this.mHeaderView.getMeasuredHeight());
            if (!CoolRefreshView.PinContentScroller.this.mScroller.isFinished() && CoolRefreshView.PinContentScroller.this.mScroller.getFinalY() != CoolRefreshView.PinContentScroller.this.mScroller.getCurrY())
              CoolRefreshView.this.post(this); 
          } 
        }
      };
    
    private PinContentScroller() {}
    
    public void abortAnimation() {
      this.mScroller.abortAnimation();
      CoolRefreshView.this.removeCallbacks(this.runnable);
    }
    
    public int getAlreadyOffset() {
      return -CoolRefreshView.this.mHeaderView.getBottom();
    }
    
    public int getOffsetX() {
      return CoolRefreshView.this.mHeaderView.getLeft();
    }
    
    public int getOffsetY() {
      return -CoolRefreshView.this.mHeaderView.getBottom();
    }
    
    void overScrollByCompat(int param1Int1, int param1Int2, int param1Int3, int param1Int4, int param1Int5, int param1Int6, int param1Int7, int param1Int8) {
      param1Int1 = param1Int4 + param1Int2;
      param1Int2 = -param1Int7;
      param1Int2 = -param1Int8;
      param1Int3 = param1Int8 + param1Int6;
      if (param1Int1 > param1Int3) {
        param1Int1 = param1Int3;
      } else if (param1Int1 < param1Int2) {
        param1Int1 = param1Int2;
      } 
      param1Int2 = param1Int1;
      if (param1Int1 > 0)
        param1Int2 = 0; 
      param1Int3 = param1Int2 - getOffsetY();
      param1Int1 = -param1Int2;
      CoolRefreshView coolRefreshView = CoolRefreshView.this;
      if (coolRefreshView.indexOfChild(coolRefreshView.mHeaderView) != CoolRefreshView.this.getChildCount() - 1) {
        coolRefreshView = CoolRefreshView.this;
        coolRefreshView.bringChildToFront(coolRefreshView.mHeaderView);
        if (CoolRefreshView.DEBUG)
          Log.d("zzzz", "bringChildToFront:"); 
      } 
      if (param1Int3 != 0) {
        ViewCompat.offsetTopAndBottom(CoolRefreshView.this.mHeaderView, -param1Int3);
        ProxyPullHeader proxyPullHeader = CoolRefreshView.this.mPullHandler;
        CoolRefreshView coolRefreshView1 = CoolRefreshView.this;
        proxyPullHeader.onPositionChange(coolRefreshView1, coolRefreshView1.mStatus, param1Int3, param1Int1);
      } 
      if (Build.VERSION.SDK_INT < 11)
        ViewCompat.postInvalidateOnAnimation((View)CoolRefreshView.this); 
      if (CoolRefreshView.DEBUG) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" bottom:");
        stringBuilder.append(CoolRefreshView.this.mHeaderView.getBottom());
        stringBuilder.append(" mHeaderView.getTop:");
        stringBuilder.append(CoolRefreshView.this.mHeaderView.getTop());
        stringBuilder.append(" scrollY:");
        stringBuilder.append(CoolRefreshView.this.getScrollY());
        stringBuilder.append(" newScrollY:");
        stringBuilder.append(param1Int2);
        stringBuilder.append(" deltaY:");
        stringBuilder.append(param1Int3);
        stringBuilder.append(" finalY:");
        stringBuilder.append(this.mScroller.getFinalY());
        Log.d("zzzz", stringBuilder.toString());
      } 
    }
    
    public void startScroll(int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
      CoolRefreshView.this.removeCallbacks(this.runnable);
      this.mScroller.startScroll(param1Int1, param1Int2, param1Int3, param1Int4, 800);
      CoolRefreshView.this.post(this.runnable);
    }
  }
  
  class null implements Runnable {
    public void run() {
      if (this.this$1.mScroller.computeScrollOffset()) {
        int i = this.this$1.mScroller.getCurrY();
        int j = this.this$1.mScroller.getCurrY();
        int k = this.this$1.getOffsetX();
        int m = this.this$1.getOffsetY();
        CoolRefreshView.PinContentScroller pinContentScroller = this.this$1;
        pinContentScroller.overScrollByCompat(i - k, j - m, k, m, 0, 0, 0, CoolRefreshView.this.mHeaderView.getMeasuredHeight());
        if (!this.this$1.mScroller.isFinished() && this.this$1.mScroller.getFinalY() != this.this$1.mScroller.getCurrY())
          CoolRefreshView.this.post(this); 
      } 
    }
  }
  
  private abstract class ScrollerHelper {
    protected final ScrollerCompat mScroller = ScrollerCompat.create(CoolRefreshView.this.getContext(), null);
    
    public abstract void abortAnimation();
    
    public void computeScroll() {}
    
    public int getAlreadyOffset() {
      return 0;
    }
    
    public abstract int getOffsetX();
    
    public abstract int getOffsetY();
    
    public boolean isFinished() {
      return this.mScroller.isFinished();
    }
    
    abstract void overScrollByCompat(int param1Int1, int param1Int2, int param1Int3, int param1Int4, int param1Int5, int param1Int6, int param1Int7, int param1Int8);
    
    public abstract void startScroll(int param1Int1, int param1Int2, int param1Int3, int param1Int4);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/shizhefei/view/coolrefreshview/CoolRefreshView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */