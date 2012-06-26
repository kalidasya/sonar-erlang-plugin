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
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.resources.Project;
import org.sonar.api.rules.ActiveRule;
import org.sonar.plugins.erlang.language.Erlang;
import org.sonar.plugins.erlang.testmetrics.utils.GenericFileNameRegexFilter;
import org.sonar.plugins.erlang.violations.ActiveRuleFilter;
import org.sonar.plugins.erlang.violations.ErlangRuleManager;
import org.sonar.plugins.erlang.violations.ErlangViolationResults;
import org.sonar.plugins.erlang.violations.Issue;

/**
 * Read and parse generated dialyzer report
 * 
 * @author tkende
 * 
 */
public class ErlangRefactorErl {
	private final static Logger LOG = LoggerFactory.getLogger(ErlangRefactorErl.class);

	/**
	 * Matches to something like: (ATOM:ATOM/NUMBER)(WHITESPACES)(SOMETHING NOT
	 * WHITESPACES)(:WHITESPACES)(NUMBER,NUMBER-NUMBER,NUMBER) like: game:start/0
	 * /home/dev/project/erlang/game.erl: 4,1-5,12 means: 'application
	 * name':'function name'/'number of parameters' 'URL to the file': 'starting
	 * row','starting col'-'ending row','ending col'
	 */

	public ErlangViolationResults refactorErl(Project project, ErlangRuleManager erlangRuleManager, RulesProfile profile) {
		ErlangViolationResults result = new ErlangViolationResults();
		List<ActiveRule> activeRules = profile.getActiveRulesByRepository("Erlang");

		/**
		 * Read refactorErl results
		 */
		File basedir = new File(project.getFileSystem().getBasedir()+File.separator+((Erlang) project.getLanguage()).getEunitFolder());
		LOG.debug("Parsing refactorErl reports from folder {}", basedir.getAbsolutePath());
		
		String refactorErlPattern = ((Erlang) project.getLanguage()).getRefactorErlFilenamePattern();

		GenericFileNameRegexFilter filter = new GenericFileNameRegexFilter(refactorErlPattern);

		if (basedir.isDirectory() == false) {
			LOG.warn("Folder does not exist {}", basedir);
			return null;
		}

		String[] list = basedir.list(filter);

		if (list.length == 0) {
			LOG.warn("no file matches to : ", refactorErlPattern);
			return null;
		}

		for (String file : list) {
			try {
				File oneReportFile = new File(basedir, file);
				FileInputStream fstream = new FileInputStream(oneReportFile);
				DataInputStream in = new DataInputStream(fstream);
				BufferedReader RefactorErlOutput = new BufferedReader(new InputStreamReader(in));
				BufferedReader breader = new BufferedReader(RefactorErlOutput);
				RefactorErlReport report = RefactorErlReportParser.parse(breader);
			//	String actModuleName = systemId.replaceAll("(.*[\\\\/])(.*?)(\\.erl.*)", "$2");
			//	List<RefactorErlReportUnit> matchingUnits = report.getUnitsByModuleName(actModuleName);
				for (RefactorErlReportUnit refactorErlReportUnit : report.getUnits()) {
					for (RefactorErlMetric metric : refactorErlReportUnit.getMetrics()) {
						ActiveRule activeRule = ActiveRuleFilter.getActiveRuleByRuleName(activeRules, metric.getName());
						if (checkIsValid(activeRule, metric)) {
							Issue issue = new Issue(refactorErlReportUnit.getModuleName() + ".erl",
									refactorErlReportUnit.getStartRow(), activeRule.getRuleKey(), getMessageForMetric(
											activeRule, metric));
							result.getIssues().add(issue);
						}

					}

				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	private String getMessageForMetric(ActiveRule activeRule, RefactorErlMetric metric) {
		String param = activeRule.getParameter("maximum");
		if (param != null) {
			return activeRule.getRule().getName() + " is " + metric.getValue() + " (max allowed is " + param + ")";
		}
		return activeRule.getRule().getDescription();
	}

	private boolean checkIsValid(ActiveRule activeRule, RefactorErlMetric metric) {
		String param = activeRule.getParameter("maximum");
		if (param != null) {
			return Integer.valueOf(metric.getValue()) > Integer.valueOf(param);
		}
		return true;
	}

}
