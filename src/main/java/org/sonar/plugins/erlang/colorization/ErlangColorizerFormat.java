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
package org.sonar.plugins.erlang.colorization;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.sonar.api.web.CodeColorizerFormat;
import org.sonar.colorizer.KeywordsTokenizer;
import org.sonar.colorizer.LiteralTokenizer;
import org.sonar.colorizer.Tokenizer;
import org.sonar.plugins.erlang.language.Erlang;

/**
 * This class extends Sonar for code colorization of Erlang source.
 *
 * @since 0.1
 */
public class ErlangColorizerFormat extends CodeColorizerFormat {

  private static final String END_SPAN_TAG = "</span>";

  private static final List<Tokenizer> TOKENIZERS = Arrays.asList(
      new LiteralTokenizer("<span class=\"s\">", END_SPAN_TAG),
      new KeywordsTokenizer("<span class=\"k\">", END_SPAN_TAG, ErlangKeywords.getAllKeywords()),
      new ErlangdocTokenizer("<span class=\"j\">", END_SPAN_TAG));

  public ErlangColorizerFormat() {
    super(Erlang.INSTANCE.getKey());
  }

  @Override
  public List<Tokenizer> getTokenizers() {
    return Collections.unmodifiableList(TOKENIZERS);
  }
}