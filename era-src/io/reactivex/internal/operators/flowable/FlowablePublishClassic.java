package io.reactivex.internal.operators.flowable;

import org.reactivestreams.Publisher;

public interface FlowablePublishClassic<T> {
  int publishBufferSize();
  
  Publisher<T> publishSource();
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowablePublishClassic.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */