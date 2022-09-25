package com.bumptech.glide.module;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@Deprecated
public final class ManifestParser {
  private static final String GLIDE_MODULE_VALUE = "GlideModule";
  
  private static final String TAG = "ManifestParser";
  
  private final Context context;
  
  public ManifestParser(Context paramContext) {
    this.context = paramContext;
  }
  
  private static GlideModule parseModule(String paramString) {
    try {
      Class<?> clazz = Class.forName(paramString);
      paramString = null;
      try {
        String str = (String)clazz.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
        paramString = str;
      } catch (InstantiationException instantiationException) {
        throwInstantiateGlideModuleException(clazz, instantiationException);
      } catch (IllegalAccessException illegalAccessException) {
        throwInstantiateGlideModuleException(clazz, illegalAccessException);
      } catch (NoSuchMethodException noSuchMethodException) {
        throwInstantiateGlideModuleException(clazz, noSuchMethodException);
      } catch (InvocationTargetException invocationTargetException) {
        throwInstantiateGlideModuleException(clazz, invocationTargetException);
      } 
      if (paramString instanceof GlideModule)
        return (GlideModule)paramString; 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Expected instanceof GlideModule, but found: ");
      stringBuilder.append(paramString);
      throw new RuntimeException(stringBuilder.toString());
    } catch (ClassNotFoundException classNotFoundException) {
      throw new IllegalArgumentException("Unable to find GlideModule implementation", classNotFoundException);
    } 
  }
  
  private static void throwInstantiateGlideModuleException(Class<?> paramClass, Exception paramException) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Unable to instantiate GlideModule implementation for ");
    stringBuilder.append(paramClass);
    throw new RuntimeException(stringBuilder.toString(), paramException);
  }
  
  public List<GlideModule> parse() {
    if (Log.isLoggable("ManifestParser", 3))
      Log.d("ManifestParser", "Loading Glide modules"); 
    ArrayList<GlideModule> arrayList = new ArrayList();
    try {
      ApplicationInfo applicationInfo = this.context.getPackageManager().getApplicationInfo(this.context.getPackageName(), 128);
      if (applicationInfo.metaData == null) {
        if (Log.isLoggable("ManifestParser", 3))
          Log.d("ManifestParser", "Got null app info metadata"); 
        return arrayList;
      } 
      if (Log.isLoggable("ManifestParser", 2)) {
        StringBuilder stringBuilder = new StringBuilder();
        this();
        stringBuilder.append("Got app info metadata: ");
        stringBuilder.append(applicationInfo.metaData);
        Log.v("ManifestParser", stringBuilder.toString());
      } 
      for (String str : applicationInfo.metaData.keySet()) {
        if ("GlideModule".equals(applicationInfo.metaData.get(str))) {
          arrayList.add(parseModule(str));
          if (Log.isLoggable("ManifestParser", 3)) {
            StringBuilder stringBuilder = new StringBuilder();
            this();
            stringBuilder.append("Loaded Glide module: ");
            stringBuilder.append(str);
            Log.d("ManifestParser", stringBuilder.toString());
          } 
        } 
      } 
      if (Log.isLoggable("ManifestParser", 3))
        Log.d("ManifestParser", "Finished loading Glide modules"); 
      return arrayList;
    } catch (android.content.pm.PackageManager.NameNotFoundException nameNotFoundException) {
      RuntimeException runtimeException = new RuntimeException("Unable to find metadata to parse GlideModules", (Throwable)nameNotFoundException);
      throw runtimeException;
    } 
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/module/ManifestParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */