package net.infopeers.restrant.route;

import net.infopeers.restrant.engine.PlaceholderFormatter;
import net.infopeers.restrant.engine.parser.BasicUrlParser;
import net.infopeers.restrant.engine.parser.PatternParser;

public class Mapper extends BasicUrlParser {

	RouteMap routes;
	
	public Mapper(RouteMap routes, String pathFormat, PlaceholderFormatter phFormatter) {
		super(pathFormat, phFormatter);
		this.routes = routes;
	}

	public void on(){
		routes.add(this);
	}
	
	@Override
	public Mapper add(PatternParser parser) {
		super.add(parser);
		return this;
	}

	@Override
	public Mapper withParam(String key, String value) {
		super.withParam(key, value);
		return this;
	}

	@Override
	public Mapper onGet() {
		super.onGet();
		return this;
	}

	@Override
	public Mapper onPost() {
		super.onPost();
		return this;
	}

	@Override
	public Mapper onHead() {
		super.onHead();
		return this;
	}

	@Override
	public Mapper onOptions() {
		super.onOptions();
		return this;
	}

	@Override
	public Mapper onDelete() {
		super.onDelete();
		return this;
	}

	@Override
	public Mapper onPut() {
		super.onPut();
		return this;
	}

	@Override
	public Mapper onRestful() {
		super.onRestful();
		return this;
	}

	@Override
	public Mapper controller(String controller) {
		super.controller(controller);
		return this;
	}

	@Override
	public Mapper action(String action) {
		super.action(action);
		return this;
	}

	public void off(){
		//nop
	}
	
}

