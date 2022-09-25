package com.netopsun.rxtxprotocol.f200_gps_protocol;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class F200PacketSplitter {
  private final Callback callback;
  
  private byte[] head = new byte[5];
  
  private final List<AbstractMap.SimpleEntry<byte[], Integer>> shouldRemovePackages = new ArrayList<AbstractMap.SimpleEntry<byte[], Integer>>();
  
  int state = 0;
  
  private int suspectPackageSize = 0;
  
  private final List<AbstractMap.SimpleEntry<byte[], Integer>> suspectPackages = new ArrayList<AbstractMap.SimpleEntry<byte[], Integer>>();
  
  public F200PacketSplitter(Callback paramCallback) {
    this.callback = paramCallback;
  }
  
  private void dealWithSuspectPackages(byte paramByte) {
    this.shouldRemovePackages.clear();
    Iterator<AbstractMap.SimpleEntry<byte[], Integer>> iterator = this.suspectPackages.iterator();
    while (true) {
      boolean bool = iterator.hasNext();
      boolean bool1 = true;
      if (bool) {
        AbstractMap.SimpleEntry<byte[], Integer> simpleEntry = iterator.next();
        byte[] arrayOfByte = (byte[])simpleEntry.getKey();
        int i = ((Integer)simpleEntry.getValue()).intValue();
        if (arrayOfByte.length - 1 > i) {
          arrayOfByte[i] = (byte)paramByte;
          simpleEntry.setValue(Integer.valueOf(i + 1));
          continue;
        } 
        if (arrayOfByte.length - 1 == i) {
          arrayOfByte[i] = (byte)paramByte;
          if (verifyPackage(arrayOfByte)) {
            this.callback.onPackage((byte[])simpleEntry.getKey());
            paramByte = bool1;
            break;
          } 
          this.shouldRemovePackages.add(simpleEntry);
        } 
        continue;
      } 
      paramByte = 0;
      break;
    } 
    if (!this.shouldRemovePackages.isEmpty())
      this.suspectPackages.removeAll(this.shouldRemovePackages); 
    if (paramByte != 0)
      this.suspectPackages.clear(); 
  }
  
  private static byte getCheckSum(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    int i = paramInt1;
    byte b1 = 0;
    byte b2;
    for (b2 = b1; i <= paramInt2; b2 = b1) {
      if (i == paramInt1) {
        b1 = paramArrayOfbyte[paramInt1];
      } else {
        b1 = (byte)(b2 ^ paramArrayOfbyte[i]);
      } 
      i++;
    } 
    return b2;
  }
  
  private boolean isPackageStart(byte paramByte) {
    int i = this.state;
    if ((i == 0 || i == 1) && paramByte == 66) {
      this.state = 1;
    } else if (this.state == 1 && paramByte == 84) {
      this.state = 2;
    } else if (this.state == 2 && paramByte == 62) {
      this.state = 3;
    } else if (this.state == 3 && paramByte == 7) {
      this.state = 4;
    } else {
      if (this.state == 4 && paramByte == 102) {
        this.suspectPackageSize = 13;
        return true;
      } 
      if (this.state == 3 && paramByte == 19) {
        this.state = 5;
      } else {
        if (this.state == 5 && paramByte == 103) {
          this.suspectPackageSize = 25;
          return true;
        } 
        if (this.state == 3 && paramByte == 1) {
          this.state = 6;
        } else {
          if (this.state == 6 && paramByte == 108) {
            this.suspectPackageSize = 7;
            return true;
          } 
          if (this.state == 6 && paramByte == 101) {
            this.suspectPackageSize = 7;
            return true;
          } 
          this.state = 0;
        } 
      } 
    } 
    return false;
  }
  
  private boolean verifyPackage(byte[] paramArrayOfbyte) {
    int i = paramArrayOfbyte.length;
    boolean bool = false;
    if (i < 6)
      return false; 
    if (getCheckSum(paramArrayOfbyte, 3, paramArrayOfbyte.length - 2) == paramArrayOfbyte[paramArrayOfbyte.length - 1])
      bool = true; 
    return bool;
  }
  
  public void onByte(byte paramByte) {
    byte[] arrayOfByte = this.head;
    arrayOfByte[0] = (byte)arrayOfByte[1];
    arrayOfByte[1] = (byte)arrayOfByte[2];
    arrayOfByte[2] = (byte)arrayOfByte[3];
    arrayOfByte[3] = (byte)arrayOfByte[4];
    arrayOfByte[4] = paramByte;
    if (isPackageStart(paramByte)) {
      arrayOfByte = new byte[this.suspectPackageSize];
      System.arraycopy(this.head, 0, arrayOfByte, 0, 5);
      AbstractMap.SimpleEntry<byte, Integer> simpleEntry = new AbstractMap.SimpleEntry<byte, Integer>(arrayOfByte, Integer.valueOf(4));
      this.suspectPackages.add(simpleEntry);
    } 
    if (!this.suspectPackages.isEmpty())
      dealWithSuspectPackages(paramByte); 
  }
  
  public static interface Callback {
    void onPackage(byte[] param1ArrayOfbyte);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/rxtxprotocol/f200_gps_protocol/F200PacketSplitter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */