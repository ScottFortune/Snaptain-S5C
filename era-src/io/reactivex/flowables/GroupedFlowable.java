package io.reactivex.flowables;

import io.reactivex.Flowable;

public abstract class GroupedFlowable<K, T> extends Flowable<T> {
  final K key;
  
  protected GroupedFlowable(K paramK) {
    this.key = paramK;
  }
  
  public K getKey() {
    return this.key;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/flowables/GroupedFlowable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */