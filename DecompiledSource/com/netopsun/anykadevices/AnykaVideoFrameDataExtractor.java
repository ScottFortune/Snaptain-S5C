package com.netopsun.anykadevices;

import android.util.Log;

public class AnykaVideoFrameDataExtractor {
  private final int CMD_TYPE_LENGTH = 12;
  
  private int devicesType;
  
  private byte lastRemoteCMD = (byte)-1;
  
  private OnFrameDataCallback onFrameDataCallback;
  
  private int remainingDataHeadLength = 32;
  
  private int remainingLength;
  
  private int state;
  
  private final byte[] tempBytes = new byte[900000];
  
  private int tempBytesCurrentPosition = -1;
  
  private static int byte2IntLittle(byte[] paramArrayOfbyte, int paramInt) {
    byte b1 = paramArrayOfbyte[paramInt + 3];
    byte b2 = paramArrayOfbyte[paramInt + 2];
    byte b3 = paramArrayOfbyte[paramInt + 1];
    return paramArrayOfbyte[paramInt] & 0xFF | (b1 & 0xFF) << 24 | (b2 & 0xFF) << 16 | (b3 & 0xFF) << 8;
  }
  
  private void decryptFrame(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    int i = this.remainingDataHeadLength;
    if (paramInt2 < i) {
      Log.e("VideoFrameDataExtractor", "decryptFrame: video Pack err");
      return;
    } 
    i = paramInt2 - i;
    paramInt1 = byte2IntLittle(paramArrayOfbyte, paramInt1 + 8);
    if ((i & 0x1) != 0) {
      paramInt1 = ((paramInt1 ^ i) + i ^ i) % i;
    } else {
      paramInt1 = ((paramInt1 ^ i) + i + 1 ^ i) % i;
    } 
    i = this.remainingDataHeadLength;
    if (i + paramInt1 < paramInt2) {
      paramInt1 = i + paramInt1;
      paramArrayOfbyte[paramInt1] = (byte)(byte)(paramArrayOfbyte[paramInt1] ^ 0xFFFFFFFF);
    } 
  }
  
  private boolean isFrameStart(byte paramByte) {
    int i = this.state;
    if ((i == 0 || i == 1) && paramByte == 108) {
      this.state = 1;
    } else if (this.state == 1 && paramByte == 101) {
      this.state = 2;
    } else if (this.state == 2 && paramByte == 119) {
      this.state = 3;
    } else if (this.state == 3 && paramByte == 101) {
      this.state = 4;
    } else if (this.state == 4 && paramByte == 105) {
      this.state = 5;
    } else if (this.state == 5 && paramByte == 95) {
      this.state = 6;
    } else if (this.state == 6 && paramByte == 99) {
      this.state = 7;
    } else if (this.state == 7 && paramByte == 109) {
      this.state = 8;
    } else if (this.state == 8 && paramByte == 100) {
      this.state = 9;
    } else if (this.state == 9 && paramByte == 0) {
      this.state = 10;
    } else {
      if (this.state == 10) {
        this.state = 0;
        return true;
      } 
      this.state = 0;
    } 
    return false;
  }
  
  public OnFrameDataCallback getOnFrameDataCallback() {
    return this.onFrameDataCallback;
  }
  
  public void onVideoData(byte[] paramArrayOfbyte, int paramInt) {
    // Byte code:
    //   0: iconst_0
    //   1: istore_3
    //   2: iload_3
    //   3: iload_2
    //   4: if_icmpge -> 411
    //   7: aload_0
    //   8: aload_1
    //   9: iload_3
    //   10: baload
    //   11: invokespecial isFrameStart : (B)Z
    //   14: ifeq -> 22
    //   17: aload_0
    //   18: iconst_0
    //   19: putfield tempBytesCurrentPosition : I
    //   22: aload_0
    //   23: getfield tempBytesCurrentPosition : I
    //   26: istore #4
    //   28: iload #4
    //   30: ifge -> 36
    //   33: goto -> 405
    //   36: aload_0
    //   37: getfield tempBytes : [B
    //   40: astore #5
    //   42: aload #5
    //   44: iload #4
    //   46: aload_1
    //   47: iload_3
    //   48: baload
    //   49: bastore
    //   50: iload #4
    //   52: bipush #35
    //   54: if_icmpne -> 171
    //   57: aload #5
    //   59: iconst_0
    //   60: baload
    //   61: iconst_1
    //   62: if_icmpeq -> 73
    //   65: aload #5
    //   67: iconst_0
    //   68: baload
    //   69: iconst_3
    //   70: if_icmpne -> 87
    //   73: aload_0
    //   74: getfield tempBytes : [B
    //   77: astore #5
    //   79: aload #5
    //   81: iconst_1
    //   82: baload
    //   83: iconst_1
    //   84: if_icmpeq -> 95
    //   87: aload_0
    //   88: iconst_m1
    //   89: putfield tempBytesCurrentPosition : I
    //   92: goto -> 405
    //   95: aload #5
    //   97: iconst_0
    //   98: baload
    //   99: iconst_1
    //   100: if_icmpne -> 117
    //   103: aload #5
    //   105: iconst_1
    //   106: baload
    //   107: iconst_1
    //   108: if_icmpne -> 117
    //   111: aload_0
    //   112: bipush #32
    //   114: putfield remainingDataHeadLength : I
    //   117: aload_0
    //   118: getfield tempBytes : [B
    //   121: astore #5
    //   123: aload #5
    //   125: iconst_0
    //   126: baload
    //   127: iconst_3
    //   128: if_icmpne -> 145
    //   131: aload #5
    //   133: iconst_1
    //   134: baload
    //   135: iconst_1
    //   136: if_icmpne -> 145
    //   139: aload_0
    //   140: bipush #40
    //   142: putfield remainingDataHeadLength : I
    //   145: aload_0
    //   146: aload_0
    //   147: getfield tempBytes : [B
    //   150: bipush #12
    //   152: invokestatic byte2IntLittle : ([BI)I
    //   155: putfield remainingLength : I
    //   158: aload_0
    //   159: aload_0
    //   160: getfield tempBytes : [B
    //   163: bipush #16
    //   165: invokestatic byte2IntLittle : ([BI)I
    //   168: putfield devicesType : I
    //   171: aload_0
    //   172: getfield tempBytesCurrentPosition : I
    //   175: istore #6
    //   177: aload_0
    //   178: getfield remainingLength : I
    //   181: istore #4
    //   183: iload #6
    //   185: iload #4
    //   187: bipush #12
    //   189: iadd
    //   190: bipush #23
    //   192: iadd
    //   193: if_icmpne -> 395
    //   196: iload #4
    //   198: aload_0
    //   199: getfield remainingDataHeadLength : I
    //   202: if_icmple -> 395
    //   205: aload_0
    //   206: getfield tempBytes : [B
    //   209: astore #5
    //   211: aload #5
    //   213: iconst_0
    //   214: baload
    //   215: iconst_1
    //   216: if_icmpne -> 324
    //   219: aload #5
    //   221: iconst_1
    //   222: baload
    //   223: iconst_1
    //   224: if_icmpne -> 324
    //   227: aload #5
    //   229: bipush #54
    //   231: baload
    //   232: istore #4
    //   234: aload_0
    //   235: getfield lastRemoteCMD : B
    //   238: istore #6
    //   240: iload #4
    //   242: iload #6
    //   244: if_icmpeq -> 317
    //   247: iload #6
    //   249: iconst_m1
    //   250: if_icmpeq -> 317
    //   253: aload_0
    //   254: getfield onFrameDataCallback : Lcom/netopsun/anykadevices/AnykaVideoFrameDataExtractor$OnFrameDataCallback;
    //   257: astore #7
    //   259: aload #7
    //   261: ifnull -> 317
    //   264: aload #5
    //   266: bipush #53
    //   268: baload
    //   269: istore #6
    //   271: iload #6
    //   273: iconst_1
    //   274: if_icmpeq -> 310
    //   277: iload #6
    //   279: iconst_2
    //   280: if_icmpeq -> 298
    //   283: iload #6
    //   285: iconst_3
    //   286: if_icmpeq -> 298
    //   289: iload #6
    //   291: iconst_4
    //   292: if_icmpeq -> 298
    //   295: goto -> 317
    //   298: aload_0
    //   299: getfield onFrameDataCallback : Lcom/netopsun/anykadevices/AnykaVideoFrameDataExtractor$OnFrameDataCallback;
    //   302: invokeinterface onRemoteRecord : ()V
    //   307: goto -> 317
    //   310: aload #7
    //   312: invokeinterface onRemoteTakePhoto : ()V
    //   317: aload_0
    //   318: iload #4
    //   320: i2b
    //   321: putfield lastRemoteCMD : B
    //   324: aload_0
    //   325: getfield devicesType : I
    //   328: iconst_1
    //   329: if_icmpne -> 346
    //   332: aload_0
    //   333: aload_0
    //   334: getfield tempBytes : [B
    //   337: bipush #36
    //   339: aload_0
    //   340: getfield remainingLength : I
    //   343: invokespecial decryptFrame : ([BII)V
    //   346: aload_0
    //   347: getfield onFrameDataCallback : Lcom/netopsun/anykadevices/AnykaVideoFrameDataExtractor$OnFrameDataCallback;
    //   350: astore #5
    //   352: aload #5
    //   354: ifnull -> 390
    //   357: aload_0
    //   358: getfield tempBytes : [B
    //   361: astore #7
    //   363: aload_0
    //   364: getfield remainingDataHeadLength : I
    //   367: istore #4
    //   369: aload #5
    //   371: aload #7
    //   373: iload #4
    //   375: bipush #36
    //   377: iadd
    //   378: aload_0
    //   379: getfield remainingLength : I
    //   382: iload #4
    //   384: isub
    //   385: invokeinterface onFrameDataAvailable : ([BII)V
    //   390: aload_0
    //   391: iconst_0
    //   392: putfield tempBytesCurrentPosition : I
    //   395: aload_0
    //   396: aload_0
    //   397: getfield tempBytesCurrentPosition : I
    //   400: iconst_1
    //   401: iadd
    //   402: putfield tempBytesCurrentPosition : I
    //   405: iinc #3, 1
    //   408: goto -> 2
    //   411: return
  }
  
  public void setOnFrameDataCallback(OnFrameDataCallback paramOnFrameDataCallback) {
    this.onFrameDataCallback = paramOnFrameDataCallback;
  }
  
  public static interface OnFrameDataCallback {
    void onFrameDataAvailable(byte[] param1ArrayOfbyte, int param1Int1, int param1Int2);
    
    void onRemoteRecord();
    
    void onRemoteTakePhoto();
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/anykadevices/AnykaVideoFrameDataExtractor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */