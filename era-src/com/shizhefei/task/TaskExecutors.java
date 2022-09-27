package com.shizhefei.task;

import android.os.Handler;
import android.os.Looper;
import com.shizhefei.mvc.IAsyncDataSource;
import com.shizhefei.mvc.IDataSource;
import com.shizhefei.mvc.ProgressSender;
import com.shizhefei.mvc.RequestHandle;
import com.shizhefei.mvc.ResponseSender;
import com.shizhefei.utils.TaskLogUtil;
import java.util.concurrent.Executor;

class TaskExecutors {
  public static <DATA> TaskExecutor<DATA> create(IAsyncDataSource<DATA> paramIAsyncDataSource, boolean paramBoolean, ICallback<DATA> paramICallback) {
    return new AsyncDataSourceExecutor<DATA>(paramIAsyncDataSource, paramBoolean, paramICallback);
  }
  
  public static <DATA> TaskExecutor<DATA> create(IDataSource<DATA> paramIDataSource, boolean paramBoolean, ICallback<DATA> paramICallback) {
    return create(paramIDataSource, paramBoolean, paramICallback, AsyncTaskV25.THREAD_POOL_EXECUTOR);
  }
  
  public static <DATA> TaskExecutor<DATA> create(IDataSource<DATA> paramIDataSource, boolean paramBoolean, ICallback<DATA> paramICallback, Executor paramExecutor) {
    return new SyncDataSourceExecutor<DATA>(paramIDataSource, paramBoolean, paramICallback, paramExecutor);
  }
  
  public static <DATA> TaskExecutor<DATA> create(IAsyncTask<DATA> paramIAsyncTask, ICallback<DATA> paramICallback) {
    return new AsyncTaskExecutor<DATA>(paramIAsyncTask, paramICallback);
  }
  
  public static <DATA> TaskExecutor<DATA> create(ITask<DATA> paramITask, ICallback<DATA> paramICallback) {
    return create(paramITask, paramICallback, AsyncTaskV25.THREAD_POOL_EXECUTOR);
  }
  
  public static <DATA> TaskExecutor<DATA> create(ITask<DATA> paramITask, ICallback<DATA> paramICallback, Executor paramExecutor) {
    return new SyncTaskExecutor<DATA>(paramITask, paramICallback, paramExecutor);
  }
  
  private static abstract class AbsAsyncTaskExecutor<DATA> implements TaskExecutor<DATA> {
    private ICallback<DATA> callback;
    
    private final ISuperTask<DATA> realTask;
    
    private RequestHandle requestHandle;
    
    private TaskExecutors.TaskResponseSender<DATA> responseSender;
    
    public AbsAsyncTaskExecutor(ISuperTask<DATA> param1ISuperTask, ICallback<DATA> param1ICallback) {
      this.realTask = param1ISuperTask;
      this.callback = param1ICallback;
      if (param1ICallback == null) {
        this.responseSender = new TaskExecutors.TaskNoCallbackResponseSender<DATA>();
      } else {
        this.responseSender = new TaskExecutors.TaskHasCallbackResponseSender<DATA>();
      } 
    }
    
    public void cancle() {
      RequestHandle requestHandle = this.requestHandle;
      if (requestHandle != null)
        requestHandle.cancle(); 
      this.responseSender.sendCancel();
    }
    
    public final RequestHandle execute() {
      this.responseSender.sendPreExecute(this.realTask, this.callback);
      try {
        this.requestHandle = executeImp(this.responseSender);
      } catch (Exception exception) {
        this.responseSender.sendError(exception);
      } 
      return this;
    }
    
    protected abstract RequestHandle executeImp(ResponseSender<DATA> param1ResponseSender) throws Exception;
    
    public ICallback<DATA> getCallback() {
      return this.callback;
    }
    
    public ISuperTask<DATA> getTask() {
      return this.realTask;
    }
    
    public boolean isRunning() {
      return this.responseSender.isRunning();
    }
  }
  
  private static abstract class AbsSyncTaskExecutor<DATA> extends AsyncTaskV25<Object, Object, DATA> implements TaskExecutor<DATA> {
    private ICallback<DATA> callback;
    
    private final Executor executor;
    
    private final ISuperTask<DATA> realTask;
    
    private final TaskExecutors.TaskResponseSender<DATA> responseSender;
    
    public AbsSyncTaskExecutor(ISuperTask<DATA> param1ISuperTask, ICallback<DATA> param1ICallback, Executor param1Executor) {
      this.callback = param1ICallback;
      this.realTask = param1ISuperTask;
      this.executor = param1Executor;
      if (param1ICallback == null) {
        this.responseSender = new TaskExecutors.TaskNoCallbackResponseSender<DATA>();
      } else {
        this.responseSender = new TaskExecutors.TaskHasCallbackResponseSender<DATA>();
      } 
    }
    
    protected abstract void cancelImp();
    
    public void cancle() {
      cancelImp();
      cancel(true);
      this.responseSender.sendCancel();
    }
    
    protected DATA doInBackground(Object... param1VarArgs) {
      try {
        return executeImp((ProgressSender)this.responseSender);
      } catch (Exception exception) {
        this.responseSender.sendError(exception);
        return null;
      } 
    }
    
    public RequestHandle execute() {
      this.responseSender.sendPreExecute(this.realTask, this.callback);
      executeOnExecutor(this.executor, new Object[] { Boolean.TRUE });
      return this;
    }
    
    protected abstract DATA executeImp(ProgressSender param1ProgressSender) throws Exception;
    
    public ICallback<DATA> getCallback() {
      return this.callback;
    }
    
    public ISuperTask<DATA> getTask() {
      return this.realTask;
    }
    
    public boolean isRunning() {
      return this.responseSender.isRunning();
    }
    
    protected void onPostExecute(DATA param1DATA) {
      super.onPostExecute(param1DATA);
      this.responseSender.sendData(param1DATA);
    }
  }
  
  private static class AsyncDataSourceExecutor<DATA> extends AbsAsyncTaskExecutor<DATA> {
    private IAsyncDataSource<DATA> dataSource;
    
    private final boolean isExeRefresh;
    
    public AsyncDataSourceExecutor(IAsyncDataSource<DATA> param1IAsyncDataSource, boolean param1Boolean, ICallback<DATA> param1ICallback) {
      super((ISuperTask<DATA>)param1IAsyncDataSource, param1ICallback);
      this.dataSource = param1IAsyncDataSource;
      this.isExeRefresh = param1Boolean;
    }
    
    protected RequestHandle executeImp(ResponseSender<DATA> param1ResponseSender) throws Exception {
      return this.isExeRefresh ? this.dataSource.refresh(param1ResponseSender) : this.dataSource.loadMore(param1ResponseSender);
    }
    
    public boolean isExeRefresh() {
      return this.isExeRefresh;
    }
  }
  
  private static class AsyncTaskExecutor<DATA> extends AbsAsyncTaskExecutor<DATA> {
    private IAsyncTask<DATA> task;
    
    public AsyncTaskExecutor(IAsyncTask<DATA> param1IAsyncTask, ICallback<DATA> param1ICallback) {
      super(param1IAsyncTask, param1ICallback);
      this.task = param1IAsyncTask;
    }
    
    protected RequestHandle executeImp(ResponseSender<DATA> param1ResponseSender) throws Exception {
      return this.task.execute(param1ResponseSender);
    }
    
    public boolean isExeRefresh() {
      return true;
    }
  }
  
  private static class SyncDataSourceExecutor<DATA> extends AbsSyncTaskExecutor<DATA> {
    private final boolean isExeRefresh;
    
    private IDataSource<DATA> task;
    
    public SyncDataSourceExecutor(IDataSource<DATA> param1IDataSource, boolean param1Boolean, ICallback<DATA> param1ICallback, Executor param1Executor) {
      super((ISuperTask<DATA>)param1IDataSource, param1ICallback, param1Executor);
      this.task = param1IDataSource;
      this.isExeRefresh = param1Boolean;
    }
    
    protected void cancelImp() {}
    
    protected DATA executeImp(ProgressSender param1ProgressSender) throws Exception {
      return (DATA)(this.isExeRefresh ? this.task.refresh() : this.task.loadMore());
    }
    
    public boolean isExeRefresh() {
      return this.isExeRefresh;
    }
  }
  
  private static class SyncTaskExecutor<DATA> extends AbsSyncTaskExecutor<DATA> {
    private ITask<DATA> task;
    
    public SyncTaskExecutor(ITask<DATA> param1ITask, ICallback<DATA> param1ICallback, Executor param1Executor) {
      super(param1ITask, param1ICallback, param1Executor);
      this.task = param1ITask;
    }
    
    protected void cancelImp() {
      this.task.cancel();
    }
    
    protected DATA executeImp(ProgressSender param1ProgressSender) throws Exception {
      return this.task.execute(param1ProgressSender);
    }
    
    public boolean isExeRefresh() {
      return true;
    }
  }
  
  private static class TaskHasCallbackResponseSender<DATA> implements TaskResponseSender<DATA> {
    private ICallback<DATA> callback;
    
    private Handler handler = new Handler(Looper.getMainLooper());
    
    private volatile boolean isRunning;
    
    private Object realTask;
    
    private void onPostExecute(final Code code, final Exception exception, final DATA data) {
      if (Looper.myLooper() != Looper.getMainLooper()) {
        this.handler.post(new Runnable() {
              public void run() {
                TaskExecutors.TaskHasCallbackResponseSender.this.onPostExecuteMainThread(code, exception, (DATA)data);
              }
            });
      } else {
        onPostExecuteMainThread(code, exception, data);
      } 
    }
    
    private void onPostExecuteMainThread(Code param1Code, Exception param1Exception, DATA param1DATA) {
      if (!this.isRunning)
        return; 
      this.isRunning = false;
      if (param1Exception == null) {
        TaskLogUtil.d("{} task={} code={} data={}", new Object[] { "执行结果", this.realTask, param1Code, param1DATA });
      } else {
        TaskLogUtil.e("{} task={} code={} exception={}", new Object[] { "执行结果", this.realTask, param1Code, param1Exception });
      } 
      ICallback<DATA> iCallback = this.callback;
      if (iCallback != null)
        iCallback.onPostExecute(this.realTask, param1Code, param1Exception, param1DATA); 
      this.realTask = null;
      this.callback = null;
    }
    
    private void onPreExecute() {
      if (Looper.myLooper() != Looper.getMainLooper()) {
        this.handler.post(new Runnable() {
              public void run() {
                TaskExecutors.TaskHasCallbackResponseSender.this.onPreExecuteMainThread();
              }
            });
      } else {
        onPreExecuteMainThread();
      } 
    }
    
    private void onPreExecuteMainThread() {
      ICallback<DATA> iCallback = this.callback;
      if (iCallback != null)
        iCallback.onPreExecute(this.realTask); 
    }
    
    private void onProgress(final int percent, final long current, final long total, final Object extraData) {
      if (Looper.myLooper() != Looper.getMainLooper()) {
        this.handler.post(new Runnable() {
              public void run() {
                TaskExecutors.TaskHasCallbackResponseSender.this.onProgressMainThread(percent, current, total, extraData);
              }
            });
      } else {
        onProgressMainThread(percent, current, total, extraData);
      } 
    }
    
    private void onProgressMainThread(int param1Int, long param1Long1, long param1Long2, Object param1Object) {
      ICallback<DATA> iCallback = this.callback;
      if (iCallback != null)
        iCallback.onProgress(this.realTask, param1Int, param1Long1, param1Long2, param1Object); 
    }
    
    public boolean isRunning() {
      return this.isRunning;
    }
    
    public void sendCancel() {
      onPostExecute(Code.CANCEL, null, null);
    }
    
    public void sendData(DATA param1DATA) {
      onPostExecute(Code.SUCCESS, null, param1DATA);
    }
    
    public void sendError(Exception param1Exception) {
      onPostExecute(Code.EXCEPTION, param1Exception, null);
    }
    
    public void sendPreExecute(Object param1Object, ICallback<DATA> param1ICallback) {
      this.realTask = param1Object;
      this.callback = param1ICallback;
      this.isRunning = true;
      onPreExecute();
    }
    
    public void sendProgress(long param1Long1, long param1Long2, Object param1Object) {
      int i;
      if (param1Long1 == 0L || param1Long2 == 0L) {
        i = 0;
      } else {
        i = (int)(100L * param1Long1 / param1Long2);
      } 
      onProgress(i, param1Long1, param1Long2, param1Object);
    }
  }
  
  class null implements Runnable {
    public void run() {
      this.this$0.onPreExecuteMainThread();
    }
  }
  
  class null implements Runnable {
    public void run() {
      this.this$0.onProgressMainThread(percent, current, total, extraData);
    }
  }
  
  class null implements Runnable {
    public void run() {
      this.this$0.onPostExecuteMainThread(code, exception, (DATA)data);
    }
  }
  
  private static class TaskNoCallbackResponseSender<DATA> implements TaskResponseSender<DATA> {
    private volatile boolean isRunning;
    
    private TaskNoCallbackResponseSender() {}
    
    public boolean isRunning() {
      return this.isRunning;
    }
    
    public void sendCancel() {
      this.isRunning = false;
    }
    
    public void sendData(DATA param1DATA) {
      this.isRunning = false;
    }
    
    public void sendError(Exception param1Exception) {
      this.isRunning = false;
    }
    
    public void sendPreExecute(Object param1Object, ICallback<DATA> param1ICallback) {
      this.isRunning = true;
    }
    
    public void sendProgress(long param1Long1, long param1Long2, Object param1Object) {}
  }
  
  private static interface TaskResponseSender<DATA> extends ResponseSender<DATA> {
    boolean isRunning();
    
    void sendCancel();
    
    void sendPreExecute(Object param1Object, ICallback<DATA> param1ICallback);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/shizhefei/task/TaskExecutors.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */