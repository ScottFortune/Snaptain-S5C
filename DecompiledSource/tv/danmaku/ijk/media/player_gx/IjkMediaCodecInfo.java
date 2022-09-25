package tv.danmaku.ijk.media.player_gx;

import android.media.MediaCodecInfo;
import android.os.Build;
import java.util.Locale;
import java.util.Map;

public class IjkMediaCodecInfo {
  public static final int RANK_ACCEPTABLE = 700;
  
  public static final int RANK_LAST_CHANCE = 600;
  
  public static final int RANK_MAX = 1000;
  
  public static final int RANK_NON_STANDARD = 100;
  
  public static final int RANK_NO_SENSE = 0;
  
  public static final int RANK_SECURE = 300;
  
  public static final int RANK_SOFTWARE = 200;
  
  public static final int RANK_TESTED = 800;
  
  private static final String TAG = "IjkMediaCodecInfo";
  
  private static Map<String, Integer> sKnownCodecList;
  
  public MediaCodecInfo mCodecInfo;
  
  public String mMimeType;
  
  public int mRank = 0;
  
  private static Map<String, Integer> getKnownCodecList() {
    // Byte code:
    //   0: ldc tv/danmaku/ijk/media/player_gx/IjkMediaCodecInfo
    //   2: monitorenter
    //   3: getstatic tv/danmaku/ijk/media/player_gx/IjkMediaCodecInfo.sKnownCodecList : Ljava/util/Map;
    //   6: ifnull -> 18
    //   9: getstatic tv/danmaku/ijk/media/player_gx/IjkMediaCodecInfo.sKnownCodecList : Ljava/util/Map;
    //   12: astore_0
    //   13: ldc tv/danmaku/ijk/media/player_gx/IjkMediaCodecInfo
    //   15: monitorexit
    //   16: aload_0
    //   17: areturn
    //   18: new java/util/TreeMap
    //   21: astore_0
    //   22: aload_0
    //   23: getstatic java/lang/String.CASE_INSENSITIVE_ORDER : Ljava/util/Comparator;
    //   26: invokespecial <init> : (Ljava/util/Comparator;)V
    //   29: aload_0
    //   30: putstatic tv/danmaku/ijk/media/player_gx/IjkMediaCodecInfo.sKnownCodecList : Ljava/util/Map;
    //   33: getstatic tv/danmaku/ijk/media/player_gx/IjkMediaCodecInfo.sKnownCodecList : Ljava/util/Map;
    //   36: ldc 'OMX.Nvidia.h264.decode'
    //   38: sipush #800
    //   41: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   44: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   49: pop
    //   50: getstatic tv/danmaku/ijk/media/player_gx/IjkMediaCodecInfo.sKnownCodecList : Ljava/util/Map;
    //   53: ldc 'OMX.Nvidia.h264.decode.secure'
    //   55: sipush #300
    //   58: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   61: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   66: pop
    //   67: getstatic tv/danmaku/ijk/media/player_gx/IjkMediaCodecInfo.sKnownCodecList : Ljava/util/Map;
    //   70: ldc 'OMX.Intel.hw_vd.h264'
    //   72: sipush #801
    //   75: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   78: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   83: pop
    //   84: getstatic tv/danmaku/ijk/media/player_gx/IjkMediaCodecInfo.sKnownCodecList : Ljava/util/Map;
    //   87: ldc 'OMX.Intel.VideoDecoder.AVC'
    //   89: sipush #800
    //   92: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   95: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   100: pop
    //   101: getstatic tv/danmaku/ijk/media/player_gx/IjkMediaCodecInfo.sKnownCodecList : Ljava/util/Map;
    //   104: ldc 'OMX.qcom.video.decoder.avc'
    //   106: sipush #800
    //   109: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   112: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   117: pop
    //   118: getstatic tv/danmaku/ijk/media/player_gx/IjkMediaCodecInfo.sKnownCodecList : Ljava/util/Map;
    //   121: ldc 'OMX.ittiam.video.decoder.avc'
    //   123: iconst_0
    //   124: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   127: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   132: pop
    //   133: getstatic tv/danmaku/ijk/media/player_gx/IjkMediaCodecInfo.sKnownCodecList : Ljava/util/Map;
    //   136: ldc 'OMX.SEC.avc.dec'
    //   138: sipush #800
    //   141: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   144: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   149: pop
    //   150: getstatic tv/danmaku/ijk/media/player_gx/IjkMediaCodecInfo.sKnownCodecList : Ljava/util/Map;
    //   153: ldc 'OMX.SEC.AVC.Decoder'
    //   155: sipush #799
    //   158: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   161: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   166: pop
    //   167: getstatic tv/danmaku/ijk/media/player_gx/IjkMediaCodecInfo.sKnownCodecList : Ljava/util/Map;
    //   170: ldc 'OMX.SEC.avcdec'
    //   172: sipush #798
    //   175: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   178: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   183: pop
    //   184: getstatic tv/danmaku/ijk/media/player_gx/IjkMediaCodecInfo.sKnownCodecList : Ljava/util/Map;
    //   187: ldc 'OMX.SEC.avc.sw.dec'
    //   189: sipush #200
    //   192: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   195: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   200: pop
    //   201: getstatic tv/danmaku/ijk/media/player_gx/IjkMediaCodecInfo.sKnownCodecList : Ljava/util/Map;
    //   204: ldc 'OMX.Exynos.avc.dec'
    //   206: sipush #800
    //   209: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   212: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   217: pop
    //   218: getstatic tv/danmaku/ijk/media/player_gx/IjkMediaCodecInfo.sKnownCodecList : Ljava/util/Map;
    //   221: ldc 'OMX.Exynos.AVC.Decoder'
    //   223: sipush #799
    //   226: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   229: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   234: pop
    //   235: getstatic tv/danmaku/ijk/media/player_gx/IjkMediaCodecInfo.sKnownCodecList : Ljava/util/Map;
    //   238: ldc 'OMX.k3.video.decoder.avc'
    //   240: sipush #800
    //   243: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   246: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   251: pop
    //   252: getstatic tv/danmaku/ijk/media/player_gx/IjkMediaCodecInfo.sKnownCodecList : Ljava/util/Map;
    //   255: ldc 'OMX.IMG.MSVDX.Decoder.AVC'
    //   257: sipush #800
    //   260: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   263: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   268: pop
    //   269: getstatic tv/danmaku/ijk/media/player_gx/IjkMediaCodecInfo.sKnownCodecList : Ljava/util/Map;
    //   272: ldc 'OMX.TI.DUCATI1.VIDEO.DECODER'
    //   274: sipush #800
    //   277: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   280: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   285: pop
    //   286: getstatic tv/danmaku/ijk/media/player_gx/IjkMediaCodecInfo.sKnownCodecList : Ljava/util/Map;
    //   289: ldc 'OMX.rk.video_decoder.avc'
    //   291: sipush #800
    //   294: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   297: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   302: pop
    //   303: getstatic tv/danmaku/ijk/media/player_gx/IjkMediaCodecInfo.sKnownCodecList : Ljava/util/Map;
    //   306: ldc 'OMX.amlogic.avc.decoder.awesome'
    //   308: sipush #800
    //   311: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   314: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   319: pop
    //   320: getstatic tv/danmaku/ijk/media/player_gx/IjkMediaCodecInfo.sKnownCodecList : Ljava/util/Map;
    //   323: ldc 'OMX.MARVELL.VIDEO.HW.CODA7542DECODER'
    //   325: sipush #800
    //   328: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   331: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   336: pop
    //   337: getstatic tv/danmaku/ijk/media/player_gx/IjkMediaCodecInfo.sKnownCodecList : Ljava/util/Map;
    //   340: ldc 'OMX.MARVELL.VIDEO.H264DECODER'
    //   342: sipush #200
    //   345: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   348: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   353: pop
    //   354: getstatic tv/danmaku/ijk/media/player_gx/IjkMediaCodecInfo.sKnownCodecList : Ljava/util/Map;
    //   357: ldc 'OMX.Action.Video.Decoder'
    //   359: invokeinterface remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   364: pop
    //   365: getstatic tv/danmaku/ijk/media/player_gx/IjkMediaCodecInfo.sKnownCodecList : Ljava/util/Map;
    //   368: ldc 'OMX.allwinner.video.decoder.avc'
    //   370: invokeinterface remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   375: pop
    //   376: getstatic tv/danmaku/ijk/media/player_gx/IjkMediaCodecInfo.sKnownCodecList : Ljava/util/Map;
    //   379: ldc 'OMX.BRCM.vc4.decoder.avc'
    //   381: invokeinterface remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   386: pop
    //   387: getstatic tv/danmaku/ijk/media/player_gx/IjkMediaCodecInfo.sKnownCodecList : Ljava/util/Map;
    //   390: ldc 'OMX.brcm.video.h264.hw.decoder'
    //   392: invokeinterface remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   397: pop
    //   398: getstatic tv/danmaku/ijk/media/player_gx/IjkMediaCodecInfo.sKnownCodecList : Ljava/util/Map;
    //   401: ldc 'OMX.brcm.video.h264.decoder'
    //   403: invokeinterface remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   408: pop
    //   409: getstatic tv/danmaku/ijk/media/player_gx/IjkMediaCodecInfo.sKnownCodecList : Ljava/util/Map;
    //   412: ldc 'OMX.cosmo.video.decoder.avc'
    //   414: invokeinterface remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   419: pop
    //   420: getstatic tv/danmaku/ijk/media/player_gx/IjkMediaCodecInfo.sKnownCodecList : Ljava/util/Map;
    //   423: ldc 'OMX.duos.h264.decoder'
    //   425: invokeinterface remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   430: pop
    //   431: getstatic tv/danmaku/ijk/media/player_gx/IjkMediaCodecInfo.sKnownCodecList : Ljava/util/Map;
    //   434: ldc 'OMX.hantro.81x0.video.decoder'
    //   436: invokeinterface remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   441: pop
    //   442: getstatic tv/danmaku/ijk/media/player_gx/IjkMediaCodecInfo.sKnownCodecList : Ljava/util/Map;
    //   445: ldc 'OMX.hantro.G1.video.decoder'
    //   447: invokeinterface remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   452: pop
    //   453: getstatic tv/danmaku/ijk/media/player_gx/IjkMediaCodecInfo.sKnownCodecList : Ljava/util/Map;
    //   456: ldc 'OMX.hisi.video.decoder'
    //   458: invokeinterface remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   463: pop
    //   464: getstatic tv/danmaku/ijk/media/player_gx/IjkMediaCodecInfo.sKnownCodecList : Ljava/util/Map;
    //   467: ldc 'OMX.LG.decoder.video.avc'
    //   469: invokeinterface remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   474: pop
    //   475: getstatic tv/danmaku/ijk/media/player_gx/IjkMediaCodecInfo.sKnownCodecList : Ljava/util/Map;
    //   478: ldc 'OMX.MS.AVC.Decoder'
    //   480: invokeinterface remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   485: pop
    //   486: getstatic tv/danmaku/ijk/media/player_gx/IjkMediaCodecInfo.sKnownCodecList : Ljava/util/Map;
    //   489: ldc 'OMX.RENESAS.VIDEO.DECODER.H264'
    //   491: invokeinterface remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   496: pop
    //   497: getstatic tv/danmaku/ijk/media/player_gx/IjkMediaCodecInfo.sKnownCodecList : Ljava/util/Map;
    //   500: ldc 'OMX.RTK.video.decoder'
    //   502: invokeinterface remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   507: pop
    //   508: getstatic tv/danmaku/ijk/media/player_gx/IjkMediaCodecInfo.sKnownCodecList : Ljava/util/Map;
    //   511: ldc 'OMX.sprd.h264.decoder'
    //   513: invokeinterface remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   518: pop
    //   519: getstatic tv/danmaku/ijk/media/player_gx/IjkMediaCodecInfo.sKnownCodecList : Ljava/util/Map;
    //   522: ldc 'OMX.ST.VFM.H264Dec'
    //   524: invokeinterface remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   529: pop
    //   530: getstatic tv/danmaku/ijk/media/player_gx/IjkMediaCodecInfo.sKnownCodecList : Ljava/util/Map;
    //   533: ldc 'OMX.vpu.video_decoder.avc'
    //   535: invokeinterface remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   540: pop
    //   541: getstatic tv/danmaku/ijk/media/player_gx/IjkMediaCodecInfo.sKnownCodecList : Ljava/util/Map;
    //   544: ldc 'OMX.WMT.decoder.avc'
    //   546: invokeinterface remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   551: pop
    //   552: getstatic tv/danmaku/ijk/media/player_gx/IjkMediaCodecInfo.sKnownCodecList : Ljava/util/Map;
    //   555: ldc 'OMX.bluestacks.hw.decoder'
    //   557: invokeinterface remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   562: pop
    //   563: getstatic tv/danmaku/ijk/media/player_gx/IjkMediaCodecInfo.sKnownCodecList : Ljava/util/Map;
    //   566: ldc 'OMX.google.h264.decoder'
    //   568: sipush #200
    //   571: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   574: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   579: pop
    //   580: getstatic tv/danmaku/ijk/media/player_gx/IjkMediaCodecInfo.sKnownCodecList : Ljava/util/Map;
    //   583: ldc 'OMX.google.h264.lc.decoder'
    //   585: sipush #200
    //   588: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   591: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   596: pop
    //   597: getstatic tv/danmaku/ijk/media/player_gx/IjkMediaCodecInfo.sKnownCodecList : Ljava/util/Map;
    //   600: ldc 'OMX.k3.ffmpeg.decoder'
    //   602: sipush #200
    //   605: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   608: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   613: pop
    //   614: getstatic tv/danmaku/ijk/media/player_gx/IjkMediaCodecInfo.sKnownCodecList : Ljava/util/Map;
    //   617: ldc 'OMX.ffmpeg.video.decoder'
    //   619: sipush #200
    //   622: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   625: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   630: pop
    //   631: getstatic tv/danmaku/ijk/media/player_gx/IjkMediaCodecInfo.sKnownCodecList : Ljava/util/Map;
    //   634: ldc 'OMX.sprd.soft.h264.decoder'
    //   636: sipush #200
    //   639: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   642: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   647: pop
    //   648: getstatic tv/danmaku/ijk/media/player_gx/IjkMediaCodecInfo.sKnownCodecList : Ljava/util/Map;
    //   651: astore_0
    //   652: ldc tv/danmaku/ijk/media/player_gx/IjkMediaCodecInfo
    //   654: monitorexit
    //   655: aload_0
    //   656: areturn
    //   657: astore_0
    //   658: ldc tv/danmaku/ijk/media/player_gx/IjkMediaCodecInfo
    //   660: monitorexit
    //   661: aload_0
    //   662: athrow
    // Exception table:
    //   from	to	target	type
    //   3	13	657	finally
    //   18	652	657	finally
  }
  
  public static String getLevelName(int paramInt) {
    if (paramInt != 1) {
      if (paramInt != 2) {
        switch (paramInt) {
          default:
            return "0";
          case 65536:
            return "52";
          case 32768:
            return "51";
          case 16384:
            return "5";
          case 8192:
            return "42";
          case 4096:
            return "41";
          case 2048:
            return "4";
          case 1024:
            return "32";
          case 512:
            return "31";
          case 256:
            return "3";
          case 128:
            return "22";
          case 64:
            return "21";
          case 32:
            return "2";
          case 16:
            return "13";
          case 8:
            return "12";
          case 4:
            break;
        } 
        return "11";
      } 
      return "1b";
    } 
    return "1";
  }
  
  public static String getProfileLevelName(int paramInt1, int paramInt2) {
    return String.format(Locale.US, " %s Profile Level %s (%d,%d)", new Object[] { getProfileName(paramInt1), getLevelName(paramInt2), Integer.valueOf(paramInt1), Integer.valueOf(paramInt2) });
  }
  
  public static String getProfileName(int paramInt) {
    return (paramInt != 1) ? ((paramInt != 2) ? ((paramInt != 4) ? ((paramInt != 8) ? ((paramInt != 16) ? ((paramInt != 32) ? ((paramInt != 64) ? "Unknown" : "High444") : "High422") : "High10") : "High") : "Extends") : "Main") : "Baseline";
  }
  
  public static IjkMediaCodecInfo setupCandidate(MediaCodecInfo paramMediaCodecInfo, String paramString) {
    // Byte code:
    //   0: aconst_null
    //   1: astore_2
    //   2: aload_2
    //   3: astore_3
    //   4: aload_0
    //   5: ifnull -> 249
    //   8: getstatic android/os/Build$VERSION.SDK_INT : I
    //   11: bipush #16
    //   13: if_icmpge -> 21
    //   16: aload_2
    //   17: astore_3
    //   18: goto -> 249
    //   21: aload_0
    //   22: invokevirtual getName : ()Ljava/lang/String;
    //   25: astore_3
    //   26: aload_3
    //   27: invokestatic isEmpty : (Ljava/lang/CharSequence;)Z
    //   30: ifeq -> 35
    //   33: aconst_null
    //   34: areturn
    //   35: aload_3
    //   36: getstatic java/util/Locale.US : Ljava/util/Locale;
    //   39: invokevirtual toLowerCase : (Ljava/util/Locale;)Ljava/lang/String;
    //   42: astore_3
    //   43: aload_3
    //   44: ldc_w 'omx.'
    //   47: invokevirtual startsWith : (Ljava/lang/String;)Z
    //   50: istore #4
    //   52: sipush #600
    //   55: istore #5
    //   57: iload #4
    //   59: ifne -> 69
    //   62: bipush #100
    //   64: istore #5
    //   66: goto -> 225
    //   69: aload_3
    //   70: ldc_w 'omx.pv'
    //   73: invokevirtual startsWith : (Ljava/lang/String;)Z
    //   76: ifeq -> 87
    //   79: sipush #200
    //   82: istore #5
    //   84: goto -> 225
    //   87: aload_3
    //   88: ldc_w 'omx.google.'
    //   91: invokevirtual startsWith : (Ljava/lang/String;)Z
    //   94: ifeq -> 100
    //   97: goto -> 79
    //   100: aload_3
    //   101: ldc_w 'omx.ffmpeg.'
    //   104: invokevirtual startsWith : (Ljava/lang/String;)Z
    //   107: ifeq -> 113
    //   110: goto -> 79
    //   113: aload_3
    //   114: ldc_w 'omx.k3.ffmpeg.'
    //   117: invokevirtual startsWith : (Ljava/lang/String;)Z
    //   120: ifeq -> 126
    //   123: goto -> 79
    //   126: aload_3
    //   127: ldc_w 'omx.avcodec.'
    //   130: invokevirtual startsWith : (Ljava/lang/String;)Z
    //   133: ifeq -> 139
    //   136: goto -> 79
    //   139: aload_3
    //   140: ldc_w 'omx.ittiam.'
    //   143: invokevirtual startsWith : (Ljava/lang/String;)Z
    //   146: ifeq -> 155
    //   149: iconst_0
    //   150: istore #5
    //   152: goto -> 225
    //   155: aload_3
    //   156: ldc_w 'omx.mtk.'
    //   159: invokevirtual startsWith : (Ljava/lang/String;)Z
    //   162: ifeq -> 184
    //   165: getstatic android/os/Build$VERSION.SDK_INT : I
    //   168: bipush #18
    //   170: if_icmpge -> 176
    //   173: goto -> 149
    //   176: sipush #800
    //   179: istore #5
    //   181: goto -> 225
    //   184: invokestatic getKnownCodecList : ()Ljava/util/Map;
    //   187: aload_3
    //   188: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   193: checkcast java/lang/Integer
    //   196: astore_3
    //   197: aload_3
    //   198: ifnull -> 210
    //   201: aload_3
    //   202: invokevirtual intValue : ()I
    //   205: istore #5
    //   207: goto -> 225
    //   210: aload_0
    //   211: aload_1
    //   212: invokevirtual getCapabilitiesForType : (Ljava/lang/String;)Landroid/media/MediaCodecInfo$CodecCapabilities;
    //   215: astore_3
    //   216: aload_3
    //   217: ifnull -> 225
    //   220: sipush #700
    //   223: istore #5
    //   225: new tv/danmaku/ijk/media/player_gx/IjkMediaCodecInfo
    //   228: dup
    //   229: invokespecial <init> : ()V
    //   232: astore_3
    //   233: aload_3
    //   234: aload_0
    //   235: putfield mCodecInfo : Landroid/media/MediaCodecInfo;
    //   238: aload_3
    //   239: iload #5
    //   241: putfield mRank : I
    //   244: aload_3
    //   245: aload_1
    //   246: putfield mMimeType : Ljava/lang/String;
    //   249: aload_3
    //   250: areturn
    //   251: astore_3
    //   252: goto -> 225
    // Exception table:
    //   from	to	target	type
    //   210	216	251	finally
  }
  
  public void dumpProfileLevels(String paramString) {
    if (Build.VERSION.SDK_INT < 16)
      return; 
    try {
      boolean bool1;
      boolean bool2;
      MediaCodecInfo.CodecCapabilities codecCapabilities = this.mCodecInfo.getCapabilitiesForType(paramString);
      if (codecCapabilities != null && codecCapabilities.profileLevels != null) {
        MediaCodecInfo.CodecProfileLevel[] arrayOfCodecProfileLevel = codecCapabilities.profileLevels;
        int i = arrayOfCodecProfileLevel.length;
        byte b = 0;
        int j = 0;
        int k = 0;
        while (true) {
          bool1 = j;
          bool2 = k;
          if (b < i) {
            MediaCodecInfo.CodecProfileLevel codecProfileLevel = arrayOfCodecProfileLevel[b];
            if (codecProfileLevel != null) {
              j = Math.max(j, codecProfileLevel.profile);
              k = Math.max(k, codecProfileLevel.level);
            } 
            b++;
            continue;
          } 
          break;
        } 
      } else {
        bool1 = false;
        bool2 = false;
      } 
    } finally {
      paramString = null;
    } 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/tv/danmaku/ijk/media/player_gx/IjkMediaCodecInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */