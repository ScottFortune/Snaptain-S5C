package com.shizhefei.task;

import android.text.TextUtils;
import com.shizhefei.mvc.IAsyncDataSource;
import com.shizhefei.mvc.IDataSource;
import com.shizhefei.mvc.RequestHandle;
import com.shizhefei.task.imp.MemoryCacheStore;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;

public class TaskHelper<BASE_DATA> implements RequestHandle {
  private ICacheStore cacheStore;
  
  private Executor executor;
  
  private Set<ICallback<BASE_DATA>> registerCallBacks = new LinkedHashSet<ICallback<BASE_DATA>>();
  
  private List<MultiTaskBindProxyCallBack<?, BASE_DATA>> taskImps = new LinkedList<MultiTaskBindProxyCallBack<?, BASE_DATA>>();
  
  public TaskHelper() {
    this((ICacheStore)new MemoryCacheStore(100));
  }
  
  public TaskHelper(ICacheStore paramICacheStore) {
    this.cacheStore = paramICacheStore;
    this.executor = AsyncTaskV25.THREAD_POOL_EXECUTOR;
  }
  
  public static <DATA> TaskExecutor<DATA> create(IDataSource<DATA> paramIDataSource, boolean paramBoolean, ICallback<DATA> paramICallback, Executor paramExecutor) {
    return TaskExecutors.create(paramIDataSource, paramBoolean, paramICallback, paramExecutor);
  }
  
  public static <DATA> TaskExecutor<DATA> create(ITask<DATA> paramITask, ICallback<DATA> paramICallback, Executor paramExecutor) {
    return TaskExecutors.create(paramITask, paramICallback, paramExecutor);
  }
  
  public static <DATA> TaskExecutor<DATA> createExecutor(IAsyncDataSource<DATA> paramIAsyncDataSource, boolean paramBoolean, ICallback<DATA> paramICallback) {
    return TaskExecutors.create(paramIAsyncDataSource, paramBoolean, paramICallback);
  }
  
  public static <DATA> TaskExecutor<DATA> createExecutor(IDataSource<DATA> paramIDataSource, boolean paramBoolean, ICallback<DATA> paramICallback) {
    return TaskExecutors.create(paramIDataSource, paramBoolean, paramICallback);
  }
  
  public static <DATA> TaskExecutor<DATA> createExecutor(IAsyncTask<DATA> paramIAsyncTask, ICallback<DATA> paramICallback) {
    return TaskExecutors.create(paramIAsyncTask, paramICallback);
  }
  
  public static <DATA> TaskExecutor<DATA> createExecutor(ITask<DATA> paramITask, ICallback<DATA> paramICallback) {
    return TaskExecutors.create(paramITask, paramICallback);
  }
  
  private <DATA extends BASE_DATA> TaskHandle executeImp(ICacheConfig<DATA> paramICacheConfig, ISuperTask<DATA> paramISuperTask, boolean paramBoolean, ICallback<DATA> paramICallback) {
    if (paramISuperTask != null) {
      TaskHandle taskHandle1;
      if (paramICacheConfig != null) {
        String str = paramICacheConfig.getTaskKey(paramISuperTask);
        if (!TextUtils.isEmpty(str)) {
          MultiTaskBindProxyCallBack<Object, ?> multiTaskBindProxyCallBack = getTaskImpByTask(str);
          if (multiTaskBindProxyCallBack != null) {
            paramICallback.onPreExecute(paramISuperTask);
            multiTaskBindProxyCallBack.addCallBack(paramISuperTask, paramICallback);
            return new TaskHandle(3, paramISuperTask, multiTaskBindProxyCallBack);
          } 
        } else {
          throw new RuntimeException("ICacheConfig 返回的taskkey不能为空");
        } 
      } 
      TaskHandle taskHandle2 = loadCache(paramICacheConfig, paramISuperTask, paramICallback);
      if (taskHandle2 == null) {
        TaskExecutor<DATA> taskExecutor;
        paramICallback = new MultiTaskBindProxyCallBack<DATA, BASE_DATA>(paramICacheConfig, paramISuperTask, paramICallback, this.registerCallBacks, this.taskImps, this.cacheStore);
        if (paramISuperTask instanceof IDataSource) {
          taskExecutor = TaskExecutors.create((IDataSource<DATA>)paramISuperTask, paramBoolean, paramICallback, this.executor);
        } else if (paramISuperTask instanceof IAsyncDataSource) {
          taskExecutor = TaskExecutors.create((IAsyncDataSource<DATA>)paramISuperTask, paramBoolean, paramICallback);
        } else if (paramISuperTask instanceof ITask) {
          taskExecutor = TaskExecutors.create((ITask<DATA>)paramISuperTask, paramICallback, this.executor);
        } else {
          taskExecutor = TaskExecutors.create((IAsyncTask<DATA>)paramISuperTask, paramICallback);
        } 
        MultiTaskBindProxyCallBack.access$102((MultiTaskBindProxyCallBack)paramICallback, taskExecutor);
        this.taskImps.add(paramICallback);
        taskExecutor.execute();
        taskHandle1 = new TaskHandle(1, paramISuperTask, (MultiTaskBindProxyCallBack)paramICallback);
      } else {
        taskHandle1 = taskHandle2;
      } 
      return taskHandle1;
    } 
    throw new RuntimeException("task不能为空");
  }
  
  private <DATA extends BASE_DATA> MultiTaskBindProxyCallBack<DATA, BASE_DATA> getTaskImpByTask(String paramString) {
    for (MultiTaskBindProxyCallBack<DATA, BASE_DATA> multiTaskBindProxyCallBack : this.taskImps) {
      if (paramString.equals(multiTaskBindProxyCallBack.taskKey))
        return multiTaskBindProxyCallBack; 
    } 
    return null;
  }
  
  private <DATA extends BASE_DATA> TaskHandle loadCache(ICacheConfig<DATA> paramICacheConfig, Object paramObject, ICallback<DATA> paramICallback) {
    if (paramICacheConfig != null) {
      String str = paramICacheConfig.getTaskKey(paramObject);
      ICacheStore.Cache cache = this.cacheStore.getCache(str);
      if (cache != null) {
        Object object = cache.data;
        if (paramICacheConfig.isUsefulCacheData(paramObject, cache.requestTime, cache.saveTime, (DATA)object)) {
          paramICallback.onPreExecute(paramObject);
          paramICallback.onPostExecute(paramObject, Code.SUCCESS, null, (DATA)object);
          return new TaskHandle(2, paramICallback, null);
        } 
      } 
    } 
    return null;
  }
  
  public void cancel(Object paramObject) {
    if (paramObject == null)
      return; 
    if (paramObject instanceof TaskHandle) {
      ((TaskHandle)paramObject).cancle();
      return;
    } 
    Iterator<MultiTaskBindProxyCallBack<?, BASE_DATA>> iterator = this.taskImps.iterator();
    do {
    
    } while (iterator.hasNext() && !((MultiTaskBindProxyCallBack)iterator.next()).cancel(paramObject));
  }
  
  public void cancelAll() {
    if (this.taskImps.isEmpty())
      return; 
    Iterator<?> iterator = (new HashSet(this.taskImps)).iterator();
    while (iterator.hasNext())
      ((MultiTaskBindProxyCallBack)iterator.next()).cancelAllTaskBinder(); 
    this.taskImps.clear();
  }
  
  public void cancelAllWithTaskKey(String paramString) {
    if (TextUtils.isEmpty(paramString))
      return; 
    MultiTaskBindProxyCallBack<Object, ?> multiTaskBindProxyCallBack = getTaskImpByTask(paramString);
    if (multiTaskBindProxyCallBack != null)
      multiTaskBindProxyCallBack.cancelAllTaskBinder(); 
  }
  
  public void cancle() {
    cancelAll();
  }
  
  public void destroy() {
    cancelAll();
  }
  
  public <DATA extends BASE_DATA> TaskHandle execute(IAsyncDataSource<DATA> paramIAsyncDataSource, ICallback<DATA> paramICallback) {
    return executeImp(null, (ISuperTask<DATA>)paramIAsyncDataSource, true, paramICallback);
  }
  
  public <DATA extends BASE_DATA> TaskHandle execute(IAsyncDataSource<DATA> paramIAsyncDataSource, boolean paramBoolean, ICallback<DATA> paramICallback) {
    return executeImp(null, (ISuperTask<DATA>)paramIAsyncDataSource, paramBoolean, paramICallback);
  }
  
  public <DATA extends BASE_DATA> TaskHandle execute(IDataSource<DATA> paramIDataSource, ICallback<DATA> paramICallback) {
    return executeImp(null, (ISuperTask<DATA>)paramIDataSource, true, paramICallback);
  }
  
  public <DATA extends BASE_DATA> TaskHandle execute(IDataSource<DATA> paramIDataSource, boolean paramBoolean, ICallback<DATA> paramICallback) {
    return executeImp(null, (ISuperTask<DATA>)paramIDataSource, paramBoolean, paramICallback);
  }
  
  public <DATA extends BASE_DATA> TaskHandle execute(IAsyncTask<DATA> paramIAsyncTask, ICallback<DATA> paramICallback) {
    return executeImp(null, paramIAsyncTask, true, paramICallback);
  }
  
  public <DATA extends BASE_DATA> TaskHandle execute(ITask<DATA> paramITask, ICallback<DATA> paramICallback) {
    return executeImp(null, paramITask, true, paramICallback);
  }
  
  public <DATA extends BASE_DATA> TaskHandle executeCache(IAsyncDataSource<DATA> paramIAsyncDataSource, ICallback<DATA> paramICallback, ICacheConfig<DATA> paramICacheConfig) {
    return executeImp(paramICacheConfig, (ISuperTask<DATA>)paramIAsyncDataSource, true, paramICallback);
  }
  
  public <DATA extends BASE_DATA> TaskHandle executeCache(IDataSource<DATA> paramIDataSource, ICallback<DATA> paramICallback, ICacheConfig<DATA> paramICacheConfig) {
    return executeImp(paramICacheConfig, (ISuperTask<DATA>)paramIDataSource, true, paramICallback);
  }
  
  public <DATA extends BASE_DATA> TaskHandle executeCache(IAsyncTask<DATA> paramIAsyncTask, ICallback<DATA> paramICallback, ICacheConfig<DATA> paramICacheConfig) {
    return executeImp(paramICacheConfig, paramIAsyncTask, true, paramICallback);
  }
  
  public <DATA extends BASE_DATA> TaskHandle executeCache(ITask<DATA> paramITask, ICallback<DATA> paramICallback, ICacheConfig<DATA> paramICacheConfig) {
    return executeImp(paramICacheConfig, paramITask, true, paramICallback);
  }
  
  public ICacheStore getCacheStore() {
    return this.cacheStore;
  }
  
  public boolean isRunning() {
    return false;
  }
  
  public void registerCallBack(ICallback<BASE_DATA> paramICallback) {
    this.registerCallBacks.add(paramICallback);
  }
  
  public void setThreadExecutor(Executor paramExecutor) {
    this.executor = paramExecutor;
  }
  
  public void unRegisterCallBack(ICallback<BASE_DATA> paramICallback) {
    this.registerCallBacks.remove(paramICallback);
  }
  
  static class MultiTaskBindProxyCallBack<DATA extends BASE_DATA, BASE_DATA> implements ICallback<DATA> {
    private Map<Object, ICallback<DATA>> bindCallBacks = new LinkedHashMap<Object, ICallback<DATA>>();
    
    private ICacheConfig<DATA> cacheConfig;
    
    private ICacheStore cacheStore;
    
    private ICallback<DATA> callback;
    
    private Object realTask;
    
    private Set<ICallback<BASE_DATA>> registerCallbacks;
    
    private long requestTime;
    
    private TaskExecutor<DATA> taskExecutor;
    
    private List<MultiTaskBindProxyCallBack<?, BASE_DATA>> taskImps;
    
    private String taskKey;
    
    public MultiTaskBindProxyCallBack(ICacheConfig<DATA> param1ICacheConfig, Object param1Object, ICallback<DATA> param1ICallback, Set<ICallback<BASE_DATA>> param1Set, List<MultiTaskBindProxyCallBack<?, BASE_DATA>> param1List, ICacheStore param1ICacheStore) {
      this.cacheConfig = param1ICacheConfig;
      this.requestTime = System.currentTimeMillis();
      if (param1ICacheConfig != null)
        this.taskKey = param1ICacheConfig.getTaskKey(param1Object); 
      this.callback = param1ICallback;
      this.registerCallbacks = param1Set;
      this.taskImps = param1List;
      this.cacheStore = param1ICacheStore;
      this.realTask = param1Object;
    }
    
    public void addCallBack(Object param1Object, ICallback<DATA> param1ICallback) {
      Map<Object, ICallback<DATA>> map = this.bindCallBacks;
      if (map != null)
        map.put(param1Object, param1ICallback); 
    }
    
