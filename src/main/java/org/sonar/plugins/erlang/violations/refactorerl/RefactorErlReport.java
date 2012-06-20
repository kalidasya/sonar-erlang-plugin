package org.sonar.plugins.erlang.violations.refactorerl;

import java.util.ArrayList;
import java.util.List;

public class RefactorErlReport {

	private List<RefactorErlReportUnit> units = new ArrayList<RefactorErlReportUnit>();

	public RefactorErlReport() {
		super();
	}

	public List<RefactorErlReportUnit> getUnits() {
		return units;
	}

	public RefactorErlReportUnit addUnit(RefactorErlReportUnit unit) {
		this.units.add(unit);
		return this.units.get(this.units.size()-1);
	}
	
	public RefactorErlReportUnit createUnit() {
		return addUnit(new RefactorErlReportUnit());
	}

	public List<RefactorErlReportUnit> getUnitsByModuleName(String moduleName) {
		List<RefactorErlReportUnit> ret = new ArrayList<RefactorErlReportUnit>();
		for (RefactorErlReportUnit unit : this.units) {
			if(unit.getModuleName().equals(moduleName)){
				ret.add(unit);
			}
		}
		return ret;
	}
	
	
	
}
