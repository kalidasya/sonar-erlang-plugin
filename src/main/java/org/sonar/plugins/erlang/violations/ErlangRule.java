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
import java.util.List;

import org.sonar.api.rules.Rule;

public class ErlangRule{
	List<String> messages = new ArrayList<String>();
	Rule sonarRule = new Rule();

	public ErlangRule(){
		super();
	}
	
	public ErlangRule(Rule rule, List<String> messages){
		this.messages = messages;
		sonarRule = rule;
	}
	
	public boolean hasMessage(String message) {
		boolean ret = false;
		for (String ruleMessage : messages) {
			if(message.matches(ruleMessage)){
				ret = true;
				break;
			}
		}
		return ret;
	}

	public void setMessages(List<String> messageList) {
		messages = messageList;
	}
	
	public List<String> getMessages(){
		return messages;
	}
	
	public void addMessage(String message) {
		messages.add(message);
	}
	
	public Rule getRule(){
		return sonarRule;
	}

}
