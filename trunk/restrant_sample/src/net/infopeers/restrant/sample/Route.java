package net.infopeers.restrant.sample;

import net.infopeers.restrant.route.RouteMap;

public class Route implements net.infopeers.restrant.route.Route {

	@Override
	public void define(RouteMap map) {
		map.path("/samples/args/:id").controller("samples").action("args").on();
		
		map.path("/jsservice/test/post?to=:to").controller("jsService").action("postGo").onPost().on();
		map.path("/jsservice/test?dummy=test&to=:to").controller("jsService").action("goWithDummy").on();
		map.path("/jsservice/test?to=:to").controller("jsService").action("go").on();
		map.path("/jsservice/json?input=:input").controller("json").action("handle").on();
		
		map.path("/restful/:id").controller("restful").action("getIt").onGet().on();
		map.path("/restful/:id").controller("restful").action("deleteIt").onDelete().on();
		map.path("/restful/:id?:body").controller("restful").action("put").onType("application/json").onPut().on();
		map.path("/restful?:body").controller("restful").action("post").onType("application/json").onPost().on();
		
		map.path("/:controller/args/:id").action("args").on();
	}

}
