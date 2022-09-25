package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.Notification;
import io.reactivex.internal.subscribers.SinglePostCompleteSubscriber;
import io.reactivex.plugins.RxJavaPlugins;
import org.reactivestreams.Subscriber;

public final class FlowableMaterialize<T> extends AbstractFlowableWithUpstream<T, Notification<T>> {
  public FlowableMaterialize(Flowable<T> paramFlowable) {
    super(paramFlowable);
  }
  
  protected void subscribeActual(Subscriber<? super Notification<T>> paramSubscriber) {
    this.source.subscribe((FlowableSubscriber)new MaterializeSubscriber<T>(paramSubscriber));
  }
  
  static final class MaterializeSubscriber<T> extends SinglePostCompleteSubscriber<T, Notification<T>> {
    private static final long serialVersionUID = -3740826063558713822L;
    
    MaterializeSubscriber(Subscriber<? super Notification<T>> param1Subscriber) {
      super(param1Subscriber);
    }
    
    public void onComplete() {
      complete(Notification.createOnComplete());
    }
    
    protected void onDrop(Notification<T> param1Notification) {
      if (param1Notification.isOnError())
        RxJavaPlugins.onError(param1Notification.getError()); 
    }
    
    public void onError(Throwable param1Throwable) {
      complete(Notification.createOnError(param1Throwable));
    }
    
    public void onNext(T param1T) {
      this.produced++;
      this.downstream.onNext(Notification.createOnNext(param1T));
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/flowable/FlowableMaterialize.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */