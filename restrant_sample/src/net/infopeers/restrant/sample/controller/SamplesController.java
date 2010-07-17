package net.infopeers.restrant.sample.controller;

import java.io.PrintWriter;

import net.infopeers.restrant.BasicController;
import net.infopeers.restrant.Method;
import net.infopeers.restrant.Params;
import net.infopeers.restrant.Request;

/**
 * samples/
 */
public class SamplesController extends BasicController{

	@Request
	public String test; 

	// samples/args/:id
	@Method({"id"})
	public void args(long id) throws Exception{
		getResponse().setContentType("text/plain");
		
		PrintWriter w = getResponse().getWriter();
		w.println("ARGS\n" + test + "\n");

		Params params = getParams();
		for(String name:params.nameSet()){
			w.println(name + ":" + params.get(name) + "\n");
		}
	}
	
	
	// samples/:id
	public void get() throws Exception{
		getResponse().setContentType("text/plain");
		
		PrintWriter w = getResponse().getWriter();
		w.println("GET\n" + test + "\n");

		Params params = getParams();
		for(String name:params.nameSet()){
			w.println(name + ":" + params.get(name) + "\n");
		}
	}
	
	// samples/
	public void post() throws Exception{
		getResponse().setContentType("text/plain");
		
		PrintWriter w = getResponse().getWriter();
		w.println("POST\n" + test + "\n");

		Params params = getParams();
		for(String name:params.nameSet()){
			w.println(name + ":" + params.get(name) + "\n");
		}
	}

	// samples/:id
	@Method({"id"})
	public void put(String id) throws Exception{
		getResponse().setContentType("text/plain");
		
		PrintWriter w = getResponse().getWriter();
		w.println("PUT\n" + test + "\n");

		Params params = getParams();
		for(String name:params.nameSet()){
			w.println(name + ":" + params.get(name) + "\n");
		}

	}
	
	// samples/:id
	public void delete() throws Exception{
		getResponse().setContentType("text/plain");
		
		PrintWriter w = getResponse().getWriter();
		w.println("DELETE\n" + test + "\n");

		Params params = getParams();
		for(String name:params.nameSet()){
			w.println(name + ":" + params.get(name) + "\n");
		}
	}

	// samples/?param=xxx&param=yyy
	public void search() throws Exception{
		getResponse().setContentType("text/plain");
		
		PrintWriter w = getResponse().getWriter();
		w.println("SEARCH\n" + test + "\n");

		Params params = getParams();
		for(String name:params.nameSet()){
			w.println(name + ":" + params.get(name) + "\n");
		}
	}

}
