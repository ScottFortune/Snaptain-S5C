package com.netopsun.ijkvideoview.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import tv.danmaku.ijk.media.player_gx.IMediaPlayer;

public class MediaPlayerService extends Service {
  private static IMediaPlayer sMediaPlayer;
  
  public static IMediaPlayer getMediaPlayer() {
    return sMediaPlayer;
  }
  
  public static void intentToStart(Context paramContext) {
    paramContext.startService(newIntent(paramContext));
  }
  
  public static void intentToStop(Context paramContext) {
    paramContext.stopService(newIntent(paramContext));
  }
  
  public static Intent newIntent(Context paramContext) {
    return new Intent(paramContext, MediaPlayerService.class);
  }
  
  public static void setMediaPlayer(IMediaPlayer paramIMediaPlayer) {
    IMediaPlayer iMediaPlayer = sMediaPlayer;
    if (iMediaPlayer != null && iMediaPlayer != paramIMediaPlayer) {
      if (iMediaPlayer.isPlaying())
        sMediaPlayer.stop(); 
      sMediaPlayer.release();
      sMediaPlayer = null;
    } 
    sMediaPlayer = paramIMediaPlayer;
  }
  
  public IBinder onBind(Intent paramIntent) {
    return null;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/ijkvideoview/services/MediaPlayerService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */