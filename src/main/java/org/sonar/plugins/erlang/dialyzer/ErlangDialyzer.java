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
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.resources.Project;
import org.sonar.plugins.erlang.language.Erlang;

public class ErlangDialyzer {
	private static final Logger LOG = LoggerFactory.getLogger(ErlangDialyzer.class);

	public ErlangDialyzerResult dialyzer(Project project, String systemId, Reader reader, DialyzerRuleManager dialyzerRuleManager) throws IOException {
		ErlangDialyzerResult result = new ErlangDialyzerResult();
		/**
		 * Read dialyzer results
		 */
		String dialyzerUri = ((Erlang) project.getLanguage()).getDialyzerUri();
		File file = new File(project.getFileSystem().getBasedir(), dialyzerUri);
		FileInputStream fstream = new FileInputStream(file);
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader dialyzerOutput = new BufferedReader(new InputStreamReader(in));
		/**
		 * Find methods
		 */
		BufferedReader breader = new BufferedReader(reader);
		String strLine;
		boolean functionOpened = false;
		ErlangFunction latest = null;
		while ((strLine = breader.readLine()) != null) {
		//	LOG.debug("while started" + strLine);
			if (strLine.trim().matches("^[a-z]+[a-z0-9_@]+ *\\(.*?\\) *->") && !functionOpened) {
		//		LOG.debug("function found" + strLine);
				result.getFunctions().add(new ErlangFunction());
				latest = result.getFunctions().get(result.getFunctions().size() - 1);
				latest.addLine(strLine);
				functionOpened = true;
			} else {
				if (functionOpened) {
		//			LOG.debug("function open" + strLine);
					latest.addLine(strLine);
				}
				if (strLine.trim().matches(".*\\.$") && functionOpened) {
		//			LOG.debug("function closed");
					latest = null;
					functionOpened = false;
				}
			}
		}

		breader = new BufferedReader(dialyzerOutput);
		while ((strLine = breader.readLine()) != null) {
			if (strLine.matches("(.*?)(:[0-9]+:)(.*)")) {
				String functionName = strLine.trim().replaceAll("(.*?)(:[0-9]+:)(.*)", "$1");
				if (systemId.contains(functionName)) {
					String[] res = strLine.split(":");
					Issue i = new Issue(res[0],Integer.valueOf(res[1]), dialyzerRuleManager.getRuleIdByMessage(res[2].trim()), res[2].trim());
					result.getIssues().add(i);
				}
			}
		}

		return result;
	}

}
