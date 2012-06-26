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

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.sonar.api.resources.AbstractLanguage;
import org.sonar.plugins.erlang.ErlangPlugin;

public class Erlang extends AbstractLanguage {
	public static final Erlang INSTANCE = new Erlang();
	private Configuration configuration;

	public Erlang(){
		super(ErlangPlugin.LANG_KEY, ErlangPlugin.NAME);
	}
	
	public Erlang(Configuration configuration) {
		super(ErlangPlugin.LANG_KEY, ErlangPlugin.NAME);
		this.configuration = configuration;
	}

	public String getDialyzerUri() {
		return ErlangPlugin.DIALYZER_DEFAULT_FILENAME;
	}
	
	public String getRefactorErlFilenamePattern() {
		return ErlangPlugin.REFACTORERL_DEFAULT_FILENAME_PATTERN;
	}


	public String getEunitFolder() {
		return ErlangPlugin.EUNIT_DEFAULT_FOLDER;
	}

	public void setConfiguration(Configuration configuration) {
	    this.configuration = configuration;
	  }
	
	public String[] getFileSuffixes() {
		String[] suffixes = configuration.getStringArray(ErlangPlugin.FILE_SUFFIXES_KEY);
		if (suffixes == null || suffixes.length == 0) {
			suffixes = StringUtils.split(ErlangPlugin.FILE_SUFFIXES_DEFVALUE, ",");
		}
		return suffixes;
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public String getRebarConfigUrl() {
		return ErlangPlugin.REBAR_CONFIG_URL;
	}

}
