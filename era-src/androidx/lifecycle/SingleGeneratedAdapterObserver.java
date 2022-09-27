package androidx.lifecycle;

class SingleGeneratedAdapterObserver implements LifecycleEventObserver {
  private final GeneratedAdapter mGeneratedAdapter;
  
  SingleGeneratedAdapterObserver(GeneratedAdapter paramGeneratedAdapter) {
    this.mGeneratedAdapter = paramGeneratedAdapter;
  }
  
  public void onStateChanged(LifecycleOwner paramLifecycleOwner, Lifecycle.Event paramEvent) {
    this.mGeneratedAdapter.callMethods(paramLifecycleOwner, paramEvent, false, null);
    this.mGeneratedAdapter.callMethods(paramLifecycleOwner, paramEvent, true, null);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/androidx/lifecycle/SingleGeneratedAdapterObserver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */