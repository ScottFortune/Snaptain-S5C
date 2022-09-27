package com.bumptech.glide.load.resource.transcode;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.engine.Resource;

public class UnitTranscoder<Z> implements ResourceTranscoder<Z, Z> {
  private static final UnitTranscoder<?> UNIT_TRANSCODER = new UnitTranscoder();
  
  public static <Z> ResourceTranscoder<Z, Z> get() {
    return (ResourceTranscoder)UNIT_TRANSCODER;
  }
  
  public Resource<Z> transcode(Resource<Z> paramResource, Options paramOptions) {
    return paramResource;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/resource/transcode/UnitTranscoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */