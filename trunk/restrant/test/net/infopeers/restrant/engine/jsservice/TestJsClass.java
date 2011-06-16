package net.infopeers.restrant.engine.jsservice;

import net.infopeers.restrant.Method;

public class TestJsClass {

	/**
	 * target of generate
	 */
	@Method()
	public void go(){
		
	}

	/**
	 * getter is ignored.
	 */
	public String getValue(){
		return null;
	}
	
	
	/**
	 * setter is ignored.
	 */
	public void setValue(String value){
	}

	/**
	 * target of generate
	 */
	@Method({"aaa"})
	public void go2(String abc){
		
	}
	
}
