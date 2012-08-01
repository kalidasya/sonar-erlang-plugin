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
import java.util.Collection;

import org.apache.commons.lang.ArrayUtils;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.rules.ActiveRule;
import org.sonar.api.rules.Rule;

public class ViolationUtil {

	public static String getMessageForMetric(ActiveRule rule, Object value) {
		String param = (rule.getParameter("maximum") != null) ? rule.getParameter("maximum") : null;
		if (param != null) {
			return rule.getRule().getName() + " is " + String.valueOf(value) + " (max allowed is " + param + ")";
		}
		param = (rule.getParameter("minimum") != null) ? rule.getParameter("minimum") : null;
		if (param != null) {
			return rule.getRule().getName() + " is " + String.valueOf(value) + " (min allowed is " + param + ")";
		}
		param = (rule.getParameter("not") != null) ? rule.getParameter("not") : null;
		if (param != null) {
			return rule.getRule().getName() + " is " + String.valueOf(value) + " (not allowed numbers are: " + param
					+ ")";
		}
		param = (rule.getParameter("regex") != null) ? rule.getParameter("regex") : null;
		if (param != null) {
			return "Regex match found. (regex is: /"
					+ param
					+ "/"
					+ ((rule.getParameter("ignoreCase") != null && rule.getParameter("ignoreCase")
							.equals("true")) ? "i" : "") + ")";
		}
		return rule.getRule().getDescription();
	}

	public static boolean checkIsValid(ActiveRule activeRule, String value) {
		String param = activeRule.getParameter("maximum");
		if (param != null) {
			return (Integer.valueOf(value) > Integer.valueOf(param));
		}
		param = activeRule.getParameter("minimum");
		if (param != null) {
			return (Integer.valueOf(value) < Integer.valueOf(param));
		}
		param = activeRule.getParameter("not");
		if (param != null) {
			param = param.replaceAll("[ \n\r\t]", "");
			return ArrayUtils.contains(param.split(","), value);
		}
		return true;
	}
	
	public static Collection<ActiveRule> getActiveRulesFromRules(Collection<Rule> rules, RulesProfile profile){
		Collection<ActiveRule> ret = new ArrayList<ActiveRule>();
		for (Rule rule : rules) {
			ActiveRule r = profile.getActiveRule(rule);
			if(r!=null){
				ret.add(r);
			}
		}
		return ret;
	}

}
