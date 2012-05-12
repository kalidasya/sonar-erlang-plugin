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
