package net.infopeers.restrant.route;

import net.infopeers.restrant.engine.InvokerBuilder;
import net.infopeers.restrant.engine.PlaceholderFormatter;
import net.infopeers.restrant.engine.parser.UrlParser;

public class RouteMap {

	PlaceholderFormatter phFormatter;
	InvokerBuilder invokerBuilder;

	public RouteMap(InvokerBuilder invokerBuilder, PlaceholderFormatter phFormatter) {
		this.invokerBuilder = invokerBuilder;
		this.phFormatter = phFormatter;
	}

	public Mapper format(String format) {
		return new Mapper(this, format, phFormatter);
	}

	/**
	 * Mapperが呼び出すためのメソッド
	 * 
	 * @param parser
	 */
	void add(UrlParser parser) {
		invokerBuilder.addParser(parser);
	}

}
