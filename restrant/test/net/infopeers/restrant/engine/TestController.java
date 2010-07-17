package net.infopeers.restrant.engine;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.infopeers.restrant.Controller;
import net.infopeers.restrant.ControllerServlet;
import net.infopeers.restrant.Method;
import net.infopeers.restrant.Params;

public class TestController implements Controller {
	
	public boolean isTestCalled;
	
	public long testAnnotatedId;

	public String testAnnotatedLabel;

	public long namedMethodId;

	public String namedMethodLabel;

	public Integer[] arrayValues;
	
	

	@Override
	public void setParams(Params params) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setRequest(HttpServletRequest request) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setResponse(HttpServletResponse response) {
		// TODO Auto-generated method stub

	}

	public void test(){
		isTestCalled = true;
	}

	@Method({"id"})
	public void testAnnotated(long id){
		testAnnotatedId = id;
	}

	@Method({"id", "label"})
	public void testAnnotated(long id, String label){
		testAnnotatedId = id;
		testAnnotatedLabel = label;
	}

	@Method(name="namedMethod", args={"id"})
	public void testNamed(long id){
		namedMethodId = id;
	}

	@Method(name="namedMethod", args={"id", "label"})
	public void testNamed(long id, String label){
		namedMethodId = id;
		namedMethodLabel = label;
	}
	
	@Method("values")
	public void testArray(Integer[] values){
		arrayValues = values;
	}

	@Override
	public void afterInvoke(String method) {
	}

	@Override
	public boolean beforeInvoke(String method) {
		return true;
	}

	@Override
	public void setServlet(ControllerServlet servlet) {
		// TODO Auto-generated method stub
		
	}
	
}
