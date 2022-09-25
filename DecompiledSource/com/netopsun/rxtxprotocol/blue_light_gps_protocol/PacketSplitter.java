package com.netopsun.rxtxprotocol.blue_light_gps_protocol;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PacketSplitter {
  private final Callback callback;
  
  private byte[] head = new byte[3];
  
  private final List<AbstractMap.SimpleEntry<byte[], Integer>> shouldRemovePackages = new ArrayList<AbstractMap.SimpleEntry<byte[], Integer>>();
  
  int state = 0;
  
  private int suspectPackageSize = 0;
  
  private final List<AbstractMap.SimpleEntry<byte[], Integer>> suspectPackages = new ArrayList<AbstractMap.SimpleEntry<byte[], Integer>>();
  
  public PacketSplitter(Callback paramCallback) {
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
    if ((i == 0 || i == 1) && paramByte == 88) {
      this.state = 1;
    } else if (this.state == 1 && paramByte == -118) {
      this.state = 2;
    } else {
      if (this.state == 2 && paramByte == 12) {
        this.suspectPackageSize = 16;
        return true;
      } 
      if (this.state == 1 && paramByte == -117) {
        this.state = 3;
      } else {
        if (this.state == 3 && paramByte == 11) {
          this.suspectPackageSize = 15;
          return true;
        } 
        if (this.state == 1 && paramByte == -116) {
          this.state = 4;
        } else {
          if (this.state == 4 && paramByte == 13) {
            this.suspectPackageSize = 17;
            return true;
          } 
          if (this.state == 1 && paramByte == -115) {
            this.state = 5;
          } else {
            if (this.state == 5 && paramByte == 38) {
              this.suspectPackageSize = 42;
              return true;
            } 
            if (this.state == 1 && paramByte == -124) {
              this.state = 6;
            } else {
              if (this.state == 6 && paramByte == 1) {
                this.suspectPackageSize = 5;
                return true;
              } 
              this.state = 0;
            } 
          } 
        } 
      } 
    } 
    return false;
  }
  
  private boolean verifyPackage(byte[] paramArrayOfbyte) {
    int i = paramArrayOfbyte.length;
    boolean bool = false;
    if (i < 2)
      return false; 
    if (getCheckSum(paramArrayOfbyte, 1, paramArrayOfbyte.length - 2) == paramArrayOfbyte[paramArrayOfbyte.length - 1])
      bool = true; 
    return bool;
  }
  
  public void onByte(byte paramByte) {
    byte[] arrayOfByte = this.head;
    arrayOfByte[0] = (byte)arrayOfByte[1];
    arrayOfByte[1] = (byte)arrayOfByte[2];
    arrayOfByte[2] = paramByte;
    if (isPackageStart(paramByte)) {
      byte[] arrayOfByte1 = new byte[this.suspectPackageSize];
      arrayOfByte = this.head;
      arrayOfByte1[0] = (byte)arrayOfByte[0];
      arrayOfByte1[1] = (byte)arrayOfByte[1];
      AbstractMap.SimpleEntry<byte, Integer> simpleEntry = new AbstractMap.SimpleEntry<byte, Integer>(arrayOfByte1, Integer.valueOf(2));
      this.suspectPackages.add(simpleEntry);
    } 
    if (!this.suspectPackages.isEmpty())
      dealWithSuspectPackages(paramByte); 
  }
  
  public static interface Callback {
    void onPackage(byte[] param1ArrayOfbyte);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/rxtxprotocol/blue_light_gps_protocol/PacketSplitter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */