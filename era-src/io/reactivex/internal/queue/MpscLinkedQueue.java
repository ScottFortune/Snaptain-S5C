package io.reactivex.internal.queue;

import io.reactivex.internal.fuseable.SimplePlainQueue;
import java.util.concurrent.atomic.AtomicReference;

public final class MpscLinkedQueue<T> implements SimplePlainQueue<T> {
  private final AtomicReference<LinkedQueueNode<T>> consumerNode = new AtomicReference<LinkedQueueNode<T>>();
  
  private final AtomicReference<LinkedQueueNode<T>> producerNode = new AtomicReference<LinkedQueueNode<T>>();
  
  public MpscLinkedQueue() {
    LinkedQueueNode<T> linkedQueueNode = new LinkedQueueNode();
    spConsumerNode(linkedQueueNode);
    xchgProducerNode(linkedQueueNode);
  }
  
  public void clear() {
    while (poll() != null && !isEmpty());
  }
  
  public boolean isEmpty() {
    boolean bool;
    if (lvConsumerNode() == lvProducerNode()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  LinkedQueueNode<T> lpConsumerNode() {
    return this.consumerNode.get();
  }
  
  LinkedQueueNode<T> lvConsumerNode() {
    return this.consumerNode.get();
  }
  
  LinkedQueueNode<T> lvProducerNode() {
    return this.producerNode.get();
  }
  
  public boolean offer(T paramT) {
    if (paramT != null) {
      LinkedQueueNode<T> linkedQueueNode = new LinkedQueueNode<T>(paramT);
      xchgProducerNode(linkedQueueNode).soNext(linkedQueueNode);
      return true;
    } 
    throw new NullPointerException("Null is not a valid element");
  }
  
  public boolean offer(T paramT1, T paramT2) {
    offer(paramT1);
    offer(paramT2);
    return true;
  }
  
  public T poll() {
    LinkedQueueNode<T> linkedQueueNode1 = lpConsumerNode();
    LinkedQueueNode<T> linkedQueueNode2 = linkedQueueNode1.lvNext();
    if (linkedQueueNode2 != null) {
      linkedQueueNode1 = (LinkedQueueNode<T>)linkedQueueNode2.getAndNullValue();
      spConsumerNode(linkedQueueNode2);
      return (T)linkedQueueNode1;
    } 
    if (linkedQueueNode1 != lvProducerNode())
      while (true) {
        linkedQueueNode2 = linkedQueueNode1.lvNext();
        if (linkedQueueNode2 == null)
          continue; 
        linkedQueueNode1 = (LinkedQueueNode<T>)linkedQueueNode2.getAndNullValue();
        spConsumerNode(linkedQueueNode2);
        return (T)linkedQueueNode1;
      }  
    return null;
  }
  
  void spConsumerNode(LinkedQueueNode<T> paramLinkedQueueNode) {
    this.consumerNode.lazySet(paramLinkedQueueNode);
  }
  
  LinkedQueueNode<T> xchgProducerNode(LinkedQueueNode<T> paramLinkedQueueNode) {
    return this.producerNode.getAndSet(paramLinkedQueueNode);
  }
  
  static final class LinkedQueueNode<E> extends AtomicReference<LinkedQueueNode<E>> {
    private static final long serialVersionUID = 2404266111789071508L;
    
    private E value;
    
    LinkedQueueNode() {}
    
    LinkedQueueNode(E param1E) {
      spValue(param1E);
    }
    
    public E getAndNullValue() {
      E e = lpValue();
      spValue((E)null);
      return e;
    }
    
    public E lpValue() {
      return this.value;
    }
    
    public LinkedQueueNode<E> lvNext() {
      return get();
    }
    
    public void soNext(LinkedQueueNode<E> param1LinkedQueueNode) {
      lazySet(param1LinkedQueueNode);
    }
    
    public void spValue(E param1E) {
      this.value = param1E;
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/queue/MpscLinkedQueue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */