package com.guanxukeji.drone_rocker;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class PTZControlSlider extends FrameLayout {
  private SliderChangeListener listener;
  
  private ImageView mSliderBackground;
  
  private ImageView mSliderDownHint;
  
  private ImageView mSliderUpHint;
  
  private ImageView mTouchMidpoint;
  
  private ImageView mVirtualMidpoint;
  
  private float touchMidpointFromY = 0.0F;
  
  public PTZControlSlider(Context paramContext) {
    super(paramContext);
    init((AttributeSet)null);
  }
  
  public PTZControlSlider(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    init(paramAttributeSet);
  }
  
  public PTZControlSlider(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    init(paramAttributeSet);
  }
  
  public PTZControlSlider(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2) {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    init(paramAttributeSet);
  }
  
  private void init(AttributeSet paramAttributeSet) {
    inflate(getContext(), R.layout.ptz_slider_panel, (ViewGroup)this);
    this.mSliderBackground = (ImageView)findViewById(R.id.slider_background);
    this.mVirtualMidpoint = (ImageView)findViewById(R.id.virtual_midpoint);
    this.mTouchMidpoint = (ImageView)findViewById(R.id.touch_midpoint);
    this.mSliderDownHint = (ImageView)findViewById(R.id.slider_down_hint);
    this.mSliderUpHint = (ImageView)findViewById(R.id.slider_up_hint);
  }
  
  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent) {
    super.onInterceptTouchEvent(paramMotionEvent);
    return true;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    // Byte code:
    //   0: aload_1
    //   1: invokevirtual getY : ()F
    //   4: aload_0
    //   5: getfield mTouchMidpoint : Landroid/widget/ImageView;
    //   8: invokevirtual getY : ()F
    //   11: fsub
    //   12: aload_0
    //   13: getfield mTouchMidpoint : Landroid/widget/ImageView;
    //   16: invokevirtual getHeight : ()I
    //   19: iconst_2
    //   20: idiv
    //   21: i2f
    //   22: fsub
    //   23: f2i
    //   24: i2f
    //   25: fstore_2
    //   26: fload_2
    //   27: aload_0
    //   28: invokevirtual getHeight : ()I
    //   31: iconst_2
    //   32: idiv
    //   33: aload_0
    //   34: getfield mTouchMidpoint : Landroid/widget/ImageView;
    //   37: invokevirtual getHeight : ()I
    //   40: iconst_2
    //   41: idiv
    //   42: isub
    //   43: i2f
    //   44: fcmpl
    //   45: ifle -> 72
    //   48: aload_0
    //   49: invokevirtual getHeight : ()I
    //   52: iconst_2
    //   53: idiv
    //   54: aload_0
    //   55: getfield mTouchMidpoint : Landroid/widget/ImageView;
    //   58: invokevirtual getHeight : ()I
    //   61: iconst_2
    //   62: idiv
    //   63: isub
    //   64: istore_3
    //   65: iload_3
    //   66: i2f
    //   67: fstore #4
    //   69: goto -> 119
    //   72: fload_2
    //   73: fstore #4
    //   75: fload_2
    //   76: aload_0
    //   77: invokevirtual getHeight : ()I
    //   80: ineg
    //   81: iconst_2
    //   82: idiv
    //   83: aload_0
    //   84: getfield mTouchMidpoint : Landroid/widget/ImageView;
    //   87: invokevirtual getHeight : ()I
    //   90: iconst_2
    //   91: idiv
    //   92: iadd
    //   93: i2f
    //   94: fcmpg
    //   95: ifge -> 119
    //   98: aload_0
    //   99: invokevirtual getHeight : ()I
    //   102: ineg
    //   103: iconst_2
    //   104: idiv
    //   105: aload_0
    //   106: getfield mTouchMidpoint : Landroid/widget/ImageView;
    //   109: invokevirtual getHeight : ()I
    //   112: iconst_2
    //   113: idiv
    //   114: iadd
    //   115: istore_3
    //   116: goto -> 65
    //   119: aload_1
    //   120: invokevirtual getAction : ()I
    //   123: istore_3
    //   124: iload_3
    //   125: ifeq -> 242
    //   128: iload_3
    //   129: iconst_1
    //   130: if_icmpeq -> 190
    //   133: iload_3
    //   134: iconst_2
    //   135: if_icmpeq -> 146
    //   138: iload_3
    //   139: iconst_3
    //   140: if_icmpeq -> 190
    //   143: goto -> 276
    //   146: aload_0
    //   147: getfield mTouchMidpoint : Landroid/widget/ImageView;
    //   150: invokevirtual clearAnimation : ()V
    //   153: new android/view/animation/TranslateAnimation
    //   156: dup
    //   157: fconst_0
    //   158: fconst_0
    //   159: aload_0
    //   160: getfield touchMidpointFromY : F
    //   163: fload #4
    //   165: invokespecial <init> : (FFFF)V
    //   168: astore_1
    //   169: aload_1
    //   170: lconst_1
    //   171: invokevirtual setDuration : (J)V
    //   174: aload_1
    //   175: iconst_1
    //   176: invokevirtual setFillAfter : (Z)V
    //   179: aload_0
    //   180: getfield mTouchMidpoint : Landroid/widget/ImageView;
    //   183: aload_1
    //   184: invokevirtual startAnimation : (Landroid/view/animation/Animation;)V
    //   187: goto -> 276
    //   190: new android/view/animation/TranslateAnimation
    //   193: dup
    //   194: fconst_0
    //   195: fconst_0
    //   196: aload_0
    //   197: getfield touchMidpointFromY : F
    //   200: fconst_0
    //   201: invokespecial <init> : (FFFF)V
    //   204: astore_1
    //   205: aload_1
    //   206: ldc2_w 200
    //   209: invokevirtual setDuration : (J)V
    //   212: aload_1
    //   213: new android/view/animation/OvershootInterpolator
    //   216: dup
    //   217: invokespecial <init> : ()V
    //   220: invokevirtual setInterpolator : (Landroid/view/animation/Interpolator;)V
    //   223: aload_1
    //   224: iconst_1
    //   225: invokevirtual setFillAfter : (Z)V
    //   228: aload_0
    //   229: getfield mTouchMidpoint : Landroid/widget/ImageView;
    //   232: aload_1
    //   233: invokevirtual startAnimation : (Landroid/view/animation/Animation;)V
    //   236: fconst_0
    //   237: fstore #4
    //   239: goto -> 276
    //   242: new android/view/animation/TranslateAnimation
    //   245: dup
    //   246: fconst_0
    //   247: fconst_0
    //   248: aload_0
    //   249: getfield touchMidpointFromY : F
    //   252: fload #4
    //   254: invokespecial <init> : (FFFF)V
    //   257: astore_1
    //   258: aload_1
    //   259: lconst_0
    //   260: invokevirtual setDuration : (J)V
    //   263: aload_1
    //   264: iconst_1
    //   265: invokevirtual setFillAfter : (Z)V
    //   268: aload_0
    //   269: getfield mTouchMidpoint : Landroid/widget/ImageView;
    //   272: aload_1
    //   273: invokevirtual startAnimation : (Landroid/view/animation/Animation;)V
    //   276: aload_0
    //   277: getfield listener : Lcom/guanxukeji/drone_rocker/PTZControlSlider$SliderChangeListener;
    //   280: astore_1
    //   281: aload_1
    //   282: ifnull -> 305
    //   285: aload_1
    //   286: fload #4
    //   288: fneg
    //   289: aload_0
    //   290: invokevirtual getHeight : ()I
    //   293: i2f
    //   294: fconst_2
    //   295: fdiv
    //   296: fdiv
    //   297: ldc 100.0
    //   299: fmul
    //   300: invokeinterface onTouchMidpointChange : (F)V
    //   305: fload #4
    //   307: fconst_0
    //   308: fcmpg
    //   309: ifge -> 331
    //   312: aload_0
    //   313: getfield mSliderUpHint : Landroid/widget/ImageView;
    //   316: iconst_0
    //   317: invokevirtual setVisibility : (I)V
    //   320: aload_0
    //   321: getfield mSliderDownHint : Landroid/widget/ImageView;
    //   324: iconst_4
    //   325: invokevirtual setVisibility : (I)V
    //   328: goto -> 373
    //   331: fload #4
    //   333: fconst_0
    //   334: fcmpl
    //   335: ifle -> 357
    //   338: aload_0
    //   339: getfield mSliderUpHint : Landroid/widget/ImageView;
    //   342: iconst_4
    //   343: invokevirtual setVisibility : (I)V
    //   346: aload_0
    //   347: getfield mSliderDownHint : Landroid/widget/ImageView;
    //   350: iconst_0
    //   351: invokevirtual setVisibility : (I)V
    //   354: goto -> 373
    //   357: aload_0
    //   358: getfield mSliderUpHint : Landroid/widget/ImageView;
    //   361: iconst_4
    //   362: invokevirtual setVisibility : (I)V
    //   365: aload_0
    //   366: getfield mSliderDownHint : Landroid/widget/ImageView;
    //   369: iconst_4
    //   370: invokevirtual setVisibility : (I)V
    //   373: aload_0
    //   374: fload #4
    //   376: putfield touchMidpointFromY : F
    //   379: iconst_1
    //   380: ireturn
  }
  
  public void setListener(SliderChangeListener paramSliderChangeListener) {
    this.listener = paramSliderChangeListener;
  }
  
  public void setSliderBackground(int paramInt) {
    this.mSliderBackground.setImageResource(paramInt);
  }
  
  public void setSliderBackground(Drawable paramDrawable) {
    this.mSliderBackground.setImageDrawable(paramDrawable);
  }
  
  public void setTouchMidpoint(int paramInt) {
    this.mTouchMidpoint.setImageResource(paramInt);
  }
  
  public void setTouchMidpoint(Drawable paramDrawable) {
    this.mTouchMidpoint.setImageDrawable(paramDrawable);
  }
  
  public void setVirtualMidpointPercentage(float paramFloat) {
    paramFloat = -getHeight() * (paramFloat - 100.0F) / 200.0F;
    this.mVirtualMidpoint.setY(paramFloat);
  }
  
  public void setVirtualMidpointVisibility(int paramInt) {
    this.mVirtualMidpoint.setVisibility(paramInt);
  }
  
  public static interface SliderChangeListener {
    void onTouchMidpointChange(float param1Float);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/guanxukeji/drone_rocker/PTZControlSlider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */