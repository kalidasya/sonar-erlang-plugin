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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class StringUtils {

  private static final Logger LOGGER = LoggerFactory.getLogger(StringUtils.class);

  private StringUtils() {
    // to prevent instantiation
  }

  public static List<String> convertStringToListOfLines(String string) throws IOException {
    final List<String> lines = new ArrayList<String>();
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new StringReader(string));
      String line = null;
      while ((line = reader.readLine()) != null) {
        lines.add(line);
      }
    } catch (IOException ioe) {
      LOGGER.error("Error while reading the lines of a given string", ioe);
      throw ioe;
    } finally {
      IOUtils.closeQuietly(reader);
    }
    return lines;
  }
}