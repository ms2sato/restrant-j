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
		
		PrefixedPlaceholderFormatter phFormatter = new PrefixedPlaceholderFormatter();
		Templator t = new Templator(phFormatter);
		StringWriter sw = new StringWriter();
		PrintWriter w = new PrintWriter(sw);
		
		t.appendHeader(w);
		
		String pathFormat = "/test/testa/myaction?to=:TO";
		
		BasicPatternParser bp = new BasicPatternParser(pathFormat, phFormatter);
		t.appendClass(w, TestJsClass.class);
		for(Method m: TestJsClass.class.getMethods()){
			
			if(m.getAnnotation(net.infopeers.restrant.Method.class) == null){
				continue;
			}
			
			t.appendFunction4formtype(w, m.getName(), m, bp);
		}
		t.appendFooter(w);

		System.out.println(sw.toString());
		
	} 
}
