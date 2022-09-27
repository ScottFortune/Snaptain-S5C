package com.netopsun.ijkvideoview.widget.media;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.net.Uri;
import android.opengl.EGLContext;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.MediaController;
import android.widget.TableLayout;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import com.netopsun.deviceshub.base.VideoCommunicator;
import com.netopsun.ijkvideoview.R;
import com.netopsun.ijkvideoview.Settings;
import com.netopsun.ijkvideoview.encoder.EncodeBitmapAndMux2Mp4;
import com.netopsun.ijkvideoview.protocols.VideoCommunicatorsProtocol;
import com.netopsun.ijkvideoview.services.MediaPlayerService;
import com.netopsun.ijkvideoview.widget.media.render.FHFishEyeDrawingHelper;
import com.netopsun.ijkvideoview.widget.media.render.FHGestureDetectorListener;
import com.netopsun.ijkvideoview.widget.media.render.IMultiFunctionRenderView;
import com.netopsun.ijkvideoview.widget.media.render.MultiFunctionRender;
import com.netopsun.ijkvideoview.widget.media.render.MultiFunctionRenderRuntimeEnvironment;
import com.yang.firework.ParticleLayer;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;
import tv.danmaku.ijk.media.player_gx.AndroidMediaPlayer;
import tv.danmaku.ijk.media.player_gx.IMediaPlayer;
import tv.danmaku.ijk.media.player_gx.IjkMediaPlayer;
import tv.danmaku.ijk.media.player_gx.IjkTimedText;
import tv.danmaku.ijk.media.player_gx.TextureMediaPlayer;
import tv.danmaku.ijk.media.player_gx.javaprotocols.BaseProtocol;
import tv.danmaku.ijk.media.player_gx.misc.IMediaFormat;
import tv.danmaku.ijk.media.player_gx.misc.ITrackInfo;

public class IjkVideoView extends FrameLayout implements MediaController.MediaPlayerControl {
  public static final int RENDER_MULTI_FUNC_TEXTURE_VIEW = 0;
  
  public static final int RENDER_NONE = 2;
  
  public static final int RENDER_SURFACE_VIEW = 3;
  
  public static final int RENDER_TEXTURE_VIEW = 4;
  
  private static final int STATE_ERROR = -1;
  
  private static final int STATE_IDLE = 0;
  
  private static final int STATE_PAUSED = 4;
  
  private static final int STATE_PLAYBACK_COMPLETED = 5;
  
  private static final int STATE_PLAYING = 3;
  
  private static final int STATE_PREPARED = 2;
  
  private static final int STATE_PREPARING = 1;
  
  private static final int[] s_allAspectRatio;
  
  private static final VideoCommunicatorsProtocol videoCommunicatorsProtocol = new VideoCommunicatorsProtocol();
  
  private String TAG = "IjkVideoView";
  
  private boolean av_sync_video_master = true;
  
  private final EncodeBitmapAndMux2Mp4 encodeBitmapAndMux2Mp4 = new EncodeBitmapAndMux2Mp4();
  
  private GestureDetector fhGestureDetector;
  
  private FHGestureDetectorListener fhGestureDetectorListener;
  
  private String iformat = "";
  
  private boolean isFHFishEyeTouchMode;
  
  private List<Integer> mAllRenders = new ArrayList<Integer>();
  
  private Context mAppContext;
  
  private IMediaPlayer.OnBufferingUpdateListener mBufferingUpdateListener = new IMediaPlayer.OnBufferingUpdateListener() {
      public void onBufferingUpdate(IMediaPlayer param1IMediaPlayer, int param1Int) {
        IjkVideoView.access$2202(IjkVideoView.this, param1Int);
      }
    };
  
  private boolean mCanPause = true;
  
  private boolean mCanSeekBack = true;
  
  private boolean mCanSeekForward = true;
  
  private IMediaPlayer.OnCompletionListener mCompletionListener = new IMediaPlayer.OnCompletionListener() {
      public void onCompletion(IMediaPlayer param1IMediaPlayer) {
        IjkVideoView.access$802(IjkVideoView.this, 5);
        IjkVideoView.access$1502(IjkVideoView.this, 5);
        if (IjkVideoView.this.mMediaController != null)
          IjkVideoView.this.mMediaController.hide(); 
        if (IjkVideoView.this.mOnCompletionListener != null)
          IjkVideoView.this.mOnCompletionListener.onCompletion(IjkVideoView.this.mMediaPlayer); 
      }
    };
  
  private int mCurrentAspectRatio = s_allAspectRatio[3];
  
  private int mCurrentAspectRatioIndex = 0;
  
  private int mCurrentBufferPercentage;
  
  private int mCurrentRender = 0;
  
  private int mCurrentRenderIndex = 0;
  
  private int mCurrentState = 0;
  
  private boolean mEnableBackgroundPlay = false;
  
  private IMediaPlayer.OnErrorListener mErrorListener = new IMediaPlayer.OnErrorListener() {
      public boolean onError(IMediaPlayer param1IMediaPlayer, int param1Int1, int param1Int2) {
        String str = IjkVideoView.this.TAG;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Error: ");
        stringBuilder.append(param1Int1);
        stringBuilder.append(",");
        stringBuilder.append(param1Int2);
        Log.d(str, stringBuilder.toString());
        IjkVideoView.access$802(IjkVideoView.this, -1);
        IjkVideoView.access$1502(IjkVideoView.this, -1);
        if (IjkVideoView.this.mMediaController != null)
          IjkVideoView.this.mMediaController.hide(); 
        if (IjkVideoView.this.mOnErrorListener != null && IjkVideoView.this.mOnErrorListener.onError(IjkVideoView.this.mMediaPlayer, param1Int1, param1Int2))
          return true; 
        if (IjkVideoView.this.getWindowToken() != null) {
          IjkVideoView.this.mAppContext.getResources();
          if (param1Int1 == 200) {
            param1Int1 = R.string.VideoView_error_text_invalid_progressive_playback;
          } else {
            param1Int1 = R.string.VideoView_error_text_unknown;
          } 
          (new AlertDialog.Builder(IjkVideoView.this.getContext())).setMessage(param1Int1).setPositiveButton(R.string.VideoView_error_button, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface param2DialogInterface, int param2Int) {
                  if (IjkVideoView.this.mOnCompletionListener != null)
                    IjkVideoView.this.mOnCompletionListener.onCompletion(IjkVideoView.this.mMediaPlayer); 
                }
              }).setCancelable(false).show();
        } 
        return true;
      }
    };
  
  private Map<String, String> mHeaders;
  
  private InfoHudViewHolder mHudViewHolder;
  
  private IMediaPlayer.OnInfoListener mInfoListener = new IMediaPlayer.OnInfoListener() {
      public boolean onInfo(IMediaPlayer param1IMediaPlayer, int param1Int1, int param1Int2) {
        if (IjkVideoView.this.mOnInfoListener != null)
          IjkVideoView.this.mOnInfoListener.onInfo(param1IMediaPlayer, param1Int1, param1Int2); 
        if (param1Int1 != 3) {
          if (param1Int1 != 901) {
            if (param1Int1 != 902) {
              if (param1Int1 != 10001) {
                if (param1Int1 != 10002) {
                  String str1;
                  StringBuilder stringBuilder1;
                  switch (param1Int1) {
                    default:
                      switch (param1Int1) {
                        default:
                          return true;
                        case 802:
                          Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_METADATA_UPDATE:");
                        case 801:
                          Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_NOT_SEEKABLE:");
                        case 800:
                          break;
                      } 
                      Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_BAD_INTERLEAVING:");
                    case 703:
                      str1 = IjkVideoView.this.TAG;
                      stringBuilder1 = new StringBuilder();
                      stringBuilder1.append("MEDIA_INFO_NETWORK_BANDWIDTH: ");
                      stringBuilder1.append(param1Int2);
                      Log.d(str1, stringBuilder1.toString());
                    case 702:
                      Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_BUFFERING_END:");
                    case 701:
                      Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_BUFFERING_START:");
                    case 700:
                      break;
                  } 
                  Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_VIDEO_TRACK_LAGGING:");
                } 
                Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_AUDIO_RENDERING_START:");
              } 
              IjkVideoView.access$1902(IjkVideoView.this, param1Int2);
              String str = IjkVideoView.this.TAG;
              StringBuilder stringBuilder = new StringBuilder();
              stringBuilder.append("MEDIA_INFO_VIDEO_ROTATION_CHANGED: ");
              stringBuilder.append(param1Int2);
              Log.d(str, stringBuilder.toString());
              if (IjkVideoView.this.mRenderView != null)
                IjkVideoView.this.mRenderView.setVideoRotation(param1Int2); 
            } 
            Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_SUBTITLE_TIMED_OUT:");
          } 
          Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_UNSUPPORTED_SUBTITLE:");
        } 
        Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_VIDEO_RENDERING_START:");
      }
    };
  
  private IMediaController mMediaController;
  
  private IMediaPlayer mMediaPlayer = null;
  
  private IMediaPlayer.OnCompletionListener mOnCompletionListener;
  
  private IMediaPlayer.OnErrorListener mOnErrorListener;
  
  private IMediaPlayer.OnInfoListener mOnInfoListener;
  
  private IMediaPlayer.OnPreparedListener mOnPreparedListener;
  
  private IMediaPlayer.OnTimedTextListener mOnTimedTextListener = new IMediaPlayer.OnTimedTextListener() {
      public void onTimedText(IMediaPlayer param1IMediaPlayer, IjkTimedText param1IjkTimedText) {
        if (param1IjkTimedText != null)
          IjkVideoView.this.subtitleDisplay.setText(param1IjkTimedText.getText()); 
      }
    };
  
  ByteBuffer mPixelBuf;
  
  private long mPrepareEndTime = 0L;
  
  private long mPrepareStartTime = 0L;
  
  IMediaPlayer.OnPreparedListener mPreparedListener = new IMediaPlayer.OnPreparedListener() {
      public void onPrepared(IMediaPlayer param1IMediaPlayer) {
        IjkVideoView.access$502(IjkVideoView.this, System.currentTimeMillis());
        if (IjkVideoView.this.mHudViewHolder != null)
          IjkVideoView.this.mHudViewHolder.updateLoadCost(IjkVideoView.this.mPrepareEndTime - IjkVideoView.this.mPrepareStartTime); 
        IjkVideoView.access$802(IjkVideoView.this, 2);
        if (IjkVideoView.this.mOnPreparedListener != null)
          IjkVideoView.this.mOnPreparedListener.onPrepared(IjkVideoView.this.mMediaPlayer); 
        if (IjkVideoView.this.mMediaController != null)
          IjkVideoView.this.mMediaController.setEnabled(true); 
        IjkVideoView.access$002(IjkVideoView.this, param1IMediaPlayer.getVideoWidth());
        IjkVideoView.access$102(IjkVideoView.this, param1IMediaPlayer.getVideoHeight());
        int i = IjkVideoView.this.mSeekWhenPrepared;
        if (i != 0)
          IjkVideoView.this.seekTo(i); 
        if (IjkVideoView.this.mVideoWidth != 0 && IjkVideoView.this.mVideoHeight != 0) {
          if (IjkVideoView.this.mRenderView != null) {
            IjkVideoView.this.mRenderView.setVideoSize(IjkVideoView.this.mVideoWidth, IjkVideoView.this.mVideoHeight);
            IjkVideoView.this.mRenderView.setVideoSampleAspectRatio(IjkVideoView.this.mVideoSarNum, IjkVideoView.this.mVideoSarDen);
            if (!IjkVideoView.this.mRenderView.shouldWaitForResize() || (IjkVideoView.this.mSurfaceWidth == IjkVideoView.this.mVideoWidth && IjkVideoView.this.mSurfaceHeight == IjkVideoView.this.mVideoHeight))
              if (IjkVideoView.this.mTargetState == 3) {
                IjkVideoView.this.start();
                if (IjkVideoView.this.mMediaController != null)
                  IjkVideoView.this.mMediaController.show(); 
              } else if (!IjkVideoView.this.isPlaying() && (i != 0 || IjkVideoView.this.getCurrentPosition() > 0) && IjkVideoView.this.mMediaController != null) {
                IjkVideoView.this.mMediaController.show(0);
              }  
          } 
        } else if (IjkVideoView.this.mTargetState == 3) {
          IjkVideoView.this.start();
        } 
      }
    };
  
  private IRenderView mRenderView;
  
  IRenderView.IRenderCallback mSHCallback = new IRenderView.IRenderCallback() {
      public void onSurfaceChanged(IRenderView.ISurfaceHolder param1ISurfaceHolder, int param1Int1, int param1Int2, int param1Int3) {
        if (param1ISurfaceHolder.getRenderView() != IjkVideoView.this.mRenderView) {
          Log.e(IjkVideoView.this.TAG, "onSurfaceChanged: unmatched render callback\n");
          return;
        } 
        IjkVideoView.access$1302(IjkVideoView.this, param1Int2);
        IjkVideoView.access$1402(IjkVideoView.this, param1Int3);
        param1Int1 = IjkVideoView.this.mTargetState;
        boolean bool1 = true;
        if (param1Int1 == 3) {
          param1Int1 = 1;
        } else {
          param1Int1 = 0;
        } 
        boolean bool2 = bool1;
        if (IjkVideoView.this.mRenderView.shouldWaitForResize())
          if (IjkVideoView.this.mVideoWidth == param1Int2 && IjkVideoView.this.mVideoHeight == param1Int3) {
            bool2 = bool1;
          } else {
            bool2 = false;
          }  
        if (IjkVideoView.this.mMediaPlayer != null && param1Int1 != 0 && bool2) {
          if (IjkVideoView.this.mSeekWhenPrepared != 0) {
            IjkVideoView ijkVideoView = IjkVideoView.this;
            ijkVideoView.seekTo(ijkVideoView.mSeekWhenPrepared);
          } 
          IjkVideoView.this.start();
        } 
      }
      
      public void onSurfaceCreated(IRenderView.ISurfaceHolder param1ISurfaceHolder, int param1Int1, int param1Int2) {
        if (param1ISurfaceHolder.getRenderView() != IjkVideoView.this.mRenderView) {
          Log.e(IjkVideoView.this.TAG, "onSurfaceCreated: unmatched render callback\n");
          return;
        } 
        IjkVideoView.access$2602(IjkVideoView.this, param1ISurfaceHolder);
        if (IjkVideoView.this.mMediaPlayer != null) {
          IjkVideoView ijkVideoView = IjkVideoView.this;
          ijkVideoView.bindSurfaceHolder(ijkVideoView.mMediaPlayer, param1ISurfaceHolder);
        } else {
          IjkVideoView.this.openVideo();
        } 
      }
      
      public void onSurfaceDestroyed(IRenderView.ISurfaceHolder param1ISurfaceHolder) {
        if (param1ISurfaceHolder.getRenderView() != IjkVideoView.this.mRenderView) {
          Log.e(IjkVideoView.this.TAG, "onSurfaceDestroyed: unmatched render callback\n");
          return;
        } 
        IjkVideoView.access$2602(IjkVideoView.this, (IRenderView.ISurfaceHolder)null);
        IjkVideoView.this.releaseWithoutStop();
      }
    };
  
  private IMediaPlayer.OnSeekCompleteListener mSeekCompleteListener = new IMediaPlayer.OnSeekCompleteListener() {
      public void onSeekComplete(IMediaPlayer param1IMediaPlayer) {
        IjkVideoView.access$2302(IjkVideoView.this, System.currentTimeMillis());
        if (IjkVideoView.this.mHudViewHolder != null)
          IjkVideoView.this.mHudViewHolder.updateSeekCost(IjkVideoView.this.mSeekEndTime - IjkVideoView.this.mSeekStartTime); 
      }
    };
  
  private long mSeekEndTime = 0L;
  
  private long mSeekStartTime = 0L;
  
  private int mSeekWhenPrepared;
  
  private Settings mSettings;
  
  IMediaPlayer.OnVideoSizeChangedListener mSizeChangedListener = new IMediaPlayer.OnVideoSizeChangedListener() {
      public void onVideoSizeChanged(IMediaPlayer param1IMediaPlayer, int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
        IjkVideoView.access$002(IjkVideoView.this, param1IMediaPlayer.getVideoWidth());
        IjkVideoView.access$102(IjkVideoView.this, param1IMediaPlayer.getVideoHeight());
        IjkVideoView.access$202(IjkVideoView.this, param1IMediaPlayer.getVideoSarNum());
        IjkVideoView.access$302(IjkVideoView.this, param1IMediaPlayer.getVideoSarDen());
        if (IjkVideoView.this.mVideoWidth != 0 && IjkVideoView.this.mVideoHeight != 0) {
          if (IjkVideoView.this.mRenderView != null) {
            IjkVideoView.this.mRenderView.setVideoSize(IjkVideoView.this.mVideoWidth, IjkVideoView.this.mVideoHeight);
            IjkVideoView.this.mRenderView.setVideoSampleAspectRatio(IjkVideoView.this.mVideoSarNum, IjkVideoView.this.mVideoSarDen);
          } 
          IjkVideoView.this.requestLayout();
        } 
      }
    };
  
  private int mSurfaceHeight;
  
  private IRenderView.ISurfaceHolder mSurfaceHolder = null;
  
  private int mSurfaceWidth;
  
  private int mTargetState = 0;
  
  private Uri mUri;
  
  private int mVideoHeight;
  
  private int mVideoRotationDegree;
  
  private int mVideoSarDen;
  
  private int mVideoSarNum;
  
  private int mVideoWidth;
  
  ParticleLayer particleLayer;
  
  private TextView subtitleDisplay;
  
  private boolean usingMediaCodec = false;
  
  private VideoCommunicator videoCommunicator;
  
  static {
    IjkMediaPlayer.loadLibrariesOnce(null);
    IjkMediaPlayer.native_profileBegin("libijkplayer_gx.so");
    IjkMediaPlayer.registerProtocolOnce((BaseProtocol)videoCommunicatorsProtocol);
    s_allAspectRatio = new int[] { 0, 1, 2, 3, 4, 5 };
  }
  
  public IjkVideoView(Context paramContext) {
    super(paramContext);
    initVideoView(paramContext);
  }
  
  public IjkVideoView(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    initVideoView(paramContext);
  }
  
  public IjkVideoView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    initVideoView(paramContext);
  }
  
  public IjkVideoView(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2) {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    initVideoView(paramContext);
  }
  
  private void attachMediaController() {
    if (this.mMediaPlayer != null) {
      IMediaController iMediaController = this.mMediaController;
      if (iMediaController != null) {
        IjkVideoView ijkVideoView;
        iMediaController.setMediaPlayer(this);
        if (getParent() instanceof View) {
          View view = (View)getParent();
        } else {
          ijkVideoView = this;
        } 
        this.mMediaController.setAnchorView((View)ijkVideoView);
        this.mMediaController.setEnabled(isInPlaybackState());
      } 
    } 
  }
  
  private void bindSurfaceHolder(IMediaPlayer paramIMediaPlayer, IRenderView.ISurfaceHolder paramISurfaceHolder) {
    if (paramIMediaPlayer == null)
      return; 
    if (paramISurfaceHolder == null) {
      paramIMediaPlayer.setDisplay(null);
      return;
    } 
    paramISurfaceHolder.bindToMediaPlayer(paramIMediaPlayer);
  }
  
  private String buildLanguage(String paramString) {
    String str = paramString;
    if (TextUtils.isEmpty(paramString))
      str = "und"; 
    return str;
  }
  
  private String buildResolution(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(paramInt1);
    stringBuilder.append(" x ");
    stringBuilder.append(paramInt2);
    if (paramInt3 > 1 || paramInt4 > 1) {
      stringBuilder.append("[");
      stringBuilder.append(paramInt3);
      stringBuilder.append(":");
      stringBuilder.append(paramInt4);
      stringBuilder.append("]");
    } 
    return stringBuilder.toString();
  }
  
  private String buildTimeMilli(long paramLong) {
    long l1 = paramLong / 1000L;
    long l2 = l1 / 3600L;
    long l3 = l1 % 3600L / 60L;
    l1 %= 60L;
    return (paramLong <= 0L) ? "--:--" : ((l2 >= 100L) ? String.format(Locale.US, "%d:%02d:%02d", new Object[] { Long.valueOf(l2), Long.valueOf(l3), Long.valueOf(l1) }) : ((l2 > 0L) ? String.format(Locale.US, "%02d:%02d:%02d", new Object[] { Long.valueOf(l2), Long.valueOf(l3), Long.valueOf(l1) }) : String.format(Locale.US, "%02d:%02d", new Object[] { Long.valueOf(l3), Long.valueOf(l1) })));
  }
  
  private String buildTrackType(int paramInt) {
    Context context = getContext();
    return (paramInt != 1) ? ((paramInt != 2) ? ((paramInt != 3) ? ((paramInt != 4) ? ((paramInt != 5) ? context.getString(R.string.TrackType_unknown) : context.getString(R.string.TrackType_metadata)) : context.getString(R.string.TrackType_subtitle)) : context.getString(R.string.TrackType_timedtext)) : context.getString(R.string.TrackType_audio)) : context.getString(R.string.TrackType_video);
  }
  
  public static String getPlayerText(Context paramContext, int paramInt) {
    String str;
    if (paramInt != 1) {
      if (paramInt != 2) {
        if (paramInt != 3) {
          str = paramContext.getString(R.string.N_A);
        } else {
          str = str.getString(R.string.VideoView_player_IjkExoMediaPlayer);
        } 
      } else {
        str = str.getString(R.string.VideoView_player_IjkMediaPlayer);
      } 
    } else {
      str = str.getString(R.string.VideoView_player_AndroidMediaPlayer);
    } 
    return str;
  }
  
  public static String getRenderText(Context paramContext, int paramInt) {
    String str;
    if (paramInt != 2) {
      if (paramInt != 3) {
        if (paramInt != 4) {
          str = paramContext.getString(R.string.N_A);
        } else {
          str = str.getString(R.string.VideoView_render_texture_view);
        } 
      } else {
        str = str.getString(R.string.VideoView_render_surface_view);
      } 
    } else {
      str = str.getString(R.string.VideoView_render_none);
    } 
    return str;
  }
  
  private void initBackground() {
    this.mEnableBackgroundPlay = this.mSettings.getEnableBackgroundPlay();
    if (this.mEnableBackgroundPlay) {
      MediaPlayerService.intentToStart(getContext());
      this.mMediaPlayer = MediaPlayerService.getMediaPlayer();
      InfoHudViewHolder infoHudViewHolder = this.mHudViewHolder;
      if (infoHudViewHolder != null)
        infoHudViewHolder.setMediaPlayer(this.mMediaPlayer); 
    } 
  }
  
  private void initRenders() {
    this.mAllRenders.clear();
    this.mAllRenders.add(Integer.valueOf(0));
    boolean bool = this.mSettings.getEnableSurfaceView();
    Integer integer = Integer.valueOf(3);
    if (bool)
      this.mAllRenders.add(integer); 
    if (this.mSettings.getEnableTextureView() && Build.VERSION.SDK_INT >= 14)
      this.mAllRenders.add(Integer.valueOf(4)); 
    if (this.mSettings.getEnableNoView())
      this.mAllRenders.add(Integer.valueOf(2)); 
    if (this.mAllRenders.isEmpty())
      this.mAllRenders.add(integer); 
    this.mCurrentRender = ((Integer)this.mAllRenders.get(this.mCurrentRenderIndex)).intValue();
    setRender(this.mCurrentRender);
  }
  
  private void initVideoView(Context paramContext) {
    this.mAppContext = paramContext.getApplicationContext();
    this.mSettings = new Settings(this.mAppContext);
    initBackground();
    initRenders();
    this.mVideoWidth = 0;
    this.mVideoHeight = 0;
    setFocusable(true);
    setFocusableInTouchMode(true);
    requestFocus();
    this.mCurrentState = 0;
    this.mTargetState = 0;
    this.subtitleDisplay = new TextView(paramContext);
    this.subtitleDisplay.setTextSize(24.0F);
    this.subtitleDisplay.setGravity(17);
    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -2, 80);
    addView((View)this.subtitleDisplay, (ViewGroup.LayoutParams)layoutParams);
  }
  
  private boolean isInPlaybackState() {
    IMediaPlayer iMediaPlayer = this.mMediaPlayer;
    null = true;
    if (iMediaPlayer != null) {
      int i = this.mCurrentState;
      if (i != -1 && i != 0 && i != 1)
        return null; 
    } 
    return false;
  }
  
  private void openVideo() {
    if (this.mUri == null || this.mSurfaceHolder == null)
      return; 
    release(false);
    ((AudioManager)this.mAppContext.getSystemService("audio")).requestAudioFocus(null, 3, 1);
    try {
      this.mMediaPlayer = createPlayer(this.mSettings.getPlayer());
      getContext();
      this.mMediaPlayer.setOnPreparedListener(this.mPreparedListener);
      this.mMediaPlayer.setOnVideoSizeChangedListener(this.mSizeChangedListener);
      this.mMediaPlayer.setOnCompletionListener(this.mCompletionListener);
      this.mMediaPlayer.setOnErrorListener(this.mErrorListener);
      this.mMediaPlayer.setOnInfoListener(this.mInfoListener);
      this.mMediaPlayer.setOnBufferingUpdateListener(this.mBufferingUpdateListener);
      this.mMediaPlayer.setOnSeekCompleteListener(this.mSeekCompleteListener);
      this.mMediaPlayer.setOnTimedTextListener(this.mOnTimedTextListener);
      this.mCurrentBufferPercentage = 0;
      String str = this.mUri.getScheme();
      if (Build.VERSION.SDK_INT >= 23 && this.mSettings.getUsingMediaDataSource() && (TextUtils.isEmpty(str) || str.equalsIgnoreCase("file"))) {
        FileMediaDataSource fileMediaDataSource = new FileMediaDataSource();
        File file = new File();
        this(this.mUri.toString());
        this(file);
        this.mMediaPlayer.setDataSource(fileMediaDataSource);
      } else if (Build.VERSION.SDK_INT >= 14) {
        this.mMediaPlayer.setDataSource(this.mAppContext, this.mUri, this.mHeaders);
      } else {
        this.mMediaPlayer.setDataSource(this.mUri.toString());
      } 
      bindSurfaceHolder(this.mMediaPlayer, this.mSurfaceHolder);
      this.mMediaPlayer.setAudioStreamType(3);
      this.mMediaPlayer.setScreenOnWhilePlaying(true);
      this.mPrepareStartTime = System.currentTimeMillis();
      this.mMediaPlayer.prepareAsync();
      if (this.mHudViewHolder != null)
        this.mHudViewHolder.setMediaPlayer(this.mMediaPlayer); 
      this.mCurrentState = 1;
      attachMediaController();
    } catch (IOException iOException) {
      String str = this.TAG;
      StringBuilder stringBuilder = new StringBuilder();
      this();
      stringBuilder.append("Unable to url_open content: ");
      stringBuilder.append(this.mUri);
      Log.w(str, stringBuilder.toString(), iOException);
      this.mCurrentState = -1;
      this.mTargetState = -1;
      this.mErrorListener.onError(this.mMediaPlayer, 1, 0);
    } catch (IllegalArgumentException illegalArgumentException) {
      String str = this.TAG;
      StringBuilder stringBuilder = new StringBuilder();
      this();
      stringBuilder.append("Unable to url_open content: ");
      stringBuilder.append(this.mUri);
      Log.w(str, stringBuilder.toString(), illegalArgumentException);
      this.mCurrentState = -1;
      this.mTargetState = -1;
      this.mErrorListener.onError(this.mMediaPlayer, 1, 0);
    } finally {
      Exception exception;
    } 
  }
  
  private void setVideoURI(Uri paramUri, Map<String, String> paramMap) {
    this.mUri = paramUri;
    this.mHeaders = paramMap;
    this.mSeekWhenPrepared = 0;
    openVideo();
    requestLayout();
    invalidate();
  }
  
  private void toggleMediaControlsVisiblity() {
    if (this.mMediaController.isShowing()) {
      this.mMediaController.hide();
    } else {
      this.mMediaController.show();
    } 
  }
  
  public boolean canPause() {
    return this.mCanPause;
  }
  
  public boolean canSeekBackward() {
    return this.mCanSeekBack;
  }
  
  public boolean canSeekForward() {
    return this.mCanSeekForward;
  }
  
  public void capture(final String fileName, final int width, final int height) {
    IRenderView iRenderView = this.mRenderView;
    if (iRenderView instanceof IMultiFunctionRenderView)
      ((IMultiFunctionRenderView)iRenderView).getMultiFunctionRenderRuntimeEnvironment().queueEvent(new Runnable() {
            public void run() {
              if (IjkVideoView.this.mPixelBuf == null || IjkVideoView.this.mPixelBuf.capacity() < width * height * 4)
                IjkVideoView.this.mPixelBuf = ByteBuffer.allocate(height * width * 4); 
              IjkVideoView.this.mPixelBuf.rewind();
              ((IMultiFunctionRenderView)IjkVideoView.this.mRenderView).getMultiFunctionRender().drawProcessedTextureToByteBuffer(IjkVideoView.this.mPixelBuf, width, height);
              Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
              bitmap.copyPixelsFromBuffer(IjkVideoView.this.mPixelBuf);
              IjkVideoView.this.compressBitmap(bitmap, fileName, width, height);
            }
          }); 
  }
  
  protected void compressBitmap(final Bitmap bmp, final String fileName, final int width, final int height) {
    (new Thread(new Runnable() {
          public void run() {
            // Byte code:
            //   0: new java/io/BufferedOutputStream
            //   3: astore_1
            //   4: new java/io/FileOutputStream
            //   7: astore_2
            //   8: aload_2
            //   9: aload_0
            //   10: getfield val$fileName : Ljava/lang/String;
            //   13: invokespecial <init> : (Ljava/lang/String;)V
            //   16: aload_1
            //   17: aload_2
            //   18: invokespecial <init> : (Ljava/io/OutputStream;)V
            //   21: aload_1
            //   22: astore_2
            //   23: new android/graphics/Matrix
            //   26: astore_3
            //   27: aload_1
            //   28: astore_2
            //   29: aload_3
            //   30: invokespecial <init> : ()V
            //   33: aload_1
            //   34: astore_2
            //   35: aload_3
            //   36: ldc -1.0
            //   38: fconst_1
            //   39: invokevirtual postScale : (FF)Z
            //   42: pop
            //   43: aload_1
            //   44: astore_2
            //   45: aload_3
            //   46: ldc -180.0
            //   48: invokevirtual postRotate : (F)Z
            //   51: pop
            //   52: aload_1
            //   53: astore_2
            //   54: aload_0
            //   55: getfield val$bmp : Landroid/graphics/Bitmap;
            //   58: iconst_0
            //   59: iconst_0
            //   60: aload_0
            //   61: getfield val$width : I
            //   64: aload_0
            //   65: getfield val$height : I
            //   68: aload_3
            //   69: iconst_1
            //   70: invokestatic createBitmap : (Landroid/graphics/Bitmap;IIIILandroid/graphics/Matrix;Z)Landroid/graphics/Bitmap;
            //   73: astore_3
            //   74: aload_1
            //   75: astore_2
            //   76: aload_0
            //   77: getfield this$0 : Lcom/netopsun/ijkvideoview/widget/media/IjkVideoView;
            //   80: getfield mPixelBuf : Ljava/nio/ByteBuffer;
            //   83: invokevirtual rewind : ()Ljava/nio/Buffer;
            //   86: pop
            //   87: aload_1
            //   88: astore_2
            //   89: aload_3
            //   90: getstatic android/graphics/Bitmap$CompressFormat.JPEG : Landroid/graphics/Bitmap$CompressFormat;
            //   93: bipush #90
            //   95: aload_1
            //   96: invokevirtual compress : (Landroid/graphics/Bitmap$CompressFormat;ILjava/io/OutputStream;)Z
            //   99: pop
            //   100: aload_1
            //   101: astore_2
            //   102: aload_0
            //   103: getfield val$bmp : Landroid/graphics/Bitmap;
            //   106: invokevirtual recycle : ()V
            //   109: aload_1
            //   110: astore_2
            //   111: aload_3
            //   112: invokevirtual recycle : ()V
            //   115: aload_1
            //   116: invokevirtual close : ()V
            //   119: goto -> 157
            //   122: astore_3
            //   123: goto -> 135
            //   126: astore_2
            //   127: aconst_null
            //   128: astore_1
            //   129: goto -> 163
            //   132: astore_3
            //   133: aconst_null
            //   134: astore_1
            //   135: aload_1
            //   136: astore_2
            //   137: aload_3
            //   138: invokevirtual printStackTrace : ()V
            //   141: aload_1
            //   142: ifnull -> 157
            //   145: aload_1
            //   146: invokevirtual close : ()V
            //   149: goto -> 157
            //   152: astore_2
            //   153: aload_2
            //   154: invokevirtual printStackTrace : ()V
            //   157: return
            //   158: astore_3
            //   159: aload_2
            //   160: astore_1
            //   161: aload_3
            //   162: astore_2
            //   163: aload_1
            //   164: ifnull -> 179
            //   167: aload_1
            //   168: invokevirtual close : ()V
            //   171: goto -> 179
            //   174: astore_1
            //   175: aload_1
            //   176: invokevirtual printStackTrace : ()V
            //   179: aload_2
            //   180: athrow
            // Exception table:
            //   from	to	target	type
            //   0	21	132	java/io/FileNotFoundException
            //   0	21	126	finally
            //   23	27	122	java/io/FileNotFoundException
            //   23	27	158	finally
            //   29	33	122	java/io/FileNotFoundException
            //   29	33	158	finally
            //   35	43	122	java/io/FileNotFoundException
            //   35	43	158	finally
            //   45	52	122	java/io/FileNotFoundException
            //   45	52	158	finally
            //   54	74	122	java/io/FileNotFoundException
            //   54	74	158	finally
            //   76	87	122	java/io/FileNotFoundException
            //   76	87	158	finally
            //   89	100	122	java/io/FileNotFoundException
            //   89	100	158	finally
            //   102	109	122	java/io/FileNotFoundException
            //   102	109	158	finally
            //   111	115	122	java/io/FileNotFoundException
            //   111	115	158	finally
            //   115	119	152	java/io/IOException
            //   137	141	158	finally
            //   145	149	152	java/io/IOException
            //   167	171	174	java/io/IOException
          }
        })).start();
  }
  
  public IMediaPlayer createPlayer(int paramInt) {
    TextureMediaPlayer textureMediaPlayer;
    AndroidMediaPlayer androidMediaPlayer2;
    if (paramInt != 1) {
      if (this.mUri != null) {
        IjkMediaPlayer ijkMediaPlayer1 = new IjkMediaPlayer();
        IjkMediaPlayer.native_setLogLevel(6);
        if (this.mSettings.getUsingMediaCodec() && this.usingMediaCodec) {
          ijkMediaPlayer1.setOption(4, "mediacodec", 1L);
          ijkMediaPlayer1.setOption(4, "mediacodec-hevc", 1L);
          ijkMediaPlayer1.setOption(2, "mediacodec-sync", 1L);
          if (this.mSettings.getUsingMediaCodecAutoRotate()) {
            ijkMediaPlayer1.setOption(4, "mediacodec-auto-rotate", 1L);
          } else {
            ijkMediaPlayer1.setOption(4, "mediacodec-auto-rotate", 0L);
          } 
          if (this.mSettings.getMediaCodecHandleResolutionChange()) {
            ijkMediaPlayer1.setOption(4, "mediacodec-handle-resolution-change", 1L);
          } else {
            ijkMediaPlayer1.setOption(4, "mediacodec-handle-resolution-change", 0L);
          } 
        } else {
          ijkMediaPlayer1.setOption(4, "mediacodec", 0L);
        } 
        if (this.mSettings.getUsingOpenSLES()) {
          ijkMediaPlayer1.setOption(4, "opensles", 1L);
        } else {
          ijkMediaPlayer1.setOption(4, "opensles", 0L);
        } 
        String str = this.mSettings.getPixelFormat();
        if (TextUtils.isEmpty(str)) {
          ijkMediaPlayer1.setOption(4, "overlay-format", 842225234L);
        } else {
          ijkMediaPlayer1.setOption(4, "overlay-format", str);
        } 
        if (!TextUtils.isEmpty(this.iformat)) {
          ijkMediaPlayer1.setOption(4, "iformat", this.iformat);
        } else {
          ijkMediaPlayer1.setOption(4, "iformat", null);
        } 
        ijkMediaPlayer1.setOption(4, "framedrop", 2L);
        ijkMediaPlayer1.setOption(4, "start-on-prepared", 0L);
        ijkMediaPlayer1.setOption(1, "http-detect-range-support", 0L);
        ijkMediaPlayer1.setOption(2, "skip_loop_filter", 0L);
        ijkMediaPlayer1.setOption(4, "thread-type", 2L);
        if (this.av_sync_video_master)
          ijkMediaPlayer1.setOption(4, "av-sync-clock", 2L); 
        IjkMediaPlayer ijkMediaPlayer2 = ijkMediaPlayer1;
        if (this.videoCommunicator != null) {
          ijkMediaPlayer1.setOption(1, "analyzemaxduration", 120L);
          ijkMediaPlayer1.setOption(1, "probesize", 100L);
          ijkMediaPlayer1.setOption(1, "flush_packets", 1L);
          ijkMediaPlayer1.setOption(4, "packet-buffering", 0L);
          ijkMediaPlayer1.setOption(4, "max_cached_duration", 5L);
          ijkMediaPlayer1.setOption(4, "max-buffer-size", 50L);
          ijkMediaPlayer1.setOption(4, "infbuf", 1L);
          ijkMediaPlayer1.setOption(4, "accelerate-when-pictq-greater-than-num", 1L);
          ijkMediaPlayer2 = ijkMediaPlayer1;
        } 
      } else {
        androidMediaPlayer2 = null;
      } 
    } else {
      androidMediaPlayer2 = new AndroidMediaPlayer();
    } 
    AndroidMediaPlayer androidMediaPlayer1 = androidMediaPlayer2;
    if (this.mSettings.getEnableDetachedSurfaceTextureView())
      textureMediaPlayer = new TextureMediaPlayer((IMediaPlayer)androidMediaPlayer2); 
    return (IMediaPlayer)textureMediaPlayer;
  }
  
  public void deselectTrack(int paramInt) {
    MediaPlayerCompat.deselectTrack(this.mMediaPlayer, paramInt);
  }
  
  public void enterBackground() {
    MediaPlayerService.setMediaPlayer(this.mMediaPlayer);
  }
  
  protected void finalize() throws Throwable {
    super.finalize();
  }
  
  public int getAudioSessionId() {
    return 0;
  }
  
  public int getBufferPercentage() {
    return (this.mMediaPlayer != null) ? this.mCurrentBufferPercentage : 0;
  }
  
  public int getCurrentPosition() {
    return isInPlaybackState() ? (int)this.mMediaPlayer.getCurrentPosition() : 0;
  }
  
  public int getDuration() {
    return isInPlaybackState() ? (int)this.mMediaPlayer.getDuration() : -1;
  }
  
  public int getSelectedTrack(int paramInt) {
    return MediaPlayerCompat.getSelectedTrack(this.mMediaPlayer, paramInt);
  }
  
  public ITrackInfo[] getTrackInfo() {
    IMediaPlayer iMediaPlayer = this.mMediaPlayer;
    return (iMediaPlayer == null) ? null : iMediaPlayer.getTrackInfo();
  }
  
  public int getmVideoHeight() {
    return this.mVideoHeight;
  }
  
  public int getmVideoWidth() {
    return this.mVideoWidth;
  }
  
  public boolean isBackgroundPlayEnabled() {
    return this.mEnableBackgroundPlay;
  }
  
  public boolean isPlaying() {
    boolean bool;
    if (isInPlaybackState() && this.mMediaPlayer.isPlaying()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
    boolean bool;
    if (paramInt != 4 && paramInt != 24 && paramInt != 25 && paramInt != 164 && paramInt != 82 && paramInt != 5 && paramInt != 6) {
      bool = true;
    } else {
      bool = false;
    } 
    if (isInPlaybackState() && bool && this.mMediaController != null) {
      if (paramInt == 79 || paramInt == 85) {
        if (this.mMediaPlayer.isPlaying()) {
          pause();
          this.mMediaController.show();
        } else {
          start();
          this.mMediaController.hide();
        } 
        return true;
      } 
      if (paramInt == 126) {
        if (!this.mMediaPlayer.isPlaying()) {
          start();
          this.mMediaController.hide();
        } 
        return true;
      } 
      if (paramInt == 86 || paramInt == 127) {
        if (this.mMediaPlayer.isPlaying()) {
          pause();
          this.mMediaController.show();
        } 
        return true;
      } 
      toggleMediaControlsVisiblity();
    } 
    return super.onKeyDown(paramInt, paramKeyEvent);
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    if (this.isFHFishEyeTouchMode) {
      GestureDetector gestureDetector = this.fhGestureDetector;
      return (gestureDetector != null) ? gestureDetector.onTouchEvent(paramMotionEvent) : true;
    } 
    if (isInPlaybackState() && this.mMediaController != null)
      toggleMediaControlsVisiblity(); 
    return false;
  }
  
  public boolean onTrackballEvent(MotionEvent paramMotionEvent) {
    if (isInPlaybackState() && this.mMediaController != null)
      toggleMediaControlsVisiblity(); 
    return false;
  }
  
  public void openFHFishEyeMode(boolean paramBoolean1, final int fishEyeType, boolean paramBoolean2) {
    if (paramBoolean1 && paramBoolean2) {
      paramBoolean2 = true;
    } else {
      paramBoolean2 = false;
    } 
    this.isFHFishEyeTouchMode = paramBoolean2;
    IRenderView iRenderView = this.mRenderView;
    if (iRenderView instanceof IMultiFunctionRenderView) {
      final MultiFunctionRender render = ((IMultiFunctionRenderView)iRenderView).getMultiFunctionRender();
      MultiFunctionRenderRuntimeEnvironment multiFunctionRenderRuntimeEnvironment = ((IMultiFunctionRenderView)this.mRenderView).getMultiFunctionRenderRuntimeEnvironment();
      if (paramBoolean1) {
        multiFunctionRenderRuntimeEnvironment.setRenderMode(1);
        multiFunctionRender.setFHFishEyeMode(paramBoolean1, new Runnable() {
              public void run() {
                final FHFishEyeDrawingHelper fhFishEyeDrawingHelper = render.getFhFishEyeDrawingHelper();
                int i = fishEyeType;
                if (i != 0) {
                  if (i != 1) {
                    if (i != 2) {
                      if (i == 3)
                        fHFishEyeDrawingHelper.switchFishEyeMode(4, 0, false, false); 
                    } else {
                      fHFishEyeDrawingHelper.switchFishEyeMode(0, 0, false, true);
                    } 
                  } else {
                    fHFishEyeDrawingHelper.switchFishEyeMode(0, 3, false, false);
                  } 
                } else {
                  fHFishEyeDrawingHelper.switchFishEyeMode(0, 0, true, false);
                } 
                IjkVideoView.this.post(new Runnable() {
                      public void run() {
                        if (IjkVideoView.this.fhGestureDetectorListener != null) {
                          IjkVideoView.this.fhGestureDetectorListener.setFhFishEyeDrawingHelper(fhFishEyeDrawingHelper);
                          return;
                        } 
                        IjkVideoView.access$2902(IjkVideoView.this, new FHGestureDetectorListener(fhFishEyeDrawingHelper));
                        IjkVideoView.access$3002(IjkVideoView.this, new GestureDetector(IjkVideoView.this.getContext(), (GestureDetector.OnGestureListener)IjkVideoView.this.fhGestureDetectorListener));
                      }
                    });
              }
            });
      } else {
        multiFunctionRenderRuntimeEnvironment.setRenderMode(0);
        multiFunctionRender.setFHFishEyeMode(false, null);
        this.fhGestureDetector = null;
        this.fhGestureDetectorListener = null;
      } 
    } 
  }
  
  public void pause() {
    if (isInPlaybackState() && this.mMediaPlayer.isPlaying()) {
      this.mMediaPlayer.pause();
      this.mCurrentState = 4;
    } 
    this.mTargetState = 4;
  }
  
  public void release(boolean paramBoolean) {
    IMediaPlayer iMediaPlayer = this.mMediaPlayer;
    if (iMediaPlayer != null) {
      iMediaPlayer.reset();
      this.mMediaPlayer.release();
      this.mMediaPlayer = null;
      this.mCurrentState = 0;
      if (paramBoolean)
        this.mTargetState = 0; 
      ((AudioManager)this.mAppContext.getSystemService("audio")).abandonAudioFocus(null);
    } 
  }
  
  public void releaseWithoutStop() {
    IMediaPlayer iMediaPlayer = this.mMediaPlayer;
    if (iMediaPlayer != null)
      iMediaPlayer.setDisplay(null); 
  }
  
  public void resume() {
    openVideo();
  }
  
  public void seekTo(int paramInt) {
    if (isInPlaybackState()) {
      this.mSeekStartTime = System.currentTimeMillis();
      this.mMediaPlayer.seekTo(paramInt);
      this.mSeekWhenPrepared = 0;
    } else {
      this.mSeekWhenPrepared = paramInt;
    } 
  }
  
  public void selectTrack(int paramInt) {
    MediaPlayerCompat.selectTrack(this.mMediaPlayer, paramInt);
  }
  
  public void setAspectRatio(int paramInt) {
    this.mCurrentAspectRatio = paramInt;
    IRenderView iRenderView = this.mRenderView;
    if (iRenderView != null)
      iRenderView.setAspectRatio(this.mCurrentAspectRatio); 
  }
  
  public void setAv_sync_video_master(boolean paramBoolean) {
    this.av_sync_video_master = paramBoolean;
  }
  
  public void setGPUFilter(final GPUImageFilter filter) {
    IRenderView iRenderView = this.mRenderView;
    if (iRenderView instanceof IMultiFunctionRenderView) {
      final IMultiFunctionRenderView renderView = (IMultiFunctionRenderView)iRenderView;
      iMultiFunctionRenderView.getMultiFunctionRenderRuntimeEnvironment().queueEvent(new Runnable() {
            public void run() {
              renderView.getMultiFunctionRender().setFilter(filter);
            }
          });
    } 
  }
  
  public void setHudView(TableLayout paramTableLayout) {
    this.mHudViewHolder = new InfoHudViewHolder(getContext(), paramTableLayout);
  }
  
  public void setKeepVideoRatio(boolean paramBoolean) {
    IRenderView iRenderView = this.mRenderView;
    if (iRenderView instanceof IMultiFunctionRenderView)
      ((IMultiFunctionRenderView)iRenderView).getMultiFunctionRender().setKeepVideoRatio(paramBoolean); 
  }
  
  public void setMagnification(double paramDouble) {
    IRenderView iRenderView = this.mRenderView;
    if (iRenderView instanceof IMultiFunctionRenderView)
      ((IMultiFunctionRenderView)iRenderView).getMultiFunctionRender().setMagnification(paramDouble); 
  }
  
  public void setMediaController(IMediaController paramIMediaController) {
    IMediaController iMediaController = this.mMediaController;
    if (iMediaController != null)
      iMediaController.hide(); 
    this.mMediaController = paramIMediaController;
    attachMediaController();
  }
  
  public void setNoneParticleSystem() {
    IRenderView iRenderView = this.mRenderView;
    if (iRenderView instanceof IMultiFunctionRenderView)
      ((IMultiFunctionRenderView)iRenderView).getMultiFunctionRenderRuntimeEnvironment().queueEvent(new Runnable() {
            public void run() {
              if (IjkVideoView.this.particleLayer != null)
                IjkVideoView.this.particleLayer.destory(); 
              ((IMultiFunctionRenderView)IjkVideoView.this.mRenderView).getMultiFunctionRender().setParticleSystem(null);
            }
          }); 
  }
  
  public void setOnCompletionListener(IMediaPlayer.OnCompletionListener paramOnCompletionListener) {
    this.mOnCompletionListener = paramOnCompletionListener;
  }
  
  public void setOnErrorListener(IMediaPlayer.OnErrorListener paramOnErrorListener) {
    this.mOnErrorListener = paramOnErrorListener;
  }
  
  public void setOnFrameByteBufferCallBack(MultiFunctionRender.OnFrameBufferCallback paramOnFrameBufferCallback, int paramInt1, int paramInt2) {
    IRenderView iRenderView = this.mRenderView;
    if (iRenderView instanceof IMultiFunctionRenderView)
      ((IMultiFunctionRenderView)iRenderView).getMultiFunctionRender().setOnFrameByteBufferCallback(paramOnFrameBufferCallback, paramInt1, paramInt2); 
  }
  
  public void setOnInfoListener(IMediaPlayer.OnInfoListener paramOnInfoListener) {
    this.mOnInfoListener = paramOnInfoListener;
  }
  
  public void setOnPreparedListener(IMediaPlayer.OnPreparedListener paramOnPreparedListener) {
    this.mOnPreparedListener = paramOnPreparedListener;
  }
  
  public void setParticleSystem(final Context context, final int plistFile, final int resourceId) {
    IRenderView iRenderView = this.mRenderView;
    if (iRenderView instanceof IMultiFunctionRenderView)
      ((IMultiFunctionRenderView)iRenderView).getMultiFunctionRenderRuntimeEnvironment().queueEvent(new Runnable() {
            public void run() {
              if (IjkVideoView.this.particleLayer != null)
                IjkVideoView.this.particleLayer.destory(); 
              IjkVideoView.this.particleLayer = new ParticleLayer(context, plistFile, resourceId);
              ((IMultiFunctionRenderView)IjkVideoView.this.mRenderView).getMultiFunctionRender().setParticleSystem(IjkVideoView.this.particleLayer);
            }
          }); 
  }
  
  public void setRender(int paramInt) {
    if (paramInt != 0) {
      if (paramInt != 2) {
        if (paramInt != 3) {
          if (paramInt != 4) {
            Log.e(this.TAG, String.format(Locale.getDefault(), "invalid render %d\n", new Object[] { Integer.valueOf(paramInt) }));
          } else {
            TextureRenderView textureRenderView = new TextureRenderView(getContext());
            if (this.mMediaPlayer != null) {
              textureRenderView.getSurfaceHolder().bindToMediaPlayer(this.mMediaPlayer);
              textureRenderView.setVideoSize(this.mMediaPlayer.getVideoWidth(), this.mMediaPlayer.getVideoHeight());
              textureRenderView.setVideoSampleAspectRatio(this.mMediaPlayer.getVideoSarNum(), this.mMediaPlayer.getVideoSarDen());
              textureRenderView.setAspectRatio(this.mCurrentAspectRatio);
            } 
            setRenderView(textureRenderView);
          } 
        } else {
          setRenderView(new SurfaceRenderView(getContext()));
        } 
      } else {
        setRenderView((IRenderView)null);
      } 
    } else {
      setRenderView(new MultiFuncTextureRenderView(getContext()));
    } 
  }
  
  public void setRenderView(IRenderView paramIRenderView) {
    if (this.mRenderView != null) {
      IMediaPlayer iMediaPlayer = this.mMediaPlayer;
      if (iMediaPlayer != null)
        iMediaPlayer.setDisplay(null); 
      View view1 = this.mRenderView.getView();
      this.mRenderView.removeRenderCallback(this.mSHCallback);
      this.mRenderView = null;
      removeView(view1);
    } 
    if (paramIRenderView == null)
      return; 
    this.mRenderView = paramIRenderView;
    paramIRenderView.setAspectRatio(this.mCurrentAspectRatio);
    int i = this.mVideoWidth;
    if (i > 0) {
      int k = this.mVideoHeight;
      if (k > 0)
        paramIRenderView.setVideoSize(i, k); 
    } 
    int j = this.mVideoSarNum;
    if (j > 0) {
      i = this.mVideoSarDen;
      if (i > 0)
        paramIRenderView.setVideoSampleAspectRatio(j, i); 
    } 
    View view = this.mRenderView.getView();
    view.setLayoutParams((ViewGroup.LayoutParams)new FrameLayout.LayoutParams(-2, -2, 17));
    addView(view);
    this.mRenderView.addRenderCallback(this.mSHCallback);
    this.mRenderView.setVideoRotation(this.mVideoRotationDegree);
  }
  
  public void setUsingMediaCodec(boolean paramBoolean) {
    this.usingMediaCodec = paramBoolean;
  }
  
  public void setVRMode(boolean paramBoolean) {
    IRenderView iRenderView = this.mRenderView;
    if (iRenderView instanceof IMultiFunctionRenderView)
      ((IMultiFunctionRenderView)iRenderView).getMultiFunctionRender().setVRMode(paramBoolean); 
  }
  
  public void setVideoCommunicator(VideoCommunicator paramVideoCommunicator) {
    this.videoCommunicator = paramVideoCommunicator;
    if (paramVideoCommunicator == null)
      return; 
    videoCommunicatorsProtocol.addVideoCommunicator(paramVideoCommunicator);
    this.usingMediaCodec = paramVideoCommunicator.usingMediaCodeC();
    this.iformat = paramVideoCommunicator.videoStreamFormat();
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("VideoCommunicators://");
    stringBuilder.append(Integer.toHexString(paramVideoCommunicator.hashCode()));
    setVideoURI(Uri.parse(stringBuilder.toString()), (Map<String, String>)null);
  }
  
  public void setVideoPath(String paramString) {
    setVideoURI(Uri.parse(paramString));
  }
  
  public void setVideoURI(Uri paramUri) {
    setVideoURI(paramUri, (Map<String, String>)null);
  }
  
  public void showMediaInfo() {
    IMediaPlayer iMediaPlayer = this.mMediaPlayer;
    if (iMediaPlayer == null)
      return; 
    int i = MediaPlayerCompat.getSelectedTrack(iMediaPlayer, 1);
    int j = MediaPlayerCompat.getSelectedTrack(this.mMediaPlayer, 2);
    int k = MediaPlayerCompat.getSelectedTrack(this.mMediaPlayer, 3);
    TableLayoutBinder tableLayoutBinder = new TableLayoutBinder(getContext());
    tableLayoutBinder.appendSection(R.string.mi_player);
    tableLayoutBinder.appendRow2(R.string.mi_player, MediaPlayerCompat.getName(this.mMediaPlayer));
    tableLayoutBinder.appendSection(R.string.mi_media);
    tableLayoutBinder.appendRow2(R.string.mi_resolution, buildResolution(this.mVideoWidth, this.mVideoHeight, this.mVideoSarNum, this.mVideoSarDen));
    tableLayoutBinder.appendRow2(R.string.mi_length, buildTimeMilli(this.mMediaPlayer.getDuration()));
    ITrackInfo[] arrayOfITrackInfo = this.mMediaPlayer.getTrackInfo();
    if (arrayOfITrackInfo != null) {
      int m = arrayOfITrackInfo.length;
      byte b = 0;
      byte b1 = -1;
      while (b < m) {
        ITrackInfo iTrackInfo = arrayOfITrackInfo[b];
        b1++;
        int n = iTrackInfo.getTrackType();
        if (b1 == i) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append(getContext().getString(R.string.mi_stream_fmt1, new Object[] { Integer.valueOf(b1) }));
          stringBuilder.append(" ");
          stringBuilder.append(getContext().getString(R.string.mi__selected_video_track));
          tableLayoutBinder.appendSection(stringBuilder.toString());
        } else if (b1 == j) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append(getContext().getString(R.string.mi_stream_fmt1, new Object[] { Integer.valueOf(b1) }));
          stringBuilder.append(" ");
          stringBuilder.append(getContext().getString(R.string.mi__selected_audio_track));
          tableLayoutBinder.appendSection(stringBuilder.toString());
        } else if (b1 == k) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append(getContext().getString(R.string.mi_stream_fmt1, new Object[] { Integer.valueOf(b1) }));
          stringBuilder.append(" ");
          stringBuilder.append(getContext().getString(R.string.mi__selected_subtitle_track));
          tableLayoutBinder.appendSection(stringBuilder.toString());
        } else {
          tableLayoutBinder.appendSection(getContext().getString(R.string.mi_stream_fmt1, new Object[] { Integer.valueOf(b1) }));
        } 
        tableLayoutBinder.appendRow2(R.string.mi_type, buildTrackType(n));
        tableLayoutBinder.appendRow2(R.string.mi_language, buildLanguage(iTrackInfo.getLanguage()));
        IMediaFormat iMediaFormat = iTrackInfo.getFormat();
        if (iMediaFormat != null && iMediaFormat instanceof tv.danmaku.ijk.media.player_gx.misc.IjkMediaFormat)
          if (n != 1) {
            if (n == 2) {
              tableLayoutBinder.appendRow2(R.string.mi_codec, iMediaFormat.getString("ijk-codec-long-name-ui"));
              tableLayoutBinder.appendRow2(R.string.mi_profile_level, iMediaFormat.getString("ijk-profile-level-ui"));
              tableLayoutBinder.appendRow2(R.string.mi_sample_rate, iMediaFormat.getString("ijk-sample-rate-ui"));
              tableLayoutBinder.appendRow2(R.string.mi_channels, iMediaFormat.getString("ijk-channel-ui"));
              tableLayoutBinder.appendRow2(R.string.mi_bit_rate, iMediaFormat.getString("ijk-bit-rate-ui"));
            } 
          } else {
            tableLayoutBinder.appendRow2(R.string.mi_codec, iMediaFormat.getString("ijk-codec-long-name-ui"));
            tableLayoutBinder.appendRow2(R.string.mi_profile_level, iMediaFormat.getString("ijk-profile-level-ui"));
            tableLayoutBinder.appendRow2(R.string.mi_pixel_format, iMediaFormat.getString("ijk-pixel-format-ui"));
            tableLayoutBinder.appendRow2(R.string.mi_resolution, iMediaFormat.getString("ijk-resolution-ui"));
            tableLayoutBinder.appendRow2(R.string.mi_frame_rate, iMediaFormat.getString("ijk-frame-rate-ui"));
            tableLayoutBinder.appendRow2(R.string.mi_bit_rate, iMediaFormat.getString("ijk-bit-rate-ui"));
          }  
        b++;
      } 
    } 
    AlertDialog.Builder builder = tableLayoutBinder.buildAlertDialogBuilder();
    builder.setTitle(R.string.media_information);
    builder.setNegativeButton(R.string.close, null);
    builder.show();
  }
  
  public void start() {
    if (isInPlaybackState()) {
      this.mMediaPlayer.start();
      this.mCurrentState = 3;
    } 
    this.mTargetState = 3;
  }
  
  public void startRecord(int paramInt1, int paramInt2, int paramInt3, int paramInt4, String paramString, EncodeBitmapAndMux2Mp4.EncodeStatusCallback paramEncodeStatusCallback) {
    IRenderView iRenderView = this.mRenderView;
    if (iRenderView instanceof IMultiFunctionRenderView) {
      EGLContext eGLContext = ((IMultiFunctionRenderView)iRenderView).getMultiFunctionRenderRuntimeEnvironment().getEGLContext_14();
      int i = ((IMultiFunctionRenderView)this.mRenderView).getMultiFunctionRender().getProcessedTextureId();
      if (eGLContext == null || i <= 0) {
        if (paramEncodeStatusCallback != null) {
          paramEncodeStatusCallback.onEncoderErro("视频未开始播放");
          Log.e(this.TAG, "startRecord: 视频未开始播放");
        } 
        return;
      } 
      this.encodeBitmapAndMux2Mp4.setOPENGLContext(eGLContext, i);
      this.encodeBitmapAndMux2Mp4.startEncode(paramInt1, paramInt2, paramInt3, paramInt4, paramString, paramEncodeStatusCallback);
    } 
  }
  
  public void startRecord(int paramInt1, int paramInt2, int paramInt3, String paramString, EncodeBitmapAndMux2Mp4.EncodeStatusCallback paramEncodeStatusCallback) {
    startRecord(paramInt1, paramInt2, paramInt3, 25, paramString, paramEncodeStatusCallback);
  }
  
  public void stopBackgroundPlay() {
    MediaPlayerService.setMediaPlayer(null);
  }
  
  public void stopPlayback() {
    IMediaPlayer iMediaPlayer = this.mMediaPlayer;
    if (iMediaPlayer != null) {
      iMediaPlayer.stop();
      this.mMediaPlayer.release();
      this.mMediaPlayer = null;
      InfoHudViewHolder infoHudViewHolder = this.mHudViewHolder;
      if (infoHudViewHolder != null)
        infoHudViewHolder.setMediaPlayer(null); 
      this.mCurrentState = 0;
      this.mTargetState = 0;
      ((AudioManager)this.mAppContext.getSystemService("audio")).abandonAudioFocus(null);
    } 
  }
  
  public void stopRecord() {
    this.encodeBitmapAndMux2Mp4.stopEncode();
  }
  
  public void suspend() {
    release(false);
  }
  
  public int toggleAspectRatio() {
    int i = ++this.mCurrentAspectRatioIndex;
    int[] arrayOfInt = s_allAspectRatio;
    this.mCurrentAspectRatioIndex = i % arrayOfInt.length;
    this.mCurrentAspectRatio = arrayOfInt[this.mCurrentAspectRatioIndex];
    IRenderView iRenderView = this.mRenderView;
    if (iRenderView != null)
      iRenderView.setAspectRatio(this.mCurrentAspectRatio); 
    return this.mCurrentAspectRatio;
  }
  
  public int togglePlayer() {
    IMediaPlayer iMediaPlayer = this.mMediaPlayer;
    if (iMediaPlayer != null)
      iMediaPlayer.release(); 
    IRenderView iRenderView = this.mRenderView;
    if (iRenderView != null)
      iRenderView.getView().invalidate(); 
    openVideo();
    return this.mSettings.getPlayer();
  }
  
  public int toggleRender() {
    this.mCurrentRenderIndex++;
    this.mCurrentRenderIndex %= this.mAllRenders.size();
    this.mCurrentRender = ((Integer)this.mAllRenders.get(this.mCurrentRenderIndex)).intValue();
    setRender(this.mCurrentRender);
    return this.mCurrentRender;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/ijkvideoview/widget/media/IjkVideoView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */