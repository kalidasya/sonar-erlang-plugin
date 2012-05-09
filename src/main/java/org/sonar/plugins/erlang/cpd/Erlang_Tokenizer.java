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
package org.sonar.plugins.erlang.cpd;

import java.util.ArrayList;

import net.sourceforge.pmd.cpd.AbstractTokenizer;
import net.sourceforge.pmd.cpd.Tokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Erlang_Tokenizer  extends AbstractTokenizer implements Tokenizer {

  private static final Logger LOG = LoggerFactory.getLogger(Erlang_Tokenizer.class);

  public Erlang_Tokenizer()
	{
	  	this.spanMultipleLinesString = false;
//		 setting markers for "string" in Erlang
		this.stringToken = new ArrayList<String>();
		this.stringToken.add("\"");
		// setting markers for 'ignorable character' in Erlang
		this.ignorableCharacter = new ArrayList<String>();
		this.ignorableCharacter.add("(");
		this.ignorableCharacter.add(")");
//		this.ignorableCharacter.add(",");
//		this.ignorableCharacter.add(".");

		// setting markers for 'ignorable string' in Erlang
		this.ignorableStmt = new ArrayList<String>();
	//	this.ignorableStmt.add("-export");
	//	this.ignorableStmt.add("-module");
	//	this.ignorableStmt.add("include");
//		this.ignorableStmt.add("try");
//		this.ignorableStmt.add("catch");
//		this.ignorableStmt.add("case");
//		this.ignorableStmt.add("end");
		// Erlang comment start with an !
//		this.oneLineCommentChar = '%';
	}
}
