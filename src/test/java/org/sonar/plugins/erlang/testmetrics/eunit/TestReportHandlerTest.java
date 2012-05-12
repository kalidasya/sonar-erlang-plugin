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
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.sonar.plugins.erlang.dialyzer.DialyzerRuleManager;
import org.sonar.plugins.erlang.dialyzer.RuleHandlerTest;
import org.xml.sax.SAXException;

public class TestReportHandlerTest {

	private TestReportHandler testReportHandler;

	@Before
	public void setup(){
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser;
		testReportHandler = new TestReportHandler();
		try {
			saxParser = factory.newSAXParser();
			saxParser.parse(RuleHandlerTest.class.getResourceAsStream("/org/sonar/plugins/erlang/erlcount/.eunit/TEST-erlcount_eunit.xml"), testReportHandler);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void checkRuleHandler(){
		assertThat(testReportHandler.testSuite.errors, Matchers.equalTo(0));
		assertThat(testReportHandler.testSuite.failures, Matchers.equalTo(1));
		assertThat(testReportHandler.testSuite.tests, Matchers.equalTo(7));
		assertThat(testReportHandler.testSuite.name, Matchers.equalTo("module 'erlcount_eunit'"));
		assertThat(testReportHandler.testSuite.time, Matchers.equalTo(0.043D));
		assertThat(testReportHandler.testSuite.testCases.size(), Matchers.equalTo(7));
		assertThat(testReportHandler.testSuite.testCases.get(0).getName(), Matchers.equalTo("erlcount_eunit:find_erl_test_/0_20"));
		assertThat(testReportHandler.testSuite.testCases.get(0).getFailure().getReason(), Matchers.containsString("-D help|tree|search|stat|rates|opt|exec]"));
		assertThat(testReportHandler.testSuite.testCases.get(0).getFailure().getType(), Matchers.equalTo("assertEqual_failed"));
		
		
	}
	
}
