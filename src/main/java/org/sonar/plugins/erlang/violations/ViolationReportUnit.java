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
package org.sonar.plugins.erlang.violations;

public class ViolationReportUnit {

	private String moduleName;
	private Position position = new Position();
	private String methodSign;
	private String uri;
	private String metricKey;
	private String metricValue;
	private String metricMessage;
	private String repositoryKey;
	private String description;
	
	public ViolationReportUnit() {
		super();
	}
	
	public String getModuleFileName() {
		return moduleName+".erl";
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getMethodSign() {
		return methodSign;
	}

	public void setMethodSign(String methodSign) {
		this.methodSign = methodSign;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMetricKey() {
		return metricKey;
	}

	public void setMetricKey(String metricKey) {
		this.metricKey = metricKey;
	}

	public String getMetricValue() {
		return metricValue;
	}

	public void setMetricValue(String metricValue) {
		this.metricValue = metricValue;
	}

	public String getRepositoryKey() {
		return repositoryKey;
	}

	public void setRepositoryKey(String repositoryKey) {
		this.repositoryKey = repositoryKey;
	}

	public String getMetricMessage() {
		return metricMessage;
	}

	public void setMetricMessage(String metricMessage) {
		this.metricMessage = metricMessage;
	}

	public Position getPosition() {
		return position;
	}

	public void setStartRow(int startRow){
		position.setStartRow(startRow);
	}
	
	public void setStartCol(int startCol){
		position.setStartCol(startCol);
	}
	
	public void setEndRow(int endRow){
		position.setEndRow(endRow);
	}
	
	public void setEndCol(int endCol){
		position.setEndCol(endCol);
	}
	
	public int getStartRow(){
		return position.getStartRow();
	}
	
	public int getStartCol(){
		return position.getStartCol();
	}
	
	public int getEndRow(){
		return position.getEndRow();
	}
	
	public int getEndCol(){
		return position.getEndCol();
	}
	
	public void setPosition(String position) {
		String[] startEnd = position.split("-");
		String[] start = startEnd[0].split(",");
		String[] end = startEnd[1].split(",");
		setStartRow(Integer.valueOf(start[0]));
		if(start.length>1){
			setStartCol(Integer.valueOf(start[1]));
		} else {
			setStartCol(1);
		}
		setEndRow(Integer.valueOf(end[0]));
		if(end.length>1){
			setEndCol(Integer.valueOf(end[1]));	
		} else {
			setEndCol(1);
		}
		
	}

	
	private class Position{
		private int startCol;
		private int startRow;
		private int endCol;
		private int endRow;
		
		public Position() {
			super();
		}
		
		public int getStartCol() {
			return startCol;
		}

		public void setStartCol(int startCol) {
			this.startCol = startCol;
		}

		public int getStartRow() {
			return startRow;
		}

		public void setStartRow(int startRow) {
			this.startRow = startRow;
		}

		public int getEndCol() {
			return endCol;
		}

		public void setEndCol(int endCol) {
			this.endCol = endCol;
		}

		public int getEndRow() {
			return endRow;
		}

		public void setEndRow(int endRow) {
			this.endRow = endRow;
		}
		
		@Override
		public String toString(){
			return startRow+","+startCol+"-"+endRow+","+endCol;
		}
	}
	
	@Override
	public String toString(){
		return moduleName+":"+methodSign+" "+metricKey+" at line "+position.toString();
	}
	
}
