package org.sonar.plugins.erlang.language;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.configuration.Configuration;
import org.junit.Test;
import org.sonar.plugins.erlang.ErlangPlugin;

public class ErlangTest {

	@Test
	  public void testGetFileSuffixes() {
	    Configuration configuration = mock(Configuration.class);
	    Erlang erlang = new Erlang(configuration);
	    erlang.setConfiguration(configuration);

	    when(configuration.getStringArray(ErlangPlugin.FILE_SUFFIXES_KEY)).thenReturn(null);

	    assertArrayEquals(erlang.getFileSuffixes(), new String[] { "erl" });
	    assertSame(configuration, erlang.getConfiguration());
	  }
}
