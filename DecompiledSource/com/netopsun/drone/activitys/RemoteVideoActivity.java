package com.netopsun.drone.activitys;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import com.netopsun.deviceshub.base.Devices;
import com.netopsun.drone.BaseSensorLandscapeActivity;
import com.netopsun.drone.DevicesUtil;
import com.netopsun.ijkvideoview.widget.media.IjkVideoView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import java.util.concurrent.TimeUnit;
import tv.danmaku.ijk.media.player_gx.IMediaPlayer;

public class RemoteVideoActivity extends BaseSensorLandscapeActivity implements View.OnClickListener {
  private View backBtn;
  
  private RelativeLayout controlContainer;
  
  private Devices devices;
  
  private long fakeProgress;
  
  private IjkVideoView ijkVideoView;
  
  private TextView playSpeedText;
  
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
    this.videoStopTime = (TextView)findViewById(2131230793);
    this.videoCurrentTime = (TextView)findViewById(2131230787);
    this.playSpeedText = (TextView)findViewById(2131230792);
    this.controlContainer = (RelativeLayout)findViewById(2131230786);
  }
  
  private void refresh() {
    if (!isDestroyed()) {
      long l1 = this.devices.getVideoCommunicator().getPlaybackDuration();
      long l2 = this.devices.getVideoCommunicator().getPlaybackCurrentPosition();
      long l3 = this.fakeProgress;
      if (l3 < l1)
        this.fakeProgress = l3 + 1000L; 
      l3 = l2;
      if (l2 == 0L)
        l3 = this.fakeProgress; 
      l3 /= 1000L;
      long l4 = l1 / 1000L;
      long l5 = l3 % 3600L / 60L;
      long l6 = l3 / 3600L;
      l2 = l4 % 3600L / 60L;
      l1 = l4 / 3600L;
      TextView textView = this.videoCurrentTime;
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(String.format("%02d", new Object[] { Long.valueOf(l6) }));
      stringBuilder.append(":");
      stringBuilder.append(String.format("%02d", new Object[] { Long.valueOf(l5) }));
      stringBuilder.append(":");
      stringBuilder.append(String.format("%02d", new Object[] { Long.valueOf(l3 % 60L) }));
      textView.setText(stringBuilder.toString());
      textView = this.videoStopTime;
      stringBuilder = new StringBuilder();
      stringBuilder.append(String.format("%02d", new Object[] { Long.valueOf(l1) }));
      stringBuilder.append(":");
      stringBuilder.append(String.format("%02d", new Object[] { Long.valueOf(l2) }));
      stringBuilder.append(":");
      stringBuilder.append(String.format("%02d", new Object[] { Long.valueOf(l4 % 60L) }));
      textView.setText(stringBuilder.toString());
      if (l4 != 0L)
        this.videoProgress.setProgress((int)((float)l3 * 100.0F / (float)l4)); 
    } 
  }
  
  public void onClick(View paramView) {
    if (paramView.getId() == 2131230785)
      finish(); 
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    requestWindowFeature(1);
    getWindow().setFlags(1024, 1024);
    this.devices = DevicesUtil.getCurrentConnectDevices();
    if (this.devices == null) {
      finish();
      return;
    } 
    setContentView(2131427367);
    initView();
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
    this.ijkVideoView.setVideoCommunicator(this.devices.getVideoCommunicator());
    this.ijkVideoView.setRender(0);
    this.ijkVideoView.start();
    this.ijkVideoView.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
          public void onPrepared(IMediaPlayer param1IMediaPlayer) {
            RemoteVideoActivity.access$002(RemoteVideoActivity.this, 0L);
            RemoteVideoActivity.access$102(RemoteVideoActivity.this, Observable.interval(1L, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() {
                    public void accept(Long param2Long) throws Exception {
                      RemoteVideoActivity.this.refresh();
                    }
                  }));
          }
        });
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/drone/activitys/RemoteVideoActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */