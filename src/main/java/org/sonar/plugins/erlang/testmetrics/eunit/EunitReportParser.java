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

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

public final class EunitReportParser {
	
	private EunitReportParser(){}

	public static Report parse(File unitTestFile) {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser;
		TestReportHandler a = new TestReportHandler();
		try {
			saxParser = factory.newSAXParser();
			saxParser.parse(unitTestFile, a);
		} catch (ParserConfigurationException e) {
		} catch (SAXException e) {
		} catch (IOException e) {
		}
		Report report = new Report();
		report.setErrors(a.getTestSuite().getErrors().doubleValue());
		report.setTests(a.getTestSuite().getTests().doubleValue());
		report.setDurationMilliseconds(a.getTestSuite().getTime().doubleValue());
		report.setFailures(a.getTestSuite().getFailures().doubleValue());
		report.setSkipped(a.getTestSuite().getSkipped().doubleValue());
		report.setTestSuite(a.getTestSuite());
		return report;
	}

}
