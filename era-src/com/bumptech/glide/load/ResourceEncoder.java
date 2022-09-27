package com.bumptech.glide.load;

import com.bumptech.glide.load.engine.Resource;

public interface ResourceEncoder<T> extends Encoder<Resource<T>> {
  EncodeStrategy getEncodeStrategy(Options paramOptions);
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/ResourceEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */