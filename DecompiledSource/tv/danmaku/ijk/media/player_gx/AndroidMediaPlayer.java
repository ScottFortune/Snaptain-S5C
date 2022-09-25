package tv.danmaku.ijk.media.player_gx;

import android.content.Context;
import android.media.MediaDataSource;
import android.media.MediaPlayer;
import android.media.TimedText;
import android.net.Uri;
import android.text.TextUtils;
import android.view.Surface;
import android.view.SurfaceHolder;
import java.io.FileDescriptor;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Map;
import tv.danmaku.ijk.media.player_gx.misc.AndroidTrackInfo;
import tv.danmaku.ijk.media.player_gx.misc.IMediaDataSource;
import tv.danmaku.ijk.media.player_gx.misc.ITrackInfo;
import tv.danmaku.ijk.media.player_gx.pragma.DebugLog;

public class AndroidMediaPlayer extends AbstractMediaPlayer {
  private static MediaInfo sMediaInfo;
  
  private String mDataSource;
  
  private final Object mInitLock = new Object();
  
  private final AndroidMediaPlayerListenerHolder mInternalListenerAdapter;
  
  private final MediaPlayer mInternalMediaPlayer;
  
  private boolean mIsReleased;
  
  private MediaDataSource mMediaDataSource;
  
  public AndroidMediaPlayer() {
    synchronized (this.mInitLock) {
      MediaPlayer mediaPlayer = new MediaPlayer();
      this();
      this.mInternalMediaPlayer = mediaPlayer;
      this.mInternalMediaPlayer.setAudioStreamType(3);
      this.mInternalListenerAdapter = new AndroidMediaPlayerListenerHolder(this);
      attachInternalListeners();
      return;
    } 
  }
  
  private void attachInternalListeners() {
    this.mInternalMediaPlayer.setOnPreparedListener(this.mInternalListenerAdapter);
    this.mInternalMediaPlayer.setOnBufferingUpdateListener(this.mInternalListenerAdapter);
    this.mInternalMediaPlayer.setOnCompletionListener(this.mInternalListenerAdapter);
    this.mInternalMediaPlayer.setOnSeekCompleteListener(this.mInternalListenerAdapter);
    this.mInternalMediaPlayer.setOnVideoSizeChangedListener(this.mInternalListenerAdapter);
    this.mInternalMediaPlayer.setOnErrorListener(this.mInternalListenerAdapter);
    this.mInternalMediaPlayer.setOnInfoListener(this.mInternalListenerAdapter);
    this.mInternalMediaPlayer.setOnTimedTextListener(this.mInternalListenerAdapter);
  }
  
  private void releaseMediaDataSource() {
    MediaDataSource mediaDataSource = this.mMediaDataSource;
    if (mediaDataSource != null) {
      try {
        mediaDataSource.close();
      } catch (IOException iOException) {
        iOException.printStackTrace();
      } 
      this.mMediaDataSource = null;
    } 
  }
  
  public int getAudioSessionId() {
    return this.mInternalMediaPlayer.getAudioSessionId();
  }
  
  public long getCurrentPosition() {
    try {
      int i = this.mInternalMediaPlayer.getCurrentPosition();
      return i;
    } catch (IllegalStateException illegalStateException) {
      DebugLog.printStackTrace(illegalStateException);
      return 0L;
    } 
  }
  
  public String getDataSource() {
    return this.mDataSource;
  }
  
  public long getDuration() {
    try {
      int i = this.mInternalMediaPlayer.getDuration();
      return i;
    } catch (IllegalStateException illegalStateException) {
      DebugLog.printStackTrace(illegalStateException);
      return 0L;
    } 
  }
  
  public MediaPlayer getInternalMediaPlayer() {
    return this.mInternalMediaPlayer;
  }
  
  public MediaInfo getMediaInfo() {
    if (sMediaInfo == null) {
      MediaInfo mediaInfo = new MediaInfo();
      mediaInfo.mVideoDecoder = "android";
      mediaInfo.mVideoDecoderImpl = "HW";
      mediaInfo.mAudioDecoder = "android";
      mediaInfo.mAudioDecoderImpl = "HW";
      sMediaInfo = mediaInfo;
    } 
    return sMediaInfo;
  }
  
  public ITrackInfo[] getTrackInfo() {
    return (ITrackInfo[])AndroidTrackInfo.fromMediaPlayer(this.mInternalMediaPlayer);
  }
  
  public int getVideoHeight() {
    return this.mInternalMediaPlayer.getVideoHeight();
  }
  
  public int getVideoSarDen() {
    return 1;
  }
  
  public int getVideoSarNum() {
    return 1;
  }
  
  public int getVideoWidth() {
    return this.mInternalMediaPlayer.getVideoWidth();
  }
  
  public boolean isLooping() {
    return this.mInternalMediaPlayer.isLooping();
  }
  
  public boolean isPlayable() {
    return true;
  }
  
  public boolean isPlaying() {
    try {
      return this.mInternalMediaPlayer.isPlaying();
    } catch (IllegalStateException illegalStateException) {
      DebugLog.printStackTrace(illegalStateException);
      return false;
    } 
  }
  
  public void pause() throws IllegalStateException {
    this.mInternalMediaPlayer.pause();
  }
  
  public void prepareAsync() throws IllegalStateException {
    this.mInternalMediaPlayer.prepareAsync();
  }
  
  public void release() {
    this.mIsReleased = true;
    this.mInternalMediaPlayer.release();
    releaseMediaDataSource();
    resetListeners();
    attachInternalListeners();
  }
  
  public void reset() {
    try {
      this.mInternalMediaPlayer.reset();
    } catch (IllegalStateException illegalStateException) {
      DebugLog.printStackTrace(illegalStateException);
    } 
    releaseMediaDataSource();
    resetListeners();
    attachInternalListeners();
  }
  
  public void seekTo(long paramLong) throws IllegalStateException {
    this.mInternalMediaPlayer.seekTo((int)paramLong);
  }
  
  public void setAudioStreamType(int paramInt) {
    this.mInternalMediaPlayer.setAudioStreamType(paramInt);
  }
  
  public void setDataSource(Context paramContext, Uri paramUri) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
    this.mInternalMediaPlayer.setDataSource(paramContext, paramUri);
  }
  
  public void setDataSource(Context paramContext, Uri paramUri, Map<String, String> paramMap) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
    this.mInternalMediaPlayer.setDataSource(paramContext, paramUri, paramMap);
  }
  
  public void setDataSource(FileDescriptor paramFileDescriptor) throws IOException, IllegalArgumentException, IllegalStateException {
    this.mInternalMediaPlayer.setDataSource(paramFileDescriptor);
  }
  
  public void setDataSource(String paramString) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
    this.mDataSource = paramString;
    Uri uri = Uri.parse(paramString);
    String str = uri.getScheme();
    if (!TextUtils.isEmpty(str) && str.equalsIgnoreCase("file")) {
      this.mInternalMediaPlayer.setDataSource(uri.getPath());
    } else {
      this.mInternalMediaPlayer.setDataSource(paramString);
    } 
  }
  
  public void setDataSource(IMediaDataSource paramIMediaDataSource) {
    releaseMediaDataSource();
    this.mMediaDataSource = new MediaDataSourceProxy(paramIMediaDataSource);
    this.mInternalMediaPlayer.setDataSource(this.mMediaDataSource);
  }
  
  public void setDisplay(SurfaceHolder paramSurfaceHolder) {
    synchronized (this.mInitLock) {
      if (!this.mIsReleased)
        this.mInternalMediaPlayer.setDisplay(paramSurfaceHolder); 
      return;
    } 
  }
  
  public void setKeepInBackground(boolean paramBoolean) {}
  
  public void setLogEnabled(boolean paramBoolean) {}
  
  public void setLooping(boolean paramBoolean) {
    this.mInternalMediaPlayer.setLooping(paramBoolean);
  }
  
  public void setScreenOnWhilePlaying(boolean paramBoolean) {
    this.mInternalMediaPlayer.setScreenOnWhilePlaying(paramBoolean);
  }
  
  public void setSurface(Surface paramSurface) {
    this.mInternalMediaPlayer.setSurface(paramSurface);
  }
  
  public void setVolume(float paramFloat1, float paramFloat2) {
    this.mInternalMediaPlayer.setVolume(paramFloat1, paramFloat2);
  }
  
  public void setWakeMode(Context paramContext, int paramInt) {
    this.mInternalMediaPlayer.setWakeMode(paramContext, paramInt);
  }
  
  public void start() throws IllegalStateException {
    this.mInternalMediaPlayer.start();
  }
  
  public void stop() throws IllegalStateException {
    this.mInternalMediaPlayer.stop();
  }
  
  private class AndroidMediaPlayerListenerHolder implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnVideoSizeChangedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnInfoListener, MediaPlayer.OnTimedTextListener {
    public final WeakReference<AndroidMediaPlayer> mWeakMediaPlayer;
    
    public AndroidMediaPlayerListenerHolder(AndroidMediaPlayer param1AndroidMediaPlayer1) {
      this.mWeakMediaPlayer = new WeakReference<AndroidMediaPlayer>(param1AndroidMediaPlayer1);
    }
    
    public void onBufferingUpdate(MediaPlayer param1MediaPlayer, int param1Int) {
      if ((AndroidMediaPlayer)this.mWeakMediaPlayer.get() == null)
        return; 
      AndroidMediaPlayer.this.notifyOnBufferingUpdate(param1Int);
    }
    
    public void onCompletion(MediaPlayer param1MediaPlayer) {
      if ((AndroidMediaPlayer)this.mWeakMediaPlayer.get() == null)
        return; 
      AndroidMediaPlayer.this.notifyOnCompletion();
    }
    
    public boolean onError(MediaPlayer param1MediaPlayer, int param1Int1, int param1Int2) {
      boolean bool;
      if ((AndroidMediaPlayer)this.mWeakMediaPlayer.get() != null && AndroidMediaPlayer.this.notifyOnError(param1Int1, param1Int2)) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public boolean onInfo(MediaPlayer param1MediaPlayer, int param1Int1, int param1Int2) {
      boolean bool;
      if ((AndroidMediaPlayer)this.mWeakMediaPlayer.get() != null && AndroidMediaPlayer.this.notifyOnInfo(param1Int1, param1Int2)) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public void onPrepared(MediaPlayer param1MediaPlayer) {
      if ((AndroidMediaPlayer)this.mWeakMediaPlayer.get() == null)
        return; 
      AndroidMediaPlayer.this.notifyOnPrepared();
    }
    
    public void onSeekComplete(MediaPlayer param1MediaPlayer) {
      if ((AndroidMediaPlayer)this.mWeakMediaPlayer.get() == null)
        return; 
      AndroidMediaPlayer.this.notifyOnSeekComplete();
    }
    
    public void onTimedText(MediaPlayer param1MediaPlayer, TimedText param1TimedText) {
      IjkTimedText ijkTimedText;
      if ((AndroidMediaPlayer)this.mWeakMediaPlayer.get() == null)
        return; 
      param1MediaPlayer = null;
      if (param1TimedText != null)
        ijkTimedText = new IjkTimedText(param1TimedText.getBounds(), param1TimedText.getText()); 
      AndroidMediaPlayer.this.notifyOnTimedText(ijkTimedText);
    }
    
    public void onVideoSizeChanged(MediaPlayer param1MediaPlayer, int param1Int1, int param1Int2) {
      if ((AndroidMediaPlayer)this.mWeakMediaPlayer.get() == null)
        return; 
      AndroidMediaPlayer.this.notifyOnVideoSizeChanged(param1Int1, param1Int2, 1, 1);
    }
  }
  
  private static class MediaDataSourceProxy extends MediaDataSource {
    private final IMediaDataSource mMediaDataSource;
    
    public MediaDataSourceProxy(IMediaDataSource param1IMediaDataSource) {
      this.mMediaDataSource = param1IMediaDataSource;
    }
    
    public void close() throws IOException {
      this.mMediaDataSource.close();
    }
    
    public long getSize() throws IOException {
      return this.mMediaDataSource.getSize();
    }
    
    public int readAt(long param1Long, byte[] param1ArrayOfbyte, int param1Int1, int param1Int2) throws IOException {
      return this.mMediaDataSource.readAt(param1Long, param1ArrayOfbyte, param1Int1, param1Int2);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/tv/danmaku/ijk/media/player_gx/AndroidMediaPlayer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */