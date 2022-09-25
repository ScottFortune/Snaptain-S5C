package com.guanxukeji.videobackgroundmusicpicker.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatSeekBar;
import com.guanxukeji.videobackgroundmusicpicker.R;
import com.guanxukeji.videobackgroundmusicpicker.utils.DateUtils;

public class MusicControlSeekbar extends FrameLayout {
  private TextView currentTimeText;
  
  private CustomSeekbar musicSeekbar;
  
  private TextView totalTimeText;
  
  public MusicControlSeekbar(Context paramContext) {
    super(paramContext);
    init();
  }
  
  public MusicControlSeekbar(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    init();
  }
  
  public MusicControlSeekbar(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    init();
  }
  
  public MusicControlSeekbar(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2) {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    init();
  }
  
  private void init() {
    inflate(getContext(), R.layout.seek_bar_music_control, (ViewGroup)this);
    this.totalTimeText = (TextView)findViewById(R.id.total_time_text);
    this.musicSeekbar = (CustomSeekbar)findViewById(R.id.music_seekbar);
    this.currentTimeText = (TextView)findViewById(R.id.current_time_text);
    this.currentTimeText.setEnabled(false);
    this.totalTimeText.setEnabled(false);
    this.musicSeekbar.setOnSeekBarChangeExtraListener(new SeekBar.OnSeekBarChangeListener() {
          public void onProgressChanged(SeekBar param1SeekBar, int param1Int, boolean param1Boolean) {
            float f;
            if (param1SeekBar.getWidth() * param1Int / param1SeekBar.getMax() < MusicControlSeekbar.this.currentTimeText.getWidth() / 2) {
              f = param1SeekBar.getX();
            } else {
              int i;
              if (param1SeekBar.getWidth() * param1Int / param1SeekBar.getMax() > param1SeekBar.getWidth() - MusicControlSeekbar.this.currentTimeText.getWidth() / 2) {
                f = param1SeekBar.getX() + param1SeekBar.getWidth();
                i = MusicControlSeekbar.this.currentTimeText.getWidth();
              } else {
                f = param1SeekBar.getX() + (param1SeekBar.getWidth() * param1Int / param1SeekBar.getMax());
                i = MusicControlSeekbar.this.currentTimeText.getWidth() / 2;
              } 
              f -= i;
            } 
            MusicControlSeekbar.this.currentTimeText.setX(f);
            MusicControlSeekbar.this.currentTimeText.setText(DateUtils.secToTime(param1Int / 1000));
          }
          
          public void onStartTrackingTouch(SeekBar param1SeekBar) {}
          
          public void onStopTrackingTouch(SeekBar param1SeekBar) {}
        });
  }
  
  public CustomSeekbar getMusicSeekbar() {
    return this.musicSeekbar;
  }
  
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    ((ViewGroup)getParent()).setClipChildren(false);
  }
  
  public void setMusicDuration(int paramInt) {
    this.musicSeekbar.setMax(paramInt);
    this.totalTimeText.setText(DateUtils.secToTime(paramInt / 1000));
  }
  
  public static class CustomSeekbar extends AppCompatSeekBar {
    private SeekBar.OnSeekBarChangeListener extraListener;
    
    private SeekBar.OnSeekBarChangeListener listener;
    
    private final SeekBar.OnSeekBarChangeListener multiplexingListener = new SeekBar.OnSeekBarChangeListener() {
        public void onProgressChanged(SeekBar param2SeekBar, int param2Int, boolean param2Boolean) {
          if (MusicControlSeekbar.CustomSeekbar.this.listener != null)
            MusicControlSeekbar.CustomSeekbar.this.listener.onProgressChanged(param2SeekBar, param2Int, param2Boolean); 
          if (MusicControlSeekbar.CustomSeekbar.this.extraListener != null)
            MusicControlSeekbar.CustomSeekbar.this.extraListener.onProgressChanged(param2SeekBar, param2Int, param2Boolean); 
        }
        
        public void onStartTrackingTouch(SeekBar param2SeekBar) {
          if (MusicControlSeekbar.CustomSeekbar.this.listener != null)
            MusicControlSeekbar.CustomSeekbar.this.listener.onStartTrackingTouch(param2SeekBar); 
          if (MusicControlSeekbar.CustomSeekbar.this.extraListener != null)
            MusicControlSeekbar.CustomSeekbar.this.extraListener.onStartTrackingTouch(param2SeekBar); 
        }
        
        public void onStopTrackingTouch(SeekBar param2SeekBar) {
          if (MusicControlSeekbar.CustomSeekbar.this.listener != null)
            MusicControlSeekbar.CustomSeekbar.this.listener.onStopTrackingTouch(param2SeekBar); 
          if (MusicControlSeekbar.CustomSeekbar.this.extraListener != null)
            MusicControlSeekbar.CustomSeekbar.this.extraListener.onStopTrackingTouch(param2SeekBar); 
        }
      };
    
    public CustomSeekbar(Context param1Context) {
      super(param1Context);
    }
    
    public CustomSeekbar(Context param1Context, AttributeSet param1AttributeSet) {
      super(param1Context, param1AttributeSet);
    }
    
    public CustomSeekbar(Context param1Context, AttributeSet param1AttributeSet, int param1Int) {
      super(param1Context, param1AttributeSet, param1Int);
    }
    
    private void setOnSeekBarChangeExtraListener(SeekBar.OnSeekBarChangeListener param1OnSeekBarChangeListener) {
      this.extraListener = param1OnSeekBarChangeListener;
      super.setOnSeekBarChangeListener(this.multiplexingListener);
    }
    
    public void setOnSeekBarChangeListener(SeekBar.OnSeekBarChangeListener param1OnSeekBarChangeListener) {
      this.listener = param1OnSeekBarChangeListener;
      super.setOnSeekBarChangeListener(this.multiplexingListener);
    }
  }
  
  class null implements SeekBar.OnSeekBarChangeListener {
    public void onProgressChanged(SeekBar param1SeekBar, int param1Int, boolean param1Boolean) {
      if (this.this$0.listener != null)
        this.this$0.listener.onProgressChanged(param1SeekBar, param1Int, param1Boolean); 
      if (this.this$0.extraListener != null)
        this.this$0.extraListener.onProgressChanged(param1SeekBar, param1Int, param1Boolean); 
    }
    
    public void onStartTrackingTouch(SeekBar param1SeekBar) {
      if (this.this$0.listener != null)
        this.this$0.listener.onStartTrackingTouch(param1SeekBar); 
      if (this.this$0.extraListener != null)
        this.this$0.extraListener.onStartTrackingTouch(param1SeekBar); 
    }
    
    public void onStopTrackingTouch(SeekBar param1SeekBar) {
      if (this.this$0.listener != null)
        this.this$0.listener.onStopTrackingTouch(param1SeekBar); 
      if (this.this$0.extraListener != null)
        this.this$0.extraListener.onStopTrackingTouch(param1SeekBar); 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/guanxukeji/videobackgroundmusicpicker/widget/MusicControlSeekbar.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */