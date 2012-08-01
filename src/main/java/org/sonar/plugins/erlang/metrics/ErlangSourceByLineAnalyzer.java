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
package org.sonar.plugins.erlang.metrics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.rules.ActiveRule;
import org.sonar.plugins.erlang.language.ErlangFunction;
import org.sonar.plugins.erlang.violations.ViolationReport;
import org.sonar.plugins.erlang.violations.ViolationReportUnit;
import org.sonar.plugins.erlang.violations.ViolationUtil;
import org.sonar.plugins.erlang.violations.dialyzer.ErlangDialyzer;

/**
 * This class contains all meause which can be done only by iterating over the
 * lines of the code. To avoid going through the source multiple times to count
 * different metrics all these calculations should be measured here.
 * 
 * @author kalidasya
 * 
 */

public class ErlangSourceByLineAnalyzer {
	private static final String FUNCTION_ENDS_REGEX = ".*\\.$";
	private static final String FUNCTION_START_REGEX = "(^[a-z]+[a-z0-9_@]+)( *\\()(.*?)(\\)) *->";
	/**
	 * Anything which starts with on or more %
	 */
	public static final Pattern isCommentPatter = Pattern.compile("%+.*");
	/**
	 * Anything which starts with one or more % and after optional amount of
	 * spaces only one non alphabet char is repeated till the end of the line.
	 */
	public static final Pattern isDecoratorPatter = Pattern.compile("%+ *([^A-Za-z])\\1+$");
	public static final Pattern functionStartsPattern = Pattern.compile(FUNCTION_START_REGEX);

	private static final Logger LOG = LoggerFactory.getLogger(ErlangDialyzer.class);

	private final List<String> lines;
	private List<ErlangFunction> functions = new ArrayList<ErlangFunction>();
	private int numberOfComments;
	private int numberOfBlankLines;
	private int numberOfDecoratorLines;
	private Map<ActiveRule, Pattern> regexRulesMap = new HashMap<ActiveRule, Pattern>();
	private ViolationReport violationReport = new ViolationReport();

	public ErlangSourceByLineAnalyzer(List<String> lines, Collection<ActiveRule> regexRules) {
		super();
		this.lines = lines;
		if (regexRules != null) {
			for (ActiveRule rule : regexRules) {
				if (Boolean.valueOf(rule.getParameter("ignoreCase"))) {
					regexRulesMap.put(rule, Pattern.compile(rule.getParameter("regex"),
							Pattern.CASE_INSENSITIVE));
				} else {
					regexRulesMap.put(rule, Pattern.compile(rule.getParameter("regex")));
				}
			}

		}

		analyze();
	}

	/**
	 * Only count commented lines which has some text after the comment prefix
	 * so we don't count those line which were created for formatting reason
	 * like: %%-------------------------------------
	 * %%=====================================
	 * 
	 * @return
	 */
	private void analyze() {

		boolean functionOpened = false;
		ErlangFunction latest = null;
		int rowNum = 1;
		for (String line : lines) {
			String lineTrimmed = line.trim();
			if (StringUtils.isBlank(line)) {
				numberOfBlankLines++;
				/**
				 * Only count commented lines which has some text after the
				 * comment prefix so we don't count those line which were
				 * created for formatting reason like:
				 * %%-------------------------------------
				 * %%=====================================
				 * 
				 * @return
				 */
			} else if (isDecoratorPatter.matcher(lineTrimmed).matches()) {
				numberOfDecoratorLines++;
			} else if (isCommentPatter.matcher(lineTrimmed).matches()) {
				numberOfComments++;
			} else if (functionStartsPattern.matcher(lineTrimmed).matches() && !functionOpened) {
				Matcher m = functionStartsPattern.matcher(lineTrimmed);
				m.find();
				String paramNumber = (m.group(3).trim().length() == 0) ? "0" : String.valueOf(m.group(3)
						.trim().split(",").length);
				functions.add(new ErlangFunction(m.group(1) + "/" + paramNumber));
				latest = functions.get(functions.size() - 1);
				latest.addLine(line);
				functionOpened = true;
			} else {
				if (functionOpened) {
					latest.addLine(line);
				}
				if (lineTrimmed.matches(FUNCTION_ENDS_REGEX) && functionOpened) {
					latest = null;
					functionOpened = false;
				}
			}
			if (regexRulesMap.size() > 0) {
				for (Entry<ActiveRule, Pattern> entry : regexRulesMap.entrySet()) {
					if (entry.getValue().matcher(lineTrimmed).find()) {
						ViolationReportUnit unit = violationReport.createUnit();
						unit.setStartRow(rowNum);
						unit.setModuleName("");
						unit.setMetricKey(entry.getKey().getRuleKey());
						unit.setDescription(ViolationUtil.getMessageForMetric(entry.getKey(), null));
						unit.setRepositoryKey("Erlang");
					}
				}

			}
			rowNum++;
		}

	}

	public int getNumberOfComments() {
		return numberOfComments;
	}

	public int getNumberOfBlankLines() {
		return numberOfBlankLines;
	}

	public int getNumberOfDecoratorLines() {
		return numberOfDecoratorLines;
	}

	public int countLines() {
		return lines.size();
	}

	public int getLinesOfCode() {
		return countLines() - numberOfBlankLines - numberOfComments - numberOfDecoratorLines;
	}

	public double getNumberOfFunctions() {
		return functions.size();
	}

	public List<ErlangFunction> getFunctions() {
		return functions;
	}

	public ViolationReport getViolationReport() {
		return violationReport;
	}

	public void setViolationReport(ViolationReport violationReport) {
		this.violationReport = violationReport;
	}
}
