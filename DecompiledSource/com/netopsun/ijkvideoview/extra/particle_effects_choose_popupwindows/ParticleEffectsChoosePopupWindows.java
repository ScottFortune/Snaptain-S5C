package com.netopsun.ijkvideoview.extra.particle_effects_choose_popupwindows;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.netopsun.ijkvideoview.R;
import java.util.ArrayList;
import java.util.List;

public class ParticleEffectsChoosePopupWindows extends PopupWindow {
  private final List<ParticleEffectsBean> particleEffectsBeans;
  
  private final ParticleEffectsListAdapter particleEffectsListAdapter;
  
  public ParticleEffectsChoosePopupWindows(Context paramContext, List<ParticleEffectsBean> paramList) {
    View view = LayoutInflater.from(paramContext).inflate(R.layout.popup_windows_particle_effects_choose, null);
    this.particleEffectsBeans = paramList;
    this.particleEffectsListAdapter = new ParticleEffectsListAdapter(paramContext, this.particleEffectsBeans);
    RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.filter_listview);
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(paramContext);
    linearLayoutManager.setOrientation(0);
    recyclerView.setLayoutManager((RecyclerView.LayoutManager)linearLayoutManager);
    recyclerView.setAdapter(this.particleEffectsListAdapter);
    setContentView(view);
    setWidth(-2);
    setHeight(-2);
  }
  
  public static ParticleEffectsChoosePopupWindows getDefaultParticleEffectsPopupWindows(Context paramContext) {
    ArrayList<ParticleEffectsBean> arrayList = new ArrayList();
    ParticleEffectsBean particleEffectsBean = new ParticleEffectsBean();
    particleEffectsBean.setPic(resourceIdToUri(paramContext, R.drawable.effects_popupwindows_rain));
    particleEffectsBean.setTextureResourceID(R.drawable.effects_rain);
    particleEffectsBean.setPlistFile(R.raw.effects_rain);
    arrayList.add(particleEffectsBean);
    particleEffectsBean = new ParticleEffectsBean();
    particleEffectsBean.setPic(resourceIdToUri(paramContext, R.drawable.effects_popupwindows_snow));
    particleEffectsBean.setTextureResourceID(R.drawable.effects_snow);
    particleEffectsBean.setPlistFile(R.raw.effects_snow);
    arrayList.add(particleEffectsBean);
    particleEffectsBean = new ParticleEffectsBean();
    particleEffectsBean.setPic(resourceIdToUri(paramContext, R.drawable.effects_popupwindows_leaf));
    particleEffectsBean.setTextureResourceID(R.drawable.effects_leaf);
    particleEffectsBean.setPlistFile(R.raw.effects_leaf);
    arrayList.add(particleEffectsBean);
    return new ParticleEffectsChoosePopupWindows(paramContext, arrayList);
  }
  
  private static String resourceIdToUri(Context paramContext, int paramInt) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("android.resource://");
    stringBuilder.append(paramContext.getPackageName());
    stringBuilder.append("/drawable/");
    stringBuilder.append(paramInt);
    return stringBuilder.toString();
  }
  
  public List<ParticleEffectsBean> getParticleEffectsBeans() {
    return this.particleEffectsBeans;
  }
  
  public void setOnItemClickListener(ParticleEffectsListAdapter.OnItemClickListener paramOnItemClickListener) {
    this.particleEffectsListAdapter.setItemClickListener(paramOnItemClickListener);
  }
  
  public void show(View paramView) {
    showAtLocation(paramView, 17, 0, 0);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/ijkvideoview/extra/particle_effects_choose_popupwindows/ParticleEffectsChoosePopupWindows.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */