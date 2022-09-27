package com.guanxukeji.videobackgroundmusicpicker.activity;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import com.bumptech.glide.Glide;
import com.guanxukeji.videobackgroundmusicpicker.R;
import com.guanxukeji.videobackgroundmusicpicker.SharedPreferenceHelper;
import com.guanxukeji.videobackgroundmusicpicker.bean.MusicBean;
import com.guanxukeji.videobackgroundmusicpicker.utils.DateUtils;
import com.guanxukeji.videobackgroundmusicpicker.utils.Music2M4A;
import com.guanxukeji.videobackgroundmusicpicker.widget.LyricView;
import com.guanxukeji.videobackgroundmusicpicker.widget.MusicControlSeekbar;
import io.reactivex.ObservableEmitter;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.List;
import java.util.Map;

public class MusicClipActivity extends AppCompatActivity {
  private static int currentMusicPosition;
  
  private static List<MusicBean> musicBeanList;
  
  long currentPlayTime = 0L;
  
  private TextView endTimeText;
  
  private LyricView lyricView;
  
  private ToggleButton lyricsPicToggleBtn;
  
  private MediaPlayer mediaPlayer;
  
  private ImageView musicControlNextBtn;
  
  private ImageView musicControlPreviousBtn;
  
  private MusicControlSeekbar musicControlSeekbar;
  
  private ToggleButton playPauseToggleBtn;
  
  private ProgressDialog progressDialog;
  
  private ImageView quitBtn;
  
  private TextView resetAudioTimeBtn;
  
  private TextView setToBackgroundMusicBtn;
  
  private TextView setToEndTimeBtn;
  
  private TextView setToStartTimeBtn;
  
  private ImageView singerImage;
  
  private TextView singerText;
  
  private TextView songText;
  
  private TextView startTimeText;
  
  private UpdateSeekbarHandler updateSeekbarHandler;
  
  public static boolean copyAssetsFile(Application paramApplication, ObservableEmitter paramObservableEmitter, String paramString1, String paramString2) throws IOException {
    String str1;
    String str2;
    Exception exception;
    FileOutputStream fileOutputStream;
    File file1 = new File(paramString2);
    File file2 = new File(paramString2.substring(0, paramString2.lastIndexOf(File.separator)));
    if (!file2.exists())
      file2.mkdirs(); 
    if (file1.exists())
      file1.delete(); 
    file2 = null;
    try {
    
    } catch (Exception null) {
    
    } finally {
      paramApplication = null;
      paramString1 = null;
    } 
    try {
      paramString1.printStackTrace();
      return false;
    } finally {
      paramString2 = null;
      exception = iOException1;
    } 
    if (exception != null)
      try {
        exception.close();
      } catch (IOException iOException2) {} 
    if (iOException2 != null)
      iOException2.close(); 
    throw str1;
  }
  
  private static String getFileMD5(File paramFile) {
    if (!paramFile.isFile())
      return null; 
    byte[] arrayOfByte = new byte[1024];
    try {
      MessageDigest messageDigest = MessageDigest.getInstance("MD5");
      FileInputStream fileInputStream = new FileInputStream();
      this(paramFile);
      while (true) {
        int i = fileInputStream.read(arrayOfByte, 0, 1024);
        if (i != -1) {
          messageDigest.update(arrayOfByte, 0, i);
          continue;
        } 
        fileInputStream.close();
        return (new BigInteger(1, messageDigest.digest())).toString(16);
      } 
    } catch (Exception exception) {
      exception.printStackTrace();
      return null;
    } 
  }
  
  public static void launch(Activity paramActivity, List<MusicBean> paramList, int paramInt) {
    musicBeanList = paramList;
    currentMusicPosition = paramInt;
    paramActivity.startActivity(new Intent((Context)paramActivity, MusicClipActivity.class));
  }
  
  private void prepareCurrentMusic() {
    this.mediaPlayer.reset();
    this.playPauseToggleBtn.setChecked(false);
    this.musicControlSeekbar.getMusicSeekbar().setProgress(0);
    try {
      MusicBean musicBean = musicBeanList.get(currentMusicPosition);
      if (musicBean.getFilename().contains("file:///android_asset/")) {
        String str = musicBean.getFilename().replace("file:///android_asset/", "");
        AssetFileDescriptor assetFileDescriptor = getAssets().openFd(str);
        this.mediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
      } else {
        this.mediaPlayer.setDataSource(musicBean.getFilename());
      } 
      this.mediaPlayer.prepareAsync();
      Glide.with((FragmentActivity)this).load(musicBean.getIcon()).into(this.singerImage);
      this.songText.setText(musicBean.getName());
      this.singerText.setText(musicBean.getSinger());
      this.lyricView.setLyricFile(musicBean.getLrcname(), "utf-8");
    } catch (IOException iOException) {
      iOException.printStackTrace();
      Toast.makeText((Context)this, getString(R.string.open_file_fail), 0).show();
    } 
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    if (musicBeanList == null) {
      String str = getLocalClassName();
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Please call ");
      stringBuilder.append(getLocalClassName());
      stringBuilder.append(".launch(Activity activity, List<MusicBean> musicBeanList,\n                              int currentMusicPosition);");
      Log.e(str, stringBuilder.toString());
      finish();
    } 
    requestWindowFeature(1);
    getWindow().setFlags(1024, 1024);
    setContentView(R.layout.activity_music_clip);
    this.musicControlSeekbar = (MusicControlSeekbar)findViewById(R.id.music_control_seekbar);
    this.startTimeText = (TextView)findViewById(R.id.start_time_text);
    this.musicControlPreviousBtn = (ImageView)findViewById(R.id.music_control_previous_btn);
    this.musicControlNextBtn = (ImageView)findViewById(R.id.music_control_next_btn);
    this.resetAudioTimeBtn = (TextView)findViewById(R.id.reset_audio_time_btn);
    this.endTimeText = (TextView)findViewById(R.id.end_time_text);
    this.lyricView = (LyricView)findViewById(R.id.lyric_view);
    this.playPauseToggleBtn = (ToggleButton)findViewById(R.id.play_pause_toggle_btn);
    this.setToBackgroundMusicBtn = (TextView)findViewById(R.id.set_to_background_music_btn);
    this.quitBtn = (ImageView)findViewById(R.id.quit_btn);
    this.songText = (TextView)findViewById(R.id.song_text);
    this.lyricsPicToggleBtn = (ToggleButton)findViewById(R.id.lyrics_pic_toggle_btn);
    this.singerText = (TextView)findViewById(R.id.singer_text);
    this.setToStartTimeBtn = (TextView)findViewById(R.id.set_to_start_time_btn);
    this.singerImage = (ImageView)findViewById(R.id.singer_image);
    this.setToEndTimeBtn = (TextView)findViewById(R.id.set_to_end_time_btn);
    this.lyricView.setTouchable(false);
    this.musicControlPreviousBtn.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            MusicClipActivity.this.mediaPlayer.stop();
            if (MusicClipActivity.access$106() < 0) {
              MusicClipActivity.access$104();
              Toast.makeText(MusicClipActivity.this.getApplicationContext(), MusicClipActivity.this.getString(R.string.no_more_music), 0).show();
            } 
            MusicClipActivity.this.prepareCurrentMusic();
          }
        });
    this.musicControlNextBtn.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            MusicClipActivity.this.mediaPlayer.stop();
            if (MusicClipActivity.access$104() >= MusicClipActivity.musicBeanList.size()) {
              MusicClipActivity.access$106();
              Toast.makeText(MusicClipActivity.this.getApplicationContext(), MusicClipActivity.this.getString(R.string.no_more_music), 0).show();
            } 
            MusicClipActivity.this.prepareCurrentMusic();
          }
        });
    this.playPauseToggleBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
          public void onCheckedChanged(CompoundButton param1CompoundButton, boolean param1Boolean) {
            if (param1Boolean) {
              MusicClipActivity.this.mediaPlayer.start();
            } else if (MusicClipActivity.this.mediaPlayer.isPlaying()) {
              MusicClipActivity.this.mediaPlayer.pause();
            } 
          }
        });
    this.lyricsPicToggleBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
          public void onCheckedChanged(CompoundButton param1CompoundButton, boolean param1Boolean) {
            if (param1Boolean) {
              MusicClipActivity.this.lyricView.setVisibility(0);
            } else {
              MusicClipActivity.this.lyricView.setVisibility(4);
            } 
          }
        });
    this.musicControlSeekbar.getMusicSeekbar().setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
          public void onProgressChanged(SeekBar param1SeekBar, int param1Int, boolean param1Boolean) {
            MusicClipActivity.this.lyricView.setCurrentTimeMillis(param1Int);
            if (param1Boolean)
              MusicClipActivity.this.mediaPlayer.seekTo(param1Int); 
          }
          
          public void onStartTrackingTouch(SeekBar param1SeekBar) {}
          
          public void onStopTrackingTouch(SeekBar param1SeekBar) {}
        });
    this.resetAudioTimeBtn.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            SharedPreferenceHelper.saveStartTime(MusicClipActivity.this.getApplicationContext(), "00:00");
            SharedPreferenceHelper.saveEndTime(MusicClipActivity.this.getApplicationContext(), "00:00");
            MusicClipActivity.this.startTimeText.setText("Time:00:00");
            MusicClipActivity.this.endTimeText.setText("Time:00:00");
          }
        });
    this.setToStartTimeBtn.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            int i = MusicClipActivity.this.musicControlSeekbar.getMusicSeekbar().getProgress();
            if (i > 3600000) {
              Toast.makeText(MusicClipActivity.this.getApplicationContext(), MusicClipActivity.this.getString(R.string.audio_too_long), 0).show();
              return;
            } 
            TextView textView = MusicClipActivity.this.startTimeText;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Time:");
            i /= 1000;
            stringBuilder.append(DateUtils.secToTime(i));
            textView.setText(stringBuilder.toString());
            SharedPreferenceHelper.saveStartTime(MusicClipActivity.this.getApplicationContext(), DateUtils.secToTime(i));
          }
        });
    this.setToEndTimeBtn.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            int i = MusicClipActivity.this.musicControlSeekbar.getMusicSeekbar().getProgress();
            if (i > 3600000) {
              Toast.makeText(MusicClipActivity.this.getApplicationContext(), MusicClipActivity.this.getString(R.string.audio_too_long), 0).show();
              return;
            } 
            TextView textView = MusicClipActivity.this.endTimeText;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Time:");
            i /= 1000;
            stringBuilder.append(DateUtils.secToTime(i));
            textView.setText(stringBuilder.toString());
            SharedPreferenceHelper.saveEndTime(MusicClipActivity.this.getApplicationContext(), DateUtils.secToTime(i));
          }
        });
    this.setToBackgroundMusicBtn.setOnClickListener(new View.OnClickListener() {
          Disposable disposable;
          
          public void onClick(View param1View) {
            final MusicClipActivity context;
            Context context = MusicClipActivity.this.getApplicationContext();
            final String bgmName = ((MusicBean)MusicClipActivity.musicBeanList.get(MusicClipActivity.currentMusicPosition)).getName();
            int i = DateUtils.timeToSec(SharedPreferenceHelper.getEndTime(MusicClipActivity.this.getApplicationContext()));
            int j = DateUtils.timeToSec(SharedPreferenceHelper.getStartTime(MusicClipActivity.this.getApplicationContext()));
            i -= j;
            if (i < 1) {
              musicClipActivity1 = MusicClipActivity.this;
              Toast.makeText((Context)musicClipActivity1, musicClipActivity1.getString(R.string.wrong_time_please_retry), 0).show();
              return;
            } 
            Disposable disposable = this.disposable;
            if (disposable != null && !disposable.isDisposed()) {
              musicClipActivity1 = MusicClipActivity.this;
              Toast.makeText((Context)musicClipActivity1, musicClipActivity1.getString(R.string.exporting_audio), 0).show();
              return;
            } 
            if (MusicClipActivity.this.progressDialog != null && MusicClipActivity.this.progressDialog.isShowing())
              MusicClipActivity.this.progressDialog.dismiss(); 
            MusicClipActivity musicClipActivity2 = MusicClipActivity.this;
            MusicClipActivity.access$802(musicClipActivity2, new ProgressDialog((Context)musicClipActivity2));
            MusicClipActivity.this.progressDialog.setProgressStyle(0);
            MusicClipActivity.this.progressDialog.setMessage(MusicClipActivity.this.getString(R.string.background_music_is_being_exported0_which_may_take_a_few_minutes));
            MusicClipActivity.this.progressDialog.setCancelable(true);
            MusicClipActivity.this.progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                  public void onCancel(DialogInterface param2DialogInterface) {
                    if (MusicClipActivity.null.this.disposable != null)
                      MusicClipActivity.null.this.disposable.dispose(); 
                  }
                });
            MusicClipActivity.this.progressDialog.show();
            this.disposable = Music2M4A.outputBGMWithHardware(MusicClipActivity.this.getApplication(), ((MusicBean)MusicClipActivity.musicBeanList.get(MusicClipActivity.currentMusicPosition)).getFilename(), SharedPreferenceHelper.getBackgroundMusicFilePath(MusicClipActivity.this.getApplicationContext()), j, i, new Consumer<Map<Boolean, String>>() {
                  public void accept(Map<Boolean, String> param2Map) throws Exception {
                    if (MusicClipActivity.this.progressDialog != null && MusicClipActivity.this.progressDialog.isShowing())
                      MusicClipActivity.this.progressDialog.dismiss(); 
                    boolean bool = param2Map.containsKey(Boolean.valueOf(true));
                    if (bool) {
                      Toast.makeText(context, MusicClipActivity.this.getString(R.string.configure_sucess), 0).show();
                      SharedPreferenceHelper.saveBackgroundMusicName(context, bgmName);
                      SharedPreferenceHelper.saveOpenBackgroundMusic((Context)MusicClipActivity.this.getApplication(), true);
                    } else {
                      Toast.makeText(context, param2Map.get(Boolean.valueOf(bool)), 0).show();
                    } 
                  }
                });
          }
        });
    this.quitBtn.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            MusicClipActivity.this.finish();
          }
        });
    this.mediaPlayer = new MediaPlayer();
    prepareCurrentMusic();
    this.mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
          public void onPrepared(MediaPlayer param1MediaPlayer) {
            int i = MusicClipActivity.this.mediaPlayer.getDuration();
            MusicClipActivity.this.musicControlSeekbar.setMusicDuration(i);
            TextView textView = MusicClipActivity.this.endTimeText;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Time:");
            i /= 1000;
            stringBuilder.append(DateUtils.secToTime(i));
            textView.setText(stringBuilder.toString());
            SharedPreferenceHelper.saveEndTime(MusicClipActivity.this.getApplicationContext(), DateUtils.secToTime(i));
            MusicClipActivity.this.startTimeText.setText("Time:00:00");
            SharedPreferenceHelper.saveStartTime(MusicClipActivity.this.getApplicationContext(), "00:00");
          }
        });
    this.mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
          public boolean onError(MediaPlayer param1MediaPlayer, int param1Int1, int param1Int2) {
            Toast.makeText(MusicClipActivity.this.getApplicationContext(), MusicClipActivity.this.getString(R.string.audio_play_fail), 0).show();
            MusicClipActivity.this.playPauseToggleBtn.setChecked(false);
            return false;
          }
        });
    this.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
          public void onCompletion(MediaPlayer param1MediaPlayer) {
            MusicClipActivity.this.playPauseToggleBtn.setChecked(false);
          }
        });
    this.updateSeekbarHandler = new UpdateSeekbarHandler(this);
  }
  
  protected void onDestroy() {
    super.onDestroy();
    this.mediaPlayer.release();
    musicBeanList = null;
    currentMusicPosition = 0;
  }
  
  protected void onPause() {
    super.onPause();
    this.updateSeekbarHandler.stop();
    this.playPauseToggleBtn.setChecked(false);
    if (this.mediaPlayer.isPlaying())
      this.mediaPlayer.pause(); 
  }
  
  protected void onStart() {
    super.onStart();
    this.updateSeekbarHandler.start();
  }
  
  public void updateMusicContolSeekbarProgress() {
    MediaPlayer mediaPlayer = this.mediaPlayer;
    if (mediaPlayer != null || mediaPlayer.isPlaying())
      this.musicControlSeekbar.getMusicSeekbar().setProgress(this.mediaPlayer.getCurrentPosition()); 
  }
  
  private static class UpdateSeekbarHandler extends Handler {
    boolean isStop = false;
    
    WeakReference<MusicClipActivity> musicClipActivityWeakReference;
    
    int updateIntervalMillis = 100;
    
    UpdateSeekbarHandler(MusicClipActivity param1MusicClipActivity) {
      this.musicClipActivityWeakReference = new WeakReference<MusicClipActivity>(param1MusicClipActivity);
    }
    
    public void handleMessage(Message param1Message) {
      super.handleMessage(param1Message);
      WeakReference<MusicClipActivity> weakReference = this.musicClipActivityWeakReference;
      if (weakReference != null) {
        MusicClipActivity musicClipActivity = weakReference.get();
        if (musicClipActivity != null && !this.isStop) {
          musicClipActivity.updateMusicContolSeekbarProgress();
          postDelayed(new Runnable() {
                public void run() {
                  MusicClipActivity.UpdateSeekbarHandler.this.sendEmptyMessage(0);
                }
              },  this.updateIntervalMillis);
        } 
      } 
    }
    
    public void start() {
      this.isStop = false;
      sendEmptyMessage(0);
    }
    
    public void stop() {
      this.isStop = true;
    }
  }
  
  class null implements Runnable {
    public void run() {
      this.this$0.sendEmptyMessage(0);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/guanxukeji/videobackgroundmusicpicker/activity/MusicClipActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */