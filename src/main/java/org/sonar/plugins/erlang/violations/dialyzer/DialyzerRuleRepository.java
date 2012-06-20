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

package org.sonar.plugins.erlang.violations.dialyzer;

import java.util.List;

import org.sonar.api.rules.Rule;
import org.sonar.api.rules.RuleRepository;
import org.sonar.api.rules.XMLRuleParser;
import org.sonar.plugins.erlang.ErlangPlugin;

public class DialyzerRuleRepository extends RuleRepository {
	private XMLRuleParser parser;
	public static final String REPOSITORY_NAME = "Erlang";
	public static final String REPOSITORY_KEY = "Erlang";
	public static final String RULES_FILE = "/org/sonar/plugins/erlang/dialyzer/rules.xml";

	public DialyzerRuleRepository() {
		super(REPOSITORY_KEY, ErlangPlugin.LANG_KEY);
		setName(REPOSITORY_NAME);
		this.parser = new XMLRuleParser();
	}

	public DialyzerRuleRepository(XMLRuleParser parser) {
		super(REPOSITORY_KEY, ErlangPlugin.LANG_KEY);
		setName(REPOSITORY_NAME);
		
		this.parser = parser;
	}

	@Override
	  public List<Rule> createRules() {
	    List<Rule> rules = parser.parse(getClass().getResourceAsStream(RULES_FILE));
	    for (Rule r : rules) {
	      r.setRepositoryKey(REPOSITORY_KEY);
	    }
	    return rules;
	  }

}