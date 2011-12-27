package net.infopeers.restrant.commons.populate.clone.leaf;

import java.util.Collection;

import net.infopeers.restrant.commons.populate.Populator;
import net.infopeers.restrant.commons.populate.clone.BeansInvoker;
import net.infopeers.restrant.commons.populate.clone.Convertor;
import net.infopeers.restrant.commons.populate.clone.DefaultConvertor;


public abstract class AbstractRecursiveConvertor implements Convertor{

	protected DefaultConvertor parent;

	public AbstractRecursiveConvertor(DefaultConvertor parent) {
		this.parent = parent;
	}

	protected final Object recur(Object obj, int level, Object to) {
		if (obj == null)
			return null;
	
		BeansInvoker invoker;
		if (to == null) {
			invoker = new BeansInvoker(obj, Object.class);
		} else {
			invoker = new BeansInvoker(obj, to);
		}
	
		Object res = parent.convert(invoker, level);
		return (res == Populator.INVALID_OBJECT) ? null : res;
	}

	protected final Object recur(Object obj, int level) {
		return recur(obj, level, null);
	}
	
	protected final Collection createCollection(Class targetClass){
		return parent.createCollection(targetClass);
	}

}