package net.infopeers.restrant.engine.parser;

import java.util.List;

public interface PatternParserWithPathFormat extends PatternParser {

	UrlPathParser getUrlPathParser();
	
	List<String> getHttpMethods();
	
	String getContentType();
}
