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
package org.sonar.plugins.erlang.testmetrics.eunit;

import static org.junit.Assert.assertThat;

import java.io.File;
import java.net.URISyntaxException;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

public class EunitReportParserTest {

	private Report result;

	@Before
	public void setup() throws URISyntaxException{
		File fileToAnalyse = new File(getClass().getResource("/org/sonar/plugins/erlang/erlcount/.eunit/TEST-erlcount_eunit.xml")
				.toURI());
		result = EunitReportParser.parse(fileToAnalyse);
	}

	@Test
	public void checkRuleHandler(){
		assertThat(result.getErrors(), Matchers.equalTo(0D));
		assertThat(result.getFailures(), Matchers.equalTo(1D));
		assertThat(result.getTests(), Matchers.equalTo(7D));
		assertThat(result.getDurationMilliseconds(), Matchers.equalTo(0.023D));
		assertThat(result.getTestSuite().getName(), Matchers.equalTo("module 'erlcount_eunit'"));
		assertThat(result.getTestSuite().getTests(), Matchers.equalTo(7));
		assertThat(result.getTestSuite().getTestCases().size(), Matchers.equalTo(7));
		assertThat(result.getTestSuite().getTestCases().get(0).getName(), Matchers.equalTo("erlcount_eunit:find_erl_test_/0_20"));
		assertThat(result.getTestSuite().getTestCases().get(0).getFailure().getReason(), Matchers.containsString("-D help|tree|search|stat|rates|opt|exec]"));
		assertThat(result.getTestSuite().getTestCases().get(0).getFailure().getType(), Matchers.equalTo("assertEqual_failed"));
	}
	
	
	
}
