package io.reactivex.internal.operators.single;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;
import io.reactivex.internal.functions.ObjectHelper;

public final class SingleZipIterable<T, R> extends Single<R> {
  final Iterable<? extends SingleSource<? extends T>> sources;
  
  final Function<? super Object[], ? extends R> zipper;
  
  public SingleZipIterable(Iterable<? extends SingleSource<? extends T>> paramIterable, Function<? super Object[], ? extends R> paramFunction) {
    this.sources = paramIterable;
    this.zipper = paramFunction;
  }
  
  protected void subscribeActual(SingleObserver<? super R> paramSingleObserver) {
    // Byte code:
    //   0: bipush #8
    //   2: anewarray io/reactivex/SingleSource
    //   5: astore_2
    //   6: aload_0
    //   7: getfield sources : Ljava/lang/Iterable;
    //   10: invokeinterface iterator : ()Ljava/util/Iterator;
    //   15: astore_3
    //   16: iconst_0
    //   17: istore #4
    //   19: iconst_0
    //   20: istore #5
    //   22: aload_3
    //   23: invokeinterface hasNext : ()Z
    //   28: ifeq -> 105
    //   31: aload_3
    //   32: invokeinterface next : ()Ljava/lang/Object;
    //   37: checkcast io/reactivex/SingleSource
    //   40: astore #6
    //   42: aload #6
    //   44: ifnonnull -> 63
    //   47: new java/lang/NullPointerException
    //   50: astore_2
    //   51: aload_2
    //   52: ldc 'One of the sources is null'
    //   54: invokespecial <init> : (Ljava/lang/String;)V
    //   57: aload_2
    //   58: aload_1
    //   59: invokestatic error : (Ljava/lang/Throwable;Lio/reactivex/SingleObserver;)V
    //   62: return
    //   63: aload_2
    //   64: astore #7
    //   66: iload #5
    //   68: aload_2
    //   69: arraylength
    //   70: if_icmpne -> 89
    //   73: aload_2
    //   74: iload #5
    //   76: iconst_2
    //   77: ishr
    //   78: iload #5
    //   80: iadd
    //   81: invokestatic copyOf : ([Ljava/lang/Object;I)[Ljava/lang/Object;
    //   84: checkcast [Lio/reactivex/SingleSource;
    //   87: astore #7
    //   89: aload #7
    //   91: iload #5
    //   93: aload #6
    //   95: aastore
    //   96: iinc #5, 1
    //   99: aload #7
    //   101: astore_2
    //   102: goto -> 22
    //   105: iload #5
    //   107: ifne -> 122
    //   110: new java/util/NoSuchElementException
    //   113: dup
    //   114: invokespecial <init> : ()V
    //   117: aload_1
    //   118: invokestatic error : (Ljava/lang/Throwable;Lio/reactivex/SingleObserver;)V
    //   121: return
    //   122: iload #5
    //   124: iconst_1
    //   125: if_icmpne -> 153
    //   128: aload_2
    //   129: iconst_0
    //   130: aaload
    //   131: new io/reactivex/internal/operators/single/SingleMap$MapSingleObserver
    //   134: dup
    //   135: aload_1
    //   136: new io/reactivex/internal/operators/single/SingleZipIterable$SingletonArrayFunc
    //   139: dup
    //   140: aload_0
    //   141: invokespecial <init> : (Lio/reactivex/internal/operators/single/SingleZipIterable;)V
    //   144: invokespecial <init> : (Lio/reactivex/SingleObserver;Lio/reactivex/functions/Function;)V
    //   147: invokeinterface subscribe : (Lio/reactivex/SingleObserver;)V
    //   152: return
    //   153: new io/reactivex/internal/operators/single/SingleZipArray$ZipCoordinator
    //   156: dup
    //   157: aload_1
    //   158: iload #5
    //   160: aload_0
    //   161: getfield zipper : Lio/reactivex/functions/Function;
    //   164: invokespecial <init> : (Lio/reactivex/SingleObserver;ILio/reactivex/functions/Function;)V
    //   167: astore #7
    //   169: aload_1
    //   170: aload #7
    //   172: invokeinterface onSubscribe : (Lio/reactivex/disposables/Disposable;)V
    //   177: iload #4
    //   179: iload #5
    //   181: if_icmpge -> 216
    //   184: aload #7
    //   186: invokevirtual isDisposed : ()Z
    //   189: ifeq -> 193
    //   192: return
    //   193: aload_2
    //   194: iload #4
    //   196: aaload
    //   197: aload #7
    //   199: getfield observers : [Lio/reactivex/internal/operators/single/SingleZipArray$ZipSingleObserver;
    //   202: iload #4
    //   204: aaload
    //   205: invokeinterface subscribe : (Lio/reactivex/SingleObserver;)V
    //   210: iinc #4, 1
    //   213: goto -> 177
    //   216: return
    //   217: astore_2
    //   218: aload_2
    //   219: invokestatic throwIfFatal : (Ljava/lang/Throwable;)V
    //   222: aload_2
    //   223: aload_1
    //   224: invokestatic error : (Ljava/lang/Throwable;Lio/reactivex/SingleObserver;)V
    //   227: return
    // Exception table:
    //   from	to	target	type
    //   6	16	217	finally
    //   22	42	217	finally
    //   47	62	217	finally
    //   66	89	217	finally
  }
  
  final class SingletonArrayFunc implements Function<T, R> {
    public R apply(T param1T) throws Exception {
      return (R)ObjectHelper.requireNonNull(SingleZipIterable.this.zipper.apply(new Object[] { param1T }, ), "The zipper returned a null value");
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/single/SingleZipIterable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */