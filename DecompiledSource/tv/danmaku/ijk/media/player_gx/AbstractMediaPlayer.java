package tv.danmaku.ijk.media.player_gx;

import tv.danmaku.ijk.media.player_gx.misc.IMediaDataSource;

public abstract class AbstractMediaPlayer implements IMediaPlayer {
  private IMediaPlayer.OnBufferingUpdateListener mOnBufferingUpdateListener;
  
  private IMediaPlayer.OnCompletionListener mOnCompletionListener;
  
  private IMediaPlayer.OnErrorListener mOnErrorListener;
  
  private IMediaPlayer.OnInfoListener mOnInfoListener;
  
  private IMediaPlayer.OnPreparedListener mOnPreparedListener;
  
  private IMediaPlayer.OnSeekCompleteListener mOnSeekCompleteListener;
  
  private IMediaPlayer.OnTimedTextListener mOnTimedTextListener;
  
  private IMediaPlayer.OnVideoSizeChangedListener mOnVideoSizeChangedListener;
  
  protected final void notifyOnBufferingUpdate(int paramInt) {
    IMediaPlayer.OnBufferingUpdateListener onBufferingUpdateListener = this.mOnBufferingUpdateListener;
    if (onBufferingUpdateListener != null)
      onBufferingUpdateListener.onBufferingUpdate(this, paramInt); 
  }
  
  protected final void notifyOnCompletion() {
    IMediaPlayer.OnCompletionListener onCompletionListener = this.mOnCompletionListener;
    if (onCompletionListener != null)
      onCompletionListener.onCompletion(this); 
  }
  
  protected final boolean notifyOnError(int paramInt1, int paramInt2) {
    boolean bool;
    IMediaPlayer.OnErrorListener onErrorListener = this.mOnErrorListener;
    if (onErrorListener != null && onErrorListener.onError(this, paramInt1, paramInt2)) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  protected final boolean notifyOnInfo(int paramInt1, int paramInt2) {
    boolean bool;
    IMediaPlayer.OnInfoListener onInfoListener = this.mOnInfoListener;
    if (onInfoListener != null && onInfoListener.onInfo(this, paramInt1, paramInt2)) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  protected final void notifyOnPrepared() {
    IMediaPlayer.OnPreparedListener onPreparedListener = this.mOnPreparedListener;
    if (onPreparedListener != null)
      onPreparedListener.onPrepared(this); 
  }
  
  protected final void notifyOnSeekComplete() {
    IMediaPlayer.OnSeekCompleteListener onSeekCompleteListener = this.mOnSeekCompleteListener;
    if (onSeekCompleteListener != null)
      onSeekCompleteListener.onSeekComplete(this); 
  }
  
  protected final void notifyOnTimedText(IjkTimedText paramIjkTimedText) {
    IMediaPlayer.OnTimedTextListener onTimedTextListener = this.mOnTimedTextListener;
    if (onTimedTextListener != null)
      onTimedTextListener.onTimedText(this, paramIjkTimedText); 
  }
  
  protected final void notifyOnVideoSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    IMediaPlayer.OnVideoSizeChangedListener onVideoSizeChangedListener = this.mOnVideoSizeChangedListener;
    if (onVideoSizeChangedListener != null)
      onVideoSizeChangedListener.onVideoSizeChanged(this, paramInt1, paramInt2, paramInt3, paramInt4); 
  }
  
  public void resetListeners() {
    this.mOnPreparedListener = null;
    this.mOnBufferingUpdateListener = null;
    this.mOnCompletionListener = null;
    this.mOnSeekCompleteListener = null;
    this.mOnVideoSizeChangedListener = null;
    this.mOnErrorListener = null;
    this.mOnInfoListener = null;
    this.mOnTimedTextListener = null;
  }
  
  public void setDataSource(IMediaDataSource paramIMediaDataSource) {
    throw new UnsupportedOperationException();
  }
  
  public final void setOnBufferingUpdateListener(IMediaPlayer.OnBufferingUpdateListener paramOnBufferingUpdateListener) {
    this.mOnBufferingUpdateListener = paramOnBufferingUpdateListener;
  }
  
  public final void setOnCompletionListener(IMediaPlayer.OnCompletionListener paramOnCompletionListener) {
    this.mOnCompletionListener = paramOnCompletionListener;
  }
  
  public final void setOnErrorListener(IMediaPlayer.OnErrorListener paramOnErrorListener) {
    this.mOnErrorListener = paramOnErrorListener;
  }
  
  public final void setOnInfoListener(IMediaPlayer.OnInfoListener paramOnInfoListener) {
    this.mOnInfoListener = paramOnInfoListener;
  }
  
  public final void setOnPreparedListener(IMediaPlayer.OnPreparedListener paramOnPreparedListener) {
    this.mOnPreparedListener = paramOnPreparedListener;
  }
  
  public final void setOnSeekCompleteListener(IMediaPlayer.OnSeekCompleteListener paramOnSeekCompleteListener) {
    this.mOnSeekCompleteListener = paramOnSeekCompleteListener;
  }
  
  public final void setOnTimedTextListener(IMediaPlayer.OnTimedTextListener paramOnTimedTextListener) {
    this.mOnTimedTextListener = paramOnTimedTextListener;
  }
  
  public final void setOnVideoSizeChangedListener(IMediaPlayer.OnVideoSizeChangedListener paramOnVideoSizeChangedListener) {
    this.mOnVideoSizeChangedListener = paramOnVideoSizeChangedListener;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/tv/danmaku/ijk/media/player_gx/AbstractMediaPlayer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */