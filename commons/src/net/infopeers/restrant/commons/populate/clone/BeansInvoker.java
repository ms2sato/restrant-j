package net.infopeers.restrant.commons.populate.clone;

public class BeansInvoker implements Invoker {

	private Object from;
	private Class toClass;
	private Object to;
	
	public BeansInvoker(Object from, Class toClass){
		this.from = from;
		this.toClass = toClass;
	}

	public BeansInvoker(Object from, Object to){
		this.from = from;
		this.toClass = to.getClass();
		this.to = to;
	}
	
	public Class getGetterClass() {
		return from.getClass();
	}

	public Class getSetterClass() {
		return toClass;
	}

	public Object invokeGetter() {
		return from;
	}

	public Object getTargetObject() {
		return to;
	}
}
