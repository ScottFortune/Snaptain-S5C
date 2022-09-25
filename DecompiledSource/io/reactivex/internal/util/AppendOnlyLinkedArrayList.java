package io.reactivex.internal.util;

import io.reactivex.Observer;
import io.reactivex.functions.BiPredicate;
import io.reactivex.functions.Predicate;
import org.reactivestreams.Subscriber;

public class AppendOnlyLinkedArrayList<T> {
  final int capacity;
  
  final Object[] head;
  
  int offset;
  
  Object[] tail;
  
  public AppendOnlyLinkedArrayList(int paramInt) {
    this.capacity = paramInt;
    this.head = new Object[paramInt + 1];
    this.tail = this.head;
  }
  
  public <U> boolean accept(Observer<? super U> paramObserver) {
    Object[] arrayOfObject = this.head;
    int i = this.capacity;
    while (true) {
      byte b = 0;
      if (arrayOfObject != null) {
        while (b < i) {
          Object object = arrayOfObject[b];
          if (object == null)
            break; 
          if (NotificationLite.acceptFull(object, paramObserver))
            return true; 
          b++;
        } 
        arrayOfObject = (Object[])arrayOfObject[i];
        continue;
      } 
      return false;
    } 
  }
  
  public <U> boolean accept(Subscriber<? super U> paramSubscriber) {
    Object[] arrayOfObject = this.head;
    int i = this.capacity;
    while (true) {
      byte b = 0;
      if (arrayOfObject != null) {
        while (b < i) {
          Object object = arrayOfObject[b];
          if (object == null)
            break; 
          if (NotificationLite.acceptFull(object, paramSubscriber))
            return true; 
          b++;
        } 
        arrayOfObject = (Object[])arrayOfObject[i];
        continue;
      } 
      return false;
    } 
  }
  
  public void add(T paramT) {
    int i = this.capacity;
    int j = this.offset;
    int k = j;
    if (j == i) {
      Object[] arrayOfObject = new Object[i + 1];
      this.tail[i] = arrayOfObject;
      this.tail = arrayOfObject;
      k = 0;
    } 
    this.tail[k] = paramT;
    this.offset = k + 1;
  }
  
  public void forEachWhile(NonThrowingPredicate<? super T> paramNonThrowingPredicate) {
    Object[] arrayOfObject = this.head;
    int i = this.capacity;
    while (arrayOfObject != null) {
      for (byte b = 0; b < i; b++) {
        Object object = arrayOfObject[b];
        if (object == null)
          break; 
        if (paramNonThrowingPredicate.test((T)object))
          return; 
      } 
      arrayOfObject = (Object[])arrayOfObject[i];
    } 
  }
  
  public <S> void forEachWhile(S paramS, BiPredicate<? super S, ? super T> paramBiPredicate) throws Exception {
    Object[] arrayOfObject = this.head;
    int i = this.capacity;
    while (true) {
      for (byte b = 0; b < i; b++) {
        Object object = arrayOfObject[b];
        if (object == null)
          return; 
        if (paramBiPredicate.test(paramS, object))
          return; 
      } 
      arrayOfObject = (Object[])arrayOfObject[i];
    } 
  }
  
  public void setFirst(T paramT) {
    this.head[0] = paramT;
  }
  
  public static interface NonThrowingPredicate<T> extends Predicate<T> {
    boolean test(T param1T);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/util/AppendOnlyLinkedArrayList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */