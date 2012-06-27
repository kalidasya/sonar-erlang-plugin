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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.net.URISyntaxException;
import java.util.ArrayList;

import org.apache.commons.configuration.Configuration;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.measures.Measure;
import org.sonar.api.resources.InputFile;
import org.sonar.api.resources.Resource;
import org.sonar.plugins.erlang.language.Erlang;
import org.sonar.plugins.erlang.utils.ProjectUtil;

public class ErlangSourceImporterSensorTest {

	private SensorContext context;

	@Before
	public void setup() throws URISyntaxException {
		context = ProjectUtil.mockContext();
		Configuration configuration = mock(Configuration.class);
		ArrayList<InputFile> srcFiles = new ArrayList<InputFile>();
		ArrayList<InputFile> otherFiles = new ArrayList<InputFile>();
		srcFiles.add(ProjectUtil.getInputFileByPath("/org/sonar/plugins/erlang/erlcount/src/erlcount.erl"));
		otherFiles.add(ProjectUtil.getInputFileByPath("/org/sonar/plugins/erlang/erlcount/test/erlcount_eunit.erl"));
		new ErlangSourceImporterSensor(new Erlang()).analyse(ProjectUtil.getProject(srcFiles, otherFiles, configuration), context);
	}

	@Test
	public void shouldSaveErrorsAndFailuresInXML() throws URISyntaxException {
		ArgumentCaptor<String> argument2 = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<Measure> argumentMeasure = ArgumentCaptor.forClass(Measure.class);
		verify(context, times(2)).index((Resource) anyObject());
		verify(context, times(2)).saveSource((Resource) anyObject(), argument2.capture());
		assertThat(argument2.getAllValues().get(0), Matchers.startsWith("-module(erlcount)"));
		assertThat(argument2.getAllValues().get(1), Matchers.startsWith("-module(erlcount_eunit)"));
	}

}
