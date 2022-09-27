package tv.danmaku.ijk.media.player_gx;

import android.content.Context;
import android.graphics.Rect;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import java.io.FileDescriptor;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import tv.danmaku.ijk.media.player_gx.javaprotocols.BaseProtocol;
import tv.danmaku.ijk.media.player_gx.javaprotocols.JavaUrlContext;
import tv.danmaku.ijk.media.player_gx.misc.IAndroidIO;
import tv.danmaku.ijk.media.player_gx.misc.IMediaDataSource;
import tv.danmaku.ijk.media.player_gx.misc.ITrackInfo;
import tv.danmaku.ijk.media.player_gx.misc.IjkTrackInfo;
import tv.danmaku.ijk.media.player_gx.pragma.DebugLog;

public final class IjkMediaPlayer extends AbstractMediaPlayer {
  public static final int FFP_PROPV_DECODER_AVCODEC = 1;
  
  public static final int FFP_PROPV_DECODER_MEDIACODEC = 2;
  
  public static final int FFP_PROPV_DECODER_UNKNOWN = 0;
  
  public static final int FFP_PROPV_DECODER_VIDEOTOOLBOX = 3;
  
  public static final int FFP_PROP_FLOAT_DROP_FRAME_RATE = 10007;
  
  public static final int FFP_PROP_FLOAT_PLAYBACK_RATE = 10003;
  
  public static final int FFP_PROP_INT64_ASYNC_STATISTIC_BUF_BACKWARDS = 20201;
  
  public static final int FFP_PROP_INT64_ASYNC_STATISTIC_BUF_CAPACITY = 20203;
  
  public static final int FFP_PROP_INT64_ASYNC_STATISTIC_BUF_FORWARDS = 20202;
  
  public static final int FFP_PROP_INT64_AUDIO_CACHED_BYTES = 20008;
  
  public static final int FFP_PROP_INT64_AUDIO_CACHED_DURATION = 20006;
  
  public static final int FFP_PROP_INT64_AUDIO_CACHED_PACKETS = 20010;
  
  public static final int FFP_PROP_INT64_AUDIO_DECODER = 20004;
  
  public static final int FFP_PROP_INT64_BIT_RATE = 20100;
  
  public static final int FFP_PROP_INT64_CACHE_STATISTIC_COUNT_BYTES = 20208;
  
  public static final int FFP_PROP_INT64_CACHE_STATISTIC_FILE_FORWARDS = 20206;
  
  public static final int FFP_PROP_INT64_CACHE_STATISTIC_FILE_POS = 20207;
  
  public static final int FFP_PROP_INT64_CACHE_STATISTIC_PHYSICAL_POS = 20205;
  
  public static final int FFP_PROP_INT64_IMMEDIATE_RECONNECT = 20211;
  
  public static final int FFP_PROP_INT64_LATEST_SEEK_LOAD_DURATION = 20300;
  
  public static final int FFP_PROP_INT64_LOGICAL_FILE_SIZE = 20209;
  
  public static final int FFP_PROP_INT64_SELECTED_AUDIO_STREAM = 20002;
  
  public static final int FFP_PROP_INT64_SELECTED_TIMEDTEXT_STREAM = 20011;
  
  public static final int FFP_PROP_INT64_SELECTED_VIDEO_STREAM = 20001;
  
  public static final int FFP_PROP_INT64_SHARE_CACHE_DATA = 20210;
  
  public static final int FFP_PROP_INT64_TCP_SPEED = 20200;
  
  public static final int FFP_PROP_INT64_TRAFFIC_STATISTIC_BYTE_COUNT = 20204;
  
  public static final int FFP_PROP_INT64_VIDEO_CACHED_BYTES = 20007;
  
  public static final int FFP_PROP_INT64_VIDEO_CACHED_DURATION = 20005;
  
  public static final int FFP_PROP_INT64_VIDEO_CACHED_PACKETS = 20009;
  
  public static final int FFP_PROP_INT64_VIDEO_DECODER = 20003;
  
  public static final int IJK_LOG_DEBUG = 3;
  
  public static final int IJK_LOG_DEFAULT = 1;
  
  public static final int IJK_LOG_ERROR = 6;
  
  public static final int IJK_LOG_FATAL = 7;
  
  public static final int IJK_LOG_INFO = 4;
  
  public static final int IJK_LOG_SILENT = 8;
  
  public static final int IJK_LOG_UNKNOWN = 0;
  
  public static final int IJK_LOG_VERBOSE = 2;
  
  public static final int IJK_LOG_WARN = 5;
  
  private static final int MEDIA_BUFFERING_UPDATE = 3;
  
  private static final int MEDIA_ERROR = 100;
  
  private static final int MEDIA_INFO = 200;
  
  private static final int MEDIA_NOP = 0;
  
  private static final int MEDIA_PLAYBACK_COMPLETE = 2;
  
  private static final int MEDIA_PREPARED = 1;
  
  private static final int MEDIA_SEEK_COMPLETE = 4;
  
  protected static final int MEDIA_SET_VIDEO_SAR = 10001;
  
  private static final int MEDIA_SET_VIDEO_SIZE = 5;
  
  private static final int MEDIA_TIMED_TEXT = 99;
  
  public static final int OPT_CATEGORY_CODEC = 2;
  
  public static final int OPT_CATEGORY_FORMAT = 1;
  
  public static final int OPT_CATEGORY_PLAYER = 4;
  
  public static final int OPT_CATEGORY_SWS = 3;
  
  public static final int PROP_FLOAT_VIDEO_DECODE_FRAMES_PER_SECOND = 10001;
  
  public static final int PROP_FLOAT_VIDEO_OUTPUT_FRAMES_PER_SECOND = 10002;
  
  public static final int SDL_FCC_RV16 = 909203026;
  
  public static final int SDL_FCC_RV32 = 842225234;
  
  public static final int SDL_FCC_YV12 = 842094169;
  
  private static final String TAG = IjkMediaPlayer.class.getName();
  
  private static volatile boolean mIsLibLoaded;
  
  private static volatile boolean mIsNativeInitialized;
  
  private static final List<String> protocolNameList;
  
  private static final IjkLibLoader sLocalLibLoader = new IjkLibLoader() {
      public void loadLibrary(String param1String) throws UnsatisfiedLinkError, SecurityException {
        System.loadLibrary(param1String);
      }
    };
  
  private String mDataSource;
  
  private EventHandler mEventHandler;
  
  private int mListenerContext;
  
  private long mNativeAndroidIO;
  
  private long mNativeMediaDataSource;
  
  private long mNativeMediaPlayer;
  
  private int mNativeSurfaceTexture;
  
  private OnControlMessageListener mOnControlMessageListener;
  
  private OnMediaCodecSelectListener mOnMediaCodecSelectListener;
  
  private OnNativeInvokeListener mOnNativeInvokeListener;
  
  private boolean mScreenOnWhilePlaying;
  
  private boolean mStayAwake;
  
  private SurfaceHolder mSurfaceHolder;
  
  private int mVideoHeight;
  
  private int mVideoSarDen;
  
  private int mVideoSarNum;
  
  private int mVideoWidth;
  
  private PowerManager.WakeLock mWakeLock = null;
  
  static {
    mIsLibLoaded = false;
    protocolNameList = new ArrayList<String>();
    mIsNativeInitialized = false;
  }
  
  public IjkMediaPlayer() {
    this(sLocalLibLoader);
  }
  
  public IjkMediaPlayer(IjkLibLoader paramIjkLibLoader) {
    initPlayer(paramIjkLibLoader);
  }
  
  private native String _getAudioCodecInfo();
  
  private static native String _getColorFormatName(int paramInt);
  
  private native int _getLoopCount();
  
  private native Bundle _getMediaMeta();
  
  private native float _getPropertyFloat(int paramInt, float paramFloat);
  
  private native long _getPropertyLong(int paramInt, long paramLong);
  
  private native String _getVideoCodecInfo();
  
  private native void _pause() throws IllegalStateException;
  
  public static native int _readBuf(ByteBuffer paramByteBuffer, long paramLong, int paramInt);
  
  public static native void _registerProtocol(BaseProtocol paramBaseProtocol, Class<JavaUrlContext> paramClass);
  
  private native void _release();
  
  private native void _reset();
  
  private native void _setAndroidIOCallback(IAndroidIO paramIAndroidIO) throws IllegalArgumentException, SecurityException, IllegalStateException;
  
  private native void _setDataSource(String paramString, String[] paramArrayOfString1, String[] paramArrayOfString2) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException;
  
  private native void _setDataSource(IMediaDataSource paramIMediaDataSource) throws IllegalArgumentException, SecurityException, IllegalStateException;
  
  private native void _setDataSourceFd(int paramInt) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException;
  
  private native void _setFrameAtTime(String paramString, long paramLong1, long paramLong2, int paramInt1, int paramInt2) throws IllegalArgumentException, IllegalStateException;
  
  private native void _setLoopCount(int paramInt);
  
  private native void _setOption(int paramInt, String paramString, long paramLong);
  
  private native void _setOption(int paramInt, String paramString1, String paramString2);
  
  private native void _setPropertyFloat(int paramInt, float paramFloat);
  
  private native void _setPropertyLong(int paramInt, long paramLong);
  
  private native void _setStreamSelected(int paramInt, boolean paramBoolean);
  
  private native void _setVideoSurface(Surface paramSurface);
  
  private native void _start() throws IllegalStateException;
  
  private native void _stop() throws IllegalStateException;
  
  public static native int _writeBuf(ByteBuffer paramByteBuffer, long paramLong, int paramInt);
  
  public static String getColorFormatName(int paramInt) {
    return _getColorFormatName(paramInt);
  }
  
  private static void initNativeOnce() {
    // Byte code:
    //   0: ldc tv/danmaku/ijk/media/player_gx/IjkMediaPlayer
    //   2: monitorenter
    //   3: getstatic tv/danmaku/ijk/media/player_gx/IjkMediaPlayer.mIsNativeInitialized : Z
    //   6: ifne -> 16
    //   9: invokestatic native_init : ()V
    //   12: iconst_1
    //   13: putstatic tv/danmaku/ijk/media/player_gx/IjkMediaPlayer.mIsNativeInitialized : Z
    //   16: ldc tv/danmaku/ijk/media/player_gx/IjkMediaPlayer
    //   18: monitorexit
    //   19: return
    //   20: astore_0
    //   21: ldc tv/danmaku/ijk/media/player_gx/IjkMediaPlayer
    //   23: monitorexit
    //   24: aload_0
    //   25: athrow
    // Exception table:
    //   from	to	target	type
    //   3	16	20	finally
    //   16	19	20	finally
    //   21	24	20	finally
  }
  
  private void initPlayer(IjkLibLoader paramIjkLibLoader) {
    loadLibrariesOnce(paramIjkLibLoader);
    initNativeOnce();
    Looper looper = Looper.myLooper();
    if (looper != null) {
      this.mEventHandler = new EventHandler(this, looper);
    } else {
      looper = Looper.getMainLooper();
      if (looper != null) {
        this.mEventHandler = new EventHandler(this, looper);
      } else {
        this.mEventHandler = null;
      } 
    } 
    native_setup(new WeakReference<IjkMediaPlayer>(this));
  }
  
  public static void loadLibrariesOnce(IjkLibLoader paramIjkLibLoader) {
    // Byte code:
    //   0: ldc tv/danmaku/ijk/media/player_gx/IjkMediaPlayer
    //   2: monitorenter
    //   3: getstatic tv/danmaku/ijk/media/player_gx/IjkMediaPlayer.mIsLibLoaded : Z
    //   6: ifne -> 50
    //   9: aload_0
    //   10: astore_1
    //   11: aload_0
    //   12: ifnonnull -> 19
    //   15: getstatic tv/danmaku/ijk/media/player_gx/IjkMediaPlayer.sLocalLibLoader : Ltv/danmaku/ijk/media/player_gx/IjkLibLoader;
    //   18: astore_1
    //   19: aload_1
    //   20: ldc_w 'ijkffmpeg_gx'
    //   23: invokeinterface loadLibrary : (Ljava/lang/String;)V
    //   28: aload_1
    //   29: ldc_w 'ijksdl_gx'
    //   32: invokeinterface loadLibrary : (Ljava/lang/String;)V
    //   37: aload_1
    //   38: ldc_w 'ijkplayer_gx'
    //   41: invokeinterface loadLibrary : (Ljava/lang/String;)V
    //   46: iconst_1
    //   47: putstatic tv/danmaku/ijk/media/player_gx/IjkMediaPlayer.mIsLibLoaded : Z
    //   50: ldc tv/danmaku/ijk/media/player_gx/IjkMediaPlayer
    //   52: monitorexit
    //   53: return
    //   54: astore_0
    //   55: ldc tv/danmaku/ijk/media/player_gx/IjkMediaPlayer
    //   57: monitorexit
    //   58: aload_0
    //   59: athrow
    // Exception table:
    //   from	to	target	type
    //   3	9	54	finally
    //   15	19	54	finally
    //   19	50	54	finally
    //   50	53	54	finally
    //   55	58	54	finally
  }
  
  private native void native_finalize();
  
  private static native void native_init();
  
  private native void native_message_loop(Object paramObject);
  
  public static native void native_profileBegin(String paramString);
  
  public static native void native_profileEnd();
  
  public static native void native_setLogLevel(int paramInt);
  
  private native void native_setup(Object paramObject);
  
  private static boolean onNativeInvoke(Object paramObject, int paramInt, Bundle paramBundle) {
    DebugLog.ifmt(TAG, "onNativeInvoke %d", new Object[] { Integer.valueOf(paramInt) });
    if (paramObject != null && paramObject instanceof WeakReference) {
      paramObject = ((WeakReference<IjkMediaPlayer>)paramObject).get();
      if (paramObject != null) {
        OnNativeInvokeListener onNativeInvokeListener = ((IjkMediaPlayer)paramObject).mOnNativeInvokeListener;
        if (onNativeInvokeListener != null && onNativeInvokeListener.onNativeInvoke(paramInt, paramBundle))
          return true; 
        if (paramInt != 131079)
          return false; 
        paramObject = ((IjkMediaPlayer)paramObject).mOnControlMessageListener;
        if (paramObject == null)
          return false; 
        paramInt = paramBundle.getInt("segment_index", -1);
        if (paramInt >= 0) {
          paramObject = paramObject.onControlResolveSegmentUrl(paramInt);
          if (paramObject != null) {
            paramBundle.putString("url", (String)paramObject);
            return true;
          } 
          throw new RuntimeException(new IOException("onNativeInvoke() = <NULL newUrl>"));
        } 
        throw new InvalidParameterException("onNativeInvoke(invalid segment index)");
      } 
      throw new IllegalStateException("<null weakPlayer>.onNativeInvoke()");
    } 
    throw new IllegalStateException("<null weakThiz>.onNativeInvoke()");
  }
  
  private static String onSelectCodec(Object paramObject, String paramString, int paramInt1, int paramInt2) {
    if (paramObject == null || !(paramObject instanceof WeakReference))
      return null; 
    IjkMediaPlayer ijkMediaPlayer = ((WeakReference<IjkMediaPlayer>)paramObject).get();
    if (ijkMediaPlayer == null)
      return null; 
    OnMediaCodecSelectListener onMediaCodecSelectListener = ijkMediaPlayer.mOnMediaCodecSelectListener;
    paramObject = onMediaCodecSelectListener;
    if (onMediaCodecSelectListener == null)
      paramObject = DefaultMediaCodecSelector.sInstance; 
    return paramObject.onMediaCodecSelect(ijkMediaPlayer, paramString, paramInt1, paramInt2);
  }
  
  private static void postEventFromNative(Object paramObject1, int paramInt1, int paramInt2, int paramInt3, Object paramObject2) {
    if (paramObject1 == null)
      return; 
    paramObject1 = ((WeakReference<IjkMediaPlayer>)paramObject1).get();
    if (paramObject1 == null)
      return; 
    if (paramInt1 == 200 && paramInt2 == 2)
      paramObject1.start(); 
    EventHandler eventHandler = ((IjkMediaPlayer)paramObject1).mEventHandler;
    if (eventHandler != null) {
      paramObject2 = eventHandler.obtainMessage(paramInt1, paramInt2, paramInt3, paramObject2);
      ((IjkMediaPlayer)paramObject1).mEventHandler.sendMessage((Message)paramObject2);
    } 
  }
  
  public static void registerProtocolOnce(BaseProtocol paramBaseProtocol) {
    // Byte code:
    //   0: ldc tv/danmaku/ijk/media/player_gx/IjkMediaPlayer
    //   2: monitorenter
    //   3: iconst_0
    //   4: istore_1
    //   5: getstatic tv/danmaku/ijk/media/player_gx/IjkMediaPlayer.protocolNameList : Ljava/util/List;
    //   8: invokeinterface iterator : ()Ljava/util/Iterator;
    //   13: astore_2
    //   14: aload_2
    //   15: invokeinterface hasNext : ()Z
    //   20: ifeq -> 49
    //   23: aload_2
    //   24: invokeinterface next : ()Ljava/lang/Object;
    //   29: checkcast java/lang/String
    //   32: astore_3
    //   33: aload_0
    //   34: getfield name : Ljava/lang/String;
    //   37: aload_3
    //   38: invokevirtual equals : (Ljava/lang/Object;)Z
    //   41: ifeq -> 14
    //   44: iconst_1
    //   45: istore_1
    //   46: goto -> 14
    //   49: iload_1
    //   50: ifne -> 73
    //   53: aload_0
    //   54: ldc_w tv/danmaku/ijk/media/player_gx/javaprotocols/JavaUrlContext
    //   57: invokestatic _registerProtocol : (Ltv/danmaku/ijk/media/player_gx/javaprotocols/BaseProtocol;Ljava/lang/Class;)V
    //   60: getstatic tv/danmaku/ijk/media/player_gx/IjkMediaPlayer.protocolNameList : Ljava/util/List;
    //   63: aload_0
    //   64: getfield name : Ljava/lang/String;
    //   67: invokeinterface add : (Ljava/lang/Object;)Z
    //   72: pop
    //   73: ldc tv/danmaku/ijk/media/player_gx/IjkMediaPlayer
    //   75: monitorexit
    //   76: return
    //   77: astore_0
    //   78: ldc tv/danmaku/ijk/media/player_gx/IjkMediaPlayer
    //   80: monitorexit
    //   81: goto -> 86
    //   84: aload_0
    //   85: athrow
    //   86: goto -> 84
    // Exception table:
    //   from	to	target	type
    //   5	14	77	finally
    //   14	44	77	finally
    //   53	73	77	finally
    //   73	76	77	finally
    //   78	81	77	finally
  }
  
  private void setDataSource(FileDescriptor paramFileDescriptor, long paramLong1, long paramLong2) throws IOException, IllegalArgumentException, IllegalStateException {
    setDataSource(paramFileDescriptor);
  }
  
  private void stayAwake(boolean paramBoolean) {
    PowerManager.WakeLock wakeLock = this.mWakeLock;
    if (wakeLock != null)
      if (paramBoolean && !wakeLock.isHeld()) {
        this.mWakeLock.acquire();
      } else if (!paramBoolean && this.mWakeLock.isHeld()) {
        this.mWakeLock.release();
      }  
    this.mStayAwake = paramBoolean;
    updateSurfaceScreenOn();
  }
  
  private void updateSurfaceScreenOn() {
    SurfaceHolder surfaceHolder = this.mSurfaceHolder;
    if (surfaceHolder != null) {
      boolean bool;
      if (this.mScreenOnWhilePlaying && this.mStayAwake) {
        bool = true;
      } else {
        bool = false;
      } 
      surfaceHolder.setKeepScreenOn(bool);
    } 
  }
  
  public native void _prepareAsync() throws IllegalStateException;
  
  public void deselectTrack(int paramInt) {
    _setStreamSelected(paramInt, false);
  }
  
  protected void finalize() throws Throwable {
    super.finalize();
    native_finalize();
  }
  
  public long getAsyncStatisticBufBackwards() {
    return _getPropertyLong(20201, 0L);
  }
  
  public long getAsyncStatisticBufCapacity() {
    return _getPropertyLong(20203, 0L);
  }
  
  public long getAsyncStatisticBufForwards() {
    return _getPropertyLong(20202, 0L);
  }
  
  public long getAudioCachedBytes() {
    return _getPropertyLong(20008, 0L);
  }
  
  public long getAudioCachedDuration() {
    return _getPropertyLong(20006, 0L);
  }
  
  public long getAudioCachedPackets() {
    return _getPropertyLong(20010, 0L);
  }
  
  public native int getAudioSessionId();
  
  public long getBitRate() {
    return _getPropertyLong(20100, 0L);
  }
  
  public long getCacheStatisticCountBytes() {
    return _getPropertyLong(20208, 0L);
  }
  
  public long getCacheStatisticFileForwards() {
    return _getPropertyLong(20206, 0L);
  }
  
  public long getCacheStatisticFilePos() {
    return _getPropertyLong(20207, 0L);
  }
  
  public long getCacheStatisticPhysicalPos() {
    return _getPropertyLong(20205, 0L);
  }
  
  public native long getCurrentPosition();
  
  public String getDataSource() {
    return this.mDataSource;
  }
  
  public float getDropFrameRate() {
    return _getPropertyFloat(10007, 0.0F);
  }
  
  public native long getDuration();
  
  public long getFileSize() {
    return _getPropertyLong(20209, 0L);
  }
  
  public MediaInfo getMediaInfo() {
    MediaInfo mediaInfo = new MediaInfo();
    mediaInfo.mMediaPlayerName = "ijkplayer";
    String str = _getVideoCodecInfo();
    if (!TextUtils.isEmpty(str)) {
      String[] arrayOfString = str.split(",");
      if (arrayOfString.length >= 2) {
        mediaInfo.mVideoDecoder = arrayOfString[0];
        mediaInfo.mVideoDecoderImpl = arrayOfString[1];
      } else if (arrayOfString.length >= 1) {
        mediaInfo.mVideoDecoder = arrayOfString[0];
        mediaInfo.mVideoDecoderImpl = "";
      } 
    } 
    str = _getAudioCodecInfo();
    if (!TextUtils.isEmpty(str)) {
      String[] arrayOfString = str.split(",");
      if (arrayOfString.length >= 2) {
        mediaInfo.mAudioDecoder = arrayOfString[0];
        mediaInfo.mAudioDecoderImpl = arrayOfString[1];
      } else if (arrayOfString.length >= 1) {
        mediaInfo.mAudioDecoder = arrayOfString[0];
        mediaInfo.mAudioDecoderImpl = "";
      } 
    } 
    try {
      mediaInfo.mMeta = IjkMediaMeta.parse(_getMediaMeta());
    } finally {
      str = null;
    } 
  }
  
  public Bundle getMediaMeta() {
    return _getMediaMeta();
  }
  
  public long getSeekLoadDuration() {
    return _getPropertyLong(20300, 0L);
  }
  
  public int getSelectedTrack(int paramInt) {
    if (paramInt != 1) {
      if (paramInt != 2) {
        if (paramInt != 3)
          return -1; 
        long l2 = _getPropertyLong(20011, -1L);
        return (int)l2;
      } 
      long l1 = _getPropertyLong(20002, -1L);
      return (int)l1;
    } 
    long l = _getPropertyLong(20001, -1L);
    return (int)l;
  }
  
  public float getSpeed(float paramFloat) {
    return _getPropertyFloat(10003, 0.0F);
  }
  
  public long getTcpSpeed() {
    return _getPropertyLong(20200, 0L);
  }
  
  public IjkTrackInfo[] getTrackInfo() {
    Bundle bundle = getMediaMeta();
    if (bundle == null)
      return null; 
    IjkMediaMeta ijkMediaMeta = IjkMediaMeta.parse(bundle);
    if (ijkMediaMeta == null || ijkMediaMeta.mStreams == null)
      return null; 
    ArrayList<IjkTrackInfo> arrayList = new ArrayList();
    for (IjkMediaMeta.IjkStreamMeta ijkStreamMeta : ijkMediaMeta.mStreams) {
      IjkTrackInfo ijkTrackInfo = new IjkTrackInfo(ijkStreamMeta);
      if (ijkStreamMeta.mType.equalsIgnoreCase("video")) {
        ijkTrackInfo.setTrackType(1);
      } else if (ijkStreamMeta.mType.equalsIgnoreCase("audio")) {
        ijkTrackInfo.setTrackType(2);
      } else if (ijkStreamMeta.mType.equalsIgnoreCase("timedtext")) {
        ijkTrackInfo.setTrackType(3);
      } 
      arrayList.add(ijkTrackInfo);
    } 
    return arrayList.<IjkTrackInfo>toArray(new IjkTrackInfo[arrayList.size()]);
  }
  
  public long getTrafficStatisticByteCount() {
    return _getPropertyLong(20204, 0L);
  }
  
  public long getVideoCachedBytes() {
    return _getPropertyLong(20007, 0L);
  }
  
  public long getVideoCachedDuration() {
    return _getPropertyLong(20005, 0L);
  }
  
  public long getVideoCachedPackets() {
    return _getPropertyLong(20009, 0L);
  }
  
  public float getVideoDecodeFramesPerSecond() {
    return _getPropertyFloat(10001, 0.0F);
  }
  
  public int getVideoDecoder() {
    return (int)_getPropertyLong(20003, 0L);
  }
  
  public int getVideoHeight() {
    return this.mVideoHeight;
  }
  
  public float getVideoOutputFramesPerSecond() {
    return _getPropertyFloat(10002, 0.0F);
  }
  
  public int getVideoSarDen() {
    return this.mVideoSarDen;
  }
  
  public int getVideoSarNum() {
    return this.mVideoSarNum;
  }
  
  public int getVideoWidth() {
    return this.mVideoWidth;
  }
  
  public void httphookReconnect() {
    _setPropertyLong(20211, 1L);
  }
  
  public boolean isLooping() {
    int i = _getLoopCount();
    boolean bool = true;
    if (i == 1)
      bool = false; 
    return bool;
  }
  
  public boolean isPlayable() {
    return true;
  }
  
  public native boolean isPlaying();
  
  public void pause() throws IllegalStateException {
    stayAwake(false);
    _pause();
  }
  
  public void prepareAsync() throws IllegalStateException {
    _prepareAsync();
  }
  
  public void release() {
    stayAwake(false);
    updateSurfaceScreenOn();
    resetListeners();
    _release();
  }
  
  public void reset() {
    stayAwake(false);
    _reset();
    this.mEventHandler.removeCallbacksAndMessages(null);
    this.mVideoWidth = 0;
    this.mVideoHeight = 0;
  }
  
  public void resetListeners() {
    super.resetListeners();
    this.mOnMediaCodecSelectListener = null;
  }
  
  public native void seekTo(long paramLong) throws IllegalStateException;
  
  public void selectTrack(int paramInt) {
    _setStreamSelected(paramInt, true);
  }
  
  public void setAndroidIOCallback(IAndroidIO paramIAndroidIO) throws IllegalArgumentException, SecurityException, IllegalStateException {
    _setAndroidIOCallback(paramIAndroidIO);
  }
  
  public void setAudioStreamType(int paramInt) {}
  
  public void setCacheShare(int paramInt) {
    _setPropertyLong(20210, paramInt);
  }
  
  public void setDataSource(Context paramContext, Uri paramUri) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
    setDataSource(paramContext, paramUri, (Map<String, String>)null);
  }
  
  public void setDataSource(Context paramContext, Uri paramUri, Map<String, String> paramMap) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
    // Byte code:
    //   0: aload_2
    //   1: invokevirtual getScheme : ()Ljava/lang/String;
    //   4: astore #4
    //   6: ldc_w 'file'
    //   9: aload #4
    //   11: invokevirtual equals : (Ljava/lang/Object;)Z
    //   14: ifeq -> 26
    //   17: aload_0
    //   18: aload_2
    //   19: invokevirtual getPath : ()Ljava/lang/String;
    //   22: invokevirtual setDataSource : (Ljava/lang/String;)V
    //   25: return
    //   26: aload_2
    //   27: astore #5
    //   29: ldc_w 'content'
    //   32: aload #4
    //   34: invokevirtual equals : (Ljava/lang/Object;)Z
    //   37: ifeq -> 85
    //   40: aload_2
    //   41: astore #5
    //   43: ldc_w 'settings'
    //   46: aload_2
    //   47: invokevirtual getAuthority : ()Ljava/lang/String;
    //   50: invokevirtual equals : (Ljava/lang/Object;)Z
    //   53: ifeq -> 85
    //   56: aload_1
    //   57: aload_2
    //   58: invokestatic getDefaultType : (Landroid/net/Uri;)I
    //   61: invokestatic getActualDefaultRingtoneUri : (Landroid/content/Context;I)Landroid/net/Uri;
    //   64: astore #5
    //   66: aload #5
    //   68: ifnull -> 74
    //   71: goto -> 85
    //   74: new java/io/FileNotFoundException
    //   77: dup
    //   78: ldc_w 'Failed to resolve default ringtone'
    //   81: invokespecial <init> : (Ljava/lang/String;)V
    //   84: athrow
    //   85: aconst_null
    //   86: astore_2
    //   87: aconst_null
    //   88: astore #4
    //   90: aconst_null
    //   91: astore #6
    //   93: aload_1
    //   94: invokevirtual getContentResolver : ()Landroid/content/ContentResolver;
    //   97: aload #5
    //   99: ldc_w 'r'
    //   102: invokevirtual openAssetFileDescriptor : (Landroid/net/Uri;Ljava/lang/String;)Landroid/content/res/AssetFileDescriptor;
    //   105: astore_1
    //   106: aload_1
    //   107: ifnonnull -> 119
    //   110: aload_1
    //   111: ifnull -> 118
    //   114: aload_1
    //   115: invokevirtual close : ()V
    //   118: return
    //   119: aload_1
    //   120: astore #6
    //   122: aload_1
    //   123: astore_2
    //   124: aload_1
    //   125: astore #4
    //   127: aload_1
    //   128: invokevirtual getDeclaredLength : ()J
    //   131: lconst_0
    //   132: lcmp
    //   133: ifge -> 155
    //   136: aload_1
    //   137: astore #6
    //   139: aload_1
    //   140: astore_2
    //   141: aload_1
    //   142: astore #4
    //   144: aload_0
    //   145: aload_1
    //   146: invokevirtual getFileDescriptor : ()Ljava/io/FileDescriptor;
    //   149: invokevirtual setDataSource : (Ljava/io/FileDescriptor;)V
    //   152: goto -> 179
    //   155: aload_1
    //   156: astore #6
    //   158: aload_1
    //   159: astore_2
    //   160: aload_1
    //   161: astore #4
    //   163: aload_0
    //   164: aload_1
    //   165: invokevirtual getFileDescriptor : ()Ljava/io/FileDescriptor;
    //   168: aload_1
    //   169: invokevirtual getStartOffset : ()J
    //   172: aload_1
    //   173: invokevirtual getDeclaredLength : ()J
    //   176: invokespecial setDataSource : (Ljava/io/FileDescriptor;JJ)V
    //   179: aload_1
    //   180: ifnull -> 187
    //   183: aload_1
    //   184: invokevirtual close : ()V
    //   187: return
    //   188: astore_1
    //   189: aload #6
    //   191: ifnull -> 199
    //   194: aload #6
    //   196: invokevirtual close : ()V
    //   199: aload_1
    //   200: athrow
    //   201: astore_1
    //   202: aload_2
    //   203: ifnull -> 222
    //   206: goto -> 218
    //   209: astore_1
    //   210: aload #4
    //   212: ifnull -> 222
    //   215: aload #4
    //   217: astore_2
    //   218: aload_2
    //   219: invokevirtual close : ()V
    //   222: getstatic tv/danmaku/ijk/media/player_gx/IjkMediaPlayer.TAG : Ljava/lang/String;
    //   225: ldc_w 'Couldn't open file on client side, trying server side'
    //   228: invokestatic d : (Ljava/lang/String;Ljava/lang/String;)I
    //   231: pop
    //   232: aload_0
    //   233: aload #5
    //   235: invokevirtual toString : ()Ljava/lang/String;
    //   238: aload_3
    //   239: invokevirtual setDataSource : (Ljava/lang/String;Ljava/util/Map;)V
    //   242: return
    // Exception table:
    //   from	to	target	type
    //   93	106	209	java/lang/SecurityException
    //   93	106	201	java/io/IOException
    //   93	106	188	finally
    //   127	136	209	java/lang/SecurityException
    //   127	136	201	java/io/IOException
    //   127	136	188	finally
    //   144	152	209	java/lang/SecurityException
    //   144	152	201	java/io/IOException
    //   144	152	188	finally
    //   163	179	209	java/lang/SecurityException
    //   163	179	201	java/io/IOException
    //   163	179	188	finally
  }
  
  public void setDataSource(FileDescriptor paramFileDescriptor) throws IOException, IllegalArgumentException, IllegalStateException {
    if (Build.VERSION.SDK_INT < 12) {
      try {
        Field field = paramFileDescriptor.getClass().getDeclaredField("descriptor");
        field.setAccessible(true);
        int i = field.getInt(paramFileDescriptor);
        _setDataSourceFd(i);
      } catch (NoSuchFieldException noSuchFieldException) {
        throw new RuntimeException(noSuchFieldException);
      } catch (IllegalAccessException illegalAccessException) {
        throw new RuntimeException(illegalAccessException);
      } 
    } else {
      ParcelFileDescriptor parcelFileDescriptor = ParcelFileDescriptor.dup((FileDescriptor)illegalAccessException);
      try {
        _setDataSourceFd(parcelFileDescriptor.getFd());
        return;
      } finally {
        parcelFileDescriptor.close();
      } 
    } 
  }
  
  public void setDataSource(String paramString) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
    this.mDataSource = paramString;
    _setDataSource(paramString, (String[])null, (String[])null);
  }
  
  public void setDataSource(String paramString, Map<String, String> paramMap) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
    if (paramMap != null && !paramMap.isEmpty()) {
      StringBuilder stringBuilder = new StringBuilder();
      for (Map.Entry<String, String> entry : paramMap.entrySet()) {
        stringBuilder.append((String)entry.getKey());
        stringBuilder.append(":");
        if (!TextUtils.isEmpty((String)entry.getValue()))
          stringBuilder.append((String)entry.getValue()); 
        stringBuilder.append("\r\n");
        setOption(1, "headers", stringBuilder.toString());
        setOption(1, "protocol_whitelist", "async,cache,crypto,file,http,https,ijkhttphook,ijkinject,ijklivehook,ijklongurl,ijksegment,ijktcphook,pipe,rtp,tcp,tls,udp,ijkurlhook,data");
      } 
    } 
    setDataSource(paramString);
  }
  
  public void setDataSource(IMediaDataSource paramIMediaDataSource) throws IllegalArgumentException, SecurityException, IllegalStateException {
    _setDataSource(paramIMediaDataSource);
  }
  
  public void setDisplay(SurfaceHolder paramSurfaceHolder) {
    this.mSurfaceHolder = paramSurfaceHolder;
    if (paramSurfaceHolder != null) {
      Surface surface = paramSurfaceHolder.getSurface();
    } else {
      paramSurfaceHolder = null;
    } 
    _setVideoSurface((Surface)paramSurfaceHolder);
    updateSurfaceScreenOn();
  }
  
  public void setKeepInBackground(boolean paramBoolean) {}
  
  public void setLogEnabled(boolean paramBoolean) {}
  
  public void setLooping(boolean paramBoolean) {
    int i = paramBoolean ^ true;
    setOption(4, "loop", i);
    _setLoopCount(i);
  }
  
  public void setOnControlMessageListener(OnControlMessageListener paramOnControlMessageListener) {
    this.mOnControlMessageListener = paramOnControlMessageListener;
  }
  
  public void setOnMediaCodecSelectListener(OnMediaCodecSelectListener paramOnMediaCodecSelectListener) {
    this.mOnMediaCodecSelectListener = paramOnMediaCodecSelectListener;
  }
  
  public void setOnNativeInvokeListener(OnNativeInvokeListener paramOnNativeInvokeListener) {
    this.mOnNativeInvokeListener = paramOnNativeInvokeListener;
  }
  
  public void setOption(int paramInt, String paramString, long paramLong) {
    _setOption(paramInt, paramString, paramLong);
  }
  
  public void setOption(int paramInt, String paramString1, String paramString2) {
    _setOption(paramInt, paramString1, paramString2);
  }
  
  public void setScreenOnWhilePlaying(boolean paramBoolean) {
    if (this.mScreenOnWhilePlaying != paramBoolean) {
      if (paramBoolean && this.mSurfaceHolder == null)
        DebugLog.w(TAG, "setScreenOnWhilePlaying(true) is ineffective without a SurfaceHolder"); 
      this.mScreenOnWhilePlaying = paramBoolean;
      updateSurfaceScreenOn();
    } 
  }
  
  public void setSpeed(float paramFloat) {
    _setPropertyFloat(10003, paramFloat);
  }
  
  public void setSurface(Surface paramSurface) {
    if (this.mScreenOnWhilePlaying && paramSurface != null)
      DebugLog.w(TAG, "setScreenOnWhilePlaying(true) is ineffective for Surface"); 
    this.mSurfaceHolder = null;
    _setVideoSurface(paramSurface);
    updateSurfaceScreenOn();
  }
  
  public native void setVolume(float paramFloat1, float paramFloat2);
  
  public void setWakeMode(Context paramContext, int paramInt) {
    boolean bool;
    PowerManager.WakeLock wakeLock = this.mWakeLock;
    if (wakeLock != null) {
      if (wakeLock.isHeld()) {
        bool = true;
        this.mWakeLock.release();
      } else {
        bool = false;
      } 
      this.mWakeLock = null;
    } else {
      bool = false;
    } 
    this.mWakeLock = ((PowerManager)paramContext.getSystemService("power")).newWakeLock(paramInt | 0x20000000, IjkMediaPlayer.class.getName());
    this.mWakeLock.setReferenceCounted(false);
    if (bool)
      this.mWakeLock.acquire(); 
  }
  
  public void start() throws IllegalStateException {
    stayAwake(true);
    _start();
  }
  
  public void stop() throws IllegalStateException {
    stayAwake(false);
    _stop();
  }
  
  public static class DefaultMediaCodecSelector implements OnMediaCodecSelectListener {
    public static final DefaultMediaCodecSelector sInstance = new DefaultMediaCodecSelector();
    
    public String onMediaCodecSelect(IMediaPlayer param1IMediaPlayer, String param1String, int param1Int1, int param1Int2) {
      if (Build.VERSION.SDK_INT < 16)
        return null; 
      if (TextUtils.isEmpty(param1String))
        return null; 
      Log.i(IjkMediaPlayer.TAG, String.format(Locale.US, "onSelectCodec: mime=%s, profile=%d, level=%d", new Object[] { param1String, Integer.valueOf(param1Int1), Integer.valueOf(param1Int2) }));
      ArrayList<IjkMediaCodecInfo> arrayList = new ArrayList();
      int i = MediaCodecList.getCodecCount();
      for (param1Int1 = 0; param1Int1 < i; param1Int1++) {
        MediaCodecInfo mediaCodecInfo = MediaCodecList.getCodecInfoAt(param1Int1);
        Log.d(IjkMediaPlayer.TAG, String.format(Locale.US, "  found codec: %s", new Object[] { mediaCodecInfo.getName() }));
        if (!mediaCodecInfo.isEncoder()) {
          String[] arrayOfString = mediaCodecInfo.getSupportedTypes();
          if (arrayOfString != null) {
            int j = arrayOfString.length;
            for (param1Int2 = 0; param1Int2 < j; param1Int2++) {
              String str = arrayOfString[param1Int2];
              if (!TextUtils.isEmpty(str)) {
                Log.d(IjkMediaPlayer.TAG, String.format(Locale.US, "    mime: %s", new Object[] { str }));
                if (str.equalsIgnoreCase(param1String)) {
                  IjkMediaCodecInfo ijkMediaCodecInfo1 = IjkMediaCodecInfo.setupCandidate(mediaCodecInfo, param1String);
                  if (ijkMediaCodecInfo1 != null) {
                    arrayList.add(ijkMediaCodecInfo1);
                    Log.i(IjkMediaPlayer.TAG, String.format(Locale.US, "candidate codec: %s rank=%d", new Object[] { mediaCodecInfo.getName(), Integer.valueOf(ijkMediaCodecInfo1.mRank) }));
                    ijkMediaCodecInfo1.dumpProfileLevels(param1String);
                  } 
                } 
              } 
            } 
          } 
        } 
      } 
      if (arrayList.isEmpty())
        return null; 
      IjkMediaCodecInfo ijkMediaCodecInfo = arrayList.get(0);
      for (IjkMediaCodecInfo ijkMediaCodecInfo1 : arrayList) {
        if (ijkMediaCodecInfo1.mRank > ijkMediaCodecInfo.mRank)
          ijkMediaCodecInfo = ijkMediaCodecInfo1; 
      } 
      if (ijkMediaCodecInfo.mRank < 600) {
        Log.w(IjkMediaPlayer.TAG, String.format(Locale.US, "unaccetable codec: %s", new Object[] { ijkMediaCodecInfo.mCodecInfo.getName() }));
        return null;
      } 
      Log.i(IjkMediaPlayer.TAG, String.format(Locale.US, "selected codec: %s rank=%d", new Object[] { ijkMediaCodecInfo.mCodecInfo.getName(), Integer.valueOf(ijkMediaCodecInfo.mRank) }));
      return ijkMediaCodecInfo.mCodecInfo.getName();
    }
  }
  
  private static class EventHandler extends Handler {
    private final WeakReference<IjkMediaPlayer> mWeakPlayer;
    
    public EventHandler(IjkMediaPlayer param1IjkMediaPlayer, Looper param1Looper) {
      super(param1Looper);
      this.mWeakPlayer = new WeakReference<IjkMediaPlayer>(param1IjkMediaPlayer);
    }
    
    public void handleMessage(Message param1Message) {
      IjkMediaPlayer ijkMediaPlayer = this.mWeakPlayer.get();
      if (ijkMediaPlayer != null) {
        long l1 = ijkMediaPlayer.mNativeMediaPlayer;
        long l2 = 0L;
        if (l1 != 0L) {
          int i = param1Message.what;
          if (i != 0)
            if (i != 1) {
              if (i != 2) {
                if (i != 3) {
                  if (i != 4) {
                    if (i != 5) {
                      if (i != 99) {
                        if (i != 100) {
                          if (i != 200) {
                            if (i != 10001) {
                              String str = IjkMediaPlayer.TAG;
                              StringBuilder stringBuilder = new StringBuilder();
                              stringBuilder.append("Unknown message type ");
                              stringBuilder.append(param1Message.what);
                              DebugLog.e(str, stringBuilder.toString());
                            } else {
                              IjkMediaPlayer.access$502(ijkMediaPlayer, param1Message.arg1);
                              IjkMediaPlayer.access$602(ijkMediaPlayer, param1Message.arg2);
                              ijkMediaPlayer.notifyOnVideoSizeChanged(ijkMediaPlayer.mVideoWidth, ijkMediaPlayer.mVideoHeight, ijkMediaPlayer.mVideoSarNum, ijkMediaPlayer.mVideoSarDen);
                            } 
                          } else {
                            if (param1Message.arg1 == 3)
                              DebugLog.i(IjkMediaPlayer.TAG, "Info: MEDIA_INFO_VIDEO_RENDERING_START\n"); 
                            ijkMediaPlayer.notifyOnInfo(param1Message.arg1, param1Message.arg2);
                            return;
                          } 
                        } else {
                          String str = IjkMediaPlayer.TAG;
                          StringBuilder stringBuilder = new StringBuilder();
                          stringBuilder.append("Error (");
                          stringBuilder.append(param1Message.arg1);
                          stringBuilder.append(",");
                          stringBuilder.append(param1Message.arg2);
                          stringBuilder.append(")");
                          DebugLog.e(str, stringBuilder.toString());
                          if (!ijkMediaPlayer.notifyOnError(param1Message.arg1, param1Message.arg2))
                            ijkMediaPlayer.notifyOnCompletion(); 
                          ijkMediaPlayer.stayAwake(false);
                          return;
                        } 
                      } else {
                        if (param1Message.obj == null) {
                          ijkMediaPlayer.notifyOnTimedText(null);
                        } else {
                          ijkMediaPlayer.notifyOnTimedText(new IjkTimedText(new Rect(0, 0, 1, 1), (String)param1Message.obj));
                        } 
                        return;
                      } 
                    } else {
                      IjkMediaPlayer.access$302(ijkMediaPlayer, param1Message.arg1);
                      IjkMediaPlayer.access$402(ijkMediaPlayer, param1Message.arg2);
                      ijkMediaPlayer.notifyOnVideoSizeChanged(ijkMediaPlayer.mVideoWidth, ijkMediaPlayer.mVideoHeight, ijkMediaPlayer.mVideoSarNum, ijkMediaPlayer.mVideoSarDen);
                      return;
                    } 
                  } else {
                    ijkMediaPlayer.notifyOnSeekComplete();
                    return;
                  } 
                } else {
                  long l = param1Message.arg1;
                  l1 = l;
                  if (l < 0L)
                    l1 = 0L; 
                  l = ijkMediaPlayer.getDuration();
                  if (l > 0L)
                    l2 = l1 * 100L / l; 
                  l1 = l2;
                  if (l2 >= 100L)
                    l1 = 100L; 
                  ijkMediaPlayer.notifyOnBufferingUpdate((int)l1);
                  return;
                } 
              } else {
                ijkMediaPlayer.stayAwake(false);
                ijkMediaPlayer.notifyOnCompletion();
                return;
              } 
            } else {
              ijkMediaPlayer.notifyOnPrepared();
            }  
          return;
        } 
      } 
      DebugLog.w(IjkMediaPlayer.TAG, "IjkMediaPlayer went away with unhandled events");
    }
  }
  
  public static interface OnControlMessageListener {
    String onControlResolveSegmentUrl(int param1Int);
  }
  
  public static interface OnMediaCodecSelectListener {
    String onMediaCodecSelect(IMediaPlayer param1IMediaPlayer, String param1String, int param1Int1, int param1Int2);
  }
  
  public static interface OnNativeInvokeListener {
    public static final String ARG_ERROR = "error";
    
    public static final String ARG_FAMILIY = "family";
    
    public static final String ARG_FD = "fd";
    
    public static final String ARG_FILE_SIZE = "file_size";
    
    public static final String ARG_HTTP_CODE = "http_code";
    
    public static final String ARG_IP = "ip";
    
    public static final String ARG_OFFSET = "offset";
    
    public static final String ARG_PORT = "port";
    
    public static final String ARG_RETRY_COUNTER = "retry_counter";
    
    public static final String ARG_SEGMENT_INDEX = "segment_index";
    
    public static final String ARG_URL = "url";
    
    public static final int CTRL_DID_TCP_OPEN = 131074;
    
    public static final int CTRL_WILL_CONCAT_RESOLVE_SEGMENT = 131079;
    
    public static final int CTRL_WILL_HTTP_OPEN = 131075;
    
    public static final int CTRL_WILL_LIVE_OPEN = 131077;
    
    public static final int CTRL_WILL_TCP_OPEN = 131073;
    
    public static final int EVENT_DID_HTTP_OPEN = 2;
    
    public static final int EVENT_DID_HTTP_SEEK = 4;
    
    public static final int EVENT_WILL_HTTP_OPEN = 1;
    
    public static final int EVENT_WILL_HTTP_SEEK = 3;
    
    boolean onNativeInvoke(int param1Int, Bundle param1Bundle);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/tv/danmaku/ijk/media/player_gx/IjkMediaPlayer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */