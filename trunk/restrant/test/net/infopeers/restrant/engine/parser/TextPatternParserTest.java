package net.infopeers.restrant.engine.parser;

import junit.framework.TestCase;
import net.infopeers.restrant.TestParams;
import net.infopeers.restrant.engine.PrefixedPlaceholderFormatter;
import net.infopeers.restrant.engine.parser.TextUrlParser;

public class TextPatternParserTest extends TestCase {

	private PrefixedPlaceholderFormatter phFormatter = new PrefixedPlaceholderFormatter();

	public void testNonHttpMethod() throws Exception {

		//フォーマットにHTTPメソッドの指定が無い
		TextUrlParser content = new TextUrlParser(
				"/:controller?content=:content&comment=:comment :action=post",
				phFormatter);
		
		TestParams params = new TestParams();
		params.addParams("content", "testcontent");
		params.addParams("comment", "testcomment");

		//どんなメソッドでコールされても動作する
		{
			params.setMethod("get");
			assertTrue(content.parse(params, "/contents"));
		}
		{
			params.setMethod("post");
			assertTrue(content.parse(params, "/contents"));
		}
		{
			params.setMethod("head");
			assertTrue(content.parse(params, "/contents"));
		}
	}

	public void testWithHttpMethod() throws Exception {

		//フォーマットにHTTPメソッド（@post）が指定されている
		TextUrlParser content = new TextUrlParser(
				"/:controller?content=:content&comment=:comment :action=post @post",
				phFormatter);
		
		TestParams params = new TestParams();
		params.addParams("content", "testcontent");
		params.addParams("comment", "testcomment");

		//POSTでのみ動作する。
		{
			params.setMethod("get");
			assertFalse(content.parse(params, "/contents"));
		}
		{
			params.setMethod("post");
			assertTrue(content.parse(params, "/contents"));
		}
		{
			params.setMethod("head");
			assertFalse(content.parse(params, "/contents"));
		}
	}
	
	public void testCombine() throws Exception{
		TextUrlParser content = new TextUrlParser(
				"/moba/location?datum=:datum&unit=:unit&lat=:lat&lon=:lon :controller=info :action=location @get",
				phFormatter);
		
		TestParams params = new TestParams();
		params.addParams("datum", "DATUM");
		params.addParams("unit", "UNIT");
		params.addParams("lon", "LON");
		params.addParams("lat", "LAT");
		params.setMethod("get");
				
		assertTrue(content.parse(params, "/moba/location"));
	}
	
	public void testSplit() throws Exception{
		TextUrlParser content = new TextUrlParser(
				"/moba/:topic/augps?lat=:lat&lon=:lon :controller=info :action=auGps @get",
				phFormatter);
		
		TestParams params = new TestParams();
		params.addParams("lon", "LON");
		params.addParams("lat", "LAT");
		params.setMethod("get");
				
		assertTrue(content.parse(params, "/moba/TOPIC/augps"));
	}
	
	

}
