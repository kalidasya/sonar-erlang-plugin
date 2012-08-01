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
import static org.mockito.Mockito.mock;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.rules.ActiveRule;
import org.sonar.plugins.erlang.utils.RuleUtil;
import org.sonar.plugins.erlang.utils.StringUtils;

public class ErlangSourceByLineAnalyzerTest {

	private ErlangSourceByLineAnalyzer la;
	private ActiveRule activeRule;
	private ActiveRule activeRule2;
	private ActiveRule activeRule3;

	@Before
	public void setup() throws IOException, URISyntaxException {
		File fileToAnalyse = new File(getClass().getResource(
				"/org/sonar/plugins/erlang/erlcount/src/erlcount_sup.erl").toURI());
		RulesProfile rp = mock(RulesProfile.class);
		Collection<ActiveRule> rlz = new ArrayList<ActiveRule>();

		activeRule = RuleUtil.generateActiveRule("regexSingleLine", "B001",
				new HashMap<String, String>() {
					{
						put("regex", ".*% *TODO.*");
						put("ignoreCase", "false");
					}
				});
		activeRule2 = RuleUtil.generateActiveRule("regexSingleLine", "B001_1",
				new HashMap<String, String>() {
					{
						put("regex", ".*% *todo.*");
						put("ignoreCase", "true");
					}
				});
		activeRule3 = RuleUtil.generateActiveRule("regexSingleLine", "B001_2",
				new HashMap<String, String>() {
					{
						put("regex", "% *TODO");
						put("ignoreCase", "false");

					}
				});
		rlz.add(activeRule);
		rlz.add(activeRule2);
		rlz.add(activeRule3);

		String source = FileUtils.readFileToString(fileToAnalyse, "UTF-8");
		List<String> lines = StringUtils.convertStringToListOfLines(source);

		la = new ErlangSourceByLineAnalyzer(lines, rlz);
	}

	@Test
	public void checkLinesAnalyzer() {
		assertThat(la.getNumberOfFunctions(), Matchers.equalTo(4D));
		assertThat(la.getNumberOfComments(), Matchers.equalTo(4));
		assertThat(la.countLines(), Matchers.equalTo(32));
		assertThat(la.getLinesOfCode(), Matchers.equalTo(21));
		assertThat(la.getViolationReport().getUnits().size(), Matchers.equalTo(4));
	}
}
