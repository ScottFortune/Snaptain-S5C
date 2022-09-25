package com.dd.plist;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.CharsetEncoder;

public class NSString extends NSObject implements Comparable<Object> {
  private static CharsetEncoder asciiEncoder;
  
  private static CharsetEncoder utf16beEncoder;
  
  private static CharsetEncoder utf8Encoder;
  
  private String content;
  
  public NSString(String paramString) {
    this.content = paramString;
  }
  
  public NSString(byte[] paramArrayOfbyte, String paramString) throws UnsupportedEncodingException {
    this.content = new String(paramArrayOfbyte, paramString);
  }
  
  static String escapeStringForASCII(String paramString) {
    char[] arrayOfChar = paramString.toCharArray();
    paramString = "";
    for (byte b = 0; b < arrayOfChar.length; b++) {
      char c = arrayOfChar[b];
      if (c > '') {
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append(paramString);
        stringBuilder1.append("\\U");
        String str = stringBuilder1.toString();
        for (paramString = Integer.toHexString(c); paramString.length() < 4; paramString = stringBuilder.toString()) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("0");
          stringBuilder.append(paramString);
        } 
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append(str);
        stringBuilder2.append(paramString);
        paramString = stringBuilder2.toString();
      } else if (c == '\\') {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(paramString);
        stringBuilder.append("\\\\");
        paramString = stringBuilder.toString();
      } else if (c == '"') {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(paramString);
        stringBuilder.append("\\\"");
        paramString = stringBuilder.toString();
      } else if (c == '\b') {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(paramString);
        stringBuilder.append("\\b");
        paramString = stringBuilder.toString();
      } else if (c == '\n') {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(paramString);
        stringBuilder.append("\\n");
        paramString = stringBuilder.toString();
      } else if (c == '\r') {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(paramString);
        stringBuilder.append("\\r");
        paramString = stringBuilder.toString();
      } else if (c == '\t') {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(paramString);
        stringBuilder.append("\\t");
        paramString = stringBuilder.toString();
      } else {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(paramString);
        stringBuilder.append(c);
        paramString = stringBuilder.toString();
      } 
    } 
    return paramString;
  }
  
  public void append(NSString paramNSString) {
    append(paramNSString.getContent());
  }
  
  public void append(String paramString) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(this.content);
    stringBuilder.append(paramString);
    this.content = stringBuilder.toString();
  }
  
  public int compareTo(Object paramObject) {
    return (paramObject instanceof NSString) ? getContent().compareTo(((NSString)paramObject).getContent()) : ((paramObject instanceof String) ? getContent().compareTo((String)paramObject) : -1);
  }
  
  public boolean equals(Object paramObject) {
    return !(paramObject instanceof NSString) ? false : this.content.equals(((NSString)paramObject).content);
  }
  
  public String getContent() {
    return this.content;
  }
  
  public int hashCode() {
    return this.content.hashCode();
  }
  
  public void prepend(NSString paramNSString) {
    prepend(paramNSString.getContent());
  }
  
  public void prepend(String paramString) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(paramString);
    stringBuilder.append(this.content);
    this.content = stringBuilder.toString();
  }
  
  public void setContent(String paramString) {
    this.content = paramString;
  }
  
  protected void toASCII(StringBuilder paramStringBuilder, int paramInt) {
    indent(paramStringBuilder, paramInt);
    paramStringBuilder.append("\"");
    paramStringBuilder.append(escapeStringForASCII(this.content));
    paramStringBuilder.append("\"");
  }
  
  protected void toASCIIGnuStep(StringBuilder paramStringBuilder, int paramInt) {
    indent(paramStringBuilder, paramInt);
    paramStringBuilder.append("\"");
    paramStringBuilder.append(escapeStringForASCII(this.content));
    paramStringBuilder.append("\"");
  }
  
  public void toBinary(BinaryPropertyListWriter paramBinaryPropertyListWriter) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: getfield content : Ljava/lang/String;
    //   4: invokestatic wrap : (Ljava/lang/CharSequence;)Ljava/nio/CharBuffer;
    //   7: astore_2
    //   8: ldc com/dd/plist/NSString
    //   10: monitorenter
    //   11: getstatic com/dd/plist/NSString.asciiEncoder : Ljava/nio/charset/CharsetEncoder;
    //   14: ifnonnull -> 31
    //   17: ldc 'ASCII'
    //   19: invokestatic forName : (Ljava/lang/String;)Ljava/nio/charset/Charset;
    //   22: invokevirtual newEncoder : ()Ljava/nio/charset/CharsetEncoder;
    //   25: putstatic com/dd/plist/NSString.asciiEncoder : Ljava/nio/charset/CharsetEncoder;
    //   28: goto -> 38
    //   31: getstatic com/dd/plist/NSString.asciiEncoder : Ljava/nio/charset/CharsetEncoder;
    //   34: invokevirtual reset : ()Ljava/nio/charset/CharsetEncoder;
    //   37: pop
    //   38: getstatic com/dd/plist/NSString.asciiEncoder : Ljava/nio/charset/CharsetEncoder;
    //   41: aload_2
    //   42: invokevirtual canEncode : (Ljava/lang/CharSequence;)Z
    //   45: ifeq -> 61
    //   48: iconst_5
    //   49: istore_3
    //   50: getstatic com/dd/plist/NSString.asciiEncoder : Ljava/nio/charset/CharsetEncoder;
    //   53: aload_2
    //   54: invokevirtual encode : (Ljava/nio/CharBuffer;)Ljava/nio/ByteBuffer;
    //   57: astore_2
    //   58: goto -> 99
    //   61: getstatic com/dd/plist/NSString.utf16beEncoder : Ljava/nio/charset/CharsetEncoder;
    //   64: ifnonnull -> 81
    //   67: ldc 'UTF-16BE'
    //   69: invokestatic forName : (Ljava/lang/String;)Ljava/nio/charset/Charset;
    //   72: invokevirtual newEncoder : ()Ljava/nio/charset/CharsetEncoder;
    //   75: putstatic com/dd/plist/NSString.utf16beEncoder : Ljava/nio/charset/CharsetEncoder;
    //   78: goto -> 88
    //   81: getstatic com/dd/plist/NSString.utf16beEncoder : Ljava/nio/charset/CharsetEncoder;
    //   84: invokevirtual reset : ()Ljava/nio/charset/CharsetEncoder;
    //   87: pop
    //   88: bipush #6
    //   90: istore_3
    //   91: getstatic com/dd/plist/NSString.utf16beEncoder : Ljava/nio/charset/CharsetEncoder;
    //   94: aload_2
    //   95: invokevirtual encode : (Ljava/nio/CharBuffer;)Ljava/nio/ByteBuffer;
    //   98: astore_2
    //   99: ldc com/dd/plist/NSString
    //   101: monitorexit
    //   102: aload_2
    //   103: invokevirtual remaining : ()I
    //   106: newarray byte
    //   108: astore #4
    //   110: aload_2
    //   111: aload #4
    //   113: invokevirtual get : ([B)Ljava/nio/ByteBuffer;
    //   116: pop
    //   117: aload_1
    //   118: iload_3
    //   119: aload_0
    //   120: getfield content : Ljava/lang/String;
    //   123: invokevirtual length : ()I
    //   126: invokevirtual writeIntHeader : (II)V
    //   129: aload_1
    //   130: aload #4
    //   132: invokevirtual write : ([B)V
    //   135: return
    //   136: astore_1
    //   137: ldc com/dd/plist/NSString
    //   139: monitorexit
    //   140: aload_1
    //   141: athrow
    // Exception table:
    //   from	to	target	type
    //   11	28	136	finally
    //   31	38	136	finally
    //   38	48	136	finally
    //   50	58	136	finally
    //   61	78	136	finally
    //   81	88	136	finally
    //   91	99	136	finally
    //   99	102	136	finally
    //   137	140	136	finally
  }
  
  public String toString() {
    return this.content;
  }
  
  void toXML(StringBuilder paramStringBuilder, int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: aload_1
    //   2: iload_2
    //   3: invokevirtual indent : (Ljava/lang/StringBuilder;I)V
    //   6: aload_1
    //   7: ldc '<string>'
    //   9: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   12: pop
    //   13: ldc com/dd/plist/NSString
    //   15: monitorenter
    //   16: getstatic com/dd/plist/NSString.utf8Encoder : Ljava/nio/charset/CharsetEncoder;
    //   19: ifnonnull -> 36
    //   22: ldc 'UTF-8'
    //   24: invokestatic forName : (Ljava/lang/String;)Ljava/nio/charset/Charset;
    //   27: invokevirtual newEncoder : ()Ljava/nio/charset/CharsetEncoder;
    //   30: putstatic com/dd/plist/NSString.utf8Encoder : Ljava/nio/charset/CharsetEncoder;
    //   33: goto -> 43
    //   36: getstatic com/dd/plist/NSString.utf8Encoder : Ljava/nio/charset/CharsetEncoder;
    //   39: invokevirtual reset : ()Ljava/nio/charset/CharsetEncoder;
    //   42: pop
    //   43: getstatic com/dd/plist/NSString.utf8Encoder : Ljava/nio/charset/CharsetEncoder;
    //   46: aload_0
    //   47: getfield content : Ljava/lang/String;
    //   50: invokestatic wrap : (Ljava/lang/CharSequence;)Ljava/nio/CharBuffer;
    //   53: invokevirtual encode : (Ljava/nio/CharBuffer;)Ljava/nio/ByteBuffer;
    //   56: astore_3
    //   57: aload_3
    //   58: invokevirtual remaining : ()I
    //   61: newarray byte
    //   63: astore #4
    //   65: aload_3
    //   66: aload #4
    //   68: invokevirtual get : ([B)Ljava/nio/ByteBuffer;
    //   71: pop
    //   72: new java/lang/String
    //   75: astore_3
    //   76: aload_3
    //   77: aload #4
    //   79: ldc 'UTF-8'
    //   81: invokespecial <init> : ([BLjava/lang/String;)V
    //   84: aload_0
    //   85: aload_3
    //   86: putfield content : Ljava/lang/String;
    //   89: ldc com/dd/plist/NSString
    //   91: monitorexit
    //   92: aload_0
    //   93: getfield content : Ljava/lang/String;
    //   96: ldc '&'
    //   98: invokevirtual contains : (Ljava/lang/CharSequence;)Z
    //   101: ifne -> 143
    //   104: aload_0
    //   105: getfield content : Ljava/lang/String;
    //   108: ldc '<'
    //   110: invokevirtual contains : (Ljava/lang/CharSequence;)Z
    //   113: ifne -> 143
    //   116: aload_0
    //   117: getfield content : Ljava/lang/String;
    //   120: ldc '>'
    //   122: invokevirtual contains : (Ljava/lang/CharSequence;)Z
    //   125: ifeq -> 131
    //   128: goto -> 143
    //   131: aload_1
    //   132: aload_0
    //   133: getfield content : Ljava/lang/String;
    //   136: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   139: pop
    //   140: goto -> 173
    //   143: aload_1
    //   144: ldc '<![CDATA['
    //   146: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   149: pop
    //   150: aload_1
    //   151: aload_0
    //   152: getfield content : Ljava/lang/String;
    //   155: ldc ']]>'
    //   157: ldc ']]]]><![CDATA[>'
    //   159: invokevirtual replaceAll : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   162: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   165: pop
    //   166: aload_1
    //   167: ldc ']]>'
    //   169: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   172: pop
    //   173: aload_1
    //   174: ldc '</string>'
    //   176: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   179: pop
    //   180: return
    //   181: astore_1
    //   182: new java/lang/RuntimeException
    //   185: astore_3
    //   186: new java/lang/StringBuilder
    //   189: astore #4
    //   191: aload #4
    //   193: invokespecial <init> : ()V
    //   196: aload #4
    //   198: ldc 'Could not encode the NSString into UTF-8: '
    //   200: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   203: pop
    //   204: aload #4
    //   206: aload_1
    //   207: invokevirtual getMessage : ()Ljava/lang/String;
    //   210: invokestatic valueOf : (Ljava/lang/Object;)Ljava/lang/String;
    //   213: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   216: pop
    //   217: aload_3
    //   218: aload #4
    //   220: invokevirtual toString : ()Ljava/lang/String;
    //   223: invokespecial <init> : (Ljava/lang/String;)V
    //   226: aload_3
    //   227: athrow
    //   228: astore_1
    //   229: ldc com/dd/plist/NSString
    //   231: monitorexit
    //   232: aload_1
    //   233: athrow
    // Exception table:
    //   from	to	target	type
    //   16	33	228	finally
    //   36	43	228	finally
    //   43	89	181	java/lang/Exception
    //   43	89	228	finally
    //   89	92	228	finally
    //   182	228	228	finally
    //   229	232	228	finally
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/dd/plist/NSString.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */