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
package org.sonar.plugins.erlang.cpd;

import java.util.List;

import net.sourceforge.pmd.cpd.AnyTokenizer;
import net.sourceforge.pmd.cpd.Tokenizer;

import org.sonar.api.batch.AbstractCpdMapping;
import org.sonar.api.resources.InputFileUtils;
import org.sonar.api.resources.Language;
import org.sonar.api.resources.Resource;
import org.sonar.plugins.erlang.language.Erlang;
import org.sonar.plugins.erlang.language.ErlangFile;

/**
 * The erlang CPD mapping for sonar, now it use AnyTokenizer 
 *
 */
public class ErlangCpdMapping extends AbstractCpdMapping {

	private Erlang erlang;

	public ErlangCpdMapping(Erlang erlang) {
		this.erlang = erlang;
	}

	public Tokenizer getTokenizer() {
		return new AnyTokenizer();
	}

	public Language getLanguage() {
		return erlang;
	}

	/**
	 * TODO Need to implement this, because the SonarBridgeEngine file will call a
	 * mapping.createResource and it returns with wrong keys and package names
	 * Now it is using the test util class which should be changed
	 * 
	 * @param file
	 * @param sourceDirs
	 * @return
	 */
	@Override
	public Resource createResource(java.io.File file, List<java.io.File> sourceDirs) {
		return ErlangFile.fromInputFile(InputFileUtils.create(file, file.getName()), file.getName().contains("eunit"));
	}

}
