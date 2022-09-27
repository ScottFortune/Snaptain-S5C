package com.netopsun.ijkvideoview.widget.media;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.MediaController;
import androidx.appcompat.app.ActionBar;
import java.util.ArrayList;
import java.util.Iterator;

public class AndroidMediaController extends MediaController implements IMediaController {
  private ActionBar mActionBar;
  
  private ArrayList<View> mShowOnceArray = new ArrayList<View>();
  
  public AndroidMediaController(Context paramContext) {
    super(paramContext);
    initView(paramContext);
  }
  
  public AndroidMediaController(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    initView(paramContext);
  }
  
  public AndroidMediaController(Context paramContext, boolean paramBoolean) {
    super(paramContext, paramBoolean);
    initView(paramContext);
  }
  
  private void initView(Context paramContext) {}
  
  public void hide() {
    super.hide();
    ActionBar actionBar = this.mActionBar;
    if (actionBar != null)
      actionBar.hide(); 
    Iterator<View> iterator = this.mShowOnceArray.iterator();
    while (iterator.hasNext())
      ((View)iterator.next()).setVisibility(8); 
    this.mShowOnceArray.clear();
  }
  
  public void setSupportActionBar(ActionBar paramActionBar) {
    this.mActionBar = paramActionBar;
    if (isShowing()) {
      paramActionBar.show();
    } else {
      paramActionBar.hide();
    } 
  }
  
  public void show() {
    super.show();
    ActionBar actionBar = this.mActionBar;
    if (actionBar != null)
      actionBar.show(); 
  }
  
  public void showOnce(View paramView) {
    this.mShowOnceArray.add(paramView);
    paramView.setVisibility(0);
    show();
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/ijkvideoview/widget/media/AndroidMediaController.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */