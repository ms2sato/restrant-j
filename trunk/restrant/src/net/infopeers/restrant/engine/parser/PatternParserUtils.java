package net.infopeers.restrant.engine.parser;

import net.infopeers.restrant.engine.PatternInvokerBuilder;
import net.infopeers.restrant.engine.params.EditableParams;

public class PatternParserUtils {

	public static String getMethod(EditableParams params) {
		String method = params.getMethod();
		if(method == null) method = PatternInvokerBuilder.GET;
		return method.toLowerCase();
	}

	
}
