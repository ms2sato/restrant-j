package net.infopeers.restrant;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Set;

import net.infopeers.restrant.engine.AbstractParams;
import net.infopeers.restrant.engine.gae.GaeExtensionPolicy;

public class TestParams extends AbstractParams {

	private HashMap<String, String> ps = new HashMap<String, String>();
	private String method;

	public TestParams() {
		super(new GaeExtensionPolicy());
	}
	
	@Override
	public String getParameter(String key) {
		return ps.get(key);
	}

	@Override
	public String[] getParameters(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> nameSet() {
		
		Set<String> names = getExtensionNames();
		for(String key: ps.keySet()){
			names.add(key);
		}
		return names;
	}

	public void addParams(String key, String value){
		ps.put(key, value);
	}
	
	public void setMethod(String method){
		this.method = method;
	}
	
	public String getMethod(){
		return method;
	}

	@Override
	public Enumeration<String> getParameterNames() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
