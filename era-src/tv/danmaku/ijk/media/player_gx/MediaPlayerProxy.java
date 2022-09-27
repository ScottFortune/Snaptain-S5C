package tv.danmaku.ijk.media.player_gx;

import android.content.Context;
import android.net.Uri;
import android.view.Surface;
import android.view.SurfaceHolder;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.Map;
import tv.danmaku.ijk.media.player_gx.misc.IMediaDataSource;
import tv.danmaku.ijk.media.player_gx.misc.ITrackInfo;

public class MediaPlayerProxy implements IMediaPlayer {
  protected final IMediaPlayer mBackEndMediaPlayer;
  
  public MediaPlayerProxy(IMediaPlayer paramIMediaPlayer) {
    this.mBackEndMediaPlayer = paramIMediaPlayer;
  }
  
  public int getAudioSessionId() {
    return this.mBackEndMediaPlayer.getAudioSessionId();
  }
  
  public long getCurrentPosition() {
    return this.mBackEndMediaPlayer.getCurrentPosition();
  }
  
  public String getDataSource() {
    return this.mBackEndMediaPlayer.getDataSource();
  }
  
  public long getDuration() {
    return this.mBackEndMediaPlayer.getDuration();
  }
  
  public IMediaPlayer getInternalMediaPlayer() {
    return this.mBackEndMediaPlayer;
  }
  
  public MediaInfo getMediaInfo() {
    return this.mBackEndMediaPlayer.getMediaInfo();
  }
  
  public ITrackInfo[] getTrackInfo() {
    return this.mBackEndMediaPlayer.getTrackInfo();
  }
  
  public int getVideoHeight() {
    return this.mBackEndMediaPlayer.getVideoHeight();
  }
  
  public int getVideoSarDen() {
    return this.mBackEndMediaPlayer.getVideoSarDen();
  }
  
  public int getVideoSarNum() {
    return this.mBackEndMediaPlayer.getVideoSarNum();
  }
  
  public int getVideoWidth() {
    return this.mBackEndMediaPlayer.getVideoWidth();
  }
  
  public boolean isLooping() {
    return this.mBackEndMediaPlayer.isLooping();
  }
  
  public boolean isPlayable() {
    return false;
  }
  
  public boolean isPlaying() {
    return this.mBackEndMediaPlayer.isPlaying();
  }
  
  public void pause() throws IllegalStateException {
    this.mBackEndMediaPlayer.pause();
  }
  
  public void prepareAsync() throws IllegalStateException {
    this.mBackEndMediaPlayer.prepareAsync();
  }
  
  public void release() {
    this.mBackEndMediaPlayer.release();
  }
  
  public void reset() {
    this.mBackEndMediaPlayer.reset();
  }
  
  public void seekTo(long paramLong) throws IllegalStateException {
    this.mBackEndMediaPlayer.seekTo(paramLong);
  }
  
  public void setAudioStreamType(int paramInt) {
    this.mBackEndMediaPlayer.setAudioStreamType(paramInt);
  }
  
  public void setDataSource(Context paramContext, Uri paramUri) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
    this.mBackEndMediaPlayer.setDataSource(paramContext, paramUri);
  }
  
  public void setDataSource(Context paramContext, Uri paramUri, Map<String, String> paramMap) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
    this.mBackEndMediaPlayer.setDataSource(paramContext, paramUri, paramMap);
  }
  
  public void setDataSource(FileDescriptor paramFileDescriptor) throws IOException, IllegalArgumentException, IllegalStateException {
    this.mBackEndMediaPlayer.setDataSource(paramFileDescriptor);
  }
  
  public void setDataSource(String paramString) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
    this.mBackEndMediaPlayer.setDataSource(paramString);
  }
  
  public void setDataSource(IMediaDataSource paramIMediaDataSource) {
    this.mBackEndMediaPlayer.setDataSource(paramIMediaDataSource);
  }
  
  public void setDisplay(SurfaceHolder paramSurfaceHolder) {
    this.mBackEndMediaPlayer.setDisplay(paramSurfaceHolder);
  }
  
  public void setKeepInBackground(boolean paramBoolean) {
    this.mBackEndMediaPlayer.setKeepInBackground(paramBoolean);
  }
  
  public void setLogEnabled(boolean paramBoolean) {}
  
  public void setLooping(boolean paramBoolean) {
    this.mBackEndMediaPlayer.setLooping(paramBoolean);
  }
  
  public void setOnBufferingUpdateListener(final IMediaPlayer.OnBufferingUpdateListener finalListener) {
    if (finalListener != null) {
      this.mBackEndMediaPlayer.setOnBufferingUpdateListener(new IMediaPlayer.OnBufferingUpdateListener() {
            public void onBufferingUpdate(IMediaPlayer param1IMediaPlayer, int param1Int) {
              finalListener.onBufferingUpdate(MediaPlayerProxy.this, param1Int);
            }
          });
    } else {
      this.mBackEndMediaPlayer.setOnBufferingUpdateListener(null);
    } 
  }
  
  public void setOnCompletionListener(final IMediaPlayer.OnCompletionListener finalListener) {
    if (finalListener != null) {
      this.mBackEndMediaPlayer.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
            public void onCompletion(IMediaPlayer param1IMediaPlayer) {
              finalListener.onCompletion(MediaPlayerProxy.this);
            }
          });
    } else {
      this.mBackEndMediaPlayer.setOnCompletionListener(null);
    } 
  }
  
  public void setOnErrorListener(final IMediaPlayer.OnErrorListener finalListener) {
    if (finalListener != null) {
      this.mBackEndMediaPlayer.setOnErrorListener(new IMediaPlayer.OnErrorListener() {
            public boolean onError(IMediaPlayer param1IMediaPlayer, int param1Int1, int param1Int2) {
              return finalListener.onError(MediaPlayerProxy.this, param1Int1, param1Int2);
            }
          });
    } else {
      this.mBackEndMediaPlayer.setOnErrorListener(null);
    } 
  }
  
  public void setOnInfoListener(final IMediaPlayer.OnInfoListener finalListener) {
    if (finalListener != null) {
      this.mBackEndMediaPlayer.setOnInfoListener(new IMediaPlayer.OnInfoListener() {
            public boolean onInfo(IMediaPlayer param1IMediaPlayer, int param1Int1, int param1Int2) {
              return finalListener.onInfo(MediaPlayerProxy.this, param1Int1, param1Int2);
            }
          });
    } else {
      this.mBackEndMediaPlayer.setOnInfoListener(null);
    } 
  }
  
  public void setOnPreparedListener(final IMediaPlayer.OnPreparedListener finalListener) {
    if (finalListener != null) {
      this.mBackEndMediaPlayer.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            public void onPrepared(IMediaPlayer param1IMediaPlayer) {
              finalListener.onPrepared(MediaPlayerProxy.this);
            }
          });
    } else {
      this.mBackEndMediaPlayer.setOnPreparedListener(null);
    } 
  }
  
  public void setOnSeekCompleteListener(final IMediaPlayer.OnSeekCompleteListener finalListener) {
    if (finalListener != null) {
      this.mBackEndMediaPlayer.setOnSeekCompleteListener(new IMediaPlayer.OnSeekCompleteListener() {
            public void onSeekComplete(IMediaPlayer param1IMediaPlayer) {
              finalListener.onSeekComplete(MediaPlayerProxy.this);
            }
          });
    } else {
      this.mBackEndMediaPlayer.setOnSeekCompleteListener(null);
    } 
  }
  
  public void setOnTimedTextListener(final IMediaPlayer.OnTimedTextListener finalListener) {
    if (finalListener != null) {
      this.mBackEndMediaPlayer.setOnTimedTextListener(new IMediaPlayer.OnTimedTextListener() {
            public void onTimedText(IMediaPlayer param1IMediaPlayer, IjkTimedText param1IjkTimedText) {
              finalListener.onTimedText(MediaPlayerProxy.this, param1IjkTimedText);
            }
          });
    } else {
      this.mBackEndMediaPlayer.setOnTimedTextListener(null);
    } 
  }
  
  public void setOnVideoSizeChangedListener(final IMediaPlayer.OnVideoSizeChangedListener finalListener) {
    if (finalListener != null) {
      this.mBackEndMediaPlayer.setOnVideoSizeChangedListener(new IMediaPlayer.OnVideoSizeChangedListener() {
            public void onVideoSizeChanged(IMediaPlayer param1IMediaPlayer, int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
              finalListener.onVideoSizeChanged(MediaPlayerProxy.this, param1Int1, param1Int2, param1Int3, param1Int4);
            }
          });
    } else {
      this.mBackEndMediaPlayer.setOnVideoSizeChangedListener(null);
    } 
  }
  
  public void setScreenOnWhilePlaying(boolean paramBoolean) {
    this.mBackEndMediaPlayer.setScreenOnWhilePlaying(paramBoolean);
  }
  
  public void setSurface(Surface paramSurface) {
    this.mBackEndMediaPlayer.setSurface(paramSurface);
  }
  
  public void setVolume(float paramFloat1, float paramFloat2) {
    this.mBackEndMediaPlayer.setVolume(paramFloat1, paramFloat2);
  }
  
  public void setWakeMode(Context paramContext, int paramInt) {
    this.mBackEndMediaPlayer.setWakeMode(paramContext, paramInt);
  }
  
  public void start() throws IllegalStateException {
    this.mBackEndMediaPlayer.start();
  }
  
  public void stop() throws IllegalStateException {
    this.mBackEndMediaPlayer.stop();
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/tv/danmaku/ijk/media/player_gx/MediaPlayerProxy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */