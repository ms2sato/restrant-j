package net.infopeers.restrant.engine.jsservice;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;

import net.infopeers.restrant.engine.PrefixedPlaceholderFormatter;
import net.infopeers.restrant.engine.parser.BasicPatternParser;

import org.junit.Test;

public class JsServiceInvokerBuilderFactoryTest {

	@Test
	public void senario() throws Exception{
		
		Templator t = new Templator();
		StringWriter sw = new StringWriter();
		PrintWriter w = new PrintWriter(sw);
		
		t.appendHeader(w);
		
		PrefixedPlaceholderFormatter phFormatter = new PrefixedPlaceholderFormatter();
		String pathFormat = "/test/testa/myaction?to=:TO";
		
		BasicPatternParser bp = new BasicPatternParser(pathFormat, phFormatter);
		t.appendClass(w, TestJsClass.class);
		for(Method m: TestJsClass.class.getMethods()){
			
			if(m.getAnnotation(net.infopeers.restrant.Method.class) == null){
				continue;
			}
			
			t.appendFunction(w, m, bp);
		}
		t.appendFooter(w);

		System.out.println(sw.toString());
		
	} 
}
