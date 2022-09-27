package com.sun.jna;

import java.awt.Component;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Window;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.WeakHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Native implements Version {
  static {
    DEFAULT_CHARSET = Charset.defaultCharset();
    DEFAULT_ENCODING = DEFAULT_CHARSET.name();
    DEBUG_LOAD = Boolean.getBoolean("jna.debug_load");
    DEBUG_JNA_LOAD = Boolean.getBoolean("jna.debug_load.jna");
    if (DEBUG_JNA_LOAD) {
      level = Level.INFO;
    } else {
      level = Level.FINE;
    } 
    DEBUG_JNA_LOAD_LEVEL = level;
    jnidispatchPath = null;
    typeOptions = Collections.synchronizedMap(new WeakHashMap<Class<?>, Map<String, Object>>());
    libraries = Collections.synchronizedMap(new WeakHashMap<Class<?>, Reference<?>>());
    DEFAULT_HANDLER = new Callback.UncaughtExceptionHandler() {
        public void uncaughtException(Callback param1Callback, Throwable param1Throwable) {
          Logger logger = Native.LOG;
          Level level = Level.WARNING;
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("JNA: Callback ");
          stringBuilder.append(param1Callback);
          stringBuilder.append(" threw the following exception");
          logger.log(level, stringBuilder.toString(), param1Throwable);
        }
      };
    callbackExceptionHandler = DEFAULT_HANDLER;
    loadNativeDispatchLibrary();
    if (!isCompatibleVersion("6.1.0", getNativeVersion())) {
      String str1;
      String str2 = System.getProperty("line.separator");
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(str2);
      stringBuilder.append(str2);
      stringBuilder.append("There is an incompatible JNA native library installed on this system");
      stringBuilder.append(str2);
      stringBuilder.append("Expected: ");
      stringBuilder.append("6.1.0");
      stringBuilder.append(str2);
      stringBuilder.append("Found:    ");
      stringBuilder.append(getNativeVersion());
      stringBuilder.append(str2);
      if (jnidispatchPath != null) {
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("(at ");
        stringBuilder1.append(jnidispatchPath);
        stringBuilder1.append(")");
        str1 = stringBuilder1.toString();
      } else {
        str1 = System.getProperty("java.library.path");
      } 
      stringBuilder.append(str1);
      stringBuilder.append(".");
      stringBuilder.append(str2);
      stringBuilder.append("To resolve this issue you may do one of the following:");
      stringBuilder.append(str2);
      stringBuilder.append(" - remove or uninstall the offending library");
      stringBuilder.append(str2);
      stringBuilder.append(" - set the system property jna.nosys=true");
      stringBuilder.append(str2);
      stringBuilder.append(" - set jna.boot.library.path to include the path to the version of the ");
      stringBuilder.append(str2);
      stringBuilder.append("   jnidispatch library included with the JNA jar file you are using");
      stringBuilder.append(str2);
      throw new Error(stringBuilder.toString());
    } 
    POINTER_SIZE = sizeof(0);
    LONG_SIZE = sizeof(1);
    WCHAR_SIZE = sizeof(2);
    SIZE_T_SIZE = sizeof(3);
    BOOL_SIZE = sizeof(4);
    LONG_DOUBLE_SIZE = sizeof(5);
    initIDs();
    if (Boolean.getBoolean("jna.protected"))
      setProtected(true); 
    boolean bool = Platform.isSPARC();
    byte b = 8;
    if (bool || Platform.isWindows() || (Platform.isLinux() && (Platform.isARM() || Platform.isPPC() || Platform.isMIPS())) || Platform.isAIX() || (Platform.isAndroid() && !Platform.isIntel())) {
      i = 8;
    } else {
      i = LONG_SIZE;
    } 
    MAX_ALIGNMENT = i;
    if (Platform.isMac() && Platform.isPPC()) {
      i = b;
    } else {
      i = MAX_ALIGNMENT;
    } 
    MAX_PADDING = i;
    System.setProperty("jna.loaded", "true");
    finalizer = new Object() {
        protected void finalize() throws Throwable {
          Native.dispose();
          super.finalize();
        }
      };
    registeredClasses = (Map)new WeakHashMap<Class<?>, long>();
    registeredLibraries = new WeakHashMap<Class<?>, NativeLibrary>();
    nativeThreadTerminationFlag = new ThreadLocal<Memory>() {
        protected Memory initialValue() {
          Memory memory = new Memory(4L);
          memory.clear();
          return memory;
        }
      };
    nativeThreads = Collections.synchronizedMap(new WeakHashMap<Thread, Pointer>());
  }
  
  private static native long _getDirectBufferPointer(Buffer paramBuffer);
  
  private static native long _getPointer(long paramLong);
  
  private static Map<String, Object> cacheOptions(Class<?> paramClass, Map<String, ?> paramMap, Object paramObject) {
    paramMap = new HashMap<String, Object>(paramMap);
    paramMap.put("enclosing-library", paramClass);
    typeOptions.put(paramClass, paramMap);
    if (paramObject != null)
      libraries.put(paramClass, new WeakReference(paramObject)); 
    if (!paramClass.isInterface() && Library.class.isAssignableFrom(paramClass))
      for (Class<?> paramClass : paramClass.getInterfaces()) {
        if (Library.class.isAssignableFrom(paramClass)) {
          cacheOptions(paramClass, paramMap, paramObject);
          break;
        } 
      }  
    return (Map)paramMap;
  }
  
  static native void close(long paramLong);
  
  static synchronized native long createNativeCallback(Callback paramCallback, Method paramMethod, Class<?>[] paramArrayOfClass, Class<?> paramClass, int paramInt1, int paramInt2, String paramString);
  
  static boolean deleteLibrary(File paramFile) {
    if (paramFile.delete())
      return true; 
    markTemporaryFile(paramFile);
    return false;
  }
  
  public static void detach(boolean paramBoolean) {
    Pointer pointer;
    Thread thread = Thread.currentThread();
    if (paramBoolean) {
      nativeThreads.remove(thread);
      pointer = nativeThreadTerminationFlag.get();
      setDetachState(true, 0L);
    } else if (!nativeThreads.containsKey(pointer)) {
      Pointer pointer1 = nativeThreadTerminationFlag.get();
      nativeThreads.put(pointer, pointer1);
      setDetachState(false, pointer1.peer);
    } 
  }
  
  private static void dispose() {
    CallbackReference.disposeAll();
    Memory.disposeAll();
    NativeLibrary.disposeAll();
    unregisterAll();
    jnidispatchPath = null;
    System.setProperty("jna.loaded", "false");
  }
  
  public static File extractFromResourcePath(String paramString) throws IOException {
    return extractFromResourcePath(paramString, null);
  }
  
  public static File extractFromResourcePath(String paramString, ClassLoader paramClassLoader) throws IOException {
    // Byte code:
    //   0: getstatic com/sun/jna/Native.DEBUG_LOAD : Z
    //   3: ifne -> 32
    //   6: getstatic com/sun/jna/Native.DEBUG_JNA_LOAD : Z
    //   9: ifeq -> 25
    //   12: aload_0
    //   13: ldc_w 'jnidispatch'
    //   16: invokevirtual contains : (Ljava/lang/CharSequence;)Z
    //   19: ifeq -> 25
    //   22: goto -> 32
    //   25: getstatic java/util/logging/Level.FINE : Ljava/util/logging/Level;
    //   28: astore_2
    //   29: goto -> 36
    //   32: getstatic java/util/logging/Level.INFO : Ljava/util/logging/Level;
    //   35: astore_2
    //   36: aload_1
    //   37: astore_3
    //   38: aload_1
    //   39: ifnonnull -> 61
    //   42: invokestatic currentThread : ()Ljava/lang/Thread;
    //   45: invokevirtual getContextClassLoader : ()Ljava/lang/ClassLoader;
    //   48: astore_1
    //   49: aload_1
    //   50: astore_3
    //   51: aload_1
    //   52: ifnonnull -> 61
    //   55: ldc com/sun/jna/Native
    //   57: invokevirtual getClassLoader : ()Ljava/lang/ClassLoader;
    //   60: astore_3
    //   61: getstatic com/sun/jna/Native.LOG : Ljava/util/logging/Logger;
    //   64: aload_2
    //   65: ldc_w 'Looking in classpath from {0} for {1}'
    //   68: iconst_2
    //   69: anewarray java/lang/Object
    //   72: dup
    //   73: iconst_0
    //   74: aload_3
    //   75: aastore
    //   76: dup
    //   77: iconst_1
    //   78: aload_0
    //   79: aastore
    //   80: invokevirtual log : (Ljava/util/logging/Level;Ljava/lang/String;[Ljava/lang/Object;)V
    //   83: aload_0
    //   84: ldc_w '/'
    //   87: invokevirtual startsWith : (Ljava/lang/String;)Z
    //   90: ifeq -> 99
    //   93: aload_0
    //   94: astore #4
    //   96: goto -> 105
    //   99: aload_0
    //   100: invokestatic mapSharedLibraryName : (Ljava/lang/String;)Ljava/lang/String;
    //   103: astore #4
    //   105: aload_0
    //   106: ldc_w '/'
    //   109: invokevirtual startsWith : (Ljava/lang/String;)Z
    //   112: ifeq -> 120
    //   115: aload_0
    //   116: astore_1
    //   117: goto -> 156
    //   120: new java/lang/StringBuilder
    //   123: dup
    //   124: invokespecial <init> : ()V
    //   127: astore_1
    //   128: aload_1
    //   129: getstatic com/sun/jna/Platform.RESOURCE_PREFIX : Ljava/lang/String;
    //   132: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   135: pop
    //   136: aload_1
    //   137: ldc_w '/'
    //   140: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   143: pop
    //   144: aload_1
    //   145: aload #4
    //   147: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   150: pop
    //   151: aload_1
    //   152: invokevirtual toString : ()Ljava/lang/String;
    //   155: astore_1
    //   156: aload_1
    //   157: astore #5
    //   159: aload_1
    //   160: ldc_w '/'
    //   163: invokevirtual startsWith : (Ljava/lang/String;)Z
    //   166: ifeq -> 176
    //   169: aload_1
    //   170: iconst_1
    //   171: invokevirtual substring : (I)Ljava/lang/String;
    //   174: astore #5
    //   176: aload_3
    //   177: aload #5
    //   179: invokevirtual getResource : (Ljava/lang/String;)Ljava/net/URL;
    //   182: astore #6
    //   184: aload #6
    //   186: astore_1
    //   187: aload #6
    //   189: ifnonnull -> 213
    //   192: aload #6
    //   194: astore_1
    //   195: aload #5
    //   197: getstatic com/sun/jna/Platform.RESOURCE_PREFIX : Ljava/lang/String;
    //   200: invokevirtual startsWith : (Ljava/lang/String;)Z
    //   203: ifeq -> 213
    //   206: aload_3
    //   207: aload #4
    //   209: invokevirtual getResource : (Ljava/lang/String;)Ljava/net/URL;
    //   212: astore_1
    //   213: aload_1
    //   214: ifnonnull -> 301
    //   217: ldc_w 'java.class.path'
    //   220: invokestatic getProperty : (Ljava/lang/String;)Ljava/lang/String;
    //   223: astore_0
    //   224: aload_3
    //   225: instanceof java/net/URLClassLoader
    //   228: ifeq -> 245
    //   231: aload_3
    //   232: checkcast java/net/URLClassLoader
    //   235: invokevirtual getURLs : ()[Ljava/net/URL;
    //   238: invokestatic asList : ([Ljava/lang/Object;)Ljava/util/List;
    //   241: invokevirtual toString : ()Ljava/lang/String;
    //   244: astore_0
    //   245: new java/lang/StringBuilder
    //   248: dup
    //   249: invokespecial <init> : ()V
    //   252: astore_1
    //   253: aload_1
    //   254: ldc_w 'Native library ('
    //   257: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   260: pop
    //   261: aload_1
    //   262: aload #5
    //   264: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   267: pop
    //   268: aload_1
    //   269: ldc_w ') not found in resource path ('
    //   272: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   275: pop
    //   276: aload_1
    //   277: aload_0
    //   278: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   281: pop
    //   282: aload_1
    //   283: ldc ')'
    //   285: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   288: pop
    //   289: new java/io/IOException
    //   292: dup
    //   293: aload_1
    //   294: invokevirtual toString : ()Ljava/lang/String;
    //   297: invokespecial <init> : (Ljava/lang/String;)V
    //   300: athrow
    //   301: getstatic com/sun/jna/Native.LOG : Ljava/util/logging/Logger;
    //   304: aload_2
    //   305: ldc_w 'Found library resource at {0}'
    //   308: aload_1
    //   309: invokevirtual log : (Ljava/util/logging/Level;Ljava/lang/String;Ljava/lang/Object;)V
    //   312: aload_1
    //   313: invokevirtual getProtocol : ()Ljava/lang/String;
    //   316: invokevirtual toLowerCase : ()Ljava/lang/String;
    //   319: ldc_w 'file'
    //   322: invokevirtual equals : (Ljava/lang/Object;)Z
    //   325: istore #7
    //   327: aconst_null
    //   328: astore #4
    //   330: aconst_null
    //   331: astore #8
    //   333: aconst_null
    //   334: astore #6
    //   336: iload #7
    //   338: ifeq -> 446
    //   341: new java/io/File
    //   344: astore_0
    //   345: new java/net/URI
    //   348: astore_3
    //   349: aload_3
    //   350: aload_1
    //   351: invokevirtual toString : ()Ljava/lang/String;
    //   354: invokespecial <init> : (Ljava/lang/String;)V
    //   357: aload_0
    //   358: aload_3
    //   359: invokespecial <init> : (Ljava/net/URI;)V
    //   362: goto -> 378
    //   365: astore_0
    //   366: new java/io/File
    //   369: dup
    //   370: aload_1
    //   371: invokevirtual getPath : ()Ljava/lang/String;
    //   374: invokespecial <init> : (Ljava/lang/String;)V
    //   377: astore_0
    //   378: getstatic com/sun/jna/Native.LOG : Ljava/util/logging/Logger;
    //   381: aload_2
    //   382: ldc_w 'Looking in {0}'
    //   385: aload_0
    //   386: invokevirtual getAbsolutePath : ()Ljava/lang/String;
    //   389: invokevirtual log : (Ljava/util/logging/Level;Ljava/lang/String;Ljava/lang/Object;)V
    //   392: aload_0
    //   393: invokevirtual exists : ()Z
    //   396: ifeq -> 404
    //   399: aload_0
    //   400: astore_1
    //   401: goto -> 765
    //   404: new java/lang/StringBuilder
    //   407: dup
    //   408: invokespecial <init> : ()V
    //   411: astore_0
    //   412: aload_0
    //   413: ldc_w 'File URL '
    //   416: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   419: pop
    //   420: aload_0
    //   421: aload_1
    //   422: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   425: pop
    //   426: aload_0
    //   427: ldc_w ' could not be properly decoded'
    //   430: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   433: pop
    //   434: new java/io/IOException
    //   437: dup
    //   438: aload_0
    //   439: invokevirtual toString : ()Ljava/lang/String;
    //   442: invokespecial <init> : (Ljava/lang/String;)V
    //   445: athrow
    //   446: aload #8
    //   448: astore_1
    //   449: ldc_w 'jna.nounpack'
    //   452: invokestatic getBoolean : (Ljava/lang/String;)Z
    //   455: ifne -> 765
    //   458: aload_3
    //   459: aload #5
    //   461: invokevirtual getResourceAsStream : (Ljava/lang/String;)Ljava/io/InputStream;
    //   464: astore #8
    //   466: aload #8
    //   468: ifnull -> 730
    //   471: aload #6
    //   473: astore_1
    //   474: invokestatic getTempDir : ()Ljava/io/File;
    //   477: astore #5
    //   479: aload #6
    //   481: astore_1
    //   482: invokestatic isWindows : ()Z
    //   485: ifeq -> 495
    //   488: ldc_w '.dll'
    //   491: astore_3
    //   492: goto -> 497
    //   495: aconst_null
    //   496: astore_3
    //   497: aload #6
    //   499: astore_1
    //   500: ldc 'jna'
    //   502: aload_3
    //   503: aload #5
    //   505: invokestatic createTempFile : (Ljava/lang/String;Ljava/lang/String;Ljava/io/File;)Ljava/io/File;
    //   508: astore #5
    //   510: aload #6
    //   512: astore_1
    //   513: ldc_w 'jnidispatch.preserve'
    //   516: invokestatic getBoolean : (Ljava/lang/String;)Z
    //   519: ifne -> 530
    //   522: aload #6
    //   524: astore_1
    //   525: aload #5
    //   527: invokevirtual deleteOnExit : ()V
    //   530: aload #6
    //   532: astore_1
    //   533: getstatic com/sun/jna/Native.LOG : Ljava/util/logging/Logger;
    //   536: aload_2
    //   537: ldc_w 'Extracting library to {0}'
    //   540: aload #5
    //   542: invokevirtual getAbsolutePath : ()Ljava/lang/String;
    //   545: invokevirtual log : (Ljava/util/logging/Level;Ljava/lang/String;Ljava/lang/Object;)V
    //   548: aload #6
    //   550: astore_1
    //   551: new java/io/FileOutputStream
    //   554: astore_3
    //   555: aload #6
    //   557: astore_1
    //   558: aload_3
    //   559: aload #5
    //   561: invokespecial <init> : (Ljava/io/File;)V
    //   564: sipush #1024
    //   567: newarray byte
    //   569: astore_1
    //   570: aload #8
    //   572: aload_1
    //   573: iconst_0
    //   574: aload_1
    //   575: arraylength
    //   576: invokevirtual read : ([BII)I
    //   579: istore #9
    //   581: iload #9
    //   583: ifle -> 597
    //   586: aload_3
    //   587: aload_1
    //   588: iconst_0
    //   589: iload #9
    //   591: invokevirtual write : ([BII)V
    //   594: goto -> 570
    //   597: aload #8
    //   599: invokevirtual close : ()V
    //   602: aload_3
    //   603: invokevirtual close : ()V
    //   606: aload #5
    //   608: astore_1
    //   609: goto -> 765
    //   612: astore_0
    //   613: aload_3
    //   614: astore_1
    //   615: goto -> 711
    //   618: astore_2
    //   619: goto -> 630
    //   622: astore_0
    //   623: goto -> 711
    //   626: astore_2
    //   627: aload #4
    //   629: astore_3
    //   630: aload_3
    //   631: astore_1
    //   632: new java/io/IOException
    //   635: astore #5
    //   637: aload_3
    //   638: astore_1
    //   639: new java/lang/StringBuilder
    //   642: astore #4
    //   644: aload_3
    //   645: astore_1
    //   646: aload #4
    //   648: invokespecial <init> : ()V
    //   651: aload_3
    //   652: astore_1
    //   653: aload #4
    //   655: ldc_w 'Failed to create temporary file for '
    //   658: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   661: pop
    //   662: aload_3
    //   663: astore_1
    //   664: aload #4
    //   666: aload_0
    //   667: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   670: pop
    //   671: aload_3
    //   672: astore_1
    //   673: aload #4
    //   675: ldc_w ' library: '
    //   678: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   681: pop
    //   682: aload_3
    //   683: astore_1
    //   684: aload #4
    //   686: aload_2
    //   687: invokevirtual getMessage : ()Ljava/lang/String;
    //   690: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   693: pop
    //   694: aload_3
    //   695: astore_1
    //   696: aload #5
    //   698: aload #4
    //   700: invokevirtual toString : ()Ljava/lang/String;
    //   703: invokespecial <init> : (Ljava/lang/String;)V
    //   706: aload_3
    //   707: astore_1
    //   708: aload #5
    //   710: athrow
    //   711: aload #8
    //   713: invokevirtual close : ()V
    //   716: goto -> 720
    //   719: astore_3
    //   720: aload_1
    //   721: ifnull -> 728
    //   724: aload_1
    //   725: invokevirtual close : ()V
    //   728: aload_0
    //   729: athrow
    //   730: new java/lang/StringBuilder
    //   733: dup
    //   734: invokespecial <init> : ()V
    //   737: astore_0
    //   738: aload_0
    //   739: ldc_w 'Can't obtain InputStream for '
    //   742: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   745: pop
    //   746: aload_0
    //   747: aload #5
    //   749: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   752: pop
    //   753: new java/io/IOException
    //   756: dup
    //   757: aload_0
    //   758: invokevirtual toString : ()Ljava/lang/String;
    //   761: invokespecial <init> : (Ljava/lang/String;)V
    //   764: athrow
    //   765: aload_1
    //   766: areturn
    //   767: astore_0
    //   768: goto -> 602
    //   771: astore_0
    //   772: goto -> 606
    //   775: astore_1
    //   776: goto -> 728
    // Exception table:
    //   from	to	target	type
    //   341	362	365	java/net/URISyntaxException
    //   474	479	626	java/io/IOException
    //   474	479	622	finally
    //   482	488	626	java/io/IOException
    //   482	488	622	finally
    //   500	510	626	java/io/IOException
    //   500	510	622	finally
    //   513	522	626	java/io/IOException
    //   513	522	622	finally
    //   525	530	626	java/io/IOException
    //   525	530	622	finally
    //   533	548	626	java/io/IOException
    //   533	548	622	finally
    //   551	555	626	java/io/IOException
    //   551	555	622	finally
    //   558	564	626	java/io/IOException
    //   558	564	622	finally
    //   564	570	618	java/io/IOException
    //   564	570	612	finally
    //   570	581	618	java/io/IOException
    //   570	581	612	finally
    //   586	594	618	java/io/IOException
    //   586	594	612	finally
    //   597	602	767	java/io/IOException
    //   602	606	771	java/io/IOException
    //   632	637	622	finally
    //   639	644	622	finally
    //   646	651	622	finally
    //   653	662	622	finally
    //   664	671	622	finally
    //   673	682	622	finally
    //   684	694	622	finally
    //   696	706	622	finally
    //   708	711	622	finally
    //   711	716	719	java/io/IOException
    //   724	728	775	java/io/IOException
  }
  
  public static native void ffi_call(long paramLong1, long paramLong2, long paramLong3, long paramLong4);
  
  public static native void ffi_free_closure(long paramLong);
  
  public static native long ffi_prep_cif(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
  
  public static native long ffi_prep_closure(long paramLong, ffi_callback paramffi_callback);
  
  static Class<?> findDirectMappedClass(Class<?> paramClass) {
    Method[] arrayOfMethod = paramClass.getDeclaredMethods();
    int i = arrayOfMethod.length;
    int j;
    for (j = 0; j < i; j++) {
      if ((arrayOfMethod[j].getModifiers() & 0x100) != 0)
        return paramClass; 
    } 
    j = paramClass.getName().lastIndexOf("$");
    if (j != -1) {
      String str = paramClass.getName().substring(0, j);
      try {
        return findDirectMappedClass(Class.forName(str, true, paramClass.getClassLoader()));
      } catch (ClassNotFoundException classNotFoundException) {}
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Can't determine class with native methods from the current context (");
    stringBuilder.append(paramClass);
    stringBuilder.append(")");
    IllegalArgumentException illegalArgumentException = new IllegalArgumentException(stringBuilder.toString());
    throw illegalArgumentException;
  }
  
  static Class<?> findEnclosingLibraryClass(Class<?> paramClass) {
    if (paramClass == null)
      return null; 
    Map map = typeOptions.get(paramClass);
    if (map != null) {
      Class<?> clazz1 = (Class)map.get("enclosing-library");
      return (clazz1 != null) ? clazz1 : paramClass;
    } 
    if (Library.class.isAssignableFrom(paramClass))
      return paramClass; 
    Class<?> clazz = paramClass;
    if (Callback.class.isAssignableFrom(paramClass))
      clazz = CallbackReference.findCallbackClass(paramClass); 
    paramClass = findEnclosingLibraryClass(clazz.getDeclaringClass());
    return (paramClass != null) ? paramClass : findEnclosingLibraryClass(clazz.getSuperclass());
  }
  
  static native long findSymbol(long paramLong, String paramString);
  
  public static native void free(long paramLong);
  
  static synchronized native void freeNativeCallback(long paramLong);
  
  private static NativeMapped fromNative(Class<?> paramClass, Object paramObject) {
    return (NativeMapped)NativeMappedConverter.getInstance(paramClass).fromNative(paramObject, new FromNativeContext(paramClass));
  }
  
  private static NativeMapped fromNative(Method paramMethod, Object paramObject) {
    Class<?> clazz = paramMethod.getReturnType();
    return (NativeMapped)NativeMappedConverter.getInstance(clazz).fromNative(paramObject, new MethodResultContext(clazz, null, null, paramMethod));
  }
  
  private static Object fromNative(FromNativeConverter paramFromNativeConverter, Object paramObject, Method paramMethod) {
    return paramFromNativeConverter.fromNative(paramObject, new MethodResultContext(paramMethod.getReturnType(), null, null, paramMethod));
  }
  
  private static native String getAPIChecksum();
  
  static native byte getByte(Pointer paramPointer, long paramLong1, long paramLong2);
  
  static byte[] getBytes(String paramString) {
    return getBytes(paramString, getDefaultStringEncoding());
  }
  
  static byte[] getBytes(String paramString1, String paramString2) {
    return getBytes(paramString1, getCharset(paramString2));
  }
  
  static byte[] getBytes(String paramString, Charset paramCharset) {
    return paramString.getBytes(paramCharset);
  }
  
  public static Callback.UncaughtExceptionHandler getCallbackExceptionHandler() {
    return callbackExceptionHandler;
  }
  
  static Class<?> getCallingClass() {
    Class[] arrayOfClass = (new SecurityManager() {
        public Class<?>[] getClassContext() {
          return super.getClassContext();
        }
      }).getClassContext();
    if (arrayOfClass != null) {
      if (arrayOfClass.length >= 4)
        return arrayOfClass[3]; 
      throw new IllegalStateException("This method must be called from the static initializer of a class");
    } 
    throw new IllegalStateException("The SecurityManager implementation on this platform is broken; you must explicitly provide the class to register");
  }
  
  static native char getChar(Pointer paramPointer, long paramLong1, long paramLong2);
  
  private static Charset getCharset(String paramString) {
    if (paramString != null) {
      Charset charset1;
      try {
        Charset charset = Charset.forName(paramString);
        charset1 = charset;
      } catch (IllegalCharsetNameException illegalCharsetNameException) {
        LOG.log(Level.WARNING, "JNA Warning: Encoding ''{0}'' is unsupported ({1})", new Object[] { charset1, illegalCharsetNameException.getMessage() });
        charset1 = null;
      } catch (UnsupportedCharsetException unsupportedCharsetException) {
        LOG.log(Level.WARNING, "JNA Warning: Encoding ''{0}'' is unsupported ({1})", new Object[] { charset1, unsupportedCharsetException.getMessage() });
      } 
      Charset charset2 = charset1;
      if (charset1 == null) {
        LOG.log(Level.WARNING, "JNA Warning: Using fallback encoding {0}", DEFAULT_CHARSET);
        charset2 = DEFAULT_CHARSET;
      } 
      return charset2;
    } 
    paramString = null;
  }
  
  public static long getComponentID(Component paramComponent) throws HeadlessException {
    return AWT.getComponentID(paramComponent);
  }
  
  public static Pointer getComponentPointer(Component paramComponent) throws HeadlessException {
    return new Pointer(AWT.getComponentID(paramComponent));
  }
  
  private static int getConversion(Class<?> paramClass, TypeMapper paramTypeMapper, boolean paramBoolean) {
    byte b;
    Class<?> clazz = paramClass;
    if (paramClass == Void.class)
      clazz = void.class; 
    if (paramTypeMapper != null) {
      FromNativeConverter fromNativeConverter = paramTypeMapper.getFromNativeConverter(clazz);
      ToNativeConverter toNativeConverter = paramTypeMapper.getToNativeConverter(clazz);
      if (fromNativeConverter != null) {
        Class<?> clazz1 = fromNativeConverter.nativeType();
        return (clazz1 == String.class) ? 24 : ((clazz1 == WString.class) ? 25 : 23);
      } 
      if (toNativeConverter != null) {
        Class<?> clazz1 = toNativeConverter.nativeType();
        return (clazz1 == String.class) ? 24 : ((clazz1 == WString.class) ? 25 : 23);
      } 
    } 
    if (Pointer.class.isAssignableFrom(clazz))
      return 1; 
    if (String.class == clazz)
      return 2; 
    if (WString.class.isAssignableFrom(clazz))
      return 20; 
    if (Platform.HAS_BUFFERS && Buffers.isBuffer(clazz))
      return 5; 
    if (Structure.class.isAssignableFrom(clazz))
      return Structure.ByValue.class.isAssignableFrom(clazz) ? 4 : 3; 
    if (clazz.isArray()) {
      b = clazz.getName().charAt(1);
      if (b != 70) {
        if (b != 83) {
          if (b != 90) {
            if (b != 73) {
              if (b != 74) {
                switch (b) {
                  case 'D':
                    return 12;
                  case 'C':
                    return 8;
                  case 'B':
                    return 6;
                } 
              } else {
                return 10;
              } 
            } else {
              return 9;
            } 
          } else {
            return 13;
          } 
        } else {
          return 7;
        } 
      } else {
        return 11;
      } 
    } 
    if (clazz.isPrimitive()) {
      if (clazz == boolean.class) {
        b = 14;
      } else {
        b = 0;
      } 
      return b;
    } 
    if (Callback.class.isAssignableFrom(clazz))
      return 15; 
    if (IntegerType.class.isAssignableFrom(clazz))
      return 21; 
    if (PointerType.class.isAssignableFrom(clazz))
      return 22; 
    if (NativeMapped.class.isAssignableFrom(clazz)) {
      paramClass = NativeMappedConverter.getInstance(clazz).nativeType();
      return (paramClass == String.class) ? 18 : ((paramClass == WString.class) ? 19 : 17);
    } 
    if (JNIEnv.class == clazz)
      return 27; 
    if (paramBoolean) {
      b = 26;
    } else {
      b = -1;
    } 
    return b;
  }
  
  public static String getDefaultStringEncoding() {
    return System.getProperty("jna.encoding", DEFAULT_ENCODING);
  }
  
  public static Pointer getDirectBufferPointer(Buffer paramBuffer) {
    Pointer pointer;
    long l = _getDirectBufferPointer(paramBuffer);
    if (l == 0L) {
      paramBuffer = null;
    } else {
      pointer = new Pointer(l);
    } 
    return pointer;
  }
  
  static native ByteBuffer getDirectByteBuffer(Pointer paramPointer, long paramLong1, long paramLong2, long paramLong3);
  
  static native double getDouble(Pointer paramPointer, long paramLong1, long paramLong2);
  
  static native float getFloat(Pointer paramPointer, long paramLong1, long paramLong2);
  
  static native int getInt(Pointer paramPointer, long paramLong1, long paramLong2);
  
  public static native int getLastError();
  
  public static Map<String, Object> getLibraryOptions(Class<?> paramClass) {
    StringBuilder stringBuilder;
    Map<String, Object> map1 = typeOptions.get(paramClass);
    if (map1 != null)
      return map1; 
    Class<?> clazz = findEnclosingLibraryClass(paramClass);
    if (clazz != null) {
      loadLibraryInstance(clazz);
    } else {
      clazz = paramClass;
    } 
    Map<String, Object> map3 = typeOptions.get(clazz);
    if (map3 != null) {
      typeOptions.put(paramClass, map3);
      return map3;
    } 
    try {
      Field field = clazz.getField("OPTIONS");
      field.setAccessible(true);
      Map map = (Map)field.get(null);
      if (map == null) {
        exception = new IllegalStateException();
        this("Null options field");
        throw exception;
      } 
    } catch (NoSuchFieldException noSuchFieldException) {
      Map<?, ?> map = Collections.emptyMap();
    } catch (Exception exception) {
      stringBuilder = new StringBuilder();
      stringBuilder.append("OPTIONS must be a public field of type java.util.Map (");
      stringBuilder.append(exception);
      stringBuilder.append("): ");
      stringBuilder.append(clazz);
      throw new IllegalArgumentException(stringBuilder.toString());
    } 
    HashMap<Object, Object> hashMap = new HashMap<Object, Object>((Map<?, ?>)exception);
    if (!hashMap.containsKey("type-mapper"))
      hashMap.put("type-mapper", lookupField(clazz, "TYPE_MAPPER", TypeMapper.class)); 
    if (!hashMap.containsKey("structure-alignment"))
      hashMap.put("structure-alignment", lookupField(clazz, "STRUCTURE_ALIGNMENT", Integer.class)); 
    if (!hashMap.containsKey("string-encoding"))
      hashMap.put("string-encoding", lookupField(clazz, "STRING_ENCODING", String.class)); 
    Map<String, Object> map2 = cacheOptions(clazz, (Map)hashMap, null);
    if (stringBuilder != clazz)
      typeOptions.put(stringBuilder, map2); 
    return map2;
  }
  
  static native long getLong(Pointer paramPointer, long paramLong1, long paramLong2);
  
  public static int getNativeSize(Class<?> paramClass) {
    Class<?> clazz = paramClass;
    if (NativeMapped.class.isAssignableFrom(paramClass))
      clazz = NativeMappedConverter.getInstance(paramClass).nativeType(); 
    if (clazz == boolean.class || clazz == Boolean.class)
      return 4; 
    if (clazz == byte.class || clazz == Byte.class)
      return 1; 
    if (clazz == short.class || clazz == Short.class)
      return 2; 
    if (clazz == char.class || clazz == Character.class)
      return WCHAR_SIZE; 
    if (clazz == int.class || clazz == Integer.class)
      return 4; 
    if (clazz == long.class || clazz == Long.class)
      return 8; 
    if (clazz == float.class || clazz == Float.class)
      return 4; 
    if (clazz == double.class || clazz == Double.class)
      return 8; 
    if (Structure.class.isAssignableFrom(clazz))
      return Structure.ByValue.class.isAssignableFrom(clazz) ? Structure.size((Class)clazz) : POINTER_SIZE; 
    if (Pointer.class.isAssignableFrom(clazz) || (Platform.HAS_BUFFERS && Buffers.isBuffer(clazz)) || Callback.class.isAssignableFrom(clazz) || String.class == clazz || WString.class == clazz)
      return POINTER_SIZE; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Native size for type \"");
    stringBuilder.append(clazz.getName());
    stringBuilder.append("\" is unknown");
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public static int getNativeSize(Class<?> paramClass, Object paramObject) {
    if (paramClass.isArray()) {
      int i = Array.getLength(paramObject);
      if (i > 0) {
        paramObject = Array.get(paramObject, 0);
        return i * getNativeSize(paramClass.getComponentType(), paramObject);
      } 
      paramObject = new StringBuilder();
      paramObject.append("Arrays of length zero not allowed: ");
      paramObject.append(paramClass);
      throw new IllegalArgumentException(paramObject.toString());
    } 
    if (Structure.class.isAssignableFrom(paramClass) && !Structure.ByReference.class.isAssignableFrom(paramClass))
      return Structure.size(paramClass, paramObject); 
    try {
      return getNativeSize(paramClass);
    } catch (IllegalArgumentException illegalArgumentException) {
      paramObject = new StringBuilder();
      paramObject.append("The type \"");
      paramObject.append(paramClass.getName());
      paramObject.append("\" is not supported: ");
      paramObject.append(illegalArgumentException.getMessage());
      throw new IllegalArgumentException(paramObject.toString());
    } 
  }
  
  private static native String getNativeVersion();
  
  static Pointer getPointer(long paramLong) {
    Pointer pointer;
    paramLong = _getPointer(paramLong);
    if (paramLong == 0L) {
      pointer = null;
    } else {
      pointer = new Pointer(paramLong);
    } 
    return pointer;
  }
  
  static native short getShort(Pointer paramPointer, long paramLong1, long paramLong2);
  
  static String getSignature(Class<?> paramClass) {
    if (paramClass.isArray()) {
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("[");
      stringBuilder1.append(getSignature(paramClass.getComponentType()));
      return stringBuilder1.toString();
    } 
    if (paramClass.isPrimitive()) {
      if (paramClass == void.class)
        return "V"; 
      if (paramClass == boolean.class)
        return "Z"; 
      if (paramClass == byte.class)
        return "B"; 
      if (paramClass == short.class)
        return "S"; 
      if (paramClass == char.class)
        return "C"; 
      if (paramClass == int.class)
        return "I"; 
      if (paramClass == long.class)
        return "J"; 
      if (paramClass == float.class)
        return "F"; 
      if (paramClass == double.class)
        return "D"; 
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("L");
    stringBuilder.append(replace(".", "/", paramClass.getName()));
    stringBuilder.append(";");
    return stringBuilder.toString();
  }
  
  static String getString(Pointer paramPointer, long paramLong) {
    return getString(paramPointer, paramLong, getDefaultStringEncoding());
  }
  
  static String getString(Pointer paramPointer, long paramLong, String paramString) {
    byte[] arrayOfByte = getStringBytes(paramPointer, paramPointer.peer, paramLong);
    if (paramString != null)
      try {
        return new String(arrayOfByte, paramString);
      } catch (UnsupportedEncodingException unsupportedEncodingException) {} 
    return new String(arrayOfByte);
  }
  
  static native byte[] getStringBytes(Pointer paramPointer, long paramLong1, long paramLong2);
  
  public static String getStringEncoding(Class<?> paramClass) {
    String str = (String)getLibraryOptions(paramClass).get("string-encoding");
    if (str == null)
      str = getDefaultStringEncoding(); 
    return str;
  }
  
  public static int getStructureAlignment(Class<?> paramClass) {
    int i;
    Integer integer = (Integer)getLibraryOptions(paramClass).get("structure-alignment");
    if (integer == null) {
      i = 0;
    } else {
      i = integer.intValue();
    } 
    return i;
  }
  
  static File getTempDir() throws IOException {
    File file;
    String str = System.getProperty("jna.tmpdir");
    if (str != null) {
      file = new File(str);
      file.mkdirs();
    } else {
      File file1;
      File file2 = new File(System.getProperty("java.io.tmpdir"));
      if (Platform.isMac()) {
        file1 = new File(System.getProperty("user.home"), "Library/Caches/JNA/temp");
      } else if (Platform.isLinux() || Platform.isSolaris() || Platform.isAIX() || Platform.isFreeBSD() || Platform.isNetBSD() || Platform.isOpenBSD() || Platform.iskFreeBSD()) {
        str = System.getenv("XDG_CACHE_HOME");
        if (str == null || str.trim().isEmpty()) {
          file1 = new File(System.getProperty("user.home"), ".cache");
        } else {
          file1 = new File((String)file1);
        } 
        file1 = new File(file1, "JNA/temp");
      } else {
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("jna-");
        stringBuilder1.append(System.getProperty("user.name").hashCode());
        file1 = new File(file2, stringBuilder1.toString());
      } 
      file1.mkdirs();
      file = file2;
      if (file1.exists())
        if (!file1.canWrite()) {
          file = file2;
        } else {
          file = file1;
        }  
    } 
    if (file.exists()) {
      if (file.canWrite())
        return file; 
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("JNA temporary directory '");
      stringBuilder1.append(file);
      stringBuilder1.append("' is not writable");
      throw new IOException(stringBuilder1.toString());
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("JNA temporary directory '");
    stringBuilder.append(file);
    stringBuilder.append("' does not exist");
    throw new IOException(stringBuilder.toString());
  }
  
  static Pointer getTerminationFlag(Thread paramThread) {
    return nativeThreads.get(paramThread);
  }
  
  public static TypeMapper getTypeMapper(Class<?> paramClass) {
    return (TypeMapper)getLibraryOptions(paramClass).get("type-mapper");
  }
  
  public static String getWebStartLibraryPath(String paramString) {
    if (System.getProperty("javawebstart.version") == null)
      return null; 
    try {
      ClassLoader classLoader = Native.class.getClassLoader();
      PrivilegedAction<Method> privilegedAction = new PrivilegedAction<Method>() {
          public Method run() {
            try {
              Method method = ClassLoader.class.getDeclaredMethod("findLibrary", new Class[] { String.class });
              method.setAccessible(true);
              return method;
            } catch (Exception exception) {
              return null;
            } 
          }
        };
      super();
      String str = (String)((Method)AccessController.<Method>doPrivileged(privilegedAction)).invoke(classLoader, new Object[] { paramString });
      if (str != null) {
        File file = new File();
        this(str);
        return file.getParent();
      } 
    } catch (Exception exception) {}
    return null;
  }
  
  static native String getWideString(Pointer paramPointer, long paramLong1, long paramLong2);
  
  static native long getWindowHandle0(Component paramComponent);
  
  public static long getWindowID(Window paramWindow) throws HeadlessException {
    return AWT.getWindowID(paramWindow);
  }
  
  public static Pointer getWindowPointer(Window paramWindow) throws HeadlessException {
    return new Pointer(AWT.getWindowID(paramWindow));
  }
  
  static native long indexOf(Pointer paramPointer, long paramLong1, long paramLong2, byte paramByte);
  
  private static native void initIDs();
  
  static native int initialize_ffi_type(long paramLong);
  
  static native double invokeDouble(Function paramFunction, long paramLong, int paramInt, Object[] paramArrayOfObject);
  
  static native float invokeFloat(Function paramFunction, long paramLong, int paramInt, Object[] paramArrayOfObject);
  
  static native int invokeInt(Function paramFunction, long paramLong, int paramInt, Object[] paramArrayOfObject);
  
  static native long invokeLong(Function paramFunction, long paramLong, int paramInt, Object[] paramArrayOfObject);
  
  static native Object invokeObject(Function paramFunction, long paramLong, int paramInt, Object[] paramArrayOfObject);
  
  static native long invokePointer(Function paramFunction, long paramLong, int paramInt, Object[] paramArrayOfObject);
  
  static Structure invokeStructure(Function paramFunction, long paramLong, int paramInt, Object[] paramArrayOfObject, Structure paramStructure) {
    invokeStructure(paramFunction, paramLong, paramInt, paramArrayOfObject, (paramStructure.getPointer()).peer, (paramStructure.getTypeInfo()).peer);
    return paramStructure;
  }
  
  private static native void invokeStructure(Function paramFunction, long paramLong1, int paramInt, Object[] paramArrayOfObject, long paramLong2, long paramLong3);
  
  static native void invokeVoid(Function paramFunction, long paramLong, int paramInt, Object[] paramArrayOfObject);
  
  static boolean isCompatibleVersion(String paramString1, String paramString2) {
    String[] arrayOfString1 = paramString1.split("\\.");
    String[] arrayOfString2 = paramString2.split("\\.");
    if (arrayOfString1.length < 3 || arrayOfString2.length < 3)
      return false; 
    int i = Integer.parseInt(arrayOfString1[0]);
    int j = Integer.parseInt(arrayOfString2[0]);
    int k = Integer.parseInt(arrayOfString1[1]);
    int m = Integer.parseInt(arrayOfString2[1]);
    return (i != j) ? false : (!(k > m));
  }
  
  public static synchronized native boolean isProtected();
  
  public static boolean isSupportedNativeType(Class<?> paramClass) {
    if (Structure.class.isAssignableFrom(paramClass))
      return true; 
    boolean bool = false;
    try {
      int i = getNativeSize(paramClass);
      if (i != 0)
        bool = true; 
    } catch (IllegalArgumentException illegalArgumentException) {}
    return bool;
  }
  
  static boolean isUnpacked(File paramFile) {
    return paramFile.getName().startsWith("jna");
  }
  
  public static <T extends Library> T load(Class<T> paramClass) {
    return load((String)null, paramClass);
  }
  
  public static <T extends Library> T load(Class<T> paramClass, Map<String, ?> paramMap) {
    return load(null, paramClass, paramMap);
  }
  
  public static <T extends Library> T load(String paramString, Class<T> paramClass) {
    return load(paramString, paramClass, Collections.emptyMap());
  }
  
  public static <T extends Library> T load(String paramString, Class<T> paramClass, Map<String, ?> paramMap) {
    Object object;
    if (Library.class.isAssignableFrom(paramClass)) {
      Library.Handler handler = new Library.Handler(paramString, paramClass, paramMap);
      object = Proxy.newProxyInstance(paramClass.getClassLoader(), new Class[] { paramClass }, handler);
      cacheOptions(paramClass, paramMap, object);
      return paramClass.cast(object);
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Interface (");
    stringBuilder.append(paramClass.getSimpleName());
    stringBuilder.append(") of library=");
    stringBuilder.append((String)object);
    stringBuilder.append(" does not extend ");
    stringBuilder.append(Library.class.getSimpleName());
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  @Deprecated
  public static <T> T loadLibrary(Class<T> paramClass) {
    return loadLibrary((String)null, paramClass);
  }
  
  @Deprecated
  public static <T> T loadLibrary(Class<T> paramClass, Map<String, ?> paramMap) {
    return loadLibrary(null, paramClass, paramMap);
  }
  
  @Deprecated
  public static <T> T loadLibrary(String paramString, Class<T> paramClass) {
    return loadLibrary(paramString, paramClass, Collections.emptyMap());
  }
  
  @Deprecated
  public static <T> T loadLibrary(String paramString, Class<T> paramClass, Map<String, ?> paramMap) {
    Object object;
    if (Library.class.isAssignableFrom(paramClass)) {
      Library.Handler handler = new Library.Handler(paramString, paramClass, paramMap);
      object = Proxy.newProxyInstance(paramClass.getClassLoader(), new Class[] { paramClass }, handler);
      cacheOptions(paramClass, paramMap, object);
      return paramClass.cast(object);
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Interface (");
    stringBuilder.append(paramClass.getSimpleName());
    stringBuilder.append(") of library=");
    stringBuilder.append((String)object);
    stringBuilder.append(" does not extend ");
    stringBuilder.append(Library.class.getSimpleName());
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  private static void loadLibraryInstance(Class<?> paramClass) {
    if (paramClass != null && !libraries.containsKey(paramClass))
      try {
        Field[] arrayOfField = paramClass.getFields();
        for (byte b = 0; b < arrayOfField.length; b++) {
          Field field = arrayOfField[b];
          if (field.getType() == paramClass && Modifier.isStatic(field.getModifiers())) {
            Map<Class<?>, Reference<?>> map = libraries;
            WeakReference<?> weakReference = new WeakReference();
            this((T)field.get(null));
            map.put(paramClass, weakReference);
            break;
          } 
        } 
      } catch (Exception exception) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Could not access instance of ");
        stringBuilder.append(paramClass);
        stringBuilder.append(" (");
        stringBuilder.append(exception);
        stringBuilder.append(")");
        throw new IllegalArgumentException(stringBuilder.toString());
      }  
  }
  
  private static void loadNativeDispatchLibrary() {
    if (!Boolean.getBoolean("jna.nounpack"))
      try {
        removeTemporaryFiles();
      } catch (IOException iOException) {
        LOG.log(Level.WARNING, "JNA Warning: IOException removing temporary files", iOException);
      }  
    String str2 = System.getProperty("jna.boot.library.name", "jnidispatch");
    String str1 = System.getProperty("jna.boot.library.path");
    if (str1 != null) {
      StringTokenizer stringTokenizer = new StringTokenizer(str1, File.pathSeparator);
      while (stringTokenizer.hasMoreTokens()) {
        File file = new File(new File(stringTokenizer.nextToken()), System.mapLibraryName(str2).replace(".dylib", ".jnilib"));
        String str = file.getAbsolutePath();
        LOG.log(DEBUG_JNA_LOAD_LEVEL, "Looking in {0}", str);
        if (file.exists())
          try {
            LOG.log(DEBUG_JNA_LOAD_LEVEL, "Trying {0}", str);
            System.setProperty("jnidispatch.path", str);
            System.load(str);
            jnidispatchPath = str;
            LOG.log(DEBUG_JNA_LOAD_LEVEL, "Found jnidispatch at {0}", str);
            return;
          } catch (UnsatisfiedLinkError unsatisfiedLinkError1) {} 
        if (Platform.isMac()) {
          String str3 = "dylib";
          boolean bool = str.endsWith("dylib");
          String str4 = "jnilib";
          if (!bool) {
            str4 = "dylib";
            str3 = "jnilib";
          } 
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append(str.substring(0, str.lastIndexOf(str3)));
          stringBuilder.append(str4);
          String str5 = stringBuilder.toString();
          LOG.log(DEBUG_JNA_LOAD_LEVEL, "Looking in {0}", str5);
          if ((new File(str5)).exists())
            try {
              LOG.log(DEBUG_JNA_LOAD_LEVEL, "Trying {0}", str5);
              System.setProperty("jnidispatch.path", str5);
              System.load(str5);
              jnidispatchPath = str5;
              LOG.log(DEBUG_JNA_LOAD_LEVEL, "Found jnidispatch at {0}", str5);
              return;
            } catch (UnsatisfiedLinkError unsatisfiedLinkError1) {
              Logger logger = LOG;
              Level level = Level.WARNING;
              StringBuilder stringBuilder1 = new StringBuilder();
              stringBuilder1.append("File found at ");
              stringBuilder1.append(str5);
              stringBuilder1.append(" but not loadable: ");
              stringBuilder1.append(unsatisfiedLinkError1.getMessage());
              logger.log(level, stringBuilder1.toString(), unsatisfiedLinkError1);
            }  
        } 
      } 
    } 
    if (!Boolean.parseBoolean(System.getProperty("jna.nosys", "true")) || Platform.isAndroid())
      try {
        LOG.log(DEBUG_JNA_LOAD_LEVEL, "Trying (via loadLibrary) {0}", str2);
        System.loadLibrary(str2);
        LOG.log(DEBUG_JNA_LOAD_LEVEL, "Found jnidispatch on system path");
        return;
      } catch (UnsatisfiedLinkError unsatisfiedLinkError1) {} 
    if (!Boolean.getBoolean("jna.noclasspath")) {
      loadNativeDispatchLibraryFromClasspath();
      return;
    } 
    UnsatisfiedLinkError unsatisfiedLinkError = new UnsatisfiedLinkError("Unable to locate JNA native support library");
    throw unsatisfiedLinkError;
  }
  
  private static void loadNativeDispatchLibraryFromClasspath() {
    try {
      String str = System.mapLibraryName("jnidispatch").replace(".dylib", ".jnilib");
      if (Platform.isAIX())
        str = "libjnidispatch.a"; 
      StringBuilder stringBuilder = new StringBuilder();
      this();
      stringBuilder.append("/com/sun/jna/");
      stringBuilder.append(Platform.RESOURCE_PREFIX);
      stringBuilder.append("/");
      stringBuilder.append(str);
      File file = extractFromResourcePath(stringBuilder.toString(), Native.class.getClassLoader());
      if (file != null || file != null) {
        LOG.log(DEBUG_JNA_LOAD_LEVEL, "Trying {0}", file.getAbsolutePath());
        System.setProperty("jnidispatch.path", file.getAbsolutePath());
        System.load(file.getAbsolutePath());
        jnidispatchPath = file.getAbsolutePath();
        LOG.log(DEBUG_JNA_LOAD_LEVEL, "Found jnidispatch at {0}", jnidispatchPath);
        if (isUnpacked(file) && !Boolean.getBoolean("jnidispatch.preserve"))
          deleteLibrary(file); 
        return;
      } 
      UnsatisfiedLinkError unsatisfiedLinkError = new UnsatisfiedLinkError();
      this("Could not find JNA native support");
      throw unsatisfiedLinkError;
    } catch (IOException iOException) {
      throw new UnsatisfiedLinkError(iOException.getMessage());
    } 
  }
  
  private static Object lookupField(Class<?> paramClass1, String paramString, Class<?> paramClass2) {
    try {
      Field field = paramClass1.getField(paramString);
      field.setAccessible(true);
      return field.get(null);
    } catch (NoSuchFieldException noSuchFieldException) {
      return null;
    } catch (Exception exception) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(paramString);
      stringBuilder.append(" must be a public field of type ");
      stringBuilder.append(paramClass2.getName());
      stringBuilder.append(" (");
      stringBuilder.append(exception);
      stringBuilder.append("): ");
      stringBuilder.append(noSuchFieldException);
      throw new IllegalArgumentException(stringBuilder.toString());
    } 
  }
  
  public static void main(String[] paramArrayOfString) {
    Package package_ = Native.class.getPackage();
    String str2 = "Java Native Access (JNA)";
    if (package_ != null) {
      str1 = package_.getSpecificationTitle();
    } else {
      str1 = "Java Native Access (JNA)";
    } 
    if (str1 != null)
      str2 = str1; 
    String str3 = "5.4.0";
    if (package_ != null) {
      str1 = package_.getSpecificationVersion();
    } else {
      str1 = "5.4.0";
    } 
    if (str1 == null)
      str1 = str3; 
    StringBuilder stringBuilder3 = new StringBuilder();
    stringBuilder3.append(str2);
    stringBuilder3.append(" API Version ");
    stringBuilder3.append(str1);
    String str1 = stringBuilder3.toString();
    System.out.println(str1);
    if (package_ != null) {
      str1 = package_.getImplementationVersion();
    } else {
      str1 = "5.4.0 (package information missing)";
    } 
    str2 = str1;
    if (str1 == null)
      str2 = "5.4.0 (package information missing)"; 
    PrintStream printStream3 = System.out;
    StringBuilder stringBuilder1 = new StringBuilder();
    stringBuilder1.append("Version: ");
    stringBuilder1.append(str2);
    printStream3.println(stringBuilder1.toString());
    PrintStream printStream2 = System.out;
    stringBuilder1 = new StringBuilder();
    stringBuilder1.append(" Native: ");
    stringBuilder1.append(getNativeVersion());
    stringBuilder1.append(" (");
    stringBuilder1.append(getAPIChecksum());
    stringBuilder1.append(")");
    printStream2.println(stringBuilder1.toString());
    PrintStream printStream1 = System.out;
    StringBuilder stringBuilder2 = new StringBuilder();
    stringBuilder2.append(" Prefix: ");
    stringBuilder2.append(Platform.RESOURCE_PREFIX);
    printStream1.println(stringBuilder2.toString());
  }
  
  public static native long malloc(long paramLong);
  
  static void markTemporaryFile(File paramFile) {
    try {
      File file1 = new File();
      File file2 = paramFile.getParentFile();
      StringBuilder stringBuilder = new StringBuilder();
      this();
      stringBuilder.append(paramFile.getName());
      stringBuilder.append(".x");
      this(file2, stringBuilder.toString());
      file1.createNewFile();
    } catch (IOException iOException) {
      iOException.printStackTrace();
    } 
  }
  
  private static Class<?> nativeType(Class<?> paramClass) {
    return NativeMappedConverter.getInstance(paramClass).nativeType();
  }
  
  static long open(String paramString) {
    return open(paramString, -1);
  }
  
  static native long open(String paramString, int paramInt);
  
  static native void read(Pointer paramPointer, long paramLong1, long paramLong2, byte[] paramArrayOfbyte, int paramInt1, int paramInt2);
  
  static native void read(Pointer paramPointer, long paramLong1, long paramLong2, char[] paramArrayOfchar, int paramInt1, int paramInt2);
  
  static native void read(Pointer paramPointer, long paramLong1, long paramLong2, double[] paramArrayOfdouble, int paramInt1, int paramInt2);
  
  static native void read(Pointer paramPointer, long paramLong1, long paramLong2, float[] paramArrayOffloat, int paramInt1, int paramInt2);
  
  static native void read(Pointer paramPointer, long paramLong1, long paramLong2, int[] paramArrayOfint, int paramInt1, int paramInt2);
  
  static native void read(Pointer paramPointer, long paramLong1, long paramLong2, long[] paramArrayOflong, int paramInt1, int paramInt2);
  
  static native void read(Pointer paramPointer, long paramLong1, long paramLong2, short[] paramArrayOfshort, int paramInt1, int paramInt2);
  
  public static void register(NativeLibrary paramNativeLibrary) {
    register(findDirectMappedClass(getCallingClass()), paramNativeLibrary);
  }
  
  public static void register(Class<?> paramClass, NativeLibrary paramNativeLibrary) {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual getDeclaredMethods : ()[Ljava/lang/reflect/Method;
    //   4: astore_2
    //   5: new java/util/ArrayList
    //   8: dup
    //   9: invokespecial <init> : ()V
    //   12: astore_3
    //   13: aload_1
    //   14: invokevirtual getOptions : ()Ljava/util/Map;
    //   17: astore #4
    //   19: aload #4
    //   21: ldc_w 'type-mapper'
    //   24: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   29: checkcast com/sun/jna/TypeMapper
    //   32: astore #5
    //   34: getstatic java/lang/Boolean.TRUE : Ljava/lang/Boolean;
    //   37: aload #4
    //   39: ldc_w 'allow-objects'
    //   42: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   47: invokevirtual equals : (Ljava/lang/Object;)Z
    //   50: istore #6
    //   52: aload_0
    //   53: aload #4
    //   55: aconst_null
    //   56: invokestatic cacheOptions : (Ljava/lang/Class;Ljava/util/Map;Ljava/lang/Object;)Ljava/util/Map;
    //   59: pop
    //   60: aload_2
    //   61: arraylength
    //   62: istore #7
    //   64: iconst_0
    //   65: istore #8
    //   67: iload #8
    //   69: iload #7
    //   71: if_icmpge -> 107
    //   74: aload_2
    //   75: iload #8
    //   77: aaload
    //   78: astore #4
    //   80: aload #4
    //   82: invokevirtual getModifiers : ()I
    //   85: sipush #256
    //   88: iand
    //   89: ifeq -> 101
    //   92: aload_3
    //   93: aload #4
    //   95: invokeinterface add : (Ljava/lang/Object;)Z
    //   100: pop
    //   101: iinc #8, 1
    //   104: goto -> 67
    //   107: aload_3
    //   108: invokeinterface size : ()I
    //   113: newarray long
    //   115: astore #4
    //   117: iconst_0
    //   118: istore #8
    //   120: aload_0
    //   121: astore #9
    //   123: iload #8
    //   125: aload #4
    //   127: arraylength
    //   128: if_icmpge -> 1235
    //   131: aload_3
    //   132: iload #8
    //   134: invokeinterface get : (I)Ljava/lang/Object;
    //   139: checkcast java/lang/reflect/Method
    //   142: astore #10
    //   144: aload #10
    //   146: invokevirtual getReturnType : ()Ljava/lang/Class;
    //   149: astore #11
    //   151: aload #10
    //   153: invokevirtual getParameterTypes : ()[Ljava/lang/Class;
    //   156: astore #12
    //   158: aload #12
    //   160: arraylength
    //   161: newarray long
    //   163: astore #13
    //   165: aload #12
    //   167: arraylength
    //   168: newarray long
    //   170: astore #14
    //   172: aload #12
    //   174: arraylength
    //   175: newarray int
    //   177: astore #15
    //   179: aload #12
    //   181: arraylength
    //   182: anewarray com/sun/jna/ToNativeConverter
    //   185: astore #16
    //   187: aload #11
    //   189: aload #5
    //   191: iload #6
    //   193: invokestatic getConversion : (Ljava/lang/Class;Lcom/sun/jna/TypeMapper;Z)I
    //   196: istore #17
    //   198: iload #17
    //   200: iconst_m1
    //   201: if_icmpeq -> 1168
    //   204: iload #17
    //   206: iconst_3
    //   207: if_icmpeq -> 445
    //   210: iload #17
    //   212: iconst_4
    //   213: if_icmpeq -> 402
    //   216: iload #17
    //   218: tableswitch default -> 244, 17 -> 370, 18 -> 370, 19 -> 370
    //   244: iload #17
    //   246: tableswitch default -> 284, 21 -> 370, 22 -> 370, 23 -> 307, 24 -> 307, 25 -> 307, 26 -> 304
    //   284: aload #11
    //   286: invokestatic get : (Ljava/lang/Object;)Lcom/sun/jna/Pointer;
    //   289: getfield peer : J
    //   292: lstore #18
    //   294: lload #18
    //   296: lstore #20
    //   298: aconst_null
    //   299: astore #22
    //   301: goto -> 459
    //   304: goto -> 445
    //   307: aload #5
    //   309: aload #11
    //   311: invokeinterface getFromNativeConverter : (Ljava/lang/Class;)Lcom/sun/jna/FromNativeConverter;
    //   316: astore #22
    //   318: aload #11
    //   320: invokevirtual isPrimitive : ()Z
    //   323: ifeq -> 332
    //   326: aload #11
    //   328: astore_2
    //   329: goto -> 336
    //   332: ldc_w com/sun/jna/Pointer
    //   335: astore_2
    //   336: aload_2
    //   337: invokestatic get : (Ljava/lang/Object;)Lcom/sun/jna/Pointer;
    //   340: getfield peer : J
    //   343: lstore #20
    //   345: aload #22
    //   347: invokeinterface nativeType : ()Ljava/lang/Class;
    //   352: invokestatic get : (Ljava/lang/Object;)Lcom/sun/jna/Pointer;
    //   355: astore #23
    //   357: aload #22
    //   359: astore_2
    //   360: aload #23
    //   362: getfield peer : J
    //   365: lstore #18
    //   367: goto -> 427
    //   370: ldc_w com/sun/jna/Pointer
    //   373: invokestatic get : (Ljava/lang/Object;)Lcom/sun/jna/Pointer;
    //   376: getfield peer : J
    //   379: lstore #20
    //   381: aload #11
    //   383: invokestatic getInstance : (Ljava/lang/Class;)Lcom/sun/jna/NativeMappedConverter;
    //   386: invokevirtual nativeType : ()Ljava/lang/Class;
    //   389: invokestatic get : (Ljava/lang/Object;)Lcom/sun/jna/Pointer;
    //   392: astore_2
    //   393: aload_2
    //   394: getfield peer : J
    //   397: lstore #18
    //   399: goto -> 425
    //   402: ldc_w com/sun/jna/Pointer
    //   405: invokestatic get : (Ljava/lang/Object;)Lcom/sun/jna/Pointer;
    //   408: getfield peer : J
    //   411: lstore #20
    //   413: aload #11
    //   415: invokestatic get : (Ljava/lang/Object;)Lcom/sun/jna/Pointer;
    //   418: astore_2
    //   419: aload_2
    //   420: getfield peer : J
    //   423: lstore #18
    //   425: aconst_null
    //   426: astore_2
    //   427: lload #20
    //   429: lstore #24
    //   431: lload #18
    //   433: lstore #20
    //   435: lload #24
    //   437: lstore #18
    //   439: aload_2
    //   440: astore #22
    //   442: goto -> 459
    //   445: ldc_w com/sun/jna/Pointer
    //   448: invokestatic get : (Ljava/lang/Object;)Lcom/sun/jna/Pointer;
    //   451: getfield peer : J
    //   454: lstore #18
    //   456: goto -> 294
    //   459: ldc_w '('
    //   462: astore_2
    //   463: iconst_0
    //   464: istore #7
    //   466: iload #7
    //   468: aload #12
    //   470: arraylength
    //   471: if_icmpge -> 906
    //   474: aload #12
    //   476: iload #7
    //   478: aaload
    //   479: astore #26
    //   481: new java/lang/StringBuilder
    //   484: dup
    //   485: invokespecial <init> : ()V
    //   488: astore #23
    //   490: aload #23
    //   492: aload_2
    //   493: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   496: pop
    //   497: aload #23
    //   499: aload #26
    //   501: invokestatic getSignature : (Ljava/lang/Class;)Ljava/lang/String;
    //   504: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   507: pop
    //   508: aload #23
    //   510: invokevirtual toString : ()Ljava/lang/String;
    //   513: astore #23
    //   515: aload #26
    //   517: aload #5
    //   519: iload #6
    //   521: invokestatic getConversion : (Ljava/lang/Class;Lcom/sun/jna/TypeMapper;Z)I
    //   524: istore #27
    //   526: aload #15
    //   528: iload #7
    //   530: iload #27
    //   532: iastore
    //   533: iload #27
    //   535: iconst_m1
    //   536: if_icmpeq -> 839
    //   539: iload #27
    //   541: bipush #17
    //   543: if_icmpeq -> 614
    //   546: iload #27
    //   548: bipush #18
    //   550: if_icmpeq -> 614
    //   553: iload #27
    //   555: bipush #19
    //   557: if_icmpeq -> 614
    //   560: iload #27
    //   562: bipush #21
    //   564: if_icmpne -> 570
    //   567: goto -> 614
    //   570: iload #27
    //   572: bipush #23
    //   574: if_icmpeq -> 594
    //   577: iload #27
    //   579: bipush #24
    //   581: if_icmpeq -> 594
    //   584: aload #26
    //   586: astore_2
    //   587: iload #27
    //   589: bipush #25
    //   591: if_icmpne -> 623
    //   594: aload #16
    //   596: iload #7
    //   598: aload #5
    //   600: aload #26
    //   602: invokeinterface getToNativeConverter : (Ljava/lang/Class;)Lcom/sun/jna/ToNativeConverter;
    //   607: aastore
    //   608: aload #26
    //   610: astore_2
    //   611: goto -> 623
    //   614: aload #26
    //   616: invokestatic getInstance : (Ljava/lang/Class;)Lcom/sun/jna/NativeMappedConverter;
    //   619: invokevirtual nativeType : ()Ljava/lang/Class;
    //   622: astore_2
    //   623: iload #27
    //   625: ifeq -> 807
    //   628: iload #27
    //   630: iconst_4
    //   631: if_icmpeq -> 778
    //   634: iload #27
    //   636: tableswitch default -> 664, 17 -> 778, 18 -> 778, 19 -> 778
    //   664: iload #27
    //   666: tableswitch default -> 700, 21 -> 778, 22 -> 778, 23 -> 728, 24 -> 728, 25 -> 728
    //   700: ldc_w com/sun/jna/Pointer
    //   703: invokestatic get : (Ljava/lang/Object;)Lcom/sun/jna/Pointer;
    //   706: getfield peer : J
    //   709: lstore #24
    //   711: aload #13
    //   713: iload #7
    //   715: lload #24
    //   717: lastore
    //   718: aload #14
    //   720: iload #7
    //   722: lload #24
    //   724: lastore
    //   725: goto -> 830
    //   728: aload_2
    //   729: invokevirtual isPrimitive : ()Z
    //   732: ifeq -> 738
    //   735: goto -> 742
    //   738: ldc_w com/sun/jna/Pointer
    //   741: astore_2
    //   742: aload #14
    //   744: iload #7
    //   746: aload_2
    //   747: invokestatic get : (Ljava/lang/Object;)Lcom/sun/jna/Pointer;
    //   750: getfield peer : J
    //   753: lastore
    //   754: aload #13
    //   756: iload #7
    //   758: aload #16
    //   760: iload #7
    //   762: aaload
    //   763: invokeinterface nativeType : ()Ljava/lang/Class;
    //   768: invokestatic get : (Ljava/lang/Object;)Lcom/sun/jna/Pointer;
    //   771: getfield peer : J
    //   774: lastore
    //   775: goto -> 830
    //   778: aload #13
    //   780: iload #7
    //   782: aload_2
    //   783: invokestatic get : (Ljava/lang/Object;)Lcom/sun/jna/Pointer;
    //   786: getfield peer : J
    //   789: lastore
    //   790: aload #14
    //   792: iload #7
    //   794: ldc_w com/sun/jna/Pointer
    //   797: invokestatic get : (Ljava/lang/Object;)Lcom/sun/jna/Pointer;
    //   800: getfield peer : J
    //   803: lastore
    //   804: goto -> 830
    //   807: aload_2
    //   808: invokestatic get : (Ljava/lang/Object;)Lcom/sun/jna/Pointer;
    //   811: getfield peer : J
    //   814: lstore #24
    //   816: aload #13
    //   818: iload #7
    //   820: lload #24
    //   822: lastore
    //   823: aload #14
    //   825: iload #7
    //   827: lload #24
    //   829: lastore
    //   830: iinc #7, 1
    //   833: aload #23
    //   835: astore_2
    //   836: goto -> 466
    //   839: new java/lang/StringBuilder
    //   842: dup
    //   843: invokespecial <init> : ()V
    //   846: astore_0
    //   847: aload_0
    //   848: aload #26
    //   850: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   853: pop
    //   854: aload_0
    //   855: ldc_w ' is not a supported argument type (in method '
    //   858: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   861: pop
    //   862: aload_0
    //   863: aload #10
    //   865: invokevirtual getName : ()Ljava/lang/String;
    //   868: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   871: pop
    //   872: aload_0
    //   873: ldc_w ' in '
    //   876: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   879: pop
    //   880: aload_0
    //   881: aload #9
    //   883: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   886: pop
    //   887: aload_0
    //   888: ldc ')'
    //   890: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   893: pop
    //   894: new java/lang/IllegalArgumentException
    //   897: dup
    //   898: aload_0
    //   899: invokevirtual toString : ()Ljava/lang/String;
    //   902: invokespecial <init> : (Ljava/lang/String;)V
    //   905: athrow
    //   906: new java/lang/StringBuilder
    //   909: dup
    //   910: invokespecial <init> : ()V
    //   913: astore #12
    //   915: aload #12
    //   917: aload_2
    //   918: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   921: pop
    //   922: aload #12
    //   924: ldc ')'
    //   926: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   929: pop
    //   930: aload #12
    //   932: invokevirtual toString : ()Ljava/lang/String;
    //   935: astore_2
    //   936: new java/lang/StringBuilder
    //   939: dup
    //   940: invokespecial <init> : ()V
    //   943: astore #12
    //   945: aload #12
    //   947: aload_2
    //   948: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   951: pop
    //   952: aload #12
    //   954: aload #11
    //   956: invokestatic getSignature : (Ljava/lang/Class;)Ljava/lang/String;
    //   959: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   962: pop
    //   963: aload #12
    //   965: invokevirtual toString : ()Ljava/lang/String;
    //   968: astore_2
    //   969: aload #10
    //   971: invokevirtual getExceptionTypes : ()[Ljava/lang/Class;
    //   974: astore #12
    //   976: iconst_0
    //   977: istore #7
    //   979: iload #7
    //   981: aload #12
    //   983: arraylength
    //   984: if_icmpge -> 1013
    //   987: ldc_w com/sun/jna/LastErrorException
    //   990: aload #12
    //   992: iload #7
    //   994: aaload
    //   995: invokevirtual isAssignableFrom : (Ljava/lang/Class;)Z
    //   998: ifeq -> 1007
    //   1001: iconst_1
    //   1002: istore #28
    //   1004: goto -> 1016
    //   1007: iinc #7, 1
    //   1010: goto -> 979
    //   1013: iconst_0
    //   1014: istore #28
    //   1016: aload_1
    //   1017: aload #10
    //   1019: invokevirtual getName : ()Ljava/lang/String;
    //   1022: aload #10
    //   1024: invokevirtual getFunction : (Ljava/lang/String;Ljava/lang/reflect/Method;)Lcom/sun/jna/Function;
    //   1027: astore #11
    //   1029: aload #10
    //   1031: invokevirtual getName : ()Ljava/lang/String;
    //   1034: astore #12
    //   1036: aload #11
    //   1038: getfield peer : J
    //   1041: lstore #24
    //   1043: aload #11
    //   1045: invokevirtual getCallingConvention : ()I
    //   1048: istore #7
    //   1050: aload #11
    //   1052: getfield encoding : Ljava/lang/String;
    //   1055: astore #11
    //   1057: aload #4
    //   1059: iload #8
    //   1061: aload_0
    //   1062: aload #12
    //   1064: aload_2
    //   1065: aload #15
    //   1067: aload #14
    //   1069: aload #13
    //   1071: iload #17
    //   1073: lload #18
    //   1075: lload #20
    //   1077: aload #10
    //   1079: lload #24
    //   1081: iload #7
    //   1083: iload #28
    //   1085: aload #16
    //   1087: aload #22
    //   1089: aload #11
    //   1091: invokestatic registerMethod : (Ljava/lang/Class;Ljava/lang/String;Ljava/lang/String;[I[J[JIJJLjava/lang/reflect/Method;JIZ[Lcom/sun/jna/ToNativeConverter;Lcom/sun/jna/FromNativeConverter;Ljava/lang/String;)J
    //   1094: lastore
    //   1095: iinc #8, 1
    //   1098: goto -> 120
    //   1101: astore_1
    //   1102: new java/lang/StringBuilder
    //   1105: dup
    //   1106: invokespecial <init> : ()V
    //   1109: astore_1
    //   1110: aload_1
    //   1111: ldc_w 'No method '
    //   1114: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1117: pop
    //   1118: aload_1
    //   1119: aload #10
    //   1121: invokevirtual getName : ()Ljava/lang/String;
    //   1124: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1127: pop
    //   1128: aload_1
    //   1129: ldc_w ' with signature '
    //   1132: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1135: pop
    //   1136: aload_1
    //   1137: aload_2
    //   1138: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1141: pop
    //   1142: aload_1
    //   1143: ldc_w ' in '
    //   1146: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1149: pop
    //   1150: aload_1
    //   1151: aload_0
    //   1152: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   1155: pop
    //   1156: new java/lang/UnsatisfiedLinkError
    //   1159: dup
    //   1160: aload_1
    //   1161: invokevirtual toString : ()Ljava/lang/String;
    //   1164: invokespecial <init> : (Ljava/lang/String;)V
    //   1167: athrow
    //   1168: new java/lang/StringBuilder
    //   1171: dup
    //   1172: invokespecial <init> : ()V
    //   1175: astore_0
    //   1176: aload_0
    //   1177: aload #11
    //   1179: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   1182: pop
    //   1183: aload_0
    //   1184: ldc_w ' is not a supported return type (in method '
    //   1187: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1190: pop
    //   1191: aload_0
    //   1192: aload #10
    //   1194: invokevirtual getName : ()Ljava/lang/String;
    //   1197: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1200: pop
    //   1201: aload_0
    //   1202: ldc_w ' in '
    //   1205: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1208: pop
    //   1209: aload_0
    //   1210: aload #9
    //   1212: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   1215: pop
    //   1216: aload_0
    //   1217: ldc ')'
    //   1219: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1222: pop
    //   1223: new java/lang/IllegalArgumentException
    //   1226: dup
    //   1227: aload_0
    //   1228: invokevirtual toString : ()Ljava/lang/String;
    //   1231: invokespecial <init> : (Ljava/lang/String;)V
    //   1234: athrow
    //   1235: getstatic com/sun/jna/Native.registeredClasses : Ljava/util/Map;
    //   1238: astore_0
    //   1239: aload_0
    //   1240: monitorenter
    //   1241: getstatic com/sun/jna/Native.registeredClasses : Ljava/util/Map;
    //   1244: aload #9
    //   1246: aload #4
    //   1248: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   1253: pop
    //   1254: getstatic com/sun/jna/Native.registeredLibraries : Ljava/util/Map;
    //   1257: aload #9
    //   1259: aload_1
    //   1260: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   1265: pop
    //   1266: aload_0
    //   1267: monitorexit
    //   1268: return
    //   1269: astore_1
    //   1270: aload_0
    //   1271: monitorexit
    //   1272: goto -> 1277
    //   1275: aload_1
    //   1276: athrow
    //   1277: goto -> 1275
    //   1280: astore_1
    //   1281: goto -> 1102
    // Exception table:
    //   from	to	target	type
    //   1029	1057	1101	java/lang/NoSuchMethodError
    //   1057	1095	1280	java/lang/NoSuchMethodError
    //   1241	1268	1269	finally
    //   1270	1272	1269	finally
  }
  
  public static void register(Class<?> paramClass, String paramString) {
    register(paramClass, NativeLibrary.getInstance(paramString, Collections.singletonMap("classloader", paramClass.getClassLoader())));
  }
  
  public static void register(String paramString) {
    register(findDirectMappedClass(getCallingClass()), paramString);
  }
  
  private static native long registerMethod(Class<?> paramClass, String paramString1, String paramString2, int[] paramArrayOfint, long[] paramArrayOflong1, long[] paramArrayOflong2, int paramInt1, long paramLong1, long paramLong2, Method paramMethod, long paramLong3, int paramInt2, boolean paramBoolean, ToNativeConverter[] paramArrayOfToNativeConverter, FromNativeConverter paramFromNativeConverter, String paramString3);
  
  public static boolean registered(Class<?> paramClass) {
    synchronized (registeredClasses) {
      return registeredClasses.containsKey(paramClass);
    } 
  }
  
  static void removeTemporaryFiles() throws IOException {
    File[] arrayOfFile = getTempDir().listFiles(new FilenameFilter() {
          public boolean accept(File param1File, String param1String) {
            boolean bool;
            if (param1String.endsWith(".x") && param1String.startsWith("jna")) {
              bool = true;
            } else {
              bool = false;
            } 
            return bool;
          }
        });
    for (byte b = 0; arrayOfFile != null && b < arrayOfFile.length; b++) {
      File file1 = arrayOfFile[b];
      String str = file1.getName();
      str = str.substring(0, str.length() - 2);
      File file2 = new File(file1.getParentFile(), str);
      if (!file2.exists() || file2.delete())
        file1.delete(); 
    } 
  }
  
  static String replace(String paramString1, String paramString2, String paramString3) {
    StringBuilder stringBuilder = new StringBuilder();
    while (true) {
      int i = paramString3.indexOf(paramString1);
      if (i == -1) {
        stringBuilder.append(paramString3);
        return stringBuilder.toString();
      } 
      stringBuilder.append(paramString3.substring(0, i));
      stringBuilder.append(paramString2);
      paramString3 = paramString3.substring(i + paramString1.length());
    } 
  }
  
  static native void setByte(Pointer paramPointer, long paramLong1, long paramLong2, byte paramByte);
  
  public static void setCallbackExceptionHandler(Callback.UncaughtExceptionHandler paramUncaughtExceptionHandler) {
    Callback.UncaughtExceptionHandler uncaughtExceptionHandler = paramUncaughtExceptionHandler;
    if (paramUncaughtExceptionHandler == null)
      uncaughtExceptionHandler = DEFAULT_HANDLER; 
    callbackExceptionHandler = uncaughtExceptionHandler;
  }
  
  public static void setCallbackThreadInitializer(Callback paramCallback, CallbackThreadInitializer paramCallbackThreadInitializer) {
    CallbackReference.setCallbackThreadInitializer(paramCallback, paramCallbackThreadInitializer);
  }
  
  static native void setChar(Pointer paramPointer, long paramLong1, long paramLong2, char paramChar);
  
  private static native void setDetachState(boolean paramBoolean, long paramLong);
  
  static native void setDouble(Pointer paramPointer, long paramLong1, long paramLong2, double paramDouble);
  
  static native void setFloat(Pointer paramPointer, long paramLong1, long paramLong2, float paramFloat);
  
  static native void setInt(Pointer paramPointer, long paramLong1, long paramLong2, int paramInt);
  
  public static native void setLastError(int paramInt);
  
  static native void setLong(Pointer paramPointer, long paramLong1, long paramLong2, long paramLong3);
  
  static native void setMemory(Pointer paramPointer, long paramLong1, long paramLong2, long paramLong3, byte paramByte);
  
  static native void setPointer(Pointer paramPointer, long paramLong1, long paramLong2, long paramLong3);
  
  public static synchronized native void setProtected(boolean paramBoolean);
  
  static native void setShort(Pointer paramPointer, long paramLong1, long paramLong2, short paramShort);
  
  static native void setWideString(Pointer paramPointer, long paramLong1, long paramLong2, String paramString);
  
  private static native int sizeof(int paramInt);
  
  public static Library synchronizedLibrary(final Library library) {
    Class<?> clazz = library.getClass();
    if (Proxy.isProxyClass(clazz)) {
      final InvocationHandler handler = Proxy.getInvocationHandler(library);
      if (invocationHandler instanceof Library.Handler) {
        InvocationHandler invocationHandler1 = new InvocationHandler() {
            public Object invoke(Object param1Object, Method param1Method, Object[] param1ArrayOfObject) throws Throwable {
              synchronized (handler.getNativeLibrary()) {
                return handler.invoke(library, param1Method, param1ArrayOfObject);
              } 
            }
          };
        return (Library)Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), invocationHandler1);
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Unrecognized proxy handler: ");
      stringBuilder.append(invocationHandler);
      throw new IllegalArgumentException(stringBuilder.toString());
    } 
    throw new IllegalArgumentException("Library must be a proxy class");
  }
  
  public static byte[] toByteArray(String paramString) {
    return toByteArray(paramString, getDefaultStringEncoding());
  }
  
  public static byte[] toByteArray(String paramString1, String paramString2) {
    return toByteArray(paramString1, getCharset(paramString2));
  }
  
  public static byte[] toByteArray(String paramString, Charset paramCharset) {
    byte[] arrayOfByte2 = getBytes(paramString, paramCharset);
    byte[] arrayOfByte1 = new byte[arrayOfByte2.length + 1];
    System.arraycopy(arrayOfByte2, 0, arrayOfByte1, 0, arrayOfByte2.length);
    return arrayOfByte1;
  }
  
  public static char[] toCharArray(String paramString) {
    char[] arrayOfChar1 = paramString.toCharArray();
    char[] arrayOfChar2 = new char[arrayOfChar1.length + 1];
    System.arraycopy(arrayOfChar1, 0, arrayOfChar2, 0, arrayOfChar1.length);
    return arrayOfChar2;
  }
  
  private static Object toNative(ToNativeConverter paramToNativeConverter, Object paramObject) {
    return paramToNativeConverter.toNative(paramObject, new ToNativeContext());
  }
  
  public static String toString(byte[] paramArrayOfbyte) {
    return toString(paramArrayOfbyte, getDefaultStringEncoding());
  }
  
  public static String toString(byte[] paramArrayOfbyte, String paramString) {
    return toString(paramArrayOfbyte, getCharset(paramString));
  }
  
  public static String toString(byte[] paramArrayOfbyte, Charset paramCharset) {
    int j;
    int i = paramArrayOfbyte.length;
    byte b = 0;
    while (true) {
      j = i;
      if (b < i) {
        if (paramArrayOfbyte[b] == 0) {
          j = b;
          break;
        } 
        b++;
        continue;
      } 
      break;
    } 
    return (j == 0) ? "" : new String(paramArrayOfbyte, 0, j, paramCharset);
  }
  
  public static String toString(char[] paramArrayOfchar) {
    int j;
    int i = paramArrayOfchar.length;
    byte b = 0;
    while (true) {
      j = i;
      if (b < i) {
        if (paramArrayOfchar[b] == '\000') {
          j = b;
          break;
        } 
        b++;
        continue;
      } 
      break;
    } 
    return (j == 0) ? "" : new String(paramArrayOfchar, 0, j);
  }
  
  public static List<String> toStringList(char[] paramArrayOfchar) {
    return toStringList(paramArrayOfchar, 0, paramArrayOfchar.length);
  }
  
  public static List<String> toStringList(char[] paramArrayOfchar, int paramInt1, int paramInt2) {
    ArrayList<String> arrayList = new ArrayList();
    int i = paramInt2 + paramInt1;
    paramInt2 = paramInt1;
    while (paramInt1 < i) {
      if (paramArrayOfchar[paramInt1] == '\000') {
        if (paramInt2 == paramInt1)
          return arrayList; 
        arrayList.add(new String(paramArrayOfchar, paramInt2, paramInt1 - paramInt2));
        paramInt2 = paramInt1 + 1;
      } 
      paramInt1++;
    } 
    if (paramInt2 < i)
      arrayList.add(new String(paramArrayOfchar, paramInt2, i - paramInt2)); 
    return arrayList;
  }
  
  public static void unregister() {
    unregister(findDirectMappedClass(getCallingClass()));
  }
  
  public static void unregister(Class<?> paramClass) {
    synchronized (registeredClasses) {
      long[] arrayOfLong = registeredClasses.get(paramClass);
      if (arrayOfLong != null) {
        unregister(paramClass, arrayOfLong);
        registeredClasses.remove(paramClass);
        registeredLibraries.remove(paramClass);
      } 
      return;
    } 
  }
  
  private static native void unregister(Class<?> paramClass, long[] paramArrayOflong);
  
  private static void unregisterAll() {
    synchronized (registeredClasses) {
      for (Map.Entry<Class<?>, long> entry : registeredClasses.entrySet())
        unregister((Class)entry.getKey(), (long[])entry.getValue()); 
      registeredClasses.clear();
      return;
    } 
  }
  
  static native void write(Pointer paramPointer, long paramLong1, long paramLong2, byte[] paramArrayOfbyte, int paramInt1, int paramInt2);
  
  static native void write(Pointer paramPointer, long paramLong1, long paramLong2, char[] paramArrayOfchar, int paramInt1, int paramInt2);
  
  static native void write(Pointer paramPointer, long paramLong1, long paramLong2, double[] paramArrayOfdouble, int paramInt1, int paramInt2);
  
  static native void write(Pointer paramPointer, long paramLong1, long paramLong2, float[] paramArrayOffloat, int paramInt1, int paramInt2);
  
  static native void write(Pointer paramPointer, long paramLong1, long paramLong2, int[] paramArrayOfint, int paramInt1, int paramInt2);
  
  static native void write(Pointer paramPointer, long paramLong1, long paramLong2, long[] paramArrayOflong, int paramInt1, int paramInt2);
  
  static native void write(Pointer paramPointer, long paramLong1, long paramLong2, short[] paramArrayOfshort, int paramInt1, int paramInt2);
  
  static {
    Level level;
    int i;
  }
  
  public static final int BOOL_SIZE;
  
  static final int CB_HAS_INITIALIZER = 1;
  
  static final int CB_OPTION_DIRECT = 1;
  
  static final int CB_OPTION_IN_DLL = 2;
  
  private static final int CVT_ARRAY_BOOLEAN = 13;
  
  private static final int CVT_ARRAY_BYTE = 6;
  
  private static final int CVT_ARRAY_CHAR = 8;
  
  private static final int CVT_ARRAY_DOUBLE = 12;
  
  private static final int CVT_ARRAY_FLOAT = 11;
  
  private static final int CVT_ARRAY_INT = 9;
  
  private static final int CVT_ARRAY_LONG = 10;
  
  private static final int CVT_ARRAY_SHORT = 7;
  
  private static final int CVT_BOOLEAN = 14;
  
  private static final int CVT_BUFFER = 5;
  
  private static final int CVT_CALLBACK = 15;
  
  private static final int CVT_DEFAULT = 0;
  
  private static final int CVT_FLOAT = 16;
  
  private static final int CVT_INTEGER_TYPE = 21;
  
  private static final int CVT_JNIENV = 27;
  
  private static final int CVT_NATIVE_MAPPED = 17;
  
  private static final int CVT_NATIVE_MAPPED_STRING = 18;
  
  private static final int CVT_NATIVE_MAPPED_WSTRING = 19;
  
  private static final int CVT_OBJECT = 26;
  
  private static final int CVT_POINTER = 1;
  
  private static final int CVT_POINTER_TYPE = 22;
  
  private static final int CVT_STRING = 2;
  
  private static final int CVT_STRUCTURE = 3;
  
  private static final int CVT_STRUCTURE_BYVAL = 4;
  
  private static final int CVT_TYPE_MAPPER = 23;
  
  private static final int CVT_TYPE_MAPPER_STRING = 24;
  
  private static final int CVT_TYPE_MAPPER_WSTRING = 25;
  
  private static final int CVT_UNSUPPORTED = -1;
  
  private static final int CVT_WSTRING = 20;
  
  public static final boolean DEBUG_JNA_LOAD;
  
  private static final Level DEBUG_JNA_LOAD_LEVEL;
  
  public static final boolean DEBUG_LOAD;
  
  public static final Charset DEFAULT_CHARSET;
  
  public static final String DEFAULT_ENCODING;
  
  private static final Callback.UncaughtExceptionHandler DEFAULT_HANDLER;
  
  static final String JNA_TMPLIB_PREFIX = "jna";
  
  private static final Logger LOG = Logger.getLogger(Native.class.getName());
  
  public static final int LONG_DOUBLE_SIZE;
  
  public static final int LONG_SIZE;
  
  static final int MAX_ALIGNMENT;
  
  static final int MAX_PADDING;
  
  public static final int POINTER_SIZE;
  
  public static final int SIZE_T_SIZE;
  
  private static final int TYPE_BOOL = 4;
  
  private static final int TYPE_LONG = 1;
  
  private static final int TYPE_LONG_DOUBLE = 5;
  
  private static final int TYPE_SIZE_T = 3;
  
  private static final int TYPE_VOIDP = 0;
  
  private static final int TYPE_WCHAR_T = 2;
  
  public static final int WCHAR_SIZE;
  
  private static final String _OPTION_ENCLOSING_LIBRARY = "enclosing-library";
  
  private static Callback.UncaughtExceptionHandler callbackExceptionHandler;
  
  private static final Object finalizer;
  
  static String jnidispatchPath;
  
  private static final Map<Class<?>, Reference<?>> libraries;
  
  private static final ThreadLocal<Memory> nativeThreadTerminationFlag;
  
  private static final Map<Thread, Pointer> nativeThreads;
  
  private static final Map<Class<?>, long[]> registeredClasses;
  
  private static final Map<Class<?>, NativeLibrary> registeredLibraries;
  
  private static final Map<Class<?>, Map<String, Object>> typeOptions;
  
  private static class AWT {
    static long getComponentID(Object param1Object) throws HeadlessException {
      if (!GraphicsEnvironment.isHeadless()) {
        param1Object = param1Object;
        if (!param1Object.isLightweight()) {
          if (param1Object.isDisplayable()) {
            if (!Platform.isX11() || !System.getProperty("java.version").startsWith("1.4") || param1Object.isVisible())
              return Native.getWindowHandle0((Component)param1Object); 
            throw new IllegalStateException("Component must be visible");
          } 
          throw new IllegalStateException("Component must be displayable");
        } 
        throw new IllegalArgumentException("Component must be heavyweight");
      } 
      throw new HeadlessException("No native windows when headless");
    }
    
    static long getWindowID(Window param1Window) throws HeadlessException {
      return getComponentID(param1Window);
    }
  }
  
  private static class Buffers {
    static boolean isBuffer(Class<?> param1Class) {
      return Buffer.class.isAssignableFrom(param1Class);
    }
  }
  
  public static interface ffi_callback {
    void invoke(long param1Long1, long param1Long2, long param1Long3);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/sun/jna/Native.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */