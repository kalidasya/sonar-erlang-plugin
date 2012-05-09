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

import org.sonar.api.resources.AbstractLanguage;

public class Erlang extends AbstractLanguage {
	public static final Erlang INSTANCE = new Erlang();
	private static final String dialyzerOutputFileUri = ".eunit/dialyzer.log";
	public static final String LANG_KEY = "erl";
	
	public Erlang() {
	    super(LANG_KEY, "Erlang");
	  }
	
	public String[] getFileSuffixes() {
		 return new String[] { "erl" };
	}


	public String getDialyzerUri() {
		return dialyzerOutputFileUri;
	}
	
	
}
