package com.bumptech.glide.request;

public final class ErrorRequestCoordinator implements RequestCoordinator, Request {
  private volatile Request error;
  
  private RequestCoordinator.RequestState errorState = RequestCoordinator.RequestState.CLEARED;
  
  private final RequestCoordinator parent;
  
  private volatile Request primary;
  
  private RequestCoordinator.RequestState primaryState = RequestCoordinator.RequestState.CLEARED;
  
  private final Object requestLock;
  
  public ErrorRequestCoordinator(Object paramObject, RequestCoordinator paramRequestCoordinator) {
    this.requestLock = paramObject;
    this.parent = paramRequestCoordinator;
  }
  
  private boolean isValidRequest(Request paramRequest) {
    return (paramRequest.equals(this.primary) || (this.primaryState == RequestCoordinator.RequestState.FAILED && paramRequest.equals(this.error)));
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
      if (this.primaryState != RequestCoordinator.RequestState.RUNNING) {
        this.primaryState = RequestCoordinator.RequestState.RUNNING;
        this.primary.begin();
      } 
      return;
    } 
  }
  
  public boolean canNotifyCleared(Request paramRequest) {
    synchronized (this.requestLock) {
      boolean bool;
      if (parentCanNotifyCleared() && isValidRequest(paramRequest)) {
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
      if (parentCanNotifyStatusChanged() && isValidRequest(paramRequest)) {
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
      if (parentCanSetImage() && isValidRequest(paramRequest)) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    } 
  }
  
  public void clear() {
    synchronized (this.requestLock) {
      this.primaryState = RequestCoordinator.RequestState.CLEARED;
      this.primary.clear();
      if (this.errorState != RequestCoordinator.RequestState.CLEARED) {
        this.errorState = RequestCoordinator.RequestState.CLEARED;
        this.error.clear();
      } 
      return;
    } 
  }
  
  public boolean isAnyResourceSet() {
    synchronized (this.requestLock) {
      if (parentIsAnyResourceSet() || isComplete())
        return true; 
      return false;
    } 
  }
  
  public boolean isCleared() {
    synchronized (this.requestLock) {
      boolean bool;
      if (this.primaryState == RequestCoordinator.RequestState.CLEARED && this.errorState == RequestCoordinator.RequestState.CLEARED) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    } 
  }
  
  public boolean isComplete() {
    synchronized (this.requestLock) {
      if (this.primaryState == RequestCoordinator.RequestState.SUCCESS || this.errorState == RequestCoordinator.RequestState.SUCCESS)
        return true; 
      return false;
    } 
  }
  
  public boolean isEquivalentTo(Request paramRequest) {
    boolean bool = paramRequest instanceof ErrorRequestCoordinator;
    boolean bool1 = false;
    boolean bool2 = bool1;
    if (bool) {
      paramRequest = paramRequest;
      bool2 = bool1;
      if (this.primary.isEquivalentTo(((ErrorRequestCoordinator)paramRequest).primary)) {
        bool2 = bool1;
        if (this.error.isEquivalentTo(((ErrorRequestCoordinator)paramRequest).error))
          bool2 = true; 
      } 
    } 
    return bool2;
  }
  
  public boolean isRunning() {
    synchronized (this.requestLock) {
      if (this.primaryState == RequestCoordinator.RequestState.RUNNING || this.errorState == RequestCoordinator.RequestState.RUNNING)
        return true; 
      return false;
    } 
  }
  
  public void onRequestFailed(Request paramRequest) {
    synchronized (this.requestLock) {
      if (!paramRequest.equals(this.error)) {
        this.primaryState = RequestCoordinator.RequestState.FAILED;
        if (this.errorState != RequestCoordinator.RequestState.RUNNING) {
          this.errorState = RequestCoordinator.RequestState.RUNNING;
          this.error.begin();
        } 
        return;
      } 
      this.errorState = RequestCoordinator.RequestState.FAILED;
      if (this.parent != null)
        this.parent.onRequestFailed(this); 
      return;
    } 
  }
  
  public void onRequestSuccess(Request paramRequest) {
    synchronized (this.requestLock) {
      if (paramRequest.equals(this.primary)) {
        this.primaryState = RequestCoordinator.RequestState.SUCCESS;
      } else if (paramRequest.equals(this.error)) {
        this.errorState = RequestCoordinator.RequestState.SUCCESS;
      } 
      if (this.parent != null)
        this.parent.onRequestSuccess(this); 
      return;
    } 
  }
  
  public void pause() {
    synchronized (this.requestLock) {
      if (this.primaryState == RequestCoordinator.RequestState.RUNNING) {
        this.primaryState = RequestCoordinator.RequestState.PAUSED;
        this.primary.pause();
      } 
      if (this.errorState == RequestCoordinator.RequestState.RUNNING) {
        this.errorState = RequestCoordinator.RequestState.PAUSED;
        this.error.pause();
      } 
      return;
    } 
  }
  
  public void setRequests(Request paramRequest1, Request paramRequest2) {
    this.primary = paramRequest1;
    this.error = paramRequest2;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/request/ErrorRequestCoordinator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */