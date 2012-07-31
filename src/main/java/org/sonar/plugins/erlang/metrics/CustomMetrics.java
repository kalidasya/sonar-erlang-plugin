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

import java.lang.reflect.Field;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Metric;
import org.sonar.api.measures.Metrics;
import org.sonar.api.utils.SonarException;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public class CustomMetrics implements Metrics {
	private static final String DOMAIN = "Domain";

	public static final String IMPORTED_MODULES_KEY = "imported_modules";
	public static final Metric IMPORTED_MODULES = new Metric.Builder(IMPORTED_MODULES_KEY,
			"Number of imported modules", Metric.ValueType.INT)
			.setDescription(
					"The domain of the query is a module. This metric gives the number of imported modules used in a concrete module. "
							+ "The metric does not contain the number of qualified calls (calls that have the following form: module:function).")
			.setDirection(Metric.DIRECTION_WORST).setQualitative(true).setDomain(DOMAIN)
			.setUserManaged(false).create();

	public static final String CALL_FOR_FUNCTION_KEY = "calls_for_function";
	public static final Metric CALL_FOR_FUNCTION = new Metric.Builder(CALL_FOR_FUNCTION_KEY,
			"Number of calls for a function", Metric.ValueType.INT)
			.setDescription(
					"The domain of the query is a function. This metric gives the number of calls for a concrete function. It is not" +
					" equivalent with the number of other functions calling the function, because all of these other functions can refer " +
					"to the measured one more than once.")
			.setDirection(Metric.DIRECTION_WORST).setQualitative(true).setDomain(DOMAIN)
			.setUserManaged(false).create();

	private static final List<Metric> METRICS;

	static {
		METRICS = Lists.newLinkedList();
		for (Field field : CustomMetrics.class.getFields()) {
			if (Metric.class.isAssignableFrom(field.getType())) {
				try {
					Metric metric = (Metric) field.get(null);
					METRICS.add(metric);
				} catch (IllegalAccessException e) {
					throw new SonarException("can not introspect " + CustomMetrics.class
							+ " to get metrics", e);
				}
			}
		}
	}

	@Override
	public List<Metric> getMetrics() {
		return METRICS;

	}

	public static Metric getMetricByKey(String metricKey) {
		for (Metric metric : METRICS) {
			if (metric.getKey().equals(metricKey)) {
				return metric;
			}
		}
		return null;
	}
}
