package com.sun.jna;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

public class Pointer {
  public static final Pointer NULL;
  
  protected long peer;
  
  Pointer() {}
  
  public Pointer(long paramLong) {
    this.peer = paramLong;
  }
  
  public static final Pointer createConstant(int paramInt) {
    return new Opaque(paramInt & 0xFFFFFFFFFFFFFFFFL);
  }
  
  public static final Pointer createConstant(long paramLong) {
    return new Opaque(paramLong);
  }
  
  public static long nativeValue(Pointer paramPointer) {
    long l;
    if (paramPointer == null) {
      l = 0L;
    } else {
      l = paramPointer.peer;
    } 
    return l;
  }
  
  public static void nativeValue(Pointer paramPointer, long paramLong) {
    paramPointer.peer = paramLong;
  }
  
  private void readArray(long paramLong, Object paramObject, Class<?> paramClass) {
    int i = Array.getLength(paramObject);
    if (paramClass == byte.class) {
      read(paramLong, (byte[])paramObject, 0, i);
    } else if (paramClass == short.class) {
      read(paramLong, (short[])paramObject, 0, i);
    } else if (paramClass == char.class) {
      read(paramLong, (char[])paramObject, 0, i);
    } else if (paramClass == int.class) {
      read(paramLong, (int[])paramObject, 0, i);
    } else if (paramClass == long.class) {
      read(paramLong, (long[])paramObject, 0, i);
    } else if (paramClass == float.class) {
      read(paramLong, (float[])paramObject, 0, i);
    } else if (paramClass == double.class) {
      read(paramLong, (double[])paramObject, 0, i);
    } else if (Pointer.class.isAssignableFrom(paramClass)) {
      read(paramLong, (Pointer[])paramObject, 0, i);
    } else {
      boolean bool = Structure.class.isAssignableFrom(paramClass);
      i = 0;
      int j = 0;
      if (bool) {
        Structure[] arrayOfStructure = (Structure[])paramObject;
        if (Structure.ByReference.class.isAssignableFrom(paramClass)) {
          paramObject = getPointerArray(paramLong, arrayOfStructure.length);
          for (i = j; i < arrayOfStructure.length; i++)
            arrayOfStructure[i] = Structure.updateStructureByReference(paramClass, arrayOfStructure[i], (Pointer)paramObject[i]); 
        } else {
          paramObject = arrayOfStructure[0];
          if (paramObject == null) {
            paramObject = Structure.newInstance(paramClass, share(paramLong));
            paramObject.conditionalAutoRead();
            arrayOfStructure[0] = (Structure)paramObject;
          } else {
            paramObject.useMemory(this, (int)paramLong, true);
            paramObject.read();
          } 
          paramObject = paramObject.toArray(arrayOfStructure.length);
          for (i = 1; i < arrayOfStructure.length; i++) {
            if (arrayOfStructure[i] == null) {
              arrayOfStructure[i] = (Structure)paramObject[i];
            } else {
              arrayOfStructure[i].useMemory(this, (int)((arrayOfStructure[i].size() * i) + paramLong), true);
              arrayOfStructure[i].read();
            } 
          } 
        } 
      } else {
        if (NativeMapped.class.isAssignableFrom(paramClass)) {
          NativeMapped[] arrayOfNativeMapped = (NativeMapped[])paramObject;
          NativeMappedConverter nativeMappedConverter = NativeMappedConverter.getInstance(paramClass);
          j = Native.getNativeSize(paramObject.getClass(), paramObject) / arrayOfNativeMapped.length;
          while (i < arrayOfNativeMapped.length) {
            arrayOfNativeMapped[i] = (NativeMapped)nativeMappedConverter.fromNative(getValue((j * i) + paramLong, nativeMappedConverter.nativeType(), arrayOfNativeMapped[i]), new FromNativeContext(paramClass));
            i++;
          } 
          return;
        } 
        paramObject = new StringBuilder();
        paramObject.append("Reading array of ");
        paramObject.append(paramClass);
        paramObject.append(" from memory not supported");
        paramObject = new IllegalArgumentException(paramObject.toString());
        throw paramObject;
      } 
    } 
  }
  
  private void writeArray(long paramLong, Object paramObject, Class<?> paramClass) {
    if (paramClass == byte.class) {
      paramObject = paramObject;
      write(paramLong, (byte[])paramObject, 0, paramObject.length);
    } else if (paramClass == short.class) {
      paramObject = paramObject;
      write(paramLong, (short[])paramObject, 0, paramObject.length);
    } else if (paramClass == char.class) {
      paramObject = paramObject;
      write(paramLong, (char[])paramObject, 0, paramObject.length);
    } else if (paramClass == int.class) {
      paramObject = paramObject;
      write(paramLong, (int[])paramObject, 0, paramObject.length);
    } else if (paramClass == long.class) {
      paramObject = paramObject;
      write(paramLong, (long[])paramObject, 0, paramObject.length);
    } else if (paramClass == float.class) {
      paramObject = paramObject;
      write(paramLong, (float[])paramObject, 0, paramObject.length);
    } else if (paramClass == double.class) {
      paramObject = paramObject;
      write(paramLong, (double[])paramObject, 0, paramObject.length);
    } else if (Pointer.class.isAssignableFrom(paramClass)) {
      paramObject = paramObject;
      write(paramLong, (Pointer[])paramObject, 0, paramObject.length);
    } else {
      boolean bool = Structure.class.isAssignableFrom(paramClass);
      int i = 0;
      int j = 0;
      if (bool) {
        Structure[] arrayOfStructure = (Structure[])paramObject;
        if (Structure.ByReference.class.isAssignableFrom(paramClass)) {
          paramObject = new Pointer[arrayOfStructure.length];
          for (i = j; i < arrayOfStructure.length; i++) {
            if (arrayOfStructure[i] == null) {
              paramObject[i] = null;
            } else {
              paramObject[i] = arrayOfStructure[i].getPointer();
              arrayOfStructure[i].write();
            } 
          } 
          write(paramLong, (Pointer[])paramObject, 0, paramObject.length);
        } else {
          paramObject = arrayOfStructure[0];
          if (paramObject == null) {
            paramObject = Structure.newInstance(paramClass, share(paramLong));
            arrayOfStructure[0] = (Structure)paramObject;
          } else {
            paramObject.useMemory(this, (int)paramLong, true);
          } 
          paramObject.write();
          paramObject = paramObject.toArray(arrayOfStructure.length);
          for (i = 1; i < arrayOfStructure.length; i++) {
            if (arrayOfStructure[i] == null) {
              arrayOfStructure[i] = (Structure)paramObject[i];
            } else {
              arrayOfStructure[i].useMemory(this, (int)((arrayOfStructure[i].size() * i) + paramLong), true);
            } 
            arrayOfStructure[i].write();
          } 
        } 
      } else {
        NativeMappedConverter nativeMappedConverter;
        if (NativeMapped.class.isAssignableFrom(paramClass)) {
          NativeMapped[] arrayOfNativeMapped = (NativeMapped[])paramObject;
          nativeMappedConverter = NativeMappedConverter.getInstance(paramClass);
          Class<?> clazz = nativeMappedConverter.nativeType();
          j = Native.getNativeSize(paramObject.getClass(), paramObject) / arrayOfNativeMapped.length;
          while (i < arrayOfNativeMapped.length) {
            paramObject = nativeMappedConverter.toNative(arrayOfNativeMapped[i], new ToNativeContext());
            setValue((i * j) + paramLong, paramObject, clazz);
            i++;
          } 
          return;
        } 
        paramObject = new StringBuilder();
        paramObject.append("Writing array of ");
        paramObject.append(nativeMappedConverter);
        paramObject.append(" to memory not supported");
        paramObject = new IllegalArgumentException(paramObject.toString());
        throw paramObject;
      } 
    } 
  }
  
  public void clear(long paramLong) {
    setMemory(0L, paramLong, (byte)0);
  }
  
  public String dump(long paramLong, int paramInt) {
    StringWriter stringWriter = new StringWriter(13 + paramInt * 2 + paramInt / 4 * 4);
    PrintWriter printWriter = new PrintWriter(stringWriter);
    printWriter.println("memory dump");
    for (byte b = 0; b < paramInt; b++) {
      byte b1 = getByte(b + paramLong);
      int i = b % 4;
      if (i == 0)
        printWriter.print("["); 
      if (b1 >= 0 && b1 < 16)
        printWriter.print("0"); 
      printWriter.print(Integer.toHexString(b1 & 0xFF));
      if (i == 3 && b < paramInt - 1)
        printWriter.println("]"); 
    } 
    if (stringWriter.getBuffer().charAt(stringWriter.getBuffer().length() - 2) != ']')
      printWriter.println("]"); 
    return stringWriter.toString();
  }
  
  public boolean equals(Object paramObject) {
    boolean bool = true;
    if (paramObject == this)
      return true; 
    if (paramObject == null)
      return false; 
    if (!(paramObject instanceof Pointer) || ((Pointer)paramObject).peer != this.peer)
      bool = false; 
    return bool;
  }
  
  public byte getByte(long paramLong) {
    return Native.getByte(this, this.peer, paramLong);
  }
  
  public byte[] getByteArray(long paramLong, int paramInt) {
    byte[] arrayOfByte = new byte[paramInt];
    read(paramLong, arrayOfByte, 0, paramInt);
    return arrayOfByte;
  }
  
  public ByteBuffer getByteBuffer(long paramLong1, long paramLong2) {
    return Native.getDirectByteBuffer(this, this.peer, paramLong1, paramLong2).order(ByteOrder.nativeOrder());
  }
  
  public char getChar(long paramLong) {
    return Native.getChar(this, this.peer, paramLong);
  }
  
  public char[] getCharArray(long paramLong, int paramInt) {
    char[] arrayOfChar = new char[paramInt];
    read(paramLong, arrayOfChar, 0, paramInt);
    return arrayOfChar;
  }
  
  public double getDouble(long paramLong) {
    return Native.getDouble(this, this.peer, paramLong);
  }
  
  public double[] getDoubleArray(long paramLong, int paramInt) {
    double[] arrayOfDouble = new double[paramInt];
    read(paramLong, arrayOfDouble, 0, paramInt);
    return arrayOfDouble;
  }
  
  public float getFloat(long paramLong) {
    return Native.getFloat(this, this.peer, paramLong);
  }
  
  public float[] getFloatArray(long paramLong, int paramInt) {
    float[] arrayOfFloat = new float[paramInt];
    read(paramLong, arrayOfFloat, 0, paramInt);
    return arrayOfFloat;
  }
  
  public int getInt(long paramLong) {
    return Native.getInt(this, this.peer, paramLong);
  }
  
  public int[] getIntArray(long paramLong, int paramInt) {
    int[] arrayOfInt = new int[paramInt];
    read(paramLong, arrayOfInt, 0, paramInt);
    return arrayOfInt;
  }
  
  public long getLong(long paramLong) {
    return Native.getLong(this, this.peer, paramLong);
  }
  
  public long[] getLongArray(long paramLong, int paramInt) {
    long[] arrayOfLong = new long[paramInt];
    read(paramLong, arrayOfLong, 0, paramInt);
    return arrayOfLong;
  }
  
  public NativeLong getNativeLong(long paramLong) {
    if (NativeLong.SIZE == 8) {
      paramLong = getLong(paramLong);
    } else {
      paramLong = getInt(paramLong);
    } 
    return new NativeLong(paramLong);
  }
  
  public Pointer getPointer(long paramLong) {
    return Native.getPointer(this.peer + paramLong);
  }
  
  public Pointer[] getPointerArray(long paramLong) {
    ArrayList<Pointer> arrayList = new ArrayList();
    Pointer pointer = getPointer(paramLong);
    int i = 0;
    while (pointer != null) {
      arrayList.add(pointer);
      i += Native.POINTER_SIZE;
      pointer = getPointer(i + paramLong);
    } 
    return arrayList.<Pointer>toArray(new Pointer[0]);
  }
  
  public Pointer[] getPointerArray(long paramLong, int paramInt) {
    Pointer[] arrayOfPointer = new Pointer[paramInt];
    read(paramLong, arrayOfPointer, 0, paramInt);
    return arrayOfPointer;
  }
  
  public short getShort(long paramLong) {
    return Native.getShort(this, this.peer, paramLong);
  }
  
  public short[] getShortArray(long paramLong, int paramInt) {
    short[] arrayOfShort = new short[paramInt];
    read(paramLong, arrayOfShort, 0, paramInt);
    return arrayOfShort;
  }
  
  public String getString(long paramLong) {
    return getString(paramLong, Native.getDefaultStringEncoding());
  }
  
  public String getString(long paramLong, String paramString) {
    return Native.getString(this, paramLong, paramString);
  }
  
  public String[] getStringArray(long paramLong) {
    return getStringArray(paramLong, -1, Native.getDefaultStringEncoding());
  }
  
  public String[] getStringArray(long paramLong, int paramInt) {
    return getStringArray(paramLong, paramInt, Native.getDefaultStringEncoding());
  }
  
  public String[] getStringArray(long paramLong, int paramInt, String paramString) {
    ArrayList<String> arrayList = new ArrayList();
    if (paramInt != -1) {
      Pointer pointer = getPointer(0L + paramLong);
      int i = 0;
      int j = 0;
      while (true) {
        int k = i + 1;
        if (i < paramInt) {
          String str;
          if (pointer == null) {
            str = null;
          } else if ("--WIDE-STRING--".equals(paramString)) {
            str = pointer.getWideString(0L);
          } else {
            str = pointer.getString(0L, paramString);
          } 
          arrayList.add(str);
          int m = j;
          if (k < paramInt) {
            m = j + Native.POINTER_SIZE;
            pointer = getPointer(m + paramLong);
          } 
          i = k;
          j = m;
          continue;
        } 
        break;
      } 
    } else {
      paramInt = 0;
      while (true) {
        Pointer pointer = getPointer(paramInt + paramLong);
        if (pointer != null) {
          String str;
          if ("--WIDE-STRING--".equals(paramString)) {
            str = pointer.getWideString(0L);
          } else {
            str = str.getString(0L, paramString);
          } 
          arrayList.add(str);
          paramInt += Native.POINTER_SIZE;
          continue;
        } 
        break;
      } 
    } 
    return arrayList.<String>toArray(new String[0]);
  }
  
  public String[] getStringArray(long paramLong, String paramString) {
    return getStringArray(paramLong, -1, paramString);
  }
  
  Object getValue(long paramLong, Class<?> paramClass, Object paramObject) {
    // Byte code:
    //   0: ldc com/sun/jna/Structure
    //   2: aload_3
    //   3: invokevirtual isAssignableFrom : (Ljava/lang/Class;)Z
    //   6: istore #5
    //   8: iconst_1
    //   9: istore #6
    //   11: iload #5
    //   13: ifeq -> 67
    //   16: aload #4
    //   18: checkcast com/sun/jna/Structure
    //   21: astore #4
    //   23: ldc com/sun/jna/Structure$ByReference
    //   25: aload_3
    //   26: invokevirtual isAssignableFrom : (Ljava/lang/Class;)Z
    //   29: ifeq -> 50
    //   32: aload_3
    //   33: aload #4
    //   35: aload_0
    //   36: lload_1
    //   37: invokevirtual getPointer : (J)Lcom/sun/jna/Pointer;
    //   40: invokestatic updateStructureByReference : (Ljava/lang/Class;Lcom/sun/jna/Structure;Lcom/sun/jna/Pointer;)Lcom/sun/jna/Structure;
    //   43: astore_3
    //   44: aload_3
    //   45: astore #4
    //   47: goto -> 805
    //   50: aload #4
    //   52: aload_0
    //   53: lload_1
    //   54: l2i
    //   55: iconst_1
    //   56: invokevirtual useMemory : (Lcom/sun/jna/Pointer;IZ)V
    //   59: aload #4
    //   61: invokevirtual read : ()V
    //   64: goto -> 805
    //   67: aload_3
    //   68: getstatic java/lang/Boolean.TYPE : Ljava/lang/Class;
    //   71: if_acmpeq -> 784
    //   74: aload_3
    //   75: ldc_w java/lang/Boolean
    //   78: if_acmpne -> 84
    //   81: goto -> 784
    //   84: aload_3
    //   85: getstatic java/lang/Byte.TYPE : Ljava/lang/Class;
    //   88: if_acmpeq -> 771
    //   91: aload_3
    //   92: ldc java/lang/Byte
    //   94: if_acmpne -> 100
    //   97: goto -> 771
    //   100: aload_3
    //   101: getstatic java/lang/Short.TYPE : Ljava/lang/Class;
    //   104: if_acmpeq -> 758
    //   107: aload_3
    //   108: ldc java/lang/Short
    //   110: if_acmpne -> 116
    //   113: goto -> 758
    //   116: aload_3
    //   117: getstatic java/lang/Character.TYPE : Ljava/lang/Class;
    //   120: if_acmpeq -> 745
    //   123: aload_3
    //   124: ldc java/lang/Character
    //   126: if_acmpne -> 132
    //   129: goto -> 745
    //   132: aload_3
    //   133: getstatic java/lang/Integer.TYPE : Ljava/lang/Class;
    //   136: if_acmpeq -> 732
    //   139: aload_3
    //   140: ldc java/lang/Integer
    //   142: if_acmpne -> 148
    //   145: goto -> 732
    //   148: aload_3
    //   149: getstatic java/lang/Long.TYPE : Ljava/lang/Class;
    //   152: if_acmpeq -> 719
    //   155: aload_3
    //   156: ldc java/lang/Long
    //   158: if_acmpne -> 164
    //   161: goto -> 719
    //   164: aload_3
    //   165: getstatic java/lang/Float.TYPE : Ljava/lang/Class;
    //   168: if_acmpeq -> 706
    //   171: aload_3
    //   172: ldc java/lang/Float
    //   174: if_acmpne -> 180
    //   177: goto -> 706
    //   180: aload_3
    //   181: getstatic java/lang/Double.TYPE : Ljava/lang/Class;
    //   184: if_acmpeq -> 693
    //   187: aload_3
    //   188: ldc java/lang/Double
    //   190: if_acmpne -> 196
    //   193: goto -> 693
    //   196: ldc com/sun/jna/Pointer
    //   198: aload_3
    //   199: invokevirtual isAssignableFrom : (Ljava/lang/Class;)Z
    //   202: istore #6
    //   204: aconst_null
    //   205: astore #7
    //   207: aconst_null
    //   208: astore #8
    //   210: aconst_null
    //   211: astore #9
    //   213: iload #6
    //   215: ifeq -> 289
    //   218: aload_0
    //   219: lload_1
    //   220: invokevirtual getPointer : (J)Lcom/sun/jna/Pointer;
    //   223: astore #8
    //   225: aload #7
    //   227: astore_3
    //   228: aload #8
    //   230: ifnull -> 283
    //   233: aload #9
    //   235: astore #7
    //   237: aload #4
    //   239: instanceof com/sun/jna/Pointer
    //   242: ifeq -> 252
    //   245: aload #4
    //   247: checkcast com/sun/jna/Pointer
    //   250: astore #7
    //   252: aload #8
    //   254: astore_3
    //   255: aload #7
    //   257: ifnull -> 44
    //   260: aload #7
    //   262: astore_3
    //   263: aload #8
    //   265: getfield peer : J
    //   268: aload #7
    //   270: getfield peer : J
    //   273: lcmp
    //   274: ifeq -> 283
    //   277: aload #8
    //   279: astore_3
    //   280: goto -> 44
    //   283: aload_3
    //   284: astore #4
    //   286: goto -> 805
    //   289: aload_3
    //   290: ldc_w java/lang/String
    //   293: if_acmpne -> 321
    //   296: aload_0
    //   297: lload_1
    //   298: invokevirtual getPointer : (J)Lcom/sun/jna/Pointer;
    //   301: astore #4
    //   303: aload #7
    //   305: astore_3
    //   306: aload #4
    //   308: ifnull -> 283
    //   311: aload #4
    //   313: lconst_0
    //   314: invokevirtual getString : (J)Ljava/lang/String;
    //   317: astore_3
    //   318: goto -> 44
    //   321: aload_3
    //   322: ldc_w com/sun/jna/WString
    //   325: if_acmpne -> 361
    //   328: aload_0
    //   329: lload_1
    //   330: invokevirtual getPointer : (J)Lcom/sun/jna/Pointer;
    //   333: astore #4
    //   335: aload #7
    //   337: astore_3
    //   338: aload #4
    //   340: ifnull -> 283
    //   343: new com/sun/jna/WString
    //   346: dup
    //   347: aload #4
    //   349: lconst_0
    //   350: invokevirtual getWideString : (J)Ljava/lang/String;
    //   353: invokespecial <init> : (Ljava/lang/String;)V
    //   356: astore #4
    //   358: goto -> 805
    //   361: ldc_w com/sun/jna/Callback
    //   364: aload_3
    //   365: invokevirtual isAssignableFrom : (Ljava/lang/Class;)Z
    //   368: ifeq -> 424
    //   371: aload_0
    //   372: lload_1
    //   373: invokevirtual getPointer : (J)Lcom/sun/jna/Pointer;
    //   376: astore #8
    //   378: aload #8
    //   380: ifnonnull -> 389
    //   383: aload #7
    //   385: astore_3
    //   386: goto -> 283
    //   389: aload #4
    //   391: checkcast com/sun/jna/Callback
    //   394: astore #7
    //   396: aload #7
    //   398: astore #4
    //   400: aload #8
    //   402: aload #7
    //   404: invokestatic getFunctionPointer : (Lcom/sun/jna/Callback;)Lcom/sun/jna/Pointer;
    //   407: invokevirtual equals : (Ljava/lang/Object;)Z
    //   410: ifne -> 805
    //   413: aload_3
    //   414: aload #8
    //   416: invokestatic getCallback : (Ljava/lang/Class;Lcom/sun/jna/Pointer;)Lcom/sun/jna/Callback;
    //   419: astore #4
    //   421: goto -> 805
    //   424: getstatic com/sun/jna/Platform.HAS_BUFFERS : Z
    //   427: ifeq -> 505
    //   430: ldc_w java/nio/Buffer
    //   433: aload_3
    //   434: invokevirtual isAssignableFrom : (Ljava/lang/Class;)Z
    //   437: ifeq -> 505
    //   440: aload_0
    //   441: lload_1
    //   442: invokevirtual getPointer : (J)Lcom/sun/jna/Pointer;
    //   445: astore #9
    //   447: aload #9
    //   449: ifnonnull -> 458
    //   452: aload #7
    //   454: astore_3
    //   455: goto -> 283
    //   458: aload #4
    //   460: ifnonnull -> 469
    //   463: aload #8
    //   465: astore_3
    //   466: goto -> 478
    //   469: aload #4
    //   471: checkcast java/nio/Buffer
    //   474: invokestatic getDirectBufferPointer : (Ljava/nio/Buffer;)Lcom/sun/jna/Pointer;
    //   477: astore_3
    //   478: aload_3
    //   479: ifnull -> 494
    //   482: aload_3
    //   483: aload #9
    //   485: invokevirtual equals : (Ljava/lang/Object;)Z
    //   488: ifeq -> 494
    //   491: goto -> 805
    //   494: new java/lang/IllegalStateException
    //   497: dup
    //   498: ldc_w 'Can't autogenerate a direct buffer on memory read'
    //   501: invokespecial <init> : (Ljava/lang/String;)V
    //   504: athrow
    //   505: ldc com/sun/jna/NativeMapped
    //   507: aload_3
    //   508: invokevirtual isAssignableFrom : (Ljava/lang/Class;)Z
    //   511: ifeq -> 609
    //   514: aload #4
    //   516: checkcast com/sun/jna/NativeMapped
    //   519: astore #4
    //   521: aload #4
    //   523: ifnull -> 575
    //   526: aload #4
    //   528: aload_0
    //   529: lload_1
    //   530: aload #4
    //   532: invokeinterface nativeType : ()Ljava/lang/Class;
    //   537: aconst_null
    //   538: invokevirtual getValue : (JLjava/lang/Class;Ljava/lang/Object;)Ljava/lang/Object;
    //   541: new com/sun/jna/FromNativeContext
    //   544: dup
    //   545: aload_3
    //   546: invokespecial <init> : (Ljava/lang/Class;)V
    //   549: invokeinterface fromNative : (Ljava/lang/Object;Lcom/sun/jna/FromNativeContext;)Ljava/lang/Object;
    //   554: astore #7
    //   556: aload #7
    //   558: astore_3
    //   559: aload #4
    //   561: aload #7
    //   563: invokevirtual equals : (Ljava/lang/Object;)Z
    //   566: ifeq -> 44
    //   569: aload #4
    //   571: astore_3
    //   572: goto -> 44
    //   575: aload_3
    //   576: invokestatic getInstance : (Ljava/lang/Class;)Lcom/sun/jna/NativeMappedConverter;
    //   579: astore #4
    //   581: aload #4
    //   583: aload_0
    //   584: lload_1
    //   585: aload #4
    //   587: invokevirtual nativeType : ()Ljava/lang/Class;
    //   590: aconst_null
    //   591: invokevirtual getValue : (JLjava/lang/Class;Ljava/lang/Object;)Ljava/lang/Object;
    //   594: new com/sun/jna/FromNativeContext
    //   597: dup
    //   598: aload_3
    //   599: invokespecial <init> : (Ljava/lang/Class;)V
    //   602: invokevirtual fromNative : (Ljava/lang/Object;Lcom/sun/jna/FromNativeContext;)Ljava/lang/Object;
    //   605: astore_3
    //   606: goto -> 44
    //   609: aload_3
    //   610: invokevirtual isArray : ()Z
    //   613: ifeq -> 646
    //   616: aload #4
    //   618: ifnull -> 635
    //   621: aload_0
    //   622: lload_1
    //   623: aload #4
    //   625: aload_3
    //   626: invokevirtual getComponentType : ()Ljava/lang/Class;
    //   629: invokespecial readArray : (JLjava/lang/Object;Ljava/lang/Class;)V
    //   632: goto -> 805
    //   635: new java/lang/IllegalStateException
    //   638: dup
    //   639: ldc_w 'Need an initialized array'
    //   642: invokespecial <init> : (Ljava/lang/String;)V
    //   645: athrow
    //   646: new java/lang/StringBuilder
    //   649: dup
    //   650: invokespecial <init> : ()V
    //   653: astore #4
    //   655: aload #4
    //   657: ldc_w 'Reading "'
    //   660: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   663: pop
    //   664: aload #4
    //   666: aload_3
    //   667: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   670: pop
    //   671: aload #4
    //   673: ldc_w '" from memory is not supported'
    //   676: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   679: pop
    //   680: new java/lang/IllegalArgumentException
    //   683: dup
    //   684: aload #4
    //   686: invokevirtual toString : ()Ljava/lang/String;
    //   689: invokespecial <init> : (Ljava/lang/String;)V
    //   692: athrow
    //   693: aload_0
    //   694: lload_1
    //   695: invokevirtual getDouble : (J)D
    //   698: invokestatic valueOf : (D)Ljava/lang/Double;
    //   701: astore #4
    //   703: goto -> 805
    //   706: aload_0
    //   707: lload_1
    //   708: invokevirtual getFloat : (J)F
    //   711: invokestatic valueOf : (F)Ljava/lang/Float;
    //   714: astore #4
    //   716: goto -> 805
    //   719: aload_0
    //   720: lload_1
    //   721: invokevirtual getLong : (J)J
    //   724: invokestatic valueOf : (J)Ljava/lang/Long;
    //   727: astore #4
    //   729: goto -> 805
    //   732: aload_0
    //   733: lload_1
    //   734: invokevirtual getInt : (J)I
    //   737: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   740: astore #4
    //   742: goto -> 805
    //   745: aload_0
    //   746: lload_1
    //   747: invokevirtual getChar : (J)C
    //   750: invokestatic valueOf : (C)Ljava/lang/Character;
    //   753: astore #4
    //   755: goto -> 805
    //   758: aload_0
    //   759: lload_1
    //   760: invokevirtual getShort : (J)S
    //   763: invokestatic valueOf : (S)Ljava/lang/Short;
    //   766: astore #4
    //   768: goto -> 805
    //   771: aload_0
    //   772: lload_1
    //   773: invokevirtual getByte : (J)B
    //   776: invokestatic valueOf : (B)Ljava/lang/Byte;
    //   779: astore #4
    //   781: goto -> 805
    //   784: aload_0
    //   785: lload_1
    //   786: invokevirtual getInt : (J)I
    //   789: ifeq -> 795
    //   792: goto -> 798
    //   795: iconst_0
    //   796: istore #6
    //   798: iload #6
    //   800: invokestatic valueOf : (Z)Ljava/lang/Boolean;
    //   803: astore #4
    //   805: aload #4
    //   807: areturn
  }
  
  public String getWideString(long paramLong) {
    return Native.getWideString(this, this.peer, paramLong);
  }
  
  public String[] getWideStringArray(long paramLong) {
    return getWideStringArray(paramLong, -1);
  }
  
  public String[] getWideStringArray(long paramLong, int paramInt) {
    return getStringArray(paramLong, paramInt, "--WIDE-STRING--");
  }
  
  public int hashCode() {
    long l = this.peer;
    return (int)((l >>> 32L) + (l & 0xFFFFFFFFFFFFFFFFL));
  }
  
  public long indexOf(long paramLong, byte paramByte) {
    return Native.indexOf(this, this.peer, paramLong, paramByte);
  }
  
  public void read(long paramLong, byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    Native.read(this, this.peer, paramLong, paramArrayOfbyte, paramInt1, paramInt2);
  }
  
  public void read(long paramLong, char[] paramArrayOfchar, int paramInt1, int paramInt2) {
    Native.read(this, this.peer, paramLong, paramArrayOfchar, paramInt1, paramInt2);
  }
  
  public void read(long paramLong, double[] paramArrayOfdouble, int paramInt1, int paramInt2) {
    Native.read(this, this.peer, paramLong, paramArrayOfdouble, paramInt1, paramInt2);
  }
  
  public void read(long paramLong, float[] paramArrayOffloat, int paramInt1, int paramInt2) {
    Native.read(this, this.peer, paramLong, paramArrayOffloat, paramInt1, paramInt2);
  }
  
  public void read(long paramLong, int[] paramArrayOfint, int paramInt1, int paramInt2) {
    Native.read(this, this.peer, paramLong, paramArrayOfint, paramInt1, paramInt2);
  }
  
  public void read(long paramLong, long[] paramArrayOflong, int paramInt1, int paramInt2) {
    Native.read(this, this.peer, paramLong, paramArrayOflong, paramInt1, paramInt2);
  }
  
  public void read(long paramLong, Pointer[] paramArrayOfPointer, int paramInt1, int paramInt2) {
    for (byte b = 0; b < paramInt2; b++) {
      Pointer pointer1 = getPointer((Native.POINTER_SIZE * b) + paramLong);
      int i = b + paramInt1;
      Pointer pointer2 = paramArrayOfPointer[i];
      if (pointer2 == null || pointer1 == null || pointer1.peer != pointer2.peer)
        paramArrayOfPointer[i] = pointer1; 
    } 
  }
  
  public void read(long paramLong, short[] paramArrayOfshort, int paramInt1, int paramInt2) {
    Native.read(this, this.peer, paramLong, paramArrayOfshort, paramInt1, paramInt2);
  }
  
  public void setByte(long paramLong, byte paramByte) {
    Native.setByte(this, this.peer, paramLong, paramByte);
  }
  
  public void setChar(long paramLong, char paramChar) {
    Native.setChar(this, this.peer, paramLong, paramChar);
  }
  
  public void setDouble(long paramLong, double paramDouble) {
    Native.setDouble(this, this.peer, paramLong, paramDouble);
  }
  
  public void setFloat(long paramLong, float paramFloat) {
    Native.setFloat(this, this.peer, paramLong, paramFloat);
  }
  
  public void setInt(long paramLong, int paramInt) {
    Native.setInt(this, this.peer, paramLong, paramInt);
  }
  
  public void setLong(long paramLong1, long paramLong2) {
    Native.setLong(this, this.peer, paramLong1, paramLong2);
  }
  
  public void setMemory(long paramLong1, long paramLong2, byte paramByte) {
    Native.setMemory(this, this.peer, paramLong1, paramLong2, paramByte);
  }
  
  public void setNativeLong(long paramLong, NativeLong paramNativeLong) {
    if (NativeLong.SIZE == 8) {
      setLong(paramLong, paramNativeLong.longValue());
    } else {
      setInt(paramLong, paramNativeLong.intValue());
    } 
  }
  
  public void setPointer(long paramLong, Pointer paramPointer) {
    long l2;
    long l1 = this.peer;
    if (paramPointer != null) {
      l2 = paramPointer.peer;
    } else {
      l2 = 0L;
    } 
    Native.setPointer(this, l1, paramLong, l2);
  }
  
  public void setShort(long paramLong, short paramShort) {
    Native.setShort(this, this.peer, paramLong, paramShort);
  }
  
  public void setString(long paramLong, WString paramWString) {
    String str;
    if (paramWString == null) {
      paramWString = null;
    } else {
      str = paramWString.toString();
    } 
    setWideString(paramLong, str);
  }
  
  public void setString(long paramLong, String paramString) {
    setString(paramLong, paramString, Native.getDefaultStringEncoding());
  }
  
  public void setString(long paramLong, String paramString1, String paramString2) {
    byte[] arrayOfByte = Native.getBytes(paramString1, paramString2);
    write(paramLong, arrayOfByte, 0, arrayOfByte.length);
    setByte(paramLong + arrayOfByte.length, (byte)0);
  }
  
  void setValue(long paramLong, Object<boolean> paramObject, Class<?> paramClass) {
    Class<boolean> clazz = boolean.class;
    boolean bool1 = false;
    boolean bool2 = false;
    boolean bool3 = false;
    short s = 0;
    int i = 0;
    if (paramClass == clazz || paramClass == Boolean.class) {
      i = s;
      if (Boolean.TRUE.equals(paramObject))
        i = -1; 
      setInt(paramLong, i);
      return;
    } 
    if (paramClass == byte.class || paramClass == Byte.class) {
      int j;
      if (paramObject == null) {
        j = bool3;
      } else {
        i = ((Byte)paramObject).byteValue();
        j = i;
      } 
      setByte(paramLong, j);
      return;
    } 
    if (paramClass == short.class || paramClass == Short.class) {
      int j;
      if (paramObject == null) {
        j = bool2;
      } else {
        i = ((Short)paramObject).shortValue();
        j = i;
      } 
      setShort(paramLong, j);
      return;
    } 
    if (paramClass == char.class || paramClass == Character.class) {
      int j;
      if (paramObject == null) {
        j = bool1;
      } else {
        i = ((Character)paramObject).charValue();
        j = i;
      } 
      setChar(paramLong, j);
      return;
    } 
    if (paramClass == int.class || paramClass == Integer.class) {
      if (paramObject != null)
        i = ((Integer)paramObject).intValue(); 
      setInt(paramLong, i);
      return;
    } 
    if (paramClass == long.class || paramClass == Long.class) {
      long l;
      if (paramObject == null) {
        l = 0L;
      } else {
        l = ((Long)paramObject).longValue();
      } 
      setLong(paramLong, l);
      return;
    } 
    if (paramClass == float.class || paramClass == Float.class) {
      float f;
      if (paramObject == null) {
        f = 0.0F;
      } else {
        f = ((Float)paramObject).floatValue();
      } 
      setFloat(paramLong, f);
      return;
    } 
    if (paramClass == double.class || paramClass == Double.class) {
      double d;
      if (paramObject == null) {
        d = 0.0D;
      } else {
        d = ((Double)paramObject).doubleValue();
      } 
      setDouble(paramLong, d);
      return;
    } 
    if (paramClass == Pointer.class) {
      setPointer(paramLong, (Pointer)paramObject);
    } else if (paramClass == String.class) {
      setPointer(paramLong, (Pointer)paramObject);
    } else if (paramClass == WString.class) {
      setPointer(paramLong, (Pointer)paramObject);
    } else {
      boolean bool = Structure.class.isAssignableFrom(paramClass);
      Structure structure = null;
      clazz = null;
      if (bool) {
        structure = (Structure)paramObject;
        if (Structure.ByReference.class.isAssignableFrom(paramClass)) {
          if (structure == null) {
            paramObject = (Object<boolean>)clazz;
          } else {
            paramObject = (Object<boolean>)structure.getPointer();
          } 
          setPointer(paramLong, (Pointer)paramObject);
          if (structure != null)
            structure.autoWrite(); 
        } else {
          structure.useMemory(this, (int)paramLong, true);
          structure.write();
        } 
      } else if (Callback.class.isAssignableFrom(paramClass)) {
        setPointer(paramLong, CallbackReference.getFunctionPointer((Callback)paramObject));
      } else if (Platform.HAS_BUFFERS && Buffer.class.isAssignableFrom(paramClass)) {
        if (paramObject == null) {
          paramObject = (Object<boolean>)structure;
        } else {
          paramObject = (Object<boolean>)Native.getDirectBufferPointer((Buffer)paramObject);
        } 
        setPointer(paramLong, (Pointer)paramObject);
      } else if (NativeMapped.class.isAssignableFrom(paramClass)) {
        NativeMappedConverter nativeMappedConverter = NativeMappedConverter.getInstance(paramClass);
        paramClass = nativeMappedConverter.nativeType();
        setValue(paramLong, nativeMappedConverter.toNative(paramObject, new ToNativeContext()), paramClass);
      } else if (paramClass.isArray()) {
        writeArray(paramLong, paramObject, paramClass.getComponentType());
      } else {
        paramObject = (Object<boolean>)new StringBuilder();
        paramObject.append("Writing ");
        paramObject.append(paramClass);
        paramObject.append(" to memory is not supported");
        throw new IllegalArgumentException(paramObject.toString());
      } 
    } 
  }
  
  public void setWideString(long paramLong, String paramString) {
    Native.setWideString(this, this.peer, paramLong, paramString);
  }
  
  public Pointer share(long paramLong) {
    return share(paramLong, 0L);
  }
  
  public Pointer share(long paramLong1, long paramLong2) {
    return (paramLong1 == 0L) ? this : new Pointer(this.peer + paramLong1);
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("native@0x");
    stringBuilder.append(Long.toHexString(this.peer));
    return stringBuilder.toString();
  }
  
  public void write(long paramLong, byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    Native.write(this, this.peer, paramLong, paramArrayOfbyte, paramInt1, paramInt2);
  }
  
  public void write(long paramLong, char[] paramArrayOfchar, int paramInt1, int paramInt2) {
    Native.write(this, this.peer, paramLong, paramArrayOfchar, paramInt1, paramInt2);
  }
  
  public void write(long paramLong, double[] paramArrayOfdouble, int paramInt1, int paramInt2) {
    Native.write(this, this.peer, paramLong, paramArrayOfdouble, paramInt1, paramInt2);
  }
  
  public void write(long paramLong, float[] paramArrayOffloat, int paramInt1, int paramInt2) {
    Native.write(this, this.peer, paramLong, paramArrayOffloat, paramInt1, paramInt2);
  }
  
  public void write(long paramLong, int[] paramArrayOfint, int paramInt1, int paramInt2) {
    Native.write(this, this.peer, paramLong, paramArrayOfint, paramInt1, paramInt2);
  }
  
  public void write(long paramLong, long[] paramArrayOflong, int paramInt1, int paramInt2) {
    Native.write(this, this.peer, paramLong, paramArrayOflong, paramInt1, paramInt2);
  }
  
  public void write(long paramLong, Pointer[] paramArrayOfPointer, int paramInt1, int paramInt2) {
    for (byte b = 0; b < paramInt2; b++)
      setPointer((Native.POINTER_SIZE * b) + paramLong, paramArrayOfPointer[paramInt1 + b]); 
  }
  
  public void write(long paramLong, short[] paramArrayOfshort, int paramInt1, int paramInt2) {
    Native.write(this, this.peer, paramLong, paramArrayOfshort, paramInt1, paramInt2);
  }
  
  private static class Opaque extends Pointer {
    private final String MSG;
    
    private Opaque(long param1Long) {
      super(param1Long);
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("This pointer is opaque: ");
      stringBuilder.append(this);
      this.MSG = stringBuilder.toString();
    }
    
    public void clear(long param1Long) {
      throw new UnsupportedOperationException(this.MSG);
    }
    
    public String dump(long param1Long, int param1Int) {
      throw new UnsupportedOperationException(this.MSG);
    }
    
    public byte getByte(long param1Long) {
      throw new UnsupportedOperationException(this.MSG);
    }
    
    public ByteBuffer getByteBuffer(long param1Long1, long param1Long2) {
      throw new UnsupportedOperationException(this.MSG);
    }
    
    public char getChar(long param1Long) {
      throw new UnsupportedOperationException(this.MSG);
    }
    
    public double getDouble(long param1Long) {
      throw new UnsupportedOperationException(this.MSG);
    }
    
    public float getFloat(long param1Long) {
      throw new UnsupportedOperationException(this.MSG);
    }
    
    public int getInt(long param1Long) {
      throw new UnsupportedOperationException(this.MSG);
    }
    
    public long getLong(long param1Long) {
      throw new UnsupportedOperationException(this.MSG);
    }
    
    public Pointer getPointer(long param1Long) {
      throw new UnsupportedOperationException(this.MSG);
    }
    
    public short getShort(long param1Long) {
      throw new UnsupportedOperationException(this.MSG);
    }
    
    public String getString(long param1Long, String param1String) {
      throw new UnsupportedOperationException(this.MSG);
    }
    
    public String getWideString(long param1Long) {
      throw new UnsupportedOperationException(this.MSG);
    }
    
    public long indexOf(long param1Long, byte param1Byte) {
      throw new UnsupportedOperationException(this.MSG);
    }
    
    public void read(long param1Long, byte[] param1ArrayOfbyte, int param1Int1, int param1Int2) {
      throw new UnsupportedOperationException(this.MSG);
    }
    
    public void read(long param1Long, char[] param1ArrayOfchar, int param1Int1, int param1Int2) {
      throw new UnsupportedOperationException(this.MSG);
    }
    
    public void read(long param1Long, double[] param1ArrayOfdouble, int param1Int1, int param1Int2) {
      throw new UnsupportedOperationException(this.MSG);
    }
    
    public void read(long param1Long, float[] param1ArrayOffloat, int param1Int1, int param1Int2) {
      throw new UnsupportedOperationException(this.MSG);
    }
    
    public void read(long param1Long, int[] param1ArrayOfint, int param1Int1, int param1Int2) {
      throw new UnsupportedOperationException(this.MSG);
    }
    
    public void read(long param1Long, long[] param1ArrayOflong, int param1Int1, int param1Int2) {
      throw new UnsupportedOperationException(this.MSG);
    }
    
    public void read(long param1Long, Pointer[] param1ArrayOfPointer, int param1Int1, int param1Int2) {
      throw new UnsupportedOperationException(this.MSG);
    }
    
    public void read(long param1Long, short[] param1ArrayOfshort, int param1Int1, int param1Int2) {
      throw new UnsupportedOperationException(this.MSG);
    }
    
    public void setByte(long param1Long, byte param1Byte) {
      throw new UnsupportedOperationException(this.MSG);
    }
    
    public void setChar(long param1Long, char param1Char) {
      throw new UnsupportedOperationException(this.MSG);
    }
    
    public void setDouble(long param1Long, double param1Double) {
      throw new UnsupportedOperationException(this.MSG);
    }
    
    public void setFloat(long param1Long, float param1Float) {
      throw new UnsupportedOperationException(this.MSG);
    }
    
    public void setInt(long param1Long, int param1Int) {
      throw new UnsupportedOperationException(this.MSG);
    }
    
    public void setLong(long param1Long1, long param1Long2) {
      throw new UnsupportedOperationException(this.MSG);
    }
    
    public void setMemory(long param1Long1, long param1Long2, byte param1Byte) {
      throw new UnsupportedOperationException(this.MSG);
    }
    
    public void setPointer(long param1Long, Pointer param1Pointer) {
      throw new UnsupportedOperationException(this.MSG);
    }
    
    public void setShort(long param1Long, short param1Short) {
      throw new UnsupportedOperationException(this.MSG);
    }
    
    public void setString(long param1Long, String param1String1, String param1String2) {
      throw new UnsupportedOperationException(this.MSG);
    }
    
    public void setWideString(long param1Long, String param1String) {
      throw new UnsupportedOperationException(this.MSG);
    }
    
    public Pointer share(long param1Long1, long param1Long2) {
      throw new UnsupportedOperationException(this.MSG);
    }
    
    public String toString() {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("const@0x");
      stringBuilder.append(Long.toHexString(this.peer));
      return stringBuilder.toString();
    }
    
    public void write(long param1Long, byte[] param1ArrayOfbyte, int param1Int1, int param1Int2) {
      throw new UnsupportedOperationException(this.MSG);
    }
    
    public void write(long param1Long, char[] param1ArrayOfchar, int param1Int1, int param1Int2) {
      throw new UnsupportedOperationException(this.MSG);
    }
    
    public void write(long param1Long, double[] param1ArrayOfdouble, int param1Int1, int param1Int2) {
      throw new UnsupportedOperationException(this.MSG);
    }
    
    public void write(long param1Long, float[] param1ArrayOffloat, int param1Int1, int param1Int2) {
      throw new UnsupportedOperationException(this.MSG);
    }
    
    public void write(long param1Long, int[] param1ArrayOfint, int param1Int1, int param1Int2) {
      throw new UnsupportedOperationException(this.MSG);
    }
    
    public void write(long param1Long, long[] param1ArrayOflong, int param1Int1, int param1Int2) {
      throw new UnsupportedOperationException(this.MSG);
    }
    
    public void write(long param1Long, Pointer[] param1ArrayOfPointer, int param1Int1, int param1Int2) {
      throw new UnsupportedOperationException(this.MSG);
    }
    
    public void write(long param1Long, short[] param1ArrayOfshort, int param1Int1, int param1Int2) {
      throw new UnsupportedOperationException(this.MSG);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/sun/jna/Pointer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */