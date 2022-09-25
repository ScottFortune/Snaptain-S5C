package com.shizhefei.task;

import com.shizhefei.mvc.RequestHandle;
import java.lang.ref.WeakReference;

public class TaskHandle implements RequestHandle {
  public static final int TYPE_ATTACH = 3;
  
  public static final int TYPE_CACHE = 2;
  
  public static final int TYPE_RUN = 1;
  
  private WeakReference<TaskHelper.MultiTaskBindProxyCallBack> taskImpWeakReference;
  
  private final WeakReference<Object> taskReference;
  
  private final int type;
  
  public TaskHandle(int paramInt, Object paramObject, TaskHelper.MultiTaskBindProxyCallBack paramMultiTaskBindProxyCallBack) {
    this.type = paramInt;
    this.taskReference = new WeakReference(paramObject);
    if (paramMultiTaskBindProxyCallBack != null)
      this.taskImpWeakReference = new WeakReference<TaskHelper.MultiTaskBindProxyCallBack>(paramMultiTaskBindProxyCallBack); 
  }
  
  public void cancle() {
    WeakReference<TaskHelper.MultiTaskBindProxyCallBack> weakReference = this.taskImpWeakReference;
    if (weakReference == null)
      return; 
    TaskHelper.MultiTaskBindProxyCallBack multiTaskBindProxyCallBack = weakReference.get();
    Object object = this.taskReference.get();
    if (multiTaskBindProxyCallBack != null && object != null)
      multiTaskBindProxyCallBack.cancel(object); 
  }
  
  public int getRunType() {
    return this.type;
  }
  
  public boolean isRunning() {
    WeakReference<TaskHelper.MultiTaskBindProxyCallBack> weakReference = this.taskImpWeakReference;
    if (weakReference == null)
      return false; 
    TaskHelper.MultiTaskBindProxyCallBack multiTaskBindProxyCallBack = weakReference.get();
    return (multiTaskBindProxyCallBack != null) ? multiTaskBindProxyCallBack.isRunning() : false;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/shizhefei/task/TaskHandle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */