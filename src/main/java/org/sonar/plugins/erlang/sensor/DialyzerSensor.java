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
package org.sonar.plugins.erlang.sensor;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.resources.InputFile;
import org.sonar.api.resources.Project;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.Violation;
import org.sonar.plugins.erlang.dialyzer.DialyzerRuleManager;
import org.sonar.plugins.erlang.dialyzer.DialyzerRuleRepository;
import org.sonar.plugins.erlang.dialyzer.ErlangDialyzer;
import org.sonar.plugins.erlang.dialyzer.ErlangDialyzerResult;
import org.sonar.plugins.erlang.dialyzer.Issue;
import org.sonar.plugins.erlang.language.Erlang;
import org.sonar.plugins.erlang.language.ErlangFile;

public class DialyzerSensor extends AbstractErlangSensor {

	private DialyzerRuleManager dialyzerRuleManager = new DialyzerRuleManager();

	public DialyzerSensor(Erlang erlang) {
		super(erlang);
	}


	private static final Logger LOG = LoggerFactory.getLogger(DialyzerSensor.class);
	private ErlangDialyzer dialyzer = new ErlangDialyzer();

	public void analyse(Project project, SensorContext context) {
		for (InputFile inputFile : project.getFileSystem().mainFiles(getErlang().getKey())) {
			try {
				analyzeFile(inputFile, project, context);
			} catch (Exception e) {
				LOG.error("Can not analyze the file " + inputFile.getFileBaseDir() + "\\" + inputFile.getRelativePath(), e);
			}

		}
	}

	private void analyzeFile(InputFile inputFile, Project project, SensorContext context) throws IOException {
		ErlangFile erlangFile = ErlangFile.fromInputFile(inputFile);
		System.out.println("Erlang file in DS:" + erlangFile.getLongName());
		Reader reader = null;
		try {

			reader = new StringReader(FileUtils.readFileToString(inputFile.getFile(), project.getFileSystem()
					.getSourceCharset().name()));

			LOG.debug("values:" + inputFile.getFile().getPath() + " " + project.getFileSystem().getSourceCharset().name()
					+ " " + reader + " " + project);

			ErlangDialyzerResult result = dialyzer.dialyzer(project, inputFile.getFile().getPath(), reader, dialyzerRuleManager);

			List<Issue> issues = result.getIssues();
			LOG.debug("Issue Size:" + result.getIssues().size() + " " + inputFile.getFile().getPath());
			for (Issue issue : issues) {
				/**
				 * TODO: add some rule checking here
				 */
				Rule rule =  Rule.create(DialyzerRuleRepository.REPOSITORY_NAME, issue.ruleId);
				Violation violation = Violation.create(rule, erlangFile);
				violation.setLineId(issue.line);
				violation.setMessage(issue.descr);
				context.saveViolation(violation);
			}

		} finally {
			IOUtils.closeQuietly(reader);
		}
	}

}
