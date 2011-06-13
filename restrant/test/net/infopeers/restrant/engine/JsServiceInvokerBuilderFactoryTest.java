package net.infopeers.restrant.engine;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;

import net.infopeers.restrant.engine.parser.UrlPathParser;

import org.junit.Test;

public class JsServiceInvokerBuilderFactoryTest {

	@Test
	public void senario() throws Exception{
		
		JsServiceInvokerBuilderFactory.Templator t = new JsServiceInvokerBuilderFactory.Templator();
		StringWriter sw = new StringWriter();
		PrintWriter w = new PrintWriter(sw);
		
		PrefixedPlaceholderFormatter phFormatter = new PrefixedPlaceholderFormatter();
		String pathFormat = "/test/testa/myaction";
		
		UrlPathParser pp = new UrlPathParser(phFormatter, pathFormat);
		for(Method m: TestJsClass.class.getMethods()){
			t.appendFunction(w, m, pp);
		}
		System.out.println(sw.toString());
		
	} 
}
