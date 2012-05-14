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
package org.sonar.plugins.erlang.metrics;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.bootstrap.ProjectBuilder;
import org.sonar.api.batch.bootstrap.ProjectDefinition;
import org.sonar.api.batch.bootstrap.ProjectReactor;
import org.sonar.plugins.erlang.language.Erlang;
import org.sonar.plugins.erlang.sensor.ErlangLibrarySensor;

public class ErlangProjectBuilder extends ProjectBuilder{

	private final static Logger LOG = LoggerFactory.getLogger(ErlangLibrarySensor.class);

	public ErlangProjectBuilder(ProjectReactor reactor) {
		super(reactor);
	}


	@Override
	protected void build(ProjectReactor reactor) {
		ProjectDefinition projectDefinition = reactor.getRoot();
		File rebarConfig = new File(projectDefinition.getBaseDir()+File.separator+"rebar.config");
		projectDefinition.addLibrary("");
		try {
			String rebarConfigContent = FileUtils.readFileToString(rebarConfig, "UTF-8");
			Matcher m = Pattern.compile("\\{deps, ?\\[.*?\\]\\}\\.", Pattern.DOTALL + Pattern.MULTILINE).matcher(
					rebarConfigContent);
			Pattern p2 = Pattern.compile("\\{[^\\[]+?\\}", Pattern.DOTALL + Pattern.MULTILINE);
			while (m.find()) {
				String exportedMethods = rebarConfigContent.substring(m.start(), m.end() - 1);
				Matcher deps = p2.matcher(exportedMethods);
				while(deps.find()){
					String dep = exportedMethods.substring(deps.start(),deps.end());
					String name = dep.replaceFirst("(^\\{)([A-Za-z_]*?)(\\,.*)", "$2");
					String version = dep.replaceFirst("(.*tag.*?\\\")(.*?)(\\\".*)", "$2");
					projectDefinition.addLibrary(name+"-"+version);
				}
			}
		} catch (FileNotFoundException e) {
			LOG.error("Cannot open file: " + rebarConfig.getAbsolutePath() + e);
		} catch (IOException e) {
			LOG.error("Cannot open file: " + rebarConfig.getAbsolutePath() + e);
		} 
		projectDefinition.getProperties();
	}

}
