package org.reactivestreams;

import java.util.Objects;
import java.util.concurrent.Flow;

public final class FlowAdapters {
  private FlowAdapters() {
    throw new IllegalStateException("No instances!");
  }
  
  public static <T, U> Flow.Processor<T, U> toFlowProcessor(Processor<? super T, ? extends U> paramProcessor) {
    Flow.Processor<Object, Object> processor;
    Objects.requireNonNull(paramProcessor, "reactiveStreamsProcessor");
    if (paramProcessor instanceof ReactiveToFlowProcessor) {
      processor = ((ReactiveToFlowProcessor)paramProcessor).flow;
    } else if (processor instanceof Flow.Processor) {
      processor = processor;
    } else {
      processor = new FlowToReactiveProcessor<Object, Object>((Processor<?, ?>)processor);
    } 
    return (Flow.Processor)processor;
  }
  
  public static <T> Flow.Publisher<T> toFlowPublisher(Publisher<? extends T> paramPublisher) {
    Flow.Publisher<T> publisher;
    Objects.requireNonNull(paramPublisher, "reactiveStreamsPublisher");
    if (paramPublisher instanceof ReactivePublisherFromFlow) {
      publisher = ((ReactivePublisherFromFlow)paramPublisher).flow;
    } else if (publisher instanceof Flow.Publisher) {
      publisher = publisher;
    } else {
      publisher = new FlowPublisherFromReactive((Publisher<?>)publisher);
    } 
    return publisher;
  }
  
  public static <T> Flow.Subscriber<T> toFlowSubscriber(Subscriber<T> paramSubscriber) {
    Flow.Subscriber<T> subscriber;
    Objects.requireNonNull(paramSubscriber, "reactiveStreamsSubscriber");
    if (paramSubscriber instanceof ReactiveToFlowSubscriber) {
      subscriber = ((ReactiveToFlowSubscriber)paramSubscriber).flow;
    } else if (subscriber instanceof Flow.Subscriber) {
      subscriber = subscriber;
    } else {
      subscriber = new FlowToReactiveSubscriber((Subscriber<?>)subscriber);
    } 
    return subscriber;
  }
  
  public static <T, U> Processor<T, U> toProcessor(Flow.Processor<? super T, ? extends U> paramProcessor) {
    Processor<Object, Object> processor;
    Objects.requireNonNull(paramProcessor, "flowProcessor");
    if (paramProcessor instanceof FlowToReactiveProcessor) {
      processor = ((FlowToReactiveProcessor)paramProcessor).reactiveStreams;
    } else if (processor instanceof Processor) {
      processor = processor;
    } else {
      processor = new ReactiveToFlowProcessor<Object, Object>((Flow.Processor<?, ?>)processor);
    } 
    return (Processor)processor;
  }
  
  public static <T> Publisher<T> toPublisher(Flow.Publisher<? extends T> paramPublisher) {
    Publisher<T> publisher;
    Objects.requireNonNull(paramPublisher, "flowPublisher");
    if (paramPublisher instanceof FlowPublisherFromReactive) {
      publisher = ((FlowPublisherFromReactive)paramPublisher).reactiveStreams;
    } else if (publisher instanceof Publisher) {
      publisher = publisher;
    } else {
      publisher = new ReactivePublisherFromFlow((Flow.Publisher<?>)publisher);
    } 
    return publisher;
  }
  
  public static <T> Subscriber<T> toSubscriber(Flow.Subscriber<T> paramSubscriber) {
    Subscriber<T> subscriber;
    Objects.requireNonNull(paramSubscriber, "flowSubscriber");
    if (paramSubscriber instanceof FlowToReactiveSubscriber) {
      subscriber = ((FlowToReactiveSubscriber)paramSubscriber).reactiveStreams;
    } else if (subscriber instanceof Subscriber) {
      subscriber = subscriber;
    } else {
      subscriber = new ReactiveToFlowSubscriber((Flow.Subscriber<?>)subscriber);
    } 
    return subscriber;
  }
  
  static final class FlowPublisherFromReactive<T> implements Flow.Publisher<T> {
    final Publisher<? extends T> reactiveStreams;
    
    public FlowPublisherFromReactive(Publisher<? extends T> param1Publisher) {
      this.reactiveStreams = param1Publisher;
    }
    
    public void subscribe(Flow.Subscriber<? super T> param1Subscriber) {
      FlowAdapters.ReactiveToFlowSubscriber<T> reactiveToFlowSubscriber;
      Publisher<? extends T> publisher = this.reactiveStreams;
      if (param1Subscriber == null) {
        param1Subscriber = null;
      } else {
        reactiveToFlowSubscriber = new FlowAdapters.ReactiveToFlowSubscriber<T>(param1Subscriber);
      } 
      publisher.subscribe(reactiveToFlowSubscriber);
    }
  }
  
  static final class FlowToReactiveProcessor<T, U> implements Flow.Processor<T, U> {
    final Processor<? super T, ? extends U> reactiveStreams;
    
    public FlowToReactiveProcessor(Processor<? super T, ? extends U> param1Processor) {
      this.reactiveStreams = param1Processor;
    }
    
    public void onComplete() {
      this.reactiveStreams.onComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      this.reactiveStreams.onError(param1Throwable);
    }
    
    public void onNext(T param1T) {
      this.reactiveStreams.onNext(param1T);
    }
    
    public void onSubscribe(Flow.Subscription param1Subscription) {
      FlowAdapters.ReactiveToFlowSubscription reactiveToFlowSubscription;
      Processor<? super T, ? extends U> processor = this.reactiveStreams;
      if (param1Subscription == null) {
        param1Subscription = null;
      } else {
        reactiveToFlowSubscription = new FlowAdapters.ReactiveToFlowSubscription(param1Subscription);
      } 
      processor.onSubscribe(reactiveToFlowSubscription);
    }
    
    public void subscribe(Flow.Subscriber<? super U> param1Subscriber) {
      FlowAdapters.ReactiveToFlowSubscriber<U> reactiveToFlowSubscriber;
      Processor<? super T, ? extends U> processor = this.reactiveStreams;
      if (param1Subscriber == null) {
        param1Subscriber = null;
      } else {
        reactiveToFlowSubscriber = new FlowAdapters.ReactiveToFlowSubscriber<U>(param1Subscriber);
      } 
      processor.subscribe(reactiveToFlowSubscriber);
    }
  }
  
  static final class FlowToReactiveSubscriber<T> implements Flow.Subscriber<T> {
    final Subscriber<? super T> reactiveStreams;
    
    public FlowToReactiveSubscriber(Subscriber<? super T> param1Subscriber) {
      this.reactiveStreams = param1Subscriber;
    }
    
    public void onComplete() {
      this.reactiveStreams.onComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      this.reactiveStreams.onError(param1Throwable);
    }
    
    public void onNext(T param1T) {
      this.reactiveStreams.onNext(param1T);
    }
    
    public void onSubscribe(Flow.Subscription param1Subscription) {
      FlowAdapters.ReactiveToFlowSubscription reactiveToFlowSubscription;
      Subscriber<? super T> subscriber = this.reactiveStreams;
      if (param1Subscription == null) {
        param1Subscription = null;
      } else {
        reactiveToFlowSubscription = new FlowAdapters.ReactiveToFlowSubscription(param1Subscription);
      } 
      subscriber.onSubscribe(reactiveToFlowSubscription);
    }
  }
  
  static final class FlowToReactiveSubscription implements Flow.Subscription {
    final Subscription reactiveStreams;
    
    public FlowToReactiveSubscription(Subscription param1Subscription) {
      this.reactiveStreams = param1Subscription;
    }
    
    public void cancel() {
      this.reactiveStreams.cancel();
    }
    
    public void request(long param1Long) {
      this.reactiveStreams.request(param1Long);
    }
  }
  
  static final class ReactivePublisherFromFlow<T> implements Publisher<T> {
    final Flow.Publisher<? extends T> flow;
    
    public ReactivePublisherFromFlow(Flow.Publisher<? extends T> param1Publisher) {
      this.flow = param1Publisher;
    }
    
    public void subscribe(Subscriber<? super T> param1Subscriber) {
      FlowAdapters.FlowToReactiveSubscriber<T> flowToReactiveSubscriber;
      Flow.Publisher<? extends T> publisher = this.flow;
      if (param1Subscriber == null) {
        param1Subscriber = null;
      } else {
        flowToReactiveSubscriber = new FlowAdapters.FlowToReactiveSubscriber<T>(param1Subscriber);
      } 
      publisher.subscribe(flowToReactiveSubscriber);
    }
  }
  
  static final class ReactiveToFlowProcessor<T, U> implements Processor<T, U> {
    final Flow.Processor<? super T, ? extends U> flow;
    
    public ReactiveToFlowProcessor(Flow.Processor<? super T, ? extends U> param1Processor) {
      this.flow = param1Processor;
    }
    
    public void onComplete() {
      this.flow.onComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      this.flow.onError(param1Throwable);
    }
    
    public void onNext(T param1T) {
      this.flow.onNext(param1T);
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      FlowAdapters.FlowToReactiveSubscription flowToReactiveSubscription;
      Flow.Processor<? super T, ? extends U> processor = this.flow;
      if (param1Subscription == null) {
        param1Subscription = null;
      } else {
        flowToReactiveSubscription = new FlowAdapters.FlowToReactiveSubscription(param1Subscription);
      } 
      processor.onSubscribe(flowToReactiveSubscription);
    }
    
    public void subscribe(Subscriber<? super U> param1Subscriber) {
      FlowAdapters.FlowToReactiveSubscriber<U> flowToReactiveSubscriber;
      Flow.Processor<? super T, ? extends U> processor = this.flow;
      if (param1Subscriber == null) {
        param1Subscriber = null;
      } else {
        flowToReactiveSubscriber = new FlowAdapters.FlowToReactiveSubscriber<U>(param1Subscriber);
      } 
      processor.subscribe(flowToReactiveSubscriber);
    }
  }
  
  static final class ReactiveToFlowSubscriber<T> implements Subscriber<T> {
    final Flow.Subscriber<? super T> flow;
    
    public ReactiveToFlowSubscriber(Flow.Subscriber<? super T> param1Subscriber) {
      this.flow = param1Subscriber;
    }
    
    public void onComplete() {
      this.flow.onComplete();
    }
    
    public void onError(Throwable param1Throwable) {
      this.flow.onError(param1Throwable);
    }
    
    public void onNext(T param1T) {
      this.flow.onNext(param1T);
    }
    
    public void onSubscribe(Subscription param1Subscription) {
      FlowAdapters.FlowToReactiveSubscription flowToReactiveSubscription;
      Flow.Subscriber<? super T> subscriber = this.flow;
      if (param1Subscription == null) {
        param1Subscription = null;
      } else {
        flowToReactiveSubscription = new FlowAdapters.FlowToReactiveSubscription(param1Subscription);
      } 
      subscriber.onSubscribe(flowToReactiveSubscription);
    }
  }
  
  static final class ReactiveToFlowSubscription implements Subscription {
    final Flow.Subscription flow;
    
    public ReactiveToFlowSubscription(Flow.Subscription param1Subscription) {
      this.flow = param1Subscription;
    }
    
    public void cancel() {
      this.flow.cancel();
    }
    
    public void request(long param1Long) {
      this.flow.request(param1Long);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/org/reactivestreams/FlowAdapters.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */