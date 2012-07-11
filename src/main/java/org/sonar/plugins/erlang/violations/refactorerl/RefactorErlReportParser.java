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
package org.sonar.plugins.erlang.violations.refactorerl;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.sonar.plugins.erlang.violations.ViolationReport;
import org.sonar.plugins.erlang.violations.ViolationReportUnit;

public class RefactorErlReportParser {

	private static final Pattern REFACTORERL_UNIT_LEVEL_LINE = Pattern
			.compile("([\'@_a-zA-z0-9]*?:[\'@_a-zA-z0-9]*?/[0-9]+)( *)([^ ]+)(: *)([0-9]+,[0-9]+-[0-9]+,[0-9]+)");

	private static final Pattern REFACTORERL_MODULE_LEVEL_LINE = Pattern
			.compile("([\'@_a-zA-z0-9\\.]*?)( *)([^ ]+)(: *)([0-9]+-[0-9]+)");

	private static final Pattern REFACTORERL_METRIC_LINE = Pattern
			.compile("([\'@_a-zA-z0-9]*?)( = )([\'@_a-zA-z0-9]*?)");

	public static List<ViolationReportUnit> parse(BufferedReader breader) throws IOException {
		ViolationReport report = new ViolationReport();

		String strLine;
		ViolationReportUnit reportUnit = null;
		while ((strLine = breader.readLine()) != null) {
			strLine = strLine.trim();
			Matcher matcher = REFACTORERL_UNIT_LEVEL_LINE.matcher(strLine);
			Matcher matcher2 = REFACTORERL_METRIC_LINE.matcher(strLine);
			Matcher matcher3 = REFACTORERL_MODULE_LEVEL_LINE.matcher(strLine);
			if (matcher.matches()) {
				String[] moduleNameAndMethod = matcher.group(1).split(":");
				String moduleName = moduleNameAndMethod[0];
				String methodName = moduleNameAndMethod[1];
				String url = matcher.group(3);
				String position = matcher.group(5);

				reportUnit = report.createUnit();
				reportUnit.setModuleName(moduleName);
				reportUnit.setMethodSign(methodName);
				reportUnit.setUri(url);
				reportUnit.setPosition(position);
				reportUnit.setRepositoryKey(ErlangRefactorErl.REPO_KEY);
			} else if (matcher3.matches()) {
				String moduleName = matcher3.group(1).replaceAll("\\.erl", "");
				String url = matcher3.group(3);
				String position = matcher3.group(5);

				reportUnit = report.createUnit();
				reportUnit.setModuleName(moduleName);
				reportUnit.setUri(url);
				reportUnit.setPosition(position);
				reportUnit.setRepositoryKey(ErlangRefactorErl.REPO_KEY);
			} else if (matcher2.matches()) {
				if (reportUnit != null) {
					String name = matcher2.group(1).trim();
					String value = matcher2.group(3).trim();
					reportUnit.setMetricKey(name);
					if(RefactorErlMappings.map.containsKey(value)){
						reportUnit.setMetricValue(RefactorErlMappings.map.get(value));
					} else {
						reportUnit.setMetricValue(value);	
					}
					
				}
			} else {
				/**
				 * Erease the reportUnit to make sure not trying to analyse
				 * external library metrics
				 */
				reportUnit = null;
			}

		}
		return report.getUnits();
	}

}
