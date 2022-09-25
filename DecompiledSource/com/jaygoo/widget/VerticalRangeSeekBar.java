package com.jaygoo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class VerticalRangeSeekBar extends RangeSeekBar {
  public static final int DIRECTION_LEFT = 1;
  
  public static final int DIRECTION_RIGHT = 2;
  
  public static final int TEXT_DIRECTION_HORIZONTAL = 2;
  
  public static final int TEXT_DIRECTION_VERTICAL = 1;
  
  private int maxTickMarkWidth;
  
  private int orientation = 1;
  
  private int tickMarkDirection = 1;
  
  public VerticalRangeSeekBar(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public VerticalRangeSeekBar(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    initAttrs(paramAttributeSet);
    initSeekBar(paramAttributeSet);
  }
  
  private void initAttrs(AttributeSet paramAttributeSet) {
    try {
      TypedArray typedArray = getContext().obtainStyledAttributes(paramAttributeSet, R.styleable.VerticalRangeSeekBar);
      this.orientation = typedArray.getInt(R.styleable.VerticalRangeSeekBar_rsb_orientation, 1);
      this.tickMarkDirection = typedArray.getInt(R.styleable.VerticalRangeSeekBar_rsb_tick_mark_orientation, 1);
      typedArray.recycle();
    } catch (Exception exception) {
      exception.printStackTrace();
    } 
  }
  
  protected float getEventX(MotionEvent paramMotionEvent) {
    return (this.orientation == 1) ? (getHeight() - paramMotionEvent.getY()) : paramMotionEvent.getY();
  }
  
  protected float getEventY(MotionEvent paramMotionEvent) {
    return (this.orientation == 1) ? paramMotionEvent.getX() : (-paramMotionEvent.getX() + getWidth());
  }
  
  public VerticalSeekBar getLeftSeekBar() {
    return (VerticalSeekBar)this.leftSB;
  }
  
  public int getOrientation() {
    return this.orientation;
  }
  
  public VerticalSeekBar getRightSeekBar() {
    return (VerticalSeekBar)this.rightSB;
  }
  
  public int getTickMarkDirection() {
    return this.tickMarkDirection;
  }
  
  protected int getTickMarkRawHeight() {
    if (this.maxTickMarkWidth > 0) {
      int i = getTickMarkTextMargin();
      int j = this.maxTickMarkWidth;
      return i + j;
    } 
    if (getTickMarkTextArray() != null && (getTickMarkTextArray()).length > 0) {
      int j = (getTickMarkTextArray()).length;
      this.maxTickMarkWidth = Utils.measureText(String.valueOf(getTickMarkTextArray()[0]), getTickMarkTextSize()).width();
      int i;
      for (i = 1; i < j; i++) {
        int k = Utils.measureText(String.valueOf(getTickMarkTextArray()[i]), getTickMarkTextSize()).width();
        if (this.maxTickMarkWidth < k)
          this.maxTickMarkWidth = k; 
      } 
      i = getTickMarkTextMargin();
      j = this.maxTickMarkWidth;
      return i + j;
    } 
    return 0;
  }
  
  protected void initSeekBar(AttributeSet paramAttributeSet) {
    boolean bool = true;
    this.leftSB = new VerticalSeekBar(this, paramAttributeSet, true);
    this.rightSB = new VerticalSeekBar(this, paramAttributeSet, false);
    SeekBar seekBar = this.rightSB;
    if (getSeekBarMode() == 1)
      bool = false; 
    seekBar.setVisible(bool);
  }
  
  protected void onDraw(Canvas paramCanvas) {
    if (this.orientation == 1) {
      paramCanvas.rotate(-90.0F);
      paramCanvas.translate(-getHeight(), 0.0F);
    } else {
      paramCanvas.rotate(90.0F);
      paramCanvas.translate(0.0F, -getWidth());
    } 
    super.onDraw(paramCanvas);
  }
  
  protected void onDrawTickMark(Canvas paramCanvas, Paint paramPaint) {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual getTickMarkTextArray : ()[Ljava/lang/CharSequence;
    //   4: ifnull -> 446
    //   7: aload_0
    //   8: invokevirtual getTickMarkTextArray : ()[Ljava/lang/CharSequence;
    //   11: arraylength
    //   12: istore_3
    //   13: aload_0
    //   14: invokevirtual getProgressWidth : ()I
    //   17: iload_3
    //   18: iconst_1
    //   19: isub
    //   20: idiv
    //   21: istore #4
    //   23: iconst_0
    //   24: istore #5
    //   26: iload #5
    //   28: iload_3
    //   29: if_icmpge -> 446
    //   32: aload_0
    //   33: invokevirtual getTickMarkTextArray : ()[Ljava/lang/CharSequence;
    //   36: iload #5
    //   38: aaload
    //   39: invokeinterface toString : ()Ljava/lang/String;
    //   44: astore #6
    //   46: aload #6
    //   48: invokestatic isEmpty : (Ljava/lang/CharSequence;)Z
    //   51: ifeq -> 57
    //   54: goto -> 440
    //   57: aload_2
    //   58: aload #6
    //   60: iconst_0
    //   61: aload #6
    //   63: invokevirtual length : ()I
    //   66: aload_0
    //   67: getfield tickMarkTextRect : Landroid/graphics/Rect;
    //   70: invokevirtual getTextBounds : (Ljava/lang/String;IILandroid/graphics/Rect;)V
    //   73: aload_2
    //   74: aload_0
    //   75: invokevirtual getTickMarkTextColor : ()I
    //   78: invokevirtual setColor : (I)V
    //   81: aload_0
    //   82: invokevirtual getTickMarkMode : ()I
    //   85: iconst_1
    //   86: if_icmpne -> 175
    //   89: aload_0
    //   90: invokevirtual getTickMarkGravity : ()I
    //   93: iconst_2
    //   94: if_icmpne -> 125
    //   97: aload_0
    //   98: invokevirtual getProgressLeft : ()I
    //   101: iload #5
    //   103: iload #4
    //   105: imul
    //   106: iadd
    //   107: aload_0
    //   108: getfield tickMarkTextRect : Landroid/graphics/Rect;
    //   111: invokevirtual width : ()I
    //   114: isub
    //   115: istore #7
    //   117: iload #7
    //   119: i2f
    //   120: fstore #8
    //   122: goto -> 278
    //   125: aload_0
    //   126: invokevirtual getTickMarkGravity : ()I
    //   129: iconst_1
    //   130: if_icmpne -> 160
    //   133: aload_0
    //   134: invokevirtual getProgressLeft : ()I
    //   137: iload #5
    //   139: iload #4
    //   141: imul
    //   142: iadd
    //   143: i2f
    //   144: aload_0
    //   145: getfield tickMarkTextRect : Landroid/graphics/Rect;
    //   148: invokevirtual width : ()I
    //   151: i2f
    //   152: fconst_2
    //   153: fdiv
    //   154: fsub
    //   155: fstore #8
    //   157: goto -> 278
    //   160: aload_0
    //   161: invokevirtual getProgressLeft : ()I
    //   164: iload #5
    //   166: iload #4
    //   168: imul
    //   169: iadd
    //   170: istore #7
    //   172: goto -> 117
    //   175: aload #6
    //   177: invokestatic parseFloat : (Ljava/lang/String;)F
    //   180: fstore #8
    //   182: aload_0
    //   183: invokevirtual getRangeSeekBarState : ()[Lcom/jaygoo/widget/SeekBarState;
    //   186: astore #9
    //   188: fload #8
    //   190: aload #9
    //   192: iconst_0
    //   193: aaload
    //   194: getfield value : F
    //   197: invokestatic compareFloat : (FF)I
    //   200: iconst_m1
    //   201: if_icmpeq -> 236
    //   204: fload #8
    //   206: aload #9
    //   208: iconst_1
    //   209: aaload
    //   210: getfield value : F
    //   213: invokestatic compareFloat : (FF)I
    //   216: iconst_1
    //   217: if_icmpeq -> 236
    //   220: aload_0
    //   221: invokevirtual getSeekBarMode : ()I
    //   224: iconst_2
    //   225: if_icmpne -> 236
    //   228: aload_2
    //   229: aload_0
    //   230: invokevirtual getTickMarkInRangeTextColor : ()I
    //   233: invokevirtual setColor : (I)V
    //   236: aload_0
    //   237: invokevirtual getProgressLeft : ()I
    //   240: i2f
    //   241: aload_0
    //   242: invokevirtual getProgressWidth : ()I
    //   245: i2f
    //   246: fload #8
    //   248: aload_0
    //   249: invokevirtual getMinProgress : ()F
    //   252: fsub
    //   253: fmul
    //   254: aload_0
    //   255: invokevirtual getMaxProgress : ()F
    //   258: aload_0
    //   259: invokevirtual getMinProgress : ()F
    //   262: fsub
    //   263: fdiv
    //   264: fadd
    //   265: aload_0
    //   266: getfield tickMarkTextRect : Landroid/graphics/Rect;
    //   269: invokevirtual width : ()I
    //   272: i2f
    //   273: fconst_2
    //   274: fdiv
    //   275: fsub
    //   276: fstore #8
    //   278: aload_0
    //   279: invokevirtual getTickMarkLayoutGravity : ()I
    //   282: ifne -> 299
    //   285: aload_0
    //   286: invokevirtual getProgressTop : ()I
    //   289: aload_0
    //   290: invokevirtual getTickMarkTextMargin : ()I
    //   293: isub
    //   294: istore #7
    //   296: goto -> 318
    //   299: aload_0
    //   300: invokevirtual getProgressBottom : ()I
    //   303: aload_0
    //   304: invokevirtual getTickMarkTextMargin : ()I
    //   307: iadd
    //   308: aload_0
    //   309: getfield tickMarkTextRect : Landroid/graphics/Rect;
    //   312: invokevirtual height : ()I
    //   315: iadd
    //   316: istore #7
    //   318: iload #7
    //   320: i2f
    //   321: fstore #10
    //   323: aload_0
    //   324: getfield tickMarkTextRect : Landroid/graphics/Rect;
    //   327: invokevirtual width : ()I
    //   330: i2f
    //   331: fconst_2
    //   332: fdiv
    //   333: fload #8
    //   335: fadd
    //   336: fstore #11
    //   338: fload #10
    //   340: aload_0
    //   341: getfield tickMarkTextRect : Landroid/graphics/Rect;
    //   344: invokevirtual height : ()I
    //   347: i2f
    //   348: fconst_2
    //   349: fdiv
    //   350: fsub
    //   351: fstore #12
    //   353: aload_0
    //   354: getfield tickMarkDirection : I
    //   357: iconst_1
    //   358: if_icmpne -> 393
    //   361: aload_0
    //   362: getfield orientation : I
    //   365: istore #7
    //   367: iload #7
    //   369: iconst_1
    //   370: if_icmpne -> 380
    //   373: bipush #90
    //   375: istore #7
    //   377: goto -> 396
    //   380: iload #7
    //   382: iconst_2
    //   383: if_icmpne -> 393
    //   386: bipush #-90
    //   388: istore #7
    //   390: goto -> 396
    //   393: iconst_0
    //   394: istore #7
    //   396: iload #7
    //   398: ifeq -> 412
    //   401: aload_1
    //   402: iload #7
    //   404: i2f
    //   405: fload #11
    //   407: fload #12
    //   409: invokevirtual rotate : (FFF)V
    //   412: aload_1
    //   413: aload #6
    //   415: fload #8
    //   417: fload #10
    //   419: aload_2
    //   420: invokevirtual drawText : (Ljava/lang/String;FFLandroid/graphics/Paint;)V
    //   423: iload #7
    //   425: ifeq -> 440
    //   428: aload_1
    //   429: iload #7
    //   431: ineg
    //   432: i2f
    //   433: fload #11
    //   435: fload #12
    //   437: invokevirtual rotate : (FFF)V
    //   440: iinc #5, 1
    //   443: goto -> 26
    //   446: return
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    int i = View.MeasureSpec.getSize(paramInt1);
    paramInt1 = View.MeasureSpec.getMode(paramInt1);
    if (paramInt1 == 1073741824) {
      paramInt1 = View.MeasureSpec.makeMeasureSpec(i, 1073741824);
    } else if (paramInt1 == Integer.MIN_VALUE && getParent() instanceof ViewGroup && i == -1) {
      paramInt1 = View.MeasureSpec.makeMeasureSpec(((ViewGroup)getParent()).getMeasuredHeight(), -2147483648);
    } else {
      if (getGravity() == 2) {
        paramInt1 = getProgressTop() * 2 + getProgressHeight();
      } else {
        paramInt1 = (int)getRawHeight();
      } 
      paramInt1 = View.MeasureSpec.makeMeasureSpec(paramInt1, 1073741824);
    } 
    super.onMeasure(paramInt1, paramInt2);
  }
  
  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super.onSizeChanged(paramInt2, paramInt1, paramInt4, paramInt3);
  }
  
  public void setOrientation(int paramInt) {
    this.orientation = paramInt;
  }
  
  public void setTickMarkDirection(int paramInt) {
    this.tickMarkDirection = paramInt;
  }
  
  public void setTickMarkTextArray(CharSequence[] paramArrayOfCharSequence) {
    super.setTickMarkTextArray(paramArrayOfCharSequence);
    this.maxTickMarkWidth = 0;
  }
  
  public void setTickMarkTextSize(int paramInt) {
    super.setTickMarkTextSize(paramInt);
    this.maxTickMarkWidth = 0;
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface DirectionDef {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface TextDirectionDef {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/jaygoo/widget/VerticalRangeSeekBar.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */