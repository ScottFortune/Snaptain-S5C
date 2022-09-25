package com.shizhefei.task;

import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import java.util.ArrayDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

abstract class AsyncTaskV25<Params, Progress, Result> {
  private static final int CORE_POOL_SIZE;
  
  private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
  
  private static final int KEEP_ALIVE_SECONDS = 30;
  
  private static final String LOG_TAG = "AsyncTaskV25";
  
  private static final int MAXIMUM_POOL_SIZE;
  
  private static final int MESSAGE_POST_PROGRESS = 2;
  
  private static final int MESSAGE_POST_RESULT = 1;
  
  public static final Executor SERIAL_EXECUTOR;
  
  public static final Executor THREAD_POOL_EXECUTOR;
  
  private static volatile Executor sDefaultExecutor;
  
  private static InternalHandler sHandler;
  
  private static final BlockingQueue<Runnable> sPoolWorkQueue;
  
  private static final ThreadFactory sThreadFactory;
  
  private final AtomicBoolean mCancelled = new AtomicBoolean();
  
  private final FutureTask<Result> mFuture = new FutureTask<Result>(this.mWorker) {
      protected void done() {
        try {
          AsyncTaskV25.this.postResultIfNotInvoked(get());
        } catch (InterruptedException interruptedException) {
          Log.w("AsyncTaskV25", interruptedException);
        } catch (ExecutionException executionException) {
          throw new RuntimeException("An error occurred while executing doInBackground()", executionException.getCause());
        } catch (CancellationException cancellationException) {
          AsyncTaskV25.this.postResultIfNotInvoked(null);
        } 
      }
    };
  
  private volatile Status mStatus = Status.PENDING;
  
  private final AtomicBoolean mTaskInvoked = new AtomicBoolean();
  
  private final WorkerRunnable<Params, Result> mWorker = new WorkerRunnable<Params, Result>() {
      public Result call() throws Exception {
        AsyncTaskV25.this.mTaskInvoked.set(true);
        Object object1 = null;
        Object object2 = object1;
        try {
          Process.setThreadPriority(10);
          object2 = object1;
          object1 = AsyncTaskV25.this.doInBackground((Object[])this.mParams);
          object2 = object1;
          Binder.flushPendingCommands();
          return (Result)object1;
        } finally {
          object1 = null;
        } 
      }
    };
  
  static {
    CORE_POOL_SIZE = Math.max(2, Math.min(CPU_COUNT - 1, 4));
    MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);
        
        public Thread newThread(Runnable param1Runnable) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("AsyncTaskV25 #");
          stringBuilder.append(this.mCount.getAndIncrement());
          return new Thread(param1Runnable, stringBuilder.toString());
        }
      };
    sPoolWorkQueue = new LinkedBlockingQueue<Runnable>(128);
    if (Build.VERSION.SDK_INT >= 11) {
      THREAD_POOL_EXECUTOR = AsyncTask.THREAD_POOL_EXECUTOR;
      SERIAL_EXECUTOR = AsyncTask.SERIAL_EXECUTOR;
    } else {
      ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, 30L, TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory);
      threadPoolExecutor.allowCoreThreadTimeOut(true);
      THREAD_POOL_EXECUTOR = threadPoolExecutor;
      SERIAL_EXECUTOR = new SerialExecutor();
    } 
    sDefaultExecutor = SERIAL_EXECUTOR;
  }
  
  public static void execute(Runnable paramRunnable) {
    sDefaultExecutor.execute(paramRunnable);
  }
  
  private void finish(Result paramResult) {
    if (isCancelled()) {
      onCancelled(paramResult);
    } else {
      onPostExecute(paramResult);
    } 
    this.mStatus = Status.FINISHED;
  }
  
  private static Handler getHandler() {
    // Byte code:
    //   0: ldc com/shizhefei/task/AsyncTaskV25
    //   2: monitorenter
    //   3: getstatic com/shizhefei/task/AsyncTaskV25.sHandler : Lcom/shizhefei/task/AsyncTaskV25$InternalHandler;
    //   6: ifnonnull -> 21
    //   9: new com/shizhefei/task/AsyncTaskV25$InternalHandler
    //   12: astore_0
    //   13: aload_0
    //   14: invokespecial <init> : ()V
    //   17: aload_0
    //   18: putstatic com/shizhefei/task/AsyncTaskV25.sHandler : Lcom/shizhefei/task/AsyncTaskV25$InternalHandler;
    //   21: getstatic com/shizhefei/task/AsyncTaskV25.sHandler : Lcom/shizhefei/task/AsyncTaskV25$InternalHandler;
    //   24: astore_0
    //   25: ldc com/shizhefei/task/AsyncTaskV25
    //   27: monitorexit
    //   28: aload_0
    //   29: areturn
    //   30: astore_0
    //   31: ldc com/shizhefei/task/AsyncTaskV25
    //   33: monitorexit
    //   34: aload_0
    //   35: athrow
    // Exception table:
    //   from	to	target	type
    //   3	21	30	finally
    //   21	28	30	finally
    //   31	34	30	finally
  }
  
  private Result postResult(Result paramResult) {
    getHandler().obtainMessage(1, new AsyncTaskResult(this, new Object[] { paramResult })).sendToTarget();
    return paramResult;
  }
  
  private void postResultIfNotInvoked(Result paramResult) {
    if (!this.mTaskInvoked.get())
      postResult(paramResult); 
  }
  
  public static void setDefaultExecutor(Executor paramExecutor) {
    sDefaultExecutor = paramExecutor;
  }
  
  public final boolean cancel(boolean paramBoolean) {
    this.mCancelled.set(true);
    return this.mFuture.cancel(paramBoolean);
  }
  
  protected abstract Result doInBackground(Params... paramVarArgs);
  
  public final AsyncTaskV25<Params, Progress, Result> execute(Params... paramVarArgs) {
    return executeOnExecutor(sDefaultExecutor, paramVarArgs);
  }
  
  public final AsyncTaskV25<Params, Progress, Result> executeOnExecutor(Executor paramExecutor, Params... paramVarArgs) {
    if (this.mStatus != Status.PENDING) {
      int i = null.$SwitchMap$com$shizhefei$task$AsyncTaskV25$Status[this.mStatus.ordinal()];
      if (i != 1) {
        if (i == 2)
          throw new IllegalStateException("Cannot execute task: the task has already been executed (a task can be executed only once)"); 
      } else {
        throw new IllegalStateException("Cannot execute task: the task is already running.");
      } 
    } 
    this.mStatus = Status.RUNNING;
    onPreExecute();
    this.mWorker.mParams = paramVarArgs;
    paramExecutor.execute(this.mFuture);
    return this;
  }
  
  public final Result get() throws InterruptedException, ExecutionException {
    return this.mFuture.get();
  }
  
  public final Result get(long paramLong, TimeUnit paramTimeUnit) throws InterruptedException, ExecutionException, TimeoutException {
    return this.mFuture.get(paramLong, paramTimeUnit);
  }
  
  public final Status getStatus() {
    return this.mStatus;
  }
  
  public final boolean isCancelled() {
    return this.mCancelled.get();
  }
  
  protected void onCancelled() {}
  
  protected void onCancelled(Result paramResult) {
    onCancelled();
  }
  
  protected void onPostExecute(Result paramResult) {}
  
  protected void onPreExecute() {}
  
  protected void onProgressUpdate(Progress... paramVarArgs) {}
  
  protected final void publishProgress(Progress... paramVarArgs) {
    if (!isCancelled())
      getHandler().obtainMessage(2, new AsyncTaskResult<Progress>(this, paramVarArgs)).sendToTarget(); 
  }
  
  private static class AsyncTaskResult<Data> {
    final Data[] mData;
    
    final AsyncTaskV25 mTask;
    
    AsyncTaskResult(AsyncTaskV25 param1AsyncTaskV25, Data... param1VarArgs) {
      this.mTask = param1AsyncTaskV25;
      this.mData = param1VarArgs;
    }
  }
  
  private static class InternalHandler extends Handler {
    public InternalHandler() {
      super(Looper.getMainLooper());
    }
    
    public void handleMessage(Message param1Message) {
      AsyncTaskV25.AsyncTaskResult asyncTaskResult = (AsyncTaskV25.AsyncTaskResult)param1Message.obj;
      int i = param1Message.what;
      if (i != 1) {
        if (i == 2)
          asyncTaskResult.mTask.onProgressUpdate((Object[])asyncTaskResult.mData); 
      } else {
        asyncTaskResult.mTask.finish((Result)asyncTaskResult.mData[0]);
      } 
    }
  }
  
  private static class SerialExecutor implements Executor {
    Runnable mActive;
    
    final ArrayDeque<Runnable> mTasks = new ArrayDeque<Runnable>();
    
    private SerialExecutor() {}
    
    public void execute(Runnable param1Runnable) {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: getfield mTasks : Ljava/util/ArrayDeque;
      //   6: astore_2
      //   7: new com/shizhefei/task/AsyncTaskV25$SerialExecutor$1
      //   10: astore_3
      //   11: aload_3
      //   12: aload_0
      //   13: aload_1
      //   14: invokespecial <init> : (Lcom/shizhefei/task/AsyncTaskV25$SerialExecutor;Ljava/lang/Runnable;)V
      //   17: aload_2
      //   18: aload_3
      //   19: invokevirtual offer : (Ljava/lang/Object;)Z
      //   22: pop
      //   23: aload_0
      //   24: getfield mActive : Ljava/lang/Runnable;
      //   27: ifnonnull -> 34
      //   30: aload_0
      //   31: invokevirtual scheduleNext : ()V
      //   34: aload_0
      //   35: monitorexit
      //   36: return
      //   37: astore_1
      //   38: aload_0
      //   39: monitorexit
      //   40: aload_1
      //   41: athrow
      // Exception table:
      //   from	to	target	type
      //   2	34	37	finally
    }
    
    protected void scheduleNext() {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: getfield mTasks : Ljava/util/ArrayDeque;
      //   6: invokevirtual poll : ()Ljava/lang/Object;
      //   9: checkcast java/lang/Runnable
      //   12: astore_1
      //   13: aload_0
      //   14: aload_1
      //   15: putfield mActive : Ljava/lang/Runnable;
      //   18: aload_1
      //   19: ifnull -> 34
      //   22: getstatic com/shizhefei/task/AsyncTaskV25.THREAD_POOL_EXECUTOR : Ljava/util/concurrent/Executor;
      //   25: aload_0
      //   26: getfield mActive : Ljava/lang/Runnable;
      //   29: invokeinterface execute : (Ljava/lang/Runnable;)V
      //   34: aload_0
      //   35: monitorexit
      //   36: return
      //   37: astore_1
      //   38: aload_0
      //   39: monitorexit
      //   40: aload_1
      //   41: athrow
      // Exception table:
      //   from	to	target	type
      //   2	18	37	finally
      //   22	34	37	finally
    }
  }
  
  class null implements Runnable {
    public void run() {
      try {
        r.run();
        return;
      } finally {
        this.this$0.scheduleNext();
      } 
    }
  }
  
  public enum Status {
    FINISHED, PENDING, RUNNING;
    
    static {
      $VALUES = new Status[] { PENDING, RUNNING, FINISHED };
    }
  }
  
  private static abstract class WorkerRunnable<Params, Result> implements Callable<Result> {
    Params[] mParams;
    
    private WorkerRunnable() {}
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/shizhefei/task/AsyncTaskV25.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */