package com.bumptech.glide.request;

import android.graphics.drawable.Drawable;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class RequestFutureTarget<R> implements FutureTarget<R>, RequestListener<R> {
  private static final Waiter DEFAULT_WAITER = new Waiter();
  
  private final boolean assertBackgroundThread;
  
  private GlideException exception;
  
  private final int height;
  
  private boolean isCancelled;
  
  private boolean loadFailed;
  
  private Request request;
  
  private R resource;
  
  private boolean resultReceived;
  
  private final Waiter waiter;
  
  private final int width;
  
  public RequestFutureTarget(int paramInt1, int paramInt2) {
    this(paramInt1, paramInt2, true, DEFAULT_WAITER);
  }
  
  RequestFutureTarget(int paramInt1, int paramInt2, boolean paramBoolean, Waiter paramWaiter) {
    this.width = paramInt1;
    this.height = paramInt2;
    this.assertBackgroundThread = paramBoolean;
    this.waiter = paramWaiter;
  }
  
  private R doGet(Long paramLong) throws ExecutionException, InterruptedException, TimeoutException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield assertBackgroundThread : Z
    //   6: ifeq -> 19
    //   9: aload_0
    //   10: invokevirtual isDone : ()Z
    //   13: ifne -> 19
    //   16: invokestatic assertBackgroundThread : ()V
    //   19: aload_0
    //   20: getfield isCancelled : Z
    //   23: ifne -> 213
    //   26: aload_0
    //   27: getfield loadFailed : Z
    //   30: ifne -> 199
    //   33: aload_0
    //   34: getfield resultReceived : Z
    //   37: ifeq -> 49
    //   40: aload_0
    //   41: getfield resource : Ljava/lang/Object;
    //   44: astore_1
    //   45: aload_0
    //   46: monitorexit
    //   47: aload_1
    //   48: areturn
    //   49: aload_1
    //   50: ifnonnull -> 65
    //   53: aload_0
    //   54: getfield waiter : Lcom/bumptech/glide/request/RequestFutureTarget$Waiter;
    //   57: aload_0
    //   58: lconst_0
    //   59: invokevirtual waitForTimeout : (Ljava/lang/Object;J)V
    //   62: goto -> 119
    //   65: aload_1
    //   66: invokevirtual longValue : ()J
    //   69: lconst_0
    //   70: lcmp
    //   71: ifle -> 119
    //   74: invokestatic currentTimeMillis : ()J
    //   77: lstore_2
    //   78: aload_1
    //   79: invokevirtual longValue : ()J
    //   82: lload_2
    //   83: ladd
    //   84: lstore #4
    //   86: aload_0
    //   87: invokevirtual isDone : ()Z
    //   90: ifne -> 119
    //   93: lload_2
    //   94: lload #4
    //   96: lcmp
    //   97: ifge -> 119
    //   100: aload_0
    //   101: getfield waiter : Lcom/bumptech/glide/request/RequestFutureTarget$Waiter;
    //   104: aload_0
    //   105: lload #4
    //   107: lload_2
    //   108: lsub
    //   109: invokevirtual waitForTimeout : (Ljava/lang/Object;J)V
    //   112: invokestatic currentTimeMillis : ()J
    //   115: lstore_2
    //   116: goto -> 86
    //   119: invokestatic interrupted : ()Z
    //   122: ifne -> 189
    //   125: aload_0
    //   126: getfield loadFailed : Z
    //   129: ifne -> 175
    //   132: aload_0
    //   133: getfield isCancelled : Z
    //   136: ifne -> 165
    //   139: aload_0
    //   140: getfield resultReceived : Z
    //   143: ifeq -> 155
    //   146: aload_0
    //   147: getfield resource : Ljava/lang/Object;
    //   150: astore_1
    //   151: aload_0
    //   152: monitorexit
    //   153: aload_1
    //   154: areturn
    //   155: new java/util/concurrent/TimeoutException
    //   158: astore_1
    //   159: aload_1
    //   160: invokespecial <init> : ()V
    //   163: aload_1
    //   164: athrow
    //   165: new java/util/concurrent/CancellationException
    //   168: astore_1
    //   169: aload_1
    //   170: invokespecial <init> : ()V
    //   173: aload_1
    //   174: athrow
    //   175: new java/util/concurrent/ExecutionException
    //   178: astore_1
    //   179: aload_1
    //   180: aload_0
    //   181: getfield exception : Lcom/bumptech/glide/load/engine/GlideException;
    //   184: invokespecial <init> : (Ljava/lang/Throwable;)V
    //   187: aload_1
    //   188: athrow
    //   189: new java/lang/InterruptedException
    //   192: astore_1
    //   193: aload_1
    //   194: invokespecial <init> : ()V
    //   197: aload_1
    //   198: athrow
    //   199: new java/util/concurrent/ExecutionException
    //   202: astore_1
    //   203: aload_1
    //   204: aload_0
    //   205: getfield exception : Lcom/bumptech/glide/load/engine/GlideException;
    //   208: invokespecial <init> : (Ljava/lang/Throwable;)V
    //   211: aload_1
    //   212: athrow
    //   213: new java/util/concurrent/CancellationException
    //   216: astore_1
    //   217: aload_1
    //   218: invokespecial <init> : ()V
    //   221: aload_1
    //   222: athrow
    //   223: astore_1
    //   224: aload_0
    //   225: monitorexit
    //   226: goto -> 231
    //   229: aload_1
    //   230: athrow
    //   231: goto -> 229
    // Exception table:
    //   from	to	target	type
    //   2	19	223	finally
    //   19	45	223	finally
    //   53	62	223	finally
    //   65	86	223	finally
    //   86	93	223	finally
    //   100	116	223	finally
    //   119	151	223	finally
    //   155	165	223	finally
    //   165	175	223	finally
    //   175	189	223	finally
    //   189	199	223	finally
    //   199	213	223	finally
    //   213	223	223	finally
  }
  
  public boolean cancel(boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual isDone : ()Z
    //   6: ifeq -> 13
    //   9: aload_0
    //   10: monitorexit
    //   11: iconst_0
    //   12: ireturn
    //   13: aload_0
    //   14: iconst_1
    //   15: putfield isCancelled : Z
    //   18: aload_0
    //   19: getfield waiter : Lcom/bumptech/glide/request/RequestFutureTarget$Waiter;
    //   22: aload_0
    //   23: invokevirtual notifyAll : (Ljava/lang/Object;)V
    //   26: iload_1
    //   27: ifeq -> 43
    //   30: aload_0
    //   31: getfield request : Lcom/bumptech/glide/request/Request;
    //   34: astore_2
    //   35: aload_0
    //   36: aconst_null
    //   37: putfield request : Lcom/bumptech/glide/request/Request;
    //   40: goto -> 45
    //   43: aconst_null
    //   44: astore_2
    //   45: aload_0
    //   46: monitorexit
    //   47: aload_2
    //   48: ifnull -> 57
    //   51: aload_2
    //   52: invokeinterface clear : ()V
    //   57: iconst_1
    //   58: ireturn
    //   59: astore_2
    //   60: aload_0
    //   61: monitorexit
    //   62: aload_2
    //   63: athrow
    // Exception table:
    //   from	to	target	type
    //   2	11	59	finally
    //   13	26	59	finally
    //   30	40	59	finally
    //   45	47	59	finally
    //   60	62	59	finally
  }
  
  public R get() throws InterruptedException, ExecutionException {
    try {
      return doGet(null);
    } catch (TimeoutException timeoutException) {
      throw new AssertionError(timeoutException);
    } 
  }
  
  public R get(long paramLong, TimeUnit paramTimeUnit) throws InterruptedException, ExecutionException, TimeoutException {
    return doGet(Long.valueOf(paramTimeUnit.toMillis(paramLong)));
  }
  
  public Request getRequest() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield request : Lcom/bumptech/glide/request/Request;
    //   6: astore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: aload_1
    //   10: areturn
    //   11: astore_1
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_1
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  public void getSize(SizeReadyCallback paramSizeReadyCallback) {
    paramSizeReadyCallback.onSizeReady(this.width, this.height);
  }
  
  public boolean isCancelled() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield isCancelled : Z
    //   6: istore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: iload_1
    //   10: ireturn
    //   11: astore_2
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_2
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  public boolean isDone() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield isCancelled : Z
    //   6: ifne -> 33
    //   9: aload_0
    //   10: getfield resultReceived : Z
    //   13: ifne -> 33
    //   16: aload_0
    //   17: getfield loadFailed : Z
    //   20: istore_1
    //   21: iload_1
    //   22: ifeq -> 28
    //   25: goto -> 33
    //   28: iconst_0
    //   29: istore_1
    //   30: goto -> 35
    //   33: iconst_1
    //   34: istore_1
    //   35: aload_0
    //   36: monitorexit
    //   37: iload_1
    //   38: ireturn
    //   39: astore_2
    //   40: aload_0
    //   41: monitorexit
    //   42: aload_2
    //   43: athrow
    // Exception table:
    //   from	to	target	type
    //   2	21	39	finally
  }
  
  public void onDestroy() {}
  
  public void onLoadCleared(Drawable paramDrawable) {}
  
  public void onLoadFailed(Drawable paramDrawable) {
    /* monitor enter ThisExpression{ObjectType{com/bumptech/glide/request/RequestFutureTarget}} */
    /* monitor exit ThisExpression{ObjectType{com/bumptech/glide/request/RequestFutureTarget}} */
  }
  
  public boolean onLoadFailed(GlideException paramGlideException, Object paramObject, Target<R> paramTarget, boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iconst_1
    //   4: putfield loadFailed : Z
    //   7: aload_0
    //   8: aload_1
    //   9: putfield exception : Lcom/bumptech/glide/load/engine/GlideException;
    //   12: aload_0
    //   13: getfield waiter : Lcom/bumptech/glide/request/RequestFutureTarget$Waiter;
    //   16: aload_0
    //   17: invokevirtual notifyAll : (Ljava/lang/Object;)V
    //   20: aload_0
    //   21: monitorexit
    //   22: iconst_0
    //   23: ireturn
    //   24: astore_1
    //   25: aload_0
    //   26: monitorexit
    //   27: aload_1
    //   28: athrow
    // Exception table:
    //   from	to	target	type
    //   2	20	24	finally
  }
  
  public void onLoadStarted(Drawable paramDrawable) {}
  
  public void onResourceReady(R paramR, Transition<? super R> paramTransition) {
    /* monitor enter ThisExpression{ObjectType{com/bumptech/glide/request/RequestFutureTarget}} */
    /* monitor exit ThisExpression{ObjectType{com/bumptech/glide/request/RequestFutureTarget}} */
  }
  
  public boolean onResourceReady(R paramR, Object paramObject, Target<R> paramTarget, DataSource paramDataSource, boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iconst_1
    //   4: putfield resultReceived : Z
    //   7: aload_0
    //   8: aload_1
    //   9: putfield resource : Ljava/lang/Object;
    //   12: aload_0
    //   13: getfield waiter : Lcom/bumptech/glide/request/RequestFutureTarget$Waiter;
    //   16: aload_0
    //   17: invokevirtual notifyAll : (Ljava/lang/Object;)V
    //   20: aload_0
    //   21: monitorexit
    //   22: iconst_0
    //   23: ireturn
    //   24: astore_1
    //   25: aload_0
    //   26: monitorexit
    //   27: aload_1
    //   28: athrow
    // Exception table:
    //   from	to	target	type
    //   2	20	24	finally
  }
  
  public void onStart() {}
  
  public void onStop() {}
  
  public void removeCallback(SizeReadyCallback paramSizeReadyCallback) {}
  
  public void setRequest(Request paramRequest) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: putfield request : Lcom/bumptech/glide/request/Request;
    //   7: aload_0
    //   8: monitorexit
    //   9: return
    //   10: astore_1
    //   11: aload_0
    //   12: monitorexit
    //   13: aload_1
    //   14: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	10	finally
  }
  
  static class Waiter {
    void notifyAll(Object param1Object) {
      param1Object.notifyAll();
    }
    
    void waitForTimeout(Object param1Object, long param1Long) throws InterruptedException {
      param1Object.wait(param1Long);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/request/RequestFutureTarget.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */