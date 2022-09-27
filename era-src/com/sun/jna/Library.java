package com.sun.jna;

import com.sun.jna.internal.ReflectionUtils;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

public interface Library {
  public static final String OPTION_ALLOW_OBJECTS = "allow-objects";
  
  public static final String OPTION_CALLING_CONVENTION = "calling-convention";
  
  public static final String OPTION_CLASSLOADER = "classloader";
  
  public static final String OPTION_FUNCTION_MAPPER = "function-mapper";
  
  public static final String OPTION_INVOCATION_MAPPER = "invocation-mapper";
  
  public static final String OPTION_OPEN_FLAGS = "open-flags";
  
  public static final String OPTION_STRING_ENCODING = "string-encoding";
  
  public static final String OPTION_STRUCTURE_ALIGNMENT = "structure-alignment";
  
  public static final String OPTION_TYPE_MAPPER = "type-mapper";
  
  public static class Handler implements InvocationHandler {
    static final Method OBJECT_EQUALS;
    
    static final Method OBJECT_HASHCODE;
    
    static final Method OBJECT_TOSTRING;
    
    private final Map<Method, FunctionInfo> functions = new WeakHashMap<Method, FunctionInfo>();
    
    private final Class<?> interfaceClass;
    
    private final InvocationMapper invocationMapper;
    
    private final NativeLibrary nativeLibrary;
    
    private final Map<String, Object> options;
    
    static {
      try {
        OBJECT_TOSTRING = Object.class.getMethod("toString", new Class[0]);
        OBJECT_HASHCODE = Object.class.getMethod("hashCode", new Class[0]);
        OBJECT_EQUALS = Object.class.getMethod("equals", new Class[] { Object.class });
        return;
      } catch (Exception exception) {
        throw new Error("Error retrieving Object.toString() method");
      } 
    }
    
    public Handler(String param1String, Class<?> param1Class, Map<String, ?> param1Map) {
      if (param1String == null || !"".equals(param1String.trim())) {
        if (param1Class.isInterface()) {
          boolean bool;
          this.interfaceClass = param1Class;
          this.options = new HashMap<String, Object>(param1Map);
          if (AltCallingConvention.class.isAssignableFrom(param1Class)) {
            bool = true;
          } else {
            bool = false;
          } 
          if (this.options.get("calling-convention") == null)
            this.options.put("calling-convention", Integer.valueOf(bool)); 
          if (this.options.get("classloader") == null)
            this.options.put("classloader", param1Class.getClassLoader()); 
          this.nativeLibrary = NativeLibrary.getInstance(param1String, this.options);
          this.invocationMapper = (InvocationMapper)this.options.get("invocation-mapper");
          return;
        } 
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append(param1String);
        stringBuilder1.append(" does not implement an interface: ");
        stringBuilder1.append(param1Class.getName());
        throw new IllegalArgumentException(stringBuilder1.toString());
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Invalid library name \"");
      stringBuilder.append(param1String);
      stringBuilder.append("\"");
      throw new IllegalArgumentException(stringBuilder.toString());
    }
    
    public Class<?> getInterfaceClass() {
      return this.interfaceClass;
    }
    
    public String getLibraryName() {
      return this.nativeLibrary.getName();
    }
    
    public NativeLibrary getNativeLibrary() {
      return this.nativeLibrary;
    }
    
    public Object invoke(Object param1Object, Method param1Method, Object[] param1ArrayOfObject) throws Throwable {
      if (OBJECT_TOSTRING.equals(param1Method)) {
        param1Object = new StringBuilder();
        param1Object.append("Proxy interface to ");
        param1Object.append(this.nativeLibrary);
        return param1Object.toString();
      } 
      if (OBJECT_HASHCODE.equals(param1Method))
        return Integer.valueOf(hashCode()); 
      if (OBJECT_EQUALS.equals(param1Method)) {
        boolean bool = false;
        param1Object = param1ArrayOfObject[0];
        if (param1Object != null && Proxy.isProxyClass(param1Object.getClass())) {
          if (Proxy.getInvocationHandler(param1Object) == this)
            bool = true; 
          return Function.valueOf(bool);
        } 
        return Boolean.FALSE;
      } 
      FunctionInfo functionInfo1 = this.functions.get(param1Method);
      FunctionInfo functionInfo2 = functionInfo1;
      if (functionInfo1 == null)
        synchronized (this.functions) {
          functionInfo1 = this.functions.get(param1Method);
          functionInfo2 = functionInfo1;
          if (functionInfo1 == null) {
            if (!ReflectionUtils.isDefault(param1Method)) {
              FunctionInfo functionInfo3;
              FunctionInfo functionInfo4;
              boolean bool = Function.isVarArgs(param1Method);
              if (this.invocationMapper != null) {
                InvocationHandler invocationHandler = this.invocationMapper.getInvocationHandler(this.nativeLibrary, param1Method);
              } else {
                functionInfo1 = null;
              } 
              if (functionInfo1 == null) {
                Function function = this.nativeLibrary.getFunction(param1Method.getName(), param1Method);
                Class[] arrayOfClass = param1Method.getParameterTypes();
                HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
                this((Map)this.options);
                hashMap.put("invoking-method", param1Method);
              } else {
                functionInfo3 = null;
                functionInfo2 = functionInfo3;
                functionInfo4 = functionInfo2;
              } 
              FunctionInfo functionInfo5 = new FunctionInfo();
              this((InvocationHandler)functionInfo1, (Function)functionInfo3, (Class<?>[])functionInfo2, bool, (Map<String, ?>)functionInfo4);
              functionInfo2 = functionInfo5;
            } else {
              functionInfo2 = new FunctionInfo(ReflectionUtils.getMethodHandle(param1Method));
            } 
            this.functions.put(param1Method, functionInfo2);
          } 
        }  
      if (functionInfo2.methodHandle != null)
        return ReflectionUtils.invokeDefaultMethod(param1Object, functionInfo2.methodHandle, param1ArrayOfObject); 
      Object[] arrayOfObject = param1ArrayOfObject;
      if (functionInfo2.isVarArgs)
        arrayOfObject = Function.concatenateVarArgs(param1ArrayOfObject); 
      return (functionInfo2.handler != null) ? functionInfo2.handler.invoke(param1Object, param1Method, arrayOfObject) : functionInfo2.function.invoke(param1Method, functionInfo2.parameterTypes, param1Method.getReturnType(), arrayOfObject, functionInfo2.options);
    }
    
    private static final class FunctionInfo {
      final Function function = null;
      
      final InvocationHandler handler = null;
      
      final boolean isVarArgs = false;
      
      final Object methodHandle;
      
      final Map<String, ?> options = null;
      
      final Class<?>[] parameterTypes = null;
      
      FunctionInfo(Object param2Object) {
        this.methodHandle = param2Object;
      }
      
      FunctionInfo(InvocationHandler param2InvocationHandler, Function param2Function, Class<?>[] param2ArrayOfClass, boolean param2Boolean, Map<String, ?> param2Map) {
        this.methodHandle = null;
      }
    }
  }
  
  private static final class FunctionInfo {
    final Function function = null;
    
    final InvocationHandler handler = null;
    
    final boolean isVarArgs = false;
    
    final Object methodHandle;
    
    final Map<String, ?> options = null;
    
    final Class<?>[] parameterTypes = null;
    
    FunctionInfo(Object param1Object) {
      this.methodHandle = param1Object;
    }
    
    FunctionInfo(InvocationHandler param1InvocationHandler, Function param1Function, Class<?>[] param1ArrayOfClass, boolean param1Boolean, Map<String, ?> param1Map) {
      this.methodHandle = null;
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/sun/jna/Library.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */