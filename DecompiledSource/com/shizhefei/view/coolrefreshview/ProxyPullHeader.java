package com.shizhefei.view.coolrefreshview;

import android.view.View;
import java.util.HashSet;
import java.util.Iterator;

class ProxyPullHeader implements PullHeader {
  private HashSet<OnPullListener> listeners = new HashSet<OnPullListener>(3);
  
  private PullHeader reaPullHeader;
  
  public ProxyPullHeader(PullHeader paramPullHeader) {
    this.reaPullHeader = paramPullHeader;
  }
  
  public void addListener(OnPullListener paramOnPullListener) {
    this.listeners.add(paramOnPullListener);
  }
  
  public View createHeaderView(CoolRefreshView paramCoolRefreshView) {
    return this.reaPullHeader.createHeaderView(paramCoolRefreshView);
  }
  
  public PullHeader.Config getConfig() {
    return this.reaPullHeader.getConfig();
  }
  
  public void onPositionChange(CoolRefreshView paramCoolRefreshView, int paramInt1, int paramInt2, int paramInt3) {
    this.reaPullHeader.onPositionChange(paramCoolRefreshView, paramInt1, paramInt2, paramInt3);
    Iterator<OnPullListener> iterator = this.listeners.iterator();
    while (iterator.hasNext())
      ((OnPullListener)iterator.next()).onPositionChange(paramCoolRefreshView, paramInt1, paramInt2, paramInt3); 
  }
  
  public void onPullBegin(CoolRefreshView paramCoolRefreshView) {
    this.reaPullHeader.onPullBegin(paramCoolRefreshView);
    Iterator<OnPullListener> iterator = this.listeners.iterator();
    while (iterator.hasNext())
      ((OnPullListener)iterator.next()).onPullBegin(paramCoolRefreshView); 
  }
  
  public void onPullRefreshComplete(CoolRefreshView paramCoolRefreshView) {
    this.reaPullHeader.onPullRefreshComplete(paramCoolRefreshView);
    Iterator<OnPullListener> iterator = this.listeners.iterator();
    while (iterator.hasNext())
      ((OnPullListener)iterator.next()).onPullRefreshComplete(paramCoolRefreshView); 
  }
  
  public void onRefreshing(CoolRefreshView paramCoolRefreshView) {
    this.reaPullHeader.onRefreshing(paramCoolRefreshView);
    Iterator<OnPullListener> iterator = this.listeners.iterator();
    while (iterator.hasNext())
      ((OnPullListener)iterator.next()).onRefreshing(paramCoolRefreshView); 
  }
  
  public void onReset(CoolRefreshView paramCoolRefreshView, boolean paramBoolean) {
    this.reaPullHeader.onReset(paramCoolRefreshView, paramBoolean);
    Iterator<OnPullListener> iterator = this.listeners.iterator();
    while (iterator.hasNext())
      ((OnPullListener)iterator.next()).onReset(paramCoolRefreshView, paramBoolean); 
  }
  
  public void removeListener(OnPullListener paramOnPullListener) {
    this.listeners.remove(paramOnPullListener);
  }
  
  public void setPullHandler(PullHeader paramPullHeader) {
    this.reaPullHeader = paramPullHeader;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/shizhefei/view/coolrefreshview/ProxyPullHeader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */