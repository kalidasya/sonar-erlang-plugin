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
package org.sonar.plugins.erlang.language;

import java.util.ArrayList;
import java.util.List;

public class ErlangFunction {

	private final String functionSign;
	private final List<String> functionLines = new ArrayList<String>();
	
	
	
	public ErlangFunction(String functionSign) {
		super();
		this.functionSign = functionSign;
	}

	public void addLine(String line){
		functionLines.add(line);
	}
	
	public String getFirstLine(){
		return functionLines.get(0);
	}
	
	public List<String> getLines(){
		return functionLines;
	}

	public String getFunctionSign() {
		return functionSign;
	}
}
