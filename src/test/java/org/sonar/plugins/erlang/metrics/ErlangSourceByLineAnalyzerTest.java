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
package org.sonar.plugins.erlang.metrics;

import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.sonar.plugins.erlang.utils.StringUtils;

public class ErlangSourceByLineAnalyzerTest {
	
	
	private ErlangSourceByLineAnalyzer la;

	@Before
	public void setup() throws IOException, URISyntaxException{
		File fileToAnalyse =  new File(getClass().getResource("/org/sonar/plugins/erlang/erlcount/src/erlcount_sup.erl")
				.toURI());
		String source = FileUtils.readFileToString(fileToAnalyse, "UTF-8");
		List<String> lines = StringUtils.convertStringToListOfLines(source);
		la = new ErlangSourceByLineAnalyzer(lines);
	}
	
	@Test
	public void checkLinesAnalyzer(){
		assertThat(la.getNumberOfFunctions(), Matchers.equalTo(4D));
		assertThat(la.getNumberOfComments(), Matchers.equalTo(4));
		assertThat(la.countLines(), Matchers.equalTo(31));
		assertThat(la.getLinesOfCode(), Matchers.equalTo(21));
	}
}
