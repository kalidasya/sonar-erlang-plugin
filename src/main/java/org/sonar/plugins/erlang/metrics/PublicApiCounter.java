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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.sonar.plugins.erlang.utils.StringUtils;

public class PublicApiCounter {

	private static final Pattern exportPattern = Pattern.compile("(-export\\(\\[)(.*?)(\\]\\))\\.", Pattern.DOTALL
			+ Pattern.MULTILINE);

	public static List<Double> countPublicApi(String source) throws IOException {
		/**
		 * TODO handle the export_all function
		 */
		Matcher m = exportPattern.matcher(source);
		List<Double> ret = new ArrayList<Double>();
		int numOfPublicMethods = 0;
		int numOfUndocPublicMethods = 0;
		while (m.find()) {
			String exportedMethods = m.group(2);
			String[] publicMethods = exportedMethods.split(",");
			numOfPublicMethods += publicMethods.length;
			for (String method : publicMethods) {
				String methodName = method.split("\\/")[0].trim();
				Integer numOfVariables = Integer.valueOf(method.split("\\/")[1]);
				StringBuilder regEx = new StringBuilder(methodName + "\\(.*?");
				for (int i = 1; i < numOfVariables; i++) {
					regEx.append(",.*?");
				}
				regEx.append("\\).*");
				Matcher findMethodMatcher = Pattern.compile(regEx.toString(), Pattern.DOTALL + Pattern.MULTILINE)
						.matcher(source);
				findMethodMatcher.find();
				String beforeMethod = source.substring(0, findMethodMatcher.start());
				List<String> linesBefore = StringUtils.convertStringToListOfLines(beforeMethod);
				boolean isComment = true;
				for (ListIterator<String> iterator = linesBefore.listIterator(linesBefore.size()); iterator
						.hasPrevious();) {
					String line = iterator.previous();
					if (StringUtils.isBlank(line) || ErlangSourceByLineAnalyzer.isDecoratorPatter.matcher(line).matches()) {
						continue;
					}
					if (ErlangSourceByLineAnalyzer.isCommentPatter.matcher(line).matches()) {
						break;
					}
					isComment = false;
					break;
				}
				if(!isComment){
					numOfUndocPublicMethods++;
				}
			}

		}
		ret.add((double) numOfPublicMethods);
		ret.add((double) numOfUndocPublicMethods);
		return ret;
	}

}
