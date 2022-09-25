package io.reactivex.internal.util;

import io.reactivex.functions.BiFunction;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public final class MergerBiFunction<T> implements BiFunction<List<T>, List<T>, List<T>> {
  final Comparator<? super T> comparator;
  
  public MergerBiFunction(Comparator<? super T> paramComparator) {
    this.comparator = paramComparator;
  }
  
  public List<T> apply(List<T> paramList1, List<T> paramList2) throws Exception {
    int i = paramList1.size() + paramList2.size();
    if (i == 0)
      return new ArrayList<T>(); 
    ArrayList<List<T>> arrayList = new ArrayList(i);
    Iterator<T> iterator1 = paramList1.iterator();
    Iterator<T> iterator2 = paramList2.iterator();
    if (iterator1.hasNext()) {
      paramList1 = (List<T>)iterator1.next();
    } else {
      paramList1 = null;
    } 
    List<T> list = paramList1;
    if (iterator2.hasNext()) {
      paramList2 = (List<T>)iterator2.next();
      while (true) {
        if (paramList1 != null && paramList2 != null) {
          if (this.comparator.compare((T)paramList1, (T)paramList2) < 0) {
            arrayList.add(paramList1);
            if (iterator1.hasNext()) {
              paramList1 = (List<T>)iterator1.next();
              continue;
            } 
            paramList1 = null;
            continue;
          } 
          arrayList.add(paramList2);
          list = paramList1;
          if (iterator2.hasNext()) {
            paramList2 = (List<T>)iterator2.next();
            continue;
          } 
        } else {
          break;
        } 
        paramList2 = null;
        paramList1 = list;
      } 
      if (paramList1 != null) {
        arrayList.add(paramList1);
        while (iterator1.hasNext())
          arrayList.add((List<T>)iterator1.next()); 
      } else {
        arrayList.add(paramList2);
        while (iterator2.hasNext())
          arrayList.add((List<T>)iterator2.next()); 
      } 
      return (List)arrayList;
    } 
    paramList2 = null;
    paramList1 = list;
    continue;
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/io/reactivex/internal/util/MergerBiFunction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */