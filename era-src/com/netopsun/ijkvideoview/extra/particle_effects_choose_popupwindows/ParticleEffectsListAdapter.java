package com.netopsun.ijkvideoview.extra.particle_effects_choose_popupwindows;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.netopsun.ijkvideoview.R;
import java.util.List;

public class ParticleEffectsListAdapter extends RecyclerView.Adapter<ParticleEffectsListAdapter.ViewHolder> {
  int checkPosition = 0;
  
  Context context;
  
  OnItemClickListener itemClickListener;
  
  List<ParticleEffectsBean> particleEffectsBeans;
  
  public ParticleEffectsListAdapter(Context paramContext, List<ParticleEffectsBean> paramList) {
    this.context = paramContext;
    this.particleEffectsBeans = paramList;
  }
  
  public int getItemCount() {
    return this.particleEffectsBeans.size();
  }
  
  public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
    int i = this.checkPosition;
    Glide.with(this.context).load(((ParticleEffectsBean)this.particleEffectsBeans.get(i)).getPic()).into(viewHolder.particleEffectsImg);
    viewHolder.view.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            ParticleEffectsListAdapter particleEffectsListAdapter = ParticleEffectsListAdapter.this;
            particleEffectsListAdapter.checkPosition = i;
            particleEffectsListAdapter.notifyDataSetChanged();
            if (ParticleEffectsListAdapter.this.itemClickListener != null)
              ParticleEffectsListAdapter.this.itemClickListener.onItemClick(viewHolder.view, ParticleEffectsListAdapter.this.particleEffectsBeans, i); 
          }
        });
  }
  
  public ViewHolder onCreateViewHolder(ViewGroup paramViewGroup, int paramInt) {
    return new ViewHolder(LayoutInflater.from(this.context).inflate(R.layout.adapter_particle_effects_recyclerview, paramViewGroup, false));
  }
  
  public ParticleEffectsListAdapter setItemClickListener(OnItemClickListener paramOnItemClickListener) {
    this.itemClickListener = paramOnItemClickListener;
    return this;
  }
  
  public static interface OnItemClickListener {
    void onItemClick(View param1View, List<ParticleEffectsBean> param1List, int param1Int);
  }
  
  static class ViewHolder extends RecyclerView.ViewHolder {
    ImageView particleEffectsImg;
    
    View view;
    
    ViewHolder(View param1View) {
      super(param1View);
      this.view = param1View;
      this.particleEffectsImg = (ImageView)param1View.findViewById(R.id.particle_effects_img);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/ijkvideoview/extra/particle_effects_choose_popupwindows/ParticleEffectsListAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */