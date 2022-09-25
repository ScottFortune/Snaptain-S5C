package com.netopsun.deviceshub.base.bean;

import java.util.ArrayList;
import java.util.List;

public class RemoteVideoFiles {
  private List<RemoteVideoFilesBean> remote_video_files = new ArrayList<RemoteVideoFilesBean>();
  
  public List<RemoteVideoFilesBean> getRemote_video_files() {
    return this.remote_video_files;
  }
  
  public void setRemote_video_files(List<RemoteVideoFilesBean> paramList) {
    this.remote_video_files = paramList;
  }
  
  public static class RemoteVideoFilesBean {
    private String duration;
    
    private String record_start_time;
    
    private String video_name;
    
    public String getDuration() {
      return this.duration;
    }
    
    public String getRecord_start_time() {
      return this.record_start_time;
    }
    
    public String getVideo_name() {
      return this.video_name;
    }
    
    public void setDuration(String param1String) {
      this.duration = param1String;
    }
    
    public void setRecord_start_time(String param1String) {
      this.record_start_time = param1String;
    }
    
    public void setVideo_name(String param1String) {
      this.video_name = param1String;
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/deviceshub/base/bean/RemoteVideoFiles.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */