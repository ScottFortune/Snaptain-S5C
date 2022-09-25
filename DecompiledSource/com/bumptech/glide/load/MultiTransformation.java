package com.bumptech.glide.load;

import android.content.Context;
import com.bumptech.glide.load.engine.Resource;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class MultiTransformation<T> implements Transformation<T> {
  private final Collection<? extends Transformation<T>> transformations;
  
  public MultiTransformation(Collection<? extends Transformation<T>> paramCollection) {
    if (!paramCollection.isEmpty()) {
      this.transformations = paramCollection;
      return;
    } 
    throw new IllegalArgumentException("MultiTransformation must contain at least one Transformation");
  }
  
  @SafeVarargs
  public MultiTransformation(Transformation<T>... paramVarArgs) {
    if (paramVarArgs.length != 0) {
      this.transformations = Arrays.asList(paramVarArgs);
      return;
    } 
    throw new IllegalArgumentException("MultiTransformation must contain at least one Transformation");
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject instanceof MultiTransformation) {
      paramObject = paramObject;
      return this.transformations.equals(((MultiTransformation)paramObject).transformations);
    } 
    return false;
  }
  
  public int hashCode() {
    return this.transformations.hashCode();
  }
  
  public Resource<T> transform(Context paramContext, Resource<T> paramResource, int paramInt1, int paramInt2) {
    Iterator<? extends Transformation<T>> iterator = this.transformations.iterator();
    Resource<T> resource;
    for (resource = paramResource; iterator.hasNext(); resource = resource1) {
      Resource<T> resource1 = ((Transformation<T>)iterator.next()).transform(paramContext, resource, paramInt1, paramInt2);
      if (resource != null && !resource.equals(paramResource) && !resource.equals(resource1))
        resource.recycle(); 
    } 
    return resource;
  }
  
  public void updateDiskCacheKey(MessageDigest paramMessageDigest) {
    Iterator<? extends Transformation<T>> iterator = this.transformations.iterator();
    while (iterator.hasNext())
      ((Transformation)iterator.next()).updateDiskCacheKey(paramMessageDigest); 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/MultiTransformation.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */