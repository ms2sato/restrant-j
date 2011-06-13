package net.infopeers.restrant.route;

import net.infopeers.restrant.engine.ParserHolder;
import net.infopeers.restrant.engine.PlaceholderFormatter;
import net.infopeers.restrant.engine.parser.PatternParser;

public class RouteMap {

	PlaceholderFormatter phFormatter;
	ParserHolder parserHolder;

	public RouteMap(ParserHolder parserHolder, PlaceholderFormatter phFormatter) {
		this.parserHolder = parserHolder;
		this.phFormatter = phFormatter;
	}

	public Mapper path(String format) {
		return new Mapper(this, format, phFormatter);
	}

	/**
	 * Mapperが呼び出すためのメソッド
	 * 
	 * @param parser
	 */
	void add(PatternParser parser) {
		parserHolder.addParser(parser);
	}

}
