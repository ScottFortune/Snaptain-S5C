package com.dd.plist;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public class PropertyListParser {
  private static final int TYPE_ASCII = 2;
  
  private static final int TYPE_BINARY = 1;
  
  private static final int TYPE_ERROR_BLANK = 10;
  
  private static final int TYPE_ERROR_UNKNOWN = 11;
  
  private static final int TYPE_XML = 0;
  
  public static void convertToASCII(File paramFile1, File paramFile2) throws ParserConfigurationException, ParseException, SAXException, PropertyListFormatException, IOException {
    NSObject nSObject = parse(paramFile1);
    if (nSObject instanceof NSDictionary) {
      saveAsASCII((NSDictionary)nSObject, paramFile2);
    } else {
      if (nSObject instanceof NSArray) {
        saveAsASCII((NSArray)nSObject, paramFile2);
        return;
      } 
      throw new PropertyListFormatException("The root of the given input property list is neither a Dictionary nor an Array!");
    } 
  }
  
  public static void convertToBinary(File paramFile1, File paramFile2) throws IOException, ParserConfigurationException, ParseException, SAXException, PropertyListFormatException {
    saveAsBinary(parse(paramFile1), paramFile2);
  }
  
  public static void convertToGnuStepASCII(File paramFile1, File paramFile2) throws ParserConfigurationException, ParseException, SAXException, PropertyListFormatException, IOException {
    NSObject nSObject = parse(paramFile1);
    if (nSObject instanceof NSDictionary) {
      saveAsGnuStepASCII((NSDictionary)nSObject, paramFile2);
    } else {
      if (nSObject instanceof NSArray) {
        saveAsGnuStepASCII((NSArray)nSObject, paramFile2);
        return;
      } 
      throw new PropertyListFormatException("The root of the given input property list is neither a Dictionary nor an Array!");
    } 
  }
  
  public static void convertToXml(File paramFile1, File paramFile2) throws ParserConfigurationException, ParseException, SAXException, PropertyListFormatException, IOException {
    saveAsXML(parse(paramFile1), paramFile2);
  }
  
  private static int determineType(InputStream paramInputStream) throws IOException {
    byte[] arrayOfByte = new byte[8];
    long l = -1L;
    boolean bool = false;
    while (true) {
      while (true)
        break; 
      if (SYNTHETIC_LOCAL_VARIABLE_5 != 9) {
        Object object1 = SYNTHETIC_LOCAL_VARIABLE_6;
        Object object2 = SYNTHETIC_LOCAL_VARIABLE_8;
        if (SYNTHETIC_LOCAL_VARIABLE_5 != 13) {
          object1 = SYNTHETIC_LOCAL_VARIABLE_6;
          object2 = SYNTHETIC_LOCAL_VARIABLE_8;
          if (SYNTHETIC_LOCAL_VARIABLE_5 != 10) {
            object1 = SYNTHETIC_LOCAL_VARIABLE_6;
            object2 = SYNTHETIC_LOCAL_VARIABLE_8;
            if (SYNTHETIC_LOCAL_VARIABLE_5 != 12) {
              object1 = SYNTHETIC_LOCAL_VARIABLE_6;
              object2 = SYNTHETIC_LOCAL_VARIABLE_8;
              if (SYNTHETIC_LOCAL_VARIABLE_8 == null) {
                arrayOfByte[0] = (byte)(byte)SYNTHETIC_LOCAL_VARIABLE_5;
                int i = determineType(new String(arrayOfByte, 0, paramInputStream.read(arrayOfByte, 1, 7)));
                if (paramInputStream.markSupported())
                  paramInputStream.reset(); 
                return i;
              } 
            } 
          } 
        } 
      } 
    } 
  }
  
  private static int determineType(String paramString) {
    paramString = paramString.trim();
    return (paramString.length() == 0) ? 10 : (paramString.startsWith("bplist") ? 1 : ((paramString.startsWith("(") || paramString.startsWith("{") || paramString.startsWith("/")) ? 2 : (paramString.startsWith("<") ? 0 : 11)));
  }
  
  private static int determineType(byte[] paramArrayOfbyte) {
    int i = paramArrayOfbyte.length;
    byte b = 3;
    if (i < 3 || (paramArrayOfbyte[0] & 0xFF) != 239 || (paramArrayOfbyte[1] & 0xFF) != 187 || (paramArrayOfbyte[2] & 0xFF) != 191)
      b = 0; 
    while (true) {
      if ((b < paramArrayOfbyte.length && paramArrayOfbyte[b] == 32) || paramArrayOfbyte[b] == 9 || paramArrayOfbyte[b] == 13 || paramArrayOfbyte[b] == 10 || paramArrayOfbyte[b] == 12) {
        b++;
        continue;
      } 
      return determineType(new String(paramArrayOfbyte, b, Math.min(8, paramArrayOfbyte.length - b)));
    } 
  }
  
  public static NSObject parse(File paramFile) throws IOException, PropertyListFormatException, ParseException, ParserConfigurationException, SAXException {
    FileInputStream fileInputStream = new FileInputStream(paramFile);
    int i = determineType(fileInputStream);
    fileInputStream.close();
    if (i != 0) {
      if (i != 1) {
        if (i == 2)
          return ASCIIPropertyListParser.parse(paramFile); 
        throw new PropertyListFormatException("The given file is not a property list of a supported format.");
      } 
      return BinaryPropertyListParser.parse(paramFile);
    } 
    return XMLPropertyListParser.parse(paramFile);
  }
  
  public static NSObject parse(InputStream paramInputStream) throws IOException, PropertyListFormatException, ParseException, ParserConfigurationException, SAXException {
    return parse(readAll(paramInputStream));
  }
  
  public static NSObject parse(String paramString) throws ParserConfigurationException, ParseException, SAXException, PropertyListFormatException, IOException {
    return parse(new File(paramString));
  }
  
  public static NSObject parse(byte[] paramArrayOfbyte) throws IOException, PropertyListFormatException, ParseException, ParserConfigurationException, SAXException {
    int i = determineType(paramArrayOfbyte);
    if (i != 0) {
      if (i != 1) {
        if (i == 2)
          return ASCIIPropertyListParser.parse(paramArrayOfbyte); 
        throw new PropertyListFormatException("The given data is not a property list of a supported format.");
      } 
      return BinaryPropertyListParser.parse(paramArrayOfbyte);
    } 
    return XMLPropertyListParser.parse(paramArrayOfbyte);
  }
  
  protected static byte[] readAll(InputStream paramInputStream) throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    byte[] arrayOfByte = new byte[512];
    int i = 512;
    while (i == 512) {
      int j = paramInputStream.read(arrayOfByte);
      i = j;
      if (j != -1) {
        byteArrayOutputStream.write(arrayOfByte, 0, j);
        i = j;
      } 
    } 
    return byteArrayOutputStream.toByteArray();
  }
  
  public static void saveAsASCII(NSArray paramNSArray, File paramFile) throws IOException {
    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(paramFile), "ASCII");
    outputStreamWriter.write(paramNSArray.toASCIIPropertyList());
    outputStreamWriter.close();
  }
  
  public static void saveAsASCII(NSDictionary paramNSDictionary, File paramFile) throws IOException {
    File file = paramFile.getParentFile();
    if (file.exists() || file.mkdirs()) {
      OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(paramFile), "ASCII");
      outputStreamWriter.write(paramNSDictionary.toASCIIPropertyList());
      outputStreamWriter.close();
      return;
    } 
    throw new IOException("The output directory does not exist and could not be created.");
  }
  
  public static void saveAsBinary(NSObject paramNSObject, File paramFile) throws IOException {
    File file = paramFile.getParentFile();
    if (file.exists() || file.mkdirs()) {
      BinaryPropertyListWriter.write(paramFile, paramNSObject);
      return;
    } 
    throw new IOException("The output directory does not exist and could not be created.");
  }
  
  public static void saveAsBinary(NSObject paramNSObject, OutputStream paramOutputStream) throws IOException {
    BinaryPropertyListWriter.write(paramOutputStream, paramNSObject);
  }
  
  public static void saveAsGnuStepASCII(NSArray paramNSArray, File paramFile) throws IOException {
    File file = paramFile.getParentFile();
    if (file.exists() || file.mkdirs()) {
      OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(paramFile), "ASCII");
      outputStreamWriter.write(paramNSArray.toGnuStepASCIIPropertyList());
      outputStreamWriter.close();
      return;
    } 
    throw new IOException("The output directory does not exist and could not be created.");
  }
  
  public static void saveAsGnuStepASCII(NSDictionary paramNSDictionary, File paramFile) throws IOException {
    File file = paramFile.getParentFile();
    if (file.exists() || file.mkdirs()) {
      OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(paramFile), "ASCII");
      outputStreamWriter.write(paramNSDictionary.toGnuStepASCIIPropertyList());
      outputStreamWriter.close();
      return;
    } 
    throw new IOException("The output directory does not exist and could not be created.");
  }
  
  public static void saveAsXML(NSObject paramNSObject, File paramFile) throws IOException {
    File file = paramFile.getParentFile();
    if (file.exists() || file.mkdirs()) {
      FileOutputStream fileOutputStream = new FileOutputStream(paramFile);
      saveAsXML(paramNSObject, fileOutputStream);
      fileOutputStream.close();
      return;
    } 
    throw new IOException("The output directory does not exist and could not be created.");
  }
  
  public static void saveAsXML(NSObject paramNSObject, OutputStream paramOutputStream) throws IOException {
    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(paramOutputStream, "UTF-8");
    outputStreamWriter.write(paramNSObject.toXMLPropertyList());
    outputStreamWriter.close();
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/dd/plist/PropertyListParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */