package com.netopsun.ijkvideoview.widget.media;

import android.view.View;
import android.widget.MediaController;

public interface IMediaController {
  void hide();
  
  boolean isShowing();
  
  void setAnchorView(View paramView);
  
  void setEnabled(boolean paramBoolean);
  
  void setMediaPlayer(MediaController.MediaPlayerControl paramMediaPlayerControl);
  
  void show();
  
  void show(int paramInt);
  
  void showOnce(View paramView);
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/ijkvideoview/widget/media/IMediaController.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */