package com.netopsun.ijkvideoview.widget.media;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import tv.danmaku.ijk.media.player_gx.IMediaPlayer;
import tv.danmaku.ijk.media.player_gx.ISurfaceTextureHolder;

public class SurfaceRenderView extends SurfaceView implements IRenderView {
  private MeasureHelper mMeasureHelper;
  
  private SurfaceCallback mSurfaceCallback;
  
  public SurfaceRenderView(Context paramContext) {
    super(paramContext);
    initView(paramContext);
  }
  
  public SurfaceRenderView(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    initView(paramContext);
  }
  
  public SurfaceRenderView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    initView(paramContext);
  }
  
  public SurfaceRenderView(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2) {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    initView(paramContext);
  }
  
  private void initView(Context paramContext) {
    this.mMeasureHelper = new MeasureHelper((View)this);
    this.mSurfaceCallback = new SurfaceCallback(this);
    getHolder().addCallback(this.mSurfaceCallback);
    getHolder().setType(0);
  }
  
  public void addRenderCallback(IRenderView.IRenderCallback paramIRenderCallback) {
    this.mSurfaceCallback.addRenderCallback(paramIRenderCallback);
  }
  
  public View getView() {
    return (View)this;
  }
  
  public void onInitializeAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent) {
    super.onInitializeAccessibilityEvent(paramAccessibilityEvent);
    paramAccessibilityEvent.setClassName(SurfaceRenderView.class.getName());
  }
  
  public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo paramAccessibilityNodeInfo) {
    super.onInitializeAccessibilityNodeInfo(paramAccessibilityNodeInfo);
    if (Build.VERSION.SDK_INT >= 14)
      paramAccessibilityNodeInfo.setClassName(SurfaceRenderView.class.getName()); 
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    this.mMeasureHelper.doMeasure(paramInt1, paramInt2);
    setMeasuredDimension(this.mMeasureHelper.getMeasuredWidth(), this.mMeasureHelper.getMeasuredHeight());
  }
  
  public void removeRenderCallback(IRenderView.IRenderCallback paramIRenderCallback) {
    this.mSurfaceCallback.removeRenderCallback(paramIRenderCallback);
  }
  
  public void setAspectRatio(int paramInt) {
    this.mMeasureHelper.setAspectRatio(paramInt);
    requestLayout();
  }
  
  public void setVideoRotation(int paramInt) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("SurfaceView doesn't support rotation (");
    stringBuilder.append(paramInt);
    stringBuilder.append(")!\n");
    Log.e("", stringBuilder.toString());
  }
  
  public void setVideoSampleAspectRatio(int paramInt1, int paramInt2) {
    if (paramInt1 > 0 && paramInt2 > 0) {
      this.mMeasureHelper.setVideoSampleAspectRatio(paramInt1, paramInt2);
      requestLayout();
    } 
  }
  
  public void setVideoSize(int paramInt1, int paramInt2) {
    if (paramInt1 > 0 && paramInt2 > 0) {
      this.mMeasureHelper.setVideoSize(paramInt1, paramInt2);
      getHolder().setFixedSize(paramInt1, paramInt2);
      requestLayout();
    } 
  }
  
  public boolean shouldWaitForResize() {
    return true;
  }
  
  private static final class InternalSurfaceHolder implements IRenderView.ISurfaceHolder {
    private SurfaceHolder mSurfaceHolder;
    
    private SurfaceRenderView mSurfaceView;
    
    public InternalSurfaceHolder(SurfaceRenderView param1SurfaceRenderView, SurfaceHolder param1SurfaceHolder) {
      this.mSurfaceView = param1SurfaceRenderView;
      this.mSurfaceHolder = param1SurfaceHolder;
    }
    
    public void bindToMediaPlayer(IMediaPlayer param1IMediaPlayer) {
      if (param1IMediaPlayer != null) {
        if (Build.VERSION.SDK_INT >= 16 && param1IMediaPlayer instanceof ISurfaceTextureHolder)
          ((ISurfaceTextureHolder)param1IMediaPlayer).setSurfaceTexture(null); 
        param1IMediaPlayer.setDisplay(this.mSurfaceHolder);
      } 
    }
    
    public IRenderView getRenderView() {
      return this.mSurfaceView;
    }
    
    public SurfaceHolder getSurfaceHolder() {
      return this.mSurfaceHolder;
    }
    
    public SurfaceTexture getSurfaceTexture() {
      return null;
    }
    
    public Surface openSurface() {
      SurfaceHolder surfaceHolder = this.mSurfaceHolder;
      return (surfaceHolder == null) ? null : surfaceHolder.getSurface();
    }
  }
  
  private static final class SurfaceCallback implements SurfaceHolder.Callback {
    private int mFormat;
    
    private int mHeight;
    
    private boolean mIsFormatChanged;
    
    private Map<IRenderView.IRenderCallback, Object> mRenderCallbackMap = new ConcurrentHashMap<IRenderView.IRenderCallback, Object>();
    
    private SurfaceHolder mSurfaceHolder;
    
    private WeakReference<SurfaceRenderView> mWeakSurfaceView;
    
    private int mWidth;
    
    public SurfaceCallback(SurfaceRenderView param1SurfaceRenderView) {
      this.mWeakSurfaceView = new WeakReference<SurfaceRenderView>(param1SurfaceRenderView);
    }
    
    public void addRenderCallback(IRenderView.IRenderCallback param1IRenderCallback) {
      SurfaceRenderView.InternalSurfaceHolder internalSurfaceHolder;
      this.mRenderCallbackMap.put(param1IRenderCallback, param1IRenderCallback);
      if (this.mSurfaceHolder != null) {
        internalSurfaceHolder = new SurfaceRenderView.InternalSurfaceHolder(this.mWeakSurfaceView.get(), this.mSurfaceHolder);
        param1IRenderCallback.onSurfaceCreated(internalSurfaceHolder, this.mWidth, this.mHeight);
      } else {
        internalSurfaceHolder = null;
      } 
      if (this.mIsFormatChanged) {
        SurfaceRenderView.InternalSurfaceHolder internalSurfaceHolder1 = internalSurfaceHolder;
        if (internalSurfaceHolder == null)
          internalSurfaceHolder1 = new SurfaceRenderView.InternalSurfaceHolder(this.mWeakSurfaceView.get(), this.mSurfaceHolder); 
        param1IRenderCallback.onSurfaceChanged(internalSurfaceHolder1, this.mFormat, this.mWidth, this.mHeight);
      } 
    }
    
    public void removeRenderCallback(IRenderView.IRenderCallback param1IRenderCallback) {
      this.mRenderCallbackMap.remove(param1IRenderCallback);
    }
    
    public void surfaceChanged(SurfaceHolder param1SurfaceHolder, int param1Int1, int param1Int2, int param1Int3) {
      this.mSurfaceHolder = param1SurfaceHolder;
      this.mIsFormatChanged = true;
      this.mFormat = param1Int1;
      this.mWidth = param1Int2;
      this.mHeight = param1Int3;
      SurfaceRenderView.InternalSurfaceHolder internalSurfaceHolder = new SurfaceRenderView.InternalSurfaceHolder(this.mWeakSurfaceView.get(), this.mSurfaceHolder);
      Iterator<IRenderView.IRenderCallback> iterator = this.mRenderCallbackMap.keySet().iterator();
      while (iterator.hasNext())
        ((IRenderView.IRenderCallback)iterator.next()).onSurfaceChanged(internalSurfaceHolder, param1Int1, param1Int2, param1Int3); 
    }
    
    public void surfaceCreated(SurfaceHolder param1SurfaceHolder) {
      this.mSurfaceHolder = param1SurfaceHolder;
      this.mIsFormatChanged = false;
      this.mFormat = 0;
      this.mWidth = 0;
      this.mHeight = 0;
      SurfaceRenderView.InternalSurfaceHolder internalSurfaceHolder = new SurfaceRenderView.InternalSurfaceHolder(this.mWeakSurfaceView.get(), this.mSurfaceHolder);
      Iterator<IRenderView.IRenderCallback> iterator = this.mRenderCallbackMap.keySet().iterator();
      while (iterator.hasNext())
        ((IRenderView.IRenderCallback)iterator.next()).onSurfaceCreated(internalSurfaceHolder, 0, 0); 
    }
    
    public void surfaceDestroyed(SurfaceHolder param1SurfaceHolder) {
      this.mSurfaceHolder = null;
      this.mIsFormatChanged = false;
      this.mFormat = 0;
      this.mWidth = 0;
      this.mHeight = 0;
      SurfaceRenderView.InternalSurfaceHolder internalSurfaceHolder = new SurfaceRenderView.InternalSurfaceHolder(this.mWeakSurfaceView.get(), this.mSurfaceHolder);
      Iterator<IRenderView.IRenderCallback> iterator = this.mRenderCallbackMap.keySet().iterator();
      while (iterator.hasNext())
        ((IRenderView.IRenderCallback)iterator.next()).onSurfaceDestroyed(internalSurfaceHolder); 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/ijkvideoview/widget/media/SurfaceRenderView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */