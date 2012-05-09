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
import org.sonar.api.SonarPlugin;
import org.sonar.plugins.erlang.colorization.ErlangColorizerFormat;
import org.sonar.plugins.erlang.cpd.ErlangCpdMapping;
import org.sonar.plugins.erlang.dialyzer.DialyzerRuleRepository;
import org.sonar.plugins.erlang.dialyzer.DialyzerSensor;
import org.sonar.plugins.erlang.dialyzer.ErlangDefaultProfile;
import org.sonar.plugins.erlang.language.Erlang;
import org.sonar.plugins.erlang.sensor.BaseMetricsSensor;
import org.sonar.plugins.erlang.sensor.ErlangSourceImporterSensor;

public class ErlangPlugin extends SonarPlugin{

	public List getExtensions() {
		final List<Class<? extends Extension>> extensions = new ArrayList<Class<? extends Extension>>();
	    extensions.add(Erlang.class);
	    extensions.add(ErlangSourceImporterSensor.class);
	    extensions.add(ErlangColorizerFormat.class);
	    extensions.add(BaseMetricsSensor.class);
	    extensions.add(DialyzerRuleRepository.class);
	    extensions.add(ErlangDefaultProfile.class);
	    extensions.add(DialyzerSensor.class);
	    extensions.add(ErlangCpdMapping.class);
	    
	    return extensions;
	}

}
