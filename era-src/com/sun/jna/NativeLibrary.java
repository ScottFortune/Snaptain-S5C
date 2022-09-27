package com.sun.jna;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.ref.Reference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NativeLibrary {
  static {
    if (Native.DEBUG_LOAD) {
      level = Level.INFO;
    } else {
      level = Level.FINE;
    } 
    DEBUG_LOAD_LEVEL = level;
    libraries = new HashMap<String, Reference<NativeLibrary>>();
    searchPaths = Collections.synchronizedMap(new HashMap<String, List<String>>());
    librarySearchPath = new ArrayList<String>();
    if (Native.POINTER_SIZE != 0) {
      addSuppressedMethod = null;
      byte b = 0;
      try {
        addSuppressedMethod = Throwable.class.getMethod("addSuppressed", new Class[] { Throwable.class });
      } catch (NoSuchMethodException noSuchMethodException) {
      
      } catch (SecurityException securityException) {
        Logger.getLogger(NativeLibrary.class.getName()).log(Level.SEVERE, "Failed to initialize 'addSuppressed' method", securityException);
      } 
      String str = Native.getWebStartLibraryPath("jnidispatch");
      if (str != null)
        librarySearchPath.add(str); 
      if (System.getProperty("jna.platform.library.path") == null && !Platform.isWindows()) {
        String str2;
        if (Platform.isLinux() || Platform.isSolaris() || Platform.isFreeBSD() || Platform.iskFreeBSD()) {
          StringBuilder stringBuilder1 = new StringBuilder();
          if (Platform.isSolaris()) {
            str = "/";
          } else {
            str = "";
          } 
          stringBuilder1.append(str);
          stringBuilder1.append(Native.POINTER_SIZE * 8);
          str2 = stringBuilder1.toString();
        } else {
          str2 = "";
        } 
        String[] arrayOfString1 = new String[4];
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("/usr/lib");
        stringBuilder.append(str2);
        arrayOfString1[0] = stringBuilder.toString();
        stringBuilder = new StringBuilder();
        stringBuilder.append("/lib");
        stringBuilder.append(str2);
        arrayOfString1[1] = stringBuilder.toString();
        arrayOfString1[2] = "/usr/lib";
        arrayOfString1[3] = "/lib";
        if (Platform.isLinux() || Platform.iskFreeBSD() || Platform.isGNU()) {
          String str5 = getMultiArchPath();
          StringBuilder stringBuilder1 = new StringBuilder();
          stringBuilder1.append("/usr/lib/");
          stringBuilder1.append(str5);
          String str4 = stringBuilder1.toString();
          StringBuilder stringBuilder2 = new StringBuilder();
          stringBuilder2.append("/lib/");
          stringBuilder2.append(str5);
          str5 = stringBuilder2.toString();
          stringBuilder2 = new StringBuilder();
          stringBuilder2.append("/usr/lib");
          stringBuilder2.append(str2);
          String str6 = stringBuilder2.toString();
          stringBuilder2 = new StringBuilder();
          stringBuilder2.append("/lib");
          stringBuilder2.append(str2);
          arrayOfString1 = new String[] { str4, str5, str6, stringBuilder2.toString(), "/usr/lib", "/lib" };
        } 
        String[] arrayOfString2 = arrayOfString1;
        if (Platform.isLinux()) {
          ArrayList<String> arrayList = getLinuxLdPaths();
          for (int i = arrayOfString1.length - 1; i >= 0; i--) {
            int j = arrayList.indexOf(arrayOfString1[i]);
            if (j != -1)
              arrayList.remove(j); 
            arrayList.add(0, arrayOfString1[i]);
          } 
          arrayOfString2 = arrayList.<String>toArray(new String[0]);
        } 
        String str3 = "";
        String str1 = str3;
        byte b1 = b;
        while (b1 < arrayOfString2.length) {
          File file = new File(arrayOfString2[b1]);
          String str5 = str3;
          String str4 = str1;
          if (file.exists()) {
            str5 = str3;
            str4 = str1;
            if (file.isDirectory()) {
              StringBuilder stringBuilder1 = new StringBuilder();
              stringBuilder1.append(str3);
              stringBuilder1.append(str1);
              stringBuilder1.append(arrayOfString2[b1]);
              str5 = stringBuilder1.toString();
              str4 = File.pathSeparator;
            } 
          } 
          b1++;
          str3 = str5;
          str1 = str4;
        } 
        if (!"".equals(str3))
          System.setProperty("jna.platform.library.path", str3); 
      } 
      librarySearchPath.addAll(initPaths("jna.platform.library.path"));
      return;
    } 
    Error error = new Error("Native library not initialized");
    throw error;
  }
  
  private NativeLibrary(String paramString1, String paramString2, long paramLong, Map<String, ?> paramMap) {
    boolean bool;
    this.functions = new HashMap<String, Function>();
    this.libraryName = getLibraryName(paramString1);
    this.libraryPath = paramString2;
    this.handle = paramLong;
    paramString1 = (String)paramMap.get("calling-convention");
    if (paramString1 instanceof Number) {
      bool = ((Number)paramString1).intValue();
    } else {
      bool = false;
    } 
    this.callFlags = bool;
    this.options = paramMap;
    this.encoding = (String)paramMap.get("string-encoding");
    if (this.encoding == null)
      this.encoding = Native.getDefaultStringEncoding(); 
    if (Platform.isWindows() && "kernel32".equals(this.libraryName.toLowerCase()))
      synchronized (this.functions) {
        Function function = new Function() {
            Object invoke(Method param1Method, Class<?>[] param1ArrayOfClass, Class<?> param1Class, Object[] param1ArrayOfObject, Map<String, ?> param1Map) {
              return Integer.valueOf(Native.getLastError());
            }
            
            Object invoke(Object[] param1ArrayOfObject, Class<?> param1Class, boolean param1Boolean, int param1Int) {
              return Integer.valueOf(Native.getLastError());
            }
          };
        super(this, this, "GetLastError", 63, this.encoding);
        this.functions.put(functionKey("GetLastError", this.callFlags, this.encoding), function);
      }  
  }
  
  public static final void addSearchPath(String paramString1, String paramString2) {
    synchronized (searchPaths) {
      List<?> list1 = searchPaths.get(paramString1);
      List<?> list2 = list1;
      if (list1 == null) {
        list2 = new ArrayList();
        super();
        list2 = Collections.synchronizedList(list2);
        searchPaths.put(paramString1, list2);
      } 
      list2.add(paramString2);
      return;
    } 
  }
  
  private static void addSuppressedReflected(Throwable paramThrowable1, Throwable paramThrowable2) {
    Method method = addSuppressedMethod;
    if (method == null)
      return; 
    try {
      method.invoke(paramThrowable1, new Object[] { paramThrowable2 });
      return;
    } catch (IllegalAccessException illegalAccessException) {
      throw new RuntimeException("Failed to call addSuppressedMethod", illegalAccessException);
    } catch (IllegalArgumentException illegalArgumentException) {
      throw new RuntimeException("Failed to call addSuppressedMethod", illegalArgumentException);
    } catch (InvocationTargetException invocationTargetException) {
      throw new RuntimeException("Failed to call addSuppressedMethod", invocationTargetException);
    } 
  }
  
  static void disposeAll() {
    Map<String, Reference<NativeLibrary>> map;
    Iterator<Reference<NativeLibrary>> iterator;
    synchronized (libraries) {
      LinkedHashSet linkedHashSet = new LinkedHashSet();
      this((Collection)libraries.values());
      iterator = linkedHashSet.iterator();
      while (iterator.hasNext()) {
        NativeLibrary nativeLibrary = ((Reference<NativeLibrary>)iterator.next()).get();
        if (nativeLibrary != null)
          nativeLibrary.dispose(); 
      } 
      return;
    } 
  }
  
  private static String findLibraryPath(String paramString, List<String> paramList) {
    String str1;
    if ((new File(paramString)).isAbsolute())
      return paramString; 
    String str2 = mapSharedLibraryName(paramString);
    Iterator<String> iterator = paramList.iterator();
    while (true) {
      paramString = str2;
      if (iterator.hasNext()) {
        paramString = iterator.next();
        File file = new File(paramString, str2);
        if (file.exists())
          return file.getAbsolutePath(); 
        if (Platform.isMac() && str2.endsWith(".dylib")) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append(str2.substring(0, str2.lastIndexOf(".dylib")));
          stringBuilder.append(".jnilib");
          File file1 = new File(paramString, stringBuilder.toString());
          if (file1.exists()) {
            str1 = file1.getAbsolutePath();
            break;
          } 
        } 
        continue;
      } 
      break;
    } 
    return str1;
  }
  
  private static String functionKey(String paramString1, int paramInt, String paramString2) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(paramString1);
    stringBuilder.append("|");
    stringBuilder.append(paramInt);
    stringBuilder.append("|");
    stringBuilder.append(paramString2);
    return stringBuilder.toString();
  }
  
  public static final NativeLibrary getInstance(String paramString) {
    return getInstance(paramString, Collections.emptyMap());
  }
  
  public static final NativeLibrary getInstance(String paramString, ClassLoader paramClassLoader) {
    return getInstance(paramString, Collections.singletonMap("classloader", paramClassLoader));
  }
  
  public static final NativeLibrary getInstance(String paramString, Map<String, ?> paramMap) {
    // Byte code:
    //   0: new java/util/HashMap
    //   3: dup
    //   4: aload_1
    //   5: invokespecial <init> : (Ljava/util/Map;)V
    //   8: astore_2
    //   9: aload_2
    //   10: ldc 'calling-convention'
    //   12: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   17: ifnonnull -> 33
    //   20: aload_2
    //   21: ldc 'calling-convention'
    //   23: iconst_0
    //   24: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   27: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   32: pop
    //   33: invokestatic isLinux : ()Z
    //   36: ifne -> 53
    //   39: invokestatic isFreeBSD : ()Z
    //   42: ifne -> 53
    //   45: aload_0
    //   46: astore_1
    //   47: invokestatic isAIX : ()Z
    //   50: ifeq -> 67
    //   53: aload_0
    //   54: astore_1
    //   55: getstatic com/sun/jna/Platform.C_LIBRARY_NAME : Ljava/lang/String;
    //   58: aload_0
    //   59: invokevirtual equals : (Ljava/lang/Object;)Z
    //   62: ifeq -> 67
    //   65: aconst_null
    //   66: astore_1
    //   67: getstatic com/sun/jna/NativeLibrary.libraries : Ljava/util/Map;
    //   70: astore_3
    //   71: aload_3
    //   72: monitorenter
    //   73: getstatic com/sun/jna/NativeLibrary.libraries : Ljava/util/Map;
    //   76: astore_0
    //   77: new java/lang/StringBuilder
    //   80: astore #4
    //   82: aload #4
    //   84: invokespecial <init> : ()V
    //   87: aload #4
    //   89: aload_1
    //   90: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   93: pop
    //   94: aload #4
    //   96: aload_2
    //   97: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   100: pop
    //   101: aload_0
    //   102: aload #4
    //   104: invokevirtual toString : ()Ljava/lang/String;
    //   107: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   112: checkcast java/lang/ref/Reference
    //   115: astore_0
    //   116: aload_0
    //   117: ifnull -> 131
    //   120: aload_0
    //   121: invokevirtual get : ()Ljava/lang/Object;
    //   124: checkcast com/sun/jna/NativeLibrary
    //   127: astore_0
    //   128: goto -> 133
    //   131: aconst_null
    //   132: astore_0
    //   133: aload_0
    //   134: astore #4
    //   136: aload_0
    //   137: ifnonnull -> 340
    //   140: aload_1
    //   141: ifnonnull -> 168
    //   144: new com/sun/jna/NativeLibrary
    //   147: astore_0
    //   148: aload_0
    //   149: ldc_w '<process>'
    //   152: aconst_null
    //   153: aconst_null
    //   154: aload_2
    //   155: invokestatic openFlags : (Ljava/util/Map;)I
    //   158: invokestatic open : (Ljava/lang/String;I)J
    //   161: aload_2
    //   162: invokespecial <init> : (Ljava/lang/String;Ljava/lang/String;JLjava/util/Map;)V
    //   165: goto -> 174
    //   168: aload_1
    //   169: aload_2
    //   170: invokestatic loadLibrary : (Ljava/lang/String;Ljava/util/Map;)Lcom/sun/jna/NativeLibrary;
    //   173: astore_0
    //   174: new java/lang/ref/WeakReference
    //   177: astore_1
    //   178: aload_1
    //   179: aload_0
    //   180: invokespecial <init> : (Ljava/lang/Object;)V
    //   183: getstatic com/sun/jna/NativeLibrary.libraries : Ljava/util/Map;
    //   186: astore #5
    //   188: new java/lang/StringBuilder
    //   191: astore #4
    //   193: aload #4
    //   195: invokespecial <init> : ()V
    //   198: aload #4
    //   200: aload_0
    //   201: invokevirtual getName : ()Ljava/lang/String;
    //   204: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   207: pop
    //   208: aload #4
    //   210: aload_2
    //   211: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   214: pop
    //   215: aload #5
    //   217: aload #4
    //   219: invokevirtual toString : ()Ljava/lang/String;
    //   222: aload_1
    //   223: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   228: pop
    //   229: aload_0
    //   230: invokevirtual getFile : ()Ljava/io/File;
    //   233: astore #5
    //   235: aload_0
    //   236: astore #4
    //   238: aload #5
    //   240: ifnull -> 340
    //   243: getstatic com/sun/jna/NativeLibrary.libraries : Ljava/util/Map;
    //   246: astore #6
    //   248: new java/lang/StringBuilder
    //   251: astore #4
    //   253: aload #4
    //   255: invokespecial <init> : ()V
    //   258: aload #4
    //   260: aload #5
    //   262: invokevirtual getAbsolutePath : ()Ljava/lang/String;
    //   265: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   268: pop
    //   269: aload #4
    //   271: aload_2
    //   272: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   275: pop
    //   276: aload #6
    //   278: aload #4
    //   280: invokevirtual toString : ()Ljava/lang/String;
    //   283: aload_1
    //   284: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   289: pop
    //   290: getstatic com/sun/jna/NativeLibrary.libraries : Ljava/util/Map;
    //   293: astore #4
    //   295: new java/lang/StringBuilder
    //   298: astore #6
    //   300: aload #6
    //   302: invokespecial <init> : ()V
    //   305: aload #6
    //   307: aload #5
    //   309: invokevirtual getName : ()Ljava/lang/String;
    //   312: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   315: pop
    //   316: aload #6
    //   318: aload_2
    //   319: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   322: pop
    //   323: aload #4
    //   325: aload #6
    //   327: invokevirtual toString : ()Ljava/lang/String;
    //   330: aload_1
    //   331: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   336: pop
    //   337: aload_0
    //   338: astore #4
    //   340: aload_3
    //   341: monitorexit
    //   342: aload #4
    //   344: areturn
    //   345: astore_0
    //   346: aload_3
    //   347: monitorexit
    //   348: aload_0
    //   349: athrow
    // Exception table:
    //   from	to	target	type
    //   73	116	345	finally
    //   120	128	345	finally
    //   144	165	345	finally
    //   168	174	345	finally
    //   174	235	345	finally
    //   243	337	345	finally
    //   340	342	345	finally
    //   346	348	345	finally
  }
  
  private String getLibraryName(String paramString) {
    String str1 = mapSharedLibraryName("---");
    int i = str1.indexOf("---");
    String str2 = paramString;
    if (i > 0) {
      str2 = paramString;
      if (paramString.startsWith(str1.substring(0, i)))
        str2 = paramString.substring(i); 
    } 
    i = str2.indexOf(str1.substring(i + 3));
    paramString = str2;
    if (i != -1)
      paramString = str2.substring(0, i); 
    return paramString;
  }
  
  private static ArrayList<String> getLinuxLdPaths() {
    // Byte code:
    //   0: new java/util/ArrayList
    //   3: dup
    //   4: invokespecial <init> : ()V
    //   7: astore_0
    //   8: aconst_null
    //   9: astore_1
    //   10: aconst_null
    //   11: astore_2
    //   12: invokestatic getRuntime : ()Ljava/lang/Runtime;
    //   15: ldc_w '/sbin/ldconfig -p'
    //   18: invokevirtual exec : (Ljava/lang/String;)Ljava/lang/Process;
    //   21: astore_3
    //   22: new java/io/BufferedReader
    //   25: astore #4
    //   27: new java/io/InputStreamReader
    //   30: astore #5
    //   32: aload #5
    //   34: aload_3
    //   35: invokevirtual getInputStream : ()Ljava/io/InputStream;
    //   38: invokespecial <init> : (Ljava/io/InputStream;)V
    //   41: aload #4
    //   43: aload #5
    //   45: invokespecial <init> : (Ljava/io/Reader;)V
    //   48: aload #4
    //   50: invokevirtual readLine : ()Ljava/lang/String;
    //   53: astore_2
    //   54: aload_2
    //   55: ifnull -> 122
    //   58: aload_2
    //   59: ldc_w ' => '
    //   62: invokevirtual indexOf : (Ljava/lang/String;)I
    //   65: istore #6
    //   67: aload_2
    //   68: bipush #47
    //   70: invokevirtual lastIndexOf : (I)I
    //   73: istore #7
    //   75: iload #6
    //   77: iconst_m1
    //   78: if_icmpeq -> 48
    //   81: iload #7
    //   83: iconst_m1
    //   84: if_icmpeq -> 48
    //   87: iload #6
    //   89: iload #7
    //   91: if_icmpge -> 48
    //   94: aload_2
    //   95: iload #6
    //   97: iconst_4
    //   98: iadd
    //   99: iload #7
    //   101: invokevirtual substring : (II)Ljava/lang/String;
    //   104: astore_2
    //   105: aload_0
    //   106: aload_2
    //   107: invokevirtual contains : (Ljava/lang/Object;)Z
    //   110: ifne -> 48
    //   113: aload_0
    //   114: aload_2
    //   115: invokevirtual add : (Ljava/lang/Object;)Z
    //   118: pop
    //   119: goto -> 48
    //   122: aload #4
    //   124: invokevirtual close : ()V
    //   127: goto -> 174
    //   130: astore_2
    //   131: aload #4
    //   133: astore_1
    //   134: aload_2
    //   135: astore #4
    //   137: aload_1
    //   138: astore_2
    //   139: goto -> 148
    //   142: astore_2
    //   143: goto -> 164
    //   146: astore #4
    //   148: aload_2
    //   149: ifnull -> 156
    //   152: aload_2
    //   153: invokevirtual close : ()V
    //   156: aload #4
    //   158: athrow
    //   159: astore #4
    //   161: aload_1
    //   162: astore #4
    //   164: aload #4
    //   166: ifnull -> 174
    //   169: aload #4
    //   171: invokevirtual close : ()V
    //   174: aload_0
    //   175: areturn
    //   176: astore #4
    //   178: goto -> 174
    //   181: astore_2
    //   182: goto -> 156
    // Exception table:
    //   from	to	target	type
    //   12	48	159	java/lang/Exception
    //   12	48	146	finally
    //   48	54	142	java/lang/Exception
    //   48	54	130	finally
    //   58	75	142	java/lang/Exception
    //   58	75	130	finally
    //   94	119	142	java/lang/Exception
    //   94	119	130	finally
    //   122	127	176	java/io/IOException
    //   152	156	181	java/io/IOException
    //   169	174	176	java/io/IOException
  }
  
  private static String getMultiArchPath() {
    String str2;
    String str4;
    String str1 = Platform.ARCH;
    if (Platform.iskFreeBSD()) {
      str2 = "-kfreebsd";
    } else if (Platform.isGNU()) {
      str2 = "";
    } else {
      str2 = "-linux";
    } 
    boolean bool = Platform.isIntel();
    String str3 = "-gnu";
    if (bool) {
      if (Platform.is64Bit()) {
        str4 = "x86_64";
      } else {
        str4 = "i386";
      } 
    } else if (Platform.isPPC()) {
      if (Platform.is64Bit()) {
        str4 = "powerpc64";
      } else {
        str4 = "powerpc";
      } 
    } else if (Platform.isARM()) {
      str4 = "arm";
      str3 = "-gnueabi";
    } else {
      str4 = str1;
      if (Platform.ARCH.equals("mips64el")) {
        str3 = "-gnuabi64";
        str4 = str1;
      } 
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(str4);
    stringBuilder.append(str2);
    stringBuilder.append(str3);
    return stringBuilder.toString();
  }
  
  public static final NativeLibrary getProcess() {
    // Byte code:
    //   0: ldc com/sun/jna/NativeLibrary
    //   2: monitorenter
    //   3: aconst_null
    //   4: invokestatic getInstance : (Ljava/lang/String;)Lcom/sun/jna/NativeLibrary;
    //   7: astore_0
    //   8: ldc com/sun/jna/NativeLibrary
    //   10: monitorexit
    //   11: aload_0
    //   12: areturn
    //   13: astore_0
    //   14: ldc com/sun/jna/NativeLibrary
    //   16: monitorexit
    //   17: aload_0
    //   18: athrow
    // Exception table:
    //   from	to	target	type
    //   3	8	13	finally
  }
  
  public static final NativeLibrary getProcess(Map<String, ?> paramMap) {
    // Byte code:
    //   0: ldc com/sun/jna/NativeLibrary
    //   2: monitorenter
    //   3: aconst_null
    //   4: aload_0
    //   5: invokestatic getInstance : (Ljava/lang/String;Ljava/util/Map;)Lcom/sun/jna/NativeLibrary;
    //   8: astore_0
    //   9: ldc com/sun/jna/NativeLibrary
    //   11: monitorexit
    //   12: aload_0
    //   13: areturn
    //   14: astore_0
    //   15: ldc com/sun/jna/NativeLibrary
    //   17: monitorexit
    //   18: aload_0
    //   19: athrow
    // Exception table:
    //   from	to	target	type
    //   3	9	14	finally
  }
  
  private static List<String> initPaths(String paramString) {
    paramString = System.getProperty(paramString, "");
    if ("".equals(paramString))
      return Collections.emptyList(); 
    StringTokenizer stringTokenizer = new StringTokenizer(paramString, File.pathSeparator);
    ArrayList<String> arrayList = new ArrayList();
    while (stringTokenizer.hasMoreTokens()) {
      String str = stringTokenizer.nextToken();
      if (!"".equals(str))
        arrayList.add(str); 
    } 
    return arrayList;
  }
  
  private static boolean isVersionedName(String paramString) {
    if (paramString.startsWith("lib")) {
      int i = paramString.lastIndexOf(".so.");
      i += 4;
      if (i != -1 && i < paramString.length()) {
        while (i < paramString.length()) {
          char c = paramString.charAt(i);
          if (!Character.isDigit(c) && c != '.')
            return false; 
          i++;
        } 
        return true;
      } 
    } 
    return false;
  }
  
  private static NativeLibrary loadLibrary(String paramString, Map<String, ?> paramMap) {
    // Byte code:
    //   0: getstatic com/sun/jna/NativeLibrary.LOG : Ljava/util/logging/Logger;
    //   3: astore_2
    //   4: getstatic com/sun/jna/NativeLibrary.DEBUG_LOAD_LEVEL : Ljava/util/logging/Level;
    //   7: astore_3
    //   8: new java/lang/StringBuilder
    //   11: dup
    //   12: invokespecial <init> : ()V
    //   15: astore #4
    //   17: aload #4
    //   19: ldc_w 'Looking for library ''
    //   22: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   25: pop
    //   26: aload #4
    //   28: aload_0
    //   29: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   32: pop
    //   33: aload #4
    //   35: ldc_w '''
    //   38: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   41: pop
    //   42: aload_2
    //   43: aload_3
    //   44: aload #4
    //   46: invokevirtual toString : ()Ljava/lang/String;
    //   49: invokevirtual log : (Ljava/util/logging/Level;Ljava/lang/String;)V
    //   52: new java/util/ArrayList
    //   55: dup
    //   56: invokespecial <init> : ()V
    //   59: astore #5
    //   61: new java/io/File
    //   64: dup
    //   65: aload_0
    //   66: invokespecial <init> : (Ljava/lang/String;)V
    //   69: invokevirtual isAbsolute : ()Z
    //   72: istore #6
    //   74: new java/util/ArrayList
    //   77: dup
    //   78: invokespecial <init> : ()V
    //   81: astore #4
    //   83: aload_1
    //   84: invokestatic openFlags : (Ljava/util/Map;)I
    //   87: istore #7
    //   89: aload_0
    //   90: invokestatic getWebStartLibraryPath : (Ljava/lang/String;)Ljava/lang/String;
    //   93: astore #8
    //   95: aload #8
    //   97: ifnull -> 152
    //   100: getstatic com/sun/jna/NativeLibrary.LOG : Ljava/util/logging/Logger;
    //   103: astore #9
    //   105: getstatic com/sun/jna/NativeLibrary.DEBUG_LOAD_LEVEL : Ljava/util/logging/Level;
    //   108: astore_2
    //   109: new java/lang/StringBuilder
    //   112: dup
    //   113: invokespecial <init> : ()V
    //   116: astore_3
    //   117: aload_3
    //   118: ldc_w 'Adding web start path '
    //   121: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   124: pop
    //   125: aload_3
    //   126: aload #8
    //   128: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   131: pop
    //   132: aload #9
    //   134: aload_2
    //   135: aload_3
    //   136: invokevirtual toString : ()Ljava/lang/String;
    //   139: invokevirtual log : (Ljava/util/logging/Level;Ljava/lang/String;)V
    //   142: aload #4
    //   144: aload #8
    //   146: invokeinterface add : (Ljava/lang/Object;)Z
    //   151: pop
    //   152: getstatic com/sun/jna/NativeLibrary.searchPaths : Ljava/util/Map;
    //   155: aload_0
    //   156: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   161: checkcast java/util/List
    //   164: astore_3
    //   165: aload_3
    //   166: ifnull -> 191
    //   169: aload_3
    //   170: monitorenter
    //   171: aload #4
    //   173: iconst_0
    //   174: aload_3
    //   175: invokeinterface addAll : (ILjava/util/Collection;)Z
    //   180: pop
    //   181: aload_3
    //   182: monitorexit
    //   183: goto -> 191
    //   186: astore_0
    //   187: aload_3
    //   188: monitorexit
    //   189: aload_0
    //   190: athrow
    //   191: getstatic com/sun/jna/NativeLibrary.LOG : Ljava/util/logging/Logger;
    //   194: astore_3
    //   195: getstatic com/sun/jna/NativeLibrary.DEBUG_LOAD_LEVEL : Ljava/util/logging/Level;
    //   198: astore #9
    //   200: new java/lang/StringBuilder
    //   203: dup
    //   204: invokespecial <init> : ()V
    //   207: astore_2
    //   208: aload_2
    //   209: ldc_w 'Adding paths from jna.library.path: '
    //   212: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   215: pop
    //   216: aload_2
    //   217: ldc_w 'jna.library.path'
    //   220: invokestatic getProperty : (Ljava/lang/String;)Ljava/lang/String;
    //   223: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   226: pop
    //   227: aload_3
    //   228: aload #9
    //   230: aload_2
    //   231: invokevirtual toString : ()Ljava/lang/String;
    //   234: invokevirtual log : (Ljava/util/logging/Level;Ljava/lang/String;)V
    //   237: aload #4
    //   239: ldc_w 'jna.library.path'
    //   242: invokestatic initPaths : (Ljava/lang/String;)Ljava/util/List;
    //   245: invokeinterface addAll : (Ljava/util/Collection;)Z
    //   250: pop
    //   251: aload_0
    //   252: aload #4
    //   254: invokestatic findLibraryPath : (Ljava/lang/String;Ljava/util/List;)Ljava/lang/String;
    //   257: astore_2
    //   258: getstatic com/sun/jna/NativeLibrary.LOG : Ljava/util/logging/Logger;
    //   261: astore #8
    //   263: getstatic com/sun/jna/NativeLibrary.DEBUG_LOAD_LEVEL : Ljava/util/logging/Level;
    //   266: astore #9
    //   268: new java/lang/StringBuilder
    //   271: astore_3
    //   272: aload_3
    //   273: invokespecial <init> : ()V
    //   276: aload_3
    //   277: ldc_w 'Trying '
    //   280: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   283: pop
    //   284: aload_3
    //   285: aload_2
    //   286: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   289: pop
    //   290: aload #8
    //   292: aload #9
    //   294: aload_3
    //   295: invokevirtual toString : ()Ljava/lang/String;
    //   298: invokevirtual log : (Ljava/util/logging/Level;Ljava/lang/String;)V
    //   301: aload_2
    //   302: iload #7
    //   304: invokestatic open : (Ljava/lang/String;I)J
    //   307: lstore #10
    //   309: goto -> 435
    //   312: astore_3
    //   313: getstatic com/sun/jna/NativeLibrary.LOG : Ljava/util/logging/Logger;
    //   316: astore #8
    //   318: getstatic com/sun/jna/NativeLibrary.DEBUG_LOAD_LEVEL : Ljava/util/logging/Level;
    //   321: astore #12
    //   323: new java/lang/StringBuilder
    //   326: dup
    //   327: invokespecial <init> : ()V
    //   330: astore #9
    //   332: aload #9
    //   334: ldc_w 'Loading failed with message: '
    //   337: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   340: pop
    //   341: aload #9
    //   343: aload_3
    //   344: invokevirtual getMessage : ()Ljava/lang/String;
    //   347: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   350: pop
    //   351: aload #8
    //   353: aload #12
    //   355: aload #9
    //   357: invokevirtual toString : ()Ljava/lang/String;
    //   360: invokevirtual log : (Ljava/util/logging/Level;Ljava/lang/String;)V
    //   363: getstatic com/sun/jna/NativeLibrary.LOG : Ljava/util/logging/Logger;
    //   366: astore #12
    //   368: getstatic com/sun/jna/NativeLibrary.DEBUG_LOAD_LEVEL : Ljava/util/logging/Level;
    //   371: astore #9
    //   373: new java/lang/StringBuilder
    //   376: dup
    //   377: invokespecial <init> : ()V
    //   380: astore #8
    //   382: aload #8
    //   384: ldc_w 'Adding system paths: '
    //   387: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   390: pop
    //   391: aload #8
    //   393: getstatic com/sun/jna/NativeLibrary.librarySearchPath : Ljava/util/List;
    //   396: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   399: pop
    //   400: aload #12
    //   402: aload #9
    //   404: aload #8
    //   406: invokevirtual toString : ()Ljava/lang/String;
    //   409: invokevirtual log : (Ljava/util/logging/Level;Ljava/lang/String;)V
    //   412: aload #5
    //   414: aload_3
    //   415: invokeinterface add : (Ljava/lang/Object;)Z
    //   420: pop
    //   421: aload #4
    //   423: getstatic com/sun/jna/NativeLibrary.librarySearchPath : Ljava/util/List;
    //   426: invokeinterface addAll : (Ljava/util/Collection;)Z
    //   431: pop
    //   432: lconst_0
    //   433: lstore #10
    //   435: aload_2
    //   436: astore_3
    //   437: lload #10
    //   439: lstore #13
    //   441: lload #10
    //   443: lconst_0
    //   444: lcmp
    //   445: ifne -> 1765
    //   448: lload #10
    //   450: lstore #13
    //   452: aload_0
    //   453: aload #4
    //   455: invokestatic findLibraryPath : (Ljava/lang/String;Ljava/util/List;)Ljava/lang/String;
    //   458: astore_3
    //   459: aload_3
    //   460: astore_2
    //   461: lload #10
    //   463: lstore #13
    //   465: getstatic com/sun/jna/NativeLibrary.LOG : Ljava/util/logging/Logger;
    //   468: astore #12
    //   470: aload_3
    //   471: astore_2
    //   472: lload #10
    //   474: lstore #13
    //   476: getstatic com/sun/jna/NativeLibrary.DEBUG_LOAD_LEVEL : Ljava/util/logging/Level;
    //   479: astore #8
    //   481: aload_3
    //   482: astore_2
    //   483: lload #10
    //   485: lstore #13
    //   487: new java/lang/StringBuilder
    //   490: astore #9
    //   492: aload_3
    //   493: astore_2
    //   494: lload #10
    //   496: lstore #13
    //   498: aload #9
    //   500: invokespecial <init> : ()V
    //   503: aload_3
    //   504: astore_2
    //   505: lload #10
    //   507: lstore #13
    //   509: aload #9
    //   511: ldc_w 'Trying '
    //   514: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   517: pop
    //   518: aload_3
    //   519: astore_2
    //   520: lload #10
    //   522: lstore #13
    //   524: aload #9
    //   526: aload_3
    //   527: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   530: pop
    //   531: aload_3
    //   532: astore_2
    //   533: lload #10
    //   535: lstore #13
    //   537: aload #12
    //   539: aload #8
    //   541: aload #9
    //   543: invokevirtual toString : ()Ljava/lang/String;
    //   546: invokevirtual log : (Ljava/util/logging/Level;Ljava/lang/String;)V
    //   549: aload_3
    //   550: astore_2
    //   551: lload #10
    //   553: lstore #13
    //   555: aload_3
    //   556: iload #7
    //   558: invokestatic open : (Ljava/lang/String;I)J
    //   561: lstore #10
    //   563: lload #10
    //   565: lconst_0
    //   566: lcmp
    //   567: ifeq -> 577
    //   570: lload #10
    //   572: lstore #13
    //   574: goto -> 1765
    //   577: aload_3
    //   578: astore_2
    //   579: lload #10
    //   581: lstore #13
    //   583: new java/lang/UnsatisfiedLinkError
    //   586: astore #9
    //   588: aload_3
    //   589: astore_2
    //   590: lload #10
    //   592: lstore #13
    //   594: new java/lang/StringBuilder
    //   597: astore #8
    //   599: aload_3
    //   600: astore_2
    //   601: lload #10
    //   603: lstore #13
    //   605: aload #8
    //   607: invokespecial <init> : ()V
    //   610: aload_3
    //   611: astore_2
    //   612: lload #10
    //   614: lstore #13
    //   616: aload #8
    //   618: ldc_w 'Failed to load library ''
    //   621: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   624: pop
    //   625: aload_3
    //   626: astore_2
    //   627: lload #10
    //   629: lstore #13
    //   631: aload #8
    //   633: aload_0
    //   634: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   637: pop
    //   638: aload_3
    //   639: astore_2
    //   640: lload #10
    //   642: lstore #13
    //   644: aload #8
    //   646: ldc_w '''
    //   649: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   652: pop
    //   653: aload_3
    //   654: astore_2
    //   655: lload #10
    //   657: lstore #13
    //   659: aload #9
    //   661: aload #8
    //   663: invokevirtual toString : ()Ljava/lang/String;
    //   666: invokespecial <init> : (Ljava/lang/String;)V
    //   669: aload_3
    //   670: astore_2
    //   671: lload #10
    //   673: lstore #13
    //   675: aload #9
    //   677: athrow
    //   678: astore #8
    //   680: getstatic com/sun/jna/NativeLibrary.LOG : Ljava/util/logging/Logger;
    //   683: astore #9
    //   685: getstatic com/sun/jna/NativeLibrary.DEBUG_LOAD_LEVEL : Ljava/util/logging/Level;
    //   688: astore_3
    //   689: new java/lang/StringBuilder
    //   692: dup
    //   693: invokespecial <init> : ()V
    //   696: astore #12
    //   698: aload #12
    //   700: ldc_w 'Loading failed with message: '
    //   703: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   706: pop
    //   707: aload #12
    //   709: aload #8
    //   711: invokevirtual getMessage : ()Ljava/lang/String;
    //   714: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   717: pop
    //   718: aload #9
    //   720: aload_3
    //   721: aload #12
    //   723: invokevirtual toString : ()Ljava/lang/String;
    //   726: invokevirtual log : (Ljava/util/logging/Level;Ljava/lang/String;)V
    //   729: aload #5
    //   731: aload #8
    //   733: invokeinterface add : (Ljava/lang/Object;)Z
    //   738: pop
    //   739: invokestatic isAndroid : ()Z
    //   742: ifeq -> 878
    //   745: getstatic com/sun/jna/NativeLibrary.LOG : Ljava/util/logging/Logger;
    //   748: astore_3
    //   749: getstatic com/sun/jna/NativeLibrary.DEBUG_LOAD_LEVEL : Ljava/util/logging/Level;
    //   752: astore #9
    //   754: new java/lang/StringBuilder
    //   757: astore #4
    //   759: aload #4
    //   761: invokespecial <init> : ()V
    //   764: aload #4
    //   766: ldc_w 'Preload (via System.loadLibrary) '
    //   769: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   772: pop
    //   773: aload #4
    //   775: aload_0
    //   776: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   779: pop
    //   780: aload_3
    //   781: aload #9
    //   783: aload #4
    //   785: invokevirtual toString : ()Ljava/lang/String;
    //   788: invokevirtual log : (Ljava/util/logging/Level;Ljava/lang/String;)V
    //   791: aload_0
    //   792: invokestatic loadLibrary : (Ljava/lang/String;)V
    //   795: aload_2
    //   796: iload #7
    //   798: invokestatic open : (Ljava/lang/String;I)J
    //   801: lstore #10
    //   803: aload_2
    //   804: astore_3
    //   805: goto -> 1421
    //   808: astore #8
    //   810: getstatic com/sun/jna/NativeLibrary.LOG : Ljava/util/logging/Logger;
    //   813: astore #4
    //   815: getstatic com/sun/jna/NativeLibrary.DEBUG_LOAD_LEVEL : Ljava/util/logging/Level;
    //   818: astore_3
    //   819: new java/lang/StringBuilder
    //   822: dup
    //   823: invokespecial <init> : ()V
    //   826: astore #9
    //   828: aload #9
    //   830: ldc_w 'Loading failed with message: '
    //   833: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   836: pop
    //   837: aload #9
    //   839: aload #8
    //   841: invokevirtual getMessage : ()Ljava/lang/String;
    //   844: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   847: pop
    //   848: aload #4
    //   850: aload_3
    //   851: aload #9
    //   853: invokevirtual toString : ()Ljava/lang/String;
    //   856: invokevirtual log : (Ljava/util/logging/Level;Ljava/lang/String;)V
    //   859: aload #5
    //   861: aload #8
    //   863: invokeinterface add : (Ljava/lang/Object;)Z
    //   868: pop
    //   869: aload_2
    //   870: astore_3
    //   871: lload #13
    //   873: lstore #10
    //   875: goto -> 1421
    //   878: invokestatic isLinux : ()Z
    //   881: ifne -> 1269
    //   884: invokestatic isFreeBSD : ()Z
    //   887: ifeq -> 893
    //   890: goto -> 1269
    //   893: invokestatic isMac : ()Z
    //   896: ifeq -> 1064
    //   899: aload_0
    //   900: ldc_w '.dylib'
    //   903: invokevirtual endsWith : (Ljava/lang/String;)Z
    //   906: ifne -> 1064
    //   909: getstatic com/sun/jna/NativeLibrary.LOG : Ljava/util/logging/Logger;
    //   912: getstatic com/sun/jna/NativeLibrary.DEBUG_LOAD_LEVEL : Ljava/util/logging/Level;
    //   915: ldc_w 'Looking for matching frameworks'
    //   918: invokevirtual log : (Ljava/util/logging/Level;Ljava/lang/String;)V
    //   921: aload_0
    //   922: invokestatic matchFramework : (Ljava/lang/String;)Ljava/lang/String;
    //   925: astore_2
    //   926: aload_2
    //   927: astore_3
    //   928: lload #13
    //   930: lstore #10
    //   932: aload_2
    //   933: ifnull -> 1421
    //   936: getstatic com/sun/jna/NativeLibrary.LOG : Ljava/util/logging/Logger;
    //   939: astore #4
    //   941: getstatic com/sun/jna/NativeLibrary.DEBUG_LOAD_LEVEL : Ljava/util/logging/Level;
    //   944: astore_3
    //   945: new java/lang/StringBuilder
    //   948: astore #9
    //   950: aload #9
    //   952: invokespecial <init> : ()V
    //   955: aload #9
    //   957: ldc_w 'Trying '
    //   960: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   963: pop
    //   964: aload #9
    //   966: aload_2
    //   967: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   970: pop
    //   971: aload #4
    //   973: aload_3
    //   974: aload #9
    //   976: invokevirtual toString : ()Ljava/lang/String;
    //   979: invokevirtual log : (Ljava/util/logging/Level;Ljava/lang/String;)V
    //   982: aload_2
    //   983: iload #7
    //   985: invokestatic open : (Ljava/lang/String;I)J
    //   988: lstore #10
    //   990: aload_2
    //   991: astore_3
    //   992: goto -> 1421
    //   995: astore_3
    //   996: getstatic com/sun/jna/NativeLibrary.LOG : Ljava/util/logging/Logger;
    //   999: astore #8
    //   1001: getstatic com/sun/jna/NativeLibrary.DEBUG_LOAD_LEVEL : Ljava/util/logging/Level;
    //   1004: astore #9
    //   1006: new java/lang/StringBuilder
    //   1009: dup
    //   1010: invokespecial <init> : ()V
    //   1013: astore #4
    //   1015: aload #4
    //   1017: ldc_w 'Loading failed with message: '
    //   1020: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1023: pop
    //   1024: aload #4
    //   1026: aload_3
    //   1027: invokevirtual getMessage : ()Ljava/lang/String;
    //   1030: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1033: pop
    //   1034: aload #8
    //   1036: aload #9
    //   1038: aload #4
    //   1040: invokevirtual toString : ()Ljava/lang/String;
    //   1043: invokevirtual log : (Ljava/util/logging/Level;Ljava/lang/String;)V
    //   1046: aload #5
    //   1048: aload_3
    //   1049: invokeinterface add : (Ljava/lang/Object;)Z
    //   1054: pop
    //   1055: aload_2
    //   1056: astore_3
    //   1057: lload #13
    //   1059: lstore #10
    //   1061: goto -> 1421
    //   1064: aload_2
    //   1065: astore_3
    //   1066: lload #13
    //   1068: lstore #10
    //   1070: invokestatic isWindows : ()Z
    //   1073: ifeq -> 1421
    //   1076: aload_2
    //   1077: astore_3
    //   1078: lload #13
    //   1080: lstore #10
    //   1082: iload #6
    //   1084: ifne -> 1421
    //   1087: getstatic com/sun/jna/NativeLibrary.LOG : Ljava/util/logging/Logger;
    //   1090: getstatic com/sun/jna/NativeLibrary.DEBUG_LOAD_LEVEL : Ljava/util/logging/Level;
    //   1093: ldc_w 'Looking for lib- prefix'
    //   1096: invokevirtual log : (Ljava/util/logging/Level;Ljava/lang/String;)V
    //   1099: new java/lang/StringBuilder
    //   1102: dup
    //   1103: invokespecial <init> : ()V
    //   1106: astore_3
    //   1107: aload_3
    //   1108: ldc_w 'lib'
    //   1111: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1114: pop
    //   1115: aload_3
    //   1116: aload_0
    //   1117: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1120: pop
    //   1121: aload_3
    //   1122: invokevirtual toString : ()Ljava/lang/String;
    //   1125: aload #4
    //   1127: invokestatic findLibraryPath : (Ljava/lang/String;Ljava/util/List;)Ljava/lang/String;
    //   1130: astore_2
    //   1131: aload_2
    //   1132: astore_3
    //   1133: lload #13
    //   1135: lstore #10
    //   1137: aload_2
    //   1138: ifnull -> 1421
    //   1141: getstatic com/sun/jna/NativeLibrary.LOG : Ljava/util/logging/Logger;
    //   1144: astore #4
    //   1146: getstatic com/sun/jna/NativeLibrary.DEBUG_LOAD_LEVEL : Ljava/util/logging/Level;
    //   1149: astore_3
    //   1150: new java/lang/StringBuilder
    //   1153: dup
    //   1154: invokespecial <init> : ()V
    //   1157: astore #9
    //   1159: aload #9
    //   1161: ldc_w 'Trying '
    //   1164: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1167: pop
    //   1168: aload #9
    //   1170: aload_2
    //   1171: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1174: pop
    //   1175: aload #4
    //   1177: aload_3
    //   1178: aload #9
    //   1180: invokevirtual toString : ()Ljava/lang/String;
    //   1183: invokevirtual log : (Ljava/util/logging/Level;Ljava/lang/String;)V
    //   1186: aload_2
    //   1187: iload #7
    //   1189: invokestatic open : (Ljava/lang/String;I)J
    //   1192: lstore #10
    //   1194: aload_2
    //   1195: astore_3
    //   1196: goto -> 1421
    //   1199: astore #4
    //   1201: getstatic com/sun/jna/NativeLibrary.LOG : Ljava/util/logging/Logger;
    //   1204: astore #9
    //   1206: getstatic com/sun/jna/NativeLibrary.DEBUG_LOAD_LEVEL : Ljava/util/logging/Level;
    //   1209: astore_3
    //   1210: new java/lang/StringBuilder
    //   1213: dup
    //   1214: invokespecial <init> : ()V
    //   1217: astore #8
    //   1219: aload #8
    //   1221: ldc_w 'Loading failed with message: '
    //   1224: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1227: pop
    //   1228: aload #8
    //   1230: aload #4
    //   1232: invokevirtual getMessage : ()Ljava/lang/String;
    //   1235: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1238: pop
    //   1239: aload #9
    //   1241: aload_3
    //   1242: aload #8
    //   1244: invokevirtual toString : ()Ljava/lang/String;
    //   1247: invokevirtual log : (Ljava/util/logging/Level;Ljava/lang/String;)V
    //   1250: aload #5
    //   1252: aload #4
    //   1254: invokeinterface add : (Ljava/lang/Object;)Z
    //   1259: pop
    //   1260: aload_2
    //   1261: astore_3
    //   1262: lload #13
    //   1264: lstore #10
    //   1266: goto -> 1421
    //   1269: getstatic com/sun/jna/NativeLibrary.LOG : Ljava/util/logging/Logger;
    //   1272: getstatic com/sun/jna/NativeLibrary.DEBUG_LOAD_LEVEL : Ljava/util/logging/Level;
    //   1275: ldc_w 'Looking for version variants'
    //   1278: invokevirtual log : (Ljava/util/logging/Level;Ljava/lang/String;)V
    //   1281: aload_0
    //   1282: aload #4
    //   1284: invokestatic matchLibrary : (Ljava/lang/String;Ljava/util/List;)Ljava/lang/String;
    //   1287: astore_2
    //   1288: aload_2
    //   1289: astore_3
    //   1290: lload #13
    //   1292: lstore #10
    //   1294: aload_2
    //   1295: ifnull -> 1421
    //   1298: getstatic com/sun/jna/NativeLibrary.LOG : Ljava/util/logging/Logger;
    //   1301: astore #9
    //   1303: getstatic com/sun/jna/NativeLibrary.DEBUG_LOAD_LEVEL : Ljava/util/logging/Level;
    //   1306: astore #4
    //   1308: new java/lang/StringBuilder
    //   1311: dup
    //   1312: invokespecial <init> : ()V
    //   1315: astore_3
    //   1316: aload_3
    //   1317: ldc_w 'Trying '
    //   1320: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1323: pop
    //   1324: aload_3
    //   1325: aload_2
    //   1326: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1329: pop
    //   1330: aload #9
    //   1332: aload #4
    //   1334: aload_3
    //   1335: invokevirtual toString : ()Ljava/lang/String;
    //   1338: invokevirtual log : (Ljava/util/logging/Level;Ljava/lang/String;)V
    //   1341: aload_2
    //   1342: iload #7
    //   1344: invokestatic open : (Ljava/lang/String;I)J
    //   1347: lstore #10
    //   1349: aload_2
    //   1350: astore_3
    //   1351: goto -> 1421
    //   1354: astore #9
    //   1356: getstatic com/sun/jna/NativeLibrary.LOG : Ljava/util/logging/Logger;
    //   1359: astore #4
    //   1361: getstatic com/sun/jna/NativeLibrary.DEBUG_LOAD_LEVEL : Ljava/util/logging/Level;
    //   1364: astore_3
    //   1365: new java/lang/StringBuilder
    //   1368: dup
    //   1369: invokespecial <init> : ()V
    //   1372: astore #8
    //   1374: aload #8
    //   1376: ldc_w 'Loading failed with message: '
    //   1379: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1382: pop
    //   1383: aload #8
    //   1385: aload #9
    //   1387: invokevirtual getMessage : ()Ljava/lang/String;
    //   1390: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1393: pop
    //   1394: aload #4
    //   1396: aload_3
    //   1397: aload #8
    //   1399: invokevirtual toString : ()Ljava/lang/String;
    //   1402: invokevirtual log : (Ljava/util/logging/Level;Ljava/lang/String;)V
    //   1405: aload #5
    //   1407: aload #9
    //   1409: invokeinterface add : (Ljava/lang/Object;)Z
    //   1414: pop
    //   1415: lload #13
    //   1417: lstore #10
    //   1419: aload_2
    //   1420: astore_3
    //   1421: lload #10
    //   1423: lconst_0
    //   1424: lcmp
    //   1425: ifne -> 1630
    //   1428: aload_3
    //   1429: astore_2
    //   1430: lload #10
    //   1432: lstore #13
    //   1434: aload_0
    //   1435: aload_1
    //   1436: ldc_w 'classloader'
    //   1439: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   1444: checkcast java/lang/ClassLoader
    //   1447: invokestatic extractFromResourcePath : (Ljava/lang/String;Ljava/lang/ClassLoader;)Ljava/io/File;
    //   1450: astore #9
    //   1452: aload #9
    //   1454: invokevirtual getAbsolutePath : ()Ljava/lang/String;
    //   1457: iload #7
    //   1459: invokestatic open : (Ljava/lang/String;I)J
    //   1462: lstore #15
    //   1464: lload #15
    //   1466: lstore #10
    //   1468: aload #9
    //   1470: invokevirtual getAbsolutePath : ()Ljava/lang/String;
    //   1473: astore #4
    //   1475: aload #4
    //   1477: astore_2
    //   1478: lload #15
    //   1480: lstore #13
    //   1482: aload #4
    //   1484: astore_3
    //   1485: lload #15
    //   1487: lstore #10
    //   1489: aload #9
    //   1491: invokestatic isUnpacked : (Ljava/io/File;)Z
    //   1494: ifeq -> 1630
    //   1497: aload #4
    //   1499: astore_2
    //   1500: lload #15
    //   1502: lstore #13
    //   1504: aload #9
    //   1506: invokestatic deleteLibrary : (Ljava/io/File;)Z
    //   1509: pop
    //   1510: aload #4
    //   1512: astore_3
    //   1513: lload #15
    //   1515: lstore #10
    //   1517: goto -> 1630
    //   1520: astore #4
    //   1522: aload_3
    //   1523: astore_2
    //   1524: lload #10
    //   1526: lstore #13
    //   1528: aload #9
    //   1530: invokestatic isUnpacked : (Ljava/io/File;)Z
    //   1533: ifeq -> 1548
    //   1536: aload_3
    //   1537: astore_2
    //   1538: lload #10
    //   1540: lstore #13
    //   1542: aload #9
    //   1544: invokestatic deleteLibrary : (Ljava/io/File;)Z
    //   1547: pop
    //   1548: aload_3
    //   1549: astore_2
    //   1550: lload #10
    //   1552: lstore #13
    //   1554: aload #4
    //   1556: athrow
    //   1557: astore #4
    //   1559: aload_2
    //   1560: astore_3
    //   1561: lload #13
    //   1563: lstore #10
    //   1565: goto -> 1568
    //   1568: getstatic com/sun/jna/NativeLibrary.LOG : Ljava/util/logging/Logger;
    //   1571: astore_2
    //   1572: getstatic com/sun/jna/NativeLibrary.DEBUG_LOAD_LEVEL : Ljava/util/logging/Level;
    //   1575: astore #8
    //   1577: new java/lang/StringBuilder
    //   1580: dup
    //   1581: invokespecial <init> : ()V
    //   1584: astore #9
    //   1586: aload #9
    //   1588: ldc_w 'Loading failed with message: '
    //   1591: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1594: pop
    //   1595: aload #9
    //   1597: aload #4
    //   1599: invokevirtual getMessage : ()Ljava/lang/String;
    //   1602: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1605: pop
    //   1606: aload_2
    //   1607: aload #8
    //   1609: aload #9
    //   1611: invokevirtual toString : ()Ljava/lang/String;
    //   1614: invokevirtual log : (Ljava/util/logging/Level;Ljava/lang/String;)V
    //   1617: aload #5
    //   1619: aload #4
    //   1621: invokeinterface add : (Ljava/lang/Object;)Z
    //   1626: pop
    //   1627: goto -> 1630
    //   1630: lload #10
    //   1632: lstore #13
    //   1634: lload #10
    //   1636: lconst_0
    //   1637: lcmp
    //   1638: ifne -> 1765
    //   1641: new java/lang/StringBuilder
    //   1644: dup
    //   1645: invokespecial <init> : ()V
    //   1648: astore_1
    //   1649: aload_1
    //   1650: ldc_w 'Unable to load library ''
    //   1653: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1656: pop
    //   1657: aload_1
    //   1658: aload_0
    //   1659: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1662: pop
    //   1663: aload_1
    //   1664: ldc_w '':'
    //   1667: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1670: pop
    //   1671: aload #5
    //   1673: invokeinterface iterator : ()Ljava/util/Iterator;
    //   1678: astore_3
    //   1679: aload_3
    //   1680: invokeinterface hasNext : ()Z
    //   1685: ifeq -> 1718
    //   1688: aload_3
    //   1689: invokeinterface next : ()Ljava/lang/Object;
    //   1694: checkcast java/lang/Throwable
    //   1697: astore_0
    //   1698: aload_1
    //   1699: ldc_w '\\n'
    //   1702: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1705: pop
    //   1706: aload_1
    //   1707: aload_0
    //   1708: invokevirtual getMessage : ()Ljava/lang/String;
    //   1711: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1714: pop
    //   1715: goto -> 1679
    //   1718: new java/lang/UnsatisfiedLinkError
    //   1721: dup
    //   1722: aload_1
    //   1723: invokevirtual toString : ()Ljava/lang/String;
    //   1726: invokespecial <init> : (Ljava/lang/String;)V
    //   1729: astore_0
    //   1730: aload #5
    //   1732: invokeinterface iterator : ()Ljava/util/Iterator;
    //   1737: astore_1
    //   1738: aload_1
    //   1739: invokeinterface hasNext : ()Z
    //   1744: ifeq -> 1763
    //   1747: aload_0
    //   1748: aload_1
    //   1749: invokeinterface next : ()Ljava/lang/Object;
    //   1754: checkcast java/lang/Throwable
    //   1757: invokestatic addSuppressedReflected : (Ljava/lang/Throwable;Ljava/lang/Throwable;)V
    //   1760: goto -> 1738
    //   1763: aload_0
    //   1764: athrow
    //   1765: getstatic com/sun/jna/NativeLibrary.LOG : Ljava/util/logging/Logger;
    //   1768: astore #5
    //   1770: getstatic com/sun/jna/NativeLibrary.DEBUG_LOAD_LEVEL : Ljava/util/logging/Level;
    //   1773: astore #4
    //   1775: new java/lang/StringBuilder
    //   1778: dup
    //   1779: invokespecial <init> : ()V
    //   1782: astore_2
    //   1783: aload_2
    //   1784: ldc_w 'Found library ''
    //   1787: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1790: pop
    //   1791: aload_2
    //   1792: aload_0
    //   1793: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1796: pop
    //   1797: aload_2
    //   1798: ldc_w '' at '
    //   1801: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1804: pop
    //   1805: aload_2
    //   1806: aload_3
    //   1807: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1810: pop
    //   1811: aload #5
    //   1813: aload #4
    //   1815: aload_2
    //   1816: invokevirtual toString : ()Ljava/lang/String;
    //   1819: invokevirtual log : (Ljava/util/logging/Level;Ljava/lang/String;)V
    //   1822: new com/sun/jna/NativeLibrary
    //   1825: dup
    //   1826: aload_0
    //   1827: aload_3
    //   1828: lload #13
    //   1830: aload_1
    //   1831: invokespecial <init> : (Ljava/lang/String;Ljava/lang/String;JLjava/util/Map;)V
    //   1834: areturn
    // Exception table:
    //   from	to	target	type
    //   171	183	186	finally
    //   187	189	186	finally
    //   258	309	312	java/lang/UnsatisfiedLinkError
    //   452	459	678	java/lang/UnsatisfiedLinkError
    //   465	470	678	java/lang/UnsatisfiedLinkError
    //   476	481	678	java/lang/UnsatisfiedLinkError
    //   487	492	678	java/lang/UnsatisfiedLinkError
    //   498	503	678	java/lang/UnsatisfiedLinkError
    //   509	518	678	java/lang/UnsatisfiedLinkError
    //   524	531	678	java/lang/UnsatisfiedLinkError
    //   537	549	678	java/lang/UnsatisfiedLinkError
    //   555	563	678	java/lang/UnsatisfiedLinkError
    //   583	588	678	java/lang/UnsatisfiedLinkError
    //   594	599	678	java/lang/UnsatisfiedLinkError
    //   605	610	678	java/lang/UnsatisfiedLinkError
    //   616	625	678	java/lang/UnsatisfiedLinkError
    //   631	638	678	java/lang/UnsatisfiedLinkError
    //   644	653	678	java/lang/UnsatisfiedLinkError
    //   659	669	678	java/lang/UnsatisfiedLinkError
    //   675	678	678	java/lang/UnsatisfiedLinkError
    //   745	803	808	java/lang/UnsatisfiedLinkError
    //   936	990	995	java/lang/UnsatisfiedLinkError
    //   1186	1194	1199	java/lang/UnsatisfiedLinkError
    //   1341	1349	1354	java/lang/UnsatisfiedLinkError
    //   1434	1452	1557	java/io/IOException
    //   1452	1464	1520	finally
    //   1468	1475	1520	finally
    //   1489	1497	1557	java/io/IOException
    //   1504	1510	1557	java/io/IOException
    //   1528	1536	1557	java/io/IOException
    //   1542	1548	1557	java/io/IOException
    //   1554	1557	1557	java/io/IOException
  }
  
  static String mapSharedLibraryName(String paramString) {
    String str;
    if (Platform.isMac()) {
      if (paramString.startsWith("lib") && (paramString.endsWith(".dylib") || paramString.endsWith(".jnilib")))
        return paramString; 
      String str1 = System.mapLibraryName(paramString);
      paramString = str1;
      if (str1.endsWith(".jnilib")) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(str1.substring(0, str1.lastIndexOf(".jnilib")));
        stringBuilder.append(".dylib");
        str = stringBuilder.toString();
      } 
      return str;
    } 
    if (Platform.isLinux() || Platform.isFreeBSD()) {
      String str1 = str;
      if (!isVersionedName(str)) {
        if (str.endsWith(".so"))
          return str; 
      } else {
        return str1;
      } 
    } else if (Platform.isAIX()) {
      if (str.startsWith("lib"))
        return str; 
    } else if (Platform.isWindows() && (str.endsWith(".drv") || str.endsWith(".dll") || str.endsWith(".ocx"))) {
      return str;
    } 
    return System.mapLibraryName(str);
  }
  
  static String matchFramework(String paramString) {
    File file1;
    File file2 = new File(paramString);
    if (file2.isAbsolute()) {
      if (paramString.indexOf(".framework") != -1 && file2.exists())
        return file2.getAbsolutePath(); 
      file1 = file2.getParentFile();
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(file2.getName());
      stringBuilder.append(".framework");
      file1 = new File(new File(file1, stringBuilder.toString()), file2.getName());
      if (file1.exists())
        return file1.getAbsolutePath(); 
    } else {
      String str1;
      String[] arrayOfString = new String[3];
      String str2 = System.getProperty("user.home");
      byte b1 = 0;
      arrayOfString[0] = str2;
      arrayOfString[1] = "";
      arrayOfString[2] = "/System";
      byte b2 = b1;
      File file = file1;
      if (file1.indexOf(".framework") == -1) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append((String)file1);
        stringBuilder.append(".framework/");
        stringBuilder.append((String)file1);
        str1 = stringBuilder.toString();
        b2 = b1;
      } 
      while (b2 < arrayOfString.length) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(arrayOfString[b2]);
        stringBuilder.append("/Library/Frameworks/");
        stringBuilder.append(str1);
        String str = stringBuilder.toString();
        if ((new File(str)).exists())
          return str; 
        b2++;
      } 
    } 
    return null;
  }
  
  static String matchLibrary(final String libName, List<String> paramList) {
    String str;
    File file = new File(libName);
    if (file.isAbsolute())
      paramList = Arrays.asList(new String[] { file.getParent() }); 
    FilenameFilter filenameFilter = new FilenameFilter() {
        public boolean accept(File param1File, String param1String) {
          // Byte code:
          //   0: new java/lang/StringBuilder
          //   3: dup
          //   4: invokespecial <init> : ()V
          //   7: astore_1
          //   8: aload_1
          //   9: ldc 'lib'
          //   11: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
          //   14: pop
          //   15: aload_1
          //   16: aload_0
          //   17: getfield val$libName : Ljava/lang/String;
          //   20: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
          //   23: pop
          //   24: aload_1
          //   25: ldc '.so'
          //   27: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
          //   30: pop
          //   31: aload_2
          //   32: aload_1
          //   33: invokevirtual toString : ()Ljava/lang/String;
          //   36: invokevirtual startsWith : (Ljava/lang/String;)Z
          //   39: ifne -> 89
          //   42: new java/lang/StringBuilder
          //   45: dup
          //   46: invokespecial <init> : ()V
          //   49: astore_1
          //   50: aload_1
          //   51: aload_0
          //   52: getfield val$libName : Ljava/lang/String;
          //   55: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
          //   58: pop
          //   59: aload_1
          //   60: ldc '.so'
          //   62: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
          //   65: pop
          //   66: aload_2
          //   67: aload_1
          //   68: invokevirtual toString : ()Ljava/lang/String;
          //   71: invokevirtual startsWith : (Ljava/lang/String;)Z
          //   74: ifeq -> 101
          //   77: aload_0
          //   78: getfield val$libName : Ljava/lang/String;
          //   81: ldc 'lib'
          //   83: invokevirtual startsWith : (Ljava/lang/String;)Z
          //   86: ifeq -> 101
          //   89: aload_2
          //   90: invokestatic access$000 : (Ljava/lang/String;)Z
          //   93: ifeq -> 101
          //   96: iconst_1
          //   97: istore_3
          //   98: goto -> 103
          //   101: iconst_0
          //   102: istore_3
          //   103: iload_3
          //   104: ireturn
        }
      };
    LinkedList linkedList = new LinkedList();
    Iterator<String> iterator = paramList.iterator();
    while (iterator.hasNext()) {
      File[] arrayOfFile = (new File(iterator.next())).listFiles(filenameFilter);
      if (arrayOfFile != null && arrayOfFile.length > 0)
        linkedList.addAll(Arrays.asList(arrayOfFile)); 
    } 
    double d = -1.0D;
    filenameFilter = null;
    Iterator<File> iterator1 = linkedList.iterator();
    while (iterator1.hasNext()) {
      String str1 = ((File)iterator1.next()).getAbsolutePath();
      double d1 = parseVersion(str1.substring(str1.lastIndexOf(".so.") + 4));
      if (d1 > d) {
        str = str1;
        d = d1;
      } 
    } 
    return str;
  }
  
  private static int openFlags(Map<String, ?> paramMap) {
    paramMap = (Map<String, ?>)paramMap.get("open-flags");
    return (paramMap instanceof Number) ? ((Number)paramMap).intValue() : -1;
  }
  
  static double parseVersion(String paramString) {
    int i = paramString.indexOf(".");
    double d1 = 1.0D;
    double d2 = 0.0D;
    while (paramString != null) {
      String str;
      if (i != -1) {
        str = paramString.substring(0, i);
        paramString = paramString.substring(i + 1);
        i = paramString.indexOf(".");
      } else {
        String str1 = null;
        str = paramString;
        paramString = str1;
      } 
      try {
        int j = Integer.parseInt(str);
        double d = j;
        Double.isNaN(d);
        d2 += d / d1;
        d1 *= 100.0D;
      } catch (NumberFormatException numberFormatException) {
        return 0.0D;
      } 
    } 
    return d2;
  }
  
  public void dispose() {
    // Byte code:
    //   0: new java/util/HashSet
    //   3: dup
    //   4: invokespecial <init> : ()V
    //   7: astore_1
    //   8: getstatic com/sun/jna/NativeLibrary.libraries : Ljava/util/Map;
    //   11: astore_2
    //   12: aload_2
    //   13: monitorenter
    //   14: getstatic com/sun/jna/NativeLibrary.libraries : Ljava/util/Map;
    //   17: invokeinterface entrySet : ()Ljava/util/Set;
    //   22: invokeinterface iterator : ()Ljava/util/Iterator;
    //   27: astore_3
    //   28: aload_3
    //   29: invokeinterface hasNext : ()Z
    //   34: ifeq -> 82
    //   37: aload_3
    //   38: invokeinterface next : ()Ljava/lang/Object;
    //   43: checkcast java/util/Map$Entry
    //   46: astore #4
    //   48: aload #4
    //   50: invokeinterface getValue : ()Ljava/lang/Object;
    //   55: checkcast java/lang/ref/Reference
    //   58: invokevirtual get : ()Ljava/lang/Object;
    //   61: aload_0
    //   62: if_acmpne -> 28
    //   65: aload_1
    //   66: aload #4
    //   68: invokeinterface getKey : ()Ljava/lang/Object;
    //   73: invokeinterface add : (Ljava/lang/Object;)Z
    //   78: pop
    //   79: goto -> 28
    //   82: aload_1
    //   83: invokeinterface iterator : ()Ljava/util/Iterator;
    //   88: astore_3
    //   89: aload_3
    //   90: invokeinterface hasNext : ()Z
    //   95: ifeq -> 121
    //   98: aload_3
    //   99: invokeinterface next : ()Ljava/lang/Object;
    //   104: checkcast java/lang/String
    //   107: astore_1
    //   108: getstatic com/sun/jna/NativeLibrary.libraries : Ljava/util/Map;
    //   111: aload_1
    //   112: invokeinterface remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   117: pop
    //   118: goto -> 89
    //   121: aload_2
    //   122: monitorexit
    //   123: aload_0
    //   124: monitorenter
    //   125: aload_0
    //   126: getfield handle : J
    //   129: lconst_0
    //   130: lcmp
    //   131: ifeq -> 146
    //   134: aload_0
    //   135: getfield handle : J
    //   138: invokestatic close : (J)V
    //   141: aload_0
    //   142: lconst_0
    //   143: putfield handle : J
    //   146: aload_0
    //   147: monitorexit
    //   148: return
    //   149: astore_2
    //   150: aload_0
    //   151: monitorexit
    //   152: aload_2
    //   153: athrow
    //   154: astore_3
    //   155: aload_2
    //   156: monitorexit
    //   157: goto -> 162
    //   160: aload_3
    //   161: athrow
    //   162: goto -> 160
    // Exception table:
    //   from	to	target	type
    //   14	28	154	finally
    //   28	79	154	finally
    //   82	89	154	finally
    //   89	118	154	finally
    //   121	123	154	finally
    //   125	146	149	finally
    //   146	148	149	finally
    //   150	152	149	finally
    //   155	157	154	finally
  }
  
  protected void finalize() {
    dispose();
  }
  
  public File getFile() {
    String str = this.libraryPath;
    return (str == null) ? null : new File(str);
  }
  
  public Function getFunction(String paramString) {
    return getFunction(paramString, this.callFlags);
  }
  
  public Function getFunction(String paramString, int paramInt) {
    return getFunction(paramString, paramInt, this.encoding);
  }
  
  public Function getFunction(String paramString1, int paramInt, String paramString2) {
    if (paramString1 != null)
      synchronized (this.functions) {
        String str = functionKey(paramString1, paramInt, paramString2);
        Function function1 = this.functions.get(str);
        Function function2 = function1;
        if (function1 == null) {
          function2 = new Function();
          this(this, paramString1, paramInt, paramString2);
          this.functions.put(str, function2);
        } 
        return function2;
      }  
    throw new NullPointerException("Function name may not be null");
  }
  
  Function getFunction(String paramString, Method paramMethod) {
    FunctionMapper functionMapper = (FunctionMapper)this.options.get("function-mapper");
    if (functionMapper != null)
      paramString = functionMapper.getFunctionName(this, paramMethod); 
    String str2 = System.getProperty("jna.profiler.prefix", "$$YJP$$");
    String str1 = paramString;
    if (paramString.startsWith(str2))
      str1 = paramString.substring(str2.length()); 
    int i = this.callFlags;
    Class[] arrayOfClass = paramMethod.getExceptionTypes();
    byte b = 0;
    while (b < arrayOfClass.length) {
      int j = i;
      if (LastErrorException.class.isAssignableFrom(arrayOfClass[b]))
        j = i | 0x40; 
      b++;
      i = j;
    } 
    return getFunction(str1, i);
  }
  
  public Pointer getGlobalVariableAddress(String paramString) {
    try {
      return new Pointer(getSymbolAddress(paramString));
    } catch (UnsatisfiedLinkError unsatisfiedLinkError) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Error looking up '");
      stringBuilder.append(paramString);
      stringBuilder.append("': ");
      stringBuilder.append(unsatisfiedLinkError.getMessage());
      throw new UnsatisfiedLinkError(stringBuilder.toString());
    } 
  }
  
  public String getName() {
    return this.libraryName;
  }
  
  public Map<String, ?> getOptions() {
    return this.options;
  }
  
  long getSymbolAddress(String paramString) {
    long l = this.handle;
    if (l != 0L)
      return Native.findSymbol(l, paramString); 
    throw new UnsatisfiedLinkError("Library has been unloaded");
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Native Library <");
    stringBuilder.append(this.libraryPath);
    stringBuilder.append("@");
    stringBuilder.append(this.handle);
    stringBuilder.append(">");
    return stringBuilder.toString();
  }
  
  static {
    Level level;
  }
  
  private static final Level DEBUG_LOAD_LEVEL;
  
  private static final int DEFAULT_OPEN_OPTIONS = -1;
  
  private static final Logger LOG = Logger.getLogger(NativeLibrary.class.getName());
  
  private static Method addSuppressedMethod;
  
  private static final Map<String, Reference<NativeLibrary>> libraries;
  
  private static final List<String> librarySearchPath;
  
  private static final Map<String, List<String>> searchPaths;
  
  final int callFlags;
  
  private String encoding;
  
  private final Map<String, Function> functions;
  
  private long handle;
  
  private final String libraryName;
  
  private final String libraryPath;
  
  final Map<String, ?> options;
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/sun/jna/NativeLibrary.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */