package org.sonar.plugins.erlang.violations.refactorerl;

import java.util.ArrayList;
import java.util.List;

public class RefactorErlReportUnit {

	private String moduleName;
	private String methodSign;
	private String uri;
	private Position position = new Position();
	private List<RefactorErlMetric> metrics = new ArrayList<RefactorErlMetric>();
	
	public RefactorErlReportUnit() {
		super();
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
	
	
	public List<RefactorErlMetric> getMetrics() {
		return metrics;
	}

	public RefactorErlMetric addMetric(RefactorErlMetric metric){
		this.metrics.add(metric);
		return this.metrics.get(this.metrics.size()-1);
	}
	
	public RefactorErlMetric createMetric() {
		return addMetric(new RefactorErlMetric());
	}

	public void setPosition(String position) {
		String[] startEnd = position.split("-");
		setStartRow(Integer.valueOf(startEnd[0].split(",")[0]));
		setStartCol(Integer.valueOf(startEnd[0].split(",")[1]));
		setEndRow(Integer.valueOf(startEnd[1].split(",")[0]));
		setEndCol(Integer.valueOf(startEnd[1].split(",")[1]));
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
		
		
	}
	
}
