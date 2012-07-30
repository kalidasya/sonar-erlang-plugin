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
package org.sonar.plugins.erlang.utils;

import java.util.ArrayList;
import java.util.List;

import org.sonar.api.rules.ActiveRule;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.RuleParam;
import org.sonar.api.rules.RulePriority;
import org.sonar.check.Cardinality;
import org.sonar.plugins.erlang.violations.ErlangRule;
import org.sonar.plugins.erlang.violations.ErlangRuleManager;
import org.sonar.plugins.erlang.violations.ErlangRuleRepository;

public class RuleUtil {

	public static ErlangRule getOneRule(String key, String repoKey) {
		ErlangRuleManager rm = new ErlangRuleManager(getFileUrlForRepo(repoKey));
		return rm.getErlangRuleByKey(key);
	}

	private static String getFileUrlForRepo(String repoKey) {
		if ("dialyzer".equalsIgnoreCase(repoKey)) {
			return ErlangRuleRepository.DIALYZER_PATH;
		} else if ("refactorerl".equalsIgnoreCase(repoKey)) {
			return ErlangRuleRepository.REFACTORERL_PATH;
		}

		return null;
	}

	public static ActiveRule generateActiveRule(String ruleName, String ruleKey, String paramName,
			String paramValue) {
		List<RuleParam> params = new ArrayList<RuleParam>();
		Rule rule = new Rule();
		if (paramName != null) {
			RuleParam param = new RuleParam();
			param.setKey(paramName);
			param.setDefaultValue(paramValue);
			params.add(param);
			rule.setParams(params);
		}
		rule.setName(ruleName);
		rule.setKey(ruleKey);
		rule.setConfigKey(ruleKey);
		rule.setPluginName("Erlang");
		rule.setEnabled(true);
		rule.setSeverity(RulePriority.MAJOR);
		rule.setCardinality(Cardinality.SINGLE);
		ActiveRule activeRule = new ActiveRule();
		activeRule.setPriority(RulePriority.MAJOR);
		activeRule.setRule(rule);
		activeRule.setParameter(paramName, paramValue);
		return activeRule;
	}

}
