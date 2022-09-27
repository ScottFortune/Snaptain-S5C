package io.reactivex.observables;

import io.reactivex.Observable;

public abstract class GroupedObservable<K, T> extends Observable<T> {
  final K key;
  
  protected GroupedObservable(K paramK) {
    this.key = paramK;
  }
  
  public K getKey() {
    return this.key;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/observables/GroupedObservable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */