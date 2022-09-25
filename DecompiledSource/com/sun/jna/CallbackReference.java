package com.sun.jna;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class CallbackReference extends WeakReference<Callback> {
  private static final Method PROXY_CALLBACK_METHOD;
  
  private static final Map<CallbackReference, Reference<CallbackReference>> allocatedMemory;
  
  static final Map<Object, Object> allocations;
  
  static final Map<Callback, CallbackReference> callbackMap = new WeakHashMap<Callback, CallbackReference>();
  
  static final Map<Callback, CallbackReference> directCallbackMap = new WeakHashMap<Callback, CallbackReference>();
  
  private static final Map<Callback, CallbackThreadInitializer> initializers;
  
  static final Map<Pointer, Reference<Callback>> pointerCallbackMap = new WeakHashMap<Pointer, Reference<Callback>>();
  
  int callingConvention;
  
  Pointer cbstruct;
  
  Method method;
  
  CallbackProxy proxy;
  
  Pointer trampoline;
  
  static {
    allocations = new WeakHashMap<Object, Object>();
    allocatedMemory = Collections.synchronizedMap(new WeakHashMap<CallbackReference, Reference<CallbackReference>>());
    try {
      PROXY_CALLBACK_METHOD = CallbackProxy.class.getMethod("callback", new Class[] { Object[].class });
      initializers = new WeakHashMap<Callback, CallbackThreadInitializer>();
      return;
    } catch (Exception exception) {
      throw new Error("Error looking up CallbackProxy.callback() method");
    } 
  }
  
  private CallbackReference(Callback paramCallback, int paramInt, boolean paramBoolean) {
    super(paramCallback);
    long l;
    TypeMapper typeMapper = Native.getTypeMapper(paramCallback.getClass());
    this.callingConvention = paramInt;
    boolean bool1 = Platform.isPPC();
    boolean bool2 = paramBoolean;
    if (paramBoolean) {
      boolean bool;
      Method method = getCallbackMethod(paramCallback);
      Class[] arrayOfClass = method.getParameterTypes();
      byte b = 0;
      while (true) {
        bool = paramBoolean;
        if (b < arrayOfClass.length) {
          if ((bool1 && (arrayOfClass[b] == float.class || arrayOfClass[b] == double.class)) || (typeMapper != null && typeMapper.getFromNativeConverter(arrayOfClass[b]) != null)) {
            bool = false;
            break;
          } 
          b++;
          continue;
        } 
        break;
      } 
      bool2 = bool;
      if (typeMapper != null) {
        bool2 = bool;
        if (typeMapper.getToNativeConverter(method.getReturnType()) != null)
          bool2 = false; 
      } 
    } 
    String str = Native.getStringEncoding(paramCallback.getClass());
    if (bool2) {
      boolean bool;
      this.method = getCallbackMethod(paramCallback);
      Class[] arrayOfClass = this.method.getParameterTypes();
      Class<?> clazz = this.method.getReturnType();
      if (paramCallback instanceof com.sun.jna.win32.DLLCallback) {
        bool = true;
      } else {
        bool = true;
      } 
      l = Native.createNativeCallback(paramCallback, this.method, arrayOfClass, clazz, paramInt, bool, str);
    } else {
      StringBuilder stringBuilder;
      if (paramCallback instanceof CallbackProxy) {
        this.proxy = (CallbackProxy)paramCallback;
      } else {
        this.proxy = new DefaultCallbackProxy(getCallbackMethod(paramCallback), typeMapper, str);
      } 
      Class[] arrayOfClass = this.proxy.getParameterTypes();
      Class<?> clazz1 = this.proxy.getReturnType();
      Class<?> clazz2 = clazz1;
      if (typeMapper != null) {
        for (byte b1 = 0; b1 < arrayOfClass.length; b1++) {
          FromNativeConverter fromNativeConverter = typeMapper.getFromNativeConverter(arrayOfClass[b1]);
          if (fromNativeConverter != null)
            arrayOfClass[b1] = fromNativeConverter.nativeType(); 
        } 
        ToNativeConverter toNativeConverter = typeMapper.getToNativeConverter(clazz1);
        clazz2 = clazz1;
        if (toNativeConverter != null)
          clazz2 = toNativeConverter.nativeType(); 
      } 
      byte b = 0;
      while (b < arrayOfClass.length) {
        arrayOfClass[b] = getNativeType(arrayOfClass[b]);
        if (isAllowableNativeType(arrayOfClass[b])) {
          b++;
          continue;
        } 
        stringBuilder = new StringBuilder();
        stringBuilder.append("Callback argument ");
        stringBuilder.append(arrayOfClass[b]);
        stringBuilder.append(" requires custom type conversion");
        throw new IllegalArgumentException(stringBuilder.toString());
      } 
      clazz2 = getNativeType(clazz2);
      if (isAllowableNativeType(clazz2)) {
        if (stringBuilder instanceof com.sun.jna.win32.DLLCallback) {
          b = 2;
        } else {
          b = 0;
        } 
        l = Native.createNativeCallback(this.proxy, PROXY_CALLBACK_METHOD, arrayOfClass, clazz2, paramInt, b, str);
      } else {
        stringBuilder = new StringBuilder();
        stringBuilder.append("Callback return type ");
        stringBuilder.append(clazz2);
        stringBuilder.append(" requires custom type conversion");
        IllegalArgumentException illegalArgumentException = new IllegalArgumentException(stringBuilder.toString());
        throw illegalArgumentException;
      } 
    } 
    if (l != 0L) {
      Pointer pointer = new Pointer(l);
    } else {
      paramCallback = null;
    } 
    this.cbstruct = (Pointer)paramCallback;
    allocatedMemory.put(this, new WeakReference<CallbackReference>(this));
  }
  
  private static Method checkMethod(Method paramMethod) {
    if ((paramMethod.getParameterTypes()).length <= 256)
      return paramMethod; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Method signature exceeds the maximum parameter count: ");
    stringBuilder.append(paramMethod);
    throw new UnsupportedOperationException(stringBuilder.toString());
  }
  
  static void disposeAll() {
    Iterator<?> iterator = (new LinkedList(allocatedMemory.keySet())).iterator();
    while (iterator.hasNext())
      ((CallbackReference)iterator.next()).dispose(); 
  }
  
  static Class<?> findCallbackClass(Class<?> paramClass) {
    if (Callback.class.isAssignableFrom(paramClass)) {
      if (paramClass.isInterface())
        return paramClass; 
      Class[] arrayOfClass = paramClass.getInterfaces();
      for (byte b = 0; b < arrayOfClass.length; b++) {
        if (Callback.class.isAssignableFrom(arrayOfClass[b]))
          try {
            getCallbackMethod(arrayOfClass[b]);
            return arrayOfClass[b];
          } catch (IllegalArgumentException illegalArgumentException1) {
            break;
          }  
      } 
      Class<?> clazz = paramClass;
      if (Callback.class.isAssignableFrom(paramClass.getSuperclass()))
        clazz = findCallbackClass(paramClass.getSuperclass()); 
      return clazz;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(paramClass.getName());
    stringBuilder.append(" is not derived from com.sun.jna.Callback");
    IllegalArgumentException illegalArgumentException = new IllegalArgumentException(stringBuilder.toString());
    throw illegalArgumentException;
  }
  
  private Callback getCallback() {
    return get();
  }
  
  public static Callback getCallback(Class<?> paramClass, Pointer paramPointer) {
    return getCallback(paramClass, paramPointer, false);
  }
  
  private static Callback getCallback(Class<?> paramClass, Pointer paramPointer, boolean paramBoolean) {
    if (paramPointer == null)
      return null; 
    if (paramClass.isInterface()) {
      if (paramBoolean) {
        Map<Callback, CallbackReference> map = directCallbackMap;
      } else {
        Map<Callback, CallbackReference> map = callbackMap;
      } 
      synchronized (pointerCallbackMap) {
        IllegalStateException illegalStateException;
        Callback callback2;
        boolean bool;
        Reference<Callback> reference = pointerCallbackMap.get(paramPointer);
        if (reference != null) {
          callback2 = reference.get();
          if (callback2 == null || paramClass.isAssignableFrom(callback2.getClass()))
            return callback2; 
          illegalStateException = new IllegalStateException();
          StringBuilder stringBuilder = new StringBuilder();
          this();
          stringBuilder.append("Pointer ");
          stringBuilder.append(paramPointer);
          stringBuilder.append(" already mapped to ");
          stringBuilder.append(callback2);
          stringBuilder.append(".\nNative code may be re-using a default function pointer, in which case you may need to use a common Callback class wherever the function pointer is reused.");
          this(stringBuilder.toString());
          throw illegalStateException;
        } 
        if (AltCallingConvention.class.isAssignableFrom((Class<?>)illegalStateException)) {
          bool = true;
        } else {
          bool = false;
        } 
        HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
        this((Map)Native.getLibraryOptions((Class<?>)illegalStateException));
        hashMap.put("invoking-method", getCallbackMethod((Class<?>)illegalStateException));
        NativeFunctionHandler nativeFunctionHandler = new NativeFunctionHandler();
        this(paramPointer, bool, (Map)hashMap);
        Callback callback1 = (Callback)Proxy.newProxyInstance(illegalStateException.getClassLoader(), new Class[] { (Class)illegalStateException }, nativeFunctionHandler);
        callback2.remove(callback1);
        Map<Pointer, Reference<Callback>> map = pointerCallbackMap;
        WeakReference<Callback> weakReference = new WeakReference();
        this((T)callback1);
        map.put(paramPointer, weakReference);
        return callback1;
      } 
    } 
    throw new IllegalArgumentException("Callback type must be an interface");
  }
  
  private static Method getCallbackMethod(Callback paramCallback) {
    return getCallbackMethod(findCallbackClass(paramCallback.getClass()));
  }
  
  private static Method getCallbackMethod(Class<?> paramClass) {
    Method[] arrayOfMethod2 = paramClass.getDeclaredMethods();
    Method[] arrayOfMethod3 = paramClass.getMethods();
    HashSet hashSet = new HashSet(Arrays.asList((Object[])arrayOfMethod2));
    hashSet.retainAll(Arrays.asList((Object[])arrayOfMethod3));
    Iterator<Method> iterator = hashSet.iterator();
    while (iterator.hasNext()) {
      Method method = iterator.next();
      if (Callback.FORBIDDEN_NAMES.contains(method.getName()))
        iterator.remove(); 
    } 
    byte b = 0;
    Method[] arrayOfMethod1 = (Method[])hashSet.toArray((Object[])new Method[0]);
    if (arrayOfMethod1.length == 1)
      return checkMethod(arrayOfMethod1[0]); 
    while (b < arrayOfMethod1.length) {
      Method method = arrayOfMethod1[b];
      if ("callback".equals(method.getName()))
        return checkMethod(method); 
      b++;
    } 
    IllegalArgumentException illegalArgumentException = new IllegalArgumentException("Callback must implement a single public method, or one public method named 'callback'");
    throw illegalArgumentException;
  }
  
  public static Pointer getFunctionPointer(Callback paramCallback) {
    return getFunctionPointer(paramCallback, false);
  }
  
  private static Pointer getFunctionPointer(Callback paramCallback, boolean paramBoolean) {
    boolean bool;
    Map<Callback, CallbackReference> map1;
    if (paramCallback == null)
      return null; 
    Pointer pointer = getNativeFunctionPointer(paramCallback);
    if (pointer != null)
      return pointer; 
    Map<String, Object> map = Native.getLibraryOptions(paramCallback.getClass());
    if (paramCallback instanceof AltCallingConvention) {
      bool = true;
    } else if (map != null && map.containsKey("calling-convention")) {
      bool = ((Integer)map.get("calling-convention")).intValue();
    } else {
      bool = false;
    } 
    if (paramBoolean) {
      map1 = directCallbackMap;
    } else {
      map1 = callbackMap;
    } 
    synchronized (pointerCallbackMap) {
      CallbackReference callbackReference2 = map1.get(paramCallback);
      CallbackReference callbackReference1 = callbackReference2;
      if (callbackReference2 == null) {
        callbackReference2 = new CallbackReference();
        this(paramCallback, bool, paramBoolean);
        map1.put(paramCallback, callbackReference2);
        Map<Pointer, Reference<Callback>> map2 = pointerCallbackMap;
        Pointer pointer1 = callbackReference2.getTrampoline();
        WeakReference<Callback> weakReference = new WeakReference();
        this((T)paramCallback);
        map2.put(pointer1, weakReference);
        callbackReference1 = callbackReference2;
        if (initializers.containsKey(paramCallback)) {
          callbackReference2.setCallbackOptions(1);
          callbackReference1 = callbackReference2;
        } 
      } 
      return callbackReference1.getTrampoline();
    } 
  }
  
  private static Pointer getNativeFunctionPointer(Callback paramCallback) {
    if (Proxy.isProxyClass(paramCallback.getClass())) {
      InvocationHandler invocationHandler = Proxy.getInvocationHandler(paramCallback);
      if (invocationHandler instanceof NativeFunctionHandler)
        return ((NativeFunctionHandler)invocationHandler).getPointer(); 
    } 
    return null;
  }
  
  private static Pointer getNativeString(Object paramObject, boolean paramBoolean) {
    if (paramObject != null) {
      NativeString nativeString = new NativeString(paramObject.toString(), paramBoolean);
      allocations.put(paramObject, nativeString);
      return nativeString.getPointer();
    } 
    return null;
  }
  
  private Class<?> getNativeType(Class<?> paramClass) {
    if (Structure.class.isAssignableFrom(paramClass)) {
      Structure.validate((Class)paramClass);
      if (!Structure.ByValue.class.isAssignableFrom(paramClass))
        return Pointer.class; 
    } else {
      if (NativeMapped.class.isAssignableFrom(paramClass))
        return NativeMappedConverter.getInstance(paramClass).nativeType(); 
      if (paramClass == String.class || paramClass == WString.class || paramClass == String[].class || paramClass == WString[].class || Callback.class.isAssignableFrom(paramClass))
        return Pointer.class; 
    } 
    return paramClass;
  }
  
  private static ThreadGroup initializeThread(Callback paramCallback, AttachOptions paramAttachOptions) {
    Map<Callback, CallbackThreadInitializer> map;
    ThreadGroup threadGroup;
    Callback callback = paramCallback;
    if (paramCallback instanceof DefaultCallbackProxy)
      callback = ((DefaultCallbackProxy)paramCallback).getCallback(); 
    synchronized (initializers) {
      CallbackThreadInitializer callbackThreadInitializer = initializers.get(callback);
      map = null;
      if (callbackThreadInitializer != null) {
        threadGroup = callbackThreadInitializer.getThreadGroup(callback);
        paramAttachOptions.name = callbackThreadInitializer.getName(callback);
        paramAttachOptions.daemon = callbackThreadInitializer.isDaemon(callback);
        paramAttachOptions.detach = callbackThreadInitializer.detach(callback);
        paramAttachOptions.write();
      } 
      return threadGroup;
    } 
  }
  
  private static boolean isAllowableNativeType(Class<?> paramClass) {
    return (paramClass == void.class || paramClass == Void.class || paramClass == boolean.class || paramClass == Boolean.class || paramClass == byte.class || paramClass == Byte.class || paramClass == short.class || paramClass == Short.class || paramClass == char.class || paramClass == Character.class || paramClass == int.class || paramClass == Integer.class || paramClass == long.class || paramClass == Long.class || paramClass == float.class || paramClass == Float.class || paramClass == double.class || paramClass == Double.class || (Structure.ByValue.class.isAssignableFrom(paramClass) && Structure.class.isAssignableFrom(paramClass)) || Pointer.class.isAssignableFrom(paramClass));
  }
  
  private void setCallbackOptions(int paramInt) {
    this.cbstruct.setInt(Native.POINTER_SIZE, paramInt);
  }
  
  static CallbackThreadInitializer setCallbackThreadInitializer(Callback paramCallback, CallbackThreadInitializer paramCallbackThreadInitializer) {
    // Byte code:
    //   0: getstatic com/sun/jna/CallbackReference.initializers : Ljava/util/Map;
    //   3: astore_2
    //   4: aload_2
    //   5: monitorenter
    //   6: aload_1
    //   7: ifnull -> 28
    //   10: getstatic com/sun/jna/CallbackReference.initializers : Ljava/util/Map;
    //   13: aload_0
    //   14: aload_1
    //   15: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   20: checkcast com/sun/jna/CallbackThreadInitializer
    //   23: astore_0
    //   24: aload_2
    //   25: monitorexit
    //   26: aload_0
    //   27: areturn
    //   28: getstatic com/sun/jna/CallbackReference.initializers : Ljava/util/Map;
    //   31: aload_0
    //   32: invokeinterface remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   37: checkcast com/sun/jna/CallbackThreadInitializer
    //   40: astore_0
    //   41: aload_2
    //   42: monitorexit
    //   43: aload_0
    //   44: areturn
    //   45: astore_0
    //   46: aload_2
    //   47: monitorexit
    //   48: aload_0
    //   49: athrow
    // Exception table:
    //   from	to	target	type
    //   10	26	45	finally
    //   28	43	45	finally
    //   46	48	45	finally
  }
  
  protected void dispose() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield cbstruct : Lcom/sun/jna/Pointer;
    //   6: astore_1
    //   7: aload_1
    //   8: ifnull -> 73
    //   11: aload_0
    //   12: getfield cbstruct : Lcom/sun/jna/Pointer;
    //   15: getfield peer : J
    //   18: invokestatic freeNativeCallback : (J)V
    //   21: aload_0
    //   22: getfield cbstruct : Lcom/sun/jna/Pointer;
    //   25: lconst_0
    //   26: putfield peer : J
    //   29: aload_0
    //   30: aconst_null
    //   31: putfield cbstruct : Lcom/sun/jna/Pointer;
    //   34: getstatic com/sun/jna/CallbackReference.allocatedMemory : Ljava/util/Map;
    //   37: aload_0
    //   38: invokeinterface remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   43: pop
    //   44: goto -> 73
    //   47: astore_1
    //   48: aload_0
    //   49: getfield cbstruct : Lcom/sun/jna/Pointer;
    //   52: lconst_0
    //   53: putfield peer : J
    //   56: aload_0
    //   57: aconst_null
    //   58: putfield cbstruct : Lcom/sun/jna/Pointer;
    //   61: getstatic com/sun/jna/CallbackReference.allocatedMemory : Ljava/util/Map;
    //   64: aload_0
    //   65: invokeinterface remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   70: pop
    //   71: aload_1
    //   72: athrow
    //   73: aload_0
    //   74: monitorexit
    //   75: return
    //   76: astore_1
    //   77: aload_0
    //   78: monitorexit
    //   79: aload_1
    //   80: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	76	finally
    //   11	21	47	finally
    //   21	44	76	finally
    //   48	73	76	finally
  }
  
  protected void finalize() {
    dispose();
  }
  
  public Pointer getTrampoline() {
    if (this.trampoline == null)
      this.trampoline = this.cbstruct.getPointer(0L); 
    return this.trampoline;
  }
  
  static class AttachOptions extends Structure {
    public static final List<String> FIELDS = createFieldsOrder(new String[] { "daemon", "detach", "name" });
    
    public boolean daemon;
    
    public boolean detach;
    
    public String name;
    
    AttachOptions() {
      setStringEncoding("utf8");
    }
    
    protected List<String> getFieldOrder() {
      return FIELDS;
    }
  }
  
  private class DefaultCallbackProxy implements CallbackProxy {
    private final Method callbackMethod;
    
    private final String encoding;
    
    private final FromNativeConverter[] fromNative;
    
    private ToNativeConverter toNative;
    
    public DefaultCallbackProxy(Method param1Method, TypeMapper param1TypeMapper, String param1String) {
      this.callbackMethod = param1Method;
      this.encoding = param1String;
      Class[] arrayOfClass = param1Method.getParameterTypes();
      Class<?> clazz = param1Method.getReturnType();
      this.fromNative = new FromNativeConverter[arrayOfClass.length];
      if (NativeMapped.class.isAssignableFrom(clazz)) {
        this.toNative = NativeMappedConverter.getInstance(clazz);
      } else if (param1TypeMapper != null) {
        this.toNative = param1TypeMapper.getToNativeConverter(clazz);
      } 
      for (byte b = 0; b < this.fromNative.length; b++) {
        if (NativeMapped.class.isAssignableFrom(arrayOfClass[b])) {
          this.fromNative[b] = new NativeMappedConverter(arrayOfClass[b]);
        } else if (param1TypeMapper != null) {
          this.fromNative[b] = param1TypeMapper.getFromNativeConverter(arrayOfClass[b]);
        } 
      } 
      if (!param1Method.isAccessible())
        try {
          param1Method.setAccessible(true);
        } catch (SecurityException securityException) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("Callback method is inaccessible, make sure the interface is public: ");
          stringBuilder.append(param1Method);
          throw new IllegalArgumentException(stringBuilder.toString());
        }  
    }
    
    private Object convertArgument(Object<?> param1Object, Class<?> param1Class) {
      Object<?> object;
      if (param1Object instanceof Pointer) {
        if (param1Class == String.class) {
          String str = ((Pointer)param1Object).getString(0L, this.encoding);
        } else {
          if (param1Class == WString.class) {
            param1Object = (Object<?>)new WString(((Pointer)param1Object).getWideString(0L));
          } else {
            Object<?> object1;
            if (param1Class == String[].class) {
              String[] arrayOfString = ((Pointer)param1Object).getStringArray(0L, this.encoding);
            } else if (param1Class == WString[].class) {
              String[] arrayOfString = ((Pointer)param1Object).getWideStringArray(0L);
            } else if (Callback.class.isAssignableFrom(param1Class)) {
              Callback callback = CallbackReference.getCallback(param1Class, (Pointer)param1Object);
            } else {
              object1 = param1Object;
              if (Structure.class.isAssignableFrom(param1Class)) {
                if (Structure.ByValue.class.isAssignableFrom(param1Class)) {
                  param1Class = Structure.newInstance(param1Class);
                  object1 = (Object<?>)new byte[param1Class.size()];
                  ((Pointer)param1Object).read(0L, (byte[])object1, 0, object1.length);
                  param1Class.getPointer().write(0L, (byte[])object1, 0, object1.length);
                  param1Class.read();
                  param1Object = (Object<?>)param1Class;
                } else {
                  object1 = Structure.newInstance(param1Class, (Pointer)param1Object);
                  object1.conditionalAutoRead();
                  return object1;
                } 
              } else {
                return object1;
              } 
              object1 = param1Object;
            } 
            return object1;
          } 
          object = param1Object;
        } 
      } else {
        if (boolean.class != param1Class) {
          Object<?> object1 = param1Object;
          if (Boolean.class == param1Class)
            object1 = param1Object; 
          return object1;
        } 
        object = param1Object;
      } 
      return object;
    }
    
    private Object convertResult(Object param1Object) {
      ToNativeConverter toNativeConverter = this.toNative;
      Object object = param1Object;
      if (toNativeConverter != null)
        object = toNativeConverter.toNative(param1Object, new CallbackResultContext(this.callbackMethod)); 
      if (object == null)
        return null; 
      Class<?> clazz = object.getClass();
      if (Structure.class.isAssignableFrom(clazz))
        return Structure.ByValue.class.isAssignableFrom(clazz) ? object : ((Structure)object).getPointer(); 
      if (clazz == boolean.class || clazz == Boolean.class) {
        if (Boolean.TRUE.equals(object)) {
          param1Object = Function.INTEGER_TRUE;
        } else {
          param1Object = Function.INTEGER_FALSE;
        } 
        return param1Object;
      } 
      if (clazz == String.class || clazz == WString.class) {
        boolean bool;
        if (clazz == WString.class) {
          bool = true;
        } else {
          bool = false;
        } 
        return CallbackReference.getNativeString(object, bool);
      } 
      if (clazz == String[].class || clazz == WString.class) {
        if (clazz == String[].class) {
          param1Object = new StringArray((String[])object, this.encoding);
        } else {
          param1Object = new StringArray((WString[])object);
        } 
        CallbackReference.allocations.put(object, param1Object);
        return param1Object;
      } 
      param1Object = object;
      if (Callback.class.isAssignableFrom(clazz))
        param1Object = CallbackReference.getFunctionPointer((Callback)object); 
      return param1Object;
    }
    
    private Object invokeCallback(Object[] param1ArrayOfObject) {
      Object object;
      Class[] arrayOfClass = this.callbackMethod.getParameterTypes();
      Object[] arrayOfObject1 = new Object[param1ArrayOfObject.length];
      boolean bool = false;
      byte b;
      for (b = 0; b < param1ArrayOfObject.length; b++) {
        CallbackParameterContext callbackParameterContext;
        Class<?> clazz = arrayOfClass[b];
        Object object1 = param1ArrayOfObject[b];
        if (this.fromNative[b] != null) {
          callbackParameterContext = new CallbackParameterContext(clazz, this.callbackMethod, param1ArrayOfObject, b);
          arrayOfObject1[b] = this.fromNative[b].fromNative(object1, callbackParameterContext);
        } else {
          arrayOfObject1[b] = convertArgument(object1, (Class<?>)callbackParameterContext);
        } 
      } 
      Object[] arrayOfObject2 = null;
      Callback callback = getCallback();
      b = bool;
      param1ArrayOfObject = arrayOfObject2;
      if (callback != null)
        try {
          object = convertResult(this.callbackMethod.invoke(callback, arrayOfObject1));
          b = bool;
        } catch (IllegalArgumentException illegalArgumentException) {
          Native.getCallbackExceptionHandler().uncaughtException(callback, illegalArgumentException);
          object = arrayOfObject2;
          b = bool;
        } catch (IllegalAccessException illegalAccessException) {
          Native.getCallbackExceptionHandler().uncaughtException(callback, illegalAccessException);
          b = bool;
          object = arrayOfObject2;
        } catch (InvocationTargetException invocationTargetException) {
          Native.getCallbackExceptionHandler().uncaughtException(callback, invocationTargetException.getTargetException());
          b = bool;
          object = arrayOfObject2;
        }  
      while (b < arrayOfObject1.length) {
        if (arrayOfObject1[b] instanceof Structure && !(arrayOfObject1[b] instanceof Structure.ByValue))
          ((Structure)arrayOfObject1[b]).autoWrite(); 
        b++;
      } 
      return object;
    }
    
    public Object callback(Object[] param1ArrayOfObject) {
      try {
        return invokeCallback(param1ArrayOfObject);
      } finally {
        param1ArrayOfObject = null;
        Native.getCallbackExceptionHandler().uncaughtException(getCallback(), (Throwable)param1ArrayOfObject);
      } 
    }
    
    public Callback getCallback() {
      return CallbackReference.this.getCallback();
    }
    
    public Class<?>[] getParameterTypes() {
      return this.callbackMethod.getParameterTypes();
    }
    
    public Class<?> getReturnType() {
      return this.callbackMethod.getReturnType();
    }
  }
  
  private static class NativeFunctionHandler implements InvocationHandler {
    private final Function function;
    
    private final Map<String, ?> options;
    
    public NativeFunctionHandler(Pointer param1Pointer, int param1Int, Map<String, ?> param1Map) {
      this.options = param1Map;
      this.function = new Function(param1Pointer, param1Int, (String)param1Map.get("string-encoding"));
    }
    
    public Pointer getPointer() {
      return this.function;
    }
    
    public Object invoke(Object<?> param1Object, Method param1Method, Object[] param1ArrayOfObject) throws Throwable {
      Object[] arrayOfObject;
      StringBuilder stringBuilder;
      Class<?> clazz;
      if (Library.Handler.OBJECT_TOSTRING.equals(param1Method)) {
        param1Object = (Object<?>)new StringBuilder();
        param1Object.append("Proxy interface to ");
        param1Object.append(this.function);
        param1Object = (Object<?>)param1Object.toString();
        clazz = CallbackReference.findCallbackClass(((Method)this.options.get("invoking-method")).getDeclaringClass());
        stringBuilder = new StringBuilder();
        stringBuilder.append((String)param1Object);
        stringBuilder.append(" (");
        stringBuilder.append(clazz.getName());
        stringBuilder.append(")");
        return stringBuilder.toString();
      } 
      if (Library.Handler.OBJECT_HASHCODE.equals(stringBuilder))
        return Integer.valueOf(hashCode()); 
      if (Library.Handler.OBJECT_EQUALS.equals(stringBuilder)) {
        boolean bool = false;
        param1Object = (Object<?>)clazz[0];
        if (param1Object != null && Proxy.isProxyClass(param1Object.getClass())) {
          if (Proxy.getInvocationHandler(param1Object) == this)
            bool = true; 
          return Function.valueOf(bool);
        } 
        return Boolean.FALSE;
      } 
      param1Object = (Object<?>)clazz;
      if (Function.isVarArgs((Method)stringBuilder))
        arrayOfObject = Function.concatenateVarArgs((Object[])clazz); 
      return this.function.invoke(stringBuilder.getReturnType(), arrayOfObject, this.options);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/sun/jna/CallbackReference.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */