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

import java.util.ArrayList;
import java.util.List;


public class ViolationReport {

	private List<ViolationReportUnit> units = new ArrayList<ViolationReportUnit>();

	public ViolationReport() {
		super();
	}

	public List<ViolationReportUnit> getUnits() {
		return units;
	}
	
	public void setUnits(List<ViolationReportUnit> units) {
		this.units = units;
	}

	public ViolationReportUnit addUnit(ViolationReportUnit unit) {
		this.units.add(unit);
		return this.units.get(this.units.size()-1);
	}
	
	public ViolationReportUnit createUnit() {
		return addUnit(new ViolationReportUnit());
	}

	public List<ViolationReportUnit> getUnitsByModuleName(String moduleName) {
		List<ViolationReportUnit> ret = new ArrayList<ViolationReportUnit>();
		for (ViolationReportUnit unit : this.units) {
			if(unit.getModuleName().equals(moduleName)){
				ret.add(unit);
			}
		}
		return ret;
	}

	public void appendUnits(List<ViolationReportUnit> addUnits) {
		this.units.addAll(addUnits);
		
	}

	public List<ViolationReportUnit> getUnitsByMetricKey(String mcCabeKey) {
		List<ViolationReportUnit> ret = new ArrayList<ViolationReportUnit>();
		for (ViolationReportUnit unit : this.units) {
			if(unit.getMetricKey().equals(mcCabeKey)){
				ret.add(unit);
			}
		}
		return ret;
	}

	public static List<ViolationReportUnit> filterUnitsByModuleName(List<ViolationReportUnit> units, String moduleName){
		List<ViolationReportUnit> ret = new ArrayList<ViolationReportUnit>();
		for (ViolationReportUnit unit : units) {
			if(unit.getModuleName().equals(moduleName)){
				ret.add(unit);
			}
		}
		return ret;
	}
	
}
