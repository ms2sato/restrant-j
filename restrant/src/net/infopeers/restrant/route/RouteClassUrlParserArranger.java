package net.infopeers.restrant.route;

import net.infopeers.restrant.engine.ParserHolder;
import net.infopeers.restrant.engine.PlaceholderFormatter;
import net.infopeers.restrant.engine.parser.PatternParserArranger;

public class RouteClassUrlParserArranger implements PatternParserArranger {

	private Route route;
	PlaceholderFormatter phFormatter;
	
	public RouteClassUrlParserArranger(PlaceholderFormatter phFormatter, Class<?> cls){
		this.phFormatter = phFormatter;
		
		Object obj;
		try {
			obj = cls.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		if (!(obj instanceof Route)) {
			throw new RuntimeException(cls.getName() + " must implements "
					+ Route.class.getName());
		}
		
		route = (Route)obj;
	}

	@Override
	public void arrange(ParserHolder parserHolder) {
		RouteMap routes = new RouteMap(parserHolder, phFormatter);
		route.define(routes);
	}

}
