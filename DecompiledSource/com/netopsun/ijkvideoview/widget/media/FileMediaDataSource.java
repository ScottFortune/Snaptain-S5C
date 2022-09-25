package com.netopsun.ijkvideoview.widget.media;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import tv.danmaku.ijk.media.player_gx.misc.IMediaDataSource;

public class FileMediaDataSource implements IMediaDataSource {
  private RandomAccessFile mFile;
  
  private long mFileSize;
  
  public FileMediaDataSource(File paramFile) throws IOException {
    this.mFile = new RandomAccessFile(paramFile, "r");
    this.mFileSize = this.mFile.length();
  }
  
  public void close() throws IOException {
    this.mFileSize = 0L;
    this.mFile.close();
    this.mFile = null;
  }
  
  public long getSize() throws IOException {
    return this.mFileSize;
  }
  
  public int readAt(long paramLong, byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException {
    if (this.mFile.getFilePointer() != paramLong)
      this.mFile.seek(paramLong); 
    return (paramInt2 == 0) ? 0 : this.mFile.read(paramArrayOfbyte, 0, paramInt2);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/ijkvideoview/widget/media/FileMediaDataSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */