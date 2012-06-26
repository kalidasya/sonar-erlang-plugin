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
package org.sonar.plugins.erlang;

import java.util.ArrayList;
import java.util.List;

import org.sonar.api.Extension;
import org.sonar.api.Properties;
import org.sonar.api.Property;
import org.sonar.api.SonarPlugin;
import org.sonar.plugins.erlang.colorization.ErlangColorizerFormat;
import org.sonar.plugins.erlang.cpd.ErlangCpdMapping;
import org.sonar.plugins.erlang.language.Erlang;
import org.sonar.plugins.erlang.sensor.BaseMetricsSensor;
import org.sonar.plugins.erlang.sensor.ErlangCoverageSensor;
import org.sonar.plugins.erlang.sensor.ErlangEunitSensor;
import org.sonar.plugins.erlang.sensor.ErlangLibrarySensor;
import org.sonar.plugins.erlang.sensor.ErlangSourceImporterSensor;
import org.sonar.plugins.erlang.sensor.ViolationSensor;
import org.sonar.plugins.erlang.violations.ErlangDefaultProfile;
import org.sonar.plugins.erlang.violations.ErlangRuleRepository;
import org.sonar.plugins.erlang.violations.dialyzer.DialyzerRuleRepository;
import org.sonar.plugins.erlang.violations.refactorerl.RefactorErlRuleRepository;

@Properties({
		@Property(key = ErlangPlugin.FILE_SUFFIXES_KEY, defaultValue = ErlangPlugin.FILE_SUFFIXES_DEFVALUE, name = "File suffixes", description = "Comma-separated list of suffixes for files to analyze. To not filter, leave the list empty.", global = true, project = true),

		@Property(key = ErlangPlugin.EUNIT_FOLDER_KEY, defaultValue = ErlangPlugin.EUNIT_DEFAULT_FOLDER, name = "Eunit Default Output Folder", description = "Folder where Eunit unit test and code coverage reports are located", global = true, project = true),

		@Property(key = ErlangPlugin.DIALYZER_FILENAME_KEY, defaultValue = ErlangPlugin.DIALYZER_DEFAULT_FILENAME, name = "Dialyzer Default Filename", description = "Filename of the dialyzer output located in the eunit folder", global = true, project = true),
		
		@Property(key = ErlangPlugin.REFACTORERL_DEFAULT_FILENAME_PATTERN_KEY, defaultValue = ErlangPlugin.REFACTORERL_DEFAULT_FILENAME_PATTERN, name = "RefactorErl default report pattern", description = "Filename patter to the RefactorErl output files", global = true, project = true)

})
public class ErlangPlugin extends SonarPlugin {

	public static final String EUNIT_FOLDER_KEY = "sonar.erlang.eunit.repotsfolder";
	public static final String EUNIT_DEFAULT_FOLDER = ".eunit/";
	public static final String DIALYZER_FILENAME_KEY = "sonar.erlang.dialyzer.filename";
	
	public static final String DIALYZER_DEFAULT_FILENAME = EUNIT_DEFAULT_FOLDER + "dialyzer.log";
	public static final String REFACTORERL_DEFAULT_FILENAME_PATTERN_KEY = "sonar.erlang.refactorerl.filename";
	public static final String REFACTORERL_DEFAULT_FILENAME_PATTERN = "RE-.*\\.txt";
	public static final String NAME = "Erlang";
	public static final String LANG_KEY = "erl";
	public static final String EXTENSION = "."+LANG_KEY;
	public static final String FILE_SUFFIXES_KEY = "sonar.erlang.file.suffixes";
	public static final String FILE_SUFFIXES_DEFVALUE = LANG_KEY;
	public static final String REBAR_CONFIG_URL = "rebar.config";

	/**
	 * Main entry point, here are all the extensions what this plugin can execute...
	 * There are server and client side extensions 
	 */
	
	public List getExtensions() {
		final List<Class<? extends Extension>> extensions = new ArrayList<Class<? extends Extension>>();
		extensions.add(Erlang.class);
		extensions.add(ErlangSourceImporterSensor.class);
		extensions.add(ErlangColorizerFormat.class);
		extensions.add(BaseMetricsSensor.class);
//		extensions.add(DialyzerRuleRepository.class);
//		extensions.add(RefactorErlRuleRepository.class);
		extensions.add(ErlangRuleRepository.class);
		extensions.add(ErlangDefaultProfile.class);
		extensions.add(ViolationSensor.class);
		extensions.add(ErlangCpdMapping.class);
		extensions.add(ErlangEunitSensor.class);
		extensions.add(ErlangCoverageSensor.class);
		extensions.add(ErlangLibrarySensor.class);
		return extensions;
	}

}
