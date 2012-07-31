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
