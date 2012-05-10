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
package org.sonar.plugins.erlang.dialyzer;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.sonar.api.rules.RulePriority;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class RuleHandler extends DefaultHandler {
	List<DialyzerRule> rules = new ArrayList<DialyzerRule>();
	private String tmpValue = "";
	private Object tmpRule;

	@Override
	public void startElement(String s, String s1, String elementName, Attributes attributes) throws SAXException {
		if ("rule".equals(elementName)) {
			rules.add(new DialyzerRule());
			tmpRule = rules.get(rules.size() - 1);
		}
		tmpValue = "";
	}

	@Override
	public void endElement(String s, String s1, String element) throws SAXException {
		if (StringUtils.equalsIgnoreCase("name", element)) {
			((DialyzerRule) tmpRule).getRule().setName(StringUtils.trim(tmpValue));
		} else if (StringUtils.equalsIgnoreCase("description", element)) {
			((DialyzerRule) tmpRule).getRule().setDescription(StringUtils.trim(tmpValue));
		} else if (StringUtils.equalsIgnoreCase("key", element)) {
			((DialyzerRule) tmpRule).getRule().setKey(StringUtils.trim(tmpValue));
		} else if (StringUtils.equalsIgnoreCase("configKey", element)) {
			((DialyzerRule) tmpRule).getRule().setConfigKey((StringUtils.trim(tmpValue)));
		} else if (StringUtils.equalsIgnoreCase("priority", element)) {
			((DialyzerRule) tmpRule).getRule().setSeverity(RulePriority.valueOf(StringUtils.trim(tmpValue)));
		} else if (StringUtils.equalsIgnoreCase("message", element)) {
			((DialyzerRule) tmpRule).addMessage(tmpValue.replaceAll("~.", ".*?").replaceAll("([\\{\\}\\[\\]])", "\\\\$1"));
		}
	}

	@Override
	public void characters(char[] ac, int i, int j) throws SAXException {
		tmpValue = tmpValue + new String(ac, i, j);
	}

}
