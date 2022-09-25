package com.netopsun.ijkvideoview.widget.media;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.SparseArray;
import android.view.View;
import android.widget.TableLayout;
import com.netopsun.ijkvideoview.R;
import java.util.Locale;
import tv.danmaku.ijk.media.player_gx.IMediaPlayer;
import tv.danmaku.ijk.media.player_gx.IjkMediaPlayer;
import tv.danmaku.ijk.media.player_gx.MediaPlayerProxy;

public class InfoHudViewHolder {
  private static final int MSG_UPDATE_HUD = 1;
  
  private Handler mHandler = new Handler() {
      public void handleMessage(Message param1Message) {
        if (param1Message.what == 1) {
          InfoHudViewHolder infoHudViewHolder1 = InfoHudViewHolder.this;
          InfoHudViewHolder infoHudViewHolder2 = null;
          if (infoHudViewHolder1.mMediaPlayer != null) {
            IjkMediaPlayer ijkMediaPlayer;
            if (InfoHudViewHolder.this.mMediaPlayer instanceof IjkMediaPlayer) {
              ijkMediaPlayer = (IjkMediaPlayer)InfoHudViewHolder.this.mMediaPlayer;
            } else {
              infoHudViewHolder1 = infoHudViewHolder2;
              if (InfoHudViewHolder.this.mMediaPlayer instanceof MediaPlayerProxy) {
                IMediaPlayer iMediaPlayer = ((MediaPlayerProxy)InfoHudViewHolder.this.mMediaPlayer).getInternalMediaPlayer();
                infoHudViewHolder1 = infoHudViewHolder2;
                if (iMediaPlayer != null) {
                  infoHudViewHolder1 = infoHudViewHolder2;
                  if (iMediaPlayer instanceof IjkMediaPlayer)
                    ijkMediaPlayer = (IjkMediaPlayer)iMediaPlayer; 
                } 
              } 
            } 
            if (ijkMediaPlayer != null) {
              int i = ijkMediaPlayer.getVideoDecoder();
              if (i != 1) {
                if (i != 2) {
                  InfoHudViewHolder.this.setRowValue(R.string.vdec, "");
                } else {
                  InfoHudViewHolder.this.setRowValue(R.string.vdec, "MediaCodec");
                } 
              } else {
                InfoHudViewHolder.this.setRowValue(R.string.vdec, "avcodec");
              } 
              float f1 = ijkMediaPlayer.getVideoOutputFramesPerSecond();
              float f2 = ijkMediaPlayer.getVideoDecodeFramesPerSecond();
              InfoHudViewHolder.this.setRowValue(R.string.fps, String.format(Locale.US, "%.2f / %.2f", new Object[] { Float.valueOf(f2), Float.valueOf(f1) }));
              long l1 = ijkMediaPlayer.getVideoCachedDuration();
              long l2 = ijkMediaPlayer.getAudioCachedDuration();
              long l3 = ijkMediaPlayer.getVideoCachedBytes();
              long l4 = ijkMediaPlayer.getAudioCachedBytes();
              long l5 = ijkMediaPlayer.getTcpSpeed();
              long l6 = ijkMediaPlayer.getBitRate();
              long l7 = ijkMediaPlayer.getSeekLoadDuration();
              InfoHudViewHolder.this.setRowValue(R.string.v_cache, String.format(Locale.US, "%s, %s", new Object[] { InfoHudViewHolder.access$200(l1), InfoHudViewHolder.access$300(l3) }));
              InfoHudViewHolder.this.setRowValue(R.string.a_cache, String.format(Locale.US, "%s, %s", new Object[] { InfoHudViewHolder.access$200(l2), InfoHudViewHolder.access$300(l4) }));
              InfoHudViewHolder.this.setRowValue(R.string.load_cost, String.format(Locale.US, "%d ms", new Object[] { Long.valueOf(InfoHudViewHolder.access$400(this.this$0)) }));
              InfoHudViewHolder.this.setRowValue(R.string.seek_cost, String.format(Locale.US, "%d ms", new Object[] { Long.valueOf(InfoHudViewHolder.access$500(this.this$0)) }));
              InfoHudViewHolder.this.setRowValue(R.string.seek_load_cost, String.format(Locale.US, "%d ms", new Object[] { Long.valueOf(l7) }));
              InfoHudViewHolder.this.setRowValue(R.string.tcp_speed, String.format(Locale.US, "%s", new Object[] { InfoHudViewHolder.access$600(l5, 1000L) }));
              InfoHudViewHolder.this.setRowValue(R.string.bit_rate, String.format(Locale.US, "%.2f kbs", new Object[] { Float.valueOf((float)l6 / 1000.0F) }));
              InfoHudViewHolder.this.mHandler.removeMessages(1);
              InfoHudViewHolder.this.mHandler.sendEmptyMessageDelayed(1, 500L);
            } 
          } 
        } 
      }
    };
  
  private long mLoadCost = 0L;
  
  private IMediaPlayer mMediaPlayer;
  
  private SparseArray<View> mRowMap = new SparseArray();
  
  private long mSeekCost = 0L;
  
  private TableLayoutBinder mTableLayoutBinder;
  
  public InfoHudViewHolder(Context paramContext, TableLayout paramTableLayout) {
    this.mTableLayoutBinder = new TableLayoutBinder(paramContext, paramTableLayout);
  }
  
  private void appendRow(int paramInt) {
    View view = this.mTableLayoutBinder.appendRow2(paramInt, (String)null);
    this.mRowMap.put(paramInt, view);
  }
  
  private void appendSection(int paramInt) {
    this.mTableLayoutBinder.appendSection(paramInt);
  }
  
  private static String formatedDurationMilli(long paramLong) {
    return (paramLong >= 1000L) ? String.format(Locale.US, "%.2f sec", new Object[] { Float.valueOf((float)paramLong / 1000.0F) }) : String.format(Locale.US, "%d msec", new Object[] { Long.valueOf(paramLong) });
  }
  
  private static String formatedSize(long paramLong) {
    return (paramLong >= 100000L) ? String.format(Locale.US, "%.2f MB", new Object[] { Float.valueOf((float)paramLong / 1000.0F / 1000.0F) }) : ((paramLong >= 100L) ? String.format(Locale.US, "%.1f KB", new Object[] { Float.valueOf((float)paramLong / 1000.0F) }) : String.format(Locale.US, "%d B", new Object[] { Long.valueOf(paramLong) }));
  }
  
  private static String formatedSpeed(long paramLong1, long paramLong2) {
    if (paramLong2 <= 0L)
      return "0 B/s"; 
    if (paramLong1 <= 0L)
      return "0 B/s"; 
    float f = (float)paramLong1 * 1000.0F / (float)paramLong2;
    return (f >= 1000000.0F) ? String.format(Locale.US, "%.2f MB/s", new Object[] { Float.valueOf(f / 1000.0F / 1000.0F) }) : ((f >= 1000.0F) ? String.format(Locale.US, "%.1f KB/s", new Object[] { Float.valueOf(f / 1000.0F) }) : String.format(Locale.US, "%d B/s", new Object[] { Long.valueOf((long)f) }));
  }
  
  private void setRowValue(int paramInt, String paramString) {
    View view1;
    View view2 = (View)this.mRowMap.get(paramInt);
    if (view2 == null) {
      view1 = this.mTableLayoutBinder.appendRow2(paramInt, paramString);
      this.mRowMap.put(paramInt, view1);
    } else {
      this.mTableLayoutBinder.setValueText(view2, (String)view1);
    } 
  }
  
  public void setMediaPlayer(IMediaPlayer paramIMediaPlayer) {
    this.mMediaPlayer = paramIMediaPlayer;
    if (this.mMediaPlayer != null) {
      this.mHandler.sendEmptyMessageDelayed(1, 500L);
    } else {
      this.mHandler.removeMessages(1);
    } 
  }
  
  public void updateLoadCost(long paramLong) {
    this.mLoadCost = paramLong;
  }
  
  public void updateSeekCost(long paramLong) {
    this.mSeekCost = paramLong;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/ijkvideoview/widget/media/InfoHudViewHolder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */