package com.netopsun.ijkvideoview.widget.media;

import android.graphics.SurfaceTexture;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;
import tv.danmaku.ijk.media.player_gx.IMediaPlayer;

public interface IRenderView {
  public static final int AR_16_9_FIT_PARENT = 4;
  
  public static final int AR_4_3_FIT_PARENT = 5;
  
  public static final int AR_ASPECT_FILL_PARENT = 1;
  
  public static final int AR_ASPECT_FIT_PARENT = 0;
  
  public static final int AR_ASPECT_WRAP_CONTENT = 2;
  
  public static final int AR_MATCH_PARENT = 3;
  
  void addRenderCallback(IRenderCallback paramIRenderCallback);
  
  View getView();
  
  void removeRenderCallback(IRenderCallback paramIRenderCallback);
  
  void setAspectRatio(int paramInt);
  
  void setVideoRotation(int paramInt);
  
  void setVideoSampleAspectRatio(int paramInt1, int paramInt2);
  
  void setVideoSize(int paramInt1, int paramInt2);
  
  boolean shouldWaitForResize();
  
  public static interface IRenderCallback {
    void onSurfaceChanged(IRenderView.ISurfaceHolder param1ISurfaceHolder, int param1Int1, int param1Int2, int param1Int3);
    
    void onSurfaceCreated(IRenderView.ISurfaceHolder param1ISurfaceHolder, int param1Int1, int param1Int2);
    
    void onSurfaceDestroyed(IRenderView.ISurfaceHolder param1ISurfaceHolder);
  }
  
  public static interface ISurfaceHolder {
    void bindToMediaPlayer(IMediaPlayer param1IMediaPlayer);
    
    IRenderView getRenderView();
    
    SurfaceHolder getSurfaceHolder();
    
    SurfaceTexture getSurfaceTexture();
    
    Surface openSurface();
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/ijkvideoview/widget/media/IRenderView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */