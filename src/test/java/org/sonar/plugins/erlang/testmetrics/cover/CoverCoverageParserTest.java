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
package org.sonar.plugins.erlang.testmetrics.cover;

import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

public class CoverCoverageParserTest {

	private File fileToAnalyse;
	private CoverCoverageParser parser;

	@Before
	public void setup() throws IOException, URISyntaxException {
		fileToAnalyse = new File(getClass().getResource("/org/sonar/plugins/erlang/erlcount/.eunit/erlcount_lib.COVER.html")
				.toURI());
		parser = new CoverCoverageParser();
	}

	@Test
	public void checkLinesAnalyzer(){
		CoverFileCoverage result = parser.parseFile(fileToAnalyse, fileToAnalyse.getParent(), "erlcount_lib.COVER.html");
		assertThat(result.getCoveredLines(), Matchers.equalTo(19));
		assertThat(result.getUncoveredLines(), Matchers.equalTo(1));
		assertThat(result.getLinesToCover(), Matchers.equalTo(20));
	}
}
