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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.BatchComponent;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.SonarIndex;
import org.sonar.api.batch.bootstrap.ProjectDefinition;
import org.sonar.api.resources.Project;
import org.sonar.plugins.erlang.language.Erlang;

public class ErlangLibrarySensor2 implements Sensor {

	private SonarIndex index;

	public ErlangLibrarySensor2(SonarIndex index) {
		this.index = index;
	}

	private final static Logger LOG = LoggerFactory.getLogger(ErlangLibrarySensor2.class);

	@Override
	public void analyse(Project project, SensorContext context) {
		String rebarConfigUrl = ((Erlang) project.getLanguage()).getRebarConfigUrl();
		File rebarConfigFile = new File(project.getFileSystem().getBasedir(), rebarConfigUrl);
		try {
			String rebarConfigContent = FileUtils.readFileToString(rebarConfigFile, project.getFileSystem()
					.getSourceCharset().name());
			Matcher m = Pattern.compile("\\{deps, ?\\[.*?\\]\\}\\.", Pattern.DOTALL + Pattern.MULTILINE).matcher(
					rebarConfigContent);
			Pattern p2 = Pattern.compile("\\{[^\\[]+?\\}", Pattern.DOTALL + Pattern.MULTILINE);
			while (m.find()) {
				String exportedMethods = rebarConfigContent.substring(m.start(), m.end() - 1);
				Matcher deps = p2.matcher(exportedMethods);
				while (deps.find()) {
					String dep = exportedMethods.substring(deps.start(), deps.end());
					String name = dep.replaceFirst("(^\\{)([A-Za-z_]*?)(\\,.*)", "$2");
					String version = dep.replaceFirst("(.*tag.*?\\\")(.*?)(\\\".*)", "$2");
					//projectDefinition.addLibrary(name + "-" + version);
				}
			}
		} catch (FileNotFoundException e) {
			LOG.error("Cannot open file: " + rebarConfigUrl + e);
		} catch (IOException e) {
			LOG.error("Cannot open file: " + rebarConfigUrl + e);
		}
	}

	@Override
	public boolean shouldExecuteOnProject(Project project) {
		return true;
	}

}
