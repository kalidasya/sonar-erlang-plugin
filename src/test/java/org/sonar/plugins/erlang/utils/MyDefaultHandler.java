/*
 * Sonar Erlang Plugin
 * Copyright (C) 2012 Tamás Kende
 * kende.tamas@gmail.com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
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
