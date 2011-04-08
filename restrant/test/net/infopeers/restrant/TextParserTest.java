package net.infopeers.restrant;

import net.infopeers.restrant.engine.TextParser;
import net.infopeers.restrant.engine.Parser;
import net.infopeers.restrant.engine.DefaultPlaceholderFormatter;
import net.infopeers.restrant.engine.PlaceholderFormatter;
import junit.framework.TestCase;

public class TextParserTest extends TestCase {
	
	private PlaceholderFormatter phFormatter = new DefaultPlaceholderFormatter();

	public void testNormal() throws Exception{
		
		TestParams params = new TestParams();
		String format = "/:controller/:action/:id";
		String path = "/con/act/uid";

		Parser parser = new TextParser(format, phFormatter);
		assertTrue(parser.parse(params, path));
		
		assertEquals("con", params.get("controller"));
		assertEquals("act", params.get("action"));
		assertEquals("uid", params.get("id"));
	}

	public void testNotEnoughParams() throws Exception{
		
		TestParams params = new TestParams();
		String format = "/:controller/:action/:id";
		String path = "/con/act";
		
		Parser parser = new TextParser(format, phFormatter);
		assertFalse(parser.parse(params, path));
	}
	
	
	public void testFixed() throws Exception{
		
		TestParams params = new TestParams();
		String format = "/con/:id :action=act 	:controller=don";
		String path = "/con/uid";
		
		Parser parser = new TextParser(format, phFormatter);
		assertTrue(parser.parse(params, path));
		
		assertEquals("don", params.get("controller"));
		assertEquals("act", params.get("action"));
		assertEquals("uid", params.get("id"));
	}

	public void testParams() throws Exception{

		TestParams params = new TestParams();
		String format = "/:controller/:action?uid=:id&uid2=:id2";
		String path = "/con/act";
		params.addParams("uid", "111");
		assertEquals("111", params.get("uid"));
		params.addParams("uid2", "222");
		assertEquals("222", params.get("uid2"));
		
		Parser parser = new TextParser(format, phFormatter);
		assertTrue(parser.parse(params, path));
		
		assertEquals("con", params.get("controller"));
		assertEquals("act", params.get("action"));
		assertEquals("111", params.get("id"));
		assertEquals("222", params.get("id2"));
		
	}

	public void testQuestions() throws Exception{

		TestParams params = new TestParams();
		String format = "/:controller/:action?uid=:id?aaa"; //「?」が複数
		String path = "/con/act";
		params.addParams("uid", "111");
		assertEquals("111", params.get("uid"));
		
		try{
			Parser parser = new TextParser(format, phFormatter);
			parser.parse(params, path);
			fail("「?」が複数なら例外");
		}catch(IllegalArgumentException e){
			//ここに来ればよい
		}
	}
	
	public void testEnoughParams() throws Exception{

		TestParams params = new TestParams();
		String format = "/:controller/:action?uid=:id&qqq=:id2";
		String path = "/con/act";
		params.addParams("uid", "111");
		assertEquals("111", params.get("uid"));
		
		//qqqに対応する引数が存在しない
		
		Parser parser = new TextParser(format, phFormatter);
		assertFalse(parser.parse(params, path));
	}

	public void testNonMatch() throws Exception{

		TestParams params = new TestParams();
		String format = "/:controller/:id :action=get";
		String path = "/con/act/111";

		Parser parser = new TextParser(format, phFormatter);
		assertFalse(parser.parse(params, path));
	}
	
	public void testRestful() throws Exception{
		
		TestParams params = new TestParams();
		String format = "/:controller/:id @restful";
		String path = "/con/uid";

		params.addParams("uid", "111");
		
		params.setMethod("POST");
		
		Parser parser = new TextParser(format, phFormatter);
		assertTrue(parser.parse(params, path));
		
		assertEquals("con", params.get("controller"));
		assertEquals("post", params.get("action"));
		assertEquals("uid", params.get("id"));
		
	}

	public void testControllerAndId() throws Exception{
		
		TestParams params = new TestParams();
		String format = "/:controller?id=:id :action=put";
		String path = "/con";

		params.addParams("id", "test");
		
		Parser parser = new TextParser(format, phFormatter);
		assertTrue(parser.parse(params, path));
		
		assertEquals("con", params.get("controller"));
		assertEquals("put", params.get("action"));
		assertEquals("test", params.get("id"));
		
	}
}
