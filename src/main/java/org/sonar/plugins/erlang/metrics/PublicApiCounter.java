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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PublicApiCounter {

	public static double countPublicApi(String source) {
		Matcher m = Pattern.compile("(.*-export\\(\\[)(.*?)(\\]\\).*)", Pattern.DOTALL + Pattern.MULTILINE).matcher(
				source);
		if (m.matches()) {
			String exportedMethods = m.replaceAll("$2");
			System.out.println(exportedMethods + " " + exportedMethods.split(",").length);
			return exportedMethods.split(",").length;
		}
		return 0;
	}

}
