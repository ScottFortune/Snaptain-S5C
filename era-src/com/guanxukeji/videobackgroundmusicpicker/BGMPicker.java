package com.guanxukeji.videobackgroundmusicpicker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.media.MediaPlayer;
import com.guanxukeji.videobackgroundmusicpicker.activity.MusicChooseActivity;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

public class BGMPicker {
  private static final MediaPlayer mediaPlayer = new MediaPlayer();
  
  public static BGMPickerDisposable coverVideoBGM(Context paramContext, final String videoIn, final String videoOut, final CoverBGMCallback callback) {
    return new BGMPickerDisposable(Observable.create(new ObservableOnSubscribe<Map<Boolean, String>>() {
            public void subscribe(ObservableEmitter<Map<Boolean, String>> param1ObservableEmitter) throws Exception {
              boolean bool = BGMPicker.isBGMFileReady(application);
              Boolean bool1 = Boolean.valueOf(false);
              if (!bool) {
                HashMap<Object, Object> hashMap1 = new HashMap<Object, Object>();
                hashMap1.put(bool1, application.getString(R.string.no_config_bgm));
                param1ObservableEmitter.onNext(hashMap1);
                param1ObservableEmitter.onComplete();
                return;
              } 
              if ((new File(videoOut)).exists()) {
                HashMap<Object, Object> hashMap1 = new HashMap<Object, Object>();
                hashMap1.put(bool1, application.getString(R.string.output_file_is_exist));
                param1ObservableEmitter.onNext(hashMap1);
                param1ObservableEmitter.onComplete();
                return;
              } 
              String str1 = SharedPreferenceHelper.getBackgroundMusicFilePath(application);
              MediaExtractor mediaExtractor1 = new MediaExtractor();
              MediaExtractor mediaExtractor2 = new MediaExtractor();
              MediaFormat mediaFormat2 = null;
              MediaFormat mediaFormat3 = null;
              MediaFormat mediaFormat1 = mediaFormat3;
              try {
                mediaFormat4 = (MediaFormat)new MediaMuxer();
                mediaFormat1 = mediaFormat3;
                this(videoOut, 0);
                try {
                  mediaExtractor1.setDataSource(str1);
                  int i = mediaExtractor1.getTrackCount();
                  int j = -1;
                  byte b = 0;
                  byte b1 = -1;
                  int k;
                  for (k = 102400; b < i; k = i2) {
                    mediaFormat1 = mediaExtractor1.getTrackFormat(b);
                    int i2 = k;
                    if (mediaFormat1.getString("mime").startsWith("audio/")) {
                      j = mediaFormat4.addTrack(mediaFormat1);
                      try {
                        i2 = mediaFormat1.getInteger("max-input-size");
                        k = i2;
                      } catch (Exception exception) {
                        exception.printStackTrace();
                      } 
                      b1 = b;
                      i2 = k;
                    } 
                    b++;
                  } 
                  mediaExtractor2.setDataSource(videoIn);
                  i = mediaExtractor2.getTrackCount();
                  byte b2 = -1;
                  b = 0;
                  int n = -1;
                  long l = 0L;
                  int m = 102400;
                  int i1 = 0;
                  while (b < i) {
                    mediaFormat3 = mediaExtractor2.getTrackFormat(b);
                    int i2 = m;
                    if (mediaFormat3.getString("mime").startsWith("video/")) {
                      n = mediaFormat4.addTrack(mediaFormat3);
                      try {
                        i1 = mediaFormat3.getInteger("max-input-size");
                        m = i1;
                      } catch (Exception exception) {
                        exception.printStackTrace();
                      } 
                      i1 = mediaFormat3.getInteger("frame-rate");
                      l = mediaFormat3.getLong("durationUs");
                      b2 = b;
                      i2 = m;
                    } 
                    b++;
                    m = i2;
                  } 
                  mediaFormat4.start();
                  mediaExtractor1.selectTrack(b1);
                  MediaCodec.BufferInfo bufferInfo2 = new MediaCodec.BufferInfo();
                  this();
                  ByteBuffer byteBuffer1 = ByteBuffer.allocate(k);
                  while (!param1ObservableEmitter.isDisposed()) {
                    k = mediaExtractor1.readSampleData(byteBuffer1, 0);
                    if (k < 0) {
                      mediaExtractor1.unselectTrack(b1);
                      break;
                    } 
                    long l1 = mediaExtractor1.getSampleTime();
                    if (l1 > l)
                      break; 
                    bufferInfo2.size = k;
                    bufferInfo2.offset = 0;
                    bufferInfo2.flags = mediaExtractor1.getSampleFlags();
                    bufferInfo2.presentationTimeUs = l1;
                    mediaFormat4.writeSampleData(j, byteBuffer1, bufferInfo2);
                    mediaExtractor1.advance();
                  } 
                  if (param1ObservableEmitter.isDisposed()) {
                    File file1 = new File();
                    this(videoOut);
                    file1.delete();
                    mediaExtractor1.release();
                    mediaExtractor2.release();
                    return;
                  } 
                  mediaExtractor2.selectTrack(b2);
                  MediaCodec.BufferInfo bufferInfo1 = new MediaCodec.BufferInfo();
                  this();
                  ByteBuffer byteBuffer2 = ByteBuffer.allocate(m);
                  while (!param1ObservableEmitter.isDisposed()) {
                    k = mediaExtractor2.readSampleData(byteBuffer2, 0);
                    if (k < 0) {
                      mediaExtractor2.unselectTrack(b2);
                      break;
                    } 
                    bufferInfo1.size = k;
                    bufferInfo1.offset = 0;
                    bufferInfo1.flags = mediaExtractor2.getSampleFlags();
                    bufferInfo1.presentationTimeUs += (1000000 / i1);
                    mediaFormat4.writeSampleData(n, byteBuffer2, bufferInfo1);
                    mediaExtractor2.advance();
                  } 
                  if (param1ObservableEmitter.isDisposed()) {
                    File file1 = new File();
                    this(videoOut);
                    file1.delete();
                  } 
                  mediaExtractor1.release();
                  mediaExtractor2.release();
                  mediaFormat4.release();
                  HashMap<Object, Object> hashMap1 = new HashMap<Object, Object>();
                  hashMap1.put(Boolean.valueOf(true), "success");
                  param1ObservableEmitter.onNext(hashMap1);
                  return;
                } catch (IOException null) {
                
                } finally {
                  MediaMuxer mediaMuxer;
                  param1ObservableEmitter = null;
                } 
              } catch (IOException iOException) {
                mediaFormat4 = mediaFormat2;
              } finally {}
              mediaFormat1 = mediaFormat4;
              iOException.printStackTrace();
              mediaFormat1 = mediaFormat4;
              File file = new File();
              mediaFormat1 = mediaFormat4;
              this(videoOut);
              MediaFormat mediaFormat4;
              mediaFormat1 = mediaFormat4;
              file.delete();
              mediaFormat1 = mediaFormat4;
              String[] arrayOfString = iOException.getMessage().split("\n");
              mediaFormat1 = mediaFormat4;
              HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
              mediaFormat1 = mediaFormat4;
              this();
              String str2 = "";
              String str3 = str2;
              if (arrayOfString != null) {
                mediaFormat1 = mediaFormat4;
                str3 = str2;
                if (arrayOfString.length > 0) {
                  mediaFormat1 = mediaFormat4;
                  str3 = arrayOfString[arrayOfString.length - 1];
                } 
              } 
              mediaFormat1 = mediaFormat4;
              StringBuilder stringBuilder = new StringBuilder();
              mediaFormat1 = mediaFormat4;
              this();
              mediaFormat1 = mediaFormat4;
              stringBuilder.append("fail:");
              mediaFormat1 = mediaFormat4;
              stringBuilder.append(str3);
              mediaFormat1 = mediaFormat4;
              hashMap.put(bool1, stringBuilder.toString());
              mediaFormat1 = mediaFormat4;
              param1ObservableEmitter.onNext(hashMap);
              mediaFormat1 = mediaFormat4;
              param1ObservableEmitter.onComplete();
              mediaExtractor1.release();
              mediaExtractor2.release();
              if (mediaFormat4 != null)
                mediaFormat4.release(); 
            }
          }).subscribeOn(Schedulers.io()).unsubscribeOn(AndroidSchedulers.mainThread()).doOnDispose(new Action() {
            public void run() throws Exception {
              callback.onCancel();
            }
          }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Map<Boolean, String>>() {
            public void accept(Map<Boolean, String> param1Map) throws Exception {
              boolean bool = param1Map.containsKey(Boolean.valueOf(true));
              callback.onResult(bool, param1Map.get(Boolean.valueOf(bool)));
            }
          }));
  }
  
  public static String getBGMName(Context paramContext) {
    return SharedPreferenceHelper.getBackgroundMusicName(paramContext);
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
  
  public static boolean isBGMFileReady(Context paramContext) {
    File file = new File(SharedPreferenceHelper.getBackgroundMusicFilePath(paramContext));
    return !file.exists() ? false : SharedPreferenceHelper.getBackgroundMusicFileMD5(paramContext).equals(getFileMD5(file));
  }
  
  public static boolean isOpenBGMMode(Context paramContext) {
    return SharedPreferenceHelper.isOpenBackgroundMusic(paramContext);
  }
  
  public static void launchMusicChooseActivity(Activity paramActivity) {
    paramActivity.startActivity(new Intent((Context)paramActivity, MusicChooseActivity.class));
  }
  
  public static boolean playBGM(Context paramContext) {
    // Byte code:
    //   0: ldc com/guanxukeji/videobackgroundmusicpicker/BGMPicker
    //   2: monitorenter
    //   3: aload_0
    //   4: invokestatic isBGMFileReady : (Landroid/content/Context;)Z
    //   7: istore_1
    //   8: iload_1
    //   9: ifne -> 17
    //   12: ldc com/guanxukeji/videobackgroundmusicpicker/BGMPicker
    //   14: monitorexit
    //   15: iconst_0
    //   16: ireturn
    //   17: getstatic com/guanxukeji/videobackgroundmusicpicker/BGMPicker.mediaPlayer : Landroid/media/MediaPlayer;
    //   20: invokevirtual reset : ()V
    //   23: getstatic com/guanxukeji/videobackgroundmusicpicker/BGMPicker.mediaPlayer : Landroid/media/MediaPlayer;
    //   26: aload_0
    //   27: invokestatic getBackgroundMusicFilePath : (Landroid/content/Context;)Ljava/lang/String;
    //   30: invokevirtual setDataSource : (Ljava/lang/String;)V
    //   33: getstatic com/guanxukeji/videobackgroundmusicpicker/BGMPicker.mediaPlayer : Landroid/media/MediaPlayer;
    //   36: astore_0
    //   37: new com/guanxukeji/videobackgroundmusicpicker/BGMPicker$4
    //   40: astore_2
    //   41: aload_2
    //   42: invokespecial <init> : ()V
    //   45: aload_0
    //   46: aload_2
    //   47: invokevirtual setOnPreparedListener : (Landroid/media/MediaPlayer$OnPreparedListener;)V
    //   50: getstatic com/guanxukeji/videobackgroundmusicpicker/BGMPicker.mediaPlayer : Landroid/media/MediaPlayer;
    //   53: invokevirtual prepareAsync : ()V
    //   56: ldc com/guanxukeji/videobackgroundmusicpicker/BGMPicker
    //   58: monitorexit
    //   59: iconst_1
    //   60: ireturn
    //   61: astore_0
    //   62: aload_0
    //   63: invokevirtual printStackTrace : ()V
    //   66: ldc com/guanxukeji/videobackgroundmusicpicker/BGMPicker
    //   68: monitorexit
    //   69: iconst_0
    //   70: ireturn
    //   71: astore_0
    //   72: ldc com/guanxukeji/videobackgroundmusicpicker/BGMPicker
    //   74: monitorexit
    //   75: aload_0
    //   76: athrow
    // Exception table:
    //   from	to	target	type
    //   3	8	71	finally
    //   17	23	71	finally
    //   23	56	61	java/io/IOException
    //   23	56	71	finally
    //   62	66	71	finally
  }
  
  public static void stopBGMPlaying(Context paramContext) {
    try {
      if (mediaPlayer.isPlaying())
        mediaPlayer.stop(); 
    } catch (IllegalStateException illegalStateException) {
      illegalStateException.printStackTrace();
    } 
  }
  
  public static class BGMPickerDisposable {
    final Disposable disposable;
    
    BGMPickerDisposable(Disposable param1Disposable) {
      this.disposable = param1Disposable;
    }
    
    public void dispose() {
      this.disposable.dispose();
    }
    
    public boolean isDisposed() {
      return this.disposable.isDisposed();
    }
  }
  
  public static interface CoverBGMCallback {
    void onCancel();
    
    void onResult(boolean param1Boolean, String param1String);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/guanxukeji/videobackgroundmusicpicker/BGMPicker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */