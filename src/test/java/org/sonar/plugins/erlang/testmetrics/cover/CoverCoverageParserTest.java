package org.sonar.plugins.erlang.testmetrics.cover;

import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

public class CoverCoverageParserTest {

	private File fileToAnalyse;
	private CoverCoverageParser parser;

	@Before
	public void setup() throws IOException, URISyntaxException {
		fileToAnalyse = new File(getClass().getResource("/org/sonar/plugins/erlang/erlcount/.eunit/erlcount_lib.COVER.html")
				.toURI());
		parser = new CoverCoverageParser();
	}

	@Test
	public void checkLinesAnalyzer(){
		CoverFileCoverage result = parser.parseFile(fileToAnalyse, fileToAnalyse.getParent(), "erlcount_lib.COVER.html");
		assertThat(result.getCoveredLines(), Matchers.equalTo(19));
		assertThat(result.getUncoveredLines(), Matchers.equalTo(1));
		assertThat(result.getLinesToCover(), Matchers.equalTo(20));
	}
}
