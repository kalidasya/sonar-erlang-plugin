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
