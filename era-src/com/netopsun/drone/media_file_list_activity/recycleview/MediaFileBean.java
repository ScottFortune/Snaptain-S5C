package com.netopsun.drone.media_file_list_activity.recycleview;

public class MediaFileBean {
  String fileModified = "";
  
  String fileName = "";
  
  String filePath = "";
  
  String fileSize = "";
  
  String groupTitle = "";
  
  boolean isChoose = false;
  
  boolean isPhoto = true;
  
  public String getFileModified() {
    return this.fileModified;
  }
  
  public String getFileName() {
    return this.fileName;
  }
  
  public String getFilePath() {
    return this.filePath;
  }
  
  public String getFileSize() {
    return this.fileSize;
  }
  
  public String getGroupTitle() {
    return this.groupTitle;
  }
  
  public boolean isChoose() {
    return this.isChoose;
  }
  
  public boolean isPhoto() {
    return this.isPhoto;
  }
  
  public void setChoose(boolean paramBoolean) {
    this.isChoose = paramBoolean;
  }
  
  public void setFileModified(String paramString) {
    this.fileModified = paramString;
  }
  
  public void setFileName(String paramString) {
    this.fileName = paramString;
  }
  
  public void setFilePath(String paramString) {
    this.filePath = paramString;
  }
  
  public void setFileSize(String paramString) {
    this.fileSize = paramString;
  }
  
  public void setGroupTitle(String paramString) {
    this.groupTitle = paramString;
  }
  
  public void setPhoto(boolean paramBoolean) {
    this.isPhoto = paramBoolean;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/drone/media_file_list_activity/recycleview/MediaFileBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */