package com.bumptech.glide.load.engine;

import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.Transformation;
import java.util.Map;

class EngineKeyFactory {
  EngineKey buildKey(Object paramObject, Key paramKey, int paramInt1, int paramInt2, Map<Class<?>, Transformation<?>> paramMap, Class<?> paramClass1, Class<?> paramClass2, Options paramOptions) {
    return new EngineKey(paramObject, paramKey, paramInt1, paramInt2, paramMap, paramClass1, paramClass2, paramOptions);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/engine/EngineKeyFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */