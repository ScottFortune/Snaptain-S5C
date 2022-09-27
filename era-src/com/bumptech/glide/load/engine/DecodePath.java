package com.bumptech.glide.load.engine;

import android.util.Log;
import androidx.core.util.Pools;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.data.DataRewinder;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;
import com.bumptech.glide.util.Preconditions;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DecodePath<DataType, ResourceType, Transcode> {
  private static final String TAG = "DecodePath";
  
  private final Class<DataType> dataClass;
  
  private final List<? extends ResourceDecoder<DataType, ResourceType>> decoders;
  
  private final String failureMessage;
  
  private final Pools.Pool<List<Throwable>> listPool;
  
  private final ResourceTranscoder<ResourceType, Transcode> transcoder;
  
  public DecodePath(Class<DataType> paramClass, Class<ResourceType> paramClass1, Class<Transcode> paramClass2, List<? extends ResourceDecoder<DataType, ResourceType>> paramList, ResourceTranscoder<ResourceType, Transcode> paramResourceTranscoder, Pools.Pool<List<Throwable>> paramPool) {
    this.dataClass = paramClass;
    this.decoders = paramList;
    this.transcoder = paramResourceTranscoder;
    this.listPool = paramPool;
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Failed DecodePath{");
    stringBuilder.append(paramClass.getSimpleName());
    stringBuilder.append("->");
    stringBuilder.append(paramClass1.getSimpleName());
    stringBuilder.append("->");
    stringBuilder.append(paramClass2.getSimpleName());
    stringBuilder.append("}");
    this.failureMessage = stringBuilder.toString();
  }
  
  private Resource<ResourceType> decodeResource(DataRewinder<DataType> paramDataRewinder, int paramInt1, int paramInt2, Options paramOptions) throws GlideException {
    List<Throwable> list = (List)Preconditions.checkNotNull(this.listPool.acquire());
    try {
      return decodeResourceWithList(paramDataRewinder, paramInt1, paramInt2, paramOptions, list);
    } finally {
      this.listPool.release(list);
    } 
  }
  
  private Resource<ResourceType> decodeResourceWithList(DataRewinder<DataType> paramDataRewinder, int paramInt1, int paramInt2, Options paramOptions, List<Throwable> paramList) throws GlideException {
    int i = this.decoders.size();
    Resource resource = null;
    byte b = 0;
    while (true) {
      Resource resource1 = resource;
      if (b < i) {
        ResourceDecoder resourceDecoder = this.decoders.get(b);
        resource1 = resource;
        try {
          if (resourceDecoder.handles(paramDataRewinder.rewindAndGet(), paramOptions))
            resource1 = resourceDecoder.decode(paramDataRewinder.rewindAndGet(), paramInt1, paramInt2, paramOptions); 
        } catch (IOException iOException) {
          if (Log.isLoggable("DecodePath", 2)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Failed to decode data for ");
            stringBuilder.append(resourceDecoder);
            Log.v("DecodePath", stringBuilder.toString(), iOException);
          } 
          paramList.add(iOException);
          Resource resource2 = resource;
        } catch (RuntimeException runtimeException) {
        
        } catch (OutOfMemoryError outOfMemoryError) {}
        if (outOfMemoryError != null)
          break; 
        b++;
        OutOfMemoryError outOfMemoryError1 = outOfMemoryError;
        continue;
      } 
      break;
    } 
    if (outOfMemoryError != null)
      return (Resource<ResourceType>)outOfMemoryError; 
    GlideException glideException = new GlideException(this.failureMessage, new ArrayList<Throwable>(paramList));
    throw glideException;
  }
  
  public Resource<Transcode> decode(DataRewinder<DataType> paramDataRewinder, int paramInt1, int paramInt2, Options paramOptions, DecodeCallback<ResourceType> paramDecodeCallback) throws GlideException {
    Resource<ResourceType> resource = paramDecodeCallback.onResourceDecoded(decodeResource(paramDataRewinder, paramInt1, paramInt2, paramOptions));
    return this.transcoder.transcode(resource, paramOptions);
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("DecodePath{ dataClass=");
    stringBuilder.append(this.dataClass);
    stringBuilder.append(", decoders=");
    stringBuilder.append(this.decoders);
    stringBuilder.append(", transcoder=");
    stringBuilder.append(this.transcoder);
    stringBuilder.append('}');
    return stringBuilder.toString();
  }
  
  static interface DecodeCallback<ResourceType> {
    Resource<ResourceType> onResourceDecoded(Resource<ResourceType> param1Resource);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/engine/DecodePath.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */