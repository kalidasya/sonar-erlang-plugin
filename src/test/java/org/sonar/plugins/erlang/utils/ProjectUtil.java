package org.sonar.plugins.erlang.utils;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.apache.commons.configuration.Configuration;
import org.sonar.api.resources.InputFile;
import org.sonar.api.resources.Language;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.ProjectFileSystem;
import org.sonar.plugins.erlang.ErlangPlugin;
import org.sonar.plugins.erlang.language.Erlang;

public class ProjectUtil {

	 public static Project getProject(InputFile inputFile, final Configuration configuration) throws URISyntaxException {
		    final ProjectFileSystem fileSystem = mock(ProjectFileSystem.class);
		    when(fileSystem.getSourceCharset()).thenReturn(Charset.defaultCharset());

		    final File folder = new File(ProjectUtil.class.getResource("/org/sonar/plugins/erlang/erlcount").toURI());
		    when(fileSystem.getBuildDir()).thenReturn(folder);
		    when(fileSystem.getBasedir()).thenReturn(folder);

		    ArrayList<InputFile> inputFiles = new ArrayList<InputFile>();
		    inputFiles.add(inputFile);
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
}
