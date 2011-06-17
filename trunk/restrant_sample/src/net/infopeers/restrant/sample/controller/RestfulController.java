package net.infopeers.restrant.sample.controller;

import java.io.PrintWriter;

import net.infopeers.restrant.BasicController;
import net.infopeers.restrant.Method;
import net.infopeers.restrant.Params;

public class RestfulController extends BasicController {

	@Method({"id"})
	public void get(String id) throws Exception {
		PrintWriter w = getResponse().getWriter();
		w.println("GET\n" + id + "\n");
	}

	@Method({"body"})
	public void post(String body) throws Exception {
		PrintWriter w = getResponse().getWriter();
		w.println("POST\n" + body + "\n");
	}

	@Method({ "id", "body" })
	public void put(String id, String body) throws Exception {
		PrintWriter w = getResponse().getWriter();
		w.println("PUT\n" + id + "\n" + body);
	}

	@Method({ "id" })
	public void delete(String id) throws Exception {
		PrintWriter w = getResponse().getWriter();
		w.println("DELETE\n" + id + "\n");
	}

	public void search() throws Exception {
		PrintWriter w = getResponse().getWriter();
		w.println("SEARCH\n");

		Params params = getParams();
		for (String name : params.nameSet()) {
			w.println(name + ":" + params.get(name) + "\n");
		}
	}

}
