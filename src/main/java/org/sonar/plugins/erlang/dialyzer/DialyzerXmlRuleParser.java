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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLStreamException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.CharEncoding;
import org.apache.commons.lang.StringUtils;
import org.codehaus.staxmate.in.SMInputCursor;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.sonar.api.ServerComponent;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.RuleParam;
import org.sonar.api.rules.RulePriority;
import org.sonar.api.utils.SonarException;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public final class DialyzerXmlRuleParser implements ServerComponent {

	public List<DialyzerRule> parse(File file) throws DocumentException {
		Reader reader = null;
		try {
			reader = new InputStreamReader(FileUtils.openInputStream(file), CharEncoding.UTF_8);
			return null;//parse(reader);

		} catch (IOException e) {
			throw new SonarException("Fail to load the file: " + file, e);

		} finally {
			IOUtils.closeQuietly(reader);
		}
	}

	/**
	 * Warning : the input stream is closed in this method
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 * 
	 * @throws DocumentException
	 */
	public List<DialyzerRule> parse(InputStream input) {
	/*	Reader reader = null;
		try {
			reader = new InputStreamReader(input, CharEncoding.UTF_8);
			return parse(reader);

		} catch (IOException e) {
			throw new SonarException("Fail to load the xml stream", e);

		} catch (DocumentException e) {
			throw new SonarException("Fail to load the xml stream", e);
		} finally {
			IOUtils.closeQuietly(reader);
		}
	}

	public List<DialyzerRule> parse(Reader reader) throws DocumentException {*/
		List<DialyzerRule> rules = new ArrayList<DialyzerRule>();
		SAXReader saxReader = new SAXReader();
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser;
		RuleHandler a = new RuleHandler();
		try {
			saxParser = factory.newSAXParser();
			saxParser.parse(input, a);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		rules = a.rules;
		
		/*Document document = saxReader.read(reader);
		Element root = document.getRootElement();*/
		/*for (Object node : root.elements()) {
			DialyzerRule rule = new DialyzerRule();
			rules.add(rule);
			processRule(rule, node);

		}*//*
		 * XMLInputFactory xmlFactory = XMLInputFactory.newInstance();
		 * xmlFactory.setProperty(XMLInputFactory.IS_COALESCING, Boolean.TRUE);
		 * xmlFactory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE,
		 * Boolean.FALSE); // just so it won't try to load DTD in if there's
		 * DOCTYPE xmlFactory.setProperty(XMLInputFactory.SUPPORT_DTD,
		 * Boolean.FALSE); xmlFactory.setProperty(XMLInputFactory.IS_VALIDATING,
		 * Boolean.FALSE); SMInputFactory inputFactory = new
		 * SMInputFactory(xmlFactory); try { SMHierarchicCursor rootC =
		 * inputFactory.rootElementCursor(reader); rootC.advance(); // <rules>
		 * List<DialyzerRule> rules = new ArrayList<DialyzerRule>();
		 * 
		 * SMInputCursor rulesC = rootC.childElementCursor("rule"); while
		 * (rulesC.getNext() != null) { // <rule> DialyzerRule rule = new
		 * DialyzerRule(); rules.add(rule);
		 * 
		 * processRule(rule, rulesC); }
		 */
		return rules;

	}

	private static void processRule(DialyzerRule rule, Object node) {
		Node actRule = (Node) node;
		for (Object val : actRule.selectNodes("./*")) {
			Node value = (Node) val;
			String nodeName = value.getName();
			String text = value.getStringValue();
			if (StringUtils.equalsIgnoreCase("name", nodeName)) {
				rule.getRule().setName(StringUtils.trim(text));
			} else if (StringUtils.equalsIgnoreCase("description", nodeName)) {
				rule.getRule().setDescription(StringUtils.trim(text));
			} else if (StringUtils.equalsIgnoreCase("key", nodeName)) {
				rule.getRule().setKey(StringUtils.trim(text));
			} else if (StringUtils.equalsIgnoreCase("priority", nodeName)) {
				rule.getRule().setSeverity(RulePriority.valueOf(StringUtils.trim(text)));
			} else if (StringUtils.equalsIgnoreCase("messages", nodeName)) {
				List<String> messages = new ArrayList<String>();
				for (Object message : value.selectNodes("./*")) {
					messages.add(StringUtils.trim(((Node) message).getStringValue()));
				}
				rule.setMessages(messages);
			}
		}

	}

	/*
	 * private static void processRule(DialyzerRule rule, SMInputCursor ruleC)
	 * throws XMLStreamException {
	 * 
	 * SMInputCursor cursor = ruleC.childElementCursor(); while
	 * (cursor.getNext() != null) { String nodeName = cursor.getLocalName();
	 * String text = cursor.collectDescendantText(false); if
	 * (StringUtils.equalsIgnoreCase("name", nodeName)) {
	 * rule.getRule().setName(StringUtils.trim(text)); } else if
	 * (StringUtils.equalsIgnoreCase("description", nodeName)) {
	 * rule.getRule().setDescription(StringUtils.trim(text)); } else if
	 * (StringUtils.equalsIgnoreCase("key", nodeName)) {
	 * rule.getRule().setKey(StringUtils.trim(text)); } else if
	 * (StringUtils.equalsIgnoreCase("priority", nodeName)) {
	 * rule.getRule().setPriority(RulePriority.valueOf(StringUtils.trim(text)));
	 * } else if (StringUtils.equalsIgnoreCase("messages", nodeName)) {
	 * SMInputCursor messagesCursor = cursor.childElementCursor("message");
	 * List<String> messages = new ArrayList<String>();
	 * while(messagesCursor.getNext()!=null){
	 * messages.add(StringUtils.trim(cursor.collectDescendantText(false))); }
	 * rule.setMessages(messages); } } if
	 * (StringUtils.isEmpty(rule.getRule().getKey())) { throw new
	 * SonarException("Node <key> is missing in <rule>"); } if
	 * (StringUtils.isEmpty(rule.getRule().getName())) { throw new
	 * SonarException("Node <name> is missing in <rule>"); } }
	 */

	private static void processParameter(Rule rule, SMInputCursor ruleC) throws XMLStreamException {
		RuleParam param = rule.createParameter();

		SMInputCursor paramC = ruleC.childElementCursor();
		while (paramC.getNext() != null) {
			String propNodeName = paramC.getLocalName();
			String propText = StringUtils.trim(paramC.collectDescendantText(false));
			if (StringUtils.equalsIgnoreCase("key", propNodeName)) {
				param.setKey(propText);

			} else if (StringUtils.equalsIgnoreCase("description", propNodeName)) {
				param.setDescription(propText);

			} else if (StringUtils.equalsIgnoreCase("type", propNodeName)) {
				param.setType(propText);

			} else if (StringUtils.equalsIgnoreCase("defaultValue", propNodeName)) {
				param.setDefaultValue(propText);
			}
		}
		if (StringUtils.isEmpty(param.getKey())) {
			throw new SonarException("Node <key> is missing in <param>");
		}
	}
}
