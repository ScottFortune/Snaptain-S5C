package com.bumptech.glide.load.engine.prefill;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

final class PreFillQueue {
  private final Map<PreFillType, Integer> bitmapsPerType;
  
  private int bitmapsRemaining;
  
  private int keyIndex;
  
  private final List<PreFillType> keyList;
  
  public PreFillQueue(Map<PreFillType, Integer> paramMap) {
    this.bitmapsPerType = paramMap;
    this.keyList = new ArrayList<PreFillType>(paramMap.keySet());
    for (Integer integer : paramMap.values())
      this.bitmapsRemaining += integer.intValue(); 
  }
  
  public int getSize() {
    return this.bitmapsRemaining;
  }
  
  public boolean isEmpty() {
    boolean bool;
    if (this.bitmapsRemaining == 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public PreFillType remove() {
    int i;
    PreFillType preFillType = this.keyList.get(this.keyIndex);
    Integer integer = this.bitmapsPerType.get(preFillType);
    if (integer.intValue() == 1) {
      this.bitmapsPerType.remove(preFillType);
      this.keyList.remove(this.keyIndex);
    } else {
      this.bitmapsPerType.put(preFillType, Integer.valueOf(integer.intValue() - 1));
    } 
    this.bitmapsRemaining--;
    if (this.keyList.isEmpty()) {
      i = 0;
    } else {
      i = (this.keyIndex + 1) % this.keyList.size();
    } 
    this.keyIndex = i;
    return preFillType;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/engine/prefill/PreFillQueue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */