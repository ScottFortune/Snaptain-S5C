package com.guanxukeji.videobackgroundmusicpicker.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import com.guanxukeji.videobackgroundmusicpicker.R;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LyricView extends View {
  private final int MSG_PLAYER_HIDE = 343;
  
  private final int MSG_PLAYER_SLIDE = 344;
  
  private Rect mBtnBound;
  
  private int mBtnColor = Color.parseColor("#EFEFEF");
  
  private Paint mBtnPaint;
  
  private int mBtnWidth = 0;
  
  private OnPlayerClickListener mClickListener;
  
  private int mCurrentPlayLine = 0;
  
  private int mCurrentShowColor = Color.parseColor("#AAAAAA");
  
  private int mCurrentShowLine = 0;
  
  private int mDefaultColor = Color.parseColor("#FFFFFF");
  
  private String mDefaultHint = "暂无歌词";
  
  private int mDefaultMargin = 12;
  
  private String mDefaultTime = "00:00";
  
  private float mDownX;
  
  private float mDownY;
  
  private ValueAnimator mFlingAnimator;
  
  private int mHighLightColor = Color.parseColor("#4FC5C7");
  
  private int mHintColor = Color.parseColor("#FFFFFF");
  
  private int mIndicatorColor = Color.parseColor("#EFEFEF");
  
  private Paint mIndicatorPaint;
  
  private boolean mIndicatorShow = false;
  
  private boolean mIsMoved = false;
  
  private float mLastScrollY;
  
  private int mLineCount;
  
  private float mLineHeight;
  
  private float mLineSpace = 0.0F;
  
  private LyricInfo mLyricInfo;
  
  private final int mMaxDampingDistance = 360;
  
  private int mMinStartUpSpeed = 1600;
  
  private boolean mPlayable = false;
  
  private boolean mPlayerClick = false;
  
  private float mScrollY = 0.0F;
  
  private float mShaderWidth = 0.0F;
  
  private boolean mSliding = false;
  
  private Paint mTextPaint;
  
  private Rect mTimerBound;
  
  private boolean mTouchable = true;
  
  private boolean mUserTouch = false;
  
  private float mVelocity = 0.0F;
  
  private VelocityTracker mVelocityTracker;
  
  private int maximumFlingVelocity;
  
  Handler postman = new Handler() {
      public void handleMessage(Message param1Message) {
        super.handleMessage(param1Message);
        int i = param1Message.what;
        if (i != 343) {
          if (i != 344)
            return; 
        } else {
          LyricView.this.postman.sendEmptyMessageDelayed(344, 1200L);
          LyricView.access$702(LyricView.this, false);
          LyricView.this.invalidateView();
        } 
        LyricView lyricView = LyricView.this;
        lyricView.smoothScrollTo(lyricView.measureCurrentScrollY(lyricView.mCurrentPlayLine));
        LyricView.this.invalidateView();
      }
    };
  
  public LyricView(Context paramContext) {
    super(paramContext);
    initMyView(paramContext);
  }
  
  public LyricView(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    initMyView(paramContext);
  }
  
  public LyricView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    initMyView(paramContext);
  }
  
  private void actionCancel(MotionEvent paramMotionEvent) {
    releaseVelocityTracker();
  }
  
  private void actionDown(MotionEvent paramMotionEvent) {
    this.postman.removeMessages(344);
    this.postman.removeMessages(343);
    this.mLastScrollY = this.mScrollY;
    this.mDownX = paramMotionEvent.getX();
    this.mDownY = paramMotionEvent.getY();
    ValueAnimator valueAnimator = this.mFlingAnimator;
    if (valueAnimator != null) {
      valueAnimator.cancel();
      this.mFlingAnimator = null;
    } 
    setUserTouch(true);
    this.mIsMoved = false;
    this.mPlayerClick = false;
  }
  
  private void actionMove(MotionEvent paramMotionEvent) {
    if (scrollable()) {
      VelocityTracker velocityTracker = this.mVelocityTracker;
      velocityTracker.computeCurrentVelocity(1000, this.maximumFlingVelocity);
      float f1 = this.mLastScrollY + this.mDownY - paramMotionEvent.getY();
      float f2 = f1 - this.mLineCount * this.mLineHeight * 0.5F;
      float f3 = Math.abs(f2) - this.mLineCount * this.mLineHeight * 0.5F;
      float f4 = f1;
      if (f3 > 0.0F)
        f4 = f1 - measureDampingDistance(f3) * f2 / Math.abs(f2); 
      this.mScrollY = f4;
      this.mVelocity = velocityTracker.getYVelocity();
      measureCurrentLine();
      if (Math.abs(this.mVelocity) > 1.0F)
        this.mIsMoved = true; 
    } 
  }
  
  private void actionUp(MotionEvent paramMotionEvent) {
    releaseVelocityTracker();
    this.postman.sendEmptyMessageDelayed(343, 2400L);
    if (scrollable()) {
      setUserTouch(false);
      if (overScrolled() && this.mScrollY < 0.0F) {
        smoothScrollTo(0.0F);
        return;
      } 
      if (overScrolled()) {
        float f1 = this.mScrollY;
        float f2 = this.mLineHeight;
        int i = this.mLineCount;
        if (f1 > (i - 1) * f2) {
          smoothScrollTo(f2 * (i - 1));
          return;
        } 
      } 
      if (Math.abs(this.mVelocity) > this.mMinStartUpSpeed) {
        doFlingAnimator(this.mVelocity);
        return;
      } 
      if (this.mIndicatorShow && clickPlayer(paramMotionEvent) && this.mCurrentShowLine != this.mCurrentPlayLine) {
        this.mIndicatorShow = false;
        this.mPlayerClick = true;
        OnPlayerClickListener onPlayerClickListener = this.mClickListener;
        if (onPlayerClickListener != null)
          onPlayerClickListener.onPlayerClicked(((LineInfo)this.mLyricInfo.song_lines.get(this.mCurrentShowLine - 1)).start, ((LineInfo)this.mLyricInfo.song_lines.get(this.mCurrentShowLine - 1)).content); 
      } 
    } else {
      performClick();
    } 
  }
  
  private void analyzeLyric(LyricInfo paramLyricInfo, String paramString) {
    int i = paramString.indexOf("]");
    if (paramString != null && paramString.startsWith("[offset:")) {
      paramLyricInfo.song_offset = Long.parseLong(paramString.substring(8, i).trim());
      return;
    } 
    if (paramString != null && paramString.startsWith("[ti:")) {
      paramLyricInfo.song_title = paramString.substring(4, i).trim();
      return;
    } 
    if (paramString != null && paramString.startsWith("[ar:")) {
      paramLyricInfo.song_artist = paramString.substring(4, i).trim();
      return;
    } 
    if (paramString != null && paramString.startsWith("[al:")) {
      paramLyricInfo.song_album = paramString.substring(4, i).trim();
      return;
    } 
    if (paramString != null && paramString.startsWith("[by:"))
      return; 
    if (paramString != null && i == 9 && paramString.trim().length() > 10) {
      int j = paramString.lastIndexOf("]") + 1;
      String str = paramString.substring(j, paramString.length());
      i = 0;
      String[] arrayOfString = paramString.substring(0, j).replace("[", "-").replace("]", "-").split("-");
      j = arrayOfString.length;
      while (i < j) {
        String str1 = arrayOfString[i];
        if (str1.trim().length() != 0) {
          LineInfo lineInfo = new LineInfo();
          lineInfo.content = str;
          lineInfo.start = measureStartTimeMillis(str1);
          paramLyricInfo.song_lines.add(lineInfo);
        } 
        i++;
      } 
    } 
  }
  
  private boolean clickPlayer(MotionEvent paramMotionEvent) {
    Rect rect = this.mBtnBound;
    boolean bool1 = false;
    boolean bool2 = bool1;
    if (rect != null) {
      bool2 = bool1;
      if (this.mDownX > (rect.left - this.mDefaultMargin)) {
        bool2 = bool1;
        if (this.mDownX < (this.mBtnBound.right + this.mDefaultMargin)) {
          bool2 = bool1;
          if (this.mDownY > (this.mBtnBound.top - this.mDefaultMargin)) {
            bool2 = bool1;
            if (this.mDownY < (this.mBtnBound.bottom + this.mDefaultMargin)) {
              float f1 = paramMotionEvent.getX();
              float f2 = paramMotionEvent.getY();
              bool2 = bool1;
              if (f1 > (this.mBtnBound.left - this.mDefaultMargin)) {
                bool2 = bool1;
                if (f1 < (this.mBtnBound.right + this.mDefaultMargin)) {
                  bool2 = bool1;
                  if (f2 > (this.mBtnBound.top - this.mDefaultMargin)) {
                    bool2 = bool1;
                    if (f2 < (this.mBtnBound.bottom + this.mDefaultMargin))
                      bool2 = true; 
                  } 
                } 
              } 
            } 
          } 
        } 
      } 
    } 
    return bool2;
  }
  
  private void doFlingAnimator(float paramFloat) {
    float f = paramFloat / Math.abs(paramFloat);
    paramFloat = Math.min(Math.abs(paramFloat) * 0.05F, 640.0F);
    paramFloat = Math.min(Math.max(0.0F, this.mScrollY - f * paramFloat), (this.mLineCount - 1) * this.mLineHeight);
    this.mFlingAnimator = ValueAnimator.ofFloat(new float[] { this.mScrollY, paramFloat });
    this.mFlingAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
          public void onAnimationUpdate(ValueAnimator param1ValueAnimator) {
            LyricView.access$002(LyricView.this, ((Float)param1ValueAnimator.getAnimatedValue()).floatValue());
            LyricView.this.measureCurrentLine();
            LyricView.this.invalidateView();
          }
        });
    this.mFlingAnimator.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter() {
          public void onAnimationCancel(Animator param1Animator) {
            super.onAnimationCancel(param1Animator);
          }
          
          public void onAnimationEnd(Animator param1Animator) {
            super.onAnimationEnd(param1Animator);
            LyricView.access$502(LyricView.this, false);
          }
          
          public void onAnimationStart(Animator param1Animator) {
            super.onAnimationStart(param1Animator);
            LyricView lyricView = LyricView.this;
            LyricView.access$302(lyricView, (lyricView.mMinStartUpSpeed - 1));
            LyricView.access$502(LyricView.this, true);
          }
        });
    this.mFlingAnimator.setDuration(420L);
    this.mFlingAnimator.setInterpolator((TimeInterpolator)new DecelerateInterpolator());
    this.mFlingAnimator.start();
  }
  
  private void drawIndicator(Canvas paramCanvas) {
    float f;
    this.mIndicatorPaint.setColor(this.mIndicatorColor);
    this.mIndicatorPaint.setAlpha(128);
    this.mIndicatorPaint.setStyle(Paint.Style.FILL);
    paramCanvas.drawText(measureCurrentTime(), (getMeasuredWidth() - this.mTimerBound.width()), (getMeasuredHeight() + this.mTimerBound.height() - 6) * 0.5F, this.mIndicatorPaint);
    Path path = new Path();
    this.mIndicatorPaint.setStrokeWidth(2.0F);
    this.mIndicatorPaint.setStyle(Paint.Style.STROKE);
    this.mIndicatorPaint.setPathEffect((PathEffect)new DashPathEffect(new float[] { 20.0F, 10.0F }, 0.0F));
    if (this.mPlayable) {
      f = (this.mBtnBound.right + 24);
    } else {
      f = 24.0F;
    } 
    path.moveTo(f, getMeasuredHeight() * 0.5F);
    path.lineTo((getMeasuredWidth() - this.mTimerBound.width() - this.mTimerBound.width() - 36), getMeasuredHeight() * 0.5F);
    paramCanvas.drawPath(path, this.mIndicatorPaint);
  }
  
  private void drawPlayer(Canvas paramCanvas) {
    int i = this.mDefaultMargin;
    float f1 = getMeasuredHeight();
    int j = this.mBtnWidth;
    this.mBtnBound = new Rect(i, (int)(f1 * 0.5F - j * 0.5F), j + this.mDefaultMargin, (int)(getMeasuredHeight() * 0.5F + this.mBtnWidth * 0.5F));
    Path path = new Path();
    float f2 = this.mBtnBound.width() * 0.3F;
    double d = Math.pow(f2, 2.0D);
    f1 = 0.5F * f2;
    float f3 = (float)Math.sqrt(d - Math.pow(f1, 2.0D));
    path.moveTo(this.mBtnBound.centerX() - f1, this.mBtnBound.centerY() - f3);
    path.lineTo(this.mBtnBound.centerX() - f1, this.mBtnBound.centerY() + f3);
    path.lineTo(this.mBtnBound.centerX() + f2, this.mBtnBound.centerY());
    path.lineTo(this.mBtnBound.centerX() - f1, this.mBtnBound.centerY() - f3);
    this.mBtnPaint.setAlpha(128);
    paramCanvas.drawPath(path, this.mBtnPaint);
    paramCanvas.drawCircle(this.mBtnBound.centerX(), this.mBtnBound.centerY(), this.mBtnBound.width() * 0.48F, this.mBtnPaint);
  }
  
  private float getRawSize(int paramInt, float paramFloat) {
    Resources resources;
    Context context = getContext();
    if (context == null) {
      resources = Resources.getSystem();
    } else {
      resources = resources.getResources();
    } 
    return TypedValue.applyDimension(paramInt, paramFloat, resources.getDisplayMetrics());
  }
  
  private void initAllBounds() {
    setTextSize(15.0F);
    setLineSpace(12.0F);
    this.mBtnWidth = (int)getRawSize(2, 24.0F);
    this.mTimerBound = new Rect();
    Paint paint = this.mIndicatorPaint;
    String str = this.mDefaultTime;
    paint.getTextBounds(str, 0, str.length(), this.mTimerBound);
    measureLineHeight();
  }
  
  private void initAllPaints() {
    this.mTextPaint = new Paint();
    this.mTextPaint.setDither(true);
    this.mTextPaint.setAntiAlias(true);
    this.mTextPaint.setTextAlign(Paint.Align.CENTER);
    this.mIndicatorPaint = new Paint();
    this.mIndicatorPaint.setDither(true);
    this.mIndicatorPaint.setAntiAlias(true);
    this.mIndicatorPaint.setTextSize(getRawSize(2, 12.0F));
    this.mIndicatorPaint.setTextAlign(Paint.Align.CENTER);
    this.mBtnPaint = new Paint();
    this.mBtnPaint.setDither(true);
    this.mBtnPaint.setAntiAlias(true);
    this.mBtnPaint.setColor(this.mBtnColor);
    this.mBtnPaint.setStrokeWidth(3.0F);
    this.mBtnPaint.setStyle(Paint.Style.STROKE);
  }
  
  private void initMyView(Context paramContext) {
    this.maximumFlingVelocity = ViewConfiguration.get(paramContext).getScaledMaximumFlingVelocity();
    initAllPaints();
    initAllBounds();
    this.mDefaultHint = getContext().getString(R.string.no_lric);
  }
  
  private void invalidateView() {
    if (Looper.getMainLooper() == Looper.myLooper()) {
      invalidate();
    } else {
      postInvalidate();
    } 
  }
  
  private void measureCurrentLine() {
    float f1 = this.mScrollY;
    float f2 = this.mLineHeight;
    this.mCurrentShowLine = (int)((f1 + 0.5F * f2) / f2 + 1.0F);
  }
  
  private float measureCurrentScrollY(int paramInt) {
    return (paramInt - 1) * this.mLineHeight;
  }
  
  private String measureCurrentTime() {
    DecimalFormat decimalFormat = new DecimalFormat("00");
    if (this.mLyricInfo != null) {
      int i = this.mLineCount;
      if (i > 0) {
        int j = this.mCurrentShowLine;
        if (j - 1 < i && j > 0) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append(decimalFormat.format(((LineInfo)this.mLyricInfo.song_lines.get(this.mCurrentShowLine - 1)).start / 1000L / 60L));
          stringBuilder.append(":");
          stringBuilder.append(decimalFormat.format(((LineInfo)this.mLyricInfo.song_lines.get(this.mCurrentShowLine - 1)).start / 1000L % 60L));
          return stringBuilder.toString();
        } 
      } 
    } 
    if (this.mLyricInfo != null) {
      int i = this.mLineCount;
      if (i > 0 && this.mCurrentShowLine - 1 >= i) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(decimalFormat.format(((LineInfo)this.mLyricInfo.song_lines.get(this.mLineCount - 1)).start / 1000L / 60L));
        stringBuilder.append(":");
        stringBuilder.append(decimalFormat.format(((LineInfo)this.mLyricInfo.song_lines.get(this.mLineCount - 1)).start / 1000L % 60L));
        return stringBuilder.toString();
      } 
    } 
    if (this.mLyricInfo != null && this.mLineCount > 0 && this.mCurrentShowLine - 1 <= 0) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(decimalFormat.format(((LineInfo)this.mLyricInfo.song_lines.get(0)).start / 1000L / 60L));
      stringBuilder.append(":");
      stringBuilder.append(decimalFormat.format(((LineInfo)this.mLyricInfo.song_lines.get(0)).start / 1000L % 60L));
      return stringBuilder.toString();
    } 
    return this.mDefaultTime;
  }
  
  private float measureDampingDistance(float paramFloat) {
    if (paramFloat > 360.0F) {
      paramFloat = (paramFloat - 360.0F) * 0.72F + 216.00002F;
    } else {
      paramFloat *= 0.6F;
    } 
    return paramFloat;
  }
  
  private void measureLineHeight() {
    Rect rect = new Rect();
    Paint paint = this.mTextPaint;
    String str = this.mDefaultHint;
    paint.getTextBounds(str, 0, str.length(), rect);
    this.mLineHeight = rect.height() + this.mLineSpace;
  }
  
  private static long measureStartTimeMillis(String paramString) {
    String[] arrayOfString = paramString.replace('.', ':').split(":");
    if (arrayOfString.length >= 3) {
      int i = Integer.valueOf(arrayOfString[0]).intValue() * 60 * 1000 + Integer.valueOf(arrayOfString[1]).intValue() * 1000;
      int j = Integer.valueOf(arrayOfString[2]).intValue();
      return (i + j);
    } 
    if (arrayOfString.length >= 2) {
      int i = Integer.valueOf(arrayOfString[0]).intValue() * 1000;
      int j = Integer.valueOf(arrayOfString[1]).intValue();
      return (i + j);
    } 
    return 0L;
  }
  
  private boolean overScrolled() {
    null = scrollable();
    boolean bool = true;
    if (null) {
      float f = this.mScrollY;
      null = bool;
      if (f <= this.mLineHeight * (this.mLineCount - 1)) {
        if (f < 0.0F)
          return bool; 
      } else {
        return null;
      } 
    } 
    return false;
  }
  
  private void releaseVelocityTracker() {
    VelocityTracker velocityTracker = this.mVelocityTracker;
    if (velocityTracker != null) {
      velocityTracker.clear();
      this.mVelocityTracker.recycle();
      this.mVelocityTracker = null;
    } 
  }
  
  private void resetLyricInfo() {
    LyricInfo lyricInfo = this.mLyricInfo;
    if (lyricInfo != null) {
      if (lyricInfo.song_lines != null) {
        this.mLyricInfo.song_lines.clear();
        this.mLyricInfo.song_lines = null;
      } 
      this.mLyricInfo = null;
    } 
  }
  
  private void resetView() {
    this.mCurrentShowLine = 0;
    this.mCurrentPlayLine = 0;
    resetLyricInfo();
    invalidateView();
    this.mLineCount = 0;
    this.mScrollY = 0.0F;
  }
  
  private void scrollToCurrentTimeMillis(long paramLong) {
    boolean bool = scrollable();
    int i = 0;
    int j = 0;
    if (bool) {
      int k = this.mLineCount;
      int m = 0;
      i = j;
      while (true) {
        if (i < k) {
          LineInfo lineInfo = this.mLyricInfo.song_lines.get(i);
          if (lineInfo != null && lineInfo.start > paramLong)
            break; 
          j = this.mLineCount;
          if (i == j - 1)
            m = j; 
          i++;
          continue;
        } 
        i = m;
        break;
      } 
    } 
    if (this.mCurrentPlayLine != i && !this.mUserTouch && !this.mSliding && !this.mIndicatorShow) {
      this.mCurrentPlayLine = i;
      smoothScrollTo(measureCurrentScrollY(i));
    } else if (!this.mSliding && !this.mIndicatorShow) {
      this.mCurrentShowLine = i;
      this.mCurrentPlayLine = i;
    } 
  }
  
  private boolean scrollable() {
    boolean bool;
    LyricInfo lyricInfo = this.mLyricInfo;
    if (lyricInfo != null && lyricInfo.song_lines != null && this.mLyricInfo.song_lines.size() > 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private void setRawTextSize(float paramFloat) {
    if (paramFloat != this.mTextPaint.getTextSize()) {
      this.mTextPaint.setTextSize(paramFloat);
      measureLineHeight();
      this.mScrollY = measureCurrentScrollY(this.mCurrentPlayLine);
      invalidateView();
    } 
  }
  
  private void setUserTouch(boolean paramBoolean) {
    if (this.mUserTouch == paramBoolean)
      return; 
    this.mUserTouch = paramBoolean;
    if (paramBoolean)
      this.mIndicatorShow = paramBoolean; 
  }
  
  private void setupLyricResource(InputStream paramInputStream, String paramString) {
    if (paramInputStream != null) {
      try {
        LyricInfo lyricInfo = new LyricInfo();
        this(this);
        ArrayList<LineInfo> arrayList = new ArrayList();
        this();
        lyricInfo.song_lines = arrayList;
        InputStreamReader inputStreamReader = new InputStreamReader();
        this(paramInputStream, paramString);
        BufferedReader bufferedReader = new BufferedReader();
        this(inputStreamReader);
        while (true) {
          String str = bufferedReader.readLine();
          if (str != null) {
            analyzeLyric(lyricInfo, str);
            continue;
          } 
          List<LineInfo> list = lyricInfo.song_lines;
          sort sort = new sort();
          this(this);
          Collections.sort(list, sort);
          bufferedReader.close();
          paramInputStream.close();
          inputStreamReader.close();
          this.mLyricInfo = lyricInfo;
          this.mLineCount = this.mLyricInfo.song_lines.size();
          invalidateView();
          return;
        } 
      } catch (IOException iOException) {
        iOException.printStackTrace();
      } 
    } else {
      this.mDefaultHint = getContext().getString(R.string.no_lric);
      invalidateView();
    } 
  }
  
  private void smoothScrollTo(float paramFloat) {
    final ValueAnimator animator = ValueAnimator.ofFloat(new float[] { this.mScrollY, paramFloat });
    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
          public void onAnimationUpdate(ValueAnimator param1ValueAnimator) {
            if (LyricView.this.mUserTouch) {
              animator.cancel();
              return;
            } 
            LyricView.access$002(LyricView.this, ((Float)param1ValueAnimator.getAnimatedValue()).floatValue());
            LyricView.this.invalidateView();
          }
        });
    valueAnimator.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter() {
          public void onAnimationEnd(Animator param1Animator) {
            super.onAnimationEnd(param1Animator);
            LyricView.access$502(LyricView.this, false);
            LyricView.this.measureCurrentLine();
            LyricView.this.invalidateView();
          }
          
          public void onAnimationStart(Animator param1Animator) {
            super.onAnimationStart(param1Animator);
            LyricView.access$502(LyricView.this, true);
          }
        });
    valueAnimator.setDuration(640L);
    valueAnimator.setInterpolator((TimeInterpolator)new OvershootInterpolator(0.5F));
    valueAnimator.start();
  }
  
  public boolean dispatchTouchEvent(MotionEvent paramMotionEvent) {
    int i = paramMotionEvent.getAction();
    if (i != 0) {
      if (i != 1 && i == 2)
        getParent().requestDisallowInterceptTouchEvent(true); 
    } else {
      getParent().requestDisallowInterceptTouchEvent(true);
    } 
    return super.dispatchTouchEvent(paramMotionEvent);
  }
  
  public int getDefaultColor() {
    return this.mDefaultColor;
  }
  
  protected void onDraw(Canvas paramCanvas) {
    LyricInfo lyricInfo = this.mLyricInfo;
    if (lyricInfo != null && lyricInfo.song_lines != null && this.mLyricInfo.song_lines.size() > 0) {
      byte b = 0;
      int i = this.mLineCount;
      while (b < i) {
        float f1 = getMeasuredWidth();
        float f2 = getMeasuredHeight();
        float f3 = b;
        float f4 = this.mLineHeight;
        f2 = f2 * 0.5F + (f3 + 0.5F) * f4 - 6.0F - this.mLineSpace * 0.5F - this.mScrollY;
        if (f4 * 0.5F + f2 >= 0.0F) {
          if (f2 - f4 * 0.5F > getMeasuredHeight())
            break; 
          if (b == this.mCurrentPlayLine - 1) {
            this.mTextPaint.setColor(this.mHighLightColor);
          } else if (this.mIndicatorShow && b == this.mCurrentShowLine - 1) {
            this.mTextPaint.setColor(this.mCurrentShowColor);
          } else {
            this.mTextPaint.setColor(this.mDefaultColor);
          } 
          f3 = getMeasuredHeight();
          f4 = this.mShaderWidth;
          if (f2 > f3 - f4 || f2 < f4) {
            f4 = this.mShaderWidth;
            if (f2 < f4) {
              this.mTextPaint.setAlpha((int)(23000.0F * f2 / f4 * 0.01F) + 26);
            } else {
              this.mTextPaint.setAlpha((int)((getMeasuredHeight() - f2) * 23000.0F / this.mShaderWidth * 0.01F) + 26);
            } 
          } else {
            this.mTextPaint.setAlpha(255);
          } 
          paramCanvas.drawText(((LineInfo)this.mLyricInfo.song_lines.get(b)).content, f1 * 0.5F, f2, this.mTextPaint);
        } 
        b++;
      } 
    } else {
      this.mTextPaint.setColor(this.mHintColor);
      paramCanvas.drawText(this.mDefaultHint, getMeasuredWidth() * 0.5F, (getMeasuredHeight() + this.mLineHeight - 6.0F) * 0.5F, this.mTextPaint);
    } 
    if (this.mIndicatorShow && scrollable() && this.mPlayable) {
      drawPlayer(paramCanvas);
      drawIndicator(paramCanvas);
    } 
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    super.onMeasure(paramInt1, paramInt2);
    this.mShaderWidth = getMeasuredHeight() * 0.3F;
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    if (!this.mTouchable)
      return super.onTouchEvent(paramMotionEvent); 
    if (this.mVelocityTracker == null)
      this.mVelocityTracker = VelocityTracker.obtain(); 
    this.mVelocityTracker.addMovement(paramMotionEvent);
    int i = paramMotionEvent.getAction();
    if (i != 0) {
      if (i != 1) {
        if (i != 2) {
          if (i == 3)
            actionCancel(paramMotionEvent); 
        } else {
          actionMove(paramMotionEvent);
        } 
      } else {
        actionUp(paramMotionEvent);
      } 
    } else {
      actionDown(paramMotionEvent);
    } 
    invalidateView();
    return (this.mIsMoved || this.mPlayerClick) ? true : super.onTouchEvent(paramMotionEvent);
  }
  
  public void reset(String paramString) {
    this.mDefaultHint = paramString;
    resetView();
  }
  
  public void setCurrentTimeMillis(long paramLong) {
    scrollToCurrentTimeMillis(paramLong);
  }
  
  public void setDefaultColor(int paramInt) {
    if (this.mDefaultColor != paramInt) {
      this.mDefaultColor = paramInt;
      invalidateView();
    } 
  }
  
  public void setHighLightTextColor(int paramInt) {
    if (this.mHighLightColor != paramInt) {
      this.mHighLightColor = paramInt;
      invalidateView();
    } 
  }
  
  public void setHintColor(int paramInt) {
    if (this.mHintColor != paramInt) {
      this.mHintColor = paramInt;
      invalidate();
    } 
  }
  
  public void setLineSpace(float paramFloat) {
    if (this.mLineSpace != paramFloat) {
      this.mLineSpace = getRawSize(2, paramFloat);
      measureLineHeight();
      this.mScrollY = measureCurrentScrollY(this.mCurrentPlayLine);
      invalidateView();
    } 
  }
  
  public void setLyricFile(String paramString1, String paramString2) {
    String str = paramString1;
    if (paramString1 == null)
      str = ""; 
    File file = new File(str);
    if (str.contains("file:///android_asset/")) {
      String str1 = str.replace("file:///android_asset/", "");
      try {
        setupLyricResource(getResources().getAssets().open(str1), paramString2);
      } catch (IOException iOException) {
        iOException.printStackTrace();
      } 
    } else if (iOException.exists()) {
      try {
        FileInputStream fileInputStream = new FileInputStream();
        this((File)iOException);
        setupLyricResource(fileInputStream, paramString2);
      } catch (FileNotFoundException fileNotFoundException) {
        fileNotFoundException.printStackTrace();
      } 
    } else {
      this.mDefaultHint = getContext().getString(R.string.no_lric);
      invalidateView();
    } 
  }
  
  public void setOnPlayerClickListener(OnPlayerClickListener paramOnPlayerClickListener) {
    this.mClickListener = paramOnPlayerClickListener;
  }
  
  public void setPlayable(boolean paramBoolean) {
    this.mPlayable = paramBoolean;
  }
  
  public void setTextSize(float paramFloat) {
    setTextSize(2, paramFloat);
  }
  
  public void setTextSize(int paramInt, float paramFloat) {
    setRawTextSize(getRawSize(paramInt, paramFloat));
  }
  
  public void setTouchable(boolean paramBoolean) {
    this.mTouchable = paramBoolean;
  }
  
  class LineInfo {
    String content;
    
    long start;
  }
  
  class LyricInfo {
    String song_album;
    
    String song_artist;
    
    List<LyricView.LineInfo> song_lines;
    
    long song_offset;
    
    String song_title;
  }
  
  public static interface OnPlayerClickListener {
    void onPlayerClicked(long param1Long, String param1String);
  }
  
  class sort implements Comparator<LineInfo> {
    public int compare(LyricView.LineInfo param1LineInfo1, LyricView.LineInfo param1LineInfo2) {
      return (param1LineInfo1.start < param1LineInfo2.start) ? -1 : ((param1LineInfo1.start > param1LineInfo2.start) ? 1 : 0);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/guanxukeji/videobackgroundmusicpicker/widget/LyricView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */