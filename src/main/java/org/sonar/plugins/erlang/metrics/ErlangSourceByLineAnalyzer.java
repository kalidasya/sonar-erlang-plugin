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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.plugins.erlang.language.ErlangFunction;
import org.sonar.plugins.erlang.violations.dialyzer.ErlangDialyzer;

/**
 * This class contains all meause which can be done only by iterating over the
 * lines of the code. To avoid going through the source multiple times to count
 * different metrics all these calculations should be measured here.
 * 
 * @author kalidasya
 * 
 */

public class ErlangSourceByLineAnalyzer {

	public static final Pattern isCommentPatter = Pattern.compile("%+ *[^-=]+");
	public static final Pattern isDecoratorPatter = Pattern.compile("%+ *[-=]+");
	private static final String FUNCTION_ENDS_REGEX = ".*\\.$";
	private static final String FUNCTION_START_REGEX = "^[a-z]+[a-z0-9_@]+ *\\(.*?\\) *->";
	private static final Logger LOG = LoggerFactory.getLogger(ErlangDialyzer.class);

	private final List<String> lines;
	private List<ErlangFunction> functions = new ArrayList<ErlangFunction>();
	private int numberOfComments;
	private int numberOfBlankLines;
	private int numberOfDecoratorLines;

	public ErlangSourceByLineAnalyzer(List<String> lines) {
		super();
		this.lines = lines;
		analyze();
	}

	/**
	 * Only count commented lines which has some text after the comment prefix
	 * so we don't count those line which were created for formatting reason
	 * like: %%-------------------------------------
	 * %%=====================================
	 * 
	 * @return
	 */
	private void analyze() {

		boolean functionOpened = false;
		ErlangFunction latest = null;
		for (String line : lines) {
			if (StringUtils.isBlank(line)) {
				numberOfBlankLines++;
			}
			/**
			 * Only count commented lines which has some text after the comment
			 * prefix so we don't count those line which were created for
			 * formatting reason like: %%-------------------------------------
			 * %%=====================================
			 * 
			 * @return
			 */
			else if (isCommentPatter.matcher(line.trim()).matches()) {
				numberOfComments++;
			} else if (isDecoratorPatter.matcher(line.trim()).matches()) {
				numberOfDecoratorLines++;
			} else if (line.trim().matches(FUNCTION_START_REGEX) && !functionOpened) {
				functions.add(new ErlangFunction());
				latest = functions.get(functions.size() - 1);
				latest.addLine(line);
				functionOpened = true;
			} else {
				if (functionOpened) {
					latest.addLine(line);
				}
				if (line.trim().matches(FUNCTION_ENDS_REGEX) && functionOpened) {
					latest = null;
					functionOpened = false;
				}
			}
		}

	}

	public int getNumberOfComments() {
		return numberOfComments;
	}

	public int getNumberOfBlankLines() {
		return numberOfBlankLines;
	}

	public int getNumberOfDecoratorLines() {
		return numberOfDecoratorLines;
	}

	public int countLines() {
		return lines.size();
	}

	public int getLinesOfCode() {
		return countLines() - numberOfBlankLines - numberOfComments - numberOfDecoratorLines;
	}

	public double getNumberOfFunctions() {
		return functions.size();
	}
	
	public List<ErlangFunction> getFunctions() {
		return functions;
	}
}
