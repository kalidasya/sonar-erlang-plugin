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
package org.sonar.plugins.erlang.violation;

import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.sonar.plugins.erlang.violations.ErlangRuleManager;
import org.sonar.plugins.erlang.violations.ErlangRuleRepository;

public class ErlangRuleManagerTest {

	private ErlangRuleManager drm;
	private ErlangRuleManager rrm;

	@Before
	public void setup(){
		drm = new ErlangRuleManager(ErlangRuleRepository.DIALYZER_PATH);
		rrm = new ErlangRuleManager(ErlangRuleRepository.REFACTORERL_PATH);
	}
	
	@Test
	public void checkGetRuleByKey(){
		assertThat(drm.getRuleByKey("Z1313"), Matchers.nullValue());
		assertThat(drm.getRuleByKey("D001").getName(), Matchers.equalTo("Function that will never return a value"));
		assertThat(rrm.getRuleByKey("R001").getName(), Matchers.equalTo("mcCabe"));
	}
	
	@Test
	public void checkGetDialyzerRuleByKey(){
		assertThat(drm.getErlangRuleByKey("Z1313"), Matchers.nullValue());
		assertThat(rrm.getErlangRuleByKey("R001").getRule().getName(), Matchers.equalTo("mcCabe"));
	}
	
	@Test
	public void checkGetRuleIdByMessage(){
		assertThat(drm.getRuleKeyByMessage("Function ~w/~w has no local return"), Matchers.equalTo("D013"));
		assertThat(drm.getRuleKeyByMessage("Z1313"), Matchers.equalTo(ErlangRuleManager.OTHER_RULES_KEY));
	}
	
	@Test
	public void checkGetErlangRules(){
		assertThat(rrm.getErlangRules(), Matchers.notNullValue());
	}
	
	@Test
	public void checkGetRuleKeyByName(){
		assertThat(rrm.getRuleKeyByName("mcCabe"), Matchers.equalTo("R001"));
		assertThat(drm.getRuleKeyByName("call_to_missing"), Matchers.equalTo("D005"));
		assertThat(drm.getRuleKeyByName("NOT_VALID_NAME"), Matchers.equalTo("OTHER_RULES"));
		
	}
	
	@Test
	public void checkGetRuleByName(){
		assertThat(rrm.getRuleByName("mcCabe").getKey(), Matchers.equalTo("R001"));
		assertThat(drm.getRuleByName("call_to_missing").getKey(), Matchers.equalTo("D005"));
		assertThat(drm.getRuleByName("NOT_VALID_NAME"), Matchers.nullValue());
		
	}
	
}
