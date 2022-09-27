package com.stx.xhb.xbanner;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.stx.xhb.xbanner.entity.SimpleBannerInfo;
import com.stx.xhb.xbanner.transformers.BasePageTransformer;
import com.stx.xhb.xbanner.transformers.Transformer;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class XBanner extends RelativeLayout implements XBannerViewPager.AutoPlayDelegate, ViewPager.OnPageChangeListener {
  public static final int BOTTOM = 12;
  
  public static final int CENTER = 1;
  
  public static final int LEFT = 0;
  
  private static final int LWC = -2;
  
  public static final int NO_PLACE_HOLDER = -1;
  
  public static final int RIGHT = 2;
  
  private static final int RMP = -1;
  
  private static final int RWC = -2;
  
  public static final int TOP = 10;
  
  private static final int VEL_THRESHOLD = 400;
  
  private static final ImageView.ScaleType[] sScaleTypeArray = new ImageView.ScaleType[] { ImageView.ScaleType.MATRIX, ImageView.ScaleType.FIT_XY, ImageView.ScaleType.FIT_START, ImageView.ScaleType.FIT_CENTER, ImageView.ScaleType.FIT_END, ImageView.ScaleType.CENTER, ImageView.ScaleType.CENTER_CROP, ImageView.ScaleType.CENTER_INSIDE };
  
  private XBannerAdapter mAdapter;
  
  private int mAutoPalyTime = 5000;
  
  private AutoSwitchTask mAutoSwitchTask;
  
  public int mBannerBottomMargin = 0;
  
  private int mClipChildrenLeftRightMargin;
  
  private int mClipChildrenTopBottomMargin;
  
  private List<?> mDatas;
  
  private boolean mIsAllowUserScroll = true;
  
  private boolean mIsAutoPlay = true;
  
  private boolean mIsClipChildrenMode;
  
  private boolean mIsClipChildrenModeLessThree;
  
  private boolean mIsFirstInvisible = true;
  
  private boolean mIsHandLoop = false;
  
  private boolean mIsNumberIndicator = false;
  
  private boolean mIsOneImg = false;
  
  private boolean mIsShowIndicatorOnlyOne = false;
  
  private boolean mIsShowTips;
  
  private boolean mIsTipsMarquee = false;
  
  private List<View> mLessViews;
  
  private Drawable mNumberIndicatorBackground;
  
  private TextView mNumberIndicatorTv;
  
  private OnItemClickListener mOnItemClickListener;
  
  private ViewPager.OnPageChangeListener mOnPageChangeListener;
  
  private int mPageChangeDuration = 1000;
  
  private int mPageScrollPosition;
  
  private float mPageScrollPositionOffset;
  
  private int mPlaceholderDrawableResId = -1;
  
  private ImageView mPlaceholderImg;
  
  private Drawable mPointContainerBackgroundDrawable;
  
  private int mPointContainerLeftRightPadding;
  
  private RelativeLayout.LayoutParams mPointContainerLp;
  
  private int mPointContainerPosition = 12;
  
  private int mPointLeftRightPading;
  
  private int mPointNoraml;
  
  private int mPointPosition = 1;
  
  private LinearLayout mPointRealContainerLl;
  
  private RelativeLayout.LayoutParams mPointRealContainerLp;
  
  private int mPointSelected;
  
  private int mPointTopBottomPading;
  
  private boolean mPointsIsVisible = true;
  
  private ImageView.ScaleType mScaleType = ImageView.ScaleType.FIT_XY;
  
  private int mSlideScrollMode = 0;
  
  private List<String> mTipData;
  
  private int mTipTextColor;
  
  private int mTipTextSize;
  
  private TextView mTipTv;
  
  private Transformer mTransformer;
  
  private XBannerViewPager mViewPager;
  
  private boolean mViewPagerClipChildren;
  
  private int mViewPagerMargin;
  
  private List<View> mViews;
  
  public XBanner(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public XBanner(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public XBanner(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    init(paramContext);
    initCustomAttrs(paramContext, paramAttributeSet);
    initView();
  }
  
  private void init(Context paramContext) {
    this.mAutoSwitchTask = new AutoSwitchTask(this);
    this.mPointLeftRightPading = XBannerUtils.dp2px(paramContext, 3.0F);
    this.mPointTopBottomPading = XBannerUtils.dp2px(paramContext, 6.0F);
    this.mPointContainerLeftRightPadding = XBannerUtils.dp2px(paramContext, 10.0F);
    this.mClipChildrenLeftRightMargin = XBannerUtils.dp2px(paramContext, 30.0F);
    this.mClipChildrenTopBottomMargin = XBannerUtils.dp2px(paramContext, 10.0F);
    this.mViewPagerMargin = XBannerUtils.dp2px(paramContext, 10.0F);
    this.mTipTextSize = XBannerUtils.sp2px(paramContext, 10.0F);
    this.mTransformer = Transformer.Default;
    this.mTipTextColor = -1;
    this.mPointContainerBackgroundDrawable = (Drawable)new ColorDrawable(Color.parseColor("#44aaaaaa"));
  }
  
  private void initCustomAttrs(Context paramContext, AttributeSet paramAttributeSet) {
    TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.XBanner);
    if (typedArray != null) {
      this.mIsAutoPlay = typedArray.getBoolean(R.styleable.XBanner_isAutoPlay, true);
      this.mIsHandLoop = typedArray.getBoolean(R.styleable.XBanner_isHandLoop, false);
      this.mIsTipsMarquee = typedArray.getBoolean(R.styleable.XBanner_isTipsMarquee, false);
      this.mAutoPalyTime = typedArray.getInteger(R.styleable.XBanner_AutoPlayTime, 5000);
      this.mPointsIsVisible = typedArray.getBoolean(R.styleable.XBanner_pointsVisibility, true);
      this.mPointPosition = typedArray.getInt(R.styleable.XBanner_pointsPosition, 1);
      this.mPointContainerLeftRightPadding = typedArray.getDimensionPixelSize(R.styleable.XBanner_pointContainerLeftRightPadding, this.mPointContainerLeftRightPadding);
      this.mPointLeftRightPading = typedArray.getDimensionPixelSize(R.styleable.XBanner_pointLeftRightPadding, this.mPointLeftRightPading);
      this.mPointTopBottomPading = typedArray.getDimensionPixelSize(R.styleable.XBanner_pointTopBottomPadding, this.mPointTopBottomPading);
      this.mPointContainerPosition = typedArray.getInt(R.styleable.XBanner_pointContainerPosition, 12);
      this.mPointContainerBackgroundDrawable = typedArray.getDrawable(R.styleable.XBanner_pointsContainerBackground);
      this.mPointNoraml = typedArray.getResourceId(R.styleable.XBanner_pointNormal, R.drawable.shape_point_normal);
      this.mPointSelected = typedArray.getResourceId(R.styleable.XBanner_pointSelect, R.drawable.shape_point_select);
      this.mTipTextColor = typedArray.getColor(R.styleable.XBanner_tipTextColor, this.mTipTextColor);
      this.mTipTextSize = typedArray.getDimensionPixelSize(R.styleable.XBanner_tipTextSize, this.mTipTextSize);
      this.mIsNumberIndicator = typedArray.getBoolean(R.styleable.XBanner_isShowNumberIndicator, this.mIsNumberIndicator);
      this.mNumberIndicatorBackground = typedArray.getDrawable(R.styleable.XBanner_numberIndicatorBacgroud);
      this.mIsShowIndicatorOnlyOne = typedArray.getBoolean(R.styleable.XBanner_isShowIndicatorOnlyOne, this.mIsShowIndicatorOnlyOne);
      this.mPageChangeDuration = typedArray.getInt(R.styleable.XBanner_pageChangeDuration, this.mPageChangeDuration);
      this.mPlaceholderDrawableResId = typedArray.getResourceId(R.styleable.XBanner_placeholderDrawable, this.mPlaceholderDrawableResId);
      this.mIsClipChildrenMode = typedArray.getBoolean(R.styleable.XBanner_isClipChildrenMode, false);
      this.mClipChildrenLeftRightMargin = typedArray.getDimensionPixelSize(R.styleable.XBanner_clipChildrenLeftRightMargin, this.mClipChildrenLeftRightMargin);
      this.mClipChildrenTopBottomMargin = typedArray.getDimensionPixelSize(R.styleable.XBanner_clipChildrenTopBottomMargin, this.mClipChildrenTopBottomMargin);
      this.mViewPagerMargin = typedArray.getDimensionPixelSize(R.styleable.XBanner_viewpagerMargin, this.mViewPagerMargin);
      this.mIsClipChildrenModeLessThree = typedArray.getBoolean(R.styleable.XBanner_isClipChildrenModeLessThree, false);
      this.mIsShowTips = typedArray.getBoolean(R.styleable.XBanner_isShowTips, false);
      this.mBannerBottomMargin = typedArray.getDimensionPixelSize(R.styleable.XBanner_bannerBottomMargin, this.mBannerBottomMargin);
      this.mViewPagerClipChildren = typedArray.getBoolean(R.styleable.XBanner_viewPagerClipChildren, false);
      int i = typedArray.getInt(R.styleable.XBanner_android_scaleType, -1);
      if (i >= 0) {
        ImageView.ScaleType[] arrayOfScaleType = sScaleTypeArray;
        if (i < arrayOfScaleType.length)
          this.mScaleType = arrayOfScaleType[i]; 
      } 
      typedArray.recycle();
    } 
    if (this.mIsClipChildrenMode)
      this.mTransformer = Transformer.Scale; 
  }
  
  private void initPoints() {
    LinearLayout linearLayout = this.mPointRealContainerLl;
    if (linearLayout != null) {
      linearLayout.removeAllViews();
      if (getRealCount() > 0 && (this.mIsShowIndicatorOnlyOne || !this.mIsOneImg)) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
        layoutParams.gravity = 16;
        int i = this.mPointLeftRightPading;
        int j = this.mPointTopBottomPading;
        layoutParams.setMargins(i, j, i, j);
        for (j = 0; j < getRealCount(); j++) {
          ImageView imageView = new ImageView(getContext());
          imageView.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
          i = this.mPointNoraml;
          if (i != 0 && this.mPointSelected != 0)
            imageView.setImageResource(i); 
          this.mPointRealContainerLl.addView((View)imageView);
        } 
      } 
    } 
    if (this.mNumberIndicatorTv != null)
      if (getRealCount() > 0 && (this.mIsShowIndicatorOnlyOne || !this.mIsOneImg)) {
        this.mNumberIndicatorTv.setVisibility(0);
      } else {
        this.mNumberIndicatorTv.setVisibility(8);
      }  
  }
  
  private void initView() {
    RelativeLayout relativeLayout = new RelativeLayout(getContext());
    if (Build.VERSION.SDK_INT >= 16) {
      relativeLayout.setBackground(this.mPointContainerBackgroundDrawable);
    } else {
      relativeLayout.setBackgroundDrawable(this.mPointContainerBackgroundDrawable);
    } 
    int i = this.mPointContainerLeftRightPadding;
    int j = this.mPointTopBottomPading;
    relativeLayout.setPadding(i, j, i, j);
    this.mPointContainerLp = new RelativeLayout.LayoutParams(-1, -2);
    this.mPointContainerLp.addRule(this.mPointContainerPosition);
    if (this.mIsClipChildrenMode) {
      RelativeLayout.LayoutParams layoutParams1 = this.mPointContainerLp;
      i = this.mClipChildrenLeftRightMargin;
      layoutParams1.setMargins(i, 0, i, this.mClipChildrenTopBottomMargin);
    } 
    addView((View)relativeLayout, (ViewGroup.LayoutParams)this.mPointContainerLp);
    this.mPointRealContainerLp = new RelativeLayout.LayoutParams(-2, -2);
    if (this.mIsNumberIndicator) {
      this.mNumberIndicatorTv = new TextView(getContext());
      this.mNumberIndicatorTv.setId(R.id.xbanner_pointId);
      this.mNumberIndicatorTv.setGravity(17);
      this.mNumberIndicatorTv.setSingleLine(true);
      this.mNumberIndicatorTv.setEllipsize(TextUtils.TruncateAt.END);
      this.mNumberIndicatorTv.setTextColor(this.mTipTextColor);
      this.mNumberIndicatorTv.setTextSize(0, this.mTipTextSize);
      this.mNumberIndicatorTv.setVisibility(4);
      if (this.mNumberIndicatorBackground != null)
        if (Build.VERSION.SDK_INT >= 16) {
          this.mNumberIndicatorTv.setBackground(this.mNumberIndicatorBackground);
        } else {
          this.mNumberIndicatorTv.setBackgroundDrawable(this.mNumberIndicatorBackground);
        }  
      relativeLayout.addView((View)this.mNumberIndicatorTv, (ViewGroup.LayoutParams)this.mPointRealContainerLp);
    } else {
      this.mPointRealContainerLl = new LinearLayout(getContext());
      this.mPointRealContainerLl.setOrientation(0);
      this.mPointRealContainerLl.setId(R.id.xbanner_pointId);
      relativeLayout.addView((View)this.mPointRealContainerLl, (ViewGroup.LayoutParams)this.mPointRealContainerLp);
    } 
    LinearLayout linearLayout = this.mPointRealContainerLl;
    if (linearLayout != null)
      if (this.mPointsIsVisible) {
        linearLayout.setVisibility(0);
      } else {
        linearLayout.setVisibility(8);
      }  
    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -2);
    layoutParams.addRule(15);
    if (this.mIsShowTips) {
      this.mTipTv = new TextView(getContext());
      this.mTipTv.setGravity(16);
      this.mTipTv.setSingleLine(true);
      if (this.mIsTipsMarquee) {
        this.mTipTv.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        this.mTipTv.setMarqueeRepeatLimit(3);
        this.mTipTv.setSelected(true);
      } else {
        this.mTipTv.setEllipsize(TextUtils.TruncateAt.END);
      } 
      this.mTipTv.setTextColor(this.mTipTextColor);
      this.mTipTv.setTextSize(0, this.mTipTextSize);
      relativeLayout.addView((View)this.mTipTv, (ViewGroup.LayoutParams)layoutParams);
    } 
    i = this.mPointPosition;
    if (1 == i) {
      this.mPointRealContainerLp.addRule(14);
      layoutParams.addRule(0, R.id.xbanner_pointId);
    } else if (i == 0) {
      this.mPointRealContainerLp.addRule(9);
      TextView textView = this.mTipTv;
      if (textView != null)
        textView.setGravity(21); 
      layoutParams.addRule(1, R.id.xbanner_pointId);
    } else if (2 == i) {
      this.mPointRealContainerLp.addRule(11);
      layoutParams.addRule(0, R.id.xbanner_pointId);
    } 
    setBannerPlaceholderDrawable();
  }
  
  private void initViewPager() {
    XBannerViewPager xBannerViewPager = this.mViewPager;
    if (xBannerViewPager != null && equals(xBannerViewPager.getParent())) {
      removeView((View)this.mViewPager);
      this.mViewPager = null;
    } 
    this.mViewPager = new XBannerViewPager(getContext());
    this.mViewPager.setAdapter(new XBannerPageAdapter());
    this.mViewPager.addOnPageChangeListener(this);
    this.mViewPager.setOverScrollMode(this.mSlideScrollMode);
    this.mViewPager.setIsAllowUserScroll(this.mIsAllowUserScroll);
    this.mViewPager.setPageTransformer(true, (ViewPager.PageTransformer)BasePageTransformer.getPageTransformer(this.mTransformer));
    setPageChangeDuration(this.mPageChangeDuration);
    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -1);
    layoutParams.setMargins(0, 0, 0, this.mBannerBottomMargin);
    if (this.mIsClipChildrenMode) {
      this.mViewPager.setPageMargin(this.mViewPagerMargin);
      this.mViewPager.setClipChildren(this.mViewPagerClipChildren);
      setClipChildren(false);
      int i = this.mClipChildrenLeftRightMargin;
      int j = this.mClipChildrenTopBottomMargin;
      layoutParams.setMargins(i, j, i, this.mBannerBottomMargin + j);
      setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View param1View, MotionEvent param1MotionEvent) {
              return XBanner.this.mViewPager.dispatchTouchEvent(param1MotionEvent);
            }
          });
    } 
    addView((View)this.mViewPager, 0, (ViewGroup.LayoutParams)layoutParams);
    if (!this.mIsOneImg && this.mIsAutoPlay && getRealCount() != 0) {
      this.mViewPager.setAutoPlayDelegate(this);
      int i = getRealCount();
      this.mViewPager.setCurrentItem(1073741823 - 1073741823 % i, false);
      startAutoPlay();
    } else {
      if (this.mIsHandLoop && getRealCount() != 0) {
        int i = getRealCount();
        this.mViewPager.setCurrentItem(1073741823 - 1073741823 % i, false);
      } 
      switchToPoint(0);
    } 
  }
  
  private void onInvisibleToUser() {
    stopAutoPlay();
    if (!this.mIsFirstInvisible && this.mIsAutoPlay && this.mViewPager != null && getRealCount() > 0 && this.mPageScrollPositionOffset != 0.0F) {
      XBannerViewPager xBannerViewPager = this.mViewPager;
      xBannerViewPager.setCurrentItem(xBannerViewPager.getCurrentItem() - 1, false);
      xBannerViewPager = this.mViewPager;
      xBannerViewPager.setCurrentItem(xBannerViewPager.getCurrentItem() + 1, false);
    } 
    this.mIsFirstInvisible = false;
  }
  
  private void removeBannerPlaceHolderDrawable() {
    ImageView imageView = this.mPlaceholderImg;
    if (imageView != null && equals(imageView.getParent())) {
      removeView((View)this.mPlaceholderImg);
      this.mPlaceholderImg = null;
    } 
  }
  
  private void setBannerData(List<View> paramList, List<? extends SimpleBannerInfo> paramList1) {
    if (this.mIsAutoPlay && paramList.size() < 3 && this.mLessViews == null)
      this.mIsAutoPlay = false; 
    if (!this.mIsClipChildrenModeLessThree && paramList.size() < 3)
      this.mIsClipChildrenMode = false; 
    this.mDatas = paramList1;
    this.mViews = paramList;
    int i = paramList1.size();
    boolean bool = true;
    if (i > 1)
      bool = false; 
    this.mIsOneImg = bool;
    initPoints();
    initViewPager();
    removeBannerPlaceHolderDrawable();
    if (!paramList1.isEmpty()) {
      removeBannerPlaceHolderDrawable();
    } else {
      setBannerPlaceholderDrawable();
    } 
  }
  
  private void setBannerPlaceholderDrawable() {
    if (this.mPlaceholderDrawableResId != -1 && this.mPlaceholderImg == null) {
      this.mPlaceholderImg = new ImageView(getContext());
      this.mPlaceholderImg.setScaleType(this.mScaleType);
      this.mPlaceholderImg.setImageResource(this.mPlaceholderDrawableResId);
      RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -1);
      addView((View)this.mPlaceholderImg, (ViewGroup.LayoutParams)layoutParams);
    } 
  }
  
  @Deprecated
  private void setData(List<View> paramList, List<?> paramList1, List<String> paramList2) {
    if (this.mIsAutoPlay && paramList.size() < 3 && this.mLessViews == null)
      this.mIsAutoPlay = false; 
    if (!this.mIsClipChildrenModeLessThree && paramList.size() < 3)
      this.mIsClipChildrenMode = false; 
    this.mDatas = paramList1;
    this.mTipData = paramList2;
    this.mViews = paramList;
    int i = paramList1.size();
    boolean bool = true;
    if (i > 1)
      bool = false; 
    this.mIsOneImg = bool;
    initPoints();
    initViewPager();
    removeBannerPlaceHolderDrawable();
    if (!paramList1.isEmpty()) {
      removeBannerPlaceHolderDrawable();
    } else {
      setBannerPlaceholderDrawable();
    } 
  }
  
  private void switchToPoint(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mPointRealContainerLl : Landroid/widget/LinearLayout;
    //   4: ifnull -> 12
    //   7: iconst_1
    //   8: istore_2
    //   9: goto -> 14
    //   12: iconst_0
    //   13: istore_2
    //   14: aload_0
    //   15: getfield mDatas : Ljava/util/List;
    //   18: ifnull -> 26
    //   21: iconst_1
    //   22: istore_3
    //   23: goto -> 28
    //   26: iconst_0
    //   27: istore_3
    //   28: iload_2
    //   29: iload_3
    //   30: iand
    //   31: ifeq -> 116
    //   34: aload_0
    //   35: invokevirtual getRealCount : ()I
    //   38: iconst_1
    //   39: if_icmple -> 116
    //   42: iconst_0
    //   43: istore_2
    //   44: iload_2
    //   45: aload_0
    //   46: getfield mPointRealContainerLl : Landroid/widget/LinearLayout;
    //   49: invokevirtual getChildCount : ()I
    //   52: if_icmpge -> 116
    //   55: iload_2
    //   56: iload_1
    //   57: if_icmpne -> 81
    //   60: aload_0
    //   61: getfield mPointRealContainerLl : Landroid/widget/LinearLayout;
    //   64: iload_2
    //   65: invokevirtual getChildAt : (I)Landroid/view/View;
    //   68: checkcast android/widget/ImageView
    //   71: aload_0
    //   72: getfield mPointSelected : I
    //   75: invokevirtual setImageResource : (I)V
    //   78: goto -> 99
    //   81: aload_0
    //   82: getfield mPointRealContainerLl : Landroid/widget/LinearLayout;
    //   85: iload_2
    //   86: invokevirtual getChildAt : (I)Landroid/view/View;
    //   89: checkcast android/widget/ImageView
    //   92: aload_0
    //   93: getfield mPointNoraml : I
    //   96: invokevirtual setImageResource : (I)V
    //   99: aload_0
    //   100: getfield mPointRealContainerLl : Landroid/widget/LinearLayout;
    //   103: iload_2
    //   104: invokevirtual getChildAt : (I)Landroid/view/View;
    //   107: invokevirtual requestLayout : ()V
    //   110: iinc #2, 1
    //   113: goto -> 44
    //   116: aload_0
    //   117: getfield mTipTv : Landroid/widget/TextView;
    //   120: ifnull -> 186
    //   123: aload_0
    //   124: getfield mDatas : Ljava/util/List;
    //   127: astore #4
    //   129: aload #4
    //   131: ifnull -> 186
    //   134: aload #4
    //   136: invokeinterface size : ()I
    //   141: ifeq -> 186
    //   144: aload_0
    //   145: getfield mDatas : Ljava/util/List;
    //   148: iconst_0
    //   149: invokeinterface get : (I)Ljava/lang/Object;
    //   154: instanceof com/stx/xhb/xbanner/entity/SimpleBannerInfo
    //   157: ifeq -> 186
    //   160: aload_0
    //   161: getfield mTipTv : Landroid/widget/TextView;
    //   164: aload_0
    //   165: getfield mDatas : Ljava/util/List;
    //   168: iload_1
    //   169: invokeinterface get : (I)Ljava/lang/Object;
    //   174: checkcast com/stx/xhb/xbanner/entity/SimpleBannerInfo
    //   177: invokevirtual getXBannerTitle : ()Ljava/lang/String;
    //   180: invokevirtual setText : (Ljava/lang/CharSequence;)V
    //   183: goto -> 234
    //   186: aload_0
    //   187: getfield mTipTv : Landroid/widget/TextView;
    //   190: ifnull -> 234
    //   193: aload_0
    //   194: getfield mTipData : Ljava/util/List;
    //   197: astore #4
    //   199: aload #4
    //   201: ifnull -> 234
    //   204: aload #4
    //   206: invokeinterface isEmpty : ()Z
    //   211: ifne -> 234
    //   214: aload_0
    //   215: getfield mTipTv : Landroid/widget/TextView;
    //   218: aload_0
    //   219: getfield mTipData : Ljava/util/List;
    //   222: iload_1
    //   223: invokeinterface get : (I)Ljava/lang/Object;
    //   228: checkcast java/lang/CharSequence
    //   231: invokevirtual setText : (Ljava/lang/CharSequence;)V
    //   234: aload_0
    //   235: getfield mNumberIndicatorTv : Landroid/widget/TextView;
    //   238: ifnull -> 323
    //   241: aload_0
    //   242: getfield mViews : Ljava/util/List;
    //   245: ifnull -> 323
    //   248: aload_0
    //   249: getfield mIsShowIndicatorOnlyOne : Z
    //   252: ifne -> 262
    //   255: aload_0
    //   256: getfield mIsOneImg : Z
    //   259: ifne -> 323
    //   262: aload_0
    //   263: getfield mNumberIndicatorTv : Landroid/widget/TextView;
    //   266: astore #4
    //   268: new java/lang/StringBuilder
    //   271: dup
    //   272: invokespecial <init> : ()V
    //   275: astore #5
    //   277: aload #5
    //   279: iload_1
    //   280: iconst_1
    //   281: iadd
    //   282: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   285: pop
    //   286: aload #5
    //   288: ldc_w '/'
    //   291: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   294: pop
    //   295: aload #5
    //   297: aload_0
    //   298: getfield mViews : Ljava/util/List;
    //   301: invokeinterface size : ()I
    //   306: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   309: pop
    //   310: aload #4
    //   312: aload #5
    //   314: invokevirtual toString : ()Ljava/lang/String;
    //   317: invokestatic valueOf : (Ljava/lang/Object;)Ljava/lang/String;
    //   320: invokevirtual setText : (Ljava/lang/CharSequence;)V
    //   323: return
  }
  
  public boolean dispatchTouchEvent(MotionEvent paramMotionEvent) {
    if (this.mIsAutoPlay) {
      int i;
      boolean bool = this.mIsOneImg;
      if (this.mViewPager != null) {
        i = 1;
      } else {
        i = 0;
      } 
      if (((bool ^ true) & i) != 0) {
        i = paramMotionEvent.getAction();
        if (i != 0) {
          if (i == 1 || i == 3 || i == 4)
            startAutoPlay(); 
        } else {
          float f = paramMotionEvent.getRawX();
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("touchX:");
          stringBuilder.append(f);
          Log.i("===>touchX", stringBuilder.toString());
          i = this.mViewPager.getLeft();
          if (f >= i && f < (XBannerUtils.getScreenWidth(getContext()) - i))
            stopAutoPlay(); 
        } 
      } 
    } 
    return super.dispatchTouchEvent(paramMotionEvent);
  }
  
  public int getBannerCurrentItem() {
    if (this.mViewPager != null) {
      List<?> list = this.mDatas;
      if (list != null && list.size() != 0)
        return this.mViewPager.getCurrentItem() % getRealCount(); 
    } 
    return -1;
  }
  
  public int getRealCount() {
    int i;
    List<View> list = this.mViews;
    if (list == null) {
      i = 0;
    } else {
      i = list.size();
    } 
    return i;
  }
  
  public XBannerViewPager getViewPager() {
    return this.mViewPager;
  }
  
  public void handleAutoPlayActionUpOrCancel(float paramFloat) {
    if (this.mPageScrollPosition < this.mViewPager.getCurrentItem()) {
      if (paramFloat > 400.0F || (this.mPageScrollPositionOffset < 0.7F && paramFloat > -400.0F)) {
        this.mViewPager.setBannerCurrentItemInternal(this.mPageScrollPosition, true);
        return;
      } 
      this.mViewPager.setBannerCurrentItemInternal(this.mPageScrollPosition + 1, true);
    } else if (this.mPageScrollPosition == this.mViewPager.getCurrentItem()) {
      if (paramFloat < -400.0F || (this.mPageScrollPositionOffset > 0.3F && paramFloat < 400.0F)) {
        this.mViewPager.setBannerCurrentItemInternal(this.mPageScrollPosition + 1, true);
        return;
      } 
      this.mViewPager.setBannerCurrentItemInternal(this.mPageScrollPosition, true);
    } else {
      this.mViewPager.setBannerCurrentItemInternal(this.mPageScrollPosition, true);
    } 
  }
  
  public void loadImage(XBannerAdapter paramXBannerAdapter) {
    this.mAdapter = paramXBannerAdapter;
  }
  
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    startAutoPlay();
  }
  
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    onInvisibleToUser();
  }
  
  public void onPageScrollStateChanged(int paramInt) {
    ViewPager.OnPageChangeListener onPageChangeListener = this.mOnPageChangeListener;
    if (onPageChangeListener != null)
      onPageChangeListener.onPageScrollStateChanged(paramInt); 
  }
  
  public void onPageScrolled(int paramInt1, float paramFloat, int paramInt2) {
    // Byte code:
    //   0: aload_0
    //   1: iload_1
    //   2: putfield mPageScrollPosition : I
    //   5: aload_0
    //   6: fload_2
    //   7: putfield mPageScrollPositionOffset : F
    //   10: aload_0
    //   11: getfield mTipTv : Landroid/widget/TextView;
    //   14: ifnull -> 167
    //   17: aload_0
    //   18: getfield mDatas : Ljava/util/List;
    //   21: astore #4
    //   23: aload #4
    //   25: ifnull -> 167
    //   28: aload #4
    //   30: invokeinterface size : ()I
    //   35: ifeq -> 167
    //   38: aload_0
    //   39: getfield mDatas : Ljava/util/List;
    //   42: iconst_0
    //   43: invokeinterface get : (I)Ljava/lang/Object;
    //   48: instanceof com/stx/xhb/xbanner/entity/SimpleBannerInfo
    //   51: ifeq -> 167
    //   54: fload_2
    //   55: f2d
    //   56: ldc2_w 0.5
    //   59: dcmpl
    //   60: ifle -> 115
    //   63: aload_0
    //   64: getfield mTipTv : Landroid/widget/TextView;
    //   67: astore #5
    //   69: aload_0
    //   70: getfield mDatas : Ljava/util/List;
    //   73: astore #4
    //   75: aload #5
    //   77: aload #4
    //   79: iload_1
    //   80: iconst_1
    //   81: iadd
    //   82: aload #4
    //   84: invokeinterface size : ()I
    //   89: irem
    //   90: invokeinterface get : (I)Ljava/lang/Object;
    //   95: checkcast com/stx/xhb/xbanner/entity/SimpleBannerInfo
    //   98: invokevirtual getXBannerTitle : ()Ljava/lang/String;
    //   101: invokevirtual setText : (Ljava/lang/CharSequence;)V
    //   104: aload_0
    //   105: getfield mTipTv : Landroid/widget/TextView;
    //   108: fload_2
    //   109: invokevirtual setAlpha : (F)V
    //   112: goto -> 299
    //   115: aload_0
    //   116: getfield mTipTv : Landroid/widget/TextView;
    //   119: astore #4
    //   121: aload_0
    //   122: getfield mDatas : Ljava/util/List;
    //   125: astore #5
    //   127: aload #4
    //   129: aload #5
    //   131: iload_1
    //   132: aload #5
    //   134: invokeinterface size : ()I
    //   139: irem
    //   140: invokeinterface get : (I)Ljava/lang/Object;
    //   145: checkcast com/stx/xhb/xbanner/entity/SimpleBannerInfo
    //   148: invokevirtual getXBannerTitle : ()Ljava/lang/String;
    //   151: invokevirtual setText : (Ljava/lang/CharSequence;)V
    //   154: aload_0
    //   155: getfield mTipTv : Landroid/widget/TextView;
    //   158: fconst_1
    //   159: fload_2
    //   160: fsub
    //   161: invokevirtual setAlpha : (F)V
    //   164: goto -> 299
    //   167: aload_0
    //   168: getfield mTipTv : Landroid/widget/TextView;
    //   171: ifnull -> 299
    //   174: aload_0
    //   175: getfield mTipData : Ljava/util/List;
    //   178: astore #4
    //   180: aload #4
    //   182: ifnull -> 299
    //   185: aload #4
    //   187: invokeinterface isEmpty : ()Z
    //   192: ifne -> 299
    //   195: fload_2
    //   196: f2d
    //   197: ldc2_w 0.5
    //   200: dcmpl
    //   201: ifle -> 253
    //   204: aload_0
    //   205: getfield mTipTv : Landroid/widget/TextView;
    //   208: astore #4
    //   210: aload_0
    //   211: getfield mTipData : Ljava/util/List;
    //   214: astore #5
    //   216: aload #4
    //   218: aload #5
    //   220: iload_1
    //   221: iconst_1
    //   222: iadd
    //   223: aload #5
    //   225: invokeinterface size : ()I
    //   230: irem
    //   231: invokeinterface get : (I)Ljava/lang/Object;
    //   236: checkcast java/lang/CharSequence
    //   239: invokevirtual setText : (Ljava/lang/CharSequence;)V
    //   242: aload_0
    //   243: getfield mTipTv : Landroid/widget/TextView;
    //   246: fload_2
    //   247: invokevirtual setAlpha : (F)V
    //   250: goto -> 299
    //   253: aload_0
    //   254: getfield mTipTv : Landroid/widget/TextView;
    //   257: astore #5
    //   259: aload_0
    //   260: getfield mTipData : Ljava/util/List;
    //   263: astore #4
    //   265: aload #5
    //   267: aload #4
    //   269: iload_1
    //   270: aload #4
    //   272: invokeinterface size : ()I
    //   277: irem
    //   278: invokeinterface get : (I)Ljava/lang/Object;
    //   283: checkcast java/lang/CharSequence
    //   286: invokevirtual setText : (Ljava/lang/CharSequence;)V
    //   289: aload_0
    //   290: getfield mTipTv : Landroid/widget/TextView;
    //   293: fconst_1
    //   294: fload_2
    //   295: fsub
    //   296: invokevirtual setAlpha : (F)V
    //   299: aload_0
    //   300: getfield mOnPageChangeListener : Landroidx/viewpager/widget/ViewPager$OnPageChangeListener;
    //   303: ifnull -> 330
    //   306: aload_0
    //   307: invokevirtual getRealCount : ()I
    //   310: ifeq -> 330
    //   313: aload_0
    //   314: getfield mOnPageChangeListener : Landroidx/viewpager/widget/ViewPager$OnPageChangeListener;
    //   317: iload_1
    //   318: aload_0
    //   319: invokevirtual getRealCount : ()I
    //   322: irem
    //   323: fload_2
    //   324: iload_3
    //   325: invokeinterface onPageScrolled : (IFI)V
    //   330: return
  }
  
  public void onPageSelected(int paramInt) {
    if (getRealCount() == 0)
      return; 
    paramInt %= getRealCount();
    switchToPoint(paramInt);
    ViewPager.OnPageChangeListener onPageChangeListener = this.mOnPageChangeListener;
    if (onPageChangeListener != null)
      onPageChangeListener.onPageSelected(paramInt); 
  }
  
  protected void onVisibilityChanged(View paramView, int paramInt) {
    super.onVisibilityChanged(paramView, paramInt);
    if (paramInt == 0) {
      startAutoPlay();
    } else if (8 == paramInt || 4 == paramInt) {
      onInvisibleToUser();
    } 
  }
  
  public void setAllowUserScrollable(boolean paramBoolean) {
    this.mIsAllowUserScroll = paramBoolean;
    XBannerViewPager xBannerViewPager = this.mViewPager;
    if (xBannerViewPager != null)
      xBannerViewPager.setIsAllowUserScroll(paramBoolean); 
  }
  
  public void setAutoPalyTime(int paramInt) {
    this.mAutoPalyTime = paramInt;
  }
  
  public void setAutoPlayAble(boolean paramBoolean) {
    this.mIsAutoPlay = paramBoolean;
    stopAutoPlay();
    XBannerViewPager xBannerViewPager = this.mViewPager;
    if (xBannerViewPager != null && xBannerViewPager.getAdapter() != null)
      this.mViewPager.getAdapter().notifyDataSetChanged(); 
  }
  
  public void setBannerCurrentItem(int paramInt) {
    if (this.mViewPager != null && this.mDatas != null) {
      int i = getRealCount();
      boolean bool = true;
      if (paramInt <= i - 1) {
        if (this.mIsAutoPlay || this.mIsHandLoop) {
          i = this.mViewPager.getCurrentItem();
          int j = paramInt - i % getRealCount();
          if (j < 0) {
            for (paramInt = -1; paramInt >= j; paramInt--)
              this.mViewPager.setCurrentItem(i + paramInt, false); 
          } else if (j > 0) {
            for (paramInt = bool; paramInt <= j; paramInt++)
              this.mViewPager.setCurrentItem(i + paramInt, false); 
          } 
          if (this.mIsAutoPlay)
            startAutoPlay(); 
          return;
        } 
        this.mViewPager.setCurrentItem(paramInt, false);
      } 
    } 
  }
  
  public void setBannerData(int paramInt, List<? extends SimpleBannerInfo> paramList) {
    this.mViews = new ArrayList<View>();
    List<? extends SimpleBannerInfo> list = paramList;
    if (paramList == null)
      list = new ArrayList<SimpleBannerInfo>(); 
    for (byte b = 0; b < list.size(); b++)
      this.mViews.add(View.inflate(getContext(), paramInt, null)); 
    if (this.mViews.isEmpty()) {
      this.mIsAutoPlay = false;
      this.mIsClipChildrenMode = false;
    } 
    if ((this.mIsAutoPlay && this.mViews.size() < 3) || (this.mIsHandLoop && this.mViews.size() < 3)) {
      this.mLessViews = new ArrayList<View>(this.mViews);
      this.mLessViews.add(View.inflate(getContext(), paramInt, null));
      if (this.mLessViews.size() == 2)
        this.mLessViews.add(View.inflate(getContext(), paramInt, null)); 
    } 
    setBannerData(this.mViews, list);
  }
  
  public void setBannerData(List<? extends SimpleBannerInfo> paramList) {
    setBannerData(R.layout.xbanner_item_image, paramList);
  }
  
  public void setCustomPageTransformer(ViewPager.PageTransformer paramPageTransformer) {
    if (paramPageTransformer != null) {
      XBannerViewPager xBannerViewPager = this.mViewPager;
      if (xBannerViewPager != null)
        xBannerViewPager.setPageTransformer(true, paramPageTransformer); 
    } 
  }
  
  @Deprecated
  public void setData(int paramInt, List<?> paramList, List<String> paramList1) {
    this.mViews = new ArrayList<View>();
    List<?> list = paramList;
    if (paramList == null)
      list = new ArrayList(); 
    for (byte b = 0; b < list.size(); b++)
      this.mViews.add(View.inflate(getContext(), paramInt, null)); 
    if (this.mViews.isEmpty()) {
      this.mIsAutoPlay = false;
      this.mIsClipChildrenMode = false;
    } 
    if ((this.mIsAutoPlay && this.mViews.size() < 3) || (this.mIsHandLoop && this.mViews.size() < 3)) {
      this.mLessViews = new ArrayList<View>(this.mViews);
      this.mLessViews.add(View.inflate(getContext(), paramInt, null));
      if (this.mLessViews.size() == 2)
        this.mLessViews.add(View.inflate(getContext(), paramInt, null)); 
    } 
    setData(this.mViews, list, paramList1);
  }
  
  @Deprecated
  public void setData(List<?> paramList, List<String> paramList1) {
    setData(R.layout.xbanner_item_image, paramList, paramList1);
  }
  
  public void setHandLoop(boolean paramBoolean) {
    this.mIsHandLoop = paramBoolean;
  }
  
  public void setIsClipChildrenMode(boolean paramBoolean) {
    this.mIsClipChildrenMode = paramBoolean;
  }
  
  public void setOnItemClickListener(OnItemClickListener paramOnItemClickListener) {
    this.mOnItemClickListener = paramOnItemClickListener;
  }
  
  public void setOnPageChangeListener(ViewPager.OnPageChangeListener paramOnPageChangeListener) {
    this.mOnPageChangeListener = paramOnPageChangeListener;
  }
  
  public void setPageChangeDuration(int paramInt) {
    XBannerViewPager xBannerViewPager = this.mViewPager;
    if (xBannerViewPager != null)
      xBannerViewPager.setScrollDuration(paramInt); 
  }
  
  public void setPageTransformer(Transformer paramTransformer) {
    this.mTransformer = paramTransformer;
    if (this.mViewPager != null) {
      initViewPager();
      List<View> list = this.mLessViews;
      if (list == null) {
        XBannerUtils.resetPageTransformer(this.mViews);
      } else {
        XBannerUtils.resetPageTransformer(list);
      } 
    } 
  }
  
  public void setPointContainerPosition(int paramInt) {
    if (12 == paramInt) {
      this.mPointContainerLp.addRule(12);
    } else if (10 == paramInt) {
      this.mPointContainerLp.addRule(10);
    } 
  }
  
  public void setPointPosition(int paramInt) {
    if (1 == paramInt) {
      this.mPointRealContainerLp.addRule(14);
    } else if (paramInt == 0) {
      this.mPointRealContainerLp.addRule(9);
    } else if (2 == paramInt) {
      this.mPointRealContainerLp.addRule(11);
    } 
  }
  
  public void setPointsIsVisible(boolean paramBoolean) {
    LinearLayout linearLayout = this.mPointRealContainerLl;
    if (linearLayout != null)
      if (paramBoolean) {
        linearLayout.setVisibility(0);
      } else {
        linearLayout.setVisibility(8);
      }  
  }
  
  public void setShowIndicatorOnlyOne(boolean paramBoolean) {
    this.mIsShowIndicatorOnlyOne = paramBoolean;
  }
  
  public void setSlideScrollMode(int paramInt) {
    this.mSlideScrollMode = paramInt;
    XBannerViewPager xBannerViewPager = this.mViewPager;
    if (xBannerViewPager != null)
      xBannerViewPager.setOverScrollMode(paramInt); 
  }
  
  public void setViewPagerClipChildren(boolean paramBoolean) {
    this.mViewPagerClipChildren = paramBoolean;
    XBannerViewPager xBannerViewPager = this.mViewPager;
    if (xBannerViewPager != null)
      xBannerViewPager.setClipChildren(paramBoolean); 
  }
  
  public void setViewPagerMargin(int paramInt) {
    this.mViewPagerMargin = paramInt;
    XBannerViewPager xBannerViewPager = this.mViewPager;
    if (xBannerViewPager != null)
      xBannerViewPager.setPageMargin(XBannerUtils.dp2px(getContext(), paramInt)); 
  }
  
  @Deprecated
  public void setmAdapter(XBannerAdapter paramXBannerAdapter) {
    this.mAdapter = paramXBannerAdapter;
  }
  
  public void startAutoPlay() {
    stopAutoPlay();
    if (this.mIsAutoPlay)
      postDelayed(this.mAutoSwitchTask, this.mAutoPalyTime); 
  }
  
  public void stopAutoPlay() {
    AutoSwitchTask autoSwitchTask = this.mAutoSwitchTask;
    if (autoSwitchTask != null)
      removeCallbacks(autoSwitchTask); 
  }
  
  private static class AutoSwitchTask implements Runnable {
    private final WeakReference<XBanner> mXBanner;
    
    private AutoSwitchTask(XBanner param1XBanner) {
      this.mXBanner = new WeakReference<XBanner>(param1XBanner);
    }
    
    public void run() {
      XBanner xBanner = this.mXBanner.get();
      if (xBanner != null) {
        if (xBanner.mViewPager != null) {
          int i = xBanner.mViewPager.getCurrentItem();
          xBanner.mViewPager.setCurrentItem(i + 1);
        } 
        xBanner.startAutoPlay();
      } 
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface INDICATOR_GRAVITY {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface INDICATOR_POSITION {}
  
  public static interface OnItemClickListener {
    void onItemClick(XBanner param1XBanner, Object param1Object, View param1View, int param1Int);
  }
  
  public static interface XBannerAdapter {
    void loadBanner(XBanner param1XBanner, Object param1Object, View param1View, int param1Int);
  }
  
  private class XBannerPageAdapter extends PagerAdapter {
    private XBannerPageAdapter() {}
    
    public void destroyItem(ViewGroup param1ViewGroup, int param1Int, Object param1Object) {}
    
    public int getCount() {
      if (XBanner.this.mIsOneImg)
        return 1; 
      boolean bool = XBanner.this.mIsAutoPlay;
      int i = Integer.MAX_VALUE;
      if (!bool && !XBanner.this.mIsHandLoop)
        i = XBanner.this.getRealCount(); 
      return i;
    }
    
    public int getItemPosition(Object param1Object) {
      return -2;
    }
    
    public Object instantiateItem(ViewGroup param1ViewGroup, int param1Int) {
      View view;
      if (XBanner.this.getRealCount() == 0)
        return null; 
      final int realPosition = param1Int % XBanner.this.getRealCount();
      if (XBanner.this.mViews.size() < 3 && XBanner.this.mLessViews != null) {
        view = XBanner.this.mLessViews.get(param1Int % XBanner.this.mLessViews.size());
      } else {
        view = XBanner.this.mViews.get(i);
      } 
      if (param1ViewGroup.equals(view.getParent()))
        param1ViewGroup.removeView(view); 
      if (XBanner.this.mOnItemClickListener != null && XBanner.this.mDatas.size() != 0)
        view.setOnClickListener(new OnDoubleClickListener() {
              public void onNoDoubleClick(View param2View) {
                XBanner.this.mOnItemClickListener.onItemClick(XBanner.this, XBanner.this.mDatas.get(realPosition), param2View, realPosition);
              }
            }); 
      if (XBanner.this.mAdapter != null && XBanner.this.mDatas.size() != 0) {
        XBanner.XBannerAdapter xBannerAdapter = XBanner.this.mAdapter;
        XBanner xBanner = XBanner.this;
        xBannerAdapter.loadBanner(xBanner, xBanner.mDatas.get(i), view, i);
      } 
      ViewParent viewParent = view.getParent();
      if (viewParent != null)
        ((ViewGroup)viewParent).removeView(view); 
      param1ViewGroup.addView(view);
      return view;
    }
    
    public boolean isViewFromObject(View param1View, Object param1Object) {
      boolean bool;
      if (param1View == param1Object) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
  }
  
  class null extends OnDoubleClickListener {
    public void onNoDoubleClick(View param1View) {
      XBanner.this.mOnItemClickListener.onItemClick(XBanner.this, XBanner.this.mDatas.get(realPosition), param1View, realPosition);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/stx/xhb/xbanner/XBanner.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */