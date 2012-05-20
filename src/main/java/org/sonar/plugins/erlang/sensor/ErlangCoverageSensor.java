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

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.PropertiesBuilder;
import org.sonar.api.resources.InputFile;
import org.sonar.api.resources.Project;
import org.sonar.plugins.erlang.ErlangPlugin;
import org.sonar.plugins.erlang.language.Erlang;
import org.sonar.plugins.erlang.language.ErlangFile;
import org.sonar.plugins.erlang.testmetrics.cover.CoverCoverageParser;
import org.sonar.plugins.erlang.testmetrics.cover.CoverFileCoverage;
import org.sonar.plugins.erlang.testmetrics.utils.GenericExtFilter;
import org.sonar.plugins.erlang.testmetrics.utils.TestSensorUtils;

public final class ErlangCoverageSensor extends AbstractErlangSensor {

	public ErlangCoverageSensor(Erlang erlang) {
		super(erlang);
	}

	private final static Logger LOG = LoggerFactory.getLogger(ErlangCoverageSensor.class);

	public void analyse(Project project, SensorContext sensorContext) {

		File reportsDir = new File(project.getFileSystem().getBasedir(), erlang.getEunitFolder());
		GenericExtFilter filter = new GenericExtFilter(".html");

		if (reportsDir.isDirectory() == false) {
			LOG.warn("Folder does not exist {}", reportsDir);
			/**
			 * Make 0 coverage to those source files what are missing from the list
			 */
			setZeroCoverageIfNoFilesArePresented(project, sensorContext, new ArrayList());
			return;
		}

		String[] list = reportsDir.list(filter);

		if (list.length == 0) {
			LOG.warn("no files end with : ", reportsDir);
			return;
		}
		List<String> coveredFiles = new ArrayList<String>();
		for (String file : list) {
			/*
			 * TODO move cover filename strings to elsewhere
			 */
			if (!file.matches(".*\\.COVER.html") || file.contains("_eunit")) {
				continue;
			}
			String sourceName = file.replaceAll("(.*?)(\\.COVER\\.html)", "$1");
			/*
			 * The Path could contain other source files (the sources of the dependencies) what will have 
			 * code coverage so we have to exclude everything which is not in the source folder of the project
			 */
			if (TestSensorUtils.findFileForReport(project.getFileSystem().mainFiles(erlang.getKey()),
					sourceName.concat(ErlangPlugin.EXTENSION)) != null) {
				coveredFiles.add(sourceName.concat(ErlangPlugin.EXTENSION));
				CoverCoverageParser parser = new CoverCoverageParser();
				CoverFileCoverage fileCoverage = parser.parseFile(new File(reportsDir, file), project.getFileSystem()
						.getSourceDirs().get(0).getName(), sourceName);
				LOG.debug("Analysing coverage file: " + file + " for coverage result of " + sourceName);
				analyseCoveredFile(project, sensorContext, fileCoverage, sourceName);
			}
		}
		/**
		 * Make 0 coverage to those source files what are missing from the list
		 */
		setZeroCoverageIfNoFilesArePresented(project, sensorContext, coveredFiles);

	}

	private void setZeroCoverageIfNoFilesArePresented(Project project, SensorContext sensorContext,
			List<String> coveredFiles) {
		for (InputFile source : project.getFileSystem().mainFiles(erlang.getKey())) {
			if (!coveredFiles.contains(source.getFile().getName())) {
				ErlangFile sourceResource = ErlangFile.fromInputFile(source, true);
				PropertiesBuilder<Integer, Integer> lineHitsData = new PropertiesBuilder<Integer, Integer>(
						CoreMetrics.COVERAGE_LINE_HITS_DATA);
				for (int x = 1; x < sensorContext.getMeasure(sourceResource, CoreMetrics.LINES).getIntValue(); x++) {
					lineHitsData.add(x, 0);
				}
				// use non comment lines of code for coverage calculation
				Measure ncloc = sensorContext.getMeasure(sourceResource, CoreMetrics.NCLOC);
				sensorContext.saveMeasure(sourceResource, lineHitsData.build());
				sensorContext.saveMeasure(sourceResource, CoreMetrics.LINES_TO_COVER, ncloc.getValue());
				sensorContext.saveMeasure(sourceResource, CoreMetrics.UNCOVERED_LINES, ncloc.getValue());
			}
		}
	}

	protected void analyseCoveredFile(Project project, SensorContext sensorContext, CoverFileCoverage coveredFile,
			String sourceName) {
		InputFile sourceFile = TestSensorUtils.findFileForReport(project.getFileSystem().mainFiles(erlang.getKey()),
				sourceName.concat(ErlangPlugin.EXTENSION));
		ErlangFile sourceResource = ErlangFile.fromInputFile(sourceFile, true);

		PropertiesBuilder<Integer, Integer> lineHitsData = new PropertiesBuilder<Integer, Integer>(
				CoreMetrics.COVERAGE_LINE_HITS_DATA);

		if (coveredFile.getLineCoverageData().size() > 0) {
			Map<Integer, Integer> hits = coveredFile.getLineCoverageData();
			for (Map.Entry<Integer, Integer> entry : hits.entrySet()) {
				lineHitsData.add(entry.getKey(), entry.getValue());
			}
			try {
				sensorContext.saveMeasure(sourceResource, lineHitsData.build());
			} catch (Exception e) {
				e.printStackTrace();
			}
			sensorContext.saveMeasure(sourceResource, CoreMetrics.LINES_TO_COVER, (double) coveredFile.getLinesToCover());
			sensorContext.saveMeasure(sourceResource, CoreMetrics.UNCOVERED_LINES,
					(double) coveredFile.getUncoveredLines());
			/**
			 * Put STATEMENTS METRIC here because the coverage file only contains metrics on executable statements so 
			 * lines to cover = statements
			 */
			sensorContext.saveMeasure(sourceResource, CoreMetrics.STATEMENTS, (double) coveredFile.getLinesToCover());
		}
	}

	protected CoverFileCoverage getFileCoverage(InputFile input, List<CoverFileCoverage> coverages) {
		for (CoverFileCoverage file : coverages) {
			if (file.getFullFileName().equals(input.getFile().getAbsolutePath())) {
				return file;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName();
	}

}
