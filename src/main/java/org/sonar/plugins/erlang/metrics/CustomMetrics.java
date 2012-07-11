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

import org.sonar.api.measures.Metric;
import org.sonar.api.measures.Metrics;

import com.google.common.collect.ImmutableList;

public class CustomMetrics implements Metrics {
	private static final String DOMAIN = "DOMAIN_DESIGN";
	private static final ImmutableList<Metric> METRICS = ImmutableList.of(new Metric.Builder("re_imported_modules", "Number of imported modules",
			Metric.ValueType.INT).setDirection(Metric.DIRECTION_WORST).setQualitative(true).setDomain(DOMAIN)
			.setUserManaged(false).create(),
			new Metric.Builder("re_calls_for_function", "Number of calls for a function",
					Metric.ValueType.INT).setDirection(Metric.DIRECTION_WORST).setQualitative(true).setDomain(DOMAIN)
					.setUserManaged(false).create());

	@Override
	public List<Metric> getMetrics() {
		return METRICS;
		
		 
	}

	public static Metric getMetricByKey(String metricKey) {
		for (Metric metric : METRICS) {
			if(metric.getKey().equals(metricKey)){
				return metric;
			}
		}
		return null;
	}
}
