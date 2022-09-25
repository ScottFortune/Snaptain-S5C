package androidx.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.ANNOTATION_TYPE})
public @interface LongDef {
  boolean flag() default false;
  
  boolean open() default false;
  
  long[] value() default {};
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/androidx/annotation/LongDef.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */