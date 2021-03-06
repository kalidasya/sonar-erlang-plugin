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
package org.sonar.plugins.erlang.testmetrics.eunit;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestSuite {

	public static final String ISO8601_DATETIME_PATTERN
   = "yyyy-MM-dd'T'HH:mm:ss";
	
	private Integer tests;
	private Integer failures;
	private Integer errors;
	private Integer skipped;
	private Double time;
	private String name;
	
	private List<TestCase> testCases = new ArrayList<TestCase>();

	public TestSuite() {
		super();
	}

	public Integer getTests() {
		return tests;
	}

	public void setTests(Integer tests) {
		this.tests = tests;
	}

	public Integer getFailures() {
		return failures;
	}

	public void setFailures(Integer failures) {
		this.failures = failures;
	}

	public Integer getErrors() {
		return errors;
	}

	public void setErrors(Integer errors) {
		this.errors = errors;
	}

	public Integer getSkipped() {
		return skipped;
	}

	public void setSkipped(Integer skipped) {
		this.skipped = skipped;
	}

	public Double getTime() {
		return time;
	}

	public void setTime(Double time) {
		this.time = time;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<TestCase> getTestCases() {
		return testCases;
	}

	public void setTestCases(List<TestCase> testCases) {
		this.testCases = testCases;
	}

	public void addTestCase(TestCase testCase) {
		testCases.add(testCase);
		
	}
	
	public String toXml() {
	    StringBuilder sb = new StringBuilder(256);
	    sb.append("<testsuites><testsuite");
	    sb.append(" name=\""+getName()+"\"");
	    sb.append(" timestamp=\""+new SimpleDateFormat(ISO8601_DATETIME_PATTERN).format(new Date())+"\"");
	    sb.append(" hostname=\"localhost\"");
	    sb.append(" tests=\""+getTests()+"\"");
	    sb.append(" failures=\""+getFailures()+"\"");
	    sb.append(" errors=\""+getErrors()+"\"");
	    sb.append(" time=\""+getTime()+"\"");
	    sb.append(" package=\"\"");
	    sb.append(" id=\"0\"");
	    sb.append("><properties />");
	    for (TestCase result : testCases) {
	      result.appendXml(sb);
	    }
	    sb.append("<system-out><![CDATA[]]></system-out><system-err><![CDATA[]]></system-err></testsuite></testsuites>");
	    return sb.toString();
	  }
	
	
}
