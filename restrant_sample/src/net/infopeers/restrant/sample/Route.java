package net.infopeers.restrant.sample;

import net.infopeers.restrant.route.RouteMap;

public class Route implements net.infopeers.restrant.route.Route {

	@Override
	public void define(RouteMap map) {
		map.path("/:controller/args/:id").action("post").on();
	}

}
