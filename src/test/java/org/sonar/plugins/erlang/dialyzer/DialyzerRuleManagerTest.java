package org.sonar.plugins.erlang.dialyzer;

import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

public class DialyzerRuleManagerTest {

	private DialyzerRuleManager rm;

	@Before
	public void setup(){
		rm = new DialyzerRuleManager();
	}
	
	@Test
	public void checkGetRuleByKey(){
		assertThat(rm.getRuleByKey("Z1313"), Matchers.nullValue());
		assertThat(rm.getRuleByKey("D001").getName(), Matchers.equalTo("Function that will never return a value"));
	}
	
	@Test
	public void checkGetDialyzerRuleByKey(){
		assertThat(rm.getDializerRuleByKey("Z1313"), Matchers.nullValue());
		assertThat(rm.getDializerRuleByKey("D001").sonarRule.getName(), Matchers.equalTo("Function that will never return a value"));
	}
	
	@Test
	public void checkGetRuleIdByMessage(){
		assertThat(rm.getRuleIdByMessage("Function ~w/~w has no local return"), Matchers.equalTo("D013"));
		assertThat(rm.getRuleIdByMessage("Z1313"), Matchers.equalTo(DialyzerRuleManager.OTHER_RULES_KEY));
	}
	
}
