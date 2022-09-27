package com.google.gson.internal.bind.util;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class ISO8601Utils {
  private static final TimeZone TIMEZONE_UTC = TimeZone.getTimeZone("UTC");
  
  private static final String UTC_ID = "UTC";
  
  private static boolean checkOffset(String paramString, int paramInt, char paramChar) {
    boolean bool;
    if (paramInt < paramString.length() && paramString.charAt(paramInt) == paramChar) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public static String format(Date paramDate) {
    return format(paramDate, false, TIMEZONE_UTC);
  }
  
  public static String format(Date paramDate, boolean paramBoolean) {
    return format(paramDate, paramBoolean, TIMEZONE_UTC);
  }
  
  public static String format(Date paramDate, boolean paramBoolean, TimeZone paramTimeZone) {
    int i;
    int j;
    GregorianCalendar gregorianCalendar = new GregorianCalendar(paramTimeZone, Locale.US);
    gregorianCalendar.setTime(paramDate);
    if (paramBoolean) {
      i = 4;
    } else {
      i = 0;
    } 
    if (paramTimeZone.getRawOffset() == 0) {
      j = 1;
    } else {
      j = 6;
    } 
    StringBuilder stringBuilder = new StringBuilder(19 + i + j);
    padInt(stringBuilder, gregorianCalendar.get(1), 4);
    byte b = 45;
    stringBuilder.append('-');
    padInt(stringBuilder, gregorianCalendar.get(2) + 1, 2);
    stringBuilder.append('-');
    padInt(stringBuilder, gregorianCalendar.get(5), 2);
    stringBuilder.append('T');
    padInt(stringBuilder, gregorianCalendar.get(11), 2);
    stringBuilder.append(':');
    padInt(stringBuilder, gregorianCalendar.get(12), 2);
    stringBuilder.append(':');
    padInt(stringBuilder, gregorianCalendar.get(13), 2);
    if (paramBoolean) {
      stringBuilder.append('.');
      padInt(stringBuilder, gregorianCalendar.get(14), 3);
    } 
    int k = paramTimeZone.getOffset(gregorianCalendar.getTimeInMillis());
    if (k != 0) {
      byte b1;
      j = k / 60000;
      i = Math.abs(j / 60);
      j = Math.abs(j % 60);
      if (k < 0) {
        b1 = b;
      } else {
        b = 43;
        b1 = b;
      } 
      stringBuilder.append(b1);
      padInt(stringBuilder, i, 2);
      stringBuilder.append(':');
      padInt(stringBuilder, j, 2);
    } else {
      stringBuilder.append('Z');
    } 
    return stringBuilder.toString();
  }
  
  private static int indexOfNonDigit(String paramString, int paramInt) {
    while (paramInt < paramString.length()) {
      char c = paramString.charAt(paramInt);
      if (c < '0' || c > '9')
        return paramInt; 
      paramInt++;
    } 
    return paramString.length();
  }
  
  private static void padInt(StringBuilder paramStringBuilder, int paramInt1, int paramInt2) {
    String str = Integer.toString(paramInt1);
    for (paramInt1 = paramInt2 - str.length(); paramInt1 > 0; paramInt1--)
      paramStringBuilder.append('0'); 
    paramStringBuilder.append(str);
  }
  
  public static Date parse(String paramString, ParsePosition paramParsePosition) throws ParseException {
    // Byte code:
    //   0: aload_1
    //   1: invokevirtual getIndex : ()I
    //   4: istore_2
    //   5: iload_2
    //   6: iconst_4
    //   7: iadd
    //   8: istore_3
    //   9: aload_0
    //   10: iload_2
    //   11: iload_3
    //   12: invokestatic parseInt : (Ljava/lang/String;II)I
    //   15: istore #4
    //   17: iload_3
    //   18: istore_2
    //   19: aload_0
    //   20: iload_3
    //   21: bipush #45
    //   23: invokestatic checkOffset : (Ljava/lang/String;IC)Z
    //   26: ifeq -> 33
    //   29: iload_3
    //   30: iconst_1
    //   31: iadd
    //   32: istore_2
    //   33: iload_2
    //   34: iconst_2
    //   35: iadd
    //   36: istore_3
    //   37: aload_0
    //   38: iload_2
    //   39: iload_3
    //   40: invokestatic parseInt : (Ljava/lang/String;II)I
    //   43: istore #5
    //   45: iload_3
    //   46: istore_2
    //   47: aload_0
    //   48: iload_3
    //   49: bipush #45
    //   51: invokestatic checkOffset : (Ljava/lang/String;IC)Z
    //   54: ifeq -> 61
    //   57: iload_3
    //   58: iconst_1
    //   59: iadd
    //   60: istore_2
    //   61: iload_2
    //   62: iconst_2
    //   63: iadd
    //   64: istore #6
    //   66: aload_0
    //   67: iload_2
    //   68: iload #6
    //   70: invokestatic parseInt : (Ljava/lang/String;II)I
    //   73: istore #7
    //   75: aload_0
    //   76: iload #6
    //   78: bipush #84
    //   80: invokestatic checkOffset : (Ljava/lang/String;IC)Z
    //   83: istore #8
    //   85: iload #8
    //   87: ifne -> 129
    //   90: aload_0
    //   91: invokevirtual length : ()I
    //   94: iload #6
    //   96: if_icmpgt -> 129
    //   99: new java/util/GregorianCalendar
    //   102: astore #9
    //   104: aload #9
    //   106: iload #4
    //   108: iload #5
    //   110: iconst_1
    //   111: isub
    //   112: iload #7
    //   114: invokespecial <init> : (III)V
    //   117: aload_1
    //   118: iload #6
    //   120: invokevirtual setIndex : (I)V
    //   123: aload #9
    //   125: invokevirtual getTime : ()Ljava/util/Date;
    //   128: areturn
    //   129: iload #8
    //   131: ifeq -> 412
    //   134: iload #6
    //   136: iconst_1
    //   137: iadd
    //   138: istore_2
    //   139: iload_2
    //   140: iconst_2
    //   141: iadd
    //   142: istore_3
    //   143: aload_0
    //   144: iload_2
    //   145: iload_3
    //   146: invokestatic parseInt : (Ljava/lang/String;II)I
    //   149: istore #10
    //   151: iload_3
    //   152: istore_2
    //   153: aload_0
    //   154: iload_3
    //   155: bipush #58
    //   157: invokestatic checkOffset : (Ljava/lang/String;IC)Z
    //   160: ifeq -> 167
    //   163: iload_3
    //   164: iconst_1
    //   165: iadd
    //   166: istore_2
    //   167: iload_2
    //   168: iconst_2
    //   169: iadd
    //   170: istore_3
    //   171: aload_0
    //   172: iload_2
    //   173: iload_3
    //   174: invokestatic parseInt : (Ljava/lang/String;II)I
    //   177: istore #11
    //   179: iload_3
    //   180: istore #12
    //   182: aload_0
    //   183: iload_3
    //   184: bipush #58
    //   186: invokestatic checkOffset : (Ljava/lang/String;IC)Z
    //   189: ifeq -> 197
    //   192: iload_3
    //   193: iconst_1
    //   194: iadd
    //   195: istore #12
    //   197: iload #10
    //   199: istore_2
    //   200: iload #11
    //   202: istore_3
    //   203: iload #12
    //   205: istore #6
    //   207: aload_0
    //   208: invokevirtual length : ()I
    //   211: iload #12
    //   213: if_icmple -> 416
    //   216: aload_0
    //   217: iload #12
    //   219: invokevirtual charAt : (I)C
    //   222: istore #13
    //   224: iload #10
    //   226: istore_2
    //   227: iload #11
    //   229: istore_3
    //   230: iload #12
    //   232: istore #6
    //   234: iload #13
    //   236: bipush #90
    //   238: if_icmpeq -> 416
    //   241: iload #10
    //   243: istore_2
    //   244: iload #11
    //   246: istore_3
    //   247: iload #12
    //   249: istore #6
    //   251: iload #13
    //   253: bipush #43
    //   255: if_icmpeq -> 416
    //   258: iload #10
    //   260: istore_2
    //   261: iload #11
    //   263: istore_3
    //   264: iload #12
    //   266: istore #6
    //   268: iload #13
    //   270: bipush #45
    //   272: if_icmpeq -> 416
    //   275: iload #12
    //   277: iconst_2
    //   278: iadd
    //   279: istore_3
    //   280: aload_0
    //   281: iload #12
    //   283: iload_3
    //   284: invokestatic parseInt : (Ljava/lang/String;II)I
    //   287: istore #6
    //   289: iload #6
    //   291: istore_2
    //   292: iload #6
    //   294: bipush #59
    //   296: if_icmple -> 312
    //   299: iload #6
    //   301: istore_2
    //   302: iload #6
    //   304: bipush #63
    //   306: if_icmpge -> 312
    //   309: bipush #59
    //   311: istore_2
    //   312: aload_0
    //   313: iload_3
    //   314: bipush #46
    //   316: invokestatic checkOffset : (Ljava/lang/String;IC)Z
    //   319: ifeq -> 403
    //   322: iload_3
    //   323: iconst_1
    //   324: iadd
    //   325: istore #13
    //   327: aload_0
    //   328: iload #13
    //   330: iconst_1
    //   331: iadd
    //   332: invokestatic indexOfNonDigit : (Ljava/lang/String;I)I
    //   335: istore #12
    //   337: iload #12
    //   339: iload #13
    //   341: iconst_3
    //   342: iadd
    //   343: invokestatic min : (II)I
    //   346: istore #6
    //   348: aload_0
    //   349: iload #13
    //   351: iload #6
    //   353: invokestatic parseInt : (Ljava/lang/String;II)I
    //   356: istore_3
    //   357: iload #6
    //   359: iload #13
    //   361: isub
    //   362: istore #6
    //   364: iload #6
    //   366: iconst_1
    //   367: if_icmpeq -> 387
    //   370: iload #6
    //   372: iconst_2
    //   373: if_icmpeq -> 379
    //   376: goto -> 395
    //   379: iload_3
    //   380: bipush #10
    //   382: imul
    //   383: istore_3
    //   384: goto -> 376
    //   387: iload_3
    //   388: bipush #100
    //   390: imul
    //   391: istore_3
    //   392: goto -> 376
    //   395: iload_2
    //   396: istore #6
    //   398: iload_3
    //   399: istore_2
    //   400: goto -> 435
    //   403: iload_3
    //   404: istore #12
    //   406: iload_2
    //   407: istore #6
    //   409: goto -> 433
    //   412: iconst_0
    //   413: istore_2
    //   414: iconst_0
    //   415: istore_3
    //   416: iconst_0
    //   417: istore #10
    //   419: iload #6
    //   421: istore #12
    //   423: iload #10
    //   425: istore #6
    //   427: iload_3
    //   428: istore #11
    //   430: iload_2
    //   431: istore #10
    //   433: iconst_0
    //   434: istore_2
    //   435: aload_0
    //   436: invokevirtual length : ()I
    //   439: iload #12
    //   441: if_icmple -> 864
    //   444: aload_0
    //   445: iload #12
    //   447: invokevirtual charAt : (I)C
    //   450: istore #14
    //   452: iload #14
    //   454: bipush #90
    //   456: if_icmpne -> 472
    //   459: getstatic com/google/gson/internal/bind/util/ISO8601Utils.TIMEZONE_UTC : Ljava/util/TimeZone;
    //   462: astore #9
    //   464: iload #12
    //   466: iconst_1
    //   467: iadd
    //   468: istore_3
    //   469: goto -> 774
    //   472: iload #14
    //   474: bipush #43
    //   476: if_icmpeq -> 541
    //   479: iload #14
    //   481: bipush #45
    //   483: if_icmpne -> 489
    //   486: goto -> 541
    //   489: new java/lang/IndexOutOfBoundsException
    //   492: astore #15
    //   494: new java/lang/StringBuilder
    //   497: astore #9
    //   499: aload #9
    //   501: invokespecial <init> : ()V
    //   504: aload #9
    //   506: ldc 'Invalid time zone indicator ''
    //   508: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   511: pop
    //   512: aload #9
    //   514: iload #14
    //   516: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   519: pop
    //   520: aload #9
    //   522: ldc '''
    //   524: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   527: pop
    //   528: aload #15
    //   530: aload #9
    //   532: invokevirtual toString : ()Ljava/lang/String;
    //   535: invokespecial <init> : (Ljava/lang/String;)V
    //   538: aload #15
    //   540: athrow
    //   541: aload_0
    //   542: iload #12
    //   544: invokevirtual substring : (I)Ljava/lang/String;
    //   547: astore #9
    //   549: aload #9
    //   551: invokevirtual length : ()I
    //   554: iconst_5
    //   555: if_icmplt -> 561
    //   558: goto -> 594
    //   561: new java/lang/StringBuilder
    //   564: astore #15
    //   566: aload #15
    //   568: invokespecial <init> : ()V
    //   571: aload #15
    //   573: aload #9
    //   575: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   578: pop
    //   579: aload #15
    //   581: ldc '00'
    //   583: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   586: pop
    //   587: aload #15
    //   589: invokevirtual toString : ()Ljava/lang/String;
    //   592: astore #9
    //   594: iload #12
    //   596: aload #9
    //   598: invokevirtual length : ()I
    //   601: iadd
    //   602: istore_3
    //   603: ldc '+0000'
    //   605: aload #9
    //   607: invokevirtual equals : (Ljava/lang/Object;)Z
    //   610: ifne -> 769
    //   613: ldc '+00:00'
    //   615: aload #9
    //   617: invokevirtual equals : (Ljava/lang/Object;)Z
    //   620: ifeq -> 626
    //   623: goto -> 769
    //   626: new java/lang/StringBuilder
    //   629: astore #15
    //   631: aload #15
    //   633: invokespecial <init> : ()V
    //   636: aload #15
    //   638: ldc 'GMT'
    //   640: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   643: pop
    //   644: aload #15
    //   646: aload #9
    //   648: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   651: pop
    //   652: aload #15
    //   654: invokevirtual toString : ()Ljava/lang/String;
    //   657: astore #15
    //   659: aload #15
    //   661: invokestatic getTimeZone : (Ljava/lang/String;)Ljava/util/TimeZone;
    //   664: astore #9
    //   666: aload #9
    //   668: invokevirtual getID : ()Ljava/lang/String;
    //   671: astore #16
    //   673: aload #16
    //   675: aload #15
    //   677: invokevirtual equals : (Ljava/lang/Object;)Z
    //   680: ifne -> 766
    //   683: aload #16
    //   685: ldc ':'
    //   687: ldc ''
    //   689: invokevirtual replace : (Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;
    //   692: aload #15
    //   694: invokevirtual equals : (Ljava/lang/Object;)Z
    //   697: ifeq -> 703
    //   700: goto -> 766
    //   703: new java/lang/IndexOutOfBoundsException
    //   706: astore #16
    //   708: new java/lang/StringBuilder
    //   711: astore #17
    //   713: aload #17
    //   715: invokespecial <init> : ()V
    //   718: aload #17
    //   720: ldc 'Mismatching time zone indicator: '
    //   722: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   725: pop
    //   726: aload #17
    //   728: aload #15
    //   730: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   733: pop
    //   734: aload #17
    //   736: ldc ' given, resolves to '
    //   738: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   741: pop
    //   742: aload #17
    //   744: aload #9
    //   746: invokevirtual getID : ()Ljava/lang/String;
    //   749: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   752: pop
    //   753: aload #16
    //   755: aload #17
    //   757: invokevirtual toString : ()Ljava/lang/String;
    //   760: invokespecial <init> : (Ljava/lang/String;)V
    //   763: aload #16
    //   765: athrow
    //   766: goto -> 774
    //   769: getstatic com/google/gson/internal/bind/util/ISO8601Utils.TIMEZONE_UTC : Ljava/util/TimeZone;
    //   772: astore #9
    //   774: new java/util/GregorianCalendar
    //   777: astore #15
    //   779: aload #15
    //   781: aload #9
    //   783: invokespecial <init> : (Ljava/util/TimeZone;)V
    //   786: aload #15
    //   788: iconst_0
    //   789: invokevirtual setLenient : (Z)V
    //   792: aload #15
    //   794: iconst_1
    //   795: iload #4
    //   797: invokevirtual set : (II)V
    //   800: aload #15
    //   802: iconst_2
    //   803: iload #5
    //   805: iconst_1
    //   806: isub
    //   807: invokevirtual set : (II)V
    //   810: aload #15
    //   812: iconst_5
    //   813: iload #7
    //   815: invokevirtual set : (II)V
    //   818: aload #15
    //   820: bipush #11
    //   822: iload #10
    //   824: invokevirtual set : (II)V
    //   827: aload #15
    //   829: bipush #12
    //   831: iload #11
    //   833: invokevirtual set : (II)V
    //   836: aload #15
    //   838: bipush #13
    //   840: iload #6
    //   842: invokevirtual set : (II)V
    //   845: aload #15
    //   847: bipush #14
    //   849: iload_2
    //   850: invokevirtual set : (II)V
    //   853: aload_1
    //   854: iload_3
    //   855: invokevirtual setIndex : (I)V
    //   858: aload #15
    //   860: invokevirtual getTime : ()Ljava/util/Date;
    //   863: areturn
    //   864: new java/lang/IllegalArgumentException
    //   867: astore #9
    //   869: aload #9
    //   871: ldc 'No time zone indicator'
    //   873: invokespecial <init> : (Ljava/lang/String;)V
    //   876: aload #9
    //   878: athrow
    //   879: astore #9
    //   881: goto -> 891
    //   884: astore #9
    //   886: goto -> 891
    //   889: astore #9
    //   891: aload_0
    //   892: ifnonnull -> 900
    //   895: aconst_null
    //   896: astore_0
    //   897: goto -> 938
    //   900: new java/lang/StringBuilder
    //   903: dup
    //   904: invokespecial <init> : ()V
    //   907: astore #15
    //   909: aload #15
    //   911: bipush #34
    //   913: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   916: pop
    //   917: aload #15
    //   919: aload_0
    //   920: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   923: pop
    //   924: aload #15
    //   926: bipush #34
    //   928: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   931: pop
    //   932: aload #15
    //   934: invokevirtual toString : ()Ljava/lang/String;
    //   937: astore_0
    //   938: aload #9
    //   940: invokevirtual getMessage : ()Ljava/lang/String;
    //   943: astore #16
    //   945: aload #16
    //   947: ifnull -> 962
    //   950: aload #16
    //   952: astore #15
    //   954: aload #16
    //   956: invokevirtual isEmpty : ()Z
    //   959: ifeq -> 1008
    //   962: new java/lang/StringBuilder
    //   965: dup
    //   966: invokespecial <init> : ()V
    //   969: astore #15
    //   971: aload #15
    //   973: ldc '('
    //   975: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   978: pop
    //   979: aload #15
    //   981: aload #9
    //   983: invokevirtual getClass : ()Ljava/lang/Class;
    //   986: invokevirtual getName : ()Ljava/lang/String;
    //   989: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   992: pop
    //   993: aload #15
    //   995: ldc ')'
    //   997: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1000: pop
    //   1001: aload #15
    //   1003: invokevirtual toString : ()Ljava/lang/String;
    //   1006: astore #15
    //   1008: new java/lang/StringBuilder
    //   1011: dup
    //   1012: invokespecial <init> : ()V
    //   1015: astore #16
    //   1017: aload #16
    //   1019: ldc 'Failed to parse date ['
    //   1021: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1024: pop
    //   1025: aload #16
    //   1027: aload_0
    //   1028: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1031: pop
    //   1032: aload #16
    //   1034: ldc ']: '
    //   1036: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1039: pop
    //   1040: aload #16
    //   1042: aload #15
    //   1044: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1047: pop
    //   1048: new java/text/ParseException
    //   1051: dup
    //   1052: aload #16
    //   1054: invokevirtual toString : ()Ljava/lang/String;
    //   1057: aload_1
    //   1058: invokevirtual getIndex : ()I
    //   1061: invokespecial <init> : (Ljava/lang/String;I)V
    //   1064: astore_0
    //   1065: aload_0
    //   1066: aload #9
    //   1068: invokevirtual initCause : (Ljava/lang/Throwable;)Ljava/lang/Throwable;
    //   1071: pop
    //   1072: goto -> 1077
    //   1075: aload_0
    //   1076: athrow
    //   1077: goto -> 1075
    // Exception table:
    //   from	to	target	type
    //   0	5	889	java/lang/IndexOutOfBoundsException
    //   0	5	884	java/lang/NumberFormatException
    //   0	5	879	java/lang/IllegalArgumentException
    //   9	17	889	java/lang/IndexOutOfBoundsException
    //   9	17	884	java/lang/NumberFormatException
    //   9	17	879	java/lang/IllegalArgumentException
    //   19	29	889	java/lang/IndexOutOfBoundsException
    //   19	29	884	java/lang/NumberFormatException
    //   19	29	879	java/lang/IllegalArgumentException
    //   37	45	889	java/lang/IndexOutOfBoundsException
    //   37	45	884	java/lang/NumberFormatException
    //   37	45	879	java/lang/IllegalArgumentException
    //   47	57	889	java/lang/IndexOutOfBoundsException
    //   47	57	884	java/lang/NumberFormatException
    //   47	57	879	java/lang/IllegalArgumentException
    //   66	85	889	java/lang/IndexOutOfBoundsException
    //   66	85	884	java/lang/NumberFormatException
    //   66	85	879	java/lang/IllegalArgumentException
    //   90	129	889	java/lang/IndexOutOfBoundsException
    //   90	129	884	java/lang/NumberFormatException
    //   90	129	879	java/lang/IllegalArgumentException
    //   143	151	889	java/lang/IndexOutOfBoundsException
    //   143	151	884	java/lang/NumberFormatException
    //   143	151	879	java/lang/IllegalArgumentException
    //   153	163	889	java/lang/IndexOutOfBoundsException
    //   153	163	884	java/lang/NumberFormatException
    //   153	163	879	java/lang/IllegalArgumentException
    //   171	179	889	java/lang/IndexOutOfBoundsException
    //   171	179	884	java/lang/NumberFormatException
    //   171	179	879	java/lang/IllegalArgumentException
    //   182	192	889	java/lang/IndexOutOfBoundsException
    //   182	192	884	java/lang/NumberFormatException
    //   182	192	879	java/lang/IllegalArgumentException
    //   207	224	889	java/lang/IndexOutOfBoundsException
    //   207	224	884	java/lang/NumberFormatException
    //   207	224	879	java/lang/IllegalArgumentException
    //   280	289	889	java/lang/IndexOutOfBoundsException
    //   280	289	884	java/lang/NumberFormatException
    //   280	289	879	java/lang/IllegalArgumentException
    //   312	322	889	java/lang/IndexOutOfBoundsException
    //   312	322	884	java/lang/NumberFormatException
    //   312	322	879	java/lang/IllegalArgumentException
    //   327	357	889	java/lang/IndexOutOfBoundsException
    //   327	357	884	java/lang/NumberFormatException
    //   327	357	879	java/lang/IllegalArgumentException
    //   435	452	889	java/lang/IndexOutOfBoundsException
    //   435	452	884	java/lang/NumberFormatException
    //   435	452	879	java/lang/IllegalArgumentException
    //   459	464	889	java/lang/IndexOutOfBoundsException
    //   459	464	884	java/lang/NumberFormatException
    //   459	464	879	java/lang/IllegalArgumentException
    //   489	541	889	java/lang/IndexOutOfBoundsException
    //   489	541	884	java/lang/NumberFormatException
    //   489	541	879	java/lang/IllegalArgumentException
    //   541	558	889	java/lang/IndexOutOfBoundsException
    //   541	558	884	java/lang/NumberFormatException
    //   541	558	879	java/lang/IllegalArgumentException
    //   561	594	889	java/lang/IndexOutOfBoundsException
    //   561	594	884	java/lang/NumberFormatException
    //   561	594	879	java/lang/IllegalArgumentException
    //   594	623	889	java/lang/IndexOutOfBoundsException
    //   594	623	884	java/lang/NumberFormatException
    //   594	623	879	java/lang/IllegalArgumentException
    //   626	700	889	java/lang/IndexOutOfBoundsException
    //   626	700	884	java/lang/NumberFormatException
    //   626	700	879	java/lang/IllegalArgumentException
    //   703	766	889	java/lang/IndexOutOfBoundsException
    //   703	766	884	java/lang/NumberFormatException
    //   703	766	879	java/lang/IllegalArgumentException
    //   769	774	889	java/lang/IndexOutOfBoundsException
    //   769	774	884	java/lang/NumberFormatException
    //   769	774	879	java/lang/IllegalArgumentException
    //   774	864	889	java/lang/IndexOutOfBoundsException
    //   774	864	884	java/lang/NumberFormatException
    //   774	864	879	java/lang/IllegalArgumentException
    //   864	879	889	java/lang/IndexOutOfBoundsException
    //   864	879	884	java/lang/NumberFormatException
    //   864	879	879	java/lang/IllegalArgumentException
  }
  
  private static int parseInt(String paramString, int paramInt1, int paramInt2) throws NumberFormatException {
    if (paramInt1 >= 0 && paramInt2 <= paramString.length() && paramInt1 <= paramInt2) {
      int i;
      int j;
      if (paramInt1 < paramInt2) {
        i = paramInt1 + 1;
        j = Character.digit(paramString.charAt(paramInt1), 10);
        if (j >= 0) {
          j = -j;
        } else {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("Invalid number: ");
          stringBuilder.append(paramString.substring(paramInt1, paramInt2));
          throw new NumberFormatException(stringBuilder.toString());
        } 
      } else {
        i = paramInt1;
        j = 0;
      } 
      while (i < paramInt2) {
        int k = Character.digit(paramString.charAt(i), 10);
        if (k >= 0) {
          j = j * 10 - k;
          i++;
          continue;
        } 
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Invalid number: ");
        stringBuilder.append(paramString.substring(paramInt1, paramInt2));
        throw new NumberFormatException(stringBuilder.toString());
      } 
      return -j;
    } 
    NumberFormatException numberFormatException = new NumberFormatException(paramString);
    throw numberFormatException;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/google/gson/internal/bind/util/ISO8601Utils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */