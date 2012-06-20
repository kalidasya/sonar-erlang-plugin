package org.sonar.plugins.erlang.utils;

import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class MyDefaultHandler extends DefaultHandler {
   public String errMessage = "";
   public boolean valid = true;
   
	public void warning(SAXParseException e) {
      System.out.println("Warning Line " + e.getLineNumber() + ": " + e.getMessage() + "\n");
  }

  public void error(SAXParseException e) {
      errMessage = new String("Error Line " + e.getLineNumber() + ": " + e.getMessage() + "\n");
      System.out.println(errMessage);
      valid = false;
  }

  public void fatalError(SAXParseException e) {
      errMessage = new String("Error Line " + e.getLineNumber() + ": " + e.getMessage() + "\n");
      System.out.println(errMessage);
      valid = false;
  }
}
