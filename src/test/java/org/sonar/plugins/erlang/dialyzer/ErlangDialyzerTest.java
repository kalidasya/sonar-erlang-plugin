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
package org.sonar.plugins.erlang.dialyzer;

import static org.mockito.Mockito.mock;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.URISyntaxException;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.io.FileUtils;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.sonar.api.resources.InputFile;
import org.sonar.api.resources.InputFileUtils;
import org.sonar.api.resources.Project;
import org.sonar.plugins.erlang.utils.ProjectUtil;
import static org.junit.Assert.assertThat;

public class ErlangDialyzerTest {

	private ErlangDialyzer ed;
	private Configuration configuration;
	private ErlangDialyzerResult result;

	@Before
	public void setup() throws URISyntaxException, IOException {
		ed = new ErlangDialyzer();
		configuration = mock(Configuration.class);
		 
		File fileToAnalyse =  new File(getClass().getResource("/org/sonar/plugins/erlang/erlcount/src/erlcount_counter.erl")
				.toURI());

		InputFile inputFile = InputFileUtils.create(fileToAnalyse.getParentFile(), fileToAnalyse);

		Project project = ProjectUtil.getProject(inputFile, configuration);
		
		StringReader reader = new StringReader(FileUtils.readFileToString(inputFile.getFile(), project.getFileSystem()
				.getSourceCharset().name()));
		 result = ed.dialyzer(project, inputFile.getFile().getPath(), reader, new DialyzerRuleManager());
	}

	@Test
	public void checkDialyzer() {
		assertThat(result.getFunctions().size(), Matchers.equalTo(7));
		assertThat(result.getIssues().size(), Matchers.equalTo(1));
		assertThat(result.getIssues().get(0).ruleId, Matchers.equalTo("D042"));
		assertThat(result.getIssues().get(0).line, Matchers.equalTo(2));
	}
}
