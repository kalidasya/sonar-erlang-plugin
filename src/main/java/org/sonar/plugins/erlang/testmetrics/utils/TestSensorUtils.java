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
package org.sonar.plugins.erlang.testmetrics.utils;

import java.util.List;

import org.jfree.util.Log;
import org.sonar.api.resources.InputFile;

public final class TestSensorUtils {
	
	private TestSensorUtils(){}

	public static InputFile findFileForReport(List<InputFile> files, String name) {
		for (InputFile inputFile : files) {
			if(inputFile.getFile().getName().equals(name)){
				return inputFile;
			}
		}
		Log.error("Referenced resource file not found: " + name);
		return null;
	}

}
