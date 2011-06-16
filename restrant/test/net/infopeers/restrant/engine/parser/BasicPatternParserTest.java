package net.infopeers.restrant.engine.parser;

import junit.framework.TestCase;
import net.infopeers.restrant.engine.PlaceholderFormatter;
import net.infopeers.restrant.engine.PrefixedPlaceholderFormatter;

import org.junit.Test;

public class BasicPatternParserTest extends TestCase {

	private PlaceholderFormatter phFormatter = new PrefixedPlaceholderFormatter();

	public void testNormal() throws Exception {

		TestParams params = new TestParams();
		String format = "/:controller/:action/:id";
		String path = "/con/act/uid";

		PatternParser parser = new BasicPatternParser(format, phFormatter);
		assertTrue(parser.parse(params, path));

		assertEquals("con", params.get("controller"));
		assertEquals("act", params.get("action"));
		assertEquals("uid", params.get("id"));
	}

	public void testNotEnoughParams() throws Exception {

		TestParams params = new TestParams();
		String format = "/:controller/:action/:id";
		String path = "/con/act";

		PatternParser parser = new BasicPatternParser(format, phFormatter);
		assertFalse(parser.parse(params, path));
	}

	public void testFixed() throws Exception {

		TestParams params = new TestParams();
		String format = "/con/:id";
		String path = "/con/uid";

		PatternParser parser = new BasicPatternParser(format, phFormatter).action("act")
				.controller("don");
		assertTrue(parser.parse(params, path));

		assertEquals("don", params.get("controller"));
		assertEquals("act", params.get("action"));
		assertEquals("uid", params.get("id"));
	}

	public void testParams() throws Exception {

		TestParams params = new TestParams();
		String format = "/:controller/:action?uid=:id&uid2=:id2";
		String path = "/con/act";
		params.addParams("uid", "111");
		assertEquals("111", params.get("uid"));
		params.addParams("uid2", "222");
		assertEquals("222", params.get("uid2"));

		PatternParser parser = new BasicPatternParser(format, phFormatter);
		assertTrue(parser.parse(params, path));

		assertEquals("con", params.get("controller"));
		assertEquals("act", params.get("action"));
		assertEquals("111", params.get("id"));
		assertEquals("222", params.get("id2"));

	}

	public void testQuestions() throws Exception {

		TestParams params = new TestParams();
		String format = "/:controller/:action?uid=:id?aaa"; // 「?」が複数
		String path = "/con/act";
		params.addParams("uid", "111");
		assertEquals("111", params.get("uid"));

		try {
			PatternParser parser = new BasicPatternParser(format, phFormatter);
			parser.parse(params, path);
			fail("「?」が複数なら例外");
		} catch (IllegalArgumentException e) {
			// ここに来ればよい
		}
	}

	public void testEnoughParams() throws Exception {

		TestParams params = new TestParams();
		String format = "/:controller/:action?uid=:id&qqq=:id2";
		String path = "/con/act";
		params.addParams("uid", "111");
		assertEquals("111", params.get("uid"));

		// qqqに対応する引数が存在しない

		PatternParser parser = new BasicPatternParser(format, phFormatter);
		assertFalse(parser.parse(params, path));
	}

	public void testNonMatch() throws Exception {

		TestParams params = new TestParams();
		String format = "/:controller/:id";
		String path = "/con/act/111";

		PatternParser parser = new BasicPatternParser(format, phFormatter).action("get");
		assertFalse(parser.parse(params, path));
	}

	public void testRestful() throws Exception {

		TestParams params = new TestParams();
		String format = "/:controller/:id";
		String path = "/con/uid";

		params.addParams("uid", "111");

		params.setMethod("POST");

		PatternParser parser = new BasicPatternParser(format, phFormatter).onRestful();
		assertTrue(parser.parse(params, path));

		assertEquals("con", params.get("controller"));
		assertEquals("post", params.get("action"));
		assertEquals("uid", params.get("id"));

	}

	public void testControllerAndId() throws Exception {

		TestParams params = new TestParams();
		String format = "/:controller?id=:id";
		String path = "/con";

		params.addParams("id", "test");
		params.setMethod("put");

		PatternParser parser = new BasicPatternParser(format, phFormatter).onRestful();
		assertTrue(parser.parse(params, path));

		assertEquals("con", params.get("controller"));
		assertEquals("put", params.get("action"));
		assertEquals("test", params.get("id"));

	}
	
	@Test
	public void senarioTestGet() throws Exception {

		PrefixedPlaceholderFormatter phFormatter = new PrefixedPlaceholderFormatter();
		String pathFormat = "/test/testa/myaction?to=:TO";

		BasicPatternParser bp = new BasicPatternParser(pathFormat, phFormatter);
		bp.controller("TestClass").action("go").onGet();

		TestParams params = new TestParams();
		params.addParams("to", "REST");
		assertTrue(bp.parse(params, "/test/testa/myaction"));

		assertEquals("TestClass", params.getExtension("controller"));
		assertEquals("go", params.getExtension("action"));
	}

	@Test
	public void senarioTestDefault() throws Exception {

		PrefixedPlaceholderFormatter phFormatter = new PrefixedPlaceholderFormatter();
		String pathFormat = "/test/testa/myaction?to=:TO";

		BasicPatternParser bp = new BasicPatternParser(pathFormat, phFormatter);
		bp.controller("TestClass").action("go");

		TestParams params = new TestParams();
		params.setMethod("get");
		params.addParams("to", "REST");
		assertTrue(bp.parse(params, "/test/testa/myaction")); //default is get method
		assertEquals("TestClass", params.getExtension("controller"));
		assertEquals("go", params.getExtension("action"));
	}

	@Test
	public void senarioTestPost() throws Exception {

		PrefixedPlaceholderFormatter phFormatter = new PrefixedPlaceholderFormatter();
		String pathFormat = "/test/testa/myaction?to=:TO";

		BasicPatternParser bp = new BasicPatternParser(pathFormat, phFormatter);
		bp.controller("TestClass").action("go").onPost();

		TestParams params = new TestParams();
		params.setMethod("get"); //NOTPOST
		params.addParams("to", "REST");
		assertFalse(bp.parse(params, "/test/testa/myaction"));
	}

	@Test
	public void senarioTestRestful() throws Exception {

		PrefixedPlaceholderFormatter phFormatter = new PrefixedPlaceholderFormatter();
		String pathFormat = "/test/testa/myaction?to=:TO";

		BasicPatternParser bp = new BasicPatternParser(pathFormat, phFormatter);
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
