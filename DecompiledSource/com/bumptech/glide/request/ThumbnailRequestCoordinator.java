package com.bumptech.glide.request;

public class ThumbnailRequestCoordinator implements RequestCoordinator, Request {
  private volatile Request full;
  
  private RequestCoordinator.RequestState fullState = RequestCoordinator.RequestState.CLEARED;
  
  private boolean isRunningDuringBegin;
  
  private final RequestCoordinator parent;
  
  private final Object requestLock;
  
  private volatile Request thumb;
  
  private RequestCoordinator.RequestState thumbState = RequestCoordinator.RequestState.CLEARED;
  
  public ThumbnailRequestCoordinator(Object paramObject, RequestCoordinator paramRequestCoordinator) {
    this.requestLock = paramObject;
    this.parent = paramRequestCoordinator;
  }
  
  private boolean isResourceSet() {
    synchronized (this.requestLock) {
      if (this.fullState == RequestCoordinator.RequestState.SUCCESS || this.thumbState == RequestCoordinator.RequestState.SUCCESS)
        return true; 
      return false;
    } 
  }
  
  private boolean parentCanNotifyCleared() {
    RequestCoordinator requestCoordinator = this.parent;
    return (requestCoordinator == null || requestCoordinator.canNotifyCleared(this));
  }
  
  private boolean parentCanNotifyStatusChanged() {
    RequestCoordinator requestCoordinator = this.parent;
    return (requestCoordinator == null || requestCoordinator.canNotifyStatusChanged(this));
  }
  
  private boolean parentCanSetImage() {
    RequestCoordinator requestCoordinator = this.parent;
    return (requestCoordinator == null || requestCoordinator.canSetImage(this));
  }
  
  private boolean parentIsAnyResourceSet() {
    boolean bool;
    RequestCoordinator requestCoordinator = this.parent;
    if (requestCoordinator != null && requestCoordinator.isAnyResourceSet()) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public void begin() {
    synchronized (this.requestLock) {
      this.isRunningDuringBegin = true;
      try {
        if (this.fullState != RequestCoordinator.RequestState.SUCCESS && this.thumbState != RequestCoordinator.RequestState.RUNNING) {
          this.thumbState = RequestCoordinator.RequestState.RUNNING;
          this.thumb.begin();
        } 
        if (this.isRunningDuringBegin && this.fullState != RequestCoordinator.RequestState.RUNNING) {
          this.fullState = RequestCoordinator.RequestState.RUNNING;
          this.full.begin();
        } 
        return;
      } finally {
        this.isRunningDuringBegin = false;
      } 
    } 
  }
  
  public boolean canNotifyCleared(Request paramRequest) {
    synchronized (this.requestLock) {
      boolean bool;
      if (parentCanNotifyCleared() && paramRequest.equals(this.full) && this.fullState != RequestCoordinator.RequestState.PAUSED) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    } 
  }
  
  public boolean canNotifyStatusChanged(Request paramRequest) {
    synchronized (this.requestLock) {
      boolean bool;
      if (parentCanNotifyStatusChanged() && paramRequest.equals(this.full) && !isResourceSet()) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    } 
  }
  
  public boolean canSetImage(Request paramRequest) {
    synchronized (this.requestLock) {
      boolean bool;
      if (parentCanSetImage() && (paramRequest.equals(this.full) || this.fullState != RequestCoordinator.RequestState.SUCCESS)) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    } 
  }
  
  public void clear() {
    synchronized (this.requestLock) {
      this.isRunningDuringBegin = false;
      this.fullState = RequestCoordinator.RequestState.CLEARED;
      this.thumbState = RequestCoordinator.RequestState.CLEARED;
      this.thumb.clear();
      this.full.clear();
      return;
    } 
  }
  
  public boolean isAnyResourceSet() {
    synchronized (this.requestLock) {
      if (parentIsAnyResourceSet() || isResourceSet())
        return true; 
      return false;
    } 
  }
  
  public boolean isCleared() {
    synchronized (this.requestLock) {
      boolean bool;
      if (this.fullState == RequestCoordinator.RequestState.CLEARED) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    } 
  }
  
  public boolean isComplete() {
    synchronized (this.requestLock) {
      boolean bool;
      if (this.fullState == RequestCoordinator.RequestState.SUCCESS) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    } 
  }
  
  public boolean isEquivalentTo(Request paramRequest) {
    // Byte code:
    //   0: aload_1
    //   1: instanceof com/bumptech/glide/request/ThumbnailRequestCoordinator
    //   4: istore_2
    //   5: iconst_0
    //   6: istore_3
    //   7: iload_3
    //   8: istore #4
    //   10: iload_2
    //   11: ifeq -> 100
    //   14: aload_1
    //   15: checkcast com/bumptech/glide/request/ThumbnailRequestCoordinator
    //   18: astore_1
    //   19: aload_0
    //   20: getfield full : Lcom/bumptech/glide/request/Request;
    //   23: ifnonnull -> 39
    //   26: iload_3
    //   27: istore #4
    //   29: aload_1
    //   30: getfield full : Lcom/bumptech/glide/request/Request;
    //   33: ifnonnull -> 100
    //   36: goto -> 58
    //   39: iload_3
    //   40: istore #4
    //   42: aload_0
    //   43: getfield full : Lcom/bumptech/glide/request/Request;
    //   46: aload_1
    //   47: getfield full : Lcom/bumptech/glide/request/Request;
    //   50: invokeinterface isEquivalentTo : (Lcom/bumptech/glide/request/Request;)Z
    //   55: ifeq -> 100
    //   58: aload_0
    //   59: getfield thumb : Lcom/bumptech/glide/request/Request;
    //   62: ifnonnull -> 78
    //   65: iload_3
    //   66: istore #4
    //   68: aload_1
    //   69: getfield thumb : Lcom/bumptech/glide/request/Request;
    //   72: ifnonnull -> 100
    //   75: goto -> 97
    //   78: iload_3
    //   79: istore #4
    //   81: aload_0
    //   82: getfield thumb : Lcom/bumptech/glide/request/Request;
    //   85: aload_1
    //   86: getfield thumb : Lcom/bumptech/glide/request/Request;
    //   89: invokeinterface isEquivalentTo : (Lcom/bumptech/glide/request/Request;)Z
    //   94: ifeq -> 100
    //   97: iconst_1
    //   98: istore #4
    //   100: iload #4
    //   102: ireturn
  }
  
  public boolean isRunning() {
    synchronized (this.requestLock) {
      boolean bool;
      if (this.fullState == RequestCoordinator.RequestState.RUNNING) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    } 
  }
  
  public void onRequestFailed(Request paramRequest) {
    synchronized (this.requestLock) {
      if (!paramRequest.equals(this.full)) {
        this.thumbState = RequestCoordinator.RequestState.FAILED;
        return;
      } 
      this.fullState = RequestCoordinator.RequestState.FAILED;
      if (this.parent != null)
        this.parent.onRequestFailed(this); 
      return;
    } 
  }
  
  public void onRequestSuccess(Request paramRequest) {
    synchronized (this.requestLock) {
      if (paramRequest.equals(this.thumb)) {
        this.thumbState = RequestCoordinator.RequestState.SUCCESS;
        return;
      } 
      this.fullState = RequestCoordinator.RequestState.SUCCESS;
      if (this.parent != null)
        this.parent.onRequestSuccess(this); 
      if (!this.thumbState.isComplete())
        this.thumb.clear(); 
      return;
    } 
  }
  
  public void pause() {
    synchronized (this.requestLock) {
      if (!this.thumbState.isComplete()) {
        this.thumbState = RequestCoordinator.RequestState.PAUSED;
        this.thumb.pause();
      } 
      if (!this.fullState.isComplete()) {
        this.fullState = RequestCoordinator.RequestState.PAUSED;
        this.full.pause();
      } 
      return;
    } 
  }
  
  public void setRequests(Request paramRequest1, Request paramRequest2) {
    this.full = paramRequest1;
    this.thumb = paramRequest2;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/request/ThumbnailRequestCoordinator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */