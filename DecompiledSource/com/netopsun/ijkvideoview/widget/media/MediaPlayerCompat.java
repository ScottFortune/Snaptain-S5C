package com.netopsun.ijkvideoview.widget.media;

import tv.danmaku.ijk.media.player_gx.IMediaPlayer;
import tv.danmaku.ijk.media.player_gx.IjkMediaPlayer;
import tv.danmaku.ijk.media.player_gx.MediaPlayerProxy;
import tv.danmaku.ijk.media.player_gx.TextureMediaPlayer;

public class MediaPlayerCompat {
  public static void deselectTrack(IMediaPlayer paramIMediaPlayer, int paramInt) {
    IjkMediaPlayer ijkMediaPlayer = getIjkMediaPlayer(paramIMediaPlayer);
    if (ijkMediaPlayer == null)
      return; 
    ijkMediaPlayer.deselectTrack(paramInt);
  }
  
  public static IjkMediaPlayer getIjkMediaPlayer(IMediaPlayer paramIMediaPlayer) {
    IjkMediaPlayer ijkMediaPlayer2;
    IjkMediaPlayer ijkMediaPlayer1 = null;
    if (paramIMediaPlayer == null)
      return null; 
    if (paramIMediaPlayer instanceof IjkMediaPlayer) {
      ijkMediaPlayer2 = (IjkMediaPlayer)paramIMediaPlayer;
    } else {
      ijkMediaPlayer2 = ijkMediaPlayer1;
      if (paramIMediaPlayer instanceof MediaPlayerProxy) {
        MediaPlayerProxy mediaPlayerProxy = (MediaPlayerProxy)paramIMediaPlayer;
        ijkMediaPlayer2 = ijkMediaPlayer1;
        if (mediaPlayerProxy.getInternalMediaPlayer() instanceof IjkMediaPlayer)
          ijkMediaPlayer2 = (IjkMediaPlayer)mediaPlayerProxy.getInternalMediaPlayer(); 
      } 
    } 
    return ijkMediaPlayer2;
  }
  
  public static String getName(IMediaPlayer paramIMediaPlayer) {
    if (paramIMediaPlayer == null)
      return "null"; 
    if (paramIMediaPlayer instanceof TextureMediaPlayer) {
      StringBuilder stringBuilder = new StringBuilder("TextureMediaPlayer <");
      paramIMediaPlayer = ((TextureMediaPlayer)paramIMediaPlayer).getInternalMediaPlayer();
      if (paramIMediaPlayer == null) {
        stringBuilder.append("null>");
      } else {
        stringBuilder.append(paramIMediaPlayer.getClass().getSimpleName());
        stringBuilder.append(">");
      } 
      return stringBuilder.toString();
    } 
    return paramIMediaPlayer.getClass().getSimpleName();
  }
  
  public static int getSelectedTrack(IMediaPlayer paramIMediaPlayer, int paramInt) {
    IjkMediaPlayer ijkMediaPlayer = getIjkMediaPlayer(paramIMediaPlayer);
    return (ijkMediaPlayer == null) ? -1 : ijkMediaPlayer.getSelectedTrack(paramInt);
  }
  
  public static void selectTrack(IMediaPlayer paramIMediaPlayer, int paramInt) {
    IjkMediaPlayer ijkMediaPlayer = getIjkMediaPlayer(paramIMediaPlayer);
    if (ijkMediaPlayer == null)
      return; 
    ijkMediaPlayer.selectTrack(paramInt);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/ijkvideoview/widget/media/MediaPlayerCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */