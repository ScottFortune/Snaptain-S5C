package com.bumptech.glide.request.transition;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import com.bumptech.glide.load.DataSource;

public abstract class BitmapContainerTransitionFactory<R> implements TransitionFactory<R> {
  private final TransitionFactory<Drawable> realFactory;
  
  public BitmapContainerTransitionFactory(TransitionFactory<Drawable> paramTransitionFactory) {
    this.realFactory = paramTransitionFactory;
  }
  
  public Transition<R> build(DataSource paramDataSource, boolean paramBoolean) {
    return new BitmapGlideAnimation(this.realFactory.build(paramDataSource, paramBoolean));
  }
  
  protected abstract Bitmap getBitmap(R paramR);
  
  private final class BitmapGlideAnimation implements Transition<R> {
    private final Transition<Drawable> transition;
    
    BitmapGlideAnimation(Transition<Drawable> param1Transition) {
      this.transition = param1Transition;
    }
    
    public boolean transition(R param1R, Transition.ViewAdapter param1ViewAdapter) {
      BitmapDrawable bitmapDrawable = new BitmapDrawable(param1ViewAdapter.getView().getResources(), BitmapContainerTransitionFactory.this.getBitmap(param1R));
      return this.transition.transition(bitmapDrawable, param1ViewAdapter);
    }
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/request/transition/BitmapContainerTransitionFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */