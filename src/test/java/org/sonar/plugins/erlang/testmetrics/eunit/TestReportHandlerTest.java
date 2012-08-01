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

import java.io.IOException;
import java.io.StringReader;
import java.net.URISyntaxException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.sonar.plugins.erlang.utils.MyDefaultHandler;
import org.sonar.plugins.erlang.violation.RuleHandlerTest;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public class TestReportHandlerTest {

	private TestReportHandler testReportHandler;

	@Before
	public void setup() {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser;
		testReportHandler = new TestReportHandler();
		try {
			saxParser = factory.newSAXParser();
			saxParser.parse(RuleHandlerTest.class
					.getResourceAsStream("/org/sonar/plugins/erlang/erlcount/.eunit/TEST-erlcount_eunit.xml"),
					testReportHandler);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void checkRuleHandler() {
		assertThat(testReportHandler.getTestSuite().getErrors(), Matchers.equalTo(0));
		assertThat(testReportHandler.getTestSuite().getFailures(), Matchers.equalTo(1));
		assertThat(testReportHandler.getTestSuite().getTests(), Matchers.equalTo(7));
		assertThat(testReportHandler.getTestSuite().getName(), Matchers.equalTo("module 'erlcount_eunit'"));
		assertThat(testReportHandler.getTestSuite().getTime(), Matchers.equalTo(0.057D));
		assertThat(testReportHandler.getTestSuite().getTestCases().size(), Matchers.equalTo(7));
		assertThat(testReportHandler.getTestSuite().getTestCases().get(0).getName(),
				Matchers.equalTo("erlcount_eunit:find_erl_test_/0_20"));
		assertThat(testReportHandler.getTestSuite().getTestCases().get(0).getFailure().getReason(),
				Matchers.containsString("-D help|tree|search|stat|rates|opt|exec]"));
		assertThat(testReportHandler.getTestSuite().getTestCases().get(0).getFailure().getType(),
				Matchers.equalTo("assertEqual_failed"));
	}

	@Test
	public void checkXml() throws SAXException, IOException, URISyntaxException, ParserConfigurationException {
	/*	StringWriter writer = new StringWriter();
		IOUtils.copy(RuleHandlerTest.class
				.getResourceAsStream("/org/sonar/plugins/erlang/erlcount/.eunit/TEST-erlcount_eunit.xml"), writer, "UTF-8");

		XMLAssert.assertXMLEqual(writer.toString(), testReportHandler.testSuite.toXml());
		*/
		
		SAXParserFactory spf = SAXParserFactory.newInstance();
		spf.setValidating(true);
		spf.setNamespaceAware(true);
		spf.setFeature("http://xml.org/sax/features/validation", true);
		spf.setFeature("http://apache.org/xml/features/validation/schema", true);
      spf.setFeature("http://apache.org/xml/features/validation/schema-full-checking", true);
		SAXParser parser = spf.newSAXParser();
		parser.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");
		parser.setProperty("http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation", RuleHandlerTest.class
				.getResource("/org/sonar/plugins/erlang/junit.xsd").toURI().getPath());
		
		XMLReader reader = parser.getXMLReader();
		MyDefaultHandler eHandler = new MyDefaultHandler();
		reader.setErrorHandler(eHandler);
		reader.parse(new InputSource(new StringReader(testReportHandler.getTestSuite().toXml())));
		assertThat("Invalid Xml "+eHandler.errMessage, eHandler.valid, Matchers.is(true));
	}
}
