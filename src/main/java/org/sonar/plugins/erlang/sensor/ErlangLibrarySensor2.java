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
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.database.DatabaseSession;
import org.sonar.api.database.model.ResourceModel;
import org.sonar.api.database.model.Snapshot;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.Qualifiers;
import org.sonar.api.resources.Resource;
import org.sonar.api.resources.Scopes;
import org.sonar.plugins.erlang.ErlangPlugin;
import org.sonar.plugins.erlang.language.Erlang;

public class ErlangLibrarySensor2 extends AbstractErlangSensor{
	DatabaseSession session;

	public ErlangLibrarySensor2(Erlang erlang, DatabaseSession session) {
		super(erlang);
		this.session= session;
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
					String[] parts = dep.split(",");
					String key = parts[3].replaceFirst("(.*:)(.*?)(\\\")", "$2").replaceAll("[\\\\/]", ":").replaceAll("\\.git", "");
				/*	Library lib = new Library(name,version);
					lib.setName(name);
					lib.setEffectiveKey(key);
					context.index(lib);
					Project depProject = new Project(key);
					depProject.setLanguage(erlang);
					depProject.setName(name);
					depProject = session.save(depProject);
					*/
					ResourceModel depP = new ResourceModel(Scopes.PROJECT, key, Qualifiers.LIBRARY, null, name);
					depP.setLanguageKey(ErlangPlugin.LANG_KEY);
					List res = session.createQuery("select id from ResourceModel WHERE KEE ='"+key+"'").getResultList();
					if(res.size()==0){
						depP = session.save(depP);
					} else {
						depP.setId((Integer) res.get(0));
					}
					Snapshot snapshot = new Snapshot();
					snapshot.setParentId(null);
					snapshot.setRootId(null);
					snapshot.setVersion(version);
					snapshot.setQualifier(Qualifiers.LIBRARY);
					snapshot.setScope(Scopes.PROJECT);
					snapshot.setCreatedAt(project.getAnalysisDate());
					snapshot.setBuildDate(project.getAnalysisDate());
					snapshot.setDepth(0);
					snapshot.setRootProjectId(project.getId());
					snapshot.setResourceId(depP.getId());
					snapshot.save(session);
					//projectDefinition.addLibrary(name + "-" + version);
				}
			}
		} catch (FileNotFoundException e) {
			LOG.error("Cannot open file: " + rebarConfigUrl + e);
		} catch (IOException e) {
			LOG.error("Cannot open file: " + rebarConfigUrl + e);
		}
		
		LOG.debug("Libraries added: "+context);
	}
}
