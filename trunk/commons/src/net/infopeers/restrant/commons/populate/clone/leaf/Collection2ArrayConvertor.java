/**
 * 
 */
package net.infopeers.restrant.commons.populate.clone.leaf;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;

import net.infopeers.restrant.commons.populate.clone.DefaultConvertor;
import net.infopeers.restrant.commons.populate.clone.Invoker;


public class Collection2ArrayConvertor extends AbstractRecursiveConvertor {

	public Collection2ArrayConvertor(DefaultConvertor parent) {
		super(parent);
	}

	public Object convert(Invoker invoker, int level) {
		if (Collection.class.isAssignableFrom(invoker.getGetterClass())) {
			if (invoker.getSetterClass().isArray()) {
				Object arg = invoker.invokeGetter();
				if (arg == null)
					return null;
				Collection from = (Collection) arg;

				Class componentType = invoker.getSetterClass()
						.getComponentType();

				
				Object[] to = (Object[])invoker.getTargetObject();
				if(to == null){
					to = (Object[]) Array.newInstance(componentType, from
							.size());
				}

				int i = 0;
				for (Iterator itr = from.iterator(); itr.hasNext(); ++i) {
					to[i] = (recur(itr.next(), level));
				}

				return to;
			}
		}
		return null;
	}
}