package com.sun.jna;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;

public class Function extends Pointer {
  public static final int ALT_CONVENTION = 63;
  
  public static final int C_CONVENTION = 0;
  
  static final Integer INTEGER_FALSE;
  
  static final Integer INTEGER_TRUE = Integer.valueOf(-1);
  
  private static final VarArgsChecker IS_VARARGS;
  
  private static final int MASK_CC = 63;
  
  public static final int MAX_NARGS = 256;
  
  static final String OPTION_INVOKING_METHOD = "invoking-method";
  
  public static final int THROW_LAST_ERROR = 64;
  
  public static final int USE_VARARGS = 384;
  
  final int callFlags;
  
  final String encoding;
  
  private final String functionName;
  
  private NativeLibrary library;
  
  final Map<String, ?> options;
  
  static {
    INTEGER_FALSE = Integer.valueOf(0);
    IS_VARARGS = VarArgsChecker.create();
  }
  
  Function(NativeLibrary paramNativeLibrary, String paramString1, int paramInt, String paramString2) {
    checkCallingConvention(paramInt & 0x3F);
    if (paramString1 != null) {
      this.library = paramNativeLibrary;
      this.functionName = paramString1;
      this.callFlags = paramInt;
      this.options = paramNativeLibrary.options;
      if (paramString2 == null)
        paramString2 = Native.getDefaultStringEncoding(); 
      this.encoding = paramString2;
      try {
        this.peer = paramNativeLibrary.getSymbolAddress(paramString1);
        return;
      } catch (UnsatisfiedLinkError unsatisfiedLinkError) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Error looking up function '");
        stringBuilder.append(paramString1);
        stringBuilder.append("': ");
        stringBuilder.append(unsatisfiedLinkError.getMessage());
        throw new UnsatisfiedLinkError(stringBuilder.toString());
      } 
    } 
    throw new NullPointerException("Function name must not be null");
  }
  
  Function(Pointer paramPointer, int paramInt, String paramString) {
    checkCallingConvention(paramInt & 0x3F);
    if (paramPointer != null && paramPointer.peer != 0L) {
      this.functionName = paramPointer.toString();
      this.callFlags = paramInt;
      this.peer = paramPointer.peer;
      this.options = Collections.EMPTY_MAP;
      if (paramString == null)
        paramString = Native.getDefaultStringEncoding(); 
      this.encoding = paramString;
      return;
    } 
    throw new NullPointerException("Function address may not be null");
  }
  
  private void checkCallingConvention(int paramInt) throws IllegalArgumentException {
    if ((paramInt & 0x3F) == paramInt)
      return; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Unrecognized calling convention: ");
    stringBuilder.append(paramInt);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  static Object[] concatenateVarArgs(Object[] paramArrayOfObject) {
    Object[] arrayOfObject = paramArrayOfObject;
    if (paramArrayOfObject != null) {
      arrayOfObject = paramArrayOfObject;
      if (paramArrayOfObject.length > 0) {
        Class clazz;
        Object object = paramArrayOfObject[paramArrayOfObject.length - 1];
        if (object != null) {
          clazz = object.getClass();
        } else {
          clazz = null;
        } 
        arrayOfObject = paramArrayOfObject;
        if (clazz != null) {
          arrayOfObject = paramArrayOfObject;
          if (clazz.isArray()) {
            Object[] arrayOfObject1 = (Object[])object;
            for (byte b = 0; b < arrayOfObject1.length; b++) {
              if (arrayOfObject1[b] instanceof Float)
                arrayOfObject1[b] = Double.valueOf(((Float)arrayOfObject1[b]).floatValue()); 
            } 
            arrayOfObject = new Object[paramArrayOfObject.length + arrayOfObject1.length];
            System.arraycopy(paramArrayOfObject, 0, arrayOfObject, 0, paramArrayOfObject.length - 1);
            System.arraycopy(arrayOfObject1, 0, arrayOfObject, paramArrayOfObject.length - 1, arrayOfObject1.length);
            arrayOfObject[arrayOfObject.length - 1] = null;
          } 
        } 
      } 
    } 
    return arrayOfObject;
  }
  
  private Object convertArgument(Object[] paramArrayOfObject, int paramInt, Method paramMethod, TypeMapper paramTypeMapper, boolean paramBoolean, Class<?> paramClass) {
    Integer integer;
    Class<?> clazz1;
    StringBuilder stringBuilder1;
    Structure structure;
    Object<?> object1 = (Object<?>)paramArrayOfObject[paramInt];
    Object<?> object2 = object1;
    if (object1 != null) {
      NativeMappedConverter nativeMappedConverter;
      object2 = (Object<?>)object1.getClass();
      if (NativeMapped.class.isAssignableFrom((Class<?>)object2)) {
        nativeMappedConverter = NativeMappedConverter.getInstance((Class<?>)object2);
      } else if (nativeMappedConverter != null) {
        ToNativeConverter toNativeConverter = nativeMappedConverter.getToNativeConverter((Class<?>)object2);
      } else {
        nativeMappedConverter = null;
      } 
      object2 = object1;
      if (nativeMappedConverter != null) {
        FunctionParameterContext functionParameterContext;
        if (paramMethod != null) {
          functionParameterContext = new MethodParameterContext(this, paramArrayOfObject, paramInt, paramMethod);
        } else {
          functionParameterContext = new FunctionParameterContext(this, (Object[])functionParameterContext, paramInt);
        } 
        object2 = (Object<?>)nativeMappedConverter.toNative(object1, functionParameterContext);
      } 
    } 
    if (object2 == null || isPrimitiveArray(object2.getClass()))
      return object2; 
    Class<?> clazz2 = object2.getClass();
    if (object2 instanceof Structure) {
      structure = (Structure)object2;
      structure.autoWrite();
      if (structure instanceof Structure.ByValue) {
        Class<?> clazz = structure.getClass();
        clazz2 = clazz;
        if (paramMethod != null) {
          Class[] arrayOfClass = paramMethod.getParameterTypes();
          if (IS_VARARGS.isVarArgs(paramMethod)) {
            if (paramInt < arrayOfClass.length - 1) {
              clazz1 = arrayOfClass[paramInt];
            } else {
              Class<?> clazz3 = clazz1[clazz1.length - 1].getComponentType();
              clazz1 = clazz;
              if (clazz3 != Object.class)
                clazz1 = clazz3; 
            } 
          } else {
            clazz1 = clazz1[paramInt];
          } 
        } 
        if (Structure.ByValue.class.isAssignableFrom(clazz1))
          return structure; 
      } 
      return structure.getPointer();
    } 
    if (object2 instanceof Callback)
      return CallbackReference.getFunctionPointer((Callback)object2); 
    boolean bool = object2 instanceof String;
    boolean bool1 = false;
    if (bool)
      return (new NativeString((String)object2, false)).getPointer(); 
    if (object2 instanceof WString)
      return (new NativeString(object2.toString(), true)).getPointer(); 
    if (object2 instanceof Boolean) {
      if (Boolean.TRUE.equals(object2)) {
        integer = INTEGER_TRUE;
      } else {
        integer = INTEGER_FALSE;
      } 
      return integer;
    } 
    if (String[].class == integer)
      return new StringArray((String[])object2, this.encoding); 
    if (WString[].class == integer)
      return new StringArray((WString[])object2); 
    if (Pointer[].class == integer)
      return new PointerArray((Pointer[])object2); 
    if (NativeMapped[].class.isAssignableFrom((Class<?>)integer))
      return new NativeMappedArray((NativeMapped[])object2); 
    if (Structure[].class.isAssignableFrom((Class<?>)integer)) {
      StringBuilder stringBuilder;
      Structure[] arrayOfStructure = (Structure[])object2;
      clazz1 = integer.getComponentType();
      paramBoolean = Structure.ByReference.class.isAssignableFrom(clazz1);
      if (structure != null && !Structure.ByReference[].class.isAssignableFrom((Class<?>)structure))
        if (!paramBoolean) {
          byte b = 0;
          while (b < arrayOfStructure.length) {
            if (!(arrayOfStructure[b] instanceof Structure.ByReference)) {
              b++;
              continue;
            } 
            stringBuilder1 = new StringBuilder();
            stringBuilder1.append("Function ");
            stringBuilder1.append(getName());
            stringBuilder1.append(" declared Structure[] at parameter ");
            stringBuilder1.append(paramInt);
            stringBuilder1.append(" but element ");
            stringBuilder1.append(b);
            stringBuilder1.append(" is of Structure.ByReference type");
            throw new IllegalArgumentException(stringBuilder1.toString());
          } 
        } else {
          stringBuilder = new StringBuilder();
          stringBuilder.append("Function ");
          stringBuilder.append(getName());
          stringBuilder.append(" declared Structure[] at parameter ");
          stringBuilder.append(paramInt);
          stringBuilder.append(" but array of ");
          stringBuilder.append(stringBuilder1);
          stringBuilder.append(" was passed");
          throw new IllegalArgumentException(stringBuilder.toString());
        }  
      if (paramBoolean) {
        Structure.autoWrite((Structure[])stringBuilder);
        Pointer[] arrayOfPointer = new Pointer[stringBuilder.length + 1];
        for (paramInt = bool1; paramInt < stringBuilder.length; paramInt++) {
          if (stringBuilder[paramInt] != null) {
            Pointer pointer = stringBuilder[paramInt].getPointer();
          } else {
            clazz1 = null;
          } 
          arrayOfPointer[paramInt] = (Pointer)clazz1;
        } 
        return new PointerArray(arrayOfPointer);
      } 
      if (stringBuilder.length != 0) {
        if (stringBuilder[0] == null) {
          Structure.newInstance(clazz1).toArray((Structure[])stringBuilder);
          return stringBuilder[0].getPointer();
        } 
        Structure.autoWrite((Structure[])stringBuilder);
        return stringBuilder[0].getPointer();
      } 
      throw new IllegalArgumentException("Structure array must have non-zero length");
    } 
    if (!clazz1.isArray()) {
      if (paramBoolean)
        return object2; 
      if (Native.isSupportedNativeType(object2.getClass()))
        return object2; 
      stringBuilder1 = new StringBuilder();
      stringBuilder1.append("Unsupported argument type ");
      stringBuilder1.append(object2.getClass().getName());
      stringBuilder1.append(" at parameter ");
      stringBuilder1.append(paramInt);
      stringBuilder1.append(" of function ");
      stringBuilder1.append(getName());
      throw new IllegalArgumentException(stringBuilder1.toString());
    } 
    StringBuilder stringBuilder2 = new StringBuilder();
    stringBuilder2.append("Unsupported array argument type: ");
    stringBuilder2.append(stringBuilder1.getComponentType());
    throw new IllegalArgumentException(stringBuilder2.toString());
  }
  
  static int fixedArgs(Method paramMethod) {
    return IS_VARARGS.fixedArgs(paramMethod);
  }
  
  public static Function getFunction(Pointer paramPointer) {
    return getFunction(paramPointer, 0, (String)null);
  }
  
  public static Function getFunction(Pointer paramPointer, int paramInt) {
    return getFunction(paramPointer, paramInt, (String)null);
  }
  
  public static Function getFunction(Pointer paramPointer, int paramInt, String paramString) {
    return new Function(paramPointer, paramInt, paramString);
  }
  
  public static Function getFunction(String paramString1, String paramString2) {
    return NativeLibrary.getInstance(paramString1).getFunction(paramString2);
  }
  
  public static Function getFunction(String paramString1, String paramString2, int paramInt) {
    return NativeLibrary.getInstance(paramString1).getFunction(paramString2, paramInt, null);
  }
  
  public static Function getFunction(String paramString1, String paramString2, int paramInt, String paramString3) {
    return NativeLibrary.getInstance(paramString1).getFunction(paramString2, paramInt, paramString3);
  }
  
  private Pointer invokePointer(int paramInt, Object[] paramArrayOfObject) {
    Pointer pointer;
    long l = Native.invokePointer(this, this.peer, paramInt, paramArrayOfObject);
    if (l == 0L) {
      paramArrayOfObject = null;
    } else {
      pointer = new Pointer(l);
    } 
    return pointer;
  }
  
  private String invokeString(int paramInt, Object[] paramArrayOfObject, boolean paramBoolean) {
    Pointer pointer = invokePointer(paramInt, paramArrayOfObject);
    if (pointer != null) {
      String str;
      if (paramBoolean) {
        str = pointer.getWideString(0L);
      } else {
        str = str.getString(0L, this.encoding);
      } 
    } else {
      pointer = null;
    } 
    return (String)pointer;
  }
  
  private boolean isPrimitiveArray(Class<?> paramClass) {
    boolean bool;
    if (paramClass.isArray() && paramClass.getComponentType().isPrimitive()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  static boolean isVarArgs(Method paramMethod) {
    return IS_VARARGS.isVarArgs(paramMethod);
  }
  
  static Boolean valueOf(boolean paramBoolean) {
    Boolean bool;
    if (paramBoolean) {
      bool = Boolean.TRUE;
    } else {
      bool = Boolean.FALSE;
    } 
    return bool;
  }
  
  public boolean equals(Object paramObject) {
    boolean bool = true;
    if (paramObject == this)
      return true; 
    if (paramObject == null)
      return false; 
    if (paramObject.getClass() == getClass()) {
      paramObject = paramObject;
      if (((Function)paramObject).callFlags != this.callFlags || !((Function)paramObject).options.equals(this.options) || ((Function)paramObject).peer != this.peer)
        bool = false; 
      return bool;
    } 
    return false;
  }
  
  public int getCallingConvention() {
    return this.callFlags & 0x3F;
  }
  
  public String getName() {
    return this.functionName;
  }
  
  public int hashCode() {
    return this.callFlags + this.options.hashCode() + super.hashCode();
  }
  
  public Object invoke(Class<?> paramClass, Object[] paramArrayOfObject) {
    return invoke(paramClass, paramArrayOfObject, this.options);
  }
  
  public Object invoke(Class<?> paramClass, Object[] paramArrayOfObject, Map<String, ?> paramMap) {
    Class[] arrayOfClass;
    Method method = (Method)paramMap.get("invoking-method");
    if (method != null) {
      arrayOfClass = method.getParameterTypes();
    } else {
      arrayOfClass = null;
    } 
    return invoke(method, arrayOfClass, paramClass, paramArrayOfObject, paramMap);
  }
  
  Object invoke(Method paramMethod, Class<?>[] paramArrayOfClass, Class<?> paramClass, Object[] paramArrayOfObject, Map<String, ?> paramMap) {
    // Byte code:
    //   0: iconst_0
    //   1: anewarray java/lang/Object
    //   4: astore #6
    //   6: aload #4
    //   8: ifnull -> 54
    //   11: aload #4
    //   13: arraylength
    //   14: sipush #256
    //   17: if_icmpgt -> 43
    //   20: aload #4
    //   22: arraylength
    //   23: anewarray java/lang/Object
    //   26: astore #6
    //   28: aload #4
    //   30: iconst_0
    //   31: aload #6
    //   33: iconst_0
    //   34: aload #6
    //   36: arraylength
    //   37: invokestatic arraycopy : (Ljava/lang/Object;ILjava/lang/Object;II)V
    //   40: goto -> 54
    //   43: new java/lang/UnsupportedOperationException
    //   46: dup
    //   47: ldc_w 'Maximum argument count is 256'
    //   50: invokespecial <init> : (Ljava/lang/String;)V
    //   53: athrow
    //   54: aload #5
    //   56: ldc_w 'type-mapper'
    //   59: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   64: checkcast com/sun/jna/TypeMapper
    //   67: astore #7
    //   69: getstatic java/lang/Boolean.TRUE : Ljava/lang/Boolean;
    //   72: aload #5
    //   74: ldc_w 'allow-objects'
    //   77: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   82: invokevirtual equals : (Ljava/lang/Object;)Z
    //   85: istore #8
    //   87: aload #6
    //   89: arraylength
    //   90: ifle -> 106
    //   93: aload_1
    //   94: ifnull -> 106
    //   97: aload_1
    //   98: invokestatic isVarArgs : (Ljava/lang/reflect/Method;)Z
    //   101: istore #9
    //   103: goto -> 109
    //   106: iconst_0
    //   107: istore #9
    //   109: aload #6
    //   111: arraylength
    //   112: ifle -> 128
    //   115: aload_1
    //   116: ifnull -> 128
    //   119: aload_1
    //   120: invokestatic fixedArgs : (Ljava/lang/reflect/Method;)I
    //   123: istore #10
    //   125: goto -> 131
    //   128: iconst_0
    //   129: istore #10
    //   131: iconst_0
    //   132: istore #11
    //   134: aload #6
    //   136: arraylength
    //   137: istore #12
    //   139: aconst_null
    //   140: astore #5
    //   142: iload #11
    //   144: iload #12
    //   146: if_icmpge -> 219
    //   149: aload_1
    //   150: ifnull -> 190
    //   153: iload #9
    //   155: ifeq -> 181
    //   158: iload #11
    //   160: aload_2
    //   161: arraylength
    //   162: iconst_1
    //   163: isub
    //   164: if_icmplt -> 181
    //   167: aload_2
    //   168: aload_2
    //   169: arraylength
    //   170: iconst_1
    //   171: isub
    //   172: aaload
    //   173: invokevirtual getComponentType : ()Ljava/lang/Class;
    //   176: astore #5
    //   178: goto -> 187
    //   181: aload_2
    //   182: iload #11
    //   184: aaload
    //   185: astore #5
    //   187: goto -> 193
    //   190: aconst_null
    //   191: astore #5
    //   193: aload #6
    //   195: iload #11
    //   197: aload_0
    //   198: aload #6
    //   200: iload #11
    //   202: aload_1
    //   203: aload #7
    //   205: iload #8
    //   207: aload #5
    //   209: invokespecial convertArgument : ([Ljava/lang/Object;ILjava/lang/reflect/Method;Lcom/sun/jna/TypeMapper;ZLjava/lang/Class;)Ljava/lang/Object;
    //   212: aastore
    //   213: iinc #11, 1
    //   216: goto -> 134
    //   219: ldc com/sun/jna/NativeMapped
    //   221: aload_3
    //   222: invokevirtual isAssignableFrom : (Ljava/lang/Class;)Z
    //   225: ifeq -> 242
    //   228: aload_3
    //   229: invokestatic getInstance : (Ljava/lang/Class;)Lcom/sun/jna/NativeMappedConverter;
    //   232: astore_2
    //   233: aload_2
    //   234: invokevirtual nativeType : ()Ljava/lang/Class;
    //   237: astore #5
    //   239: goto -> 286
    //   242: aload #5
    //   244: astore_2
    //   245: aload #7
    //   247: ifnull -> 283
    //   250: aload #7
    //   252: aload_3
    //   253: invokeinterface getFromNativeConverter : (Ljava/lang/Class;)Lcom/sun/jna/FromNativeConverter;
    //   258: astore #7
    //   260: aload #7
    //   262: astore_2
    //   263: aload #7
    //   265: ifnull -> 283
    //   268: aload #7
    //   270: invokeinterface nativeType : ()Ljava/lang/Class;
    //   275: astore #5
    //   277: aload #7
    //   279: astore_2
    //   280: goto -> 286
    //   283: aload_3
    //   284: astore #5
    //   286: aload_0
    //   287: aload #6
    //   289: aload #5
    //   291: iload #8
    //   293: iload #10
    //   295: invokevirtual invoke : ([Ljava/lang/Object;Ljava/lang/Class;ZI)Ljava/lang/Object;
    //   298: astore #7
    //   300: aload #7
    //   302: astore #5
    //   304: aload_2
    //   305: ifnull -> 351
    //   308: aload_1
    //   309: ifnull -> 328
    //   312: new com/sun/jna/MethodResultContext
    //   315: dup
    //   316: aload_3
    //   317: aload_0
    //   318: aload #4
    //   320: aload_1
    //   321: invokespecial <init> : (Ljava/lang/Class;Lcom/sun/jna/Function;[Ljava/lang/Object;Ljava/lang/reflect/Method;)V
    //   324: astore_1
    //   325: goto -> 340
    //   328: new com/sun/jna/FunctionResultContext
    //   331: dup
    //   332: aload_3
    //   333: aload_0
    //   334: aload #4
    //   336: invokespecial <init> : (Ljava/lang/Class;Lcom/sun/jna/Function;[Ljava/lang/Object;)V
    //   339: astore_1
    //   340: aload_2
    //   341: aload #7
    //   343: aload_1
    //   344: invokeinterface fromNative : (Ljava/lang/Object;Lcom/sun/jna/FromNativeContext;)Ljava/lang/Object;
    //   349: astore #5
    //   351: aload #4
    //   353: ifnull -> 543
    //   356: iconst_0
    //   357: istore #10
    //   359: iload #10
    //   361: aload #4
    //   363: arraylength
    //   364: if_icmpge -> 543
    //   367: aload #4
    //   369: iload #10
    //   371: aaload
    //   372: astore_3
    //   373: aload_3
    //   374: ifnonnull -> 380
    //   377: goto -> 537
    //   380: aload_3
    //   381: instanceof com/sun/jna/Structure
    //   384: ifeq -> 404
    //   387: aload_3
    //   388: instanceof com/sun/jna/Structure$ByValue
    //   391: ifne -> 537
    //   394: aload_3
    //   395: checkcast com/sun/jna/Structure
    //   398: invokevirtual autoRead : ()V
    //   401: goto -> 537
    //   404: aload #6
    //   406: iload #10
    //   408: aaload
    //   409: instanceof com/sun/jna/Function$PostCallRead
    //   412: ifeq -> 517
    //   415: aload #6
    //   417: iload #10
    //   419: aaload
    //   420: checkcast com/sun/jna/Function$PostCallRead
    //   423: invokeinterface read : ()V
    //   428: aload #6
    //   430: iload #10
    //   432: aaload
    //   433: instanceof com/sun/jna/Function$PointerArray
    //   436: ifeq -> 537
    //   439: aload #6
    //   441: iload #10
    //   443: aaload
    //   444: checkcast com/sun/jna/Function$PointerArray
    //   447: astore_2
    //   448: ldc_w [Lcom/sun/jna/Structure$ByReference;
    //   451: aload_3
    //   452: invokevirtual getClass : ()Ljava/lang/Class;
    //   455: invokevirtual isAssignableFrom : (Ljava/lang/Class;)Z
    //   458: ifeq -> 537
    //   461: aload_3
    //   462: invokevirtual getClass : ()Ljava/lang/Class;
    //   465: invokevirtual getComponentType : ()Ljava/lang/Class;
    //   468: astore_1
    //   469: aload_3
    //   470: checkcast [Lcom/sun/jna/Structure;
    //   473: astore_3
    //   474: iconst_0
    //   475: istore #11
    //   477: iload #11
    //   479: aload_3
    //   480: arraylength
    //   481: if_icmpge -> 537
    //   484: aload_2
    //   485: getstatic com/sun/jna/Native.POINTER_SIZE : I
    //   488: iload #11
    //   490: imul
    //   491: i2l
    //   492: invokevirtual getPointer : (J)Lcom/sun/jna/Pointer;
    //   495: astore #7
    //   497: aload_3
    //   498: iload #11
    //   500: aload_1
    //   501: aload_3
    //   502: iload #11
    //   504: aaload
    //   505: aload #7
    //   507: invokestatic updateStructureByReference : (Ljava/lang/Class;Lcom/sun/jna/Structure;Lcom/sun/jna/Pointer;)Lcom/sun/jna/Structure;
    //   510: aastore
    //   511: iinc #11, 1
    //   514: goto -> 477
    //   517: ldc_w [Lcom/sun/jna/Structure;
    //   520: aload_3
    //   521: invokevirtual getClass : ()Ljava/lang/Class;
    //   524: invokevirtual isAssignableFrom : (Ljava/lang/Class;)Z
    //   527: ifeq -> 537
    //   530: aload_3
    //   531: checkcast [Lcom/sun/jna/Structure;
    //   534: invokestatic autoRead : ([Lcom/sun/jna/Structure;)V
    //   537: iinc #10, 1
    //   540: goto -> 359
    //   543: aload #5
    //   545: areturn
  }
  
  Object invoke(Object[] paramArrayOfObject, Class<?> paramClass, boolean paramBoolean) {
    return invoke(paramArrayOfObject, paramClass, paramBoolean, 0);
  }
  
  Object invoke(Object[] paramArrayOfObject, Class<?> paramClass, boolean paramBoolean, int paramInt) {
    String str;
    Object object;
    int i = this.callFlags | (paramInt & 0x3) << 7;
    Object[] arrayOfObject = null;
    if (paramClass == null || paramClass == void.class || paramClass == Void.class) {
      Native.invokeVoid(this, this.peer, i, paramArrayOfObject);
      return arrayOfObject;
    } 
    Class<boolean> clazz = boolean.class;
    boolean bool = true;
    paramInt = 0;
    if (paramClass == clazz || paramClass == Boolean.class) {
      if (Native.invokeInt(this, this.peer, i, paramArrayOfObject) != 0) {
        paramBoolean = bool;
      } else {
        paramBoolean = false;
      } 
      return valueOf(paramBoolean);
    } 
    if (paramClass == byte.class || paramClass == Byte.class)
      return Byte.valueOf((byte)Native.invokeInt(this, this.peer, i, paramArrayOfObject)); 
    if (paramClass == short.class || paramClass == Short.class)
      return Short.valueOf((short)Native.invokeInt(this, this.peer, i, paramArrayOfObject)); 
    if (paramClass == char.class || paramClass == Character.class)
      return Character.valueOf((char)Native.invokeInt(this, this.peer, i, paramArrayOfObject)); 
    if (paramClass == int.class || paramClass == Integer.class)
      return Integer.valueOf(Native.invokeInt(this, this.peer, i, paramArrayOfObject)); 
    if (paramClass == long.class || paramClass == Long.class)
      return Long.valueOf(Native.invokeLong(this, this.peer, i, paramArrayOfObject)); 
    if (paramClass == float.class || paramClass == Float.class)
      return Float.valueOf(Native.invokeFloat(this, this.peer, i, paramArrayOfObject)); 
    if (paramClass == double.class || paramClass == Double.class)
      return Double.valueOf(Native.invokeDouble(this, this.peer, i, paramArrayOfObject)); 
    if (paramClass == String.class) {
      str = invokeString(i, paramArrayOfObject, false);
    } else {
      WString wString;
      String str1;
      if (paramClass == WString.class) {
        str1 = invokeString(i, (Object[])str, true);
        Object[] arrayOfObject1 = arrayOfObject;
        if (str1 != null)
          wString = new WString(str1); 
      } else {
        Pointer pointer;
        if (Pointer.class.isAssignableFrom((Class<?>)str1))
          return invokePointer(i, (Object[])wString); 
        if (Structure.class.isAssignableFrom((Class<?>)str1)) {
          Structure structure;
          if (Structure.ByValue.class.isAssignableFrom((Class<?>)str1)) {
            structure = Native.invokeStructure(this, this.peer, i, (Object[])wString, Structure.newInstance((Class<Structure>)str1));
            structure.autoRead();
          } else {
            Pointer pointer1 = invokePointer(i, (Object[])structure);
            pointer = pointer1;
            if (pointer1 != null) {
              pointer = Structure.newInstance((Class<Pointer>)str1, pointer1);
              pointer.conditionalAutoRead();
            } 
          } 
        } else {
          Callback callback;
          Pointer pointer1;
          if (Callback.class.isAssignableFrom((Class<?>)str1)) {
            pointer1 = invokePointer(i, (Object[])pointer);
            pointer = pointer1;
            if (pointer1 != null)
              callback = CallbackReference.getCallback((Class<?>)str1, pointer1); 
          } else {
            String[] arrayOfString;
            Pointer pointer2;
            if (str1 == String[].class) {
              pointer2 = invokePointer(i, (Object[])callback);
              Pointer pointer3 = pointer1;
              if (pointer2 != null)
                arrayOfString = pointer2.getStringArray(0L, this.encoding); 
            } else {
              WString[] arrayOfWString1;
              WString[] arrayOfWString2;
              String[] arrayOfString1;
              if (pointer2 == WString[].class) {
                pointer2 = invokePointer(i, (Object[])arrayOfString);
                Pointer pointer3 = pointer1;
                if (pointer2 != null) {
                  arrayOfString1 = pointer2.getWideStringArray(0L);
                  arrayOfWString2 = new WString[arrayOfString1.length];
                  while (true) {
                    arrayOfWString1 = arrayOfWString2;
                    if (paramInt < arrayOfString1.length) {
                      arrayOfWString2[paramInt] = new WString(arrayOfString1[paramInt]);
                      paramInt++;
                      continue;
                    } 
                    break;
                  } 
                } 
              } else {
                Pointer[] arrayOfPointer;
                Pointer pointer3;
                if (arrayOfWString2 == Pointer[].class) {
                  pointer3 = invokePointer(i, (Object[])arrayOfWString1);
                  String[] arrayOfString2 = arrayOfString1;
                  if (pointer3 != null)
                    arrayOfPointer = pointer3.getPointerArray(0L); 
                } else if (paramBoolean) {
                  Object object1 = Native.invokeObject(this, this.peer, i, (Object[])arrayOfPointer);
                  object = object1;
                  if (object1 != null)
                    if (pointer3.isAssignableFrom(object1.getClass())) {
                      object = object1;
                    } else {
                      object = new StringBuilder();
                      object.append("Return type ");
                      object.append(pointer3);
                      object.append(" does not match result ");
                      object.append(object1.getClass());
                      throw new ClassCastException(object.toString());
                    }  
                } else {
                  object = new StringBuilder();
                  object.append("Unsupported return type ");
                  object.append(pointer3);
                  object.append(" in function ");
                  object.append(getName());
                  throw new IllegalArgumentException(object.toString());
                } 
              } 
            } 
          } 
        } 
      } 
    } 
    return object;
  }
  
  public void invoke(Object[] paramArrayOfObject) {
    invoke(Void.class, paramArrayOfObject);
  }
  
  public double invokeDouble(Object[] paramArrayOfObject) {
    return ((Double)invoke(Double.class, paramArrayOfObject)).doubleValue();
  }
  
  public float invokeFloat(Object[] paramArrayOfObject) {
    return ((Float)invoke(Float.class, paramArrayOfObject)).floatValue();
  }
  
  public int invokeInt(Object[] paramArrayOfObject) {
    return ((Integer)invoke(Integer.class, paramArrayOfObject)).intValue();
  }
  
  public long invokeLong(Object[] paramArrayOfObject) {
    return ((Long)invoke(Long.class, paramArrayOfObject)).longValue();
  }
  
  public Object invokeObject(Object[] paramArrayOfObject) {
    return invoke(Object.class, paramArrayOfObject);
  }
  
  public Pointer invokePointer(Object[] paramArrayOfObject) {
    return (Pointer)invoke(Pointer.class, paramArrayOfObject);
  }
  
  public String invokeString(Object[] paramArrayOfObject, boolean paramBoolean) {
    Class<String> clazz;
    if (paramBoolean) {
      Class<WString> clazz1 = WString.class;
    } else {
      clazz = String.class;
    } 
    Object object = invoke(clazz, paramArrayOfObject);
    if (object != null) {
      object = object.toString();
    } else {
      object = null;
    } 
    return (String)object;
  }
  
  public void invokeVoid(Object[] paramArrayOfObject) {
    invoke(Void.class, paramArrayOfObject);
  }
  
  public String toString() {
    if (this.library != null) {
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("native function ");
      stringBuilder1.append(this.functionName);
      stringBuilder1.append("(");
      stringBuilder1.append(this.library.getName());
      stringBuilder1.append(")@0x");
      stringBuilder1.append(Long.toHexString(this.peer));
      return stringBuilder1.toString();
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("native function@0x");
    stringBuilder.append(Long.toHexString(this.peer));
    return stringBuilder.toString();
  }
  
  private static class NativeMappedArray extends Memory implements PostCallRead {
    private final NativeMapped[] original;
    
    public NativeMappedArray(NativeMapped[] param1ArrayOfNativeMapped) {
      super(Native.getNativeSize(param1ArrayOfNativeMapped.getClass(), param1ArrayOfNativeMapped));
      this.original = param1ArrayOfNativeMapped;
      param1ArrayOfNativeMapped = this.original;
      setValue(0L, param1ArrayOfNativeMapped, param1ArrayOfNativeMapped.getClass());
    }
    
    public void read() {
      getValue(0L, this.original.getClass(), this.original);
    }
  }
  
  private static class PointerArray extends Memory implements PostCallRead {
    private final Pointer[] original;
    
    public PointerArray(Pointer[] param1ArrayOfPointer) {
      super((Native.POINTER_SIZE * (param1ArrayOfPointer.length + 1)));
      this.original = param1ArrayOfPointer;
      for (byte b = 0; b < param1ArrayOfPointer.length; b++)
        setPointer((Native.POINTER_SIZE * b), param1ArrayOfPointer[b]); 
      setPointer((Native.POINTER_SIZE * param1ArrayOfPointer.length), (Pointer)null);
    }
    
    public void read() {
      Pointer[] arrayOfPointer = this.original;
      read(0L, arrayOfPointer, 0, arrayOfPointer.length);
    }
  }
  
  public static interface PostCallRead {
    void read();
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/sun/jna/Function.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */