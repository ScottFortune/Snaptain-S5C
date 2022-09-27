package com.bumptech.glide.load.resource.transcode;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.bytes.BytesResource;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.util.ByteBufferUtil;

public class GifDrawableBytesTranscoder implements ResourceTranscoder<GifDrawable, byte[]> {
  public Resource<byte[]> transcode(Resource<GifDrawable> paramResource, Options paramOptions) {
    return (Resource<byte[]>)new BytesResource(ByteBufferUtil.toBytes(((GifDrawable)paramResource.get()).getBuffer()));
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/resource/transcode/GifDrawableBytesTranscoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */