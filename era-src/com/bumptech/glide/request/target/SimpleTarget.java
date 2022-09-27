package com.bumptech.glide.request.target;

import com.bumptech.glide.util.Util;

@Deprecated
public abstract class SimpleTarget<Z> extends BaseTarget<Z> {
  private final int height;
  
  private final int width;
  
  public SimpleTarget() {
    this(-2147483648, -2147483648);
  }
  
  public SimpleTarget(int paramInt1, int paramInt2) {
    this.width = paramInt1;
    this.height = paramInt2;
  }
  
  public final void getSize(SizeReadyCallback paramSizeReadyCallback) {
    if (Util.isValidDimensions(this.width, this.height)) {
      paramSizeReadyCallback.onSizeReady(this.width, this.height);
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Width and height must both be > 0 or Target#SIZE_ORIGINAL, but given width: ");
    stringBuilder.append(this.width);
    stringBuilder.append(" and height: ");
    stringBuilder.append(this.height);
    stringBuilder.append(", either provide dimensions in the constructor or call override()");
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public void removeCallback(SizeReadyCallback paramSizeReadyCallback) {}
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/request/target/SimpleTarget.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */