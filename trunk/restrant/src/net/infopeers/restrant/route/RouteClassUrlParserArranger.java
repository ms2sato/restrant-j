package net.infopeers.restrant.route;

import net.infopeers.restrant.engine.InvokerBuilder;
import net.infopeers.restrant.engine.PlaceholderFormatter;
import net.infopeers.restrant.engine.parser.UrlParserArranger;

public class RouteClassUrlParserArranger implements UrlParserArranger {

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
			throw new RuntimeException(cls.getName() + "は"
					+ Route.class.getName() + "を実装ていなければならない");
		}
		
		route = (Route)obj;
	}

	@Override
	public void arrange(InvokerBuilder invokerBuilder) {
		RouteMap routes = new RouteMap(invokerBuilder, phFormatter);
		route.define(routes);
	}

}
