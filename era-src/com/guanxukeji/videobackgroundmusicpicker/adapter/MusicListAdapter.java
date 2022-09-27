package com.guanxukeji.videobackgroundmusicpicker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.guanxukeji.videobackgroundmusicpicker.R;
import com.guanxukeji.videobackgroundmusicpicker.bean.MusicBean;
import java.util.List;

public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.ViewHolder> {
  Context context;
  
  OnItemClickListener itemClickListener;
  
  List<MusicBean> musicBeanList;
  
  public MusicListAdapter(Context paramContext, List<MusicBean> paramList) {
    this.context = paramContext;
    this.musicBeanList = paramList;
  }
  
  public int getItemCount() {
    return this.musicBeanList.size();
  }
  
  public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
    viewHolder.musicName.setText(((MusicBean)this.musicBeanList.get(i)).getName());
    Glide.with(this.context).load(((MusicBean)this.musicBeanList.get(i)).getIcon()).into(viewHolder.singerImage);
    viewHolder.view.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            if (MusicListAdapter.this.itemClickListener != null)
              MusicListAdapter.this.itemClickListener.onItemClick(viewHolder.view, i); 
          }
        });
  }
  
  public ViewHolder onCreateViewHolder(ViewGroup paramViewGroup, int paramInt) {
    return new ViewHolder(LayoutInflater.from(this.context).inflate(R.layout.recycle_view_music_list, null));
  }
  
  public MusicListAdapter setItemClickListener(OnItemClickListener paramOnItemClickListener) {
    this.itemClickListener = paramOnItemClickListener;
    return this;
  }
  
  public static interface OnItemClickListener {
    void onItemClick(View param1View, int param1Int);
  }
  
  static class ViewHolder extends RecyclerView.ViewHolder {
    TextView musicName;
    
    ImageView singerImage;
    
    View view;
    
    ViewHolder(View param1View) {
      super(param1View);
      this.view = param1View;
      this.musicName = (TextView)param1View.findViewById(R.id.music_name);
      this.singerImage = (ImageView)param1View.findViewById(R.id.singer_image);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/guanxukeji/videobackgroundmusicpicker/adapter/MusicListAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */