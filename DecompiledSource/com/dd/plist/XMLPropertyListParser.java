package com.dd.plist;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XMLPropertyListParser {
  private static DocumentBuilderFactory docBuilderFactory;
  
  private static List<Node> filterElementNodes(NodeList paramNodeList) {
    ArrayList<Node> arrayList = new ArrayList(paramNodeList.getLength());
    for (byte b = 0; b < paramNodeList.getLength(); b++) {
      if (paramNodeList.item(b).getNodeType() == 1)
        arrayList.add(paramNodeList.item(b)); 
    } 
    return arrayList;
  }
  
  private static DocumentBuilder getDocBuilder() throws ParserConfigurationException {
    // Byte code:
    //   0: ldc com/dd/plist/XMLPropertyListParser
    //   2: monitorenter
    //   3: getstatic com/dd/plist/XMLPropertyListParser.docBuilderFactory : Ljavax/xml/parsers/DocumentBuilderFactory;
    //   6: ifnonnull -> 12
    //   9: invokestatic initDocBuilderFactory : ()V
    //   12: getstatic com/dd/plist/XMLPropertyListParser.docBuilderFactory : Ljavax/xml/parsers/DocumentBuilderFactory;
    //   15: invokevirtual newDocumentBuilder : ()Ljavax/xml/parsers/DocumentBuilder;
    //   18: astore_0
    //   19: new com/dd/plist/XMLPropertyListParser$1
    //   22: astore_1
    //   23: aload_1
    //   24: invokespecial <init> : ()V
    //   27: aload_0
    //   28: aload_1
    //   29: invokevirtual setEntityResolver : (Lorg/xml/sax/EntityResolver;)V
    //   32: ldc com/dd/plist/XMLPropertyListParser
    //   34: monitorexit
    //   35: aload_0
    //   36: areturn
    //   37: astore_0
    //   38: ldc com/dd/plist/XMLPropertyListParser
    //   40: monitorexit
    //   41: aload_0
    //   42: athrow
    // Exception table:
    //   from	to	target	type
    //   3	12	37	finally
    //   12	32	37	finally
  }
  
  private static String getNodeTextContents(Node paramNode) {
    String str;
    if (paramNode.getNodeType() == 3 || paramNode.getNodeType() == 4) {
      str = ((Text)paramNode).getWholeText();
      return (str == null) ? "" : str;
    } 
    if (str.hasChildNodes()) {
      NodeList nodeList = str.getChildNodes();
      for (byte b = 0; b < nodeList.getLength(); b++) {
        Node node = nodeList.item(b);
        if (node.getNodeType() == 3 || node.getNodeType() == 4) {
          String str1 = ((Text)node).getWholeText();
          return (str1 == null) ? "" : str1;
        } 
      } 
    } 
    return "";
  }
  
  private static void initDocBuilderFactory() {
    // Byte code:
    //   0: ldc com/dd/plist/XMLPropertyListParser
    //   2: monitorenter
    //   3: invokestatic newInstance : ()Ljavax/xml/parsers/DocumentBuilderFactory;
    //   6: putstatic com/dd/plist/XMLPropertyListParser.docBuilderFactory : Ljavax/xml/parsers/DocumentBuilderFactory;
    //   9: getstatic com/dd/plist/XMLPropertyListParser.docBuilderFactory : Ljavax/xml/parsers/DocumentBuilderFactory;
    //   12: iconst_1
    //   13: invokevirtual setIgnoringComments : (Z)V
    //   16: getstatic com/dd/plist/XMLPropertyListParser.docBuilderFactory : Ljavax/xml/parsers/DocumentBuilderFactory;
    //   19: iconst_1
    //   20: invokevirtual setCoalescing : (Z)V
    //   23: ldc com/dd/plist/XMLPropertyListParser
    //   25: monitorexit
    //   26: return
    //   27: astore_0
    //   28: ldc com/dd/plist/XMLPropertyListParser
    //   30: monitorexit
    //   31: aload_0
    //   32: athrow
    // Exception table:
    //   from	to	target	type
    //   3	23	27	finally
  }
  
  public static NSObject parse(File paramFile) throws ParserConfigurationException, IOException, SAXException, PropertyListFormatException, ParseException {
    return parseDocument(getDocBuilder().parse(paramFile));
  }
  
  public static NSObject parse(InputStream paramInputStream) throws ParserConfigurationException, IOException, SAXException, PropertyListFormatException, ParseException {
    return parseDocument(getDocBuilder().parse(paramInputStream));
  }
  
  public static NSObject parse(byte[] paramArrayOfbyte) throws ParserConfigurationException, ParseException, SAXException, PropertyListFormatException, IOException {
    return parse(new ByteArrayInputStream(paramArrayOfbyte));
  }
  
  private static NSObject parseDocument(Document paramDocument) throws PropertyListFormatException, IOException, ParseException {
    Node node;
    DocumentType documentType = paramDocument.getDoctype();
    if (documentType == null) {
      if (!paramDocument.getDocumentElement().getNodeName().equals("plist"))
        throw new UnsupportedOperationException("The given XML document is not a property list."); 
    } else if (!documentType.getName().equals("plist")) {
      throw new UnsupportedOperationException("The given XML document is not a property list.");
    } 
    if (paramDocument.getDocumentElement().getNodeName().equals("plist")) {
      List<Node> list = filterElementNodes(paramDocument.getDocumentElement().getChildNodes());
      if (!list.isEmpty()) {
        if (list.size() == 1) {
          node = list.get(0);
        } else {
          throw new PropertyListFormatException("The given XML property list has more than one root element!");
        } 
      } else {
        throw new PropertyListFormatException("The given XML property list has no root element!");
      } 
    } else {
      node = node.getDocumentElement();
    } 
    return parseObject(node);
  }
  
  private static NSObject parseObject(Node paramNode) throws ParseException, IOException {
    NSArray nSArray;
    NSDictionary nSDictionary;
    List<Node> list;
    String str = paramNode.getNodeName();
    boolean bool = str.equals("dict");
    boolean bool1 = false;
    byte b = 0;
    if (bool) {
      nSDictionary = new NSDictionary();
      List<Node> list1 = filterElementNodes(paramNode.getChildNodes());
      while (b < list1.size()) {
        paramNode = list1.get(b);
        Node node = list1.get(b + 1);
        nSDictionary.put(getNodeTextContents(paramNode), parseObject(node));
        b += 2;
      } 
      return nSDictionary;
    } 
    if (nSDictionary.equals("array")) {
      list = filterElementNodes(paramNode.getChildNodes());
      nSArray = new NSArray(list.size());
      for (b = bool1; b < list.size(); b++)
        nSArray.setValue(b, parseObject(list.get(b))); 
      return nSArray;
    } 
    return (NSObject)(list.equals("true") ? new NSNumber(true) : (list.equals("false") ? new NSNumber(false) : (list.equals("integer") ? new NSNumber(getNodeTextContents((Node)nSArray)) : (list.equals("real") ? new NSNumber(getNodeTextContents((Node)nSArray)) : (list.equals("string") ? new NSString(getNodeTextContents((Node)nSArray)) : (list.equals("data") ? new NSData(getNodeTextContents((Node)nSArray)) : (list.equals("date") ? new NSDate(getNodeTextContents((Node)nSArray)) : null)))))));
  }
}


/* Location:              /home/platinum/Documents/AndroidRE/com.guanxu.technology.snaptain_era_s5c_29_apps.evozi.com-dex2jar.jar!/com/dd/plist/XMLPropertyListParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */