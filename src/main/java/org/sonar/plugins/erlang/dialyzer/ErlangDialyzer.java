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

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import org.sonar.api.resources.Project;
import org.sonar.plugins.erlang.language.Erlang;

public class ErlangDialyzer {
	private static final String DIALYZER_VIOLATION_ROW_REGEX = "(.*?)(:[0-9]+:)(.*)";

	public ErlangDialyzerResult dialyzer(Project project, String systemId, Reader reader, DialyzerRuleManager dialyzerRuleManager) {
		ErlangDialyzerResult result = new ErlangDialyzerResult();
		/**
		 * Read dialyzer results
		 */
		try {
			String dialyzerUri = ((Erlang) project.getLanguage()).getDialyzerUri();
			File file = new File(project.getFileSystem().getBasedir(), dialyzerUri);
			FileInputStream fstream = new FileInputStream(file);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader dialyzerOutput = new BufferedReader(new InputStreamReader(in));
			BufferedReader breader = new BufferedReader(dialyzerOutput);
			
			String strLine;
			while ((strLine = breader.readLine()) != null) {
				if (strLine.matches(DIALYZER_VIOLATION_ROW_REGEX)) {
					String functionName = strLine.trim().replaceAll(DIALYZER_VIOLATION_ROW_REGEX, "$1");
					if (systemId.contains(functionName)) {
						String[] res = strLine.split(":");
						Issue i = new Issue(res[0],Integer.valueOf(res[1]), dialyzerRuleManager.getRuleIdByMessage(res[2].trim()), res[2].trim());
						result.getIssues().add(i);
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}

}
