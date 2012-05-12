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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang.ArrayUtils;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.sonar.plugins.erlang.utils.RuleUtil;

public class DialyzerRuleTest {
	
	private DialyzerRule dialyzerRule;

	@Before
	public void setup(){
		dialyzerRule = RuleUtil.getOneRule("D009");
	}
	
	@Test
	public void checkDialyzerRule(){
		assertFalse(ArrayUtils.contains(dialyzerRule.messages.toArray(), "dunno"));
		assertFalse(ArrayUtils.contains(dialyzerRule.messages.toArray(), "Guard test ~s ~s ~s can never succeed"));
		assertTrue(ArrayUtils.contains(dialyzerRule.messages.toArray(), "Clause guard cannot succeed."));
		assertTrue(ArrayUtils.contains(dialyzerRule.messages.toArray(), "Guard test ~s ~s ~s can never succeed".replaceAll("~.", ".*?").replaceAll("([\\{\\}\\[\\]])", "\\\\$1")));
		
		assertThat(dialyzerRule.getRule().getKey(), Matchers.equalTo("D009"));
		assertThat(dialyzerRule.getRule().getName(), Matchers.equalTo("guard_fail"));
		assertThat(dialyzerRule.getRule().getDescription(), Matchers.equalTo("Clause guard cannot succeed."));
		assertThat(dialyzerRule.getRule().getConfigKey(), Matchers.equalTo("guard_fail"));
	}

	@Test
	public void checkhasMessage(){
		assertTrue(dialyzerRule.hasMessage("Guard test abc de3f stgAS6 can never succeed"));
		assertTrue(dialyzerRule.hasMessage("Clause guard cannot succeed."));
		assertFalse(dialyzerRule.hasMessage("No matching"));
	}
	
}
