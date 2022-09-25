package androidx.recyclerview.widget;

import android.animation.LayoutTransition;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.Observable;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.FocusFinder;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.Interpolator;
import android.widget.EdgeEffect;
import android.widget.OverScroller;
import androidx.core.os.TraceCompat;
import androidx.core.util.Preconditions;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.MotionEventCompat;
import androidx.core.view.NestedScrollingChild2;
import androidx.core.view.NestedScrollingChild3;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.ScrollingView;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewConfigurationCompat;
import androidx.core.view.accessibility.AccessibilityEventCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.customview.view.AbsSavedState;
import androidx.recyclerview.R;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecyclerView extends ViewGroup implements ScrollingView, NestedScrollingChild2, NestedScrollingChild3 {
  static {
    if (Build.VERSION.SDK_INT == 18 || Build.VERSION.SDK_INT == 19 || Build.VERSION.SDK_INT == 20) {
      bool = true;
    } else {
      bool = false;
    } 
    FORCE_INVALIDATE_DISPLAY_LIST = bool;
    if (Build.VERSION.SDK_INT >= 23) {
      bool = true;
    } else {
      bool = false;
    } 
    ALLOW_SIZE_IN_UNSPECIFIED_SPEC = bool;
    if (Build.VERSION.SDK_INT >= 16) {
      bool = true;
    } else {
      bool = false;
    } 
    POST_UPDATES_ON_ANIMATION = bool;
    if (Build.VERSION.SDK_INT >= 21) {
      bool = true;
    } else {
      bool = false;
    } 
    ALLOW_THREAD_GAP_WORK = bool;
    if (Build.VERSION.SDK_INT <= 15) {
      bool = true;
    } else {
      bool = false;
    } 
    FORCE_ABS_FOCUS_SEARCH_DIRECTION = bool;
    if (Build.VERSION.SDK_INT <= 15) {
      bool = true;
    } else {
      bool = false;
    } 
    IGNORE_DETACHED_FOCUSED_CHILD = bool;
    LAYOUT_MANAGER_CONSTRUCTOR_SIGNATURE = new Class[] { Context.class, AttributeSet.class, int.class, int.class };
    sQuinticInterpolator = new Interpolator() {
        public float getInterpolation(float param1Float) {
          param1Float--;
          return param1Float * param1Float * param1Float * param1Float * param1Float + 1.0F;
        }
      };
  }
  
  public RecyclerView(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public RecyclerView(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, R.attr.recyclerViewStyle);
  }
  
  public RecyclerView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    GapWorker.LayoutPrefetchRegistryImpl layoutPrefetchRegistryImpl;
    this.mObserver = new RecyclerViewDataObserver();
    this.mRecycler = new Recycler();
    this.mViewInfoStore = new ViewInfoStore();
    this.mUpdateChildViewsRunnable = new Runnable() {
        public void run() {
          if (RecyclerView.this.mFirstLayoutComplete && !RecyclerView.this.isLayoutRequested()) {
            if (!RecyclerView.this.mIsAttached) {
              RecyclerView.this.requestLayout();
              return;
            } 
            if (RecyclerView.this.mLayoutSuppressed) {
              RecyclerView.this.mLayoutWasDefered = true;
              return;
            } 
            RecyclerView.this.consumePendingUpdateOperations();
          } 
        }
      };
    this.mTempRect = new Rect();
    this.mTempRect2 = new Rect();
    this.mTempRectF = new RectF();
    this.mItemDecorations = new ArrayList<ItemDecoration>();
    this.mOnItemTouchListeners = new ArrayList<OnItemTouchListener>();
    this.mInterceptRequestLayoutDepth = 0;
    this.mDataSetHasChangedAfterLayout = false;
    this.mDispatchItemsChangedEvent = false;
    this.mLayoutOrScrollCounter = 0;
    this.mDispatchScrollCounter = 0;
    this.mEdgeEffectFactory = new EdgeEffectFactory();
    this.mItemAnimator = new DefaultItemAnimator();
    this.mScrollState = 0;
    this.mScrollPointerId = -1;
    this.mScaledHorizontalScrollFactor = Float.MIN_VALUE;
    this.mScaledVerticalScrollFactor = Float.MIN_VALUE;
    boolean bool1 = true;
    this.mPreserveFocusAfterLayout = true;
    this.mViewFlinger = new ViewFlinger();
    if (ALLOW_THREAD_GAP_WORK) {
      layoutPrefetchRegistryImpl = new GapWorker.LayoutPrefetchRegistryImpl();
    } else {
      layoutPrefetchRegistryImpl = null;
    } 
    this.mPrefetchRegistry = layoutPrefetchRegistryImpl;
    this.mState = new State();
    this.mItemsAddedOrRemoved = false;
    this.mItemsChanged = false;
    this.mItemAnimatorListener = new ItemAnimatorRestoreListener();
    this.mPostedAnimatorRunner = false;
    this.mMinMaxLayoutPositions = new int[2];
    this.mScrollOffset = new int[2];
    this.mNestedOffsets = new int[2];
    this.mReusableIntPair = new int[2];
    this.mPendingAccessibilityImportanceChange = new ArrayList<ViewHolder>();
    this.mItemAnimatorRunner = new Runnable() {
        public void run() {
          if (RecyclerView.this.mItemAnimator != null)
            RecyclerView.this.mItemAnimator.runPendingAnimations(); 
          RecyclerView.this.mPostedAnimatorRunner = false;
        }
      };
    this.mViewInfoProcessCallback = new ViewInfoStore.ProcessCallback() {
        public void processAppeared(RecyclerView.ViewHolder param1ViewHolder, RecyclerView.ItemAnimator.ItemHolderInfo param1ItemHolderInfo1, RecyclerView.ItemAnimator.ItemHolderInfo param1ItemHolderInfo2) {
          RecyclerView.this.animateAppearance(param1ViewHolder, param1ItemHolderInfo1, param1ItemHolderInfo2);
        }
        
        public void processDisappeared(RecyclerView.ViewHolder param1ViewHolder, RecyclerView.ItemAnimator.ItemHolderInfo param1ItemHolderInfo1, RecyclerView.ItemAnimator.ItemHolderInfo param1ItemHolderInfo2) {
          RecyclerView.this.mRecycler.unscrapView(param1ViewHolder);
          RecyclerView.this.animateDisappearance(param1ViewHolder, param1ItemHolderInfo1, param1ItemHolderInfo2);
        }
        
        public void processPersistent(RecyclerView.ViewHolder param1ViewHolder, RecyclerView.ItemAnimator.ItemHolderInfo param1ItemHolderInfo1, RecyclerView.ItemAnimator.ItemHolderInfo param1ItemHolderInfo2) {
          param1ViewHolder.setIsRecyclable(false);
          if (RecyclerView.this.mDataSetHasChangedAfterLayout) {
            if (RecyclerView.this.mItemAnimator.animateChange(param1ViewHolder, param1ViewHolder, param1ItemHolderInfo1, param1ItemHolderInfo2))
              RecyclerView.this.postAnimationRunner(); 
          } else if (RecyclerView.this.mItemAnimator.animatePersistence(param1ViewHolder, param1ItemHolderInfo1, param1ItemHolderInfo2)) {
            RecyclerView.this.postAnimationRunner();
          } 
        }
        
        public void unused(RecyclerView.ViewHolder param1ViewHolder) {
          RecyclerView.this.mLayout.removeAndRecycleView(param1ViewHolder.itemView, RecyclerView.this.mRecycler);
        }
      };
    setScrollContainer(true);
    setFocusableInTouchMode(true);
    ViewConfiguration viewConfiguration = ViewConfiguration.get(paramContext);
    this.mTouchSlop = viewConfiguration.getScaledTouchSlop();
    this.mScaledHorizontalScrollFactor = ViewConfigurationCompat.getScaledHorizontalScrollFactor(viewConfiguration, paramContext);
    this.mScaledVerticalScrollFactor = ViewConfigurationCompat.getScaledVerticalScrollFactor(viewConfiguration, paramContext);
    this.mMinFlingVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
    this.mMaxFlingVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
    if (getOverScrollMode() == 2) {
      bool2 = true;
    } else {
      bool2 = false;
    } 
    setWillNotDraw(bool2);
    this.mItemAnimator.setListener(this.mItemAnimatorListener);
    initAdapterManager();
    initChildrenHelper();
    initAutofill();
    if (ViewCompat.getImportantForAccessibility((View)this) == 0)
      ViewCompat.setImportantForAccessibility((View)this, 1); 
    this.mAccessibilityManager = (AccessibilityManager)getContext().getSystemService("accessibility");
    setAccessibilityDelegateCompat(new RecyclerViewAccessibilityDelegate(this));
    TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.RecyclerView, paramInt, 0);
    if (Build.VERSION.SDK_INT >= 29)
      saveAttributeDataForStyleable(paramContext, R.styleable.RecyclerView, paramAttributeSet, typedArray, paramInt, 0); 
    String str = typedArray.getString(R.styleable.RecyclerView_layoutManager);
    if (typedArray.getInt(R.styleable.RecyclerView_android_descendantFocusability, -1) == -1)
      setDescendantFocusability(262144); 
    this.mClipToPadding = typedArray.getBoolean(R.styleable.RecyclerView_android_clipToPadding, true);
    this.mEnableFastScroller = typedArray.getBoolean(R.styleable.RecyclerView_fastScrollEnabled, false);
    if (this.mEnableFastScroller)
      initFastScroller((StateListDrawable)typedArray.getDrawable(R.styleable.RecyclerView_fastScrollVerticalThumbDrawable), typedArray.getDrawable(R.styleable.RecyclerView_fastScrollVerticalTrackDrawable), (StateListDrawable)typedArray.getDrawable(R.styleable.RecyclerView_fastScrollHorizontalThumbDrawable), typedArray.getDrawable(R.styleable.RecyclerView_fastScrollHorizontalTrackDrawable)); 
    typedArray.recycle();
    createLayoutManager(paramContext, str, paramAttributeSet, paramInt, 0);
    boolean bool2 = bool1;
    if (Build.VERSION.SDK_INT >= 21) {
      TypedArray typedArray1 = paramContext.obtainStyledAttributes(paramAttributeSet, NESTED_SCROLLING_ATTRS, paramInt, 0);
      if (Build.VERSION.SDK_INT >= 29)
        saveAttributeDataForStyleable(paramContext, NESTED_SCROLLING_ATTRS, paramAttributeSet, typedArray1, paramInt, 0); 
      bool2 = typedArray1.getBoolean(0, true);
      typedArray1.recycle();
    } 
    setNestedScrollingEnabled(bool2);
  }
  
  private void addAnimatingView(ViewHolder paramViewHolder) {
    boolean bool;
    View view = paramViewHolder.itemView;
    if (view.getParent() == this) {
      bool = true;
    } else {
      bool = false;
    } 
    this.mRecycler.unscrapView(getChildViewHolder(view));
    if (paramViewHolder.isTmpDetached()) {
      this.mChildHelper.attachViewToParent(view, -1, view.getLayoutParams(), true);
    } else if (!bool) {
      this.mChildHelper.addView(view, true);
    } else {
      this.mChildHelper.hide(view);
    } 
  }
  
  private void animateChange(ViewHolder paramViewHolder1, ViewHolder paramViewHolder2, ItemAnimator.ItemHolderInfo paramItemHolderInfo1, ItemAnimator.ItemHolderInfo paramItemHolderInfo2, boolean paramBoolean1, boolean paramBoolean2) {
    paramViewHolder1.setIsRecyclable(false);
    if (paramBoolean1)
      addAnimatingView(paramViewHolder1); 
    if (paramViewHolder1 != paramViewHolder2) {
      if (paramBoolean2)
        addAnimatingView(paramViewHolder2); 
      paramViewHolder1.mShadowedHolder = paramViewHolder2;
      addAnimatingView(paramViewHolder1);
      this.mRecycler.unscrapView(paramViewHolder1);
      paramViewHolder2.setIsRecyclable(false);
      paramViewHolder2.mShadowingHolder = paramViewHolder1;
    } 
    if (this.mItemAnimator.animateChange(paramViewHolder1, paramViewHolder2, paramItemHolderInfo1, paramItemHolderInfo2))
      postAnimationRunner(); 
  }
  
  private void cancelScroll() {
    resetScroll();
    setScrollState(0);
  }
  
  static void clearNestedRecyclerViewIfNotNested(ViewHolder paramViewHolder) {
    if (paramViewHolder.mNestedRecyclerView != null) {
      View view = (View)paramViewHolder.mNestedRecyclerView.get();
      while (view != null) {
        if (view == paramViewHolder.itemView)
          return; 
        ViewParent viewParent = view.getParent();
        if (viewParent instanceof View) {
          View view1 = (View)viewParent;
          continue;
        } 
        viewParent = null;
      } 
      paramViewHolder.mNestedRecyclerView = null;
    } 
  }
  
  private void createLayoutManager(Context paramContext, String paramString, AttributeSet paramAttributeSet, int paramInt1, int paramInt2) {
    if (paramString != null) {
      paramString = paramString.trim();
      if (!paramString.isEmpty()) {
        String str = getFullClassName(paramContext, paramString);
        try {
          Object[] arrayOfObject;
          IllegalStateException illegalStateException;
          if (isInEditMode()) {
            classLoader = getClass().getClassLoader();
          } else {
            classLoader = paramContext.getClassLoader();
          } 
          Class<? extends LayoutManager> clazz = Class.forName(str, false, classLoader).asSubclass(LayoutManager.class);
          ClassLoader classLoader = null;
          try {
            Constructor<? extends LayoutManager> constructor2 = clazz.getConstructor(LAYOUT_MANAGER_CONSTRUCTOR_SIGNATURE);
            arrayOfObject = new Object[] { paramContext, paramAttributeSet, Integer.valueOf(paramInt1), Integer.valueOf(paramInt2) };
            Constructor<? extends LayoutManager> constructor1 = constructor2;
          } catch (NoSuchMethodException noSuchMethodException1) {
            try {
              Constructor<? extends LayoutManager> constructor = clazz.getConstructor(new Class[0]);
              constructor.setAccessible(true);
              setLayoutManager(constructor.newInstance(arrayOfObject));
            } catch (NoSuchMethodException noSuchMethodException) {
              noSuchMethodException.initCause(noSuchMethodException1);
              illegalStateException = new IllegalStateException();
              StringBuilder stringBuilder = new StringBuilder();
              this();
              stringBuilder.append(paramAttributeSet.getPositionDescription());
              stringBuilder.append(": Error creating LayoutManager ");
              stringBuilder.append(str);
              this(stringBuilder.toString(), noSuchMethodException);
              throw illegalStateException;
            } 
          } 
          noSuchMethodException.setAccessible(true);
          setLayoutManager(noSuchMethodException.newInstance((Object[])illegalStateException));
        } catch (ClassNotFoundException classNotFoundException) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append(paramAttributeSet.getPositionDescription());
          stringBuilder.append(": Unable to find LayoutManager ");
          stringBuilder.append(str);
          throw new IllegalStateException(stringBuilder.toString(), classNotFoundException);
        } catch (InvocationTargetException invocationTargetException) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append(paramAttributeSet.getPositionDescription());
          stringBuilder.append(": Could not instantiate the LayoutManager: ");
          stringBuilder.append(str);
          throw new IllegalStateException(stringBuilder.toString(), invocationTargetException);
        } catch (InstantiationException instantiationException) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append(paramAttributeSet.getPositionDescription());
          stringBuilder.append(": Could not instantiate the LayoutManager: ");
          stringBuilder.append(str);
          throw new IllegalStateException(stringBuilder.toString(), instantiationException);
        } catch (IllegalAccessException illegalAccessException) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append(paramAttributeSet.getPositionDescription());
          stringBuilder.append(": Cannot access non-public constructor ");
          stringBuilder.append(str);
          throw new IllegalStateException(stringBuilder.toString(), illegalAccessException);
        } catch (ClassCastException classCastException) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append(paramAttributeSet.getPositionDescription());
          stringBuilder.append(": Class is not a LayoutManager ");
          stringBuilder.append(str);
          throw new IllegalStateException(stringBuilder.toString(), classCastException);
        } 
      } 
    } 
  }
  
  private boolean didChildRangeChange(int paramInt1, int paramInt2) {
    findMinMaxChildLayoutPositions(this.mMinMaxLayoutPositions);
    int[] arrayOfInt = this.mMinMaxLayoutPositions;
    boolean bool = false;
    if (arrayOfInt[0] != paramInt1 || arrayOfInt[1] != paramInt2)
      bool = true; 
    return bool;
  }
  
  private void dispatchContentChangedIfNecessary() {
    int i = this.mEatenAccessibilityChangeFlags;
    this.mEatenAccessibilityChangeFlags = 0;
    if (i != 0 && isAccessibilityEnabled()) {
      AccessibilityEvent accessibilityEvent = AccessibilityEvent.obtain();
      accessibilityEvent.setEventType(2048);
      AccessibilityEventCompat.setContentChangeTypes(accessibilityEvent, i);
      sendAccessibilityEventUnchecked(accessibilityEvent);
    } 
  }
  
  private void dispatchLayoutStep1() {
    State state = this.mState;
    boolean bool = true;
    state.assertLayoutStep(1);
    fillRemainingScrollValues(this.mState);
    this.mState.mIsMeasuring = false;
    startInterceptRequestLayout();
    this.mViewInfoStore.clear();
    onEnterLayoutOrScroll();
    processAdapterUpdatesAndSetAnimationFlags();
    saveFocusInfo();
    state = this.mState;
    if (!state.mRunSimpleAnimations || !this.mItemsChanged)
      bool = false; 
    state.mTrackOldChangeHolders = bool;
    this.mItemsChanged = false;
    this.mItemsAddedOrRemoved = false;
    state = this.mState;
    state.mInPreLayout = state.mRunPredictiveAnimations;
    this.mState.mItemCount = this.mAdapter.getItemCount();
    findMinMaxChildLayoutPositions(this.mMinMaxLayoutPositions);
    if (this.mState.mRunSimpleAnimations) {
      int i = this.mChildHelper.getChildCount();
      for (byte b = 0; b < i; b++) {
        ViewHolder viewHolder = getChildViewHolderInt(this.mChildHelper.getChildAt(b));
        if (!viewHolder.shouldIgnore() && (!viewHolder.isInvalid() || this.mAdapter.hasStableIds())) {
          ItemAnimator.ItemHolderInfo itemHolderInfo = this.mItemAnimator.recordPreLayoutInformation(this.mState, viewHolder, ItemAnimator.buildAdapterChangeFlagsForAnimations(viewHolder), viewHolder.getUnmodifiedPayloads());
          this.mViewInfoStore.addToPreLayout(viewHolder, itemHolderInfo);
          if (this.mState.mTrackOldChangeHolders && viewHolder.isUpdated() && !viewHolder.isRemoved() && !viewHolder.shouldIgnore() && !viewHolder.isInvalid()) {
            long l = getChangedHolderKey(viewHolder);
            this.mViewInfoStore.addToOldChangeHolders(l, viewHolder);
          } 
        } 
      } 
    } 
    if (this.mState.mRunPredictiveAnimations) {
      saveOldPositions();
      bool = this.mState.mStructureChanged;
      state = this.mState;
      state.mStructureChanged = false;
      this.mLayout.onLayoutChildren(this.mRecycler, state);
      this.mState.mStructureChanged = bool;
      for (byte b = 0; b < this.mChildHelper.getChildCount(); b++) {
        ViewHolder viewHolder = getChildViewHolderInt(this.mChildHelper.getChildAt(b));
        if (!viewHolder.shouldIgnore() && !this.mViewInfoStore.isInPreLayout(viewHolder)) {
          int j = ItemAnimator.buildAdapterChangeFlagsForAnimations(viewHolder);
          bool = viewHolder.hasAnyOfTheFlags(8192);
          int i = j;
          if (!bool)
            i = j | 0x1000; 
          ItemAnimator.ItemHolderInfo itemHolderInfo = this.mItemAnimator.recordPreLayoutInformation(this.mState, viewHolder, i, viewHolder.getUnmodifiedPayloads());
          if (bool) {
            recordAnimationInfoIfBouncedHiddenView(viewHolder, itemHolderInfo);
          } else {
            this.mViewInfoStore.addToAppearedInPreLayoutHolders(viewHolder, itemHolderInfo);
          } 
        } 
      } 
      clearOldPositions();
    } else {
      clearOldPositions();
    } 
    onExitLayoutOrScroll();
    stopInterceptRequestLayout(false);
    this.mState.mLayoutStep = 2;
  }
  
  private void dispatchLayoutStep2() {
    boolean bool;
    startInterceptRequestLayout();
    onEnterLayoutOrScroll();
    this.mState.assertLayoutStep(6);
    this.mAdapterHelper.consumeUpdatesInOnePass();
    this.mState.mItemCount = this.mAdapter.getItemCount();
    State state = this.mState;
    state.mDeletedInvisibleItemCountSincePreviousLayout = 0;
    state.mInPreLayout = false;
    this.mLayout.onLayoutChildren(this.mRecycler, state);
    state = this.mState;
    state.mStructureChanged = false;
    this.mPendingSavedState = null;
    if (state.mRunSimpleAnimations && this.mItemAnimator != null) {
      bool = true;
    } else {
      bool = false;
    } 
    state.mRunSimpleAnimations = bool;
    this.mState.mLayoutStep = 4;
    onExitLayoutOrScroll();
    stopInterceptRequestLayout(false);
  }
  
  private void dispatchLayoutStep3() {
    this.mState.assertLayoutStep(4);
    startInterceptRequestLayout();
    onEnterLayoutOrScroll();
    State state = this.mState;
    state.mLayoutStep = 1;
    if (state.mRunSimpleAnimations) {
      for (int i = this.mChildHelper.getChildCount() - 1; i >= 0; i--) {
        ViewHolder viewHolder = getChildViewHolderInt(this.mChildHelper.getChildAt(i));
        if (!viewHolder.shouldIgnore()) {
          long l = getChangedHolderKey(viewHolder);
          ItemAnimator.ItemHolderInfo itemHolderInfo = this.mItemAnimator.recordPostLayoutInformation(this.mState, viewHolder);
          ViewHolder viewHolder1 = this.mViewInfoStore.getFromOldChangeHolders(l);
          if (viewHolder1 != null && !viewHolder1.shouldIgnore()) {
            boolean bool1 = this.mViewInfoStore.isDisappearing(viewHolder1);
            boolean bool2 = this.mViewInfoStore.isDisappearing(viewHolder);
            if (bool1 && viewHolder1 == viewHolder) {
              this.mViewInfoStore.addToPostLayout(viewHolder, itemHolderInfo);
            } else {
              ItemAnimator.ItemHolderInfo itemHolderInfo1 = this.mViewInfoStore.popFromPreLayout(viewHolder1);
              this.mViewInfoStore.addToPostLayout(viewHolder, itemHolderInfo);
              itemHolderInfo = this.mViewInfoStore.popFromPostLayout(viewHolder);
              if (itemHolderInfo1 == null) {
                handleMissingPreInfoForChangeError(l, viewHolder, viewHolder1);
              } else {
                animateChange(viewHolder1, viewHolder, itemHolderInfo1, itemHolderInfo, bool1, bool2);
              } 
            } 
          } else {
            this.mViewInfoStore.addToPostLayout(viewHolder, itemHolderInfo);
          } 
        } 
      } 
      this.mViewInfoStore.process(this.mViewInfoProcessCallback);
    } 
    this.mLayout.removeAndRecycleScrapInt(this.mRecycler);
    state = this.mState;
    state.mPreviousLayoutItemCount = state.mItemCount;
    this.mDataSetHasChangedAfterLayout = false;
    this.mDispatchItemsChangedEvent = false;
    state = this.mState;
    state.mRunSimpleAnimations = false;
    state.mRunPredictiveAnimations = false;
    this.mLayout.mRequestedSimpleAnimations = false;
    if (this.mRecycler.mChangedScrap != null)
      this.mRecycler.mChangedScrap.clear(); 
    if (this.mLayout.mPrefetchMaxObservedInInitialPrefetch) {
      LayoutManager layoutManager = this.mLayout;
      layoutManager.mPrefetchMaxCountObserved = 0;
      layoutManager.mPrefetchMaxObservedInInitialPrefetch = false;
      this.mRecycler.updateViewCacheSize();
    } 
    this.mLayout.onLayoutCompleted(this.mState);
    onExitLayoutOrScroll();
    stopInterceptRequestLayout(false);
    this.mViewInfoStore.clear();
    int[] arrayOfInt = this.mMinMaxLayoutPositions;
    if (didChildRangeChange(arrayOfInt[0], arrayOfInt[1]))
      dispatchOnScrolled(0, 0); 
    recoverFocusFromState();
    resetFocusInfo();
  }
  
  private boolean dispatchToOnItemTouchListeners(MotionEvent paramMotionEvent) {
    OnItemTouchListener onItemTouchListener = this.mInterceptingOnItemTouchListener;
    if (onItemTouchListener == null)
      return (paramMotionEvent.getAction() == 0) ? false : findInterceptingOnItemTouchListener(paramMotionEvent); 
    onItemTouchListener.onTouchEvent(this, paramMotionEvent);
    int i = paramMotionEvent.getAction();
    if (i == 3 || i == 1)
      this.mInterceptingOnItemTouchListener = null; 
    return true;
  }
  
  private boolean findInterceptingOnItemTouchListener(MotionEvent paramMotionEvent) {
    int i = paramMotionEvent.getAction();
    int j = this.mOnItemTouchListeners.size();
    for (byte b = 0; b < j; b++) {
      OnItemTouchListener onItemTouchListener = this.mOnItemTouchListeners.get(b);
      if (onItemTouchListener.onInterceptTouchEvent(this, paramMotionEvent) && i != 3) {
        this.mInterceptingOnItemTouchListener = onItemTouchListener;
        return true;
      } 
    } 
    return false;
  }
  
  private void findMinMaxChildLayoutPositions(int[] paramArrayOfint) {
    int i = this.mChildHelper.getChildCount();
    if (i == 0) {
      paramArrayOfint[0] = -1;
      paramArrayOfint[1] = -1;
      return;
    } 
    byte b = 0;
    int j = Integer.MAX_VALUE;
    int k;
    for (k = Integer.MIN_VALUE; b < i; k = m) {
      int m;
      ViewHolder viewHolder = getChildViewHolderInt(this.mChildHelper.getChildAt(b));
      if (viewHolder.shouldIgnore()) {
        m = k;
      } else {
        int n = viewHolder.getLayoutPosition();
        int i1 = j;
        if (n < j)
          i1 = n; 
        j = i1;
        m = k;
        if (n > k) {
          m = n;
          j = i1;
        } 
      } 
      b++;
    } 
    paramArrayOfint[0] = j;
    paramArrayOfint[1] = k;
  }
  
  static RecyclerView findNestedRecyclerView(View paramView) {
    if (!(paramView instanceof ViewGroup))
      return null; 
    if (paramView instanceof RecyclerView)
      return (RecyclerView)paramView; 
    ViewGroup viewGroup = (ViewGroup)paramView;
    int i = viewGroup.getChildCount();
    for (byte b = 0; b < i; b++) {
      RecyclerView recyclerView = findNestedRecyclerView(viewGroup.getChildAt(b));
      if (recyclerView != null)
        return recyclerView; 
    } 
    return null;
  }
  
  private View findNextViewToFocus() {
    if (this.mState.mFocusedItemPosition != -1) {
      i = this.mState.mFocusedItemPosition;
    } else {
      i = 0;
    } 
    int j = this.mState.getItemCount();
    for (int k = i; k < j; k++) {
      ViewHolder viewHolder = findViewHolderForAdapterPosition(k);
      if (viewHolder == null)
        break; 
      if (viewHolder.itemView.hasFocusable())
        return viewHolder.itemView; 
    } 
    for (int i = Math.min(j, i) - 1; i >= 0; i--) {
      ViewHolder viewHolder = findViewHolderForAdapterPosition(i);
      if (viewHolder == null)
        return null; 
      if (viewHolder.itemView.hasFocusable())
        return viewHolder.itemView; 
    } 
    return null;
  }
  
  static ViewHolder getChildViewHolderInt(View paramView) {
    return (paramView == null) ? null : ((LayoutParams)paramView.getLayoutParams()).mViewHolder;
  }
  
  static void getDecoratedBoundsWithMarginsInt(View paramView, Rect paramRect) {
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    Rect rect = layoutParams.mDecorInsets;
    paramRect.set(paramView.getLeft() - rect.left - layoutParams.leftMargin, paramView.getTop() - rect.top - layoutParams.topMargin, paramView.getRight() + rect.right + layoutParams.rightMargin, paramView.getBottom() + rect.bottom + layoutParams.bottomMargin);
  }
  
  private int getDeepestFocusedViewWithId(View paramView) {
    int i = paramView.getId();
    while (!paramView.isFocused() && paramView instanceof ViewGroup && paramView.hasFocus()) {
      View view = ((ViewGroup)paramView).getFocusedChild();
      paramView = view;
      if (view.getId() != -1) {
        i = view.getId();
        paramView = view;
      } 
    } 
    return i;
  }
  
  private String getFullClassName(Context paramContext, String paramString) {
    if (paramString.charAt(0) == '.') {
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append(paramContext.getPackageName());
      stringBuilder1.append(paramString);
      return stringBuilder1.toString();
    } 
    if (paramString.contains("."))
      return paramString; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(RecyclerView.class.getPackage().getName());
    stringBuilder.append('.');
    stringBuilder.append(paramString);
    return stringBuilder.toString();
  }
  
  private NestedScrollingChildHelper getScrollingChildHelper() {
    if (this.mScrollingChildHelper == null)
      this.mScrollingChildHelper = new NestedScrollingChildHelper((View)this); 
    return this.mScrollingChildHelper;
  }
  
  private void handleMissingPreInfoForChangeError(long paramLong, ViewHolder paramViewHolder1, ViewHolder paramViewHolder2) {
    StringBuilder stringBuilder1;
    int i = this.mChildHelper.getChildCount();
    for (byte b = 0; b < i; b++) {
      ViewHolder viewHolder = getChildViewHolderInt(this.mChildHelper.getChildAt(b));
      if (viewHolder != paramViewHolder1 && getChangedHolderKey(viewHolder) == paramLong) {
        Adapter adapter = this.mAdapter;
        if (adapter != null && adapter.hasStableIds()) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("Two different ViewHolders have the same stable ID. Stable IDs in your adapter MUST BE unique and SHOULD NOT change.\n ViewHolder 1:");
          stringBuilder.append(viewHolder);
          stringBuilder.append(" \n View Holder 2:");
          stringBuilder.append(paramViewHolder1);
          stringBuilder.append(exceptionLabel());
          throw new IllegalStateException(stringBuilder.toString());
        } 
        stringBuilder1 = new StringBuilder();
        stringBuilder1.append("Two different ViewHolders have the same change ID. This might happen due to inconsistent Adapter update events or if the LayoutManager lays out the same View multiple times.\n ViewHolder 1:");
        stringBuilder1.append(viewHolder);
        stringBuilder1.append(" \n View Holder 2:");
        stringBuilder1.append(paramViewHolder1);
        stringBuilder1.append(exceptionLabel());
        throw new IllegalStateException(stringBuilder1.toString());
      } 
    } 
    StringBuilder stringBuilder2 = new StringBuilder();
    stringBuilder2.append("Problem while matching changed view holders with the newones. The pre-layout information for the change holder ");
    stringBuilder2.append(stringBuilder1);
    stringBuilder2.append(" cannot be found but it is necessary for ");
    stringBuilder2.append(paramViewHolder1);
    stringBuilder2.append(exceptionLabel());
    Log.e("RecyclerView", stringBuilder2.toString());
  }
  
  private boolean hasUpdatedView() {
    int i = this.mChildHelper.getChildCount();
    for (byte b = 0; b < i; b++) {
      ViewHolder viewHolder = getChildViewHolderInt(this.mChildHelper.getChildAt(b));
      if (viewHolder != null && !viewHolder.shouldIgnore() && viewHolder.isUpdated())
        return true; 
    } 
    return false;
  }
  
  private void initAutofill() {
    if (ViewCompat.getImportantForAutofill((View)this) == 0)
      ViewCompat.setImportantForAutofill((View)this, 8); 
  }
  
  private void initChildrenHelper() {
    this.mChildHelper = new ChildHelper(new ChildHelper.Callback() {
          public void addView(View param1View, int param1Int) {
            RecyclerView.this.addView(param1View, param1Int);
            RecyclerView.this.dispatchChildAttached(param1View);
          }
          
          public void attachViewToParent(View param1View, int param1Int, ViewGroup.LayoutParams param1LayoutParams) {
            StringBuilder stringBuilder;
            RecyclerView.ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(param1View);
            if (viewHolder != null)
              if (viewHolder.isTmpDetached() || viewHolder.shouldIgnore()) {
                viewHolder.clearTmpDetachFlag();
              } else {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Called attach on a child which is not detached: ");
                stringBuilder.append(viewHolder);
                stringBuilder.append(RecyclerView.this.exceptionLabel());
                throw new IllegalArgumentException(stringBuilder.toString());
              }  
            RecyclerView.this.attachViewToParent((View)stringBuilder, param1Int, param1LayoutParams);
          }
          
          public void detachViewFromParent(int param1Int) {
            View view = getChildAt(param1Int);
            if (view != null) {
              RecyclerView.ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(view);
              if (viewHolder != null)
                if (!viewHolder.isTmpDetached() || viewHolder.shouldIgnore()) {
                  viewHolder.addFlags(256);
                } else {
                  StringBuilder stringBuilder = new StringBuilder();
                  stringBuilder.append("called detach on an already detached child ");
                  stringBuilder.append(viewHolder);
                  stringBuilder.append(RecyclerView.this.exceptionLabel());
                  throw new IllegalArgumentException(stringBuilder.toString());
                }  
            } 
            RecyclerView.this.detachViewFromParent(param1Int);
          }
          
          public View getChildAt(int param1Int) {
            return RecyclerView.this.getChildAt(param1Int);
          }
          
          public int getChildCount() {
            return RecyclerView.this.getChildCount();
          }
          
          public RecyclerView.ViewHolder getChildViewHolder(View param1View) {
            return RecyclerView.getChildViewHolderInt(param1View);
          }
          
          public int indexOfChild(View param1View) {
            return RecyclerView.this.indexOfChild(param1View);
          }
          
          public void onEnteredHiddenState(View param1View) {
            RecyclerView.ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(param1View);
            if (viewHolder != null)
              viewHolder.onEnteredHiddenState(RecyclerView.this); 
          }
          
          public void onLeftHiddenState(View param1View) {
            RecyclerView.ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(param1View);
            if (viewHolder != null)
              viewHolder.onLeftHiddenState(RecyclerView.this); 
          }
          
          public void removeAllViews() {
            int i = getChildCount();
            for (byte b = 0; b < i; b++) {
              View view = getChildAt(b);
              RecyclerView.this.dispatchChildDetached(view);
              view.clearAnimation();
            } 
            RecyclerView.this.removeAllViews();
          }
          
          public void removeViewAt(int param1Int) {
            View view = RecyclerView.this.getChildAt(param1Int);
            if (view != null) {
              RecyclerView.this.dispatchChildDetached(view);
              view.clearAnimation();
            } 
            RecyclerView.this.removeViewAt(param1Int);
          }
        });
  }
  
  private boolean isPreferredNextFocus(View paramView1, View paramView2, int paramInt) {
    boolean bool1 = false;
    boolean bool2 = false;
    boolean bool3 = false;
    boolean bool4 = false;
    boolean bool5 = false;
    boolean bool6 = false;
    boolean bool7 = bool5;
    if (paramView2 != null)
      if (paramView2 == this) {
        bool7 = bool5;
      } else {
        byte b1;
        if (findContainingItemView(paramView2) == null)
          return false; 
        if (paramView1 == null)
          return true; 
        if (findContainingItemView(paramView1) == null)
          return true; 
        this.mTempRect.set(0, 0, paramView1.getWidth(), paramView1.getHeight());
        this.mTempRect2.set(0, 0, paramView2.getWidth(), paramView2.getHeight());
        offsetDescendantRectToMyCoords(paramView1, this.mTempRect);
        offsetDescendantRectToMyCoords(paramView2, this.mTempRect2);
        int i = this.mLayout.getLayoutDirection();
        byte b = -1;
        if (i == 1) {
          b1 = -1;
        } else {
          b1 = 1;
        } 
        if ((this.mTempRect.left < this.mTempRect2.left || this.mTempRect.right <= this.mTempRect2.left) && this.mTempRect.right < this.mTempRect2.right) {
          i = 1;
        } else if ((this.mTempRect.right > this.mTempRect2.right || this.mTempRect.left >= this.mTempRect2.right) && this.mTempRect.left > this.mTempRect2.left) {
          i = -1;
        } else {
          i = 0;
        } 
        if ((this.mTempRect.top < this.mTempRect2.top || this.mTempRect.bottom <= this.mTempRect2.top) && this.mTempRect.bottom < this.mTempRect2.bottom) {
          b = 1;
        } else if ((this.mTempRect.bottom <= this.mTempRect2.bottom && this.mTempRect.top < this.mTempRect2.bottom) || this.mTempRect.top <= this.mTempRect2.top) {
          b = 0;
        } 
        if (paramInt != 1) {
          if (paramInt != 2) {
            if (paramInt != 17) {
              if (paramInt != 33) {
                if (paramInt != 66) {
                  if (paramInt == 130) {
                    bool7 = bool6;
                    if (b > 0)
                      bool7 = true; 
                    return bool7;
                  } 
                  StringBuilder stringBuilder = new StringBuilder();
                  stringBuilder.append("Invalid direction: ");
                  stringBuilder.append(paramInt);
                  stringBuilder.append(exceptionLabel());
                  throw new IllegalArgumentException(stringBuilder.toString());
                } 
                bool7 = bool1;
                if (i > 0)
                  bool7 = true; 
                return bool7;
              } 
              bool7 = bool2;
              if (b < 0)
                bool7 = true; 
              return bool7;
            } 
            bool7 = bool3;
            if (i < 0)
              bool7 = true; 
            return bool7;
          } 
          if (b <= 0) {
            bool7 = bool4;
            if (b == 0) {
              bool7 = bool4;
              if (i * b1 >= 0)
                bool7 = true; 
            } 
            return bool7;
          } 
        } else {
          if (b >= 0) {
            bool7 = bool5;
            if (b == 0) {
              bool7 = bool5;
              if (i * b1 <= 0)
                bool7 = true; 
            } 
            return bool7;
          } 
          bool7 = true;
        } 
        bool7 = true;
      }  
    return bool7;
  }
  
  private void onPointerUp(MotionEvent paramMotionEvent) {
    int i = paramMotionEvent.getActionIndex();
    if (paramMotionEvent.getPointerId(i) == this.mScrollPointerId) {
      if (i == 0) {
        i = 1;
      } else {
        i = 0;
      } 
      this.mScrollPointerId = paramMotionEvent.getPointerId(i);
      int j = (int)(paramMotionEvent.getX(i) + 0.5F);
      this.mLastTouchX = j;
      this.mInitialTouchX = j;
      i = (int)(paramMotionEvent.getY(i) + 0.5F);
      this.mLastTouchY = i;
      this.mInitialTouchY = i;
    } 
  }
  
  private boolean predictiveItemAnimationsEnabled() {
    boolean bool;
    if (this.mItemAnimator != null && this.mLayout.supportsPredictiveItemAnimations()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private void processAdapterUpdatesAndSetAnimationFlags() {
    boolean bool2;
    if (this.mDataSetHasChangedAfterLayout) {
      this.mAdapterHelper.reset();
      if (this.mDispatchItemsChangedEvent)
        this.mLayout.onItemsChanged(this); 
    } 
    if (predictiveItemAnimationsEnabled()) {
      this.mAdapterHelper.preProcess();
    } else {
      this.mAdapterHelper.consumeUpdatesInOnePass();
    } 
    boolean bool = this.mItemsAddedOrRemoved;
    boolean bool1 = false;
    if (bool || this.mItemsChanged) {
      bool2 = true;
    } else {
      bool2 = false;
    } 
    State state = this.mState;
    if (this.mFirstLayoutComplete && this.mItemAnimator != null && (this.mDataSetHasChangedAfterLayout || bool2 || this.mLayout.mRequestedSimpleAnimations) && (!this.mDataSetHasChangedAfterLayout || this.mAdapter.hasStableIds())) {
      bool = true;
    } else {
      bool = false;
    } 
    state.mRunSimpleAnimations = bool;
    state = this.mState;
    bool = bool1;
    if (state.mRunSimpleAnimations) {
      bool = bool1;
      if (bool2) {
        bool = bool1;
        if (!this.mDataSetHasChangedAfterLayout) {
          bool = bool1;
          if (predictiveItemAnimationsEnabled())
            bool = true; 
        } 
      } 
    } 
    state.mRunPredictiveAnimations = bool;
  }
  
  private void pullGlows(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
    // Byte code:
    //   0: iconst_1
    //   1: istore #5
    //   3: fload_2
    //   4: fconst_0
    //   5: fcmpg
    //   6: ifge -> 43
    //   9: aload_0
    //   10: invokevirtual ensureLeftGlow : ()V
    //   13: aload_0
    //   14: getfield mLeftGlow : Landroid/widget/EdgeEffect;
    //   17: fload_2
    //   18: fneg
    //   19: aload_0
    //   20: invokevirtual getWidth : ()I
    //   23: i2f
    //   24: fdiv
    //   25: fconst_1
    //   26: fload_3
    //   27: aload_0
    //   28: invokevirtual getHeight : ()I
    //   31: i2f
    //   32: fdiv
    //   33: fsub
    //   34: invokestatic onPull : (Landroid/widget/EdgeEffect;FF)V
    //   37: iconst_1
    //   38: istore #6
    //   40: goto -> 80
    //   43: fload_2
    //   44: fconst_0
    //   45: fcmpl
    //   46: ifle -> 77
    //   49: aload_0
    //   50: invokevirtual ensureRightGlow : ()V
    //   53: aload_0
    //   54: getfield mRightGlow : Landroid/widget/EdgeEffect;
    //   57: fload_2
    //   58: aload_0
    //   59: invokevirtual getWidth : ()I
    //   62: i2f
    //   63: fdiv
    //   64: fload_3
    //   65: aload_0
    //   66: invokevirtual getHeight : ()I
    //   69: i2f
    //   70: fdiv
    //   71: invokestatic onPull : (Landroid/widget/EdgeEffect;FF)V
    //   74: goto -> 37
    //   77: iconst_0
    //   78: istore #6
    //   80: fload #4
    //   82: fconst_0
    //   83: fcmpg
    //   84: ifge -> 121
    //   87: aload_0
    //   88: invokevirtual ensureTopGlow : ()V
    //   91: aload_0
    //   92: getfield mTopGlow : Landroid/widget/EdgeEffect;
    //   95: fload #4
    //   97: fneg
    //   98: aload_0
    //   99: invokevirtual getHeight : ()I
    //   102: i2f
    //   103: fdiv
    //   104: fload_1
    //   105: aload_0
    //   106: invokevirtual getWidth : ()I
    //   109: i2f
    //   110: fdiv
    //   111: invokestatic onPull : (Landroid/widget/EdgeEffect;FF)V
    //   114: iload #5
    //   116: istore #6
    //   118: goto -> 163
    //   121: fload #4
    //   123: fconst_0
    //   124: fcmpl
    //   125: ifle -> 163
    //   128: aload_0
    //   129: invokevirtual ensureBottomGlow : ()V
    //   132: aload_0
    //   133: getfield mBottomGlow : Landroid/widget/EdgeEffect;
    //   136: fload #4
    //   138: aload_0
    //   139: invokevirtual getHeight : ()I
    //   142: i2f
    //   143: fdiv
    //   144: fconst_1
    //   145: fload_1
    //   146: aload_0
    //   147: invokevirtual getWidth : ()I
    //   150: i2f
    //   151: fdiv
    //   152: fsub
    //   153: invokestatic onPull : (Landroid/widget/EdgeEffect;FF)V
    //   156: iload #5
    //   158: istore #6
    //   160: goto -> 163
    //   163: iload #6
    //   165: ifne -> 181
    //   168: fload_2
    //   169: fconst_0
    //   170: fcmpl
    //   171: ifne -> 181
    //   174: fload #4
    //   176: fconst_0
    //   177: fcmpl
    //   178: ifeq -> 185
    //   181: aload_0
    //   182: invokestatic postInvalidateOnAnimation : (Landroid/view/View;)V
    //   185: return
  }
  
  private void recoverFocusFromState() {
    if (this.mPreserveFocusAfterLayout && this.mAdapter != null && hasFocus() && getDescendantFocusability() != 393216 && (getDescendantFocusability() != 131072 || !isFocused())) {
      View view1;
      if (!isFocused()) {
        view1 = getFocusedChild();
        if (IGNORE_DETACHED_FOCUSED_CHILD && (view1.getParent() == null || !view1.hasFocus())) {
          if (this.mChildHelper.getChildCount() == 0) {
            requestFocus();
            return;
          } 
        } else if (!this.mChildHelper.isHidden(view1)) {
          return;
        } 
      } 
      long l = this.mState.mFocusedItemId;
      View view2 = null;
      if (l != -1L && this.mAdapter.hasStableIds()) {
        view1 = (View)findViewHolderForItemId(this.mState.mFocusedItemId);
      } else {
        view1 = null;
      } 
      if (view1 == null || this.mChildHelper.isHidden(((ViewHolder)view1).itemView) || !((ViewHolder)view1).itemView.hasFocusable()) {
        view1 = view2;
        if (this.mChildHelper.getChildCount() > 0)
          view1 = findNextViewToFocus(); 
      } else {
        view1 = ((ViewHolder)view1).itemView;
      } 
      if (view1 != null) {
        if (this.mState.mFocusedSubChildId != -1L) {
          view2 = view1.findViewById(this.mState.mFocusedSubChildId);
          if (view2 != null && view2.isFocusable())
            view1 = view2; 
        } 
        view1.requestFocus();
      } 
    } 
  }
  
  private void releaseGlows() {
    EdgeEffect edgeEffect = this.mLeftGlow;
    if (edgeEffect != null) {
      edgeEffect.onRelease();
      bool1 = this.mLeftGlow.isFinished();
    } else {
      bool1 = false;
    } 
    edgeEffect = this.mTopGlow;
    boolean bool2 = bool1;
    if (edgeEffect != null) {
      edgeEffect.onRelease();
      bool2 = bool1 | this.mTopGlow.isFinished();
    } 
    edgeEffect = this.mRightGlow;
    boolean bool1 = bool2;
    if (edgeEffect != null) {
      edgeEffect.onRelease();
      bool1 = bool2 | this.mRightGlow.isFinished();
    } 
    edgeEffect = this.mBottomGlow;
    bool2 = bool1;
    if (edgeEffect != null) {
      edgeEffect.onRelease();
      bool2 = bool1 | this.mBottomGlow.isFinished();
    } 
    if (bool2)
      ViewCompat.postInvalidateOnAnimation((View)this); 
  }
  
  private void requestChildOnScreen(View paramView1, View paramView2) {
    View view;
    boolean bool1;
    if (paramView2 != null) {
      view = paramView2;
    } else {
      view = paramView1;
    } 
    this.mTempRect.set(0, 0, view.getWidth(), view.getHeight());
    ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
    if (layoutParams instanceof LayoutParams) {
      LayoutParams layoutParams1 = (LayoutParams)layoutParams;
      if (!layoutParams1.mInsetsDirty) {
        Rect rect1 = layoutParams1.mDecorInsets;
        Rect rect2 = this.mTempRect;
        rect2.left -= rect1.left;
        rect2 = this.mTempRect;
        rect2.right += rect1.right;
        rect2 = this.mTempRect;
        rect2.top -= rect1.top;
        rect2 = this.mTempRect;
        rect2.bottom += rect1.bottom;
      } 
    } 
    if (paramView2 != null) {
      offsetDescendantRectToMyCoords(paramView2, this.mTempRect);
      offsetRectIntoDescendantCoords(paramView1, this.mTempRect);
    } 
    LayoutManager layoutManager = this.mLayout;
    Rect rect = this.mTempRect;
    boolean bool = this.mFirstLayoutComplete;
    if (paramView2 == null) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    layoutManager.requestChildRectangleOnScreen(this, paramView1, rect, bool ^ true, bool1);
  }
  
  private void resetFocusInfo() {
    State state = this.mState;
    state.mFocusedItemId = -1L;
    state.mFocusedItemPosition = -1;
    state.mFocusedSubChildId = -1;
  }
  
  private void resetScroll() {
    VelocityTracker velocityTracker = this.mVelocityTracker;
    if (velocityTracker != null)
      velocityTracker.clear(); 
    stopNestedScroll(0);
    releaseGlows();
  }
  
  private void saveFocusInfo() {
    ViewHolder viewHolder2;
    boolean bool = this.mPreserveFocusAfterLayout;
    ViewHolder viewHolder1 = null;
    if (bool && hasFocus() && this.mAdapter != null) {
      viewHolder2 = (ViewHolder)getFocusedChild();
    } else {
      viewHolder2 = null;
    } 
    if (viewHolder2 == null) {
      viewHolder2 = viewHolder1;
    } else {
      viewHolder2 = findContainingViewHolder((View)viewHolder2);
    } 
    if (viewHolder2 == null) {
      resetFocusInfo();
    } else {
      long l;
      int i;
      State state = this.mState;
      if (this.mAdapter.hasStableIds()) {
        l = viewHolder2.getItemId();
      } else {
        l = -1L;
      } 
      state.mFocusedItemId = l;
      state = this.mState;
      if (this.mDataSetHasChangedAfterLayout) {
        i = -1;
      } else if (viewHolder2.isRemoved()) {
        i = viewHolder2.mOldPosition;
      } else {
        i = viewHolder2.getAdapterPosition();
      } 
      state.mFocusedItemPosition = i;
      this.mState.mFocusedSubChildId = getDeepestFocusedViewWithId(viewHolder2.itemView);
    } 
  }
  
  private void setAdapterInternal(Adapter paramAdapter, boolean paramBoolean1, boolean paramBoolean2) {
    Adapter adapter = this.mAdapter;
    if (adapter != null) {
      adapter.unregisterAdapterDataObserver(this.mObserver);
      this.mAdapter.onDetachedFromRecyclerView(this);
    } 
    if (!paramBoolean1 || paramBoolean2)
      removeAndRecycleViews(); 
    this.mAdapterHelper.reset();
    adapter = this.mAdapter;
    this.mAdapter = paramAdapter;
    if (paramAdapter != null) {
      paramAdapter.registerAdapterDataObserver(this.mObserver);
      paramAdapter.onAttachedToRecyclerView(this);
    } 
    LayoutManager layoutManager = this.mLayout;
    if (layoutManager != null)
      layoutManager.onAdapterChanged(adapter, this.mAdapter); 
    this.mRecycler.onAdapterChanged(adapter, this.mAdapter, paramBoolean1);
    this.mState.mStructureChanged = true;
  }
  
  private void stopScrollersInternal() {
    this.mViewFlinger.stop();
    LayoutManager layoutManager = this.mLayout;
    if (layoutManager != null)
      layoutManager.stopSmoothScroller(); 
  }
  
  void absorbGlows(int paramInt1, int paramInt2) {
    if (paramInt1 < 0) {
      ensureLeftGlow();
      if (this.mLeftGlow.isFinished())
        this.mLeftGlow.onAbsorb(-paramInt1); 
    } else if (paramInt1 > 0) {
      ensureRightGlow();
      if (this.mRightGlow.isFinished())
        this.mRightGlow.onAbsorb(paramInt1); 
    } 
    if (paramInt2 < 0) {
      ensureTopGlow();
      if (this.mTopGlow.isFinished())
        this.mTopGlow.onAbsorb(-paramInt2); 
    } else if (paramInt2 > 0) {
      ensureBottomGlow();
      if (this.mBottomGlow.isFinished())
        this.mBottomGlow.onAbsorb(paramInt2); 
    } 
    if (paramInt1 != 0 || paramInt2 != 0)
      ViewCompat.postInvalidateOnAnimation((View)this); 
  }
  
  public void addFocusables(ArrayList<View> paramArrayList, int paramInt1, int paramInt2) {
    LayoutManager layoutManager = this.mLayout;
    if (layoutManager == null || !layoutManager.onAddFocusables(this, paramArrayList, paramInt1, paramInt2))
      super.addFocusables(paramArrayList, paramInt1, paramInt2); 
  }
  
  public void addItemDecoration(ItemDecoration paramItemDecoration) {
    addItemDecoration(paramItemDecoration, -1);
  }
  
  public void addItemDecoration(ItemDecoration paramItemDecoration, int paramInt) {
    LayoutManager layoutManager = this.mLayout;
    if (layoutManager != null)
      layoutManager.assertNotInLayoutOrScroll("Cannot add item decoration during a scroll  or layout"); 
    if (this.mItemDecorations.isEmpty())
      setWillNotDraw(false); 
    if (paramInt < 0) {
      this.mItemDecorations.add(paramItemDecoration);
    } else {
      this.mItemDecorations.add(paramInt, paramItemDecoration);
    } 
    markItemDecorInsetsDirty();
    requestLayout();
  }
  
  public void addOnChildAttachStateChangeListener(OnChildAttachStateChangeListener paramOnChildAttachStateChangeListener) {
    if (this.mOnChildAttachStateListeners == null)
      this.mOnChildAttachStateListeners = new ArrayList<OnChildAttachStateChangeListener>(); 
    this.mOnChildAttachStateListeners.add(paramOnChildAttachStateChangeListener);
  }
  
  public void addOnItemTouchListener(OnItemTouchListener paramOnItemTouchListener) {
    this.mOnItemTouchListeners.add(paramOnItemTouchListener);
  }
  
  public void addOnScrollListener(OnScrollListener paramOnScrollListener) {
    if (this.mScrollListeners == null)
      this.mScrollListeners = new ArrayList<OnScrollListener>(); 
    this.mScrollListeners.add(paramOnScrollListener);
  }
  
  void animateAppearance(ViewHolder paramViewHolder, ItemAnimator.ItemHolderInfo paramItemHolderInfo1, ItemAnimator.ItemHolderInfo paramItemHolderInfo2) {
    paramViewHolder.setIsRecyclable(false);
    if (this.mItemAnimator.animateAppearance(paramViewHolder, paramItemHolderInfo1, paramItemHolderInfo2))
      postAnimationRunner(); 
  }
  
  void animateDisappearance(ViewHolder paramViewHolder, ItemAnimator.ItemHolderInfo paramItemHolderInfo1, ItemAnimator.ItemHolderInfo paramItemHolderInfo2) {
    addAnimatingView(paramViewHolder);
    paramViewHolder.setIsRecyclable(false);
    if (this.mItemAnimator.animateDisappearance(paramViewHolder, paramItemHolderInfo1, paramItemHolderInfo2))
      postAnimationRunner(); 
  }
  
  void assertInLayoutOrScroll(String paramString) {
    if (!isComputingLayout()) {
      StringBuilder stringBuilder1;
      if (paramString == null) {
        stringBuilder1 = new StringBuilder();
        stringBuilder1.append("Cannot call this method unless RecyclerView is computing a layout or scrolling");
        stringBuilder1.append(exceptionLabel());
        throw new IllegalStateException(stringBuilder1.toString());
      } 
      StringBuilder stringBuilder2 = new StringBuilder();
      stringBuilder2.append((String)stringBuilder1);
      stringBuilder2.append(exceptionLabel());
      throw new IllegalStateException(stringBuilder2.toString());
    } 
  }
  
  void assertNotInLayoutOrScroll(String paramString) {
    if (isComputingLayout()) {
      StringBuilder stringBuilder;
      if (paramString == null) {
        stringBuilder = new StringBuilder();
        stringBuilder.append("Cannot call this method while RecyclerView is computing a layout or scrolling");
        stringBuilder.append(exceptionLabel());
        throw new IllegalStateException(stringBuilder.toString());
      } 
      throw new IllegalStateException(stringBuilder);
    } 
    if (this.mDispatchScrollCounter > 0) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("");
      stringBuilder.append(exceptionLabel());
      Log.w("RecyclerView", "Cannot call this method in a scroll callback. Scroll callbacks mightbe run during a measure & layout pass where you cannot change theRecyclerView data. Any method call that might change the structureof the RecyclerView or the adapter contents should be postponed tothe next frame.", new IllegalStateException(stringBuilder.toString()));
    } 
  }
  
  boolean canReuseUpdatedViewHolder(ViewHolder paramViewHolder) {
    ItemAnimator itemAnimator = this.mItemAnimator;
    return (itemAnimator == null || itemAnimator.canReuseUpdatedViewHolder(paramViewHolder, paramViewHolder.getUnmodifiedPayloads()));
  }
  
  protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    boolean bool;
    if (paramLayoutParams instanceof LayoutParams && this.mLayout.checkLayoutParams((LayoutParams)paramLayoutParams)) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  void clearOldPositions() {
    int i = this.mChildHelper.getUnfilteredChildCount();
    for (byte b = 0; b < i; b++) {
      ViewHolder viewHolder = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(b));
      if (!viewHolder.shouldIgnore())
        viewHolder.clearOldPosition(); 
    } 
    this.mRecycler.clearOldPositions();
  }
  
  public void clearOnChildAttachStateChangeListeners() {
    List<OnChildAttachStateChangeListener> list = this.mOnChildAttachStateListeners;
    if (list != null)
      list.clear(); 
  }
  
  public void clearOnScrollListeners() {
    List<OnScrollListener> list = this.mScrollListeners;
    if (list != null)
      list.clear(); 
  }
  
  public int computeHorizontalScrollExtent() {
    LayoutManager layoutManager = this.mLayout;
    int i = 0;
    if (layoutManager == null)
      return 0; 
    if (layoutManager.canScrollHorizontally())
      i = this.mLayout.computeHorizontalScrollExtent(this.mState); 
    return i;
  }
  
  public int computeHorizontalScrollOffset() {
    LayoutManager layoutManager = this.mLayout;
    int i = 0;
    if (layoutManager == null)
      return 0; 
    if (layoutManager.canScrollHorizontally())
      i = this.mLayout.computeHorizontalScrollOffset(this.mState); 
    return i;
  }
  
  public int computeHorizontalScrollRange() {
    LayoutManager layoutManager = this.mLayout;
    int i = 0;
    if (layoutManager == null)
      return 0; 
    if (layoutManager.canScrollHorizontally())
      i = this.mLayout.computeHorizontalScrollRange(this.mState); 
    return i;
  }
  
  public int computeVerticalScrollExtent() {
    LayoutManager layoutManager = this.mLayout;
    int i = 0;
    if (layoutManager == null)
      return 0; 
    if (layoutManager.canScrollVertically())
      i = this.mLayout.computeVerticalScrollExtent(this.mState); 
    return i;
  }
  
  public int computeVerticalScrollOffset() {
    LayoutManager layoutManager = this.mLayout;
    int i = 0;
    if (layoutManager == null)
      return 0; 
    if (layoutManager.canScrollVertically())
      i = this.mLayout.computeVerticalScrollOffset(this.mState); 
    return i;
  }
  
  public int computeVerticalScrollRange() {
    LayoutManager layoutManager = this.mLayout;
    int i = 0;
    if (layoutManager == null)
      return 0; 
    if (layoutManager.canScrollVertically())
      i = this.mLayout.computeVerticalScrollRange(this.mState); 
    return i;
  }
  
  void considerReleasingGlowsOnScroll(int paramInt1, int paramInt2) {
    EdgeEffect edgeEffect = this.mLeftGlow;
    if (edgeEffect != null && !edgeEffect.isFinished() && paramInt1 > 0) {
      this.mLeftGlow.onRelease();
      bool1 = this.mLeftGlow.isFinished();
    } else {
      bool1 = false;
    } 
    edgeEffect = this.mRightGlow;
    boolean bool2 = bool1;
    if (edgeEffect != null) {
      bool2 = bool1;
      if (!edgeEffect.isFinished()) {
        bool2 = bool1;
        if (paramInt1 < 0) {
          this.mRightGlow.onRelease();
          bool2 = bool1 | this.mRightGlow.isFinished();
        } 
      } 
    } 
    edgeEffect = this.mTopGlow;
    boolean bool1 = bool2;
    if (edgeEffect != null) {
      bool1 = bool2;
      if (!edgeEffect.isFinished()) {
        bool1 = bool2;
        if (paramInt2 > 0) {
          this.mTopGlow.onRelease();
          bool1 = bool2 | this.mTopGlow.isFinished();
        } 
      } 
    } 
    edgeEffect = this.mBottomGlow;
    bool2 = bool1;
    if (edgeEffect != null) {
      bool2 = bool1;
      if (!edgeEffect.isFinished()) {
        bool2 = bool1;
        if (paramInt2 < 0) {
          this.mBottomGlow.onRelease();
          bool2 = bool1 | this.mBottomGlow.isFinished();
        } 
      } 
    } 
    if (bool2)
      ViewCompat.postInvalidateOnAnimation((View)this); 
  }
  
  void consumePendingUpdateOperations() {
    if (!this.mFirstLayoutComplete || this.mDataSetHasChangedAfterLayout) {
      TraceCompat.beginSection("RV FullInvalidate");
      dispatchLayout();
      TraceCompat.endSection();
      return;
    } 
    if (!this.mAdapterHelper.hasPendingUpdates())
      return; 
    if (this.mAdapterHelper.hasAnyUpdateTypes(4) && !this.mAdapterHelper.hasAnyUpdateTypes(11)) {
      TraceCompat.beginSection("RV PartialInvalidate");
      startInterceptRequestLayout();
      onEnterLayoutOrScroll();
      this.mAdapterHelper.preProcess();
      if (!this.mLayoutWasDefered)
        if (hasUpdatedView()) {
          dispatchLayout();
        } else {
          this.mAdapterHelper.consumePostponedUpdates();
        }  
      stopInterceptRequestLayout(true);
      onExitLayoutOrScroll();
      TraceCompat.endSection();
    } else if (this.mAdapterHelper.hasPendingUpdates()) {
      TraceCompat.beginSection("RV FullInvalidate");
      dispatchLayout();
      TraceCompat.endSection();
    } 
  }
  
  void defaultOnMeasure(int paramInt1, int paramInt2) {
    setMeasuredDimension(LayoutManager.chooseSize(paramInt1, getPaddingLeft() + getPaddingRight(), ViewCompat.getMinimumWidth((View)this)), LayoutManager.chooseSize(paramInt2, getPaddingTop() + getPaddingBottom(), ViewCompat.getMinimumHeight((View)this)));
  }
  
  void dispatchChildAttached(View paramView) {
    ViewHolder viewHolder = getChildViewHolderInt(paramView);
    onChildAttachedToWindow(paramView);
    Adapter<ViewHolder> adapter = this.mAdapter;
    if (adapter != null && viewHolder != null)
      adapter.onViewAttachedToWindow(viewHolder); 
    List<OnChildAttachStateChangeListener> list = this.mOnChildAttachStateListeners;
    if (list != null)
      for (int i = list.size() - 1; i >= 0; i--)
        ((OnChildAttachStateChangeListener)this.mOnChildAttachStateListeners.get(i)).onChildViewAttachedToWindow(paramView);  
  }
  
  void dispatchChildDetached(View paramView) {
    ViewHolder viewHolder = getChildViewHolderInt(paramView);
    onChildDetachedFromWindow(paramView);
    Adapter<ViewHolder> adapter = this.mAdapter;
    if (adapter != null && viewHolder != null)
      adapter.onViewDetachedFromWindow(viewHolder); 
    List<OnChildAttachStateChangeListener> list = this.mOnChildAttachStateListeners;
    if (list != null)
      for (int i = list.size() - 1; i >= 0; i--)
        ((OnChildAttachStateChangeListener)this.mOnChildAttachStateListeners.get(i)).onChildViewDetachedFromWindow(paramView);  
  }
  
  void dispatchLayout() {
    if (this.mAdapter == null) {
      Log.e("RecyclerView", "No adapter attached; skipping layout");
      return;
    } 
    if (this.mLayout == null) {
      Log.e("RecyclerView", "No layout manager attached; skipping layout");
      return;
    } 
    State state = this.mState;
    state.mIsMeasuring = false;
    if (state.mLayoutStep == 1) {
      dispatchLayoutStep1();
      this.mLayout.setExactMeasureSpecsFrom(this);
      dispatchLayoutStep2();
    } else if (this.mAdapterHelper.hasUpdates() || this.mLayout.getWidth() != getWidth() || this.mLayout.getHeight() != getHeight()) {
      this.mLayout.setExactMeasureSpecsFrom(this);
      dispatchLayoutStep2();
    } else {
      this.mLayout.setExactMeasureSpecsFrom(this);
    } 
    dispatchLayoutStep3();
  }
  
  public boolean dispatchNestedFling(float paramFloat1, float paramFloat2, boolean paramBoolean) {
    return getScrollingChildHelper().dispatchNestedFling(paramFloat1, paramFloat2, paramBoolean);
  }
  
  public boolean dispatchNestedPreFling(float paramFloat1, float paramFloat2) {
    return getScrollingChildHelper().dispatchNestedPreFling(paramFloat1, paramFloat2);
  }
  
  public boolean dispatchNestedPreScroll(int paramInt1, int paramInt2, int[] paramArrayOfint1, int[] paramArrayOfint2) {
    return getScrollingChildHelper().dispatchNestedPreScroll(paramInt1, paramInt2, paramArrayOfint1, paramArrayOfint2);
  }
  
  public boolean dispatchNestedPreScroll(int paramInt1, int paramInt2, int[] paramArrayOfint1, int[] paramArrayOfint2, int paramInt3) {
    return getScrollingChildHelper().dispatchNestedPreScroll(paramInt1, paramInt2, paramArrayOfint1, paramArrayOfint2, paramInt3);
  }
  
  public final void dispatchNestedScroll(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfint1, int paramInt5, int[] paramArrayOfint2) {
    getScrollingChildHelper().dispatchNestedScroll(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfint1, paramInt5, paramArrayOfint2);
  }
  
  public boolean dispatchNestedScroll(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfint) {
    return getScrollingChildHelper().dispatchNestedScroll(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfint);
  }
  
  public boolean dispatchNestedScroll(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfint, int paramInt5) {
    return getScrollingChildHelper().dispatchNestedScroll(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfint, paramInt5);
  }
  
  void dispatchOnScrollStateChanged(int paramInt) {
    LayoutManager layoutManager = this.mLayout;
    if (layoutManager != null)
      layoutManager.onScrollStateChanged(paramInt); 
    onScrollStateChanged(paramInt);
    OnScrollListener onScrollListener = this.mScrollListener;
    if (onScrollListener != null)
      onScrollListener.onScrollStateChanged(this, paramInt); 
    List<OnScrollListener> list = this.mScrollListeners;
    if (list != null)
      for (int i = list.size() - 1; i >= 0; i--)
        ((OnScrollListener)this.mScrollListeners.get(i)).onScrollStateChanged(this, paramInt);  
  }
  
  void dispatchOnScrolled(int paramInt1, int paramInt2) {
    this.mDispatchScrollCounter++;
    int i = getScrollX();
    int j = getScrollY();
    onScrollChanged(i, j, i - paramInt1, j - paramInt2);
    onScrolled(paramInt1, paramInt2);
    OnScrollListener onScrollListener = this.mScrollListener;
    if (onScrollListener != null)
      onScrollListener.onScrolled(this, paramInt1, paramInt2); 
    List<OnScrollListener> list = this.mScrollListeners;
    if (list != null)
      for (i = list.size() - 1; i >= 0; i--)
        ((OnScrollListener)this.mScrollListeners.get(i)).onScrolled(this, paramInt1, paramInt2);  
    this.mDispatchScrollCounter--;
  }
  
  void dispatchPendingImportantForAccessibilityChanges() {
    for (int i = this.mPendingAccessibilityImportanceChange.size() - 1; i >= 0; i--) {
      ViewHolder viewHolder = this.mPendingAccessibilityImportanceChange.get(i);
      if (viewHolder.itemView.getParent() == this && !viewHolder.shouldIgnore()) {
        int j = viewHolder.mPendingAccessibilityState;
        if (j != -1) {
          ViewCompat.setImportantForAccessibility(viewHolder.itemView, j);
          viewHolder.mPendingAccessibilityState = -1;
        } 
      } 
    } 
    this.mPendingAccessibilityImportanceChange.clear();
  }
  
  public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent) {
    onPopulateAccessibilityEvent(paramAccessibilityEvent);
    return true;
  }
  
  protected void dispatchRestoreInstanceState(SparseArray<Parcelable> paramSparseArray) {
    dispatchThawSelfOnly(paramSparseArray);
  }
  
  protected void dispatchSaveInstanceState(SparseArray<Parcelable> paramSparseArray) {
    dispatchFreezeSelfOnly(paramSparseArray);
  }
  
  public void draw(Canvas paramCanvas) {
    super.draw(paramCanvas);
    int i = this.mItemDecorations.size();
    boolean bool = false;
    int j;
    for (j = 0; j < i; j++)
      ((ItemDecoration)this.mItemDecorations.get(j)).onDrawOver(paramCanvas, this, this.mState); 
    EdgeEffect edgeEffect = this.mLeftGlow;
    if (edgeEffect != null && !edgeEffect.isFinished()) {
      int k = paramCanvas.save();
      if (this.mClipToPadding) {
        j = getPaddingBottom();
      } else {
        j = 0;
      } 
      paramCanvas.rotate(270.0F);
      paramCanvas.translate((-getHeight() + j), 0.0F);
      edgeEffect = this.mLeftGlow;
      if (edgeEffect != null && edgeEffect.draw(paramCanvas)) {
        i = 1;
      } else {
        i = 0;
      } 
      paramCanvas.restoreToCount(k);
    } else {
      i = 0;
    } 
    edgeEffect = this.mTopGlow;
    j = i;
    if (edgeEffect != null) {
      j = i;
      if (!edgeEffect.isFinished()) {
        int k = paramCanvas.save();
        if (this.mClipToPadding)
          paramCanvas.translate(getPaddingLeft(), getPaddingTop()); 
        edgeEffect = this.mTopGlow;
        if (edgeEffect != null && edgeEffect.draw(paramCanvas)) {
          j = 1;
        } else {
          j = 0;
        } 
        j = i | j;
        paramCanvas.restoreToCount(k);
      } 
    } 
    edgeEffect = this.mRightGlow;
    i = j;
    if (edgeEffect != null) {
      i = j;
      if (!edgeEffect.isFinished()) {
        int k = paramCanvas.save();
        int m = getWidth();
        if (this.mClipToPadding) {
          i = getPaddingTop();
        } else {
          i = 0;
        } 
        paramCanvas.rotate(90.0F);
        paramCanvas.translate(-i, -m);
        edgeEffect = this.mRightGlow;
        if (edgeEffect != null && edgeEffect.draw(paramCanvas)) {
          i = 1;
        } else {
          i = 0;
        } 
        i = j | i;
        paramCanvas.restoreToCount(k);
      } 
    } 
    edgeEffect = this.mBottomGlow;
    if (edgeEffect != null && !edgeEffect.isFinished()) {
      int k = paramCanvas.save();
      paramCanvas.rotate(180.0F);
      if (this.mClipToPadding) {
        paramCanvas.translate((-getWidth() + getPaddingRight()), (-getHeight() + getPaddingBottom()));
      } else {
        paramCanvas.translate(-getWidth(), -getHeight());
      } 
      edgeEffect = this.mBottomGlow;
      j = bool;
      if (edgeEffect != null) {
        j = bool;
        if (edgeEffect.draw(paramCanvas))
          j = 1; 
      } 
      j |= i;
      paramCanvas.restoreToCount(k);
    } else {
      j = i;
    } 
    i = j;
    if (j == 0) {
      i = j;
      if (this.mItemAnimator != null) {
        i = j;
        if (this.mItemDecorations.size() > 0) {
          i = j;
          if (this.mItemAnimator.isRunning())
            i = 1; 
        } 
      } 
    } 
    if (i != 0)
      ViewCompat.postInvalidateOnAnimation((View)this); 
  }
  
  public boolean drawChild(Canvas paramCanvas, View paramView, long paramLong) {
    return super.drawChild(paramCanvas, paramView, paramLong);
  }
  
  void ensureBottomGlow() {
    if (this.mBottomGlow != null)
      return; 
    this.mBottomGlow = this.mEdgeEffectFactory.createEdgeEffect(this, 3);
    if (this.mClipToPadding) {
      this.mBottomGlow.setSize(getMeasuredWidth() - getPaddingLeft() - getPaddingRight(), getMeasuredHeight() - getPaddingTop() - getPaddingBottom());
    } else {
      this.mBottomGlow.setSize(getMeasuredWidth(), getMeasuredHeight());
    } 
  }
  
  void ensureLeftGlow() {
    if (this.mLeftGlow != null)
      return; 
    this.mLeftGlow = this.mEdgeEffectFactory.createEdgeEffect(this, 0);
    if (this.mClipToPadding) {
      this.mLeftGlow.setSize(getMeasuredHeight() - getPaddingTop() - getPaddingBottom(), getMeasuredWidth() - getPaddingLeft() - getPaddingRight());
    } else {
      this.mLeftGlow.setSize(getMeasuredHeight(), getMeasuredWidth());
    } 
  }
  
  void ensureRightGlow() {
    if (this.mRightGlow != null)
      return; 
    this.mRightGlow = this.mEdgeEffectFactory.createEdgeEffect(this, 2);
    if (this.mClipToPadding) {
      this.mRightGlow.setSize(getMeasuredHeight() - getPaddingTop() - getPaddingBottom(), getMeasuredWidth() - getPaddingLeft() - getPaddingRight());
    } else {
      this.mRightGlow.setSize(getMeasuredHeight(), getMeasuredWidth());
    } 
  }
  
  void ensureTopGlow() {
    if (this.mTopGlow != null)
      return; 
    this.mTopGlow = this.mEdgeEffectFactory.createEdgeEffect(this, 1);
    if (this.mClipToPadding) {
      this.mTopGlow.setSize(getMeasuredWidth() - getPaddingLeft() - getPaddingRight(), getMeasuredHeight() - getPaddingTop() - getPaddingBottom());
    } else {
      this.mTopGlow.setSize(getMeasuredWidth(), getMeasuredHeight());
    } 
  }
  
  String exceptionLabel() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(" ");
    stringBuilder.append(toString());
    stringBuilder.append(", adapter:");
    stringBuilder.append(this.mAdapter);
    stringBuilder.append(", layout:");
    stringBuilder.append(this.mLayout);
    stringBuilder.append(", context:");
    stringBuilder.append(getContext());
    return stringBuilder.toString();
  }
  
  final void fillRemainingScrollValues(State paramState) {
    if (getScrollState() == 2) {
      OverScroller overScroller = this.mViewFlinger.mOverScroller;
      paramState.mRemainingScrollHorizontal = overScroller.getFinalX() - overScroller.getCurrX();
      paramState.mRemainingScrollVertical = overScroller.getFinalY() - overScroller.getCurrY();
    } else {
      paramState.mRemainingScrollHorizontal = 0;
      paramState.mRemainingScrollVertical = 0;
    } 
  }
  
  public View findChildViewUnder(float paramFloat1, float paramFloat2) {
    for (int i = this.mChildHelper.getChildCount() - 1; i >= 0; i--) {
      View view = this.mChildHelper.getChildAt(i);
      float f1 = view.getTranslationX();
      float f2 = view.getTranslationY();
      if (paramFloat1 >= view.getLeft() + f1 && paramFloat1 <= view.getRight() + f1 && paramFloat2 >= view.getTop() + f2 && paramFloat2 <= view.getBottom() + f2)
        return view; 
    } 
    return null;
  }
  
  public View findContainingItemView(View paramView) {
    ViewParent viewParent;
    for (viewParent = paramView.getParent(); viewParent != null && viewParent != this && viewParent instanceof View; viewParent = paramView.getParent())
      paramView = (View)viewParent; 
    if (viewParent != this)
      paramView = null; 
    return paramView;
  }
  
  public ViewHolder findContainingViewHolder(View paramView) {
    ViewHolder viewHolder;
    paramView = findContainingItemView(paramView);
    if (paramView == null) {
      paramView = null;
    } else {
      viewHolder = getChildViewHolder(paramView);
    } 
    return viewHolder;
  }
  
  public ViewHolder findViewHolderForAdapterPosition(int paramInt) {
    boolean bool = this.mDataSetHasChangedAfterLayout;
    ViewHolder viewHolder = null;
    if (bool)
      return null; 
    int i = this.mChildHelper.getUnfilteredChildCount();
    byte b = 0;
    while (b < i) {
      ViewHolder viewHolder1 = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(b));
      ViewHolder viewHolder2 = viewHolder;
      if (viewHolder1 != null) {
        viewHolder2 = viewHolder;
        if (!viewHolder1.isRemoved()) {
          viewHolder2 = viewHolder;
          if (getAdapterPositionFor(viewHolder1) == paramInt)
            if (this.mChildHelper.isHidden(viewHolder1.itemView)) {
              viewHolder2 = viewHolder1;
            } else {
              return viewHolder1;
            }  
        } 
      } 
      b++;
      viewHolder = viewHolder2;
    } 
    return viewHolder;
  }
  
  public ViewHolder findViewHolderForItemId(long paramLong) {
    Adapter adapter = this.mAdapter;
    ViewHolder viewHolder1 = null;
    ViewHolder viewHolder2 = null;
    ViewHolder viewHolder3 = viewHolder1;
    if (adapter != null)
      if (!adapter.hasStableIds()) {
        viewHolder3 = viewHolder1;
      } else {
        int i = this.mChildHelper.getUnfilteredChildCount();
        byte b = 0;
        while (true) {
          viewHolder3 = viewHolder2;
          if (b < i) {
            viewHolder1 = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(b));
            viewHolder3 = viewHolder2;
            if (viewHolder1 != null) {
              viewHolder3 = viewHolder2;
              if (!viewHolder1.isRemoved()) {
                viewHolder3 = viewHolder2;
                if (viewHolder1.getItemId() == paramLong)
                  if (this.mChildHelper.isHidden(viewHolder1.itemView)) {
                    viewHolder3 = viewHolder1;
                  } else {
                    return viewHolder1;
                  }  
              } 
            } 
            b++;
            viewHolder2 = viewHolder3;
            continue;
          } 
          break;
        } 
      }  
    return viewHolder3;
  }
  
  public ViewHolder findViewHolderForLayoutPosition(int paramInt) {
    return findViewHolderForPosition(paramInt, false);
  }
  
  @Deprecated
  public ViewHolder findViewHolderForPosition(int paramInt) {
    return findViewHolderForPosition(paramInt, false);
  }
  
  ViewHolder findViewHolderForPosition(int paramInt, boolean paramBoolean) {
    int i = this.mChildHelper.getUnfilteredChildCount();
    Object object = null;
    byte b = 0;
    while (b < i) {
      ViewHolder viewHolder = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(b));
      Object object1 = object;
      if (viewHolder != null) {
        object1 = object;
        if (!viewHolder.isRemoved()) {
          if (paramBoolean) {
            if (viewHolder.mPosition != paramInt) {
              object1 = object;
              continue;
            } 
          } else if (viewHolder.getLayoutPosition() != paramInt) {
            object1 = object;
            continue;
          } 
          if (this.mChildHelper.isHidden(viewHolder.itemView)) {
            object1 = viewHolder;
          } else {
            return viewHolder;
          } 
        } 
      } 
      continue;
      b++;
      object = SYNTHETIC_LOCAL_VARIABLE_7;
    } 
    return (ViewHolder)object;
  }
  
  public boolean fling(int paramInt1, int paramInt2) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mLayout : Landroidx/recyclerview/widget/RecyclerView$LayoutManager;
    //   4: astore_3
    //   5: iconst_0
    //   6: istore #4
    //   8: aload_3
    //   9: ifnonnull -> 23
    //   12: ldc 'RecyclerView'
    //   14: ldc_w 'Cannot fling without a LayoutManager set. Call setLayoutManager with a non-null argument.'
    //   17: invokestatic e : (Ljava/lang/String;Ljava/lang/String;)I
    //   20: pop
    //   21: iconst_0
    //   22: ireturn
    //   23: aload_0
    //   24: getfield mLayoutSuppressed : Z
    //   27: ifeq -> 32
    //   30: iconst_0
    //   31: ireturn
    //   32: aload_3
    //   33: invokevirtual canScrollHorizontally : ()Z
    //   36: istore #5
    //   38: aload_0
    //   39: getfield mLayout : Landroidx/recyclerview/widget/RecyclerView$LayoutManager;
    //   42: invokevirtual canScrollVertically : ()Z
    //   45: istore #6
    //   47: iload #5
    //   49: ifeq -> 66
    //   52: iload_1
    //   53: istore #7
    //   55: iload_1
    //   56: invokestatic abs : (I)I
    //   59: aload_0
    //   60: getfield mMinFlingVelocity : I
    //   63: if_icmpge -> 69
    //   66: iconst_0
    //   67: istore #7
    //   69: iload #6
    //   71: ifeq -> 88
    //   74: iload_2
    //   75: istore #8
    //   77: iload_2
    //   78: invokestatic abs : (I)I
    //   81: aload_0
    //   82: getfield mMinFlingVelocity : I
    //   85: if_icmpge -> 91
    //   88: iconst_0
    //   89: istore #8
    //   91: iload #7
    //   93: ifne -> 103
    //   96: iload #8
    //   98: ifne -> 103
    //   101: iconst_0
    //   102: ireturn
    //   103: iload #7
    //   105: i2f
    //   106: fstore #9
    //   108: iload #8
    //   110: i2f
    //   111: fstore #10
    //   113: aload_0
    //   114: fload #9
    //   116: fload #10
    //   118: invokevirtual dispatchNestedPreFling : (FF)Z
    //   121: ifne -> 257
    //   124: iload #5
    //   126: ifne -> 143
    //   129: iload #6
    //   131: ifeq -> 137
    //   134: goto -> 143
    //   137: iconst_0
    //   138: istore #11
    //   140: goto -> 146
    //   143: iconst_1
    //   144: istore #11
    //   146: aload_0
    //   147: fload #9
    //   149: fload #10
    //   151: iload #11
    //   153: invokevirtual dispatchNestedFling : (FFZ)Z
    //   156: pop
    //   157: aload_0
    //   158: getfield mOnFlingListener : Landroidx/recyclerview/widget/RecyclerView$OnFlingListener;
    //   161: astore_3
    //   162: aload_3
    //   163: ifnull -> 179
    //   166: aload_3
    //   167: iload #7
    //   169: iload #8
    //   171: invokevirtual onFling : (II)Z
    //   174: ifeq -> 179
    //   177: iconst_1
    //   178: ireturn
    //   179: iload #11
    //   181: ifeq -> 257
    //   184: iload #4
    //   186: istore_1
    //   187: iload #5
    //   189: ifeq -> 194
    //   192: iconst_1
    //   193: istore_1
    //   194: iload_1
    //   195: istore_2
    //   196: iload #6
    //   198: ifeq -> 205
    //   201: iload_1
    //   202: iconst_2
    //   203: ior
    //   204: istore_2
    //   205: aload_0
    //   206: iload_2
    //   207: iconst_1
    //   208: invokevirtual startNestedScroll : (II)Z
    //   211: pop
    //   212: aload_0
    //   213: getfield mMaxFlingVelocity : I
    //   216: istore_1
    //   217: iload_1
    //   218: ineg
    //   219: iload #7
    //   221: iload_1
    //   222: invokestatic min : (II)I
    //   225: invokestatic max : (II)I
    //   228: istore_1
    //   229: aload_0
    //   230: getfield mMaxFlingVelocity : I
    //   233: istore_2
    //   234: iload_2
    //   235: ineg
    //   236: iload #8
    //   238: iload_2
    //   239: invokestatic min : (II)I
    //   242: invokestatic max : (II)I
    //   245: istore_2
    //   246: aload_0
    //   247: getfield mViewFlinger : Landroidx/recyclerview/widget/RecyclerView$ViewFlinger;
    //   250: iload_1
    //   251: iload_2
    //   252: invokevirtual fling : (II)V
    //   255: iconst_1
    //   256: ireturn
    //   257: iconst_0
    //   258: ireturn
  }
  
  public View focusSearch(View paramView, int paramInt) {
    View view1;
    byte b;
    View view2 = this.mLayout.onInterceptFocusSearch(paramView, paramInt);
    if (view2 != null)
      return view2; 
    if (this.mAdapter != null && this.mLayout != null && !isComputingLayout() && !this.mLayoutSuppressed) {
      b = 1;
    } else {
      b = 0;
    } 
    FocusFinder focusFinder = FocusFinder.getInstance();
    if (b && (paramInt == 2 || paramInt == 1)) {
      if (this.mLayout.canScrollVertically()) {
        byte b2;
        byte b3;
        if (paramInt == 2) {
          b2 = 130;
        } else {
          b2 = 33;
        } 
        if (focusFinder.findNextFocus(this, paramView, b2) == null) {
          b3 = 1;
        } else {
          b3 = 0;
        } 
        b = b3;
        if (FORCE_ABS_FOCUS_SEARCH_DIRECTION) {
          paramInt = b2;
          b = b3;
        } 
      } else {
        b = 0;
      } 
      byte b1 = b;
      int i = paramInt;
      if (!b) {
        b1 = b;
        i = paramInt;
        if (this.mLayout.canScrollHorizontally()) {
          byte b2;
          if (this.mLayout.getLayoutDirection() == 1) {
            b = 1;
          } else {
            b = 0;
          } 
          if (paramInt == 2) {
            b2 = 1;
          } else {
            b2 = 0;
          } 
          if ((b ^ b2) != 0) {
            b = 66;
          } else {
            b = 17;
          } 
          if (focusFinder.findNextFocus(this, paramView, b) == null) {
            b2 = 1;
          } else {
            b2 = 0;
          } 
          b1 = b2;
          i = paramInt;
          if (FORCE_ABS_FOCUS_SEARCH_DIRECTION) {
            i = b;
            b1 = b2;
          } 
        } 
      } 
      if (b1 != 0) {
        consumePendingUpdateOperations();
        if (findContainingItemView(paramView) == null)
          return null; 
        startInterceptRequestLayout();
        this.mLayout.onFocusSearchFailed(paramView, i, this.mRecycler, this.mState);
        stopInterceptRequestLayout(false);
      } 
      view1 = focusFinder.findNextFocus(this, paramView, i);
      paramInt = i;
    } else {
      view1 = view1.findNextFocus(this, paramView, paramInt);
      if (view1 == null && b != 0) {
        consumePendingUpdateOperations();
        if (findContainingItemView(paramView) == null)
          return null; 
        startInterceptRequestLayout();
        view1 = this.mLayout.onFocusSearchFailed(paramView, paramInt, this.mRecycler, this.mState);
        stopInterceptRequestLayout(false);
      } 
    } 
    if (view1 != null && !view1.hasFocusable()) {
      if (getFocusedChild() == null)
        return super.focusSearch(paramView, paramInt); 
      requestChildOnScreen(view1, (View)null);
      return paramView;
    } 
    if (!isPreferredNextFocus(paramView, view1, paramInt))
      view1 = super.focusSearch(paramView, paramInt); 
    return view1;
  }
  
  protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
    LayoutManager layoutManager = this.mLayout;
    if (layoutManager != null)
      return (ViewGroup.LayoutParams)layoutManager.generateDefaultLayoutParams(); 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("RecyclerView has no LayoutManager");
    stringBuilder.append(exceptionLabel());
    throw new IllegalStateException(stringBuilder.toString());
  }
  
  public ViewGroup.LayoutParams generateLayoutParams(AttributeSet paramAttributeSet) {
    LayoutManager layoutManager = this.mLayout;
    if (layoutManager != null)
      return (ViewGroup.LayoutParams)layoutManager.generateLayoutParams(getContext(), paramAttributeSet); 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("RecyclerView has no LayoutManager");
    stringBuilder.append(exceptionLabel());
    throw new IllegalStateException(stringBuilder.toString());
  }
  
  protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    LayoutManager layoutManager = this.mLayout;
    if (layoutManager != null)
      return (ViewGroup.LayoutParams)layoutManager.generateLayoutParams(paramLayoutParams); 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("RecyclerView has no LayoutManager");
    stringBuilder.append(exceptionLabel());
    throw new IllegalStateException(stringBuilder.toString());
  }
  
  public CharSequence getAccessibilityClassName() {
    return "androidx.recyclerview.widget.RecyclerView";
  }
  
  public Adapter getAdapter() {
    return this.mAdapter;
  }
  
  int getAdapterPositionFor(ViewHolder paramViewHolder) {
    return (paramViewHolder.hasAnyOfTheFlags(524) || !paramViewHolder.isBound()) ? -1 : this.mAdapterHelper.applyPendingUpdatesToPosition(paramViewHolder.mPosition);
  }
  
  public int getBaseline() {
    LayoutManager layoutManager = this.mLayout;
    return (layoutManager != null) ? layoutManager.getBaseline() : super.getBaseline();
  }
  
  long getChangedHolderKey(ViewHolder paramViewHolder) {
    long l;
    if (this.mAdapter.hasStableIds()) {
      l = paramViewHolder.getItemId();
    } else {
      l = paramViewHolder.mPosition;
    } 
    return l;
  }
  
  public int getChildAdapterPosition(View paramView) {
    byte b;
    ViewHolder viewHolder = getChildViewHolderInt(paramView);
    if (viewHolder != null) {
      b = viewHolder.getAdapterPosition();
    } else {
      b = -1;
    } 
    return b;
  }
  
  protected int getChildDrawingOrder(int paramInt1, int paramInt2) {
    ChildDrawingOrderCallback childDrawingOrderCallback = this.mChildDrawingOrderCallback;
    return (childDrawingOrderCallback == null) ? super.getChildDrawingOrder(paramInt1, paramInt2) : childDrawingOrderCallback.onGetChildDrawingOrder(paramInt1, paramInt2);
  }
  
  public long getChildItemId(View paramView) {
    Adapter adapter = this.mAdapter;
    long l1 = -1L;
    long l2 = l1;
    if (adapter != null)
      if (!adapter.hasStableIds()) {
        l2 = l1;
      } else {
        ViewHolder viewHolder = getChildViewHolderInt(paramView);
        l2 = l1;
        if (viewHolder != null)
          l2 = viewHolder.getItemId(); 
      }  
    return l2;
  }
  
  public int getChildLayoutPosition(View paramView) {
    byte b;
    ViewHolder viewHolder = getChildViewHolderInt(paramView);
    if (viewHolder != null) {
      b = viewHolder.getLayoutPosition();
    } else {
      b = -1;
    } 
    return b;
  }
  
  @Deprecated
  public int getChildPosition(View paramView) {
    return getChildAdapterPosition(paramView);
  }
  
  public ViewHolder getChildViewHolder(View paramView) {
    ViewParent viewParent = paramView.getParent();
    if (viewParent == null || viewParent == this)
      return getChildViewHolderInt(paramView); 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("View ");
    stringBuilder.append(paramView);
    stringBuilder.append(" is not a direct child of ");
    stringBuilder.append(this);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public boolean getClipToPadding() {
    return this.mClipToPadding;
  }
  
  public RecyclerViewAccessibilityDelegate getCompatAccessibilityDelegate() {
    return this.mAccessibilityDelegate;
  }
  
  public void getDecoratedBoundsWithMargins(View paramView, Rect paramRect) {
    getDecoratedBoundsWithMarginsInt(paramView, paramRect);
  }
  
  public EdgeEffectFactory getEdgeEffectFactory() {
    return this.mEdgeEffectFactory;
  }
  
  public ItemAnimator getItemAnimator() {
    return this.mItemAnimator;
  }
  
  Rect getItemDecorInsetsForChild(View paramView) {
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    if (!layoutParams.mInsetsDirty)
      return layoutParams.mDecorInsets; 
    if (this.mState.isPreLayout() && (layoutParams.isItemChanged() || layoutParams.isViewInvalid()))
      return layoutParams.mDecorInsets; 
    Rect rect = layoutParams.mDecorInsets;
    rect.set(0, 0, 0, 0);
    int i = this.mItemDecorations.size();
    for (byte b = 0; b < i; b++) {
      this.mTempRect.set(0, 0, 0, 0);
      ((ItemDecoration)this.mItemDecorations.get(b)).getItemOffsets(this.mTempRect, paramView, this, this.mState);
      rect.left += this.mTempRect.left;
      rect.top += this.mTempRect.top;
      rect.right += this.mTempRect.right;
      rect.bottom += this.mTempRect.bottom;
    } 
    layoutParams.mInsetsDirty = false;
    return rect;
  }
  
  public ItemDecoration getItemDecorationAt(int paramInt) {
    int i = getItemDecorationCount();
    if (paramInt >= 0 && paramInt < i)
      return this.mItemDecorations.get(paramInt); 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(paramInt);
    stringBuilder.append(" is an invalid index for size ");
    stringBuilder.append(i);
    throw new IndexOutOfBoundsException(stringBuilder.toString());
  }
  
  public int getItemDecorationCount() {
    return this.mItemDecorations.size();
  }
  
  public LayoutManager getLayoutManager() {
    return this.mLayout;
  }
  
  public int getMaxFlingVelocity() {
    return this.mMaxFlingVelocity;
  }
  
  public int getMinFlingVelocity() {
    return this.mMinFlingVelocity;
  }
  
  long getNanoTime() {
    return ALLOW_THREAD_GAP_WORK ? System.nanoTime() : 0L;
  }
  
  public OnFlingListener getOnFlingListener() {
    return this.mOnFlingListener;
  }
  
  public boolean getPreserveFocusAfterLayout() {
    return this.mPreserveFocusAfterLayout;
  }
  
  public RecycledViewPool getRecycledViewPool() {
    return this.mRecycler.getRecycledViewPool();
  }
  
  public int getScrollState() {
    return this.mScrollState;
  }
  
  public boolean hasFixedSize() {
    return this.mHasFixedSize;
  }
  
  public boolean hasNestedScrollingParent() {
    return getScrollingChildHelper().hasNestedScrollingParent();
  }
  
  public boolean hasNestedScrollingParent(int paramInt) {
    return getScrollingChildHelper().hasNestedScrollingParent(paramInt);
  }
  
  public boolean hasPendingAdapterUpdates() {
    return (!this.mFirstLayoutComplete || this.mDataSetHasChangedAfterLayout || this.mAdapterHelper.hasPendingUpdates());
  }
  
  void initAdapterManager() {
    this.mAdapterHelper = new AdapterHelper(new AdapterHelper.Callback() {
          void dispatchUpdate(AdapterHelper.UpdateOp param1UpdateOp) {
            int i = param1UpdateOp.cmd;
            if (i != 1) {
              if (i != 2) {
                if (i != 4) {
                  if (i == 8)
                    RecyclerView.this.mLayout.onItemsMoved(RecyclerView.this, param1UpdateOp.positionStart, param1UpdateOp.itemCount, 1); 
                } else {
                  RecyclerView.this.mLayout.onItemsUpdated(RecyclerView.this, param1UpdateOp.positionStart, param1UpdateOp.itemCount, param1UpdateOp.payload);
                } 
              } else {
                RecyclerView.this.mLayout.onItemsRemoved(RecyclerView.this, param1UpdateOp.positionStart, param1UpdateOp.itemCount);
              } 
            } else {
              RecyclerView.this.mLayout.onItemsAdded(RecyclerView.this, param1UpdateOp.positionStart, param1UpdateOp.itemCount);
            } 
          }
          
          public RecyclerView.ViewHolder findViewHolder(int param1Int) {
            RecyclerView.ViewHolder viewHolder = RecyclerView.this.findViewHolderForPosition(param1Int, true);
            return (viewHolder == null) ? null : (RecyclerView.this.mChildHelper.isHidden(viewHolder.itemView) ? null : viewHolder);
          }
          
          public void markViewHoldersUpdated(int param1Int1, int param1Int2, Object param1Object) {
            RecyclerView.this.viewRangeUpdate(param1Int1, param1Int2, param1Object);
            RecyclerView.this.mItemsChanged = true;
          }
          
          public void offsetPositionsForAdd(int param1Int1, int param1Int2) {
            RecyclerView.this.offsetPositionRecordsForInsert(param1Int1, param1Int2);
            RecyclerView.this.mItemsAddedOrRemoved = true;
          }
          
          public void offsetPositionsForMove(int param1Int1, int param1Int2) {
            RecyclerView.this.offsetPositionRecordsForMove(param1Int1, param1Int2);
            RecyclerView.this.mItemsAddedOrRemoved = true;
          }
          
          public void offsetPositionsForRemovingInvisible(int param1Int1, int param1Int2) {
            RecyclerView.this.offsetPositionRecordsForRemove(param1Int1, param1Int2, true);
            RecyclerView recyclerView = RecyclerView.this;
            recyclerView.mItemsAddedOrRemoved = true;
            RecyclerView.State state = recyclerView.mState;
            state.mDeletedInvisibleItemCountSincePreviousLayout += param1Int2;
          }
          
          public void offsetPositionsForRemovingLaidOutOrNewView(int param1Int1, int param1Int2) {
            RecyclerView.this.offsetPositionRecordsForRemove(param1Int1, param1Int2, false);
            RecyclerView.this.mItemsAddedOrRemoved = true;
          }
          
          public void onDispatchFirstPass(AdapterHelper.UpdateOp param1UpdateOp) {
            dispatchUpdate(param1UpdateOp);
          }
          
          public void onDispatchSecondPass(AdapterHelper.UpdateOp param1UpdateOp) {
            dispatchUpdate(param1UpdateOp);
          }
        });
  }
  
  void initFastScroller(StateListDrawable paramStateListDrawable1, Drawable paramDrawable1, StateListDrawable paramStateListDrawable2, Drawable paramDrawable2) {
    if (paramStateListDrawable1 != null && paramDrawable1 != null && paramStateListDrawable2 != null && paramDrawable2 != null) {
      Resources resources = getContext().getResources();
      new FastScroller(this, paramStateListDrawable1, paramDrawable1, paramStateListDrawable2, paramDrawable2, resources.getDimensionPixelSize(R.dimen.fastscroll_default_thickness), resources.getDimensionPixelSize(R.dimen.fastscroll_minimum_range), resources.getDimensionPixelOffset(R.dimen.fastscroll_margin));
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Trying to set fast scroller without both required drawables.");
    stringBuilder.append(exceptionLabel());
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  void invalidateGlows() {
    this.mBottomGlow = null;
    this.mTopGlow = null;
    this.mRightGlow = null;
    this.mLeftGlow = null;
  }
  
  public void invalidateItemDecorations() {
    if (this.mItemDecorations.size() == 0)
      return; 
    LayoutManager layoutManager = this.mLayout;
    if (layoutManager != null)
      layoutManager.assertNotInLayoutOrScroll("Cannot invalidate item decorations during a scroll or layout"); 
    markItemDecorInsetsDirty();
    requestLayout();
  }
  
  boolean isAccessibilityEnabled() {
    boolean bool;
    AccessibilityManager accessibilityManager = this.mAccessibilityManager;
    if (accessibilityManager != null && accessibilityManager.isEnabled()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isAnimating() {
    boolean bool;
    ItemAnimator itemAnimator = this.mItemAnimator;
    if (itemAnimator != null && itemAnimator.isRunning()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean isAttachedToWindow() {
    return this.mIsAttached;
  }
  
  public boolean isComputingLayout() {
    boolean bool;
    if (this.mLayoutOrScrollCounter > 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  @Deprecated
  public boolean isLayoutFrozen() {
    return isLayoutSuppressed();
  }
  
  public final boolean isLayoutSuppressed() {
    return this.mLayoutSuppressed;
  }
  
  public boolean isNestedScrollingEnabled() {
    return getScrollingChildHelper().isNestedScrollingEnabled();
  }
  
  void jumpToPositionForSmoothScroller(int paramInt) {
    if (this.mLayout == null)
      return; 
    setScrollState(2);
    this.mLayout.scrollToPosition(paramInt);
    awakenScrollBars();
  }
  
  void markItemDecorInsetsDirty() {
    int i = this.mChildHelper.getUnfilteredChildCount();
    for (byte b = 0; b < i; b++)
      ((LayoutParams)this.mChildHelper.getUnfilteredChildAt(b).getLayoutParams()).mInsetsDirty = true; 
    this.mRecycler.markItemDecorInsetsDirty();
  }
  
  void markKnownViewsInvalid() {
    int i = this.mChildHelper.getUnfilteredChildCount();
    for (byte b = 0; b < i; b++) {
      ViewHolder viewHolder = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(b));
      if (viewHolder != null && !viewHolder.shouldIgnore())
        viewHolder.addFlags(6); 
    } 
    markItemDecorInsetsDirty();
    this.mRecycler.markKnownViewsInvalid();
  }
  
  public void offsetChildrenHorizontal(int paramInt) {
    int i = this.mChildHelper.getChildCount();
    for (byte b = 0; b < i; b++)
      this.mChildHelper.getChildAt(b).offsetLeftAndRight(paramInt); 
  }
  
  public void offsetChildrenVertical(int paramInt) {
    int i = this.mChildHelper.getChildCount();
    for (byte b = 0; b < i; b++)
      this.mChildHelper.getChildAt(b).offsetTopAndBottom(paramInt); 
  }
  
  void offsetPositionRecordsForInsert(int paramInt1, int paramInt2) {
    int i = this.mChildHelper.getUnfilteredChildCount();
    for (byte b = 0; b < i; b++) {
      ViewHolder viewHolder = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(b));
      if (viewHolder != null && !viewHolder.shouldIgnore() && viewHolder.mPosition >= paramInt1) {
        viewHolder.offsetPosition(paramInt2, false);
        this.mState.mStructureChanged = true;
      } 
    } 
    this.mRecycler.offsetPositionRecordsForInsert(paramInt1, paramInt2);
    requestLayout();
  }
  
  void offsetPositionRecordsForMove(int paramInt1, int paramInt2) {
    int j;
    int k;
    boolean bool;
    int i = this.mChildHelper.getUnfilteredChildCount();
    if (paramInt1 < paramInt2) {
      j = paramInt1;
      k = paramInt2;
      bool = true;
    } else {
      k = paramInt1;
      j = paramInt2;
      bool = true;
    } 
    for (byte b = 0; b < i; b++) {
      ViewHolder viewHolder = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(b));
      if (viewHolder != null && viewHolder.mPosition >= j && viewHolder.mPosition <= k) {
        if (viewHolder.mPosition == paramInt1) {
          viewHolder.offsetPosition(paramInt2 - paramInt1, false);
        } else {
          viewHolder.offsetPosition(bool, false);
        } 
        this.mState.mStructureChanged = true;
      } 
    } 
    this.mRecycler.offsetPositionRecordsForMove(paramInt1, paramInt2);
    requestLayout();
  }
  
  void offsetPositionRecordsForRemove(int paramInt1, int paramInt2, boolean paramBoolean) {
    int i = this.mChildHelper.getUnfilteredChildCount();
    for (byte b = 0; b < i; b++) {
      ViewHolder viewHolder = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(b));
      if (viewHolder != null && !viewHolder.shouldIgnore())
        if (viewHolder.mPosition >= paramInt1 + paramInt2) {
          viewHolder.offsetPosition(-paramInt2, paramBoolean);
          this.mState.mStructureChanged = true;
        } else if (viewHolder.mPosition >= paramInt1) {
          viewHolder.flagRemovedAndOffsetPosition(paramInt1 - 1, -paramInt2, paramBoolean);
          this.mState.mStructureChanged = true;
        }  
    } 
    this.mRecycler.offsetPositionRecordsForRemove(paramInt1, paramInt2, paramBoolean);
    requestLayout();
  }
  
  protected void onAttachedToWindow() {
    // Byte code:
    //   0: aload_0
    //   1: invokespecial onAttachedToWindow : ()V
    //   4: aload_0
    //   5: iconst_0
    //   6: putfield mLayoutOrScrollCounter : I
    //   9: iconst_1
    //   10: istore_1
    //   11: aload_0
    //   12: iconst_1
    //   13: putfield mIsAttached : Z
    //   16: aload_0
    //   17: getfield mFirstLayoutComplete : Z
    //   20: ifeq -> 33
    //   23: aload_0
    //   24: invokevirtual isLayoutRequested : ()Z
    //   27: ifne -> 33
    //   30: goto -> 35
    //   33: iconst_0
    //   34: istore_1
    //   35: aload_0
    //   36: iload_1
    //   37: putfield mFirstLayoutComplete : Z
    //   40: aload_0
    //   41: getfield mLayout : Landroidx/recyclerview/widget/RecyclerView$LayoutManager;
    //   44: astore_2
    //   45: aload_2
    //   46: ifnull -> 54
    //   49: aload_2
    //   50: aload_0
    //   51: invokevirtual dispatchAttachedToWindow : (Landroidx/recyclerview/widget/RecyclerView;)V
    //   54: aload_0
    //   55: iconst_0
    //   56: putfield mPostedAnimatorRunner : Z
    //   59: getstatic androidx/recyclerview/widget/RecyclerView.ALLOW_THREAD_GAP_WORK : Z
    //   62: ifeq -> 163
    //   65: aload_0
    //   66: getstatic androidx/recyclerview/widget/GapWorker.sGapWorker : Ljava/lang/ThreadLocal;
    //   69: invokevirtual get : ()Ljava/lang/Object;
    //   72: checkcast androidx/recyclerview/widget/GapWorker
    //   75: putfield mGapWorker : Landroidx/recyclerview/widget/GapWorker;
    //   78: aload_0
    //   79: getfield mGapWorker : Landroidx/recyclerview/widget/GapWorker;
    //   82: ifnonnull -> 155
    //   85: aload_0
    //   86: new androidx/recyclerview/widget/GapWorker
    //   89: dup
    //   90: invokespecial <init> : ()V
    //   93: putfield mGapWorker : Landroidx/recyclerview/widget/GapWorker;
    //   96: aload_0
    //   97: invokestatic getDisplay : (Landroid/view/View;)Landroid/view/Display;
    //   100: astore_2
    //   101: aload_0
    //   102: invokevirtual isInEditMode : ()Z
    //   105: ifne -> 128
    //   108: aload_2
    //   109: ifnull -> 128
    //   112: aload_2
    //   113: invokevirtual getRefreshRate : ()F
    //   116: fstore_3
    //   117: fload_3
    //   118: ldc_w 30.0
    //   121: fcmpl
    //   122: iflt -> 128
    //   125: goto -> 132
    //   128: ldc_w 60.0
    //   131: fstore_3
    //   132: aload_0
    //   133: getfield mGapWorker : Landroidx/recyclerview/widget/GapWorker;
    //   136: ldc_w 1.0E9
    //   139: fload_3
    //   140: fdiv
    //   141: f2l
    //   142: putfield mFrameIntervalNs : J
    //   145: getstatic androidx/recyclerview/widget/GapWorker.sGapWorker : Ljava/lang/ThreadLocal;
    //   148: aload_0
    //   149: getfield mGapWorker : Landroidx/recyclerview/widget/GapWorker;
    //   152: invokevirtual set : (Ljava/lang/Object;)V
    //   155: aload_0
    //   156: getfield mGapWorker : Landroidx/recyclerview/widget/GapWorker;
    //   159: aload_0
    //   160: invokevirtual add : (Landroidx/recyclerview/widget/RecyclerView;)V
    //   163: return
  }
  
  public void onChildAttachedToWindow(View paramView) {}
  
  public void onChildDetachedFromWindow(View paramView) {}
  
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    ItemAnimator itemAnimator = this.mItemAnimator;
    if (itemAnimator != null)
      itemAnimator.endAnimations(); 
    stopScroll();
    this.mIsAttached = false;
    LayoutManager layoutManager = this.mLayout;
    if (layoutManager != null)
      layoutManager.dispatchDetachedFromWindow(this, this.mRecycler); 
    this.mPendingAccessibilityImportanceChange.clear();
    removeCallbacks(this.mItemAnimatorRunner);
    this.mViewInfoStore.onDetach();
    if (ALLOW_THREAD_GAP_WORK) {
      GapWorker gapWorker = this.mGapWorker;
      if (gapWorker != null) {
        gapWorker.remove(this);
        this.mGapWorker = null;
      } 
    } 
  }
  
  public void onDraw(Canvas paramCanvas) {
    super.onDraw(paramCanvas);
    int i = this.mItemDecorations.size();
    for (byte b = 0; b < i; b++)
      ((ItemDecoration)this.mItemDecorations.get(b)).onDraw(paramCanvas, this, this.mState); 
  }
  
  void onEnterLayoutOrScroll() {
    this.mLayoutOrScrollCounter++;
  }
  
  void onExitLayoutOrScroll() {
    onExitLayoutOrScroll(true);
  }
  
  void onExitLayoutOrScroll(boolean paramBoolean) {
    this.mLayoutOrScrollCounter--;
    if (this.mLayoutOrScrollCounter < 1) {
      this.mLayoutOrScrollCounter = 0;
      if (paramBoolean) {
        dispatchContentChangedIfNecessary();
        dispatchPendingImportantForAccessibilityChanges();
      } 
    } 
  }
  
  public boolean onGenericMotionEvent(MotionEvent paramMotionEvent) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mLayout : Landroidx/recyclerview/widget/RecyclerView$LayoutManager;
    //   4: ifnonnull -> 9
    //   7: iconst_0
    //   8: ireturn
    //   9: aload_0
    //   10: getfield mLayoutSuppressed : Z
    //   13: ifeq -> 18
    //   16: iconst_0
    //   17: ireturn
    //   18: aload_1
    //   19: invokevirtual getAction : ()I
    //   22: bipush #8
    //   24: if_icmpne -> 172
    //   27: aload_1
    //   28: invokevirtual getSource : ()I
    //   31: iconst_2
    //   32: iand
    //   33: ifeq -> 87
    //   36: aload_0
    //   37: getfield mLayout : Landroidx/recyclerview/widget/RecyclerView$LayoutManager;
    //   40: invokevirtual canScrollVertically : ()Z
    //   43: ifeq -> 57
    //   46: aload_1
    //   47: bipush #9
    //   49: invokevirtual getAxisValue : (I)F
    //   52: fneg
    //   53: fstore_2
    //   54: goto -> 59
    //   57: fconst_0
    //   58: fstore_2
    //   59: fload_2
    //   60: fstore_3
    //   61: aload_0
    //   62: getfield mLayout : Landroidx/recyclerview/widget/RecyclerView$LayoutManager;
    //   65: invokevirtual canScrollHorizontally : ()Z
    //   68: ifeq -> 138
    //   71: aload_1
    //   72: bipush #10
    //   74: invokevirtual getAxisValue : (I)F
    //   77: fstore #4
    //   79: fload_2
    //   80: fstore_3
    //   81: fload #4
    //   83: fstore_2
    //   84: goto -> 140
    //   87: aload_1
    //   88: invokevirtual getSource : ()I
    //   91: ldc_w 4194304
    //   94: iand
    //   95: ifeq -> 136
    //   98: aload_1
    //   99: bipush #26
    //   101: invokevirtual getAxisValue : (I)F
    //   104: fstore_2
    //   105: aload_0
    //   106: getfield mLayout : Landroidx/recyclerview/widget/RecyclerView$LayoutManager;
    //   109: invokevirtual canScrollVertically : ()Z
    //   112: ifeq -> 121
    //   115: fload_2
    //   116: fneg
    //   117: fstore_3
    //   118: goto -> 138
    //   121: aload_0
    //   122: getfield mLayout : Landroidx/recyclerview/widget/RecyclerView$LayoutManager;
    //   125: invokevirtual canScrollHorizontally : ()Z
    //   128: ifeq -> 136
    //   131: fconst_0
    //   132: fstore_3
    //   133: goto -> 140
    //   136: fconst_0
    //   137: fstore_3
    //   138: fconst_0
    //   139: fstore_2
    //   140: fload_3
    //   141: fconst_0
    //   142: fcmpl
    //   143: ifne -> 152
    //   146: fload_2
    //   147: fconst_0
    //   148: fcmpl
    //   149: ifeq -> 172
    //   152: aload_0
    //   153: fload_2
    //   154: aload_0
    //   155: getfield mScaledHorizontalScrollFactor : F
    //   158: fmul
    //   159: f2i
    //   160: fload_3
    //   161: aload_0
    //   162: getfield mScaledVerticalScrollFactor : F
    //   165: fmul
    //   166: f2i
    //   167: aload_1
    //   168: invokevirtual scrollByInternal : (IILandroid/view/MotionEvent;)Z
    //   171: pop
    //   172: iconst_0
    //   173: ireturn
  }
  
  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent) {
    StringBuilder stringBuilder;
    boolean bool1 = this.mLayoutSuppressed;
    boolean bool = false;
    if (bool1)
      return false; 
    this.mInterceptingOnItemTouchListener = null;
    if (findInterceptingOnItemTouchListener(paramMotionEvent)) {
      cancelScroll();
      return true;
    } 
    LayoutManager layoutManager = this.mLayout;
    if (layoutManager == null)
      return false; 
    bool1 = layoutManager.canScrollHorizontally();
    boolean bool2 = this.mLayout.canScrollVertically();
    if (this.mVelocityTracker == null)
      this.mVelocityTracker = VelocityTracker.obtain(); 
    this.mVelocityTracker.addMovement(paramMotionEvent);
    int i = paramMotionEvent.getActionMasked();
    int j = paramMotionEvent.getActionIndex();
    if (i != 0) {
      if (i != 1) {
        if (i != 2) {
          if (i != 3) {
            if (i != 5) {
              if (i == 6)
                onPointerUp(paramMotionEvent); 
            } else {
              this.mScrollPointerId = paramMotionEvent.getPointerId(j);
              i = (int)(paramMotionEvent.getX(j) + 0.5F);
              this.mLastTouchX = i;
              this.mInitialTouchX = i;
              j = (int)(paramMotionEvent.getY(j) + 0.5F);
              this.mLastTouchY = j;
              this.mInitialTouchY = j;
            } 
          } else {
            cancelScroll();
          } 
        } else {
          i = paramMotionEvent.findPointerIndex(this.mScrollPointerId);
          if (i < 0) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Error processing scroll; pointer index for id ");
            stringBuilder.append(this.mScrollPointerId);
            stringBuilder.append(" not found. Did any MotionEvents get skipped?");
            Log.e("RecyclerView", stringBuilder.toString());
            return false;
          } 
          j = (int)(stringBuilder.getX(i) + 0.5F);
          int k = (int)(stringBuilder.getY(i) + 0.5F);
          if (this.mScrollState != 1) {
            i = this.mInitialTouchX;
            int m = this.mInitialTouchY;
            if (bool1 && Math.abs(j - i) > this.mTouchSlop) {
              this.mLastTouchX = j;
              j = 1;
            } else {
              j = 0;
            } 
            i = j;
            if (bool2) {
              i = j;
              if (Math.abs(k - m) > this.mTouchSlop) {
                this.mLastTouchY = k;
                i = 1;
              } 
            } 
            if (i != 0)
              setScrollState(1); 
          } 
        } 
      } else {
        this.mVelocityTracker.clear();
        stopNestedScroll(0);
      } 
    } else {
      if (this.mIgnoreMotionEventTillDown)
        this.mIgnoreMotionEventTillDown = false; 
      this.mScrollPointerId = stringBuilder.getPointerId(0);
      j = (int)(stringBuilder.getX() + 0.5F);
      this.mLastTouchX = j;
      this.mInitialTouchX = j;
      j = (int)(stringBuilder.getY() + 0.5F);
      this.mLastTouchY = j;
      this.mInitialTouchY = j;
      if (this.mScrollState == 2) {
        getParent().requestDisallowInterceptTouchEvent(true);
        setScrollState(1);
        stopNestedScroll(1);
      } 
      int[] arrayOfInt = this.mNestedOffsets;
      arrayOfInt[1] = 0;
      arrayOfInt[0] = 0;
      if (bool1) {
        j = 1;
      } else {
        j = 0;
      } 
      i = j;
      if (bool2)
        i = j | 0x2; 
      startNestedScroll(i, 0);
    } 
    if (this.mScrollState == 1)
      bool = true; 
    return bool;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    TraceCompat.beginSection("RV OnLayout");
    dispatchLayout();
    TraceCompat.endSection();
    this.mFirstLayoutComplete = true;
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    LayoutManager layoutManager = this.mLayout;
    if (layoutManager == null) {
      defaultOnMeasure(paramInt1, paramInt2);
      return;
    } 
    boolean bool = layoutManager.isAutoMeasureEnabled();
    boolean bool1 = false;
    if (bool) {
      int i = View.MeasureSpec.getMode(paramInt1);
      int j = View.MeasureSpec.getMode(paramInt2);
      this.mLayout.onMeasure(this.mRecycler, this.mState, paramInt1, paramInt2);
      boolean bool2 = bool1;
      if (i == 1073741824) {
        bool2 = bool1;
        if (j == 1073741824)
          bool2 = true; 
      } 
      if (bool2 || this.mAdapter == null)
        return; 
      if (this.mState.mLayoutStep == 1)
        dispatchLayoutStep1(); 
      this.mLayout.setMeasureSpecs(paramInt1, paramInt2);
      this.mState.mIsMeasuring = true;
      dispatchLayoutStep2();
      this.mLayout.setMeasuredDimensionFromChildren(paramInt1, paramInt2);
      if (this.mLayout.shouldMeasureTwice()) {
        this.mLayout.setMeasureSpecs(View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 1073741824), View.MeasureSpec.makeMeasureSpec(getMeasuredHeight(), 1073741824));
        this.mState.mIsMeasuring = true;
        dispatchLayoutStep2();
        this.mLayout.setMeasuredDimensionFromChildren(paramInt1, paramInt2);
      } 
    } else {
      if (this.mHasFixedSize) {
        this.mLayout.onMeasure(this.mRecycler, this.mState, paramInt1, paramInt2);
        return;
      } 
      if (this.mAdapterUpdateDuringMeasure) {
        startInterceptRequestLayout();
        onEnterLayoutOrScroll();
        processAdapterUpdatesAndSetAnimationFlags();
        onExitLayoutOrScroll();
        if (this.mState.mRunPredictiveAnimations) {
          this.mState.mInPreLayout = true;
        } else {
          this.mAdapterHelper.consumeUpdatesInOnePass();
          this.mState.mInPreLayout = false;
        } 
        this.mAdapterUpdateDuringMeasure = false;
        stopInterceptRequestLayout(false);
      } else if (this.mState.mRunPredictiveAnimations) {
        setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight());
        return;
      } 
      Adapter adapter = this.mAdapter;
      if (adapter != null) {
        this.mState.mItemCount = adapter.getItemCount();
      } else {
        this.mState.mItemCount = 0;
      } 
      startInterceptRequestLayout();
      this.mLayout.onMeasure(this.mRecycler, this.mState, paramInt1, paramInt2);
      stopInterceptRequestLayout(false);
      this.mState.mInPreLayout = false;
    } 
  }
  
  protected boolean onRequestFocusInDescendants(int paramInt, Rect paramRect) {
    return isComputingLayout() ? false : super.onRequestFocusInDescendants(paramInt, paramRect);
  }
  
  protected void onRestoreInstanceState(Parcelable paramParcelable) {
    if (!(paramParcelable instanceof SavedState)) {
      super.onRestoreInstanceState(paramParcelable);
      return;
    } 
    this.mPendingSavedState = (SavedState)paramParcelable;
    super.onRestoreInstanceState(this.mPendingSavedState.getSuperState());
    if (this.mLayout != null && this.mPendingSavedState.mLayoutState != null)
      this.mLayout.onRestoreInstanceState(this.mPendingSavedState.mLayoutState); 
  }
  
  protected Parcelable onSaveInstanceState() {
    SavedState savedState1 = new SavedState(super.onSaveInstanceState());
    SavedState savedState2 = this.mPendingSavedState;
    if (savedState2 != null) {
      savedState1.copyFrom(savedState2);
    } else {
      LayoutManager layoutManager = this.mLayout;
      if (layoutManager != null) {
        savedState1.mLayoutState = layoutManager.onSaveInstanceState();
      } else {
        savedState1.mLayoutState = null;
      } 
    } 
    return (Parcelable)savedState1;
  }
  
  public void onScrollStateChanged(int paramInt) {}
  
  public void onScrolled(int paramInt1, int paramInt2) {}
  
  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    if (paramInt1 != paramInt3 || paramInt2 != paramInt4)
      invalidateGlows(); 
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mLayoutSuppressed : Z
    //   4: istore_2
    //   5: iconst_0
    //   6: istore_3
    //   7: iload_2
    //   8: ifne -> 1057
    //   11: aload_0
    //   12: getfield mIgnoreMotionEventTillDown : Z
    //   15: ifeq -> 21
    //   18: goto -> 1057
    //   21: aload_0
    //   22: aload_1
    //   23: invokespecial dispatchToOnItemTouchListeners : (Landroid/view/MotionEvent;)Z
    //   26: ifeq -> 35
    //   29: aload_0
    //   30: invokespecial cancelScroll : ()V
    //   33: iconst_1
    //   34: ireturn
    //   35: aload_0
    //   36: getfield mLayout : Landroidx/recyclerview/widget/RecyclerView$LayoutManager;
    //   39: astore #4
    //   41: aload #4
    //   43: ifnonnull -> 48
    //   46: iconst_0
    //   47: ireturn
    //   48: aload #4
    //   50: invokevirtual canScrollHorizontally : ()Z
    //   53: istore #5
    //   55: aload_0
    //   56: getfield mLayout : Landroidx/recyclerview/widget/RecyclerView$LayoutManager;
    //   59: invokevirtual canScrollVertically : ()Z
    //   62: istore_2
    //   63: aload_0
    //   64: getfield mVelocityTracker : Landroid/view/VelocityTracker;
    //   67: ifnonnull -> 77
    //   70: aload_0
    //   71: invokestatic obtain : ()Landroid/view/VelocityTracker;
    //   74: putfield mVelocityTracker : Landroid/view/VelocityTracker;
    //   77: aload_1
    //   78: invokevirtual getActionMasked : ()I
    //   81: istore #6
    //   83: aload_1
    //   84: invokevirtual getActionIndex : ()I
    //   87: istore #7
    //   89: iload #6
    //   91: ifne -> 110
    //   94: aload_0
    //   95: getfield mNestedOffsets : [I
    //   98: astore #4
    //   100: aload #4
    //   102: iconst_1
    //   103: iconst_0
    //   104: iastore
    //   105: aload #4
    //   107: iconst_0
    //   108: iconst_0
    //   109: iastore
    //   110: aload_1
    //   111: invokestatic obtain : (Landroid/view/MotionEvent;)Landroid/view/MotionEvent;
    //   114: astore #4
    //   116: aload_0
    //   117: getfield mNestedOffsets : [I
    //   120: astore #8
    //   122: aload #4
    //   124: aload #8
    //   126: iconst_0
    //   127: iaload
    //   128: i2f
    //   129: aload #8
    //   131: iconst_1
    //   132: iaload
    //   133: i2f
    //   134: invokevirtual offsetLocation : (FF)V
    //   137: iload #6
    //   139: ifeq -> 942
    //   142: iload #6
    //   144: iconst_1
    //   145: if_icmpeq -> 827
    //   148: iload #6
    //   150: iconst_2
    //   151: if_icmpeq -> 266
    //   154: iload #6
    //   156: iconst_3
    //   157: if_icmpeq -> 256
    //   160: iload #6
    //   162: iconst_5
    //   163: if_icmpeq -> 190
    //   166: iload #6
    //   168: bipush #6
    //   170: if_icmpeq -> 179
    //   173: iload_3
    //   174: istore #9
    //   176: goto -> 1036
    //   179: aload_0
    //   180: aload_1
    //   181: invokespecial onPointerUp : (Landroid/view/MotionEvent;)V
    //   184: iload_3
    //   185: istore #9
    //   187: goto -> 1036
    //   190: aload_0
    //   191: aload_1
    //   192: iload #7
    //   194: invokevirtual getPointerId : (I)I
    //   197: putfield mScrollPointerId : I
    //   200: aload_1
    //   201: iload #7
    //   203: invokevirtual getX : (I)F
    //   206: ldc_w 0.5
    //   209: fadd
    //   210: f2i
    //   211: istore #6
    //   213: aload_0
    //   214: iload #6
    //   216: putfield mLastTouchX : I
    //   219: aload_0
    //   220: iload #6
    //   222: putfield mInitialTouchX : I
    //   225: aload_1
    //   226: iload #7
    //   228: invokevirtual getY : (I)F
    //   231: ldc_w 0.5
    //   234: fadd
    //   235: f2i
    //   236: istore #7
    //   238: aload_0
    //   239: iload #7
    //   241: putfield mLastTouchY : I
    //   244: aload_0
    //   245: iload #7
    //   247: putfield mInitialTouchY : I
    //   250: iload_3
    //   251: istore #9
    //   253: goto -> 1036
    //   256: aload_0
    //   257: invokespecial cancelScroll : ()V
    //   260: iload_3
    //   261: istore #9
    //   263: goto -> 1036
    //   266: aload_1
    //   267: aload_0
    //   268: getfield mScrollPointerId : I
    //   271: invokevirtual findPointerIndex : (I)I
    //   274: istore #7
    //   276: iload #7
    //   278: ifge -> 326
    //   281: new java/lang/StringBuilder
    //   284: dup
    //   285: invokespecial <init> : ()V
    //   288: astore_1
    //   289: aload_1
    //   290: ldc_w 'Error processing scroll; pointer index for id '
    //   293: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   296: pop
    //   297: aload_1
    //   298: aload_0
    //   299: getfield mScrollPointerId : I
    //   302: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   305: pop
    //   306: aload_1
    //   307: ldc_w ' not found. Did any MotionEvents get skipped?'
    //   310: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   313: pop
    //   314: ldc 'RecyclerView'
    //   316: aload_1
    //   317: invokevirtual toString : ()Ljava/lang/String;
    //   320: invokestatic e : (Ljava/lang/String;Ljava/lang/String;)I
    //   323: pop
    //   324: iconst_0
    //   325: ireturn
    //   326: aload_1
    //   327: iload #7
    //   329: invokevirtual getX : (I)F
    //   332: ldc_w 0.5
    //   335: fadd
    //   336: f2i
    //   337: istore #10
    //   339: aload_1
    //   340: iload #7
    //   342: invokevirtual getY : (I)F
    //   345: ldc_w 0.5
    //   348: fadd
    //   349: f2i
    //   350: istore #11
    //   352: aload_0
    //   353: getfield mLastTouchX : I
    //   356: iload #10
    //   358: isub
    //   359: istore #7
    //   361: aload_0
    //   362: getfield mLastTouchY : I
    //   365: iload #11
    //   367: isub
    //   368: istore #9
    //   370: iload #7
    //   372: istore #6
    //   374: iload #9
    //   376: istore #12
    //   378: aload_0
    //   379: getfield mScrollState : I
    //   382: iconst_1
    //   383: if_icmpeq -> 543
    //   386: iload #7
    //   388: istore #12
    //   390: iload #5
    //   392: ifeq -> 444
    //   395: iload #7
    //   397: ifle -> 416
    //   400: iconst_0
    //   401: iload #7
    //   403: aload_0
    //   404: getfield mTouchSlop : I
    //   407: isub
    //   408: invokestatic max : (II)I
    //   411: istore #7
    //   413: goto -> 429
    //   416: iconst_0
    //   417: iload #7
    //   419: aload_0
    //   420: getfield mTouchSlop : I
    //   423: iadd
    //   424: invokestatic min : (II)I
    //   427: istore #7
    //   429: iload #7
    //   431: istore #12
    //   433: iload #7
    //   435: ifeq -> 444
    //   438: iconst_1
    //   439: istore #6
    //   441: goto -> 451
    //   444: iconst_0
    //   445: istore #6
    //   447: iload #12
    //   449: istore #7
    //   451: iload #9
    //   453: istore #13
    //   455: iload #6
    //   457: istore #14
    //   459: iload_2
    //   460: ifeq -> 517
    //   463: iload #9
    //   465: ifle -> 484
    //   468: iconst_0
    //   469: iload #9
    //   471: aload_0
    //   472: getfield mTouchSlop : I
    //   475: isub
    //   476: invokestatic max : (II)I
    //   479: istore #12
    //   481: goto -> 497
    //   484: iconst_0
    //   485: iload #9
    //   487: aload_0
    //   488: getfield mTouchSlop : I
    //   491: iadd
    //   492: invokestatic min : (II)I
    //   495: istore #12
    //   497: iload #12
    //   499: istore #13
    //   501: iload #6
    //   503: istore #14
    //   505: iload #12
    //   507: ifeq -> 517
    //   510: iconst_1
    //   511: istore #14
    //   513: iload #12
    //   515: istore #13
    //   517: iload #7
    //   519: istore #6
    //   521: iload #13
    //   523: istore #12
    //   525: iload #14
    //   527: ifeq -> 543
    //   530: aload_0
    //   531: iconst_1
    //   532: invokevirtual setScrollState : (I)V
    //   535: iload #13
    //   537: istore #12
    //   539: iload #7
    //   541: istore #6
    //   543: iload #6
    //   545: istore #13
    //   547: iload_3
    //   548: istore #9
    //   550: aload_0
    //   551: getfield mScrollState : I
    //   554: iconst_1
    //   555: if_icmpne -> 1036
    //   558: aload_0
    //   559: getfield mReusableIntPair : [I
    //   562: astore #8
    //   564: aload #8
    //   566: iconst_0
    //   567: iconst_0
    //   568: iastore
    //   569: aload #8
    //   571: iconst_1
    //   572: iconst_0
    //   573: iastore
    //   574: iload #5
    //   576: ifeq -> 586
    //   579: iload #13
    //   581: istore #9
    //   583: goto -> 589
    //   586: iconst_0
    //   587: istore #9
    //   589: iload_2
    //   590: ifeq -> 600
    //   593: iload #12
    //   595: istore #14
    //   597: goto -> 603
    //   600: iconst_0
    //   601: istore #14
    //   603: iload #13
    //   605: istore #6
    //   607: iload #12
    //   609: istore #7
    //   611: aload_0
    //   612: iload #9
    //   614: iload #14
    //   616: aload_0
    //   617: getfield mReusableIntPair : [I
    //   620: aload_0
    //   621: getfield mScrollOffset : [I
    //   624: iconst_0
    //   625: invokevirtual dispatchNestedPreScroll : (II[I[II)Z
    //   628: ifeq -> 707
    //   631: aload_0
    //   632: getfield mReusableIntPair : [I
    //   635: astore #8
    //   637: iload #13
    //   639: aload #8
    //   641: iconst_0
    //   642: iaload
    //   643: isub
    //   644: istore #6
    //   646: iload #12
    //   648: aload #8
    //   650: iconst_1
    //   651: iaload
    //   652: isub
    //   653: istore #7
    //   655: aload_0
    //   656: getfield mNestedOffsets : [I
    //   659: astore #15
    //   661: aload #15
    //   663: iconst_0
    //   664: iaload
    //   665: istore #12
    //   667: aload_0
    //   668: getfield mScrollOffset : [I
    //   671: astore #8
    //   673: aload #15
    //   675: iconst_0
    //   676: iload #12
    //   678: aload #8
    //   680: iconst_0
    //   681: iaload
    //   682: iadd
    //   683: iastore
    //   684: aload #15
    //   686: iconst_1
    //   687: aload #15
    //   689: iconst_1
    //   690: iaload
    //   691: aload #8
    //   693: iconst_1
    //   694: iaload
    //   695: iadd
    //   696: iastore
    //   697: aload_0
    //   698: invokevirtual getParent : ()Landroid/view/ViewParent;
    //   701: iconst_1
    //   702: invokeinterface requestDisallowInterceptTouchEvent : (Z)V
    //   707: aload_0
    //   708: getfield mScrollOffset : [I
    //   711: astore #8
    //   713: aload_0
    //   714: iload #10
    //   716: aload #8
    //   718: iconst_0
    //   719: iaload
    //   720: isub
    //   721: putfield mLastTouchX : I
    //   724: aload_0
    //   725: iload #11
    //   727: aload #8
    //   729: iconst_1
    //   730: iaload
    //   731: isub
    //   732: putfield mLastTouchY : I
    //   735: iload #5
    //   737: ifeq -> 747
    //   740: iload #6
    //   742: istore #12
    //   744: goto -> 750
    //   747: iconst_0
    //   748: istore #12
    //   750: iload_2
    //   751: ifeq -> 761
    //   754: iload #7
    //   756: istore #13
    //   758: goto -> 764
    //   761: iconst_0
    //   762: istore #13
    //   764: aload_0
    //   765: iload #12
    //   767: iload #13
    //   769: aload_1
    //   770: invokevirtual scrollByInternal : (IILandroid/view/MotionEvent;)Z
    //   773: ifeq -> 786
    //   776: aload_0
    //   777: invokevirtual getParent : ()Landroid/view/ViewParent;
    //   780: iconst_1
    //   781: invokeinterface requestDisallowInterceptTouchEvent : (Z)V
    //   786: iload_3
    //   787: istore #9
    //   789: aload_0
    //   790: getfield mGapWorker : Landroidx/recyclerview/widget/GapWorker;
    //   793: ifnull -> 1036
    //   796: iload #6
    //   798: ifne -> 809
    //   801: iload_3
    //   802: istore #9
    //   804: iload #7
    //   806: ifeq -> 1036
    //   809: aload_0
    //   810: getfield mGapWorker : Landroidx/recyclerview/widget/GapWorker;
    //   813: aload_0
    //   814: iload #6
    //   816: iload #7
    //   818: invokevirtual postFromTraversal : (Landroidx/recyclerview/widget/RecyclerView;II)V
    //   821: iload_3
    //   822: istore #9
    //   824: goto -> 1036
    //   827: aload_0
    //   828: getfield mVelocityTracker : Landroid/view/VelocityTracker;
    //   831: aload #4
    //   833: invokevirtual addMovement : (Landroid/view/MotionEvent;)V
    //   836: aload_0
    //   837: getfield mVelocityTracker : Landroid/view/VelocityTracker;
    //   840: sipush #1000
    //   843: aload_0
    //   844: getfield mMaxFlingVelocity : I
    //   847: i2f
    //   848: invokevirtual computeCurrentVelocity : (IF)V
    //   851: iload #5
    //   853: ifeq -> 873
    //   856: aload_0
    //   857: getfield mVelocityTracker : Landroid/view/VelocityTracker;
    //   860: aload_0
    //   861: getfield mScrollPointerId : I
    //   864: invokevirtual getXVelocity : (I)F
    //   867: fneg
    //   868: fstore #16
    //   870: goto -> 876
    //   873: fconst_0
    //   874: fstore #16
    //   876: iload_2
    //   877: ifeq -> 897
    //   880: aload_0
    //   881: getfield mVelocityTracker : Landroid/view/VelocityTracker;
    //   884: aload_0
    //   885: getfield mScrollPointerId : I
    //   888: invokevirtual getYVelocity : (I)F
    //   891: fneg
    //   892: fstore #17
    //   894: goto -> 900
    //   897: fconst_0
    //   898: fstore #17
    //   900: fload #16
    //   902: fconst_0
    //   903: fcmpl
    //   904: ifne -> 914
    //   907: fload #17
    //   909: fconst_0
    //   910: fcmpl
    //   911: ifeq -> 927
    //   914: aload_0
    //   915: fload #16
    //   917: f2i
    //   918: fload #17
    //   920: f2i
    //   921: invokevirtual fling : (II)Z
    //   924: ifne -> 932
    //   927: aload_0
    //   928: iconst_0
    //   929: invokevirtual setScrollState : (I)V
    //   932: aload_0
    //   933: invokespecial resetScroll : ()V
    //   936: iconst_1
    //   937: istore #9
    //   939: goto -> 1036
    //   942: aload_0
    //   943: aload_1
    //   944: iconst_0
    //   945: invokevirtual getPointerId : (I)I
    //   948: putfield mScrollPointerId : I
    //   951: aload_1
    //   952: invokevirtual getX : ()F
    //   955: ldc_w 0.5
    //   958: fadd
    //   959: f2i
    //   960: istore #7
    //   962: aload_0
    //   963: iload #7
    //   965: putfield mLastTouchX : I
    //   968: aload_0
    //   969: iload #7
    //   971: putfield mInitialTouchX : I
    //   974: aload_1
    //   975: invokevirtual getY : ()F
    //   978: ldc_w 0.5
    //   981: fadd
    //   982: f2i
    //   983: istore #7
    //   985: aload_0
    //   986: iload #7
    //   988: putfield mLastTouchY : I
    //   991: aload_0
    //   992: iload #7
    //   994: putfield mInitialTouchY : I
    //   997: iload #5
    //   999: ifeq -> 1008
    //   1002: iconst_1
    //   1003: istore #7
    //   1005: goto -> 1011
    //   1008: iconst_0
    //   1009: istore #7
    //   1011: iload #7
    //   1013: istore #6
    //   1015: iload_2
    //   1016: ifeq -> 1025
    //   1019: iload #7
    //   1021: iconst_2
    //   1022: ior
    //   1023: istore #6
    //   1025: aload_0
    //   1026: iload #6
    //   1028: iconst_0
    //   1029: invokevirtual startNestedScroll : (II)Z
    //   1032: pop
    //   1033: iload_3
    //   1034: istore #9
    //   1036: iload #9
    //   1038: ifne -> 1050
    //   1041: aload_0
    //   1042: getfield mVelocityTracker : Landroid/view/VelocityTracker;
    //   1045: aload #4
    //   1047: invokevirtual addMovement : (Landroid/view/MotionEvent;)V
    //   1050: aload #4
    //   1052: invokevirtual recycle : ()V
    //   1055: iconst_1
    //   1056: ireturn
    //   1057: iconst_0
    //   1058: ireturn
  }
  
  void postAnimationRunner() {
    if (!this.mPostedAnimatorRunner && this.mIsAttached) {
      ViewCompat.postOnAnimation((View)this, this.mItemAnimatorRunner);
      this.mPostedAnimatorRunner = true;
    } 
  }
  
  void processDataSetCompletelyChanged(boolean paramBoolean) {
    this.mDispatchItemsChangedEvent = paramBoolean | this.mDispatchItemsChangedEvent;
    this.mDataSetHasChangedAfterLayout = true;
    markKnownViewsInvalid();
  }
  
  void recordAnimationInfoIfBouncedHiddenView(ViewHolder paramViewHolder, ItemAnimator.ItemHolderInfo paramItemHolderInfo) {
    paramViewHolder.setFlags(0, 8192);
    if (this.mState.mTrackOldChangeHolders && paramViewHolder.isUpdated() && !paramViewHolder.isRemoved() && !paramViewHolder.shouldIgnore()) {
      long l = getChangedHolderKey(paramViewHolder);
      this.mViewInfoStore.addToOldChangeHolders(l, paramViewHolder);
    } 
    this.mViewInfoStore.addToPreLayout(paramViewHolder, paramItemHolderInfo);
  }
  
  void removeAndRecycleViews() {
    ItemAnimator itemAnimator = this.mItemAnimator;
    if (itemAnimator != null)
      itemAnimator.endAnimations(); 
    LayoutManager layoutManager = this.mLayout;
    if (layoutManager != null) {
      layoutManager.removeAndRecycleAllViews(this.mRecycler);
      this.mLayout.removeAndRecycleScrapInt(this.mRecycler);
    } 
    this.mRecycler.clear();
  }
  
  boolean removeAnimatingView(View paramView) {
    startInterceptRequestLayout();
    boolean bool = this.mChildHelper.removeViewIfHidden(paramView);
    if (bool) {
      ViewHolder viewHolder = getChildViewHolderInt(paramView);
      this.mRecycler.unscrapView(viewHolder);
      this.mRecycler.recycleViewHolderInternal(viewHolder);
    } 
    stopInterceptRequestLayout(bool ^ true);
    return bool;
  }
  
  protected void removeDetachedView(View paramView, boolean paramBoolean) {
    StringBuilder stringBuilder;
    ViewHolder viewHolder = getChildViewHolderInt(paramView);
    if (viewHolder != null)
      if (viewHolder.isTmpDetached()) {
        viewHolder.clearTmpDetachFlag();
      } else if (!viewHolder.shouldIgnore()) {
        stringBuilder = new StringBuilder();
        stringBuilder.append("Called removeDetachedView with a view which is not flagged as tmp detached.");
        stringBuilder.append(viewHolder);
        stringBuilder.append(exceptionLabel());
        throw new IllegalArgumentException(stringBuilder.toString());
      }  
    stringBuilder.clearAnimation();
    dispatchChildDetached((View)stringBuilder);
    super.removeDetachedView((View)stringBuilder, paramBoolean);
  }
  
  public void removeItemDecoration(ItemDecoration paramItemDecoration) {
    LayoutManager layoutManager = this.mLayout;
    if (layoutManager != null)
      layoutManager.assertNotInLayoutOrScroll("Cannot remove item decoration during a scroll  or layout"); 
    this.mItemDecorations.remove(paramItemDecoration);
    if (this.mItemDecorations.isEmpty()) {
      boolean bool;
      if (getOverScrollMode() == 2) {
        bool = true;
      } else {
        bool = false;
      } 
      setWillNotDraw(bool);
    } 
    markItemDecorInsetsDirty();
    requestLayout();
  }
  
  public void removeItemDecorationAt(int paramInt) {
    int i = getItemDecorationCount();
    if (paramInt >= 0 && paramInt < i) {
      removeItemDecoration(getItemDecorationAt(paramInt));
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(paramInt);
    stringBuilder.append(" is an invalid index for size ");
    stringBuilder.append(i);
    throw new IndexOutOfBoundsException(stringBuilder.toString());
  }
  
  public void removeOnChildAttachStateChangeListener(OnChildAttachStateChangeListener paramOnChildAttachStateChangeListener) {
    List<OnChildAttachStateChangeListener> list = this.mOnChildAttachStateListeners;
    if (list == null)
      return; 
    list.remove(paramOnChildAttachStateChangeListener);
  }
  
  public void removeOnItemTouchListener(OnItemTouchListener paramOnItemTouchListener) {
    this.mOnItemTouchListeners.remove(paramOnItemTouchListener);
    if (this.mInterceptingOnItemTouchListener == paramOnItemTouchListener)
      this.mInterceptingOnItemTouchListener = null; 
  }
  
  public void removeOnScrollListener(OnScrollListener paramOnScrollListener) {
    List<OnScrollListener> list = this.mScrollListeners;
    if (list != null)
      list.remove(paramOnScrollListener); 
  }
  
  void repositionShadowingViews() {
    int i = this.mChildHelper.getChildCount();
    for (byte b = 0; b < i; b++) {
      View view = this.mChildHelper.getChildAt(b);
      ViewHolder viewHolder = getChildViewHolder(view);
      if (viewHolder != null && viewHolder.mShadowingHolder != null) {
        View view1 = viewHolder.mShadowingHolder.itemView;
        int j = view.getLeft();
        int k = view.getTop();
        if (j != view1.getLeft() || k != view1.getTop())
          view1.layout(j, k, view1.getWidth() + j, view1.getHeight() + k); 
      } 
    } 
  }
  
  public void requestChildFocus(View paramView1, View paramView2) {
    if (!this.mLayout.onRequestChildFocus(this, this.mState, paramView1, paramView2) && paramView2 != null)
      requestChildOnScreen(paramView1, paramView2); 
    super.requestChildFocus(paramView1, paramView2);
  }
  
  public boolean requestChildRectangleOnScreen(View paramView, Rect paramRect, boolean paramBoolean) {
    return this.mLayout.requestChildRectangleOnScreen(this, paramView, paramRect, paramBoolean);
  }
  
  public void requestDisallowInterceptTouchEvent(boolean paramBoolean) {
    int i = this.mOnItemTouchListeners.size();
    for (byte b = 0; b < i; b++)
      ((OnItemTouchListener)this.mOnItemTouchListeners.get(b)).onRequestDisallowInterceptTouchEvent(paramBoolean); 
    super.requestDisallowInterceptTouchEvent(paramBoolean);
  }
  
  public void requestLayout() {
    if (this.mInterceptRequestLayoutDepth == 0 && !this.mLayoutSuppressed) {
      super.requestLayout();
    } else {
      this.mLayoutWasDefered = true;
    } 
  }
  
  void saveOldPositions() {
    int i = this.mChildHelper.getUnfilteredChildCount();
    for (byte b = 0; b < i; b++) {
      ViewHolder viewHolder = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(b));
      if (!viewHolder.shouldIgnore())
        viewHolder.saveOldPosition(); 
    } 
  }
  
  public void scrollBy(int paramInt1, int paramInt2) {
    LayoutManager layoutManager = this.mLayout;
    if (layoutManager == null) {
      Log.e("RecyclerView", "Cannot scroll without a LayoutManager set. Call setLayoutManager with a non-null argument.");
      return;
    } 
    if (this.mLayoutSuppressed)
      return; 
    boolean bool1 = layoutManager.canScrollHorizontally();
    boolean bool2 = this.mLayout.canScrollVertically();
    if (bool1 || bool2) {
      if (!bool1)
        paramInt1 = 0; 
      if (!bool2)
        paramInt2 = 0; 
      scrollByInternal(paramInt1, paramInt2, (MotionEvent)null);
    } 
  }
  
  boolean scrollByInternal(int paramInt1, int paramInt2, MotionEvent paramMotionEvent) {
    byte b1;
    byte b2;
    boolean bool2;
    boolean bool3;
    boolean bool4;
    consumePendingUpdateOperations();
    Adapter adapter = this.mAdapter;
    boolean bool1 = true;
    if (adapter != null) {
      int[] arrayOfInt = this.mReusableIntPair;
      arrayOfInt[0] = 0;
      arrayOfInt[1] = 0;
      scrollStep(paramInt1, paramInt2, arrayOfInt);
      arrayOfInt = this.mReusableIntPair;
      b1 = arrayOfInt[0];
      b2 = arrayOfInt[1];
      bool2 = b2;
      bool3 = b1;
      b1 = paramInt1 - b1;
      b2 = paramInt2 - b2;
    } else {
      bool3 = false;
      bool2 = false;
      b1 = 0;
      b2 = 0;
    } 
    if (!this.mItemDecorations.isEmpty())
      invalidate(); 
    int[] arrayOfInt1 = this.mReusableIntPair;
    arrayOfInt1[0] = 0;
    arrayOfInt1[1] = 0;
    dispatchNestedScroll(bool3, bool2, b1, b2, this.mScrollOffset, 0, arrayOfInt1);
    arrayOfInt1 = this.mReusableIntPair;
    int i = arrayOfInt1[0];
    int j = arrayOfInt1[1];
    if (arrayOfInt1[0] != 0 || arrayOfInt1[1] != 0) {
      bool4 = true;
    } else {
      bool4 = false;
    } 
    int k = this.mLastTouchX;
    arrayOfInt1 = this.mScrollOffset;
    this.mLastTouchX = k - arrayOfInt1[0];
    this.mLastTouchY -= arrayOfInt1[1];
    int[] arrayOfInt2 = this.mNestedOffsets;
    arrayOfInt2[0] = arrayOfInt2[0] + arrayOfInt1[0];
    arrayOfInt2[1] = arrayOfInt2[1] + arrayOfInt1[1];
    if (getOverScrollMode() != 2) {
      if (paramMotionEvent != null && !MotionEventCompat.isFromSource(paramMotionEvent, 8194))
        pullGlows(paramMotionEvent.getX(), (b1 - i), paramMotionEvent.getY(), (b2 - j)); 
      considerReleasingGlowsOnScroll(paramInt1, paramInt2);
    } 
    if (bool3 || bool2)
      dispatchOnScrolled(bool3, bool2); 
    if (!awakenScrollBars())
      invalidate(); 
    boolean bool5 = bool1;
    if (!bool4) {
      bool5 = bool1;
      if (!bool3)
        if (bool2) {
          bool5 = bool1;
        } else {
          bool5 = false;
        }  
    } 
    return bool5;
  }
  
  void scrollStep(int paramInt1, int paramInt2, int[] paramArrayOfint) {
    startInterceptRequestLayout();
    onEnterLayoutOrScroll();
    TraceCompat.beginSection("RV Scroll");
    fillRemainingScrollValues(this.mState);
    if (paramInt1 != 0) {
      paramInt1 = this.mLayout.scrollHorizontallyBy(paramInt1, this.mRecycler, this.mState);
    } else {
      paramInt1 = 0;
    } 
    if (paramInt2 != 0) {
      paramInt2 = this.mLayout.scrollVerticallyBy(paramInt2, this.mRecycler, this.mState);
    } else {
      paramInt2 = 0;
    } 
    TraceCompat.endSection();
    repositionShadowingViews();
    onExitLayoutOrScroll();
    stopInterceptRequestLayout(false);
    if (paramArrayOfint != null) {
      paramArrayOfint[0] = paramInt1;
      paramArrayOfint[1] = paramInt2;
    } 
  }
  
  public void scrollTo(int paramInt1, int paramInt2) {
    Log.w("RecyclerView", "RecyclerView does not support scrolling to an absolute position. Use scrollToPosition instead");
  }
  
  public void scrollToPosition(int paramInt) {
    if (this.mLayoutSuppressed)
      return; 
    stopScroll();
    LayoutManager layoutManager = this.mLayout;
    if (layoutManager == null) {
      Log.e("RecyclerView", "Cannot scroll to position a LayoutManager set. Call setLayoutManager with a non-null argument.");
      return;
    } 
    layoutManager.scrollToPosition(paramInt);
    awakenScrollBars();
  }
  
  public void sendAccessibilityEventUnchecked(AccessibilityEvent paramAccessibilityEvent) {
    if (shouldDeferAccessibilityEvent(paramAccessibilityEvent))
      return; 
    super.sendAccessibilityEventUnchecked(paramAccessibilityEvent);
  }
  
  public void setAccessibilityDelegateCompat(RecyclerViewAccessibilityDelegate paramRecyclerViewAccessibilityDelegate) {
    this.mAccessibilityDelegate = paramRecyclerViewAccessibilityDelegate;
    ViewCompat.setAccessibilityDelegate((View)this, this.mAccessibilityDelegate);
  }
  
  public void setAdapter(Adapter paramAdapter) {
    setLayoutFrozen(false);
    setAdapterInternal(paramAdapter, false, true);
    processDataSetCompletelyChanged(false);
    requestLayout();
  }
  
  public void setChildDrawingOrderCallback(ChildDrawingOrderCallback paramChildDrawingOrderCallback) {
    boolean bool;
    if (paramChildDrawingOrderCallback == this.mChildDrawingOrderCallback)
      return; 
    this.mChildDrawingOrderCallback = paramChildDrawingOrderCallback;
    if (this.mChildDrawingOrderCallback != null) {
      bool = true;
    } else {
      bool = false;
    } 
    setChildrenDrawingOrderEnabled(bool);
  }
  
  boolean setChildImportantForAccessibilityInternal(ViewHolder paramViewHolder, int paramInt) {
    if (isComputingLayout()) {
      paramViewHolder.mPendingAccessibilityState = paramInt;
      this.mPendingAccessibilityImportanceChange.add(paramViewHolder);
      return false;
    } 
    ViewCompat.setImportantForAccessibility(paramViewHolder.itemView, paramInt);
    return true;
  }
  
  public void setClipToPadding(boolean paramBoolean) {
    if (paramBoolean != this.mClipToPadding)
      invalidateGlows(); 
    this.mClipToPadding = paramBoolean;
    super.setClipToPadding(paramBoolean);
    if (this.mFirstLayoutComplete)
      requestLayout(); 
  }
  
  public void setEdgeEffectFactory(EdgeEffectFactory paramEdgeEffectFactory) {
    Preconditions.checkNotNull(paramEdgeEffectFactory);
    this.mEdgeEffectFactory = paramEdgeEffectFactory;
    invalidateGlows();
  }
  
  public void setHasFixedSize(boolean paramBoolean) {
    this.mHasFixedSize = paramBoolean;
  }
  
  public void setItemAnimator(ItemAnimator paramItemAnimator) {
    ItemAnimator itemAnimator = this.mItemAnimator;
    if (itemAnimator != null) {
      itemAnimator.endAnimations();
      this.mItemAnimator.setListener(null);
    } 
    this.mItemAnimator = paramItemAnimator;
    paramItemAnimator = this.mItemAnimator;
    if (paramItemAnimator != null)
      paramItemAnimator.setListener(this.mItemAnimatorListener); 
  }
  
  public void setItemViewCacheSize(int paramInt) {
    this.mRecycler.setViewCacheSize(paramInt);
  }
  
  @Deprecated
  public void setLayoutFrozen(boolean paramBoolean) {
    suppressLayout(paramBoolean);
  }
  
  public void setLayoutManager(LayoutManager paramLayoutManager) {
    if (paramLayoutManager == this.mLayout)
      return; 
    stopScroll();
    if (this.mLayout != null) {
      ItemAnimator itemAnimator = this.mItemAnimator;
      if (itemAnimator != null)
        itemAnimator.endAnimations(); 
      this.mLayout.removeAndRecycleAllViews(this.mRecycler);
      this.mLayout.removeAndRecycleScrapInt(this.mRecycler);
      this.mRecycler.clear();
      if (this.mIsAttached)
        this.mLayout.dispatchDetachedFromWindow(this, this.mRecycler); 
      this.mLayout.setRecyclerView(null);
      this.mLayout = null;
    } else {
      this.mRecycler.clear();
    } 
    this.mChildHelper.removeAllViewsUnfiltered();
    this.mLayout = paramLayoutManager;
    if (paramLayoutManager != null)
      if (paramLayoutManager.mRecyclerView == null) {
        this.mLayout.setRecyclerView(this);
        if (this.mIsAttached)
          this.mLayout.dispatchAttachedToWindow(this); 
      } else {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("LayoutManager ");
        stringBuilder.append(paramLayoutManager);
        stringBuilder.append(" is already attached to a RecyclerView:");
        stringBuilder.append(paramLayoutManager.mRecyclerView.exceptionLabel());
        throw new IllegalArgumentException(stringBuilder.toString());
      }  
    this.mRecycler.updateViewCacheSize();
    requestLayout();
  }
  
  @Deprecated
  public void setLayoutTransition(LayoutTransition paramLayoutTransition) {
    if (Build.VERSION.SDK_INT < 18) {
      if (paramLayoutTransition == null) {
        suppressLayout(false);
        return;
      } 
      if (paramLayoutTransition.getAnimator(0) == null && paramLayoutTransition.getAnimator(1) == null && paramLayoutTransition.getAnimator(2) == null && paramLayoutTransition.getAnimator(3) == null && paramLayoutTransition.getAnimator(4) == null) {
        suppressLayout(true);
        return;
      } 
    } 
    if (paramLayoutTransition == null) {
      super.setLayoutTransition(null);
      return;
    } 
    throw new IllegalArgumentException("Providing a LayoutTransition into RecyclerView is not supported. Please use setItemAnimator() instead for animating changes to the items in this RecyclerView");
  }
  
  public void setNestedScrollingEnabled(boolean paramBoolean) {
    getScrollingChildHelper().setNestedScrollingEnabled(paramBoolean);
  }
  
  public void setOnFlingListener(OnFlingListener paramOnFlingListener) {
    this.mOnFlingListener = paramOnFlingListener;
  }
  
  @Deprecated
  public void setOnScrollListener(OnScrollListener paramOnScrollListener) {
    this.mScrollListener = paramOnScrollListener;
  }
  
  public void setPreserveFocusAfterLayout(boolean paramBoolean) {
    this.mPreserveFocusAfterLayout = paramBoolean;
  }
  
  public void setRecycledViewPool(RecycledViewPool paramRecycledViewPool) {
    this.mRecycler.setRecycledViewPool(paramRecycledViewPool);
  }
  
  public void setRecyclerListener(RecyclerListener paramRecyclerListener) {
    this.mRecyclerListener = paramRecyclerListener;
  }
  
  void setScrollState(int paramInt) {
    if (paramInt == this.mScrollState)
      return; 
    this.mScrollState = paramInt;
    if (paramInt != 2)
      stopScrollersInternal(); 
    dispatchOnScrollStateChanged(paramInt);
  }
  
  public void setScrollingTouchSlop(int paramInt) {
    ViewConfiguration viewConfiguration = ViewConfiguration.get(getContext());
    if (paramInt != 0)
      if (paramInt != 1) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("setScrollingTouchSlop(): bad argument constant ");
        stringBuilder.append(paramInt);
        stringBuilder.append("; using default value");
        Log.w("RecyclerView", stringBuilder.toString());
      } else {
        this.mTouchSlop = viewConfiguration.getScaledPagingTouchSlop();
        return;
      }  
    this.mTouchSlop = viewConfiguration.getScaledTouchSlop();
  }
  
  public void setViewCacheExtension(ViewCacheExtension paramViewCacheExtension) {
    this.mRecycler.setViewCacheExtension(paramViewCacheExtension);
  }
  
  boolean shouldDeferAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent) {
    if (isComputingLayout()) {
      boolean bool1;
      if (paramAccessibilityEvent != null) {
        bool1 = AccessibilityEventCompat.getContentChangeTypes(paramAccessibilityEvent);
      } else {
        bool1 = false;
      } 
      boolean bool2 = bool1;
      if (!bool1)
        bool2 = false; 
      this.mEatenAccessibilityChangeFlags = bool2 | this.mEatenAccessibilityChangeFlags;
      return true;
    } 
    return false;
  }
  
  public void smoothScrollBy(int paramInt1, int paramInt2) {
    smoothScrollBy(paramInt1, paramInt2, (Interpolator)null);
  }
  
  public void smoothScrollBy(int paramInt1, int paramInt2, Interpolator paramInterpolator) {
    smoothScrollBy(paramInt1, paramInt2, paramInterpolator, -2147483648);
  }
  
  public void smoothScrollBy(int paramInt1, int paramInt2, Interpolator paramInterpolator, int paramInt3) {
    smoothScrollBy(paramInt1, paramInt2, paramInterpolator, paramInt3, false);
  }
  
  void smoothScrollBy(int paramInt1, int paramInt2, Interpolator paramInterpolator, int paramInt3, boolean paramBoolean) {
    LayoutManager layoutManager = this.mLayout;
    if (layoutManager == null) {
      Log.e("RecyclerView", "Cannot smooth scroll without a LayoutManager set. Call setLayoutManager with a non-null argument.");
      return;
    } 
    if (this.mLayoutSuppressed)
      return; 
    boolean bool = layoutManager.canScrollHorizontally();
    int i = 0;
    int j = paramInt1;
    if (!bool)
      j = 0; 
    if (!this.mLayout.canScrollVertically())
      paramInt2 = 0; 
    if (j != 0 || paramInt2 != 0) {
      if (paramInt3 == Integer.MIN_VALUE || paramInt3 > 0) {
        paramInt1 = 1;
      } else {
        paramInt1 = 0;
      } 
      if (paramInt1 != 0) {
        if (paramBoolean) {
          paramInt1 = i;
          if (j != 0)
            paramInt1 = 1; 
          i = paramInt1;
          if (paramInt2 != 0)
            i = paramInt1 | 0x2; 
          startNestedScroll(i, 1);
        } 
        this.mViewFlinger.smoothScrollBy(j, paramInt2, paramInt3, paramInterpolator);
      } else {
        scrollBy(j, paramInt2);
      } 
    } 
  }
  
  public void smoothScrollToPosition(int paramInt) {
    if (this.mLayoutSuppressed)
      return; 
    LayoutManager layoutManager = this.mLayout;
    if (layoutManager == null) {
      Log.e("RecyclerView", "Cannot smooth scroll without a LayoutManager set. Call setLayoutManager with a non-null argument.");
      return;
    } 
    layoutManager.smoothScrollToPosition(this, this.mState, paramInt);
  }
  
  void startInterceptRequestLayout() {
    this.mInterceptRequestLayoutDepth++;
    if (this.mInterceptRequestLayoutDepth == 1 && !this.mLayoutSuppressed)
      this.mLayoutWasDefered = false; 
  }
  
  public boolean startNestedScroll(int paramInt) {
    return getScrollingChildHelper().startNestedScroll(paramInt);
  }
  
  public boolean startNestedScroll(int paramInt1, int paramInt2) {
    return getScrollingChildHelper().startNestedScroll(paramInt1, paramInt2);
  }
  
  void stopInterceptRequestLayout(boolean paramBoolean) {
    if (this.mInterceptRequestLayoutDepth < 1)
      this.mInterceptRequestLayoutDepth = 1; 
    if (!paramBoolean && !this.mLayoutSuppressed)
      this.mLayoutWasDefered = false; 
    if (this.mInterceptRequestLayoutDepth == 1) {
      if (paramBoolean && this.mLayoutWasDefered && !this.mLayoutSuppressed && this.mLayout != null && this.mAdapter != null)
        dispatchLayout(); 
      if (!this.mLayoutSuppressed)
        this.mLayoutWasDefered = false; 
    } 
    this.mInterceptRequestLayoutDepth--;
  }
  
  public void stopNestedScroll() {
    getScrollingChildHelper().stopNestedScroll();
  }
  
  public void stopNestedScroll(int paramInt) {
    getScrollingChildHelper().stopNestedScroll(paramInt);
  }
  
  public void stopScroll() {
    setScrollState(0);
    stopScrollersInternal();
  }
  
  public final void suppressLayout(boolean paramBoolean) {
    if (paramBoolean != this.mLayoutSuppressed) {
      assertNotInLayoutOrScroll("Do not suppressLayout in layout or scroll");
      if (!paramBoolean) {
        this.mLayoutSuppressed = false;
        if (this.mLayoutWasDefered && this.mLayout != null && this.mAdapter != null)
          requestLayout(); 
        this.mLayoutWasDefered = false;
      } else {
        long l = SystemClock.uptimeMillis();
        onTouchEvent(MotionEvent.obtain(l, l, 3, 0.0F, 0.0F, 0));
        this.mLayoutSuppressed = true;
        this.mIgnoreMotionEventTillDown = true;
        stopScroll();
      } 
    } 
  }
  
  public void swapAdapter(Adapter paramAdapter, boolean paramBoolean) {
    setLayoutFrozen(false);
    setAdapterInternal(paramAdapter, true, paramBoolean);
    processDataSetCompletelyChanged(true);
    requestLayout();
  }
  
  void viewRangeUpdate(int paramInt1, int paramInt2, Object paramObject) {
    int i = this.mChildHelper.getUnfilteredChildCount();
    for (byte b = 0; b < i; b++) {
      View view = this.mChildHelper.getUnfilteredChildAt(b);
      ViewHolder viewHolder = getChildViewHolderInt(view);
      if (viewHolder != null && !viewHolder.shouldIgnore() && viewHolder.mPosition >= paramInt1 && viewHolder.mPosition < paramInt1 + paramInt2) {
        viewHolder.addFlags(2);
        viewHolder.addChangePayload(paramObject);
        ((LayoutParams)view.getLayoutParams()).mInsetsDirty = true;
      } 
    } 
    this.mRecycler.viewRangeUpdate(paramInt1, paramInt2);
  }
  
  static {
    boolean bool;
  }
  
  static final boolean ALLOW_SIZE_IN_UNSPECIFIED_SPEC;
  
  static final boolean ALLOW_THREAD_GAP_WORK;
  
  static final boolean DEBUG = false;
  
  static final int DEFAULT_ORIENTATION = 1;
  
  static final boolean DISPATCH_TEMP_DETACH = false;
  
  private static final boolean FORCE_ABS_FOCUS_SEARCH_DIRECTION;
  
  static final boolean FORCE_INVALIDATE_DISPLAY_LIST;
  
  static final long FOREVER_NS = 9223372036854775807L;
  
  public static final int HORIZONTAL = 0;
  
  private static final boolean IGNORE_DETACHED_FOCUSED_CHILD;
  
  private static final int INVALID_POINTER = -1;
  
  public static final int INVALID_TYPE = -1;
  
  private static final Class<?>[] LAYOUT_MANAGER_CONSTRUCTOR_SIGNATURE;
  
  static final int MAX_SCROLL_DURATION = 2000;
  
  private static final int[] NESTED_SCROLLING_ATTRS = new int[] { 16843830 };
  
  public static final long NO_ID = -1L;
  
  public static final int NO_POSITION = -1;
  
  static final boolean POST_UPDATES_ON_ANIMATION;
  
  public static final int SCROLL_STATE_DRAGGING = 1;
  
  public static final int SCROLL_STATE_IDLE = 0;
  
  public static final int SCROLL_STATE_SETTLING = 2;
  
  static final String TAG = "RecyclerView";
  
  public static final int TOUCH_SLOP_DEFAULT = 0;
  
  public static final int TOUCH_SLOP_PAGING = 1;
  
  static final String TRACE_BIND_VIEW_TAG = "RV OnBindView";
  
  static final String TRACE_CREATE_VIEW_TAG = "RV CreateView";
  
  private static final String TRACE_HANDLE_ADAPTER_UPDATES_TAG = "RV PartialInvalidate";
  
  static final String TRACE_NESTED_PREFETCH_TAG = "RV Nested Prefetch";
  
  private static final String TRACE_ON_DATA_SET_CHANGE_LAYOUT_TAG = "RV FullInvalidate";
  
  private static final String TRACE_ON_LAYOUT_TAG = "RV OnLayout";
  
  static final String TRACE_PREFETCH_TAG = "RV Prefetch";
  
  static final String TRACE_SCROLL_TAG = "RV Scroll";
  
  public static final int UNDEFINED_DURATION = -2147483648;
  
  static final boolean VERBOSE_TRACING = false;
  
  public static final int VERTICAL = 1;
  
  static final Interpolator sQuinticInterpolator;
  
  RecyclerViewAccessibilityDelegate mAccessibilityDelegate;
  
  private final AccessibilityManager mAccessibilityManager;
  
  Adapter mAdapter;
  
  AdapterHelper mAdapterHelper;
  
  boolean mAdapterUpdateDuringMeasure;
  
  private EdgeEffect mBottomGlow;
  
  private ChildDrawingOrderCallback mChildDrawingOrderCallback;
  
  ChildHelper mChildHelper;
  
  boolean mClipToPadding;
  
  boolean mDataSetHasChangedAfterLayout;
  
  boolean mDispatchItemsChangedEvent;
  
  private int mDispatchScrollCounter;
  
  private int mEatenAccessibilityChangeFlags;
  
  private EdgeEffectFactory mEdgeEffectFactory;
  
  boolean mEnableFastScroller;
  
  boolean mFirstLayoutComplete;
  
  GapWorker mGapWorker;
  
  boolean mHasFixedSize;
  
  private boolean mIgnoreMotionEventTillDown;
  
  private int mInitialTouchX;
  
  private int mInitialTouchY;
  
  private int mInterceptRequestLayoutDepth;
  
  private OnItemTouchListener mInterceptingOnItemTouchListener;
  
  boolean mIsAttached;
  
  ItemAnimator mItemAnimator;
  
  private ItemAnimator.ItemAnimatorListener mItemAnimatorListener;
  
  private Runnable mItemAnimatorRunner;
  
  final ArrayList<ItemDecoration> mItemDecorations;
  
  boolean mItemsAddedOrRemoved;
  
  boolean mItemsChanged;
  
  private int mLastTouchX;
  
  private int mLastTouchY;
  
  LayoutManager mLayout;
  
  private int mLayoutOrScrollCounter;
  
  boolean mLayoutSuppressed;
  
  boolean mLayoutWasDefered;
  
  private EdgeEffect mLeftGlow;
  
  private final int mMaxFlingVelocity;
  
  private final int mMinFlingVelocity;
  
  private final int[] mMinMaxLayoutPositions;
  
  private final int[] mNestedOffsets;
  
  private final RecyclerViewDataObserver mObserver;
  
  private List<OnChildAttachStateChangeListener> mOnChildAttachStateListeners;
  
  private OnFlingListener mOnFlingListener;
  
  private final ArrayList<OnItemTouchListener> mOnItemTouchListeners;
  
  final List<ViewHolder> mPendingAccessibilityImportanceChange;
  
  private SavedState mPendingSavedState;
  
  boolean mPostedAnimatorRunner;
  
  GapWorker.LayoutPrefetchRegistryImpl mPrefetchRegistry;
  
  private boolean mPreserveFocusAfterLayout;
  
  final Recycler mRecycler;
  
  RecyclerListener mRecyclerListener;
  
  final int[] mReusableIntPair;
  
  private EdgeEffect mRightGlow;
  
  private float mScaledHorizontalScrollFactor;
  
  private float mScaledVerticalScrollFactor;
  
  private OnScrollListener mScrollListener;
  
  private List<OnScrollListener> mScrollListeners;
  
  private final int[] mScrollOffset;
  
  private int mScrollPointerId;
  
  private int mScrollState;
  
  private NestedScrollingChildHelper mScrollingChildHelper;
  
  final State mState;
  
  final Rect mTempRect;
  
  private final Rect mTempRect2;
  
  final RectF mTempRectF;
  
  private EdgeEffect mTopGlow;
  
  private int mTouchSlop;
  
  final Runnable mUpdateChildViewsRunnable;
  
  private VelocityTracker mVelocityTracker;
  
  final ViewFlinger mViewFlinger;
  
  private final ViewInfoStore.ProcessCallback mViewInfoProcessCallback;
  
  final ViewInfoStore mViewInfoStore;
  
  public static abstract class Adapter<VH extends ViewHolder> {
    private boolean mHasStableIds = false;
    
    private final RecyclerView.AdapterDataObservable mObservable = new RecyclerView.AdapterDataObservable();
    
    public final void bindViewHolder(VH param1VH, int param1Int) {
      ((RecyclerView.ViewHolder)param1VH).mPosition = param1Int;
      if (hasStableIds())
        ((RecyclerView.ViewHolder)param1VH).mItemId = getItemId(param1Int); 
      param1VH.setFlags(1, 519);
      TraceCompat.beginSection("RV OnBindView");
      onBindViewHolder(param1VH, param1Int, param1VH.getUnmodifiedPayloads());
      param1VH.clearPayload();
      ViewGroup.LayoutParams layoutParams = ((RecyclerView.ViewHolder)param1VH).itemView.getLayoutParams();
      if (layoutParams instanceof RecyclerView.LayoutParams)
        ((RecyclerView.LayoutParams)layoutParams).mInsetsDirty = true; 
      TraceCompat.endSection();
    }
    
    public final VH createViewHolder(ViewGroup param1ViewGroup, int param1Int) {
      try {
        TraceCompat.beginSection("RV CreateView");
        param1ViewGroup = (ViewGroup)onCreateViewHolder(param1ViewGroup, param1Int);
        if (((RecyclerView.ViewHolder)param1ViewGroup).itemView.getParent() == null) {
          ((RecyclerView.ViewHolder)param1ViewGroup).mItemViewType = param1Int;
          return (VH)param1ViewGroup;
        } 
        IllegalStateException illegalStateException = new IllegalStateException();
        this("ViewHolder views must not be attached when created. Ensure that you are not passing 'true' to the attachToRoot parameter of LayoutInflater.inflate(..., boolean attachToRoot)");
        throw illegalStateException;
      } finally {
        TraceCompat.endSection();
      } 
    }
    
    public abstract int getItemCount();
    
    public long getItemId(int param1Int) {
      return -1L;
    }
    
    public int getItemViewType(int param1Int) {
      return 0;
    }
    
    public final boolean hasObservers() {
      return this.mObservable.hasObservers();
    }
    
    public final boolean hasStableIds() {
      return this.mHasStableIds;
    }
    
    public final void notifyDataSetChanged() {
      this.mObservable.notifyChanged();
    }
    
    public final void notifyItemChanged(int param1Int) {
      this.mObservable.notifyItemRangeChanged(param1Int, 1);
    }
    
    public final void notifyItemChanged(int param1Int, Object param1Object) {
      this.mObservable.notifyItemRangeChanged(param1Int, 1, param1Object);
    }
    
    public final void notifyItemInserted(int param1Int) {
      this.mObservable.notifyItemRangeInserted(param1Int, 1);
    }
    
    public final void notifyItemMoved(int param1Int1, int param1Int2) {
      this.mObservable.notifyItemMoved(param1Int1, param1Int2);
    }
    
    public final void notifyItemRangeChanged(int param1Int1, int param1Int2) {
      this.mObservable.notifyItemRangeChanged(param1Int1, param1Int2);
    }
    
    public final void notifyItemRangeChanged(int param1Int1, int param1Int2, Object param1Object) {
      this.mObservable.notifyItemRangeChanged(param1Int1, param1Int2, param1Object);
    }
    
    public final void notifyItemRangeInserted(int param1Int1, int param1Int2) {
      this.mObservable.notifyItemRangeInserted(param1Int1, param1Int2);
    }
    
    public final void notifyItemRangeRemoved(int param1Int1, int param1Int2) {
      this.mObservable.notifyItemRangeRemoved(param1Int1, param1Int2);
    }
    
    public final void notifyItemRemoved(int param1Int) {
      this.mObservable.notifyItemRangeRemoved(param1Int, 1);
    }
    
    public void onAttachedToRecyclerView(RecyclerView param1RecyclerView) {}
    
    public abstract void onBindViewHolder(VH param1VH, int param1Int);
    
    public void onBindViewHolder(VH param1VH, int param1Int, List<Object> param1List) {
      onBindViewHolder(param1VH, param1Int);
    }
    
    public abstract VH onCreateViewHolder(ViewGroup param1ViewGroup, int param1Int);
    
    public void onDetachedFromRecyclerView(RecyclerView param1RecyclerView) {}
    
    public boolean onFailedToRecycleView(VH param1VH) {
      return false;
    }
    
    public void onViewAttachedToWindow(VH param1VH) {}
    
    public void onViewDetachedFromWindow(VH param1VH) {}
    
    public void onViewRecycled(VH param1VH) {}
    
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver param1AdapterDataObserver) {
      this.mObservable.registerObserver(param1AdapterDataObserver);
    }
    
    public void setHasStableIds(boolean param1Boolean) {
      if (!hasObservers()) {
        this.mHasStableIds = param1Boolean;
        return;
      } 
      throw new IllegalStateException("Cannot change whether this adapter has stable IDs while the adapter has registered observers.");
    }
    
    public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver param1AdapterDataObserver) {
      this.mObservable.unregisterObserver(param1AdapterDataObserver);
    }
  }
  
  static class AdapterDataObservable extends Observable<AdapterDataObserver> {
    public boolean hasObservers() {
      return this.mObservers.isEmpty() ^ true;
    }
    
    public void notifyChanged() {
      for (int i = this.mObservers.size() - 1; i >= 0; i--)
        ((RecyclerView.AdapterDataObserver)this.mObservers.get(i)).onChanged(); 
    }
    
    public void notifyItemMoved(int param1Int1, int param1Int2) {
      for (int i = this.mObservers.size() - 1; i >= 0; i--)
        ((RecyclerView.AdapterDataObserver)this.mObservers.get(i)).onItemRangeMoved(param1Int1, param1Int2, 1); 
    }
    
    public void notifyItemRangeChanged(int param1Int1, int param1Int2) {
      notifyItemRangeChanged(param1Int1, param1Int2, (Object)null);
    }
    
    public void notifyItemRangeChanged(int param1Int1, int param1Int2, Object param1Object) {
      for (int i = this.mObservers.size() - 1; i >= 0; i--)
        ((RecyclerView.AdapterDataObserver)this.mObservers.get(i)).onItemRangeChanged(param1Int1, param1Int2, param1Object); 
    }
    
    public void notifyItemRangeInserted(int param1Int1, int param1Int2) {
      for (int i = this.mObservers.size() - 1; i >= 0; i--)
        ((RecyclerView.AdapterDataObserver)this.mObservers.get(i)).onItemRangeInserted(param1Int1, param1Int2); 
    }
    
    public void notifyItemRangeRemoved(int param1Int1, int param1Int2) {
      for (int i = this.mObservers.size() - 1; i >= 0; i--)
        ((RecyclerView.AdapterDataObserver)this.mObservers.get(i)).onItemRangeRemoved(param1Int1, param1Int2); 
    }
  }
  
  public static abstract class AdapterDataObserver {
    public void onChanged() {}
    
    public void onItemRangeChanged(int param1Int1, int param1Int2) {}
    
    public void onItemRangeChanged(int param1Int1, int param1Int2, Object param1Object) {
      onItemRangeChanged(param1Int1, param1Int2);
    }
    
    public void onItemRangeInserted(int param1Int1, int param1Int2) {}
    
    public void onItemRangeMoved(int param1Int1, int param1Int2, int param1Int3) {}
    
    public void onItemRangeRemoved(int param1Int1, int param1Int2) {}
  }
  
  public static interface ChildDrawingOrderCallback {
    int onGetChildDrawingOrder(int param1Int1, int param1Int2);
  }
  
  public static class EdgeEffectFactory {
    public static final int DIRECTION_BOTTOM = 3;
    
    public static final int DIRECTION_LEFT = 0;
    
    public static final int DIRECTION_RIGHT = 2;
    
    public static final int DIRECTION_TOP = 1;
    
    protected EdgeEffect createEdgeEffect(RecyclerView param1RecyclerView, int param1Int) {
      return new EdgeEffect(param1RecyclerView.getContext());
    }
    
    @Retention(RetentionPolicy.SOURCE)
    public static @interface EdgeDirection {}
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface EdgeDirection {}
  
  public static abstract class ItemAnimator {
    public static final int FLAG_APPEARED_IN_PRE_LAYOUT = 4096;
    
    public static final int FLAG_CHANGED = 2;
    
    public static final int FLAG_INVALIDATED = 4;
    
    public static final int FLAG_MOVED = 2048;
    
    public static final int FLAG_REMOVED = 8;
    
    private long mAddDuration = 120L;
    
    private long mChangeDuration = 250L;
    
    private ArrayList<ItemAnimatorFinishedListener> mFinishedListeners = new ArrayList<ItemAnimatorFinishedListener>();
    
    private ItemAnimatorListener mListener = null;
    
    private long mMoveDuration = 250L;
    
    private long mRemoveDuration = 120L;
    
    static int buildAdapterChangeFlagsForAnimations(RecyclerView.ViewHolder param1ViewHolder) {
      int i = param1ViewHolder.mFlags & 0xE;
      if (param1ViewHolder.isInvalid())
        return 4; 
      int j = i;
      if ((i & 0x4) == 0) {
        int k = param1ViewHolder.getOldPosition();
        int m = param1ViewHolder.getAdapterPosition();
        j = i;
        if (k != -1) {
          j = i;
          if (m != -1) {
            j = i;
            if (k != m)
              j = i | 0x800; 
          } 
        } 
      } 
      return j;
    }
    
    public abstract boolean animateAppearance(RecyclerView.ViewHolder param1ViewHolder, ItemHolderInfo param1ItemHolderInfo1, ItemHolderInfo param1ItemHolderInfo2);
    
    public abstract boolean animateChange(RecyclerView.ViewHolder param1ViewHolder1, RecyclerView.ViewHolder param1ViewHolder2, ItemHolderInfo param1ItemHolderInfo1, ItemHolderInfo param1ItemHolderInfo2);
    
    public abstract boolean animateDisappearance(RecyclerView.ViewHolder param1ViewHolder, ItemHolderInfo param1ItemHolderInfo1, ItemHolderInfo param1ItemHolderInfo2);
    
    public abstract boolean animatePersistence(RecyclerView.ViewHolder param1ViewHolder, ItemHolderInfo param1ItemHolderInfo1, ItemHolderInfo param1ItemHolderInfo2);
    
    public boolean canReuseUpdatedViewHolder(RecyclerView.ViewHolder param1ViewHolder) {
      return true;
    }
    
    public boolean canReuseUpdatedViewHolder(RecyclerView.ViewHolder param1ViewHolder, List<Object> param1List) {
      return canReuseUpdatedViewHolder(param1ViewHolder);
    }
    
    public final void dispatchAnimationFinished(RecyclerView.ViewHolder param1ViewHolder) {
      onAnimationFinished(param1ViewHolder);
      ItemAnimatorListener itemAnimatorListener = this.mListener;
      if (itemAnimatorListener != null)
        itemAnimatorListener.onAnimationFinished(param1ViewHolder); 
    }
    
    public final void dispatchAnimationStarted(RecyclerView.ViewHolder param1ViewHolder) {
      onAnimationStarted(param1ViewHolder);
    }
    
    public final void dispatchAnimationsFinished() {
      int i = this.mFinishedListeners.size();
      for (byte b = 0; b < i; b++)
        ((ItemAnimatorFinishedListener)this.mFinishedListeners.get(b)).onAnimationsFinished(); 
      this.mFinishedListeners.clear();
    }
    
    public abstract void endAnimation(RecyclerView.ViewHolder param1ViewHolder);
    
    public abstract void endAnimations();
    
    public long getAddDuration() {
      return this.mAddDuration;
    }
    
    public long getChangeDuration() {
      return this.mChangeDuration;
    }
    
    public long getMoveDuration() {
      return this.mMoveDuration;
    }
    
    public long getRemoveDuration() {
      return this.mRemoveDuration;
    }
    
    public abstract boolean isRunning();
    
    public final boolean isRunning(ItemAnimatorFinishedListener param1ItemAnimatorFinishedListener) {
      boolean bool = isRunning();
      if (param1ItemAnimatorFinishedListener != null)
        if (!bool) {
          param1ItemAnimatorFinishedListener.onAnimationsFinished();
        } else {
          this.mFinishedListeners.add(param1ItemAnimatorFinishedListener);
        }  
      return bool;
    }
    
    public ItemHolderInfo obtainHolderInfo() {
      return new ItemHolderInfo();
    }
    
    public void onAnimationFinished(RecyclerView.ViewHolder param1ViewHolder) {}
    
    public void onAnimationStarted(RecyclerView.ViewHolder param1ViewHolder) {}
    
    public ItemHolderInfo recordPostLayoutInformation(RecyclerView.State param1State, RecyclerView.ViewHolder param1ViewHolder) {
      return obtainHolderInfo().setFrom(param1ViewHolder);
    }
    
    public ItemHolderInfo recordPreLayoutInformation(RecyclerView.State param1State, RecyclerView.ViewHolder param1ViewHolder, int param1Int, List<Object> param1List) {
      return obtainHolderInfo().setFrom(param1ViewHolder);
    }
    
    public abstract void runPendingAnimations();
    
    public void setAddDuration(long param1Long) {
      this.mAddDuration = param1Long;
    }
    
    public void setChangeDuration(long param1Long) {
      this.mChangeDuration = param1Long;
    }
    
    void setListener(ItemAnimatorListener param1ItemAnimatorListener) {
      this.mListener = param1ItemAnimatorListener;
    }
    
    public void setMoveDuration(long param1Long) {
      this.mMoveDuration = param1Long;
    }
    
    public void setRemoveDuration(long param1Long) {
      this.mRemoveDuration = param1Long;
    }
    
    @Retention(RetentionPolicy.SOURCE)
    public static @interface AdapterChanges {}
    
    public static interface ItemAnimatorFinishedListener {
      void onAnimationsFinished();
    }
    
    static interface ItemAnimatorListener {
      void onAnimationFinished(RecyclerView.ViewHolder param2ViewHolder);
    }
    
    public static class ItemHolderInfo {
      public int bottom;
      
      public int changeFlags;
      
      public int left;
      
      public int right;
      
      public int top;
      
      public ItemHolderInfo setFrom(RecyclerView.ViewHolder param2ViewHolder) {
        return setFrom(param2ViewHolder, 0);
      }
      
      public ItemHolderInfo setFrom(RecyclerView.ViewHolder param2ViewHolder, int param2Int) {
        View view = param2ViewHolder.itemView;
        this.left = view.getLeft();
        this.top = view.getTop();
        this.right = view.getRight();
        this.bottom = view.getBottom();
        return this;
      }
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface AdapterChanges {}
  
  public static interface ItemAnimatorFinishedListener {
    void onAnimationsFinished();
  }
  
  static interface ItemAnimatorListener {
    void onAnimationFinished(RecyclerView.ViewHolder param1ViewHolder);
  }
  
  public static class ItemHolderInfo {
    public int bottom;
    
    public int changeFlags;
    
    public int left;
    
    public int right;
    
    public int top;
    
    public ItemHolderInfo setFrom(RecyclerView.ViewHolder param1ViewHolder) {
      return setFrom(param1ViewHolder, 0);
    }
    
    public ItemHolderInfo setFrom(RecyclerView.ViewHolder param1ViewHolder, int param1Int) {
      View view = param1ViewHolder.itemView;
      this.left = view.getLeft();
      this.top = view.getTop();
      this.right = view.getRight();
      this.bottom = view.getBottom();
      return this;
    }
  }
  
  private class ItemAnimatorRestoreListener implements ItemAnimator.ItemAnimatorListener {
    public void onAnimationFinished(RecyclerView.ViewHolder param1ViewHolder) {
      param1ViewHolder.setIsRecyclable(true);
      if (param1ViewHolder.mShadowedHolder != null && param1ViewHolder.mShadowingHolder == null)
        param1ViewHolder.mShadowedHolder = null; 
      param1ViewHolder.mShadowingHolder = null;
      if (!param1ViewHolder.shouldBeKeptAsChild() && !RecyclerView.this.removeAnimatingView(param1ViewHolder.itemView) && param1ViewHolder.isTmpDetached())
        RecyclerView.this.removeDetachedView(param1ViewHolder.itemView, false); 
    }
  }
  
  public static abstract class ItemDecoration {
    @Deprecated
    public void getItemOffsets(Rect param1Rect, int param1Int, RecyclerView param1RecyclerView) {
      param1Rect.set(0, 0, 0, 0);
    }
    
    public void getItemOffsets(Rect param1Rect, View param1View, RecyclerView param1RecyclerView, RecyclerView.State param1State) {
      getItemOffsets(param1Rect, ((RecyclerView.LayoutParams)param1View.getLayoutParams()).getViewLayoutPosition(), param1RecyclerView);
    }
    
    @Deprecated
    public void onDraw(Canvas param1Canvas, RecyclerView param1RecyclerView) {}
    
    public void onDraw(Canvas param1Canvas, RecyclerView param1RecyclerView, RecyclerView.State param1State) {
      onDraw(param1Canvas, param1RecyclerView);
    }
    
    @Deprecated
    public void onDrawOver(Canvas param1Canvas, RecyclerView param1RecyclerView) {}
    
    public void onDrawOver(Canvas param1Canvas, RecyclerView param1RecyclerView, RecyclerView.State param1State) {
      onDrawOver(param1Canvas, param1RecyclerView);
    }
  }
  
  public static abstract class LayoutManager {
    boolean mAutoMeasure = false;
    
    ChildHelper mChildHelper;
    
    private int mHeight;
    
    private int mHeightMode;
    
    ViewBoundsCheck mHorizontalBoundCheck = new ViewBoundsCheck(this.mHorizontalBoundCheckCallback);
    
    private final ViewBoundsCheck.Callback mHorizontalBoundCheckCallback = new ViewBoundsCheck.Callback() {
        public View getChildAt(int param2Int) {
          return RecyclerView.LayoutManager.this.getChildAt(param2Int);
        }
        
        public int getChildEnd(View param2View) {
          RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)param2View.getLayoutParams();
          return RecyclerView.LayoutManager.this.getDecoratedRight(param2View) + layoutParams.rightMargin;
        }
        
        public int getChildStart(View param2View) {
          RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)param2View.getLayoutParams();
          return RecyclerView.LayoutManager.this.getDecoratedLeft(param2View) - layoutParams.leftMargin;
        }
        
        public int getParentEnd() {
          return RecyclerView.LayoutManager.this.getWidth() - RecyclerView.LayoutManager.this.getPaddingRight();
        }
        
        public int getParentStart() {
          return RecyclerView.LayoutManager.this.getPaddingLeft();
        }
      };
    
    boolean mIsAttachedToWindow = false;
    
    private boolean mItemPrefetchEnabled = true;
    
    private boolean mMeasurementCacheEnabled = true;
    
    int mPrefetchMaxCountObserved;
    
    boolean mPrefetchMaxObservedInInitialPrefetch;
    
    RecyclerView mRecyclerView;
    
    boolean mRequestedSimpleAnimations = false;
    
    RecyclerView.SmoothScroller mSmoothScroller;
    
    ViewBoundsCheck mVerticalBoundCheck = new ViewBoundsCheck(this.mVerticalBoundCheckCallback);
    
    private final ViewBoundsCheck.Callback mVerticalBoundCheckCallback = new ViewBoundsCheck.Callback() {
        public View getChildAt(int param2Int) {
          return RecyclerView.LayoutManager.this.getChildAt(param2Int);
        }
        
        public int getChildEnd(View param2View) {
          RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)param2View.getLayoutParams();
          return RecyclerView.LayoutManager.this.getDecoratedBottom(param2View) + layoutParams.bottomMargin;
        }
        
        public int getChildStart(View param2View) {
          RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)param2View.getLayoutParams();
          return RecyclerView.LayoutManager.this.getDecoratedTop(param2View) - layoutParams.topMargin;
        }
        
        public int getParentEnd() {
          return RecyclerView.LayoutManager.this.getHeight() - RecyclerView.LayoutManager.this.getPaddingBottom();
        }
        
        public int getParentStart() {
          return RecyclerView.LayoutManager.this.getPaddingTop();
        }
      };
    
    private int mWidth;
    
    private int mWidthMode;
    
    private void addViewInt(View param1View, int param1Int, boolean param1Boolean) {
      RecyclerView.ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(param1View);
      if (param1Boolean || viewHolder.isRemoved()) {
        this.mRecyclerView.mViewInfoStore.addToDisappearedInLayout(viewHolder);
      } else {
        this.mRecyclerView.mViewInfoStore.removeFromDisappearedInLayout(viewHolder);
      } 
      RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)param1View.getLayoutParams();
      if (viewHolder.wasReturnedFromScrap() || viewHolder.isScrap()) {
        if (viewHolder.isScrap()) {
          viewHolder.unScrap();
        } else {
          viewHolder.clearReturnedFromScrapFlag();
        } 
        this.mChildHelper.attachViewToParent(param1View, param1Int, param1View.getLayoutParams(), false);
      } else if (param1View.getParent() == this.mRecyclerView) {
        int i = this.mChildHelper.indexOfChild(param1View);
        int j = param1Int;
        if (param1Int == -1)
          j = this.mChildHelper.getChildCount(); 
        if (i != -1) {
          if (i != j)
            this.mRecyclerView.mLayout.moveView(i, j); 
        } else {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("Added View has RecyclerView as parent but view is not a real child. Unfiltered index:");
          stringBuilder.append(this.mRecyclerView.indexOfChild(param1View));
          stringBuilder.append(this.mRecyclerView.exceptionLabel());
          throw new IllegalStateException(stringBuilder.toString());
        } 
      } else {
        this.mChildHelper.addView(param1View, param1Int, false);
        layoutParams.mInsetsDirty = true;
        RecyclerView.SmoothScroller smoothScroller = this.mSmoothScroller;
        if (smoothScroller != null && smoothScroller.isRunning())
          this.mSmoothScroller.onChildAttachedToWindow(param1View); 
      } 
      if (layoutParams.mPendingInvalidate) {
        viewHolder.itemView.invalidate();
        layoutParams.mPendingInvalidate = false;
      } 
    }
    
    public static int chooseSize(int param1Int1, int param1Int2, int param1Int3) {
      int i = View.MeasureSpec.getMode(param1Int1);
      param1Int1 = View.MeasureSpec.getSize(param1Int1);
      if (i != Integer.MIN_VALUE) {
        if (i != 1073741824)
          param1Int1 = Math.max(param1Int2, param1Int3); 
        return param1Int1;
      } 
      return Math.min(param1Int1, Math.max(param1Int2, param1Int3));
    }
    
    private void detachViewInternal(int param1Int, View param1View) {
      this.mChildHelper.detachViewFromParent(param1Int);
    }
    
    public static int getChildMeasureSpec(int param1Int1, int param1Int2, int param1Int3, int param1Int4, boolean param1Boolean) {
      // Byte code:
      //   0: iconst_0
      //   1: istore #5
      //   3: iconst_0
      //   4: iload_0
      //   5: iload_2
      //   6: isub
      //   7: invokestatic max : (II)I
      //   10: istore #6
      //   12: iload #4
      //   14: ifeq -> 62
      //   17: iload_3
      //   18: iflt -> 24
      //   21: goto -> 66
      //   24: iload_3
      //   25: iconst_m1
      //   26: if_icmpne -> 120
      //   29: iload_1
      //   30: ldc -2147483648
      //   32: if_icmpeq -> 52
      //   35: iload_1
      //   36: ifeq -> 45
      //   39: iload_1
      //   40: ldc 1073741824
      //   42: if_icmpeq -> 52
      //   45: iconst_0
      //   46: istore_1
      //   47: iconst_0
      //   48: istore_0
      //   49: goto -> 55
      //   52: iload #6
      //   54: istore_0
      //   55: iload_0
      //   56: istore_2
      //   57: iload_1
      //   58: istore_0
      //   59: goto -> 125
      //   62: iload_3
      //   63: iflt -> 74
      //   66: iload_3
      //   67: istore_2
      //   68: ldc 1073741824
      //   70: istore_0
      //   71: goto -> 125
      //   74: iload_3
      //   75: iconst_m1
      //   76: if_icmpne -> 87
      //   79: iload_1
      //   80: istore_0
      //   81: iload #6
      //   83: istore_2
      //   84: goto -> 125
      //   87: iload_3
      //   88: bipush #-2
      //   90: if_icmpne -> 120
      //   93: iload_1
      //   94: ldc -2147483648
      //   96: if_icmpeq -> 111
      //   99: iload #6
      //   101: istore_2
      //   102: iload #5
      //   104: istore_0
      //   105: iload_1
      //   106: ldc 1073741824
      //   108: if_icmpne -> 125
      //   111: ldc -2147483648
      //   113: istore_0
      //   114: iload #6
      //   116: istore_2
      //   117: goto -> 125
      //   120: iconst_0
      //   121: istore_2
      //   122: iload #5
      //   124: istore_0
      //   125: iload_2
      //   126: iload_0
      //   127: invokestatic makeMeasureSpec : (II)I
      //   130: ireturn
    }
    
    @Deprecated
    public static int getChildMeasureSpec(int param1Int1, int param1Int2, int param1Int3, boolean param1Boolean) {
      // Byte code:
      //   0: iconst_0
      //   1: istore #4
      //   3: iconst_0
      //   4: iload_0
      //   5: iload_1
      //   6: isub
      //   7: invokestatic max : (II)I
      //   10: istore_0
      //   11: iload_3
      //   12: ifeq -> 30
      //   15: iload_2
      //   16: iflt -> 22
      //   19: goto -> 34
      //   22: iconst_0
      //   23: istore_0
      //   24: iload #4
      //   26: istore_1
      //   27: goto -> 59
      //   30: iload_2
      //   31: iflt -> 42
      //   34: iload_2
      //   35: istore_0
      //   36: ldc 1073741824
      //   38: istore_1
      //   39: goto -> 59
      //   42: iload_2
      //   43: iconst_m1
      //   44: if_icmpne -> 50
      //   47: goto -> 36
      //   50: iload_2
      //   51: bipush #-2
      //   53: if_icmpne -> 22
      //   56: ldc -2147483648
      //   58: istore_1
      //   59: iload_0
      //   60: iload_1
      //   61: invokestatic makeMeasureSpec : (II)I
      //   64: ireturn
    }
    
    private int[] getChildRectangleOnScreenScrollAmount(View param1View, Rect param1Rect) {
      int i = getPaddingLeft();
      int j = getPaddingTop();
      int k = getWidth();
      int m = getPaddingRight();
      int n = getHeight();
      int i1 = getPaddingBottom();
      int i2 = param1View.getLeft() + param1Rect.left - param1View.getScrollX();
      int i3 = param1View.getTop() + param1Rect.top - param1View.getScrollY();
      int i4 = param1Rect.width();
      int i5 = param1Rect.height();
      int i6 = i2 - i;
      i = Math.min(0, i6);
      int i7 = i3 - j;
      j = Math.min(0, i7);
      i2 = i4 + i2 - k - m;
      i4 = Math.max(0, i2);
      i1 = Math.max(0, i5 + i3 - n - i1);
      if (getLayoutDirection() == 1) {
        if (i4 != 0) {
          i = i4;
        } else {
          i = Math.max(i, i2);
        } 
      } else if (i == 0) {
        i = Math.min(i6, i4);
      } 
      if (j == 0)
        j = Math.min(i7, i1); 
      return new int[] { i, j };
    }
    
    public static Properties getProperties(Context param1Context, AttributeSet param1AttributeSet, int param1Int1, int param1Int2) {
      Properties properties = new Properties();
      TypedArray typedArray = param1Context.obtainStyledAttributes(param1AttributeSet, R.styleable.RecyclerView, param1Int1, param1Int2);
      properties.orientation = typedArray.getInt(R.styleable.RecyclerView_android_orientation, 1);
      properties.spanCount = typedArray.getInt(R.styleable.RecyclerView_spanCount, 1);
      properties.reverseLayout = typedArray.getBoolean(R.styleable.RecyclerView_reverseLayout, false);
      properties.stackFromEnd = typedArray.getBoolean(R.styleable.RecyclerView_stackFromEnd, false);
      typedArray.recycle();
      return properties;
    }
    
    private boolean isFocusedChildVisibleAfterScrolling(RecyclerView param1RecyclerView, int param1Int1, int param1Int2) {
      View view = param1RecyclerView.getFocusedChild();
      if (view == null)
        return false; 
      int i = getPaddingLeft();
      int j = getPaddingTop();
      int k = getWidth();
      int m = getPaddingRight();
      int n = getHeight();
      int i1 = getPaddingBottom();
      Rect rect = this.mRecyclerView.mTempRect;
      getDecoratedBoundsWithMargins(view, rect);
      return !(rect.left - param1Int1 >= k - m || rect.right - param1Int1 <= i || rect.top - param1Int2 >= n - i1 || rect.bottom - param1Int2 <= j);
    }
    
    private static boolean isMeasurementUpToDate(int param1Int1, int param1Int2, int param1Int3) {
      int i = View.MeasureSpec.getMode(param1Int2);
      param1Int2 = View.MeasureSpec.getSize(param1Int2);
      boolean bool1 = false;
      boolean bool2 = false;
      if (param1Int3 > 0 && param1Int1 != param1Int3)
        return false; 
      if (i != Integer.MIN_VALUE) {
        if (i != 0) {
          if (i != 1073741824)
            return false; 
          if (param1Int2 == param1Int1)
            bool2 = true; 
          return bool2;
        } 
        return true;
      } 
      bool2 = bool1;
      if (param1Int2 >= param1Int1)
        bool2 = true; 
      return bool2;
    }
    
    private void scrapOrRecycleView(RecyclerView.Recycler param1Recycler, int param1Int, View param1View) {
      RecyclerView.ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(param1View);
      if (viewHolder.shouldIgnore())
        return; 
      if (viewHolder.isInvalid() && !viewHolder.isRemoved() && !this.mRecyclerView.mAdapter.hasStableIds()) {
        removeViewAt(param1Int);
        param1Recycler.recycleViewHolderInternal(viewHolder);
      } else {
        detachViewAt(param1Int);
        param1Recycler.scrapView(param1View);
        this.mRecyclerView.mViewInfoStore.onViewDetached(viewHolder);
      } 
    }
    
    public void addDisappearingView(View param1View) {
      addDisappearingView(param1View, -1);
    }
    
    public void addDisappearingView(View param1View, int param1Int) {
      addViewInt(param1View, param1Int, true);
    }
    
    public void addView(View param1View) {
      addView(param1View, -1);
    }
    
    public void addView(View param1View, int param1Int) {
      addViewInt(param1View, param1Int, false);
    }
    
    public void assertInLayoutOrScroll(String param1String) {
      RecyclerView recyclerView = this.mRecyclerView;
      if (recyclerView != null)
        recyclerView.assertInLayoutOrScroll(param1String); 
    }
    
    public void assertNotInLayoutOrScroll(String param1String) {
      RecyclerView recyclerView = this.mRecyclerView;
      if (recyclerView != null)
        recyclerView.assertNotInLayoutOrScroll(param1String); 
    }
    
    public void attachView(View param1View) {
      attachView(param1View, -1);
    }
    
    public void attachView(View param1View, int param1Int) {
      attachView(param1View, param1Int, (RecyclerView.LayoutParams)param1View.getLayoutParams());
    }
    
    public void attachView(View param1View, int param1Int, RecyclerView.LayoutParams param1LayoutParams) {
      RecyclerView.ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(param1View);
      if (viewHolder.isRemoved()) {
        this.mRecyclerView.mViewInfoStore.addToDisappearedInLayout(viewHolder);
      } else {
        this.mRecyclerView.mViewInfoStore.removeFromDisappearedInLayout(viewHolder);
      } 
      this.mChildHelper.attachViewToParent(param1View, param1Int, (ViewGroup.LayoutParams)param1LayoutParams, viewHolder.isRemoved());
    }
    
    public void calculateItemDecorationsForChild(View param1View, Rect param1Rect) {
      RecyclerView recyclerView = this.mRecyclerView;
      if (recyclerView == null) {
        param1Rect.set(0, 0, 0, 0);
        return;
      } 
      param1Rect.set(recyclerView.getItemDecorInsetsForChild(param1View));
    }
    
    public boolean canScrollHorizontally() {
      return false;
    }
    
    public boolean canScrollVertically() {
      return false;
    }
    
    public boolean checkLayoutParams(RecyclerView.LayoutParams param1LayoutParams) {
      boolean bool;
      if (param1LayoutParams != null) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public void collectAdjacentPrefetchPositions(int param1Int1, int param1Int2, RecyclerView.State param1State, LayoutPrefetchRegistry param1LayoutPrefetchRegistry) {}
    
    public void collectInitialPrefetchPositions(int param1Int, LayoutPrefetchRegistry param1LayoutPrefetchRegistry) {}
    
    public int computeHorizontalScrollExtent(RecyclerView.State param1State) {
      return 0;
    }
    
    public int computeHorizontalScrollOffset(RecyclerView.State param1State) {
      return 0;
    }
    
    public int computeHorizontalScrollRange(RecyclerView.State param1State) {
      return 0;
    }
    
    public int computeVerticalScrollExtent(RecyclerView.State param1State) {
      return 0;
    }
    
    public int computeVerticalScrollOffset(RecyclerView.State param1State) {
      return 0;
    }
    
    public int computeVerticalScrollRange(RecyclerView.State param1State) {
      return 0;
    }
    
    public void detachAndScrapAttachedViews(RecyclerView.Recycler param1Recycler) {
      for (int i = getChildCount() - 1; i >= 0; i--)
        scrapOrRecycleView(param1Recycler, i, getChildAt(i)); 
    }
    
    public void detachAndScrapView(View param1View, RecyclerView.Recycler param1Recycler) {
      scrapOrRecycleView(param1Recycler, this.mChildHelper.indexOfChild(param1View), param1View);
    }
    
    public void detachAndScrapViewAt(int param1Int, RecyclerView.Recycler param1Recycler) {
      scrapOrRecycleView(param1Recycler, param1Int, getChildAt(param1Int));
    }
    
    public void detachView(View param1View) {
      int i = this.mChildHelper.indexOfChild(param1View);
      if (i >= 0)
        detachViewInternal(i, param1View); 
    }
    
    public void detachViewAt(int param1Int) {
      detachViewInternal(param1Int, getChildAt(param1Int));
    }
    
    void dispatchAttachedToWindow(RecyclerView param1RecyclerView) {
      this.mIsAttachedToWindow = true;
      onAttachedToWindow(param1RecyclerView);
    }
    
    void dispatchDetachedFromWindow(RecyclerView param1RecyclerView, RecyclerView.Recycler param1Recycler) {
      this.mIsAttachedToWindow = false;
      onDetachedFromWindow(param1RecyclerView, param1Recycler);
    }
    
    public void endAnimation(View param1View) {
      if (this.mRecyclerView.mItemAnimator != null)
        this.mRecyclerView.mItemAnimator.endAnimation(RecyclerView.getChildViewHolderInt(param1View)); 
    }
    
    public View findContainingItemView(View param1View) {
      RecyclerView recyclerView = this.mRecyclerView;
      if (recyclerView == null)
        return null; 
      param1View = recyclerView.findContainingItemView(param1View);
      return (param1View == null) ? null : (this.mChildHelper.isHidden(param1View) ? null : param1View);
    }
    
    public View findViewByPosition(int param1Int) {
      int i = getChildCount();
      for (byte b = 0; b < i; b++) {
        View view = getChildAt(b);
        RecyclerView.ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(view);
        if (viewHolder != null && viewHolder.getLayoutPosition() == param1Int && !viewHolder.shouldIgnore() && (this.mRecyclerView.mState.isPreLayout() || !viewHolder.isRemoved()))
          return view; 
      } 
      return null;
    }
    
    public abstract RecyclerView.LayoutParams generateDefaultLayoutParams();
    
    public RecyclerView.LayoutParams generateLayoutParams(Context param1Context, AttributeSet param1AttributeSet) {
      return new RecyclerView.LayoutParams(param1Context, param1AttributeSet);
    }
    
    public RecyclerView.LayoutParams generateLayoutParams(ViewGroup.LayoutParams param1LayoutParams) {
      return (param1LayoutParams instanceof RecyclerView.LayoutParams) ? new RecyclerView.LayoutParams((RecyclerView.LayoutParams)param1LayoutParams) : ((param1LayoutParams instanceof ViewGroup.MarginLayoutParams) ? new RecyclerView.LayoutParams((ViewGroup.MarginLayoutParams)param1LayoutParams) : new RecyclerView.LayoutParams(param1LayoutParams));
    }
    
    public int getBaseline() {
      return -1;
    }
    
    public int getBottomDecorationHeight(View param1View) {
      return ((RecyclerView.LayoutParams)param1View.getLayoutParams()).mDecorInsets.bottom;
    }
    
    public View getChildAt(int param1Int) {
      ChildHelper childHelper = this.mChildHelper;
      if (childHelper != null) {
        View view = childHelper.getChildAt(param1Int);
      } else {
        childHelper = null;
      } 
      return (View)childHelper;
    }
    
    public int getChildCount() {
      boolean bool;
      ChildHelper childHelper = this.mChildHelper;
      if (childHelper != null) {
        bool = childHelper.getChildCount();
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public boolean getClipToPadding() {
      boolean bool;
      RecyclerView recyclerView = this.mRecyclerView;
      if (recyclerView != null && recyclerView.mClipToPadding) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public int getColumnCountForAccessibility(RecyclerView.Recycler param1Recycler, RecyclerView.State param1State) {
      RecyclerView recyclerView = this.mRecyclerView;
      byte b = 1;
      int i = b;
      if (recyclerView != null)
        if (recyclerView.mAdapter == null) {
          i = b;
        } else {
          i = b;
          if (canScrollHorizontally())
            i = this.mRecyclerView.mAdapter.getItemCount(); 
        }  
      return i;
    }
    
    public int getDecoratedBottom(View param1View) {
      return param1View.getBottom() + getBottomDecorationHeight(param1View);
    }
    
    public void getDecoratedBoundsWithMargins(View param1View, Rect param1Rect) {
      RecyclerView.getDecoratedBoundsWithMarginsInt(param1View, param1Rect);
    }
    
    public int getDecoratedLeft(View param1View) {
      return param1View.getLeft() - getLeftDecorationWidth(param1View);
    }
    
    public int getDecoratedMeasuredHeight(View param1View) {
      Rect rect = ((RecyclerView.LayoutParams)param1View.getLayoutParams()).mDecorInsets;
      return param1View.getMeasuredHeight() + rect.top + rect.bottom;
    }
    
    public int getDecoratedMeasuredWidth(View param1View) {
      Rect rect = ((RecyclerView.LayoutParams)param1View.getLayoutParams()).mDecorInsets;
      return param1View.getMeasuredWidth() + rect.left + rect.right;
    }
    
    public int getDecoratedRight(View param1View) {
      return param1View.getRight() + getRightDecorationWidth(param1View);
    }
    
    public int getDecoratedTop(View param1View) {
      return param1View.getTop() - getTopDecorationHeight(param1View);
    }
    
    public View getFocusedChild() {
      RecyclerView recyclerView = this.mRecyclerView;
      if (recyclerView == null)
        return null; 
      View view = recyclerView.getFocusedChild();
      return (view == null || this.mChildHelper.isHidden(view)) ? null : view;
    }
    
    public int getHeight() {
      return this.mHeight;
    }
    
    public int getHeightMode() {
      return this.mHeightMode;
    }
    
    public int getItemCount() {
      boolean bool;
      RecyclerView recyclerView = this.mRecyclerView;
      if (recyclerView != null) {
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
      } else {
        recyclerView = null;
      } 
      if (recyclerView != null) {
        bool = recyclerView.getItemCount();
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public int getItemViewType(View param1View) {
      return RecyclerView.getChildViewHolderInt(param1View).getItemViewType();
    }
    
    public int getLayoutDirection() {
      return ViewCompat.getLayoutDirection((View)this.mRecyclerView);
    }
    
    public int getLeftDecorationWidth(View param1View) {
      return ((RecyclerView.LayoutParams)param1View.getLayoutParams()).mDecorInsets.left;
    }
    
    public int getMinimumHeight() {
      return ViewCompat.getMinimumHeight((View)this.mRecyclerView);
    }
    
    public int getMinimumWidth() {
      return ViewCompat.getMinimumWidth((View)this.mRecyclerView);
    }
    
    public int getPaddingBottom() {
      boolean bool;
      RecyclerView recyclerView = this.mRecyclerView;
      if (recyclerView != null) {
        bool = recyclerView.getPaddingBottom();
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public int getPaddingEnd() {
      boolean bool;
      RecyclerView recyclerView = this.mRecyclerView;
      if (recyclerView != null) {
        bool = ViewCompat.getPaddingEnd((View)recyclerView);
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public int getPaddingLeft() {
      boolean bool;
      RecyclerView recyclerView = this.mRecyclerView;
      if (recyclerView != null) {
        bool = recyclerView.getPaddingLeft();
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public int getPaddingRight() {
      boolean bool;
      RecyclerView recyclerView = this.mRecyclerView;
      if (recyclerView != null) {
        bool = recyclerView.getPaddingRight();
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public int getPaddingStart() {
      boolean bool;
      RecyclerView recyclerView = this.mRecyclerView;
      if (recyclerView != null) {
        bool = ViewCompat.getPaddingStart((View)recyclerView);
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public int getPaddingTop() {
      boolean bool;
      RecyclerView recyclerView = this.mRecyclerView;
      if (recyclerView != null) {
        bool = recyclerView.getPaddingTop();
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public int getPosition(View param1View) {
      return ((RecyclerView.LayoutParams)param1View.getLayoutParams()).getViewLayoutPosition();
    }
    
    public int getRightDecorationWidth(View param1View) {
      return ((RecyclerView.LayoutParams)param1View.getLayoutParams()).mDecorInsets.right;
    }
    
    public int getRowCountForAccessibility(RecyclerView.Recycler param1Recycler, RecyclerView.State param1State) {
      RecyclerView recyclerView = this.mRecyclerView;
      byte b = 1;
      int i = b;
      if (recyclerView != null)
        if (recyclerView.mAdapter == null) {
          i = b;
        } else {
          i = b;
          if (canScrollVertically())
            i = this.mRecyclerView.mAdapter.getItemCount(); 
        }  
      return i;
    }
    
    public int getSelectionModeForAccessibility(RecyclerView.Recycler param1Recycler, RecyclerView.State param1State) {
      return 0;
    }
    
    public int getTopDecorationHeight(View param1View) {
      return ((RecyclerView.LayoutParams)param1View.getLayoutParams()).mDecorInsets.top;
    }
    
    public void getTransformedBoundingBox(View param1View, boolean param1Boolean, Rect param1Rect) {
      if (param1Boolean) {
        Rect rect = ((RecyclerView.LayoutParams)param1View.getLayoutParams()).mDecorInsets;
        param1Rect.set(-rect.left, -rect.top, param1View.getWidth() + rect.right, param1View.getHeight() + rect.bottom);
      } else {
        param1Rect.set(0, 0, param1View.getWidth(), param1View.getHeight());
      } 
      if (this.mRecyclerView != null) {
        Matrix matrix = param1View.getMatrix();
        if (matrix != null && !matrix.isIdentity()) {
          RectF rectF = this.mRecyclerView.mTempRectF;
          rectF.set(param1Rect);
          matrix.mapRect(rectF);
          param1Rect.set((int)Math.floor(rectF.left), (int)Math.floor(rectF.top), (int)Math.ceil(rectF.right), (int)Math.ceil(rectF.bottom));
        } 
      } 
      param1Rect.offset(param1View.getLeft(), param1View.getTop());
    }
    
    public int getWidth() {
      return this.mWidth;
    }
    
    public int getWidthMode() {
      return this.mWidthMode;
    }
    
    boolean hasFlexibleChildInBothOrientations() {
      int i = getChildCount();
      for (byte b = 0; b < i; b++) {
        ViewGroup.LayoutParams layoutParams = getChildAt(b).getLayoutParams();
        if (layoutParams.width < 0 && layoutParams.height < 0)
          return true; 
      } 
      return false;
    }
    
    public boolean hasFocus() {
      boolean bool;
      RecyclerView recyclerView = this.mRecyclerView;
      if (recyclerView != null && recyclerView.hasFocus()) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public void ignoreView(View param1View) {
      ViewParent viewParent = param1View.getParent();
      RecyclerView recyclerView = this.mRecyclerView;
      if (viewParent == recyclerView && recyclerView.indexOfChild(param1View) != -1) {
        RecyclerView.ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(param1View);
        viewHolder.addFlags(128);
        this.mRecyclerView.mViewInfoStore.removeViewHolder(viewHolder);
        return;
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("View should be fully attached to be ignored");
      stringBuilder.append(this.mRecyclerView.exceptionLabel());
      throw new IllegalArgumentException(stringBuilder.toString());
    }
    
    public boolean isAttachedToWindow() {
      return this.mIsAttachedToWindow;
    }
    
    public boolean isAutoMeasureEnabled() {
      return this.mAutoMeasure;
    }
    
    public boolean isFocused() {
      boolean bool;
      RecyclerView recyclerView = this.mRecyclerView;
      if (recyclerView != null && recyclerView.isFocused()) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public final boolean isItemPrefetchEnabled() {
      return this.mItemPrefetchEnabled;
    }
    
    public boolean isLayoutHierarchical(RecyclerView.Recycler param1Recycler, RecyclerView.State param1State) {
      return false;
    }
    
    public boolean isMeasurementCacheEnabled() {
      return this.mMeasurementCacheEnabled;
    }
    
    public boolean isSmoothScrolling() {
      boolean bool;
      RecyclerView.SmoothScroller smoothScroller = this.mSmoothScroller;
      if (smoothScroller != null && smoothScroller.isRunning()) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public boolean isViewPartiallyVisible(View param1View, boolean param1Boolean1, boolean param1Boolean2) {
      if (this.mHorizontalBoundCheck.isViewWithinBoundFlags(param1View, 24579) && this.mVerticalBoundCheck.isViewWithinBoundFlags(param1View, 24579)) {
        param1Boolean2 = true;
      } else {
        param1Boolean2 = false;
      } 
      return param1Boolean1 ? param1Boolean2 : (param1Boolean2 ^ true);
    }
    
    public void layoutDecorated(View param1View, int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
      Rect rect = ((RecyclerView.LayoutParams)param1View.getLayoutParams()).mDecorInsets;
      param1View.layout(param1Int1 + rect.left, param1Int2 + rect.top, param1Int3 - rect.right, param1Int4 - rect.bottom);
    }
    
    public void layoutDecoratedWithMargins(View param1View, int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
      RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)param1View.getLayoutParams();
      Rect rect = layoutParams.mDecorInsets;
      param1View.layout(param1Int1 + rect.left + layoutParams.leftMargin, param1Int2 + rect.top + layoutParams.topMargin, param1Int3 - rect.right - layoutParams.rightMargin, param1Int4 - rect.bottom - layoutParams.bottomMargin);
    }
    
    public void measureChild(View param1View, int param1Int1, int param1Int2) {
      RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)param1View.getLayoutParams();
      Rect rect = this.mRecyclerView.getItemDecorInsetsForChild(param1View);
      int i = rect.left;
      int j = rect.right;
      int k = rect.top;
      int m = rect.bottom;
      param1Int1 = getChildMeasureSpec(getWidth(), getWidthMode(), getPaddingLeft() + getPaddingRight() + param1Int1 + i + j, layoutParams.width, canScrollHorizontally());
      param1Int2 = getChildMeasureSpec(getHeight(), getHeightMode(), getPaddingTop() + getPaddingBottom() + param1Int2 + k + m, layoutParams.height, canScrollVertically());
      if (shouldMeasureChild(param1View, param1Int1, param1Int2, layoutParams))
        param1View.measure(param1Int1, param1Int2); 
    }
    
    public void measureChildWithMargins(View param1View, int param1Int1, int param1Int2) {
      RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)param1View.getLayoutParams();
      Rect rect = this.mRecyclerView.getItemDecorInsetsForChild(param1View);
      int i = rect.left;
      int j = rect.right;
      int k = rect.top;
      int m = rect.bottom;
      param1Int1 = getChildMeasureSpec(getWidth(), getWidthMode(), getPaddingLeft() + getPaddingRight() + layoutParams.leftMargin + layoutParams.rightMargin + param1Int1 + i + j, layoutParams.width, canScrollHorizontally());
      param1Int2 = getChildMeasureSpec(getHeight(), getHeightMode(), getPaddingTop() + getPaddingBottom() + layoutParams.topMargin + layoutParams.bottomMargin + param1Int2 + k + m, layoutParams.height, canScrollVertically());
      if (shouldMeasureChild(param1View, param1Int1, param1Int2, layoutParams))
        param1View.measure(param1Int1, param1Int2); 
    }
    
    public void moveView(int param1Int1, int param1Int2) {
      View view = getChildAt(param1Int1);
      if (view != null) {
        detachViewAt(param1Int1);
        attachView(view, param1Int2);
        return;
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Cannot move a child from non-existing index:");
      stringBuilder.append(param1Int1);
      stringBuilder.append(this.mRecyclerView.toString());
      throw new IllegalArgumentException(stringBuilder.toString());
    }
    
    public void offsetChildrenHorizontal(int param1Int) {
      RecyclerView recyclerView = this.mRecyclerView;
      if (recyclerView != null)
        recyclerView.offsetChildrenHorizontal(param1Int); 
    }
    
    public void offsetChildrenVertical(int param1Int) {
      RecyclerView recyclerView = this.mRecyclerView;
      if (recyclerView != null)
        recyclerView.offsetChildrenVertical(param1Int); 
    }
    
    public void onAdapterChanged(RecyclerView.Adapter param1Adapter1, RecyclerView.Adapter param1Adapter2) {}
    
    public boolean onAddFocusables(RecyclerView param1RecyclerView, ArrayList<View> param1ArrayList, int param1Int1, int param1Int2) {
      return false;
    }
    
    public void onAttachedToWindow(RecyclerView param1RecyclerView) {}
    
    @Deprecated
    public void onDetachedFromWindow(RecyclerView param1RecyclerView) {}
    
    public void onDetachedFromWindow(RecyclerView param1RecyclerView, RecyclerView.Recycler param1Recycler) {
      onDetachedFromWindow(param1RecyclerView);
    }
    
    public View onFocusSearchFailed(View param1View, int param1Int, RecyclerView.Recycler param1Recycler, RecyclerView.State param1State) {
      return null;
    }
    
    public void onInitializeAccessibilityEvent(AccessibilityEvent param1AccessibilityEvent) {
      onInitializeAccessibilityEvent(this.mRecyclerView.mRecycler, this.mRecyclerView.mState, param1AccessibilityEvent);
    }
    
    public void onInitializeAccessibilityEvent(RecyclerView.Recycler param1Recycler, RecyclerView.State param1State, AccessibilityEvent param1AccessibilityEvent) {
      RecyclerView recyclerView = this.mRecyclerView;
      if (recyclerView != null && param1AccessibilityEvent != null) {
        boolean bool1 = true;
        boolean bool2 = bool1;
        if (!recyclerView.canScrollVertically(1)) {
          bool2 = bool1;
          if (!this.mRecyclerView.canScrollVertically(-1)) {
            bool2 = bool1;
            if (!this.mRecyclerView.canScrollHorizontally(-1))
              if (this.mRecyclerView.canScrollHorizontally(1)) {
                bool2 = bool1;
              } else {
                bool2 = false;
              }  
          } 
        } 
        param1AccessibilityEvent.setScrollable(bool2);
        if (this.mRecyclerView.mAdapter != null)
          param1AccessibilityEvent.setItemCount(this.mRecyclerView.mAdapter.getItemCount()); 
      } 
    }
    
    void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfoCompat param1AccessibilityNodeInfoCompat) {
      onInitializeAccessibilityNodeInfo(this.mRecyclerView.mRecycler, this.mRecyclerView.mState, param1AccessibilityNodeInfoCompat);
    }
    
    public void onInitializeAccessibilityNodeInfo(RecyclerView.Recycler param1Recycler, RecyclerView.State param1State, AccessibilityNodeInfoCompat param1AccessibilityNodeInfoCompat) {
      if (this.mRecyclerView.canScrollVertically(-1) || this.mRecyclerView.canScrollHorizontally(-1)) {
        param1AccessibilityNodeInfoCompat.addAction(8192);
        param1AccessibilityNodeInfoCompat.setScrollable(true);
      } 
      if (this.mRecyclerView.canScrollVertically(1) || this.mRecyclerView.canScrollHorizontally(1)) {
        param1AccessibilityNodeInfoCompat.addAction(4096);
        param1AccessibilityNodeInfoCompat.setScrollable(true);
      } 
      param1AccessibilityNodeInfoCompat.setCollectionInfo(AccessibilityNodeInfoCompat.CollectionInfoCompat.obtain(getRowCountForAccessibility(param1Recycler, param1State), getColumnCountForAccessibility(param1Recycler, param1State), isLayoutHierarchical(param1Recycler, param1State), getSelectionModeForAccessibility(param1Recycler, param1State)));
    }
    
    void onInitializeAccessibilityNodeInfoForItem(View param1View, AccessibilityNodeInfoCompat param1AccessibilityNodeInfoCompat) {
      RecyclerView.ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(param1View);
      if (viewHolder != null && !viewHolder.isRemoved() && !this.mChildHelper.isHidden(viewHolder.itemView))
        onInitializeAccessibilityNodeInfoForItem(this.mRecyclerView.mRecycler, this.mRecyclerView.mState, param1View, param1AccessibilityNodeInfoCompat); 
    }
    
    public void onInitializeAccessibilityNodeInfoForItem(RecyclerView.Recycler param1Recycler, RecyclerView.State param1State, View param1View, AccessibilityNodeInfoCompat param1AccessibilityNodeInfoCompat) {
      boolean bool1;
      boolean bool2;
      if (canScrollVertically()) {
        bool1 = getPosition(param1View);
      } else {
        bool1 = false;
      } 
      if (canScrollHorizontally()) {
        bool2 = getPosition(param1View);
      } else {
        bool2 = false;
      } 
      param1AccessibilityNodeInfoCompat.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(bool1, 1, bool2, 1, false, false));
    }
    
    public View onInterceptFocusSearch(View param1View, int param1Int) {
      return null;
    }
    
    public void onItemsAdded(RecyclerView param1RecyclerView, int param1Int1, int param1Int2) {}
    
    public void onItemsChanged(RecyclerView param1RecyclerView) {}
    
    public void onItemsMoved(RecyclerView param1RecyclerView, int param1Int1, int param1Int2, int param1Int3) {}
    
    public void onItemsRemoved(RecyclerView param1RecyclerView, int param1Int1, int param1Int2) {}
    
    public void onItemsUpdated(RecyclerView param1RecyclerView, int param1Int1, int param1Int2) {}
    
    public void onItemsUpdated(RecyclerView param1RecyclerView, int param1Int1, int param1Int2, Object param1Object) {
      onItemsUpdated(param1RecyclerView, param1Int1, param1Int2);
    }
    
    public void onLayoutChildren(RecyclerView.Recycler param1Recycler, RecyclerView.State param1State) {
      Log.e("RecyclerView", "You must override onLayoutChildren(Recycler recycler, State state) ");
    }
    
    public void onLayoutCompleted(RecyclerView.State param1State) {}
    
    public void onMeasure(RecyclerView.Recycler param1Recycler, RecyclerView.State param1State, int param1Int1, int param1Int2) {
      this.mRecyclerView.defaultOnMeasure(param1Int1, param1Int2);
    }
    
    @Deprecated
    public boolean onRequestChildFocus(RecyclerView param1RecyclerView, View param1View1, View param1View2) {
      return (isSmoothScrolling() || param1RecyclerView.isComputingLayout());
    }
    
    public boolean onRequestChildFocus(RecyclerView param1RecyclerView, RecyclerView.State param1State, View param1View1, View param1View2) {
      return onRequestChildFocus(param1RecyclerView, param1View1, param1View2);
    }
    
    public void onRestoreInstanceState(Parcelable param1Parcelable) {}
    
    public Parcelable onSaveInstanceState() {
      return null;
    }
    
    public void onScrollStateChanged(int param1Int) {}
    
    void onSmoothScrollerStopped(RecyclerView.SmoothScroller param1SmoothScroller) {
      if (this.mSmoothScroller == param1SmoothScroller)
        this.mSmoothScroller = null; 
    }
    
    boolean performAccessibilityAction(int param1Int, Bundle param1Bundle) {
      return performAccessibilityAction(this.mRecyclerView.mRecycler, this.mRecyclerView.mState, param1Int, param1Bundle);
    }
    
    public boolean performAccessibilityAction(RecyclerView.Recycler param1Recycler, RecyclerView.State param1State, int param1Int, Bundle param1Bundle) {
      // Byte code:
      //   0: aload_0
      //   1: getfield mRecyclerView : Landroidx/recyclerview/widget/RecyclerView;
      //   4: astore_1
      //   5: aload_1
      //   6: ifnonnull -> 11
      //   9: iconst_0
      //   10: ireturn
      //   11: iload_3
      //   12: sipush #4096
      //   15: if_icmpeq -> 96
      //   18: iload_3
      //   19: sipush #8192
      //   22: if_icmpeq -> 33
      //   25: iconst_0
      //   26: istore_3
      //   27: iconst_0
      //   28: istore #5
      //   30: goto -> 169
      //   33: aload_1
      //   34: iconst_m1
      //   35: invokevirtual canScrollVertically : (I)Z
      //   38: ifeq -> 60
      //   41: aload_0
      //   42: invokevirtual getHeight : ()I
      //   45: aload_0
      //   46: invokevirtual getPaddingTop : ()I
      //   49: isub
      //   50: aload_0
      //   51: invokevirtual getPaddingBottom : ()I
      //   54: isub
      //   55: ineg
      //   56: istore_3
      //   57: goto -> 62
      //   60: iconst_0
      //   61: istore_3
      //   62: iload_3
      //   63: istore #5
      //   65: aload_0
      //   66: getfield mRecyclerView : Landroidx/recyclerview/widget/RecyclerView;
      //   69: iconst_m1
      //   70: invokevirtual canScrollHorizontally : (I)Z
      //   73: ifeq -> 167
      //   76: aload_0
      //   77: invokevirtual getWidth : ()I
      //   80: aload_0
      //   81: invokevirtual getPaddingLeft : ()I
      //   84: isub
      //   85: aload_0
      //   86: invokevirtual getPaddingRight : ()I
      //   89: isub
      //   90: ineg
      //   91: istore #5
      //   93: goto -> 154
      //   96: aload_1
      //   97: iconst_1
      //   98: invokevirtual canScrollVertically : (I)Z
      //   101: ifeq -> 122
      //   104: aload_0
      //   105: invokevirtual getHeight : ()I
      //   108: aload_0
      //   109: invokevirtual getPaddingTop : ()I
      //   112: isub
      //   113: aload_0
      //   114: invokevirtual getPaddingBottom : ()I
      //   117: isub
      //   118: istore_3
      //   119: goto -> 124
      //   122: iconst_0
      //   123: istore_3
      //   124: iload_3
      //   125: istore #5
      //   127: aload_0
      //   128: getfield mRecyclerView : Landroidx/recyclerview/widget/RecyclerView;
      //   131: iconst_1
      //   132: invokevirtual canScrollHorizontally : (I)Z
      //   135: ifeq -> 167
      //   138: aload_0
      //   139: invokevirtual getWidth : ()I
      //   142: aload_0
      //   143: invokevirtual getPaddingLeft : ()I
      //   146: isub
      //   147: aload_0
      //   148: invokevirtual getPaddingRight : ()I
      //   151: isub
      //   152: istore #5
      //   154: iload_3
      //   155: istore #6
      //   157: iload #5
      //   159: istore_3
      //   160: iload #6
      //   162: istore #5
      //   164: goto -> 169
      //   167: iconst_0
      //   168: istore_3
      //   169: iload #5
      //   171: ifne -> 180
      //   174: iload_3
      //   175: ifne -> 180
      //   178: iconst_0
      //   179: ireturn
      //   180: aload_0
      //   181: getfield mRecyclerView : Landroidx/recyclerview/widget/RecyclerView;
      //   184: iload_3
      //   185: iload #5
      //   187: aconst_null
      //   188: ldc -2147483648
      //   190: iconst_1
      //   191: invokevirtual smoothScrollBy : (IILandroid/view/animation/Interpolator;IZ)V
      //   194: iconst_1
      //   195: ireturn
    }
    
    boolean performAccessibilityActionForItem(View param1View, int param1Int, Bundle param1Bundle) {
      return performAccessibilityActionForItem(this.mRecyclerView.mRecycler, this.mRecyclerView.mState, param1View, param1Int, param1Bundle);
    }
    
    public boolean performAccessibilityActionForItem(RecyclerView.Recycler param1Recycler, RecyclerView.State param1State, View param1View, int param1Int, Bundle param1Bundle) {
      return false;
    }
    
    public void postOnAnimation(Runnable param1Runnable) {
      RecyclerView recyclerView = this.mRecyclerView;
      if (recyclerView != null)
        ViewCompat.postOnAnimation((View)recyclerView, param1Runnable); 
    }
    
    public void removeAllViews() {
      for (int i = getChildCount() - 1; i >= 0; i--)
        this.mChildHelper.removeViewAt(i); 
    }
    
    public void removeAndRecycleAllViews(RecyclerView.Recycler param1Recycler) {
      for (int i = getChildCount() - 1; i >= 0; i--) {
        if (!RecyclerView.getChildViewHolderInt(getChildAt(i)).shouldIgnore())
          removeAndRecycleViewAt(i, param1Recycler); 
      } 
    }
    
    void removeAndRecycleScrapInt(RecyclerView.Recycler param1Recycler) {
      int i = param1Recycler.getScrapCount();
      for (int j = i - 1; j >= 0; j--) {
        View view = param1Recycler.getScrapViewAt(j);
        RecyclerView.ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(view);
        if (!viewHolder.shouldIgnore()) {
          viewHolder.setIsRecyclable(false);
          if (viewHolder.isTmpDetached())
            this.mRecyclerView.removeDetachedView(view, false); 
          if (this.mRecyclerView.mItemAnimator != null)
            this.mRecyclerView.mItemAnimator.endAnimation(viewHolder); 
          viewHolder.setIsRecyclable(true);
          param1Recycler.quickRecycleScrapView(view);
        } 
      } 
      param1Recycler.clearScrap();
      if (i > 0)
        this.mRecyclerView.invalidate(); 
    }
    
    public void removeAndRecycleView(View param1View, RecyclerView.Recycler param1Recycler) {
      removeView(param1View);
      param1Recycler.recycleView(param1View);
    }
    
    public void removeAndRecycleViewAt(int param1Int, RecyclerView.Recycler param1Recycler) {
      View view = getChildAt(param1Int);
      removeViewAt(param1Int);
      param1Recycler.recycleView(view);
    }
    
    public boolean removeCallbacks(Runnable param1Runnable) {
      RecyclerView recyclerView = this.mRecyclerView;
      return (recyclerView != null) ? recyclerView.removeCallbacks(param1Runnable) : false;
    }
    
    public void removeDetachedView(View param1View) {
      this.mRecyclerView.removeDetachedView(param1View, false);
    }
    
    public void removeView(View param1View) {
      this.mChildHelper.removeView(param1View);
    }
    
    public void removeViewAt(int param1Int) {
      if (getChildAt(param1Int) != null)
        this.mChildHelper.removeViewAt(param1Int); 
    }
    
    public boolean requestChildRectangleOnScreen(RecyclerView param1RecyclerView, View param1View, Rect param1Rect, boolean param1Boolean) {
      return requestChildRectangleOnScreen(param1RecyclerView, param1View, param1Rect, param1Boolean, false);
    }
    
    public boolean requestChildRectangleOnScreen(RecyclerView param1RecyclerView, View param1View, Rect param1Rect, boolean param1Boolean1, boolean param1Boolean2) {
      int[] arrayOfInt = getChildRectangleOnScreenScrollAmount(param1View, param1Rect);
      int i = arrayOfInt[0];
      int j = arrayOfInt[1];
      if ((!param1Boolean2 || isFocusedChildVisibleAfterScrolling(param1RecyclerView, i, j)) && (i != 0 || j != 0)) {
        if (param1Boolean1) {
          param1RecyclerView.scrollBy(i, j);
        } else {
          param1RecyclerView.smoothScrollBy(i, j);
        } 
        return true;
      } 
      return false;
    }
    
    public void requestLayout() {
      RecyclerView recyclerView = this.mRecyclerView;
      if (recyclerView != null)
        recyclerView.requestLayout(); 
    }
    
    public void requestSimpleAnimationsInNextLayout() {
      this.mRequestedSimpleAnimations = true;
    }
    
    public int scrollHorizontallyBy(int param1Int, RecyclerView.Recycler param1Recycler, RecyclerView.State param1State) {
      return 0;
    }
    
    public void scrollToPosition(int param1Int) {}
    
    public int scrollVerticallyBy(int param1Int, RecyclerView.Recycler param1Recycler, RecyclerView.State param1State) {
      return 0;
    }
    
    @Deprecated
    public void setAutoMeasureEnabled(boolean param1Boolean) {
      this.mAutoMeasure = param1Boolean;
    }
    
    void setExactMeasureSpecsFrom(RecyclerView param1RecyclerView) {
      setMeasureSpecs(View.MeasureSpec.makeMeasureSpec(param1RecyclerView.getWidth(), 1073741824), View.MeasureSpec.makeMeasureSpec(param1RecyclerView.getHeight(), 1073741824));
    }
    
    public final void setItemPrefetchEnabled(boolean param1Boolean) {
      if (param1Boolean != this.mItemPrefetchEnabled) {
        this.mItemPrefetchEnabled = param1Boolean;
        this.mPrefetchMaxCountObserved = 0;
        RecyclerView recyclerView = this.mRecyclerView;
        if (recyclerView != null)
          recyclerView.mRecycler.updateViewCacheSize(); 
      } 
    }
    
    void setMeasureSpecs(int param1Int1, int param1Int2) {
      this.mWidth = View.MeasureSpec.getSize(param1Int1);
      this.mWidthMode = View.MeasureSpec.getMode(param1Int1);
      if (this.mWidthMode == 0 && !RecyclerView.ALLOW_SIZE_IN_UNSPECIFIED_SPEC)
        this.mWidth = 0; 
      this.mHeight = View.MeasureSpec.getSize(param1Int2);
      this.mHeightMode = View.MeasureSpec.getMode(param1Int2);
      if (this.mHeightMode == 0 && !RecyclerView.ALLOW_SIZE_IN_UNSPECIFIED_SPEC)
        this.mHeight = 0; 
    }
    
    public void setMeasuredDimension(int param1Int1, int param1Int2) {
      this.mRecyclerView.setMeasuredDimension(param1Int1, param1Int2);
    }
    
    public void setMeasuredDimension(Rect param1Rect, int param1Int1, int param1Int2) {
      int i = param1Rect.width();
      int j = getPaddingLeft();
      int k = getPaddingRight();
      int m = param1Rect.height();
      int n = getPaddingTop();
      int i1 = getPaddingBottom();
      setMeasuredDimension(chooseSize(param1Int1, i + j + k, getMinimumWidth()), chooseSize(param1Int2, m + n + i1, getMinimumHeight()));
    }
    
    void setMeasuredDimensionFromChildren(int param1Int1, int param1Int2) {
      int i = getChildCount();
      if (i == 0) {
        this.mRecyclerView.defaultOnMeasure(param1Int1, param1Int2);
        return;
      } 
      byte b = 0;
      int j = Integer.MAX_VALUE;
      int k = Integer.MAX_VALUE;
      int m = Integer.MIN_VALUE;
      int n;
      for (n = Integer.MIN_VALUE; b < i; n = i3) {
        View view = getChildAt(b);
        Rect rect = this.mRecyclerView.mTempRect;
        getDecoratedBoundsWithMargins(view, rect);
        int i1 = j;
        if (rect.left < j)
          i1 = rect.left; 
        int i2 = m;
        if (rect.right > m)
          i2 = rect.right; 
        m = k;
        if (rect.top < k)
          m = rect.top; 
        int i3 = n;
        if (rect.bottom > n)
          i3 = rect.bottom; 
        b++;
        k = m;
        j = i1;
        m = i2;
      } 
      this.mRecyclerView.mTempRect.set(j, k, m, n);
      setMeasuredDimension(this.mRecyclerView.mTempRect, param1Int1, param1Int2);
    }
    
    public void setMeasurementCacheEnabled(boolean param1Boolean) {
      this.mMeasurementCacheEnabled = param1Boolean;
    }
    
    void setRecyclerView(RecyclerView param1RecyclerView) {
      if (param1RecyclerView == null) {
        this.mRecyclerView = null;
        this.mChildHelper = null;
        this.mWidth = 0;
        this.mHeight = 0;
      } else {
        this.mRecyclerView = param1RecyclerView;
        this.mChildHelper = param1RecyclerView.mChildHelper;
        this.mWidth = param1RecyclerView.getWidth();
        this.mHeight = param1RecyclerView.getHeight();
      } 
      this.mWidthMode = 1073741824;
      this.mHeightMode = 1073741824;
    }
    
    boolean shouldMeasureChild(View param1View, int param1Int1, int param1Int2, RecyclerView.LayoutParams param1LayoutParams) {
      return (param1View.isLayoutRequested() || !this.mMeasurementCacheEnabled || !isMeasurementUpToDate(param1View.getWidth(), param1Int1, param1LayoutParams.width) || !isMeasurementUpToDate(param1View.getHeight(), param1Int2, param1LayoutParams.height));
    }
    
    boolean shouldMeasureTwice() {
      return false;
    }
    
    boolean shouldReMeasureChild(View param1View, int param1Int1, int param1Int2, RecyclerView.LayoutParams param1LayoutParams) {
      return (!this.mMeasurementCacheEnabled || !isMeasurementUpToDate(param1View.getMeasuredWidth(), param1Int1, param1LayoutParams.width) || !isMeasurementUpToDate(param1View.getMeasuredHeight(), param1Int2, param1LayoutParams.height));
    }
    
    public void smoothScrollToPosition(RecyclerView param1RecyclerView, RecyclerView.State param1State, int param1Int) {
      Log.e("RecyclerView", "You must override smoothScrollToPosition to support smooth scrolling");
    }
    
    public void startSmoothScroll(RecyclerView.SmoothScroller param1SmoothScroller) {
      RecyclerView.SmoothScroller smoothScroller = this.mSmoothScroller;
      if (smoothScroller != null && param1SmoothScroller != smoothScroller && smoothScroller.isRunning())
        this.mSmoothScroller.stop(); 
      this.mSmoothScroller = param1SmoothScroller;
      this.mSmoothScroller.start(this.mRecyclerView, this);
    }
    
    public void stopIgnoringView(View param1View) {
      RecyclerView.ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(param1View);
      viewHolder.stopIgnoring();
      viewHolder.resetInternal();
      viewHolder.addFlags(4);
    }
    
    void stopSmoothScroller() {
      RecyclerView.SmoothScroller smoothScroller = this.mSmoothScroller;
      if (smoothScroller != null)
        smoothScroller.stop(); 
    }
    
    public boolean supportsPredictiveItemAnimations() {
      return false;
    }
    
    public static interface LayoutPrefetchRegistry {
      void addPosition(int param2Int1, int param2Int2);
    }
    
    public static class Properties {
      public int orientation;
      
      public boolean reverseLayout;
      
      public int spanCount;
      
      public boolean stackFromEnd;
    }
  }
  
  class null implements ViewBoundsCheck.Callback {
    public View getChildAt(int param1Int) {
      return this.this$0.getChildAt(param1Int);
    }
    
    public int getChildEnd(View param1View) {
      RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)param1View.getLayoutParams();
      return this.this$0.getDecoratedRight(param1View) + layoutParams.rightMargin;
    }
    
    public int getChildStart(View param1View) {
      RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)param1View.getLayoutParams();
      return this.this$0.getDecoratedLeft(param1View) - layoutParams.leftMargin;
    }
    
    public int getParentEnd() {
      return this.this$0.getWidth() - this.this$0.getPaddingRight();
    }
    
    public int getParentStart() {
      return this.this$0.getPaddingLeft();
    }
  }
  
  class null implements ViewBoundsCheck.Callback {
    public View getChildAt(int param1Int) {
      return this.this$0.getChildAt(param1Int);
    }
    
    public int getChildEnd(View param1View) {
      RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)param1View.getLayoutParams();
      return this.this$0.getDecoratedBottom(param1View) + layoutParams.bottomMargin;
    }
    
    public int getChildStart(View param1View) {
      RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)param1View.getLayoutParams();
      return this.this$0.getDecoratedTop(param1View) - layoutParams.topMargin;
    }
    
    public int getParentEnd() {
      return this.this$0.getHeight() - this.this$0.getPaddingBottom();
    }
    
    public int getParentStart() {
      return this.this$0.getPaddingTop();
    }
  }
  
  public static interface LayoutPrefetchRegistry {
    void addPosition(int param1Int1, int param1Int2);
  }
  
  public static class Properties {
    public int orientation;
    
    public boolean reverseLayout;
    
    public int spanCount;
    
    public boolean stackFromEnd;
  }
  
  public static class LayoutParams extends ViewGroup.MarginLayoutParams {
    final Rect mDecorInsets = new Rect();
    
    boolean mInsetsDirty = true;
    
    boolean mPendingInvalidate = false;
    
    RecyclerView.ViewHolder mViewHolder;
    
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
    
    public LayoutParams(LayoutParams param1LayoutParams) {
      super((ViewGroup.LayoutParams)param1LayoutParams);
    }
    
    public int getViewAdapterPosition() {
      return this.mViewHolder.getAdapterPosition();
    }
    
    public int getViewLayoutPosition() {
      return this.mViewHolder.getLayoutPosition();
    }
    
    @Deprecated
    public int getViewPosition() {
      return this.mViewHolder.getPosition();
    }
    
    public boolean isItemChanged() {
      return this.mViewHolder.isUpdated();
    }
    
    public boolean isItemRemoved() {
      return this.mViewHolder.isRemoved();
    }
    
    public boolean isViewInvalid() {
      return this.mViewHolder.isInvalid();
    }
    
    public boolean viewNeedsUpdate() {
      return this.mViewHolder.needsUpdate();
    }
  }
  
  public static interface OnChildAttachStateChangeListener {
    void onChildViewAttachedToWindow(View param1View);
    
    void onChildViewDetachedFromWindow(View param1View);
  }
  
  public static abstract class OnFlingListener {
    public abstract boolean onFling(int param1Int1, int param1Int2);
  }
  
  public static interface OnItemTouchListener {
    boolean onInterceptTouchEvent(RecyclerView param1RecyclerView, MotionEvent param1MotionEvent);
    
    void onRequestDisallowInterceptTouchEvent(boolean param1Boolean);
    
    void onTouchEvent(RecyclerView param1RecyclerView, MotionEvent param1MotionEvent);
  }
  
  public static abstract class OnScrollListener {
    public void onScrollStateChanged(RecyclerView param1RecyclerView, int param1Int) {}
    
    public void onScrolled(RecyclerView param1RecyclerView, int param1Int1, int param1Int2) {}
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Orientation {}
  
  public static class RecycledViewPool {
    private static final int DEFAULT_MAX_SCRAP = 5;
    
    private int mAttachCount = 0;
    
    SparseArray<ScrapData> mScrap = new SparseArray();
    
    private ScrapData getScrapDataForType(int param1Int) {
      ScrapData scrapData1 = (ScrapData)this.mScrap.get(param1Int);
      ScrapData scrapData2 = scrapData1;
      if (scrapData1 == null) {
        scrapData2 = new ScrapData();
        this.mScrap.put(param1Int, scrapData2);
      } 
      return scrapData2;
    }
    
    void attach() {
      this.mAttachCount++;
    }
    
    public void clear() {
      for (byte b = 0; b < this.mScrap.size(); b++)
        ((ScrapData)this.mScrap.valueAt(b)).mScrapHeap.clear(); 
    }
    
    void detach() {
      this.mAttachCount--;
    }
    
    void factorInBindTime(int param1Int, long param1Long) {
      ScrapData scrapData = getScrapDataForType(param1Int);
      scrapData.mBindRunningAverageNs = runningAverage(scrapData.mBindRunningAverageNs, param1Long);
    }
    
    void factorInCreateTime(int param1Int, long param1Long) {
      ScrapData scrapData = getScrapDataForType(param1Int);
      scrapData.mCreateRunningAverageNs = runningAverage(scrapData.mCreateRunningAverageNs, param1Long);
    }
    
    public RecyclerView.ViewHolder getRecycledView(int param1Int) {
      ScrapData scrapData = (ScrapData)this.mScrap.get(param1Int);
      if (scrapData != null && !scrapData.mScrapHeap.isEmpty()) {
        ArrayList<RecyclerView.ViewHolder> arrayList = scrapData.mScrapHeap;
        for (param1Int = arrayList.size() - 1; param1Int >= 0; param1Int--) {
          if (!((RecyclerView.ViewHolder)arrayList.get(param1Int)).isAttachedToTransitionOverlay())
            return arrayList.remove(param1Int); 
        } 
      } 
      return null;
    }
    
    public int getRecycledViewCount(int param1Int) {
      return (getScrapDataForType(param1Int)).mScrapHeap.size();
    }
    
    void onAdapterChanged(RecyclerView.Adapter param1Adapter1, RecyclerView.Adapter param1Adapter2, boolean param1Boolean) {
      if (param1Adapter1 != null)
        detach(); 
      if (!param1Boolean && this.mAttachCount == 0)
        clear(); 
      if (param1Adapter2 != null)
        attach(); 
    }
    
    public void putRecycledView(RecyclerView.ViewHolder param1ViewHolder) {
      int i = param1ViewHolder.getItemViewType();
      ArrayList<RecyclerView.ViewHolder> arrayList = (getScrapDataForType(i)).mScrapHeap;
      if (((ScrapData)this.mScrap.get(i)).mMaxScrap <= arrayList.size())
        return; 
      param1ViewHolder.resetInternal();
      arrayList.add(param1ViewHolder);
    }
    
    long runningAverage(long param1Long1, long param1Long2) {
      return (param1Long1 == 0L) ? param1Long2 : (param1Long1 / 4L * 3L + param1Long2 / 4L);
    }
    
    public void setMaxRecycledViews(int param1Int1, int param1Int2) {
      ScrapData scrapData = getScrapDataForType(param1Int1);
      scrapData.mMaxScrap = param1Int2;
      ArrayList<RecyclerView.ViewHolder> arrayList = scrapData.mScrapHeap;
      while (arrayList.size() > param1Int2)
        arrayList.remove(arrayList.size() - 1); 
    }
    
    int size() {
      byte b = 0;
      int i;
      for (i = 0; b < this.mScrap.size(); i = j) {
        ArrayList<RecyclerView.ViewHolder> arrayList = ((ScrapData)this.mScrap.valueAt(b)).mScrapHeap;
        int j = i;
        if (arrayList != null)
          j = i + arrayList.size(); 
        b++;
      } 
      return i;
    }
    
    boolean willBindInTime(int param1Int, long param1Long1, long param1Long2) {
      long l = (getScrapDataForType(param1Int)).mBindRunningAverageNs;
      return (l == 0L || param1Long1 + l < param1Long2);
    }
    
    boolean willCreateInTime(int param1Int, long param1Long1, long param1Long2) {
      long l = (getScrapDataForType(param1Int)).mCreateRunningAverageNs;
      return (l == 0L || param1Long1 + l < param1Long2);
    }
    
    static class ScrapData {
      long mBindRunningAverageNs = 0L;
      
      long mCreateRunningAverageNs = 0L;
      
      int mMaxScrap = 5;
      
      final ArrayList<RecyclerView.ViewHolder> mScrapHeap = new ArrayList<RecyclerView.ViewHolder>();
    }
  }
  
  static class ScrapData {
    long mBindRunningAverageNs = 0L;
    
    long mCreateRunningAverageNs = 0L;
    
    int mMaxScrap = 5;
    
    final ArrayList<RecyclerView.ViewHolder> mScrapHeap = new ArrayList<RecyclerView.ViewHolder>();
  }
  
  public final class Recycler {
    static final int DEFAULT_CACHE_SIZE = 2;
    
    final ArrayList<RecyclerView.ViewHolder> mAttachedScrap = new ArrayList<RecyclerView.ViewHolder>();
    
    final ArrayList<RecyclerView.ViewHolder> mCachedViews = new ArrayList<RecyclerView.ViewHolder>();
    
    ArrayList<RecyclerView.ViewHolder> mChangedScrap = null;
    
    RecyclerView.RecycledViewPool mRecyclerPool;
    
    private int mRequestedCacheMax = 2;
    
    private final List<RecyclerView.ViewHolder> mUnmodifiableAttachedScrap = Collections.unmodifiableList(this.mAttachedScrap);
    
    private RecyclerView.ViewCacheExtension mViewCacheExtension;
    
    int mViewCacheMax = 2;
    
    private void attachAccessibilityDelegateOnBind(RecyclerView.ViewHolder param1ViewHolder) {
      if (RecyclerView.this.isAccessibilityEnabled()) {
        View view = param1ViewHolder.itemView;
        if (ViewCompat.getImportantForAccessibility(view) == 0)
          ViewCompat.setImportantForAccessibility(view, 1); 
        if (RecyclerView.this.mAccessibilityDelegate == null)
          return; 
        AccessibilityDelegateCompat accessibilityDelegateCompat = RecyclerView.this.mAccessibilityDelegate.getItemDelegate();
        if (accessibilityDelegateCompat instanceof RecyclerViewAccessibilityDelegate.ItemDelegate)
          ((RecyclerViewAccessibilityDelegate.ItemDelegate)accessibilityDelegateCompat).saveOriginalDelegate(view); 
        ViewCompat.setAccessibilityDelegate(view, accessibilityDelegateCompat);
      } 
    }
    
    private void invalidateDisplayListInt(ViewGroup param1ViewGroup, boolean param1Boolean) {
      int i;
      for (i = param1ViewGroup.getChildCount() - 1; i >= 0; i--) {
        View view = param1ViewGroup.getChildAt(i);
        if (view instanceof ViewGroup)
          invalidateDisplayListInt((ViewGroup)view, true); 
      } 
      if (!param1Boolean)
        return; 
      if (param1ViewGroup.getVisibility() == 4) {
        param1ViewGroup.setVisibility(0);
        param1ViewGroup.setVisibility(4);
      } else {
        i = param1ViewGroup.getVisibility();
        param1ViewGroup.setVisibility(4);
        param1ViewGroup.setVisibility(i);
      } 
    }
    
    private void invalidateDisplayListInt(RecyclerView.ViewHolder param1ViewHolder) {
      if (param1ViewHolder.itemView instanceof ViewGroup)
        invalidateDisplayListInt((ViewGroup)param1ViewHolder.itemView, false); 
    }
    
    private boolean tryBindViewHolderByDeadline(RecyclerView.ViewHolder param1ViewHolder, int param1Int1, int param1Int2, long param1Long) {
      param1ViewHolder.mOwnerRecyclerView = RecyclerView.this;
      int i = param1ViewHolder.getItemViewType();
      long l = RecyclerView.this.getNanoTime();
      if (param1Long != Long.MAX_VALUE && !this.mRecyclerPool.willBindInTime(i, l, param1Long))
        return false; 
      RecyclerView.this.mAdapter.bindViewHolder(param1ViewHolder, param1Int1);
      param1Long = RecyclerView.this.getNanoTime();
      this.mRecyclerPool.factorInBindTime(param1ViewHolder.getItemViewType(), param1Long - l);
      attachAccessibilityDelegateOnBind(param1ViewHolder);
      if (RecyclerView.this.mState.isPreLayout())
        param1ViewHolder.mPreLayoutPosition = param1Int2; 
      return true;
    }
    
    void addViewHolderToRecycledViewPool(RecyclerView.ViewHolder param1ViewHolder, boolean param1Boolean) {
      RecyclerView.clearNestedRecyclerViewIfNotNested(param1ViewHolder);
      View view = param1ViewHolder.itemView;
      if (RecyclerView.this.mAccessibilityDelegate != null) {
        AccessibilityDelegateCompat accessibilityDelegateCompat = RecyclerView.this.mAccessibilityDelegate.getItemDelegate();
        if (accessibilityDelegateCompat instanceof RecyclerViewAccessibilityDelegate.ItemDelegate) {
          accessibilityDelegateCompat = ((RecyclerViewAccessibilityDelegate.ItemDelegate)accessibilityDelegateCompat).getAndRemoveOriginalDelegateForItem(view);
        } else {
          accessibilityDelegateCompat = null;
        } 
        ViewCompat.setAccessibilityDelegate(view, accessibilityDelegateCompat);
      } 
      if (param1Boolean)
        dispatchViewRecycled(param1ViewHolder); 
      param1ViewHolder.mOwnerRecyclerView = null;
      getRecycledViewPool().putRecycledView(param1ViewHolder);
    }
    
    public void bindViewToPosition(View param1View, int param1Int) {
      RecyclerView.ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(param1View);
      if (viewHolder != null) {
        int i = RecyclerView.this.mAdapterHelper.findPositionOffset(param1Int);
        if (i >= 0 && i < RecyclerView.this.mAdapter.getItemCount()) {
          RecyclerView.LayoutParams layoutParams;
          tryBindViewHolderByDeadline(viewHolder, i, param1Int, Long.MAX_VALUE);
          ViewGroup.LayoutParams layoutParams1 = viewHolder.itemView.getLayoutParams();
          if (layoutParams1 == null) {
            layoutParams = (RecyclerView.LayoutParams)RecyclerView.this.generateDefaultLayoutParams();
            viewHolder.itemView.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
          } else if (!RecyclerView.this.checkLayoutParams((ViewGroup.LayoutParams)layoutParams)) {
            layoutParams = (RecyclerView.LayoutParams)RecyclerView.this.generateLayoutParams((ViewGroup.LayoutParams)layoutParams);
            viewHolder.itemView.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
          } else {
            layoutParams = layoutParams;
          } 
          boolean bool = true;
          layoutParams.mInsetsDirty = true;
          layoutParams.mViewHolder = viewHolder;
          if (viewHolder.itemView.getParent() != null)
            bool = false; 
          layoutParams.mPendingInvalidate = bool;
          return;
        } 
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("Inconsistency detected. Invalid item position ");
        stringBuilder1.append(param1Int);
        stringBuilder1.append("(offset:");
        stringBuilder1.append(i);
        stringBuilder1.append(").state:");
        stringBuilder1.append(RecyclerView.this.mState.getItemCount());
        stringBuilder1.append(RecyclerView.this.exceptionLabel());
        throw new IndexOutOfBoundsException(stringBuilder1.toString());
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("The view does not have a ViewHolder. You cannot pass arbitrary views to this method, they should be created by the Adapter");
      stringBuilder.append(RecyclerView.this.exceptionLabel());
      throw new IllegalArgumentException(stringBuilder.toString());
    }
    
    public void clear() {
      this.mAttachedScrap.clear();
      recycleAndClearCachedViews();
    }
    
    void clearOldPositions() {
      int i = this.mCachedViews.size();
      boolean bool = false;
      byte b;
      for (b = 0; b < i; b++)
        ((RecyclerView.ViewHolder)this.mCachedViews.get(b)).clearOldPosition(); 
      i = this.mAttachedScrap.size();
      for (b = 0; b < i; b++)
        ((RecyclerView.ViewHolder)this.mAttachedScrap.get(b)).clearOldPosition(); 
      ArrayList<RecyclerView.ViewHolder> arrayList = this.mChangedScrap;
      if (arrayList != null) {
        i = arrayList.size();
        for (b = bool; b < i; b++)
          ((RecyclerView.ViewHolder)this.mChangedScrap.get(b)).clearOldPosition(); 
      } 
    }
    
    void clearScrap() {
      this.mAttachedScrap.clear();
      ArrayList<RecyclerView.ViewHolder> arrayList = this.mChangedScrap;
      if (arrayList != null)
        arrayList.clear(); 
    }
    
    public int convertPreLayoutPositionToPostLayout(int param1Int) {
      if (param1Int >= 0 && param1Int < RecyclerView.this.mState.getItemCount())
        return !RecyclerView.this.mState.isPreLayout() ? param1Int : RecyclerView.this.mAdapterHelper.findPositionOffset(param1Int); 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("invalid position ");
      stringBuilder.append(param1Int);
      stringBuilder.append(". State item count is ");
      stringBuilder.append(RecyclerView.this.mState.getItemCount());
      stringBuilder.append(RecyclerView.this.exceptionLabel());
      throw new IndexOutOfBoundsException(stringBuilder.toString());
    }
    
    void dispatchViewRecycled(RecyclerView.ViewHolder param1ViewHolder) {
      if (RecyclerView.this.mRecyclerListener != null)
        RecyclerView.this.mRecyclerListener.onViewRecycled(param1ViewHolder); 
      if (RecyclerView.this.mAdapter != null)
        RecyclerView.this.mAdapter.onViewRecycled(param1ViewHolder); 
      if (RecyclerView.this.mState != null)
        RecyclerView.this.mViewInfoStore.removeViewHolder(param1ViewHolder); 
    }
    
    RecyclerView.ViewHolder getChangedScrapViewForPosition(int param1Int) {
      ArrayList<RecyclerView.ViewHolder> arrayList = this.mChangedScrap;
      if (arrayList != null) {
        int i = arrayList.size();
        if (i != 0) {
          boolean bool = false;
          for (byte b = 0; b < i; b++) {
            RecyclerView.ViewHolder viewHolder = this.mChangedScrap.get(b);
            if (!viewHolder.wasReturnedFromScrap() && viewHolder.getLayoutPosition() == param1Int) {
              viewHolder.addFlags(32);
              return viewHolder;
            } 
          } 
          if (RecyclerView.this.mAdapter.hasStableIds()) {
            param1Int = RecyclerView.this.mAdapterHelper.findPositionOffset(param1Int);
            if (param1Int > 0 && param1Int < RecyclerView.this.mAdapter.getItemCount()) {
              long l = RecyclerView.this.mAdapter.getItemId(param1Int);
              for (param1Int = bool; param1Int < i; param1Int++) {
                RecyclerView.ViewHolder viewHolder = this.mChangedScrap.get(param1Int);
                if (!viewHolder.wasReturnedFromScrap() && viewHolder.getItemId() == l) {
                  viewHolder.addFlags(32);
                  return viewHolder;
                } 
              } 
            } 
          } 
        } 
      } 
      return null;
    }
    
    RecyclerView.RecycledViewPool getRecycledViewPool() {
      if (this.mRecyclerPool == null)
        this.mRecyclerPool = new RecyclerView.RecycledViewPool(); 
      return this.mRecyclerPool;
    }
    
    int getScrapCount() {
      return this.mAttachedScrap.size();
    }
    
    public List<RecyclerView.ViewHolder> getScrapList() {
      return this.mUnmodifiableAttachedScrap;
    }
    
    RecyclerView.ViewHolder getScrapOrCachedViewForId(long param1Long, int param1Int, boolean param1Boolean) {
      int i;
      for (i = this.mAttachedScrap.size() - 1; i >= 0; i--) {
        RecyclerView.ViewHolder viewHolder = this.mAttachedScrap.get(i);
        if (viewHolder.getItemId() == param1Long && !viewHolder.wasReturnedFromScrap()) {
          if (param1Int == viewHolder.getItemViewType()) {
            viewHolder.addFlags(32);
            if (viewHolder.isRemoved() && !RecyclerView.this.mState.isPreLayout())
              viewHolder.setFlags(2, 14); 
            return viewHolder;
          } 
          if (!param1Boolean) {
            this.mAttachedScrap.remove(i);
            RecyclerView.this.removeDetachedView(viewHolder.itemView, false);
            quickRecycleScrapView(viewHolder.itemView);
          } 
        } 
      } 
      for (i = this.mCachedViews.size() - 1; i >= 0; i--) {
        RecyclerView.ViewHolder viewHolder = this.mCachedViews.get(i);
        if (viewHolder.getItemId() == param1Long && !viewHolder.isAttachedToTransitionOverlay()) {
          if (param1Int == viewHolder.getItemViewType()) {
            if (!param1Boolean)
              this.mCachedViews.remove(i); 
            return viewHolder;
          } 
          if (!param1Boolean) {
            recycleCachedViewAt(i);
            return null;
          } 
        } 
      } 
      return null;
    }
    
    RecyclerView.ViewHolder getScrapOrHiddenOrCachedHolderForPosition(int param1Int, boolean param1Boolean) {
      int i = this.mAttachedScrap.size();
      boolean bool = false;
      byte b;
      for (b = 0; b < i; b++) {
        RecyclerView.ViewHolder viewHolder = this.mAttachedScrap.get(b);
        if (!viewHolder.wasReturnedFromScrap() && viewHolder.getLayoutPosition() == param1Int && !viewHolder.isInvalid() && (RecyclerView.this.mState.mInPreLayout || !viewHolder.isRemoved())) {
          viewHolder.addFlags(32);
          return viewHolder;
        } 
      } 
      if (!param1Boolean) {
        View view = RecyclerView.this.mChildHelper.findHiddenNonRemovedView(param1Int);
        if (view != null) {
          RecyclerView.ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(view);
          RecyclerView.this.mChildHelper.unhide(view);
          param1Int = RecyclerView.this.mChildHelper.indexOfChild(view);
          if (param1Int != -1) {
            RecyclerView.this.mChildHelper.detachViewFromParent(param1Int);
            scrapView(view);
            viewHolder.addFlags(8224);
            return viewHolder;
          } 
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("layout index should not be -1 after unhiding a view:");
          stringBuilder.append(viewHolder);
          stringBuilder.append(RecyclerView.this.exceptionLabel());
          throw new IllegalStateException(stringBuilder.toString());
        } 
      } 
      i = this.mCachedViews.size();
      for (b = bool; b < i; b++) {
        RecyclerView.ViewHolder viewHolder = this.mCachedViews.get(b);
        if (!viewHolder.isInvalid() && viewHolder.getLayoutPosition() == param1Int && !viewHolder.isAttachedToTransitionOverlay()) {
          if (!param1Boolean)
            this.mCachedViews.remove(b); 
          return viewHolder;
        } 
      } 
      return null;
    }
    
    View getScrapViewAt(int param1Int) {
      return ((RecyclerView.ViewHolder)this.mAttachedScrap.get(param1Int)).itemView;
    }
    
    public View getViewForPosition(int param1Int) {
      return getViewForPosition(param1Int, false);
    }
    
    View getViewForPosition(int param1Int, boolean param1Boolean) {
      return (tryGetViewHolderForPositionByDeadline(param1Int, param1Boolean, Long.MAX_VALUE)).itemView;
    }
    
    void markItemDecorInsetsDirty() {
      int i = this.mCachedViews.size();
      for (byte b = 0; b < i; b++) {
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)((RecyclerView.ViewHolder)this.mCachedViews.get(b)).itemView.getLayoutParams();
        if (layoutParams != null)
          layoutParams.mInsetsDirty = true; 
      } 
    }
    
    void markKnownViewsInvalid() {
      int i = this.mCachedViews.size();
      for (byte b = 0; b < i; b++) {
        RecyclerView.ViewHolder viewHolder = this.mCachedViews.get(b);
        if (viewHolder != null) {
          viewHolder.addFlags(6);
          viewHolder.addChangePayload(null);
        } 
      } 
      if (RecyclerView.this.mAdapter == null || !RecyclerView.this.mAdapter.hasStableIds())
        recycleAndClearCachedViews(); 
    }
    
    void offsetPositionRecordsForInsert(int param1Int1, int param1Int2) {
      int i = this.mCachedViews.size();
      for (byte b = 0; b < i; b++) {
        RecyclerView.ViewHolder viewHolder = this.mCachedViews.get(b);
        if (viewHolder != null && viewHolder.mPosition >= param1Int1)
          viewHolder.offsetPosition(param1Int2, true); 
      } 
    }
    
    void offsetPositionRecordsForMove(int param1Int1, int param1Int2) {
      int i;
      int j;
      boolean bool;
      if (param1Int1 < param1Int2) {
        i = param1Int1;
        j = param1Int2;
        bool = true;
      } else {
        j = param1Int1;
        i = param1Int2;
        bool = true;
      } 
      int k = this.mCachedViews.size();
      for (byte b = 0; b < k; b++) {
        RecyclerView.ViewHolder viewHolder = this.mCachedViews.get(b);
        if (viewHolder != null && viewHolder.mPosition >= i && viewHolder.mPosition <= j)
          if (viewHolder.mPosition == param1Int1) {
            viewHolder.offsetPosition(param1Int2 - param1Int1, false);
          } else {
            viewHolder.offsetPosition(bool, false);
          }  
      } 
    }
    
    void offsetPositionRecordsForRemove(int param1Int1, int param1Int2, boolean param1Boolean) {
      for (int i = this.mCachedViews.size() - 1; i >= 0; i--) {
        RecyclerView.ViewHolder viewHolder = this.mCachedViews.get(i);
        if (viewHolder != null)
          if (viewHolder.mPosition >= param1Int1 + param1Int2) {
            viewHolder.offsetPosition(-param1Int2, param1Boolean);
          } else if (viewHolder.mPosition >= param1Int1) {
            viewHolder.addFlags(8);
            recycleCachedViewAt(i);
          }  
      } 
    }
    
    void onAdapterChanged(RecyclerView.Adapter param1Adapter1, RecyclerView.Adapter param1Adapter2, boolean param1Boolean) {
      clear();
      getRecycledViewPool().onAdapterChanged(param1Adapter1, param1Adapter2, param1Boolean);
    }
    
    void quickRecycleScrapView(View param1View) {
      RecyclerView.ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(param1View);
      viewHolder.mScrapContainer = null;
      viewHolder.mInChangeScrap = false;
      viewHolder.clearReturnedFromScrapFlag();
      recycleViewHolderInternal(viewHolder);
    }
    
    void recycleAndClearCachedViews() {
      for (int i = this.mCachedViews.size() - 1; i >= 0; i--)
        recycleCachedViewAt(i); 
      this.mCachedViews.clear();
      if (RecyclerView.ALLOW_THREAD_GAP_WORK)
        RecyclerView.this.mPrefetchRegistry.clearPrefetchPositions(); 
    }
    
    void recycleCachedViewAt(int param1Int) {
      addViewHolderToRecycledViewPool(this.mCachedViews.get(param1Int), true);
      this.mCachedViews.remove(param1Int);
    }
    
    public void recycleView(View param1View) {
      RecyclerView.ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(param1View);
      if (viewHolder.isTmpDetached())
        RecyclerView.this.removeDetachedView(param1View, false); 
      if (viewHolder.isScrap()) {
        viewHolder.unScrap();
      } else if (viewHolder.wasReturnedFromScrap()) {
        viewHolder.clearReturnedFromScrapFlag();
      } 
      recycleViewHolderInternal(viewHolder);
      if (RecyclerView.this.mItemAnimator != null && !viewHolder.isRecyclable())
        RecyclerView.this.mItemAnimator.endAnimation(viewHolder); 
    }
    
    void recycleViewHolderInternal(RecyclerView.ViewHolder param1ViewHolder) {
      IllegalArgumentException illegalArgumentException;
      StringBuilder stringBuilder1;
      boolean bool1 = param1ViewHolder.isScrap();
      boolean bool2 = false;
      boolean bool = false;
      if (bool1 || param1ViewHolder.itemView.getParent() != null) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Scrapped or attached views may not be recycled. isScrap:");
        stringBuilder.append(param1ViewHolder.isScrap());
        stringBuilder.append(" isAttached:");
        if (param1ViewHolder.itemView.getParent() != null)
          bool2 = true; 
        stringBuilder.append(bool2);
        stringBuilder.append(RecyclerView.this.exceptionLabel());
        illegalArgumentException = new IllegalArgumentException(stringBuilder.toString());
        throw illegalArgumentException;
      } 
      if (!illegalArgumentException.isTmpDetached()) {
        if (!illegalArgumentException.shouldIgnore()) {
          int i;
          boolean bool3;
          bool2 = illegalArgumentException.doesTransientStatePreventRecycling();
          if (RecyclerView.this.mAdapter != null && bool2 && RecyclerView.this.mAdapter.onFailedToRecycleView(illegalArgumentException)) {
            i = 1;
          } else {
            i = 0;
          } 
          if (i || illegalArgumentException.isRecyclable()) {
            if (this.mViewCacheMax > 0 && !illegalArgumentException.hasAnyOfTheFlags(526)) {
              int j = this.mCachedViews.size();
              i = j;
              if (j >= this.mViewCacheMax) {
                i = j;
                if (j > 0) {
                  recycleCachedViewAt(0);
                  i = j - 1;
                } 
              } 
              j = i;
              if (RecyclerView.ALLOW_THREAD_GAP_WORK) {
                j = i;
                if (i > 0) {
                  j = i;
                  if (!RecyclerView.this.mPrefetchRegistry.lastPrefetchIncludedPosition(((RecyclerView.ViewHolder)illegalArgumentException).mPosition)) {
                    while (--i >= 0) {
                      j = ((RecyclerView.ViewHolder)this.mCachedViews.get(i)).mPosition;
                      if (!RecyclerView.this.mPrefetchRegistry.lastPrefetchIncludedPosition(j))
                        break; 
                      i--;
                    } 
                    j = i + 1;
                  } 
                } 
              } 
              this.mCachedViews.add(j, illegalArgumentException);
              i = 1;
            } else {
              i = 0;
            } 
            bool3 = i;
            if (i == 0) {
              addViewHolderToRecycledViewPool((RecyclerView.ViewHolder)illegalArgumentException, true);
              bool = true;
              bool3 = i;
            } 
          } else {
            bool3 = false;
          } 
          RecyclerView.this.mViewInfoStore.removeViewHolder((RecyclerView.ViewHolder)illegalArgumentException);
          if (!bool3 && !bool && bool2)
            ((RecyclerView.ViewHolder)illegalArgumentException).mOwnerRecyclerView = null; 
          return;
        } 
        stringBuilder1 = new StringBuilder();
        stringBuilder1.append("Trying to recycle an ignored view holder. You should first call stopIgnoringView(view) before calling recycle.");
        stringBuilder1.append(RecyclerView.this.exceptionLabel());
        throw new IllegalArgumentException(stringBuilder1.toString());
      } 
      StringBuilder stringBuilder2 = new StringBuilder();
      stringBuilder2.append("Tmp detached view should be removed from RecyclerView before it can be recycled: ");
      stringBuilder2.append(stringBuilder1);
      stringBuilder2.append(RecyclerView.this.exceptionLabel());
      throw new IllegalArgumentException(stringBuilder2.toString());
    }
    
    void scrapView(View param1View) {
      StringBuilder stringBuilder;
      RecyclerView.ViewHolder viewHolder = RecyclerView.getChildViewHolderInt(param1View);
      if (viewHolder.hasAnyOfTheFlags(12) || !viewHolder.isUpdated() || RecyclerView.this.canReuseUpdatedViewHolder(viewHolder)) {
        if (!viewHolder.isInvalid() || viewHolder.isRemoved() || RecyclerView.this.mAdapter.hasStableIds()) {
          viewHolder.setScrapContainer(this, false);
          this.mAttachedScrap.add(viewHolder);
          return;
        } 
        stringBuilder = new StringBuilder();
        stringBuilder.append("Called scrap view with an invalid view. Invalid views cannot be reused from scrap, they should rebound from recycler pool.");
        stringBuilder.append(RecyclerView.this.exceptionLabel());
        throw new IllegalArgumentException(stringBuilder.toString());
      } 
      if (this.mChangedScrap == null)
        this.mChangedScrap = new ArrayList<RecyclerView.ViewHolder>(); 
      stringBuilder.setScrapContainer(this, true);
      this.mChangedScrap.add(stringBuilder);
    }
    
    void setRecycledViewPool(RecyclerView.RecycledViewPool param1RecycledViewPool) {
      RecyclerView.RecycledViewPool recycledViewPool = this.mRecyclerPool;
      if (recycledViewPool != null)
        recycledViewPool.detach(); 
      this.mRecyclerPool = param1RecycledViewPool;
      if (this.mRecyclerPool != null && RecyclerView.this.getAdapter() != null)
        this.mRecyclerPool.attach(); 
    }
    
    void setViewCacheExtension(RecyclerView.ViewCacheExtension param1ViewCacheExtension) {
      this.mViewCacheExtension = param1ViewCacheExtension;
    }
    
    public void setViewCacheSize(int param1Int) {
      this.mRequestedCacheMax = param1Int;
      updateViewCacheSize();
    }
    
    RecyclerView.ViewHolder tryGetViewHolderForPositionByDeadline(int param1Int, boolean param1Boolean, long param1Long) {
      // Byte code:
      //   0: iload_1
      //   1: iflt -> 1058
      //   4: iload_1
      //   5: aload_0
      //   6: getfield this$0 : Landroidx/recyclerview/widget/RecyclerView;
      //   9: getfield mState : Landroidx/recyclerview/widget/RecyclerView$State;
      //   12: invokevirtual getItemCount : ()I
      //   15: if_icmpge -> 1058
      //   18: aload_0
      //   19: getfield this$0 : Landroidx/recyclerview/widget/RecyclerView;
      //   22: getfield mState : Landroidx/recyclerview/widget/RecyclerView$State;
      //   25: invokevirtual isPreLayout : ()Z
      //   28: istore #5
      //   30: iconst_1
      //   31: istore #6
      //   33: iload #5
      //   35: ifeq -> 64
      //   38: aload_0
      //   39: iload_1
      //   40: invokevirtual getChangedScrapViewForPosition : (I)Landroidx/recyclerview/widget/RecyclerView$ViewHolder;
      //   43: astore #7
      //   45: aload #7
      //   47: astore #8
      //   49: aload #7
      //   51: ifnull -> 67
      //   54: iconst_1
      //   55: istore #9
      //   57: aload #7
      //   59: astore #8
      //   61: goto -> 70
      //   64: aconst_null
      //   65: astore #8
      //   67: iconst_0
      //   68: istore #9
      //   70: aload #8
      //   72: astore #7
      //   74: iload #9
      //   76: istore #10
      //   78: aload #8
      //   80: ifnonnull -> 188
      //   83: aload_0
      //   84: iload_1
      //   85: iload_2
      //   86: invokevirtual getScrapOrHiddenOrCachedHolderForPosition : (IZ)Landroidx/recyclerview/widget/RecyclerView$ViewHolder;
      //   89: astore #8
      //   91: aload #8
      //   93: astore #7
      //   95: iload #9
      //   97: istore #10
      //   99: aload #8
      //   101: ifnull -> 188
      //   104: aload_0
      //   105: aload #8
      //   107: invokevirtual validateViewHolderForOffsetPosition : (Landroidx/recyclerview/widget/RecyclerView$ViewHolder;)Z
      //   110: ifne -> 181
      //   113: iload_2
      //   114: ifne -> 171
      //   117: aload #8
      //   119: iconst_4
      //   120: invokevirtual addFlags : (I)V
      //   123: aload #8
      //   125: invokevirtual isScrap : ()Z
      //   128: ifeq -> 152
      //   131: aload_0
      //   132: getfield this$0 : Landroidx/recyclerview/widget/RecyclerView;
      //   135: aload #8
      //   137: getfield itemView : Landroid/view/View;
      //   140: iconst_0
      //   141: invokevirtual removeDetachedView : (Landroid/view/View;Z)V
      //   144: aload #8
      //   146: invokevirtual unScrap : ()V
      //   149: goto -> 165
      //   152: aload #8
      //   154: invokevirtual wasReturnedFromScrap : ()Z
      //   157: ifeq -> 165
      //   160: aload #8
      //   162: invokevirtual clearReturnedFromScrapFlag : ()V
      //   165: aload_0
      //   166: aload #8
      //   168: invokevirtual recycleViewHolderInternal : (Landroidx/recyclerview/widget/RecyclerView$ViewHolder;)V
      //   171: aconst_null
      //   172: astore #7
      //   174: iload #9
      //   176: istore #10
      //   178: goto -> 188
      //   181: iconst_1
      //   182: istore #10
      //   184: aload #8
      //   186: astore #7
      //   188: aload #7
      //   190: astore #11
      //   192: iload #10
      //   194: istore #12
      //   196: aload #7
      //   198: ifnonnull -> 748
      //   201: aload_0
      //   202: getfield this$0 : Landroidx/recyclerview/widget/RecyclerView;
      //   205: getfield mAdapterHelper : Landroidx/recyclerview/widget/AdapterHelper;
      //   208: iload_1
      //   209: invokevirtual findPositionOffset : (I)I
      //   212: istore #12
      //   214: iload #12
      //   216: iflt -> 656
      //   219: iload #12
      //   221: aload_0
      //   222: getfield this$0 : Landroidx/recyclerview/widget/RecyclerView;
      //   225: getfield mAdapter : Landroidx/recyclerview/widget/RecyclerView$Adapter;
      //   228: invokevirtual getItemCount : ()I
      //   231: if_icmpge -> 656
      //   234: aload_0
      //   235: getfield this$0 : Landroidx/recyclerview/widget/RecyclerView;
      //   238: getfield mAdapter : Landroidx/recyclerview/widget/RecyclerView$Adapter;
      //   241: iload #12
      //   243: invokevirtual getItemViewType : (I)I
      //   246: istore #13
      //   248: aload #7
      //   250: astore #8
      //   252: iload #10
      //   254: istore #9
      //   256: aload_0
      //   257: getfield this$0 : Landroidx/recyclerview/widget/RecyclerView;
      //   260: getfield mAdapter : Landroidx/recyclerview/widget/RecyclerView$Adapter;
      //   263: invokevirtual hasStableIds : ()Z
      //   266: ifeq -> 317
      //   269: aload_0
      //   270: aload_0
      //   271: getfield this$0 : Landroidx/recyclerview/widget/RecyclerView;
      //   274: getfield mAdapter : Landroidx/recyclerview/widget/RecyclerView$Adapter;
      //   277: iload #12
      //   279: invokevirtual getItemId : (I)J
      //   282: iload #13
      //   284: iload_2
      //   285: invokevirtual getScrapOrCachedViewForId : (JIZ)Landroidx/recyclerview/widget/RecyclerView$ViewHolder;
      //   288: astore #7
      //   290: aload #7
      //   292: astore #8
      //   294: iload #10
      //   296: istore #9
      //   298: aload #7
      //   300: ifnull -> 317
      //   303: aload #7
      //   305: iload #12
      //   307: putfield mPosition : I
      //   310: iconst_1
      //   311: istore #9
      //   313: aload #7
      //   315: astore #8
      //   317: aload #8
      //   319: astore #7
      //   321: aload #8
      //   323: ifnonnull -> 476
      //   326: aload_0
      //   327: getfield mViewCacheExtension : Landroidx/recyclerview/widget/RecyclerView$ViewCacheExtension;
      //   330: astore #11
      //   332: aload #8
      //   334: astore #7
      //   336: aload #11
      //   338: ifnull -> 476
      //   341: aload #11
      //   343: aload_0
      //   344: iload_1
      //   345: iload #13
      //   347: invokevirtual getViewForPositionAndType : (Landroidx/recyclerview/widget/RecyclerView$Recycler;II)Landroid/view/View;
      //   350: astore #11
      //   352: aload #8
      //   354: astore #7
      //   356: aload #11
      //   358: ifnull -> 476
      //   361: aload_0
      //   362: getfield this$0 : Landroidx/recyclerview/widget/RecyclerView;
      //   365: aload #11
      //   367: invokevirtual getChildViewHolder : (Landroid/view/View;)Landroidx/recyclerview/widget/RecyclerView$ViewHolder;
      //   370: astore #7
      //   372: aload #7
      //   374: ifnull -> 432
      //   377: aload #7
      //   379: invokevirtual shouldIgnore : ()Z
      //   382: ifne -> 388
      //   385: goto -> 476
      //   388: new java/lang/StringBuilder
      //   391: dup
      //   392: invokespecial <init> : ()V
      //   395: astore #7
      //   397: aload #7
      //   399: ldc_w 'getViewForPositionAndType returned a view that is ignored. You must call stopIgnoring before returning this view.'
      //   402: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   405: pop
      //   406: aload #7
      //   408: aload_0
      //   409: getfield this$0 : Landroidx/recyclerview/widget/RecyclerView;
      //   412: invokevirtual exceptionLabel : ()Ljava/lang/String;
      //   415: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   418: pop
      //   419: new java/lang/IllegalArgumentException
      //   422: dup
      //   423: aload #7
      //   425: invokevirtual toString : ()Ljava/lang/String;
      //   428: invokespecial <init> : (Ljava/lang/String;)V
      //   431: athrow
      //   432: new java/lang/StringBuilder
      //   435: dup
      //   436: invokespecial <init> : ()V
      //   439: astore #7
      //   441: aload #7
      //   443: ldc_w 'getViewForPositionAndType returned a view which does not have a ViewHolder'
      //   446: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   449: pop
      //   450: aload #7
      //   452: aload_0
      //   453: getfield this$0 : Landroidx/recyclerview/widget/RecyclerView;
      //   456: invokevirtual exceptionLabel : ()Ljava/lang/String;
      //   459: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   462: pop
      //   463: new java/lang/IllegalArgumentException
      //   466: dup
      //   467: aload #7
      //   469: invokevirtual toString : ()Ljava/lang/String;
      //   472: invokespecial <init> : (Ljava/lang/String;)V
      //   475: athrow
      //   476: aload #7
      //   478: astore #8
      //   480: aload #7
      //   482: ifnonnull -> 530
      //   485: aload_0
      //   486: invokevirtual getRecycledViewPool : ()Landroidx/recyclerview/widget/RecyclerView$RecycledViewPool;
      //   489: iload #13
      //   491: invokevirtual getRecycledView : (I)Landroidx/recyclerview/widget/RecyclerView$ViewHolder;
      //   494: astore #7
      //   496: aload #7
      //   498: astore #8
      //   500: aload #7
      //   502: ifnull -> 530
      //   505: aload #7
      //   507: invokevirtual resetInternal : ()V
      //   510: aload #7
      //   512: astore #8
      //   514: getstatic androidx/recyclerview/widget/RecyclerView.FORCE_INVALIDATE_DISPLAY_LIST : Z
      //   517: ifeq -> 530
      //   520: aload_0
      //   521: aload #7
      //   523: invokespecial invalidateDisplayListInt : (Landroidx/recyclerview/widget/RecyclerView$ViewHolder;)V
      //   526: aload #7
      //   528: astore #8
      //   530: aload #8
      //   532: astore #11
      //   534: iload #9
      //   536: istore #12
      //   538: aload #8
      //   540: ifnonnull -> 748
      //   543: aload_0
      //   544: getfield this$0 : Landroidx/recyclerview/widget/RecyclerView;
      //   547: invokevirtual getNanoTime : ()J
      //   550: lstore #14
      //   552: lload_3
      //   553: ldc2_w 9223372036854775807
      //   556: lcmp
      //   557: ifeq -> 577
      //   560: aload_0
      //   561: getfield mRecyclerPool : Landroidx/recyclerview/widget/RecyclerView$RecycledViewPool;
      //   564: iload #13
      //   566: lload #14
      //   568: lload_3
      //   569: invokevirtual willCreateInTime : (IJJ)Z
      //   572: ifne -> 577
      //   575: aconst_null
      //   576: areturn
      //   577: aload_0
      //   578: getfield this$0 : Landroidx/recyclerview/widget/RecyclerView;
      //   581: getfield mAdapter : Landroidx/recyclerview/widget/RecyclerView$Adapter;
      //   584: aload_0
      //   585: getfield this$0 : Landroidx/recyclerview/widget/RecyclerView;
      //   588: iload #13
      //   590: invokevirtual createViewHolder : (Landroid/view/ViewGroup;I)Landroidx/recyclerview/widget/RecyclerView$ViewHolder;
      //   593: astore #8
      //   595: getstatic androidx/recyclerview/widget/RecyclerView.ALLOW_THREAD_GAP_WORK : Z
      //   598: ifeq -> 630
      //   601: aload #8
      //   603: getfield itemView : Landroid/view/View;
      //   606: invokestatic findNestedRecyclerView : (Landroid/view/View;)Landroidx/recyclerview/widget/RecyclerView;
      //   609: astore #7
      //   611: aload #7
      //   613: ifnull -> 630
      //   616: aload #8
      //   618: new java/lang/ref/WeakReference
      //   621: dup
      //   622: aload #7
      //   624: invokespecial <init> : (Ljava/lang/Object;)V
      //   627: putfield mNestedRecyclerView : Ljava/lang/ref/WeakReference;
      //   630: aload_0
      //   631: getfield this$0 : Landroidx/recyclerview/widget/RecyclerView;
      //   634: invokevirtual getNanoTime : ()J
      //   637: lstore #16
      //   639: aload_0
      //   640: getfield mRecyclerPool : Landroidx/recyclerview/widget/RecyclerView$RecycledViewPool;
      //   643: iload #13
      //   645: lload #16
      //   647: lload #14
      //   649: lsub
      //   650: invokevirtual factorInCreateTime : (IJ)V
      //   653: goto -> 756
      //   656: new java/lang/StringBuilder
      //   659: dup
      //   660: invokespecial <init> : ()V
      //   663: astore #7
      //   665: aload #7
      //   667: ldc 'Inconsistency detected. Invalid item position '
      //   669: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   672: pop
      //   673: aload #7
      //   675: iload_1
      //   676: invokevirtual append : (I)Ljava/lang/StringBuilder;
      //   679: pop
      //   680: aload #7
      //   682: ldc_w '(offset:'
      //   685: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   688: pop
      //   689: aload #7
      //   691: iload #12
      //   693: invokevirtual append : (I)Ljava/lang/StringBuilder;
      //   696: pop
      //   697: aload #7
      //   699: ldc_w ').state:'
      //   702: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   705: pop
      //   706: aload #7
      //   708: aload_0
      //   709: getfield this$0 : Landroidx/recyclerview/widget/RecyclerView;
      //   712: getfield mState : Landroidx/recyclerview/widget/RecyclerView$State;
      //   715: invokevirtual getItemCount : ()I
      //   718: invokevirtual append : (I)Ljava/lang/StringBuilder;
      //   721: pop
      //   722: aload #7
      //   724: aload_0
      //   725: getfield this$0 : Landroidx/recyclerview/widget/RecyclerView;
      //   728: invokevirtual exceptionLabel : ()Ljava/lang/String;
      //   731: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   734: pop
      //   735: new java/lang/IndexOutOfBoundsException
      //   738: dup
      //   739: aload #7
      //   741: invokevirtual toString : ()Ljava/lang/String;
      //   744: invokespecial <init> : (Ljava/lang/String;)V
      //   747: athrow
      //   748: aload #11
      //   750: astore #8
      //   752: iload #12
      //   754: istore #9
      //   756: iload #9
      //   758: ifeq -> 857
      //   761: aload_0
      //   762: getfield this$0 : Landroidx/recyclerview/widget/RecyclerView;
      //   765: getfield mState : Landroidx/recyclerview/widget/RecyclerView$State;
      //   768: invokevirtual isPreLayout : ()Z
      //   771: ifne -> 857
      //   774: aload #8
      //   776: sipush #8192
      //   779: invokevirtual hasAnyOfTheFlags : (I)Z
      //   782: ifeq -> 857
      //   785: aload #8
      //   787: iconst_0
      //   788: sipush #8192
      //   791: invokevirtual setFlags : (II)V
      //   794: aload_0
      //   795: getfield this$0 : Landroidx/recyclerview/widget/RecyclerView;
      //   798: getfield mState : Landroidx/recyclerview/widget/RecyclerView$State;
      //   801: getfield mRunSimpleAnimations : Z
      //   804: ifeq -> 857
      //   807: aload #8
      //   809: invokestatic buildAdapterChangeFlagsForAnimations : (Landroidx/recyclerview/widget/RecyclerView$ViewHolder;)I
      //   812: istore #10
      //   814: aload_0
      //   815: getfield this$0 : Landroidx/recyclerview/widget/RecyclerView;
      //   818: getfield mItemAnimator : Landroidx/recyclerview/widget/RecyclerView$ItemAnimator;
      //   821: aload_0
      //   822: getfield this$0 : Landroidx/recyclerview/widget/RecyclerView;
      //   825: getfield mState : Landroidx/recyclerview/widget/RecyclerView$State;
      //   828: aload #8
      //   830: iload #10
      //   832: sipush #4096
      //   835: ior
      //   836: aload #8
      //   838: invokevirtual getUnmodifiedPayloads : ()Ljava/util/List;
      //   841: invokevirtual recordPreLayoutInformation : (Landroidx/recyclerview/widget/RecyclerView$State;Landroidx/recyclerview/widget/RecyclerView$ViewHolder;ILjava/util/List;)Landroidx/recyclerview/widget/RecyclerView$ItemAnimator$ItemHolderInfo;
      //   844: astore #7
      //   846: aload_0
      //   847: getfield this$0 : Landroidx/recyclerview/widget/RecyclerView;
      //   850: aload #8
      //   852: aload #7
      //   854: invokevirtual recordAnimationInfoIfBouncedHiddenView : (Landroidx/recyclerview/widget/RecyclerView$ViewHolder;Landroidx/recyclerview/widget/RecyclerView$ItemAnimator$ItemHolderInfo;)V
      //   857: aload_0
      //   858: getfield this$0 : Landroidx/recyclerview/widget/RecyclerView;
      //   861: getfield mState : Landroidx/recyclerview/widget/RecyclerView$State;
      //   864: invokevirtual isPreLayout : ()Z
      //   867: ifeq -> 887
      //   870: aload #8
      //   872: invokevirtual isBound : ()Z
      //   875: ifeq -> 887
      //   878: aload #8
      //   880: iload_1
      //   881: putfield mPreLayoutPosition : I
      //   884: goto -> 914
      //   887: aload #8
      //   889: invokevirtual isBound : ()Z
      //   892: ifeq -> 919
      //   895: aload #8
      //   897: invokevirtual needsUpdate : ()Z
      //   900: ifne -> 919
      //   903: aload #8
      //   905: invokevirtual isInvalid : ()Z
      //   908: ifeq -> 914
      //   911: goto -> 919
      //   914: iconst_0
      //   915: istore_2
      //   916: goto -> 939
      //   919: aload_0
      //   920: aload #8
      //   922: aload_0
      //   923: getfield this$0 : Landroidx/recyclerview/widget/RecyclerView;
      //   926: getfield mAdapterHelper : Landroidx/recyclerview/widget/AdapterHelper;
      //   929: iload_1
      //   930: invokevirtual findPositionOffset : (I)I
      //   933: iload_1
      //   934: lload_3
      //   935: invokespecial tryBindViewHolderByDeadline : (Landroidx/recyclerview/widget/RecyclerView$ViewHolder;IIJ)Z
      //   938: istore_2
      //   939: aload #8
      //   941: getfield itemView : Landroid/view/View;
      //   944: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
      //   947: astore #7
      //   949: aload #7
      //   951: ifnonnull -> 979
      //   954: aload_0
      //   955: getfield this$0 : Landroidx/recyclerview/widget/RecyclerView;
      //   958: invokevirtual generateDefaultLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
      //   961: checkcast androidx/recyclerview/widget/RecyclerView$LayoutParams
      //   964: astore #7
      //   966: aload #8
      //   968: getfield itemView : Landroid/view/View;
      //   971: aload #7
      //   973: invokevirtual setLayoutParams : (Landroid/view/ViewGroup$LayoutParams;)V
      //   976: goto -> 1025
      //   979: aload_0
      //   980: getfield this$0 : Landroidx/recyclerview/widget/RecyclerView;
      //   983: aload #7
      //   985: invokevirtual checkLayoutParams : (Landroid/view/ViewGroup$LayoutParams;)Z
      //   988: ifne -> 1018
      //   991: aload_0
      //   992: getfield this$0 : Landroidx/recyclerview/widget/RecyclerView;
      //   995: aload #7
      //   997: invokevirtual generateLayoutParams : (Landroid/view/ViewGroup$LayoutParams;)Landroid/view/ViewGroup$LayoutParams;
      //   1000: checkcast androidx/recyclerview/widget/RecyclerView$LayoutParams
      //   1003: astore #7
      //   1005: aload #8
      //   1007: getfield itemView : Landroid/view/View;
      //   1010: aload #7
      //   1012: invokevirtual setLayoutParams : (Landroid/view/ViewGroup$LayoutParams;)V
      //   1015: goto -> 1025
      //   1018: aload #7
      //   1020: checkcast androidx/recyclerview/widget/RecyclerView$LayoutParams
      //   1023: astore #7
      //   1025: aload #7
      //   1027: aload #8
      //   1029: putfield mViewHolder : Landroidx/recyclerview/widget/RecyclerView$ViewHolder;
      //   1032: iload #9
      //   1034: ifeq -> 1047
      //   1037: iload_2
      //   1038: ifeq -> 1047
      //   1041: iload #6
      //   1043: istore_2
      //   1044: goto -> 1049
      //   1047: iconst_0
      //   1048: istore_2
      //   1049: aload #7
      //   1051: iload_2
      //   1052: putfield mPendingInvalidate : Z
      //   1055: aload #8
      //   1057: areturn
      //   1058: new java/lang/StringBuilder
      //   1061: dup
      //   1062: invokespecial <init> : ()V
      //   1065: astore #7
      //   1067: aload #7
      //   1069: ldc_w 'Invalid item position '
      //   1072: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   1075: pop
      //   1076: aload #7
      //   1078: iload_1
      //   1079: invokevirtual append : (I)Ljava/lang/StringBuilder;
      //   1082: pop
      //   1083: aload #7
      //   1085: ldc_w '('
      //   1088: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   1091: pop
      //   1092: aload #7
      //   1094: iload_1
      //   1095: invokevirtual append : (I)Ljava/lang/StringBuilder;
      //   1098: pop
      //   1099: aload #7
      //   1101: ldc_w '). Item count:'
      //   1104: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   1107: pop
      //   1108: aload #7
      //   1110: aload_0
      //   1111: getfield this$0 : Landroidx/recyclerview/widget/RecyclerView;
      //   1114: getfield mState : Landroidx/recyclerview/widget/RecyclerView$State;
      //   1117: invokevirtual getItemCount : ()I
      //   1120: invokevirtual append : (I)Ljava/lang/StringBuilder;
      //   1123: pop
      //   1124: aload #7
      //   1126: aload_0
      //   1127: getfield this$0 : Landroidx/recyclerview/widget/RecyclerView;
      //   1130: invokevirtual exceptionLabel : ()Ljava/lang/String;
      //   1133: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   1136: pop
      //   1137: new java/lang/IndexOutOfBoundsException
      //   1140: dup
      //   1141: aload #7
      //   1143: invokevirtual toString : ()Ljava/lang/String;
      //   1146: invokespecial <init> : (Ljava/lang/String;)V
      //   1149: athrow
    }
    
    void unscrapView(RecyclerView.ViewHolder param1ViewHolder) {
      if (param1ViewHolder.mInChangeScrap) {
        this.mChangedScrap.remove(param1ViewHolder);
      } else {
        this.mAttachedScrap.remove(param1ViewHolder);
      } 
      param1ViewHolder.mScrapContainer = null;
      param1ViewHolder.mInChangeScrap = false;
      param1ViewHolder.clearReturnedFromScrapFlag();
    }
    
    void updateViewCacheSize() {
      if (RecyclerView.this.mLayout != null) {
        i = RecyclerView.this.mLayout.mPrefetchMaxCountObserved;
      } else {
        i = 0;
      } 
      this.mViewCacheMax = this.mRequestedCacheMax + i;
      for (int i = this.mCachedViews.size() - 1; i >= 0 && this.mCachedViews.size() > this.mViewCacheMax; i--)
        recycleCachedViewAt(i); 
    }
    
    boolean validateViewHolderForOffsetPosition(RecyclerView.ViewHolder param1ViewHolder) {
      if (param1ViewHolder.isRemoved())
        return RecyclerView.this.mState.isPreLayout(); 
      if (param1ViewHolder.mPosition >= 0 && param1ViewHolder.mPosition < RecyclerView.this.mAdapter.getItemCount()) {
        boolean bool = RecyclerView.this.mState.isPreLayout();
        boolean bool1 = false;
        if (!bool && RecyclerView.this.mAdapter.getItemViewType(param1ViewHolder.mPosition) != param1ViewHolder.getItemViewType())
          return false; 
        if (RecyclerView.this.mAdapter.hasStableIds()) {
          if (param1ViewHolder.getItemId() == RecyclerView.this.mAdapter.getItemId(param1ViewHolder.mPosition))
            bool1 = true; 
          return bool1;
        } 
        return true;
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Inconsistency detected. Invalid view holder adapter position");
      stringBuilder.append(param1ViewHolder);
      stringBuilder.append(RecyclerView.this.exceptionLabel());
      throw new IndexOutOfBoundsException(stringBuilder.toString());
    }
    
    void viewRangeUpdate(int param1Int1, int param1Int2) {
      for (int i = this.mCachedViews.size() - 1; i >= 0; i--) {
        RecyclerView.ViewHolder viewHolder = this.mCachedViews.get(i);
        if (viewHolder != null) {
          int j = viewHolder.mPosition;
          if (j >= param1Int1 && j < param1Int2 + param1Int1) {
            viewHolder.addFlags(2);
            recycleCachedViewAt(i);
          } 
        } 
      } 
    }
  }
  
  public static interface RecyclerListener {
    void onViewRecycled(RecyclerView.ViewHolder param1ViewHolder);
  }
  
  private class RecyclerViewDataObserver extends AdapterDataObserver {
    public void onChanged() {
      RecyclerView.this.assertNotInLayoutOrScroll((String)null);
      RecyclerView.this.mState.mStructureChanged = true;
      RecyclerView.this.processDataSetCompletelyChanged(true);
      if (!RecyclerView.this.mAdapterHelper.hasPendingUpdates())
        RecyclerView.this.requestLayout(); 
    }
    
    public void onItemRangeChanged(int param1Int1, int param1Int2, Object param1Object) {
      RecyclerView.this.assertNotInLayoutOrScroll((String)null);
      if (RecyclerView.this.mAdapterHelper.onItemRangeChanged(param1Int1, param1Int2, param1Object))
        triggerUpdateProcessor(); 
    }
    
    public void onItemRangeInserted(int param1Int1, int param1Int2) {
      RecyclerView.this.assertNotInLayoutOrScroll((String)null);
      if (RecyclerView.this.mAdapterHelper.onItemRangeInserted(param1Int1, param1Int2))
        triggerUpdateProcessor(); 
    }
    
    public void onItemRangeMoved(int param1Int1, int param1Int2, int param1Int3) {
      RecyclerView.this.assertNotInLayoutOrScroll((String)null);
      if (RecyclerView.this.mAdapterHelper.onItemRangeMoved(param1Int1, param1Int2, param1Int3))
        triggerUpdateProcessor(); 
    }
    
    public void onItemRangeRemoved(int param1Int1, int param1Int2) {
      RecyclerView.this.assertNotInLayoutOrScroll((String)null);
      if (RecyclerView.this.mAdapterHelper.onItemRangeRemoved(param1Int1, param1Int2))
        triggerUpdateProcessor(); 
    }
    
    void triggerUpdateProcessor() {
      if (RecyclerView.POST_UPDATES_ON_ANIMATION && RecyclerView.this.mHasFixedSize && RecyclerView.this.mIsAttached) {
        RecyclerView recyclerView = RecyclerView.this;
        ViewCompat.postOnAnimation((View)recyclerView, recyclerView.mUpdateChildViewsRunnable);
      } else {
        RecyclerView recyclerView = RecyclerView.this;
        recyclerView.mAdapterUpdateDuringMeasure = true;
        recyclerView.requestLayout();
      } 
    }
  }
  
  public static class SavedState extends AbsSavedState {
    public static final Parcelable.Creator<SavedState> CREATOR = (Parcelable.Creator<SavedState>)new Parcelable.ClassLoaderCreator<SavedState>() {
        public RecyclerView.SavedState createFromParcel(Parcel param2Parcel) {
          return new RecyclerView.SavedState(param2Parcel, null);
        }
        
        public RecyclerView.SavedState createFromParcel(Parcel param2Parcel, ClassLoader param2ClassLoader) {
          return new RecyclerView.SavedState(param2Parcel, param2ClassLoader);
        }
        
        public RecyclerView.SavedState[] newArray(int param2Int) {
          return new RecyclerView.SavedState[param2Int];
        }
      };
    
    Parcelable mLayoutState;
    
    SavedState(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      super(param1Parcel, param1ClassLoader);
      if (param1ClassLoader == null)
        param1ClassLoader = RecyclerView.LayoutManager.class.getClassLoader(); 
      this.mLayoutState = param1Parcel.readParcelable(param1ClassLoader);
    }
    
    SavedState(Parcelable param1Parcelable) {
      super(param1Parcelable);
    }
    
    void copyFrom(SavedState param1SavedState) {
      this.mLayoutState = param1SavedState.mLayoutState;
    }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) {
      super.writeToParcel(param1Parcel, param1Int);
      param1Parcel.writeParcelable(this.mLayoutState, 0);
    }
  }
  
  static final class null implements Parcelable.ClassLoaderCreator<SavedState> {
    public RecyclerView.SavedState createFromParcel(Parcel param1Parcel) {
      return new RecyclerView.SavedState(param1Parcel, null);
    }
    
    public RecyclerView.SavedState createFromParcel(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      return new RecyclerView.SavedState(param1Parcel, param1ClassLoader);
    }
    
    public RecyclerView.SavedState[] newArray(int param1Int) {
      return new RecyclerView.SavedState[param1Int];
    }
  }
  
  public static class SimpleOnItemTouchListener implements OnItemTouchListener {
    public boolean onInterceptTouchEvent(RecyclerView param1RecyclerView, MotionEvent param1MotionEvent) {
      return false;
    }
    
    public void onRequestDisallowInterceptTouchEvent(boolean param1Boolean) {}
    
    public void onTouchEvent(RecyclerView param1RecyclerView, MotionEvent param1MotionEvent) {}
  }
  
  public static abstract class SmoothScroller {
    private RecyclerView.LayoutManager mLayoutManager;
    
    private boolean mPendingInitialRun;
    
    private RecyclerView mRecyclerView;
    
    private final Action mRecyclingAction = new Action(0, 0);
    
    private boolean mRunning;
    
    private boolean mStarted;
    
    private int mTargetPosition = -1;
    
    private View mTargetView;
    
    public PointF computeScrollVectorForPosition(int param1Int) {
      RecyclerView.LayoutManager layoutManager = getLayoutManager();
      if (layoutManager instanceof ScrollVectorProvider)
        return ((ScrollVectorProvider)layoutManager).computeScrollVectorForPosition(param1Int); 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("You should override computeScrollVectorForPosition when the LayoutManager does not implement ");
      stringBuilder.append(ScrollVectorProvider.class.getCanonicalName());
      Log.w("RecyclerView", stringBuilder.toString());
      return null;
    }
    
    public View findViewByPosition(int param1Int) {
      return this.mRecyclerView.mLayout.findViewByPosition(param1Int);
    }
    
    public int getChildCount() {
      return this.mRecyclerView.mLayout.getChildCount();
    }
    
    public int getChildPosition(View param1View) {
      return this.mRecyclerView.getChildLayoutPosition(param1View);
    }
    
    public RecyclerView.LayoutManager getLayoutManager() {
      return this.mLayoutManager;
    }
    
    public int getTargetPosition() {
      return this.mTargetPosition;
    }
    
    @Deprecated
    public void instantScrollToPosition(int param1Int) {
      this.mRecyclerView.scrollToPosition(param1Int);
    }
    
    public boolean isPendingInitialRun() {
      return this.mPendingInitialRun;
    }
    
    public boolean isRunning() {
      return this.mRunning;
    }
    
    protected void normalize(PointF param1PointF) {
      float f = (float)Math.sqrt((param1PointF.x * param1PointF.x + param1PointF.y * param1PointF.y));
      param1PointF.x /= f;
      param1PointF.y /= f;
    }
    
    void onAnimation(int param1Int1, int param1Int2) {
      RecyclerView recyclerView = this.mRecyclerView;
      if (this.mTargetPosition == -1 || recyclerView == null)
        stop(); 
      if (this.mPendingInitialRun && this.mTargetView == null && this.mLayoutManager != null) {
        PointF pointF = computeScrollVectorForPosition(this.mTargetPosition);
        if (pointF != null && (pointF.x != 0.0F || pointF.y != 0.0F))
          recyclerView.scrollStep((int)Math.signum(pointF.x), (int)Math.signum(pointF.y), (int[])null); 
      } 
      this.mPendingInitialRun = false;
      View view = this.mTargetView;
      if (view != null)
        if (getChildPosition(view) == this.mTargetPosition) {
          onTargetFound(this.mTargetView, recyclerView.mState, this.mRecyclingAction);
          this.mRecyclingAction.runIfNecessary(recyclerView);
          stop();
        } else {
          Log.e("RecyclerView", "Passed over target position while smooth scrolling.");
          this.mTargetView = null;
        }  
      if (this.mRunning) {
        onSeekTargetStep(param1Int1, param1Int2, recyclerView.mState, this.mRecyclingAction);
        boolean bool = this.mRecyclingAction.hasJumpTarget();
        this.mRecyclingAction.runIfNecessary(recyclerView);
        if (bool && this.mRunning) {
          this.mPendingInitialRun = true;
          recyclerView.mViewFlinger.postOnAnimation();
        } 
      } 
    }
    
    protected void onChildAttachedToWindow(View param1View) {
      if (getChildPosition(param1View) == getTargetPosition())
        this.mTargetView = param1View; 
    }
    
    protected abstract void onSeekTargetStep(int param1Int1, int param1Int2, RecyclerView.State param1State, Action param1Action);
    
    protected abstract void onStart();
    
    protected abstract void onStop();
    
    protected abstract void onTargetFound(View param1View, RecyclerView.State param1State, Action param1Action);
    
    public void setTargetPosition(int param1Int) {
      this.mTargetPosition = param1Int;
    }
    
    void start(RecyclerView param1RecyclerView, RecyclerView.LayoutManager param1LayoutManager) {
      param1RecyclerView.mViewFlinger.stop();
      if (this.mStarted) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("An instance of ");
        stringBuilder.append(getClass().getSimpleName());
        stringBuilder.append(" was started more than once. Each instance of");
        stringBuilder.append(getClass().getSimpleName());
        stringBuilder.append(" is intended to only be used once. You should create a new instance for each use.");
        Log.w("RecyclerView", stringBuilder.toString());
      } 
      this.mRecyclerView = param1RecyclerView;
      this.mLayoutManager = param1LayoutManager;
      if (this.mTargetPosition != -1) {
        this.mRecyclerView.mState.mTargetPosition = this.mTargetPosition;
        this.mRunning = true;
        this.mPendingInitialRun = true;
        this.mTargetView = findViewByPosition(getTargetPosition());
        onStart();
        this.mRecyclerView.mViewFlinger.postOnAnimation();
        this.mStarted = true;
        return;
      } 
      throw new IllegalArgumentException("Invalid target position");
    }
    
    protected final void stop() {
      if (!this.mRunning)
        return; 
      this.mRunning = false;
      onStop();
      this.mRecyclerView.mState.mTargetPosition = -1;
      this.mTargetView = null;
      this.mTargetPosition = -1;
      this.mPendingInitialRun = false;
      this.mLayoutManager.onSmoothScrollerStopped(this);
      this.mLayoutManager = null;
      this.mRecyclerView = null;
    }
    
    public static class Action {
      public static final int UNDEFINED_DURATION = -2147483648;
      
      private boolean mChanged = false;
      
      private int mConsecutiveUpdates = 0;
      
      private int mDuration;
      
      private int mDx;
      
      private int mDy;
      
      private Interpolator mInterpolator;
      
      private int mJumpToPosition = -1;
      
      public Action(int param2Int1, int param2Int2) {
        this(param2Int1, param2Int2, -2147483648, null);
      }
      
      public Action(int param2Int1, int param2Int2, int param2Int3) {
        this(param2Int1, param2Int2, param2Int3, null);
      }
      
      public Action(int param2Int1, int param2Int2, int param2Int3, Interpolator param2Interpolator) {
        this.mDx = param2Int1;
        this.mDy = param2Int2;
        this.mDuration = param2Int3;
        this.mInterpolator = param2Interpolator;
      }
      
      private void validate() {
        if (this.mInterpolator == null || this.mDuration >= 1) {
          if (this.mDuration >= 1)
            return; 
          throw new IllegalStateException("Scroll duration must be a positive number");
        } 
        throw new IllegalStateException("If you provide an interpolator, you must set a positive duration");
      }
      
      public int getDuration() {
        return this.mDuration;
      }
      
      public int getDx() {
        return this.mDx;
      }
      
      public int getDy() {
        return this.mDy;
      }
      
      public Interpolator getInterpolator() {
        return this.mInterpolator;
      }
      
      boolean hasJumpTarget() {
        boolean bool;
        if (this.mJumpToPosition >= 0) {
          bool = true;
        } else {
          bool = false;
        } 
        return bool;
      }
      
      public void jumpTo(int param2Int) {
        this.mJumpToPosition = param2Int;
      }
      
      void runIfNecessary(RecyclerView param2RecyclerView) {
        int i = this.mJumpToPosition;
        if (i >= 0) {
          this.mJumpToPosition = -1;
          param2RecyclerView.jumpToPositionForSmoothScroller(i);
          this.mChanged = false;
          return;
        } 
        if (this.mChanged) {
          validate();
          param2RecyclerView.mViewFlinger.smoothScrollBy(this.mDx, this.mDy, this.mDuration, this.mInterpolator);
          this.mConsecutiveUpdates++;
          if (this.mConsecutiveUpdates > 10)
            Log.e("RecyclerView", "Smooth Scroll action is being updated too frequently. Make sure you are not changing it unless necessary"); 
          this.mChanged = false;
        } else {
          this.mConsecutiveUpdates = 0;
        } 
      }
      
      public void setDuration(int param2Int) {
        this.mChanged = true;
        this.mDuration = param2Int;
      }
      
      public void setDx(int param2Int) {
        this.mChanged = true;
        this.mDx = param2Int;
      }
      
      public void setDy(int param2Int) {
        this.mChanged = true;
        this.mDy = param2Int;
      }
      
      public void setInterpolator(Interpolator param2Interpolator) {
        this.mChanged = true;
        this.mInterpolator = param2Interpolator;
      }
      
      public void update(int param2Int1, int param2Int2, int param2Int3, Interpolator param2Interpolator) {
        this.mDx = param2Int1;
        this.mDy = param2Int2;
        this.mDuration = param2Int3;
        this.mInterpolator = param2Interpolator;
        this.mChanged = true;
      }
    }
    
    public static interface ScrollVectorProvider {
      PointF computeScrollVectorForPosition(int param2Int);
    }
  }
  
  public static class Action {
    public static final int UNDEFINED_DURATION = -2147483648;
    
    private boolean mChanged = false;
    
    private int mConsecutiveUpdates = 0;
    
    private int mDuration;
    
    private int mDx;
    
    private int mDy;
    
    private Interpolator mInterpolator;
    
    private int mJumpToPosition = -1;
    
    public Action(int param1Int1, int param1Int2) {
      this(param1Int1, param1Int2, -2147483648, null);
    }
    
    public Action(int param1Int1, int param1Int2, int param1Int3) {
      this(param1Int1, param1Int2, param1Int3, null);
    }
    
    public Action(int param1Int1, int param1Int2, int param1Int3, Interpolator param1Interpolator) {
      this.mDx = param1Int1;
      this.mDy = param1Int2;
      this.mDuration = param1Int3;
      this.mInterpolator = param1Interpolator;
    }
    
    private void validate() {
      if (this.mInterpolator == null || this.mDuration >= 1) {
        if (this.mDuration >= 1)
          return; 
        throw new IllegalStateException("Scroll duration must be a positive number");
      } 
      throw new IllegalStateException("If you provide an interpolator, you must set a positive duration");
    }
    
    public int getDuration() {
      return this.mDuration;
    }
    
    public int getDx() {
      return this.mDx;
    }
    
    public int getDy() {
      return this.mDy;
    }
    
    public Interpolator getInterpolator() {
      return this.mInterpolator;
    }
    
    boolean hasJumpTarget() {
      boolean bool;
      if (this.mJumpToPosition >= 0) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public void jumpTo(int param1Int) {
      this.mJumpToPosition = param1Int;
    }
    
    void runIfNecessary(RecyclerView param1RecyclerView) {
      int i = this.mJumpToPosition;
      if (i >= 0) {
        this.mJumpToPosition = -1;
        param1RecyclerView.jumpToPositionForSmoothScroller(i);
        this.mChanged = false;
        return;
      } 
      if (this.mChanged) {
        validate();
        param1RecyclerView.mViewFlinger.smoothScrollBy(this.mDx, this.mDy, this.mDuration, this.mInterpolator);
        this.mConsecutiveUpdates++;
        if (this.mConsecutiveUpdates > 10)
          Log.e("RecyclerView", "Smooth Scroll action is being updated too frequently. Make sure you are not changing it unless necessary"); 
        this.mChanged = false;
      } else {
        this.mConsecutiveUpdates = 0;
      } 
    }
    
    public void setDuration(int param1Int) {
      this.mChanged = true;
      this.mDuration = param1Int;
    }
    
    public void setDx(int param1Int) {
      this.mChanged = true;
      this.mDx = param1Int;
    }
    
    public void setDy(int param1Int) {
      this.mChanged = true;
      this.mDy = param1Int;
    }
    
    public void setInterpolator(Interpolator param1Interpolator) {
      this.mChanged = true;
      this.mInterpolator = param1Interpolator;
    }
    
    public void update(int param1Int1, int param1Int2, int param1Int3, Interpolator param1Interpolator) {
      this.mDx = param1Int1;
      this.mDy = param1Int2;
      this.mDuration = param1Int3;
      this.mInterpolator = param1Interpolator;
      this.mChanged = true;
    }
  }
  
  public static interface ScrollVectorProvider {
    PointF computeScrollVectorForPosition(int param1Int);
  }
  
  public static class State {
    static final int STEP_ANIMATIONS = 4;
    
    static final int STEP_LAYOUT = 2;
    
    static final int STEP_START = 1;
    
    private SparseArray<Object> mData;
    
    int mDeletedInvisibleItemCountSincePreviousLayout = 0;
    
    long mFocusedItemId;
    
    int mFocusedItemPosition;
    
    int mFocusedSubChildId;
    
    boolean mInPreLayout = false;
    
    boolean mIsMeasuring = false;
    
    int mItemCount = 0;
    
    int mLayoutStep = 1;
    
    int mPreviousLayoutItemCount = 0;
    
    int mRemainingScrollHorizontal;
    
    int mRemainingScrollVertical;
    
    boolean mRunPredictiveAnimations = false;
    
    boolean mRunSimpleAnimations = false;
    
    boolean mStructureChanged = false;
    
    int mTargetPosition = -1;
    
    boolean mTrackOldChangeHolders = false;
    
    void assertLayoutStep(int param1Int) {
      if ((this.mLayoutStep & param1Int) != 0)
        return; 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Layout state should be one of ");
      stringBuilder.append(Integer.toBinaryString(param1Int));
      stringBuilder.append(" but it is ");
      stringBuilder.append(Integer.toBinaryString(this.mLayoutStep));
      throw new IllegalStateException(stringBuilder.toString());
    }
    
    public boolean didStructureChange() {
      return this.mStructureChanged;
    }
    
    public <T> T get(int param1Int) {
      SparseArray<Object> sparseArray = this.mData;
      return (T)((sparseArray == null) ? null : sparseArray.get(param1Int));
    }
    
    public int getItemCount() {
      int i;
      if (this.mInPreLayout) {
        i = this.mPreviousLayoutItemCount - this.mDeletedInvisibleItemCountSincePreviousLayout;
      } else {
        i = this.mItemCount;
      } 
      return i;
    }
    
    public int getRemainingScrollHorizontal() {
      return this.mRemainingScrollHorizontal;
    }
    
    public int getRemainingScrollVertical() {
      return this.mRemainingScrollVertical;
    }
    
    public int getTargetScrollPosition() {
      return this.mTargetPosition;
    }
    
    public boolean hasTargetScrollPosition() {
      boolean bool;
      if (this.mTargetPosition != -1) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public boolean isMeasuring() {
      return this.mIsMeasuring;
    }
    
    public boolean isPreLayout() {
      return this.mInPreLayout;
    }
    
    void prepareForNestedPrefetch(RecyclerView.Adapter param1Adapter) {
      this.mLayoutStep = 1;
      this.mItemCount = param1Adapter.getItemCount();
      this.mInPreLayout = false;
      this.mTrackOldChangeHolders = false;
      this.mIsMeasuring = false;
    }
    
    public void put(int param1Int, Object param1Object) {
      if (this.mData == null)
        this.mData = new SparseArray(); 
      this.mData.put(param1Int, param1Object);
    }
    
    public void remove(int param1Int) {
      SparseArray<Object> sparseArray = this.mData;
      if (sparseArray == null)
        return; 
      sparseArray.remove(param1Int);
    }
    
    public String toString() {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("State{mTargetPosition=");
      stringBuilder.append(this.mTargetPosition);
      stringBuilder.append(", mData=");
      stringBuilder.append(this.mData);
      stringBuilder.append(", mItemCount=");
      stringBuilder.append(this.mItemCount);
      stringBuilder.append(", mIsMeasuring=");
      stringBuilder.append(this.mIsMeasuring);
      stringBuilder.append(", mPreviousLayoutItemCount=");
      stringBuilder.append(this.mPreviousLayoutItemCount);
      stringBuilder.append(", mDeletedInvisibleItemCountSincePreviousLayout=");
      stringBuilder.append(this.mDeletedInvisibleItemCountSincePreviousLayout);
      stringBuilder.append(", mStructureChanged=");
      stringBuilder.append(this.mStructureChanged);
      stringBuilder.append(", mInPreLayout=");
      stringBuilder.append(this.mInPreLayout);
      stringBuilder.append(", mRunSimpleAnimations=");
      stringBuilder.append(this.mRunSimpleAnimations);
      stringBuilder.append(", mRunPredictiveAnimations=");
      stringBuilder.append(this.mRunPredictiveAnimations);
      stringBuilder.append('}');
      return stringBuilder.toString();
    }
    
    public boolean willRunPredictiveAnimations() {
      return this.mRunPredictiveAnimations;
    }
    
    public boolean willRunSimpleAnimations() {
      return this.mRunSimpleAnimations;
    }
  }
  
  public static abstract class ViewCacheExtension {
    public abstract View getViewForPositionAndType(RecyclerView.Recycler param1Recycler, int param1Int1, int param1Int2);
  }
  
  class ViewFlinger implements Runnable {
    private boolean mEatRunOnAnimationRequest = false;
    
    Interpolator mInterpolator = RecyclerView.sQuinticInterpolator;
    
    private int mLastFlingX;
    
    private int mLastFlingY;
    
    OverScroller mOverScroller = new OverScroller(RecyclerView.this.getContext(), RecyclerView.sQuinticInterpolator);
    
    private boolean mReSchedulePostAnimationCallback = false;
    
    private int computeScrollDuration(int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
      boolean bool;
      int i = Math.abs(param1Int1);
      int j = Math.abs(param1Int2);
      if (i > j) {
        bool = true;
      } else {
        bool = false;
      } 
      param1Int3 = (int)Math.sqrt((param1Int3 * param1Int3 + param1Int4 * param1Int4));
      param1Int2 = (int)Math.sqrt((param1Int1 * param1Int1 + param1Int2 * param1Int2));
      RecyclerView recyclerView = RecyclerView.this;
      if (bool) {
        param1Int1 = recyclerView.getWidth();
      } else {
        param1Int1 = recyclerView.getHeight();
      } 
      param1Int4 = param1Int1 / 2;
      float f1 = param1Int2;
      float f2 = param1Int1;
      float f3 = Math.min(1.0F, f1 * 1.0F / f2);
      f1 = param1Int4;
      f3 = distanceInfluenceForSnapDuration(f3);
      if (param1Int3 > 0) {
        param1Int1 = Math.round(Math.abs((f1 + f3 * f1) / param1Int3) * 1000.0F) * 4;
      } else {
        if (bool) {
          param1Int1 = i;
        } else {
          param1Int1 = j;
        } 
        param1Int1 = (int)((param1Int1 / f2 + 1.0F) * 300.0F);
      } 
      return Math.min(param1Int1, 2000);
    }
    
    private float distanceInfluenceForSnapDuration(float param1Float) {
      return (float)Math.sin(((param1Float - 0.5F) * 0.47123894F));
    }
    
    private void internalPostOnAnimation() {
      RecyclerView.this.removeCallbacks(this);
      ViewCompat.postOnAnimation((View)RecyclerView.this, this);
    }
    
    public void fling(int param1Int1, int param1Int2) {
      RecyclerView.this.setScrollState(2);
      this.mLastFlingY = 0;
      this.mLastFlingX = 0;
      if (this.mInterpolator != RecyclerView.sQuinticInterpolator) {
        this.mInterpolator = RecyclerView.sQuinticInterpolator;
        this.mOverScroller = new OverScroller(RecyclerView.this.getContext(), RecyclerView.sQuinticInterpolator);
      } 
      this.mOverScroller.fling(0, 0, param1Int1, param1Int2, -2147483648, 2147483647, -2147483648, 2147483647);
      postOnAnimation();
    }
    
    void postOnAnimation() {
      if (this.mEatRunOnAnimationRequest) {
        this.mReSchedulePostAnimationCallback = true;
      } else {
        internalPostOnAnimation();
      } 
    }
    
    public void run() {
      if (RecyclerView.this.mLayout == null) {
        stop();
        return;
      } 
      this.mReSchedulePostAnimationCallback = false;
      this.mEatRunOnAnimationRequest = true;
      RecyclerView.this.consumePendingUpdateOperations();
      OverScroller overScroller = this.mOverScroller;
      if (overScroller.computeScrollOffset()) {
        int i = overScroller.getCurrX();
        int j = overScroller.getCurrY();
        int k = i - this.mLastFlingX;
        int m = j - this.mLastFlingY;
        this.mLastFlingX = i;
        this.mLastFlingY = j;
        RecyclerView.this.mReusableIntPair[0] = 0;
        RecyclerView.this.mReusableIntPair[1] = 0;
        RecyclerView recyclerView = RecyclerView.this;
        j = k;
        i = m;
        if (recyclerView.dispatchNestedPreScroll(k, m, recyclerView.mReusableIntPair, (int[])null, 1)) {
          j = k - RecyclerView.this.mReusableIntPair[0];
          i = m - RecyclerView.this.mReusableIntPair[1];
        } 
        if (RecyclerView.this.getOverScrollMode() != 2)
          RecyclerView.this.considerReleasingGlowsOnScroll(j, i); 
        if (RecyclerView.this.mAdapter != null) {
          RecyclerView.this.mReusableIntPair[0] = 0;
          RecyclerView.this.mReusableIntPair[1] = 0;
          recyclerView = RecyclerView.this;
          recyclerView.scrollStep(j, i, recyclerView.mReusableIntPair);
          int i2 = RecyclerView.this.mReusableIntPair[0];
          int i3 = RecyclerView.this.mReusableIntPair[1];
          int i4 = j - i2;
          int i5 = i - i3;
          RecyclerView.SmoothScroller smoothScroller2 = RecyclerView.this.mLayout.mSmoothScroller;
          j = i2;
          k = i4;
          i = i3;
          m = i5;
          if (smoothScroller2 != null) {
            j = i2;
            k = i4;
            i = i3;
            m = i5;
            if (!smoothScroller2.isPendingInitialRun()) {
              j = i2;
              k = i4;
              i = i3;
              m = i5;
              if (smoothScroller2.isRunning()) {
                j = RecyclerView.this.mState.getItemCount();
                if (j == 0) {
                  smoothScroller2.stop();
                  j = i2;
                  k = i4;
                  i = i3;
                  m = i5;
                } else if (smoothScroller2.getTargetPosition() >= j) {
                  smoothScroller2.setTargetPosition(j - 1);
                  smoothScroller2.onAnimation(i2, i3);
                  j = i2;
                  k = i4;
                  i = i3;
                  m = i5;
                } else {
                  smoothScroller2.onAnimation(i2, i3);
                  j = i2;
                  k = i4;
                  i = i3;
                  m = i5;
                } 
              } 
            } 
          } 
        } else {
          boolean bool = false;
          k = 0;
          m = i;
          i = k;
          k = j;
          j = bool;
        } 
        if (!RecyclerView.this.mItemDecorations.isEmpty())
          RecyclerView.this.invalidate(); 
        RecyclerView.this.mReusableIntPair[0] = 0;
        RecyclerView.this.mReusableIntPair[1] = 0;
        recyclerView = RecyclerView.this;
        recyclerView.dispatchNestedScroll(j, i, k, m, (int[])null, 1, recyclerView.mReusableIntPair);
        int n = k - RecyclerView.this.mReusableIntPair[0];
        int i1 = m - RecyclerView.this.mReusableIntPair[1];
        if (j != 0 || i != 0)
          RecyclerView.this.dispatchOnScrolled(j, i); 
        if (!RecyclerView.this.awakenScrollBars())
          RecyclerView.this.invalidate(); 
        if (overScroller.getCurrX() == overScroller.getFinalX()) {
          m = 1;
        } else {
          m = 0;
        } 
        if (overScroller.getCurrY() == overScroller.getFinalY()) {
          k = 1;
        } else {
          k = 0;
        } 
        if (overScroller.isFinished() || ((m != 0 || n != 0) && (k != 0 || i1 != 0))) {
          m = 1;
        } else {
          m = 0;
        } 
        RecyclerView.SmoothScroller smoothScroller1 = RecyclerView.this.mLayout.mSmoothScroller;
        if (smoothScroller1 != null && smoothScroller1.isPendingInitialRun()) {
          k = 1;
        } else {
          k = 0;
        } 
        if (k == 0 && m != 0) {
          if (RecyclerView.this.getOverScrollMode() != 2) {
            i = (int)overScroller.getCurrVelocity();
            if (n < 0) {
              j = -i;
            } else if (n > 0) {
              j = i;
            } else {
              j = 0;
            } 
            if (i1 < 0) {
              i = -i;
            } else if (i1 <= 0) {
              i = 0;
            } 
            RecyclerView.this.absorbGlows(j, i);
          } 
          if (RecyclerView.ALLOW_THREAD_GAP_WORK)
            RecyclerView.this.mPrefetchRegistry.clearPrefetchPositions(); 
        } else {
          postOnAnimation();
          if (RecyclerView.this.mGapWorker != null)
            RecyclerView.this.mGapWorker.postFromTraversal(RecyclerView.this, j, i); 
        } 
      } 
      RecyclerView.SmoothScroller smoothScroller = RecyclerView.this.mLayout.mSmoothScroller;
      if (smoothScroller != null && smoothScroller.isPendingInitialRun())
        smoothScroller.onAnimation(0, 0); 
      this.mEatRunOnAnimationRequest = false;
      if (this.mReSchedulePostAnimationCallback) {
        internalPostOnAnimation();
      } else {
        RecyclerView.this.setScrollState(0);
        RecyclerView.this.stopNestedScroll(1);
      } 
    }
    
    public void smoothScrollBy(int param1Int1, int param1Int2, int param1Int3, Interpolator param1Interpolator) {
      int i = param1Int3;
      if (param1Int3 == Integer.MIN_VALUE)
        i = computeScrollDuration(param1Int1, param1Int2, 0, 0); 
      Interpolator interpolator = param1Interpolator;
      if (param1Interpolator == null)
        interpolator = RecyclerView.sQuinticInterpolator; 
      if (this.mInterpolator != interpolator) {
        this.mInterpolator = interpolator;
        this.mOverScroller = new OverScroller(RecyclerView.this.getContext(), interpolator);
      } 
      this.mLastFlingY = 0;
      this.mLastFlingX = 0;
      RecyclerView.this.setScrollState(2);
      this.mOverScroller.startScroll(0, 0, param1Int1, param1Int2, i);
      if (Build.VERSION.SDK_INT < 23)
        this.mOverScroller.computeScrollOffset(); 
      postOnAnimation();
    }
    
    public void stop() {
      RecyclerView.this.removeCallbacks(this);
      this.mOverScroller.abortAnimation();
    }
  }
  
  public static abstract class ViewHolder {
    static final int FLAG_ADAPTER_FULLUPDATE = 1024;
    
    static final int FLAG_ADAPTER_POSITION_UNKNOWN = 512;
    
    static final int FLAG_APPEARED_IN_PRE_LAYOUT = 4096;
    
    static final int FLAG_BOUNCED_FROM_HIDDEN_LIST = 8192;
    
    static final int FLAG_BOUND = 1;
    
    static final int FLAG_IGNORE = 128;
    
    static final int FLAG_INVALID = 4;
    
    static final int FLAG_MOVED = 2048;
    
    static final int FLAG_NOT_RECYCLABLE = 16;
    
    static final int FLAG_REMOVED = 8;
    
    static final int FLAG_RETURNED_FROM_SCRAP = 32;
    
    static final int FLAG_TMP_DETACHED = 256;
    
    static final int FLAG_UPDATE = 2;
    
    private static final List<Object> FULLUPDATE_PAYLOADS = Collections.emptyList();
    
    static final int PENDING_ACCESSIBILITY_STATE_NOT_SET = -1;
    
    public final View itemView;
    
    int mFlags;
    
    boolean mInChangeScrap = false;
    
    private int mIsRecyclableCount = 0;
    
    long mItemId = -1L;
    
    int mItemViewType = -1;
    
    WeakReference<RecyclerView> mNestedRecyclerView;
    
    int mOldPosition = -1;
    
    RecyclerView mOwnerRecyclerView;
    
    List<Object> mPayloads = null;
    
    int mPendingAccessibilityState = -1;
    
    int mPosition = -1;
    
    int mPreLayoutPosition = -1;
    
    RecyclerView.Recycler mScrapContainer = null;
    
    ViewHolder mShadowedHolder = null;
    
    ViewHolder mShadowingHolder = null;
    
    List<Object> mUnmodifiedPayloads = null;
    
    private int mWasImportantForAccessibilityBeforeHidden = 0;
    
    public ViewHolder(View param1View) {
      if (param1View != null) {
        this.itemView = param1View;
        return;
      } 
      throw new IllegalArgumentException("itemView may not be null");
    }
    
    private void createPayloadsIfNeeded() {
      if (this.mPayloads == null) {
        this.mPayloads = new ArrayList();
        this.mUnmodifiedPayloads = Collections.unmodifiableList(this.mPayloads);
      } 
    }
    
    void addChangePayload(Object param1Object) {
      if (param1Object == null) {
        addFlags(1024);
      } else if ((0x400 & this.mFlags) == 0) {
        createPayloadsIfNeeded();
        this.mPayloads.add(param1Object);
      } 
    }
    
    void addFlags(int param1Int) {
      this.mFlags = param1Int | this.mFlags;
    }
    
    void clearOldPosition() {
      this.mOldPosition = -1;
      this.mPreLayoutPosition = -1;
    }
    
    void clearPayload() {
      List<Object> list = this.mPayloads;
      if (list != null)
        list.clear(); 
      this.mFlags &= 0xFFFFFBFF;
    }
    
    void clearReturnedFromScrapFlag() {
      this.mFlags &= 0xFFFFFFDF;
    }
    
    void clearTmpDetachFlag() {
      this.mFlags &= 0xFFFFFEFF;
    }
    
    boolean doesTransientStatePreventRecycling() {
      boolean bool;
      if ((this.mFlags & 0x10) == 0 && ViewCompat.hasTransientState(this.itemView)) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    void flagRemovedAndOffsetPosition(int param1Int1, int param1Int2, boolean param1Boolean) {
      addFlags(8);
      offsetPosition(param1Int2, param1Boolean);
      this.mPosition = param1Int1;
    }
    
    public final int getAdapterPosition() {
      RecyclerView recyclerView = this.mOwnerRecyclerView;
      return (recyclerView == null) ? -1 : recyclerView.getAdapterPositionFor(this);
    }
    
    public final long getItemId() {
      return this.mItemId;
    }
    
    public final int getItemViewType() {
      return this.mItemViewType;
    }
    
    public final int getLayoutPosition() {
      int i = this.mPreLayoutPosition;
      int j = i;
      if (i == -1)
        j = this.mPosition; 
      return j;
    }
    
    public final int getOldPosition() {
      return this.mOldPosition;
    }
    
    @Deprecated
    public final int getPosition() {
      int i = this.mPreLayoutPosition;
      int j = i;
      if (i == -1)
        j = this.mPosition; 
      return j;
    }
    
    List<Object> getUnmodifiedPayloads() {
      if ((this.mFlags & 0x400) == 0) {
        List<Object> list = this.mPayloads;
        return (list == null || list.size() == 0) ? FULLUPDATE_PAYLOADS : this.mUnmodifiedPayloads;
      } 
      return FULLUPDATE_PAYLOADS;
    }
    
    boolean hasAnyOfTheFlags(int param1Int) {
      boolean bool;
      if ((param1Int & this.mFlags) != 0) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    boolean isAdapterPositionUnknown() {
      return ((this.mFlags & 0x200) != 0 || isInvalid());
    }
    
    boolean isAttachedToTransitionOverlay() {
      boolean bool;
      if (this.itemView.getParent() != null && this.itemView.getParent() != this.mOwnerRecyclerView) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    boolean isBound() {
      int i = this.mFlags;
      boolean bool = true;
      if ((i & 0x1) == 0)
        bool = false; 
      return bool;
    }
    
    boolean isInvalid() {
      boolean bool;
      if ((this.mFlags & 0x4) != 0) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public final boolean isRecyclable() {
      boolean bool;
      if ((this.mFlags & 0x10) == 0 && !ViewCompat.hasTransientState(this.itemView)) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    boolean isRemoved() {
      boolean bool;
      if ((this.mFlags & 0x8) != 0) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    boolean isScrap() {
      boolean bool;
      if (this.mScrapContainer != null) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    boolean isTmpDetached() {
      boolean bool;
      if ((this.mFlags & 0x100) != 0) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    boolean isUpdated() {
      boolean bool;
      if ((this.mFlags & 0x2) != 0) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    boolean needsUpdate() {
      boolean bool;
      if ((this.mFlags & 0x2) != 0) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    void offsetPosition(int param1Int, boolean param1Boolean) {
      if (this.mOldPosition == -1)
        this.mOldPosition = this.mPosition; 
      if (this.mPreLayoutPosition == -1)
        this.mPreLayoutPosition = this.mPosition; 
      if (param1Boolean)
        this.mPreLayoutPosition += param1Int; 
      this.mPosition += param1Int;
      if (this.itemView.getLayoutParams() != null)
        ((RecyclerView.LayoutParams)this.itemView.getLayoutParams()).mInsetsDirty = true; 
    }
    
    void onEnteredHiddenState(RecyclerView param1RecyclerView) {
      int i = this.mPendingAccessibilityState;
      if (i != -1) {
        this.mWasImportantForAccessibilityBeforeHidden = i;
      } else {
        this.mWasImportantForAccessibilityBeforeHidden = ViewCompat.getImportantForAccessibility(this.itemView);
      } 
      param1RecyclerView.setChildImportantForAccessibilityInternal(this, 4);
    }
    
    void onLeftHiddenState(RecyclerView param1RecyclerView) {
      param1RecyclerView.setChildImportantForAccessibilityInternal(this, this.mWasImportantForAccessibilityBeforeHidden);
      this.mWasImportantForAccessibilityBeforeHidden = 0;
    }
    
    void resetInternal() {
      this.mFlags = 0;
      this.mPosition = -1;
      this.mOldPosition = -1;
      this.mItemId = -1L;
      this.mPreLayoutPosition = -1;
      this.mIsRecyclableCount = 0;
      this.mShadowedHolder = null;
      this.mShadowingHolder = null;
      clearPayload();
      this.mWasImportantForAccessibilityBeforeHidden = 0;
      this.mPendingAccessibilityState = -1;
      RecyclerView.clearNestedRecyclerViewIfNotNested(this);
    }
    
    void saveOldPosition() {
      if (this.mOldPosition == -1)
        this.mOldPosition = this.mPosition; 
    }
    
    void setFlags(int param1Int1, int param1Int2) {
      this.mFlags = param1Int1 & param1Int2 | this.mFlags & (param1Int2 ^ 0xFFFFFFFF);
    }
    
    public final void setIsRecyclable(boolean param1Boolean) {
      int i = this.mIsRecyclableCount;
      if (param1Boolean) {
        i--;
      } else {
        i++;
      } 
      this.mIsRecyclableCount = i;
      i = this.mIsRecyclableCount;
      if (i < 0) {
        this.mIsRecyclableCount = 0;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("isRecyclable decremented below 0: unmatched pair of setIsRecyable() calls for ");
        stringBuilder.append(this);
        Log.e("View", stringBuilder.toString());
      } else if (!param1Boolean && i == 1) {
        this.mFlags |= 0x10;
      } else if (param1Boolean && this.mIsRecyclableCount == 0) {
        this.mFlags &= 0xFFFFFFEF;
      } 
    }
    
    void setScrapContainer(RecyclerView.Recycler param1Recycler, boolean param1Boolean) {
      this.mScrapContainer = param1Recycler;
      this.mInChangeScrap = param1Boolean;
    }
    
    boolean shouldBeKeptAsChild() {
      boolean bool;
      if ((this.mFlags & 0x10) != 0) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    boolean shouldIgnore() {
      boolean bool;
      if ((this.mFlags & 0x80) != 0) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    void stopIgnoring() {
      this.mFlags &= 0xFFFFFF7F;
    }
    
    public String toString() {
      String str;
      if (getClass().isAnonymousClass()) {
        str = "ViewHolder";
      } else {
        str = getClass().getSimpleName();
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(str);
      stringBuilder.append("{");
      stringBuilder.append(Integer.toHexString(hashCode()));
      stringBuilder.append(" position=");
      stringBuilder.append(this.mPosition);
      stringBuilder.append(" id=");
      stringBuilder.append(this.mItemId);
      stringBuilder.append(", oldPos=");
      stringBuilder.append(this.mOldPosition);
      stringBuilder.append(", pLpos:");
      stringBuilder.append(this.mPreLayoutPosition);
      stringBuilder = new StringBuilder(stringBuilder.toString());
      if (isScrap()) {
        stringBuilder.append(" scrap ");
        if (this.mInChangeScrap) {
          str = "[changeScrap]";
        } else {
          str = "[attachedScrap]";
        } 
        stringBuilder.append(str);
      } 
      if (isInvalid())
        stringBuilder.append(" invalid"); 
      if (!isBound())
        stringBuilder.append(" unbound"); 
      if (needsUpdate())
        stringBuilder.append(" update"); 
      if (isRemoved())
        stringBuilder.append(" removed"); 
      if (shouldIgnore())
        stringBuilder.append(" ignored"); 
      if (isTmpDetached())
        stringBuilder.append(" tmpDetached"); 
      if (!isRecyclable()) {
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append(" not recyclable(");
        stringBuilder1.append(this.mIsRecyclableCount);
        stringBuilder1.append(")");
        stringBuilder.append(stringBuilder1.toString());
      } 
      if (isAdapterPositionUnknown())
        stringBuilder.append(" undefined adapter position"); 
      if (this.itemView.getParent() == null)
        stringBuilder.append(" no parent"); 
      stringBuilder.append("}");
      return stringBuilder.toString();
    }
    
    void unScrap() {
      this.mScrapContainer.unscrapView(this);
    }
    
    boolean wasReturnedFromScrap() {
      boolean bool;
      if ((this.mFlags & 0x20) != 0) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/androidx/recyclerview/widget/RecyclerView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */