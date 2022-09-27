package com.netopsun.gesturerecognition;

import android.content.Context;
import android.graphics.Bitmap;
import com.netopsun.opencvapi.OpenCVAPI;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class GestureReconizer {
  private static volatile boolean beInitializing = false;
  
  private static volatile long caffeNetPtr;
  
  private static volatile Thread gestureReconizeThread;
  
  public static void detect(Bitmap paramBitmap, final GestureReconizeCallBack callBack, final float confidenceThreshold) {
    if (gestureReconizeThread == null && caffeNetPtr != 0L) {
      gestureReconizeThread = new Thread(new Runnable() {
            public void run() {
              try {
                float[] arrayOfFloat = OpenCVAPI.caffeNetDetect(GestureReconizer.caffeNetPtr, bitmap, confidenceThreshold);
                if (arrayOfFloat.length > 1)
                  callBack.onResult((int)arrayOfFloat[0]); 
              } catch (Exception exception) {
                exception.printStackTrace();
              } finally {
                Exception exception;
              } 
              bitmap.recycle();
              GestureReconizer.access$202(null);
            }
          });
      gestureReconizeThread.setPriority(3);
      gestureReconizeThread.start();
    } 
  }
  
  public static void init(Context paramContext) {
    // Byte code:
    //   0: ldc com/netopsun/gesturerecognition/GestureReconizer
    //   2: monitorenter
    //   3: getstatic com/netopsun/gesturerecognition/GestureReconizer.caffeNetPtr : J
    //   6: lconst_0
    //   7: lcmp
    //   8: ifne -> 50
    //   11: getstatic com/netopsun/gesturerecognition/GestureReconizer.beInitializing : Z
    //   14: ifeq -> 20
    //   17: goto -> 50
    //   20: iconst_1
    //   21: putstatic com/netopsun/gesturerecognition/GestureReconizer.beInitializing : Z
    //   24: new java/lang/Thread
    //   27: astore_1
    //   28: new com/netopsun/gesturerecognition/GestureReconizer$1
    //   31: astore_2
    //   32: aload_2
    //   33: aload_0
    //   34: invokespecial <init> : (Landroid/content/Context;)V
    //   37: aload_1
    //   38: aload_2
    //   39: invokespecial <init> : (Ljava/lang/Runnable;)V
    //   42: aload_1
    //   43: invokevirtual start : ()V
    //   46: ldc com/netopsun/gesturerecognition/GestureReconizer
    //   48: monitorexit
    //   49: return
    //   50: ldc com/netopsun/gesturerecognition/GestureReconizer
    //   52: monitorexit
    //   53: return
    //   54: astore_0
    //   55: ldc com/netopsun/gesturerecognition/GestureReconizer
    //   57: monitorexit
    //   58: aload_0
    //   59: athrow
    // Exception table:
    //   from	to	target	type
    //   3	17	54	finally
    //   20	46	54	finally
  }
  
  public static void release() {
    // Byte code:
    //   0: ldc com/netopsun/gesturerecognition/GestureReconizer
    //   2: monitorenter
    //   3: getstatic com/netopsun/gesturerecognition/GestureReconizer.caffeNetPtr : J
    //   6: lconst_0
    //   7: lcmp
    //   8: ifeq -> 21
    //   11: getstatic com/netopsun/gesturerecognition/GestureReconizer.caffeNetPtr : J
    //   14: invokestatic caffeNetDelete : (J)V
    //   17: lconst_0
    //   18: putstatic com/netopsun/gesturerecognition/GestureReconizer.caffeNetPtr : J
    //   21: ldc com/netopsun/gesturerecognition/GestureReconizer
    //   23: monitorexit
    //   24: return
    //   25: astore_0
    //   26: ldc com/netopsun/gesturerecognition/GestureReconizer
    //   28: monitorexit
    //   29: aload_0
    //   30: athrow
    // Exception table:
    //   from	to	target	type
    //   3	21	25	finally
  }
  
  public static interface GestureReconizeCallBack {
    void onResult(int param1Int);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/netopsun/gesturerecognition/GestureReconizer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */