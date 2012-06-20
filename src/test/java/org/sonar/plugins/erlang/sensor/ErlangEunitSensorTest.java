package org.sonar.plugins.erlang.sensor;

import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.FileReader;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.ObjectUtils;
import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.Metric;
import org.sonar.api.resources.InputFile;
import org.sonar.api.resources.InputFileUtils;
import org.sonar.api.resources.JavaFile;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.Qualifiers;
import org.sonar.api.resources.Scopes;
import org.sonar.api.test.IsMeasure;
import org.sonar.api.test.IsResource;
import org.sonar.plugins.erlang.language.Erlang;
import org.sonar.plugins.erlang.utils.ProjectUtil;

public class ErlangEunitSensorTest {

	private SensorContext context;

	@Before
	public void setup() throws URISyntaxException{
		context = ProjectUtil.mockContext();
		Configuration configuration = mock(Configuration.class);
		File fileToAnalyse = new File(getClass().getResource(
				"/org/sonar/plugins/erlang/erlcount/.eunit/TEST-erlcount_eunit.xml").toURI());
		InputFile inputFile = InputFileUtils.create(fileToAnalyse.getParentFile(), fileToAnalyse);
		
		File fileToAnalyse2 = new File(getClass().getResource(
				"/org/sonar/plugins/erlang/erlcount/.eunit/erlcount_eunit.erl").toURI());
		InputFile inputFile2 = InputFileUtils.create(fileToAnalyse2.getParentFile(), fileToAnalyse2);
		
		ArrayList<InputFile> inputFiles = new ArrayList<InputFile>();
		inputFiles.add(inputFile);
		inputFiles.add(inputFile2);
		new ErlangEunitSensor(new Erlang()).collect(ProjectUtil.getProject(inputFiles, configuration), context, new File(
				getClass().getResource("/org/sonar/plugins/erlang/erlcount/.eunit/").toURI()));
	}
	
	@Test
	public void shouldSaveErrorsAndFailuresInXML() throws URISyntaxException {
		
		
		// 1 classes, 6 measures by class
		verify(context, times(1)).saveMeasure(argThat(new IsResource(Scopes.FILE, Qualifiers.UNIT_TEST_FILE)),
				eq(CoreMetrics.SKIPPED_TESTS), anyDouble());

		verify(context, times(1)).saveMeasure(argThat(new IsResource(Scopes.FILE, Qualifiers.UNIT_TEST_FILE)),
				eq(CoreMetrics.TESTS), anyDouble());
		verify(context, times(6)).saveMeasure(argThat(new IsResource(Scopes.FILE, Qualifiers.UNIT_TEST_FILE)),
				(Metric) anyObject(), anyDouble());
		verify(context, times(1)).saveMeasure(argThat(new IsResource(Scopes.FILE, Qualifiers.UNIT_TEST_FILE)),
				argThat(new IsMeasure(CoreMetrics.TEST_DATA)));

		verify(context).saveMeasure(eq(new JavaFile("org.sonar.core.ExtensionsFinderTest", true)),
				argThat(getTestDetailsMatcher("shouldSaveErrorsAndFailuresInXML/expected-test-details.xml")));
	}

	private BaseMatcher<Measure> getTestDetailsMatcher(final String xmlBaseFile) {
		return new BaseMatcher<Measure>() {

			private Diff diff;

			public boolean matches(Object obj) {
				try {
					if (!ObjectUtils.equals(CoreMetrics.TEST_DATA, ((Measure) obj).getMetric())) {
						return false;
					}

					File expectedXML = new File(getClass().getResource(
							"/org/sonar/plugins/surefire/SurefireSensorTest/" + xmlBaseFile).toURI());
					XMLUnit.setIgnoreWhitespace(true);
					XMLUnit.setIgnoreDiffBetweenTextAndCDATA(true);
					XMLUnit.setNormalize(true);
					XMLUnit.setNormalizeWhitespace(true);
					diff = XMLUnit.compareXML(new FileReader(expectedXML), new StringReader(((Measure) obj).getData()));
					return diff.similar();
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}

			@Override
			public void describeTo(Description d) {
				DetailedDiff dd = new DetailedDiff(diff);
				d.appendText("XML differences in " + xmlBaseFile + ": " + dd.getAllDifferences());
			}
		};
	}

}
