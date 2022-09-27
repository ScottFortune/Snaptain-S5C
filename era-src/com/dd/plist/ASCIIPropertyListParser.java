package com.dd.plist;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetEncoder;
import java.text.ParseException;
import java.text.StringCharacterIterator;
import java.util.LinkedList;

public class ASCIIPropertyListParser {
  public static final char ARRAY_BEGIN_TOKEN = '(';
  
  public static final char ARRAY_END_TOKEN = ')';
  
  public static final char ARRAY_ITEM_DELIMITER_TOKEN = ',';
  
  public static final char COMMENT_BEGIN_TOKEN = '/';
  
  public static final char DATA_BEGIN_TOKEN = '<';
  
  public static final char DATA_END_TOKEN = '>';
  
  public static final char DATA_GSBOOL_BEGIN_TOKEN = 'B';
  
  public static final char DATA_GSBOOL_FALSE_TOKEN = 'N';
  
  public static final char DATA_GSBOOL_TRUE_TOKEN = 'Y';
  
  public static final char DATA_GSDATE_BEGIN_TOKEN = 'D';
  
  public static final char DATA_GSINT_BEGIN_TOKEN = 'I';
  
  public static final char DATA_GSOBJECT_BEGIN_TOKEN = '*';
  
  public static final char DATA_GSREAL_BEGIN_TOKEN = 'R';
  
  public static final char DATE_APPLE_DATE_TIME_DELIMITER = 'T';
  
  public static final char DATE_APPLE_END_TOKEN = 'Z';
  
  public static final char DATE_DATE_FIELD_DELIMITER = '-';
  
  public static final char DATE_GS_DATE_TIME_DELIMITER = ' ';
  
  public static final char DATE_TIME_FIELD_DELIMITER = ':';
  
  public static final char DICTIONARY_ASSIGN_TOKEN = '=';
  
  public static final char DICTIONARY_BEGIN_TOKEN = '{';
  
  public static final char DICTIONARY_END_TOKEN = '}';
  
  public static final char DICTIONARY_ITEM_DELIMITER_TOKEN = ';';
  
  public static final char MULTILINE_COMMENT_END_TOKEN = '/';
  
  public static final char MULTILINE_COMMENT_SECOND_TOKEN = '*';
  
  public static final char QUOTEDSTRING_BEGIN_TOKEN = '"';
  
  public static final char QUOTEDSTRING_END_TOKEN = '"';
  
  public static final char QUOTEDSTRING_ESCAPE_TOKEN = '\\';
  
  public static final char SINGLELINE_COMMENT_SECOND_TOKEN = '/';
  
  public static final char WHITESPACE_CARRIAGE_RETURN = '\r';
  
  public static final char WHITESPACE_NEWLINE = '\n';
  
  public static final char WHITESPACE_SPACE = ' ';
  
  public static final char WHITESPACE_TAB = '\t';
  
  private static CharsetEncoder asciiEncoder;
  
  private byte[] data;
  
  private int index;
  
  protected ASCIIPropertyListParser() {}
  
  private ASCIIPropertyListParser(byte[] paramArrayOfbyte) {
    this.data = paramArrayOfbyte;
  }
  
  private boolean accept(char paramChar) {
    boolean bool;
    if (this.data[this.index] == paramChar) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  private boolean accept(char... paramVarArgs) {
    int i = paramVarArgs.length;
    byte b = 0;
    boolean bool = false;
    while (b < i) {
      char c = paramVarArgs[b];
      if (this.data[this.index] == c)
        bool = true; 
      b++;
    } 
    return bool;
  }
  
  private boolean acceptSequence(char... paramVarArgs) {
    for (byte b = 0; b < paramVarArgs.length; b++) {
      if (this.data[this.index + b] != paramVarArgs[b])
        return false; 
    } 
    return true;
  }
  
  private void expect(char paramChar) throws ParseException {
    if (accept(paramChar))
      return; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Expected '");
    stringBuilder.append(paramChar);
    stringBuilder.append("' but found '");
    stringBuilder.append((char)this.data[this.index]);
    stringBuilder.append("'");
    throw new ParseException(stringBuilder.toString(), this.index);
  }
  
  private void expect(char... paramVarArgs) throws ParseException {
    if (!accept(paramVarArgs)) {
      StringBuilder stringBuilder2 = new StringBuilder();
      stringBuilder2.append("Expected '");
      stringBuilder2.append(paramVarArgs[0]);
      stringBuilder2.append("'");
      String str = stringBuilder2.toString();
      for (byte b = 1; b < paramVarArgs.length; b++) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(str);
        stringBuilder.append(" or '");
        stringBuilder.append(paramVarArgs[b]);
        stringBuilder.append("'");
        str = stringBuilder.toString();
      } 
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append(str);
      stringBuilder1.append(" but found '");
      stringBuilder1.append((char)this.data[this.index]);
      stringBuilder1.append("'");
      throw new ParseException(stringBuilder1.toString(), this.index);
    } 
  }
  
  public static NSObject parse(File paramFile) throws IOException, ParseException {
    return parse(new FileInputStream(paramFile));
  }
  
  public static NSObject parse(InputStream paramInputStream) throws ParseException, IOException {
    byte[] arrayOfByte = PropertyListParser.readAll(paramInputStream);
    paramInputStream.close();
    return parse(arrayOfByte);
  }
  
  public static NSObject parse(byte[] paramArrayOfbyte) throws ParseException {
    return (new ASCIIPropertyListParser(paramArrayOfbyte)).parse();
  }
  
  private NSArray parseArray() throws ParseException {
    skip();
    skipWhitespacesAndComments();
    LinkedList<NSObject> linkedList = new LinkedList();
    while (!accept(')')) {
      linkedList.add(parseObject());
      skipWhitespacesAndComments();
      if (accept(',')) {
        skip();
        skipWhitespacesAndComments();
      } 
    } 
    read(')');
    return new NSArray(linkedList.<NSObject>toArray(new NSObject[linkedList.size()]));
  }
  
  private NSObject parseData() throws ParseException {
    NSData nSData;
    skip();
    boolean bool = accept('*');
    byte b = 0;
    if (bool) {
      skip();
      expect(new char[] { 'B', 'D', 'I', 'R' });
      if (accept('B')) {
        skip();
        expect(new char[] { 'Y', 'N' });
        if (accept('Y')) {
          NSNumber nSNumber = new NSNumber(true);
        } else {
          NSNumber nSNumber = new NSNumber(false);
        } 
        skip();
      } else if (accept('D')) {
        skip();
        NSDate nSDate = new NSDate(readInputUntil('>'));
      } else if (accept(new char[] { 'I', 'R' })) {
        skip();
        NSNumber nSNumber = new NSNumber(readInputUntil('>'));
      } else {
        nSData = null;
      } 
      read('>');
    } else {
      String str = readInputUntil('>').replaceAll("\\s+", "");
      byte[] arrayOfByte = new byte[str.length() / 2];
      while (b < arrayOfByte.length) {
        int i = b * 2;
        arrayOfByte[b] = (byte)(byte)Integer.parseInt(str.substring(i, i + 2), 16);
        b++;
      } 
      nSData = new NSData(arrayOfByte);
      skip();
    } 
    return nSData;
  }
  
  private NSObject parseDateString() {
    String str = parseString();
    if (str.length() > 4 && str.charAt(4) == '-')
      try {
        return new NSDate(str);
      } catch (Exception exception) {} 
    return new NSString(str);
  }
  
  private NSDictionary parseDictionary() throws ParseException {
    skip();
    skipWhitespacesAndComments();
    NSDictionary nSDictionary = new NSDictionary();
    while (!accept('}')) {
      String str;
      if (accept('"')) {
        str = parseQuotedString();
      } else {
        str = parseString();
      } 
      skipWhitespacesAndComments();
      read('=');
      skipWhitespacesAndComments();
      nSDictionary.put(str, parseObject());
      skipWhitespacesAndComments();
      read(';');
      skipWhitespacesAndComments();
    } 
    skip();
    return nSDictionary;
  }
  
  private static String parseEscapedSequence(StringCharacterIterator paramStringCharacterIterator) throws UnsupportedEncodingException {
    String str1;
    char c = paramStringCharacterIterator.next();
    if (c == '\\')
      return new String(new byte[] { 0, 92 }, "UTF-8"); 
    if (c == '"')
      return new String(new byte[] { 0, 34 }, "UTF-8"); 
    if (c == 'b')
      return new String(new byte[] { 0, 8 }, "UTF-8"); 
    if (c == 'n')
      return new String(new byte[] { 0, 10 }, "UTF-8"); 
    if (c == 'r')
      return new String(new byte[] { 0, 13 }, "UTF-8"); 
    if (c == 't')
      return new String(new byte[] { 0, 9 }, "UTF-8"); 
    if (c == 'U' || c == 'u') {
      StringBuilder stringBuilder4 = new StringBuilder();
      stringBuilder4.append("");
      stringBuilder4.append(paramStringCharacterIterator.next());
      String str5 = stringBuilder4.toString();
      stringBuilder4 = new StringBuilder();
      stringBuilder4.append(str5);
      stringBuilder4.append(paramStringCharacterIterator.next());
      String str4 = stringBuilder4.toString();
      StringBuilder stringBuilder5 = new StringBuilder();
      stringBuilder5.append("");
      stringBuilder5.append(paramStringCharacterIterator.next());
      String str6 = stringBuilder5.toString();
      stringBuilder5 = new StringBuilder();
      stringBuilder5.append(str6);
      stringBuilder5.append(paramStringCharacterIterator.next());
      str1 = stringBuilder5.toString();
      return new String(new byte[] { (byte)Integer.parseInt(str4, 16), (byte)Integer.parseInt(str1, 16) }"UTF-8");
    } 
    StringBuilder stringBuilder2 = new StringBuilder();
    stringBuilder2.append("");
    stringBuilder2.append(c);
    String str2 = stringBuilder2.toString();
    StringBuilder stringBuilder3 = new StringBuilder();
    stringBuilder3.append(str2);
    stringBuilder3.append(str1.next());
    String str3 = stringBuilder3.toString();
    StringBuilder stringBuilder1 = new StringBuilder();
    stringBuilder1.append(str3);
    stringBuilder1.append(str1.next());
    return new String(new byte[] { 0, (byte)Integer.parseInt(stringBuilder1.toString(), 8) }"UTF-8");
  }
  
  private NSObject parseObject() throws ParseException {
    byte[] arrayOfByte = this.data;
    int i = this.index;
    byte b = arrayOfByte[i];
    if (b != 34)
      return (b != 40) ? ((b != 60) ? ((b != 123) ? ((arrayOfByte[i] > 47 && arrayOfByte[i] < 58) ? parseDateString() : new NSString(parseString())) : parseDictionary()) : parseData()) : parseArray(); 
    String str = parseQuotedString();
    if (str.length() == 20 && str.charAt(4) == '-')
      try {
        return new NSDate(str);
      } catch (Exception exception) {
        return new NSString(str);
      }  
    return new NSString(str);
  }
  
  private String parseQuotedString() throws ParseException {
    skip();
    String str = "";
    int i = 1;
    while (true) {
      byte[] arrayOfByte = this.data;
      int j = this.index;
      if (arrayOfByte[j] != 34 || (arrayOfByte[j - 1] == 92 && i)) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(str);
        stringBuilder.append((char)this.data[this.index]);
        str = stringBuilder.toString();
        j = i;
        if (accept('\\'))
          if (this.data[this.index - 1] != 92 || !i) {
            j = 1;
          } else {
            j = 0;
          }  
        skip();
        i = j;
        continue;
      } 
      try {
        str = parseQuotedString(str);
        skip();
        return str;
      } catch (Exception exception) {
        throw new ParseException("The quoted string could not be parsed.", this.index);
      } 
    } 
  }
  
  public static String parseQuotedString(String paramString) throws UnsupportedEncodingException, CharacterCodingException {
    // Byte code:
    //   0: ldc com/dd/plist/ASCIIPropertyListParser
    //   2: monitorenter
    //   3: new java/util/LinkedList
    //   6: astore_1
    //   7: aload_1
    //   8: invokespecial <init> : ()V
    //   11: new java/text/StringCharacterIterator
    //   14: astore_2
    //   15: aload_2
    //   16: aload_0
    //   17: invokespecial <init> : (Ljava/lang/String;)V
    //   20: aload_2
    //   21: invokevirtual current : ()C
    //   24: istore_3
    //   25: aload_2
    //   26: invokevirtual getIndex : ()I
    //   29: istore #4
    //   31: aload_2
    //   32: invokevirtual getEndIndex : ()I
    //   35: istore #5
    //   37: iconst_0
    //   38: istore #6
    //   40: iconst_0
    //   41: istore #7
    //   43: iload #4
    //   45: iload #5
    //   47: if_icmpge -> 133
    //   50: iload_3
    //   51: bipush #92
    //   53: if_icmpeq -> 82
    //   56: aload_1
    //   57: iconst_0
    //   58: invokestatic valueOf : (B)Ljava/lang/Byte;
    //   61: invokeinterface add : (Ljava/lang/Object;)Z
    //   66: pop
    //   67: aload_1
    //   68: iload_3
    //   69: i2b
    //   70: invokestatic valueOf : (B)Ljava/lang/Byte;
    //   73: invokeinterface add : (Ljava/lang/Object;)Z
    //   78: pop
    //   79: goto -> 125
    //   82: aload_2
    //   83: invokestatic parseEscapedSequence : (Ljava/text/StringCharacterIterator;)Ljava/lang/String;
    //   86: ldc_w 'UTF-8'
    //   89: invokevirtual getBytes : (Ljava/lang/String;)[B
    //   92: astore_0
    //   93: aload_0
    //   94: arraylength
    //   95: istore #6
    //   97: iload #7
    //   99: istore_3
    //   100: iload_3
    //   101: iload #6
    //   103: if_icmpge -> 125
    //   106: aload_1
    //   107: aload_0
    //   108: iload_3
    //   109: baload
    //   110: invokestatic valueOf : (B)Ljava/lang/Byte;
    //   113: invokeinterface add : (Ljava/lang/Object;)Z
    //   118: pop
    //   119: iinc #3, 1
    //   122: goto -> 100
    //   125: aload_2
    //   126: invokevirtual next : ()C
    //   129: istore_3
    //   130: goto -> 25
    //   133: aload_1
    //   134: invokeinterface size : ()I
    //   139: newarray byte
    //   141: astore_0
    //   142: aload_1
    //   143: invokeinterface iterator : ()Ljava/util/Iterator;
    //   148: astore_1
    //   149: iload #6
    //   151: istore_3
    //   152: aload_1
    //   153: invokeinterface hasNext : ()Z
    //   158: ifeq -> 182
    //   161: aload_0
    //   162: iload_3
    //   163: aload_1
    //   164: invokeinterface next : ()Ljava/lang/Object;
    //   169: checkcast java/lang/Byte
    //   172: invokevirtual byteValue : ()B
    //   175: bastore
    //   176: iinc #3, 1
    //   179: goto -> 152
    //   182: new java/lang/String
    //   185: astore_1
    //   186: aload_1
    //   187: aload_0
    //   188: ldc_w 'UTF-8'
    //   191: invokespecial <init> : ([BLjava/lang/String;)V
    //   194: aload_1
    //   195: invokestatic wrap : (Ljava/lang/CharSequence;)Ljava/nio/CharBuffer;
    //   198: astore_0
    //   199: getstatic com/dd/plist/ASCIIPropertyListParser.asciiEncoder : Ljava/nio/charset/CharsetEncoder;
    //   202: ifnonnull -> 217
    //   205: ldc_w 'ASCII'
    //   208: invokestatic forName : (Ljava/lang/String;)Ljava/nio/charset/Charset;
    //   211: invokevirtual newEncoder : ()Ljava/nio/charset/CharsetEncoder;
    //   214: putstatic com/dd/plist/ASCIIPropertyListParser.asciiEncoder : Ljava/nio/charset/CharsetEncoder;
    //   217: getstatic com/dd/plist/ASCIIPropertyListParser.asciiEncoder : Ljava/nio/charset/CharsetEncoder;
    //   220: aload_0
    //   221: invokevirtual canEncode : (Ljava/lang/CharSequence;)Z
    //   224: ifeq -> 246
    //   227: getstatic com/dd/plist/ASCIIPropertyListParser.asciiEncoder : Ljava/nio/charset/CharsetEncoder;
    //   230: aload_0
    //   231: invokevirtual encode : (Ljava/nio/CharBuffer;)Ljava/nio/ByteBuffer;
    //   234: invokevirtual asCharBuffer : ()Ljava/nio/CharBuffer;
    //   237: invokevirtual toString : ()Ljava/lang/String;
    //   240: astore_0
    //   241: ldc com/dd/plist/ASCIIPropertyListParser
    //   243: monitorexit
    //   244: aload_0
    //   245: areturn
    //   246: ldc com/dd/plist/ASCIIPropertyListParser
    //   248: monitorexit
    //   249: aload_1
    //   250: areturn
    //   251: astore_0
    //   252: ldc com/dd/plist/ASCIIPropertyListParser
    //   254: monitorexit
    //   255: goto -> 260
    //   258: aload_0
    //   259: athrow
    //   260: goto -> 258
    // Exception table:
    //   from	to	target	type
    //   3	25	251	finally
    //   25	37	251	finally
    //   56	79	251	finally
    //   82	97	251	finally
    //   106	119	251	finally
    //   125	130	251	finally
    //   133	149	251	finally
    //   152	176	251	finally
    //   182	217	251	finally
    //   217	241	251	finally
  }
  
  private String parseString() {
    return readInputUntil(new char[] { ' ', '\t', '\n', '\r', ',', ';', '=', ')' });
  }
  
  private void read(char paramChar) throws ParseException {
    expect(paramChar);
    this.index++;
  }
  
  private String readInputUntil(char paramChar) {
    String str = "";
    while (!accept(paramChar)) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(str);
      stringBuilder.append((char)this.data[this.index]);
      str = stringBuilder.toString();
      skip();
    } 
    return str;
  }
  
  private String readInputUntil(char... paramVarArgs) {
    String str = "";
    while (!accept(paramVarArgs)) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(str);
      stringBuilder.append((char)this.data[this.index]);
      str = stringBuilder.toString();
      skip();
    } 
    return str;
  }
  
  private void skip() {
    this.index++;
  }
  
  private void skip(int paramInt) {
    this.index += paramInt;
  }
  
  private void skipWhitespacesAndComments() {
    boolean bool;
    do {
      while (accept(new char[] { '\r', '\n', ' ', '\t' }))
        skip(); 
      boolean bool1 = acceptSequence(new char[] { '/', '/' });
      bool = true;
      if (bool1) {
        skip(2);
        readInputUntil(new char[] { '\r', '\n' });
      } else if (acceptSequence(new char[] { '/', '*' })) {
        skip(2);
        while (true) {
          if (acceptSequence(new char[] { '*', '/' })) {
            skip(2);
            break;
          } 
          skip();
        } 
      } else {
        bool = false;
      } 
    } while (bool);
  }
  
  public NSObject parse() throws ParseException {
    this.index = 0;
    byte[] arrayOfByte = this.data;
    if (arrayOfByte.length >= 3 && (arrayOfByte[0] & 0xFF) == 239 && (arrayOfByte[1] & 0xFF) == 187 && (arrayOfByte[2] & 0xFF) == 191)
      skip(3); 
    skipWhitespacesAndComments();
    expect(new char[] { '{', '(', '/' });
    try {
      return parseObject();
    } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
      throw new ParseException("Reached end of input unexpectedly.", this.index);
    } 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/dd/plist/ASCIIPropertyListParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */