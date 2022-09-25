package com.bumptech.glide.request;

public interface RequestCoordinator {
  boolean canNotifyCleared(Request paramRequest);
  
  boolean canNotifyStatusChanged(Request paramRequest);
  
  boolean canSetImage(Request paramRequest);
  
  boolean isAnyResourceSet();
  
  void onRequestFailed(Request paramRequest);
  
  void onRequestSuccess(Request paramRequest);
  
  public enum RequestState {
    CLEARED,
    FAILED,
    PAUSED,
    RUNNING(false),
    SUCCESS(false);
    
    private final boolean isComplete;
    
    static {
      CLEARED = new RequestState("CLEARED", 2, false);
      SUCCESS = new RequestState("SUCCESS", 3, true);
      FAILED = new RequestState("FAILED", 4, true);
      $VALUES = new RequestState[] { RUNNING, PAUSED, CLEARED, SUCCESS, FAILED };
    }
    
    RequestState(boolean param1Boolean) {
      this.isComplete = param1Boolean;
    }
    
    boolean isComplete() {
      return this.isComplete;
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/request/RequestCoordinator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */