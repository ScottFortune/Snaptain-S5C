package com.netopsun.deviceshub.base;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class ConnectHandler {
  private static final int CONNECT_CMD = 104;
  
  private static final int CONNECT_RxTx = 107;
  
  private static final int CONNECT_VIDEO = 101;
  
  private static final int DISCONNECT_CMD = 105;
  
  private static final int DISCONNECT_RxTx = 108;
  
  private static final int DISCONNECT_VIDEO = 102;
  
  private static final int RECONNECT_CMD = 106;
  
  private static final int RECONNECT_RxTx = 109;
  
  private static final int RECONNECT_VIDEO = 103;
  
  private volatile Handler handler;
  
  private volatile boolean isRelease;
  
  public ConnectHandler(final Devices devices, final VideoCommunicator videoCommunicator, final CMDCommunicator cmdCommunicator, final RxTxCommunicator rxtxCommunicator) {
    (new Thread(new Runnable() {
          public void run() {
            Looper.prepare();
            ConnectHandler.access$002(ConnectHandler.this, new Handler(new Handler.Callback() {
                    public boolean handleMessage(Message param2Message) {
                      // Byte code:
                      //   0: aload_1
                      //   1: getfield what : I
                      //   4: tableswitch default -> 56, 101 -> 1315, 102 -> 1209, 103 -> 995, 104 -> 847, 105 -> 741, 106 -> 527, 107 -> 379, 108 -> 273, 109 -> 59
                      //   56: goto -> 1463
                      //   59: aload_0
                      //   60: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   63: getfield val$rxtxCommunicator : Lcom/netopsun/deviceshub/base/RxTxCommunicator;
                      //   66: invokevirtual isConnected : ()Z
                      //   69: ifne -> 159
                      //   72: aload_0
                      //   73: monitorenter
                      //   74: aload_0
                      //   75: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   78: getfield this$0 : Lcom/netopsun/deviceshub/base/ConnectHandler;
                      //   81: invokestatic access$000 : (Lcom/netopsun/deviceshub/base/ConnectHandler;)Landroid/os/Handler;
                      //   84: bipush #108
                      //   86: invokevirtual hasMessages : (I)Z
                      //   89: ifeq -> 97
                      //   92: aload_0
                      //   93: monitorexit
                      //   94: goto -> 1463
                      //   97: aload_0
                      //   98: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   101: getfield this$0 : Lcom/netopsun/deviceshub/base/ConnectHandler;
                      //   104: invokestatic access$000 : (Lcom/netopsun/deviceshub/base/ConnectHandler;)Landroid/os/Handler;
                      //   107: bipush #107
                      //   109: invokevirtual hasMessages : (I)Z
                      //   112: ifne -> 149
                      //   115: aload_0
                      //   116: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   119: getfield this$0 : Lcom/netopsun/deviceshub/base/ConnectHandler;
                      //   122: invokestatic access$000 : (Lcom/netopsun/deviceshub/base/ConnectHandler;)Landroid/os/Handler;
                      //   125: bipush #109
                      //   127: invokevirtual hasMessages : (I)Z
                      //   130: ifne -> 149
                      //   133: aload_0
                      //   134: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   137: getfield this$0 : Lcom/netopsun/deviceshub/base/ConnectHandler;
                      //   140: invokestatic access$000 : (Lcom/netopsun/deviceshub/base/ConnectHandler;)Landroid/os/Handler;
                      //   143: bipush #107
                      //   145: invokevirtual sendEmptyMessage : (I)Z
                      //   148: pop
                      //   149: aload_0
                      //   150: monitorexit
                      //   151: goto -> 1463
                      //   154: astore_1
                      //   155: aload_0
                      //   156: monitorexit
                      //   157: aload_1
                      //   158: athrow
                      //   159: aload_0
                      //   160: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   163: getfield val$rxtxCommunicator : Lcom/netopsun/deviceshub/base/RxTxCommunicator;
                      //   166: invokevirtual disconnectInternal : ()I
                      //   169: ifge -> 186
                      //   172: aload_0
                      //   173: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   176: getfield val$rxtxCommunicator : Lcom/netopsun/deviceshub/base/RxTxCommunicator;
                      //   179: invokevirtual disconnectInternal : ()I
                      //   182: pop
                      //   183: goto -> 1463
                      //   186: aload_0
                      //   187: monitorenter
                      //   188: aload_0
                      //   189: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   192: getfield this$0 : Lcom/netopsun/deviceshub/base/ConnectHandler;
                      //   195: invokestatic access$000 : (Lcom/netopsun/deviceshub/base/ConnectHandler;)Landroid/os/Handler;
                      //   198: bipush #108
                      //   200: invokevirtual hasMessages : (I)Z
                      //   203: ifeq -> 211
                      //   206: aload_0
                      //   207: monitorexit
                      //   208: goto -> 1463
                      //   211: aload_0
                      //   212: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   215: getfield this$0 : Lcom/netopsun/deviceshub/base/ConnectHandler;
                      //   218: invokestatic access$000 : (Lcom/netopsun/deviceshub/base/ConnectHandler;)Landroid/os/Handler;
                      //   221: bipush #107
                      //   223: invokevirtual hasMessages : (I)Z
                      //   226: ifne -> 263
                      //   229: aload_0
                      //   230: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   233: getfield this$0 : Lcom/netopsun/deviceshub/base/ConnectHandler;
                      //   236: invokestatic access$000 : (Lcom/netopsun/deviceshub/base/ConnectHandler;)Landroid/os/Handler;
                      //   239: bipush #109
                      //   241: invokevirtual hasMessages : (I)Z
                      //   244: ifne -> 263
                      //   247: aload_0
                      //   248: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   251: getfield this$0 : Lcom/netopsun/deviceshub/base/ConnectHandler;
                      //   254: invokestatic access$000 : (Lcom/netopsun/deviceshub/base/ConnectHandler;)Landroid/os/Handler;
                      //   257: bipush #107
                      //   259: invokevirtual sendEmptyMessage : (I)Z
                      //   262: pop
                      //   263: aload_0
                      //   264: monitorexit
                      //   265: goto -> 1463
                      //   268: astore_1
                      //   269: aload_0
                      //   270: monitorexit
                      //   271: aload_1
                      //   272: athrow
                      //   273: aload_0
                      //   274: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   277: getfield val$rxtxCommunicator : Lcom/netopsun/deviceshub/base/RxTxCommunicator;
                      //   280: invokevirtual isConnected : ()Z
                      //   283: ifne -> 289
                      //   286: goto -> 1463
                      //   289: aload_0
                      //   290: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   293: getfield val$rxtxCommunicator : Lcom/netopsun/deviceshub/base/RxTxCommunicator;
                      //   296: invokevirtual disconnectInternal : ()I
                      //   299: ifge -> 316
                      //   302: aload_0
                      //   303: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   306: getfield val$rxtxCommunicator : Lcom/netopsun/deviceshub/base/RxTxCommunicator;
                      //   309: invokevirtual disconnectInternal : ()I
                      //   312: pop
                      //   313: goto -> 1463
                      //   316: aload_0
                      //   317: monitorenter
                      //   318: aload_0
                      //   319: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   322: getfield this$0 : Lcom/netopsun/deviceshub/base/ConnectHandler;
                      //   325: invokestatic access$000 : (Lcom/netopsun/deviceshub/base/ConnectHandler;)Landroid/os/Handler;
                      //   328: bipush #108
                      //   330: invokevirtual hasMessages : (I)Z
                      //   333: ifeq -> 369
                      //   336: aload_0
                      //   337: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   340: getfield this$0 : Lcom/netopsun/deviceshub/base/ConnectHandler;
                      //   343: invokestatic access$000 : (Lcom/netopsun/deviceshub/base/ConnectHandler;)Landroid/os/Handler;
                      //   346: bipush #107
                      //   348: invokevirtual hasMessages : (I)Z
                      //   351: ifne -> 369
                      //   354: aload_0
                      //   355: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   358: getfield this$0 : Lcom/netopsun/deviceshub/base/ConnectHandler;
                      //   361: invokestatic access$000 : (Lcom/netopsun/deviceshub/base/ConnectHandler;)Landroid/os/Handler;
                      //   364: bipush #108
                      //   366: invokevirtual removeMessages : (I)V
                      //   369: aload_0
                      //   370: monitorexit
                      //   371: goto -> 1463
                      //   374: astore_1
                      //   375: aload_0
                      //   376: monitorexit
                      //   377: aload_1
                      //   378: athrow
                      //   379: aload_0
                      //   380: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   383: getfield val$rxtxCommunicator : Lcom/netopsun/deviceshub/base/RxTxCommunicator;
                      //   386: invokevirtual isConnected : ()Z
                      //   389: ifeq -> 395
                      //   392: goto -> 1463
                      //   395: aload_0
                      //   396: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   399: getfield val$devices : Lcom/netopsun/deviceshub/base/Devices;
                      //   402: invokevirtual IFNeedInitDevices : ()I
                      //   405: ifge -> 428
                      //   408: aload_0
                      //   409: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   412: getfield this$0 : Lcom/netopsun/deviceshub/base/ConnectHandler;
                      //   415: aload_0
                      //   416: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   419: getfield val$rxtxCommunicator : Lcom/netopsun/deviceshub/base/RxTxCommunicator;
                      //   422: invokestatic access$300 : (Lcom/netopsun/deviceshub/base/ConnectHandler;Lcom/netopsun/deviceshub/base/RxTxCommunicator;)V
                      //   425: goto -> 1463
                      //   428: aload_0
                      //   429: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   432: getfield val$rxtxCommunicator : Lcom/netopsun/deviceshub/base/RxTxCommunicator;
                      //   435: invokevirtual connectInternal : ()I
                      //   438: ifge -> 461
                      //   441: aload_0
                      //   442: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   445: getfield this$0 : Lcom/netopsun/deviceshub/base/ConnectHandler;
                      //   448: aload_0
                      //   449: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   452: getfield val$rxtxCommunicator : Lcom/netopsun/deviceshub/base/RxTxCommunicator;
                      //   455: invokestatic access$300 : (Lcom/netopsun/deviceshub/base/ConnectHandler;Lcom/netopsun/deviceshub/base/RxTxCommunicator;)V
                      //   458: goto -> 1463
                      //   461: aload_0
                      //   462: monitorenter
                      //   463: aload_0
                      //   464: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   467: getfield this$0 : Lcom/netopsun/deviceshub/base/ConnectHandler;
                      //   470: invokestatic access$000 : (Lcom/netopsun/deviceshub/base/ConnectHandler;)Landroid/os/Handler;
                      //   473: bipush #107
                      //   475: invokevirtual removeMessages : (I)V
                      //   478: aload_0
                      //   479: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   482: getfield this$0 : Lcom/netopsun/deviceshub/base/ConnectHandler;
                      //   485: invokestatic access$000 : (Lcom/netopsun/deviceshub/base/ConnectHandler;)Landroid/os/Handler;
                      //   488: bipush #109
                      //   490: invokevirtual removeMessages : (I)V
                      //   493: aload_0
                      //   494: monitorexit
                      //   495: aload_0
                      //   496: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   499: getfield val$rxtxCommunicator : Lcom/netopsun/deviceshub/base/RxTxCommunicator;
                      //   502: invokevirtual getConnectResultCallback : ()Lcom/netopsun/deviceshub/interfaces/ConnectResultCallback;
                      //   505: astore_1
                      //   506: aload_1
                      //   507: ifnull -> 1463
                      //   510: aload_1
                      //   511: iconst_0
                      //   512: ldc ''
                      //   514: invokeinterface onConnectSuccess : (ILjava/lang/String;)V
                      //   519: goto -> 1463
                      //   522: astore_1
                      //   523: aload_0
                      //   524: monitorexit
                      //   525: aload_1
                      //   526: athrow
                      //   527: aload_0
                      //   528: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   531: getfield val$cmdCommunicator : Lcom/netopsun/deviceshub/base/CMDCommunicator;
                      //   534: invokevirtual isConnected : ()Z
                      //   537: ifne -> 627
                      //   540: aload_0
                      //   541: monitorenter
                      //   542: aload_0
                      //   543: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   546: getfield this$0 : Lcom/netopsun/deviceshub/base/ConnectHandler;
                      //   549: invokestatic access$000 : (Lcom/netopsun/deviceshub/base/ConnectHandler;)Landroid/os/Handler;
                      //   552: bipush #105
                      //   554: invokevirtual hasMessages : (I)Z
                      //   557: ifeq -> 565
                      //   560: aload_0
                      //   561: monitorexit
                      //   562: goto -> 1463
                      //   565: aload_0
                      //   566: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   569: getfield this$0 : Lcom/netopsun/deviceshub/base/ConnectHandler;
                      //   572: invokestatic access$000 : (Lcom/netopsun/deviceshub/base/ConnectHandler;)Landroid/os/Handler;
                      //   575: bipush #104
                      //   577: invokevirtual hasMessages : (I)Z
                      //   580: ifne -> 617
                      //   583: aload_0
                      //   584: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   587: getfield this$0 : Lcom/netopsun/deviceshub/base/ConnectHandler;
                      //   590: invokestatic access$000 : (Lcom/netopsun/deviceshub/base/ConnectHandler;)Landroid/os/Handler;
                      //   593: bipush #106
                      //   595: invokevirtual hasMessages : (I)Z
                      //   598: ifne -> 617
                      //   601: aload_0
                      //   602: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   605: getfield this$0 : Lcom/netopsun/deviceshub/base/ConnectHandler;
                      //   608: invokestatic access$000 : (Lcom/netopsun/deviceshub/base/ConnectHandler;)Landroid/os/Handler;
                      //   611: bipush #104
                      //   613: invokevirtual sendEmptyMessage : (I)Z
                      //   616: pop
                      //   617: aload_0
                      //   618: monitorexit
                      //   619: goto -> 1463
                      //   622: astore_1
                      //   623: aload_0
                      //   624: monitorexit
                      //   625: aload_1
                      //   626: athrow
                      //   627: aload_0
                      //   628: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   631: getfield val$cmdCommunicator : Lcom/netopsun/deviceshub/base/CMDCommunicator;
                      //   634: invokevirtual disconnectInternal : ()I
                      //   637: ifge -> 654
                      //   640: aload_0
                      //   641: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   644: getfield val$cmdCommunicator : Lcom/netopsun/deviceshub/base/CMDCommunicator;
                      //   647: invokevirtual disconnectInternal : ()I
                      //   650: pop
                      //   651: goto -> 1463
                      //   654: aload_0
                      //   655: monitorenter
                      //   656: aload_0
                      //   657: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   660: getfield this$0 : Lcom/netopsun/deviceshub/base/ConnectHandler;
                      //   663: invokestatic access$000 : (Lcom/netopsun/deviceshub/base/ConnectHandler;)Landroid/os/Handler;
                      //   666: bipush #105
                      //   668: invokevirtual hasMessages : (I)Z
                      //   671: ifeq -> 679
                      //   674: aload_0
                      //   675: monitorexit
                      //   676: goto -> 1463
                      //   679: aload_0
                      //   680: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   683: getfield this$0 : Lcom/netopsun/deviceshub/base/ConnectHandler;
                      //   686: invokestatic access$000 : (Lcom/netopsun/deviceshub/base/ConnectHandler;)Landroid/os/Handler;
                      //   689: bipush #104
                      //   691: invokevirtual hasMessages : (I)Z
                      //   694: ifne -> 731
                      //   697: aload_0
                      //   698: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   701: getfield this$0 : Lcom/netopsun/deviceshub/base/ConnectHandler;
                      //   704: invokestatic access$000 : (Lcom/netopsun/deviceshub/base/ConnectHandler;)Landroid/os/Handler;
                      //   707: bipush #106
                      //   709: invokevirtual hasMessages : (I)Z
                      //   712: ifne -> 731
                      //   715: aload_0
                      //   716: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   719: getfield this$0 : Lcom/netopsun/deviceshub/base/ConnectHandler;
                      //   722: invokestatic access$000 : (Lcom/netopsun/deviceshub/base/ConnectHandler;)Landroid/os/Handler;
                      //   725: bipush #104
                      //   727: invokevirtual sendEmptyMessage : (I)Z
                      //   730: pop
                      //   731: aload_0
                      //   732: monitorexit
                      //   733: goto -> 1463
                      //   736: astore_1
                      //   737: aload_0
                      //   738: monitorexit
                      //   739: aload_1
                      //   740: athrow
                      //   741: aload_0
                      //   742: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   745: getfield val$cmdCommunicator : Lcom/netopsun/deviceshub/base/CMDCommunicator;
                      //   748: invokevirtual isConnected : ()Z
                      //   751: ifne -> 757
                      //   754: goto -> 1463
                      //   757: aload_0
                      //   758: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   761: getfield val$cmdCommunicator : Lcom/netopsun/deviceshub/base/CMDCommunicator;
                      //   764: invokevirtual disconnectInternal : ()I
                      //   767: ifge -> 784
                      //   770: aload_0
                      //   771: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   774: getfield val$cmdCommunicator : Lcom/netopsun/deviceshub/base/CMDCommunicator;
                      //   777: invokevirtual disconnectInternal : ()I
                      //   780: pop
                      //   781: goto -> 1463
                      //   784: aload_0
                      //   785: monitorenter
                      //   786: aload_0
                      //   787: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   790: getfield this$0 : Lcom/netopsun/deviceshub/base/ConnectHandler;
                      //   793: invokestatic access$000 : (Lcom/netopsun/deviceshub/base/ConnectHandler;)Landroid/os/Handler;
                      //   796: bipush #105
                      //   798: invokevirtual hasMessages : (I)Z
                      //   801: ifeq -> 837
                      //   804: aload_0
                      //   805: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   808: getfield this$0 : Lcom/netopsun/deviceshub/base/ConnectHandler;
                      //   811: invokestatic access$000 : (Lcom/netopsun/deviceshub/base/ConnectHandler;)Landroid/os/Handler;
                      //   814: bipush #104
                      //   816: invokevirtual hasMessages : (I)Z
                      //   819: ifne -> 837
                      //   822: aload_0
                      //   823: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   826: getfield this$0 : Lcom/netopsun/deviceshub/base/ConnectHandler;
                      //   829: invokestatic access$000 : (Lcom/netopsun/deviceshub/base/ConnectHandler;)Landroid/os/Handler;
                      //   832: bipush #105
                      //   834: invokevirtual removeMessages : (I)V
                      //   837: aload_0
                      //   838: monitorexit
                      //   839: goto -> 1463
                      //   842: astore_1
                      //   843: aload_0
                      //   844: monitorexit
                      //   845: aload_1
                      //   846: athrow
                      //   847: aload_0
                      //   848: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   851: getfield val$cmdCommunicator : Lcom/netopsun/deviceshub/base/CMDCommunicator;
                      //   854: invokevirtual isConnected : ()Z
                      //   857: ifeq -> 863
                      //   860: goto -> 1463
                      //   863: aload_0
                      //   864: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   867: getfield val$devices : Lcom/netopsun/deviceshub/base/Devices;
                      //   870: invokevirtual IFNeedInitDevices : ()I
                      //   873: ifge -> 896
                      //   876: aload_0
                      //   877: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   880: getfield this$0 : Lcom/netopsun/deviceshub/base/ConnectHandler;
                      //   883: aload_0
                      //   884: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   887: getfield val$cmdCommunicator : Lcom/netopsun/deviceshub/base/CMDCommunicator;
                      //   890: invokestatic access$200 : (Lcom/netopsun/deviceshub/base/ConnectHandler;Lcom/netopsun/deviceshub/base/CMDCommunicator;)V
                      //   893: goto -> 1463
                      //   896: aload_0
                      //   897: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   900: getfield val$cmdCommunicator : Lcom/netopsun/deviceshub/base/CMDCommunicator;
                      //   903: invokevirtual connectInternal : ()I
                      //   906: ifge -> 929
                      //   909: aload_0
                      //   910: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   913: getfield this$0 : Lcom/netopsun/deviceshub/base/ConnectHandler;
                      //   916: aload_0
                      //   917: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   920: getfield val$cmdCommunicator : Lcom/netopsun/deviceshub/base/CMDCommunicator;
                      //   923: invokestatic access$200 : (Lcom/netopsun/deviceshub/base/ConnectHandler;Lcom/netopsun/deviceshub/base/CMDCommunicator;)V
                      //   926: goto -> 1463
                      //   929: aload_0
                      //   930: monitorenter
                      //   931: aload_0
                      //   932: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   935: getfield this$0 : Lcom/netopsun/deviceshub/base/ConnectHandler;
                      //   938: invokestatic access$000 : (Lcom/netopsun/deviceshub/base/ConnectHandler;)Landroid/os/Handler;
                      //   941: bipush #104
                      //   943: invokevirtual removeMessages : (I)V
                      //   946: aload_0
                      //   947: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   950: getfield this$0 : Lcom/netopsun/deviceshub/base/ConnectHandler;
                      //   953: invokestatic access$000 : (Lcom/netopsun/deviceshub/base/ConnectHandler;)Landroid/os/Handler;
                      //   956: bipush #106
                      //   958: invokevirtual removeMessages : (I)V
                      //   961: aload_0
                      //   962: monitorexit
                      //   963: aload_0
                      //   964: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   967: getfield val$cmdCommunicator : Lcom/netopsun/deviceshub/base/CMDCommunicator;
                      //   970: invokevirtual getConnectResultCallback : ()Lcom/netopsun/deviceshub/interfaces/ConnectResultCallback;
                      //   973: astore_1
                      //   974: aload_1
                      //   975: ifnull -> 1463
                      //   978: aload_1
                      //   979: iconst_0
                      //   980: ldc ''
                      //   982: invokeinterface onConnectSuccess : (ILjava/lang/String;)V
                      //   987: goto -> 1463
                      //   990: astore_1
                      //   991: aload_0
                      //   992: monitorexit
                      //   993: aload_1
                      //   994: athrow
                      //   995: aload_0
                      //   996: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   999: getfield val$videoCommunicator : Lcom/netopsun/deviceshub/base/VideoCommunicator;
                      //   1002: invokevirtual isConnected : ()Z
                      //   1005: ifne -> 1095
                      //   1008: aload_0
                      //   1009: monitorenter
                      //   1010: aload_0
                      //   1011: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   1014: getfield this$0 : Lcom/netopsun/deviceshub/base/ConnectHandler;
                      //   1017: invokestatic access$000 : (Lcom/netopsun/deviceshub/base/ConnectHandler;)Landroid/os/Handler;
                      //   1020: bipush #102
                      //   1022: invokevirtual hasMessages : (I)Z
                      //   1025: ifeq -> 1033
                      //   1028: aload_0
                      //   1029: monitorexit
                      //   1030: goto -> 1463
                      //   1033: aload_0
                      //   1034: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   1037: getfield this$0 : Lcom/netopsun/deviceshub/base/ConnectHandler;
                      //   1040: invokestatic access$000 : (Lcom/netopsun/deviceshub/base/ConnectHandler;)Landroid/os/Handler;
                      //   1043: bipush #101
                      //   1045: invokevirtual hasMessages : (I)Z
                      //   1048: ifne -> 1085
                      //   1051: aload_0
                      //   1052: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   1055: getfield this$0 : Lcom/netopsun/deviceshub/base/ConnectHandler;
                      //   1058: invokestatic access$000 : (Lcom/netopsun/deviceshub/base/ConnectHandler;)Landroid/os/Handler;
                      //   1061: bipush #103
                      //   1063: invokevirtual hasMessages : (I)Z
                      //   1066: ifne -> 1085
                      //   1069: aload_0
                      //   1070: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   1073: getfield this$0 : Lcom/netopsun/deviceshub/base/ConnectHandler;
                      //   1076: invokestatic access$000 : (Lcom/netopsun/deviceshub/base/ConnectHandler;)Landroid/os/Handler;
                      //   1079: bipush #101
                      //   1081: invokevirtual sendEmptyMessage : (I)Z
                      //   1084: pop
                      //   1085: aload_0
                      //   1086: monitorexit
                      //   1087: goto -> 1463
                      //   1090: astore_1
                      //   1091: aload_0
                      //   1092: monitorexit
                      //   1093: aload_1
                      //   1094: athrow
                      //   1095: aload_0
                      //   1096: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   1099: getfield val$videoCommunicator : Lcom/netopsun/deviceshub/base/VideoCommunicator;
                      //   1102: invokevirtual disconnectInternal : ()I
                      //   1105: ifge -> 1122
                      //   1108: aload_0
                      //   1109: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   1112: getfield val$videoCommunicator : Lcom/netopsun/deviceshub/base/VideoCommunicator;
                      //   1115: invokevirtual disconnectInternal : ()I
                      //   1118: pop
                      //   1119: goto -> 1463
                      //   1122: aload_0
                      //   1123: monitorenter
                      //   1124: aload_0
                      //   1125: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   1128: getfield this$0 : Lcom/netopsun/deviceshub/base/ConnectHandler;
                      //   1131: invokestatic access$000 : (Lcom/netopsun/deviceshub/base/ConnectHandler;)Landroid/os/Handler;
                      //   1134: bipush #102
                      //   1136: invokevirtual hasMessages : (I)Z
                      //   1139: ifeq -> 1147
                      //   1142: aload_0
                      //   1143: monitorexit
                      //   1144: goto -> 1463
                      //   1147: aload_0
                      //   1148: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   1151: getfield this$0 : Lcom/netopsun/deviceshub/base/ConnectHandler;
                      //   1154: invokestatic access$000 : (Lcom/netopsun/deviceshub/base/ConnectHandler;)Landroid/os/Handler;
                      //   1157: bipush #101
                      //   1159: invokevirtual hasMessages : (I)Z
                      //   1162: ifne -> 1199
                      //   1165: aload_0
                      //   1166: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   1169: getfield this$0 : Lcom/netopsun/deviceshub/base/ConnectHandler;
                      //   1172: invokestatic access$000 : (Lcom/netopsun/deviceshub/base/ConnectHandler;)Landroid/os/Handler;
                      //   1175: bipush #103
                      //   1177: invokevirtual hasMessages : (I)Z
                      //   1180: ifne -> 1199
                      //   1183: aload_0
                      //   1184: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   1187: getfield this$0 : Lcom/netopsun/deviceshub/base/ConnectHandler;
                      //   1190: invokestatic access$000 : (Lcom/netopsun/deviceshub/base/ConnectHandler;)Landroid/os/Handler;
                      //   1193: bipush #101
                      //   1195: invokevirtual sendEmptyMessage : (I)Z
                      //   1198: pop
                      //   1199: aload_0
                      //   1200: monitorexit
                      //   1201: goto -> 1463
                      //   1204: astore_1
                      //   1205: aload_0
                      //   1206: monitorexit
                      //   1207: aload_1
                      //   1208: athrow
                      //   1209: aload_0
                      //   1210: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   1213: getfield val$videoCommunicator : Lcom/netopsun/deviceshub/base/VideoCommunicator;
                      //   1216: invokevirtual isConnected : ()Z
                      //   1219: ifne -> 1225
                      //   1222: goto -> 1463
                      //   1225: aload_0
                      //   1226: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   1229: getfield val$videoCommunicator : Lcom/netopsun/deviceshub/base/VideoCommunicator;
                      //   1232: invokevirtual disconnectInternal : ()I
                      //   1235: ifge -> 1252
                      //   1238: aload_0
                      //   1239: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   1242: getfield val$videoCommunicator : Lcom/netopsun/deviceshub/base/VideoCommunicator;
                      //   1245: invokevirtual disconnectInternal : ()I
                      //   1248: pop
                      //   1249: goto -> 1463
                      //   1252: aload_0
                      //   1253: monitorenter
                      //   1254: aload_0
                      //   1255: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   1258: getfield this$0 : Lcom/netopsun/deviceshub/base/ConnectHandler;
                      //   1261: invokestatic access$000 : (Lcom/netopsun/deviceshub/base/ConnectHandler;)Landroid/os/Handler;
                      //   1264: bipush #102
                      //   1266: invokevirtual hasMessages : (I)Z
                      //   1269: ifeq -> 1305
                      //   1272: aload_0
                      //   1273: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   1276: getfield this$0 : Lcom/netopsun/deviceshub/base/ConnectHandler;
                      //   1279: invokestatic access$000 : (Lcom/netopsun/deviceshub/base/ConnectHandler;)Landroid/os/Handler;
                      //   1282: bipush #101
                      //   1284: invokevirtual hasMessages : (I)Z
                      //   1287: ifne -> 1305
                      //   1290: aload_0
                      //   1291: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   1294: getfield this$0 : Lcom/netopsun/deviceshub/base/ConnectHandler;
                      //   1297: invokestatic access$000 : (Lcom/netopsun/deviceshub/base/ConnectHandler;)Landroid/os/Handler;
                      //   1300: bipush #102
                      //   1302: invokevirtual removeMessages : (I)V
                      //   1305: aload_0
                      //   1306: monitorexit
                      //   1307: goto -> 1463
                      //   1310: astore_1
                      //   1311: aload_0
                      //   1312: monitorexit
                      //   1313: aload_1
                      //   1314: athrow
                      //   1315: aload_0
                      //   1316: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   1319: getfield val$videoCommunicator : Lcom/netopsun/deviceshub/base/VideoCommunicator;
                      //   1322: invokevirtual isConnected : ()Z
                      //   1325: ifeq -> 1331
                      //   1328: goto -> 1463
                      //   1331: aload_0
                      //   1332: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   1335: getfield val$devices : Lcom/netopsun/deviceshub/base/Devices;
                      //   1338: invokevirtual IFNeedInitDevices : ()I
                      //   1341: ifge -> 1364
                      //   1344: aload_0
                      //   1345: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   1348: getfield this$0 : Lcom/netopsun/deviceshub/base/ConnectHandler;
                      //   1351: aload_0
                      //   1352: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   1355: getfield val$videoCommunicator : Lcom/netopsun/deviceshub/base/VideoCommunicator;
                      //   1358: invokestatic access$100 : (Lcom/netopsun/deviceshub/base/ConnectHandler;Lcom/netopsun/deviceshub/base/VideoCommunicator;)V
                      //   1361: goto -> 1463
                      //   1364: aload_0
                      //   1365: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   1368: getfield val$videoCommunicator : Lcom/netopsun/deviceshub/base/VideoCommunicator;
                      //   1371: invokevirtual connectInternal : ()I
                      //   1374: ifge -> 1397
                      //   1377: aload_0
                      //   1378: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   1381: getfield this$0 : Lcom/netopsun/deviceshub/base/ConnectHandler;
                      //   1384: aload_0
                      //   1385: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   1388: getfield val$videoCommunicator : Lcom/netopsun/deviceshub/base/VideoCommunicator;
                      //   1391: invokestatic access$100 : (Lcom/netopsun/deviceshub/base/ConnectHandler;Lcom/netopsun/deviceshub/base/VideoCommunicator;)V
                      //   1394: goto -> 1463
                      //   1397: aload_0
                      //   1398: monitorenter
                      //   1399: aload_0
                      //   1400: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   1403: getfield this$0 : Lcom/netopsun/deviceshub/base/ConnectHandler;
                      //   1406: invokestatic access$000 : (Lcom/netopsun/deviceshub/base/ConnectHandler;)Landroid/os/Handler;
                      //   1409: bipush #101
                      //   1411: invokevirtual removeMessages : (I)V
                      //   1414: aload_0
                      //   1415: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   1418: getfield this$0 : Lcom/netopsun/deviceshub/base/ConnectHandler;
                      //   1421: invokestatic access$000 : (Lcom/netopsun/deviceshub/base/ConnectHandler;)Landroid/os/Handler;
                      //   1424: bipush #103
                      //   1426: invokevirtual removeMessages : (I)V
                      //   1429: aload_0
                      //   1430: monitorexit
                      //   1431: aload_0
                      //   1432: getfield this$1 : Lcom/netopsun/deviceshub/base/ConnectHandler$1;
                      //   1435: getfield val$videoCommunicator : Lcom/netopsun/deviceshub/base/VideoCommunicator;
                      //   1438: invokevirtual getConnectResultCallback : ()Lcom/netopsun/deviceshub/interfaces/ConnectResultCallback;
                      //   1441: astore_1
                      //   1442: aload_1
                      //   1443: ifnull -> 1463
                      //   1446: aload_1
                      //   1447: iconst_0
                      //   1448: ldc ''
                      //   1450: invokeinterface onConnectSuccess : (ILjava/lang/String;)V
                      //   1455: goto -> 1463
                      //   1458: astore_1
                      //   1459: aload_0
                      //   1460: monitorexit
                      //   1461: aload_1
                      //   1462: athrow
                      //   1463: iconst_0
                      //   1464: ireturn
                      // Exception table:
                      //   from	to	target	type
                      //   74	94	154	finally
                      //   97	149	154	finally
                      //   149	151	154	finally
                      //   155	157	154	finally
                      //   188	208	268	finally
                      //   211	263	268	finally
                      //   263	265	268	finally
                      //   269	271	268	finally
                      //   318	369	374	finally
                      //   369	371	374	finally
                      //   375	377	374	finally
                      //   463	495	522	finally
                      //   523	525	522	finally
                      //   542	562	622	finally
                      //   565	617	622	finally
                      //   617	619	622	finally
                      //   623	625	622	finally
                      //   656	676	736	finally
                      //   679	731	736	finally
                      //   731	733	736	finally
                      //   737	739	736	finally
                      //   786	837	842	finally
                      //   837	839	842	finally
                      //   843	845	842	finally
                      //   931	963	990	finally
                      //   991	993	990	finally
                      //   1010	1030	1090	finally
                      //   1033	1085	1090	finally
                      //   1085	1087	1090	finally
                      //   1091	1093	1090	finally
                      //   1124	1144	1204	finally
                      //   1147	1199	1204	finally
                      //   1199	1201	1204	finally
                      //   1205	1207	1204	finally
                      //   1254	1305	1310	finally
                      //   1305	1307	1310	finally
                      //   1311	1313	1310	finally
                      //   1399	1431	1458	finally
                      //   1459	1461	1458	finally
                    }
                  }));
            Looper.loop();
          }
        })).start();
    while (this.handler == null);
  }
  
  private void checkIfNeedConnectCMDAgain(CMDCommunicator paramCMDCommunicator) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield handler : Landroid/os/Handler;
    //   6: bipush #105
    //   8: invokevirtual hasMessages : (I)Z
    //   11: ifeq -> 26
    //   14: aload_0
    //   15: getfield handler : Landroid/os/Handler;
    //   18: bipush #105
    //   20: invokevirtual removeMessages : (I)V
    //   23: aload_0
    //   24: monitorexit
    //   25: return
    //   26: aload_1
    //   27: invokevirtual shouldRetryConnect : ()Z
    //   30: ifeq -> 67
    //   33: aload_0
    //   34: getfield handler : Landroid/os/Handler;
    //   37: bipush #104
    //   39: invokevirtual hasMessages : (I)Z
    //   42: ifne -> 67
    //   45: aload_0
    //   46: getfield handler : Landroid/os/Handler;
    //   49: bipush #106
    //   51: invokevirtual hasMessages : (I)Z
    //   54: ifne -> 67
    //   57: aload_0
    //   58: getfield handler : Landroid/os/Handler;
    //   61: bipush #104
    //   63: invokevirtual sendEmptyMessage : (I)Z
    //   66: pop
    //   67: aload_0
    //   68: monitorexit
    //   69: return
    //   70: astore_1
    //   71: aload_0
    //   72: monitorexit
    //   73: aload_1
    //   74: athrow
    // Exception table:
    //   from	to	target	type
    //   2	23	70	finally
    //   26	67	70	finally
  }
  
  private void checkIfNeedConnectRxTxAgain(RxTxCommunicator paramRxTxCommunicator) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield handler : Landroid/os/Handler;
    //   6: bipush #108
    //   8: invokevirtual hasMessages : (I)Z
    //   11: ifeq -> 26
    //   14: aload_0
    //   15: getfield handler : Landroid/os/Handler;
    //   18: bipush #108
    //   20: invokevirtual removeMessages : (I)V
    //   23: aload_0
    //   24: monitorexit
    //   25: return
    //   26: aload_1
    //   27: invokevirtual shouldRetryConnect : ()Z
    //   30: ifeq -> 67
    //   33: aload_0
    //   34: getfield handler : Landroid/os/Handler;
    //   37: bipush #107
    //   39: invokevirtual hasMessages : (I)Z
    //   42: ifne -> 67
    //   45: aload_0
    //   46: getfield handler : Landroid/os/Handler;
    //   49: bipush #109
    //   51: invokevirtual hasMessages : (I)Z
    //   54: ifne -> 67
    //   57: aload_0
    //   58: getfield handler : Landroid/os/Handler;
    //   61: bipush #107
    //   63: invokevirtual sendEmptyMessage : (I)Z
    //   66: pop
    //   67: aload_0
    //   68: monitorexit
    //   69: return
    //   70: astore_1
    //   71: aload_0
    //   72: monitorexit
    //   73: aload_1
    //   74: athrow
    // Exception table:
    //   from	to	target	type
    //   2	23	70	finally
    //   26	67	70	finally
  }
  
  private void checkIfNeedConnectVideoAgain(VideoCommunicator paramVideoCommunicator) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield handler : Landroid/os/Handler;
    //   6: bipush #102
    //   8: invokevirtual hasMessages : (I)Z
    //   11: ifeq -> 26
    //   14: aload_0
    //   15: getfield handler : Landroid/os/Handler;
    //   18: bipush #102
    //   20: invokevirtual removeMessages : (I)V
    //   23: aload_0
    //   24: monitorexit
    //   25: return
    //   26: aload_1
    //   27: invokevirtual shouldRetryConnect : ()Z
    //   30: ifeq -> 67
    //   33: aload_0
    //   34: getfield handler : Landroid/os/Handler;
    //   37: bipush #101
    //   39: invokevirtual hasMessages : (I)Z
    //   42: ifne -> 67
    //   45: aload_0
    //   46: getfield handler : Landroid/os/Handler;
    //   49: bipush #103
    //   51: invokevirtual hasMessages : (I)Z
    //   54: ifne -> 67
    //   57: aload_0
    //   58: getfield handler : Landroid/os/Handler;
    //   61: bipush #101
    //   63: invokevirtual sendEmptyMessage : (I)Z
    //   66: pop
    //   67: aload_0
    //   68: monitorexit
    //   69: return
    //   70: astore_1
    //   71: aload_0
    //   72: monitorexit
    //   73: aload_1
    //   74: athrow
    // Exception table:
    //   from	to	target	type
    //   2	23	70	finally
    //   26	67	70	finally
  }
  
  public Handler getHandler() {
    return this.handler;
  }
  
  public boolean isRelease() {
    return this.isRelease;
  }
  
  public void notifyConnectCMD() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield handler : Landroid/os/Handler;
    //   6: bipush #105
    //   8: invokevirtual hasMessages : (I)Z
    //   11: ifeq -> 23
    //   14: aload_0
    //   15: getfield handler : Landroid/os/Handler;
    //   18: bipush #105
    //   20: invokevirtual removeMessages : (I)V
    //   23: aload_0
    //   24: getfield handler : Landroid/os/Handler;
    //   27: bipush #104
    //   29: invokevirtual hasMessages : (I)Z
    //   32: ifne -> 57
    //   35: aload_0
    //   36: getfield handler : Landroid/os/Handler;
    //   39: bipush #106
    //   41: invokevirtual hasMessages : (I)Z
    //   44: ifne -> 57
    //   47: aload_0
    //   48: getfield handler : Landroid/os/Handler;
    //   51: bipush #104
    //   53: invokevirtual sendEmptyMessage : (I)Z
    //   56: pop
    //   57: aload_0
    //   58: monitorexit
    //   59: return
    //   60: astore_1
    //   61: aload_0
    //   62: monitorexit
    //   63: aload_1
    //   64: athrow
    // Exception table:
    //   from	to	target	type
    //   2	23	60	finally
    //   23	57	60	finally
  }
  
  public void notifyConnectRxTx() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield handler : Landroid/os/Handler;
    //   6: bipush #108
    //   8: invokevirtual hasMessages : (I)Z
    //   11: ifeq -> 23
    //   14: aload_0
    //   15: getfield handler : Landroid/os/Handler;
    //   18: bipush #108
    //   20: invokevirtual removeMessages : (I)V
    //   23: aload_0
    //   24: getfield handler : Landroid/os/Handler;
    //   27: bipush #107
    //   29: invokevirtual hasMessages : (I)Z
    //   32: ifne -> 57
    //   35: aload_0
    //   36: getfield handler : Landroid/os/Handler;
    //   39: bipush #109
    //   41: invokevirtual hasMessages : (I)Z
    //   44: ifne -> 57
    //   47: aload_0
    //   48: getfield handler : Landroid/os/Handler;
    //   51: bipush #107
    //   53: invokevirtual sendEmptyMessage : (I)Z
    //   56: pop
    //   57: aload_0
    //   58: monitorexit
    //   59: return
    //   60: astore_1
    //   61: aload_0
    //   62: monitorexit
    //   63: aload_1
    //   64: athrow
    // Exception table:
    //   from	to	target	type
    //   2	23	60	finally
    //   23	57	60	finally
  }
  
  public void notifyConnectVideo() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield handler : Landroid/os/Handler;
    //   6: bipush #102
    //   8: invokevirtual hasMessages : (I)Z
    //   11: ifeq -> 23
    //   14: aload_0
    //   15: getfield handler : Landroid/os/Handler;
    //   18: bipush #102
    //   20: invokevirtual removeMessages : (I)V
    //   23: aload_0
    //   24: getfield handler : Landroid/os/Handler;
    //   27: bipush #101
    //   29: invokevirtual hasMessages : (I)Z
    //   32: ifne -> 57
    //   35: aload_0
    //   36: getfield handler : Landroid/os/Handler;
    //   39: bipush #103
    //   41: invokevirtual hasMessages : (I)Z
    //   44: ifne -> 57
    //   47: aload_0
    //   48: getfield handler : Landroid/os/Handler;
    //   51: bipush #101
    //   53: invokevirtual sendEmptyMessage : (I)Z
    //   56: pop
    //   57: aload_0
    //   58: monitorexit
    //   59: return
    //   60: astore_1
    //   61: aload_0
    //   62: monitorexit
    //   63: aload_1
    //   64: athrow
    // Exception table:
    //   from	to	target	type
    //   2	23	60	finally
    //   23	57	60	finally
  }
  
  public void notifyDisconnectCMD() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield handler : Landroid/os/Handler;
    //   6: bipush #104
    //   8: invokevirtual hasMessages : (I)Z
    //   11: ifeq -> 23
    //   14: aload_0
    //   15: getfield handler : Landroid/os/Handler;
    //   18: bipush #104
    //   20: invokevirtual removeMessages : (I)V
    //   23: aload_0
    //   24: getfield handler : Landroid/os/Handler;
    //   27: bipush #106
    //   29: invokevirtual hasMessages : (I)Z
    //   32: ifeq -> 44
    //   35: aload_0
    //   36: getfield handler : Landroid/os/Handler;
    //   39: bipush #106
    //   41: invokevirtual removeMessages : (I)V
    //   44: aload_0
    //   45: getfield handler : Landroid/os/Handler;
    //   48: bipush #105
    //   50: invokevirtual hasMessages : (I)Z
    //   53: ifne -> 66
    //   56: aload_0
    //   57: getfield handler : Landroid/os/Handler;
    //   60: bipush #105
    //   62: invokevirtual sendEmptyMessage : (I)Z
    //   65: pop
    //   66: aload_0
    //   67: monitorexit
    //   68: return
    //   69: astore_1
    //   70: aload_0
    //   71: monitorexit
    //   72: aload_1
    //   73: athrow
    // Exception table:
    //   from	to	target	type
    //   2	23	69	finally
    //   23	44	69	finally
    //   44	66	69	finally
  }
  
  public void notifyDisconnectRxTx() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield handler : Landroid/os/Handler;
    //   6: bipush #107
    //   8: invokevirtual hasMessages : (I)Z
    //   11: ifeq -> 23
    //   14: aload_0
    //   15: getfield handler : Landroid/os/Handler;
    //   18: bipush #107
    //   20: invokevirtual removeMessages : (I)V
    //   23: aload_0
    //   24: getfield handler : Landroid/os/Handler;
    //   27: bipush #109
    //   29: invokevirtual hasMessages : (I)Z
    //   32: ifeq -> 44
    //   35: aload_0
    //   36: getfield handler : Landroid/os/Handler;
    //   39: bipush #109
    //   41: invokevirtual removeMessages : (I)V
    //   44: aload_0
    //   45: getfield handler : Landroid/os/Handler;
    //   48: bipush #108
    //   50: invokevirtual hasMessages : (I)Z
    //   53: ifne -> 66
    //   56: aload_0
    //   57: getfield handler : Landroid/os/Handler;
    //   60: bipush #108
    //   62: invokevirtual sendEmptyMessage : (I)Z
    //   65: pop
    //   66: aload_0
    //   67: monitorexit
    //   68: return
    //   69: astore_1
    //   70: aload_0
    //   71: monitorexit
    //   72: aload_1
    //   73: athrow
    // Exception table:
    //   from	to	target	type
    //   2	23	69	finally
    //   23	44	69	finally
    //   44	66	69	finally
  }
  
  public void notifyDisconnectVideo() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield handler : Landroid/os/Handler;
    //   6: bipush #101
    //   8: invokevirtual hasMessages : (I)Z
    //   11: ifeq -> 23
    //   14: aload_0
    //   15: getfield handler : Landroid/os/Handler;
    //   18: bipush #101
    //   20: invokevirtual removeMessages : (I)V
    //   23: aload_0
    //   24: getfield handler : Landroid/os/Handler;
    //   27: bipush #103
    //   29: invokevirtual hasMessages : (I)Z
    //   32: ifeq -> 44
    //   35: aload_0
    //   36: getfield handler : Landroid/os/Handler;
    //   39: bipush #103
    //   41: invokevirtual removeMessages : (I)V
    //   44: aload_0
    //   45: getfield handler : Landroid/os/Handler;
    //   48: bipush #102
    //   50: invokevirtual hasMessages : (I)Z
    //   53: ifne -> 66
    //   56: aload_0
    //   57: getfield handler : Landroid/os/Handler;
    //   60: bipush #102
    //   62: invokevirtual sendEmptyMessage : (I)Z
    //   65: pop
    //   66: aload_0
    //   67: monitorexit
    //   68: return
    //   69: astore_1
    //   70: aload_0
    //   71: monitorexit
    //   72: aload_1
    //   73: athrow
    // Exception table:
    //   from	to	target	type
    //   2	23	69	finally
    //   23	44	69	finally
    //   44	66	69	finally
  }
  
  public void notifyReconnectCMD() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield handler : Landroid/os/Handler;
    //   6: bipush #104
    //   8: invokevirtual hasMessages : (I)Z
    //   11: ifeq -> 23
    //   14: aload_0
    //   15: getfield handler : Landroid/os/Handler;
    //   18: bipush #104
    //   20: invokevirtual removeMessages : (I)V
    //   23: aload_0
    //   24: getfield handler : Landroid/os/Handler;
    //   27: bipush #105
    //   29: invokevirtual hasMessages : (I)Z
    //   32: ifeq -> 44
    //   35: aload_0
    //   36: getfield handler : Landroid/os/Handler;
    //   39: bipush #105
    //   41: invokevirtual removeMessages : (I)V
    //   44: aload_0
    //   45: getfield handler : Landroid/os/Handler;
    //   48: bipush #106
    //   50: invokevirtual hasMessages : (I)Z
    //   53: ifne -> 66
    //   56: aload_0
    //   57: getfield handler : Landroid/os/Handler;
    //   60: bipush #106
    //   62: invokevirtual sendEmptyMessage : (I)Z
    //   65: pop
    //   66: aload_0
    //   67: monitorexit
    //   68: return
    //   69: astore_1
    //   70: aload_0
    //   71: monitorexit
    //   72: aload_1
    //   73: athrow
    // Exception table:
    //   from	to	target	type
    //   2	23	69	finally
    //   23	44	69	finally
    //   44	66	69	finally
  }
  
  public void notifyReconnectRxTx() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield handler : Landroid/os/Handler;
    //   6: bipush #107
    //   8: invokevirtual hasMessages : (I)Z
    //   11: ifeq -> 23
    //   14: aload_0
    //   15: getfield handler : Landroid/os/Handler;
    //   18: bipush #107
    //   20: invokevirtual removeMessages : (I)V
    //   23: aload_0
    //   24: getfield handler : Landroid/os/Handler;
    //   27: bipush #108
    //   29: invokevirtual hasMessages : (I)Z
    //   32: ifeq -> 44
    //   35: aload_0
    //   36: getfield handler : Landroid/os/Handler;
    //   39: bipush #108
    //   41: invokevirtual removeMessages : (I)V
    //   44: aload_0
    //   45: getfield handler : Landroid/os/Handler;
    //   48: bipush #109
    //   50: invokevirtual hasMessages : (I)Z
    //   53: ifne -> 66
    //   56: aload_0
    //   57: getfield handler : Landroid/os/Handler;
    //   60: bipush #109
    //   62: invokevirtual sendEmptyMessage : (I)Z
    //   65: pop
    //   66: aload_0
    //   67: monitorexit
    //   68: return
    //   69: astore_1
    //   70: aload_0
    //   71: monitorexit
    //   72: aload_1
    //   73: athrow
    // Exception table:
    //   from	to	target	type
    //   2	23	69	finally
    //   23	44	69	finally
    //   44	66	69	finally
  }
  
  public void notifyReconnectVideo() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield handler : Landroid/os/Handler;
    //   6: bipush #101
    //   8: invokevirtual hasMessages : (I)Z
    //   11: ifeq -> 23
    //   14: aload_0
    //   15: getfield handler : Landroid/os/Handler;
    //   18: bipush #101
    //   20: invokevirtual removeMessages : (I)V
    //   23: aload_0
    //   24: getfield handler : Landroid/os/Handler;
    //   27: bipush #102
    //   29: invokevirtual hasMessages : (I)Z
    //   32: ifeq -> 44
    //   35: aload_0
    //   36: getfield handler : Landroid/os/Handler;
    //   39: bipush #102
    //   41: invokevirtual removeMessages : (I)V
    //   44: aload_0
    //   45: getfield handler : Landroid/os/Handler;
    //   48: bipush #103
    //   50: invokevirtual hasMessages : (I)Z
    //   53: ifne -> 66
    //   56: aload_0
    //   57: getfield handler : Landroid/os/Handler;
    //   60: bipush #103
    //   62: invokevirtual sendEmptyMessage : (I)Z
    //   65: pop
    //   66: aload_0
    //   67: monitorexit
    //   68: return
    //   69: astore_1
    //   70: aload_0
    //   71: monitorexit
    //   72: aload_1
    //   73: athrow
    // Exception table:
    //   from	to	target	type
    //   2	23	69	finally
    //   23	44	69	finally
    //   44	66	69	finally
  }
  
  public void release() {
    this.handler.getLooper().quitSafely();
    this.isRelease = true;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/deviceshub/base/ConnectHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */