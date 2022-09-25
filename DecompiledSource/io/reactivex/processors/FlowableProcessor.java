package io.reactivex.processors;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.annotations.CheckReturnValue;
import org.reactivestreams.Processor;

public abstract class FlowableProcessor<T> extends Flowable<T> implements Processor<T, T>, FlowableSubscriber<T> {
  public abstract Throwable getThrowable();
  
  public abstract boolean hasComplete();
  
  public abstract boolean hasSubscribers();
  
  public abstract boolean hasThrowable();
  
  @CheckReturnValue
  public final FlowableProcessor<T> toSerialized() {
    return (this instanceof SerializedProcessor) ? this : new SerializedProcessor<T>(this);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/processors/FlowableProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */