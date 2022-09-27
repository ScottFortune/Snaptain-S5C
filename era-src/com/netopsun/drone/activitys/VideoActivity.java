package com.netopsun.drone.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import com.netopsun.deviceshub.base.VideoCommunicator;
import com.netopsun.drone.BaseSensorLandscapeActivity;
import com.netopsun.fhdevices.FHLocalVideoCommunicator;
import com.netopsun.ijkvideoview.widget.media.IjkVideoView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import java.io.File;
import java.util.concurrent.TimeUnit;
import tv.danmaku.ijk.media.player_gx.IMediaPlayer;

public class VideoActivity extends BaseSensorLandscapeActivity implements View.OnClickListener {
  public static final String VIDEOPATH = "videoPath";
  
  private View backBtn;
  
  private RelativeLayout controlContainer;
  
  private FHLocalVideoCommunicator fhLocalVideoCommunicator;
  
  private IjkVideoView ijkVideoView;
  
  private boolean isPlaying = true;
  
  private String path;
  
  private ImageView playBtn;
  
  private ImageView playSpeedDownBtn;
  
  private TextView playSpeedText;
  
  private ImageView playSpeedUpBtn;
  
  private Disposable updateProgressTask;
  
  private TextView videoCurrentTime;
  
  private SeekBar videoProgress;
  
  private TextView videoStopTime;
  
  private void initView() {
    this.ijkVideoView = (IjkVideoView)findViewById(2131230913);
    this.ijkVideoView.setOnClickListener(this);
    this.backBtn = findViewById(2131230785);
    this.backBtn.setOnClickListener(this);
    this.videoProgress = (SeekBar)findViewById(2131230791);
    this.videoProgress.setOnClickListener(this);
    this.videoStopTime = (TextView)findViewById(2131230793);
    this.videoStopTime.setOnClickListener(this);
    this.videoCurrentTime = (TextView)findViewById(2131230787);
    this.videoCurrentTime.setOnClickListener(this);
    this.playSpeedText = (TextView)findViewById(2131230792);
    this.playSpeedText.setOnClickListener(this);
    this.playBtn = (ImageView)findViewById(2131230788);
    this.playBtn.setOnClickListener(this);
    this.playSpeedDownBtn = (ImageView)findViewById(2131230789);
    this.playSpeedDownBtn.setOnClickListener(this);
    this.playSpeedUpBtn = (ImageView)findViewById(2131230790);
    this.playSpeedUpBtn.setOnClickListener(this);
    this.controlContainer = (RelativeLayout)findViewById(2131230786);
    this.controlContainer.setOnClickListener(this);
  }
  
  public static void launch(Context paramContext, String paramString) {
    Intent intent = new Intent(paramContext, VideoActivity.class);
    intent.putExtra("videoPath", paramString);
    paramContext.startActivity(intent);
  }
  
  private void refresh() {
    if (!isDestroyed()) {
      long l1 = this.ijkVideoView.getCurrentPosition() / 1000L;
      long l2 = this.ijkVideoView.getDuration() / 1000L;
      FHLocalVideoCommunicator fHLocalVideoCommunicator = this.fhLocalVideoCommunicator;
      if (fHLocalVideoCommunicator != null) {
        l2 = fHLocalVideoCommunicator.getPlaybackDuration() / 1000L;
        l1 = this.fhLocalVideoCommunicator.getPlaybackCurrentPosition() / 1000L;
      } 
      long l3 = l1 % 3600L / 60L;
      long l4 = l1 / 3600L;
      long l5 = l2 % 3600L / 60L;
      long l6 = l2 / 3600L;
      TextView textView2 = this.videoCurrentTime;
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append(String.format("%02d", new Object[] { Long.valueOf(l4) }));
      stringBuilder1.append(":");
      stringBuilder1.append(String.format("%02d", new Object[] { Long.valueOf(l3) }));
      stringBuilder1.append(":");
      stringBuilder1.append(String.format("%02d", new Object[] { Long.valueOf(l1 % 60L) }));
      textView2.setText(stringBuilder1.toString());
      TextView textView1 = this.videoStopTime;
      StringBuilder stringBuilder2 = new StringBuilder();
      stringBuilder2.append(String.format("%02d", new Object[] { Long.valueOf(l6) }));
      stringBuilder2.append(":");
      stringBuilder2.append(String.format("%02d", new Object[] { Long.valueOf(l5) }));
      stringBuilder2.append(":");
      stringBuilder2.append(String.format("%02d", new Object[] { Long.valueOf(l2 % 60L) }));
      textView1.setText(stringBuilder2.toString());
      if (l2 != 0L)
        this.videoProgress.setProgress((int)((float)l1 * 1.0F / (float)l2 * 100.0F)); 
    } 
  }
  
  public void onClick(View paramView) {
    int i = paramView.getId();
    if (i != 2131230913) {
      FHLocalVideoCommunicator fHLocalVideoCommunicator;
      switch (i) {
        case 2131230788:
          fHLocalVideoCommunicator = this.fhLocalVideoCommunicator;
          if (fHLocalVideoCommunicator != null) {
            if (this.isPlaying) {
              fHLocalVideoCommunicator.pause();
            } else {
              fHLocalVideoCommunicator.continuePlay();
            } 
          } else if (this.isPlaying) {
            this.ijkVideoView.pause();
          } else {
            this.ijkVideoView.start();
          } 
          this.isPlaying ^= 0x1;
          break;
        case 2131230785:
          finish();
          break;
      } 
    } 
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    requestWindowFeature(1);
    getWindow().setFlags(1024, 1024);
    this.path = getIntent().getStringExtra("videoPath");
    if (this.path == null && paramBundle != null)
      this.path = paramBundle.getString("videoPath"); 
    if (this.path == null)
      finish(); 
    setContentView(2131427370);
    initView();
    String str = this.path.replace("avi", "h264");
    if (this.path.toLowerCase().contains("avi") && (new File(str)).exists()) {
      this.fhLocalVideoCommunicator = new FHLocalVideoCommunicator(str);
      this.ijkVideoView.setVideoCommunicator((VideoCommunicator)this.fhLocalVideoCommunicator);
    } 
    if (this.fhLocalVideoCommunicator == null)
      this.ijkVideoView.setVideoPath(this.path); 
    this.ijkVideoView.start();
    this.ijkVideoView.setUsingMediaCodec(false);
    this.ijkVideoView.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
          public void onCompletion(IMediaPlayer param1IMediaPlayer) {
            VideoActivity.this.ijkVideoView.seekTo(0);
            VideoActivity.this.ijkVideoView.start();
          }
        });
    this.videoProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
          public void onProgressChanged(SeekBar param1SeekBar, int param1Int, boolean param1Boolean) {
            if (param1Boolean)
              if (VideoActivity.this.fhLocalVideoCommunicator != null) {
                VideoActivity.this.fhLocalVideoCommunicator.seek(param1Int, 0);
              } else {
                IjkVideoView ijkVideoView = VideoActivity.this.ijkVideoView;
                double d = (param1Int * VideoActivity.this.ijkVideoView.getDuration());
                Double.isNaN(d);
                ijkVideoView.seekTo((int)(d / 100.0D));
              }  
          }
          
          public void onStartTrackingTouch(SeekBar param1SeekBar) {}
          
          public void onStopTrackingTouch(SeekBar param1SeekBar) {}
        });
  }
  
  protected void onPause() {
    super.onPause();
    Disposable disposable = this.updateProgressTask;
    if (disposable != null)
      disposable.dispose(); 
    this.ijkVideoView.stopPlayback();
    this.ijkVideoView.release(true);
    this.ijkVideoView.stopBackgroundPlay();
  }
  
  protected void onResume() {
    super.onResume();
    this.updateProgressTask = Observable.interval(50L, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() {
          public void accept(Long param1Long) throws Exception {
            VideoActivity.this.refresh();
          }
        });
  }
  
  protected void onSaveInstanceState(Bundle paramBundle) {
    super.onSaveInstanceState(paramBundle);
    String str = this.path;
    if (str != null)
      paramBundle.putString("videoPath", str); 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/drone/activitys/VideoActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */