package org.sonar.plugins.erlang.metrics;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.sonar.plugins.erlang.utils.StringUtils;
import static org.junit.Assert.assertThat;

public class LinesAnalyzerTest {

	private LinesAnalyzer la;

	@Before
	public void setup() throws IOException, URISyntaxException{
		File fileToAnalyse =  new File(getClass().getResource("/org/sonar/plugins/erlang/erlcount/src/erlcount_sup.erl")
				.toURI());
		String source = FileUtils.readFileToString(fileToAnalyse, "UTF-8");
		List<String> lines = StringUtils.convertStringToListOfLines(source);
		la = new LinesAnalyzer(lines);
	}
	
	@Test
	public void checkLinesAnalyzer(){
		assertThat(la.getNumberOfComments(), Matchers.equalTo(3));
		assertThat(la.countLines(), Matchers.equalTo(22));
		assertThat(la.getLinesOfCode(), Matchers.equalTo(15));
	}
	
}
