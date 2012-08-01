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

public class Report {
	
	private Double tests;
	private Double skipped;
	private Double errors;
	private Double failures;
	private Double durationMilliseconds;
	private TestSuite testSuite;
	public Report() {
		super();
	}
	public Double getTests() {
		return tests;
	}
	public void setTests(Double tests) {
		this.tests = tests;
	}
	public Double getSkipped() {
		return skipped;
	}
	public void setSkipped(Double skipped) {
		this.skipped = skipped;
	}
	public Double getErrors() {
		return errors;
	}
	public void setErrors(Double errors) {
		this.errors = errors;
	}
	public Double getFailures() {
		return failures;
	}
	public void setFailures(Double failures) {
		this.failures = failures;
	}
	public Double getDurationMilliseconds() {
		return durationMilliseconds;
	}
	public void setDurationMilliseconds(Double durationMilliseconds) {
		this.durationMilliseconds = durationMilliseconds;
	}
	public void setTestSuite(TestSuite testSuite) {
		this.testSuite = testSuite;
	}
	public TestSuite getTestSuite() {
		return testSuite;
	}
	
	

}
