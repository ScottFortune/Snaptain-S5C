package com.bumptech.glide.load.engine.bitmap_recycle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class GroupedLinkedMap<K extends Poolable, V> {
  private final LinkedEntry<K, V> head = new LinkedEntry<K, V>();
  
  private final Map<K, LinkedEntry<K, V>> keyToEntry = new HashMap<K, LinkedEntry<K, V>>();
  
  private void makeHead(LinkedEntry<K, V> paramLinkedEntry) {
    removeEntry(paramLinkedEntry);
    LinkedEntry<K, V> linkedEntry = this.head;
    paramLinkedEntry.prev = linkedEntry;
    paramLinkedEntry.next = linkedEntry.next;
    updateEntry(paramLinkedEntry);
  }
  
  private void makeTail(LinkedEntry<K, V> paramLinkedEntry) {
    removeEntry(paramLinkedEntry);
    paramLinkedEntry.prev = this.head.prev;
    paramLinkedEntry.next = this.head;
    updateEntry(paramLinkedEntry);
  }
  
  private static <K, V> void removeEntry(LinkedEntry<K, V> paramLinkedEntry) {
    paramLinkedEntry.prev.next = paramLinkedEntry.next;
    paramLinkedEntry.next.prev = paramLinkedEntry.prev;
  }
  
  private static <K, V> void updateEntry(LinkedEntry<K, V> paramLinkedEntry) {
    paramLinkedEntry.next.prev = paramLinkedEntry;
    paramLinkedEntry.prev.next = paramLinkedEntry;
  }
  
  public V get(K paramK) {
    LinkedEntry<K, Object> linkedEntry1;
    LinkedEntry<K, Object> linkedEntry2 = (LinkedEntry)this.keyToEntry.get(paramK);
    if (linkedEntry2 == null) {
      linkedEntry2 = new LinkedEntry<K, Object>(paramK);
      this.keyToEntry.put(paramK, linkedEntry2);
      linkedEntry1 = linkedEntry2;
    } else {
      linkedEntry1.offer();
      linkedEntry1 = linkedEntry2;
    } 
    makeHead((LinkedEntry)linkedEntry1);
    return (V)linkedEntry1.removeLast();
  }
  
  public void put(K paramK, V paramV) {
    LinkedEntry<K, Object> linkedEntry1;
    LinkedEntry<K, Object> linkedEntry2 = (LinkedEntry)this.keyToEntry.get(paramK);
    if (linkedEntry2 == null) {
      linkedEntry2 = new LinkedEntry<K, Object>(paramK);
      makeTail((LinkedEntry)linkedEntry2);
      this.keyToEntry.put(paramK, linkedEntry2);
      linkedEntry1 = linkedEntry2;
    } else {
      linkedEntry1.offer();
      linkedEntry1 = linkedEntry2;
    } 
    linkedEntry1.add(paramV);
  }
  
  public V removeLast() {
    for (LinkedEntry<K, V> linkedEntry = this.head.prev; !linkedEntry.equals(this.head); linkedEntry = linkedEntry.prev) {
      V v = linkedEntry.removeLast();
      if (v != null)
        return v; 
      removeEntry(linkedEntry);
      this.keyToEntry.remove(linkedEntry.key);
      ((Poolable)linkedEntry.key).offer();
    } 
    return null;
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder("GroupedLinkedMap( ");
    LinkedEntry<K, V> linkedEntry = this.head.next;
    boolean bool = false;
    while (!linkedEntry.equals(this.head)) {
      bool = true;
      stringBuilder.append('{');
      stringBuilder.append(linkedEntry.key);
      stringBuilder.append(':');
      stringBuilder.append(linkedEntry.size());
      stringBuilder.append("}, ");
      linkedEntry = linkedEntry.next;
    } 
    if (bool)
      stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length()); 
    stringBuilder.append(" )");
    return stringBuilder.toString();
  }
  
  private static class LinkedEntry<K, V> {
    final K key;
    
    LinkedEntry<K, V> next = this;
    
    LinkedEntry<K, V> prev = this;
    
    private List<V> values;
    
    LinkedEntry() {
      this(null);
    }
    
    LinkedEntry(K param1K) {
      this.key = param1K;
    }
    
    public void add(V param1V) {
      if (this.values == null)
        this.values = new ArrayList<V>(); 
      this.values.add(param1V);
    }
    
    public V removeLast() {
      V v;
      int i = size();
      if (i > 0) {
        V v1 = this.values.remove(i - 1);
      } else {
        v = null;
      } 
      return v;
    }
    
    public int size() {
      boolean bool;
      List<V> list = this.values;
      if (list != null) {
        bool = list.size();
      } else {
        bool = false;
      } 
      return bool;
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/engine/bitmap_recycle/GroupedLinkedMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */