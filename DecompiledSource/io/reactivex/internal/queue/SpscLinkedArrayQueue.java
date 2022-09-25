package io.reactivex.internal.queue;

import io.reactivex.internal.fuseable.SimplePlainQueue;
import io.reactivex.internal.util.Pow2;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReferenceArray;

public final class SpscLinkedArrayQueue<T> implements SimplePlainQueue<T> {
  private static final Object HAS_NEXT;
  
  static final int MAX_LOOK_AHEAD_STEP = Integer.getInteger("jctools.spsc.max.lookahead.step", 4096).intValue();
  
  AtomicReferenceArray<Object> consumerBuffer;
  
  final AtomicLong consumerIndex = new AtomicLong();
  
  final int consumerMask;
  
  AtomicReferenceArray<Object> producerBuffer;
  
  final AtomicLong producerIndex = new AtomicLong();
  
  long producerLookAhead;
  
  int producerLookAheadStep;
  
  final int producerMask;
  
  static {
    HAS_NEXT = new Object();
  }
  
  public SpscLinkedArrayQueue(int paramInt) {
    int i = Pow2.roundToPowerOfTwo(Math.max(8, paramInt));
    paramInt = i - 1;
    AtomicReferenceArray<Object> atomicReferenceArray = new AtomicReferenceArray(i + 1);
    this.producerBuffer = atomicReferenceArray;
    this.producerMask = paramInt;
    adjustLookAheadStep(i);
    this.consumerBuffer = atomicReferenceArray;
    this.consumerMask = paramInt;
    this.producerLookAhead = (paramInt - 1);
    soProducerIndex(0L);
  }
  
  private void adjustLookAheadStep(int paramInt) {
    this.producerLookAheadStep = Math.min(paramInt / 4, MAX_LOOK_AHEAD_STEP);
  }
  
  private static int calcDirectOffset(int paramInt) {
    return paramInt;
  }
  
  private static int calcWrappedOffset(long paramLong, int paramInt) {
    return calcDirectOffset((int)paramLong & paramInt);
  }
  
  private long lpConsumerIndex() {
    return this.consumerIndex.get();
  }
  
  private long lpProducerIndex() {
    return this.producerIndex.get();
  }
  
  private long lvConsumerIndex() {
    return this.consumerIndex.get();
  }
  
  private static <E> Object lvElement(AtomicReferenceArray<Object> paramAtomicReferenceArray, int paramInt) {
    return paramAtomicReferenceArray.get(paramInt);
  }
  
  private AtomicReferenceArray<Object> lvNextBufferAndUnlink(AtomicReferenceArray<Object> paramAtomicReferenceArray, int paramInt) {
    paramInt = calcDirectOffset(paramInt);
    AtomicReferenceArray<Object> atomicReferenceArray = (AtomicReferenceArray)lvElement(paramAtomicReferenceArray, paramInt);
    soElement(paramAtomicReferenceArray, paramInt, null);
    return atomicReferenceArray;
  }
  
  private long lvProducerIndex() {
    return this.producerIndex.get();
  }
  
  private T newBufferPeek(AtomicReferenceArray<Object> paramAtomicReferenceArray, long paramLong, int paramInt) {
    this.consumerBuffer = paramAtomicReferenceArray;
    return (T)lvElement(paramAtomicReferenceArray, calcWrappedOffset(paramLong, paramInt));
  }
  
  private T newBufferPoll(AtomicReferenceArray<Object> paramAtomicReferenceArray, long paramLong, int paramInt) {
    this.consumerBuffer = paramAtomicReferenceArray;
    paramInt = calcWrappedOffset(paramLong, paramInt);
    Object object = lvElement(paramAtomicReferenceArray, paramInt);
    if (object != null) {
      soElement(paramAtomicReferenceArray, paramInt, null);
      soConsumerIndex(paramLong + 1L);
    } 
    return (T)object;
  }
  
  private void resize(AtomicReferenceArray<Object> paramAtomicReferenceArray, long paramLong1, int paramInt, T paramT, long paramLong2) {
    AtomicReferenceArray<Object> atomicReferenceArray = new AtomicReferenceArray(paramAtomicReferenceArray.length());
    this.producerBuffer = atomicReferenceArray;
    this.producerLookAhead = paramLong2 + paramLong1 - 1L;
    soElement(atomicReferenceArray, paramInt, paramT);
    soNext(paramAtomicReferenceArray, atomicReferenceArray);
    soElement(paramAtomicReferenceArray, paramInt, HAS_NEXT);
    soProducerIndex(paramLong1 + 1L);
  }
  
  private void soConsumerIndex(long paramLong) {
    this.consumerIndex.lazySet(paramLong);
  }
  
  private static void soElement(AtomicReferenceArray<Object> paramAtomicReferenceArray, int paramInt, Object paramObject) {
    paramAtomicReferenceArray.lazySet(paramInt, paramObject);
  }
  
  private void soNext(AtomicReferenceArray<Object> paramAtomicReferenceArray1, AtomicReferenceArray<Object> paramAtomicReferenceArray2) {
    soElement(paramAtomicReferenceArray1, calcDirectOffset(paramAtomicReferenceArray1.length() - 1), paramAtomicReferenceArray2);
  }
  
  private void soProducerIndex(long paramLong) {
    this.producerIndex.lazySet(paramLong);
  }
  
  private boolean writeToQueue(AtomicReferenceArray<Object> paramAtomicReferenceArray, T paramT, long paramLong, int paramInt) {
    soElement(paramAtomicReferenceArray, paramInt, paramT);
    soProducerIndex(paramLong + 1L);
    return true;
  }
  
  public void clear() {
    while (poll() != null || !isEmpty());
  }
  
  public boolean isEmpty() {
    boolean bool;
    if (lvProducerIndex() == lvConsumerIndex()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public boolean offer(T paramT) {
    if (paramT != null) {
      AtomicReferenceArray<Object> atomicReferenceArray = this.producerBuffer;
      long l1 = lpProducerIndex();
      int i = this.producerMask;
      int j = calcWrappedOffset(l1, i);
      if (l1 < this.producerLookAhead)
        return writeToQueue(atomicReferenceArray, paramT, l1, j); 
      long l2 = this.producerLookAheadStep + l1;
      if (lvElement(atomicReferenceArray, calcWrappedOffset(l2, i)) == null) {
        this.producerLookAhead = l2 - 1L;
        return writeToQueue(atomicReferenceArray, paramT, l1, j);
      } 
      if (lvElement(atomicReferenceArray, calcWrappedOffset(1L + l1, i)) == null)
        return writeToQueue(atomicReferenceArray, paramT, l1, j); 
      resize(atomicReferenceArray, l1, j, paramT, i);
      return true;
    } 
    throw new NullPointerException("Null is not a valid element");
  }
  
  public boolean offer(T paramT1, T paramT2) {
    AtomicReferenceArray<Object> atomicReferenceArray = this.producerBuffer;
    long l1 = lvProducerIndex();
    int i = this.producerMask;
    long l2 = 2L + l1;
    if (lvElement(atomicReferenceArray, calcWrappedOffset(l2, i)) == null) {
      i = calcWrappedOffset(l1, i);
      soElement(atomicReferenceArray, i + 1, paramT2);
      soElement(atomicReferenceArray, i, paramT1);
      soProducerIndex(l2);
    } else {
      AtomicReferenceArray<Object> atomicReferenceArray1 = new AtomicReferenceArray(atomicReferenceArray.length());
      this.producerBuffer = atomicReferenceArray1;
      i = calcWrappedOffset(l1, i);
      soElement(atomicReferenceArray1, i + 1, paramT2);
      soElement(atomicReferenceArray1, i, paramT1);
      soNext(atomicReferenceArray, atomicReferenceArray1);
      soElement(atomicReferenceArray, i, HAS_NEXT);
      soProducerIndex(l2);
    } 
    return true;
  }
  
  public T peek() {
    AtomicReferenceArray<Object> atomicReferenceArray = this.consumerBuffer;
    long l = lpConsumerIndex();
    int i = this.consumerMask;
    Object object = lvElement(atomicReferenceArray, calcWrappedOffset(l, i));
    return (T)((object == HAS_NEXT) ? (Object)newBufferPeek(lvNextBufferAndUnlink(atomicReferenceArray, i + 1), l, i) : object);
  }
  
  public T poll() {
    boolean bool;
    AtomicReferenceArray<Object> atomicReferenceArray = this.consumerBuffer;
    long l = lpConsumerIndex();
    int i = this.consumerMask;
    int j = calcWrappedOffset(l, i);
    Object object = lvElement(atomicReferenceArray, j);
    if (object == HAS_NEXT) {
      bool = true;
    } else {
      bool = false;
    } 
    if (object != null && !bool) {
      soElement(atomicReferenceArray, j, null);
      soConsumerIndex(l + 1L);
      return (T)object;
    } 
    return bool ? newBufferPoll(lvNextBufferAndUnlink(atomicReferenceArray, i + 1), l, i) : null;
  }
  
  public int size() {
    for (long l = lvConsumerIndex();; l = l2) {
      long l1 = lvProducerIndex();
      long l2 = lvConsumerIndex();
      if (l == l2)
        return (int)(l1 - l2); 
    } 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/queue/SpscLinkedArrayQueue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */