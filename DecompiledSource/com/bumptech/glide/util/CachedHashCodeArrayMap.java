package com.bumptech.glide.util;

import androidx.collection.ArrayMap;
import androidx.collection.SimpleArrayMap;

public final class CachedHashCodeArrayMap<K, V> extends ArrayMap<K, V> {
  private int hashCode;
  
  public void clear() {
    this.hashCode = 0;
    super.clear();
  }
  
  public int hashCode() {
    if (this.hashCode == 0)
      this.hashCode = super.hashCode(); 
    return this.hashCode;
  }
  
  public V put(K paramK, V paramV) {
    this.hashCode = 0;
    return (V)super.put(paramK, paramV);
  }
  
  public void putAll(SimpleArrayMap<? extends K, ? extends V> paramSimpleArrayMap) {
    this.hashCode = 0;
    super.putAll(paramSimpleArrayMap);
  }
  
  public V removeAt(int paramInt) {
    this.hashCode = 0;
    return (V)super.removeAt(paramInt);
  }
  
  public V setValueAt(int paramInt, V paramV) {
    this.hashCode = 0;
    return (V)super.setValueAt(paramInt, paramV);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/util/CachedHashCodeArrayMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */