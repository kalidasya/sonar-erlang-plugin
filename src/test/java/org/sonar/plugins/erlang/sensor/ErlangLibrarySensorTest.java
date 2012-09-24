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
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.meta.When;

import org.apache.commons.configuration.Configuration;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.design.Dependency;
import org.sonar.api.resources.InputFile;
import org.sonar.api.resources.Library;
import org.sonar.api.rules.Violation;
import org.sonar.plugins.erlang.language.Erlang;
import org.sonar.plugins.erlang.utils.ProjectUtil;

public class ErlangLibrarySensorTest {

	private SensorContext context;

	@Before
	public void setup() throws URISyntaxException {
		context = ProjectUtil.mockContext();
		Configuration configuration = mock(Configuration.class);
		ArrayList<InputFile> srcFiles = new ArrayList<InputFile>();
		ArrayList<InputFile> otherFiles = new ArrayList<InputFile>();
		when(context.getResource(any(Library.class))).thenAnswer(new Answer<Library>() {
			public Library answer(InvocationOnMock invocation) {
		         Object[] args = invocation.getArguments();         
		         return (Library) args[0];
		     }
		 });
		srcFiles.add(ProjectUtil.getInputFileByPath("/org/sonar/plugins/erlang/erlcount/rebar.config"));
		new ErlangLibrarySensor(new Erlang()).analyse(ProjectUtil.getProject(srcFiles, otherFiles, configuration), context);
	}

	@Test
	public void erlangLibrariesTest() throws URISyntaxException {
		ArgumentCaptor<Dependency> argument = ArgumentCaptor.forClass(Dependency.class);
		verify(context, times(5)).saveDependency(argument.capture());
		List<Dependency> capturedDependencies = argument.getAllValues();
		assertThat(((Library)capturedDependencies.get(0).getTo()).getKey(),
				Matchers.equalTo("fake:elibs"));
		assertThat(((Library)capturedDependencies.get(1).getTo()).getKey(),
				Matchers.equalTo("kake:estat"));
		assertThat(((Library)capturedDependencies.get(2).getTo()).getKey(),
				Matchers.equalTo("lake:malle"));
		assertThat(((Library)capturedDependencies.get(3).getTo()).getKey(),
				Matchers.equalTo("hola:moke_ads"));
		assertThat(((Library)capturedDependencies.get(4).getTo()).getKey(),
				Matchers.equalTo("malna:eper"));
		
		assertThat(((Library)capturedDependencies.get(0).getTo()).getName(),
				Matchers.equalTo("elibs"));
		assertThat(((Library)capturedDependencies.get(1).getTo()).getName(),
				Matchers.equalTo("estat"));
		assertThat(((Library)capturedDependencies.get(2).getTo()).getName(),
				Matchers.equalTo("malle"));
		assertThat(((Library)capturedDependencies.get(3).getTo()).getName(),
				Matchers.equalTo("moke_ads"));
		assertThat(((Library)capturedDependencies.get(4).getTo()).getName(),
				Matchers.equalTo("eper"));
		
		assertThat(((Library)capturedDependencies.get(0).getTo()).getVersion(),
				Matchers.equalTo("1.1.0"));
		assertThat(((Library)capturedDependencies.get(1).getTo()).getVersion(),
				Matchers.equalTo("0.0.1"));
		assertThat(((Library)capturedDependencies.get(2).getTo()).getVersion(),
				Matchers.equalTo("0.7.2-0"));
		assertThat(((Library)capturedDependencies.get(3).getTo()).getVersion(),
				Matchers.equalTo("19840221-3"));
		assertThat(((Library)capturedDependencies.get(4).getTo()).getVersion(),
				Matchers.equalTo("HEAD"));
	}

}
