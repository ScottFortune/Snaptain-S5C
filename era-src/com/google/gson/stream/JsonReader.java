package com.google.gson.stream;

import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.internal.bind.JsonTreeReader;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;

public class JsonReader implements Closeable {
  private static final long MIN_INCOMPLETE_INTEGER = -922337203685477580L;
  
  private static final char[] NON_EXECUTE_PREFIX = ")]}'\n".toCharArray();
  
  private static final int NUMBER_CHAR_DECIMAL = 3;
  
  private static final int NUMBER_CHAR_DIGIT = 2;
  
  private static final int NUMBER_CHAR_EXP_DIGIT = 7;
  
  private static final int NUMBER_CHAR_EXP_E = 5;
  
  private static final int NUMBER_CHAR_EXP_SIGN = 6;
  
  private static final int NUMBER_CHAR_FRACTION_DIGIT = 4;
  
  private static final int NUMBER_CHAR_NONE = 0;
  
  private static final int NUMBER_CHAR_SIGN = 1;
  
  private static final int PEEKED_BEGIN_ARRAY = 3;
  
  private static final int PEEKED_BEGIN_OBJECT = 1;
  
  private static final int PEEKED_BUFFERED = 11;
  
  private static final int PEEKED_DOUBLE_QUOTED = 9;
  
  private static final int PEEKED_DOUBLE_QUOTED_NAME = 13;
  
  private static final int PEEKED_END_ARRAY = 4;
  
  private static final int PEEKED_END_OBJECT = 2;
  
  private static final int PEEKED_EOF = 17;
  
  private static final int PEEKED_FALSE = 6;
  
  private static final int PEEKED_LONG = 15;
  
  private static final int PEEKED_NONE = 0;
  
  private static final int PEEKED_NULL = 7;
  
  private static final int PEEKED_NUMBER = 16;
  
  private static final int PEEKED_SINGLE_QUOTED = 8;
  
  private static final int PEEKED_SINGLE_QUOTED_NAME = 12;
  
  private static final int PEEKED_TRUE = 5;
  
  private static final int PEEKED_UNQUOTED = 10;
  
  private static final int PEEKED_UNQUOTED_NAME = 14;
  
  private final char[] buffer = new char[1024];
  
  private final Reader in;
  
  private boolean lenient = false;
  
  private int limit = 0;
  
  private int lineNumber = 0;
  
  private int lineStart = 0;
  
  private int[] pathIndices;
  
  private String[] pathNames;
  
  int peeked = 0;
  
  private long peekedLong;
  
  private int peekedNumberLength;
  
  private String peekedString;
  
  private int pos = 0;
  
  private int[] stack = new int[32];
  
  private int stackSize = 0;
  
  static {
    JsonReaderInternalAccess.INSTANCE = new JsonReaderInternalAccess() {
        public void promoteNameToValue(JsonReader param1JsonReader) throws IOException {
          if (param1JsonReader instanceof JsonTreeReader) {
            ((JsonTreeReader)param1JsonReader).promoteNameToValue();
            return;
          } 
          int i = param1JsonReader.peeked;
          int j = i;
          if (i == 0)
            j = param1JsonReader.doPeek(); 
          if (j == 13) {
            param1JsonReader.peeked = 9;
          } else if (j == 12) {
            param1JsonReader.peeked = 8;
          } else {
            if (j == 14) {
              param1JsonReader.peeked = 10;
              return;
            } 
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Expected a name but was ");
            stringBuilder.append(param1JsonReader.peek());
            stringBuilder.append(param1JsonReader.locationString());
            throw new IllegalStateException(stringBuilder.toString());
          } 
        }
      };
  }
  
  public JsonReader(Reader paramReader) {
    int[] arrayOfInt = this.stack;
    int i = this.stackSize;
    this.stackSize = i + 1;
    arrayOfInt[i] = 6;
    this.pathNames = new String[32];
    this.pathIndices = new int[32];
    if (paramReader != null) {
      this.in = paramReader;
      return;
    } 
    throw new NullPointerException("in == null");
  }
  
  private void checkLenient() throws IOException {
    if (this.lenient)
      return; 
    throw syntaxError("Use JsonReader.setLenient(true) to accept malformed JSON");
  }
  
  private void consumeNonExecutePrefix() throws IOException {
    nextNonWhitespace(true);
    int i = --this.pos;
    char[] arrayOfChar = NON_EXECUTE_PREFIX;
    if (i + arrayOfChar.length > this.limit && !fillBuffer(arrayOfChar.length))
      return; 
    i = 0;
    while (true) {
      arrayOfChar = NON_EXECUTE_PREFIX;
      if (i < arrayOfChar.length) {
        if (this.buffer[this.pos + i] != arrayOfChar[i])
          return; 
        i++;
        continue;
      } 
      this.pos += arrayOfChar.length;
      return;
    } 
  }
  
  private boolean fillBuffer(int paramInt) throws IOException {
    char[] arrayOfChar = this.buffer;
    int i = this.lineStart;
    int j = this.pos;
    this.lineStart = i - j;
    i = this.limit;
    if (i != j) {
      this.limit = i - j;
      System.arraycopy(arrayOfChar, j, arrayOfChar, 0, this.limit);
    } else {
      this.limit = 0;
    } 
    this.pos = 0;
    while (true) {
      Reader reader = this.in;
      j = this.limit;
      j = reader.read(arrayOfChar, j, arrayOfChar.length - j);
      if (j != -1) {
        this.limit += j;
        j = paramInt;
        if (this.lineNumber == 0) {
          i = this.lineStart;
          j = paramInt;
          if (i == 0) {
            j = paramInt;
            if (this.limit > 0) {
              j = paramInt;
              if (arrayOfChar[0] == '﻿') {
                this.pos++;
                this.lineStart = i + 1;
                j = paramInt + 1;
              } 
            } 
          } 
        } 
        paramInt = j;
        if (this.limit >= j)
          return true; 
        continue;
      } 
      return false;
    } 
  }
  
  private boolean isLiteral(char paramChar) throws IOException {
    if (paramChar != '\t' && paramChar != '\n' && paramChar != '\f' && paramChar != '\r' && paramChar != ' ')
      if (paramChar != '#') {
        if (paramChar != ',')
          if (paramChar != '/' && paramChar != '=') {
            if (paramChar != '{' && paramChar != '}' && paramChar != ':')
              if (paramChar != ';') {
                switch (paramChar) {
                  default:
                    return true;
                  case '\\':
                    checkLenient();
                    break;
                  case '[':
                  case ']':
                    break;
                } 
                return false;
              }  
            return false;
          }  
        return false;
      }  
    return false;
  }
  
  private int nextNonWhitespace(boolean paramBoolean) throws IOException {
    char[] arrayOfChar = this.buffer;
    int i = this.pos;
    int j = this.limit;
    while (true) {
      StringBuilder stringBuilder1;
      StringBuilder stringBuilder3;
      int k = i;
      int m = j;
      if (i == j) {
        this.pos = i;
        if (!fillBuffer(1)) {
          if (!paramBoolean)
            return -1; 
          stringBuilder1 = new StringBuilder();
          stringBuilder1.append("End of input");
          stringBuilder1.append(locationString());
          throw new EOFException(stringBuilder1.toString());
        } 
        k = this.pos;
        m = this.limit;
      } 
      i = k + 1;
      StringBuilder stringBuilder2 = stringBuilder1[k];
      if (stringBuilder2 == 10) {
        this.lineNumber++;
        this.lineStart = i;
      } else if (stringBuilder2 != 32 && stringBuilder2 != 13 && stringBuilder2 != 9) {
        int n;
        if (stringBuilder2 == 47) {
          this.pos = i;
          if (i == m) {
            this.pos--;
            boolean bool = fillBuffer(2);
            this.pos++;
            if (!bool)
              return stringBuilder2; 
          } 
          checkLenient();
          i = this.pos;
          stringBuilder3 = stringBuilder1[i];
          if (stringBuilder3 != 42) {
            if (stringBuilder3 != 47)
              return stringBuilder2; 
            this.pos = i + 1;
            skipToEndOfLine();
            i = this.pos;
            n = this.limit;
            continue;
          } 
          this.pos = i + 1;
          if (skipTo("*/")) {
            i = this.pos + 2;
            n = this.limit;
            continue;
          } 
          throw syntaxError("Unterminated comment");
        } 
        if (n == 35) {
          this.pos = i;
          checkLenient();
          skipToEndOfLine();
          i = this.pos;
          n = this.limit;
          continue;
        } 
        this.pos = i;
        return n;
      } 
      stringBuilder2 = stringBuilder3;
    } 
  }
  
  private String nextQuotedValue(char paramChar) throws IOException {
    char[] arrayOfChar = this.buffer;
    for (StringBuilder stringBuilder = null;; stringBuilder = stringBuilder1) {
      StringBuilder stringBuilder1;
      int i = this.pos;
      int j = this.limit;
      label34: while (true) {
        int k = i;
        while (true) {
          int m = k;
          if (m < j) {
            k = m + 1;
            m = arrayOfChar[m];
            if (m == paramChar) {
              this.pos = k;
              int n = k - i - 1;
              if (stringBuilder == null)
                return new String(arrayOfChar, i, n); 
              stringBuilder.append(arrayOfChar, i, n);
              return stringBuilder.toString();
            } 
            if (m == 92) {
              this.pos = k;
              k = k - i - 1;
              StringBuilder stringBuilder2 = stringBuilder;
              if (stringBuilder == null)
                stringBuilder2 = new StringBuilder(Math.max((k + 1) * 2, 16)); 
              stringBuilder2.append(arrayOfChar, i, k);
              stringBuilder2.append(readEscapeCharacter());
              i = this.pos;
              j = this.limit;
              stringBuilder = stringBuilder2;
              continue label34;
            } 
            if (m == 10) {
              this.lineNumber++;
              this.lineStart = k;
            } 
            continue;
          } 
          stringBuilder1 = stringBuilder;
          if (stringBuilder == null)
            stringBuilder1 = new StringBuilder(Math.max((m - i) * 2, 16)); 
          stringBuilder1.append(arrayOfChar, i, m - i);
          this.pos = m;
          if (fillBuffer(1))
            break; 
          IOException iOException = syntaxError("Unterminated string");
          throw iOException;
        } 
        break;
      } 
    } 
  }
  
  private String nextUnquotedValue() throws IOException {
    String str;
    byte b;
    boolean bool = false;
    StringBuilder stringBuilder = null;
    while (true) {
      b = 0;
      while (true) {
        int i = this.pos;
        if (i + b < this.limit) {
          i = this.buffer[i + b];
          if (i != 9 && i != 10 && i != 12 && i != 13 && i != 32)
            if (i != 35) {
              if (i != 44)
                if (i != 47 && i != 61) {
                  if (i != 123 && i != 125 && i != 58)
                    if (i != 59) {
                      switch (i) {
                        case 92:
                          checkLenient();
                          break;
                        case 91:
                        case 93:
                          break;
                      } 
                      continue;
                    }  
                  break;
                }  
              break;
            }  
          break;
        } 
        if (b < this.buffer.length) {
          if (fillBuffer(b + 1))
            continue; 
          break;
        } 
        StringBuilder stringBuilder1 = stringBuilder;
        if (stringBuilder == null)
          stringBuilder1 = new StringBuilder(Math.max(b, 16)); 
        stringBuilder1.append(this.buffer, this.pos, b);
        this.pos += b;
        stringBuilder = stringBuilder1;
        if (!fillBuffer(1)) {
          stringBuilder = stringBuilder1;
          b = bool;
          break;
        } 
      } 
      break;
    } 
    if (stringBuilder == null) {
      str = new String(this.buffer, this.pos, b);
    } else {
      str.append(this.buffer, this.pos, b);
      str = str.toString();
    } 
    this.pos += b;
    return str;
  }
  
  private int peekKeyword() throws IOException {
    String str1;
    String str2;
    char c = this.buffer[this.pos];
    if (c == 't' || c == 'T') {
      c = '\005';
      str1 = "true";
      str2 = "TRUE";
    } else if (c == 'f' || c == 'F') {
      c = '\006';
      str1 = "false";
      str2 = "FALSE";
    } else if (c == 'n' || c == 'N') {
      c = '\007';
      str1 = "null";
      str2 = "NULL";
    } else {
      return 0;
    } 
    int i = str1.length();
    for (byte b = 1; b < i; b++) {
      if (this.pos + b >= this.limit && !fillBuffer(b + 1))
        return 0; 
      char c1 = this.buffer[this.pos + b];
      if (c1 != str1.charAt(b) && c1 != str2.charAt(b))
        return 0; 
    } 
    if ((this.pos + i < this.limit || fillBuffer(i + 1)) && isLiteral(this.buffer[this.pos + i]))
      return 0; 
    this.pos += i;
    this.peeked = c;
    return c;
  }
  
  private int peekNumber() throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: getfield buffer : [C
    //   4: astore_1
    //   5: aload_0
    //   6: getfield pos : I
    //   9: istore_2
    //   10: aload_0
    //   11: getfield limit : I
    //   14: istore_3
    //   15: iconst_0
    //   16: istore #4
    //   18: iconst_0
    //   19: istore #5
    //   21: iconst_1
    //   22: istore #6
    //   24: lconst_0
    //   25: lstore #7
    //   27: iconst_0
    //   28: istore #9
    //   30: iload_2
    //   31: istore #10
    //   33: iload_3
    //   34: istore #11
    //   36: iload_2
    //   37: iload #4
    //   39: iadd
    //   40: iload_3
    //   41: if_icmpne -> 79
    //   44: iload #4
    //   46: aload_1
    //   47: arraylength
    //   48: if_icmpne -> 53
    //   51: iconst_0
    //   52: ireturn
    //   53: aload_0
    //   54: iload #4
    //   56: iconst_1
    //   57: iadd
    //   58: invokespecial fillBuffer : (I)Z
    //   61: ifne -> 67
    //   64: goto -> 304
    //   67: aload_0
    //   68: getfield pos : I
    //   71: istore #10
    //   73: aload_0
    //   74: getfield limit : I
    //   77: istore #11
    //   79: aload_1
    //   80: iload #10
    //   82: iload #4
    //   84: iadd
    //   85: caload
    //   86: istore #12
    //   88: iload #12
    //   90: bipush #43
    //   92: if_icmpeq -> 483
    //   95: iload #12
    //   97: bipush #69
    //   99: if_icmpeq -> 460
    //   102: iload #12
    //   104: bipush #101
    //   106: if_icmpeq -> 460
    //   109: iload #12
    //   111: bipush #45
    //   113: if_icmpeq -> 435
    //   116: iload #12
    //   118: bipush #46
    //   120: if_icmpeq -> 421
    //   123: iload #12
    //   125: bipush #48
    //   127: if_icmplt -> 295
    //   130: iload #12
    //   132: bipush #57
    //   134: if_icmple -> 140
    //   137: goto -> 295
    //   140: iload #5
    //   142: iconst_1
    //   143: if_icmpeq -> 280
    //   146: iload #5
    //   148: ifne -> 154
    //   151: goto -> 280
    //   154: iload #5
    //   156: iconst_2
    //   157: if_icmpne -> 228
    //   160: lload #7
    //   162: lconst_0
    //   163: lcmp
    //   164: ifne -> 169
    //   167: iconst_0
    //   168: ireturn
    //   169: ldc2_w 10
    //   172: lload #7
    //   174: lmul
    //   175: iload #12
    //   177: bipush #48
    //   179: isub
    //   180: i2l
    //   181: lsub
    //   182: lstore #13
    //   184: lload #7
    //   186: ldc2_w -922337203685477580
    //   189: lcmp
    //   190: ifgt -> 218
    //   193: lload #7
    //   195: ldc2_w -922337203685477580
    //   198: lcmp
    //   199: ifne -> 213
    //   202: lload #13
    //   204: lload #7
    //   206: lcmp
    //   207: ifge -> 213
    //   210: goto -> 218
    //   213: iconst_0
    //   214: istore_3
    //   215: goto -> 220
    //   218: iconst_1
    //   219: istore_3
    //   220: iload_3
    //   221: iload #6
    //   223: iand
    //   224: istore_3
    //   225: goto -> 263
    //   228: iload #5
    //   230: iconst_3
    //   231: if_icmpne -> 240
    //   234: iconst_4
    //   235: istore #5
    //   237: goto -> 292
    //   240: iload #5
    //   242: iconst_5
    //   243: if_icmpeq -> 273
    //   246: iload #6
    //   248: istore_3
    //   249: lload #7
    //   251: lstore #13
    //   253: iload #5
    //   255: bipush #6
    //   257: if_icmpne -> 263
    //   260: goto -> 273
    //   263: iload_3
    //   264: istore #6
    //   266: lload #13
    //   268: lstore #7
    //   270: goto -> 292
    //   273: bipush #7
    //   275: istore #5
    //   277: goto -> 292
    //   280: iload #12
    //   282: bipush #48
    //   284: isub
    //   285: ineg
    //   286: i2l
    //   287: lstore #7
    //   289: iconst_2
    //   290: istore #5
    //   292: goto -> 493
    //   295: aload_0
    //   296: iload #12
    //   298: invokespecial isLiteral : (C)Z
    //   301: ifne -> 419
    //   304: iload #5
    //   306: iconst_2
    //   307: if_icmpne -> 380
    //   310: iload #6
    //   312: ifeq -> 380
    //   315: lload #7
    //   317: ldc2_w -9223372036854775808
    //   320: lcmp
    //   321: ifne -> 329
    //   324: iload #9
    //   326: ifeq -> 380
    //   329: lload #7
    //   331: lconst_0
    //   332: lcmp
    //   333: ifne -> 341
    //   336: iload #9
    //   338: ifne -> 380
    //   341: iload #9
    //   343: ifeq -> 349
    //   346: goto -> 354
    //   349: lload #7
    //   351: lneg
    //   352: lstore #7
    //   354: aload_0
    //   355: lload #7
    //   357: putfield peekedLong : J
    //   360: aload_0
    //   361: aload_0
    //   362: getfield pos : I
    //   365: iload #4
    //   367: iadd
    //   368: putfield pos : I
    //   371: aload_0
    //   372: bipush #15
    //   374: putfield peeked : I
    //   377: bipush #15
    //   379: ireturn
    //   380: iload #5
    //   382: iconst_2
    //   383: if_icmpeq -> 404
    //   386: iload #5
    //   388: iconst_4
    //   389: if_icmpeq -> 404
    //   392: iload #5
    //   394: bipush #7
    //   396: if_icmpne -> 402
    //   399: goto -> 404
    //   402: iconst_0
    //   403: ireturn
    //   404: aload_0
    //   405: iload #4
    //   407: putfield peekedNumberLength : I
    //   410: aload_0
    //   411: bipush #16
    //   413: putfield peeked : I
    //   416: bipush #16
    //   418: ireturn
    //   419: iconst_0
    //   420: ireturn
    //   421: iload #5
    //   423: iconst_2
    //   424: if_icmpne -> 433
    //   427: iconst_3
    //   428: istore #5
    //   430: goto -> 493
    //   433: iconst_0
    //   434: ireturn
    //   435: iload #5
    //   437: ifne -> 449
    //   440: iconst_1
    //   441: istore #5
    //   443: iconst_1
    //   444: istore #9
    //   446: goto -> 493
    //   449: iload #5
    //   451: iconst_5
    //   452: if_icmpne -> 458
    //   455: goto -> 489
    //   458: iconst_0
    //   459: ireturn
    //   460: iload #5
    //   462: iconst_2
    //   463: if_icmpeq -> 477
    //   466: iload #5
    //   468: iconst_4
    //   469: if_icmpne -> 475
    //   472: goto -> 477
    //   475: iconst_0
    //   476: ireturn
    //   477: iconst_5
    //   478: istore #5
    //   480: goto -> 493
    //   483: iload #5
    //   485: iconst_5
    //   486: if_icmpne -> 505
    //   489: bipush #6
    //   491: istore #5
    //   493: iinc #4, 1
    //   496: iload #10
    //   498: istore_2
    //   499: iload #11
    //   501: istore_3
    //   502: goto -> 30
    //   505: iconst_0
    //   506: ireturn
  }
  
  private void push(int paramInt) {
    int i = this.stackSize;
    int[] arrayOfInt = this.stack;
    if (i == arrayOfInt.length) {
      i *= 2;
      this.stack = Arrays.copyOf(arrayOfInt, i);
      this.pathIndices = Arrays.copyOf(this.pathIndices, i);
      this.pathNames = Arrays.<String>copyOf(this.pathNames, i);
    } 
    arrayOfInt = this.stack;
    i = this.stackSize;
    this.stackSize = i + 1;
    arrayOfInt[i] = paramInt;
  }
  
  private char readEscapeCharacter() throws IOException {
    if (this.pos != this.limit || fillBuffer(1)) {
      char c;
      char[] arrayOfChar = this.buffer;
      int i = this.pos;
      this.pos = i + 1;
      int j = arrayOfChar[i];
      if (j != 10) {
        if (j != 34 && j != 39 && j != 47 && j != 92) {
          if (j != 98) {
            if (j != 102) {
              if (j != 110) {
                if (j != 114) {
                  if (j != 116) {
                    if (j == 117) {
                      if (this.pos + 4 <= this.limit || fillBuffer(4)) {
                        int k = 0;
                        int m = this.pos;
                        i = m;
                        j = k;
                        while (true) {
                          k = i;
                          if (k < m + 4) {
                            char c1 = this.buffer[k];
                            char c2 = (char)(j << 4);
                            if (c1 >= '0' && c1 <= '9') {
                              c1 -= '0';
                            } else {
                              if (c1 >= 'a' && c1 <= 'f') {
                                c1 -= 'a';
                              } else if (c1 >= 'A') {
                                if (c1 <= 'F') {
                                  c1 -= 'A';
                                } else {
                                  StringBuilder stringBuilder = new StringBuilder();
                                  stringBuilder.append("\\u");
                                  stringBuilder.append(new String(this.buffer, this.pos, 4));
                                  throw new NumberFormatException(stringBuilder.toString());
                                } 
                              } else {
                                continue;
                              } 
                              c1 += '\n';
                            } 
                            c2 = (char)(c2 + c1);
                            int n = k + 1;
                            c = c2;
                            continue;
                          } 
                          this.pos += 4;
                          return c;
                        } 
                      } 
                      throw syntaxError("Unterminated escape sequence");
                    } 
                    throw syntaxError("Invalid escape sequence");
                  } 
                  return '\t';
                } 
                return '\r';
              } 
              return '\n';
            } 
            return '\f';
          } 
          return '\b';
        } 
      } else {
        this.lineNumber++;
        this.lineStart = this.pos;
      } 
      return c;
    } 
    throw syntaxError("Unterminated escape sequence");
  }
  
  private void skipQuotedValue(char paramChar) throws IOException {
    char[] arrayOfChar = this.buffer;
    while (true) {
      int i = this.pos;
      int j = this.limit;
      while (i < j) {
        int k = i + 1;
        i = arrayOfChar[i];
        if (i == paramChar) {
          this.pos = k;
          return;
        } 
        if (i == 92) {
          this.pos = k;
          readEscapeCharacter();
          i = this.pos;
          j = this.limit;
          continue;
        } 
        if (i == 10) {
          this.lineNumber++;
          this.lineStart = k;
        } 
        i = k;
      } 
      this.pos = i;
      if (fillBuffer(1))
        continue; 
      IOException iOException = syntaxError("Unterminated string");
      throw iOException;
    } 
  }
  
  private boolean skipTo(String paramString) throws IOException {
    int i = paramString.length();
    label20: while (true) {
      int j = this.pos;
      int k = this.limit;
      byte b = 0;
      if (j + i <= k || fillBuffer(i)) {
        char[] arrayOfChar = this.buffer;
        j = this.pos;
        if (arrayOfChar[j] == '\n') {
          this.lineNumber++;
          this.lineStart = j + 1;
          continue;
        } 
        while (b < i) {
          if (this.buffer[this.pos + b] != paramString.charAt(b)) {
            this.pos++;
            continue label20;
          } 
          b++;
        } 
        return true;
      } 
      return false;
    } 
  }
  
  private void skipToEndOfLine() throws IOException {
    while (this.pos < this.limit || fillBuffer(1)) {
      char[] arrayOfChar = this.buffer;
      int i = this.pos;
      this.pos = i + 1;
      i = arrayOfChar[i];
      if (i == 10) {
        this.lineNumber++;
        this.lineStart = this.pos;
        break;
      } 
      if (i == 13)
        break; 
    } 
  }
  
  private void skipUnquotedValue() throws IOException {
    do {
      byte b = 0;
      while (true) {
        int i = this.pos;
        if (i + b < this.limit) {
          i = this.buffer[i + b];
          if (i != 9 && i != 10 && i != 12 && i != 13 && i != 32)
            if (i != 35) {
              if (i != 44)
                if (i != 47 && i != 61) {
                  if (i != 123 && i != 125 && i != 58)
                    if (i != 59) {
                      switch (i) {
                        default:
                          b++;
                          continue;
                        case 92:
                          checkLenient();
                          break;
                        case 91:
                        case 93:
                          break;
                      } 
                    } else {
                    
                    }  
                } else {
                
                }  
            } else {
            
            }  
          this.pos += b;
          return;
        } 
        this.pos = i + b;
        break;
      } 
    } while (fillBuffer(1));
  }
  
  private IOException syntaxError(String paramString) throws IOException {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(paramString);
    stringBuilder.append(locationString());
    throw new MalformedJsonException(stringBuilder.toString());
  }
  
  public void beginArray() throws IOException {
    int i = this.peeked;
    int j = i;
    if (i == 0)
      j = doPeek(); 
    if (j == 3) {
      push(1);
      this.pathIndices[this.stackSize - 1] = 0;
      this.peeked = 0;
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Expected BEGIN_ARRAY but was ");
    stringBuilder.append(peek());
    stringBuilder.append(locationString());
    throw new IllegalStateException(stringBuilder.toString());
  }
  
  public void beginObject() throws IOException {
    int i = this.peeked;
    int j = i;
    if (i == 0)
      j = doPeek(); 
    if (j == 1) {
      push(3);
      this.peeked = 0;
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Expected BEGIN_OBJECT but was ");
    stringBuilder.append(peek());
    stringBuilder.append(locationString());
    throw new IllegalStateException(stringBuilder.toString());
  }
  
  public void close() throws IOException {
    this.peeked = 0;
    this.stack[0] = 8;
    this.stackSize = 1;
    this.in.close();
  }
  
  int doPeek() throws IOException {
    int[] arrayOfInt = this.stack;
    int i = this.stackSize;
    int j = arrayOfInt[i - 1];
    if (j == 1) {
      arrayOfInt[i - 1] = 2;
    } else if (j == 2) {
      i = nextNonWhitespace(true);
      if (i != 44) {
        if (i != 59) {
          if (i == 93) {
            this.peeked = 4;
            return 4;
          } 
          throw syntaxError("Unterminated array");
        } 
        checkLenient();
      } 
    } else {
      if (j == 3 || j == 5) {
        this.stack[this.stackSize - 1] = 4;
        if (j == 5) {
          i = nextNonWhitespace(true);
          if (i != 44) {
            if (i != 59) {
              if (i == 125) {
                this.peeked = 2;
                return 2;
              } 
              throw syntaxError("Unterminated object");
            } 
            checkLenient();
          } 
        } 
        i = nextNonWhitespace(true);
        if (i != 34) {
          if (i != 39) {
            if (i != 125) {
              checkLenient();
              this.pos--;
              if (isLiteral((char)i)) {
                this.peeked = 14;
                return 14;
              } 
              throw syntaxError("Expected name");
            } 
            if (j != 5) {
              this.peeked = 2;
              return 2;
            } 
            throw syntaxError("Expected name");
          } 
          checkLenient();
          this.peeked = 12;
          return 12;
        } 
        this.peeked = 13;
        return 13;
      } 
      if (j == 4) {
        arrayOfInt[i - 1] = 5;
        i = nextNonWhitespace(true);
        if (i != 58)
          if (i == 61) {
            checkLenient();
            if (this.pos < this.limit || fillBuffer(1)) {
              char[] arrayOfChar = this.buffer;
              i = this.pos;
              if (arrayOfChar[i] == '>')
                this.pos = i + 1; 
            } 
          } else {
            throw syntaxError("Expected ':'");
          }  
      } else if (j == 6) {
        if (this.lenient)
          consumeNonExecutePrefix(); 
        this.stack[this.stackSize - 1] = 7;
      } else if (j == 7) {
        if (nextNonWhitespace(false) == -1) {
          this.peeked = 17;
          return 17;
        } 
        checkLenient();
        this.pos--;
      } else if (j == 8) {
        throw new IllegalStateException("JsonReader is closed");
      } 
    } 
    i = nextNonWhitespace(true);
    if (i != 34) {
      if (i != 39) {
        if (i != 44 && i != 59)
          if (i != 91) {
            if (i != 93) {
              if (i != 123) {
                this.pos--;
                j = peekKeyword();
                if (j != 0)
                  return j; 
                j = peekNumber();
                if (j != 0)
                  return j; 
                if (isLiteral(this.buffer[this.pos])) {
                  checkLenient();
                  this.peeked = 10;
                  return 10;
                } 
                throw syntaxError("Expected value");
              } 
              this.peeked = 1;
              return 1;
            } 
            if (j == 1) {
              this.peeked = 4;
              return 4;
            } 
          } else {
            this.peeked = 3;
            return 3;
          }  
        if (j == 1 || j == 2) {
          checkLenient();
          this.pos--;
          this.peeked = 7;
          return 7;
        } 
        throw syntaxError("Unexpected value");
      } 
      checkLenient();
      this.peeked = 8;
      return 8;
    } 
    this.peeked = 9;
    return 9;
  }
  
  public void endArray() throws IOException {
    int i = this.peeked;
    int j = i;
    if (i == 0)
      j = doPeek(); 
    if (j == 4) {
      this.stackSize--;
      int[] arrayOfInt = this.pathIndices;
      j = this.stackSize - 1;
      arrayOfInt[j] = arrayOfInt[j] + 1;
      this.peeked = 0;
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Expected END_ARRAY but was ");
    stringBuilder.append(peek());
    stringBuilder.append(locationString());
    throw new IllegalStateException(stringBuilder.toString());
  }
  
  public void endObject() throws IOException {
    int i = this.peeked;
    int j = i;
    if (i == 0)
      j = doPeek(); 
    if (j == 2) {
      this.stackSize--;
      String[] arrayOfString = this.pathNames;
      j = this.stackSize;
      arrayOfString[j] = null;
      int[] arrayOfInt = this.pathIndices;
      arrayOfInt[--j] = arrayOfInt[j] + 1;
      this.peeked = 0;
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Expected END_OBJECT but was ");
    stringBuilder.append(peek());
    stringBuilder.append(locationString());
    throw new IllegalStateException(stringBuilder.toString());
  }
  
  public String getPath() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append('$');
    int i = this.stackSize;
    for (byte b = 0; b < i; b++) {
      int j = this.stack[b];
      if (j != 1 && j != 2) {
        if (j == 3 || j == 4 || j == 5) {
          stringBuilder.append('.');
          String[] arrayOfString = this.pathNames;
          if (arrayOfString[b] != null)
            stringBuilder.append(arrayOfString[b]); 
        } 
      } else {
        stringBuilder.append('[');
        stringBuilder.append(this.pathIndices[b]);
        stringBuilder.append(']');
      } 
    } 
    return stringBuilder.toString();
  }
  
  public boolean hasNext() throws IOException {
    boolean bool;
    int i = this.peeked;
    int j = i;
    if (i == 0)
      j = doPeek(); 
    if (j != 2 && j != 4) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public final boolean isLenient() {
    return this.lenient;
  }
  
  String locationString() {
    int i = this.lineNumber;
    int j = this.pos;
    int k = this.lineStart;
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(" at line ");
    stringBuilder.append(i + 1);
    stringBuilder.append(" column ");
    stringBuilder.append(j - k + 1);
    stringBuilder.append(" path ");
    stringBuilder.append(getPath());
    return stringBuilder.toString();
  }
  
  public boolean nextBoolean() throws IOException {
    int i = this.peeked;
    int j = i;
    if (i == 0)
      j = doPeek(); 
    if (j == 5) {
      this.peeked = 0;
      int[] arrayOfInt = this.pathIndices;
      j = this.stackSize - 1;
      arrayOfInt[j] = arrayOfInt[j] + 1;
      return true;
    } 
    if (j == 6) {
      this.peeked = 0;
      int[] arrayOfInt = this.pathIndices;
      j = this.stackSize - 1;
      arrayOfInt[j] = arrayOfInt[j] + 1;
      return false;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Expected a boolean but was ");
    stringBuilder.append(peek());
    stringBuilder.append(locationString());
    throw new IllegalStateException(stringBuilder.toString());
  }
  
  public double nextDouble() throws IOException {
    int i = this.peeked;
    int j = i;
    if (i == 0)
      j = doPeek(); 
    if (j == 15) {
      this.peeked = 0;
      int[] arrayOfInt = this.pathIndices;
      j = this.stackSize - 1;
      arrayOfInt[j] = arrayOfInt[j] + 1;
      return this.peekedLong;
    } 
    if (j == 16) {
      this.peekedString = new String(this.buffer, this.pos, this.peekedNumberLength);
      this.pos += this.peekedNumberLength;
    } else if (j == 8 || j == 9) {
      int k;
      if (j == 8) {
        j = 39;
        k = j;
      } else {
        j = 34;
        k = j;
      } 
      this.peekedString = nextQuotedValue(k);
    } else if (j == 10) {
      this.peekedString = nextUnquotedValue();
    } else if (j != 11) {
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("Expected a double but was ");
      stringBuilder1.append(peek());
      stringBuilder1.append(locationString());
      throw new IllegalStateException(stringBuilder1.toString());
    } 
    this.peeked = 11;
    double d = Double.parseDouble(this.peekedString);
    if (this.lenient || (!Double.isNaN(d) && !Double.isInfinite(d))) {
      this.peekedString = null;
      this.peeked = 0;
      int[] arrayOfInt = this.pathIndices;
      j = this.stackSize - 1;
      arrayOfInt[j] = arrayOfInt[j] + 1;
      return d;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("JSON forbids NaN and infinities: ");
    stringBuilder.append(d);
    stringBuilder.append(locationString());
    throw new MalformedJsonException(stringBuilder.toString());
  }
  
  public int nextInt() throws IOException {
    int i = this.peeked;
    int j = i;
    if (i == 0)
      j = doPeek(); 
    if (j == 15) {
      long l = this.peekedLong;
      j = (int)l;
      if (l == j) {
        this.peeked = 0;
        int[] arrayOfInt = this.pathIndices;
        i = this.stackSize - 1;
        arrayOfInt[i] = arrayOfInt[i] + 1;
        return j;
      } 
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("Expected an int but was ");
      stringBuilder1.append(this.peekedLong);
      stringBuilder1.append(locationString());
      throw new NumberFormatException(stringBuilder1.toString());
    } 
    if (j == 16) {
      this.peekedString = new String(this.buffer, this.pos, this.peekedNumberLength);
      this.pos += this.peekedNumberLength;
    } else if (j == 8 || j == 9 || j == 10) {
      if (j == 10) {
        this.peekedString = nextUnquotedValue();
      } else {
        int k;
        if (j == 8) {
          j = 39;
          k = j;
        } else {
          j = 34;
          k = j;
        } 
        this.peekedString = nextQuotedValue(k);
      } 
      try {
        i = Integer.parseInt(this.peekedString);
        this.peeked = 0;
        int[] arrayOfInt = this.pathIndices;
        j = this.stackSize - 1;
        arrayOfInt[j] = arrayOfInt[j] + 1;
        return i;
      } catch (NumberFormatException numberFormatException) {}
    } else {
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("Expected an int but was ");
      stringBuilder1.append(peek());
      stringBuilder1.append(locationString());
      throw new IllegalStateException(stringBuilder1.toString());
    } 
    this.peeked = 11;
    double d = Double.parseDouble(this.peekedString);
    j = (int)d;
    if (j == d) {
      this.peekedString = null;
      this.peeked = 0;
      int[] arrayOfInt = this.pathIndices;
      i = this.stackSize - 1;
      arrayOfInt[i] = arrayOfInt[i] + 1;
      return j;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Expected an int but was ");
    stringBuilder.append(this.peekedString);
    stringBuilder.append(locationString());
    throw new NumberFormatException(stringBuilder.toString());
  }
  
  public long nextLong() throws IOException {
    int i = this.peeked;
    int j = i;
    if (i == 0)
      j = doPeek(); 
    if (j == 15) {
      this.peeked = 0;
      int[] arrayOfInt = this.pathIndices;
      j = this.stackSize - 1;
      arrayOfInt[j] = arrayOfInt[j] + 1;
      return this.peekedLong;
    } 
    if (j == 16) {
      this.peekedString = new String(this.buffer, this.pos, this.peekedNumberLength);
      this.pos += this.peekedNumberLength;
    } else if (j == 8 || j == 9 || j == 10) {
      if (j == 10) {
        this.peekedString = nextUnquotedValue();
      } else {
        int k;
        if (j == 8) {
          j = 39;
          k = j;
        } else {
          j = 34;
          k = j;
        } 
        this.peekedString = nextQuotedValue(k);
      } 
      try {
        long l1 = Long.parseLong(this.peekedString);
        this.peeked = 0;
        int[] arrayOfInt = this.pathIndices;
        j = this.stackSize - 1;
        arrayOfInt[j] = arrayOfInt[j] + 1;
        return l1;
      } catch (NumberFormatException numberFormatException) {}
    } else {
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("Expected a long but was ");
      stringBuilder1.append(peek());
      stringBuilder1.append(locationString());
      throw new IllegalStateException(stringBuilder1.toString());
    } 
    this.peeked = 11;
    double d = Double.parseDouble(this.peekedString);
    long l = (long)d;
    if (l == d) {
      this.peekedString = null;
      this.peeked = 0;
      int[] arrayOfInt = this.pathIndices;
      j = this.stackSize - 1;
      arrayOfInt[j] = arrayOfInt[j] + 1;
      return l;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Expected a long but was ");
    stringBuilder.append(this.peekedString);
    stringBuilder.append(locationString());
    throw new NumberFormatException(stringBuilder.toString());
  }
  
  public String nextName() throws IOException {
    StringBuilder stringBuilder;
    int i = this.peeked;
    int j = i;
    if (i == 0)
      j = doPeek(); 
    if (j == 14) {
      String str = nextUnquotedValue();
    } else if (j == 12) {
      String str = nextQuotedValue('\'');
    } else {
      if (j == 13) {
        String str = nextQuotedValue('"');
        this.peeked = 0;
        this.pathNames[this.stackSize - 1] = str;
        return str;
      } 
      stringBuilder = new StringBuilder();
      stringBuilder.append("Expected a name but was ");
      stringBuilder.append(peek());
      stringBuilder.append(locationString());
      throw new IllegalStateException(stringBuilder.toString());
    } 
    this.peeked = 0;
    this.pathNames[this.stackSize - 1] = (String)stringBuilder;
    return (String)stringBuilder;
  }
  
  public void nextNull() throws IOException {
    int i = this.peeked;
    int j = i;
    if (i == 0)
      j = doPeek(); 
    if (j == 7) {
      this.peeked = 0;
      int[] arrayOfInt = this.pathIndices;
      j = this.stackSize - 1;
      arrayOfInt[j] = arrayOfInt[j] + 1;
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Expected null but was ");
    stringBuilder.append(peek());
    stringBuilder.append(locationString());
    throw new IllegalStateException(stringBuilder.toString());
  }
  
  public String nextString() throws IOException {
    StringBuilder stringBuilder;
    int i = this.peeked;
    int j = i;
    if (i == 0)
      j = doPeek(); 
    if (j == 10) {
      String str = nextUnquotedValue();
    } else if (j == 8) {
      String str = nextQuotedValue('\'');
    } else if (j == 9) {
      String str = nextQuotedValue('"');
    } else if (j == 11) {
      String str = this.peekedString;
      this.peekedString = null;
    } else if (j == 15) {
      String str = Long.toString(this.peekedLong);
    } else {
      if (j == 16) {
        String str = new String(this.buffer, this.pos, this.peekedNumberLength);
        this.pos += this.peekedNumberLength;
        this.peeked = 0;
        int[] arrayOfInt1 = this.pathIndices;
        j = this.stackSize - 1;
        arrayOfInt1[j] = arrayOfInt1[j] + 1;
        return str;
      } 
      stringBuilder = new StringBuilder();
      stringBuilder.append("Expected a string but was ");
      stringBuilder.append(peek());
      stringBuilder.append(locationString());
      throw new IllegalStateException(stringBuilder.toString());
    } 
    this.peeked = 0;
    int[] arrayOfInt = this.pathIndices;
    j = this.stackSize - 1;
    arrayOfInt[j] = arrayOfInt[j] + 1;
    return (String)stringBuilder;
  }
  
  public JsonToken peek() throws IOException {
    int i = this.peeked;
    int j = i;
    if (i == 0)
      j = doPeek(); 
    switch (j) {
      default:
        throw new AssertionError();
      case 17:
        return JsonToken.END_DOCUMENT;
      case 15:
      case 16:
        return JsonToken.NUMBER;
      case 12:
      case 13:
      case 14:
        return JsonToken.NAME;
      case 8:
      case 9:
      case 10:
      case 11:
        return JsonToken.STRING;
      case 7:
        return JsonToken.NULL;
      case 5:
      case 6:
        return JsonToken.BOOLEAN;
      case 4:
        return JsonToken.END_ARRAY;
      case 3:
        return JsonToken.BEGIN_ARRAY;
      case 2:
        return JsonToken.END_OBJECT;
      case 1:
        break;
    } 
    return JsonToken.BEGIN_OBJECT;
  }
  
  public final void setLenient(boolean paramBoolean) {
    this.lenient = paramBoolean;
  }
  
  public void skipValue() throws IOException {
    for (int i = 0;; i = j) {
      int j = this.peeked;
      int k = j;
      if (j == 0)
        k = doPeek(); 
      if (k == 3) {
        push(1);
      } else if (k == 1) {
        push(3);
      } else {
        if (k == 4) {
          this.stackSize--;
        } else if (k == 2) {
          this.stackSize--;
        } else {
          if (k == 14 || k == 10) {
            skipUnquotedValue();
            j = i;
          } else if (k == 8 || k == 12) {
            skipQuotedValue('\'');
            j = i;
          } else if (k == 9 || k == 13) {
            skipQuotedValue('"');
            j = i;
          } else {
            j = i;
            if (k == 16) {
              this.pos += this.peekedNumberLength;
              j = i;
            } 
          } 
          this.peeked = 0;
          i = j;
        } 
        j = i - 1;
        this.peeked = 0;
        i = j;
      } 
      j = i + 1;
      this.peeked = 0;
    } 
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(getClass().getSimpleName());
    stringBuilder.append(locationString());
    return stringBuilder.toString();
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/google/gson/stream/JsonReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */