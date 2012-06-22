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

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.apache.commons.configuration.Configuration;
import org.junit.Before;
import org.junit.Test;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.resources.InputFile;
import org.sonar.api.resources.InputFileUtils;
import org.sonar.api.resources.Resource;
import org.sonar.plugins.erlang.language.Erlang;
import org.sonar.plugins.erlang.utils.ProjectUtil;

public class ErlangEunitSensorTest {

	private SensorContext context;

	@Before
	public void setup() throws URISyntaxException {
		context = ProjectUtil.mockContext();
		Configuration configuration = mock(Configuration.class);
		File fileToAnalyse = new File(getClass().getResource(
				"/org/sonar/plugins/erlang/erlcount/.eunit/TEST-erlcount_eunit.xml").toURI());
		InputFile inputFile = InputFileUtils.create(fileToAnalyse.getParentFile(), fileToAnalyse);

		File fileToAnalyse2 = new File(getClass().getResource(
				"/org/sonar/plugins/erlang/erlcount/.eunit/erlcount_eunit.erl").toURI());
		InputFile inputFile2 = InputFileUtils.create(fileToAnalyse2.getParentFile(), fileToAnalyse2);

		ArrayList<InputFile> inputFiles = new ArrayList<InputFile>();
		inputFiles.add(inputFile);
		inputFiles.add(inputFile2);
		new ErlangEunitSensor(new Erlang()).collect(ProjectUtil.getProject(inputFiles, configuration), context, new File(
				getClass().getResource("/org/sonar/plugins/erlang/erlcount/.eunit/").toURI()));
	}

	@Test
	public void shouldSaveErrorsAndFailuresInXML() throws URISyntaxException {
		verify(context).saveMeasure((Resource) anyObject(), eq(CoreMetrics.TESTS), eq(7.0));
		verify(context).saveMeasure((Resource) anyObject(), eq(CoreMetrics.SKIPPED_TESTS), eq(0.0));
		verify(context).saveMeasure((Resource) anyObject(), eq(CoreMetrics.TEST_ERRORS), eq(0.0));
		verify(context).saveMeasure((Resource) anyObject(), eq(CoreMetrics.TEST_FAILURES), eq(1.0));
		verify(context).saveMeasure((Resource) anyObject(), eq(CoreMetrics.TEST_EXECUTION_TIME), eq(0.023));
		verify(context).saveMeasure((Resource) anyObject(), eq(CoreMetrics.TEST_SUCCESS_DENSITY), eq(85.71));
	}

}