    public boolean cancel(Object<DATA> param1Object) {
      Map<Object, ICallback<DATA>> map = getRegisterCallbacks();
      if (map == null)
        return false; 
      if (param1Object.equals(this.realTask)) {
        if (!map.isEmpty()) {
          param1Object = (Object<DATA>)this.callback;
          if (param1Object != null)
            param1Object.onPostExecute(this.realTask, Code.CANCEL, null, null); 
          this.callback = null;
        } else {
          cancelAllTaskBinder();
        } 
        return true;
      } 
      Iterator<Map.Entry> iterator = map.entrySet().iterator();
      while (iterator.hasNext()) {
        Map.Entry entry = iterator.next();
        Object object = entry.getKey();
        if (param1Object.equals(object)) {
          iterator.remove();
          ((ICallback)entry.getValue()).onPostExecute(object, Code.CANCEL, null, null);
          return true;
        } 
      } 
      return false;
    }
    
    public void cancelAllTaskBinder() {
      TaskExecutor<DATA> taskExecutor = this.taskExecutor;
      if (taskExecutor != null)
        taskExecutor.cancle(); 
    }
    
    public Map<Object, ICallback<DATA>> getRegisterCallbacks() {
      return this.bindCallBacks;
    }
    
    public boolean isRunning() {
      TaskExecutor<DATA> taskExecutor = this.taskExecutor;
      return (taskExecutor != null) ? taskExecutor.isRunning() : false;
    }
    
    public void onPostExecute(Object param1Object, Code param1Code, Exception param1Exception, DATA param1DATA) {
      for (Map.Entry<Object, ICallback<DATA>> entry : this.bindCallBacks.entrySet()) {
        Object object = entry.getKey();
        ((ICallback<DATA>)entry.getValue()).onPostExecute(object, param1Code, param1Exception, param1DATA);
      } 
      ICallback<DATA> iCallback = this.callback;
      if (iCallback != null)
        iCallback.onPostExecute(param1Object, param1Code, param1Exception, param1DATA); 
      Iterator<ICallback<BASE_DATA>> iterator = this.registerCallbacks.iterator();
      while (iterator.hasNext())
        ((ICallback<DATA>)iterator.next()).onPostExecute(param1Object, param1Code, param1Exception, param1DATA); 
      this.taskImps.remove(this);
      if (param1Code == Code.SUCCESS && this.cacheConfig != null) {
        long l = System.currentTimeMillis();
        if (this.cacheConfig.isNeedSave(param1Object, this.requestTime, l, param1DATA)) {
          param1Object = this.cacheConfig.getTaskKey(param1Object);
          this.cacheStore.saveCache((String)param1Object, this.requestTime, l, param1DATA);
        } 
      } 
      this.cacheStore = null;
      this.registerCallbacks = null;
      this.cacheConfig = null;
      this.taskExecutor = null;
      this.taskImps = null;
      this.callback = null;
      this.bindCallBacks = null;
      this.realTask = null;
      this.taskKey = null;
    }
    
    public void onPreExecute(Object param1Object) {
      for (Map.Entry<Object, ICallback<DATA>> entry : this.bindCallBacks.entrySet()) {
        Object object = entry.getKey();
        ((ICallback)entry.getValue()).onPreExecute(object);
      } 
      ICallback<DATA> iCallback = this.callback;
      if (iCallback != null)
        iCallback.onPreExecute(param1Object); 
      Iterator<ICallback<BASE_DATA>> iterator = this.registerCallbacks.iterator();
      while (iterator.hasNext())
        ((ICallback)iterator.next()).onPreExecute(param1Object); 
    }
    
    public void onProgress(Object param1Object1, int param1Int, long param1Long1, long param1Long2, Object param1Object2) {
      for (Map.Entry<Object, ICallback<DATA>> entry : this.bindCallBacks.entrySet()) {
        Object object = entry.getKey();
        ((ICallback)entry.getValue()).onProgress(object, param1Int, param1Long1, param1Long2, param1Object2);
      } 
      ICallback<DATA> iCallback = this.callback;
      if (iCallback != null)
        iCallback.onProgress(param1Object1, param1Int, param1Long1, param1Long2, param1Object2); 
      Iterator<ICallback<BASE_DATA>> iterator = this.registerCallbacks.iterator();
      while (iterator.hasNext())
        ((ICallback)iterator.next()).onProgress(param1Object1, param1Int, param1Long1, param1Long2, param1Object2); 
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/shizhefei/task/TaskHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */