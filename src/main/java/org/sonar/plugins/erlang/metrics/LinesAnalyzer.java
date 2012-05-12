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

import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * This class implements the computation of basic line metrics for a
 * {@link ErlangaFile}.
 * 
 * @since 0.1
 */
public class LinesAnalyzer {
	

	private final List<String> lines;
	private int numberOfComments;
	private int numberOfBlankLines;
	private int numberOfDecoratorLines;
	
	public int getNumberOfComments() {
		return numberOfComments;
	}

	public int getNumberOfBlankLines() {
		return numberOfBlankLines;
	}

	public int getNumberOfDecoratorLines() {
		return numberOfDecoratorLines;
	}

	public LinesAnalyzer(List<String> lines) {
		this.lines = lines;
		this.countCommentedLines();
		this.countBlankLines();
	}

	public int countLines() {
		return lines.size();
	}

	public int getLinesOfCode() {
		return countLines() - numberOfBlankLines - numberOfComments - numberOfDecoratorLines;
	}

	private int countBlankLines() {
		numberOfBlankLines = 0;
		for (String line : lines) {
			if (StringUtils.isBlank(line)) {
				numberOfBlankLines++;
			}
		}
		return numberOfBlankLines;
	}

	/**
	 * Only count commented lines which has some text after the comment prefix
	 * so we don't count those line which were created for formatting reason like:
	 * %%-------------------------------------
	 * %%=====================================
	 * 
	 * @return
	 */
	private int countCommentedLines() {
		numberOfComments = 0;
		for (String line : lines) {
			if (line.trim().matches("%+ *[^-=]+")) {
				numberOfComments++;
			}
			if(line.trim().matches("%+ *[-=]+")){
				numberOfDecoratorLines++;
			}
		}
		return numberOfComments;
	}
	
}