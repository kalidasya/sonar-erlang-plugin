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

import org.apache.commons.lang.ArrayUtils;
import org.sonar.api.rules.ActiveRule;
import org.sonar.api.rules.Rule;

public class ViolationUtil {

	public static String getMessageForMetric(ActiveRule activeRule, Object value) {
		return getMessageForMetric(activeRule.getRule(), value);
	}

	public static String getMessageForMetric(Rule rule, Object value) {
		String param = (rule.getParam("maximum") != null) ? rule.getParam("maximum").getDefaultValue() : null;
		if (param != null) {
			return rule.getName() + " is " + String.valueOf(value) + " (max allowed is " + param + ")";
		}
		param = (rule.getParam("minimum") != null) ? rule.getParam("minimum").getDefaultValue() : null;
		if (param != null) {
			return rule.getName() + " is " + String.valueOf(value) + " (min allowed is " + param + ")";
		}
		param = (rule.getParam("not") != null) ? rule.getParam("not").getDefaultValue() : null;
		if (param != null) {
			return rule.getName() + " is " + String.valueOf(value) + " (not allowed numbers are: " + param
					+ ")";
		}
		param = (rule.getParam("regex") != null) ? rule.getParam("regex").getDefaultValue() : null;
		if (param != null) {
			return "Regex match found. (regex is: /"
					+ param
					+ "/"
					+ ((rule.getParam("ignoreCase") != null && rule.getParam("ignoreCase").getDefaultValue()
							.equals("true")) ? "i" : "") + ")";
		}
		return rule.getDescription();
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

}
