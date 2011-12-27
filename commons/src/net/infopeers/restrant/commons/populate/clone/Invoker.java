package net.infopeers.restrant.commons.populate.clone;

public interface Invoker {

	
	Object invokeGetter();

	/**
	 * @return the getterClass
	 */
	Class getGetterClass();

	/**
	 * @return the setterClass
	 */
	Class getSetterClass();
	
	
	Object getTargetObject();
}