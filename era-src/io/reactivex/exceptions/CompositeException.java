package io.reactivex.exceptions;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

public final class CompositeException extends RuntimeException {
  private static final long serialVersionUID = 3026362227162912146L;
  
  private Throwable cause;
  
  private final List<Throwable> exceptions;
  
  private final String message;
  
  public CompositeException(Iterable<? extends Throwable> paramIterable) {
    LinkedHashSet<Throwable> linkedHashSet = new LinkedHashSet();
    ArrayList<Throwable> arrayList = new ArrayList();
    if (paramIterable != null) {
      for (Throwable throwable : paramIterable) {
        if (throwable instanceof CompositeException) {
          linkedHashSet.addAll(((CompositeException)throwable).getExceptions());
          continue;
        } 
        if (throwable != null) {
          linkedHashSet.add(throwable);
          continue;
        } 
        linkedHashSet.add(new NullPointerException("Throwable was null!"));
      } 
    } else {
      linkedHashSet.add(new NullPointerException("errors was null"));
    } 
    if (!linkedHashSet.isEmpty()) {
      arrayList.addAll(linkedHashSet);
      this.exceptions = Collections.unmodifiableList(arrayList);
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(this.exceptions.size());
      stringBuilder.append(" exceptions occurred. ");
      this.message = stringBuilder.toString();
      return;
    } 
    IllegalArgumentException illegalArgumentException = new IllegalArgumentException("errors is empty");
    throw illegalArgumentException;
  }
  
  public CompositeException(Throwable... paramVarArgs) {
    // Byte code:
    //   0: aload_1
    //   1: ifnonnull -> 20
    //   4: new java/lang/NullPointerException
    //   7: dup
    //   8: ldc 'exceptions was null'
    //   10: invokespecial <init> : (Ljava/lang/String;)V
    //   13: invokestatic singletonList : (Ljava/lang/Object;)Ljava/util/List;
    //   16: astore_1
    //   17: goto -> 25
    //   20: aload_1
    //   21: invokestatic asList : ([Ljava/lang/Object;)Ljava/util/List;
    //   24: astore_1
    //   25: aload_0
    //   26: aload_1
    //   27: invokespecial <init> : (Ljava/lang/Iterable;)V
    //   30: return
  }
  
  private void appendStackTrace(StringBuilder paramStringBuilder, Throwable paramThrowable, String paramString) {
    paramStringBuilder.append(paramString);
    paramStringBuilder.append(paramThrowable);
    paramStringBuilder.append('\n');
    for (StackTraceElement stackTraceElement : paramThrowable.getStackTrace()) {
      paramStringBuilder.append("\t\tat ");
      paramStringBuilder.append(stackTraceElement);
      paramStringBuilder.append('\n');
    } 
    if (paramThrowable.getCause() != null) {
      paramStringBuilder.append("\tCaused by: ");
      appendStackTrace(paramStringBuilder, paramThrowable.getCause(), "");
    } 
  }
  
  private List<Throwable> getListOfCauses(Throwable paramThrowable) {
    ArrayList<Throwable> arrayList = new ArrayList();
    Throwable throwable = paramThrowable.getCause();
    if (throwable != null) {
      Throwable throwable1 = throwable;
      if (throwable != paramThrowable)
        while (true) {
          arrayList.add(throwable1);
          paramThrowable = throwable1.getCause();
          if (paramThrowable == null || paramThrowable == throwable1)
            break; 
          throwable1 = paramThrowable;
        }  
    } 
    return arrayList;
  }
  
  private void printStackTrace(PrintStreamOrWriter paramPrintStreamOrWriter) {
    StringBuilder stringBuilder = new StringBuilder(128);
    stringBuilder.append(this);
    stringBuilder.append('\n');
    for (StackTraceElement stackTraceElement : getStackTrace()) {
      stringBuilder.append("\tat ");
      stringBuilder.append(stackTraceElement);
      stringBuilder.append('\n');
    } 
    Iterator<Throwable> iterator = this.exceptions.iterator();
    for (byte b = 1; iterator.hasNext(); b++) {
      Throwable throwable = iterator.next();
      stringBuilder.append("  ComposedException ");
      stringBuilder.append(b);
      stringBuilder.append(" :\n");
      appendStackTrace(stringBuilder, throwable, "\t");
    } 
    paramPrintStreamOrWriter.println(stringBuilder.toString());
  }
  
  public Throwable getCause() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield cause : Ljava/lang/Throwable;
    //   6: ifnonnull -> 177
    //   9: new io/reactivex/exceptions/CompositeException$CompositeExceptionCausalChain
    //   12: astore_1
    //   13: aload_1
    //   14: invokespecial <init> : ()V
    //   17: new java/util/HashSet
    //   20: astore_2
    //   21: aload_2
    //   22: invokespecial <init> : ()V
    //   25: aload_0
    //   26: getfield exceptions : Ljava/util/List;
    //   29: invokeinterface iterator : ()Ljava/util/Iterator;
    //   34: astore_3
    //   35: aload_1
    //   36: astore #4
    //   38: aload_3
    //   39: invokeinterface hasNext : ()Z
    //   44: ifeq -> 172
    //   47: aload_3
    //   48: invokeinterface next : ()Ljava/lang/Object;
    //   53: checkcast java/lang/Throwable
    //   56: astore #5
    //   58: aload_2
    //   59: aload #5
    //   61: invokeinterface contains : (Ljava/lang/Object;)Z
    //   66: ifeq -> 72
    //   69: goto -> 38
    //   72: aload_2
    //   73: aload #5
    //   75: invokeinterface add : (Ljava/lang/Object;)Z
    //   80: pop
    //   81: aload_0
    //   82: aload #5
    //   84: invokespecial getListOfCauses : (Ljava/lang/Throwable;)Ljava/util/List;
    //   87: invokeinterface iterator : ()Ljava/util/Iterator;
    //   92: astore #6
    //   94: aload #6
    //   96: invokeinterface hasNext : ()Z
    //   101: ifeq -> 153
    //   104: aload #6
    //   106: invokeinterface next : ()Ljava/lang/Object;
    //   111: checkcast java/lang/Throwable
    //   114: astore #7
    //   116: aload_2
    //   117: aload #7
    //   119: invokeinterface contains : (Ljava/lang/Object;)Z
    //   124: ifeq -> 141
    //   127: new java/lang/RuntimeException
    //   130: dup
    //   131: ldc 'Duplicate found in causal chain so cropping to prevent loop ...'
    //   133: invokespecial <init> : (Ljava/lang/String;)V
    //   136: astore #5
    //   138: goto -> 94
    //   141: aload_2
    //   142: aload #7
    //   144: invokeinterface add : (Ljava/lang/Object;)Z
    //   149: pop
    //   150: goto -> 94
    //   153: aload #4
    //   155: aload #5
    //   157: invokevirtual initCause : (Ljava/lang/Throwable;)Ljava/lang/Throwable;
    //   160: pop
    //   161: aload_0
    //   162: aload #4
    //   164: invokevirtual getRootCause : (Ljava/lang/Throwable;)Ljava/lang/Throwable;
    //   167: astore #4
    //   169: goto -> 38
    //   172: aload_0
    //   173: aload_1
    //   174: putfield cause : Ljava/lang/Throwable;
    //   177: aload_0
    //   178: getfield cause : Ljava/lang/Throwable;
    //   181: astore #4
    //   183: aload_0
    //   184: monitorexit
    //   185: aload #4
    //   187: areturn
    //   188: astore #4
    //   190: aload_0
    //   191: monitorexit
    //   192: goto -> 198
    //   195: aload #4
    //   197: athrow
    //   198: goto -> 195
    //   201: astore #5
    //   203: goto -> 161
    // Exception table:
    //   from	to	target	type
    //   2	35	188	finally
    //   38	69	188	finally
    //   72	94	188	finally
    //   94	138	188	finally
    //   141	150	188	finally
    //   153	161	201	finally
    //   161	169	188	finally
    //   172	177	188	finally
    //   177	183	188	finally
  }
  
  public List<Throwable> getExceptions() {
    return this.exceptions;
  }
  
  public String getMessage() {
    return this.message;
  }
  
  Throwable getRootCause(Throwable paramThrowable) {
    Throwable throwable = paramThrowable.getCause();
    if (throwable != null) {
      Throwable throwable1 = throwable;
      if (paramThrowable != throwable) {
        while (true) {
          paramThrowable = throwable1.getCause();
          if (paramThrowable == null || paramThrowable == throwable1)
            break; 
          throwable1 = paramThrowable;
        } 
        return throwable1;
      } 
    } 
    return paramThrowable;
  }
  
  public void printStackTrace() {
    printStackTrace(System.err);
  }
  
  public void printStackTrace(PrintStream paramPrintStream) {
    printStackTrace(new WrappedPrintStream(paramPrintStream));
  }
  
  public void printStackTrace(PrintWriter paramPrintWriter) {
    printStackTrace(new WrappedPrintWriter(paramPrintWriter));
  }
  
  public int size() {
    return this.exceptions.size();
  }
  
  static final class CompositeExceptionCausalChain extends RuntimeException {
    static final String MESSAGE = "Chain of Causes for CompositeException In Order Received =>";
    
    private static final long serialVersionUID = 3875212506787802066L;
    
    public String getMessage() {
      return "Chain of Causes for CompositeException In Order Received =>";
    }
  }
  
  static abstract class PrintStreamOrWriter {
    abstract void println(Object param1Object);
  }
  
  static final class WrappedPrintStream extends PrintStreamOrWriter {
    private final PrintStream printStream;
    
    WrappedPrintStream(PrintStream param1PrintStream) {
      this.printStream = param1PrintStream;
    }
    
    void println(Object param1Object) {
      this.printStream.println(param1Object);
    }
  }
  
  static final class WrappedPrintWriter extends PrintStreamOrWriter {
    private final PrintWriter printWriter;
    
    WrappedPrintWriter(PrintWriter param1PrintWriter) {
      this.printWriter = param1PrintWriter;
    }
    
    void println(Object param1Object) {
      this.printWriter.println(param1Object);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/exceptions/CompositeException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */