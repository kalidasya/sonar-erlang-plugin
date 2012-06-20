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
package org.sonar.plugins.erlang.utils;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.apache.commons.configuration.Configuration;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.resources.InputFile;
import org.sonar.api.resources.Language;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.ProjectFileSystem;
import org.sonar.api.resources.Resource;
import org.sonar.plugins.erlang.ErlangPlugin;
import org.sonar.plugins.erlang.language.Erlang;

public class ProjectUtil {

	 public static Project getProject(ArrayList<InputFile> inputFiles, final Configuration configuration) throws URISyntaxException {
		    final ProjectFileSystem fileSystem = mock(ProjectFileSystem.class);
		    when(fileSystem.getSourceCharset()).thenReturn(Charset.defaultCharset());

		    final File folder = new File(ProjectUtil.class.getResource("/org/sonar/plugins/erlang/erlcount").toURI());
		    when(fileSystem.getBuildDir()).thenReturn(folder);
		    when(fileSystem.getBasedir()).thenReturn(folder);
		    when(fileSystem.testFiles(any(String.class))).thenReturn(inputFiles);

		    when(fileSystem.mainFiles(ErlangPlugin.LANG_KEY)).thenReturn(inputFiles);

		    Project project = new Project("dummy") {

		      public ProjectFileSystem getFileSystem() {
		        return fileSystem;
		      }

		      public Language getLanguage() {
		        return new Erlang(configuration);
		      }
		    };

		    return project;

		  }
	 
	 public static SensorContext mockContext() {
	    SensorContext context = mock(SensorContext.class);
	    when(context.isIndexed(any(Resource.class), eq(false))).thenReturn(true);
	    return context;
	  }
}
