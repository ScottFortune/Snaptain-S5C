package com.netopsun.ijkvideoview.protocols;

import android.util.Log;
import com.netopsun.deviceshub.base.VideoCommunicator;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import tv.danmaku.ijk.media.player_gx.javaprotocols.BaseProtocol;
import tv.danmaku.ijk.media.player_gx.javaprotocols.JavaUrlContext;

public class VideoCommunicatorsProtocol extends BaseProtocol {
  private static final String TAG = "VideoProtocol";
  
  private static final ConcurrentHashMap<String, WeakReference<VideoCommunicator>> communicatorsHashMap = new ConcurrentHashMap<String, WeakReference<VideoCommunicator>>();
  
  public VideoCommunicatorsProtocol() {
    super("VideoCommunicators", "");
  }
  
  private void removeInvalidCommunicator() {
    Iterator<String> iterator = communicatorsHashMap.keySet().iterator();
    while (iterator.hasNext()) {
      String str = iterator.next();
      WeakReference weakReference = communicatorsHashMap.get(str);
      if (weakReference == null || weakReference.get() == null)
        iterator.remove(); 
    } 
  }
  
  public void addVideoCommunicator(VideoCommunicator paramVideoCommunicator) {
    communicatorsHashMap.put(Integer.toHexString(paramVideoCommunicator.hashCode()), new WeakReference<VideoCommunicator>(paramVideoCommunicator));
  }
  
  public void removeVideoCommunicator(VideoCommunicator paramVideoCommunicator) {
    communicatorsHashMap.remove(Integer.toHexString(paramVideoCommunicator.hashCode()), new WeakReference<VideoCommunicator>(paramVideoCommunicator));
  }
  
  protected int url_close(JavaUrlContext paramJavaUrlContext) {
    return ((VideoCommunicator)paramJavaUrlContext.getPrivateObject()).disconnect();
  }
  
  protected int url_open(JavaUrlContext paramJavaUrlContext, String paramString, int paramInt) {
    removeInvalidCommunicator();
    int i = paramString.indexOf("://");
    if (i >= 0) {
      paramInt = paramString.length();
      i += 3;
      if (paramInt > i) {
        WeakReference<VideoCommunicator> weakReference = communicatorsHashMap.get(paramString.substring(i));
        if (weakReference == null) {
          Log.e("VideoProtocol", "url_open: communicator hashcode不存在");
          return -1;
        } 
        VideoCommunicator videoCommunicator = weakReference.get();
        if (videoCommunicator == null) {
          Log.e("VideoProtocol", "url_open: 对象已被回收");
          return -1;
        } 
        paramJavaUrlContext.setPrivateObject(videoCommunicator);
        paramJavaUrlContext.operator().setMaxPacketSize(videoCommunicator.maxFrameSize()).setForceFPS(videoCommunicator.maxFPS()).commit();
        return videoCommunicator.connect();
      } 
    } 
    Log.e("VideoProtocol", "url_open: url不正确");
    return -1;
  }
  
  protected int url_read(JavaUrlContext paramJavaUrlContext, ByteBuffer paramByteBuffer, int paramInt) {
    return ((VideoCommunicator)paramJavaUrlContext.getPrivateObject()).read(paramByteBuffer, paramInt);
  }
  
  protected long url_seek(JavaUrlContext paramJavaUrlContext, long paramLong, int paramInt) {
    return ((VideoCommunicator)paramJavaUrlContext.getPrivateObject()).seek(paramLong, paramInt);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/ijkvideoview/protocols/VideoCommunicatorsProtocol.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */