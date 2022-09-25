package com.bumptech.glide.manager;

import com.bumptech.glide.RequestManager;
import java.util.Collections;
import java.util.Set;

final class EmptyRequestManagerTreeNode implements RequestManagerTreeNode {
  public Set<RequestManager> getDescendants() {
    return Collections.emptySet();
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/bumptech/glide/manager/EmptyRequestManagerTreeNode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */