package com.bumptech.glide.request.transition;

import com.bumptech.glide.load.DataSource;

public interface TransitionFactory<R> {
  Transition<R> build(DataSource paramDataSource, boolean paramBoolean);
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/request/transition/TransitionFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */