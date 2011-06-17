package net.infopeers.restrant.sample;

import net.infopeers.restrant.route.RouteMap;

public class Route implements net.infopeers.restrant.route.Route {

	@Override
	public void define(RouteMap map) {
		map.path("/samples/args/:id").controller("samples").action("args").on();
		
		map.path("/jsservice/test/post?to=:to").controller("jsService").action("postGo").onPost().on();
		map.path("/jsservice/test?to=:to").controller("jsService").action("go").on();
		map.path("/jsservice/json?input=:input").controller("json").action("handle").on();
		
		map.path("/restful/:id").controller("restful").action("get").onGet().on();
		map.path("/restful/:id").controller("restful").action("delete").onDelete().on();
		map.path("/restful/:id?:body").controller("restful").action("put").onPut().on();
		map.path("/restful?:body").controller("restful").action("post").onPost().on();
		
		map.path("/:controller/args/:id").action("args").on();
	}

}
