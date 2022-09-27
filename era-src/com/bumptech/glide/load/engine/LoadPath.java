package com.bumptech.glide.load.engine;

import androidx.core.util.Pools;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataRewinder;
import com.bumptech.glide.util.Preconditions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoadPath<Data, ResourceType, Transcode> {
  private final Class<Data> dataClass;
  
  private final List<? extends DecodePath<Data, ResourceType, Transcode>> decodePaths;
  
  private final String failureMessage;
  
  private final Pools.Pool<List<Throwable>> listPool;
  
  public LoadPath(Class<Data> paramClass, Class<ResourceType> paramClass1, Class<Transcode> paramClass2, List<DecodePath<Data, ResourceType, Transcode>> paramList, Pools.Pool<List<Throwable>> paramPool) {
    this.dataClass = paramClass;
    this.listPool = paramPool;
    this.decodePaths = (List<? extends DecodePath<Data, ResourceType, Transcode>>)Preconditions.checkNotEmpty(paramList);
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Failed LoadPath{");
    stringBuilder.append(paramClass.getSimpleName());
    stringBuilder.append("->");
    stringBuilder.append(paramClass1.getSimpleName());
    stringBuilder.append("->");
    stringBuilder.append(paramClass2.getSimpleName());
    stringBuilder.append("}");
    this.failureMessage = stringBuilder.toString();
  }
  
  private Resource<Transcode> loadWithExceptionList(DataRewinder<Data> paramDataRewinder, Options paramOptions, int paramInt1, int paramInt2, DecodePath.DecodeCallback<ResourceType> paramDecodeCallback, List<Throwable> paramList) throws GlideException {
    Resource<Transcode> resource;
    int i = this.decodePaths.size();
    byte b = 0;
    DecodePath decodePath = null;
    while (true) {
      DecodePath decodePath1 = decodePath;
      if (b < i) {
        Resource<Transcode> resource1;
        decodePath1 = this.decodePaths.get(b);
        try {
          resource = decodePath1.decode(paramDataRewinder, paramInt1, paramInt2, paramOptions, paramDecodeCallback);
          resource1 = resource;
        } catch (GlideException glideException1) {
          paramList.add(glideException1);
        } 
        if (resource1 != null) {
          resource = resource1;
          break;
        } 
        b++;
        continue;
      } 
      break;
    } 
    if (resource != null)
      return resource; 
    GlideException glideException = new GlideException(this.failureMessage, new ArrayList<Throwable>(paramList));
    throw glideException;
  }
  
  public Class<Data> getDataClass() {
    return this.dataClass;
  }
  
  public Resource<Transcode> load(DataRewinder<Data> paramDataRewinder, Options paramOptions, int paramInt1, int paramInt2, DecodePath.DecodeCallback<ResourceType> paramDecodeCallback) throws GlideException {
    List<Throwable> list = (List)Preconditions.checkNotNull(this.listPool.acquire());
    try {
      return loadWithExceptionList(paramDataRewinder, paramOptions, paramInt1, paramInt2, paramDecodeCallback, list);
    } finally {
      this.listPool.release(list);
    } 
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("LoadPath{decodePaths=");
    stringBuilder.append(Arrays.toString(this.decodePaths.toArray()));
    stringBuilder.append('}');
    return stringBuilder.toString();
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/engine/LoadPath.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */