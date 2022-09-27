package com.dd.plist;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.zip.GZIPOutputStream;

public class Base64 {
  public static final int DECODE = 0;
  
  public static final int DONT_GUNZIP = 4;
  
  public static final int DO_BREAK_LINES = 8;
  
  public static final int ENCODE = 1;
  
  private static final byte EQUALS_SIGN = 61;
  
  private static final byte EQUALS_SIGN_ENC = -1;
  
  public static final int GZIP = 2;
  
  private static final int MAX_LINE_LENGTH = 76;
  
  private static final byte NEW_LINE = 10;
  
  public static final int NO_OPTIONS = 0;
  
  public static final int ORDERED = 32;
  
  private static final String PREFERRED_ENCODING = "US-ASCII";
  
  public static final int URL_SAFE = 16;
  
  private static final byte WHITE_SPACE_ENC = -5;
  
  private static final byte[] _ORDERED_ALPHABET;
  
  private static final byte[] _ORDERED_DECODABET;
  
  private static final byte[] _STANDARD_ALPHABET = new byte[] { 
      65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 
      75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 
      85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 
      101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 
      111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 
      121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 
      56, 57, 43, 47 };
  
  private static final byte[] _STANDARD_DECODABET = new byte[] { 
      -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, 
      -5, -9, -9, -5, -9, -9, -9, -9, -9, -9, 
      -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
      -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, 
      -9, -9, -9, 62, -9, -9, -9, 63, 52, 53, 
      54, 55, 56, 57, 58, 59, 60, 61, -9, -9, 
      -9, -1, -9, -9, -9, 0, 1, 2, 3, 4, 
      5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 
      15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 
      25, -9, -9, -9, -9, -9, -9, 26, 27, 28, 
      29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 
      39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 
      49, 50, 51, -9, -9, -9, -9, -9, -9, -9, 
      -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
      -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
      -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
      -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
      -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
      -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
      -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
      -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
      -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
      -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
      -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
      -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
      -9, -9, -9, -9, -9, -9 };
  
  private static final byte[] _URL_SAFE_ALPHABET = new byte[] { 
      65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 
      75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 
      85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 
      101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 
      111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 
      121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 
      56, 57, 45, 95 };
  
  private static final byte[] _URL_SAFE_DECODABET = new byte[] { 
      -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, 
      -5, -9, -9, -5, -9, -9, -9, -9, -9, -9, 
      -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
      -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, 
      -9, -9, -9, -9, -9, 62, -9, -9, 52, 53, 
      54, 55, 56, 57, 58, 59, 60, 61, -9, -9, 
      -9, -1, -9, -9, -9, 0, 1, 2, 3, 4, 
      5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 
      15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 
      25, -9, -9, -9, -9, 63, -9, 26, 27, 28, 
      29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 
      39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 
      49, 50, 51, -9, -9, -9, -9, -9, -9, -9, 
      -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
      -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
      -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
      -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
      -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
      -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
      -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
      -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
      -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
      -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
      -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
      -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
      -9, -9, -9, -9, -9, -9 };
  
  static {
    _ORDERED_ALPHABET = new byte[] { 
        45, 48, 49, 50, 51, 52, 53, 54, 55, 56, 
        57, 65, 66, 67, 68, 69, 70, 71, 72, 73, 
        74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 
        84, 85, 86, 87, 88, 89, 90, 95, 97, 98, 
        99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 
        109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 
        119, 120, 121, 122 };
    _ORDERED_DECODABET = new byte[] { 
        -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, 
        -5, -9, -9, -5, -9, -9, -9, -9, -9, -9, 
        -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
        -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, 
        -9, -9, -9, -9, -9, 0, -9, -9, 1, 2, 
        3, 4, 5, 6, 7, 8, 9, 10, -9, -9, 
        -9, -1, -9, -9, -9, 11, 12, 13, 14, 15, 
        16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 
        26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 
        36, -9, -9, -9, -9, 37, -9, 38, 39, 40, 
        41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 
        51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 
        61, 62, 63, -9, -9, -9, -9, -9, -9, -9, 
        -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
        -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
        -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
        -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
        -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
        -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
        -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
        -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
        -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
        -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
        -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
        -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
        -9, -9, -9, -9, -9, -9, -9 };
  }
  
  public static byte[] decode(String paramString) throws IOException {
    return decode(paramString, 0);
  }
  
  public static byte[] decode(String paramString, int paramInt) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: ifnull -> 331
    //   4: aload_0
    //   5: ldc 'US-ASCII'
    //   7: invokevirtual getBytes : (Ljava/lang/String;)[B
    //   10: astore_2
    //   11: aload_2
    //   12: astore_0
    //   13: goto -> 22
    //   16: astore_2
    //   17: aload_0
    //   18: invokevirtual getBytes : ()[B
    //   21: astore_0
    //   22: aload_0
    //   23: iconst_0
    //   24: aload_0
    //   25: arraylength
    //   26: iload_1
    //   27: invokestatic decode : ([BIII)[B
    //   30: astore_3
    //   31: iload_1
    //   32: iconst_4
    //   33: iand
    //   34: ifeq -> 42
    //   37: iconst_1
    //   38: istore_1
    //   39: goto -> 44
    //   42: iconst_0
    //   43: istore_1
    //   44: aload_3
    //   45: astore #4
    //   47: aload_3
    //   48: ifnull -> 328
    //   51: aload_3
    //   52: astore #4
    //   54: aload_3
    //   55: arraylength
    //   56: iconst_4
    //   57: if_icmplt -> 328
    //   60: aload_3
    //   61: astore #4
    //   63: iload_1
    //   64: ifne -> 328
    //   67: aload_3
    //   68: astore #4
    //   70: ldc 35615
    //   72: aload_3
    //   73: iconst_0
    //   74: baload
    //   75: sipush #255
    //   78: iand
    //   79: aload_3
    //   80: iconst_1
    //   81: baload
    //   82: bipush #8
    //   84: ishl
    //   85: ldc 65280
    //   87: iand
    //   88: ior
    //   89: if_icmpne -> 328
    //   92: sipush #2048
    //   95: newarray byte
    //   97: astore #5
    //   99: aconst_null
    //   100: astore #4
    //   102: aconst_null
    //   103: astore #6
    //   105: new java/io/ByteArrayOutputStream
    //   108: astore #7
    //   110: aload #7
    //   112: invokespecial <init> : ()V
    //   115: new java/io/ByteArrayInputStream
    //   118: astore_0
    //   119: aload_0
    //   120: aload_3
    //   121: invokespecial <init> : ([B)V
    //   124: new java/util/zip/GZIPInputStream
    //   127: astore_2
    //   128: aload_2
    //   129: aload_0
    //   130: invokespecial <init> : (Ljava/io/InputStream;)V
    //   133: aload_2
    //   134: aload #5
    //   136: invokevirtual read : ([B)I
    //   139: istore_1
    //   140: iload_1
    //   141: iflt -> 156
    //   144: aload #7
    //   146: aload #5
    //   148: iconst_0
    //   149: iload_1
    //   150: invokevirtual write : ([BII)V
    //   153: goto -> 133
    //   156: aload #7
    //   158: invokevirtual toByteArray : ()[B
    //   161: astore #8
    //   163: aload_0
    //   164: astore #5
    //   166: aload_2
    //   167: astore #6
    //   169: aload #8
    //   171: astore #4
    //   173: aload #7
    //   175: invokevirtual close : ()V
    //   178: aload #8
    //   180: astore #4
    //   182: aload_2
    //   183: astore #6
    //   185: aload_0
    //   186: astore_2
    //   187: aload #6
    //   189: invokevirtual close : ()V
    //   192: aload_2
    //   193: invokevirtual close : ()V
    //   196: goto -> 328
    //   199: astore #4
    //   201: aload_2
    //   202: astore_3
    //   203: goto -> 306
    //   206: astore #4
    //   208: aload_0
    //   209: astore #6
    //   211: aload_2
    //   212: astore_0
    //   213: goto -> 246
    //   216: astore_2
    //   217: goto -> 312
    //   220: astore #4
    //   222: aconst_null
    //   223: astore_2
    //   224: aload_0
    //   225: astore #6
    //   227: aload_2
    //   228: astore_0
    //   229: goto -> 246
    //   232: astore_2
    //   233: aconst_null
    //   234: astore_0
    //   235: goto -> 312
    //   238: astore #4
    //   240: aconst_null
    //   241: astore #6
    //   243: aload #6
    //   245: astore_0
    //   246: aload #6
    //   248: astore_2
    //   249: goto -> 272
    //   252: astore_2
    //   253: aconst_null
    //   254: astore #7
    //   256: aload #7
    //   258: astore_0
    //   259: goto -> 312
    //   262: astore #4
    //   264: aconst_null
    //   265: astore_2
    //   266: aload_2
    //   267: astore_0
    //   268: aload #6
    //   270: astore #7
    //   272: aload #4
    //   274: invokevirtual printStackTrace : ()V
    //   277: aload_2
    //   278: astore #5
    //   280: aload_0
    //   281: astore #6
    //   283: aload_3
    //   284: astore #4
    //   286: aload #7
    //   288: invokevirtual close : ()V
    //   291: aload_0
    //   292: astore #6
    //   294: aload_3
    //   295: astore #4
    //   297: goto -> 187
    //   300: astore #4
    //   302: aload_0
    //   303: astore_3
    //   304: aload_2
    //   305: astore_0
    //   306: aload #4
    //   308: astore_2
    //   309: aload_3
    //   310: astore #4
    //   312: aload #7
    //   314: invokevirtual close : ()V
    //   317: aload #4
    //   319: invokevirtual close : ()V
    //   322: aload_0
    //   323: invokevirtual close : ()V
    //   326: aload_2
    //   327: athrow
    //   328: aload #4
    //   330: areturn
    //   331: new java/lang/NullPointerException
    //   334: dup
    //   335: ldc_w 'Input string was null.'
    //   338: invokespecial <init> : (Ljava/lang/String;)V
    //   341: astore_0
    //   342: goto -> 347
    //   345: aload_0
    //   346: athrow
    //   347: goto -> 345
    //   350: astore_0
    //   351: aload #5
    //   353: astore_2
    //   354: goto -> 187
    //   357: astore_0
    //   358: goto -> 192
    //   361: astore_0
    //   362: goto -> 328
    //   365: astore_3
    //   366: goto -> 317
    //   369: astore #4
    //   371: goto -> 322
    //   374: astore_0
    //   375: goto -> 326
    // Exception table:
    //   from	to	target	type
    //   4	11	16	java/io/UnsupportedEncodingException
    //   105	115	262	java/io/IOException
    //   105	115	252	finally
    //   115	124	238	java/io/IOException
    //   115	124	232	finally
    //   124	133	220	java/io/IOException
    //   124	133	216	finally
    //   133	140	206	java/io/IOException
    //   133	140	199	finally
    //   144	153	206	java/io/IOException
    //   144	153	199	finally
    //   156	163	206	java/io/IOException
    //   156	163	199	finally
    //   173	178	350	java/lang/Exception
    //   187	192	357	java/lang/Exception
    //   192	196	361	java/lang/Exception
    //   272	277	300	finally
    //   286	291	350	java/lang/Exception
    //   312	317	365	java/lang/Exception
    //   317	322	369	java/lang/Exception
    //   322	326	374	java/lang/Exception
  }
  
  public static byte[] decode(byte[] paramArrayOfbyte) throws IOException {
    return decode(paramArrayOfbyte, 0, paramArrayOfbyte.length, 0);
  }
  
  public static byte[] decode(byte[] paramArrayOfbyte, int paramInt1, int paramInt2, int paramInt3) throws IOException {
    if (paramArrayOfbyte != null) {
      StringBuilder stringBuilder;
      if (paramInt1 >= 0) {
        int i = paramInt1 + paramInt2;
        if (i <= paramArrayOfbyte.length) {
          if (paramInt2 == 0)
            return new byte[0]; 
          if (paramInt2 >= 4) {
            byte[] arrayOfByte1 = getDecodabet(paramInt3);
            byte[] arrayOfByte2 = new byte[paramInt2 * 3 / 4];
            byte[] arrayOfByte3 = new byte[4];
            int j = 0;
            paramInt2 = 0;
            int k = paramInt1;
            paramInt1 = paramInt2;
            while (true) {
              paramInt2 = paramInt1;
              if (k < i) {
                byte b = arrayOfByte1[paramArrayOfbyte[k] & 0xFF];
                if (b >= -5) {
                  paramInt2 = j;
                  int m = paramInt1;
                  if (b >= -1) {
                    paramInt2 = j + 1;
                    arrayOfByte3[j] = (byte)paramArrayOfbyte[k];
                    if (paramInt2 > 3) {
                      m = paramInt1 + decode4to3(arrayOfByte3, 0, arrayOfByte2, paramInt1, paramInt3);
                      if (paramArrayOfbyte[k] == 61) {
                        paramInt2 = m;
                        break;
                      } 
                      paramInt2 = 0;
                    } else {
                      m = paramInt1;
                    } 
                  } 
                  k++;
                  j = paramInt2;
                  paramInt1 = m;
                  continue;
                } 
                throw new IOException(String.format("Bad Base64 input character decimal %d in array position %d", new Object[] { Integer.valueOf(paramArrayOfbyte[k] & 0xFF), Integer.valueOf(k) }));
              } 
              break;
            } 
            paramArrayOfbyte = new byte[paramInt2];
            System.arraycopy(arrayOfByte2, 0, paramArrayOfbyte, 0, paramInt2);
            return paramArrayOfbyte;
          } 
          stringBuilder = new StringBuilder();
          stringBuilder.append("Base64-encoded string must have at least four characters, but length specified was ");
          stringBuilder.append(paramInt2);
          throw new IllegalArgumentException(stringBuilder.toString());
        } 
      } 
      throw new IllegalArgumentException(String.format("Source array with length %d cannot have offset of %d and process %d bytes.", new Object[] { Integer.valueOf(stringBuilder.length), Integer.valueOf(paramInt1), Integer.valueOf(paramInt2) }));
    } 
    NullPointerException nullPointerException = new NullPointerException("Cannot decode null source array.");
    throw nullPointerException;
  }
  
  private static int decode4to3(byte[] paramArrayOfbyte1, int paramInt1, byte[] paramArrayOfbyte2, int paramInt2, int paramInt3) {
    if (paramArrayOfbyte1 != null) {
      if (paramArrayOfbyte2 != null) {
        if (paramInt1 >= 0) {
          int i = paramInt1 + 3;
          if (i < paramArrayOfbyte1.length) {
            if (paramInt2 >= 0) {
              int j = paramInt2 + 2;
              if (j < paramArrayOfbyte2.length) {
                byte[] arrayOfByte = getDecodabet(paramInt3);
                paramInt3 = paramInt1 + 2;
                if (paramArrayOfbyte1[paramInt3] == 61) {
                  paramInt3 = arrayOfByte[paramArrayOfbyte1[paramInt1]];
                  paramArrayOfbyte2[paramInt2] = (byte)(byte)(((arrayOfByte[paramArrayOfbyte1[paramInt1 + 1]] & 0xFF) << 12 | (paramInt3 & 0xFF) << 18) >>> 16);
                  return 1;
                } 
                if (paramArrayOfbyte1[i] == 61) {
                  j = arrayOfByte[paramArrayOfbyte1[paramInt1]];
                  paramInt1 = arrayOfByte[paramArrayOfbyte1[paramInt1 + 1]];
                  paramInt1 = (arrayOfByte[paramArrayOfbyte1[paramInt3]] & 0xFF) << 6 | (paramInt1 & 0xFF) << 12 | (j & 0xFF) << 18;
                  paramArrayOfbyte2[paramInt2] = (byte)(byte)(paramInt1 >>> 16);
                  paramArrayOfbyte2[paramInt2 + 1] = (byte)(byte)(paramInt1 >>> 8);
                  return 2;
                } 
                byte b = arrayOfByte[paramArrayOfbyte1[paramInt1]];
                paramInt1 = arrayOfByte[paramArrayOfbyte1[paramInt1 + 1]];
                paramInt3 = arrayOfByte[paramArrayOfbyte1[paramInt3]];
                paramInt1 = arrayOfByte[paramArrayOfbyte1[i]] & 0xFF | (paramInt1 & 0xFF) << 12 | (b & 0xFF) << 18 | (paramInt3 & 0xFF) << 6;
                paramArrayOfbyte2[paramInt2] = (byte)(byte)(paramInt1 >> 16);
                paramArrayOfbyte2[paramInt2 + 1] = (byte)(byte)(paramInt1 >> 8);
                paramArrayOfbyte2[j] = (byte)(byte)paramInt1;
                return 3;
              } 
            } 
            throw new IllegalArgumentException(String.format("Destination array with length %d cannot have offset of %d and still store three bytes.", new Object[] { Integer.valueOf(paramArrayOfbyte2.length), Integer.valueOf(paramInt2) }));
          } 
        } 
        throw new IllegalArgumentException(String.format("Source array with length %d cannot have offset of %d and still process four bytes.", new Object[] { Integer.valueOf(paramArrayOfbyte1.length), Integer.valueOf(paramInt1) }));
      } 
      throw new NullPointerException("Destination array was null.");
    } 
    throw new NullPointerException("Source array was null.");
  }
  
  public static void decodeFileToFile(String paramString1, String paramString2) throws IOException {
    byte[] arrayOfByte = decodeFromFile(paramString1);
    String str1 = null;
    String str2 = null;
    paramString1 = str2;
    try {
      BufferedOutputStream bufferedOutputStream = new BufferedOutputStream();
      paramString1 = str2;
      FileOutputStream fileOutputStream = new FileOutputStream();
      paramString1 = str2;
      this(paramString2);
      paramString1 = str2;
      this(fileOutputStream);
      try {
        bufferedOutputStream.write(arrayOfByte);
      } catch (IOException iOException) {
      
      } finally {
        BufferedOutputStream bufferedOutputStream1;
        paramString2 = null;
      } 
    } catch (IOException iOException) {
      paramString1 = str1;
    } finally {}
    throw paramString2;
  }
  
  public static byte[] decodeFromFile(String paramString) throws IOException {
    // Byte code:
    //   0: aconst_null
    //   1: astore_1
    //   2: aconst_null
    //   3: astore_2
    //   4: aload_2
    //   5: astore_3
    //   6: new java/io/File
    //   9: astore #4
    //   11: aload_2
    //   12: astore_3
    //   13: aload #4
    //   15: aload_0
    //   16: invokespecial <init> : (Ljava/lang/String;)V
    //   19: aload_2
    //   20: astore_3
    //   21: aload #4
    //   23: invokevirtual length : ()J
    //   26: ldc2_w 2147483647
    //   29: lcmp
    //   30: ifgt -> 156
    //   33: aload_2
    //   34: astore_3
    //   35: aload #4
    //   37: invokevirtual length : ()J
    //   40: l2i
    //   41: newarray byte
    //   43: astore #5
    //   45: aload_2
    //   46: astore_3
    //   47: new com/dd/plist/Base64$B64InputStream
    //   50: astore_0
    //   51: aload_2
    //   52: astore_3
    //   53: new java/io/BufferedInputStream
    //   56: astore #6
    //   58: aload_2
    //   59: astore_3
    //   60: new java/io/FileInputStream
    //   63: astore #7
    //   65: aload_2
    //   66: astore_3
    //   67: aload #7
    //   69: aload #4
    //   71: invokespecial <init> : (Ljava/io/File;)V
    //   74: aload_2
    //   75: astore_3
    //   76: aload #6
    //   78: aload #7
    //   80: invokespecial <init> : (Ljava/io/InputStream;)V
    //   83: aload_2
    //   84: astore_3
    //   85: aload_0
    //   86: aload #6
    //   88: iconst_0
    //   89: invokespecial <init> : (Ljava/io/InputStream;I)V
    //   92: iconst_0
    //   93: istore #8
    //   95: aload_0
    //   96: aload #5
    //   98: iload #8
    //   100: sipush #4096
    //   103: invokevirtual read : ([BII)I
    //   106: istore #9
    //   108: iload #9
    //   110: iflt -> 123
    //   113: iload #8
    //   115: iload #9
    //   117: iadd
    //   118: istore #8
    //   120: goto -> 95
    //   123: iload #8
    //   125: newarray byte
    //   127: astore_3
    //   128: aload #5
    //   130: iconst_0
    //   131: aload_3
    //   132: iconst_0
    //   133: iload #8
    //   135: invokestatic arraycopy : (Ljava/lang/Object;ILjava/lang/Object;II)V
    //   138: aload_0
    //   139: invokevirtual close : ()V
    //   142: aload_3
    //   143: areturn
    //   144: astore_3
    //   145: goto -> 239
    //   148: astore_1
    //   149: aload_0
    //   150: astore_3
    //   151: aload_1
    //   152: astore_0
    //   153: goto -> 237
    //   156: aload_2
    //   157: astore_3
    //   158: new java/io/IOException
    //   161: astore_0
    //   162: aload_2
    //   163: astore_3
    //   164: new java/lang/StringBuilder
    //   167: astore #5
    //   169: aload_2
    //   170: astore_3
    //   171: aload #5
    //   173: invokespecial <init> : ()V
    //   176: aload_2
    //   177: astore_3
    //   178: aload #5
    //   180: ldc_w 'File is too big for this convenience method ('
    //   183: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   186: pop
    //   187: aload_2
    //   188: astore_3
    //   189: aload #5
    //   191: aload #4
    //   193: invokevirtual length : ()J
    //   196: invokevirtual append : (J)Ljava/lang/StringBuilder;
    //   199: pop
    //   200: aload_2
    //   201: astore_3
    //   202: aload #5
    //   204: ldc_w ' bytes).'
    //   207: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   210: pop
    //   211: aload_2
    //   212: astore_3
    //   213: aload_0
    //   214: aload #5
    //   216: invokevirtual toString : ()Ljava/lang/String;
    //   219: invokespecial <init> : (Ljava/lang/String;)V
    //   222: aload_2
    //   223: astore_3
    //   224: aload_0
    //   225: athrow
    //   226: astore_1
    //   227: aload_3
    //   228: astore_0
    //   229: aload_1
    //   230: astore_3
    //   231: goto -> 239
    //   234: astore_0
    //   235: aload_1
    //   236: astore_3
    //   237: aload_0
    //   238: athrow
    //   239: aload_0
    //   240: invokevirtual close : ()V
    //   243: goto -> 248
    //   246: aload_3
    //   247: athrow
    //   248: goto -> 246
    //   251: astore_0
    //   252: goto -> 142
    //   255: astore_0
    //   256: goto -> 243
    // Exception table:
    //   from	to	target	type
    //   6	11	234	java/io/IOException
    //   6	11	226	finally
    //   13	19	234	java/io/IOException
    //   13	19	226	finally
    //   21	33	234	java/io/IOException
    //   21	33	226	finally
    //   35	45	234	java/io/IOException
    //   35	45	226	finally
    //   47	51	234	java/io/IOException
    //   47	51	226	finally
    //   53	58	234	java/io/IOException
    //   53	58	226	finally
    //   60	65	234	java/io/IOException
    //   60	65	226	finally
    //   67	74	234	java/io/IOException
    //   67	74	226	finally
    //   76	83	234	java/io/IOException
    //   76	83	226	finally
    //   85	92	234	java/io/IOException
    //   85	92	226	finally
    //   95	108	148	java/io/IOException
    //   95	108	144	finally
    //   123	138	148	java/io/IOException
    //   123	138	144	finally
    //   138	142	251	java/lang/Exception
    //   158	162	234	java/io/IOException
    //   158	162	226	finally
    //   164	169	234	java/io/IOException
    //   164	169	226	finally
    //   171	176	234	java/io/IOException
    //   171	176	226	finally
    //   178	187	234	java/io/IOException
    //   178	187	226	finally
    //   189	200	234	java/io/IOException
    //   189	200	226	finally
    //   202	211	234	java/io/IOException
    //   202	211	226	finally
    //   213	222	234	java/io/IOException
    //   213	222	226	finally
    //   224	226	234	java/io/IOException
    //   224	226	226	finally
    //   237	239	226	finally
    //   239	243	255	java/lang/Exception
  }
  
  public static void decodeToFile(String paramString1, String paramString2) throws IOException {
    B64OutputStream b64OutputStream1 = null;
    B64OutputStream b64OutputStream2 = null;
    B64OutputStream b64OutputStream3 = b64OutputStream2;
    try {
      B64OutputStream b64OutputStream = new B64OutputStream();
      b64OutputStream3 = b64OutputStream2;
      FileOutputStream fileOutputStream = new FileOutputStream();
      b64OutputStream3 = b64OutputStream2;
      this(paramString2);
      b64OutputStream3 = b64OutputStream2;
      this(fileOutputStream, 0);
      try {
        b64OutputStream.write(paramString1.getBytes("US-ASCII"));
      } catch (IOException iOException) {
      
      } finally {
        paramString1 = null;
      } 
    } catch (IOException iOException) {
      b64OutputStream3 = b64OutputStream1;
    } finally {}
    throw paramString1;
  }
  
  public static Object decodeToObject(String paramString) throws IOException, ClassNotFoundException {
    return decodeToObject(paramString, 0, null);
  }
  
  public static Object decodeToObject(String paramString, int paramInt, ClassLoader paramClassLoader) throws IOException, ClassNotFoundException {
    Object object;
    byte[] arrayOfByte = decode(paramString, paramInt);
    ObjectInputStream objectInputStream1 = null;
    ObjectInputStream objectInputStream2 = null;
    ObjectInputStream objectInputStream3 = null;
    ObjectInputStream objectInputStream4 = null;
    String str = null;
    paramString = null;
    try {
      ObjectInputStream objectInputStream;
      ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream();
      this(arrayOfByte);
    } catch (IOException null) {
      paramClassLoader = null;
    } catch (ClassNotFoundException null) {
      paramClassLoader = null;
      ObjectInputStream objectInputStream = objectInputStream3;
    } finally {
      paramString = null;
    } 
    throw object;
  }
  
  public static void encode(ByteBuffer paramByteBuffer1, ByteBuffer paramByteBuffer2) {
    byte[] arrayOfByte1 = new byte[3];
    byte[] arrayOfByte2 = new byte[4];
    while (paramByteBuffer1.hasRemaining()) {
      int i = Math.min(3, paramByteBuffer1.remaining());
      paramByteBuffer1.get(arrayOfByte1, 0, i);
      encode3to4(arrayOfByte2, arrayOfByte1, i, 0);
      paramByteBuffer2.put(arrayOfByte2);
    } 
  }
  
  public static void encode(ByteBuffer paramByteBuffer, CharBuffer paramCharBuffer) {
    byte[] arrayOfByte1 = new byte[3];
    byte[] arrayOfByte2 = new byte[4];
    while (paramByteBuffer.hasRemaining()) {
      int i = Math.min(3, paramByteBuffer.remaining());
      byte b = 0;
      paramByteBuffer.get(arrayOfByte1, 0, i);
      encode3to4(arrayOfByte2, arrayOfByte1, i, 0);
      while (b < 4) {
        paramCharBuffer.put((char)(arrayOfByte2[b] & 0xFF));
        b++;
      } 
    } 
  }
  
  private static byte[] encode3to4(byte[] paramArrayOfbyte1, int paramInt1, int paramInt2, byte[] paramArrayOfbyte2, int paramInt3, int paramInt4) {
    byte b;
    byte[] arrayOfByte = getAlphabet(paramInt4);
    int i = 0;
    if (paramInt2 > 0) {
      paramInt4 = paramArrayOfbyte1[paramInt1] << 24 >>> 8;
    } else {
      paramInt4 = 0;
    } 
    if (paramInt2 > 1) {
      b = paramArrayOfbyte1[paramInt1 + 1] << 24 >>> 16;
    } else {
      b = 0;
    } 
    if (paramInt2 > 2)
      i = paramArrayOfbyte1[paramInt1 + 2] << 24 >>> 24; 
    paramInt1 = paramInt4 | b | i;
    if (paramInt2 != 1) {
      if (paramInt2 != 2) {
        if (paramInt2 != 3)
          return paramArrayOfbyte2; 
        paramArrayOfbyte2[paramInt3] = (byte)arrayOfByte[paramInt1 >>> 18];
        paramArrayOfbyte2[paramInt3 + 1] = (byte)arrayOfByte[paramInt1 >>> 12 & 0x3F];
        paramArrayOfbyte2[paramInt3 + 2] = (byte)arrayOfByte[paramInt1 >>> 6 & 0x3F];
        paramArrayOfbyte2[paramInt3 + 3] = (byte)arrayOfByte[paramInt1 & 0x3F];
        return paramArrayOfbyte2;
      } 
      paramArrayOfbyte2[paramInt3] = (byte)arrayOfByte[paramInt1 >>> 18];
      paramArrayOfbyte2[paramInt3 + 1] = (byte)arrayOfByte[paramInt1 >>> 12 & 0x3F];
      paramArrayOfbyte2[paramInt3 + 2] = (byte)arrayOfByte[paramInt1 >>> 6 & 0x3F];
      paramArrayOfbyte2[paramInt3 + 3] = (byte)61;
      return paramArrayOfbyte2;
    } 
    paramArrayOfbyte2[paramInt3] = (byte)arrayOfByte[paramInt1 >>> 18];
    paramArrayOfbyte2[paramInt3 + 1] = (byte)arrayOfByte[paramInt1 >>> 12 & 0x3F];
    paramArrayOfbyte2[paramInt3 + 2] = (byte)61;
    paramArrayOfbyte2[paramInt3 + 3] = (byte)61;
    return paramArrayOfbyte2;
  }
  
  private static byte[] encode3to4(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, int paramInt1, int paramInt2) {
    encode3to4(paramArrayOfbyte2, 0, paramInt1, paramArrayOfbyte1, 0, paramInt2);
    return paramArrayOfbyte1;
  }
  
  public static String encodeBytes(byte[] paramArrayOfbyte) {
    try {
      String str = encodeBytes(paramArrayOfbyte, 0, paramArrayOfbyte.length, 0);
    } catch (IOException iOException) {
      iOException = null;
    } 
    return (String)iOException;
  }
  
  public static String encodeBytes(byte[] paramArrayOfbyte, int paramInt) throws IOException {
    return encodeBytes(paramArrayOfbyte, 0, paramArrayOfbyte.length, paramInt);
  }
  
  public static String encodeBytes(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    try {
      String str = encodeBytes(paramArrayOfbyte, paramInt1, paramInt2, 0);
    } catch (IOException iOException) {
      iOException = null;
    } 
    return (String)iOException;
  }
  
  public static String encodeBytes(byte[] paramArrayOfbyte, int paramInt1, int paramInt2, int paramInt3) throws IOException {
    paramArrayOfbyte = encodeBytesToBytes(paramArrayOfbyte, paramInt1, paramInt2, paramInt3);
    try {
      return new String(paramArrayOfbyte, "US-ASCII");
    } catch (UnsupportedEncodingException unsupportedEncodingException) {
      return new String(paramArrayOfbyte);
    } 
  }
  
  public static byte[] encodeBytesToBytes(byte[] paramArrayOfbyte) {
    try {
      paramArrayOfbyte = encodeBytesToBytes(paramArrayOfbyte, 0, paramArrayOfbyte.length, 0);
    } catch (IOException iOException) {
      iOException = null;
    } 
    return (byte[])iOException;
  }
  
  public static byte[] encodeBytesToBytes(byte[] paramArrayOfbyte, int paramInt1, int paramInt2, int paramInt3) throws IOException {
    if (paramArrayOfbyte != null) {
      if (paramInt1 >= 0) {
        if (paramInt2 >= 0) {
          byte[] arrayOfByte;
          if (paramInt1 + paramInt2 <= paramArrayOfbyte.length) {
            GZIPOutputStream gZIPOutputStream;
            boolean bool;
            if ((paramInt3 & 0x2) != 0) {
              B64OutputStream b64OutputStream1;
              byte[] arrayOfByte3;
              B64OutputStream b64OutputStream2;
              GZIPOutputStream gZIPOutputStream1 = null;
              GZIPOutputStream gZIPOutputStream2 = null;
              try {
                GZIPOutputStream gZIPOutputStream3;
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                this();
              } catch (IOException iOException2) {
                b64OutputStream2 = null;
              } finally {
                paramArrayOfbyte = null;
                b64OutputStream1 = null;
                b64OutputStream2 = b64OutputStream1;
              } 
              try {
                throw gZIPOutputStream2;
              } finally {
                gZIPOutputStream2 = null;
                arrayOfByte3 = paramArrayOfbyte;
              } 
              byte[] arrayOfByte2 = arrayOfByte3;
              try {
                arrayOfByte2.close();
              } catch (Exception exception) {}
              try {
                b64OutputStream2.close();
              } catch (Exception exception) {}
              try {
                b64OutputStream1.close();
              } catch (Exception exception) {}
            } 
            if ((paramInt3 & 0x8) != 0) {
              bool = true;
            } else {
              bool = false;
            } 
            int i = paramInt2 / 3;
            if (paramInt2 % 3 > 0) {
              j = 4;
            } else {
              j = 0;
            } 
            i = i * 4 + j;
            int j = i;
            if (bool)
              j = i + i / 76; 
            byte[] arrayOfByte1 = new byte[j];
            byte b = 0;
            j = 0;
            i = 0;
            while (b < paramInt2 - 2) {
              encode3to4((byte[])gZIPOutputStream, b + paramInt1, 3, arrayOfByte1, j, paramInt3);
              i += 4;
              if (bool && i >= 76) {
                arrayOfByte1[j + 4] = (byte)10;
                j++;
                i = 0;
              } 
              b += 3;
              j += 4;
            } 
            i = j;
            if (b < paramInt2) {
              encode3to4((byte[])gZIPOutputStream, b + paramInt1, paramInt2 - b, arrayOfByte1, j, paramInt3);
              i = j + 4;
            } 
            if (i <= arrayOfByte1.length - 1) {
              arrayOfByte = new byte[i];
              System.arraycopy(arrayOfByte1, 0, arrayOfByte, 0, i);
              return arrayOfByte;
            } 
            return arrayOfByte1;
          } 
          throw new IllegalArgumentException(String.format("Cannot have offset of %d and length of %d with array of length %d", new Object[] { Integer.valueOf(paramInt1), Integer.valueOf(paramInt2), Integer.valueOf(arrayOfByte.length) }));
        } 
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("Cannot have length offset: ");
        stringBuilder1.append(paramInt2);
        throw new IllegalArgumentException(stringBuilder1.toString());
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Cannot have negative offset: ");
      stringBuilder.append(paramInt1);
      throw new IllegalArgumentException(stringBuilder.toString());
    } 
    NullPointerException nullPointerException = new NullPointerException("Cannot serialize a null array.");
    throw nullPointerException;
  }
  
  public static void encodeFileToFile(String paramString1, String paramString2) throws IOException {
    String str1 = encodeFromFile(paramString1);
    String str2 = null;
    String str3 = null;
    paramString1 = str3;
    try {
      BufferedOutputStream bufferedOutputStream = new BufferedOutputStream();
      paramString1 = str3;
      FileOutputStream fileOutputStream = new FileOutputStream();
      paramString1 = str3;
      this(paramString2);
      paramString1 = str3;
    } catch (IOException iOException) {
    
    } finally {
      Exception exception1;
      Exception exception2 = null;
      paramString2 = paramString1;
    } 
    throw paramString2;
  }
  
  public static String encodeFromFile(String paramString) throws IOException {
    // Byte code:
    //   0: aconst_null
    //   1: astore_1
    //   2: aconst_null
    //   3: astore_2
    //   4: aload_2
    //   5: astore_3
    //   6: new java/io/File
    //   9: astore #4
    //   11: aload_2
    //   12: astore_3
    //   13: aload #4
    //   15: aload_0
    //   16: invokespecial <init> : (Ljava/lang/String;)V
    //   19: aload_2
    //   20: astore_3
    //   21: aload #4
    //   23: invokevirtual length : ()J
    //   26: lstore #5
    //   28: lload #5
    //   30: l2d
    //   31: dstore #7
    //   33: dload #7
    //   35: invokestatic isNaN : (D)Z
    //   38: pop
    //   39: dload #7
    //   41: ldc2_w 1.4
    //   44: dmul
    //   45: dconst_1
    //   46: dadd
    //   47: d2i
    //   48: istore #9
    //   50: aload_2
    //   51: astore_3
    //   52: iload #9
    //   54: bipush #40
    //   56: invokestatic max : (II)I
    //   59: newarray byte
    //   61: astore #10
    //   63: aload_2
    //   64: astore_3
    //   65: new com/dd/plist/Base64$B64InputStream
    //   68: astore_0
    //   69: aload_2
    //   70: astore_3
    //   71: new java/io/BufferedInputStream
    //   74: astore #11
    //   76: aload_2
    //   77: astore_3
    //   78: new java/io/FileInputStream
    //   81: astore #12
    //   83: aload_2
    //   84: astore_3
    //   85: aload #12
    //   87: aload #4
    //   89: invokespecial <init> : (Ljava/io/File;)V
    //   92: aload_2
    //   93: astore_3
    //   94: aload #11
    //   96: aload #12
    //   98: invokespecial <init> : (Ljava/io/InputStream;)V
    //   101: aload_2
    //   102: astore_3
    //   103: aload_0
    //   104: aload #11
    //   106: iconst_1
    //   107: invokespecial <init> : (Ljava/io/InputStream;I)V
    //   110: iconst_0
    //   111: istore #9
    //   113: aload_0
    //   114: aload #10
    //   116: iload #9
    //   118: sipush #4096
    //   121: invokevirtual read : ([BII)I
    //   124: istore #13
    //   126: iload #13
    //   128: iflt -> 141
    //   131: iload #9
    //   133: iload #13
    //   135: iadd
    //   136: istore #9
    //   138: goto -> 113
    //   141: new java/lang/String
    //   144: dup
    //   145: aload #10
    //   147: iconst_0
    //   148: iload #9
    //   150: ldc 'US-ASCII'
    //   152: invokespecial <init> : ([BIILjava/lang/String;)V
    //   155: astore_3
    //   156: aload_0
    //   157: invokevirtual close : ()V
    //   160: aload_3
    //   161: areturn
    //   162: astore_2
    //   163: aload_0
    //   164: astore_3
    //   165: goto -> 183
    //   168: astore_2
    //   169: aload_0
    //   170: astore_3
    //   171: goto -> 181
    //   174: astore_2
    //   175: goto -> 183
    //   178: astore_2
    //   179: aload_1
    //   180: astore_3
    //   181: aload_2
    //   182: athrow
    //   183: aload_3
    //   184: invokevirtual close : ()V
    //   187: goto -> 192
    //   190: aload_2
    //   191: athrow
    //   192: goto -> 190
    //   195: astore_0
    //   196: goto -> 160
    //   199: astore_0
    //   200: goto -> 187
    // Exception table:
    //   from	to	target	type
    //   6	11	178	java/io/IOException
    //   6	11	174	finally
    //   13	19	178	java/io/IOException
    //   13	19	174	finally
    //   21	28	178	java/io/IOException
    //   21	28	174	finally
    //   52	63	178	java/io/IOException
    //   52	63	174	finally
    //   65	69	178	java/io/IOException
    //   65	69	174	finally
    //   71	76	178	java/io/IOException
    //   71	76	174	finally
    //   78	83	178	java/io/IOException
    //   78	83	174	finally
    //   85	92	178	java/io/IOException
    //   85	92	174	finally
    //   94	101	178	java/io/IOException
    //   94	101	174	finally
    //   103	110	178	java/io/IOException
    //   103	110	174	finally
    //   113	126	168	java/io/IOException
    //   113	126	162	finally
    //   141	156	168	java/io/IOException
    //   141	156	162	finally
    //   156	160	195	java/lang/Exception
    //   181	183	174	finally
    //   183	187	199	java/lang/Exception
  }
  
  public static String encodeObject(Serializable paramSerializable) throws IOException {
    return encodeObject(paramSerializable, 0);
  }
  
  public static String encodeObject(Serializable paramSerializable, int paramInt) throws IOException {
    if (paramSerializable != null) {
      ObjectOutputStream objectOutputStream2;
      ObjectOutputStream objectOutputStream4;
      Serializable serializable;
      ObjectOutputStream objectOutputStream5;
      ByteArrayOutputStream byteArrayOutputStream = null;
      ObjectOutputStream objectOutputStream1 = null;
      GZIPOutputStream gZIPOutputStream = null;
      ObjectOutputStream objectOutputStream3 = null;
      try {
        ByteArrayOutputStream byteArrayOutputStream1;
      } catch (IOException iOException) {
        objectOutputStream5 = null;
      } finally {
        paramSerializable = null;
        objectOutputStream5 = null;
        objectOutputStream4 = objectOutputStream5;
      } 
      try {
        throw iOException;
      } finally {
        objectOutputStream1 = null;
        Serializable serializable1 = paramSerializable;
        objectOutputStream2 = objectOutputStream4;
        ObjectOutputStream objectOutputStream = objectOutputStream1;
      } 
      try {
        objectOutputStream2.close();
      } catch (Exception exception) {}
      try {
        objectOutputStream5.close();
      } catch (Exception exception) {}
      try {
        objectOutputStream3.close();
      } catch (Exception exception) {}
      try {
        serializable.close();
      } catch (Exception exception) {}
    } 
    throw new NullPointerException("Cannot serialize a null object.");
  }
  
  public static void encodeToFile(byte[] paramArrayOfbyte, String paramString) throws IOException {
    if (paramArrayOfbyte != null) {
      B64OutputStream b64OutputStream1 = null;
      B64OutputStream b64OutputStream2 = null;
      B64OutputStream b64OutputStream3 = b64OutputStream2;
      try {
        B64OutputStream b64OutputStream = new B64OutputStream();
        b64OutputStream3 = b64OutputStream2;
        FileOutputStream fileOutputStream = new FileOutputStream();
        b64OutputStream3 = b64OutputStream2;
        this(paramString);
        b64OutputStream3 = b64OutputStream2;
        this(fileOutputStream, 1);
        try {
          b64OutputStream.write(paramArrayOfbyte);
        } catch (IOException iOException) {
        
        } finally {
          paramArrayOfbyte = null;
        } 
      } catch (IOException iOException) {
        b64OutputStream3 = b64OutputStream1;
      } finally {}
      throw paramArrayOfbyte;
    } 
    throw new NullPointerException("Data to encode was null.");
  }
  
  private static byte[] getAlphabet(int paramInt) {
    return ((paramInt & 0x10) == 16) ? _URL_SAFE_ALPHABET : (((paramInt & 0x20) == 32) ? _ORDERED_ALPHABET : _STANDARD_ALPHABET);
  }
  
  private static byte[] getDecodabet(int paramInt) {
    return ((paramInt & 0x10) == 16) ? _URL_SAFE_DECODABET : (((paramInt & 0x20) == 32) ? _ORDERED_DECODABET : _STANDARD_DECODABET);
  }
  
  public static class B64InputStream extends FilterInputStream {
    private boolean breakLines;
    
    private byte[] buffer;
    
    private int bufferLength;
    
    private byte[] decodabet;
    
    private boolean encode;
    
    private int lineLength;
    
    private int numSigBytes;
    
    private int options;
    
    private int position;
    
    public B64InputStream(InputStream param1InputStream) {
      this(param1InputStream, 0);
    }
    
    public B64InputStream(InputStream param1InputStream, int param1Int) {
      super(param1InputStream);
      boolean bool2;
      byte b;
      this.options = param1Int;
      boolean bool1 = true;
      if ((param1Int & 0x8) > 0) {
        bool2 = true;
      } else {
        bool2 = false;
      } 
      this.breakLines = bool2;
      if ((param1Int & 0x1) > 0) {
        bool2 = bool1;
      } else {
        bool2 = false;
      } 
      this.encode = bool2;
      if (this.encode) {
        b = 4;
      } else {
        b = 3;
      } 
      this.bufferLength = b;
      this.buffer = new byte[this.bufferLength];
      this.position = -1;
      this.lineLength = 0;
      this.decodabet = Base64.getDecodabet(param1Int);
    }
    
    public int read() throws IOException {
      if (this.position < 0)
        if (this.encode) {
          byte[] arrayOfByte = new byte[3];
          byte b1 = 0;
          byte b2 = 0;
          while (b1 < 3) {
            int j = this.in.read();
            if (j >= 0) {
              arrayOfByte[b1] = (byte)(byte)j;
              b2++;
              b1++;
            } 
          } 
          if (b2 > 0) {
            Base64.encode3to4(arrayOfByte, 0, b2, this.buffer, 0, this.options);
            this.position = 0;
            this.numSigBytes = 4;
          } else {
            return -1;
          } 
        } else {
          byte[] arrayOfByte = new byte[4];
          byte b;
          for (b = 0; b < 4; b++) {
            int j;
            do {
              j = this.in.read();
            } while (j >= 0 && this.decodabet[j & 0x7F] <= -5);
            if (j < 0)
              break; 
            arrayOfByte[b] = (byte)(byte)j;
          } 
          if (b == 4) {
            this.numSigBytes = Base64.decode4to3(arrayOfByte, 0, this.buffer, 0, this.options);
            this.position = 0;
          } else {
            if (b == 0)
              return -1; 
            throw new IOException("Improperly padded Base64 input.");
          } 
        }  
      int i = this.position;
      if (i >= 0) {
        if (i >= this.numSigBytes)
          return -1; 
        if (this.encode && this.breakLines && this.lineLength >= 76) {
          this.lineLength = 0;
          return 10;
        } 
        this.lineLength++;
        byte[] arrayOfByte = this.buffer;
        i = this.position;
        this.position = i + 1;
        i = arrayOfByte[i];
        if (this.position >= this.bufferLength)
          this.position = -1; 
        return i & 0xFF;
      } 
      IOException iOException = new IOException("Error in Base64 code reading stream.");
      throw iOException;
    }
    
    public int read(byte[] param1ArrayOfbyte, int param1Int1, int param1Int2) throws IOException {
      byte b = 0;
      while (b < param1Int2) {
        int i = read();
        if (i >= 0) {
          param1ArrayOfbyte[param1Int1 + b] = (byte)(byte)i;
          b++;
          continue;
        } 
        if (b == 0)
          return -1; 
      } 
      return b;
    }
  }
  
  public static class B64OutputStream extends FilterOutputStream {
    private byte[] b4;
    
    private boolean breakLines;
    
    private byte[] buffer;
    
    private int bufferLength;
    
    private byte[] decodabet;
    
    private boolean encode;
    
    private int lineLength;
    
    private int options;
    
    private int position;
    
    private boolean suspendEncoding;
    
    public B64OutputStream(OutputStream param1OutputStream) {
      this(param1OutputStream, 1);
    }
    
    public B64OutputStream(OutputStream param1OutputStream, int param1Int) {
      super(param1OutputStream);
      boolean bool2;
      byte b;
      boolean bool1 = true;
      if ((param1Int & 0x8) != 0) {
        bool2 = true;
      } else {
        bool2 = false;
      } 
      this.breakLines = bool2;
      if ((param1Int & 0x1) != 0) {
        bool2 = bool1;
      } else {
        bool2 = false;
      } 
      this.encode = bool2;
      if (this.encode) {
        b = 3;
      } else {
        b = 4;
      } 
      this.bufferLength = b;
      this.buffer = new byte[this.bufferLength];
      this.position = 0;
      this.lineLength = 0;
      this.suspendEncoding = false;
      this.b4 = new byte[4];
      this.options = param1Int;
      this.decodabet = Base64.getDecodabet(param1Int);
    }
    
    public void close() throws IOException {
      flushBase64();
      super.close();
      this.buffer = null;
      this.out = null;
    }
    
    public void flushBase64() throws IOException {
      if (this.position > 0)
        if (this.encode) {
          this.out.write(Base64.encode3to4(this.b4, this.buffer, this.position, this.options));
          this.position = 0;
        } else {
          throw new IOException("Base64 input not properly padded.");
        }  
    }
    
    public void resumeEncoding() {
      this.suspendEncoding = false;
    }
    
    public void suspendEncoding() throws IOException {
      flushBase64();
      this.suspendEncoding = true;
    }
    
    public void write(int param1Int) throws IOException {
      if (this.suspendEncoding) {
        this.out.write(param1Int);
        return;
      } 
      if (this.encode) {
        byte[] arrayOfByte = this.buffer;
        int i = this.position;
        this.position = i + 1;
        arrayOfByte[i] = (byte)(byte)param1Int;
        if (this.position >= this.bufferLength) {
          this.out.write(Base64.encode3to4(this.b4, this.buffer, this.bufferLength, this.options));
          this.lineLength += 4;
          if (this.breakLines && this.lineLength >= 76) {
            this.out.write(10);
            this.lineLength = 0;
          } 
          this.position = 0;
        } 
      } else {
        byte[] arrayOfByte = this.decodabet;
        int i = param1Int & 0x7F;
        if (arrayOfByte[i] > -5) {
          arrayOfByte = this.buffer;
          i = this.position;
          this.position = i + 1;
          arrayOfByte[i] = (byte)(byte)param1Int;
          if (this.position >= this.bufferLength) {
            param1Int = Base64.decode4to3(arrayOfByte, 0, this.b4, 0, this.options);
            this.out.write(this.b4, 0, param1Int);
            this.position = 0;
          } 
        } else if (arrayOfByte[i] != -5) {
          throw new IOException("Invalid character in Base64 data.");
        } 
      } 
    }
    
    public void write(byte[] param1ArrayOfbyte, int param1Int1, int param1Int2) throws IOException {
      if (this.suspendEncoding) {
        this.out.write(param1ArrayOfbyte, param1Int1, param1Int2);
        return;
      } 
      for (byte b = 0; b < param1Int2; b++)
        write(param1ArrayOfbyte[param1Int1 + b]); 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/dd/plist/Base64.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */