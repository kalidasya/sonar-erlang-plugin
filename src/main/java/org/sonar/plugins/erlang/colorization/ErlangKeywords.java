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
import java.util.HashSet;
import java.util.Set;

/**
 * This is a helper class for collecting every Scala keyword.
 * 
 * @author Felix Müller
 * @since 0.1
 */
public final class ErlangKeywords {

	private static final Set<String> KEYWORDS = new HashSet<String>(Arrays.asList("after", "and", "andalso", "band",
			"begin", "bnot", "bor", "bsl", "bsr", "bxor", "case", "catch", "cond", "div", "end", "fun", "if", "let",
			"not", "of", "or", "orelse", "query", "receive", "rem", "try", "when", "xor"));

	private ErlangKeywords() {
		// to prevent instantiation
	}

	public static Set<String> getAllKeywords() {
		return Collections.unmodifiableSet(KEYWORDS);
	}
}