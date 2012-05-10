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

import org.sonar.api.BatchExtension;
import org.sonar.api.ServerExtension;
import org.sonar.api.rules.Rule;

public class DialyzerRuleManager implements ServerExtension, BatchExtension {

	private List<DialyzerRule> rules = new ArrayList<DialyzerRule>();

	public static final String OTHER_RULES_KEY = "OTHER_RULES";
	public static final String UNUSED_NAMES_KEY = "UNUSED_NAMES";
	public static final String CYCLOMATIC_COMPLEXITY_KEY = "CYCLOMATIC_COMPLEXITY";
	private static final String RULES_FILE_LOCATION = "/org/sonar/plugins/erlang/dialyzer/rules.xml"; 

	public DialyzerRuleManager() {
		this(RULES_FILE_LOCATION);
	}

	public DialyzerRuleManager(String rulesPath) {
		rules = new DialyzerXmlRuleParser().parse(DialyzerRuleManager.class.getResourceAsStream(rulesPath));
	}

	public Rule getRuleByKey(String key){
		for (DialyzerRule rule : rules) {
			if (rule.getRule().getKey().equals(key)) {
				return rule.getRule();
			}
		}
		return null;
	}

	public String getRuleIdByMessage(String message) {
		for (DialyzerRule rule : rules) {
			if (rule.hasMessage(message)) {
				return rule.getRule().getKey();
			}
		}
		return OTHER_RULES_KEY;
	}

	public List<DialyzerRule> getDialyzerRules() {
		return rules;
	}

}
