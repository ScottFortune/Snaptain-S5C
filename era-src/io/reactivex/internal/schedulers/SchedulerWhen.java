package io.reactivex.internal.schedulers;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.functions.Function;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.UnicastProcessor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class SchedulerWhen extends Scheduler implements Disposable {
  static final Disposable DISPOSED;
  
  static final Disposable SUBSCRIBED = new SubscribedDisposable();
  
  private final Scheduler actualScheduler;
  
  private Disposable disposable;
  
  private final FlowableProcessor<Flowable<Completable>> workerProcessor;
  
  static {
    DISPOSED = Disposables.disposed();
  }
  
  public SchedulerWhen(Function<Flowable<Flowable<Completable>>, Completable> paramFunction, Scheduler paramScheduler) {
    this.actualScheduler = paramScheduler;
    this.workerProcessor = UnicastProcessor.create().toSerialized();
    try {
      return;
    } finally {
      paramFunction = null;
    } 
  }
  
  public Scheduler.Worker createWorker() {
    Scheduler.Worker worker = this.actualScheduler.createWorker();
    FlowableProcessor<ScheduledAction> flowableProcessor = UnicastProcessor.create().toSerialized();
    Flowable flowable = flowableProcessor.map(new CreateWorkerFunction(worker));
    worker = new QueueWorker(flowableProcessor, worker);
    this.workerProcessor.onNext(flowable);
    return worker;
  }
  
  public void dispose() {
    this.disposable.dispose();
  }
  
  public boolean isDisposed() {
    return this.disposable.isDisposed();
  }
  
  static final class CreateWorkerFunction implements Function<ScheduledAction, Completable> {
    final Scheduler.Worker actualWorker;
    
    CreateWorkerFunction(Scheduler.Worker param1Worker) {
      this.actualWorker = param1Worker;
    }
    
    public Completable apply(SchedulerWhen.ScheduledAction param1ScheduledAction) {
      return new WorkerCompletable(param1ScheduledAction);
    }
    
    final class WorkerCompletable extends Completable {
      final SchedulerWhen.ScheduledAction action;
      
      WorkerCompletable(SchedulerWhen.ScheduledAction param2ScheduledAction) {
        this.action = param2ScheduledAction;
      }
      
      protected void subscribeActual(CompletableObserver param2CompletableObserver) {
        param2CompletableObserver.onSubscribe(this.action);
        this.action.call(SchedulerWhen.CreateWorkerFunction.this.actualWorker, param2CompletableObserver);
      }
    }
  }
  
  final class WorkerCompletable extends Completable {
    final SchedulerWhen.ScheduledAction action;
    
    WorkerCompletable(SchedulerWhen.ScheduledAction param1ScheduledAction) {
      this.action = param1ScheduledAction;
    }
    
    protected void subscribeActual(CompletableObserver param1CompletableObserver) {
      param1CompletableObserver.onSubscribe(this.action);
      this.action.call(this.this$0.actualWorker, param1CompletableObserver);
    }
  }
  
  static class DelayedAction extends ScheduledAction {
    private final Runnable action;
    
    private final long delayTime;
    
    private final TimeUnit unit;
    
    DelayedAction(Runnable param1Runnable, long param1Long, TimeUnit param1TimeUnit) {
      this.action = param1Runnable;
      this.delayTime = param1Long;
      this.unit = param1TimeUnit;
    }
    
    protected Disposable callActual(Scheduler.Worker param1Worker, CompletableObserver param1CompletableObserver) {
      return param1Worker.schedule(new SchedulerWhen.OnCompletedAction(this.action, param1CompletableObserver), this.delayTime, this.unit);
    }
  }
  
  static class ImmediateAction extends ScheduledAction {
    private final Runnable action;
    
    ImmediateAction(Runnable param1Runnable) {
      this.action = param1Runnable;
    }
    
    protected Disposable callActual(Scheduler.Worker param1Worker, CompletableObserver param1CompletableObserver) {
      return param1Worker.schedule(new SchedulerWhen.OnCompletedAction(this.action, param1CompletableObserver));
    }
  }
  
  static class OnCompletedAction implements Runnable {
    final Runnable action;
    
    final CompletableObserver actionCompletable;
    
    OnCompletedAction(Runnable param1Runnable, CompletableObserver param1CompletableObserver) {
      this.action = param1Runnable;
      this.actionCompletable = param1CompletableObserver;
    }
    
    public void run() {
      try {
        this.action.run();
        return;
      } finally {
        this.actionCompletable.onComplete();
      } 
    }
  }
  
  static final class QueueWorker extends Scheduler.Worker {
    private final FlowableProcessor<SchedulerWhen.ScheduledAction> actionProcessor;
    
    private final Scheduler.Worker actualWorker;
    
    private final AtomicBoolean unsubscribed;
    
    QueueWorker(FlowableProcessor<SchedulerWhen.ScheduledAction> param1FlowableProcessor, Scheduler.Worker param1Worker) {
      this.actionProcessor = param1FlowableProcessor;
      this.actualWorker = param1Worker;
      this.unsubscribed = new AtomicBoolean();
    }
    
    public void dispose() {
      if (this.unsubscribed.compareAndSet(false, true)) {
        this.actionProcessor.onComplete();
        this.actualWorker.dispose();
      } 
    }
    
    public boolean isDisposed() {
      return this.unsubscribed.get();
    }
    
    public Disposable schedule(Runnable param1Runnable) {
      SchedulerWhen.ImmediateAction immediateAction = new SchedulerWhen.ImmediateAction(param1Runnable);
      this.actionProcessor.onNext(immediateAction);
      return immediateAction;
    }
    
    public Disposable schedule(Runnable param1Runnable, long param1Long, TimeUnit param1TimeUnit) {
      SchedulerWhen.DelayedAction delayedAction = new SchedulerWhen.DelayedAction(param1Runnable, param1Long, param1TimeUnit);
      this.actionProcessor.onNext(delayedAction);
      return delayedAction;
    }
  }
  
  static abstract class ScheduledAction extends AtomicReference<Disposable> implements Disposable {
    ScheduledAction() {
      super(SchedulerWhen.SUBSCRIBED);
    }
    
    void call(Scheduler.Worker param1Worker, CompletableObserver param1CompletableObserver) {
      Disposable disposable2 = get();
      if (disposable2 == SchedulerWhen.DISPOSED)
        return; 
      if (disposable2 != SchedulerWhen.SUBSCRIBED)
        return; 
      Disposable disposable1 = callActual(param1Worker, param1CompletableObserver);
      if (!compareAndSet(SchedulerWhen.SUBSCRIBED, disposable1))
        disposable1.dispose(); 
    }
    
    protected abstract Disposable callActual(Scheduler.Worker param1Worker, CompletableObserver param1CompletableObserver);
    
    public void dispose() {
      Disposable disposable = SchedulerWhen.DISPOSED;
      while (true) {
        Disposable disposable1 = get();
        if (disposable1 == SchedulerWhen.DISPOSED)
          return; 
        if (compareAndSet(disposable1, disposable)) {
          if (disposable1 != SchedulerWhen.SUBSCRIBED)
            disposable1.dispose(); 
          return;
        } 
      } 
    }
    
    public boolean isDisposed() {
      return get().isDisposed();
    }
  }
  
  static final class SubscribedDisposable implements Disposable {
    public void dispose() {}
    
    public boolean isDisposed() {
      return false;
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/schedulers/SchedulerWhen.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */