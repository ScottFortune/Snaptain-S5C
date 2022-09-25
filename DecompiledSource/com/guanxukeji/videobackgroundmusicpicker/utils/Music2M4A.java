package com.guanxukeji.videobackgroundmusicpicker.utils;

import android.app.Application;
import android.content.Context;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.util.Log;
import com.guanxukeji.videobackgroundmusicpicker.R;
import com.guanxukeji.videobackgroundmusicpicker.SharedPreferenceHelper;
import com.guanxukeji.videobackgroundmusicpicker.activity.MusicClipActivity;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Cancellable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

public class Music2M4A {
  private static void addADTStoPacket(byte[] paramArrayOfbyte, int paramInt, int[] paramArrayOfint) {
    byte b;
    switch (paramArrayOfint[0]) {
      default:
        b = 4;
        break;
      case 96000:
        b = 0;
        break;
      case 88200:
        b = 1;
        break;
      case 64000:
        b = 2;
        break;
      case 48000:
        b = 3;
        break;
      case 32000:
        b = 5;
        break;
      case 24000:
        b = 6;
        break;
      case 22050:
        b = 7;
        break;
      case 16000:
        b = 8;
        break;
      case 12000:
        b = 9;
        break;
      case 11025:
        b = 10;
        break;
      case 8000:
        b = 11;
        break;
      case 7350:
        b = 12;
        break;
    } 
    int i = paramArrayOfint[1];
    paramArrayOfbyte[0] = (byte)-1;
    paramArrayOfbyte[1] = (byte)-7;
    paramArrayOfbyte[2] = (byte)(byte)(64 + (b << 2) + (i >> 2));
    paramArrayOfbyte[3] = (byte)(byte)(((i & 0x3) << 6) + (paramInt >> 11));
    paramArrayOfbyte[4] = (byte)(byte)((paramInt & 0x7FF) >> 3);
    paramArrayOfbyte[5] = (byte)(byte)(((paramInt & 0x7) << 5) + 31);
    paramArrayOfbyte[6] = (byte)-4;
  }
  
  private static void dstAudioFormatFromPCM(int[] paramArrayOfint, MediaCodec paramMediaCodec, ByteBuffer[] paramArrayOfByteBuffer1, ByteBuffer[] paramArrayOfByteBuffer2, MediaCodec.BufferInfo paramBufferInfo, byte[] paramArrayOfbyte, BufferedOutputStream paramBufferedOutputStream) {
    if (paramArrayOfbyte != null) {
      i = paramMediaCodec.dequeueInputBuffer(1000L);
      if (i >= 0) {
        ByteBuffer byteBuffer = paramArrayOfByteBuffer1[i];
        byteBuffer.clear();
        byteBuffer.limit(paramArrayOfbyte.length);
        byteBuffer.put(paramArrayOfbyte);
        paramMediaCodec.queueInputBuffer(i, 0, paramArrayOfbyte.length, 0L, 0);
      } 
      i = 0;
    } else {
      i = 2000000;
    } 
    long l = i;
    int i = paramMediaCodec.dequeueOutputBuffer(paramBufferInfo, l);
    while (i >= 0) {
      int j = paramBufferInfo.size;
      int k = j + 7;
      ByteBuffer byteBuffer = paramArrayOfByteBuffer2[i];
      byteBuffer.position(paramBufferInfo.offset);
      byteBuffer.limit(paramBufferInfo.offset + j);
      byte[] arrayOfByte = new byte[k];
      addADTStoPacket(arrayOfByte, k, paramArrayOfint);
      byteBuffer.get(arrayOfByte, 7, j);
      byteBuffer.position(paramBufferInfo.offset);
      try {
        j = arrayOfByte.length;
        try {
          paramBufferedOutputStream.write(arrayOfByte, 0, j);
        } catch (IOException null) {}
      } catch (IOException iOException) {}
      iOException.printStackTrace();
    } 
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
  
  private static MediaCodec initAACMediaEncode(int paramInt1, int paramInt2, int paramInt3) {
    try {
      String str = String.valueOf(Music2M4A.class);
      StringBuilder stringBuilder = new StringBuilder();
      this();
      stringBuilder.append(paramInt3);
      stringBuilder.append(" ");
      stringBuilder.append(paramInt2);
      stringBuilder.append(" ");
      stringBuilder.append(paramInt1);
      Log.d(str, stringBuilder.toString());
      MediaFormat mediaFormat = MediaFormat.createAudioFormat("audio/mp4a-latm", paramInt1, paramInt2);
      mediaFormat.setInteger("bitrate", paramInt3);
      mediaFormat.setInteger("aac-profile", 2);
      mediaFormat.setInteger("max-input-size", 102400);
      MediaCodec mediaCodec = MediaCodec.createEncoderByType("audio/mp4a-latm");
      try {
        mediaCodec.configure(mediaFormat, null, null, 1);
      } catch (IOException null) {}
    } catch (IOException iOException) {
      Object object = null;
    } 
    iOException.printStackTrace();
  }
  
  private static MediaCodec initMediaDecode(MediaExtractor paramMediaExtractor, String paramString, int[] paramArrayOfint) {
    try {
      paramMediaExtractor.setDataSource(paramString);
      byte b = 0;
      while (true) {
        if (b < paramMediaExtractor.getTrackCount()) {
          MediaFormat mediaFormat = paramMediaExtractor.getTrackFormat(b);
          String str = mediaFormat.getString("mime");
          if (str.startsWith("audio")) {
            paramArrayOfint[0] = mediaFormat.getInteger("sample-rate");
            paramArrayOfint[1] = mediaFormat.getInteger("channel-count");
            try {
              paramArrayOfint[2] = mediaFormat.getInteger("bitrate");
            } catch (Exception exception) {
              paramArrayOfint[2] = 32000;
              exception.printStackTrace();
            } 
            paramMediaExtractor.selectTrack(b);
            MediaCodec mediaCodec = MediaCodec.createDecoderByType(str);
            mediaCodec.configure(mediaFormat, null, null, 0);
            break;
          } 
          b++;
          continue;
        } 
        paramMediaExtractor = null;
        break;
      } 
      if (paramMediaExtractor == null) {
        Log.e(String.valueOf(Music2M4A.class), "create mediaDecode failed");
        return null;
      } 
      paramMediaExtractor.start();
      return (MediaCodec)paramMediaExtractor;
    } catch (Exception exception) {
      exception.printStackTrace();
      return null;
    } 
  }
  
  public static Disposable outputBGMWithHardware(final Application context, final String audioIn, final String audioOut, final int startSencond, final int duration, final Consumer<Map<Boolean, String>> consumer) {
    return Observable.create(new ObservableOnSubscribe<Map<Boolean, String>>() {
          public void subscribe(ObservableEmitter<Map<Boolean, String>> param1ObservableEmitter) throws Exception {
            String str2;
            String str1 = audioIn;
            if (str1.contains("file:///android_asset/")) {
              str1 = str1.replace("file:///android_asset/", "");
              StringBuilder stringBuilder = new StringBuilder();
              stringBuilder.append(SharedPreferenceHelper.getAssetMusicCopyBasePath((Context)context));
              stringBuilder.append(str1);
              str2 = stringBuilder.toString();
              if (MusicClipActivity.copyAssetsFile(context, param1ObservableEmitter, str1, str2)) {
                str1 = str2;
              } else {
                HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
                hashMap.put(Boolean.valueOf(false), "复制asset文件失败");
                param1ObservableEmitter.onNext(hashMap);
                param1ObservableEmitter.onComplete();
                return;
              } 
            } else {
              str2 = null;
            } 
            String str3 = audioOut;
            File file = new File(str3.substring(0, str3.lastIndexOf(File.separator)));
            if (!file.exists())
              file.mkdirs(); 
            file = new File(audioOut);
            if (file.exists())
              file.delete(); 
            param1ObservableEmitter.setCancellable(new Cancellable() {
                  public void cancel() throws Exception {
                    Disposable disposable = disposable;
                    if (disposable != null)
                      disposable.dispose(); 
                  }
                });
          }
        }).subscribeOn(Schedulers.newThread()).doOnDispose(new Action() {
          public void run() throws Exception {
            HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
            hashMap.put(Boolean.valueOf(false), context.getString(R.string.cancel_task));
            consumer.accept(hashMap);
          }
        }).unsubscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(consumer);
  }
  
  private static Disposable process(final String audioIn, final String audioOut, final int startSencond, final int duration, final Music2M4ACallback callback) {
    final int[] config = new int[3];
    return (Disposable)Observable.create(new ObservableOnSubscribe<byte[]>() {
          public void subscribe(ObservableEmitter<byte[]> param1ObservableEmitter) throws Exception {
            MediaExtractor mediaExtractor = new MediaExtractor();
            MediaCodec mediaCodec = Music2M4A.initMediaDecode(mediaExtractor, audioIn, config);
            if (mediaCodec == null) {
              param1ObservableEmitter.onError(new Exception("mediaDecode is null"));
              mediaExtractor.release();
              return;
            } 
            ByteBuffer[] arrayOfByteBuffer1 = mediaCodec.getInputBuffers();
            ByteBuffer[] arrayOfByteBuffer2 = mediaCodec.getOutputBuffers();
            MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
            boolean bool = false;
            long l = (new File(audioIn)).length();
            mediaExtractor.seekTo((startSencond * 1000 * 1000), 2);
            int i = (startSencond + duration) * 1000 * 1000;
            while (!param1ObservableEmitter.isDisposed() && !bool && mediaExtractor.getSampleTime() < i) {
              bool = Music2M4A.srcAudioFormatToPCM(config, mediaExtractor, mediaCodec, arrayOfByteBuffer1, arrayOfByteBuffer2, bufferInfo, param1ObservableEmitter, 0L, l, callback);
              arrayOfByteBuffer2 = mediaCodec.getOutputBuffers();
            } 
            param1ObservableEmitter.onComplete();
            mediaCodec.stop();
            mediaCodec.release();
            mediaExtractor.release();
          }
        }).subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribeWith((Observer)new DisposableObserver<byte[]>() {
          BufferedOutputStream bos;
          
          MediaCodec.BufferInfo encodeBufferInfo;
          
          ByteBuffer[] encodeInputBuffers;
          
          ByteBuffer[] encodeOutputBuffers;
          
          FileOutputStream fos;
          
          MediaCodec mediaEncode;
          
          private void release() {
            // Byte code:
            //   0: aload_0
            //   1: getfield bos : Ljava/io/BufferedOutputStream;
            //   4: ifnull -> 14
            //   7: aload_0
            //   8: getfield bos : Ljava/io/BufferedOutputStream;
            //   11: invokevirtual flush : ()V
            //   14: aload_0
            //   15: getfield bos : Ljava/io/BufferedOutputStream;
            //   18: astore_1
            //   19: aload_1
            //   20: ifnull -> 98
            //   23: aload_1
            //   24: invokevirtual close : ()V
            //   27: aload_0
            //   28: aconst_null
            //   29: putfield bos : Ljava/io/BufferedOutputStream;
            //   32: goto -> 98
            //   35: astore_1
            //   36: goto -> 47
            //   39: astore_1
            //   40: aload_1
            //   41: invokevirtual printStackTrace : ()V
            //   44: goto -> 27
            //   47: aload_0
            //   48: aconst_null
            //   49: putfield bos : Ljava/io/BufferedOutputStream;
            //   52: aload_1
            //   53: athrow
            //   54: astore_1
            //   55: goto -> 165
            //   58: astore_1
            //   59: aload_1
            //   60: invokevirtual printStackTrace : ()V
            //   63: aload_0
            //   64: getfield bos : Ljava/io/BufferedOutputStream;
            //   67: astore_1
            //   68: aload_1
            //   69: ifnull -> 98
            //   72: aload_1
            //   73: invokevirtual close : ()V
            //   76: goto -> 27
            //   79: astore_1
            //   80: goto -> 91
            //   83: astore_1
            //   84: aload_1
            //   85: invokevirtual printStackTrace : ()V
            //   88: goto -> 27
            //   91: aload_0
            //   92: aconst_null
            //   93: putfield bos : Ljava/io/BufferedOutputStream;
            //   96: aload_1
            //   97: athrow
            //   98: aload_0
            //   99: getfield fos : Ljava/io/FileOutputStream;
            //   102: ifnull -> 112
            //   105: aload_0
            //   106: getfield fos : Ljava/io/FileOutputStream;
            //   109: invokevirtual close : ()V
            //   112: aload_0
            //   113: aconst_null
            //   114: putfield fos : Ljava/io/FileOutputStream;
            //   117: goto -> 132
            //   120: astore_1
            //   121: goto -> 158
            //   124: astore_1
            //   125: aload_1
            //   126: invokevirtual printStackTrace : ()V
            //   129: goto -> 112
            //   132: aload_0
            //   133: getfield mediaEncode : Landroid/media/MediaCodec;
            //   136: astore_1
            //   137: aload_1
            //   138: ifnull -> 157
            //   141: aload_1
            //   142: invokevirtual stop : ()V
            //   145: aload_0
            //   146: getfield mediaEncode : Landroid/media/MediaCodec;
            //   149: invokevirtual release : ()V
            //   152: aload_0
            //   153: aconst_null
            //   154: putfield mediaEncode : Landroid/media/MediaCodec;
            //   157: return
            //   158: aload_0
            //   159: aconst_null
            //   160: putfield fos : Ljava/io/FileOutputStream;
            //   163: aload_1
            //   164: athrow
            //   165: aload_0
            //   166: getfield bos : Ljava/io/BufferedOutputStream;
            //   169: astore_2
            //   170: aload_2
            //   171: ifnull -> 205
            //   174: aload_2
            //   175: invokevirtual close : ()V
            //   178: aload_0
            //   179: aconst_null
            //   180: putfield bos : Ljava/io/BufferedOutputStream;
            //   183: goto -> 205
            //   186: astore_1
            //   187: goto -> 198
            //   190: astore_2
            //   191: aload_2
            //   192: invokevirtual printStackTrace : ()V
            //   195: goto -> 178
            //   198: aload_0
            //   199: aconst_null
            //   200: putfield bos : Ljava/io/BufferedOutputStream;
            //   203: aload_1
            //   204: athrow
            //   205: goto -> 210
            //   208: aload_1
            //   209: athrow
            //   210: goto -> 208
            // Exception table:
            //   from	to	target	type
            //   0	14	58	java/io/IOException
            //   0	14	54	finally
            //   23	27	39	java/io/IOException
            //   23	27	35	finally
            //   40	44	35	finally
            //   59	63	54	finally
            //   72	76	83	java/io/IOException
            //   72	76	79	finally
            //   84	88	79	finally
            //   98	112	124	java/io/IOException
            //   98	112	120	finally
            //   125	129	120	finally
            //   174	178	190	java/io/IOException
            //   174	178	186	finally
            //   191	195	186	finally
          }
          
          public void onComplete() {
            Music2M4A.dstAudioFormatFromPCM(config, this.mediaEncode, this.encodeInputBuffers, this.encodeOutputBuffers, this.encodeBufferInfo, null, this.bos);
            release();
            Music2M4A.Music2M4ACallback music2M4ACallback = callback;
            if (music2M4ACallback != null)
              music2M4ACallback.onSuccess(); 
          }
          
          public void onError(Throwable param1Throwable) {
            param1Throwable.printStackTrace();
            File file = new File(audioOut);
            release();
            if (file.exists())
              file.delete(); 
            Music2M4A.Music2M4ACallback music2M4ACallback = callback;
            if (music2M4ACallback != null)
              music2M4ACallback.onFail(param1Throwable.getMessage()); 
          }
          
          public void onNext(byte[] param1ArrayOfbyte) {
            if (this.mediaEncode == null) {
              int[] arrayOfInt = config;
              this.mediaEncode = Music2M4A.initAACMediaEncode(arrayOfInt[0], arrayOfInt[1], arrayOfInt[2]);
              MediaCodec mediaCodec = this.mediaEncode;
              if (mediaCodec == null) {
                dispose();
                return;
              } 
              this.encodeInputBuffers = mediaCodec.getInputBuffers();
              this.encodeOutputBuffers = this.mediaEncode.getOutputBuffers();
              this.encodeBufferInfo = new MediaCodec.BufferInfo();
              try {
                FileOutputStream fileOutputStream = new FileOutputStream();
                File file = new File();
                this(audioOut);
                this(file);
                this.fos = fileOutputStream;
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream();
                this(this.fos, 204800);
                this.bos = bufferedOutputStream;
              } catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
                dispose();
              } 
            } 
            Music2M4A.dstAudioFormatFromPCM(config, this.mediaEncode, this.encodeInputBuffers, this.encodeOutputBuffers, this.encodeBufferInfo, param1ArrayOfbyte, this.bos);
          }
        });
  }
  
  private static boolean srcAudioFormatToPCM(int[] paramArrayOfint, MediaExtractor paramMediaExtractor, MediaCodec paramMediaCodec, ByteBuffer[] paramArrayOfByteBuffer1, ByteBuffer[] paramArrayOfByteBuffer2, MediaCodec.BufferInfo paramBufferInfo, ObservableEmitter paramObservableEmitter, long paramLong1, long paramLong2, Music2M4ACallback paramMusic2M4ACallback) {
    int i = 0;
    int j = 0;
    boolean bool = false;
    while (i < paramArrayOfByteBuffer1.length - 1) {
      int k = paramMediaCodec.dequeueInputBuffer(1000L);
      if (k < 0)
        break; 
      ByteBuffer byteBuffer = paramArrayOfByteBuffer1[k];
      byteBuffer.clear();
      int m = paramMediaExtractor.readSampleData(byteBuffer, 0);
      if (m < 0) {
        j = 3000000;
        bool = true;
      } else {
        paramMediaCodec.queueInputBuffer(k, 0, m, 0L, 0);
        paramMediaExtractor.advance();
        paramLong1 += m;
        if (paramMusic2M4ACallback != null) {
          double d1 = paramLong1;
          Double.isNaN(d1);
          double d2 = paramLong2;
          Double.isNaN(d2);
          paramMusic2M4ACallback.onprogress((int)(d1 * 1.0D / d2 * 100.0D));
        } 
      } 
      i++;
    } 
    paramLong1 = j;
    for (i = paramMediaCodec.dequeueOutputBuffer(paramBufferInfo, paramLong1); i >= 0 && !paramObservableEmitter.isDisposed(); i = paramMediaCodec.dequeueOutputBuffer(paramBufferInfo, paramLong1)) {
      ByteBuffer byteBuffer = paramArrayOfByteBuffer2[i];
      byte[] arrayOfByte = new byte[paramBufferInfo.size];
      byteBuffer.get(arrayOfByte);
      byteBuffer.clear();
      paramObservableEmitter.onNext(arrayOfByte);
      paramMediaCodec.releaseOutputBuffer(i, false);
    } 
    if (i == -2) {
      MediaFormat mediaFormat = paramMediaCodec.getOutputFormat();
      paramArrayOfint[0] = mediaFormat.getInteger("sample-rate");
      paramArrayOfint[1] = mediaFormat.getInteger("channel-count");
      try {
        paramArrayOfint[2] = mediaFormat.getInteger("bitrate");
      } catch (Exception exception) {
        paramArrayOfint[2] = 32000;
        exception.printStackTrace();
      } 
    } 
    return bool;
  }
  
  public static interface Music2M4ACallback {
    void onFail(String param1String);
    
    void onSuccess();
    
    void onprogress(int param1Int);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/guanxukeji/videobackgroundmusicpicker/utils/Music2M4A.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */