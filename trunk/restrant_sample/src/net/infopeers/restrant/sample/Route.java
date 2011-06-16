package net.infopeers.restrant.sample;

import net.infopeers.restrant.route.RouteMap;

public class Route implements net.infopeers.restrant.route.Route {

	@Override
	public void define(RouteMap map) {
		map.path("/samples/args/:id").controller("samples").action("post").on();
		map.path("/jsservice/test/post?to=:to").controller("jsService").action("postGo").onPost().on();
		map.path("/jsservice/test?to=:to").controller("jsService").action("go").on();
		//map.path("/:controller/args/:id").action("post").on();
	}

}
