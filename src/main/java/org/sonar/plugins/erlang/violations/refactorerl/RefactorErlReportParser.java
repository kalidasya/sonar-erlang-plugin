package org.sonar.plugins.erlang.violations.refactorerl;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RefactorErlReportParser {

	private static final Pattern REFACTORERL_UNIT_LEVEL_LINE = Pattern
			.compile("([\'@_a-zA-z0-9]*?:[\'@_a-zA-z0-9]*?/[0-9]+)( *)([^ ]+)(: *)([0-9]+,[0-9]+-[0-9]+,[0-9]+)");

	private static final Pattern REFACTORERL_METRIC_LINE = Pattern
			.compile("([\'@_a-zA-z0-9]*?)( = )([\'@_a-zA-z0-9]*?)");

	public static RefactorErlReport parse(BufferedReader breader) throws IOException {
		RefactorErlReport report = new RefactorErlReport();

		String strLine;
		RefactorErlReportUnit reportUnit = null;
		while ((strLine = breader.readLine()) != null) {
			Matcher matcher = REFACTORERL_UNIT_LEVEL_LINE.matcher(strLine);
			Matcher matcher2 = REFACTORERL_METRIC_LINE.matcher(strLine);
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
			} else if (matcher2.matches()) {
				if (reportUnit != null) {
					String name = matcher2.group(1).trim();
					String value = matcher2.group(2).trim();
					RefactorErlMetric metric = reportUnit.createMetric();
					metric.setName(name);
					metric.setValue(value);
				}
			} else {
				/**
				 * Erease the reportUnit to make sure not trying to analyse external
				 * library metrics
				 */
				reportUnit = null;
			}

		}
		return report;
	}

}
