package net.infopeers.restrant.commons.populate;

import net.infopeers.restrant.commons.populate.clone.BeansInvoker;
import net.infopeers.restrant.commons.populate.clone.DefaultConvertor;

/**
 * BeanÇÃílÇÃà⁄êAäÌ
 */
class PopulatorImpl implements Populator {

	private DefaultConvertor traits;

	PopulatorImpl(DefaultConvertor traits) {
		this.traits = traits;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jp.co.scio.syare.util.populate.Populator#clone(java.lang.Class,
	 *      java.lang.Object)
	 */
	public Object clone(Class type, Object from) {

		if(type == null) throw new IllegalArgumentException("type == null");
		if(from == null) throw new IllegalArgumentException("from == null");
		
		BeansInvoker invoker = new BeansInvoker(from, type);
		return traits.convert(invoker, 0);
	}

	public void populate(Object from, Object to) {
		if(from == null) throw new IllegalArgumentException("from == null");
		if(to == null) throw new IllegalArgumentException("to == null");
		
		BeansInvoker invoker = new BeansInvoker(from, to);
		traits.convert(invoker, 0);
	}
	
	

}
