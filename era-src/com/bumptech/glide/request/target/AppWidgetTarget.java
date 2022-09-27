package com.bumptech.glide.request.target;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.RemoteViews;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.util.Preconditions;

public class AppWidgetTarget extends CustomTarget<Bitmap> {
  private final ComponentName componentName;
  
  private final Context context;
  
  private final RemoteViews remoteViews;
  
  private final int viewId;
  
  private final int[] widgetIds;
  
  public AppWidgetTarget(Context paramContext, int paramInt1, int paramInt2, int paramInt3, RemoteViews paramRemoteViews, ComponentName paramComponentName) {
    super(paramInt1, paramInt2);
    this.context = (Context)Preconditions.checkNotNull(paramContext, "Context can not be null!");
    this.remoteViews = (RemoteViews)Preconditions.checkNotNull(paramRemoteViews, "RemoteViews object can not be null!");
    this.componentName = (ComponentName)Preconditions.checkNotNull(paramComponentName, "ComponentName can not be null!");
    this.viewId = paramInt3;
    this.widgetIds = null;
  }
  
  public AppWidgetTarget(Context paramContext, int paramInt1, int paramInt2, int paramInt3, RemoteViews paramRemoteViews, int... paramVarArgs) {
    super(paramInt1, paramInt2);
    if (paramVarArgs.length != 0) {
      this.context = (Context)Preconditions.checkNotNull(paramContext, "Context can not be null!");
      this.remoteViews = (RemoteViews)Preconditions.checkNotNull(paramRemoteViews, "RemoteViews object can not be null!");
      this.widgetIds = (int[])Preconditions.checkNotNull(paramVarArgs, "WidgetIds can not be null!");
      this.viewId = paramInt3;
      this.componentName = null;
      return;
    } 
    throw new IllegalArgumentException("WidgetIds must have length > 0");
  }
  
  public AppWidgetTarget(Context paramContext, int paramInt, RemoteViews paramRemoteViews, ComponentName paramComponentName) {
    this(paramContext, -2147483648, -2147483648, paramInt, paramRemoteViews, paramComponentName);
  }
  
  public AppWidgetTarget(Context paramContext, int paramInt, RemoteViews paramRemoteViews, int... paramVarArgs) {
    this(paramContext, -2147483648, -2147483648, paramInt, paramRemoteViews, paramVarArgs);
  }
  
  private void setBitmap(Bitmap paramBitmap) {
    this.remoteViews.setImageViewBitmap(this.viewId, paramBitmap);
    update();
  }
  
  private void update() {
    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this.context);
    ComponentName componentName = this.componentName;
    if (componentName != null) {
      appWidgetManager.updateAppWidget(componentName, this.remoteViews);
    } else {
      appWidgetManager.updateAppWidget(this.widgetIds, this.remoteViews);
    } 
  }
  
  public void onLoadCleared(Drawable paramDrawable) {
    setBitmap(null);
  }
  
  public void onResourceReady(Bitmap paramBitmap, Transition<? super Bitmap> paramTransition) {
    setBitmap(paramBitmap);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/request/target/AppWidgetTarget.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */