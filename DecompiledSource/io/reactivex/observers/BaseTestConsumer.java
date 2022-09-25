package io.reactivex.observers;

import io.reactivex.Notification;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.functions.Predicate;
import io.reactivex.internal.functions.Functions;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.util.ExceptionHelper;
import io.reactivex.internal.util.VolatileSizeArrayList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public abstract class BaseTestConsumer<T, U extends BaseTestConsumer<T, U>> implements Disposable {
  protected boolean checkSubscriptionOnce;
  
  protected long completions;
  
  protected final CountDownLatch done = new CountDownLatch(1);
  
  protected final List<Throwable> errors = (List<Throwable>)new VolatileSizeArrayList();
  
  protected int establishedFusionMode;
  
  protected int initialFusionMode;
  
  protected Thread lastThread;
  
  protected CharSequence tag;
  
  protected boolean timeout;
  
  protected final List<T> values = (List<T>)new VolatileSizeArrayList();
  
  public static String valueAndClass(Object paramObject) {
    if (paramObject != null) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(paramObject);
      stringBuilder.append(" (class: ");
      stringBuilder.append(paramObject.getClass().getSimpleName());
      stringBuilder.append(")");
      return stringBuilder.toString();
    } 
    return "null";
  }
  
  public final U assertComplete() {
    long l = this.completions;
    if (l != 0L) {
      if (l <= 1L)
        return (U)this; 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Multiple completions: ");
      stringBuilder.append(l);
      throw fail(stringBuilder.toString());
    } 
    throw fail("Not completed");
  }
  
  public final U assertEmpty() {
    return (U)assertSubscribed().assertNoValues().assertNoErrors().assertNotComplete();
  }
  
  public final U assertError(Predicate<Throwable> paramPredicate) {
    int i = this.errors.size();
    if (i != 0) {
      boolean bool2;
      boolean bool1 = false;
      Iterator<Throwable> iterator = this.errors.iterator();
      while (true) {
        bool2 = bool1;
        if (iterator.hasNext()) {
          Throwable throwable = iterator.next();
          try {
            boolean bool = paramPredicate.test(throwable);
            if (bool) {
              bool2 = true;
              break;
            } 
          } catch (Exception exception) {
            throw ExceptionHelper.wrapOrThrow(exception);
          } 
          continue;
        } 
        break;
      } 
      if (bool2) {
        if (i == 1)
          return (U)this; 
        throw fail("Error present but other errors as well");
      } 
      throw fail("Error not present");
    } 
    AssertionError assertionError = fail("No errors");
    throw assertionError;
  }
  
  public final U assertError(Class<? extends Throwable> paramClass) {
    return assertError(Functions.isInstanceOf(paramClass));
  }
  
  public final U assertError(Throwable paramThrowable) {
    return assertError(Functions.equalsWith(paramThrowable));
  }
  
  public final U assertErrorMessage(String paramString) {
    int i = this.errors.size();
    if (i != 0) {
      if (i == 1) {
        String str = ((Throwable)this.errors.get(0)).getMessage();
        if (ObjectHelper.equals(paramString, str))
          return (U)this; 
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Error message differs; exptected: ");
        stringBuilder.append(paramString);
        stringBuilder.append(" but was: ");
        stringBuilder.append(str);
        throw fail(stringBuilder.toString());
      } 
      throw fail("Multiple errors");
    } 
    throw fail("No errors");
  }
  
  public final U assertFailure(Predicate<Throwable> paramPredicate, T... paramVarArgs) {
    return (U)assertSubscribed().assertValues(paramVarArgs).assertError(paramPredicate).assertNotComplete();
  }
  
  public final U assertFailure(Class<? extends Throwable> paramClass, T... paramVarArgs) {
    return (U)assertSubscribed().assertValues(paramVarArgs).assertError(paramClass).assertNotComplete();
  }
  
  public final U assertFailureAndMessage(Class<? extends Throwable> paramClass, String paramString, T... paramVarArgs) {
    return (U)assertSubscribed().assertValues(paramVarArgs).assertError(paramClass).assertErrorMessage(paramString).assertNotComplete();
  }
  
  public final U assertNever(Predicate<? super T> paramPredicate) {
    int i = this.values.size();
    byte b = 0;
    while (b < i) {
      T t = this.values.get(b);
      try {
        if (!paramPredicate.test(t)) {
          b++;
          continue;
        } 
        StringBuilder stringBuilder = new StringBuilder();
        this();
        stringBuilder.append("Value at position ");
        stringBuilder.append(b);
        stringBuilder.append(" matches predicate ");
        stringBuilder.append(paramPredicate.toString());
        stringBuilder.append(", which was not expected.");
        throw fail(stringBuilder.toString());
      } catch (Exception exception) {
        throw ExceptionHelper.wrapOrThrow(exception);
      } 
    } 
    return (U)this;
  }
  
  public final U assertNever(T paramT) {
    int i = this.values.size();
    byte b = 0;
    while (b < i) {
      if (!ObjectHelper.equals(this.values.get(b), paramT)) {
        b++;
        continue;
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Value at position ");
      stringBuilder.append(b);
      stringBuilder.append(" is equal to ");
      stringBuilder.append(valueAndClass(paramT));
      stringBuilder.append("; Expected them to be different");
      throw fail(stringBuilder.toString());
    } 
    return (U)this;
  }
  
  public final U assertNoErrors() {
    if (this.errors.size() == 0)
      return (U)this; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Error(s) present: ");
    stringBuilder.append(this.errors);
    throw fail(stringBuilder.toString());
  }
  
  public final U assertNoTimeout() {
    if (!this.timeout)
      return (U)this; 
    throw fail("Timeout?!");
  }
  
  public final U assertNoValues() {
    return assertValueCount(0);
  }
  
  public final U assertNotComplete() {
    long l = this.completions;
    if (l != 1L) {
      if (l <= 1L)
        return (U)this; 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Multiple completions: ");
      stringBuilder.append(l);
      throw fail(stringBuilder.toString());
    } 
    throw fail("Completed!");
  }
  
  public abstract U assertNotSubscribed();
  
  public final U assertNotTerminated() {
    if (this.done.getCount() != 0L)
      return (U)this; 
    throw fail("Subscriber terminated!");
  }
  
  public final U assertResult(T... paramVarArgs) {
    return (U)assertSubscribed().assertValues(paramVarArgs).assertNoErrors().assertComplete();
  }
  
  public abstract U assertSubscribed();
  
  public final U assertTerminated() {
    if (this.done.getCount() == 0L) {
      long l = this.completions;
      if (l <= 1L) {
        int i = this.errors.size();
        if (i <= 1) {
          if (l == 0L || i == 0)
            return (U)this; 
          StringBuilder stringBuilder2 = new StringBuilder();
          stringBuilder2.append("Terminated with multiple completions and errors: ");
          stringBuilder2.append(l);
          throw fail(stringBuilder2.toString());
        } 
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("Terminated with multiple errors: ");
        stringBuilder1.append(i);
        throw fail(stringBuilder1.toString());
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Terminated with multiple completions: ");
      stringBuilder.append(l);
      throw fail(stringBuilder.toString());
    } 
    throw fail("Subscriber still running!");
  }
  
  public final U assertTimeout() {
    if (this.timeout)
      return (U)this; 
    throw fail("No timeout?!");
  }
  
  public final U assertValue(Predicate<T> paramPredicate) {
    assertValueAt(0, paramPredicate);
    if (this.values.size() <= 1)
      return (U)this; 
    throw fail("Value present but other values as well");
  }
  
  public final U assertValue(T paramT) {
    if (this.values.size() == 1) {
      T t = this.values.get(0);
      if (ObjectHelper.equals(paramT, t))
        return (U)this; 
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("expected: ");
      stringBuilder1.append(valueAndClass(paramT));
      stringBuilder1.append(" but was: ");
      stringBuilder1.append(valueAndClass(t));
      throw fail(stringBuilder1.toString());
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("expected: ");
    stringBuilder.append(valueAndClass(paramT));
    stringBuilder.append(" but was: ");
    stringBuilder.append(this.values);
    throw fail(stringBuilder.toString());
  }
  
  public final U assertValueAt(int paramInt, Predicate<T> paramPredicate) {
    if (this.values.size() != 0) {
      if (paramInt < this.values.size())
        try {
          boolean bool = paramPredicate.test(this.values.get(paramInt));
          if (bool)
            return (U)this; 
          throw fail("Value not present");
        } catch (Exception exception) {
          throw ExceptionHelper.wrapOrThrow(exception);
        }  
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Invalid index: ");
      stringBuilder.append(paramInt);
      throw fail(stringBuilder.toString());
    } 
    throw fail("No values");
  }
  
  public final U assertValueAt(int paramInt, T paramT) {
    int i = this.values.size();
    if (i != 0) {
      if (paramInt < i) {
        T t = this.values.get(paramInt);
        if (ObjectHelper.equals(paramT, t))
          return (U)this; 
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("expected: ");
        stringBuilder1.append(valueAndClass(paramT));
        stringBuilder1.append(" but was: ");
        stringBuilder1.append(valueAndClass(t));
        throw fail(stringBuilder1.toString());
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Invalid index: ");
      stringBuilder.append(paramInt);
      throw fail(stringBuilder.toString());
    } 
    throw fail("No values");
  }
  
  public final U assertValueCount(int paramInt) {
    int i = this.values.size();
    if (i == paramInt)
      return (U)this; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Value counts differ; expected: ");
    stringBuilder.append(paramInt);
    stringBuilder.append(" but was: ");
    stringBuilder.append(i);
    throw fail(stringBuilder.toString());
  }
  
  public final U assertValueSequence(Iterable<? extends T> paramIterable) {
    boolean bool1;
    boolean bool2;
    Iterator<T> iterator = this.values.iterator();
    Iterator<? extends T> iterator1 = paramIterable.iterator();
    byte b = 0;
    while (true) {
      bool1 = iterator1.hasNext();
      bool2 = iterator.hasNext();
      if (!bool2 || !bool1)
        break; 
      paramIterable = (Iterable<? extends T>)iterator1.next();
      T t = iterator.next();
      if (ObjectHelper.equals(paramIterable, t)) {
        b++;
        continue;
      } 
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("Values at position ");
      stringBuilder1.append(b);
      stringBuilder1.append(" differ; expected: ");
      stringBuilder1.append(valueAndClass(paramIterable));
      stringBuilder1.append(" but was: ");
      stringBuilder1.append(valueAndClass(t));
      throw fail(stringBuilder1.toString());
    } 
    if (!bool2) {
      if (!bool1)
        return (U)this; 
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("Fewer values received than expected (");
      stringBuilder1.append(b);
      stringBuilder1.append(")");
      throw fail(stringBuilder1.toString());
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("More values received than expected (");
    stringBuilder.append(b);
    stringBuilder.append(")");
    AssertionError assertionError = fail(stringBuilder.toString());
    throw assertionError;
  }
  
  public final U assertValueSequenceOnly(Iterable<? extends T> paramIterable) {
    return (U)assertSubscribed().assertValueSequence(paramIterable).assertNoErrors().assertNotComplete();
  }
  
  public final U assertValueSet(Collection<? extends T> paramCollection) {
    // Byte code:
    //   0: aload_1
    //   1: invokeinterface isEmpty : ()Z
    //   6: ifeq -> 16
    //   9: aload_0
    //   10: invokevirtual assertNoValues : ()Lio/reactivex/observers/BaseTestConsumer;
    //   13: pop
    //   14: aload_0
    //   15: areturn
    //   16: aload_0
    //   17: getfield values : Ljava/util/List;
    //   20: invokeinterface iterator : ()Ljava/util/Iterator;
    //   25: astore_2
    //   26: aload_2
    //   27: invokeinterface hasNext : ()Z
    //   32: ifeq -> 89
    //   35: aload_2
    //   36: invokeinterface next : ()Ljava/lang/Object;
    //   41: astore_3
    //   42: aload_1
    //   43: aload_3
    //   44: invokeinterface contains : (Ljava/lang/Object;)Z
    //   49: ifeq -> 55
    //   52: goto -> 26
    //   55: new java/lang/StringBuilder
    //   58: dup
    //   59: invokespecial <init> : ()V
    //   62: astore_1
    //   63: aload_1
    //   64: ldc_w 'Value not in the expected collection: '
    //   67: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   70: pop
    //   71: aload_1
    //   72: aload_3
    //   73: invokestatic valueAndClass : (Ljava/lang/Object;)Ljava/lang/String;
    //   76: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   79: pop
    //   80: aload_0
    //   81: aload_1
    //   82: invokevirtual toString : ()Ljava/lang/String;
    //   85: invokevirtual fail : (Ljava/lang/String;)Ljava/lang/AssertionError;
    //   88: athrow
    //   89: aload_0
    //   90: areturn
  }
  
  public final U assertValueSetOnly(Collection<? extends T> paramCollection) {
    return (U)assertSubscribed().assertValueSet(paramCollection).assertNoErrors().assertNotComplete();
  }
  
  public final U assertValues(T... paramVarArgs) {
    StringBuilder stringBuilder1;
    int i = this.values.size();
    if (i == paramVarArgs.length) {
      byte b = 0;
      while (b < i) {
        T t1 = this.values.get(b);
        T t2 = paramVarArgs[b];
        if (ObjectHelper.equals(t2, t1)) {
          b++;
          continue;
        } 
        stringBuilder1 = new StringBuilder();
        stringBuilder1.append("Values at position ");
        stringBuilder1.append(b);
        stringBuilder1.append(" differ; expected: ");
        stringBuilder1.append(valueAndClass(t2));
        stringBuilder1.append(" but was: ");
        stringBuilder1.append(valueAndClass(t1));
        throw fail(stringBuilder1.toString());
      } 
      return (U)this;
    } 
    StringBuilder stringBuilder2 = new StringBuilder();
    stringBuilder2.append("Value count differs; expected: ");
    stringBuilder2.append(stringBuilder1.length);
    stringBuilder2.append(" ");
    stringBuilder2.append(Arrays.toString((Object[])stringBuilder1));
    stringBuilder2.append(" but was: ");
    stringBuilder2.append(i);
    stringBuilder2.append(" ");
    stringBuilder2.append(this.values);
    AssertionError assertionError = fail(stringBuilder2.toString());
    throw assertionError;
  }
  
  public final U assertValuesOnly(T... paramVarArgs) {
    return (U)assertSubscribed().assertValues(paramVarArgs).assertNoErrors().assertNotComplete();
  }
  
  public final U await() throws InterruptedException {
    if (this.done.getCount() == 0L)
      return (U)this; 
    this.done.await();
    return (U)this;
  }
  
  public final boolean await(long paramLong, TimeUnit paramTimeUnit) throws InterruptedException {
    if (this.done.getCount() == 0L || this.done.await(paramLong, paramTimeUnit)) {
      boolean bool1 = true;
      this.timeout = bool1 ^ true;
      return bool1;
    } 
    boolean bool = false;
    this.timeout = bool ^ true;
    return bool;
  }
  
  public final U awaitCount(int paramInt) {
    return awaitCount(paramInt, TestWaitStrategy.SLEEP_10MS, 5000L);
  }
  
  public final U awaitCount(int paramInt, Runnable paramRunnable) {
    return awaitCount(paramInt, paramRunnable, 5000L);
  }
  
  public final U awaitCount(int paramInt, Runnable paramRunnable, long paramLong) {
    long l = System.currentTimeMillis();
    while (true) {
      if (paramLong > 0L && System.currentTimeMillis() - l >= paramLong) {
        this.timeout = true;
      } else if (this.done.getCount() != 0L && this.values.size() < paramInt) {
        paramRunnable.run();
        continue;
      } 
      return (U)this;
    } 
  }
  
  public final U awaitDone(long paramLong, TimeUnit paramTimeUnit) {
    try {
      if (!this.done.await(paramLong, paramTimeUnit)) {
        this.timeout = true;
        dispose();
      } 
      return (U)this;
    } catch (InterruptedException interruptedException) {
      dispose();
      throw ExceptionHelper.wrapOrThrow(interruptedException);
    } 
  }
  
  public final boolean awaitTerminalEvent() {
    try {
      await();
      return true;
    } catch (InterruptedException interruptedException) {
      Thread.currentThread().interrupt();
      return false;
    } 
  }
  
  public final boolean awaitTerminalEvent(long paramLong, TimeUnit paramTimeUnit) {
    try {
      return await(paramLong, paramTimeUnit);
    } catch (InterruptedException interruptedException) {
      Thread.currentThread().interrupt();
      return false;
    } 
  }
  
  public final U clearTimeout() {
    this.timeout = false;
    return (U)this;
  }
  
  public final long completions() {
    return this.completions;
  }
  
  public final int errorCount() {
    return this.errors.size();
  }
  
  public final List<Throwable> errors() {
    return this.errors;
  }
  
  protected final AssertionError fail(String paramString) {
    StringBuilder stringBuilder = new StringBuilder(paramString.length() + 64);
    stringBuilder.append(paramString);
    stringBuilder.append(" (");
    stringBuilder.append("latch = ");
    stringBuilder.append(this.done.getCount());
    stringBuilder.append(", ");
    stringBuilder.append("values = ");
    stringBuilder.append(this.values.size());
    stringBuilder.append(", ");
    stringBuilder.append("errors = ");
    stringBuilder.append(this.errors.size());
    stringBuilder.append(", ");
    stringBuilder.append("completions = ");
    stringBuilder.append(this.completions);
    if (this.timeout)
      stringBuilder.append(", timeout!"); 
    if (isDisposed())
      stringBuilder.append(", disposed!"); 
    CharSequence charSequence = this.tag;
    if (charSequence != null) {
      stringBuilder.append(", tag = ");
      stringBuilder.append(charSequence);
    } 
    stringBuilder.append(')');
    AssertionError assertionError = new AssertionError(stringBuilder.toString());
    if (!this.errors.isEmpty())
      if (this.errors.size() == 1) {
        assertionError.initCause(this.errors.get(0));
      } else {
        assertionError.initCause((Throwable)new CompositeException(this.errors));
      }  
    return assertionError;
  }
  
  public final List<List<Object>> getEvents() {
    ArrayList<List<T>> arrayList = new ArrayList();
    arrayList.add(values());
    arrayList.add(errors());
    ArrayList<Notification> arrayList1 = new ArrayList();
    for (long l = 0L; l < this.completions; l++)
      arrayList1.add(Notification.createOnComplete()); 
    arrayList.add(arrayList1);
    return (List)arrayList;
  }
  
  public final boolean isTerminated() {
    boolean bool;
    if (this.done.getCount() == 0L) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public final boolean isTimeout() {
    return this.timeout;
  }
  
  public final Thread lastThread() {
    return this.lastThread;
  }
  
  public final int valueCount() {
    return this.values.size();
  }
  
  public final List<T> values() {
    return this.values;
  }
  
  public final U withTag(CharSequence paramCharSequence) {
    this.tag = paramCharSequence;
    return (U)this;
  }
  
  public enum TestWaitStrategy implements Runnable {
    SLEEP_1000MS,
    SLEEP_100MS,
    SLEEP_10MS,
    SLEEP_1MS,
    SPIN {
      public void run() {}
    },
    YIELD {
      public void run() {
        Thread.yield();
      }
    };
    
    static {
      SLEEP_10MS = new null("SLEEP_10MS", 3);
      SLEEP_100MS = new null("SLEEP_100MS", 4);
      SLEEP_1000MS = new null("SLEEP_1000MS", 5);
      $VALUES = new TestWaitStrategy[] { SPIN, YIELD, SLEEP_1MS, SLEEP_10MS, SLEEP_100MS, SLEEP_1000MS };
    }
    
    static void sleep(int param1Int) {
      long l = param1Int;
      try {
        Thread.sleep(l);
        return;
      } catch (InterruptedException interruptedException) {
        throw new RuntimeException(interruptedException);
      } 
    }
    
    public abstract void run();
  }
  
  enum null {
    public void run() {}
  }
  
  enum null {
    public void run() {
      Thread.yield();
    }
  }
  
  enum null {
    public void run() {
      sleep(1);
    }
  }
  
  enum null {
    public void run() {
      sleep(10);
    }
  }
  
  enum null {
    public void run() {
      sleep(100);
    }
  }
  
  enum null {
    public void run() {
      sleep(1000);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/observers/BaseTestConsumer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */