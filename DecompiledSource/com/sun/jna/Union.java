package com.sun.jna;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class Union extends Structure {
  private Structure.StructField activeField;
  
  protected Union() {}
  
  protected Union(Pointer paramPointer) {
    super(paramPointer);
  }
  
  protected Union(Pointer paramPointer, int paramInt) {
    super(paramPointer, paramInt);
  }
  
  protected Union(Pointer paramPointer, int paramInt, TypeMapper paramTypeMapper) {
    super(paramPointer, paramInt, paramTypeMapper);
  }
  
  protected Union(TypeMapper paramTypeMapper) {
    super(paramTypeMapper);
  }
  
  private Structure.StructField findField(Class<?> paramClass) {
    ensureAllocated();
    for (Structure.StructField structField : fields().values()) {
      if (structField.type.isAssignableFrom(paramClass))
        return structField; 
    } 
    return null;
  }
  
  protected List<String> getFieldOrder() {
    List<Field> list = getFieldList();
    ArrayList<String> arrayList = new ArrayList(list.size());
    Iterator<Field> iterator = list.iterator();
    while (iterator.hasNext())
      arrayList.add(((Field)iterator.next()).getName()); 
    return arrayList;
  }
  
  protected int getNativeAlignment(Class<?> paramClass, Object paramObject, boolean paramBoolean) {
    return super.getNativeAlignment(paramClass, paramObject, true);
  }
  
  public Object getTypedValue(Class<?> paramClass) {
    ensureAllocated();
    for (Structure.StructField structField : fields().values()) {
      if (structField.type == paramClass) {
        this.activeField = structField;
        read();
        return getFieldValue(this.activeField.field);
      } 
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("No field of type ");
    stringBuilder.append(paramClass);
    stringBuilder.append(" in ");
    stringBuilder.append(this);
    IllegalArgumentException illegalArgumentException = new IllegalArgumentException(stringBuilder.toString());
    throw illegalArgumentException;
  }
  
  protected Object readField(Structure.StructField paramStructField) {
    return (paramStructField == this.activeField || (!Structure.class.isAssignableFrom(paramStructField.type) && !String.class.isAssignableFrom(paramStructField.type) && !WString.class.isAssignableFrom(paramStructField.type))) ? super.readField(paramStructField) : null;
  }
  
  public Object readField(String paramString) {
    ensureAllocated();
    setType(paramString);
    return super.readField(paramString);
  }
  
  public void setType(Class<?> paramClass) {
    ensureAllocated();
    for (Structure.StructField structField : fields().values()) {
      if (structField.type == paramClass) {
        this.activeField = structField;
        return;
      } 
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("No field of type ");
    stringBuilder.append(paramClass);
    stringBuilder.append(" in ");
    stringBuilder.append(this);
    IllegalArgumentException illegalArgumentException = new IllegalArgumentException(stringBuilder.toString());
    throw illegalArgumentException;
  }
  
  public void setType(String paramString) {
    ensureAllocated();
    Structure.StructField structField = fields().get(paramString);
    if (structField != null) {
      this.activeField = structField;
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("No field named ");
    stringBuilder.append(paramString);
    stringBuilder.append(" in ");
    stringBuilder.append(this);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public Object setTypedValue(Object paramObject) {
    Structure.StructField structField = findField(paramObject.getClass());
    if (structField != null) {
      this.activeField = structField;
      setFieldValue(structField.field, paramObject);
      return this;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("No field of type ");
    stringBuilder.append(paramObject.getClass());
    stringBuilder.append(" in ");
    stringBuilder.append(this);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  protected void writeField(Structure.StructField paramStructField) {
    if (paramStructField == this.activeField)
      super.writeField(paramStructField); 
  }
  
  public void writeField(String paramString) {
    ensureAllocated();
    setType(paramString);
    super.writeField(paramString);
  }
  
  public void writeField(String paramString, Object paramObject) {
    ensureAllocated();
    setType(paramString);
    super.writeField(paramString, paramObject);
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/sun/jna/Union.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */