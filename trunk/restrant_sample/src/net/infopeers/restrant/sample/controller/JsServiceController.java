package net.infopeers.restrant.sample.controller;

import java.io.IOException;

import net.infopeers.restrant.BasicController;
import net.infopeers.restrant.Method;

public class JsServiceController extends BasicController{

	@Method({"to"})
	public void go(String to) throws IOException{
		getResponse().getWriter().append(to);
	}
	
	@Method({"to"})
	public void postGo(String to) throws IOException{
		getResponse().getWriter().append(to);
	}
}
