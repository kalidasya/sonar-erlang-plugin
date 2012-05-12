package org.sonar.plugins.erlang.metrics;

import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.commons.io.FileUtils;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

public class PublicApiCounterTest {

	private LinesAnalyzer la;
	private String source;

	@Before
	public void setup() throws IOException, URISyntaxException{
		File fileToAnalyse =  new File(getClass().getResource("/org/sonar/plugins/erlang/erlcount/src/erlcount_counter.erl")
				.toURI());
		source = FileUtils.readFileToString(fileToAnalyse, "UTF-8");
	}
	
	@Test
	public void checkLinesAnalyzer(){
		assertThat(PublicApiCounter.countPublicApi(source), Matchers.equalTo(7D));
	}
	
}
