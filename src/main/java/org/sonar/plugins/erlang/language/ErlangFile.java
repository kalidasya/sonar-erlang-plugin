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
package org.sonar.plugins.erlang.language;

import org.apache.commons.lang.StringUtils;
import org.sonar.api.resources.InputFile;
import org.sonar.api.resources.Language;
import org.sonar.api.resources.Qualifiers;
import org.sonar.api.resources.Resource;
import org.sonar.api.resources.Scopes;
import org.sonar.api.utils.WildcardPattern;

/**
 * This class implements a Scala source file for Sonar.
 *
 * @author Felix Müller
 * @since 0.1
 */
public class ErlangFile extends Resource<ErlangPackage> {

  private final boolean isUnitTest;
  private final String filename;
  private final String longName;
  private final ErlangPackage parent;

  public ErlangFile(String packageKey, String className, boolean isUnitTest) {
    super();
    this.isUnitTest = isUnitTest;
    filename = className.trim();

    String key;
    if (StringUtils.isBlank(packageKey)) {
      packageKey = ErlangPackage.DEFAULT_PACKAGE_NAME;
      key = new StringBuilder().append(packageKey).append(".").append(this.filename).toString();
      longName = filename;
    } else {
      packageKey = packageKey.trim();
      key = new StringBuilder().append(packageKey).append(".").append(this.filename).toString();
      longName = key;
    }
    parent = new ErlangPackage(packageKey);
    setKey(key);
  }

  @Override
  public String getName() {
    return filename;
  }

  @Override
  public String getLongName() {
    return longName;
  }

  @Override
  public String getDescription() {
    return null;
  }

  @Override
  public Language getLanguage() {
    return Erlang.INSTANCE;
  }

  @Override
  public String getScope() {
    return Scopes.FILE;
  }

  @Override
  public String getQualifier() {
    return isUnitTest ? Qualifiers.UNIT_TEST_FILE : Qualifiers.FILE;
  }

  @Override
  public ErlangPackage getParent() {
    return parent;
  }

  @Override
  public boolean matchFilePattern(String antPattern) {
    final String patternWithoutFileSuffix = StringUtils.substringBeforeLast(antPattern, ".");
    final WildcardPattern matcher = WildcardPattern.create(patternWithoutFileSuffix, ".");
    return matcher.match(getKey());
  }

  public boolean isUnitTest() {
    return isUnitTest;
  }

  /**
   * Shortcut for {@link #fromInputFile(InputFile, boolean)} for source files.
   */
  public static ErlangFile fromInputFile(InputFile inputFile) {
    return ErlangFile.fromInputFile(inputFile, false);
  }

  /**
   * Creates a {@link RebarFile} from a file in the source directories.
   *
   * @param inputFile the file object with relative path
   * @param isUnitTest whether it is a unit test file or a source file
   * @return the {@link RebarFile} created if exists, null otherwise
   */
  public static ErlangFile fromInputFile(InputFile inputFile, boolean isUnitTest) {
    if (inputFile == null || inputFile.getFile() == null || inputFile.getRelativePath() == null) {
      return null;
    }

    final String packageName = "package";
    final String className = resolveClassName(inputFile);
    return new ErlangFile(packageName, className, isUnitTest);
  }

  private static String resolveClassName(InputFile inputFile) {
    String classname = inputFile.getRelativePath();
    if (inputFile.getRelativePath().indexOf('/') >= 0) {
      classname = StringUtils.substringAfterLast(inputFile.getRelativePath(), "/");
    }
    return StringUtils.substringBeforeLast(classname, ".");
  }
}