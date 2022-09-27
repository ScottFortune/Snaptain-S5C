package com.sun.jna;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.nio.Buffer;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.logging.Logger;

public abstract class Structure {
  public static final int ALIGN_DEFAULT = 0;
  
  public static final int ALIGN_GNUC = 2;
  
  public static final int ALIGN_MSVC = 3;
  
  public static final int ALIGN_NONE = 1;
  
  protected static final int CALCULATE_SIZE = -1;
  
  private static final Logger LOG = Logger.getLogger(Structure.class.getName());
  
  private static final Pointer PLACEHOLDER_MEMORY;
  
  private static final ThreadLocal<Set<Structure>> busy;
  
  static final Map<Class<?>, List<String>> fieldOrder;
  
  static final Map<Class<?>, LayoutInfo> layoutInfo = new WeakHashMap<Class<?>, LayoutInfo>();
  
  private static final ThreadLocal<Map<Pointer, Structure>> reads;
  
  private int actualAlignType;
  
  private int alignType;
  
  private Structure[] array;
  
  private boolean autoRead = true;
  
  private boolean autoWrite = true;
  
  private String encoding;
  
  private Pointer memory;
  
  private final Map<String, Object> nativeStrings = new HashMap<String, Object>();
  
  private boolean readCalled;
  
  private int size = -1;
  
  private int structAlignment;
  
  private Map<String, StructField> structFields;
  
  private long typeInfo;
  
  private TypeMapper typeMapper;
  
  static {
    fieldOrder = new WeakHashMap<Class<?>, List<String>>();
    reads = new ThreadLocal<Map<Pointer, Structure>>() {
        protected Map<Pointer, Structure> initialValue() {
          // Byte code:
          //   0: aload_0
          //   1: monitorenter
          //   2: new java/util/HashMap
          //   5: dup
          //   6: invokespecial <init> : ()V
          //   9: astore_1
          //   10: aload_0
          //   11: monitorexit
          //   12: aload_1
          //   13: areturn
          //   14: astore_1
          //   15: aload_0
          //   16: monitorexit
          //   17: aload_1
          //   18: athrow
          // Exception table:
          //   from	to	target	type
          //   2	10	14	finally
        }
      };
    busy = new ThreadLocal<Set<Structure>>() {
        protected Set<Structure> initialValue() {
          // Byte code:
          //   0: aload_0
          //   1: monitorenter
          //   2: new com/sun/jna/Structure$StructureSet
          //   5: dup
          //   6: invokespecial <init> : ()V
          //   9: astore_1
          //   10: aload_0
          //   11: monitorexit
          //   12: aload_1
          //   13: areturn
          //   14: astore_1
          //   15: aload_0
          //   16: monitorexit
          //   17: aload_1
          //   18: athrow
          // Exception table:
          //   from	to	target	type
          //   2	10	14	finally
        }
      };
    PLACEHOLDER_MEMORY = new Pointer(0L) {
        public Pointer share(long param1Long1, long param1Long2) {
          return this;
        }
      };
  }
  
  protected Structure() {
    this(0);
  }
  
  protected Structure(int paramInt) {
    this((Pointer)null, paramInt);
  }
  
  protected Structure(int paramInt, TypeMapper paramTypeMapper) {
    this(null, paramInt, paramTypeMapper);
  }
  
  protected Structure(Pointer paramPointer) {
    this(paramPointer, 0);
  }
  
  protected Structure(Pointer paramPointer, int paramInt) {
    this(paramPointer, paramInt, null);
  }
  
  protected Structure(Pointer paramPointer, int paramInt, TypeMapper paramTypeMapper) {
    setAlignType(paramInt);
    setStringEncoding(Native.getStringEncoding(getClass()));
    initializeTypeMapper(paramTypeMapper);
    validateFields();
    if (paramPointer != null) {
      useMemory(paramPointer, 0, true);
    } else {
      allocateMemory(-1);
    } 
    initializeFields();
  }
  
  protected Structure(TypeMapper paramTypeMapper) {
    this(null, 0, paramTypeMapper);
  }
  
  private int addPadding(int paramInt) {
    return addPadding(paramInt, this.structAlignment);
  }
  
  private int addPadding(int paramInt1, int paramInt2) {
    int i = paramInt1;
    if (this.actualAlignType != 1) {
      int j = paramInt1 % paramInt2;
      i = paramInt1;
      if (j != 0)
        i = paramInt1 + paramInt2 - j; 
    } 
    return i;
  }
  
  private void allocateMemory(boolean paramBoolean) {
    allocateMemory(calculateSize(true, paramBoolean));
  }
  
  public static void autoRead(Structure[] paramArrayOfStructure) {
    structureArrayCheck(paramArrayOfStructure);
    byte b = 0;
    if ((paramArrayOfStructure[0]).array == paramArrayOfStructure) {
      paramArrayOfStructure[0].autoRead();
    } else {
      while (b < paramArrayOfStructure.length) {
        if (paramArrayOfStructure[b] != null)
          paramArrayOfStructure[b].autoRead(); 
        b++;
      } 
    } 
  }
  
  public static void autoWrite(Structure[] paramArrayOfStructure) {
    structureArrayCheck(paramArrayOfStructure);
    byte b = 0;
    if ((paramArrayOfStructure[0]).array == paramArrayOfStructure) {
      paramArrayOfStructure[0].autoWrite();
    } else {
      while (b < paramArrayOfStructure.length) {
        if (paramArrayOfStructure[b] != null)
          paramArrayOfStructure[b].autoWrite(); 
        b++;
      } 
    } 
  }
  
  private Class<?> baseClass() {
    return ((this instanceof ByReference || this instanceof ByValue) && Structure.class.isAssignableFrom(getClass().getSuperclass())) ? getClass().getSuperclass() : getClass();
  }
  
  static Set<Structure> busy() {
    return busy.get();
  }
  
  public static List<String> createFieldsOrder(String paramString) {
    return Collections.unmodifiableList(Collections.singletonList(paramString));
  }
  
  public static List<String> createFieldsOrder(List<String> paramList1, List<String> paramList2) {
    ArrayList<String> arrayList = new ArrayList(paramList1.size() + paramList2.size());
    arrayList.addAll(paramList1);
    arrayList.addAll(paramList2);
    return Collections.unmodifiableList(arrayList);
  }
  
  public static List<String> createFieldsOrder(List<String> paramList, String... paramVarArgs) {
    return createFieldsOrder(paramList, Arrays.asList(paramVarArgs));
  }
  
  public static List<String> createFieldsOrder(String... paramVarArgs) {
    return Collections.unmodifiableList(Arrays.asList(paramVarArgs));
  }
  
  private LayoutInfo deriveLayout(boolean paramBoolean1, boolean paramBoolean2) {
    // Byte code:
    //   0: aload_0
    //   1: iload_1
    //   2: invokevirtual getFields : (Z)Ljava/util/List;
    //   5: astore_3
    //   6: aload_3
    //   7: ifnonnull -> 12
    //   10: aconst_null
    //   11: areturn
    //   12: new com/sun/jna/Structure$LayoutInfo
    //   15: dup
    //   16: aconst_null
    //   17: invokespecial <init> : (Lcom/sun/jna/Structure$1;)V
    //   20: astore #4
    //   22: aload #4
    //   24: aload_0
    //   25: getfield alignType : I
    //   28: invokestatic access$202 : (Lcom/sun/jna/Structure$LayoutInfo;I)I
    //   31: pop
    //   32: aload #4
    //   34: aload_0
    //   35: getfield typeMapper : Lcom/sun/jna/TypeMapper;
    //   38: invokestatic access$302 : (Lcom/sun/jna/Structure$LayoutInfo;Lcom/sun/jna/TypeMapper;)Lcom/sun/jna/TypeMapper;
    //   41: pop
    //   42: aload_3
    //   43: invokeinterface iterator : ()Ljava/util/Iterator;
    //   48: astore #5
    //   50: iconst_0
    //   51: istore #6
    //   53: iconst_1
    //   54: istore #7
    //   56: aload #5
    //   58: invokeinterface hasNext : ()Z
    //   63: ifeq -> 1012
    //   66: aload #5
    //   68: invokeinterface next : ()Ljava/lang/Object;
    //   73: checkcast java/lang/reflect/Field
    //   76: astore #8
    //   78: aload #8
    //   80: invokevirtual getModifiers : ()I
    //   83: istore #9
    //   85: aload #8
    //   87: invokevirtual getType : ()Ljava/lang/Class;
    //   90: astore #10
    //   92: aload #10
    //   94: invokevirtual isArray : ()Z
    //   97: ifeq -> 107
    //   100: aload #4
    //   102: iconst_1
    //   103: invokestatic access$002 : (Lcom/sun/jna/Structure$LayoutInfo;Z)Z
    //   106: pop
    //   107: new com/sun/jna/Structure$StructField
    //   110: dup
    //   111: invokespecial <init> : ()V
    //   114: astore #11
    //   116: aload #11
    //   118: iload #9
    //   120: invokestatic isVolatile : (I)Z
    //   123: putfield isVolatile : Z
    //   126: aload #11
    //   128: iload #9
    //   130: invokestatic isFinal : (I)Z
    //   133: putfield isReadOnly : Z
    //   136: aload #11
    //   138: getfield isReadOnly : Z
    //   141: ifeq -> 222
    //   144: getstatic com/sun/jna/Platform.RO_FIELDS : Z
    //   147: ifeq -> 159
    //   150: aload #8
    //   152: iconst_1
    //   153: invokevirtual setAccessible : (Z)V
    //   156: goto -> 222
    //   159: new java/lang/StringBuilder
    //   162: dup
    //   163: invokespecial <init> : ()V
    //   166: astore_3
    //   167: aload_3
    //   168: ldc_w 'This VM does not support read-only fields (field ''
    //   171: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   174: pop
    //   175: aload_3
    //   176: aload #8
    //   178: invokevirtual getName : ()Ljava/lang/String;
    //   181: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   184: pop
    //   185: aload_3
    //   186: ldc_w '' within '
    //   189: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   192: pop
    //   193: aload_3
    //   194: aload_0
    //   195: invokevirtual getClass : ()Ljava/lang/Class;
    //   198: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   201: pop
    //   202: aload_3
    //   203: ldc_w ')'
    //   206: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   209: pop
    //   210: new java/lang/IllegalArgumentException
    //   213: dup
    //   214: aload_3
    //   215: invokevirtual toString : ()Ljava/lang/String;
    //   218: invokespecial <init> : (Ljava/lang/String;)V
    //   221: athrow
    //   222: aload #11
    //   224: aload #8
    //   226: putfield field : Ljava/lang/reflect/Field;
    //   229: aload #11
    //   231: aload #8
    //   233: invokevirtual getName : ()Ljava/lang/String;
    //   236: putfield name : Ljava/lang/String;
    //   239: aload #11
    //   241: aload #10
    //   243: putfield type : Ljava/lang/Class;
    //   246: ldc_w com/sun/jna/Callback
    //   249: aload #10
    //   251: invokevirtual isAssignableFrom : (Ljava/lang/Class;)Z
    //   254: ifeq -> 314
    //   257: aload #10
    //   259: invokevirtual isInterface : ()Z
    //   262: ifeq -> 268
    //   265: goto -> 314
    //   268: new java/lang/StringBuilder
    //   271: dup
    //   272: invokespecial <init> : ()V
    //   275: astore_3
    //   276: aload_3
    //   277: ldc_w 'Structure Callback field ''
    //   280: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   283: pop
    //   284: aload_3
    //   285: aload #8
    //   287: invokevirtual getName : ()Ljava/lang/String;
    //   290: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   293: pop
    //   294: aload_3
    //   295: ldc_w '' must be an interface'
    //   298: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   301: pop
    //   302: new java/lang/IllegalArgumentException
    //   305: dup
    //   306: aload_3
    //   307: invokevirtual toString : ()Ljava/lang/String;
    //   310: invokespecial <init> : (Ljava/lang/String;)V
    //   313: athrow
    //   314: aload #10
    //   316: invokevirtual isArray : ()Z
    //   319: ifeq -> 349
    //   322: ldc com/sun/jna/Structure
    //   324: aload #10
    //   326: invokevirtual getComponentType : ()Ljava/lang/Class;
    //   329: invokevirtual equals : (Ljava/lang/Object;)Z
    //   332: ifne -> 338
    //   335: goto -> 349
    //   338: new java/lang/IllegalArgumentException
    //   341: dup
    //   342: ldc_w 'Nested Structure arrays must use a derived Structure type so that the size of the elements can be determined'
    //   345: invokespecial <init> : (Ljava/lang/String;)V
    //   348: athrow
    //   349: aload #8
    //   351: invokevirtual getModifiers : ()I
    //   354: invokestatic isPublic : (I)Z
    //   357: ifne -> 363
    //   360: goto -> 843
    //   363: aload_0
    //   364: aload #11
    //   366: getfield field : Ljava/lang/reflect/Field;
    //   369: invokevirtual getFieldValue : (Ljava/lang/reflect/Field;)Ljava/lang/Object;
    //   372: astore #12
    //   374: aload #12
    //   376: ifnonnull -> 404
    //   379: aload #10
    //   381: invokevirtual isArray : ()Z
    //   384: ifeq -> 404
    //   387: iload_1
    //   388: ifne -> 393
    //   391: aconst_null
    //   392: areturn
    //   393: new java/lang/IllegalStateException
    //   396: dup
    //   397: ldc_w 'Array fields must be initialized'
    //   400: invokespecial <init> : (Ljava/lang/String;)V
    //   403: athrow
    //   404: ldc_w com/sun/jna/NativeMapped
    //   407: aload #10
    //   409: invokevirtual isAssignableFrom : (Ljava/lang/Class;)Z
    //   412: ifeq -> 460
    //   415: aload #10
    //   417: invokestatic getInstance : (Ljava/lang/Class;)Lcom/sun/jna/NativeMappedConverter;
    //   420: astore #13
    //   422: aload #13
    //   424: invokevirtual nativeType : ()Ljava/lang/Class;
    //   427: astore_3
    //   428: aload #11
    //   430: aload #13
    //   432: putfield writeConverter : Lcom/sun/jna/ToNativeConverter;
    //   435: aload #11
    //   437: aload #13
    //   439: putfield readConverter : Lcom/sun/jna/FromNativeConverter;
    //   442: aload #11
    //   444: new com/sun/jna/StructureReadContext
    //   447: dup
    //   448: aload_0
    //   449: aload #8
    //   451: invokespecial <init> : (Lcom/sun/jna/Structure;Ljava/lang/reflect/Field;)V
    //   454: putfield context : Lcom/sun/jna/FromNativeContext;
    //   457: goto -> 627
    //   460: aload_0
    //   461: getfield typeMapper : Lcom/sun/jna/TypeMapper;
    //   464: astore_3
    //   465: aload_3
    //   466: ifnull -> 624
    //   469: aload_3
    //   470: aload #10
    //   472: invokeinterface getToNativeConverter : (Ljava/lang/Class;)Lcom/sun/jna/ToNativeConverter;
    //   477: astore #14
    //   479: aload_0
    //   480: getfield typeMapper : Lcom/sun/jna/TypeMapper;
    //   483: aload #10
    //   485: invokeinterface getFromNativeConverter : (Ljava/lang/Class;)Lcom/sun/jna/FromNativeConverter;
    //   490: astore #13
    //   492: aload #14
    //   494: ifnull -> 576
    //   497: aload #13
    //   499: ifnull -> 576
    //   502: aload #14
    //   504: aload #12
    //   506: new com/sun/jna/StructureWriteContext
    //   509: dup
    //   510: aload_0
    //   511: aload #11
    //   513: getfield field : Ljava/lang/reflect/Field;
    //   516: invokespecial <init> : (Lcom/sun/jna/Structure;Ljava/lang/reflect/Field;)V
    //   519: invokeinterface toNative : (Ljava/lang/Object;Lcom/sun/jna/ToNativeContext;)Ljava/lang/Object;
    //   524: astore #12
    //   526: aload #12
    //   528: ifnull -> 540
    //   531: aload #12
    //   533: invokevirtual getClass : ()Ljava/lang/Class;
    //   536: astore_3
    //   537: goto -> 544
    //   540: ldc_w com/sun/jna/Pointer
    //   543: astore_3
    //   544: aload #11
    //   546: aload #14
    //   548: putfield writeConverter : Lcom/sun/jna/ToNativeConverter;
    //   551: aload #11
    //   553: aload #13
    //   555: putfield readConverter : Lcom/sun/jna/FromNativeConverter;
    //   558: aload #11
    //   560: new com/sun/jna/StructureReadContext
    //   563: dup
    //   564: aload_0
    //   565: aload #8
    //   567: invokespecial <init> : (Lcom/sun/jna/Structure;Ljava/lang/reflect/Field;)V
    //   570: putfield context : Lcom/sun/jna/FromNativeContext;
    //   573: goto -> 627
    //   576: aload #14
    //   578: ifnonnull -> 589
    //   581: aload #13
    //   583: ifnonnull -> 589
    //   586: goto -> 624
    //   589: new java/lang/StringBuilder
    //   592: dup
    //   593: invokespecial <init> : ()V
    //   596: astore_3
    //   597: aload_3
    //   598: ldc_w 'Structures require bidirectional type conversion for '
    //   601: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   604: pop
    //   605: aload_3
    //   606: aload #10
    //   608: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   611: pop
    //   612: new java/lang/IllegalArgumentException
    //   615: dup
    //   616: aload_3
    //   617: invokevirtual toString : ()Ljava/lang/String;
    //   620: invokespecial <init> : (Ljava/lang/String;)V
    //   623: athrow
    //   624: aload #10
    //   626: astore_3
    //   627: aload #12
    //   629: astore #8
    //   631: aload #12
    //   633: ifnonnull -> 649
    //   636: aload_0
    //   637: aload #11
    //   639: getfield field : Ljava/lang/reflect/Field;
    //   642: aload #10
    //   644: invokespecial initializeField : (Ljava/lang/reflect/Field;Ljava/lang/Class;)Ljava/lang/Object;
    //   647: astore #8
    //   649: aload #11
    //   651: aload_0
    //   652: aload_3
    //   653: aload #8
    //   655: invokevirtual getNativeSize : (Ljava/lang/Class;Ljava/lang/Object;)I
    //   658: putfield size : I
    //   661: aload_0
    //   662: aload_3
    //   663: aload #8
    //   665: iload #7
    //   667: invokevirtual getNativeAlignment : (Ljava/lang/Class;Ljava/lang/Object;Z)I
    //   670: istore #15
    //   672: iload #15
    //   674: ifeq -> 849
    //   677: aload #4
    //   679: aload #4
    //   681: invokestatic access$400 : (Lcom/sun/jna/Structure$LayoutInfo;)I
    //   684: iload #15
    //   686: invokestatic max : (II)I
    //   689: invokestatic access$402 : (Lcom/sun/jna/Structure$LayoutInfo;I)I
    //   692: pop
    //   693: iload #6
    //   695: iload #15
    //   697: irem
    //   698: istore #16
    //   700: iload #6
    //   702: istore #9
    //   704: iload #16
    //   706: ifeq -> 719
    //   709: iload #6
    //   711: iload #15
    //   713: iload #16
    //   715: isub
    //   716: iadd
    //   717: istore #9
    //   719: aload_0
    //   720: instanceof com/sun/jna/Union
    //   723: ifeq -> 747
    //   726: aload #11
    //   728: iconst_0
    //   729: putfield offset : I
    //   732: iload #9
    //   734: aload #11
    //   736: getfield size : I
    //   739: invokestatic max : (II)I
    //   742: istore #6
    //   744: goto -> 764
    //   747: aload #11
    //   749: iload #9
    //   751: putfield offset : I
    //   754: aload #11
    //   756: getfield size : I
    //   759: iload #9
    //   761: iadd
    //   762: istore #6
    //   764: aload #4
    //   766: invokestatic access$500 : (Lcom/sun/jna/Structure$LayoutInfo;)Ljava/util/Map;
    //   769: aload #11
    //   771: getfield name : Ljava/lang/String;
    //   774: aload #11
    //   776: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   781: pop
    //   782: aload #4
    //   784: invokestatic access$700 : (Lcom/sun/jna/Structure$LayoutInfo;)Lcom/sun/jna/Structure$StructField;
    //   787: ifnull -> 835
    //   790: aload #4
    //   792: invokestatic access$700 : (Lcom/sun/jna/Structure$LayoutInfo;)Lcom/sun/jna/Structure$StructField;
    //   795: getfield size : I
    //   798: aload #11
    //   800: getfield size : I
    //   803: if_icmplt -> 835
    //   806: aload #4
    //   808: invokestatic access$700 : (Lcom/sun/jna/Structure$LayoutInfo;)Lcom/sun/jna/Structure$StructField;
    //   811: getfield size : I
    //   814: aload #11
    //   816: getfield size : I
    //   819: if_icmpne -> 843
    //   822: ldc com/sun/jna/Structure
    //   824: aload #11
    //   826: getfield type : Ljava/lang/Class;
    //   829: invokevirtual isAssignableFrom : (Ljava/lang/Class;)Z
    //   832: ifeq -> 843
    //   835: aload #4
    //   837: aload #11
    //   839: invokestatic access$702 : (Lcom/sun/jna/Structure$LayoutInfo;Lcom/sun/jna/Structure$StructField;)Lcom/sun/jna/Structure$StructField;
    //   842: pop
    //   843: iconst_0
    //   844: istore #7
    //   846: goto -> 56
    //   849: new java/lang/StringBuilder
    //   852: dup
    //   853: invokespecial <init> : ()V
    //   856: astore_3
    //   857: aload_3
    //   858: ldc_w 'Field alignment is zero for field ''
    //   861: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   864: pop
    //   865: aload_3
    //   866: aload #11
    //   868: getfield name : Ljava/lang/String;
    //   871: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   874: pop
    //   875: aload_3
    //   876: ldc_w '' within '
    //   879: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   882: pop
    //   883: aload_3
    //   884: aload_0
    //   885: invokevirtual getClass : ()Ljava/lang/Class;
    //   888: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   891: pop
    //   892: new java/lang/Error
    //   895: dup
    //   896: aload_3
    //   897: invokevirtual toString : ()Ljava/lang/String;
    //   900: invokespecial <init> : (Ljava/lang/String;)V
    //   903: athrow
    //   904: astore #12
    //   906: iload_1
    //   907: ifne -> 919
    //   910: aload_0
    //   911: getfield typeMapper : Lcom/sun/jna/TypeMapper;
    //   914: ifnonnull -> 919
    //   917: aconst_null
    //   918: areturn
    //   919: new java/lang/StringBuilder
    //   922: dup
    //   923: invokespecial <init> : ()V
    //   926: astore_3
    //   927: aload_3
    //   928: ldc_w 'Invalid Structure field in '
    //   931: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   934: pop
    //   935: aload_3
    //   936: aload_0
    //   937: invokevirtual getClass : ()Ljava/lang/Class;
    //   940: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   943: pop
    //   944: aload_3
    //   945: ldc_w ', field name ''
    //   948: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   951: pop
    //   952: aload_3
    //   953: aload #11
    //   955: getfield name : Ljava/lang/String;
    //   958: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   961: pop
    //   962: aload_3
    //   963: ldc_w '' ('
    //   966: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   969: pop
    //   970: aload_3
    //   971: aload #11
    //   973: getfield type : Ljava/lang/Class;
    //   976: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   979: pop
    //   980: aload_3
    //   981: ldc_w '): '
    //   984: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   987: pop
    //   988: aload_3
    //   989: aload #12
    //   991: invokevirtual getMessage : ()Ljava/lang/String;
    //   994: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   997: pop
    //   998: new java/lang/IllegalArgumentException
    //   1001: dup
    //   1002: aload_3
    //   1003: invokevirtual toString : ()Ljava/lang/String;
    //   1006: aload #12
    //   1008: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
    //   1011: athrow
    //   1012: iload #6
    //   1014: ifle -> 1057
    //   1017: aload_0
    //   1018: iload #6
    //   1020: aload #4
    //   1022: invokestatic access$400 : (Lcom/sun/jna/Structure$LayoutInfo;)I
    //   1025: invokespecial addPadding : (II)I
    //   1028: istore #6
    //   1030: aload_0
    //   1031: instanceof com/sun/jna/Structure$ByValue
    //   1034: ifeq -> 1046
    //   1037: iload_2
    //   1038: ifne -> 1046
    //   1041: aload_0
    //   1042: invokevirtual getTypeInfo : ()Lcom/sun/jna/Pointer;
    //   1045: pop
    //   1046: aload #4
    //   1048: iload #6
    //   1050: invokestatic access$102 : (Lcom/sun/jna/Structure$LayoutInfo;I)I
    //   1053: pop
    //   1054: aload #4
    //   1056: areturn
    //   1057: new java/lang/StringBuilder
    //   1060: dup
    //   1061: invokespecial <init> : ()V
    //   1064: astore_3
    //   1065: aload_3
    //   1066: ldc_w 'Structure '
    //   1069: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1072: pop
    //   1073: aload_3
    //   1074: aload_0
    //   1075: invokevirtual getClass : ()Ljava/lang/Class;
    //   1078: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   1081: pop
    //   1082: aload_3
    //   1083: ldc_w ' has unknown or zero size (ensure all fields are public)'
    //   1086: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1089: pop
    //   1090: new java/lang/IllegalArgumentException
    //   1093: dup
    //   1094: aload_3
    //   1095: invokevirtual toString : ()Ljava/lang/String;
    //   1098: invokespecial <init> : (Ljava/lang/String;)V
    //   1101: astore_3
    //   1102: goto -> 1107
    //   1105: aload_3
    //   1106: athrow
    //   1107: goto -> 1105
    // Exception table:
    //   from	to	target	type
    //   649	672	904	java/lang/IllegalArgumentException
  }
  
  private void ensureAllocated(boolean paramBoolean) {
    if (this.memory == null) {
      allocateMemory(paramBoolean);
    } else if (this.size == -1) {
      this.size = calculateSize(true, paramBoolean);
      Pointer pointer = this.memory;
      if (!(pointer instanceof AutoAllocated))
        try {
          this.memory = pointer.share(0L, this.size);
        } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
          throw new IllegalArgumentException("Structure exceeds provided memory bounds", indexOutOfBoundsException);
        }  
    } 
  }
  
  private List<String> fieldOrder() {
    Class<?> clazz = getClass();
    synchronized (fieldOrder) {
      List<String> list1 = fieldOrder.get(clazz);
      List<String> list2 = list1;
      if (list1 == null) {
        list2 = getFieldOrder();
        fieldOrder.put(clazz, list2);
      } 
      return list2;
    } 
  }
  
  private String format(Class<?> paramClass) {
    String str = paramClass.getName();
    return str.substring(str.lastIndexOf(".") + 1);
  }
  
  private static <T> Constructor<T> getPointerConstructor(Class<T> paramClass) {
    for (Constructor<T> constructor : paramClass.getConstructors()) {
      Class[] arrayOfClass = constructor.getParameterTypes();
      if (arrayOfClass.length == 1 && arrayOfClass[0].equals(Pointer.class))
        return constructor; 
    } 
    return null;
  }
  
  static Pointer getTypeInfo(Object paramObject) {
    return FFIType.get(paramObject);
  }
  
  private Object initializeField(Field paramField, Class<?> paramClass) {
    if (Structure.class.isAssignableFrom(paramClass) && !ByReference.class.isAssignableFrom(paramClass)) {
      try {
        paramClass = newInstance(paramClass, PLACEHOLDER_MEMORY);
        setFieldValue(paramField, paramClass);
        Class<?> clazz = paramClass;
      } catch (IllegalArgumentException illegalArgumentException) {
        throw new IllegalArgumentException("Can't determine size of nested structure", illegalArgumentException);
      } 
    } else if (NativeMapped.class.isAssignableFrom(paramClass)) {
      NativeMapped nativeMapped2 = NativeMappedConverter.getInstance(paramClass).defaultValue();
      setFieldValue((Field)illegalArgumentException, nativeMapped2);
      NativeMapped nativeMapped1 = nativeMapped2;
    } else {
      illegalArgumentException = null;
    } 
    return illegalArgumentException;
  }
  
  private void initializeFields() {
    Iterator<Field> iterator = getFieldList().iterator();
    while (iterator.hasNext()) {
      Field field = iterator.next();
      try {
        if (field.get(this) == null)
          initializeField(field, field.getType()); 
      } catch (Exception exception) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Exception reading field '");
        stringBuilder.append(field.getName());
        stringBuilder.append("' in ");
        stringBuilder.append(getClass());
        throw new Error(stringBuilder.toString(), exception);
      } 
    } 
  }
  
  private void initializeTypeMapper(TypeMapper paramTypeMapper) {
    TypeMapper typeMapper = paramTypeMapper;
    if (paramTypeMapper == null)
      typeMapper = Native.getTypeMapper(getClass()); 
    this.typeMapper = typeMapper;
    layoutChanged();
  }
  
  private void layoutChanged() {
    if (this.size != -1) {
      this.size = -1;
      if (this.memory instanceof AutoAllocated)
        this.memory = null; 
      ensureAllocated();
    } 
  }
  
  public static <T extends Structure> T newInstance(Class<T> paramClass) throws IllegalArgumentException {
    Structure structure = Klass.<Structure>newInstance(paramClass);
    if (structure instanceof ByValue)
      structure.allocateMemory(); 
    return (T)structure;
  }
  
  private static <T extends Structure> T newInstance(Class<T> paramClass, long paramLong) {
    // Byte code:
    //   0: lload_1
    //   1: lconst_0
    //   2: lcmp
    //   3: ifne -> 13
    //   6: getstatic com/sun/jna/Structure.PLACEHOLDER_MEMORY : Lcom/sun/jna/Pointer;
    //   9: astore_3
    //   10: goto -> 22
    //   13: new com/sun/jna/Pointer
    //   16: dup
    //   17: lload_1
    //   18: invokespecial <init> : (J)V
    //   21: astore_3
    //   22: aload_0
    //   23: aload_3
    //   24: invokestatic newInstance : (Ljava/lang/Class;Lcom/sun/jna/Pointer;)Lcom/sun/jna/Structure;
    //   27: astore_0
    //   28: lload_1
    //   29: lconst_0
    //   30: lcmp
    //   31: ifeq -> 38
    //   34: aload_0
    //   35: invokevirtual conditionalAutoRead : ()V
    //   38: aload_0
    //   39: areturn
    //   40: astore_0
    //   41: getstatic com/sun/jna/Structure.LOG : Ljava/util/logging/Logger;
    //   44: getstatic java/util/logging/Level.WARNING : Ljava/util/logging/Level;
    //   47: ldc_w 'JNA: Error creating structure'
    //   50: aload_0
    //   51: invokevirtual log : (Ljava/util/logging/Level;Ljava/lang/String;Ljava/lang/Throwable;)V
    //   54: aconst_null
    //   55: areturn
    // Exception table:
    //   from	to	target	type
    //   6	10	40	finally
    //   13	22	40	finally
    //   22	28	40	finally
    //   34	38	40	finally
  }
  
  public static <T extends Structure> T newInstance(Class<T> paramClass, Pointer paramPointer) throws IllegalArgumentException {
    StringBuilder stringBuilder;
    try {
      Constructor<T> constructor = getPointerConstructor(paramClass);
      if (constructor != null)
        return constructor.newInstance(new Object[] { paramPointer }); 
    } catch (SecurityException securityException) {
    
    } catch (InstantiationException instantiationException) {
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("Can't instantiate ");
      stringBuilder1.append(paramClass);
      throw new IllegalArgumentException(stringBuilder1.toString(), instantiationException);
    } catch (IllegalAccessException illegalAccessException) {
      stringBuilder = new StringBuilder();
      stringBuilder.append("Instantiation of ");
      stringBuilder.append(paramClass);
      stringBuilder.append(" (Pointer) not allowed, is it public?");
      throw new IllegalArgumentException(stringBuilder.toString(), illegalAccessException);
    } catch (InvocationTargetException invocationTargetException) {
      stringBuilder = new StringBuilder();
      stringBuilder.append("Exception thrown while instantiating an instance of ");
      stringBuilder.append(paramClass);
      throw new IllegalArgumentException(stringBuilder.toString(), invocationTargetException);
    } 
    paramClass = newInstance((Class)paramClass);
    if (stringBuilder != PLACEHOLDER_MEMORY)
      paramClass.useMemory((Pointer)stringBuilder); 
    return (T)paramClass;
  }
  
  static Map<Pointer, Structure> reading() {
    return reads.get();
  }
  
  private void setFieldValue(Field paramField, Object paramObject, boolean paramBoolean) {
    try {
      paramField.set(this, paramObject);
      return;
    } catch (IllegalAccessException illegalAccessException) {
      if (Modifier.isFinal(paramField.getModifiers())) {
        if (paramBoolean) {
          StringBuilder stringBuilder2 = new StringBuilder();
          stringBuilder2.append("This VM does not support Structures with final fields (field '");
          stringBuilder2.append(paramField.getName());
          stringBuilder2.append("' within ");
          stringBuilder2.append(getClass());
          stringBuilder2.append(")");
          throw new UnsupportedOperationException(stringBuilder2.toString(), illegalAccessException);
        } 
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("Attempt to write to read-only field '");
        stringBuilder1.append(paramField.getName());
        stringBuilder1.append("' within ");
        stringBuilder1.append(getClass());
        throw new UnsupportedOperationException(stringBuilder1.toString(), illegalAccessException);
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Unexpectedly unable to write to field '");
      stringBuilder.append(paramField.getName());
      stringBuilder.append("' within ");
      stringBuilder.append(getClass());
      throw new Error(stringBuilder.toString(), illegalAccessException);
    } 
  }
  
  static int size(Class<? extends Structure> paramClass) {
    return size(paramClass, null);
  }
  
  static <T extends Structure> int size(Class<T> paramClass, T paramT) {
    Map<Class<?>, LayoutInfo> map;
    T t;
    synchronized (layoutInfo) {
      byte b;
      LayoutInfo layoutInfo = layoutInfo.get(paramClass);
      if (layoutInfo != null && !layoutInfo.variable) {
        b = layoutInfo.size;
      } else {
        b = -1;
      } 
      int i = b;
      if (b == -1) {
        t = paramT;
        if (paramT == null)
          t = newInstance(paramClass, PLACEHOLDER_MEMORY); 
        i = t.size();
      } 
      return i;
    } 
  }
  
  private static <T extends Comparable<T>> List<T> sort(Collection<? extends T> paramCollection) {
    paramCollection = new ArrayList<T>(paramCollection);
    Collections.sort((List<? extends T>)paramCollection);
    return (List)paramCollection;
  }
  
  private static void structureArrayCheck(Structure[] paramArrayOfStructure) {
    if (ByReference[].class.isAssignableFrom(paramArrayOfStructure.getClass()))
      return; 
    Pointer pointer = paramArrayOfStructure[0].getPointer();
    int i = paramArrayOfStructure[0].size();
    byte b = 1;
    while (b < paramArrayOfStructure.length) {
      if ((paramArrayOfStructure[b].getPointer()).peer == pointer.peer + (i * b)) {
        b++;
        continue;
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Structure array elements must use contiguous memory (bad backing address at Structure array index ");
      stringBuilder.append(b);
      stringBuilder.append(")");
      throw new IllegalArgumentException(stringBuilder.toString());
    } 
  }
  
  private String toString(int paramInt, boolean paramBoolean1, boolean paramBoolean2) {
    String str4;
    ensureAllocated();
    String str1 = System.getProperty("line.separator");
    StringBuilder stringBuilder1 = new StringBuilder();
    stringBuilder1.append(format(getClass()));
    stringBuilder1.append("(");
    stringBuilder1.append(getPointer());
    stringBuilder1.append(")");
    String str2 = stringBuilder1.toString();
    String str3 = str2;
    if (!(getPointer() instanceof Memory)) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(str2);
      stringBuilder.append(" (");
      stringBuilder.append(size());
      stringBuilder.append(" bytes)");
      str3 = stringBuilder.toString();
    } 
    String str5 = "";
    int i;
    for (i = 0; i < paramInt; i++) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(str5);
      stringBuilder.append("  ");
      str5 = stringBuilder.toString();
    } 
    if (!paramBoolean1) {
      str4 = "...}";
    } else {
      Iterator<StructField> iterator = fields().values().iterator();
      str2 = str1;
      while (true) {
        str4 = str2;
        if (iterator.hasNext()) {
          StructField structField = iterator.next();
          Object object2 = getFieldValue(structField.field);
          str4 = format(structField.type);
          StringBuilder stringBuilder4 = new StringBuilder();
          stringBuilder4.append(str2);
          stringBuilder4.append(str5);
          String str7 = stringBuilder4.toString();
          if (structField.type.isArray() && object2 != null) {
            str2 = format(structField.type.getComponentType());
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("[");
            stringBuilder.append(Array.getLength(object2));
            stringBuilder.append("]");
            str6 = stringBuilder.toString();
          } else {
            String str = "";
            str2 = str6;
            str6 = str;
          } 
          stringBuilder4 = new StringBuilder();
          stringBuilder4.append(str7);
          stringBuilder4.append(String.format("  %s %s%s@0x%X", new Object[] { str2, structField.name, str6, Integer.valueOf(structField.offset) }));
          String str6 = stringBuilder4.toString();
          Object object1 = object2;
          if (object2 instanceof Structure)
            object1 = ((Structure)object2).toString(paramInt + 1, object2 instanceof ByReference ^ true, paramBoolean2); 
          object2 = new StringBuilder();
          object2.append(str6);
          object2.append("=");
          str6 = object2.toString();
          if (object1 instanceof Long) {
            object2 = new StringBuilder();
            object2.append(str6);
            object2.append(String.format("0x%08X", new Object[] { object1 }));
            object1 = object2.toString();
          } else if (object1 instanceof Integer) {
            object2 = new StringBuilder();
            object2.append(str6);
            object2.append(String.format("0x%04X", new Object[] { object1 }));
            object1 = object2.toString();
          } else if (object1 instanceof Short) {
            object2 = new StringBuilder();
            object2.append(str6);
            object2.append(String.format("0x%02X", new Object[] { object1 }));
            object1 = object2.toString();
          } else if (object1 instanceof Byte) {
            object2 = new StringBuilder();
            object2.append(str6);
            object2.append(String.format("0x%01X", new Object[] { object1 }));
            object1 = object2.toString();
          } else {
            object2 = new StringBuilder();
            object2.append(str6);
            object2.append(String.valueOf(object1).trim());
            object1 = object2.toString();
          } 
          StringBuilder stringBuilder3 = new StringBuilder();
          stringBuilder3.append((String)object1);
          stringBuilder3.append(str1);
          str4 = stringBuilder3.toString();
          object1 = str4;
          if (!iterator.hasNext()) {
            object1 = new StringBuilder();
            object1.append(str4);
            object1.append(str5);
            object1.append("}");
            object1 = object1.toString();
          } 
          continue;
        } 
        break;
      } 
    } 
    str2 = str4;
    if (paramInt == 0) {
      str2 = str4;
      if (paramBoolean2) {
        String str;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(str4);
        stringBuilder.append(str1);
        stringBuilder.append("memory dump");
        stringBuilder.append(str1);
        str4 = stringBuilder.toString();
        byte[] arrayOfByte = getPointer().getByteArray(0L, size());
        for (paramInt = 0; paramInt < arrayOfByte.length; paramInt++) {
          i = paramInt % 4;
          String str7 = str4;
          if (i == 0) {
            StringBuilder stringBuilder4 = new StringBuilder();
            stringBuilder4.append(str4);
            stringBuilder4.append("[");
            str7 = stringBuilder4.toString();
          } 
          str4 = str7;
          if (arrayOfByte[paramInt] >= 0) {
            str4 = str7;
            if (arrayOfByte[paramInt] < 16) {
              StringBuilder stringBuilder4 = new StringBuilder();
              stringBuilder4.append(str7);
              stringBuilder4.append("0");
              str = stringBuilder4.toString();
            } 
          } 
          StringBuilder stringBuilder3 = new StringBuilder();
          stringBuilder3.append(str);
          stringBuilder3.append(Integer.toHexString(arrayOfByte[paramInt] & 0xFF));
          String str6 = stringBuilder3.toString();
          if (i == 3) {
            str = str6;
            if (paramInt < arrayOfByte.length - 1) {
              StringBuilder stringBuilder4 = new StringBuilder();
              stringBuilder4.append(str6);
              stringBuilder4.append("]");
              stringBuilder4.append(str1);
              String str8 = stringBuilder4.toString();
            } 
          } else {
            str = str6;
          } 
        } 
        stringBuilder = new StringBuilder();
        stringBuilder.append(str);
        stringBuilder.append("]");
        str2 = stringBuilder.toString();
      } 
    } 
    StringBuilder stringBuilder2 = new StringBuilder();
    stringBuilder2.append(str3);
    stringBuilder2.append(" {");
    stringBuilder2.append(str2);
    return stringBuilder2.toString();
  }
  
  static <T extends Structure> T updateStructureByReference(Class<T> paramClass, T paramT, Pointer paramPointer) {
    Structure structure;
    if (paramPointer == null) {
      paramT = null;
    } else {
      if (paramT == null || !paramPointer.equals(paramT.getPointer())) {
        structure = reading().get(paramPointer);
        if (structure != null && paramClass.equals(structure.getClass())) {
          structure.autoRead();
        } else {
          structure = newInstance(paramClass, paramPointer);
          structure.conditionalAutoRead();
        } 
        return (T)structure;
      } 
      structure.autoRead();
    } 
    return (T)structure;
  }
  
  static void validate(Class<? extends Structure> paramClass) {
    try {
      paramClass.getConstructor(new Class[0]);
      return;
    } catch (NoSuchMethodException|SecurityException noSuchMethodException) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("No suitable constructor found for class: ");
      stringBuilder.append(paramClass.getName());
      throw new IllegalArgumentException(stringBuilder.toString());
    } 
  }
  
  private void validateField(String paramString, Class<?> paramClass) {
    TypeMapper typeMapper = this.typeMapper;
    if (typeMapper != null) {
      ToNativeConverter toNativeConverter = typeMapper.getToNativeConverter(paramClass);
      if (toNativeConverter != null) {
        validateField(paramString, toNativeConverter.nativeType());
        return;
      } 
    } 
    if (paramClass.isArray()) {
      validateField(paramString, paramClass.getComponentType());
    } else {
      try {
        getNativeSize(paramClass);
        return;
      } catch (IllegalArgumentException illegalArgumentException) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Invalid Structure field in ");
        stringBuilder.append(getClass());
        stringBuilder.append(", field name '");
        stringBuilder.append(paramString);
        stringBuilder.append("' (");
        stringBuilder.append(paramClass);
        stringBuilder.append("): ");
        stringBuilder.append(illegalArgumentException.getMessage());
        throw new IllegalArgumentException(stringBuilder.toString(), illegalArgumentException);
      } 
    } 
  }
  
  private void validateFields() {
    for (Field field : getFieldList())
      validateField(field.getName(), field.getType()); 
  }
  
  protected void allocateMemory() {
    allocateMemory(false);
  }
  
  protected void allocateMemory(int paramInt) {
    if (paramInt == -1) {
      paramInt = calculateSize(false);
    } else if (paramInt <= 0) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Structure size must be greater than zero: ");
      stringBuilder.append(paramInt);
      throw new IllegalArgumentException(stringBuilder.toString());
    } 
    if (paramInt != -1) {
      Pointer pointer = this.memory;
      if (pointer == null || pointer instanceof AutoAllocated)
        this.memory = autoAllocate(paramInt); 
      this.size = paramInt;
    } 
  }
  
  protected Memory autoAllocate(int paramInt) {
    return new AutoAllocated(paramInt);
  }
  
  public void autoRead() {
    if (getAutoRead()) {
      read();
      if (this.array != null) {
        byte b = 1;
        while (true) {
          Structure[] arrayOfStructure = this.array;
          if (b < arrayOfStructure.length) {
            arrayOfStructure[b].autoRead();
            b++;
            continue;
          } 
          break;
        } 
      } 
    } 
  }
  
  public void autoWrite() {
    if (getAutoWrite()) {
      write();
      if (this.array != null) {
        byte b = 1;
        while (true) {
          Structure[] arrayOfStructure = this.array;
          if (b < arrayOfStructure.length) {
            arrayOfStructure[b].autoWrite();
            b++;
            continue;
          } 
          break;
        } 
      } 
    } 
  }
  
  protected void cacheTypeInfo(Pointer paramPointer) {
    this.typeInfo = paramPointer.peer;
  }
  
  protected int calculateSize(boolean paramBoolean) {
    return calculateSize(paramBoolean, false);
  }
  
  int calculateSize(boolean paramBoolean1, boolean paramBoolean2) {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual getClass : ()Ljava/lang/Class;
    //   4: astore_3
    //   5: getstatic com/sun/jna/Structure.layoutInfo : Ljava/util/Map;
    //   8: astore #4
    //   10: aload #4
    //   12: monitorenter
    //   13: getstatic com/sun/jna/Structure.layoutInfo : Ljava/util/Map;
    //   16: aload_3
    //   17: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   22: checkcast com/sun/jna/Structure$LayoutInfo
    //   25: astore #5
    //   27: aload #4
    //   29: monitorexit
    //   30: aload #5
    //   32: ifnull -> 63
    //   35: aload_0
    //   36: getfield alignType : I
    //   39: aload #5
    //   41: invokestatic access$200 : (Lcom/sun/jna/Structure$LayoutInfo;)I
    //   44: if_icmpne -> 63
    //   47: aload #5
    //   49: astore #4
    //   51: aload_0
    //   52: getfield typeMapper : Lcom/sun/jna/TypeMapper;
    //   55: aload #5
    //   57: invokestatic access$300 : (Lcom/sun/jna/Structure$LayoutInfo;)Lcom/sun/jna/TypeMapper;
    //   60: if_acmpeq -> 71
    //   63: aload_0
    //   64: iload_1
    //   65: iload_2
    //   66: invokespecial deriveLayout : (ZZ)Lcom/sun/jna/Structure$LayoutInfo;
    //   69: astore #4
    //   71: aload #4
    //   73: ifnull -> 172
    //   76: aload_0
    //   77: aload #4
    //   79: invokestatic access$400 : (Lcom/sun/jna/Structure$LayoutInfo;)I
    //   82: putfield structAlignment : I
    //   85: aload_0
    //   86: aload #4
    //   88: invokestatic access$500 : (Lcom/sun/jna/Structure$LayoutInfo;)Ljava/util/Map;
    //   91: putfield structFields : Ljava/util/Map;
    //   94: aload #4
    //   96: invokestatic access$000 : (Lcom/sun/jna/Structure$LayoutInfo;)Z
    //   99: ifne -> 162
    //   102: getstatic com/sun/jna/Structure.layoutInfo : Ljava/util/Map;
    //   105: astore #5
    //   107: aload #5
    //   109: monitorenter
    //   110: getstatic com/sun/jna/Structure.layoutInfo : Ljava/util/Map;
    //   113: aload_3
    //   114: invokeinterface containsKey : (Ljava/lang/Object;)Z
    //   119: ifeq -> 136
    //   122: aload_0
    //   123: getfield alignType : I
    //   126: ifne -> 136
    //   129: aload_0
    //   130: getfield typeMapper : Lcom/sun/jna/TypeMapper;
    //   133: ifnull -> 148
    //   136: getstatic com/sun/jna/Structure.layoutInfo : Ljava/util/Map;
    //   139: aload_3
    //   140: aload #4
    //   142: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   147: pop
    //   148: aload #5
    //   150: monitorexit
    //   151: goto -> 162
    //   154: astore #4
    //   156: aload #5
    //   158: monitorexit
    //   159: aload #4
    //   161: athrow
    //   162: aload #4
    //   164: invokestatic access$100 : (Lcom/sun/jna/Structure$LayoutInfo;)I
    //   167: istore #6
    //   169: goto -> 175
    //   172: iconst_m1
    //   173: istore #6
    //   175: iload #6
    //   177: ireturn
    //   178: astore #5
    //   180: aload #4
    //   182: monitorexit
    //   183: aload #5
    //   185: athrow
    // Exception table:
    //   from	to	target	type
    //   13	30	178	finally
    //   110	136	154	finally
    //   136	148	154	finally
    //   148	151	154	finally
    //   156	159	154	finally
    //   180	183	178	finally
  }
  
  public void clear() {
    ensureAllocated();
    this.memory.clear(size());
  }
  
  void conditionalAutoRead() {
    if (!this.readCalled)
      autoRead(); 
  }
  
  public boolean dataEquals(Structure paramStructure) {
    return dataEquals(paramStructure, false);
  }
  
  public boolean dataEquals(Structure paramStructure, boolean paramBoolean) {
    if (paramBoolean) {
      paramStructure.getPointer().clear(paramStructure.size());
      paramStructure.write();
      getPointer().clear(size());
      write();
    } 
    byte[] arrayOfByte2 = paramStructure.getPointer().getByteArray(0L, paramStructure.size());
    byte[] arrayOfByte1 = getPointer().getByteArray(0L, size());
    if (arrayOfByte2.length == arrayOfByte1.length) {
      for (byte b = 0; b < arrayOfByte2.length; b++) {
        if (arrayOfByte2[b] != arrayOfByte1[b])
          return false; 
      } 
      return true;
    } 
    return false;
  }
  
  protected void ensureAllocated() {
    ensureAllocated(false);
  }
  
  public boolean equals(Object paramObject) {
    boolean bool;
    if (paramObject instanceof Structure && paramObject.getClass() == getClass() && ((Structure)paramObject).getPointer().equals(getPointer())) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  protected int fieldOffset(String paramString) {
    ensureAllocated();
    StructField structField = fields().get(paramString);
    if (structField != null)
      return structField.offset; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("No such field: ");
    stringBuilder.append(paramString);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  Map<String, StructField> fields() {
    return this.structFields;
  }
  
  public boolean getAutoRead() {
    return this.autoRead;
  }
  
  public boolean getAutoWrite() {
    return this.autoWrite;
  }
  
  protected List<Field> getFieldList() {
    ArrayList<Field> arrayList = new ArrayList();
    for (Class<?> clazz = getClass(); !clazz.equals(Structure.class); clazz = clazz.getSuperclass()) {
      ArrayList<Field> arrayList1 = new ArrayList();
      Field[] arrayOfField = clazz.getDeclaredFields();
      for (byte b = 0; b < arrayOfField.length; b++) {
        int i = arrayOfField[b].getModifiers();
        if (!Modifier.isStatic(i) && Modifier.isPublic(i))
          arrayList1.add(arrayOfField[b]); 
      } 
      arrayList.addAll(0, arrayList1);
    } 
    return arrayList;
  }
  
  protected List<String> getFieldOrder() {
    LinkedList<? extends String> linkedList = new LinkedList();
    for (Class<?> clazz = getClass(); clazz != Structure.class; clazz = clazz.getSuperclass()) {
      FieldOrder fieldOrder = clazz.<FieldOrder>getAnnotation(FieldOrder.class);
      if (fieldOrder != null)
        linkedList.addAll(0, Arrays.asList(fieldOrder.value())); 
    } 
    return Collections.unmodifiableList(linkedList);
  }
  
  Pointer getFieldTypeInfo(StructField paramStructField) {
    Class<?> clazz1 = paramStructField.type;
    Object object2 = getFieldValue(paramStructField.field);
    TypeMapper typeMapper = this.typeMapper;
    Class<?> clazz2 = clazz1;
    Object object1 = object2;
    if (typeMapper != null) {
      ToNativeConverter toNativeConverter = typeMapper.getToNativeConverter(clazz1);
      clazz2 = clazz1;
      object1 = object2;
      if (toNativeConverter != null) {
        clazz2 = toNativeConverter.nativeType();
        object1 = toNativeConverter.toNative(object2, new ToNativeContext());
      } 
    } 
    return FFIType.get(object1, clazz2);
  }
  
  Object getFieldValue(Field paramField) {
    try {
      return paramField.get(this);
    } catch (Exception exception) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Exception reading field '");
      stringBuilder.append(paramField.getName());
      stringBuilder.append("' in ");
      stringBuilder.append(getClass());
      throw new Error(stringBuilder.toString(), exception);
    } 
  }
  
  protected List<Field> getFields(boolean paramBoolean) {
    List<Field> list = getFieldList();
    HashSet<String> hashSet = new HashSet();
    Iterator<Field> iterator = list.iterator();
    while (iterator.hasNext())
      hashSet.add(((Field)iterator.next()).getName()); 
    List<String> list1 = fieldOrder();
    if (list1.size() != list.size() && list.size() > 1) {
      if (paramBoolean) {
        String str;
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("Structure.getFieldOrder() on ");
        stringBuilder1.append(getClass());
        if (list1.size() < list.size()) {
          str = " does not provide enough";
        } else {
          str = " provides too many";
        } 
        stringBuilder1.append(str);
        stringBuilder1.append(" names [");
        stringBuilder1.append(list1.size());
        stringBuilder1.append("] (");
        stringBuilder1.append(sort(list1));
        stringBuilder1.append(") to match declared fields [");
        stringBuilder1.append(list.size());
        stringBuilder1.append("] (");
        stringBuilder1.append(sort(hashSet));
        stringBuilder1.append(")");
        throw new Error(stringBuilder1.toString());
      } 
      return null;
    } 
    if ((new HashSet(list1)).equals(hashSet)) {
      sortFields(list, list1);
      return list;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Structure.getFieldOrder() on ");
    stringBuilder.append(getClass());
    stringBuilder.append(" returns names (");
    stringBuilder.append(sort(list1));
    stringBuilder.append(") which do not match declared field names (");
    stringBuilder.append(sort(hashSet));
    stringBuilder.append(")");
    Error error = new Error(stringBuilder.toString());
    throw error;
  }
  
  protected int getNativeAlignment(Class<?> paramClass, Object paramObject, boolean paramBoolean) {
    // Byte code:
    //   0: aload_1
    //   1: astore #4
    //   3: aload_2
    //   4: astore #5
    //   6: ldc_w com/sun/jna/NativeMapped
    //   9: aload_1
    //   10: invokevirtual isAssignableFrom : (Ljava/lang/Class;)Z
    //   13: ifeq -> 41
    //   16: aload_1
    //   17: invokestatic getInstance : (Ljava/lang/Class;)Lcom/sun/jna/NativeMappedConverter;
    //   20: astore_1
    //   21: aload_1
    //   22: invokevirtual nativeType : ()Ljava/lang/Class;
    //   25: astore #4
    //   27: aload_1
    //   28: aload_2
    //   29: new com/sun/jna/ToNativeContext
    //   32: dup
    //   33: invokespecial <init> : ()V
    //   36: invokevirtual toNative : (Ljava/lang/Object;Lcom/sun/jna/ToNativeContext;)Ljava/lang/Object;
    //   39: astore #5
    //   41: aload #4
    //   43: aload #5
    //   45: invokestatic getNativeSize : (Ljava/lang/Class;Ljava/lang/Object;)I
    //   48: istore #6
    //   50: iload #6
    //   52: istore #7
    //   54: aload #4
    //   56: invokevirtual isPrimitive : ()Z
    //   59: ifne -> 359
    //   62: iload #6
    //   64: istore #7
    //   66: ldc_w java/lang/Long
    //   69: aload #4
    //   71: if_acmpeq -> 359
    //   74: iload #6
    //   76: istore #7
    //   78: ldc_w java/lang/Integer
    //   81: aload #4
    //   83: if_acmpeq -> 359
    //   86: iload #6
    //   88: istore #7
    //   90: ldc_w java/lang/Short
    //   93: aload #4
    //   95: if_acmpeq -> 359
    //   98: iload #6
    //   100: istore #7
    //   102: ldc_w java/lang/Character
    //   105: aload #4
    //   107: if_acmpeq -> 359
    //   110: iload #6
    //   112: istore #7
    //   114: ldc_w java/lang/Byte
    //   117: aload #4
    //   119: if_acmpeq -> 359
    //   122: iload #6
    //   124: istore #7
    //   126: ldc_w java/lang/Boolean
    //   129: aload #4
    //   131: if_acmpeq -> 359
    //   134: iload #6
    //   136: istore #7
    //   138: ldc_w java/lang/Float
    //   141: aload #4
    //   143: if_acmpeq -> 359
    //   146: ldc_w java/lang/Double
    //   149: aload #4
    //   151: if_acmpne -> 161
    //   154: iload #6
    //   156: istore #7
    //   158: goto -> 359
    //   161: ldc_w com/sun/jna/Pointer
    //   164: aload #4
    //   166: invokevirtual isAssignableFrom : (Ljava/lang/Class;)Z
    //   169: ifeq -> 183
    //   172: ldc_w com/sun/jna/Function
    //   175: aload #4
    //   177: invokevirtual isAssignableFrom : (Ljava/lang/Class;)Z
    //   180: ifeq -> 354
    //   183: getstatic com/sun/jna/Platform.HAS_BUFFERS : Z
    //   186: ifeq -> 200
    //   189: ldc_w java/nio/Buffer
    //   192: aload #4
    //   194: invokevirtual isAssignableFrom : (Ljava/lang/Class;)Z
    //   197: ifne -> 354
    //   200: ldc_w com/sun/jna/Callback
    //   203: aload #4
    //   205: invokevirtual isAssignableFrom : (Ljava/lang/Class;)Z
    //   208: ifne -> 354
    //   211: ldc_w com/sun/jna/WString
    //   214: aload #4
    //   216: if_acmpeq -> 354
    //   219: ldc_w java/lang/String
    //   222: aload #4
    //   224: if_acmpne -> 230
    //   227: goto -> 354
    //   230: ldc com/sun/jna/Structure
    //   232: aload #4
    //   234: invokevirtual isAssignableFrom : (Ljava/lang/Class;)Z
    //   237: ifeq -> 287
    //   240: ldc com/sun/jna/Structure$ByReference
    //   242: aload #4
    //   244: invokevirtual isAssignableFrom : (Ljava/lang/Class;)Z
    //   247: ifeq -> 258
    //   250: getstatic com/sun/jna/Native.POINTER_SIZE : I
    //   253: istore #7
    //   255: goto -> 359
    //   258: aload #5
    //   260: astore_1
    //   261: aload #5
    //   263: ifnonnull -> 275
    //   266: aload #4
    //   268: getstatic com/sun/jna/Structure.PLACEHOLDER_MEMORY : Lcom/sun/jna/Pointer;
    //   271: invokestatic newInstance : (Ljava/lang/Class;Lcom/sun/jna/Pointer;)Lcom/sun/jna/Structure;
    //   274: astore_1
    //   275: aload_1
    //   276: checkcast com/sun/jna/Structure
    //   279: invokevirtual getStructAlignment : ()I
    //   282: istore #7
    //   284: goto -> 359
    //   287: aload #4
    //   289: invokevirtual isArray : ()Z
    //   292: ifeq -> 311
    //   295: aload_0
    //   296: aload #4
    //   298: invokevirtual getComponentType : ()Ljava/lang/Class;
    //   301: aconst_null
    //   302: iload_3
    //   303: invokevirtual getNativeAlignment : (Ljava/lang/Class;Ljava/lang/Object;Z)I
    //   306: istore #7
    //   308: goto -> 359
    //   311: new java/lang/StringBuilder
    //   314: dup
    //   315: invokespecial <init> : ()V
    //   318: astore_1
    //   319: aload_1
    //   320: ldc_w 'Type '
    //   323: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   326: pop
    //   327: aload_1
    //   328: aload #4
    //   330: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   333: pop
    //   334: aload_1
    //   335: ldc_w ' has unknown native alignment'
    //   338: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   341: pop
    //   342: new java/lang/IllegalArgumentException
    //   345: dup
    //   346: aload_1
    //   347: invokevirtual toString : ()Ljava/lang/String;
    //   350: invokespecial <init> : (Ljava/lang/String;)V
    //   353: athrow
    //   354: getstatic com/sun/jna/Native.POINTER_SIZE : I
    //   357: istore #7
    //   359: aload_0
    //   360: getfield actualAlignType : I
    //   363: istore #8
    //   365: iload #8
    //   367: iconst_1
    //   368: if_icmpne -> 377
    //   371: iconst_1
    //   372: istore #6
    //   374: goto -> 476
    //   377: iload #8
    //   379: iconst_3
    //   380: if_icmpne -> 395
    //   383: bipush #8
    //   385: iload #7
    //   387: invokestatic min : (II)I
    //   390: istore #6
    //   392: goto -> 476
    //   395: iload #7
    //   397: istore #6
    //   399: iload #8
    //   401: iconst_2
    //   402: if_icmpne -> 476
    //   405: iload_3
    //   406: ifeq -> 425
    //   409: invokestatic isMac : ()Z
    //   412: ifeq -> 425
    //   415: iload #7
    //   417: istore #8
    //   419: invokestatic isPPC : ()Z
    //   422: ifne -> 435
    //   425: getstatic com/sun/jna/Native.MAX_ALIGNMENT : I
    //   428: iload #7
    //   430: invokestatic min : (II)I
    //   433: istore #8
    //   435: iload #8
    //   437: istore #6
    //   439: iload_3
    //   440: ifne -> 476
    //   443: iload #8
    //   445: istore #6
    //   447: invokestatic isAIX : ()Z
    //   450: ifeq -> 476
    //   453: aload #4
    //   455: getstatic java/lang/Double.TYPE : Ljava/lang/Class;
    //   458: if_acmpeq -> 473
    //   461: iload #8
    //   463: istore #6
    //   465: aload #4
    //   467: ldc_w java/lang/Double
    //   470: if_acmpne -> 476
    //   473: iconst_4
    //   474: istore #6
    //   476: iload #6
    //   478: ireturn
  }
  
  protected int getNativeSize(Class<?> paramClass) {
    return getNativeSize(paramClass, null);
  }
  
  protected int getNativeSize(Class<?> paramClass, Object paramObject) {
    return Native.getNativeSize(paramClass, paramObject);
  }
  
  public Pointer getPointer() {
    ensureAllocated();
    return this.memory;
  }
  
  protected String getStringEncoding() {
    return this.encoding;
  }
  
  protected int getStructAlignment() {
    if (this.size == -1)
      calculateSize(true); 
    return this.structAlignment;
  }
  
  Pointer getTypeInfo() {
    Pointer pointer = getTypeInfo(this);
    cacheTypeInfo(pointer);
    return pointer;
  }
  
  TypeMapper getTypeMapper() {
    return this.typeMapper;
  }
  
  public int hashCode() {
    return (getPointer() != null) ? getPointer().hashCode() : getClass().hashCode();
  }
  
  public void read() {
    if (this.memory == PLACEHOLDER_MEMORY)
      return; 
    this.readCalled = true;
    ensureAllocated();
    if (busy().contains(this))
      return; 
    busy().add(this);
    if (this instanceof ByReference)
      reading().put(getPointer(), this); 
    try {
      Iterator<StructField> iterator = fields().values().iterator();
      while (iterator.hasNext())
        readField(iterator.next()); 
      return;
    } finally {
      busy().remove(this);
      if (reading().get(getPointer()) == this)
        reading().remove(getPointer()); 
    } 
  }
  
  protected Object readField(StructField paramStructField) {
    StringBuilder stringBuilder;
    int i = paramStructField.offset;
    Class<?> clazz = paramStructField.type;
    FromNativeConverter fromNativeConverter = paramStructField.readConverter;
    if (fromNativeConverter != null)
      clazz = fromNativeConverter.nativeType(); 
    boolean bool = Structure.class.isAssignableFrom(clazz);
    Object object = null;
    if (bool || Callback.class.isAssignableFrom(clazz) || (Platform.HAS_BUFFERS && Buffer.class.isAssignableFrom(clazz)) || Pointer.class.isAssignableFrom(clazz) || NativeMapped.class.isAssignableFrom(clazz) || clazz.isArray()) {
      stringBuilder = (StringBuilder)getFieldValue(paramStructField.field);
    } else {
      stringBuilder = null;
    } 
    if (clazz == String.class) {
      Pointer pointer = this.memory.getPointer(i);
      if (pointer != null)
        object = pointer.getString(0L, this.encoding); 
    } else {
      object = this.memory.getValue(i, clazz, stringBuilder);
    } 
    if (fromNativeConverter != null) {
      Object object1 = fromNativeConverter.fromNative(object, paramStructField.context);
      object = object1;
      if (stringBuilder != null) {
        object = object1;
        if (stringBuilder.equals(object1))
          object = stringBuilder; 
      } 
    } 
    if (clazz.equals(String.class) || clazz.equals(WString.class)) {
      Map<String, Object> map = this.nativeStrings;
      stringBuilder = new StringBuilder();
      stringBuilder.append(paramStructField.name);
      stringBuilder.append(".ptr");
      map.put(stringBuilder.toString(), this.memory.getPointer(i));
      map = this.nativeStrings;
      stringBuilder = new StringBuilder();
      stringBuilder.append(paramStructField.name);
      stringBuilder.append(".val");
      map.put(stringBuilder.toString(), object);
    } 
    setFieldValue(paramStructField.field, object, true);
    return object;
  }
  
  public Object readField(String paramString) {
    ensureAllocated();
    StructField structField = fields().get(paramString);
    if (structField != null)
      return readField(structField); 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("No such field: ");
    stringBuilder.append(paramString);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  protected void setAlignType(int paramInt) {
    this.alignType = paramInt;
    int i = paramInt;
    if (paramInt == 0) {
      paramInt = Native.getStructureAlignment(getClass());
      i = paramInt;
      if (paramInt == 0)
        if (Platform.isWindows()) {
          i = 3;
        } else {
          i = 2;
        }  
    } 
    this.actualAlignType = i;
    layoutChanged();
  }
  
  public void setAutoRead(boolean paramBoolean) {
    this.autoRead = paramBoolean;
  }
  
  public void setAutoSynch(boolean paramBoolean) {
    setAutoRead(paramBoolean);
    setAutoWrite(paramBoolean);
  }
  
  public void setAutoWrite(boolean paramBoolean) {
    this.autoWrite = paramBoolean;
  }
  
  void setFieldValue(Field paramField, Object paramObject) {
    setFieldValue(paramField, paramObject, false);
  }
  
  protected void setStringEncoding(String paramString) {
    this.encoding = paramString;
  }
  
  public int size() {
    ensureAllocated();
    return this.size;
  }
  
  protected void sortFields(List<Field> paramList, List<String> paramList1) {
    for (byte b = 0; b < paramList1.size(); b++) {
      String str = paramList1.get(b);
      for (byte b1 = 0; b1 < paramList.size(); b1++) {
        if (str.equals(((Field)paramList.get(b1)).getName())) {
          Collections.swap(paramList, b, b1);
          break;
        } 
      } 
    } 
  }
  
  public Structure[] toArray(int paramInt) {
    return toArray((Structure[])Array.newInstance(getClass(), paramInt));
  }
  
  public Structure[] toArray(Structure[] paramArrayOfStructure) {
    ensureAllocated();
    Pointer pointer = this.memory;
    if (pointer instanceof AutoAllocated) {
      pointer = pointer;
      int j = paramArrayOfStructure.length * size();
      if (pointer.size() < j)
        useMemory(autoAllocate(j)); 
    } 
    paramArrayOfStructure[0] = this;
    int i = size();
    for (byte b = 1; b < paramArrayOfStructure.length; b++) {
      paramArrayOfStructure[b] = newInstance(getClass(), this.memory.share((b * i), i));
      paramArrayOfStructure[b].conditionalAutoRead();
    } 
    if (!(this instanceof ByValue))
      this.array = paramArrayOfStructure; 
    return paramArrayOfStructure;
  }
  
  public String toString() {
    return toString(Boolean.getBoolean("jna.dump_memory"));
  }
  
  public String toString(boolean paramBoolean) {
    return toString(0, true, paramBoolean);
  }
  
  StructField typeInfoField() {
    synchronized (layoutInfo) {
      LayoutInfo layoutInfo = layoutInfo.get(getClass());
      return (layoutInfo != null) ? layoutInfo.typeInfoField : null;
    } 
  }
  
  protected void useMemory(Pointer paramPointer) {
    useMemory(paramPointer, 0);
  }
  
  protected void useMemory(Pointer paramPointer, int paramInt) {
    useMemory(paramPointer, paramInt, false);
  }
  
  void useMemory(Pointer paramPointer, int paramInt, boolean paramBoolean) {
    try {
      this.nativeStrings.clear();
      if (this instanceof ByValue && !paramBoolean) {
        byte[] arrayOfByte = new byte[size()];
        paramPointer.read(0L, arrayOfByte, 0, arrayOfByte.length);
        this.memory.write(0L, arrayOfByte, 0, arrayOfByte.length);
      } else {
        long l = paramInt;
        this.memory = paramPointer.share(l);
        if (this.size == -1)
          this.size = calculateSize(false); 
        if (this.size != -1)
          this.memory = paramPointer.share(l, this.size); 
      } 
      this.array = null;
      this.readCalled = false;
      return;
    } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
      throw new IllegalArgumentException("Structure exceeds provided memory bounds", indexOutOfBoundsException);
    } 
  }
  
  public void write() {
    if (this.memory == PLACEHOLDER_MEMORY)
      return; 
    ensureAllocated();
    if (this instanceof ByValue)
      getTypeInfo(); 
    if (busy().contains(this))
      return; 
    busy().add(this);
    try {
      for (StructField structField : fields().values()) {
        if (!structField.isVolatile)
          writeField(structField); 
      } 
      return;
    } finally {
      busy().remove(this);
    } 
  }
  
  protected void writeField(StructField paramStructField) {
    // Byte code:
    //   0: aload_1
    //   1: getfield isReadOnly : Z
    //   4: ifeq -> 8
    //   7: return
    //   8: aload_1
    //   9: getfield offset : I
    //   12: istore_2
    //   13: aload_0
    //   14: aload_1
    //   15: getfield field : Ljava/lang/reflect/Field;
    //   18: invokevirtual getFieldValue : (Ljava/lang/reflect/Field;)Ljava/lang/Object;
    //   21: astore_3
    //   22: aload_1
    //   23: getfield type : Ljava/lang/Class;
    //   26: astore #4
    //   28: aload_1
    //   29: getfield writeConverter : Lcom/sun/jna/ToNativeConverter;
    //   32: astore #5
    //   34: aload_3
    //   35: astore #6
    //   37: aload #5
    //   39: ifnull -> 73
    //   42: aload #5
    //   44: aload_3
    //   45: new com/sun/jna/StructureWriteContext
    //   48: dup
    //   49: aload_0
    //   50: aload_1
    //   51: getfield field : Ljava/lang/reflect/Field;
    //   54: invokespecial <init> : (Lcom/sun/jna/Structure;Ljava/lang/reflect/Field;)V
    //   57: invokeinterface toNative : (Ljava/lang/Object;Lcom/sun/jna/ToNativeContext;)Ljava/lang/Object;
    //   62: astore #6
    //   64: aload #5
    //   66: invokeinterface nativeType : ()Ljava/lang/Class;
    //   71: astore #4
    //   73: ldc_w java/lang/String
    //   76: aload #4
    //   78: if_acmpeq -> 92
    //   81: aload #6
    //   83: astore_3
    //   84: ldc_w com/sun/jna/WString
    //   87: aload #4
    //   89: if_acmpne -> 384
    //   92: aload #4
    //   94: ldc_w com/sun/jna/WString
    //   97: if_acmpne -> 106
    //   100: iconst_1
    //   101: istore #7
    //   103: goto -> 109
    //   106: iconst_0
    //   107: istore #7
    //   109: aload #6
    //   111: ifnull -> 281
    //   114: aload_0
    //   115: getfield nativeStrings : Ljava/util/Map;
    //   118: astore_3
    //   119: new java/lang/StringBuilder
    //   122: dup
    //   123: invokespecial <init> : ()V
    //   126: astore #5
    //   128: aload #5
    //   130: aload_1
    //   131: getfield name : Ljava/lang/String;
    //   134: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   137: pop
    //   138: aload #5
    //   140: ldc_w '.ptr'
    //   143: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   146: pop
    //   147: aload_3
    //   148: aload #5
    //   150: invokevirtual toString : ()Ljava/lang/String;
    //   153: invokeinterface containsKey : (Ljava/lang/Object;)Z
    //   158: ifeq -> 214
    //   161: aload_0
    //   162: getfield nativeStrings : Ljava/util/Map;
    //   165: astore_3
    //   166: new java/lang/StringBuilder
    //   169: dup
    //   170: invokespecial <init> : ()V
    //   173: astore #5
    //   175: aload #5
    //   177: aload_1
    //   178: getfield name : Ljava/lang/String;
    //   181: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   184: pop
    //   185: aload #5
    //   187: ldc_w '.val'
    //   190: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   193: pop
    //   194: aload #6
    //   196: aload_3
    //   197: aload #5
    //   199: invokevirtual toString : ()Ljava/lang/String;
    //   202: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   207: invokevirtual equals : (Ljava/lang/Object;)Z
    //   210: ifeq -> 214
    //   213: return
    //   214: iload #7
    //   216: ifeq -> 237
    //   219: new com/sun/jna/NativeString
    //   222: dup
    //   223: aload #6
    //   225: invokevirtual toString : ()Ljava/lang/String;
    //   228: iconst_1
    //   229: invokespecial <init> : (Ljava/lang/String;Z)V
    //   232: astore #6
    //   234: goto -> 255
    //   237: new com/sun/jna/NativeString
    //   240: dup
    //   241: aload #6
    //   243: invokevirtual toString : ()Ljava/lang/String;
    //   246: aload_0
    //   247: getfield encoding : Ljava/lang/String;
    //   250: invokespecial <init> : (Ljava/lang/String;Ljava/lang/String;)V
    //   253: astore #6
    //   255: aload_0
    //   256: getfield nativeStrings : Ljava/util/Map;
    //   259: aload_1
    //   260: getfield name : Ljava/lang/String;
    //   263: aload #6
    //   265: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   270: pop
    //   271: aload #6
    //   273: invokevirtual getPointer : ()Lcom/sun/jna/Pointer;
    //   276: astore #6
    //   278: goto -> 295
    //   281: aload_0
    //   282: getfield nativeStrings : Ljava/util/Map;
    //   285: aload_1
    //   286: getfield name : Ljava/lang/String;
    //   289: invokeinterface remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   294: pop
    //   295: aload_0
    //   296: getfield nativeStrings : Ljava/util/Map;
    //   299: astore #5
    //   301: new java/lang/StringBuilder
    //   304: dup
    //   305: invokespecial <init> : ()V
    //   308: astore_3
    //   309: aload_3
    //   310: aload_1
    //   311: getfield name : Ljava/lang/String;
    //   314: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   317: pop
    //   318: aload_3
    //   319: ldc_w '.ptr'
    //   322: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   325: pop
    //   326: aload #5
    //   328: aload_3
    //   329: invokevirtual toString : ()Ljava/lang/String;
    //   332: invokeinterface remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   337: pop
    //   338: aload_0
    //   339: getfield nativeStrings : Ljava/util/Map;
    //   342: astore #5
    //   344: new java/lang/StringBuilder
    //   347: dup
    //   348: invokespecial <init> : ()V
    //   351: astore_3
    //   352: aload_3
    //   353: aload_1
    //   354: getfield name : Ljava/lang/String;
    //   357: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   360: pop
    //   361: aload_3
    //   362: ldc_w '.val'
    //   365: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   368: pop
    //   369: aload #5
    //   371: aload_3
    //   372: invokevirtual toString : ()Ljava/lang/String;
    //   375: invokeinterface remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   380: pop
    //   381: aload #6
    //   383: astore_3
    //   384: aload_0
    //   385: getfield memory : Lcom/sun/jna/Pointer;
    //   388: iload_2
    //   389: i2l
    //   390: aload_3
    //   391: aload #4
    //   393: invokevirtual setValue : (JLjava/lang/Object;Ljava/lang/Class;)V
    //   396: return
    //   397: astore_3
    //   398: new java/lang/StringBuilder
    //   401: dup
    //   402: invokespecial <init> : ()V
    //   405: astore #6
    //   407: aload #6
    //   409: ldc_w 'Structure field "'
    //   412: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   415: pop
    //   416: aload #6
    //   418: aload_1
    //   419: getfield name : Ljava/lang/String;
    //   422: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   425: pop
    //   426: aload #6
    //   428: ldc_w '" was declared as '
    //   431: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   434: pop
    //   435: aload #6
    //   437: aload_1
    //   438: getfield type : Ljava/lang/Class;
    //   441: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   444: pop
    //   445: aload_1
    //   446: getfield type : Ljava/lang/Class;
    //   449: aload #4
    //   451: if_acmpne -> 461
    //   454: ldc_w ''
    //   457: astore_1
    //   458: goto -> 497
    //   461: new java/lang/StringBuilder
    //   464: dup
    //   465: invokespecial <init> : ()V
    //   468: astore_1
    //   469: aload_1
    //   470: ldc_w ' (native type '
    //   473: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   476: pop
    //   477: aload_1
    //   478: aload #4
    //   480: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   483: pop
    //   484: aload_1
    //   485: ldc_w ')'
    //   488: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   491: pop
    //   492: aload_1
    //   493: invokevirtual toString : ()Ljava/lang/String;
    //   496: astore_1
    //   497: aload #6
    //   499: aload_1
    //   500: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   503: pop
    //   504: aload #6
    //   506: ldc_w ', which is not supported within a Structure'
    //   509: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   512: pop
    //   513: new java/lang/IllegalArgumentException
    //   516: dup
    //   517: aload #6
    //   519: invokevirtual toString : ()Ljava/lang/String;
    //   522: aload_3
    //   523: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
    //   526: athrow
    // Exception table:
    //   from	to	target	type
    //   384	396	397	java/lang/IllegalArgumentException
  }
  
  public void writeField(String paramString) {
    ensureAllocated();
    StructField structField = fields().get(paramString);
    if (structField != null) {
      writeField(structField);
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("No such field: ");
    stringBuilder.append(paramString);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public void writeField(String paramString, Object paramObject) {
    ensureAllocated();
    StructField structField = fields().get(paramString);
    if (structField != null) {
      setFieldValue(structField.field, paramObject);
      writeField(structField);
      return;
    } 
    paramObject = new StringBuilder();
    paramObject.append("No such field: ");
    paramObject.append(paramString);
    throw new IllegalArgumentException(paramObject.toString());
  }
  
  private static class AutoAllocated extends Memory {
    public AutoAllocated(int param1Int) {
      super(param1Int);
      clear();
    }
    
    public String toString() {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("auto-");
      stringBuilder.append(super.toString());
      return stringBuilder.toString();
    }
  }
  
  public static interface ByReference {}
  
  public static interface ByValue {}
  
  @FieldOrder({"size", "alignment", "type", "elements"})
  static class FFIType extends Structure {
    private static final int FFI_TYPE_STRUCT = 13;
    
    private static final Map<Object, Object> typeInfoMap = new WeakHashMap<Object, Object>();
    
    public short alignment;
    
    public Pointer elements;
    
    public size_t size;
    
    public short type;
    
    static {
      if (Native.POINTER_SIZE != 0) {
        if (FFITypes.ffi_type_void != null) {
          Pointer pointer;
          typeInfoMap.put(void.class, FFITypes.ffi_type_void);
          typeInfoMap.put(Void.class, FFITypes.ffi_type_void);
          typeInfoMap.put(float.class, FFITypes.ffi_type_float);
          typeInfoMap.put(Float.class, FFITypes.ffi_type_float);
          typeInfoMap.put(double.class, FFITypes.ffi_type_double);
          typeInfoMap.put(Double.class, FFITypes.ffi_type_double);
          typeInfoMap.put(long.class, FFITypes.ffi_type_sint64);
          typeInfoMap.put(Long.class, FFITypes.ffi_type_sint64);
          typeInfoMap.put(int.class, FFITypes.ffi_type_sint32);
          typeInfoMap.put(Integer.class, FFITypes.ffi_type_sint32);
          typeInfoMap.put(short.class, FFITypes.ffi_type_sint16);
          typeInfoMap.put(Short.class, FFITypes.ffi_type_sint16);
          if (Native.WCHAR_SIZE == 2) {
            pointer = FFITypes.ffi_type_uint16;
          } else {
            pointer = FFITypes.ffi_type_uint32;
          } 
          typeInfoMap.put(char.class, pointer);
          typeInfoMap.put(Character.class, pointer);
          typeInfoMap.put(byte.class, FFITypes.ffi_type_sint8);
          typeInfoMap.put(Byte.class, FFITypes.ffi_type_sint8);
          typeInfoMap.put(Pointer.class, FFITypes.ffi_type_pointer);
          typeInfoMap.put(String.class, FFITypes.ffi_type_pointer);
          typeInfoMap.put(WString.class, FFITypes.ffi_type_pointer);
          typeInfoMap.put(boolean.class, FFITypes.ffi_type_uint32);
          typeInfoMap.put(Boolean.class, FFITypes.ffi_type_uint32);
          return;
        } 
        throw new Error("FFI types not initialized");
      } 
      throw new Error("Native library not initialized");
    }
    
    private FFIType(Structure param1Structure) {
      Pointer[] arrayOfPointer;
      this.type = (short)13;
      param1Structure.ensureAllocated(true);
      boolean bool = param1Structure instanceof Union;
      byte b = 0;
      if (bool) {
        Structure.StructField structField = ((Union)param1Structure).typeInfoField();
        arrayOfPointer = new Pointer[2];
        arrayOfPointer[0] = get(param1Structure.getFieldValue(structField.field), structField.type);
        arrayOfPointer[1] = null;
      } else {
        Pointer[] arrayOfPointer1 = new Pointer[param1Structure.fields().size() + 1];
        Iterator<Structure.StructField> iterator = param1Structure.fields().values().iterator();
        while (true) {
          arrayOfPointer = arrayOfPointer1;
          if (iterator.hasNext()) {
            arrayOfPointer1[b] = param1Structure.getFieldTypeInfo(iterator.next());
            b++;
            continue;
          } 
          break;
        } 
      } 
      init(arrayOfPointer);
    }
    
    private FFIType(Object param1Object, Class<?> param1Class) {
      this.type = (short)13;
      int i = Array.getLength(param1Object);
      param1Object = new Pointer[i + 1];
      Pointer pointer = get((Object)null, param1Class.getComponentType());
      for (byte b = 0; b < i; b++)
        param1Object[b] = pointer; 
      init((Pointer[])param1Object);
    }
    
    static Pointer get(Object param1Object) {
      return (param1Object == null) ? FFITypes.ffi_type_pointer : ((param1Object instanceof Class) ? get((Object)null, (Class)param1Object) : get(param1Object, param1Object.getClass()));
    }
    
    private static Pointer get(Object param1Object, Class<?> param1Class) {
      TypeMapper typeMapper = Native.getTypeMapper(param1Class);
      Class<?> clazz = param1Class;
      if (typeMapper != null) {
        ToNativeConverter toNativeConverter = typeMapper.getToNativeConverter(param1Class);
        clazz = param1Class;
        if (toNativeConverter != null)
          clazz = toNativeConverter.nativeType(); 
      } 
      synchronized (typeInfoMap) {
        ToNativeContext toNativeContext;
        param1Class = (Class<?>)typeInfoMap.get(clazz);
        if (param1Class instanceof Pointer) {
          param1Object = param1Class;
          return (Pointer)param1Object;
        } 
        if (param1Class instanceof FFIType) {
          param1Object = ((FFIType)param1Class).getPointer();
          return (Pointer)param1Object;
        } 
        if ((Platform.HAS_BUFFERS && Buffer.class.isAssignableFrom(clazz)) || Callback.class.isAssignableFrom(clazz)) {
          typeInfoMap.put(clazz, FFITypes.ffi_type_pointer);
          param1Object = FFITypes.ffi_type_pointer;
          return (Pointer)param1Object;
        } 
        if (Structure.class.isAssignableFrom(clazz)) {
          Object object = param1Object;
          if (param1Object == null)
            object = newInstance(clazz, Structure.PLACEHOLDER_MEMORY); 
          if (Structure.ByReference.class.isAssignableFrom(clazz)) {
            typeInfoMap.put(clazz, FFITypes.ffi_type_pointer);
            param1Object = FFITypes.ffi_type_pointer;
            return (Pointer)param1Object;
          } 
          param1Object = new FFIType();
          super((Structure)object);
          typeInfoMap.put(clazz, param1Object);
          param1Object = param1Object.getPointer();
          return (Pointer)param1Object;
        } 
        if (NativeMapped.class.isAssignableFrom(clazz)) {
          NativeMappedConverter nativeMappedConverter = NativeMappedConverter.getInstance(clazz);
          toNativeContext = new ToNativeContext();
          this();
          param1Object = get(nativeMappedConverter.toNative(param1Object, toNativeContext), nativeMappedConverter.nativeType());
          return (Pointer)param1Object;
        } 
        if (toNativeContext.isArray()) {
          FFIType fFIType = new FFIType();
          this(param1Object, (Class<?>)toNativeContext);
          typeInfoMap.put(param1Object, fFIType);
          param1Object = fFIType.getPointer();
          return (Pointer)param1Object;
        } 
        IllegalArgumentException illegalArgumentException = new IllegalArgumentException();
        param1Object = new StringBuilder();
        super();
        param1Object.append("Unsupported type ");
        param1Object.append(toNativeContext);
        this(param1Object.toString());
        throw illegalArgumentException;
      } 
    }
    
    private void init(Pointer[] param1ArrayOfPointer) {
      this.elements = new Memory((Native.POINTER_SIZE * param1ArrayOfPointer.length));
      this.elements.write(0L, param1ArrayOfPointer, 0, param1ArrayOfPointer.length);
      write();
    }
    
    private static class FFITypes {
      private static Pointer ffi_type_double;
      
      private static Pointer ffi_type_float;
      
      private static Pointer ffi_type_longdouble;
      
      private static Pointer ffi_type_pointer;
      
      private static Pointer ffi_type_sint16;
      
      private static Pointer ffi_type_sint32;
      
      private static Pointer ffi_type_sint64;
      
      private static Pointer ffi_type_sint8;
      
      private static Pointer ffi_type_uint16;
      
      private static Pointer ffi_type_uint32;
      
      private static Pointer ffi_type_uint64;
      
      private static Pointer ffi_type_uint8;
      
      private static Pointer ffi_type_void;
    }
    
    public static class size_t extends IntegerType {
      private static final long serialVersionUID = 1L;
      
      public size_t() {
        this(0L);
      }
      
      public size_t(long param2Long) {
        super(Native.SIZE_T_SIZE, param2Long);
      }
    }
  }
  
  private static class FFITypes {
    private static Pointer ffi_type_double;
    
    private static Pointer ffi_type_float;
    
    private static Pointer ffi_type_longdouble;
    
    private static Pointer ffi_type_pointer;
    
    private static Pointer ffi_type_sint16;
    
    private static Pointer ffi_type_sint32;
    
    private static Pointer ffi_type_sint64;
    
    private static Pointer ffi_type_sint8;
    
    private static Pointer ffi_type_uint16;
    
    private static Pointer ffi_type_uint32;
    
    private static Pointer ffi_type_uint64;
    
    private static Pointer ffi_type_uint8;
    
    private static Pointer ffi_type_void;
  }
  
  public static class size_t extends IntegerType {
    private static final long serialVersionUID = 1L;
    
    public size_t() {
      this(0L);
    }
    
    public size_t(long param1Long) {
      super(Native.SIZE_T_SIZE, param1Long);
    }
  }
  
  @Documented
  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.TYPE})
  public static @interface FieldOrder {
    String[] value();
  }
  
  private static class LayoutInfo {
    private int alignType = 0;
    
    private int alignment = 1;
    
    private final Map<String, Structure.StructField> fields = Collections.synchronizedMap(new LinkedHashMap<String, Structure.StructField>());
    
    private int size = -1;
    
    private Structure.StructField typeInfoField;
    
    private TypeMapper typeMapper;
    
    private boolean variable;
    
    private LayoutInfo() {}
  }
  
  protected static class StructField {
    public FromNativeContext context;
    
    public Field field;
    
    public boolean isReadOnly;
    
    public boolean isVolatile;
    
    public String name;
    
    public int offset = -1;
    
    public FromNativeConverter readConverter;
    
    public int size = -1;
    
    public Class<?> type;
    
    public ToNativeConverter writeConverter;
    
    public String toString() {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(this.name);
      stringBuilder.append("@");
      stringBuilder.append(this.offset);
      stringBuilder.append("[");
      stringBuilder.append(this.size);
      stringBuilder.append("] (");
      stringBuilder.append(this.type);
      stringBuilder.append(")");
      return stringBuilder.toString();
    }
  }
  
  static class StructureSet extends AbstractCollection<Structure> implements Set<Structure> {
    private int count;
    
    Structure[] elements;
    
    private void ensureCapacity(int param1Int) {
      Structure[] arrayOfStructure = this.elements;
      if (arrayOfStructure == null) {
        this.elements = new Structure[param1Int * 3 / 2];
      } else if (arrayOfStructure.length < param1Int) {
        Structure[] arrayOfStructure1 = new Structure[param1Int * 3 / 2];
        System.arraycopy(arrayOfStructure, 0, arrayOfStructure1, 0, arrayOfStructure.length);
        this.elements = arrayOfStructure1;
      } 
    }
    
    private int indexOf(Structure param1Structure) {
      for (byte b = 0; b < this.count; b++) {
        Structure structure = this.elements[b];
        if (param1Structure == structure || (param1Structure.getClass() == structure.getClass() && param1Structure.size() == structure.size() && param1Structure.getPointer().equals(structure.getPointer())))
          return b; 
      } 
      return -1;
    }
    
    public boolean add(Structure param1Structure) {
      if (!contains(param1Structure)) {
        ensureCapacity(this.count + 1);
        Structure[] arrayOfStructure = this.elements;
        int i = this.count;
        this.count = i + 1;
        arrayOfStructure[i] = param1Structure;
      } 
      return true;
    }
    
    public boolean contains(Object param1Object) {
      boolean bool;
      if (indexOf((Structure)param1Object) != -1) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    public Structure[] getElements() {
      return this.elements;
    }
    
    public Iterator<Structure> iterator() {
      int i = this.count;
      Structure[] arrayOfStructure = new Structure[i];
      if (i > 0)
        System.arraycopy(this.elements, 0, arrayOfStructure, 0, i); 
      return Arrays.<Structure>asList(arrayOfStructure).iterator();
    }
    
    public boolean remove(Object param1Object) {
      int i = indexOf((Structure)param1Object);
      if (i != -1) {
        int j = this.count - 1;
        this.count = j;
        if (j >= 0) {
          param1Object = this.elements;
          j = this.count;
          param1Object[i] = param1Object[j];
          param1Object[j] = null;
        } 
        return true;
      } 
      return false;
    }
    
    public int size() {
      return this.count;
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/sun/jna/Structure.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */