package com.fh.lib;

public class Define {
  public class BCSS {
    public int brightness;
    
    public int contrast;
    
    public int saturation;
    
    public int sharpness;
  }
  
  public static interface CbDataInterface {
    void cb_data(int param1Int1, byte[] param1ArrayOfbyte, int param1Int2);
  }
  
  public class Circle {
    public int r;
    
    public int x;
    
    public int y;
  }
  
  public class DevSearch {
    public String devIP;
    
    public String devName;
    
    public int isAlive;
    
    public String port;
  }
  
  public class DeviceTime {
    public int day;
    
    public int hour;
    
    public int minute;
    
    public int month;
    
    public int msecond;
    
    public int second;
    
    public int wday;
    
    public int year;
  }
  
  public class FrameHead {
    public int frameType;
    
    public int height;
    
    public long timeStamp;
    
    public int videoFormat;
    
    public int width;
  }
  
  public class IpConfig {
    public int isAutoIP;
    
    public String sGateway;
    
    public String sIP;
    
    public String sMark;
    
    public String sPort;
  }
  
  public class PBRecTime {
    public long pbStartTime;
    
    public long pbStopTime;
  }
  
  public class PicSearch {
    public int chanSeldID;
    
    public int lockFSeldID;
    
    public int startDay;
    
    public int startMonth;
    
    public int startYear;
    
    public int stopDay;
    
    public int stopMonth;
    
    public int stopYear;
    
    public int typeSeldID;
  }
  
  public class Picture {
    public int chanID;
    
    public long dataSize;
    
    public long frameCount;
    
    public int lockFlag;
    
    public int picType;
    
    public long startTime;
    
    public long stopTime;
  }
  
  public class Preview {
    public int blocked;
    
    public int chan;
    
    public int encId;
    
    public int transMode;
  }
  
  public class RecSearch {
    public int chanSeldID;
    
    public int lockFSeldID;
    
    public int startDay;
    
    public int startMonth;
    
    public int startYear;
    
    public int stopDay;
    
    public int stopMonth;
    
    public int stopYear;
    
    public int typeSeldID;
  }
  
  public class Record {
    public int chanID;
    
    public long dataSize;
    
    public int lockFlag;
    
    public int recType;
    
    public long startTime;
    
    public long stopTime;
  }
  
  public enum Res_e {
    FHEN_ER_1360x768,
    FHEN_ER_1440x904,
    FHEN_ER_1600x1200,
    FHEN_ER_1600x904,
    FHEN_ER_1792x1344,
    FHEN_ER_1920x1200,
    FHEN_ER_2048x1152,
    FHEN_ER_2304x1728,
    FHEN_ER_2560x1440,
    FHEN_ER_2560x1600,
    FHEN_ER_2560x1920,
    FHEN_ER_2560x1944,
    FHEN_ER_2592x1944,
    FHEN_ER_2816x2112,
    FHEN_ER_3072x1728,
    FHEN_ER_3072x2304,
    FHEN_ER_3264x2448,
    FHEN_ER_3456x2592,
    FHEN_ER_3600x2704,
    FHEN_ER_3840x2160,
    FHEN_ER_3840x2800,
    FHEN_ER_4000x3000,
    FHEN_ER_4096x2160,
    FHEN_ER_4096x2304,
    FHEN_ER_4096x3072,
    FHEN_ER_4608x2592,
    FHEN_ER_4800x3200,
    FHEN_ER_5120x2880,
    FHEN_ER_5120x3840,
    FHEN_ER_6400x4800,
    FHNPEN_ER_1024x1024,
    FHNPEN_ER_1072x1072,
    FHNPEN_ER_1080P,
    FHNPEN_ER_1520x1520,
    FHNPEN_ER_1536x1536,
    FHNPEN_ER_2048x1520,
    FHNPEN_ER_2048x1536,
    FHNPEN_ER_2048x2048,
    FHNPEN_ER_384x384,
    FHNPEN_ER_4CIF,
    FHNPEN_ER_4CIF_N,
    FHNPEN_ER_512x512,
    FHNPEN_ER_640x360,
    FHNPEN_ER_640x480,
    FHNPEN_ER_720P,
    FHNPEN_ER_768x768,
    FHNPEN_ER_960H,
    FHNPEN_ER_960P,
    FHNPEN_ER_960x960,
    FHNPEN_ER_CIF,
    FHNPEN_ER_CIF_N,
    FHNPEN_ER_D1,
    FHNPEN_ER_D1_N,
    FHNPEN_ER_HALFD1,
    FHNPEN_ER_QCIF("QCIF", 0),
    FHNPEN_ER_QVGA("QCIF", 0);
    
    private int index;
    
    private String name;
    
    static {
      FHNPEN_ER_4CIF = new Res_e("FHNPEN_ER_4CIF", 3, "4CIF", 3);
      FHNPEN_ER_D1 = new Res_e("FHNPEN_ER_D1", 4, "D1", 4);
      FHNPEN_ER_960H = new Res_e("FHNPEN_ER_960H", 5, "960H", 5);
      FHNPEN_ER_720P = new Res_e("FHNPEN_ER_720P", 6, "720P", 6);
      FHNPEN_ER_1080P = new Res_e("FHNPEN_ER_1080P", 7, "1080P", 7);
      FHNPEN_ER_960P = new Res_e("FHNPEN_ER_960P", 8, "960P", 8);
      FHNPEN_ER_640x480 = new Res_e("FHNPEN_ER_640x480", 9, "VGA(640x480)", 9);
      FHNPEN_ER_QVGA = new Res_e("FHNPEN_ER_QVGA", 10, "QVGA", 10);
      FHNPEN_ER_640x360 = new Res_e("FHNPEN_ER_640x360", 11, "VGA(640x360)", 11);
      FHNPEN_ER_960x960 = new Res_e("FHNPEN_ER_960x960", 12, "960x960", 12);
      FHNPEN_ER_2048x1536 = new Res_e("FHNPEN_ER_2048x1536", 13, "2048x1536", 13);
      FHNPEN_ER_2048x1520 = new Res_e("FHNPEN_ER_2048x1520", 14, "2048x1520", 14);
      FHNPEN_ER_2048x2048 = new Res_e("FHNPEN_ER_2048x2048", 15, "2048x2048", 15);
      FHNPEN_ER_1536x1536 = new Res_e("FHNPEN_ER_1536x1536", 16, "1536x1536", 16);
      FHNPEN_ER_1520x1520 = new Res_e("FHNPEN_ER_1520x1520", 17, "1520x1520", 17);
      FHNPEN_ER_1024x1024 = new Res_e("FHNPEN_ER_1024x1024", 18, "1024x1024", 18);
      FHNPEN_ER_512x512 = new Res_e("FHNPEN_ER_512x512", 19, "512x512", 19);
      FHNPEN_ER_CIF_N = new Res_e("FHNPEN_ER_CIF_N", 20, "CIF_N(352x240)", 20);
      FHNPEN_ER_4CIF_N = new Res_e("FHNPEN_ER_4CIF_N", 21, "4CIF_N(704x480)", 21);
      FHNPEN_ER_D1_N = new Res_e("FHNPEN_ER_D1_N", 22, "D1_N(720x480)", 22);
      FHNPEN_ER_768x768 = new Res_e("FHNPEN_ER_768x768", 23, "768x768", 23);
      FHNPEN_ER_384x384 = new Res_e("FHNPEN_ER_384x384", 24, "384x384", 24);
      FHEN_ER_1600x904 = new Res_e("FHEN_ER_1600x904", 25, "1600x904", 25);
      FHEN_ER_1600x1200 = new Res_e("FHEN_ER_1600x1200", 26, "1600x1200", 26);
      FHEN_ER_2560x1440 = new Res_e("FHEN_ER_2560x1440", 27, "2560x1440", 27);
      FHEN_ER_2560x1944 = new Res_e("FHEN_ER_2560x1944", 28, "2560x1944", 28);
      FHEN_ER_2560x1920 = new Res_e("FHEN_ER_2560x1920", 29, "2560x1920", 29);
      FHEN_ER_2560x1600 = new Res_e("FHEN_ER_2560x1600", 30, "2560x1600", 30);
      FHEN_ER_4096x2160 = new Res_e("FHEN_ER_4096x2160", 31, "4096x2160", 31);
      FHEN_ER_1920x1200 = new Res_e("FHEN_ER_1920x1200", 32, "1920x1200", 32);
      FHEN_ER_1440x904 = new Res_e("FHEN_ER_1440x904", 33, "1440x904", 33);
      FHEN_ER_1360x768 = new Res_e("FHEN_ER_1360x768", 34, "1360x768", 34);
      FHEN_ER_1792x1344 = new Res_e("FHEN_ER_1792x1344", 35, "1792x1344", 35);
      FHEN_ER_2048x1152 = new Res_e("FHEN_ER_2048x1152", 36, "2048x1152", 36);
      FHEN_ER_2304x1728 = new Res_e("FHEN_ER_2304x1728", 37, "2304x1728", 37);
      FHEN_ER_2592x1944 = new Res_e("FHEN_ER_2592x1944", 38, "2592x1944", 38);
      FHEN_ER_3072x1728 = new Res_e("FHEN_ER_3072x1728", 39, "3072x1728", 39);
      FHEN_ER_2816x2112 = new Res_e("FHEN_ER_2816x2112", 40, "2816x2112", 40);
      FHEN_ER_3072x2304 = new Res_e("FHEN_ER_3072x2304", 41, "3072x2304", 41);
      FHEN_ER_3264x2448 = new Res_e("FHEN_ER_3264x2448", 42, "3264x2448", 42);
      FHEN_ER_3840x2160 = new Res_e("FHEN_ER_3840x2160", 43, "3840x2160", 43);
      FHEN_ER_3456x2592 = new Res_e("FHEN_ER_3456x2592", 44, "3456x2592", 44);
      FHEN_ER_3600x2704 = new Res_e("FHEN_ER_3600x2704", 45, "3600x2704", 45);
      FHEN_ER_4096x2304 = new Res_e("FHEN_ER_4096x2304", 46, "4096x2304", 46);
      FHEN_ER_3840x2800 = new Res_e("FHEN_ER_3840x2800", 47, "3840x2800", 47);
      FHEN_ER_4000x3000 = new Res_e("FHEN_ER_4000x3000", 48, "4000x3000", 48);
      FHEN_ER_4608x2592 = new Res_e("FHEN_ER_4608x2592", 49, "4608x2592", 49);
      FHEN_ER_4096x3072 = new Res_e("FHEN_ER_4096x3072", 50, "4096x3072", 50);
      FHEN_ER_4800x3200 = new Res_e("FHEN_ER_4800x3200", 51, "4800x3200", 51);
      FHEN_ER_5120x2880 = new Res_e("FHEN_ER_5120x2880", 52, "5120x2880", 52);
      FHEN_ER_5120x3840 = new Res_e("FHEN_ER_5120x3840", 53, "5120x3840", 53);
      FHEN_ER_6400x4800 = new Res_e("FHEN_ER_6400x4800", 54, "6400x4800", 54);
      FHNPEN_ER_1072x1072 = new Res_e("FHNPEN_ER_1072x1072", 55, "1072x1072", 128);
      $VALUES = new Res_e[] { 
          FHNPEN_ER_QCIF, FHNPEN_ER_CIF, FHNPEN_ER_HALFD1, FHNPEN_ER_4CIF, FHNPEN_ER_D1, FHNPEN_ER_960H, FHNPEN_ER_720P, FHNPEN_ER_1080P, FHNPEN_ER_960P, FHNPEN_ER_640x480, 
          FHNPEN_ER_QVGA, FHNPEN_ER_640x360, FHNPEN_ER_960x960, FHNPEN_ER_2048x1536, FHNPEN_ER_2048x1520, FHNPEN_ER_2048x2048, FHNPEN_ER_1536x1536, FHNPEN_ER_1520x1520, FHNPEN_ER_1024x1024, FHNPEN_ER_512x512, 
          FHNPEN_ER_CIF_N, FHNPEN_ER_4CIF_N, FHNPEN_ER_D1_N, FHNPEN_ER_768x768, FHNPEN_ER_384x384, FHEN_ER_1600x904, FHEN_ER_1600x1200, FHEN_ER_2560x1440, FHEN_ER_2560x1944, FHEN_ER_2560x1920, 
          FHEN_ER_2560x1600, FHEN_ER_4096x2160, FHEN_ER_1920x1200, FHEN_ER_1440x904, FHEN_ER_1360x768, FHEN_ER_1792x1344, FHEN_ER_2048x1152, FHEN_ER_2304x1728, FHEN_ER_2592x1944, FHEN_ER_3072x1728, 
          FHEN_ER_2816x2112, FHEN_ER_3072x2304, FHEN_ER_3264x2448, FHEN_ER_3840x2160, FHEN_ER_3456x2592, FHEN_ER_3600x2704, FHEN_ER_4096x2304, FHEN_ER_3840x2800, FHEN_ER_4000x3000, FHEN_ER_4608x2592, 
          FHEN_ER_4096x3072, FHEN_ER_4800x3200, FHEN_ER_5120x2880, FHEN_ER_5120x3840, FHEN_ER_6400x4800, FHNPEN_ER_1072x1072 };
    }
    
    Res_e(String param1String1, int param1Int1) {
      this.name = param1String1;
      this.index = param1Int1;
    }
    
    public static String getNameByIndex(int param1Int) {
      for (Res_e res_e : values()) {
        if (param1Int == res_e.index)
          return res_e.name; 
      } 
      return null;
    }
    
    public int getIndex() {
      return this.index;
    }
    
    public String getName() {
      return this.name;
    }
  }
  
  public class SDCardFormat {
    public int formatProgress;
    
    public int formatState;
  }
  
  public class SDCardInfo {
    public byte state;
    
    public long totalSize;
    
    public long usedSize;
  }
  
  public static interface SerialDataCallBackInterface {
    int SerialDataCallBack(int param1Int1, byte[] param1ArrayOfbyte, int param1Int2);
  }
  
  public class SerialPortCfg {
    public int baudRate;
    
    public int dataBit;
    
    public int flowCtrl;
    
    public int parity;
    
    public int stopBit;
  }
  
  public static interface StreamDataCallBackInterface {
    void StreamDataCallBack(int param1Int1, int param1Int2, Define.FrameHead param1FrameHead, byte[] param1ArrayOfbyte, int param1Int3);
  }
  
  public class VideoEncode {
    public int ctrlType;
    
    public int deinter;
    
    public int denoise;
    
    public int iFrameInterval;
    
    public int maxBitRate;
    
    public int maxFRate;
    
    public int quality;
    
    public int res;
  }
  
  public class WifiConfig {
    public String sChan;
    
    public String sPSK;
    
    public String sSSID;
    
    public int status;
    
    public int wifiMode;
    
    public int wifiType;
  }
  
  public static interface YUVDataCallBackInterface {
    void update(int param1Int1, int param1Int2);
    
    void update(byte[] param1ArrayOfbyte);
    
    void update(byte[] param1ArrayOfbyte1, byte[] param1ArrayOfbyte2, byte[] param1ArrayOfbyte3);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/fh/lib/Define.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */