package androidx.versionedparcelable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE})
public @interface VersionedParcelize {
  boolean allowSerialization() default false;
  
  int[] deprecatedIds() default {};
  
  Class factory() default void.class;
  
  boolean ignoreParcelables() default false;
  
  boolean isCustom() default false;
  
  String jetifyAs() default "";
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/androidx/versionedparcelable/VersionedParcelize.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */