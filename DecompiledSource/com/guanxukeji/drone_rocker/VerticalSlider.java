package com.guanxukeji.drone_rocker;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class VerticalSlider extends FrameLayout {
  private int currentSliderLevel = 0;
  
  private boolean isLayout;
  
  private ImageView mSliderArrowDown;
  
  private ImageView mSliderArrowUp;
  
  private ImageView mSliderBackground;
  
  private ImageView mSliderMidpoint;
  
  private int maxSliderLevel = 25;
  
  private VerticalSliderMidpointChangeListener midpointChangelistener;
  
  private int slideEndOffset = -30;
  
  public VerticalSlider(Context paramContext) {
    super(paramContext);
    init((AttributeSet)null);
  }
  
  public VerticalSlider(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    init(paramAttributeSet);
  }
  
  public VerticalSlider(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    init(paramAttributeSet);
  }
  
  public VerticalSlider(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2) {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    init(paramAttributeSet);
  }
  
  private void init(AttributeSet paramAttributeSet) {
    inflate(getContext(), R.layout.slider_vertical, (ViewGroup)this);
    this.mSliderMidpoint = (ImageView)findViewById(R.id.slider_midpoint);
    this.mSliderArrowDown = (ImageView)findViewById(R.id.slider_arrow_down);
    this.mSliderBackground = (ImageView)findViewById(R.id.slider_background);
    this.mSliderArrowUp = (ImageView)findViewById(R.id.slider_arrow_up);
    if (paramAttributeSet != null) {
      TypedArray typedArray = getContext().obtainStyledAttributes(paramAttributeSet, R.styleable.DroneSlider);
      int i = typedArray.getInt(R.styleable.DroneSlider_slider_level, -1);
      if (i != -1)
        this.maxSliderLevel = i; 
      typedArray.recycle();
    } 
  }
  
  private void updateCurrentSliderLevel(int paramInt) {
    float f = (this.mSliderBackground.getHeight() + this.slideEndOffset * 2.0F) / this.maxSliderLevel * 2.0F;
    TranslateAnimation translateAnimation = new TranslateAnimation(0.0F, 0.0F, 0.0F, paramInt * f);
    translateAnimation.setDuration(100L);
    translateAnimation.setFillAfter(true);
    this.mSliderMidpoint.startAnimation((Animation)translateAnimation);
    VerticalSliderMidpointChangeListener verticalSliderMidpointChangeListener = this.midpointChangelistener;
    if (verticalSliderMidpointChangeListener != null)
      verticalSliderMidpointChangeListener.onSliderMidpointChange(false, -paramInt, this.maxSliderLevel); 
  }
  
  public int getCurrentSliderLevel() {
    return -this.currentSliderLevel;
  }
  
  public int getMaxSliderLevel() {
    return this.maxSliderLevel;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    this.isLayout = true;
    updateCurrentSliderLevel(this.currentSliderLevel);
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    // Byte code:
    //   0: aload_1
    //   1: invokevirtual getAction : ()I
    //   4: iconst_1
    //   5: if_icmpeq -> 10
    //   8: iconst_1
    //   9: ireturn
    //   10: aload_0
    //   11: getfield mSliderBackground : Landroid/widget/ImageView;
    //   14: invokevirtual getHeight : ()I
    //   17: i2f
    //   18: aload_0
    //   19: getfield slideEndOffset : I
    //   22: i2f
    //   23: fconst_2
    //   24: fmul
    //   25: fadd
    //   26: aload_0
    //   27: getfield maxSliderLevel : I
    //   30: i2f
    //   31: fconst_2
    //   32: fmul
    //   33: fdiv
    //   34: fstore_2
    //   35: aload_1
    //   36: invokevirtual getY : ()F
    //   39: aload_0
    //   40: getfield mSliderArrowUp : Landroid/widget/ImageView;
    //   43: invokevirtual getY : ()F
    //   46: aload_0
    //   47: getfield mSliderArrowUp : Landroid/widget/ImageView;
    //   50: invokevirtual getHeight : ()I
    //   53: i2f
    //   54: fadd
    //   55: fcmpg
    //   56: ifge -> 64
    //   59: iconst_m1
    //   60: istore_3
    //   61: goto -> 106
    //   64: aload_1
    //   65: invokevirtual getY : ()F
    //   68: aload_0
    //   69: getfield mSliderArrowDown : Landroid/widget/ImageView;
    //   72: invokevirtual getY : ()F
    //   75: fcmpl
    //   76: ifle -> 84
    //   79: iconst_1
    //   80: istore_3
    //   81: goto -> 106
    //   84: aload_1
    //   85: invokevirtual getY : ()F
    //   88: aload_0
    //   89: getfield mSliderMidpoint : Landroid/widget/ImageView;
    //   92: invokevirtual getY : ()F
    //   95: fsub
    //   96: aload_0
    //   97: getfield currentSliderLevel : I
    //   100: i2f
    //   101: fload_2
    //   102: fmul
    //   103: fsub
    //   104: f2i
    //   105: istore_3
    //   106: iload_3
    //   107: ifge -> 122
    //   110: aload_0
    //   111: getfield currentSliderLevel : I
    //   114: aload_0
    //   115: getfield maxSliderLevel : I
    //   118: ineg
    //   119: if_icmpeq -> 143
    //   122: iload_3
    //   123: istore #4
    //   125: iload_3
    //   126: ifle -> 146
    //   129: iload_3
    //   130: istore #4
    //   132: aload_0
    //   133: getfield currentSliderLevel : I
    //   136: aload_0
    //   137: getfield maxSliderLevel : I
    //   140: if_icmpne -> 146
    //   143: iconst_0
    //   144: istore #4
    //   146: aload_0
    //   147: getfield currentSliderLevel : I
    //   150: istore_3
    //   151: iload_3
    //   152: i2f
    //   153: fstore #5
    //   155: iload #4
    //   157: ifle -> 170
    //   160: aload_0
    //   161: iload_3
    //   162: iconst_1
    //   163: iadd
    //   164: putfield currentSliderLevel : I
    //   167: goto -> 182
    //   170: iload #4
    //   172: ifge -> 182
    //   175: aload_0
    //   176: iload_3
    //   177: iconst_1
    //   178: isub
    //   179: putfield currentSliderLevel : I
    //   182: new android/view/animation/TranslateAnimation
    //   185: dup
    //   186: fconst_0
    //   187: fconst_0
    //   188: fload #5
    //   190: fload_2
    //   191: fmul
    //   192: aload_0
    //   193: getfield currentSliderLevel : I
    //   196: i2f
    //   197: fload_2
    //   198: fmul
    //   199: invokespecial <init> : (FFFF)V
    //   202: astore_1
    //   203: aload_1
    //   204: ldc2_w 100
    //   207: invokevirtual setDuration : (J)V
    //   210: aload_1
    //   211: iconst_1
    //   212: invokevirtual setFillAfter : (Z)V
    //   215: aload_0
    //   216: getfield mSliderMidpoint : Landroid/widget/ImageView;
    //   219: aload_1
    //   220: invokevirtual startAnimation : (Landroid/view/animation/Animation;)V
    //   223: aload_0
    //   224: getfield midpointChangelistener : Lcom/guanxukeji/drone_rocker/VerticalSlider$VerticalSliderMidpointChangeListener;
    //   227: astore_1
    //   228: aload_1
    //   229: ifnull -> 248
    //   232: aload_1
    //   233: iconst_1
    //   234: aload_0
    //   235: getfield currentSliderLevel : I
    //   238: ineg
    //   239: aload_0
    //   240: getfield maxSliderLevel : I
    //   243: invokeinterface onSliderMidpointChange : (ZII)V
    //   248: iconst_1
    //   249: ireturn
  }
  
  public void setCurrentSliderLevel(int paramInt) {
    this.currentSliderLevel = -paramInt;
    if (this.isLayout)
      updateCurrentSliderLevel(paramInt); 
  }
  
  public void setMaxSliderLevel(int paramInt) {
    this.maxSliderLevel = paramInt;
  }
  
  public void setMidpoint(int paramInt) {
    this.mSliderMidpoint.setImageResource(paramInt);
  }
  
  public void setMidpoint(Drawable paramDrawable) {
    this.mSliderMidpoint.setImageDrawable(paramDrawable);
  }
  
  public void setMidpointChangelistener(VerticalSliderMidpointChangeListener paramVerticalSliderMidpointChangeListener) {
    this.midpointChangelistener = paramVerticalSliderMidpointChangeListener;
  }
  
  public void setSlideEndOffset(int paramInt) {
    this.slideEndOffset = paramInt;
  }
  
  protected static interface VerticalSliderMidpointChangeListener {
    void onSliderMidpointChange(boolean param1Boolean, int param1Int1, int param1Int2);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/guanxukeji/drone_rocker/VerticalSlider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */