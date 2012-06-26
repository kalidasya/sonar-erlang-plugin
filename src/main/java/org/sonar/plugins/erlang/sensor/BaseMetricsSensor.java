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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.resources.InputFile;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.ProjectFileSystem;
import org.sonar.plugins.erlang.language.Erlang;
import org.sonar.plugins.erlang.language.ErlangFile;
import org.sonar.plugins.erlang.language.ErlangPackage;
import org.sonar.plugins.erlang.metrics.ErlangSourceByLineAnalyzer;
import org.sonar.plugins.erlang.metrics.PublicApiCounter;
import org.sonar.plugins.erlang.utils.StringUtils;

/**
 * This is the main sensor of the Erlang plugin. It gathers all results of the
 * computation of base metrics for all Erlang resources.
 * 
 * @since 0.1
 */
public class BaseMetricsSensor extends AbstractErlangSensor {

	private static final Logger LOGGER = LoggerFactory.getLogger(BaseMetricsSensor.class);

	public BaseMetricsSensor(Erlang erlang) {
		super(erlang);
	}

	public void analyse(Project project, SensorContext sensorContext) {
		final ProjectFileSystem fileSystem = project.getFileSystem();
		final String charset = fileSystem.getSourceCharset().toString();
		final Set<ErlangPackage> packages = new HashSet<ErlangPackage>();

		// MetricDistribution complexityOfClasses = null;
		// MetricDistribution complexityOfFunctions = null;
		for (InputFile inputFile : fileSystem.mainFiles(getErlang().getKey())) {
			final ErlangFile erlangFile = ErlangFile.fromInputFile(inputFile);
			packages.add(erlangFile.getParent());
			sensorContext.saveMeasure(erlangFile, CoreMetrics.FILES, 1.0);

			try {
				final String source = FileUtils.readFileToString(inputFile.getFile(), charset);
				final List<String> lines = StringUtils.convertStringToListOfLines(source);

				final ErlangSourceByLineAnalyzer linesAnalyzer = new ErlangSourceByLineAnalyzer(lines);

				addLineMetrics(sensorContext, erlangFile, linesAnalyzer);
				addCodeMetrics(sensorContext, erlangFile, linesAnalyzer);
				addPublicApiMetrics(sensorContext, erlangFile, source, linesAnalyzer);

				// complexityOfClasses =
				// sumUpMetricDistributions(complexityOfClasses,
				// ComplexityCalculator.measureComplexityOfClasses(source));

				// complexityOfFunctions =
				// sumUpMetricDistributions(complexityOfFunctions,
				// ComplexityCalculator.measureComplexityOfFunctions(source));

			} catch (IOException ioe) {
				LOGGER.error("Could not read the file: " + inputFile.getFile().getAbsolutePath(), ioe);
			}
		}
		if (project.getModules().size() > 0) {
			sensorContext.saveMeasure(CoreMetrics.PROJECTS, (double) project.getModules().size());
		}

		// sensorContext.saveMeasure(complexityOfClasses.getMeasure());
		// sensorContext.saveMeasure(complexityOfFunctions.getMeasure());

		computePackagesMetric(sensorContext, packages);
	}

	private void addLineMetrics(SensorContext sensorContext, ErlangFile erlangFile,
			ErlangSourceByLineAnalyzer linesAnalyzer) {
		sensorContext.saveMeasure(erlangFile, CoreMetrics.LINES, (double) linesAnalyzer.countLines());
		sensorContext.saveMeasure(erlangFile, CoreMetrics.NCLOC, (double) linesAnalyzer.getLinesOfCode());
		sensorContext.saveMeasure(erlangFile, CoreMetrics.COMMENT_LINES, (double) linesAnalyzer.getNumberOfComments());
	}

	private void addCodeMetrics(SensorContext sensorContext, ErlangFile erlangFile,
			ErlangSourceByLineAnalyzer linesAnalyzer) {
		sensorContext.saveMeasure(erlangFile, CoreMetrics.CLASSES, 1D);
		// sensorContext.saveMeasure(erlangFile, CoreMetrics.STATEMENTS,
		// (double) StatementCounter.countStatements(source));

		sensorContext.saveMeasure(erlangFile, CoreMetrics.FUNCTIONS, linesAnalyzer.getNumberOfFunctions());
		// sensorContext.saveMeasure(erlangFile, CoreMetrics.COMPLEXITY,
		// (double) ComplexityCalculator.measureComplexity(source));
	}

	private void addPublicApiMetrics(SensorContext sensorContext, ErlangFile erlangFile, String source,
			ErlangSourceByLineAnalyzer linesnAlyzer) throws IOException {
		List<Double> publicApiCounter = PublicApiCounter.countPublicApi(source, linesnAlyzer);
		Double publicApi = publicApiCounter.get(0);
		sensorContext.saveMeasure(erlangFile, CoreMetrics.PUBLIC_API, publicApi);
		Double undocApi = publicApiCounter.get(1);
		sensorContext.saveMeasure(erlangFile, CoreMetrics.PUBLIC_UNDOCUMENTED_API, undocApi);
		double density = 0D;
		if (publicApi > 0) {
			density = (publicApi - undocApi) * 100d / publicApi;
		}
		sensorContext.saveMeasure(erlangFile, CoreMetrics.PUBLIC_DOCUMENTED_API_DENSITY, density);
	}

	private void computePackagesMetric(SensorContext sensorContext, Set<ErlangPackage> packages) {
		for (ErlangPackage currentPackage : packages) {
			sensorContext.saveMeasure(currentPackage, CoreMetrics.PACKAGES, 1.0);
		}
	}

	@Override
	public String toString() {
		return getClass().getSimpleName();
	}
}