package net.infopeers.restrant.engine.parser;

import static org.junit.Assert.*;
import net.infopeers.restrant.TestParams;
import net.infopeers.restrant.engine.PrefixedPlaceholderFormatter;

import org.junit.Test;

public class BasicUrlParserTest {

	@Test
	public void senario() throws Exception {

		PrefixedPlaceholderFormatter phFormatter = new PrefixedPlaceholderFormatter();
		String pathFormat = "/test/testa/myaction?to=:TO";

		BasicUrlParser bp = new BasicUrlParser(pathFormat, phFormatter);
		bp.controller("TestClass").action("go").onGet();

		TestParams params = new TestParams();
		params.addParams("to", "REST");
		assertTrue(bp.parse(params, "/test/testa/myaction"));

		assertEquals("TestClass", params.getExtension("controller"));
		assertEquals("go", params.getExtension("action"));
	}

	@Test
	public void testDefault() throws Exception {

		PrefixedPlaceholderFormatter phFormatter = new PrefixedPlaceholderFormatter();
		String pathFormat = "/test/testa/myaction?to=:TO";

		BasicUrlParser bp = new BasicUrlParser(pathFormat, phFormatter);
		bp.controller("TestClass").action("go");

		TestParams params = new TestParams();
		params.setMethod("get");
		params.addParams("to", "REST");
		assertTrue(bp.parse(params, "/test/testa/myaction")); //default is get method
		assertEquals("TestClass", params.getExtension("controller"));
		assertEquals("go", params.getExtension("action"));
	}

	@Test
	public void testPost() throws Exception {

		PrefixedPlaceholderFormatter phFormatter = new PrefixedPlaceholderFormatter();
		String pathFormat = "/test/testa/myaction?to=:TO";

		BasicUrlParser bp = new BasicUrlParser(pathFormat, phFormatter);
		bp.controller("TestClass").action("go").onPost();

		TestParams params = new TestParams();
		params.setMethod("get"); //NOTPOST
		params.addParams("to", "REST");
		assertFalse(bp.parse(params, "/test/testa/myaction"));
	}

	@Test
	public void testRestful() throws Exception {

		PrefixedPlaceholderFormatter phFormatter = new PrefixedPlaceholderFormatter();
		String pathFormat = "/test/testa/myaction?to=:TO";

		BasicUrlParser bp = new BasicUrlParser(pathFormat, phFormatter);
		bp.controller("TestClass").action("go").onRestful();

		{
			TestParams params = new TestParams();
			params.setMethod("get");
			params.addParams("to", "REST");
			assertTrue(bp.parse(params, "/test/testa/myaction"));

			assertEquals("TestClass", params.getExtension("controller"));
			assertEquals("go", params.getExtension("action"));
		}

		{
			TestParams params = new TestParams();
			params.setMethod("post");
			params.addParams("to", "REST");
			assertTrue(bp.parse(params, "/test/testa/myaction"));

			assertEquals("TestClass", params.getExtension("controller"));
			assertEquals("go", params.getExtension("action"));
		}
	}

}
