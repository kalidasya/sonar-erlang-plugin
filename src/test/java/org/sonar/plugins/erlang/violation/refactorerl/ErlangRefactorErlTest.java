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
package org.sonar.plugins.erlang.violation.refactorerl;

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.io.FileUtils;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.resources.InputFile;
import org.sonar.api.resources.InputFileUtils;
import org.sonar.api.resources.Project;
import org.sonar.api.rules.ActiveRule;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.RuleParam;
import org.sonar.api.rules.RulePriority;
import org.sonar.plugins.erlang.utils.ProjectUtil;
import org.sonar.plugins.erlang.violations.ErlangRuleManager;
import org.sonar.plugins.erlang.violations.ErlangViolationResults;
import org.sonar.plugins.erlang.violations.refactorerl.ErlangRefactorErl;
import org.sonar.plugins.erlang.violations.refactorerl.RefactorErlRuleRepository;

public class ErlangRefactorErlTest {

	private ErlangRefactorErl er;
	private Configuration configuration;
	private ErlangViolationResults result;

	@Before
	public void setup() throws URISyntaxException, IOException {
		er = new ErlangRefactorErl();
		configuration = mock(Configuration.class);
		RulesProfile rp = mock(RulesProfile.class);
		List<ActiveRule> rlz = new ArrayList<ActiveRule>();
		RuleParam param = new RuleParam();
		param.setKey("maximum");
		param.setDefaultValue("10");
		List<RuleParam> params = new ArrayList<RuleParam>();
		params.add(param);
		Rule rule = new Rule();
		rule.setParams(params);
		rule.setName("mcCabe");
		rule.setKey("R001");
		ActiveRule activeRule = new ActiveRule();
		activeRule.setPriority(RulePriority.MAJOR);
		activeRule.setRule(rule);
		activeRule.setParameter("maximum", "10");
		rlz.add(activeRule);
		when(rp.getActiveRulesByRepository("Erlang")).thenReturn(rlz);
	    
		File fileToAnalyse =  new File(getClass().getResource("/org/sonar/plugins/erlang/erlcount/src/erlcount_counter.erl")
				.toURI());
		InputFile inputFile = InputFileUtils.create(fileToAnalyse.getParentFile(), fileToAnalyse);
		ArrayList<InputFile> inputFiles = new ArrayList<InputFile>();
		inputFiles.add(inputFile);
		Project project = ProjectUtil.getProject(inputFiles, configuration);
		StringReader reader = new StringReader(FileUtils.readFileToString(inputFile.getFile(), project.getFileSystem()
				.getSourceCharset().name()));
		 result = er.refactorErl(project, inputFile.getFile().getPath(), reader, new ErlangRuleManager(RefactorErlRuleRepository.RULES_FILE),rp);
	}

	@Test
	public void checkRefactorErl() {
		assertThat(result.getIssues().size(), Matchers.equalTo(2));
		assertThat(result.getIssues().get(0).ruleId, Matchers.equalTo("R001"));
		
	}
}
