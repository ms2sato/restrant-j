package net.infopeers.restrant.sample.controller;

import java.io.InputStream;
import java.io.PrintWriter;

import net.infopeers.commons.io.StreamUtils;
import net.infopeers.restrant.BasicController;
import net.infopeers.restrant.Method;
import net.infopeers.restrant.Params;

public class RestfulController extends BasicController {

	@Method(name="getIt", args={"id"})
	public void get(String id) throws Exception {
		PrintWriter w = getResponse().getWriter();
		w.println("GET\n" + id + "\n");
	}

	@Method({"body"})
	public void post(InputStream is) throws Exception {
		//contents body can get as InputStream.
		PrintWriter w = getResponse().getWriter();
		w.println("POST\n" + StreamUtils.toString(is) + "\n");
	}

	@Method({ "id", "body" })
	public void put(String id, String body) throws Exception {
		//contents body can get as String too.
		PrintWriter w = getResponse().getWriter();
		w.println("PUT\n" + id + "\n" + body);
	}

	@Method(name="deleteIt", args={ "id" })
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
