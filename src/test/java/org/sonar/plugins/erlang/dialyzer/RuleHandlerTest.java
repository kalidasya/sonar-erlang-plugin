package org.sonar.plugins.erlang.dialyzer;

import static org.junit.Assert.assertThat;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

public class RuleHandlerTest {

	private RuleHandler ruleHandler;

	@Before
	public void setup(){
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser;
		ruleHandler = new RuleHandler();
		try {
			saxParser = factory.newSAXParser();
			saxParser.parse(RuleHandlerTest.class.getResourceAsStream(DialyzerRuleManager.RULES_FILE_LOCATION), ruleHandler);
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
		assertThat(ruleHandler.rules.size(), Matchers.equalTo(42));
	}
	
}
