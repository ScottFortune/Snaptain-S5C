package com.dd.plist;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class NSDate extends NSObject {
  private static final long EPOCH = 978307200000L;
  
  private static final SimpleDateFormat sdfDefault = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
  
  private static final SimpleDateFormat sdfGnuStep = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
  
  private Date date;
  
  static {
    sdfDefault.setTimeZone(TimeZone.getTimeZone("GMT"));
    sdfGnuStep.setTimeZone(TimeZone.getTimeZone("GMT"));
  }
  
  public NSDate(String paramString) throws ParseException {
    this.date = parseDateString(paramString);
  }
  
  public NSDate(Date paramDate) {
    if (paramDate != null) {
      this.date = paramDate;
      return;
    } 
    throw new IllegalArgumentException("Date cannot be null");
  }
  
  public NSDate(byte[] paramArrayOfbyte) {
    this.date = new Date((long)(BinaryPropertyListParser.parseDouble(paramArrayOfbyte) * 1000.0D) + 978307200000L);
  }
  
  private static String makeDateString(Date paramDate) {
    // Byte code:
    //   0: ldc com/dd/plist/NSDate
    //   2: monitorenter
    //   3: getstatic com/dd/plist/NSDate.sdfDefault : Ljava/text/SimpleDateFormat;
    //   6: aload_0
    //   7: invokevirtual format : (Ljava/util/Date;)Ljava/lang/String;
    //   10: astore_0
    //   11: ldc com/dd/plist/NSDate
    //   13: monitorexit
    //   14: aload_0
    //   15: areturn
    //   16: astore_0
    //   17: ldc com/dd/plist/NSDate
    //   19: monitorexit
    //   20: aload_0
    //   21: athrow
    // Exception table:
    //   from	to	target	type
    //   3	11	16	finally
  }
  
  private static String makeDateStringGnuStep(Date paramDate) {
    // Byte code:
    //   0: ldc com/dd/plist/NSDate
    //   2: monitorenter
    //   3: getstatic com/dd/plist/NSDate.sdfGnuStep : Ljava/text/SimpleDateFormat;
    //   6: aload_0
    //   7: invokevirtual format : (Ljava/util/Date;)Ljava/lang/String;
    //   10: astore_0
    //   11: ldc com/dd/plist/NSDate
    //   13: monitorexit
    //   14: aload_0
    //   15: areturn
    //   16: astore_0
    //   17: ldc com/dd/plist/NSDate
    //   19: monitorexit
    //   20: aload_0
    //   21: athrow
    // Exception table:
    //   from	to	target	type
    //   3	11	16	finally
  }
  
  private static Date parseDateString(String paramString) throws ParseException {
    /* monitor enter TypeReferenceDotClassExpression{ObjectType{com/dd/plist/NSDate}} */
    try {
      Date date = sdfDefault.parse(paramString);
      /* monitor exit TypeReferenceDotClassExpression{ObjectType{com/dd/plist/NSDate}} */
      return date;
    } catch (ParseException parseException) {
      Date date = sdfGnuStep.parse(paramString);
      /* monitor exit TypeReferenceDotClassExpression{ObjectType{com/dd/plist/NSDate}} */
      return date;
    } finally {}
    /* monitor exit TypeReferenceDotClassExpression{ObjectType{com/dd/plist/NSDate}} */
    throw paramString;
  }
  
  public boolean equals(Object paramObject) {
    boolean bool;
    if (paramObject.getClass().equals(getClass()) && this.date.equals(((NSDate)paramObject).getDate())) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  public Date getDate() {
    return this.date;
  }
  
  public int hashCode() {
    return this.date.hashCode();
  }
  
  protected void toASCII(StringBuilder paramStringBuilder, int paramInt) {
    indent(paramStringBuilder, paramInt);
    paramStringBuilder.append("\"");
    paramStringBuilder.append(makeDateString(this.date));
    paramStringBuilder.append("\"");
  }
  
  protected void toASCIIGnuStep(StringBuilder paramStringBuilder, int paramInt) {
    indent(paramStringBuilder, paramInt);
    paramStringBuilder.append("<*D");
    paramStringBuilder.append(makeDateStringGnuStep(this.date));
    paramStringBuilder.append(">");
  }
  
  public void toBinary(BinaryPropertyListWriter paramBinaryPropertyListWriter) throws IOException {
    paramBinaryPropertyListWriter.write(51);
    double d = (this.date.getTime() - 978307200000L);
    Double.isNaN(d);
    paramBinaryPropertyListWriter.writeDouble(d / 1000.0D);
  }
  
  public String toString() {
    return this.date.toString();
  }
  
  void toXML(StringBuilder paramStringBuilder, int paramInt) {
    indent(paramStringBuilder, paramInt);
    paramStringBuilder.append("<date>");
    paramStringBuilder.append(makeDateString(this.date));
    paramStringBuilder.append("</date>");
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/dd/plist/NSDate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */