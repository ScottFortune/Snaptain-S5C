package com.netopsun.anykadevices;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

public class PacketSplitter {
  private final Callback callback;
  
  private byte[] head = new byte[] { 108, 101, 119, 101, 105, 95, 99, 109, 100, 0 };
  
  private final List<AbstractMap.SimpleEntry<byte[], Integer>> incompletePackages = new ArrayList<AbstractMap.SimpleEntry<byte[], Integer>>();
  
  private final List<AbstractMap.SimpleEntry<byte[], Integer>> shouldAddNewPackages = new ArrayList<AbstractMap.SimpleEntry<byte[], Integer>>();
  
  private final List<AbstractMap.SimpleEntry<byte[], Integer>> shouldRemovePackages = new ArrayList<AbstractMap.SimpleEntry<byte[], Integer>>();
  
  int state = 0;
  
  private int suspectPackageSize = 46;
  
  public PacketSplitter(Callback paramCallback) {
    this.callback = paramCallback;
  }
  
  private static int byte2IntLittle(byte[] paramArrayOfbyte, int paramInt) {
    byte b1 = paramArrayOfbyte[paramInt + 3];
    byte b2 = paramArrayOfbyte[paramInt + 2];
    byte b3 = paramArrayOfbyte[paramInt + 1];
    return paramArrayOfbyte[paramInt] & 0xFF | (b1 & 0xFF) << 24 | (b2 & 0xFF) << 16 | (b3 & 0xFF) << 8;
  }
  
  private void dealWithIncompletePackages(byte paramByte) {
    this.shouldRemovePackages.clear();
    this.shouldAddNewPackages.clear();
    for (AbstractMap.SimpleEntry<byte[], Integer> simpleEntry : this.incompletePackages) {
      byte[] arrayOfByte = (byte[])simpleEntry.getKey();
      int i = ((Integer)simpleEntry.getValue()).intValue();
      if (arrayOfByte.length - 1 > i) {
        arrayOfByte[i] = (byte)paramByte;
      } else if (arrayOfByte.length - 1 == i) {
        arrayOfByte[i] = (byte)paramByte;
        this.callback.onPackage((byte[])simpleEntry.getKey());
        this.shouldRemovePackages.add(simpleEntry);
        continue;
      } 
      if (i == 26) {
        int j = byte2IntLittle(arrayOfByte, 22);
        if (j != 0) {
          this.shouldRemovePackages.add(simpleEntry);
          byte[] arrayOfByte1 = new byte[this.suspectPackageSize + j];
          System.arraycopy(arrayOfByte, 0, arrayOfByte1, 0, 26);
          AbstractMap.SimpleEntry<byte, Integer> simpleEntry1 = new AbstractMap.SimpleEntry<byte, Integer>(arrayOfByte1, Integer.valueOf(27));
          this.shouldAddNewPackages.add(simpleEntry1);
        } 
      } 
      simpleEntry.setValue(Integer.valueOf(i + 1));
    } 
    if (!this.shouldRemovePackages.isEmpty())
      this.incompletePackages.removeAll(this.shouldRemovePackages); 
    if (!this.shouldAddNewPackages.isEmpty())
      this.incompletePackages.addAll(this.shouldAddNewPackages); 
  }
  
  private boolean isPackageStart(byte paramByte) {
    int i = this.state;
    if ((i == 0 || i == 1) && paramByte == 108) {
      this.state = 1;
    } else if (this.state == 1 && paramByte == 101) {
      this.state = 2;
    } else if (this.state == 2 && paramByte == 119) {
      this.state = 3;
    } else if (this.state == 3 && paramByte == 101) {
      this.state = 4;
    } else if (this.state == 4 && paramByte == 105) {
      this.state = 5;
    } else if (this.state == 5 && paramByte == 95) {
      this.state = 6;
    } else if (this.state == 6 && paramByte == 99) {
      this.state = 7;
    } else if (this.state == 7 && paramByte == 109) {
      this.state = 8;
    } else if (this.state == 8 && paramByte == 100) {
      this.state = 9;
    } else {
      if (this.state == 9 && paramByte == 0) {
        this.state = 0;
        return true;
      } 
      this.state = 0;
    } 
    return false;
  }
  
  private boolean verifyPackage(byte[] paramArrayOfbyte) {
    return true;
  }
  
  public void onByte(byte paramByte) {
    if (isPackageStart(paramByte)) {
      byte[] arrayOfByte = new byte[this.suspectPackageSize];
      System.arraycopy(this.head, 0, arrayOfByte, 0, 10);
      AbstractMap.SimpleEntry<byte, Integer> simpleEntry = new AbstractMap.SimpleEntry<byte, Integer>(arrayOfByte, Integer.valueOf(10));
      this.incompletePackages.add(simpleEntry);
    } 
    if (!this.incompletePackages.isEmpty())
      dealWithIncompletePackages(paramByte); 
  }
  
  public static interface Callback {
    void onPackage(byte[] param1ArrayOfbyte);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/anykadevices/PacketSplitter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */