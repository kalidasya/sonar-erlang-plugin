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

import java.util.ArrayList;
import java.util.List;

import org.sonar.api.rules.Rule;
import org.sonar.api.rules.RuleRepository;
import org.sonar.api.rules.XMLRuleParser;
import org.sonar.plugins.erlang.ErlangPlugin;

public class ErlangRuleRepository extends RuleRepository {
	private XMLRuleParser parser;
	public static final String REPOSITORY_NAME = "Erlang";
	public static final String REPOSITORY_KEY = "Erlang";
	public static final String DIALYZER_PATH = "/org/sonar/plugins/erlang/dialyzer/rules.xml";
	public static final String REFACTORERL_PATH = "/org/sonar/plugins/erlang/refactorerl/rules.xml";
	public static final String BASICRULES_PATH = "/org/sonar/plugins/erlang/basic/rules.xml";
	public static final List<String> RULES_FILE = new ArrayList<String>() {
		private static final long serialVersionUID = 8245183286506333769L;
		{
			add(DIALYZER_PATH);
			add(REFACTORERL_PATH);
			add(BASICRULES_PATH);
		}
	};

	public ErlangRuleRepository() {
		super(REPOSITORY_KEY, ErlangPlugin.LANG_KEY);
		setName(REPOSITORY_NAME);
		this.parser = new XMLRuleParser();
	}

	public ErlangRuleRepository(XMLRuleParser parser) {
		super(REPOSITORY_KEY, ErlangPlugin.LANG_KEY);
		setName(REPOSITORY_NAME);
		this.parser = parser;
	}

	@Override
	public List<Rule> createRules() {
		List<Rule> rules = new ArrayList<Rule>();
		for (String oneFile : RULES_FILE) {
			rules.addAll(parser.parse(getClass().getResourceAsStream(oneFile)));
		}
		for (Rule rule : rules) {
			rule.setRepositoryKey(REPOSITORY_KEY);
		}
		return rules;
	}
}

