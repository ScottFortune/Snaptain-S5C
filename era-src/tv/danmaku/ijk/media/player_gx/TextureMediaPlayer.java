package tv.danmaku.ijk.media.player_gx;

import android.graphics.SurfaceTexture;
import android.view.Surface;
import android.view.SurfaceHolder;

public class TextureMediaPlayer extends MediaPlayerProxy implements IMediaPlayer, ISurfaceTextureHolder {
  private SurfaceTexture mSurfaceTexture;
  
  private ISurfaceTextureHost mSurfaceTextureHost;
  
  public TextureMediaPlayer(IMediaPlayer paramIMediaPlayer) {
    super(paramIMediaPlayer);
  }
  
  public SurfaceTexture getSurfaceTexture() {
    return this.mSurfaceTexture;
  }
  
  public void release() {
    super.release();
    releaseSurfaceTexture();
  }
  
  public void releaseSurfaceTexture() {
    SurfaceTexture surfaceTexture = this.mSurfaceTexture;
    if (surfaceTexture != null) {
      ISurfaceTextureHost iSurfaceTextureHost = this.mSurfaceTextureHost;
      if (iSurfaceTextureHost != null) {
        iSurfaceTextureHost.releaseSurfaceTexture(surfaceTexture);
      } else {
        surfaceTexture.release();
      } 
      this.mSurfaceTexture = null;
    } 
  }
  
  public void reset() {
    super.reset();
    releaseSurfaceTexture();
  }
  
  public void setDisplay(SurfaceHolder paramSurfaceHolder) {
    if (this.mSurfaceTexture == null)
      super.setDisplay(paramSurfaceHolder); 
  }
  
  public void setSurface(Surface paramSurface) {
    if (this.mSurfaceTexture == null)
      super.setSurface(paramSurface); 
  }
  
  public void setSurfaceTexture(SurfaceTexture paramSurfaceTexture) {
    if (this.mSurfaceTexture == paramSurfaceTexture)
      return; 
    releaseSurfaceTexture();
    this.mSurfaceTexture = paramSurfaceTexture;
    if (paramSurfaceTexture == null) {
      super.setSurface(null);
    } else {
      super.setSurface(new Surface(paramSurfaceTexture));
    } 
  }
  
  public void setSurfaceTextureHost(ISurfaceTextureHost paramISurfaceTextureHost) {
    this.mSurfaceTextureHost = paramISurfaceTextureHost;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/tv/danmaku/ijk/media/player_gx/TextureMediaPlayer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */