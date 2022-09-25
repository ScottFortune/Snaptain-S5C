package com.bumptech.glide.load;

import com.bumptech.glide.load.engine.Resource;
import java.io.IOException;

public interface ResourceDecoder<T, Z> {
  Resource<Z> decode(T paramT, int paramInt1, int paramInt2, Options paramOptions) throws IOException;
  
  boolean handles(T paramT, Options paramOptions) throws IOException;
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/ResourceDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */