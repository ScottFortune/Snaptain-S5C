package com.sun.jna;

import java.lang.reflect.Method;

abstract class VarArgsChecker {
  private VarArgsChecker() {}
  
  static VarArgsChecker create() {
    try {
      return (VarArgsChecker)((Method.class.getMethod("isVarArgs", new Class[0]) != null) ? new RealVarArgsChecker() : new NoVarArgsChecker());
    } catch (NoSuchMethodException noSuchMethodException) {
      return new NoVarArgsChecker();
    } catch (SecurityException securityException) {
      return new NoVarArgsChecker();
    } 
  }
  
  abstract int fixedArgs(Method paramMethod);
  
  abstract boolean isVarArgs(Method paramMethod);
  
  private static final class NoVarArgsChecker extends VarArgsChecker {
    private NoVarArgsChecker() {}
    
    int fixedArgs(Method param1Method) {
      return 0;
    }
    
    boolean isVarArgs(Method param1Method) {
      return false;
    }
  }
  
  private static final class RealVarArgsChecker extends VarArgsChecker {
    private RealVarArgsChecker() {}
    
    int fixedArgs(Method param1Method) {
      boolean bool;
      if (param1Method.isVarArgs()) {
        bool = (param1Method.getParameterTypes()).length - 1;
      } else {
        bool = false;
      } 
      return bool;
    }
    
    boolean isVarArgs(Method param1Method) {
      return param1Method.isVarArgs();
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/sun/jna/VarArgsChecker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */