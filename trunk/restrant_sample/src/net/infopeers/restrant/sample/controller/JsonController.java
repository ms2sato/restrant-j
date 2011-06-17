package net.infopeers.restrant.sample.controller;

import java.io.IOException;

import net.infopeers.restrant.BasicController;
import net.infopeers.restrant.Method;

public class JsonController extends BasicController {

	@Method({ "input" })
	public void handle(String input) throws IOException {

		getResponse().addHeader("Content-Type", "application/json");
		getResponse().getWriter().print("{\"msg\": \"message is " + input + "\"}");

	}
}
