package org.sonar.plugins.erlang.testmetrics.eunit;

import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.sonar.plugins.erlang.dialyzer.RuleHandlerTest;
import org.sonar.plugins.erlang.testmetrics.cover.CoverCoverageParser;
import org.xml.sax.SAXException;

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
		assertThat(result.getDurationMilliseconds(), Matchers.equalTo(0.043D));
		assertThat(result.getTestSuite().getName(), Matchers.equalTo("module 'erlcount_eunit'"));
		assertThat(result.getTestSuite().getTests(), Matchers.equalTo(7));
		assertThat(result.getTestSuite().getTestCases().size(), Matchers.equalTo(7));
		assertThat(result.getTestSuite().getTestCases().get(0).getName(), Matchers.equalTo("erlcount_eunit:find_erl_test_/0_20"));
		assertThat(result.getTestSuite().getTestCases().get(0).getFailure().getReason(), Matchers.containsString("-D help|tree|search|stat|rates|opt|exec]"));
		assertThat(result.getTestSuite().getTestCases().get(0).getFailure().getType(), Matchers.equalTo("assertEqual_failed"));
		
		
	}
	
}
