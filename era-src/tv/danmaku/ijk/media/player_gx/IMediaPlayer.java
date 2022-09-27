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

public interface IMediaPlayer {
  public static final int MEDIA_ERROR_IO = -1004;
  
  public static final int MEDIA_ERROR_MALFORMED = -1007;
  
  public static final int MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK = 200;
  
  public static final int MEDIA_ERROR_SERVER_DIED = 100;
  
  public static final int MEDIA_ERROR_TIMED_OUT = -110;
  
  public static final int MEDIA_ERROR_UNKNOWN = 1;
  
  public static final int MEDIA_ERROR_UNSUPPORTED = -1010;
  
  public static final int MEDIA_INFO_AUDIO_DECODED_START = 10003;
  
  public static final int MEDIA_INFO_AUDIO_RENDERING_START = 10002;
  
  public static final int MEDIA_INFO_AUDIO_SEEK_RENDERING_START = 10009;
  
  public static final int MEDIA_INFO_BAD_INTERLEAVING = 800;
  
  public static final int MEDIA_INFO_BUFFERING_END = 702;
  
  public static final int MEDIA_INFO_BUFFERING_START = 701;
  
  public static final int MEDIA_INFO_COMPONENT_OPEN = 10007;
  
  public static final int MEDIA_INFO_FIND_STREAM_INFO = 10006;
  
  public static final int MEDIA_INFO_MEDIA_ACCURATE_SEEK_COMPLETE = 10100;
  
  public static final int MEDIA_INFO_METADATA_UPDATE = 802;
  
  public static final int MEDIA_INFO_NETWORK_BANDWIDTH = 703;
  
  public static final int MEDIA_INFO_NOT_SEEKABLE = 801;
  
  public static final int MEDIA_INFO_OPEN_INPUT = 10005;
  
  public static final int MEDIA_INFO_STARTED_AS_NEXT = 2;
  
  public static final int MEDIA_INFO_SUBTITLE_TIMED_OUT = 902;
  
  public static final int MEDIA_INFO_TIMED_TEXT_ERROR = 900;
  
  public static final int MEDIA_INFO_UNKNOWN = 1;
  
  public static final int MEDIA_INFO_UNSUPPORTED_SUBTITLE = 901;
  
  public static final int MEDIA_INFO_VIDEO_DECODED_START = 10004;
  
  public static final int MEDIA_INFO_VIDEO_RENDERING_START = 3;
  
  public static final int MEDIA_INFO_VIDEO_ROTATION_CHANGED = 10001;
  
  public static final int MEDIA_INFO_VIDEO_SEEK_RENDERING_START = 10008;
  
  public static final int MEDIA_INFO_VIDEO_TRACK_LAGGING = 700;
  
  int getAudioSessionId();
  
  long getCurrentPosition();
  
  String getDataSource();
  
  long getDuration();
  
  MediaInfo getMediaInfo();
  
  ITrackInfo[] getTrackInfo();
  
  int getVideoHeight();
  
  int getVideoSarDen();
  
  int getVideoSarNum();
  
  int getVideoWidth();
  
  boolean isLooping();
  
  @Deprecated
  boolean isPlayable();
  
  boolean isPlaying();
  
  void pause() throws IllegalStateException;
  
  void prepareAsync() throws IllegalStateException;
  
  void release();
  
  void reset();
  
  void seekTo(long paramLong) throws IllegalStateException;
  
  void setAudioStreamType(int paramInt);
  
  void setDataSource(Context paramContext, Uri paramUri) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException;
  
  void setDataSource(Context paramContext, Uri paramUri, Map<String, String> paramMap) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException;
  
  void setDataSource(FileDescriptor paramFileDescriptor) throws IOException, IllegalArgumentException, IllegalStateException;
  
  void setDataSource(String paramString) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException;
  
  void setDataSource(IMediaDataSource paramIMediaDataSource);
  
  void setDisplay(SurfaceHolder paramSurfaceHolder);
  
  @Deprecated
  void setKeepInBackground(boolean paramBoolean);
  
  @Deprecated
  void setLogEnabled(boolean paramBoolean);
  
  void setLooping(boolean paramBoolean);
  
  void setOnBufferingUpdateListener(OnBufferingUpdateListener paramOnBufferingUpdateListener);
  
  void setOnCompletionListener(OnCompletionListener paramOnCompletionListener);
  
  void setOnErrorListener(OnErrorListener paramOnErrorListener);
  
  void setOnInfoListener(OnInfoListener paramOnInfoListener);
  
  void setOnPreparedListener(OnPreparedListener paramOnPreparedListener);
  
  void setOnSeekCompleteListener(OnSeekCompleteListener paramOnSeekCompleteListener);
  
  void setOnTimedTextListener(OnTimedTextListener paramOnTimedTextListener);
  
  void setOnVideoSizeChangedListener(OnVideoSizeChangedListener paramOnVideoSizeChangedListener);
  
  void setScreenOnWhilePlaying(boolean paramBoolean);
  
  void setSurface(Surface paramSurface);
  
  void setVolume(float paramFloat1, float paramFloat2);
  
  @Deprecated
  void setWakeMode(Context paramContext, int paramInt);
  
  void start() throws IllegalStateException;
  
  void stop() throws IllegalStateException;
  
  public static interface OnBufferingUpdateListener {
    void onBufferingUpdate(IMediaPlayer param1IMediaPlayer, int param1Int);
  }
  
  public static interface OnCompletionListener {
    void onCompletion(IMediaPlayer param1IMediaPlayer);
  }
  
  public static interface OnErrorListener {
    boolean onError(IMediaPlayer param1IMediaPlayer, int param1Int1, int param1Int2);
  }
  
  public static interface OnInfoListener {
    boolean onInfo(IMediaPlayer param1IMediaPlayer, int param1Int1, int param1Int2);
  }
  
  public static interface OnPreparedListener {
    void onPrepared(IMediaPlayer param1IMediaPlayer);
  }
  
  public static interface OnSeekCompleteListener {
    void onSeekComplete(IMediaPlayer param1IMediaPlayer);
  }
  
  public static interface OnTimedTextListener {
    void onTimedText(IMediaPlayer param1IMediaPlayer, IjkTimedText param1IjkTimedText);
  }
  
  public static interface OnVideoSizeChangedListener {
    void onVideoSizeChanged(IMediaPlayer param1IMediaPlayer, int param1Int1, int param1Int2, int param1Int3, int param1Int4);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/tv/danmaku/ijk/media/player_gx/IMediaPlayer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */