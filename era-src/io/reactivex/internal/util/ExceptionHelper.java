package io.reactivex.internal.util;

import io.reactivex.exceptions.CompositeException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public final class ExceptionHelper {
  public static final Throwable TERMINATED = new Termination();
  
  private ExceptionHelper() {
    throw new IllegalStateException("No instances!");
  }
  
  public static <T> boolean addThrowable(AtomicReference<Throwable> paramAtomicReference, Throwable paramThrowable) {
    while (true) {
      CompositeException compositeException;
      Throwable throwable = paramAtomicReference.get();
      if (throwable == TERMINATED)
        return false; 
      if (throwable == null) {
        Throwable throwable1 = paramThrowable;
      } else {
        compositeException = new CompositeException(new Throwable[] { throwable, paramThrowable });
      } 
      if (paramAtomicReference.compareAndSet(throwable, compositeException))
        return true; 
    } 
  }
  
  public static List<Throwable> flatten(Throwable paramThrowable) {
    ArrayList<List<Throwable>> arrayList = new ArrayList();
    ArrayDeque<Throwable> arrayDeque = new ArrayDeque();
    arrayDeque.offer(paramThrowable);
    while (!arrayDeque.isEmpty()) {
      List<Throwable> list;
      paramThrowable = arrayDeque.removeFirst();
      if (paramThrowable instanceof CompositeException) {
        list = ((CompositeException)paramThrowable).getExceptions();
        for (int i = list.size() - 1; i >= 0; i--)
          arrayDeque.offerFirst(list.get(i)); 
        continue;
      } 
      arrayList.add(list);
    } 
    return (List)arrayList;
  }
  
  public static <T> Throwable terminate(AtomicReference<Throwable> paramAtomicReference) {
    Throwable throwable1 = paramAtomicReference.get();
    Throwable throwable2 = TERMINATED;
    Throwable throwable3 = throwable1;
    if (throwable1 != throwable2)
      throwable3 = paramAtomicReference.getAndSet(throwable2); 
    return throwable3;
  }
  
  public static <E extends Throwable> Exception throwIfThrowable(Throwable paramThrowable) throws E {
    if (paramThrowable instanceof Exception)
      return (Exception)paramThrowable; 
    throw (E)paramThrowable;
  }
  
  public static String timeoutMessage(long paramLong, TimeUnit paramTimeUnit) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("The source did not signal an event for ");
    stringBuilder.append(paramLong);
    stringBuilder.append(" ");
    stringBuilder.append(paramTimeUnit.toString().toLowerCase());
    stringBuilder.append(" and has been terminated.");
    return stringBuilder.toString();
  }
  
  public static RuntimeException wrapOrThrow(Throwable paramThrowable) {
    if (!(paramThrowable instanceof Error))
      return (paramThrowable instanceof RuntimeException) ? (RuntimeException)paramThrowable : new RuntimeException(paramThrowable); 
    throw (Error)paramThrowable;
  }
  
  static final class Termination extends Throwable {
    private static final long serialVersionUID = -4649703670690200604L;
    
    Termination() {
      super("No further exceptions");
    }
    
    public Throwable fillInStackTrace() {
      return this;
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/util/ExceptionHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */