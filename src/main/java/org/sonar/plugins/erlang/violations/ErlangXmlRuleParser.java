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

package org.sonar.plugins.erlang.violations;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.dom4j.DocumentException;
import org.sonar.api.ServerComponent;
import org.xml.sax.SAXException;

public final class ErlangXmlRuleParser implements ServerComponent {

	/**
	 * Warning : the input stream is closed in this method
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 * 
	 * @throws DocumentException
	 */
	public List<ErlangRule> parse(InputStream input) {
		List<ErlangRule> rules = new ArrayList<ErlangRule>();
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser;
		RuleHandler a = new RuleHandler();
		try {
			saxParser = factory.newSAXParser();
			saxParser.parse(input, a);
		} catch (ParserConfigurationException e) {
		} catch (SAXException e) {
		} catch (IOException e) {
		}
		
		rules = a.getRules();
		return rules;

	}
}
