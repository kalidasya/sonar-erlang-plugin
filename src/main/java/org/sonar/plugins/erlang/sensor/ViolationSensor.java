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
package org.sonar.plugins.erlang.sensor;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.PersistenceMode;
import org.sonar.api.measures.RangeDistributionBuilder;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.resources.InputFile;
import org.sonar.api.resources.Project;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.Violation;
import org.sonar.plugins.erlang.language.Erlang;
import org.sonar.plugins.erlang.language.ErlangFile;
import org.sonar.plugins.erlang.violations.ErlangRuleManager;
import org.sonar.plugins.erlang.violations.ErlangRuleRepository;
import org.sonar.plugins.erlang.violations.ViolationReport;
import org.sonar.plugins.erlang.violations.ViolationReportUnit;
import org.sonar.plugins.erlang.violations.dialyzer.ErlangDialyzer;
import org.sonar.plugins.erlang.violations.refactorerl.ErlangRefactorErl;

/**
 * Calls the dialyzer report parser and the refactorerl report parser and saves
 * violations to sonar
 * 
 * @author tkende
 * 
 */
public class ViolationSensor extends AbstractErlangSensor {

	private ErlangRuleManager dialyzerRuleManager = new ErlangRuleManager(ErlangRuleRepository.DIALYZER_PATH);
	private ErlangRuleManager refactorErlRuleManager = new ErlangRuleManager(ErlangRuleRepository.REFACTORERL_PATH);
	private RulesProfile rulesProfile;

	public ViolationSensor(Erlang erlang, RulesProfile rulesProfile) {
		super(erlang);
		this.rulesProfile = rulesProfile;
	}

	private static final Logger LOG = LoggerFactory.getLogger(ViolationSensor.class);
	private ErlangDialyzer dialyzer = new ErlangDialyzer();
	private ErlangRefactorErl refactorErl = new ErlangRefactorErl();

	public void analyse(Project project, SensorContext context) {
		ViolationReport report = refactorErl.refactorErl(project, refactorErlRuleManager, rulesProfile);
		report.appendUnits(dialyzer.dialyzer(project, dialyzerRuleManager).getUnits());
		for (InputFile inputFile : project.getFileSystem().mainFiles(getErlang().getKey())) {
			ErlangFile erlangFile = ErlangFile.fromInputFile(inputFile);
			try {
				analyzeFile(erlangFile, project, context, report);
			} catch (Exception e) {
				LOG.error("Can not analyze the file " + inputFile.getFileBaseDir() + "\\" + inputFile.getRelativePath(), e);
			}
		}
	}

	private void analyzeFile(ErlangFile erlangFile, Project project, SensorContext context, ViolationReport report)
			throws IOException {
		String actModuleName = erlangFile.getName();

		for (ViolationReportUnit reportUnit : report.getUnitsByModuleName(actModuleName)) {
			/**
			 * TODO: review this, how can I get the repository key from the
			 * rulesProfile, instead of this reference... it is also stupid that
			 * the DialyzerRuleRepository and the RefactorErlRuleRepository has the
			 * same key and name...or not?
			 */
			Rule rule = Rule.create(ErlangRuleRepository.REPOSITORY_KEY, reportUnit.getMetricKey());
			Violation violation = Violation.create(rule, erlangFile);
			violation.setLineId(reportUnit.getStartRow());
			violation.setMessage(reportUnit.getDescription());
			context.saveViolation(violation);
		}
	}
}
