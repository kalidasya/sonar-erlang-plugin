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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang.ArrayUtils;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.sonar.plugins.erlang.utils.RuleUtil;
import org.sonar.plugins.erlang.violations.ErlangRule;

public class ErlangRuleTest {
	
	private ErlangRule erlangRule;
	private ErlangRule erlangRule2;

	@Before
	public void setup(){
		erlangRule = RuleUtil.getOneRule("D009", "dialyzer");
		erlangRule2 = RuleUtil.getOneRule("D001", "refactorerl");
	}
	
	@Test
	public void checkDialyzerRule(){
		assertFalse(ArrayUtils.contains(erlangRule.getMessages().toArray(), "dunno"));
		assertFalse(ArrayUtils.contains(erlangRule.getMessages().toArray(), "Guard test ~s ~s ~s can never succeed"));
		assertTrue(ArrayUtils.contains(erlangRule.getMessages().toArray(), "Clause guard cannot succeed."));
		assertTrue(ArrayUtils.contains(erlangRule.getMessages().toArray(), "Guard test ~s ~s ~s can never succeed".replaceAll("~.", ".*?").replaceAll("([\\{\\}\\[\\]])", "\\\\$1")));
		
		assertThat(erlangRule.getRule().getKey(), Matchers.equalTo("D009"));
		assertThat(erlangRule.getRule().getName(), Matchers.equalTo("guard_fail"));
		assertThat(erlangRule.getRule().getDescription(), Matchers.equalTo("Clause guard cannot succeed."));
		assertThat(erlangRule.getRule().getConfigKey(), Matchers.equalTo("guard_fail"));
	}

	@Test
	public void checkhasMessage(){
		assertTrue(erlangRule.hasMessage("Guard test abc de3f stgAS6 can never succeed"));
		assertTrue(erlangRule.hasMessage("Clause guard cannot succeed."));
		assertFalse(erlangRule.hasMessage("No matching"));
	}
	
}
