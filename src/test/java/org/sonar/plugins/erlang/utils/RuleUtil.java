package org.sonar.plugins.erlang.utils;

import org.sonar.plugins.erlang.dialyzer.DialyzerRule;
import org.sonar.plugins.erlang.dialyzer.DialyzerRuleManager;

public class RuleUtil {

	public static DialyzerRule getOneRule(String key){
		DialyzerRuleManager rm = new DialyzerRuleManager();
		return rm.getDializerRuleByKey(key);
	}
	
}
