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

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.BatchExtension;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.RuleRepository;
import org.sonar.api.rules.XMLRuleParser;
import org.sonar.plugins.erlang.language.Erlang;

public class DialyzerRuleRepository extends RuleRepository implements BatchExtension {
	private static final Logger LOG = LoggerFactory.getLogger(DialyzerSensor.class);
	private XMLRuleParser parser;
	public static final String REPOSITORY_NAME = "Erlang";
	public static final String REPOSITORY_KEY = "Erlang";
	public static final String RULES_FILE = "/org/sonar/plugins/erlang/dialyzer/rules.xml";

	public DialyzerRuleRepository() {
		super(REPOSITORY_KEY, Erlang.LANG_KEY);
		setName(REPOSITORY_NAME);
		this.parser = new XMLRuleParser();
	}

	public DialyzerRuleRepository(XMLRuleParser parser) {
		super(REPOSITORY_KEY, Erlang.LANG_KEY);
		setName(REPOSITORY_NAME);
		
		this.parser = parser;
	}

	/*public DialyzerRuleRepository(Erlang erlang, DialyzerRuleManager dialyzerRuleManager) {
		super(REPOSITORY_KEY, erlang.getKey());
		setName(REPOSITORY_NAME);
		LOG.warn("Constructor called, fields: erlang="+erlang+" dialyzerRuleManager="+dialyzerRuleManager);
		this.dialyzerRuleManager = dialyzerRuleManager;
	}*/

	@Override
	  public List<Rule> createRules() {
	    List<Rule> rules = parser.parse(getClass().getResourceAsStream(RULES_FILE));
	    for (Rule r : rules) {
	      r.setRepositoryKey(REPOSITORY_KEY);
	    }
	    return rules;
	  }
	
/*
	@Override
	public List<Rule> createRules() {

		List<Rule> rulesList = new ArrayList<Rule>();

		for (DialyzerRule dialyzerRule : dialyzerRuleManager.getDialyzerRules()) {
			Rule rule = Rule.create(REPOSITORY_KEY, dialyzerRule.getRule().getKey(), dialyzerRule.getRule().getName());

			rule.setDescription(dialyzerRule.getRule().getDescription());
			rule.setSeverity(dialyzerRule.getRule().getSeverity());

			for (RuleParam ruleParam : dialyzerRule.getRule().getParams()) {
				RuleParam param = rule.createParameter();
				param.setKey(ruleParam.getKey());
				param.setDefaultValue(ruleParam.getDefaultValue());
				param.setDescription(ruleParam.getDescription());
				param.setType(ruleParam.getType());
			}

			rulesList.add(rule);
		}

		return rulesList;
	}*/
}