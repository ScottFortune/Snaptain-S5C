package io.reactivex.internal.operators.maybe;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.functions.Function;
import io.reactivex.internal.functions.ObjectHelper;

public final class MaybeZipIterable<T, R> extends Maybe<R> {
  final Iterable<? extends MaybeSource<? extends T>> sources;
  
  final Function<? super Object[], ? extends R> zipper;
  
  public MaybeZipIterable(Iterable<? extends MaybeSource<? extends T>> paramIterable, Function<? super Object[], ? extends R> paramFunction) {
    this.sources = paramIterable;
    this.zipper = paramFunction;
  }
  
  protected void subscribeActual(MaybeObserver<? super R> paramMaybeObserver) {
    // Byte code:
    //   0: bipush #8
    //   2: anewarray io/reactivex/MaybeSource
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
    //   37: checkcast io/reactivex/MaybeSource
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
    //   59: invokestatic error : (Ljava/lang/Throwable;Lio/reactivex/MaybeObserver;)V
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
    //   84: checkcast [Lio/reactivex/MaybeSource;
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
    //   107: ifne -> 115
    //   110: aload_1
    //   111: invokestatic complete : (Lio/reactivex/MaybeObserver;)V
    //   114: return
    //   115: iload #5
    //   117: iconst_1
    //   118: if_icmpne -> 146
    //   121: aload_2
    //   122: iconst_0
    //   123: aaload
    //   124: new io/reactivex/internal/operators/maybe/MaybeMap$MapMaybeObserver
    //   127: dup
    //   128: aload_1
    //   129: new io/reactivex/internal/operators/maybe/MaybeZipIterable$SingletonArrayFunc
    //   132: dup
    //   133: aload_0
    //   134: invokespecial <init> : (Lio/reactivex/internal/operators/maybe/MaybeZipIterable;)V
    //   137: invokespecial <init> : (Lio/reactivex/MaybeObserver;Lio/reactivex/functions/Function;)V
    //   140: invokeinterface subscribe : (Lio/reactivex/MaybeObserver;)V
    //   145: return
    //   146: new io/reactivex/internal/operators/maybe/MaybeZipArray$ZipCoordinator
    //   149: dup
    //   150: aload_1
    //   151: iload #5
    //   153: aload_0
    //   154: getfield zipper : Lio/reactivex/functions/Function;
    //   157: invokespecial <init> : (Lio/reactivex/MaybeObserver;ILio/reactivex/functions/Function;)V
    //   160: astore #7
    //   162: aload_1
    //   163: aload #7
    //   165: invokeinterface onSubscribe : (Lio/reactivex/disposables/Disposable;)V
    //   170: iload #4
    //   172: iload #5
    //   174: if_icmpge -> 209
    //   177: aload #7
    //   179: invokevirtual isDisposed : ()Z
    //   182: ifeq -> 186
    //   185: return
    //   186: aload_2
    //   187: iload #4
    //   189: aaload
    //   190: aload #7
    //   192: getfield observers : [Lio/reactivex/internal/operators/maybe/MaybeZipArray$ZipMaybeObserver;
    //   195: iload #4
    //   197: aaload
    //   198: invokeinterface subscribe : (Lio/reactivex/MaybeObserver;)V
    //   203: iinc #4, 1
    //   206: goto -> 170
    //   209: return
    //   210: astore_2
    //   211: aload_2
    //   212: invokestatic throwIfFatal : (Ljava/lang/Throwable;)V
    //   215: aload_2
    //   216: aload_1
    //   217: invokestatic error : (Ljava/lang/Throwable;Lio/reactivex/MaybeObserver;)V
    //   220: return
    // Exception table:
    //   from	to	target	type
    //   6	16	210	finally
    //   22	42	210	finally
    //   47	62	210	finally
    //   66	89	210	finally
  }
  
  final class SingletonArrayFunc implements Function<T, R> {
    public R apply(T param1T) throws Exception {
      return (R)ObjectHelper.requireNonNull(MaybeZipIterable.this.zipper.apply(new Object[] { param1T }, ), "The zipper returned a null value");
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/operators/maybe/MaybeZipIterable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */