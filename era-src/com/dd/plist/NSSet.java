package com.dd.plist;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

public class NSSet extends NSObject {
  private boolean ordered = false;
  
  private Set<NSObject> set;
  
  public NSSet() {
    this.set = new LinkedHashSet<NSObject>();
  }
  
  public NSSet(boolean paramBoolean) {
    this.ordered = paramBoolean;
    if (!paramBoolean) {
      this.set = new LinkedHashSet<NSObject>();
    } else {
      this.set = new TreeSet<NSObject>();
    } 
  }
  
  public NSSet(boolean paramBoolean, NSObject... paramVarArgs) {
    this.ordered = paramBoolean;
    if (!paramBoolean) {
      this.set = new LinkedHashSet<NSObject>();
    } else {
      this.set = new TreeSet<NSObject>();
    } 
    this.set.addAll(Arrays.asList(paramVarArgs));
  }
  
  public NSSet(NSObject... paramVarArgs) {
    this.set = new LinkedHashSet<NSObject>();
    this.set.addAll(Arrays.asList(paramVarArgs));
  }
  
  public void addObject(NSObject paramNSObject) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield set : Ljava/util/Set;
    //   6: aload_1
    //   7: invokeinterface add : (Ljava/lang/Object;)Z
    //   12: pop
    //   13: aload_0
    //   14: monitorexit
    //   15: return
    //   16: astore_1
    //   17: aload_0
    //   18: monitorexit
    //   19: aload_1
    //   20: athrow
    // Exception table:
    //   from	to	target	type
    //   2	13	16	finally
  }
  
  public NSObject[] allObjects() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield set : Ljava/util/Set;
    //   6: aload_0
    //   7: invokevirtual count : ()I
    //   10: anewarray com/dd/plist/NSObject
    //   13: invokeinterface toArray : ([Ljava/lang/Object;)[Ljava/lang/Object;
    //   18: checkcast [Lcom/dd/plist/NSObject;
    //   21: astore_1
    //   22: aload_0
    //   23: monitorexit
    //   24: aload_1
    //   25: areturn
    //   26: astore_1
    //   27: aload_0
    //   28: monitorexit
    //   29: aload_1
    //   30: athrow
    // Exception table:
    //   from	to	target	type
    //   2	22	26	finally
  }
  
  public NSObject anyObject() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield set : Ljava/util/Set;
    //   6: invokeinterface isEmpty : ()Z
    //   11: istore_1
    //   12: iload_1
    //   13: ifeq -> 20
    //   16: aload_0
    //   17: monitorexit
    //   18: aconst_null
    //   19: areturn
    //   20: aload_0
    //   21: getfield set : Ljava/util/Set;
    //   24: invokeinterface iterator : ()Ljava/util/Iterator;
    //   29: invokeinterface next : ()Ljava/lang/Object;
    //   34: checkcast com/dd/plist/NSObject
    //   37: astore_2
    //   38: aload_0
    //   39: monitorexit
    //   40: aload_2
    //   41: areturn
    //   42: astore_2
    //   43: aload_0
    //   44: monitorexit
    //   45: aload_2
    //   46: athrow
    // Exception table:
    //   from	to	target	type
    //   2	12	42	finally
    //   20	38	42	finally
  }
  
  void assignIDs(BinaryPropertyListWriter paramBinaryPropertyListWriter) {
    super.assignIDs(paramBinaryPropertyListWriter);
    Iterator<NSObject> iterator = this.set.iterator();
    while (iterator.hasNext())
      ((NSObject)iterator.next()).assignIDs(paramBinaryPropertyListWriter); 
  }
  
  public boolean containsObject(NSObject paramNSObject) {
    return this.set.contains(paramNSObject);
  }
  
  public int count() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield set : Ljava/util/Set;
    //   6: invokeinterface size : ()I
    //   11: istore_1
    //   12: aload_0
    //   13: monitorexit
    //   14: iload_1
    //   15: ireturn
    //   16: astore_2
    //   17: aload_0
    //   18: monitorexit
    //   19: aload_2
    //   20: athrow
    // Exception table:
    //   from	to	target	type
    //   2	12	16	finally
  }
  
  public boolean equals(Object<NSObject> paramObject) {
    boolean bool = false;
    if (paramObject == null)
      return false; 
    if (getClass() != paramObject.getClass())
      return false; 
    NSSet nSSet = (NSSet)paramObject;
    paramObject = (Object<NSObject>)this.set;
    Set<NSObject> set = nSSet.set;
    if (paramObject != set) {
      boolean bool1 = bool;
      if (paramObject != null) {
        bool1 = bool;
        if (paramObject.equals(set))
          return true; 
      } 
      return bool1;
    } 
    return true;
  }
  
  Set<NSObject> getSet() {
    return this.set;
  }
  
  public int hashCode() {
    byte b;
    Set<NSObject> set = this.set;
    if (set != null) {
      b = set.hashCode();
    } else {
      b = 0;
    } 
    return 203 + b;
  }
  
  public boolean intersectsSet(NSSet paramNSSet) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield set : Ljava/util/Set;
    //   6: invokeinterface iterator : ()Ljava/util/Iterator;
    //   11: astore_2
    //   12: aload_2
    //   13: invokeinterface hasNext : ()Z
    //   18: ifeq -> 43
    //   21: aload_1
    //   22: aload_2
    //   23: invokeinterface next : ()Ljava/lang/Object;
    //   28: checkcast com/dd/plist/NSObject
    //   31: invokevirtual containsObject : (Lcom/dd/plist/NSObject;)Z
    //   34: istore_3
    //   35: iload_3
    //   36: ifeq -> 12
    //   39: aload_0
    //   40: monitorexit
    //   41: iconst_1
    //   42: ireturn
    //   43: aload_0
    //   44: monitorexit
    //   45: iconst_0
    //   46: ireturn
    //   47: astore_1
    //   48: aload_0
    //   49: monitorexit
    //   50: goto -> 55
    //   53: aload_1
    //   54: athrow
    //   55: goto -> 53
    // Exception table:
    //   from	to	target	type
    //   2	12	47	finally
    //   12	35	47	finally
  }
  
  public boolean isSubsetOfSet(NSSet paramNSSet) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield set : Ljava/util/Set;
    //   6: invokeinterface iterator : ()Ljava/util/Iterator;
    //   11: astore_2
    //   12: aload_2
    //   13: invokeinterface hasNext : ()Z
    //   18: ifeq -> 43
    //   21: aload_1
    //   22: aload_2
    //   23: invokeinterface next : ()Ljava/lang/Object;
    //   28: checkcast com/dd/plist/NSObject
    //   31: invokevirtual containsObject : (Lcom/dd/plist/NSObject;)Z
    //   34: istore_3
    //   35: iload_3
    //   36: ifne -> 12
    //   39: aload_0
    //   40: monitorexit
    //   41: iconst_0
    //   42: ireturn
    //   43: aload_0
    //   44: monitorexit
    //   45: iconst_1
    //   46: ireturn
    //   47: astore_1
    //   48: aload_0
    //   49: monitorexit
    //   50: goto -> 55
    //   53: aload_1
    //   54: athrow
    //   55: goto -> 53
    // Exception table:
    //   from	to	target	type
    //   2	12	47	finally
    //   12	35	47	finally
  }
  
  public NSObject member(NSObject paramNSObject) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield set : Ljava/util/Set;
    //   6: invokeinterface iterator : ()Ljava/util/Iterator;
    //   11: astore_2
    //   12: aload_2
    //   13: invokeinterface hasNext : ()Z
    //   18: ifeq -> 47
    //   21: aload_2
    //   22: invokeinterface next : ()Ljava/lang/Object;
    //   27: checkcast com/dd/plist/NSObject
    //   30: astore_3
    //   31: aload_3
    //   32: aload_1
    //   33: invokevirtual equals : (Ljava/lang/Object;)Z
    //   36: istore #4
    //   38: iload #4
    //   40: ifeq -> 12
    //   43: aload_0
    //   44: monitorexit
    //   45: aload_3
    //   46: areturn
    //   47: aload_0
    //   48: monitorexit
    //   49: aconst_null
    //   50: areturn
    //   51: astore_1
    //   52: aload_0
    //   53: monitorexit
    //   54: goto -> 59
    //   57: aload_1
    //   58: athrow
    //   59: goto -> 57
    // Exception table:
    //   from	to	target	type
    //   2	12	51	finally
    //   12	38	51	finally
  }
  
  public Iterator<NSObject> objectIterator() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield set : Ljava/util/Set;
    //   6: invokeinterface iterator : ()Ljava/util/Iterator;
    //   11: astore_1
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_1
    //   15: areturn
    //   16: astore_1
    //   17: aload_0
    //   18: monitorexit
    //   19: aload_1
    //   20: athrow
    // Exception table:
    //   from	to	target	type
    //   2	12	16	finally
  }
  
  public void removeObject(NSObject paramNSObject) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield set : Ljava/util/Set;
    //   6: aload_1
    //   7: invokeinterface remove : (Ljava/lang/Object;)Z
    //   12: pop
    //   13: aload_0
    //   14: monitorexit
    //   15: return
    //   16: astore_1
    //   17: aload_0
    //   18: monitorexit
    //   19: aload_1
    //   20: athrow
    // Exception table:
    //   from	to	target	type
    //   2	13	16	finally
  }
  
  protected void toASCII(StringBuilder paramStringBuilder, int paramInt) {
    indent(paramStringBuilder, paramInt);
    NSObject[] arrayOfNSObject = allObjects();
    paramStringBuilder.append('(');
    int i = paramStringBuilder.lastIndexOf(NEWLINE);
    for (byte b = 0; b < arrayOfNSObject.length; b++) {
      int j;
      Class<?> clazz = arrayOfNSObject[b].getClass();
      if ((clazz.equals(NSDictionary.class) || clazz.equals(NSArray.class) || clazz.equals(NSData.class)) && i != paramStringBuilder.length()) {
        paramStringBuilder.append(NEWLINE);
        j = paramStringBuilder.length();
        arrayOfNSObject[b].toASCII(paramStringBuilder, paramInt + 1);
      } else {
        if (b != 0)
          paramStringBuilder.append(" "); 
        arrayOfNSObject[b].toASCII(paramStringBuilder, 0);
        j = i;
      } 
      if (b != arrayOfNSObject.length - 1)
        paramStringBuilder.append(','); 
      i = j;
      if (paramStringBuilder.length() - j > 80) {
        paramStringBuilder.append(NEWLINE);
        i = paramStringBuilder.length();
      } 
    } 
    paramStringBuilder.append(')');
  }
  
  protected void toASCIIGnuStep(StringBuilder paramStringBuilder, int paramInt) {
    indent(paramStringBuilder, paramInt);
    NSObject[] arrayOfNSObject = allObjects();
    paramStringBuilder.append('(');
    int i = paramStringBuilder.lastIndexOf(NEWLINE);
    for (byte b = 0; b < arrayOfNSObject.length; b++) {
      int j;
      Class<?> clazz = arrayOfNSObject[b].getClass();
      if ((clazz.equals(NSDictionary.class) || clazz.equals(NSArray.class) || clazz.equals(NSData.class)) && i != paramStringBuilder.length()) {
        paramStringBuilder.append(NEWLINE);
        j = paramStringBuilder.length();
        arrayOfNSObject[b].toASCIIGnuStep(paramStringBuilder, paramInt + 1);
      } else {
        if (b != 0)
          paramStringBuilder.append(" "); 
        arrayOfNSObject[b].toASCIIGnuStep(paramStringBuilder, 0);
        j = i;
      } 
      if (b != arrayOfNSObject.length - 1)
        paramStringBuilder.append(','); 
      i = j;
      if (paramStringBuilder.length() - j > 80) {
        paramStringBuilder.append(NEWLINE);
        i = paramStringBuilder.length();
      } 
    } 
    paramStringBuilder.append(')');
  }
  
  void toBinary(BinaryPropertyListWriter paramBinaryPropertyListWriter) throws IOException {
    if (this.ordered) {
      paramBinaryPropertyListWriter.writeIntHeader(11, this.set.size());
    } else {
      paramBinaryPropertyListWriter.writeIntHeader(12, this.set.size());
    } 
    Iterator<NSObject> iterator = this.set.iterator();
    while (iterator.hasNext())
      paramBinaryPropertyListWriter.writeID(paramBinaryPropertyListWriter.getID(iterator.next())); 
  }
  
  void toXML(StringBuilder paramStringBuilder, int paramInt) {
    indent(paramStringBuilder, paramInt);
    paramStringBuilder.append("<array>");
    paramStringBuilder.append(NSObject.NEWLINE);
    Iterator<NSObject> iterator = this.set.iterator();
    while (iterator.hasNext()) {
      ((NSObject)iterator.next()).toXML(paramStringBuilder, paramInt + 1);
      paramStringBuilder.append(NSObject.NEWLINE);
    } 
    indent(paramStringBuilder, paramInt);
    paramStringBuilder.append("</array>");
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/dd/plist/NSSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */