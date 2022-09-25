package com.bumptech.glide.load.resource.drawable;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import java.io.IOException;
import java.util.List;

public class ResourceDrawableDecoder implements ResourceDecoder<Uri, Drawable> {
  private static final String ANDROID_PACKAGE_NAME = "android";
  
  private static final int ID_PATH_SEGMENTS = 1;
  
  private static final int MISSING_RESOURCE_ID = 0;
  
  private static final int NAME_PATH_SEGMENT_INDEX = 1;
  
  private static final int NAME_URI_PATH_SEGMENTS = 2;
  
  private static final int RESOURCE_ID_SEGMENT_INDEX = 0;
  
  private static final int TYPE_PATH_SEGMENT_INDEX = 0;
  
  private final Context context;
  
  public ResourceDrawableDecoder(Context paramContext) {
    this.context = paramContext.getApplicationContext();
  }
  
  private Context findContextForPackage(Uri paramUri, String paramString) {
    if (paramString.equals(this.context.getPackageName()))
      return this.context; 
    try {
      return this.context.createPackageContext(paramString, 0);
    } catch (android.content.pm.PackageManager.NameNotFoundException nameNotFoundException) {
      if (paramString.contains(this.context.getPackageName()))
        return this.context; 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Failed to obtain context or unrecognized Uri format for: ");
      stringBuilder.append(paramUri);
      throw new IllegalArgumentException(stringBuilder.toString(), nameNotFoundException);
    } 
  }
  
  private int findResourceIdFromResourceIdUri(Uri paramUri) {
    List<String> list = paramUri.getPathSegments();
    try {
      return Integer.parseInt(list.get(0));
    } catch (NumberFormatException numberFormatException) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Unrecognized Uri format: ");
      stringBuilder.append(paramUri);
      throw new IllegalArgumentException(stringBuilder.toString(), numberFormatException);
    } 
  }
  
  private int findResourceIdFromTypeAndNameResourceUri(Context paramContext, Uri paramUri) {
    List<String> list = paramUri.getPathSegments();
    String str2 = paramUri.getAuthority();
    String str3 = list.get(0);
    String str1 = list.get(1);
    int i = paramContext.getResources().getIdentifier(str1, str3, str2);
    int j = i;
    if (i == 0)
      j = Resources.getSystem().getIdentifier(str1, str3, "android"); 
    if (j != 0)
      return j; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Failed to find resource id for: ");
    stringBuilder.append(paramUri);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  private int findResourceIdFromUri(Context paramContext, Uri paramUri) {
    List list = paramUri.getPathSegments();
    if (list.size() == 2)
      return findResourceIdFromTypeAndNameResourceUri(paramContext, paramUri); 
    if (list.size() == 1)
      return findResourceIdFromResourceIdUri(paramUri); 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Unrecognized Uri format: ");
    stringBuilder.append(paramUri);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public Resource<Drawable> decode(Uri paramUri, int paramInt1, int paramInt2, Options paramOptions) {
    Context context = findContextForPackage(paramUri, paramUri.getAuthority());
    paramInt1 = findResourceIdFromUri(context, paramUri);
    return NonOwnedDrawableResource.newInstance(DrawableDecoderCompat.getDrawable(this.context, context, paramInt1));
  }
  
  public boolean handles(Uri paramUri, Options paramOptions) {
    return paramUri.getScheme().equals("android.resource");
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/load/resource/drawable/ResourceDrawableDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */